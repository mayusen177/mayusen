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
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.dao.TeacherMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.Teacher;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class StudentService {

	@Autowired
	private StudentMapper sm;
	@Autowired
	private TeacherMapper tm;

	@Autowired
	private JmsTemplate jt;
	// 新增
	public void addStudent(Student t) throws Exception {
		t.setCreatetime(new Date());
		sm.insert(t);
	};

	// 修改
	public void updateStudent(Student t) throws Exception {
		sm.update(t);
	};

	// 删除
	public void deleteStudent(Integer id) throws Exception {
		sm.deleteByPrimaryKey(id);
	};

	//查询所有
	public PageInfo<StudentVo> findStudentPage(StudentVo student, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> userList = sm.findStudent(student);
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(userList);
		return pageInfo;
	}
	//查询老师
	public List<Teacher> findTeacher(){
		return tm.selectAll();
	}
	//echarts图表
	public List<StudentVo> findTj(){
		return sm.findTj();
	}
	//编号查询
	public List<Student> findcode(String code){
		return sm.findcode(code);
	}
	//添加老师
	public void addTeacher(Teacher teacher){
		teacher.setCreatetime(new Date());
		tm.insert(teacher);
		Teacher t=tm.findName(teacher.getName());
		fs(t.toString());
	}
	//mq发送消息
	public void fs(final String message){
		jt.send("h1909essm",new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(message);
			}
		});
	}
}
