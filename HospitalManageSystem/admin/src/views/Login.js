import React, {Component} from 'react';
import {Card, Form, Input, Button, Radio, Message} from "element-react";
import imgURL from "../img/hospital.png"
import axios from "axios";
import {connect} from "react-redux";
class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            form: {
                username: "",
                password: "",
                rePassword: "",
                phone: "",
                roleId: "1"
            },
            title: "医院挂号管理系统登录",
            rePasswordVisible: false,
            phoneVisible: false,
            buttonText: "登录",
            type: "login",
            navigateText: "还没有账号，快注册！"
        }
    }
    render() {
        const _this = this;
        let rePasswordItem = null;
        if(_this.state.rePasswordVisible){
            rePasswordItem = (
                <Form.Item label="确认密码" labelWidth="80">
                    <Input type={"password"} onChange={_this.changeInput.bind(_this, 'rePassword')} value={_this.state.form.rePassword}  style={{width: '250px'}} placeholder="请输入确认密码"/>
                </Form.Item>
            );
        }
        let phoneItem = null;
        if(_this.state.phoneVisible){
            phoneItem = (
                <Form.Item label="手机号码" labelWidth="80">
                    <Input value={_this.state.form.phone} onChange={_this.changeInput.bind(_this, 'phone')} style={{width: '250px'}} placeholder="请输入手机号码"/>
                </Form.Item>
            );
        }
        let roleItem = null;
        if(_this.state.type === "login"){
            roleItem = (
                <span style={{marginLeft: "80px"}}>
                    <Radio value="1" checked={_this.state.form.roleId === "1"} onChange={_this.onChange.bind(_this)}>普通用户</Radio>
                    <Radio value="2" checked={_this.state.form.roleId === "2"} onChange={_this.onChange.bind(_this)}>医生</Radio>
                    <Radio value="3" checked={_this.state.form.roleId === "3"} onChange={_this.onChange.bind(_this)}>管理员</Radio>
                </span>
            );
        }
        return (
            <div style={loginStyle}>
                <Card className="box-card" style={cardLoginStyle}>
                    <h3 style={{textAlign: "center"}}>{_this.state.title}</h3>
                    <Form model={_this.state.form} style={{position:"absolute"}}>
                        <Form.Item label="用户名称" labelWidth="80">
                            <Input value={_this.state.form.username} onChange={_this.changeInput.bind(_this, 'username')}  style={{width: '250px'}} placeholder="请输入用户名称"/>
                        </Form.Item>
                        <Form.Item label="用户密码" labelWidth="80">
                            <Input type={"password"} value={_this.state.form.password} onChange={_this.changeInput.bind(_this, 'password')}  style={{width: '250px'}} placeholder="请输入用户密码"/>
                        </Form.Item>
                        {roleItem}
                        {rePasswordItem}
                        {phoneItem}
                        <span style={{textAlign:"center",display:"block", marginTop:"20px"}}><Button style={{width:"360px"}} type="info" onClick={_this.submitForm.bind(_this)}>{_this.state.buttonText}</Button></span>
                        <div style={{fontSize:"12px",textAlign:"center",marginTop:"10px"}}><span style={{cursor: "pointer"}} onClick={_this.changePage.bind(_this)}>{_this.state.navigateText}</span></div>
                    </Form>

                </Card>
            </div>
        );
    }

    // 输入内容双向绑定实现
    changeInput(type, e){
        // type:对象成员  e:变化值
        const _this = this;
        let target= Object.assign({}, _this.state.form, {
            [type]: e
        });
        //console.log("解构：" + JSON.stringify(target));
        _this.setState({
            form: target
        })
    }

    // 复选框点击切换
    onChange(e){
        const _this = this;
        let form = {
            ..._this.state.form,
            roleId: e
        };
        _this.setState({form: form});
    }


    // 提交表单操作处理
    submitForm(){
        const _this = this;
        const { serverUrl } = _this.props;
        if(_this.state.type === "login"){
            // 如果是登录操作
            axios.post(serverUrl+ '/user/login', _this.state.form)
                .then(function (response) {
                    let resp = response.data;
                    if(resp.code === 0){
                        global.tools.setLoginUser(resp.data.token);
                        Message.success(resp.msg);
                        _this.props.history.push('/');
                    }else{
                        Message.error(resp.msg);
                    }
                }).catch(function (error) {
                Message.error('网络错误，登录失败！');
            })
        }else if(_this.state.type === "register"){
            // 如果是注册操作
            axios.post(serverUrl+ '/user/register', _this.state.form)
                .then(function (response) {
                    let resp = response.data;
                    if(resp.code === 0){
                        Message.success(resp.msg);
                    }else{
                        Message.error(resp.msg);
                    }
                }).catch(function (error) {
                Message.error('网络错误，注册用户失败！');
            })
        }
    }

    // 点击跳转
    changePage(){
        const _this = this;
        if(_this.state.type === "login"){
            _this.setState({title:"医院挂号管理系统注册", phoneVisible:true, rePasswordVisible:true,
                buttonText: "注册", navigateText: "已有账号，去登录！", type: "register"
            });
            cardLoginStyle.height = "400px";
        }else if(_this.state.type === "register"){
            _this.setState({title:"医院挂号管理系统登录", phoneVisible:false, rePasswordVisible:false,
                buttonText: "登录", navigateText: "还没有账号，快注册！", type: "login"
            });
            cardLoginStyle.height = "330px";
        }
    }
}

const cardLoginStyle = {
    width: "400px",
    height: "330px",
    position: "absolute",
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
    margin: "auto",
    background:" rgba(255, 255, 255, 0.4)"
};

const loginStyle = {
    backgroundImage: "url(" + imgURL + ")" ,
    backgroundPosition: "center",
    width: "100%",
    height:"100%",
    backgroundSize: "cover",
    position: "fixed",
    borderWidth: "3px"
};

const mapStateToProps = state => ({
    menuName: state.menuName,
    serverUrl: state.serverUrl
});

const mapDispatchToProps = dispatch => ({
    changeName: name => dispatch({type:"changeName", name}),
    changeActiveNum: num => dispatch({type:"changeActiveNum", num})
});

export default connect(mapStateToProps, mapDispatchToProps)(Login);
