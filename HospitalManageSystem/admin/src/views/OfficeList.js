import React, {Component} from 'react';
import {Button, Table, Input, Message, Pagination, Dialog, Form} from "element-react";
import {connect} from "react-redux";
import axios from "axios";

class OfficeList extends Component {

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
        _this.getOfficeList();
    };

    // 获取科室列表数据
    getOfficeList(){
        const _this = this;
        const {serverUrl } = _this.props;
        let data = {
            searchContent: _this.state.form.searchContent,
            page: _this.state.pageData.currentPage
        };
        axios.post(serverUrl+ '/office/list',data)
            .then(function (response) {
                let list = response.data.data.list;
                let data = response.data.data;
                let pageData = {
                    totalPage: data.totalPage,
                    pageSize: data.size,
                    currentPage: data.page
                };
                _this.setState({data: list, pageData: pageData})
            }).catch(function (error) {
            Message.error('网络错误，获取科室数据失败！');
        })
    }

    // react生命周期
    componentDidMount() {
        const _this = this;    //先存一下this，以防使用箭头函数this会指向我们不希望它所指向的对象。
        _this.checkLogin();
        // mount: 挂载, 代表组件首次生成并展示出来
        const { changeName, changeActiveNum } = _this.props;
        changeName("科室管理 / 科室列表");
        changeActiveNum("3-1");
        _this.getOfficeList();
    }

    // 表格初始化
    constructor(props) {
        super(props);
        this.state = {
            dialogVisible: false,
            title: "",
            form: {
                id: "",
                name: "",
                info: "",
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
                    width: 150,
                    align: "center"
                },
                {
                    label: "科室名称",
                    prop: "name",
                    width: 300,
                    align: "center"
                },
                {
                    label: "科室简介",
                    prop: "info",
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
                  <Button type="success" onClick={ () => _this.setState({ dialogVisible: true, title: "添加科室信息", form: {..._this.state.form,id:""}}) }><i className="el-icon-plus"></i>添加</Button>
                  <Button type="warning" onClick={_this.openEdit.bind(_this)}><i className="el-icon-edit"></i> 修改</Button>
                  <Button type="danger" onClick={_this.deleteOffice.bind(_this)}><i className="el-icon-delete"></i>删除</Button>
                  <Input defaultValue={_this.state.form.searchContent} onChange={_this.changeInput.bind(_this, 'searchContent')} style={{width: '300px', marginLeft: '20px'}} placeholder="请输入科室名称" />
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
                    title={_this.state.title}
                    visible={ _this.state.dialogVisible }
                    onCancel={ () => _this.setState({ dialogVisible: false }) }
                >
                    <Dialog.Body>
                        <Form model={_this.state.form}>
                            <Form.Item label="科室名称" labelWidth="120">
                                <Input value={_this.state.form.name} onChange={_this.changeInput.bind(_this, 'name')} style={{width: '400px'}}></Input>
                            </Form.Item>
                            <Form.Item label="科室简介" labelWidth="120">
                                <Input type="textarea" value={_this.state.form.info} onChange={_this.changeInput.bind(_this, 'info')} autosize={{ minRows: 3}} style={{width: '400px'}} placeholder="请输入科室简介"/>
                            </Form.Item>
                        </Form>
                    </Dialog.Body>

                    <Dialog.Footer className="dialog-footer">
                        <Button onClick={ () => _this.setState({ dialogVisible: false }) }>取 消</Button>
                        <Button type="primary" onClick={_this.saveOffice.bind(_this)}>确 定</Button>
                    </Dialog.Footer>
                </Dialog>
            </div>
        );
    }

    search(){
        const _this = this;
        _this.state.pageData.currentPage = 1;
        _this.getOfficeList();
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

    // 删除科室操作
    deleteOffice(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行删除！');
            return false;
        }
        const {serverUrl} = _this.props;
        let form = {
            ..._this.state.selectItem,
        };
        _this.state.form = form;
        axios.post(serverUrl+ '/office/delete', _this.state.form)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    Message.success(resp.msg);
                    _this.getOfficeList();
                    _this.setState({selectItem: {}});
                }else{
                    Message.error(resp.msg);
                }
            }).catch(function (error) {
            Message.error('网络错误，删除科室数据失败！');
        })
    }

    // 存储选中行
    saveItem(item){
        const _this = this;
        _this.setState({selectItem: item});
    }


    // 保存科室数据
    saveOffice(){
        const _this = this;
        const {serverUrl } = _this.props;
        // 保存操作
        axios.post(serverUrl+ '/office/save',_this.state.form)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    successOption.message = resp.msg;
                    Message.success(successOption);
                    _this.getOfficeList();
                    _this.setState({ dialogVisible: false, selectItem: {}});
                }else{
                    errorOption.message = resp.msg;
                    Message.error(errorOption);
                }
            }).catch(function (error) {
            errorOption.message = "网络错误，保存科室数据失败！";
            Message.error(errorOption);
        })
    }

    // 打开编辑科室窗口
    openEdit(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行编辑！');
            return false;
        }
        let form = {
            ..._this.state.selectItem
        };
        // setState 有延迟(更新数据+更新UI) 用回调确保数据更新完毕
        _this.setState({form: form}, () => {
            _this.setState({ dialogVisible: true, title: "编辑科室信息"});
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



export default connect(mapStateToProps, mapDispatchToProps)(OfficeList);
