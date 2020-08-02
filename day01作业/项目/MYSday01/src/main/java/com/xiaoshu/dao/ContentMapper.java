package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Content;
import com.xiaoshu.entity.ContentExample;
import com.xiaoshu.entity.Cx2;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentMapper extends BaseMapper<Content> {
    List<Content> querycontent(Cx2 cx);

	List<Content> findcoByName(String contenttitle);
}