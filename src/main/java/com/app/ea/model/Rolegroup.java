package com.app.ea.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import com.utils.string.StringProcess;

@Entity
@Table(name = "manager_ea_rolegroup")
public class Rolegroup extends BaseModel {
	private Long id;
	public String name;
	public String alias;
	public String description;
	@Column(length = 2000)
	//
	public String positiondescription;
	@Column(length = 2000)
	//
	public String kpidescription;
	private Set<Rolegroup> childRolegroups = new HashSet();
	private Set<Role> roles = new HashSet();
	private Set<Resource> resource = new HashSet();

	@ManyToMany(cascade = CascadeType.REFRESH, targetEntity = Resource.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_resource_rolegroup", joinColumns = { @JoinColumn(name = "rolegroup_id") }, inverseJoinColumns = { @JoinColumn(name = "resource_id") })
	public Set<Resource> getResources() {
		return resource;
	}

	public void setResources(Set<Resource> resources) {
		this.resource = resources;
	}

	private Rolegroup parentModel;

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id", nullable = true)
	public Rolegroup getParentModel() {
		return parentModel;
	}

	public void setParentModel(Rolegroup parentModel) {
		this.parentModel = parentModel;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "parentModel", cascade = CascadeType.ALL)
	public Set<Rolegroup> getChildRolegroups() {
		return childRolegroups;
	}

	public void setChildRolegroups(Set<Rolegroup> childRolegroups) {
		this.childRolegroups = childRolegroups;
	}

	public String getName() {
		return name;
	}

	public List allUserOfRolegroup() {
		List<User> result_user = new ArrayList();
		for (Iterator iterator = this.getRoles().iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			result_user.addAll(role.getUsers());
		}
		
		for (Iterator iterator = result_user.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
		//	System.out.println("用户"+user.getName());
			
		}
		return result_user;
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

	@ManyToMany(cascade = CascadeType.REFRESH, targetEntity = Role.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_rolegroup_role", joinColumns = { @JoinColumn(name = "rolegroup_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	public Set<Role> getRoles() {
		return roles;
	}
	
	
	
	public Set<Role> RootRoles() {
		 Set<Role> rootRoleSet = new HashSet();
		for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			if(role.getParentModel()==null){
				rootRoleSet.add(role);
			}
		}
		return rootRoleSet;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getPositiondescription() {
		return StringProcess.cleanString(positiondescription);
		
	}

	public void setPositiondescription(String positiondescription) {
		this.positiondescription = positiondescription;
	}

	public String getKpidescription() {
		return StringProcess.cleanString(kpidescription);
	}

	public void setKpidescription(String kpidescription) {
		this.kpidescription = kpidescription;
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