/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.utils.encrypt.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.utils.encrypt.EncryptUtils;
import com.utils.encrypt.PasswordEncryptor;

/**
 * MD5加密算法 ，这种形式的加密敏感查找的循环攻击
 * 
 * Md5的全称是Message-Digest Algorithm 5（信息-摘要算法），在90年代初由Mit Laboratory For Computer
 * Science和Rsa Data SecurityInc的Ronaldl.rivest开发出来，经md2、md3和md4发展而来。它的作用是让大容量
 * 信息在用数字签名软件签署私人密钥前被 “压缩”成一种保密的格式
 * 。由于md5算法的使用不需要支付任何版权费用的，所以在一般的情况下，md5也不失为一种非常优秀的加密算法，被大量公司和个人广泛使用。
 * 
 * 2004年8月17日的美国加州圣巴巴拉的国际密码学会议（Crypto’2004）上，来自中国山东大学的王小云教授做了破译MD5、HAVAL-128、
 * MD4和RIPEMD算法的报告，公布了MD系列算法的破解结果，MD5破解工程权威网站（http://www.md5crk.com）也因此关闭，
 * 从此宣布MD5加密算法不再是一种安全的加密算法
 * 
 * 
 * 对信息系统或者网站系统来说，MD5算法主要用在用户注册口令的加密，对于普通强度的口令加密，可以通过以下三种方式进行破解：
 * 
 * （1）在线查询密码。一些在线的MD5值查询网站提供MD5密码值的查询，输入MD5密码值后，如果在数据库中存在，那么可以很快获取其密码值。
 * 
 * （2）使用MD5破解工具。网络上有许多针对MD5破解的专用软件，通过设置字典来进行破解。
 * 
 * （3）通过社会工程学来获取或者重新设置用户的口令。
 * 
 * 
 * Password encryptor that hashes the password using MD5. Please note that this
 * form of encryption is sensitive to lookup attacks.
 * 
 * @author lingli
 */
public class Md5PasswordEncryptor implements PasswordEncryptor {

	/**
	 * Hashes the password using MD5
	 */
	public String encrypt(String password) {
		return EncryptUtils.encryptMD5(password);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean matches(String passwordToCheck, String storedPassword) {
		if (storedPassword == null) {
			throw new NullPointerException("storedPassword can not be null");
		}
		if (passwordToCheck == null) {
			throw new NullPointerException("passwordToCheck can not be null");
		}

		return encrypt(passwordToCheck).equalsIgnoreCase(storedPassword);
	}

	/**
	 * MD5加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String getMD5Hash(String plainText) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] hashByte = messageDigest.digest(plainText.getBytes());
		StringBuffer buffer = new StringBuffer(32);
		for (int i = 0; i < hashByte.length; i++) {
			String aHash = Integer.toHexString(0xFF & hashByte[i]);
			if (aHash.length() < 2)
				aHash = aHash + "e";
			buffer.append(aHash);
		}
		return buffer.toString();
	}
}
