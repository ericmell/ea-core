package com.examples.xml;

public class Server {
	String hostname;
	String urlIn;
	String urlOut;
	String tag;

	public Server(String hostname, String urlIn, String urlOut) {
		this.hostname = hostname;
		this.urlIn = urlIn;
		this.urlOut = urlOut;
	}

	public Server() {
		// TODO Auto-generated constructor stub
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getUrlIn() {
		return urlIn;
	}

	public void setUrlIn(String urlIn) {
		this.urlIn = urlIn;
	}

	public String getUrlOut() {
		return urlOut;
	}

	public void setUrlOut(String urlOut) {
		this.urlOut = urlOut;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
