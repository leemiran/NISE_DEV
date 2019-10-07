package com.ziaan.system;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class AutoSystemBean {
	
    /**
	 * 자동화 리스트
	 * @param box          receive from the form object and session
	 * @return ArrayList   자동화 리스트
     **/
    public ArrayList selectAutoSystemList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT SEQ  \n");
            sbSQL.append("     , TITLE  \n");
            sbSQL.append("     , TYPECODE  \n");
            sbSQL.append("     , DECODE(TYPECODE, 'BAT', '실행(배치)', 'SMS', 'SMS', 'EML', 'E-Mail', 'GNG', '공지사항') AS TYPENM  \n");
            sbSQL.append("     , RUNHOUR  \n");
            sbSQL.append("     , RUNDATE  \n");
            sbSQL.append("     , LAST_RUNDATE  \n");
            sbSQL.append("     , LAST_RUNCNT  \n");
            sbSQL.append("     , USE_YN  \n");
            sbSQL.append("     , EXECTIME  \n");
            sbSQL.append("FROM   TZ_AUTOMATION  \n");
            sbSQL.append("WHERE  1 = 1  \n");
            if(!box.getStringDefault("s_typecode", "ALL").equals("ALL"))
            	sbSQL.append("AND    TYPECODE = " + SQLString.Format(box.getString("s_typecode")) + "  \n");
            if(!box.getString("p_search_text").equals(""))
            	sbSQL.append("AND    TITLE LIKE '%' || " + StringManager.makeSQL(box.getString("p_search_text")) + " || '%'  \n");
            sbSQL.append("ORDER BY SEQ  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 자동화 등록
	 * @param box receive from the form object and session
	 * @return int 등록결과
     **/
    public int insertAutoSystemInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_AUTOMATION  \n");
            sbSQL.append("      (SEQ  \n");
            sbSQL.append("     , TITLE  \n");
            sbSQL.append("     , SUMMARY  \n");
            sbSQL.append("     , TYPECODE  \n");
            sbSQL.append("     , RUNHOUR  \n");
            sbSQL.append("     , RUNDATE  \n");
            sbSQL.append("     , QUERYSTRING1  \n");
            sbSQL.append("     , QUERYSTRING2  \n");
            sbSQL.append("     , QUERYSTRING3  \n");
            sbSQL.append("     , LOGNUM  \n");
            sbSQL.append("     , SEND_TITLE  \n");
            sbSQL.append("     , SEND_MESSAGE  \n");
            sbSQL.append("     , MATCH_TEXT1  \n");
            sbSQL.append("     , MATCH_CUL1  \n");
            sbSQL.append("     , MATCH_TEXT2  \n");
            sbSQL.append("     , MATCH_CUL2  \n");
            sbSQL.append("     , MATCH_TEXT3  \n");
            sbSQL.append("     , MATCH_CUL3  \n");
            sbSQL.append("     , MATCH_TEXT4  \n");
            sbSQL.append("     , MATCH_CUL4  \n");
            sbSQL.append("     , MATCH_TEXT5  \n");
            sbSQL.append("     , MATCH_CUL5  \n");
            sbSQL.append("     , JOBEXCUTEYN  \n");
            sbSQL.append("     , ISNOW  \n");
            sbSQL.append("     , USE_YN  \n");
            sbSQL.append("     , LUSERID  \n");
            sbSQL.append("     , EXECTIME  \n");
            sbSQL.append("     , LDATE)  \n");
            sbSQL.append("SELECT NVL(MAX(SEQ), 0) + 1  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
//            sbSQL.append("     , EMPTY_CLOB()  \n");
//            sbSQL.append("     , EMPTY_CLOB()  \n");
//            sbSQL.append("     , EMPTY_CLOB()  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("FROM   TZ_AUTOMATION  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_title"));
            pstmt.setString(index++, box.getString("p_summary"));
            pstmt.setString(index++, box.getString("p_typecode"));
            pstmt.setString(index++, box.getString("p_runhour"));
            pstmt.setString(index++, box.getString("p_rundate"));
            
            pstmt.setString(index++, box.getString("p_querystring1"));
            pstmt.setString(index++, box.getString("p_querystring2"));
            pstmt.setString(index++, box.getString("p_querystring3"));
            
            pstmt.setString(index++, box.getString("p_lognum"));
          //  pstmt.setString(index++, box.getString("p_send_message"));
            pstmt.setString(index++, box.getString("p_send_title"));
            pstmt.setCharacterStream(index++, new StringReader(box.getString("p_send_message")),  box.getString("p_send_message").length() );
            pstmt.setString(index++, box.getString("p_match_text1"));
            pstmt.setString(index++, box.getString("p_match_cul1"));
            pstmt.setString(index++, box.getString("p_match_text2"));
            pstmt.setString(index++, box.getString("p_match_cul2"));
            pstmt.setString(index++, box.getString("p_match_text3"));
            pstmt.setString(index++, box.getString("p_match_cul3"));
            pstmt.setString(index++, box.getString("p_match_text4"));
            pstmt.setString(index++, box.getString("p_match_cul4"));
            pstmt.setString(index++, box.getString("p_match_text5"));
            pstmt.setString(index++, box.getString("p_match_cul5"));
            pstmt.setString(index++, "N");  //JOBEXCUTEYN
            pstmt.setString(index++, "N");  //ISNOW
            pstmt.setString(index++, box.getString("p_use_yn"));
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, "00000000");
            isOk = pstmt.executeUpdate();
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT QUERYSTRING1  \n");
            sbSQL.append("FROM   TZ_AUTOMATION  \n");
            sbSQL.append("WHERE  SEQ = (SELECT MAX(SEQ) FROM TZ_AUTOMATION)  \n");
//            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_querystring1"));
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT QUERYSTRING2  \n");
            sbSQL.append("FROM   TZ_AUTOMATION  \n");
            sbSQL.append("WHERE  SEQ = (SELECT MAX(SEQ) FROM TZ_AUTOMATION)  \n");
//            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_querystring2"));
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT QUERYSTRING3  \n");
            sbSQL.append("FROM   TZ_AUTOMATION  \n");
            sbSQL.append("WHERE  SEQ = (SELECT MAX(SEQ) FROM TZ_AUTOMATION)  \n");
//            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_querystring3"));

            if(isOk > 0) {
                connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 자동화 정보
	 * @param box receive from the form object and session
	 * @return DataBox 자동화 정보
     **/
    public DataBox selectAutoSystemInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	int v_seq = box.getInt("p_seq");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT SEQ  \n");
            sbSQL.append("     , TITLE  \n");
            sbSQL.append("     , SUMMARY  \n");
            sbSQL.append("     , TYPECODE  \n");
            sbSQL.append("     , RUNHOUR  \n");
            sbSQL.append("     , RUNDATE  \n");
            sbSQL.append("     , QUERYSTRING1  \n");
            sbSQL.append("     , QUERYSTRING2  \n");
            sbSQL.append("     , QUERYSTRING3  \n");
            sbSQL.append("     , LOGNUM  \n");
            sbSQL.append("     , SEND_TITLE  \n");
            sbSQL.append("     , SEND_MESSAGE  \n");
            sbSQL.append("     , MATCH_TEXT1  \n");
            sbSQL.append("     , MATCH_CUL1  \n");
            sbSQL.append("     , MATCH_TEXT2  \n");
            sbSQL.append("     , MATCH_CUL2  \n");
            sbSQL.append("     , MATCH_TEXT3  \n");
            sbSQL.append("     , MATCH_CUL3  \n");
            sbSQL.append("     , MATCH_TEXT4  \n");
            sbSQL.append("     , MATCH_CUL4  \n");
            sbSQL.append("     , MATCH_TEXT5  \n");
            sbSQL.append("     , MATCH_CUL5  \n");
            sbSQL.append("     , USE_YN  \n");
            sbSQL.append("FROM   TZ_AUTOMATION  \n");
            sbSQL.append("WHERE  SEQ = " + SQLString.Format(v_seq) + "  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return dbox;
    }
    
    /**
	 * 자동화 수정
	 * @param box receive from the form object and session
	 * @return int 등록결과
     **/
    public int updateAutoSystemInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_AUTOMATION  \n");
            sbSQL.append("SET    TITLE = ?  \n");
            sbSQL.append("     , SUMMARY = ?  \n");
            sbSQL.append("     , TYPECODE = ?  \n");
            sbSQL.append("     , RUNHOUR = ?  \n");
            sbSQL.append("     , RUNDATE = ?  \n");
            sbSQL.append("     , QUERYSTRING1 = ?  \n");
            sbSQL.append("     , QUERYSTRING2 = ?  \n");
            sbSQL.append("     , QUERYSTRING3 = ?  \n");
//            sbSQL.append("     , QUERYSTRING1 = EMPTY_CLOB()  \n");
//            sbSQL.append("     , QUERYSTRING2 = EMPTY_CLOB()  \n");
//            sbSQL.append("     , QUERYSTRING3 = EMPTY_CLOB()  \n");
            sbSQL.append("     , LOGNUM = ?  \n");
            sbSQL.append("     , SEND_TITLE = ?  \n");
            sbSQL.append("     , SEND_MESSAGE = EMPTY_CLOB()  \n");
            sbSQL.append("     , MATCH_TEXT1 = ?  \n");
            sbSQL.append("     , MATCH_CUL1 = ?  \n");
            sbSQL.append("     , MATCH_TEXT2 = ?  \n");
            sbSQL.append("     , MATCH_CUL2 = ?  \n");
            sbSQL.append("     , MATCH_TEXT3 = ?  \n");
            sbSQL.append("     , MATCH_CUL3 = ?  \n");
            sbSQL.append("     , MATCH_TEXT4 = ?  \n");
            sbSQL.append("     , MATCH_CUL4 = ?  \n");
            sbSQL.append("     , MATCH_TEXT5 = ?  \n");
            sbSQL.append("     , MATCH_CUL5 = ?  \n");
            sbSQL.append("     , USE_YN = ?  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  SEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_title"));
            pstmt.setString(index++, box.getString("p_summary"));
            pstmt.setString(index++, box.getString("p_typecode"));
            pstmt.setString(index++, box.getString("p_runhour"));
            pstmt.setString(index++, box.getString("p_rundate"));
            
            pstmt.setString(index++, box.getString("p_querystring1"));
            pstmt.setString(index++, box.getString("p_querystring2"));
            pstmt.setString(index++, box.getString("p_querystring3"));
            
            pstmt.setString(index++, box.getString("p_lognum"));
            pstmt.setString(index++, box.getString("p_send_title"));
 //           pstmt1.setCharacterStream(6, new StringReader(v_content), v_content.length() );
            //pstmt.setCharacterStream(index++, new StringReader(box.getString("p_send_message")),  box.getString("p_send_message").length() );
            
            pstmt.setString(index++, box.getString("p_match_text1"));
            pstmt.setString(index++, box.getString("p_match_cul1"));
            pstmt.setString(index++, box.getString("p_match_text2"));
            pstmt.setString(index++, box.getString("p_match_cul2"));
            pstmt.setString(index++, box.getString("p_match_text3"));
            pstmt.setString(index++, box.getString("p_match_cul3"));
            pstmt.setString(index++, box.getString("p_match_text4"));
            pstmt.setString(index++, box.getString("p_match_cul4"));
            pstmt.setString(index++, box.getString("p_match_text5"));
            pstmt.setString(index++, box.getString("p_match_cul5"));
            pstmt.setString(index++, box.getString("p_use_yn"));
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setInt(index++, box.getInt("p_seq"));
            isOk = pstmt.executeUpdate();
            
//            sbSQL.setLength(0);
//            sbSQL.append("SELECT QUERYSTRING1  \n");
//            sbSQL.append("FROM   TZ_AUTOMATION  \n");
//            sbSQL.append("WHERE  SEQ = " + SQLString.Format(box.getInt("p_seq")) + "  \n");
//            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_querystring1"));
            
//            sbSQL.setLength(0);
//            sbSQL.append("SELECT QUERYSTRING2  \n");
//            sbSQL.append("FROM   TZ_AUTOMATION  \n");
//            sbSQL.append("WHERE  SEQ = " + SQLString.Format(box.getInt("p_seq")) + "  \n");
//            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_querystring2"));
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT SEND_MESSAGE  \n");
            sbSQL.append("FROM   TZ_AUTOMATION  \n");
            sbSQL.append("WHERE  SEQ = " + box.getInt("p_seq") + "  \n");
            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_send_message"));

            if(isOk > 0) {
                connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
}