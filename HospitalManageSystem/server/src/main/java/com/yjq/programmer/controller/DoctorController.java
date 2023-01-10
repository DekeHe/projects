package com.yjq.programmer.controller;

import com.yjq.programmer.dto.DoctorDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IDoctorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-11 18:53
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Resource
    private IDoctorService doctorService;

    /**
     * 分页获取医生数据
     * @param pageDTO
     * @param token
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<DoctorDTO>> getDoctorList(@RequestBody PageDTO<DoctorDTO> pageDTO, @RequestHeader("token") String token){
        return doctorService.getDoctorList(pageDTO, token);
    }

    /**
     * 保存医生信息操作
     * @param doctorDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveDoctor(@RequestBody DoctorDTO doctorDTO){
        return doctorService.saveDoctor(doctorDTO);
    }

    /**
     * 删除医生操作
     * @param doctorDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteDoctor(@RequestBody DoctorDTO doctorDTO){
        return doctorService.deleteDoctor(doctorDTO);
    }

}
