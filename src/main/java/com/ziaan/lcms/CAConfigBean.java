/*
 * @(#)CAConfigBean.java	1.0 2008. 11. 03
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * CA구성 DAO Bean
 *
 * @version		1.0, 2008/11/03
 * @author		Chung Jin-pil
 */
public class CAConfigBean
{
	private Logger logger = Logger.getLogger(this.getClass());

	public CAConfigBean()
	{
	}

	/**
	 * CA구성 - 목록 (KT, Normal)
	 * 
	 * @param reqBox
	 * @return
	 */
	public List selectCAListForNormal(RequestBox reqBox) throws Exception
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
				"\n  SELECT   a.subj, a.module, a.sdesc AS modulenm, a.TYPES AS moduletypes,  " +
				"\n           b.lesson, b.sdesc AS lessonnm, b.TYPES AS lessontypes, b.starting,  " +
				"\n           b.edu_time, b.edu_time_yn, c.width, c.height  " +
				"\n      FROM tz_subjmodule a, tz_subjlesson b, tz_subj c  " +
				"\n     WHERE a.subj = c.subj and a.subj = b.subj(+) AND a.module = b.module(+) AND a.subj = " + StringManager.makeSQL(p_subj) +
				"\n  ORDER BY module, lesson  ";				

			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > 목록(KT&Normal)  \n " + sql );
			
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
	 * CA구성 - 목록 (OBC)
	 * 
	 * @param reqBox
	 * @return
	 */
	public List selectCAListForOBC(RequestBox reqBox) throws Exception
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
				"\n  SELECT   a.subj, a.module, a.sdesc AS modulenm, a.TYPES AS moduletypes,  " +
				"\n           b.lesson, b.sdesc AS lessonnm, b.TYPES AS lessontypes, b.starting lessonstarting,  " +
				"\n           c.OID, c.ordering, c.sdesc AS objnm, c.TYPE AS objtype, d.starting, d.npage,  " +
				"\n           c.edu_time, c.edu_time_yn, e.width, e.height   " +
				"\n      FROM tz_subjmodule a, tz_subjlesson b, tz_subjobj c, tz_object d, tz_subj e  " +
				"\n     WHERE a.subj = e.subj  " +
				"\n       AND a.subj = b.subj(+)  " +
				"\n       AND a.module = b.module(+)  " +
				"\n       AND a.subj = " + StringManager.makeSQL(p_subj) +
				"\n       AND b.subj = c.subj(+)  " +
				"\n       AND b.module = c.module(+)  " +
				"\n       AND b.lesson = c.lesson(+)  " +
				"\n       AND c.OID = d.OID(+)  " +
				"\n  ORDER BY a.module, b.lesson, c.ordering  ";
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > 목록(OBC)   \n " + sql );
			
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
	 * CA목록 Module(Lesson대상) Rowspan 정보 (KT, Normal)
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public Map selectModuleRowspanForNormalMap(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;

		String sql = "";
		Map map = null;

		try
		{
			connMgr = new DBConnectionManager();
			map = new HashMap();
		
			String p_subj = reqBox.getString("p_subj");
			
			sql =
				"\n  SELECT   module, COUNT (lesson) rowspan  " +
				"\n      FROM tz_subjlesson  " +
				"\n     WHERE subj = " + StringManager.makeSQL(p_subj) +
				"\n  GROUP BY module  " +
				"\n  ORDER BY module  ";
				
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Module Rowspan  \n " + sql );
			
			while ( ls.next() ) 
			{
				map.put( ls.getString("module"), ls.getString("rowspan") );
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
		
		return map;
	}
	
	/**
	 * CA목록 Module(Object대상) Rowspan 정보 (OBC)
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public Map selectModuleRowspanForOBCMap(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		
		String sql = "";
		Map map = null;
		
		try
		{
			connMgr = new DBConnectionManager();
			map = new HashMap();
			
			String p_subj = reqBox.getString("p_subj");
			
			sql =
				"\n      SELECT a.module, COUNT(b.lesson) rowspan      " +
				"\n        FROM tz_subjmodule a, tz_subjlesson b, tz_subjobj c      " +
				"\n       WHERE a.subj = " + StringManager.makeSQL(p_subj) +
				"\n  	   AND a.subj = b.subj(+)  " +
				"\n  	   AND a.module = b.module(+)  " +
				"\n         AND b.subj = c.subj(+)  " +
				"\n    	 AND b.module = c.module(+)   " +
				"\n    	 AND b.lesson = c.lesson(+)      " +
				"\n    GROUP BY a.module    " +
				"\n    ORDER BY a.module  ";

			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Module(OBC) Rowspan  \n " + sql );
			
			while ( ls.next() ) 
			{
				map.put( ls.getString("module"), ls.getString("rowspan") );
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
		
		return map;
	}
	
	/**
	 * CA목록 Lesson(Object대상) Rowspan 정보 (OBC)
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public Map selectLessonRowspanForOBCMap(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		
		String sql = "";
		Map map = null;
		
		try
		{
			connMgr = new DBConnectionManager();
			map = new HashMap();
			
			String p_subj = reqBox.getString("p_subj");
			
			sql =
				"\n  SELECT   a.lesson, COUNT (b.oid) rowspan  " +
				"\n      FROM tz_subjlesson a, tz_subjobj b  " +
				"\n     WHERE a.subj =  " + StringManager.makeSQL(p_subj) +
				"\n       AND a.subj = b.subj  " +
				"\n  	 AND a.module = b.module  " +
				"\n  	 AND a.lesson = b.lesson  " +
				"\n  GROUP BY a.lesson  " +
				"\n  ORDER BY a.lesson  ";
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Lesson(OBC) Rowspan  \n " + sql );
			
			while ( ls.next() ) 
			{
				map.put( ls.getString("lesson"), ls.getString("rowspan") );
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
		
		return map;
	}

	/**
	 * Module 정보
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public DataBox selectModuleInfo(RequestBox reqBox) throws Exception
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

			if ( p_module != null && !p_module.equals("") ) {
			
				sql =
					"\n  SELECT subj, module, sdesc AS modulenm, nvl(TYPES, '1001') as moduletypes  " +
					"\n    FROM tz_subjmodule  " +
					"\n   WHERE subj = " + StringManager.makeSQL(p_subj) +
					"\n     AND module = " + StringManager.makeSQL(p_module);					
					
				ls = connMgr.executeQuery(sql);
				logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Module Info  \n " + sql );
				
				while ( ls.next() ) 
				{
					dbox = ls.getDataBox();
				}
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
	 * 다음 Module 값
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public String selectNextModule(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		
		String sql = "";
		String nextModule = "";
		
		try
		{
			connMgr = new DBConnectionManager();

			String p_subj = reqBox.getString("p_subj");

			sql =
				"\n  SELECT TRIM(TO_CHAR(NVL(MAX(module),0)+1, '00')) next_module  " +
				"\n    FROM tz_subjmodule  " +
				"\n   WHERE subj = " + StringManager.makeSQL(p_subj);
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Next Module \n " + sql );
			
			while ( ls.next() ) 
			{
				nextModule = ls.getString("next_module");
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
		
		return nextModule;		
	}
	
	/**
	 * Lesson 정보
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public DataBox selectLessonInfo(RequestBox reqBox) throws Exception
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
			
			if ( (p_module != null && !p_module.equals("")) && (p_lesson != null && !p_lesson.equals("")) ) {
			
				sql =
					"\n  SELECT a.module, a.sdesc AS modulenm, nvl(a.types, '1001') as moduletypes,  " +
					"\n         b.lesson, b.sdesc AS lessonnm, nvl(b.types, '1001') as lessontypes, b.starting  " +
					"\n    FROM tz_subjmodule a, tz_subjlesson b  " +
					"\n   WHERE a.subj = b.subj  " +
					"\n     AND a.module = b.module  " +
					"\n     AND a.subj = " + StringManager.makeSQL(p_subj) +
					"\n     AND a.module = " + StringManager.makeSQL(p_module) +
					"\n     AND b.lesson = " + StringManager.makeSQL(p_lesson);
				
				ls = connMgr.executeQuery(sql);
				logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Lesson Info \n " + sql );
				
				while ( ls.next() ) 
				{
					dbox = ls.getDataBox();
				}
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
	 * 다음 Lesson 값
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public String selectNextLesson(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		
		String sql = "";
		String nextModule = "";
		
		try
		{
			connMgr = new DBConnectionManager();
			
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			
			if ( p_module != null && !p_module.equals("") ) {
				
				sql =
					"\n  SELECT TRIM(TO_CHAR(NVL(MAX(lesson),0)+1, '00')) next_lesson  " +
					"\n    FROM tz_subjlesson  " +
					"\n   WHERE subj = " + StringManager.makeSQL(p_subj);
				
				ls = connMgr.executeQuery(sql);
				logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Next Lesson  \n " + sql );
				
				while ( ls.next() ) 
				{
					nextModule = ls.getString("next_lesson");
				}
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
		
		return nextModule;		
	}

	/**
	 * Module 목록
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public List selectModuleList(RequestBox reqBox) throws Exception
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
			
			sql =
				"\n  SELECT subj, module, sdesc AS modulenm, types  " +
				"\n    FROM tz_subjmodule  " +
				"\n   WHERE subj = " + StringManager.makeSQL(p_subj);
			
			if ( !p_module.equals("") )
				sql += "\n     AND module != " + StringManager.makeSQL(p_module);
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Module List   \n " + sql );
			
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
	 * Lesson 목록
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public List selectLessonList(RequestBox reqBox) throws Exception
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
				"\n  SELECT a.subj, a.module, a.sdesc AS modulenm, a.TYPES AS moduletypes,  " +
				"\n         b.lesson, TRIM(TO_CHAR(TO_NUMBER(b.lesson)+1, '00')) next_lesson, b.sdesc AS lessonnm, b.TYPES AS lessontypes  " +
				"\n    FROM tz_subjmodule a, tz_subjlesson b  " +
				"\n   WHERE a.subj = b.subj AND a.module = b.module AND a.subj = " + StringManager.makeSQL(p_subj);
	
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Lesson List   \n " + sql );
			
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
	 * Module 등록
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean insertModule(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		boolean result = false;
		
		try
		{
			boolean isInsertSubjModule = false;
			
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			/*
			boolean isUpdateModuleOrder = false;
			boolean isUpdateLessonOrder = false;

			// module, lesson 재부여
			if ( !p_order.equals("") ) {
				boolean isExistOrderModule = isExistOrderModule( connMgr, p_subj, p_order );
				
				if ( isExistOrderModule ) {
					isUpdateModuleOrder = updateModuleOrder( connMgr, p_subj, p_order );
					isUpdateLessonOrder = updateLessonOrder( connMgr, p_subj, p_order );
				}

				p_module = p_order;
			} else {
				isUpdateModuleOrder = true;
				isUpdateLessonOrder = true;
			}
			*/

			isInsertSubjModule = insertSubjModule( connMgr, reqBox );
			result = isInsertSubjModule;

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
	 * Module 정보등록
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean insertSubjModule(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_modulenm = reqBox.getString("p_modulenm");
			String p_types = reqBox.getString("p_types");
			String s_userid = reqBox.getSession("userid");

			sql =
				"\n  INSERT INTO tz_subjmodule  " +
				"\n      ( subj, module, sdesc, types, luserid, ldate ) " +
				"\n  VALUES  " +
				"\n      ( ?, ?, ?, ?, ?, TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') )  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson저장  \n " + sql );
			
			pstmt.setString(1, p_subj);
			pstmt.setString(2, p_module);
			pstmt.setString(3, p_modulenm);
			pstmt.setString(4, p_types);
			pstmt.setString(5, s_userid);
			
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
	 * Module 수정
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean updateModule(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
	
		String sql = "";
		boolean result = false;
	
		try
		{
			connMgr = new DBConnectionManager();
			result = updateSubjModule( connMgr, reqBox );
		}
		catch (Exception ex)
		{
			logger.error(ex);
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
	
		return result;	
	}

	/**
	 * Module 삭제
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean deleteModule(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		int result = 0;
		
		try
		{
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			String p_contenttype = reqBox.getString("p_contenttype");
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");

			sql =
				"\n  DELETE FROM tz_subjmodule  " +
				"\n  WHERE subj = ? " +
				"\n    AND module = ? ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Module삭제 \n " + sql );
			
			pstmt.setString(1, p_subj);
			pstmt.setString(2, p_module);
			
			result = pstmt.executeUpdate();
			pstmt.close();
			
			sql =
				"\n  DELETE FROM tz_subjlesson  " +
				"\n  WHERE subj = ? " +
				"\n    AND module = ? ";
			
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson삭제 \n " + sql );
			
			pstmt.setString(1, p_subj);
			pstmt.setString(2, p_module);

			pstmt.executeUpdate();
			pstmt.close();


			// OBC일 경우, 맵핑된 Object 및 맛보기정보 삭제
			if ( p_contenttype.equals("O") ) {
				CAConfigForOBCBean caob = new CAConfigForOBCBean();
				caob.deletePreviewObj( connMgr, reqBox );
				caob.deleteSubjObj( connMgr, reqBox );
			}
			
			connMgr.commit();
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

		return result > 0;		
	}

	/**
	 * Lesson 등록
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean insertLesson(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		boolean result = false;
		
		try
		{
			connMgr = new DBConnectionManager();
			result = insertSubjLesson( connMgr, reqBox );
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
	 * TODO 삭제예정
	 * 하위 Lesson 순서변경
	 * 
	 * @param connMgr
	 * @param p_subj
	 * @param p_lesson
	 * @return
	 * @throws Exception
	 */
	private boolean incrementSubLesson(DBConnectionManager connMgr, String p_subj, String p_lesson) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			sql =
				"\n  UPDATE tz_subjlesson  " +
				"\n     SET lesson = lesson + 1  " +
				"\n   WHERE subj = ?  " +
				"\n     AND lesson >= ?  ";
			
			pstmt = connMgr.prepareStatement(sql);
			
			pstmt.setString(1, p_subj);
			pstmt.setString(2, p_lesson);
			
			result = pstmt.executeUpdate();
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > 하위 Lesson 재부여  " + ": " + (result>0) + "\n "+ sql );
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
	 * Lesson 정보수정
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean insertSubjLesson(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");
			String p_lessonnm = reqBox.getString("p_lessonnm");
			String p_types = reqBox.getString("p_types");
			String p_starting = reqBox.getString("p_starting");
			String s_userid = reqBox.getSession("userid");
			
			sql =
				"\n  INSERT INTO tz_subjlesson  " +
				"\n      ( subj, module, lesson, sdesc, types, owner, starting, luserid, ldate ) " +
				"\n  VALUES  " +
				"\n      ( ?, ?, ?, ?, ?, ?, ?, ?, TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') )  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson저장  \n " + sql );
			
			pstmt.setString(1, p_subj);
			pstmt.setString(2, p_module);
			pstmt.setString(3, p_lesson);
			pstmt.setString(4, p_lessonnm);
			pstmt.setString(5, p_types);
			pstmt.setString(6, p_subj);
			pstmt.setString(7, p_starting);
			pstmt.setString(8, s_userid);
			
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
	 * Lesson 수정
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean updateLesson(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
	
		String sql = "";
		boolean result = false;
	
		try
		{
			connMgr = new DBConnectionManager();
			result = updateSubjLesson( connMgr, reqBox );
		}
		catch (Exception ex)
		{
			logger.error(ex);
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
	
		return result;
	}

	/**
	 * Lesson 삭제
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean deleteLesson(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		int result = 0;
		
		try
		{
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			String p_contenttype = reqBox.getString("p_contenttype");
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");

			sql =
				"\n  DELETE FROM tz_subjlesson  " +
				"\n  WHERE subj = ?  " +
				"\n    AND module = ?  " +
				"\n    AND lesson = ?  ";
			
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson 삭제 \n " + sql );
			
			pstmt.setString(1, p_subj);
			pstmt.setString(2, p_module);
			pstmt.setString(3, p_lesson);

			result = pstmt.executeUpdate();
			pstmt.close();
			
			// OBC일 경우, 맵핑된 Object 및 맛보기정보 삭제
			if ( p_contenttype.equals("O") ) {
				CAConfigForOBCBean caob = new CAConfigForOBCBean();
				caob.deletePreviewObj( connMgr, reqBox );
				caob.deleteSubjObj( connMgr, reqBox );
			}
			
			connMgr.commit();
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

		return result > 0;	
	}

	/**
	 * Module 존재여부
	 * 
	 * @param connMgr
	 * @param p_subj
	 * @param p_order
	 * @return
	 * @throws Exception
	 */
	private boolean isExistOrderModule(DBConnectionManager connMgr, String p_subj, String p_order) throws Exception
	{
		ListSet ls = null;
		int count = 0;
	
		try
		{
			String sql =
				"\n  SELECT count(module) count  " +
				"\n    FROM tz_subjmodule  " +
				"\n   WHERE subj =  " + StringManager.makeSQL(p_subj) +
				"\n     AND module = TRIM(TO_CHAR(TO_NUMBER(" + StringManager.makeSQL(p_order) + ")+1, '00'))  "; 
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Module 존재여부   \n " + sql );
			
			if ( ls.next() ) {
				count = ls.getInt("count");
			}
		}
		catch (Exception ex)
		{
			throw new Exception( ex.getMessage() );
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
		}
		
		return count > 0;
	}

	/**
	 * Lesson Order Update
	 * 
	 * @param connMgr
	 * @param p_subj
	 * @param p_order
	 * @return
	 * @throws Exception
	 */
	private boolean updateLessonOrder(DBConnectionManager connMgr, String p_subj, String p_order) throws Exception
	{
		PreparedStatement pstmt = null;
		ListSet ls = null;
		
		boolean isUpdateModuleSql = false;
		boolean isUpdateLessonSql = false;
	
		try
		{
			int count = 0;
			
			// Module Order 재부여
			String updateModuleSql =
				"\n  UPDATE tz_subjlesson  " +
				"\n     SET module = TRIM (TO_CHAR (TO_NUMBER (module) + 1, '00'))  " +
				"\n   WHERE subj = ?  " +
				"\n     AND module > ?  ";
			
			pstmt = connMgr.prepareStatement(updateModuleSql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Lesson 재부여  Step1 \n " + updateModuleSql );
	
			pstmt.setString(1, p_subj);
			pstmt.setString(2, p_order);
			
			count = pstmt.executeUpdate();
			if ( count > -1 ) isUpdateModuleSql = true;
			
			pstmt.close();
	
			String selectAssignLessonSql =
				"\n  SELECT subj, module, lesson,  " +
				"\n         TRIM (TO_CHAR (TO_NUMBER (ROWNUM), '00')) assignlesson  " +
				"\n    FROM (SELECT   subj, module, lesson  " +
				"\n              FROM tz_subjlesson  " +
				"\n             WHERE subj = " + StringManager.makeSQL(p_subj) +
				"\n          ORDER BY module, lesson)  ";
			
			ls = connMgr.executeQuery(selectAssignLessonSql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Lesson 재부여  Step2 \n " + selectAssignLessonSql );
	
			String updateLessonSql = 
				"\n  UPDATE tz_subjlesson  " +
				"\n     SET lesson = ?  " +
				"\n   WHERE subj = ? AND module = ? AND lesson = ?  ";
	
			pstmt = connMgr.prepareStatement(updateLessonSql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Lesson 재부여  Step3 \n " + updateLessonSql );
			
			while ( ls.next() ) {
				pstmt.setString( 1, ls.getString("assignlesson") );
				pstmt.setString( 2, ls.getString("subj") );
				pstmt.setString( 3, ls.getString("module") );
				pstmt.setString( 4, ls.getString("lesson") );
				
				count = pstmt.executeUpdate();
				boolean isFailedAtLeastOnce = false;
				
				if ( count > 0 && !isFailedAtLeastOnce )
				{
					isUpdateLessonSql = true;
				}
				else
				{
					isFailedAtLeastOnce = true;
					isUpdateLessonSql = false;
				}			
			}
			pstmt.close();
		}
		catch (Exception ex)
		{
			throw new Exception( ex.getMessage() );
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }			
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		logger.debug( "# updateLesson " + isUpdateModuleSql + " " + isUpdateLessonSql );
		return isUpdateModuleSql && isUpdateLessonSql;
	}

	/**
	 * Module Order 수정
	 * 
	 * @param connMgr
	 * @param p_subj
	 * @param p_order
	 * @return
	 * @throws Exception
	 */
	private boolean updateModuleOrder(DBConnectionManager connMgr, String p_subj, String p_order) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			logger.debug( p_subj + " " + p_order );
			// Module Order 재부여
			sql =
				"\n  UPDATE tz_subjmodule  " +
				"\n     SET module = TRIM (TO_CHAR (TO_NUMBER (module) + 1, '00'))  " +
				"\n   WHERE subj = ?  " +
				"\n     AND module > ?  ";
	
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > Module 재부여  \n " + sql );
	
			pstmt.setString(1, p_subj);
			pstmt.setString(2, p_order);
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
	 * Module 정보수정
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean updateSubjModule(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			String p_subj = reqBox.getString("p_subj");
			String p_order= reqBox.getString("p_order");
			String p_module = reqBox.getString("p_module");
			String p_modulenm = reqBox.getString("p_modulenm");
			String p_types = reqBox.getString("p_types");
	
			String s_userid = reqBox.getSession("userid");
	
			sql =
				"\n  UPDATE tz_subjmodule  " +
				"\n  SET  " +
				"\n      module = ?, sdesc = ?, types = ?,  " +
				"\n      luserid = ?, ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')  " +
				"\n  WHERE  " +
				"\n      subj = ? AND module = ?  ";
	
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Module수정  \n " + sql );
			
			pstmt.setString(1, p_module);
			pstmt.setString(2, p_modulenm);
			pstmt.setString(3, p_types);
			pstmt.setString(4, s_userid);
			pstmt.setString(5, p_subj);
			pstmt.setString(6, p_module);
			
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
	 * Lesson 정보 수정
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean updateSubjLesson(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			String p_subj = reqBox.getString("p_subj");
			String p_module = reqBox.getString("p_module");
			String p_lesson = reqBox.getString("p_lesson");
			String p_lesson_ori = reqBox.getString("p_lesson_ori");
			String p_lessonnm = reqBox.getString("p_lessonnm");
			String p_starting = reqBox.getString("p_starting");
			String p_types = reqBox.getString("p_types");
			String s_userid = reqBox.getSession("userid");

			sql =
				"\n  UPDATE tz_subjlesson  " +
				"\n  SET  " +
				"\n      module = ?, lesson = ?, sdesc = ?, types = ?, starting = ?,  " +
				"\n      luserid = ?, ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')  " +
				"\n  WHERE  " +
				"\n      subj = ? AND module = ? AND lesson = ?";
	
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson수정  \n " + sql );
			
			pstmt.setString(1, p_module);
			pstmt.setString(2, p_lesson);
			pstmt.setString(3, p_lessonnm);
			pstmt.setString(4, p_types);
			pstmt.setString(5, p_starting);
			pstmt.setString(6, s_userid);
			pstmt.setString(7, p_subj);
			pstmt.setString(8, p_module);
			pstmt.setString(9, p_lesson_ori);

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
	 * TODO 삭지예정
	 * 하위Lesson Update 필요 여부 
	 * (추가할 Lesson이 중복 될 경우, 하위 Lesson을 Update 해주어야 함)
	 * 
	 * @param reqBox
	 * @param nextLesson
	 * @return
	 * @throws Exception
	 */
	public boolean isUpdateSubLesson(RequestBox reqBox, String nextLesson) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		
		int count = 0;

		try
		{
			connMgr = new DBConnectionManager();
			
			String p_subj = reqBox.getString("p_subj");
			
			String sql =
				"\n  SELECT count(lesson) count  " +
				"\n    FROM tz_subjlesson  " +
				"\n   WHERE subj = " + StringManager.makeSQL(p_subj) +
				"\n     AND lesson = " + StringManager.makeSQL(nextLesson);

			ls = connMgr.executeQuery(sql);
			
			if ( ls.next() ) {
				count = ls.getInt("count");
			}
			
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > 하위Lesson Update 필요 여부   " + (count>0) + "\n " + sql );
		}
		catch (Exception ex)
		{
			throw new Exception( ex.getMessage() );
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
		
		return count > 0;		
	}
	
	/**
	 * OBC 맛보기 Object 목록
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public Map selectPreviewObjectMap(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		
		String sql = "";
		Map map = null;
		
		try
		{
			connMgr = new DBConnectionManager();
			map = new HashMap();
			
			String p_subj = reqBox.getString("p_subj");
			
			sql =
				"\n  SELECT oid, oidnm  " +
				"\n    FROM tz_previewobj  " +
				"\n   WHERE subj = " + StringManager.makeSQL(p_subj);
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > OBC 맛보기 Object 목록  \n " + sql );
			
			DataBox dbox = null;
			while ( ls.next() ) 
			{
				map.put( ls.getString("oid"), ls.getString("oidnm") );
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
		
		return map;		
	}
	
	/**
	 * 권장학습시간 저장
	 * 
	 * @param reqBox
	 * @return
	 */
	public boolean updateEduTimeForLesson(RequestBox reqBox) throws Exception {
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
			int eduTime = 0;
			String eduTimeYn = "";
			
			sql =
				"\n  UPDATE tz_subjlesson SET edu_time = ?, edu_time_yn = ?  " +
				"\n  WHERE subj = ?  " +
				"\n    AND module = ?  " +
				"\n    AND lesson = ?  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > SCO 수정  \n " + sql + reqBox );

			int count = 0;
			String[] info = null;
			for ( int i=0; i<p_oid.length; i++ ) 
			{
				info = p_oid[i].split(",");

				module = info[0];
				lesson = info[1];
				eduTime = Integer.parseInt(info[2]);
				eduTimeYn = info[3];

				pstmt.setInt(1, eduTime);
				pstmt.setString(2, eduTimeYn);
				pstmt.setString(3, p_subj);
				pstmt.setString(4, module);
				pstmt.setString(5, lesson);

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
	 * CA정보를 삭제한다. (Module, Lesson 모두 삭제)
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCA(RequestBox reqBox) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		int result = 0;
		
		try
		{
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			String p_contenttype = reqBox.getString("p_contenttype");
			String p_subj = reqBox.getString("p_subj");

			sql =
				"\n  DELETE FROM tz_subjmodule  " +
				"\n  WHERE subj = ? ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Module삭제 \n " + sql );
			
			pstmt.setString(1, p_subj);
			
			result = pstmt.executeUpdate();
			pstmt.close();
			
			sql =
				"\n  DELETE FROM tz_subjlesson  " +
				"\n  WHERE subj = ? ";
			
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > Lesson삭제 \n " + sql );
			
			pstmt.setString(1, p_subj);

			pstmt.executeUpdate();
			pstmt.close();


			// OBC일 경우, 맵핑된 Object 및 맛보기정보 삭제
			if ( p_contenttype.equals("O") ) {
				CAConfigForOBCBean caob = new CAConfigForOBCBean();
				caob.deletePreviewObj( connMgr, reqBox );
				caob.deleteSubjObj( connMgr, reqBox );
			}
			
			connMgr.commit();
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

		return result > 0;		
	}

	/**
	 * 차시 중복여부 체크
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean isDuplicateLesson(RequestBox reqBox) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		
		int count = 0;

		try
		{
			connMgr = new DBConnectionManager();

			String p_subj = reqBox.getString("p_subj");
			String p_lesson = reqBox.getString("p_lesson");
			String p_lesson_ori = reqBox.getString("p_lesson_ori");

			String sql =
				"\n  SELECT count(lesson) count  " +
				"\n    FROM tz_subjlesson  " +
				"\n   WHERE subj = " + StringManager.makeSQL(p_subj) +
				"\n     AND lesson = " + StringManager.makeSQL(p_lesson)+
				"\n     AND lesson != " + StringManager.makeSQL(p_lesson_ori);

			ls = connMgr.executeQuery(sql);
			
			if ( ls.next() ) {
				count = ls.getInt("count");
			}
			
			logger.debug( "LCMS > 컨텐츠 관리 > CA구성 > 하위Lesson Update 필요 여부   " + (count>0) + "\n " + sql );
		}
		catch (Exception ex)
		{
			throw new Exception( ex.getMessage() );
		}
		finally
		{
			if (ls != null) { try { ls.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
		
		return count > 0;
	}
	
	public ArrayList SelectLessonPageList(RequestBox box) throws Exception {
		DBConnectionManager connMgr 				= null;
		ListSet 			ls 						= null;
		ArrayList 			list1 					= null;
		StringBuffer		sql  					= new StringBuffer();
		DataBox        		dbox    				= null;
		
		String v_subj			= box.getString("p_subj");
		String v_lesson			= box.getString("p_lesson");
		
		try {
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();
			
			sql.append(" select subj, module, lesson, pagenum, starting \n" +
					" from TZ_SUBJLESSON_PAGE \n" +
					" where subj = '" + v_subj + "' \n" +
					" 	and lesson = '" + v_lesson + "' \n" +
			" order by pagenum ");
			
			ls = connMgr.executeQuery(sql.toString());
			while (ls.next()) {
				dbox = ls.getDataBox();
				
				list1.add(dbox); 
			}
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql.toString());
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list1;
	}
	
    /**
    마스터폼정보 조회
    @param box          receive from the form object and session
    @return MasterFormData   마스터폼 정보
    */
	public MasterFormData SelectMasterFormData(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls = null,ls2=null;
		ArrayList list1 = null, list2=null;
		String sql  = "", sql2="";
		MasterFormData data = null;
		
		String  unchangableMaxLesson = "001"; //수정/삭제 불가 최대차시
		
		String ss_gyear = box.getStringDefault("s_gyear",FormatDate.getDate("yyyy")); //교육년도
		
		String p_subj = box.getString("p_subj");
		String p_process = box.getString("p_process");
		
		
		try {
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();
			
			sql = "select subj, subjnm, iscentered, dir, isuse, mftype, width, height, mfdlist, "
				+ "       otbgcolor, mfgrdate, nvl(contenttype,'N') contenttype, " //OBC tree배경색, module출력여부(Normal MsForm)
				+ "       server,port,eduurl,preurl,vodurl,eduprocess,otbgcolor, isoutsourcing "
				+ "  from tz_subj "
				+ " where  subj="+StringManager.makeSQL(p_subj);
			
			ls = connMgr.executeQuery(sql);
			if (ls.next()) {
				data=new MasterFormData();
				data.setSubj        (ls.getString("subj"));
				data.setSubjnm      (ls.getString("subjnm"));
				data.setIscentered  (ls.getString("iscentered"));
				data.setDir         (ls.getString("dir"));
				data.setMftype      (ls.getString("mftype"));
				data.setWidth       (ls.getInt("width"));
				data.setHeight      (ls.getInt("height"));
				data.setMfdlist     (ls.getString("mfdlist"));
				data.setOtbgcolor   (ls.getString("otbgcolor"));
				data.setMfgrdate    (ls.getString("mfgrdate"));
				data.setContenttype (ls.getString("contenttype"));
				data.setServer      (ls.getString("server"));
				data.setPort        (ls.getString("port"));
				data.setEduurl      (ls.getString("eduurl"));
				data.setPreurl      (ls.getString("preurl"));
				data.setVodurl      (ls.getString("vodurl"));
				data.setEduprocess  (ls.getString("eduprocess"));
				data.setOtbgcolor   (ls.getString("otbgcolor"));
				data.setIsoutsourcing  (ls.getString("isoutsourcing"));
				
				
				//박기만 20080219
				if(ls != null) ls.close();
				
				if(p_process.equals("updateModulePage")){
					sql = "select count(module) CNTS from tz_subjmodule  where  subj="+StringManager.makeSQL(p_subj);
					if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
					ls2 = connMgr.executeQuery(sql);
					ls2.next();
					data.setCnt_module(ls2.getInt("CNTS"));
					
					
				}
				
				sql = "select count(lesson) CNTS from tz_subjlesson  where  subj="+StringManager.makeSQL(p_subj);
				if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
				ls2 = connMgr.executeQuery(sql);
				ls2.next();
				data.setCnt_lesson(ls2.getInt("CNTS"));
				
				if(p_process.equals("updateLessonPage")){
					sql = "select max(lesson) LS from tz_progress  where  subj="+StringManager.makeSQL(p_subj);
					if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
					ls2 = connMgr.executeQuery(sql);
					if(ls2.next() && (!ls2.getString("LS").equals(""))) unchangableMaxLesson=ls2.getString("LS");
					
					//박기만 20080219
					if(ls2 != null) ls2.close();
					
					sql = "select max(lesson) LS from tz_exammaster  where  subj="+StringManager.makeSQL(p_subj)
					+ "  and  lesson > "+StringManager.makeSQL(unchangableMaxLesson);
					if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
					ls2 = connMgr.executeQuery(sql);
					if(ls2.next() && (!ls2.getString("LS").equals(""))) unchangableMaxLesson=ls2.getString("LS");
					
					//박기만 20080219
					if(ls2 != null) ls2.close();
					
					sql = "select max(lesson) LS from tz_projord  where  subj="+StringManager.makeSQL(p_subj)
					+ "  and  lesson > "+StringManager.makeSQL(unchangableMaxLesson);
					if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
					ls2 = connMgr.executeQuery(sql);
					if(ls2.next() && (!ls2.getString("LS").equals("")))  unchangableMaxLesson=ls2.getString("LS");
					
				}
				
				data.setUnchangableMaxLesson(unchangableMaxLesson);
				
				if(p_process.equals("updateBranchPage")&&(data.getCnt_branch()>0)){
					sql = "select count(branch) LS from tz_branch  where  subj="+StringManager.makeSQL(p_subj);
					if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
					ls2 = connMgr.executeQuery(sql);
					if(ls2.next())  data.setCnt_branch(ls2.getInt("LS"));
				}
				list1.add(data);
			}
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
			
			//수정가능여부 체크
			data.setCanModify(canModify(connMgr,p_subj,"MAIN",""));
			
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return data;
	}
	
	public boolean canModify(   DBConnectionManager connMgr,
			String      p_subj,
			String      p_gubun,
			String      p_sub)      throws Exception {
		ListSet ls = null;
		String sql = "";
		boolean resultb = true;
		int cnts = 0;
		
		try{
			
			if(p_gubun.equals("MAIN")){
				sql = "select count(*) CNTS from tz_progress where subj="+StringManager.makeSQL(p_subj);
			} else if(p_gubun.equals("LESSON")){
				sql = "select count(*) CNTS from tz_progress where subj="+StringManager.makeSQL(p_subj)
				+ "   and lesson="+StringManager.makeSQL(p_sub);
			} else if(p_gubun.equals("BRANCH")){
				sql = "select count(*) CNTS from tz_progress where subj="+StringManager.makeSQL(p_subj)
				+ "   and branch="+p_sub;
			} else if(p_gubun.equals("OBJECT-ASSIGN")){ //OBC: Object연결정보 수정가능여부
				sql = "select count(*) CNTS from tz_progress where subj="+StringManager.makeSQL(p_subj)
				+ "   and lesson="+StringManager.makeSQL(p_sub.substring(0,2))
				+ "   and oid="+StringManager.makeSQL(p_sub.substring(2,12));
			}
			
			if(!sql.equals("")){
				if(ls != null) { try { ls.close(); }catch (Exception e) {} }
				ls = connMgr.executeQuery(sql);
				ls.next();
				cnts = ls.getInt("CNTS");
				if(cnts>0)  resultb = false;
			}
		}
		catch(Exception ex) {
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
		}
//제약 조건 걸지 않음
		resultb = true;
		return resultb;
		
	}
    
}
