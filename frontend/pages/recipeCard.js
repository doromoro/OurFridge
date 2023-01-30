import React from "react";
import img from '../img/logo.png'
import "../css/styles.css";

let style = {
    "float": "right"
}
let styles = {
    "float": "left",
    "max-width": "100%"
}

function RecipeCard() {
    return (
        <div>
            <div className="card h-100">
                <div className="d-flex mb-3">
                    <h3 className="">
                        <img src={img} alt="Laptop" style={styles}></img>

                        <div className="card h-100">
                        </div>레시피 제목</h3>
                </div>

                <div className="mb-0">
                    <h7 className="text-dark mb-0" style={style}>작성일자: asdsadsadsad</h7>
                    <h7 className="text-dark mb-0" style={style}>수정일자: asdasdsadsad</h7>
                </div>

                <div className="d-flex justify-content-between">
                    <p className="text-muted mb-0">
                        조회수: <span className="fw-bold">7111</span>
                    </p>
                    <p className="text-muted mb-4 mx-2">
                        추천수: <span className="fw-bold">11117</span>
                    </p>
                    <div className="ms-auto text-warning">
                    </div>
                </div>
            </div>
        </div>
    )
}
export default RecipeCard;