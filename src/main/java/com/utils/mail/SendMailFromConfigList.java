package com.utils.mail;

/*******************************************************************************
 * import com.snp.common.Dateprocess; import com.snp.site.config.EmailConfig;
 * import com.snp.site.config.EmailObject; import com.snp.site.init.SystemInit;
 * 
 * 
 * public class SendMailFromConfigList extends Thread { private static Log log =
 * LogFactory.getLog(SendMailThead.class); String mailtitle; String mailcontent;
 * String mailaddress; String configpath; String[] filenames;
 * 
 * public SendMailFromConfigList(String title, String content, String address,
 * String[] filename ,String path) { try { mailtitle
 * ="[Time]"+Dateprocess.getTimeStr("yyyy-MM-dd-hh.mm.ss") + title; } catch
 * (Exception e1) { e1.printStackTrace(); } configpath=path; filenames =
 * filename; mailaddress = address; mailcontent = content +
 * "\n ****本邮件由系统自动发出，请勿直接回复！****"; }
 * 
 * public void run() { long begin = System.currentTimeMillis(); boolean
 * notsendflag = true; int stmpNumber = 0; ArrayList
 * smtplist=getSmtpList(configpath); while (notsendflag && stmpNumber <
 * smtplist.size()) { EmailObject emailObject = (EmailObject)
 * smtplist.get(stmpNumber); stmpNumber++; try {
 * 
 * log.debug("使用的Email设置：SMTP服务器： " + emailObject.getSmtphost()+":"+
 * emailObject.getPort()+"用户名"+emailObject.getSmtphostusername()); sendmail mail
 * = new sendmail();
 * 
 * mail.connect(emailObject.getSmtphost(), emailObject .getSmtphostusername(),
 * emailObject .getSmtphostpassword(),emailObject.getPort());
 * 
 * mail.send(emailObject.getFormsender(), mailaddress, "",
 * SystemInit.mail_ansong, mailtitle, mailcontent, filenames);
 * 
 * mail.close(); notsendflag = false; log.info("THE SMTP=" +
 * emailObject.getFormsender()); log.info("Email Send Time = "+
 * (System.currentTimeMillis() - begin)); } catch (Exception e) {
 * log.error("SMTP服务器： " +
 * emailObject.getSmtphost()+":用户名"+emailObject.getSmtphostusername() +"帐号异常",
 * e);
 * 
 * } }
 * 
 * } //标题，内容，地址，文件名
 * 
 * public static void sendmail(String title, String content,String mailaddress,
 * String[] filename,String configpath) { SendMailFromConfigList send = new
 * SendMailFromConfigList(title, content, mailaddress,filename,configpath);
 * send.start();
 * 
 * }
 * 
 * public static ArrayList getSmtpList(String configpath){ ArrayList
 * smtplist=new ArrayList(); XMLDecoder xmlDecoder; try { xmlDecoder = new
 * XMLDecoder(new BufferedInputStream( new FileInputStream(configpath) ));
 * EmailConfig emailConfig = (EmailConfig) xmlDecoder.readObject();
 * xmlDecoder.close(); smtplist= emailConfig.getEmailList(); } catch (Exception
 * e) { log.error("email stmp config file error!"); e.printStackTrace(); }
 * return smtplist; } //lingli@revenco.com时给屏蔽掉了 public static void main
 * (String[] args) { SendMailFromConfigList.sendmail("房子", "2222我 爱你，斯内普",
 * "freemanlingli@qq.com", null,"e:/stmp.xml"); } }
 */
