package com.xiaoshu.service;

import java.util.List;

import javax.jms.CompletionListener;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.MajordayMapper;
import com.xiaoshu.dao.StudayMapper;
import com.xiaoshu.entity.Cx;
import com.xiaoshu.entity.Majorday;
import com.xiaoshu.entity.Studay;
import com.xiaoshu.entity.Tj;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class StudayService {

	@Autowired
	private StudayMapper sm;
	@Autowired
	private MajordayMapper mm;
	@Autowired
	private JmsTemplate jt;
	
	@Autowired
	 JedisPool jedisPool;
	//分页查询
	public PageInfo<Studay> findStuPage(Cx cx, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Studay> stuList =sm.queryStu(cx);
		PageInfo<Studay> pageInfo = new PageInfo<Studay>(stuList);
		return pageInfo;
	}
	// 新增
		public void addStuday(Studay s) throws Exception {
			sm.insert(s);
		};

		// 修改
		public void updateStuday(Studay s) throws Exception {
			sm.updateByPrimaryKey(s);
		};

		// 删除
		public void deleteStuday(Integer id) throws Exception {
			sm.deleteByPrimaryKey(id);
		};
		//专业查询
		public List<Majorday> findMajor(){
			return mm.findMajor();
		}
		public List<Studay> findStuByName(String sdname) {
			
			return sm.findByName(sdname);
		}
		public void addMajor(Majorday major) {
			mm.insert(major);
			Majorday majorday = findmajByname(major.getMdname());
			Jedis jedis = jedisPool.getResource();
			jedis.set(majorday.getMdId()+"",major.getMdname());
			fs(major.toString());
		}
		public Majorday findmajByname(String mdname){
			return mm.findmajByname(mdname);
		}
		public void fs(final String message){
			jt.send("h1909dssm",new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					return session.createTextMessage(message);
				}
			});
		}
		public List<Studay> findStuday() {
			return sm.queryStu(null);
			
		}
		public List<Tj> findTj(){
			return sm.findTj();
		}
}
