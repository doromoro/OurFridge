import React from 'react';
import { useNavigate } from 'react-router';
import { useDispatch } from 'react-redux';
import {useForm} from "react-hook-form";
import API from '../api/API';

import 'bootstrap/dist/css/bootstrap.min.css';
import {Form, Button, Row, Col, Container} from "react-bootstrap";

import { SET_TOKEN } from '../store/Auth';
import { setRefreshToken } from '../store/Cookie';

import LoginHeader from "./LoginHeader";
import "./ErrorMessage.css";

function UseFormLogin(){

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const { register, watch, formState: {errors}, handleSubmit, reset} = useForm({ mode : "onChange"});

  //required설정에 위배되는 인풋을 주었을 경우에 프론트에서 리셋시키고 BE로 보내지 않음.
  const onInvalid = (errors) => {
    console.log(errors, "onInvalid");
    reset();
  }

  //required에 위배되지 않는 정보를 보낸 경우
  const onSubmit = (data) => {
      console.log(data,errors);
      const {email, password} = data;
      
      API({
        method: 'post',
        url: '/login',
        headers: {"Content-Type": `application/json`,},
        data: {email, password}
      }).then(response => {
        const { accessToken } = response.data.accesToken;
        // store(redux)에 accesToken저장.
        dispatch(SET_TOKEN(accessToken));
        // API 요청하는 콜마다 헤더에 accessToken 담아 보내도록 설정
        API.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

        // refreshToken은 cookie에 담기.
        setRefreshToken(response.headers['Set-Cookie']);

        // login상태 반영하는 변수가 필요할 듯


        // 로그인 성공 시에는 홈으로 이동
        return navigate('/');
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

        //오류 발생시 다시 login 으로 redirect
        // window.alert("다시 시도해주세요.");
        return navigate('/login');
      });

  }
  
  console.log(watch());


    return (
        <div>
            <Container className="panel">
                <Form onSubmit={handleSubmit(onSubmit, onInvalid)}>
                    <LoginHeader />
                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                        <Col sm>
                            <Form.Control {...register("email", {required:true, pattern: {
                                value : /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i ,
                                message : "올바르지 않은 Email 형식입니다."
                            } 
                            })} type="text" placeholder="UserEmail" />
                            <div className='errorMessage'>
                              {errors.email?.type === "pattern" && errors.email.message}
                            </div>
                            
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                        <Col sm>
                            <Form.Control {...register("password", { required: true, minLength: {
                                value : 10,
                                message : "비밀번호는 10자 이상입니다."
                            },
                            maxLength : { value: 15, message : "비밀번호는 15자 이하입니다."}
                            })} type="password" placeholder="Password" />
                            <div className='errorMessage'>
                              {errors.password?.type === "minLength" && errors.password.message}
                              {errors.password?.type === "maxLength" && errors.password.message}
                            </div>
                        </Col>
                    </Form.Group>
                    <br/>

                    <div className="d-grid gap-1">
                        <Button type="submit" >
                            Sign In
                        </Button>
                    </div>
                </Form>
            </Container>
        </div>
    );
}

export default UseFormLogin