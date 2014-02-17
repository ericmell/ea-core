package com.utils.svn;

import java.util.Properties;

import com.utils.config.Appconfig;

/**
 * 本工具类必须使用 appconfig.txt配置文件
 * */
public class SvnUtils {
	public static void gen_svn_apache_account(String username, String passwd)
			throws Exception {
		Properties prop = Appconfig.configuration;
		String apache_home = Appconfig.getValue("apache_home", "apache路径没有定义");
		String cmd = apache_home + "bin/htpasswd.exe -b -m " + apache_home
				+ "passwd.txt  " + username + "  " + passwd;
		System.out.println(cmd);
		Runtime.getRuntime().exec(cmd);
	}

	/**
	 * 常见目录 如果通过file的调用，是不需要用 帐号就可以直接操作了
	 * */
	public static void create_svn_dir(String svnpath) throws Exception {
		String svnUrl = "file:///"
				+ Appconfig.getValue("apache_home", "apache_home没定义")
				+ "svn-repo/" + Appconfig.getValue("svn_repo_name", "");
		String account = Appconfig.getValue("svn_admin_username",
				" svnadmin没有定义");
		String passwd = Appconfig.getValue("svn_admin_passwd", "");
		SvnApi svn = SvnApi.getInstance(svnUrl, account, passwd);
		svn.createDirectories(svnpath);

	}

	public static void delete_svn_dir(String svnpath) throws Exception {
		String svnUrl = "file:///"
				+ Appconfig.getValue("apache_home", "apache_home没定义")
				+ "svn-repo/" + Appconfig.getValue("svn_repo_name", "");
		String account = Appconfig.getValue("svn_admin_username",
				" svnadmin没有定义");
		String passwd = Appconfig.getValue("svn_admin_passwd", "");
		SvnApi svn = SvnApi.getInstance(svnUrl, account, passwd);
		svn.delete(svnUrl + svnpath);

	}

	public static void main(String[] args) throws Exception {
		// create_svn_dir("/project/test9");
		delete_svn_dir("/project/test9");
	}

}
