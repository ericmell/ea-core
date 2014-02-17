/*
 * Created on 2006-5-18
 */
package com.utils.file;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/**
 * @author tom
 */
public class PathUtils {

	/**
	 * 连接两个路径，例如：<br/>
	 * a : /abc/ <br/>
	 * b : /def <br/>
	 * 返回 /abc/def <br/>
	 * 
	 * @param a
	 *            路径1
	 * @param b
	 *            路径2
	 * @return 连接后的路径
	 */
	public static String combindPath(String a, String b) {
		if (a == null || a.length() == 0) {
			return b;
		}
		if (b == null || b.length() == 0) {
			return a;
		}
		if (a.endsWith("/")) {
			a = a.substring(0, a.length() - 1);
		}
		if (b.startsWith("/")) {
			b = b.substring(1, b.length());
		}
		return a + "/" + b;
	}

	/**
	 * 如果最后一个字符是/，将其删除。
	 * 
	 * @param dirPath
	 * @return
	 */
	public static String removeTailInEnd(String dirPath) {
		if (dirPath != null && dirPath.endsWith("/")) {
			dirPath = dirPath.substring(0, dirPath.length() - 1);
		}
		return dirPath;
	}

	/**
	 * 取得当前类所在的文件
	 * 
	 * @param clazz
	 * @return
	 */
	public static File getClassFile(Class clazz) {
		URL path = clazz.getResource(clazz.getName().substring(
				clazz.getName().lastIndexOf(".") + 1)
				+ ".classs");
		if (path == null) {
			String name = clazz.getName().replaceAll("[.]", "/");
			path = clazz.getResource("/" + name + ".class");
		}
		return new File(path.getFile());
	}

	/**
	 * 得到当前类的路径
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getClassFilePath(Class clazz) {
		try {

			String classpath = java.net.URLDecoder.decode(getClassFile(clazz)
					.getAbsolutePath(), "UTF-8");
			return StringUtils.replace(classpath, "\\", "/");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 取得当前类所在的ClassPath目录，比如tomcat下的classes路径
	 * 
	 * @param clazz
	 * @return
	 */
	public static File getClassPathFile(Class clazz) {
		File file = getClassFile(clazz);
		for (int i = 0, count = clazz.getName().split("[.]").length; i < count; i++)
			file = file.getParentFile();
		if (file.getName().toUpperCase().endsWith(".JAR!")) {
			file = file.getParentFile();
		}
		return file;
	}

	/**
	 * 取得当前类所在的ClassPath路径
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getClassPath(Class clazz) {
		try {
			String classpath = java.net.URLDecoder.decode(
					getClassPathFile(clazz).getAbsolutePath(), "UTF-8");

			return StringUtils.replace(classpath, "\\", "/");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
	}

	public static String getWebrootPath(Class clazz) {
		try {

			String classpath = getClassPath(clazz);
			if (classpath.contains("/target/classes")) {
				return classpath.replaceFirst("target/classes",
						"src/main/webapp");
			}

			if (classpath.contains("/WEB-INF/classes")) {
				return classpath.replaceFirst("/WEB-INF/classes", "");
			}
			return classpath;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
	}

	public static String getHomePath(Class clazz) {
		try {

			String classpath = getClassPath(clazz);
			if (classpath.contains("/target/classes")) {
				return classpath.replaceFirst("target/classes", "");
			}

			if (classpath.contains("/WEB-INF/classes")) {
				return classpath.replaceFirst(
						"src/main/webapp/WEB-INF/classes", "");
			}
			return classpath;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
	}
}
