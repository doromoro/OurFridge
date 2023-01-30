import React from "react";
import { useNavigate, NavLink } from "react-router-dom";
import { Button } from 'react-bootstrap';
import title from './title2.png';

import NavScroll from "../Navbar/Navbar";
import "./Header.css";
// import 'bootstrap/dist/css/bootstrap.min.css';

function Header() {

  const navigate = useNavigate();
  // 이전 페이지로 이동
  // navigate(-1);

  let check = localStorage.getItem('isLoggined');

  //로그인을 안한경우
  const goLoginOrOut = () => {
    
    //로그인이 되어있다면
    if(check) {
      //logout api호출 현재 logout.js에서 export하여 구현예정 거기 안에 navigate도 있을 거라 밑의 코드는 없어질 예정
      navigate('/');
    }
    else {
      navigate('/login');
    }
  };

  const goRegisterOrMyinfo = () => {
    //로그인이 되어있다면
    if(check) {
      navigate('/mypage/myinfo');
    }
    else {
      navigate('/register');
    }
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
        <Button variant="outline-success" onClick={goRegisterOrMyinfo}>{check ? "내 냉장고" : "회원가입"}</Button>{'  '}
        <Button variant="outline-success" onClick={goLoginOrOut}>{check ? "로그아웃" : "로그인"}</Button>
      </div>
    </header>
    <NavScroll />

  </>
  );
};

export default Header;