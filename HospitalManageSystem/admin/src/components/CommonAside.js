import React, {Component} from 'react';
import {Menu} from "element-react";
import {Link} from "react-router-dom";
import {connect} from "react-redux";

class CommonAside extends Component {



    render() {
        const _this = this;
        const { changeName, activeNum } = this.props;
        console.log("ss:"+_this.props.roleId);
        return (
            <div>
                <Menu defaultActive={activeNum} className="el-menu-vertical-demo" style={{height:'700px',width:'250px'}}>
                    <h2 style={{textAlign: 'center', lineHeight: '48px'}}>医院挂号管理系统</h2>
                    <Link to="/" onClick={()=>{changeName('首页')}} style={{textDecoration: 'none'}}><Menu.Item index="1" ><i className="el-icon-menu"></i>首页</Menu.Item></Link>
                    {
                        _this.props.roleId === "1" ||  _this.props.roleId === "3" ?
                        <Menu.SubMenu index="2" title={<span><i className="el-icon-information"></i>用户管理</span>}>
                            <Link onClick={()=>{changeName('用户管理 / 用户列表')}} to="/user_list" style={{textDecoration: 'none'}}><Menu.Item index="2-1"><i className="el-icon-document"></i>用户列表</Menu.Item></Link>
                        </Menu.SubMenu> : null
                    }
                    {
                        _this.props.roleId === "3" ?
                        <Menu.SubMenu index="3" title={<span><i className="el-icon-star-on"></i>科室管理</span>}>
                            <Link onClick={()=>{changeName('科室管理 / 科室列表')}} to="/office_list" style={{textDecoration: 'none'}}><Menu.Item index="3-1"><i className="el-icon-document"></i>科室列表</Menu.Item></Link>
                        </Menu.SubMenu>  : null

                    }
                    <Menu.SubMenu index="4" title={<span><i className="el-icon-setting"></i>医生管理</span>}>
                        <Link onClick={()=>{changeName('医生管理 / 医生列表')}} to="/doctor_list" style={{textDecoration: 'none'}}><Menu.Item index="4-1"><i className="el-icon-document"></i>医生列表</Menu.Item></Link>
                    </Menu.SubMenu>
                    <Menu.SubMenu index="5" title={<span><i className="el-icon-time"></i>挂号管理</span>}>
                        <Link onClick={()=>{changeName('挂号管理 / 挂号列表')}} to="/appoint_list" style={{textDecoration: 'none'}}><Menu.Item index="5-1"><i className="el-icon-document"></i>挂号列表</Menu.Item></Link>
                    </Menu.SubMenu>
                    {
                        _this.props.roleId === "3" ?
                        <Menu.SubMenu index="6" title={<span><i className="el-icon-message"></i>公告管理</span>}>
                            <Link onClick={()=>{changeName('公告管理 / 公告列表')}} to="/announce_list" style={{textDecoration: 'none'}}><Menu.Item index="6-1"><i className="el-icon-document"></i>公告列表</Menu.Item></Link>
                        </Menu.SubMenu> : null
                    }
                </Menu>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    menuName: state.menuName,
    activeNum: state.activeNum
});

const mapDispatchToProps = dispatch => ({
    changeName: name => dispatch({type:"changeName", name}),
    changeActiveNum: num => dispatch({type:"changeActiveNum", num})
});


export default connect(mapStateToProps, mapDispatchToProps)(CommonAside);
