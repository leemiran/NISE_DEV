// **********************************************************
//  1. f      ��: �λ�DB �˻�
//  2. �wα׷���: MemberSearchBean.java
//  3. ��      ��: �λ�DB �˻�
//  4. ȯ      ��: JDK 1.4
//  5. ��      o: 1.0
//  6. ��      ��:  2004. 12. 20
//  7. ��      d:
// **********************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class BLStatisticsBean {

    // private
    private ConfigSet config;
    private int         row;
    
    public BLStatisticsBean() {
    }

    public static String getOrganization(RequestBox box, boolean isChange,
            boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        String result = "v�� ";

        try {
            String p_orgcode = box.getStringDefault("p_orgcode", "ALL"); // ��0���

            connMgr = new DBConnectionManager();

            sql = "select orgcode, title ";
            sql += " from tz_organization ";
            sql += " order by title";

            pstmt = connMgr.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);

            result += getSelectTag(ls, isChange, isALL, "p_orgcode", p_orgcode);
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e1) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }
        return result;
    }

    public static String getSelectTag(ListSet ls, boolean isChange,
            boolean isALL, String selname, String optionselected)
            throws Exception {
        StringBuffer sb = null;
        boolean isSelected = false;
        String v_tmpselname = "";

        try {
            sb = new StringBuffer();

            if (selname.equals("s_damungyyyy")) {
                v_tmpselname = "p_orgcode";
                sb.append("<select name = \"" + v_tmpselname + "\"");
            } else {
                sb.append("<select name = \"" + selname + "\"");
            }
            if (isChange)
                sb.append(" onChange = \"whenSelection('change')\"");
            sb.append(" > \r\n");
            if (isALL) {
                sb.append("<option value = \"ALL\" > ALL</option > \r\n");
            } else if (isChange) {
                if (selname.indexOf("year") == -1)
                    sb
                            .append("<option value = \"----\" >== ���� == </option > \r\n");
            }

            while (ls.next()) {
                ResultSetMetaData meta = ls.getMetaData();
                int columnCount = meta.getColumnCount();

                sb.append("<option value = \"" + ls.getString(1) + "\"");
                if (optionselected.equals(ls.getString(1)) && !isSelected) {
                    sb.append(" selected");
                    isSelected = true;
                } else if (selname.equals("s_gyear")
                        && ls.getString("gyear").equals(
                                FormatDate.getDate("yyyy"))
                        && optionselected.equals("") && !isSelected) { // ����ڵ������
                                                                        // ����
                    sb.append(" selected");
                    isSelected = true;
                }

                sb
                        .append(" > " + ls.getString(columnCount)
                                + "</option > \r\n");
            }
            sb.append("</select > \r\n");
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage());
        }
        return sb.toString();
    }

    /**
    ��d�� ��0����
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList performStatistics_03_01_L(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls1             = null;
        ArrayList           list1           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        DataBox             dbox            = null;
        
        String              v_Bcourse       = ""; // �����ڽ�
        String              v_course        = ""; // �����ڽ�
        String              v_Bcourseseq    = ""; // �����ڽ����
        String              v_courseseq     = ""; // �����ڽ����
        
        int                 v_pageno        = box.getInt            ( "p_pageno"                );
        String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" );  // ��0�׷�
        String              ss_blcourseyear = box.getStringDefault  ( "s_blcourseyear"  , "ALL" );  // �⵵
        String              ss_blcourseseq  = box.getStringDefault  ( "s_blcourseseq"   , "ALL" );  // ��� ���
        String              ss_branchcode   = box.getStringDefault  ( "s_branch"        , "ALL" );  // ��a
        String              ss_blcourse     = box.getStringDefault  ( "s_blcourse"      , "ALL" );  // ���&�ڽ�
        String              ss_action       = box.getString         ( "s_action"                );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"       , "ALL" );  // v���ڵ�
        String              v_status        = box.getStringDefault  ( "p_status"        , "ALL" );  // ����
        
        if("K2".equals(box.getSession("gadmin"))) {
             ss_branchcode                  = box.getSession("branch");
        }

        String              v_orderColumn   = box.getString( "p_orderColumn"  );  // d���� �÷���
        String              v_orderType     = box.getString( "p_orderType"    );  // d���� ��
        
        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();
                
            sbSQL.append(" SELECT   v1.CourseCode                                                                                                   \n")
                 .append("      ,   DECODE(v1.CourseCode, NULL, '�Ѱ�', v1.CourseNm) CourseNm                                                       \n")
                 .append("      ,   v1.CourseYear                                                                                                   \n")
                 .append("      ,   v1.CourseSeq                                                                                                    \n")
                 .append("      ,   v1.GradCnt                                                                                                      \n")
                 .append("      ,   v1.NoGradCnt                                                                                                    \n")
                 .append("      ,   v1.EduCnt                                                                                                       \n")
                 .append("      ,   ROUND(DECODE(v1.EduCnt, 0, 0, (v1.GradCnt / v1.EduCnt)), 2) * 100   GradRate                                    \n")
                 .append(" FROM     (                                                                                                               \n")
                 .append("              SELECT   v.CourseCode                                                                                       \n")
                 .append("                     , MAX(v.CourseNm)    CourseNm                                                                        \n");

            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("                  , v.CourseSeq                                                                                       \n");
            } else {
                sbSQL.append("                  , MAX(v.CourseSeq)   CourseSeq                                                                      \n");
            }
                 
            sbSQL.append("                     , MAX(v.CourseYear)  CourseYear                                                                      \n")
                 .append("                     , SUM(DECODE(v.isgraduated, 'Y', 1, 0))  GradCnt                                                     \n")
                 .append("                     , SUM(DECODE(v.isgraduated, NULL, 1, 'N', 1, 0))  NoGradCnt                                          \n")
                 .append("                     , COUNT(*)                           EduCnt                                                          \n")
                 .append("              FROM   (                                                                                                    \n")
                 .append("                          SELECT  a.coursecode                                                                            \n")
                 .append("                              ,   b.coursenm                                                                              \n")
                 .append("                              ,   a.courseseq                                                                             \n")
                 .append("                              ,   a.courseyear                                                                            \n")
                 .append("                              ,   c.isgraduated                                                                           \n")
                 .append("                              ,   c.BranchCode                                                                            \n")
                 .append("                              ,   c.userid                                                                                \n")
                 .append("                              ,   c.status                                                                                \n")               
                 .append("                          FROM    tz_blcourseseq  a                                                                       \n")
                 .append("                              ,   tz_bl_course    b                                                                       \n")
                 .append("                              ,   tz_blstudent    c                                                                       \n");
                 
            if ( !v_orgcode.equals("ALL") ) { 
                sbSQL.append("                              ,   vz_orgmember    d                                                                   \n");
            }
            
            sbSQL.append("                          WHERE   a.coursecode    = b.coursecode                                                          \n");
                 
            if ( !ss_grcode.equals("ALL") ) { 
                sbSQL.append("                          AND     a.grcode        = " + StringManager.makeSQL(ss_grcode       )   + "                     \n");
            }
            
            if ( !ss_blcourseyear.equals("ALL") ) { 
                sbSQL.append("                          AND     a.courseyear    = " + StringManager.makeSQL(ss_blcourseyear )   + "                     \n");
            }            
            
            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseCode    = " + StringManager.makeSQL(ss_blcourse     )   + "                     \n");
            }
            
            if ( !ss_blcourseseq.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseSeq     = " + StringManager.makeSQL(ss_blcourseseq  )   + "                     \n");
            }
            
            if ( !ss_branchcode.equals("ALL") ) { 
                sbSQL.append("                          AND     c.branchcode    = " + StringManager.makeSQL(ss_branchcode   )   + "                     \n");
            }

            sbSQL.append("                          AND     a.CourseCode    = c.CourseCode                                                          \n")
                 .append("                          AND     a.CourseYear    = c.CourseYear                                                          \n")
                 .append("                          AND     a.CourseSeq     = c.CourseSeq                                                           \n");
                 
            if ( v_status.equals("A") ) { 
                 sbSQL.append("                          AND     c.STATUS        < 'G'                                                              \n");
            } else if ( v_status.equals("G") ) {
                sbSQL.append("                          AND     c.STATUS         >= 'G'                                                             \n");
            }    
            
            if ( !v_orgcode.equals("ALL") ) { 
                sbSQL.append("                          AND     d.orgcode       = " + v_orgcode                                 + "                 \n")
                     .append("                          AND     c.userid        = d.userid(+)                                                       \n");
            }
            
            sbSQL.append("                      )       v                                                                                           \n");
                 
            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("              GROUP BY ROLLUP((v.CourseCode, v.Courseseq))                                                            \n");
            } else {
                sbSQL.append("              GROUP BY ROLLUP(v.CourseCode)                                                                           \n");
            }
                 
            sbSQL.append("          )   v1                                                                                                          \n");
            
            if ( v_orderColumn.equals("") ) {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, CourseCode , CourseNm                                                   \n");
            } else {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, " + v_orderColumn + v_orderType + "  \n");
            }
            
            ls1 = connMgr.executeQuery(sbSQL.toString());

            while ( ls1.next() ) {
                dbox = ls1.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list1;
    }
    
     
    /**
    ��d�� �����Ȳ
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList performStatistics_03_02_L(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls1             = null;
        ArrayList           list1           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        DataBox             dbox            = null;
        
        String              v_Bcourse       = ""; // �����ڽ�
        String              v_course        = ""; // �����ڽ�
        String              v_Bcourseseq    = ""; // �����ڽ����
        String              v_courseseq     = ""; // �����ڽ����
        
        int                 v_pageno        = box.getInt            ( "p_pageno"                );
        String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" );  // ��0�׷�
        String              ss_blcourseyear = box.getStringDefault  ( "s_blcourseyear"  , "ALL" );  // �⵵
        String              ss_blcourseseq  = box.getStringDefault  ( "s_blcourseseq"   , "ALL" );  // ��� ���
        String              ss_branchcode   = box.getStringDefault  ( "s_branch"        , "ALL" );  // ��a
        String              ss_blcourse     = box.getStringDefault  ( "s_blcourse"      , "ALL" );  // ���&�ڽ�
        String              ss_action       = box.getString         ( "s_action"                );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"       , "ALL" );  // v���ڵ�
        String              v_status        = box.getStringDefault  ( "p_status"        , "ALL" );  // ����
        
        if("K2".equals(box.getSession("gadmin"))) {
            ss_branchcode                  = box.getSession("branch");
        }

        String              v_orderColumn   = box.getString( "p_orderColumn"  );  // d���� �÷���
        String              v_orderType     = box.getString( "p_orderType"    );  // d���� ��
        
        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();
                
            sbSQL.append(" SELECT   v1.CourseCode                                                                                                   \n")
                 .append("      ,   DECODE(v1.CourseCode, NULL, '�Ѱ�', v1.CourseNm) CourseNm                                                       \n")
                 .append("      ,   v1.CourseYear                                                                                                   \n")
                 .append("      ,   v1.CourseSeq                                                                                                    \n")
                 .append("      ,   v1.GradCnt                                                                                                      \n")
                 .append("      ,   v1.NoGradCnt                                                                                                    \n")
                 .append("      ,   v1.EduCnt                                                                                                       \n")
                 .append("      ,   ROUND(DECODE(v1.EduCnt, 0, 0, (v1.GradCnt / v1.EduCnt)), 2) * 100   GradRate                                    \n")
                 .append(" FROM     (                                                                                                               \n")
                 .append("              SELECT   v.CourseCode                                                                                       \n")
                 .append("                     , MAX(v.CourseNm)    CourseNm                                                                        \n");

            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("                  , v.CourseSeq                                                                                       \n");
            } else {
                sbSQL.append("                  , MAX(v.CourseSeq)   CourseSeq                                                                      \n");
            }
                 
            sbSQL.append("                     , MAX(v.CourseYear)  CourseYear                                                                      \n")
                 .append("                     , SUM(DECODE(v.isgraduated, 'Y', 1, 0))  GradCnt                                                     \n")
                 .append("                     , SUM(DECODE(v.isgraduated, NULL, 1, 'N', 1, 0))  NoGradCnt                                          \n")
                 .append("                     , COUNT(*)                           EduCnt                                                          \n")
                 .append("              FROM   (                                                                                                    \n")
                 .append("                          SELECT  a.coursecode                                                                            \n")
                 .append("                              ,   b.coursenm                                                                              \n")
                 .append("                              ,   a.courseseq                                                                             \n")
                 .append("                              ,   a.courseyear                                                                            \n")
                 .append("                              ,   c.isgraduated                                                                           \n")
                 .append("                              ,   c.BranchCode                                                                            \n")
                 .append("                              ,   c.userid                                                                                \n")
                 .append("                              ,   c.status                                                                                \n")               
                 .append("                          FROM    tz_blcourseseq  a                                                                       \n")
                 .append("                              ,   tz_bl_course    b                                                                       \n")
                 .append("                              ,   tz_blstudent    c                                                                       \n");
                 
            if ( !v_orgcode.equals("ALL") ) { 
                sbSQL.append("                              ,   vz_orgmember    d                                                                   \n");
            }
            
            sbSQL.append("                          WHERE   a.coursecode    = b.coursecode                                                          \n");
                 
            if ( !ss_grcode.equals("ALL") ) { 
                sbSQL.append("                          AND     a.grcode        = " + StringManager.makeSQL(ss_grcode       )   + "                     \n");
            }
            
            if ( !ss_blcourseyear.equals("ALL") ) { 
                sbSQL.append("                          AND     a.courseyear    = " + StringManager.makeSQL(ss_blcourseyear )   + "                     \n");
            }            
            
            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseCode    = " + StringManager.makeSQL(ss_blcourse     )   + "                     \n");
            }
            
            if ( !ss_blcourseseq.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseSeq     = " + StringManager.makeSQL(ss_blcourseseq  )   + "                     \n");
            }
            
            if ( !ss_branchcode.equals("ALL") ) { 
                sbSQL.append("                          AND     c.branchcode    = " + StringManager.makeSQL(ss_branchcode   )   + "                     \n");
            }

            sbSQL.append("                          AND     a.CourseCode    = c.CourseCode                                                          \n")
                 .append("                          AND     a.CourseYear    = c.CourseYear                                                          \n")
                 .append("                          AND     a.CourseSeq     = c.CourseSeq                                                           \n");
                 
            if ( v_status.equals("A") ) { 
                sbSQL.append("                          AND     c.STATUS        < 'G'                                                              \n");
           } else if ( v_status.equals("G") ) {
               sbSQL.append("                          AND     c.STATUS         >= 'G'                                                             \n");
           }    
            
            if ( !v_orgcode.equals("ALL") ) { 
                sbSQL.append("                          AND     d.orgcode       = " + v_orgcode                                 + "                 \n")
                     .append("                          AND     c.userid        = d.userid(+)                                                       \n");
            }
            
            sbSQL.append("                      )       v                                                                                           \n");
                 
            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("              GROUP BY ROLLUP((v.CourseCode, v.Courseseq))                                                            \n");
            } else {
                sbSQL.append("              GROUP BY ROLLUP(v.CourseCode)                                                                           \n");
            }
                 
            sbSQL.append("          )   v1                                                                                                          \n");
            
            if ( v_orderColumn.equals("") ) {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, CourseCode , CourseNm                                                   \n");
            } else {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, " + v_orderColumn + v_orderType + "  \n");
            }
            
            ls1 = connMgr.executeQuery(sbSQL.toString());

            while ( ls1.next() ) {
                dbox = ls1.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list1;
    }
    
    
    /**
    ��d�� �����Ȳ
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList performStatistics_03_03_L(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls1             = null;
        ArrayList           list1           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        DataBox             dbox            = null;
        
        String              v_Bcourse       = ""; // �����ڽ�
        String              v_course        = ""; // �����ڽ�
        String              v_Bcourseseq    = ""; // �����ڽ����
        String              v_courseseq     = ""; // �����ڽ����
        
        int                 v_pageno        = box.getInt            ( "p_pageno"                );
        String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" );  // ��0�׷�
        String              ss_blcourseyear = box.getStringDefault  ( "s_blcourseyear"  , "ALL" );  // �⵵
        String              ss_blcourseseq  = box.getStringDefault  ( "s_blcourseseq"   , "ALL" );  // ��� ���
        String              ss_branchcode   = box.getStringDefault  ( "s_branch"        , "ALL" );  // ��a
        String              ss_blcourse     = box.getStringDefault  ( "s_blcourse"      , "ALL" );  // ���&�ڽ�
        String              ss_action       = box.getString         ( "s_action"                );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"       , "ALL" );  // v���ڵ�
        
        if("K2".equals(box.getSession("gadmin"))) {
            ss_branchcode                  = box.getSession("branch");
        }

        String              v_orderColumn   = box.getString( "p_orderColumn"  );  // d���� �÷���
        String              v_orderType     = box.getString( "p_orderType"    );  // d���� ��
        
        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();
                
            sbSQL.append(" SELECT   v1.CourseCode                                                                                                   \n")
                 .append("      ,   DECODE(v1.CourseCode, NULL, '�Ѱ�', v1.CourseNm) CourseNm                                                       \n")
                 .append("      ,   v1.CourseYear                                                                                                   \n")
                 .append("      ,   v1.CourseSeq                                                                                                    \n")
                 .append("      ,   v1.GradCnt                                                                                                      \n")
                 .append("      ,   v1.NoGradCnt                                                                                                    \n")
                 .append("      ,   v1.EduCnt                                                                                                       \n")
                 .append("      ,   ROUND(DECODE(v1.EduCnt, 0, 0, (v1.GradCnt / v1.EduCnt)), 2) * 100   GradRate                                    \n")
                 .append(" FROM     (                                                                                                               \n")
                 .append("              SELECT   v.CourseCode                                                                                       \n")
                 .append("                     , MAX(v.CourseNm)    CourseNm                                                                        \n")
                 .append("                     , MAX(v.CourseSeq)   CourseSeq                                                                       \n")
                 .append("                     , MAX(v.CourseYear)  CourseYear                                                                      \n")
                 .append("                     , SUM(DECODE(v.isgraduated, 'Y', 1, 0))  GradCnt                                                     \n")
                 .append("                     , SUM(DECODE(v.isgraduated, 'N', 1, 0))  NoGradCnt                                                   \n")
                 .append("                     , COUNT(*)                           EduCnt                                                          \n")
                 .append("              FROM   (                                                                                                    \n")
                 .append("                          SELECT  a.coursecode                                                                            \n")
                 .append("                              ,   b.coursenm                                                                              \n")
                 .append("                              ,   a.courseseq                                                                             \n")
                 .append("                              ,   a.courseyear                                                                            \n")
                 .append("                              ,   c.isgraduated                                                                           \n")
                 .append("                              ,   c.BranchCode                                                                            \n")
                 .append("                              ,   c.userid                                                                                \n")
                 .append("                              ,   c.status                                                                                \n")               
                 .append("                          FROM    tz_blcourseseq  a                                                                       \n")
                 .append("                              ,   tz_bl_course    b                                                                       \n")
                 .append("                              ,   tz_blstudent    c                                                                       \n")
                 .append("                              ,   vz_orgmember    d                                                                       \n")
                 .append("                          WHERE   a.coursecode    = b.coursecode                                                          \n");
                 
            if ( !ss_grcode.equals("ALL") ) { 
                sbSQL.append("                          AND     a.grcode        = " + StringManager.makeSQL(ss_grcode       )   + "                     \n");
            }
            
            if ( !ss_blcourseyear.equals("ALL") ) { 
                sbSQL.append("                          AND     a.courseyear    = " + StringManager.makeSQL(ss_blcourseyear )   + "                     \n");
            }            
            
            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseCode    = " + StringManager.makeSQL(ss_blcourse     )   + "                     \n");
            }
            
            if ( !ss_blcourseseq.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseSeq     = " + StringManager.makeSQL(ss_blcourseseq  )   + "                     \n");
            }
            
            if ( !v_orgcode.equals("ALL") ) { 
                sbSQL.append("                          AND     d.orgcode       = " + v_orgcode                                 + "                     \n");
            }
            
            if ( !ss_branchcode.equals("ALL") ) { 
                sbSQL.append("                          AND     c.branchcode    = " + StringManager.makeSQL(ss_branchcode   )   + "                     \n");
            }

            sbSQL.append("                          AND     a.CourseCode    = c.CourseCode                                                          \n")
                 .append("                          AND     a.CourseYear    = c.CourseYear                                                          \n")
                 .append("                          AND     a.CourseSeq     = c.CourseSeq                                                           \n")
                 .append("                          AND     c.STATUS        >= 'G'                                                                  \n")
                 .append("                          AND     c.userid        = d.userid(+)                                                           \n")
                 .append("                      )       v                                                                                           \n")
                 .append("              GROUP BY ROLLUP(v.CourseCode)                                                                               \n")
                 .append("          )   v1                                                                                                          \n");
            
            if ( v_orderColumn.equals("") ) {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, CourseCode , CourseNm                                                   \n");
            } else {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, " + v_orderColumn + v_orderType + "  \n");
            }
            
            System.out.println("1111\n\n"+ sbSQL.toString());
            ls1 = connMgr.executeQuery(sbSQL.toString());

            while ( ls1.next() ) {
                dbox = ls1.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list1;
    }
    
    
    /**
    ��d�� �����Ȳ
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList performStatistics_03_04_L(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls1             = null;
        ArrayList           list1           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        DataBox             dbox            = null;
        
        String              v_Bcourse       = ""; // �����ڽ�
        String              v_course        = ""; // �����ڽ�
        String              v_Bcourseseq    = ""; // �����ڽ����
        String              v_courseseq     = ""; // �����ڽ����
        
        int                 v_pageno        = box.getInt            ( "p_pageno"                );
        String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" );  // ��0�׷�
        String              ss_blcourseyear = box.getStringDefault  ( "s_blcourseyear"  , "ALL" );  // �⵵
        String              ss_blcourseseq  = box.getStringDefault  ( "s_blcourseseq"   , "ALL" );  // ��� ���
        String              ss_branchcode   = box.getStringDefault  ( "s_branch"        , "ALL" );  // ��a
        String              ss_blcourse     = box.getStringDefault  ( "s_blcourse"      , "ALL" );  // ���&�ڽ�
        String              ss_action       = box.getString         ( "s_action"                );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"       , "ALL" );  // v���ڵ�
        String              v_status        = box.getStringDefault  ( "p_status"        , "ALL" );  // ����
        
        if("K2".equals(box.getSession("gadmin"))) {
            ss_branchcode                  = box.getSession("branch");
        }

        String              v_orderColumn   = box.getString( "p_orderColumn"  );  // d���� �÷���
        String              v_orderType     = box.getString( "p_orderType"    );  // d���� ��
        
        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();
                
            sbSQL.append(" SELECT   v1.CourseCode                                                                                                   \n")
                 .append("      ,   DECODE(v1.CourseCode, NULL, '�Ѱ�', v1.CourseNm) CourseNm                                                       \n")
                 .append("      ,   v1.CourseYear                                                                                                   \n")
                 .append("      ,   v1.GradCnt                                                                                                      \n")
                 .append("      ,   v1.NoGradCnt                                                                                                    \n")
                 .append("      ,   v1.EduCnt                                                                                                       \n")
                 .append("      ,   ROUND(DECODE(v1.EduCnt, 0, 0, (v1.GradCnt / v1.EduCnt)), 2) * 100   GradRate                                    \n")
                 .append(" FROM     (                                                                                                               \n")
                 .append("              SELECT   v.CourseCode                                                                                       \n")
                 .append("                     , MAX(v.CourseNm)    CourseNm                                                                        \n");

            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("                  , v.CourseSeq                                                                                       \n");
            } else {
                sbSQL.append("                  , MAX(v.CourseSeq)   CourseSeq                                                                      \n");
            }
                 
            sbSQL.append("                     , MAX(v.CourseYear)  CourseYear                                                                      \n")
                 .append("                     , SUM(DECODE(v.isgraduated, 'Y', 1, 0))  GradCnt                                                     \n")
                 .append("                     , SUM(DECODE(v.isgraduated, NULL, 1, 'N', 1, 0))  NoGradCnt                                          \n")
                 .append("                     , COUNT(*)                           EduCnt                                                          \n")
                 .append("              FROM   (                                                                                                    \n")
                 .append("                          SELECT  a.coursecode                                                                            \n")
                 .append("                              ,   b.coursenm                                                                              \n")
                 .append("                              ,   a.courseseq                                                                             \n")
                 .append("                              ,   a.courseyear                                                                            \n")
                 .append("                              ,   c.isgraduated                                                                           \n")
                 .append("                              ,   c.BranchCode                                                                            \n")
                 .append("                              ,   c.userid                                                                                \n")
                 .append("                              ,   c.status                                                                                \n")               
                 .append("                          FROM    tz_blcourseseq  a                                                                       \n")
                 .append("                              ,   tz_bl_course    b                                                                       \n")
                 .append("                              ,   tz_blstudent    c                                                                       \n");
                 
            if ( !v_orgcode.equals("ALL") ) { 
                sbSQL.append("                              ,   vz_orgmember    d                                                                   \n");
            }
            
            sbSQL.append("                          WHERE   a.coursecode    = b.coursecode                                                          \n");
                 
            if ( !ss_grcode.equals("ALL") ) { 
                sbSQL.append("                          AND     a.grcode        = " + StringManager.makeSQL(ss_grcode       )   + "                     \n");
            }
            
            if ( !ss_blcourseyear.equals("ALL") ) { 
                sbSQL.append("                          AND     a.courseyear    = " + StringManager.makeSQL(ss_blcourseyear )   + "                     \n");
            }            
            
            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseCode    = " + StringManager.makeSQL(ss_blcourse     )   + "                     \n");
            }
            
            if ( !ss_blcourseseq.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseSeq     = " + StringManager.makeSQL(ss_blcourseseq  )   + "                     \n");
            }
            
            if ( !ss_branchcode.equals("ALL") ) { 
                sbSQL.append("                          AND     c.branchcode    = " + StringManager.makeSQL(ss_branchcode   )   + "                     \n");
            }

            sbSQL.append("                          AND     a.CourseCode    = c.CourseCode                                                          \n")
                 .append("                          AND     a.CourseYear    = c.CourseYear                                                          \n")
                 .append("                          AND     a.CourseSeq     = c.CourseSeq                                                           \n");
                 
            if ( v_status.equals("A") ) { 
                sbSQL.append("                          AND     c.STATUS        < 'G'                                                              \n");
           } else if ( v_status.equals("G") ) {
               sbSQL.append("                          AND     c.STATUS         >= 'G'                                                             \n");
           }    
            
            if ( !v_orgcode.equals("ALL") ) { 
                sbSQL.append("                          AND     d.orgcode       = " + v_orgcode                                 + "                 \n")
                     .append("                          AND     c.userid        = d.userid(+)                                                       \n");
            }
            
            sbSQL.append("                      )       v                                                                                           \n");
                 
            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("              GROUP BY ROLLUP((v.CourseCode))                                                                         \n");
            } else {
                sbSQL.append("              GROUP BY ROLLUP(v.CourseCode)                                                                           \n");
            }
                 
            sbSQL.append("          )   v1                                                                                                          \n");
            
            if ( v_orderColumn.equals("") ) {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, CourseCode , CourseNm                                                   \n");
            } else {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, " + v_orderColumn + v_orderType + "  \n");
            }
            
            ls1 = connMgr.executeQuery(sbSQL.toString());

            while ( ls1.next() ) {
                dbox = ls1.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list1;
    }        
    
    
    /**
    ��d�� �����Ȳ
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList performStatistics_03_05_L(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls1             = null;
        ArrayList           list1           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        DataBox             dbox            = null;
        
        String              v_Bcourse       = ""; // �����ڽ�
        String              v_course        = ""; // �����ڽ�
        String              v_Bcourseseq    = ""; // �����ڽ����
        String              v_courseseq     = ""; // �����ڽ����
        
        int                 v_pageno        = box.getInt            ( "p_pageno"                );
        String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" );  // ��0�׷�
        String              ss_blcourseyear = box.getStringDefault  ( "s_blcourseyear"  , "ALL" );  // �⵵
        String              ss_blcourseseq  = box.getStringDefault  ( "s_blcourseseq"   , "ALL" );  // ��� ���
        String              ss_branchcode   = box.getStringDefault  ( "s_branch"        , "ALL" );  // ��a
        String              ss_blcourse     = box.getStringDefault  ( "s_blcourse"      , "ALL" );  // ���&�ڽ�
        String              ss_action       = box.getString         ( "s_action"                );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"       , "ALL" );  // v���ڵ�
        String              v_status        = box.getStringDefault  ( "p_status"        , "ALL" );  // ����
        String              c_sido          = box.getString         ( "c_sido"                  );
        String              c_gugun         = box.getString         ( "c_gugun"                 );
        String              c_gender        = box.getString         ( "c_gender"                );
        String              c_achievement   = box.getString         ( "c_achievement"           );
        String              c_age           = box.getString         ( "c_age"                   );
        
        if("K2".equals(box.getSession("gadmin"))) {
            ss_branchcode                  = box.getSession("branch");
        }

        String              v_orderColumn   = box.getString( "p_orderColumn"  );  // d���� �÷���
        String              v_orderType     = box.getString( "p_orderType"    );  // d���� ��
        
        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();
            

            sbSQL.append(" SELECT   v1.CourseCode                                                                                                   \n")
                 .append("      ,   DECODE(v1.CourseCode, NULL, '�Ѱ�', v1.CourseNm) CourseNm                                                       \n")
                 .append("      ,   v1.CourseYear                                                                                                   \n")
                 .append("      ,   v1.CourseSeq                                                                                                    \n")
                 .append("      ,   v1.GradCnt                                                                                                      \n")
                 .append("      ,   v1.NoGradCnt                                                                                                    \n")
                 .append("      ,   v1.EduCnt                                                                                                       \n")
                 .append("      ,   ROUND(DECODE(v1.EduCnt, 0, 0, (v1.GradCnt / v1.EduCnt)), 2) * 100   GradRate                                    \n");
            
            if ( !c_sido.equals("") ) {
                sbSQL.append("      ,   v1.sido                                                                              \n");
            }    

            if ( !c_age.equals("") ) {
                sbSQL.append("      ,   v1.age                                                                               \n");               
            }
            
            if ( !c_achievement.equals("") ) {
                sbSQL.append("      ,   v1.achievement                                                                       \n");               
            }
            
            if ( !c_gender.equals("") ) {
                sbSQL.append("      ,   v1.gender                                                                            \n");               
            }
            
            if ( !c_gugun.equals("") ) {
                sbSQL.append("      ,   v1.gugun                                                                             \n");               
            }
                 
            sbSQL.append(" FROM     (                                                                                                               \n")
                 .append("              SELECT   v.CourseCode                                                                                       \n")
                 .append("                     , v.CourseSeq        CourseSeq                                                                       \n");
                 
            if ( !c_sido.equals("") ) {
                sbSQL.append("                              ,   v.sido                                                                              \n");
            }    

            if ( !c_age.equals("") ) {
                sbSQL.append("                              ,   v.age                                                                               \n");               
            }
            
            if ( !c_achievement.equals("") ) {
                sbSQL.append("                              ,   v.achievement                                                                       \n");               
            }
            
            if ( !c_gender.equals("") ) {
                sbSQL.append("                              ,   v.gender                                                                            \n");               
            }
            
            if ( !c_gugun.equals("") ) {
                sbSQL.append("                              ,   v.gugun                                                                             \n");               
            }
            
            sbSQL.append("                     , MAX(v.CourseNm)    CourseNm                                                                        \n")
                 .append("                     , MAX(v.CourseYear)  CourseYear                                                                      \n")
                 .append("                     , SUM(DECODE(v.isgraduated, 'Y', 1, 0))  GradCnt                                                     \n")
                 .append("                     , SUM(DECODE(v.isgraduated, NULL, 1, 'N', 1, 0))  NoGradCnt                                          \n")
                 .append("                     , COUNT(*)                           EduCnt                                                          \n")
                 .append("              FROM   (                                                                                                    \n")
                 .append("                          SELECT  a.coursecode                                                                            \n")
                 .append("                              ,   b.coursenm                                                                              \n")
                 .append("                              ,   a.courseseq                                                                             \n")
                 .append("                              ,   a.courseyear                                                                            \n")
                 .append("                              ,   c.isgraduated                                                                           \n")
                 .append("                              ,   c.BranchCode                                                                            \n")
                 .append("                              ,   c.userid                                                                                \n")
                 .append("                              ,   c.status                                                                                \n");
                 
            if ( !c_sido.equals("") ) {
                sbSQL.append("                              ,   d.sido                                                                              \n");
            }    

            if ( !c_age.equals("") ) {
                sbSQL.append("                              ,   d.age                                                                               \n");               
            }
            
            if ( !c_achievement.equals("") ) {
                sbSQL.append("                              ,   d.achievement                                                                       \n");               
            }
            
            if ( !c_gender.equals("") ) {
                sbSQL.append("                              ,   d.gender                                                                            \n");               
            }
            
            if ( !c_gugun.equals("") ) {
                sbSQL.append("                              ,   d.gugun                                                                             \n");               
            }
                 
            sbSQL.append("                          FROM    tz_blcourseseq  a                                                                       \n")
                 .append("                              ,   tz_bl_course    b                                                                       \n")
                 .append("                              ,   tz_blstudent    c                                                                       \n");
            
            if ( (!c_sido.equals("") || !c_age.equals("") || !c_achievement.equals("") || !c_gender.equals("") || !c_gugun.equals("")) && !v_orgcode.equals("ALL")  ) { 
                sbSQL.append("                              ,   vz_orgmember    d                                                                   \n");
            } else if ( (!c_sido.equals("") || !c_age.equals("") || !c_achievement.equals("") || !c_gender.equals("") || !c_gugun.equals("")) && v_orgcode.equals("ALL")  ) {
                sbSQL.append("                              ,   vz_orgmember1   d                                                                   \n");
            }
            
            if ( !box.getString("p_sdate").equals("") || !box.getString("p_ldate").equals("") ) {
                sbSQL.append("                              ,   tz_member       e                                                                   \n");
            }
                 
            sbSQL.append("                          WHERE   a.coursecode    = b.coursecode                                                          \n");
                 
            if ( !ss_grcode.equals("ALL") ) { 
                sbSQL.append("                          AND     a.grcode        = " + StringManager.makeSQL(ss_grcode       )   + "                     \n");
            }
            
            if ( !ss_blcourseyear.equals("ALL") ) { 
                sbSQL.append("                          AND     a.courseyear    = " + StringManager.makeSQL(ss_blcourseyear )   + "                     \n");
            }            
            
            if ( !ss_blcourse.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseCode    = " + StringManager.makeSQL(ss_blcourse     )   + "                     \n");
            }
            
            if ( !ss_blcourseseq.equals("ALL") ) { 
                sbSQL.append("                          AND     a.CourseSeq     = " + StringManager.makeSQL(ss_blcourseseq  )   + "                     \n");
            }
            
            if ( !ss_branchcode.equals("ALL") ) { 
                sbSQL.append("                          AND     c.branchcode    = " + StringManager.makeSQL(ss_branchcode   )   + "                     \n");
            }
            
            if ( v_status.equals("A") ) { 
                sbSQL.append("                          AND     c.STATUS        < 'G'                                                              \n");
            } else if ( v_status.equals("G") ) {
               sbSQL.append("                          AND     c.STATUS         >= 'G'                                                             \n");
            }    
            
            if ( (!c_sido.equals("") || !c_age.equals("") || !c_achievement.equals("") || !c_gender.equals("") || !c_gugun.equals("") || !v_orgcode.equals("ALL") ) ) { 
                sbSQL.append("                          AND     c.userid        = d.userid(+)                                                       \n");
            }
            
            if ( !v_orgcode.equals("ALL") ) { 
                sbSQL.append("                          AND     d.orgcode       = " + v_orgcode                                 + "                 \n");
            }
            
            if ( !box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("") ) { // �Ⱓ����
                sbSQL.append("          AND c.userid    = e.userid                                                                                  \n");
                
                if ( box.getString("p_sdate").equals(box.getString("p_ldate")) ) {
                    sbSQL.append("          AND TO_CHAR(TO_DATE(e.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                } else {
                    sbSQL.append("          AND TO_CHAR(TO_DATE(e.indate, 'yyyymmddhh24miss'), 'yyyymmdd') BETWEEN TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') AND TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_ldate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                }
            }

            sbSQL.append("                          AND     a.CourseCode    = c.CourseCode                                                          \n")
                 .append("                          AND     a.CourseYear    = c.CourseYear                                                          \n")
                 .append("                          AND     a.CourseSeq     = c.CourseSeq                                                           \n")
                 .append("                      )       v                                                                                           \n")
                 .append("              GROUP BY ROLLUP((v.CourseCode, v.CourseSeq " + (!c_sido.equals("") ? ", v.sido" : "") + (!c_achievement.equals("") ? ", v.achievement" : "") + (!c_gender.equals("") ? ", v.gender" : "") + (!c_gugun.equals("") ? ", v.gugun" : "") + (!c_age.equals("") ? ", v.age" : "") + ")) \n")
                 .append("          )   v1                                                                                                          \n");
            
            if ( v_orderColumn.equals("") ) {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, CourseCode , CourseNm                                                   \n");
            } else {
                sbSQL.append(" ORDER BY DECODE(CourseCode, NULL, 2, 1) ASC, " + v_orderColumn + v_orderType + "  \n");
            }
            
            System.out.println("sql : " + sbSQL.toString());
            
            ls1 = connMgr.executeQuery(sbSQL.toString());

            while ( ls1.next() ) {
                dbox = ls1.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list1;
    }
}