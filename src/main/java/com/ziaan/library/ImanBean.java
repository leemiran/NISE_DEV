package com.ziaan.library;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;
import java.util.Vector;

public class ImanBean {
	
    /**
     SMS 전송
     @param box      receive from the form object and session
     @return isOk    전송횟수
     */
    public int sendIman(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
    	int isOk = 0;
    	StringBuffer sbSQL = null;

    	try {
			connMgr = new DBConnectionManager();          
			connMgr.setAutoCommit(false);
			
			sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_IMANSEND_LOG  \n");
            sbSQL.append("      (SEND_SEQ  \n");
            sbSQL.append("     , SUSERID  \n");
            sbSQL.append("     , RUSERID  \n");
            sbSQL.append("     , TITLE  \n");
            sbSQL.append("     , SCONTENT  \n");
            sbSQL.append("     , SDATE)  \n");
            sbSQL.append("SELECT NVL(MAX(SEND_SEQ), 0) + 1  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
//            sbSQL.append("     , EMPTY_CLOB()  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("FROM   TZ_IMANSEND_LOG  \n");

			pstmt = connMgr.prepareStatement(sbSQL.toString());
			
    		Vector v_to = box.getVector("to");
    		String v_schecks = "";
    		String v_title = box.getString("p_title");
    		
			for(int i = 0 ; i < v_to.size() ; i++) {
				v_schecks = (String)v_to.elementAt(i);
				
				String v_userid = "";
				String v_name = "";
				String v_email = "";
				String v_subjnm = "";
				String v_edustart = "";
				String v_eduend = "";
				String v_tstep = "";
				String v_study_time = "";
				
				StringTokenizer v_token = new StringTokenizer(v_schecks, "|");

				while(v_token.hasMoreTokens()) {
					v_userid = v_token.nextToken();
					if(v_token.hasMoreTokens()) {
						v_name = v_token.nextToken();
					}
					if(v_token.hasMoreTokens()) {
						v_email = v_token.nextToken();
					}
					if(v_token.hasMoreTokens()) {
						v_subjnm = v_token.nextToken();
					}
					if(v_token.hasMoreTokens()) {
						v_edustart = v_token.nextToken();
					}
					if(v_token.hasMoreTokens()) {
						v_eduend = v_token.nextToken();
					}
					if(v_token.hasMoreTokens()) {
						v_tstep = v_token.nextToken();
					}
					if(v_token.hasMoreTokens()) {
						v_study_time = v_token.nextToken();
					}
				}
				
	    		String v_msg = box.getString("p_content");
	    		String v_msg_head = box.getString("p_msg_head");
	    		String v_msg_tail = box.getString("p_msg_tail");
	    		
				v_msg = v_msg_head + v_msg + v_msg_tail;
				
				// 대체문자 처리
				v_msg = StringManager.replace(v_msg, "{name}", v_name);
				v_msg = StringManager.replace(v_msg, "{email}", v_email);
				v_msg = StringManager.replace(v_msg, "{subjnm}", v_subjnm);
				v_msg = StringManager.replace(v_msg, "{edustart}", v_edustart);
				v_msg = StringManager.replace(v_msg, "{eduend}", v_eduend);
				v_msg = StringManager.replace(v_msg, "{tstep}", v_tstep);
				v_msg = StringManager.replace(v_msg, "{study_time}", v_study_time);
				
	    		// 줄바꿈 기호 및 특수문자처리
				v_msg = StringManager.replace(v_msg, "\n", "%0A");
				v_msg = StringManager.replace(v_msg, "%", "%25");
				v_msg = StringManager.replace(v_msg, "&", "%26");
				v_msg = StringManager.replace(v_msg, "=", "%3d");
				v_msg = StringManager.replace(v_msg, "|", "%7C");
				v_msg = StringManager.replace(v_msg, "+", "%2B");
				v_msg = StringManager.replace(v_msg, "?", "%3F");
	    		
	    		String msg0 = "cmd=04&svcName=" + v_title + "&empNo=" + v_userid + "&msg=" + v_msg + "";
	    		//msg0 = StringManager.korEncode(msg0);
	    		//System.out.println(box.getSession("userid") + ":" + sId + ":" + v_title + ":" + v_msg);
	    		
	    		PrintWriter os = null;
	    		Socket sock = new Socket("147.6.37.35", 17115);
	    		
	    		os = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()),true);
	    		os.println(msg0);
	    		sock.close();
	    		
				int index = 1;
				pstmt.setString(index++, box.getSession("userid"));
				pstmt.setString(index++, v_userid);
				pstmt.setString(index++, v_title);
				pstmt.setString(index++, v_msg);
				
				isOk = pstmt.executeUpdate();
				
	            sbSQL.setLength(0);
	            sbSQL.append("SELECT SCONTENT  \n");
	            sbSQL.append("FROM   TZ_IMANSEND_LOG  \n");
	            sbSQL.append("WHERE  SEND_SEQ = (SELECT MAX(SEND_SEQ) FROM TZ_IMANSEND_LOG)  \n");
//	            connMgr.setOracleCLOB(sbSQL.toString(), v_msg);
			}
			if(isOk > 0) {
				if(connMgr != null) { try { connMgr.commit(); }catch (Exception e10) {} }
			} else {
				if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }
			}
		} catch (Exception ex) {
			if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }
			ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
			throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
		} finally {
			if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
    	return isOk;
    }
}