// **********************************************************
//  1. 제      목: 튜터 메인
//  2. 프로그램: TutorMainBean.java
//  3. 개      요: 튜터 메인
//  4. 환      경: JDK 1.5
//  5. 버      젼: 1.0
//  6. 작      성: 2008.12.17
//  7. 수      정:
// **********************************************************
package com.ziaan.tutor;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class TutorMainBean { 
    private ConfigSet config;
    private int row;
    
    public TutorMainBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
     * 강사 공지사항
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList noticeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        int v_tabseq = 1;
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select title, indate, name, seq, tabseq, ldate                   \n"
            	+ "from (															\n"
            	+ "		select title, indate, name, seq, tabseq, ldate  			\n"
            	+ "		from tz_board												\n"
            	+ "		where tabseq = " + SQLString.Format(v_tabseq) + " 			\n"
            	+ "     order by ldate asc         		                        \n"
            	+ ")                                        		                \n"
            	+ "where rownum <=3													\n";
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
     * 운영자에게
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList toadminList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        int v_tabseq = 2;
        String s_userid = box.getSession("userid");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select title, b.indate, name, a.tabseq, b.seq										\n"
            	+ "from tz_board a, (																\n"
            	+ "    select max(seq) seq, refseq, max(indate) indate								\n"											
            	+ "    from tz_board																\n"											
            	+ "    where tabseq = " + SQLString.Format(v_tabseq)  + "							\n"        
            	+ "    and levels > 1																\n"											
            	+ "    and substr(indate, 1, 8) between to_char(sysdate -3, 'yyyyMMdd') and to_char(sysdate, 'yyyyMMdd')		\n"	
            	+ "    group by refseq																\n"
            	+ ") b																				\n"    
            	+ "where a.tabseq = " + SQLString.Format(v_tabseq)  + "								\n"            
            	+ "and a.userid = " + StringManager.makeSQL(s_userid) + "							\n"         
            	+ "and a.levels = 1																	\n"												
            	+ "and a.seq = b.refseq																\n";
             
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
     * 담당과정 comment
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList tutorChargeList1(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select x.grcodenm									\n"
            	+ "	   , x.grseqnm										\n"
            	+ "    , x.subjnm										\n"
            	+ "    , x.edustart										\n"
            	+ "    , x.eduend										\n"
            	+ "    , x.subj											\n"
            	+ "    , x.year											\n"
            	+ "    , x.subjseq										\n"
            	+ "    , nvl(y.studcnt, 0) studcnt						\n"
            	+ "    , nvl(z.stoldcnt, 0) stoldcnt					\n"
            	+ "    , x.startday										\n"
            	+ "    , x.endday										\n"
            	+ "    , nvl(z.stoldcnt, 0)/nvl(y.studcnt, 1)*100 gradcnt	\n"
            	+ "    , class                                          \n"
            	+ "    , isonoff                                        \n"
            	+ "    , contenttype                                    \n"
            	+ "from (													\n"
            	+ "    select c.grcodenm, d.grseqnm, b.subjnm, edustart, eduend, b.subj, b.year, b.subjseq	\n"
            	+ "        , floor(sysdate - to_date(edustart, 'yyyyMMddhh24miss')) startday				\n"
            	+ "        , floor(sysdate - to_date(eduend, 'yyyyMMddhh24miss')) endday					\n"
            	+"         , a.class                                                                        \n"
            	+ "        , b.isonoff                                                                      \n"
            	+ "        , b.contenttype                                                                  \n"
            	+ "    from tz_classtutor a, vz_scsubjseq b, tz_grcode c, tz_grseq d						\n"
            	+ "    where a.subj = b.subj																\n"
            	+ "    and a.year = b.year																	\n"
            	+ "    and a.subjseq = b.subjseq															\n"
            	+ "    and b.grcode = c.grcode																\n"
            	+ "    and b.grcode = d.grcode																\n"
            	+ "    and b.gyear = d.gyear																\n"
            	+ "    and b.grseq = d.grseq																\n"
            	+ "    and a.tuserid = " + StringManager.makeSQL(s_userid)+ "                               \n"
            	+ ") x left outer join (																	\n"
            	+ "    select subj, year, subjseq, count(*) studcnt											\n"
            	+ "    from tz_student y																	\n"
            	+ "    group by subj, year, subjseq															\n"
            	+ ") y																						\n" 
            	+ "on x.subj  = y.subj																		\n"
            	+ "and x.year= y.year																		\n"
            	+ "and x.subjseq = y.subjseq																\n"
            	+ "left outer join (																		\n"
            	+ "    select subj, year, subjseq, count(*) stoldcnt										\n"
            	+ "    from tz_student y																	\n"
            	+ "    where isgraduated = 'Y'																\n"
            	+ "    group by subj, year, subjseq															\n"
            	+ ") z																						\n"    
            	+ "on x.subj = z.subj																		\n"
            	+ "and x.year = z.year																		\n"
            	+ "and x.subjseq = z.subjseq																\n"
            	+ "where to_char(sysdate, 'yyyyMMDDhh24mi') between x.edustart and x.eduend					\n";
            
            ls = connMgr.executeQuery(sql);
        

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_gradcnt", new Double(ls.getDouble("gradcnt")));
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
     * 담당과정 Data
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList tutorChargeList2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select cnt, content											\n"
            	+ "from (														\n"
            	+ "    select count(*) cnt, '오늘 교육이 시작되는 과정' content		\n"
            	+ "    from vz_scsubjseq a, tz_classtutor b						\n"
            	+ "    where a.subj = b.subj									\n"
            	+ "    and a.year= b.year										\n"
            	+ "    and a.subjseq = b.subjseq								\n"
            	+ "    and b.tuserid = " +  StringManager.makeSQL(s_userid) + " \n"
            	+ "    and edustart like to_char(sysdate, 'yyyyMMdd') || '%'	\n"
            	+ ")															\n"
            	+ "where cnt > 0												\n"
            	+ "union all													\n"
            	+ "select cnt, content											\n"
            	+ "from (														\n"
            	+ "    select eduend, count(*) cnt								\n"
            	+ "        , case when floor(sysdate - to_date(eduend, 'yyyyMMddhh24miss')) < 0 then '종료 D' || floor(sysdate - to_date(eduend, 'yyyyMMddhh24miss')) || '인 과정'		\n"
            	+ "        when floor(sysdate - to_date(eduend, 'yyyyMMddhh24miss')) = 0 then '오늘 교육이 종료되는 과정' end content							\n"
            	+ "    from vz_scsubjseq a, tz_classtutor b						\n"
            	+ "    where a.subj = b.subj									\n"
            	+ "    and a.year= b.year										\n"
            	+ "    and a.subjseq = b.subjseq								\n"
            	+ "    and b.tuserid = " + StringManager.makeSQL(s_userid) + "  \n"
            	+ "    and eduend >= to_char(sysdate, 'yyyyMMddhh24mi')			\n"  
            	+ "    group by eduend											\n"
            	+ "    order by eduend desc										\n"
            	+ ") content													\n";
            
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
     * 과제 
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList projList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select d.grcodenm, e.gyear, e.grseq, e.grseqnm, c.subjnm, c.subj, c.year, c.subjseq, b.class  	\n"
            	+ "    , case when to_char(sysdate, 'yyyyMMddhh24mi') between edustart and eduend then '진행중'		\n"
            	+ "           when to_char(sysdate, 'yyyyMMddhh24mi') > eduend then '교육완료' end status				\n"
            	+ "    , c.edustart, c.eduend                                                                       \n"
            	+ "    , (																							\n"
            	+ "        select count(*)																			\n"
            	+ "        from tz_projassign																		\n"
            	+ "        where subj = c.subj																		\n"
            	+ "        and year= c.year																			\n"
            	+ "        and subjseq= c.subjseq																	\n"
            	+ "        and repdate is not null																	\n"
            	+ "    ) repcnt																						\n"
            	+ "    , (																							\n"
            	+ "        select count(*)																			\n"
            	+ "        from tz_projassign																		\n"
            	+ "        where subj = c.subj																		\n"
            	+ "        and year= c.year																			\n"        
            	+ " 	   and subjseq= c.subjseq																	\n"
            	+ "        and repdate is not null																	\n"
            	+ "        and totalscore is null																	\n"
            	+ "		) noscorecnt        																		\n"
            	+ "from tz_classtutor b, vz_scsubjseq c, tz_grcode d, tz_grseq e									\n"
            	+ "where b.subj = c.subj																			\n"
            	+ "and b.year= c.year																				\n"
            	+ "and b.subjseq= c.subjseq																			\n"
            	+ "and c.grcode= d.grcode																			\n"
            	+ "and c.grcode= e.grcode																			\n"
            	+ "and c.gyear= e.gyear																				\n"
            	+ "and c.grseq = e.grseq																			\n"
            	+ "and b.tuserid = " + StringManager.makeSQL(s_userid) + "                                          \n"
            	+ "and to_char(sysdate, 'yyyyMMddhh24mi') >= edustart												\n"
            	+ "and c.isclosed = 'N'																				\n";
            
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
     * 질문방 
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList sqList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox        = null;
        String  sql        = "";
        String s_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "select x.tabseq, x.seq, x.levels, x.subj, x.year, x.subjseq, z.subjnm, x.title, x.indate				\n"
            	+ "     , nvl(y.cnt, 0) cnt																				\n"
            	+ "    from (																							\n"
            	+ "        select a.tabseq, b.seq, b.levels, a.subj, b.year, b.subjseq, b.title, b.indate				\n"
            	+ "        from tz_bds a, tz_board b																	\n"
            	+ "        where a.tabseq = b.tabseq																	\n"
            	+ "        and a.type = 'SQ'																			\n"
            	+ "    ) x inner join tz_subj z																			\n"
            	+ "    on x.subj = z.subj																				\n"
            	+ "    inner join tz_classtutor k																		\n"
            	+ "    on x.subj = k.subj																				\n"
            	+ "    and x.year = k.year																				\n"
            	+ "    and x.subjseq = k.subjseq																		\n"
            	+ "    left outer join (																				\n"
            	+ "        select tabseq, refseq, count(*) cnt															\n"
            	+ "        from tz_board																				\n"
            	+ "        where levels > 1                                                                             \n"
            	+ "        group by tabseq, refseq																		\n"
            	+ "    ) y																								\n"        
            	+ "    on x.tabseq = y.tabseq																			\n"
            	+ "    and x.seq = y.refseq																				\n"
            	+ "    where x.levels = 1																				\n"
            	+ "    and nvl(y.cnt, 0) = 0				    														\n"
            	+ "    and tuserid = " + StringManager.makeSQL(s_userid) + "                                            \n"
            	+ "    order by x.subj, x.year, x.subjseq, x.indate desc												\n";
            
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
     * 최종접속일시 받아오기 
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public static String getlglast(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String  sql        = "";
        String s_userid = box.getSession("userid");
        String v_lglast = "";
        
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "select lglast													\n"
            	+ "from tz_member													\n"
            	+ "where userid = " + StringManager.makeSQL(s_userid) + "           \n";
            
            ls = connMgr.executeQuery(sql);
        

            if ( ls.next() ) { 
            	v_lglast = ls.getString("lglast");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_lglast;
    }
	
	/**
     * 권한 받아오기 
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public static String getGadmin(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String  sql        = "";
        String s_gadmin = box.getSession("gadmin");
        String v_gadminnm = "";
        
        try { 
            connMgr = new DBConnectionManager();
            
            sql = "select gadminnm											\n" 
            	+ "from tz_gadmin											\n" 
            	+ "where gadmin = " + StringManager.makeSQL(s_gadmin)+ " 	\n";
            
            ls = connMgr.executeQuery(sql);
        

            if ( ls.next() ) { 
            	v_gadminnm = ls.getString("gadminnm");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_gadminnm;
    }
}