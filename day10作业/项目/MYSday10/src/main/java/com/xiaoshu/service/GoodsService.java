package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.GoodsMapper;
import com.xiaoshu.dao.GoodstypeMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Goods;
import com.xiaoshu.entity.GoodsVo;
import com.xiaoshu.entity.Goodstype;
import com.xiaoshu.entity.Tj;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class GoodsService {

	@Autowired
	private GoodsMapper gm;
	@Autowired
	private GoodstypeMapper tm;
	@Autowired
	private JedisPool jp;
	
	// 新增
	public void addGoods(Goods t) throws Exception {
		t.setCreatetime(new Date());
		gm.insert(t);
	};

	// 修改
	public void updateGoods(Goods t) throws Exception {
		gm.update(t);
	};

	// 删除
	public void deleteGoods(Integer id) throws Exception {
		gm.deleteByPrimaryKey(id);
	};

	public PageInfo<GoodsVo> findGoodsPage(GoodsVo goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<GoodsVo> userList = gm.findGoods(goods);
		PageInfo<GoodsVo> pageInfo = new PageInfo<GoodsVo>(userList);
		return pageInfo;
	}
	
	public Goods findBycode(Integer code){
		
		return gm.findBycode(code);
	}
	public List<Goodstype>  findGoodsType(){
		return tm.selectAll();
	}

	public void addType(Goodstype type) {
		type.setCreatetime(new Date());
		tm.insert(type);
		Goodstype gt=tm.findByName(type.getTypename());
		Jedis jedis = jp.getResource();
		jedis.hset("Goodstype", gt.getId()+"", gt.toString());
	}

	public List<Tj> findTj() {
		// TODO Auto-generated method stub
		return gm.findTj();
	}

}
