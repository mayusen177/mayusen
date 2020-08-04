package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CompanyMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.Tj;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class PersonService {

	@Autowired
	private PersonMapper pm;
	@Autowired
	private CompanyMapper cm;
	
	public PageInfo<Person> findPersonPage(int pageNum, int pageSize,Person person) {
		PageHelper.startPage(pageNum, pageSize);
		List<Person> personList = pm.queryPerson(person);
		PageInfo<Person> pageInfo = new PageInfo<Person>(personList);
		return pageInfo;
	}
	// 新增
		public void addPerson(Person p) throws Exception {
			pm.insert(p);
		};

		// 修改
		public void updatePerson(Person p) throws Exception {
			pm.updateByPrimaryKey(p);
		};

		// 删除
		public void deletePerson(Integer id) throws Exception {
			pm.deleteByPrimaryKey(id);
		}
		public List<Company> findCompany() {
			
			return cm.findCompany();
		}
		public List<Tj> findTj() {
			// TODO Auto-generated method stub
			return pm.findTj();
		};


}
