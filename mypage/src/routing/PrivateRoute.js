import React from 'react';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { Navigate } from 'react-router-dom';
import { getAccessToken, removeAccessToken } from '../store/Cookie';

const PrivateRoute = ({ authenticated, component: Component }) => {

  const [doubleCheck, setDoubleCheck] = useState();

  //check login(토큰이 유효한지 확인하는 작업)
  useEffect(() => {
    
    const token = getAccessToken();

    if(token) {
      axios({
        method: 'get',
        url : 'https://localhost:8080/test',
        headers : {
          "Content-Type": 'application/json',
          'Access-Control-Allow-Origin': 'https://localhost:8080',
          'Authorization' : `Bearer ${token}`
        },
        withCredentials : true,
      }).then(res => {
        console.log("res", res);
        console.log("data", res.data);
        //반응이 어케 오는지 모르겠음 일단은
        //1. 토큰이 존재하지만 유효하지 않은 경우
        //reissue api호출 해야겠는데 위에 const reissue = () => {} 만들기


        //2. 토큰이 존재하고 유효한 경우
        // setDoubleCheck(true);
      }).catch(function(error) {
        if(error.response) {
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

        //이러면 서버가 모르는데 로그아웃한지
        alert("로그인 여부에 오류가 있습니다.");
        localStorage.setItem('isLoggined', false);
        localStorage.setItem('logout', Date.now());
        removeAccessToken();
        setDoubleCheck(false);
      });
    } // end of if(token)
    //토큰이 존재하지 않는다면 이것도 로그아웃 api호출을 하긴 했어야 할거같음.
    else {
      alert("유효하지 않은 로그인 정보입니다.");
      localStorage.setItem('isLoggined', false);
      localStorage.setItem('logout', Date.now());
      setDoubleCheck(false);
    }
  }, [])

  return (
    authenticated ? Component : <Navigate to='/login' {...alert('로그인 후 이용가능합니다.')} />
  )
}

export default PrivateRoute