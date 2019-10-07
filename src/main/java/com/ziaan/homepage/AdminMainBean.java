// **********************************************************
//  1. 제      목: 운영자 메인
//  2. 프로그램: AdminMainBean.java
//  3. 개      요: 운영자 메인
//  4. 환      경: JDK 1.5
//  5. 버      젼: 1.0
//  6. 작      성: 2008.12.17
//  7. 수      정:
// **********************************************************
package com.ziaan.homepage;
import java.util.ArrayList;

import com.ziaan.course.GrcompBean;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.library.FormatDate;

public class AdminMainBean { 
    private ConfigSet config;
    private int row;
    
    public AdminMainBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    /**
     * 운영자에게 
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList toAdminList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select a.title, a.indate, a.name, a.tabseq, a.seq				\n"
            	+ "from tz_board a, (												\n"
            	+ "		select max(seq) seq, refseq, max(indate) indate				\n"
            	+ "		from tz_board												\n"												
            	+ "		where tabseq = 2											\n"
            	+ "    	and levels > 1												\n"													
            	+ "	    group by refseq												\n"												
            	+ ") b																\n"																
            	+ "where a.tabseq = 2												\n"
            	+ "and a.levels = 1													\n"													
            	+ "and a.seq = b.refseq												\n";	
            
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
     * 강사활동관리 
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList toturActionList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select tuserid, name, lglast, subjcnt, studentcnt, noanswercnt, noscorecnt								\n"
            	+ "from (																									\n"
            	+ "    select max(a.lglast) lglast, b.tuserid, max(a.name) name, count(c.subj) subjcnt, sum(studentcnt) studentcnt,	\n" 
            	+ "			nvl(sum(noanswercnt), 0) noanswercnt, nvl(sum(noscorecnt), 0) noscorecnt										\n"
            	+ "    from tz_member a, tz_classtutor b, tz_subjseq c, (																	\n"
            	+ "        select subj, year, subjseq, count(userid) studentcnt																\n"
            	+ "        from tz_student																									\n"
            	+ "        group by subj, year, subjseq																						\n"
            	+ "    ) d, (																												\n"
            	+ "        select x.subj, x.userid, nvl(y.cnt, 0) noanswercnt																\n"				
            	+ "        from (																											\n"												
            	+ "				select a.tabseq, b.seq, b.levels, a.subj, b.title, b.indate, b.userid										\n"						
            	+ "				from tz_bds a, tz_board b																					\n"										
            	+ "				where a.tabseq = b.tabseq																					\n"										
            	+ "				and a.type = 'SQ'																							\n"											
            	+ "			) x inner join tz_subj z																						\n"											
            	+ "			on x.subj = z.subj																								\n"											
            	+ "			inner join tz_classtutor k																						\n"										
            	+ "			on x.subj = k.subj																								\n"
            	+ "			left outer join (																								\n"											
            	+ "				select tabseq, refseq, count(*) cnt																			\n"									
            	+ "				from tz_board																								\n"					
            	+ "				where levels > 1																							\n"
            	+ "     		group by tabseq, refseq																						\n"										
            	+ "			) y																												\n"													
            	+ "			on x.tabseq = y.tabseq																							\n"											
            	+ "			and x.seq = y.refseq																							\n"											
            	+ "			where x.levels = 1																								\n"											
            	+ "			and nvl(y.cnt, 0) = 0																							\n"	
            	+ "		) e, 																												\n"
            	+ "	   (																													\n"													
            	+ "			select subj, year, subjseq, count(*) noscorecnt																	\n"								
            	+ "			from tz_projassign																								\n"											
            	+ "			where repdate is not null																						\n"										
            	+ "			and totalscore is null																							\n"											
            	+ "			group by subj, year, subjseq																					\n"
            	+ "    ) f																													\n"	 
            	+ "		where a.userid = b.tuserid																							\n"
            	+ "		and b.subj = c.subj																									\n"											
            	+ "		and b.year= c.year																									\n"												
            	+ "		and b.subjseq= c.subjseq																							\n"											
            	+ "		and to_char(sysdate, 'yyyyMMddhh24mi') >= edustart																	\n"		
            	+ "		and c.isclosed = 'N'																								\n"			
            	+ "		and c.subj = d.subj																									\n"
            	+ "		and c.year= d.year																									\n"
            	+ "		and c.subjseq= d.subjseq																							\n"
            	+ "		and b.subj = e.subj(+)																								\n"
            	+ "		and b.tuserid = e.userid(+)																							\n"
            	+ "		and b.subj = f.subj(+)																								\n"
            	+ "		and b.year = f.year(+)																								\n"
            	+ "		and b.subjseq= f.subjseq(+)																							\n";
            	if("F1".equals(s_gadmin)) {
            		sql += "and c.muserid = " +StringManager.makeSQL(s_userid) + "                                                          \n";
            	}
            sql+= "		group by b.tuserid																									\n"            
            	+ ") xx																														\n"    
            	+ "where subjcnt > 0																										\n"
            	+ "and studentcnt > 0																										\n"
            	+ "and (noanswercnt > 0 or noscorecnt > 0 )    																				\n";
            
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
     * 수강승인 미처리 대상자 수
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public int getApprovalCnt(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String  sql        = "";
        int v_cnt = 0;
        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "select count(userid) cnt				\n"
            	+ "from tz_propose a, tz_subjseq b		\n"
            	+ "where a.subj = b.subj                \n"
            	+ "and a.year= b.year                   \n"
            	+ "and a.subjseq= b.subjseq             \n"
            	+ "and chkfinal = 'B'					\n";
            if("F1".equals(s_gadmin)) {
        		sql += "and b.muserid = " +StringManager.makeSQL(s_userid) + "  \n";
        	}
            
            ls = connMgr.executeQuery(sql);
        

            if ( ls.next() ) { 
            	v_cnt = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_cnt;
    }
	
	/**
     * 수강승인 미처리 교육기수 수
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList approvalListForGrseq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String  sql        = "";
        DataBox dbox = null;
        ArrayList list = null;
        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select y.grcodenm, z.gyear, z.grseqnm			\n" 
            	+ "from (											\n"
            	+ "	select grcode, gyear, grseq						\n" 
            	+ "	from tz_propose a, tz_subjseq b					\n"
            	+ "	where a.subj = b.subj							\n"
            	+ "	and a.year = b.year								\n"
            	+ "	and a.subjseq = b.subjseq						\n" 
            	+ "	and a.chkfinal = 'B'							\n";		
            if("F1".equals(s_gadmin)) {
        		sql += "and b.muserid = " +StringManager.makeSQL(s_userid) + "  \n";
            }
            sql+= "	group by grcode, gyear, grseq					\n"
            	+ ") x, tz_grcode y, tz_grseq z						\n"
            	+ "where x.grcode = y.grcode						\n"
            	+ "and y.grcode = z.grcode							\n"
            	+ "and x.gyear = z.gyear							\n"
            	+ "and x.grseq = z.grseq							\n";
            
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
     * 다음달 신청 인원
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList proposeForNextMonth(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String  sql        = "";
        DataBox dbox = null;
        ArrayList list = null;
        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select a.subjnm, a.year, a.subjseq									\n"
            	+ "from tz_subjseq a, tz_propose b										\n"
            	+ "where a.subj  =b.subj												\n"
            	+ "and a.year= b.year													\n"
            	+ "and a.subjseq =b.subjseq												\n"
            	+ "and ((edustart like to_char(add_months(sysdate, 1), 'yyyyMM') || '%') or (eduend like to_char(add_months(sysdate, 1), 'yyyyMM') || '%'))	\n"
            	+ "and b.chkfinal = 'Y'													\n";
            	 if("F1".equals(s_gadmin)) {
              		sql += "and a.muserid = " +StringManager.makeSQL(s_userid) + "  \n";
              	}
            sql+= "group by a.subjnm, a.year, a.subjseq									\n";
            
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
     * 수강 관련 cnt 받아오기
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public int getCnt(String period, RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String  sql        = "";
        int v_cnt = 0;
        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "select nvl(sum(cnt), 0) cnt										\n"
            	+ "from (															\n"
            	+ "    select count(grseq) cnt										\n"
            	+ "    from tz_subjseq												\n"
            	+ "    where substr(" + period + ", 1, 8) = to_char(sysdate, 'yyyyMMdd')	\n";
            if("F1".equals(s_gadmin)) {
            	sql += "and muserid = " +StringManager.makeSQL(s_userid) + "  \n";
            }
            sql+= "    group by grcode, gyear, grseq								\n"
            	+ ")																\n";
            
            ls = connMgr.executeQuery(sql);
        

            if ( ls.next() ) { 
            	v_cnt = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_cnt;
    }
	
	/**
     * 평가 3일데 미평가한 수강생 count
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public int getExamCnt(String v_date, RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String  sql        = "";
        int v_cnt = 0;
        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "select (count(b.userid) - count(c.userid)) cnt						\n"
            	+ "from tz_subjseq a, tz_student b, tz_examresult c						\n"
            	+ "where a.subj = b.subj												\n"
            	+ "and a.year= b.year													\n"
            	+ "and a.subjseq= b.subjseq												\n"
            	+ "and b.subj = c.subj(+)												\n"
            	+ "and b.year= c.year(+)												\n"
            	+ "and b.subjseq= c.subjseq(+)											\n"
            	+ "and b.userid = c.userid(+)											\n"
            	+ "and to_char(sysdate, 'yyyyMMdd') between to_char(to_date(" + v_date + ", 'yyyyMMdd') - 3, 'yyyyMMdd') and substr(" + v_date + ", 1,8)	\n"; 
            if("F1".equals(s_gadmin)) {
          		sql += "and a.muserid = " +StringManager.makeSQL(s_userid) + "  \n";
          	}
            ls = connMgr.executeQuery(sql);
        

            if ( ls.next() ) { 
            	v_cnt = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_cnt;
    }
	
	
	/**
     * 수료처리 미처리 대상자 수
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public int getStoldCnt(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String  sql        = "";
        int v_cnt = 0;
        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "select count(*) cnt								\n"
            	+ "from tz_student a, tz_subjseq b				\n"
            	+ "where a.subj = b.subj							\n"
            	+ "and a.year= b.year								\n"
            	+ "and a.subjseq= b.subjseq							\n"
            	+ "and to_char(sysdate, 'yyyyMMddhh24') >= substr(b.eduend, 1, 10) \n"
            	+ "and b.isclosed = 'N'							\n";
            if("F1".equals(s_gadmin)) {
          		sql += "and b.muserid = " +StringManager.makeSQL(s_userid) + "  \n";
          	}
            ls = connMgr.executeQuery(sql);
        

            if ( ls.next() ) { 
            	v_cnt = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_cnt;
    }
	
	/**
     * 수료처리 미처리 교육기수 수
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList completeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String  sql        = "";
        DataBox dbox = null;
        ArrayList list = null;
        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select y.grcodenm, z.gyear, z.grseqnm			\n" 
            	+ "from (											\n"
            	+ "	select b.grcode, b.gyear, b.grseq				\n" 
            	+ "	from tz_student a, tz_subjseq b					\n"
            	+ " where a.subj = b.subj							\n"
            	+ " and a.year= b.year								\n"
            	+ " and a.subjseq= b.subjseq						\n"
            	+ " and to_char(sysdate, 'yyyyMMddhh24') >= substr(b.eduend, 1, 10) \n"
            	+ " and b.isclosed = 'N'							\n";
            	if("F1".equals(s_gadmin)) {
               		sql += "and b.muserid = " +StringManager.makeSQL(s_userid) + "  \n";
               	}
            sql+= "	group by grcode, gyear, grseq					\n"
            	+ ") x, tz_grcode y, tz_grseq z						\n"
            	+ "where x.grcode = y.grcode						\n"
            	+ "and y.grcode = z.grcode							\n"
            	+ "and x.gyear = z.gyear							\n"
            	+ "and x.grseq = z.grseq							\n";
            
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
     * 현재의 교육운영현황
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList eduList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        ArrayList grcomps     = null;
        DataBox dbox        = null;
        String  sql        = "";
        int v_pageno = box.getInt("p_pageno");
        String v_gubun = box.getString("p_gubun");
        String s_gadmin = box.getSession("gadmin");
        String ss_subjcourse = "ALL";
        ss_subjcourse = box.getStringDefault("s_subjcourse", "ALL");
        if("F1".equals(s_gadmin)) {
        	ss_subjcourse = box.getString("s_subjcourse").replaceAll("----", "ALL");
        }
        
        String ss_company = box.getStringDefault("s_company", "ALL");
        GrcompBean  grcompBean = new GrcompBean();
        
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            grcomps = new ArrayList();
            
            sql = "select b.grcode, b.grcodenm, c.grseqnm, a.subjnm, a.subj, a.year, a.subjseq, f.isonoff													\n"
                + "    , case when floor(to_date(to_char(sysdate, 'yyyyMMdd'), 'yyyyMMdd') - to_date(substr(eduend, 1,8), 'yyyyMMdd')) < 0 then 'D' || floor(to_date(to_char(sysdate, 'yyyyMMdd'), 'yyyyMMdd') - to_date(substr(eduend, 1,8), 'yyyyMMdd'))	\n"	
                + "			  when floor(to_date(to_char(sysdate, 'yyyyMMdd'), 'yyyyMMdd') - to_date(substr(eduend, 1,8), 'yyyyMMdd')) = 0 then 'Today'			\n"
                + "			  when floor(to_date(to_char(sysdate, 'yyyyMMdd'), 'yyyyMMdd') - to_date(substr(eduend, 1,8), 'yyyyMMdd')) > 0 then 'D' || '+' || floor(to_date(to_char(sysdate, 'yyyyMMdd'), 'yyyyMMdd') - to_date(substr(eduend, 1,8), 'yyyyMMdd')) end content	\n"
            	+ "    , eduend	,edustart																																\n"           
            	+ "    , nvl(round(avgvalue, 1),0) avgvalue																	   											\n" 
            	+ "    , case when f.isonoff != 'OFF' then (																										\n"
            	+ "        select count(*) || ''																												\n"
            	+ "        from tz_projassign																													\n"	
            	+ "        where subj = a.subj																													\n"
            	+ "        and year= a.year																														\n"		
            	+ "        and subjseq= a.subjseq																												\n"
            	+ "        and repdate is not null																												\n"															
            	+ "        and totalscore is null																												\n"					
            	+ "    ) else '해당없음' end noscorecnt																											\n"   
            	+ "    , case when f.isonoff != 'OFF' then (																										\n"
            	+ "        select count(*) || ''																												\n"
            	+ "        from (																																\n"																						
            	+ "	           select a.tabseq, b.seq, b.levels, a.subj, b.title, b.indate																		\n"				
            	+ "            from tz_bds a, tz_board b																										\n"												
            	+ "            where a.tabseq = b.tabseq																										\n"												
            	+ "	           and a.type = 'SQ'																												\n"														
            	+ "        ) x inner join tz_subj z																												\n"														
            	+ "        on x.subj = z.subj																													\n"															
            	+ "        inner join tz_classtutor k																											\n"													
            	+ "        on x.subj = k.subj																													\n"															
            	+ "        left outer join (																													\n"															
            	+ "            select tabseq, refseq, count(*) cnt																								\n"										
            	+ "            from tz_board																													\n"															
            	+ "            where levels > 1																													\n"                                                         
            	+ "            group by tabseq, refseq																											\n"
            	+ "			) y																																	\n"																			
            	+ "        on x.tabseq = y.tabseq																												\n"														
            	+ "        and x.seq = y.refseq																													\n"															
            	+ "        where x.levels = 1																													\n"															
            	+ "        and nvl(y.cnt, 0) = 0																												\n"
            	+ "        and x.subj = a.subj                                                                                                                  \n"
            	+ "    ) else '해당없음' end noanswercnt																											\n"    
            	+ "    , nvl(round(stoldcnt/studentcnt*100, 1), 0) graduatedpercent																						\n"
            	+ "    , f.isonoff, f.contenttype                                                                                                               \n"
            	+ "from tz_subjseq a, tz_subj f, tz_grcode b, tz_grseq c																									\n";
            	if(!"ALL".equals(ss_company)) {
            		sql+= " , tz_grcomp f                                                                                                                       \n";
            	}
            	sql+= " , (																								\n"
            	+ "    select subj, year, subjseq, avg(nvl(tstep, 0)) avgvalue, count(*) studentcnt																\n"
            	+ "    from tz_student																															\n"
            	+ "    group by subj, year, subjseq																												\n"
            	+ ") d, (																																		\n"
            	+ "    select subj, year, subjseq, count(*) stoldcnt																							\n"
            	+ "    from tz_student																															\n"
            	+ "	   where isgraduated= 'Y'																													\n"
            	+ "    group by subj, year, subjseq																												\n"
            	+ ") e																																			\n"
            	+ "where a.subj = f.subj                                                                                                                        \n"
            	+ "and a.grcode = b.grcode																													\n"
            	+ "and a.grcode = c.grcode																														\n"
            	+ "and a.gyear= c.gyear																															\n"
            	+ "and a.grseq = c.grseq																														\n";
            	if("Y".equals(v_gubun)) {
            		sql += "and to_char(sysdate, 'yyyyMMdd') > substr(eduend, 1, 8)																			\n"
            			+ "and a.isclosed = 'N'																													\n";
            	} else {
            		sql += "and to_char(sysdate, 'yyyyMMdd') between substr(edustart, 1, 8) and substr(eduend, 1, 8)											\n";
            	}
            	
            	if(!"ALL".equals(ss_subjcourse)) {
            		sql += "and a.subj = " + StringManager.makeSQL(ss_subjcourse) + "                                                                           \n";
            	}
            	
            	if(!"ALL".equals(ss_company)) {
            		sql += "and b.grcode = f.grcode																												\n"
            			+ "and f.comp = " + StringManager.makeSQL(ss_company) + "                                                                               \n";
            	}
             sql+= "and a.subj = d.subj(+)																														\n"
            	+ "and a.year= d.year(+)																														\n"
            	+ "and a.subjseq= d.subjseq(+)																													\n"
            	+ "and a.subj = e.subj(+)																														\n"
            	+ "and a.year= e.year(+)																														\n"
            	+ "and a.subjseq= e.subjseq(+)																													\n";
             
             if("F1".equals(s_gadmin)) {
            	 sql += "and a.muserid = " + StringManager.makeSQL(s_userid) + "                                                                                \n";
             }

            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
            int totalpagecount = ls.getTotalPage();    //     전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount();    //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_avgvalue", new Double(ls.getDouble("avgvalue")));
                dbox.put("d_graduatedpercent", new Double(ls.getDouble("graduatedpercent")));
                dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(row));
                grcomps = (ArrayList)grcompBean.SelectGrcompList(box,ls.getString("grcode") );
                dbox.put("comptxt",grcompBean.getCompTxt(grcomps,ls.getString("grcode")));
                dbox.put("grcomps",grcomps);
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
     * 이달의 교육운영현황
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList thisMonthList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String sql        = "";
        String v_enddate = FormatDate.getEnddate(FormatDate.getDate("yyyyMMdd"));
        int v_endday = Integer.parseInt(v_enddate.substring(6, 8));
        
        String v_month = FormatDate.getDate("yyyyMM");
        String v_day = "";
        String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        	
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
//            sql = "select 																				\n";
//            for(int i=1; i<=v_endday; i++) {
//            	if(i > 1)
//            		sql += ", ";
//            	
//            	if(i < 10)
//            		v_day = "0" + i;
//            	
//            	sql += "trim(GET_EDUINFO(" + SQLString.Format(v_month + v_day) + ", " + SQLString.Format(s_userid) + ", " + SQLString.Format(s_gadmin) + ")) a" + i +"				\n";
//            }
//            sql += "from dual																			\n";
            sql = "select trim(GET_EDUINFO(" + SQLString.Format(v_month) + "|| day, " + SQLString.Format(s_userid) + ", " + SQLString.Format(s_gadmin) + ")) content		\n"
            	+ "from (																\n"
            	+ "	select lpad(rownum, 2, '0') day										\n"
            	+ "	from tz_member														\n" 
            	+ "	where rownum <= to_char(last_day(sysdate), 'dd')					\n"
            	+ ")																	\n";
            
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
     * 이달의 교육운영현황 - 팝업
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList thisMonthView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String sql        = "";
        
        String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        String v_gubun = box.getString("p_gubun");
        String v_day = box.getString("p_day");
        	
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
//            sql = "select exp_es, exp_ee, exp_ps, exp_pe, exp_mts, exp_mte, exp_fts, exp_fte														\n"
//            	+ "	  from (																														\n"
//            	+ "			select case when edustart like '20081201' || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end exp_es		\n"
//            	+ "				   , case when eduend like '20081201' || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end exp_ee		\n"
//            	+ "				   , case when propstart like '20081201' || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end exp_ps		\n"
//            	+ "   			   , case when propend like '20081201' || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end exp_pe		\n"	
//            	+ "				   , case when mtest_start like '20081201' || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end exp_mts	\n"
//            	+ "				   , case when mtest_end like '20081201' || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end exp_mte	\n"
//            	+ "				   , case when ftest_start like '20081201' || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end exp_fts	\n"
//            	+ "				   , case when ftest_end like '20081201' || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end exp_fte	\n"	
//            	+ "				   , case when edustart like '20081201' || '%' then 1 else 0 end es													\n"
//            	+ "				   , case when eduend like '20081201' || '%' then 1 else 0 end ee													\n"
//            	+ "				   , case when propstart like '20081201' || '%' then 1 else 0 end ps												\n"
//            	+ "				   , case when propend like '20081201' || '%' then 1 else 0 end pe													\n"
//            	+ "				   , case when mtest_start like '20081201' || '%' then 1 else 0 end mts												\n"
//            	+ "				   , case when mtest_end like '20081201' || '%' then 1 else 0 end mte												\n"
//            	+ "				   , case when ftest_start like '20081201' || '%' then 1 else 0 end fts												\n"
//            	+ "				   , case when ftest_end like '20081201' || '%' then 1 else 0 end fte												\n"			   				   				   				   				   
//            	+ "			from (																													\n" 
//            	+ "				   select x.subj, x.year, x.subjseq, y.subjnm, x.muserid, x.edustart,  x.eduend, x.propstart, x.propend, x.mtest_start, x.mtest_end, x.ftest_start, x.ftest_end	\n"
//            	+ "				   from tz_subjseq x, tz_subj y																						\n"
//            	+ "				   where x.subj = y.subj																							\n";
//            
//            if("F1".equals(s_gadmin)) {
//            	sql += "and a.muserid = " + StringManager.makeSQL(s_userid) + "                                                                     \n";
//            }
//            	
//            sql+= "			) a left outer join tz_projgrp b																						\n"
//            	+ "			on a.subj = b.subj																										\n"
//            	+ "			and a.year = b.year																										\n"
//            	+ "			and a.subjseq= b.subjseq																								\n" 
//            	+ "	)																																\n"
//            	+ "	where ( es > 0 or ee > 0 or ps > 0 or pe > 0 or mts > 0 or mte > 0 or fts > 0 or fte > 0)										\n";
            sql = " select content                                                                                                                  \n"
            	+ " from (																															\n"
            	+ "			select 																													\n";
            	if("ES".equals(v_gubun)) {
            		sql+= "			case when edustart like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 				\n";
            	} else if("EE".equals(v_gubun)) {
            		sql+= "			case when eduend like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 				\n";
            	} else if("PS".equals(v_gubun)) {
            		sql+= "		    case when propstart like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 			\n";
            	} else if("PE".equals(v_gubun)) {
            		sql+= "			case when propend like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 				\n";	
            	} else if("MTS".equals(v_gubun)) {
            		sql+= "		    case when mtest_start like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 			\n";	
            	} else if("MTE".equals(v_gubun)) {
            		sql+= "		    case when mtest_end like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 			\n";
            	} else if("FTS".equals(v_gubun)) {
            		sql+= "		    case when ftest_start like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 			\n";	
            	} else if("FTE".equals(v_gubun)) {
            		sql+= "			case when ftest_end like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 			\n";	
            	} else if("RS".equals(v_gubun)) {
            		sql+= "		    case when b.sdate like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 			\n";	
            	} else if("RE".equals(v_gubun)) {
            		sql+= "			case when b.edate like " + StringManager.makeSQL(v_day) + " || '%' then ( a.subjnm || '[' || a.subjseq || ']' ) else '' end 			\n";	            		
            	} 			   				   				   				   				   
            sql+= " as content																														\n"
            	+ "	from (																															\n" 
            	+ "				   select x.subj, x.year, x.subjseq, y.subjnm, x.muserid, x.edustart,  x.eduend, x.propstart, x.propend, x.mtest_start, x.mtest_end, x.ftest_start, x.ftest_end	\n"
            	+ "				   from tz_subjseq x, tz_subj y																						\n"
            	+ "				   where x.subj = y.subj																							\n";
            
            if("F1".equals(s_gadmin)) {
            	sql += "and x.muserid = " + StringManager.makeSQL(s_userid) + "                                                                     \n";
            }
            	
            sql+= "			) a																														\n";
            	if("RS".equals(v_gubun) || "RE".equals(v_gubun)) {
            sql+= "			left outer join tz_projgrp b																							\n"
            	+ "			on a.subj = b.subj																										\n"
            	+ "			and a.year = b.year																										\n"
            	+ "			and a.subjseq= b.subjseq																								\n";
            	}
            sql+= "			where 1=1																												\n";
        	if("ES".equals(v_gubun)) {
        		sql+= "		and case when edustart like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end	 > 0						\n";
        	} else if("EE".equals(v_gubun)) {
        		sql+= "		and case when eduend like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end > 0								\n";
        	} else if("PS".equals(v_gubun)) {
        		sql+= "		and case when propstart like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end > 0							\n";
        	} else if("PE".equals(v_gubun)) {
        		sql+= "		and case when propend like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end > 0							\n";	
        	} else if("MTS".equals(v_gubun)) {
        		sql+= "		and case when mtest_start like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end > 0	 					\n";	
        	} else if("MTE".equals(v_gubun)) {
        		sql+= "		and case when mtest_end like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end > 0							\n";
        	} else if("FTS".equals(v_gubun)) {
        		sql+= "		and case when ftest_start like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end > 0						\n";	
        	} else if("FTE".equals(v_gubun)) {
        		sql+= "		and case when ftest_end like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end > 0							\n";	
        	} else if("RS".equals(v_gubun)) {
        		sql+= "		and case when b.sdate like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end > 0							\n";	
        	} else if("RE".equals(v_gubun)) {
        		sql+= "		and	case when b.edate like " + StringManager.makeSQL(v_day) + " || '%' then 1 else 0 end > 0							\n";	            		
        	} 		
        	sql += ")                                                                                                                                 	\n";
    		if(!("RS".equals(v_gubun) || "RE".equals(v_gubun))) {
    			sql+= "group by content																														\n";
    		}
            
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
     * 이달의 교육운영현황(수정)
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList thisMonthList2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = new ArrayList();
        DataBox dbox        = null;
        String sql        = "";
        String v_enddate = FormatDate.getEnddate(FormatDate.getDate("yyyyMMdd"));
        int v_endday = Integer.parseInt(v_enddate.substring(6, 8));
        
        String v_year = FormatDate.getDate("yyyy");
        String v_month = FormatDate.getDate("MM");
        String v_day = "";
        String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        
        String v_gubun = box.getStringDefault("p_gubun", "ALL");
        	
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            dbox = new DataBox("selectbox");
            for(int i=1; i<=v_endday; i++) {
            	if(i < 10) {
            		v_day = "0" + i;
            	} else {
            		v_day = i + "";
            	}
            	dbox.put("d_year", v_year);
            	dbox.put("d_month", v_month);
            	dbox.put("d_day", v_day);
            	dbox.put("d_titlecd", this.getTitlecd(v_year, v_month, v_day, v_gubun, s_gadmin, s_userid));
            	list.add(dbox);
            	dbox = new DataBox("selectbox");
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
     * 타이틀 alias값 받아오기
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public String getTitlecd(String v_year, String v_month, String v_day, String v_gubun, String s_gadmin, String s_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String sql        = "";
        String v_titlecd = "";
        
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "	select a.gubun, f_year, f_month, f_date, a.titlecd, content, b.codenm title		\n"					
            	+ "	from tz_schedule a, (												\n"
            	+ "		 select code, codenm											\n"
            	+ "		 from tz_code													\n"
            	+ "		 where gubun = '0108'											\n"
            	+ ") b																	\n";		 													
            if("P1".equals(s_gadmin)) {
	            sql+= "	, (																\n"												
	            	+ "		select a.subj, a.gubun										\n"												
	            	+ "		from tz_subj_mapping a, tz_classtutor b						\n"								
	            	+ "		where a.subj = b.subj										\n"												
	            	+ "		and b.tuserid = " + StringManager.makeSQL(s_userid) + "  	\n"											
	            	+ "		group by a.subj, a.gubun									\n"											
	            	+ "	) c																\n"																	
	            	+ "	where a.gubun = c.gubun											\n"
	            	+ " and ( a.gadmin = 'P' or a.gadmin = 'A')							\n";
            } else if("F1".equals(s_gadmin)) {
            	sql += " , (															\n"
            		+ "			select x.subj, max(y.gubun) gubun						\n" 
            		+ "			from tz_subjseq x, tz_subj_mapping y					\n"
            		+ "			where x.subj = y.subj									\n"
            		+ "			and x.muserid = " + StringManager.makeSQL(s_userid) + " \n"
            		+ "			group by x.subj											\n"
            		+ ") c																\n"
            		+ "where a.gubun =c.gubun 											\n"
            		+ " and ( a.gadmin = 'F' or a.gadmin = 'A')							\n";
            } else {
            	sql+= "where 1=1 														\n";
            }
            sql+= " and a.titlecd = b.code 												\n"
            	+ "	and f_year = " + StringManager.makeSQL(v_year) + " 					\n"					
            	+ "	and f_month = " + StringManager.makeSQL(v_month) + "                \n"	
            	+ "	and f_date = " + StringManager.makeSQL(v_day) + "                   \n";
            	
            if(!"ALL".equals(v_gubun)) {	
            	sql+= "	and a.gubun = " + StringManager.makeSQL(v_gubun) + " 			\n";
            }	
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
//                v_titlecd += "<a href='javascript:view(\"" + v_year + v_month + v_day + "\", \"" + ls.getString("titlecd") + "\");'>" + ls.getString("titlecd") + "</a> ";
                v_titlecd += "<a href='#none' title='" + ls.getString("title") + " : " + ls.getString("content") + "'>" + ls.getString("titlecd") + "</a> ";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_titlecd;
    }
	
	/**
     * 이달의 교육운영현황(수정) - 구분 select box값
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList selectGubun(String v_gubun) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = new ArrayList();
        DataBox dbox        = null;
        String sql        = "";
        	
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = "	select code, codenm		\n"
            	+ "	from tz_code			\n"
            	+ "	where gubun = " + StringManager.makeSQL(v_gubun) + " \n";
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) {
            	dbox = ls.getDataBox();
            	list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
}