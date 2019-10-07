/*
 * @(#)CAConfigBean.java	1.0 2008. 11. 03
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * CA구성 DAO Bean
 *
 * @version		1.0, 2008/11/03
 * @author		Chung Jin-pil
 */
public class CAConfigExcelBean
{
	private Logger logger = Logger.getLogger(this.getClass());

	public CAConfigExcelBean()
	{
	}

	// module 정보
	public class ModuleInfo {
		public String subj = null;
		public String module = null;
		public String sdesc = null;
		public String luserid = null;
	}

	// lesson 정보
	public class LessonInfo {
		public String subj = null;
		public String module = null;
		public String lesson = null;
		public String sdesc = null;
		public String starting = null;
		public int eduTime = 0;
		public String eduTimeYn = null;
		public String luserid = null;
	}
	
	// lesson별 페이지 정보
	public class LessonPageInfo { 
		public String subj = null;
		public String lesson = null;
		public String pagenum = null;
		public String starting = null;
		public String luserid = null;
	}

	// 과정정보
	public class SubjInfo {
		public String subj = null;
		public String width = null;
		public String height = null;
	}

	// object정보
	public class ObjectInfo {
		public String oid = null;
		public String otype = null;
		public String filetype = null;
		public int npage = 0;
		public String sdesc = null;
		public String master = null;
		public String starting = null;
		public String server = null;
		public String guid = null;
		public String luserid = null;
	}

	// Object과정(Subj)맵핑정보
	public class SubjObjInfo {
		public String subj = null;
		public String type = null;
		public String module = null;
		public String lesson = null;
		public String oid = null;
		public int ordering = 0;
		public String sdesc = null;
		public String types = null;
		public String luserid = null;
		public int eduTime = 0;
		public String eduTimeYn = null;
	}
	
	/**
	 * 엑셀등록 - KT, Normal
	 *  
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public String uploadExcelCA(RequestBox reqBox) throws Exception {

		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		String msg = "";
		boolean result = false;
		
		try
		{
			boolean isModuleInfo = false;
			boolean isLessonInfo = false;
			boolean isSubjInfo = false;
			
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			Map caInfoMaps = getCAInfoMaps(reqBox);
			
			isModuleInfo = insertModuleInfo( connMgr, (Map) caInfoMaps.get("moduleInfoMap") );
			isLessonInfo = insertLessonInfo( connMgr, (Map) caInfoMaps.get("lessonInfoMap") );
			//isSubjInfo = updateSubjInfo( connMgr, (Map) caInfoMaps.get("subjInfoMap") );

			if ( !isModuleInfo ) {
				msg += "모듈 등록 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;이미 모듈이 등록되었거나 엑셀에 잘못된 정보가 들어 있는지 확인하세요.";
			}
			else if ( !isLessonInfo ) {
				msg += "레슨 등록 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;이미 레슨이 등록되었거나 엑셀에 잘못된 정보가 들어 있는지 확인하세요.";
			}
			//else if ( !isSubjInfo ) {
			//	msg += "과정 정보 수정 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;엑셀에 잘못된 정보가 들어 있는지 확인하세요.";
			//}

			result = isModuleInfo && isLessonInfo; //&& isSubjInfo;
			
			if ( result ) {
				connMgr.commit();
				msg = "OK";
			}
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

		return msg;
	}

	/**
	 * 페이지 엑셀등록
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public String uploadExcelPageCA(RequestBox reqBox) throws Exception {

		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		String msg = "";
		boolean result = false;
		
		try
		{
			boolean isLessonPageInfo = false;
			
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			Map caInfoMaps = getPageCAInfoMaps(reqBox);
			
			isLessonPageInfo = insertLessonPageInfo( connMgr, (Map) caInfoMaps.get("lessonPageInfoMap") );

			if ( !isLessonPageInfo ) {
				msg += "페이지정보 등록 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;이미 페이지정보가 등록되었거나 엑셀에 잘못된 정보가 들어 있는지 확인하세요.";
			}

			result = isLessonPageInfo; 
			
			if ( result ) {
				connMgr.commit();
				msg = "OK";
			}
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

		return msg;
	}
	
	// module, lesson, 과정정보 Map 반환
	private Map getCAInfoMaps(RequestBox reqBox) throws Exception {
		
		Map maps = new HashMap();

		String subj = reqBox.getString("p_subj"); 
		String userid = reqBox.getSession("userid"); 
	    String v_newFileName = reqBox.getNewFileName("p_file");
	    
	    ConfigSet conf = new ConfigSet();
	    String fileName = conf.getProperty("dir.upload.caconfig") + v_newFileName; 
	    
	    Workbook workbook = Workbook.getWorkbook( new File(fileName) );
	    Sheet sheet = workbook.getSheet(0);
	    
	    Map moduleInfoMap = new HashMap();
	    Map lessonInfoMap = new HashMap();
	    Map subjInfoMap = new HashMap();

	    // 엑셀 Column 순서
	    String module = "";
	    String moduleName = "";
	    String lesson = "";
	    String lessonName = "";
	    String eduTime = "";
	    String eduTimeYn = "";
	    String starting = "";
	    String width = "";
	    String height = "";
	    
	    for( int i=1; i<sheet.getRows(); i++ ) {
	    	module = getCellContents( sheet, 0, i );
	    	moduleName = getCellContents( sheet, 1, i );
	    	lesson = getCellContents( sheet, 2, i );
	    	lessonName = getCellContents( sheet, 3, i );
	    	starting = getCellContents( sheet, 4, i );
	    	//width = getCellContents( sheet, 5, i );
	    	//height = getCellContents( sheet, 6, i );
	    	//eduTime = getCellContents( sheet, 7, i, "0" );
	    	//eduTimeYn = getCellContents( sheet, 8, i, "N" );

	    	if ( !module.equals("") && !lesson.equals("") ) {
		    	if ( !moduleInfoMap.containsKey(module) ) {
		    		ModuleInfo moduleInfo = new ModuleInfo();
		    		moduleInfo.subj = subj;
		    		moduleInfo.module = module;
		    		moduleInfo.sdesc = moduleName;
		    		moduleInfo.luserid = userid;
		    		moduleInfoMap.put( module, moduleInfo );
		    	}
		    	
		    	if ( !lessonInfoMap.containsKey(module+lesson) ) {
		    		LessonInfo lessonInfo = new LessonInfo();
		    		lessonInfo.subj = subj;
		    		lessonInfo.module = module;
		    		lessonInfo.lesson = lesson;
		    		lessonInfo.sdesc = lessonName;
		    		lessonInfo.eduTime = !eduTime.equals("") ? Integer.parseInt( eduTime ) : 0;
		    		lessonInfo.eduTimeYn = eduTimeYn;
		    		lessonInfo.starting = starting;
		    		lessonInfo.luserid = userid;
		    		lessonInfoMap.put( module+lesson, lessonInfo );
		    	}
	
		    	if ( !subjInfoMap.containsKey(subj) ) {
		    		SubjInfo subjInfo = new SubjInfo();
		    		subjInfo.subj = subj;
		    		subjInfo.width = width;
		    		subjInfo.height = height;
		    		subjInfoMap.put( subj, subjInfo );
		    	}
	    	}
	    }

	    maps.put( "moduleInfoMap", moduleInfoMap );
	    maps.put( "lessonInfoMap", lessonInfoMap );
	    maps.put( "subjInfoMap", subjInfoMap );
	    
		return maps;
	}
	
	private Map getPageCAInfoMaps(RequestBox reqBox) throws Exception {
		
		Map maps = new HashMap();

		String subj_org = reqBox.getString("p_subj"); 
		String userid = reqBox.getSession("userid"); 
	    String v_newFileName = reqBox.getNewFileName("p_file");
	    
	    ConfigSet conf = new ConfigSet();
	    String fileName = conf.getProperty("dir.upload.caconfig") + v_newFileName; 
	    
	    Workbook workbook = Workbook.getWorkbook( new File(fileName) );
	    Sheet sheet = workbook.getSheet(0);
	    
	    Map lessonPageInfoMap = new HashMap();

	    // 엑셀 Column 순서
	    String subj = "";
	    String lesson = "";
	    String starting = "";
	    String pagenum = "";
	    String s_userid = reqBox.getSession("userid");
	    
	    for( int i=1; i<sheet.getRows(); i++ ) {
	    	subj = getCellContents( sheet, 0, i );
	    	lesson = getCellContents( sheet, 1, i );
	    	pagenum = getCellContents( sheet, 2, i );
	    	starting = getCellContents( sheet, 3, i );

		    	
	    	LessonPageInfo lessonPageInfo = new LessonPageInfo();
	    	
	    	lessonPageInfo.subj = subj;
	    	lessonPageInfo.lesson = lesson;
	    	lessonPageInfo.pagenum = pagenum;
	    	lessonPageInfo.starting = starting;
	    	lessonPageInfo.luserid = s_userid;
	    	lessonPageInfoMap.put( subj+lesson+pagenum+starting, lessonPageInfo );
	    }

	    maps.put( "lessonPageInfoMap", lessonPageInfoMap );
	    
		return maps;
	}

	// 엑셀 Sheet Cell Data 반환
	private String getCellContents( Sheet sheet, int col, int row ) {
		return getCellContents( sheet, col, row, "" );
	}

	// 엑셀 Sheet Cell Data 반환
	private String getCellContents( Sheet sheet, int col, int row, String defaultValue ) {
		String cellContents = sheet.getCell(col,row).getContents();
		cellContents = cellContents != null ? cellContents.trim() : defaultValue;

		return cellContents;
	}

	// module 정보 DB Insert
	private boolean insertModuleInfo(DBConnectionManager connMgr, Map moduleInfoMap) throws Exception {
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			sql =
				"\n  INSERT INTO TZ_SUBJMODULE (  " +
				"\n     SUBJ, MODULE, SDESC,   " +
				"\n     TYPES, LUSERID, LDATE)   " +
				"\n  VALUES ( ?, TRIM(TO_CHAR(?, '00')), ?,  " +
				"\n     '1001', ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') )  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > 엑셀등록  \n " + sql );
			
			Iterator it = moduleInfoMap.keySet().iterator();
			while ( it.hasNext() ) {
				
				ModuleInfo moduleInfo = (ModuleInfo) moduleInfoMap.get(it.next());
				pstmt.clearParameters();
				pstmt.setString(1, moduleInfo.subj );
				pstmt.setString(2, moduleInfo.module );
				pstmt.setString(3, moduleInfo.sdesc );
				pstmt.setString(4, moduleInfo.luserid );
				
				result += pstmt.executeUpdate();
			}
			pstmt.close();
		}
		catch (Exception ex)
		{
			logger.error(sql, ex);
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result == moduleInfoMap.size() ? true:false;
	}

	// lesson 정보 DB Insert
	private boolean insertLessonInfo(DBConnectionManager connMgr, Map lessonInfoMap) {
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			sql =
				"\n  INSERT INTO TZ_SUBJLESSON (  " +
				"\n     SUBJ, MODULE, LESSON, SDESC,  " +
				"\n     TYPES, OWNER, STARTING, ISBRANCH, LUSERID, LDATE  " +
				"\n      )  " +
				"\n  VALUES ( ?, TRIM(TO_CHAR(?, '00')), TRIM(TO_CHAR(?, '00')), ?,  " +
				"\n     '1001', ?, ?, 'N', ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')  " +
				"\n     )  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > 엑셀등록  \n " + sql );
			
			Iterator it = lessonInfoMap.keySet().iterator();
			while ( it.hasNext() ) {
				
				LessonInfo lessonInfo = (LessonInfo) lessonInfoMap.get(it.next());
				pstmt.clearParameters();
				pstmt.setString(1, lessonInfo.subj );
				pstmt.setString(2, lessonInfo.module );
				pstmt.setString(3, lessonInfo.lesson );
				pstmt.setString(4, lessonInfo.sdesc );
				pstmt.setString(5, lessonInfo.subj );
				pstmt.setString(6, lessonInfo.starting );
				pstmt.setString(7, lessonInfo.luserid );
				
				result += pstmt.executeUpdate();
			}
			pstmt.close();
		}
		catch (Exception ex)
		{
			logger.error(sql, ex);
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result == lessonInfoMap.size() ? true:false;
	}
	
	// object 정보 DB Insert
	private boolean insertObjectInfo(DBConnectionManager connMgr, Map objectInfoMap) {
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			sql =
				"\n  INSERT INTO tz_object  " +
				"\n              (OID, otype, filetype, npage, sdesc,   " +
				"\n  			 MASTER, starting, server, luserid, ldate, guid)  " +
				"\n       VALUES (?, ?, ?, ?, ?,  " +
				"\n  	         ?, ?, ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS' ),  " +
				"\n  			 ? )  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > 엑셀등록  \n " + sql );
			
			Iterator it = objectInfoMap.keySet().iterator();
			while ( it.hasNext() ) {
				
				ObjectInfo objectInfo = (ObjectInfo) objectInfoMap.get(it.next());
				pstmt.clearParameters();
				pstmt.setString(1, objectInfo.oid );
				pstmt.setString(2, objectInfo.otype );
				pstmt.setString(3, objectInfo.filetype );
				pstmt.setInt(4, objectInfo.npage );
				pstmt.setString(5, objectInfo.sdesc );
				pstmt.setString(6, objectInfo.master );
				pstmt.setString(7, objectInfo.starting );
				pstmt.setString(8, objectInfo.server );
				pstmt.setString(9, objectInfo.luserid );
				pstmt.setString(10, objectInfo.guid );
				
				result += pstmt.executeUpdate();
			}
			pstmt.close();
		}
		catch (Exception ex)
		{
			logger.error(sql, ex);
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result == objectInfoMap.size() ? true:false;
	}	

	// object과정(subj)맵핑정보 DB Insert
	private boolean insertSubjObjInfo(DBConnectionManager connMgr, Map subjObjInfoMap) {
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			sql =
				"\n  INSERT INTO TZ_SUBJOBJ (  " +
				"\n     SUBJ, TYPE, MODULE, LESSON, OID, ORDERING,   " +
				"\n     SDESC, TYPES, LUSERID,   " +
				"\n     LDATE, COMMENTSFROMLMS)   " +
				"\n  VALUES ( ?, ?,  TRIM(TO_CHAR(?, '00')), TRIM(TO_CHAR(?, '00')), ?, ?,  " +
				"\n      ?, ?, ?,  " +
				"\n      TO_CHAR( sysdate, 'YYYYMMDDHH24MISS'), '')  ";
			
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > 엑셀등록  \n " + sql );
			
			Iterator it = subjObjInfoMap.keySet().iterator();
			while ( it.hasNext() ) {
				
				SubjObjInfo subjObjInfo = (SubjObjInfo) subjObjInfoMap.get(it.next());
				pstmt.clearParameters();
				pstmt.setString(1, subjObjInfo.subj );
				pstmt.setString(2, subjObjInfo.type );
				pstmt.setString(3, subjObjInfo.module );
				pstmt.setString(4, subjObjInfo.lesson );
				pstmt.setString(5, subjObjInfo.oid );
				pstmt.setInt(6, subjObjInfo.ordering );
				pstmt.setString(7, subjObjInfo.sdesc );
				pstmt.setString(8, subjObjInfo.types );
				pstmt.setString(9, subjObjInfo.luserid );
				result += pstmt.executeUpdate();
			}
			pstmt.close();
		}
		catch (Exception ex)
		{
			logger.error(sql, ex);
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result == subjObjInfoMap.size() ? true:false;
	}
	
	// 과정정보 DB Update
	private boolean updateSubjInfo(DBConnectionManager connMgr, Map subjInfoMap) {
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			sql =
				"\n  UPDATE TZ_SUBJ SET WIDTH = ?, HEIGHT = ?  " +
				"\n  WHERE SUBJ = ?  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > C/A 구성 > 엑셀등록  \n " + sql );
			
			Iterator it = subjInfoMap.keySet().iterator();
			while ( it.hasNext() ) {
				
				SubjInfo subjInfo = (SubjInfo) subjInfoMap.get(it.next());
				pstmt.clearParameters();
				pstmt.setInt(1, Integer.parseInt(subjInfo.width) );
				pstmt.setInt(2, Integer.parseInt(subjInfo.height) );
				pstmt.setString(3, subjInfo.subj );
				
				result += pstmt.executeUpdate();
			}
			pstmt.close();
		}
		catch (Exception ex)
		{
			logger.error(sql, ex);
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result == subjInfoMap.size() ? true:false;
	}

	/**
	 * 엑셀등록 - OBC, OBC-Author
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public String uploadExcelCAForOBC(RequestBox reqBox) throws Exception {

		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		String msg = "";
		boolean result = false;
		
		try
		{
			boolean isModuleInfo = false;
			boolean isLessonInfo = false;
			boolean isSubjObjInfo = false;
			boolean isObjectInfo = false;
			boolean isSubjInfo = false;
			
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			Map caInfoMapsForOBC = getCAInfoMapsForOBC(reqBox);
			
			isModuleInfo = insertModuleInfo( connMgr, (Map) caInfoMapsForOBC.get("moduleInfoMap") );
			isLessonInfo = insertLessonInfo( connMgr, (Map) caInfoMapsForOBC.get("lessonInfoMap") );
			isObjectInfo = insertObjectInfo( connMgr, (Map) caInfoMapsForOBC.get("objectInfoMap") );
			isSubjObjInfo = insertSubjObjInfo( connMgr, (Map) caInfoMapsForOBC.get("subjObjInfoMap") );
			//isSubjInfo = updateSubjInfo( connMgr, (Map) caInfoMapsForOBC.get("subjInfoMap") );

			if ( !isModuleInfo ) {
				msg += "*모듈 등록 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;이미 모듈이 등록되었거나 엑셀에 잘못된 정보가 들어 있는지 확인하세요.\r\n<br/>";
			}
			if ( !isLessonInfo ) {
				msg += "*레슨 등록 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;이미 레슨이 등록되었거나 엑셀에 잘못된 정보가 들어 있는지 확인하세요.\r\n<br/>";
			}
			if ( !isObjectInfo ) {
				msg += "*Object 등록 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;이미 Object가 등록되었거나 엑셀에 잘못된 정보가 들어 있는지 확인하세요.\r\n<br/>";
			}
			if ( !isSubjObjInfo ) {
				msg += "*Object 맵핑정보  등록 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;이미 맵핑정보가 등록되었거나 엑셀에 잘못된 정보가 들어 있는지 확인하세요.\r\n<br/>";
			}
			//if ( !isSubjInfo ) {
			//	msg += "*과정 정보 수정 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;엑셀에 잘못된 정보가 들어 있는지 확인하세요.\r\n<br/>";
			//}

			result = isModuleInfo && isLessonInfo; // && isSubjInfo;
			
			if ( result ) { 
				connMgr.commit();
				msg = "OK";
			}
			else { connMgr.rollback(); }
		}
		catch (Exception ex)
		{
			connMgr.rollback();
			logger.error(ex);
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			// throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
			msg = "엑셀에 잘못된 정보가 들어 있는지 확인하세요.";
		}
		finally
		{
			connMgr.setAutoCommit(true);
			
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}

		return msg;
	}

	private Map getCAInfoMapsForOBC(RequestBox reqBox) throws Exception {
		Map maps = new HashMap();

		String subj = reqBox.getString("p_subj"); 
		String userid = reqBox.getSession("userid"); 
	    String v_newFileName = reqBox.getNewFileName("p_file");
	    
	    ConfigSet conf = new ConfigSet();
	    String fileName = conf.getProperty("dir.upload.caconfig") + v_newFileName; 
	    
	    Workbook workbook = Workbook.getWorkbook( new File(fileName) );
	    Sheet sheet = workbook.getSheet(0);
	    
	    Map moduleInfoMap = new HashMap();
	    Map lessonInfoMap = new HashMap();
	    Map objectInfoMap = new HashMap();
	    Map subjObjInfoMap = new HashMap();
	    Map subjInfoMap = new HashMap();

	    // 엑셀 Column 순서
	    String module = "";
	    String moduleName = "";
	    String lesson = "";
	    String lessonName = "";
	    String obj = "";
	    String objName = "";
	    String objPage = "";
	    String ordering = "";
	    String starting = "";
	    String width = "";
	    String height = "";
	    String sampleYn = "";
	    String eduTime = "";
	    String eduTimeYn = "";

	    for( int i=1; i<sheet.getRows(); i++ ) {
	    	module = getCellContents( sheet, 0, i );
	    	moduleName = getCellContents( sheet, 1, i );
	    	lesson = getCellContents( sheet, 2, i );
	    	lessonName = getCellContents( sheet, 3, i );
	    	obj = getCellContents( sheet, 4, i );
	    	objName = getCellContents( sheet, 5, i );
	    	objPage = getCellContents( sheet, 6, i );
	    	ordering = getCellContents( sheet, 7, i );
	    	starting = getCellContents( sheet, 8, i );
	    	//width = getCellContents( sheet, 9, i );
	    	//height = getCellContents( sheet, 10, i );
	    	//sampleYn = getCellContents( sheet, 11, i, "N" );
	    	//eduTime = getCellContents( sheet, 11, i, "0" );
	    	//eduTimeYn = getCellContents( sheet, 12, i, "N" );

	    	if ( !module.equals("") && !lesson.equals("") ) {
		    	if ( !moduleInfoMap.containsKey(module) ) {
		    		ModuleInfo moduleInfo = new ModuleInfo();
		    		moduleInfo.subj = subj;
		    		moduleInfo.module = module;
		    		moduleInfo.sdesc = moduleName;
		    		moduleInfo.luserid = userid;
		    		moduleInfoMap.put( module, moduleInfo );
		    	}
		    	
		    	if ( !lessonInfoMap.containsKey(module+lesson) ) {
		    		LessonInfo lessonInfo = new LessonInfo();
		    		lessonInfo.subj = subj;
		    		lessonInfo.module = module;
		    		lessonInfo.lesson = lesson;
		    		lessonInfo.sdesc = lessonName;
		    		//lessonInfo.eduTime = !eduTime.equals("") ? Integer.parseInt( eduTime ) : 0;
		    		//lessonInfo.eduTimeYn = eduTimeYn;
		    		lessonInfo.starting = starting;
		    		lessonInfo.luserid = userid;
		    		lessonInfoMap.put( module+lesson, lessonInfo );
		    	}

		    	if ( !objectInfoMap.containsKey(obj) ) {
		    		ObjectInfo objectInfo = new ObjectInfo();
		    		objectInfo.oid = obj;
		    		objectInfo.otype = "SC";
		    		objectInfo.filetype = "HTML";
		    		objectInfo.npage = !objPage.equals("") ? Integer.parseInt( objPage ) : 0;
		    		objectInfo.sdesc = objName;
		    		objectInfo.master = userid;
		    		objectInfo.starting = starting;
		    		objectInfo.server = "";
		    		objectInfo.guid = "";
		    		objectInfo.luserid = userid;
		    		objectInfoMap.put( obj, objectInfo );
		    	}
				
		    	if ( !subjObjInfoMap.containsKey(module+lesson) ) {
		    		SubjObjInfo subjObjInfo = new SubjObjInfo(); 
		    		subjObjInfo.subj = subj;
		    		subjObjInfo.type = "SC";
		    		subjObjInfo.module = module;
		    		subjObjInfo.lesson = lesson;
		    		subjObjInfo.oid = obj;
		    		subjObjInfo.ordering = !ordering.equals("") ? Integer.parseInt( ordering ) : 0;;
		    		subjObjInfo.sdesc = objName;
		    		subjObjInfo.types = "1001";
		    		subjObjInfo.luserid = userid;
		    		subjObjInfo.eduTime = !eduTime.equals("") ? Integer.parseInt( eduTime ) : 0;
		    		subjObjInfo.eduTimeYn = eduTimeYn;
		    		subjObjInfoMap.put( subj+"SC"+module+lesson+obj, subjObjInfo );
		    	}
	
		    	if ( !subjInfoMap.containsKey(subj) ) {
		    		SubjInfo subjInfo = new SubjInfo();
		    		subjInfo.subj = subj;
		    		subjInfo.width = width;
		    		subjInfo.height = height;
		    		subjInfoMap.put( subj, subjInfo );
		    	}
	    	}
	    }

	    maps.put( "moduleInfoMap", moduleInfoMap );
	    maps.put( "lessonInfoMap", lessonInfoMap );
	    maps.put( "subjObjInfoMap", subjObjInfoMap );
	    maps.put( "objectInfoMap", objectInfoMap );
	    maps.put( "subjInfoMap", subjInfoMap );
	    
		return maps;		
	}	
	
	/**
	 * 차시별 페이지정보 엑셀등록
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public String uploadExcelCALessonPage(RequestBox reqBox) throws Exception {

		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;

		String sql = "";
		String msg = "";
		boolean result = false;
		
		try
		{
			boolean isLessonPageInfo = false;
			//boolean isLessonInfo = false;
			//boolean isSubjInfo = false;
			
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			Map caInfoMaps = getCAInfoMaps(reqBox);
			
			isLessonPageInfo = insertLessonPageInfo( connMgr, (Map) caInfoMaps.get("LessonPageInfo") );
			//isLessonInfo = insertLessonInfo( connMgr, (Map) caInfoMaps.get("lessonInfoMap") );
			//isSubjInfo = updateSubjInfo( connMgr, (Map) caInfoMaps.get("subjInfoMap") );

			if ( !isLessonPageInfo ) {
				msg += "페이지 정보  등록 중 에러가 발생했습니다.\r\n<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;이미 페이지 정보가  등록되었거나 엑셀에 잘못된 정보가 들어 있는지 확인하세요.";
			}

			result = isLessonPageInfo; 
			
			if ( result ) {
				connMgr.commit();
				msg = "OK";
			}
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

		return msg;
	}
	
	/**
	 * 차시별 페이지정보 등록
	 * @param connMgr
	 * @param lessonInfoMap
	 * @return
	 */
	private boolean insertLessonPageInfo(DBConnectionManager connMgr, Map lessonPageInfoMap) {
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
	
		try
		{
			/*
			sql =
				"\n  INSERT INTO TZ_SUBJLESSON (  " +
				"\n     SUBJ, MODULE, LESSON, SDESC,  " +
				"\n     TYPES, OWNER, STARTING, ISBRANCH, LUSERID, LDATE  " +
				"\n      )  " +
				"\n  VALUES ( ?, TRIM(TO_CHAR(?, '00')), TRIM(TO_CHAR(?, '00')), ?,  " +
				"\n     '1001', ?, ?, 'N', ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')  " +
				"\n     )  ";
			*/
			sql =
				"\n  INSERT INTO TZ_SUBJLESSON_PAGE (  " +
				"\n     SUBJ, MODULE, LESSON, PAGENUM,  " +
				"\n     STARTING, LUSERID, LDATE " +
				"\n      )  " +
				"\n  VALUES ( ?, ?, TRIM(TO_CHAR(?, '00')), TRIM(?),  " +
				"\n           ?, ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')  " +
				"\n         )  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "차시별 페이지 엑셀 등록  \n " + sql );
			
			Iterator it = lessonPageInfoMap.keySet().iterator();
			while ( it.hasNext() ) {
				
				LessonPageInfo lessonPageInfo = (LessonPageInfo) lessonPageInfoMap.get(it.next());
				pstmt.clearParameters();
				pstmt.setString(1, lessonPageInfo.subj );
				pstmt.setInt   (2, 1);
				pstmt.setString(3, lessonPageInfo.lesson );
				pstmt.setInt   (4, Integer.parseInt(lessonPageInfo.pagenum) );
				pstmt.setString(5, lessonPageInfo.starting );
				pstmt.setString(6, lessonPageInfo.luserid );
				
				result += pstmt.executeUpdate();
			}
			pstmt.close();
		}
		catch (Exception ex)
		{
			logger.error(sql, ex);
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result == lessonPageInfoMap.size() ? true:false;
	}
}
