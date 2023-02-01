import React from 'react';
import { Navigate } from 'react-router-dom';
import { getAccessToken, removeAccessToken } from '../../store/Cookie';
import axios from 'axios';



// 서버로는 토큰들을 전송 서버에 성공적으로 전달해서 res받으면
// 내부적으로는 localStorage에 isLoggined = false, logout = Data.now()
// 토큰 삭제(removeAccessToken), navigate까지
const Logout = () => {

  const accessToken = getAccessToken();

  axios ({
    method : 'post',
    url : 'https://localhost:8080/logout',
    headers: {
      "Content-Type" : 'application/json',
      "Authorization" : `Bearer ${accessToken}`
    },
    withCredentials : true
  }).then(res => {
    console.log("res", res);
    console.log("data", res.data);
    if(res.data.message === "잘못된 요청입니다.") {
      alert("로그인이 유효하지 않습니다.");
      
    }else {
      alert("로그아웃 되었습니다.");
    }
    localStorage.setItem('isLoggined', "false");
    localStorage.setItem('logout', Date.now());
    removeAccessToken();
    // return navigate('/');
  }).catch(function(error) {
    if (error.response) {
      // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
      console.log(error.response.data);
      console.log(error.response.status);
      console.log(error.response.headers);
    }
    else if (error.request) {
      // 요청이 이루어 졌으나 응답을 받지 못했습니다.
      // `error.request`는 브라우저의 XMLHttpRequest 인스턴스 또는
      // Node.js의 http.ClientRequest 인스턴스입니다.
      console.log(error.request);
    }
    else {
      // 오류를 발생시킨 요청을 설정하는 중에 문제가 발생했습니다.
      console.log('Error', error.message);
    }
    console.log(error.config);

    // 로그아웃은 오류가 나도 그냥 로그아웃으로 처리해야 하는가??
    return alert("로그아웃 중 오류 발생");
    // return navigate('/');
  })

  return (
    <>
      <Navigate to='/' {...alert("로그아웃 되었습니다.")}/>
    </>
  )
}

export default Logout;