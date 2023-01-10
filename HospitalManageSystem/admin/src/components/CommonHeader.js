import React, {Component} from 'react';
import {Dropdown, Message} from "element-react";
import {connect} from "react-redux";
import axios from "axios";

class CommonHeader extends Component {

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
                            _this.setState({token: resp.data.token, user: resp.data}, ()=>{
                                _this.initPhoto();
                            });
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

    logout(){
        const _this = this;
        const { serverUrl } =_this.props;
        axios.post(serverUrl+ '/user/logout',{token: _this.state.token})
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    _this.setState({token: ""}, ()=>{
                        global.tools.setLoginUser("");
                        Message.success(resp.msg);
                        window.location.reload();
                    });
                }
            }).catch(function (error) {
            Message.error('网络错误，退出登录失败！');
        })
    }

    constructor(props) {
        super(props);

        this.state = {
            token: "",
            user: {}
        }
    }

    // react生命周期
    componentDidMount() {
        const _this = this;    //先存一下this，以防使用箭头函数this会指向我们不希望它所指向的对象。
        _this.checkLogin();
    }

    initPhoto(){
        const _this = this;
        const { serverUrl } = _this.props;
        let user = {
            ..._this.state.user,
            headPic: serverUrl + "/photo/view?filename=" + _this.state.user.headPic
        };
        _this.setState({user: user});
    }

    render() {
        const { menuName } = this.props;
        const _this = this;
        return (
            <div>
                <header style={headStyle}>
                    <div style={{display: 'flex', alignItems: 'center'}}>
                        <h4 style={{color:'#fff',marginLeft:'50px'}} >{menuName}</h4>
                    </div>
                    <div>
                        <Dropdown onCommand={_this.logout.bind(_this)} trigger="click" menu={(
                            <Dropdown.Menu>
                                <Dropdown.Item>退出登录</Dropdown.Item>
                            </Dropdown.Menu>
                        )}>
                                <span className="el-dropdown-link">
                                    <img src={_this.state.user.headPic} alt={''} style={headPicStyle}/>
                                </span>
                        </Dropdown>
                    </div>
                </header>
            </div>
        );
    }
}




const headStyle = {
    display: 'flex',
    height: '100%',
    background: '#8492A6',
    alignItems: 'center',
    justifyContent: 'space-between'
};

const headPicStyle={
    width: '40px',
    height: '40px',
    borderRadius: '50%',
    marginRight: '30px'
};

const mapStateToProps = state => ({
    menuName: state.menuName,
    serverUrl: state.serverUrl
});

const mapDispatchToProps = dispatch => ({
    changeName: name => dispatch({type:"changeName", name}),
    changeActiveNum: num => dispatch({type:"changeActiveNum", num})
});



export default connect(mapStateToProps, mapDispatchToProps)(CommonHeader);

