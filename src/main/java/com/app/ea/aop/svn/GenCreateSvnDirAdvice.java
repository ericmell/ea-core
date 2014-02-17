package com.app.ea.aop.svn;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Component;

import com.app.ea.model.Organize;
import com.app.ea.model.Organizegroup;
import com.utils.svn.SvnUtils;

@Aspect
@Component("genCreateSvnDirAdvice")
public class GenCreateSvnDirAdvice implements AfterReturningAdvice {
	private final Logger log = LoggerFactory
			.getLogger(GenCreateSvnDirAdvice.class);

	public String getPath(Organize organize) {
		String path = "";
		while (organize.getParentModel() != null) {
			path = "/" + organize.getAlias() + path;
			organize = organize.getParentModel();
		}
		return path;
	}

	public void afterReturning(Object returnValue, Method method,
			Object[] args, Object target) throws Throwable {
		Organize organize = (Organize) args[0];
		Organizegroup organizegroup = (Organizegroup) args[1];
		if (organizegroup.getAlias().equals("svn"))
			;
		String path = "";

		try {
			if (organizegroup.getOrganizes().contains(organize)) {
				System.out.println("建立" + path);

				while (organize.getParentModel() != null) {
					path = "/" + organize.getAlias() + path;
					organize = organize.getParentModel();
				}

				SvnUtils.create_svn_dir(path);
			} else {
				while (organize.getParentModel() != null) {
					path = "/" + organize.getAlias() + path;
					organize = organize.getParentModel();
				}
				SvnUtils.delete_svn_dir(path);

				System.out.println("删除" + path);
			}
		} catch (Exception e) {
			System.out.println("svn 操作失败");
			// e.printStackTrace();
		}

	}

}
