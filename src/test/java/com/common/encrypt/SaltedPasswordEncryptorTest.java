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

package com.common.encrypt;

import com.utils.encrypt.PasswordEncryptor;
import com.utils.encrypt.impl.SaltedPasswordEncryptor;

/**
 * 哈希值最安全。所以我们一般用哈希值存储密码，常用的计算哈希值的方法是 MD5 和 SHA-1， 本文以
 * MD5为例。为哈希值增加一个随机盐可以增加密码的安全性。 增加简单密码的安全性。 但如果加上随机 盐就不同了，假如我们的随机盐是 6 个随机字符，
 * 就比如是：xids#3，存储的就是 MD5("xids#3" + "1")，随机盐被明文存储在另一个字段时，
 * 到时候判断密码时再把密码和随机盐混合起来计算。而要反查 MD5("xids#3" + "1") 就不
 * 件容易的事了。让相同的密码具有不同的结果由于有随机盐参与了 MD5 运算，所以相同的密码， 由于随机盐不同，哈希值就不同了。
 * 
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>*
 */
public class SaltedPasswordEncryptorTest extends ClearTextPasswordEncryptorTest {

	@Override
	protected PasswordEncryptor createPasswordEncryptor() {
		return new SaltedPasswordEncryptor();
	}

	public void testMatches() {
		PasswordEncryptor encryptor = createPasswordEncryptor();

		assertTrue(encryptor.matches("foo",
				"71288966:F7B097C7BB2D02B8C2ACCE8A17284715"));

		// check lower case
		assertTrue(encryptor.matches("foo",
				"71288966:f7b097C7BB2D02B8C2ACCE8A17284715"));

		assertFalse(encryptor.matches("foo", "bar:bar"));
	}
}
