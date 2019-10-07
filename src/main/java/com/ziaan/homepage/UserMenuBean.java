package com.ziaan.homepage;

import java.sql.PreparedStatement;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

public class UserMenuBean {
	
    /**
	 * 사용자 메뉴 정보
	 * @param box          receive from the form object and session
	 * @return DataBox   	사용자 메뉴 정보
     **/
    public DataBox selectUserMenuInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	int v_topmenu = box.getInt("p_topmenu");
    	int v_leftmenu = box.getInt("p_leftmenu");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT TOPMENU  \n");
            sbSQL.append("     , LEFTMENU  \n");
            sbSQL.append("     , MENUNM  \n");
            sbSQL.append("     , SERVLETURL  \n");
            sbSQL.append("     , PROCESS  \n");
            sbSQL.append("     , PARAM  \n");
            sbSQL.append("FROM   TZ_USERMENU  \n");
            sbSQL.append("WHERE  TOPMENU = " + SQLString.Format(v_topmenu) + "  \n");
            sbSQL.append("AND    LEFTMENU = " + SQLString.Format(v_leftmenu) + "  \n");
    		
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
	 * 사용자 홈페이지메뉴 로그등록
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int insertUserMenueLog(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_USERMENU_LOG  \n");
            sbSQL.append("      (LOG_SEQ  \n");
            sbSQL.append("     , TOPMENU  \n");
            sbSQL.append("     , LEFTMENU  \n");
            sbSQL.append("     , USERID  \n");
            sbSQL.append("     , LDATE)  \n");
            sbSQL.append("SELECT USERMENU_LOG_SEQ.NEXTVAL  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("FROM   DUAL  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, box.getInt("p_topmenu"));
            pstmt.setInt(index++, box.getInt("p_leftmenu"));
            pstmt.setString(index++, box.getSession("userid"));
            isOk = pstmt.executeUpdate();

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
	 * 사용자 블로그메뉴 로그등록
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int insertBlogMenueLog(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("\n INSERT INTO TZ_USERMENU_LOG  					");
            sbSQL.append("\n       (LOG_SEQ 								");
            sbSQL.append("\n      , TOPMENU  								");
            sbSQL.append("\n      , LEFTMENU  								");
            sbSQL.append("\n      , USERID  								");
            sbSQL.append("\n      , LDATE)  								");
            sbSQL.append("\n select USERMENU_LOG_SEQ.NEXTVAL				");
            sbSQL.append("\n     , TOPMENU  								");
            sbSQL.append("\n     , LEFTMENU  								");
            sbSQL.append("\n     , ?  										");
            sbSQL.append("\n     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  	");
            sbSQL.append("\n FROM  TZ_USERMENU  							");
            sbSQL.append("\n WHERE SERVLETURL = ? 							");
            sbSQL.append("\n   AND PROCESS = ?	 							");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getString("v_url"));
            pstmt.setString(index++, box.getString("v_process"));
            isOk = pstmt.executeUpdate();

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