package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.AnnounceMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.Announce;
import com.yjq.programmer.domain.AnnounceExample;
import com.yjq.programmer.domain.User;
import com.yjq.programmer.dto.AnnounceDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IAnnounceService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-13 20:24
 */
@Transactional
@Service
public class AnnounceServiceImpl implements IAnnounceService {

    @Resource
    private AnnounceMapper announceMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 分页获取公告数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<AnnounceDTO>> getAnnounceList(PageDTO<AnnounceDTO> pageDTO) {
        AnnounceExample announceExample = new AnnounceExample();
        // 判断是否进行关键字搜索
        if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
            announceExample.createCriteria().andContentLike("%"+pageDTO.getSearchContent()+"%");
        }
        announceExample.setOrderByClause("create_time desc");
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        pageDTO.setSize(5);
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出公告数据
        List<Announce> announceList = announceMapper.selectByExample(announceExample);
        PageInfo<Announce> pageInfo = new PageInfo<>(announceList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<AnnounceDTO> announceDTOList = CopyUtil.copyList(announceList, AnnounceDTO.class);
        // 封装公告所属用户数据
        for(AnnounceDTO announceDTO : announceDTOList){
            User user = userMapper.selectByPrimaryKey(announceDTO.getUserId());
            announceDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
        }
        pageDTO.setList(announceDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存公告信息操作
     * @param announceDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveAnnounce(AnnounceDTO announceDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(announceDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Announce announce = CopyUtil.copy(announceDTO, Announce.class);
        if(CommonUtil.isEmpty(announce.getId())){
            // id为空  添加操作 设置主键id
            announce.setId(UuidUtil.getShortUuid());
            announce.setCreateTime(new Date());
            if(announceMapper.insertSelective(announce) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.ANNOUNCE_ADD_ERROR);
            }
        }else{
            // id不为空 编辑操作
            if(announceMapper.updateByPrimaryKeySelective(announce) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.ANNOUNCE_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存公告信息成功！");
    }

    /**
     * 删除公告信息操作
     * @param announceDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteAnnounce(AnnounceDTO announceDTO) {
        if(CommonUtil.isEmpty(announceDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除公告信息
        if(announceMapper.deleteByPrimaryKey(announceDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.ANNOUNCE_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "成功删除公告信息！");
    }

    /**
     * 获取首页前三条公告
     * @return
     */
    @Override
    public ResponseDTO<List<AnnounceDTO>> getHomeAnnounce() {
        // 设置分页
        PageDTO<AnnounceDTO> pageDTO = new PageDTO<>();
        pageDTO.setPage(1);
        pageDTO.setSize(3);
        AnnounceExample announceExample = new AnnounceExample();
        announceExample.setOrderByClause("create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 查出公告数据
        List<Announce> announceList = announceMapper.selectByExample(announceExample);
        // 讲domain类型数据  转成 DTO类型数据
        List<AnnounceDTO> announceDTOList = CopyUtil.copyList(announceList, AnnounceDTO.class);
        // 封装公告所属用户数据
        for(AnnounceDTO announceDTO : announceDTOList){
            User user = userMapper.selectByPrimaryKey(announceDTO.getUserId());
            announceDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
        }
        return ResponseDTO.success(announceDTOList);
    }


}
