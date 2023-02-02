import React from 'react';
import axios from 'axios';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { setAccessToken, getAccessToken } from '../../store/Cookie';
import Loading from '../../routing/Loading';

const KakaoLogin = () => {

  const navigate = useNavigate();

  let message = '카카오에 로그인 시도 중입니다.';
  // 인가코드
  let code = new URL(window.location.href).searchParams.get("code");

  useEffect( () => {
    axios({
      method : 'get',
      url : `https://localhost:8080/social/oauth/kakao?code=${code}`,
      withCredentials : true,
      headers : {
        "Content-Type": 'application/json',
        'Access-Control-Allow-Origin': 'https://localhost:8080'
      }
    }).then(res => {
      console.log('res전체, ', res);
      console.log('data', res.data);
      console.log('header', res.headers);
      console.log('access', res.data.data.accesToken);

      if(res.data.data.accessToken !== undefined) {
        console.log('로그인 성공')
        const accessToken = res.data.data.accessToken;

        // API 요청하는 콜마다 헤더에 accessToken 담아 보내도록 설정
        axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

        // accessToken은 cookie에 담기.
        setAccessToken(res.data.data.accessToken);
        console.log('token ' , getAccessToken());
        //로컬에 로그인 상태 저장, 민감한 정보에는 접근하는데에 사용하지 않고 routing에 사용
        localStorage.clear();
        localStorage.setItem('isLoggined', true);
        // 로그인 성공 시에는 홈으로 이동
        navigate('/');

      }
      //undef의 경우 (실패에 대한 api호출이 필요할듯?)
      else {
        console.log('로그인 실패');
        alert("시스템 오류 : 다시 로그인해주세요");
        localStorage.setItem('isLoggined', false);
        navigate('/login');
      }
      

    }).catch(function(error) {
      if(error.res) {
        // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
        console.log(error.res.data);
        console.log(error.res.status);
        console.log(error.res.headers);
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

      //오류 발생시 다시 login 으로 redirect
      localStorage.setItem('isLoggined', false);
      navigate('/login');
      alert("다시 시도해주세요.");
    });
  }, []);

  return (
    <Loading message={message}/>
  )
}

export default KakaoLogin