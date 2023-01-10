package com.yjq.programmer.service;

import com.yjq.programmer.dto.OfficeDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-11 11:24
 */
public interface IOfficeService {

    // 分页获取科室数据
    ResponseDTO<PageDTO<OfficeDTO>> getOfficeList(PageDTO<OfficeDTO> pageDTO);

    // 获取所有科室数据
    ResponseDTO<List<OfficeDTO>> getAllOffice();

    // 保存科室信息操作
    ResponseDTO<Boolean> saveOffice(OfficeDTO officeDTO);

    // 删除科室操作
    ResponseDTO<Boolean> deleteOffice(OfficeDTO officeDTO);
}
