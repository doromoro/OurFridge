// import { useParams } from 'react-router-dom';

// const data = {
//   velopert: {
//     name: '김민준',
//     description: '리액트를 좋아하는 개발자',
//   },
//   gildong: {
//     name: '홍길동',
//     description: '고전 소설 홍길동전의 주인공',
//   },
// };

// function Myinfo() {
//   const params = useParams();
//   const profile = data[params.username];

//   return (
//     <div>
//       <h1>사용자 프로필</h1>
//       {profile ? (
//         <div>
//           <h2>{profile.name}</h2>
//           <p>{profile.description}</p>
//         </div>
//       ) : (
//         <p>존재하지 않는 프로필입니다.</p>
//       )}
//     </div>
//   );
// };

// export default Myinfo;

import Nav from 'react-bootstrap/Nav';
import React from "react";
import { useState } from 'react';

import TabContent from './TabContent';

//nav 따로 분리해줘야함. 상단에 쌓이는 컴포넌트만 3갠데 위치 변경 또는 가볍게 해야하는 것도 고려 필요
function MyinfoMain() {

    const [tab, setTab] = useState(0)

    return (
    <section className="pt-4">
        <div className="container px-lg-5">
        <Nav variant="tabs" defaultActiveKey="link0">
            <Nav.Item>
                <Nav.Link eventKey="link0" onClick={()=>{setTab(0)}}>내 정보</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="link1" onClick={()=>{setTab(1)}}>내 냉장고</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="link2" onClick={()=>{setTab(2)}}>내 레시피</Nav.Link>
            </Nav.Item>
        </Nav>

            <div className="container px-lg-5">
                <div className="row gx-lg-5">
                    <TabContent tab={tab} />
                </div>
            </div>


        </div>
    </section>
    );
}

export default MyinfoMain;