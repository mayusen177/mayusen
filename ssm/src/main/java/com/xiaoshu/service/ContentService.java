package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.ContentMapper;
import com.xiaoshu.dao.ContentcategoryMapper;
import com.xiaoshu.entity.Content;
import com.xiaoshu.entity.Contentcategory;
import com.xiaoshu.entity.Cx2;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class ContentService {

	@Autowired
	private ContentMapper cm;
	@Autowired
	private ContentcategoryMapper cam;
	
	//分页查询所以
	public PageInfo<Content> findContentPage(Cx2 cx, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Content> conList = cm.querycontent(cx);
		PageInfo<Content> pageInfo = new PageInfo<Content>(conList);
		return pageInfo;
	}
	// 新增
		public void addContent(Content c) throws Exception {
			cm.insert(c);
		};

		// 修改
		public void updateContent(Content c) throws Exception {
			cm.updateByPrimaryKey(c);
		};

		// 删除
		public void deleteContent(Integer id) throws Exception {
			cm.deleteByPrimaryKey(id);
		}
		public List<Contentcategory> findCa() {
			// TODO Auto-generated method stub
			return cam.findCa();
		}
		public List<Content> findcaByName(String contenttitle) {
			// TODO Auto-generated method stub
			return cm.findcoByName(contenttitle);
		};
	
}
