package com.app.ea.model;

import java.util.HashSet;
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
import javax.persistence.Table;

import com.app.common.spring.ssh.model.BaseModel;

@Entity
@Table(name = "manager_ea_user")
public class User extends BaseModel {
	private Long id;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String account;
	private String name;
	private String englishname;
	private String age;
	private String sex;
	private String email;
	private String email2;
	private String passwd;
	private String ericssonid;
	@Column(length = 2000)
	private String description;
	private String companyname;
	private String groupname;
	private String teamname;
	private String techname;
	@Column(length = 600)
	private String allrole;
	@Column(length = 600)
	private String allrolegroup;
	private String phoneNumber;
	private String rollDate;
	private String leftDate;
	private String address;
	private String birthDate;
	private String graduateDate;
	private String qq;
	private String assessLev;
	private String identityCard;
	private String home;
	private String certification;
	private String experience;
	private String score;
	private String mark;
	private String imgfilename;
	private String status;

	private String sognumber;
	private String ksonumber;
	public String conbegindate;
	public String contenddate ; 
	public String conmonfee    ;
	public String condayfee ;   
	public String connetfee  ;
	public String conadminfee  ;
	public String lastdate ;       
	public String laste  ;       
	public String lastw  ;
	public String efee  ;       
	public String wfee  ;
	


	@Column(length = 2000)
	public String positiondescription;
	@Column(length = 2000)
	public String kpidescription;

	/**
	 * inverseJoinColumns: inverse反转的意思，在JPA中可以理解为被维护端
	 * inverseJoinColumns中的＠JoinColumn: 被维护端外键的定义 fum_id:指 中间表那个外键字段与学生表关联
	 * joinColumns: 关系维护端的定义
	 * */

	private Set roles = new HashSet();
	
	
	
	@ManyToMany(cascade = CascadeType.REFRESH, targetEntity = Role.class, fetch = FetchType.LAZY )
	@JoinTable(name = "manager_ea_role_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	public Set getRoles() {
		return roles;
	}

	public void setRoles(Set roles) {
		this.roles = roles;
	}

	private Set<Resource> resource = new HashSet<Resource>();
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Resource.class, fetch = FetchType.LAZY  )
	@JoinTable(name = "manager_ea_resource_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "resource_id") })
	public Set<Resource> getResources() {
		return resource;
	}

	public void setResources(Set<Resource> resources) {
		this.resource = resources;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRollDate() {
		return rollDate;
	}

	public void setRollDate(String rollDate) {
		this.rollDate = rollDate;
	}

	public String getLeftDate() {
		return leftDate;
	}

	public void setLeftDate(String leftDate) {
		this.leftDate = leftDate;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getGraduateDate() {
		return graduateDate;
	}

	public void setGraduateDate(String graduateDate) {
		this.graduateDate = graduateDate;
	}

	
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getAssessLev() {
		if(assessLev==null)return "";
		return assessLev;
	}

	public void setAssessLev(String assessLev) {
		this.assessLev = assessLev;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getImgfilename() {
		return imgfilename;
	}

	public void setImgfilename(String imgfilename) {
		this.imgfilename = imgfilename;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEricssonid() {
		return ericssonid;
	}

	public void setEricssonid(String ericssonid) {
		this.ericssonid = ericssonid;
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

	public String getEnglishname() {
		return englishname;
	}

	public void setEnglishname(String englishname) {
		this.englishname = englishname;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getTeamname() {
		if(teamname==null)return "";
		return teamname;
		
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public String getTechname() {
		return techname;
	}

	public void setTechname(String techname) {
		this.techname = techname;
	}



	public String getAllrole() {
		return allrole;
	}

	public void setAllrole(String allrole) {
		this.allrole = allrole;
	}

	public String getAllrolegroup() {
		return allrolegroup;
	}

	public void setAllrolegroup(String allrolegroup) {
		this.allrolegroup = allrolegroup;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getConbegindate() {
		return conbegindate;
	}

	public void setConbegindate(String conbegindate) {
		this.conbegindate = conbegindate;
	}

	public String getContenddate() {
		return contenddate;
	}

	public void setContenddate(String contenddate) {
		this.contenddate = contenddate;
	}

	public String getConmonfee() {
		return conmonfee;
	}

	public void setConmonfee(String conmonfee) {
		this.conmonfee = conmonfee;
	}

	public String getCondayfee() {
		return condayfee;
	}

	public void setCondayfee(String condayfee) {
		this.condayfee = condayfee;
	}

	public String getLastdate() {
		return lastdate;
	}

	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}

	public String getLaste() {
		return laste;
	}

	public void setLaste(String laste) {
		this.laste = laste;
	}

	public String getLastw() {
		return lastw;
	}

	public void setLastw(String lastw) {
		this.lastw = lastw;
	}

	public String getEfee() {
		return efee;
	}

	public void setEfee(String efee) {
		this.efee = efee;
	}

	public String getWfee() {
		return wfee;
	}

	public void setWfee(String wfee) {
		this.wfee = wfee;
	}

	public String getConnetfee() {
		return connetfee;
	}

	public void setConnetfee(String connetfee) {
		this.connetfee = connetfee;
	}

	public String getConadminfee() {
		return conadminfee;
	}

	public void setConadminfee(String conadminfee) {
		this.conadminfee = conadminfee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSognumber() {
		return sognumber;
	}

	public void setSognumber(String sognumber) {
		this.sognumber = sognumber;
	}

	public String getKsonumber() {
		return ksonumber;
	}

	public void setKsonumber(String ksonumber) {
		this.ksonumber = ksonumber;
	}



	

	
}