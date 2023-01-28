import React, { useEffect, useState } from "react";
import { Routes, Route } from "react-router-dom";
// import { useReducer } from "react";

import 'bootstrap/dist/css/bootstrap.min.css';

import { getAccessToken } from "./store/Cookie";
// import Login from "./loginPart/Login";
// import MetaLogin from "./loginPart/MetaLogin";
// import Home from "./Home";
import LoginMain from "./pages/LoginMain";
import UseFormLogin from "./loginPart/UseFormLogin";
import MainPage from "./pages/Mainpage";
import Myinfo from "./pages/Myinfo";
import BoardHome from "./pages/BoardHome";
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

  //로그인 여부를 확인
  useEffect(() => {
    console.log("accessToken check", getAccessToken())
    if(getAccessToken() !== undefined) {
      localStorage.setItem('isLoggined', true);
    }
    else {
      localStorage.setItem('isLoggined', false);
    }
    let check = localStorage.getItem('isLoggined');
    setIsLoggined(check);
    
    
  },[])

  // console.log("check Login",checkedLogin);
  return (
    <div>
      <Header />
      <Routes>
        <Route exact path="/" element={<MainPage />} />
        <Route path="/login/meta" element={<UseFormLogin />} />
        <Route path="/login" element={<LoginMain />} />
        <Route path="/mypage/myinfo/changepw" element={<ChangePw />} />
        <Route path="/mypage/myinfo/:username" element={<Myinfo />} />
        <Route path="/board" element={<BoardHome />}>
            <Route path=":id" element={<Detailpage />} />
        </Route>
        <Route path="/register" element={<Register />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
      <Footer />
    </div>
  )
}

export default FridgeApp;