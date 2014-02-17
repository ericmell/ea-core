package com.app.common.spring.ssh.action;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.common.spring.ssh.page.Pagination;
import com.opensymphony.util.BeanUtils;
import com.opensymphony.xwork2.ActionContext;

import freemarker.ext.beans.BeansWrapper;

/*一些通用的方法,对所有项目的action共享，没有特别的类对象*/
@Scope("prototype")
public class BaseAction {
	private final Logger log = LoggerFactory.getLogger(BaseAction.class);
	public HashMap<String, Object> rhs = new HashMap<String, Object>();
	public BaseDao baseDao;
	public Pagination pagination;
	public Object javacall = BeansWrapper.getDefaultInstance()
			.getStaticModels();
	public File file;
	public String fileFileName;
	public String fileContentType;

	public HttpServletRequest getRequest() {
		String url = ServletActionContext.getRequest().getServletPath() + "?"
				+ ServletActionContext.getRequest().getQueryString();
		return ServletActionContext.getRequest();
	}

	public HashMap<String, Object> getRhs() {
		return rhs;
	}

	public void setRhs(HashMap<String, Object> rhs) {
		this.rhs = rhs;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@Resource(name = "pagination")
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public String getpara(String key) {
		if (ServletActionContext.getRequest().getParameter(key) == null)
			return "";
		else {
			return ServletActionContext.getRequest().getParameter(key);
		}

	}

	public String getWebroot() {
		return ServletActionContext.getRequest().getRealPath("/"); 
	}
	
	
	
	public String getnumberpara(String key) {
		if (ServletActionContext.getRequest().getParameter(key) == null)
			return "0";
		else {
			if (ServletActionContext.getRequest().getParameter(key).equals("")){
				return "0";
			}
			return ServletActionContext.getRequest().getParameter(key).trim();
		}

	}
	public Object getSessionValue(String key) {
		Map<String, Object> sessionMap = ActionContext.getContext()
				.getSession();
		return sessionMap.get(key);

	}

	public Object putSessionValue(String key, Object o) {
		Map<String, Object> sessionMap = ActionContext.getContext()
				.getSession();
		return sessionMap.put(key, o);

	}

	public void common_update() throws Exception {
		String id = getpara("id");
		String beanname = getpara("beanname");
		String column = getpara("column");
		String columnValue = java.net.URLDecoder.decode(getpara("columnValue"));
		/*
		 * searchtext=java.net.URLDecoder.decode(searchtext,"UTF-8");
		 * 另外还有一种方法是JavaScript进行一次编码，后台java处理时换种想法就好了： java代码：String s = new
		 * String(request.getParameter("name").getBytes("ISO8859-1"), "UTF-8");
		 */

		BaseModel baseModel = (BaseModel) baseDao.loadById(beanname,
				Long.parseLong(id));
		try {
			BeanUtils.setValue(baseModel, column, columnValue);
		} catch (Exception e) {
			log.error("属性修改有问题" + column + "=" + columnValue);
			e.printStackTrace();
		}
		baseDao.update(baseModel);
	}

	public void common_change_level() throws Exception {
		String beanname = getpara("beanname");
		BaseModel baseModel = (BaseModel) baseDao.loadById(beanname,
				Long.parseLong(getpara("id")));
		Object op = BeanUtils.getValue(baseModel, "parentModel");
		if (getpara("parentId") == "") {
			if (op != null) {
				Object opp = BeanUtils.getValue(op, "parentModel");
				BeanUtils.setValue(baseModel, "parentModel", opp);
			}
		} else {
			BaseModel baseModelParent = (BaseModel) baseDao.loadById(beanname,
					Long.parseLong(getpara("parentId")));
			BeanUtils.setValue(baseModel, "parentModel", baseModelParent);
		}
		baseDao.update(baseModel);
	}

	
	
	
	public String common_update(String sql) throws Exception {
		String id = getpara("id");
		String column = getpara("column");
		// String columnValue = getpara("columnValue");
		String columnValue = java.net.URLDecoder.decode(getpara("columnValue"));
		if (column.equals("maxSize")) {
			int pageNum = Integer.parseInt(columnValue);
			if (pageNum != pagination.getMaxSize() && pageNum > 0) {
				pagination.setMaxSize(pageNum);
			}
		} else {
			BaseModel modle = (BaseModel) baseDao.loadById(getpara("beanname"),
					Long.parseLong(id));
			BeanUtils.setValue(modle, column, columnValue);
			baseDao.update(modle);
		}

		
		rhs.put("info_type", "success");
		rhs.put("info", "update success!");
		getPageData(sql);
		return "success";
	}

	public void getPageData(String sql) throws Exception {
		String pageId = getpara("pageId");
		String maxSize = getpara("maxSize");

		if (maxSize == null || "".equals(maxSize)) {
			maxSize = (String) getSessionValue("maxSize");
			if (maxSize == null || "".equals(maxSize))
				maxSize = "20";
		} else {
			pagination.setMaxSize(Integer.parseInt(maxSize));
		}

		if (pageId == null || "".equals(pageId)) {
			pageId = (String) getSessionValue("pageId");
			if (pageId == null || "".equals(pageId))
				pageId = "1";
		}
		putSessionValue("pageId", pageId);
		putSessionValue("maxSize", maxSize);
		pagination.setCurrentPage(Integer.parseInt(pageId));
		List tpltb1List = baseDao.page(sql, pagination);
		rhs.put("dataList", tpltb1List);
		List countList = baseDao.find(sql);
		rhs.put("maxSize", maxSize);
		rhs.put("count", countList.size());
		rhs.put("maxPage",
				countList.size() % pagination.getMaxSize() > 0 ? countList
						.size() / pagination.getMaxSize() + 1 : countList
						.size() / pagination.getMaxSize());
		rhs.put("currentPage", Integer.parseInt(pageId));
		if (getpara("sortName").equals("")) {
			//rhs.put("sortName", "id");
			putSessionValue("sortName", "id");
		} else {
			//rhs.put("sortName", getpara("sortName"));
			putSessionValue("sortName",  getpara("sortName"));
		}
	
	}

	public void common_change_rank() throws Exception {
		String id_from = getpara("id_from");
		String id_to = getpara("id_to");
		String beanname = getpara("beanname");
		Object object_from = baseDao
				.loadById(beanname, Long.parseLong(id_from));
		Object object_to = baseDao.loadById(beanname, Long.parseLong(id_to));
		Long oderid_from = 0L;
		Long oderid_to = 0L;
		try {
			oderid_from = (Long) BeanUtils.getValue(object_from, "sortNob");
		} catch (Exception e) {
			oderid_from = (Long) BeanUtils.getValue(object_from, "id");
			e.printStackTrace();
		}

		try {
			oderid_to = (Long) BeanUtils.getValue(object_to, "sortNob");
		} catch (Exception e) {
			oderid_to = (Long) BeanUtils.getValue(object_to, "id");
			e.printStackTrace();
		}

		BeanUtils.setValue(object_from, "sortNob", oderid_to);
		BeanUtils.setValue(object_to, "sortNob", oderid_from);
		baseDao.update(object_from);
		baseDao.update(object_to);

	}

	public List common_get_tree_root(String beanname) throws Exception {
		List olist = baseDao.find(" from " + beanname
				+ " where parent_id = null");
		getExptList("Organize");
		common_get_extp("Organize");
	
		return olist;

	}

	public void common_get_extp(String beanname){
		List  valueList= (List) baseDao.find( "from Extv ev where ev.modelname='"+beanname+"'");
		HashMap extvmap=new HashMap();
		for (Iterator iterator = valueList.iterator(); iterator.hasNext();) {
			Object extv = (Object) iterator.next();
			extvmap.put(BeanUtils.getValue(extv, "extpalias")+"_"+BeanUtils.getValue(extv, "modelid"), extv);
		}
		rhs.put("extvmap", extvmap);
	
		
	}
	
	public void common_del_tree_node() throws Exception {
		String id = getpara("id");
		String beanname = getpara("beanname");
		BaseModel baseModel = (BaseModel) baseDao.loadById(beanname,
				Long.parseLong(id));
		BeanUtils.setValue(baseModel, "parentModel", null);
		BeanUtils.setValue(baseModel, "organizegroups", null);
		
		baseDao.update(baseModel);
		baseDao.delete(baseModel);

	}
	
	
	
	public void getExptList(String modelname){
		rhs.put("extpList",  baseDao.find("from Extp ep  where ep.modelname='"+modelname+"'"));
	}
	public String edit_bean_property() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String op = getpara("op");
		String id = getpara("id");
		String objectname = getpara("objectname"); // 对象名'
		String propertyname = getpara("propertyname"); // 对象名'
		String content = getpara("content");
		BaseModel object = (BaseModel) baseDao.loadById(objectname,
				Long.parseLong(id));
		if (op.equals("r")) {
			content = BeanUtils.getValue(object, propertyname).toString();
		}

		if (op.equals("w")) {
			try {
				BeanUtils.setValue(object, propertyname, content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			baseDao.update((BaseModel) object);
		}
		rhs.put("content", content);
		rhs.put("object", object);
		rhs.put("objectname", objectname);
		rhs.put("propertyname", propertyname);
		rhs.put("id", id);
		rhs.put("op", op);
		return "success";
	}

	public String getNewfilename(String fileFileName) {
		String newfilename = new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new Date());
		String houzhui = StringUtils.substringAfterLast(file.getName(), ".");
		System.out.println(fileFileName + "文件名:" + fileContentType);
		newfilename = newfilename
				+ fileFileName.substring(fileFileName.lastIndexOf("."));
		return newfilename;
	}

	/**
	 * 获取查询分页的sql语句
	 * 
	 * @param baseSql
	 *            eg. select * from user where 1=1
	 * @return
	 * @throws Exception
	 */
	public String getSearchSql(String baseSql) throws Exception {
		String search = getpara("search");
		String sql = null;
		if ("search".equals(search)) {
			HttpServletRequest request = ServletActionContext.getRequest();
			String name;
			String nameValue;
			String op;
			StringBuffer where = new StringBuffer("");
			Enumeration pNames = request.getParameterNames();
			while (pNames.hasMoreElements()) {
				name = pNames.nextElement().toString();
				nameValue = request.getParameter(name);
				if (nameValue != null && !"".equals(nameValue)) {
					String[] split = name.split(":");
					if (split.length == 2) {
						name = split[0];
						op = split[1];
						rhs.put(name, nameValue.trim());
						if ("like".equals(op)) {
							nameValue = "'%" + nameValue.trim() + "%'";
						} else {
							nameValue = "'" + nameValue.trim() + "'";
						}

						where.append(" and ").append(name).append(" ")
								.append(op).append(" ").append(nameValue)
								.append(" ");
					}
				}
			}
			sql = baseSql + where.toString();
		} else {
			sql = baseSql;
		}
		return sql;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public Object getJavacall() {
		return javacall;
	}

	public void setJavacall(Object javacall) {
		this.javacall = javacall;
	}

}
