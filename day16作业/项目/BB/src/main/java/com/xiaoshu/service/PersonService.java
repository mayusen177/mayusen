package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.BankMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Bank;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.PersonVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class PersonService {

	@Autowired
	private PersonMapper pm;
	@Autowired
	private BankMapper bm;

	@Autowired
	private JmsTemplate jt;
	// 新增
	public void addPerson(Person t) throws Exception {
		pm.insert(t);
	};

	// 修改
	public void updatePerson(Person t) throws Exception {
		pm.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void deletePerson(Integer id) throws Exception {
		pm.deleteByPrimaryKey(id);
	};

	
	//查询所有
	public PageInfo<PersonVo> findPersonPage(PersonVo person, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<PersonVo> userList = pm.findPerson(person);
		PageInfo<PersonVo> pageInfo = new PageInfo<PersonVo>(userList);
		return pageInfo;
	}
	//查询银行
	public List<Bank> findBank(){
		return bm.selectAll();
	}
	//统计图表
	public List<PersonVo> findTj(){
		return pm.findTj();
	}
	//新增银行
	public void addBank(Bank bank){
		bank.setCreatetime(new Date());
		bm.insert(bank);
		Bank b=bm.findByName(bank.getbName());
		fs(b.getbId()+"");
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

	public List<PersonVo> findPerson() {
		// TODO Auto-generated method stub
		return pm.findPerson(null);
	}
}
