package com.app.ea.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.app.common.base.action.BaseEaAction;
import com.app.ea.model.Rule;
import com.app.ea.model.Ruledetail;



@Component("ruledetailAction")
@SuppressWarnings("rawtypes")
@Scope("prototype")
public class RuledetailAction extends BaseEaAction {


	private static Logger log = LoggerFactory.getLogger(RuledetailAction.class);
    public Ruledetail getRuledetail(){
    	 Ruledetail ruledetail=(Ruledetail)baseDao.loadById("Ruledetail", Long.parseLong(getpara("id")));
    	 return ruledetail;
    }
    public Rule getRule(){
    	  Rule rule=(Rule)baseDao.loadById("Rule", Long.parseLong(getpara("parentId")));
    	  return rule;
    }

	public String menu_ruledetail() throws Exception {
		//getPageData();
		Rule rule=(Rule)baseDao.loadById("Rule", Long.parseLong(getpara("parentId")));
		rhs.put("rule",rule);
		rhs.put("dataList",rule.getRuledetails());
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}

	public String create() throws Exception {
		Ruledetail ruledetail = new Ruledetail();
		ruledetail.setName("");
		ruledetail.setAlign("left");
		Rule rule=getRule();
		ruledetail.setRule(rule);
		baseDao.create(ruledetail);
		rhs.put("dataList",rule.getRuledetails());
		rhs.put("rule",rule);
		rhs.put("info_type", "success");
		rhs.put("info", "create success!");
		return "success";
	}
	public String change_rank() throws Exception {
		common_change_rank(); 
	
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		getPageData();
		return "success";
	}
	public String upload(){
		rhs.put("ruledetail",getRuledetail());
		return "success";
	}	
	public String upload_save(){
		try {
			ServletActionContext.getRequest().getParameterMap().keySet();
			for (Iterator iterator = ServletActionContext.getRequest().getParameterMap().keySet().iterator(); iterator.hasNext();) {
				String  key = (String ) iterator.next();
				log.debug(key);
				
			}
			
			/**/
			String newfilename=getNewfilename(fileFileName);
			//String newfilename="a.flv";
			InputStream is = new FileInputStream(file);
			File deskFile = new File(getWebroot() + "/file/resource/", newfilename);
			OutputStream os = new FileOutputStream(deskFile);
			byte[] bytefer = new byte[1024];
			int length = 0;
			while ((length = is.read(bytefer)) != -1) {
				os.write(bytefer, 0, length);
			}
			os.close();
			is.close();
			Ruledetail ruledetail=getRuledetail();
			String colomn=getpara("column");
			BeanUtils.setProperty(ruledetail, colomn ,newfilename);
			baseDao.update(ruledetail);
			log.debug("image路径"+ruledetail.getImagefilepath());
			log.debug("video路径"+ruledetail.getVideofilepath());
			rhs.put("ruledetail",ruledetail);
			/*
			File[] files = (File[]) ServletActionContext.getContext()
					.getParameters().get("file");
			for (int i = 0; i < files.length; i++) {
				String houzhui = StringUtils.substringAfterLast(files[i].getName(),
						".");
				System.out.println(files[i].getName()+"后缀："+houzhui);
			}*/
			menu_ruledetail();
		} catch (Exception e) {
			log.debug(e.toString());
		}
		return "success";
	}
	public String delete() throws Exception {
		Ruledetail ruledetail=(Ruledetail)baseDao.loadById("Ruledetail", Long.parseLong(getpara("id")));
		Rule rule=getRule();
		
		log.debug("个数："+rule.getRuledetails().size());
		rule.getRuledetails().remove(ruledetail);
		ruledetail.setRule(null);
		
		log.debug("个数："+rule.getRuledetails().size());
		baseDao.update(rule);
		baseDao.update(ruledetail);
		//baseDao.delete(ruledetail);
		log.debug("--个数："+rule.getRuledetails().size());
		rhs.put("dataList",rule.getRuledetails());
		rhs.put("rule",rule);
		rhs.put("info_type", "success");
		rhs.put("info", "delete success!");
		return "success";
	}

	public String update() throws Exception {
		String id = getpara("id");
		String column = ServletActionContext.getRequest()
				.getParameter("column");
		String columnValue = getpara("columnValue");
		if (column.equals("maxSize")) {
			int pageNum = Integer.parseInt(columnValue);
			if (pageNum != pagination.getMaxSize() && pageNum > 0) {
				pagination.setMaxSize(pageNum);
			}
		} else {
			Ruledetail ruledetail = (Ruledetail) baseDao.loadById("Ruledetail",
					Long.parseLong(id));
			BeanUtils.setProperty(ruledetail, column, columnValue);
			baseDao.update(ruledetail);
		}

		if (false) {
			rhs.put("info_type", "error");
			rhs.put("info", "错误异常代码演示!");
		} else {
			rhs.put("info_type", "success");
			rhs.put("info", "update success!");
		}
		Rule rule=getRule();
		rhs.put("dataList",rule.getRuledetails());
		rhs.put("rule",rule);
		return "success";
	}

	@SuppressWarnings("unchecked")
	public void getPageData() throws Exception {
		String pageId = getpara("pageId");
		int maxSize = 20;
		if (getSessionValue("maxSize") == null
				|| getSessionValue("maxSize").equals("")) {
			maxSize = pagination.getMaxSize();
		} else {
			maxSize = Integer.parseInt(getSessionValue("maxSize").toString());
			pagination.setMaxSize(maxSize);

		}
		if (pageId == null || "".equals(pageId)) {
			pageId = (String) ServletActionContext.getRequest().getSession()
					.getAttribute("pageId");
			if (pageId == null || "".equals(pageId))
				pageId = "1";
		}

		putSessionValue("pageId", pageId);

		pagination.setCurrentPage(Integer.parseInt(pageId));
		List ruledetailList = baseDao.page("from Ruledetail", pagination);
		rhs.put("dataList", ruledetailList);
		List countList = baseDao.find("from Ruledetail");
		rhs.put("maxSize", maxSize);
		rhs.put("count", countList.size());
		rhs.put("maxPage",
				countList.size() % pagination.getMaxSize() > 0 ? countList
						.size() / pagination.getMaxSize() + 1 : countList
						.size() / pagination.getMaxSize());
		rhs.put("currentPage", Integer.parseInt(pageId));
	}

	public String change_page_number() throws Exception {
		putSessionValue("maxSize", getpara("maxSize"));
		getPageData();
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}

	public String ajax_page_data() throws Exception {
		getPageData();
		rhs.put("info_type", "success");
		rhs.put("info", "success!");
		return "success";
	}
	

}
