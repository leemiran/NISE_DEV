/*
 * @(#)ScormPreviewBean.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DataBox;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.scorm2004.runtime.client.SequencingEngineBean;

/**
 * 마스터폼 - 맛보기 학습창 Bean Class
 *
 * @version 1.0 2006. 4. 19.
 * @author Jin-pil Chung
 *
 */
public class ScormPreviewBean
{
    /**
     * 맛보기 설정 화면
     * 
     * @param box
     * @return ArrayList
     * @throws Exception
     */
    public List selectPreviewOrgList(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ListSet ls1 = null;

        List resultList = null;
        String sql = "";
        
        try 
        {
            connMgr = new DBConnectionManager();
            resultList = new ArrayList();

            String v_subj = box.getString("p_subj");
            String v_courseCode = "";
            String v_orgID = "";
            
            sql =
                "\n  SELECT subj, course_code, org_id, ord  " +
                "\n  FROM tz_subj_contents                  " +
                "\n  WHERE subj = ':p_subj'                 " +
                "\n  ORDER BY ord                           ";

            sql = sql.replaceAll( ":p_subj", v_subj );
            ls = connMgr.executeQuery( sql );
            
            
            while ( ls.next() )
            {
                List list = new ArrayList();
                
                v_courseCode = ls.getString("course_code");
                v_orgID = ls.getString("org_id");
                
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
                    "\n     ITEM_PID = ':org_id'                                                                        " +
                    "\n  CONNECT BY                                                                                     " +
                    "\n     PRIOR ITEM_ID = ITEM_PID                                                                    " +
                    "\n  ORDER SIBLINGS BY TREE_ORD                                                                     ";                
        
                sql = sql.replaceAll( ":course_code", v_courseCode );
                sql = sql.replaceAll( ":org_id", v_orgID );
                
                ls1 = connMgr.executeQuery( sql );

                while ( ls1.next() )
                {
                    list.add( ls1.getDataBox() );
                }
                
                resultList.add( list );
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
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        

        return resultList;
    }    
    
    /**
     * 맛보기설정 수정
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public int updatePreview( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        
        String sql = "";
        int isOk = 0;

        try 
        { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            String v_subj = box.getString("p_subj");
            Vector v_objID = box.getVector( "p_obj_id" );
            String v_userid = box.getSession("userid");
            
            // delete
            sql = 
                "\n  delete from tz_previewobj      " +
                "\n  where subj = " + SQLString.Format(v_subj);
            
            isOk = connMgr.executeUpdate( sql );

            // insert
            sql =
                "\n  insert into tz_previewobj                                    " +
                "\n  ( subj, oid, ordering, luserid, ldate )                      " +
                "\n  values ( ?, ?, ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS' ) )  ";
            
            pstmt = connMgr.prepareStatement(sql);
            
            for ( int i=0; i<v_objID.size(); i++ )
            {
                pstmt.setString( 1, v_subj );
                pstmt.setString( 2, (String) v_objID.get(i) );
                pstmt.setInt( 3, i+1 );
                pstmt.setString( 4, v_userid );
                
                isOk = pstmt.executeUpdate();
                
                if ( isOk < 0 ) 
                {
                    connMgr.rollback();
                    return -1;
                }
            }
            
            connMgr.commit();
        }
        catch ( Exception ex ) 
        { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch (Exception e2) {} }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return isOk;        
    }
    
    /**
     * 맛보기로 설정된 Item을 반환한다.
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public List selectPreviewSelectedItem( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        ArrayList list = null;
        String sql = "";
        
        try 
        {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            String v_subj = box.getString("p_subj");

            sql =
                "\n  SELECT OID             " +
                "\n  FROM tz_previewobj     " +
                "\n  WHERE subj = ':subj'   ";
            
            sql = sql.replaceAll( ":subj", v_subj );

            ls = connMgr.executeQuery( sql );
            
            while ( ls.next() )
            {
                list.add( ls.getString("oid") );
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
     * 맛보기 페이지 Url을 반환한다.
     * 
     * @param box
     * @return
     */
    public String getPreviewUrl( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String sql = "";
        
        String previewUrl = "";
        String webPath = "";        
        
        try 
        {
            connMgr = new DBConnectionManager();

            String v_subj = box.getString("p_subj");
            String v_ordering = box.getStringDefault("p_ordering", "1" );
            
            webPath = box.getString("p_webpath");
            
            sql = 
                "\n  SELECT                                                                                 " +
                "\n      a.course_code, a.item_id, a.item_parameters, b.res_href,                           " +
                "\n      c.ordering, c.oid, e.upload_path                                                   " +
                "\n  FROM                                                                                   " +
                "\n      tys_item a, tys_resource b, tz_previewobj c,                                       " +
                "\n      (SELECT subj, course_code, org_id FROM tz_subj_contents WHERE subj = ':subj') d,   " +
                "\n      tys_course e                                                                       " +
                "\n  WHERE 1 = 1                                                                            " +
                "\n     AND a.course_code = b.course_code                                                   " +
                "\n     AND b.course_code = d.course_code                                                   " +
                "\n     AND d.course_code = e.course_code                                                   " +
                "\n     AND c.subj = d.subj                                                                 " +
                "\n     AND a.org_id = b.org_id                                                             " +
                "\n     AND b.org_id = d.org_id                                                             " +
                "\n     AND a.item_id = b.item_id                                                           " +
                "\n     AND a.obj_id = c.oid                                                                " +
                "\n     AND c.ordering = ':ordering'                                                        ";
            
            sql = sql.replaceAll( ":subj", v_subj );
            sql = sql.replaceAll( ":ordering", v_ordering );
            
            ls = connMgr.executeQuery( sql );
            
            String courseCode = "";
            if ( ls.next() )
            {
                courseCode = ls.getString("course_code");
                previewUrl = ls.getString("res_href") + ls.getString("item_parameters");
            }            
            
            if ( webPath.equals("") )
            {
                ConfigSet conf = new ConfigSet();
                SequencingEngineBean seb = new SequencingEngineBean();
                
                String uploadCode = seb.selectUploadPath( courseCode );
                String webPathKey = "SCORM2004.WEBPATH." + uploadCode;
                
                if ( !uploadCode.equals("") )
                {
                    webPath = conf.getProperty( webPathKey );
                    box.put( "p_webpath", webPath );
                }
                else
                {
                    webPath = "";
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

        return webPath + previewUrl;
    }

    public String[] getMinMaxOrdering( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String sql = "";
        
        String minOrdering = "";
        String maxOrdering = "";
        
        try 
        {
            connMgr = new DBConnectionManager();

            String v_subj = box.getString("p_subj");

            sql = 
                "\n  SELECT MIN(ordering) as minOrdering, MAX (ordering) as maxOrdering  " +
                "\n  FROM tz_previewobj                     " +
                "\n  WHERE subj = " + SQLString.Format( v_subj );                
            
            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() )
            {
                minOrdering = ls.getString("minOrdering");
                maxOrdering = ls.getString("maxOrdering");
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

        return new String[]{ minOrdering, maxOrdering };
    }
}
