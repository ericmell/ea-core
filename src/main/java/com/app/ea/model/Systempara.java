package com.app.ea.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.app.common.spring.ssh.model.BaseModel;

@Entity
@Table(name = "manager_ea_systempara")
public class Systempara extends BaseModel {
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
	public String key;
	public String value;
	public String alias;
	private Long sortNob = 0l;

	private Systempara parentModel;
	private Set<Systempara> childSystemparas = new HashSet<Systempara>();

	@OneToMany(mappedBy = "parentModel", cascade = CascadeType.ALL)
	public Set<Systempara> getChildSystemparas() {
		return childSystemparas;
	}

	public void setChildSystemparas(Set<Systempara> childSystemparas) {
		this.childSystemparas = childSystemparas;
	}

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id", nullable = true)
	public Systempara getParentModel() {
		return parentModel;
	}

	public void setParentModel(Systempara parentModel) {
		this.parentModel = parentModel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSortNob() {
		return sortNob;
	}

	public void setSortNob(Long sortNob) {
		this.sortNob = sortNob;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}