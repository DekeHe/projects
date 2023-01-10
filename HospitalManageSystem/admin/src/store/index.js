import {createStore} from "redux"

// 状态初始
const initState = {
    menuName: "首页",
    activeNum: "1",
    serverUrl: "http://127.0.0.1:8080"
};

const reducer = (state = initState, action) =>{
    switch (action.type) {
        case "changeName":
            return {
                ...state,
                menuName: action.name
            };
        case "changeActiveNum":
            return {
                ...state,
                activeNum: action.num
            };
        default:
            return state;
    }
};

export default createStore(reducer);