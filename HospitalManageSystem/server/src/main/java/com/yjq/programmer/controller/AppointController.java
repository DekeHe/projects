package com.yjq.programmer.controller;

import com.yjq.programmer.dto.AppointDTO;
import com.yjq.programmer.dto.AppointItemDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IAppointService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-12 11:10
 */
@RestController
@RequestMapping("/appoint")
public class AppointController {

    @Resource
    private IAppointService appointService;

    /**
     * 保存预约挂号信息操作
     * @param appointDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveAppoint(@RequestBody AppointDTO appointDTO){
        return appointService.saveAppoint(appointDTO);
    }

    /**
     * 分页获取预约挂号数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<AppointDTO>> getAppointList(@RequestBody PageDTO<AppointDTO> pageDTO, @RequestHeader("token") String token){
        return appointService.getAppointList(pageDTO, token);
    }

    /**
     * 取消预约挂号操作
     * @param appointDTO
     * @return
     */
    @PostMapping("/cancel")
    public ResponseDTO<Boolean> cancelAppoint(@RequestBody AppointDTO appointDTO){
        return appointService.cancelAppoint(appointDTO);
    }

    /**
     * 编辑预约挂号操作
     * @param appointDTO
     * @return
     */
    @PostMapping("/edit")
    public ResponseDTO<Boolean> editAppoint(@RequestBody AppointDTO appointDTO){
        return appointService.editAppoint(appointDTO);
    }

    /**
     * 删除预约挂号操作
     * @param appointDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteAppoint(@RequestBody AppointDTO appointDTO){
        return appointService.deleteAppoint(appointDTO);
    }

    /**
     * 保存挂号详细记录操作
     * @param appointItemDTO
     * @return
     */
    @PostMapping("/save_item")
    public ResponseDTO<Boolean> saveAppointItem(@RequestBody AppointItemDTO appointItemDTO){
        return appointService.saveAppointItem(appointItemDTO);
    }

    /**
     * 根据挂号id获取挂号详细记录
     * @param appointDTO
     * @return
     */
    @PostMapping("/list_item")
    public ResponseDTO<List<AppointItemDTO>> getAppointItem(@RequestBody AppointDTO appointDTO){
        return appointService.getAppointItem(appointDTO);
    }

    /**
     * 根据时间范围获取预约挂号的总数
     * @return
     */
    @PostMapping("/total_date")
    public ResponseDTO<List<Integer>> getAppointCountByDate(){
        return appointService.getAppointCountByDate();
    }
}
