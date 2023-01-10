import axios from "axios";




/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log("请求：", config);
    let token = global.tools.getLoginUser();
    console.log("取出的token：", token);
    if (global.tools.isNotEmpty(token)) {
        config.headers.token = token;
        console.log("请求headers增加token:", token);
    }
    return config;
}, error => {});


axios.interceptors.response.use(function (response) {
    console.log("返回结果：", response);
    return response;
}, error => {});
