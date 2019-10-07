package com.ziaan.library;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.lcms.EduScoreData;
import com.ziaan.lcms.EduScoreDataSub;
import com.ziaan.lcms.EduStartBean;
import com.ziaan.course.SubjGongAdminBean;
import com.ziaan.exam.ExamUserBean;
import com.ziaan.study.ProjectAdminBean;

public class AutoMailBean { 
    public static String TEMPLETID = "0078";
    public static String REPORTTEMPLETID = "0081";
        
    public AutoMailBean() { 
    }
    
    public String getMId() throws Exception { 
        DBConnectionManager connMgr     = null;
        DataBox             dbox        = null;
        ListSet             ls          = null;

        StringBuffer        sbSQL       = new StringBuffer("");
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        String              v_mid       = "1";
        
        try { 
            connMgr     = new DBConnectionManager();
            
            sbSQL.append(" SELECT  Tz_MailWorkInfo_MId_Seq.NextVal Mid                                  \n")
                 .append(" FROM    DUAL                                                                 \n");

            System.out.println(this.getClass().getName() + "." + "sendMailUserForSubjSeqList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls  = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
            }
            
            v_mid = dbox.getString("d_mid");
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return v_mid;
    }
    
    
    public String getPkseq() throws Exception { 
        DBConnectionManager connMgr     = null;
        DataBox             dbox        = null;
        ListSet             ls          = null;

        StringBuffer        sbSQL       = new StringBuffer("");
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        String              v_pkseq     = "1";
        
        
        try { 
            connMgr     = new DBConnectionManager();
            
            sbSQL.append(" SELECT  Tz_MailWorkInfo_Pk_Seq.NextVal Pkseq                                 \n")
                 .append(" FROM    DUAL                                                                 \n");

            System.out.println(this.getClass().getName() + "." + "sendMailUserForSubjSeqList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls  = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
            }
            
            v_pkseq = dbox.getString("d_pkseq");
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return v_pkseq;
    }
    
    
    public ArrayList sendBLMailUserForSubjSeqList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        DataBox             dbox        = null;
        ListSet             ls          = null;
        ArrayList           list        = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        StringBuffer        sbDual      = new StringBuffer("");
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              where_query = "";
        
        Vector              v_vchecks   = box.getVector("p_checks_bl");
        
        String              v_schecks   = "";
        String              v_union     = "";
        boolean             v_procunion = false;
        
        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            where_query = "('xxxx'";
            
            sbDual.append(" SELECT '', '', '', '' FROM DUAL \n");
        
            for ( int i = 0; i < v_vchecks.size(); i++ ) { 
                v_schecks           = (String)v_vchecks.elementAt(i);
                String [] v_svalue  = v_schecks.split(",");
                
                System.out.println("=======================1");                
                System.out.println("=======================s_value" + v_svalue);                
                for ( int j = 0 ; j < v_svalue.length; j++) {    
                    v_svalue[j]     = v_svalue[j].trim();
                }
System.out.println("=======================2");                
                
                
                sbDual.append(" UNION ALL       ");
                sbDual.append(" SELECT          ");
                
                for ( int j = 0 ; j < 4 ; j++) {
                    sbDual.append( (j == 0 ? "" : " , ") + SQLString.Format(v_svalue[j]) + " ");
                }
                
                sbDual.append(" FROM   DUAL     \n");       

                where_query = where_query + "," + SQLString.Format(v_svalue[0]) ;
                
                if ( i != 0 && i % 500 == 0 ) {
                    where_query += ")";
                    
                    sbSQL.append(" " + v_union + "                                                              \n")
                         .append(" SELECT  tm.userid                                                            \n")
                         .append("     ,   tm.name                                                              \n")
                         .append("     ,   tm.handphone                                                         \n")
                         .append("     ,   tm.email                                                             \n")
                         .append("     ,   NVL(vst.subj    , vso.subj   )  subj                                 \n")
                         .append("     ,   NVL(vst.subjseq , vso.subjseq)  subjseq                              \n")
                         .append("     ,   NVL(vst.year    , vso.year   )  year                                 \n")
                         .append("     ,   NVL(vst.subjnm  , vso.subjnm )  subjnm                               \n")
                         .append(" from    tz_member    tm                                                      \n")
                         .append("     ,   (                                                                    \n")
                         .append("       SELECT tst.UserId, tst.Subj, tst.Year, tst.Subjseq, tsj.Subjnm         \n")
                         .append("       FROM   tz_student tst                                                  \n")
                         .append("          ,   tz_subj    tsj                                                  \n")
                         .append("       WHERE  ( tst.UserId, tst.Subj, tst.Year, tst.Subjseq ) IN (            \n")
                         .append( sbDual.toString()                                                                 )
                         .append("                                                                 )            \n")
                         .append("       AND    tst.subj    = tsj.subj                                          \n")
                         .append("      )            vst                                                        \n")
                         .append("     ,   (                                                                    \n")
                         .append("       SELECT tso.UserId, tso.Subj, tso.Year, tso.Subjseq, tsj.Subjnm         \n")
                         .append("       FROM   tz_stold   tso                                                  \n")
                         .append("          ,   tz_subj    tsj                                                  \n")
                         .append("       WHERE  ( tso.UserId, tso.Subj, tso.Year, tso.Subjseq ) IN (            \n")
                         .append( sbDual.toString()                                                                 )
                         .append("                                                                 )            \n")
                         .append("       AND    tso.subj    = tsj.subj                                          \n")
                         .append("      )            vso                                                        \n")
                         .append(" where   tm.userid in " + where_query + "                                     \n")
                         .append(" and      tm.userid = vst.userid(+)                                           \n")
                         .append(" and      tm.userid = vso.userid(+)                                           \n")
                         .append(" and      NVL(tm.ismailling, 'Y') = 'Y'                                       \n");

                    v_union     = " UNION ALL \n";
                    where_query = " ('xxxx'     ";
                    
                    sbDual.setLength(0);
                    
                    sbDual.append(" SELECT '', '', '', '' FROM DUAL \n");
                    
                    v_procunion = true;
                } else {
                    v_procunion = false;
                }
            }
            
            if ( !v_procunion ) {
                where_query += ")";
                
                sbSQL.append(" " + v_union + "                                                              \n")
                     .append(" SELECT  tm.userid                                                            \n")
                     .append("     ,   tm.name                                                              \n")
                     .append("     ,   tm.handphone                                                         \n")
                     .append("     ,   tm.email                                                             \n")
                     .append("     ,   NVL(vst.subj    , vso.subj   )  subj                                 \n")
                     .append("     ,   NVL(vst.subjseq , vso.subjseq)  subjseq                              \n")
                     .append("     ,   NVL(vst.year    , vso.year   )  year                                 \n")
                     .append("     ,   NVL(vst.subjnm  , vso.subjnm )  subjnm                               \n")
                     .append(" from    tz_member    tm                                                      \n")
                     .append("     ,   (                                                                    \n")
                     .append("       SELECT tst.UserId, tst.Subj, tst.Year, tst.Subjseq, tsj.Subjnm         \n")
                     .append("       FROM   tz_student tst                                                  \n")
                     .append("          ,   tz_subj    tsj                                                  \n")
                     .append("       WHERE  ( tst.UserId, tst.Subj, tst.Year, tst.Subjseq ) IN (            \n")
                     .append( sbDual.toString()                                                                 )
                     .append("                                                                 )            \n")
                     .append("       AND    tst.subj    = tsj.subj                                          \n")
                     .append("      )            vst                                                        \n")
                     .append("     ,   (                                                                    \n")
                     .append("       SELECT tso.UserId, tso.Subj, tso.Year, tso.Subjseq, tsj.Subjnm         \n")
                     .append("       FROM   tz_stold   tso                                                  \n")
                     .append("          ,   tz_subj    tsj                                                  \n")
                     .append("       WHERE  ( tso.UserId, tso.Subj, tso.Year, tso.Subjseq ) IN (            \n")
                     .append( sbDual.toString()                                                                 )
                     .append("                                                                 )            \n")
                     .append("       AND    tso.subj    = tsj.subj                                          \n")
                     .append("      )            vso                                                        \n")
                     .append(" where   tm.userid in " + where_query + "                                     \n")
                     .append(" and      tm.userid = vst.userid(+)                                           \n")
                     .append(" and      tm.userid = vso.userid(+)                                           \n")
                     .append(" and      NVL(tm.ismailling, 'Y') = 'Y'                                       \n");
            }   
            
            System.out.println(this.getClass().getName() + "." + "sendMailUserForSubjSeqList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls  = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                list.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return list;
    }
    
    
    public ArrayList sendMailUserForSubjSeqList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        DataBox             dbox        = null;
        ListSet             ls          = null;
        ArrayList           list        = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        StringBuffer        sbDual      = new StringBuffer("");
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              where_query = "";
        
        Vector              v_vchecks   = box.getVector("p_checks");
        
        if ( v_vchecks.size() == 0 )
            v_vchecks                   = box.getVector("p_checks1");
        
        String              v_schecks   = "";
        String              v_union     = "";
        boolean             v_procunion = false;
        
        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            where_query = "('xxxx'";
            
            sbDual.append(" SELECT '', '', '', '' FROM DUAL \n");
        
            for ( int i = 0; i < v_vchecks.size(); i++ ) { 
                v_schecks           = (String)v_vchecks.elementAt(i);
                String [] v_svalue  = v_schecks.split(",");
                
                for ( int j = 0 ; j < v_svalue.length; j++ ) 
                    v_svalue[j] = v_svalue[j].trim();
                
                sbDual.append(" UNION ALL       ");
                sbDual.append(" SELECT          ");
                
                for ( int j = 0 ; j < 4 ; j++) {
                    sbDual.append( (j == 0 ? "" : " , ") + SQLString.Format(v_svalue[j]) + " ");
                }
                
                sbDual.append(" FROM   DUAL     \n");       

                where_query = where_query + "," + SQLString.Format(v_svalue[0]) ;
                
                if ( i != 0 && i % 500 == 0 ) {
                    where_query += ")";
                    
                    sbSQL.append(" " + v_union + "                                                                                   \n")
                         .append(" SELECT  v.userid                                                                                  \n")
                         .append("      ,  MAX(v.name     )     name                                                                 \n")
                         .append("      ,  MAX(v.handphone)     handphone                                                            \n")
                         .append("      ,  MAX(v.email    )     email                                                                \n")
                         .append("      ,  v.subj                                                                                    \n")
                         .append("      ,  v.subjseq                                                                                 \n")
                         .append("      ,  v.year                                                                                    \n")
                         .append("      ,  MAX(v.subjnm   )     subjnm                                                               \n")
                         .append(" FROM    (                                                                                         \n")
                         .append("              SELECT  tm.userid                                                                    \n")
                         .append("                  ,   tm.name                                                                      \n")
                         .append("                  ,   tm.handphone                                                                 \n")
                         .append("                  ,   tm.email                                                                     \n")
                         .append("                  ,   NVL(vst.subj    , vso.subj   )  subj                                         \n")
                         .append("                  ,   NVL(vst.subjseq , vso.subjseq)  subjseq                                      \n")
                         .append("                  ,   NVL(vst.year    , vso.year   )  year                                         \n")
                         .append("                  ,   NVL(vst.subjnm  , vso.subjnm )  subjnm                                       \n")
                         .append("              from    tz_member    tm                                                              \n")
                         .append("                  ,   (                                                                            \n")
                         .append("                        SELECT     tst.UserId, tst.Subj, tst.Year, tst.Subjseq, tsj.Subjnm         \n")
                         .append("                        FROM       tz_student tst                                                  \n")
                         .append("                               ,   tz_subj    tsj                                                  \n")
                         .append("                        WHERE      ( tst.UserId, tst.Subj, tst.Year, tst.Subjseq ) IN (            \n")
                         .append(                                        sbDual.toString()                                              )
                         .append("                                                                                       )           \n")
                         .append("                        AND        tst.subj    = tsj.subj                                          \n")
                         .append("                      )            vst                                                             \n")
                         .append("                  ,   (                                                                            \n")
                         .append("                        SELECT     tso.UserId, tso.Subj, tso.Year, tso.Subjseq, tsj.Subjnm         \n")
                         .append("                        FROM       tz_stold   tso                                                  \n")
                         .append("                           ,       tz_subj    tsj                                                  \n")
                         .append("                        WHERE          ( tso.UserId, tso.Subj, tso.Year, tso.Subjseq ) IN (        \n")
                         .append(                                        sbDual.toString()                                              )
                         .append("                                                                                      )            \n")
                         .append("                        AND        tso.subj    = tsj.subj                                          \n")
                         .append("                      )            vso                                                             \n")
                         .append("              where   tm.userid in " + where_query + "                                             \n")
                         .append("              and     tm.userid = vst.userid(+)                                                    \n")
                         .append("              and     tm.userid = vso.userid(+)                                                    \n")
                         .append("              and     NVL(tm.ismailling, 'Y') = 'Y'                                                 \n")
                         .append("      )   v                                                                                        \n")
                         .append(" GROUP BY  v.userid, v.year, v.subj, v.subjseq                                                     \n");

                    v_union     = " UNION ALL \n";
                    where_query = " ('xxxx'     ";
                    
                    sbDual.setLength(0);
                    
                    sbDual.append(" SELECT '', '', '', '' FROM DUAL \n");
                    
                    v_procunion = true;
                } else {
                    v_procunion = false;
                }
            }
            
            Log.info.println(this.getClass().getName() + "." + "sendMailUserForSubjSeqList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            if ( !v_procunion ) {
                where_query += ")";
                
                sbSQL.append(" " + v_union + "                                                                                   \n")
                     .append(" SELECT  v.userid                                                                                  \n")
                     .append("      ,  MAX(v.name     )     name                                                                 \n")
                     .append("      ,  MAX(v.handphone)     handphone                                                            \n")
                     .append("      ,  MAX(v.email    )     email                                                                \n")
                     .append("      ,  v.subj                                                                                    \n")
                     .append("      ,  v.subjseq                                                                                 \n")
                     .append("      ,  v.year                                                                                    \n")
                     .append("      ,  MAX(v.subjnm   )     subjnm                                                               \n")
                     .append(" FROM    (                                                                                         \n")
                     .append("              SELECT  tm.userid                                                                    \n")
                     .append("                  ,   tm.name                                                                      \n")
                     .append("                  ,   tm.handphone                                                                 \n")
                     .append("                  ,   tm.email                                                                     \n")
                     .append("                  ,   NVL(vst.subj    , vso.subj   )  subj                                         \n")
                     .append("                  ,   NVL(vst.subjseq , vso.subjseq)  subjseq                                      \n")
                     .append("                  ,   NVL(vst.year    , vso.year   )  year                                         \n")
                     .append("                  ,   NVL(vst.subjnm  , vso.subjnm )  subjnm                                       \n")
                     .append("              from    tz_member    tm                                                              \n")
                     .append("                  ,   (                                                                            \n")
                     .append("                        SELECT     tst.UserId, tst.Subj, tst.Year, tst.Subjseq, tsj.Subjnm         \n")
                     .append("                        FROM       tz_student tst                                                  \n")
                     .append("                               ,   tz_subj    tsj                                                  \n")
                     .append("                        WHERE      ( tst.UserId, tst.Subj, tst.Year, tst.Subjseq ) IN (            \n")
                     .append(                                        sbDual.toString()                                              )
                     .append("                                                                                       )           \n")
                     .append("                        AND        tst.subj    = tsj.subj                                          \n")
                     .append("                      )            vst                                                             \n")
                     .append("                  ,   (                                                                            \n")
                     .append("                        SELECT     tso.UserId, tso.Subj, tso.Year, tso.Subjseq, tsj.Subjnm         \n")
                     .append("                        FROM       tz_stold   tso                                                  \n")
                     .append("                           ,       tz_subj    tsj                                                  \n")
                     .append("                        WHERE          ( tso.UserId, tso.Subj, tso.Year, tso.Subjseq ) IN (        \n")
                     .append(                                        sbDual.toString()                                              )
                     .append("                                                                                      )            \n")
                     .append("                        AND        tso.subj    = tsj.subj                                          \n")
                     .append("                      )            vso                                                             \n")
                     .append("              where   tm.userid in " + where_query + "                                             \n")
                     .append("              and     tm.userid = vst.userid(+)                                                    \n")
                     .append("              and     tm.userid = vso.userid(+)                                                    \n")
                     .append("              and     NVL(tm.ismailling, 'Y') = 'Y'                                                 \n")
                     .append("      )   v                                                                                        \n")
                     .append(" GROUP BY  v.userid, v.year, v.subj, v.subjseq                                                     \n");
            }   
            
//            System.out.println(this.getClass().getName() + "." + "sendMailUserForSubjSeqList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            Log.info.println(this.getClass().getName() + "." + "sendMailUserForSubjSeqList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            

            ls  = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                list.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return list;
    }
    
    
    public ArrayList sendMailUserForList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        DataBox             dbox        = null;
        ListSet             ls          = null;
        ArrayList           list        = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              where_query = "";
        
        Vector              v_vchecks   = box.getVector("p_checks");
        String              v_schecks   = "";
        String              v_union     = "";
        boolean             v_procunion = false;
        
        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            sbSQL.append(" SELECT   UserId                          \n")
                 .append("      ,   MAX(Name      )     Name        \n")
                 .append("      ,   MAX(Name      )     Name        \n")
                 .append("      ,   MAX(Name      )     Name        \n")
                 .append("      ,   MAX(Handphone )     Handphone   \n")
                 .append("      ,   MAX(Email     )     Email       \n")
                 .append(" FROM     (                               \n");
            
            where_query = "('xxxx'";
        
            for ( int i = 0; i < v_vchecks.size(); i++ ) { 
                v_schecks = (String)v_vchecks.elementAt(i);
                
                if ( v_schecks.indexOf(',') > 0)
                    v_schecks = StringManager.substring(v_schecks, 0, v_schecks.indexOf(','));
            
                where_query = where_query + "," + SQLString.Format(v_schecks) ;
                
                if ( i != 0 && i % 500 == 0 ) {
                    where_query += ")";
                    
                    sbSQL.append(" " + v_union + "                                ")
                         .append(" SELECT  userid                               \n")
                         .append("     ,   name                                 \n")
                         .append("     ,   handphone                            \n")
                         .append("     ,   email                                \n")
                         .append(" from    tz_member                            \n")
                         .append(" where   userid in " + where_query + "        \n");
                    
                    v_union     = " UNION ALL \n";
                    where_query = " ('xxxx'     ";
                    
                    v_procunion = true;
                } else {
                    v_procunion = false;
                }
            }
            
            if ( !v_procunion ) {
                where_query += ")";
            
                sbSQL.append(" " + v_union + "                                ")
                     .append(" SELECT  userid                               \n")
                     .append("     ,   name                                 \n")
                     .append("     ,   handphone                            \n")
                     .append("     ,   email                                \n")
                     .append(" from    tz_member                            \n")
                     .append(" where   userid in " + where_query + "        \n");
            }   
            
            sbSQL.append("          )       V                               \n")
                 .append(" GROUP BY V.UserId                                \n");
            
            System.out.println(this.getClass().getName() + "." + "sendMailUserForList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls  = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                list.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return list;
    }

    
    /**
    진도관련 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int ProgressSubjMainSendMail(RequestBox box) throws Exception {
        DBConnectionManager connMgr             = null;
        int                 isOk                = 1;
        int                 isSend              = 0;
        Vector              v_vchecks           = box.getVector("p_checks"   );
        String              v_schecks           = "";
        
        String              v_email             = "helpdesk@kg21.net";
        String              v_name              = "경기여성e-러닝센터";

        String              v_mgrcode           = box.getString("p_mgrcode"  );
        String              v_mgrseq            = box.getString("p_mgrseq"   );
        String              v_myear             = box.getString("p_myear"    );
        String              v_msubjcourse       = box.getString("p_msubj"    );
        String              v_msubjseq          = box.getString("p_msubjseq" );
        String              v_templetid         = box.getString("p_templetid");
        String              v_mid               = "";

        URL                 url                 = null; // URL 주소 객체
        URLConnection       urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader   isr                 = null;
        BufferedReader      br                  = null;
        String              v_url               = "";
        
        try {
            v_mid       = this.getMId();
            
            box.put("p_mid"       ,  v_mid                               );
            box.put("p_templetid" ,  box.getString("p_templetid"        ));
            
            isSend      = this.ProgressSubjSendMail(box);
            
            //http://mailer.womenpro.or.kr/mail_input.asp?mid=메일번호&templetid=템플릿번호&revname=보내는사람이름&revemail=보내는사람이메일&s_grcode=교육그룹&s_gyear=교육연도&s_grseq=교육차수&s_subjcourse=과정&s_subjseq=차수
            // 운영자명|사용자명|과목명|학습시작일|학습종료일|과제제출일|학습기간|유저ID|패스워드|총점|평가기준
            // 17 : 입과안내 템플릿 번호
            v_url  = "http://mailer.womenpro.or.kr/mail_input.asp?" ;
            v_url += "mid="             + v_mid         ;             
            v_url += "&templetid="      + v_templetid   ;
            v_url += "&revname="        + URLEncoder.encode(v_name, "euc-kr");
            v_url += "&revemail="       + v_email       ;
            v_url += "&s_grcode="       + v_mgrcode     ;
            v_url += "&s_gyear="        + v_myear       ;
            v_url += "&s_grseq="        + v_mgrseq      ;
            v_url += "&s_subjcourse="   + v_msubjcourse ;
            v_url += "&s_subjseq="      + v_msubjseq    ;
            
            System.out.println("=========[URL] : ["  + v_url + "]\n");
            
            url         = new URL(v_url);
            urlconn     = url.openConnection();
            
            //내용을 읽어오기위한 InputStream객체를 생성한다..
            is          = urlconn.getInputStream();
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } 
        
        return isOk;
    }
    
    
    /**
    진도관련 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int ProgressFreeMainSendMail(RequestBox box) throws Exception {
        DBConnectionManager connMgr             = null;
        int                 isOk                = 1;
        int                 isSend              = 0;
        Vector              v_vchecks           = box.getVector("p_checks");
        String              v_schecks           = "";
        
        String              v_email             = "helpdesk@kg21.net";
        String              v_name              = "경기여성e-러닝센터";

        String              v_mgrcode           = box.getString("p_mgrcode"  );
        String              v_mgrseq            = box.getString("p_mgrseq"   );
        String              v_myear             = box.getString("p_myear"    );
        String              v_msubjcourse       = box.getString("p_msubj"    );
        String              v_msubjseq          = box.getString("p_msubjseq" );
        String              v_templetid         = box.getString("p_templetid");
        String              v_mid               = "";

        URL                 url                 = null; // URL 주소 객체
        URLConnection       urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader   isr                 = null;
        BufferedReader      br                  = null;
        String              v_url               = "";
        
        try {
            v_mid       = this.getMId();
            
            box.put("p_mid"       ,  v_mid                               );
            box.put("p_templetid" ,  box.getString("p_templetid"        ));
            
            isSend      = this.ProgressFreeSendMail(box);
            
            //http://mailer.womenpro.or.kr/mail_input.asp?mid=메일번호&templetid=템플릿번호&revname=보내는사람이름&revemail=보내는사람이메일&s_grcode=교육그룹&s_gyear=교육연도&s_grseq=교육차수&s_subjcourse=과정&s_subjseq=차수
            // 운영자명|사용자명|과목명|학습시작일|학습종료일|과제제출일|학습기간|유저ID|패스워드|총점|평가기준
            // 17 : 입과안내 템플릿 번호
            v_url  = "http://mailer.womenpro.or.kr/mail_input.asp?" ;
            v_url += "mid="             + v_mid         ;             
            v_url += "&templetid="      + v_templetid   ;
            v_url += "&revname="        + URLEncoder.encode(v_name, "euc-kr");
            v_url += "&revemail="       + v_email       ;
            v_url += "&s_grcode="       + v_mgrcode     ;
            v_url += "&s_gyear="        + v_myear       ;
            v_url += "&s_grseq="        + v_mgrseq      ;
            v_url += "&s_subjcourse="   + v_msubjcourse ;
            v_url += "&s_subjseq="      + v_msubjseq    ;
            
            System.out.println("=========[URL] : ["  + v_url + "]\n");
            
            url         = new URL(v_url);
            urlconn     = url.openConnection();
            
            //내용을 읽어오기위한 InputStream객체를 생성한다..
            is          = urlconn.getInputStream();
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } 
        
        return isOk;
    }
    
    
    /**
    진도관련 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int ProgressFreeSendMail(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        DataBox             dbox            = null;
        DataBox             pbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk            = 0;
        int                 isSend          = 0;
        
        Vector              v_vchecks       = box.getVector("p_checks"   );
        String              v_schecks       = "";
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            System.out.println("v_vchecks=================" + v_vchecks);            

            for ( int i = 0 ; i < v_vchecks.size(); i++ ) {
                v_schecks   = (String)v_vchecks.elementAt(i);
                
//                System.out.println("===================[v_schecks]:[" + v_schecks + "]");
                
                String [] v_svalue  = v_schecks.split(",");
                
//                System.out.println("===================[v_svalue.length]:[" + v_svalue.length + "]");
                
                box.put("p_userid"    , v_svalue[0].trim());
                box.put("p_subj"      , v_svalue[1].trim());
                box.put("p_year"      , v_svalue[2].trim());
                box.put("p_subjseq"   , v_svalue[3].trim());
                box.put("p_email"     , v_svalue[4].trim());
                box.put("p_name"      , v_svalue[5].trim());
                
                v_svalue[0] = v_svalue[0].trim();
                v_svalue[1] = v_svalue[1].trim();
                v_svalue[2] = v_svalue[2].trim();
                v_svalue[3] = v_svalue[3].trim();
                v_svalue[4] = v_svalue[4].trim();
                v_svalue[5] = v_svalue[5].trim();
                
                sbSQL.setLength(0);
                
                sbSQL.append(" SELECT c.Name                                               User_Name            \n")
                     .append("     ,  c.email                                                                   \n")
                     .append(" FROM   Tz_Member    c                                                            \n")
                     .append(" WHERE  c.UserId     = " + SQLString.Format(box.getString("p_userid")) + "        \n");
                
                System.out.println(this.getClass().getName() + "." + "ProgressSubjSendMail() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
                ls = connMgr.executeQuery(sbSQL.toString());
                
                if ( ls.next() )
                    dbox        = ls.getDataBox();
                
                ls.close();
                
                dbox.put("d_userid"    , (v_svalue == null ? "" : v_svalue[0]));
                dbox.put("d_subj"      , (v_svalue == null ? "" : v_svalue[1]));
                dbox.put("d_year"      , (v_svalue == null ? "" : v_svalue[2]));
                dbox.put("d_subjseq"   , (v_svalue == null ? "" : v_svalue[3]));
                dbox.put("d_email"     , (v_svalue == null ? "" : v_svalue[4]));
                dbox.put("d_name"      , (v_svalue == null ? "" : v_svalue[5]));
                dbox.put("d_mid"       ,  box.getString("p_mid"              ));
                dbox.put("d_templetid" ,  box.getString("p_templetid"        ));
                
                dbox.put("d_sub1"      ,  box.getString("p_title"   )         );
                dbox.put("d_sub2"      ,  box.getString("p_contents").replaceAll("\r\n", "<br>"));
                
                //isSend      = this.insertMailWorkInfo(connMgr, dbox);
                isSend      = this.insertMailWorkInfoUrl(dbox);
                
                System.out.println("===================[i     ]:[" + i      + "]");
                
                if ( isSend > 0  )
                    isOk++;
                else
                    throw new SQLException("SQLExcetion : Insert Fail\n");
            }
            
            if ( isOk > 0 ) 
                connMgr.commit();
            else
                connMgr.rollback();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
        }
     
        return isOk;
    }
    
    
    /**
    진도관련 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int ProgressSubjSendMail(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        DataBox             dbox            = null;
        DataBox             pbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk            = 0;
        int                 isSend          = 0;
        
        Vector              v_vchecks       = box.getVector("p_checks"   );
        String              v_schecks       = "";
        DecimalFormat       df              = new DecimalFormat("###.00");
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            for ( int i = 0 ; i < v_vchecks.size(); i++ ) {
                v_schecks   = (String)v_vchecks.elementAt(i);
                
//                System.out.println("===================[v_schecks]:[" + v_schecks + "]");
                
                String [] v_svalue  = v_schecks.split(",");
                
//                System.out.println("===================[v_svalue.length]:[" + v_svalue.length + "]");
                
                box.put("p_userid"    , v_svalue[0].trim());
                box.put("p_subj"      , v_svalue[1].trim());
                box.put("p_year"      , v_svalue[2].trim());
                box.put("p_subjseq"   , v_svalue[3].trim());
                box.put("p_email"     , v_svalue[4].trim());
                box.put("p_name"      , v_svalue[5].trim());
                
                v_svalue[0] = v_svalue[0].trim();
                v_svalue[1] = v_svalue[1].trim();
                v_svalue[2] = v_svalue[2].trim();
                v_svalue[3] = v_svalue[3].trim();
                v_svalue[4] = v_svalue[4].trim();
                v_svalue[5] = v_svalue[5].trim();
                
                switch ( box.getInt("p_templetid") ) {
                    case 13 :
                        dbox    = ProgressInstance13(connMgr, box);
                        break;
                    case 14 :
                        dbox    = ProgressInstance14(connMgr, box);
                        break;
                    case 23 :
                        dbox    = ProgressInstance23(connMgr, box);
                        break;
                    case 24 :
                        dbox    = ProgressInstance24(connMgr, box);
                        break;
                    case 25 :
                        dbox    = ProgressInstance25(connMgr, box);
                        break;
                    default :
                        dbox    = ProgressInstance12(connMgr, box);
                        break;
                }
                
                isSend      = this.insertMailWorkInfo(connMgr, dbox);
                //isSend      = this.insertMailWorkInfoUrl(dbox);
                
                System.out.println("===================[i     ]:[" + i      + "]");
                
                if ( isSend > 0  )
                    isOk++;
                else
                    throw new SQLException("SQLExcetion : Insert Fail\n");
            }
            
            if ( isOk > 0 ) 
                connMgr.commit();
            else
                connMgr.rollback();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
        }
     
        return isOk;
    }
    
    
    /**
    진도관련 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public DataBox ProgressInstance12(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls              = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        DecimalFormat       df              = new DecimalFormat("###.00");
        
        try {
            sbSQL.append(" SELECT c.Name                                               User_Name            \n")
                 .append("     ,  d.Name                                               Admin_Name           \n")
                 .append("     ,  c.email                                                                   \n")
                 .append("     ,  b.Subjnm                                                                  \n")
                 .append("     ,  b.eduperiod                                                               \n")
                 .append("     ,  TO_CHAR( SYSDATE, 'MM')                              Lecture_Cancel_MM    \n")
                 .append("     ,  TO_CHAR( SYSDATE, 'DD')                              Lecture_Cancel_DD    \n")
                 .append(" FROM   Tz_Subj      b                                                            \n")
                 .append("     ,  Tz_Member    c                                                            \n")
                 .append("     ,  Tz_Member    d                                                            \n")
                 .append(" WHERE  c.UserId     = " + SQLString.Format(box.getString("p_userid")) + "        \n")
                 .append(" AND    b.Subj       = " + SQLString.Format(box.getString("p_subj"  )) + "        \n")
                 .append(" AND    b.MUserId    = d.UserId                                                   \n");
            
//                System.out.println(this.getClass().getName() + "." + "ProgressSubjSendMail() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() )
                dbox        = ls.getDataBox();
            
            ls.close();
            
            dbox.put("d_userid"    , box.getString("p_userid"   ));
            dbox.put("d_subj"      , box.getString("p_subj"     ));
            dbox.put("d_year"      , box.getString("p_year"     ));
            dbox.put("d_subjseq"   , box.getString("p_subjseq"  ));
            dbox.put("d_email"     , box.getString("p_email"    ));
            dbox.put("d_name"      , box.getString("p_name"     ));
            dbox.put("d_mid"       , box.getString("p_mid"      ));
            dbox.put("d_templetid" , box.getString("p_templetid"));

            // START :: 취득점수 ------------------------------------
            EduStartBean    ebean               = EduStartBean.getInstance();
            EduScoreData    data2               = ebean.SelectEduScore(box);
            // END   :: 취득점수 ------------------------------------
            
            // START :: 권장진도율, 자기진도율 -------------------------
            SubjGongAdminBean   sbean           = new SubjGongAdminBean();                            
            String              promotion       = sbean.getPromotion(box);
            String              progress        = sbean.getProgress(box);
            // END   :: 권장진도율, 자기진도율 -------------------------
            
            // START :: 과제 제출개수 ----------------------------------
            ProjectAdminBean    report          = new ProjectAdminBean();
            int                 reportadmin     = report.getAdminData(box);
            // END   :: 과제 제출개수 ----------------------------------

            // START :: 과제 제출여부 ----------------------------------
            ProjectAdminBean    reportuser      = new ProjectAdminBean();
            int                 reportdata      = reportuser.getUserData(box);
            // ENd   :: 과제 제출여부 ----------------------------------

            // START :: 평가 갯수 --------------------------------------
            ExamUserBean        exambean        = new ExamUserBean();
            ArrayList           examdata        = exambean.getUserData(box);
            ArrayList           examresultdata  = exambean.getUserResultData(box);
            // ENd   :: 평가 갯수 --------------------------------------
            
            Hashtable   ht          = null;
            int         htsize      = 0;
            double      v_totscore  = 0.0;
            
            if ( data2 != null ) {
                htsize  = data2.eduScoreList.size();
                
                for (int j = 0; j < htsize; j++) {
                    EduScoreDataSub sds = (EduScoreDataSub)data2.eduScoreList.get(String.valueOf(j));
                    
                    if ( (Hashtable)data2.getEduScoreList() != null){
                        ht          = (Hashtable)data2.getEduScoreList();
                        sds         = (EduScoreDataSub)ht.get(String.valueOf(j));
                        v_totscore  += sds.getAvscore();
                    }
                }
            }
            
            dbox.put("d_sub1", dbox.getString("d_subjnm"    ) );
            dbox.put("d_sub2", dbox.getString("d_admin_name") );
            dbox.put("d_sub3", FormatDate.getFormatDate(data2.getEdustart(),"yyyy.MM.dd") + " ~ " + FormatDate.getFormatDate(data2.getEduend(),"yyyy.MM.dd"));
            dbox.put("d_sub4", dbox.getString("d_eduperiod" )  );
            dbox.put("d_sub5", String.valueOf(data2.getTstep())); // 자기 진도율
            dbox.put("d_sub6", promotion                       ); // 권장 진도율
            dbox.put("d_sub7", (examdata.get(0).equals("0") ? "없음" : (examdata.get(0) + "개중 " + examresultdata.get(0) + "개")));
            dbox.put("d_sub8", (examdata.get(2).equals("0") ? "없음" : (examdata.get(2) + "개중 " + examresultdata.get(2) + "개")));
            dbox.put("d_sub9", (reportadmin == 0 ? "없음" : (String.valueOf(reportadmin) + "개중 " + String.valueOf(reportdata) + "개 제출<br>" + FormatDate.getFormatDate(data2.getEduend(),"yyyy.MM.dd") + "까지 꼭 제출바랍니다.")));
            dbox.put("d_sub10", v_totscore <= 0 ? "0.00" : df.format(v_totscore));
            dbox.put("d_sub11", FormatDate.getFormatDate(data2.getEduend(),"yyyy.MM.dd"));
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
        }
     
        return dbox;
    }
    
    
    
    
    /**
    진도관련 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public DataBox ProgressInstance13(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls              = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        DecimalFormat       df              = new DecimalFormat("###.00");
        
        try {
            sbSQL.append(" SELECT c.Name                                               User_Name            \n")
                 .append("     ,  d.Name                                               Admin_Name           \n")
                 .append("     ,  c.email                                                                   \n")
                 .append("     ,  b.Subjnm                                                                  \n")
                 .append("     ,  b.eduperiod                                                               \n")
                 .append("     ,  TO_CHAR( SYSDATE, 'MM')                              Lecture_Cancel_MM    \n")
                 .append("     ,  TO_CHAR( SYSDATE, 'DD')                              Lecture_Cancel_DD    \n")
                 .append(" FROM   Tz_Subj      b                                                            \n")
                 .append("     ,  Tz_Member    c                                                            \n")
                 .append("     ,  Tz_Member    d                                                            \n")
                 .append(" WHERE  c.UserId     = " + SQLString.Format(box.getString("p_userid")) + "        \n")
                 .append(" AND    b.Subj       = " + SQLString.Format(box.getString("p_subj"  )) + "        \n")
                 .append(" AND    b.MUserId    = d.UserId                                                   \n");
            
//                System.out.println(this.getClass().getName() + "." + "ProgressSubjSendMail() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() )
                dbox        = ls.getDataBox();
            
            ls.close();
            
            dbox.put("d_userid"    , box.getString("p_userid"   ));
            dbox.put("d_subj"      , box.getString("p_subj"     ));
            dbox.put("d_year"      , box.getString("p_year"     ));
            dbox.put("d_subjseq"   , box.getString("p_subjseq"  ));
            dbox.put("d_email"     , box.getString("p_email"    ));
            dbox.put("d_name"      , box.getString("p_name"     ));
            dbox.put("d_mid"       , box.getString("p_mid"      ));
            dbox.put("d_templetid" , box.getString("p_templetid"));

            // START :: 취득점수 ------------------------------------
            EduStartBean    ebean               = EduStartBean.getInstance();
            EduScoreData    data2               = ebean.SelectEduScore(box);
            // END   :: 취득점수 ------------------------------------
            
            // START :: 권장진도율, 자기진도율 -------------------------
            SubjGongAdminBean   sbean           = new SubjGongAdminBean();                            
            String              promotion       = sbean.getPromotion(box);
            String              progress        = sbean.getProgress(box);
            // END   :: 권장진도율, 자기진도율 -------------------------
            
            // START :: 과제 제출개수 ----------------------------------
            ProjectAdminBean    report          = new ProjectAdminBean();
            int                 reportadmin     = report.getAdminData(box);
            // END   :: 과제 제출개수 ----------------------------------

            // START :: 과제 제출여부 ----------------------------------
            ProjectAdminBean    reportuser      = new ProjectAdminBean();
            int                 reportdata      = reportuser.getUserData(box);
            // ENd   :: 과제 제출여부 ----------------------------------

            // START :: 평가 갯수 --------------------------------------
            ExamUserBean        exambean        = new ExamUserBean();
            ArrayList           examdata        = exambean.getUserData(box);
            ArrayList           examresultdata  = exambean.getUserResultData(box);
            // ENd   :: 평가 갯수 --------------------------------------
            
            Hashtable   ht          = null;
            int         htsize      = 0;
            double      v_totscore  = 0.0;
            
            if ( data2 != null ) {
                htsize  = data2.eduScoreList.size();
                
                for (int j = 0; j < htsize; j++) {
                    EduScoreDataSub sds = (EduScoreDataSub)data2.eduScoreList.get(String.valueOf(j));
                    
                    if ( (Hashtable)data2.getEduScoreList() != null){
                        ht          = (Hashtable)data2.getEduScoreList();
                        sds         = (EduScoreDataSub)ht.get(String.valueOf(j));
                        v_totscore  += sds.getAvscore();
                    }
                }
            }
            
            dbox.put("d_sub1", dbox.getString("d_subjnm"    ) );
            dbox.put("d_sub2", dbox.getString("d_admin_name") );
            dbox.put("d_sub3", FormatDate.getFormatDate(data2.getEdustart(),"yyyy.MM.dd") + " ~ " + FormatDate.getFormatDate(data2.getEduend(),"yyyy.MM.dd"));
            dbox.put("d_sub4", dbox.getString("d_eduperiod" )  );
            dbox.put("d_sub5", String.valueOf(data2.getTstep())); // 자기 진도율
            dbox.put("d_sub6", promotion                       ); // 권장 진도율
            dbox.put("d_sub7", (examdata.get(0).equals("0") ? "없음" : (examdata.get(0) + "개중 " + examresultdata.get(0) + "개")));
            dbox.put("d_sub8", (examdata.get(2).equals("0") ? "없음" : (examdata.get(2) + "개중 " + examresultdata.get(2) + "개")));
            dbox.put("d_sub9", (reportadmin == 0 ? "없음" : (String.valueOf(reportadmin) + "개중 " + String.valueOf(reportdata) + "개 제출<br>" + FormatDate.getFormatDate(data2.getEduend(),"yyyy.MM.dd") + "까지 꼭 제출바랍니다.")));
            dbox.put("d_sub10", v_totscore <= 0 ? "0.00" : df.format(v_totscore));
            dbox.put("d_sub11", FormatDate.getFormatDate(data2.getEduend(),"yyyy.MM.dd"));
            dbox.put("d_sub12", FormatDate.getFormatDate(data2.getEduend(),"MM"));
            dbox.put("d_sub13", FormatDate.getFormatDate(data2.getEduend(),"dd"));
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
        }
     
        return dbox;
    }
    
    
    
    /**
    진도관련 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public DataBox ProgressInstance14(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls              = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk            = 0;
        int                 isSend          = 0;
        
        try {
            sbSQL.append(" SELECT c.Name                                                           User_Name            \n")
                 .append("     ,  d.Name                                                           Admin_Name           \n")
                 .append("     ,  c.email                                                                               \n")
                 .append("     ,  e.Subjnm                                                                              \n")
                 .append("     ,  TO_CHAR( TO_DATE(RPAD(b.EduEnd, 8, '0'), 'YYYYMMDDHH24'), 'MM')  EduEnd_MM            \n")
                 .append("     ,  TO_CHAR( TO_DATE(RPAD(b.EduEnd, 8, '0'), 'YYYYMMDDHH24'), 'DD')  EduEnd_DD            \n")
                 .append(" FROM   Tz_Subjseq   b                                                                        \n")
                 .append("     ,  Tz_Member    c                                                                        \n")
                 .append("     ,  Tz_Member    d                                                                        \n")
                 .append("     ,  Tz_Subj      e                                                                        \n")
                 .append(" WHERE  c.UserId     = " + SQLString.Format(box.getString("p_userid" )) + "                   \n")
                 .append(" AND    b.Subj       = " + SQLString.Format(box.getString("p_subj"   )) + "                   \n")
                 .append(" AND    b.Year       = " + SQLString.Format(box.getString("p_year"   )) + "                   \n")
                 .append(" AND    b.Subjseq    = " + SQLString.Format(box.getString("p_subjseq")) + "                   \n")
                 .append(" AND    b.Subj       = e.Subj                                                                 \n")
                 .append(" AND    e.MUserId    = d.UserId                                                               \n");
                
//                System.out.println(this.getClass().getName() + "." + "ProgressSubjSendMail() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
            ls = connMgr.executeQuery(sbSQL.toString());
                
            if ( ls.next() )
                dbox        = ls.getDataBox();
            
            dbox.put("d_userid"    , box.getString("p_userid"       ));
            dbox.put("d_subj"      , box.getString("p_subj"         ));
            dbox.put("d_year"      , box.getString("p_year"         ));
            dbox.put("d_subjseq"   , box.getString("p_subjseq"      ));
            dbox.put("d_email"     , box.getString("p_email"        ));
            dbox.put("d_name"      , box.getString("p_name"         ));
            dbox.put("d_mid"       , box.getString("p_mid"          ));
            dbox.put("d_templetid" , box.getString("p_templetid"    ));
            
            dbox.put("d_sub1"      , dbox.getString("d_admin_name"  ));
            dbox.put("d_sub2"      , dbox.getString("d_subjnm"      ));
            dbox.put("d_sub3"      , dbox.getString("d_eduend_mm"   ));
            dbox.put("d_sub4"      , dbox.getString("d_eduend_dd"   ));
        } catch ( SQLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
        }
     
        return dbox;
    }
    
    
    /**
    개인정보 부정 수집 및 사용금지 인스턴스 메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public DataBox ProgressInstance23(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls              = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk            = 0;
        int                 isSend          = 0;
        
        try {
            sbSQL.append(" SELECT c.Name                                                           User_Name            \n")
                 .append("     ,  c.email                                                                               \n")
                 .append(" FROM   Tz_Member    c                                                                        \n")
                 .append(" WHERE  c.UserId     = " + SQLString.Format(box.getString("p_userid" )) + "                   \n");
                
//                System.out.println(this.getClass().getName() + "." + "ProgressSubjSendMail() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
            ls = connMgr.executeQuery(sbSQL.toString());
                
            if ( ls.next() )
                dbox        = ls.getDataBox();
            
            dbox.put("d_userid"    , box.getString("p_userid"       ));
            dbox.put("d_subj"      , ""                              );
            dbox.put("d_year"      , ""                              );
            dbox.put("d_subjseq"   , ""                              );
            dbox.put("d_email"     , box.getString("p_email"        ));
            dbox.put("d_name"      , box.getString("p_name"         ));
            dbox.put("d_mid"       , box.getString("p_mid"          ));
            dbox.put("d_templetid" , box.getString("p_templetid"    ));
        } catch ( SQLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
        }
     
        return dbox;
    }
    
    
    /**
    News Letter 인스턴스 메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public DataBox ProgressInstance24(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls              = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk            = 0;
        int                 isSend          = 0;
        
        try {
            sbSQL.append(" SELECT c.Name                                                           User_Name            \n")
                 .append("     ,  c.email                                                                               \n")
                 .append(" FROM   Tz_Member    c                                                                        \n")
                 .append(" WHERE  c.UserId     = " + SQLString.Format(box.getString("p_userid" )) + "                   \n");
                
//                System.out.println(this.getClass().getName() + "." + "ProgressSubjSendMail() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
            ls = connMgr.executeQuery(sbSQL.toString());
                
            if ( ls.next() )
                dbox        = ls.getDataBox();
            
            dbox.put("d_userid"    , box.getString("p_userid"       ));
            dbox.put("d_subj"      , ""                              );
            dbox.put("d_year"      , ""                              );
            dbox.put("d_subjseq"   , ""                              );
            dbox.put("d_email"     , box.getString("p_email"        ));
            dbox.put("d_name"      , box.getString("p_name"         ));
            dbox.put("d_mid"       , box.getString("p_mid"          ));
            dbox.put("d_templetid" , box.getString("p_templetid"    ));
        } catch ( SQLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
        }
     
        return dbox;
    }
    
    
    /**
    News Letter 인스턴스 메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public DataBox ProgressInstance25(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls              = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk            = 0;
        int                 isSend          = 0;
        
        try {
            sbSQL.append(" SELECT c.Name                                                           User_Name            \n")
                 .append("     ,  c.email                                                                               \n")
                 .append(" FROM   Tz_Member    c                                                                        \n")
                 .append(" WHERE  c.UserId     = " + SQLString.Format(box.getString("p_userid" )) + "                   \n");
                
//                System.out.println(this.getClass().getName() + "." + "ProgressSubjSendMail() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
            ls = connMgr.executeQuery(sbSQL.toString());
                
            if ( ls.next() )
                dbox        = ls.getDataBox();
            
            dbox.put("d_userid"    , box.getString("p_userid"       ));
            dbox.put("d_subj"      , ""                              );
            dbox.put("d_year"      , ""                              );
            dbox.put("d_subjseq"   , ""                              );
            dbox.put("d_email"     , box.getString("p_email"        ));
            dbox.put("d_name"      , box.getString("p_name"         ));
            dbox.put("d_mid"       , box.getString("p_mid"          ));
            dbox.put("d_templetid" , box.getString("p_templetid"    ));
        } catch ( SQLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
        }
     
        return dbox;
    }
    
    
    /**
    대용량 메일 Table에 Insert
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int insertMailWorkInfo(DBConnectionManager connMgr, DataBox dbox) throws Exception { 
        PreparedStatement   pstmt               = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        int                 iIdxAdd             = 0;
        
        int                 isOk                = 0;
        
        try { 
            sbSQL.append(" INSERT INTO TZ_MAILWORKINFO                      \n")                
                 .append(" (                                                \n")
                 .append("         PK_SEQ                                   \n")
                 .append("     ,   MID                                      \n")
                 .append("     ,   TEMPLETID                                \n")
                 .append("     ,   USERID                                   \n")
                 .append("     ,   NAME                                     \n")
                 .append("     ,   EMAIL                                    \n")
                 .append("     ,   SUBJ                                     \n")
                 .append("     ,   YEAR                                     \n")
                 .append("     ,   SUBJSEQ                                  \n")
                 .append("     ,   GUBUN                                    \n")
                 .append("     ,   SUB1                                     \n")
                 .append("     ,   SUB2                                     \n")
                 .append("     ,   SUB3                                     \n")
                 .append("     ,   SUB4                                     \n")
                 .append("     ,   SUB5                                     \n")
                 .append("     ,   SUB6                                     \n")
                 .append("     ,   SUB7                                     \n")
                 .append("     ,   SUB8                                     \n")
                 .append("     ,   SUB9                                     \n")
                 .append("     ,   SUB10                                    \n")
                 .append("     ,   SUB11                                    \n")
                 .append("     ,   SUB12                                    \n")
                 .append("     ,   SUB13                                    \n")
                 .append("     ,   SUB14                                    \n")
                 .append("     ,   SUB15                                    \n")
                 .append("     ,   SUB16                                    \n")
                 .append("     ,   SUB17                                    \n")
                 .append("     ,   SUB18                                    \n")
                 .append("     ,   SUB19                                    \n")
                 .append("     ,   SUB20                                    \n")
                 .append(" ) VALUES (                                       \n")
                 .append("         Tz_MailWorkInfo_Pk_Seq.NextVal           \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append("     ,   ?                                        \n")
                 .append(" )                                                \n");
            
            System.out.println("========" + sbSQL.toString());
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString( ++iIdxAdd, dbox.getString("d_mid"      ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_templetid"));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_userid"   ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_name"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_email"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_subj"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_year"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_subjseq"  ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_gubun"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub1"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub2"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub3"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub4"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub5"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub6"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub7"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub8"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub9"     ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub10"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub11"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub12"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub13"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub14"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub15"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub16"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub17"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub18"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub19"    ));
            pstmt.setString( ++iIdxAdd, dbox.getString("d_sub20"    ));
            
            isOk = pstmt.executeUpdate();
            
            isOk        = 1;
        } catch ( SQLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e ) { } }
        }
     
        return isOk;
    }
    
    
    /**
    대용량 메일 Table에 Insert
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int insertMailWorkInfoUrl(DataBox dbox) throws Exception {
        ListSet             ls                  = null;
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk                = 0;

        URL                 url                 = null; // URL 주소 객체
        URLConnection       urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader   isr                 = null;
        BufferedReader      br                  = null;
        String              v_url               = "";
        
        try {
            // 운영자명|사용자명|과목명|학습시작일|학습종료일|과제제출일|학습기간|유저ID|패스워드|총점|평가기준
            // 17 : 입과안내 템플릿 번호
            v_url  = "http://mailer.womenpro.or.kr/inst_input.asp?"      ;
            v_url += "i_id="   + dbox.getString("d_templetid"); 
            v_url += "&name="  + URLEncoder.encode(dbox.getString("d_name"      ), "euc-kr");
            v_url += "&email=" + URLEncoder.encode(dbox.getString("d_email"     ), "euc-kr");
            v_url += "&sub1="  + URLEncoder.encode(dbox.getString("d_sub1"      ), "euc-kr");
            v_url += "&sub2="  + URLEncoder.encode(dbox.getString("d_sub2"      ), "euc-kr");
            v_url += "&sub3="  + URLEncoder.encode(dbox.getString("d_sub3"      ), "euc-kr");
            v_url += "&sub4="  + URLEncoder.encode(dbox.getString("d_sub4"      ), "euc-kr");
            v_url += "&sub5="  + URLEncoder.encode(dbox.getString("d_sub5"      ), "euc-kr");
            v_url += "&sub6="  + URLEncoder.encode(dbox.getString("d_sub6"      ), "euc-kr");
            v_url += "&sub7="  + URLEncoder.encode(dbox.getString("d_sub7"      ), "euc-kr");
            v_url += "&sub8="  + URLEncoder.encode(dbox.getString("d_sub8"      ), "euc-kr");
            v_url += "&sub9="  + URLEncoder.encode(dbox.getString("d_sub9"      ), "euc-kr");
            v_url += "&sub10=" + URLEncoder.encode(dbox.getString("d_sub10"     ), "euc-kr");
            v_url += "&sub11=" + URLEncoder.encode(dbox.getString("d_sub11"     ), "euc-kr");
            v_url += "&sub12=" + URLEncoder.encode(dbox.getString("d_sub12"     ), "euc-kr");
            
            System.out.println(v_url);
            
            url         = new URL(v_url);
            urlconn     = url.openConnection();
            
            //내용을 읽어오기위한 InputStream객체를 생성한다..
            is          = urlconn.getInputStream();

            isOk        = 1;
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        }
     
        return isOk;
    }
    
            
    /**
    입과안내 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int EnterSubjSendMail(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        DataBox             dbox                = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        ConfigSet			conf				= new ConfigSet();
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk                = 0;

        URL                 url                 = null; // URL 주소 객체
        URLConnection       urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader   isr                 = null;
        BufferedReader      br                  = null;
        String              v_url               = "";
        
        String              v_subj              = box.getString ("p_subj"           );
        String              v_subjseq           = box.getString ("p_subjseq"        );
        String              v_year              = box.getString ("p_year"           );
        String              v_userid            = box.getSession("userid"           );
        String              v_grad_string       = "";
        String              v_prefix            = "";
        String              v_content			= "";
        
        try { 
            connMgr = new DBConnectionManager();

            sbSQL.append(" SELECT   v.Admin_Name                                                                                                                    \n")
                 .append("  ,   v.User_Name                                                                                                                         \n")
                 .append("  ,   v.Email                                                                                                                             \n")
                 .append("  ,   v.Lecture_Appdate_Date                                                                                                                             \n")
                 .append("  ,   v.Lecture_Name                                                                                                                      \n")
                 .append("  ,   v.Lecture_Begin_Date                                                                                                                \n")
                 .append("  ,   v.Lecture_End_Date                                                                                                                  \n")
                 .append("  ,   v.Report_End_Date                                                                                                                   \n")
                 .append("  ,   v.Lecture_Period                                                                                                                    \n")
                 .append("  ,   v.User_Id                                                                                                                           \n")
                 .append("  ,   v.User_Passwd                                                                                                                       \n")
                 .append("  ,   v.Total_Cutline                                                                                                                     \n")
                 .append("  ,   v.gradstep_title                                                                                                                    \n")
                 .append("  ,   v.gradftest_title                                                                                                                   \n")
                 .append("  ,   v.gradexam_title                                                                                                                    \n")
                 .append("  ,   v.gradhtest_title                                                                                                                   \n")
                 .append("  ,   v.gradreport_title                                                                                                                  \n")
                 .append("  ,   v.wetc1_title                                                                                                                       \n")
                 .append("  ,   v.wetc2_title                                                                                                                       \n")
                 .append("  ,   v.gradstep                                                                                                                          \n")
                 .append("  ,   v.gradftest                                                                                                                         \n")
                 .append("  ,   v.gradexam                                                                                                                          \n")
                 .append("  ,   v.gradhtest                                                                                                                         \n")
                 .append("  ,   v.gradreport                                                                                                                        \n")
                 .append("  ,   v.wetc1                                                                                                                             \n")
                 .append("  ,   v.wetc2                                                                                                                             \n")
                 .append("  ,       DECODE(v.gradstep_title, '', '', v.gradstep_title || ' ' || gradstep || '%')                                                    \n")
                 .append("      ||  DECODE(v.gradftest_title || v.gradexam_title || v.gradhtest_title                                                               \n")
                 .append("                , ''                                                                                                                      \n")
                 .append("                , ''                                                                                                                      \n")
                 .append("                , ' + 평가('                                                                                                              \n")
                 .append("          )                                                                                                                               \n")
                 .append("      ||  DECODE( v.gradftest_title , '', '',          v.gradftest_title )                                                                \n")
                 .append("      ||  DECODE( v.gradexam_title  , '', '', ' + ' || v.gradexam_title  )                                                                \n")
                 .append("      ||  DECODE( v.gradhtest_title , '', '', ' + ' || v.gradhtest_title )                                                                \n")
                 .append("      ||  DECODE(v.gradftest_title || v.gradexam_title || v.gradhtest_title                                                               \n")
                 .append("                , ''                                                                                                                      \n")
                 .append("                , ''                                                                                                                      \n")
                 .append("                , ')'                                                                                                                     \n")
                 .append("          )                                                                                                                               \n")
                 .append("      ||  DECODE( v.gradftest                                                                                                             \n")
                 .append("              +   v.gradexam                                                                                                              \n")
                 .append("              +   v.gradhtest                                                                                                             \n")
                 .append("                , 0, '', ' ' || (  v.gradftest                                                                                            \n")
                 .append("                                +  v.gradexam                                                                                             \n")
                 .append("                                +  v.gradhtest                                                                                            \n")
                 .append("                                )                                                                                                         \n")
                 .append("                             ||    '%'                                                                                                    \n")
                 .append("                )                                                                                                                         \n")
                 .append("      ||  DECODE( v.gradreport_title , '', '', ' + ' || v.gradreport_title || ' ' || v.gradreport || '%' )                                \n")
                 .append("      ||  DECODE( v.wetc1_title      , '', '', ' + ' || v.wetc1_title      || ' ' || v.wetc1      || '%' )                                \n")
                 .append("      ||  DECODE( v.wetc2_title      , '', '', ' + ' || v.wetc2_title      || ' ' || v.wetc2      || '%' )    Grad_String                 \n")
                 .append(" FROM   (                                                                                                                                 \n")
                 .append("          SELECT d.Name                                                               User_Name                                           \n")
                 .append("              ,  d.email                                                              Email                                               \n")
                 .append("              ,  e.Name                                                               Admin_Name                                          \n")
                 .append("              ,  TO_CHAR(TO_DATE(a.appdate, 'YYYYMMDDHH24MISS'), 'YYYY.MM.DD HH24\"시\" MI\"분\"')  Lecture_Appdate_Date                                 \n")
                 .append("              ,  b.Subjnm                                                             Lecture_Name                                        \n")
                 .append("              ,  TO_CHAR(TO_DATE(c.EduStart, 'YYYYMMDDHH24'), 'YYYY.MM.DD HH24\"시\"')      Lecture_Begin_Date                                  \n")
                 .append("              ,  TO_CHAR(TO_DATE(c.EduEnd  , 'YYYYMMDDHH24'), 'YYYY.MM.DD HH24\"시\"')      Lecture_End_Date                                    \n")
                 .append("              ,  b.eduperiod                                                          Lecture_Period                                      \n")
                 .append("              ,  TO_CHAR(TO_DATE(c.EduEnd  , 'YYYYMMDDHH24'), 'YYYY.MM.DD')           Report_End_Date                                     \n")
                 .append("              ,  d.UserId                                                             User_Id                                             \n")
                 .append("              ,  fn_crypt('2', d.pwd, 'knise')                                        User_Passwd                                         \n")
                 .append("              ,  DECODE(SIGN(c.wstep      ), 0, '', '진도')                             gradstep_title                                      \n")
                 .append("              ,  DECODE(SIGN(c.wftest     ), 0, '', '최종')                             gradftest_title                                     \n")
                 .append("              ,  DECODE(SIGN(c.wmtest     ), 0, '', '중간')                             gradexam_title                                      \n")
                 .append("              ,  DECODE(SIGN(c.whtest     ), 0, '', '형성')                             gradhtest_title                                     \n")
                 .append("              ,  DECODE(SIGN(c.wreport    ), 0, '', '과제')                             gradreport_title                                    \n")
                 .append("              ,  DECODE(SIGN(c.wetc1      ), 0, '', '참여도')                            wetc1_title                                         \n")
                 .append("              ,  DECODE(SIGN(c.wetc2      ), 0, '', '토론참여도')                         wetc2_title                                       \n")
                 .append("              ,  c.wstep                                                                gradstep                                          \n")
                 .append("              ,  c.wftest                                                               gradftest                                         \n")
                 .append("              ,  c.wmtest                                                                gradexam                                         \n")
                 .append("              ,  c.whtest                                                               gradhtest                                         \n")
                 .append("              ,  c.wreport                                                              gradreport                                        \n")
                 .append("              ,  c.wetc1                                                                                                                  \n")
                 .append("              ,  c.wetc2                                                                                                                  \n")
                 .append("              ,  c.gradscore                                                           Total_cutline                                      \n")        
                 .append("          FROM   Tz_Propose   a                                                                                                           \n")
                 .append("              ,  Tz_Subj      b                                                                                                           \n")
                 .append("              ,  Tz_Subjseq   c                                                                                                           \n")
                 .append("              ,  Tz_Member    d                                                                                                           \n")
                 .append("              ,  Tz_Member    e                                                                                                           \n")
                 .append("          WHERE  a.UserId     = " + SQLString.Format(v_userid ) + "                                                                       \n")
                 .append("          AND    a.Subj       = " + SQLString.Format(v_subj   ) + "                                                                       \n")
                 .append("          AND    a.Year       = " + SQLString.Format(v_year   ) + "                                                                       \n")
                 .append("          AND    a.Subjseq    = " + SQLString.Format(v_subjseq) + "                                                                       \n")
                 .append("          AND    a.Subj       = b.Subj                                                                                                    \n")
                 .append("          AND    a.Subj       = c.Subj                                                                                                    \n")
                 .append("          AND    a.Year       = c.Year                                                                                                    \n")
                 .append("          AND    a.Subjseq    = c.Subjseq                                                                                                 \n")
                 .append("          AND    a.UserId     = d.UserId                                                                                                  \n")
                 .append("          AND    b.MUserId    = e.UserId (+)                                                                                              \n")
                 .append("      )      v                                                                                                                            \n");

//            System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                dbox  = ls.getDataBox();
            }
            if(dbox == null) {
            	dbox = new DataBox("");
            }
            
            if ( !dbox.getString("d_gradstep_title").equals("")   ) {
                v_grad_string   = dbox.getString("d_gradstep_title") + " " + dbox.getString("d_gradstep") + "%";

                v_prefix        = " + ";
            }
            
            if (    !dbox.getString("d_gradftest_title").equals("")  
                 || !dbox.getString("d_gradexam_title").equals("")
                 || !dbox.getString("d_gradhtest_title").equals("") 
               ) {
                v_grad_string   += v_prefix + "평가" + "(";
                
                v_prefix        = "";
                
                if ( !dbox.getString("d_gradftest_title").equals("") ) {
                    v_grad_string   += v_prefix + dbox.getString("d_gradftest_title") + " " + dbox.getString("d_gradftest") + "%";
                    v_prefix    = " + ";
                }
                
                if ( !dbox.getString("d_gradexam_title").equals("") ) {
                    v_grad_string   += v_prefix + dbox.getString("d_gradexam_title") + " " + dbox.getString("d_gradexam") + "%";
                    v_prefix    = " + ";
                }    

                if ( !dbox.getString("d_gradhtest_title").equals("") ) {
                    v_grad_string   += v_prefix + dbox.getString("d_gradhtest_title") + " " + dbox.getString("d_gradhtest") + "%";
                    v_prefix    = " + ";
                }    
                
                v_grad_string   += ")";
                
                v_prefix = " + ";
            }
            
            if ( !dbox.getString("d_gradreport_title").equals("") ) {
                v_grad_string   += v_prefix + dbox.getString("d_gradreport_title") + " " + dbox.getString("d_gradreport") + "%";
                v_prefix    = " + ";
            }
            
            if ( !dbox.getString("d_wetc1_title").equals("") ) {
                v_grad_string   += v_prefix + dbox.getString("d_wetc1_title") + " " + dbox.getString("d_wetc1") + "%";
                v_prefix    = " + ";
            }

            if ( !dbox.getString("d_wetc2_title").equals("") ) {
                v_grad_string   += v_prefix + dbox.getString("d_wetc2_title") + " " + dbox.getString("d_wetc2") + "%";
                v_prefix    = " + ";
            }
           
            v_content = dbox.getString("d_user_name") + "님 안녕하세요? e-Eureka 운영자입니다. <br><br> ";
            v_content += dbox.getString("d_user_name") + "님께서는 <span style='color:#ff4800;font-weight:bold;'>" + dbox.getString("d_lecture_name") + "</span> 과정에 수강신청하셨습니다.<br>";
            v_content += "학습기간은 <span style='color:#ff4800;'>" + dbox.getString("d_lecture_begin_date") + " ~ " + dbox.getString("d_lecture_end_date") + "</span>까지이며 수강신청하신 과정에 대한 문의나 기타 궁금하신 점이 있으시면 전화 또는 메일로 문의 주시기 바랍니다.<br><br> ";
            v_content += dbox.getString("d_user_name") + "님 오늘 하루도 행복한 하루 되시길 바랍니다.";
            	     
            box.put("p_mail_code", "003");
            box.put("from_name", conf.getProperty("mail.admin.name"));
            box.put("from_email", conf.getProperty("mail.admin.email"));
            box.put("p_title", "[e-Eureka] " + dbox.getString("d_user_name") + "님 수강신청이 완료 되었습니다.");
            box.put("p_content", v_content);
            //box.put("p_map1", dbox.getString("d_lecture_name"));
            //box.put("p_map2", dbox.getString("d_lecture_begin_date"));
            //box.put("p_map3", dbox.getString("d_lecture_end_date"));
            Vector v_to = new Vector();
            v_to.addElement(dbox.getString("d_user_id") + "|" + dbox.getString("d_user_name") + "|" + dbox.getString("d_email"));
            //v_to.addElement(dbox.getString("d_userid") + ":" + dbox.getString("d_smemail") + ":" + dbox.getString("d_smnm"));
            box.put("to", v_to);
            
            FreeMailBean bean = new FreeMailBean();
            bean.amailSendMail(box);
            isOk = 1;
        } catch ( SQLException e ) {
            isOk    = 0;
            e.printStackTrace();
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            e.printStackTrace();
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            e.printStackTrace();
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
             
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
     
        return isOk;
    }
    
    
    /**
    수강취소 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int CancelSubjSendMail(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        DataBox             dbox                = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        ConfigSet			conf				= new ConfigSet();
              
        int                 isOk                = 0;
        
        String              v_subj              = box.getString ("p_subj"           );
        String              v_subjseq           = box.getString ("p_subjseq"        );
        String              v_year              = box.getString ("p_year"           );
        String              v_userid            = box.getSession("userid"           );
        String              v_grad_string       = "";
        String              v_prefix            = "";
        String              v_content			= "";
        try { 
            connMgr = new DBConnectionManager();
            
            sbSQL.append(" SELECT b.name                                                       \n")
                 .append("     ,  b.email                                                      \n")
                 .append("     ,  c.subjnm                                                     \n")
                 .append("     ,  a.reason                              			    	   \n")
                 .append(" FROM   Tz_cancel    a, tz_member b. tz_subj c                                       \n")
                 .append(" WHERE  c.userId     = " + SQLString.Format(v_userid ) 	+ "                        \n")
                 .append(" AND    b.subj       = " + SQLString.Format(v_subj   ) 	+ "                        \n")
                 .append(" AND    b.year       = " + SQLString.Format(v_year   ) 	+ "                        \n")
                 .append(" AND    b.subjseq    = " + SQLString.Format(v_subjseq) + "                        \n")
                 .append(" AND    a.userid = b.userid  and a.subj = c.subj ");

            //System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                dbox  = ls.getDataBox();
            }
            
            v_content = dbox.getString("d_name") + "님 안녕하세요? e-Eureka 운영자입니다. <br><br> ";
            v_content += dbox.getString("d_name") + "님께서는 <span style='color:#ff4800;font-weight:bold;'>" + dbox.getString("d_subjnm") + "</span> 과정 수강을 취소하셨습니다.<br>";
            v_content += "취소사유는 <span style='color:#ff4800;'>" + dbox.getString("d_reason") + " </span>입니다. 다음엔 더 좋은 과정으로 만나뵙기를 희망하겠습니다. <br>수강신청하신 과정에 대한 문의나 기타 궁금하신 점이 있으시면 전화 또는 메일로 문의 주시기 바랍니다.<br><br> ";
            v_content += dbox.getString("d_name") + "님 오늘 하루도 행복한 하루 되시길 바랍니다.";
            	     
            box.put("p_mail_code", "003");
            box.put("from_name", conf.getProperty("mail.admin.name"));
            box.put("from_email", conf.getProperty("mail.admin.email"));
            box.put("p_title", "[e-Eureka] " + dbox.getString("d_name") + "님 수강신청이 취소되었습니다.");
            box.put("p_content", v_content);
            Vector v_to = new Vector();
            v_to.addElement(dbox.getString("d_user_id") + "|" + dbox.getString("d_name") + "|" + dbox.getString("d_email"));
            box.put("to", v_to);
            
            FreeMailBean bean = new FreeMailBean();
            bean.amailSendMail(box);


            isOk        = 1;
        } catch ( SQLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
             
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
     
        return isOk;
    }
    

    /**
    회원가입 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int JoinMemberSendMail(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        DataBox             dbox                = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk                = 0;

        URL                 url                 = null; // URL 주소 객체
        URLConnection       urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader   isr                 = null;
        BufferedReader      br                  = null;
        String              v_url               = "";
        
        String              v_userid            = box.getString("p_userid");
        
        try { 
            connMgr = new DBConnectionManager();

            sbSQL.append(" SELECT userid, name, email                       \n");
            sbSQL.append(" FROM tz_member                                   \n");
            sbSQL.append(" WHERE userid=" + SQLString.Format(v_userid ) + " \n");
            
            System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                dbox  = ls.getDataBox();
            }
            
            
            // 운영자명|사용자명|과목명|학습시작일|학습종료일|과제제출일|학습기간|유저ID|패스워드|총점|평가기준
            // 17 : 입과안내 템플릿 번호
            v_url  = "http://mailer.womenpro.or.kr/inst_input.asp?"      ;
            v_url += "i_id=4"                                            ; 
            v_url += "&name="  + URLEncoder.encode(dbox.getString("d_name"                ), "euc-kr");
            v_url += "&email=" + URLEncoder.encode(dbox.getString("d_email"               ), "euc-kr");
            v_url += "&sub1="  + URLEncoder.encode(dbox.getString("d_name"                ), "euc-kr");
            
            url         = new URL(v_url);
            System.out.println("=================================" + URLEncoder.encode(v_url, "euc-kr"));
            urlconn     = url.openConnection();
            
            //내용을 읽어오기위한 InputStream객체를 생성한다..
            is          = urlconn.getInputStream();

            isOk        = 1;
        } catch ( SQLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
             
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
     
        return isOk;
    }
    
    /**
    비밀번호 찾기 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int PwdSendMail(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        DataBox             dbox                = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk                = 0;

        URL                 url                 = null; // URL 주소 객체
        URLConnection       urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader   isr                 = null;
        BufferedReader      br                  = null;
        String              v_url               = "";
        
        String              v_userid            = box.getString("p_userid");
        
        try { 
            connMgr = new DBConnectionManager();

            sbSQL.append(" SELECT userid, name, email, fn_crypt('2', pwd, 'knise') pwd                  \n");
            sbSQL.append(" FROM tz_member                                   \n");
            sbSQL.append(" WHERE userid=" + SQLString.Format(v_userid ) + " \n");
            
            System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                dbox  = ls.getDataBox();
            }
            
            
            // 운영자명|사용자명|과목명|학습시작일|학습종료일|과제제출일|학습기간|유저ID|패스워드|총점|평가기준
            // 17 : 입과안내 템플릿 번호
            v_url  = "http://mailer.womenpro.or.kr/inst_input.asp?"      ;
            v_url += "i_id=15"                                           ; 
            v_url += "&name="  + URLEncoder.encode(dbox.getString("d_name"                ), "euc-kr");
            v_url += "&email=" + URLEncoder.encode(dbox.getString("d_email"               ), "euc-kr");
            v_url += "&sub2="  + URLEncoder.encode(dbox.getString("d_name"                ), "euc-kr");
            v_url += "&sub3="  + URLEncoder.encode(dbox.getString("d_userid"                ), "euc-kr");
            v_url += "&sub4="  + URLEncoder.encode(StringManager.BASE64Decode(dbox.getString("d_pwd")), "euc-kr");
            
            url         = new URL(v_url);
            System.out.println("=================================" + URLEncoder.encode(v_url, "euc-kr"));
            urlconn     = url.openConnection();
            
            //내용을 읽어오기위한 InputStream객체를 생성한다..
            is          = urlconn.getInputStream();

            isOk        = 1;
        } catch ( SQLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
             
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
     
        return isOk;
    }
    
    
    /**
    14세 미만 법정대리인 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int Agree14MemberSendMail(RequestBox box) throws Exception { 
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk                = 0;

        URL                 url                 = null; // URL 주소 객체
        URLConnection       urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader   isr                 = null;
        BufferedReader      br                  = null;
        String              v_url               = "";
        
        String              v_userid            = box.getString ("p_userid"         );
        String              v_parentname        = box.getString ("p_parentname"     );
        String              v_parentemail       = box.getString ("p_parentemail1") + "@" + box.getString("p_parentemail2");
        
        try { 
            // userid
            // 17 : 14세 미만 법정대리인 자동메일
            v_url  = "http://mailer.womenpro.or.kr/inst_input.asp?"      ;
            v_url += "i_id=18"                                           ; 
            v_url += "&name="  + URLEncoder.encode(v_parentname , "euc-kr");
            v_url += "&email=" + URLEncoder.encode(v_parentemail, "euc-kr");
            v_url += "&sub1="  + URLEncoder.encode(v_userid     , "euc-kr");

            System.out.println("=========[name : v_parentname    ] : ["  + v_parentname     + "]\n");
            System.out.println("=========[email: v_parentemail   ] : ["  + v_parentemail    + "]\n");
            System.out.println("=========[SUB1 : v_userid        ] : ["  + v_userid         + "]\n");
            
            System.out.println("=========[URL] : ["  + v_url + "]\n");
            
            url         = new URL(v_url);
            System.out.println("=================================" + URLEncoder.encode(v_url, "euc-kr"));
            urlconn     = url.openConnection();
            
            //내용을 읽어오기위한 InputStream객체를 생성한다..
            is          = urlconn.getInputStream();

            isOk        = 1;
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } 
     
        return isOk;
    }

    /**
    외국인 동의 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int ForeignerSendMail(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        DataBox             dbox                = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk                = 0;

        URL                 url                 = null; // URL 주소 객체
        URLConnection       urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader   isr                 = null;
        BufferedReader      br                  = null;
        String              v_url               = "";
        
        String              v_userid            = box.getString ("p_userid"         );
        
        try { 
            connMgr = new DBConnectionManager();

            sbSQL.append(" SELECT userid, name, email                       \n");
            sbSQL.append(" FROM tz_member_type23                            \n");
            sbSQL.append(" WHERE userid=" + SQLString.Format(v_userid ) + " \n");
            
            System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                dbox  = ls.getDataBox();
            }
            
            // userid
            // 17 : 14세 미만 법정대리인 자동메일
            v_url  = "http://mailer.womenpro.or.kr/inst_input.asp?"      ;
            v_url += "i_id=20"                                           ; 
            v_url += "&name="  + URLEncoder.encode(dbox.getString("d_name") , "euc-kr");
            v_url += "&email=" + URLEncoder.encode(dbox.getString("d_email"), "euc-kr");
            v_url += "&sub1="  + URLEncoder.encode(v_userid     , "euc-kr");

            url         = new URL(v_url);
            urlconn     = url.openConnection();
            
            //내용을 읽어오기위한 InputStream객체를 생성한다..
            is          = urlconn.getInputStream();

            isOk        = 1;
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } 
     
        return isOk;
    }
    
    /**
    설문조사 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int SulmunTargetMainSendMail(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        DataBox             dbox                = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        int                 isOk                = 0;

        String              v_grcode            = box.getString ("p_grcode"     );
        int                 v_sulpapernum       = box.getInt    ("p_sulpapernum");
        String              v_gyear             = box.getString ("s_gyear"      );
        Vector              v_checks            = box.getVector ("p_checks"     ); // userid, subj, gyear,subjseq, ismailing, name, v_email
        String              v_subj              = "";
        String              v_subjseq           = "";
        String              v_ismailcnt         = "";
        String              v_userid            = "";
        String              v_name              = "";
        String              v_email             = "";
        String              v_sulpapernm        = "";
        String              v_schecks           = "";
        
        StringTokenizer     st                  = null;
        
        try { 
            connMgr = new DBConnectionManager();
            
            sbSQL.append(" SELECT sulpapernm                                                \n")
                 .append(" FROM   tz_sulpaper                                               \n")
                 .append(" WHERE  grcode        = " + SQLString.Format(v_grcode     ) + "   \n")
                 .append(" AND    year          = " + SQLString.Format(v_gyear      ) + "   \n")
                 .append(" AND    subj          = 'TARGET'                                  \n")
                 .append(" AND    sulpapernum   = " + String.valueOf(v_sulpapernum  ) + "   \n");
            
            System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                dbox            = ls.getDataBox();
                v_sulpapernm    = dbox.getString("d_sulpapernm");    
            }
            
            ls = connMgr.executeQuery(sbSQL.toString());
       
           for ( int i = 0; i < v_checks.size(); i++ ) { 
               v_schecks    = (String)v_checks.elementAt(i);
               st           = new StringTokenizer(v_schecks, ",");
               
               System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
               
               v_userid     = st.hasMoreTokens() ? (String)st.nextToken() : "";
               v_subj       = st.hasMoreTokens() ? (String)st.nextToken() : "";
               v_gyear      = st.hasMoreTokens() ? (String)st.nextToken() : "";
               v_subjseq    = st.hasMoreTokens() ? (String)st.nextToken() : "";
               v_ismailcnt  = st.hasMoreTokens() ? (String)st.nextToken() : "";
               v_name       = st.hasMoreTokens() ? (String)st.nextToken() : "";
               v_email      = st.hasMoreTokens() ? (String)st.nextToken() : "";
                
                // 설문대상자 메일 발송
                SulmunTargetSendMail(v_userid, v_name, v_email, v_grcode, v_gyear, String.valueOf(v_sulpapernum), v_sulpapernm); 
            }    

            isOk        = 1;
            
        } catch ( SQLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
             
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
     
        return isOk;
    }
    
    
    /**
    설문조사 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int SulmunTargetSendMail(String p_userid, String p_name, String p_email, String p_grcode, String p_gyear, String p_sulpapernum, String p_sulpapernm) throws Exception { 
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        int                 isOk                = 0;

        URL                 url                 = null; // URL 주소 객체
        URLConnection       urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; //URL접속에서 내용을 읽기위한 Stream
        InputStreamReader   isr                 = null;
        BufferedReader      br                  = null;
        String              v_url               = "";
        
        try { 
            // 운영자명|사용자명|과목명|학습시작일|학습종료일|과제제출일|학습기간|유저ID|패스워드|총점|평가기준
            // 17 : 설문조사 메일
            v_url  = "http://mailer.womenpro.or.kr/inst_input.asp?"      ;
            v_url += "i_id=19"                                           ; 
            v_url += "&name="  + URLEncoder.encode(p_name           , "euc-kr");
            v_url += "&email=" + URLEncoder.encode(p_email          , "euc-kr");
            v_url += "&sub1="  + URLEncoder.encode(p_userid         , "euc-kr");
            v_url += "&sub2="  + URLEncoder.encode(p_grcode         , "euc-kr");
            v_url += "&sub3="  + URLEncoder.encode(p_gyear          , "euc-kr");
            v_url += "&sub4="  + URLEncoder.encode(p_sulpapernum    , "euc-kr");
            v_url += "&sub5="  + URLEncoder.encode(p_sulpapernm     , "euc-kr");

            System.out.println(" [name  ] : [" + p_name         + "]");
            System.out.println(" [email ] : [" + p_email        + "]");
            System.out.println(" [sub1  ] : [" + p_userid       + "]");
            System.out.println(" [sub2  ] : [" + p_grcode       + "]");
            System.out.println(" [sub3  ] : [" + p_gyear        + "]");
            System.out.println(" [sub4  ] : [" + p_sulpapernum  + "]");
            System.out.println(" [sub5  ] : [" + p_sulpapernm   + "]");
            
            System.out.println("=========[URL] : ["  + v_url + "]\n");
            
            url         = new URL(v_url);

            urlconn     = url.openConnection();
            
            //내용을 읽어오기위한 InputStream객체를 생성한다..
            is          = urlconn.getInputStream();

            isOk        = 1;
            
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
        }
     
        return isOk;
    }
    
    
    /**
    ContactUs 자동메일
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int ContactUsSendMail(RequestBox box) throws Exception { 
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        int                 isOk                = 0;

        URL                 url                 = null; // URL 주소 객체
        HttpURLConnection   urlconn             = null; // URL접속을 가지는 객체
        InputStream         is                  = null; // URL접속에서 내용을 읽기위한 Stream
        DataOutputStream    out                 = null;
        BufferedReader      br                  = null;
        
        InputStreamReader   isr                 = null;
        String              v_url               = "";
        String              v_param             = "";
        byte[]              sendbyte            = null;
        String              buf                 = "";
        
        
        try { 
            // 운영자명|사용자명|과목명|학습시작일|학습종료일|과제제출일|학습기간|유저ID|패스워드|총점|평가기준
            // 21 : 설문조사 메일
            v_url  = "http://mailer.womenpro.or.kr/inst_input.asp";
            
            v_param     =       URLEncoder.encode("i_id" , "euc-kr") + "=" + URLEncoder.encode("21"                    , "euc-kr");
            v_param    += "&" + URLEncoder.encode("name" , "euc-kr") + "=" + URLEncoder.encode("경기여성e-러닝센터"       , "euc-kr");
            v_param    += "&" + URLEncoder.encode("email", "euc-kr") + "=" + URLEncoder.encode("helpdesk@kg21.net"     , "euc-kr");
            v_param    += "&" + URLEncoder.encode("sub1" , "euc-kr") + "=" + URLEncoder.encode(box.getString("p_title"), "euc-kr");
            v_param    += "&" + URLEncoder.encode("sub2" , "euc-kr") + "=" + URLEncoder.encode(box.getString("p_name" ), "euc-kr");
            v_param    += "&" + URLEncoder.encode("sub3" , "euc-kr") + "=" + URLEncoder.encode(box.getString("p_email"), "euc-kr");
            v_param    += "&" + URLEncoder.encode("sub4" , "euc-kr") + "=" + URLEncoder.encode(box.getString("p_contents").replaceAll("\r\n", "<br>"), "euc-kr");
            
            // SEND
            url         = new URL(v_url);
            urlconn     = (HttpURLConnection)url.openConnection();

            sendbyte    = new String(v_param).getBytes();
            
            urlconn.setDefaultUseCaches (false );
            urlconn.setDoInput          (true  );
            urlconn.setDoOutput         (true  );
            urlconn.setRequestMethod    ("POST");
            urlconn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            
            out         = new DataOutputStream(urlconn.getOutputStream());
            out.write(sendbyte);
            out.flush();

            // RECIEVE
            br          = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));

            while ( ( buf = br.readLine()) != null ) {
                System.out.println("kjkkkkkkkkkkkkkkkkkkkkkkkkk=" + buf);
            }
            
            br.close();

            isOk        = 1;
            
        } catch ( MalformedURLException e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [잘못된 URL입니다. \n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
        }
     
        return isOk;
    }
}
