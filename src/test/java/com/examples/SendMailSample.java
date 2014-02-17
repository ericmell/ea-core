package com.examples;

import com.utils.mail.SendMail;

/**
 * 主要电脑不要限制端口发出信息，
 * 1. new sendmail() 使用GBK的默认编码发送文本信息 new sendmail(编码,文本还是html); sendmail mail =
 * new sendmail("GBK", true); 2. mail.connect (smtp服务器);
 * main.connect(smtp服务器,用户名,密码); 适用于smtp认证的发信服务器 3.
 * main.send(from,to，cc,bcc,主题,正文,附件文件名) to,cc（抄送）,bcc(暗送)可填写多个mail地址
 * 抄送多个的话是要使用，隔开 附件文件名为null，表示不发送附件 定义两个字段，emailleavword , emailorder
 */
public class SendMailSample {
	public static void main(String[] args) throws Exception {
		SendMail sendMail = new SendMail();
		sendMail.connect("smtp.163.com", "gscsystem", "abc123", "25");
		sendMail.send("gscsystem@163.com", "tom.ling@ericsson.com", "", "",
				"from 163 用户发的", "test这是正文", null);
		sendMail.close();
		System.out.println("发送完毕");

	}

}
