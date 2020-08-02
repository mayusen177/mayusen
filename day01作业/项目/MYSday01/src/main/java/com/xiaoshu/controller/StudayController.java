package com.xiaoshu.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Cx;
import com.xiaoshu.entity.Majorday;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Studay;
import com.xiaoshu.entity.Tj;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.StudayService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

@Controller
@RequestMapping("stu")
public class StudayController {

	@Autowired
	private StudayService ss;
	@Autowired
	private OperationService operationService;
	@RequestMapping("stuIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		return "stu";
	}
	
	
	@RequestMapping(value="stuList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,Cx cx) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Studay> stuList= ss.findStuPage(cx, pageNum, pageSize);
			List<Majorday> mlist = ss.findMajor();
			request.getSession().setAttribute("mlist", mlist);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",stuList.getTotal() );
			jsonObj.put("rows", stuList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	// 新增或修改
	@RequestMapping("reserveStu")
	public void reserveUser(HttpServletRequest request,Studay stu,HttpServletResponse response){
		Integer id = stu.getSdId();
		JSONObject result=new JSONObject();
		try {
			if (id != null) {   // userId不为空 说明是修改
					List<Studay> slist=ss.findStuByName(stu.getSdname());
				if(slist.size()>0){
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}else{
					ss.updateStuday(stu);
					result.put("success", true);
					
				}
				
			}else {   // 添加
				List<Studay> slist=ss.findStuByName(stu.getSdname());
				if(slist.size()>0){  // 没有重复可以添加
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				} else {
					ss.addStuday(stu);
					result.put("success", true);	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("deleteUser")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				ss.deleteStuday(Integer.parseInt(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	//导出
	@RequestMapping("dc")
	public void dc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		String[] arr={"编号","姓名","性别","爱好","生日","专业"};
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < arr.length; i++) {
			row.createCell(i).setCellValue(arr[i]);
		}
		int a=1;
		List<Studay> slist = ss.findStuday();
		for (int i = 0; i < slist.size(); i++) {
			Studay studay = slist.get(i);
			if(studay.getSdhobby().contains("篮球") && studay.getMid()==1){
				HSSFRow row2 = sheet.createRow(a);
				row2.createCell(0).setCellValue(studay.getSdId());
				row2.createCell(1).setCellValue(studay.getSdname());
				row2.createCell(2).setCellValue(studay.getSdsex());
				row2.createCell(3).setCellValue(studay.getSdhobby());
				row2.createCell(4).setCellValue(TimeUtil.formatTime(studay.getSdbirth(), "yyyy-MM-dd"));
				row2.createCell(5).setCellValue(studay.getMajorday().getMdname());
				a++;
			}
		}
		File file=new File("D:/h1909d学生表.xls");
		FileOutputStream os=new FileOutputStream(file);
		wb.write(os);
		wb.close();
	}
	//添加专业
	@RequestMapping("addMajor")
	public void addMajor(HttpServletRequest request,Majorday major,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			   // 添加
					ss.addMajor(major);
					result.put("success", true);	
				
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	//柱状图
	@RequestMapping("tj")
	public void tj(HttpServletRequest request,HttpServletResponse response){
		List<Tj> tjlist = ss.findTj();
		Object json=JSONObject.toJSON(tjlist);
		WriterUtil.write(response, json.toString());	
	}
}
