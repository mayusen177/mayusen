package com.xiaoshu.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

public class Content implements Serializable {
    @Id
    @Column(name = "contentId")
    private Integer contentid;

    @Column(name = "caId")
    private Integer caid;

    private String contenttitle;

    private String contenturl;

    private String picpath;

    private Integer price;

    private String status;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createtime;
    @Transient
    private Contentcategory contentcategory;
    private static final long serialVersionUID = 1L;
    
    public Contentcategory getContentcategory() {
		return contentcategory;
	}

	public void setContentcategory(Contentcategory contentcategory) {
		this.contentcategory = contentcategory;
	}

	/**
     * @return contentId
     */
    public Integer getContentid() {
        return contentid;
    }

    /**
     * @param contentid
     */
    public void setContentid(Integer contentid) {
        this.contentid = contentid;
    }

    /**
     * @return caId
     */
    public Integer getCaid() {
        return caid;
    }

    /**
     * @param caid
     */
    public void setCaid(Integer caid) {
        this.caid = caid;
    }

    /**
     * @return contenttitle
     */
    public String getContenttitle() {
        return contenttitle;
    }

    /**
     * @param contenttitle
     */
    public void setContenttitle(String contenttitle) {
        this.contenttitle = contenttitle == null ? null : contenttitle.trim();
    }

    /**
     * @return contenturl
     */
    public String getContenturl() {
        return contenturl;
    }

    /**
     * @param contenturl
     */
    public void setContenturl(String contenturl) {
        this.contenturl = contenturl == null ? null : contenturl.trim();
    }

    /**
     * @return picpath
     */
    public String getPicpath() {
        return picpath;
    }

    /**
     * @param picpath
     */
    public void setPicpath(String picpath) {
        this.picpath = picpath == null ? null : picpath.trim();
    }

    /**
     * @return price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * @return createtime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
	public String toString() {
		return "Content [contentid=" + contentid + ", caid=" + caid + ", contenttitle=" + contenttitle + ", contenturl="
				+ contenturl + ", picpath=" + picpath + ", price=" + price + ", status=" + status + ", createtime="
				+ createtime + ", contentcategory=" + contentcategory + "]";
	}
}