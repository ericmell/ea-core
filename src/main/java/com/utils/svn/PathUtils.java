/*
 * Created on 2006-5-18
 */
package com.utils.svn;

/**
 * @author Huang W.Q.
 */
public class PathUtils {
	public static final String UNAUTHENTICATION = "unauthorized.SVNDownload";

	/**
	 * a : /abc b : /def a + b = /abc/def
	 * 
	 * @param a
	 * @param b
	 * @return
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
	 * ������һ���ַ���/������ɾ��
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

}
