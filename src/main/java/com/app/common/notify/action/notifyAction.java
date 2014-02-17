package com.app.common.notify.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.api.InfEa;
import com.app.ea.model.Organize;
import com.app.ea.model.Resource;
import com.app.ea.model.Role;
import com.app.ea.model.Rolegroup;
import com.app.ea.model.Smtp;
import com.app.ea.model.User;

import com.opensymphony.xwork2.ActionContext;
import com.utils.mail.SendMail;
import com.utils.time.TimeUtil;

@Component("notifyAction")
public class notifyAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(notifyAction.class);
	public User user = new User();
	public String execute() throws Exception {
    	return "success";
	}
	
	public String send_email() {
		ArrayList userlist=new ArrayList();
		if(getpara("roleId")!=""){
			Role role=(Role)baseDao.loadById("Role",Long.parseLong(getpara("roleId")));
			
		}
		if(getpara("userId")!=""){
			User user=(User)baseDao.loadById("User",Long.parseLong(getpara("userId")));
			userlist.add(user);
			rhs.put("userList",userlist);
		}		
		if(getpara("rolegroupId")!=""){
		  Rolegroup Rolegroup=(Rolegroup)baseDao.loadById("Rolegroup",Long.parseLong(getpara("rolegroupId")));
		  rhs.put("userList", Rolegroup.allUserOfRolegroup());
		}
		if(userlist.size()<1){
			rhs.put("userList",baseDao.find("from User"));
		}
		return "success";
	}	

	
	public String submit_send_mail() throws Exception {
		List info=new ArrayList();
		/*
		for (Iterator iterator = smtpList.iterator(); iterator.hasNext();) {
			Smtp smtp = (Smtp) iterator.next();
			log.debug(smtp.getHost()+smtp.getPort()+smtp.getAccount()+smtp.getPasswd());
			try {
				SendMail sendMail = new SendMail();
				sendMail.connect(smtp.getHost(), smtp.getAccount(), smtp.getPasswd(), smtp.getPort());
				sendMail.send(smtp.getSender(), getpara("to"), "", "",getpara("subject"), getpara("context"), null);
				sendMail.close();
				info.add(getpara("subject")+"<br>发送成功.  使用邮件服务帐号" +smtp.getTitle()+smtp.getHost()+":"+smtp.getPort()+":"+smtp.getSender());
			} catch (Exception e) {
				//log.debug("发送失败，重新服务器尝试");
				info.add(getpara("subject")+"<br>使用下面邮箱配置发送失败：" +smtp.getTitle()+smtp.getHost()+":"+smtp.getPort()+":"+smtp.getSender());
				e.printStackTrace();
				continue;
			}
		}
		*/
	
		log.debug("内容："+getpara("context"));
		
		String content = java.net.URLDecoder.decode(getpara("content"));
		String subject = java.net.URLDecoder.decode(getpara("subject"));
		String to = java.net.URLDecoder.decode(getpara("to"));
		SendMailTheadBySmtpList.sendmail((ArrayList) baseDao.find("from Smtp"),subject, content + TimeUtil.getTimeStr("yyyy-MM-dd hh:mm:ss"),to, null);
		rhs.put("info", info);
		return "success";
	}	
		


}
