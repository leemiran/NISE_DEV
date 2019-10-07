/*
 * @(#)ScormCourseBean.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.scorm2004.sequencer.ADLSeqUtilities;
import com.ziaan.scorm2004.sequencer.SeqActivityTree;

/**
 * ��� ��� Bean Class
 *
 * @version 1.1 2006. 9. 6.
 * @author Jin-pil Chung
 *
 */
public class ScormCourseBean
{
    private static boolean _Debug = false;

    /**
     * ����ں� Ʈ����ü ������ ���̺? �����Ѵ�. - ��ȿ�� �˻�
     * (SCORM ������ ���, ������û�� �н����� ��쿡�� ������)
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean processTreeObjectForValidUsers( String v_subj, String v_year, String v_subjseq, String v_userids ) throws Exception {

    	DBConnectionManager connMgr = null;
        ListSet ls = null;

        List useridList = null;
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager();
            useridList = new ArrayList();
            
            String[] userids = v_userids.split(",");
            String id = "";
            for ( int i=0; i<userids.length; i++ ) {
            	id += StringManager.makeSQL(userids[i].trim());
            	
            	if ( i != userids.length-1 ) {
            		id += ",";
            	}
            }
            
            sql =
            	"\n  SELECT userid  " +
            	"\n    FROM tz_subj a, tz_subjseq b, tz_propose c  " +
            	"\n   WHERE a.subj = b.subj  " +
            	"\n     AND b.subj = c.subj  " +
            	"\n     AND b.YEAR = c.YEAR  " +
            	"\n     AND b.subjseq = c.subjseq  " +
            	"\n     AND b.subj = '#subj#'  " +
            	"\n     AND b.YEAR = '#year#'  " +
            	"\n     AND b.subjseq = '#subjseq#'  " +
            	"\n     AND a.contenttype = 'S'  " +
            	"\n     AND userid IN ( #userids# )  ";            	

            sql = sql.replaceAll( "#subj#", v_subj );
            sql = sql.replaceAll( "#year#", v_year );
            sql = sql.replaceAll( "#subjseq#", v_subjseq );
            sql = sql.replaceAll( "#userids#", id );

            ls = connMgr.executeQuery( sql );
            System.out.println( sql );
            
            while ( ls.next() ) {
            	useridList.add( ls.getString("userid") );
            }
            
        }
        catch( IOException ioe )
        {
           ioe.printStackTrace();
           ErrorManager.getErrorStackTrace(ioe);
           throw new IOException( ioe.getMessage());
        }
        catch ( Exception e )
        {
           e.printStackTrace();
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        
        
        return processTreeObjectForUsers( v_subj, v_year, v_subjseq, (String[]) useridList.toArray(new String[useridList.size()]) );      	
    }
    
    /**
     * ����ں� Ʈ����ü ������ ���̺? �����Ѵ�.
     * (������û�� emp_id�� String[]�� �޴´�.)
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean processTreeObjectForUsers( String v_subj, String v_year, String v_subjseq, String[] v_userids ) throws Exception
    {
        boolean result = false;
        
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager();
            
            sql = 
                "\n  SELECT course_code, org_id " +
                "\n  FROM                       " +
                "\n      tz_subj_contents       " +
                "\n  WHERE 1=1                  " +
                "\n      AND subj = ':subj'     ";

            sql = sql.replaceAll( ":subj", v_subj );

            ls = connMgr.executeQuery( sql );
            
            String courseCode = "";
            String orgID = "";
            
            while ( ls.next() )
            {
                courseCode = ls.getString( "course_code" );
                orgID =  ls.getString( "org_id");

                // ������ �ȵǾ� ������ false ��ȯ.
                if ( courseCode == null || orgID == null || courseCode.equals("") || orgID.equals("") )
                {
                    result = false;
                    break;
                }
                
                for ( int i=0; i<v_userids.length; i++ )
                {
                    // Tree ��ü get
                    //Serialize the template activity tree for the selected course
                    SeqActivityTree mySeqActivityTree = new SeqActivityTree();
    
                    ScormBlobBean sbb = new ScormBlobBean();
                    mySeqActivityTree = (SeqActivityTree) sbb.selectTreeObjectInOrganizationTable( courseCode, orgID );
                    
                    if ( _Debug )
                    {
                        System.out.println( "## Tree Object In Org : ScormCourseBean  �б� ����" );
                    }
                    
                    // Set the student ID
                    mySeqActivityTree.setLearnerID( v_userids[i].trim() );
                    
                    String scope = mySeqActivityTree.getScopeID();
                    
                    // Get any global objectives identified in the manifest from the activity tree.
                    Vector theGobalObjectiveList = mySeqActivityTree.getGlobalObjectives();
                    
                    if ( theGobalObjectiveList != null )
                    {  
                        // Create Global Object
                        ADLSeqUtilities.createGlobalObjs( v_userids[i].trim(), scope, theGobalObjectiveList );
                    }
                    
                    String lmsID = getLMSID( v_subj, v_year, v_subjseq );
                    sbb.insertTreeObject( lmsID, courseCode, orgID, v_userids[i].trim(), mySeqActivityTree, false );
    
                    if ( _Debug )
                    {
                        System.out.println( "## Tree Object : ScormCourseBean  ���� ����" );
                    }
                }
                
                result = true;
            }
        }
        catch( IOException ioe )
        {
           ioe.printStackTrace();
           ErrorManager.getErrorStackTrace(ioe);
           throw new IOException( ioe.getMessage());
        }
        catch ( Exception e )
        {
           e.printStackTrace();
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        
        
        return result;        
    }

    /**
     * ����ں� Ʈ����ü ������ ���̺? �����Ѵ�. 
     * ( reviewtype == "openedu" || reviewtype == "review" )
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean processTreeObjectForUser( RequestBox box ) throws Exception
    {
        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        
        String v_userid = box.getSession("userid");
        
        return processTreeObjectForUsers( v_subj, v_year, v_subjseq, new String[]{ v_userid } );
    }
    
    /**
     * ����ں� Ʈ����ü ������ ���̺? �����Ѵ�. 
     * ( reviewtype == "openedu" || reviewtype == "review" )
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean processTreeObjectReviewForUser( RequestBox box ) throws Exception
    {
        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");

        String courseCode = box.getString("p_course_code");
        String orgID = box.getString("p_org_id");
        String v_userid = box.getSession("userid");
        

        // Tree ��ü get
        //Serialize the template activity tree for the selected course
        SeqActivityTree mySeqActivityTree = new SeqActivityTree();

        ScormBlobBean sbb = new ScormBlobBean();
        mySeqActivityTree = (SeqActivityTree) sbb.selectTreeObjectInOrganizationTable( courseCode, orgID );
        
        if ( _Debug )
        {
            System.out.println( "## Tree Object In Org : ScormCourseBean  �б� ����" );
        }
        
        // Set the student ID
        mySeqActivityTree.setLearnerID( v_userid );
        
        String lmsID = getLMSID( v_subj, v_year, v_subjseq );
        sbb.insertTreeObjectReview( lmsID, courseCode, orgID, v_userid, mySeqActivityTree );

        if ( _Debug )
        {
            System.out.println( "## Tree Object : ScormCourseBean  ���� ����" );
        }
        
        return true;
    }

    /**
     * �ش� �⵵/����� Ʈ����ü ������ ���̺?�� �����Ѵ�.
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public int deleteTreeObjectForSubjseq(  RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String sql = "";
        int isOk = 0;
        
        try
        {
            connMgr = new DBConnectionManager();
            
            String v_subj = box.getString( "p_subj" );
            String v_year = box.getString( "p_year" );
            String v_subjseq = box.getString( "p_subjseq" );
            
            sql = 
                "\n  SELECT subj, YEAR, subjseq, course_code, org_id, userid    " +
                "\n  FROM tys_treeobject                                        " +
                "\n  WHERE 1 = 1                                                " +
                "\n      AND subj = ':subj'                                     " +
                "\n      AND YEAR = ':year'                                     " +
                "\n      AND subjseq = ':seq'                                   ";
            
            sql = sql.replaceAll( ":subj", v_subj );
            sql = sql.replaceAll( ":year", v_year );
            sql = sql.replaceAll( ":seq", v_subjseq );  
            
            ls = connMgr.executeQuery( sql );
            
            String courseCode = "";
            String orgID = "";
            String v_userid = "";
            
            while ( ls.next() )
            {
                courseCode = ls.getString( "course_code" );
                orgID = ls.getString( "org_id" );
                v_userid = ls.getString( "userid" );
                
                ScormBlobBean sbb = new ScormBlobBean();
                
                // GlobalObeject ����
                SeqActivityTree mySeqActivityTree = (SeqActivityTree) sbb.selectTreeObjectInOrganizationTable( courseCode, orgID );
                
                if ( _Debug )
                {
                    System.out.println( "## Tree Object In Org : ScormCourseBean  �б� ����" );
                }
                
                String scope = mySeqActivityTree.getScopeID();
                
                // Get any global objectives identified in the manifest from the activity tree.
                Vector theGobalObjectiveList = mySeqActivityTree.getGlobalObjectives();
                
                if ( theGobalObjectiveList != null )
                {  
                    // Delete Global Object
                    ADLSeqUtilities.deleteGlobalObjs( v_userid, scope, theGobalObjectiveList );
                }            
                
                if ( _Debug )
                {
                    System.out.println( "## Tree Object : ScormCourseBean  ���� ����" );
                }
            }
            
            sql = 
                "\n  DELETE                     " +
                "\n  FROM TYS_TREEOBJECT        " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ':subj'     " +
                "\n      AND YEAR = ':year'     " +
                "\n      AND SUBJSEQ = ':seq'   ";

            sql = sql.replaceAll( ":subj", v_subj );
            sql = sql.replaceAll( ":year", v_year );
            sql = sql.replaceAll( ":seq", v_subjseq );  

            isOk = connMgr.executeUpdate( sql );
        }
        catch( IOException ioe )
        {
            ioe.printStackTrace();
            
            ErrorManager.getErrorStackTrace(ioe);
            throw new IOException( ioe.getMessage());
        }
        catch ( Exception e )
        {
            e.printStackTrace();
    
            ErrorManager.getErrorStackTrace(e);
            throw new Exception( e.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        
        
        return isOk;
    }    
    
    /**
     * ����ں� Ʈ����ü ������ ���̺?�� �����Ѵ�.
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean deleteTreeObjectForUsers(  String v_subj, String v_year, String v_subjseq, String[] v_userids ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String sql = "";

        try
        {
            connMgr = new DBConnectionManager();
            
            sql = 
                "\n  SELECT course_code, org_id     " +
                "\n  FROM                           " +
                "\n      tz_subj_contents           " +
                "\n  WHERE 1=1                      " +
                "\n      AND subj = ':subj'         ";

            sql = sql.replaceAll( ":subj", v_subj );

            ls = connMgr.executeQuery( sql );
            
            String courseCode = "";
            String orgID = "";
            
            while ( ls.next() )
            {
                courseCode = ls.getString( "course_code" );
                orgID = ls.getString( "org_id" );

                ScormBlobBean sbb = new ScormBlobBean();
                
                for ( int i=0; i<v_userids.length; i++ )
                {
                    // GlobalObeject ����
                    SeqActivityTree mySeqActivityTree = (SeqActivityTree) sbb.selectTreeObjectInOrganizationTable( courseCode, orgID );
                    
                    if ( _Debug )
                    {
                        System.out.println( "## Tree Object In Org : ScormCourseBean  �б� ����" );
                    }
                    
                    String scope = mySeqActivityTree.getScopeID();
                    
                    // Get any global objectives identified in the manifest from the activity tree.
                    Vector theGobalObjectiveList = mySeqActivityTree.getGlobalObjectives();
                    
                    if ( theGobalObjectiveList != null )
                    {  
                        // Delete Global Object
                        ADLSeqUtilities.deleteGlobalObjs( v_userids[i].trim(), scope, theGobalObjectiveList );
                    }
                    
                    deleteTree( connMgr, v_subj, v_year, v_subjseq, courseCode, orgID, v_userids[i].trim() );
                    deleteDataModelObjectForUser( v_subj, v_year, v_subjseq, v_userids[i].trim() );
                    
                    if ( _Debug )
                    {
                        System.out.println( "## Tree Object : ScormCourseBean  ���� ����" );
                    }
                }
            }
        }
        catch( IOException ioe )
        {
            ioe.printStackTrace();
            
            ErrorManager.getErrorStackTrace(ioe);
            throw new IOException( ioe.getMessage());
        }
        catch ( Exception e )
        {
            e.printStackTrace();
    
            ErrorManager.getErrorStackTrace(e);
            throw new Exception( e.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        
        
        return true;
    }

    /**
     * ����ں� Ʈ����ü ������ ���̺?�� �����Ѵ�.
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean deleteTreeObjectForUser( RequestBox box ) throws Exception
    {
        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        
        String v_userid = box.getSession("userid");
        
        return deleteTreeObjectForUsers( v_subj, v_year, v_subjseq, new String[]{ v_userid }  );
    }
    
    /**
     * ��Ÿ�׽�Ʈ ������ ����
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public int deleteProgress( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;

        String sql = "";
        String sql1 = "";
        int result = 0;
        
        try 
        {
            // tz_beta_progress���� �� ����
            connMgr = new DBConnectionManager();

            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            
            String v_userid = box.getSession("userid");

            sql =
                "\n  DELETE                     " +
                "\n     FROM tz_beta_progress    " +
                "\n  WHERE 1 = 1                " +
                "\n     AND subj = ?            " +
                "\n     AND YEAR = ?            " +
                "\n     AND subjseq = ?         " +
                "\n     AND userid = ?          ";
            
            pstmt = connMgr.prepareStatement(sql);
            
            pstmt.setString( 1, v_subj );
            pstmt.setString( 2, v_year );
            pstmt.setString( 3, v_subjseq );
            pstmt.setString( 4, v_userid );
            
            result = pstmt.executeUpdate();
            

            sql1 = 
                "\n  SELECT course_code, org_id " +
                "\n  FROM                       " +
                "\n      tz_subj_contents       " +
                "\n  WHERE 1=1                  " +
                "\n      AND subj = ':subj'     ";

            sql1 = sql1.replaceAll( ":subj", v_subj );

            ls = connMgr.executeQuery( sql1 );
            
            String courseCode = "";
            String orgID = "";
            
            while ( ls.next() )
            {
                courseCode = ls.getString( "course_code" );
                orgID =  ls.getString( "org_id");

                // ������ �ȵǾ� ������ false ��ȯ.
                if ( courseCode == null || orgID == null || courseCode.equals("") || orgID.equals("") )
                {
                    return -1;
                }
  
                deleteTree( connMgr, v_subj, v_year, v_subjseq, courseCode, orgID, v_userid );

                if ( _Debug )
                {
                    System.out.println( "## Tree Object : ScormCourseBean  ���� ����" );
                }
            }
        }
        catch (IOException ioe) 
        {
            ErrorManager.getErrorStackTrace( ioe, box, sql );
            throw new Exception( ioe.getMessage() );
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace( ex, box, sql );
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return result;
    }

    private void deleteTree(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String courseCode, String orgID, String v_userid) 
        throws Exception
    {
        String sql =
            "\n  DELETE                                 " +
            "\n  FROM TYS_TREEOBJECT                    " +
            "\n  WHERE 1 = 1                            " +
            "\n      AND SUBJ = ':subj'                 " +
            "\n      AND COURSE_CODE = ':courseCode'    " +
            "\n      AND ORG_ID = ':orgID'              " +
            "\n      AND USERID = ':userid'             ";
        
        if ( !v_year.equals("") || !v_subjseq.equals("") )
        {
            sql += 
                "\n      AND SUBJSEQ = ':seq'               " +
                "\n      AND YEAR = ':year'                 ";
        }
        
        sql = sql.replaceAll( ":subj", v_subj );
        sql = sql.replaceAll( ":year", v_year );
        sql = sql.replaceAll( ":seq", v_subjseq );
        sql = sql.replaceAll( ":courseCode", courseCode );
        sql = sql.replaceAll( ":orgID", orgID );
        sql = sql.replaceAll( ":userid", v_userid );
        
        connMgr.executeUpdate( sql );
    }

    /**
     * ���̺? ����� ����ں� Tree ��ü�� �����Ѵ�.
     * ( review )
     * 
     * @param lmsID
     * @param courseID
     * @param orgID
     * @param userid
     * @param reviewtype
     * @return
     * @throws Exception
     */
    private int deleteTreeObjectReview( String userid ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        
        String sql = "";
        int isOk = 0;

        try 
        {
            connMgr = new DBConnectionManager();

            sql = 
                "\n  DELETE                     " +
                "\n  FROM TYS_TREEOBJECT_REVIEW " +
                "\n  WHERE 1 = 1                " +
                "\n      AND USERID = ?         ";

            pstmt = connMgr.prepareStatement( sql );
            
            pstmt.setString( 1, userid );
            
            isOk = pstmt.executeUpdate();
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

        return isOk;
    }    
    
    public int deleteDataModelObjectForSubjseq( RequestBox box ) throws Exception
    {
        String v_subj = box.getString( "p_subj" );
        String v_year = box.getString( "p_year" );
        String v_subjseq = box.getString( "p_subjseq" );

        return deleteDataModelObjectForSubjseq( v_subj, v_year, v_subjseq );
    }    
    
    /**
     * ���̺? ����� ���� ����� DataModel ��ü�� �����Ѵ�.
     * 
     * @param lmsID
     * @param courseID
     * @param orgID
     * @param userid
     * @param reviewtype
     * @return
     * @throws Exception
     */
    public int deleteDataModelObjectForSubjseq( String subj, String year, String subjseq ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        
        String sql = "";
        int isOk = 0;

        try 
        {
            connMgr = new DBConnectionManager();

            sql = 
                "\n  DELETE                     " +
                "\n  FROM TYS_DATAMODELOBJECT   " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ?           " +
                "\n      AND YEAR = ?           " +
                "\n      AND SUBJSEQ = ?        ";

            pstmt = connMgr.prepareStatement( sql );
            
            pstmt.setString( 1, subj );
            pstmt.setString( 2, year );
            pstmt.setString( 3, subjseq );
            
            isOk = pstmt.executeUpdate();
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

        return isOk;
    }
    
    /**
     * ���̺? ����� ����� DataModel ��ü�� �����Ѵ�.
     * 
     * @param lmsID
     * @param courseID
     * @param orgID
     * @param userid
     * @param reviewtype
     * @return
     * @throws Exception
     */
    public int deleteDataModelObjectForUser( String subj, String year, String subjseq, String userid ) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        
        String sql = "";
        int isOk = 0;

        try 
        {
            connMgr = new DBConnectionManager();

            sql = 
                "\n  DELETE                     " +
                "\n  FROM TYS_DATAMODELOBJECT   " +
                "\n  WHERE 1 = 1                " +
                "\n      AND SUBJ = ?           " +
                "\n      AND YEAR = ?           " +
                "\n      AND SUBJSEQ = ?        " +
                "\n      AND USERID = ?         ";

            pstmt = connMgr.prepareStatement( sql );
            
            pstmt.setString( 1, subj );
            pstmt.setString( 2, year );
            pstmt.setString( 3, subjseq );
            pstmt.setString( 4, userid );
            
            isOk = pstmt.executeUpdate();
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

        return isOk;
    }      
    
    /**
     * �ش� ������ �⵵/��� ����Ʈ�� ��ȯ�Ѵ�
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public List selectSubjseqList( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        List list = null;
        String sql = "";
        
        try 
        {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            String v_subj = box.getString("p_subj");
            
            sql =
            	"\n  SELECT subj, YEAR, subjseq  " +
            	"\n    FROM tz_subjseq  " +
            	"\n  WHERE 1 = 1  " +
            	"\n    AND subj = '#subj#'  " +
            	"\n  ORDER BY YEAR desc, subjseq desc ";            

            sql = sql.replaceAll( "#subj#", v_subj );

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
    
    private String getLMSID( String v_subj, String v_year, String v_subjseq ) 
    {
        return v_subj + "_" + v_year + "_" + v_subjseq;
    }

	public void processSeqInfoForUser(String string, String string2,
			String string3, String string4, String string5) {
		// TODO Auto-generated method stub
		
	}
}
