import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
// import axios from 'axios';

import { CookiesProvider } from 'react-cookie';
import FridgeApp from './FridgeApp';
import { BrowserRouter } from 'react-router-dom';

// axios.defaults.baseURL = "http://localhost:3000";
// axios.defaults.crossDomain = true;
// axios.defaults.withCredentials = true;

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <CookiesProvider>
    <BrowserRouter>
      <FridgeApp />
    </BrowserRouter>
  </CookiesProvider>
);
