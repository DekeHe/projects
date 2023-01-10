package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-09 15:31
 */
public interface IUserService {

    // 分页获取用户数据
    ResponseDTO<PageDTO<UserDTO>> getUserList(PageDTO<UserDTO> pageDTO, String token);

    // 保存用户信息操作
    ResponseDTO<Boolean> saveUser(UserDTO userDTO);

    // 删除用户操作
    ResponseDTO<Boolean> deleteUser(UserDTO userDTO);

    // 用户注册操作
    ResponseDTO<Boolean> registerUser(UserDTO userDTO);

    // 用户登录操作
    ResponseDTO<UserDTO> loginUser(UserDTO userDTO);

    // 检查用户是否登录
    ResponseDTO<UserDTO> checkLogin(UserDTO userDTO);

    // 退出登录操作
    ResponseDTO<Boolean> logout(UserDTO userDTO);

    // 获取当前登录用户
    ResponseDTO<UserDTO> getLoginUser(String token);

}
