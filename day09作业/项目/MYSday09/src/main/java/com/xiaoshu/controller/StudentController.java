package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.Tj;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.StudentService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("student")
public class StudentController extends LogController{
	static Logger logger = Logger.getLogger(StudentController.class);

	@Autowired
	private StudentService ss;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("studentIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<Course> clist = ss.findCourse();
		request.setAttribute("clist", clist);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "student";
	}
	
	
	@RequestMapping(value="studentList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,StudentVo studentvo) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<StudentVo> slist = ss.findStudentPage(studentvo, pageNum, pageSize);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",slist.getTotal() );
			jsonObj.put("rows", slist.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
	}
	
	
	// 新增或修改
	@RequestMapping("reserveStudent")
	public void reserveUser(HttpServletRequest request,Student student,HttpServletResponse response){
		Integer userId = student.getId();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
					ss.updateStudent(student);
					result.put("success", true);		
			}else {   // 添加
					ss.addStudent(student);
					result.put("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("deleteStudent")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				ss.deleteStudent(Integer.parseInt(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("addcourse")
	public void addcourse(HttpServletRequest request,HttpServletResponse response,Course course){
		JSONObject result=new JSONObject();
		try {
					Course course1=ss.findBycode(course.getCode());
					if(course1!=null){
						result.put("success", true);
						result.put("errorMsg", "该课程编号已存在");
					}else{
						ss.addcourse(course);
						result.put("success", true);
					}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	@RequestMapping("tj")
	public void tj(HttpServletRequest request,HttpServletResponse response){
		List<Tj> tlist=ss.findTj();
		Object json=JSONObject.toJSON(tlist);
		WriterUtil.write(response, json.toString());
	}
	@RequestMapping("dc")
	public void dc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		String[] arr={"学生编号","学生姓名","学生年龄","所选课程","所属年级","入校时间","创建时间"};
		for (int i = 0; i < arr.length; i++) {
			row.createCell(i).setCellValue(arr[i]);
		}
		List<StudentVo> slist=ss.findStuden();
		for (int i = 0; i < slist.size(); i++) {
			HSSFRow row2 = sheet.createRow(i+1);
			StudentVo vo = slist.get(i);
			row2.createCell(0).setCellValue(vo.getId());
			row2.createCell(1).setCellValue(vo.getName());
			row2.createCell(2).setCellValue(vo.getAge());
			row2.createCell(3).setCellValue(vo.getCname());
			row2.createCell(4).setCellValue(vo.getGrade());
			row2.createCell(5).setCellValue(TimeUtil.formatTime(vo.getEntrytime(), "yyyy-MM-dd"));
			row2.createCell(6).setCellValue(TimeUtil.formatTime(vo.getCreatetime(), "yyyy-MM-dd"));
		}
		File file=new File("D:/学生信息表.xls");
		FileOutputStream os=new FileOutputStream(file);
		wb.write(os);
		wb.close();
	}
}
