import React from "react";

function MainCard(props) {
    return (
            <div className="col-lg-6 col-xxl-4 mb-5">
                <div className="card bg-light border-0 h-100">
                    <img className="card-img-top" src="https://dummyimage.com/450x300/dee2e6/6c757d.jpg" alt="..."/>
                    <div className="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                        <div className="feature bg-primary bg-gradient text-white rounded-3 mb-4 mt-n4"><i
                            className="bi bi-collection"></i></div>
                        <h2 className="fs-4 fw-bold">{props.title}</h2>
                        <p className="mb-0">{props.detail}</p>
                    </div>
                </div>
            </div>
    )
}
export default MainCard;