package com.examples;

import com.utils.hardinfo.HardInfo;

public class HardInfoSample {
	/**
	 * 得到系统中所有属性的名称
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(HardInfo.getHostAddresses()[0]);
		System.out.println(HardInfo.findNonLocalhostIp().toString());
	}

}