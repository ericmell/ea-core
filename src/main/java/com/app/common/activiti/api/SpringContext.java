package com.app.common.activiti.api;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 此类可以取得Spring的上下文.
 * Spring 使new方法创建的对象可以引用spring管理的bean.
 * 2007-10-18 上午11:12:33
 * @author chenlb
 */
@Component("springContext")
public class SpringContext implements ApplicationContextAware {

    protected static ApplicationContext context;
    
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static Object getBean(String name){
    	return context.getBean(name);
    }
}
