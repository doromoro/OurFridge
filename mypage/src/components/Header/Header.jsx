import React from "react";
import { useNavigate, NavLink } from "react-router-dom";
import axios from 'axios';
import { Button } from 'react-bootstrap';
import title from './title2.png';

import { getAccessToken, removeAccessToken } from "../../store/Cookie";
import NavScroll from "./Navbar/Navbar";
import "./Header.css";
// import 'bootstrap/dist/css/bootstrap.min.css';

function Header({authenticated}) {

  // const accessToken = getAccessToken();
  const navigate = useNavigate();
  // 이전 페이지로 이동
  // navigate(-1);

  const goLogin = () => {
    navigate('/login');
  }

  const goRegisterOrMyinfo = () => {
    console.log("auth ", authenticated);
    //로그인이 되어있다면
    if(authenticated) {
      navigate('/mypage/myinfo');
    }
    else {
      navigate('/register');
    }
  };

  const logout = () => {
    axios ({
      method : 'post',
      url : 'https://localhost:8080/logout',
      headers: {
        "Content-Type" : 'application/json',
        'Access-Control-Allow-Origin': 'https://localhost:8080'
        // "Authorization" : `Bearer ${accessToken}`
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
      localStorage.setItem('isLoggined', false);
      localStorage.setItem('logout', Date.now());
      removeAccessToken();
      navigate('/');
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
      alert("로그아웃 중 오류 발생");
      localStorage.setItem('isLoggined', false);
      localStorage.setItem('logout', Date.now());
      removeAccessToken();
      navigate('/');
    })
  }

  return (
    <>
      <header id="main-bg">
        <NavLink to="/">
          <img className="img" src={title} alt="title" />
        </NavLink>
        <div className="button">
          <Button variant="outline-success" onClick={goRegisterOrMyinfo}>{authenticated ? "내 냉장고" : "회원가입"}</Button>{'  '}
          {authenticated ? <Button variant="outline-secondary" onClick={logout}>로그아웃</Button> : <Button variant="outline-success" onClick={goLogin}>로그인</Button>}
        </div>
      </header>

      <NavScroll />
    </>
  );
};

export default Header;