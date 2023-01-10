import React, {Component} from 'react';
import {Button, Table, Input, Message, Pagination, Dialog, Form, Select, Steps} from "element-react";
import {connect} from "react-redux";
import axios from "axios";

class AppointList extends Component {

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

    changePage(e){
        const _this = this;
        _this.state.pageData.currentPage = e;
        _this.getAppointList();
    };

    // 获取预约挂号列表数据
    getAppointList(){
        const _this = this;
        const {serverUrl } = _this.props;
        let data = {
            searchContent: _this.state.form.searchContent,
            page: _this.state.pageData.currentPage
        };
        axios.post(serverUrl+ '/appoint/list',data)
            .then(function (response) {
                let list = response.data.data.list;
                let data = response.data.data;
                list.map((item) =>{
                        if(item.state === 1){
                            item.state = "待就诊";
                        }else if(item.state === 2){
                            item.state = "已就诊";
                        }else if(item.state === 3){
                            item.state = "已取消";
                        }
                        return item;
                    }
                );
                let pageData = {
                    totalPage: data.totalPage,
                    pageSize: data.size,
                    currentPage: data.page
                };
                _this.setState({data: list, pageData: pageData})
            }).catch(function (error) {
            Message.error('网络错误，获取预约挂号数据失败！');
        })
    }

    // react生命周期
    componentDidMount() {
        const _this = this;    //先存一下this，以防使用箭头函数this会指向我们不希望它所指向的对象。
        _this.checkLogin();
        // mount: 挂载, 代表组件首次生成并展示出来
        const { changeName, changeActiveNum } = _this.props;
        changeName("挂号管理 / 挂号列表");
        changeActiveNum("5-1");
        _this.getAppointList();
    }

    // 表格初始化
    constructor(props) {
        super(props);
        this.state = {
            user: {},
            dialogVisible: false,
            itemDialogVisible: false,
            viewDialogVisible: false,
            stepLength: 0,
            appointItemList: [],
            form: {
                id: "",
                reply: "",
                time: "",
                state: "",
                content: "",
                searchContent: ""
            },
            selectItem: {},
            pageData: {
                totalPage: 1,
                pageSize: 5,
                currentPage: 1
            },
            columns: [
                {
                    label: "编号",
                    prop: "id",
                    width: 120,
                    align: "center"
                },
                {
                    label: "挂号时间",
                    prop: "time",
                    width: 180,
                    align: "center"
                },
                {
                    label: "挂号用户",
                    prop: "userDTO.username",
                    width: 180,
                    align: "center"
                },
                {
                    label: "挂号医生",
                    prop: "doctorDTO.name",
                    width: 180,
                    align: "center"
                },
                {
                    label: "挂号状态",
                    prop: "state",
                    width: 120,
                    align: "center"
                },
                {
                    label: "挂号备注",
                    prop: "info",
                    width: 250,
                    align: "center"
                },
                {
                    label: "医生回复",
                    prop: "reply",
                    width: 250,
                    align: "center"
                }
            ],
            data: []
        }
    }

    render() {
        const _this = this;
        return (
            <div key={_this.props.location.key}  style={{margin: '20px 30px'}}>
                <span className="wrapper">
                    {
                        _this.state.user.roleId === "2" ||  _this.state.user.roleId === "3" ?
                        <Button type="success" onClick={_this.openAddItem.bind(_this)}><i className="el-icon-plus"></i> 添加记录</Button>
                        : null
                    }
                    {
                        _this.state.user.roleId === "1" ||  _this.state.user.roleId === "3" ?
                        <Button type="danger" onClick={_this.cancelAppoint.bind(_this)}><i className="el-icon-circle-cross"></i> 取消</Button>
                        : null
                    }
                    {
                        _this.state.user.roleId === "3" ?
                        <Button type="danger" onClick={_this.deleteAppoint.bind(_this)}><i className="el-icon-delete"></i> 删除</Button>
                        : null
                    }
                    <Button type="primary" onClick={_this.openViewItem.bind(_this)}><i className="el-icon-view"></i> 查看详情</Button>
                    {
                        _this.state.user.roleId === "2" ||  _this.state.user.roleId === "3" ?
                        <Button type="warning" onClick={_this.openEdit.bind(_this)}><i className="el-icon-edit"></i> 修改</Button>
                        : null
                    }
                    <Input defaultValue={_this.state.form.searchContent} onChange={_this.changeInput.bind(_this, 'searchContent')} style={{width: '300px', marginLeft: '20px'}} placeholder="请输入预约挂号的编号" />
                  <Button onClick={_this.search.bind(_this)} style={{marginLeft: "5px"}} type="primary" icon="search">搜索</Button>
                </span>
                <Table
                    style={{width: '100%',marginTop: '30px'}}
                    columns={_this.state.columns}
                    data={_this.state.data}
                    border={true}
                    height={500}
                    highlightCurrentRow={true}
                    onCurrentChange={item=>{_this.saveItem(item)}}
                />
                <Pagination layout="prev, pager, next"  style={{marginTop:'20px'}} currentPage={_this.state.pageData.currentPage} pageSize={_this.state.pageData.pageSize} onCurrentChange={_this.changePage.bind(_this)} pageCount={_this.state.pageData.totalPage}/>
                <Dialog
                    title="编辑预约挂号信息"
                    visible={ _this.state.dialogVisible }
                    onCancel={ () => _this.setState({ dialogVisible: false }) }
                >
                    <Dialog.Body>
                        <Form model={_this.state.form}>
                            <Form.Item label="挂号编号" labelWidth="120">
                                <span>{_this.state.form.id}</span>
                            </Form.Item>
                            <Form.Item label="挂号时间" labelWidth="120">
                                <span>{_this.state.form.time}</span>
                            </Form.Item>
                            <Form.Item label="挂号状态" labelWidth="120">
                                <Select value={_this.state.form.state} onChange={_this.changeInput.bind(_this, 'state')} placeholder="请选择挂号状态">
                                    <Select.Option label="待就诊" value="1"></Select.Option>
                                    <Select.Option label="已就诊" value="2"></Select.Option>
                                    <Select.Option label="已取消" value="3"></Select.Option>
                                </Select>
                            </Form.Item>
                            <Form.Item label="医生回复" labelWidth="120">
                                <Input type="textarea" value={_this.state.form.reply} onChange={_this.changeInput.bind(_this, 'reply')} autosize={{ minRows: 3}} style={{width: '400px'}} placeholder="请输入医生回复"/>
                            </Form.Item>
                        </Form>
                    </Dialog.Body>

                    <Dialog.Footer className="dialog-footer">
                        <Button onClick={ () => _this.setState({ dialogVisible: false }) }>取 消</Button>
                        <Button type="primary" onClick={_this.editAppoint.bind(_this)}>确 定</Button>
                    </Dialog.Footer>
                </Dialog>

                <Dialog title="添加挂号详情记录" visible={ _this.state.itemDialogVisible } onCancel={ () => _this.setState({ itemDialogVisible: false }) }>
                    <Dialog.Body>
                        <Form model={_this.state.form}>
                            <Form.Item label="挂号编号" labelWidth="120">
                                <span>{_this.state.form.id}</span>
                            </Form.Item>
                            <Form.Item label="挂号时间" labelWidth="120">
                                <span>{_this.state.form.time}</span>
                            </Form.Item>
                            <Form.Item label="记录内容" labelWidth="120">
                                <Input type="textarea" value={_this.state.form.content} onChange={_this.changeInput.bind(_this, 'content')} autosize={{ minRows: 3}} style={{width: '400px'}} placeholder="请输入记录内容"/>
                            </Form.Item>
                        </Form>
                    </Dialog.Body>

                    <Dialog.Footer className="dialog-footer">
                        <Button onClick={ () => _this.setState({ itemDialogVisible: false }) }>取 消</Button>
                        <Button type="primary"  onClick={_this.saveAppointItem.bind(_this)}>确 定</Button>
                    </Dialog.Footer>
                </Dialog>

                <Dialog title="查看挂号详情记录" visible={ _this.state.viewDialogVisible }  onCancel={ () => _this.setState({ viewDialogVisible: false }) }>
                    <Dialog.Body>
                        <Form model={_this.state.form}>
                            <Form.Item label="挂号编号" labelWidth="120">
                                <span>{_this.state.form.id}</span>
                            </Form.Item>
                            <Form.Item label="挂号时间" labelWidth="120">
                                <span>{_this.state.form.time}</span>
                            </Form.Item>
                            <Steps style={{marginLeft: "50px"}} active={_this.state.stepLength} space={100} direction="vertical">
                                {
                                    _this.state.appointItemList.map((item,i) => (
                                        <Steps.Step key={i} title={item.content+"  ("+item.createTime+")"}></Steps.Step>
                                    ))
                                }
                            </Steps>

                        </Form>
                    </Dialog.Body>

                    <Dialog.Footer className="dialog-footer">
                        <Button type="primary" onClick={ () => _this.setState({ viewDialogVisible: false }) }>关 闭</Button>
                    </Dialog.Footer>
                </Dialog>
            </div>
        );
    }

    // 打开查看挂号详情记录的窗口
    openViewItem(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行查看！');
            return false;
        }
        const {serverUrl } = _this.props;
        let form = {
            ..._this.state.selectItem,
        };
        if(form.state === "待就诊"){
            form.state = "1";
        }else if(form.state === "已就诊"){
            form.state = "2";
        }else if(form.state === "已取消"){
            form.state = "3";
        }
        _this.state.form = form;
        // 获取挂号记录详情
        axios.post(serverUrl+ '/appoint/list_item', form)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    _this.setState({appointItemList: resp.data, stepLength: resp.data.length}, () => {
                        _this.setState({ viewDialogVisible: true});
                    });
                }else{
                    Message.error(resp.msg);
                }
            }).catch(function (error) {
            Message.error("网络错误，获取挂号详细记录数据失败！");
        })
    }

    // 搜索操作
    search(){
        const _this = this;
        _this.state.pageData.currentPage = 1;
        _this.getAppointList();
    }

    // 添加挂号记录操作
    saveAppointItem(){
        const _this = this;
        const {serverUrl } = _this.props;
        // 添加挂号记录操作
        axios.post(serverUrl+ '/appoint/save_item',_this.state.form)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    successOption.message = resp.msg;
                    Message.success(successOption);
                    _this.getAppointList();
                    _this.setState({ itemDialogVisible: false, selectItem: {}});
                }else{
                    errorOption.message = resp.msg;
                    Message.error(errorOption);
                }
            }).catch(function (error) {
            errorOption.message = "网络错误，添加挂号记录数据失败！";
            Message.error(errorOption);
        })
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

    // 打开添加挂号详细记录的窗口
    openAddItem(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行添加！');
            return false;
        }
        let form = {
            ..._this.state.selectItem
        };
        if(form.state === "待就诊"){
            form.state = "1";
        }else if(form.state === "已就诊"){
            form.state = "2";
        }else if(form.state === "已取消"){
            form.state = "3";
        }
        // setState 有延迟(更新数据+更新UI) 用回调确保数据更新完毕
        _this.setState({form: form}, () => {
            _this.setState({ itemDialogVisible: true});
        });
    }

    // 取消预约挂号操作
    cancelAppoint(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行取消！');
            return false;
        }
        const {serverUrl} = _this.props;
        let form = {
            ..._this.state.selectItem,
        };
        if(form.state === "待就诊"){
            form.state = 1;
        }else if(form.state === "已就诊"){
            form.state = 2;
        }else if(form.state === "已取消"){
            form.state = 3;
        }
        _this.state.form = form;
        axios.post(serverUrl+ '/appoint/cancel', form)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    Message.success(resp.msg);
                    _this.getAppointList();
                    _this.setState({selectItem: {}});
                }else{
                    Message.error(resp.msg);
                }
            }).catch(function (error) {
            Message.error('网络错误，取消预约挂号失败！');
        })

    }

    // 删除预约挂号操作
    deleteAppoint(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行删除！');
            return false;
        }
        const {serverUrl} = _this.props;
        let form = {
            ..._this.state.selectItem,
        };
        if(form.state === "待就诊"){
            form.state = 1;
        }else if(form.state === "已就诊"){
            form.state = 2;
        }else if(form.state === "已取消"){
            form.state = 3;
        }
        _this.state.form = form;
        axios.post(serverUrl+ '/appoint/delete', form)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    Message.success(resp.msg);
                    _this.getAppointList();
                    _this.setState({selectItem: {}});
                }else{
                    Message.error(resp.msg);
                }
            }).catch(function (error) {
            Message.error('网络错误，删除预约挂号数据失败！');
        })
    }

    // 存储选中行
    saveItem(item){
        const _this = this;
        _this.setState({selectItem: item});
    }


    // 编辑预约挂号数据
    editAppoint(){
        const _this = this;
        const {serverUrl } = _this.props;
        // 编辑操作
        axios.post(serverUrl+ '/appoint/edit',_this.state.form)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    successOption.message = resp.msg;
                    Message.success(successOption);
                    _this.getAppointList();
                    _this.setState({ dialogVisible: false, selectItem: {}});
                }else{
                    errorOption.message = resp.msg;
                    Message.error(errorOption);
                }
            }).catch(function (error) {
            errorOption.message = "网络错误，编辑预约挂号数据失败！";
            Message.error(errorOption);
        })
    }

    // 打开编辑预约挂号的窗口
    openEdit(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行编辑！');
            return false;
        }
        let form = {
            ..._this.state.selectItem
        };
        if(form.state === "待就诊"){
            form.state = "1";
        }else if(form.state === "已就诊"){
            form.state = "2";
        }else if(form.state === "已取消"){
            form.state = "3";
        }
        // setState 有延迟(更新数据+更新UI) 用回调确保数据更新完毕
        _this.setState({form: form}, () => {
            _this.setState({ dialogVisible: true});
        });
    }

}
const successOption = {
    type: "success",
    message: "",
    customClass: 'zZindex'
};

const errorOption = {
    type: "error",
    message: "",
    customClass: 'zZindex'
};

const mapStateToProps = state => ({
    menuName: state.menuName,
    serverUrl: state.serverUrl
});

const mapDispatchToProps = dispatch => ({
    changeName: name => dispatch({type:"changeName", name}),
    changeActiveNum: num => dispatch({type:"changeActiveNum", num})
});



export default connect(mapStateToProps, mapDispatchToProps)(AppointList);
