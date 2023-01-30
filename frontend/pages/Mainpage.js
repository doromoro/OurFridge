import Nav from 'react-bootstrap/Nav';
import RecipeCard from "./recipeCard";
import React from "react";

function BasicExample() {
    return (
    <section className="pt-4">
        <div className="container px-lg-5">
            <Nav justify variant="tabs" defaultActiveKey="/home">
                <Nav.Item>
                    <Nav.Link href="/home">Active</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="link-1">Loooonger NavLink</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="link-2">Link</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="disabled" disabled>
                        Disabled
                    </Nav.Link>
                </Nav.Item>
            </Nav>

            <div className="container px-lg-5">
                <div className="row gx-lg-5">
                    <RecipeCard></RecipeCard>
                    <RecipeCard></RecipeCard>
                    <RecipeCard></RecipeCard>

                </div>
            </div>


        </div>
    </section>
    );
}

export default BasicExample;