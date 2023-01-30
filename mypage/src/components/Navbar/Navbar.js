import logo from "../Header/logo.png";

import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';

import "./Navbar.css"

function NavScroll() {
  return (
    <Navbar id="NavbarWrapper" expand="lg">
      <Container fluid>
        <Navbar.Brand href="/">
        <img
              src={logo}
              width="40"
              height="40"
              className="d-inline-block align-top"
              alt="MyFridge logo"
            />
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            // className="me-auto my-2 my-lg-0"
            className="justify-content-end flex-grow-1 pe-3"
            style={{ maxHeight: '100px' }}
            navbarScroll
          >
            <Nav.Link href="/">나의 냉장고(/)</Nav.Link>
            <Nav.Link href="/login">ㅁㄹ</Nav.Link>
            <NavDropdown title="레시피" id="navbarScrollingDropdown">
              <NavDropdown.Item href="#action3">레시피 랭킹</NavDropdown.Item>
              <NavDropdown.Item href="#action4">
                계절별 레시피
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item href="/board">
                전체 레시피
              </NavDropdown.Item>
            </NavDropdown>
            {/* <Nav.Link href="#" disabled>
              Link
            </Nav.Link> */}
          </Nav>
          <Form className="d-flex">
            <Form.Control
              type="search"
              placeholder="Search"
              className="me-2"
              aria-label="Search"
            />
            <Button variant="outline-success">Search</Button>
          </Form>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default NavScroll;