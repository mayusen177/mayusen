package com.xiaoshu.entity;

public class ContentVo extends Content{

	private String cname;
	private Integer num;

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "ContentVo "+super.toString()+"[cname=" + cname + ", num=" + num + "]";
	}
	
}
