import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import './utils/request'
import './utils/session-storage'
import './utils/tool'
import App from './App';
// 嵌入：把 redux管理的state嵌入到整个项目中去
import store from './store'
import { Provider } from "react-redux";

ReactDOM.render(
    <Provider store={store}>
        <App />
    </Provider>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
//reportWebVitals();
