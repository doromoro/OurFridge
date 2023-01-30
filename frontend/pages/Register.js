import React from 'react'
import {useState, useRef} from 'react';
import axios from 'axios';

import { RiFridgeFill } from 'react-icons/ri';
import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';


const Register = () => {
  // const [email, setEmail] = useState('');
  const emailRef = useRef(null);
  const validateRef = useRef(null);
  const [mailSend, setMailSend] = useState(true);
  const [validate, setValidate] = useState(true);

  const sendEmail = () => {
    console.log("email, ", emailRef.current.value);
    let data = emailRef.current.value;
    axios({
      method: 'post',
      url : '/api/mailSend',
      headers: {
        "Content-Type" : 'application/json',
      },
      data : data,
      withCredentials : true,
    }).then(response => {
      console.log("data", response.data);
      console.log("header", response.headers);
      setMailSend(false);
    })
  }


  return (
    <>
    <h1 className='mb-5 ms-3 mt-3'><RiFridgeFill />{' '}회원가입</h1>
    <Form>
      <Row className="mb-3 mx-5">
        <Form.Group as={Col} controlId="formGridEmail">
          <Form.Label>Email</Form.Label>
          <Form.Control ref={emailRef} type="email" placeholder="Enter email" disabled={!mailSend}/>
          <Button onClick={sendEmail} className="mt-2" variant="outline-secondary">
            인증메일 발송
          </Button>
        </Form.Group>

        <Form.Group as={Col} controlId="formGridValidate">
          <Form.Label>ValidateCode</Form.Label>
          <Form.Control ref={validateRef} type="text" placeholder="6 words" disabled={mailSend}/>
          <Button className="mt-2" variant="outline-secondary" disabled={mailSend}>
            인증
          </Button>
        </Form.Group>
      </Row>

      <Form.Group className="mb-3 mx-5 px-5" controlId="formGridPassword">
        <Form.Label>Password</Form.Label>
          <Form.Control type="password" placeholder="Password" />
          <Form.Text id="passwordHelpBlock" muted>
            10자 이상 15자 이하이어야 하며 영문,숫자,특수문자가 포함되어야 합니다.
          </Form.Text>
      </Form.Group>

      <Row className="mb-3 mx-5">
        <Form.Group as={Col} controlId="formGridName">
          <Form.Label>Name</Form.Label>
          <Form.Control />
        </Form.Group>

        <Form.Group as={Col} controlId="formGridGender">
          <Form.Label>Gender</Form.Label>
          <Form.Select defaultValue="Female">
            <option>Male</option>
            <option>Female</option>
          </Form.Select>
        </Form.Group>

        <Form.Group as={Col} controlId="formGridNum">
          <Form.Label>Num.</Form.Label>
          <Form.Control type='number' placeholder='-없이 기재'/>
        </Form.Group>
      </Row>

      {/* <Form.Group className="mb-3" id="formGridCheckbox">
        <Form.Check type="checkbox" label="Check me out" />
      </Form.Group> */}

      <Button className="ms-5" variant="primary" type="submit" disabled={validate}>
        Submit
      </Button>
    </Form>
    </>
  )
}

export default Register