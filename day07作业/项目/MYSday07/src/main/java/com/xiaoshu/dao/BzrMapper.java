package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Bzr;
import com.xiaoshu.entity.BzrExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BzrMapper extends BaseMapper<Bzr> {
    long countByExample(BzrExample example);

    int deleteByExample(BzrExample example);

    List<Bzr> selectByExample(BzrExample example);

    int updateByExampleSelective(@Param("record") Bzr record, @Param("example") BzrExample example);

    int updateByExample(@Param("record") Bzr record, @Param("example") BzrExample example);
}