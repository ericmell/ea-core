package com.examples.xml;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 xml用来保存数据量少的配置文件信息

 如何封装出一个界面，对XML进行添加删除，会降低系统数据对象复杂度

 步骤：
 1. main 主页面，显示所有服务器list
 2.添加新服务   读取XML，然后添加一个新的server,再生成配置文件
 3.修改配置信息，根据list id,update list到配置文件

 */
public class XmlDataGen {
	static Logger log = LoggerFactory.getLogger(XmlDataGen.class);
	public ArrayList serverList = new ArrayList();

	public ArrayList getServerList() {
		return serverList;
	}

	public void setServerList(ArrayList serverList) {
		this.serverList = serverList;
	}

	public static void main(String[] args) throws FileNotFoundException {
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream("server-list.xml")));
		XmlDataGen xmlDataGen = new XmlDataGen();
		Server ServerProperty = new Server();
		xmlDataGen.getServerList().add(
				new Server("windows", "http://129.33.33.2:88",
						"http://129.33.31.2:88"));
		System.out.println(xmlDataGen.getServerList().size());
		e.writeObject(xmlDataGen);
		e.close();
	}

}
