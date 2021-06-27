package com.rossyoung.anonvoting.controller;

import com.rossyoung.anonvoting.election.ElectionRepository;
import com.rossyoung.anonvoting.election.ElectionService;
import com.rossyoung.anonvoting.model.Ballot;
import com.rossyoung.anonvoting.model.ElectionResult;
import com.rossyoung.anonvoting.model.WebSocketSession;
import com.rossyoung.anonvoting.model.WebSocketType;
import com.rossyoung.anonvoting.player.PlayerRepository;
import com.rossyoung.anonvoting.player.PlayerService;
import com.rossyoung.anonvoting.repository.WebSocketSessionRepository;
import com.rossyoung.anonvoting.vote.Vote;
import com.rossyoung.anonvoting.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ElectionWebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;
    private final ElectionService electionService;
    private final WebSocketSessionRepository webSocketSessionRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;
    private final static Map<String, Long> electionNotification = new HashMap<>();

    @Autowired
    public ElectionWebSocketController(SimpMessagingTemplate simpMessagingTemplate, VoteRepository voteRepository,
                                       ElectionRepository electionRepository,
                                       ElectionService electionService, WebSocketSessionRepository webSocketSessionRepository, PlayerRepository playerRepository, PlayerService playerService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
        this.electionService = electionService;
        this.webSocketSessionRepository = webSocketSessionRepository;
        this.playerRepository = playerRepository;
        this.playerService = playerService;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) throws Exception {
        MessageHeaders headers = event.getMessage().getHeaders();
        Map<String, Object> nativeHeaderMap = (Map<String, Object>) headers.get("nativeHeaders");
        assert nativeHeaderMap != null;
        String username = getValueFromNativeHeader(nativeHeaderMap, "username");
        WebSocketType webSocketType = WebSocketType.valueOf(getValueFromNativeHeader(nativeHeaderMap, "webSocketType"));
        Principal principal = Objects.requireNonNull(event.getUser());
        if(playerRepository.existsById(username) && (webSocketType.equals(WebSocketType.FindElection) || webSocketType.equals(WebSocketType.Vote))) {
            webSocketSessionRepository.save(new WebSocketSession(username, principal.getName(), webSocketType, LocalDateTime.now()));
            deleteOldWebSocketSessions(webSocketSessionRepository.findByUsernameAndWebSocketType(username, webSocketType));
        }
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        Principal principal = Objects.requireNonNull(event.getUser());
        if(webSocketSessionRepository.existsById(principal.getName())) {
            webSocketSessionRepository.deleteById(principal.getName());
        }
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) throws Exception {
        GenericMessage message = (GenericMessage) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");
        assert simpDestination != null;
        if (simpDestination.startsWith("/topic/election")) {
            Long electionId = Long.parseLong(simpDestination.substring(simpDestination.lastIndexOf('/')+1));
            ElectionResult electionResult = getElectionResults(electionId);
            simpMessagingTemplate.convertAndSend("/topic/election/" + electionId, electionResult);
        } else if(simpDestination.startsWith("/user/queue/election")) {
            Principal principal = Objects.requireNonNull(event.getUser());
            String username = webSocketSessionRepository.findByPrincipalNameAndWebSocketType(principal.getName(), WebSocketType.FindElection).get(0).getUsername();
            if(electionNotification.containsKey(username)) {
                if(electionRepository.existsById(electionNotification.get(username))) {
                    simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/election", electionNotification.get(username));
                }
                electionNotification.remove(username);
            }
        }
    }

    @MessageMapping("/election/vote")
    public void vote(Ballot ballot, Principal principal) throws Exception {
        if (webSocketSessionRepository.findByUsernameAndWebSocketType(ballot.getUsername(), WebSocketType.Vote).
                stream().anyMatch(ws -> ws.getPrincipalName().equals(principal.getName()))
                && voteRepository.findVoteByUsername(ballot.getUsername())
                .map(v -> v.getElectionId().equals(ballot.getElectionId())).orElse(false)) {
            deleteOldWebSocketSessions(webSocketSessionRepository.findByUsernameAndWebSocketType(ballot.getUsername(), WebSocketType.Vote));
            if(voteRepository.findVoteByUsername(ballot.getUsername()).map(Vote::getHasVoted).orElse(true)) {
                throw new Exception(String.format("Player has already voted: `%s`", ballot.getUsername()));
            }
            voteRepository.updateVoteStatus(ballot.getUsername());
            if (ballot.getVote().equals(Ballot.Vote.YES)) {
                electionRepository.updateYesVotes(ballot.getElectionId());
            } else if (ballot.getVote().equals(Ballot.Vote.NO)) {
                electionRepository.updateNoVotes(ballot.getElectionId());
            } else {
                throw new Exception(String.format("Invalid vote in ballot: `%s`", ballot.getVote()));
            }
            ElectionResult electionResult = getElectionResults(ballot.getElectionId());
            simpMessagingTemplate.convertAndSend("/topic/election/" + ballot.getElectionId(), electionResult);
        } else {
            throw new Exception(String.format("User cannot vote in this election: `%s`", ballot.getUsername()));
        }
    }

    @MessageMapping("/election/{electionId}")
    public void addPlayerToElection(String username, @DestinationVariable Long electionId) {
        electionService.addPlayerToElection(username, electionId);
        if (!webSocketSessionRepository.existsByUsernameAndWebSocketType(username, WebSocketType.FindElection)) {
            electionNotification.put(username, electionId);
        } else {
            List<WebSocketSession> webSocketSessionList = webSocketSessionRepository.findByUsernameAndWebSocketType(username, WebSocketType.FindElection);
            String principalName = webSocketSessionList.get(0).getPrincipalName();
            simpMessagingTemplate.convertAndSendToUser(principalName, "/queue/election", electionId);
        }
    }

    ElectionResult getElectionResults(Long electionId) {
        ElectionResult electionResult = new ElectionResult();
        electionResult.setVotingActive(electionService.isVotingActive(electionId));
        electionResult.setYesVotes(electionService.getYesVotes(electionId));
        electionResult.setNoVotes(electionService.getNoVotes(electionId));
        return electionResult;
    }

    String getValueFromNativeHeader(Map<String, Object> nativeHeaderMap, String key) throws Exception {
        if(!nativeHeaderMap.containsKey(key)) {
            throw new Exception(String.format("Native Header does not contain key: `%s`", key));
        }
        List<String> keyList = (List<String>) nativeHeaderMap.get(key);
        if(keyList.isEmpty()) {
            throw new Exception(String.format("Value for `%s` in native header was empty", key));
        }
        return keyList.get(0);
    }

    void deleteOldWebSocketSessions(List<WebSocketSession> webSocketSessionList) {
        if(webSocketSessionList.size() > 1) {
            webSocketSessionList.sort(Comparator.comparing(WebSocketSession::getCreationTime));
            final String principalName = webSocketSessionList.get(webSocketSessionList.size()-1).getPrincipalName();
            webSocketSessionList.forEach(ws -> {
                if (!ws.getPrincipalName().equals(principalName)) {
                    webSocketSessionRepository.deleteById(ws.getPrincipalName());
                }
            });
        }
    }
}
