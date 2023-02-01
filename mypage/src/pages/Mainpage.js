// import React from "react";
// import { Link } from 'react-router-dom';

// function MainPage() {
  


//   return (
//     <div>
//       <h1>Home</h1>
//       <Link to="/login/meta">메타로그인</Link>
//       <p></p>
//       <Link to="/mypage/myinfo/velopert">velopert의 프로필</Link>
//       <p></p>
//       <Link to="/mypage/myinfo/gildong">gildong의 프로필</Link>
//       <p></p>
//       <Link to="/mypage/myinfo/void">존재하지 않는 프로필</Link>    
//       <p></p>
//       <Link to="/board">전체 게시글 목록</Link>  
//     </div>
//   )
// }

// export default MainPage;

import React from "react";
import MainCard  from "./MainCard";
import titleImage from './title.png';
import ad from './ad.jpg';

var data = [
    {
        "title": "추천 레시피 확인하기",
        "detail": "추천 레시피 확인하기"
    },
    {
        "title": "냉장고 관리",
        "detail": "냉장고 관리"
    },
    {
        "title": "계절, 랭킹 레시피 확인",
        "detail": "계절, 랭킹 레시피 확인"
    }
]
function MainPage() {
    return (

        <div>
            <section className="pt-4">
            <div className="container px-lg-5">
                <div className="row gx-lg-5">
                    <img className="modal-footer" src={titleImage} alt="dd"></img>
                    { data.map((data) => {
                        return <MainCard title={data.title} detail={data.detail} />;
                    })}
                    <img className="modal-footer" src={ad} alt="dd"></img>
                </div>
            </div>
            </section>
        </div>
    )
}

export default MainPage;