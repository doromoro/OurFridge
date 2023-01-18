import React from "react";

import 'bootstrap/dist/css/bootstrap.min.css';
import {Form, Button, Row, Col, Container} from "react-bootstrap";

function Join(){
    return (
        <div>
            <Container className="panel">
                <Form>
                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                        <Col sm>
                            <Form.Control type="password" placeholder="UserID" />
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                        <Col sm>
                            <Form.Control type="password" placeholder="Password" />
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">   
                        <Col sm>
                            <Form.Control type="password" placeholder="Confirm Password" />
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                        <Col sm>
                            <Form.Control type="password" placeholder="Username" />
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row} className="mb-3" controlId="formBasicEmail">
                        <Col sm>
                            <Form.Control type="email" placeholder="Email Address" />
                        </Col>
                    </Form.Group>
                    <br/>

                    <div className="d-grid gap-1">
                        <Button variant="secondary" type="submit" >
                            Sign Up
                        </Button>
                    </div>
                </Form>
            </Container>
        </div>
    );
}

export default Join