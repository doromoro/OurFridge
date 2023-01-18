import React from "react";
import { Routes, Route, Link } from "react-router-dom";
// import Login from "./loginPart/Login";
import MetaLogin from "./loginPart/MetaLogin";
import Home from "./Home";
import Logout from "./loginPart/Logout";


function FridgeApp() {
  
  return (
    <div>
      <div>
        <button>
          <Link to="/"> HOME </Link>
        </button>
        <button>
          <Link to="/login/meta"> MetaLogin </Link>
        </button>
        <button>
          <Link to="/logout"> Logout </Link>
        </button>
      </div>

      <Routes>
        <Route path="/" element={<Home />}></Route>
        <Route path="/login/meta" element={<MetaLogin />}></Route>
        <Route path="/logout" element={<Logout />}></Route>
      </Routes>
    </div>
  )
}

export default FridgeApp;