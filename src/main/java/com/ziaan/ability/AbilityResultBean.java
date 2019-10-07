// **********************************************************
//  1. 제      목: 역량 분류
//  2. 프로그램명: AbilityResultBean.java
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

public class AbilityResultBean {

    public AbilityResultBean() { }

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

        String 				v_gubuncd		= box.getString("p_gubuncd");
        String 				v_gubuncddt		= box.getString("p_gubuncddt");
		String 				v_resultseq		= box.getString("p_resultseq");
		String v_msidecode  		= box.getString("p_msidecode");
		
		System.out.println("v_msidecode::"+v_msidecode);
		
        String				v_search		= box.getString("p_search");
        String				v_started		= box.getString("p_started");
        String				v_ended			= box.getString("p_ended");

        //String				v_comp			= box.getStringDefault("s_comp", "ALL");			//소속기관	
        //String				v_jikryul		= box.getStringDefault("s_jikryul", "ALL");		//직렬
        String				v_comp			= box.getStringDefault("s_company", "ALL");			//소속기관	
        String				v_jikup			= box.getStringDefault("s_jikup", "ALL");			//직급

		if (!v_started.equals("")){
			v_started = v_started.replaceAll("-","");
		}
		if (!v_ended.equals("")){
			v_ended = v_ended.replaceAll("-","");
		}

        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            /*
            sql = " select a.*,b.oneavg,b.twoavg,b.thravg from ( ";
            sql  += "SELECT	B.NAME, A.USERID, A.INDATE, A.RESULTSEQ, TO_CHAR(A.RESULTPOINT) RESULTPOINT,	";
            sql += "				A.GUBUNCD, A.GUBUNCDDT,	C.GUBUNCDNM, D.GUBUNCDDTNM,								 						";
            sql += "				get_jikryulnm (b.jikryul) jikryulnm, get_jikupnm (b.jikryul, b.jikup) jikupnm, b.deptnam, get_compnm (a.comp, b.userid) compnm ";
            sql += " FROM		TZ_MEMBER B, TZ_ABILITY_RSLT_MAS A,  TZ_ABILITY_CODE C, TZ_ABILITY_CODE_DT	D	";
            sql += " WHERE		B.USERID		= A.USERID																												";
            sql += " AND			A.GUBUNCD		= C.GUBUNCD																												";
            sql += " AND			A.GUBUNCD		= D.GUBUNCD																												";
            sql += " AND			A.GUBUNCDDT	= D.GUBUNCDDT																											"; 
             */
			sql = " select a.*,b.oneavg,b.twoavg,b.thravg from ( \n ";
            sql  += "SELECT	B.NAME, A.USERID, A.INDATE, A.RESULTSEQ, TO_CHAR(A.RESULTPOINT) RESULTPOINT, \n ";
            sql += "				A.GUBUNCD, A.GUBUNCDDT,	C.GUBUNCDNM, D.GUBUNCDDTNM, \n ";
            sql += "				'' jikryulnm, get_codenm ('0113', a.jikup) jikupnm, b.position_nm, get_compnm (a.comp) compnm \n ";
            sql += " FROM		TZ_MEMBER B, TZ_ABILITY_RSLT_MAS A,  TZ_ABILITY_CODE C, TZ_ABILITY_CODE_DT	D \n ";
            sql += " WHERE		B.USERID		= A.USERID \n ";
            sql += " AND			A.GUBUNCD		= C.GUBUNCD \n ";
            sql += " AND			A.GUBUNCD		= D.GUBUNCD \n ";
            sql += " AND			A.GUBUNCDDT	= D.GUBUNCDDT \n ";
            
            if (v_gubuncd != null && !v_gubuncd.equals("")){
            	sql += " AND		A.GUBUNCD 	= '"+ v_gubuncd +"' \n ";
            }
            if (v_gubuncddt != null && !v_gubuncddt.equals("")){
            	sql += " AND		A.GUBUNCDDT = '"+ v_gubuncddt +"' \n ";
            }
            if (v_search != null && !v_search.equals("")){
            	sql += " AND	(	B.USERID LIKE '"+ v_search +"%' \n ";
            	sql += " OR		B.NAME	 LIKE '"+ v_search +"%') \n ";
            }
            //소속기관
            if (!v_comp.equals("ALL")){
            	sql += " AND	a.comp = "+ v_comp +" \n";
            }
            //직렬
            /* 직렬 사용하지 않음
            if (!v_jikryul.equals("ALL")){
            	sql += " AND	a.jikryul = "+ v_jikryul +" \n";
            }
            */
            
            //직급
            if (!v_jikup.equals("ALL")){
            	sql += " AND	a.jikup = "+ v_jikup +" \n";
            }

			if (!v_started.equals("")) {
				if(v_ended.equals("")){
					sql+= " and A.INDATE >= " + v_started + "000000";
				}
				else{
					sql+= " and A.INDATE >= " + v_started + "000000";
					sql+= " and A.INDATE <= " + v_ended + "235999";
				}
			}
			
			sql += " 		)a,      \n";
			sql += " 		( \n";
			sql += " 		select userid, TO_CHAR(sum(oneavg))oneavg, TO_CHAR(sum(twoavg))twoavg, TO_CHAR(sum(thravg))thravg from( \n";
			sql += " 		select * from ( \n";
			sql += " 	select a.userid, round(avg(c.inputs),1) as oneavg , 0 twoavg, 0 thravg from  tz_ability_rslt_mas a, TZ_MSIDEPAR_TARGET b , tz_ability_rslt_dt c \n";
			sql += " 	where 1=1 \n";
			sql += " 	and a.userid=b.tuserid \n";
			sql += " 	and a.resultseq=b.MSIDECODE \n";
			sql += " 	and a.RESULTSEQ=c.RESULTSEQ \n";
			sql += " 	and b.suserid=c.puserid \n";
			sql += " 	and b.tuserid=c.userid \n";
			sql += " 	and b.IDGUBUN='1' \n";
            if (v_gubuncd != null && !v_gubuncd.equals("")){
            	sql += " AND		A.GUBUNCD 	= '"+ v_gubuncd +"'";
            }
            if (v_gubuncddt != null && !v_gubuncddt.equals("")){
            	sql += " AND		A.GUBUNCDDT = '"+ v_gubuncddt +"'";
            }
			sql += " 	group by a.userid \n";
			sql += " 		)  \n";
			sql += " 	union all \n";
			sql += " 	( \n";
			sql += " 	select a.userid,  0 oneavg , round(avg(c.inputs),1) as twoavg, 0thravg from  tz_ability_rslt_mas a, TZ_MSIDEPAR_TARGET b , tz_ability_rslt_dt c \n";
			sql += " 	where 1=1 \n";
			sql += " 	and a.userid=b.tuserid \n";
			sql += " 	and a.resultseq=b.MSIDECODE \n";
			sql += " 	and a.RESULTSEQ=c.RESULTSEQ \n";
			sql += " 	and b.suserid=c.puserid \n";
			sql += " 	and b.tuserid=c.userid \n";
			sql += " 	and b.IDGUBUN='2' \n";
            if (v_gubuncd != null && !v_gubuncd.equals("")){
            	sql += " AND		A.GUBUNCD 	= '"+ v_gubuncd +"'";
            }
            if (v_gubuncddt != null && !v_gubuncddt.equals("")){
            	sql += " AND		A.GUBUNCDDT = '"+ v_gubuncddt +"'";
            }
			sql += " 	group by a.userid \n";
			sql += " 	) \n";
			sql += " 	 union all \n";
			sql += " 	( \n";
			sql += " 	select a.userid, 0 oneavg , 0 twoavg, round(avg(c.inputs),1) as thravg from  tz_ability_rslt_mas a, TZ_MSIDEPAR_TARGET b , tz_ability_rslt_dt c \n";
			sql += " 	where 1=1 \n";
			sql += " 	and a.userid=b.tuserid \n";
			sql += " 	and a.resultseq=b.MSIDECODE \n";
			sql += " 	and a.RESULTSEQ=c.RESULTSEQ \n";
			sql += " 	and b.suserid=c.puserid \n";
			sql += " 	and b.tuserid=c.userid \n";
			sql += " 	and b.IDGUBUN='3' \n";
            if (v_gubuncd != null && !v_gubuncd.equals("")){
            	sql += " AND		A.GUBUNCD 	= '"+ v_gubuncd +"'";
            }
            if (v_gubuncddt != null && !v_gubuncddt.equals("")){
            	sql += " AND		A.GUBUNCDDT = '"+ v_gubuncddt +"'";
            }
			sql += " 	group by a.userid \n";
			sql += " 	)) \n";
			sql += " 	group by userid \n";
			sql += " 	)b \n";
			sql += " 	where a.userid=b.userid(+)		 \n";
			
            sql += " ORDER BY A.GUBUNCD, A.GUBUNCDDT, B.USERID ASC, A.INDATE DESC ";

		//	System.out.println(sql);
			
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) {
	            dbox = ls.getDataBox();
	            list.add(dbox);
            }
			
		//	System.out.println(list);
			
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

  		String  v_resultseq		= box.getString("p_resultseq");
  		String  v_target_userid	= box.getString("p_target_userid");

        try {
            connMgr     = new DBConnectionManager();
            /*
            sql  = "SELECT	B.NAME, A.USERID, A.INDATE, A.RESULTSEQ, TO_CHAR(A.RESULTPOINT) RESULTPOINT,	";
            sql += "				A.GUBUNCD, A.GUBUNCDDT,	C.GUBUNCDNM, D.GUBUNCDDTNM,								 						";
            sql += "				GET_JIKUPNM(b.jikryul, b.jikup) JIKUPNM , GET_COMPNM(A.COMP, a.userid) COMPNM, ";
            sql += "				GET_JIKRYULNM(b.jikryul) JIKRYULNM , b.deptnam ";
            sql += "FROM		TZ_MEMBER B, TZ_ABILITY_RSLT_MAS A,  TZ_ABILITY_CODE C, TZ_ABILITY_CODE_DT	D	";
            sql += "WHERE		B.USERID		= A.USERID	 	";
            sql += "AND			A.GUBUNCD		= C.GUBUNCD	 	";
            sql += "AND			A.GUBUNCD		= D.GUBUNCD	 	";
            sql += "AND			A.GUBUNCDDT	= D.GUBUNCDDT	 ";
           	sql += "AND			A.RESULTSEQ	= '"+ v_resultseq +"' ";
           	sql += "AND			A.userid	= '"+ v_target_userid +"' ";
           	*/
            
           	sql  = "SELECT	B.NAME, A.USERID, A.INDATE, A.RESULTSEQ, TO_CHAR(A.RESULTPOINT) RESULTPOINT,	";
           	sql += "				A.GUBUNCD, A.GUBUNCDDT,	C.GUBUNCDNM, D.GUBUNCDDTNM,								 						";
           	sql += "				get_codenm('0113',a.jikup) JIKUPNM , GET_COMPNM(A.COMP) COMPNM ";
           	sql += "FROM		TZ_MEMBER B, TZ_ABILITY_RSLT_MAS A,  TZ_ABILITY_CODE C, TZ_ABILITY_CODE_DT	D	";
           	sql += "WHERE		B.USERID		= A.USERID	 	";
           	sql += "AND			A.GUBUNCD		= C.GUBUNCD	 	";
           	sql += "AND			A.GUBUNCD		= D.GUBUNCD	 	";
           	sql += "AND			A.GUBUNCDDT	= D.GUBUNCDDT	 ";
           	sql += "AND			A.RESULTSEQ	= '"+ v_resultseq +"' ";
           	sql += "AND			A.userid	= '"+ v_target_userid +"' ";

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
    역량 결과조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectAbility(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        ArrayList			list		= null;
        String				sql			= "";

        String 	v_gubuncd		= box.getString("p_gubuncd");
        String 	v_gubuncddt		= box.getString("p_gubuncddt");
        String 	v_resultseq		= box.getString("p_resultseq");
  		String  v_target_userid	= box.getString("p_target_userid");

        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
			
			
			sql += " select  ability, abilitynm, round(avg(abilitypoint), 1) abilitypoint, \n" +
					"		round(avg(graph1), 1) graph1, round(graph2, 1) graph2, avg_abilitypoint,totcnt,orders from(	\n";
            sql += " SELECT	A.ABILITY, A.ABILITYNM, TO_CHAR(NVL(B.ABILITYPOINT,0)) ABILITYPOINT, 			\n";
            sql += "				NVL(B.ABILITYPOINT,0) / 5 * 100 AS GRAPH1,		\n";
            sql += "				NVL(C.ABILITYPOINT,0) / 5 * 100 AS GRAPH2,			\n";
            sql += "				TO_CHAR(NVL(C.ABILITYPOINT,0)) AVG_ABILITYPOINT, NVL(C.TOTCNT,0) TOTCNT	,orders	\n";
            sql += " FROM		TZ_ABILITY A		\n";
            sql += "					LEFT OUTER JOIN TZ_ABILITY_RSLT_ABL B	\n";
           	sql += "						ON 	A.GUBUNCD		= B.GUBUNCD	\n";
           	sql += "						AND A.GUBUNCDDT	= B.GUBUNCDDT		\n";
           	sql += "						AND A.ABILITY		= B.ABILITY	\n";
           	sql += "						AND B.RESULTSEQ	= '"+ v_resultseq +"'	\n";
           	sql += "						AND B.userid	= '"+ v_target_userid +"'	\n";
            sql += "					LEFT OUTER JOIN (		\n";
           	sql += "							SELECT	GUBUNCD, GUBUNCDDT, ABILITY, COUNT(USERID) AS TOTCNT,	\n";
           	sql += "											ROUND(AVG(ABILITYPOINT),1) AS ABILITYPOINT	\n";
           	sql += "							FROM		TZ_ABILITY_RSLT_ABL		\n";
           	sql += "							WHERE		GUBUNCD 	= '"+ v_gubuncd +"'	\n";
           	sql += "							AND			GUBUNCDDT = '"+ v_gubuncddt +"'	\n";
           	sql += "							GROUP BY  GUBUNCD, GUBUNCDDT, ABILITY )	C	\n";
           	sql += "						ON 	A.GUBUNCD		= C.GUBUNCD		\n";
           	sql += "						AND A.GUBUNCDDT	= C.GUBUNCDDT		\n";
           	sql += "						AND A.ABILITY		= C.ABILITY		\n";
           	sql += "WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'	\n";
           	sql += "AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'	\n";
            sql += "	) group by ability, abilitynm,graph2,avg_abilitypoint,totcnt,orders order by orders																															\n";
         
            ls = connMgr.executeQuery(sql);
						
            while ( ls.next() ) {
                dbox = ls.getDataBox();
				dbox.put("graph1"	   		,new Double(ls.getString("graph1")));
				dbox.put("graph2"	    	,new Double(ls.getString("graph2")));
				dbox.put("abilitypoint"	   		,new Double(ls.getString("abilitypoint")));
				dbox.put("avg_abilitypoint"	    ,new Double(ls.getString("avg_abilitypoint")));
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
    역량 결과 상세 조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectAbilityDetail(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        ArrayList			list		= null;
        String				sql			= "";

        String 				v_gubuncd		= box.getString("p_gubuncd");
        String 				v_gubuncddt		= box.getString("p_gubuncddt");
        String 				v_resultseq		= box.getString("p_resultseq");
        String 				v_ability		= box.getString("p_abilitycd");
        String 				v_target_userid	= box.getString("p_target_userid");
        
        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
			/*
			sql += " select ability  , abilitynm,  avg(abilitypoint)abilitypoint, avg(graph1)graph1, avg(graph2)graph2, avg(avg_abilitypoint)abilitypoint, abilityprodesc, rowcnt from (	\n";
            sql  += "SELECT	A.ABILITY, A.ABILITYNM, TO_CHAR(NVL(C.ABILITYPOINT,0)) ABILITYPOINT, 			\n";
            sql += "				NVL(C.ABILITYPOINT,0) / 5 * 100 AS GRAPH1,									 							\n";
            sql += "				NVL(D.ABILITYPOINT,0) / 5 * 100 AS GRAPH2,									 							\n";
            sql += "				TO_CHAR(NVL(D.ABILITYPOINT,0)) AVG_ABILITYPOINT, B.ABILITYPRODESC,				\n";
            sql += "				E.ROWCNT																																	\n";
            sql += "FROM		TZ_ABILITY A																															\n";
           	sql += "					INNER JOIN TZ_ABILITY_PRO B																							\n";
           	sql += "						ON 	A.GUBUNCD		= B.GUBUNCD																						\n";
           	sql += "						AND A.GUBUNCDDT	= B.GUBUNCDDT																					\n";
           	sql += "						AND A.ABILITY		= B.ABILITY																						\n";
            sql += "					LEFT OUTER JOIN TZ_ABILITY_RSLT_DT C																		\n";
           	sql += "						ON 	B.GUBUNCD			= C.GUBUNCD																					\n";
           	sql += "						AND B.GUBUNCDDT		= C.GUBUNCDDT																				\n";
           	sql += "						AND B.ABILITY			= C.ABILITY																					\n";
           	sql += "						AND B.ABILITYPRO	= C.ABILITYPRO																			\n";
           	sql += "						AND	C.RESULTSEQ		= '"+ v_resultseq +"'																\n";
            sql += "					LEFT OUTER JOIN (																												\n";
           	sql += "							SELECT	GUBUNCD, GUBUNCDDT, ABILITY, ABILITYPRO,										\n";
           	sql += "											ROUND(AVG(ABILITYPOINT),1) AS ABILITYPOINT									\n";
           	sql += "							FROM		TZ_ABILITY_RSLT_DT 																					\n";
           	sql += "							WHERE		GUBUNCD 	= '"+ v_gubuncd +"'																\n";
           	sql += "							AND			GUBUNCDDT = '"+ v_gubuncddt +"'															\n";
           	sql += "							GROUP BY  GUBUNCD, GUBUNCDDT, ABILITY, ABILITYPRO )	D								\n";
           	sql += "						ON 	B.GUBUNCD			= D.GUBUNCD																					\n";
           	sql += "						AND B.GUBUNCDDT		= D.GUBUNCDDT																				\n";
           	sql += "						AND B.ABILITY			= D.ABILITY																					\n";
           	sql += "						AND B.ABILITYPRO	= D.ABILITYPRO																			\n";
            sql += "					LEFT OUTER JOIN (																												\n";
           	sql += "							SELECT	GUBUNCD, GUBUNCDDT, ABILITY, 																\n";
           	sql += "											COUNT(GUBUNCD) AS ROWCNT																		\n";
           	sql += "							FROM		TZ_ABILITY_PRO		 																					\n";
           	sql += "							WHERE		GUBUNCD 	= '"+ v_gubuncd +"'																\n";
           	sql += "							AND			GUBUNCDDT = '"+ v_gubuncddt +"'															\n";
           	sql += "							GROUP BY  GUBUNCD, GUBUNCDDT, ABILITY )	E														\n";
           	sql += "						ON 	A.GUBUNCD			= E.GUBUNCD																					\n";
           	sql += "						AND A.GUBUNCDDT		= E.GUBUNCDDT																				\n";
           	sql += "						AND A.ABILITY			= E.ABILITY																					\n";
           	sql += "WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'																						\n";
           	sql += "AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'																					\n";
			sql += " )   group by  ability, abilitynm, abilityprodesc, rowcnt \n";
			sql += "ORDER BY ABILITY ASC	\n";
			*/
			
			sql = "	SELECT e.gubuncd, e.gubuncddt, d.resultseq, e.ability, e.abilitypro, e.abilityprodesc,   \n"+
			  "     round(nvl(d.abilitypoint, 0), 1) abilitypoint, round(NVL(d.abilitypoint, 0) / 5 * 100, 1) AS graph1,	\n"+
			"       round(NVL(e.avg_abilitypoint, 0) / 5 * 100, 1) AS graph2, round(nvl(e.avg_abilitypoint, 0), 1) avg_abilitypoint, \n"+
			 "      e.rowcnt, e.orders, e.abilitynm, e.descs \n"+
			 " FROM (SELECT   gubuncd, gubuncddt, resultseq, ability, abilitypro, \n"+
			 "                AVG(abilitypoint) abilitypoint \n"+
			 "           FROM tz_ability_rslt_dt \n"+
			 "          WHERE gubuncd = '"+ v_gubuncd +"' AND gubuncddt = '"+ v_gubuncddt +"' \n"+
			 "                AND userid = '" + v_target_userid + "' and resultseq = '"+ v_resultseq +"' \n"+
			 "       GROUP BY gubuncd, gubuncddt, resultseq, ability, abilitypro) d \n"+
			 "      right OUTER JOIN \n"+
			 "      (SELECT a.gubuncd, a.gubuncddt, a.ability, c.abilitynm, a.abilitypro,abilityprodesc, \n"+
			 "              c.descs, a.abilitypoint avg_abilitypoint, f.rowcnt, b.orders \n"+
			 "        FROM (SELECT   gubuncd, gubuncddt, ability, abilitypro, \n"+
			 "                       AVG (abilitypoint) AS abilitypoint \n"+
			 "                  FROM tz_ability_rslt_dt \n"+
			 "                 WHERE gubuncd = '"+ v_gubuncd +"' AND gubuncddt = '"+ v_gubuncddt +"' \n"+
			 "              GROUP BY gubuncd, gubuncddt, ability, abilitypro) a \n"+
			 "              LEFT OUTER JOIN \n"+
			 "              (SELECT   gubuncd, gubuncddt, ability, abilityprodesc,abilitypro, \n"+
			  "                       COUNT (gubuncd) AS rowcnt, orders \n"+
			  "                  FROM tz_ability_pro \n"+
			  "                 WHERE gubuncd = '"+ v_gubuncd +"' AND gubuncddt = '"+ v_gubuncddt +"' \n"+
			  "              GROUP BY gubuncd, gubuncddt, ability,abilityprodesc,abilitypro, orders) b \n"+
			  "             ON a.gubuncd = b.gubuncd \n"+
			  "           AND a.gubuncddt = b.gubuncddt \n"+
			  "           AND a.ability = b.ability  AND a.abilitypro = b.abilitypro \n"+
			 " left outer join 	\n"+
	        "     (SELECT   gubuncd, gubuncddt, ability,	\n"+
	        "                 COUNT (gubuncd) AS rowcnt	\n"+
	        "            FROM tz_ability_pro	\n"+
	        "           WHERE gubuncd = '"+ v_gubuncd +"' AND gubuncddt = '"+ v_gubuncddt +"' 	\n"+
	        "        GROUP BY gubuncd, gubuncddt, ability) f	\n"+
	         "                ON b.gubuncd = f.gubuncd	\n"+
	         "    AND b.gubuncddt = f.gubuncddt	\n"+
	         "    AND b.ability = f.ability	\n"+
			  "             , tz_ability c \n"+
			  "       WHERE a.gubuncd = c.gubuncd \n"+
			  "         AND a.gubuncddt = c.gubuncddt \n"+
			  "         AND a.ability = c.ability) e \n"+
			  "     ON e.gubuncd = d.gubuncd \n"+
			  "   AND e.gubuncddt = d.gubuncddt \n"+
			  "   AND e.ability = d.ability \n"+
			  "   AND e.abilitypro = d.abilitypro \n"+
			  "  order by e.gubuncd, e.gubuncddt, e.ability, e.abilitypro, e.rowcnt  \n";

            ls = connMgr.executeQuery(sql);
			
            while ( ls.next() ) {
                dbox = ls.getDataBox();
				dbox.put("d_graph1"	   		,new Double(ls.getString("graph1")));
				dbox.put("d_graph2"	    	,new Double(ls.getString("graph2")));
				dbox.put("d_abilitypoint"	   		,new Double(ls.getString("abilitypoint")));
				dbox.put("d_avg_abilitypoint"	    ,new Double(ls.getString("avg_abilitypoint")));
				
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
    역량 전체 결과조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectListPageAll(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        ArrayList			list		= null;
        String				sql			= "";

        String 				v_gubuncd		= box.getString("p_gubuncd");
        String 				v_gubuncddt		= box.getString("p_gubuncddt");
        String 				v_resultseq		= box.getString("p_resultseq");
        //String 				v_company		= box.getString("s_comp");
        String 				v_company		= box.getString("s_company");
        String 				v_company2		= box.getString("s_comp2");
        String 				v_jikup			= box.getString("s_jikup");
        //String 				v_jikryul		= box.getString("s_jikryul");
		
		String v_msidecode  		= box.getString("p_msidecode");

        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();

            sql  = "SELECT	A.ABILITY, A.ABILITYNM, round(NVL(C.ABILITYPOINT,0), 1) AS AVG_POINT,			\n";
            sql += "		round(NVL(C.ABILITYPOINT,0) / 5 * 100, 1) PERCENT_POINT \n";
            sql += "FROM		TZ_ABILITY A \n";
            sql += "					LEFT OUTER JOIN ( \n";
           	sql += "							SELECT	A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, a.RESULTSEQ,  \n";
           	sql += "											ROUND(AVG(A.ABILITYPOINT),1) AS ABILITYPOINT \n";
           	sql += "							FROM		TZ_ABILITY_RSLT_ABL A, TZ_ABILITY_RSLT_MAS B \n";
           	sql += "							WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'	 \n";
           	sql += "							AND			A.GUBUNCDDT = '"+ v_gubuncddt +"' \n";
           
			 if(!v_msidecode.equals("ALL")){
				  sql +=" and a.RESULTSEQ='"+v_msidecode+"'  \n";
			  }
			 
			sql += "							AND 		A.GUBUNCD		= B.GUBUNCD	 \n";
           	sql += "							AND 		A.GUBUNCDDT	= B.GUBUNCDDT \n";
           	sql += "							AND 		A.RESULTSEQ	= B.RESULTSEQ \n";
           	sql += "							AND 		A.USERID		= B.USERID \n";
			if(!v_company.equals("") && !v_company.equals("ALL")){
				sql += "AND	B.COMP = '"+ v_company +"'				\n";
			}
			if(!v_company2.equals("") && !v_company2.equals("ALL")){
				sql += "AND	B.COMP = '"+ v_company2 +"'  \n";
			}
			if(!v_jikup.equals("")  && !v_jikup.equals("ALL")){
				sql += "AND	B.JIKUP = '"+ v_jikup +"'  \n";
			}
			
			/* 직렬 사용하지 않음
			if(!v_jikryul.equals("")  && !v_jikryul.equals("ALL")){
				sql += "AND	B.jikryul = '"+ v_jikryul +"'  \n";
			}
			*/
			
           	sql += "							GROUP BY  A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, a.resultseq )	C	 \n";
           	sql += "						ON 	A.GUBUNCD		= C.GUBUNCD	 \n";
           	sql += "						AND A.GUBUNCDDT	= C.GUBUNCDDT	 \n";
           	sql += "						AND A.ABILITY		= C.ABILITY \n";
           	sql += "WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'	 \n";
           	sql += "AND			A.GUBUNCDDT = '"+ v_gubuncddt +"' \n";
			 if(!v_msidecode.equals("ALL")){
				  sql +=" and c.RESULTSEQ='"+v_msidecode+"'  \n";
			  }
			
			sql += "ORDER BY A.ORDERS ASC	\n";
            
			
			System.out.println("====역량 전체 결과조회=====\n"+sql);
     
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) {
                dbox = ls.getDataBox();
				dbox.put("d_avg_point"	   		,new Double(ls.getString("avg_point")));
				dbox.put("d_percent_point"	    ,new Double(ls.getString("percent_point")));
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
    역량 결과 조회 건수
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public DataBox selectCount(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        ArrayList			list		= null;
        String				sql			= "";

        String 				v_gubuncd		= box.getString("p_gubuncd");
        String 				v_gubuncddt		= box.getString("p_gubuncddt");
        String 				v_resultseq		= box.getString("p_resultseq");
        String 				v_ability		= box.getString("p_ability");
        String 				v_company		= box.getString("s_comp");
        String 				v_company2		= box.getString("s_comp2");
        String 				v_jikup			= box.getString("s_jikup");
        String 				v_jikryul		= box.getString("s_jikryul");
		
		String v_msidecode  		= box.getString("p_msidecode");

        try {
            connMgr     = new DBConnectionManager();

           	sql  = "SELECT	NVL(SUM(TOTCNT),0) AS TOTCNT, NVL(SUM(SEARCHCNT),0) AS SEARCHCNT		\n";
           	sql += "FROM(		SELECT	COUNT(A.USERID) AS TOTCNT, 0 AS SEARCHCNT 									\n";
           	sql += "				FROM		TZ_ABILITY_RSLT_MAS A																				\n";
           	sql += "				WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'															\n";
           	sql += "				AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'														\n";
			 if(!v_msidecode.equals("ALL")){
				  sql +=" and a.RESULTSEQ='"+v_msidecode+"'  \n";
			  }
			
			if(!v_company.equals("") && !v_company.equals("ALL")){
				sql += "AND	A.COMP = '"+ v_company +"'				\n";
			}
           	if(!v_company2.equals("") && !v_company2.equals("ALL")){
				sql += "AND	A.COMP = '"+ v_company2 +"' 																					\n";
			}
			if(!v_jikup.equals("")  && !v_jikup.equals("ALL")){
				sql += "AND	A.JIKUP = '"+ v_jikup +"' 																						\n";
			}
			if(!v_jikryul.equals("")  && !v_jikryul.equals("ALL")){
				sql += "AND	a.jikryul = '"+ v_jikryul +"' 																									\n";
			}
			sql += "				UNION ALL 																													\n";
           	sql += "				SELECT	0 AS TOTCNT, COUNT(A.USERID) AS SEARCHCNT										\n";
           	sql += "				FROM		TZ_ABILITY_RSLT_MAS A																				\n";
           	sql += "				WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'															\n";
           	sql += "				AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'														\n";
			 if(!v_msidecode.equals("ALL")){
				  sql +=" and a.RESULTSEQ='"+v_msidecode+"'  \n";
			  }
			
			
			if(!v_company.equals("") && !v_company.equals("ALL")){
				sql += "AND	A.COMP = '"+ v_company +"'				\n";
			}
			if(!v_company2.equals("") && !v_company2.equals("ALL")){
				sql += "AND	A.COMP = '"+ v_company2 +"' 																						\n";
			}
			if(!v_jikup.equals("")  && !v_jikup.equals("ALL")){
				sql += "AND	A.JIKUP = '"+ v_jikup +"' 																						\n";
			}
			if(!v_jikryul.equals("")  && !v_jikryul.equals("ALL")){
				sql += "AND	a.jikryul = '"+ v_jikryul +"' 																									\n";
			}
           	sql += "			) X																																		\n";
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) {
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
    역량 전체 결과조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectAll(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        ArrayList			list		= null;
        String				sql			= "";

        String 				v_gubuncd		= box.getString("p_gubuncd");
        String 				v_gubuncddt		= box.getString("p_gubuncddt");
        String 				v_resultseq		= box.getString("p_resultseq");
        String 				v_ability		= box.getString("p_ability");
        //String 				v_company		= box.getString("s_comp");
        String 				v_company		= box.getString("s_company");
        String 				v_company2		= box.getString("s_comp2");
        String 				v_jikup			= box.getString("s_jikup");
        String 				v_jikryul		= box.getString("s_jikryul");
		

		
		String v_msidecode  		= box.getString("p_msidecode");

		String subsql = "";
		String subsql2 = "";
		if(!v_msidecode.equals("ALL")){
			subsql =" AND a.resultseq = '"+v_msidecode+"'  ";
		  }
		
		
		if(!v_company.equals("ALL")){
			subsql2 += "AND	B.COMP = '"+ v_company +"'			\n";
		}
		if(!v_jikryul.equals("ALL")){
			subsql2 += "AND	b.jikryul = '"+ v_jikryul +"' \n";
		}
		if(!v_jikup.equals("ALL")){
			subsql2 += "AND	b.JIKUP = '"+ v_jikup +"' \n";
		}
		
		
        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
			
	/*		
			sql  = "		select gubuncd, gubuncddt, ability, abilitynm, pointnm, cnt, totcnt,			\n"
			+"		round(DECODE(NVL(totcnt,0), 0, 0, NVL(cnt,0) / NVL(totcnt,0) * 100), 1) AS percent	\n"
			+"		 from (																				\n"
			+"		select gubuncd, gubuncddt, ability, abilitynm, pointnm, sum(cnt)cnt, totcnt 		\n"
			+"		from (																				\n"
			+"		SELECT   a.gubuncd, a.gubuncddt, a.ability, a.abilitynm, b.pointnm,					\n"
			+"	         NVL (c.cnt, 0) AS cnt, NVL (d.totcnt, 0) AS totcnt							\n"
			+"	    FROM tz_ability a							\n"
			+"	         LEFT OUTER JOIN						\n"
			+"	         (SELECT '5점' AS pointnm, 5 AS point	\n"
			+"	            FROM DUAL							\n"
			+"	          UNION ALL								\n"
			+"	          SELECT '4점' AS pointnm, 4 AS point	\n"
			+"	            FROM DUAL							\n"
			+"	          UNION ALL								\n"
			+"	         SELECT '3점' AS pointnm, 3 AS point	\n"
			+"	            FROM DUAL							\n"
			+"	          UNION ALL								\n"
			+"	          SELECT '2점' AS pointnm, 2 AS point	\n"
			+"	            FROM DUAL							\n"
			+"	          UNION ALL								\n"
			+"	          SELECT '1점' AS pointnm, 1 AS point	\n"
			+"	            FROM DUAL) b ON 1 = 1				\n"
			+"	         LEFT OUTER JOIN						\n"
			+"	         (SELECT   a.gubuncd, a.gubuncddt, a.ability, round(a.abilitypoint)abilitypoint, a.resultseq,	\n"
			+"	                   COUNT (a.userid) AS cnt		\n"
			+"	              FROM tz_ability_rslt_abl	a, tz_ability_rslt_mas b			\n"
			+"	             WHERE a.gubuncd = '"+ v_gubuncd +"'			\n"
			+"	               AND a.gubuncddt = '"+ v_gubuncddt +"'		\n"
			
			+subsql
			
			+" 	  							and a.gubuncd = b.gubuncd	\n"
			+"    							AND a.gubuncddt = b.gubuncddt	\n"
			+"    							AND a.resultseq = b.resultseq	\n"
			+"    							and a.userid= b.userid  	\n"
			
			+subsql2
			
			
			+"	          GROUP BY a.gubuncd, a.gubuncddt, a.ability, a.abilitypoint, a.resultseq) c	\n"
			+"	         ON a.gubuncd = c.gubuncd				\n"
			+"	       AND a.gubuncddt = c.gubuncddt			\n"
			+"	       AND a.ability = c.ability				\n"
			+"	       AND b.point = c.abilitypoint				\n"
			+"	     LEFT OUTER JOIN							\n"
			+"	         (SELECT   a.gubuncd, a.gubuncddt, a.ability,  a.resultseq,	\n"
			+"	                   COUNT (a.userid) AS totcnt		\n"
			+"	              FROM tz_ability_rslt_abl	a		\n"
			+"	             WHERE a.gubuncd = '"+ v_gubuncd +"'			\n"
			+"	               AND a.gubuncddt = '"+ v_gubuncddt +"'		\n"
			+subsql
			+"	          GROUP BY a.gubuncd, a.gubuncddt, a.ability, a.resultseq) d	\n"
			+"	         ON a.gubuncd = d.gubuncd				\n"
			+"	       AND a.gubuncddt = d.gubuncddt			\n"
			+"	       AND a.ability = d.ability				\n"
			+"	   WHERE a.gubuncd = '"+ v_gubuncd +"' AND a.gubuncddt = '"+ v_gubuncddt +"'	\n"
			+"	) group by gubuncd, gubuncddt, ability, abilitynm, pointnm,totcnt	\n"
			+"	)		\n"
			+"	ORDER BY ability, pointnm DESC			\n";
		*/	

            sql  = "SELECT	A.ABILITY, A.ABILITYNM, B.POINTNM, NVL(C.CNT,0) AS CNT, E.ROWCNT,					\n";
            sql += "				round(DECODE(NVL(D.TOTCNT,0), 0, 0, NVL(C.CNT,0) / NVL(D.TOTCNT,0) * 100), 1) AS PERCENT	\n";
            sql += "FROM		TZ_ABILITY A																															\n";
            sql += "					LEFT OUTER JOIN (																												\n";
           	sql += "							SELECT	'5점  ' AS POINTNM, 5 AS POINT	FROM DUAL											\n";
           	sql += "							UNION ALL																														\n";
           	sql += "							SELECT	'4점  ' AS POINTNM, 4 AS POINT	FROM DUAL											\n";
           	sql += "							UNION ALL																														\n";
           	sql += "							SELECT	'3점  ' AS POINTNM, 3 AS POINT	FROM DUAL											\n";
           	sql += "							UNION ALL																														\n";
           	sql += "							SELECT	'2점  ' AS POINTNM, 2 AS POINT	FROM DUAL											\n";
           	sql += "							UNION ALL																														\n";
           	sql += "							SELECT	'1점  ' AS POINTNM, 1 AS POINT	FROM DUAL)	B									\n";
           	sql += "						ON 	1 = 1																															\n";
            sql += "					LEFT OUTER JOIN (																												\n";
           	sql += "							SELECT	A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, A.ABILITYPOINT,a.RESULTSEQ,					\n";
           	sql += "											COUNT(A.INPUTS) AS CNT																			\n";
           	sql += "							FROM		TZ_ABILITY_RSLT_DT A, TZ_ABILITY_RSLT_MAS B									\n";
           	sql += "							WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'															\n";
           	sql += "							AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'														\n";
			if(!v_msidecode.equals("ALL")){
				  sql +=" and a.RESULTSEQ='"+v_msidecode+"'  \n";
			  }
			
			sql += "							AND 		A.GUBUNCD		= B.GUBUNCD																			\n";
           	sql += "							AND 		A.GUBUNCDDT	= B.GUBUNCDDT																		\n";
           	sql += "							AND 		A.RESULTSEQ	= B.RESULTSEQ																		\n";
           	sql += "							AND 		A.USERID		= B.USERID																			\n";
			if(!v_company.equals("") && !v_company.equals("ALL")){
				sql += "AND	B.COMP = '"+ v_company +"'				\n";
			}
			if(!v_company2.equals("") && !v_company2.equals("ALL")){
				sql += "AND	B.COMP = '"+ v_company2 +"' 																									\n";
			}
			if(!v_jikup.equals("")  && !v_jikup.equals("ALL")){
				sql += "AND	B.JIKUP = '"+ v_jikup +"' 																									\n";
			}
			
			/* 직렬 사용하지 않음
			if(!v_jikryul.equals("")  && !v_jikryul.equals("ALL")){
				sql += "AND	B.jikryul = '"+ v_jikryul +"' 																									\n";
			}
			*/
           	sql += "							GROUP BY  A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, A.ABILITYPOINT ,a.RESULTSEQ)	C			\n";
           	sql += "						ON 	A.GUBUNCD		= C.GUBUNCD																						\n";
           	sql += "						AND A.GUBUNCDDT	= C.GUBUNCDDT																					\n";
           	sql += "						AND A.ABILITY		= C.ABILITY																						\n";
           	sql += "						AND B.POINT			= C.ABILITYPOINT																			\n";
            sql += "					LEFT OUTER JOIN (																												\n";
           	sql += "							SELECT	A.GUBUNCD, A.GUBUNCDDT, A.ABILITY,	a.RESULTSEQ,												\n";
           	sql += "											COUNT(A.ABILITYPOINT) AS TOTCNT															\n";
           	sql += "							FROM		TZ_ABILITY_RSLT_DT A, TZ_ABILITY_RSLT_MAS B									\n";
           	sql += "							WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'															\n";
           	sql += "							AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'														\n";
			if(!v_msidecode.equals("ALL")){
				  sql +=" and a.RESULTSEQ='"+v_msidecode+"'  \n";
			  }
			
			sql += "							AND 		A.GUBUNCD		= B.GUBUNCD																			\n";
           	sql += "							AND 		A.GUBUNCDDT	= B.GUBUNCDDT																		\n";
           	sql += "							AND 		A.RESULTSEQ	= B.RESULTSEQ																		\n";
			sql += "							AND 		A.USERID		= B.USERID																			\n";
			if(!v_company.equals("") && !v_company.equals("ALL")){
				sql += "AND	B.COMP = '"+ v_company +"'				\n";
			}
			if(!v_company2.equals("") &&  !v_company2.equals("ALL")){
				sql += "AND	B.COMP = '"+ v_company2 +"' 																									\n";
			}
			if(!v_jikup.equals("")  && !v_jikup.equals("ALL")){
				sql += "AND	B.JIKUP = '"+ v_jikup +"' 																									\n";
			}
			
			/* 직렬 사용하지 않음
			if(!v_jikryul.equals("")  && !v_jikryul.equals("ALL")){
				sql += "AND	B.jikryul = '"+ v_jikryul +"' 																									\n";
			}
			*/
			
           	sql += "							GROUP BY  A.GUBUNCD, A.GUBUNCDDT, A.ABILITY,a.RESULTSEQ )	D											\n";
           	sql += "						ON 	A.GUBUNCD		= D.GUBUNCD																						\n";
           	sql += "						AND A.GUBUNCDDT	= D.GUBUNCDDT																					\n";
           	sql += "						AND A.ABILITY		= D.ABILITY																						\n";
            sql += "					LEFT OUTER JOIN (																												\n";
           	sql += "							SELECT	GUBUNCD, GUBUNCDDT, ABILITY,																\n";
           	sql += "											COUNT(GUBUNCD) AS ROWCNT																		\n";
           	sql += "							FROM		TZ_ABILITY_PRO		 \n";
           	sql += "							WHERE		GUBUNCD 	= '"+ v_gubuncd +"'	 \n";
            sql += "							AND			GUBUNCDDT = '"+ v_gubuncddt +"'	 \n";
			sql += "							GROUP BY  GUBUNCD, GUBUNCDDT, ABILITY)	E \n";
           	sql += "						ON 	A.GUBUNCD			= E.GUBUNCD	 \n";
           	sql += "						AND A.GUBUNCDDT		= E.GUBUNCDDT	 \n";
           	sql += "						AND A.ABILITY			= E.ABILITY	 \n";
           	sql += "WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'	 \n";
           	sql += "AND			A.GUBUNCDDT = '"+ v_gubuncddt +"' \n";
			sql += "ORDER BY A.ABILITY, B.POINTNM DESC	 \n";
                
	//		System.out.println("===========역량 전체 결과조회=====\n"+sql);
			
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) {
                dbox = ls.getDataBox();
				dbox.put("d_percent"	    ,new Double(ls.getString("percent")));
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
    역량 문항별 전체 결과조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectQuestionAll(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        ArrayList			list		= null;
        String				sql			= "";

        String 				v_gubuncd		= box.getString("p_gubuncd");
        String 				v_gubuncddt		= box.getString("p_gubuncddt");
        String 				v_resultseq		= box.getString("p_resultseq");
        String 				v_ability		= box.getString("p_ability");
        //String 				v_company		= box.getStringDefault("s_comp", "ALL");
        String 				v_company		= box.getStringDefault("s_company", "ALL");
        String 				v_jikup			= box.getStringDefault("s_jikup", "ALL");
        //String 				v_jikryul		= box.getStringDefault("s_jikryul", "ALL");
		
		String v_msidecode  		= box.getString("p_msidecode");

		String subsql = "";
		String subsql2 = "";
		
		if(!v_msidecode.equals("ALL")){
			subsql =" AND a.resultseq = '"+v_msidecode+"'  ";
		  }

		if(!v_company.equals("ALL")){
			subsql2 += "AND	B.COMP = '"+ v_company +"'			\n";
		}
		/*
		if(!v_jikryul.equals("ALL")){
			subsql2 += "AND	b.jikryul = '"+ v_jikryul +"' \n";
		}
		*/
		
		if(!v_jikup.equals("ALL")){
			subsql2 += "AND	b.JIKUP = '"+ v_jikup +"' \n";
		}
		
		
		
		
        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();

	
		/*	
		sql="	SELECT   gubuncd, gubuncddt, ability, abilitynm, pointnm, cnt, totcnt,		\n"
	    +"     abilitypro,									\n"
		+"     ROUND (DECODE (NVL (totcnt, 0),				\n"
		+"                    0, 0,							\n"
		+"                    NVL (cnt, 0) / NVL (totcnt, 0) * 100		\n"
		+"                   ),					\n"
		+"             1						\n"
		+"           ) AS PERCENT,			\n"
		+"      descs, abilityprodesc, orders	,rowcnt	\n"
		+"  FROM (SELECT   gubuncd, gubuncddt, ability, abilitynm, pointnm,		\n"
		+"                 SUM (cnt) cnt, totcnt, descs, abilityprodesc, orders,		\n"
		+"                 abilitypro	,rowcnt								\n"
		+"            FROM (SELECT a.gubuncd, a.gubuncddt, a.ability, a.abilitynm,		\n"
		+"                         b.pointnm, a.descs, e.abilityprodesc, e.orders,		\n"
		+"                         NVL (c.cnt, 0) AS cnt, NVL (d.totcnt, 0) AS totcnt,		\n"
		+"                        e.abilitypro	,f.rowcnt									\n"
		+"                    FROM tz_ability a INNER JOIN tz_ability_pro e			\n"
		+"                         ON a.gubuncd = e.gubuncd							\n"
		+"                       AND a.gubuncddt = e.gubuncddt							\n"
		+"                       AND a.ability = e.ability								\n"
		+"                         LEFT OUTER JOIN									\n"
		+"                         (SELECT '5점' AS pointnm, 5 AS point				\n"
		+"                            FROM DUAL						\n"
		+"                          UNION ALL						\n"
		+"                          SELECT '4점' AS pointnm, 4 AS point	\n"
		+"                             FROM DUAL						\n"
		+"                            UNION ALL						\n"
		+"                            SELECT '3점' AS pointnm, 3 AS point	\n"
		+"                              FROM DUAL						\n"
		+"                            UNION ALL						\n"
		+"                           SELECT '2점' AS pointnm, 2 AS point	\n"
		+"                             FROM DUAL		\n"
		+"                           UNION ALL		\n"
		+"                          SELECT '1점' AS pointnm, 1 AS point	\n"
		+"                            FROM DUAL) b ON 1 = 1	\n"
		+"                         LEFT OUTER JOIN			\n"
		+"                         (SELECT   a.gubuncd, a.gubuncddt, a.ability,		\n"
		+"                                   ROUND (a.abilitypoint) abilitypoint,	\n"
		+"                                  a.resultseq, COUNT (a.userid) AS cnt	\n"
		+"                              FROM tz_ability_rslt_abl a, tz_ability_rslt_mas b				\n"
		+"                            WHERE a.gubuncd = '"+ v_gubuncd +"'					\n"
		+"                              AND a.gubuncddt = '"+ v_gubuncddt +"'				\n"
		+subsql
		+" 	  							and a.gubuncd = b.gubuncd	\n"
		+"    							AND a.gubuncddt = b.gubuncddt	\n"
		+"    							AND a.resultseq = b.resultseq	\n"
		+"    							and a.userid= b.userid  	\n"
		
		+subsql2
		
		+"                         GROUP BY a.gubuncd,				\n"
		+"                                  a.gubuncddt,				\n"
		+"                                  a.ability,				\n"
		+"                                  a.abilitypoint,			\n"
		+"                                  a.resultseq) c			\n"
		+"                       ON a.gubuncd = c.gubuncd			\n"
		+"                     AND a.gubuncddt = c.gubuncddt		\n"
		+"                     AND a.ability = c.ability			\n"
		+"                     AND b.point = c.abilitypoint		\n"
		+"                       LEFT OUTER JOIN					\n"
		+"                       (SELECT   a.gubuncd, a.gubuncddt, a.ability, a.resultseq,	\n"
		+"                                  COUNT (a.userid) AS totcnt		\n"
		+"                             FROM tz_ability_rslt_abl	a		\n"
		+"                            WHERE a.gubuncd = '"+ v_gubuncd +"'				\n"
		+"                              AND a.gubuncddt = '"+ v_gubuncddt +"'			\n"
		+subsql
		+"                         GROUP BY a.gubuncd, a.gubuncddt, a.ability, a.resultseq) d	\n"
		+"                        ON a.gubuncd = d.gubuncd				\n"
		+"                      AND a.gubuncddt = d.gubuncddt			\n"
		+"                      AND a.ability = d.ability				\n"
		
		+"   LEFT OUTER JOIN							\n"
		+"       (SELECT   gubuncd, gubuncddt, ability, COUNT (gubuncd) AS rowcnt	\n"
		+"            FROM tz_ability_pro						\n"
		+"           WHERE gubuncd = '"+ v_gubuncd +"' AND gubuncddt = '"+ v_gubuncddt +"'	\n"
		+"        GROUP BY gubuncd, gubuncddt, ability) f		\n"
		+"       ON a.gubuncd = f.gubuncd		\n"
		+"     AND a.gubuncddt = f.gubuncddt		\n"
		+"     AND a.ability = f.ability  		\n"
		
		+"                  WHERE a.gubuncd = '"+ v_gubuncd +"' AND a.gubuncddt = '"+ v_gubuncddt +"')	\n"
		+"       GROUP BY gubuncd,			\n"
		+"                gubuncddt,		\n"
		+"                ability,			\n"
		+"                abilitynm,		\n"
		+"               pointnm,			\n"
		+"               totcnt,			\n"
		+"               abilityprodesc,	\n"
		+"               orders,			\n"
		+"               descs,			\n"
		+"                abilitypro,rowcnt)		\n"
		+" ORDER BY ability, abilitypro, pointnm DESC	\n";
		*/	
			
			
			
            sql  = "SELECT	A.ABILITY, A.ABILITYNM, B.ABILITYPRODESC, NVL(C.CNT,0) AS CNT, B.ORDERS,	\n";
            sql += "				DECODE(NVL(D.TOTCNT,0), 0, 0, round(NVL(C.CNT,0) / NVL(D.TOTCNT,0) * 100, 1)) AS PERCENT,	\n";
            sql += "				E.ROWCNT, A.DESCS, C1.POINTNM	, B.ABILITYPRO \n";
            sql += "FROM		TZ_ABILITY A							 \n";
           	sql += "					INNER JOIN TZ_ABILITY_PRO B			 	\n";
           	sql += "						ON 	A.GUBUNCD		= B.GUBUNCD	 	\n";
           	sql += "						AND A.GUBUNCDDT	= B.GUBUNCDDT	 \n";
           	sql += "						AND A.ABILITY		= B.ABILITY	 	\n";
            sql += "					LEFT OUTER JOIN (	 \n";
           	sql += "							SELECT	'5점  ' AS POINTNM, 5 AS POINT	FROM DUAL	 	\n";
           	sql += "							UNION ALL	 					\n";
           	sql += "							SELECT	'4점  ' AS POINTNM, 4 AS POINT	FROM DUAL 	\n";
           	sql += "							UNION ALL		 			\n";
           	sql += "							SELECT	'3점  ' AS POINTNM, 3 AS POINT	FROM DUAL \n";
           	sql += "							UNION ALL	 						\n";
           	sql += "							SELECT	'2점  ' AS POINTNM, 2 AS POINT	FROM DUAL 	\n";
           	sql += "							UNION ALL	 				\n";
           	sql += "							SELECT	'1점  ' AS POINTNM, 1 AS POINT	FROM DUAL)	C1 \n";
           	sql += "						ON 	1 = 1	 			\n";
            sql += "					LEFT OUTER JOIN ( 		\n";
           	sql += "							SELECT	A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, A.ABILITYPRO, 	\n";
           	sql += "											A.INPUTS, COUNT(A.GUBUNCD) AS CNT \n";
           	sql += "							FROM		TZ_ABILITY_RSLT_DT A, TZ_ABILITY_RSLT_MAS B \n";
           	sql += "							WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'	 	\n";
           	sql += "							AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'	 \n";
			if(!v_msidecode.equals("ALL")){
				sql += "  AND a.resultseq = '"+v_msidecode+"'  \n";
			  }
			
			sql += "							AND 		A.GUBUNCD		= B.GUBUNCD		\n";
           	sql += "							AND 		A.GUBUNCDDT	= B.GUBUNCDDT		\n";
           	sql += "							AND 		A.RESULTSEQ	= B.RESULTSEQ		\n";
           	sql += "							AND 		A.USERID		= B.USERID		\n";
			if(!v_company.equals("ALL")){
				sql += "AND	B.COMP = '"+ v_company +"'			\n";
			}
			
			/*
			if(!v_jikryul.equals("ALL")){
				sql += "AND	b.jikryul = '"+ v_jikryul +"' \n";
			}
			*/
			
			if(!v_jikup.equals("ALL")){
				sql += "AND	b.JIKUP = '"+ v_jikup +"' \n";
			}
           	sql += "							GROUP BY A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, A.ABILITYPRO,A.INPUTS) C \n";
           	sql += "						ON 	B.GUBUNCD			= C.GUBUNCD	\n";
           	sql += "						AND B.GUBUNCDDT		= C.GUBUNCDDT \n";
           	sql += "						AND B.ABILITY			= C.ABILITY	 \n";
           	sql += "						AND B.ABILITYPRO	= C.ABILITYPRO	\n";
           	sql += "						AND C1.POINT			= C.INPUTS	\n";
            sql += "					LEFT OUTER JOIN (					 \n";
           	sql += "							SELECT	A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, \n";
           	sql += "											COUNT(A.ABILITYPOINT) AS TOTCNT, A.ABILITYpro  \n";
           	sql += "							FROM		TZ_ABILITY_RSLT_DT A, TZ_ABILITY_RSLT_MAS B	 \n";
           	sql += "							WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'	 \n";
           	sql += "							AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'	 \n";
			if(!v_msidecode.equals("ALL")){
				sql += "  AND a.resultseq = '"+v_msidecode+"'  \n";
			  }
			
			sql += "							AND 		A.GUBUNCD		= B.GUBUNCD		 \n";
           	sql += "							AND 		A.GUBUNCDDT	= B.GUBUNCDDT	 \n";
           	sql += "							AND 		A.RESULTSEQ	= B.RESULTSEQ	 \n";
           	sql += "							AND 		A.USERID		= B.USERID  \n";
           	if(!v_company.equals("ALL")){
				sql += "AND	B.COMP = '"+ v_company +"'			\n";
			}
           	
           	/*
			if(!v_jikryul.equals("ALL")){
				sql += "AND	b.jikryul = '"+ v_jikryul +"'  \n";
			}
			*/
           	
			if(!v_jikup.equals("ALL")){
				sql += "AND	b.JIKUP = '"+ v_jikup +"'  \n";
			}
           	sql += "							GROUP BY  A.GUBUNCD, A.GUBUNCDDT, A.ABILITY, A.ABILITYpro )	D  \n";
           	sql += "						ON 	A.GUBUNCD		= D.GUBUNCD	 \n";
           	sql += "						AND A.GUBUNCDDT	= D.GUBUNCDDT	 \n";
           	sql += "						AND A.ABILITY		= D.ABILITY	 \n" +
           			"						AND b.ABILITYpro	= D.ABILITYpro	\n";
            sql += "					LEFT OUTER JOIN (	  \n";
           	sql += "							SELECT	GUBUNCD, GUBUNCDDT, ABILITY,   \n";
           	sql += "											COUNT(GUBUNCD) AS ROWCNT  \n";
           	sql += "							FROM		TZ_ABILITY_PRO		 \n";
           	sql += "							WHERE		GUBUNCD 	= '"+ v_gubuncd +"'		 \n";
           	sql += "							AND			GUBUNCDDT = '"+ v_gubuncddt +"'		 \n";
			sql += "							GROUP BY  GUBUNCD, GUBUNCDDT, ABILITY )	E		 \n";
           	sql += "						ON 	A.GUBUNCD			= E.GUBUNCD	  \n";
           	sql += "						AND A.GUBUNCDDT		= E.GUBUNCDDT	 \n";
           	sql += "						AND A.ABILITY			= E.ABILITY	 \n";
           	sql += "WHERE		A.GUBUNCD 	= '"+ v_gubuncd +"'		\n";
           	sql += "AND			A.GUBUNCDDT = '"+ v_gubuncddt +"'  \n";
            sql += "ORDER BY B.ABILITY, B.ABILITYPRO, C1.POINT DESC	 \n";
           
//			System.out.println(sql);
			
			ls = connMgr.executeQuery(sql);
			

            while ( ls.next() ) {
                dbox = ls.getDataBox();
				dbox.put("d_percent"	   		,new Double(ls.getString("percent")));
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



}
