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

package com.utils.path;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Path {

    
    
    public static String   getLocation(Class clazz) {
		try {
			java.net.URL url = clazz.getProtectionDomain().getCodeSource()
					.getLocation();
			String location = url.toString();
			if (location.startsWith("jar")) {
				url = ((java.net.JarURLConnection) url.openConnection())
						.getJarFileURL();
				location = url.toString();
			}

			if (location.startsWith("file")) {
				java.io.File file = new java.io.File(url.getFile());
				return file.getAbsolutePath();
			} else {
				return url.toString();
			}
		} catch (Throwable t) {
		}
		return "an unknown location";
	}
	
    boolean resourceExists(String resource) {
        boolean found;
        InputStream instream=this.getClass().getResourceAsStream(resource);
        found=instream!=null;
        if(instream!=null) {
            try {
                instream.close();
            } catch (IOException e) {
            }
        }
        return found;
    }
    Class classExists(String classname) {
        try {
            return Class.forName(classname);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    public static void main(String[] args) throws Exception {
    	//求类的路径
		System.out.println( getLocation( Class.forName("org.springframework.orm.hibernate3.support.OpenSessionInViewFilter")));
		System.out.println( getLocation( Class.forName("org.hibernate.dialect.H2Dialect")));
		System.out.println( getLocation( Class.forName("org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter")));
		System.out.println( getLocation( Class.forName("freemarker.ext.servlet.FreemarkerServlet")));

		System.out.println( getLocation( Class.forName("org.h2.Driver")));
		
	}
}
