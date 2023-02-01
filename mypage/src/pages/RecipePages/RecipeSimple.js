import React from "react";

let style = {
    top: "0.5rem",
    right: "0.5rem"
}

function RecipeSimple(props) {
    return (
        <div>
        <div className="col mb-5">
            <div className="card h-100">
                <div className="badge bg-dark text-white position-absolute" style={style}>
                    {props.hot}</div>
                <img className="card-img-top" src="https://dummyimage.com/450x300/dee2e6/6c757d.jpg" alt="..."/>
                <div className="card-body p-4">
                    <div className="text-center">
                        <h5 className="fw-bolder">{props.name}</h5>
                        {props.summary}
                    </div>
                </div>
                <div className="card-footer p-4 pt-0 border-top-0 bg-transparent">
                    <h5 className="fw-bold customFont">작성자: {props.author}</h5>
                </div>
            </div>
        </div>
        </div>
    )
}
export default RecipeSimple;