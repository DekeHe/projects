import React, {Component} from 'react';
import 'element-theme-default';
import {Layout, Message} from 'element-react';
import Home from './Home'
import {BrowserRouter as Router, Route} from "react-router-dom";
import CommonHeader from "../components/CommonHeader";
import CommonAside from "../components/CommonAside";
import UserList from "./UserList";
import OfficeList from "./OfficeList";
import DoctorList from "./DoctorList";
import AppointList from "./AppointList";
import AnnounceList from "./AnnounceList";
import axios from "axios";
import {connect} from "react-redux";
class Main extends Component {

    checkLogin(){
        const _this = this;
        const { serverUrl } = _this.props;
        let token = global.tools.getLoginUser();
        if (global.tools.isEmpty(token)) {
            _this.props.history.push('/login');
            window.location.reload();
        }else{
            // 后端token验证
            axios.post(serverUrl+ '/user/check_login',{token: token})
                .then(function (response) {
                    let resp = response.data;
                    if(resp.code === 0){
                        if (global.tools.isEmpty(resp.data.token)) {
                            _this.props.history.push('/login');
                            window.location.reload();
                            Message.error('还未登录或会话失效，请重新登录！');
                        }else{
                            _this.setState({user: resp.data});
                        }
                    }else{
                        _this.props.history.push('/login');
                        window.location.reload();
                        Message.error(resp.msg);
                    }
                }).catch(function (error) {
                Message.error('网络错误，用户登录token获取失败，请重新登录！');
                window.location.reload();
                _this.props.history.push('/login');
            })
        }
    }

    constructor(props) {
        super(props);
        this.state = {
            user: {}
        }
    }

    // react生命周期
    componentDidMount() {
        const _this = this;    //先存一下this，以防使用箭头函数this会指向我们不希望它所指向的对象。
        _this.checkLogin();
    }

    render() {
        const _this = this;
        return (
            <div>
                <Layout.Row gutter="20">
                    <Layout.Col span="4">
                        {/* 公共菜单组件 */}
                        <CommonAside roleId={_this.state.user.roleId}></CommonAside>
                    </Layout.Col>
                    <Layout.Col span="20">
                        {/* 公共顶部组件 */}
                        <CommonHeader></CommonHeader>
                        {/* 路由页面跳转 */}
                        <div key={_this.props.location.key}>
                            <Router>
                                <Route exact path="/" component={Home}></Route>
                                <Route exact path="/user_list" component={UserList}></Route>
                                <Route exact path="/office_list" component={OfficeList}></Route>
                                <Route exact path="/doctor_list" component={DoctorList}></Route>
                                <Route exact path="/appoint_list" component={AppointList}></Route>
                                <Route exact path="/announce_list" component={AnnounceList}></Route>
                            </Router>
                        </div>
                    </Layout.Col>
                </Layout.Row>

            </div>
        );
    }
}

const mapStateToProps = state => ({
    menuName: state.menuName,
    serverUrl: state.serverUrl
});

const mapDispatchToProps = dispatch => ({
    changeName: name => dispatch({type:"changeName", name}),
    changeActiveNum: num => dispatch({type:"changeActiveNum", num})
});



export default connect(mapStateToProps, mapDispatchToProps)(Main);
