package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-12 11:11
 */
public class AppointDTO {

    private String id;

    private Date time;

    @ValidateEntity(required=true,errorRequiredMsg="预约挂号时间不能为空！")
    private String appointTime;

    @ValidateEntity(required=true,errorRequiredMsg="登录身份已失效，请重新登录！")
    private String userId;

    private UserDTO userDTO;

    @ValidateEntity(required=true,errorRequiredMsg="请选择你要挂号的医生！")
    private String doctorId;

    private DoctorDTO doctorDTO;

    private Integer state;

    @ValidateEntity(requiredMaxLength=true,maxLength=256,errorMaxLengthMsg="预约挂号备注长度不能大于256！")
    private String info;

    @ValidateEntity(requiredMaxLength=true,maxLength=256,errorMaxLengthMsg="医生回复内容长度不能大于256！")
    private String reply;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public DoctorDTO getDoctorDTO() {
        return doctorDTO;
    }

    public void setDoctorDTO(DoctorDTO doctorDTO) {
        this.doctorDTO = doctorDTO;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", time=").append(time);
        sb.append(", userId=").append(userId);
        sb.append(", doctorId=").append(doctorId);
        sb.append(", state=").append(state);
        sb.append(", info=").append(info);
        sb.append(", reply=").append(reply);
        sb.append(", appointTime=").append(appointTime);
        sb.append(", userDTO=").append(userDTO);
        sb.append(", doctorDTO=").append(doctorDTO);
        sb.append("]");
        return sb.toString();
    }
}
