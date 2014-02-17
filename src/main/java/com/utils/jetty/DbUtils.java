package com.utils.jetty;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.utils.file.FileProcessor;

public class DbUtils {
	public static void copydb(String filename) throws Exception {
		Properties p = System.getProperties();
		String filepath = p.getProperty("user.home") + "/" + filename;
		if (FileProcessor.checkFileExit(filepath)) {
		} else {
			FileUtils
					.copyFile(new File("./db/" + filename), new File(filepath));
		}
	}

}
