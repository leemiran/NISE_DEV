// **********************************************************
//  1. 제      목: SMS 발송
//  2. 프로그램명: SmsBean.java
//  3. 개      요: SMS 발송
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2005. 8. 1
//  7. 수      정:  2005. 8. 1
// **********************************************************
package com.ziaan.library   ;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;

import com.ziaan.library.smsLog.model.ArreoSmsLogBean;

/**
 * SMS 발송
 *
 * @date   : 2003. 5
 * @author : s.j. Jung
 */
public class SmsSendBean {
	private SMSProcessor smsProcessor = null;
	static int counter=0;
	int cnt=0;

    public SmsSendBean() {
    	smsProcessor = SMSProcessor.getInstance();
    }
    
    public void setSMS(Object[] smsinfos) {
    	smsProcessor.sendSMS(smsinfos);
    }


    public String addZeros(String value, int len) {
     int i;

     value = "" + value;

     if (value.length() < len) {
      for (i=0; i<(len- value.length()); i++)
       value = "0" + value;
     }

     return value;
     
    } 
    
    
    /**
     SMS 전송
     @param box      receive from the form object and session
     @return isOk    전송횟수
     */
    public int sendSms(RequestBox box) throws Exception {
    	
    	
    	
    	
    	
    	int isOk = 1;
        Vector v_vchecks = box.getVector("p_checks");
        Vector v_vhandphone = box.getVector("p_handphone");
        Vector v_names = box.getVector("p_names");
        String v_msg = box.getString("p_title");
        String single_tel = box.getString("single_tel");
        String v_sender_tel = box.getStringDefault("p_sender_tel", box.getSession("handphone"));

    	SmsBean[] sbs = null;
    	String tmp_msg = v_msg;

    	try {
    		// SmsBean처리
    		if(single_tel != null && !"".equals(single_tel) && !"null".equals(single_tel)) {
    			sbs = new SmsBean[1];
    			sbs[0] = new SmsBean();
    			sbs[0].emp_id = "-";
    			sbs[0].emp_nm = "개인";
    			sbs[0].tel = single_tel.replaceAll("-", "");
    			sbs[0].message = v_msg;
    		} else {
				sbs = new SmsBean[(v_vhandphone.size() + 1)];

				for(int i = 0 ; i < sbs.length - 1 ; i++) {
					sbs[i] = new SmsBean();
					sbs[i].emp_id = "" + v_vchecks.elementAt(i);
					sbs[i].tel = ("" + v_vhandphone.elementAt(i)).replaceAll("-", "");

					if(tmp_msg.indexOf("%") > -1) {
						v_msg = tmp_msg;
						v_msg = StringManager.replace(v_msg, "%name%", "" + v_names.elementAt(i));
					}
					sbs[i].message = v_msg;
				}
				sbs[sbs.length - 1] = new SmsBean();
				sbs[sbs.length - 1].emp_id = box.getSession("userid");
				sbs[sbs.length - 1].emp_nm = box.getSession("name");
				sbs[sbs.length - 1].tel = v_sender_tel;

				if(tmp_msg.indexOf("%") > -1) {
					v_msg = tmp_msg;
					v_msg = StringManager.replace(v_msg, "%name%", box.getSession("name"));
				}
				sbs[sbs.length - 1].message = v_msg;
    		}

    		// SMS발송
    		//SmsRepository sr = SmsRepository.getInstance();

            SmsSendBean scm = new SmsSendBean();
            scm.setSMS(new Object[]{sbs, v_sender_tel, "", ""});
        //    boolean test= sendSMSMsg(single_tel, v_sender_tel,  v_msg, box);
            
            
    	} catch(Exception e) {
    		isOk = 0;
    		e.printStackTrace();
    		throw new Exception("SmsCommand:"+e.getMessage());
    	}
    	
    	/*
        int isOk = 0;
        boolean isSend = false;
        Vector v_vchecks = box.getVector("p_checks");
        Vector v_vhandphone = box.getVector("p_handphone");
        String v_schecks = "";
        String v_shandphone = "";
        String v_msg = box.getString("p_title");

        // 발신번호 properites 에서 가지고 오기
        ConfigSet conf = new ConfigSet();
        String p_fromPhone = conf.getProperty("sms.admin.comptel");
        
        try {
            for(int i = 0 ; i < v_vchecks.size() ; i++ ) {
                v_schecks = (String)v_vchecks.elementAt(i);
                v_shandphone = (String)v_vhandphone.elementAt(i);
                isSend = this.sendSMSMsg(v_shandphone, p_fromPhone, v_msg);
                
                if(isSend)
                    isOk++;
            }
        } catch(Exception e) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
        }
        */
        return isOk;
    }
    
    public int sendSMSMsg(RequestBox box) throws Exception {
    	
    	Connection	con  = null;
    	Statement	stmt = null;
    	ResultSet	rs	 = null;
    	ResultSet   rs1  = null;
    	DBConnectionManager connMgr = null;
    	PreparedStatement pstmt = null;
    	PreparedStatement pstmt1=null;
    	
    	StringBuffer sbSQL = null;
    	ArrayList    list  = null;
    	boolean      isOk  = false;
    	int proccnt = 0;
    	int idx     = 1;
    	
    	Vector v_userid       = box.getVector("p_checks");     //받는사람 ID
    	Vector v_handphone    = box.getVector("p_handphone");  //받는사람번호
    	
    	String v_seq = "";
    	String v_handphone1  = box.getString("p_sender_tel").equals("") ? "--" : box.getString("p_sender_tel");  //보내는사람
    	String v_title       = box.getString("p_title");
    	String vs_userid     = "";
    	String vs_handphone = ""; //받는 사람
    	
    	String v_reserved    = box.getString("p_reserved");
    	String v_CMP_MSG_ID    =null;
    	String v_writeDateTime=null;
    	String url  = "jdbc:oracle:thin:@172.16.1.51:1521:akis01"; 
    	String id   = "arreo_sms";
    	String pass = "akissms";
    	SmsLogBean logBean=new SmsLogBean();
    	int v_tabseq=logBean.getMaxTabseq();
    	int temp = v_userid.size();
    	try {
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		con = DriverManager.getConnection(url,id,pass);
    		
    		connMgr = new DBConnectionManager();
    		
    		for(int i=0; i < v_userid.size(); i++){
    			
    			stmt = con.createStatement(); 
    			sbSQL = new StringBuffer();
    			
    			sbSQL.append(" select NVL(MAX(substr(CMP_MSG_ID,15,6))+1, 100001) seq from ARREO_SMS ");
    			
    			rs   = stmt.executeQuery(sbSQL.toString());
    			if(rs.next()){
    				v_seq = this.addZeros(rs.getString(1),6);
    			}
    			
    			stmt.close();
    			
    			sbSQL = new StringBuffer();
    			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
    			String date=sdf.format(new Date());
    			v_CMP_MSG_ID=date+v_seq;
    			vs_userid      = (String) v_userid.elementAt(i);
    			vs_handphone  = (String)  v_handphone.elementAt(i);
    			if(v_reserved.equals("")){
    				v_reserved=date;	
    			}
    			
    			sbSQL.append("INSERT INTO ARREO_SMS  \n")
    			.append("(CMP_MSG_ID, CMP_USR_ID, CALLBACK, ODR_FG, SMS_GB, USED_CD, MSG_GB, WRT_DTTM, SND_DTTM, SND_PHN_ID, RCV_PHN_ID , SND_MSG, SMS_ST			 \n")
    			.append(" )                                        \n")
    			.append( "VALUES                                   \n")
    			.append(" (                                        \n")
    			.append(v_CMP_MSG_ID+"\n")
    			.append("     ,    ?                                \n")
    			.append("     ,    ?                                \n")
    			.append("     ,    ?                                \n")
    			.append("     ,    ?                                \n")
    			.append("     ,    ?                                \n")
    			.append("     ,    ?                                \n")
    			.append("     ,"+date+"       \n");
    			sbSQL.append("     ,  ?     \n");
    			sbSQL.append("     ,    ?                                \n")
    			.append("     ,    ?                                \n")
    			.append("     ,    ?                                \n")
    			.append("     ,    ?                                \n")
    			.append(" )                                         \n");
    			
    			idx = 1; 
    			//pstmt = connMgr.prepareStatement(sbSQL.toString()); 
    			pstmt = con.prepareStatement(sbSQL.toString()); 
    			pstmt.setString(idx++, "00000");
    			pstmt.setString(idx++,v_handphone1);
    			pstmt.setString(idx++,"2");
    			pstmt.setString(idx++,"1");
    			pstmt.setString(idx++,"00");
    			pstmt.setString(idx++, "A");
    			pstmt.setString(idx++, v_reserved);
    			pstmt.setString(idx++, v_handphone1);
    			pstmt.setString(idx++, vs_handphone);
    			pstmt.setString(idx++, v_title);
    			pstmt.setString(idx++, "0");
    			
    			proccnt = pstmt.executeUpdate();
    			System.out.println("proccnt= "+proccnt);
    			
    			
    			
    			
    			if(proccnt > 0){
    				isOk = true;
    			}else{
    				isOk = false;
    			}
    			box.put("p_tabseq", new Integer(v_tabseq));//그룹seq
    			box.put("p_userid", vs_userid);//사용자 ID
    			box.put("p_sendPhoneId", v_handphone1);//보내는 사람 전화번호
    			box.put("p_receiverPhoneId", vs_handphone);//받는 사람 전화번호
    			box.put("p_sendMessage", v_title);//보내는 메시지
    			box.put("p_writeDateTime", date);//쓴 날짜
    			box.put("p_sendDateTime", v_reserved);//전송할 날짜 시간
    			box.put("p_status", new Integer(proccnt));//성공시 1, 실패시 0
    			box.put("p_CMP_MSG_ID", v_CMP_MSG_ID);//sms 기본키
    			
				logBean.insertSmsLog(box);
    			
    		}
    		
    	} catch(SQLException e) {
    		ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
    		throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
    	} catch ( Exception e ) {
    		ErrorManager.getErrorStackTrace(e);
    		throw new Exception("\n sendSMSMsg Exception Occured e.getMessage() : [\n" + e.getMessage() + "\n]");
    	} finally {
    		if(rs != null){
    			try{
    				rs.close();
    			}catch(Exception e) { }
    		}
    		if(stmt != null){
    			try{
    				stmt.close();
    			}catch(Exception e) { }
    		}
    		if(pstmt != null) {
    			try {
    				pstmt.close();
    			} catch(Exception e) { }
    		}
    		if(connMgr != null) {
    			try {
    				connMgr.freeConnection();
    			} catch ( Exception e ) { }
    		}
    	}
    	return proccnt;
    } 
    
    
    public int sendSMSMsg2(RequestBox box) throws Exception {
       	
        Connection	con  = null;
        Statement	stmt = null;
        ResultSet	rs	 = null;
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        
        StringBuffer sbSQL = null;
        ArrayList    list  = null;
        boolean      isOk  = false;
        int proccnt = 0;
        int idx     = 1;
        
        //Vector v_userid       = box.getVector("p_checks");     //받는사람 ID
        //Vector v_handphone    = box.getVector("p_handphone");  //받는사람번호
        
        String v_seq = "";
        String v_handphone1  = box.getString("p_sender_tel").equals("") ? "--" : box.getString("p_sender_tel");  //보내는사람
        String v_title       = box.getString("p_title");
        String vs_handphone  = box.getString("p_handphone"); //받는 사람
        
        String url  = "jdbc:oracle:thin:@172.16.1.51:1521:akis01"; 
        String id   = "arreo_sms";
        String pass = "akissms";
        
        try {
        	Class.forName("oracle.jdbc.driver.OracleDriver");
 			con = DriverManager.getConnection(url,id,pass);
            
           	
            	stmt = con.createStatement(); 
            	sbSQL = new StringBuffer();
            	
            	sbSQL.append(" select NVL(MAX(substr(CMP_MSG_ID,15,6))+1, 100001) seq from ARREO_SMS ");
            	
            	rs   = stmt.executeQuery(sbSQL.toString());
            	if(rs.next()){
            	    v_seq = this.addZeros(rs.getString(1),6);
            	}
            	
            	stmt.close();
            	
            	sbSQL = new StringBuffer();
            	
                
 	             sbSQL.append("INSERT INTO ARREO_SMS  \n")
 	            	  .append("(CMP_MSG_ID, CMP_USR_ID, CALLBACK, ODR_FG, SMS_GB, USED_CD, MSG_GB, WRT_DTTM, SND_DTTM, SND_PHN_ID, RCV_PHN_ID , SND_MSG, SMS_ST			 \n")
 	            	  .append(" )                                        \n")
 	            	  .append( "VALUES                                   \n")
 	            	  .append(" (                                        \n")
 	                  .append("   to_char(sysdate,'yyyymmddhh24miss') || "+v_seq+"  \n")
 	                  .append("     ,    ?                                \n")
 	                  .append("     ,    ?                                \n")
 	                  .append("     ,    ?                                \n")
 	                  .append("     ,    ?                                \n")
 	                  .append("     ,    ?                                \n")
 	                  .append("     ,    ?                                \n")
 	                  .append("     ,  to_char(sysdate,'yyyymmddhh24miss')     \n")
 	                  .append("     ,  to_char(sysdate,'yyyymmddhh24miss')     \n")
 	                  .append("     ,    ?                                \n")
 	                  .append("     ,    ?                                \n")
 	                  .append("     ,    ?                                \n")
 	                  .append("     ,    ?                                \n")
 	                  .append(" )                                         \n");
 	
 	            idx = 1; 
 	            pstmt = con.prepareStatement(sbSQL.toString()); 
 	            
 	            pstmt.setString(idx++, "00000");
 	            pstmt.setString(idx++,v_handphone1);
 	            pstmt.setString(idx++,"2");
 	            pstmt.setString(idx++,"1");
 	            pstmt.setString(idx++,"00");
 	            pstmt.setString(idx++, "A");
 	            pstmt.setString(idx++, v_handphone1);
 	            pstmt.setString(idx++, vs_handphone);
 	            pstmt.setString(idx++, v_title);
 	            pstmt.setString(idx++, "0");
 	            
 	            proccnt = pstmt.executeUpdate();
 	            System.out.println("proccnt= "+proccnt);
 	            pstmt.close();
            
            
           
        } catch(SQLException e) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
        	   if(rs != null){
        		   try{
        		   	   rs.close();
        		   }catch(Exception e) { }
        	   }
        	   if(stmt != null){
        		   try{
        			   stmt.close();
        		   }catch(Exception e) { }
        	   }
            if(pstmt != null) {
                try {
                    pstmt.close();
                } catch(Exception e) { }
            }
            if(connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }
        return proccnt;
    }    
    
        
    public ArrayList sendUserList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox = null;
        ListSet ls = null;
        ArrayList list = null;
        StringBuffer sbSQL = new StringBuffer("");
        String where_query = "";
        Vector v_vchecks = box.getVector("p_checks");
        
        if(box.getVector("p_checks") == null)
            v_vchecks = box.getVector("p_checks_bl");
        
        if(v_vchecks.size() == 0) {
            if(box.getString("p_checks").equals("") && !box.getString("p_checks_bl").equals(""))
                v_vchecks.add(box.getString("p_checks_bl"));
            else if(!box.getString("p_checks").equals("") && box.getString("p_checks_bl").equals(""))
                v_vchecks.add(box.getString("p_checks"));
        }
        
        String v_schecks = "";
        String v_union = "";
        boolean v_procunion = false;
        
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sbSQL.append(" SELECT   UserId                          \n")
                 .append("      ,   MAX(Name      )     Name        \n")
                 .append("      ,   MAX(Handphone )     Handphone   \n")
                 .append("      ,   MAX(Email     )     Email       \n")
                 .append(" FROM     (                               \n");
            where_query = "('xxxxxxxx'";
            
            if(box.getString("p_type").equals("ForBL")) {
                for(int i = 0 ; i < v_vchecks.size() ; i++) {
                    where_query = where_query + ", " + SQLString.Format(box.getString("p_userid" + (String)v_vchecks.elementAt(i)));
                    if(i != 0 && i % 500 == 0 ) {
                        where_query += ")";                        
                        sbSQL.append(" " + v_union + "                                ")
                             .append(" SELECT  userid                               \n")
                             .append("     ,   name                                 \n")
                             .append("     ,   handphone                            \n")
                             .append("     ,   email                                \n")
                             .append(" from    tz_member                            \n")
                             .append(" where   userid in " + where_query + "        \n")
                             .append("   and     NVL(issms, 'Y') = 'Y'                \n");
                        v_union     = " UNION ALL \n";
                        where_query = " ('xxxxxxxx'     ";
                        v_procunion = true;
                    } else {
                        v_procunion = false;
                    }
                }
            } else {
                for(int i = 0 ; i < v_vchecks.size() ; i++ ) {
                    v_schecks = (String)v_vchecks.elementAt(i);
                    
                    if ( v_schecks.indexOf(',') > 0)
                        v_schecks = StringManager.substring(v_schecks, 0, v_schecks.indexOf(','));
                    
                    where_query = where_query + "," + SQLString.Format(v_schecks.trim()) ;
                    
                    if(i != 0 && i % 500 == 0) {
                        where_query += ")";
                        sbSQL.append(" " + v_union + "                                ")
                             .append(" SELECT  userid                               \n")
                             .append("     ,   name                                 \n")
                             .append("     ,   handphone                            \n")
                             .append("     ,   email                                \n")
                             .append(" from    tz_member                            \n")
                             .append(" where   userid in " + where_query + "        \n")
                             .append("   and     NVL(issms, 'Y') = 'Y'                \n");
                        v_union     = " UNION ALL \n";
                        where_query = " ('xxxx'     ";
                        v_procunion = true;
                    } else {
                        v_procunion = false;
                    }
                }
            }
            if(!v_procunion) {
                where_query += ")";
                sbSQL.append(" " + v_union + "                                ")
                     .append(" SELECT  userid                               \n")
                     .append("     ,   name                                 \n")
                     .append("     ,   handphone                            \n")
                     .append("     ,   email                                \n")
                     .append(" from    tz_member                            \n")
                     .append(" where   userid in " + where_query + "        \n")
                     .append("   and     NVL(issms, 'Y') = 'Y'                \n");
            }
            sbSQL.append("          )       V                               \n")
                 .append(" GROUP BY V.UserId                                \n");

            ls  = connMgr.executeQuery(sbSQL.toString());
            while(ls.next()) {
                dbox    = ls.getDataBox();
                list.add(dbox);
            }
        } catch(SQLException e) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if(ls != null) {
                try {
                    ls.close();
                } catch(Exception e) { }
            }
            if(connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch(Exception e) { }
            }
        }
        return list;
    }
    
	/**
	 SMS 발송로그
	 @param box      receive from the form object and session
	 @return isOk    전송횟수
	
	 */
	public int insertSMSSendLog(String p_suserid, String p_sname, String p_shandphone, String p_rhandphone, String p_scontent) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		int isOk = 0;
		StringBuffer sbSQL = null;

		try {
			connMgr = new DBConnectionManager();          
			connMgr.setAutoCommit(false);

			sbSQL = new StringBuffer();			
			sbSQL.append("INSERT INTO TZ_SMSSEND_LOG  \n");
			sbSQL.append("      (SEND_SEQ  \n");
			sbSQL.append("     , SUSERID  \n");
			sbSQL.append("     , SNAME  \n");
			sbSQL.append("     , SHANDPHONE  \n");
			sbSQL.append("     , RHANDPHONE  \n");
			sbSQL.append("     , SCONTENT  \n");
			sbSQL.append("     , SDATE)  \n");
			sbSQL.append("     , SGUBUN)  \n");
			sbSQL.append("SELECT NVL(MAX(SEND_SEQ), 0) + 1  \n");
			sbSQL.append("     , ?  \n");
			sbSQL.append("     , ?  \n");
			sbSQL.append("     , ?  \n");
			sbSQL.append("     , ?  \n");
			sbSQL.append("     , ?  \n");
			sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
			sbSQL.append("     , ?  \n");
			sbSQL.append("FROM   TZ_SMSSEND_LOG  \n");

			pstmt = connMgr.prepareStatement(sbSQL.toString());
			int index = 1;
			pstmt.setString(index++, p_suserid);
			pstmt.setString(index++, p_sname);
			pstmt.setString(index++, p_shandphone);
			pstmt.setString(index++, p_rhandphone);
			pstmt.setString(index++, p_scontent);
			pstmt.setString(index++, "M");
			
			isOk = pstmt.executeUpdate();

			if(isOk > 0) {
				connMgr.commit();
			} else {
				connMgr.rollback();
			}
		} catch(SQLException e) {
			connMgr.rollback();
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
        	connMgr.rollback();
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
			if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return isOk;
	}
	
}