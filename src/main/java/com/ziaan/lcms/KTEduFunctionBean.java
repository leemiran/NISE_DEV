/*
 * @(#)KTEduFunctionBean.java	1.0 2008. 12. 25
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * KT 학습창 DAO - 생각나누기, 실습하기 Function
 *
 * @version		1.0, 2008/12/25
 * @author		Chung Jin-pil
 */
public class KTEduFunctionBean {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public KTEduFunctionBean() {
	}

	/**
	 * 생각나누기 나의의견 전체보기
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List selectThinkBoardSelfNoteAll(RequestBox box) throws Exception { 
        
		DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        
        List list = null;
        String sql = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            sql =
            	"\n  SELECT no, subj, year, subjseq, group_no, userid, content, ldate, get_name(userid) name,  " +
            	"\n         DECODE (LENGTH (group_no), 3, SUBSTR (group_no, 0, 1), DECODE (LENGTH (group_no), 4, SUBSTR (group_no, 0, 2), '') ) chasi,  " +
            	"\n         DECODE (LENGTH (group_no), 3, SUBSTR (group_no, 2, 3), DECODE (LENGTH (group_no), 4, SUBSTR (group_no, 3, 4), '') ) page  " +
            	"\n    FROM tz_think_board  " +
            	"\n   WHERE subj = #subj#  " +
            	"\n     AND year = #year#  " +
            	"\n     AND subjseq = #subjseq#  " +
            	"\n     AND userid = #userid#  ";
            
            sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
            sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
            sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
            sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid));
            
            logger.debug( "생각나누기 나의의견 전체보기 \n " + sql );
            ls = connMgr.executeQuery(sql);
            
            DataBox dbox = null;
            while ( ls.next() ) {
            	dbox = ls.getDataBox();
            	list.add(dbox);
            }
        } catch ( Exception ex ) { 
        	ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }	
	
	/**
	 * 생각나누기 목록
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List selectThinkBoardList(RequestBox box) throws Exception { 
        
		DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        
        List list = null;
        String sql = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            
            String p_groupNo= box.getString("group_no");
            // String p_groupSubject = box.getString("group_subject");
            
            int v_pageno = box.getInt("p_pageno");
            int row = 5;

            sql =
            	"\n  SELECT no, subj, year, subjseq, group_no, userid, content, ldate, get_name(userid) name  " +
            	"\n    FROM tz_think_board  " +
            	"\n   WHERE subj = #subj#  " +
            	"\n     AND year = #year#  " +
            	"\n     AND subjseq = #subjseq#  " +
            	"\n     AND group_no = #group_no#  " +
            	"\n   ORDER BY NO DESC  ";
            
            sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
            sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
            sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
            sql = sql.replaceAll("#group_no#", StringManager.makeSQL(p_groupNo));
            
            logger.debug( "생각나누기 목록  \n " + sql );
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                         // 페이지당 row 갯수를 세팅한다.
            ls.setCurrentPage(v_pageno);                 // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();    // 전체 페이지 수를 반환한다.
            int total_row_count = ls.getTotalCount();  	 // 전체 row 수를 반환한다.
            
            DataBox dbox = null;
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
        	ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

	/**
	 * 생각나누기 저장
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public boolean insertThinkBoard(RequestBox box) throws Exception {
    	 
		DBConnectionManager connMgr = null;
    	PreparedStatement pstmt = null;
    	 
    	String sql = "";
    	int count = 0;

    	try {
    		connMgr    = new DBConnectionManager();
          	   
            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            String p_groupNo= box.getString("group_no");
            String p_content = box.getString("content");

    		sql = 
    			"\n  INSERT INTO TZ_THINK_BOARD (  " +
    			"\n     NO, SUBJ, YEAR,   " +
    			"\n     SUBJSEQ, GROUP_NO, USERID,   " +
    			"\n     CONTENT, LDATE)   " +
    			"\n  VALUES ( (SELECT NVL(MAX(NO),0)+1 FROM TZ_THINK_BOARD), ?, ?,  " +
    			"\n      ?, ?, ?,  " +
    			"\n      ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') )  ";
    		
	        pstmt = connMgr.prepareStatement(sql);

	        pstmt.setString( 1, s_subj);
	        pstmt.setString( 2, s_year);
	        pstmt.setString( 3, s_subjseq);
	        pstmt.setString( 4, p_groupNo);
	        pstmt.setString( 5, s_userid);
	        pstmt.setString( 6, p_content);

	        count = pstmt.executeUpdate();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		ErrorManager.getErrorStackTrace(ex, box, sql);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	} finally {
    		 if ( pstmt != null ) { try { pstmt.close(); } catch (Exception e) {} }
			 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
		}
    	
    	return count > 0;
	}	

	/**
	 * 생각나누기 삭제
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public boolean deleteThinkBoard(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
    	PreparedStatement   pstmt   = null;

    	String sql = "";
    	int count = 0;

    	try {
    		connMgr    = new DBConnectionManager();

            String s_userid = box.getSession("userid");
            int p_no = box.getInt("p_no");
    		
    		sql =
    			"\n  DELETE FROM tz_think_board  " +
    			"\n        WHERE NO = ?   " +
    			"\n          AND userid = ?  ";
    			
	        pstmt   = connMgr.prepareStatement(sql);

	        pstmt.setInt(1, p_no);
	        pstmt.setString(2, s_userid);

	        count = pstmt.executeUpdate();
	  } catch(Exception ex) {
		  	ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
	  } finally {
		  if ( pstmt != null ) { try { pstmt.close(); } catch (Exception e) {} }	
		  if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
	  }

	  return count > 0;
	}

	/**
	 * 실습하기 목록
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List selectSilsupList(RequestBox box) throws Exception {
        
		DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        
        List list = null;
        String sql = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            sql =
            	"\n  SELECT   lesson,  " +
            	"\n           MIN (DECODE (try, 1, a.errornum)) errornum1,  " +
            	"\n           MIN (DECODE (try, 1, a.steptime)) steptime1,  " +
            	"\n           MIN (DECODE (try, 2, a.errornum)) errornum2,  " +
            	"\n           MIN (DECODE (try, 2, a.steptime)) steptime2,  " +
            	"\n           MIN (DECODE (try, 3, a.errornum)) errornum3,  " +
            	"\n           MIN (DECODE (try, 3, a.steptime)) steptime3,  " +
            	"\n           MIN (DECODE (try, 4, a.errornum)) errornum4,  " +
            	"\n           MIN (DECODE (try, 4, a.steptime)) steptime4  " +
            	"\n      FROM (  SELECT lesson, try, errornum, steptime  " +
            	"\n                FROM tz_silsup  " +
            	"\n               WHERE subj = #subj#  " +
            	"\n                 AND YEAR = #year#  " +
            	"\n                 AND subjseq = #subjseq#  " +
            	"\n                 AND userid = #userid#  " +
            	"\n            ORDER BY lesson, try) a  " +
            	"\n  GROUP BY lesson   ";
            
            sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
            sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
            sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
            sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid));
            
            logger.debug( "생각나누기 목록  \n " + sql );
            ls = connMgr.executeQuery(sql);
            
            DataBox dbox = null;
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
        	ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;		
	}

	/**
	 * 실습하기 저장
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public boolean insertSilsup(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
    	PreparedStatement pstmt = null;
    	 
    	String sql = "";
    	int count = 0;

    	try {
    		connMgr    = new DBConnectionManager();
          	   
            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            String p_lesson = box.getString("p_lesson");
            String p_errorNum = box.getString("errorNum");
            String p_stepTime = box.getString("stepTime");

    		sql = 
    			"\n  INSERT INTO TZ_SILSUP (  " +
    			"\n     NO, SUBJ, YEAR,   " +
    			"\n     SUBJSEQ, LESSON, TRY,   " +
    			"\n     USERID, ERRORNUM, STEPTIME, LDATE )   " +
    			"\n  VALUES ( (SELECT NVL(MAX(NO),0)+1 FROM TZ_SILSUP), ?, ?,  " +
    			"\n      ?, ?, (SELECT NVL (MAX (try), 0) + 1 FROM tz_silsup WHERE subj = ? AND YEAR = ? AND subjseq = ? AND lesson = ? AND userid = ?),  " +
    			"\n      ?, ?, ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') )  ";
    		
	        pstmt = connMgr.prepareStatement(sql);

	        pstmt.setString( 1, s_subj);
	        pstmt.setString( 2, s_year);
	        pstmt.setString( 3, s_subjseq);
	        pstmt.setString( 4, p_lesson);
	        pstmt.setString( 5, s_subj);
	        pstmt.setString( 6, s_year);
	        pstmt.setString( 7, s_subjseq);
	        pstmt.setString( 8, p_lesson);
	        pstmt.setString( 9, s_userid);
	        pstmt.setString( 10, s_userid);
	        pstmt.setString( 11, p_errorNum);
	        pstmt.setString( 12, p_stepTime);
	        
	        logger.debug( "실습하기 저장 \n " + sql );
	        count = pstmt.executeUpdate();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		ErrorManager.getErrorStackTrace(ex, box, sql);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	} finally {
    		 if ( pstmt != null ) { try { pstmt.close(); } catch (Exception e) {} }
			 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
		}
    	
    	return count > 0;
	}

	/**
	 * O, X 저장하기
	 * 
	 * @param box
	 * @return
	 */
	public boolean insertOX(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
    	PreparedStatement pstmt = null;
    	ListSet ls = null;
    	 
    	String sql = "";
    	int count = 0;

    	try {
    		connMgr    = new DBConnectionManager();
          	   
            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            String lesson = box.getString("lesson");
            String num = box.getString("num");

            sql =
            	"\n  SELECT COUNT(*) cnt FROM TZ_OX_BOARD  " +
            	"\n   WHERE SUBJ = #subj# AND YEAR = #year# AND SUBJSEQ = #subjseq# AND USERID = #userid# AND LESSON = #lesson#";

            sql = sql.replaceAll( "#subj#", StringManager.makeSQL(s_subj) );
            sql = sql.replaceAll( "#year#", StringManager.makeSQL(s_year) );
            sql = sql.replaceAll( "#subjseq#", StringManager.makeSQL(s_subjseq) );
            sql = sql.replaceAll( "#userid#", StringManager.makeSQL(s_userid) );
            sql = sql.replaceAll( "#lesson#", StringManager.makeSQL(lesson) );
            
	        ls = connMgr.executeQuery(sql);
	        if ( ls.next() ) {
	        	count = ls.getInt("cnt");
	        }
	        
	        if ( count == 0 ) { 
	    		sql = 
	    			"\n  INSERT INTO TZ_OX_BOARD (  " +
	    			"\n     NO, SUBJ, YEAR, SUBJSEQ, USERID,   " +
	    			"\n     LESSON, NUM, LDATE )   " +
	    			"\n  VALUES ( (SELECT NVL(MAX(NO),0)+1 FROM TZ_OX_BOARD), ?, ?, ?, ?,  " +
	    			"\n      ?, ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') )  ";
	    		
		        pstmt = connMgr.prepareStatement(sql);
	
		        pstmt.setString( 1, s_subj);
		        pstmt.setString( 2, s_year);
		        pstmt.setString( 3, s_subjseq);
		        pstmt.setString( 4, s_userid);
		        pstmt.setString( 5, lesson);
		        pstmt.setString( 6, num);
		        
		        logger.debug( "O,X하기 저장 \n " + sql );
		        count = pstmt.executeUpdate();
	        }
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		ErrorManager.getErrorStackTrace(ex, box, sql);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	} finally {
    		 if ( ls != null ) { try { ls.close(); } catch (Exception e) {} }
    		 if ( pstmt != null ) { try { pstmt.close(); } catch (Exception e) {} }
			 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
		}
    	
    	return count > 0;
	}
}
