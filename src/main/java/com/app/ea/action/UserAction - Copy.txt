package com.app.manager.ea.action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.manager.common.base.action.BaseEaAction;
import com.app.manager.ea.hsql.Hsql;
import com.app.manager.ea.model.Organize;
import com.app.manager.ea.model.User;

import com.common.time.TimeUtil;
@Component("userAction")
@Scope("prototype")
@SuppressWarnings("unchecked")
public class UserAction extends BaseEaAction {
	static Logger log = LoggerFactory.getLogger(UserAction.class);
	public User user = new User();

	public String menu_user() throws Exception {
		getPageData(Hsql.All_USER);
		return "success";
	}

	public String create() throws Exception {
		
		infEa.createUser("", "abc123");
		getPageData(Hsql.All_USER);
		return "success";
	}
 
	public String regedit() throws Exception {
		User who =(User) infEa.getbaseDao().loadById("User", Long.parseLong(getpara("id")));
		List<Organize> organizelist = infEa
				.getOrganizeByOrganizegroupAlias("tech");
		/*
		User who = (User) infEa.getUserbyAccount(getpara("account"));
		*/
		rhs.put("organizelist", organizelist);
		rhs.put("user", who);
		return "success";
	}
	public String photo_upload() throws Exception {
		User who =(User) infEa.getbaseDao().loadById("User", Long.parseLong(getpara("id")));
		rhs.put("user", who);
		return "success";
	}
	/*
	public String photo_save() throws Exception {
		User who =(User) infEa.getbaseDao().loadById("User", Long.parseLong(getpara("id")));
		String imagefilename = who.getImgfilename();
		try {
			InputStream is = new FileInputStream(file);
			String fileName = this.getFileFileName();
			imagefilename = DateUtil.getTimeStr("yyyyMMddhhmmssSSS")
					+ fileName.substring(fileName.lastIndexOf("."));
			File deskFile = new File(SystemInit.WEB_ROOT + "/file/photo/", imagefilename);
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
		who.setImgfilename(imagefilename);
		baseDao.updateObject(who);
		rhs.put("user", who);
		return "success";
	}	
	
	public String regeditSave() throws Exception {
	
		User who =(User) infEa.getbaseDao().loadById("User", user.getId());
		String imagefilename = who.getImgfilename();
		user.setRoles(who.getRoles());
		user.setResources(who.getResources());
		try {
			InputStream is = new FileInputStream(file);
			String fileName = this.getFileFileName();
			imagefilename = DateUtil.getTimeStr("yyyyMMddhhmmssSSS")
					+ fileName.substring(fileName.lastIndexOf("."));
			File deskFile = new File(SystemInit.WEB_ROOT + "/file/photo/", imagefilename);
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
		baseDao.updateObject(who);
		rhs.put("user", who);
		return "success";
	}
*/
	/*
	public String uploadPhoto() throws Exception {
		User who = (User) infEa.getUserbyAccount(getpara("account"));
		String msg = "";
		try {
			InputStream is = new FileInputStream(file);
			String fileName = this.getFileFileName();
			String saveName = who.getAccount();
			File deskFile = new File(SystemInit.WEB_ROOT  + "/file/photo/", saveName);
			who.setImgfilename(saveName);
			OutputStream os = new FileOutputStream(deskFile);
			byte[] bytefer = new byte[1024];
			int length = 0;
			while ((length = is.read(bytefer)) != -1) {
				os.write(bytefer, 0, length);
			}
			os.close();
			is.close();
			baseDao.update(who);
		} catch (NullPointerException e) {
			msg = "{'StatusCode':'-1','message':'文件上传失败! 失败原因:文件过大'}";
			rhs.put("info", msg);
		} catch (Exception e) {
			msg = "{'StatusCode':'-2','message':'文件上传失败! 失败原因:"
					+ e.getMessage() + "'}";
			rhs.put("info", msg);
		}
		rhs.put("user", who);
		return "success";
	}
	*/
	
	public String regedit_sum() throws Exception {
		List organizeRootList = infEa.getOrganizeRootNods();
		rhs.put("organizeRootList", organizeRootList);
		return "success";
	}

	public String delete() throws Exception {
		String id = getpara("id");
		User userdata = new User();
		userdata.setId(Long.parseLong(id));
		infEa.deleteUser(userdata);
		getPageData(Hsql.All_USER);
		rhs.put("info_type", "success");
		rhs.put("info", "delete success!");		
		return "success";
	}
	public String update() throws Exception {
		common_update(Hsql.All_USER);
		return "success";
	}
    //修改每页显示的个数
	public String change_page_number() throws Exception {
		putSessionValue("maxSize", getpara("maxSize"));
		getPageData(Hsql.All_USER);
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}
     //翻页
	public String ajax_page_data() throws Exception {
		getPageData(Hsql.All_USER);
		rhs.put("info_type", "success");
		rhs.put("info", "success!");
		return "success";
	}
	
	
	public String menu_query_user_resource() throws Exception {
		getPageData(Hsql.All_USER);
		return "success";
	}

	public String ajax_query_user_by_name() {
		String name = getpara("username");
		List list = infEa.getAllResoucesByUserName(name);
		rhs.put("name", name);
		rhs.put("list", list);
		return "success";
	}
	
	public String search_by_level() throws Exception {
		
		String hsql = Hsql.All_USER + "where  upper(assessLev) like '%"+ getpara("assessLev")+"%'";
		getPageData(hsql);
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
