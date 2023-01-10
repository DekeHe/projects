package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-11 18:07
 */
public enum PositionEnum {

    HOUSE_DOCTOR("1","住院医师"),

    MAJOR_DOCTOR("2","主治医师"),

    DIRECT_DOCTOR("3","主任医师"),

    SECOND_DIRECT_DOCTOR("4","副主任医师")

    ;

    String code;

    String desc;

    PositionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
