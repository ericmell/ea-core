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

package com.utils.string;

/**
 * <strong>Internal class, do not use directly.</strong>
 * 
 * String utility methods.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class StringProcess {

	/**
	 * Get hex string from byte array
	 */
	public final static String toHexString(byte[] res) {
		StringBuilder sb = new StringBuilder(res.length << 1);
		for (int i = 0; i < res.length; i++) {
			String digit = Integer.toHexString(0xFF & res[i]);
			if (digit.length() == 1) {
				sb.append('0');
			}
			sb.append(digit);
		}
		return sb.toString().toUpperCase();
	}

	public final static String cleanString(String srcstring) {
		if (srcstring != null) {
			return srcstring.replaceAll("\\t", "").replaceAll("\\n", "")
					.replaceAll("\\r", "").replaceAll("\"", "'");
			// return organizedescription.replaceAll("\\t",
			// "").replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\"",
			// "'");
		}
		return srcstring;
	}

}
