/*
 * @(#)SCObjectBean.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.lcms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileDelete;
import com.ziaan.library.FileManager;
import com.ziaan.library.FileMove;
import com.ziaan.library.FileUnzip;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * OBC Object 관리 Bean
 * 
 * @author Jin-pil Chung
 * @version 1.0, 2006/09/21
 */
public class SCObjectBean
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static final String FILE_TYPE = "p_file";   // 파일 업로드되는 tag name
    private static final int FILE_LIMIT = 1;            // 페이지에 세팅된 파일첨부 갯수

    /**
     * Object 리스트 조회
     * 
     * @param box receive from the form object and session
     * @return ArrayList Object리스트
     */
    public List SelectObjectList(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        List list1 = null;
        String sql = "";
        
        SCObjectData data = null;

        String s_subj = box.getString("s_subj");
        String s_objnm = box.getString("s_objnm");

		String p_orderColumn = box.getStringDefault("p_orderColumn", "oid");
		String p_orderType = box.getString("p_orderType");
        
        try
        {
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            if ( s_subj.equals("NO-SUBJ") ) {
            	sql =
            		"\n  SELECT a.OID, a.otype, a.filetype, a.npage, a.sdesc, a.MASTER,  " +
            		"\n         a.MASTER mastername, a.starting, a.server, a.subj, a.parameterstring,  " +
            		"\n         a.datafromlms, a.IDENTIFIER, a.prerequisites, a.masteryscore,  " +
            		"\n         a.maxtimeallowed, a.timelimitaction, a.SEQUENCE, a.thelevel, a.luserid, a.ldate,  " +
            		"\n         0 as cntused  " +
            		"\n    FROM tz_object a  " +
            		"\n   WHERE a.OID NOT IN (SELECT a.OID FROM tz_subjobj b WHERE a.OID = b.OID)  ";
            } else {
            	sql = 
            		"\n  SELECT a.OID, a.otype, a.filetype, a.npage, a.sdesc, a.MASTER,  " +
            		"\n         a.MASTER mastername, a.starting, a.server, a.subj, a.parameterstring,  " +
            		"\n         a.datafromlms, a.IDENTIFIER, a.prerequisites, a.masteryscore,  " +
            		"\n         a.maxtimeallowed, a.timelimitaction, a.SEQUENCE, a.thelevel, a.luserid, a.ldate,   " +
            		"\n  	   (SELECT COUNT (*) FROM tz_subjobj WHERE OID = a.OID) AS cntused  " +
            		"\n    FROM tz_object a, tz_subjobj b  " +
            		"\n   WHERE a.OID = b.OID AND b.subj = " + StringManager.makeSQL(s_subj);
            }
            
        	if ( !s_objnm.equals("") ) {
        		sql += "\n         and (a.oid like " + StringManager.makeSQL("%"+s_objnm+"%") +
        		       "\n              or a.sdesc like "+ StringManager.makeSQL("%"+s_objnm+"%") + ")  ";
        	}

        	sql += "\n   ORDER BY  " + p_orderColumn + " " + p_orderType;            

            ls = connMgr.executeQuery(sql);
            logger.debug("Object관리 목록 : \n" + sql);

            while (ls.next())
            {
                data = new SCObjectData();
                data.setOid(ls.getString("oid"));
                data.setOtype(ls.getString("otype"));
                data.setFiletype(ls.getString("filetype"));
                data.setNpage(ls.getInt("npage"));
                data.setSdesc(ls.getString("sdesc"));
                data.setMaster(ls.getString("master"));
                data.setMastername(ls.getString("mastername"));
                data.setStarting(ls.getString("starting"));
                data.setServer(ls.getString("server"));
                data.setSubj(ls.getString("subj"));
                data.setParameterstring(ls.getString("parameterstring"));
                data.setdatafromlms(ls.getString("datafromlms"));
                data.setIdentifier(ls.getString("identifier"));
                data.setPrerequisites(ls.getString("prerequisites"));
                data.setMasteryscore(ls.getInt("masteryscore"));
                data.setMaxtimeallowed(ls.getString("maxtimeallowed"));
                data.setTimelimitaction(ls.getString("timelimitaction"));
                data.setSequence(ls.getInt("sequence"));
                data.setThelevel(ls.getInt("thelevel"));
                data.setLuserid(ls.getString("luserid"));
                data.setLdate(ls.getString("ldate"));
                data.setCntUsed(ls.getInt("cntused"));

                list1.add(data);
            }
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally
        {
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }
        
        return list1;
    }

    /**
     * Object정보 조회
     * 
     * @param box
     *            receive from the form object and session
     * @return SCObjectData Object 정보
     */
    public SCObjectData SelectObjectData(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null, ls2 = null;
        
        String sql = "";
        SCObjectData data = null;

        String p_oid = box.getString("p_oid");

        try
        {
            connMgr = new DBConnectionManager();

            // Object가 사용된 과목리스트 저장
            sql =
            	"\n  SELECT OID, otype, filetype, npage, sdesc, MASTER, starting, server, subj,  " +
            	"\n         parameterstring, datafromlms, IDENTIFIER, prerequisites, masteryscore,  " +
            	"\n         maxtimeallowed, timelimitaction, SEQUENCE, thelevel, luserid, ldate  " +
            	"\n    FROM tz_object  " +
            	"\n   WHERE OID = " + StringManager.makeSQL(p_oid);

            ls = connMgr.executeQuery(sql);

            if (ls.next())
            {
                data = new SCObjectData();
                data.setOid(ls.getString("oid"));
                data.setOtype(ls.getString("otype"));
                data.setFiletype(ls.getString("filetype"));
                data.setNpage(ls.getInt("npage"));
                data.setSdesc(ls.getString("sdesc"));
                data.setMaster(ls.getString("master"));
                data.setStarting(ls.getString("starting"));
                data.setServer(ls.getString("server"));
                data.setSubj(ls.getString("subj"));
                data.setParameterstring(ls.getString("parameterstring"));
                data.setdatafromlms(ls.getString("datafromlms"));
                data.setIdentifier(ls.getString("identifier"));
                data.setPrerequisites(ls.getString("prerequisites"));
                data.setMasteryscore(ls.getInt("masteryscore"));
                data.setMaxtimeallowed(ls.getString("maxtimeallowed"));
                data.setTimelimitaction(ls.getString("timelimitaction"));
                data.setSequence(ls.getInt("sequence"));
                data.setThelevel(ls.getInt("thelevel"));
                data.setLuserid(ls.getString("luserid"));
                data.setLdate(ls.getString("ldate"));

                if (ls2 != null) { try { ls2.close(); } catch (Exception e) {} }
                
                // 사용된 과목갯수
                sql =
                	"\n  SELECT DISTINCT a.subj, b.subjnm subjnm  " +
                	"\n             FROM tz_subjobj a, tz_subj b  " +
                	"\n            WHERE a.subj = b.subj AND a.OID = " + StringManager.makeSQL(p_oid);

                ls2 = connMgr.executeQuery(sql);
                while (ls2.next())
                {
                    data.makeSub(ls2.getString("subj"), ls2.getString("subjnm"));
                }

                data.setCntUsed(ls2.getTotalCount());
                if (ls2 != null) { try { ls2.close(); } catch (Exception e) {} }
                
                
                // 등록자 이름
                sql =
                	"\n  SELECT NAME FROM tz_member WHERE userid = " + StringManager.makeSQL(data.getMaster());
                ls2 = connMgr.executeQuery(sql);
                if (ls2.next()) { data.setMastername(ls2.getString("name")); }
            }
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally
        {
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
            if (ls2 != null) { try { ls2.close(); } catch (Exception e) {} }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }
        return data;
    }

    /**
     * Object등록
     * 
     * @param box
     *            receive from the form object and session
     * @return isOk 1:update success,0:update fail
     */
    public String InsertObject(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        int isOk = 0, j = 0;

        String v_luserid = box.getSession("userid");
        String v_oid = box.getString("p_oid");
        String results = "";

        try
        {
            connMgr = new DBConnectionManager();

            // get Object-ID
            // ms-sql : sql = "select cast(dbo.to_number(isnull(max(oid),'1000000000')) +1 as varchar(10)) oid from tz_object";
            // sql = "select ltrim(to_char( to_number(nvl(max(oid),'1000000000')) +1, '0000000000'  )) oid from tz_object";

            // FILE MOVE & UNZIP
            results = this.controlObjectFile("insert", v_oid, box);

            if (results.equals("OK"))
            {
                ConfigSet conf = new ConfigSet();
                
                sql = "insert into tz_object " + "(oid , otype, filetype, npage, sdesc,master "
                        + " ,starting,server,subj,parameterstring,datafromlms "
                        + " ,identifier,prerequisites,masteryscore,maxtimeallowed "
                        + " ,timelimitaction,sequence,thelevel,luserid,ldate)" + " values " + "("
                        + StringManager.makeSQL(v_oid) + "," 
                        + StringManager.makeSQL("SC") + ","
                        + StringManager.makeSQL("HTML") + "," 
                        + box.getInt("p_npage") + ","
                        + StringManager.makeSQL(box.getString("p_sdesc")) + ","
                        + StringManager.makeSQL(v_luserid) + "," 
                        + StringManager.makeSQL( conf.getProperty("dir.object.path") + v_oid + "/1001.html") + "," 
                        + StringManager.makeSQL(box.getString("p_server")) + ","
                        + StringManager.makeSQL(box.getString("p_subj")) + ","
                        + StringManager.makeSQL(box.getString("p_parameterstring")) + ","
                        + StringManager.makeSQL(box.getString("p_datafromlms")) + ","
                        + StringManager.makeSQL(box.getString("p_identifier")) + ","
                        + StringManager.makeSQL(box.getString("p_prerequisites")) + "," 
                        + box.getInt("p_masteryscore") + "," 
                        + StringManager.makeSQL(box.getString("p_maxtimeallowed")) + ","
                        + StringManager.makeSQL(box.getString("p_timelimitaction")) + "," 
                        + box.getInt("p_sequence") + "," 
                        + box.getInt("p_thelevel") + "," 
                        + StringManager.makeSQL(v_luserid)
                        + ", to_char(sysdate,'YYYYMMDDHH24MISS') " + ")";

                isOk = connMgr.executeUpdate(sql);
            }

        }
        catch (Exception ex)
        {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("results = " + results + "\r\n" + "sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally
        {
            if (isOk > 0) { connMgr.commit(); }
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }
        return results;
    }

    /**
     * Object수정
     * 
     * @param box
     *            receive from the form object and session
     * @return isOk 1:update success,0:update fail
     */
    public String UpdateObject(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;

        String sql = "";
        int isOk = 0, j = 0;

        String results = "";
        String v_luserid = box.getSession("userid");
        String v_oid = box.getString("p_oid");

        try
        {
            // FILE MOVE & UNZIP
            results = this.controlObjectFile("update", v_oid, box);
        }
        catch (Exception ex)
        {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("results = " + results + "\r\n" + ex.getMessage());
        }

        try
        {
            connMgr = new DBConnectionManager();
            // if ( results.equals("OK") ) {

            // update Object-Information 'TZ_OBJECT'
            sql = "update tz_object set "
            		// + " otype = " + StringManager.makeSQL(box.getString("p_otype"))
                    // + " ,filetype = " +
                    // StringManager.makeSQL(box.getString("p_filetype"))
                    + "   npage = " + box.getInt("p_npage") 
                    + "  ,sdesc = " + StringManager.makeSQL(box.getString("p_sdesc")) 
                    + "  ,master = " + StringManager.makeSQL(v_luserid)
                    // + " ,starting = " + StringManager.makeSQL("http:// "
                    // +GetCodenm.get_config("eduDomain")
                    // +GetCodenm.get_config("object_locate") +v_oid +
                    // "/1001.html")
                    + "  ,server             = " + StringManager.makeSQL(box.getString("p_server"))
                    + "  ,subj               = " + StringManager.makeSQL(box.getString("p_subj"))
                    + "  ,parameterstring    = " + StringManager.makeSQL(box.getString("p_parameterstring"))
                    + "  ,datafromlms        = " + StringManager.makeSQL(box.getString("p_datafromlms"))
                    + "  ,identifier         = " + StringManager.makeSQL(box.getString("p_identifier"))
                    + "  ,prerequisites      = " + StringManager.makeSQL(box.getString("p_prerequisites"))
                    + "  ,masteryscore       = " + box.getInt("p_masteryscore") 
                    + "  ,maxtimeallowed     = " + StringManager.makeSQL(box.getString("p_maxtimeallowed")) 
                    + "  ,timelimitaction    = " + StringManager.makeSQL(box.getString("p_timelimitaction")) 
                    + "  ,sequence           = " + box.getInt("p_sequence") + "     ,thelevel           = " + box.getInt("p_thelevel")
                    + "  ,luserid            = " + StringManager.makeSQL(v_luserid)
                    + "  ,ldate              = to_char(sysdate,'YYYYMMDDHH24MISS') " 
                    + " where oid=" + StringManager.makeSQL(v_oid);
            
            isOk = connMgr.executeUpdate(sql);
            // }
        }
        catch (Exception ex)
        {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally
        {
            connMgr.setAutoCommit(true);
            if (isOk > 0) { connMgr.commit(); }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }
        return results;
    }

    /**
     * Object 삭제
     * 
     * @param box
     *            receive from the form object and session
     * @return isOk 1:update success,0:update fail
     */
    public String DeleteObject(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String result = "";
        String sql = "";
        String p_oid = box.getString("p_oid");

        try
        {
            connMgr = new DBConnectionManager();

            // 1. Mapping 정보 있는지 Check
            sql = 
                "\n  SELECT COUNT(*) cnt        " +
                "\n  FROM tz_subjobj            " +
                "\n  WHERE OID = " + StringManager.makeSQL( p_oid );
            
            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() )
            {
                int mappingCount = ls.getInt("cnt");
                
                if ( mappingCount > 0 )
                {
                    result = "연결된 과정정보가 존재합니다. 연결을 해제하신 후 삭제해주세요.";
                    return result;
                }
            }
            
            // 2. 진도 정보 있는지 Check
            sql = 
                "\n  SELECT COUNT(*) cnt        " +
                "\n  FROM tz_progress           " +
                "\n  WHERE OID = " + StringManager.makeSQL( p_oid );
            
            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() )
            {
                int progressCount = ls.getInt("cnt");
                
                if ( progressCount > 0 )
                {
                    result = "진도정보가 남아 있습니다.";
                    return result;
                }
            }

            // 3. 아니면 DB 삭제
            sql = 
                "\n  DELETE FROM tz_object     " +
                "\n  WHERE OID = " + StringManager.makeSQL( p_oid );
            
            int isOk = connMgr.executeUpdate( sql );
            
            // 4. 물리적 파일 삭제
            if ( isOk == 1 )
            {
                ConfigSet conf = new ConfigSet();
                String v_realPath = conf.getProperty("dir.object.upload") + p_oid; // 실제 Un-zip될 Dir

                FileDelete fd = new FileDelete();
                boolean allDelete_success = fd.allDelete(v_realPath);

                if ( allDelete_success && new File( v_realPath ).delete() )
                {
                    result = "OK";
                }
                else
                {
                    result = "물리적 파일 삭제중 오류가 발생했습니다.";
                }
            }
        }
        catch (Exception ex)
        {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally
        {
            connMgr.setAutoCommit(true);
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return result;
    }

    /**
     * OBC 과목선택 List만들기
     * 
     * @param String
     * @return String
     */
    public static String makeObcSubjSelect(RequestBox box, String p_selsubj, String p_onchange) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String sql = "";

        StringBuffer sb = new StringBuffer();
        sb.append( "<select name=\"s_subj\" " );

        if (!p_onchange.equals("")) {
        	sb.append(" onChange=\"" + p_onchange + "\" ");
        }
        
        sb.append( " > " );
        
        try
        {
            connMgr = new DBConnectionManager();

            String s_upperclass = box.getStringDefault("s_upperclass", "ALL");
            String s_middleclass = box.getStringDefault("s_middleclass", "ALL");
            String s_lowerclass = box.getStringDefault("s_lowerclass", "ALL");
            
            sql =
            	"\n  SELECT subj, subjnm  " +
            	"\n    FROM tz_subj  " +
            	"\n   WHERE 1 = 1  " +
            	"\n     AND (contenttype = 'O' OR contenttype = 'OA')  ";

            if ( !s_upperclass.equals("ALL")  ) {
            	sql += "\n     AND upperclass = " + StringManager.makeSQL(s_upperclass);
            }
            if ( !s_middleclass.equals("ALL")  ) {
            	sql += "\n     AND middleclass = " + StringManager.makeSQL(s_middleclass);
            }
            if ( !s_lowerclass.equals("ALL")  ) {
            	sql += "\n     AND lowerclass = " + StringManager.makeSQL(s_lowerclass);
            }

            ls = connMgr.executeQuery(sql);

            sb.append( "<option value=\"\">== 과정명을 선택하세요. ==</option>");
            if ( s_upperclass.equals("ALL") ) {
            	sb.append( "<option value=\"NO-SUBJ\" " );
            	
            	if ( p_selsubj.equals("NO-SUBJ") ) {
            		sb.append( " selected" );
            	}
            	
            	sb.append( ">== 과정 미지정 Object ==</option>\n" );
            }
            
            while (ls.next())
            {
                sb.append( "<option value='" + ls.getString("subj") + "' " );
                if ( p_selsubj.equals(ls.getString("subj")) ) {
                	sb.append( " selected" );
                }
                sb.append( ">" + ls.getString("subjnm") + "</option>\n" );
            }
        }
        catch (Exception ex)
        {
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally
        {
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }
        sb.append( "</select>" );

        return sb.toString();
    }

    /**
     * Object ZIP 파일로 Directory 구성
     * 
     * @param String
     *            p_job 등록/수정구분(insert/update) p_oid Object ID box RequestBox
     * @return String resuts 결과메세지
     */
    public String controlObjectFile(String p_job, String p_oid, RequestBox box) throws Exception
    {
        String results = "OK";
        String sql = "";
        int isOk = 1;

        String v_realPath = "";
        String v_tempPath = "";
        boolean insert_success = false;
        boolean move_success = false;
        boolean update_success = false;
        boolean extract_success = false;
        boolean allDelete_success = false;

        /*
         * 업로드되는 파일의 형식을 알고 코딩해야한다
         * String v_realFileName = box.getRealFileName("p_file");
         * 원 파일명 String v_newFileName = box.getNewFileName("p_file"); // 실제 upload된 파일명 //
         */
        String[] v_realFileName = new String[FILE_LIMIT];
        String[] v_newFileName = new String[FILE_LIMIT];
        String v_file1 = "";

        for (int i = 0; i < FILE_LIMIT; i++)
        {
            v_realFileName[i] = box.getRealFileName(FILE_TYPE + (i + 1));
            v_newFileName[i] = box.getNewFileName(FILE_TYPE + (i + 1));
        }

        if (!v_newFileName[0].equals(""))
        {
            v_file1 = v_newFileName[0];
        }

        // 첨부파일이 있을 경우에만 실행.
        if (!v_file1.equals(""))
        {
            // Object 폴더결정
            ConfigSet conf = new ConfigSet();

            // Un-zip될 Dir
            v_realPath = conf.getProperty("dir.object.upload") + p_oid; // 실제 Un-zip될 Dir
            v_tempPath = conf.getProperty("dir.upload.scobject");        // upload된 파일 위치
            
            results = p_job;
            results += "\\n\\n 0. v_realPath=" + v_realPath;
            results += "\\n\\n 0. v_tempPath=" + v_tempPath;

            try
            {
                if (p_job.equals("insert"))         // Object 등록시
                { 
                    // 1. 폴더 생성
                    File newDir = new File(v_realPath);
                    newDir.mkdir();
                    allDelete_success = true;
                    results += "\\n\\n 1. makeDirecotry OK. ";
                }
                else if (p_job.equals("update"))    // Object 수정시
                { 
                    // 1. 수정할 파일및 폴더 모두 삭제
                    FileDelete fd = new FileDelete();
                    allDelete_success = fd.allDelete(v_realPath);
                    results += "\\n\\n 1. allDelete =  " + (new Boolean(allDelete_success).toString());
                    // 1. 폴더 생성
                    File newDir = new File(v_realPath);
                    newDir.mkdirs();
                    allDelete_success = true;
                    results += "\\n\\n 1. makeDirecotry OK. ";
                }

                // 2. 파일 이동
                if (allDelete_success)
                {
                    FileMove fc = new FileMove();
                    move_success = fc.move(v_realPath, v_tempPath, v_file1);
                }
                results += "\\n\\n 2. move to [" + v_realPath + "] =  " + (new Boolean(move_success).toString());

                // 3. 압축 풀기
                if (move_success)
                {
                    FileUnzip unzip = new FileUnzip();
                    extract_success = unzip.extract(v_realPath, v_file1);
                    results += "\\n\\n 3. unzip to [" + v_realPath + "] =  "
                            + (new Boolean(extract_success).toString());
                    if (!extract_success)
                    {
                        FileDelete fd = new FileDelete();
                        fd.allDelete(v_realPath);
                    }
                }
                results += "\\n\\n END of controlObjectFile() ";
                System.out.println( results );
                results = "OK";
            }
            catch (Exception ex)
            {
                FileDelete fd = new FileDelete();
                fd.allDelete(v_realPath);
                throw new Exception("ERROR results=" + results + "\r\n" + ex.getMessage());
            }
            finally
            {
                FileManager.deleteFile(v_realPath + "\\" + v_file1);
            }
        }

        return results;
    }

	/**
	 * 다음 Oid 값
	 * 
	 * @param reqBox
	 * @return
	 * @throws Exception
	 */
	public String selectNextOid(RequestBox reqBox) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		
		String sql = "";
		String nextOid = "";
		
		try
		{
			connMgr = new DBConnectionManager();

			sql =
				"\n  SELECT TRIM(TO_CHAR (TO_NUMBER (NVL(MAX(OID), '5000000000')) + 1, '0000000000' ) ) next_oid  " +
				"\n    FROM tz_object  ";
			
			ls = connMgr.executeQuery(sql);
			logger.debug( "다음 Oid 값" + sql );
			
			if ( ls.next() ) 
			{
				nextOid = ls.getString("next_oid");
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
		
		return nextOid;		
	}
}
