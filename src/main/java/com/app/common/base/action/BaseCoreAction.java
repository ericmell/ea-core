package com.app.common.base.action;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.common.spring.ssh.action.BaseAction;
import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.api.InfEa;
import com.app.ea.model.Extv;
import com.app.ea.model.Organize;
import com.app.ea.model.User;



public class BaseCoreAction extends BaseAction {
	private final Logger log = LoggerFactory
			.getLogger(BaseCoreAction.class);
	protected InfEa infEa;
	@Resource(name = "impEa")
	public void setInfEa(InfEa infEa) {
		this.infEa = infEa;
	}
	public InfEa getInfEa() {
		return infEa;
	}	
	public Organize getCurrentProject() {
		try {
			Organize project = (Organize) getSessionValue("currnetProject");
			return project;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public Organize getCurrentDepartment() {
		try {
			Organize department = (Organize) getSessionValue("currnetDepartment");
			return department;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getCurrentProjectId() {
		try {
			Organize project = (Organize) getSessionValue("currnetProject");
			return project.getId().toString();
		} catch (Exception e) {
			return "";
		}

	}
	public User getCurrentUser() {
		try {
			User user = (User) getSessionValue("userlogined");
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getCurrentAccount() {
		try {
			User user = (User) getSessionValue("userlogined");
			return user.account;
		} catch (Exception e) {
		//	e.printStackTrace();
			return "";
		}

	}

	public void setUserCurrentProject(Organize organize) {
		try {
			putSessionValue("currnetProject", organize);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}	
	public void common_get_para_map(){
		rhs.put("system_para_map", 	infEa.getParaMap());
	}
	public void common_update_extp() throws Exception {
		 String extpalias=getpara("extpalias");
		 String modelid=getpara("modelid");
		 String modelname=getpara("modelname");
		 common_update_extp( modelname,  modelid,  extpalias,getpara("value"));
	}	
	public void common_update_extp(String modelname, String modelid, String extpalias,String value) throws Exception {
		
		 List  valueList= (List) baseDao.find( "from Extv ev where ev.modelname='"+modelname+"' and ev.extpalias='" +extpalias+"' and ev.modelid='" +modelid+"'");
		 if(valueList.size()>0){
			 Extv extv = (Extv) valueList.get(0);
			 extv.setValue(java.net.URLDecoder.decode(value));   
			 baseDao.update(extv);
		 }else{
			 Extv extv =new Extv();
			 extv.setExtpalias(extpalias);
			 extv.setModelid(getpara("modelid"));
			 extv.setModelname(modelname);
			 extv.setValue(java.net.URLDecoder.decode(getpara("value")));
			 baseDao.create(extv);
		 }
	}	
}
