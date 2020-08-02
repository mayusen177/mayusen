package com.xiaoshu.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "tb_stu_day")
public class Studay implements Serializable {
    @Id
    @Column(name = "sd_id")
    private Integer sdId;

    private String sdname;

    private String sdsex;

    private String sdhobby;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date sdbirth;

    private Integer mid;
    @Transient
    private Majorday majorday;

    private static final long serialVersionUID = 1L;

    public Majorday getMajorday() {
		return majorday;
	}

	public void setMajorday(Majorday majorday) {
		this.majorday = majorday;
	}

	/**
     * @return sd_id
     */
    public Integer getSdId() {
        return sdId;
    }

    /**
     * @param sdId
     */
    public void setSdId(Integer sdId) {
        this.sdId = sdId;
    }

    /**
     * @return sdname
     */
    public String getSdname() {
        return sdname;
    }

    /**
     * @param sdname
     */
    public void setSdname(String sdname) {
        this.sdname = sdname == null ? null : sdname.trim();
    }

    /**
     * @return sdsex
     */
    public String getSdsex() {
        return sdsex;
    }

    /**
     * @param sdsex
     */
    public void setSdsex(String sdsex) {
        this.sdsex = sdsex == null ? null : sdsex.trim();
    }

    /**
     * @return sdhobby
     */
    public String getSdhobby() {
        return sdhobby;
    }

    /**
     * @param sdhobby
     */
    public void setSdhobby(String sdhobby) {
        this.sdhobby = sdhobby == null ? null : sdhobby.trim();
    }

    /**
     * @return sdbirth
     */
    public Date getSdbirth() {
        return sdbirth;
    }

    /**
     * @param sdbirth
     */
    public void setSdbirth(Date sdbirth) {
        this.sdbirth = sdbirth;
    }

    /**
     * @return mid
     */
    public Integer getMid() {
        return mid;
    }

    /**
     * @param mid
     */
    public void setMid(Integer mid) {
        this.mid = mid;
    }

    @Override
	public String toString() {
		return "Studay [sdId=" + sdId + ", sdname=" + sdname + ", sdsex=" + sdsex + ", sdhobby=" + sdhobby
				+ ", sdbirth=" + sdbirth + ", mid=" + mid + ", majorday=" + majorday + "]";
	}
}