package com.app.common.edit.action;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.common.spring.ssh.model.BaseModel;

@Component("editAction")
public class editAction extends BaseEaAction {

	private final Logger log = LoggerFactory.getLogger(editAction.class);

	public String edit_bean_property() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		System.out.println("");
		String op = getpara("op");
		String id = getpara("id");
		String objectname = getpara("objectname"); // 对象名'
		String propertyname = getpara("propertyname"); // 对象名'

		String content = java.net.URLDecoder.decode(getpara("content"));
		BaseModel object = (BaseModel) baseDao.loadById(objectname,
				Long.parseLong(id));
		if (op.equals("r")) {
			content = BeanUtils.getProperty(object, propertyname);
		}

		if (op.equals("w")) {
			try {
				BeanUtils.setProperty(object, propertyname, content);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			baseDao.update((BaseModel) object);
		}
		rhs.put("content", content);

		rhs.put("objectname", objectname);
		rhs.put("propertyname", propertyname);
		rhs.put("id", id);
		rhs.put("op", op);
		return "success";
	}

}
