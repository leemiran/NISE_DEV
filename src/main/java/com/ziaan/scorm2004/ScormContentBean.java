/*
 * @(#)ScormContentBean.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ziaan.library.FileDelete;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DataBox;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * 컨텐츠 등록 관리 Bean Class
 *
 * @version 1.0 2006. 4. 19.
 * @author Jin-pil Chung
 *
 */
public class ScormContentBean
{
    /**
     * 컨텐츠 등록 관리 - 리스트
     * 
     * 등록된 컨텐츠 리스트를 반환한다.
     * 
     * @param box
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList selectContentList(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        ArrayList list = null;
        String sql = "";
        
        try 
        {
            if ( box.getString("s_action").equals("go") )
            {
                connMgr = new DBConnectionManager();
                list = new ArrayList();

                /*
                 *  베타테스트 필요 변수
                String s_gadmin = box.getSession( "gadmin" );
                String s_userid = box.getSession( "userid" );
                String v_testGubun = box.getString("p_testgubun");
                */
                
                String v_orderColumn = box.getString("p_orderColumn");
                String v_orderType = box.getString("p_orderType");

                String ss_upperclass = box.getStringDefault("s_upperclass", "ALL");
                String ss_middleclass = box.getStringDefault("s_middleclass", "ALL");
                String ss_lowerclass = box.getStringDefault("s_lowerclass", "ALL");
                
                String ss_courseTypeCode = box.getStringDefault("s_course_type", "ALL");
                String ss_courseNm = box.getString("s_course_nm");
    
                sql =
                    "\n  SELECT                                 " +
                    "\n     COURSE_CODE, COURSE_TYPE,           " +
                    "\n     COURSE_NM, IS_SEQUENCING, DIST_CODE, " +
                    "\n     (SELECT CODENM FROM TZ_CODE WHERE GUBUN='0098' AND CODE=A.COURSE_TYPE ) COURSE_TYPE_NM " +
                    "\n  FROM                                   " +
                    "\n     TYS_COURSE A                        " +
                    "\n  WHERE 1=1                              " +
                    "       :addWhere                           ";
                
                StringBuffer addWhere = new StringBuffer();
                
                // 검색 조건
                if ( !ss_upperclass.equals("ALL") )
                {
                    addWhere.append( "\n  AND SUBSTR(DIST_CODE, 0, 3) = " );
                    addWhere.append( StringManager.makeSQL(ss_upperclass) );
                }
                
                if ( !ss_middleclass.equals("ALL") )
                {
                	addWhere.append( "\n  AND SUBSTR(DIST_CODE, 4, 3) = " );
                	addWhere.append( StringManager.makeSQL(ss_middleclass) );
                }

                if ( !ss_lowerclass.equals("ALL") )
                {
                	addWhere.append( "\n  AND SUBSTR(DIST_CODE, 7, 3) = " );
                	addWhere.append( StringManager.makeSQL(ss_middleclass) );
                }
                
                
                if ( !ss_courseTypeCode.equals("ALL") )
                {
                    addWhere.append( "\n  AND COURSE_TYPE=");
                    addWhere.append( SQLString.Format(ss_courseTypeCode) );
                }
                
                if ( !ss_courseNm.equals("") )
                {
                    addWhere.append( "\n  AND UPPER(COURSE_NM) LIKE '%" );
                    addWhere.append( ss_courseNm.toUpperCase() );
                    addWhere.append( "%'" );
                }
                
                /*
                 * 베타 테스터 업체인지, (KT 사용안함)
                if ( (s_gadmin.equals("S1") || s_gadmin.equals("T1") || s_gadmin.equals("M1")) && v_testGubun.equals("beta") ) 
                {
                    String betaProducer = "";
                    String betaSql =
                                    "\n  SELECT cpseq, cpnm         " +
                                    "\n  FROM tz_cpinfo             " +
                                    "\n  WHERE userid = ':userid'   " +
                                    "\n  ORDER BY cpnm              ";
                    
                    betaSql = betaSql.replaceAll( ":userid", s_userid );
                    ls = connMgr.executeQuery( betaSql );
                    
                    if ( ls.next() )
                    {
                        betaProducer = ls.getString("cpseq");
                    }
                    ls.close();
                    
                    addWhere.append( "\n  AND isbeta = 'Y' and producer = '" );
                    addWhere.append( betaProducer );
                    addWhere.append( "'" );
                    
                }
                else if ( v_testGubun.equals("beta") )
                {
                    addWhere.append( "\n  AND isbeta = 'Y' ");
                }
                */
                
                if( !v_orderColumn.equals("") )
                {
                    addWhere.append( "\n  ORDER BY " );
                    addWhere.append( v_orderColumn );
                    addWhere.append( v_orderType );
                }
                else
                {
                    addWhere.append( "\n  ORDER BY " );
                    addWhere.append( "\n     COURSE_CODE desc ");
                }

                sql = sql.replaceAll( ":addWhere", addWhere.toString() );
                ls = connMgr.executeQuery( sql );
                
                System.out.println( "SCORM2004 목록 \n" + sql );
                while ( ls.next() )
                {
                    list.add( ls.getDataBox() );
                }
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace( ex, box, sql );
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return list;
    }
    
    /**
     * 컨텐츠 등록 관리 - 구성 정보 (기본정보)
     * 
     * 등록된 컨텐츠 리스트를 반환한다.
     * 
     * @param box
     * @return ArrayList
     * @throws Exception
     */
    public DataBox selectContentDetailInfo(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        DataBox dbox = null;
        
        String sql = "";
        
        try 
        {
            connMgr = new DBConnectionManager();

            String v_courseCode = box.getString("p_course_code");

            sql =
                "\n  SELECT                                                             " +
                "\n        course_code, course_nm, course_type,                         " +
                "\n        manifest_id, is_sequencing, dist_code, upload_path,          " +
                "\n        ldate, luserid, get_name(luserid) name, producer,            " +
                "\n        GET_SUBJCLASS_FULLNM(dist_code) bunyu    				    " +
                "\n  FROM tys_course a                                                  " +
                "\n  WHERE course_code = ':course_code'                                 ";
            
            sql = sql.replaceAll( ":course_code", v_courseCode );

            ls = connMgr.executeQuery( sql );
            System.out.println( sql );
            if ( ls.next() )
            {
                dbox = ls.getDataBox();
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace( ex, box, sql );
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return dbox;
    }

    /**
     * 컨텐츠 등록 관리 - 구성 정보 (상세정보)
     * 
     * 등록된 컨텐츠 리스트를 반환한다.
     * 
     * @param box
     * @return ArrayList
     * @throws Exception
     */
    public List selectContentDetail(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        List list = null;
        String sql = "";
        
        try 
        {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            String v_courseCode = box.getString("p_course_code");

            sql = 
                "\n  SELECT                                                                                         " +
                "\n     LEVEL, ORG_TREE_ORD, ORG_ID, ORG_TITLE, COURSE_CODE,                                        " +
                "\n     ITEM_ID, ITEM_PID, ITEM_ID_REF, ITEM_PARAMETERS, ITEM_TITLE,                                " +
                "\n     ITEM_META, OBJ_ID, TREE_ORD, PREVIOUS, NEXT, EXIT, ABANDON,                                 " +
                "\n     RES_ID, RES_HREF, RES_SCORM_TYPE, RES_META, UPLOAD_PATH                                     " +
                "\n  FROM                                                                                           " +
                "\n  (                                                                                              " +
                "\n     SELECT                                                                                      " +
                "\n        A.TREE_ORD as ORG_TREE_ORD, A.ORG_ID, A.ORG_TITLE, A.COURSE_CODE,                        " +
                "\n        B.ITEM_ID, B.ITEM_PID, B.ITEM_ID_REF, B.ITEM_PARAMETERS, B.ITEM_TITLE,                   " +
                "\n        B.META_LOCATION ITEM_META, B.OBJ_ID, B.TREE_ORD, B.PREVIOUS, B.NEXT, B.EXIT, B.ABANDON,  " +
                "\n        C.RES_ID, C.RES_HREF, C.RES_SCORM_TYPE, C.META_LOCATION RES_META, D.UPLOAD_PATH          " +
                "\n     FROM                                                                                        " +
                "\n         TYS_ORGANIZATION A, TYS_ITEM B, TYS_RESOURCE C, TYS_COURSE D                            " +
                "\n     WHERE                                                                                       " +
                "\n         A.COURSE_CODE = ':course_code'                                                          " +
                "\n         AND A.COURSE_CODE = B.COURSE_CODE                                                       " +
                "\n         AND B.COURSE_CODE = D.COURSE_CODE                                                       " +
                "\n         AND A.ORG_ID = B.ORG_ID                                                                 " +
                "\n        AND B.COURSE_CODE = C.COURSE_CODE(+)                                                     " +
                "\n        AND B.ORG_ID = C.ORG_ID(+)                                                               " +
                "\n        AND B.ITEM_ID = C.ITEM_ID(+)                                                             " +
                "\n        AND B.ITEM_ID_REF = C.RES_ID(+)                                                          " +
                "\n  ) T                                                                                            " +
                "\n  START WITH                                                                                     " +
                "\n     ITEM_PID IN ( SELECT ORG_ID FROM TYS_ORGANIZATION WHERE COURSE_CODE=':course_code')         " +
                "\n  CONNECT BY                                                                                     " +
                "\n     PRIOR ITEM_ID = ITEM_PID                                                                    " +
                "\n  ORDER SIBLINGS BY TREE_ORD                                                                     ";                
    
            sql = sql.replaceAll( ":course_code", v_courseCode );
            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                list.add( ls.getDataBox() );
            }
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace( ex, box, sql );
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        

        return list;
    }

    /**
     * userid에 대한 CP 정보를 map에 담아 반환한다.
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public Map selectCpInfo( RequestBox box ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        Map map = null;
        String sql = "";
        
        try 
        {
            connMgr = new DBConnectionManager();
            map = new HashMap();
         
            String s_gadmin = box.getSession( "gadmin" );
            String s_userid = box.getSession( "userid" );
            String v_testGubun = box.getString("p_testgubun");

            if ( (s_gadmin.equals("S1") || s_gadmin.equals("T1") || s_gadmin.equals("M1")) && v_testGubun.equals("beta") ) 
            {
                sql = 
                    "\n  SELECT cpseq, cpnm         " +
                    "\n  FROM tz_cpinfo             " +
                    "\n  WHERE userid = ':userid'   " +
                    "\n  ORDER BY cpnm              ";
    
                sql = sql.replaceAll( ":userid", s_userid );
                ls = connMgr.executeQuery( sql );
    
                if ( ls.next() )
                {
                    map.put( "isbeta", "Y" );
                    map.put( "producer", ls.getString( "cpseq" ) );
                }
            }
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace( ex, box, sql );
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        

        return map;        
    }

    /**
     * 과정에 매핑된 컨텐츠이면 true를 아니면 false를 반환한다.
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean isMappedContent( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        boolean isMapped = false;
        
        try 
        {
            connMgr = new DBConnectionManager();
            
            String courseCode = box.getString("p_course_code");
            
            sql = 
                "\n  SELECT subj                " +
                "\n  FROM tz_subj_contents      " +
                "\n  WHERE course_code = " + SQLString.Format( courseCode );
            
            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() )
            {
                isMapped = true;
            }
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace( ex, box, sql );
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        

        return isMapped;          
    }

    /**
     * 진도가 나간 컨텐츠이면 true를 아니면 false를 반환한다.
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean isProgressed( RequestBox box ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        boolean isProgressed = false;
        
        try 
        {
            connMgr = new DBConnectionManager();
            
            String courseCode = box.getString("p_course_code");
            
            sql =
                "\n  SELECT subj                " +
                "\n  FROM tz_progress           " +
                "\n  WHERE lesson LIKE '" + courseCode + "%'";
            
            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() )
            {
                isProgressed = true;
            }
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace( ex, box, sql );
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        

        return isProgressed;          
    }

    /**
     * 컨텐츠 삭제 (DB Table과 물리적 File)
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean deleteContent(RequestBox box) throws Exception 
    {
        DBConnectionManager connMgr = null;
        boolean result = false;
        
        try
        {
            // 1. db 삭제
            // 2. db 삭제 성공이면, 파일 삭제
            // 3. 파일 삭제 성공이면, DB Commit
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit( false );
            
            String courseCode =  box.getString( "p_course_code" );
            String uploadPath = getUploadPath( connMgr, courseCode );
            
            boolean deleteTable = false;
            boolean deleteDir = false;
            
            deleteTable = deleteScormContentTable( connMgr, courseCode );

            if ( deleteTable )
            {
                //System.out.println( "# DB 삭제 성공 " );
                deleteDir = deleteScormContentDir( uploadPath, courseCode );
                
                if ( deleteDir )
                {
                    //System.out.println( "# File 삭제 성공 ");
                    result = true;
                    connMgr.commit();
                }
                else
                {
                    //System.out.println( "# File 삭제 실패 ");
                    connMgr.rollback();
                }
            }
            else
            {
                connMgr.rollback();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            connMgr.rollback();
            ErrorManager.getErrorStackTrace( ex );
            throw new Exception( ex.getMessage() );
        }
        finally 
        {
            connMgr.setAutoCommit(true);
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        

        return result;
    }

    /*
     * 컨텐츠의 업로드 경로값을 가져온다
     * 
     * @param connMgr
     * @param courseCode
     * @return
     * @throws Exception
     */
    private String getUploadPath( DBConnectionManager connMgr, String courseCode ) throws Exception
    {
        ListSet ls = null;
        String uploadPath = "";
        
        try
        {
            String sql =
                "\n  SELECT upload_path     " +
                "\n  FROM tys_course        " +
                "\n  WHERE course_code = " + SQLString.Format( courseCode );
            
            ls = connMgr.executeQuery( sql );

            if ( ls.next() )
            {    
                uploadPath = ls.getString( "upload_path" );
            }
        }
        catch( Exception ex )
        {
            ErrorManager.getErrorStackTrace( ex );
            throw new Exception( ex.getMessage() );        
        }
        finally
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }        
        }
                
        return uploadPath;            
    }
    
    /*
     * 컨텐츠의 물리적 파일을 삭제한다.
     *  
     * @param uploadPath
     * @param courseCode
     * @return
     * @throws Exception
     */
    private boolean deleteScormContentDir( String uploadPath, String courseCode ) throws Exception 
    {
        boolean result = false;
            
        ConfigSet conf = new ConfigSet();
        String realPath = conf.getProperty( "SCORM2004.REALPATH." + uploadPath );
        String contentPath = realPath + File.separator + courseCode;
        
        // 디렉토리가 존재하지 않을 경우, 삭제할 파일이 없으므로 true return
        File contentPathFile = new File( contentPath );
        if ( !contentPathFile.exists() ) {
            result = true;
        }
        else {
            FileDelete fd = new FileDelete();
            result = fd.allDelete( contentPath );
            new File( contentPath ).delete();
        }
        
        return result;
    }

    /*
     * Scorm2004 관련 테이블에서 컨텐츠를 삭제한다. 
     * 
     * @param connMgr
     * @param courseCode
     * @return
     * @throws Exception
     */
    private boolean deleteScormContentTable( DBConnectionManager connMgr, String courseCode ) throws Exception
    {
        String sqlCourse = "\r\n  DELETE FROM TYS_COURSE WHERE COURSE_CODE = " + SQLString.Format( courseCode );
        String sqlOrg = "\r\n  DELETE FROM TYS_ORGANIZATION WHERE COURSE_CODE = " + SQLString.Format( courseCode );
        String sqlItem = "\r\n  DELETE FROM TYS_ITEM WHERE COURSE_CODE = " + SQLString.Format( courseCode );
        String sqlResource = "\r\n  DELETE FROM TYS_RESOURCE WHERE COURSE_CODE = " + SQLString.Format( courseCode );
        String sqlResourceFile = "\r\n  DELETE FROM TYS_RESOURCE_FILE WHERE COURSE_CODE = " + SQLString.Format( courseCode );
        String sqlResourceDef = "\r\n  DELETE FROM TYS_RESOURCE_DEPENDENCY WHERE COURSE_CODE = " + SQLString.Format( courseCode );

        String sqlRollupRule = "\r\n  DELETE FROM TYS_ROLLUP_RULE WHERE SEQ_IDX IN ( SELECT SEQ_IDX FROM TYS_SEQUENCE WHERE COURSE_CODE = " + SQLString.Format( courseCode ) + " )";
        String sqlRollupRuleCon = "\r\n  DELETE FROM TYS_ROLLUP_RULE_CONDITION WHERE SEQ_IDX IN ( SELECT SEQ_IDX FROM TYS_SEQUENCE WHERE COURSE_CODE = " + SQLString.Format( courseCode ) + " )";
        String sqlSeqRule = "\r\n  DELETE FROM TYS_SEQ_RULE WHERE SEQ_IDX IN ( SELECT SEQ_IDX FROM TYS_SEQUENCE WHERE COURSE_CODE = " + SQLString.Format( courseCode ) + " )";
        String sqlSeqRuleCon = "\r\n  DELETE FROM TYS_SEQ_RULE_CONDITION WHERE SEQ_IDX IN ( SELECT SEQ_IDX FROM TYS_SEQUENCE WHERE COURSE_CODE = " + SQLString.Format( courseCode ) + " )";
        String sqlObj = "\r\n  DELETE FROM TYS_OBJECTIVES WHERE SEQ_IDX IN ( SELECT SEQ_IDX FROM TYS_SEQUENCE WHERE COURSE_CODE = " + SQLString.Format( courseCode ) + " )";
        String sqlObjMap = "\r\n  DELETE FROM TYS_OBJECTIVES_MAPINFO WHERE SEQ_IDX IN ( SELECT SEQ_IDX FROM TYS_SEQUENCE WHERE COURSE_CODE = " + SQLString.Format( courseCode ) + " )";
        String sqlSeq = "\r\n  DELETE FROM TYS_SEQUENCE WHERE COURSE_CODE = " + SQLString.Format( courseCode );

        String sqlLOMBasic = "\r\n  DELETE FROM LOM_BASIC WHERE COURSE_CODE = " + SQLString.Format( courseCode );
        String sqlLOMMetadata = "\r\n  DELETE FROM LOM_METADATA WHERE COURSE_CODE = " + SQLString.Format( courseCode );

        
        connMgr.executeUpdate( sqlCourse );
        connMgr.executeUpdate( sqlOrg );
        connMgr.executeUpdate( sqlItem );
        connMgr.executeUpdate( sqlResource );
        connMgr.executeUpdate( sqlResourceFile );
        connMgr.executeUpdate( sqlResourceDef );

        connMgr.executeUpdate( sqlRollupRule );
        connMgr.executeUpdate( sqlRollupRuleCon );
        connMgr.executeUpdate( sqlSeqRule );
        connMgr.executeUpdate( sqlSeqRuleCon );
        connMgr.executeUpdate( sqlObj );
        connMgr.executeUpdate( sqlObjMap );
        connMgr.executeUpdate( sqlSeq );
        
        connMgr.executeUpdate( sqlLOMBasic );
        connMgr.executeUpdate( sqlLOMMetadata );
        
        return true;
    }

	public boolean updateContent(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
	
		String sql = "";
		int count = 0;
	
		try
		{
			connMgr = new DBConnectionManager();
			
			String courseCode = box.getString("p_course_code");
			String distCode = box.getString("p_dist_code");
			
			String courseNm = box.getString("p_course_nm");
			String courseType = box.getString("p_course_type");
			String isSequencing = box.getString("p_is_sequencing");
			String luserid = box.getSession("userid");

			sql =
				"\n  UPDATE TYS_COURSE  " +
				"\n  SET    COURSE_NM     = ?,  " +
				"\n         COURSE_TYPE   = ?,  " +
				"\n         IS_SEQUENCING = ?,  " +
				"\n         DIST_CODE     = ?,  " +
				"\n         LUSERID       = ?,  " +
				"\n         LDATE         = TO_CHAR( sysdate, 'YYYYMMDDHH24MISS' )  " +
				"\n  WHERE  COURSE_CODE   = ?  ";
			
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setString( 1, courseNm );
			pstmt.setString( 2, courseType );
			pstmt.setString( 3, isSequencing );
			pstmt.setString( 4, distCode );
			pstmt.setString( 5, luserid );
			pstmt.setString( 6, courseCode );
			
			count = pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
		}
	
		return count > 0;		
	}
}
