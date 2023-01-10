package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-12 16:31
 */
public enum AppointStateEnum {

    WAIT(1,"待就诊"),

    VISITED(2,"已就诊"),

    CANCEL(3,"已取消"),

    ;

    Integer code;

    String desc;

    AppointStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
