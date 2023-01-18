import React from "react";
import { useNavigate } from 'react-router';
import { useDispatch } from 'react-redux';
import { useForm } from 'react-hook-form';

import { ErrorMessage } from '@hookform/error-message';

import { loginUser } from "../api/User";
import {setRefreshToken} from "../store/Cookie";
import { SET_TOKEN } from "../store/Auth";
// import { BrowserRouter, Routes, Route, Link, useNavigate} from 'react-router-dom';
import LoginHeader from "./LoginHeader";


function MetaLogin () {

  const navigate = useNavigate();
  const dispatch = useDispatch();

  // useForm 사용을 위한 선언
  const { register, setValue, formState: { errors }, handleSubmit } = useForm();

   // submit 이후 동작할 코드
    // 백으로 유저 정보 전달
  const onValid = async ({ userid, password }) => {
    // input 태그 값 비워주는 코드
    setValue("password", "");
    
    // 백으로부터 받은 응답
    const response = await loginUser({ userid, password });

    if (response.status) {
        // 쿠키에 Refresh Token, store에 Access Token 저장
        setRefreshToken(response.json.refresh_token);
        dispatch(SET_TOKEN(response.json.access_token));

        return navigate("/");
    } else {
        console.log(response.json);
    }
  };

  return (
  <>
    <LoginHeader />
    <form className="mt-8 space-y-6" onSubmit={handleSubmit(onValid)}>
                        <div>
                            <div>
                                <label htmlFor="UserID">
                                    User Email
                                </label>
                                <input
                                    {...register("userid", {required: "Please Enter Your Email"})}
                                    type="text"
                                    placeholder="User Email"
                                />
                                <ErrorMessage
                                    name="userid"
                                    errors={errors}
                                    render={( { message }) =>
                                        <p>
                                            { message }
                                        </p>
                                }
                                />
                            </div>
                            <div>
                                <label htmlFor="password">
                                    Password
                                </label>
                                <Input
                                    {...register("password", {required: "Please Enter Your Password"})}
                                    type="text"
                                    placeholder="Password"
                                />
                                <ErrorMessage
                                    name="password"
                                    errors={errors}
                                    render={( { message }) =>
                                        <p>
                                            { message }
                                        </p>
                                    }
                                />
                            </div>
                        </div>
                        <div>
                            <button type="submit">
                                Sign in
                            </button>
                        </div>
                    </form>
    {/* <Link to="/login/meta"> */}
      {/* <button onClick={() => navigate('/')}>모든 로그인 보기</button> */}
        {/* <Routes> */}
          
          {/* 폼 삽입 */}
  {/*         
          <Route path="/myinfo/pw/change" element={<PwChange />}></Route>
          <Route path="/register" element={<Register />}></Route> */}
        {/* </Routes> */}
  </> 
  );
};

export default MetaLogin;