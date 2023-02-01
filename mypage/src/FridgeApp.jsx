import React, { useEffect, useRef } from "react";
import { Routes, Route } from "react-router-dom";
import PrivateRoute from "./routing/PrivateRoute";
import PublicRoute from "./routing/PublicRoute";

import 'bootstrap/dist/css/bootstrap.min.css';

import LoginMain from "./pages/LoginPages/LoginMain";
import UseFormLogin from "./pages/LoginPages/UseFormLogin";
import MainPage from "./pages/Mainpage";
import Myinfo from "./pages/MyinfoPages/Myinfo";
import MainRecipe from "./pages/RecipePages/MainRecipe"
import Detailpage from "./pages/Detailpage";
import Header from "./components/Header/Header";
import NotFound from "./pages/NotFound";
import Footer from "./components/Footer/Footer";
import Register from "./pages/Register";
import ChangePw from "./pages/ChangePw";




function FridgeApp() {
  const checkRef = useRef();
  let check = localStorage.getItem('isLoggined') === "true";
  checkRef.current = check;
  console.log("ref, ", checkRef.current);
  //주기적으로 checkRef값 주입
  useEffect(() => {
    const timeout = setTimeout(() => {
      checkRef.current = localStorage.getItem('isLoggined') === 'true';
    }, 2000);

    return () => clearTimeout(timeout);
  },[checkRef.current])


  //로그인 여부를 확인(첫 렌더링 시에만)
  useEffect(() => {

    window.addEventListener('storage', (e) => {
      if (e.key === 'logout') {
        console.log('로그아웃 감지');
        localStorage.setItem('isLoggined', false);
      }
      if (e.key === 'isLoggined') {
        console.log('로그인 여부 확인')
        
      }
      let sym = localStorage.getItem('isLoggined') === "true";
      console.log('check param, ' , sym);
      checkRef.current = sym;
      console.log( "ref check " , checkRef.current);
    });
  },[])

  return (
    <div>
      <Header authenticated={checkRef.current}/>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login/meta" element={<PublicRoute authenticated={checkRef.current} component={<UseFormLogin />} />} />
        <Route path="/login" element={<PublicRoute authenticated={checkRef.current} component={<LoginMain />} />} />
        <Route path="/mypage/myinfo/changepw" element={<PrivateRoute authenticated={checkRef.current} component={<ChangePw />} />} />
        <Route path="/mypage/myinfo" element={<PrivateRoute authenticated={checkRef.current} component={<Myinfo />} />} />
        <Route path="/board" element={<MainRecipe />}>
            <Route path=":id" element={<Detailpage />} />
        </Route>
        <Route path="/register" element={<PublicRoute authenticated={checkRef.current} component={<Register />} />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
      <Footer />
    </div>
  )
}

export default FridgeApp;