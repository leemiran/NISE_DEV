/*
 * @(#)EduLogBean.java	1.0 2008. 12. 13
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.system.SubjCountBean;

/**
 * �н�â Log Bean
 *
 * @version		1.0, 2008/12/13
 * @author		Chung Jin-pil
 */
public class EduLogBean {

	private Logger logger = Logger.getLogger(this.getClass());
	
	// ���� �н� �����ð� (�� ����)
	public static final int STUDY_TIME_MAX = 180;
	
	/**
     * �н�â ���� �α׸� ����� : ������ ������� �α�, �н�â ���� �α�, �н��� �н�Ƚ�� ����
     * 
     * @param box
     * @param remoteAddr
     * @throws Exception
     */
	public boolean eduStartLog(RequestBox reqBox, String remoteAddr) throws Exception {
		
		boolean countLog = false;
		boolean eduStartLog = false;
		boolean updateStudentStudyCount = false;
		
		reqBox.put("p_userip", remoteAddr);

		// ������ ������� �α�
		String v_subj = reqBox.getString("p_subj");
		String v_year = reqBox.getString("p_year");
		String v_subjseq = reqBox.getString("p_subjseq");

		SubjCountBean CountBean = new SubjCountBean();
		countLog = CountBean.writeLog( reqBox, v_subj, v_year, v_subjseq );

		// �н�â ���� �α�
		eduStartLog = writeEduStartLog(reqBox);
		
		// �н��� �н�Ƚ�� ����
		updateStudentStudyCount = updateStudentStudyCount(reqBox);

		return countLog && eduStartLog && updateStudentStudyCount;
	}

	/**
	 * �н�â ���� �α׸� �����. : �н�â ���� �α�, �н��� �н��ð� ����
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean eduExitLog(RequestBox reqBox) throws Exception {
		boolean eduExitLog = false;
		boolean updateStudentStudyTime = false;
		
		eduExitLog = writeEduExitLog(reqBox);
		updateStudentStudyTime = updateStudentStudyTime(reqBox);
		
		return eduExitLog && updateStudentStudyTime;
	}

	/**
	 * �н��� �� �н�Ƚ�� update
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean updateStudentStudyCount(RequestBox reqBox) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		int count = 0;

		try {
			connMgr = new DBConnectionManager();
			
			String v_subj = reqBox.getString("p_subj");
			String v_year = reqBox.getString("p_year");
			String v_subjseq = reqBox.getString("p_subjseq");
			String s_userid = reqBox.getSession("userid");

			sql =
				"\n  UPDATE tz_student a  " +
				"\n     SET study_count = (SELECT COUNT (seq) study_count  " +
				"\n                          FROM tz_subjloginid  " +
				"\n                         WHERE subj = a.subj  " +
				"\n                           AND YEAR = a.YEAR  " +
				"\n                           AND subjseq = a.subjseq  " +
				"\n                           AND userid = a.userid)  " +
				"\n   WHERE subj = ?  " +
				"\n     AND YEAR = ?  " +
				"\n     AND subjseq = ?  " +
				"\n     AND userid = ?  ";
			
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "�н��� �н�Ƚ�� ����  �α�  \n " + sql );

			pstmt.setString(1, v_subj);
			pstmt.setString(2, v_year);
			pstmt.setString(3, v_subjseq);
			pstmt.setString(4, s_userid);
				
			count = pstmt.executeUpdate();
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return count > 0;		
	}
	
	/**
	 * �н��� �� �н��ð� update
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean updateStudentStudyTime(RequestBox reqBox) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		int count = 0;

		try {
			connMgr = new DBConnectionManager();
			
			String v_subj = reqBox.getString("p_subj");
			String v_year = reqBox.getString("p_year");
			String v_subjseq = reqBox.getString("p_subjseq");
			String s_userid = reqBox.getSession("userid");

			sql =
				"\n  UPDATE tz_student a  " +
				"\n     SET study_time = (SELECT NVL (SUM (study_time), 0) study_time  " +
				"\n                         FROM tz_subjloginid  " +
				"\n                        WHERE subj = a.subj  " +
				"\n                          AND YEAR = a.YEAR  " +
				"\n                          AND subjseq = a.subjseq  " +
				"\n                          AND userid = a.userid)  " +
				"\n   WHERE subj = ?  " +
				"\n     AND YEAR = ?  " +
				"\n     AND subjseq = ?  " +
				"\n     AND userid = ?  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "�н��� �н��ð� ����  �α�  \n " + sql );

			pstmt.setString(1, v_subj);
			pstmt.setString(2, v_year);
			pstmt.setString(3, v_subjseq);
			pstmt.setString(4, s_userid);
				
			count = pstmt.executeUpdate();
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return count > 0;		
	}
	
	/**
	 * �н�â ���� �α�
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean writeEduStartLog(RequestBox reqBox) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		int count = 0;

		try {
			connMgr = new DBConnectionManager();
			
			String v_subj = reqBox.getString("p_subj");
			String v_year = reqBox.getString("p_year");
			String v_subjseq = reqBox.getString("p_subjseq");
			String s_userid = reqBox.getSession("userid");
			String v_userip = reqBox.getString("p_userip");

			sql =
				"\n  INSERT INTO TZ_SUBJLOGINID (  " +
				"\n     SUBJ, YEAR, SUBJSEQ,   " +
				"\n     USERID, SEQ,  " +
				"\n     START_TIME, END_TIME, STUDY_TIME,  " +
				"\n     LGIP, LDATE)   " +
				"\n  VALUES ( ?, ?, ?,  " +
				"\n     ?, (SELECT NVL(MAX(SEQ),0)+1 FROM TZ_SUBJLOGINID WHERE SUBJ=? AND YEAR=? AND SUBJSEQ=? AND USERID=?),  " +
				"\n     sysdate, NULL, NULL,  " +
				"\n  	?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') )  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "�н�â ���� �α�  \n " + sql );

			pstmt.setString(1, v_subj);
			pstmt.setString(2, v_year);
			pstmt.setString(3, v_subjseq);
			pstmt.setString(4, s_userid);
			pstmt.setString(5, v_subj);
			pstmt.setString(6, v_year);
			pstmt.setString(7, v_subjseq);
			pstmt.setString(8, s_userid);
			pstmt.setString(9, v_userip );
				
			count = pstmt.executeUpdate();
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return count > 0;
	}
	
	/**
	 * �н�â ���� �α�
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean writeEduExitLog(RequestBox reqBox) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		int count = 0;

		try {
			connMgr = new DBConnectionManager();
			
			String v_subj = reqBox.getString("p_subj");
			String v_year = reqBox.getString("p_year");
			String v_subjseq = reqBox.getString("p_subjseq");
			String s_userid = reqBox.getSession("userid");

			// �н��ð�(study_time)�� ���� STUDY_TIME_MAX �ð������� �����Ѵ�.
			sql =
				"\n  UPDATE tz_subjloginid  a  " +
				"\n     SET end_time = SYSDATE,  " +
				"\n         study_time = TRUNC( DECODE(SIGN(((SYSDATE-start_time)*24*60)-#studyTimeMax#), 1, #studyTimeMax#, (SYSDATE-start_time)*24*60) ),  " +
				"\n         ldate = TO_CHAR( SYSDATE, 'YYYYMMDDHH24MISS' )  " +
				"\n   WHERE subj = ?  " +
				"\n     AND YEAR = ?  " +
				"\n     AND subjseq = ?  " +
				"\n     AND userid = ?  " +
				"\n     AND seq = (  " +
				"\n                SELECT MAX (seq) FROM tz_subjloginid  " +
				"\n                WHERE subj = a.subj and YEAR = a.YEAR and subjseq = a.subjseq and userid = a.userid  " +
				"\n               )  ";

			sql = sql.replaceAll( "#studyTimeMax#", String.valueOf(STUDY_TIME_MAX) );
			
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "�н�â ���� �α�  \n " + sql );

			pstmt.setString(1, v_subj);
			pstmt.setString(2, v_year);
			pstmt.setString(3, v_subjseq);
			pstmt.setString(4, s_userid);
				
			count = pstmt.executeUpdate();
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return count > 0;
	}	
	
	/**
	 * �н�â ���� �̾ ����� ���� �н����̴� URL ������ �����.
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean eduContSave(RequestBox reqBox) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

        ListSet             ls      = null;
        String              sql     = "";
        String sql1 	= "";
        String sql2 	= "";
        String sql3 	= "";
        int cnt  		= 0;
        int is_Ok 		= 0;
		
        String v_subj = reqBox.getString("p_subj");
		String v_year = reqBox.getString("p_year");
		String v_subjseq = reqBox.getString("p_subjseq");
		String v_cont_url = reqBox.getString("p_cont_url");
		String v_cont_page = reqBox.getStringDefault("p_cont_page", "0");
		String v_pages = reqBox.getStringDefault("p_pages", "0");
		String v_cont_yn = reqBox.getString("p_cont_yn");
		String s_userid = reqBox.getSession("userid");
		String v_userip = reqBox.getString("p_userip");
		
		try {
			connMgr = new DBConnectionManager();
			
			if(v_cont_yn.equals("Y")){
			
				StringBuffer strSQL = new StringBuffer();
				strSQL.append("select count(userid) cnt \n") ;
				strSQL.append("  from tz_subjconturl \n") ;
				strSQL.append(" where subj = "+StringManager.makeSQL(v_subj)+"   \n") ;
				strSQL.append("   and year = "+StringManager.makeSQL(v_year)+"   \n") ;
				strSQL.append("   and subjseq = "+StringManager.makeSQL(v_subjseq)+"   \n") ;
				strSQL.append("   and userid = "+StringManager.makeSQL(s_userid)+"   \n") ;
	
				ls = connMgr.executeQuery(strSQL.toString());
				
				System.out.println("tz_subjconturl ===> "+strSQL.toString());
	
	            if ( ls.next() ) { 
	                cnt = ls.getInt("cnt");
	            }
	            ls.close();
	            if(v_cont_url.split("p_process").length > 1){
	            	
	            }else{
	            	
	            	if ( cnt > 0 ) {                         // update
	            		sql2  = " update tz_subjconturl set cont_url = ? , ldate = to_char(sysdate,'yyyymmddhh24mmss'), seq = ? , lgip = ? ";
	            		sql2 += " where subj = ?  and year = ? and subjseq = ? and userid = ? ";
	            		pstmt = connMgr.prepareStatement(sql2);
	            		
	            		pstmt.setString(1,  v_cont_url);
	            		pstmt.setInt   (2, Integer.parseInt(v_cont_page));
	            		pstmt.setString(3,  v_pages);
	            		pstmt.setString(4,  v_subj);
	            		pstmt.setString(5,  v_year);
	            		pstmt.setString(6,  v_subjseq);
	            		pstmt.setString(7,  s_userid);
	            		
	            	}else {                                // insert
	            		sql3  = " insert into tz_subjconturl(subj, year, subjseq, userid, lgip, ldate, cont_url , seq) ";
	            		sql3 += " values (?, ?, ?, ?, ?, to_char(sysdate,'yyyymmddhh24mmss'), ? , ?    )                                       ";
	            		pstmt = connMgr.prepareStatement(sql3);
	            		
	            		pstmt.setString(1,  v_subj);
	            		pstmt.setString(2,  v_year);
	            		pstmt.setString(3,  v_subjseq);
	            		pstmt.setString(4,  s_userid);
	            		pstmt.setString(5,  v_pages);
	            		pstmt.setString(6,  v_cont_url);
	            		pstmt.setInt   (7, Integer.parseInt(v_cont_page));
	            	}
	            	is_Ok = pstmt.executeUpdate();
	            	if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
	            }
			}
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return is_Ok > 0;
	}
}
