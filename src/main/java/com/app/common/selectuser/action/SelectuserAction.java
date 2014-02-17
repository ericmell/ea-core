package com.app.common.selectuser.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.common.selectuser.model.OftenSelectedUsers;

@Component("selectuserAction")
@Scope("prototype")
public class SelectuserAction extends BaseEaAction {
	
	private static Logger log = LoggerFactory.getLogger(SelectuserAction.class);
	
	public String oftenselectlist(){
		List list = baseDao.find("from OftenSelectedUsers where userId=" + getCurrentUser().getId());
		rhs.put("dataList", list);
		log.debug(list.toString());
		return "success";
	}
	public String select_userlist_by_organize() throws Exception {
		rhs.put("organizeRootList",infEa.getOrganizeRootNods());	
	return "success";	
		
		
	}	
	
	public String users_for_select() throws Exception {
		String term = getpara("term");
		log.debug("users_for_select() : " + term);
		
		String sql = "from User where name like ? or account like ?";
		Object[] values = new Object[]{'%' + term + '%', '%' + term + '%'};
		List dataList = infEa.getbaseDao().find(sql, values);
		
		rhs.put("dataList", dataList);
		return "success";
	}	
	
	public String saveoftenselect(){
		log.debug("save()");
		
		String selectedUserAccount = getpara("selectedUserAccount");
		String selectedUserName = getpara("selectedUserName");
		
		List list = baseDao.find("from OftenSelectedUsers where selectedUserAccount='" + selectedUserAccount + "' and userId=" + getCurrentUser().getId());
		if(list != null && list.size() != 0){
			OftenSelectedUsers o = (OftenSelectedUsers)list.get(0);
			o.setCount(o.getCount() + 1);
			baseDao.update(o);
		}else{
			OftenSelectedUsers o = new OftenSelectedUsers();
			o.setUserId(getCurrentUser().getId());
			o.setUserAccount(getCurrentUser().getAccount());
			o.setSelectedUserAccount(selectedUserAccount);
			o.setSelectedUserName(selectedUserName);
			o.setCount(1);
			baseDao.create(o);
		}
		
		return null;
	}

}
