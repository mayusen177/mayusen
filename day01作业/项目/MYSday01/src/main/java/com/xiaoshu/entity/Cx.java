package com.xiaoshu.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Cx {
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date star;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date end;
	public Date getStar() {
		return star;
	}
	public void setStar(Date star) {
		this.star = star;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	@Override
	public String toString() {
		return "Cx [star=" + star + ", end=" + end + "]";
	}
	
}
