package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-11 19:54
 */
public class DoctorDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="医生名称不能为空！",errorMaxLengthMsg="医生名称长度不能大于8！",errorMinLengthMsg="医生名称不能为空！")
    private String name;

    private String officeId;

    private OfficeDTO officeDTO;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=256,minLength=1,errorRequiredMsg="医生简介不能为空！",errorMaxLengthMsg="医生简介长度不能大于256！",errorMinLengthMsg="医生简介不能为空！")
    private String info;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=6,errorRequiredMsg="医生密码不能为空！",errorMaxLengthMsg="医生密码长度不能大于16！",errorMinLengthMsg="医生密码不能为空！")
    private String password;

    private String headPic;

    private String position;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=11,minLength=11,errorRequiredMsg="医生手机号不能为空！",errorMaxLengthMsg="医生手机号长度必须11位！",errorMinLengthMsg="医生手机号长度必须11位！")
    private String phone;

    private Integer sex;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public OfficeDTO getOfficeDTO() {
        return officeDTO;
    }

    public void setOfficeDTO(OfficeDTO officeDTO) {
        this.officeDTO = officeDTO;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", officeId=").append(officeId);
        sb.append(", info=").append(info);
        sb.append(", password=").append(password);
        sb.append(", headPic=").append(headPic);
        sb.append(", position=").append(position);
        sb.append(", phone=").append(phone);
        sb.append(", sex=").append(sex);
        sb.append(", officeDTO=").append(officeDTO);
        sb.append(", token=").append(token);
        sb.append("]");
        return sb.toString();
    }
}
