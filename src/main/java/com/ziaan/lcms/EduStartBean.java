// **********************************************************
//  1. 제      목: 학습창 START BEAN
//  2. 프로그램명: EduStartBean.java
//  3. 개      요: 학습창 START BEAN
//  4. 환      경: JDK 1.4
//  5. 버      젼: 0.1
//  6. 작      성: S.W.Kang 2004. 12. 5
//  7. 수      정:
// **********************************************************
package com.ziaan.lcms;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.EduEtc1Bean;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class EduStartBean { 
    
	public static final String BETA_PROGRESS_TABLE_NAME = "tz_beta_progress";
	public static final String PROGRESS_TABLE_NAME = "tz_progress";

	private Logger logger = Logger.getLogger(this.getClass());
	
    private static EduStartBean instance = null;
    
	private EduStartBean() {
	}
	
	public static EduStartBean getInstance() {
		if ( instance == null ) {
			instance = new EduStartBean();
		}

		return instance;
	}
    
    /**
     * 마스터폼 Lesson리스트
     * @param box          receive from the form object and session
     * @return ArrayList   Lesson리스트
     */      
    public ArrayList SelectMfLessonList(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet ls = null, ls1 = null;
        ArrayList list1 = null;
        String sql  = "";
        MfLessonData data = null;

        String  s_subj      = box.getSession("s_subj");
        String  s_year      = box.getSession("s_year");
        String  s_subjseq   = box.getSession("s_subjseq");
        String  s_userid    = box.getSession("userid");
                
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql = "select module,lesson,sdesc,types,owner,starting "
                + "  from tz_subjlesson  "
                + " where subj=" + StringManager.makeSQL(s_subj)
                + " order by lesson";

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                data = new MfLessonData(); 
                data.setModule    ( ls.getString("module") );
                data.setLesson    ( ls.getString("lesson") );
                data.setSdesc     ( ls.getString("sdesc")) ;
                data.setTypes     ( ls.getString("types")) ;
                data.setOwner     ( ls.getString("owner")) ;
                data.setStarting  ( ls.getString("starting")) ;
                // data.setIsbranch  ( ls.getString("isbranch")) ;
                // 학습여부 Set
                /*
                sql = "select decode(count(userid),0,'N','Y') f_exist from tz_progress"
                    + " where subj="    +StringManager.makeSQL(s_subj)
                    + "   and year="    +StringManager.makeSQL(s_year)
                    + "   and subjseq=" +StringManager.makeSQL(s_subjseq)
                    + "   and lesson="  +StringManager.makeSQL(data.getLesson() )
                    + "   and userid="  +StringManager.makeSQL(s_userid);
                if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                ls1 = connMgr.executeQuery(sql);
                ls1.next();
                data.setIseducated( ls1.getString("f_exist") );
                // 응시할 평가존재여부 Set
                sql = "select decode(count(lesson),0,'N','Y') f_exist from tz_exampaper"
                    + " where subj="    +StringManager.makeSQL(s_subj)
                    + " and year="  +StringManager.makeSQL(s_year)
                    + " and subjseq="   +StringManager.makeSQL(s_subjseq)
                    // + "   and year='TEST'"
                    // + "   and subjseq='TEST'"
                    + "   and lesson="  +StringManager.makeSQL(data.getLesson() );

                if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                ls1 = connMgr.executeQuery(sql);
                ls1.next();
                data.setIsexam( ls1.getString("f_exist") );
                */
                list1.add(data);    
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    /**
     * 진도/목차 리스트 조회
     * @param box          receive from the form object and session
     * @return ArrayList   진도/목차리스트
     */      
    public ArrayList SelectEduList(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        ArrayList list = null;
        String sql  = "", sql2= "";
        EduListData x = null;

        String p_subj      = box.getString("p_subj");
        String p_year      = box.getString("p_year");
        String p_subjseq   = box.getString("p_subjseq");
        String s_userid    = box.getSession("userid");
                        
        if ( p_subj.equals("")||p_year.equals("")||p_subjseq.equals("")||s_userid.equals("") ) { 
            p_subj      = box.getSession("s_subj");
            p_year      = box.getSession("s_year");
            p_subjseq   = box.getSession("s_subjseq");
            s_userid    = box.getSession("userid"); 
        }
        
        if ( !box.getString("p_userid").equals("") && EduAuthBean.getInstance().isAdminAuth(box.getSession("gadmin")) ) {
        	s_userid = box.getString("p_userid");
        }
        
        
        String progressTableName = getProgressTableName( box ); 

        try { 
            connMgr = new DBConnectionManager();
            
            // 점수 재계산 (ALL)
            int isOk = 1;
        	//isOk = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.ALL,p_subj,p_year,p_subjseq,p_userid);

            list = new ArrayList();
            
            sql =
            	"\n  SELECT   a.lesson, a.sdesc, a.module, b.sdesc as modulenm, a.isbranch, a.starting,  " +
            	"\n           c.first_edu, c.first_end, c.session_time, c.total_time, c.lesson_count,  " +
            	//"\n           (select pagenum from tz_subjlesson_page where subj=c.subj and lesson=c.lesson and substr(starting, instr(starting, '/', -1) + 1) = c.stu_page) stu_page_cnt,  " +
            	"\n           (select pagenum from tz_subjlesson_page where subj=c.subj and lesson=c.lesson) stu_page_cnt,  " +
            	"\n           (select count(*) from tz_subjlesson_page where subj=a.subj and lesson=a.lesson) total_page_cnt  " +
            	"\n      FROM tz_subjlesson a, tz_subjmodule b, #progressTableName# c  " +
            	"\n     WHERE a.subj = #subj#  " +
            	"\n       AND a.subj = b.subj(+)  " +
            	"\n  	 AND a.module = b.module(+)  " +
            	"\n       AND c.subj(+) = a.subj  " +
            	"\n  	 AND c.year(+) = #year#  " +
            	"\n  	 AND c.subjseq(+) = #subjseq#  " +
            	"\n  	 AND c.userid(+) = #userid#  " +
            	"\n  	 AND c.lesson(+) = a.lesson  " +
            	"\n  ORDER BY a.lesson  ";

            sql = sql.replaceAll( "#subj#", StringManager.makeSQL(p_subj) );
            sql = sql.replaceAll( "#year#", StringManager.makeSQL(p_year) );
            sql = sql.replaceAll( "#subjseq#", StringManager.makeSQL(p_subjseq) );
            sql = sql.replaceAll( "#userid#", StringManager.makeSQL(s_userid) );
            sql = sql.replaceAll( "#progressTableName#", progressTableName );
            
            	/*
            	"select a.lesson, a.sdesc, a.module, a.isbranch,a.starting,                                                                                                   "
                + "       (select sdesc from tz_subjmodule where module=a.module and subj=a.subj) modulenm,                                                         "
                + "       (select first_edu from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson) first_edu,                                      "
                + "       (select first_end from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson) first_end,                                      "
                + "       (select session_time from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson) session_time,                                "
                + "       (select total_time from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson) total_time,                                    "
                + "       (select lesson_count from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson) lesson_count                                "
                // + "       (select count(ordseq) from tz_projord where subj=a.subj and " +v_str2 + " and lesson=a.lesson) cntReport                                    "
                // + "       (select count(seq) from tz_activity where subj=a.subj and lesson=a.lesson) cntAct,                                                        "
                // + "       (select count(distinct seq) from tz_activity_ans where subj=a.subj and  " +v_str1 + " and lesson=a.lesson) cntMyAct                          "
                + "  from tz_subjlesson a   "
                + " where a.subj=" +StringManager.makeSQL(p_subj)
                + " order by a.lesson ";
                */
            
            logger.info("진도목차 : \n" + sql);
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                x = new EduListData();
                
                x.setRecordType  ("STEP");
                x.setLesson      ( ls.getString("lesson") );  
                x.setSdesc       ( ls.getString("sdesc") );
                x.setStarting    ( ls.getString("starting") );
                x.setModule      ( ls.getString("module") );
                x.setModulenm    ( ls.getString("modulenm") );
                x.setIsbranch    ( ls.getString("isbranch") );
                x.setFirst_edu   ( ls.getString("first_edu") );
                x.setFirst_end   ( ls.getString("first_end") );
                x.setStu_page_cnt   ( ls.getString("stu_page_cnt") );
                x.setTotal_page_cnt ( ls.getString("total_page_cnt") );
                
                if ( x.getStarting() == null || x.getStarting().equals("") ) { x.setIsEducated("P"); }
                else if ( x.getFirst_end() != null && !x.getFirst_end().equals("") ) { x.setIsEducated("Y"); }
                else if ( (x.getFirst_edu() != null && !x.getFirst_edu().equals("")) && (x.getFirst_end() == null || x.getFirst_end().equals("")) ) { x.setIsEducated("X"); }
                
                x.setSession_time( ls.getString("session_time") );
                x.setTotal_time  ( ls.getString("total_time") );
                x.setLesson_count( ls.getInt("lesson_count") );
                
                if ( x.getCntReport() > 0) { 
                    x.setCntMyReport ( get_repCnt( connMgr,p_subj,p_year,p_subjseq,x.getLesson(),s_userid ) );
                }
                // x.setCntReport   ( ls.getInt("cntReport") );
                // x.setCntAct      ( ls.getInt("cntAct") );
                // x.setCntMyAct    ( ls.getInt("cntMyAct") );
               
                list.add(x);    
                
                /* 평가
                sql = "select examtype from tz_exampaper where subj=" +SQLString.Format(p_subj)
                    + "   and  year=" +SQLString.Format(p_year)
                    + "   and  subjseq=" +SQLString.Format(p_subjseq)
                    + "   and  lesson=" +SQLString.Format(x.getLesson() )
                    + " order by examtype";

                if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                ls1 = connMgr.executeQuery(sql);
                while ( ls1.next() ) { 
                    x = new EduListData();
                    x.setRecordType  ("EXAM");
                    x.setLesson      ( ls.getString("lesson") );  
                    x.setSdesc       ( ls.getString("sdesc") );
                    x.setModule      ( ls.getString("module") );
                    x.setModulenm    ( ls.getString("modulenm") );
                    x.setExamtype    ( ls1.getString("examtype") );

                    sql = "select score,started,ended from tz_examresult  where subj=" +SQLString.Format(p_subj)
                        + "   and year=" +SQLString.Format(p_year) + " and subjseq=" +SQLString.Format(p_subjseq)
                        + "   and examtype='" +ls1.getString("examtype") + "' and userid=" +SQLString.Format(p_userid)
                        + "    and lesson=" +SQLString.Format(x.getLesson() );
                    
                    if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    ls2 = connMgr.executeQuery(sql);
                    if ( ls2.next() ) { 
                        x.setFirst_edu   ( ls2.getString("started") );
                        x.setFirst_end   ( ls2.getString("ended") );
                        x.setScore       ( ls2.getDouble("score") );
                        x.setIsEducated("Y");
                    }
                    list.add(x);
                }
                */
            }
            
            // set row-span
            String v_module= "";
            for ( int i = 0;i<list.size();i++ ) { 
                x = (EduListData)list.get(i);
                
                if ( !v_module.equals(x.getModule() )) { 
                    v_module = x.getModule();
                    x.setRowspan(get_count(list,v_module));
                    list.set(i,x);
                }
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
     * 진도정보 테이블명을 반환한다.
     * 베타테스트 과정일 경우, BETA_PROGRESS_TABLE_NAME을 반환한다.
     * 
     * @param box
     * @return
     */
    private String getProgressTableName(RequestBox box) {
    	String progressTableName = EduStartBean.PROGRESS_TABLE_NAME;
    	
    	String p_subjseq = box.getStringDefault("p_subjseq", box.getSession("s_subjseq"));
    	if ( p_subjseq.equals("0000") ) {
    		progressTableName = EduStartBean.BETA_PROGRESS_TABLE_NAME;
    	}
    	
		return progressTableName;
	}

	/**
     * 진도/목차 리스트 조회-OBC
     * @param box          receive from the form object and session
     * @return ArrayList   OBC-진도/목차리스트
     */      
    public ArrayList SelectEduListOBC(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        ArrayList list = null, list2= null;
        String sql  = "", sql2= "";
        EduListData x = null;
        int isOk =0;

        String v_module = "01";	// set row-span
        String v_lesson = "00";
        
        // for Branching
        String  v_sdesc= "",oidnm= "",v_isbranch="N";
                            
        String p_subj      = box.getString("p_subj");
        String p_year      = box.getString("p_year");
        String p_subjseq   = box.getString("p_subjseq");
        String s_userid    = box.getSession("userid");
                        
        if ( p_subj.equals("")||p_year.equals("")||p_subjseq.equals("")||s_userid.equals("") ) { 
            p_subj      = box.getSession("s_subj");
            p_year      = box.getSession("s_year");
            p_subjseq   = box.getSession("s_subjseq");
            s_userid    = box.getSession("userid"); 
  		}
        
        // get my Branch
        //int v_mybranch=EduEtc1Bean.get_mybranch(p_subj,p_year,p_subjseq,p_userid);
        //if ( v_mybranch == 0 || v_mybranch == 99) v_mybranch = 1;
        
        String progressTableName = getProgressTableName(box);
        
        try { 
            connMgr = new DBConnectionManager();
            
            // 점수 재계산 (ALL)
            // isOk = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.ALL,p_subj,p_year,p_subjseq,p_userid);

            list = new ArrayList();

            sql =
            	"\n  SELECT   a.module, b.sdesc modulenm, a.lesson, c.sdesc, c.isbranch,  " +
            	"\n           a.OID, a.sdesc oidnm, a.ordering, d.starting,  " +
            	"\n           e.first_edu, e.first_end, e.session_time, e.total_time, e.lesson_count  " +
            	"\n      FROM tz_subjobj a, tz_subjmodule b, tz_subjlesson c, tz_object d, #progressTableName# e  " +
            	"\n     WHERE a.subj = b.subj  " +
            	"\n       AND a.subj = c.subj  " +
            	"\n       AND a.module = b.module  " +
            	"\n       AND a.module = c.module  " +
            	"\n       AND a.lesson = c.lesson  " +
            	"\n       AND a.OID = d.OID  " +
            	"\n       AND a.subj = #subj#  " +
            	"\n  	  AND e.subj(+) = a.subj  " +
            	"\n       AND e.subj(+) = a.subj  " +
            	"\n  	  AND e.year(+) = #year#  " +
            	"\n  	  AND e.subjseq(+) = #subjseq#  " +
            	"\n  	  AND e.userid(+) = #userid#  " +
            	"\n  	  AND e.lesson(+) = a.lesson  " +
            	"\n  	  AND e.oid(+) = a.oid  " +
            	"\n  ORDER BY a.module, a.lesson, a.ordering, a.OID  ";
            
            sql = sql.replaceAll( "#subj#", StringManager.makeSQL(p_subj) );
            sql = sql.replaceAll( "#year#", StringManager.makeSQL(p_year) );
            sql = sql.replaceAll( "#subjseq#", StringManager.makeSQL(p_subjseq) );
            sql = sql.replaceAll( "#userid#", StringManager.makeSQL(s_userid) );
            sql = sql.replaceAll( "#progressTableName#", progressTableName );

            	/*
            	"select a.module, b.sdesc modulenm, a.lesson, c.sdesc , c.isbranch, a.oid, a.sdesc oidnm, a.ordering, d.starting, "
                + "       (select first_edu from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson and oid=a.oid) first_edu,        "
                + "       (select first_end from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson and oid=a.oid) first_end,        "
                + "       (select session_time from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson and oid=a.oid) session_time,  "
                + "       (select total_time from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson and oid=a.oid) total_time,      "
                + "       (select lesson_count from tz_progress where subj=a.subj and " +v_str1 + " and lesson=a.lesson and oid=a.oid) lesson_count  "
                // + "       (select count(ordseq) from tz_projord where subj=a.subj and " +v_str2 + " and lesson=a.lesson) cntReport                    "
                // + "       (select count(seq) from tz_activity where subj=a.subj and lesson=a.lesson) cntAct,                                     "
                // + "       (select count(distinct seq) from tz_activity_ans where subj=a.subj and  " +v_str1 + " and lesson=a.lesson) cntMyAct       "       
                + "  from tz_subjobj a, tz_subjmodule b, tz_subjlesson c,tz_object d    "
                + " where a.subj=b.subj and a.subj=c.subj                   "
                + "   and a.module = b.module and a.module = c.module       "
                + "   and a.lesson = c.lesson                               "
                + "   and a.oid = d.oid                                     "
                + "   and a.subj=" +StringManager.makeSQL(p_subj)
                // + "   and length(d.starting) > 1                          "
                + " order by a.module, a.lesson, a.ordering, a.oid          ";
                */              
                
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                if ( !ls.getString("lesson").equals(v_lesson)) { 
                    // 평가
                    /*
                    sql = "select examtype from tz_exampaper where subj=" +SQLString.Format(p_subj)
                    + "   and year='TEST' and subjseq='TEST' and lesson=" +SQLString.Format(v_lesson)
                    + " order by ptype";
                    if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                    ls1 = connMgr.executeQuery(sql);
                    while ( ls1.next() ) { 
                        x = new EduListData();
                        x.setRecordType  ("EXAM");
                        x.setLesson      (v_lesson);    
                        x.setSdesc       ( ls.getString("sdesc") );
                        x.setModule      ( ls.getString("module") );
                        x.setModulenm    ( ls.getString("modulenm") );
                        x.setPtype( ls1.getString("ptype") );
    
                        sql = "select score,started,ended from tz_examresult  where subj=" +SQLString.Format(p_subj)
                            + "   and year=" +SQLString.Format(p_year) + " and subjseq=" +SQLString.Format(p_subjseq)
                            + "   and ptype='" +ls1.getString("ptype") + "' and userid=" +SQLString.Format(p_userid)
                            + "    and lesson=" +SQLString.Format(v_lesson);
                        if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                        ls2 = connMgr.executeQuery(sql);
                        if ( ls2.next() ) { 
                            x.setFirst_edu   ( ls2.getString("started") );
                            x.setFirst_end   ( ls2.getString("ended") );
                            x.setScore       ( ls2.getDouble("score") );
                            x.setIsEducated("Y");
                        }
                        list.add(x);
                    }
                    */

                	// list = getExamObject(connMgr,list,p_subj,p_year,p_subjseq,p_userid,v_module,v_lesson);
                    v_lesson = ls.getString("lesson");
                    v_module = ls.getString("module");
                    
                    v_isbranch = ls.getString("isbranch");
                    v_sdesc = ls.getString("sdesc");
                }   

                x = new EduListData();
                
                x.setRecordType  ("STEP");
                x.setModule      ( ls.getString("module") );
                x.setModulenm    ( ls.getString("modulenm") );
                x.setLesson      ( ls.getString("lesson") );  
                // x.setSdesc       ( ls.getString("sdesc") );
                x.setOidnm		 ( ls.getString("oidnm"));
                x.setSdesc       (v_sdesc);
                x.setOid         ( ls.getString("oid") );
                if ( v_isbranch.equals("Y") ) { 
                	// x.setOidnm(GetCodenm.get_objectBranchName(connMgr,p_subj,x.getLesson(),ls.getInt("ordering"),v_mybranch));
                } else { 
                	// x.setOidnm( ls.getString("oidnm") );
                }
                x.setIsbranch    ( ls.getString("isbranch") );
                x.setFirst_edu   ( ls.getString("first_edu") );
                x.setFirst_end   ( ls.getString("first_end") );
                x.setStarting    ( ls.getString("starting") );
                
                String str = x.getStarting();
                String str2 = x.getFirst_end();
                
                if ( str.length() <= 1) { 
                    x.setIsEducated("P");
                }
                else if ( str2.length() > 0 ) { 
                    x.setIsEducated("Y");
                }
                else { 
                    x.setIsEducated("N");
                }
                x.setSession_time( ls.getString("session_time") );
                x.setTotal_time  ( ls.getString("total_time") );
                x.setLesson_count( ls.getInt("lesson_count") );
                // x.setCntReport   ( ls.getInt("cntReport") );
                if ( x.getCntReport() > 0) { 
                    x.setCntMyReport ( get_repCnt( connMgr,p_subj,p_year,p_subjseq,x.getLesson(),s_userid ) );
                }
                           
                list.add(x);    
            }
            
            // 마지막 Lesson의 평가Object 얻기
            // list = getExamObject(connMgr,list,p_subj,p_year,p_subjseq,p_userid,v_module,v_lesson);
            
            // set row-span
            v_module= ""; v_lesson= "";
            for ( int i = 0;i<list.size();i++ ) { 
                x = (EduListData)list.get(i);
                
                if ( !v_module.equals(x.getModule() )) { 
                    v_module = x.getModule();
                    x.setRowspan(get_count(list,v_module));
                    list.set(i,x);
                }
                if ( !v_lesson.equals(x.getLesson() )) { 
                    v_lesson = x.getLesson();
                    x.setRowspan_lesson(get_count_lesson(list,x.getModule(),x.getLesson() ));
                    list.set(i,x);
                }
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
     * 진도/목차 리스트 조회 - SCORM2004
     * 
     * @param box
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList SelectEduListSCORM2004( RequestBox box ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        ArrayList list = null;
        int isOk = 0;
            
        String sql  = "";

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_subjnm = box.getString("p_subjnm");
        String v_userid = box.getSession("userid");
        
        String v_course_code = box.getString("p_course_code");
        String v_org_id = box.getString("p_org_id");
        
        String tableName = "";
        if ( v_subjseq.equals("0000") )
        {
            tableName = "tz_beta_progress";
        }
        else
        {
            tableName = "tz_progress";
        }
        
        // get my Branch
        /*
        int v_mybranch = EduEtc1Bean.get_mybranch( v_subj,v_year,v_subjseq,v_userid );
        if ( v_mybranch == 0 || v_mybranch == 99 )
        {
            v_mybranch = 1;
        }
        */
        
        try 
        { 
            connMgr = new DBConnectionManager();
            
            // 점수 재계산 (ALL)
//0928            isOk = CalcUtil.getInstance().calc_score( connMgr,CalcUtil.ALL,v_subj,v_year,v_subjseq,v_userid );

            list = new ArrayList();
            
            sql =
                "\n  SELECT                                                                           " +
                "\n     LEVEL, T.ORG_TREE_ORD, T.ORG_ID, T.ORG_TITLE, T.COURSE_CODE,                  " +
                "\n     T.ITEM_ID, T.ITEM_PID, T.ITEM_ID_REF, T.ITEM_TITLE,                           " +
                "\n     T.OBJ_ID, T.TREE_ORD, T.RES_SCORM_TYPE,                                       " +
                "\n     P.LESSONSTATUS, P.FIRST_EDU, P.FIRST_END,                                     " +
                "\n     P.SESSION_TIME, P.TOTAL_TIME, P.LESSON_COUNT                                  " +
                "\n  FROM                                                                             " +
                "\n  (                                                                                " +
                "\n     SELECT                                                                        " +
                "\n         A.TREE_ORD as ORG_TREE_ORD, A.ORG_ID, A.ORG_TITLE, A.COURSE_CODE,         " +
                "\n         B.ITEM_ID, B.ITEM_PID, B.ITEM_ID_REF, B.ITEM_TITLE, B.OBJ_ID, B.TREE_ORD, " +
                "\n         C.RES_SCORM_TYPE                                                          " +
                "\n     FROM                                                                          " +
                "\n         TYS_ORGANIZATION A, TYS_ITEM B, TYS_RESOURCE C                            " +
                "\n     WHERE 1=1                                                                     " +
                "\n         AND A.COURSE_CODE = ':course_code'                                        " +
                "\n         AND B.ORG_ID = ':org_id'                                                  " +
                "\n         AND A.COURSE_CODE = B.COURSE_CODE                                         " +
                "\n         AND A.ORG_ID = B.ORG_ID                                                   " +
                "\n         AND B.COURSE_CODE = C.COURSE_CODE(+)                                      " +
                "\n         AND B.ORG_ID = C.ORG_ID(+)                                                " +
                "\n         AND B.ITEM_ID = C.ITEM_ID(+)                                              " +
                "\n  ) T,                                                                             " +
                "\n  ( SELECT                                                                         " +
                "\n        LESSONSTATUS, FIRST_EDU, FIRST_END, OID,                                   " +
                "\n        SESSION_TIME, TOTAL_TIME, LESSON_COUNT                                     " +
                "\n     FROM " + tableName +
                "\n     WHERE 1=1                                                                     " +
                "\n         AND SUBJ=':subj'                                                          " +
                "\n         AND YEAR=':year'                                                          " +
                "\n         AND SUBJSEQ=':seq'                                                        " +
                "\n         AND LESSON = ':course_code' || '_' || ':org_id'                           " +
                "\n         AND USERID=':userid'                                                      " +
                "\n  ) P                                                                              " +
                "\n  WHERE                                                                            " +
                "\n      T.ITEM_ID = P.OID(+)                                                         " +
                "\n  START WITH                                                                       " +
                "\n     ITEM_PID = ':org_id'                                                          " +
                "\n  CONNECT BY                                                                       " +
                "\n     PRIOR ITEM_ID = ITEM_PID                                                      " +
                "\n  ORDER SIBLINGS BY TREE_ORD                                                       ";

            sql = sql.replaceAll( ":course_code", v_course_code );
            sql = sql.replaceAll( ":org_id", v_org_id );
            sql = sql.replaceAll( ":subj", v_subj );
            sql = sql.replaceAll( ":year", v_year );
            sql = sql.replaceAll( ":seq", v_subjseq );
            sql = sql.replaceAll( ":userid", v_userid );
            
            ls = connMgr.executeQuery( sql );
            
            while ( ls.next() )
            {
                list.add( ls.getDataBox() );
            }
            
            
        }
        catch ( Exception ex ) 
        {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } 
        finally 
        {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }   

    /**
     * 학습시간, 최근학습일, 강의실접근횟수 - OBC
     * @param box          receive from the form object and session
     * @return ArrayList  
     */      
    public ArrayList SelectEduTimeCountOBC(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String      sql     = "";
        EduListData edudata = null;

        String p_subj, p_year, p_subjseq, p_userid;
        String etime = "";
        
        p_subj      = box.getString("p_subj");
        p_year      = box.getString("p_year");
        p_subjseq   = box.getString("p_subjseq");
        p_userid    = box.getString("p_userid");

        if ( p_userid.equals("") ) { 
            p_userid    = box.getSession("userid");
        }

        if ( p_subj.equals("")||p_year.equals("")||p_subjseq.equals("") ) { 
            p_subj      = box.getSession("s_subj");
            p_year      = box.getSession("s_year");
            p_subjseq   = box.getSession("s_subjseq");
            p_userid    = box.getSession("userid"); 
        }
        
        String progressTableName = getProgressTableName(box);
        
        try { 
            connMgr = new DBConnectionManager();
            
            sql =
            	"\n  SELECT TRUNC (  " +
            	"\n           ( SUM (TO_NUMBER (SUBSTR (total_time, 1, (INSTR (total_time, ':', 1, 1) - 1)) )) * 60 * 60 +   " +
            	"\n             SUM (TO_NUMBER (SUBSTR (total_time, (INSTR (total_time, ':', 1, 1) + 1), 2) )) * 60 +  " +
            	"\n  	       SUM (TO_NUMBER (SUBSTR (total_time, (INSTR (total_time, ':', 1, 2) + 1), 2) ))   " +
            	"\n  		 ) / (60 * 60), 0 ) total_time,  " +
            	"\n         TRUNC ( MOD (  " +
            	"\n           ( SUM (TO_NUMBER (SUBSTR (total_time, 1, (INSTR (total_time, ':', 1, 1) - 1)) )) * 60 * 60 +   " +
            	"\n             SUM (TO_NUMBER (SUBSTR (total_time, (INSTR (total_time, ':', 1, 1) + 1), 2) )) * 60 +  " +
            	"\n  	       SUM (TO_NUMBER (SUBSTR (total_time, (INSTR (total_time, ':', 1, 2) + 1), 2) ))   " +
            	"\n  		 ) / 60, 60), 0 ) total_minute,  " +
            	"\n         MOD (  " +
            	"\n             SUM (TO_NUMBER (SUBSTR (total_time, 1, (INSTR (total_time, ':', 1, 1) - 1)) ) * 60 * 60 +   " +
            	"\n             TO_NUMBER (SUBSTR (total_time, (INSTR (total_time, ':', 1, 1) + 1), 2) ) * 60 +  " +
            	"\n  	       TO_NUMBER (SUBSTR (total_time, (INSTR (total_time, ':', 1, 2) + 1), 2) ) )  " +
            	"\n  		   , 60 ) total_sec,		 		   " +
            	"\n  	   MAX(substr(first_end,1,8)) edudt,  " +
            	"\n  	   SUM(lesson_count) count  " +
            	"\n    FROM #progressTableName#  " +
            	"\n   WHERE subj = #subj#  " +
            	"\n     AND YEAR = #year#  " +
            	"\n     AND subjseq = #subjseq#  " +
            	"\n     AND userid = #userid#  ";

            sql = sql.replaceAll( "#subj#", StringManager.makeSQL(p_subj) );
            sql = sql.replaceAll( "#year#", StringManager.makeSQL(p_year) );
            sql = sql.replaceAll( "#subjseq#", StringManager.makeSQL(p_subjseq) );
            sql = sql.replaceAll( "#userid#", StringManager.makeSQL(p_userid) );
            sql = sql.replaceAll( "#progressTableName#", progressTableName );
            
            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) {     
                edudata = new EduListData();        

                etime = ls.getString("total_time") + " 시간 " +ls.getString("total_minute") + " 분 " +ls.getString("total_sec") + " 초 ";                           
                edudata.setTotal_time     (etime);                   	// 학습시간            
                edudata.setFirst_edu      ( ls.getString("edudt") );	// 최근학습일          
                edudata.setLesson_count   ( ls.getInt("count") );		// 강의실접근횟수 

                list.add(edudata);
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
    
        
    /** 평가 Object 얻기 **/
    public ArrayList    getExamObject(  DBConnectionManager connMgr, 
                                        ArrayList   list, 
                                        String      p_subj,
                                        String      p_year,
                                        String      p_subjseq,
                                        String      p_userid,
                                        String      p_module,
                                        String      p_lesson) { 
        ListSet ls = null,ls1= null,ls2= null;
        ArrayList list2 = list;
        String sql  = "", sql2= "";
        EduListData x = null;

        // set row-span
        String v_module= "";
        
        try { 
                    sql = "select examtype from tz_exampaper where subj=" +SQLString.Format(p_subj)
                    + "   and year=" +SQLString.Format(p_year)
                    + "   and subjseq=" +SQLString.Format(p_subjseq)
                    + "   and lesson=" +SQLString.Format(p_lesson)
                    + " order by examtype";
                    if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                    ls1 = connMgr.executeQuery(sql);
                    while ( ls1.next() ) { 
                        x = new EduListData();
                        x.setRecordType  ("EXAM");
                        x.setLesson      (p_lesson);    
                        x.setSdesc       (p_lesson);
                        x.setModule      (p_module);
                        x.setModulenm    (p_module);
                        x.setPtype( ls1.getString("ptype") );
    
                        sql = "select score,started,ended from tz_examresult  where subj=" +SQLString.Format(p_subj)
                            + "   and year=" +SQLString.Format(p_year) + " and subjseq=" +SQLString.Format(p_subjseq)
                            + "   and examtype='" +ls1.getString("ptype") + "' and userid=" +SQLString.Format(p_userid)
                            + "    and lesson=" +SQLString.Format(p_lesson);
                        if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                        ls2 = connMgr.executeQuery(sql);
                        if ( ls2.next() ) { 
                            x.setFirst_edu   ( ls2.getString("started") );
                            x.setFirst_end   ( ls2.getString("ended") );
                            x.setScore       ( ls2.getDouble("score") );
                            x.setIsEducated("Y");
                        }
                        list2.add(x);
                    }
        } catch ( Exception ex ) { 
            System.out.println("ERROR=" +ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
        }

        return list2;
    }
    
    /** 모듈 Rowspan 얻기 **/       
    public int  get_count(ArrayList list,String p_module) { 
        int v_cnt=0;
        EduListData x;
        for ( int i = 0;i<list.size();i++ ) { 
            x = (EduListData)list.get(i);
            if ( x.getModule().equals(p_module))
                v_cnt++;
        }

        return v_cnt;
    }
    
    /** Lesson Rowspan 얻기 **/
    public int  get_count_lesson(ArrayList list,String p_module, String p_lesson) { 
        int v_cnt=0;
        EduListData x;
        for ( int i = 0;i<list.size();i++ ) { 
            x = (EduListData)list.get(i);
            if ( x.getModule().equals(p_module) && x.getLesson().equals(p_lesson))
                v_cnt++;
        }

        return v_cnt;
    }   
    /**
    과제 제출건수 Return
    @param DBConnectionManager  
    @param String  과목,년도,기수,레슨,id
    @return String  제출한 과제건수
        p_lesson : 'ALL'이면 과목기수전체 과제제출건수(중복제출 무시)
                   'ALL'아니면 해당 레슨의 과제제출건수(중복제출 무시)
    */  
    private int get_repCnt(   DBConnectionManager connMgr,
                                    String  p_subj,
                                    String  p_year,
                                    String  p_subjseq,
                                    String  p_lesson,
                                    String  p_userid ) throws Exception{ 

        ListSet ls = null, ls1= null;
        String sql  = "", v_reptype, v_projgrp=p_userid;
        int resulti =0, v_ordseq=0;
        
        try { 
            sql = " select  lesson,ordseq,reptype  from tz_projord where subj=" +SQLString.Format(p_subj);
            if ( !p_lesson.equals("ALL")) sql +=" and lesson=" +SQLString.Format(p_lesson);
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_reptype = ls.getString("reptype");
                v_ordseq  = ls.getInt("ordseq");
                if ( v_reptype.equals("P") ) { // Project이면 tz_projgrp에서 get projgrp
                     sql = "select projgrp from tz_projgrp where subj=" +SQLString.Format(p_subj)
                         + "   and year=" +SQLString.Format(p_subj) + " and subjseq=" +SQLString.Format(p_subjseq)
                         + "   and ordseq=" +v_ordseq + " and userid=" +SQLString.Format(p_userid);
                    if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                    ls1 = connMgr.executeQuery(sql);
                    if ( ls1.next() )  v_projgrp = ls1.getString("projgrp");
                    else            return 0;
                }
                sql = "select count(distinct ordseq) CNTS from tz_projrep where subj=" +SQLString.Format(p_subj)
                     + "   and year=" +SQLString.Format(p_subj) + " and subjseq=" +SQLString.Format(p_subjseq)
                     + "   and ordseq=" +v_ordseq + " and projid=" +SQLString.Format(v_projgrp);
                if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                ls1 = connMgr.executeQuery(sql);
                ls1.next();
                return ls1.getInt("CNTS");
            }
            
        } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
        }

        return resulti;
    }   
    /**
    학습점수정보  Return
    @param DBConnectionManager  
    @param String  과목,년도,기수,id
    @return String  
    */  
    public EduScoreData SelectEduScore(RequestBox box) throws Exception {                
        DBConnectionManager	connMgr	= null;
        ListSet ls = null,ls1= null,ls2= null;
        ArrayList list = null, list2= null;
        String sql  = "", sql2= "";
        EduScoreData x = null;

        String  p_subj, p_year, p_subjseq, p_userid;
        String  v_contenttype= "";
        int isOk = 0;

        String  p_isFromLMS = box.getString("p_isFromLMS");

        /*if ( p_isFromLMS.equals("Y") ) {     
            p_subj      = box.getSession("s_subj");
            p_year      = box.getSession("s_year");
            p_subjseq   = box.getSession("s_subjseq");
            p_userid    = box.getSession("userid");         
        } else { 
            p_subj      = box.getString("p_subj");
            p_year      = box.getString("p_year");
            p_subjseq   = box.getString("p_subjseq");
            p_userid    = box.getString("p_userid");
        }*/
        
        p_subj      = box.getString("p_subj");    
        p_year      = box.getString("p_year");    
        p_subjseq   = box.getString("p_subjseq"); 
        p_userid    = box.getString("p_userid");  
 
        if ( p_userid.equals("")) p_userid    = box.getSession("userid");
        if ( p_subj.equals("")||p_year.equals("")||p_subjseq.equals("") ) { 
            p_subj      = box.getSession("s_subj");        
            p_year      = box.getSession("s_year");        
            p_subjseq   = box.getSession("s_subjseq");     
            p_userid    = box.getSession("userid");      
        }
        
        
        try { 
            connMgr = new DBConnectionManager();
            
//0928           	isOk = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.ALL,p_subj,p_year,p_subjseq,p_userid);

            
            sql = "\n	select a.score, a.tstep, a.htest, a.mtest, a.ftest,				"
            	+ "\n		a.report, a.act, a.etc1, a.etc2, a.avtstep, 				"
            	+ "\n		a.avhtest, a.avmtest, a.avftest, a.avreport, a.avact,		"
            	+ "\n		a.avetc1, a.avetc2, b.gradscore, b.gradstep, b.gradexam, 	"
            	+ "\n		b.gradhtest, b.gradftest, b.gradreport, b.wstep, b.wmtest, 	"
                + "\n		b.wftest, b.whtest, b.wreport, b.wact, b.wetc1, b.wetc2, 	"
                + "\n		b.gradftest_flag, b.gradhtest_flag, b.gradexam_flag, b.gradreport_flag, b.gradstep, "
                + "\n		a.isgraduated, b.edustart edustart, b.eduend eduend, b.study_count	"
                + "\n     , (												"
                + "\n			select count(*) cnt 						"
                + "\n			from tz_subjloginid							"
                + "\n			where subj = a.subj							"
                + "\n			and year = a.year							"
                + "\n 		and subjseq= a.subjseq							"
                + "\n			and userid = a.userid						"	
                + "\n		) cnt											"
                + "\n     , decode(c.userid, null, 'N', 'Y') as realgraduated, b.isclosed,a.editscore "
                + "\n	from tz_student a, tz_subjseq b, tz_stold c "
                + "\n	where a.subj=" +StringManager.makeSQL(p_subj)
                + "\n		and a.year	=" +StringManager.makeSQL(p_year)
                + "\n		and a.subjseq	=" +StringManager.makeSQL(p_subjseq)
                + "\n		and a.userid	=" +StringManager.makeSQL(p_userid)
                + "\n		and a.subj	=b.subj 				"
                + "\n		and a.year=b.year 					"
                + "\n		and a.subjseq=b.subjseq				"
                + "\n		and a.subj=c.subj(+)				"
                + "\n		and a.year=c.year(+)				"
                + "\n		and a.subjseq=c.subjseq(+)			"
                + "\n		and a.userid=c.userid(+)			";
            
            //System.out.println("sql:::::::"+sql);

            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) { 
                x = new EduScoreData();
                x.setScore           ( ls.getDouble("score") );
                x.setTstep           ( ls.getDouble("tstep") );                
                x.setMtest           ( ls.getDouble("mtest") );
                x.setFtest           ( ls.getDouble("ftest") );
                x.setHtest           ( ls.getDouble("htest") );
                x.setReport          ( ls.getDouble("report") );
                x.setAct             ( ls.getDouble("act") );
                x.setEtc1            ( ls.getDouble("etc1") );
                x.setEtc2            ( ls.getDouble("etc2") );
                x.setAvtstep         ( ls.getDouble("avtstep") );
                x.setAvhtest         ( ls.getDouble("avhtest") );
                x.setAvmtest         ( ls.getDouble("avmtest") );
                x.setAvftest         ( ls.getDouble("avftest") );
                x.setAvreport        ( ls.getDouble("avreport") );
                x.setAvact           ( ls.getDouble("avact") );
                x.setAvetc1          ( ls.getDouble("avetc1") );
                x.setAvetc2          ( ls.getDouble("avetc2") );
                x.setGradscore       ( ls.getDouble("gradscore") );
                x.setGradstep        ( ls.getDouble("gradstep") );
                x.setGradexam        ( ls.getDouble("gradexam") );
                x.setGradhtest       ( ls.getDouble("gradhtest") );
                x.setGradftest       ( ls.getDouble("gradftest") );
                x.setGradreport      ( ls.getDouble("gradreport") );
                x.setWstep           ( ls.getDouble("wstep") );
                x.setWmtest          ( ls.getDouble("wmtest") );
                x.setWftest          ( ls.getDouble("wftest") );
                x.setWhtest          ( ls.getDouble("whtest") );
                x.setWreport         ( ls.getDouble("wreport") );
                x.setWact            ( ls.getDouble("wact") );
                x.setWetc1           ( ls.getDouble("wetc1") );
                x.setWetc2           ( ls.getDouble("wetc2") );
                x.setIsgraduated     ( ls.getString("isgraduated") ); 
                x.setEdustart        ( ls.getString("edustart") );
                x.setEduend          ( ls.getString("eduend") );
                
                x.setGradftest_flag  ( ls.getString("gradftest_flag") ); 
                x.setGradhtest_flag  ( ls.getString("gradhtest_flag") );
                x.setGradexam_flag   ( ls.getString("gradexam_flag") );
                x.setGradreport_flag ( ls.getString("gradreport_flag") ); 
                
                x.setCnt(ls.getInt("cnt"));
                x.setStudyCount(ls.getInt("study_count"));
                x.setRealgraduated(ls.getString("realgraduated"));
                x.setIsClosed(ls.getString("isclosed")); 
                x.setEditscore(ls.getDouble("editscore"));
                                
                x.makeScoreList();              // 가중치별 점수 HashTable생성           
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return x;
    }
    
    
    /**
    학습점수정보  Return
    @param DBConnectionManager  
    @param String  과목,년도,기수,id
    @return String  
    */  
    public ArrayList SelectEduScore2(RequestBox box) throws Exception {                
        DBConnectionManager	connMgr	= null;
        ListSet ls        = null;
        ArrayList list    = null;
        String sql        = "";
        DataBox dbox = null;

        String  p_subj, p_year, p_subjseq, p_userid;
             
        p_subj      = box.getString("p_subj");    
        p_year      = box.getString("p_year");    
        p_subjseq   = box.getString("p_subjseq"); 
        p_userid    = box.getString("p_userid");  
              
        
        try { 
            connMgr = new DBConnectionManager();
            
            list = new ArrayList();
            
            sql=" select \n";
            sql+="gradexam,gradftest,gradreport,wetc1\n";
            sql+=",(select avmtest from tz_student b where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq and b.userid='"+p_userid+"' ) as mmtest \n";
            sql+=",(select avftest from tz_student b where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq and b.userid='"+p_userid+"' ) as mftest \n";
	        sql+=",(select avreport from tz_student b where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq and b.userid='"+p_userid+"' ) as mreport \n";
		    sql+=",(select avetc2 from tz_student b where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq and b.userid='"+p_userid+"' ) as mect2 \n";
			sql+=",(select avact from tz_student b where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq and b.userid='"+p_userid+"' ) as mact2 \n";
			sql+=",(select score from tz_student b where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq and b.userid='"+p_userid+"' ) as mscore \n";
			sql+=",(select editscore from tz_student b where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq and b.userid='"+p_userid+"' ) as meditscore \n";
			sql+=",wmtest,wftest ,wreport,wetc2 \n";
			sql+=",(select edutimes from tz_subj b where a.subj = b.subj ) as edutimes \n";
			sql+="from   \n";   
			sql+="tz_subjseq a   \n";
			sql+="where a.subj='"+p_subj+"' \n";
			sql+="and a.year    ='"+p_year+"' \n";
			sql+="and a.subjseq    ='"+p_subjseq+"'  \n";
            	 
           
            
   //         System.out.println("sql:::::::"+sql);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            	dbox = ls.getDataBox();
            	  dbox.put("d_gradexam", ls.getDouble("gradexam"));
            	  dbox.put("d_gradftest", ls.getDouble("gradftest"));
            	  dbox.put("d_gradreport", ls.getDouble("gradreport"));
            	  dbox.put("d_wetc1", ls.getDouble("wetc1"));
            	  dbox.put("d_mmtest", ls.getDouble("mmtest"));
            	  dbox.put("d_mftest", ls.getDouble("mftest"));
            	  dbox.put("d_mreport", ls.getDouble("mreport"));
            	  dbox.put("d_mect2", ls.getDouble("mect2"));
            	  dbox.put("d_mact2", ls.getDouble("mact2"));
            	  dbox.put("d_mscore", ls.getDouble("mscore"));
            	  dbox.put("d_meditscore", ls.getDouble("meditscore"));
            	  
            	//  System.out.println("========dbox=================="+dbox);
            	  
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
    차시별 분기List  Return
    @param DBConnectionManager  
    @param String  과목,년도,기수,id
    @return String  
    */  
    public ArrayList SelectEduBranch(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        MfBranchData x = null;

        String  p_subj, p_year, p_subjseq, p_userid;
        
        p_subj      = box.getSession("s_subj");
        p_year      = box.getSession("s_year");
        p_subjseq   = box.getSession("s_subjseq");
        p_userid    = box.getSession("userid");
        int p_branch = box.getInt("p_branch");
        int v_mybranch =0, isOk=0;

        try { 
            connMgr = new DBConnectionManager();
            
            
            // 학습자의 분기설정여부 체크, 학습이지만 분기정보 없으면 p_branch로 tz_student update..
            sql = "select nvl(branch,99) branch from tz_student where subj=" +StringManager.makeSQL(p_subj)
                + "   and year=" +StringManager.makeSQL(p_year)
                + "   and subjseq=" +StringManager.makeSQL(p_subjseq)
                + "   and userid=" +StringManager.makeSQL(p_userid);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
                v_mybranch = ls.getInt("branch");
                if ( v_mybranch == 99 && box.getString("p_gubun").equals("CONF") ) { 
                    sql = "update tz_student set branch=" +p_branch
                        + " where subj=" +StringManager.makeSQL(p_subj)
                        + "   and year=" +StringManager.makeSQL(p_year)
                        + "   and subjseq=" +StringManager.makeSQL(p_subjseq)
                        + "   and userid=" +StringManager.makeSQL(p_userid);
                    isOk = connMgr.executeUpdate(sql);
                    if ( isOk != 1) { 
                        // makeLog..
                        isOk=1;
                    }
                }
            }
    
            sql = "select a.lesson LESSON,a.sdesc LESSONNM,b.sdesc BRNAME,b.starting STARTING "
                + "  from tz_subjlesson a, tz_subjbranch b "
                + " where a.subj=b.subj and a.lesson=b.lesson "
                + "   and a.isbranch='Y'  and b.branch=" +p_branch
                + "   and a.subj=" +StringManager.makeSQL(p_subj)
                + " order by a.lesson ";
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }        
            ls = connMgr.executeQuery(sql);
            list = new ArrayList();
            while ( ls.next() ) { 
                x = new MfBranchData();
                x.setLesson     ( ls.getString("LESSON") );
                x.setLessonnm   ( ls.getString("LESSONNM") );
                x.setSdesc      ( ls.getString("BRNAME") );
                x.setStarting   ( ls.getString("STARTING") );
                list.add(x);
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
    [OBC] 마스터폼 Tree Data 리스트
    @param box          receive from the form object and session
    @return ArrayList   OBCTreeData리스트
    */
    public ArrayList SelectTreeDataList(RequestBox box) throws Exception {               
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ListSet ls1 = null;

        ArrayList list1 = null;
        String sql  = "";
        OBCTreeData d = null;
        int j = 1;
    
        String  s_subj      = box.getSession("s_subj");
        String  s_year      = box.getSession("s_year");
        String  s_subjseq   = box.getSession("s_subjseq");
        String  s_userid    = box.getSession("userid");
        String  p_tmp1      = box.getString("p_tmp1");

        // Branch 과정 일 경우 사용 되는 변수 
        // int p_branch    = 0;
        // p_branch = box.getInt("p_branch");
        // String  f_branchsubj= "", v_lesson_brname= "";
        // int v_mybranch  = EduEtc1Bean.get_mybranch(s_subj, s_year, s_subjseq, s_userid);
        // int v_curbranch = 99;
        // String v_extype = "", v_tmp= "", v_ptypenm= "", v_brtxt= "";       
        
        String v_module = "";
        String v_lesson = "";
        String v_oid = "";
        String v_types = "1001";
        
        String  f_exist = "N";
        boolean f_go = true;
        
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql =
                "\n  SELECT                                                           " +
                "\n     a.module module, a.sdesc modulenm,                            " +
                "\n     b.lesson lesson, b.sdesc lessonnm,                            " +
                "\n        NVL (b.TYPES, '1001') TYPES, NVL (a.TYPES, '1001') mtypes, " +
                "\n        NVL (b.isbranch, 'N') isbranch                             " +
                "\n  FROM tz_subjmodule a, tz_subjlesson b                            " +
                "\n  WHERE 1=1                                                        " +
                "\n     AND a.subj = " + StringManager.makeSQL(s_subj) +
                "\n     AND a.module = b.module(+)                                    " +
                "\n     AND a.subj = b.subj(+)                                        " +
                "\n  ORDER BY a.module, b.lesson                                      ";
            
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {       // ##1  
                if ( !ls.getString("lesson").equals("") )
                {
                    String delimeter = "||";
                    
                    if ( !ls.getString("module").equals(v_module)) { 
                        v_module = ls.getString("module");
    
                        d = new OBCTreeData();
                        // Paramter Rule = Index||유형(MODULE,LESSON,SC)||목차명||Type(1001,1002,1003)||Mode(SC,TM,TT)||진도여부(Lesson_OID)
                        d.setAp ( j + delimeter + "MODULE" + delimeter + ls.getString("modulenm") 
                                + delimeter + ls.getString("MTYPES") + delimeter + "SC" + delimeter  + delimeter + "Y");
                        d.setAh ( v_module + "," +ls.getString("lesson") + ",MODULE1234,MO,SC,MODULE,0,N,XXX");
                        
                        list1.add(d);
                        j++;
                    }
            
                    f_go = true;            // Tree 출력 허용
            
                    if ( !ls.getString("lesson").equals(v_lesson)) {  
                        v_lesson = ls.getString("lesson");
                        
                        d = new OBCTreeData();
                        d.setAp ( j + delimeter + "LESSON" + delimeter + ls.getString("lessonnm")
                                + delimeter + ls.getString("TYPES") + delimeter + "SC" + delimeter  + delimeter + "Y");
                        d.setAh ( v_module + "," +v_lesson + ",LESSON1234,LE,SC,LESSON,0,N,XXX");
                        
                        list1.add(d);
                        j++;
                    }
            
                    if ( f_go) {
                        
                        sql =
                            "\n  SELECT                                                               " +
                            "\n        a.TYPE TYPE, a.OID OID, a.sdesc sdesc, a.ordering ordering,    " +
                            "\n           b.starting starting, NVL (a.TYPES, '1001') TYPES,           " +
                            "\n           b.filetype filetype, b.npage npage                          " +
                            "\n  FROM tz_subjobj a, tz_object b                                       " +
                            "\n  WHERE 1 = 1                                                          " +
                            "\n    AND a.subj = " + StringManager.makeSQL(s_subj) +
                            "\n       AND a.OID = b.OID                                               " +
                            "\n       AND a.module = " + StringManager.makeSQL(v_module) +
                            "\n       AND a.lesson = " + StringManager.makeSQL(v_lesson) +
                            "\n       AND a.TYPE IN ('SC', 'TM', 'TT')                                " +
                            "\n  ORDER BY ordering ASC                                                ";
                        
                        if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                        ls1 = connMgr.executeQuery(sql);
                        
                        while ( ls1.next() ) {
                            v_oid = ls1.getString("OID");
                            v_types = ls1.getString("TYPES");
                            
                            /*
                            sql =
                                "\r\n  SELECT COUNT(*) cnts         " +
                                "\r\n  FROM tz_progress             " +
                                "\r\n  WHERE 1 = 1                  " +
                                "\r\n     AND subj = " + StringManager.makeSQL(s_subj) +
                                "\r\n     AND YEAR = " + StringManager.makeSQL(s_year) +
                                "\r\n     AND subjseq = " + StringManager.makeSQL(s_subjseq) +
                                "\r\n     AND userid =  " + StringManager.makeSQL(s_userid) +
                                "\r\n     AND lesson =  " + StringManager.makeSQL(v_lesson) +
                                "\r\n     AND OID = " + StringManager.makeSQL(v_oid) +
                                "\r\n     AND first_end IS NOT NULL ";
                                
                            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                            ls2 = connMgr.executeQuery(sql);
                            ls2.next();
                            
                            if ( ls2.getInt("CNTS") > 0)
                                f_exist = "Y";
                            else
                                f_exist = "N";
                            */

                            d = new OBCTreeData();
                                	
                            d.setAp ( j + delimeter + "SC" + delimeter + ls1.getString("SDESC") 
                                        + delimeter + v_types + delimeter + "SC" + delimeter + v_lesson + "_" + v_oid + delimeter + "Y");//this.getIsProgress(s_subj, s_year, s_subjseq, s_userid, v_module, v_lesson, v_oid)  );
//                            d.setAp ( j + delimeter + "SC" + delimeter + ls1.getString("SDESC") 
//                                    + delimeter + v_types + delimeter + "SC" + delimeter + v_lesson + "_" + v_oid );
                            d.setAh ( v_module + "," +v_lesson + "," +v_oid + ",SC,SC," +ls1.getString("FILETYPE") + "," +ls1.getInt("NPAGE") + "," +f_exist + "," +ls1.getString("STARTING") + ",NOT");
                            
                            list1.add(d);
                            j++;
                        }
                    }       // end if f_go == true
                }
            }         
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    /**
     * 이전 학습 여부 가져오기
     * @param box receive from the form object and session
     * @return Stringt
     * @throws Exception
     */    
    public String getIsProgress(String subj, String year, String subjseq, String userid, String module, String lesson, String oid) throws Exception {
        
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String isabled = "N";
        String sql = "";    
        
        String tablename = "tz_progress";
        if("0000".equals(subjseq)) {
        	tablename = "tz_betaprogress";
        }
        
        try { 
            connMgr = new DBConnectionManager();
            sql = "SELECT count(*) cnt,case when count(*) = 0 then 'Y' else 'N' end isabled		\n"
            	+ "FROM tz_subjmodule a left outer join tz_subjlesson b	\n"
            	+ "ON a.module = b.module								\n"                                      
            	+ "AND a.subj = b.subj									\n"                                           
            	+ "left outer join tz_subjobj c							\n"                            
            	+ "on b.subj = c.subj									\n"
            	+ "and b.module = c.module								\n"
            	+ "and b.lesson = c.lesson								\n"
            	+ "left outer join " + tablename + " d					\n"
            	+ "on c.subj = d.subj									\n"
            	+ "and c.lesson = d.lesson								\n"
            	+ "and c.oid = d.oid									\n"
            	+ "WHERE 1=1											\n"                                                        
            	+ "AND a.subj = " + StringManager.makeSQL(subj) + "		\n"
            	+ "AND d.year = " + StringManager.makeSQL(year) + "		\n"
            	+ "and d.subjseq = " + StringManager.makeSQL(subjseq) + " \n"
            	+ "and d.userid = " + StringManager.makeSQL(userid) + "  \n"
            	+ "and to_number( b.module || b.lesson || c.ordering) < (	\n"
            	+ "	select to_number(module || lesson || ordering)	\n" 
            	+ "	from tz_subjobj										 \n"
            	+ "	where subj = " + StringManager.makeSQL(subj) + "	\n"
            	+ "	and module = " + StringManager.makeSQL(module) + "	\n"
            	+ "	and lesson = " + StringManager.makeSQL(lesson) + "	\n"
            	+ "	and oid = " + StringManager.makeSQL(oid) + "    	\n"
            	+ ")                                                    \n"
            	+ "and first_end is null	                            \n";
            
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  "+sql);
            
            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() )
            {
            	isabled = ls.getString("isabled");
            }
            ls.close();
            
        }
        catch ( Exception ex )
        { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isabled;
    }
    
    /**
    [OBC] 마스터폼 맛보기 Tree Data 리스트
    @param box          receive from the form object and session
    @return ArrayList   OBCTreeData리스트
    */
    public ArrayList SelectTreeDataListPreview(RequestBox box) throws Exception {
        DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        ListSet ls1 = null;
        
        ArrayList list1 = null;
        String sql  = "";
        OBCTreeData d = null;
        int j = 1;
    
        String  s_subj      = box.getSession("s_subj");
        String  s_year      = box.getSession("s_year");
        String  s_subjseq   = box.getSession("s_subjseq");
        String  s_userid    = box.getSession("userid");
        String  p_tmp1      = box.getString("p_tmp1");

        // int p_branch    = 0;
        // p_branch = box.getInt("p_branch");
        // String  f_branchsubj= "", v_lesson_brname= "";
        // int v_mybranch  = EduEtc1Bean.get_mybranch(s_subj, s_year, s_subjseq, s_userid);
        // int v_curbranch = 99;
        
        String  v_module = "", v_lesson= "", v_oid= "";
        String  f_exist = "N", v_types = "1001";
        String v_extype = "", v_tmp= "", v_ptypenm= "", v_brtxt= "";       
        boolean f_go = true;
        
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            
            sql =
                "\n  SELECT                                                                       " +
                "\n        a.module module, a.sdesc modulenm, b.lesson lesson, b.sdesc lessonnm,  " +
                "\n           NVL (b.TYPES, '1001') TYPES, NVL (a.TYPES, '1001') mtypes,          " +
                "\n           NVL (b.isbranch, 'N') isbranch                                      " +
                "\n  FROM tz_subjmodule a,                                                        " +
                "\n       tz_subjlesson b,                                                        " +
                "\n       (SELECT distinct a.subj, a.module, a.lesson                             " +
                "\n        FROM tz_subjobj a, tz_previewobj b                                     " +
                "\n        WHERE a.subj = b.subj AND a.OID = b.OID AND a.subj = " + StringManager.makeSQL(s_subj) + ") c " +
                "\n  WHERE 1 = 1                                                                  " +
                "\n    AND a.subj = " + StringManager.makeSQL(s_subj) +
                "\n       AND a.module = b.module(+)                                              " +
                "\n       AND a.subj = b.subj(+)                                                  " +
                "\n       AND b.subj = c.subj                                                     " +
                "\n       AND b.module = c.module                                                 " +
                "\n       AND b.lesson = c.lesson                                                 " +
                "\n  ORDER BY a.module, b.lesson                                                  ";

            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            ls = connMgr.executeQuery(sql);
            logger.debug( "맛보기 SQL : \n" + sql );
            
            while ( ls.next() ) {       // ##1  
                if ( !ls.getString("lesson").equals("") )
                {
                    String delimeter = "||";
                    
                    if ( !ls.getString("module").equals(v_module)) { 
                        v_module = ls.getString("module");
    
                        d = new OBCTreeData();
                        // Paramter Rule = Index||유형(MODULE,LESSON,SC)||목차명||Type(1001,1002,1003)||Mode(SC,TM,TT)||진도여부
                        d.setAp ( j + delimeter + "MODULE" + delimeter + ls.getString("modulenm") 
                                + delimeter + ls.getString("MTYPES") + delimeter + "SC" + delimeter );
                        d.setAh ( v_module + "," +ls.getString("lesson") + ",MODULE1234,MO,SC,MODULE,0,N,XXX");
                        
                        list1.add(d);
                        j++;
                    }
            
                    f_go = true;            // Tree 출력 허용
            
                    if ( !ls.getString("lesson").equals(v_lesson)) {  
                        v_lesson = ls.getString("lesson");
                        
                        d = new OBCTreeData();
                        d.setAp ( j + delimeter + "LESSON" + delimeter + ls.getString("lessonnm")
                                + delimeter + ls.getString("TYPES") + delimeter + "SC" + delimeter );
                        d.setAh ( v_module + "," +v_lesson + ",LESSON1234,LE,SC,LESSON,0,N,XXX");
                        
                        list1.add(d);
                        j++;
                    }
            
                    if ( f_go) {
                        
                        sql =
                            "\n  SELECT  " +
                            "\n        a.TYPE TYPE, a.OID OID, a.sdesc sdesc, a.ordering ordering,  " +
                            "\n           b.starting starting, NVL (a.TYPES, '1001') TYPES,  " +
                            "\n           b.filetype filetype, b.npage npage  " +
                            "\n  FROM tz_subjobj a, tz_object b, tz_previewobj c  " +
                            "\n  WHERE 1 = 1  " +
                            "\n    AND a.subj = " + StringManager.makeSQL(s_subj) +
                            "\n       AND a.module = " + StringManager.makeSQL(v_module) +
                            "\n       AND a.lesson = " + StringManager.makeSQL(v_lesson) +
                            "\n       AND a.TYPE IN ('SC', 'TM', 'TT')  " +
                            "\n       AND a.OID = b.OID  " +
                            "\n	      AND a.subj = c.subj  " +
                            "\n	      AND a.OID = c.OID  " +
                            "\n  ORDER BY ordering ASC  ";
                        
                        if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                        ls1 = connMgr.executeQuery(sql);
                        
                        while ( ls1.next() ) { 
                            v_oid = ls1.getString("OID");
                            v_types = ls1.getString("TYPES");

                            d = new OBCTreeData();
                            d.setAp ( j + delimeter + "SC" + delimeter + ls1.getString("SDESC") 
                                    + delimeter + v_types + delimeter + "SC" + delimeter );
                            d.setAh ( v_module + "," +v_lesson + "," +v_oid + ",SC,SC," +ls1.getString("FILETYPE") + "," +ls1.getInt("NPAGE") + "," +f_exist + "," +ls1.getString("STARTING") + ",NOT");
                            
                            list1.add(d);
                            j++;
                        }
                    }       // end if f_go == true
                }
            }        
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    /**
    과목 제한 차시 조회
    @param box  receive from the form object and session
    @return ArrayList   Object리스트
    */      
    public int SelectEudLimit(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;        
        String sql  = "";      
        int edulimit = 0;

        String p_subj = box.getString("p_subj");
        String p_year = box.getString("p_year");
        String p_subjseq = box.getString("p_subjseq");
        String p_userid = box.getString("p_userid");
        
        try { 
            connMgr = new DBConnectionManager();        

            sql = "select nvl(edulimit,0) limit from tz_subjseq  "
                + " where subj= " + StringManager.makeSQL(p_subj)   
                + "   and year= " + StringManager.makeSQL(p_year)
                + "   and subjseq= " + StringManager.makeSQL(p_subjseq);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                edulimit = ls.getInt("limit");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return edulimit;
    }

    /**
    Oid 갯수 조회
    @param box  receive from the form object and session
    @return ArrayList   Object리스트
    */      
    public int SelectOidCount(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;        
        String sql  = "";      
        int oid_count = 0;

        String p_subj = box.getString("p_subj");
        String p_year = box.getString("p_year");
        String p_subjseq = box.getString("p_subjseq");
        String p_userid = box.getString("p_userid");
        
        try { 
            connMgr = new DBConnectionManager();        

            sql = "select count(*) as oid_count from tz_object a, tz_subjobj b where a.oid =b.oid and  "
                + " b.subj= " + StringManager.makeSQL(p_subj) + " and trim(a.starting) is not null "   ;

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                oid_count = ls.getInt("oid_count");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return oid_count;
    }

    /**
    진도 나간 갯수 조회
    @param box  receive from the form object and session
    @return ArrayList   Object리스트
    */      
    public int SelectJindoCount(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;        
        String sql  = "";      
        int jindo_count = 0;

        String p_subj = box.getString("p_subj");
        String p_year = box.getString("p_year");
        String p_subjseq = box.getString("p_subjseq");
        String p_userid = box.getString("p_userid");
        
        try { 
            connMgr = new DBConnectionManager();        

            sql = "select count(*) as jindo_count from tz_progress   "
                + " where subj= " + StringManager.makeSQL(p_subj)   
                + "   and year= " + StringManager.makeSQL(p_year) + " and userid = " + StringManager.makeSQL(p_userid)
                + "   and subjseq=" + StringManager.makeSQL(p_subjseq)
                + " and substr(ldate,1,8) = to_char(sysdate,'YYYYMMDD') and lessonstatus = 'complete' ";
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                jindo_count = ls.getInt("jindo_count");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return jindo_count;
    }


    /**
    학습기간, 복습기간 체크
    @param box  receive from the form object and session
    @return ArrayList   Object리스트
    */      
    public int SelectEudPeriod(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;        
        String sql  = "";             
        ArrayList list1 = null;
        String s_eduperiod = "";
        int cnt = 0;

        String s_subj = box.getString("p_subj");
        String s_year = box.getString("p_year");
        String s_subjseq = box.getString("p_subjseq");
        String s_userid = box.getString("p_userid");
        
        try { 
            connMgr = new DBConnectionManager();   
            list1 = new ArrayList();

            sql = "select count(*) as cnt  from tz_subjseq "    
                    + " where subj=" + StringManager.makeSQL(s_subj)
                    + "   and year=" + StringManager.makeSQL(s_year)
                    + "   and subjseq=" + StringManager.makeSQL(s_subjseq)              
                    + "  and to_char(sysdate,'YYYYMMDDHH24') between edustart and eduend ";

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                cnt = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }

    /**
    해당 ID의 입과생 존재 유무
    @param box      receive from the form object and session
    @return ProjectData
    */
     public int selectUserPage(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";         
        
        String s_subj       = box.getString("p_subj");      // 과목
        String s_year       = box.getString("p_year");      // 년도
        String s_subjseq    = box.getString("p_subjseq");   // 과목기수
        String s_user_id    = box.getString("p_userid");    // 입과생ID
        
        if ( s_user_id.equals("") ) {
            s_user_id   = box.getSession("userid");     	// 입과생ID
        }

        if ( s_subj.equals("")||s_year.equals("")||s_subjseq.equals("") ) { 
            s_subj      = box.getSession("s_subj");    		// 과목
            s_subjseq   = box.getSession("s_subjseq");  	// 과목기수
            s_year      = box.getSession("s_year");     	// 년도

        }

        int result = 0;										// 1:입과생임, 0:입과생이 아님
        try { 
            connMgr = new DBConnectionManager();

            sql  = "select count(userid) cnt ";
            sql += "from TZ_STUDENT ";
            sql += "where subj=" + StringManager.makeSQL(s_subj);
            sql += "  and year=" + StringManager.makeSQL(s_year);
            sql += "  and subjseq=" + StringManager.makeSQL(s_subjseq);
            sql += "  and userid=" + StringManager.makeSQL(s_user_id);

            ls = connMgr.executeQuery(sql);
            if ( ls.next() && ls.getInt("cnt") > 0 ) { 
                result = 1;     // 해당 ID가 입과생인 경우
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


   /**
     * 정보동의 체크
     * @param box       receive from the form object and session
     * @return is_Ok    0 : 정보동의필요      1 : 정보동의함
     * @throws Exception
     */
    public int firstCheck(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int is_Ok = 0;

        String p_userid     = box.getSession("userid");
        String p_subj       = box.getString("p_subj");
        String p_year       = box.getString("p_year");
        String p_subjseq    = box.getString("p_subjseq");

                        
        if ( p_subj.equals("")||p_year.equals("")||p_subjseq.equals("") ) { 
            p_subj      = box.getSession("s_subj");
            p_year      = box.getSession("s_year");
            p_subjseq   = box.getSession("s_subjseq");
        }

        try { 
              connMgr = new DBConnectionManager();

              sql  = " select nvl(a.validation,'0') validation "; //, b.isgoyong 
              sql += " from TZ_STUDENT a,TZ_SUBJ b ";
              sql += " where ";
              sql += "   a.subj = b.subj ";
              sql += "   and a.userid = " + StringManager.makeSQL(p_userid);
              sql += "   and a.subj    = " + StringManager.makeSQL(p_subj);
              sql += "   and a.year    = " + StringManager.makeSQL(p_year);
              sql += "   and a.subjseq = " + StringManager.makeSQL(p_subjseq);
              ls = connMgr.executeQuery(sql);

              if ( ls.next() ) { 
                /*if ( ls.getString("isgoyong").equals("Y") ) { 
                  is_Ok = StringManager.toInt( ls.getString("validation") );
                } else { */
                  is_Ok = 1;
                /*}*/
              } else { 
                is_Ok = 1;
              }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }
    
    /**
     * 고용보험 환급과정 체크
     * @param box       receive from the form object and session
     * @return is_Ok    0 : 비관급과정      1 : 환급과정
     * @throws Exception
     */
    public int goyongCheck(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int is_Ok = 0;

        String p_subj       = box.getString("p_subj");
        String p_year       = box.getString("p_year");
        String p_subjseq    = box.getString("p_subjseq");

        try { 
              connMgr = new DBConnectionManager();

              sql  = " select isgoyong "; //, b.isgoyong 
              sql += " from tz_subjseq";
              sql += " where subj    = " + StringManager.makeSQL(p_subj);
              sql += "   and year    = " + StringManager.makeSQL(p_year);
              sql += "   and subjseq = " + StringManager.makeSQL(p_subjseq);
              
              //System.out.println(sql);
              
              ls = connMgr.executeQuery(sql);

              if ( ls.next() ) {
            	  if(ls.getString("isgoyong").equals("Y")){
            		  is_Ok = 1;
            	  }
            	  else{
            		  is_Ok = 0;
            	  }
              }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }

    /**
     * 정보보호 확인
     * @param box       receive from the form object and session
     * @return is_Ok    1 : login ok      2 : login fail
     * @throws Exception
     */
    public int firstSubj(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int is_Ok = 0;

        String p_userid     = box.getSession("userid");
        String p_subj       = box.getString("p_subj");
        String p_year       = box.getString("p_year");
        String p_subjseq    = box.getString("p_subjseq");
                        
        if ( p_subj.equals("")||p_year.equals("")||p_subjseq.equals("") ) { 
            p_subj      = box.getSession("s_subj");
            p_year      = box.getSession("s_year");
            p_subjseq   = box.getSession("s_subjseq");
        }

        try { 
             connMgr = new DBConnectionManager();

            sql  = " update TZ_STUDENT set validation = ?                         ";
            sql += "  where userid = ? and subj = ? and year = ? and subjseq = ? ";
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, "1");
            pstmt.setString(2, p_userid);
            pstmt.setString(3, p_subj);
            pstmt.setString(4, p_year);
            pstmt.setString(5, p_subjseq);
            is_Ok = pstmt.executeUpdate();
         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

        return is_Ok;
    }
    
    /**
     * 과정명 가져오기 
     * @param box receive from the form object and session
     * @return Stringt
     * @throws Exception
     */    
    public String selectSubjnm(RequestBox box) throws Exception {
        
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";    
        String subjnm = "";

        try { 
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");        

            sql = 
                "SELECT subjnm FROM tz_subj WHERE subj = " + StringManager.makeSQL(s_subj);
            
            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() )
            {
                subjnm = ls.getString( "subjnm" );
            }
            ls.close();
            
        }
        catch ( Exception ex )
        { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return subjnm;
    }
    
    /**
     * 진도정보를 가져온다 
     *
     * @param box receive from the form object and session
     * @return Map
     * @throws Exception
     */ 
    public Map selectProgressData(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        Map resultMap = new HashMap();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            sql =
                "\n  SELECT lesson, oid, lesson_count FROM #progressTableName#" +
                "\n  WHERE 1 = 1 " +
                "\n      AND subj = " + SQLString.Format( s_subj ) +
                "\n      AND year = " + SQLString.Format( s_year ) +
                "\n      AND subjseq = "+ SQLString.Format( s_subjseq ) +
                "\n      AND LESSON_COUNT > 0 " +
                "\n      AND userid = " + SQLString.Format( s_userid );

            String progressTableName = getProgressTableName( box );
            sql = sql.replaceAll("#progressTableName#", progressTableName);
            System.out.println( "진도테이블 : " + sql );
            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                resultMap.put( ls.getString("lesson") + "_" + ls.getString("oid"), ls.getString("lesson_count") );
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return resultMap;        
    }
    
    /**
     * LiveShare Parameter 정보를 가져온다 
     *
     * @param box receive from the form object and session
     * @return Map
     * @throws Exception
     */    
    public Map getLiveShareParams(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        Map resultMap = new HashMap();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_userid = box.getSession("userid");
            
            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            
            String v_module = box.getString("p_module");
            String v_lesson = box.getString("p_lesson");
            String v_object = box.getString("p_object");
            String v_type = box.getString("p_type");
            
            sql =
                "\n  SELECT session_time, first_end     " +
                "\n  FROM tz_progress                   " +
                "\n  WHERE 1 = 1                        " +
                "\n     AND subj = " + SQLString.Format( v_subj ) +
                "\n     AND year = " + SQLString.Format( v_year ) +
                "\n     AND subjseq = " + SQLString.Format( v_subjseq ) +
                "\n     AND lesson = " + SQLString.Format( v_lesson ) +
                "\n     AND OID = " + SQLString.Format( v_object ) +
                "\n     AND userid = " + SQLString.Format( s_userid );
            
            ls = connMgr.executeQuery( sql );

            String v_isstudycomp = "";
            String v_session_time = "";
            
            if ( ls.next() )
            {
                v_session_time = convertSecond( ls.getString( "session_time" ) );
                if ( ls.getString( "first_end" ).equals("") )
                {
                    v_isstudycomp = "view";
                }
                else
                {
                    v_isstudycomp = "study";
                }
            }
            else
            {
                v_isstudycomp = "view";
                v_session_time = "0";
            }
            
            resultMap.put( "isstudycomp", v_isstudycomp );
            resultMap.put( "session_time", v_session_time );
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return resultMap;        
    }
    
    /*
     * 00:00:00 (시:분:초)를 초 단위 값으로 변경한다.
     *
     */
    private String convertSecond( String inputTime )
    {
        String resultTime = "0";
        
        String[] time = inputTime.split(":");
        
        if ( time.length != 3 )
        {
            resultTime = "0";
        }
        else
        {
            try
            {
                resultTime = String.valueOf((Integer.parseInt(time[0])*60*60) + (Integer.parseInt(time[1])*60) + (Integer.parseInt(time[2])));
            }
            catch ( Exception e )
            {
                resultTime = "0";
            }
        }
        
        return resultTime;
    }

    /**
     * Normal 과정 시작위치 스크립트 문자열 반환
     * 
     * @param box
     * @return
     * @throws Exception
     */
	public String getStartLessonString(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        
        String startLesson = "";
        
        try 
        {
            connMgr = new DBConnectionManager();

            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String s_userid = box.getSession("userid");

            
            sql =
            	"\n  SELECT TRIM (TO_CHAR (NVL ((MAX (lesson) + 1), '01'), '00')) maxlesson  " +
            	"\n    FROM tz_progress a  " +
            	"\n   WHERE subj = #subj#  " +
            	"\n     AND YEAR = #year#  " +
            	"\n     AND subjseq = #subjseq#  " +
            	"\n     AND userid = #userid#  " +
            	"\n     AND first_end IS NOT NULL  ";

            sql = sql.replaceAll("#subj#", StringManager.makeSQL(v_subj) );
            sql = sql.replaceAll("#year#", StringManager.makeSQL(v_year) );
            sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(v_subjseq) );
            sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid) );

            ls = connMgr.executeQuery(sql);
            logger.debug( "시작위치 SQL :\n" + sql );
            
            String maxLesson = "";
            if ( ls.next() ) {
            	maxLesson = ls.getString( "maxlesson" );
            }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

            sql =
            	"\n  SELECT DECODE (COUNT (lesson), 0, '01', #maxlesson# ) startlesson  " +
            	"\n   FROM tz_subjlesson  " +
            	"\n   WHERE subj = #subj#  " +
            	"\n     AND lesson = LTRIM(#maxlesson#)  ";
            
            sql = sql.replaceAll("#subj#", StringManager.makeSQL(v_subj) );
            sql = sql.replaceAll("#maxlesson#", StringManager.makeSQL(maxLesson) );
            	
            ls = connMgr.executeQuery( sql );
            if ( ls.next() )
            {
            	startLesson = ls.getString("startlesson");
            }
            
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            
            if(startLesson.equals("01")){
            	sql =
                	"\n  SELECT min(lesson) startlesson  " +
                	"\n   FROM tz_subjlesson  " +
                	"\n   WHERE subj = #subj#  " ;
            	
            	sql = sql.replaceAll("#subj#", StringManager.makeSQL(v_subj) );
            	ls = connMgr.executeQuery( sql );
            	
            	if ( ls.next() )
                {
                	startLesson = ls.getString("startlesson");
                }
            }
            
            logger.debug( "시작위치 N : " + startLesson);
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return StringManager.makeSQL(startLesson);        
	}

    /**
     * OBC, OBC-Author 과정 시작위치 스크립트 문자열 반환
     * 
     * @param box
     * @return
     * @throws Exception
     */
	public String getStartOidString(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        
        String startModule = "";
        String startLesson = "";
        String startOid = "";
        
        try 
        {
            connMgr = new DBConnectionManager();

            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String s_userid = box.getSession("userid");

            sql =
            	"\n  SELECT   a.subj, a.module, a.lesson, a.OID,  " +
            	"\n           DECODE (b.first_end, NULL, 'N', '', 'N', 'Y') is_completed  " +
            	"\n      FROM tz_subjobj a,  " +
            	"\n           (SELECT subj, lesson, OID, first_end  " +
            	"\n              FROM tz_progress  " +
            	"\n             WHERE subj = #subj#  " +
            	"\n               AND YEAR = #year#  " +
            	"\n               AND subjseq = #subjseq#  " +
            	"\n               AND userid = #userid#  " +
            	"\n               AND (first_end IS NOT NULL OR first_end != '')) b  " +
            	"\n     WHERE a.subj = #subj#  " +
            	"\n       AND a.subj = b.subj(+)  " +
            	"\n  	  AND a.lesson = b.lesson(+)  " +
            	"\n       AND a.OID = b.OID(+)  " +
            	"\n  ORDER BY lesson DESC, ordering DESC  ";
            
            sql = sql.replaceAll("#subj#", StringManager.makeSQL(v_subj) );
            sql = sql.replaceAll("#year#", StringManager.makeSQL(v_year) );
            sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(v_subjseq) );
            sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid) );

            ls = connMgr.executeQuery(sql);
            
            String isCompleted = "";
            
            String tempPrevModule = "";
            String tempPrevLesson = "";
            String tempPrevOid = "";
            
            boolean isFirst = true;
            while ( ls.next() ) {
            	startModule = ls.getString( "module" );
            	startLesson = ls.getString( "lesson" );
            	startOid = ls.getString( "oid" );
            	
            	isCompleted = ls.getString( "is_completed" );
            	if ( isCompleted.equals("Y") ) {
            		if ( !isFirst ) {
	        			startModule = tempPrevModule;
	        			startLesson = tempPrevLesson;
	        			startOid = tempPrevOid;
	        			break;
            		} else {
            			break;
            		}
            	}
            	
            	tempPrevModule = startModule;
            	tempPrevLesson = startLesson;
            	tempPrevOid = startOid;
            	isFirst = false;
            }
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        String result = StringManager.makeSQL(startModule) + "," 
        				+ StringManager.makeSQL(startLesson) + "," 
        				+ StringManager.makeSQL(startOid);

        logger.debug( "시작위치 O : " + result);
        
        return result;
	}
	

	 /**
   과정별 자료실
   @param box          receive from the form object and session
   @return ArrayList   과정별 자료실, 과정별 질문방 TOP 3
   */
   public ArrayList selectBoardList(RequestBox box) throws Exception { 
       DBConnectionManager	connMgr	= null;
       ListSet ls        = null;
       ArrayList list    = null;
       String sql        = "";
       DataBox dbox = null;

       String v_subj    = box.getString("p_subj");
       String v_type = box.getString("p_type");
       
       try { 
           connMgr = new DBConnectionManager();


           list = new ArrayList();

           sql = "\n select title, ldate, tabseq, seq "
        	   + "\n from  ( "
        	   + "\n        select b.title, b.ldate, a.tabseq, b.seq "
        	   + "\n        from   tz_bds a, tz_board b "
        	   + "\n        where  a.tabseq = b.tabseq "
        	   + "\n        and    a.type = " + StringManager.makeSQL(v_type)
        	   + "\n        and    a.subj = " + StringManager.makeSQL(v_subj)
        	   + "\n        order  by b.ldate desc "
        	   + "\n       ) "
        	   + "\n where  rownum <= 3 ";
        	   
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
   과정별 질문방
   @param box          receive from the form object and session
   @return ArrayList   과정별 자료실, 과정별 질문방 TOP 3
   */
   public ArrayList selectQnaBoardList(RequestBox box) throws Exception { 
       DBConnectionManager	connMgr	= null;
       ListSet ls        = null;
       ArrayList list    = null;
       String sql        = "";
       DataBox dbox = null;

       String v_subj    = box.getString("p_subj");
       String v_year    = box.getString("p_year");
       String v_subjseq = box.getString("p_subjseq");
       String v_type = box.getString("p_type");
       String s_userid     = box.getSession("userid");
       
       try { 
           connMgr = new DBConnectionManager();


           list = new ArrayList();

           sql = "\n select title, ldate, tabseq, seq ,chk , luserid "
        	   + "\n from  ( "
        	   + "\n        select b.title, b.ldate, a.tabseq, b.seq ,DECODE(userid,'"+s_userid+"','Y',NVL(ISOPEN,'N')) AS chk "
        	   + "\n         ,(select luserid from tz_subjseq ts where ts.subj = a.subj and ts.year = b.year and ts.subjseq = b.subjseq) luserid "
        	   + "\n        from   tz_bds a, tz_board b "
        	   + "\n        where  a.tabseq = b.tabseq "
        	   + "\n        and    a.type = " + StringManager.makeSQL(v_type)
        	   + "\n        and    a.subj = " + StringManager.makeSQL(v_subj)
        	   + "\n        and    b.year = " + StringManager.makeSQL(v_year)
        	   + "\n        and    b.subjseq = " + StringManager.makeSQL(v_subjseq)
        	   + "\n        order  by b.ldate desc "
        	   + "\n       ) "
        	   + "\n where  rownum <= 3 "; 
  	   
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
   과정 차수별 공지사항
   @param box          receive from the form object and session
   @return ArrayList   과목기수별 공지  리스트
   */
   public ArrayList selectGongList(RequestBox box) throws Exception { 
       DBConnectionManager	connMgr	= null;
       ListSet ls        = null;
       ArrayList list    = null;
       String sql        = "";
       DataBox dbox = null;

       String v_subj    = box.getString("p_subj");
       String v_year    = box.getString("p_year");
       String v_subjseq = box.getString("p_subjseq");
       
       try { 
           connMgr = new DBConnectionManager();
           list = new ArrayList();

           sql = "\n select seq, addate, title "
        	   + "\n from  ( "
        	   + "\n        select seq, addate, title "
        	   + "\n        from   tz_gong "
        	   + "\n        where  subj = " + StringManager.makeSQL(v_subj)
        	   + "\n        and    year = " + StringManager.makeSQL(v_year)
        	   + "\n        and    subjseq = " + StringManager.makeSQL(v_subjseq)
        	   + "\n        order  by addate desc "
        	   + "\n       ) "
        	   + "\n where  rownum <= 3 ";
//System.out.println("======================공지리스트 ::::::::" + sql);
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
    * 과정구분, 총차시, 학습한차시, 진도율 가져오기
    * @param box
    * @return
    * @throws Exception
    */
   public DataBox getStudyChasi(RequestBox box) throws Exception { 
       DBConnectionManager	connMgr	= null;
       ListSet         ls              = null;
       StringBuffer    sbSQL           = new StringBuffer("");
       StringBuffer    sbSQL1          = new StringBuffer("");
       int				isOk            = 0;
       DataBox			dbox			= null;

       String v_contenttype   = "";

       String v_subj      = box.getString("p_subj");
       String v_year      = box.getString("p_year");
       String v_subjseq   = box.getString("p_subjseq");
       String v_userid    = box.getSession("userid");
       
       if ( !box.getString("p_userid").equals("") && EduAuthBean.getInstance().isAdminAuth(box.getSession("gadmin")) ) {
    	   v_userid = box.getString("p_userid");
       }

       try { 
           connMgr = new DBConnectionManager();
           dbox = new DataBox("responsebox");
           
           sbSQL.append(" select  nvl(contenttype,'N') as contenttype          \n")
           		.append("       , subj_gu                                      \n")
                .append(" from    tz_subj                                      \n")
                .append(" where   subj   = " + SQLString.Format(v_subj) + "    \n");
           
           ls  = connMgr.executeQuery(sbSQL.toString());
           
           while ( ls.next() ) { 
               v_contenttype   = ls.getString("contenttype");
               dbox.put("subjgu", ls.getString("subj_gu"));
           }
           ls.close();

           sbSQL.setLength(0);
           sbSQL.append(" select  gradstep as wstep     		    	    	\n")
           		.append(" from    tz_subjseq									\n")
           		.append(" where   subj   = " + SQLString.Format(v_subj) + "		\n")
           		.append(" and     year   = " + SQLString.Format(v_year) + "		\n")
           		.append(" and     subjseq= " + SQLString.Format(v_subjseq) + "	\n");
      
           ls  = connMgr.executeQuery(sbSQL.toString());
           while ( ls.next() ) { 
        	   dbox.put("wstep", ls.getDouble("wstep"));
           }
           ls.close();
           
           sbSQL.setLength(0);
           
           if ( v_contenttype.equals("O") || v_contenttype.equals("OA") ) {	// OBC, OBC-Author
           	
           	// 전체 OID 갯수
               sbSQL.append(" select  count(*) datecnt                                     \n")
                    .append(" from    tz_subjobj                                           \n")
                    .append(" where   subj = " + SQLString.Format(v_subj) + "              \n")
                    .append(" and     type = 'SC'                                          \n");

               // 학습한 OID 갯수
               sbSQL1.append(" SELECT  count(*) edudatecnt                                 \n")                            
                     .append(" FROM    tz_progress a                                       \n")
                     .append("     ,   tz_subjobj  b                                       \n")
                     .append(" WHERE   a.subj      = " + SQLString.Format(v_subj   ) + "   \n")
                     .append(" AND     a.year      = " + SQLString.Format(v_year   ) + "   \n")
                     .append(" AND     a.subjseq   = " + SQLString.Format(v_subjseq) + "   \n")
                     .append(" AND     a.userid    = " + SQLString.Format(v_userid ) + "   \n")
                     .append(" AND     a.FIRST_END IS NOT NULL                             \n")
                     .append(" AND     a.subj      = b.subj                                \n")
                     .append(" AND     a.lesson    = b.lesson                              \n")
                     .append(" AND     a.oid       = b.oid                                 \n")
                     .append(" AND     nvl(lesson_count, 0) > 0                            \n");

           } else if ( v_contenttype.equals("S") ) {	// SCORM2004
               
               // 전체 sco 갯수
               sbSQL.append( " SELECT COUNT (*) datecnt                               \n" )
					 .append( " FROM tys_item a, tys_resource b, tz_subj_contents c    \n" )
					 .append( " WHERE 1 = 1                                            \n" )
					 .append( "    AND a.course_code = b.course_code                   \n" )
					 .append( "    AND b.course_code = c.course_code                   \n" )
					 .append( "    AND a.org_id = b.org_id                             \n" )
					 .append( "    AND b.org_id = c.org_id                             \n" )
					 .append( "    AND a.item_id = b.item_id                           \n" )
					 .append( "    AND b.res_scorm_type = 'sco'                        \n" )
					 .append( "    AND c.subj = " + SQLString.Format(v_subj) + "       \n" );

               // 학습한 sco 갯수
               sbSQL1.append(" select  count(lesson) edudatecnt     			            \n")
                     .append(" from    tz_progress                                         \n")
                     .append(" where   subj        = " + SQLString.Format(v_subj   ) + "   \n")
                     .append(" and     year        = " + SQLString.Format(v_year   ) + "   \n")
                     .append(" and     subjseq     = " + SQLString.Format(v_subjseq) + "   \n")
                     .append(" and     userid      = " + SQLString.Format(v_userid ) + "   \n")
                     .append(" and     first_edu  IS NOT NULL                              \n")
                     .append(" and     nvl(lesson_count, 0) > 0                            \n");

           } else {	// Normal, KT
           	
        	   // 전체 차시 갯수
               sbSQL.append(" select  count(*) datecnt                                     \n")
                    .append(" from    tz_subjlesson                                        \n")
                    .append(" where   subj = " + SQLString.Format(v_subj) + "              \n")
                    .append(" and     lesson != '00' and lesson != '99'                    \n");
               
               // 학습한 차시 갯수
               sbSQL1.append(" select  count(lesson) edudatecnt     			            \n")
                     .append(" from    tz_progress                                         \n")
                     .append(" where   subj        = " + SQLString.Format(v_subj   ) + "   \n")
                     .append(" and     year        = " + SQLString.Format(v_year   ) + "   \n")
                     .append(" and     subjseq     = " + SQLString.Format(v_subjseq) + "   \n")
                     .append(" and     userid      = " + SQLString.Format(v_userid ) + "   \n")
                     .append(" and     first_edu  IS NOT NULL                              \n")
                     .append(" and     nvl(lesson_count, 0) > 0                            \n");
               
           }

           ls  = connMgr.executeQuery(sbSQL.toString());
           
           while ( ls.next() ) { 
               dbox.put("datecnt", ls.getInt("datecnt"));
           }
           ls.close();
           
           ls  = connMgr.executeQuery(sbSQL1.toString());
           
           while ( ls.next() ) { 
           	dbox.put("edudatecnt", ls.getInt("edudatecnt"));
           }
           ls.close();
           
       } catch ( SQLException e ) {
           ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
           throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
       } catch ( Exception e ) {
           ErrorManager.getErrorStackTrace(e);
           throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
       } finally {
           if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }

       return dbox;
   }    

   /**
    * 과정구분, 총차시, 학습한차시, 진도율 가져오기
    * @param box
    * @return
    * @throws Exception
    */
   public DataBox getCalcStudyMm(RequestBox box) throws Exception { 
       DBConnectionManager	connMgr	= null;
       ListSet         ls              = null;
       StringBuffer    sbSQL           = new StringBuffer("");
       int				isOk            = 0;
       DataBox			dbox			= null;

       String v_contenttype   = "";

       String v_subj      = box.getString("p_subj");
       String v_year      = box.getString("p_year");
       String v_subjseq   = box.getString("p_subjseq");
       String v_userid    = box.getSession("userid");
       String v_yyyy	  = FormatDate.getDate("yyyy");
       String v_mm	 	  = FormatDate.getDate("MM");
       
       String v_startDate = "";
       String v_endDate = "";

       try { 
           connMgr = new DBConnectionManager();
           dbox = new DataBox("responsebox");
           
           sbSQL.append("\n select  start_date, end_date, study_chasi	")
                .append("\n from    tz_subj_mm                          ")
                .append("\n where   subj = " + SQLString.Format(v_subj)  )
                .append("\n and     year = " + SQLString.Format(v_year)  )
                .append("\n and     mm   = " + SQLString.Format(v_mm)    );
           
           ls  = connMgr.executeQuery(sbSQL.toString());
           
           while ( ls.next() ) { 
        	   v_startDate = ls.getString("start_date");
               v_endDate   = ls.getString("end_date");
               dbox.put("start_date", v_startDate);
               dbox.put("end_date"  , v_endDate);
               dbox.put("totalchasi", ls.getInt("study_chasi"));
           }
           ls.close();

           sbSQL.setLength(0);
           sbSQL.append("\n select  get_studycount('"+v_subj+"', '"+v_year+"', '"+v_subjseq+"', '"+v_userid+"', '"+v_startDate+"', '"+v_endDate+"') as studycount ")
           		.append("\n       , get_studytime('"+v_subj+"', '"+v_year+"', '"+v_subjseq+"', '"+v_userid+"', '"+v_startDate+"', '"+v_endDate+"') as studytime ")
           		.append("\n       , get_studychasi('"+v_subj+"', '"+v_year+"', '"+v_subjseq+"', '"+v_mm+"', '"+v_userid+"', '"+v_startDate+"', '"+v_endDate+"') as studychasi ")
           		.append("\n from    dual ");
      
           ls  = connMgr.executeQuery(sbSQL.toString());
           while ( ls.next() ) { 
        	   dbox.put("studycount", ls.getInt("studycount"));
        	   dbox.put("studytime" , ls.getInt("studytime"));
        	   dbox.put("studychasi", ls.getInt("studychasi"));
           }
           ls.close();
           
       } catch ( SQLException e ) {
           ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
           throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
       } catch ( Exception e ) {
           ErrorManager.getErrorStackTrace(e);
           throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
       } finally {
           if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }

       return dbox;
   }    
   
   /**
    * 강사정보
    * @param box
    * @return
    * @throws Exception
    */  
   public DataBox getTutorInfo(RequestBox box) throws Exception {                
       DBConnectionManager	connMgr	= null;
       ListSet ls = null;
       ArrayList list = null;
       DataBox dbox = null;
       String sql  = "";

       String  p_subj, p_year, p_subjseq, p_userid;

       p_subj      = box.getString("p_subj");    
       p_year      = box.getString("p_year");    
       p_subjseq   = box.getString("p_subjseq"); 
       p_userid    = box.getString("p_userid");  

       if ( p_userid.equals("")) p_userid    = box.getSession("userid");
       if ( p_subj.equals("")||p_year.equals("")||p_subjseq.equals("") ) { 
           p_subj      = box.getSession("s_subj");        
           p_year      = box.getSession("s_year");        
           p_subjseq   = box.getSession("s_subjseq");     
           p_userid    = box.getSession("userid");      
       }
       
       try { 
           connMgr = new DBConnectionManager();
           
           sql = "\n select a.tuserid "
        	   + "\n      , b.name "
        	   + "\n      , b.handphone "
        	   + "\n      , b.hometel "
        	   + "\n      , b.email "
        	   + "\n      , b.position_nm "
        	   + "\n from   tz_classtutor a, tz_member b  "
        	   + "\n where  a.subj = " + StringManager.makeSQL(p_subj)
        	   + "\n and    a.year = " + StringManager.makeSQL(p_year)
        	   + "\n and    a.subjseq = " + StringManager.makeSQL(p_subjseq)
        	   + "\n and    a.tuserid = b.userid "
        	   + "\n and    rownum = 1 ";

           ls = connMgr.executeQuery(sql);
           
           if ( ls.next() ) {
               dbox = ls.getDataBox();
           }

       } catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       } finally { 
           if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }

       return dbox;
   }
   
   /**
	 * 순차방식일경우 첫 진도를 생성한다.
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean firstInsertProgress(RequestBox reqBox) throws Exception {
		ListSet ls = null;
		DBConnectionManager	connMgr	= null;
		PreparedStatement pstmt = null;
		
		DataBox dbox = null;
		
		String sql = "";
		boolean result = false;
	
		try {
			connMgr = new DBConnectionManager();
			int count = 0;
			
			String s_subj = reqBox.getSession("s_subj");
			String s_year = reqBox.getSession("s_year");
			String s_subjseq = reqBox.getSession("s_subjseq");
			String s_userid = reqBox.getSession("userid");
			
			sql = "\n select lesson, oid "
				   + "\n from tz_subjobj "
	        	   + "\n where  subj = " + StringManager.makeSQL(s_subj)
	        	   + "\n and    rownum = 1 " 
	        	   + "\n order by module, lesson ";

	           ls = connMgr.executeQuery(sql);
	           
	           if ( ls.next() ) {
	               dbox = ls.getDataBox();
	           }
			
			String p_lesson = dbox.getString("d_lesson");
			String p_contenttype = reqBox.getString("p_contenttype");
			String p_oid = dbox.getString("d_oid");
			
			String v_first_end = "NULL";
			String v_lesson_count = "0";

			sql =
				"\n  INSERT INTO tz_progress "+
				"\n     ( subj, YEAR, subjseq, userid, lesson, OID,  " +
				"\n       session_time, total_time,  " +
				"\n       first_edu, first_end,  " +
				"\n       lesson_count, ldate )  " +
				"\n  VALUES  " +
				"\n     ( ?, ?, ?, ?, ?, ?,  " +
				"\n       '00:00:00.00', '00:00:00.00',  " +
				"\n       TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS'), #first_end#,  " +
				"\n       #lesson_count#, TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') )  ";
			
			sql = sql.replaceAll("#first_end#", v_first_end );
			sql = sql.replaceAll("#lesson_count#", v_lesson_count );
			
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setString(1, s_subj);
			pstmt.setString(2, s_year);
			pstmt.setString(3, s_subjseq);
			pstmt.setString(4, s_userid);
			pstmt.setString(5, p_lesson);
			pstmt.setString(6, p_oid);
			
           count = pstmt.executeUpdate();
           pstmt.close();
			
			logger.debug( "@ Do Edu Check : " + result );
		}
		catch (Exception ex) {
			logger.error( ex );
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
		}
	
		return result;	
	}
	
	/**
     * 랜덤학습인지 아닌지 여부(랜덤학습이면 return값 Y)
     @param String  과정코드
     @return String 
     */
     public String israndomaccess(String p_subj) throws Exception{ 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String  sql  = "";
         String v_contentprogress = "";

         try { 
             connMgr = new DBConnectionManager();
             sql = "select contentprogress		\n"
            	 + "from tz_subj																		\n"
            	 + "where subj = " + StringManager.makeSQL(p_subj) + "									\n";
             
             //System.out.println(sql);
             
             ls = connMgr.executeQuery(sql);
             if ( ls.next() )   { 
                 v_contentprogress = ls.getString("contentprogress");
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return  v_contentprogress;
     }
     
     /**
      * 이전 학습 여부 가져오기
      * @param box receive from the form object and session
      * @return Stringt
      * @throws Exception
      */    
     public String getContUrl(String subj, String year, String subjseq, String userid) throws Exception {
         
         DBConnectionManager connMgr = null;
         ListSet ls = null;

         String cont_url = "";
         String sql = "";    
         
         try { 
             connMgr = new DBConnectionManager();
             StringBuffer strSQL = new StringBuffer();
             strSQL.append("select cont_url ") ;
             strSQL.append("  from tz_subjconturl ") ;
             strSQL.append(" where subj = "+StringManager.makeSQL(subj) + "		\n") ;
             strSQL.append("   and year = "+ StringManager.makeSQL(year) + "		\n") ;
             strSQL.append("   and subjseq = "+ StringManager.makeSQL(subjseq) + " \n") ;
             strSQL.append("   and userid = "+ StringManager.makeSQL(userid) + "  \n") ;
                                                
             //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  "+strSQL.toString());
             
             ls = connMgr.executeQuery(strSQL.toString());
             
             if ( ls.next() )
             {
            	 cont_url = ls.getString("cont_url");
             }
             ls.close();
             
         }
         catch ( Exception ex )
         { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception(ex.getMessage() );
         }
         finally
         { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return cont_url;
     }

 
     
     
     // 20100512 학습창 수정작업 @ Park
     
     
     /**
      * 마스터폼 Lesson리스트
      * @param box          receive from the form object and session
      * @return ArrayList   Lesson리스트
      */      
     public ArrayList SelectNewLessonList(RequestBox box) throws Exception {               
         DBConnectionManager	connMgr	= null;
         ListSet ls = null;
         ArrayList list1 = null;
         String sql  = "";
         MfLessonData data = null;

         String  s_subj      = box.getSession("s_subj");
         String  s_year      = box.getSession("s_year");
         String  s_subjseq   = box.getSession("s_subjseq");
         String  s_userid    = box.getSession("userid");
                 
         try { 
             connMgr = new DBConnectionManager();
             list1 = new ArrayList();

             sql = "select module,lesson,sdesc,types,owner,starting "
                 + "  from tz_subjlesson  "
                 + " where subj=" + StringManager.makeSQL(s_subj)
                 + " order by lesson";

             ls = connMgr.executeQuery(sql);
             while ( ls.next() ) { 
                 data = new MfLessonData(); 
                 data.setModule    ( ls.getString("module") );
                 data.setLesson    ( ls.getString("lesson") );
                 data.setSdesc     ( ls.getString("sdesc")) ;
                 data.setTypes     ( ls.getString("types")) ;
                 data.setOwner     ( ls.getString("owner")) ;
                 data.setStarting  ( ls.getString("starting")) ;
                 list1.add(data);    
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list1;
     }
     
     // 컨텐츠 URL 가져오기     
     public String SelectLesson(String v_subj, String v_lesson) throws Exception {               
         DBConnectionManager	connMgr	= null;
         ListSet ls = null;
         String sql  = "";
         String ret  = "";
         
         try { 
             connMgr = new DBConnectionManager();
             sql = "select starting "
                 + "  from tz_subjlesson  "
                 + " where subj=" + StringManager.makeSQL(v_subj)
                 + " AND lesson=" + StringManager.makeSQL(v_lesson);


             ls = connMgr.executeQuery(sql);
             if ( ls.next() ) { 
                 ret = ls.getString("starting");
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, null, sql);
             throw new Exception("SelectLesson sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return ret;
     }
}
