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
 * 학습관련 Util DAO Bean
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
     * 학습시간제한 통과여부를 반환한다.
     * 
     * @param reqBox
     * @return 제한없을 경우 : true 반환, 제한있을 경우 : 에러메시지 반환
     * @throws Exception
     */
	public String isPassedLimitStudyTime( RequestBox reqBox ) throws Exception {
		
		DBConnectionManager connMgr = null;

		String sql = "";
		String result = "true";

		try {
			connMgr = new DBConnectionManager();
			
			// 관리자 or 사외IP or 공휴일은 제한 없음 (Guard Condition)
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
	 * 관리자 권한 여부를 반환한다.
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

		logger.debug("@ 관리자 권한여부 : " + isAdminAuth );
		return isAdminAuth;
	}

	/**
	 * 학습시간제한 여부를 반환한다.
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
				
				sb.append("현재 업무집중 시간입니다.");
				sb.append( ls.getString("sthh") );
				sb.append( "시부터 " );
				sb.append( ls.getString("enhh") );
				sb.append( "시까지 학습접속이 제한됩니다. \n" );
				sb.append( ls.getString("enhh") );
				sb.append( "시 이후에 접속하시길 바랍니다." );
				
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
	 * 공휴일 여부를 반환한다.
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
	 * 사외IP 여부를 반환한다.
	 * 
	 * @param reqBox
	 * @return true : 사외IP, false : 사내IP
	 * @throws Exception
	 */
	private boolean isOutSideNetwork(RequestBox reqBox) throws Exception {
		
		boolean isOutSideNetwork = false;
		
		ConfigSet conf = new ConfigSet();
		
		String v_ktip 	= conf.getProperty("ktedu.inIP"); //KT사외 IP정보
		String v_userip = reqBox.getSession("userip");
		
		if( v_userip.matches(v_ktip) ) {
			isOutSideNetwork = true;
		}
		
		return isOutSideNetwork;
	}
}
