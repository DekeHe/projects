package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.AppointMapper;
import com.yjq.programmer.dao.DoctorMapper;
import com.yjq.programmer.dao.OfficeMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IAppointService;
import com.yjq.programmer.service.IDoctorService;
import com.yjq.programmer.service.IUserService;
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
 * @create 2021-12-11 19:38
 */
@Service
@Transactional
public class DoctorServiceImpl implements IDoctorService {

    @Resource
    private DoctorMapper doctorMapper;

    @Resource
    private OfficeMapper officeMapper;

    @Resource
    private AppointMapper appointMapper;

    @Resource
    private IAppointService appointService;

    @Resource
    private IUserService userService;

    /**
     * 分页获取医生数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<DoctorDTO>> getDoctorList(PageDTO<DoctorDTO> pageDTO, String token) {
        DoctorExample doctorExample = new DoctorExample();
        ResponseDTO<UserDTO> loginUser = userService.getLoginUser(token);
        String roleId = loginUser.getData().getRoleId();
        String id = loginUser.getData().getId();
        if(RoleEnum.ADMIN.getCode().equals(roleId)){
            // 如果是管理员角色，可以看到所有数据
            // 判断是否进行关键字搜索
            if(!CommonUtil.isEmpty(pageDTO.getSearchContent())) {
                doctorExample.createCriteria().andNameLike("%" + pageDTO.getSearchContent() + "%");
            }
        }else if(RoleEnum.DOCTOR.getCode().equals(roleId)){
            // 如果是医生角色，只能看到自己数据
            // 判断是否进行关键字搜索
            if(!CommonUtil.isEmpty(pageDTO.getSearchContent())) {
                doctorExample.createCriteria().andNameLike("%" + pageDTO.getSearchContent() + "%").andIdEqualTo(id);
            }else{
                doctorExample.createCriteria().andIdEqualTo(id);
            }
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        pageDTO.setSize(5);
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出医生数据
        List<Doctor> doctorList = doctorMapper.selectByExample(doctorExample);
        PageInfo<Doctor> pageInfo = new PageInfo<>(doctorList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<DoctorDTO> doctorDTOList = CopyUtil.copyList(doctorList, DoctorDTO.class);
        // 封装每个医生所属科室的数据
        for(DoctorDTO doctorDTO : doctorDTOList){
            Office office = officeMapper.selectByPrimaryKey(doctorDTO.getOfficeId());
            doctorDTO.setOfficeDTO(CopyUtil.copy(office, OfficeDTO.class));
        }
        pageDTO.setList(doctorDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存医生信息操作
     * @param doctorDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveDoctor(DoctorDTO doctorDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(doctorDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Doctor doctor = CopyUtil.copy(doctorDTO, Doctor.class);
        if(CommonUtil.isEmpty(doctor.getId())){
            // id为空  添加操作 设置主键id
            // 判断医生名称是否存在
            if(isNameExist(doctor, "")){
                return ResponseDTO.errorByMsg(CodeMsg.DOCTOR_NAME_EXIST);
            }
            doctor.setId(UuidUtil.getShortUuid());
            if(doctorMapper.insertSelective(doctor) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.DOCTOR_ADD_ERROR);
            }
        }else{
            // id不为空 编辑操作
            // 判断医生名称是否存在
            if(isNameExist(doctor, doctor.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.DOCTOR_NAME_EXIST);
            }
            if(doctorMapper.updateByPrimaryKeySelective(doctor) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.DOCTOR_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存医生信息成功！");
    }

    /**
     * 删除医生操作
     * @param doctorDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteDoctor(DoctorDTO doctorDTO) {
        if(CommonUtil.isEmpty(doctorDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除与该医生有关的其他所有数据
        AppointExample appointExample = new AppointExample();
        appointExample.createCriteria().andDoctorIdEqualTo(doctorDTO.getId());
        List<Appoint> appointList = appointMapper.selectByExample(appointExample);
        for(Appoint appoint : appointList){
            appointService.deleteAppoint(CopyUtil.copy(appoint, AppointDTO.class));
        }
        // 删除医生数据
        if(doctorMapper.deleteByPrimaryKey(doctorDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.DOCTOR_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "成功删除医生信息！");
    }


    /**
     * 判断医生名称是否重复
     * @param doctor
     * @param id
     * @return
     */
    public Boolean isNameExist(Doctor doctor, String id) {
        DoctorExample doctorExample = new DoctorExample();
        doctorExample.createCriteria().andNameEqualTo(doctor.getName());
        List<Doctor> selectedDoctorList = doctorMapper.selectByExample(doctorExample);
        if(selectedDoctorList != null && selectedDoctorList.size() > 0) {
            if(selectedDoctorList.size() > 1){
                return true; //出现重名
            }
            if(!selectedDoctorList.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }
}
