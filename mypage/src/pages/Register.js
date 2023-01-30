import React from 'react'
import {useState} from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router';
import {useForm} from "react-hook-form";

import { RiFridgeFill } from 'react-icons/ri';
import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import "../loginPart/ErrorMessage.css";


const Register = () => {
  const [mailSend, setMailSend] = useState(true);
  const [resData, setResData] = useState();
  // const [validate,setValidate] = useState(true);

  const navigate = useNavigate();
  const { register, watch, formState: {errors, isSubmitting}, handleSubmit, reset} = useForm({ mode : "onChange"});

  const sendEmail = () => {
    // console.log("email, ", emailRef.current.value);
    console.log("email", watch("email"));
    let data = watch("email");
    
    axios({
      method: 'post',
      url : 'https://localhost:8080/mailSend',
      headers: {
        "Content-Type" : 'application/json',
      },
      data : {"email":`${data}`},
      withCredentials : true,
    }).then(response => {
      console.log("data", response.data);
      console.log("header", response.headers);
      setMailSend(false);
    })
  }


  //인증번호 확인은 회원가입 과정에서 딱히 없음 이 부분은 비밀번호 변경으로 넘기자.
  // const checkCode = () => {
  //   let email = emailRef.current.value;
  //   let code = validateRef.current.value;
    
  //   axios({
  //     method: 'post',
  //     url : 'https://localhost:8080/mailSend',
  //     headers: {
  //       "Content-Type" : 'application/json',
  //     },
  //     data : {"email":`${email}`, "validateCode" : `${code}`},
  //     withCredentials : true,
  //   }).then(response => {
  //     console.log("data", response.data);
  //     console.log("header", response.headers);
  //     setMailSend(false);
  //   })
  // }
  const onSubmit = (data) => {
    console.log(data);

    axios({
      method: 'post',
      url : "https://localhost:8080/sign-up",
      headers : { "Content-Type" : "application/json"},
      data : data,
      withCredentials : true,
    }).then(res => {
      console.log("res", res);
      console.log("data", res.data);
      console.log("state", res.data.state);
      console.log("result" , res.data.result);
      console.log("msg ", res.data.massage);
      // console.log("header", res.headers);
      console.log("another approach", JSON.stringify(res.data));
      // console.log("another approach msg", JSON.stringify(res.data).message);
      // console.log("3", JSON.stringify())

      setResData(res.data);
      console.log("3 " + resData.massage);
      console.log("2 " , resData);

      if(res.data.massage === "인증에 실패했습니다.") {
        alert("인증코드가 유효하지 않습니다.");
        reset();
        setMailSend(true);
      } else if(res.data.massage === "이미 회원가입된 이메일입니다.") {
        alert("이미 회원가입이 된 메일입니다.");
        reset();
        setMailSend(true);
      } else {
        //회원가입 성공
        alert("회원가입 성공");
        navigate('/login/meta');
      }
      
    }).catch(function(error) {
      if(error.response) {
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

      //오류 발생시 다시 실행
      reset();
      setMailSend(true);
    })
  }

  const onInvalid = (errors) => {
    console.log(errors, "onInvalid");
    alert("유효하지 않은 데이터가 있습니다.");
    setMailSend(true);
    reset();
  }

  

  return (
    <>
    <h1 className='mb-5 ms-3 mt-3'><RiFridgeFill />{' '}회원가입</h1>
    <Form onSubmit={handleSubmit(onSubmit, onInvalid)}>
      <Row className="mb-3 mx-5">
        <Form.Group as={Col} controlId="formGridEmail">
          <Form.Label>Email</Form.Label>
          <Form.Control
          type="email" 
          placeholder="Enter email"
          {...register("email", {required:true, pattern : {
            value : /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i ,
            message : "올바르지 않은 Email 형식입니다."
          }})}
          />
          <div className='errorMessage'>
              {errors.email?.type === "pattern" && errors.email.message}
          </div>
          <Button onClick={sendEmail} className="mt-2" variant="outline-secondary" disabled={!mailSend}>
            인증메일 발송
          </Button>
        </Form.Group>

        <Form.Group as={Col} controlId="formGridValidate">
          <Form.Label>ValidateCode</Form.Label>
          <Form.Control type="text" placeholder="8 words" disabled={mailSend}
          {...register("validatedCode", {required:true, minLength : {
            value : 8,
            message : "8자리의 인증코드를 넣어주세요."
          }})}
          />
          <Form.Text id="passwordHelpBlock" muted>
            8글자의 인증코드를 넣어주세요.
          </Form.Text>
          <div className='errorMessage'>
            {errors.validatedCode?.type === "minLength" && errors.validatedCode.message}
          </div>
          {/* <Button className="mt-2" variant="outline-secondary" disabled={mailSend}>
            인증
          </Button> */}
        </Form.Group>
      </Row>

      <Form.Group className="mb-3 mx-5 px-5" controlId="formGridPassword">
        <Form.Label>Password</Form.Label>
          <Form.Control type="password" placeholder="Password" required
          {...register("password", { required: true, minLength: {
            value : 10,
            message : "비밀번호는 10자 이상입니다."
            },
            maxLength : { value: 15, message : "비밀번호는 15자 이하입니다."},
            pattern : {
              value : /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{10,15}$/,
              message : "영문 소문자, 숫자, 특수기호를 조합해서 사용하세요"
            }
          })}
          />
          <Form.Text id="passwordHelpBlock" muted>
            영문(소문자), 숫자, 특수기호를 조합하여 10~15글자의 비밀번호를 생성하세요.
          </Form.Text>
          <div className='errorMessage'>
            {errors.password?.type === "minLength" && errors.password.message}
            {errors.password?.type === "pattern" && errors.password.message}
            {errors.password?.type === "maxLength" && errors.password.message}
          </div>
      </Form.Group>

      <Row className="mb-3 mx-5">
        <Form.Group as={Col} controlId="formGridName">
          <Form.Label>Name</Form.Label>
          <Form.Control type='text' required
          {...register("name", { required: true ,minLength : {
            value : 2,
            message : "2글자 이상이어야 합니다."
          }, pattern : {
            value : /^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,30}$/,
            message : "특수기호는 포함될 수 없습니다."
          } })}
            />
          <div className='errorMessage'>
            {errors.name?.type === "minLength" && errors.name.message}
            {errors.name?.type === "pattern" && errors.name.message}
          </div>
        </Form.Group>

        <Form.Group as={Col} controlId="formGridGender">
          <Form.Label>Gender</Form.Label>
          <Form.Select defaultValue="Female" {...register("gender")}>
            <option>Male</option>
            <option>Female</option>
          </Form.Select>
        </Form.Group>

        <Form.Group as={Col} controlId="formGridNum">
          <Form.Label>Num.</Form.Label>
          <Form.Control type='text' placeholder='-없이 기재'
          {...register("nums", { pattern : { value : /(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/g,
            message : "-없이 기재해주세요." }})}
          />
          <div className='errorMessage'>
            {errors.nums?.type === "pattern" && errors.nums.message}
          </div>
        </Form.Group>
      </Row>

      <Button className="ms-5" variant="primary" type="submit" disabled={isSubmitting}>
        Submit
      </Button>
    </Form>
    </>
  )
}

export default Register