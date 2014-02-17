package com.app.ea.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.app.common.spring.ssh.model.BaseModel;



@Entity
@Table(name = "manager_ea_resource")
public class Resource extends BaseModel {
	private Long id;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String name;
	public String alias;
	public String description;
	public String actionUrl;
	public String tipInfo;
	public String ruleNum;
	public String type;
	public String newPage;
	public String publi;


	private Resource parentModel;
	private Set<Resource> childResources = new HashSet<Resource>();
	private Set<Rolegroup> rolegroups = new HashSet<Rolegroup>();
	private Set<Role> roles = new HashSet<Role>();
	private Set<User> users = new HashSet<User>();

	@ManyToMany(cascade = CascadeType.REFRESH, targetEntity = Rolegroup.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_resource_rolegroup", joinColumns = { @JoinColumn(name = "resource_id") }, inverseJoinColumns = { @JoinColumn(name = "rolegroup_id") })
	public Set<Rolegroup> getRolegroups() {
		return rolegroups;
	}

	public void setRolegroups(Set<Rolegroup> rolegroups) {
		this.rolegroups = rolegroups;
	}

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id", nullable = true)
	public Resource getParentModel() {
		return parentModel;
	}

	public void setParentModel(Resource parentModel) {
		this.parentModel = parentModel;
	}

	@OneToMany(mappedBy = "parentModel", cascade = CascadeType.ALL)
	public Set<Resource> getChildResources() {
		return childResources;
	}

	public void setChildResources(Set<Resource> childResources) {
		this.childResources = childResources;
	}

	@ManyToMany(cascade = CascadeType.REFRESH, targetEntity = Role.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_resource_role", joinColumns = { @JoinColumn(name = "resource_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_resource_user", joinColumns = { @JoinColumn(name = "resource_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	public Set<User> getUsers() {
		return users;
	}
	
	

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getTipInfo() {
		return tipInfo;
	}

	public void setTipInfo(String tipInfo) {
		this.tipInfo = tipInfo;
	}

	public String getRuleNum() {
		return ruleNum;
	}

	public void setRuleNum(String ruleNum) {
		this.ruleNum = ruleNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNewPage() {
		return newPage;
	}

	public void setNewPage(String newPage) {
		this.newPage = newPage;
	}

	public String getPubli() {
		return publi;
	}

	public void setPubli(String publi) {
		this.publi = publi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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