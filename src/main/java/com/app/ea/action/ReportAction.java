package com.app.ea.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.hsql.Hsql;
import com.app.ea.model.Organize;
import com.app.ea.model.Organizegroup;
import com.app.ea.model.Report;
import com.app.ea.model.Role;
import com.app.ea.model.Rolegroup;
import com.app.ea.model.User;
import com.utils.cache.Cache;
import com.utils.file.FileProcessor;
import com.utils.time.TimeUtil;

@Component("reportAction")
public class ReportAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(ReportAction.class);
	//private static String hsql_all = "from Report";
	
	public void common_get_user_info(List userList) {
		for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			user.setAllrolegroup("");
			user.setAllrole("");
			try {
				String companyname = "";
				companyname = infEa.getUserFirestOrgNameByOrgGroup(
						user.getId(), "company").getName();
				user.setCompanyname(companyname);
			} catch (Exception e) {
				user.setCompanyname("未分配公司");
			}
			try {
				String teamname = "";
				Organize org=infEa.getOrganzieOfUserByOrganizeGroupAlias(
						user.getId(), "team");
				teamname = org.getName();
				user.setTeamname(teamname);
				user.setGroupname(org.getParentModel().getName());
			} catch (Exception e) {
				user.setTeamname("");
				user.setGroupname("");
			}

			String techname="";
			try {
				for (Iterator iterator2 = user.getRoles().iterator(); iterator2
						.hasNext();) {
					Role role = (Role) iterator2.next();
					if(role.getAlias()!=null){
						user.setAllrole(user.getAllrole()+"-"+role.getAlias());
					}
					for (Iterator iterator3 = role.getOrganizes().iterator(); iterator3
							.hasNext();) {
						Organize organize = (Organize) iterator3.next();
						if(organize.getParentModel()!=null&&organize.getParentModel().getAlias()!=null){
							if(organize.getParentModel().getAlias().equalsIgnoreCase("tech")){
								techname=techname+role.getName()+";";
							}
						}
					}
					for (Iterator iterator3 = role.getRolegroups().iterator(); iterator3
							.hasNext();) {
						Rolegroup rolegroup = (Rolegroup) iterator3.next();
						if(rolegroup.getAlias()!=null){
							user.setAllrolegroup(user.getAllrolegroup()+"-"+rolegroup.getAlias());
						}
					}
						
				}
				
			} catch (Exception e) {
				System.out.println("异常用户："+user.getName()+e.toString());
			
			}		
			user.setTechname(techname);
			/*
			try {
				String techname = "";
				techname = infEa.getOrganzieOfUserByOrganizeGroupAlias(
						user.getId(), "tech").getName();
				user.setTechname(techname);
			} catch (Exception e) {
				user.setTechname("未分配技术领域");
			}
			*/
			continue;
		}
	}

	public String report_user_check() throws Exception {
		List userList = baseDao.find(Hsql.All_USER);
		common_get_user_info(userList);
		rhs.put("userList", userList);
		return "success";
	}
	public String birt_user() throws Exception {
	
		List userList = (List)Cache.get("userlist");
		
		if (userList == null) {
			userList = baseDao.find(Hsql.All_USER);
			common_get_user_info(userList);
			Cache.set("userlist", userList, "8h"); // 放入缓存
		}
		if (getpara("show").equals("")) {
			putSessionValue("show", "none");
		} else {
			putSessionValue("show",  getpara("show"));
		}		
		rhs.put("dataList", userList);
		return "success";
	}
	
	public String assessment() throws Exception {
		
		birt_user();
		return "success";
	}

	public String tech_member() throws Exception {
		List organizeRootList = infEa.getOrganizeRootNods();
		Organize organize=(Organize)infEa.getOrganizeByAlias("tech");
		rhs.put("system_para_map", 	infEa.getParaMap());   
		rhs.put("organizeRootList", organize.getChildOrganizes());
		rhs.put("userList", infEa.getAllUser());
		return "success";
	}	
	
	public String report() throws Exception {
		String year = getpara("year");
		if("".equals(year))
		{
			year = TimeUtil.getCurrentYear().toString();
		}		
		rhs.put("year", year);
		return "success";
	}	
	
	public String data_save() throws Exception {
		String date = getpara("date");
		String Content = java.net.URLDecoder.decode(getpara("content"));
		String column = getpara("column");
		String type = getpara("retype");
		String account =getCurrentUser().getAccount();
		ArrayList reportList= (ArrayList) baseDao.find("select r from Report r where r.userAccount = '" + account+"' and r."+column+"='" + date+"' and r.type = '"+type+"'");
		
		if(reportList.size()<1){	
			Report r = new Report();
			r.setDate(date);
			r.setContent(Content);
			r.setUserAccount(account);
			r.setType(type);
			baseDao.create(r);
			rhs.put("content",Content);
		}
		else{
			Report re = (Report) reportList.get(0);
			re.setContent(Content);
			baseDao.update(re);
		}		
		return "success";
	}
	
	public String data_read() throws Exception {	
		String column = getpara("column");
		String date = getpara("value");	
		String account =getCurrentUser().getAccount();
		String type = getpara("retype");
		System.out.println("type "+ type);
		ArrayList reportList= (ArrayList) baseDao.find("select r from Report r where r.userAccount = '" + account+"' and r."+column+"='" + date+"' and r.type = '"+type+"'");

		if(reportList.size()<1){
			rhs.put("content", "");
		}
		else{
			Report re = (Report) reportList.get(0);
			rhs.put("content", re.getContent());
		}
		return "success";
	}
	
	public String sumReport() throws Exception {
		String year = getpara("theYear");
		String type = getpara("type");
		String userA = getpara("userAccount");
		String account;
		if(userA.equals("")){
			account = getCurrentUser().getAccount();
			System.out.println("no account here");
		}
		else{
			account = userA;
			System.out.println("get account here");
		}
		ArrayList reportList= (ArrayList) baseDao.find("select r from Report r where r.type = '" + type +"' and r.userAccount = '" + account+ "'and date like '"+year+"%'");
		rhs.put("sumReport",reportList);
		rhs.put("year", year);
		rhs.put("type", type);
		return "success";
	}	
	
	public String ipm() throws Exception {
		List hrworkshopList = baseDao.find("from Hrworkshop");
		rhs.put("workshopList", hrworkshopList);
		rhs.put("organizeRootList",infEa.getOrganizeRootNods());
		if (getpara("show").equals("")) {
			putSessionValue("show", "");
		} else {
			putSessionValue("show",  "report");
		}		
		return "success";
	}		
}
