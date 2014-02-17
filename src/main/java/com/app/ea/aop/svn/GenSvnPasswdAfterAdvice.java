package com.app.ea.aop.svn;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Component;

import com.utils.svn.SvnUtils;

@Aspect
@Component("genSvnPasswdAfterAdvice")
public class GenSvnPasswdAfterAdvice implements AfterReturningAdvice {
	private final Logger log = LoggerFactory
			.getLogger(GenSvnPasswdAfterAdvice.class);

	public void afterReturning(Object returnValue, Method method,
			Object[] args, Object target) throws Throwable {
		if (args.length > 1)
			SvnUtils.gen_svn_apache_account(args[0].toString(),
					args[1].toString());
		else
			SvnUtils.gen_svn_apache_account(args[0].toString(), "");
		log.info("更用户密码后，更新SVN密码文件");

	}

}
