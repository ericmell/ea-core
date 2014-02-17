package com.utils.mail;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.utils.time.TimeUtil;

/*******************************************************************************
 * 发送的邮箱线程模块: 需要和静态初始化配合
 * 
 */
public class SendMailThead extends Thread {
	private static Log log = LogFactory.getLog(SendMailThead.class);
	String mailtitle;
	String mailcontent;
	String mailaddress;
	String[] filenames;
	private static ArrayList emailList;

	public static ArrayList getEmailList() {
		XMLDecoder xmlDecoder;
		try {
			xmlDecoder = new XMLDecoder(new BufferedInputStream(
					new FileInputStream("email_config.xml")));
			EmailConfig emailConfig = (EmailConfig) xmlDecoder.readObject();
			return emailConfig.getEmailList();

		} catch (Exception e) {
			log.debug("email urlmap config file doest not exit!");
			return new ArrayList();

		}
	}

	public SendMailThead(String title, String content, String address,
			String[] filename) {
		try {
			mailtitle = "[Time]" + TimeUtil.getTimeStr("yyyy-MM-dd-hh.mm.ss")
					+ title;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		filenames = filename;
		mailaddress = address;
		mailcontent = content + "\n ****本邮件由系统自动发出，请勿直接回复！****";
	}

	public void run() {
		long begin = System.currentTimeMillis();
		boolean notsendflag = true;
		int stmpNumber = 0;
		/* 如果没有出始化，出始化一次，如果修改了要重新， */

		while (notsendflag && stmpNumber < getEmailList().size()) {
			EmailObject emailObject = (EmailObject) getEmailList().get(
					stmpNumber);
			stmpNumber++;
			try {
				SendMail mail = new SendMail();
				mail.connect(emailObject.getSmtphost(),
						emailObject.getSmtphostusername(),
						emailObject.getSmtphostpassword(),
						emailObject.getPort());
				// 错误也是暗送了;如果是错误的话，不要有暗送
				log.info("THE SMTP=" + emailObject.getFormsender()
						+ emailObject.getPort() + emailObject.getSmtphost());
				mail.send(emailObject.getFormsender(), mailaddress, "", "",
						mailtitle, mailcontent, filenames);

				mail.close();
				notsendflag = false;
				log.info("THE SMTP=" + emailObject.getFormsender());
				log.info("Email Send Time = "
						+ (System.currentTimeMillis() - begin));
			} catch (Exception e) {
				log.error("Email send error = " + emailObject.getTitle(), e);

			}
		}

	}

	// 标题，内容，地址
	public static void sendmail(String title, String content,
			String mailaddress, String[] filename) {

		SendMailThead sendmail = new SendMailThead(title, content, mailaddress,
				filename);
		sendmail.start();

	}

}
