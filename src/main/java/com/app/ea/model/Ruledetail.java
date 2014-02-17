package com.app.ea.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.app.common.spring.ssh.model.BaseModel;

@Entity
@Table(name = "manager_ea_ruledetail")
public class Ruledetail extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long sortNob = 0l;
	private Rule rule ;
	private String align;

	private String imagefilepath;
	private String imagewidth;
	private String imageheight;

	private String videowidth;
	private String videoheight;
	private String videofilepath;	
	
	private String attachfilepath;	
	private String attachfilename;
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String name;

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public Long getSortNob() {
		return sortNob;
	}

	public void setSortNob(Long sortNob) {
		this.sortNob = sortNob;
	}
  
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "rule_id", nullable = true)
	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getImagefilepath() {
		return imagefilepath;
	}

	public void setImagefilepath(String imagefilepath) {
		this.imagefilepath = imagefilepath;
	}

	public String getImagewidth() {
		return imagewidth;
	}

	public void setImagewidth(String imagewidth) {
		this.imagewidth = imagewidth;
	}

	public String getImageheight() {
		return imageheight;
	}

	public void setImageheight(String imageheight) {
		this.imageheight = imageheight;
	}

	public String getVideowidth() {
		return videowidth;
	}

	public void setVideowidth(String videowidth) {
		this.videowidth = videowidth;
	}

	public String getVideoheight() {
		return videoheight;
	}

	public void setVideoheight(String videoheight) {
		this.videoheight = videoheight;
	}

	public String getVideofilepath() {
		return videofilepath;
	}

	public void setVideofilepath(String videofilepath) {
		this.videofilepath = videofilepath;
	}

	public String getAttachfilepath() {
		return attachfilepath;
	}

	public void setAttachfilepath(String attachfilepath) {
		this.attachfilepath = attachfilepath;
	}

	public String getAttachfilename() {
		return attachfilename;
	}

	public void setAttachfilename(String attachfilename) {
		this.attachfilename = attachfilename;
	}
	
	
}