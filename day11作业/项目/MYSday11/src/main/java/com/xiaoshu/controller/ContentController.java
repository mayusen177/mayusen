package com.xiaoshu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Content;
import com.xiaoshu.entity.ContentVo;
import com.xiaoshu.entity.Contentcategory;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.ContentService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("content")
public class ContentController extends LogController{
	static Logger logger = Logger.getLogger(ContentController.class);

	@Autowired
	private ContentService cs;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("contentIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<Contentcategory> calist = cs.findCategory();
		request.setAttribute("calist", calist);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "content";
	}
	
	
	@RequestMapping(value="contentList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,ContentVo content) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<ContentVo> clist = cs.findContentPage(content, pageNum, pageSize);
			System.out.println(clist.getList().get(0));
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",clist.getTotal() );
			jsonObj.put("rows", clist.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
	}
	
	
	// 新增或修改
	@RequestMapping("reserveContent")
	public void reserveUser(HttpServletRequest request,Content content,HttpServletResponse response){
		Integer userId = content.getContentid();
		JSONObject result=new JSONObject();
		try {
			String price = content.getPrice()+"";
			if (userId != null) {   // userId不为空 说明是修改
				if(price.length()<4){
					cs.updateContent(content);
					result.put("success", true);
				}else{
					result.put("success", true);
					result.put("errorMsg", "费用不能超过三位数");
				}
				
			}else {   // 添加
				if(price.length()<4){  // 没有重复可以添加
					cs.addContent(content);
					result.put("success", true);
				} else {
					result.put("success", true);
					result.put("errorMsg", "费用不能超过三位数");
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
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("tj")
	public void tj(HttpServletRequest request,HttpServletResponse response){
		List<ContentVo> tlist = cs.findTj();
		Object json=JSONObject.toJSON(tlist);
		WriterUtil.write(response, json.toString());
	}
	
}
