package com.utils.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 调用java mail发送邮件的类
 */
public class SendMail {
	private static Log log = LogFactory.getLog(SendMail.class);
	private String encode;// 文本编码

	private boolean HTML;// 文本还是html内容

	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private javax.mail.Session session;

	private javax.mail.Transport transport;

	public SendMail() {
		// this.encode = "GBK";
		this.encode = "utf-8";
		this.HTML = true;
	}

	public SendMail(String encode, boolean html) {
		this.encode = encode;
		this.HTML = html;
	}

	public boolean connect(String smtpHost) {
		return connect(smtpHost, null, null, null);
	}

	public boolean connect(String smtpHost, String uid, String pwd, String port) {
		try {
			java.util.Properties properties = new java.util.Properties();
			properties.put("mail.smtp.host", smtpHost);
			properties.setProperty("mail.smtp.port", port);
			// 如果使用GOOLGE邮箱，就要加这个
			if (smtpHost.indexOf("gmail") > 0) {
				properties.put("mail.smtp.socketFactory.class", SSL_FACTORY);
				properties.put("mail.smtp.socketFactory.fallback", "false");
			}
			if (uid == null || uid.equals("")) {
				session = javax.mail.Session.getInstance(properties, null);
			} else {
				properties.put("mail.smtp.auth", "true");
				Auth auth = new Auth(uid, pwd);
				session = javax.mail.Session.getInstance(properties, auth);
			}
			transport = session.getTransport("smtp");
			transport.connect();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void close() {
		if (transport != null) {
			try {
				transport.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void send(String from, String to, String cc, String bcc,
			String subject, String text, String[] filename) throws Exception {
		try {
			javax.mail.internet.MimeMessage mimeMessage = createMimeMessage(
					session, from, to, cc, bcc, subject, text, filename);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		} catch (Exception e) {
			log.error(from + "error,please check it");
			throw e;
		}
	}

	private javax.mail.internet.MimeMessage createMimeMessage(
			javax.mail.Session session, String from, String to, String cc,
			String bcc, String subject, String text, String[] filename)
			throws javax.mail.MessagingException {
		javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage(
				session);
		javax.mail.internet.InternetAddress fromAddress = new javax.mail.internet.InternetAddress(
				from);
		message.setFrom(fromAddress);

		javax.mail.internet.InternetAddress[] toAddress = javax.mail.internet.InternetAddress
				.parse(to);
		javax.mail.internet.InternetAddress[] ccAddress = javax.mail.internet.InternetAddress
				.parse(cc);
		javax.mail.internet.InternetAddress[] bccAddress = javax.mail.internet.InternetAddress
				.parse(bcc);

		message.setRecipients(javax.mail.Message.RecipientType.TO, toAddress);
		message.setRecipients(javax.mail.Message.RecipientType.CC, ccAddress);
		message.setRecipients(javax.mail.Message.RecipientType.BCC, bccAddress);

		message.setSubject(subject, this.encode);
		// 处理附件
		if (filename != null && filename.length > 0) {
			javax.mail.Multipart multipart = new javax.mail.internet.MimeMultipart();

			javax.mail.internet.MimeBodyPart mimeBodyPart = new javax.mail.internet.MimeBodyPart();
			if (HTML) {
				mimeBodyPart.setContent(text, "text/html;charset="
						+ this.encode);
			} else {
				mimeBodyPart.setText(text, this.encode);
			}
			multipart.addBodyPart(mimeBodyPart);
			for (int i = 0; i < filename.length; i++) {
				try {
					javax.mail.internet.MimeBodyPart fileBodyPart = new javax.mail.internet.MimeBodyPart();
					javax.activation.DataSource datasource = new javax.activation.FileDataSource(
							filename[i]);
					fileBodyPart
							.setDataHandler(new javax.activation.DataHandler(
									datasource));
					fileBodyPart.setFileName(new java.io.File(filename[i])
							.getName());
					multipart.addBodyPart(fileBodyPart);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			message.setContent(multipart);
		} else {
			if (HTML) {
				message.setContent(text, "text/html;charset=" + this.encode);
			} else {
				message.setText(text, this.encode);
			}
		}
		return message;
	}

}
