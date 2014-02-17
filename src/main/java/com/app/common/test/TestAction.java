package com.app.common.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;

@Component("testAction")
@SuppressWarnings("rawtypes")
public class TestAction extends BaseEaAction {
	
	public String menu_para() throws Exception {
		rhs.put("system_para_map", 	infEa.getParaMap());
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}
   
	public String date() throws Exception {
		
		return "success";
	}
	   
	
	
   public String dialog() throws Exception {
		
		return "success";
	}

	public String java() throws Exception {
		
		return "success";
	}
	
	public String drag() throws Exception {
		
		return "success";
	}	
	public String pic() throws Exception {
		
		return "success";
	}	
	
	public String select_user() throws Exception {
		
		return "success";
	}
	
    public String push_iframe() throws Exception {
    	rhs.put("runtime", Runtime.getRuntime());
    	return "success";
	}
    public String push_ajax() throws Exception {
    	rhs.put("runtime", Runtime.getRuntime());
    	return "success";
	}
    public String push() throws Exception {
    	rhs.put("runtime", Runtime.getRuntime());
    	return "success";
	}	
	public String upload() throws Exception {
		ServletActionContext.getRequest().getRealPath("");
		System.out.println("webroot路径"+ServletActionContext.getRequest().getRealPath(""));
		return "success";
	}
	
	public String day_grid() throws Exception {
		List<String> dayList = new ArrayList<String>();
		Date currentDate = new Date();
		Calendar c=Calendar.getInstance();   
		DateFormat df=new SimpleDateFormat("MM-dd");
		DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
		String daysStr = getpara("showDays");
		String dateStr = getpara("startDate");
		int days = 0;
		if (!"".equals(daysStr))
			days = Integer.parseInt(daysStr);
		else 
			days = 10;
		if (!"".equals(dateStr))
			currentDate = df1.parse(dateStr);
		for (int n = 1; n <= days; n++) {
			c.setTime(currentDate);   
			c.add(Calendar.DATE,n);
			Date date=c.getTime();   
			String s=df.format(date); 
			dayList.add(s);
		}
		rhs.put("startDate", "".equals(dateStr) ? df1.format(currentDate) : dateStr);
		rhs.put("showDays", days);
		rhs.put("dayList", dayList);
		return "success";
	}
	
}
