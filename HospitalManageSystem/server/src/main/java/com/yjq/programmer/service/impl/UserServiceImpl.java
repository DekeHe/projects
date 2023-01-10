package com.yjq.programmer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.AnnounceMapper;
import com.yjq.programmer.dao.AppointMapper;
import com.yjq.programmer.dao.DoctorMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IAppointService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-09 15:34
 */

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DoctorMapper doctorMapper;

    @Resource
    private AnnounceMapper announceMapper;

    @Resource
    private AppointMapper appointMapper;

    @Resource
    private IAppointService appointService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    /**
     * 分页获取用户数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<UserDTO>> getUserList(PageDTO<UserDTO> pageDTO, String token) {
        UserExample userExample = new UserExample();
        // 判断用户角色是不是普通用户，若是，只能看到自己信息
        ResponseDTO<UserDTO> loginUser = getLoginUser(token);
        String roleId = loginUser.getData().getRoleId();
        String id = loginUser.getData().getId();
        if(RoleEnum.USER.getCode().equals(roleId)){
            // 如果是普通用户
            if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
                userExample.createCriteria().andUsernameLike("%"+pageDTO.getSearchContent()+"%").andIdEqualTo(id);
            }else{
                userExample.createCriteria().andIdEqualTo(id);
            }
        }else if(RoleEnum.ADMIN.getCode().equals(roleId)){
            // 如果是管理员
            UserExample.Criteria c1 = userExample.createCriteria();
            UserExample.Criteria c2 = userExample.createCriteria();
            // 判断是否进行关键字搜索 展示普通用户和管理员的数据
            if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
                c1.andUsernameLike("%"+pageDTO.getSearchContent()+"%").andRoleIdEqualTo(RoleEnum.USER.getCode());
                c2.andUsernameLike("%"+pageDTO.getSearchContent()+"%").andRoleIdEqualTo(RoleEnum.ADMIN.getCode());
            }else{
                c1.andRoleIdEqualTo(RoleEnum.USER.getCode());
                c2.andRoleIdEqualTo(RoleEnum.ADMIN.getCode());
            }
            userExample.or(c2);
        }

        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        pageDTO.setSize(5);
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出用户数据
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<UserDTO> userDTOList = CopyUtil.copyList(userList, UserDTO.class);
        pageDTO.setList(userDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存用户信息操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveUser(UserDTO userDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        User user = CopyUtil.copy(userDTO, User.class);
        if(CommonUtil.isEmpty(user.getId())){
            // id为空  添加操作 设置主键id
            // 判断用户名称是否存在
            if(isUsernameExist(user, "")){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
            }
            user.setId(UuidUtil.getShortUuid());
            if(userMapper.insertSelective(user) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.USER_ADD_ERROR);
            }
        }else{
            // id不为空 编辑操作
            // 判断用户名称是否存在
            if(isUsernameExist(user, user.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
            }
            if(userMapper.updateByPrimaryKeySelective(user) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.USER_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存用户信息成功！");
    }

    /**
     * 删除用户操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteUser(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除其他与该用户有关的数据
        AnnounceExample announceExample = new AnnounceExample();
        announceExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        announceMapper.deleteByExample(announceExample);

        AppointExample appointExample = new AppointExample();
        appointExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        List<Appoint> appointList = appointMapper.selectByExample(appointExample);
        for(Appoint appoint : appointList){
            appointService.deleteAppoint(CopyUtil.copy(appoint, AppointDTO.class));
        }

        // 删除用户数据
        if(userMapper.deleteByPrimaryKey(userDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.USER_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "成功删除用户信息！");
    }

    /**
     * 用户注册操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> registerUser(UserDTO userDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        // 判断确认密码是否为空
        if(CommonUtil.isEmpty(userDTO.getRePassword())){
            return ResponseDTO.errorByMsg(CodeMsg.REPASSWORD_EMPTY);
        }
        // 判断确认密码是否一致
        if(!userDTO.getPassword().equals(userDTO.getRePassword())){
            return ResponseDTO.errorByMsg(CodeMsg.REPASSWORD_ERROR);
        }
        User user = CopyUtil.copy(userDTO, User.class);
        // 判断用户名称是否存在
        if(isUsernameExist(user, "")){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
        }
        user.setId(UuidUtil.getShortUuid());
        user.setRoleId(RoleEnum.USER.getCode());
        // 保存用户数据
        if(userMapper.insertSelective(user) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.USER_REGISTER_ERROR);
        }
        return ResponseDTO.successByMsg(true, "用户注册成功");
    }

    /**
     * 检查用户是否登录
     * @param userDTO
     * @return
     */
    public ResponseDTO<UserDTO> checkLogin(UserDTO userDTO){
        if(userDTO == null || CommonUtil.isEmpty(userDTO.getToken())){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        ResponseDTO<UserDTO> responseDTO = getLoginUser(userDTO.getToken());
        if(responseDTO.getCode() != 0){
            return responseDTO;
        }
        logger.info("获取到的登录信息={}", responseDTO.getData());
        return ResponseDTO.success(responseDTO.getData());
    }

    /**
     * 退出登录操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> logout(UserDTO userDTO) {
        if(!CommonUtil.isEmpty(userDTO.getToken())){
            // token不为空  清除redis中数据
            stringRedisTemplate.delete("USER_" + userDTO.getToken());
        }
        return ResponseDTO.successByMsg(true, "退出登录成功！");
    }

    /**
     * 获取当前登录用户
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> getLoginUser(String token){
        String value = stringRedisTemplate.opsForValue().get("USER_" + token);
        if(CommonUtil.isEmpty(value)){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        UserDTO selectedUserDTO = JSON.parseObject(value, UserDTO.class);
        return ResponseDTO.success(selectedUserDTO);
    }

    /**
     * 用户登录操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> loginUser(UserDTO userDTO) {
        // 进行是否为空判断
        if(CommonUtil.isEmpty(userDTO.getUsername())){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getPassword())){
            return ResponseDTO.errorByMsg(CodeMsg.PASSWORD_EMPTY);
        }
        UserDTO selectedUserDTO = new UserDTO();
        String token = "";
        // 对比名称和密码是否正确
        if(RoleEnum.ADMIN.getCode().equals(userDTO.getRoleId()) || RoleEnum.USER.getCode().equals(userDTO.getRoleId())){
            // 如果是普通用户或管理员登录
            UserExample userExample = new UserExample();
            userExample.createCriteria().andUsernameEqualTo(userDTO.getUsername()).andPasswordEqualTo(userDTO.getPassword()).andRoleIdEqualTo(userDTO.getRoleId());
            List<User> userList = userMapper.selectByExample(userExample);
            if(userList == null || userList.size() != 1){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_PASSWORD_ERROR);
            }
            // 生成登录token并存入Redis中
            selectedUserDTO = CopyUtil.copy(userList.get(0), UserDTO.class);
            token = UuidUtil.getShortUuid();
            selectedUserDTO.setToken(token);

        }else if(RoleEnum.DOCTOR.getCode().equals(userDTO.getRoleId())){
            // 如果是医生登录
            DoctorExample doctorExample = new DoctorExample();
            doctorExample.createCriteria().andNameEqualTo(userDTO.getUsername()).andPasswordEqualTo(userDTO.getPassword());
            List<Doctor> doctorList = doctorMapper.selectByExample(doctorExample);
            if(doctorList == null || doctorList.size() != 1){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_PASSWORD_ERROR);
            }
            // 生成登录token并存入Redis中
            DoctorDTO selectedDoctorDTO = CopyUtil.copy(doctorList.get(0), DoctorDTO.class);
            token = UuidUtil.getShortUuid();
            selectedUserDTO.setRoleId(RoleEnum.DOCTOR.getCode());
            selectedUserDTO.setToken(token);
            selectedUserDTO.setId(selectedDoctorDTO.getId());
            selectedUserDTO.setUsername(selectedDoctorDTO.getName());
            selectedUserDTO.setPassword(selectedDoctorDTO.getPassword());
            selectedUserDTO.setHeadPic(selectedDoctorDTO.getHeadPic());
            selectedUserDTO.setPhone(selectedDoctorDTO.getPhone());
        }
        //把token存入redis中 有效期1小时
        stringRedisTemplate.opsForValue().set("USER_" + token, JSON.toJSONString(selectedUserDTO), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(selectedUserDTO, "登录成功！");

    }

    /**
     * 判断用户名称是否重复
     * @param user
     * @param id
     * @return
     */
    public Boolean isUsernameExist(User user, String id) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername());
        List<User> selectedUserList = userMapper.selectByExample(userExample);
        if(selectedUserList != null && selectedUserList.size() > 0) {
            if(selectedUserList.size() > 1){
                return true; //出现重名
            }
            if(!selectedUserList.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }
}
