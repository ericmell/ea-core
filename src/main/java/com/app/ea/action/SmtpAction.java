package com.app.ea.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.common.spring.ssh.page.Pagination;
import com.app.ea.model.Smtp;


@Component("smtpAction")
@SuppressWarnings("rawtypes")
@Scope("prototype")
public class SmtpAction extends BaseEaAction {
	private static Logger log = LoggerFactory.getLogger(SmtpAction.class);
	private static String hsql_all = "from Smtp";

	public String menu_smtp() throws Exception {
		System.out.println();
		System.out.println("进入了");
		this.getPageData(hsql_all);
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}

	public String create() throws Exception {
		Smtp smtp = new Smtp();
		
		baseDao.create(smtp);
		getPageData(hsql_all);
		List countList = baseDao.find(hsql_all);
		int maxPage = countList.size() % pagination.getMaxSize() > 0 ? countList
				.size() / pagination.getMaxSize() + 1
				: countList.size() / pagination.getMaxSize();
		pagination.setCurrentPage(maxPage);
		List smtpList = baseDao.page("from Smtp", pagination);
		rhs.put("dataList", smtpList);
		rhs.put("maxSize", pagination.getMaxSize());
		rhs.put("count", countList.size());
		rhs.put("maxPage", maxPage);
		rhs.put("currentPage", maxPage);
		rhs.put("info_type", "success");
		rhs.put("info", "create success!");
		return "success";
	}
	public String change_rank() throws Exception {
		common_change_rank(); 
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		getPageData(hsql_all);
		return "success";
	}
	public String delete() throws Exception {
		String id = getpara("id");
		Smtp smtpdata = new Smtp();
		smtpdata.setId(Long.parseLong(id));
		baseDao.delete(smtpdata);
		getPageData(hsql_all);
		rhs.put("info_type", "success");
		rhs.put("info", "delete success!");
		return "success";
	}
	


	public String update() throws Exception {
		common_update(hsql_all);
		return "success";
	}   
	
     //修改每页显示的个数
	public String change_page_number() throws Exception {
		putSessionValue("maxSize", getpara("maxSize"));
		getPageData(hsql_all);
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}
     //翻页
	public String ajax_page_data() throws Exception {
		getPageData(hsql_all);
		rhs.put("info_type", "success");
		rhs.put("info", "success!");
		return "success";
	}

}
