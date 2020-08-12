package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import com.xiaoshu.dao.ContentMapper;
import com.xiaoshu.dao.ContentcategoryMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Content;

import com.xiaoshu.entity.ContentVo;
import com.xiaoshu.entity.Contentcategory;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class ContentService {

	@Autowired
	private ContentMapper cm;
	@Autowired
	private ContentcategoryMapper ccm;

	
	// 新增
	public void addContent(Content t) throws Exception {
		t.setCreatetime(new Date());
		cm.insert(t);
	};

	// 修改
	public void updateContent(Content t) throws Exception {
		cm.update(t);
	};

	// 删除
	public void deleteContent(Integer id) throws Exception {
		cm.deleteByPrimaryKey(id);
	};

	public PageInfo<ContentVo> findContentPage(ContentVo content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ContentVo> userList = cm.findContent(content);
		PageInfo<ContentVo> pageInfo = new PageInfo<ContentVo>(userList);
		return pageInfo;
	}
	public List<Contentcategory> findCategory(){
		return ccm.selectAll();
	}
	public List<ContentVo> findTj(){
		return cm.findTj();
	}
}
