import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
// import axios from 'axios';
// import MetaLogin from './loginPart/MetaLogin';
import store from './store';
import { Provider } from 'react-redux';
import { CookiesProvider } from 'react-cookie';
// import FridgeApp from './FridgeApp';
// import { BrowserRouter } from 'react-router-dom';
import UseFormLogin from './loginPart/UseFormLogin';

// axios.defaults.baseURL = "http://localhost:3000";
// axios.defaults.withCredentials = true;
// axios.defaults.crossDomain = true;

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <CookiesProvider>
    <Provider store={store}>
      <UseFormLogin />
    </Provider>
  </CookiesProvider>
    ,{/* <BrowserRouter>
      // <FridgeApp />
    </BrowserRouter> */}
);
