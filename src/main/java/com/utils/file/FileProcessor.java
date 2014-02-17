package com.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.oro.io.GlobFilenameFilter;
import org.apache.oro.text.GlobCompiler;

/**
 * 文件处理工具类 。<br>
 * 注意扩展：org.apache.commons.io.FileUtils
 * */
public class FileProcessor {
	/**
	 * 读取网络文件，保存到本地路径
	 * */
	public static boolean getNetfile(String url, String savepath)
			throws Exception {
		try {
			URL address;
			address = new URL(url);
			byte[] data = new byte[2024];
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					address.openStream()); // 获取输入流
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(savepath));
			int c = 0;
			while ((c = bufferedInputStream.read(data)) != -1) {// 这里才能保证不会写入多余的缓存字节数
				bufferedOutputStream.write(data, 0, c);
			}
			if (c == 0)
				return false;// 当没读到数据时
			bufferedOutputStream.flush();
			bufferedInputStream.close();
			bufferedOutputStream.close();
			return true;
		} catch (Exception e) {
			throw e;

		}
	}

	/**
	 * 检查文件是否存在，CSS更改里面，还有FTP里面
	 * */
	public static boolean checkFileExit(String filepath) {
		try {
			new FileInputStream(filepath);
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	/**
	 * 求某目录下的文件个数，我们的LICENCEMANAGER用到了
	 */
	public static String[] getfilenames(String path, String strfilter)
			throws Exception {
		File dir;
		FilenameFilter filter;
		dir = new File(path);

		filter = new GlobFilenameFilter(strfilter,
				GlobCompiler.STAR_CANNOT_MATCH_NULL_MASK);
		String[] filenames = dir.list(filter);
		return filenames;
	}

	/**
	 * 字符串变成新文件
	 * */
	public static void strToDoc(String filename, String str) {
		FileOutputStream fostemp;
		try {
			fostemp = new FileOutputStream(filename, false);
			PrintStream out = new PrintStream(
					new BufferedOutputStream(fostemp), false, "utf-8");
			out.print(str);
			out.close();
			fostemp.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 字符串变成新文件
	 * */
	public static void strToDoc(String filename, String str, boolean flag) {
		FileOutputStream fostemp;
		try {
			fostemp = new FileOutputStream(filename, flag);
			PrintStream out = new PrintStream(
					new BufferedOutputStream(fostemp), false, "utf-8");
			out.print(str);
			out.close();
			fostemp.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过字节码的形式写到文件 <br>
	 * JFrameVersion下载升级包时，是下载一个ZIP包，必须用这种方式才不会让包损坏
	 * FileProcessor.betyToDoc(server_path, get.getResponseBody());
	 * 我们如果通过网络转化一个字符串的话，会导致ZIP文件不能使用,
	 */
	public static void betyToDoc(String filename, byte[] betyarray)
			throws Exception {
		FileOutputStream fis = new FileOutputStream(filename);
		fis.write(betyarray);
		fis.close();
	}

	/**
	 * 带时间标签写入新文件里面，是添加新行的模式
	 **/
	public static void writetofile(String filename, String infostr)
			throws Exception {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strCurrTime = df.format(date);
		FileOutputStream fos = new FileOutputStream(filename, true);
		PrintStream out = new PrintStream(new BufferedOutputStream(fos));
		out.println(strCurrTime + " " + infostr);
		out.close();

	}

	public static String getStringFromFile(String fileName) throws IOException {
		File file = new File(fileName);
		BufferedReader bf = new BufferedReader(new FileReader(file));
		String content = "";
		StringBuilder sb = new StringBuilder();
		while (content != null) {
			content = bf.readLine();
			if (content == null) {
				break;
			}
			sb.append(content.trim());
		}

		bf.close();
		return sb.toString();
	}

}