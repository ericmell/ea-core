package com.utils.path;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SystemConfig {
	public String woEditModel="all-see-all-edit"; //1.all-see-all-edit  2，all-see-owner-edit，3.ower-see-owner-edit
	public String getWoEditModel() {
		return woEditModel;
	}
	public void setWoEditModel(String woEditModel) {
		this.woEditModel = woEditModel;
	}
    
	public static void main(String[] args) throws FileNotFoundException {
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream("system-config.xml")));
	    SystemConfig  systemConfig=new SystemConfig();
		systemConfig.setWoEditModel("3");
		e.writeObject(systemConfig );
		e.close();

	}
}
