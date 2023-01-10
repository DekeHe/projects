package com.yjq.programmer.service;

import com.yjq.programmer.dto.AppointDTO;
import com.yjq.programmer.dto.AppointItemDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-12 11:12
 */
public interface IAppointService {

    // 保存预约挂号信息操作
    ResponseDTO<Boolean> saveAppoint(AppointDTO appointDTO);

    // 分页获取预约挂号数据
    ResponseDTO<PageDTO<AppointDTO>> getAppointList(PageDTO<AppointDTO> pageDTO, String token);

    // 取消预约挂号操作
    ResponseDTO<Boolean> cancelAppoint(AppointDTO appointDTO);

    // 删除预约挂号操作
    ResponseDTO<Boolean> deleteAppoint(AppointDTO appointDTO);

    // 编辑预约挂号操作
    ResponseDTO<Boolean> editAppoint(AppointDTO appointDTO);

    // 保存挂号详细记录操作
    ResponseDTO<Boolean> saveAppointItem(AppointItemDTO appointItemDTO);

    // 根据挂号id获取挂号详细记录
    ResponseDTO<List<AppointItemDTO>> getAppointItem(AppointDTO appointDTO);

    // 根据时间范围获取预约挂号的总数
    ResponseDTO<List<Integer>> getAppointCountByDate();
}
