package com.xiaoshu.entity;

public class Tj {

	private String typename;
	private Integer num;
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "Tj [typename=" + typename + ", num=" + num + "]";
	}
	
}
