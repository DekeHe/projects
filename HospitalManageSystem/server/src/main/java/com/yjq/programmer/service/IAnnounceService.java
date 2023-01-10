package com.yjq.programmer.service;

import com.yjq.programmer.dto.AnnounceDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-13 20:23
 */
public interface IAnnounceService {

    // 分页获取公告数据
    ResponseDTO<PageDTO<AnnounceDTO>> getAnnounceList(PageDTO<AnnounceDTO> pageDTO);

    // 保存公告信息操作
    ResponseDTO<Boolean> saveAnnounce(AnnounceDTO announceDTO);

    // 删除公告信息操作
    ResponseDTO<Boolean> deleteAnnounce(AnnounceDTO announceDTO);

    // 获取首页前三条公告
    ResponseDTO<List<AnnounceDTO>> getHomeAnnounce();
}
