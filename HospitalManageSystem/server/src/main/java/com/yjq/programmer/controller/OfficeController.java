package com.yjq.programmer.controller;

import com.yjq.programmer.dto.OfficeDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IOfficeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-11 11:18
 */
@RestController
@RequestMapping("/office")
public class OfficeController {

    @Resource
    private IOfficeService officeService;

    /**
     * 分页获取科室数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<OfficeDTO>> getOfficeList(@RequestBody PageDTO<OfficeDTO> pageDTO){
        return officeService.getOfficeList(pageDTO);
    }

    /**
     * 获取所有科室数据
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<OfficeDTO>> getAllOffice(){
        return officeService.getAllOffice();
    }

    /**
     * 保存科室信息操作
     * @param officeDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveOffice(@RequestBody OfficeDTO officeDTO){
        return officeService.saveOffice(officeDTO);
    }

    /**
     * 删除科室操作
     * @param officeDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteOffice(@RequestBody OfficeDTO officeDTO){
        return officeService.deleteOffice(officeDTO);
    }
}
