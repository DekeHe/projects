import React, {Component} from 'react';
import {Layout, Card, Table, Message} from 'element-react';
import * as echarts from 'echarts/lib/echarts';
import 'echarts/lib/chart/bar';
import 'echarts/lib/component/tooltip';
import 'echarts/lib/component/title';
import 'echarts/lib/component/grid';
import axios from "axios";
import {connect} from "react-redux";

class Home extends Component {

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
                            _this.setState({user: resp.data}, ()=>{
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

    constructor(props) {
        super(props);

        this.state = {
            columns: [
                {
                    label: "发布时间",
                    prop: "createTime",
                    width: 200
                },
                {
                    label: "发布用户",
                    prop: "userDTO.username",
                    width: 250
                },
                {
                    label: "公告内容",
                    prop: "content"
                }
            ],
            data: [],
            seriesData: [],
            user: {}
        }
    }



    // react生命周期
    componentDidMount() {
        const _this = this;    //先存一下this，以防使用箭头函数this会指向我们不希望它所指向的对象。
        _this.checkLogin();
        const {serverUrl } = _this.props;
        // mount: 挂载, 代表组件首次生成并展示出来
        // 基于准备好的dom，初始化echarts实例
        let myChart = echarts.init(document.getElementById('main'));
        // 获取首页图表数据
        axios.post(serverUrl+ '/appoint/total_date')
            .then(function (response) {
                let list = response.data.data;
                _this.setState({seriesData: list}, () => {
                    _this.initChart(myChart);
                });
            }).catch(function (error) {
            Message.error('网络错误，获取图表数据失败！');
        });
        // 获取首页公告数据
        axios.post(serverUrl+ '/announce/index')
            .then(function (response) {
                let list = response.data.data;
                _this.setState({data: list})
            }).catch(function (error) {
            Message.error('网络错误，获取公告数据失败！');
        })
    }

    initChart(myChart){
        const _this = this;
        // 绘制图表
        myChart.setOption({
            title: { text: '近三天用户挂号数' },
            tooltip: {},
            xAxis: {
                data: [ _this.getDate(3), _this.getDate(2), _this.getDate(1)]
            },
            yAxis: {},
            series: [{
                name: '挂号数',
                type: 'bar',
                data: _this.state.seriesData
            }]
        });
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

    getDate(i) {
        let date;
        switch (i) {
            case 1:
                // 当前日期
                date = new Date();
                break;
            case 2:
                // 昨天日期
                date = new Date(new Date() - 60000*60*24);
                break;
            case 3:
                // 前天日期
                date = new Date(new Date() - 60000*60*24*2);
                break;
            default:
                date = new Date();
                break;
        }
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        let currentDate = year + "年" + month + "月" + strDate + "日";
        return currentDate;
    }

    render() {
        const _this = this;
        let role = "";
        if(_this.state.user.roleId === "1"){
            role = "普通用户";
        }else if(_this.state.user.roleId === "2"){
            role = "医生";
        }else if(_this.state.user.roleId === "3"){
            role = "管理员";
        }
        return (
            <div key={_this.props.location.key}>
                <Layout.Row gutter="20">
                    <Layout.Col span="12">
                        <Card className="box-card" style={{margin: '20px 30px'}}>
                            <div style={cardUserStyle}>
                                <img src={_this.state.user.headPic} alt={''} style={cardImgStyle}/>
                                <div>
                                    <p style={{fontSize: '32px', marginBottom: '10px'}} >{_this.state.user.username}</p>
                                    <p style={{color: '#999999'}}>{role}</p>
                                </div>
                            </div>
                            <div>
                                <p style={{lineHeight:'28px',fontSize: '14px',color: '#999999'}}>手机号码：<span style={{color:' #666666', marginLeft: '60px'}}>{_this.state.user.phone}</span></p>
                                <p style={{lineHeight:'28px',fontSize: '14px',color: '#999999'}}>今日日期：<span style={{color:' #666666', marginLeft: '60px'}}>{_this.getDate(1)}</span></p>
                            </div>
                        </Card>
                    </Layout.Col>


                    <Layout.Col span="12">
                        <Card className="box-card" style={{margin: '20px', marginLeft: "0px"}}>
                            <div id="main" style={{ width: 530, height: 275 }}>

                            </div>
                        </Card>
                    </Layout.Col>
                </Layout.Row>
                <Layout.Row gutter="20">
                    <Layout.Col span="24">
                        <div style={{margin: '20px 30px', marginRight: "20px"}}>
                            <Table border={true} height={260} columns={_this.state.columns} maxHeight={260} data={_this.state.data}/>
                        </div>
                    </Layout.Col>
                </Layout.Row>
            </div>
        );
    }
}

const cardImgStyle={
    width: '150px',
    height: '150px',
    borderRadius: '50%',
    marginRight: '40px'
};

const cardUserStyle={
    display: 'flex',
    alignItems: 'center',
    paddingBottom: '20px',
    marginBottom: '20px',
    borderBottom:' 1px solid #ccc'
};

const mapStateToProps = state => ({
    menuName: state.menuName,
    serverUrl: state.serverUrl
});

const mapDispatchToProps = dispatch => ({
    changeName: name => dispatch({type:"changeName", name}),
    changeActiveNum: num => dispatch({type:"changeActiveNum", num})
});



export default connect(mapStateToProps, mapDispatchToProps)(Home);
