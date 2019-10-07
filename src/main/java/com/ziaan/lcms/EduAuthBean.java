/*
 * @(#)EduSession.java	1.0 2008. 12.11
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */
package com.ziaan.lcms;

import org.apache.log4j.Logger;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * �н�â ���� ���� Bean Ŭ����
 * 
 * @version 1.0, 2008/12/11
 * @author Chung Jin-pil
 */
public class EduAuthBean {

	private Logger logger = Logger.getLogger(this.getClass());

	private static EduAuthBean instance = null;
	
	// �н� ���� ����
	public static final String EDU_AUTH_AUTHORIZED = "Y";

	// �н� ���� ����
	public static final String EDU_AUTH_UNAUTHORIZED = "N";

	// ��Ÿ �׽�Ʈ
	public static final String EDU_AUTH_BETATEST = "B";

	// ��ȸ ���� (�̸�����, ������, ����, �������� �� ���)
	public static final String EDU_AUTH_PREVIEW = "P";

	private EduAuthBean() {
	}

	public static EduAuthBean getInstance() {
		if ( instance == null ) {
			instance = new EduAuthBean();
		}

		return instance;
	}

	/**
	 * ������������ ���ѿ��θ� ��ȯ�Ѵ�.
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public String getEduAuthOpenedu(RequestBox box) throws Exception {

		DBConnectionManager connMgr = new DBConnectionManager();
		
    	ListSet ls = null;
        String sql = "";
        
        int count = 0;
        
        try {
            String v_subj = box.getString("p_subj");

            sql =
            	"\n  SELECT COUNT (subj) cnt  " +
            	"\n    FROM tz_subj  " +
            	"\n   WHERE subj = #subj#  " +
            	"\n     AND isopenedu = 'Y'  ";
            
            sql = sql.replaceAll( "#subj#", StringManager.makeSQL(v_subj) );

            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() ) {
            	count = ls.getInt("cnt");
            }
            
            logger.debug( "@ �������� ���� : " +  (count == 1) );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return (count == 1) ? EDU_AUTH_PREVIEW : EDU_AUTH_UNAUTHORIZED;
	}

	/**
	 * ���������� ���ѿ��θ� ��ȯ�Ѵ�.
	 * 
	 * @param box
	 * @return
	 */
	public String getEduAuthCustomedu(RequestBox box) throws Exception {

		DBConnectionManager connMgr = new DBConnectionManager();
		
    	ListSet ls = null;
        String sql = "";
        
        int count = 0;
        
        try {
            String v_subj = box.getString("p_subj");

            sql =
            	"\n  SELECT COUNT (subj) cnt  " +
            	"\n    FROM tz_subj  " +
            	"\n   WHERE subj = #subj#  " +
            	"\n     AND iscustomedu = 'Y'  ";
            
            sql = sql.replaceAll( "#subj#", StringManager.makeSQL(v_subj) );

            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() ) {
            	count = ls.getInt("cnt");
            }
            
            logger.debug( "@ �������� ���� : " +  (count == 1) );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return (count == 1) ? EDU_AUTH_PREVIEW : EDU_AUTH_UNAUTHORIZED;
	}
	
	
	/**
	 * �����ϱ� ���ѿ��θ� ��ȯ�Ѵ�.
	 * 
	 * :������ ������ �������, �����Ⱓ ����, �����Ⱓ(��) �̳��� ���
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public String getEduAuthReview(RequestBox box) throws Exception {
		DBConnectionManager connMgr = new DBConnectionManager();
		
    	ListSet ls = null;
        String sql = "";
        
        int count = 0;
        
        try {
            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String s_userid = box.getSession("userid");

            sql =
            	"\n  SELECT COUNT (*) cnt  " +
            	"\n    FROM tz_subjseq a, tz_student b  " +
            	"\n   WHERE a.subj = #subj#  " +
            	"\n     AND a.YEAR = #year#  " +
            	"\n     AND a.subjseq = #subjseq#  " +
            	"\n     AND a.isablereview = 'Y'  " +
            	"\n     AND a.subj = b.subj  " +
            	"\n     AND a.YEAR = b.YEAR  " +
            	"\n     AND a.subjseq = b.subjseq  " +
            	"\n     AND b.userid = #userid#  " +
            	"\n     AND SYSDATE BETWEEN TO_DATE(a.eduend, 'YYYYMMDDHH24') AND ADD_MONTHS (TO_DATE(a.eduend, 'YYYYMMDDHH24'), a.reviewdays)  ";

            sql = sql.replaceAll( "#subj#", StringManager.makeSQL(v_subj) );
            sql = sql.replaceAll( "#year#", StringManager.makeSQL(v_year) );
            sql = sql.replaceAll( "#subjseq#", StringManager.makeSQL(v_subjseq) );
            sql = sql.replaceAll( "#userid#", StringManager.makeSQL(s_userid) );

            ls = connMgr.executeQuery( sql );

            if ( ls.next() ) {
            	count = ls.getInt("cnt");
            }

            logger.debug( "@ �����ϱ� ���� ���� : " + (count == 1) );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return count == 1 ? EDU_AUTH_PREVIEW : EDU_AUTH_UNAUTHORIZED;
	}

	/**
	 * �н��� ������ ��ȯ�Ѵ�.
	 * 
	 * : ���οϷ� �ǰ�, �н����� �����Ⱓ�Ͽ� ���� ���
	 * 
	 * @param box
	 * @return
	 */
	public String getEduAuthStudent(RequestBox box) throws Exception {
		DBConnectionManager connMgr = new DBConnectionManager();
		
    	ListSet ls = null;
        String sql = "";
        
        int count = 0;
        
        try {
            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String s_userid = box.getSession("userid");

            sql =
            	"\n  SELECT COUNT (*) cnt  " +
            	"\n    FROM tz_subjseq a, tz_student b  " +
            	"\n   WHERE a.subj = #subj#  " +
            	"\n     AND a.YEAR = #year#  " +
            	"\n     AND a.subjseq = #subjseq#  " +
            	"\n     AND a.subj = b.subj  " +
            	"\n     AND a.YEAR = b.YEAR  " +
            	"\n     AND a.subjseq = b.subjseq  " +
            	"\n     AND b.userid = #userid#  " +
            	"\n     and to_char(sysdate,'YYYYMMDDHH24') between a.edustart and a.eduend  ";

            sql = sql.replaceAll( "#subj#", StringManager.makeSQL(v_subj) );
            sql = sql.replaceAll( "#year#", StringManager.makeSQL(v_year) );
            sql = sql.replaceAll( "#subjseq#", StringManager.makeSQL(v_subjseq) );
            sql = sql.replaceAll( "#userid#", StringManager.makeSQL(s_userid) );

            ls = connMgr.executeQuery( sql );

            if ( ls.next() ) {
            	count = ls.getInt("cnt");
            }

            logger.debug( "@ �н��ϱ� ���� ���� : " + (count == 1) );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return count == 1 ? EDU_AUTH_AUTHORIZED : EDU_AUTH_UNAUTHORIZED;
	}

	/**
	 * ������ ���θ� ��ȯ�Ѵ�.
	 * 
	 * @param gadmin
	 * @return
	 */
	public boolean isAdminAuth(String gadmin) {
		String s = "";
		
		if ( gadmin != null && !gadmin.equals("") ) {
			s = gadmin.substring(0,1);
		}

        if ( s.equals("A") || s.equals("F") || s.equals("P") || s.equals("T") || s.equals("M") || s.equals("S") ) {
            return true;
        }
        else {
        	return false;
        }
	}

	/**
	 * ��Ÿ�׽�Ʈ ���ѿ��θ� ��ȯ�Ѵ�.
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public String getEduAuthBetatest(RequestBox box) throws Exception {
		
		if ( box.getSession("gadmin").startsWith("A") ) 
			return EDU_AUTH_BETATEST;

		if ( !box.getSession("gadmin").startsWith("M") ) 
			return EDU_AUTH_UNAUTHORIZED;

		DBConnectionManager connMgr = new DBConnectionManager();
		
    	ListSet ls = null;
        String sql = "";
        
        int count = 0;
        
        try {
            String v_subj = box.getString("p_subj");
            String s_userid = box.getSession("userid");

            sql =
            	"\n  SELECT count(*) cnt  " +
            	"\n    FROM tz_subj a, tz_cpasinfo b  " +
            	"\n   WHERE a.subj = #subj#  " +
            	"\n  	AND b.userid = #userid#  " +
            	"\n     AND a.producer = b.cpseq  ";
            
            sql = sql.replaceAll( "#subj#", StringManager.makeSQL(v_subj) );
            sql = sql.replaceAll( "#userid#", StringManager.makeSQL(s_userid) );

            ls = connMgr.executeQuery( sql );

            if ( ls.next() ) {
            	count = ls.getInt("cnt");
            }

            logger.debug( "@ ��Ÿ�׽�Ʈ ���� ���� : " + (count == 1) );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return count == 1 ? EDU_AUTH_BETATEST : EDU_AUTH_UNAUTHORIZED;
	}
}
