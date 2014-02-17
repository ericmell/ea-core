package com.utils.mail;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class EmailConfig {
	public ArrayList emailList = new ArrayList();
	private String id;
	private String mailAnsong;
	private String mailError;
	private String validateImage;

	public String getValidateImage() {
		return validateImage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setValidateImage(String validateImage) {
		this.validateImage = validateImage;
	}

	public ArrayList getEmailList() {
		return emailList;
	}

	public void setEmailList(ArrayList emailList) {
		this.emailList = emailList;
	}

	/**/
	public static void main(String[] args) throws FileNotFoundException {
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream("email_config.xml")));
		EmailConfig emailConfig = new EmailConfig();
		emailConfig.setMailAnsong("freemanlingli@gmail.com");
		emailConfig.setMailError("freemanlingli@gmail.com");

		EmailObject emailObject = new EmailObject();
		EmailObject emailObject2 = new EmailObject();
		emailObject.setSmtphost("smtp.163.com");
		emailObject.setSmtphostusername("lingli");
		emailObject.setSmtphostpassword("aaa");
		emailObject.setFormsender("lingli@snpsoft.com");
		emailObject.setTitle("网容邮箱");

		emailObject2.setSmtphost("smtp.163.com");
		emailObject2.setSmtphostusername("lingli");
		emailObject2.setSmtphostpassword("aaa");
		emailObject2.setFormsender("lingli@snpsoft.com");
		emailObject2.setTitle("网容邮箱");

		emailConfig.emailList.add(emailObject);
		emailConfig.emailList.add(emailObject2);

		e.writeObject(emailConfig);
		e.close();

	}

	public String getMailAnsong() {
		return mailAnsong;
	}

	public void setMailAnsong(String mailAnsong) {
		this.mailAnsong = mailAnsong;
	}

	public String getMailError() {
		return mailError;
	}

	public void setMailError(String mailError) {
		this.mailError = mailError;
	}

}
