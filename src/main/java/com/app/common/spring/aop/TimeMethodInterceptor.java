package com.app.common.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/*
 打印方法的执行时间
 * */
@Component("timeMethodInterceptor")
public class TimeMethodInterceptor implements MethodInterceptor {
	private static Logger log = LoggerFactory
			.getLogger(TimeMethodInterceptor.class);

	public String getPara(Object[] para) {
		String str_para = "";
		for (int i = 0; i < para.length; i++) {
			str_para = str_para + "\"" + para[i].toString() + "\",";
		}
		return str_para;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = null;
		long a = System.currentTimeMillis();
		result = invocation.proceed();
		float time = (System.currentTimeMillis() - a) / 1000f;
		String info_may = invocation.getThis().getClass().getName() + "."
				+ invocation.getMethod().getName() + "("
				+ getPara(invocation.getArguments()) + ")耗时" + time + " 秒 ";
		// if (time > 0.5)
		log.warn("耗时" + info_may);
		return result;
	}
}