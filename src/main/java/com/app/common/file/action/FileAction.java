package com.app.common.file.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.api.InfEa;
import com.app.ea.model.Organize;
import com.app.ea.model.Resource;
import com.app.ea.model.User;
import com.opensymphony.xwork2.ActionContext;
import com.utils.time.TimeUtil;

@Component("fileAction")
public class FileAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(FileAction.class);
	public User user = new User();
	public String execute() throws Exception {
    	return "success";
	}
	
	public String image_upload() throws Exception {
		return file_upload();
	}	
		

	public String file_upload() throws Exception {
		String id = getpara("id");
		String beanname = getpara("beanname");
		BaseModel baseModel = (BaseModel) baseDao.loadById(beanname,
				Long.parseLong(id));
		if(getpara("op").equals("w")){
			InputStream is = new FileInputStream(file);
			String fileName = this.getFileFileName();
			String newfilename = TimeUtil.getTimeStr("yyyyMMddhhmmssSSS")
						+ fileName.substring(fileName.lastIndexOf("."));
			String filepathname="";
			if(getpara("folder").length()>0){
				filepathname = getWebroot() + "/file/"+getpara("folder")+"/";
			}else{
				filepathname = getWebroot() + "/file/";
			}
			File deskFile = new File(filepathname+newfilename);
			FileUtils.forceMkdir(new File(filepathname));

			/*if(!deskFile.exists())    
			{    
				deskFile.createNewFile();    
			}    */
			OutputStream os = new FileOutputStream(deskFile);
			byte[] bytefer = new byte[1024];
			int length = 0;
			while ((length = is.read(bytefer)) != -1) {
				os.write(bytefer, 0, length);
			}
			os.close();
			is.close();
			rhs.put(getpara("propertyfilepath"), BeanUtils.getProperty(baseModel, getpara("propertyfilepath")));
			BeanUtils.setProperty(baseModel, getpara("propertyfilepath"), newfilename);
			try {
				BeanUtils.setProperty(baseModel, getpara("propertyfilename"), fileName);
			} catch (Exception e) {
				// TODO: 不一定要保文件名的
			}		
			baseDao.update(baseModel);
			rhs.put("beanname", beanname);
			rhs.put("propertyname", getpara("propertyname"));
				
		}else{
			rhs.put("propertyfilepath", getpara("propertyfilepath"));
			rhs.put("propertyfilename", getpara("propertyfilename"));
			rhs.put("beanname", getpara("beanname"));
			rhs.put("folder", getpara("folder"));
			
		}	
		rhs.put("filepath", BeanUtils.getProperty(baseModel, getpara("propertyfilepath")));
		try {
			rhs.put("filename", BeanUtils.getProperty(baseModel, getpara("propertyfilename")));
		} catch (Exception e) {
			rhs.put("filename", "");
			// TODO: 不一定要保文件名的
		}	
		rhs.put("object", baseModel);
		rhs.put("op", "w");
		return "success";
	}	
			
	
	
	public String regedit() throws Exception {
		User who = (User) infEa.getUserbyAccount(getCurrentAccount());
		List<Organize> organizelist = infEa
				.getOrganizeByOrganizegroupAlias("tech");
		rhs.put("organizelist", organizelist);
		return "success";
	}

	public String regeditSave() throws Exception {
		User who = (User) infEa.getUserbyAccount(user.getAccount());
		String imagefilename = who.getImgfilename();
		user.setRoles(who.getRoles());
		user.setResources(who.getResources());
		try {
			String filepath = System.getProperty("webroot",
					"./src/main/webapp/");
			InputStream is = new FileInputStream(file);
			String fileName = this.getFileFileName();

			imagefilename = who.getAccount()
					+ fileName.substring(fileName.lastIndexOf("."));
			File deskFile = new File(filepath + "file/photo/", imagefilename);
			OutputStream os = new FileOutputStream(deskFile);
			byte[] bytefer = new byte[1024];
			int length = 0;
			while ((length = is.read(bytefer)) != -1) {
				os.write(bytefer, 0, length);
			}
			os.close();
			is.close();

		} catch (Exception e) {
			log.debug(e.toString());
		}

		BeanUtils.copyProperties(who, user);
		who.setImgfilename(imagefilename);
		infEa.updateOjbect(who);
		rhs.put("user", who);

		putSessionValue("userlogined", user);
		// return "regedit";
		return "success";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}	

}
