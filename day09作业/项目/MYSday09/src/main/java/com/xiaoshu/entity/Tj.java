package com.xiaoshu.entity;

public class Tj {

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
		return "Tj [cname=" + cname + ", num=" + num + "]";
	}
	
}
