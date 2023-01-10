package com.yjq.programmer.dao.my;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-14 17:00
 */
public interface MyAppointMapper {

    // 根据时间范围获取交易的订单总数
    Integer getAppointTotalByDate(@Param("queryMap") Map<String, Object> queryMap);
}
