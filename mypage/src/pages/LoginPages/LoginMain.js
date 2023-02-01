import React from 'react'
import { useNavigate } from 'react-router-dom';

import { Button, Stack } from 'react-bootstrap';

import { AiFillGoogleCircle } from 'react-icons/ai';
import { RiKakaoTalkFill } from 'react-icons/ri';
import { SiNaver } from 'react-icons/si';
import { AiOutlineMail } from 'react-icons/ai';


import LoginHeader from './LoginHeader';

const LoginMain = () => {

  const navigate = useNavigate();

  //meta로그인의 경우 그냥 navigate만 해주어도 되지만 다른 소셜 로그인은 api호출 필요
  const goMetaLogin = () => {
    navigate('/login/meta');
  }

  return (
    <div>
      <Stack gap={2} className="col-md-5 mx-auto">
      <LoginHeader />
      <div className="d-grid gap-3">
        <Button className="" variant="outline-warning" size="lg">
          <RiKakaoTalkFill />{' '}카카오톡 로그인
        </Button>
        <Button variant="outline-success" size="lg">
          <SiNaver />{' '}네이버 로그인
        </Button>
        <Button variant="outline-primary" size="lg">
          <AiFillGoogleCircle />{' '}구글 로그인
        </Button>
        <p className="text-center mt-4 mb-4">또는</p>
        <Button onClick={goMetaLogin} variant="outline-secondary" size="lg">
          <AiOutlineMail />{' '}이메일로 로그인
        </Button>
      </div>
      </Stack>
    </div>
  )
}

export default LoginMain