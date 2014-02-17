package com.app.ea.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.app.common.spring.ssh.model.BaseModel;

@Entity
@Table(name = "manager_ea_workpage2")
public class WorkPage2 extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Long id;
	public String projectId;
	private String title;
	private String requestDesc;
	

	public WorkPage2() {
		super();
	}

	public WorkPage2(String projectId, String title,  String requestDesc) {
		super();
		this.projectId = projectId;
		this.title = title;		
		this.requestDesc = requestDesc;
		
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}


	public String getRequestDesc() {
		return requestDesc;
	}

	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
	}



	@Override
	public String toString() {
		return "WorkPage2 [id=" + id + ", projectId=" + projectId + ", title=" + title + ",  requestDesc=" + requestDesc + "]";
	}
	
}