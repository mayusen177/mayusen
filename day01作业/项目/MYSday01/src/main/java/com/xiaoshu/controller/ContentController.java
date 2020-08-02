package com.xiaoshu.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Content;
import com.xiaoshu.entity.Contentcategory;
import com.xiaoshu.entity.Cx2;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.ContentService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;

@Controller
@RequestMapping("content")
public class ContentController {

	@Autowired
	private ContentService cs;
	@Autowired
	private OperationService operationService;
	@RequestMapping("conIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		return "content";
	}
	
	
	@RequestMapping(value="contentList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,Cx2 cx) throws Exception{
		try {
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Content> conList = cs.findContentPage(cx, pageNum, pageSize);
			List<Contentcategory> calist=cs.findCa();
			request.getSession().setAttribute("calist", calist);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",conList.getTotal() );
			jsonObj.put("rows", conList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// 新增或修改
		@RequestMapping("reserveContent")
		public void reserveUser(HttpServletRequest request,Content content,HttpServletResponse response,Content con,MultipartFile file){
			Integer conid = content.getContentid();
			List<Content> conlist=cs.findcaByName(con.getContenttitle());
			String filename = file.getOriginalFilename();
			JSONObject result=new JSONObject();
			try {
				if (conid != null) {   // userId不为空 说明是修改
					if(conlist.size()>0){
						result.put("success", true);
						result.put("errorMsg", "该用户名被使用");
					}else{
						if(filename!=null && filename!=""){
							String newname=UUID.randomUUID().toString()+filename.substring(filename.lastIndexOf("."));
							file.transferTo(new File("D:/pic/"+newname));
							con.setPicpath(newname);
						}
						cs.updateContent(con);
						result.put("success", true);
						
					}
					
				}else {   // 添加
					if(conlist.size()>0){  // 没有重复可以添加
						result.put("success", true);
						result.put("errorMsg", "该用户名被使用");
					} else {
						if(filename!=null && filename!=""){
							String newname=UUID.randomUUID().toString()+filename.substring(filename.lastIndexOf("."));
							file.transferTo(new File("D:/pic/"+newname));
							con.setPicpath(newname);
						}
						con.setCreatetime(new Date());
						cs.addContent(con);
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
		
		
		@RequestMapping("deleteContent")
		public void delUser(HttpServletRequest request,HttpServletResponse response){
			JSONObject result=new JSONObject();
			try {
				String[] ids=request.getParameter("ids").split(",");
				for (String id : ids) {
					cs.deleteContent(Integer.parseInt(id));
				}
				result.put("success", true);
				result.put("delNums", ids.length);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("errorMsg", "对不起，删除失败");
			}
			WriterUtil.write(response, result.toString());
		}
	//导入
		@RequestMapping("dr")
		public void dr(HttpServletRequest request,HttpServletResponse response,MultipartFile file) throws Exception{
			JSONObject result=new JSONObject();
			HSSFWorkbook wb=new HSSFWorkbook(file.getInputStream());
			HSSFSheet sheet = wb.getSheetAt(0);
			int num = sheet.getLastRowNum();
			for (int i = 0; i < num; i++) {
				Content con=new Content();
				HSSFRow row = sheet.getRow(i);
				con.setCaid((int)row.getCell(1).getNumericCellValue());
				con.setContenttitle(row.getCell(2).getStringCellValue());
				con.setContenturl(row.getCell(3).getStringCellValue());
				con.setPicpath(row.getCell(4).getStringCellValue());
				con.setPrice((int)row.getCell(5).getNumericCellValue());
				con.setStatus(row.getCell(6).getStringCellValue());
				con.setCreatetime(new Date());
				cs.addContent(con);
			}
			result.put("success", true);
			WriterUtil.write(response, result.toString());
		}
}
