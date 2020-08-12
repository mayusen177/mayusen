package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Contentcategory;
import com.xiaoshu.entity.ContentcategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentcategoryMapper extends BaseMapper<Contentcategory> {
   

    int updateByExample(@Param("record") Contentcategory record, @Param("example") ContentcategoryExample example);
}