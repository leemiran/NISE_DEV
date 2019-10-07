/*
 * @(#)EduProgressBean.java	1.0 2008. 11. 20
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.EduEtc1Bean;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * 진도체크 DAO Bean
 *
 * @version		2.0, 2009/03/23
 * @author		Chung Jin-pil
 */
public class EduProgressBean {
	
	private Logger logger = Logger.getLogger(this.getClass());

	public static final String OID_NORMAL_DEFAULT_VALUE = "1";
	
	public static final String PROGRESS_TABLE_NAME = "tz_progress";
	public static final String BETA_PROGRESS_TABLE_NAME = "tz_beta_progress";
	
	private String eduCheckMessage = "";
	
	public EduProgressBean() {
	}

    /**
     * 진도체크를 실행한다
     * 
     * @param reqBox
     * @return
     * @throws Exception
     */
	public boolean eduCheck( RequestBox reqBox ) throws Exception {
		
		eduCheckStartLog(reqBox, EduProgressBean.PROGRESS_TABLE_NAME);
		
		DBConnectionManager connMgr = null;

		
		String sql = "";
		boolean result = false;

		try {
			connMgr = new DBConnectionManager();
			
	        boolean isAlreadyCompletedEduCheck = false;
	        boolean isEduCheck = false;
	        boolean isEndEduCheck = reqBox.getStringDefault("p_gubun","END").equals("END")? true : false;

        	isAlreadyCompletedEduCheck = isAlreadyCompletedEduCheck(connMgr, reqBox);
        		
        	logger.error("EduProgressBean.eduCheck isEndEduCheck : " + isEndEduCheck + " / isAlreadyCompletedEduCheck : " + isAlreadyCompletedEduCheck );
        	
        	if ( isAlreadyCompletedEduCheck ) {
        		
        		//권장학습시간
        		//boolean isPassedLimitForEduTime = isPassedLimitForEduTime(connMgr, reqBox);
        		//진도율제한
        		boolean isPassedLimitForRateOfProgress = isPassedLimitForRateOfProgress(connMgr, reqBox);
        		
//        		if ( isPassedLimitForEduTime && isPassedLimitForRateOfProgress ) {
        		if ( isPassedLimitForRateOfProgress ) {
        			isEduCheck = doEduCheck(connMgr, reqBox, EduProgressBean.PROGRESS_TABLE_NAME);
        		}
	        }
	        
	        // 진도체크 나가지 않았음 && 진도끝 체크일 때 && 그런데 진도체크가 되지 않았을 경우!
//	        if ( !isAlreadyCompletedEduCheck && isEndEduCheck && !isEduCheck ) {
        	if ( isEndEduCheck && !isEduCheck ) {
	        	result = false;
	        } else {
	        	result = true;
	        }

	        eduCheckEndLog(result, isEduCheck);
		}
		catch (Exception ex) {
			logger.error( sql + "\n" + reqBox, ex );
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return result;		
	}
	
	/**
	 * (기존 완료 안된 진도 및 첫 진도) 진도를 생성한다.
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @param tableName	tz_progress: 진도테이블, tz_beta_progress: 베타테스트 진도테이블
	 * @return
	 * @throws Exception
	 */
	private boolean doEduCheck(DBConnectionManager connMgr, RequestBox reqBox, String tableName) throws Exception {
		ListSet ls = null;
		PreparedStatement pstmt = null;
		
		String sql = "";
		boolean result = false;
	
		try {
			int count = 0;
			
			String s_subj = reqBox.getSession("s_subj");
			String s_year = reqBox.getSession("s_year");
			String s_subjseq = reqBox.getSession("s_subjseq");
			String s_userid = reqBox.getSession("userid");
			
			String p_gubun = reqBox.getStringDefault("p_gubun", "END");
			String p_lesson = reqBox.getString("p_lesson");
			String p_contenttype = reqBox.getString("p_contenttype");
			String p_oid = reqBox.getStringDefault("p_oid", EduProgressBean.OID_NORMAL_DEFAULT_VALUE );
			
			String v_currentPage = reqBox.getString("p_currentPage");  //현재페이지
			
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select cnt, (select substr(starting, instr(starting, '/', -1) + 1) ") ;
			strSQL.append("              from tz_subjlesson_page ") ;
			strSQL.append("             where subj = "+StringManager.makeSQL(s_subj)+" ") ;
			strSQL.append("               and lesson = "+StringManager.makeSQL(p_lesson)+" ") ;
			strSQL.append("               and pagenum = (select max(pagenum) from tz_subjlesson_page where subj = "+StringManager.makeSQL(s_subj)+" and lesson = "+StringManager.makeSQL(p_lesson)+") ") ;
			strSQL.append("            ) lessonlastpage ") ;
			strSQL.append(" from ( ") ;
			strSQL.append("        select count(*) cnt ") ;
			strSQL.append("          from tz_subjlesson_page ") ;
			strSQL.append("         where subj = "+StringManager.makeSQL(s_subj)+" ") ;
			strSQL.append("      ) ") ;

			logger.debug( "현재("+v_currentPage+")의 마지막 페이지 가져오기  strSQL \n" + strSQL +"\n :"+p_gubun+":");
			
			int cnt = 0;
			boolean islessonPage = false;
			String v_lessonlastpage = "";
			ls = connMgr.executeQuery(strSQL.toString());
			
			if ( ls.next() ) {
				cnt = ls.getInt("cnt");
				v_lessonlastpage = ls.getString("lessonlastpage");
				//if((cnt > 0 && (v_lessonlastpage.equals(v_currentPage))) || cnt == 1) {
				//if((cnt > 0 && (v_lessonlastpage.equals(v_currentPage)))) {
				if(cnt > 0) {
					islessonPage = true;
				}else{
					if(cnt > 0){
						p_gubun = "";
					}else{
						islessonPage = true;
					}
				}
			}
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			
			sql =
				"\n  SELECT ldate, first_edu, first_end, TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') nowtime  " +
				"\n    FROM  " + tableName +
				"\n   WHERE subj = #subj#  " +
				"\n     AND YEAR = #year#  " +
				"\n     AND subjseq = #subjseq#  " +
				"\n     AND lesson = #lesson#  " +
				"\n     AND oid = #oid#  " +
				"\n     AND userid = #userid#  ";
				//"\n     AND FIRST_END IS NULL  " +
			
			sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
			sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
			sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
			sql = sql.replaceAll("#lesson#", StringManager.makeSQL(p_lesson));
			sql = sql.replaceAll("#oid#", StringManager.makeSQL(p_oid));
			sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid));
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "기존 진도 검사  sql \n" + sql +"\n :"+p_gubun+":");
			
			if ( ls.next() ) {
				// 기존 완료 안된(FIRST_END IS NULL) 진도 있을 경우, update
				if ( p_gubun.equals("END") && islessonPage) {
	                String v_ldate = ls.getString("ldate");
	                String v_sysdate = ls.getString("nowtime");
	                String v_session_time = EduEtc1Bean.get_duringtime(v_ldate, v_sysdate);

	                sql =
	                	"\n  UPDATE " + tableName +
	                	"\n     SET session_time = ?,  " +
	                	"\n         total_time = ?,  " +
	                	"\n         ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS'),  " +
	                	"\n         lesson_count = lesson_count + 1,  " +
	                	"\n         first_end = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')  " +
	                	"\n   WHERE subj = ?  " +
	                	"\n     AND YEAR = ?  " +
	                	"\n     AND subjseq = ?  " +
	                	"\n     AND lesson = ?  " +
	                	"\n     AND oid = ?  " +
	                	"\n     AND userid = ?  ";
	                
	                pstmt = connMgr.prepareStatement(sql);
	                pstmt.setString(1, v_session_time);
	                pstmt.setString(2, v_session_time);
	                pstmt.setString(3, s_subj);
	                pstmt.setString(4, s_year);
	                pstmt.setString(5, s_subjseq);
	                pstmt.setString(6, p_lesson);
	                pstmt.setString(7, p_oid);
	                pstmt.setString(8, s_userid);
				} else {
	                sql =
	                	"\n  UPDATE " + tableName +
	                	"\n     SET  " +
	                	"\n         ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')  " +
	                	//"\n         ,lesson_count = lesson_count + 1  " +
	                	"\n   WHERE subj = ?  " +
	                	"\n     AND YEAR = ?  " +
	                	"\n     AND subjseq = ?  " +
	                	"\n     AND lesson = ?  " +
						"\n     AND oid = ?  " +
						"\n     AND userid = ?  ";
	                
	                pstmt = connMgr.prepareStatement(sql);
	                pstmt.setString(1, s_subj);
	                pstmt.setString(2, s_year);
	                pstmt.setString(3, s_subjseq);
	                pstmt.setString(4, p_lesson);
                	pstmt.setString(5, p_oid);
                	pstmt.setString(6, s_userid);
				}
				
	            count = pstmt.executeUpdate();

	            ls.close();
	            pstmt.close();
			}
			// 기존 완료 안된(FIRST_END IS NULL) 진도 없을 경우, insert
			else {
				String v_first_end = "";
				String v_lesson_count = "";

				if ( p_gubun.equals("END") ) {
					v_first_end = "TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')";
					v_lesson_count = "1";
				} else {
					v_first_end = "NULL";
					v_lesson_count = "0";
				}

				sql =
					"\n  INSERT INTO " + tableName +
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
			}
			
			result = count > 0;
			logger.debug( "@ Do Edu Check : " + result );
			logger.debug( "  => EduCheck Gubun / Contenttype / Update       : " + p_gubun + " / " + p_contenttype + " / " + count );
		}
		catch (Exception ex) {
			logger.error( ex );
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result;	
	}

	/**
	 * 진도율 제한 통과 여부를 반환한다.
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 */
	private boolean isPassedLimitForRateOfProgress(DBConnectionManager connMgr, RequestBox reqBox) throws Exception {
		ListSet ls = null;
		
		String sql = "";
		boolean result = false;
	
		try
		{
			String s_subj = reqBox.getSession("s_subj");
			String s_year = reqBox.getSession("s_year");
			String s_subjseq = reqBox.getSession("s_subjseq");
			String s_userid = reqBox.getSession("userid");
			
			String p_gubun = reqBox.getStringDefault("p_gubun", "END");
			String p_lesson = reqBox.getString("p_lesson");
			String p_contenttype = reqBox.getString("p_contenttype");
			String p_oid = reqBox.getStringDefault("p_oid", EduProgressBean.OID_NORMAL_DEFAULT_VALUE);

			String tableName = "";
			if ( p_contenttype.equals("O") ) {
				tableName = "tz_subjobj";
			} else {
				tableName = "tz_subjlesson";
			}
			
			sql =
				"\n  SELECT ROUND ((a.completed_educheck_cnt / b.total_cnt) * 100, 2) AS rate_of_progress,  " +
				"\n         c.edulimit  " +
				"\n    FROM (SELECT COUNT (*) completed_educheck_cnt  " +
				"\n            FROM tz_progress  " +
				"\n           WHERE subj = #subj#  " +
				"\n             AND YEAR = #year#  " +
				"\n             AND subjseq = #subjseq#  " +
				"\n             AND userid = #userid#  " +
				"\n             AND SUBSTR (first_end, 1, 8) = TO_CHAR (SYSDATE, 'YYYYMMDD')  " +
				"\n             AND NOT (lesson = #lesson# AND OID = #oid#)) a,  " +
				"\n         (SELECT COUNT (*) total_cnt  " +
				"\n            FROM #tableName#  " +
				"\n           WHERE subj = #subj# and lesson != '00' and lesson != '99') b,  " +
				"\n         (SELECT edulimit  " +
				"\n            FROM tz_subjseq  " +
				"\n           WHERE subj = #subj# AND YEAR = #year# AND subjseq = #subjseq#) c  ";

			sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
			sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
			sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
			sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid));
			sql = sql.replaceAll("#lesson#", StringManager.makeSQL(p_lesson));
			sql = sql.replaceAll("#oid#", StringManager.makeSQL(p_oid));
			sql = sql.replaceAll("#tableName#", tableName );
				
			ls = connMgr.executeQuery(sql);
					
			if ( ls.next() ) {
				double rateOfProgress = ls.getDouble("rate_of_progress");
				double eduLimit = ls.getDouble("edulimit");
				
				logger.debug( "@ 진도율 : " + rateOfProgress + "%, 진도제한 : " + eduLimit + "%" );
				if ( rateOfProgress >= eduLimit ) {
					result = false;
					
					String message = "금일 학습할 수 있는 제한을 넘었으므로 진도체크되지 않습니다. 학습창이 닫힙니다.";
					this.setEduCheckMessage(message);
				} else {
					result = true;
				}
			}
		}
		catch (Exception ex)
		{
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
		}

		logger.debug("@ is Passed Limit For Rate Of Progress : " + result );
		return result;		
	}

	public String getEduCheckMessage() {
		return eduCheckMessage;
	}
	
	private void setEduCheckMessage(String message) {
		this.eduCheckMessage = message;
	}

	/**
	 * 권장학습시간 제한 통과 여부를 반환한다.
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 */
	private boolean isPassedLimitForEduTime(DBConnectionManager connMgr, RequestBox reqBox) throws Exception {
		ListSet ls = null;
		
		String sql = "";
		
		// 진도제한에 대한 에러 발생시 skip하기 위해 true로 초기화
		boolean result = true;
	
		try
		{
			String s_subj = reqBox.getSession("s_subj");
			String s_year = reqBox.getSession("s_year");
			String s_subjseq = reqBox.getSession("s_subjseq");
			String s_userid = reqBox.getSession("userid");
			
			String p_gubun = reqBox.getStringDefault("p_gubun", "END");
			String p_lesson = reqBox.getString("p_lesson");
			String p_contenttype = reqBox.getString("p_contenttype");
			String p_oid = reqBox.getStringDefault("p_oid", EduProgressBean.OID_NORMAL_DEFAULT_VALUE);

			// 진도종료 체크시만 제한 Check 
			if ( p_gubun.equals("END") ) {
				if ( p_contenttype.equals("O") ) {
					sql = 
						"\n  SELECT NVL (edu_time, 0) edu_time  " +
						"\n    FROM tz_subjobj  " +
						"\n   WHERE subj = #subj#  " +
						//"\n     AND module = #module#  " +
						"\n     AND lesson = #lesson#  " +
						"\n     AND OID = #oid#  " +
						"\n     AND edu_time_yn = 'Y'  ";					
				} else {
					sql = 
						"\n  SELECT NVL (edu_time, 0) edu_time  " +
						"\n    FROM tz_subjlesson  " +
						"\n   WHERE subj = #subj#  " +
						//"\n     AND module = #module#  " +
						"\n     AND lesson = #lesson#  " +
						"\n     AND edu_time_yn = 'Y'  ";				
				}
	
				sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
				sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
				sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
				//sql = sql.replaceAll("#module#", StringManager.makeSQL(p_module));
				sql = sql.replaceAll("#lesson#", StringManager.makeSQL(p_lesson));
				sql = sql.replaceAll("#oid#", StringManager.makeSQL(p_oid));
	
				ls = connMgr.executeQuery(sql);
				logger.debug( "@ 권장학습시간 제한  \n " + sql ); 
						
				if ( ls.next() ) {
					int eduTime = ls.getInt("edu_time");
					int timeOfProgress = 0;
					
					if ( eduTime != 0 ) {
						
						sql = 
							"\n  SELECT ROUND((SYSDATE - TO_DATE(ldate,'YYYYMMDDHH24MISS')) * 24 * 60) time_of_progress   " +
							"\n  FROM tz_progress  " +
							"\n  WHERE subj = #subj#  " +
							"\n     AND year = #year#  " +
							"\n     AND subjseq = #subjseq#  " +
							"\n     AND lesson = #lesson#  " +
							"\n     AND oid = #oid#  " +
							"\n     AND userid = #userid#  ";							
						
						sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
						sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
						sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
						sql = sql.replaceAll("#lesson#", StringManager.makeSQL(p_lesson));
						sql = sql.replaceAll("#oid#", StringManager.makeSQL(p_oid));
						sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid));
						
						ls.close();
						ls = connMgr.executeQuery(sql);
						
						if ( ls.next() ) {
							timeOfProgress = ls.getInt("time_of_progress");

							if ( eduTime > timeOfProgress ) {
								result = false;

								/*
								String message =
									s_userid + "님의 현재 학습시간은 " + eduTime + "분입니다.\\r\\n\\r\\n" +
									"본 차시의 최소 학습시간은 " + timeOfProgress + "분이며,\\r\\n" +
									"최소 학습시간이 경과하여야 차시진도가 인정됩니다.\\r\\n\\r\\n" +
									"뒤로 돌아가 다시 학습하여 주십시오.";
								*/
								String message =
									"[ 내 학습시간 : "+ timeOfProgress +"분, 권장 학습시간 : " + eduTime + "분 ]\\r\\n\\r\\n" +
									"각 차시(페이지)마다 정상적으로 학습하셔야\\r\\n" +
									"학습진도에 반영됩니다.\\r\\n\\r\\n" +
									"차근차근 학습해 주시길 바랍니다!";
								
								this.setEduCheckMessage(message);
								logger.debug("@ 권장 학습시간 : " + eduTime + "분, 진행 학습시간 : " + timeOfProgress + "분" );
							} 
						}
						ls.close();
					}
				}
			}
		}
		catch (Exception ex)
		{
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
		}

		logger.debug("@ is Passed Limit EduTime : " + result );
		return result;		
	}

	/**
	 * 기존 완료된 진도(FIRST_END IS NOT NULL) 여부를 반환한다.
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean isAlreadyCompletedEduCheck(DBConnectionManager connMgr, RequestBox reqBox) throws Exception {
		ListSet ls = null;
		PreparedStatement pstmt = null;
		
		String sql = "";
		boolean result = false;
	
		try {
			int count = 0;
			
			String s_subj = reqBox.getSession("s_subj");
			String s_year = reqBox.getSession("s_year");
			String s_subjseq = reqBox.getSession("s_subjseq");
			String s_userid = reqBox.getSession("userid");
			
			String p_gubun = reqBox.getStringDefault("p_gubun", "END");
			String p_lesson = reqBox.getString("p_lesson");
			String p_contenttype = reqBox.getString("p_contenttype");
			String p_oid = reqBox.getStringDefault("p_oid", EduProgressBean.OID_NORMAL_DEFAULT_VALUE );
			
			sql =
				"\n  SELECT total_time, ldate, TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') nowtime  " +
				"\n    FROM tz_progress  " +
				"\n   WHERE subj = #subj#  " +
				"\n     AND YEAR = #year#  " +
				"\n     AND subjseq = #subjseq#  " +
				"\n     AND userid = #userid#  " +
				"\n     AND lesson = #lesson#  " +
				"\n     AND first_end IS NOT NULL  " +
				"\n     AND oid = #oid#  ";
			
			sql = sql.replaceAll("#subj#", StringManager.makeSQL(s_subj));
			sql = sql.replaceAll("#year#", StringManager.makeSQL(s_year));
			sql = sql.replaceAll("#subjseq#", StringManager.makeSQL(s_subjseq));
			sql = sql.replaceAll("#userid#", StringManager.makeSQL(s_userid));
			sql = sql.replaceAll("#lesson#", StringManager.makeSQL(p_lesson));
			sql = sql.replaceAll("#oid#", StringManager.makeSQL(p_oid));
			logger.debug( "@ sql0 : " + sql );
			ls = connMgr.executeQuery(sql);

			// 기존 완료된 (FIRST_END IS NOT NULL) 진도 있을 경우,
			if (ls.next()) {
				
				if ( p_gubun.equals("END") ) {
				//if ( 1 == 1 ) {
	                String v_ldate = ls.getString("ldate");
	                String v_sysdate = ls.getString("nowtime");
	                String v_total_time = ls.getString("total_time");
	                String v_session_time = EduEtc1Bean.get_duringtime(v_ldate, v_sysdate);
	                v_total_time = EduEtc1Bean.add_duringtime(v_total_time, v_session_time);

	                sql =
	                	"\n  UPDATE tz_progress  " +
	                	"\n     SET session_time = ?,  " +
	                	"\n         total_time = ?,  " +
	                	"\n         ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS'),  " +
	                	"\n         lesson_count = lesson_count + 1  " +
	                	"\n   WHERE subj = ?  " +
	                	"\n     AND YEAR = ?  " +
	                	"\n     AND subjseq = ?  " +
	                	"\n     AND userid = ?  " +
	                	"\n     AND lesson = ?  " +
	                	"\n     AND oid = ?  ";
	                logger.debug( "@ sql 1: " + sql );
	                pstmt = connMgr.prepareStatement(sql);
	                pstmt.setString(1, v_session_time);
	                pstmt.setString(2, v_total_time);
	                pstmt.setString(3, s_subj);
	                pstmt.setString(4, s_year);
	                pstmt.setString(5, s_subjseq);
	                pstmt.setString(6, s_userid);
	                pstmt.setString(7, p_lesson);
	                pstmt.setString(8, p_oid);

	    			logger.debug( "@ v_session_time : " + v_session_time );			
	    			logger.debug( "@ v_total_time : " + v_total_time );	
	    			logger.debug( "@ s_subj : " + s_subj );	
	    			logger.debug( "@ s_year : " + s_year );	
	    			logger.debug( "@ s_subjseq : " + s_subjseq );	
	    			logger.debug( "@ s_userid : " + s_userid );	
	    			logger.debug( "@ p_lesson : " + p_lesson );	
	    			logger.debug( "@ p_oid : " + p_oid );
	                
	                count = pstmt.executeUpdate();
	    			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
	    			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
				} else {
					sql =
						"\n  UPDATE tz_progress  " +
						"\n     SET ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')  " +
						"\n   WHERE subj = ?  " +
						"\n     AND YEAR = ?  " +
						"\n     AND subjseq = ?  " +
						"\n     AND userid = ?  " +
						"\n     AND lesson = ?  " +
						"\n     AND oid = ?  ";
					logger.debug( "@ sql2 : " + sql );
					pstmt = connMgr.prepareStatement(sql);
	                pstmt.setString(1, s_subj);
	                pstmt.setString(2, s_year);
	                pstmt.setString(3, s_subjseq);
	                pstmt.setString(4, s_userid);
	                pstmt.setString(5, p_lesson);
                	pstmt.setString(6, p_oid);
	                
	                count = pstmt.executeUpdate();
				}
				ls.close();
				pstmt.close();
			} 
			// 특수교육원용
			else {
				if ( p_gubun.equals("END") ) {


					ls = connMgr.executeQuery(sql);
					
					if (ls.next()) {
		                String v_ldate = ls.getString("ldate");
		                String v_sysdate = ls.getString("nowtime");
		                String v_total_time = ls.getString("total_time");
		                String v_session_time = EduEtc1Bean.get_duringtime(v_ldate, v_sysdate);
		                v_total_time = EduEtc1Bean.add_duringtime(v_total_time, v_session_time);

		                sql =
		                	"\n  UPDATE tz_progress  " +
		                	"\n     SET session_time = ?,  " +
		                	"\n         total_time = ?,  " +
		                	"\n         ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS'),  " +
		                	"\n         lesson_count = lesson_count + 1  " +
		                	"\n   WHERE subj = ?  " +
		                	"\n     AND YEAR = ?  " +
		                	"\n     AND subjseq = ?  " +
		                	"\n     AND userid = ?  " +
		                	"\n     AND lesson = ?  " +
		                	"\n     AND oid = ?  ";
		                
		                pstmt = connMgr.prepareStatement(sql);
		                pstmt.setString(1, v_session_time);
		                pstmt.setString(2, v_total_time);
		                pstmt.setString(3, s_subj);
		                pstmt.setString(4, s_year);
		                pstmt.setString(5, s_subjseq);
		                pstmt.setString(6, s_userid);
		                pstmt.setString(7, p_lesson);
		                pstmt.setString(8, p_oid);

		    			logger.debug( "@ v_session_time : " + v_session_time );			
		    			logger.debug( "@ v_total_time : " + v_total_time );	
		    			logger.debug( "@ s_subj : " + s_subj );	
		    			logger.debug( "@ s_year : " + s_year );	
		    			logger.debug( "@ s_subjseq : " + s_subjseq );	
		    			logger.debug( "@ s_userid : " + s_userid );	
		    			logger.debug( "@ p_lesson : " + p_lesson );	
		    			logger.debug( "@ p_oid : " + p_oid );
		    			
		    			logger.debug( "@ sql3 : " + sql );
		                
		                count = pstmt.executeUpdate();
					}
		    		if (ls != null) { try { ls.close(); } catch (Exception e) {} }
		    		if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
				}
			}
			result = count > 0;
			
			if(p_lesson.equals("01")) result = true; 
				
			logger.debug( "@ is Already Completed EduCheck : " + result );
			logger.debug( "  => EduCheck ContentyType : " + p_contenttype + ", Gubun : " + p_gubun + ", Update : " + count );

			logger.debug( "@ s_subj : " + s_subj );	
			logger.debug( "@ s_year : " + s_year );	
			logger.debug( "@ s_subjseq : " + s_subjseq );	
			logger.debug( "@ s_userid : " + s_userid );	
			logger.debug( "@ p_lesson : " + p_lesson );	
			logger.debug( "@ p_oid : " + p_oid );	
			
		}
		catch (Exception ex) {
			logger.error( "Already Completed EduCheck : " + ex.getMessage(), ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
		
		return result;		
	}
	
	/**
	 * 진도체크를 실행한다 (Betatest일 경우)
	 * 
	 * @param box
	 * @return
	 */
	public boolean eduCheckForBetatest(RequestBox reqBox) throws Exception {
		
		eduCheckStartLog(reqBox, EduProgressBean.BETA_PROGRESS_TABLE_NAME);
		
		DBConnectionManager connMgr = null;

		String sql = "";
		boolean result = false;

		try {
			connMgr = new DBConnectionManager();
			
	        boolean isAlreadyCompletedEduCheck = false;
	        boolean isEduCheck = false;
	        boolean isEndEduCheck = reqBox.getStringDefault("p_gubun","END").equals("END")? true : false;
	        
        	isAlreadyCompletedEduCheck = isAlreadyCompletedEduCheck(connMgr, reqBox);
        	if ( !isAlreadyCompletedEduCheck ) {
    			isEduCheck = doEduCheck(connMgr, reqBox, EduProgressBean.BETA_PROGRESS_TABLE_NAME);
	        }

	        // 진도체크 나가지 않았음 && 진도끝 체크일 때 && 그런데 진도체크가 되지 않았을 경우!
	        if ( !isAlreadyCompletedEduCheck && isEndEduCheck && !isEduCheck ) {
	        	result = false;
	        } else {
	        	result = true;
	        }

	        eduCheckEndLog(result, isEduCheck);
		}
		catch (Exception ex) {
			logger.error( sql + "\n" + reqBox, ex );
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return result;		
	}

	private void eduCheckStartLog(RequestBox reqBox, String progressTableName) {
    	logger.debug( "====================================" );
    	logger.debug( "진도체크 시작 : 구분 = " + reqBox.getStringDefault("p_gubun","END") );
    	logger.debug( "진도 Table  : " + progressTableName );
    	logger.debug( "subj   : " + reqBox.getSession("s_subj") );
    	logger.debug( "year   : " + reqBox.getSession("s_year") );
    	logger.debug( "subjseq: " + reqBox.getSession("s_subjseq") );
    	logger.debug( "userid : " + reqBox.getSession("userid") );
    	logger.debug( "co-type: " + reqBox.getString("p_contenttype") );
    	logger.debug( "lesson : " + reqBox.getString("p_lesson") );
    	logger.debug( "oid    : " + reqBox.getStringDefault("p_oid", EduProgressBean.OID_NORMAL_DEFAULT_VALUE) );
    	logger.debug( "====================================" );
	}

	private void eduCheckEndLog(boolean result, boolean isEduCheck) {
        logger.debug( "====================================" );
    	logger.debug( "진도체크 종료 : Beta, " + result + ", 저장여부 = " + isEduCheck );
    	logger.debug( "====================================" );
	}

	/**
	 * 베타테스트 진도정보 삭제
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public boolean delBetaProgress(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		
		String sql = "";
		
		int cnt = 0;
		
		try	{
			connMgr = new DBConnectionManager();

			String v_subj = box.getString("p_subj");
			String s_userid = box.getSession("userid");
			
			sql =
				"\n  DELETE FROM tz_beta_progress  " +
				"\n        WHERE subj = ?  " +
				"\n          AND userid = ?  ";
			
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setString( 1, v_subj );
			pstmt.setString( 2, s_userid );
			
			cnt = pstmt.executeUpdate();
		}
		catch (Exception ex) {
			logger.error( sql + "\n" + box, ex );
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return cnt > -1;
	}
	
	/**
	 * 학습한 페이지정보 저장
	 * @param box
	 * @return
	 * @throws Exception
	 */
    public String setPageInfo(RequestBox box, String tableName) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        StringBuffer sbSQL = null;
        ListSet ls = null;
        
        int isOk     = 0;
        int pstmtIdx = 1; 
        String result = "N";
        int pagenum = 0;
        int existingPagenum = 0;
        
        try {
            connMgr = new DBConnectionManager();
            sbSQL = new StringBuffer();
            
            sbSQL.append(" select pagenum \n ");
            sbSQL.append("   from TZ_SUBJLESSON_PAGE \n ");
            sbSQL.append("  where subj = " + StringManager.makeSQL(box.getString("p_subj"))+" \n ");
            sbSQL.append("    and substr(starting, instr(starting, '/', -1) + 1) = " +StringManager.makeSQL(box.getString("p_pageInfo")) + " \n ");
            sbSQL.append("    and lesson = "+StringManager.makeSQL(box.getString("p_lesson"))+" \n "); 
			ls = connMgr.executeQuery(sbSQL.toString());
			
			if ( ls.next() ) {
				pagenum = ls.getInt("pagenum");
			}  			
			if(ls != null) { ls.close(); }
			
			sbSQL.setLength(0);
			
			/*
			sbSQL.append("select pagenum existingpagenum \n ") ;
			sbSQL.append("  from tz_subjlesson_page \n ") ;
			sbSQL.append("  where (subj, lesson, substr(starting, instr(starting, '/', -1) + 1)) = \n ") ;
			sbSQL.append("        ( \n ") ;
			sbSQL.append("            select subj, lesson, stu_page \n ") ;
			sbSQL.append("              from "+tableName+" \n ") ;
			sbSQL.append("             where subj = "+StringManager.makeSQL(box.getString("p_subj"))+" \n ") ;
			sbSQL.append("               and year = "+StringManager.makeSQL(box.getString("p_year"))+" \n ") ;
			sbSQL.append("               and subjseq = "+StringManager.makeSQL(box.getString("p_subjseq"))+" \n ") ;
			sbSQL.append("               and userid = "+StringManager.makeSQL(box.getString("p_userid"))+" \n ") ;
			sbSQL.append("               and lesson = ( \n ") ;
			sbSQL.append("                              select max(lesson) lesson \n ") ;
			sbSQL.append("                                from "+tableName+" \n ") ;
			sbSQL.append("                               where subj = "+StringManager.makeSQL(box.getString("p_subj"))+" \n ") ;
			sbSQL.append("                                 and year = "+StringManager.makeSQL(box.getString("p_year"))+" \n ") ;
			sbSQL.append("                                 and subjseq = "+StringManager.makeSQL(box.getString("p_subjseq"))+" \n ") ;
			sbSQL.append("                                 and userid = "+StringManager.makeSQL(box.getString("p_userid"))+" \n ") ;
			sbSQL.append("                             ) ") ;
			sbSQL.append("          ) ") ;
			*/
			sbSQL.append("select pagenum existingpagenum \n ") ;
			sbSQL.append("  from tz_subjlesson_page \n ") ;
			sbSQL.append("  where (subj, lesson, substr(starting, instr(starting, '/', -1) + 1)) = \n ") ;
			sbSQL.append("        ( \n ") ;
			sbSQL.append("            select subj, lesson, stu_page \n ") ;
			sbSQL.append("              from "+tableName+" \n ") ;
			sbSQL.append("             where subj = "+StringManager.makeSQL(box.getString("p_subj"))+" \n ") ;
			sbSQL.append("               and year = "+StringManager.makeSQL(box.getString("p_year"))+" \n ") ;
			sbSQL.append("               and subjseq = "+StringManager.makeSQL(box.getString("p_subjseq"))+" \n ") ;
			sbSQL.append("               and userid = "+StringManager.makeSQL(box.getString("p_userid"))+" \n ") ;
			sbSQL.append("               and lesson = "+StringManager.makeSQL(box.getString("p_lesson"))+" ") ;
			sbSQL.append("          ) ") ;

			ls = connMgr.executeQuery(sbSQL.toString());
			
			if ( ls.next() ) {
				existingPagenum = ls.getInt("existingpagenum");
			}  
			System.out.println(sbSQL.toString());
			System.out.println(sbSQL.toString());
            System.out.println("existingPagenum  >>> " + existingPagenum);
            System.out.println("existingPagenum  >>> " + existingPagenum);
			
			if(ls != null) { ls.close(); }
			
			if(pagenum > existingPagenum || existingPagenum == 0){
				
	            sbSQL.setLength(0);
	            sbSQL.append(" update "+tableName+" \n ");
	            sbSQL.append("    set stu_page = ? \n ");
	            sbSQL.append("  where subj = ? \n ");
	            sbSQL.append("    and year = ? \n ");
	            sbSQL.append("    and subjseq = ? \n ");
	            sbSQL.append("    and lesson = ? \n ");
	            sbSQL.append("    and userid = ? \n ");
	  			
	            /*
	            System.out.println(box.getString("p_pageInfo"));
	            System.out.println(box.getString("p_subj"));
	            System.out.println(box.getString("p_year"));
	            System.out.println(box.getString("p_subjseq"));
	            System.out.println(box.getString("p_lesson"));
	            System.out.println(box.getString("p_userid"));
	            */
	            
	            pstmtIdx = 1;
	  			pstmt = connMgr.prepareStatement(sbSQL.toString());
	  			pstmt.setString(pstmtIdx++, box.getString("p_pageInfo"));
	  			pstmt.setString(pstmtIdx++, box.getString("p_subj"));
	  			pstmt.setString(pstmtIdx++, box.getString("p_year"));
	  			pstmt.setString(pstmtIdx++, box.getString("p_subjseq"));
	  			pstmt.setString(pstmtIdx++, box.getString("p_lesson"));
	  			pstmt.setString(pstmtIdx++, box.getString("p_userid"));
	  			
	  			isOk  = pstmt.executeUpdate();
	  			if(isOk > 0){
	  				result = "Y";
	  			}
			} else {
				result = "Y";
			}
            
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }
        
        return result;
    }   
    
    public String getLessonLastPage(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls      = null;
        StringBuffer sbSQL = null;
        
        int isOk     = 0;
        int pstmtIdx = 1; 
        String result = "N";
        
        try {
            connMgr = new DBConnectionManager();
            sbSQL = new StringBuffer();
            
            System.out.println(box.getString("p_subj"));
            System.out.println(box.getString("p_lessoninfo"));
            
            sbSQL.append(" select max(pagenum) pagenum \n ");
            sbSQL.append("   from tz_subjlesson_page \n ");
            sbSQL.append("  where subj = '"+box.getString("p_subj")+"' \n ");
            sbSQL.append("    and lesson = '"+box.getString("p_lessoninfo")+"' \n ");
  			
            ls = connMgr.executeQuery(sbSQL.toString());

            if ( ls.next() )   { 
            	result = ls.getString("pagenum");
             }
            
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls   != null ) { try { ls.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }
        
        return result;
    }  
    
    public String getLessonLastPageInfo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls      = null;
        StringBuffer sbSQL = null;
        String sql = "";
        
        int isOk     = 0;
        int pstmtIdx = 1; 
        String result = "N";
        
        String v_subj = box.getString("p_subj");
        String v_lesson = box.getString("p_lesson");
        
        try {
            connMgr = new DBConnectionManager();
            sbSQL = new StringBuffer();
            
            sql = " select substr(starting, instr(starting, '/', -1) + 1) lastpage \n "+
		            "   from tz_subjlesson_page \n "+
		            "  where subj    = " +StringManager.makeSQL(v_subj)+ " \n"+
		            "    and lesson  = " +StringManager.makeSQL(v_lesson)+ " \n"+
		            "    and pagenum = (select max(pagenum) from tz_subjlesson_page where subj = "+StringManager.makeSQL(v_subj)+" and lesson="+StringManager.makeSQL(v_lesson)+") \n ";
  			
            System.out.println("getLessonLastPageInfo  :::  "+sql);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() )   { 
            	result = ls.getString("lastpage");
             }
            
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls   != null ) { try { ls.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }
        
        return result;
    }  
    


    // 20100512 학습창 진도 처리 하기 @ Parks
    public int setProgress(String p_subj, String p_year, String p_subjseq, String p_lesson, String p_userid, String p_sec) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        ListSet             ls      = null;
        String  sql  	= "";
        String  sdate  	= "";
        String  lastedu	= "";
        String  session_time = "";
        String  total_time   = "";
        
        int ret 		= 0;
        try { 
            connMgr = new DBConnectionManager();
            sql =   "SELECT " +
            		"		TOTAL_TIME, " +
            		"		TO_CHAR(SYSDATE-((1/24/60/60)*"+p_sec+"),'YYYYMMDDHH24MISS') AS LASTEDU, " +
            		"		TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') AS SDATE  " +
            		"FROM TZ_PROGRESS " +
            		"WHERE YEAR = " +StringManager.makeSQL(p_year)+ 
            		" 		AND SUBJ = " +StringManager.makeSQL(p_subj)+
            		" 		AND SUBJSEQ = " +StringManager.makeSQL(p_subjseq)+
            		" 		AND LESSON = " +StringManager.makeSQL(p_lesson)+
            		" 		AND USERID = " +StringManager.makeSQL(p_userid);
            
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
              total_time = ls.getString("total_time");
          	  sdate   	 = ls.getString("sdate");
          	  lastedu    = ls.getString("lastedu");
            }

            if (!"".equals(total_time)) {
            	// update
            	sql =	"UPDATE TZ_PROGRESS " +
            			"SET " +
            			"		SESSION_TIME = ?, "+
            			"		TOTAL_TIME = ?, "+
            			"		LESSON_COUNT = LESSON_COUNT + 1," +
            			"		LDATE = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'), "+
            			"		FIRST_END = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') "+
                		"WHERE YEAR = " +StringManager.makeSQL(p_year)+ 
                		" 		AND SUBJ = " +StringManager.makeSQL(p_subj)+
                		" 		AND SUBJSEQ = " +StringManager.makeSQL(p_subjseq)+
                		" 		AND LESSON = " +StringManager.makeSQL(p_lesson)+
                		" 		AND USERID = " +StringManager.makeSQL(p_userid);
            	
                session_time = EduEtc1Bean.get_duringtime(lastedu, sdate);
                total_time   = EduEtc1Bean.add_duringtime(total_time,session_time);
            } else {
            	// insert
            	sql =	"INSERT INTO TZ_PROGRESS " +
            			"		(SUBJ, YEAR, SUBJSEQ, LESSON, OID, " +
            			"		USERID, SESSION_TIME, TOTAL_TIME, FIRST_EDU, FIRST_END," +
            			"		LESSON_COUNT, LDATE)" +
            			"VALUES" +
            			"("+StringManager.makeSQL(p_subj)+", "+StringManager.makeSQL(p_year)+", "+StringManager.makeSQL(p_subjseq)+", "+StringManager.makeSQL(p_lesson)+", '1', " +
            			" "+StringManager.makeSQL(p_userid)+", ?, ?, TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'), " +
            			" '1', TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'))";
            	
                session_time = "00:00:00.00";
                total_time   = "00:00:00.00";
            }
            
  			pstmt = connMgr.prepareStatement(sql);
  			pstmt.setString(1, session_time);
  			pstmt.setString(2, total_time);
  			ret  = pstmt.executeUpdate();

//System.out.println("=====  session_time : "+session_time);  
//System.out.println("=====  total_time : "+total_time);  
//System.out.println(" === ret : "+ret);
//System.out.println(" === sql : "+sql);  

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("setProgress sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  ret;
    }
    
    
    // 20100525 학습창 출석 처리 하기 @ Parks
    public int setAttendance(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        ListSet ls      = null;
        String  sql  	= "";

        int ret 		= 0;
        try { 
            connMgr = new DBConnectionManager();
            sql =   "SELECT " +
            		"		COUNT(1) AS CNT " +
            		"FROM 	TZ_ATTENDANCE " +
            		"WHERE 	ATTDATE 	= TO_CHAR(SYSDATE,'YYYYMMDD') " +
            		"		AND YEAR 	= " +StringManager.makeSQL(p_year)+ 
            		" 		AND SUBJ 	= " +StringManager.makeSQL(p_subj)+
            		" 		AND SUBJSEQ = " +StringManager.makeSQL(p_subjseq)+
            		" 		AND USERID 	= " +StringManager.makeSQL(p_userid);
            
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
            	ret = ls.getInt("cnt");
            }

            if (ret == 0) {
            	// insert
            	sql =	"INSERT INTO TZ_ATTENDANCE " +
            			"		(SUBJ, YEAR, SUBJSEQ, USERID, ATTDATE, ATTTIME, ISATTEND, LDATE)" +
            			"VALUES" +
            			"("+StringManager.makeSQL(p_subj)+", "+StringManager.makeSQL(p_year)+", "+StringManager.makeSQL(p_subjseq)+", " +
            			" "+StringManager.makeSQL(p_userid)+", TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MI'), " +
            			" 'O', TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'))";

            	ret = connMgr.executeUpdate(sql);
            	
//	  			pstmt = connMgr.prepareStatement(sql);
//	  			pstmt.setString(1, "O");
//	  			ret  = pstmt.executeUpdate();
            }
//System.out.println("=====  session_time : "+session_time);  
//System.out.println("=====  total_time : "+total_time);  
//System.out.println(" === ret : "+ret);
//System.out.println(" === sql : "+sql);  

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("setAttendance sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  ret;
    }
}
