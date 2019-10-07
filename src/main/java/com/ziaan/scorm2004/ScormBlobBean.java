/*
 * @(#)ScormBlobBean.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//import org.apache.tomcat.dbcp.dbcp.DelegatingResultSet;
//import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.RequestBox;

/**
 * Tree객체 및 CMI Datamodel을 BLOB형으로 Write/Read 하기 위한 Bean Class
 *
 * @version 1.0 2006. 9. 6.
 * @author Jin-pil Chung
 *
 */
public class ScormBlobBean
{
    /**
     * "_" 구분자로 문자열 배열을 반환한다.
     * 
     * @param lmsID
     * @return subj, year, subjseq 의 배열값 반환
     */
    private String[] getLMSIDToken( String lmsID )
    {
        String[] token = lmsID.split( "_" );
        
        if ( token == null )
        {
            token = new String[]{ "", "", "" }; 
        }
        
        return token;
    }
    
    /**
     * DataModel 객체를 테이블에 Blob형으로 저장한다.
     * 
     * @param lmsID
     * @param courseID
     * @param orgID
     * @param scoID
     * @param attempt
     * @param userid
     * @param obj
     * @return
     * @throws Exception
     */
    public boolean insertDataModelObject( String lmsID, String courseID, String orgID, String scoID, String attempt, String userid, Object obj ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        boolean result = false;
        
        String sqlInsert = "";
        String sqlSelect = "";
        int isOk = 0;
        
        try 
        { 
            String[] token = getLMSIDToken( lmsID );

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // select
            sqlSelect =
                "\n  SELECT SUBJ                " +
                "\n  FROM TYS_DATAMODELOBJECT   " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ?           " +
                "\n      AND YEAR = ?           " +
                "\n      AND SUBJSEQ = ?        " +
                "\n      AND COURSE_CODE = ?    " +
                "\n      AND ORG_ID = ?         " +
                "\n      AND SCO_ID = ?         " +
                "\n      AND ATTEMPT = ?        " +
                "\n      AND USERID = ?         ";

            pstmt = connMgr.prepareStatement( sqlSelect );
            
            pstmt.setString( 1, token[0] );
            pstmt.setString( 2, token[1] );
            pstmt.setString( 3, token[2] );
            pstmt.setString( 4, courseID );
            pstmt.setString( 5, orgID );
            pstmt.setString( 6, scoID );
            pstmt.setString( 7, attempt );
            pstmt.setString( 8, userid );
            
            rs = pstmt.executeQuery();
            
            if ( rs.next() )    // 있으면 update
            {
                // insert
                sqlInsert =
                    "\n  UPDATE TYS_DATAMODELOBJECT                                                             " +
                    "\n      SET DATAMODELOBJECT = EMPTY_BLOB(), LDATE = TO_CHAR( sysdate, 'YYYYMMDDHH24MISS')  " +
                    "\n  WHERE 1 = 1                                                                            " +
                    "\n      AND SUBJ = ?                                                                       " +
                    "\n      AND YEAR = ?                                                                       " +
                    "\n      AND SUBJSEQ = ?                                                                    " +
                    "\n      AND COURSE_CODE = ?                                                                " +
                    "\n      AND ORG_ID = ?                                                                     " +
                    "\n      AND SCO_ID = ?                                                                     " +
                    "\n      AND ATTEMPT = ?                                                                    " +
                    "\n      AND USERID = ?                                                                     ";
            }
            else                // 없으면 insert
            {
                // insert
                sqlInsert =
                    "\n  INSERT INTO TYS_DATAMODELOBJECT                                                                    " +
                    "\n      ( SUBJ, YEAR, SUBJSEQ, COURSE_CODE, ORG_ID, SCO_ID, ATTEMPT, USERID, DATAMODELOBJECT, LDATE )  " +
                    "\n  VALUES                                                                                             " +
                    "\n      ( ?, ?, ?, ?, ?, ?, ?, ?, EMPTY_BLOB(), TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )                ";
            }
            pstmt.close();
            
            pstmt = connMgr.prepareStatement(sqlInsert);
            
            pstmt.setString( 1, token[0] );
            pstmt.setString( 2, token[1] );
            pstmt.setString( 3, token[2] );
            pstmt.setString( 4, courseID );
            pstmt.setString( 5, orgID );
            pstmt.setString( 6, scoID );
            pstmt.setString( 7, attempt );
            pstmt.setString( 8, userid );
            
            isOk = pstmt.executeUpdate();
            pstmt.close();

            if ( isOk == 1 )
            {
                sqlSelect =
                    "\n  SELECT DATAMODELOBJECT     " +
                    "\n  FROM TYS_DATAMODELOBJECT   " +
                    "\n  WHERE 1 = 1                " +
                    "\n      AND SUBJ = ?           " +
                    "\n      AND YEAR = ?           " +
                    "\n      AND SUBJSEQ = ?        " +
                    "\n      AND COURSE_CODE = ?    " +
                    "\n      AND ORG_ID = ?         " +
                    "\n      AND SCO_ID = ?         " +
                    "\n      AND ATTEMPT = ?         " +
                    "\n      AND USERID = ?         " +
                    "\n  FOR UPDATE                 ";                
                
                pstmt = connMgr.prepareStatement( sqlSelect );
    
                pstmt.setString( 1, token[0] );
                pstmt.setString( 2, token[1] );
                pstmt.setString( 3, token[2] );
                pstmt.setString( 4, courseID );
                pstmt.setString( 5, orgID );
                pstmt.setString( 6, scoID );
                pstmt.setString( 7, attempt );
                pstmt.setString( 8, userid );
                
                rs = pstmt.executeQuery();
                
                if ( rs.next() )
                {
                    // BLOB blob = ((OracleResultSet)((DelegatingResultSet)rs).getDelegate()).getBLOB("DATAMODELOBJECT");
                	BLOB blob = (BLOB) rs.getBlob("DATAMODELOBJECT");
                    
                    OutputStream os = blob.getBinaryOutputStream();
                    ObjectOutputStream oop = new ObjectOutputStream(os);
                    oop.writeObject(obj);
                    oop.flush();
                    oop.close();
                    os.close();
                }
    
                pstmt.close();
                
                connMgr.commit();
                result = true;
            }
            else
            {
                connMgr.rollback();
                result = false;
            }
            
        }
        catch ( Exception ex ) 
        { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception( ex.getMessage() );
        }
        finally 
        { 
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch (Exception e2) {} }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return result;        
    }
    
    /**
     * 테이블에 저장된 DataModel 객체를 반환한다.
     * 
     * @param lmsID
     * @param courseID
     * @param orgID
     * @param scoID
     * @param attempt
     * @param userid
     * @return
     * @throws Exception
     */
    public Object selectDataModelObject( String lmsID, String courseID, String orgID, String scoID, String attempt, String userid ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "";
        Object obj = null;

        try 
        {
            String[] token = getLMSIDToken( lmsID );
            
            connMgr = new DBConnectionManager();

            sql = 
                "\n  SELECT DATAMODELOBJECT     " +
                "\n  FROM TYS_DATAMODELOBJECT   " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ?           " +
                "\n      AND YEAR = ?           " +
                "\n      AND SUBJSEQ = ?        " +
                "\n      AND COURSE_CODE = ?    " +
                "\n      AND ORG_ID = ?         " +
                "\n      AND SCO_ID = ?         " +
                "\n      AND ATTEMPT = ?        " +
                "\n      AND USERID = ?         ";

            pstmt = connMgr.prepareStatement( sql );
            
            pstmt.setString( 1, token[0] );
            pstmt.setString( 2, token[1] );
            pstmt.setString( 3, token[2] );
            pstmt.setString( 4, courseID );
            pstmt.setString( 5, orgID );
            pstmt.setString( 6, scoID );
            pstmt.setString( 7, attempt );
            pstmt.setString( 8, userid );
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) 
            {
                InputStream is = rs.getBlob(1).getBinaryStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                obj = ois.readObject();
                ois.close();
                is.close();
            }
            pstmt.close();
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } 
        finally 
        {
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch (Exception e) {} }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return obj;        
    }    

    /*
    public boolean insertTreeObject( String lmsID, String courseID, String orgID, String userid, Object obj, String reviewtype ) throws Exception
    {
        if ( reviewtype.equals("review") || reviewtype.equals("openedu") )
        {
            deleteTreeObjectReview( userid );
            return insertTreeObjectReview( lmsID, courseID, orgID, userid, obj );
        }
        else
        {
            return insertTreeObject( lmsID, courseID, orgID, userid, obj );
        }
    }
    */
    
    public Object selectTreeObject( String lmsID, String courseID, String orgID, String userid, String reviewType ) throws Exception
    {
        if ( reviewType.equals("betatest") || reviewType.equals("review") || reviewType.equals("openedu") || reviewType.equals("preview") )
        {
            return selectTreeObjectReview( lmsID, courseID, orgID, userid );
        }
        else
        {
            return selectTreeObject( lmsID, courseID, orgID, userid );
        }
    }
    
    /**
     * 사용자별 트리객체를 테이블에 Blob형으로 저장한다.
     * 
     * @param lmsID
     * @param courseID
     * @param orgID
     * @param userid
     * @param obj
     * @param reviewtype
     * @return
     * @throws Exception
     */
    public boolean insertTreeObject( String lmsID, String courseID, String orgID, String userid, Object obj, boolean isUpdate ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        boolean result = false;
        
        String sqlInsert = "";
        String sqlSelect = "";
        int isOk = 0;
        
        boolean isSkip = false;
        
        try 
        { 
            String[] token = getLMSIDToken( lmsID );

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // select
            sqlSelect =
                "\n  SELECT SUBJ                " +
                "\n  FROM TYS_TREEOBJECT        " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ?           " +
                "\n      AND YEAR = ?           " +
                "\n      AND SUBJSEQ = ?        " +
                "\n      AND COURSE_CODE = ?    " +
                "\n      AND ORG_ID = ?         " +
                "\n      AND USERID = ?         ";

            pstmt = connMgr.prepareStatement( sqlSelect );
            pstmt.setString( 1, token[0] );
            pstmt.setString( 2, token[1] );
            pstmt.setString( 3, token[2] );
            pstmt.setString( 4, courseID );
            pstmt.setString( 5, orgID );
            pstmt.setString( 6, userid );
            
            rs = pstmt.executeQuery();
            
            if ( rs.next() )    // 있으면 update
            {
                if ( !isUpdate )
                {
                    isSkip = true;
                }
                
                // update
                sqlInsert =
                    "\n  UPDATE TYS_TREEOBJECT                                                              " +
                    "\n      SET TREEOBJECT = EMPTY_BLOB(), LDATE = TO_CHAR( sysdate, 'YYYYMMDDHH24MISS')   " +
                    "\n  WHERE 1 = 1                                                                        " +
                    "\n      AND SUBJ = ?                                                                   " +
                    "\n      AND YEAR = ?                                                                   " +
                    "\n      AND SUBJSEQ = ?                                                                " +
                    "\n      AND COURSE_CODE = ?                                                            " +
                    "\n      AND ORG_ID = ?                                                                 " +
                    "\n      AND USERID = ?                                                                 ";
            }
            else                // 없으면 insert
            {
                // insert
                sqlInsert =
                    "\n  INSERT INTO TYS_TREEOBJECT                                                     " +
                    "\n      ( SUBJ, YEAR, SUBJSEQ, COURSE_CODE, ORG_ID, USERID, TREEOBJECT, LDATE )    " +
                    "\n  VALUES                                                                         " +
                    "\n      ( ?, ?, ?, ?, ?, ?, EMPTY_BLOB(), TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )  ";
            }
            pstmt.close();
            
            if ( !isSkip )
            {
                pstmt = connMgr.prepareStatement(sqlInsert);
                
                pstmt.setString( 1, token[0] );
                pstmt.setString( 2, token[1] );
                pstmt.setString( 3, token[2] );
                pstmt.setString( 4, courseID );
                pstmt.setString( 5, orgID );
                pstmt.setString( 6, userid );
                
                isOk = pstmt.executeUpdate();
                pstmt.close();
            }
            
            if ( isOk == 1 )
            {
                sqlSelect =
                    "\n  SELECT TREEOBJECT          " +
                    "\n  FROM TYS_TREEOBJECT        " +
                    "\n  WHERE 1 = 1                " +
                    "\n      AND SUBJ = ?           " +
                    "\n      AND YEAR = ?           " +
                    "\n      AND SUBJSEQ = ?        " +
                    "\n      AND COURSE_CODE = ?    " +
                    "\n      AND ORG_ID = ?         " +
                    "\n      AND USERID = ?         " +
                    "\n  FOR UPDATE                 ";                
                
                pstmt = connMgr.prepareStatement( sqlSelect );
    
                pstmt.setString( 1, token[0] );
                pstmt.setString( 2, token[1] );
                pstmt.setString( 3, token[2] );
                pstmt.setString( 4, courseID );
                pstmt.setString( 5, orgID );
                pstmt.setString( 6, userid );
                
                rs = pstmt.executeQuery();
                
                if ( rs.next() )
                {
                    // BLOB blob = ((OracleResultSet)((DelegatingResultSet)rs).getDelegate()).getBLOB("TREEOBJECT");
                	BLOB blob = (BLOB) rs.getBlob("TREEOBJECT");
                    
                    OutputStream os = blob.getBinaryOutputStream();
                    ObjectOutputStream oop = new ObjectOutputStream(os);
                    oop.writeObject(obj);
                    oop.flush();
                    oop.close();
                    os.close();
                }
    
                pstmt.close();
                
                connMgr.commit();
                result = true;
            }
            else
            {
                connMgr.rollback();
                result = false;
            }
        }
        catch ( Exception ex ) 
        { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception( ex.getMessage() );
        }
        finally 
        { 
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch (Exception e2) {} }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return result;    
    }    

    /**
     * 테이블에 저장된 사용자별 트리객체를 반환한다.
     * 
     * @param lmsID
     * @param courseID
     * @param orgID
     * @param userid
     * @param reviewtype
     * @return
     * @throws Exception
     */
    private Object selectTreeObject( String lmsID, String courseID, String orgID, String userid ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "";
        Object obj = null;

        try 
        {
            String[] token = getLMSIDToken( lmsID );
            
            connMgr = new DBConnectionManager();

            sql = 
                "\n  SELECT treeobject          " +
                "\n  FROM TYS_TREEOBJECT        " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ?           " +
                "\n      AND YEAR = ?           " +
                "\n      AND SUBJSEQ = ?        " +
                "\n      AND COURSE_CODE = ?    " +
                "\n      AND ORG_ID = ?         " +
                "\n      AND USERID = ?         ";

            pstmt = connMgr.prepareStatement( sql );
            
            pstmt.setString( 1, token[0] );
            pstmt.setString( 2, token[1] );
            pstmt.setString( 3, token[2] );
            pstmt.setString( 4, courseID );
            pstmt.setString( 5, orgID );
            pstmt.setString( 6, userid );
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) 
            {
                InputStream is = rs.getBlob(1).getBinaryStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                obj = ois.readObject();
                ois.close();
                is.close();
            }
            pstmt.close();
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } 
        finally 
        {
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch (Exception e) {} }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return obj;
    }    
    
    /**
     * 사용자별 트리객체를 테이블에 Blob형으로 저장한다.
     * 
     * @param lmsID
     * @param courseID
     * @param orgID
     * @param userid
     * @param obj
     * @param reviewtype
     * @return
     * @throws Exception
     */
    public boolean insertTreeObjectReview( String lmsID, String courseID, String orgID, String userid, Object obj ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        boolean result = false;
        
        String sqlInsert = "";
        String sqlSelect = "";
        int isOk = 0;
        
        try 
        { 
            String[] token = getLMSIDToken( lmsID );

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // select
            sqlSelect =
                "\n  SELECT SUBJ                " +
                "\n  FROM TYS_TREEOBJECT_REVIEW  " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ?           " +
                "\n      AND COURSE_CODE = ?    " +
                "\n      AND ORG_ID = ?         " +
                "\n      AND USERID = ?         ";

            pstmt = connMgr.prepareStatement( sqlSelect );
            pstmt.setString( 1, token[0] );
            pstmt.setString( 2, courseID );
            pstmt.setString( 3, orgID );
            pstmt.setString( 4, userid );
            
            rs = pstmt.executeQuery();
            
            if ( rs.next() )    // 있으면 update
            {
                // insert
                sqlInsert =
                    "\n  UPDATE TYS_TREEOBJECT_REVIEW                                                        " +
                    "\n      SET TREEOBJECT = EMPTY_BLOB(), LDATE = TO_CHAR( sysdate, 'YYYYMMDDHH24MISS')   " +
                    "\n  WHERE 1 = 1                                                                        " +
                    "\n      AND SUBJ = ?                                                                   " +
                    "\n      AND COURSE_CODE = ?                                                            " +
                    "\n      AND ORG_ID = ?                                                                 " +
                    "\n      AND USERID = ?                                                                 ";
            }
            else                // 없으면 insert
            {
                // insert
                sqlInsert =
                    "\n  INSERT INTO TYS_TREEOBJECT_REVIEW                                               " +
                    "\n      ( SUBJ, COURSE_CODE, ORG_ID, USERID, TREEOBJECT, LDATE )    " +
                    "\n  VALUES                                                                         " +
                    "\n      ( ?, ?, ?, ?, EMPTY_BLOB(), TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') )  ";
            }
            pstmt.close();
            
            pstmt = connMgr.prepareStatement(sqlInsert);
            
            pstmt.setString( 1, token[0] );
            pstmt.setString( 2, courseID );
            pstmt.setString( 3, orgID );
            pstmt.setString( 4, userid );
            
            isOk = pstmt.executeUpdate();
            pstmt.close();

            if ( isOk == 1 )
            {
                sqlSelect =
                    "\n  SELECT TREEOBJECT          " +
                    "\n  FROM TYS_TREEOBJECT_REVIEW  " +
                    "\n  WHERE 1 = 1                " +
                    "\n      AND SUBJ = ?           " +
                    "\n      AND COURSE_CODE = ?    " +
                    "\n      AND ORG_ID = ?         " +
                    "\n      AND USERID = ?         " +
                    "\n  FOR UPDATE                 ";                
                
                pstmt = connMgr.prepareStatement( sqlSelect );
    
                pstmt.setString( 1, token[0] );
                pstmt.setString( 2, courseID );
                pstmt.setString( 3, orgID );
                pstmt.setString( 4, userid );
                
                rs = pstmt.executeQuery();
                
                if ( rs.next() )
                {
                    // BLOB blob = ((OracleResultSet)((DelegatingResultSet)rs).getDelegate()).getBLOB("TREEOBJECT");
                	BLOB blob = (BLOB) rs.getBlob("TREEOBJECT");
                    
                    OutputStream os = blob.getBinaryOutputStream();
                    ObjectOutputStream oop = new ObjectOutputStream(os);
                    oop.writeObject(obj);
                    oop.flush();
                    oop.close();
                    os.close();
                }
    
                pstmt.close();
                
                connMgr.commit();
                result = true;
            }
            else
            {
                connMgr.rollback();
                result = false;
            }
            
        }
        catch ( Exception ex ) 
        { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception( ex.getMessage() );
        }
        finally 
        { 
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch (Exception e2) {} }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return result;    
    }    

    /**
     * 테이블에 저장된 사용자별 트리객체를 반환한다.
     * 
     * @param lmsID
     * @param courseID
     * @param orgID
     * @param userid
     * @param reviewtype
     * @return
     * @throws Exception
     */
    private Object selectTreeObjectReview( String lmsID, String courseID, String orgID, String userid ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "";
        Object obj = null;

        try 
        {
            String[] token = getLMSIDToken( lmsID );
            
            connMgr = new DBConnectionManager();

            sql = 
                "\n  SELECT treeobject          " +
                "\n  FROM TYS_TREEOBJECT_REVIEW " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ?           " +
                "\n      AND COURSE_CODE = ?    " +
                "\n      AND ORG_ID = ?         " +
                "\n      AND USERID = ?         ";

            pstmt = connMgr.prepareStatement( sql );
            
            pstmt.setString( 1, token[0] );
            pstmt.setString( 2, courseID );
            pstmt.setString( 3, orgID );
            pstmt.setString( 4, userid );
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) 
            {
                InputStream is = rs.getBlob(1).getBinaryStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                obj = ois.readObject();
                ois.close();
                is.close();
            }
            pstmt.close();
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } 
        finally 
        {
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch (Exception e) {} }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return obj;
    }    
    
    /**
     * 컨텐츠 Import시 기본 Tree 객체를 테이블에 저장한다.
     * @param connMgr2 
     * 
     * @param courseID
     * @param orgID
     * @param obj
     * @return
     * @throws Exception
     */
    public boolean insertTreeObjectInOrganizationTable( DBConnectionManager connMgr, String courseID, String orgID, Object obj ) throws Exception 
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        boolean result = false;
        
        String sqlSelect = "";
        String sqlUpdate = "";
        int isOk = 0;
        
        try 
        { 
            // insert
            sqlUpdate =
                "\n  UPDATE TYS_ORGANIZATION            " +
                "\n      SET TREEOBJECT = EMPTY_BLOB()  " +
                "\n  WHERE 1 = 1                        " +
                "\n      AND COURSE_CODE = ?            " +
                "\n      AND ORG_ID = ?            ";

            pstmt = connMgr.prepareStatement(sqlUpdate);
            pstmt.setString( 1, courseID );
            pstmt.setString( 2, orgID );
            
            isOk = pstmt.executeUpdate();
            pstmt.close();

            if ( isOk == 1 )
            {
                sqlSelect =
                    "\n  SELECT TREEOBJECT          " +
                    "\n  FROM TYS_ORGANIZATION      " +
                    "\n  WHERE 1 = 1                " +
                    "\n      AND COURSE_CODE = ?    " +
                    "\n      AND ORG_ID = ?         " +
                    "\n  FOR UPDATE                 ";                
                
                pstmt = connMgr.prepareStatement( sqlSelect );
                pstmt.setString( 1, courseID );
                pstmt.setString( 2, orgID );
                
                rs = pstmt.executeQuery();
                
                if ( rs.next() )
                {
                    //BLOB blob = ((OracleResultSet)((DelegatingResultSet)rs).getDelegate()).getBLOB("TREEOBJECT");                    
                	BLOB blob = (BLOB) rs.getBlob("TREEOBJECT");

                    OutputStream os = blob.getBinaryOutputStream();
                    ObjectOutputStream oop = new ObjectOutputStream(os);
                    oop.writeObject(obj);
                    oop.flush();
                    oop.close();
                    os.close();
                }
    
                pstmt.close();
                
                result = true;
            }
            else
            {
                result = false;
            }
            
        }
        catch ( Exception ex ) 
        { 
        	ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception( ex.getMessage() );
        }
        finally 
        { 
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return result;    
    }

    /**
     * 테이블에 저장된 기본 Tree 객체를 반환한다.
     * 
     * @param courseID
     * @param orgID
     * @return
     * @throws Exception
     */
    public Object selectTreeObjectInOrganizationTable( String courseID, String orgID ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "";
        Object obj = null;

        try 
        {
            connMgr = new DBConnectionManager();

            sql = 
                "\n  SELECT TREEOBJECT          " +
                "\n  FROM TYS_ORGANIZATION      " +
                "\n  WHERE 1 = 1                " +
                "\n      AND COURSE_CODE = ?    " +
                "\n      AND ORG_ID = ?         ";

            pstmt = connMgr.prepareStatement( sql );
            pstmt.setString( 1, courseID );
            pstmt.setString( 2, orgID );
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) 
            {
                InputStream is = rs.getBlob(1).getBinaryStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                obj = ois.readObject();
                ois.close();
                is.close();
            }
            pstmt.close();
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } 
        finally 
        {
            if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return obj;     
    }

	public boolean isTreeObjectForUser(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "";
        Object obj = null;

        try 
        {
            connMgr = new DBConnectionManager();

            String subj = box.getSession("subj");
            String year = box.getSession("year");
            String subjseq = box.getSession("subjseq");
            
            sql = 
                "\n  SELECT TREEOBJECT          " +
                "\n  FROM TYS_TREEOBJECT        " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ?    		" +
                "\n      AND YEAR = ?         	" +
            	"\n      AND SUBJSEQ = ?       	";

            pstmt = connMgr.prepareStatement( sql );
            pstmt.setString( 1, subj );
            pstmt.setString( 2, year );
            pstmt.setString( 3, subjseq );
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) 
            {
                InputStream is = rs.getBlob(1).getBinaryStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                obj = ois.readObject();
                ois.close();
                is.close();
            }
            pstmt.close();
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } 
        finally 
        {
            if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if (connMgr != null) { try { connMgr.freeConnection(); } catch (Exception e10) {} }
        }

        return obj == null ? false : true; 
	}
}
