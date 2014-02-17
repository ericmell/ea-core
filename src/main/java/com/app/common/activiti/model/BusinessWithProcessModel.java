package com.app.common.activiti.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.app.common.activiti.api.InfActiviti;
import com.app.common.activiti.api.SpringContext;

public class BusinessWithProcessModel<T> {
	
	private T businessModel;
	private String processInstanceStatus = "";
	
	public BusinessWithProcessModel(T businessModel){
		this.businessModel = businessModel;
		
		InfActiviti infActiviti = (InfActiviti)SpringContext.getBean("impActiviti");
		Class<?> c = businessModel.getClass();
		try {
			String para = "";
			Method method = c.getMethod("getProcessInstanceId", null);
			Object ret = method.invoke(businessModel, null);
			if(ret != null)
				para = (String)ret;
			processInstanceStatus = infActiviti.processInstanceStatus(para);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public String getProcessInstanceStatus(){
		return this.processInstanceStatus;
	}
	
	public T getBusinessModel(){
		return this.businessModel;
	}
}
