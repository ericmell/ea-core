package com.utils.freemark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class StringbyPerlFreemark {
	private static Log log = LogFactory.getLog(StringbyPerlFreemark.class);

	/*
	 * String savehtmlname, //保存结果路径 String templetpath,
	 * //模版路径（提醒：因为直接生成文件，需要指定一个模版目录） String Fltfilename, //模版文件 SimpleHash root
	 * //业务数据
	 */

	public static void getfilebyFreemark(String savepath,String savehtmlname,
			String templetpath, String Fltfilename, SimpleHash root)
			throws Exception {
		try {
			
			FileUtils.forceMkdir(new File(savepath));
			Configuration cfg = new Configuration();
			cfg.setDefaultEncoding("utf-8"); // 这个如果设置成utf-8代表该flt是utf8得，所以去掉
			cfg.setDirectoryForTemplateLoading(new File(templetpath));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			Template temp = cfg.getTemplate(Fltfilename);
			Charset charset = Charset.forName("utf-8"); // 保存到本地的文件就是GBK，如果浏览起器打开就是正确的
			CharsetEncoder encoder = charset.newEncoder();

			Writer out = new OutputStreamWriter(new FileOutputStream(
					savepath+savehtmlname, false), encoder);

			temp.process(root, out);

			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("[SNP ERROR]getfilebyFreemark by gen file="
					+ savehtmlname + ", tmpletfile=" + Fltfilename, e);
			throw e;
		}

	}

}
