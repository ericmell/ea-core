package com.app.ea.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.hsql.Hsql;
import com.app.ea.model.Organize;
import com.app.ea.model.Organizegroup;
import com.app.ea.model.Resource;
import com.app.ea.model.Role;
import com.app.ea.model.Rolegroup;
import com.app.ea.model.Smtp;
import com.app.ea.model.Systempara;
import com.app.ea.model.User;


@Component("HotelData") 
public class ParentDemo  {
	private final Logger log = LoggerFactory.getLogger(ParentDemo.class);
	protected BaseDao baseDao;
	public BaseDao getbaseDao() {
		return baseDao;
	}
	@javax.annotation.Resource(name = "eaDaoTarget")
	public void setbaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public User createUser(String account, String password) {
		User user = new User();
		user.setAccount(account);
		user.setPasswd(password);
		user.setName(account);
		baseDao.create(user);
		return user;
	}

	public Organize create_organize(String name, String alias,
			Organizegroup organizegroup) throws Exception {
		Organize organize = new Organize();
		organize.setName(name);

		organize.setAlias(alias);
		baseDao.create(organize);
		if (!(organizegroup == null)) {
			organize.getOrganizegroups().add(organizegroup);
			baseDao.update(organize);
		}

		return organize;
	}

	public Smtp create_smtp(String sender, String host, String account,
			String passwd, String port, String title) throws Exception {
		Smtp smtp = new Smtp();
		smtp.setAccount(account);
		smtp.setPasswd(passwd);
		smtp.setHost(host);
		smtp.setPort(port);
		smtp.setSender(sender);
		smtp.setTitle(title);
		baseDao.create(smtp);
		baseDao.update(smtp);
		return smtp;
	}

	// 根据姓名 更新身份证、生日、入职日期
	public void update_user(Long id, String inentifyCard, String birthDate,
			String ruzhiDate) throws Exception {
		User user = (User) baseDao.loadById("User", id);
		user.setIdentityCard(inentifyCard);
		user.setBirthDate(birthDate);
		user.setRollDate(ruzhiDate);
		baseDao.update(user);
	}

	public User create_user(String account, String name, String phonenumber,
			String email, String assessLev) throws Exception {
		User user = new User();
		user.setAccount(account);
		user.setName(name);
		user.setPasswd("abc123");
		user.setAccount(account);
		user.setPhoneNumber(phonenumber);
		user.setEmail(email);
		user.setAssessLev(assessLev);
		baseDao.create(user);
		return user;
	}

	public Organize create_sub_organize(Organize organize, String name,
			String alias, Organizegroup organizegroup) throws Exception {
		Organize suborganize = new Organize();
		suborganize.setName(name);
		suborganize.setAlias(alias);

		suborganize.setParentModel(organize);
		baseDao.create(suborganize);
		if (!(organizegroup == null)) {
			suborganize.getOrganizegroups().add(organizegroup);
			baseDao.update(suborganize);
		}
		return suborganize;
	}

	public Rolegroup create_rolegroup(String name, String alias)
			throws Exception {
		Rolegroup rolegroup = new Rolegroup();
		rolegroup.setName(name);
		rolegroup.setAlias(alias);
		baseDao.create(rolegroup);
		return rolegroup;
	}

	public Rolegroup create_sub_rolegroup(Rolegroup rolegroup, String name,
			String alias) throws Exception {
		Rolegroup subrolegroup = new Rolegroup();
		subrolegroup.setName(name);
		subrolegroup.setAlias(alias);
		subrolegroup.setParentModel(rolegroup);
		baseDao.create(subrolegroup);
		return subrolegroup;
	}

	public Resource create_resource(String name, String alias, String url)
			throws Exception {
		Resource resource = new Resource();
		resource.setName(name);
		resource.setAlias(alias);
		resource.setActionUrl(url);
		baseDao.create(resource);
		return resource;
	}

	public Resource create_sub_resource(Resource resource, String name,
			String alias, String url) throws Exception {
		Resource subresource = new Resource();
		subresource.setName(name);
		subresource.setAlias(alias);
		subresource.setActionUrl(url);
		subresource.setParentModel(resource);
		baseDao.create(subresource);
		return subresource;
	}

	public void batCreateRole(String organizeId, String rolegroupIdString) {

		log.debug("organizeId=" + organizeId);
		log.debug("rolegroupIdString=" + rolegroupIdString);

		String[] rolegroupId = rolegroupIdString.split("-");
		Organize organize = (Organize) baseDao.loadById("Organize",
				Long.parseLong(organizeId));
		for (int i = 0; i < rolegroupId.length; i++) {
			Rolegroup rolegroup = (Rolegroup) baseDao.loadById("Rolegroup",
					Long.parseLong(rolegroupId[i]));
			log.debug(rolegroup.getName());
			Role role = new Role();
			role.setName(organize.getName() + "-" + rolegroup.getName());
			role.setAlias(organize.getAlias() + "-" + rolegroup.getAlias());
			role.getOrganizes().add(organize);
			role.getRolegroups().add(rolegroup);
			baseDao.create(role);
		}
	}

	public void batCreateRoleByRolegroupId(String rolegroupId, String organizeIdString) {

		Rolegroup rolegroup = (Rolegroup) baseDao.loadById("Rolegroup",
				Long.parseLong(rolegroupId));

		String[] organizeId = organizeIdString.split("-");
		for (int i = 0; i < organizeId.length; i++) {
			
			Organize organize = (Organize) baseDao.loadById("Organize",
					Long.parseLong(organizeId[i]));
			
			log.debug(rolegroup.getName());
			Role role = new Role();
			role.setName(organize.getName() + "-" + rolegroup.getName());
			role.setAlias(organize.getAlias() + "-" + rolegroup.getAlias());
			role.getOrganizes().add(organize);
			role.getRolegroups().add(rolegroup);
			baseDao.create(role);
		}
	}

	
	public Organizegroup create_organizegroup(Organizegroup parent,
			String name, String alias) throws Exception {
		Organizegroup organizegroup = new Organizegroup();
		organizegroup.setParentModel(parent);
		organizegroup.setName(name);
		organizegroup.setAlias(alias);
		baseDao.create(organizegroup);
		return organizegroup;
	}
	
	public Systempara create_systempara(String key,String value)
			throws Exception {
		Systempara systempara = new Systempara();
		systempara.setKey(key);
		systempara.setValue(value);
		baseDao.create(systempara);
		return systempara;
	}

	public Systempara create_sub_systempara(Systempara systempara, String key,
			String value) throws Exception {
		Systempara subsystempara = new Systempara();
		subsystempara.setKey(key);
		subsystempara.setValue(value);
		subsystempara.setParentModel(systempara);
		baseDao.create(subsystempara);
		return subsystempara;
	}
	
}
