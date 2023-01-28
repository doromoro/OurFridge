import React from 'react'
import { setAccessToken, getAccessToken, removeAccessToken } from '../store/Cookie'
import axios from 'axios'

//이 컴포넌트는 로그인을 체크하고 로그인이 되어있지 않다면 후속 처리를,
//토큰이 있지만 서버에서 유효한 토큰이 아니라면 재발급 절차를 받는 api를 담는다.
export const CheckLogin = () => {
  //토큰이 존재한다면 login상태임을 local에 다시 작성?하고
  console.log("accessToken check", getAccessToken())
  if(getAccessToken() !== undefined) {
    localStorage.setItem('isLoggined', true);
    return;
  }
  //엑세스 토큰이 존재하지 않는다면 리프레쉬 토큰으로 reissue를 해야할 것 같은데..
  //또는 email과 pw로 다시 로그인 api를 호출하던가 일단 그냥 테스트해보고 인터셉터도 어차피 구현해야함.
  // 그냥 하.. 로컬스토리지에도 토큰을 저장해버리고 올려치는 방식으로 해야겠음.
  else {
    localStorage.setItem('isloggined', false);
    let token = localStorage.getItem('access');
    axios({
      method : 'post',
      url : 'https://localhost:8080/reissue',
      header : {
        "Authorization" : `Bearer ${token}`
      },
      // data: {},
      withCredentials : true,
    }).then(response => {
      console.log("전체 데이터",response.data);
      console.log("헤더",response.headers);
      console.log("access", response.data.data.accessToken);
      console.log("refresh", response.data.data.refreshToken);
      const accessToken = response.data.data.accessToken;
      
      //쿠키가 덮어쓰기가 되는지 확인좀 해봐야할듯 안되면 지우고 만드는 동기적인 코드를 만들어야함.
      setAccessToken(`${accessToken}`);
      localStorage.setItem('isLoggined', true);
      localStorage.setItem('access', accessToken);
    }).catch(function(error) {
      console.log(error.config);
    })
  }

  return (
    <div>

    </div>
  )
}

