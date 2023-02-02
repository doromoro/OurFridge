import React from 'react';
import { useState, useEffect } from 'react';
import axios from 'axios';

import { Button } from 'react-bootstrap';

const Myinfo = () => {
  const [myData, setMyData] = useState([]);

  useEffect(() => {

    axios({
      method : 'get' ,
      url : 'https://localhost:8080/mypage/my-info',
      headers: {
        "Content-Type": 'application/json',
        'Access-Control-Allow-Origin': 'https://localhost:8080'
      },
      withCredentials : true,
    }).then(res => {
      console.log("res", res);
      console.log("data", res.data);
      setMyData(res.data);
      console.log('in 확인(myData)', myData);
      console.log("받아오기 성공")
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
    })

  }, [])

  return (
    <div>
      {/* { myData.map((data) => {
        return;
      })} */}
      <Button>비밀번호 변경</Button>
      <Button>내 정보 수정</Button>
    </div>
  )
}

export default Myinfo;