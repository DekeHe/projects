package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Appoint;
import com.yjq.programmer.domain.AppointExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppointMapper {
    long countByExample(AppointExample example);

    int deleteByExample(AppointExample example);

    int deleteByPrimaryKey(String id);

    int insert(Appoint record);

    int insertSelective(Appoint record);

    List<Appoint> selectByExample(AppointExample example);

    Appoint selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Appoint record, @Param("example") AppointExample example);

    int updateByExample(@Param("record") Appoint record, @Param("example") AppointExample example);

    int updateByPrimaryKeySelective(Appoint record);

    int updateByPrimaryKey(Appoint record);
}