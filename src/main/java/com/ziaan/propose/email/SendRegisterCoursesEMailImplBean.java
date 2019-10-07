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
 * SendRegisterCoursesEMail를 구현한 빈즈
 * @author Yang Seunghyeon
 * @category SendRegisterCoursesEMail
 */

public class SendRegisterCoursesEMailImplBean implements SendRegisterCoursesEMail {
	private ConfigSet conf;
	
///////////////////////////////////////////////////////학습자가 신청하고 취소하는 경우 시작////////////////////////////////////////	
	
	//수강 신청 취소시
	public int canceledRegisterCoursesMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//메일서버
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//보내는 이 이름
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//보내는 이 이메일
		String p_toEmail   = box.getSession("email"); //받는 이 이메일
		String p_mailTitle   = box.getStringDefault("p_title", "제목없음");// 이메일 제목
		String p_mailContent = box.getStringDefault("p_content", "내용없음");//이메일 내용
		StringBuffer mailForm = null;
		
		System.out.println("p_mailServer===========>"+p_mailServer);
		System.out.println("p_fromName=============>"+p_fromName);
		System.out.println("p_fromEmail============>"+p_fromEmail);
		System.out.println("p_toEmail==============>"+p_toEmail);
		
		String v_subj = box.getString("p_subj");// 과목 코드
		String v_subjnm =null;// 과목명
		String v_eduStart=null;
		String v_eduEnd=null;
		String v_year=box.getString("p_year");//년도
		String v_subjseq=box.getString("p_subjseq");
		String v_userName=box.getSession("name");
		System.out.println("v_userName==============>"+v_userName);
		//String v_reason = box.getString("p_reason");//취소이유
		
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
			
			// 사용자에게 보낼 메시지를 작성한다.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0> ");
			mailForm.append("<tr>");
			mailForm.append("<td width=7%></td>");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>");
			mailForm.append(v_userName);mailForm.append("님,안녕하세요 e-Eureka입니다.<br> 『<font style='color=#0033ff'> ");
			mailForm.append(v_subjnm);mailForm.append("</font>』 과정의 수강 취소가 완료되었습니다.<br>");
			mailForm.append("다음 기회에 본 과정의 학습을 원하시면 다시 수강신청 바랍니다. <br>좋은 하루 보내세요.<br><br>");
			mailForm.append("<b>수강취소과정</b><br>");
			mailForm.append("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#cccccc' >");
			mailForm.append("<tr>");
			mailForm.append("<td valign=middle style='font-weight:bold;font-size:9pt;line-height:150%;color:#5c5c5c;word-break:break-all; padding-left: 5px;padding-top: 5px;padding-bottom: 5px;'>");
			mailForm.append("과&nbsp;정&nbsp;명&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_subjnm);		
			mailForm.append("</font><br> ");
			mailForm.append("학습기간&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_eduStart);mailForm.append("&nbsp;~&nbsp;");mailForm.append(v_eduEnd);mailForm.append("</font>");
			mailForm.append("<br>");
			mailForm.append("</td>");
			mailForm.append("</tr>");
			mailForm.append("</table>");
			mailForm.append("<br>");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka 바로가기</a></center>");
			mailForm.append("<br><br>");
			mailForm.append("<b>e-Eureka&nbsp;&nbsp;운영자</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='7%'></td></tr>");
			mailForm.append("</table>");
			mailForm.append("<td></tr><tr><td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table>");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-Eureka] 수강취소 되었습니다.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);
			
							
			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			

			if(isOk==1){//발송이 성공하면...
				//서버로그에 남긴다.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getSession("userid"));
			}else{//발송이 실패하면
				//서버로그에 남긴다.
				System.out.println("\n email send ===========>  failed,\n userId===========>"+box.getSession("userid"));
			}
			//데이터베이스 이메일로그에 정보를 저장하기 위해 box에 정보를 추가 저장한다.
			box.put("p_receiverName", v_userName);// 수신자 이름
			box.put("p_email",p_toEmail );//수신자 이메일
			box.put("p_userid", box.getSession("userid"));//수신자 아이디
			box.put("p_mailTitle", p_mailTitle);//이메일 제목
			box.put("p_mailContent", p_mailContent);//메일 내용
			box.put("p_senderEmail", p_fromEmail);// 발신자 이메일
			box.put("p_senderName", p_fromName); // 발신자 이름
			box.put("p_status", new Integer(isOk));// 발신 성공여부
		
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
	

	
	//수강 신청 시
	public int sendRegisterCoursesMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//메일서버
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//보내는 이 이름
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//보내는 이 이메일
		String p_toEmail   = box.getSession("email"); //받는 이 이메일
		String p_mailTitle   = box.getStringDefault("p_title", "제목없음");// 이메일 제목
		String p_mailContent = box.getStringDefault("p_content", "내용없음");//이메일 내용
		String v_userName=box.getSession("name");
		StringBuffer mailForm = null;
		System.out.println("p_mailServer===========>"+p_mailServer);
		System.out.println("p_fromName=============>"+p_fromName);
		System.out.println("p_fromEmail============>"+p_fromEmail);

		String v_subj = box.getString("p_subj");// 과목 코드
		String v_subjnm =null;// 과목명
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
			
			// 사용자에게 보낼 메시지를 작성한다.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0 >  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr>  ");
			mailForm.append("<td width=7%></td>  ");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>  ");
			mailForm.append(v_userName);mailForm.append("님, e-Eureka입니다.<br> 『 <font style='color=#0033ff'> ");
			mailForm.append(v_subjnm);mailForm.append("</font> 』 과정이 수강신청이 되었으며 승인대기 중입니다.<br>  ");
			mailForm.append("교육담당자의 승인 후 수강이 가능하십니다.<br><br>  ");
			mailForm.append("<b>수강신청과정</b><br>  ");
			mailForm.append("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#cccccc' >  ");
			mailForm.append("<tr>  ");
			mailForm.append("<td valign='middle' style='font-weight:bold;font-size:9pt;line-height:150%;color:#5c5c5c;word-break:break-all; padding-left: 5px;padding-top: 5px;padding-bottom: 5px;'>  ");
			mailForm.append("과&nbsp;정&nbsp;명&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_subjnm);		
			mailForm.append("</font><br> ");
			mailForm.append("학습기간&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_eduStart);mailForm.append("&nbsp;~&nbsp;");mailForm.append(v_eduEnd);mailForm.append("</font>");
			mailForm.append("<br> ");
			mailForm.append("</td> ");
			mailForm.append("</tr> ");
			mailForm.append("</table> ");
			mailForm.append("<br> ");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka 바로가기</a></center>");
			mailForm.append("<br><br> ");
			mailForm.append("<b>e-Eureka&nbsp;&nbsp;운영자</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='7%'></td></tr> ");
			mailForm.append("</table> ");
			mailForm.append("<td></tr><tr> <td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table> ");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-Eureka] 수강신청이 되었습니다.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);
			
			
			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			
			
			box.put("p_userName", v_userName);// 수신자 이름
			box.put("p_email",p_toEmail );//수신자 이메일
			box.put("p_userid", box.getSession("userid"));//수신자 아이디
			
			if(isOk==1){//발송이 성공하면...
				//서버로그에 남긴다.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getSession("userid"));
			}else{//발송이 실패하면
				//서버로그에 남긴다.
				System.out.println("\n email send ===========>  failed,\n userId===========>"+box.getSession("userid"));
			}
			//데이터베이스 이메일로그에 정보를 저장하기 위해 box에 정보를 추가 저장한다.
			box.put("p_receiverName", v_userName);// 수신자 이름
			box.put("p_email",p_toEmail );//수신자 이메일
			box.put("p_userId", box.getSession("userid"));//수신자 아이디
			box.put("p_receiverEmail", box.getSession("userid"));//수신자 아이디
			box.put("p_mailTitle", p_mailTitle);//이메일 제목
			box.put("p_mailContent", p_mailContent);//메일 내용
			box.put("p_senderEmail", p_fromEmail);// 발신자 이메일
			box.put("p_senderName", p_fromName); // 발신자 이름
			box.put("p_status", new Integer(isOk));// 발신 성공여부
			
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
///////////////////////////////////////////////////////학습자가 신청하고 취소하는 경우 끝////////////////////////////////////////	
	
	
	
///////////////////////////////////////////////////////운영자가 승인하고 반려(거절)하는 경우 시작////////////////////////////////////////	
	//수강 신청 승인 시
	public int sendAcceptRegisterCoursesMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//메일서버
		
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//보내는 이 이름
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//보내는 이 이메일
		String p_toEmail   = box.getString("p_email"); //받는 이 이메일
		String p_mailTitle   = box.getStringDefault("p_title", "제목없음");// 이메일 제목
		String p_mailContent = box.getStringDefault("p_content", "내용없음");//이메일 내용
		StringBuffer mailForm = null;
		String v_userName=box.getString("p_userName");
		
		System.out.println("p_mailServer===========>"+p_mailServer);
		System.out.println("p_fromName=============>"+p_fromName);
		System.out.println("p_fromEmail============>"+p_fromEmail);
		
		

		String v_subj = box.getString("p_subj");// 과목 코드
		String v_subjnm =null;// 과목명
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
			
			// 사용자에게 보낼 메시지를 작성한다.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0 >  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr>  ");
			mailForm.append("<td width=7%></td>  ");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>  ");
			mailForm.append(v_userName);mailForm.append("님, e-Eureka입니다.<br> 『 <font style='color=#0033ff'> ");
			mailForm.append(v_subjnm);mailForm.append("</font> 』 과정이  승인되었습니다.<br>  ");
			mailForm.append("수강 신청한 과정을 충분히 학습하시어 좋은 성과 얻으시길 바랍니다.<br><br>  ");
			mailForm.append("<b>수강승인과정</b><br>  ");
			mailForm.append("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#cccccc' >  ");
			mailForm.append("<tr>  ");
			mailForm.append("<td valign='middle' style='font-weight:bold;font-size:9pt;line-height:150%;color:#5c5c5c;word-break:break-all; padding-left: 5px;padding-top: 5px;padding-bottom: 5px;'>  ");
			mailForm.append("과&nbsp;정&nbsp;명&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_subjnm);		
			mailForm.append("</font><br> ");
			mailForm.append("학습기간&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_eduStart);mailForm.append("&nbsp;~&nbsp;");mailForm.append(v_eduEnd);mailForm.append("</font>");
			mailForm.append("<br> ");
			mailForm.append("</td> ");
			mailForm.append("</tr> ");
			mailForm.append("</table> ");
			mailForm.append("<br> ");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka 바로가기</a></center>");
			mailForm.append("<br><br> ");
			mailForm.append("<b>e-Eureka&nbsp;&nbsp;운영자</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='7%'></td></tr> ");
			mailForm.append("</table> ");
			mailForm.append("<td></tr><tr> <td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table> ");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-Eureka] 수강신청내역이 승인되었습니다.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);

			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			
			if(isOk==1){//발송이 성공하면...
				//서버로그에 남긴다.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}else{//발송이 실패하면
				//서버로그에 남긴다
				System.out.println("\n email send ===========>  failed,\n userId===========>"+box.getString("p_userid"));
			}
			//데이터베이스 이메일로그에 정보를 저장하기 위해 box에 정보를 추가 저장한다.
			box.put("p_receiverName", v_userName);// 수신자 이름
			box.put("p_email",p_toEmail );//수신자 이메일
			box.put("p_mailTitle", p_mailTitle);//이메일 제목
			box.put("p_mailContent", p_mailContent);//메일 내용
			box.put("p_senderEmail", p_fromEmail);// 발신자 이메일
			box.put("p_senderName", p_fromName); // 발신자 이름
			box.put("p_status", new Integer(isOk));// 발신 성공여부
			
			
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
	
	//수강 신청 반려
	public int sendRejectRegisterCoursesMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//메일서버
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//보내는 이 이름
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//보내는 이 이메일
		String p_toEmail   = box.getString("p_email"); //받는 이 이메일
		String p_mailTitle   = box.getStringDefault("p_title", "제목없음");// 이메일 제목
		String p_mailContent = box.getStringDefault("p_content", "내용없음");//이메일 내용
		String v_userName=box.getString("p_userName");
		StringBuffer mailForm = null;
		
		
		System.out.println("p_mailServer===========>"+p_mailServer);
		System.out.println("p_fromName=============>"+p_fromName);
		System.out.println("p_fromEmail============>"+p_fromEmail);
	

		String v_subj = box.getString("p_subj");// 과목 코드
		String v_subjnm =null;// 과목명
		String v_eduStart=null;
		String v_eduEnd=null;
		String v_year=box.getString("p_year");//년도
		String v_reason = box.getStringDefault("p_reason","운영자 미승인");//반려이유
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
			
			// 사용자에게 보낼 메시지를 작성한다.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0> ");
			mailForm.append("<tr>");
			mailForm.append("<td width=7%></td>");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>");
			mailForm.append(v_userName);mailForm.append("님,안녕하세요 e-Eureka입니다.<br> 수강신청하신『 <font style='color=#0033ff'>");
			mailForm.append(v_subjnm);mailForm.append("</font>』 과정이 반려되었습니다.<br>");
			mailForm.append("자세한 내용은 사내 교육담당자에게 문의바랍니다<br><br>");
			mailForm.append("<b>수강반려과정</b><br>");
			mailForm.append("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#cccccc' >");
			mailForm.append("<tr>");
			mailForm.append("<td valign=middle style='font-weight:bold;font-size:9pt;line-height:150%;color:#5c5c5c;word-break:break-all; padding-left: 5px;padding-top: 5px;padding-bottom: 5px;'>");
			mailForm.append("과&nbsp;정&nbsp;명&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_subjnm);		
			mailForm.append("</font><br> ");
			mailForm.append("학습기간&nbsp;:&nbsp;<font style='color=#0033ff'>");mailForm.append(v_eduStart);mailForm.append("&nbsp;~&nbsp;");mailForm.append(v_eduEnd);mailForm.append("</font>");
			mailForm.append("<br>");
			mailForm.append("반려사유  :<font style='color=#0033ff'>");mailForm.append(v_reason);
			mailForm.append("</font></td>");
			mailForm.append("</tr>");
			mailForm.append("</table>");
			mailForm.append("<br>");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka 바로가기</a></center>");
			mailForm.append("<br><br>");
			mailForm.append("<b>e-Eureka&nbsp;&nbsp;운영자</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='7%'></td></tr>");
			mailForm.append("</table>");
			mailForm.append("<td></tr><tr><td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table>");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-Eureka] 수강신청내역이 반려되었습니다.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);
		
			
			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			

			if(isOk==1){//발송이 성공하면...
				//서버로그에 남긴다.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}else{//발송이 실패하면
				//서버로그에 남긴다.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}
			//데이터베이스 이메일로그에 정보를 저장하기 위해 box에 정보를 추가 저장한다.
			box.put("p_receiverName", v_userName);// 수신자 이름
			box.put("p_email",p_toEmail );//수신자 이메일
			box.put("p_mailTitle", p_mailTitle);//이메일 제목
			box.put("p_mailContent", p_mailContent);//메일 내용
			box.put("p_senderEmail", p_fromEmail);// 발신자 이메일
			box.put("p_senderName", p_fromName); // 발신자 이름
			box.put("p_status", new Integer(isOk));// 발신 성공여부
			
			
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
///////////////////////////////////////////////////////운영자가 승인하고 반려(거절)하는 경우 끝////////////////////////////////////////
	
	
	public int sendSubjQnaStudyMail(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		 Mailing mailing=null;
		 EmailLog emailLog=null;
		conf = new ConfigSet();
		PreparedStatement pstmt = null;
		ListSet ls=null;
		
		int isOk = 0;
		String sql = "";
		
		String p_mailServer  = conf.getProperty("mail.server");//메일서버
		String p_fromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));//보내는 이 이름
		String p_fromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));//보내는 이 이메일
		String p_toEmail   = box.getString("p_email"); //받는 이 이메일
		String p_mailTitle   = box.getStringDefault("p_title", "제목없음");// 이메일 제목
		String p_mailContent = box.getStringDefault("p_content", "내용없음");//이메일 내용
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
			// 사용자에게 보낼 메시지를 작성한다.
			mailForm=new StringBuffer();
			mailForm.append("<table border=0 width=720 border=0 cellspacing=0 cellpadding=0>  ");
			mailForm.append("<tr> <td><img src=http://61.111.12.165/images/user/mail/free_top.gif></td> </tr>  ");
			mailForm.append("<tr> <td background=http://61.111.12.165/images/user/mail/free_bg.gif>  ");
			mailForm.append("<table width=100% border=0 cellspacing=0 cellpadding=0> ");
			mailForm.append("<tr>");
			mailForm.append("<td width=12%></td>");
			mailForm.append("<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>");
			mailForm.append(box.getSession("name")+"님,안녕하세요 e-Eureka입니다.<br> "+box.getSession("name")+"님께서 Q&A에 올리신 글이 접수되었습니다.<br>");
			mailForm.append("궁금하신 점이 빨리 해결될 수 있도록 신속히 처리하도록 하겠습니다.<br><br>");
			mailForm.append("<br>즐거운 하루 보내세요!<br>감사합니다.");
			mailForm.append("<br><br>");
			mailForm.append("<center><a href='http://www.e-eureka.co.kr' target='_blank'>e-Eureka 바로가기</a></center>");
			mailForm.append("<br><br>");
			mailForm.append("<b>[e-유레카 문의]</b><br> ");
			mailForm.append("E-mail <a href='mailto:eduadmin@aekyung.kr'>eduadmin@aekyung.kr</a><br>");
			mailForm.append("TEL&nbsp;&nbsp;&nbsp;&nbsp;02-2627-8540<br>");
			mailForm.append("<td>  <td width='12%'></td></tr>");
			mailForm.append("</table>");
			mailForm.append("<td></tr><tr><td><img src='http://61.111.12.165/images/user/mail/free_bottom.gif'></td></tr>");
			mailForm.append("</table>");
			
			
			mailing = Mailing.getInstance();
			p_mailContent=mailing.makeHtml(mailForm.toString());
			p_mailTitle="[e-유레카] Q&A가 접수되었습니다.";
			System.out.println("p_mailTitle================>"+p_mailTitle);
			System.out.println("p_mailContent================>"+p_mailContent);
		
			
			isOk =mailing.send(p_mailServer, p_fromEmail, p_fromName, p_toEmail, p_mailTitle, p_mailContent);
			

			if(isOk==1){//발송이 성공하면...
				//서버로그에 남긴다.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}else{//발송이 실패하면
				//서버로그에 남긴다.
				System.out.println("\n email send ===========> success,\n userId===========>"+box.getString("p_userid"));
			}
			//데이터베이스 이메일로그에 정보를 저장하기 위해 box에 정보를 추가 저장한다.
			box.put("p_receiverName", v_userName);// 수신자 이름
			box.put("p_email",p_toEmail );//수신자 이메일
			box.put("p_mailTitle", p_mailTitle);//이메일 제목
			box.put("p_mailContent", p_mailContent);//메일 내용
			box.put("p_senderEmail", p_fromEmail);// 발신자 이메일
			box.put("p_senderName", p_fromName); // 발신자 이름
			box.put("p_status", new Integer(isOk));// 발신 성공여부
			
			
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
