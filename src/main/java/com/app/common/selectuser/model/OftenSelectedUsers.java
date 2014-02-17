package com.app.common.selectuser.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.app.common.spring.ssh.model.BaseModel;

@Entity
@Table(name = "ea_oftenselectedusers")
public class OftenSelectedUsers extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long userId;
	private String userAccount;
	private Long selectedUserId;
	private String selectedUserAccount;
	private String selectedUserName;
	private Integer count;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public Long getSelectedUserId() {
		return selectedUserId;
	}

	public void setSelectedUserId(Long selectedUserId) {
		this.selectedUserId = selectedUserId;
	}

	public String getSelectedUserAccount() {
		return selectedUserAccount;
	}

	public void setSelectedUserAccount(String selectedUserAccount) {
		this.selectedUserAccount = selectedUserAccount;
	}

	public String getSelectedUserName() {
		return selectedUserName;
	}

	public void setSelectedUserName(String selectedUserName) {
		this.selectedUserName = selectedUserName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
