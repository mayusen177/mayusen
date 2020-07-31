package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Cx;
import com.xiaoshu.entity.Studay;
import com.xiaoshu.entity.StudayExample;
import com.xiaoshu.entity.Tj;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StudayMapper extends BaseMapper<Studay> {
    long countByExample(StudayExample example);

    int deleteByExample(StudayExample example);

    List<Studay> selectByExample(StudayExample example);

    int updateByExampleSelective(@Param("record") Studay record, @Param("example") StudayExample example);

    int updateByExample(@Param("record") Studay record, @Param("example") StudayExample example);
    List<Studay> queryStu(Cx cx);

	List<Studay> findByName(String sdname);
	
	List<Tj> findTj();
}