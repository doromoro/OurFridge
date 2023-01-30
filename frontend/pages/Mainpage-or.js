import React from "react";
import MainCard  from "./MainCard";
import titleImage from '../img/title.png';
import ad from '../img/ad.jpg';

import "../css/styles.css";

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