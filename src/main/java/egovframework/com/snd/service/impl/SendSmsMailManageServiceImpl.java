package egovframework.com.snd.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.ziaan.library.Mailing;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.snd.dao.MysqlSendSmsMailManageDAO;
import egovframework.com.snd.dao.SendSmsMailManageDAO;
import egovframework.com.snd.service.SendSmsMailManageService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("sendSmsMailManageService")
public class SendSmsMailManageServiceImpl extends EgovAbstractServiceImpl implements SendSmsMailManageService{
	
	/** log */
	protected static final Log log = LogFactory.getLog(SendSmsMailManageServiceImpl.class);
	
	@Resource(name="sendSmsMailManageDAO")
    private SendSmsMailManageDAO sendSmsMailManageDAO;
	
	@Resource(name="mysqlSendSmsMailManageDAO")
    private MysqlSendSmsMailManageDAO mysqlSendSmsMailManageDAO;
	
	
	

	public List getSendMemberInfo(Map<String, Object> commandMap) throws Exception{
		return sendSmsMailManageDAO.getSendMemberInfo(commandMap);
	}
	
	public int freeMailSend(Map<String, Object> commandMap) throws Exception{
		
		Mailing mailing = null;
		
		int isOk = 1;
		
		String mailServer = EgovProperties.getProperty("mail.server");
		String fromName   = (commandMap.get("name") != null ? commandMap.get("name") +"" : EgovProperties.getProperty("mail.admin.name"));
		String fromEmail   = (commandMap.get("email") != null ? commandMap.get("email") +"" : EgovProperties.getProperty("mail.admin.email"));
		String formNamaEmail = "\""+fromName+"\"<"+fromEmail+">";
		
		String title = (String)commandMap.get("p_title");
		String content = (String)commandMap.get("p_content");
		
		String[] toEmail = EgovStringUtil.getStringSequence(commandMap, "p_email");
		String[] toName = EgovStringUtil.getStringSequence(commandMap, "p_name");
		String[] toUserId = EgovStringUtil.getStringSequence(commandMap, "p_userid");
		
		String mailsql = "";
		String mailTo = "SSV:";
		for( int i=0; i<toEmail.length; i++ ){
			
			try{
				/*
				//send
				String v_email = toEmail[i];
				mailing = Mailing.getInstance();
				content = mailing.makeHtml(content);

				isOk = mailing.send(mailServer, fromEmail, fromName, v_email, title, content);
				*/
				mailTo += toEmail[i]+";";		
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		mailsql = mailTo;
		commandMap.put("formNamaEmail", formNamaEmail);
		commandMap.put("fromName", fromName);
		commandMap.put("fromEmail", fromEmail);
		commandMap.put("mailsql", mailsql);
		int cnt=0;

		sendSmsMailManageDAO.insertMemberGubunSendMail(commandMap);
		
		cnt=1;
		
		//return isOk;
		return cnt;
	}

	/*
	 * 발송자 핸드폰 번호 조회 
	 * */
	public Map<String, Object> getSendFromHandPhon(Map<String, Object> commandMap)throws Exception {
		return sendSmsMailManageDAO.getSendFromHandPhon(commandMap);
	}
	
	//회원구분별 인원수
	public Map<String, Object> selectMemberGubunCount(Map<String, Object> commandMap) throws Exception{
		return sendSmsMailManageDAO.selectMemberGubunCount(commandMap);
	}
	
	//회원구분별 메일 발송
	public int insertMemberGubunSendMail(Map<String, Object> commandMap) throws Exception{
		
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		
		Mailing mailing = null;
		
		int isOk = 1;
		
		String mailServer = EgovProperties.getProperty("mail.server");
		//String fromName   = (commandMap.get("name") != null ? commandMap.get("name") +"" : EgovProperties.getProperty("mail.admin.name"));
		//String fromEmail   = (commandMap.get("email") != null ? commandMap.get("email") +"" : EgovProperties.getProperty("mail.admin.email"));
		String fromName   = EgovProperties.getProperty("mail.admin.name");
		String fromEmail   = EgovProperties.getProperty("mail.admin.email");
		String formNamaEmail = "\""+fromName+"\"<"+fromEmail+">";
		
		
		String title = (String)commandMap.get("p_title");
		String content = (String)commandMap.get("p_content");		
		String t_emp_gubun = commandMap.get("t_emp_gubun") != null ? (String)commandMap.get("t_emp_gubun") : "";
		
		String p_emp_gubun = commandMap.get("p_emp_gubun") != null ? (String)commandMap.get("p_emp_gubun") : "";
		
		String p_send_kind = commandMap.get("p_send_kind").toString();
		
		
		String mailsql = "";
		System.out.println("p_send_kind----> "+p_send_kind);
		System.out.println("p_emp_gubun----> "+p_emp_gubun);
		
		if(p_send_kind.equals("1")){	//회원구분별 발송
			//테스트 
			//mailsql = "SQL:select email,name from tz_member where emp_gubun ='"+t_emp_gubun+"' and userid in( 'admin_test', 'admin') and ismailling = 'Y'";
			
			//실제발송
			mailsql = "SQL:select email,name from tz_member where emp_gubun ='"+t_emp_gubun+"' and ismailling = 'Y'";
		}else if(p_send_kind.equals("2")){	//선택회원 발송
			//선택		
			String [] arry_mailTo    = (String []) commandMap.get("_Array_p_key1");
						
			String mailTo = "SSV:";
			for(int i=0; arry_mailTo.length>i; i++ ){
				mailTo += arry_mailTo[i]+";";						
				
			}				 
			mailsql = mailTo;				
		}
		
		
		commandMap.put("formNamaEmail", formNamaEmail);
		commandMap.put("fromName", fromName);
		commandMap.put("fromEmail", fromEmail);
		commandMap.put("mailsql", mailsql);
		
		
		
		int cnt=0;
		/*
		 *직접발송
		List memberGubunSendMailList = sendSmsMailManageDAO.selectMemberGubunSendMailList(commandMap);
		//System.out.println("size ---------> "+memberGubunSendMailList.size());
		
		for( int i=0; i<memberGubunSendMailList.size(); i++ ){
			
			try{
				Map<String, Object> memberEmail = (Map)memberGubunSendMailList.get(i);
				String v_email = (String)memberEmail.get("email");
				
				System.out.println("mailServer -=-----> "+mailServer);
				System.out.println("fromEmail -=-----> "+fromEmail);
				System.out.println("fromName -=-----> "+fromName);
				System.out.println("v_email -=-----> "+v_email);
				System.out.println("title -=-----> "+title);
				System.out.println("content -=-----> "+content);
				
				
				mailing = Mailing.getInstance();
				content = mailing.makeHtml(content);
				if(v_email !=null && !"".equals(v_email)){	
					isOk = mailing.send(mailServer, fromEmail, fromName, v_email, title, content);
					//isOk = 1;
					if(i!=0 && i%100==0){
						Thread.currentThread().sleep(5000);
						
					}
					if(isOk == 1){
						cnt++;						
					}
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}*/
		
		sendSmsMailManageDAO.insertMemberGubunSendMail(commandMap);
		
		cnt=1;
		
		stopWatch.stop();

		commandMap.put("cnt", cnt);
		commandMap.put("totaltime", stopWatch.getTotalTimeSeconds());
		//메일폼 저장
		sendSmsMailManageDAO.insertMemberGubunSendMailForm(commandMap);
				
		return cnt;
	}
	
	public int updateNoMemberGubunSendMail(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			
			sendSmsMailManageDAO.updateNoMemberGubunSendMail(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		
		return isOk;
	}
	
	
	public int selectSendMailListCnt(Map<String, Object> commandMap) throws Exception{
		return mysqlSendSmsMailManageDAO.selectSendMailListCnt(commandMap);
	}
	
	/**
	 * 발송리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSendMailList(Map<String, Object> commandMap) throws Exception{
		System.out.println("impl -----> selectSendMailList ");
		return mysqlSendSmsMailManageDAO.selectSendMailList(commandMap);
	}
	
	//선택 회원 메일 발송
	public int insertMemberSearchMail(Map<String, Object> commandMap) throws Exception{
		
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		
		Mailing mailing = null;
		
		int isOk = 1;
		
		String mailServer = EgovProperties.getProperty("mail.server");
		//String fromName   = (commandMap.get("name") != null ? commandMap.get("name") +"" : EgovProperties.getProperty("mail.admin.name"));
		//String fromEmail   = (commandMap.get("email") != null ? commandMap.get("email") +"" : EgovProperties.getProperty("mail.admin.email"));
		String fromName   = EgovProperties.getProperty("mail.admin.name");
		String fromEmail   = EgovProperties.getProperty("mail.admin.email");
		String formNamaEmail = "\""+fromName+"\"<"+fromEmail+">";
		
		
		String title = (String)commandMap.get("p_title");
		String content = (String)commandMap.get("p_content");
		String p_emp_gubun = commandMap.get("p_emp_gubun").toString();
		//선택		
		String [] arry_mailTo    = (String []) commandMap.get("_Array_p_key1");
					
		String mailTo = "SSV:";
		for(int i=0; arry_mailTo.length>i; i++ ){
			mailTo += arry_mailTo[i]+";";						
			
		}
			 
		String mailsql = mailTo;
		
		
		commandMap.put("formNamaEmail", formNamaEmail);
		commandMap.put("fromName", fromName);
		commandMap.put("fromEmail", fromEmail);
		commandMap.put("mailsql", mailsql);
		
		
		
		int cnt=0;
		
		
		sendSmsMailManageDAO.insertMemberGubunSendMail(commandMap);
		
		cnt=1;
		
		stopWatch.stop();

		commandMap.put("cnt", cnt);
		commandMap.put("totaltime", stopWatch.getTotalTimeSeconds());
		//메일폼 저장
		sendSmsMailManageDAO.insertMemberGubunSendMailForm(commandMap);
				
		return cnt;
	}

	public void insertCloseUserSendMail(Map<String, Object> commandMap)
			throws Exception {
		// TODO Auto-generated method stub
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		
		Mailing mailing = null;
		
		int isOk = 1;
		
		String mailServer = EgovProperties.getProperty("mail.server");
		String fromName   = EgovProperties.getProperty("mail.admin.name");
		String fromEmail   = EgovProperties.getProperty("mail.admin.email");
		String formNamaEmail = "\""+fromName+"\"<"+fromEmail+">";
		
		
		String title = (String)commandMap.get("p_title");
		String content = (String)commandMap.get("p_content");		
		
		
		String mailsql = "";
		
			//테스트 
			//mailsql = "SQL:select email,name from tz_member where emp_gubun ='"+t_emp_gubun+"' and userid in( 'admin_test', 'admin') and ismailling = 'Y'";
			
			//실제발송
			//mailsql = "SQL:select email,name from tz_member where emp_gubun ='"+t_emp_gubun+"' and ismailling = 'Y'";
		//mailsql = "SQL:select 'jhyoon@hkinet.co.kr' as email , '윤진현' as name , 'neonar**' as userid, to_char(add_months(sysdate, 3)) as rdate from dual";
		
		
		mailsql = "SQL:select email, name, SUBSTR(USERID, 0, LENGTH(USERID)-2)||'**' as userid, to_char(add_months(sysdate, 3)) as rdate  FROM TZ_MEMBER WHERE indate is not null  and lglast is not null  and isretire = 'N' and dormant_yn = 'N' and ismailling = 'Y' and to_date(to_char(sysdate,'yyyymmdd')) - to_date(substr(lglast,1,8),'yyyymmddhh24miss') > 365";
		
		commandMap.put("formNamaEmail", formNamaEmail);
		commandMap.put("fromName", fromName);
		commandMap.put("fromEmail", fromEmail);
		commandMap.put("mailsql", mailsql);
		
		
		
		int cnt=0;
		
		if("dormant".equals(commandMap.get("pGubun"))){
			sendSmsMailManageDAO.insertMemberGubunDormantSendMail(commandMap);
		}else{
			//sendSmsMailManageDAO.insertMemberGubunDormantSendMail(commandMap);
			sendSmsMailManageDAO.insertMemberGubunSendMail(commandMap);
		}
		
		
		cnt=1;
		
		stopWatch.stop();

		commandMap.put("cnt", cnt);
		commandMap.put("totaltime", stopWatch.getTotalTimeSeconds());
		//메일폼 저장
		sendSmsMailManageDAO.insertMemberGubunSendMailForm(commandMap);
				
	}

	public List selectCloseUser(Map<String, Object> commandMap)
			throws Exception {
		return sendSmsMailManageDAO.selectCloseUser(commandMap);
	}

	public Map<String, Object> selectSendMailContent(Map<String, Object> commandMap) {
		return mysqlSendSmsMailManageDAO.selectSendMailContent(commandMap);
	}


}