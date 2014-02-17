package com.app.common.spring.ssh.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 普通表结构的父类
 * 
 * @author lingli
 * @version 1.0
 * @updated 23-八月-2011 16:10:00
 */

public class BaseModel implements Serializable, Comparable {
	private Long id;
	public Long sortNob = 0l;
	public String inputtime;

	public Long getId() {
		return id;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

	public boolean equals(Object o) {
		if (!(o instanceof BaseModel)) {
			return false;
		}

		BaseModel bm = (BaseModel) o;

		if (this.getId() == null || bm.getId() == null) {
			return super.equals(bm);
		}

		return new EqualsBuilder().append(this.getId(), bm.getId())
				.append(this.getClass(), o.getClass()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	public int compareTo(Object o) {
		BaseModel bm = (BaseModel) o;
		return this.getId().compareTo(bm.getId());
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSortNob() {
		return sortNob;
	}

	public void setSortNob(Long sortNob) {
		this.sortNob = sortNob;
	}

	public String getInputtime() {
		return inputtime;
	}

	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}

}