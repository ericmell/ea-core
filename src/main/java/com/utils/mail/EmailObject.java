package com.utils.mail;

public class EmailObject {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String title;
	private String smtphost;
	private String port = "25";

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	private String smtphostusername;
	private String smtphostpassword;
	private String formsender;

	public String getFormsender() {
		return formsender;
	}

	public void setFormsender(String formsender) {
		this.formsender = formsender;
	}

	public String getSmtphost() {
		return smtphost;
	}

	public void setSmtphost(String smtphost) {
		this.smtphost = smtphost;
	}

	public String getSmtphostpassword() {
		return smtphostpassword;
	}

	public void setSmtphostpassword(String smtphostpassword) {
		this.smtphostpassword = smtphostpassword;
	}

	public String getSmtphostusername() {
		return smtphostusername;
	}

	public void setSmtphostusername(String smtphostusername) {
		this.smtphostusername = smtphostusername;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
