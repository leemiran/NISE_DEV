// **********************************************************
//  1. 제      목: 역량 분류
//  2. 프로그램명: AbilityCodeBean.java
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

public class AbilityCodeBean {

    public AbilityCodeBean() { }


    /**
    역량 분류 Select Box 조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectAbilityCd(RequestBox box) throws Exception {
    	DBConnectionManager	connMgr		= null;
    	DataBox             dbox    	= null;
    	ListSet				ls			= null;
    	ArrayList			list		= null;
    	String				sql			= "";
    	
    	String              v_gubun     = box.getString("pp_gubun");
    	String              v_process    = box.getString("p_process");
    	
    	
    	try {
    		connMgr     = new DBConnectionManager();
    		list        = new ArrayList();
    		
    		sql  = "SELECT	GUBUNCD, GUBUNCDNM, GUBUN		";
    		sql += "FROM		TZ_ABILITY_CODE				" +
    		" where 1 = 1";
    		
    		//사용자페이지일 경우    
    		if(!"listPage".equals(v_process) && !"insertPage".equals(v_process) ) {    							
    			sql += " and		GUBUN='Y'				";
    		}   
    		
    		if (!v_gubun.equals("")){
    			sql += "AND	GUBUN = '"+ v_gubun +"'			";
    		}
    		
    		sql += "ORDER BY GUBUNCD ASC					";
    		
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
    역량 군  Select Box 조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectAbilityCdDt(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet							ls				= null;
        ArrayList						list			= null;
        String							sql				= "";
        String 							v_gubuncd		= box.getString("p_gubuncd");

        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();

            sql  = "SELECT	GUBUNCDDT, GUBUNCDDTNM			";
            sql += "FROM		TZ_ABILITY_CODE_DT					";
            sql += "WHERE		GUBUNCD	= '"+ v_gubuncd +"'	";
            sql += "ORDER BY GUBUNCDDT ASC							";
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
        ListSet							ls				= null;
        ArrayList						list			= null;
        String							sql				= "";
        String 							v_gubun		= box.getString("p_gubun");
        String                          vv_gubun     = box.getString("pp_gubun");

        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();

            sql  = "SELECT	A.GUBUNCD, A.GUBUN, A.GUBUNCDNM, B.GUBUNCDDT, B.GUBUNCDDTNM, B.INDATE, 	";
            sql += "				NVL(C.ROWCNT,1) AS ROWCNT																				";
            sql += "FROM		TZ_ABILITY_CODE A																								";
            sql += "					LEFT OUTER JOIN TZ_ABILITY_CODE_DT B													";
            sql += "						ON A.GUBUNCD = B.GUBUNCD																		";
            sql += "					LEFT OUTER JOIN(																							";
            sql += "						SELECT	GUBUNCD, COUNT(GUBUNCD) AS ROWCNT										";
            sql += "						FROM	TZ_ABILITY_CODE_DT																		";
            sql += "						GROUP BY GUBUNCD ) C																				";
            sql += "						ON A.GUBUNCD = C.GUBUNCD																		";

            
            if (!vv_gubun.equals("") && !v_gubun.equals("")){
            	sql += "WHERE	A.GUBUN = '"+ vv_gubun +"'																			";
            	sql += "AND		A.GUBUNCD = '"+ v_gubun +"'																			";	
            
            }
            
            if (!vv_gubun.equals("") && v_gubun.equals("")){
            	sql += "WHERE	A.GUBUN = '"+ vv_gubun +"'																			";
            	
            }
            
            if (vv_gubun.equals("") && !v_gubun.equals("")){
            	sql += "WHERE	A.GUBUNCD = '"+ v_gubun +"'																			";
            	
            }
            
            /*
            if (v_gubun != null && !v_gubun.equals("")){
            	sql += "WHERE	A.GUBUNCD = '"+ v_gubun +"'																			";
            }
           
            if (!vv_gubun.equals("")){
            	sql += "AND	A.GUBUN = '"+ vv_gubun +"'																			";
            }
            */	
            	
            sql += "ORDER BY A.GUBUNCD ASC, B.GUBUNCDDT ASC																									";
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
        ListSet							ls				= null;
        String							sql				= "";

        String v_gubuncd = box.getString("p_gubuncd");

        try {
            connMgr     = new DBConnectionManager();

            sql  = "SELECT 	GUBUNCD, GUBUNCDNM, INDATE, INUSERID, LDATE, LUSERID, GUBUN	";
            sql += "FROM		TZ_ABILITY_CODE																				";
            sql += "WHERE		GUBUNCD = '"+ v_gubuncd +"'														";
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

        String v_gubuncdnm 	= box.getString("p_gubuncdnm");
        String s_userid 	= box.getSession("userid");
        String abilityGubun = box.getString("p_gubun");

        try {
           connMgr = new DBConnectionManager();

           sql  = "SELECT NVL(MAX(GUBUNCD), 10000) + 1 AS GUBUNCD FROM TZ_ABILITY_CODE  ";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) {
               v_gubun = ls.getString("GUBUNCD");
           }

           sql1 =  "INSERT INTO TZ_ABILITY_CODE(GUBUNCD, GUBUNCDNM, INDATE, INUSERID, LDATE, LUSERID, GUBUN)   					";
           sql1 += " VALUES (?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, ? )	";

           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setString(1, v_gubun);
           pstmt.setString(2, v_gubuncdnm);
           pstmt.setString(3, s_userid);
           pstmt.setString(4, s_userid);
           pstmt.setString(5, abilityGubun);

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

        String v_gubuncd 	= box.getString("p_gubuncd");
        String v_gubuncdnm 	= box.getString("p_gubuncdnm");
        String s_userid    	= box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();


            sql  = "UPDATE 	TZ_ABILITY_CODE 																	";
            sql += "SET			GUBUNCDNM	= ? 	,																	";
            sql += "				LUSERID		= ? 	, 																";
            sql += "				LDATE 		= TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') 	";
            sql += "WHERE		GUBUNCD		= ?                          						";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_gubuncdnm);
			pstmt.setString(2, s_userid);
            pstmt.setString(3, v_gubuncd);
            
            if ( pstmt != null ) { pstmt.close(); }


            isOk = pstmt.executeUpdate();
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

        String v_gubuncd  = box.getString("p_gubuncd");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = "DELETE 					 										";
            sql += "FROM		TZ_ABILITY_CODE_DT					";
            sql += "WHERE		GUBUNCD		= ?               ";

            pstmt1 = connMgr.prepareStatement(sql);
            pstmt1.setString(1, v_gubuncd);
            isOk1 = pstmt1.executeUpdate();
            
            if ( pstmt1 != null ) { pstmt1.close(); }

            sql  = "DELETE 					 										";
            sql += "FROM		TZ_ABILITY_CODE							";
            sql += "WHERE		GUBUNCD		= ?               ";

            pstmt1 = connMgr.prepareStatement(sql);
            pstmt1.setString(1, v_gubuncd);
            isOk1 = pstmt1.executeUpdate();
            
            if ( pstmt1 != null ) { pstmt1.close(); }

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
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
        }
        return isOk1;
    }


}
