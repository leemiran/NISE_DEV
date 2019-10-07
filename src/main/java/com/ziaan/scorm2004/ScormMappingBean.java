/*
 * @(#)ScormMappingBean.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.scorm2004.runtime.client.SequencingEngineBean;

/**
 * 마스터폼 - 과정매핑 Bean Class
 *
 * @version 1.0 2006. 4. 19.
 * @author Jin-pil Chung
 *
 */
public class ScormMappingBean
{
    /**
     * LMS과정과 스콤 컨텐츠의 매핑 리스트를 반환한다
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectMappingList( RequestBox box ) throws Exception
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
                "\n  SELECT                                                                 " +
                "\n     a.subj, a.course_code, b.course_nm, c.org_title, a.org_id, a.ord    " +
                "\n  FROM                                                                   " +
                "\n     tz_subj_contents a, tys_course b, tys_organization c                " +
                "\n  WHERE 1 = 1                                                            " +
                "\n     AND a.course_code = b.course_code(+)                                " +
                "\n     AND a.org_id = c.org_id                                             " +
                "\n     AND b.course_code = c.course_code                                   " +
                "\n     AND a.subj = " + SQLString.Format( v_subj ) +
                "\n  ORDER BY a.ord                                                         ";

            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                list.add ( ls.getDataBox() );
            }

            ls.close();
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
     * SCORM2004 컨텐츠 리스트를 반환한다.
     * 
     * @return
     * @throws Exception
     */
    public ArrayList selectCourseList( RequestBox box ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        ArrayList list = null;
        String sql = "";
        
        
        try 
        {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            String v_action = box.getString("p_action");
            
            if ( v_action.equals("go") )
            {
                String s_gadmin = box.getSession( "gadmin" );
                String s_userid = box.getSession( "userid" );                
                String v_testGubun = box.getString("p_testgubun");
                
                String ss_upperclass = box.getString("s_upperclass");
                String ss_middleclass = box.getString("s_middleclass");
                String ss_courseNm = box.getString("s_course_nm");

                sql =
                    "\n  SELECT                                                   " +
                    "\n      a.course_code, a.course_nm, b.org_title, b.org_id    " +
                    "\n  FROM                                                     " +
                    "\n      tys_course a, tys_organization b                     " +
                    "\n  WHERE                                                    " +
                    "\n      a.course_code = b.course_code                        " +
                    "\n      :addWhere                                            ";
                
                // 베타 테스터 업체인지,
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
                    
                    sql +=
                        "\n  AND isbeta = 'Y' and producer = '" +
                        betaProducer +
                        "'";
                }

                StringBuffer addWhere = new StringBuffer();
                
                // 검색 조건
                if ( !ss_upperclass.equals("ALL") )
                {
                    addWhere.append( "\n  AND SUBSTR(DIST_CODE, 0, 1) = '" );
                    addWhere.append( ss_upperclass );
                    addWhere.append( "'" );
                    
                    if ( !ss_middleclass.equals("ALL") )
                    {
                        addWhere.append( "\n  AND DIST_CODE = '" );
                        addWhere.append( ss_middleclass );
                        addWhere.append( "'" );
                    }
                }

                if ( !ss_courseNm.equals("") )
                {
                    addWhere.append( "\n  AND UPPER(COURSE_NM) LIKE '%" );
                    addWhere.append( ss_courseNm.toUpperCase() );
                    addWhere.append( "%'" );
                }
                
                addWhere.append( "\n  ORDER BY COURSE_CODE " );
                sql = sql.replaceAll( ":addWhere", addWhere.toString() );
                ls = connMgr.executeQuery( sql );   
                
                while ( ls.next() )
                {
                    list.add ( ls.getDataBox() );
                }
    
                ls.close();
            }
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace( ex );
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
     * 과정 매핑 정보를 저장한다
     * 
     * @param box
     * @return
     */
    public int updateMapping( RequestBox box ) throws Exception 
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
            String v_userid = box.getSession("userid");
            Vector v_mappingCode = box.getVector( "p_mapping_code" );

            // delete
            sql =
                "\n  delete from TZ_SUBJ_CONTENTS   " +
                "\n  where subj = " + SQLString.Format(v_subj);

            isOk = connMgr.executeUpdate( sql );


            // insert
            sql =
                "\n  INSERT INTO tz_subj_contents                                   " +
                "\n  (subj, course_code, org_id, ord, luserid, ldate )              " +
                "\n  VALUES (?, ?, ?, ?, ?, TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') ) ";

            pstmt = connMgr.prepareStatement(sql);

            String courseCode = "";
            String orgID = "";

            String code = "";
            int idx = 0;

            for ( int i=0; i<v_mappingCode.size(); i++ )
            {
                code = (String) v_mappingCode.get(i);
                idx = code.indexOf( "_" );
                
                courseCode = code.substring( 0, idx );
                orgID = code.substring( idx+1, code.length() );
                
                pstmt.setString( 1, v_subj );
                pstmt.setString( 2, courseCode );
                pstmt.setString( 3, orgID );
                pstmt.setInt( 4, i+1 );
                pstmt.setString( 5, v_userid );
                
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
     * 매핑된 컨텐츠 목록을 반환한다.
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public Map selectMappedMap( RequestBox box ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        Map map = null;
        String sql = "";
        
        try 
        {
            connMgr = new DBConnectionManager();
            map = new HashMap();
            
            String v_subj = box.getString("p_subj");

            sql =
                "\n  SELECT subj, course_code, org_id   " +
                "\n  FROM tz_subj_contents              " +
                "\n  WHERE subj = " + SQLString.Format(v_subj);                
            
            ls = connMgr.executeQuery( sql );

            String mappingCode = "";
            
            while ( ls.next() )
            {
                mappingCode = ls.getString("course_code") + "_" + ls.getString("org_id");
                map.put( mappingCode, v_subj );
            }

            ls.close();
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace( ex );
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
            Vector v_itemID = box.getVector( "p_item_id" );
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
                "\n  values ( ?, ?, ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24SS' ) )  ";
            
            pstmt = connMgr.prepareStatement(sql);
            
            for ( int i=0; i<v_itemID.size(); i++ )
            {
                pstmt.setString( 1, v_subj );
                pstmt.setString( 2, (String) v_itemID.get(i) );
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
    public ArrayList selectPreviewSelectedItem( RequestBox box ) throws Exception
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
                "\n  SELECT                                                                     " +
                "\n      a.course_code, a.item_id, a.item_parameters, b.res_href, c.ordering    " +
                "\n  FROM                                                                       " +
                "\n      tys_item a, tys_resource b, tz_previewobj c,                           " +
                "\n      (SELECT course_code FROM tz_subj_contents WHERE subj = ':subj') d      " +
                "\n  WHERE 1 = 1                                                                " +
                "\n      AND a.course_code = d.course_code                                      " +
                "\n      AND a.course_code = b.course_code                                      " +
                "\n      AND a.org_id = b.org_id                                                " +
                "\n      AND a.item_id = b.item_id                                              " +
                "\n      AND b.item_id = c.OID                                                  " +
                "\n      AND c.ordering = :ordering                                             ";
            
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

    public int insertMappingCourse( RequestBox  box) throws Exception
    {
        DBConnectionManager connMgr = null;
        
        String sql = "";
        int isOk = 0;

        try 
        { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            String v_subj = box.getString("p_subj");
            String v_userid = box.getSession("userid");
            Vector v_checks = box.getVector( "p_check" );
            
            StringBuffer courseCodes = new StringBuffer(); 
            
            if ( v_checks != null && v_checks.size() != 0 )
            {
                for ( int i=0; i<v_checks.size(); i++ )
                {
                    courseCodes.append( "'" );
                    courseCodes.append( v_checks.get(i) );
                    courseCodes.append( "'" );
                    
                    if ( i != v_checks.size()-1 )
                    {
                        courseCodes.append( "," );
                    }
                }
            }
            else
            {
                courseCodes.append( "''" );
            }
            
            // delete
            sql =
                "\n  delete from TZ_SUBJ_CONTENTS   " +
                "\n  where subj = " + SQLString.Format(v_subj);

            isOk = connMgr.executeUpdate( sql );


            // insert
            sql =
                "\n  INSERT INTO tz_subj_contents                                                                   " +
                "\n   SELECT ':subj', course_code, org_id, ROWNUM, ':userid', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  " +
                "\n   FROM tys_organization                                                                         " +
                "\n   WHERE course_code IN ( :courseCodes )                                                         " +
                "\n   ORDER BY course_code                                                                          ";

            sql = sql.replaceAll( ":subj", v_subj );
            sql = sql.replaceAll( ":userid", v_userid );
            sql = sql.replaceAll( ":courseCodes", courseCodes.toString() );
            
            isOk = connMgr.executeUpdate( sql );

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
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch (Exception e2) {} }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return isOk;             
    }
}
