import React, { useEffect, useState } from "react";
import { Routes, Route } from "react-router-dom";
import PrivateRoute from "./routing/PrivateRoute";
import PublicRoute from "./routing/PublicRoute";

import 'bootstrap/dist/css/bootstrap.min.css';

import { getAccessToken } from "./store/Cookie";
// import Login from "./loginPart/Login";
// import MetaLogin from "./loginPart/MetaLogin";
// import Home from "./Home";
import LoginMain from "./pages/LoginMain";
import UseFormLogin from "./loginPart/UseFormLogin";
import MainPage from "./pages/Mainpage";
import Myinfo from "./pages/Myinfo";
import MainRecipe from "./pages/MainRecipe"
import Detailpage from "./pages/Detailpage";
import Header from "./components/Header/Header";
import NotFound from "./pages/NotFound";
import Footer from "./components/Footer/Footer";
import Register from "./pages/Register";
import ChangePw from "./pages/ChangePw";
// import Logout from "./loginPart/Logout";

// const initialState = {
//   authenticated: false,
//   token: null,
//   expireTime : null
// }

// function reducer(state, action) {
//   switch(action.type) {
//       case 'SET_TOKEN':
//           return {...state, token: action.token, authenticated: action.result, expireTime : };
//       default:
//           return state;
//   }
// }

// if (token) {
//   console.log('로그인 성공!');
//   dispatch({
//     type: 'SET_TOKEN',
//     token: token,
//     result: true,
//   });
// } else {
//   console.log('로그인 실패');
//   dispatch({
//     type: 'SET_TOKEN',
//     token: null,
//     result: false,
//   });
// }


function FridgeApp() {
  // const [state, dispatch] = useReducer(reducer, initialState);
  // const { authenticated } = state;
  const [isLoggined, setIsLoggined] = useState(false);
  let check = localStorage.getItem('isLoggined');
  setIsLoggined(check);

  //로그인 여부를 확인(첫 렌더링 시에만), 수정필요
  useEffect(() => {
    window.addEventListener('storage', (e) => {
      if (e.key === 'logout') {
        console.log('로그아웃 감지');
        localStorage.setItem('isLoggined', false);
      }
      
    });
  },[])

    //시작할때 로그아웃 탐지를 함.
  // useEffect(() => {
  //   window.addEventListener('storage', (e) => {
  //     if (e.key === 'logout') {
  //       console.log('로그아웃 감지');
  //       localStorage.setItem('isLoggined', "");
  //     }
  //   });
  // }, []);

  // console.log("check Login",checkedLogin);
  return (
    <div>
      <Header />
      <Routes>
        <Route exact path="/" element={<MainPage />} />
        <Route path="/login/meta" element={<PublicRoute authenticated={isLoggined} component={<UseFormLogin />} />} />
        <Route exact path="/login" element={<PublicRoute authenticated={isLoggined} component={<LoginMain />} />} />
        <Route path="/mypage/myinfo/changepw" element={<PrivateRoute authenticated={isLoggined} component={<ChangePw />} />} />
        <Route exact path="/mypage/myinfo" element={<PrivateRoute authenticated={isLoggined} component={<Myinfo />} />} />
        <Route path="/board" element={<MainRecipe />}>
            <Route path=":id" element={<Detailpage />} />
        </Route>
        <Route path="/register" element={<PublicRoute authenticated={isLoggined} component={<Register />} />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
      <Footer />
    </div>
  )
}

export default FridgeApp;