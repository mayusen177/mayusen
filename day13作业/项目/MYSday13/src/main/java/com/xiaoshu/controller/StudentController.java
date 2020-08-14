package com.xiaoshu.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import org.springframework.web.multipart.MultipartFile;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Major;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
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
		List<Major> mlist = ss.findMajor();
		request.setAttribute("mlist", mlist);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "student";
	}
	
	
	@RequestMapping(value="studentList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,StudentVo student) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<StudentVo> slist = ss.findStudentPage(student, pageNum, pageSize);
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
		Integer userId = student.getsId();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
				
					ss.updateStudent(student);
					result.put("success", true);
				
				
			}else {   // 添加
					Student student1=ss.findStudentByName(student.getsName());
					if(student1==null){
						ss.addStudent(student);
						result.put("success", true);
					}else{
						result.put("success", true);
						result.put("errorMsg", "该学生已存在");
					}
					
				
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
	
	@RequestMapping("tj")
	public void tj(HttpServletRequest request,HttpServletResponse response){
		List<StudentVo> tlist = ss.findTj();
		Object json=JSONObject.toJSON(tlist);
		WriterUtil.write(response, json.toString());
	}
	@RequestMapping("dc")
	public void dc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		String[] arr={"学生编号","学生姓名","学生性别","学生爱好","学生生日","专业"};
		for (int i = 0; i < arr.length; i++) {
			row.createCell(i).setCellValue(arr[i]);
		}
		List<StudentVo> slist=ss.findStu();
		int a=1;
		for (int i = 0; i < slist.size(); i++) {
			StudentVo vo = slist.get(i);
			HSSFRow row2 = sheet.createRow(a);
			if(vo.getsHobby().length()>2){
				row2.createCell(0).setCellValue(vo.getsId());
				row2.createCell(1).setCellValue(vo.getsName());
				row2.createCell(2).setCellValue(vo.getsSex());
				row2.createCell(3).setCellValue(vo.getsHobby());
				row2.createCell(4).setCellValue(TimeUtil.formatTime(vo.getsBirth(), "yyyy-MM-dd"));
				row2.createCell(5).setCellValue(vo.getMname());
				a++;
			}
		}
		File file=new File("D:/学生管理表8-14.xls");
		FileOutputStream os=new FileOutputStream(file);
		wb.write(os);
		wb.close();
	}
	// 导入
	@RequestMapping("dr")
	public void dr(HttpServletRequest request,HttpServletResponse response,MultipartFile file){
		JSONObject result=new JSONObject();
		try {
			HSSFWorkbook wb=new HSSFWorkbook(file.getInputStream());
			HSSFSheet sheet = wb.getSheetAt(0);
			int num = sheet.getLastRowNum();
			for (int i = 1; i <= num; i++) {
				HSSFRow row = sheet.getRow(i);
				Student stu=new Student();
				//学生编号","学生姓名","学生性别","学生爱好","学生生日","专业"
				stu.setsName(row.getCell(1).getStringCellValue());
				stu.setsSex(row.getCell(2).getStringCellValue());
				stu.setsHobby(row.getCell(3).getStringCellValue());
				stu.setsBirth(TimeUtil.ParseTime(row.getCell(4).getStringCellValue(), "yyyy-MM-dd"));
				String mName = row.getCell(5).getStringCellValue();
				Major major=ss.findMajorByName(mName);
				if(major==null){
					Major major1=new Major();
					major1.setmName(mName);
					ss.addMajor(major1);
					 major=ss.findMajorByName(mName);
				}
				stu.setmId(major.getmId());
				ss.addStudent(stu);
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
	// 新增专业
		@RequestMapping("addmajor")
		public void addmajor(HttpServletRequest request,Major major,HttpServletResponse response){
			JSONObject result=new JSONObject();
			try {
					Major major2 = ss.findMajorByName(major.getmName());
					if(major2==null){
						ss.addMajor(major);
						result.put("success", true);
					}else{
						result.put("success", true);
						result.put("errorMsg", "该专业已存在");
					}
						

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("保存用户信息错误",e);
				result.put("success", true);
				result.put("errorMsg", "对不起，操作失败");
			}
			WriterUtil.write(response, result.toString());
		}
}
