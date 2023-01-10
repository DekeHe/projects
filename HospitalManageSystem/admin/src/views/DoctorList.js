import React, {Component} from 'react';
import {Button, Table, Input, Message, Pagination, Dialog, Form, Select, DatePicker} from "element-react";
import {connect} from "react-redux";
import axios from "axios";
import $ from 'jquery' ;

class DoctorList extends Component {

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
        _this.getDoctorList();
    };

    // 获取医生列表数据
    getDoctorList(){
        const _this = this;
        const {serverUrl } = _this.props;
        let data = {
            searchContent: _this.state.form.searchContent,
            page: _this.state.pageData.currentPage
        };
        axios.post(serverUrl+ '/doctor/list',data)
            .then(function (response) {
                let list = response.data.data.list;
                let data = response.data.data;
                list.map((item) =>{
                        item.headPic = <img src={serverUrl + "/photo/view?filename=" + item.headPic} alt={item.headPic} style={{width:'90px',height: '70px',marginTop: '5px'}} />;
                        if(item.sex === 1){
                            item.sex = "男";
                        }else if(item.sex === 2){
                            item.sex = "女";
                        }else if(item.sex === 3){
                            item.sex = "未知";
                        }
                        if(item.position === "1"){
                            item.position = "住院医师";
                        }else if(item.position === "2"){
                            item.position = "主治医师";
                        }else if(item.position === "3"){
                            item.position = "主任医师";
                        }else if(item.position === "4"){
                            item.position = "副主任医师";
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
            Message.error('网络错误，获取医生数据失败！');
        })
    }

    // 获取所有科室数据
    getOfficeList(){
        const _this = this;
        const {serverUrl } = _this.props;
        axios.post(serverUrl+ '/office/all')
            .then(function (response) {
                let resp = response.data;
                _this.setState({officeList: resp.data})
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
        changeName("医生管理 / 医生列表");
        changeActiveNum("4-1");
        _this.getDoctorList();
        _this.getOfficeList();
    }

    // 表格初始化
    constructor(props) {
        super(props);
        const {serverUrl} = props;
        this.state = {
            user: {},
            dialogVisible: false,
            appointDialogVisible: false,
            title: "",
            form: {
                id: "",
                name: "",
                password: "",
                headPic: "common/no_image.jpg",
                photoView: serverUrl +"/photo/view?filename=common/no_image.jpg",
                sex: "3",
                phone: "",
                info: "",
                officeId: "",
                position: "",
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
                    label: "医生名称",
                    prop: "name",
                    width: 180,
                    align: "center"
                },
                {
                    label: "医生头像",
                    prop: "headPic",
                    width: 130,
                    align: "center"
                },
                {
                    label: "医生性别",
                    prop: "sex",
                    width: 120,
                    align: "center"
                },
                {
                    label: "医生手机号",
                    prop: "phone",
                    width: 200,
                    align: "center"
                },
                {
                    label: "所属科室",
                    prop: "officeDTO.name",
                    width: 120,
                    align: "center"
                },
                {
                    label: "医生职位",
                    prop: "position",
                    width: 120,
                    align: "center"
                },
                {
                    label: "医生简介",
                    prop: "info",
                    width: 300,
                    align: "center"
                }

            ],
            data: [],
            officeList: [],
            appoint:{
                date: "",
                info: ""
            }
        }
    }

    render() {
        const _this = this;
        return (
            <div key={_this.props.location.key}  style={{margin: '20px 30px'}}>
                <span className="wrapper">
                    {
                        _this.state.user.roleId === "3" ?
                        <Button type="success" onClick={ () => _this.setState({ dialogVisible: true, title: "添加医生信息",form: {..._this.state.form,id:""}}) }><i className="el-icon-plus"></i>添加</Button>
                        : null
                    }
                    {
                        _this.state.user.roleId === "2" || _this.state.user.roleId === "3" ?
                        <Button type="warning" onClick={_this.openEdit.bind(_this)}><i className="el-icon-edit"></i> 修改</Button>
                        : null
                    }
                    {
                        _this.state.user.roleId === "3" ?
                        <Button type="danger" onClick={_this.deleteDoctor.bind(_this)}><i className="el-icon-delete"></i>删除</Button>
                        : null
                    }
                    {
                        _this.state.user.roleId === "1" || _this.state.user.roleId === "3" ?
                        <Button type="primary" onClick={_this.openAppointDialog.bind(_this)}><i className="el-icon-date"></i> 挂号</Button>
                        : null
                    }
                    <Input defaultValue={_this.state.form.searchContent} onChange={_this.changeInput.bind(_this, 'searchContent')} style={{width: '300px', marginLeft: '20px'}} placeholder="请输入医生名称" />
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
                            <Form.Item label="医生头像" labelWidth="120">
                                <input type={"file"} style={{display: 'none'}} id={"photo-file"} onChange={_this.upload.bind(_this)}/>
                                <img src={_this.state.form.photoView} alt={""} style={{width:'90px', height: '70px'}} />
                                <Button type="primary" onClick={_this.uploadPhoto} style={uploadButtonStyle}><i className="el-icon-upload"></i>上传图片</Button>
                            </Form.Item>
                            <Form.Item label="医生名称" labelWidth="120">
                                <Input value={_this.state.form.name} onChange={_this.changeInput.bind(_this, 'name')} style={{width: '400px'}}></Input>
                            </Form.Item>
                            <Form.Item label="医生密码" labelWidth="120">
                                <Input value={_this.state.form.password} type={"password"} onChange={_this.changeInput.bind(_this, 'password')} style={{width: '400px'}}></Input>
                            </Form.Item>
                            <Form.Item label="手机号码" labelWidth="120">
                                <Input value={_this.state.form.phone} onChange={_this.changeInput.bind(_this, 'phone')} style={{width: '400px'}}></Input>
                            </Form.Item>
                            <Form.Item label="医生性别" labelWidth="120">
                                <Select value={_this.state.form.sex} onChange={_this.changeInput.bind(_this, 'sex')} placeholder="请选择医生性别">
                                    <Select.Option label="男" value="1"></Select.Option>
                                    <Select.Option label="女" value="2"></Select.Option>
                                    <Select.Option label="未知" value="3"></Select.Option>
                                </Select>
                            </Form.Item>
                            <Form.Item label="所属科室" labelWidth="120">
                                <Select value={_this.state.form.officeId} onChange={_this.changeInput.bind(_this, 'officeId')} placeholder="请选择所属科室">
                                    {
                                        _this.state.officeList.map((item,i) => (
                                            <Select.Option  key={i} label={item.name} value={item.id}></Select.Option>
                                        ))
                                    }
                                </Select>
                            </Form.Item>
                            <Form.Item label="医生职位" labelWidth="120">
                                <Select value={_this.state.form.position} onChange={_this.changeInput.bind(_this, 'position')} placeholder="请选择医生职位">
                                    <Select.Option label="住院医师" value="1"></Select.Option>
                                    <Select.Option label="主治医师" value="2"></Select.Option>
                                    <Select.Option label="主任医师" value="3"></Select.Option>
                                    <Select.Option label="副主任医师" value="4"></Select.Option>
                                </Select>
                            </Form.Item>
                            <Form.Item label="医生简介" labelWidth="120">
                                <Input type="textarea" value={_this.state.form.info} onChange={_this.changeInput.bind(_this, 'info')} autosize={{ minRows: 3}} style={{width: '400px'}} placeholder="请输入医生简介"/>
                            </Form.Item>

                        </Form>
                    </Dialog.Body>

                    <Dialog.Footer className="dialog-footer">
                        <Button onClick={ () => _this.setState({ dialogVisible: false }) }>取 消</Button>
                        <Button type="primary" onClick={_this.saveDoctor.bind(_this)}>确 定</Button>
                    </Dialog.Footer>
                </Dialog>


                <Dialog title="预约挂号" visible={ _this.state.appointDialogVisible } onCancel={ () => _this.setState({ appointDialogVisible: false }) }>
                    <Dialog.Body>
                        <Form model={_this.state.form}>
                            <Form.Item label="医生编号" labelWidth="120">
                                <span style={{width: '400px'}}>{_this.state.form.id}</span>
                            </Form.Item>
                            <Form.Item label="医生名称" labelWidth="120">
                                <span style={{width: '400px'}}>{_this.state.form.name}</span>
                            </Form.Item>
                            <Form.Item label="请选择挂号日期" labelWidth="120">
                                <DatePicker
                                    value={_this.state.appoint.date}
                                    isShowTime={true}
                                    placeholder="请选择挂号日期"
                                    format={"yyyy-MM-dd HH:mm:ss"}
                                    onChange={date=>{
                                        _this.setState({appoint: {date:date}});
                                    }}
                                    disabledDate={time=>time.getTime() < Date.now() - 8.64e7}
                                />
                            </Form.Item>
                            <Form.Item label="挂号备注" labelWidth="120">
                                <Input type="textarea" value={_this.state.appoint.info} onChange={_this.appointInput.bind(_this, 'info')} autosize={{ minRows: 3}} style={{width: '400px'}} placeholder="请输入挂号备注"/>
                            </Form.Item>
                        </Form>
                    </Dialog.Body>
                    <Dialog.Footer className="dialog-footer">
                        <Button onClick={ () => _this.setState({ appointDialogVisible: false }) }>取 消</Button>
                        <Button type="primary" onClick={_this.saveAppoint.bind(_this)}>确 定</Button>
                    </Dialog.Footer>
                </Dialog>

            </div>
        );
    }

    search(){
        const _this = this;
        _this.state.pageData.currentPage = 1;
        _this.getDoctorList();
    }

    // 表单输入内容双向绑定实现
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

    // 预约表单内容双向绑定
    appointInput(type, e){
        // type:对象成员  e:变化值
        const _this = this;
        let target= Object.assign({}, _this.state.appoint, {
            [type]: e
        });
        _this.setState({
            appoint: target
        })
    }

    // 打开预约挂号的窗口
    openAppointDialog(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行挂号！');
            return false;
        }
        let form = {
            ..._this.state.selectItem,
        };
        _this.setState({form: form}, () => {
            _this.setState({ appointDialogVisible: true});
        });
    }

    // 保存预约挂号的数据
    saveAppoint(){
        const _this = this;
        let data = {
            appointTime: _this.getTime( _this.state.appoint.date),
            info: _this.state.appoint.info,
            doctorId: _this.state.selectItem.id,
            userId: _this.state.user.id
        };
        const {serverUrl} = _this.props;
        axios.post(serverUrl+ '/appoint/save', data)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    successOption.message = resp.msg;
                    Message.success(successOption);
                    _this.setState({ appointDialogVisible: false});
                }else{
                    errorOption.message = resp.msg;
                    Message.error(errorOption);
                }
            }).catch(function (error) {
            errorOption.message = "网络错误，保存预约数据失败！";
            Message.error(errorOption);
        })
    }

    // 删除医生操作
    deleteDoctor(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行删除！');
            return false;
        }
        const {serverUrl} = _this.props;
        let form = {
            ..._this.state.selectItem,
            headPic: _this.state.selectItem.headPic,
            photoView: serverUrl + "/photo/view?filename=" + _this.state.selectItem.headPic
        };
        _this.state.form = form;
        axios.post(serverUrl+ '/doctor/delete', _this.state.form)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    Message.success(resp.msg);
                    _this.getDoctorList();
                    _this.setState({selectItem: {}});
                }else{
                    Message.error(resp.msg);
                }
            }).catch(function (error) {
            Message.error('网络错误，删除医生数据失败！');
        })
    }

    getTime(date){
        let str = date;
        let strDate  = new Date(str);
        let year = strDate.getFullYear() + '-';
        let month = (strDate.getMonth()+1 < 10 ? '0'+(strDate.getMonth()+1) : strDate.getMonth()+1) + '-';
        let day = (strDate.getDate() < 10 ? '0'+ strDate.getDate() : strDate.getDate()) + " ";
        let hour = (strDate.getHours() < 10 ? '0'+ strDate.getHours() : strDate.getHours()) + ":";
        let minute = (strDate.getMinutes() < 10 ? '0'+ strDate.getMinutes() : strDate.getMinutes()) + ":"; //获取当前分钟数(0-59)
        let second =  (strDate.getSeconds() < 10 ? '0'+ strDate.getSeconds() : strDate.getSeconds()); //获取当前秒数(0-59)
        let dateAfter = year + month + day + hour + minute + second;
        return dateAfter;
    }

    // 存储选中行
    saveItem(item){
        const _this = this;
        let headPic = item.headPic.props.alt;
        let data = {
            ...item,
            headPic: headPic
        };
        if(data.sex === "男"){
            data.sex = "1";
        }else if(data.sex === "女"){
            data.sex = "2";
        }else if(data.sex === "未知"){
            data.sex = "3";
        }
        if(data.position === "住院医师"){
            data.position = "1";
        }else if(data.position === "主治医师"){
            data.position = "2";
        }else if(data.position === "主任医师"){
            data.position = "3";
        }else if(data.position === "副主任医师"){
            data.position = "4";
        }
        _this.setState({selectItem: data});
    }

    uploadPhoto(){
        $("#photo-file").click();
    }

    upload(){
        const _this = this;
        const {serverUrl} = _this.props;
        if($("#photo-file").val() === '')return;
        let config = {
            headers:{'Content-Type':'multipart/form-data'}
        };
        let formData = new FormData();
        formData.append('photo', document.getElementById('photo-file').files[0]);
        // 普通上传
        axios.post(serverUrl + "/photo/upload_photo", formData, config).then((response)=>{
            let resp = response.data;
            if(resp.code === 0){
                let data = {
                    ..._this.state.form,
                    headPic: resp.data,
                    photoView: serverUrl +"/photo/view?filename=" + resp.data
                };
                _this.setState({
                    form: data
                });
                successOption.message = resp.msg;
                Message.success(successOption);
            }else{
                errorOption.message = resp.msg;
                Message.error(errorOption);
            }
            $("#photo-file").val("");
        });
    }

    // 保存医生数据
    saveDoctor(){
        const _this = this;
        const {serverUrl } = _this.props;
        // 保存操作
        axios.post(serverUrl+ '/doctor/save',_this.state.form)
            .then(function (response) {
                let resp = response.data;
                if(resp.code === 0){
                    successOption.message = resp.msg;
                    Message.success(successOption);
                    _this.getDoctorList();
                    _this.setState({ dialogVisible: false, selectItem: {}});
                }else{
                    errorOption.message = resp.msg;
                    Message.error(errorOption);
                }
            }).catch(function (error) {
            errorOption.message = "网络错误，保存医生数据失败！";
            Message.error(errorOption);
        })
    }

    // 打开编辑医生窗口
    openEdit(){
        const _this = this;
        if(JSON.stringify(_this.state.selectItem) === '{}'){
            Message.warning('请选中一条数据进行编辑！');
            return false;
        }
        const {serverUrl} = _this.props;
        let form = {
            ..._this.state.selectItem,
            headPic: _this.state.selectItem.headPic,
            photoView: serverUrl + "/photo/view?filename=" + _this.state.selectItem.headPic
        };
        _this.setState({form: form}, () => {
            _this.setState({ dialogVisible: true, title: "编辑医生信息"});
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

const uploadButtonStyle = {
    verticalAlign: 'middle',
    float: 'none',
    marginTop: '-50px',
    marginLeft: '20px'
};

const mapStateToProps = state => ({
    menuName: state.menuName,
    serverUrl: state.serverUrl
});

const mapDispatchToProps = dispatch => ({
    changeName: name => dispatch({type:"changeName", name}),
    changeActiveNum: num => dispatch({type:"changeActiveNum", num})
});



export default connect(mapStateToProps, mapDispatchToProps)(DoctorList);
