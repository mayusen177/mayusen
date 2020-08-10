package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CourseMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.Tj;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class StudentService {

	@Autowired
	private StudentMapper sm;
	@Autowired
	private CourseMapper cm;
	@Autowired
	private JedisPool jp;

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

	public PageInfo<StudentVo> findStudentPage(StudentVo studentvo, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> userList = sm.findStudent(studentvo);
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(userList);
		return pageInfo;
	}
	public List<Course> findCourse() {
		
		return cm.selectAll();
	}

	public void addcourse(Course course) {
		course.setCreatetime(new Date());
		cm.insert(course);
		Course course1=cm.findByName(course);
		Jedis jedis = jp.getResource();
		jedis.hset("course",course1.getId()+"", course1.toString());
		
	}

	public Course findBycode(Integer code) {
		// TODO Auto-generated method stub
		return cm.findBycode(code);
	}

	public List<Tj> findTj() {
		// TODO Auto-generated method stub
		return sm.findTj();
	}

	public List<StudentVo> findStuden() {
		// TODO Auto-generated method stub
		return sm.findStudent(null);
	}
		
	

}
