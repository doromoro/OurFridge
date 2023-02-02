import React from 'react';

import { Background, LoadingText } from './LoadingCss';
import SpinnerEmoji from './spinner.gif';

const Loading = ({message}) => {
  return (
    <div>
      <Background>
        <LoadingText>잠시만 기다려 주세요. {message}</LoadingText>
        <img src={SpinnerEmoji} alt="Loading..." width="5%" />
      </Background>
    </div>
  )
}

export default Loading;