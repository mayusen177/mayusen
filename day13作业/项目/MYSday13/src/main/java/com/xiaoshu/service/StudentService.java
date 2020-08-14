package com.xiaoshu.service;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.MajorMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Major;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class StudentService {

	@Autowired
	private StudentMapper sm;
	@Autowired
	private MajorMapper mm;
	@Autowired
	private JmsTemplate jt;
	@Autowired
	private RedisTemplate rt;

	

	// 新增
	public void addStudent(Student t) throws Exception {
		sm.insert(t);
		rt.delete("StuLists");
	};

	// 修改
	public void updateStudent(Student t) throws Exception {
		sm.updateByPrimaryKeySelective(t);
		rt.delete("StuLists");
	};

	// 删除
	public void deleteStudent(Integer id) throws Exception {
		sm.deleteByPrimaryKey(id);
		rt.delete("StuLists");
	};

	public PageInfo<StudentVo> findStudentPage(StudentVo student, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> StuList=(List<StudentVo>) rt.boundHashOps("StuLists").get("Stu");
		if(StuList==null){
			StuList= sm.findStudent(student);
			rt.boundHashOps("StuLists").put("Stu", StuList);
			System.out.println("从数据库查询到的");
		}else{
			System.out.println("从缓存中读到的");
			
		}
		
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(StuList);
		return pageInfo;
	}

	public List<Major> findMajor(){
		return mm.selectAll();
	}
	public List<StudentVo> findTj(){
		return sm.findTj();
	}

	public List<StudentVo> findStu() {
		// TODO Auto-generated method stub
		return sm.findStudent(null);
	}
	//新增专业
	public void addMajor(Major major) {
		// TODO Auto-generated method stub
		mm.insert(major);
		Major major2 = mm.findMajorByName(major.getmName());
		fs(major2.toString());
		
	}

	public Major findMajorByName(String mName) {
		// TODO Auto-generated method stub
		return mm.findMajorByName(mName);
	}

	public Student findStudentByName(String sName) {
		// TODO Auto-generated method stub
		return sm.findStudentByName(sName);
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
}
