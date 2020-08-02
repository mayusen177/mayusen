package com.xiaoshu.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
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
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.Devicetype;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.DeviceService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

@Controller
@RequestMapping("device")
public class DeviceController {

	@Autowired
	private DeviceService ds;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("deviceIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		List<Devicetype> dlist=ds.findType();
		request.getSession().setAttribute("dlist", dlist);
		return "device";
	}
	
	
	@RequestMapping(value="deviceList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,Device device) throws Exception{
		try {
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Device> deviceList = ds.findDevicePage(device, pageNum, pageSize);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",deviceList.getTotal() );
			jsonObj.put("rows", deviceList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// 新增或修改
		@RequestMapping("reserveDevice")
		public void reserveUser(HttpServletRequest request,HttpServletResponse response,Device device){
			JSONObject result=new JSONObject();
			List<Device> dlist=ds.findDeByName(device.getDevicename());
			try {
				 // 添加
					if(dlist.size()>0){  // 没有重复可以添加
						result.put("success", true);
						result.put("errorMsg", "该用户名被使用");
					} else {
						device.setCreatetime(new Date());
						ds.addDevice(device);
						result.put("success", true);
						
					}
				
			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", true);
				result.put("errorMsg", "对不起，操作失败");
			}
			WriterUtil.write(response, result.toString());
		}
		
		
		@RequestMapping("deleteDevice")
		public void delUser(HttpServletRequest request,HttpServletResponse response){
			JSONObject result=new JSONObject();
			try {
				String[] ids=request.getParameter("ids").split(",");
				for (String id : ids) {
					ds.deleteDevice(Integer.parseInt(id));
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
			HSSFRow row = sheet.createRow(0);
			String[] arr={"设备编号","设备名称","设备类型名称","内存","颜色","价格","设备状态","创建时间"};
			for (int i = 0; i < arr.length; i++) {
				row.createCell(i).setCellValue(arr[i]);
			}
			List<Device> delist=ds.findDevice();
			for (int i = 0; i < delist.size(); i++) {
				Device device = delist.get(i);
				HSSFRow row2 = sheet.createRow(i+1);
				row2.createCell(0).setCellValue(device.getDeviceid());
				row2.createCell(1).setCellValue(device.getDevicename());
				row2.createCell(2).setCellValue(device.getDevicetype().getTypename());
				row2.createCell(3).setCellValue(device.getDeviceram());
				row2.createCell(4).setCellValue(device.getColor());
				row2.createCell(5).setCellValue(device.getPrice());
				row2.createCell(6).setCellValue(device.getStatus());
				row2.createCell(7).setCellValue(TimeUtil.formatTime(device.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
			}
			
			File file=new File("D:/设备表.xls");
			FileOutputStream os=new FileOutputStream(file);
			wb.write(os);
			wb.close();
		}
}
