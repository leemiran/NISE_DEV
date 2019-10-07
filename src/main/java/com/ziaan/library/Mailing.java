package com.ziaan.library;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import javax.activation.DataHandler;
/**
 * javaMail�� �̿��Ͽ� ���ϰ� �̸����� �߼��� �� �ִ� ��ƿ��Ƽ
 *
 */
public class Mailing {
	
	private volatile static Mailing mailing;
	
	private Mailing(){}
	
	/**
	 * Mailing Ŭ������ �ν��Ͻ��� �������ִ� �޼ҵ�
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
	 * ���Ǵ� �󿡼� ������ ������ �޾� �̸����� �߼��Ѵ�.
	 * @param p_mailServer 	�̸��ϼ���
	 * @param p_fromEmail  	�߼��� �̸���
	 * @param p_fromName   	�߼��� �̸�
	 * @param p_toEmail    	������ �̸���
	 * @param p_mailTitle  	�̸��� ����
	 * @param p_mailContent �̸��� ����
	 * @return 				success(�߼� ������ 1, ���н� 0�� ��ȯ�Ѵ�)
	 * @throws Exception
	 */
    public int send(String p_mailServer, String p_fromEmail, String p_fromName, String p_toEmail, String p_mailTitle, String p_mailContent) throws Exception{
    	
    	int success = 0;	
    	
	    String v_mailServer  = p_mailServer;
	    String v_fromEmail   = p_fromEmail;     //������  �ΰ�
	    String v_fromName    = p_fromName;      //������ �ΰ� �̸� 
	    String v_toEmail     = p_toEmail;       //�޴� �ΰ�
	    String v_mailTitle   = p_mailTitle;
	    String v_mailContent = p_mailContent;
	  
	    
	    Properties prop = new Properties();
	    Session session = Session.getInstance(prop,null); //���� ����!
	    MimeMessage message = new MimeMessage(session);
	  
	    MimeBodyPart mbp = new MimeBodyPart();   
	    Multipart mp = new MimeMultipart();
	  
	    try {
	        message.setFrom(new InternetAddress(v_fromEmail, v_fromName, "EUC-KR"));
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(v_toEmail));
		    message.setSubject(MimeUtility.encodeText(v_mailTitle, "EUC-KR","B")); //����
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
