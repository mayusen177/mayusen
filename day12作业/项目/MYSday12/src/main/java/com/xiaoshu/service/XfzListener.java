package com.xiaoshu.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.xiaoshu.entity.Major;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class XfzListener implements MessageListener{
	@Autowired
	private JedisPool jd;
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		TextMessage text=(TextMessage) message;
		try {
			String maj = text.getText();
			Major major = JSONObject.parseObject(maj, Major.class);
			System.out.println("接收的消息是:------"+major.toString());
			Jedis jedis = jd.getResource();
			jedis.set(major.getMdname(), major.getMdId()+"");
			String string = jedis.get(major.getMdname());
			System.out.println(string);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
