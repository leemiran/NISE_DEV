/*
 * @(#)MasterformConfigBean.java	1.0 2008. 11. 03
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FileMove;
import com.ziaan.library.FileUnzip;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * M/F관리 DAO Bean
 *
 * @version		1.0, 2008/11/03
 * @author		Chung Jin-pil
 */
public class MasterformConfigBean
{
	private Logger logger = Logger.getLogger(this.getClass());

	public MasterformConfigBean()
	{
	}

	/**
	 * M/F관리 상세
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public MasterFormData selectMasterform(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;

		String sql = "";
		MasterFormData data = null;

		try
		{
			connMgr = new DBConnectionManager();
			data = new MasterFormData();

			String p_subj = reqBox.getString("p_subj");
			
			sql =
				"\n  SELECT a.subj, subjnm,   " +
				"\n         NVL (iscentered, 'Y') iscentered,  " +
				"\n         NVL (mftype, 'ND') mftype,   " +
				"\n  	    NVL (width, '99999') width,  " +
				"\n         NVL (height, '99999') height,  " +
				"\n         NVL (otbgcolor, '#FFFFFF') otbgcolor,  " +
				"\n  	    NVL (contenttype, 'N') contenttype,  " +
				"\n  	    NVL (eduprocess, 'N') eduprocess,  " +
				"\n  	    NVL (ismfmenuimg, 'N') ismfmenuimg,  " +
				"\n  	    dir, isuse, mfdlist, mfgrdate,  " +
				"\n         server, port, eduurl, preurl,cp,  " +
				"\n         vodurl, isoutsourcing, content_cd, subj_gu, NVL (mfchat, 'N') mfchat,  " +
				"\n         b.cont_yn , b.reasons, b.cont_url_info " +
				"\n    FROM tz_subj a , tz_subjcontInfo b  " +
				"\n   WHERE a.subj = b.subj(+)  " +
				"\n     AND a.subj = " + StringManager.makeSQL(p_subj);
				
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > M/F관리 > Query  \n " + sql );
			
			if (ls.next())
			{
				data.setSubj(ls.getString("subj"));
				data.setSubjnm(ls.getString("subjnm"));
				data.setWidth(ls.getInt("width"));
				data.setHeight(ls.getInt("height"));
				data.setMftype(ls.getString("mftype"));
				data.setEduprocess(ls.getString("eduprocess"));
				data.setIscentered(ls.getString("iscentered"));
				data.setOtbgcolor(ls.getString("otbgcolor"));
				data.setContenttype(ls.getString("contenttype"));
				data.setIsMFMenuImg(ls.getString("ismfmenuimg"));
				
				data.setDir(ls.getString("dir"));
				data.setMfdlist(ls.getString("mfdlist"));
				data.setMfgrdate(ls.getString("mfgrdate"));
				data.setServer(ls.getString("server"));
				data.setPort(ls.getString("port"));
				data.setEduurl(ls.getString("eduurl"));
				data.setPreurl(ls.getString("preurl"));
				data.setVodurl(ls.getString("vodurl"));
				data.setOtbgcolor(ls.getString("otbgcolor"));
				data.setIsoutsourcing(ls.getString("isoutsourcing"));
				data.setContentCD(ls.getString("content_cd"));
				data.setSubjGubun(ls.getString("subj_gu"));
				data.setMFChat(ls.getString("mfchat"));
				data.setCp(ls.getString("cp"));
				data.setContYn(ls.getString("cont_yn"));
				data.setReasons(ls.getString("reasons"));
				data.setContUrlInfo(ls.getString("cont_url_info"));
				
				data.setIsmfbranch("N");
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

		return data;
	}

	/**
	 * M/F 관리 - 메뉴선택 (기본메뉴)
	 * 
	 * @return
	 * @throws Exception
	 */
	public List selectMFMenuList(String v_subj) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;

		String sql = "";
		List list = null;

		try
		{
			connMgr = new DBConnectionManager();
			list = new ArrayList();
		
			sql =
				"\n  SELECT   menu, menunm, isrequired, pgm, pgmtype, pgram1, pgram2, pgram3,  " +
				"\n           pgram4, pgram5, isuse, luserid, ldate  " +
				"\n      FROM tz_mfmenu  " +
				"\n     WHERE isuse = 'Y'  " +
				"\n       AND menu not in ( SELECT menu FROM TZ_MFSUBJ WHERE subj =  " + StringManager.makeSQL(v_subj) +" )  " +
				"\n  ORDER BY isrequired DESC, menu ASC  ";
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > M/F관리 > 기본메뉴 Query  \n " + sql );
			
			MfSubjData mfSubjData = null;
			while ( ls.next() ) 
			{
				mfSubjData = new MfSubjData();
				
				mfSubjData.setMenu( ls.getString("menu") );
				mfSubjData.setMenunm( ls.getString("menunm") );
				mfSubjData.setIsRequired( ls.getString("isrequired") );
				mfSubjData.setPgm( ls.getString("pgm") );
				mfSubjData.setPgmType( ls.getString("pgmtype") );
				mfSubjData.setPgram1( ls.getString("pgram1") );
				mfSubjData.setPgram2( ls.getString("pgram2") );
				mfSubjData.setPgram3( ls.getString("pgram3") );
				mfSubjData.setPgram4( ls.getString("pgram4") );
				mfSubjData.setPgram5( ls.getString("pgram5") );
				mfSubjData.setLuserid( ls.getString("luserid") );
				mfSubjData.setLdate( ls.getString("ldate") );

				list.add(mfSubjData);
			}
		}
		catch (Exception ex)
		{
			logger.error( sql, ex );
			ErrorManager.getErrorStackTrace(ex);
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
	 * M/F 관리 - 메뉴선택 (선택메뉴)
	 * 
	 * @return
	 * @throws Exception
	 */
	public List selectMFSubjList(String v_subj) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;

		String sql = "";
		List list = null;


		try
		{
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			
			sql =
				"\n  SELECT   b.menu, b.menunm, b.pgm, b.pgmtype, b.pgram1, b.pgram2, b.pgram3,  " +
				"\n           b.pgram4, b.pgram5, b.orders, b.luserid, b.ldate,  " +
				"\n  		  a.isrequired, a.isuse   " +
				"\n      FROM tz_mfmenu a, tz_mfsubj b  " +
				"\n  	WHERE a.isuse = 'Y'   " +
				"\n  	  AND a.menu = b.menu  " +
				"\n  	  AND b.subj = " + StringManager.makeSQL(v_subj) +
				"\n  ORDER BY b.orders ASC  ";

			ls = connMgr.executeQuery(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > M/F관리 > 선택메뉴 Query  \n " + sql );
			
			MfSubjData mfSubjData = null;
			while ( ls.next() ) 
			{
				mfSubjData = new MfSubjData();
				
				mfSubjData.setMenu( ls.getString("menu") );
				mfSubjData.setMenunm( ls.getString("menunm") );
				mfSubjData.setIsRequired( ls.getString("isrequired") );
				mfSubjData.setPgm( ls.getString("pgm") );
				mfSubjData.setPgmType( ls.getString("pgmtype") );
				mfSubjData.setPgram1( ls.getString("pgram1") );
				mfSubjData.setPgram2( ls.getString("pgram2") );
				mfSubjData.setPgram3( ls.getString("pgram3") );
				mfSubjData.setPgram4( ls.getString("pgram4") );
				mfSubjData.setPgram5( ls.getString("pgram5") );
				mfSubjData.setOrders( ls.getInt("orders") );
				mfSubjData.setLuserid( ls.getString("luserid") );
				mfSubjData.setLdate( ls.getString("ldate") );
				
				list.add(mfSubjData);
			}
		}
		catch (Exception ex)
		{
			logger.error( sql, ex );
			ErrorManager.getErrorStackTrace(ex);
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
	 * M/F 관리 수정
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public boolean updateMasterform(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		
		String sql = "";
		
		boolean result = false;
		
		boolean isUpdateMFData = false;
		boolean isUpdateMFMenuList = false;
		boolean isUploadMFMenuImage = false;
		boolean isContInfoDate = false;

		try
		{
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			// mfdata update
			deleteMFMenuList(connMgr,reqBox);
			isUpdateMFData = updateMFData(connMgr, reqBox);

			// menuList update
			isUpdateMFMenuList = updateMFMenuList(connMgr, reqBox);
			
			// menuImage update
			isUploadMFMenuImage = uploadMFMenuImage(reqBox);
			
			isContInfoDate = updateContInfo(connMgr, reqBox);
			
			logger.debug( "isUpdateMFData : " + isUpdateMFData 
					+ ", isUpdateMFMenuList : " + isUpdateMFMenuList 
					+ ", isUploadMFMenuImage : " + isUploadMFMenuImage 
					+ ", isUploadMFMenuImage : " + isUploadMFMenuImage );
			
			if ( isUpdateMFData && isUpdateMFMenuList && isUploadMFMenuImage )
			{
				result = true;
			}
			else 
			{
				result = false;
			}
			
			if ( result ) {	connMgr.commit(); }
			else {	connMgr.rollback();	}
		}
		catch (Exception ex)
		{
			connMgr.rollback();
			logger.error( sql + "\n" + reqBox, ex );
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
	 * 이미지 등록
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean uploadMFMenuImage(RequestBox reqBox) throws Exception
	{
		String p_subj = reqBox.getString("p_subj");
		boolean isMfMenuImg = reqBox.getString("p_ismfmenuimg").equals("Y")?true:false;

		boolean result = false;
		String errorMsg = "";
		
		String v_realFileName = reqBox.getRealFileName("p_file");
		String v_newFileName = reqBox.getNewFileName("p_file");

		if ( isMfMenuImg && !v_newFileName.equals(""))
		{
			ConfigSet conf = new ConfigSet();
			
			String v_realPath = conf.getProperty("dir.content.upload")
								+ p_subj + File.separator + "docs" + File.separator + "menuimg"; // 실제 Un-zip될 Dir
			String v_tempPath = conf.getProperty("dir.upload.default"); // upload된 파일 위치

			try
			{
				logger.debug("# M/F MENU IMG FILE UPLOAD START");
				boolean isFileMove = false;
				boolean isExtract = false;
				
				logger.debug(" realPath : " + v_realPath );
				logger.debug(" tempPath : " + v_tempPath );
				// 1. 디렉토리 체크 , 없으면 생성한다.
				File dir = new File(v_realPath);
				if (!dir.exists())
				{
					dir.mkdirs();
				}

				// 2. 파일 이동
				FileMove fc = new FileMove();
				isFileMove = fc.move(v_realPath, v_tempPath, v_newFileName);
				
				logger.debug(" isFileMove : " + isFileMove );
				
				// 3. 압축 풀기
				if (isFileMove)
				{
					FileUnzip unzip = new FileUnzip();
					isExtract = unzip.extract(v_realPath, v_newFileName);

					if (!isExtract)
					{
						errorMsg = "압축 푸는 도중 오류 발생";
					}
					else
					{
						result = true;
					}
				}
				else
				{
					errorMsg = "업로드 파일 이동 중 오류 발생";
				}
				logger.debug(" errorMsg : " + errorMsg );
				logger.debug("# M/F MENU IMG FILE UPLOAD END");
			}
			catch (Exception ex)
			{
				throw new Exception("ERROR results=" + errorMsg + "\r\n" + ex.getMessage());
			}
			finally
			{
				FileManager.deleteFile(v_realPath + "\\" + v_newFileName);
			}
		} else {
			result = true;
		}
		
		if ( !result ) {
			logger.error("LCMS > 컨텐츠 관리 > M/F관리 > 메뉴이미지 업로드 중 에러 발생 \n" + errorMsg );
		}

		return result;
	}

	/**
	 * M/F 관리 - 기본정보 수정
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean updateMFData(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;

		try
		{
			String p_subj = reqBox.getString("p_subj");
			String p_mftype = reqBox.getString("p_mftype");
			int p_width = reqBox.getInt("p_width");
			int p_height = reqBox.getInt("p_height");
			String p_eduprocess = reqBox.getString("p_eduprocess");
			String p_iscentered = reqBox.getString("p_iscentered");
			String p_otbgcolor = reqBox.getString("p_otbgcolor");
			String p_ismfmenuimg = reqBox.getString("p_ismfmenuimg");
			String p_mfchat = reqBox.getString("p_mfchat");
			String s_userid = reqBox.getSession("userid");
			
			// update materform info
			sql =
				"\n  UPDATE tz_subj  " +
				"\n     SET mftype = ?,  " +
				"\n         iscentered = ?,  " +
				"\n         width = ?,  " +
				"\n         height = ?,  " +
				"\n         otbgcolor = ?,  " +
				"\n         eduprocess = ?,  " +
				"\n         ismfmenuimg = ?,  " +
				"\n         mfchat = ?,  " +
				"\n         luserid = ?,  " +
				"\n         ldate = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  " +
				"\n   WHERE subj = ?  ";
			
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > M/F 관리 > M/F정보저장  \n " + sql );

			pstmt.setString(1, p_mftype);
			pstmt.setString(2, p_iscentered);
			pstmt.setInt(3, p_width);
			pstmt.setInt(4, p_height);
			pstmt.setString(5, p_otbgcolor);
			pstmt.setString(6, p_eduprocess);
			pstmt.setString(7, p_ismfmenuimg);
			pstmt.setString(8, p_mfchat);
			pstmt.setString(9, s_userid);
			pstmt.setString(10, p_subj);
			
			result = pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			logger.error( sql + "\n" + reqBox, ex );
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}

		return result > 0 ? true:false;
	}

	/**
	 * 이어보기 관련정보 수정
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	private boolean updateContInfo(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;
		
		try
		{
			String p_subj = reqBox.getString("p_subj");
			String p_cont_yn = reqBox.getString("p_cont_yn");
			String p_cont_url_info = reqBox.getString("p_cont_url_info");
			String p_reasons = reqBox.getString("p_reasons");

			// update materform info
			sql =
				"\n  UPDATE tz_subjcontinfo  " +
				"\n     SET cont_yn = ?,  " +
				"\n         cont_url_info = ?,  " +
				"\n         reasons = ?,  " +
				"\n         ldate = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  " +
				"\n   WHERE subj = ?  ";
			
			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > M/F 관리 > M/F정보저장  \n " + sql );
			
			pstmt.setString(1, p_cont_yn);
			pstmt.setString(2, p_cont_url_info);
			pstmt.setString(3, p_reasons);
			pstmt.setString(4, p_subj);
			
			result = pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			logger.error( sql + "\n" + reqBox, ex );
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
		
		return result > 0 ? true:false;
	}
	
	/**
	 * M/F 관리 - 메뉴선택 삭제
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @throws Exception
	 */
	private boolean deleteMFMenuList(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql = "";
		int result = 0;

		try
		{
			String p_subj = reqBox.getString("p_subj");
			
			sql =
				"\n  DELETE FROM tz_mfsubj WHERE subj = ? and menu not in ('80', '81') ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "# TZ_MFSUBJ 삭제 " + sql );
			
			pstmt.setString( 1, p_subj);
			
			result = pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
	
		return result > 0 ? true : false;			
	}
	
	/**
	 * M/F 관리 - 메뉴선택 저장
	 * 
	 * @param connMgr
	 * @param reqBox
	 * @throws Exception
	 */
	private boolean updateMFMenuList(DBConnectionManager connMgr, RequestBox reqBox) throws Exception
	{
		PreparedStatement pstmt = null;

		String sql = "";
		boolean result = false;

		try
		{
			String p_subj = reqBox.getString("p_subj");
			String s_userid = reqBox.getSession("userid");

			Vector applyMenuList = reqBox.getVector("applyMenuList");
			
			sql =
				"\n  INSERT INTO tz_mfsubj  " +
				"\n     SELECT ? as subj, menu, menunm, pgm, pgram1, pgram2, pgram3, pgram4, pgram5, ? as orders,  " +
				"\n            pgmtype, ? as luserid, TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')  " +
				"\n       FROM tz_mfmenu  " +
				"\n      WHERE menu = ?  ";

			pstmt = connMgr.prepareStatement(sql);
			logger.debug( "LCMS > 컨텐츠 관리 > M/F 관리 > 메뉴선택저장  \n " + sql );
			
			String menu = "";
			if ( applyMenuList == null || applyMenuList.size() == 0 ) {
				result = true;
			} else {
				for ( int i=0; i<applyMenuList.size(); i++ ) 
				{
					menu = (String) applyMenuList.get(i);
					
					if(menu.equals("80") || menu.equals("81")){
						continue;
					}
					
					pstmt.setString(1, p_subj);
					pstmt.setInt(2, i+1);
					pstmt.setString(3, s_userid );
					pstmt.setString(4, menu );
					
					int count = pstmt.executeUpdate();
					boolean isFailedAtLeastOnce = false;
					
					if ( count > 0 && !isFailedAtLeastOnce )
					{
						result = true;
					}
					else
					{
						isFailedAtLeastOnce = true;
						result = false;
					}
				}
			}
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, reqBox, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}

		return result;			
	}
}
