package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Majorday;
import com.xiaoshu.entity.MajordayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MajordayMapper extends BaseMapper<Majorday> {
    long countByExample(MajordayExample example);

    int deleteByExample(MajordayExample example);

    List<Majorday> selectByExample(MajordayExample example);

    int updateByExampleSelective(@Param("record") Majorday record, @Param("example") MajordayExample example);

    int updateByExample(@Param("record") Majorday record, @Param("example") MajordayExample example);

	List<Majorday> findMajor();

	Majorday findmajByname(String mdname);
}