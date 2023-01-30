import { useNavigate } from 'react-router';
import { getAccessToken, removeAccessToken } from '../store/Cookie';


const navigate = useNavigate();
const accessToken = getAccessToken();

// 서버로는 토큰들을 전송 서버에 성공적으로 전달해서 res받으면
// 내부적으로는 localStorage에 isLoggined = false, logout = Data.now()
// 토큰 삭제(removeAccessToken), navigate까지
export const logout = () => {

}