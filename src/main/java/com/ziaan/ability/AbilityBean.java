// **********************************************************
//  1. 제      목: 역량 분류
//  2. 프로그램명: AbilityBean.java
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

public class AbilityBean { 

    public AbilityBean() { } 


	
    /**
    역량 분류 Select Box 조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectAbility(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        ArrayList			list		= null;
        String				sql			= "";

        String 				v_gubuncd		= box.getString("p_gubuncd");
        String 				v_gubuncddt		= box.getString("p_gubuncddt");

        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();

            sql  = "SELECT	ABILITY, ABILITYNM							";
            sql += "FROM		TZ_ABILITY											";
           	sql += "WHERE		GUBUNCD 	= '"+ v_gubuncd +"'		";
           	sql += "AND			GUBUNCDDT = '"+ v_gubuncddt +"'	";
            sql += "ORDER BY ORDERS ASC											";
           
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) {  try { ls.close();  } catch ( Exception e ) { }  }
            if ( connMgr != null ) {  try {  connMgr.freeConnection();  } catch (Exception e ) { } }
        }

        return list;
    }


    /**
    리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectListPage(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        ArrayList			list		= null;
        String				sql			= "";

        String 				v_gubuncd			= box.getString("p_gubuncd");
        String 				v_gubuncddt		= box.getString("p_gubuncddt");

        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();

            sql  = "SELECT	A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, A.ABILITYNM, A.DESCS, A.INDATE, 	";
            sql += "				B.GUBUNCDNM, C.GUBUNCDDTNM, A.ORDERS,																";
            sql += "				(SELECT COUNT(SUBJ) FROM TZ_ABILITY_SUBJ X WHERE A.GUBUNCD = X.GUBUNCD AND A.GUBUNCDDT = X.GUBUNCDDT AND A.ABILITY = X.ABILITY) AS SUBJCNT,	";
            sql += "				(SELECT COUNT(GUBUNCD) FROM TZ_ABILITY_PRO X WHERE A.GUBUNCD = X.GUBUNCD AND A.GUBUNCDDT = X.GUBUNCDDT AND A.ABILITY = X.ABILITY) AS SELCNT	";
            sql += "FROM		TZ_ABILITY A, TZ_ABILITY_CODE B, TZ_ABILITY_CODE_DT	C								";
            sql += "WHERE		A.GUBUNCD		= B.GUBUNCD																							";
            sql += "AND			A.GUBUNCD		= C.GUBUNCD																							";
            sql += "AND			A.GUBUNCDDT	= C.GUBUNCDDT																						";
            if (v_gubuncd != null && !v_gubuncd.equals("")){
            	sql += "AND		A.GUBUNCD 	= '"+ v_gubuncd +"'																			";
            }
            if (v_gubuncddt != null && !v_gubuncddt.equals("")){
            	sql += "AND		A.GUBUNCDDT = '"+ v_gubuncddt +"'																		";
            }

            sql += "ORDER BY A.GUBUNCD, A.GUBUNCDDT, A.ORDERS ASC																";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) {
	            dbox = ls.getDataBox();
	            list.add(dbox);
            }
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) {  try { ls.close();  } catch ( Exception e ) { }  }
            if ( connMgr != null ) {  try {  connMgr.freeConnection();  } catch (Exception e ) { } }
        }

        return list;
    }


    /**
    상세 조회
    @param box          receive from the form object and session
    @return DataBox
    */
    public DataBox select(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        String				sql			= "";

        String v_gubuncd 		= box.getString("p_gubuncd");
        String v_gubuncddt 	= box.getString("p_gubuncddt");
        String v_ability		= box.getString("p_ability");

        try {
            connMgr     = new DBConnectionManager();

            sql  = "SELECT	A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, A.ABILITYNM, A.DESCS, A.INDATE, 	";
            sql += "				B.GUBUNCDNM, C.GUBUNCDDTNM, A.ORDERS																";
            sql += "FROM		TZ_ABILITY A, TZ_ABILITY_CODE B, TZ_ABILITY_CODE_DT	C								";
            sql += "WHERE		A.GUBUNCD		= B.GUBUNCD																							";
            sql += "AND			A.GUBUNCD		= C.GUBUNCD																							";
            sql += "AND			A.GUBUNCDDT	= C.GUBUNCDDT																						";
          	sql += "AND			A.GUBUNCD 	= '"+ v_gubuncd +"'																			";
          	sql += "AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'																		";
          	sql += "AND			A.ABILITY 	= '"+ v_ability +"'																			";
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
        String v_ability		= "";

        String v_gubuncd 		= box.getString("p_gubuncd");
        String v_gubuncddt 	= box.getString("p_gubuncddt");
        String v_abilitynm 	= box.getString("p_abilitynm");
        int		 v_orders		 	= box.getInt("p_orders");
        String v_descs		 	= box.getString("p_descs");
        String s_userid 		= box.getSession("userid");

        try {
           connMgr = new DBConnectionManager();

           sql  = "SELECT NVL(MAX(ABILITY), 100000000) + 1 AS ABILITY FROM TZ_ABILITY WHERE GUBUNCD = '"+ v_gubuncd +"' AND GUBUNCDDT = '"+ v_gubuncddt +"'   ";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) {
               v_ability = ls.getString("ABILITY");
           }

           sql1 =  "INSERT INTO TZ_ABILITY(GUBUNCD, GUBUNCDDT, ABILITY, ABILITYNM, ORDERS, DESCS, INDATE, INUSERID, LDATE, LUSERID)   ";
           sql1 += " VALUES (?, ?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?)			";
           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setString(1, v_gubuncd);
           pstmt.setString(2, v_gubuncddt);
           pstmt.setString(3, v_ability);
           pstmt.setString(4, v_abilitynm);
           pstmt.setInt		(5, v_orders);
           pstmt.setString(6, v_descs);
           pstmt.setString(7, s_userid);
           pstmt.setString(8, s_userid);

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

        String v_gubuncd 		= box.getString("p_gubuncd");
        String v_gubuncddt 	= box.getString("p_gubuncddt");
        String v_ability 		= box.getString("p_ability");
        String v_abilitynm 	= box.getString("p_abilitynm");
        String v_orders 		= box.getString("p_orders");
        String v_descs 			= box.getString("p_descs");
        String s_userid    	= box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();

            sql  = "UPDATE 	TZ_ABILITY			 																		";
            sql += "SET			ABILITYNM		= ? 	,																	";
            sql += "				ORDERS			= ? 	,																	";
            sql += "				DESCS				= ? 	,																	";
            sql += "				LUSERID			= ? 	, 																";
            sql += "				LDATE 			= TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') 	";
            sql += "WHERE		GUBUNCD			= ?                          						";
            sql += "AND			GUBUNCDDT		= ?                          						";
            sql += "AND			ABILITY			= ?                          						";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_abilitynm);
						pstmt.setString(2, v_orders);
            pstmt.setString(3, v_descs);
            pstmt.setString(4, s_userid);
            pstmt.setString(5, v_gubuncd);
            pstmt.setString(6, v_gubuncddt);
            pstmt.setString(7, v_ability);

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
        PreparedStatement pstmt1 = null;
        String sql = "";

        int isOk1 = 0;

        String v_gubuncd 	= box.getString("p_gubuncd");
        String v_gubuncddt 	= box.getString("p_gubuncddt");
        String v_ability 	= box.getString("p_ability");

        try {
            connMgr = new DBConnectionManager();
        		connMgr.setAutoCommit(false);

            sql  = "DELETE 					 																						";
            sql += "FROM		TZ_ABILITY_PRO																			";
            sql += "WHERE		GUBUNCD			= ?                          						";
            sql += "AND			GUBUNCDDT		= ?                          						";
            sql += "AND			ABILITY			= ?                          						";

            pstmt1 = connMgr.prepareStatement(sql);
            pstmt1.setString(1, v_gubuncd);
            pstmt1.setString(2, v_gubuncddt);
            pstmt1.setString(3, v_ability);
            isOk1 = pstmt1.executeUpdate();
            
            if ( pstmt1 != null ) { pstmt1.close(); }

            sql  = "DELETE 					 																						";
            sql += "FROM		TZ_ABILITY																					";
            sql += "WHERE		GUBUNCD			= ?                          						";
            sql += "AND			GUBUNCDDT		= ?                          						";
            sql += "AND			ABILITY			= ?                          						";

            pstmt1 = connMgr.prepareStatement(sql);
            pstmt1.setString(1, v_gubuncd);
            pstmt1.setString(2, v_gubuncddt);
            pstmt1.setString(3, v_ability);
            isOk1 = pstmt1.executeUpdate();

            if ( pstmt1 != null ) { pstmt1.close(); }
            
        } catch ( Exception ex ) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
        		connMgr.setAutoCommit(true);
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
        }
        return isOk1;
    }


}
