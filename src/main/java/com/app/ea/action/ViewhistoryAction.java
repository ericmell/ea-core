package com.app.ea.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.spring.ssh.action.BaseBusinessAction;
import com.app.ea.action.EaAction;
import com.app.ea.hsql.Hsql;
import com.app.ea.model.Organize;
import com.app.ea.model.Viewhistory;
import com.utils.freemark.StringbyPerlFreemark;
import com.utils.time.TimeUtil;

import freemarker.template.SimpleHash;

@Component("viewhistoryAction")
@SuppressWarnings("rawtypes")
@Scope("prototype")
public class ViewhistoryAction extends EaAction {
	private static Logger log = LoggerFactory.getLogger(ViewhistoryAction.class);
	public Viewhistory viewhistoryobject = new Viewhistory();

	public String menu_viewhistory() throws Exception {
		getPageData();
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}

	public String load() throws Exception {
		if (!getpara("id").equals("")) {
			Viewhistory viewhistory = (Viewhistory) baseDao.loadById("Viewhistory",
					Long.parseLong(getpara("id")));
			rhs.put("viewhistory", viewhistory);
		} else {
			rhs.put("viewhistory", null);
		}
		return "success";
	}

	public String save() throws Exception {
		if (getpara("id").equals("")) {
			// viewhistoryobject.setBirthDate();
			baseDao.create(viewhistoryobject);
			rhs.put("viewhistory", viewhistoryobject);
			List countList = baseDao.find("from Viewhistory");
			int maxPage = countList.size() % pagination.getMaxSize() > 0 ? countList
					.size() / pagination.getMaxSize() + 1
					: countList.size() / pagination.getMaxSize();
			pagination.setCurrentPage(maxPage);

			List viewhistoryList = baseDao.page("from Viewhistory", pagination);
			rhs.put("dataList", viewhistoryList);
			rhs.put("maxSize", pagination.getMaxSize());
			rhs.put("count", countList.size());
			rhs.put("maxPage", maxPage);
			rhs.put("currentPage", maxPage);

			rhs.put("info_type", "success");
			rhs.put("info", "create success!");
		} else {
			viewhistoryobject.setId(Long.parseLong(getpara("id")));
			Viewhistory viewhistory = (Viewhistory) baseDao.loadById("Viewhistory",
					Long.parseLong(getpara("id")));
			BeanUtils.copyProperties(viewhistory, viewhistoryobject);
			baseDao.update(viewhistory);
			rhs.put("viewhistory", viewhistory);
			getPageData();
		}
		return "success";
	}

	public String create() throws Exception {
		Viewhistory viewhistory = new Viewhistory();
		viewhistory.setName("");
		baseDao.create(viewhistory);
		getPageData();
		List countList = baseDao.find("from Viewhistory");
		// List countList =infEa.getAllViewhistory();
		int maxPage = countList.size() % pagination.getMaxSize() > 0 ? countList
				.size() / pagination.getMaxSize() + 1
				: countList.size() / pagination.getMaxSize();
		pagination.setCurrentPage(maxPage);

		List viewhistoryList = baseDao.page("from Viewhistory", pagination);
		rhs.put("dataList", viewhistoryList);
		rhs.put("maxSize", pagination.getMaxSize());
		rhs.put("count", countList.size());
		rhs.put("maxPage", maxPage);
		rhs.put("currentPage", maxPage);

		rhs.put("info_type", "success");
		rhs.put("info", "create success!");
		return "success";
	}

	public String delete() throws Exception {
		String id = getpara("id");
		Viewhistory viewhistorydata = new Viewhistory();
		viewhistorydata.setId(Long.parseLong(id));
		baseDao.delete(viewhistorydata);
		getPageData();
		rhs.put("info_type", "success");
		rhs.put("info", "delete success!");
		return "success";
	}

	public String update() throws Exception {
		common_update("from Viewhistory v ");
        /*
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
			Viewhistory viewhistory = (Viewhistory) baseDao.loadById("Viewhistory",
					Long.parseLong(id));
			BeanUtils.setProperty(viewhistory, column, columnValue);
			baseDao.update(viewhistory);
		}

		if (false) {
			rhs.put("info_type", "error");
			rhs.put("info", "错误异常代码演示!");
		} else {
			rhs.put("info_type", "success");
			rhs.put("info", "update success!");
		}
		getPageData();
		*/
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
		List viewhistoryList = baseDao.page("from Viewhistory", pagination);
		rhs.put("dataList", viewhistoryList);
		List countList = baseDao.find("from Viewhistory");
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

	public Viewhistory getViewhistoryobject() {
		return viewhistoryobject;
	}

	public void setViewhistoryobject(Viewhistory viewhistoryobject) {
		this.viewhistoryobject = viewhistoryobject;
	}

	
	public List getRootList() {
		if(getpara("id")==""){
			return baseDao.find(" from Organize where parent_id = null");
		}else{
			Organize organize = (Organize) baseDao.loadById("Organize",
					Long.parseLong(getpara("id")));
		    ArrayList rootList=new ArrayList();
		    for (Iterator iterator = organize.getChildOrganizes().iterator(); iterator.hasNext();) {
				Organize o = (Organize) iterator.next();
				rootList.add(o);
				
			}
			 
			 return rootList;
		}
	}	
	
	public String save_organize_pic() throws Exception  {
		String filename=TimeUtil.getTimeStr("yyyy-MM-dd-hh-mm-ss");
		Viewhistory viewhistory = new Viewhistory();
		viewhistory.setFilename(filename);
		viewhistory.setName(getpara("name"));
		baseDao.create(viewhistory);
		
		SimpleHash root=new SimpleHash();
		root.put("rootname",getpara("name"));
		
		root.put("organizeRootList", getRootList());
		StringbyPerlFreemark.getfilebyFreemark(getWebroot()  + "/file/view/",filename+".ftl", getWebroot()  + "/app/manager/view",getpara("t")+".ftl",root);
		rhs.put("filename", filename);
		return "success";

	}	

}
