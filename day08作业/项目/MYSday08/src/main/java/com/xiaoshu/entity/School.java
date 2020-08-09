package com.xiaoshu.entity;

import java.io.Serializable;
import javax.persistence.*;

public class School implements Serializable {
    @Id
    private Integer scid;

    private String scname;

    private static final long serialVersionUID = 1L;

    /**
     * @return scid
     */
    public Integer getScid() {
        return scid;
    }

    /**
     * @param scid
     */
    public void setScid(Integer scid) {
        this.scid = scid;
    }

    /**
     * @return scname
     */
    public String getScname() {
        return scname;
    }

    /**
     * @param scname
     */
    public void setScname(String scname) {
        this.scname = scname == null ? null : scname.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", scid=").append(scid);
        sb.append(", scname=").append(scname);
        sb.append("]");
        return sb.toString();
    }
}