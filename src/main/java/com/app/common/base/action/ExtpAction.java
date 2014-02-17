package com.app.common.base.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.ea.action.EaAction;
import com.app.ea.model.Extp;

@Component("extpAction")
@Scope("prototype")
public class ExtpAction extends BaseEaAction {
	private static Logger log = LoggerFactory.getLogger(EaAction.class);
	private static String hsql_all = "from Extp ep";

	public void getDataList(){
		rhs.put("modelname", getpara("modelname"));
		rhs.put("dataList",  baseDao.find(hsql_all+ " where ep.modelname='"+getpara("modelname")+"'"));
	}
	public String menu_extp() throws Exception {
		getDataList();
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}

	
	
	public String create() throws Exception {
		Extp extp = new Extp();
		extp.setName("");
		extp.setModelname(getpara("modelname"));
		baseDao.create(extp);
		
		getDataList();
		rhs.put("modelname", getpara("modelname"));
		rhs.put("info_type", "success");
		rhs.put("info", "create success!");
		return "success";
	}
	public String change_rank() throws Exception {
		common_change_rank(); 
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
	
		getDataList();
		return "success";
	}
	public String delete() throws Exception {
		String id = getpara("id");
		Extp extpdata = new Extp();
		extpdata.setId(Long.parseLong(id));
		baseDao.delete(extpdata);
		getDataList();
		rhs.put("info_type", "success");
		rhs.put("info", "delete success!");
		return "success";
	}
	


	public String update() throws Exception {
		common_update(hsql_all);
		getDataList();
		rhs.put("info_type", "success");
		rhs.put("info", "更新 success!");
		return "success";
	}   
	
	 public String update_extp() throws Exception {
		 common_update_extp();
		 rhs.put("info_type", "success");
		 rhs.put("info", "更新成功!");
		 return "success";
	    }


	
}
