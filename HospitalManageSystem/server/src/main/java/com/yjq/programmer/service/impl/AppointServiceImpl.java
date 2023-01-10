package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.AppointItemMapper;
import com.yjq.programmer.dao.AppointMapper;
import com.yjq.programmer.dao.DoctorMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.dao.my.MyAppointMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.AppointStateEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IAppointService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-12 11:13
 */
@Service
@Transactional
public class AppointServiceImpl implements IAppointService {

    @Resource
    private AppointMapper appointMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private DoctorMapper doctorMapper;

    @Resource
    private AppointItemMapper appointItemMapper;

    @Resource
    private MyAppointMapper myAppointMapper;

    @Resource
    private IUserService userService;


    /**
     * 保存预约挂号信息操作
     * @param appointDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveAppoint(AppointDTO appointDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(appointDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Date time = CommonUtil.getFormatterDate(appointDTO.getAppointTime(), "yyyy-MM-dd HH:mm:ss");
        if(time == null){
            return ResponseDTO.errorByMsg(CodeMsg.APPOINT_TIME_EMPTY);
        }
        appointDTO.setTime(time);
        Appoint appoint = CopyUtil.copy(appointDTO, Appoint.class);
        appoint.setId(UuidUtil.getShortUuid());
        appoint.setState(AppointStateEnum.WAIT.getCode());
        if(appointMapper.insertSelective(appoint) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.APPOINT_SAVE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "成功预约挂号！");
    }

    /**
     * 分页获取预约挂号数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<AppointDTO>> getAppointList(PageDTO<AppointDTO> pageDTO, String token) {
        AppointExample appointExample = new AppointExample();
        ResponseDTO<UserDTO> loginUser = userService.getLoginUser(token);
        String roleId = loginUser.getData().getRoleId();
        String id = loginUser.getData().getId();
        if(RoleEnum.USER.getCode().equals(roleId)){
            // 如果是普通用户  只能看到自己的预约信息
            // 判断是否进行关键字搜索
            if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
                appointExample.createCriteria().andIdLike("%"+pageDTO.getSearchContent()+"%").andUserIdEqualTo(id);
            }else{
                appointExample.createCriteria().andUserIdEqualTo(id);
            }
        }else if(RoleEnum.DOCTOR.getCode().equals(roleId)){
            // 如果是医生  只能看到自己的预约信息
            // 判断是否进行关键字搜索
            if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
                appointExample.createCriteria().andIdLike("%"+pageDTO.getSearchContent()+"%").andDoctorIdEqualTo(id);
            }else{
                appointExample.createCriteria().andDoctorIdEqualTo(id);
            }
        }else if(RoleEnum.ADMIN.getCode().equals(roleId)){
            // 如果是管理员  能看到所有信息
            if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
                appointExample.createCriteria().andIdLike("%"+pageDTO.getSearchContent()+"%");
            }
        }

        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        pageDTO.setSize(5);
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出科室数据
        List<Appoint> appointList = appointMapper.selectByExample(appointExample);
        PageInfo<Appoint> pageInfo = new PageInfo<>(appointList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<AppointDTO> appointDTOList = CopyUtil.copyList(appointList, AppointDTO.class);
        // 封装UserDTO和DoctorDTO数据
        for(AppointDTO appointDTO: appointDTOList){
            User user = userMapper.selectByPrimaryKey(appointDTO.getUserId());
            appointDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            Doctor doctor = doctorMapper.selectByPrimaryKey(appointDTO.getDoctorId());
            appointDTO.setDoctorDTO(CopyUtil.copy(doctor, DoctorDTO.class));
        }
        pageDTO.setList(appointDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 取消预约挂号操作
     * @param appointDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> cancelAppoint(AppointDTO appointDTO) {
        if(CommonUtil.isEmpty(appointDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Appoint appoint = appointMapper.selectByPrimaryKey(appointDTO.getId());
        // 判断是否已经取消
        if(appoint.getState().equals(AppointStateEnum.CANCEL.getCode())){
            return ResponseDTO.errorByMsg(CodeMsg.APPOINT_ALREADY_CANCEL);
        }
        // 把预约挂号状态改为已取消
        appoint.setState(AppointStateEnum.CANCEL.getCode());
        if(appointMapper.updateByPrimaryKeySelective(appoint) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.APPOINT_SAVE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "成功取消预约挂号！");
    }

    /**
     * 删除预约挂号操作
     * @param appointDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteAppoint(AppointDTO appointDTO) {
        if(CommonUtil.isEmpty(appointDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除挂号信息
        if(appointMapper.deleteByPrimaryKey(appointDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.APPOINT_DELETE_ERROR);
        }
        // 删除挂号详细记录
        AppointItemExample appointItemExample = new AppointItemExample();
        appointItemExample.createCriteria().andAppointIdEqualTo(appointDTO.getId());
        appointItemMapper.deleteByExample(appointItemExample);
        return ResponseDTO.successByMsg(true, "成功删除预约挂号！");
    }

    /**
     * 编辑预约挂号操作
     * @param appointDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> editAppoint(AppointDTO appointDTO) {
        if(CommonUtil.isEmpty(appointDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Appoint appoint = appointMapper.selectByPrimaryKey(appointDTO.getId());
        appoint.setState(appointDTO.getState());
        appoint.setReply(appointDTO.getReply());
        appointDTO = CopyUtil.copy(appoint, AppointDTO.class);
        appointDTO.setAppointTime(CommonUtil.getFormatterDate(appoint.getTime(), "yyyy-MM-dd HH:mm:ss"));
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(appointDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        if(appointMapper.updateByPrimaryKeySelective(appoint) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.APPOINT_SAVE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "成功编辑预约挂号信息！");
    }

    /**
     * 保存挂号详细记录操作
     * @param appointItemDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveAppointItem(AppointItemDTO appointItemDTO) {
        if(CommonUtil.isEmpty(appointItemDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(appointItemDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        AppointItem appointItem = new AppointItem();
        appointItem.setAppointId(appointItemDTO.getId());
        appointItem.setId(UuidUtil.getShortUuid());
        appointItem.setContent(appointItemDTO.getContent());
        appointItem.setCreateTime(new Date());
        // 添加记录到数据库中
        if(appointItemMapper.insertSelective(appointItem) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.APPOINT_ITEM_SAVE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "成功保存挂号详细记录！");
    }

    /**
     * 根据挂号id获取挂号详细记录
     * @param appointDTO
     * @return
     */
    @Override
    public ResponseDTO<List<AppointItemDTO>> getAppointItem(AppointDTO appointDTO) {
        if(CommonUtil.isEmpty(appointDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        AppointItemExample appointItemExample = new AppointItemExample();
        appointItemExample.createCriteria().andAppointIdEqualTo(appointDTO.getId());
        appointItemExample.setOrderByClause("create_time asc");
        List<AppointItem> appointItemList = appointItemMapper.selectByExample(appointItemExample);
        List<AppointItemDTO> appointItemDTOList = CopyUtil.copyList(appointItemList, AppointItemDTO.class);
        return ResponseDTO.success(appointItemDTOList);
    }

    /**
     * 根据时间范围获取预约挂号的总数
     * @return
     */
    @Override
    public ResponseDTO<List<Integer>> getAppointCountByDate() {
        List<Integer> totalList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        // 获取前天已完成的收益次数
        queryMap.put("start", 2);
        queryMap.put("end", 1);
        totalList.add(myAppointMapper.getAppointTotalByDate(queryMap));
        // 获取昨天已完成的收益次数
        queryMap.put("start", 1);
        queryMap.put("end", 0);
        totalList.add(myAppointMapper.getAppointTotalByDate(queryMap));
        // 获取当天已完成的收益次数
        queryMap.put("start", 0);
        queryMap.put("end", -1);
        totalList.add(myAppointMapper.getAppointTotalByDate(queryMap));
        return ResponseDTO.success(totalList);
    }
}
