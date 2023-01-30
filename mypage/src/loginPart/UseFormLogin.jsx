import React from 'react';
import { useNavigate } from 'react-router';
import {useForm} from "react-hook-form";
// import API from '../api/API';
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css';
import {Form, Button, Row, Col, Container} from "react-bootstrap";

import { setAccessToken } from '../store/Cookie';
import LoginHeader from "./LoginHeader";
import "./ErrorMessage.css";



//현재 컴포넌트 내용
function UseFormLogin(){

  const navigate = useNavigate();

  const { register, formState: {errors}, handleSubmit, reset} = useForm({ mode : "onChange"});

  //required설정에 위배되는 인풋을 주었을 경우에 프론트에서 리셋시키고 BE로 보내지 않음.
  const onInvalid = (errors) => {
    console.log(errors, "onInvalid");
    reset();
  }


    //logout컴포넌트로 옮길 예정
  // function logout() {
  //   console.log('localstorage에 logout기록 명시');
  //   window.localStorage.setItem('logout', Date.now());
  // }

  // //로그아웃 버튼을 눌렀을 시(logout api호출도 필요함.)
  // const handleLogout = () => {
  //   // dispatch({
  //   //   type: 'DELETE_TOKEN',
  //   // });
  //   logout();
  // };

  //회원가입 버튼을 누르면
  const goRegister = () => {
    navigate('/register');
  }
  
  //비밀번호 찾기를 누르면
  const goFindpw = () => {
    navigate('/mypage/myinfo/changepw');
  }

  //required에 위배되지 않는 정보를 보낸 경우
  const onSubmit = (data) => {

      console.log(data);
      // const {email, password} = data;
      console.log(data.errors);
      
      axios({
        method: 'post',
        url: 'https://localhost:8080/login',
        headers: {
          "Content-Type": 'application/json',
        },
        data: data,
        withCredentials : true,
      }).then(response => {
        console.log("전체 데이터",response.data);
        console.log("헤더",response.headers);
        console.log("엑세스토큰 보이니?", response.headers.get('Authorization'));
        console.log("엑세스토큰 보이니?", response.headers['Authorization']);
        console.log("쿠키 보이니?", response.headers.get('set-cookie'));
        console.log("쿠키 보이니?", response.headers['set-cookie']);
        console.log("access", response.data.data.accessToken);
        console.log("refresh", response.data.data.refreshToken);
        
        //성공적으로 토큰을 받았다면
        if(response.data.data.accessToken !== undefined) {
          console.log('로그인 성공')
          const accessToken = response.data.data.accessToken;
          // const refreshToken = response.data.data.refreshToken;

          // API 요청하는 콜마다 헤더에 accessToken 담아 보내도록 설정
          axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

          // accessToken은 cookie에 담기.
          setAccessToken(`${accessToken}`);
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
        localStorage.setItem('isLoggined', false);
        navigate('/login');
        alert("다시 시도해주세요.");
      });

  }
  
  // console.log(watch());


    return (
        <div>
            <Container className="panel">
                <Form onSubmit={handleSubmit(onSubmit, onInvalid)}>
                    <LoginHeader />
                    <Form.Group as={Row} className="mb-3 mx-5 px-5" controlId="formPlaintextPassword">
                      <Form.Label>Email</Form.Label>
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

                    <Form.Group as={Row} className="mb-3 mx-5 px-5" controlId="formPlaintextPassword">
                      <Form.Label>Password</Form.Label>
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
                    

                    <div className='d-flex justify-content-end mb-2'>
                        <Button type="submit" size="lg">
                            로그인
                        </Button>
                    </div>
                    <div className='d-flex justify-content-end'>
                      <Button onClick={goRegister} className="mx-1" variant='secondary'>
                        회원가입
                      </Button>
                      <Button onClick={goFindpw} className="ms-1" variant='secondary'>
                        비밀번호 찾기
                      </Button>
                    </div>
                </Form>
            </Container>
        </div>
    );
}

export default UseFormLogin