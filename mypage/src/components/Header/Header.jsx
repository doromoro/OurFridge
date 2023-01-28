import React from "react";
import { useNavigate, NavLink } from "react-router-dom";
import { Button } from 'react-bootstrap';
import title from './title2.png';

import NavScroll from "../Navbar/Navbar";
import "./Header.css";
// import 'bootstrap/dist/css/bootstrap.min.css';

function Header() {

  const navigate = useNavigate();

  //로그인 여부를 검사해서 이름과 navigate가 바뀌어야함

  //로그인을 한 경우
  // const goLogout = () => {
  //   //로그아웃 절차 이후
  //   navigate('/');
  // }

  // const goMyinfo = () => {
    
  //   navigate('/mypage')
  // }


  //로그인을 안한경우
  const goLogin = () => {
    // 이전 페이지로 이동
    // navigate(-1);
    navigate('/login');
  };

  const goRegister = () => {
    // articles 경로로 이동
    navigate('/register');
  };

  return (
    <>
    <header id="main-bg">
      {/* <p className="title">나의 냉장고</p> */}
      <NavLink to="/">
        <img className="img" src={title} alt="title" />
      </NavLink>
      {/* <Nav className="justify-content-end" activeKey="/home">
        <Nav.Item>
          <Nav.Link href="/home">Active</Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link eventKey="link-1">Link</Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link eventKey="link-2">Link</Nav.Link>
        </Nav.Item>
      </Nav> */}
      <div className="button">
        <Button variant="outline-success" onClick={goRegister}>회원가입</Button>{'  '}
        <Button variant="outline-success" onClick={goLogin}>로그인</Button>
      </div>
    </header>
    <NavScroll />

  </>
  );
};

export default Header;