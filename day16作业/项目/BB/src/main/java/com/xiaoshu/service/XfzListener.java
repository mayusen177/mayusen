package com.xiaoshu.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.xiaoshu.dao.BankMapper;
import com.xiaoshu.entity.Bank;

public class XfzListener implements MessageListener {
	@Autowired
	private BankMapper bm;
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		TextMessage text=(TextMessage) message;
		try {
			String id = text.getText();
			System.out.println("发送的消息是:------"+id);
			int bId = Integer.parseInt(id);
			Bank b=bm.findById(bId);
			System.out.println("根据id查询到的数据是：---"+b.toString());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
