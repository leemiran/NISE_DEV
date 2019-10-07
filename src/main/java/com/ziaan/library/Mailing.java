package com.ziaan.library;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import javax.activation.DataHandler;
/**
 * javaMail을 이용하여 편리하게 이메일을 발송할 수 있는 유틸리티
 *
 */
public class Mailing {
	
	private volatile static Mailing mailing;
	
	private Mailing(){}
	
	/**
	 * Mailing 클래스의 인스턴스를 생성해주는 메소드
	 * @return
	 */
	public static Mailing getInstance(){
		if(mailing == null){
			synchronized(Mailing.class){
				if(mailing == null){
					mailing = new Mailing();
				}
			}
		}
		return mailing;
	}
	
	/**
	 * 사용되는 빈에서 각각의 정보를 받아 이메일을 발송한다.
	 * @param p_mailServer 	이메일서버
	 * @param p_fromEmail  	발송자 이메일
	 * @param p_fromName   	발송자 이름
	 * @param p_toEmail    	수신자 이메일
	 * @param p_mailTitle  	이메일 제목
	 * @param p_mailContent 이메일 내용
	 * @return 				success(발송 성공시 1, 실패시 0을 반환한다)
	 * @throws Exception
	 */
    public int send(String p_mailServer, String p_fromEmail, String p_fromName, String p_toEmail, String p_mailTitle, String p_mailContent) throws Exception{
    	
    	int success = 0;	
    	
	    String v_mailServer  = p_mailServer;
	    String v_fromEmail   = p_fromEmail;     //보내는  인간
	    String v_fromName    = p_fromName;      //보내는 인간 이름 
	    String v_toEmail     = p_toEmail;       //받는 인간
	    String v_mailTitle   = p_mailTitle;
	    String v_mailContent = p_mailContent;
	  
	    
	    Properties prop = new Properties();
	    Session session = Session.getInstance(prop,null); //인증 없음!
	    MimeMessage message = new MimeMessage(session);
	  
	    MimeBodyPart mbp = new MimeBodyPart();   
	    Multipart mp = new MimeMultipart();
	  
	    try {
	        message.setFrom(new InternetAddress(v_fromEmail, v_fromName, "EUC-KR"));
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(v_toEmail));
		    message.setSubject(MimeUtility.encodeText(v_mailTitle, "EUC-KR","B")); //제목
		    message.setHeader("Content-Type","text/html;charset=euc-kr");
		    message.setSentDate(new Date());
		    mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(v_mailContent, "text/html; charset=euc-kr")));
		  
		    mp.addBodyPart(mbp);
		    message.setContent(mp);	
		  
		    Transport transport = session.getTransport("smtp");
		    transport.connect(v_mailServer, "", "");
		    transport.sendMessage(message, message.getAllRecipients());
		    transport.close();
		    success = 1;
		    
	    } catch(Exception e) {
	        message.setFrom(new InternetAddress(v_fromName+"<"+v_fromEmail+">"));
	        success = 0;
	        //message.setHeader("FROM","\"" + name + "\"<" + from + " > ");
	    }
	    return success;
    }
        
    public String makeHtml(String str){
    	StringBuffer strb = new StringBuffer();
    	for(int i=0; i<str.length(); i++){
    		if(str.charAt(i)=='\n') strb.append("<BR>");
    		else                    strb.append(str.charAt(i));
    	}
    	return strb.toString();
    }
} //end
