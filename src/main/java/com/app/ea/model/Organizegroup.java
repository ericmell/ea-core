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

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.model.BaseModel;

@Entity
@Table(name = "manager_ea_organizegroup")
public class Organizegroup extends BaseModel {
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

	protected BaseDao baseDao;

	private Set<Organizegroup> childOrganizegroups = new HashSet<Organizegroup>();
	private Organizegroup parentModel;

	private Set<Organize> organizes = new HashSet<Organize>();

	@ManyToMany(cascade = CascadeType.REFRESH, targetEntity = Organize.class, fetch = FetchType.LAZY)
	@JoinTable(name = "manager_ea_organizegroup_organize", joinColumns = { @JoinColumn(name = "organizegroup_id") }, inverseJoinColumns = { @JoinColumn(name = "organize_id") })
	public Set<Organize> getOrganizes() {
		return organizes;
	}

	public void setOrganizes(Set<Organize> organizes) {
		this.organizes = organizes;
	}

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id", nullable = true)
	public Organizegroup getParentModel() {
		return parentModel;
	}

	public void setParentModel(Organizegroup parentModel) {
		this.parentModel = parentModel;
	}

	@OneToMany(mappedBy = "parentModel", cascade = CascadeType.ALL)
	public Set<Organizegroup> getChildOrganizegroups() {
		return childOrganizegroups;
	}

	public void setChildOrganizegroups(Set<Organizegroup> childOrganizegroups) {
		this.childOrganizegroups = childOrganizegroups;
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