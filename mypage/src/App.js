// import logo from './logo.svg';
import axios from "axios";
import './App.css';

function App() {


  // main page구성
  // const [ testStr, setTestStr ] = useState('');

  // // 변수 초기화
  // function callback(str) {
  //   setTestStr(str);
  // }

  // // 첫 번째 렌더링을 마친 후 실행(마운트 시 실행)
  // useEffect(
  //     () => {
  //       axios({
  //           url: '/home',
  //           method: 'GET'
  //       }).then((res) => {
  //           callback(res.data);  //메인페이지에 필요한 데이터를 받음
  //       })
  //     }, []
  // );



  const emailBaseUrl = "http://localhost:8080/sing-up";

  const [user_email, setUser_email] = useState();  //사용자 email
  const [user_verifCode, setUser_verifiCode] = useState();  //email로 보낸 인증코드

  //email입력 받음
  const handleChange_email = (e)=>{
    e.preventDefault();
    setUser_email(e.target.value);
}

//인증 코드 입력 받음
const handleChange_verifiCode = (e)=>{
    e.preventDefault();
    setUser_verifiCode(e.target.value);
}




  return (
    // // app.js원형, logo쓰는 방법에 대한 참고
    // <div className="App">
    //   <header className="App-header">
    //     <img src={logo} className="App-logo" alt="logo" />
    //   </header>
    // </div>

    
  );
}

export default App;
