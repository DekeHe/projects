package com.yjq.programmer.dao;

import com.yjq.programmer.domain.AppointItem;
import com.yjq.programmer.domain.AppointItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppointItemMapper {
    long countByExample(AppointItemExample example);

    int deleteByExample(AppointItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(AppointItem record);

    int insertSelective(AppointItem record);

    List<AppointItem> selectByExample(AppointItemExample example);

    AppointItem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AppointItem record, @Param("example") AppointItemExample example);

    int updateByExample(@Param("record") AppointItem record, @Param("example") AppointItemExample example);

    int updateByPrimaryKeySelective(AppointItem record);

    int updateByPrimaryKey(AppointItem record);
}