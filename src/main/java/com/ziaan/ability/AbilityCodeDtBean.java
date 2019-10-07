// **********************************************************
//  1. 제      목: 역량 분류
//  2. 프로그램명: AbilityCodeDtBean.java
//  3. 개      요: 역량 분류
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: jang dong jin 2007.12.10
//  7. 수      정:
// **********************************************************
package com.ziaan.ability;

import java.util.*;
import java.sql.*;
import com.ziaan.library.*;
import com.ziaan.ability.*;

public class AbilityCodeDtBean {

    public AbilityCodeDtBean() { }


    /**
    상세 조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public DataBox select(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet							ls				= null;
        String							sql				= "";

        String v_gubuncd 	= box.getString("p_gubuncd");
        String v_gubuncddt 	= box.getString("p_gubuncddt");

        try {
            connMgr     = new DBConnectionManager();

            sql  = "SELECT 	GUBUNCD, GUBUNCDDT, GUBUNCDDTNM, INDATE, INUSERID, LDATE, LUSERID, DESCS  	";
            sql += "FROM		TZ_ABILITY_CODE_DT																			";
            sql += "WHERE		GUBUNCD		= '"+ v_gubuncd +"'																				";
            sql += "AND			GUBUNCDDT	= '"+ v_gubuncddt +"'																			";
            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                dbox = ls.getDataBox();
            }
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) {  try { ls.close();  } catch ( Exception e ) { }  }
            if ( connMgr != null ) {  try {  connMgr.freeConnection();  } catch (Exception e ) { } }
        }

        return dbox;
    }

    /**
    등록할때
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int insert(RequestBox box) throws Exception {
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        int isOk = 0;
        String v_gubun = "";

        String v_gubuncd 			= box.getString("p_gubuncd");
        String v_gubuncddt 		= box.getString("p_gubuncddt");
        String v_gubuncddtnm 	= box.getString("p_gubuncddtnm");
        String s_userid 			= box.getSession("userid");
        String v_descs          = box.getString("p_descs");
        
        String v_subj           = box.getString("p_subjcd");
        
        try {
           connMgr = new DBConnectionManager();

           sql  = "SELECT NVL(MAX(GUBUNCDDT), 1000000) + 1 AS GUBUNCDDT FROM TZ_ABILITY_CODE_DT WHERE GUBUNCD = '"+ v_gubuncd +"'  ";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) {
               v_gubun = ls.getString("GUBUNCDDT");
           }

           sql1 =  "INSERT INTO TZ_ABILITY_CODE_DT(GUBUNCD, GUBUNCDDT, GUBUNCDDTNM, INDATE, INUSERID, LDATE, LUSERID, DESCS,  SUBJ)   ";
           sql1 += " VALUES (?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, ?, ?)	";

           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setString(1, v_gubuncd);
           pstmt.setString(2, v_gubun);
           pstmt.setString(3, v_gubuncddtnm);
           pstmt.setString(4, s_userid);
           pstmt.setString(5, s_userid);
           pstmt.setString(6, v_descs);
           pstmt.setString(7, v_subj);

           isOk = pstmt.executeUpdate();
           
           if ( pstmt != null ) { pstmt.close(); }
           
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    수정하여 저장할때
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int update(RequestBox box) throws Exception {
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_gubuncd 			= box.getString("p_gubuncd");
        String v_gubuncddt 		= box.getString("p_gubuncddt");
        String v_gubuncddtnm 	= box.getString("p_gubuncddtnm");
        String s_userid 			= box.getSession("userid");
        String v_descs          = box.getString("p_descs");
        
        try {
            connMgr = new DBConnectionManager();

            sql  = "UPDATE 	TZ_ABILITY_CODE_DT 																	";
            sql += "SET			GUBUNCDDTNM	= ? 	,																	";
            sql += "				LUSERID			= ? 	, 																";
            sql += "				DESCS			= ? 	, 																";
            sql += "				LDATE 			= TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') 	";
            sql += "WHERE		GUBUNCD			= ?                          						";
            sql += "AND			GUBUNCDDT		= ?                          						";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_gubuncddtnm);
            pstmt.setString(2, s_userid);
            pstmt.setString(3, v_descs);
            pstmt.setString(4, v_gubuncd);
            pstmt.setString(5, v_gubuncddt);

            isOk = pstmt.executeUpdate();
            
            if ( pstmt != null ) { pstmt.close(); }
            
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }


    /**
    삭제할때
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int delete(RequestBox box) throws Exception {
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        String sql = "";

        int isOk1 = 0;

        String v_gubuncd 			= box.getString("p_gubuncd");
        String v_gubuncddt 		= box.getString("p_gubuncddt");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = "DELETE 	TZ_ABILITY_CODE_DT 																	";
            sql += "WHERE		GUBUNCD			= ?                          						";
            sql += "AND			GUBUNCDDT		= ?                          						";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_gubuncd);
            pstmt.setString(2, v_gubuncddt);

            isOk1 = pstmt.executeUpdate();
            
            if ( pstmt != null ) { pstmt.close(); }

            if ( isOk1 > 0 )
            	connMgr.commit();
            else
            	connMgr.rollback();

        } catch ( Exception ex ) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
        		connMgr.setAutoCommit(true);
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }


}
