package com.common.encrypt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;

import com.common.spring.junit.SpringConfigTestTemplate;
import com.utils.encrypt.PasswordEncryptor;

public class PasswordEncryptorTest {
	static Logger log = LoggerFactory.getLogger(PasswordEncryptorTest.class);

	/**
	 * 
	 ClearTextPasswordEncryptor明文保存密码 Md5PasswordEncryptor
	 * 加密算法MD5可逆（http://www.cmd5.com/） SaltedPasswordEncryptor 加盐不可逆
	 * 
	 * 
	 * 1.userserice中 在添加用户，保存数据库时，调用该beanupdate密码时，调用该bean
	 * 3.loginaction验证时调用该bean，批量建用户时 还有认证时使用 算法的测试案例在common里面
	 * http://www.cmd5.com/ 是可以破解MD5的
	 * */

	 /**/
	public PasswordEncryptor getPasswordEncryptor(String beanname) {
		XmlBeanFactory factory = SpringConfigTestTemplate
				.createXmlBeanFactory("<bean id=\"passwordEncryptor\" class=\"com.common.encrypt.impl."
						+ beanname + "\"></bean>");
		PasswordEncryptor passwordEncryptor = (PasswordEncryptor) factory
				.getBean("passwordEncryptor");
		return passwordEncryptor;
	}
	

	@Test
	public void testClearTextPasswordEncryptor() {
		PasswordEncryptor passwordEncryptor = (PasswordEncryptor) getPasswordEncryptor("ClearTextPasswordEncryptor");
		assertEquals("123456", passwordEncryptor.encrypt("123456"));
	}

	@Test
	public void testMd5PasswordEncryptor() {
		PasswordEncryptor passwordEncryptor = (PasswordEncryptor) getPasswordEncryptor("Md5PasswordEncryptor");
		assertEquals("E10ADC3949BA59ABBE56E057F20F883E",
				passwordEncryptor.encrypt("123456"));
	}

	@Test
	/**加盐不可逆,所以这里不能相等，在match方法中实现了逆向验证的方法，具体看实际类 */
	public void testSaltedPasswordEncryptor() {
		PasswordEncryptor passwordEncryptor = (PasswordEncryptor) getPasswordEncryptor("SaltedPasswordEncryptor");
		assertNotSame("4061262:A4FF37D63C6794E26CDFCF3C556A9D92",
				passwordEncryptor.encrypt("123456"));

	}
}
