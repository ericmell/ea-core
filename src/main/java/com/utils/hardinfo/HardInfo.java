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

package com.utils.hardinfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 求端口和ip地址 <br>
 * 应用场景
 * <p>
 * 比如一些服务守护程序，需要读取自己的IP和端口号。
 * </p>
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>*
 */
public class HardInfo {

	private static final int DEFAULT_PORT = 12321;

	/**
	 * Attempts to find a free port
	 * 
	 * @throws IOException
	 * 
	 * @throws IOException
	 */
	public static int findFreePort() throws IOException {
		return findFreePort(DEFAULT_PORT);
	}

	/**
	 * 求得一个没有使用的端口
	 * 
	 * @param initPort
	 *            The first port to try, before resolving to brute force
	 *            searching
	 * @throws IOException
	 * 
	 * @throws IOException
	 */
	public static int findFreePort(int initPort) throws IOException {
		int port = -1;
		ServerSocket tmpSocket = null;
		// first try the default port
		try {
			tmpSocket = new ServerSocket(initPort);

			port = initPort;

			System.out.println("Using default port: " + port);
		} catch (IOException e) {
			System.out.println("Failed to use specified port");
			// didn't work, try to find one dynamically
			try {
				int attempts = 0;

				while (port < 1024 && attempts < 2000) {
					attempts++;

					tmpSocket = new ServerSocket(0);

					port = tmpSocket.getLocalPort();
				}

			} catch (IOException e1) {
				throw new IOException(
						"Failed to find a port to use for testing: "
								+ e1.getMessage());
			}
		} finally {
			if (tmpSocket != null) {
				try {
					tmpSocket.close();
				} catch (IOException e) {
					// ignore
				}
				tmpSocket = null;
			}
		}

		return port;
	}

	public static String[] getHostAddresses() throws Exception {
		Enumeration<NetworkInterface> nifs = NetworkInterface
				.getNetworkInterfaces();

		List<String> hostIps = new ArrayList<String>();
		while (nifs.hasMoreElements()) {
			NetworkInterface nif = (NetworkInterface) nifs.nextElement();
			Enumeration<InetAddress> ips = nif.getInetAddresses();

			while (ips.hasMoreElements()) {
				InetAddress ip = (InetAddress) ips.nextElement();
				if (ip instanceof java.net.Inet4Address) {
					hostIps.add(ip.getHostAddress());
				} // IPv6 not tested
			}
		}

		return hostIps.toArray(new String[0]);
	}

	/**
	 * 求非本地地址的ip
	 * */
	public static InetAddress findNonLocalhostIp() throws Exception {
		Enumeration<NetworkInterface> nifs = NetworkInterface
				.getNetworkInterfaces();

		while (nifs.hasMoreElements()) {
			NetworkInterface nif = (NetworkInterface) nifs.nextElement();
			Enumeration<InetAddress> ips = nif.getInetAddresses();

			while (ips.hasMoreElements()) {
				InetAddress ip = (InetAddress) ips.nextElement();
				if (ip instanceof java.net.Inet4Address
						&& !ip.isLoopbackAddress()) {
					return ip;
				} // IPv6 not tested
			}
		}

		return null;
	}

}
