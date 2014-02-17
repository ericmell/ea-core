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

@Entity
@Table(name = "manager_ea_role")
public class Role extends BaseModel {
	private Long id;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	private Role parentModel;
	public String name;
	public String alias;
	@Column(length = 2000)
	public String positiondescription;
	@Column(length = 2000)
	public String kpidescription;
	private Set<User> users = new HashSet<User>();
	private Set<Role> childRoles = new HashSet();
	private Set<Rolegroup> rolegroups = new HashSet<Rolegroup>();

	private Set<Organize> organizes = new HashSet<Organize>();

	private Set<Resource> resources = new HashSet<Resource>();

	@ManyToMany(cascade = CascadeType.REFRESH, targetEntity = Resource.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_resource_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "resource_id") })
	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_role_user", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	public Set getUsers() {
		return users;
	}
   
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public void digui_getSubUser_of_role(Role role,List<User> result_user) {
		result_user.addAll(role.getUsers());
		if(role.getChildRoles()!=null){
			for (Iterator iterator = role.getChildRoles().iterator(); iterator.hasNext();) {
				Role subRole = (Role) iterator.next();
				digui_getSubUser_of_role( subRole,
						 result_user);
			}
			}
	}
	public List allUserUnderRole() {
		List<User> result_user = new ArrayList();
		digui_getSubUser_of_role( this,
				 result_user);
		return result_user;
	}
	
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Rolegroup.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_rolegroup_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "rolegroup_id") })
	public Set<Rolegroup> getRolegroups() {
		return rolegroups;
	}

	public void setRolegroups(Set<Rolegroup> rolegroups) {
		this.rolegroups = rolegroups;
	}

	@OneToMany(mappedBy = "parentModel", cascade = CascadeType.ALL)
	public Set<Role> getChildRoles() {
		return childRoles;
	}

	public void setChildRoles(Set<Role> childRoles) {
		this.childRoles = childRoles;
	}
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Organize.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_organize_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "organize_id") })
	public Set<Organize> getOrganizes() {
		return organizes;
	}

	public void setOrganizes(Set<Organize> organizes) {
		this.organizes = organizes;
	}

	public Role() {
		super();
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

	public String getPositiondescription() {
		return positiondescription;
	}

	public void setPositiondescription(String positiondescription) {
		this.positiondescription = positiondescription;
	}

	public String getKpidescription() {
		return kpidescription;
	}

	public void setKpidescription(String kpidescription) {
		this.kpidescription = kpidescription;
	}
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id", nullable = true)
	public Role getParentModel() {
		return parentModel;
	}

	public void setParentModel(Role parentModel) {
		this.parentModel = parentModel;
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
