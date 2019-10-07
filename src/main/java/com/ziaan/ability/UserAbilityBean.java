// **********************************************************
//  1. 제      목: 역량 분류
//  2. 프로그램명: UserAbilityBean.java
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

public class UserAbilityBean {

  public UserAbilityBean() { }


  /**
    내 직급 정보
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public DataBox myInfo(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd			= box.getString("p_gubuncd");
    String 				v_gubuncddt		= box.getString("p_gubuncddt");
    String 				v_resultseq		= box.getString("p_resultseq");
    String 				s_userid 			= box.getSession("userid");

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();

      //직급 직위 성별 가져오기.
      sql  = "SELECT 	COMP, JIKUP, JIKWI, GET_JIKUPNM(JIKWI, COMP) JIKWINM,												";
      sql += "				GET_JIKUPNM(JIKUP, COMP) JIKUPNM , GET_COMPNM(COMP,2,2) COMPNM,							";
      sql += "				DECODE(SUBSTR(birth_date, 7,1), '3', '1', '4', '2', SUBSTR(birth_date, 7, 1)) AS SEX	";
      sql += "FROM 		TZ_MEMBER																																		";
      sql += "WHERE 	USERID			= '"+ s_userid +"'  		 																				";
      ls = connMgr.executeQuery(sql);

      if ( ls.next() ) {
        //직급. 직위 정보 넣기
        box.put("p_jikup", ls.getString("jikup"));
        box.put("p_jikupnm", ls.getString("jikupnm"));
        box.put("p_jikwi", ls.getString("jikwi"));
        box.put("p_jikwinm", ls.getString("jikwinm"));
        box.put("p_deptnm", ls.getString("compnm"));

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

    String 				v_gubuncd			= box.getStringDefault("p_gubuncd", "10002");

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
    역량 분류 안내 목록
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilityList(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd			= box.getStringDefault("p_gubuncd", "NOT");
    String 				v_gubuncddt		= box.getStringDefault("p_gubuncddt", "NOT");

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();


      sql  = "SELECT	ABILITY, ABILITYNM, DESCS, INDATE		";
      sql += "FROM		TZ_ABILITY													";
      sql += "WHERE		GUBUNCD 	= '"+ v_gubuncd +"'				";
  //  if(!v_gubuncddt.equals("ALL")){
	  sql += "AND			GUBUNCDDT = '"+ v_gubuncddt +"'			";
  //  }
	  sql += "ORDER BY ORDERS ASC													";
	  

	  
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
    역량 분류 실행/결과 목록
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilityResultList(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String s_userid    = box.getSession("userid");
    String v_gubuncd   = box.getString("p_gubuncd");
    String v_gubuncddt = box.getString("p_gubuncddt");
    //String v_gubuncd = box.getStringDefault("p_gubuncd", "10002");


    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();

      /*
      sql  = "SELECT	 C.NAME, C.USERID, A.INDATE, A.RESULTSEQ, TO_CHAR(A.RESULTPOINT) RESULTPOINT,	";
      sql += "				A.GUBUNCD, A.GUBUNCDDT, f.gubuncdnm,	B.GUBUNCDDTNM, GET_JIKUPNM(C.JIKWI, C.COMP) JIKWINM,	";
      sql += "				GET_JIKUPNM(C.JIKUP, C.COMP) JIKUPNM , GET_COMPNM(C.COMP,2,2) COMPNM,	 				";
      sql += "				NVL(E.CNT,0) AS SELCNT, NVL(D.TOTCNT,0) AS TOTCNT,														";
      sql += "				DECODE(NVL(D.TOTCNT,0),0,0,ROUND(NVL(E.CNT,0) / NVL(D.TOTCNT,0) * 100,1)) AS PERCENT	";
      sql += "FROM		TZ_ABILITY_RSLT_MAS A																													";
      sql += "					LEFT OUTER JOIN TZ_ABILITY_CODE_DT B																				";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD																								";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT																							";
      sql += "					INNER JOIN TZ_MEMBER	C																											";
      sql += "						ON	A.USERID		= C.USERID																								";
      sql += "						AND	C.USERID		= '"+ s_userid +"'			LEFT OUTER JOIN tz_ability_code f  on a.gubuncd=f.GUBUNCD																	";
      sql += "					LEFT OUTER JOIN (																														";
      sql += "								SELECT	GUBUNCD, GUBUNCDDT, COUNT(GUBUNCD) AS TOTCNT									";
      sql += "								FROM		TZ_ABILITY_PRO																								";
      sql += "								GROUP BY GUBUNCD, GUBUNCDDT) D																				";
      sql += "						ON	A.GUBUNCD		= D.GUBUNCD																								";
      sql += "						AND	A.GUBUNCDDT	= D.GUBUNCDDT																							";
      sql += "					LEFT OUTER JOIN (																														";
      sql += "								SELECT	RESULTSEQ, GUBUNCD, GUBUNCDDT, COUNT(USERID) AS CNT						";
      sql += "								FROM		TZ_ABILITY_RSLT_DT																						";
      sql += "								WHERE		USERID 	= '"+ s_userid +"'																		";
      sql += "								GROUP BY RESULTSEQ, GUBUNCD, GUBUNCDDT) E															";
      sql += "						ON	A.GUBUNCD		= E.GUBUNCD																								";
      sql += "						AND	A.GUBUNCDDT	= E.GUBUNCDDT																							";
      sql += "						AND	A.RESULTSEQ	= E.RESULTSEQ																							";
      sql += "WHERE		A.USERID 	= '"+ s_userid +"'																									";
      sql += "AND       F.GUBUN = 'Y'						and a.resultseq <1000000																			";
      */
      
      /* 직위, 직급삭제, 다면진단 수행내역 제외*/ 
      sql  = "SELECT	 C.NAME,    C.USERID,    A.INDATE,      A.RESULTSEQ,    TO_CHAR(A.RESULTPOINT) RESULTPOINT,	\n ";
      sql += "			 A.GUBUNCD, A.GUBUNCDDT, f.gubuncdnm,	B.GUBUNCDDTNM,                                      \n ";
      sql += "			 GET_COMPNM(C.COMP) COMPNM,                                                                 \n ";
      sql += "			 NVL(E.CNT,0) AS SELCNT, NVL(D.TOTCNT,0) AS TOTCNT,                                         \n ";
      sql += "			 DECODE(NVL(D.TOTCNT,0),0,0,ROUND(NVL(E.CNT,0) / NVL(D.TOTCNT,0) * 100,1)) AS PERCENT       \n ";
      
      sql += "  FROM     TZ_ABILITY_RSLT_MAS A                                     \n ";
      sql += "					LEFT OUTER JOIN TZ_ABILITY_CODE_DT B               \n ";
      sql += "						         ON	A.GUBUNCD   = B.GUBUNCD            \n ";
      sql += "						        AND	A.GUBUNCDDT	= B.GUBUNCDDT          \n ";
      
      sql += "					     INNER JOIN TZ_MEMBER C                        \n ";
      sql += "						         ON	A.USERID = C.USERID                \n ";
      sql += "						        AND	C.USERID = '"+ s_userid +"'        \n ";
      
      sql += "                  LEFT OUTER JOIN TZ_ABILITY_CODE F                  \n ";
      sql += "                               ON A.GUBUNCD = F.GUBUNCD              \n ";
      
      sql += "					LEFT OUTER JOIN (						           \n ";
      sql += "								      SELECT GUBUNCD, GUBUNCDDT, COUNT(GUBUNCD) AS TOTCNT    \n ";
      sql += "								        FROM TZ_ABILITY_PRO                                  \n ";
      sql += "								       GROUP BY GUBUNCD, GUBUNCDDT) D                        \n ";
      sql += "						         ON A.GUBUNCD	= D.GUBUNCD                                  \n ";
      sql += "						        AND	A.GUBUNCDDT	= D.GUBUNCDDT                                \n ";
      
      sql += "					LEFT OUTER JOIN (                                                        \n ";
      sql += "								SELECT	AA.RESULTSEQ, AA.GUBUNCD, AA.GUBUNCDDT, COUNT(AA.USERID) AS CNT, PUSERID  \n ";
      sql += "								  FROM	TZ_ABILITY_RSLT_DT AA LEFT OUTER JOIN TZ_ABILITY_RSLT_ABL BB    \n ";
      sql += "								    ON  AA.GUBUNCD = BB.GUBUNCD                                         \n ";
      sql += "								   AND  AA.GUBUNCDDT = BB.GUBUNCDDT                                     \n ";
      sql += "								   AND  AA.USERID = BB.USERID                                           \n ";
      sql += "								   AND  AA.RESULTSEQ = BB.RESULTSEQ                                     \n ";
      sql += "								   AND  AA.ABILITY = BB.ABILITY                                         \n ";
      sql += "								 WHERE	AA.USERID 	= '"+ s_userid +"'                                  \n ";
      sql += "								 GROUP BY AA.RESULTSEQ, AA.GUBUNCD, AA.GUBUNCDDT, PUSERID) E                     \n ";
      sql += "						         ON	A.GUBUNCD	= E.GUBUNCD                                  \n ";
      sql += "						        AND	A.GUBUNCDDT	= E.GUBUNCDDT                                \n ";
      sql += "						        AND	A.RESULTSEQ	= E.RESULTSEQ                                \n ";
      sql += " WHERE A.USERID 	= '"+ s_userid +"'                                                       \n ";
      sql += "   AND F.GUBUN    = 'Y' 																	 \n ";
      sql += "   AND E.PUSERID  = '"+ s_userid +"' 														 \n ";
      sql += "   AND A.RESULTSEQ < 1000000                                                               \n ";      
      
      
      
      /*
      if(!v_gubuncd.equals("")){
	      sql += "AND       A.GUBUNCD = '"+ v_gubuncd +"' \n ";
      } 
      */   
      
	  sql += "ORDER BY  F.GUBUNCDNM, B.GUBUNCDDTNM, A.INDATE DESC \n ";
      ls = connMgr.executeQuery(sql);

      //Log.sys.println("=============역량 분류 실행/결과 목록========\n"+sql);
	  
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
    역량 실행 목록
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilityDtList(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd		= box.getStringDefault("p_gubuncd", "NOT");
    String 				v_gubuncddt		= box.getStringDefault("p_gubuncddt","NOT");

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();


      sql  = "SELECT  A.GUBUNCD, A.GUBUNCDDT, A.GUBUNCDDTNM, B.CNT_GUBUNCD, A.DESCS		";
      sql += "  FROM  TZ_ABILITY_CODE_DT a, 	\n" +
      		 "		  (SELECT GUBUNCD, GUBUNCDDT, COUNT(GUBUNCD) CNT_GUBUNCD FROM TZ_MSIDEPAR_MAS \n" +
      		 " 		    WHERE GUBUNCD 	= '"+ v_gubuncd +"' \n" +
      		 "			  AND GUBUNCDDT 	= '"+ v_gubuncddt +"' \n" +
      		 "		    GROUP BY GUBUNCD, GUBUNCDDT ) B \n" +
      		 " WHERE  A.GUBUNCD = B.GUBUNCD(+) \n" +
      		 "	AND  A.GUBUNCDDT = B.GUBUNCDDT(+) ";
      sql += "  and  a.GUBUNCD 	= '"+ v_gubuncd +"' ";
	  sql += "  AND  a.GUBUNCDDT 	= '"+ v_gubuncddt +"'  ";
      sql += "ORDER BY a.GUBUNCD, a.GUBUNCDDT												";
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
    역량 실행 목록
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilityProList(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd		= box.getString("p_gubuncd");
    String 				v_gubuncddt		= box.getString("p_gubuncddt");
    String 				v_resultseq		= box.getString("p_resultseq");
	
	String 				v_userid	= box.getSession("userid");
	
	String s_userid 	= "";
	
	//다면진단 여부
	String v_ismulti = box.getString("p_ismulti"); //
	
	//다면진단일 경우
	if(v_ismulti.equals("multi")){
		s_userid 	= box.getString("p_userid"); //피진단자  = userid
	}else{
		s_userid 	= box.getSession("userid");
	}

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();

      sql  = "SELECT	A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, A.ABILITYPRO,A.ABILITYPRODESC, ";
      sql += "				B.INPUTS, B.INDATE																								";
      sql += "FROM		TZ_ABILITY_PRO A																									";
      sql += "					LEFT OUTER JOIN TZ_ABILITY_RSLT_DT B														";
      sql += "						ON	A.GUBUNCD			= B.GUBUNCD																	";
      sql += "						AND	A.GUBUNCDDT		= B.GUBUNCDDT																";
      sql += "						AND	A.ABILITY			= B.ABILITY																	";
      sql += "						AND	A.ABILITYPRO	= B.ABILITYPRO															";
      sql += "   					and b.puserid        ='"+v_userid+"'										\n";
	  sql += "						AND	B.USERID			= '"+ s_userid +"'													";
      if (!v_resultseq.equals("")){
        sql += "					AND	B.RESULTSEQ		= '"+ v_resultseq +"'												";
      }
      sql += "WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'																			";
      sql += "AND			A.GUBUNCDDT	= '"+ v_gubuncddt +"'																		";
      sql += "ORDER BY A.ORDERS																													";
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
    역량 평가 처음 입장 했을때 마스터에 등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
  public int insertMas(RequestBox box) throws Exception {
    DBConnectionManager	connMgr	= null;
    ListSet             ls      = null;
    PreparedStatement   pstmt   = null;
    String              sql     = "";
    String 				sql1    = "";

    int isOk = 0;

    String v_gubuncd    = box.getString("p_gubuncd");
    String v_gubuncddt 	= box.getString("p_gubuncddt");
    String s_userid     = box.getSession("userid");

    String v_resultseq	= "0";
    String v_comp       = "";
    String v_jikup		= box.getString("p_jikup");
    String v_jikryul    = "";
    String v_sex		= "";

    try {
      connMgr = new DBConnectionManager();

      sql  = "SELECT 	NVL(MAX(RESULTSEQ),0) + 1	AS RESULTSEQ	";
      sql += "FROM 		TZ_ABILITY_RSLT_MAS 										\n" +
      		" where RESULTSEQ < 1000000 ";
      ls = connMgr.executeQuery(sql);

      if ( ls.next() ) {
        v_resultseq = ls.getString("resultseq");
      }
      if(ls!=null)ls.close();

      box.put("p_resultseq", v_resultseq);

      /*
      sql  = "SELECT 	COMP, JIKUP, JIKRYUL, SEX					";
      sql += "FROM 		TZ_MEMBER																																												";
      sql += "WHERE		USERID	= '"+ s_userid +"' 																																			";
      ls = connMgr.executeQuery(sql);

      if ( ls.next() ) {
        v_comp 	= ls.getString("comp");
        v_jikup = ls.getString("jikup");
        v_jikryul = ls.getString("jikryul");
        v_sex 	= ls.getString("sex");
      }
      */
      
      /* comp, birth_date만 멤버테이블에서 가져옴, 직급은 진단실시 시작시 선택한 값으로 대체 09.11.10 수정 */
      sql  = " SELECT COMP, birth_date \n ";
      sql += "   FROM TZ_MEMBER \n ";
      sql += "  WHERE USERID	= '"+ s_userid +"' \n ";
      ls = connMgr.executeQuery(sql);

      if ( ls.next() ) {
        v_comp 	= ls.getString("comp");
        v_sex 	= ls.getString("birth_date").substring(6,7);  //1:남자, 2:여자
      }
      
      if(ls!=null)ls.close();

      sql1 =  "INSERT INTO TZ_ABILITY_RSLT_MAS(GUBUNCD, GUBUNCDDT, USERID, RESULTSEQ, RESULTPOINT, INDATE, INUSERID, JIKUP, COMP, SEX)   ";
      sql1 += " VALUES (?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?)			";
      pstmt = connMgr.prepareStatement(sql1);

      pstmt.setString(1, v_gubuncd);
      pstmt.setString(2, v_gubuncddt);
      pstmt.setString(3, s_userid);
      pstmt.setString(4, v_resultseq);
      pstmt.setInt	 (5, 0);
      pstmt.setString(6, s_userid);
      pstmt.setString(7, v_jikup);      
      pstmt.setString(8, v_comp);
      pstmt.setString(9, v_sex);
      //pstmt.setString(10, v_jikryul); //직렬사용하지않음

      isOk = pstmt.executeUpdate();
      if(pstmt!=null)pstmt.close();

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
    역량 평가 등록할때
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
	  int v_cnt	= 0;
	  
	  
	  String v_gubuncd 		= box.getString("p_gubuncd");
	  String v_gubuncddt 	= box.getString("p_gubuncddt");
	  String v_ability	 	= box.getString("p_ability");
	  String v_abilitypro	= box.getString("p_abilitypro");
	  String v_resultseq	= box.getString("p_resultseq");
	  String v_inputs		= box.getString("p_inputs");
	  
	  String v_puserid 		= box.getSession("userid"); //진단자 = s_userid  = puserid
	  String s_userid 	= "";
	  
	  //다면진단 여부
	  String v_ismulti = box.getString("p_ismulti"); //
	  
	  //다면진단일 경우
	  if(v_ismulti.equals("multi")){
		  s_userid 	= box.getString("p_userid"); //피진단자  = userid
	  }else{
		  s_userid 	= box.getSession("userid");
	  }
	  
	  //
	  
	  try {
		  connMgr = new DBConnectionManager();
		  
		  //해당 문항 등록 여부
		  sql  = "SELECT 	COUNT(ABILITY) AS CNT										";
		  sql += "FROM 		TZ_ABILITY_RSLT_DT 											";
		  sql += "WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 				";
		  sql += "AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   		";
		  sql += "AND 		USERID			= '"+ s_userid +"'  		 		";
		  sql += "AND 		PUSERID			= '"+ v_puserid +"'  		 		";
		  sql += "AND 		RESULTSEQ		= '"+ v_resultseq +"'   		";
		  sql += "AND 		ABILITY			= '"+ v_ability +"'   			";
		  sql += "AND 		ABILITYPRO	= '"+ v_abilitypro +"'   		";
		  ls = connMgr.executeQuery(sql);
		  
		  if ( ls.next() ) {
			  v_cnt = ls.getInt("cnt");
		  }
		  if(ls!=null)ls.close();
		  
		  
		  //문항 입력/수정
		  if (v_cnt == 0){
			  sql1 =  "INSERT INTO TZ_ABILITY_RSLT_DT(GUBUNCD, GUBUNCDDT, USERID, RESULTSEQ, ABILITY, ABILITYPRO, INPUTS, INDATE, INUSERID, ABILITYPOINT, puserid)   ";
			  sql1 += " VALUES (?, ?, ?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, ? ,?)			";
			  pstmt = connMgr.prepareStatement(sql1);
			  
			  pstmt.setString(1, v_gubuncd);
			  pstmt.setString(2, v_gubuncddt);
			  pstmt.setString(3, s_userid);
			  pstmt.setString(4, v_resultseq);
			  pstmt.setString(5, v_ability);
			  pstmt.setString(6, v_abilitypro);
			  pstmt.setString(7, v_inputs);
			  pstmt.setString(8, v_puserid);
			  pstmt.setString(9, v_inputs);
			  pstmt.setString(10, v_puserid);
		  }
		  else{
			  sql  = "UPDATE 	TZ_ABILITY_RSLT_DT																	";
			  sql += "SET			INPUTS			= ? 	,																	";
			  sql += "				LUSERID			= ? 	, 																";
			  sql += "				LDATE 			= TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') 	";
			  sql += "WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 										";
			  sql += "AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   								";
			  sql += "AND 		USERID			= '"+ s_userid +"'  		 								";
			  sql += "AND 		PUSERID			= '"+ v_puserid +"'  		 		";
			  sql += "AND 		RESULTSEQ		= '"+ v_resultseq +"'   								";
			  sql += "AND 		ABILITY			= '"+ v_ability +"'   									";
			  sql += "AND 		ABILITYPRO	= '"+ v_abilitypro +"'   								";
			  
			  pstmt = connMgr.prepareStatement(sql);
			  
			  pstmt.setString(1, v_inputs);
			  pstmt.setString(2, v_puserid);
		  }
		  isOk = pstmt.executeUpdate();
		  if(pstmt!=null)pstmt.close();
		  
		  //역량별 통계 입력 여부
		  sql  = "SELECT 	COUNT(ABILITY) AS CNT										";
		  sql += "FROM 		TZ_ABILITY_RSLT_ABL											";
		  sql += "WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 				";
		  sql += "AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   		";
		  sql += "AND 		USERID			= '"+ s_userid +"'  		 		";
		  sql += "AND 		RESULTSEQ		= '"+ v_resultseq +"'   		";
		  sql += "AND 		ABILITY			= '"+ v_ability +"'   			";
		  ls = connMgr.executeQuery(sql);
		  
		  
		  v_cnt = 0;
		  if ( ls.next() ) {
			  v_cnt = ls.getInt("cnt");
		  }
		  if(ls!=null)ls.close();
		  
		  //역량별 통계 입력
		  if (v_cnt == 0){
			  sql1 =  "INSERT INTO TZ_ABILITY_RSLT_ABL(GUBUNCD, GUBUNCDDT, USERID, RESULTSEQ, ABILITY, ABILITYPOINT, INDATE, INUSERID)   ";
			  sql1 += " VALUES (?, ?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?)			";
			  pstmt = connMgr.prepareStatement(sql1);
			  
			  pstmt.setString(1, v_gubuncd);
			  pstmt.setString(2, v_gubuncddt);
			  pstmt.setString(3, s_userid);
			  pstmt.setString(4, v_resultseq);
			  pstmt.setString(5, v_ability);
			  pstmt.setInt	 (6, 0);
			  pstmt.setString(7, v_puserid);
			  isOk = pstmt.executeUpdate();
			  
			  
			  if(pstmt!=null)pstmt.close();
		  }
		  
		  //역량별 통계 입력
		  sql  = "UPDATE 	TZ_ABILITY_RSLT_ABL																															";
		  sql += "SET			ABILITYPOINT= NVL(( SELECT 	AVG(INPUTS)																					";
		  sql += "														FROM		TZ_ABILITY_RSLT_DT			 														";
		  sql += "														WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 										";
		  sql += "														AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   								";
		  sql += "														AND 		ABILITY			= '"+ v_ability +"'   									";
		  sql += "														AND 		USERID			= '"+ s_userid +"'  		 								";
		  sql += "														AND 		RESULTSEQ		= '"+ v_resultseq +"' ),0) ,						";
		  sql += "				LUSERID			= '"+ v_puserid +"'	, 																							";
		  sql += "				LDATE 			= TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') 															";
		  sql += "WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 																								";
		  sql += "AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   																						";
		  sql += "AND 		USERID			= '"+ s_userid +"'  		 																						";
		  sql += "AND 		RESULTSEQ		= '"+ v_resultseq +"'   																						";
		  sql += "AND 		ABILITY			= '"+ v_ability +"'   																							";
		  
		  
		  pstmt = connMgr.prepareStatement(sql);
		  isOk = pstmt.executeUpdate();
		  
		  if(pstmt!=null)pstmt.close();
		  
		  sql  = "SELECT 	COUNT(GUBUNCD) AS CNT										";
		  sql += "FROM 		TZ_ABILITY_RSLT_MAS											";
		  sql += "WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 				";
		  sql += "AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   		";
		  sql += "AND 		USERID			= '"+ s_userid +"'  		 		";
		  sql += "AND 		RESULTSEQ		= '"+ v_resultseq +"'   		";
		  ls = connMgr.executeQuery(sql);
		  
		  
		  v_cnt = 0;
		  if ( ls.next() ) {
			  v_cnt = ls.getInt("cnt");
		  }
		  if(ls!=null)ls.close();
		  
		  if (v_cnt == 0){
			  
			  String avg = "";
			  
			  sql  = "     SELECT 	AVG(INPUTS)avg					";
			  sql += "	FROM		TZ_ABILITY_RSLT_DT			 		";													
			  sql += "	WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 		";								
			  sql += "	AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   		";						
			  sql += "	AND 		USERID			= '"+ s_userid +"'  		 ";								
			  sql += "	AND 		RESULTSEQ		= '"+ v_resultseq +"'		";					
			  
			  
			  ls = connMgr.executeQuery(sql);
			  
			  
			  v_cnt = 0;
			  if ( ls.next() ) {
				  avg = ls.getString("avg");
			  }
			  
			  if(ls!=null)ls.close();
			  
			  
			  String jikup ="";
			  String comp ="";
			  String sex ="";
			  String jikryul ="";
			  
			  /*
			  sql  = "  select jikup,comp,sex,jikryul from tz_member where userid='"+s_userid+"' \n";
			  
			  ls = connMgr.executeQuery(sql);
			  
			  
			  v_cnt = 0;
			  if ( ls.next() ) {
				  jikup = ls.getString("jikup");
				  comp = ls.getString("comp");
				  sex = ls.getString("sex");
				  jikryul = ls.getString("jikryul");
			  }
			   */
			  
			  /* comp, birth_date, jikup tz_msidepar_user 테이블에서 직급을 가져옴*/
			  sql  = " select a.comp, a.birth_date, b.jikup \n ";
			  sql += "   from tz_member a, (select jikup, tuserid from tz_msidepar_user where msidecode='"+v_resultseq+"' and tuserid='"+s_userid+"') b \n ";
			  sql += "  where a.userid=b.tuserid ";
			  sql += "    and a.userid='"+s_userid+"' \n ";
			  
			  ls = connMgr.executeQuery(sql);
			  
			  v_cnt = 0;
			  if ( ls.next() ) {
				  jikup = ls.getString("jikup");
				  comp = ls.getString("comp");
				  sex = ls.getString("birth_date").substring(6, 7);
			  }
			  
			  if(ls!=null)ls.close();
			  
			  /*
			  sql1 =  "insert into TZ_ABILITY_RSLT_MAS (gubuncd,gubuncddt,userid,resultseq,resultpoint,indate,inuserid,ldate,luserid,jikup,comp,sex,jikryul)  ";
			  sql1 += " VALUES (?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?,?,?,?,?)			";
			  pstmt = connMgr.prepareStatement(sql1);
			  
			  pstmt.setString(1, v_gubuncd);
			  pstmt.setString(2, v_gubuncddt);
			  pstmt.setString(3, s_userid);
			  pstmt.setString(4, v_resultseq);
			  pstmt.setString(5, avg);
			  pstmt.setString(6, v_puserid);
			  pstmt.setString(7, v_puserid);
			  pstmt.setString(8, jikup);
			  pstmt.setString(9, comp);
			  pstmt.setString(10, sex);
			  pstmt.setString(11, jikryul);	
			  */
			  sql1 =  "insert into TZ_ABILITY_RSLT_MAS (gubuncd,gubuncddt,userid,resultseq,resultpoint,indate,inuserid,ldate,luserid,jikup,comp,sex)  ";
			  sql1 += " VALUES (?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?,?,?,?)			";
			  pstmt = connMgr.prepareStatement(sql1);
			  
			  pstmt.setString(1, v_gubuncd);
			  pstmt.setString(2, v_gubuncddt);
			  pstmt.setString(3, s_userid);
			  pstmt.setString(4, v_resultseq);
			  pstmt.setString(5, avg);
			  pstmt.setString(6, v_puserid);
			  pstmt.setString(7, v_puserid);
			  pstmt.setString(8, jikup);
			  pstmt.setString(9, comp);
			  pstmt.setString(10, sex);
			  isOk = pstmt.executeUpdate();
			  
			  if(pstmt!=null)pstmt.close();
		  }
		  
		  
		  //해당 테스트 통계 업데이트
		  sql  = "UPDATE	TZ_ABILITY_RSLT_MAS	 ";
		  sql += "SET			RESULTPOINT = NVL(( SELECT 	AVG(INPUTS)	 ";
		  sql += "									 FROM		TZ_ABILITY_RSLT_DT ";
		  sql += "									 WHERE 	GUBUNCD 	= '"+ v_gubuncd +"'  ";
		  sql += "										AND GUBUNCDDT 	= '"+ v_gubuncddt +"'  ";
		  sql += "										AND USERID		= '"+ s_userid +"'   ";
		  sql += "										AND RESULTSEQ	= '"+ v_resultseq +"' ),0) ";
		  sql += "WHERE		GUBUNCD 		= '"+ v_gubuncd +"' \n" +
		  " AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' \n" +
		  " AND 		USERID			= '"+ s_userid +"'  \n" +
		  " AND 		RESULTSEQ		= '"+ v_resultseq +"' ";
		  pstmt = connMgr.prepareStatement(sql);
		  isOk = pstmt.executeUpdate();
		  if(pstmt!=null)pstmt.close();
		  
		  
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
  추천 역량
  @param box          receive from the form object and session
  @return ArrayList   리스트
  */
  public ArrayList ablilityMyBest(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd			= box.getString("p_gubuncd");
    String 				v_gubuncddt		= box.getString("p_gubuncddt");
    String 				v_resultseq		= box.getString("p_resultseq");
    String 				s_userid 			= box.getSession("userid");

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();


      sql  = "SELECT	A.ABILITYNM, B.ABILITYPOINT																										";
      sql += "FROM		TZ_ABILITY A																																	";
      sql += "					INNER JOIN (																																";
      sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY, ABILITYPOINT											";
      sql += "								FROM		TZ_ABILITY_RSLT_ABL																						";
      sql += "								WHERE		USERID 		= '"+ s_userid +"'																	";
      sql += "								AND			RESULTSEQ = '"+ v_resultseq +"'																";
      sql += "								ORDER BY ABILITYPOINT DESC) B																					";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD																								";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT																							";
      sql += "						AND	A.ABILITY		= B.ABILITY																								";
      sql += "WHERE		ROWNUM	< 3																																		";
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
    내가 가장 못하는 역량
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilityMyWorst(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd			= box.getString("p_gubuncd");
    String 				v_gubuncddt		= box.getString("p_gubuncddt");
    String 				v_resultseq		= box.getString("p_resultseq");
    String 				s_userid 			= box.getSession("userid");

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();


      sql  = "SELECT	A.ABILITYNM, B.ABILITYPOINT																										";
      sql += "FROM		TZ_ABILITY A																																	";
      sql += "					INNER JOIN (																																";
      sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY, ABILITYPOINT											";
      sql += "								FROM		TZ_ABILITY_RSLT_ABL																						";
      sql += "								WHERE		USERID 		= '"+ s_userid +"'																	";
      sql += "								AND			RESULTSEQ = '"+ v_resultseq +"'																";
      sql += "								ORDER BY ABILITYPOINT ASC) B																					";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD																								";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT																							";
      sql += "						AND	A.ABILITY		= B.ABILITY																								";
      sql += "WHERE		ROWNUM	< 3																																		";
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
    전체 리더십 역량수준(나의 점수, 직급 평균)
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public DataBox ablilityTotBest(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String v_gubuncd	= box.getString("p_gubuncd");
    String v_gubuncddt	= box.getString("p_gubuncddt");
    String v_resultseq	= box.getString("p_resultseq");
    String v_jikup		= box.getSession("jikup");
    String s_userid 	= box.getSession("userid");

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();
      
      /*
      sql  = "SELECT	TO_CHAR(NVL(A.RESULTPOINT,0)) AS MY_POINT, \n ";
      sql += "				TO_CHAR(NVL(B.RESULTPOINT,0)) AS TOT_POINT \n ";
      sql += "FROM		TZ_ABILITY_RSLT_MAS A \n";
      sql += "					LEFT OUTER JOIN ( \n";
      sql += "								SELECT	GUBUNCD, GUBUNCDDT, ROUND(AVG(RESULTPOINT),1) AS RESULTPOINT \n	";
      sql += "								FROM		TZ_ABILITY_RSLT_MAS \n";
      sql += "								WHERE		JIKUP	 		= '"+ v_jikup +"' \n";
      sql += "								GROUP BY GUBUNCD, GUBUNCDDT) B \n";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n";
      sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      sql += "AND	    A.USERID 	    = '"+ s_userid +"' \n ";
      sql += "AND	    A.RESULTSEQ 	= '"+ v_resultseq +"' \n ";
      */
      
      /* 직급은 해당 역량평가 당시의 직급을 가져온다. */
      sql  = "SELECT	TO_CHAR(NVL(A.RESULTPOINT,0)) AS MY_POINT, \n ";
      sql += "				TO_CHAR(NVL(B.RESULTPOINT,0)) AS TOT_POINT \n ";
      sql += "FROM		TZ_ABILITY_RSLT_MAS A \n";
      sql += "					LEFT OUTER JOIN ( \n";
      sql += "								SELECT	GUBUNCD, GUBUNCDDT, ROUND(AVG(RESULTPOINT),1) AS RESULTPOINT \n	";
      sql += "								FROM		TZ_ABILITY_RSLT_MAS \n";
      sql += "								WHERE		JIKUP=(select jikup from TZ_ABILITY_RSLT_MAS where GUBUNCD = '"+ v_gubuncd +"' and GUBUNCDDT = '"+ v_gubuncddt +"' and USERID = '"+ s_userid +"' and RESULTSEQ 	= '"+ v_resultseq +"') \n ";
      sql += "								GROUP BY GUBUNCD, GUBUNCDDT) B \n";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n";
      sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      sql += "AND	    A.USERID 	    = '"+ s_userid +"' \n ";
      sql += "AND	    A.RESULTSEQ 	= '"+ v_resultseq +"' \n ";
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
  전체 리더십 역량수준(나의 점수, 직급 평균) _ 다면평가 
  @param box          receive from the form object and session
  @return ArrayList   리스트
  */
public DataBox ablilityTotBest_m(RequestBox box) throws Exception {
  DBConnectionManager	connMgr		= null;
  DataBox             dbox    	= null;
  ListSet				ls			= null;
  ArrayList			list		= null;
  String				sql			= "";

  String 				v_gubuncd			= box.getString("p_gubuncd");
  String 				v_gubuncddt		    = box.getString("p_gubuncddt");
  String 				v_resultseq		    = box.getString("p_resultseq");
  String 				v_jikup		        = box.getSession("jikup");
  String 				s_userid 			= box.getSession("userid");

  try {
    connMgr     = new DBConnectionManager();
    list        = new ArrayList();

    sql  = "SELECT	TO_CHAR(NVL(A.RESULTPOINT,0)) AS MY_POINT, \n ";
    sql += "				TO_CHAR(NVL(B.RESULTPOINT,0)) AS TOT_POINT \n ";
    sql += "FROM		TZ_ABILITY_RSLT_MAS A																													";
    sql += "					LEFT OUTER JOIN (																														";
    sql += "								SELECT	GUBUNCD, GUBUNCDDT, ROUND(AVG(RESULTPOINT),1) AS RESULTPOINT \n ";
    sql += "								FROM		TZ_ABILITY_RSLT_MAS \n ";
    sql += "								WHERE		JIKUP	 		= (select jikup from tz_member where userid='"+s_userid+"') \n ";
    sql += "								GROUP BY GUBUNCD, GUBUNCDDT) B \n ";
    sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
    sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
    sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
    sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
    sql += "AND			A.USERID 			= '"+ s_userid +"' \n ";
    sql += "AND			A.RESULTSEQ 	= '"+ v_resultseq +"' \n ";
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
    현재 리더십 역량진단 결과
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilityStatus(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd			= box.getString("p_gubuncd");
    String 				v_gubuncddt		    = box.getString("p_gubuncddt");
    String 				v_resultseq		    = box.getString("p_resultseq");
    String 				v_result			= box.getString("p_result");
    String 				s_userid 			= box.getSession("userid");
    String 				v_comp				= "";
    String 				v_jikup				= "";
    String              v_jikryul           = "";
    String 				v_sex				= "";

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();

      //직급 직위 성별 가져오기.
      /*
      sql  = "SELECT 	COMP, JIKUP, 																																";
      sql += "				JIKRYUL, SEX	";
      sql += "FROM 		TZ_MEMBER																																		";
      sql += "WHERE 	USERID			= '"+ s_userid +"'  		 																				";
      ls = connMgr.executeQuery(sql);

      if ( ls.next() ) {
        v_jikup = ls.getString("jikup");
        v_comp	= ls.getString("comp");
        v_jikryul = ls.getString("jikryul");
        v_sex		= ls.getString("sex");
      }
      */
      
      /* 결과보기를 원하는 역량의 직급, 성별을 가져온다. */
      sql  = " SELECT a.comp, substr(a.birth_date,7,1) sex, b.jikup \n ";
      sql += "   FROM TZ_MEMBER a, (select userid, jikup from tz_ability_rslt_mas where gubuncd = '"+v_gubuncd+"' and gubuncddt = '"+v_gubuncddt+"' and resultseq = '"+v_resultseq+"' and userid='"+s_userid+"') b \n ";
      sql += "  where a.userid = b.userid \n ";
      sql += "    and a.userid = '"+ s_userid +"' \n";
      ls = connMgr.executeQuery(sql);

      if ( ls.next() ) {
        v_jikup = ls.getString("jikup");
        v_comp	= ls.getString("comp");
        v_sex	= ls.getString("sex");
      }
      //end 
      
      if(ls!=null)ls.close();

      sql  = "SELECT	A.ABILITYNM, TO_CHAR(NVL(B.ABILITYPOINT,0)) AS MY_ABL, \n ";
      sql += "				TO_CHAR(NVL(C.ABILITYPOINT,0)) AS TOT_ABL \n ";
      sql += "FROM		TZ_ABILITY A \n ";
      sql += "					LEFT OUTER JOIN ( \n ";
      sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY, ABILITYPOINT \n";
      sql += "								FROM		TZ_ABILITY_RSLT_ABL \n ";
      sql += "								WHERE		USERID 		= '"+ s_userid +"' \n ";
      sql += "								AND			RESULTSEQ = '"+ v_resultseq +"'	 ) B \n ";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= B.ABILITY	\n ";
      sql += "					LEFT OUTER JOIN ( \n ";
      sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY, \n ";
      sql += "												ROUND(AVG(ABILITYPOINT),2) ABILITYPOINT \n ";
      sql += "								FROM		TZ_ABILITY_RSLT_ABL \n ";
      sql += "								WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "								AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      if (v_result.equals("2")){			//직급
        sql += "	AND	USERID IN (	\n ";
        sql += "					SELECT 	USERID \n ";
        sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
        sql += "					WHERE 	JIKUP 			= '"+ v_jikup +"' \n ";
        sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
        sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
      }
      else if (v_result.equals("3")){	//성별
        sql += "	AND	USERID IN ( \n ";
        sql += "					SELECT 	USERID \n ";
        sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
        sql += "					WHERE 	SEX 				= '"+ v_sex +"' \n ";
        sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
        sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
      }
      
      /* 직렬 사용하지 않음
      else if (v_result.equals("4")){	//직렬 (제거)
        sql += "	AND	USERID IN ( \n ";
        sql += "					SELECT 	USERID \n ";
        sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
        sql += "					WHERE 	JIKRYUL	 			= '"+ v_jikryul +"' \n ";
        sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
        sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
      }
      */
      
      else if (v_result.equals("5")){	//소속기관
          sql += "	AND	USERID IN ( \n ";
          sql += "					SELECT 	USERID \n ";
          sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
          sql += "					WHERE 	COMP	 			= '"+ v_comp +"' \n ";
          sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
          sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
        }
      sql += "								GROUP BY GUBUNCD, GUBUNCDDT, ABILITY) C	\n ";
      sql += "						ON	A.GUBUNCD		= C.GUBUNCD	\n ";
      sql += "						AND	A.GUBUNCDDT	= C.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= C.ABILITY	\n ";
      sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      sql += "ORDER BY A.ABILITY ASC \n ";
      ls = connMgr.executeQuery(sql);

      while ( ls.next() ) {
        dbox = ls.getDataBox();
        list.add(dbox);
      }

      if(ls!=null)ls.close();

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
  현재 리더십 역량진단 결과 - 부하(다면진단)
  @param box          receive from the form object and session
  @return ArrayList   리스트
   */
  public ArrayList ablilityStatus21(RequestBox box) throws Exception {
	  DBConnectionManager	connMgr		= null;
	  DataBox             dbox    	= null;
	  ListSet				ls			= null;
	  ArrayList			list		= null;
	  String				sql			= "";
	  
	  String 				v_gubuncd			= box.getString("p_gubuncd");
	  String 				v_gubuncddt		    = box.getString("p_gubuncddt");
	  String 				v_resultseq		    = box.getString("p_resultseq");
	  String 				v_result			= box.getString("p_result");
	  String 				s_userid 			= box.getSession("userid");
	  String 				v_comp				= "";
	  String 				v_jikup				= "";
	  //String                v_jikryul           = "";
	  String 				v_sex				= "";
	  
	  try {
		  connMgr     = new DBConnectionManager();
		  list        = new ArrayList();
		  
		  //직급 직위 성별 가져오기.
		  /*
	  sql  = "SELECT 	COMP, JIKUP, 																																";
	  sql += "				JIKRYUL, SEX	";
	  sql += "FROM 		TZ_MEMBER																																		";
	  sql += "WHERE 	USERID			= '"+ s_userid +"'  		 																				";
	  ls = connMgr.executeQuery(sql);
	  
	  if ( ls.next() ) {
		  v_jikup = ls.getString("jikup");
		  v_comp	= ls.getString("comp");
		  v_jikryul = ls.getString("jikryul");
		  v_sex		= ls.getString("sex");
	  }
		   */
		  
		  /* 결과보기를 원하는 역량의 직급, 성별을 가져온다. */
		  sql  = " SELECT a.comp, substr(a.birth_date,7,1) sex, b.jikup \n ";
		  sql += "   FROM TZ_MEMBER a, (select userid, jikup from tz_ability_rslt_mas where gubuncd = '"+v_gubuncd+"' and gubuncddt = '"+v_gubuncddt+"' and resultseq = '"+v_resultseq+"' and userid='"+s_userid+"') b \n ";
		  sql += "  where a.userid = b.userid \n ";
		  sql += "    and a.userid = '"+ s_userid +"' \n";
		  ls = connMgr.executeQuery(sql);
		  
		  if ( ls.next() ) {
			  v_jikup = ls.getString("jikup");
			  v_comp	= ls.getString("comp");
			  v_sex	= ls.getString("sex");
		  }
		  //end 
		  
		  if(ls!=null)ls.close();
		  
		  sql  = "SELECT	A.ABILITYNM, TO_CHAR(NVL(B.ABILITYPOINT,0)) AS MY_ABL, \n ";
		  sql += "				TO_CHAR(NVL(C.ABILITYPOINT,0)) AS TOT_ABL \n ";
		  sql += "FROM		TZ_ABILITY A \n ";
		  sql += "					LEFT OUTER JOIN ( \n ";
		  sql += " SELECT   a.gubuncd, a.gubuncddt, a.ability, \n ";
		  sql += "    ROUND (AVG (a.abilitypoint), 1) abilitypoint \n ";
		  sql += " FROM tz_ability_rslt_dt a, tz_msidepar_target b \n ";
		  sql += " WHERE a.userid = '"+s_userid+"' \n ";
		  sql += " AND a.userid = b.tuserid \n ";
		  sql += " AND b.idgubun = '3'		AND			a.RESULTSEQ = '"+ v_resultseq +"' \n ";
		  sql += " AND b.suserid = a.puserid \n ";
		  sql += " GROUP BY a.gubuncd, a.gubuncddt, a.ability \n ";
		  sql += " ORDER BY a.ability \n ";
		  sql += "													) B \n ";
		  sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
		  sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
		  sql += "						AND	A.ABILITY		= B.ABILITY \n ";
		  sql += "					LEFT OUTER JOIN ( \n ";
		  sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY, \n ";
		  sql += "												ROUND(AVG(ABILITYPOINT),2) ABILITYPOINT \n ";
		  sql += "								FROM		TZ_ABILITY_RSLT_ABL \n ";
		  sql += "								WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' \n ";
		  sql += "								AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
		  if (v_result.equals("2")){			//직급
			  sql += "	AND	USERID IN ( \n ";
			  sql += "					SELECT 	USERID \n ";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
			  sql += "					WHERE 	JIKUP 			= '"+ v_jikup +"' \n ";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n  ";
		  }
		  else if (v_result.equals("3")){	//성별
			  sql += "	AND	USERID IN ( \n ";
			  sql += "					SELECT 	USERID \n ";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
			  sql += "					WHERE 	SEX 				= '"+ v_sex +"' \n ";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n  ";
		  }
		  
		  /* 직렬 사용하지 않음
	      else if (v_result.equals("4")){	//직렬 (제거)
	         sql += "	AND	USERID IN (																			";
	         sql += "					SELECT 	USERID 															";
	         sql += "					FROM 		TZ_ABILITY_RSLT_MAS 								";
	         sql += "					WHERE 	JIKRYUL	 			= '"+ v_jikryul +"'				";
	         sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' 		";
	         sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) ";
	      }
		  */
		  else if (v_result.equals("5")){	//소속기관
			  sql += "	AND	USERID IN ( \n ";
			  sql += "					SELECT 	USERID \n ";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
			  sql += "					WHERE 	COMP	 			= '"+ v_comp +"' \n ";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
		  }
		  sql += "								GROUP BY GUBUNCD, GUBUNCDDT, ABILITY) C \n ";
		  sql += "						ON	A.GUBUNCD		= C.GUBUNCD \n ";
		  sql += "						AND	A.GUBUNCDDT	= C.GUBUNCDDT \n ";
		  sql += "						AND	A.ABILITY		= C.ABILITY \n ";
		  sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
		  sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
		  sql += "ORDER BY A.ABILITY ASC \n ";
		  ls = connMgr.executeQuery(sql);
		  
		  while ( ls.next() ) {
			  dbox = ls.getDataBox();
			  list.add(dbox);
		  }
		  
		  if(ls!=null)ls.close();
		  
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
   현재 리더십 역량진단 결과 - 동료(다면진단)
   @param box          receive from the form object and session
   @return ArrayList   리스트
   */
  public ArrayList ablilityStatus22(RequestBox box) throws Exception {
	  DBConnectionManager	connMgr		= null;
	  DataBox             dbox    	= null;
	  ListSet				ls			= null;
	  ArrayList			list		= null;
	  String				sql			= "";
	  
	  String 				v_gubuncd			= box.getString("p_gubuncd");
	  String 				v_gubuncddt		= box.getString("p_gubuncddt");
	  String 				v_resultseq		= box.getString("p_resultseq");
	  String 				v_result			= box.getString("p_result");
	  String 				s_userid 			= box.getSession("userid");
	  String 				v_comp				= "";
	  String 				v_jikup				= "";
	  String              v_jikryul           = "";
	  String 				v_sex					= "";
	  
	  try {
		  connMgr     = new DBConnectionManager();
		  list        = new ArrayList();

		  //직급 직위 성별 가져오기.
		  /*
		  sql  = "SELECT 	COMP, JIKUP, 																																";
		  sql += "				JIKRYUL, SEX	";
		  sql += "FROM 		TZ_MEMBER																																		";
		  sql += "WHERE 	USERID			= '"+ s_userid +"'  		 																				";
		  ls = connMgr.executeQuery(sql);

		  if ( ls.next() ) {
		    v_jikup = ls.getString("jikup");
		    v_comp	= ls.getString("comp");
		    v_jikryul = ls.getString("jikryul");
		    v_sex		= ls.getString("sex");
		  }
		  */
		  
	      /* 결과보기를 원하는 역량의 직급, 성별을 가져온다. */
	      sql  = " SELECT a.comp, substr(a.birth_date,7,1) sex, b.jikup \n ";
	      sql += "   FROM TZ_MEMBER a, (select userid, jikup from tz_ability_rslt_mas where gubuncd = '"+v_gubuncd+"' and gubuncddt = '"+v_gubuncddt+"' and resultseq = '"+v_resultseq+"' and userid='"+s_userid+"') b \n ";
	      sql += "  where a.userid = b.userid \n ";
	      sql += "    and a.userid = '"+ s_userid +"' \n";
	      ls = connMgr.executeQuery(sql);

	      if ( ls.next() ) {
	        v_jikup = ls.getString("jikup");
	        v_comp	= ls.getString("comp");
	        v_sex	= ls.getString("sex");
	      }
	      //end 
	      
		  if(ls!=null)ls.close();
		  
		  sql  = "SELECT	A.ABILITYNM, TO_CHAR(NVL(B.ABILITYPOINT,0)) AS MY_ABL, \n ";
		  sql += "				TO_CHAR(NVL(C.ABILITYPOINT,0)) AS TOT_ABL \n ";
		  sql += "FROM		TZ_ABILITY A \n ";
		  sql += "					LEFT OUTER JOIN ( \n ";
		  sql += " SELECT   a.gubuncd, a.gubuncddt, a.ability, \n ";
		  sql += "    ROUND (AVG (a.abilitypoint), 1) abilitypoint \n ";
		  sql += " FROM tz_ability_rslt_dt a, tz_msidepar_target b \n ";
		  sql += " WHERE a.userid = '"+s_userid+"' \n ";
		  sql += " AND a.userid = b.tuserid \n ";
		  sql += " AND b.idgubun = '2'		AND			a.RESULTSEQ = '"+ v_resultseq +"' \n ";
		  sql += " AND b.suserid = a.puserid \n ";
		  sql += " GROUP BY a.gubuncd, a.gubuncddt, a.ability \n ";
		  sql += " ORDER BY a.ability \n ";
		  sql += "													) B \n ";
		  sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
		  sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
		  sql += "						AND	A.ABILITY		= B.ABILITY \n ";
		  sql += "					LEFT OUTER JOIN ( \n ";
		  sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY, \n ";
		  sql += "												ROUND(AVG(ABILITYPOINT),2) ABILITYPOINT \n ";
		  sql += "								FROM		TZ_ABILITY_RSLT_ABL \n ";
		  sql += "								WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' \n ";
		  sql += "								AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
		  if (v_result.equals("2")){			//직급
			  sql += "	AND	USERID IN ( \n ";
			  sql += "					SELECT 	USERID \n ";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
			  sql += "					WHERE 	JIKUP 			= '"+ v_jikup +"' \n ";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
		  }
		  else if (v_result.equals("3")){	//성별
			  sql += "	AND	USERID IN ( \n ";
			  sql += "					SELECT 	USERID \n ";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
			  sql += "					WHERE 	SEX 				= '"+ v_sex +"' \n ";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
		  }
		  
		  /*
		  else if (v_result.equals("4")){	//직렬 (제거)
			  sql += "	AND	USERID IN (																			";
			  sql += "					SELECT 	USERID 															";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS 								";
			  sql += "					WHERE 	JIKRYUL	 			= '"+ v_jikryul +"'				";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' 		";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) ";
		  }
		  */
		  
		  else if (v_result.equals("5")){	//소속기관
			  sql += "	AND	USERID IN ( \n ";
			  sql += "					SELECT 	USERID \n ";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
			  sql += "					WHERE 	COMP	 			= '"+ v_comp +"' \n ";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
		  }
		  sql += "								GROUP BY GUBUNCD, GUBUNCDDT, ABILITY) C \n ";
		  sql += "						ON	A.GUBUNCD		= C.GUBUNCD \n ";
		  sql += "						AND	A.GUBUNCDDT	= C.GUBUNCDDT \n ";
		  sql += "						AND	A.ABILITY		= C.ABILITY \n ";
		  sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
		  sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
		  sql += "ORDER BY A.ABILITY ASC \n ";
		  ls = connMgr.executeQuery(sql);
		  
		  
		  while ( ls.next() ) {
			  dbox = ls.getDataBox();
			  list.add(dbox);
		  }
		  
		  if(ls!=null)ls.close();
		  
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
   현재 리더십 역량진단 결과 - 상사(다면진단)
   @param box          receive from the form object and session
   @return ArrayList   리스트
   */
  public ArrayList ablilityStatus23(RequestBox box) throws Exception {
	  DBConnectionManager	connMgr		= null;
	  DataBox             dbox    	= null;
	  ListSet				ls			= null;
	  ArrayList			list		= null;
	  String				sql			= "";
	  
	  String 				v_gubuncd			= box.getString("p_gubuncd");
	  String 				v_gubuncddt		= box.getString("p_gubuncddt");
	  String 				v_resultseq		= box.getString("p_resultseq");
	  String 				v_result			= box.getString("p_result");
	  String 				s_userid 			= box.getSession("userid");
	  String 				v_comp				= "";
	  String 				v_jikup				= "";
	  String              v_jikryul           = "";
	  String 				v_sex					= "";
	  
	  try {
		  connMgr     = new DBConnectionManager();
		  list        = new ArrayList();
		  
		  //직급 직위 성별 가져오기.
		  /*
		  sql  = "SELECT 	COMP, JIKUP, 																																";
		  sql += "				JIKRYUL, SEX	";
		  sql += "FROM 		TZ_MEMBER																																		";
		  sql += "WHERE 	USERID			= '"+ s_userid +"'  		 																				";
		  ls = connMgr.executeQuery(sql);
		  
		  if ( ls.next() ) {
			  v_jikup = ls.getString("jikup");
			  v_comp	= ls.getString("comp");
			  v_jikryul = ls.getString("jikryul");
			  v_sex		= ls.getString("sex");
		  }
		  */
		  
	      /* 결과보기를 원하는 역량의 직급, 성별을 가져온다. */
	      sql  = " SELECT a.comp, substr(a.birth_date,7,1) sex, b.jikup \n ";
	      sql += "   FROM TZ_MEMBER a, (select userid, jikup from tz_ability_rslt_mas where gubuncd = '"+v_gubuncd+"' and gubuncddt = '"+v_gubuncddt+"' and resultseq = '"+v_resultseq+"' and userid='"+s_userid+"') b \n ";
	      sql += "  where a.userid = b.userid \n ";
	      sql += "    and a.userid = '"+ s_userid +"' \n";
	      ls = connMgr.executeQuery(sql);

	      if ( ls.next() ) {
	        v_jikup = ls.getString("jikup");
	        v_comp	= ls.getString("comp");
	        v_sex	= ls.getString("sex");
	      }
	      //end 
	      
		  if(ls!=null)ls.close();
		  
		  sql  = "SELECT	A.ABILITYNM, TO_CHAR(NVL(B.ABILITYPOINT,0)) AS MY_ABL, \n ";
		  sql += "				TO_CHAR(NVL(C.ABILITYPOINT,0)) AS TOT_ABL \n ";
		  sql += "FROM		TZ_ABILITY A \n ";
		  sql += "					LEFT OUTER JOIN ( \n ";
		  sql += " SELECT   a.gubuncd, a.gubuncddt, a.ability, \n ";
		  sql += "    ROUND (AVG (a.abilitypoint), 1) abilitypoint \n ";
		  sql += " FROM tz_ability_rslt_dt a, tz_msidepar_target b \n ";
		  sql += " WHERE a.userid = '"+s_userid+"' \n ";
		  sql += " AND a.userid = b.tuserid \n ";
		  sql += " AND b.idgubun = '1'		AND			a.RESULTSEQ = '"+ v_resultseq +"' \n ";
		  sql += " AND b.suserid = a.puserid \n ";
		  sql += " GROUP BY a.gubuncd, a.gubuncddt, a.ability \n ";
		  sql += " ORDER BY a.ability \n ";
		  sql += "													) B \n ";
		  sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
		  sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
		  sql += "						AND	A.ABILITY		= B.ABILITY \n ";
		  sql += "					LEFT OUTER JOIN ( \n ";
		  sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY, \n ";
		  sql += "												ROUND(AVG(ABILITYPOINT),2) ABILITYPOINT \n ";
		  sql += "								FROM		TZ_ABILITY_RSLT_ABL \n ";
		  sql += "								WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' \n ";
		  sql += "								AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
		  if (v_result.equals("2")){			//직급
			  sql += "	AND	USERID IN ( \n ";
			  sql += "					SELECT 	USERID \n ";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
			  sql += "					WHERE 	JIKUP 			= '"+ v_jikup +"' \n ";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
		  }
		  else if (v_result.equals("3")){	//성별
			  sql += "	AND	USERID IN ( \n ";
			  sql += "					SELECT 	USERID \n ";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
			  sql += "					WHERE 	SEX 				= '"+ v_sex +"' \n ";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n  ";
		  }
		  
		  /*
		  else if (v_result.equals("4")){	//직렬 (제거)
			  sql += "	AND	USERID IN (																			";
			  sql += "					SELECT 	USERID 															";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS 								";
			  sql += "					WHERE 	JIKRYUL	 			= '"+ v_jikryul +"'				";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' 		";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) ";
		  }
		  */
		  
		  else if (v_result.equals("5")){	//소속기관
			  sql += "	AND	USERID IN ( \n ";
			  sql += "					SELECT 	USERID \n ";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS \n ";
			  sql += "					WHERE 	COMP	 			= '"+ v_comp +"' \n ";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' \n ";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) \n ";
		  }
		  sql += "								GROUP BY GUBUNCD, GUBUNCDDT, ABILITY) C \n ";
		  sql += "						ON	A.GUBUNCD		= C.GUBUNCD \n ";
		  sql += "						AND	A.GUBUNCDDT	= C.GUBUNCDDT \n ";
		  sql += "						AND	A.ABILITY		= C.ABILITY \n ";
		  sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
		  sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
		  sql += "ORDER BY A.ABILITY ASC \n ";
		  ls = connMgr.executeQuery(sql);
		  
		  while ( ls.next() ) {
			  dbox = ls.getDataBox();
			  list.add(dbox);
		  }
		  
		  if(ls!=null)ls.close();
		  
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
    3개월전 리더십 역량진단 결과
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilityStatus3(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd		= box.getString("p_gubuncd");
    String 				v_gubuncddt		= box.getString("p_gubuncddt");
    String 				v_result		= box.getString("p_result");
    String 				v_resultseq		= "0";
    String 				s_userid 		= box.getSession("userid");
    String 				v_comp			= "";
    String 				v_jikup			= "";
    //String              v_jikryul       = "";
    String 				v_sex			= "";
    //String              v_selmonth      = "3";
    String              v_selmonth   = box.getStringDefault("p_selmonth", "3");  

    
    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();

      //직급 직위 성별 가져오기.
      /*
      sql  = "SELECT 	COMP, JIKUP, JIKRYUL, SEX					";
      sql += "FROM 		TZ_MEMBER																";
      sql += "WHERE 	USERID			= '"+ s_userid +"'  		 		";
      ls = connMgr.executeQuery(sql);

      if ( ls.next() ) {
        v_jikup = ls.getString("jikup");
        v_comp	= ls.getString("comp");
        v_jikryul = ls.getString("jikryul");
        v_sex		= ls.getString("sex");
      }
      if(ls!=null)ls.close();

      //3개월전 평가 데이터 가져오기
      sql  = "SELECT 	NVL(MAX(RESULTSEQ),0) RESULTSEQ					";
      sql += "FROM 		TZ_ABILITY_RSLT_MAS											";
      sql += "WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 				";
      sql += "AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   		";
      sql += "AND 		USERID			= '"+ s_userid +"'  		 		";
      sql += "AND 		LDATE				<= TO_CHAR(ADD_MONTHS(SYSDATE, -"+ v_selmonth +"),'YYYYMMDDHH24MISS')		";
      ls = connMgr.executeQuery(sql);
      */
      
      /* 소속, 성별만 가져온다. */
      sql  = "SELECT COMP, substr(birth_date,7,1) as sex \n ";
      sql += "  FROM TZ_MEMBER \n ";
      sql += " WHERE USERID = '"+ s_userid +"' ";
      ls = connMgr.executeQuery(sql);

      if ( ls.next() ) {
        v_comp	= ls.getString("comp");
        v_sex	= ls.getString("sex");
      }      
      if(ls!=null)ls.close();

      //3개월전 평가 데이터의 데이터를 가져온다.(직급, 성별)
      sql  =  " select resultseq, jikup \n "; 
      sql  += "   from TZ_ABILITY_RSLT_MAS \n "; 
      sql  += "  where resultseq = ( \n "; 
      sql  += "                      select NVL(MAX(resultseq),0) resultseq \n "; 
      sql  += "                        from TZ_ABILITY_RSLT_MAS \n "; 
      sql  += "                     ) ";
      sql  += "    and gubuncd 		= '"+ v_gubuncd +"' \n "; 
      sql  += "    and gubuncddt 	= '"+ v_gubuncddt +"' \n "; 
      sql  += "    and userid = '"+ s_userid +"' \n "; 
      sql  += "    and ldate <= to_char(add_months(sysdate, -"+ v_selmonth +"),'yyyymmddhh24miss') \n ";       
      ls = connMgr.executeQuery(sql);      
	  
      
      if ( ls.next() ) {
        v_resultseq = ls.getString("resultseq");
        v_jikup = ls.getString("jikup");
        //v_sex		= ls.getString("sex");
      }
      if(ls!=null)ls.close();

      sql  = "SELECT	A.ABILITYNM, TO_CHAR(NVL(B.ABILITYPOINT,0)) AS MY_ABL, \n ";
      sql += "				         TO_CHAR(NVL(C.ABILITYPOINT,0)) AS TOT_ABL \n ";
      sql += "FROM		TZ_ABILITY A \n ";
      sql += "					LEFT OUTER JOIN ( \n ";
      sql += "								      SELECT	GUBUNCD, GUBUNCDDT, ABILITY, ABILITYPOINT \n ";
      sql += "								        FROM    TZ_ABILITY_RSLT_ABL \n ";
      sql += "								       WHERE    USERID 		= '"+ s_userid +"' \n ";
      sql += "								         AND    RESULTSEQ = '"+ v_resultseq +"'	 ) B \n ";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
      sql += "						AND	A.GUBUNCDDT	    = B.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= B.ABILITY \n ";
      sql += "					LEFT OUTER JOIN ( \n ";
      sql += "								      SELECT	GUBUNCD, GUBUNCDDT, ABILITY, \n ";
      sql += "										        ROUND(AVG(ABILITYPOINT),1) ABILITYPOINT \n ";
      sql += "								        FROM	TZ_ABILITY_RSLT_ABL \n ";
      sql += "								       WHERE 	GUBUNCD 	= '"+ v_gubuncd +"' \n ";
      sql += "								         AND 	GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      if (v_result.equals("2")){		//직급
        sql += "	                                 AND	USERID IN ( \n ";
        sql += "					                                    SELECT 	USERID \n ";
        sql += "					                                      FROM  TZ_ABILITY_RSLT_MAS \n ";
        sql += "					                                     WHERE  JIKUP     = '"+ v_jikup +"' \n ";
        sql += "					                                       AND  GUBUNCD   = '"+ v_gubuncd +"' \n ";
        sql += "					                                       AND  GUBUNCDDT = '"+ v_gubuncddt +"' )  \n ";
      }
      else if (v_result.equals("3")){	//성별
        sql += "	                                 AND	USERID IN ( \n ";
        sql += "														SELECT 	USERID \n ";
        sql += "				 									 	  FROM  TZ_ABILITY_RSLT_MAS \n ";
        sql += "					                                     WHERE 	SEX 	  = '"+ v_sex +"' \n ";
        sql += "					                                       AND  GUBUNCD   = '"+ v_gubuncd +"' \n ";
        sql += "					                                       AND  GUBUNCDDT = '"+ v_gubuncddt +"' ) \n ";
      }
      /*
      else if (v_result.equals("4")){	//직렬(제거)
        sql += "	                                 AND	USERID IN ( \n ";
        sql += "					                                    SELECT 	USERID \n ";
        sql += "					                                      FROM  TZ_ABILITY_RSLT_MAS \n ";
        sql += "					                                     WHERE 	JIKRYUL	  = '"+ v_jikryul +"' \n ";
        sql += "					                                       AND  GUBUNCD   = '"+ v_gubuncd +"' \n ";
        sql += "					                                       AND  GUBUNCDDT = '"+ v_gubuncddt +"' ) \n ";
      }
      */
      else if (v_result.equals("5")){	//소속기관
          sql += "	                                 AND	USERID IN ( \n ";
          sql += "					                                    SELECT 	USERID \n ";
          sql += "					         							  FROM 	TZ_ABILITY_RSLT_MAS \n ";
          sql += "														 WHERE 	COMP      = '"+ v_comp +"' \n ";
          sql += "														   AND	GUBUNCD   = '"+ v_gubuncd +"' \n ";
          sql += "														   AND 	GUBUNCDDT = '"+ v_gubuncddt +"' ) \n ";
        }
      sql += "								       GROUP BY GUBUNCD, GUBUNCDDT, ABILITY ) C \n ";
      sql += "						ON	A.GUBUNCD		= C.GUBUNCD \n ";
      sql += "						AND	A.GUBUNCDDT	    = C.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= C.ABILITY \n ";
      sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      sql += "ORDER BY A.ABILITY ASC \n ";
      ls = connMgr.executeQuery(sql);
	  
      while ( ls.next() ) {
        dbox = ls.getDataBox();
        list.add(dbox);
      }
       if(ls!=null)ls.close();
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
  3개월전 리더십 역량진단 결과 - (다면평가)부하
  @param box          receive from the form object and session
  @return ArrayList   리스트
   */
  public ArrayList ablilityStatus31(RequestBox box) throws Exception {
	  DBConnectionManager	connMgr		= null;
	  DataBox               dbox    	= null;
	  ListSet				ls			= null;
	  ArrayList			    list		= null;
	  String				sql			= "";
	  
	  String 				v_gubuncd		= box.getString("p_gubuncd");
	  String 				v_gubuncddt		= box.getString("p_gubuncddt");
	  String 				v_result		= box.getString("p_result");
	  String 				v_resultseq		= "0";
	  String 				s_userid 		= box.getSession("userid");
	  String 				v_comp			= "";
	  String 				v_jikup			= "";
	  //String                v_jikryul           = "";
	  String 				v_sex			= "";
	  
	  String                v_selmonth      = "3";
	  
	  
	  try {
		  connMgr     = new DBConnectionManager();
		  list        = new ArrayList();
		  
		  //직급 직위 성별 가져오기.
		  /*
	      sql  = "SELECT 	COMP, JIKUP, JIKRYUL, SEX					";
	      sql += "FROM 		TZ_MEMBER																";
	      sql += "WHERE 	USERID			= '"+ s_userid +"'  		 		";
	      ls = connMgr.executeQuery(sql);
	  
	      if ( ls.next() ) {
		      v_jikup = ls.getString("jikup");
		      v_comp	= ls.getString("comp");
		      v_jikryul = ls.getString("jikryul");
		      v_sex		= ls.getString("sex");
	      }
		  
		  if(ls!=null)ls.close();
		  
		  //3개월전 평가 데이터 가져오기
		  sql  = "SELECT 	NVL(MAX(RESULTSEQ),0) RESULTSEQ					";
		  sql += "FROM 		TZ_ABILITY_RSLT_MAS											";
		  sql += "WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 				";
		  sql += "AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   		";
		  sql += "AND 		USERID			= '"+ s_userid +"'  		 		";
		  sql += "AND 		LDATE				<= TO_CHAR(ADD_MONTHS(SYSDATE, -"+ v_selmonth +"),'YYYYMMDDHH24MISS')		";
		  ls = connMgr.executeQuery(sql);
		  */
		  
	      /* 소속만 가져온다. */
	      sql  = "SELECT COMP \n ";
	      sql += "  FROM TZ_MEMBER \n ";
	      sql += " WHERE USERID = '"+ s_userid +"' ";
	      ls = connMgr.executeQuery(sql);

	      if ( ls.next() ) {
	        v_comp	= ls.getString("comp");
	      }      
	      if(ls!=null)ls.close();

	      //3개월전 평가 데이터의 데이터를 가져온다.(직급, 성별)
	      sql  =  " select resultseq, jikup, sex \n "; 
	      sql  += "   from TZ_ABILITY_RSLT_MAS \n "; 
	      sql  += "  where resultseq = ( \n "; 
	      sql  += "                      select NVL(MAX(resultseq),0) resultseq \n "; 
	      sql  += "                        from TZ_ABILITY_RSLT_MAS \n "; 
	      sql  += "                     ) ";
	      sql  += "    and gubuncd 		= '"+ v_gubuncd +"' \n "; 
	      sql  += "    and gubuncddt 	= '"+ v_gubuncddt +"' \n "; 
	      sql  += "    and userid = '"+ s_userid +"' \n "; 
	      sql  += "    and ldate <= to_char(add_months(sysdate, -"+ v_selmonth +"),'yyyymmddhh24miss') \n ";       
	      ls = connMgr.executeQuery(sql); 		  
		  
		  if ( ls.next() ) {
		        v_resultseq = ls.getString("resultseq");
		        v_jikup = ls.getString("jikup");
		        v_sex		= ls.getString("sex");
		  }
		  if(ls!=null)ls.close();
		  
		  sql  = "SELECT	A.ABILITYNM, TO_CHAR(NVL(B.ABILITYPOINT,0)) AS MY_ABL, 												";
		  sql += "				TO_CHAR(NVL(C.ABILITYPOINT,0)) AS TOT_ABL																			";
		  sql += "FROM		TZ_ABILITY A																																	";
		  sql += "					LEFT OUTER JOIN (																														";
		  sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY, ABILITYPOINT											";
		  sql += "								FROM		TZ_ABILITY_RSLT_ABL																						";
		  sql += "								WHERE		USERID 		= '"+ s_userid +"'																	";
		  sql += "								AND			RESULTSEQ = '"+ v_resultseq +"'	 ) B													";
		  sql += "						ON	A.GUBUNCD		= B.GUBUNCD																								";
		  sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT																							";
		  sql += "						AND	A.ABILITY		= B.ABILITY																								";
		  sql += "					LEFT OUTER JOIN (																														";
		  sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY, 																	";
		  sql += "												ROUND(AVG(ABILITYPOINT),1) ABILITYPOINT												";
		  sql += "								FROM		TZ_ABILITY_RSLT_ABL																						";
		  sql += "								WHERE 	GUBUNCD 		= '"+ v_gubuncd +"' 															";
		  sql += "								AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"'   													";
		  if (v_result.equals("2")){			//직급
			  sql += "	AND	USERID IN (																			";
			  sql += "					SELECT 	USERID 															";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS 								";
			  sql += "					WHERE 	JIKUP 			= '"+ v_jikup +"'				";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' 		";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) ";
		  }
		  else if (v_result.equals("3")){	//성별
			  sql += "	AND	USERID IN (																			";
			  sql += "					SELECT 	USERID 															";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS 								";
			  sql += "					WHERE 	SEX 				= '"+ v_sex +"'					";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' 		";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) ";
		  }
		/*
	      else if (v_result.equals("4")){	//직렬(제거)
	         sql += "	AND	USERID IN (																			";
	         sql += "					SELECT 	USERID 															";
	         sql += "					FROM 		TZ_ABILITY_RSLT_MAS 								";
	         sql += "					WHERE 	JIKRYUL	 			= '"+ v_jikryul +"'				";
	         sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' 		";
	         sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) ";
	      }
		*/
		  else if (v_result.equals("5")){	//소속기관
			  sql += "	AND	USERID IN (																			";
			  sql += "					SELECT 	USERID 															";
			  sql += "					FROM 		TZ_ABILITY_RSLT_MAS 								";
			  sql += "					WHERE 	COMP	 			= '"+ v_comp +"'				";
			  sql += "					AND	 		GUBUNCD 		= '"+ v_gubuncd +"' 		";
			  sql += "					AND 		GUBUNCDDT 	= '"+ v_gubuncddt +"' ) ";
		  }
		  sql += "								GROUP BY GUBUNCD, GUBUNCDDT, ABILITY) C																";
		  sql += "						ON	A.GUBUNCD		= C.GUBUNCD																								";
		  sql += "						AND	A.GUBUNCDDT	= C.GUBUNCDDT																							";
		  sql += "						AND	A.ABILITY		= C.ABILITY																								";
		  sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' 																						";
		  sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"'   																				";
		  sql += "ORDER BY A.ABILITY ASC                                                                ";
		  ls = connMgr.executeQuery(sql);
		  
		  
		  while ( ls.next() ) {
			  dbox = ls.getDataBox();
			  list.add(dbox);
		  }
		  if(ls!=null)ls.close();
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
    3개월간 과정 수강 이력
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilitySubjStudent3(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd			= box.getString("p_gubuncd");
    String 				v_gubuncddt		    = box.getString("p_gubuncddt");
    String 				v_result			= box.getString("p_result");
    String 				v_resultseq		    = "0";
    String 				s_userid 			= box.getSession("userid");
    String 				v_comp				= "";
    String 				v_jikup				= "";
    String 				v_sex				= "";

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();

      sql  = "SELECT	A.ABILITY, A.ABILITYNM, C.SUBJ, \n ";
      sql += "				(SELECT SUBJNM FROM TZ_SUBJ X WHERE X.SUBJ = C.SUBJ) AS SUBJNM \n ";
      sql += "FROM		TZ_ABILITY A \n ";
      sql += "					LEFT OUTER JOIN TZ_ABILITY_SUBJ B \n ";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= B.ABILITY \n ";
      sql += "					LEFT OUTER JOIN ( \n ";
      sql += "								SELECT	UNIQUE SUBJ \n ";
      sql += "								FROM		TZ_STUDENT \n ";
      sql += "								WHERE		USERID 	= '"+ s_userid +"' \n ";
      sql += "								AND			LDATE		<= TO_CHAR(ADD_MONTHS(SYSDATE, -3),'YYYYMMDDHH24MISS') \n ";
      sql += "							) C \n ";
      sql += "						ON	B.SUBJ			= C.SUBJ \n ";
      sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      sql += "ORDER BY  A.ABILITY, A.ORDERS, B.ORDERS \n ";
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
    3개월 전까지 과정 수강 이력
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilitySubjStudent(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd			= box.getString("p_gubuncd");
    String 				v_gubuncddt		    = box.getString("p_gubuncddt");
    String 				v_result			= box.getString("p_result");
    String 				v_resultseq		    = "0";
    String 				s_userid 			= box.getSession("userid");
    String 				v_comp				= "";
    String 				v_jikup				= "";
    String 				v_sex				= "";

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();

      sql  = "SELECT	A.ABILITY, A.ABILITYNM, C.SUBJ, \n ";
      sql += "				(SELECT SUBJNM FROM TZ_SUBJ X WHERE X.SUBJ = C.SUBJ) AS SUBJNM \n ";
      sql += "FROM		TZ_ABILITY A																																	";
      sql += "					LEFT OUTER JOIN TZ_ABILITY_SUBJ B \n ";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= B.ABILITY \n ";
      sql += "					LEFT OUTER JOIN ( \n ";
      sql += "								SELECT	UNIQUE SUBJ \n ";
      sql += "								FROM		TZ_STUDENT \n ";
      sql += "								WHERE		USERID 	= '"+ s_userid +"' \n ";
      sql += "								AND			LDATE		> TO_CHAR(ADD_MONTHS(SYSDATE, -3),'YYYYMMDDHH24MISS') \n 	";
      sql += "							) C	\n ";
      sql += "						ON	B.SUBJ			= C.SUBJ \n ";
      sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      sql += "ORDER BY A.ABILITY, A.ORDERS, B.ORDERS \n ";
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
    나에게 추천 하는 과정
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilityBestSubj(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd			= box.getString("p_gubuncd");
    String 				v_gubuncddt		    = box.getString("p_gubuncddt");
    String 				v_result			= box.getString("p_result");
    String 				v_resultseq		    = box.getString("p_resultseq");
    String 				s_userid 			= box.getSession("userid");

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();

      sql  = "SELECT	A.ABILITY, A.ABILITYNM, B.SUBJ,	\n ";
      sql += "				(SELECT SUBJNM FROM TZ_SUBJ X WHERE X.SUBJ = B.SUBJ) AS SUBJNM \n ";
      sql += "FROM		TZ_ABILITY A \n ";
      sql += "					INNER JOIN TZ_ABILITY_SUBJ B \n ";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= B.ABILITY \n ";
      sql += "					INNER JOIN ( \n ";
      sql += "								SELECT	GUBUNCD, GUBUNCDDT, ABILITY \n ";
      sql += "								FROM		( \n ";
      sql += "												SELECT	GUBUNCD, GUBUNCDDT, ABILITY, ABILITYPOINT \n ";
      sql += "												FROM		TZ_ABILITY_RSLT_ABL \n ";
      sql += "												WHERE		USERID 		= '"+ s_userid +"' \n ";
      sql += "												AND			RESULTSEQ = '"+ v_resultseq +"' \n ";
      sql += "												ORDER BY ABILITYPOINT ASC)	X \n ";
      sql += "								WHERE			ROWNUM		< 3 \n ";
      sql += "								) C \n ";
      sql += "						ON	A.GUBUNCD		= C.GUBUNCD \n ";
      sql += "						AND	A.GUBUNCDDT	= C.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= C.ABILITY \n ";
      sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      sql += "ORDER BY A.ORDERS, B.ORDERS \n ";
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
    직급 추천 (최근 3개월간 수강한 교육과정)
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
  public ArrayList ablilityBestSubjGroup(RequestBox box) throws Exception {
    DBConnectionManager	connMgr		= null;
    DataBox             dbox    	= null;
    ListSet				ls			= null;
    ArrayList			list		= null;
    String				sql			= "";

    String 				v_gubuncd			= box.getString("p_gubuncd");
    String 				v_gubuncddt		    = box.getString("p_gubuncddt");
    String 				v_result			= box.getString("p_result");
    String 				v_jikup				= box.getString("p_jikup");
    String 				s_userid 			= box.getSession("userid");

    try {
      connMgr     = new DBConnectionManager();
      list        = new ArrayList();
      /*
      sql  = "SELECT	A.ABILITY, A.ABILITYNM, B.SUBJ, \n ";
      sql += "				(SELECT SUBJNM FROM TZ_SUBJ X WHERE X.SUBJ = B.SUBJ) AS SUBJNM \n ";
      sql += "FROM		TZ_ABILITY A \n ";
      sql += "					INNER JOIN TZ_ABILITY_SUBJ B \n ";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= B.ABILITY \n ";
      sql += "					INNER JOIN  ( \n ";
      sql += "								SELECT	UNIQUE SUBJ \n ";
      sql += "								FROM		TZ_STUDENT \n ";
      sql += "								WHERE		JIKUP 	= '"+ v_jikup +"' \n ";
      sql += "								AND			LDATE		> TO_CHAR(ADD_MONTHS(SYSDATE, -3),'YYYYMMDDHH24MISS')	\n ";
      sql += "							) C \n ";
      sql += "						ON	B.SUBJ			= C.SUBJ \n ";
      sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      sql += "ORDER BY A.ORDERS, B.ORDERS \n ";
      */
      sql  = "SELECT	A.ABILITY, A.ABILITYNM, B.SUBJ, \n ";
      sql += "				(SELECT SUBJNM FROM TZ_SUBJ X WHERE X.SUBJ = B.SUBJ) AS SUBJNM \n ";
      sql += "FROM		TZ_ABILITY A \n ";
      sql += "					INNER JOIN TZ_ABILITY_SUBJ B \n ";
      sql += "						ON	A.GUBUNCD		= B.GUBUNCD \n ";
      sql += "						AND	A.GUBUNCDDT	= B.GUBUNCDDT \n ";
      sql += "						AND	A.ABILITY		= B.ABILITY \n ";
      sql += "					INNER JOIN  ( \n ";
      sql += "								SELECT	UNIQUE SUBJ \n ";
      sql += "								FROM		TZ_STUDENT \n ";
      sql += "								WHERE		1=1 \n ";
      sql += "								AND			LDATE		> TO_CHAR(ADD_MONTHS(SYSDATE, -3),'YYYYMMDDHH24MISS')	\n ";
      sql += "							) C \n ";
      sql += "						ON	B.SUBJ			= C.SUBJ \n ";
      sql += "WHERE 	A.GUBUNCD 		= '"+ v_gubuncd +"' \n ";
      sql += "AND 		A.GUBUNCDDT 	= '"+ v_gubuncddt +"' \n ";
      sql += "ORDER BY A.ORDERS, B.ORDERS \n ";
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
	역량 평가 삭제할때
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail
	*/
	public int delete(RequestBox box) throws Exception {
		DBConnectionManager	connMgr	= null;
		ListSet             ls1		= null;
		ListSet             ls2		= null;
		ListSet             ls3		= null;

		PreparedStatement   pstmt1	= null;
		PreparedStatement   pstmt2	= null;
		PreparedStatement   pstmt3	= null;

		StringBuffer        sql     = new StringBuffer();

		int isOk1 = 1;
		int isOk2 = 1;
		int isOk3 = 1;
		int v_cnt = 0;
		int idx   = 1;

		String v_gubuncd 		= box.getString("p_gubuncd");
		String v_gubuncddt 		= box.getString("p_gubuncddt");
		int	 v_resultseq		= box.getInt("p_resultseq");
		String s_userid 		= box.getSession("userid");

    	try {
      		connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			//해당 문항 등록 여부
			sql.append("SELECT COUNT(userid) AS cnt ");
			sql.append(" FROM TZ_ABILITY_RSLT_DT ");
			sql.append(" WHERE GUBUNCD = '"+ v_gubuncd +"' ");
			sql.append(" 	AND GUBUNCDDT = '"+ v_gubuncddt +"' ");
			sql.append(" 	AND USERID = '"+ s_userid +"' ");
			sql.append(" 	AND RESULTSEQ = '"+ v_resultseq +"' ");

			ls1 = connMgr.executeQuery(sql.toString());

			if ( ls1.next() ) {
				v_cnt = ls1.getInt("cnt");
			}
			if(ls1 != null) ls1.close();

			sql     = new StringBuffer();

			//문항 입력/수정
			if (v_cnt > 0){
				sql.append(" DELETE FROM TZ_ABILITY_RSLT_DT ");
				sql.append(" WHERE GUBUNCD 		= ? ");
				sql.append(" 	AND GUBUNCDDT 	= ? ");
				sql.append(" 	AND USERID 		= ? ");
				sql.append(" 	AND RESULTSEQ 	= ? ");

				pstmt1 = connMgr.prepareStatement(sql.toString());

				idx = 1;

				pstmt1.setString(idx++, v_gubuncd);
				pstmt1.setString(idx++, v_gubuncddt);
				pstmt1.setString(idx++, s_userid);
				pstmt1.setInt		(idx++, v_resultseq);

	      		isOk1 = pstmt1.executeUpdate();

	      		if(pstmt1 != null) pstmt1.close();
      		}

			sql     = new StringBuffer();

			//해당 문항 마스터 등록 여부
			sql.append("SELECT COUNT(userid) AS cnt ");
			sql.append(" FROM TZ_ABILITY_RSLT_MAS ");
			sql.append(" WHERE GUBUNCD = '"+ v_gubuncd +"' ");
			sql.append(" 	AND GUBUNCDDT = '"+ v_gubuncddt +"' ");
			sql.append(" 	AND USERID = '"+ s_userid +"' ");
			sql.append(" 	AND RESULTSEQ = '"+ v_resultseq +"' ");

			ls2 = connMgr.executeQuery(sql.toString());

			if ( ls2.next() ) {
				v_cnt = ls2.getInt("cnt");
			} else {
				v_cnt = 0;
			}
			if(ls2 != null) ls2.close();

			sql     = new StringBuffer();

			//문항 입력/수정
			if (v_cnt > 0){
				sql.append(" DELETE FROM TZ_ABILITY_RSLT_MAS ");
				sql.append(" WHERE GUBUNCD 		= ? ");
				sql.append(" 	AND GUBUNCDDT 	= ? ");
				sql.append(" 	AND USERID 		= ? ");
				sql.append(" 	AND RESULTSEQ 	= ? ");

				pstmt2 = connMgr.prepareStatement(sql.toString());

				idx = 1;

				pstmt2.setString(idx++, v_gubuncd);
				pstmt2.setString(idx++, v_gubuncddt);
				pstmt2.setString(idx++, s_userid);
				pstmt2.setInt	(idx++, v_resultseq);

	      		isOk2 = pstmt2.executeUpdate();

	      		if(pstmt2 != null) pstmt2.close();
      		}

			sql     = new StringBuffer();

			//해당 진단 실시 여부
			sql.append("SELECT COUNT(userid) AS cnt ");
			sql.append(" FROM TZ_ABILITY_RSLT_ABL ");
			sql.append(" WHERE GUBUNCD = '"+ v_gubuncd +"' ");
			sql.append(" 	AND GUBUNCDDT = '"+ v_gubuncddt +"' ");
			sql.append(" 	AND USERID = '"+ s_userid +"' ");
			sql.append(" 	AND RESULTSEQ = '"+ v_resultseq +"' ");

			ls3 = connMgr.executeQuery(sql.toString());

			if ( ls3.next() ) {
				v_cnt = ls3.getInt("cnt");
			} else {
				v_cnt = 0;
			}
			if(ls3 != null) ls3.close();

			sql     = new StringBuffer();

			//문항 입력/수정
			if (v_cnt > 0){
				sql.append(" DELETE FROM TZ_ABILITY_RSLT_ABL ");
				sql.append(" WHERE GUBUNCD 		= ? ");
				sql.append(" 	AND GUBUNCDDT 	= ? ");
				sql.append(" 	AND USERID 		= ? ");
				sql.append(" 	AND RESULTSEQ 	= ? ");

				pstmt3 = connMgr.prepareStatement(sql.toString());

				idx = 1;

				pstmt3.setString(idx++, v_gubuncd);
				pstmt3.setString(idx++, v_gubuncddt);
				pstmt3.setString(idx++, s_userid);
				pstmt3.setInt	(idx++, v_resultseq);

	      		isOk3 = pstmt3.executeUpdate();

	      		if(pstmt3 != null) pstmt3.close();
      		}
			if (isOk1 * isOk2 * isOk3 > 0 ) {
				connMgr.commit();
			} else {
				connMgr.rollback();
			}

		} catch ( Exception ex ) {
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql.toString());
			throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage() );
		} finally {
			connMgr.setAutoCommit(true);
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
			if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { } }
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
			if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
			if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return isOk1 * isOk2 * isOk3;
	}

}