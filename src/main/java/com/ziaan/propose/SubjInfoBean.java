// **********************************************************
//  1. 제      목: 과정안내/신청
//  2. 프로그램: SubjInfoBean.java
//  3. 개      요: 과정안내/신청
//  4. 환      경: JDK 1.5
//  5. 버      젼: 1.0
//  6. 작      성: 2008.10.29
//  7. 수      정:
// **********************************************************
package com.ziaan.propose;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class SubjInfoBean { 
    private ConfigSet config;
    private int row;
    
    public SubjInfoBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
     * 과정검색
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList searchSubjseqD(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ListSet ls2         = null;
        ArrayList list     = null;
        ArrayList list2     = null;
        DataBox dbox        = null;
        DataBox dbox2        = null;
        String  sql        = "";
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 대분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 중분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");   // 중분류
        String  search_gubun = box.getString("p_searchGubun");
        String  v_searchtext = box.getString("p_searchtext");   // 과정검색
    	String v_year = FormatDate.getDate("yyyy"); // 올해
    	
    	String  v_preyear =  (Integer.parseInt(v_year) - 1) + "";
    	String  v_nextyear = (Integer.parseInt(v_year) + 1) + "";
    	
    	//수정해야함
    	String  v_premm = FormatDate.getDate("MM");     // 이번 달
    	String  v_predd = FormatDate.getDate("dd");     // 오늘

        String v_eduyear = box.getStringDefault("p_eduyear", v_year);  // 교육시작년
    	String  v_nextmm = FormatDate.getDateAdd("MM","month",1);     // 다음달
    	
    	if(v_premm.equals("12") && v_nextmm.equals("01") && search_gubun.equals("")){
    		v_eduyear = v_nextyear;
    	}
    	
    	String  v_mm = "";

    	if(Integer.parseInt(v_predd) > 15){
    		v_mm = v_nextmm;
    	}
    	else{
    		v_mm = v_premm;
    	}
    	
    	String v_edumm = "";
    	
    	if(!"".equals(box.getString("p_sTrue"))){
    		v_edumm = box.getStringDefault("p_edumm",v_mm);	       // 교육시작월
    	}
    	
        int v_pageno = box.getInt("p_pageno");
        String v_gubun = box.getStringDefault("p_gubun", "A");
        String v_job_cd = box.getSession("jobcd");
        String v_myjikmu = box.getString("p_myjikmu");
		String v_comp      = box.getSession("comp");
		String v_isgoyong  = box.getStringDefault("p_isgoyong", "ALL");
		String v_isonoff  = box.getStringDefault("p_isonoff", "ON");
		String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            list2 = new ArrayList();
            
            /*
            sql = "select b.subj													\n"
            	 + "	, b.year													\n"
            	 + "	, b.subjseq													\n"
            	 + "	, b.subjnm													\n"												
            	 + "	, a.upperclass												\n"											
            	 + "	, a.middleclass												\n"											
            	 + "	, a.lowerclass												\n"											
            	 + "	, b.edustart												\n"												
            	 + "	, b.eduend													\n"												
            	 + "	, b.subjnm													\n"												
            	 + "	, a.contenttype												\n"		
            	 + "    , GET_SUBJCLASSNM(a.upperclass, '000', '000') upperclassnm	\n"
            	 + "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, '000') middleclassnm	\n"
            	 + "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass) lowerclassnm	\n"
            	 + "    , a.eduperiod												\n"
            	 + "    , GET_CDNM('013', a.lev) lev                                \n"
            	 + "    , a.place                                                   \n"
            	 + "	, (															\n"
                 + "	   select count(*)											\n"
                 + "	   from   tz_stold_comments									\n"
                 + "	   where  subj = b.subj										\n"
                 + "       and    year = b.year                                     \n"
                 + "       and    subjseq = b.subjseq                               \n"
                 + "	  ) comments_cnt                                            \n"
                 + "    , (select cd_nm as codenm 									\n"
            	 + "       from   tk_edu000t 										\n"
            	 + "       where  co_cd  = '037'									\n"
            	 + "       and    cd = a.org) org 	                                \n"
                 + "    , decode(a.subj_gu, 'J', 'JIT') subj_gu                       \n"
//                 + "    , case when c.job_cd = " + SQLString.Format(v_job_cd) + " then '직무' else '-' end job_cd  \n"
                 + "  , case when c.subj is not null then '직무' else '-' end job_cd  \n"
            	 + "from tz_subj a inner join vz_scsubjseq b							\n"
            	 + "on a.subj = b.subj												\n"	
            	 + "left outer join (												\n"
            	 + "    select job_cd, subj, year									\n"
            	 + "    from   tz_subjjikmu											\n"
            	 + "    where    job_cd = " + SQLString.Format(v_job_cd) + "		\n"
            	 + ") c								                                \n"
            	 + "on a.subj = c.subj                                              \n"
            	 + "and b.year= c.year                                              \n"
            	 + "where a.isuse = 'Y'												\n"											
            	 + "and b.seqvisible = 'Y'											\n"
            	 + "and a.isopenedu = 'N'											\n"
            	 + "and b.edustart like " + SQLString.Format(v_eduyear + v_edumm + "%") + " \n"
            	 + "and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(v_comp) + ") \n";*/

            sql = "\n select distinct b.subj " 
                    + "\n      , b.year "
//                    + "\n      , b.subjseq "
                    + "\n      , b.subjnm "
                    + "\n      , b.subjseq "
                    + "\n      , a.upperclass "
                    + "\n      , a.middleclass "
                    + "\n      , a.lowerclass "
                    + "\n      , b.edustart "
                    + "\n      , b.eduend "
                    + "\n      , a.contenttype "
                    + "\n      , get_subjclass_fullnm (a.subjclass) upperclassnm "
                    + "\n      , a.eduperiod, get_cdnm ('013', a.lev) lev "
                    + "\n      , a.place "
//                    + "\n      , d.comments_cnt "	
                    + "\n      ,(select cd_nm as codenm "
                    + "\n        from   tk_edu000t "
                    + "\n        where  co_cd = '037' "
                    + "\n        and    cd = a.org) org "
                    + "\n      , decode (a.subj_gu, 'J', 'JIT') subj_gu "
//                    + "\n      , case when c.subj is not null then '직무' "
//                    + "\n             else '-' "
//                    + "\n        end job_cd "
                    + "\n      , '-' as job_cd "
                    + "\n      , nvl(cp,'') as cp "
                    + "\n      , rank() over(order by a.subjnm) as rn, a.isonoff " 
                    + "\n      , get_codenm('0004', a.isonoff) as codenm "
                    + "\n      , a.isgoyong "
                    + "\n	   , (case when to_char(sysdate, 'YYYYMMDDHH24') between propstart and to_char(to_date(nvl(propend,'2000010123'), 'YYYYMMDDHH24'), 'YYYYMMDDHH24') then '신청가능'	\n"
                	+ "\n	   	 	  when to_char(sysdate, 'YYYYMMDDHH24') < propstart then '신청전'					\n"
                	+ "\n	 	 	  when to_char(sysdate, 'YYYYMMDDHH24') > to_char(to_date(nvl(propend,'2000010123'), 'YYYYMMDDHH24'), 'YYYYMMDDHH24') then '마감'	\n" 
                	+ "\n			  else '' end) propstatus								\n"
                	+ "    , (select count(*) cnt from tz_propose where subj = a.subj and year = b.year and subjseq = b.subjseq and userid = "+StringManager.makeSQL(s_userid)+") is_propose \n"
                    + "\n from   tz_subj a "
                    + "\n      , tz_subjseq b "
//                    + "\n      ,(select job_cd "
//                    + "\n             , subj "
//                    + "\n             , year "
//                    + "\n        from   tz_subjjikmu "
//                    + "\n        where  job_cd = " + SQLString.Format(v_job_cd) + ") c "
//                    + "\n	    , (select subj, year, subjseq, count (*) comments_cnt	\n" 
//                    + "\n          from   tz_stold_comments								\n" 
//                    + "\n   	   group by subj, year, subjseq							\n"
//                    + "\n		) d														\n"  
                    + "\n where  a.subj = b.subj "
    				+ "\n   and  b.course = '000000'"                        
//                    + "\n and b.subj = d.subj(+) "
//                    + "\n and b.year = d.year(+) "
//                    + "\n and b.subjseq = d.subjseq(+) "
//                    + "\n and    b.subj = c.subj (+) "
//                    + "\n and    b.year = c.year (+) "
                    + "\n and    a.isuse = 'Y' "
                    + "\n and    b.isvisible = 'Y' "
                    + "\n and    a.isopenedu = 'N' "
                    //+ "\n and    b.edustart like " + SQLString.Format(v_eduyear + v_edumm + "%")
                    + "\n and    b.PROPSTART like " + SQLString.Format(v_eduyear + v_edumm + "%")
                    + "\n and    b.grcode in (select grcode "
                    + "\n                     from   tz_grcomp "
                    + "\n                     where  comp = " + StringManager.makeSQL(v_comp) + ") ";
                
                if ( !v_searchtext.equals("") ) { 
                	v_searchtext = v_searchtext.replaceAll("'", "");
                	v_searchtext = v_searchtext.replaceAll("/", "//");
                	v_searchtext = v_searchtext.replaceAll("%", "/%").toUpperCase();
                	sql += "\n and    upper(b.subjnm) like " +SQLString.Format("%"+v_searchtext+"%") + " escape '/' ";
                }
        
                //if ("Y".equals(v_myjikmu)) {
                //	sql += "\n and    c.job_cd = " + SQLString.Format(v_job_cd) ;
                //}
                
                	//sql += "\n and    substr(a.subj,-1) in ( " + v_value + ")  ";
                	
                if ( !ss_uclass.equals("ALL"))     sql += "\n and    a.upperclass     = " +SQLString.Format(ss_uclass);
                if ( !ss_mclass.equals("ALL"))     sql += "\n and    a.middleclass    = " +SQLString.Format(ss_mclass);
                if ( !ss_lclass.equals("ALL"))     sql += "\n and    a.lowerclass    = " +SQLString.Format(ss_lclass);
                if ( !v_isgoyong.equals("ALL"))    sql += "\n and    a.isgoyong    = " +SQLString.Format(v_isgoyong);
                if ( !v_isonoff.equals("ALL"))    sql += "\n and    a.isonoff    = " +SQLString.Format(v_isonoff);
             	   
                    sql += "\n order by b.subjnm  ";                  
           // System.out.println("000000000000000000000000000000000000000000000000\n"+sql);
            
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
            int totalpagecount = ls.getTotalPage();    //     전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount();    //     전체 row 수를 반환한다


            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum()+1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(row));
                
                if("B".equals(v_gubun)) {	// 독서교육
	                sql = "select a.month													\n"
	                	+ "    , b.bookcode													\n"
	                	+ "    , b.bookname													\n"
	                	+ "    , c.cnt														\n"
	                	+ "from tz_subjbook a, tz_bookinfo b, (								\n"
	                	+ "    select subj, month, count(*) cnt								\n"
	                	+ "    from tz_subjbook												\n"
	                	+ "    group by subj, month											\n"
	                	+ ") c																\n"
	                	+ "where a.bookcode = b.bookcode									\n"
	                	+ "and a.subj = c.subj												\n" 
	                	+ "and a.month = c.month											\n"
	                	+ "and a.subj = " + StringManager.makeSQL(ls.getString("subj")) + " \n"
	                	+ "order by a.month, b.bookname                                     \n";
	                
	                if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
	                list2 = new ArrayList();
	                ls2 = connMgr.executeQuery(sql);                
	                while(ls2.next()) {
	                	dbox2 = ls2.getDataBox();
	                	list2.add(dbox2);
	                }
	                dbox.put("d_bookinfoList", list2);
                }                
                list.add(dbox);
                list2 = null;
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

	/**
	 * 과정검색
	 * @param box      receive from the form object and session
	 * @return ArrayList
	 */
	public ArrayList searchSubjseqD2(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet ls         = null;
		ListSet ls2         = null;
		ArrayList list     = null;
		ArrayList list2     = null;
		DataBox dbox        = null;
		DataBox dbox2        = null;
		String  sql        = "";
		String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 대분류
		String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 중분류
		String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");   // 중분류
		String  search_gubun = box.getString("p_searchGubun");
		String  v_searchtext = box.getString("p_searchtext");   // 과정검색
		String v_year = FormatDate.getDate("yyyy"); // 올해
		
		String  v_preyear =  (Integer.parseInt(v_year) - 1) + "";
		String  v_nextyear = (Integer.parseInt(v_year) + 1) + "";
		
		//수정해야함
		String  v_premm = FormatDate.getDate("MM");     // 이번 달
		String  v_predd = FormatDate.getDate("dd");     // 오늘
		
		String v_eduyear = box.getStringDefault("p_eduyear", v_year);  // 교육시작년
		String  v_nextmm = FormatDate.getDateAdd("MM","month",1);     // 다음달
		String v_edumm = "";
		
		System.out.println("p_sTrue::::::::::"+box.getString("p_sTrue"));
		
		if(!"".equals(box.getString("p_sTrue"))){
				v_edumm = box.getString("p_edumm");
		}
		
		int v_pageno = box.getInt("p_pageno");
		String v_gubun = box.getStringDefault("p_gubun", "A");
		String v_job_cd = box.getSession("jobcd");
		String v_myjikmu = box.getString("p_myjikmu");
		String v_comp      = box.getSession("comp");
		String v_isgoyong  = box.getStringDefault("p_isgoyong", "ALL");
		String v_isonoff  = box.getStringDefault("p_isonoff", "ON");
		
		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			list2 = new ArrayList();
			
			/*
            sql = "select b.subj													\n"
            	 + "	, b.year													\n"
            	 + "	, b.subjseq													\n"
            	 + "	, b.subjnm													\n"												
            	 + "	, a.upperclass												\n"											
            	 + "	, a.middleclass												\n"											
            	 + "	, a.lowerclass												\n"											
            	 + "	, b.edustart												\n"												
            	 + "	, b.eduend													\n"												
            	 + "	, b.subjnm													\n"												
            	 + "	, a.contenttype												\n"		
            	 + "    , GET_SUBJCLASSNM(a.upperclass, '000', '000') upperclassnm	\n"
            	 + "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, '000') middleclassnm	\n"
            	 + "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass) lowerclassnm	\n"
            	 + "    , a.eduperiod												\n"
            	 + "    , GET_CDNM('013', a.lev) lev                                \n"
            	 + "    , a.place                                                   \n"
            	 + "	, (															\n"
                 + "	   select count(*)											\n"
                 + "	   from   tz_stold_comments									\n"
                 + "	   where  subj = b.subj										\n"
                 + "       and    year = b.year                                     \n"
                 + "       and    subjseq = b.subjseq                               \n"
                 + "	  ) comments_cnt                                            \n"
                 + "    , (select cd_nm as codenm 									\n"
            	 + "       from   tk_edu000t 										\n"
            	 + "       where  co_cd  = '037'									\n"
            	 + "       and    cd = a.org) org 	                                \n"
                 + "    , decode(a.subj_gu, 'J', 'JIT') subj_gu                       \n"
//                 + "    , case when c.job_cd = " + SQLString.Format(v_job_cd) + " then '직무' else '-' end job_cd  \n"
                 + "  , case when c.subj is not null then '직무' else '-' end job_cd  \n"
            	 + "from tz_subj a inner join vz_scsubjseq b							\n"
            	 + "on a.subj = b.subj												\n"	
            	 + "left outer join (												\n"
            	 + "    select job_cd, subj, year									\n"
            	 + "    from   tz_subjjikmu											\n"
            	 + "    where    job_cd = " + SQLString.Format(v_job_cd) + "		\n"
            	 + ") c								                                \n"
            	 + "on a.subj = c.subj                                              \n"
            	 + "and b.year= c.year                                              \n"
            	 + "where a.isuse = 'Y'												\n"											
            	 + "and b.seqvisible = 'Y'											\n"
            	 + "and a.isopenedu = 'N'											\n"
            	 + "and b.edustart like " + SQLString.Format(v_eduyear + v_edumm + "%") + " \n"
            	 + "and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(v_comp) + ") \n";*/
			
			sql = "\n select distinct b.subj " 
				+ "\n      , b.year "
                    + "\n      , b.subjseq "
				+ "\n      , b.subjnm "
				+ "\n      , a.upperclass "
				+ "\n      , a.middleclass "
				+ "\n      , a.lowerclass "
				+ "\n      , b.edustart "
				+ "\n      , b.eduend "
				+ "\n      , a.contenttype "
				+ "\n      , get_subjclass_fullnm (a.subjclass) upperclassnm "
				+ "\n      , a.eduperiod, get_cdnm ('013', a.lev) lev "
				+ "\n      , a.place "
//                    + "\n      , d.comments_cnt "	
				+ "\n      ,(select cd_nm as codenm "
				+ "\n        from   tk_edu000t "
				+ "\n        where  co_cd = '037' "
				+ "\n        and    cd = a.org) org "
				+ "\n      , decode (a.subj_gu, 'J', 'JIT') subj_gu "
//				+ "\n      , case when c.subj is not null then '직무' "
//				+ "\n             else '-' "
//				+ "\n        end job_cd "
				+ "\n      , '-' as job_cd "
				+ "\n      , nvl(cp,'') as cp "
				+ "\n      , rank() over(order by a.subjnm) as rn, a.isonoff " 
				+ "\n      , get_codenm('0004', a.isonoff) as codenm "
				+ "\n      , a.isgoyong "
                + "\n	   , (case when to_char(sysdate, 'YYYYMMDDHH24') between propstart and to_char(to_date(nvl(propend,'2000010123'), 'YYYYMMDDHH24'), 'YYYYMMDDHH24') then '신청가능'	\n"
            	+ "\n	   	 	  when to_char(sysdate, 'YYYYMMDDHH24') < propstart then '신청전'					\n"
            	+ "\n	 	 	  when to_char(sysdate, 'YYYYMMDDHH24') > to_char(to_date(nvl(propend,'2000010123'), 'YYYYMMDDHH24'), 'YYYYMMDDHH24') then '마감'	\n" 
            	+ "\n			  else '' end) propstatus								\n"
				+ "\n from   tz_subj a "
				+ "\n      , tz_subjseq b "
//				+ "\n      ,(select job_cd "
//				+ "\n             , subj "
//				+ "\n             , year "
//				+ "\n        from   tz_subjjikmu "
//				+ "\n        where  job_cd = " + SQLString.Format(v_job_cd) + ") c "
//                    + "\n	    , (select subj, year, subjseq, count (*) comments_cnt	\n" 
//                    + "\n          from   tz_stold_comments								\n" 
//                    + "\n   	   group by subj, year, subjseq							\n"
//                    + "\n		) d														\n"  
				+ "\n where  a.subj = b.subj "
				+ "\n   and  b.course = '000000'"                        
//                    + "\n and b.subj = d.subj(+) "
//                    + "\n and b.year = d.year(+) "
//                    + "\n and b.subjseq = d.subjseq(+) "
//				+ "\n and    b.subj = c.subj (+) "
//				+ "\n and    b.year = c.year (+) "
				+ "\n and    a.isuse = 'Y' "
				+ "\n and    b.isvisible = 'Y' "
				+ "\n and    a.isopenedu = 'N' "
				+ "\n and    b.edustart like " + SQLString.Format(v_eduyear + v_edumm + "%")
				+ "\n and    b.grcode in (select grcode "
				+ "\n                     from   tz_grcomp "
				+ "\n                     where  comp = " + StringManager.makeSQL(v_comp) + ") ";
			
			if ( !v_searchtext.equals("") ) { 
				v_searchtext = v_searchtext.replaceAll("'", "");
				v_searchtext = v_searchtext.replaceAll("/", "//");
				v_searchtext = v_searchtext.replaceAll("%", "/%").toUpperCase();
				sql += "\n and    upper(b.subjnm) like " +SQLString.Format("%"+v_searchtext+"%") + " escape '/' ";
			}
			
			//if ("Y".equals(v_myjikmu)) {
			//	sql += "\n and    c.job_cd = " + SQLString.Format(v_job_cd) ;
			//}
			
			//sql += "\n and    substr(a.subj,-1) in ( " + v_value + ")  ";
			
			if ( !ss_uclass.equals("ALL"))     sql += "\n and    a.upperclass     = " +SQLString.Format(ss_uclass);
			if ( !ss_mclass.equals("ALL"))     sql += "\n and    a.middleclass    = " +SQLString.Format(ss_mclass);
			if ( !ss_lclass.equals("ALL"))     sql += "\n and    a.lowerclass    = " +SQLString.Format(ss_lclass);
			if ( !v_isgoyong.equals("ALL"))    sql += "\n and    a.isgoyong    = " +SQLString.Format(v_isgoyong);
			if ( !v_isonoff.equals("ALL"))    sql += "\n and    a.isonoff    = " +SQLString.Format(v_isonoff);
			
			sql += "\n order by b.subjnm  ";                  
//			System.out.println("----------ㅇ " +sql);
			
			ls = connMgr.executeQuery(sql);
			ls.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
			ls.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
			int totalpagecount = ls.getTotalPage();    //     전체 페이지 수를 반환한다
			int totalrowcount = ls.getTotalCount();    //     전체 row 수를 반환한다
			
			
			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum()+1));
				dbox.put("d_totalpage", new Integer(totalpagecount));
				dbox.put("d_rowcount", new Integer(row));
				
				if("B".equals(v_gubun)) {	// 독서교육
					sql = "select a.month													\n"
						+ "    , b.bookcode													\n"
						+ "    , b.bookname													\n"
						+ "    , c.cnt														\n"
						+ "from tz_subjbook a, tz_bookinfo b, (								\n"
						+ "    select subj, month, count(*) cnt								\n"
						+ "    from tz_subjbook												\n"
						+ "    group by subj, month											\n"
						+ ") c																\n"
						+ "where a.bookcode = b.bookcode									\n"
						+ "and a.subj = c.subj												\n" 
						+ "and a.month = c.month											\n"
						+ "and a.subj = " + StringManager.makeSQL(ls.getString("subj")) + " \n"
						+ "order by a.month, b.bookname                                     \n";
					
					if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
					list2 = new ArrayList();
					ls2 = connMgr.executeQuery(sql);                
					while(ls2.next()) {
						dbox2 = ls2.getDataBox();
						list2.add(dbox2);
					}
					dbox.put("d_bookinfoList", list2);
				}                
				list.add(dbox);
				list2 = null;
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return list;
	}
	
	
	/**
	수강신청을 위한 코스
	@param box      receive from the form object and session
	@return ArrayList 코스 리스트
	*/
	public ArrayList selectCourseForApply(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		ListSet             ls1             = null;
		ArrayList           list1           = null;
		DataBox             dbox            = null;
		String 				sql = "";
		
    	String v_year 	= FormatDate.getDate("yyyy"); // 올해       
    	String v_month 	= FormatDate.getDate("MM");     // 이번 달		
		String v_comp   = box.getSession("comp");		

		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();

			sql = " select a.scsubj, a.scyear, a.scsubjseq, a.scsubjnm, subj, year, subjseq, subjnm, edustart, eduend, propstart, propend, isonoff, get_codenm('0004',a.isonoff) isonoffnm, isgoyong, contenttype   	\n"
				+ "      ,(select count(*) from tz_subjseq where course=a.course and cyear=a.cyear and courseseq=a.courseseq group by course, cyear, courseseq ) rowcount       \n"									
				+ "from vz_scsubjseq a, tz_grcomp c                                                                                                                             \n"
				+ "where a.course != '000000'                                                                                                                                   \n"
				+ "and a.grcode  = c.grcode 																																	\n"
				+ "and c.comp = " + SQLString.Format(v_comp) + "                                                                                                                \n"
				+ "and isuse = 'Y'                                                                                                                                              \n"
                + "and a.propstart like " + SQLString.Format(v_year + v_month + "%")				
				+ "and seqvisible = 'Y'                                                                                                                                         \n"
				+ "order by a.course, a.cyear, a.courseseq, subj                                                                                                                       \n";
//System.out.println("-------------sql---------------\n" + sql);
			ls1 = connMgr.executeQuery(sql);

			while ( ls1.next() ) { 
				dbox    = ls1.getDataBox();

				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return list1;
	}	
	
	
	 /**
     * 과정 카테고리별 카운트
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public static DataBox categorycnt(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        DataBox dbox        = null;

        String  sql        = "";
        
        String ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 대분류
        String ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 중분류
        String v_searchtext = box.getString("p_searchtext");   // 과정검색
    	String v_year = FormatDate.getDate("yyyy"); // 올해
    	String v_mm = FormatDate.getDate("MM");     // 이번 달
    	String v_comp = box.getSession("comp");

        String v_eduyear = box.getStringDefault("p_eduyear", v_year);  // 교육시작년
    	String v_edumm = box.getStringDefault("p_edumm", v_mm);	       // 교육시작월
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "select nvl(sum(case when gubun = 'D' then cnt end), 0) a				\n"
            	+ "    , nvl(sum(case when gubun = 'L' then cnt end), 0) b				\n"
            	+ "    , nvl(sum(case when gubun = 'A' then cnt end), 0) c				\n"
            	+ "    , nvl(sum(case when gubun = 'G' then cnt end), 0) d				\n"
            	+ "    , nvl(sum(case when gubun in ('K','O') then cnt end), 0) e		\n"
            	+ "    , nvl(sum(case when gubun = 'W' then cnt end), 0) f				\n"
            	+ "from (																\n"
            	+ "    select substr(a.subj,5,6) gubun, count(*) cnt					\n"				
            	+ "	   from tz_subj a, tz_subjseq b										\n"
            	+ "	   where a.subj = b.subj											\n"
            	+ "	   and a.isuse = 'Y'												\n"											
            	+ "    and b.isvisible = 'Y'											\n"
            	+ "    and b.grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(v_comp) + ") \n"
            	+ "    and b.edustart like " + SQLString.Format(v_eduyear + v_edumm + "%") + " \n";
        
	        if ( !v_searchtext.equals("") ) { 
            	v_searchtext = v_searchtext.replaceAll("'", "");
            	v_searchtext = v_searchtext.replaceAll("/", "//");
            	v_searchtext = v_searchtext.replaceAll("%", "/%").toUpperCase();
                sql += "and upper(b.subjnm) like " +SQLString.Format("%"+v_searchtext+"%") + " escape '/' \n";
	        }

	        if ( !ss_uclass.equals("ALL"))     sql += "and    a.upperclass     = " +SQLString.Format(ss_uclass) + "  \n";
	        if ( !ss_mclass.equals("ALL"))     sql += "and    a.middleclass    = " +SQLString.Format(ss_mclass) + "  \n";

            	
            sql	+= "    group by substr(a.subj,5,6)										\n"
            	+ ") x																	\n";
            
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }
	
	/**
     * 과정상세
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public DataBox searchSubjInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        
        DataBox dbox        = null;

        String  sql        = "";
        String v_subj = box.getString("p_subj");
    	String  v_comp      = box.getSession("comp");
        
        try { 
            connMgr = new DBConnectionManager();

            sql = "select 																	\n"
            //	+ "		 get_subjclass_fullnm (a.subjclass) upperclassnm 					\n" 	2010/05/07 update
            	+ "		 substr(get_subjclass_fullnm (a.subjclass),0,3) as uppcheck			\n"
            	+ "	   , b.codenm															\n"
            	+ "    , a.subj                                                             \n"
            	+ "	   , a.subjnm															\n"
            	+ "	   , a.eduperiod														\n"
            	+ "	   , a.edudays															\n"
            	+ "	   , a.studentlimit														\n" 
            	+ "	   , a.introducefilenamereal											\n"
            	+ "	   , a.introducefilenamenew												\n"
            	+ "    , a.isablereview                                                     \n"
            	+ "	   , reviewdays															\n"
            	+ "	   , usebook															\n"
            	+ "	   , bookname															\n"
            	+ "	   , isgoyong															\n"
            	+ "	   , biyong																\n"
            	+ "	   , goyongpricemajor													\n"
            	+ "	   , goyongpriceminor													\n"
            	+ "	   , goyongpricestand													\n"
            	+ "	   , edumans															\n"
            	+ "	   , intro																\n"								
            	+ "	   , explain															\n"
            	+ "	   , place																\n"
            	+ "    , wstep																\n"
            	+ "	   , gradstep															\n"
            	+ "	   , wmtest																\n"
            	+ "    , gradexam															\n"
            	+ "    , wftest																\n"
            	+ "    , gradftest															\n"
            	+ "    , wreport															\n"
            	+ "    , gradreport															\n"
            	+ "    , wetc1																\n"
            	+ "    , study_count														\n"
            	+ "    , gradscore                                                          \n"
            	+ "    , GET_CDNM('013', a.lev) lev 										\n" //교육수준
            	+ "    , GET_CDNM('015', a.gubn) gubn 										\n" //이수구분
                + "    , grade																\n" //역량등급
                + "    , test																\n" //교육평가
                + "    , musertel															\n"
                + "    , muserid															\n"
                + "    , get_name(muserid) musernm											\n"		
                + "	   , (																	\n"
                + "				select count(*)												\n"
                + "				from tz_stold_comments										\n"
                + "				where subj = a.subj											\n"
                + "				  and hidden_yn = 'Y'										\n"
                + "		) comments_cnt                                              		\n"
                + "    , a.isonoff 															\n"
                + "    , satisfaction                                                       \n"
                + "    , satisfaction1                                                      \n"
                + "    , satisfaction2                                                      \n"
                + "    , a.contenttype                                                      \n"
                + "    , GET_CDNM('037', a.org) org                                         \n"
                + "    , a.subj_gu                                                          \n"
                + "    , a.cp ,a.edutimes                                                   \n"
            	+ "from tz_subj a inner join (												\n"
            	+ "	  select code, codenm													\n"													
            	+ "	  from tz_code															\n"															
            	+ "	  where gubun = '0004'													\n"													
            	+ ") b																		\n"
            	+ "on a.isonoff = b.code													\n"
            	+ "left outer join (														\n"																	
            	+ "    select subj, round(to_number(sum(distcode1_avg))/to_number(count(*)), 1) satisfaction	\n"
            	+ "        , trunc(to_number(sum(distcode1_avg))/to_number(count(*))) satisfaction1				\n"
            	+ "		   , round(to_number(sum(distcode1_avg))/to_number(count(*)), 1) - trunc(to_number(sum(distcode1_avg))/to_number(count(*))) satisfaction2	\n"
            	+ "	   from tz_suleach														\n"		
            	+ "    group by subj														\n"
            	+ ") c																		\n"
            	+ "on a.subj = c.subj														\n"		
            	+ "where a.subj = " + StringManager.makeSQL(v_subj) + " 						\n";

            System.out.print(sql);
            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() )  {
                dbox = ls.getDataBox();
                dbox.put("d_satisfaction", new Double(ls.getDouble("satisfaction")));
                dbox.put("d_satisfaction2", new Double(ls.getDouble("satisfaction2")));
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }
	
	/**
     * 과정 차수 목록
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList subjseqList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list = null;
        DataBox dbox        = null;

        String  sql        = "";
        String v_subj   = box.getString("p_subj");
        
		String v_comp   = box.getSession("comp");
		String s_userid = box.getSession("userid");
		String v_gubun  = "N";
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = "select a.subj													\n"
            	+ "	   , a.year														\n"
            	+ "	   , a.subjseq													\n"
            	+ "	   , a.subjnm													\n"
            	+ "	   , a.edustart													\n"
            	+ "	   , a.eduend													\n"
            	+ "	   , b.name	tutornm												\n"
            	+ "	   , b.academic													\n"
            	+ "	   , b.career													\n"
            	+ "	   , b.book														\n"
            	+ "	   , a.studentlimit												\n"
            	+ "	   , (															\n"
            	+ "	   	  select count(*)											\n"
            	+ "		  from tz_propose											\n"
            	+ "		  where subj = a.subj										\n"
            	+ "		  and year = a.year											\n"
            	+ "		  and subjseq = a.subjseq									\n"
            	+ "	   ) propcnt													\n"
//            	+ "	   , 10000 AS propcnt											\n"
            	+ "	   , (case when to_char(sysdate, 'YYYYMMDDHH24') between propstart and to_char(to_date(nvl(propend,'2000010123'), 'YYYYMMDDHH24'), 'YYYYMMDDHH24') then '신청가능'	\n"
            	+ "	   	 	  when to_char(sysdate, 'YYYYMMDDHH24') < propstart then '신청전'					\n"
            	+ "	 	 	  when to_char(sysdate, 'YYYYMMDDHH24') > to_char(to_date(nvl(propend,'2000010123'), 'YYYYMMDDHH24'), 'YYYYMMDDHH24') then '마감'	\n" 
            	+ "			  else '' end) propstatus								\n"
            	+ "    , a.isgoyong	,a.proposetype												\n"
            	+ "    , (select count(*) cnt from tz_propose where subj = a.subj and year = a.year and subjseq = a.subjseq and userid = "+StringManager.makeSQL(s_userid)+") is_propose \n"
            	+ "    , (select nvl(emp_gubun, 'P') from tz_member where userid = "+StringManager.makeSQL(s_userid)+") emp_gubun \n"
            	+ "from vz_scsubjseq a left outer join (								\n"
            	+ "	 select x.subj, x.year, x.subjseq, x.tuserid, y.name, y.academic, y.career, y.book	\n"
            	+ "	 from (select subj, year, subjseq, max(tuserid) as tuserid from tz_classtutor group by subj, year, subjseq) x, tz_tutor y	\n"
            	+ "	 where x.tuserid = y.userid										\n"
            	+ ") b																\n"
            	+ "on a.subj = b.subj												\n"
            	+ "and a.year = b.year												\n"
            	+ "and a.subjseq = b.subjseq										\n"
            	+ "where a.subj = " + StringManager.makeSQL(v_subj) + "             \n"
            	+ "and seqvisible = 'Y' and course = '000000'                       \n" // 코스는 안나오도록 수정
            	+ "and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(v_comp) + ") \n"
            	+ "and ((a.subj || a.year || a.subjseq) in (						\n"
            	+ "     select  a.subjcourse || a.year || a.subjseq					\n"						
            	+ "     from    tz_mastersubj   a									\n"
            	+ "	    ,   tz_edutarget    b										\n"											
            	+ "     where   a.mastercd  = b.mastercd							\n"									                               
            	+ "     and a.subjcourse = " + StringManager.makeSQL(v_subj) + "    \n"
            	+ "     and b.userid = " + StringManager.makeSQL(s_userid)+ "		\n"
            	+ ") or (a.subj || a.year || a.subjseq) not in (					\n"
            	+ "     select  a.subjcourse || a.year || a.subjseq					\n"						
            	+ "     from    tz_mastersubj   a									\n"											
            	+ "	    ,   tz_edutarget    b										\n"											
            	+ "     where   a.mastercd  = b.mastercd							\n"									                               
            	+ "     and a.subjcourse = " + StringManager.makeSQL(v_subj) + "    \n"
            	+ "     and b.userid != " + StringManager.makeSQL(s_userid)+ "		\n"
            	+ "))																\n"
            	+ "and eduend > to_char(sysdate, 'yyyyMMddHH24') 					\n"
            	//마감된 기수는 안나오도록 수정
            	//+ "and to_char(sysdate, 'YYYYMMDDHH24') between propstart and to_char(to_date(nvl(propend,'2000010123'), 'YYYYMMDDHH24'), 'YYYYMMDDHH24')  \n"
            	+ "order by a.edustart, a.subjseq									\n";
            
            System.out.println("기수 - sql : "+sql);
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                ProposeCourseBean bean = new ProposeCourseBean();
                int result = bean.jeyakNotbirth_date(connMgr, ls.getString("subj"), ls.getString("year"), ls.getString("subjseq"), s_userid);
                if(result<0) {
                	v_gubun = "Y";
                } 
                dbox.put("d_goyong" , v_gubun);
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	/**
     * 교육내용
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList edumansList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list = null;
        DataBox dbox        = null;

        String  sql        = "";
        String v_subj = box.getString("p_subj");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = "select a.module, a.sdesc mdesc, b.lesson, b.sdesc ldesc					\n"  
            	+ "from tz_subjmodule a, tz_subjlesson b									\n"
            	+ "where a.subj = b.subj													\n"
            	+ "and a.module = b.module													\n"
            	+ "and a.subj = " + StringManager.makeSQL(v_subj) + "                       \n"
            	+ "order by a.module, b.lesson												\n";
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	
	/**
     * 교육후기
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList stoldCommentList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list = null;
        DataBox dbox        = null;

        String  sql        = "";
        String v_subj = box.getString("p_subj");
        int v_pageno = box.getInt("p_pageno");
        row = 5;
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = "select seq																\n"
            	+ "		, subj																\n"
            	+ "		, year																\n"
            	+ "		, subjseq															\n"
            	+ "		, userid															\n"
            	+ "		, comments															\n"
            	+ "		, ldate																\n"
            	+ "     , get_name(userid) name												\n"
            	+ "     , hidden_yn												\n"
            	+ "from tz_stold_comments													\n"
            	+ "where subj = " + StringManager.makeSQL(v_subj) + "                       	\n"
            	+ "  and hidden_yn = 'Y'                       	\n"
            	+ "order by ldate desc														\n";
            
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                                // 페이지당 row 갯수를 세팅한다.
            ls.setCurrentPage(v_pageno);                        // 현재페이지번호를 세팅한다.
            int     total_page_count    = ls.getTotalPage();    // 전체 페이지 수를 반환한다.
            int     total_row_count     = ls.getTotalCount();   // 전체 row 수를 반환한다.
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	/**
     * 교육후기
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList stoldCommentList2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list = null;
        DataBox dbox        = null;

        String  sql        = "";
        String v_subj = box.getString("p_subj");
        int v_pageno = box.getInt("p_pageno");
        row = 5;
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = "select seq																\n"
            	+ "		, subj																\n"
            	+ "		, year																\n"
            	+ "		, subjseq															\n"
            	+ "		, userid															\n"
            	+ "		, comments															\n"
            	+ "		, ldate																\n"
            	+ "     , get_name(userid) name												\n"
            	+ "     , hidden_yn												\n"
            	+ "from tz_stold_comments													\n"
            	+ "where subj = " + StringManager.makeSQL(v_subj) + "                       	\n"
            	+ "  and (hidden_yn = 'Y' or userid  = " + StringManager.makeSQL(box.getSession("userid")) + ")  \n"
            	+ "order by ldate desc														\n";
            
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                                // 페이지당 row 갯수를 세팅한다.
            ls.setCurrentPage(v_pageno);                        // 현재페이지번호를 세팅한다.
            int     total_page_count    = ls.getTotalPage();    // 전체 페이지 수를 반환한다.
            int     total_row_count     = ls.getTotalCount();   // 전체 row 수를 반환한다.
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	/**
     * 교육후기
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public DataBox stoldCommentView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        DataBox dbox        = null;

        String  sql        = "";
        String v_seq = box.getString("p_seq");
        
        try { 
            connMgr = new DBConnectionManager();
            sql = "select seq																\n"
            	+ "		, subj																\n"
            	+ "		, year																\n"
            	+ "		, subjseq															\n"
            	+ "		, userid															\n"
            	+ "		, comments															\n"
            	+ "		, ldate																\n"
            	+ "     , get_name(userid) name												\n"	
            	+ "from tz_stold_comments 													\n"
            	+ "where seq = " + StringManager.makeSQL(v_seq) + "                       	\n"
            	+ "order by ldate desc														\n";
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }

	
	/**
    수강신청 취소 목록
    @param box          receive from the form object and session
    @return ArrayList   수강신청 취소 목록
    */
    public ArrayList selectCancelPossibleList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        String  v_userid     = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "select																					\n"
            	+ "       a.userid,																			\n"
            	+ "       b.subj,																			\n"                                                                                                         
            	+ "       b.year,																			\n"                                                                                                         
            	+ "       b.subjseq,																		\n"                                                                                                      
            	+ "       b.subjnm,																			\n"                                                                                                       
            	+ "       b.propstart,																		\n"                                                                                                    
            	+ "       b.propend,																		\n"                                                                                                      
            	+ "       b.edustart,																		\n"                                                                                                     
            	+ "       b.eduend,																			\n"                                                                                                                                                                                                           
            	+ "       a.chkfinal,																		\n"                                                                                                     
            	+ "       a.cancelkind,																		\n"                                                                                                   
            	+ "       TO_CHAR(TO_DATE(a.ldate, 'YYYYMMDDHH24MISS'), 'MM\"월\" DD\"일\"') propose_date, 	\n"                                 
            	+ "       to_char(TO_DATE(substr(nvl(b.propend,'2000010100'),0,8),'YYYYMMDD')+nvl(b.canceldays,0), 'YYYYMMDD') cancelend	\n"  
            	+ "       , get_codenm('0004', d.isonoff) codenm											\n"
            	+ "       , d.isonoff																		\n"  
            	+ "       , d.eduperiod																		\n"
            	+ "from																						\n"                                                                                                                 
            	+ "       TZ_PROPOSE a,																		\n"
            	+ "       tz_subjseq b, 																	\n"
            	+ "       tz_subj d		  																	\n"
            	+ "where a.subj = b.subj																	\n"                                                                                                 
            	+ "and a.year = b.year																		\n"                                                                                                 
            	+ "and a.subjseq = b.subjseq																\n"                                                                                           								
            	+ "and b.subj = d.subj																		\n"
            	+ "and a.userid = " + StringManager.makeSQL(v_userid) + "                                   \n"
            	+ "and a.chkfinal != 'N'																	\n"    
            	+ "and b.course = '000000'																	\n"                	
            	//+ "and to_char(sysdate,'YYYYMMDDHH') between b.propstart and (to_char(TO_DATE(substr(nvl(b.propend,'2000010100'),0,8),'YYYYMMDD')+1+b.canceldays, 'YYYYMMDD') || '00')	\n"   
                + "and ((a.chkfinal = 'Y' and (to_char(sysdate,'YYYYMMDDHH24') < b.edustart))  				\n"
                + "       or (a.chkfinal='N' and (to_char(sysdate,'YYYYMMDDHH24') < to_char(to_date(substr(b.edustart,1,8))+10,'YYYYMMDDHH24')))  \n"
                + "       or (a.chkfinal='B' and (to_char(sysdate,'YYYYMMDDHH24') < b.propend)) 			\n"                       
                + "       or (a.chkfinal='B' and (to_char(sysdate,'YYYYMMDDHH24') < b.edustart))) 			\n"         
            	+ "order by b.subjnm, b.edustart desc														\n";
            
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
    취소신청가능 코스리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   취소신청가능 코스리스트
    */
    public ArrayList selectCourseCancelList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        String  v_userid     = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "select																					\n"
            	+ "       a.userid,																			\n"
            	+ "       b.scsubj,																			\n"                                                                                                         
            	+ "       b.scyear,																			\n"                                                                                                         
            	+ "       b.scsubjseq,																		\n"                                                                                                      
            	+ "       b.scsubjnm,																		\n"               	
            	+ "       b.subj,																			\n"                                                                                                         
            	+ "       b.year,																			\n"                                                                                                         
            	+ "       b.subjseq,																		\n"                                                                                                      
            	+ "       b.subjnm,																			\n"                                                                                                       
            	+ "       b.propstart,																		\n"                                                                                                    
            	+ "       b.propend,																		\n"                                                                                                      
            	+ "       b.edustart,																		\n"                                                                                                     
            	+ "       b.eduend,																			\n"                                                                                                                                                                                                           
            	+ "       a.chkfinal,																		\n"                                                                                                     
            	+ "       a.cancelkind,																		\n"                                                                                                   
            	+ "       TO_CHAR(TO_DATE(a.ldate, 'YYYYMMDDHH24MISS'), 'MM\"월\" DD\"일\"') propose_date, 	\n"                                 
            	+ "       to_char(TO_DATE(substr(nvl(b.propend,'2000010100'),0,8),'YYYYMMDD')+nvl(b.canceldays,0), 'YYYYMMDD') cancelend	\n"  
            	+ "       , get_codenm('0004', b.isonoff) codenm											\n"
            	+ "       , b.isonoff																		\n"  
            	+ "       , d.cnt																			\n"            	
            	+ "from																						\n"                                                                                                                 
            	+ "       TZ_PROPOSE a,																		\n"
            	+ "       vz_scsubjseq b, 																	\n"
            	//+ "       tz_subj d		  																	\n"
            	+ " ( select scsubj,scyear,scsubjseq, count(*) cnt from tz_propose x, vz_scsubjseq y where x.subj=y.subj and x.year=y.year and x.subjseq=y.subjseq and userid ="+ StringManager.makeSQL(v_userid) + " group by scsubj, scyear, scsubjseq, userid ) d "
            	+ "where a.subj = b.subj																	\n"                                                                                                 
            	+ "and a.year = b.year																		\n"                                                                                                 
            	+ "and a.subjseq = b.subjseq																\n"                                                                                           								
            	//+ "and b.subj = d.subj																		\n"
            	+ "and b.scsubj = d.scsubj																	\n"                                                                                                 
            	+ "and b.scyear = d.scyear																	\n"                                                                                                 
            	+ "and b.scsubjseq = d.scsubjseq															\n"                	
            	+ "and a.userid = " + StringManager.makeSQL(v_userid) + "                                   \n"
            	+ "and a.chkfinal != 'N'																	\n"    
            	+ "and b.course != '000000'																	\n"                	
            	//+ "and to_char(sysdate,'YYYYMMDDHH') between b.propstart and (to_char(TO_DATE(substr(nvl(b.propend,'2000010100'),0,8),'YYYYMMDD')+1+b.canceldays, 'YYYYMMDD') || '00')	\n"   
                + "and ((a.chkfinal = 'Y' and (to_char(sysdate,'YYYYMMDDHH24') < b.cedustart))  			\n"
                + "       or (a.chkfinal='N' and (to_char(sysdate,'YYYYMMDDHH24') < to_char(to_date(substr(b.cedustart,1,8))+10,'YYYYMMDDHH24')))  \n"
                + "       or (a.chkfinal='B' and (to_char(sysdate,'YYYYMMDDHH24') < b.cpropend)) 			\n"                       
                + "       or (a.chkfinal='B' and (to_char(sysdate,'YYYYMMDDHH24') < b.cedustart))) 			\n"         
            	+ "order by b.subjnm, b.edustart desc														\n";
            
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
    수강신청 취소/반려 목록
    @param box          receive from the form object and session
    @return ArrayList   수강신청 취소/반려 목록
    */
    public ArrayList selectCancelList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        String  v_userid     = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "select																					\n"
            	+ "       b.subj,																			\n"                                                                                                         
            	+ "       b.year,																			\n"                                                                                                         
            	+ "       b.subjseq,																		\n"                                                                                                      
            	+ "       b.subjnm,																			\n"                                                                                                       
            	+ "       a.userid,																			\n"            	
            	+ "       get_codenm('0036',a.cancelkind) cancelkind,										\n"                                                                                                     
            	+ "       a.canceldate,																		\n"                                                                                                   
            	+ "       a.reason,																			\n"                                                                                                   
            	+ "       get_codenm('0096',a.reasoncd) reasoncd,											\n"                                                                                                   
            	+ "       get_codenm('0004', b.isonoff) codenm												\n"
            	+ "from																						\n"                                                                                                                 
            	+ "       TZ_CANCEL a,																		\n"
            	+ "       vz_scsubjseq b 																	\n"
            	+ "where a.subj = b.subj																	\n"                                                                                                 
            	+ "and a.year = b.year																		\n"                                                                                                 
            	+ "and a.subjseq = b.subjseq																\n"                                                                                           								
            	+ "and a.userid = " + StringManager.makeSQL(v_userid) + "                                   \n"
            	+ "order by a.canceldate desc, b.subjnm														\n";
            
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
     * 취소처리 
     * @param box      receive from the form object and session
     * @return int
     */
	public int SaveCancel(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String sql1         = "";
        int isOk            = 0;
        int isOk1           = 0;
        int isOk4           = 0;

        Hashtable insertData= new Hashtable();

        String v_userid     = box.getSession("userid");
        String v_subj       = box.getString("p_subj");
        String  v_year      = box.getString("p_year");
        String  v_subjseq   = box.getString("p_subjseq");
        String v_cancelkind = "P";
        String v_reason     = box.getString("p_rejreasonnm");
        String v_reasoncd   = box.getString("p_rejreasoncd");

        ProposeBean probean = new ProposeBean();
        String v_luserid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            insertData.clear();
            insertData.put("connMgr"       , connMgr     );
            insertData.put("subj"          , v_subj      );
            insertData.put("year"          , v_year      );
            insertData.put("subjseq"       , v_subjseq   );
            insertData.put("userid"        , v_userid    );
            insertData.put("luserid"       , v_luserid   );
            insertData.put("cancelkind"    , v_cancelkind);
            insertData.put("chkfinal"      , "N"         );
            insertData.put("rejectkind"    , v_reasoncd  );
            insertData.put("rejectedreason", v_reason    );


            isOk1 = probean.deletePropose(insertData);

            insertData.put("reason",     v_reason);
            insertData.put("reasoncd",   v_reasoncd);

            isOk4 = probean.insertCancel(insertData);

            isOk = isOk1*isOk4;

            if ( isOk > 0 ) {   
            	connMgr.commit();   
            } else {   
            	connMgr.rollback(); 
            }
        } catch ( Exception ex ) { 
            isOk =0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
	
	 /**
    관심과정 저장
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int SaveSubjConcern(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 0;
        
        String              v_subj      = box.getString("p_subj"    );
        String              s_userid    = box.getSession("userid"   );
        
        try { 
            connMgr = new DBConnectionManager();
                  
            sbSQL.append(" select userid from TZ_SUBJ_CONCERN where subj = " + StringManager.makeSQL(v_subj) + "and userid = " + StringManager.makeSQL(s_userid) + "    \n");
            
            ls       = connMgr.executeQuery(sbSQL.toString());
           
            if ( !ls.next() ) { 
                ls.close();
                
                sbSQL.setLength(0);
                
                sbSQL.append(" INSERT INTO TZ_SUBJ_CONCERN                      \n")
                     .append(" (                                                \n")
                     .append("         SUBJ                                     \n")
                     .append("     ,   USERID                                   \n")
                     .append("     ,   LUSERID                                  \n")
                     .append("     ,   LDATE                                	\n")
                     .append(" ) VALUES (                                       \n")
                     .append("         ?                                        \n")
                     .append("     ,   ?									    \n")
                     .append("     ,   ?                                        \n")
                     .append("     ,   to_char(sysdate, 'yyyymmddhh24miss')     \n")
                     .append(" )                                                \n");
                     
                pstmt   = connMgr.prepareStatement(sbSQL.toString());
                
                pstmt.setString(1, v_subj        );
                pstmt.setString(2, s_userid       );
                pstmt.setString(3, s_userid     );
                
                
                isOk     = pstmt.executeUpdate();
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }
    
    
    /**
    관심과정 저장
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public String SaveSubjConcernForCheckbox(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 0;
        
        String              v_subj      = "";
        String              v_subj2      = "";
        String              s_userid    = box.getSession("userid"   );
        Vector v_checks = box.getVector("p_checks");
		Enumeration em = v_checks.elements();
		String v_subjnm = "";
		String v_message = "";
		
		try { 
            connMgr = new DBConnectionManager();
        
        	while (em.hasMoreElements()) {
        		v_subj = (String) em.nextElement();
				sbSQL.setLength(0);
	            sbSQL.append(" select b.subjnm, userid from TZ_SUBJ_CONCERN a, tz_subj b where a.subj = b.subj and a.subj = " + StringManager.makeSQL(v_subj) + "and userid = " + StringManager.makeSQL(s_userid) + "    \n");
	            
	            ls       = connMgr.executeQuery(sbSQL.toString());
	           
	            if ( !ls.next() ) { 
	                ls.close();
	                
	                sbSQL.setLength(0);
	                
	                sbSQL.append(" INSERT INTO TZ_SUBJ_CONCERN                      \n")
	                     .append(" (                                                \n")
	                     .append("         SUBJ                                     \n")
	                     .append("     ,   USERID                                   \n")
	                     .append("     ,   LUSERID                                  \n")
	                     .append("     ,   LDATE                                	\n")
	                     .append(" ) VALUES (                                       \n")
	                     .append("         ?                                        \n")
	                     .append("     ,   ?									    \n")
	                     .append("     ,   ?                                        \n")
	                     .append("     ,   to_char(sysdate, 'yyyymmddhh24miss')     \n")
	                     .append(" )                                                \n");
	                     
	                pstmt   = connMgr.prepareStatement(sbSQL.toString());
	                
	                pstmt.setString(1, v_subj        );
	                pstmt.setString(2, s_userid       );
	                pstmt.setString(3, s_userid     );
	                
	                
	                isOk     += pstmt.executeUpdate();
	                
	                if ( pstmt != null ) { 
	                    try { 
	                        pstmt.close();  
	                    } catch ( Exception e ) { } 
	                }
	            } else {
	            	//v_subjnm += ls.getString("subjnm") + ",";
	            	v_subj2 += v_subj + ",";
	            }
            }
        	
        	/*
        	if(v_subjnm.length() > 1) {
        		v_subjnm = v_subjnm.substring(0, v_subjnm.length()-1);
        	}
        	if("".equals(v_subjnm)) {
        		v_message = "선택하신 과정을 관심과정으로 등록하였습니다.";
        	} else {
	        	if(isOk > 0) {
	            	v_message = v_subjnm + "과정은 이미 관심과정으로 등록되어 있습니다.";
	            } else {
	            	v_message = "이미 관심과정으로 등록되어 있습니다.";
	            }
        	}
        	*/
        	
        	//특수문자때문에 수정.
        	if(v_subj2.length() > 1) {
        		v_subj2 = v_subj2.substring(0, v_subj2.length()-1);
        	}
        	if("".equals(v_subj2)) {
        		v_message = "insertOk";
        	} else {
	        	if(isOk > 0) {
	            	v_message = v_subj2;
	            } else {
	            	v_message = "aleadySubject";
	            }
        	}
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return v_message;
    }
    

	/**
	연간 교육 일정 리스트
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectEducationYearlyList(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet ls1             = null;
		ArrayList list          = null;
		String sql1             = "";
		DataBox dbox = null;
		
		String  v_gyear         = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );

		// 개인의 회사 기준으로 과정 리스트
		String  v_comp      = box.getSession("comp");

		String  v_select        = box.getString("p_select");
		String v_lastdate = "20081231";
		String v_month = "12";
		int v_day = 1;
		String v_day2 = "01"; 
		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();

			sql1 ="select subj, year, subjnm, isonoff								\n";
				for(int i=1; i<=12; i++) {
					if(i <10) {
						v_month = "0" + i;
					} else {
						v_month = i + "";
					}
					
					v_lastdate = FormatDate.getEnddate(v_gyear + v_month + "01");
					v_day = Integer.parseInt(v_lastdate.substring(6, 8));
					for(int j=1; j <=v_day; j++ ) {
						if(j < 10) {
							v_day2 = "0" + j;
						} else {
							v_day2 = j + "";
						}
//					sql1+= "	   , case when a" + v_month + v_day2 + " = 'Y' then			\n" 
//						+ "	   		case when isonoff='OFF' then 'tdbarOff'					\n"
//						+ "				 when isonoff='RC' then 'tdbarBook'					\n"
//						+ "				 when isonoff='ON' then 'tdbarOn' end				\n"
//						+ "		else 'tdbarBlank' end a" + v_month + v_day2 + "				\n";	
					sql1+= "	   , decode(a" + v_month + v_day2 + ", 'Y', (case when isonoff='OFF' then 'tdbarOff'	\n"					
						+ "			 when isonoff='RC' then 'tdbarBook'													\n"
						+ "			 when isonoff='ON' then 'tdbarOn' end), 'tdbarBlank') a" + v_month + v_day2 + "		\n";						
					}
				}
				sql1+= "from (															\n";
			sql1 +="select subj, year, max(subjnm) subjnm, max(isonoff) isonoff			\n";
				for(int i=1; i<=12; i++) {
					if(i <10) {
						v_month = "0" + i;
					} else {
						v_month = i + "";
					}
					
					v_lastdate = FormatDate.getEnddate(v_gyear + v_month + "01");
					v_day = Integer.parseInt(v_lastdate.substring(6, 8));
					for(int j=1; j <=v_day; j++ ) {
						if(j < 10) {
							v_day2 = "0" + j;
						} else {
							v_day2 = j + "";
						}
						sql1+= ", max(a" + v_month + v_day2 + ") a" + v_month + v_day2 + " \n";	
					}
				}
				 
			sql1+= "from (																							\n"
				 + "    select subj, year, subjnm, isonoff															\n";
				
				
				for(int i=1; i<=12; i++) {
					if(i <10) {
						v_month = "0" + i;
					} else {
						v_month = i + "";
					}
					
					v_lastdate = FormatDate.getEnddate(v_gyear + v_month + "01");
					v_day = Integer.parseInt(v_lastdate.substring(6, 8));
					for(int j=1; j <=v_day; j++ ) {
						if(j < 10) {
							v_day2 = "0" + j;
						} else {
							v_day2 = j + "";
						}
						sql1+= "		, case when " + StringManager.makeSQL(v_gyear + v_month + v_day2) + " between substr(edustart, 1, 8) and substr(eduend, 1, 8) then 'Y' else 'N' end a" + v_month + v_day2 + "	\n";
					}
				}
			
			 sql1+= "   from (																							\n"
				 + "   		select b.subj, b.year, b.subjseq, a.subjnm, a.isvisible subjvisible, b.isvisible seqvisible, b.edustart, b.eduend, b.grcode, a.isuse, b.gyear, a.isonoff	\n"
				 + "		from tz_subj a, tz_subjseq b																\n"
				 + "		where a.subj = b.subj																		\n"
				 + "	) x																						    	\n"	
				 + "    where gyear= " +SQLString.Format(v_gyear) + "         										    \n"		
				 + "	and grcode in (select grcode from TZ_GRCOMP where grcode > ' ' and comp = " + StringManager.makeSQL(v_comp) + ") \n"
				 + "    and isuse='Y'																					\n"
				 + "    and subjvisible='Y'																				\n" 
				 + "    and seqvisible='Y'																				\n"
				 + "	and length(edustart) > 7																		\n"
	             + "	and length(eduend) > 7																			\n";
			if ( v_select.equals("ON") || v_select.equals("OFF")  || v_select.equals("RC") ) { 
				sql1 += " and isonoff = " +SQLString.Format(v_select) + " \n";
			}
				sql1+= ")																								\n" 
				 + "group by subj, year																					\n"
				 + "order by subjnm                                                                                 	\n";
				sql1+= ")                                                                                               \n";
			
			ls1 = connMgr.executeQuery(sql1);
					
			while ( ls1.next() ) { 
				dbox = ls1.getDataBox();
				list.add(dbox);
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return list;
	}
	
	/**
	 * 과정체계도 조회
	 * @param box      receive from the form object and session
	 * @return ArrayList
	 */
	public ArrayList SelectEduGuideList(RequestBox box) throws Exception { 
	    DBConnectionManager	connMgr	= null;
	    ListSet ls         = null;
	    ArrayList list = null;
	    DataBox dbox        = null;
	
	    String  sql        = "";
	    //String  v_type = box.getStringDefault("p_type", "ALL");
	    String  v_upperclass = box.getStringDefault("s_upperclass", "ALL");
	    String  v_middleclass = box.getStringDefault("s_middleclass", "ALL");
	    String  v_lowerclass = box.getStringDefault("s_lowerclass", "ALL");
	    String  v_searchtext = box.getString("p_searchtext");
	    
		String  s_comp      = box.getSession("comp");
		String  v_isgoyong  = box.getStringDefault("p_isgoyong","ALL");
		
        try { 
	        connMgr = new DBConnectionManager();
	        list = new ArrayList();
	        /*if("1002".equals(s_comp)) {
	        sql = "select a.upperclass_ktf upperclass														\n"
	        	+ "    , a.middleclass_ktf middleclass	 													\n"
	        	+ "    , GET_SUBJCLASSKTFNM(a.upperclass_ktf, '000', '000') upperclassnm			\n"
	        	+ "    , GET_SUBJCLASSKTFNM(a.upperclass_ktf, a.middleclass_ktf, '000') middleclassnm	\n"
	        	+ "    , subj																\n"
	        	+ "    , subjnm																\n"
	        	+ "    , isonoff															\n"
	        	+ "    , lev																\n"
	        	+ "    , gubn 																\n"
	        	+ "    , b.codenm															\n"
	        	+ "    , uppercnt															\n"
	        	+ "    , middlecnt															\n"
	        	+ "    , nvl(a.cp,'') as cp													\n"
	        	+ "from tz_subj a, (														\n"															
	        	+ "    select code, codenm													\n"						
	        	+ "    from tz_code															\n"								
	        	+ "    where gubun = '0004'													\n"						
	        	+ ") b, (																	\n"
	        	+ "    select upperclass, count(*) uppercnt									\n"
	        	+ "	   from (																\n"
	        	+ "			select upperclass_ktf upperclass, subj											\n"			
	        	+ "    		from tz_subj x, tz_grsubj y											\n"
	        	+ "    		where x.subj = y.subjcourse                                          \n"
	        	+ "	   		and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(s_comp) + ") \n"
	        	+ "	   		and isuse = 'Y'														\n"
	        	+ "	   		and isvisible = 'Y'													\n"
	        	+ "    		group by upperclass_ktf, subj												\n"
	        	+ "    )																	\n"
	        	+ "    group by upperclass													\n"
	        	+ ") c, (																	\n"
	        	+ "    select upperclass, middleclass, count(*) middlecnt					\n"
	        	+ "    from (																\n"
	        	+ "		 	select upperclass_ktf upperclass, middleclass_ktf middleclass, subj								\n"
	        	+ "    		from tz_subj x, tz_grsubj y											\n"
	        	+ "    		where x.subj = y.subjcourse                                          \n"
	        	+ "	   		and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(s_comp) + ") \n"
	        	+ "	   		and isuse = 'Y'														\n"
	        	+ "	   		and isvisible = 'Y'													\n"
	        	+ "			group by upperclass_ktf, middleclass_ktf, subj							\n"
	        	+ "		)																	\n"
	        	+ "    group by upperclass, middleclass										\n"
	        	+ ") d, tz_grsubj e															\n"
	        	+ "where a.isonoff = b.code													\n"
	        	+ "and a.upperclass_ktf = c.upperclass											\n"
	        	+ "and a.upperclass_ktf = d.upperclass											\n"
	        	+ "and a.middleclass_ktf = d.middleclass										\n"
	        	+ "and a.subj = e.subjcourse												\n"
	        	+ "and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(s_comp) + ") \n"
	        	+ "and isuse = 'Y'															\n"
	        	+ "and isvisible = 'Y'														\n";
	        	
	        	if(!"ALL".equals(v_upperclass)) {
	        		sql += "and a.upperclass_ktf = " + StringManager.makeSQL(v_upperclass) + "        \n";
	        	}
	        	
	        sql+= "group by a.upperclass_ktf													\n"
	        	+ "    , a.middleclass_ktf														\n"														
	        	+ "    , GET_SUBJCLASSKTFNM(a.upperclass_ktf, '000', '000')						\n" 			
	        	+ "    , GET_SUBJCLASSKTFNM(a.upperclass_ktf, a.middleclass_ktf, '000')				\n"	
	        	+ "    , subj																\n"																
	        	+ "    , subjnm																\n"																
	        	+ "    , isonoff															\n"															
	        	+ "    , lev																\n"																
	        	+ "    , gubn																\n" 																
	        	+ "    , b.codenm															\n"															
	        	+ "    , uppercnt															\n"															
	        	+ "    , middlecnt															\n"
	        	+ "    , cp																	\n";		
	        sql+= "order by a.upperclass_ktf, a.middleclass_ktf, a.subjnm 							\n";
	        } else {
		        sql = "select a.upperclass														\n"
		        	+ "    , a.middleclass														\n"
		        	+ "    , a.lowerclass														\n"
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, '000', '000') upperclassnm			\n"
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, '000') middleclassnm	\n"
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass) lowerclassnm	\n"
		        	+ "    , subj																\n"
		        	+ "    , subjnm																\n"
		        	+ "    , isonoff															\n"
		        	+ "    , lev																\n"
		        	+ "    , gubn 																\n"
		        	+ "    , b.codenm															\n"
		        	+ "    , uppercnt															\n"
		        	+ "    , middlecnt															\n"
		        	+ "    , nvl(a.cp,'') as cp													\n"
		        	+ "from tz_subj a, (														\n"															
		        	+ "    select code, codenm													\n"						
		        	+ "    from tz_code															\n"								
		        	+ "    where gubun = '0004'													\n"						
		        	+ ") b, (																	\n"
		        	+ "    select upperclass, count(*) uppercnt									\n"
		        	+ "	   from (																\n"
		        	+ "			select upperclass, subj											\n"			
		        	+ "    		from tz_subj x, tz_grsubj y											\n"
		        	+ "    		where x.subj = y.subjcourse                                          \n";
		        	
		        	if(!s_comp.equals("")){
		        		sql += "	   		and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(s_comp) + ") \n";
		        	}
		        	sql += "	   		and isuse = 'Y'														\n"
		        	+ "	   		and isvisible = 'Y'													\n"
		        	+ "    		group by upperclass, subj												\n"
		        	+ "    )																	\n"
		        	+ "    group by upperclass													\n"
		        	+ ") c, (																	\n"
		        	+ "    select upperclass, middleclass, count(*) middlecnt					\n"
		        	+ "    from (																\n"
		        	+ "		 	select upperclass, middleclass, subj								\n"
		        	+ "    		from tz_subj x, tz_grsubj y											\n"
		        	+ "    		where x.subj = y.subjcourse                                          \n";
		        	if(!s_comp.equals("")){
		        		sql += "	   		and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(s_comp) + ") \n";
		        	}
		        	sql += "	   		and isuse = 'Y'														\n"
		        	+ "	   		and isvisible = 'Y'													\n"
		        	+ "			group by upperclass, middleclass, subj							\n"
		        	+ "		)																	\n"
		        	+ "    group by upperclass, middleclass	,subj									\n"
		        	+ ") d, tz_grsubj e															\n"
		        	+ "where a.isonoff = b.code													\n"
		        	+ "and a.upperclass = c.upperclass											\n"
		        	+ "and a.upperclass = d.upperclass											\n"
		        	//+ "and a.middleclass = d.middleclass										\n"
		        	+ "and a.subj = e.subjcourse												\n";
		        	if(!s_comp.equals("")){
		        		sql += "and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(s_comp) + ") \n";
		        	}
		        	sql += "and isuse = 'Y'															\n"
		        	+ "and isvisible = 'Y'														\n";
		        	
		        if(!"ALL".equals(v_upperclass)) {
	        		sql += "and a.upperclass = " + StringManager.makeSQL(v_upperclass) + "        \n";
	        	}
		        if(!"ALL".equals(v_upperclass) && !"ALL".equals(v_middleclass)) {
	        		sql += "and a.middleclass = " + StringManager.makeSQL(v_middleclass) + "        \n";
	        	}
		        if(!"ALL".equals(v_upperclass) && !"ALL".equals(v_middleclass) && !"ALL".equals(v_lowerclass)) {
	        		sql += "and a.lowerclass = " + StringManager.makeSQL(v_lowerclass) + "        \n";
	        	}
		        if(!v_searchtext.equals("")){
		        	sql += "and a.subjnm like '%" + v_searchtext +"%'        \n";
		        }
		        	
		        sql+= "group by a.upperclass													\n"
		        	+ "    , a.middleclass														\n"														
		        	+ "    , a.lowerclass														\n"														
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, '000', '000')						\n" 			
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, '000')				\n"	
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass)				\n"	
		        	+ "    , subj																\n"																
		        	+ "    , subjnm																\n"																
		        	+ "    , isonoff															\n"															
		        	+ "    , lev																\n"																
		        	+ "    , gubn																\n" 																
		        	+ "    , b.codenm															\n"															
		        	+ "    , uppercnt															\n"															
		        	+ "    , middlecnt															\n"		
		        	+ "    , cp																	\n";		
		        sql+= "order by a.upperclass, a.middleclass, a.lowerclass, a.subjnm 							\n";
		        
	        }*/
	        
	        sql = "select a.upperclass														\n"
	        	+ "    , a.middleclass														\n"
	        	+ "    , a.lowerclass														\n"
	        	+ "    , GET_SUBJCLASSNM(a.upperclass, '000', '000') upperclassnm			\n"
	        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, '000') middleclassnm	\n"
	        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass) lowerclassnm	\n"
	        	+ "    , subj																\n"
	        	+ "    , subjnm																\n"
	        	+ "    , isonoff															\n"
	        	+ "    , lev																\n"
	        	+ "    , gubn 																\n"
	        	+ "    , b.codenm															\n"
	        	+ "    , nvl(a.cp,'') as cp													\n"
	        	+ "    , a.isgoyong													\n"
	        	+ "from tz_subj a, (														\n"															
	        	+ "    select code, codenm													\n"						
	        	+ "    from tz_code															\n"								
	        	+ "    where gubun = '0004'													\n"						
	        	+ ") b, tz_grsubj e															\n"
	        	+ "where a.isonoff = b.code													\n"
	        	+ "and a.subj = e.subjcourse												\n";
	        	if(!s_comp.equals("")){
	        		sql += "and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(s_comp) + ") \n";
	        	}
	        	sql += "and isuse = 'Y'															\n"
	        	+ "and isvisible = 'Y'														\n";
	        	
	        if(!"ALL".equals(v_upperclass)) {
        		sql += "and a.upperclass = " + StringManager.makeSQL(v_upperclass) + "        \n";
        	}
	        if(!"ALL".equals(v_upperclass) && !"ALL".equals(v_middleclass)) {
        		sql += "and a.middleclass = " + StringManager.makeSQL(v_middleclass) + "        \n";
        	}
	        if(!"ALL".equals(v_upperclass) && !"ALL".equals(v_middleclass) && !"ALL".equals(v_lowerclass)) {
        		sql += "and a.lowerclass = " + StringManager.makeSQL(v_lowerclass) + "        \n";
        	}
//	        if(!v_searchtext.equals("")){
//	        	sql += "and a.subjnm like '%" + v_searchtext +"%'        \n";
//	        }
	        if ( !v_searchtext.equals("") ) { 
            	v_searchtext = v_searchtext.replaceAll("'", "");
            	v_searchtext = v_searchtext.replaceAll("/", "//");
            	v_searchtext = v_searchtext.replaceAll("%", "/%").toUpperCase();
            	sql += "\n and    upper(a.subjnm) like " +SQLString.Format("%"+v_searchtext+"%") + " escape '/' ";
            }
	        if(!v_isgoyong.equals("ALL")){
	        	sql += "and a.isgoyong = " + StringManager.makeSQL(v_isgoyong) + "       \n";
	        }
	        	
	        sql+= "group by a.upperclass													\n"
	        	+ "    , a.middleclass														\n"														
	        	+ "    , a.lowerclass														\n"														
	        	+ "    , GET_SUBJCLASSNM(a.upperclass, '000', '000')						\n" 			
	        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, '000')				\n"	
	        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass)				\n"	
	        	+ "    , subj																\n"																
	        	+ "    , subjnm																\n"																
	        	+ "    , isonoff															\n"															
	        	+ "    , lev																\n"																
	        	+ "    , gubn																\n" 																
	        	+ "    , b.codenm															\n"															
	        	+ "    , cp																	\n"		
	            + "    , a.isgoyong															\n";		
	        sql+= "order by a.upperclass, a.middleclass, a.lowerclass, a.subjnm ";						
	        
	        ls = connMgr.executeQuery(sql);
	        
	        while ( ls.next() ) {  
	            dbox = ls.getDataBox();
	            list.add(dbox);
	        }
	    } catch ( Exception ex ) { 
	        ErrorManager.getErrorStackTrace(ex, box, sql);
	        throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
	    } finally { 
	        if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
	        if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	    }
	
	    return list;
	}

    /**
     * 코드 가져오기
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList codeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list = null;
        DataBox dbox        = null;

        String  sql        = "";
        String  v_co_cd = box.getString("p_co_cd");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = "select cd, cd_nm				\n"
            	+ "  from tk_edu000t			\n"
            	+ " where co_cd = " + StringManager.makeSQL(v_co_cd) + " \n"
            	+ "   and cd <> '5'				\n" 
            	+ " order by cd desc         	\n"; 

            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
            ls.close();
            sql = "select cd, cd_nm				\n"
            	+ "  from tk_edu000t			\n"
            	+ " where co_cd = " + StringManager.makeSQL(v_co_cd) + " \n"
            	+ "   and cd = '5'				\n" ;

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
            ls.close();
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	/**
     * 대분류 코드 가져오기
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList upperclassList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list = null;
        DataBox dbox        = null;

        String  sql        = "";
        
    	String  v_comp      = box.getSession("comp");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
        	
            sql = "select upperclass			\n"
            	+ "    , classname				\n"
            	+ "from tz_subjatt				\n"
            	+ "where middleclass = '000'	\n"
            	+ "and lowerclass = '000'       \n" 
            	+ "order by upperclass          \n";            
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	 /**
    주민등록번호 저장
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int Savebirth_date(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 0;
        
        String              s_userid    = box.getSession("userid"   );
        String				v_birth_date     = box.getString("p_birth_date1") + box.getString("p_birth_date2");          
        
        try { 
            connMgr = new DBConnectionManager();
            sbSQL.append(" select userid from tz_member where birth_date = fn_crypt('1', " + StringManager.makeSQL(v_birth_date) + ", 'knise') and userid != " + StringManager.makeSQL(s_userid) + " and isretire = 'N'    \n");
            
            ls       = connMgr.executeQuery(sbSQL.toString());
           
            if ( !ls.next() ) { 
                ls.close();
                
                sbSQL.setLength(0);
                
                sbSQL.append("update tz_member set birth_date = fn_crypt('1', ?, 'knise'), ldate = to_char(sysdate, 'yyyymmddhh24miss')	\n")
                     .append("where userid = ?                                 								\n");
                     
                pstmt   = connMgr.prepareStatement(sbSQL.toString());
                
                pstmt.setString(1, v_birth_date        );
                pstmt.setString(2, s_userid       );
                
                isOk     = pstmt.executeUpdate();
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }
    
    /**
    도서 선택 및 배송지 정보 등록
    @param box      receive from the form object and session
    @return isOk   
    */
    public int UpdateRCInfo(RequestBox box) throws Exception {
    		DBConnectionManager    connMgr                     = null;
         PreparedStatement      pstmt                       = null;
         String                 sql                         = "";
         int                    isOk                        = 0;
         ListSet ls = null;
         
         String ss_userid = box.getSession("userid");
         try {
         	   connMgr    = new DBConnectionManager();
         	   connMgr.setAutoCommit(false);
         	   
         	  sql = " SELECT USERID FROM TZ_PROPOSEBOOK       		                     	 \n"
         		  + " WHERE SUBJ = " + StringManager.makeSQL(box.getString("p_subj")) + " 	 \n"
         		  + " AND YEAR = " + StringManager.makeSQL(box.getString("p_year")) + "   	 \n"
         		  + " AND SUBJSEQ = " + StringManager.makeSQL(box.getString("p_subjseq")) + " \n"
         		  + " AND USERID = " + StringManager.makeSQL(ss_userid) + "                  \n"
         	  	  + " GROUP BY SUBJ, YEAR ,SUBJSEQ, USERID                                   \n";
         	  
         	 ls =  connMgr.executeQuery(sql);
                   
         	 if(!ls.next()) {  
         		int v_bookcnt = box.getInt("p_bookcnt");
              	for(int i=1; i <= v_bookcnt; i++) {
              		int v_bookcode= box.getInt("p_" + i + "_radio");
              		// INSERT tz_proposesubj table
                    	sql = " INSERT INTO TZ_PROPOSEBOOK                                   \n"
                            + " (                                                            \n"
                            + "         subj                                                 \n"
                            + "     ,   year                                                 \n"
                            + "     ,   subjseq                                              \n"
                            + "     ,   userid                                               \n"
                            + "     ,   month                                                \n"
                            + "     ,   bookcode                                             \n"
                            + "     ,   status                                               \n"
                            + " ) VALUES (                                                   \n"
                            + "         ?                                                    \n"
                            + "     ,   ?                                                    \n"
                            + "     ,   ?                                                    \n"
                            + "     ,   ?                                                    \n"
                            + "     ,   ?                                                    \n"
                            + "     ,   ?                                                    \n"
                            + "     ,   ?                                                    \n"
                            + " )                                                            \n";
                             
                        pstmt   = connMgr.prepareStatement(sql);
                        
                        pstmt.setString(1, box.getString("p_subj"));
                        pstmt.setString(2, box.getString("p_year"));
                        pstmt.setString(3, box.getString("p_subjseq"));
                        pstmt.setString(4, ss_userid);
                        pstmt.setInt(5, i);
                        pstmt.setInt(6, v_bookcode);
                        pstmt.setString(7, "B");
                        isOk = pstmt.executeUpdate();
                        
                        if ( pstmt != null ) pstmt.close(); 
              	}
         	 }


         	   sql = "delete from TZ_DELIVERY   		\n"
         		   + "where   subj    = ?      			\n"
         		   + "and     year    = ?      			\n"
         		   + "and     subjseq = ?      			\n"
         		   + "and     userid  = ?      			\n";
	                    
	               pstmt   = connMgr.prepareStatement(sql);
	               pstmt.setString(1, box.getString("p_subj"));
                pstmt.setString(2, box.getString("p_year"));
                pstmt.setString(3, box.getString("p_subjseq"));
                pstmt.setString(4, ss_userid);
                isOk   = pstmt.executeUpdate();
                
                if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

    	        sql = "insert into tz_delivery( "
    	        	+ "       subj "
    	        	+ "     , year "
    	        	+ "     , subjseq "
    	        	+ "     , userid "
    	        	+ "     , delivery_post1 "
    	        	+ "     , delivery_post2 "
    	        	+ "     , delivery_address1 "
    	        	+ "     , delivery_handphone "
    	        	+ "     , luserid "
    	        	+ "     , ldate "
    	        	+ "      ) "
    	            + "values(? "
    	            + "     , ? "
    	            + "     , ? "
    	            + "     , ? "
    	            + "     , ? "
    	            + "     , ? "
    	            + "     , ? "
    	            + "     , ? "
    	            + "     , ? "
    	            + "     , to_char(sysdate, 'yyyymmddhh24miss') "
    	            + "      ) ";

                pstmt = connMgr.prepareStatement(sql);
                pstmt.setString(1, box.getString("p_subj"));
                pstmt.setString(2, box.getString("p_year"));
                pstmt.setString(3, box.getString("p_subjseq"));
                pstmt.setString(4, ss_userid);
                pstmt.setString(5, box.getString("p_post1"));
                pstmt.setString(6, box.getString("p_post2"));
                pstmt.setString(7, box.getString("p_address1"));
                pstmt.setString(8, box.getString("p_delivery_handphone"));
                pstmt.setString(9, ss_userid);
                isOk   = pstmt.executeUpdate();
                
                if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
                
                /*
    	        sql = "update tz_member "
    	        	+ "       set address = ?, zip_cd = ?, handphone = ? "
    	            + "     where userid = ? ";
    	        
                pstmt = connMgr.prepareStatement(sql);
                pstmt.setString(1, box.getString("p_address1"));
                pstmt.setString(2, box.getString("p_post1")+"-"+box.getString("p_post2"));
                pstmt.setString(3, box.getString("p_delivery_handphone"));
                pstmt.setString(4, ss_userid);
                isOk   = pstmt.executeUpdate();
                
                if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
                */
                
                if ( isOk > 0) { 
                    if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e ) { } }
                } else { 
                    if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e ) { } }
                }
             
         } catch(Exception ex) {
   			isOk = 0;
   			if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
   			ErrorManager.getErrorStackTrace(ex, box, sql);
   			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
   		}
   		finally {
   			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
   			
   			 if ( connMgr != null ) { 
                  try { 
                      connMgr.freeConnection(); 
                  } catch ( Exception e ) { } 
              }
   		}
   		return isOk;
    	}	
    
    /**
    월간 교육 일정 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectEducationMonthlyList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1             = null;
        ArrayList list          = null;
        String sql1             = "";
        DataBox dbox = null;
        String  v_gyear         = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
        String  v_selmonth      = box.getStringDefault("p_month",FormatDate.getDate("MM") );

        String  v_select        = box.getStringDefault("p_select", "TOTAL");
        String  v_condition     = v_gyear + v_selmonth;
        
        String ss_comp = box.getSession("comp");
        
        StringBuffer        sbSQL       = new StringBuffer("");
        int v_cnt = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql1 = "select distinct x.subj, x.subjnm, x.subjseq, x.edustart, x.eduend, x.year, x.isonoff						\n"
            	+ "      , day1, day2, day3, day4, day5, day6, day7, day8, day9, day10											\n"
            	+ "      , day11, day12, day13, day14, day15, day16, day17, day18, day19, day20									\n"
            	+ "      , day21, day22, day23, day24, day25, day26, day27, day28, day29, day30, day31							\n"
            	+ "from (																										\n"
            	+ "	select a.subj, a.subjnm, a.subjseq, a.edustart, a.eduend, a.year, a.isonoff									\n"
//            	+ "			, case when '01' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day1	\n"
//            	+ "			, case when '02' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day2	\n"
//            	+ "			, case when '03' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day3	\n"
//            	+ "			, case when '04' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day4	\n"
//            	+ "			, case when '05' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day5	\n"
//            	+ "			, case when '06' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day6	\n"
//            	+ "			, case when '07' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day7	\n"
//            	+ "			, case when '08' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day8	\n"
//            	+ "			, case when '09' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day9	\n"
//            	+ "			, case when '10' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day10	\n"
//            	+ "			, case when '11' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day11	\n"
//            	+ "			, case when '12' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day12	\n"
//            	+ "			, case when '13' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day13	\n"
//            	+ "			, case when '14' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day14	\n"
//            	+ "			, case when '15' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day15	\n"
//            	+ "			, case when '16' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day16	\n"
//            	+ "			, case when '17' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day17	\n"
//            	+ "			, case when '18' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day18	\n"
//            	+ "			, case when '19' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day19	\n"
//            	+ "			, case when '20' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day20	\n"
//            	+ "			, case when '21' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day21	\n"
//            	+ "			, case when '22' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day22	\n"
//            	+ "			, case when '23' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day23	\n"
//            	+ "			, case when '24' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day24	\n"
//            	+ "			, case when '25' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day25	\n"
//            	+ "			, case when '26' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day26	\n"
//            	+ "			, case when '27' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day27	\n"
//            	+ "			, case when '28' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day28	\n"
//            	+ "			, case when '29' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day29	\n"
//            	+ "			, case when '30' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day30	\n"
//            	+ "			, case when '31' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day31	\n"
            	+ "         , case when substr(edustart,1,6) ||'01' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day1 \n"
            	+ "         , case when substr(edustart,1,6) ||'02' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day2 \n"
            	+ "         , case when substr(edustart,1,6) ||'03' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day3 \n"
            	+ "         , case when substr(edustart,1,6) ||'04' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day4 \n"
            	+ "         , case when substr(edustart,1,6) ||'05' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day5 \n"
            	+ "         , case when substr(edustart,1,6) ||'06' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day6 \n"
            	+ "         , case when substr(edustart,1,6) ||'07' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day7 \n"
            	+ "         , case when substr(edustart,1,6) ||'08' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day8 \n"
            	+ "         , case when substr(edustart,1,6) ||'09' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day9 \n"
            	+ "         , case when substr(edustart,1,6) ||'10' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day10 \n"
            	+ "         , case when substr(edustart,1,6) ||'11' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day11 \n"
            	+ "         , case when substr(edustart,1,6) ||'12' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day12 \n"
            	+ "         , case when substr(edustart,1,6) ||'13' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day13 \n"
            	+ "         , case when substr(edustart,1,6) ||'14' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day14 \n"
            	+ "         , case when substr(edustart,1,6) ||'15' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day15 \n"
            	+ "         , case when substr(edustart,1,6) ||'16' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day16 \n"
            	+ "         , case when substr(edustart,1,6) ||'17' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day17 \n"
            	+ "         , case when substr(edustart,1,6) ||'18' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day18 \n"
            	+ "         , case when substr(edustart,1,6) ||'19' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day19 \n"
            	+ "         , case when substr(edustart,1,6) ||'20' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day20 \n"
            	+ "         , case when substr(edustart,1,6) ||'21' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day21 \n"
            	+ "         , case when substr(edustart,1,6) ||'22' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day22 \n"
            	+ "         , case when substr(edustart,1,6) ||'23' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day23 \n"
            	+ "         , case when substr(edustart,1,6) ||'24' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day24 \n"
            	+ "         , case when substr(edustart,1,6) ||'25' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day25 \n"
            	+ "         , case when substr(edustart,1,6) ||'26' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day26 \n"
            	+ "         , case when substr(edustart,1,6) ||'27' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day27 \n"
            	+ "         , case when substr(edustart,1,6) ||'28' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day28 \n"
            	+ "         , case when substr(edustart,1,6) ||'29' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day29 \n"
            	+ "         , case when substr(edustart,1,6) ||'30' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day30 \n"
            	+ "         , case when substr(edustart,1,6) ||'31' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day31 \n"
            	+ "		from vz_scsubjseq a inner join tz_subj b	\n"
            	+ "     on a.subj = b.subj                          \n"
            	+ "     left outer join tz_grcomp c					\n"
            	+ "     on a.grcode = c.grcode						\n"
            	+ "		where a.isuse = 'Y'							\n"
            	+ "		and a.subjvisible = 'Y'						\n"
            	+ "		and a.seqvisible = 'Y'						\n"
            	+ "		and length(a.edustart) > 7					\n"
            	+ "		and length(a.eduend) > 7					\n"
            	+ "     and ((edustart between substr(edustart,1,6) || '01' and substr(edustart,1,6) || '31')	or ( eduend between substr(edustart,1,6) || '01' and substr(edustart,1,6) || '31' 	)) \n"
            	+ "		and a.grcode in (select grcode from TZ_GRCOMP where comp =  " + StringManager.makeSQL(ss_comp) + ")		\n"
            	+ "		and a.gyear = " +SQLString.Format(v_gyear) + "												\n"
            	+ "		and ((" +SQLString.Format(v_condition) + " = SUBSTR(edustart,1,6)) or (" +SQLString.Format(v_condition) + " = SUBSTR(eduend,1,6) || '31'))		\n";
            	if(!"TOTAL".equals(v_select)) {
            		sql1+= " 	and b.isonoff = " +SQLString.Format(v_select) + "											\n";
            	}
            	sql1+= ") x																								    \n"
                + "where 1 = 1                                                                                              \n";
            	sql1 += "order by subjnm, subjseq                                                                           \n";
            	
            ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
            		dbox = ls1.getDataBox();
            		list.add(dbox);
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

	/**
     * 학습후기 등록
     * @param box
     * @return
     * @throws Exception
     */
	public int insertStoldComments(RequestBox box) throws Exception {
    	 DBConnectionManager connMgr = null;
    	 PreparedStatement   pstmt   = null;
    	 String sql = "";
    	 int isOk = 0;
    	 int pidx = 1;
	        
    	 String ss_userid = box.getSession("userid");
    	 String v_subj = box.getString("p_subj");
    	 String v_year = box.getStringDefault("p_year","0000");
    	 String v_subjseq = box.getStringDefault("p_subjseq","0000");
    	 String v_comments = box.getString("p_comments");
    	 
    	 try {
    		 connMgr    = new DBConnectionManager();
    		 connMgr.setAutoCommit(false);
          	   
    		 sql = "insert into tz_stold_comments "
    			 + "      ( "
    			 + "        seq "
    			 + "      , subj "
    			 + "      , year "
    			 + "      , subjseq "
    			 + "      , userid "
    			 + "      , comments "
    			 + "      , ldate "
    			 + "      , hidden_yn "
    			 + "       ) "
    			 + "values ( "
    			 + "        (select nvl(max(seq),0)+1 from tz_stold_comments) "
    			 + "      , ? "
    			 + "      , ? "
    			 + "      , ? "
    			 + "      , ? "
    			 + "      , ? "
    			 + "      , to_char(sysdate,'yyyymmddhh24miss') "
    			 + "      , 'Y' ) ";
	         pstmt   = connMgr.prepareStatement(sql);
	         
	         pstmt.setString(pidx++, v_subj);
	         pstmt.setString(pidx++, v_year);
	         pstmt.setString(pidx++, v_subjseq);
	         pstmt.setString(pidx++, ss_userid);
	         pstmt.setString(pidx++, v_comments);
	         isOk = pstmt.executeUpdate();
	         
	         if ( pstmt != null ) pstmt.close(); 
	
	         if ( isOk > 0) { 
	             if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e ) { } }
	         } else { 
	        	 if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e ) { } }
	         }
	      
    	 } catch(Exception ex) {
    		 isOk = 0;
    		 if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
    		 ErrorManager.getErrorStackTrace(ex, box, sql);
    		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	 } finally {
    		 if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			
			 if ( connMgr != null ) { 
				 try { 
					 connMgr.freeConnection(); 
				 } catch ( Exception e ) { } 
			 }
		}
		return isOk;
	}	

	/**
     * 학습후기 삭제
     * @param box
     * @return
     * @throws Exception
     */
	public int deleteStoldComments(RequestBox box) throws Exception {
    	 DBConnectionManager connMgr = null;
    	 PreparedStatement   pstmt   = null;
    	 String sql = "";
    	 int isOk = 0;
          
    	 String ss_userid = box.getSession("userid");
    	 try {
    		 connMgr    = new DBConnectionManager();
    		 connMgr.setAutoCommit(false);
          	   
    		 sql = "delete from tz_stold_comments "
    			 + "where  seq = ? "
    			 + "and    userid = " + SQLString.Format(ss_userid);
    		 
	         pstmt   = connMgr.prepareStatement(sql);
	         
	         pstmt.setInt(1, box.getInt("p_seq"));
	         isOk = pstmt.executeUpdate();
	         
	         if ( pstmt != null ) pstmt.close(); 
	
	         if ( isOk > 0) { 
	             if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e ) { } }
	         } else { 
	             if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e ) { } }
	         }
	      
	  } catch(Exception ex) {
			isOk = 0;
			if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			
			 if ( connMgr != null ) { 
	           try { 
	               connMgr.freeConnection(); 
	           } catch ( Exception e ) { } 
	       }
		}
		return isOk;
	}	
	
    /**
     * 과정별 직무 리스트
     * @param box
     * @return
     * @throws Exception 
     */
	public ArrayList SelectSubjectJikmuList(RequestBox box) throws Exception {
		DBConnectionManager connMgr         = null;
		ListSet             ls              = null;
		ArrayList           list            = null;
		String 			   sql 			   = "";
		DataBox             dbox            = null;

		String v_subj = box.getString("p_subj");
		String v_eduyear = box.getStringDefault("p_eduyear",FormatDate.getDate("yyyy"));

		try { 
			connMgr     = new DBConnectionManager();
			list        = new ArrayList();

			sql = "\n select a.job_cd, b.jobnm "
				+ "\n from   tz_subjjikmu a, tz_jikmu b "
				+ "\n where  a.job_cd =  b.job_cd "
				+ "\n and    b.isdeleted = 'N' "
				+ "\n and    a.year = " + SQLString.Format(v_eduyear)
				+ "\n and    a.subj = " + SQLString.Format(v_subj)
				+ "\n order  by b.job_cd ";
			ls = connMgr.executeQuery(sql);

			while (ls.next()) {
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		} catch ( Exception e ) { 
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
	
	/**
	 * 중분류 목록 불러오기
	 * @param upperclass
	 * @return
	 * @throws Exception
	 */
	public static ArrayList getMiddleClass(String upperclass) throws Exception { 
		DBConnectionManager		connMgr	= null;
        ListSet 				ls = null;
        ArrayList 				list = null;
        DataBox 				dbox = null;

        String  sql        = "";
        
        StringBuffer strSQL = new StringBuffer();

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
        	if(upperclass.equals("ALL")){
        		strSQL.append("select upperclass, ") ;
        		strSQL.append("       middleclass, ") ;
        		strSQL.append("       lowerclass , ") ;
        		strSQL.append("       classname ") ;
        		strSQL.append(" from tz_subjatt ") ;
        		strSQL.append(" where middleclass <> '000' ") ;
        		strSQL.append("   and lowerclass = '000' ") ;
        		strSQL.append("   and upperclass in ('A01','A02','A04') ") ;
        		strSQL.append(" union all ") ;
        		strSQL.append("select upperclass, ") ;
        		strSQL.append("       middleclass, ") ;
        		strSQL.append("       lowerclass , ") ;
        		strSQL.append("       classname ") ;
        		strSQL.append("  from tz_subjatt ") ;
        		strSQL.append(" where middleclass = '000' ") ;
        		strSQL.append("   and lowerclass = '000' ") ;
        		strSQL.append("   and upperclass in ('A03') ") ;
        		strSQL.append("  order by upperclass, middleclass") ;

        	}
        	else{
	            strSQL.append("select upperclass, ") ;
	            strSQL.append("       middleclass, ") ;
	            strSQL.append("       lowerclass , ") ;
	            strSQL.append("       classname ") ;
	            strSQL.append("  from tz_subjatt ") ;
	           	strSQL.append(" where upperclass = "+SQLString.Format(upperclass)) ;
	            strSQL.append("   and middleclass <> '000' ") ;
	            strSQL.append("   and lowerclass = '000' ") ;
	            strSQL.append(" order by upperclass , middleclass ") ;
        	}            
            
            ls = connMgr.executeQuery(strSQL.toString());
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, strSQL.toString());
            throw new Exception("sql1 = " + strSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
	}
	
	/**
	 * 소분류 목록 불러오기
	 * @param upperclass
	 * @param middleclass
	 * @return
	 * @throws Exception
	 */
	public static ArrayList getLowerClass(String upperclass, String middleclass) throws Exception { 
		DBConnectionManager		connMgr	= null;
        ListSet 				ls = null;
        ArrayList 				list = null;
        DataBox 				dbox = null;

        StringBuffer strSQL = new StringBuffer();
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            strSQL.append("select upperclass, ") ;
            strSQL.append("       middleclass, ") ;
            strSQL.append("       lowerclass , ") ;
            strSQL.append("       classname ") ;
            strSQL.append("  from tz_subjatt ") ;
            strSQL.append(" where upperclass = "+SQLString.Format(upperclass)) ;
            strSQL.append("   and middleclass <> '000' ") ;
            if(!upperclass.equals("A03")){
            	strSQL.append("   and middleclass = "+SQLString.Format(middleclass)) ;
            	strSQL.append("   and lowerclass <> '000' ") ;
            }
            strSQL.append(" order by upperclass , middleclass, lowerclass ") ;
            
            ls = connMgr.executeQuery(strSQL.toString());
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, strSQL.toString());
            throw new Exception("sql1 = " + strSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
	}
	
	/**
	 * 과정체계도 조회
	 * @param box      receive from the form object and session
	 * @return ArrayList
	 */
	public ArrayList SelectEduGuideList1(RequestBox box) throws Exception { 
	    DBConnectionManager	connMgr	= null;
	    ListSet ls         = null;
	    ArrayList list = null;
	    DataBox dbox        = null;
	
	    
	    String  sql        = "";
	    //String  v_type = box.getStringDefault("p_type", "ALL");
	    String  v_upperclass = box.getStringDefault("p_upperclass", "ALL");
	    String  v_middleclass = box.getStringDefault("p_middleclass", "ALL");
	    String  v_lowerclass = box.getStringDefault("p_lowerclass", "ALL");

		String  s_comp      = box.getSession("comp");
		
        try { 
	        connMgr = new DBConnectionManager();
	        list = new ArrayList();

	        sql = "select a.upperclass														\n"
		        	+ "    , a.middleclass														\n"
		        	+ "    , a.lowerclass														\n"
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, '000', '000') upperclassnm			\n"
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, '000') middleclassnm	\n"
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass) lowerclassnm	\n"
		        	+ "    , subj																\n"
		        	+ "    , subjnm																\n"
		        	//+ "    , lengthb(subjnm) s																\n"
		        	//+ "    , (case when lengthb(subjnm) < 40 then subjnm else substrb(subjnm, 0, 39)|| '...' end) aaa																\n"
		        	+ "    , isonoff															\n"
		        	+ "    , (select cd_nm from tk_edu000t a where co_cd = '013' and a.cd = lev) levnm																\n"
		        	+ "    , lev																\n"
		        	+ "    , gubn 																\n"
		        	+ "    , b.codenm															\n"
		        	+ "    , nvl(a.cp,'') as cp													\n"
		        	+ "from tz_subj a, (														\n"															
		        	+ "    select code, codenm													\n"						
		        	+ "    from tz_code															\n"								
		        	+ "    where gubun = '0004'													\n"						
		        	+ ") b, tz_grsubj e															\n"
		        	+ "where a.isonoff = b.code													\n"
		        	+ "and a.subj = e.subjcourse												\n";
		        	if(!s_comp.equals("")){
		        		sql += "and grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(s_comp) + ") \n";
		        	}
		        	sql += "and isuse = 'Y'															\n"
		        	+ "and isvisible = 'Y'														\n";
		        	
		        if(!"ALL".equals(v_upperclass)) {
	        		sql += "and a.upperclass = " + StringManager.makeSQL(v_upperclass) + "        \n";
	        	}
		        if(!"ALL".equals(v_upperclass) && !"ALL".equals(v_middleclass)) {
	        		sql += "and a.middleclass = " + StringManager.makeSQL(v_middleclass) + "        \n";
	        	}
		        if(!"ALL".equals(v_upperclass) && !"ALL".equals(v_middleclass) && !"ALL".equals(v_lowerclass)) {
	        		sql += "and a.lowerclass = " + StringManager.makeSQL(v_lowerclass) + "        \n";
	        	}
		        	
		        sql+= "group by a.upperclass													\n"
		        	+ "    , a.middleclass														\n"														
		        	+ "    , a.lowerclass														\n"														
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, '000', '000')						\n" 			
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, '000')				\n"	
		        	+ "    , GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass)				\n"	
		        	+ "    , subj																\n"																
		        	+ "    , subjnm																\n"																
		        	+ "    , isonoff															\n"															
		        	+ "    , lev																\n"																
		        	+ "    , gubn																\n" 																
		        	+ "    , b.codenm															\n"															
		        	+ "    , cp																	\n";		
		        sql+= "order by a.lev desc                               							\n";
		        
	        ls = connMgr.executeQuery(sql);
	        
	        while ( ls.next() ) {  
	            dbox = ls.getDataBox();
	            list.add(dbox);
	        }
	    } catch ( Exception ex ) { 
	        ErrorManager.getErrorStackTrace(ex, box, sql);
	        throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
	    } finally { 
	        if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
	        if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	    }
	
	    return list;
	}
	


}