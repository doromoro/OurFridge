import React from "react";
import RecipeSimple  from "./RecipeSimple";
// import "../css/styles.css";
import {Button} from "react-bootstrap";

var data = [
    {
        "name": "name1",
        "hot": "star",
        "author": "author1",
        "summary": "summmary1"
    },
    {
        "name": "name2",
        "hot": "star",
        "author": "author2",
        "summary": "summmary2"
    }
]
function MainRecipe() {
    return (
        <div>
            <div className="container px-4 px-lg-5 my-5">
                <div className="text-center text-black">
                    <h1 className="display-4 fw-bolder">레시피 메인 페이지</h1>
                </div>
            </div>
        <div className="container px-4 px-lg-5 mt-5">
            <div className="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 justify-content-center">
                { data.map((data) => {
                    return <RecipeSimple name={data.name} hot={data.hot} summary={data.summary} author={data.author}  />;
                })}
            </div>
        </div>
        페이지 처리 필요함
            <div className="text-center">
                <Button className="btn btn-outline-dark mt-auto" href="#" size="sm" variant="secondary">이전 페이지</Button>
                {/* <a>  1 2 3 4  </a> */}
                <Button className="btn btn-outline-dark mt-auto" href="#" size="sm" variant="secondary">다음 페이지</Button>
            </div>

        </div>
    )
}

export default MainRecipe;