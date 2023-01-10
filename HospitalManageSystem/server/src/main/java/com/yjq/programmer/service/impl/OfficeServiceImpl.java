package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.OfficeMapper;
import com.yjq.programmer.domain.Office;
import com.yjq.programmer.domain.OfficeExample;
import com.yjq.programmer.dto.OfficeDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IOfficeService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-11 11:25
 */
@Service
@Transactional
public class OfficeServiceImpl implements IOfficeService {

    @Resource
    private OfficeMapper officeMapper;

    /**
     * 分页获取科室数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<OfficeDTO>> getOfficeList(PageDTO<OfficeDTO> pageDTO) {
        OfficeExample officeExample = new OfficeExample();
        // 判断是否进行关键字搜索
        if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
            officeExample.createCriteria().andNameLike("%"+pageDTO.getSearchContent()+"%");
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        pageDTO.setSize(5);
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出科室数据
        List<Office> officeList = officeMapper.selectByExample(officeExample);
        PageInfo<Office> pageInfo = new PageInfo<>(officeList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<OfficeDTO> officeDTOList = CopyUtil.copyList(officeList, OfficeDTO.class);
        pageDTO.setList(officeDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 获取所有科室数据
     * @return
     */
    @Override
    public ResponseDTO<List<OfficeDTO>> getAllOffice() {
        List<Office> officeList = officeMapper.selectByExample(new OfficeExample());
        List<OfficeDTO> officeDTOList = CopyUtil.copyList(officeList, OfficeDTO.class);
        return ResponseDTO.success(officeDTOList);
    }

    /**
     * 保存科室信息操作
     * @param officeDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveOffice(OfficeDTO officeDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(officeDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Office office = CopyUtil.copy(officeDTO, Office.class);
        if(CommonUtil.isEmpty(office.getId())){
            // id为空  添加操作 设置主键id
            // 判断科室名称是否存在
            if(isNameExist(office, "")){
                return ResponseDTO.errorByMsg(CodeMsg.OFFICE_NAME_EXIST);
            }
            office.setId(UuidUtil.getShortUuid());
            if(officeMapper.insertSelective(office) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.OFFICE_ADD_ERROR);
            }
        }else{
            // id不为空 编辑操作
            // 判断科室名称是否存在
            if(isNameExist(office, office.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.OFFICE_NAME_EXIST);
            }
            if(officeMapper.updateByPrimaryKeySelective(office) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.OFFICE_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存科室信息成功！");
    }

    /**
     * 删除科室操作
     * @param officeDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteOffice(OfficeDTO officeDTO) {
        if(CommonUtil.isEmpty(officeDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除科室数据
        if(officeMapper.deleteByPrimaryKey(officeDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.OFFICE_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "成功删除科室信息！");
    }

    /**
     * 判断科室名称是否重复
     * @param office
     * @param id
     * @return
     */
    public Boolean isNameExist(Office office, String id) {
        OfficeExample officeExample = new OfficeExample();
        officeExample.createCriteria().andNameEqualTo(office.getName());
        List<Office> selectedOfficeList = officeMapper.selectByExample(officeExample);
        if(selectedOfficeList != null && selectedOfficeList.size() > 0) {
            if(selectedOfficeList.size() > 1){
                return true; //出现重名
            }
            if(!selectedOfficeList.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }


}
