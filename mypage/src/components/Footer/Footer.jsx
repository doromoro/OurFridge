import React from "react";
import { Link } from 'react-router-dom'; 
import "./Footer.css";

import {Row, Col} from 'react-bootstrap';

function Footer() {
  
  return (
    <div id="main-footer" className="text-center pb-5 mt-4">
      <Row>
          <Col>
              <p className="pt-4">
                  Copyright &copy; <span>2023</span>
              </p>
              <Link to='https://github.com/doromoro/OurFridge' >깃허브 주소</Link>
              <p>Email : khan1148943@gmail.com</p>
          </Col>
      </Row>
    </div>
  );
};

export default Footer;