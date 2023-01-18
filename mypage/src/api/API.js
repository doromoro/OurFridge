// API.js
// axios 의 인스턴스를 생성
import axios from 'axios';

const API = axios.create({
	BASE_URL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
  crossDomain: true,
  validateStatus: function (status) {
    // 상태 코드가 500 이상일 경우 거부. 나머지(500보다 작은)는 허용.
    return status < 500;
  },
});

export default API;