package egovframework.adm.sms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.utl.fcc.service.EgovStringUtil;

@Repository("SMSSenderDAO")
public class SMSSenderDAO {

	/** log */
	protected Log log = LogFactory.getLog(this.getClass());

	
	/**
    데이콤 문자발송 sql insert문 : Ms-Sql버젼임
    @param String to, String from, String msg
    @return boolean
    */
	public boolean dacomSmsSender(Map<String, Object> commandMap) throws Exception 
	{
		
		boolean isok = false;
		
		Connection conn = null; 
		PreparedStatement   pstmt   = null;
		String smsServer   = EgovProperties.getProperty("sms.admin.server");
		String smsDb   = EgovProperties.getProperty("sms.admin.database");
		String smsUser   = EgovProperties.getProperty("sms.admin.user");
		String smsPwd   = EgovProperties.getProperty("sms.admin.pwd");
		String fromHp   = EgovProperties.getProperty("knise.admin.comptel");
		
		
		if(commandMap.containsKey("hometel") && commandMap.get("hometel") != null && !"".equals(commandMap.get("hometel"))){
			fromHp = commandMap.get("hometel")+"";
		}
		
	
//		String url = "jdbc:sqlserver://114.141.28.199:1433;databaseName=EPASSDB;user=smsuser;password=smsuser;";
		
		//오라클
		//String url = "jdbc:oracle:thin:@" + smsServer + ":1521:orcl";
		
		//티베로
		//String url = "jdbc:tibero:thin:@" + smsServer + ":8629:tibero";
		
		//mysql
		String url = "jdbc:mysql://" + smsServer + ":3306/lgsms";
		
		System.out.println("sms url ----> " + url);
		
		
		String content = (String)commandMap.get("content");
		String[] toHp = EgovStringUtil.getStringSequence(commandMap, "p_handphone");
		String smsMms = commandMap.get("smsMms") != null ? commandMap.get("smsMms").toString() : "sms";
		String p_subject = commandMap.get("p_subject") != null ? commandMap.get("p_subject").toString() : "국립특수교육원";
		fromHp = "041-537-1476";
		String sql = "";
		
		log.info("SMS Server Url : " + url);
//		db.driver=oracle.jdbc.driver.OracleDriver
//		db.url=jdbc:oracle:thin:@127.0.0.1:1521:orcl
//		db.user=smsuser
//		db.pass=smsuser
		try {
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			//오라클
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//티베로
			//Class.forName("com.tmax.tibero.jdbc.TbDriver");
			
			//MYSQL
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, smsUser, smsPwd);
			
			/*
			'SMS Parameter : Dacom SMS 매뉴얼에서 발췌
			'TR_SENDDATE: 송신할 날짜를 나타냅니다.
			'TR_SENDSTAT: 메시지의 상태를 ‘0’, ‘1’, ‘2’로 나타내어 ‘0’이면 메시지의 입력상태, ‘1’이면 메시지 송신 상태, ‘2’이면 메시지의 결과 상태를 나타냅니다.
			'TR_RSLTSTAT: 메시지의 수신 후  성공 및 실패 여부 등의 결과 값을 나타냅니다.
			'TR_MSGTYPE: 0이면 일반적인 문자가 들어가며, 1이면 URL과 함께 송신 됩니다.
			'TR_PHONE: 수신자 전화 번호를 나타냅니다.
			'TR_CALLBACK: 송신자 전화 번호를 나타냅니다.
			'TR_RSLDATE: 수신된 메시지의 날짜를 나타냅니다.
			'TR_MODIFIED: 데이터의 갱신된 날짜를 나타냅니다.
			'TR_MSG: 데이터의 메시지를 나타냅니다. (URL 콜백시 url과 메시지 사이에 ‘0B’값을 넣습니다.)
			*/
			
			
			if(toHp != null)
			{
				for( int i=0; i<toHp.length; i++ )
				{
					if(!smsMms.equals("mms")){
						if(toHp[i] != null && !"".equals(toHp[i]))
						{
							sql = 	" insert into SC_TRAN (	\n";
							//sql += 	"		TR_NUM,	\n";
							sql += 	"		TR_SENDDATE,	\n";
							sql += 	"		TR_SENDSTAT,	\n";
							sql += 	"		TR_MSGTYPE,	\n";
							sql += 	"		TR_PHONE,	\n";
							sql += 	"		TR_CALLBACK,	\n";
							sql += 	"		TR_MSG	\n";
							sql += 	"	) values (	\n";
							//sql += 	"		SC_TRAN_SEQ.NEXTVAL,	\n";
							sql += 	"		NOW(),	\n";
							sql += 	"		'0',	\n";
							sql += 	"		'0',	\n";
							sql += 	"		replace(?, '-', ''),	\n";
							sql += 	"		replace(?, '-', ''),	\n";
							sql += 	"		?	\n";
							sql += 	"	)	\n";
							
							
							log.info("SMS Sql : " + sql);
							
							pstmt = conn.prepareStatement(sql);
							int index = 1;
							pstmt.setString(index++, toHp[i]);
							pstmt.setString(index++, fromHp);
							pstmt.setString(index++, content);
							pstmt.executeUpdate();
							pstmt.close();
							
						}
					}else{ 
						
						sql = 	" insert into MMS_MSG ( 	\n";
					//	sql += 	" MSGKEY,	\n";
						sql += 	" SUBJECT,	\n";
						sql += 	" PHONE,	\n";
						sql += 	" CALLBACK,	\n";
						sql += 	" STATUS,	\n";
						sql += 	" REQDATE,	\n";
						sql += 	" MSG) 	\n";
						sql += 	" values(	\n";
					//	sql += 	" MMS_MSG_SEQ.nextval,	\n";
						sql += 	" ?,	\n";
						sql += 	" replace(?, '-', ''),	\n";
						sql += 	" replace(?, '-', ''),	\n";
						sql += 	" '0',	\n";
						sql += 	" NOW(),	\n";
						sql += 	" ?	\n";
						sql += 	" ) 	\n";						
						
						log.info("MMS Sql : " + sql);
						
						pstmt = conn.prepareStatement(sql);
						int index = 1;
						pstmt.setString(index++, p_subject);
						pstmt.setString(index++, toHp[i]);
						pstmt.setString(index++, fromHp);
						pstmt.setString(index++, content);
						pstmt.executeUpdate();
						pstmt.close();
					}
					
					
					
				}
				
				conn.close();
			
				isok = true;
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
		}
		finally 
		{
	        if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
	        if ( conn != null ) { try { conn.close(); } catch ( Exception e10 ) { } }
		}
		
		return isok;
	}
}
