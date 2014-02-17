package com.utils.path;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.utils.file.PathUtils;


/*在这里希望可以设置一些初始化路径，比方webroot路径和配置文件的路径
 * 
 * Java取得当前类的路径 
 一此不安全的做法：
 1. new File(path),这个方法的路径到底在那里取决于调用java命令的起始位置定义在哪里,
 tomcat/bin下面的catalina.bat调用了java,所以在tomcat下相对起始位置是tomcat/bin,但是eclipse启动时，起始位置 是eclipse的项目路径。 

 2.类.class.getClassLoader().getResource("").getPath()
 如果使用了此方法，这把决定权交给了类加载器，例如tomcat的类加载是非委托机制的，而weblogic的类加载是委托的。大部分情况下是安全的。

 3.类.class.getResource("")
 这是个好方法，但局限性在于如果类文件在jar中的话，那么在打jar包时需要将文件夹一起打进去，否则会返回null,jar文件实际上就是zip文件，zip文件中：文件就是文件，
 文件夹是文件夹，不是关联在一起的，很多开源的jar包就没有把目录打进去只打了classes文件，虽然你能年到目录层次结构，但是调用
 类.class.getResource("")会返回null.因为文件的目录结构和文件夹本身是两回事。 

 */
public class SystemInit implements ServletContextListener {
	private static Log log = LogFactory.getLog(SystemInit.class);
	public static String WEB_ROOT;
	public static String ClASS_ROOT;
	public static String HOME_ROOT;
	public static String system_config_path;
	public static SystemConfig SYSTEM_CONFIG;
	public static  HashMap  SYSTEM_PARA_MAP;

	public void contextInitialized(ServletContextEvent sce) {
		try {
			WEB_ROOT = PathUtils.getWebrootPath(SystemInit.class);
			ClASS_ROOT = PathUtils.getClassPath(SystemInit.class);
			HOME_ROOT = PathUtils.getHomePath(SystemInit.class);
			system_config_path=ClASS_ROOT+"/system-config.xml";
			this.SYSTEM_CONFIG=readSystemConfigXml(system_config_path);
			System.out.println("web root path:" + SystemInit.WEB_ROOT);
			System.out.println("class root path:" + SystemInit.ClASS_ROOT);
			System.out.println("home path:" + SystemInit.HOME_ROOT);
			System.out.println("edit model:" + this.SYSTEM_CONFIG.woEditModel);

		} catch (Exception e) {
			log.error(" SystemData init error!", e);
		}
	}

	public void contextDestroyed(ServletContextEvent ce) {
	}

	public static void saveSystemConfigXml(String filepath,
			SystemConfig systemConfig) throws FileNotFoundException {
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream(filepath)));
		e.writeObject(systemConfig);
		e.close();
	}

	public static SystemConfig readSystemConfigXml(String filepath)
			throws Exception {
		XMLDecoder xmlDecoder;
		xmlDecoder = new XMLDecoder(new BufferedInputStream(
				new FileInputStream(filepath)));
		SystemConfig systemConfig = (SystemConfig) xmlDecoder.readObject();
		xmlDecoder.close();

		return systemConfig;
	}

}
