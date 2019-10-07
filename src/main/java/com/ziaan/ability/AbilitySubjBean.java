// **********************************************************
//  1. 제      목: 과정맵핑
//  2. 프로그램명: AbilitySubjBean.java
//  3. 개      요: 과정맵핑
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

public class AbilitySubjBean {

    public AbilitySubjBean() { }

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
        String				v_ability			= box.getString("p_ability");

        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();

            sql  = "SELECT	A.SUBJ, A.ORDERS, A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, B.ABILITYNM, 					";
            sql += "				C.GUBUNCDNM, D.GUBUNCDDTNM, E.SUBJNM, E.ISONOFF,														";
            sql += "				(SELECT CODENM FROM TZ_CODE WHERE GUBUN = '0004' AND CODE = E.ISONOFF) AS ISONOFFNM	";
            sql += "FROM		TZ_ABILITY_SUBJ A, TZ_ABILITY B, TZ_ABILITY_CODE C, TZ_ABILITY_CODE_DT	D,	";
            sql += "				TZ_SUBJ E																																		";
            sql += "WHERE		A.ABILITY		= B.ABILITY																											";
            sql += "AND			A.GUBUNCD		= B.GUBUNCD																											";
            sql += "AND			A.GUBUNCDDT	= B.GUBUNCDDT																										";
            sql += "AND			A.SUBJ			= E.SUBJ																												";
            sql += "AND			B.GUBUNCD		= C.GUBUNCD																											";
            sql += "AND			B.GUBUNCD		= D.GUBUNCD																											";
            sql += "AND			B.GUBUNCDDT	= D.GUBUNCDDT																										";
            if (v_gubuncd != null && !v_gubuncd.equals("")){
            	sql += "AND		A.GUBUNCD 	= '"+ v_gubuncd +"'																							";
            }
            if (v_gubuncddt != null && !v_gubuncddt.equals("")){
            	sql += "AND		A.GUBUNCDDT = '"+ v_gubuncddt +"'																						";
            }
            if (v_ability != null && !v_ability.equals("")){
            	sql += "AND		A.ABILITY = '"+ v_ability +"'																								";
            }

            sql += "ORDER BY A.ORDERS ASC, A.SUBJ ASC																										";
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
    과정 존재여부 체크
    @param box          receive from the form object and session
    @return DataBox
    */
    public int subjExistsChk(String p_gubuncd, String p_gubuncddt, String p_ability, String p_subj) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet							ls				= null;
        String							sql				= "";
        int									v_subcnt	= 0;

        try {
            connMgr     = new DBConnectionManager();


            sql  = "SELECT	COUNT(SUBJ) AS SUBCNT \n ";
            sql += "FROM		TZ_ABILITY_SUBJ A \n ";
            sql += "WHERE		A.GUBUNCD 	= '"+ p_gubuncd +"' \n ";
           	sql += "AND			A.GUBUNCDDT = '"+ p_gubuncddt +"' \n ";
           	sql += "AND			A.ABILITY 	= '"+ p_ability +"' \n ";
           	sql += "AND			A.SUBJ			= '"+ p_subj +"' \n ";
            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                v_subcnt = ls.getInt("SUBCNT");
            }
        } catch ( Exception e ) {
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) {  try { ls.close();  } catch ( Exception e ) { }  }
            if ( connMgr != null ) {  try {  connMgr.freeConnection();  } catch (Exception e ) { } }
        }

        return v_subcnt;
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
        int v_subjcnt = 0;

        String v_gubuncd 				= box.getString("p_gubuncd");
        String v_gubuncddt 			= box.getString("p_gubuncddt");
        String v_ability	 			= box.getString("p_ability");
        String v_subj []				= box.getString("p_subj").split("[,]");
        int		 v_orders		 			= box.getInt("p_orders");
        String v_subjnm					= box.getString("p_subjdesc");
        String s_userid 				= box.getSession("userid");


        try {
					connMgr = new DBConnectionManager();

					//먼저 싸그리 지운다.
					sql1 =  "DELETE												";
					sql1 += "FROM		TZ_ABILITY_SUBJ				";
					sql1 += "WHERE	GUBUNCD		= ?					";
					sql1 +=	"AND		GUBUNCDDT	= ?					";
					sql1 += "AND		ABILITY		= ?					";
					pstmt = connMgr.prepareStatement(sql1);

					pstmt.setString(1, v_gubuncd);
					pstmt.setString(2, v_gubuncddt);
					pstmt.setString(3, v_ability);
					isOk = pstmt.executeUpdate();
					
					if ( pstmt != null ) { pstmt.close(); }

					sql1 =  "INSERT INTO TZ_ABILITY_SUBJ(GUBUNCD, GUBUNCDDT, ABILITY, SUBJ, ORDERS, INDATE, INUSERID, LDATE, LUSERID)   ";
					sql1 += " VALUES (?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?)			";

					pstmt = connMgr.prepareStatement(sql1);
					for (int i = 0; i < v_subj.length; i++){
						pstmt.clearParameters();
						pstmt.setString(1, v_gubuncd);
						pstmt.setString(2, v_gubuncddt);
						pstmt.setString(3, v_ability);
						pstmt.setString(4, v_subj[i]);
						pstmt.setInt	 (5, i+1);
						pstmt.setString(6, s_userid);
						pstmt.setString(7, s_userid);

						isOk = pstmt.executeUpdate();
					}
					
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

        String v_gubuncd 				= box.getString("p_gubuncd");
        String v_gubuncddt 			= box.getString("p_gubuncddt");
        String v_ability	 			= box.getString("p_ability");
        String v_subj			= box.getString("p_subj");
        int		 v_orders		 			= box.getInt("p_orders");
        String v_subjnm	= box.getString("p_subjdesc");
        String s_userid 				= box.getSession("userid");


        try {
            connMgr = new DBConnectionManager();

            sql  = "UPDATE 	TZ_ABILITY_SUBJ	 																				";
            sql += "SET			ORDERS					= ? 	,																	";
            sql += "				ABILITYPRODESC	= ? 	,																	";
            sql += "				LUSERID					= ? 	, 																";
            sql += "				LDATE 					= TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') 	";
            sql += "WHERE		GUBUNCD					= ?                          						";
            sql += "AND			GUBUNCDDT				= ?                          						";
            sql += "AND			ABILITY					= ?                          						";
            sql += "AND			ABILITYPRO			= ?                          						";

            pstmt = connMgr.prepareStatement(sql);

						pstmt.setInt	 (1, v_orders);
            pstmt.setString(2, v_subjnm);
            pstmt.setString(3, s_userid);
            pstmt.setString(4, v_gubuncd);
            pstmt.setString(5, v_gubuncddt);
            pstmt.setString(6, v_ability);
            pstmt.setString(7, v_subj);

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

        String v_gubuncd 		= box.getString("p_gubuncd");
        String v_gubuncddt 	= box.getString("p_gubuncddt");
        String v_ability 		= box.getString("p_ability");
        String v_subj	= box.getString("p_subj");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = "DELETE 					 																						";
            sql += "FROM		TZ_ABILITY_SUBJ																			";
            sql += "WHERE		GUBUNCD			= ?                          						";
            sql += "AND			GUBUNCDDT		= ?                          						";
            sql += "AND			ABILITY			= ?                          						";
            sql += "AND			ABILITYPRO	= ?                          						";

            pstmt1 = connMgr.prepareStatement(sql);
            pstmt1.setString(1, v_gubuncd);
            pstmt1.setString(2, v_gubuncddt);
            pstmt1.setString(3, v_ability);
            pstmt1.setString(4, v_subj);
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
    
	/**
	 * 역량진단용 과정검색 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
    /*
    public ArrayList SelectSubjectList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        DataBox             dbox            = null;

        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // 교육주관
        String              ss_upperclass   = box.getStringDefault("s_upperclass"   , "ALL");   // 과목분류
        String              ss_middleclass  = box.getStringDefault("s_middleclass"  , "ALL");   // 과목분류
        String              ss_lowerclass   = box.getStringDefault("s_lowerclass"   , "ALL");   // 과목분류
        String              ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");       // 과목&코스
        String              v_searchtext    = box.getString       ("p_searchtext");
                            
        String              v_orderColumn   = box.getString       ("p_orderColumn"         );   // 정렬할 컬럼명
        String              v_orderType     = box.getString       ("p_orderType"           );   // 정렬할 순서

        String v_searchGu1 = box.getString("p_searchGu1");
        String v_searchGu2 = box.getString("p_searchGu2");

        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            if ( ss_grcode.equals("ALL") ) { 
                sbSQL.append(" select  a.upperclass, b.classname   , a.isonoff     , c.codenm               \n")
                     .append("     ,   a.subj      , a.subjnm      , a.muserid     , d.name                 \n")
                     .append("     ,   a.isuse     , '' grcode     , a.isapproval  , a.isintroduction       \n")
                     .append("     ,   a.introducefilenamereal     , a.introducefilenamenew                 \n")
                     .append("     ,   a.informationfilenamereal   , a.informationfilenamenew               \n")
                     .append("     ,   nvl(a.subj_gu,'') as subj_gu                                         \n")
                     .append("     ,   a.content_cd				                                            \n")
                     .append("     ,   nvl(a.study_count,0) as study_count                                  \n")
                     .append("     ,   a.ldate, a.contenttype                                            	\n")
                     .append("     ,   a.edutimes, a.score, a.eduperiod                                    	\n")
                     .append(" from    tz_subj     a                                                        \n")
                     .append("     ,   tz_subjatt  b                                                        \n")
                     .append("     ,   tz_code     c                                                        \n")
                     .append("     ,   tz_member   d                                                        \n")
                     .append(" where   a.upperclass  = b.upperclass                                         \n")
                     .append(" and     a.isonoff     = c.code                                               \n")
                     .append(" and     a.muserid     = d.userid(+)    	                                    \n")
                     .append(" and     b.middleclass = '000'                                                \n")
                     .append(" and     b.lowerclass  = '000'                                                \n")
                     .append(" and     c.gubun       = '0004'                                               \n");
            } else { 
                sbSQL.append(" select  a.upperclass,   b.classname , a.isonoff     , c.codenm               \n")
                     .append("     ,   a.subj      ,   a.subjnm    , a.muserid     , d.name                 \n") 
                     .append("     ,   a.isuse     ,   e.grcode    , a.isapproval  , a.isintroduction       \n")
                     .append("     ,   a.introducefilenamereal     , a.introducefilenamenew                 \n")
                     .append("     ,   a.informationfilenamereal   , a.informationfilenamenew               \n")
                     .append("     ,   nvl(a.subj_gu,'') as subj_gu                                         \n")
                     .append("     ,   a.content_cd				                                            \n")
                     .append("     ,   nvl(a.study_count,0) as study_count                                  \n")
                     .append("     ,   a.ldate, a.contenttype				                                \n")
                     .append("     ,   a.edutimes, a.score, a.eduperiod                                    	\n")
                     .append(" from    tz_subj     a                                                        \n")
                     .append("     ,   tz_subjatt  b                                                        \n")
                     .append("     ,   tz_code     c                                                        \n")
                     .append("     ,   tz_member   d                                                        \n")
                     .append("     ,   tz_grsubj   e                                                        \n")
                     .append("     ,   tz_grcode   f                                                        \n")
                     .append(" where   a.upperclass  = b.upperclass                                         \n")
                     .append(" and     a.isonoff     = c.code                                               \n")
                     .append(" and     a.muserid     = d.userid(+)	                                        \n")
                     .append(" and     b.middleclass = '000'                                                \n")
                     .append(" and     b.lowerclass  = '000'                                                \n")
                     .append(" and     a.subj        = e.subjcourse		                                    \n")
                     .append(" and     e.grcode      = f.grcode	 	                                        \n")
                     .append(" and     c.gubun       = '0004'                                               \n")
                     .append(" and     e.grcode = " + StringManager.makeSQL(ss_grcode)   + " 				\n");
            }

            if ( !ss_subjcourse.equals("ALL") ) { 
                sbSQL.append(" and a.subj = " + StringManager.makeSQL(ss_subjcourse) + " \n");
            } else { 
                if ( !ss_upperclass.equals("ALL") ) { 
                    if ( !ss_upperclass.equals("ALL") ) { 
                        sbSQL.append(" and     a.upperclass  = " + StringManager.makeSQL(ss_upperclass ) + " \n");
                    }
                    if ( !ss_middleclass.equals("ALL") ) { 
                        sbSQL.append(" and     a.middleclass = " + StringManager.makeSQL(ss_middleclass) + " \n");
                    }
                    if ( !ss_lowerclass.equals("ALL") ) { 
                        sbSQL.append(" and     a.lowerclass  = " + StringManager.makeSQL(ss_lowerclass ) + " \n");
                    }
                }
            }

            if ( !v_searchtext.equals("") ) { 
            	v_searchtext = v_searchtext.replaceAll("'", "");
            	v_searchtext = v_searchtext.replaceAll("/", "//");
            	v_searchtext = v_searchtext.replaceAll("%", "/%").toUpperCase();
                sbSQL.append(" and     (upper(a.subjnm) like " + StringManager.makeSQL("%"+v_searchtext+"%") + " escape '/'  \n");
                sbSQL.append("          or upper(a.subj) like " + StringManager.makeSQL("%"+v_searchtext+"%") + " escape '/')  \n");
            }

            // 교육구분별 조회
            if ( !v_searchGu1.equals("ALL")) {
            	sbSQL.append(" and    a.isonoff = " + StringManager.makeSQL(v_searchGu1) + " \n");
            }
            if ( !v_searchGu2.equals("ALL")) {
            	if ( v_searchGu2.equals("") ) {
            		sbSQL.append(" and    a.subj_gu is null \n");
            	} else {
            		sbSQL.append(" and    a.subj_gu = " + StringManager.makeSQL(v_searchGu2) + " \n");
            	}
            }

            //if(!ss_subjcourse.equals("ALL")) {
            	//sbSQL.append("       AND    a.SUBJ = " + SQLString.Format(ss_subjcourse) + "  \n");            	
            //}
            
            if ( v_orderColumn.equals("") ) { 
                sbSQL.append(" order   by a.subj desc \n");
            } else { 
                sbSQL.append(" order   by " + v_orderColumn + v_orderType + " \n");
            }

            ls = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
				dbox = ls.getDataBox();
				list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch (Exception e ) { } 
            }
        }
        
        return list;
    }	
    */
    
    /**
    역량진단용 과목리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   과목리스트
    */
    public ArrayList SelectSubjectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

		String  v_grcode		= box.getString("p_grcode");					 // 교육그룹
        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");   // 과목분류
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서
        
        String v_gubuncd 		= box.getString("p_gubuncd");
        String v_gubuncddt 	    = box.getString("p_gubuncddt");
        String v_ability 		= box.getString("p_ability");

        try { 

            sql  = "\n select 'S' subjtype, a.upperclass, b.classname, a.isonoff, get_codenm('0004',a.isonoff) as codenm ";
            sql += "\n      , a.subj, a.subjnm, a.isuse ";
            sql += "\n      , (select 'Y' from tz_ability_subj where subj=a.subj and gubuncd="+StringManager.makeSQL(v_gubuncd)+" and gubuncddt="+StringManager.makeSQL(v_gubuncddt)+" and ability="+StringManager.makeSQL(v_ability)+") abilityuse ";
            sql += "\n from   tz_subj a,  tz_subjatt b ";
            sql += "\n where  a.subjclass  = b.subjclass ";
            sql += "\n and    a.isuse     = 'Y' ";
            if ( !ss_upperclass.equals("ALL") ) { 
  	            sql += "\n and a.upperclass = " +SQLString.Format(ss_upperclass);
            }
            if ( !ss_middleclass.equals("ALL") ) { 
            	sql += "\n and a.middleclass = " +SQLString.Format(ss_middleclass);
            }
            if ( !ss_lowerclass.equals("ALL") ) { 
            	sql += "\n and a.lowerclass = " +SQLString.Format(ss_lowerclass);
            }
            
            sql += "\n union all ";
            sql += "\n select 'C' subjtype , '패키지' upperclass, '패키지' classname, '' isonoff, '패키지' codenm, course, coursenm, 'Y' isuse " ;
            sql += "\n      , (select 'Y' from tz_ability_subj where subj=c.course and gubuncd="+StringManager.makeSQL(v_gubuncd)+" and gubuncddt="+StringManager.makeSQL(v_gubuncddt)+" and ability="+StringManager.makeSQL(v_ability)+") abilityuse "; 
            sql += "\n  from tz_course c "; 
            
            if ( v_orderColumn.equals("") ) { 
	            //sql += "\n order by subjtype, upperclass, subjnm ";
            } else { 
            	//sql += "\n order by subjtype, " + v_orderColumn + v_orderType;
	        }            
            

            connMgr = new DBConnectionManager();
            list = new ArrayList();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            	dbox = ls.getDataBox();
            	list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
    
	/**
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ArrayList SelectSubjectList2(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql  = "";
		DataBox             dbox    = null;
		
		String  ss_grcode       = box.getStringDefault("s_grcode","N000001");          //교육주관
		String  ss_gyear  		= box.getStringDefault("s_gyear",FormatDate.getDate("yyyy")); //교육년도
		String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    //과정분류
		String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");   //과정분류
		String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");    //과정분류
		String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");    //과정&코스
		String  ss_isonoff   	= box.getStringDefault("s_isonoff","ALL");    	 //과정유형
		//String  ss_cpsubj   	= box.getStringDefault("s_company","ALL");    	 //교육기관
		String  ss_cpsubj   	= box.getStringDefault("p_cp","ALL");    	     //교육기관
		String ss_isessential   = box.getStringDefault("s_isessential","ALL");   //필수여부
		String  v_searchtext    = box.getString("s_subjsearchkey");
		
		String  v_orderColumn   = box.getString("p_orderColumn");               //정렬할 컬럼명
		String  v_orderType     = box.getString("p_orderType");                 //정렬할 순서
		
		try {
			if (ss_grcode.equals("ALL")) {
				/*
				sql = " select  a.upperclass, b.classname, a.ldate,a.isessential,a.edutimes,a.cpsubj,a.score, \n";
				sql+= "		    (select classname from tz_subjatt where classyear=a.cdate and upperclass=a.upperclass and middleclass=a.middleclass and lowerclass='000') middleclassnm, \n";
				sql+= "		    a.edudays || decode(a.edudays_cd,'01','(일)','02','(주)','03','(월)', '') as edudays, \n";
				sql+= "       a.isonoff, c.codenm,a.subj, a.subjnm, a.muserid, d.name, a.isuse, '' grcode, a.isapproval, intro, \n";
				sql+= "       a.isintroduction, a.introducefilenamereal, a.introducefilenamenew, a.informationfilenamereal, a.informationfilenamenew, e.compnm as cpsubjnm \n";
				sql+= "   from  tz_subj a,  tz_subjatt b,  tz_code c,  tz_member d, tz_compclass e \n";
				sql+= "  where  a.upperclass  = b.upperclass and \n";
				sql+= "         a.isonoff     = c.code and \n";
				sql+= "         a.muserid     = d.userid(+) and \n";
				sql+= "					a.cpsubj 			= e.cpsubj(+) and \n";
				sql+= "         b.middleclass = '000' and \n";
				sql+= "         b.lowerclass  = '000'  \n";
				sql+= "		    -- and a.cdate		  = '" + ss_gyear + "'  ";
				sql+= "         and c.gubun       = " + SQLString.Format(ONOFF_GUBUN);
				*/
				
				sql = " select  a.upperclass, b.classname, a.ldate,a.isessential,a.edutimes,a.cpsubj,a.score, '' as edudays , \n";
				sql+= "         a.isonoff, c.codenm,a.subj, a.subjnm, a.muserid, d.name, a.isuse, '' grcode, a.isapproval, intro, \n";
				sql+= "         a.isintroduction, a.introducefilenamereal, a.introducefilenamenew, a.informationfilenamereal, a.informationfilenamenew, '' as cpsubjnm \n";
				sql+= "   from  tz_subj a,  tz_subjatt b,  tz_code c,  tz_member d --, tz_compclass e \n";
				sql+= "  where  a.upperclass  = b.upperclass and \n";
				sql+= "         a.isonoff     = c.code and \n";
				sql+= "         a.muserid     = d.userid(+) and \n";
				//sql+= "					a.cpsubj 			= e.cpsubj(+) and \n";
				sql+= "         b.middleclass = '000' and \n";
				sql+= "         b.lowerclass  = '000'  \n";
				sql+= "         and c.gubun       = '0004' \n " ;
			} else {
				/*
				sql = " select  a.upperclass, b.classname, a.ldate, a.isessential, a.edutimes, a.cpsubj, a.score, \n";
				sql+= "		    (select classname from tz_subjatt where upperclass = a.upperclass and middleclass = a.middleclass and lowerclass = '000') middleclassnm, \n";
				sql+= "		    	a.edudays || decode(a.edudays_cd,'01','(일)','02','(주)','03','(월)', '') as edudays, \n";
				sql+= "         a.isonoff, c.codenm,a.subj, a.subjnm, a.muserid, a.isuse, a.isapproval,  intro, e.compnm as cpsubjnm, \n";
				sql+= "         a.isintroduction,  a.introducefilenamereal, a.introducefilenamenew, a.informationfilenamereal, a.informationfilenamenew  \n";
				sql+= "   from  tz_subj a,  tz_subjatt b,  tz_code c, tz_compclass e  \n";
				sql+= "  where  a.upperclass  = b.upperclass and \n";
				sql+= "         a.isonoff     = c.code and \n";
				sql+= "		    a.cpsubj 	  = e.comp(+) and \n";
				sql+= "         b.middleclass = '000' and \n";
				sql+= "         b.lowerclass  = '000' and \n";
				sql+= "         c.gubun       = " + SQLString.Format(ONOFF_GUBUN);
				*/
				
				sql = " select  a.upperclass, b.classname, a.ldate, a.isessential, a.edutimes, a.cpsubj, a.score, '' as edudays , \n";
				sql+= "         a.isonoff, c.codenm,a.subj, a.subjnm, a.muserid, a.isuse, a.isapproval,  intro, '' as cpsubjnm, \n";
				sql+= "         a.isintroduction,  a.introducefilenamereal, a.introducefilenamenew, a.informationfilenamereal, a.informationfilenamenew  \n";
				sql+= "   from  tz_subj a,  tz_subjatt b,  tz_code c --, tz_compclass e  \n";
				sql+= "  where  a.upperclass  = b.upperclass and \n";
				sql+= "         a.isonoff     = c.code and \n";
				//sql+= "		    a.cpsubj 	  = e.comp(+) and \n";
				sql+= "         b.middleclass = '000' and \n";
				sql+= "         b.lowerclass  = '000' and \n";
				sql+= "         c.gubun       = '0004'    \n ";
			}
			
			if (!ss_subjcourse.equals("ALL")) {
				sql+= "   and a.subj = " + SQLString.Format(ss_subjcourse);
			} else {
				if (!ss_upperclass.equals("ALL")) {
					if (!ss_upperclass.equals("ALL")) {
						sql += " and a.upperclass = "+SQLString.Format(ss_upperclass);
					}
					if (!ss_middleclass.equals("ALL")) {
						sql += " and a.middleclass = "+SQLString.Format(ss_middleclass);
					}
					/*
                if (!ss_lowerclass.equals("ALL")) {
                	sql += " and a.lowerclass = "+SQLString.Format(ss_lowerclass);
                }
					 */
				}
			}
			
			//과정유형
			if (!ss_isonoff.equals("ALL")) {
				sql+= "   and a.isonoff = " + SQLString.Format(ss_isonoff);
			}
			//과정명
			if(!v_searchtext.equals("")){
				sql += " and a.subjnm like '%"+v_searchtext+"%'";
			}
			//교육기관 ss_cpsubj
			if(!ss_cpsubj.equals("ALL")){
				sql += " and a.cpsubj = " + SQLString.Format(ss_cpsubj);
			}
			//필수여부ss_isessential
			if(!ss_isessential.equals("ALL")){
				sql += " and a.isessential = " + SQLString.Format(ss_isessential);
			}
			
			if(v_orderColumn.equals("")) {
				sql+= " order by a.upperclass, a.subj ";
			} else {
				sql+= " order by " + v_orderColumn + v_orderType;
			}
			
			connMgr = new DBConnectionManager();
			list    = new ArrayList();
			ls      = connMgr.executeQuery(sql);
			
			//        Log.info.println("sql"+sql);
			while (ls.next()) {
				dbox = ls.getDataBox();
				dbox.put("d_edutimes",	new Integer(ls.getInt("edutimes")));
				dbox.put("d_score",		new Integer(ls.getInt("score")));
				
				list.add(dbox);
			}
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list;
	}	    
}
