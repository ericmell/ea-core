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
@Table(name = "manager_ea_rule")
public class Rule extends BaseModel {
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
	private Long sortNob = 0l;
	private Rule parentModel;
	private Set<Rule> childRules = new HashSet<Rule>();
    private Set<Ruledetail> ruledetails = new HashSet<Ruledetail>();
	
	
	@OneToMany(mappedBy = "parentModel", cascade = CascadeType.ALL)
	public Set<Rule> getChildRules() {
		return childRules;
	}

	public void setChildRules(Set<Rule> childRules) {
		this.childRules = childRules;
	}

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id", nullable = true)
	public Rule getParentModel() {
		return parentModel;
	}

	
	
	public void setParentModel(Rule parentModel) {
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
	
	@OneToMany(mappedBy = "rule", cascade = CascadeType.ALL)
	public Set<Ruledetail> getRuledetails() {
		return ruledetails;
	}

	public void setRuledetails(Set<Ruledetail> ruledetails) {
		this.ruledetails = ruledetails;
	}

	
	
	
}