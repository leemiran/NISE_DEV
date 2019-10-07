/*
 * @(#)ScormStudyBean.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */
package com.ziaan.scorm2004;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.scorm2004.sequencer.ADLSequencer;
import com.ziaan.scorm2004.sequencer.ADLTOC;
import com.ziaan.scorm2004.sequencer.ADLValidRequests;
import com.ziaan.scorm2004.sequencer.SeqActivityTree;

/**
 * SCORM Study 관련 Bean
 *
 * @version 1.0 2006. 7. 20.
 * @author Jin-pil Chung
 *
 */
public class ScormStudyBean
{
    /**
     * 복습 할 수 있는 과정인지 여부 리턴
     * 
     * 등록된 컨텐츠 리스트를 반환한다.
     * 
     * @param box
     * @return ArrayList
     * @throws Exception
     */
    public boolean isStudentOld(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        
        boolean result = false;
        
        try 
        {
            connMgr = new DBConnectionManager();

            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String v_userid = box.getSession("userid");

            sql =
                "\n  SELECT subj                " +
                "\n  FROM tz_stold              " +
                "\n  WHERE 1=1                  " +
                "\n      AND subj = " + SQLString.Format( v_subj ) +
                "\n      AND YEAR = " + SQLString.Format( v_year ) +
                "\n      AND subjseq = " + SQLString.Format( v_subjseq ) +
                "\n      AND userid = " + SQLString.Format( v_userid );
            
            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() )
            {
                result = true;
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

        return result;
    }

    /**
     * 열린교육 여부인지를 반환한다
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public boolean isOpenEdu( RequestBox box ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        
        boolean result = false;
        
        try 
        {
            connMgr = new DBConnectionManager();

            String v_subj = box.getString("p_subj");

            sql =
                "\n  SELECT isopenedu       " +
                "\n  FROM tz_subj           " +
                "\n  WHERE subj = " + SQLString.Format( v_subj );
            
            ls = connMgr.executeQuery( sql );
            
            String isOpenEdu = "";
            if ( ls.next() )
            {
                isOpenEdu = ls.getString("isopenedu");
                
                if ( isOpenEdu.equals("Y") )
                {
                    result = true;
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

        return result;
    }
    
    /**
     * 과정의 컨텐츠 타입을 반환한다.
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public String selectContentType( RequestBox box ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        
        String contentType = "";
        
        try 
        {
            connMgr = new DBConnectionManager();
            
            String v_subj = box.getString("p_subj");

            sql =
                "\n  SELECT contenttype " +
                "\n  FROM tz_subj       " +
                "\n  WHERE subj = " + SQLString.Format( v_subj );  
            
            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() )
            {
                contentType = ls.getString("contenttype");
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

        return contentType;
    }


    /**
     * SCORM2004 목차 ( Level 1단계까지 보여줌)
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectLessonList( RequestBox box ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        ArrayList list = null;
        
        try 
        {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            String v_subj = box.getString("p_subj");

            sql =
                "\n  SELECT subj, course_code, org_id   " +
                "\n  FROM tz_subj_contents              " +
                "\n  WHERE subj = " + SQLString.Format( v_subj );
            
            ls = connMgr.executeQuery( sql );
            
            String courseCode = "";
            String orgID = "";
            
            if ( ls.next() )
            {
                courseCode = ls.getString("course_code");
                orgID = ls.getString("org_id");
            }
            
            ls.close();
            
            sql =
                "\n  SELECT                                             " +
                "\n     ROWNUM lesson, ITEM_TITLE sdesc                 " +
                "\n  FROM                                               " +
                "\n  (                                                  " +
                "\n     SELECT                                          " +
                "\n         ITEM_ID, ITEM_PID,ITEM_TITLE, B.TREE_ORD    " +
                "\n     FROM                                            " +
                "\n         TYS_ORGANIZATION A, TYS_ITEM B              " +
                "\n     WHERE                                           " +
                "\n         A.COURSE_CODE = ':course_code'              " +
                "\n         AND A.COURSE_CODE = B.COURSE_CODE           " +
                "\n         AND A.ORG_ID = B.ORG_ID                     " +
                "\n  ) T                                                " +
                "\n  where level=1                                      " +
                "\n  START WITH                                         " +
                "\n     ITEM_PID = ':org_id'                            " +
                "\n  CONNECT BY                                         " +
                "\n     PRIOR ITEM_ID = ITEM_PID                        " +
                "\n  ORDER SIBLINGS BY TREE_ORD                         ";
            
            sql = sql.replaceAll( ":course_code", courseCode );
            sql = sql.replaceAll( ":org_id", orgID );
            
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

    public List selectMappingInfo( RequestBox box ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        List mappingInfoList = null;
        
        try 
        {
            connMgr = new DBConnectionManager();
            mappingInfoList = new ArrayList();

            String v_subj = box.getString("p_subj");

            sql =
            	"\n  SELECT  " +
            	"\n    a.course_code, a.org_id, b.subjnm, c.org_title  " +
            	"\n  FROM  " +
            	"\n    tz_subj_contents a, tz_subj b, tys_organization c  " +
            	"\n  WHERE 1=1  " +
            	"\n    AND a.course_code = c.course_code  " +
            	"\n    AND a.org_id = c.org_id  " +
            	"\n    AND a.subj = b.subj  " +
            	"\n    AND a.subj =  " + SQLString.Format( v_subj ) +
            	"\n  ORDER BY a.ord  ";
            	
            ls = connMgr.executeQuery( sql );
            
            while ( ls.next() )
            {
                mappingInfoList.add( ls.getDataBox() );
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

        return mappingInfoList;
    }
    
    /**
     * 진도현황 - 목차 리스트
     * 
     * @param box
     * @return ArrayList
     * @throws Exception
     */
    public List selectProgressOrgList(RequestBox box) throws Exception
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
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String v_userid = box.getSession("userid");
            
            String v_courseCode = "";
            String v_orgID = "";
        
            String tableName = "";
            if ( v_subjseq.equals("0000") )
            {
                tableName = "tz_beta_progress";
            }
            else
            {
                tableName = "tz_progress";
            }
            
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
                    "\n  SELECT                                                                           " +
                    "\n     LEVEL, T.ORG_TREE_ORD, T.ORG_ID, T.ORG_TITLE, T.COURSE_CODE,                  " +
                    "\n     T.ITEM_ID, T.ITEM_PID, T.ITEM_ID_REF, T.ITEM_TITLE,                           " +
                    "\n     T.OBJ_ID, T.TREE_ORD, T.RES_SCORM_TYPE,                                       " +
                    "\n     P.LESSONSTATUS, P.FIRST_EDU, P.FIRST_END,                                     " +
                    "\n     P.SESSION_TIME, P.TOTAL_TIME, P.LESSON_COUNT                                  " +
                    "\n  FROM                                                                             " +
                    "\n  (                                                                                " +
                    "\n     SELECT                                                                        " +
                    "\n         A.TREE_ORD as ORG_TREE_ORD, A.ORG_ID, A.ORG_TITLE, A.COURSE_CODE,         " +
                    "\n         B.ITEM_ID, B.ITEM_PID, B.ITEM_ID_REF, B.ITEM_TITLE, B.OBJ_ID, B.TREE_ORD, " +
                    "\n         C.RES_SCORM_TYPE                                                          " +
                    "\n     FROM                                                                          " +
                    "\n         TYS_ORGANIZATION A, TYS_ITEM B, TYS_RESOURCE C                            " +
                    "\n     WHERE 1=1                                                                     " +
                    "\n         AND A.COURSE_CODE = ':course_code'                                        " +
                    "\n         AND B.ORG_ID = ':org_id'                                                  " +
                    "\n         AND A.COURSE_CODE = B.COURSE_CODE                                         " +
                    "\n         AND A.ORG_ID = B.ORG_ID                                                   " +
                    "\n         AND B.COURSE_CODE = C.COURSE_CODE(+)                                      " +
                    "\n         AND B.ORG_ID = C.ORG_ID(+)                                                " +
                    "\n         AND B.ITEM_ID = C.ITEM_ID(+)                                              " +
                    "\n  ) T,                                                                             " +
                    "\n  ( SELECT                                                                         " +
                    "\n        LESSONSTATUS, FIRST_EDU, FIRST_END, OID,                                   " +
                    "\n        SESSION_TIME, TOTAL_TIME, LESSON_COUNT                                     " +
                    "\n     FROM " + tableName +
                    "\n     WHERE 1=1                                                                     " +
                    "\n         AND SUBJ=':subj'                                                          " +
                    "\n         AND YEAR=':year'                                                          " +
                    "\n         AND SUBJSEQ=':seq'                                                        " +
                    "\n         AND LESSON = ':course_code' || '_' || ':org_id'                           " +
                    "\n         AND USERID=':userid'                                                      " +
                    "\n  ) P                                                                              " +
                    "\n  WHERE                                                                            " +
                    "\n      T.ITEM_ID = P.OID(+)                                                         " +
                    "\n  START WITH                                                                       " +
                    "\n     ITEM_PID = ':org_id'                                                          " +
                    "\n  CONNECT BY                                                                       " +
                    "\n     PRIOR ITEM_ID = ITEM_PID                                                      " +
                    "\n  ORDER SIBLINGS BY TREE_ORD                                                       ";

                    /*
                    "\n  SELECT                                                                                         " +
                    "\n     LEVEL, ORG_TREE_ORD, ORG_ID, ORG_TITLE, COURSE_CODE,                                        " +
                    "\n     ITEM_ID, ITEM_PID, ITEM_ID_REF, ITEM_PARAMETERS, ITEM_TITLE,                                " +
                    "\n     ITEM_META, OBJ_ID, TREE_ORD                                                                 " +
                    "\n  FROM                                                                                           " +
                    "\n  (                                                                                              " +
                    "\n     SELECT                                                                                      " +
                    "\n        A.TREE_ORD as ORG_TREE_ORD, A.ORG_ID, A.ORG_TITLE, A.COURSE_CODE,                        " +
                    "\n        B.ITEM_ID, B.ITEM_PID, B.ITEM_ID_REF, B.ITEM_PARAMETERS, B.ITEM_TITLE,                   " +
                    "\n        B.META_LOCATION ITEM_META, B.OBJ_ID, B.TREE_ORD                                          " +
                    "\n     FROM                                                                                        " +
                    "\n         TYS_ORGANIZATION A, TYS_ITEM B                                                          " +
                    "\n     WHERE                                                                                       " +
                    "\n         A.COURSE_CODE = ':course_code'                                                          " +
                    "\n         AND A.COURSE_CODE = B.COURSE_CODE                                                       " +
                    "\n         AND A.ORG_ID = B.ORG_ID                                                                 " +
                    "\n  ) T                                                                                            " +
                    "\n  START WITH                                                                                     " +
                    "\n     ITEM_PID = ':org_id'                                                                        " +
                    "\n  CONNECT BY                                                                                     " +
                    "\n     PRIOR ITEM_ID = ITEM_PID                                                                    " +
                    "\n  ORDER SIBLINGS BY TREE_ORD                                                                     ";                
                    */
                    
                sql = sql.replaceAll( ":subj", v_subj );
                sql = sql.replaceAll( ":year", v_year );
                sql = sql.replaceAll( ":seq", v_subjseq );
                sql = sql.replaceAll( ":userid", v_userid );
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

    public Map selectActivitiTreeMap(RequestBox box) throws Exception
    {
        Map activityTreeMap = new HashMap();
        
        String lmsID = box.getString("p_subj") + "_" + box.getString("p_year") + "_" + box.getString("p_subjseq");
        String courseID = "";
        String orgID = "";
        String userID = box.getSession("userid");
        
        List mappingInfo = selectMappingInfo(box);
        DataBox dbox = null;
        
        for ( int i=0; i<mappingInfo.size(); i++ ) 
        {
            dbox = (DataBox) mappingInfo.get(i);
            courseID = dbox.getString("d_course_code");
            orgID = dbox.getString("d_org_id");

            ScormBlobBean sbb = new ScormBlobBean();
            SeqActivityTree mactivityTree = (SeqActivityTree) sbb.selectTreeObject( lmsID, courseID, orgID, userID, "" );
            ADLSequencer msequencer = new ADLSequencer();
            msequencer.setActivityTree(mactivityTree);
            
            ADLValidRequests validRequests = new ADLValidRequests();
            msequencer.getValidRequests(validRequests);
            Vector TOCList = validRequests.mTOC;
            HashMap TOCMap = new HashMap();
            
            if ( TOCList != null ) 
            {
                for ( int j=0; j<TOCList.size(); j++ )
                {
                    ADLTOC tocItem =  (ADLTOC) TOCList.get(j);
                    TOCMap.put( tocItem.mID, tocItem.mTitle );
                }
            }
            
            activityTreeMap.put( orgID, TOCMap );
        }        
        
        return activityTreeMap;
    }
}
