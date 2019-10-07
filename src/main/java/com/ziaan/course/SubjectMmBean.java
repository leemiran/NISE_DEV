// **********************************************************
//  1. 제      목: 매월정기안전교육 관리
//  2. 파  일  명: SubjSeqMonthBean.java
//  3. 개      요: 매월정기안전교육 관리
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 김미향 2008.11.05
//  7. 수      정: 
// **********************************************************
package com.ziaan.course;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class SubjectMmBean { 

    public final static String LANGUAGE_GUBUN   = "0017";
    public final static String ONOFF_GUBUN      = "0004";

    public SubjectMmBean() { }

    /**
     * 매월정기안전교육 월차별 목록
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList SelectSubjMmList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls   = null;
        ArrayList   list = null;
        String      sql  = "";
        SubjectData data = null;
        DataBox     dbox = null;
        
        String s_year   = box.getString("s_gyear");	// 연도
        String s_subj   = box.getString("s_subjcourse");	// 과정코드

        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            sql = "\n select a.subj "
            	+ "\n      , a.year "
            	+ "\n      , a.mm "
            	+ "\n      , a.start_date "
            	+ "\n      , a.end_date "
            	+ "\n      , a.study_time "
            	+ "\n      , a.study_count "
            	+ "\n      , a.study_chasi "
            	+ "\n from   tz_subj_mm a "
            	+ "\n where  a.year = " + StringManager.makeSQL(s_year)
            	+ "\n and    a.subj = " + StringManager.makeSQL(s_subj)
            	+ "\n order  by a.mm ";
            
            ls = connMgr.executeQuery(sql);

            
            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
     * 매월정기안전교육 월차별 상세조회
     * @param box
     * @return
     * @throws Exception
     */
    public SubjectMmData SelectSubjMmData(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet         ls   = null;
        String          sql  = "";
        SubjectMmData   data = null;
        
        String v_year   = box.getString("p_year");	// 교육연도
        String v_subj   = box.getString("p_subj");	// 과정코드
        String v_mm   	= box.getString("p_mm");	// 월차

        try { 
            connMgr     = new DBConnectionManager();
            
            sql = "\n select a.subj "
            	+ "\n      , a.year "
            	+ "\n      , a.mm "
            	+ "\n      , a.start_date "
            	+ "\n      , a.end_date "
            	+ "\n      , a.study_time "
            	+ "\n      , a.study_count "
            	+ "\n      , a.study_chasi "
            	+ "\n      , a.luserid "
            	+ "\n      , a.ldate "
            	+ "\n from   tz_subj_mm a "
            	+ "\n where  a.subj   = " + StringManager.makeSQL(v_subj)
            	+ "\n and    a.year   = " + StringManager.makeSQL(v_year)
            	+ "\n and    a.mm     = " + StringManager.makeSQL(v_mm);
            

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data    = new SubjectMmData();
                
                data.setSubj       (ls.getString("subj"));
                data.setYear       (ls.getString("year"));
                data.setMm         (ls.getString("mm"));
                data.setStart_date (ls.getString("start_date"));
                data.setEnd_date   (ls.getString("end_date"));
                data.setStudy_count(ls.getInt   ("study_count"));
                data.setStudy_chasi(ls.getInt   ("study_chasi"));
                data.setStudy_time (ls.getInt   ("study_time"));
                data.setLuserid    (ls.getString("luserid") );
                data.setLdate      (ls.getString("ldate"  ) );
                
                break;
            }                   

        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
        
        return data;
	}
    
    
    /**
     * 매월정기안전교육 등록
     * @param box
     * @return
     * @throws Exception 
     */
	public int InsertSubjectMm(RequestBox box) throws Exception {
        DBConnectionManager 	connMgr 		= null;
        PreparedStatement 		pstmt 			= null;
        String sql  = "";
        int   isOk = 0;

        String v_luserid = box.getSession("userid");
        String v_year    = box.getString ("p_year"); // 년도
        String v_subj    = box.getString ("p_subj");
        
        int pidx = 1;
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            // 월차 중복체크
            if (getDuplSubjMm(connMgr, v_year, v_subj, box.getString("p_mm")) > 0) {
            	isOk = -1;
            } else {
	            
	            // insert tz_subj_mm table
	            sql = "\n insert into tz_subj_mm "
	            	+ "\n        ( "
	            	+ "\n         subj, year, mm "
	            	+ "\n       , start_date, end_date "
	            	+ "\n       , study_count, study_time, study_chasi "
	            	+ "\n       , luserid, ldate "
	            	+ "\n        ) "
	            	+ "\n values ( "
	            	+ "\n         ?, ?, ? "
	            	+ "\n       , ?, ? "
	            	+ "\n       , ?, ?, ? "
	            	+ "\n       , ?, ? "
	            	+ "\n        ) ";
	            	
	            pstmt = connMgr.prepareStatement(sql);
	
	            pstmt.setString(pidx++, v_subj);						// 과정코드
	            pstmt.setString(pidx++, v_year);						// 년도
	            pstmt.setString(pidx++, box.getString("p_mm"));			// 월차
	            pstmt.setString(pidx++, box.getString("p_start_date"));	// 시작일
	            pstmt.setString(pidx++, box.getString("p_end_date"));	// 종료일
	            pstmt.setInt   (pidx++, box.getInt("p_study_count"));	// 수료정보 : 횟수
	            pstmt.setInt   (pidx++, box.getInt("p_study_time"));	// 수료정보 : 시간
	            pstmt.setInt   (pidx++, box.getInt("p_study_chasi"));	// 수료정보 : 차시
	            pstmt.setString(pidx++, v_luserid);						// 생성자
	            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));	// 생성일
	            
	            isOk = pstmt.executeUpdate();
	
	            if ( isOk == 1 ) {
	                connMgr.commit();
	            } else {
	            	connMgr.rollback();
	            }
            }
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
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
     * 매월정기안전교육 등록
     * @param box
     * @return
     * @throws Exception 
     */
	public int UpdateSubjectMm(RequestBox box) throws Exception {
        DBConnectionManager 	connMgr 		= null;
        PreparedStatement 		pstmt 			= null;
        String sql  = "";
        int   isOk = 0;

        String v_luserid = box.getSession("userid" );
        String v_year    = box.getString ("p_year"); // 년도
        String v_subj    = box.getString ("p_subj");
        
        int pidx = 1;
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // update tz_subj_mm table
            sql = "\n update tz_subj_mm "
            	+ "\n    set  start_date = ? "
            	+ "\n       , end_date = ? "
            	+ "\n       , study_count = ? "
            	+ "\n       , study_time = ? "
            	+ "\n       , study_chasi = ? "
            	+ "\n       , luserid = ? "
            	+ "\n       , ldate = ? "
            	+ "\n   where subj = ? "
            	+ "\n   and   year = ? "
            	+ "\n   and   mm = ? ";
            	
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(pidx++, box.getString("p_start_date"));	// 시작일
            pstmt.setString(pidx++, box.getString("p_end_date"));	// 종료일
            pstmt.setInt   (pidx++, box.getInt("p_study_count"));	// 수료정보 : 횟수
            pstmt.setInt   (pidx++, box.getInt("p_study_time"));	// 수료정보 : 시간
            pstmt.setInt   (pidx++, box.getInt("p_study_chasi"));	// 수료정보 : 차시
            pstmt.setString(pidx++, v_luserid);						// 생성자
            pstmt.setString(pidx++, FormatDate.getDate("yyyyMMddHHmmss"));	// 생성일
            pstmt.setString(pidx++, v_subj);						// 과정코드
            pstmt.setString(pidx++, v_year);						// 교육연도
            pstmt.setString(pidx++, box.getString("p_mm"));			// 월차
            
            isOk = pstmt.executeUpdate();

            if ( isOk == 1 ) {
                connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
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
	 * 매월정기안전교육 월차 중복체크
	 * @param connMgr
	 * @param v_grcode
	 * @param v_gyear
	 * @param v_grseq
	 * @param v_subj
	 * @param v_mm
	 * @return
	 * @throws Exception
	 */
    public int getDuplSubjMm(DBConnectionManager connMgr, String v_year, String v_subj, String v_mm) throws Exception {
        ListSet ls  = null;
        String  sql = "";
        int    cnt = 0;
        
        try { 
        	sql = "\n select count(subj) as cnt "
        		+ "\n from   tz_subj_mm "
        		+ "\n where  subj   = " + SQLString.Format(v_subj)
        		+ "\n and    year   = " + SQLString.Format(v_year)
        		+ "\n and    mm     = " + SQLString.Format(v_mm);

            ls = connMgr.executeQuery(sql);
       
            if (ls.next()) {
            	cnt   = ls.getInt("cnt");
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return cnt;
    }

    /**
     * 매월 정기안전교육 삭제
     * @param box
     * @return
     * @throws Exception 
     */
	public int DeleteSubjectMm(RequestBox box) throws Exception {
        DBConnectionManager 	connMgr 		= null;
        PreparedStatement 		pstmt 			= null;
        String sql  = "";
        int   isOk1 = 0;
        int   isOk2 = 0;

        String v_year   = box.getString ("p_year"); // 년도
        String v_subj   = box.getString ("p_subj");
        
        int pidx = 1;
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // delete tz_subj_mm table
            sql = "\n delete from tz_subj_mm "
            	+ "\n   where subj = ? "
            	+ "\n   and   year = ? "
            	+ "\n   and   mm = ? ";
            	
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(pidx++, v_subj);						// 과정코드
            pstmt.setString(pidx++, v_year);						// 년도
            pstmt.setString(pidx++, box.getString("p_mm"));			// 월차
            
            isOk1 = pstmt.executeUpdate();

            // delete tz_subj_mm_lesson table
            sql = "\n delete from tz_subj_mm_lesson "
            	+ "\n   where subj = ? "
            	+ "\n   and   year = ? "
            	+ "\n   and   mm = ? ";
            	
            pstmt = connMgr.prepareStatement(sql);
            pidx = 1;
            pstmt.setString(pidx++, v_subj);						// 과정코드
            pstmt.setString(pidx++, v_year);						// 년도
            pstmt.setString(pidx++, box.getString("p_mm"));			// 월차
            
            isOk2 = pstmt.executeUpdate();

            if ( isOk1 == 1 ) {
                connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
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
            
        return isOk1;
	}

    /**
     * 매월정기안전교육 전체 차시목록
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList TargetLessonList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls   = null;
        ArrayList   list = null;
        String      sql  = "";
        DataBox     dbox = null;
        
        String v_subj   = box.getString("p_subj");	// 과정코드

        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            sql = "\n select module "
            	+ "\n      , lesson "
            	+ "\n      , sdesc "
            	+ "\n from   tz_subjlesson "
            	+ "\n where  subj   = " + StringManager.makeSQL(v_subj);

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
     * 매월정기안전교육 지정된 차시목록
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList SelectedLessonList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls   = null;
        ArrayList   list = null;
        String      sql  = "";
        DataBox     dbox = null;
        
        String v_subj   = box.getString("p_subj");	// 과정코드
        String v_year   = box.getString("p_year");	// 연도
        String v_mm     = box.getString("p_mm");	// 월차

        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            sql = "\n select a.subj  "
            	+ "\n      , a.year  "
            	+ "\n      , a.mm  "
            	+ "\n      , a.module "
            	+ "\n      , a.lesson  "
            	+ "\n      , b.sdesc "
            	+ "\n from   tz_subj_mm_lesson a, tz_subjlesson b "
            	+ "\n where  a.subj   = b.subj(+) "
            	+ "\n and    a.module = b.module(+) "
            	+ "\n and    a.lesson = b.lesson(+) "
            	+ "\n and    a.subj   = " + StringManager.makeSQL(v_subj)
            	+ "\n and    a.year   = " + StringManager.makeSQL(v_year)
            	+ "\n and    a.mm     = " + StringManager.makeSQL(v_mm);
            
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
     * 매월 정기안전교육 차시저장
     * @param box
     * @return
     * @throws Exception 
     */
	public int SaveSubjectMmLesson(RequestBox box) throws Exception {
        DBConnectionManager 	connMgr 		= null;
        PreparedStatement 		pstmt 			= null;
        String sql  = "";
        int   isOk1 = 0;
        int   isOk2 = 0;

        String v_luserid= box.getSession("userid");
        String v_subj   = box.getString ("p_subj"); // 과정코드
        String v_year   = box.getString ("p_year"); // 년도
        String v_mm     = box.getString ("p_mm"); // 월차
        String v_lesson = box.getString ("p_selectedlessons"); // 레슨

        int pidx = 1;
        String a_lesson[];
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql = "\n delete from tz_subj_mm_lesson "
            	+ "\n   where subj = ? "
            	+ "\n   and   year = ? "
            	+ "\n   and   mm = ? ";
            	
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(pidx++, v_subj);	// 과정코드
            pstmt.setString(pidx++, v_year);	// 년도
            pstmt.setString(pidx++, v_mm);		// 월차
            
            isOk1 = pstmt.executeUpdate();
            
            if (v_lesson.length() > 0) {
            	a_lesson = v_lesson.split(";");
            
	            for (int i=0; i<a_lesson.length; i++) {
	            	
	            	sql = "\n insert into tz_subj_mm_lesson "
	            		+ "\n        (subj, year, mm, module, lesson, luserid, ldate) "
	            		+ "\n values (   ?,    ?,  ?,      ?,      ?,       ?, to_char(sysdate, 'yyyymmddhh24miss'))";
	            	
	            	pstmt = connMgr.prepareStatement(sql);
	            	pidx = 1;
	            	pstmt.setString(pidx++, v_subj);		// 과정코드
	            	pstmt.setString(pidx++, v_year);		// 년도
	            	pstmt.setString(pidx++, v_mm);			// 월차
	            	pstmt.setString(pidx++, a_lesson[i].substring(0, a_lesson[i].indexOf(",")));		// 모듈
	            	pstmt.setString(pidx++, a_lesson[i].substring(a_lesson[i].indexOf(",")+1, a_lesson[i].length()));	// 레슨
	            	pstmt.setString(pidx++, v_luserid);		// 최종수정자
	            	
	            	isOk2 = pstmt.executeUpdate();   
	            	
	            	if ( pstmt != null ) { pstmt.close(); }
	            }
            }
            if ( isOk2 > 0) {
                connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
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
            
        return isOk2;
	}	    

    /**
     * 매월정기안전교육 과정별 지정된 차시 목록
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList SelectedLessonAllList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls   = null;
        ArrayList   list = null;
        String      sql  = "";
        DataBox     dbox = null;
        
        String s_subj   = box.getString("s_subjcourse");	// 과정코드
        String s_year   = box.getString("s_gyear");	// 연도

        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();		
            
            sql = "\n select a.subj  "
            	+ "\n      , a.year  "
            	+ "\n      , a.mm  "
            	+ "\n      , a.module "
            	+ "\n      , a.lesson  "
            	+ "\n      , b.sdesc "
            	+ "\n from   tz_subj_mm_lesson a, tz_subjlesson b "
            	+ "\n where  a.subj   = b.subj(+) "
            	+ "\n and    a.module = b.module(+) "
            	+ "\n and    a.lesson = b.lesson(+) "
            	+ "\n and    a.subj   = " + StringManager.makeSQL(s_subj)
            	+ "\n and    a.year   = " + StringManager.makeSQL(s_year)
            	+ "\n order  by a.mm, a.module, a.lesson ";
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
     * 매월정기안전교육 통계
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList SelectSubjMmStatList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls   = null;
        ArrayList   list = null;
        String      sql  = "";
        DataBox     dbox = null;
        
        String ss_gyear     = box.getStringDefault("s_gyear","ALL");     	// 년도
        String ss_subjcourse= box.getStringDefault("s_subjcourse","ALL");	// 과정
        String ss_subjseq   = box.getStringDefault("s_subjseq","ALL");   	// 과정 기수
        String ss_action    = box.getString("s_action");
        String v_orderColumn= box.getString("p_orderColumn");           	// 정렬할 컬럼명
        String v_orderType  = box.getString("p_orderType");           		// 정렬할 순서

        String v_mm = FormatDate.getDate("MM");

        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();		
            
            String v_start_date = box.getStringDefault("p_start_date", SelectSubjMmSdate(connMgr, ss_gyear, ss_subjcourse, v_mm));
            String v_end_date   = box.getStringDefault("p_end_date"  , SelectSubjMmEdate(connMgr, ss_gyear, ss_subjcourse, v_mm));
            
            sql = "\n select subj, year, subjseq, subjnm, position_nm, post_nm, userid "
                + "\n      , name, handphone, email "
                + "\n      , study_time "
                + "\n      , trunc(study_time/60)||':'||lpad(mod(study_time,60),2,'0') as dis_study_time "
                + "\n      , study_count "
                + "\n      , study_chasi "
                + "\n      , attention "
                + "\n      , proj_cnt, proj_usercnt "
                + "\n      , mexam_cnt, mexam_usercnt "
                + "\n      , fexam_cnt, fexam_usercnt "
                + "\n      , score "
                + "\n      , decode(subjseq, newsubjseq, '', newsubjseq) as newsubjseq "
                + "\n from  ( "
                + "\n       select a.subj " 
                + "\n            , a.year  "
                + "\n            , a.subjseq "  
                + "\n            , c.subjnm   "
                + "\n            , b.position_nm "  
                + "\n            , get_postnm(b.post) as post_nm "  
                + "\n            , a.userid   "
                + "\n            , b.name   "
                + "\n            , b.handphone "  
                + "\n            , b.email   "
                + "\n            , get_studytime(a.subj, a.year, a.subjseq, a.userid, "+StringManager.makeSQL(v_start_date)+", "+StringManager.makeSQL(v_end_date)+") as study_time " 
                + "\n            , get_studycount(a.subj, a.year, a.subjseq, a.userid, "+StringManager.makeSQL(v_start_date)+", "+StringManager.makeSQL(v_end_date)+") as study_count " 
                + "\n            , get_studychasi (a.subj, a.year, a.subjseq, "+ StringManager.makeSQL(v_mm)+ ", a.userid, "+StringManager.makeSQL(v_start_date)+", "+StringManager.makeSQL(v_end_date)+") as study_chasi " 
                + "\n            , get_attention (a.subj, a.year, a.subjseq, "+ StringManager.makeSQL(v_mm)+ ", a.userid) as attention  "
                + "\n            , nvl((  "
                + "\n               select count(distinct projgubun) " 
                + "\n               from   tz_projgrp  "
                + "\n               where  subj = a.subj " 
                + "\n               and    year = a.year  "
                + "\n               and    subjseq = a.subjseq " 
                + "\n               group by  "
                + "\n                      subj " 
                + "\n                    , year  "
                + "\n                    , subjseq),0) as proj_cnt " 
                + "\n            , nvl((  "
                + "\n               select count(distinct grpseq) "  
                + "\n               from   tz_projrep  "
                + "\n               where  subj = a.subj  "
                + "\n               and    year = a.year " 
                + "\n               and    subjseq = a.subjseq " 
                + "\n               and    projid = a.userid  "
                + "\n               group by  "
                + "\n                      subj " 
                + "\n                    , year  "
                + "\n                    , subjseq " 
                + "\n                    , projid),0) as proj_usercnt " 
                + "\n            , nvl((  "
                + "\n               select count(papernum) " 
                + "\n               from   tz_exampaper  "
                + "\n               where  subj = a.subj  "
                + "\n               and    year = a.year  "
                + "\n               and    subjseq = a.subjseq " 
                + "\n               and    examtype = 'M' " 
                + "\n               group by  "
                + "\n                      subj  "
                + "\n                    , year " 
                + "\n                    , subjseq),0) as mexam_cnt " 
                + "\n            , nvl((  "
                + "\n               select count(papernum) " 
                + "\n               from   tz_exampaper  "
                + "\n               where  subj = a.subj  "
                + "\n               and    year = a.year  "
                + "\n               and    subjseq = a.subjseq " 
                + "\n               and    examtype = 'F' " 
                + "\n               group by  "
                + "\n                      subj  "
                + "\n                    , year " 
                + "\n                    , subjseq),0) as fexam_cnt " 
                + "\n            , nvl((  "
                + "\n               select count(papernum) "  
                + "\n               from   tz_examresult " 
                + "\n               where  subj = a.subj " 
                + "\n               and    year = a.year  "
                + "\n               and    subjseq = a.subjseq " 
                + "\n               and    userid = a.userid  "
                + "\n               and    examtype = 'M' " 
                + "\n               group by  "
                + "\n                      subj  "
                + "\n                    , year " 
                + "\n                    , subjseq " 
                + "\n                    , userid),0) as mexam_usercnt " 
                + "\n            , nvl((  "
                + "\n               select count(papernum) "  
                + "\n               from   tz_examresult " 
                + "\n               where  subj = a.subj " 
                + "\n               and    year = a.year  "
                + "\n               and    subjseq = a.subjseq " 
                + "\n               and    userid = a.userid  "
                + "\n               and    examtype = 'F' " 
                + "\n               group by  "
                + "\n                      subj  "
                + "\n                    , year " 
                + "\n                    , subjseq " 
                + "\n                    , userid),0) as fexam_usercnt " 
                + "\n            , a.score  "
                + "\n            , d.newsubjseq "
                + "\n       from   tz_student a   "
                + "\n            , tz_member b "  
                + "\n            , tz_subj c   "
                + "\n            , (select subj, year, subjseq, userid, newsubjseq "
                + "\n               from   tz_student_move a "
                + "\n               where  ldate = (select max(ldate) "
                + "\n                               from   tz_student_move "
                + "\n                               where  subj = a.subj "
                + "\n                               and    year = a.year "
                + "\n                               and    subjseq = a.subjseq "
                + "\n                               and    userid = a.userid)) d "
                + "\n       where  a.userid 		= b.userid   "
                + "\n       and    a.subj   		= c.subj "
                + "\n       and    a.subj   		= d.subj(+) "
                + "\n       and    a.year   		= d.year(+) "
                + "\n       and    a.subjseq   		= d.subjseq(+) "
                + "\n       and    a.userid   		= d.userid(+) "
                + "\n       and    a.year        	= " + SQLString.Format(ss_gyear)
                + "\n       and    a.subj       	= " + SQLString.Format(ss_subjcourse)
                + "\n       and    a.subjseq    	= " + SQLString.Format(ss_subjseq)
            	+ "\n ) ";

            if ( v_orderColumn.equals("post_nm"))       v_orderColumn ="post_nm";
            if ( v_orderColumn.equals("position_nm"))   v_orderColumn ="position_nm";
            if ( v_orderColumn.equals("name"))          v_orderColumn ="name";
            if ( v_orderColumn.equals("userid"))        v_orderColumn ="userid";
            if ( v_orderColumn.equals("handphone"))     v_orderColumn ="handphone";
            if ( v_orderColumn.equals("study_chasi"))   v_orderColumn ="study_chasi";
            if ( v_orderColumn.equals("study_time"))    v_orderColumn ="study_time";
            if ( v_orderColumn.equals("study_count"))   v_orderColumn ="study_count";
            if ( v_orderColumn.equals("attention"))     v_orderColumn ="attention";
            if ( v_orderColumn.equals("proj_usercnt"))  v_orderColumn ="proj_usercnt";
            if ( v_orderColumn.equals("mexam_usercnt")) v_orderColumn ="mexam_usercnt";
            if ( v_orderColumn.equals("fexam_usercnt")) v_orderColumn ="fexam_usercnt";
            if ( v_orderColumn.equals("score"))     	 v_orderColumn ="score";
            if ( v_orderColumn.equals("proj_cnt"))     	 v_orderColumn ="proj_cnt";
 
            if ( v_orderColumn.equals("") ) { 
                sql += "\n order  by name ";
            } else { 
                sql += "\n order  by " + v_orderColumn + v_orderType;
            }
                
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
     * 매월정기안전교육 월차별 결과 출력시 수료처리한 월만 가져오기
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList getSelectMonth(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls   = null;
        ArrayList   list = null;
        String      sql  = "";
        DataBox     dbox = null;
        
        String ss_gyear     = box.getStringDefault("s_gyear","ALL");     	// 년도
        String ss_subjcourse= box.getStringDefault("s_subjcourse","ALL");	// 과정

        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();		

            sql = "\n select distinct mm "
            	+ "\n from   tz_stold_mm_temp "
            	+ "\n where  year = " + SQLString.Format(ss_gyear)
                + "\n and    subj = " + SQLString.Format(ss_subjcourse)
                + "\n order  by mm ";
            
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
     * 매월정기안전교육 통계
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList SelectSubjMmStatListM(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls   = null;
        ArrayList   list = null;
        String      sql  = "";
        DataBox     dbox = null;
        
        String ss_gyear     = box.getStringDefault("s_gyear","ALL");     	// 년도
        String ss_subjcourse= box.getStringDefault("s_subjcourse","ALL");	// 과정
        String ss_subjseq   = box.getStringDefault("s_subjseq","ALL");   	// 과정 기수
        String ss_class     = box.getStringDefault("s_class","ALL");     	// 클래스
        String ss_action    = box.getString("s_action");
        String v_orderColumn= box.getString("p_orderColumn");           	// 정렬할 컬럼명
        String v_orderType  = box.getString("p_orderType");           		// 정렬할 순서

        String v_mm  = box.getString("p_mm");           					// 월선택
        DataBox mdbox = SelectSubjMmStanard(box);
        
        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();		

            sql = "\n select a.subj "
            	+ "\n      , a.year "
            	+ "\n      , a.subjseq "
            	+ "\n      , c.subjnm "
            	+ "\n      , a.userid "
            	+ "\n      , b.name "
            	+ "\n      , b.position_nm "
             	+ "\n      , f_bonbu_nm(hq_org_cd) hq_org_cd "
             	+ "\n      , f_bonbu_nm(agency_cd) agency_cd "
             	+ "\n      , f_bonbu_nm(dept_cd) dept_cd "
            	+ "\n      , get_postnm(b.post) as post_nm "
            	+ "\n      , b.email "
            	+ "\n      , b.handphone "
            	+ "\n      , a.study_time "
                + "\n      , trunc(a.study_time/60)||':'||lpad(mod(a.study_time,60),2,'0') as dis_study_time "
            	+ "\n      , a.study_count "
            	+ "\n      , a.study_chasi "
            	+ "\n      , decode(a.isgraduate,'Y','수료','N','미수료','') isgraduate "
            	+ "\n from   tz_stold_mm_temp a "
            	+ "\n      , tz_member b "
            	+ "\n      , tz_subj c "
            	+ "\n where  a.userid = b.userid "
            	+ "\n and    a.subj = c.subj "
            	+ "\n and    a.subj = " + SQLString.Format(ss_subjcourse)
            	+ "\n and    a.year = " + SQLString.Format(ss_gyear)
            	+ "\n and    a.mm   = " + SQLString.Format(v_mm)
            	+ "\n order  by a.subjseq, b.name ";
                
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
     * 매월정기안전교육 통계
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList SelectSubjMmStatListY(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls   = null;
        ArrayList   list = null;
        String      sql  = "";
        DataBox     dbox = null;
        
        String ss_gyear     = box.getStringDefault("s_gyear","ALL");     	// 년도
        String ss_subjcourse= box.getStringDefault("s_subjcourse","ALL");	// 과정
        String ss_subjseq   = box.getStringDefault("s_subjseq","ALL");   	// 과정 기수

        String[] v_mm = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        
        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();		
            sql = "\n select a.subj "
            	+ "\n      , a.year "
            	+ "\n      , a.subjseq "
            	+ "\n      , a.userid "
            	+ "\n      , max(c.subjnm) as subjnm "
            	+ "\n      , max(d.name) as name "
            	+ "\n      , max(d.position_nm) as position_nm "
            	+ "\n      , max(get_postnm(d.post)) as post_nm "
            	+ "\n      , max(d.handphone) as handphone "
            	+ "\n      , max(d.email) as email ";
            
            for (int i=0; i<v_mm.length; i++) 
            {
        	sql+= "\n      , trunc(max(study_time"+v_mm[i]+")/60)||'' as study_time"+v_mm[i]
            	+ "\n      , max(study_count"+v_mm[i]+")||'' as study_count"+v_mm[i]
            	+ "\n      , max(study_chasi"+v_mm[i]+")||'' as study_chasi"+v_mm[i]
            	+ "\n      , max(isgraduate"+v_mm[i]+") as isgraduate"+v_mm[i];
            }
            sql+= "\n from   tz_student a "
            	+ "\n      , ( "
            	+ "\n         select subj "
            	+ "\n              , year "
            	+ "\n              , subjseq "
            	+ "\n              , userid ";
            
            for (int i=0; i<v_mm.length; i++) 
            {
        	sql+= "\n              , max(study_time"+v_mm[i]+") as study_time"+v_mm[i]
            	+ "\n              , max(study_count"+v_mm[i]+") as study_count"+v_mm[i]
            	+ "\n              , max(study_chasi"+v_mm[i]+") as study_chasi"+v_mm[i]
            	+ "\n              , max(isgraduate"+v_mm[i]+") as isgraduate"+v_mm[i];
            }
            sql+= "\n         from   ( "
            	+ "\n                 select subj "
            	+ "\n                      , year "
            	+ "\n                      , subjseq "
            	+ "\n                      , mm "
            	+ "\n                      , userid ";

            for (int i=0; i<v_mm.length; i++) 
            {
            sql+= "\n                      , decode(mm, '"+v_mm[i]+"', study_time, '') as study_time"+v_mm[i]
            	+ "\n                      , decode(mm, '"+v_mm[i]+"', study_count, '') as study_count"+v_mm[i]
            	+ "\n                      , decode(mm, '"+v_mm[i]+"', study_chasi, '') as study_chasi"+v_mm[i]
            	+ "\n                      , decode(mm, '"+v_mm[i]+"', isgraduate, '') as isgraduate"+v_mm[i];
            }
            sql+= "\n                 from   tz_stold_mm_temp "
            	+ "\n                ) "
            	+ "\n         group  by subj, year, subjseq, userid "
            	+ "\n        ) b "
            	+ "\n      , tz_subj c "
            	+ "\n      , tz_member d "
            	+ "\n where  a.subj = b.subj(+) "
            	+ "\n and    a.year = b.year(+) "
            	+ "\n and    a.subjseq = b.subjseq(+) "
            	+ "\n and    a.userid = b.userid(+) "
            	+ "\n and    a.subj = c.subj "
            	+ "\n and    a.userid = d.userid "
            	+ "\n and    a.subj    = " + StringManager.makeSQL(ss_subjcourse)
            	+ "\n and    a.year    = " + StringManager.makeSQL(ss_gyear)
            	+ "\n and    a.subjseq = " + StringManager.makeSQL(ss_subjseq)
            	+ "\n group  by a.subj "
            	+ "\n         , a.year "
            	+ "\n         , a.subjseq "
            	+ "\n         , a.userid "
            	+ "\n order  by name ";

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
     * 매월정기안전교육 수료기준 조회
     * @param box
     * @return
     * @throws Exception
     */
    public DataBox SelectSubjMmStanard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls   = null;
        String      sql  = "";
        DataBox     dbox = null;
        
        String ss_gyear     = box.getStringDefault("s_gyear","ALL");     			// 년도
        String ss_subjcourse= box.getStringDefault("s_subjcourse","ALL");			// 과정
        String v_mm 		= box.getStringDefault("p_mm",FormatDate.getDate("MM"));// 월

        try { 
            connMgr = new DBConnectionManager();
            
            sql = "\n select study_count "
                + "\n      , study_time  "
                + "\n      , study_chasi "
                + "\n      , start_date "
                + "\n      , end_date "
                + "\n from   tz_subj_mm "
                + "\n where  subj = " + SQLString.Format(ss_subjcourse)
                + "\n and    year = " + SQLString.Format(ss_gyear)
                + "\n and    mm = " + SQLString.Format(v_mm);
            
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox(); 
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
        
        return dbox;
	}

    /**
     * 매월정기안전교육 월의 시작일자
     * @param box
     * @return
     * @throws Exception
     */
    public String SelectSubjMmSdate(DBConnectionManager connMgr, String a_year, String a_subj, String a_mm) throws Exception { 
        ListSet     ls   = null;
        String      sql  = "";
        String		v_retVal = "";
        
        try { 
            sql = "\n select start_date "
                + "\n from   tz_subj_mm "
                + "\n where  subj = " + SQLString.Format(a_subj)
                + "\n and    year = " + SQLString.Format(a_year)
                + "\n and    mm   = " + SQLString.Format(a_mm);
            
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
            	v_retVal = ls.getString("start_date");
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return v_retVal;
	}

    /**
     * 매월정기안전교육 월의 종료일자
     * @param box
     * @return
     * @throws Exception
     */
    public String SelectSubjMmEdate(DBConnectionManager connMgr, String a_year, String a_subj, String a_mm) throws Exception { 
        ListSet     ls   = null;
        String      sql  = "";
        String		v_retVal = "";
        
        try { 
            sql = "\n select end_date "
                + "\n from   tz_subj_mm "
                + "\n where  subj = " + SQLString.Format(a_subj)
                + "\n and    year = " + SQLString.Format(a_year)
                + "\n and    mm   = " + SQLString.Format(a_mm);
            
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
            	v_retVal = ls.getString("end_date");
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" +sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return v_retVal;
	}
    

    /**
     * 매월정기안전교육 기수이동
     * @param box
     * @return
     * @throws Exception 
     */
	public int insertStudentMove(RequestBox box) throws Exception {
        DBConnectionManager 	connMgr 		= null;
        PreparedStatement 		pstmt 			= null;
        ListSet ls = null;
        String sql = "";
        int   isOk = 0;

        String v_luserid = box.getSession("userid" );

        String v_year     	= box.getString("s_gyear"      );   // 교육년도
        String v_subj		= box.getString("s_subjcourse" );   // 과정
        String v_subjseq   	= box.getString("s_subjseq"    );   // 과정기수
        String v_newsubjseq = box.getString("p_movesubjseq");   // 이동할과정기수
        Vector v_checks   	= box.getVector("p_checks");
        String v_schecks  	= "";
        
        int pidx = 1;
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            for(int i = 0 ; i < v_checks.size() ; i++) {
            	
            	v_schecks = (String)v_checks.elementAt(i);
            	
                sql = "\n merge into "
                    + "\n       tz_student_move a "
                    + "\n using "
                    + "\n       ( "
                    + "\n        select "+StringManager.makeSQL(v_subj)+" as subj "
                    + "\n             , "+StringManager.makeSQL(v_year)+" as year "
                    + "\n             , "+StringManager.makeSQL(v_subjseq)+" as subjseq "
                    + "\n             , "+StringManager.makeSQL(v_schecks)+" as userid "
                    + "\n        from   dual "
                    + "\n       ) b "
                    + "\n on "
                    + "\n       ( "
                    + "\n            a.subj = b.subj "
                    + "\n        and a.year = b.year "
                    + "\n        and a.subjseq = b.subjseq "
                    + "\n        and a.userid = b.userid "
                    + "\n       ) "
                    + "\n when matched then "
                    + "\n         update set newsubjseq = "+StringManager.makeSQL(v_newsubjseq)
                    + "\n                  , luserid    = "+StringManager.makeSQL(v_luserid)
                    + "\n                  , ldate      = to_char(sysdate, 'yyyymmddhh24miss') "
                    + "\n when not matched then "
                    + "\n         insert ( subj  "
                    + "\n                , year  "
                    + "\n                , subjseq " 
                    + "\n                , userid  "
                    + "\n                , newsubjseq  "
                    + "\n                , luserid  "
                    + "\n                , ldate)  "
                    + "\n         values ( "+StringManager.makeSQL(v_subj)
                    + "\n                , "+StringManager.makeSQL(v_year)
                    + "\n                , "+StringManager.makeSQL(v_subjseq)
                    + "\n                , "+StringManager.makeSQL(v_schecks)
                    + "\n                , "+StringManager.makeSQL(v_newsubjseq)
                    + "\n                , "+StringManager.makeSQL(v_luserid)
                    + "\n                , to_char(sysdate, 'yyyymmddhh24miss')) ";

                isOk += connMgr.executeUpdate(sql);
                
        	}

            if ( isOk > 0 ) {
                connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
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

    public static String getSubjseq(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;        
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        String              result          = "";
        
        String              ss_gyear        = box.getStringDefault("s_gyear"        , "ALL");   // 교육년도
        String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // 과목
        String              ss_subjseq      = box.getStringDefault("s_subjseq"      , "ALL");   // 과목기수
        
        try {
            connMgr = new DBConnectionManager();
            
            sbSQL.append(" select  distinct                                                	\n")                                       
                 .append("         a.subjseq as subjseq                                    	\n")
                 .append("     ,   a.subjseq                                               	\n")
                 .append(" from    tz_subjseq      a                                       	\n")
                 .append(" where   a.subj = " + SQLString.Format(ss_subjcourse) + "        	\n")
                 .append(" and     a.year = " + SQLString.Format(ss_gyear     ) + "        	\n")
                 //.append(" and     a.subjseq != " + SQLString.Format(ss_subjseq)+ "  		\n")
                 .append(" order   by a.subjseq										       	\n");

            pstmt   = connMgr.prepareStatement(sbSQL.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls      = new ListSet(pstmt);


            result = "<select name = \"p_movesubjseq\"> \r\n";

			while ( ls.next() ) {    
				ResultSetMetaData meta = ls.getMetaData();
       
				result += "<option value = \"" + ls.getString(1) + "\"> " + StringManager.cutZero( ls.getString(meta.getColumnCount())) + "기</option > \r\n";
			}
			
			result += "</select > \r\n";
			
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls         != null ) { try { ls.close();               } catch ( Exception e   ) { } }
            if ( pstmt      != null ) { try { pstmt.close();            } catch ( Exception e   ) { } }    
            if ( connMgr    != null ) { try { connMgr.freeConnection(); } catch ( Exception e   ) { } }
        }

        return result;
    }  

}