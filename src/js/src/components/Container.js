import React from 'react';

const Container = props =>  {
    if(props.size <= 500) {
        return (
            <div style={{width: '80%', margin: '0 auto', textAlign: 'center', marginTop: '2%'}}>
                {props.children}
            </div>
        )
    } else if(props.size <= 1000) {
        return (
            <div style={{width: '50%', margin: '0 auto', textAlign: 'center', marginTop: '2%'}}>
                {props.children}
            </div>
        )
    }

    return (
        <div style={{width: '20%', margin: '0 auto', textAlign: 'center', marginTop: '2%'}}>
            {props.children}
        </div>
    )
}

export default Container