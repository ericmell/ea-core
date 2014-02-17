/*
 * Universial Enterprise Authenticate And Authority Center
 * 
 * Copyright(c) 2005 SunRise Inc. Ricsson Group All rights reserved.
 * 
 * @created: 2005-06-21 @author: Andy
 */

package com.app.ea.interceptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.ea.model.Resource;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;


@Component("authenticateInterceptorClient")
public class AuthenticateInterceptorClient implements Interceptor {
	private final static Log log = LogFactory
			.getLog(AuthenticateInterceptorClient.class);
	public BaseDao baseDao;
	public final static String UNAUTHENTICATED_USER_CODE = "unauthenticated.user";
	
	public BaseDao getBaseDao() {
		return baseDao;
	}
	@javax.annotation.Resource(name = "eaDaoTarget")
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	public void destroy() {
	}
	public void init() {
	}
	Set getResourceActionUrlSet(){
		Set allResourceSet=new HashSet();                
		ArrayList  allResource=(ArrayList)baseDao.find("from Resource");
		for (Iterator iterator = allResource.iterator(); iterator.hasNext();) {
			Resource res = (Resource) iterator.next();
			allResourceSet.add(res.getActionUrl());
		}
		return allResourceSet;
    }
   
	 //
	boolean checkUrlVisit(String url){
		Map<String, Object> sessionMap = ActionContext.getContext().getSession();
		Set currentUserResourceSet=(Set)sessionMap.get("menuList");
		Set allResourceActionUrlSet=getResourceActionUrlSet();
		if(sessionMap.get("userlogined")== null)return false;
		if(currentUserResourceSet!=null){
			for (Iterator iterator = currentUserResourceSet.iterator(); iterator.hasNext();) {
				Resource resource = (Resource) iterator.next();
				if(url.indexOf(resource.getActionUrl())>0||!(allResourceActionUrlSet.contains(resource))){
					return true;
				}
			}
		}
		return false;
	}
   
	public String intercept(ActionInvocation ai) throws Exception {
		log.info("intercepting!");
		log.info( ai.getAction().toString());
		
		System.out.println(ServletActionContext.getRequest().getPathInfo());
		

		log.info("路径getServletPath="+ServletActionContext.getRequest().getServletPath());
		String url=ServletActionContext.getRequest().getServletPath()+"?"+ServletActionContext.getRequest().getQueryString();
		if(checkUrlVisit(url))
			return  ai.invoke();
		else {
		return "can_not_access";
		}
    	/*
		if (getSiteUser() != null) {
			String s = ai.invoke();
			return s;
		} else {
			return UNAUTHENTICATED_USER_CODE;
		}
		*/
		
		
		/*
		 * if (getUser() != null) { String s = ai.invoke(); return s; } else {
		 * return UNAUTHENTICATED_USER_CODE; }
		 */
	}

}