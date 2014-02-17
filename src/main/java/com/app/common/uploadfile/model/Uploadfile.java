package com.app.common.uploadfile.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.app.common.spring.ssh.model.BaseModel;

@Entity
@Table(name = "oa_uploadfile")
public class Uploadfile extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Long id;
	
	private String fileType;
	private String fileName;
	private String fileDescription;
	private String foreignId;
	private String url;

	public Uploadfile() {
		super();
	}
	
	public Uploadfile(String fileType, String fileName, String fileDescription, String foreignId, String url) {
		super();
		this.fileType = fileType;
		this.fileName = fileName;
		this.fileDescription = fileDescription;
		this.foreignId = foreignId;
		this.url = url;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}