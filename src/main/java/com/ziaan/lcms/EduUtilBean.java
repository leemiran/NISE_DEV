/*
 * @(#)EduUtilBean.java	1.0 2009. 03. 19
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import org.apache.log4j.Logger;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * �н����� Util DAO Bean
 *
 * @version		1.0, 2008/11/20
 * @author		Chung Jin-pil
 */
public class EduUtilBean {
	
	private Logger logger = Logger.getLogger(this.getClass());

	private static EduUtilBean instance = null;
	
	private EduUtilBean() {
	}

	public static EduUtilBean getInstance() {
		if ( instance == null ) {
			instance = new EduUtilBean();
		}

		return instance;
	}

    /**
     * �н��ð����� ������θ� ��ȯ�Ѵ�.
     * 
     * @param reqBox
     * @return ���Ѿ��� ��� : true ��ȯ, �������� ��� : �����޽��� ��ȯ
     * @throws Exception
     */
	public String isPassedLimitStudyTime( RequestBox reqBox ) throws Exception {
		
		DBConnectionManager connMgr = null;

		String sql = "";
		String result = "true";

		try {
			connMgr = new DBConnectionManager();
			
			// ������ or ���IP or �������� ���� ���� (Guard Condition)
			//if ( isAdminAuth(reqBox) || isOutSideNetwork(reqBox) || isHollyday(connMgr, reqBox) )
			if ( isAdminAuth(reqBox) || isHollyday(connMgr, reqBox) )
				return "true";
			
			result = getLimitStudyTime(connMgr);
			
		} catch (Exception ex) {
			logger.error( sql + "\n" + reqBox, ex );
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return result;		
	}

	/**
	 * ������ ���� ���θ� ��ȯ�Ѵ�.
	 * 
	 * @param reqBox
	 * @return
	 */
	private boolean isAdminAuth(RequestBox reqBox) {
		boolean isAdminAuth = false;
		
		String gadmin = reqBox.getSession("gadmin");
		
		if ( gadmin.startsWith("A") || gadmin.startsWith("F") || gadmin.startsWith("P") || gadmin.startsWith("M") )
			isAdminAuth = true;
		else
			isAdminAuth = false;

		logger.debug("@ ������ ���ѿ��� : " + isAdminAuth );
		return isAdminAuth;
	}

	/**
	 * �н��ð����� ���θ� ��ȯ�Ѵ�.
	 * 
	 * @param connMgr
	 * @return
	 * @throws Exception
	 */
	private String getLimitStudyTime(DBConnectionManager connMgr) throws Exception {
		ListSet ls = null;
		
		String sql = "";
		String result = "";
	
		try	{
			sql =
				"\n  SELECT sthh, enhh, ww  " +
				"\n    FROM tz_timelimit  " +
				"\n   WHERE ww like '%' || to_char(sysdate,'D') || '%'  " +
				"\n     AND to_char(sysdate,'hh24') between sthh and enhh-1  " +
				"\n     AND isuse = 'Y'  ";

			ls = connMgr.executeQuery(sql);
					
			if ( ls.next() ) {
				StringBuffer sb = new StringBuffer();
				
				sb.append("���� �������� �ð��Դϴ�.");
				sb.append( ls.getString("sthh") );
				sb.append( "�ú��� " );
				sb.append( ls.getString("enhh") );
				sb.append( "�ñ��� �н������� ���ѵ˴ϴ�. \n" );
				sb.append( ls.getString("enhh") );
				sb.append( "�� ���Ŀ� �����Ͻñ� �ٶ��ϴ�." );
				
				result = sb.toString();
			} else {
				result = "true";
			}
		} catch (Exception ex) {
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
		}

		return result;			
	}

	/**
	 * ������ ���θ� ��ȯ�Ѵ�.
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean isHollyday(DBConnectionManager connMgr, RequestBox reqBox) throws Exception {
		ListSet ls = null;
		
		String sql = "";
		boolean result = false;
	
		try	{
			sql =
				"\n  SELECT COUNT(seq) cnt  " +
				"\n    FROM tz_breakinfo  " +
				"\n   WHERE breakdt = TO_CHAR (SYSDATE, 'mmdd')   " +
				"\n     AND isuse = 'Y'  ";

			ls = connMgr.executeQuery(sql);
					
			if ( ls.next() ) {
				result = ls.getInt("cnt") > 0;
			}
		} catch (Exception ex) {
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
		}

		return result;			
	}

	/**
	 * ���IP ���θ� ��ȯ�Ѵ�.
	 * 
	 * @param reqBox
	 * @return true : ���IP, false : �系IP
	 * @throws Exception
	 */
	private boolean isOutSideNetwork(RequestBox reqBox) throws Exception {
		
		boolean isOutSideNetwork = false;
		
		ConfigSet conf = new ConfigSet();
		
		String v_ktip 	= conf.getProperty("ktedu.inIP"); //KT��� IP����
		String v_userip = reqBox.getSession("userip");
		
		if( v_userip.matches(v_ktip) ) {
			isOutSideNetwork = true;
		}
		
		return isOutSideNetwork;
	}
}
