package com.app.ea.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.common.spring.ssh.action.BaseAction;
import com.app.common.spring.ssh.action.BaseBusinessAction;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.model.Organize;
import com.app.ea.model.Role;
import com.app.ea.model.Rule;
import com.app.ea.model.User;
import com.app.ea.model.Viewhistory;


import com.utils.file.FileProcessor;
import com.utils.freemark.StringbyPerlFreemark;
import com.utils.path.Path;
import com.utils.path.SystemInit;
import com.utils.time.TimeUtil;



import freemarker.template.SimpleHash;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

@Component("viewAction")
public class ViewAction extends EaAction {
	private final Logger log = LoggerFactory.getLogger(ViewAction.class);
	private String fileFileName;
	private File file;
	public String list_organize_file() throws Exception {
		String[] filenames=FileProcessor.getfilenames(getWebroot()+"/file/organize", "*.*");
		ArrayList  filelist=new ArrayList();
		for (int i = 0; i < filenames.length; i++) {
			filelist.add(filenames[i]);
		}
		rhs.put("filelist", filelist);
		return "success";
	}	
	
	public String picture_role_user_v() {
		Role role = (Role) baseDao.loadById("Role",
				Long.parseLong(getpara("roleId")));
		rhs.put("role", role);
		return "success";
		

	}
	
	
	public String picture_all_role_user_v() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";
		

	}
		
	

	public String picture_user_in_organize() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		
		User user = (User) baseDao.loadById("User",
				Long.parseLong(getpara("userId")));
		rhs.put("user", user);
		return "success";
		

	}	
	
	public String position_node_v() {
		Role role = (Role) baseDao.loadById("Role",
				Long.parseLong(getpara("id")));
		rhs.put("role", role);
		rhs.put("rolegrouplist", baseDao.find(" from Rolegroup"));
		return "success";
	}
	
	

	public String picture_organize_v() {
		
		rhs.put("rootname",getName());
		rhs.put("organizeRootList",getRootList());
		rhs.put("id",getpara("id"));
		putSessionValue("show","");
	    putSessionValue("show",getpara("show"));
		return "success";
	}	
	
	
	
	
	
	
	public String getName() {
		if(getpara("id")==""){
			return "架构图";
		}else{
			Organize organize = (Organize) baseDao.loadById("Organize",
					Long.parseLong(getpara("id")));
			return organize.getName();
		}
	}	
	public List getRootList() {
		if(getpara("id")==""){
			return baseDao.find(" from Organize where parent_id = null");
		}else{
			Organize organize = (Organize) baseDao.loadById("Organize",
					Long.parseLong(getpara("id")));
			rhs.put("organize", organize);
		    ArrayList rootList=new ArrayList();
		    for (Iterator iterator = organize.getChildOrganizes().iterator(); iterator.hasNext();) {
				Organize o = (Organize) iterator.next();
				rootList.add(o);
				
			}
			 
			 return rootList;
		}
	}	


	
	public String organize_h() {
		rhs.put("rootname",getName());
		rhs.put("organizeRootList",getRootList());
		rhs.put("id",getpara("id"));
		return "success";
	}
	public String save() throws Exception {
		String filename=TimeUtil.getTimeStr("yyyy-MM-dd-hh-mm-ss");
		Viewhistory viewhistory = new Viewhistory();
		viewhistory.setFilename(filename);
		viewhistory.setName(getpara("name"));
		baseDao.create(viewhistory);
		
		SimpleHash root=new SimpleHash();
		root.put("rootname",getName());
		root.put("show",getSessionValue("show"));
		root.put("organizeRootList", getRootList());
		
		
		StringbyPerlFreemark.getfilebyFreemark(getWebroot() + "/file/view/",filename+".ftl", getWebroot() , "/app/ea/view/"+getpara("t")+".ftl",root);
		rhs.put("filename", filename);
		return "success";

	}	
	public String image_upload() throws Exception {
		String id = getpara("id");
		String beanname = getpara("beanname");
		BaseModel baseModel = (BaseModel) baseDao.loadById(beanname,
				Long.parseLong(id));
		if(getpara("op").equals("w")){
			
			InputStream is = new FileInputStream(file);
			String fileName = this.getFileFileName();
			String imagefilename = TimeUtil.getTimeStr("yyyyMMddhhmmssSSS")
						+ fileName.substring(fileName.lastIndexOf("."));
			File deskFile = new File(getWebroot() + "/file/photo/", imagefilename);
			OutputStream os = new FileOutputStream(deskFile);
			byte[] bytefer = new byte[1024];
			int length = 0;
			while ((length = is.read(bytefer)) != -1) {
				os.write(bytefer, 0, length);
			}
			os.close();
			is.close();
			rhs.put("imgfilename", BeanUtils.getProperty(baseModel, "imgfilename"));
			BeanUtils.setProperty(baseModel, getpara("propertyname"), imagefilename);
			//baseDao.updateObject(baseModel);
			rhs.put("beanname", beanname);
			rhs.put("propertyname", getpara("propertyname"));
						
		}else{
			
			rhs.put("propertyname", getpara("propertyname"));
			rhs.put("beanname", getpara("beanname"));
			
		}	
		rhs.put("object", baseModel);
		rhs.put("op", "w");
		return "success";
	}	
	
	
    //根路径就是  webroot/file/ftl,
	public String edit_ftl() throws Exception{
		String op = getpara("op");
		String content = getpara("content");
		String filepath=getWebroot()+"/file/"+getpara("filefoldname")+"/"+getpara("filename");
		if (op.equals("r")) {
			content =FileProcessor.getStringFromFile(filepath);
			rhs.put("info", "");
		}


		if (op.equals("w")) {
			FileProcessor.strToDoc(filepath, content);
			rhs.put("info_type", "success");
			rhs.put("info", "保存成功！");
		}
		rhs.put("content", content);
		rhs.put("filefoldname", getpara("filefoldname"));
		rhs.put("filename", getpara("filename"));

		return "success";
    }	

	
	public String rolegroup() throws Exception {
		rhs.put("rolegroupRootList", common_get_tree_root("Rolegroup"));
		rhs.put("rolegrouplist", baseDao.find(" from Rolegroup"));
		return "success";
	}	

	public String show_history_orgainze_pic() throws Exception {
		
		return "success";
	}	


	
	
}