package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.GoodsMapper;
import com.xiaoshu.dao.GoodstypeMapper;
import com.xiaoshu.entity.Cx;
import com.xiaoshu.entity.Goods;
import com.xiaoshu.entity.Goodstype;
import com.xiaoshu.entity.Tj;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class GoodsService {

	@Autowired
	private GoodsMapper gm;
	@Autowired
	private GoodstypeMapper tm;
	
	public PageInfo<Goods> findUserPage( int pageNum, int pageSize,Cx cx) {
		PageHelper.startPage(pageNum, pageSize);
		List<Goods> goodsList =gm.queryGoods(cx); 
		PageInfo<Goods> pageInfo = new PageInfo<Goods>(goodsList);
		return pageInfo;
	}
	// 新增
		public void addGoods(Goods g) throws Exception {
			gm.insert(g);
		};

		// 修改
		public void updateGoods(Goods g) throws Exception {
			gm.updateByPrimaryKey(g);
		};

		// 删除
		public void deleteGoods(Integer id) throws Exception {
			gm.deleteByPrimaryKey(id);
		};
		//查询分类
		public List<Goodstype> findType(){
			return tm.findType();
		}
		public List<Tj> findTj() {
			
			return gm.findTj();
		}
}
