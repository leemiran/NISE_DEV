/*
 * @(#)KTEduStartBean.java	1.0 2008. 11. 20
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * KT �н�â DAO
 *
 * @version		1.0, 2008/11/20
 * @author		Chung Jin-pil
 */
public class KTEduStartBean {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public KTEduStartBean() {
	}

	/**
	 * ���� ������ �����´�. (Ajax)
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List selectLessonList(RequestBox box) throws Exception {
		DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        
        List list = null;

        String sql = "";
        
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            String progressTableName = getProgressTableName(box);
            
            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            sql =
            	"\n  SELECT a.subj, a.module, a.lesson, a.sdesc, a.starting,   " +
            	"\n         b.total_time, b.first_edu, b.first_end, b.lesson_count, b.ldate  " +
            	"\n    FROM tz_subjlesson a, #progressTableName# b  " +
            	"\n   WHERE a.subj = #subj#  " + 
            	"\n     AND b.subj(+) = a.subj  " +
            	"\n     AND b.YEAR(+) = #year#  " +
            	"\n     AND b.subjseq(+) = #subjseq#  " +
            	"\n     AND b.lesson(+) = a.lesson  " +
            	"\n     AND b.userid(+) = #userid#  ";
            
            sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
            sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
            sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
            sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid));
            sql = sql.replaceAll( "#progressTableName#", progressTableName );
            
            logger.debug( "���� ���� \n " + sql );
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {
            	list.add( ls.getDataBox() );
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
	 * ���� ������ġ�� �����´�
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public String selectLessonStarting(RequestBox box) throws Exception {
		DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        
        String sql = "";
        String starting = "";
        
        try { 
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            String v_module = box.getString("p_module");
            String v_lesson = box.getString("p_lesson");

            sql =
            	"\n  SELECT a.subj, a.module, a.lesson, a.starting, b.content_cd  " +
            	"\n    FROM tz_subjlesson a, tz_subj b  " +
            	"\n   WHERE a.subj = b.subj  " +
            	"\n     AND a.subj = #subj#  " +
            	"\n     AND a.module = #module#  " +
            	"\n     AND a.lesson = #lesson#  ";          	
            
            sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
            sql = sql.replaceAll("#module#", StringManager.makeSQL(v_module));
            sql = sql.replaceAll("#lesson#", StringManager.makeSQL(v_lesson));
            
            logger.debug( "���� ������ġ \n " + sql );
            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) {
            	starting = getContentPath(ls.getString("content_cd")) + ls.getString("starting");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return starting;
	}

	/**
	 * ��ȣȭ�� KT ������ ��θ� �����´�
	 * 
	 * @param s_subj
	 * @return
	 * @throws Exception
	 */
	private String getContentPath(String contentCode) throws Exception {
		ConfigSet conf = new ConfigSet();
    	String contentPath = conf.getProperty("dir.kt_content.path") + KTUtil.getInstance().md5Encode(contentCode);
    	return contentPath;
	}

	/**
	 * �ſ� ������� ���� ������ �����´�. (tz_subj ���̺��� subj_gu: 'M' �� ����)
	 * 
	 * eduauth == P : �н� ���� (����üũ ����)
	 *            Y : �н� ���� (����üũ ��)
	 *            N : �� �� ����
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Map getEduAuthMapForSubjGubunM(RequestBox box) throws Exception {
		DBConnectionManager	connMgr	= null;
        ListSet ls = null;
        
        Map map = null;

        String sql = "";
        
        try {
            connMgr = new DBConnectionManager();
            map = new HashMap();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            
            sql =
            	"\n  SELECT a.subj, a.YEAR, a.mm, a.start_date, a.end_date, b.module, b.lesson,  " +
            	"\n         CASE  " +
            	"\n            WHEN (TO_CHAR (SYSDATE, 'YYYY-MM-DD') BETWEEN a.start_date AND a.end_date)  " +
            	"\n               THEN 'Y'  " +
            	"\n            WHEN (TO_CHAR (SYSDATE, 'YYYY-MM-DD') > a.end_date)  " +
            	"\n               THEN 'P'  " +
            	"\n            ELSE 'N'  " +
            	"\n         END AS eduauth,  " +
            	"\n         TO_NUMBER (a.mm) month, COUNT(a.mm) OVER(PARTITION BY a.mm) month_rowspan  " +
            	"\n    FROM tz_subj_mm a, tz_subj_mm_lesson b  " +
            	"\n   WHERE a.subj = #subj#  " +
            	"\n     AND a.YEAR = #year#  " +
            	"\n     AND a.subj = b.subj  " +
            	"\n     AND a.YEAR = b.YEAR  " +
            	"\n     AND a.mm = b.mm  ";
            
            sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
            sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
            
            logger.debug( "�ſ������������ ���� \n " + sql );
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {
            	String key = ls.getString("module") + ls.getString("lesson");
            	// �н�����, �н���, �н��� ���̺� rowspan
            	String[] eduAuthAndMonth = { ls.getString("eduauth"), ls.getString("month"), ls.getString("month_rowspan") };
            	
            	map.put( key, eduAuthAndMonth );
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return map;
	}

	/**
	 * ���� ������ �����´�. (�ſ������������)
	 * 
	 * @param box
	 * @return
	 */
	public List selectLessonListForSubjGubunM(RequestBox box) throws Exception {
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
            	"\n  SELECT a.subj, a.module, a.lesson, a.sdesc, a.starting,   " +
            	"\n         b.total_time, b.first_edu, b.first_end, b.lesson_count, b.ldate  " +
            	"\n    FROM tz_subjlesson a, tz_progress b, tz_subj_mm_lesson c  " +
            	"\n   WHERE a.subj = c.subj  " +
            	"\n     AND c.year = #year#  " + 
            	"\n     AND a.module = c.module  " + 
            	"\n     AND a.lesson = c.lesson  " + 
            	"\n     AND a.subj = #subj#  " + 
            	"\n     AND b.subj(+) = a.subj  " +
            	"\n     AND b.YEAR(+) = #year#  " +
            	"\n     AND b.subjseq(+) = #subjseq#  " +
            	"\n     AND b.lesson(+) = a.lesson  " +
            	"\n     AND b.userid(+) = #userid#  " +
            	"\n   ORDER BY mm  ";
            
            sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
            sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
            sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
            sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid));
            
            logger.debug( "���� ���� \n " + sql );
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {
            	list.add( ls.getDataBox() );
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
     * �������� ���̺���� ��ȯ�Ѵ�.
     * ��Ÿ�׽�Ʈ ������ ���, BETA_PROGRESS_TABLE_NAME�� ��ȯ�Ѵ�.
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
}
