package com.utils.mail;

public class Auth extends javax.mail.Authenticator {
	private String user, pwd;

	public Auth(String user, String pwd) {
		this.user = user;
		this.pwd = pwd;
	}

	protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
		return new javax.mail.PasswordAuthentication(this.user, this.pwd);
	}
}
