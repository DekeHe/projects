package com.yjq.programmer.controller;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-09 15:30
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 分页获取用户数据
     * @param pageDTO
     * @param token
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<UserDTO>> getUserList(@RequestBody PageDTO<UserDTO> pageDTO, @RequestHeader("token") String token){
        return userService.getUserList(pageDTO, token);
    }

    /**
     * 用户注册操作
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public ResponseDTO<Boolean> registerUser(@RequestBody UserDTO userDTO){
        return userService.registerUser(userDTO);
    }

    /**
     * 用户登录操作
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseDTO<UserDTO> loginUser(@RequestBody UserDTO userDTO){
        return userService.loginUser(userDTO);
    }

    /**
     * 保存用户信息操作
     * @param userDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveUser(@RequestBody UserDTO userDTO){
        return userService.saveUser(userDTO);
    }

    /**
     * 检查用户是否登录
     * @param userDTO
     * @return
     */
    @PostMapping("/check_login")
    public ResponseDTO<UserDTO> checkLogin(@RequestBody UserDTO userDTO){
        return userService.checkLogin(userDTO);
    }

    /**
     * 删除用户操作
     * @param userDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteUser(@RequestBody UserDTO userDTO){
        return userService.deleteUser(userDTO);
    }

    /**
     * 退出登录操作
     * @param userDTO
     * @return
     */
    @PostMapping("/logout")
    public ResponseDTO<Boolean> logout(@RequestBody UserDTO userDTO){
        return userService.logout(userDTO);
    }
}
