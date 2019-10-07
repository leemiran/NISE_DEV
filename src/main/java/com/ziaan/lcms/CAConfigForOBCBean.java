/*
 * @(#)CAConfigBean.java	1.0 2008. 11. 03
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * CA구성 DAO Bean
 *
 * @version		1.0, 2008/11/03
 * @author		Chung Jin-pil
 */
public class CAConfigForOBCBean
{
	private Logger logger = Logger.getLogger(this.getClass());

	public CAConfigForOBCBean()
	{
	}

	/**
	 * OBC 과정 목록
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public List selectOBCSubjList(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;

		String sql = "";
		List list = null;

		try
		{
			connMgr = new DBConnectionManager();
			list = new ArrayList();
		
			String p_subj = reqBox.getString("p_subj");
			
			sql =
				"\n  SELECT   subj, subjnm  " +
				"\n      FROM tz_subj  " +
				"\n     WHERE contenttype = 'O'  " +
				"\n  ORDER BY subj  ";

			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > OBC과정 목록  \n " + sql );
			
			DataBox dbox = null;
			while ( ls.next() ) 
			{
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		}
		catch (Exception ex)
		{
			logger.error( sql, ex );
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
		
		return list;		
	}

	/**
	 * OBC 과정에 연결된 Object List
	 * 단,  p_subj == "NO-SUBJ"일 경우, 미연결된 Object List를 반환
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public List selectObjectList(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;

		String sql = "";
		List list = null;

		try
		{
			connMgr = new DBConnectionManager();
			list = new ArrayList();
		
			String p_subj = reqBox.getString("p_subj");

			if ( p_subj.equals("NO-SUBJ") ) {
				sql =
					"\n  SELECT a.oid, a.sdesc obj_sdesc  " +
					"\n    FROM tz_object a, tz_subjobj b  " +
					"\n   WHERE a.OID = b.OID(+) AND b.OID IS NULL  ";
			} else {
				sql =
					"\n  SELECT a.subj, b.oid, a.sdesc subj_sdesc, b.sdesc obj_sdesc  " +
					"\n    FROM tz_subjobj a, tz_object b  " +
					"\n   WHERE a.OID = b.OID  " +
					"\n     AND a.subj = " + StringManager.makeSQL(p_subj);
			}

			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > oid 목록  \n " + sql );
			
			DataBox dbox = null;
			while ( ls.next() ) 
			{
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		}
		catch (Exception ex)
		{
			logger.error( sql, ex );
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
		
		return list;		
	}

	/**
	 * OBC Lesson에 맵핑된 Object 저장
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception 
	 */
	public boolean assignObject(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		boolean result = false;
		
		try
		{
			boolean deleteAssignObject = false;
			boolean insertAssignObject = false;
			
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			deleteAssignObject = deleteAssignObject( connMgr, reqBox );
			insertAssignObject = insertAssignObject( connMgr, reqBox );
			
			result = insertAssignObject;
			logger.debug( " 기존 Object 맵핑정보 삭제 : " + deleteAssignObject + " , 추가 : " + insertAssignObject );
			
			if ( result ) { connMgr.commit(); }
			else { connMgr.rollback(); }
		}
		catch (Exception ex)
		{
			connMgr.rollback();
			logger.error(ex);
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			connMgr.setAutoCommit(true);
			
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return result;	
	}

	/**
	 * OBC 맵핑된 Object 삭제
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean deleteAssignObject(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");
	
			sql =
				"\n  DELETE FROM tz_subjobj  " +
				"\n   WHERE subj = ?  " +
				"\n     AND module = ?  " +
				"\n     AND lesson =  ?  ";				
	
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson Object 맵핑 정보 삭제  \n " + sql );
			
			pstmt.setString(1, p_subj);
			pstmt.setString(2, p_module);
			pstmt.setString(3, p_lesson);
			
			result = pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result > 0 ? true:false;
	}

	/**
	 * OBC 맵핑된 Object 등록
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean insertAssignObject(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		boolean result = false;
	
		try
		{
			int count = 0;
			
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");
			String p_type = reqBox.getStringDefault("p_type", "SC");
			String p_types = reqBox.getStringDefault("p_types", "1001");
			String p_oid = "";
			
			String s_userid = reqBox.getSession("userid");
			
			Vector destObject = reqBox.getVector("destObject");
	
			sql =
				"\n  INSERT INTO tz_subjobj  " +
				"\n         ( subj, TYPE, module, lesson, OID, ordering,  " +
				"\n  			   sdesc, TYPES, luserid, ldate )  " +
				"\n         VALUES  " +
				"\n  	    ( ?, ?, ?, ?, ?, ?,  " +
				"\n          (SELECT sdesc FROM TZ_OBJECT WHERE oid = ?), ?, ?, TO_CHAR (SYSDATE, 'YYYYMMDDHHMISS') )  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson Object 맵핑 정보 등록  :" + destObject.size() + "건 \n " + sql );
			
			for ( int i=0; i<destObject.size(); i++ ) 
			{
				p_oid = (String) destObject.get(i);
				
				pstmt.setString(1, p_subj);
				pstmt.setString(2, p_type);
				pstmt.setString(3, p_module);
				pstmt.setString(4, p_lesson);
				pstmt.setString(5, p_oid);
				pstmt.setInt(6, i);
				pstmt.setString(7, p_oid);
				pstmt.setString(8, p_types);
				pstmt.setString(9, s_userid);
				
				count += pstmt.executeUpdate();
			}
			
			 result = ( count == destObject.size() );
		}
		catch (Exception ex)
		{
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result;
	}

	/**
	 * OBC 맵핑된 Object 목록
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public List selectAssignObjectList(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;

		String sql = "";
		List list = null;

		try
		{
			connMgr = new DBConnectionManager();
			list = new ArrayList();
		
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");
			
			sql =
				"\n  SELECT   oid, sdesc  " +
				"\n      FROM tz_subjobj  " +
				"\n     WHERE subj = " + StringManager.makeSQL(p_subj) +
				"\n       AND module = " + StringManager.makeSQL(p_module) +
				"\n  	 AND lesson = " + StringManager.makeSQL(p_lesson) +
				"\n  ORDER BY ordering  ";
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > 과정에 맵핑된 Object 목록  \n " + sql );
			
			DataBox dbox = null;
			while ( ls.next() ) 
			{
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		}
		catch (Exception ex)
		{
			logger.error( sql, ex );
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
		
		return list;	
	}

	/**
	 * OBC 맛보기 Object 저장
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean insertPreviewObject(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		boolean result = false;
		
		try
		{
			boolean deletePreviewObject = false;
			boolean insertPreviewObject = false;
			
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			deletePreviewObject = deletePreviewObject( connMgr, reqBox );
			insertPreviewObject = insertPreviewObject( connMgr, reqBox );
			
			result = deletePreviewObject && insertPreviewObject;
			logger.debug( " 맛보기 정보 삭제 : " + deletePreviewObject + " , 추가 : " + insertPreviewObject );

			if ( result ) { connMgr.commit(); }
			else { connMgr.rollback(); }
		}
		catch (Exception ex)
		{
			connMgr.rollback();
			logger.error(ex);
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			connMgr.setAutoCommit(true);
			
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return result;
	}
	
	/**
	 * 권장학습시간 저장
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean updateEduTimeForObject(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		
		String sql = "";
		boolean result = false;
		
		try
		{
			connMgr = new DBConnectionManager();
			String p_subj = reqBox.getString("p_subj");
			String p_temp = reqBox.getString("p_temp");
			
			String[] p_oid = p_temp.split(":");

			String module = "";
			String lesson = "";
			String oid = "";
			int eduTime = 0;
			String eduTimeYn = "";
			
			sql =
				"\n  UPDATE tz_subjobj SET edu_time = ?, edu_time_yn = ?  " +
				"\n  WHERE subj = ?  " +
				"\n    AND module = ?  " +
				"\n    AND lesson = ?  " +
				"\n    AND oid = ?  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > SCO 수정  \n " + sql + reqBox );

			int count = 0;
			String[] info = null;
			for ( int i=0; i<p_oid.length; i++ ) 
			{
				info = p_oid[i].split(",");

				module = info[0];
				lesson = info[1];
				oid = info[2];
				eduTime = Integer.parseInt(info[3]);
				eduTimeYn = info[4];

				pstmt.setInt(1, eduTime);
				pstmt.setString(2, eduTimeYn);
				pstmt.setString(3, p_subj);
				pstmt.setString(4, module);
				pstmt.setString(5, lesson);
				pstmt.setString(6, oid);

				count += pstmt.executeUpdate();
			}
			
			if ( p_oid.length == count ) {
				result = true;
			}
		}
		catch (Exception ex)
		{
			logger.error(ex);
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
		
		return result;
	}

	/**
	 * OBC 맛보기 Object 등록
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean insertPreviewObject(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		boolean result = false;
	
		try
		{
			int count = 0;
			
			String p_subj = reqBox.getString("p_subj");
			String p_temp_oid = reqBox.getString("p_temp");

			String[] p_oid = null;
			if ( !p_temp_oid.equals("") ) {
				p_oid = p_temp_oid.split(":");
			}

			String s_userid = reqBox.getSession("userid");

			if ( p_oid != null ) {

				sql =
					"\n  INSERT INTO tz_previewobj  " +
					"\n         ( subj, OID, oidnm, ordering,  " +
					"\n  		  luserid, ldate )  " +
					"\n         VALUES  " +
					"\n  	    ( ?, ?, (SELECT sdesc FROM TZ_OBJECT WHERE oid = ?), ?,  " +
					"\n           ?, TO_CHAR (SYSDATE, 'YYYYMMDDHHMISS') )  ";
	
				pstmt = connMgr.prepareStatement(sql);
				logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson 맛보기 Object 등록  :" + p_oid.length + "건 \n " + sql );
	
				
				for ( int i=0; i<p_oid.length; i++ ) 
				{
					pstmt.setString(1, p_subj);
					pstmt.setString(2, p_oid[i]);
					pstmt.setString(3, p_oid[i]);
					pstmt.setInt(4, i);
					pstmt.setString(5, s_userid);
					
					count += pstmt.executeUpdate();
				}
				
				 result = ( count == p_oid.length );
			} else {
				result = true;
			}
		}
		catch (Exception ex)
		{
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result;
	}

	/**
	 * OBC 맛보기 Object 삭제
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean deletePreviewObject(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			String p_subj = reqBox.getString("p_subj");
	
			sql =
				"\n  DELETE FROM tz_previewobj  " +
				"\n   WHERE subj = ?  ";
	
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > OBC 맛보기 정보 삭제  \n " + sql );
			
			pstmt.setString(1, p_subj);
			
			result = pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result > -1 ? true:false;
	}

	/**
	 * OBC Object 정보 목록
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public DataBox selectObjectInfo(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;

		String sql = "";
		DataBox dbox = null;

		try
		{
			connMgr = new DBConnectionManager();
		
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");
			String p_oid = reqBox.getString("p_oid");

			sql =
				"\n  SELECT a.OID, a.sdesc AS subj_sdesc, a.TYPES, b.sdesc AS obj_sdesc,  " +
				"\n         b.starting, b.npage  " +
				"\n    FROM tz_subjobj a, tz_object b  " +
				"\n   WHERE a.subj = " + StringManager.makeSQL(p_subj) +
				"\n     AND a.module = " + StringManager.makeSQL(p_module) +
				"\n     AND a.lesson = " + StringManager.makeSQL(p_lesson) +
				"\n     AND a.OID = " + StringManager.makeSQL(p_oid) +
				"\n     AND a.OID = b.OID  ";
				
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > OBC Object Info  \n " + sql );
			
			while ( ls.next() ) 
			{
				dbox = ls.getDataBox();
			}
		}
		catch (Exception ex)
		{
			logger.error( sql, ex );
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
		
		return dbox;			
	}

	/**
	 * OBC Object 정보 수정
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean updateObjectInfo(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		int result = 0;
		
		try
		{
			connMgr = new DBConnectionManager();
			
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");
			String p_oid = reqBox.getString("p_oid");

			String p_sdesc = reqBox.getString("p_sdesc");
			String p_types = reqBox.getString("p_types");
			
			sql =
				"\n  UPDATE tz_subjobj SET sdesc = ?, types = ?  " +
				"\n  WHERE subj = ?  " +
				"\n    AND module = ?  " +
				"\n    AND lesson = ?  " +
				"\n    AND oid = ?  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > SCO 수정  \n " + sql + reqBox );
			
			pstmt.setString(1, p_sdesc);
			pstmt.setString(2, p_types);
			pstmt.setString(3, p_subj);
			pstmt.setString(4, p_module);
			pstmt.setString(5, p_lesson);
			pstmt.setString(6, p_oid);

			result = pstmt.executeUpdate();
			pstmt.close();
		}
		catch (Exception ex)
		{
			logger.error(ex);
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return result > 0;	
	}

	public boolean deletePreviewObj(DBConnectionManager connMgr, RequestBox reqBox) throws Exception {
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");
	
			sql =
				"\n  DELETE FROM tz_previewobj  " +
				"\n   WHERE subj = ?  ";
			
			if ( !p_module.equals("") ) {
				sql +=
					"\n     AND OID IN (SELECT OID FROM tz_subjobj  " +
					"\n                  WHERE subj = " + StringManager.makeSQL(p_subj) +
					"\n						AND module =   " + StringManager.makeSQL(p_module);
					
				if ( !p_lesson.equals("") ) {
					sql +=
						"\n				AND lesson = " + StringManager.makeSQL(p_lesson);
				}
				
				sql += " )  ";
			}

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > OBC 맛보기 정보 삭제  \n " + sql );
			
			pstmt.setString(1, p_subj);
			
			result = pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result > -1 ? true:false;		
	}

	public boolean deleteSubjObj(DBConnectionManager connMgr, RequestBox reqBox) throws Exception {
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");
	
			sql =
				"\n  DELETE FROM tz_subjobj  " +
				"\n   WHERE subj = ?  ";
			
			if ( !p_module.equals("") ) {
				sql += "\n     AND module = " + StringManager.makeSQL(p_module); 
			}
				
			if ( !p_lesson.equals("") ) {
				sql += "\n     AND lesson =  " + StringManager.makeSQL(p_lesson);				
			}
	
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson Object 맵핑 정보 삭제  \n " + sql );
			
			pstmt.setString(1, p_subj);
			
			result = pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result > -1 ? true:false;		
	}
}
