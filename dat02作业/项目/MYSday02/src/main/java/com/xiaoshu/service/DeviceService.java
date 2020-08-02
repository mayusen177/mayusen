package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.DeviceMapper;
import com.xiaoshu.dao.DevicetypeMapper;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.Devicetype;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class DeviceService {

	@Autowired
	private DeviceMapper dm;
	@Autowired
	private DevicetypeMapper tm;
	
	public PageInfo<Device> findDevicePage(Device device, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Device> deviceList = dm.queryDevice(device);
		PageInfo<Device> pageInfo = new PageInfo<Device>(deviceList);
		return pageInfo;
	}
	
	// 新增
		public void addDevice(Device d) throws Exception {
			dm.insert(d);
		};

		// 修改
		public void updateDevice(Device d) throws Exception {
			dm.updateByPrimaryKey(d);
		};

		// 删除
		public void deleteDevice(Integer id) throws Exception {
			dm.deleteByPrimaryKey(id);
		}

		public List<Device> findDeByName(String devicename) {
			
			return dm.findByName(devicename);
		}

		public List<Devicetype> findType() {
			return tm.findType();
		}

		public List<Device> findDevice() {
			
			return dm.queryDevice(null);
		};

	
}
