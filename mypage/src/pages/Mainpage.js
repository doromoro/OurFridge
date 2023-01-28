import React from "react";
import { Link } from 'react-router-dom';

function MainPage() {
  


  return (
    <div>
      <h1>Home</h1>
      <Link to="/login/meta">메타로그인</Link>
      <p></p>
      <Link to="/mypage/myinfo/velopert">velopert의 프로필</Link>
      <p></p>
      <Link to="/mypage/myinfo/gildong">gildong의 프로필</Link>
      <p></p>
      <Link to="/mypage/myinfo/void">존재하지 않는 프로필</Link>    
      <p></p>
      <Link to="/board">전체 게시글 목록</Link>  
    </div>
  )
}

export default MainPage;