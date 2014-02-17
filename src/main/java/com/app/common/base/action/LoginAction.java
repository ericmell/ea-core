package com.app.common.base.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.ea.api.InfEa;
import com.app.ea.model.Organize;
import com.app.ea.model.Resource;
import com.app.ea.model.User;

import com.opensymphony.xwork2.ActionContext;

@Component("loginAction")
@Scope("prototype")
public class LoginAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(LoginAction.class);
	public InfEa infEa;

	public String execute() throws Exception {
	    infEa.initData();
		
		String account = getpara("account");
		String password = getpara("password");
		String sysName = getpara("sysName");
		if (account.equals("")) {
			rhs.put("tipInfo", "用户不能为空");
			return "fail";
		}
		User user = (User) infEa.getUserbyAccount(account);
		if (user==null) {
			rhs.put("tipInfo", "用户名不存在");
			return "fail";
		}
		String result = "";
		if (getSessionValue("userlogined") != null && (!account.equals(""))) { // 切换用户
			sysName = getSessionValue("sysName").toString();
			result = "0000";
		} else {
			result = infEa.checkLogin(account, password);
		}

		if (result.equals("0001")) {
			rhs.put("tipInfo", "用户不存在或者密码错误");
		}
		if (result.equals("0000")) { // 验证成功
			putSessionValue("sysName", sysName);
			
			Resource resource = (Resource) infEa.getBaseModelByAlias(
					"Resource", sysName);
			List menuList = new ArrayList();
			/* 为了满足用户查询需要，暂时全部返回，这里需要处理下，以后 */
			putSessionValue("userList", infEa.getAllUser());
			Set resourceSet = infEa.getAllVisibleResource(user.getId()
					.toString());
			if (user.getAccount().equals("admin")) {
				menuList = baseDao.find("from Resource");
				resourceSet.addAll(menuList);
			}
			/*
			 * log.debug("用户"+user.getAccount()+user.getName()+"可以访问的菜单"); for
			 * (Iterator iterator = resourceSet.iterator(); iterator.hasNext();)
			 * { Resource res = (Resource) iterator.next();
			 * log.info("["+res.getName()+"]"); }
			 */
			putSessionValue("menuList", resourceSet);
			List projectList = infEa.getUserAllOrganizeByOrganizegroupAlias(
					user.getId(), "project");
			List departmentList = infEa.getUserAllOrganizeByOrganizegroupAlias(
					user.getId(), "company");

			putSessionValue("projectList", projectList);
			putSessionValue("departmentList", departmentList);
			putSessionValue("userlogined", user);

			if (projectList.size() > 0) {
				Organize projectOrganize = (Organize) projectList.get(0);
				putSessionValue("currnetProject", projectOrganize);
			}
			if (departmentList.size() > 0) {
				Organize departmentOrganize = (Organize) departmentList.get(0);
				putSessionValue("currnetDepartment", departmentOrganize);
			}
			if (user.getAccount().equals("admin")) {
				return "admin";
			}
			return "success";
		}

		return "fail";
	}

	public String changeProject() {
		String id = getpara("id");
		Organize organize = (Organize) baseDao.loadById("Organize",
				Long.parseLong(id));
		setUserCurrentProject(organize);
		log.debug("当前项目ID=" + getCurrentProjectId());
		return "success";
	}

	public String ajaxResetPassword() {
		// 取得旧密码，比较是否正取，比较新密码， password_repeat, password_error,password_change

		String oldpassword = getpara("oldpassword");
		String newpassword = getpara("newpassword");
		Map sessionMap = ActionContext.getContext().getSession();
		User currentuser = (User) sessionMap.get("userlogined");
		String currentpassword = currentuser.getPasswd();
		if (!oldpassword.equals(currentpassword)) {
			String errorinfo = "密码输入错误！请正确输入当前密码";
			rhs.put("info", errorinfo);
		} else {
			// String userId = getpara("userid");
			// User user = (User) baseDao.loadById("User",
			// Long.parseLong(userId));
			infEa.resetPassword(currentuser.account, getpara("newpassword"));
			rhs.put("info", "修改成功");
		}
		return "ajaxResetPassword";
	}

	@javax.annotation.Resource(name = "impEa")
	public void setInfEa(InfEa infEa) {
		this.infEa = infEa;
	}

	public InfEa getInfEa() {
		return infEa;
	}

}
