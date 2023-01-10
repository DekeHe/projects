package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-11 11:38
 */
public class OfficeDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="科室名称不能为空！",errorMaxLengthMsg="科室名称长度不能大于32！",errorMinLengthMsg="科室名称不能为空！")
    private String name;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=256,minLength=1,errorRequiredMsg="科室简介不能为空！",errorMaxLengthMsg="科室简介长度不能大于256！",errorMinLengthMsg="科室简介不能为空！")
    private String info;

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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", info=").append(info);
        sb.append("]");
        return sb.toString();
    }
}
