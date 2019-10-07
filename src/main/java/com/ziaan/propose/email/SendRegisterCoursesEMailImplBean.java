package com.ziaan.propose.email;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.common.GetCodenm;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.Mailing;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.propose.email.log.EmailLog;
import com.ziaan.propose.email.log.impl.EmailLogImplBean;

/**
 * SendRegisterCoursesEMail�� ������ ����
 * @author Yang Seunghyeon
 * @category SendRegisterCoursesEMail
 */

public class SendRegisterCoursesEMailImplBean implements SendRegisterCoursesEMail {
	private ConfigSet conf;
	
///////////////////////////////////////////////////////�н��ڰ� ��û�ϰ� ����ϴ� ��� ����////////////////////////////////////////	
	
	//���� ��û ��ҽ�
	public int canceledRegisterCoursesMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//���ϼ���
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//������ �� �̸�
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//������ �� �̸���
		String p_toEmail   = box.getSession("email"); //�޴� �� �̸���
		String p_mailTitle   = box.getStringDefault("p_title", "�������");// �̸��� ����
		String p_mailContent = box.getStringDefault("p_content", "�������");//�̸��� ����
		StringBuffer mailForm = null;
		
		System.out.println("p_mailServer===========>"+p_mailServer);
		System.out.println("p_fromName=============>"+p_fromName);
		System.out.println("p_fromEmail============>"+p_fromEmail);
		System.out.println("p_toEmail==============>"+p_toEmail);
		
		String v_subj = box.getString("p_subj");// ���� �ڵ�
		String v_subjnm =null;// �����
		String v_eduStart=null;
		String v_eduEnd=null;
		String v_year=box.getString("p_year");//�⵵
		String v_subjseq=box.getString("p_subjseq");
		String v_userName=box.getSession("name");
		System.out.println("v_userName==============>"+v_userName);
		//String v_reason = box.getString("p_reason");//�������
		
		try {
			System.out.println("v_subj=======>"+v_subj);
			System.out.println("v_year=======>"+v_year);
			System.out.println("v_subjseq=======>"+v_subjseq);
			connMgr=new DBConnectionManager();
			sql="  select a.edustart edustart,a.eduend eduend, b.subjnm subjnm                 \n" ;
			sql+=" from tz_subjseq a LEFT OUTER JOIN tz_subj b on a.subj=b.subj                \n" ;
			sql+=" where a.subj=";
			sql+=SQLString.Format(v_subj);
			sql+=" and a.SUBJSEQ=";
			sql+=SQLString.Format(v_subjseq);
			sql+=" and a.year= ";
			sql+=SQLString.Format(v_year);
			
			ls=connMgr.executeQuery(sql);
			System.out.println("canceledRegisterCoursesMail sql =====>"+sql);
			while(ls.next()){
				v_eduStart=FormatDate.getFormatDate(ls.getString("edustart"), "yyyy.MM.dd");
				v_eduEnd=FormatDate.getFormatDate(ls.getString("eduend"), "yyyy.MM.dd");
				v_subjnm=ls.getString("subjnm");
			}
			
			System.out.println("edustart =====>"+v_eduStart);
			System.out.println("eduend 	 =====>"+v_eduEnd);
			
			// ����ڿ��� ���� �޽����� �ۼ��Ѵ�.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0> ");
			mailForm.append("<tr>");
			mailForm.append("<td width=7%></td>");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>");
			mailForm.append(v_userName);mailForm.append("��,�ȳ��ϼ��� e-Eureka�Դϴ�.<br> ��<font style='color=#0033ff'> ");
			mailForm.append(v_subjnm);mailForm.append("</font>�� ������ ���� ��Ұ� �Ϸ�Ǿ����ϴ�.<br>");
			mailForm.append("���� ��ȸ�� �� ������ �н��� ���Ͻø� �ٽ� ������û �ٶ��ϴ�. <br>���� �Ϸ� ��������.<br><br>");
			mailForm.append("<b>������Ұ���</b><br>");
			mailForm.append("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#cccccc' >");
			mailForm.append("<tr>");
			mailForm.append("<td valign=middle style='font-weight:bold;font-size:9pt;line-height:150%;color:#5c5c5c;word-break:break-all; padding-left: 5px;padding-top: 5px;padding-bottom: 5px;'>");
			mailForm.append("��&nbsp;��&nbsp;��&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_subjnm);		
			mailForm.append("</font><br> ");
			mailForm.append("�н��Ⱓ&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_eduStart);mailForm.append("&nbsp;~&nbsp;");mailForm.append(v_eduEnd);mailForm.append("</font>");
			mailForm.append("<br>");
			mailForm.append("</td>");
			mailForm.append("</tr>");
			mailForm.append("</table>");
			mailForm.append("<br>");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka �ٷΰ���</a></center>");
			mailForm.append("<br><br>");
			mailForm.append("<b>e-Eureka&nbsp;&nbsp;���</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='7%'></td></tr>");
			mailForm.append("</table>");
			mailForm.append("<td></tr><tr><td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table>");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-Eureka] ������� �Ǿ����ϴ�.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);
			
							
			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			

			if(isOk==1){//�߼��� �����ϸ�...
				//�����α׿� �����.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getSession("userid"));
			}else{//�߼��� �����ϸ�
				//�����α׿� �����.
				System.out.println("\n email send ===========>  failed,\n userId===========>"+box.getSession("userid"));
			}
			//�����ͺ��̽� �̸��Ϸα׿� ������ �����ϱ� ���� box�� ������ �߰� �����Ѵ�.
			box.put("p_receiverName", v_userName);// ������ �̸�
			box.put("p_email",p_toEmail );//������ �̸���
			box.put("p_userid", box.getSession("userid"));//������ ���̵�
			box.put("p_mailTitle", p_mailTitle);//�̸��� ����
			box.put("p_mailContent", p_mailContent);//���� ����
			box.put("p_senderEmail", p_fromEmail);// �߽��� �̸���
			box.put("p_senderName", p_fromName); // �߽��� �̸�
			box.put("p_status", new Integer(isOk));// �߽� ��������
		
		emailLog=new EmailLogImplBean();
		
		emailLog.insertEmailLog(box);
			
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			try{
				if(ls!=null)ls.close();
				if(pstmt!=null)pstmt.close();
				if(connMgr!=null)connMgr.freeConnection();
			}catch (Exception e) {
				
			}
		}
		return isOk;
	}
	

	
	//���� ��û ��
	public int sendRegisterCoursesMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//���ϼ���
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//������ �� �̸�
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//������ �� �̸���
		String p_toEmail   = box.getSession("email"); //�޴� �� �̸���
		String p_mailTitle   = box.getStringDefault("p_title", "�������");// �̸��� ����
		String p_mailContent = box.getStringDefault("p_content", "�������");//�̸��� ����
		String v_userName=box.getSession("name");
		StringBuffer mailForm = null;
		System.out.println("p_mailServer===========>"+p_mailServer);
		System.out.println("p_fromName=============>"+p_fromName);
		System.out.println("p_fromEmail============>"+p_fromEmail);

		String v_subj = box.getString("p_subj");// ���� �ڵ�
		String v_subjnm =null;// �����
		String v_eduStart=null;
		String v_eduEnd=null;
		String v_tabseq=box.getString("p_tabseq");
		String v_year=box.getString("p_year");
		String v_subjseq=box.getString("p_subjseq");
		System.out.println("v_userName==============>"+v_userName);
		System.out.println("p_toEmail==============>"+p_toEmail);
		System.out.println("v_tabseq==============>"+v_tabseq);

		
		try {
			System.out.println("v_subj=======>"+v_subj);
			System.out.println("v_year=======>"+v_year);
			System.out.println("v_subjseq=======>"+v_subjseq);
			connMgr=new DBConnectionManager();
			sql="  select a.edustart edustart,a.eduend eduend, b.subjnm subjnm                 \n" ;
			sql+=" from tz_subjseq a LEFT OUTER JOIN tz_subj b on a.subj=b.subj                \n" ;
			sql+=" where a.subj=";
			sql+=SQLString.Format(v_subj);
			sql+="and a.SUBJSEQ=";
			sql+=SQLString.Format(v_subjseq);
			sql+=" and a.year= ";
			sql+=SQLString.Format(v_year);
			
			ls=connMgr.executeQuery(sql);
			System.out.println("sendRegisterCoursesMail sql =====>"+sql);
			while(ls.next()){
				v_eduStart=FormatDate.getFormatDate(ls.getString("edustart"), "yyyy.MM.dd");
				v_eduEnd=FormatDate.getFormatDate(ls.getString("eduend"), "yyyy.MM.dd");
				v_subjnm=ls.getString("subjnm");
			}
			
			System.out.println("edustart =====>"+v_eduStart);
			System.out.println("eduend 	 =====>"+v_eduEnd);
			
			// ����ڿ��� ���� �޽����� �ۼ��Ѵ�.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0 >  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr>  ");
			mailForm.append("<td width=7%></td>  ");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>  ");
			mailForm.append(v_userName);mailForm.append("��, e-Eureka�Դϴ�.<br> �� <font style='color=#0033ff'> ");
			mailForm.append(v_subjnm);mailForm.append("</font> �� ������ ������û�� �Ǿ����� ���δ�� ���Դϴ�.<br>  ");
			mailForm.append("����������� ���� �� ������ �����Ͻʴϴ�.<br><br>  ");
			mailForm.append("<b>������û����</b><br>  ");
			mailForm.append("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#cccccc' >  ");
			mailForm.append("<tr>  ");
			mailForm.append("<td valign='middle' style='font-weight:bold;font-size:9pt;line-height:150%;color:#5c5c5c;word-break:break-all; padding-left: 5px;padding-top: 5px;padding-bottom: 5px;'>  ");
			mailForm.append("��&nbsp;��&nbsp;��&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_subjnm);		
			mailForm.append("</font><br> ");
			mailForm.append("�н��Ⱓ&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_eduStart);mailForm.append("&nbsp;~&nbsp;");mailForm.append(v_eduEnd);mailForm.append("</font>");
			mailForm.append("<br> ");
			mailForm.append("</td> ");
			mailForm.append("</tr> ");
			mailForm.append("</table> ");
			mailForm.append("<br> ");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka �ٷΰ���</a></center>");
			mailForm.append("<br><br> ");
			mailForm.append("<b>e-Eureka&nbsp;&nbsp;���</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='7%'></td></tr> ");
			mailForm.append("</table> ");
			mailForm.append("<td></tr><tr> <td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table> ");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-Eureka] ������û�� �Ǿ����ϴ�.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);
			
			
			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			
			
			box.put("p_userName", v_userName);// ������ �̸�
			box.put("p_email",p_toEmail );//������ �̸���
			box.put("p_userid", box.getSession("userid"));//������ ���̵�
			
			if(isOk==1){//�߼��� �����ϸ�...
				//�����α׿� �����.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getSession("userid"));
			}else{//�߼��� �����ϸ�
				//�����α׿� �����.
				System.out.println("\n email send ===========>  failed,\n userId===========>"+box.getSession("userid"));
			}
			//�����ͺ��̽� �̸��Ϸα׿� ������ �����ϱ� ���� box�� ������ �߰� �����Ѵ�.
			box.put("p_receiverName", v_userName);// ������ �̸�
			box.put("p_email",p_toEmail );//������ �̸���
			box.put("p_userId", box.getSession("userid"));//������ ���̵�
			box.put("p_receiverEmail", box.getSession("userid"));//������ ���̵�
			box.put("p_mailTitle", p_mailTitle);//�̸��� ����
			box.put("p_mailContent", p_mailContent);//���� ����
			box.put("p_senderEmail", p_fromEmail);// �߽��� �̸���
			box.put("p_senderName", p_fromName); // �߽��� �̸�
			box.put("p_status", new Integer(isOk));// �߽� ��������
			
			emailLog=new EmailLogImplBean();
		
			emailLog.insertEmailLog(box);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			try{
				if(ls!=null)ls.close();
				if(pstmt!=null)pstmt.close();
				if(connMgr!=null)connMgr.freeConnection();
			}catch (Exception e) {
				
			}
		}
		return isOk;
	}
///////////////////////////////////////////////////////�н��ڰ� ��û�ϰ� ����ϴ� ��� ��////////////////////////////////////////	
	
	
	
///////////////////////////////////////////////////////��ڰ� �����ϰ� �ݷ�(����)�ϴ� ��� ����////////////////////////////////////////	
	//���� ��û ���� ��
	public int sendAcceptRegisterCoursesMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//���ϼ���
		
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//������ �� �̸�
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//������ �� �̸���
		String p_toEmail   = box.getString("p_email"); //�޴� �� �̸���
		String p_mailTitle   = box.getStringDefault("p_title", "�������");// �̸��� ����
		String p_mailContent = box.getStringDefault("p_content", "�������");//�̸��� ����
		StringBuffer mailForm = null;
		String v_userName=box.getString("p_userName");
		
		System.out.println("p_mailServer===========>"+p_mailServer);
		System.out.println("p_fromName=============>"+p_fromName);
		System.out.println("p_fromEmail============>"+p_fromEmail);
		
		

		String v_subj = box.getString("p_subj");// ���� �ڵ�
		String v_subjnm =null;// �����
		String v_eduStart=null;
		String v_eduEnd=null;
		String v_year=box.getString("p_year");
		String v_subjseq=box.getString("p_subjseq");
		String v_userid=box.getString("p_userid");
		System.out.println("v_userName==============>"+v_userName);
		System.out.println("p_toEmail==============>"+p_toEmail);
		
		
		System.out.println("p_mailServer=======>"+p_mailServer);
		try {
			System.out.println("v_subj=======>"+v_subj);
			System.out.println("v_year=======>"+v_year);
			System.out.println("v_subjseq=======>"+v_subjseq);
			connMgr=new DBConnectionManager();
			sql="  select a.edustart edustart,a.eduend eduend, b.subjnm subjnm                 \n" ;
			sql+=" from tz_subjseq a LEFT OUTER JOIN tz_subj b on a.subj=b.subj                \n" ;
			sql+=" where a.subj=";
			sql+=SQLString.Format(v_subj);
			sql+="and a.SUBJSEQ=";
			sql+=SQLString.Format(v_subjseq);
			sql+=" and a.year= ";
			sql+=SQLString.Format(v_year);
			
			ls=connMgr.executeQuery(sql);
			System.out.println("sendRegisterCoursesMail sql =====>"+sql);
			while(ls.next()){
				v_eduStart=FormatDate.getFormatDate(ls.getString("edustart"), "yyyy.MM.dd");
				v_eduEnd=FormatDate.getFormatDate(ls.getString("eduend"), "yyyy.MM.dd");
				v_subjnm=ls.getString("subjnm");
			}
			
			System.out.println("edustart =====>"+v_eduStart);
			System.out.println("eduend 	 =====>"+v_eduEnd);
			
			// ����ڿ��� ���� �޽����� �ۼ��Ѵ�.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0 >  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr>  ");
			mailForm.append("<td width=7%></td>  ");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>  ");
			mailForm.append(v_userName);mailForm.append("��, e-Eureka�Դϴ�.<br> �� <font style='color=#0033ff'> ");
			mailForm.append(v_subjnm);mailForm.append("</font> �� ������  ���εǾ����ϴ�.<br>  ");
			mailForm.append("���� ��û�� ������ ����� �н��Ͻþ� ���� ���� �����ñ� �ٶ��ϴ�.<br><br>  ");
			mailForm.append("<b>�������ΰ���</b><br>  ");
			mailForm.append("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#cccccc' >  ");
			mailForm.append("<tr>  ");
			mailForm.append("<td valign='middle' style='font-weight:bold;font-size:9pt;line-height:150%;color:#5c5c5c;word-break:break-all; padding-left: 5px;padding-top: 5px;padding-bottom: 5px;'>  ");
			mailForm.append("��&nbsp;��&nbsp;��&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_subjnm);		
			mailForm.append("</font><br> ");
			mailForm.append("�н��Ⱓ&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_eduStart);mailForm.append("&nbsp;~&nbsp;");mailForm.append(v_eduEnd);mailForm.append("</font>");
			mailForm.append("<br> ");
			mailForm.append("</td> ");
			mailForm.append("</tr> ");
			mailForm.append("</table> ");
			mailForm.append("<br> ");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka �ٷΰ���</a></center>");
			mailForm.append("<br><br> ");
			mailForm.append("<b>e-Eureka&nbsp;&nbsp;���</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='7%'></td></tr> ");
			mailForm.append("</table> ");
			mailForm.append("<td></tr><tr> <td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table> ");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-Eureka] ������û������ ���εǾ����ϴ�.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);

			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			
			if(isOk==1){//�߼��� �����ϸ�...
				//�����α׿� �����.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}else{//�߼��� �����ϸ�
				//�����α׿� �����
				System.out.println("\n email send ===========>  failed,\n userId===========>"+box.getString("p_userid"));
			}
			//�����ͺ��̽� �̸��Ϸα׿� ������ �����ϱ� ���� box�� ������ �߰� �����Ѵ�.
			box.put("p_receiverName", v_userName);// ������ �̸�
			box.put("p_email",p_toEmail );//������ �̸���
			box.put("p_mailTitle", p_mailTitle);//�̸��� ����
			box.put("p_mailContent", p_mailContent);//���� ����
			box.put("p_senderEmail", p_fromEmail);// �߽��� �̸���
			box.put("p_senderName", p_fromName); // �߽��� �̸�
			box.put("p_status", new Integer(isOk));// �߽� ��������
			
			
			emailLog=new EmailLogImplBean();
			emailLog.insertEmailLog(box);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			try{
				if(ls!=null)ls.close();
				if(pstmt!=null)pstmt.close();
				if(connMgr!=null)connMgr.freeConnection();
			}catch (Exception e) {
				
			}
		}
		return isOk;
	}
	
	//���� ��û �ݷ�
	public int sendRejectRegisterCoursesMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//���ϼ���
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//������ �� �̸�
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//������ �� �̸���
		String p_toEmail   = box.getString("p_email"); //�޴� �� �̸���
		String p_mailTitle   = box.getStringDefault("p_title", "�������");// �̸��� ����
		String p_mailContent = box.getStringDefault("p_content", "�������");//�̸��� ����
		String v_userName=box.getString("p_userName");
		StringBuffer mailForm = null;
		
		
		System.out.println("p_mailServer===========>"+p_mailServer);
		System.out.println("p_fromName=============>"+p_fromName);
		System.out.println("p_fromEmail============>"+p_fromEmail);
	

		String v_subj = box.getString("p_subj");// ���� �ڵ�
		String v_subjnm =null;// �����
		String v_eduStart=null;
		String v_eduEnd=null;
		String v_year=box.getString("p_year");//�⵵
		String v_reason = box.getStringDefault("p_reason","��� �̽���");//�ݷ�����
		String v_userid=box.getString("p_userid");
		String v_subjseq=box.getString("p_subjseq");
		
		System.out.println("v_userName==============>"+v_userName);
		System.out.println("p_toEmail==============>"+p_toEmail);
		
		try {
			System.out.println("v_subj=======>"+v_subj);
			System.out.println("v_year=======>"+v_year);
			System.out.println("v_subjseq=======>"+v_subjseq);
			connMgr=new DBConnectionManager();
			sql="  select a.edustart edustart,a.eduend eduend, b.subjnm subjnm                 \n" ;
			sql+=" from tz_subjseq a LEFT OUTER JOIN tz_subj b on a.subj=b.subj                \n" ;
			sql+=" where a.subj=";
			sql+=SQLString.Format(v_subj);
			sql+=" and a.SUBJSEQ=";
			sql+=SQLString.Format(v_subjseq);
			sql+=" and a.year= ";
			sql+=SQLString.Format(v_year);
			
			ls=connMgr.executeQuery(sql);
			System.out.println("canceledRegisterCoursesMail sql =====>"+sql);
			while(ls.next()){
				v_eduStart=FormatDate.getFormatDate(ls.getString("edustart"), "yyyy.MM.dd");
				v_eduEnd=FormatDate.getFormatDate(ls.getString("eduend"), "yyyy.MM.dd");
				v_subjnm=ls.getString("subjnm");
			}
			
			System.out.println("edustart =====>"+v_eduStart);
			System.out.println("eduend 	 =====>"+v_eduEnd);
			
			// ����ڿ��� ���� �޽����� �ۼ��Ѵ�.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0> ");
			mailForm.append("<tr>");
			mailForm.append("<td width=7%></td>");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>");
			mailForm.append(v_userName);mailForm.append("��,�ȳ��ϼ��� e-Eureka�Դϴ�.<br> ������û�Ͻš� <font style='color=#0033ff'>");
			mailForm.append(v_subjnm);mailForm.append("</font>�� ������ �ݷ��Ǿ����ϴ�.<br>");
			mailForm.append("�ڼ��� ������ �系 ��������ڿ��� ���ǹٶ��ϴ�<br><br>");
			mailForm.append("<b>�����ݷ�����</b><br>");
			mailForm.append("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#cccccc' >");
			mailForm.append("<tr>");
			mailForm.append("<td valign=middle style='font-weight:bold;font-size:9pt;line-height:150%;color:#5c5c5c;word-break:break-all; padding-left: 5px;padding-top: 5px;padding-bottom: 5px;'>");
			mailForm.append("��&nbsp;��&nbsp;��&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_subjnm);		
			mailForm.append("</font><br> ");
			mailForm.append("�н��Ⱓ&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_eduStart);mailForm.append("&nbsp;~&nbsp;");mailForm.append(v_eduEnd);mailForm.append("</font>");
			mailForm.append("<br>");
			mailForm.append("�ݷ�����  :<font style='color=#0033ff'>");mailForm.append(v_reason);
			mailForm.append("</font></td>");
			mailForm.append("</tr>");
			mailForm.append("</table>");
			mailForm.append("<br>");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka �ٷΰ���</a></center>");
			mailForm.append("<br><br>");
			mailForm.append("<b>e-Eureka&nbsp;&nbsp;���</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='7%'></td></tr>");
			mailForm.append("</table>");
			mailForm.append("<td></tr><tr><td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table>");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-Eureka] ������û������ �ݷ��Ǿ����ϴ�.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);
		
			
			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			

			if(isOk==1){//�߼��� �����ϸ�...
				//�����α׿� �����.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}else{//�߼��� �����ϸ�
				//�����α׿� �����.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}
			//�����ͺ��̽� �̸��Ϸα׿� ������ �����ϱ� ���� box�� ������ �߰� �����Ѵ�.
			box.put("p_receiverName", v_userName);// ������ �̸�
			box.put("p_email",p_toEmail );//������ �̸���
			box.put("p_mailTitle", p_mailTitle);//�̸��� ����
			box.put("p_mailContent", p_mailContent);//���� ����
			box.put("p_senderEmail", p_fromEmail);// �߽��� �̸���
			box.put("p_senderName", p_fromName); // �߽��� �̸�
			box.put("p_status", new Integer(isOk));// �߽� ��������
			
			
			emailLog=new EmailLogImplBean();			
			emailLog.insertEmailLog(box);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			try{
				if(ls!=null)ls.close();
				if(pstmt!=null)pstmt.close();
				if(connMgr!=null)connMgr.freeConnection();
			}catch (Exception e) {
				
			}
		}
		return isOk;
	}
///////////////////////////////////////////////////////��ڰ� �����ϰ� �ݷ�(����)�ϴ� ��� ��////////////////////////////////////////
	
	
	public int sendSubjQnaStudyMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//���ϼ���
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//������ �� �̸�
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//������ �� �̸���
		String p_toEmail   = box.getString("p_email"); //�޴� �� �̸���
		String p_mailTitle   = box.getStringDefault("p_title", "�������");// �̸��� ����
		String p_mailContent = box.getStringDefault("p_content", "�������");//�̸��� ����
		String v_userName    = box.getSession("name");
		StringBuffer mailForm = null;
		
		
		try {
			connMgr=new DBConnectionManager();
			sql="  select email                 \n" ;
			sql+="   from tz_member                \n" ;
			sql+="  where userid = '"+box.getSession("userid")+"'";
			
			ls=connMgr.executeQuery(sql);
			System.out.println("canceledRegisterCoursesMail sql =====>"+sql);
			while(ls.next()){
				p_toEmail=ls.getString("email");
			}
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + p_toEmail);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + p_mailTitle);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + p_mailContent);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + v_userName);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + p_fromEmail);
			// ����ڿ��� ���� �޽����� �ۼ��Ѵ�.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0> ");
			mailForm.append("<tr>");
			mailForm.append("<td width=12%></td>");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>");
			mailForm.append(box.getSession("name")+"��,�ȳ��ϼ��� e-Eureka�Դϴ�.<br> "+box.getSession("name")+"�Բ��� Q&A�� �ø��� ���� �����Ǿ����ϴ�.<br>");
			mailForm.append("�ñ��Ͻ� ���� ���� �ذ�� �� �ֵ��� �ż��� ó���ϵ��� �ϰڽ��ϴ�.<br><br>");
			mailForm.append("<br>��ſ� �Ϸ� ��������!<br>�����մϴ�.");
			mailForm.append("<br><br>");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka �ٷΰ���</a></center>");
			mailForm.append("<br><br>");
			mailForm.append("<b>[e-����ī ����]</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='12%'></td></tr>");
			mailForm.append("</table>");
			mailForm.append("<td></tr><tr><td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table>");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-����ī] Q&A�� �����Ǿ����ϴ�.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);
		
			
			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			

			if(isOk==1){//�߼��� �����ϸ�...
				//�����α׿� �����.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}else{//�߼��� �����ϸ�
				//�����α׿� �����.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}
			//�����ͺ��̽� �̸��Ϸα׿� ������ �����ϱ� ���� box�� ������ �߰� �����Ѵ�.
			box.put("p_receiverName", v_userName);// ������ �̸�
			box.put("p_email",p_toEmail );//������ �̸���
			box.put("p_mailTitle", p_mailTitle);//�̸��� ����
			box.put("p_mailContent", p_mailContent);//���� ����
			box.put("p_senderEmail", p_fromEmail);// �߽��� �̸���
			box.put("p_senderName", p_fromName); // �߽��� �̸�
			box.put("p_status", new Integer(isOk));// �߽� ��������
			
			
			emailLog=new EmailLogImplBean();			
			emailLog.insertEmailLog(box);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			try{
				if(ls!=null)ls.close();
				if(pstmt!=null)pstmt.close();
				if(connMgr!=null)connMgr.freeConnection();
			}catch (Exception e) {
				
			}
		}
		return isOk;
	}
}
