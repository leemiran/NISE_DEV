// **********************************************************
//  1. ��      ��: MYCLASS USER BEAN
//  2. ���α׷���: MyClassBean.java
//  3. ��      ��: �����н��� ����� bean
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2003. 8. 19
//  7. ��      ��: ������ 2006.07. 03
//  8. ��      ��: �ʿ��� �ڷḸ...
// **********************************************************
package com.ziaan.study;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ziaan.common.GetCodenm;
import com.ziaan.common.SelectSubjBean;
import com.ziaan.common.SubjComBean;
import com.ziaan.complete.FinishBean;
import com.ziaan.complete.StoldData;
import com.ziaan.course.SubjGongAdminBean;
import com.ziaan.exam.ExamUserBean;
import com.ziaan.library.AutoMailBean;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.propose.ProposeBean;
import com.ziaan.propose.email.SendRegisterCoursesEMail;
import com.ziaan.propose.email.SendRegisterCoursesEMailImplBean;
import com.ziaan.propose.email.log.EmailLog;
import com.ziaan.propose.email.log.impl.EmailLogImplBean;
import com.ziaan.research.SulmunContentsUserBean;
import com.ziaan.research.SulmunSubjUserBean;
import com.ziaan.scorm2004.ScormCourseBean;
import com.ziaan.system.CodeAdminBean;

public class MyClassBean { 
    private     ConfigSet   config;
    private     int         row;
    private EmailLog emailLog;
    
    // �⼮üũ ���� �����
    final int LATE_START_MINUTE = 10;       // ����ó�� ���۽ð�
    final int LATE_END_MINUTE =60;          // ����ó�� ���ð�
    
    final String LATE_CODE = "L";           // ���� ó�� �ڵ�
    final String M_LATE_CODE = "M";         // ���� ���� ó�� �ڵ� (���� ����~��������ð�������)
    final String ATTENDANCE_CODE = "Y";     // �⼮ ó�� �ڵ�
    final String NOT_ATTENDANCE_CODE = "N";  // �⼮ �Ұ��� ó�� �ڵ�

    private String attCode_ = "";

    Logger logger = Logger.getLogger(MyClassBean.class);
    public MyClassBean() { 
        try { 
            config          = new ConfigSet();
            row             = Integer.parseInt(config.getProperty("page.bulletin.row") ); // �� ����� �������� row ���� �����Ѵ�
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    �н����� ���� ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectEducationSubjectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql          = "";
        DataBox dbox        = null;

        String v_user_id   = box.getSession("userid");
        String  v_ip        = box.getSession("userip");
        
        //String  v_isblended = box.getString("p_isblended");
        //String  v_isexpert  = box.getString("p_isexpert");
        //if(v_isblended.equals("")) v_isblended = "N";
        //if(v_isexpert.equals("")) v_isexpert = "N";

        String v_subj = "";
        String v_year = "";
        String v_subjseq = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            
            //if(v_isblended.equals("N") && v_isexpert.equals("N"))		// �⺻����
            //{
                sql = "\n select tsj.contenttype "
                	+ "\n      , tss.course "
                	+ "\n      , tss.cyear "
                	+ "\n      , tss.courseseq "
                	+ "\n      , tss.coursenm "
                	+ "\n      , tst.subj "
                	+ "\n      , tst.year "
                	+ "\n      , tst.subjseq "
                	+ "\n      , tss.grcode "
                	+ "\n      , tst.isgraduated "
                	+ "\n      , tst.branch "
                	+ "\n      , substr(tss.edustart,0,8) studystart "
                	+ "\n      , substr(tss.eduend,0,8) studyend "
                	+ "\n      , tsj.eduurl "
                	+ "\n      , tsj.isonoff "
                	+ "\n      , tsj.subjnm "
                	+ "\n      , tsj.subjclass "
                	+ "\n      , tsj.upperclass "
                	+ "\n      , tsj.middleclass "
                	+ "\n      , tsj.lowerclass "
                	+ "\n      , get_subjclassnm(tsj.upperclass, tsj.middleclass, tsj.lowerclass) as cname "
                	+ "\n      , tsj.cpsubj "
                	+ "\n      , tss.cpsubjseq "
                	+ "\n      , case when to_char(sysdate, 'yyyymmddhh24') between tss.edustart  and tss.eduend  then 'E' "
                    + "\n             when to_char(sysdate, 'yyyymmddhh24') between tss.propstart and tss.propend then 'R' "
                    + "\n             else ''"
                    + "\n        end as proceduflag "
                    //+ "\n      , tss.isattendchk "
                    + "\n      , tst.tstep "
                    + "\n      , tsj.subj_gu " 
                    + "\n      , tsj.isoutsourcing " 
                    + "\n      , tsj.height " 
                    + "\n      , tsj.width " 
                    + "\n from   tz_student tst "
                    + "\n      , vz_scsubjseq tss "
                    + "\n      , tz_stold tsd "
                    + "\n      , tz_subj tsj "
                    + "\n where  tst.userid      = ':userid' "
                    + "\n and    tst.subj        = tss.subj "
                    + "\n and    tst.year        = tss.year "
                    + "\n and    tst.subjseq     = tss.subjseq "
                    + "\n and    tst.subj        = tsd.subj(+) "
                    + "\n and    tst.year        = tsd.year(+) "
                    + "\n and    tst.subjseq     = tsd.subjseq(+) "
                    + "\n and    tst.userid      = tsd.userid(+) "
                    + "\n and    tst.subj        = tsj.subj "
                    //+ "\n and    tss.propstart   <= to_char(sysdate,'yyyymmddhh24') "
                    + "\n and    tss.edustart    <= to_char(sysdate,'yyyymmddhh24') "
                    + "\n and    tss.eduend      >= to_char(sysdate,'yyyymmddhh24') "
                 	+ "\n and    tsd.isgraduated is null " 
                    //+ "\n and    tss.isblended   = 'N' "
                    //+ "\n and    tss.isexpert    = 'N' "
                   	+ "\n union all   "     
                   	+ "\n select b.contenttype,c.course,c.cyear,c.courseseq,c.coursenm,a.subj,c.year,c.subjseq,c.grcode,'N',99,c.edustart,c.eduend,'',b.isonoff,  "
                   	+ "\n b.subjnm,subjclass,upperclass,middleclass,lowerclass,  "
                   	+ "\n get_subjclassnm(b.upperclass, b.middleclass,b.lowerclass) as cname,  "
                   	+ "\n '','','B',0,'','N' ,0,0  "
                   	+ "\n from tz_subjman a, tz_subj b,vz_scsubjseq c   "
                    + "\n where  a.userid      = ':userid' and "
                  	+ "\n a.gadmin ='P101' and   "
                   	+ "\n a.subj = b.subj and   "
                  	+ "\n a.subj = c.subj and  "
                  	+ "\n c.year = to_char(sysdate,'yyyy') and   "
                  	+ "\n substr(c.edustart,1,6) =  to_char(sysdate,'yyyymm')    "                  	                 	                   	
                   	+ "\n order  by course, studystart desc, subjnm  "; 
                
            //} 
            //else if (v_isblended.equals("Y") && v_isexpert.equals("N"))   // ����� ����
            //{

            //}
            //else if (v_isblended.equals("N") && v_isexpert.equals("Y"))   // ������ �缺 ����
            //{

            //}
            sql = sql.replaceAll( ":userid", v_user_id );
System.out.println("sql : "+sql);
            ls1 = connMgr.executeQuery(sql);

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                box.put("p_subj", ls1.getString("subj"));
                box.put("p_year", ls1.getString("year"));
                box.put("p_subjseq", ls1.getString("subjseq"));
/*
                if ( ls1.getString("isonoff").equals("OFF") ) {
            		// s : ����, ���� ����
            		SulmunSubjUserBean ssubean = new SulmunSubjUserBean();
                    ArrayList sulList = ssubean.selectSulmunList(box);
                    for(int i = 0; i < sulList.size(); i++) {
                    	DataBox dbox1 = (DataBox)sulList.get(i);
                    		
                		dbox.put("d_sulnums",dbox1.getString("d_sulnums"));
                		dbox.put("d_sulpapernum",dbox1.getString("d_sulpapernum"));
                		dbox.put("d_sulpapernm",dbox1.getString("d_sulpapernm"));
                		dbox.put("d_eachcnt",dbox1.getString("d_eachcnt"));
                		dbox.put("d_sultype",dbox1.getString("d_sultype"));
                		dbox.put("d_stdt",dbox1.getString("d_stdt"));
                		dbox.put("d_endt",dbox1.getString("d_endt"));
                    		
                    }
            		// e : ����, ���� ����
                }
*/            	
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

     /**
     �н����� ���� ����Ʈ
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList selectEducationSubjectListTop5(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet ls1         = null;
         ArrayList list1     = null;
         String sql          = "";
         DataBox dbox        = null;

         String v_user_id   = box.getSession("userid");
         String  v_ip        = box.getSession("userip");
         
         //String  v_isblended = box.getString("p_isblended");
         //String  v_isexpert  = box.getString("p_isexpert");
         //if(v_isblended.equals("")) v_isblended = "N";
         //if(v_isexpert.equals("")) v_isexpert = "N";

         String v_subj = "";
         String v_year = "";
         String v_subjseq = "";
         
         try { 
             connMgr = new DBConnectionManager();
             list1 = new ArrayList();
             
             //if(v_isblended.equals("N") && v_isexpert.equals("N"))		// �⺻����
             //{
                 sql= "\n select * from ("
                	+ "\n select tsj.contenttype "
                 	+ "\n      , tst.subj "
                 	+ "\n      , tst.year "
                 	+ "\n      , tst.subjseq "
                 	+ "\n      , tst.isgraduated "
                 	+ "\n      , tst.branch "
                 	+ "\n      , tss.edustart studystart "
                 	+ "\n      , tss.eduend studyend "
                 	+ "\n      , tsj.eduurl "
                 	+ "\n      , tsj.isonoff "
                 	+ "\n      , tsj.subjnm "
                 	+ "\n      , tsj.subjclass "
                 	+ "\n      , tsj.upperclass "
                 	+ "\n      , tsj.middleclass "
                 	+ "\n      , tsj.lowerclass "
                 	+ "\n      , get_subjclassnm(tsj.upperclass, tsj.middleclass, tsj.lowerclass) as cname "
                 	+ "\n      , tsj.cpsubj "
                 	+ "\n      , tss.cpsubjseq "
                 	+ "\n      , case when to_char(sysdate, 'yyyymmddhh24') between tss.edustart  and tss.eduend  then 'E' "
                    + "\n             when to_char(sysdate, 'yyyymmddhh24') between tss.propstart and tss.propend then 'R' "
                    + "\n             else ''"
                    + "\n        end as proceduflag "
                    + "\n      , tss.isattendchk "
                    + "\n      , tst.tstep "
                    + "\n      , tsj.subj_gu " 
                    + "\n      , tsj.isoutsourcing " 
                    + "\n from   tz_student tst "
                    + "\n      , tz_subjseq tss "
                    + "\n      , tz_stold tsd "
                    + "\n      , tz_subj tsj "
                    + "\n where  tst.userid      = ':userid' "
                    + "\n and    tst.subj        = tss.subj "
                    + "\n and    tst.year        = tss.year "
                    + "\n and    tst.subjseq     = tss.subjseq "
                    + "\n and    tst.subj        = tsd.subj(+) "
                    + "\n and    tst.year        = tsd.year(+) "
                    + "\n and    tst.subjseq     = tsd.subjseq(+) "
                    + "\n and    tst.userid      = tsd.userid(+) "
                    + "\n and    tst.subj        = tsj.subj "
                    + "\n and    tss.edustart    <= to_char(sysdate,'yyyymmddhh24') "
                  	+ "\n and    tsd.isgraduated is null "
                     //+ "\n and    tss.isblended   = 'N' "
                     //+ "\n and    tss.isexpert    = 'N' "
                     + "\n order  by tss.edustart desc, tsj.subjnm "
                     + "\n ) where rownum <= 5 ";
             //} 
             //else if (v_isblended.equals("Y") && v_isexpert.equals("N"))   // ����� ����
             //{

             //}
             //else if (v_isblended.equals("N") && v_isexpert.equals("Y"))   // ������ �缺 ����
             //{

             //}
             sql = sql.replaceAll( ":userid", v_user_id );

             ls1 = connMgr.executeQuery(sql);

             while ( ls1.next() ) { 
                 dbox = ls1.getDataBox();
                 
                 v_subj = ls1.getString("subj");
                 v_year = ls1.getString("year");
                 v_subjseq = ls1.getString("subjseq");
                 box.put("p_subj", v_subj);
                 box.put("p_year", v_year);
                 box.put("p_subjseq", v_subjseq);
             	
             	SubjGongAdminBean sgabean = new SubjGongAdminBean();
             	String v_promotion = sgabean.getPromotion(box);    
             	dbox.put("d_promotion", v_promotion);

             	
     			/* s : ���񼳹� ���ÿ��� */
     			SulmunSubjUserBean sulbean = new SulmunSubjUserBean();
     			int suldata = sulbean.getSulData(box);
     			int sulresult = sulbean.getUserData(box);
     			dbox.put("d_suldata",String.valueOf(suldata));
     			dbox.put("d_sulresult",String.valueOf(sulresult));
     			/* e : ���񼳹� ���ÿ��� */

         		// s : �������� ���ÿ��� 
         		//SulmunContentsUserBean contentsbean = new SulmunContentsUserBean();
         		//���� ������������ �մ��� Ȯ��...(2005.10.13)
         		//int ispaper = contentsbean.getContentsSulmunPaper(box);
         		//dbox.put("d_ispaper", String.valueOf(ispaper));
         		//int contentsdata = contentsbean.getUserData(box);
         		//dbox.put("d_contentsdata",String.valueOf(contentsdata));
         		// e : �������� ���ÿ���
     			
     			
         		// s : ���� ���ⰳ��
         		ProjectAdminBean report = new ProjectAdminBean();
         		int reportadmin = report.getAdminData(box);
         		dbox.put("d_report",String.valueOf(reportadmin));
         		// e : ���� ���ⰳ�� 

         		// s : ���� ���⿩��
         		ProjectAdminBean reportuser = new ProjectAdminBean();
         		int reportdata  = reportuser.getUserData(box);
         		dbox.put("d_reportdata",String.valueOf(reportdata));
         		// e : ���� ���⿩��

         		// s : �� ����
         		ExamUserBean exambean = new ExamUserBean();
         		ArrayList examdata = exambean.getUserData(box);
         		dbox.put("d_examDataM",examdata.get(0));
         		dbox.put("d_examDataH",examdata.get(1));
         		dbox.put("d_examDataE",examdata.get(2));
         		// e : �� ���� 

         		// s : �� ��������
         		ArrayList examcondition = exambean.getApplyExamConditionData(box);
         		dbox.put("d_examConditionM",examcondition.get(0));
         		dbox.put("d_examConditionH",examcondition.get(1));
         		dbox.put("d_examConditionE",examcondition.get(2));
         		// e : �� ��������
         		
         		// s : �� ���ÿ���
         		ArrayList examresultdata = exambean.getUserResultData(box);
         		dbox.put("d_examResultDataM",examresultdata.get(0));
         		dbox.put("d_examResultDataH",examresultdata.get(0));
         		dbox.put("d_examResultDataE",examresultdata.get(0));
         		// e : �� ���ÿ���

                 list1.add(dbox);
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list1;
     }

    /**
    ���� ��û���� ���� ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectProposeSubjectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        DataBox             dbox    = null;
        String sql1         = "";
        String  v_user_id   = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            sql1= "\n select a.subj "
                + "\n      , a.year "
                + "\n      , a.subjseq "
                + "\n      , a.userid "
                + "\n      , b.subjseqgr "
                + "\n      , b.scupperclass "
                + "\n      , b.scmiddleclass "
                + "\n      , b.isonoff "
                + "\n      , b.course "
                + "\n      , b.cyear "
                + "\n      , b.courseseq "
                + "\n      , b.coursenm "
                + "\n      , b.subj "
                + "\n      , b.year "
                + "\n      , b.subjseq "
                + "\n      , b.subjnm "
                + "\n      , b.propstart "
                + "\n      , b.propend "
                + "\n      , b.edustart "
                + "\n      , b.eduend "
                + "\n      , b.eduurl "
                + "\n      , b.subjtarget "
                + "\n      , a.chkfinal "
                + "\n      , a.cancelkind "
                + "\n      , b.contenttype "
                + "\n      , b.preurl "
                + "\n      , get_subjclassnm(b.scupperclass, b.scmiddleclass, '000') as middleclassnm "                      
                + "\n      , b.subj_gu "
                + "\n      , b.usebook " 
                + "\n      , b.bookname "                   
                + "\n      , get_codenm('0004',b.isonoff) isonoffvalue "                           
                + "\n from   tz_propose a "
                + "\n      , vz_scsubjseq b "
                + "\n where  a.subj = b.subj "
                + "\n and    a.year = b.year "
                + "\n and    a.subjseq = b.subjseq "
                + "\n and    a.userid = " + StringManager.makeSQL(v_user_id)
                + "\n and    ((a.chkfinal = 'Y' and (to_char(sysdate,'YYYYMMDDHH24') < b.edustart))  "
                + "\n        or (a.chkfinal='N' and (to_char(sysdate,'YYYYMMDDHH24') < to_char(to_date(substr(b.edustart,1,8))+10,'YYYYMMDDHH24')))  "
                + "          or (a.chkfinal='B' and (to_char(sysdate,'YYYYMMDDHH24') < b.propend)) \n"                   
                + "\n        or (a.chkfinal='B' and (to_char(sysdate,'YYYYMMDDHH24') < b.edustart))) "
                + "\n and    nvl(a.cancelkind,' ') not in ('P','F') "
                + "\n order  by "
                + "\n        b.course "                
                + "\n      , b.edustart desc "
            	+ "\n      , b.subjnm " ;          
            

            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ){ 
                dbox = ls1.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /**
    ���� �Ϸ��� ���� ����Ʈ(�н����ΰ��� -> �н��� ��ģ ����)
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectGraduationSubjectBLList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1          = null;
        ArrayList list1      = null;
        String sql1          = "";
        DataBox dbox         = null;
        String  v_user_id    = box.getSession("userid");
        String  v_isblended = box.getString("p_isblended");
        String  v_isexpert = box.getString("p_isexpert");
        
        if(v_isblended.equals("")) v_isblended = "N";
        if(v_isexpert.equals("")) v_isexpert = "N";
        
        String  v_isreview    = "";
        String  v_website     = "";

        String  v_birth_date = "";
        ProposeBean probean = new ProposeBean();
        Hashtable outdata = new Hashtable();

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            outdata = probean.getMeberInfo(v_user_id);
            v_birth_date = (String)outdata.get("birth_date");
            

            sql1 += " select  tsj.contenttype                                                                                                                                \n";
            sql1 += "     ,   tss.subj                                                                                                                                       \n";
            sql1 += "     ,   tss.year                                                                                                                                       \n";
            sql1 += "     ,   tss.subjseq                                                                                                                                    \n";
            sql1 += "     ,   tst.isgraduated                                                                                                                                \n";
            sql1 += "     ,  decode(tst.isgraduated,'Y','����','�̼���') isgraduated_value                                                                                    \n";     
            sql1 += "     ,   tss.edustart    studystart                                                                                                                     \n";
            sql1 += "     ,   tss.eduend      studyend                                                                                                                       \n";
            sql1 += "     ,   tsj.eduurl                                                                                                                                     \n";
            sql1 += "     ,   tsj.isonoff                                                                                                                                    \n";
            sql1 += "     ,   tsj.subjnm                                                                                                                                     \n";
            sql1 += "     ,   tsj.subjclass                                                                                                                                  \n";
            sql1 += "     ,   CASE WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24') BETWEEN tss.EduStart  AND tss.EduEnd  THEN 'E'                                                      \n";
            sql1 += "             WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24')  BETWEEN tss.PropStart AND tss.PropEnd THEN 'R'                                                      \n";
            sql1 += "             ELSE ''                                                                                                                                    \n";
            sql1 += "         END    ProcEduFlag,                                                                                                                            \n";
            sql1 += "        'N' review                                                                                                                                      \n";
            sql1 += "     ,   a.courseyear                                                                                                                                   \n";
            sql1 += "     ,   a.courseseq                                                                                                                                    \n";
            sql1 += "     ,   a.coursecode                                                                                                                                   \n";
            sql1 += "     ,   a.isrequired                                                                                                                                   \n";
            sql1 += "     ,  a.userid                                                                                                                                        \n";
            sql1 += "     ,  a.reedu                                                                                                                                         \n";
            sql1 += "    ,   COUNT(*) OVER(PARTITION BY a.Courseyear, a.coursecode, a.courseseq, a.isonoff) oncnt                                                            \n";
            sql1 += "    ,   COUNT(*) OVER(PARTITION BY a.Courseyear, a.coursecode, a.courseseq           ) coursecnt                                                        \n";
            sql1 += "    ,   tbc.coursenm                                                                                                                                    \n";
            sql1 += "    ,   tbs.isonlinecomplete                                                                                                                            \n";
            sql1 += "    ,   tbst.status                                                                                                                                     \n";
            sql1 += " FROM    (                                                                                                                                              \n";
            sql1 += "             SELECT  a.courseyear                                                                                                                       \n";
            sql1 += "                 ,   a.courseseq                                                                                                                        \n";
            sql1 += "                 ,   a.coursecode                                                                                                                       \n";
            sql1 += "                 ,   a.subj                                                                                                                             \n";
            sql1 += "                 ,   a.year                                                                                                                             \n";
            sql1 += "                 ,  a.subjseq                                                                                                                           \n";
            sql1 += "                 ,   a.isrequired                                                                                                                       \n";
            sql1 += "                 ,  a.isgraduated                                                                                                                       \n";
            sql1 += "                 ,  a.userid                                                                                                                            \n";
            sql1 += "                 ,  a.isonoff                                                                                                                           \n";
            sql1 += "                ,   a.reedu                                                                                                                             \n";
            sql1 += "             FROM    (                                                                                                                                  \n";
            sql1 += "                         select  a.courseyear, a.courseseq, a.coursecode                                                                                \n";
            sql1 += "                             ,   c.subj                                                                                                                 \n";
            sql1 += "                            ,   c.year                                                                                                                  \n";
            sql1 += "                            ,   c.subjseq                                                                                                               \n";
            sql1 += "                            ,   c.isrequired                                                                                                            \n";
            sql1 += "                            ,   d.isgraduated                                                                                                           \n";
            sql1 += "                            ,   a.userid                                                                                                                \n";
            sql1 += "                            ,   'ON'    isonoff                                                                                                         \n";
            sql1 += "                            ,   'N'                 reedu                                                                                               \n";
            sql1 += "                         from    tz_blstold        a                                                                                                  \n";
            sql1 += "                             ,   tz_blcourseseq      b                                                                                                  \n";
            sql1 += "                             ,   tz_blproposesubj    c                                                                                                  \n";
            sql1 += "                             ,   tz_student         d                                                                                                   \n";
            sql1 += "                         where   a.coursecode    = b.coursecode                                                                                         \n";
            sql1 += "                         and     a.courseyear    = b.courseyear                                                                                         \n";
            sql1 += "                         and     a.courseseq     = b.courseseq                                                                                          \n";
            sql1 += "                         and     a.coursecode    = c.coursecode                                                                                         \n";
            sql1 += "                         and     a.courseyear    = c.courseyear                                                                                         \n";
            sql1 += "                         and     a.courseseq     = c.courseseq                                                                                          \n";
            sql1 += "                         and     a.userid        = c.userid                                                                                             \n";
            sql1 += "                         and     c.subj          = d.subj   (+)                                                                                         \n";
            sql1 += "                         and     c.userid        = d.userid (+)                                                                                         \n";
            sql1 += "                         and     c.year          = d.year   (+)                                                                                         \n";
            sql1 += "                         and     c.subjseq       = d.subjseq(+)                                                                                         \n";
            sql1 += "                         and     (c.status = 'RE' or c.status = 'FE')                                                                                   \n";
            sql1 += "                         UNION ALL                                                                                                                      \n";
            sql1 += "                         select  a.courseyear, a.courseseq, a.coursecode                                                                                \n";
            sql1 += "                             ,   a.subj                                                                                                                 \n";
            sql1 += "                            ,   a.year                                                                                                                  \n";
            sql1 += "                            ,   a.subjseq                                                                                                               \n";
            sql1 += "                            ,   a.isgraduated                                                                                                           \n";
            sql1 += "                            ,   a.isrequired                                                                                                            \n";
            sql1 += "                            ,   a.userid                                                                                                                \n";
            sql1 += "                            ,   a.isonoff                                                                                                               \n";
            sql1 += "                            ,   'Y'                 reedu                                                                                               \n";
            sql1 += "                        FROM   (                                                                                                                        \n";
            sql1 += "                         select  a.courseyear, a.courseseq, a.coursecode                                                                                \n";
            sql1 += "                             ,   d.subj                                                                                                                 \n";
            sql1 += "                            ,   d.year                                                                                                                  \n";
            sql1 += "                            ,   d.subjseq                                                                                                               \n";
            sql1 += "                            ,   d.isgraduated                                                                                                           \n";
            sql1 += "                            ,   c.isrequired                                                                                                            \n";
            sql1 += "                            ,   a.userid                                                                                                                \n";
            sql1 += "                            ,   'ON'    isonoff                                                                                                         \n";
            sql1 += "                            ,   RANK() OVER(PARTITION BY d.UserId, d.Subj ORDER BY d.score, d.subj, d.subjseq DESC) rank                                \n";
            sql1 += "                         from    tz_blstold        a                                                                                                  \n";
            sql1 += "                             ,   tz_blcourseseq      b                                                                                                  \n";
            sql1 += "                             ,   tz_blproposesubj    c                                                                                                  \n";
            sql1 += "                             ,   tz_stold           d                                                                                                   \n";
            sql1 += "                         where   a.coursecode    = b.coursecode                                                                                         \n";
            sql1 += "                         and     a.courseyear    = b.courseyear                                                                                         \n";
            sql1 += "                         and     a.courseseq     = b.courseseq                                                                                          \n";
            sql1 += "                         and     a.coursecode    = c.coursecode                                                                                         \n";
            sql1 += "                         and     a.courseyear    = c.courseyear                                                                                         \n";
            sql1 += "                         and     a.courseseq     = c.courseseq                                                                                          \n";
            sql1 += "                         and     a.userid        = c.userid                                                                                             \n";
            sql1 += "                         and     c.subj          = d.subj                                                                                               \n";
            sql1 += "                         and     c.userid       = d.userid                                                                                              \n";
            sql1 += "                         and     c.status        = 'RV'                                                                                                 \n";
            sql1 += "                         and     add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart, 'YYYYMMDD')            \n";
            sql1 += "                        )       a                                                                                                                       \n";
            sql1 += "                        WHERE   rank = 1                                                                                                                \n";
            sql1 += "                         UNION ALL                                                                                                                      \n";
            sql1 += "                         select  a.courseyear, a.courseseq, a.coursecode                                                                                \n";
            sql1 += "                             ,   a.subj, a.year, a.subjseq, 'Y' isrequired                                                                              \n";
            sql1 += "                            ,   d.isgraduated                                                                                                           \n";
            sql1 += "                             , a.userid, 'OFF' isonoff                                                                                                  \n";
            sql1 += "                            ,   'N'                 reedu                                                                                               \n";
            sql1 += "                         from    (                                                                                                                      \n";
            sql1 += "                                     SELECT  a.courseyear                                                                                               \n";
            sql1 += "                                         ,   a.coursecode                                                                                               \n";
            sql1 += "                                         ,   a.courseseq                                                                                                \n";
            sql1 += "                                         ,   c.subj                                                                                                     \n";
            sql1 += "                                         ,   c.year                                                                                                     \n";
            sql1 += "                                         ,   c.subjseq                                                                                                  \n";
            sql1 += "                                         ,   a.userid                                                                                                   \n";
            sql1 += "                                         ,   c.isonoff                                                                                                  \n";
            sql1 += "                                     FROM    Tz_blstudent       a                                                                                       \n";
            sql1 += "                                         ,   tz_blcourseseqsubj  c                                                                                      \n";
            sql1 += "                                     WHERE   a.courseseq            = c.courseseq                                                                       \n";
            sql1 += "                                     AND        a.courseyear        = c.courseyear                                                                      \n";
            sql1 += "                                     AND        a.coursecode        = c.coursecode                                                                      \n";
            sql1 += "                                 )                   a                                                                                                  \n";
            sql1 += "                             ,   tz_blcourseseq      b                                                                                                  \n";
            sql1 += "                             ,   tz_student          d                                                                                                  \n";
            sql1 += "                          where  a.coursecode    = b.coursecode                                                                                         \n";
            sql1 += "                          and    a.courseyear    = b.courseyear                                                                                         \n";
            sql1 += "                          and    a.courseseq     = b.courseseq                                                                                          \n";
            sql1 += "                          and    a.subj          = d.subj(+)                                                                                            \n";
            sql1 += "                          and    a.userid        = d.userid(+)                                                                                          \n";
            sql1 += "                          and    a.year          = d.year(+)                                                                                            \n";
            sql1 += "                          and    a.subjseq       = d.subjseq(+)                                                                                         \n";
            sql1 += "                          and    a.isonoff       = 'OFF'                                                                                                \n";
            sql1 += "                     )       a                                                                                                                          \n";
            sql1 += "         )       a                                                                                                                                      \n";
            sql1 += "     ,   tz_stold       tst                                                                                                                             \n";
            sql1 += "     ,   tz_subjseq     tss                                                                                                                             \n";
            sql1 += "     ,   tz_subj        tsj                                                                                                                             \n";
            sql1 += "    ,   tz_bl_course   tbc                                                                                                                              \n";
            sql1 += "    ,   tz_blcourseseq tbs                                                                                                                              \n";
            sql1 += "    ,   tz_blstold   tbst                                                                                                                             \n";
            sql1 += " WHERE   a.userid(+)     = " + SQLString.Format(v_user_id) + "                                                                                                                    \n";
            sql1 += " and     a.subj          = tst.subj(+)                                                                                                                  \n";
            sql1 += " and     a.subjseq       = tst.subjseq(+)                                                                                                               \n";
            sql1 += " and     a.year          = tst.year(+)                                                                                                                  \n";
            sql1 += " and     a.userid       = tst.userid(+)                                                                                                                 \n";
            sql1 += " and     a.subj             = tss.subj                                                                                                                  \n";
            sql1 += " and     a.year             = tss.year                                                                                                                  \n";
            sql1 += " and     a.subjseq      = tss.subjseq                                                                                                                   \n";
            sql1 += " and     a.subj          = tsj.subj                                                                                                                     \n";
            sql1 += " and        a.coursecode    = tbc.coursecode                                                                                                            \n";
            sql1 += " and        a.courseyear    = tbs.courseyear                                                                                                            \n";
            sql1 += " and        a.coursecode    = tbs.coursecode                                                                                                            \n";
            sql1 += " and        a.courseseq     = tbs.courseseq                                                                                                             \n";
            sql1 += " and     tbst.coursecode  = a.coursecode                                                                                                                \n";
            sql1 += " and     tbst.courseyear  = a.courseyear                                                                                                                \n";
            sql1 += " and     tbst.courseseq   = a.courseseq                                                                                                                 \n";
            sql1 += " and     tbst.userid      = a.userid                                                                                                                    \n";
            sql1 += " ORDER BY a.CourseYear, a.CourseCode, a.CourseSeq, a.isOnOff DESC, a.Subj                                                                               \n";


            
            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
            	dbox = ls1.getDataBox();
            	list1.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
     
     
     
     /**
     ���� �Ϸ��� ���� ����Ʈ(�н����ΰ��� -> �н��� ��ģ ����)
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList selectGraduationSubjectList(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr = null;
         ListSet ls1          = null;
         ArrayList list1      = null;
         String sql           = "";
         DataBox dbox         = null;
         String  v_user_id    = box.getSession("userid");
         //String  v_isblended = box.getString("p_isblended");
         //String  v_isexpert = box.getString("p_isexpert");
         
         //if(v_isblended.equals("")) v_isblended = "N";
         //if(v_isexpert.equals("")) v_isexpert = "N";
         
         try { 
        	 //String  v_stoldyear = box.getStringDefault("p_stoldyear",FormatDate.getDate("yyyy"));
        	 String  v_stoldyear = box.getStringDefault("p_stoldyear","ALL");
        	 
             connMgr = new DBConnectionManager();
             list1 = new ArrayList();

             sql= "\n select tss.contenttype "
                 + "\n     , tss.course "
                 + "\n     , tss.cyear "
                 + "\n     , tss.courseseq "
                 + "\n     , tss.coursenm "            	 
             	+ "\n      , tss.subj "
             	+ "\n      , tss.year "
             	+ "\n      , tss.subjseq "
             	+ "\n      , tsd.userid "
             	+ "\n      , tss.edustart "
             	+ "\n      , tss.eduend "
             	+ "\n      , tss.isclosed "
             	+ "\n      , tsd.isrestudy "
             	+ "\n      , tsd.isgraduated "
             	+ "\n      , (case when tsd.isgraduated = 'Y' then '����' else '�̼���' end) isgraduated_value "
             	+ "\n      , tss.subjseqisablereview "
             	+ "\n      , to_char(to_date(substr(tss.eduend, 1, 8), 'yyyymmdd')+1,'yyyymmdd') as studystart "
             	+ "\n      , to_char(add_months(to_date(substr(tss.eduend, 1, 8), 'yyyymmdd'), tss.subjseqreviewdays),'yyyymmdd') as studyend "
             	+ "\n      , get_compnm(tsd.comp) as compnm "
             	+ "\n      , (case when sysdate <= add_months(to_date(substr(tss.eduend, 1, 8), 'yyyymmdd'), tss.subjseqreviewdays) then 'Y' else 'N' end) as isreview "
             	+ "\n      , tss.subjnm "
             	+ "\n      , tss.scsubjclass as subjclass "
             	+ "\n      , tss.scupperclass as upperclass "
             	+ "\n      , tss.scmiddleclass as middleclass "
             	+ "\n      , tss.sclowerclass as lowerclass "
             	+ "\n      , tss.isonoff "
             	+ "\n      , tsd.score "
             	+ "\n      , tss.cpsubj "
             	+ "\n      , tss.cpsubjseq "
             	+ "\n      , tsd.userid	gubun "
             	+ "\n      , tss.subjseqisablereview "
             	+ "\n      , tss.isoutsourcing "
             	+ "\n      , tss.subj_gu "
             	+ "\n      , tsd.notgraducd "
             	+ "\n      , tss.isgoyong "      
             	+ "\n  from   tz_stold tsd, vz_scsubjseq tss ,tz_student tst "
             	+ "\n	where tst.userid 	= " + SQLString.Format(v_user_id)
             	+ "\n	and tsd.subj(+)		= tst.subj  "
             	+ "\n	and tsd.year(+)		= tst.year	"
             	+ "\n   and tsd.subjseq(+)	= tst.subjseq "  
             	+ "\n	and tsd.userid(+) 	= tst.userid " 
             	+ "\n	and tst.subj          = tss.subj "  
             	+ "\n	and tst.year          = tss.year "  
             	+ "\n	and tst.subjseq       = tss.subjseq  "
             	+ "\n	and tss.EDUSTART < TO_CHAR(sysdate, 'YYYYMMDD')  "
///20100531����
//             	+ "\n from   tz_stold tsd, vz_scsubjseq tss "
//             	+ "\n where  tsd.subj = tss.subj "
//             	+ "\n and    tsd.year = tss.year "
//             	+ "\n and    tsd.subjseq = tss.subjseq "
//             	+ "\n and    tsd.userid = " + SQLString.Format(v_user_id)
//end             	

             	///+ "\n and    tss.isblended = " + StringManager.makeSQL(v_isblended)
             	///+ "\n and    tss.isexpert = " + StringManager.makeSQL(v_isexpert)
             	// �����̷��� �����ֱ� ���� �����Ⱓ�� ��� 2008�� �������� �� �����Բ� �Ѵ�.
             	+ "\n and    tss.eduend  >=  (case when to_number(substr(tss.eduend,1,4)) <= 2008 then tss.eduend else to_char(add_months(to_date(substr(tss.eduend, 1, 8), 'yyyymmdd'), -tss.subjseqreviewdays),'yyyymmdd') end) ";
             
             	if(!v_stoldyear.equals("ALL")){
                    sql += " \n and    tsd.year = " + SQLString.Format(v_stoldyear);
             	}
             	
               sql += " \n order  by tss.course, tss.edustart desc, tss.subjnm ";   
System.out.println("----����--" + sql);
             ls1 = connMgr.executeQuery(sql);

             String[] arr_notgraducd;
             String v_notgradunm = "";
             while ( ls1.next() ) { 
            	 dbox = ls1.getDataBox();
            	 // �̼��� ����
            	 arr_notgraducd = dbox.getString("d_notgraducd").split(",");
            	 for (int i=0; i<arr_notgraducd.length; i++) {
            		 if (i == 0) {
            			 v_notgradunm = GetCodenm.get_codenm("0028", arr_notgraducd[i]);
            		 } else {
            			 v_notgradunm += " , " + GetCodenm.get_codenm("0028", arr_notgraducd[i]);
            		 }
            	 }
            	 dbox.put("d_notgradunm", v_notgradunm);
            	 list1.add(dbox);
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list1;
     }

     



   /**
    ��ҽ�û���� ���񸮽�Ʈ ��ȸ
    @param box          receive from the form object and session
    @return ArrayList   ��ҽ�û���� ���񸮽�Ʈ
    */
    public ArrayList selectCancelPossibleList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        String  v_comp     = box.getSession("comp");
        String  gyear      = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
        String  v_userid     = box.getSession("userid");
        
        String  v_isblended = box.getString("p_isblended");
        String  v_isexpert = box.getString("p_isexpert");
        if(v_isblended.equals("")) v_isblended = "N";
        if(v_isexpert.equals("")) v_isexpert = "N";
        
        int                 v_pageno        = box.getInt("p_pageno");
        String v_lsearchtext = box.getString("p_lsearchtext");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  =  "select                                                                                                                 \n" +
                    "       a.userid,                                                                                                       \n" +
                    "       b.subj,                                                                                                         \n" +
                    "       b.year,                                                                                                         \n" +
                    "       b.subjseq,                                                                                                      \n" +
                    "       b.subjseqgr,                                                                                                    \n" +
                    "       b.subjnm,                                                                                                       \n" +
                    "       b.propstart,                                                                                                    \n" +
                    "       b.propend,                                                                                                      \n" +
                    "       b.edustart,                                                                                                     \n" +
                    "       b.eduend,                                                                                                       \n" +
                    "       b.isonoff,                                                                                                      \n" +
                    "       a.chkfinal,                                                                                                     \n" +
                    "       a.cancelkind,                                                                                                   \n" +
                    "       classname,                                                                                                      \n" +
                    "       TO_CHAR(TO_DATE(a.ldate, 'YYYYMMDDHH24MISS'), 'MM\"��\" DD\"��\"') propose_date,                                 \n"  +
                    "       to_char(TO_DATE(substr(nvl(b.propend,'2000010100'),0,8),'YYYYMMDD')+nvl(b.canceldays,0), 'YYYYMMDD') cancelend,  \n" +
                    "       c.classname  \n" +                    
                    "  from                                                                                                                 \n" +
                    "       TZ_PROPOSE a,                                                                                                   \n" +
                    "       VZ_SCSUBJSEQ b,                                                                                                 \n" +
                    "       tz_subjatt c                                                                                                    \n" +
                    " where                                                                                                                 \n" +
                    "       a.subj = b.subj                                                                                                 \n" +
                    "   and a.year = b.year                                                                                                 \n" +
                    "   and a.subjseq = b.subjseq                                                                                           \n" +
                    "   and a.userid = '" + v_userid + "'                                                                                   \n" +
                    "   and a.chkfinal != 'N'                                                                                               \n" +
                    "   and to_char(sysdate,'YYYYMMDDHH') between b.propstart and (to_char(TO_DATE(substr(nvl(b.propend,'2000010100'),0,8),'YYYYMMDD')+1+b.canceldays, 'YYYYMMDD') || '00')   \n" +
                    "   and b.scupperclass = c.upperclass                                                                                   \n" +
                    "   and b.scsubjclass = c.subjclass                                                                                     \n" +
                    "   and b.isblended = '"+v_isblended+"'                                                                                               \n" +
                    "   and b.isexpert = '"+v_isexpert+"'                                                                                                \n";
            
            if(!"".equals(v_lsearchtext)) {
                sql += " and b.subjnm like " + StringManager.makeSQL("%" + v_lsearchtext + "%") + "         \n";
            }
                sql+="order by b.scupperclass, b.scmiddleclass, b.subjnm, b.edustart desc                   \n";
            
//                System.out.println("\n\n\n"+sql+"\n\n\n");
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                                // �������� row ������ �����Ѵ�.
            ls.setCurrentPage(v_pageno);                        // ������������ȣ�� �����Ѵ�.
            int     total_page_count    = ls.getTotalPage();    // ��ü ������ ���� ��ȯ�Ѵ�.
            int     total_row_count     = ls.getTotalCount();   // ��ü row ���� ��ȯ�Ѵ�.

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
                
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }



    /**
    ������û���
    @param box      receive from the form object and session
    @return int
    */
    public int updateProposeCancel(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        String				sql				= "";
        int                 iSysAdd         = 0; // System.out.println�� Mehtod���� ����ϴ� ������ ǥ���ϴ� ���� 

        int                 isOk1           = 0;
        int                 isOk2           = 0;
        int                 isOk3           = 0;
        
        int                 cancel_cnt      = 0;
        String              v_subj          = box.getString ("p_subj"    );
        String              v_year          = box.getString ("p_year"    );
        String              v_subjseq       = box.getString ("p_subjseq" );
        String              v_reasoncd      = box.getString("p_reasoncd");
        String              v_reason        = box.getString("p_reason");
        String              v_user_id       = box.getSession("userid"   );
        String              v_eduterm       = "";
        
        ProposeBean         probean         = new ProposeBean();
        SubjComBean         scbean          = new SubjComBean();

        Hashtable           insertData      = new Hashtable();
        
        String order_id="";
        /////////�ѹ��� �߼۵� email�� flag�� DB���� �����´�./////////////////////
        emailLog=new EmailLogImplBean();
    	int p_tabseq=emailLog.getMaxTabseq();
        //System.out.println("p_tabseq================>"+p_tabseq);
        box.put("p_tabseq", p_tabseq);
        
        try { 
            connMgr = new DBConnectionManager();            
            connMgr.setAutoCommit(false);
            
            order_id  = getOrderId(v_subj,v_subjseq,v_user_id,v_year);
            
            if ( GetCodenm.chkIsSubj(connMgr,v_subj).equals("S")){
            // update TZ_PROPOSE table
            v_eduterm   = scbean.getEduTerm(v_subj, v_subjseq, v_year);

            insertData.clear();
            
            insertData.put("connMgr"        , connMgr       );
            insertData.put("subj"           , v_subj        );
            insertData.put("subjseq"        , v_subjseq     );
            insertData.put("year"           , v_year        );
            insertData.put("userid"         , v_user_id     );
            insertData.put("cancelkind"     , "P"           );
            insertData.put("chkfinal"       , "N"           );
            insertData.put("rejectedkind"   , v_reasoncd    );
            insertData.put("rejectedreason" , v_reason    );
            insertData.put("gubun"          , "2"           );
            insertData.put("luserid"        , v_user_id     );

            // 1 : ������û��, 2 : ������û�Ⱓ, 3 : �������Ⱓ, 4 : �����Ⱓ, 5 : ����������, �� �� : 0
            
            if ( v_eduterm.equals("0") || v_eduterm.equals("1") || v_eduterm.equals("2") || v_eduterm.equals("3") ) { 
                isOk1   = probean.deletePropose(insertData);
                isOk2   = probean.deleteStudent(insertData);
                if(isOk1 > 0 && isOk2 > 0 ) {
	                probean.deleteProposeBook(insertData);
	                probean.deleteDeliveryInfo(insertData);
                }
                isOk3   = 1;
                
                if("Y".equals(CodeAdminBean.getIsScorm(v_subj))) {
                	try {
                		/* ���� �������� ��쿡�� ó�� */
    	                ScormCourseBean scb = new ScormCourseBean();
    	                String [] emp_id = { v_user_id };
    	                boolean result = scb.deleteTreeObjectForUsers(v_subj, v_year, v_subjseq, emp_id );
    	                /* ���� �������� ��쿡�� ó�� �� */ 
                    } catch( IOException ioe ) {
                    	isOk3  = -3;
                    } catch( Exception e ) {
                    	isOk3  = -3;
                    }
                }
            } else if ( v_eduterm.equals("4") ) { 
                isOk1   = probean.updatePropose(insertData);
                isOk2   = probean.updateStudent(insertData);
                if(isOk1 > 0 && isOk2 > 0 ) {
	                probean.deleteProposeBook(insertData);
	                probean.deleteDeliveryInfo(insertData);
                }
            } else {
            	isOk1 = 1;
            	isOk2 = 1;
            }
                sbSQL.setLength(0);

                // insert TZ_CANCEL table
                    sbSQL.append(" INSERT INTO Tz_Cancel                                \n")      
                         .append(" (                                                    \n")
                         .append("         subj                                         \n")
                         .append("     ,   year                                         \n")
                         .append("     ,   subjseq                                      \n")
                         .append("     ,   userid                                       \n")
                         .append("     ,   seq                                          \n")
                         .append("     ,   cancelkind                                   \n")
                         .append("     ,   canceldate                                   \n")
                         .append("     ,   luserid                                      \n")
                         .append("     ,   ldate                                        \n")
                         .append("     ,   reason                                       \n")
                         .append("     ,   reasoncd                                     \n")
                         .append(" ) 													\n")
                         .append("select ?, ?, ?, ?, nvl(max(seq), 0) + 1, 'P', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, ?				\n")
                         .append("from tz_cancel										\n")
                         .append("where subj = ?										\n")
                         .append("and year = ?											\n")
                         .append("and subjseq= ?										\n")
                         .append("and userid=?											\n");
                    
                    pstmt   = connMgr.prepareStatement(sbSQL.toString());
                    
                    pstmt.setString(1, v_subj       );
                    pstmt.setString(2, v_year       );
                    pstmt.setString(3, v_subjseq    );
                    pstmt.setString(4, v_user_id    );
                    pstmt.setString(5, v_user_id    );
                    pstmt.setString(6, v_reason    );
                    pstmt.setString(7, v_reasoncd    );
                    pstmt.setString(8, v_subj       );
                    pstmt.setString(9, v_year       );
                    pstmt.setString(10, v_subjseq    );
                    pstmt.setString(11, v_user_id    );
                    
                    isOk3   = pstmt.executeUpdate();


            
            } else {
            	sql = "select subj, year, subjseq                          		\n"
            		+ "from vz_scsubjseq                                  	 	\n"
            		+ "where course = " + SQLString.Format(v_subj) + "     		\n"
            		+ "and cyear = " + SQLString.Format(v_year) + "        		\n"
            		+ "and courseseq = " + SQLString.Format(v_subjseq) + " 		\n";	
            	ls = connMgr.executeQuery(sql);
            		
            	while(ls.next()) {
            		v_subj = ls.getString("subj");
            		v_year = ls.getString("year");
            		v_subjseq = ls.getString("subjseq");
            		//            	 update TZ_PROPOSE table
	                v_eduterm   = scbean.getEduTerm(v_subj, v_subjseq, v_year);

	                insertData.clear();	                
	                
	                insertData.put("connMgr"        , connMgr       );
	                insertData.put("subj"           , v_subj        );
	                insertData.put("subjseq"        , v_subjseq     );
	                insertData.put("year"           , v_year        );
	                insertData.put("userid"         , v_user_id     );
	                insertData.put("cancelkind"     , "P"           );
	                insertData.put("chkfinal"       , "N"           );
	                insertData.put("rejectedkind"   , v_reasoncd    );
	                insertData.put("rejectedreason" , v_reason    );
	                insertData.put("gubun"          , "2"           );
	                insertData.put("luserid"        , v_user_id     );	                
	
	                
	                // 1 : ������û��, 2 : ������û�Ⱓ, 3 : �������Ⱓ, 4 : �����Ⱓ, 5 : ����������, �� �� : 0
	                
	                if ( v_eduterm.equals("1") || v_eduterm.equals("2") || v_eduterm.equals("3") ) {
	                	isOk1   = probean.deletePropose(insertData);
	                    isOk2   = probean.deleteStudent(insertData);
	                    isOk3   = probean.deleteProposeBook(insertData);
	                } else if ( v_eduterm.equals("4") ) {
	                	isOk3   = probean.insertCancel(insertData);
	                	isOk1   = probean.deletePropose(insertData);
	                    isOk2   = probean.deleteStudent(insertData);     
	                    isOk3   = probean.deleteProposeBook(insertData);
	                }
            	}
            }
            
            //�������� �����ϵ��� ������Ʈ
            int isOk4 = 0;
            String sql1="update pa_payment set useyn='N' where userid=? and leccode=? and lecnumb=?";
            pstmt   = connMgr.prepareStatement(sql1);
            pstmt.setString(1, v_user_id);
            pstmt.setString(2, v_subj);
            pstmt.setString(3, v_subjseq);
            
            isOk4 = pstmt.executeUpdate();
            
            //��ҽ� �ֹ���ȣ�� ������̺� �ִ´�.
            sql1=" update tz_cancel set order_id=? where userid=? and subj=? and subjseq=? and year=? and seq = " +
            	 " (select max(seq) from tz_cancel where  userid=? and subj=? and subjseq=? and year=? )";
//            System.out.println(sql1);
//            System.out.println(order_id);
//            System.out.println(v_user_id);
//            System.out.println(v_subj);
//            System.out.println(v_subjseq);
//            System.out.println(v_year);            
//            System.out.println(v_user_id);
//            System.out.println(v_subj);
//            System.out.println(v_subjseq);
//            System.out.println(v_year);
            pstmt   = connMgr.prepareStatement(sql1);
            pstmt.setString(1, order_id);
            pstmt.setString(2, v_user_id);
            pstmt.setString(3, v_subj);
            pstmt.setString(4, v_subjseq);
            pstmt.setString(5, v_year);            
            pstmt.setString(6, v_user_id);
            pstmt.setString(7, v_subj);
            pstmt.setString(8, v_subjseq);
            pstmt.setString(9, v_year);
            
            isOk4 = pstmt.executeUpdate();
            
            
            
            if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 && isOk4 > 0) { 
                connMgr.commit();
                // ��Ҹ��� �߼�
            	//AutoMailBean        bean2       = new AutoMailBean();
            	//bean2.CancelSubjSendMail(box);
                //�α� �ۼ��� ���ؼ� �α� flag�� �ִ밪�� �����´�.
//                emailLog=new EmailLogImplBean();
             
               
                //��Ҹ��� �߼�
                //SendRegisterCoursesEMail mail=new SendRegisterCoursesEMailImplBean();
                //mail.canceledRegisterCoursesMail(box);
            
            } else { 
                connMgr.rollback();
            }

        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
        
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback();
                } catch ( Exception ex ) { } 
            }
        
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return isOk1*isOk2*isOk3;
    }


    private String getOrderId(String v_subj, String v_subjseq,	String v_user_id, String v_year) throws Exception {
    	DBConnectionManager	connMgr	= null;
        ListSet ls                  = null;
        String sql                  = "";
        String order_id="";
        try { 
            connMgr = new DBConnectionManager();
            
            sql  =  "select order_id from tz_propose " +
            		"where subj = '"+v_subj+"' " +
            		"and   subjseq='"+v_subjseq+"' " +
            		"and   userid='"+v_user_id+"' " +
            		"and   year='"+v_year+"' ";
                    
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
            	order_id = ls.getString("order_id");
            }
        } catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return order_id;
	}

	/**
    �������α����̷�(���� ���� �̷�)
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectStudyHistoryTotList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        MyClassData data1= null;
        MyClassData data2= null;
        String  v_Bcourse   = ""; // �����ڽ�
        String  v_course    = ""; // �����ڽ�
        String  v_Bcourseseq= ""; // �����ڽ����
        String  v_courseseq = ""; // �����ڽ����
        String  v_subj      = ""; // ����
        String  v_year      = ""; // �⵵
        String  v_subjseq   = ""; // ������
        String  v_birth_date     = ""; // ������
        String  v_user_id   = box.getSession("userid");
        
        String	v_stext	= box.getString("p_stext").equals("")?"":box.getString("p_stext");        
        String	v_month	= box.getString("p_month").equals("")?"":box.getString("p_month");
        String	v_stype	= box.getString("p_stype").equals("")?"":box.getString("p_stype");
        
        v_year	= box.getString("p_year").equals("")?"":box.getString("p_year");
        
        String  v_isblended = box.getString("p_isblended");
        String  v_isexpert = box.getString("p_isexpert");
        
        if(v_isblended.equals("")) v_isblended = "N";
        if(v_isexpert.equals("")) v_isexpert = "N";
        
        ProposeBean probean = new ProposeBean();

        Hashtable outdata = new Hashtable();
        String v_appstatus = "";

        int     l           = 0;
        DataBox             dbox    = null;
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            list2 = new ArrayList();

            outdata = probean.getMeberInfo(v_user_id);
            v_birth_date = (String)outdata.get("birth_date");
            
            //�˻������� �����Ѵ�. ����
            sql2 = "";            
            if (!v_stext.equals("") && v_stype.equals("")) {
            		sql2 += " and A.subjnm like " + StringManager.makeSQL("%" + v_stext+ "%") + " ";
        	}
        	
            if (!v_year.equals("") && v_stype.equals("")) {
            		sql2 += " and A.year ="+SQLString.Format(v_year);
            		
			if (!v_month.equals("")) {            			
	            		sql2 += " and A.edustart<="+SQLString.Format(v_year+v_month);
            			sql2 += " and A.eduend>="+SQLString.Format(v_year+v_month);            			
        		}            		
            	}
            	
            
                   
            sql1 += "	select "; 
            sql1 += " 		  get_subjclass_fullnm(tsj.subjclass) upperclassnm, tsj.contenttype, tst.subj, tst.year, tst.subjseq, tst.userid, tss.edustart, tss.eduend,  \n "; 
            sql1 += " 		 tsd.isgraduated, decode(tsd.isgraduated,'Y','����','�̼���') isgraduated_value,  \n ";
            sql1 += " 		 to_char(to_date(substr(tsd.eduend,0,8),'yyyymmdd')+1,'yyyymmdd') studystart, to_char(to_date(substr(tsd.eduend,0,8),'yyyymmdd')+365,'yyyymmdd') studyend, tst.branch,  \n "; 
            sql1 += " 		 decode(sign(to_date(substr(tsd.eduend,0,8),'yyyymmdd')-sysdate+366),1,'Y','N') isreview,  \n "; 
            sql1 += " 		 tsj.subjnm, tsj.subjclass, tsj.upperclass, tsj.middleclass, tsj.lowerclass,  \n "; 
            sql1 += " 		 get_codenm('0004', tsj.isonoff) cname, tsj.isonoff, tss.isgoyong, tsj.biyong, tss.course , tss.cyear , tss.courseseq , tss.coursenm, tsd.score  \n ";
            sql1 += "		,(select codenm from tz_code a where a.code=c.delivery_status and gubun='0111' and code=c.delivery_status) delivery_status	\n"; 
            sql1 += "	,(select codenm from tz_code a where a.code=c.delivery_comp and gubun='0112' and code=c.delivery_comp) delivery_comp, c.delivery_number \n";
            sql1 += "	,(select url from tz_code a where a.code=c.delivery_comp and gubun='0112' and code=c.delivery_comp) url \n";
            sql1 += " 	 from tz_stold tsd, tz_student tst, tz_subj tsj, vz_scsubjseq tss, tz_bookdelivery c  \n "; 
            sql1 += " 	where tst.userid 	= " + StringManager.makeSQL(v_user_id) + " \n ";
            sql1 += "	  and tsd.subj(+)		= tst.subj  \n "; 
            sql1 += "	  and tsd.year(+)		= tst.year  \n "; 
            sql1 += " 	  and tsd.subjseq(+)	= tst.subjseq  \n "; 
            sql1 += " 	  and tsd.userid(+) 	= tst.userid  \n "; 
            sql1 += " 	  and tst.subj		    = tsj.subj  \n "; 
            sql1 += " 	 and tst.subj=c.SUBJ(+)  \n";
            sql1 += " 	 and tst.userid=c.userid(+ )  \n";
            sql1 += " 	 and tst.year=c.year(+)  \n";
            sql1 += " 	 and tst.subjseq = c.subjseq(+) \n ";
            sql1 += "     and tst.subj          = tss.subj  \n "; 
            sql1 += "     and tst.year          = tss.year  \n "; 
            sql1 += "     and tst.subjseq       = tss.subjseq  \n "; 
            sql1 += "	order by tss.course, tss.edustart desc , tss.subjnm  \n ";

//System.out.println("���� �����̷�=======>"+sql1);
            ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    dbox = ls1.getDataBox();

                    list1.add(dbox);
                }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
     
     
     /**
     �������α����̷�(���� ���� �̷�)
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList selectStudyHistoryTotList2(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet ls1         = null;
         ListSet ls2         = null;
         ArrayList list1     = null;
         ArrayList list2     = null;
         String sql1         = "";
         String sql2         = "";
         MyClassData data1= null;
         MyClassData data2= null;
       
         String  v_birth_date     = ""; 
         String  v_user_id   = box.getSession("userid");
         String v_subj = box.getString("p_subj");
         String v_year = box.getString("p_year");
         String v_subjseq = box.getString("p_subjseq");
         String v_onoff = box.getString("p_onoff");
         
         ProposeBean probean = new ProposeBean();

         Hashtable outdata = new Hashtable();
         String v_appstatus = "";

         int     l           = 0;
         DataBox             dbox    = null;
         try { 
             connMgr = new DBConnectionManager();
             list1 = new ArrayList();
             list2 = new ArrayList();

             outdata = probean.getMeberInfo(v_user_id);
             v_birth_date = (String)outdata.get("birth_date");
             
                     	
             
                    
             sql1 += "	select "; 
             sql1 += " 		 tsj.contenttype, tst.subj, tst.year, tst.subjseq, tst.userid, tss.edustart, tss.eduend,  \n "; 
             sql1 += " 		 tsd.isgraduated, decode(tsd.isgraduated,'Y','����','�̼���') isgraduated_value,  \n ";
             sql1 += "		 Case when substr(tss.eduend,0,8) < to_char(sysdate, 'yyyymmdd') then 'Y' else 'N' end endyn, \n";
             sql1 += " 		 to_char(to_date(substr(tsd.eduend,0,8),'yyyymmdd')+1,'yyyymmdd') studystart, to_char(to_date(substr(tsd.eduend,0,8),'yyyymmdd')+365,'yyyymmdd') studyend, tst.branch,  \n "; 
             sql1 += " 		 decode(sign(to_date(substr(tsd.eduend,0,8),'yyyymmdd')-sysdate+366),1,'Y','N') isreview,  \n "; 
             sql1 += " 		 tsj.subjnm, tsj.subjclass, tsj.upperclass, tsj.middleclass, tsj.lowerclass,  \n "; 
             sql1 += " 		 get_codenm('0004', tsj.isonoff) cname, tsj.isonoff, tss.isgoyong, tsj.biyong, tss.course , tss.cyear , tss.courseseq , tss.coursenm, tsd.score  \n ";
             sql1 += "       ,(select lec_sel_no from  tz_propose p where tst.subj = p.subj and tst.year = p.year and tst.subjseq = p.subjseq and tst.userid = p.userid) as lecselno  \n";
             sql1 += "       ,(select is_attend from  tz_propose p where tst.subj = p.subj and tst.year = p.year and tst.subjseq = p.subjseq and tst.userid = p.userid) as isattend  \n";
             sql1 += " 	 from tz_stold tsd, tz_student tst, tz_subj tsj, vz_scsubjseq tss  \n "; 
             sql1 += " 	where tst.subj 	= " + StringManager.makeSQL(v_subj) + " \n ";
             sql1 += "	  and tst.subjseq 	= " + StringManager.makeSQL(v_subjseq) + " \n ";
             sql1 += "	  and tst.year 	= " + StringManager.makeSQL(v_year) + " \n ";
             sql1 += "	  and tst.userid 	= " + StringManager.makeSQL(v_user_id) + " \n ";             
             sql1 += "	  and tsd.subj(+)		= tst.subj  \n "; 
             sql1 += "	  and tsd.year(+)		= tst.year  \n "; 
             sql1 += " 	  and tsd.subjseq(+)	= tst.subjseq  \n "; 
             sql1 += " 	  and tsd.userid(+) 	= tst.userid  \n "; 
             sql1 += " 	  and tst.subj		    = tsj.subj  \n "; 
             sql1 += "     and tst.subj          = tss.subj  \n "; 
             sql1 += "     and tst.year          = tss.year  \n "; 
             sql1 += "     and tst.subjseq       = tss.subjseq  \n "; 
             sql1 += "	order by tss.course, tss.edustart desc , tss.subjnm  \n ";

 //System.out.println("���������� ����Ʈ=======>"+sql1);
             
             logger.info("���� ������ : " + sql1);
             
             ls1 = connMgr.executeQuery(sql1);

                 while ( ls1.next() ) { 
                     dbox = ls1.getDataBox();

                     list1.add(dbox);
                 }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list1;
     }
      
      
      /**
      Ȯ���� ��� �˾�
      @param box      receive from the form object and session
      @return ArrayList
      */
       public ArrayList selectReqPrint(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          ListSet ls1         = null;
          ListSet ls2         = null;
          ArrayList list1     = null;
          String sql1         = "";
          String sql2         = "";
          MyClassData data1= null;
          MyClassData data2= null;
        
          String  v_birth_date     = ""; 
          String  v_user_id   = box.getSession("userid");
          String v_subj = box.getString("p_subj");
          String v_year = box.getString("p_year");
          String v_subjseq = box.getString("p_subjseq");
          String v_onoff = box.getString("p_onoff");
          
          ProposeBean probean = new ProposeBean();

          Hashtable outdata = new Hashtable();
          String v_appstatus = "";

          int     l           = 0;
          DataBox             dbox    = null;
          try { 
              connMgr = new DBConnectionManager();
              list1 = new ArrayList();

              
                     
              sql1 += "	select "; 
              sql1 += " 		 tss.edustart, tss.eduend, tsj.subjnm, tst.userid, tmm.name, tmm.position_nm, tmm.user_path  \n "; 
              sql1 += "       ,(select appdate from tz_propose p where tst.subj = p.subj and tst.year = p.year and tst.subjseq = p.subjseq and tst.userid = p.userid) appdate  \n";
              sql1 += " 	 from tz_stold tsd, tz_student tst, tz_subj tsj, vz_scsubjseq tss, tz_member tmm   \n "; 
              sql1 += " 	where tst.subj 	= " + StringManager.makeSQL(v_subj) + " \n ";
              sql1 += "	  and tst.subjseq 	= " + StringManager.makeSQL(v_subjseq) + " \n ";
              sql1 += "	  and tst.year 	= " + StringManager.makeSQL(v_year) + " \n ";
              sql1 += "	  and tst.userid 	= " + StringManager.makeSQL(v_user_id) + " \n ";             
              sql1 += "	  and tsd.subj(+)		= tst.subj  \n "; 
              sql1 += "	  and tsd.year(+)		= tst.year  \n "; 
              sql1 += " 	  and tsd.subjseq(+)	= tst.subjseq  \n "; 
              sql1 += " 	  and tsd.userid(+) 	= tst.userid  \n "; 
              sql1 += " 	  and tst.subj		    = tsj.subj  \n "; 
              sql1 += "     and tst.subj          = tss.subj  \n "; 
              sql1 += "     and tst.year          = tss.year  \n "; 
              sql1 += "     and tst.subjseq       = tss.subjseq  \n "; 
              
              sql1 += "    and tst.userid        = tmm.userid   \n "; 
              
              
              sql1 += "	order by tss.course, tss.edustart desc , tss.subjnm  \n ";

              
              logger.info("���� ������ Ȯ���� : " + sql1);
              
              ls1 = connMgr.executeQuery(sql1);

                  while ( ls1.next() ) { 
                      dbox = ls1.getDataBox();

                      list1.add(dbox);
                  }

          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex, box, sql1);
              throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
          } finally { 
              if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
              if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return list1;
      }
     
     
     /**
     �������α����̷�(���� ���� �̷�)
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList selectStudyHistoryBLTotList(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr = null;
         ListSet ls1         = null;
         ListSet ls2         = null;
         ArrayList list1     = null;
         ArrayList list2     = null;
         String sql1         = "";
         String sql2         = "";
         MyClassData data1= null;
         MyClassData data2= null;
         String  v_Bcourse   = ""; // �����ڽ�
         String  v_course    = ""; // �����ڽ�
         String  v_Bcourseseq= ""; // �����ڽ����
         String  v_courseseq = ""; // �����ڽ����
         String  v_subj      = ""; // ����
         String  v_year      = ""; // �⵵
         String  v_subjseq   = ""; // ������
         String  v_birth_date     = ""; // ������
         String  v_user_id   = box.getSession("userid");
         
         String v_stext = box.getString("p_stext").equals("")?"":box.getString("p_stext");        
         String v_month = box.getString("p_month").equals("")?"":box.getString("p_month");
         String v_stype = box.getString("p_stype").equals("")?"":box.getString("p_stype");
         
         v_year = box.getString("p_year").equals("")?"":box.getString("p_year");
         
         String  v_isblended = box.getString("p_isblended");
         String  v_isexpert = box.getString("p_isexpert");
         
         if(v_isblended.equals("")) v_isblended = "N";
         if(v_isexpert.equals("")) v_isexpert = "N";
         
         ProposeBean probean = new ProposeBean();

         Hashtable outdata = new Hashtable();
         String v_appstatus = "";

         int     l           = 0;
         DataBox             dbox    = null;
         try { 
             connMgr = new DBConnectionManager();
             list1 = new ArrayList();
             list2 = new ArrayList();

             outdata = probean.getMeberInfo(v_user_id);
             v_birth_date = (String)outdata.get("birth_date");
             
             sql1 += " select  tsj.contenttype                                                                                                                                \n";
             sql1 += "     ,   tss.subj                                                                                                                                       \n";
             sql1 += "     ,   tss.year                                                                                                                                       \n";
             sql1 += "     ,   tss.subjseq                                                                                                                                    \n";
             sql1 += "     ,   tst.isgraduated                                                                                                                                \n";
             sql1 += "     ,  decode(tst.isgraduated,'Y','����','�̼���') isgraduated_value                                                                                    \n";     
             sql1 += "     ,   tss.edustart    studystart                                                                                                                     \n";
             sql1 += "     ,   tss.eduend      studyend                                                                                                                       \n";
             sql1 += "     ,   tsj.eduurl                                                                                                                                     \n";
             sql1 += "     ,   tsj.isonoff                                                                                                                                    \n";
             sql1 += "     ,   tsj.subjnm                                                                                                                                     \n";
             sql1 += "     ,   tsj.subjclass                                                                                                                                  \n";
             sql1 += "     ,   CASE WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24') BETWEEN tss.EduStart  AND tss.EduEnd  THEN 'E'                                                      \n";
             sql1 += "             WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24')  BETWEEN tss.PropStart AND tss.PropEnd THEN 'R'                                                      \n";
             sql1 += "             ELSE ''                                                                                                                                    \n";
             sql1 += "         END    ProcEduFlag,                                                                                                                            \n";
             sql1 += "        'N' review                                                                                                                                      \n";
             sql1 += "     ,   a.courseyear                                                                                                                                   \n";
             sql1 += "     ,   a.courseseq                                                                                                                                    \n";
             sql1 += "     ,   a.coursecode                                                                                                                                   \n";
             sql1 += "     ,   a.isrequired                                                                                                                                   \n";
             sql1 += "     ,  a.userid                                                                                                                                        \n";
             sql1 += "     ,  a.reedu                                                                                                                                         \n";
             sql1 += "    ,   COUNT(*) OVER(PARTITION BY a.Courseyear, a.coursecode, a.courseseq, a.isonoff) oncnt                                                            \n";
             sql1 += "    ,   COUNT(*) OVER(PARTITION BY a.Courseyear, a.coursecode, a.courseseq           ) coursecnt                                                        \n";
             sql1 += "    ,   tbc.coursenm                                                                                                                                    \n";
             sql1 += "    ,   tbs.isonlinecomplete                                                                                                                            \n";
             sql1 += "    ,   tbst.status                                                                                                                                     \n";
             sql1 += " FROM    (                                                                                                                                              \n";
             sql1 += "             SELECT  a.courseyear                                                                                                                       \n";
             sql1 += "                 ,   a.courseseq                                                                                                                        \n";
             sql1 += "                 ,   a.coursecode                                                                                                                       \n";
             sql1 += "                 ,   a.subj                                                                                                                             \n";
             sql1 += "                 ,   a.year                                                                                                                             \n";
             sql1 += "                 ,  a.subjseq                                                                                                                           \n";
             sql1 += "                 ,   a.isrequired                                                                                                                       \n";
             sql1 += "                 ,  a.isgraduated                                                                                                                       \n";
             sql1 += "                 ,  a.userid                                                                                                                            \n";
             sql1 += "                 ,  a.isonoff                                                                                                                           \n";
             sql1 += "                ,   a.reedu                                                                                                                             \n";
             sql1 += "             FROM    (                                                                                                                                  \n";
             sql1 += "                         select  a.courseyear, a.courseseq, a.coursecode                                                                                \n";
             sql1 += "                             ,   c.subj                                                                                                                 \n";
             sql1 += "                            ,   c.year                                                                                                                  \n";
             sql1 += "                            ,   c.subjseq                                                                                                               \n";
             sql1 += "                            ,   c.isrequired                                                                                                            \n";
             sql1 += "                            ,   d.isgraduated                                                                                                           \n";
             sql1 += "                            ,   a.userid                                                                                                                \n";
             sql1 += "                            ,   'ON'    isonoff                                                                                                         \n";
             sql1 += "                            ,   'N'                 reedu                                                                                               \n";
             sql1 += "                         from    tz_blstold        a                                                                                                  \n";
             sql1 += "                             ,   tz_blcourseseq      b                                                                                                  \n";
             sql1 += "                             ,   tz_blproposesubj    c                                                                                                  \n";
             sql1 += "                             ,   tz_student         d                                                                                                   \n";
             sql1 += "                         where   a.coursecode    = b.coursecode                                                                                         \n";
             sql1 += "                         and     a.courseyear    = b.courseyear                                                                                         \n";
             sql1 += "                         and     a.courseseq     = b.courseseq                                                                                          \n";
             sql1 += "                         and     a.coursecode    = c.coursecode                                                                                         \n";
             sql1 += "                         and     a.courseyear    = c.courseyear                                                                                         \n";
             sql1 += "                         and     a.courseseq     = c.courseseq                                                                                          \n";
             sql1 += "                         and     a.userid        = c.userid                                                                                             \n";
             sql1 += "                         and     c.subj          = d.subj   (+)                                                                                         \n";
             sql1 += "                         and     c.userid        = d.userid (+)                                                                                         \n";
             sql1 += "                         and     c.year          = d.year   (+)                                                                                         \n";
             sql1 += "                         and     c.subjseq       = d.subjseq(+)                                                                                         \n";
             sql1 += "                         and     (c.status = 'RE' or c.status = 'FE')                                                                                   \n";
             sql1 += "                         UNION ALL                                                                                                                      \n";
             sql1 += "                         select  a.courseyear, a.courseseq, a.coursecode                                                                                \n";
             sql1 += "                             ,   a.subj                                                                                                                 \n";
             sql1 += "                            ,   a.year                                                                                                                  \n";
             sql1 += "                            ,   a.subjseq                                                                                                               \n";
             sql1 += "                            ,   a.isgraduated                                                                                                           \n";
             sql1 += "                            ,   a.isrequired                                                                                                            \n";
             sql1 += "                            ,   a.userid                                                                                                                \n";
             sql1 += "                            ,   a.isonoff                                                                                                               \n";
             sql1 += "                            ,   'Y'                 reedu                                                                                               \n";
             sql1 += "                        FROM   (                                                                                                                        \n";
             sql1 += "                         select  a.courseyear, a.courseseq, a.coursecode                                                                                \n";
             sql1 += "                             ,   d.subj                                                                                                                 \n";
             sql1 += "                            ,   d.year                                                                                                                  \n";
             sql1 += "                            ,   d.subjseq                                                                                                               \n";
             sql1 += "                            ,   d.isgraduated                                                                                                           \n";
             sql1 += "                            ,   c.isrequired                                                                                                            \n";
             sql1 += "                            ,   a.userid                                                                                                                \n";
             sql1 += "                            ,   'ON'    isonoff                                                                                                         \n";
             sql1 += "                            ,   RANK() OVER(PARTITION BY d.UserId, d.Subj ORDER BY d.score, d.subj, d.subjseq DESC) rank                                \n";
             sql1 += "                         from    tz_blstold        a                                                                                                  \n";
             sql1 += "                             ,   tz_blcourseseq      b                                                                                                  \n";
             sql1 += "                             ,   tz_blproposesubj    c                                                                                                  \n";
             sql1 += "                             ,   tz_stold           d                                                                                                   \n";
             sql1 += "                         where   a.coursecode    = b.coursecode                                                                                         \n";
             sql1 += "                         and     a.courseyear    = b.courseyear                                                                                         \n";
             sql1 += "                         and     a.courseseq     = b.courseseq                                                                                          \n";
             sql1 += "                         and     a.coursecode    = c.coursecode                                                                                         \n";
             sql1 += "                         and     a.courseyear    = c.courseyear                                                                                         \n";
             sql1 += "                         and     a.courseseq     = c.courseseq                                                                                          \n";
             sql1 += "                         and     a.userid        = c.userid                                                                                             \n";
             sql1 += "                         and     c.subj          = d.subj                                                                                               \n";
             sql1 += "                         and     c.userid       = d.userid                                                                                              \n";
             sql1 += "                         and     c.status        = 'RV'                                                                                                 \n";
             sql1 += "                         and     add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart, 'YYYYMMDD')            \n";
             sql1 += "                        )       a                                                                                                                       \n";
             sql1 += "                        WHERE   rank = 1                                                                                                                \n";
             sql1 += "                         UNION ALL                                                                                                                      \n";
             sql1 += "                         select  a.courseyear, a.courseseq, a.coursecode                                                                                \n";
             sql1 += "                             ,   a.subj, a.year, a.subjseq, 'Y' isrequired                                                                              \n";
             sql1 += "                            ,   d.isgraduated                                                                                                           \n";
             sql1 += "                             , a.userid, 'OFF' isonoff                                                                                                  \n";
             sql1 += "                            ,   'N'                 reedu                                                                                               \n";
             sql1 += "                         from    (                                                                                                                      \n";
             sql1 += "                                     SELECT  a.courseyear                                                                                               \n";
             sql1 += "                                         ,   a.coursecode                                                                                               \n";
             sql1 += "                                         ,   a.courseseq                                                                                                \n";
             sql1 += "                                         ,   c.subj                                                                                                     \n";
             sql1 += "                                         ,   c.year                                                                                                     \n";
             sql1 += "                                         ,   c.subjseq                                                                                                  \n";
             sql1 += "                                         ,   a.userid                                                                                                   \n";
             sql1 += "                                         ,   c.isonoff                                                                                                  \n";
             sql1 += "                                     FROM    Tz_blstudent       a                                                                                       \n";
             sql1 += "                                         ,   tz_blcourseseqsubj  c                                                                                      \n";
             sql1 += "                                     WHERE   a.courseseq            = c.courseseq                                                                       \n";
             sql1 += "                                     AND        a.courseyear        = c.courseyear                                                                      \n";
             sql1 += "                                     AND        a.coursecode        = c.coursecode                                                                      \n";
             sql1 += "                                 )                   a                                                                                                  \n";
             sql1 += "                             ,   tz_blcourseseq      b                                                                                                  \n";
             sql1 += "                             ,   tz_student          d                                                                                                  \n";
             sql1 += "                          where  a.coursecode    = b.coursecode                                                                                         \n";
             sql1 += "                          and    a.courseyear    = b.courseyear                                                                                         \n";
             sql1 += "                          and    a.courseseq     = b.courseseq                                                                                          \n";
             sql1 += "                          and    a.subj          = d.subj(+)                                                                                            \n";
             sql1 += "                          and    a.userid        = d.userid(+)                                                                                          \n";
             sql1 += "                          and    a.year          = d.year(+)                                                                                            \n";
             sql1 += "                          and    a.subjseq       = d.subjseq(+)                                                                                         \n";
             sql1 += "                          and    a.isonoff       = 'OFF'                                                                                                \n";
             sql1 += "                     )       a                                                                                                                          \n";
             sql1 += "         )       a                                                                                                                                      \n";
             sql1 += "     ,   tz_stold       tst                                                                                                                             \n";
             sql1 += "     ,   tz_subjseq     tss                                                                                                                             \n";
             sql1 += "     ,   tz_subj        tsj                                                                                                                             \n";
             sql1 += "    ,   tz_bl_course   tbc                                                                                                                              \n";
             sql1 += "    ,   tz_blcourseseq tbs                                                                                                                              \n";
             sql1 += "    ,   tz_blstold   tbst                                                                                                                             \n";
             sql1 += " WHERE   a.userid(+)     = " + SQLString.Format(v_user_id) + "                                                                                                                    \n";
             sql1 += " and     a.subj          = tst.subj(+)                                                                                                                  \n";
             sql1 += " and     a.subjseq       = tst.subjseq(+)                                                                                                               \n";
             sql1 += " and     a.year          = tst.year(+)                                                                                                                  \n";
             sql1 += " and     a.userid       = tst.userid(+)                                                                                                                 \n";
             sql1 += " and     a.subj             = tss.subj                                                                                                                  \n";
             sql1 += " and     a.year             = tss.year                                                                                                                  \n";
             sql1 += " and     a.subjseq      = tss.subjseq                                                                                                                   \n";
             sql1 += " and     a.subj          = tsj.subj                                                                                                                     \n";
             sql1 += " and        a.coursecode    = tbc.coursecode                                                                                                            \n";
             sql1 += " and        a.courseyear    = tbs.courseyear                                                                                                            \n";
             sql1 += " and        a.coursecode    = tbs.coursecode                                                                                                            \n";
             sql1 += " and        a.courseseq     = tbs.courseseq                                                                                                             \n";
             sql1 += " and     tbst.coursecode  = a.coursecode                                                                                                                \n";
             sql1 += " and     tbst.courseyear  = a.courseyear                                                                                                                \n";
             sql1 += " and     tbst.courseseq   = a.courseseq                                                                                                                 \n";
             sql1 += " and     tbst.userid      = a.userid                                                                                                                    \n";
             sql1 += " ORDER BY a.CourseYear, a.CourseCode, a.CourseSeq, a.isOnOff DESC, a.Subj                                                                               \n";
             
             ls1 = connMgr.executeQuery(sql1);

                 while ( ls1.next() ) { 
                     dbox = ls1.getDataBox();

                     list1.add(dbox);
                 }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list1;
     }
     
 

    /**
    ������ ���� ����
    @param box      receive from the form object and session
    @return TutorData
    */
     public DataBox getSuryoInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	  = null;
        ListSet             ls        = null;
        DataBox             dbox      = null;
        StringBuffer        sbSQL     = new StringBuffer("");
        
        int                 iSysAdd   = 0; // System.out.println�� Mehtod���� ����ϴ� ������ ǥ���ϴ� ����         
                                                
        String              v_subj    = box.getString("p_subj");
        String              v_subjseq = box.getString("p_subjseq");
        String              v_year    = box.getString("p_year");
        String              v_userid  = box.getString("p_userid");

        try { 
            connMgr = new DBConnectionManager();



            sbSQL.append(" select  b.serno                                                                                      \n")
                 .append("     ,   a.subj                                                                                       \n")
                 .append("     ,   a.year                                                                                       \n")
                 .append("     ,   a.subjseq                                                                                    \n")
                 .append("     ,   a.subjnm                                                                                     \n")
                 .append("     ,   get_codenm('0004', a.isonoff) onoff_name               										\n")
                 .append("     ,   a.edustart          																			\n")
                 .append("     ,   a.eduend           							 												\n")
                 .append("     ,   c.name                                                                                       \n")
                 .append("     ,   fn_crypt('2', c.birth_date, 'knise') birth_date                                                                                     \n")
                 .append("     ,   get_compnm(c.comp) compnm				               										\n")                 
                 .append("     ,   to_char(to_date(a.eduend     , 'yyyymmddhh24miss'), 'yyyy')       grad_year                   \n")
                 .append("     ,   to_char(to_date(a.eduend     , 'yyyymmddhh24miss'), 'mm'  )       grad_month                  \n")
                 .append("     ,   to_char(to_date(a.eduend     , 'yyyymmddhh24miss'), 'dd'  )       grad_day                    \n")
                 .append("     , (select lec_sel_no from  tz_propose p where a.subj = p.subj and a.year = p.year and a.subjseq = p.subjseq and b.userid = p.userid) as lecselno \n")
                 .append("     , (select EDUTIMES from tz_subj ts where a.subj = ts.subj) edutime \n") 
                 .append("     , (select get_subjclass_fullnm (subjclass) from tz_subj ts where  a.subj = ts.subj) upperclassnm \n")//����, ������, �кθ�  ����
                 .append("     , (select point from tz_subj ts where  a.subj = ts.subj) point \n")//����
                 .append("     , to_char(b.editscore) score, c.cert \n")//����
                 .append(" from    vz_scsubjseq    a                                                            \n")
                 .append("     ,   tz_stold        b                                                            \n")
                 .append("     ,   tz_member       c                                                            \n")
                 .append(" where   a.subj      = " + SQLString.Format(v_subj    ) + "                           \n")
                 .append(" and     a.year      = " + SQLString.Format(v_year    ) + "                           \n")
                 .append(" and     a.subjseq   = " + SQLString.Format(v_subjseq ) + "                           \n")
                 .append(" and     a.subj      = b.subj                                                         \n")
                 .append(" and     a.year      = b.year                                                         \n")
                 .append(" and     a.subjseq   = b.subjseq                                                      \n")
                 .append(" and     b.userid    = " +SQLString.Format(v_userid   ) + "                           \n")
                 .append(" and     b.userid    = c.userid                                                       \n");
            
//            System.out.println(this.getClass().getName() + "." + "ApprovalProcess() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            logger.info("���������ȭ��=====>"+sbSQL.toString()); 
            
            ls  = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) {
                dbox = ls.getDataBox();
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
        }
        
        return dbox;
    }
     
     /**
     ������ �ϰ����� ����
     @param box      receive from the form object and session
     @return TutorData
     */
      public ArrayList getSuryoInfoAll(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	  = null;
         ListSet             ls        = null;
         ArrayList             list      = null;
         DataBox             dbox      = null;
         StringBuffer        sbSQL = null;
         
         int                 iSysAdd   = 0; // System.out.println�� Mehtod���� ����ϴ� ������ ǥ���ϴ� ����         
                                                 
         String              v_subj    = box.getString("p_subj");
         int              v_printcnt    = Integer.parseInt(box.getString("p_printcnt"));
         String              v_subjseq = box.getString("p_subjseq");
         String              v_year    = box.getString("p_year");
       //  String              v_userid  = box.getString("p_userid");
         
 //        Vector v_vchecks = box.getVector("p_checks");
//System.out.println("v_vchecks::::::::::::::::::::::::::::::::::"+v_vchecks);
         try { 
             connMgr = new DBConnectionManager();
             list = new ArrayList();
             
         	int minSize = v_printcnt - 40;
         	
         	int maxSize = v_printcnt;
         	
         	
         	
  //       for(int i=0;i<v_vchecks.size();i++){
        	         sbSQL     = new StringBuffer("");
             sbSQL.append(" select * from ( select   rownum as rn, xx.* from ( select b.serno \n")
                  .append("     ,   a.subj                                                                                       \n")
                  .append("     ,   a.year                                                                                       \n")
                  .append("     ,   a.subjseq                                                                                    \n")
                  .append("     ,   a.subjnm                                                                                     \n")
                  .append("     ,   get_codenm('0004', a.isonoff) onoff_name               										\n")
                  .append("     ,   a.edustart          																			\n")
                  .append("     ,   a.eduend           							 												\n")
                  .append("     ,   c.name                                                                                       \n")
                  .append("     ,   fn_crypt('2', c.birth_date, 'knise') birth_date                                                                                     \n")
                  .append("     ,   get_compnm(c.comp) compnm				               										\n")                 
                  .append("     ,   to_char(to_date(a.eduend     , 'yyyymmddhh24miss'), 'yyyy')       grad_year                   \n")
                  .append("     ,   to_char(to_date(a.eduend     , 'yyyymmddhh24miss'), 'mm'  )       grad_month                  \n")
                  .append("     ,   to_char(to_date(a.eduend     , 'yyyymmddhh24miss'), 'dd'  )       grad_day                    \n")
                  .append("     , (select lec_sel_no from  tz_propose p where a.subj = p.subj and a.year = p.year and a.subjseq = p.subjseq and b.userid = p.userid) as lecselno \n")
                  .append("     , (select EDUTIMES from tz_subj ts where a.subj = ts.subj) edutime \n") 
                  .append("     , (select get_subjclass_fullnm (subjclass) from tz_subj ts where  a.subj = ts.subj) upperclassnm \n")//����, ������, �кθ�  ����
                  .append("     , (select point from tz_subj ts where  a.subj = ts.subj) point \n")//����
                  .append("     , to_char(b.editscore) score, c.cert \n")//����
                  .append(" from    vz_scsubjseq    a                                                            \n")
                  .append("     ,   tz_stold        b                                                            \n")
                  .append("     ,   tz_member       c                                                            \n")
                  .append("     ,   tz_student      d                                                              \n")
                  .append(" where   a.subj      = " + SQLString.Format(v_subj    ) + "                           \n")
                  .append(" and     a.year      = " + SQLString.Format(v_year    ) + "                           \n")
                  .append(" and     a.subjseq   = " + SQLString.Format(v_subjseq ) + "                           \n")
                  .append(" and     a.subj      = b.subj                                                         \n")
                  .append(" and     a.year      = b.year                                                         \n")
                  .append(" and     a.subjseq   = b.subjseq                                                      \n")
                  .append(" and    a.subj      = d.subj 							\n")
                  .append(" and    a.year      = d.year							\n")
                  .append("  and    a.subjseq   = d.subjseq							\n")
                  .append(" and    b.userid    = d.userid  							\n")
                 // .append(" and     b.userid    = " +SQLString.Format(v_vchecks.elementAt(i).toString()) + "                           \n")
                 // .append(" and     b.serno    = " +SQLString.Format(v_vchecks.elementAt(i).toString()) + "                           \n")
                  .append(" and     b.userid    = c.userid                                                       \n")
             	  .append("  and b.serno is not null                                                      \n")
             	  .append("  order by c.name, d.examnum  \n							")
             	  .append(" ) xx ) where rn between " + (minSize+1) + "and " + maxSize + " ")
             	  ;
             	
             System.out.println(this.getClass().getName() + "." + "ApprovalProcess() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
             
             System.out.println("�������ϰ� ���ȭ��=====>"+sbSQL.toString()); 
             
             ls  = connMgr.executeQuery(sbSQL.toString());
        
             while ( ls.next() ) {
            	 dbox = ls.getDataBox();

                 list.add(dbox);
             }
    //     }
         } catch ( SQLException e ) {
             ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
             throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } catch ( Exception e ) {
             ErrorManager.getErrorStackTrace(e);
             throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } finally {
             if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
         }
         
         return list;
     }


    /**
    HOMEPAGE ���� ����ǥ�ð� ����
    @param edustart      ����������
    @param eduend        ����������
    @return Vector
    */
     public Vector getDateInfo(String edustart, String eduend) throws Exception{ 
        Vector  result      = new Vector();
        int     v_datediff  = 0;        // �����Ⱓ
        int     v_period    = 0;        // �����Ⱓ / 4 
        String  v_predate   = "";       // ���� ��������
        String  v_printDate = "";       // ȭ�� ǥ�� ����

        v_datediff = FormatDate.datediff("date", edustart, eduend);  // �����ϼ�
        v_period = v_datediff / 4;                                   // �����ϼ� / 4
        
        v_predate = edustart;
        v_printDate = FormatDate.getFormatDate(v_predate, "MM/dd");
        result.addElement(v_printDate);

        for ( int i = 1; i < 4; i++ ) { 
            v_printDate = FormatDate.getRelativeDate(v_predate, v_period);
            v_predate =v_printDate;

            v_printDate = FormatDate.getFormatDate(v_printDate, "MM/dd");
            result.addElement(v_printDate);
        }

        return result;
     }


    //// //// //// //// //// //// //// //// //// //// //// ///������ �Ϸù�ȣ ����//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    public String getIsunum(String p_subj, String p_subjseq, String p_year,String p_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String v_isunum = "";
        try { 
            connMgr = new DBConnectionManager();
            v_isunum = getIsunum(connMgr,p_subj, p_subjseq, p_year, p_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_isunum;
    }

    public String getIsunum(DBConnectionManager connMgr, String p_subj, String p_subjseq, String p_year,String p_userid) throws Exception { 
        ListSet             ls      = null;
        String v_isunum = "";

        String v_startdate  = "";
        String v_educscd    = "";
        String v_empno      = "";
        String v_degree = "";
        String v_empnm  = "";
        int    i = 1;

        String sql  = "";

        try { 
            sql  = "select substr(startdate,0,6) startdate, ";
            sql += "       educscd, ";
            sql += "       to_char(degree,'00') degree, ";
            sql += "       empno, ";
            sql += "       empnm ";
            sql += "  from tacareer ";
            sql += " where educscd=" +StringManager.makeSQL(p_subj);
            sql += "   and eduyear=" +StringManager.makeSQL(p_year);
            sql += "   and degree=" +StringManager.makeSQL(p_subjseq);
            sql += " order by empnm asc ";

//            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            i++;

                v_startdate = ls.getString("startdate");
                v_educscd   = ls.getString("educscd");
                v_degree    = ls.getString("degree");
                v_empno     = ls.getString("empno");
                v_empnm     = ls.getString("empnm");

                if ( p_userid.equals(v_empno)) { 
                    v_isunum = v_startdate + "-" +v_educscd + "-" +v_degree + "-" +i;
                }
             }
             ls.close();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_isunum;
    }


/**
    ��������û ����
    @param box      receive from the form object and session
    @return int
    */
     public int SuRoyAppInsert(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        ListSet ls1         = null;
        
        String sql1         = "";
        int cancel_cnt      = 0;
        int isOk            = 0;
        String  v_subj      = box.getString("p_subj");
        String  v_year      = box.getString("p_year");
        String  v_subjseq   = box.getString("p_subjseq");
        String  v_userid    = box.getString("p_userid");
        
        int 	v_seq	    = 1;
        
        String 	v_username  = box.getString("p_username");
        String 	v_useradd   = box.getString("p_useradd");
        String 	v_useremail = box.getString("p_useremail");
        String 	v_usertel   = box.getString("p_usertel");
        
        int 	v_ea	    = box.getInt("p_ea");

        Hashtable insertData = new Hashtable();
        ProposeBean probean = new ProposeBean();

        SubjComBean scbean = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();

            sql1  = "	select nvl(max(seq),0)+1 maxseq ";
            sql1 += "	from tz_certapp ";
            sql1 += "	where subj = " +SQLString.Format(v_subj);
            sql1 += "   and year=" +SQLString.Format(v_year);            
            sql1 += "   and subjseq=" +SQLString.Format(v_subjseq);            
            sql1 += "   and userid=" +SQLString.Format(v_userid);
            
            ls1 = connMgr.executeQuery(sql1);
            ls1.next();

            v_seq = ls1.getInt("maxseq");
                            
            connMgr.setAutoCommit(false);

                      sql1 =	"	insert into TZ_CERTAPP (subj, year, subjseq, userid, seq, ";
                      sql1 +=	"		username, useradd, useremail, usertel, ea, ";
                      sql1 +=	"		appdate, issend, senddate )";
                      sql1 +=	"	values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ";
                      sql1 +=	"		'N', '')";
                      pstmt = connMgr.prepareStatement(sql1);
                      pstmt.setString(1, v_subj);
                      pstmt.setString(2, v_year);
                      pstmt.setString(3, v_subjseq);
                      pstmt.setString(4, v_userid);
                      pstmt.setInt(5, v_seq);
                      pstmt.setString(6, v_username);
                      pstmt.setString(7, v_useradd);
                      pstmt.setString(8, v_useremail);
                      pstmt.setString(9, v_usertel);
                      pstmt.setInt(10, v_ea);
                      isOk = pstmt.executeUpdate();

                if ( isOk > 0 ) connMgr.commit();
                else connMgr.rollback();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
    

    /**
    ������ ��û �˾����� �ʿ��� ���� ��������...
    @param box      receive from the form object and session
    @return ArrayList
    */
     public DataBox selectUserSuRoyInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        DataBox             dbox    = null;

        String  v_user_id   = box.getSession("userid");
        String  v_tem_grcode= box.getSession("tem_grcode");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1  = "";	            	  	
            	sql1 += "	select tm.name, 					";
            	sql1 += "		tm.zip_cd as post, 				";
            	sql1 += "		tm.address as addr, 			";
            	sql1 += "		nvl(tm.handphone,tm.hometel) phone, ";
            	sql1 += "		tm.email,				 		";
            	sql1 += "		tm.indate,				 		";
            	sql1 += "		tm.lgcnt,				 		";
				sql1 += "		tm.lglast,				 		";
				sql1 += "		tmf.savefile,				 	";
				sql1 += "		ts.isstrout						";
            	sql1 += "	from tz_member tm, tz_memberfile tmf, tz_strout ts 		";
            	sql1 += "	where tm.userid = " + SQLString.Format(v_user_id)		;               	
				sql1 += "		and tm.userid	= tmf.userid(+)				";            	
				sql1 += "		and 'Y'		    = tmf.ismain(+)				";            	
            	sql1 += "		and tm.userid	= ts.userid(+) 				";
            	
            	

            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }

    public String isSubTutor(RequestBox box) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String sql = "";
        String isSubTutor = "N";

        try 
        {
            connMgr = new DBConnectionManager();
            
            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            String v_userid = box.getSession("userid");

            sql =
                "\n  SELECT issubtutor      " +
                "\n  FROM tz_student        " +
                "\n  WHERE 1=1              " +
                "\n      AND subj =         " + SQLString.Format( v_subj ) + 
                "\n      AND YEAR =         " + SQLString.Format( v_year ) +
                "\n      AND subjseq =      " + SQLString.Format( v_subjseq ) +
                "\n      AND userid =       " + SQLString.Format( v_userid );
                                
            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() ) 
            {
                isSubTutor = ls.getString( "issubtutor" );
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
        
        return isSubTutor;
    }
    
    
    
    
    
    /**
     * �⼮üũ��, ���� ������ �Ǵ��Ͽ� DB�� Insert �Ѵ�.
     * 
     * @param box
     * @return isOk 1=�⼮üũ(O), 2=����(L), 3=����(M), 66,77=�⼮üũ�Ұ�, 88=�⼮üũ�Ϸ�, 99=���IP�ƴ�
     * @throws Exception
     */
    public int insertAttendance( RequestBox box ) throws Exception {
         
         final String TIME_CHECK_CODE = "0060";
         
         DBConnectionManager connMgr = null;
         ListSet ls = null;
         
         StringBuffer sql = new StringBuffer();
         int isOk = 0;
         
         try {
             connMgr = new DBConnectionManager();
             
             String v_subj = box.getString("p_subj");
             String v_year = box.getString("p_year");
             String v_subjseq = box.getString("p_subjseq");
             String v_userid = box.getSession("userid");
             String v_ip = box.getSession("userip");
             
             isOk = isAttendenceAvailable( v_subj, v_year, v_subjseq, v_userid, v_ip );
             
             if ( isOk == 1 )
             {
                 sql.delete( 0, sql.length() );
                 sql.append( "INSERT INTO tz_attendance ( subj, YEAR, subjseq, userid, ldate, " );
                 sql.append( "isattend, attdate, atttime ) " );
                 sql.append( "VALUES ( " );
                 sql.append( SQLString.Format(v_subj) );
                 sql.append ( ", ");
                 sql.append( SQLString.Format(v_year) );
                 sql.append ( ", ");
                 sql.append( SQLString.Format(v_subjseq) );
                 sql.append ( ", ");
                 sql.append( SQLString.Format(v_userid) );
                 sql.append ( ", ");
                 sql.append( "TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'), " );
                 sql.append( SQLString.Format(attCode_) );
                 sql.append( ",TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MI') )" );
                 
                 connMgr.executeUpdate( sql.toString() );
                 
                 FinishBean fbean = new FinishBean();
                 fbean.calc_offscore( connMgr, v_subj, v_year, v_subjseq, v_userid, v_userid, new StoldData() );
                 
                 if ( attCode_.equals(ATTENDANCE_CODE) )
                     isOk = 1;
                 else if ( attCode_.equals(LATE_CODE) )
                     isOk = 2;
                 else if ( attCode_.equals(M_LATE_CODE) )
                     isOk = 3;
             }
         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql.toString());
             throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         
         return isOk;
     }
    
    
     /**
      * ȯ�漳��<�ڵ����<�⼮�ð��� TZ_ATTENDANCE ���̺��� �����Ͽ�, �⼮üũ ���� ���θ� �˷��ش�.
      * 
      * @param request
      * @param box
      * @return int 1=�⼮üũ����, 66,77=�⼮üũ�Ұ�, 88=�⼮üũ�Ϸ�, 99=���IP�ƴ�
      * @throws Exception
      */
     public int isAttendenceAvailable( String v_subj, String v_year, String v_subjseq, String v_userid, String v_ip ) throws Exception 
     {
         final String TIME_CHECK_CODE = "0060";
         
         DBConnectionManager connMgr = null;
         ListSet ls = null;
         
         StringBuffer sql = new StringBuffer();
         
         try 
         {
             connMgr = new DBConnectionManager();

             if ( checkAttendanceIP( connMgr, v_subj, v_year, v_subjseq, v_ip ) )
             {
                 if ( !checkAttendanceComplete( connMgr, v_subj, v_year, v_subjseq, v_userid ) )
                 {
                     if ( checkAttendanceDate( connMgr, v_subj, v_year, v_subjseq, v_userid ) )
                     {
                         if ( checkAttendanceTime( connMgr, v_subj, v_year, v_subjseq ) )
                             return 1;  // �⼮üũ ����
                         else
                             return 66; // �⼮üũ�Ұ�(�ð� �ƴ�)
                     }
                     else
                         return 77; // �⼮üũ�Ұ�(�н����� �ƴ�)
                 }
                 else
                     return 88; // �̹� �⼮üũ �Ϸ��
             }
             else
                 return 99;     // ���IP �ƴ�
         }
         catch (Exception ex) {
             throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
     }      
     
     private boolean checkAttendanceIP( DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_ip ) throws Exception
     {
         final String IP_CHECK_CODE = "0082";

         ListSet ls = null;
         StringBuffer sql = new StringBuffer();
         
         boolean result = false;
         
         try {
             connMgr = new DBConnectionManager();
             
             sql.append( "SELECT CODENM FROM tz_code " );
             sql.append( "WHERE gubun=" );
             sql.append( SQLString.Format(IP_CHECK_CODE) );
             
             ls = connMgr.executeQuery( sql.toString() );
             
             ArrayList ipList = new ArrayList();
             
             while ( ls.next() ) 
             {
                 ipList.add( ls.getString("codenm") );
             }
             
             if ( isPermissionIP( v_ip, (String[]) ipList.toArray( new String[ipList.size()] ) ) )
                 return true;
         }
         catch (Exception ex) 
         {
             throw new Exception("sql1 = " + sql.toString() + "\r\n" + ex.getMessage());
         }
         finally 
         {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
         }
         
         return false;
     }
     
    private boolean checkAttendanceComplete( DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_userid ) throws Exception
    {
        boolean result = false;
        
        ListSet ls = null;
        StringBuffer sql = new StringBuffer();
        
        try 
        {
            // �⼮�Ϸ� ���� üũ
            sql.append( "SELECT COUNT(*) cnt FROM tz_attendance " );
            sql.append( "WHERE 1=1 " );
            sql.append( " and subj=" );
            sql.append( SQLString.Format( v_subj ) );
            sql.append( " and year=" );
            sql.append( SQLString.Format( v_year ) );
            sql.append( " and subjseq=" );
            sql.append( SQLString.Format( v_subjseq ) );
            sql.append( " and userid=" );
            sql.append( SQLString.Format( v_userid ) );
            sql.append( " and attdate=TO_CHAR(sysdate,'YYYYMMDD') " );
            
            ls = connMgr.executeQuery( sql.toString() );
            
            if ( ls.next() )
            {
                if ( ls.getInt("cnt") > 0 )
                    result = true;
            }
        }
        catch (Exception ex) {
            throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        
        return result;
    }

    private boolean checkAttendanceDate( DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_userid ) throws Exception
    {
        boolean result = false;
        
        ListSet ls = null;
        StringBuffer sql = new StringBuffer();
        
        try 
        {
            // �⼮���� ���� üũ - ��¥
            sql.delete ( 0, sql.length() );
            sql.append( "select count(*) cnt from tz_offsubjlecture offsl ");
            sql.append( "WHERE 1=1 " );
            sql.append( " and subj=" );
            sql.append( SQLString.Format( v_subj ) );
            sql.append( " and year=" );
            sql.append( SQLString.Format( v_year ) );
            sql.append( " and subjseq=" );
            sql.append( SQLString.Format( v_subjseq ) );
            sql.append( " and lectdate=to_char(sysdate,'YYYYMMDD') " );
            sql.append( " AND CLASS=(SELECT CLASS FROM tz_student WHERE subj=offsl.subj AND YEAR=offsl.YEAR AND subjseq=offsl.subjseq AND userid=" );
            sql.append( SQLString.Format(v_userid) );
            sql.append( ") " );
            
//            System.out.println("�н���üũ==============="+sql.toString());
            ls = connMgr.executeQuery( sql.toString() );
            
            if ( ls.next() )
            {
                if ( ls.getInt("cnt") > 0 )
                    result = true;
            }
        }
        catch (Exception ex) {
            throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        
        return result;
    }
    
    private boolean checkAttendanceTime( DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq ) throws Exception
    {
        final String TIME_CHECK_CODE = "0083";
        
        boolean result = false;
        
        ListSet ls = null;
        StringBuffer sql = new StringBuffer();
        
        try 
        {
            // �⼮���� ���� üũ - �ð�
            sql.delete( 0, sql.length() );             
            
            //sql.append( "SELECT SUBSTR(CODENM,1,5) STARTTIME, SUBSTR(CODENM,7,10)  ENDTIME FROM tz_code " );
            //sql.append( "WHERE gubun=" );
            //sql.append( SQLString.Format(TIME_CHECK_CODE) );
            //sql.append( " AND code = '1'" );
            //sql.append( " AND code = (SELECT upperclass FROM tz_subj WHERE subj=" );
            //sql.append( SQLString.Format(v_subj) );
            //sql.append( ") " );            
            
            sql.append( "SELECT ATTSTIME||':00' STARTTIME, ATTETIME||':00' ENDTIME FROM tz_subjseq " );
            sql.append( "WHERE subj=" );
            sql.append( SQLString.Format(v_subj) );
            sql.append( " AND year =" );
            sql.append( SQLString.Format(v_year) );
            sql.append( " AND subjseq =" );
            sql.append( SQLString.Format(v_subjseq) );

            ls = connMgr.executeQuery( sql.toString() );
            
            String startTime = "";
            String endTime = "";
            
            if ( ls.next() ) 
            {
                startTime = ls.getString("starttime");
                endTime = ls.getString("endtime");
            }
                //System.out.println("startTime======================================================"+startTime); 
                //System.out.println("endTime======================================================"+endTime);
            
            
            if ( startTime.equals("") || endTime.equals("") )
                return false;
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            String currentTime = sf.format(cal.getTime());
            
            // �⼮, ����(L), ����(M) ���� ����.
            attCode_ = getAttendanceValue( currentTime, startTime, endTime );
            
            if ( attCode_.equals(ATTENDANCE_CODE) || attCode_.equals(LATE_CODE) || attCode_.equals(M_LATE_CODE) )
                result = true;
            else
                result = false;
        }
        catch (Exception ex) {
            throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        
        return result;
    }    
    //10:27, 09:00, 22:00
    private String getAttendanceValue(  String currentTime, String startTime, String endTime ) throws Exception 
     {

         int[] current = castingIntegerArray( currentTime.split(":") );
         int[] start = castingIntegerArray( startTime.split(":") );
         int[] end = castingIntegerArray( endTime.split(":") );
         
         if ( current[0]>end[0] || (current[0]==end[0] && current[1]>end[1]) )
         {
             return NOT_ATTENDANCE_CODE;
         }
         else
         {
             int plusTime = (current[0]-start[0]) * 60;
             
             if ( isValueL((current[1]+plusTime) - start[1]) )
                 return LATE_CODE;
             else if ( isValueM((current[1]+plusTime) - start[1]) )
                 return M_LATE_CODE;
             else
                 return ATTENDANCE_CODE;
         }
     }

     private boolean isValueM(int minute) 
     {
         if (  minute > LATE_END_MINUTE )
             return true;
         else
             return false;
     }

     private boolean isValueL( int minute )
     {
         if ( minute > LATE_START_MINUTE && minute <= LATE_END_MINUTE )
             return true;
         else
             return false;
     }
     
     private int[] castingIntegerArray(String[] str) 
     {
         int[] arr = new int[str.length];
         
         for ( int i=0; i<arr.length; i++ ) 
         {
             arr[i] = Integer.parseInt(str[i]);
         }
         
         return arr;
     }          
     
     /**
      * ȯ�漳��<�ڵ����<�⼮IP�� �����Ͽ�, ��� IP ���θ� �˷��ش�.
      * 
      * @param request
      * @param box
      * @return boolean ���� IP �̸� true, �ƴϸ� false
      * @throws Exception
      */
     public boolean isPermission( RequestBox box ) throws Exception {
         
         final String IP_CHECK_CODE = "0061";
         
         DBConnectionManager connMgr = null;
         PreparedStatement pstmt = null;
         ListSet ls = null;
         
         StringBuffer sql = new StringBuffer();
         
         try {
             connMgr = new DBConnectionManager();
             
             String v_subj = box.getString("p_subj");
             String v_year = box.getString("p_year");
             String v_subjseq = box.getString("p_subjseq");
             String v_userid = box.getSession("userid");
             String v_ip = box.getSession("userip");
             
             sql.append( "SELECT CODENM FROM tz_code " );
             sql.append( "WHERE gubun=" );
             sql.append( SQLString.Format(IP_CHECK_CODE) );
             
             ls = connMgr.executeQuery( sql.toString() );
             
             ArrayList ipList = new ArrayList();
             
             while ( ls.next() ) {
                     ipList.add( ls.getString("codenm") );
             }
             
             if ( isPermissionIP( v_ip, (String[]) ipList.toArray( new String[ipList.size()] ) ) )
                 return true;
         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql.toString());
             throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
             if(pstmt != null)  { try { pstmt.close(); } catch (Exception e1) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         
         return false;
     }     

     private boolean isPermissionIP( String myIP, String[] permissionIP )
     {
         for ( int i=0, n=permissionIP.length; i<n; i++ )
         {
             if ( permissionIP[i].indexOf("~") == -1 )
             {
                 if ( permissionIP[i].equals(myIP) )
                     return true;
             }
             else
             {
                 if ( isPermissionIPs( myIP, permissionIP[i] ) )
                     return true;
             }
         }
         
         return false;
     }

     private boolean isPermissionIPs( String myIP, String permissionIP )
     {
         
         int dot = permissionIP.lastIndexOf(".");
         int tild = permissionIP.indexOf("~");
         
         String ip = permissionIP.substring( 0, dot );

         if ( myIP.startsWith(ip) )
         {
             int startNo = Integer.parseInt( permissionIP.substring( dot+1, tild ) );
             int endNo = Integer.parseInt( permissionIP.substring(tild+1, permissionIP.length()) );
             
             int myNo = Integer.parseInt(myIP.substring( myIP.lastIndexOf(".")+1, myIP.length()));
             
             for ( int i=startNo; i<endNo+1; i++ )
             {
                 if ( i == myNo )
                     return true;
             }
         }
         
         return false;
     }

     /**
      * �������� ���� ����Ʈ �ڽ�
      * @param userid      �������̵�
      * @param name        ����Ʈ�ڽ���
      * @param selected    ���ð�
      * @param event       �̺�Ʈ��
      * @return
      * @throws Exception
      */
      public static String getSubjIngSelect(String userid, String name, String selected, String event) throws Exception {
          DBConnectionManager   connMgr = null;
          ListSet   ls      = null;
          String    result  = null;
          String    sql     = "";
          int       cnt     = 0;
          
          try {
              connMgr = new DBConnectionManager();
              
              /*
              sql = " select distinct a.subj \n";    
              sql += "     , b.subjnm \n";
              sql += "from   tz_student a, tz_subj b \n";
              sql += "where  a.subj = b.subj \n";
              sql += "and    a.userid = " + StringManager.makeSQL(userid) + " \n";
              */
              
              sql = " select distinct a.subj \n";    
              sql += "     , b.subjnm \n";
              sql += "from   tz_student a, tz_subj b, tz_subjseq c \n";
              sql += "where  a.subj = b.subj \n";
              sql += "and    a.subj = c.subj \n";
              sql += "and    a.subjseq = c.subjseq \n";
              sql += "and    a.year = c.year \n";
              sql += "and    b.isonoff != 'OFF' \n";
              sql += "and    c.edustart <= to_char(sysdate,'yyyymmddhh24') \n";
              sql += "and    c.eduend >= to_char(sysdate,'yyyymmddhh24') \n";
              sql += "and    a.userid = " + StringManager.makeSQL(userid) + " \n";
              
              
              ls = connMgr.executeQuery(sql);

              result  = "<select id='" + name + "' name='" + name + "' " + event + " style='font-size:8pt;'>";
              result += "<option value='ALL' selected >��ü</option>";

              String v_subj =   "";
              String v_year =   "";
              String v_subjseq  =   "";
              String v_subjnm   =   "";
              String v_contenttype  =   "";
              String v_eduurl       =   "";

              while ( ls.next() ) {
                  v_subj        =   ls.getString("subj");
                  v_subjnm      =   ls.getString("subjnm");
                  
                  result += "<option value=\"" + v_subj + "\" ";
                  if ( selected.equals( v_subj ) ) {
                      result += " selected ";
                  }
                  result += " > " + v_subjnm + "</option>";
                  cnt++;
              }
              result += "</select>";
          }
          catch ( Exception ex ) {
              ErrorManager.getErrorStackTrace(ex);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
          }
          finally {
              if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return result;
      }

      /**
      �н����� ���� ����Ʈ
      @param box      receive from the form object and session
      @return ArrayList
      */
       public ArrayList selectEducationSubjectList2(RequestBox box) throws Exception { 
      	 DBConnectionManager	connMgr	= null;
           ListSet ls1         = null;
           ArrayList list1     = null;
           String sql         = "";
           DataBox dbox        = null;

           String  v_user_id   = box.getSession("userid");
           String  v_ip        = box.getSession("userip");
           
           try { 
               connMgr = new DBConnectionManager();
               list1 = new ArrayList();

                   sql  =
                       "\nselect                                                                                         " +
                       "\n       tsj.contenttype, tst.subj, tst.year, tst.subjseq, tst.isgraduated, tst.branch,             " +
                       "\n       tss.edustart, tss.eduend,						                                              " +
                       "\n       tss.edustart studystart, tss.eduend studyend,                                              " +
                       "\n       tsj.eduurl, tsj.isonoff, tsj.subjnm, tsj.subjclass,                                        " +
                       "\n       tsj.upperclass, tsj.middleclass, tsj.lowerclass, GET_SUBJCLASSNM(tsj.upperclass, '000', '000') cname, tsj.cpsubj, tss.cpsubjseq,  " +
                       "\n       CASE WHEN substr(to_char(sysdate,'YYYYMMDDHH24MISS'), 1, 10) BETWEEN tss.EduStart  AND tss.EduEnd  THEN 'E'  " +
                       "\n            WHEN substr(to_char(sysdate,'YYYYMMDDHH24MISS'), 1, 10) BETWEEN tss.PropStart AND tss.PropEnd THEN 'R'  " +
                       "\n            ELSE ''                                                                               " +
                       "\n       END    ProcEduFlag,                                                                        " +
                       "\n       tss.isattendchk " +
                       //"\n       , datediff(day, convert(datetime, (case when len(tss.edustart) >= 8 then substring(tss.edustart, 1, 8) else '00000000' end )), convert(datetime, (case when len(tss.eduend) >= 8 then substring(tss.eduend, 1, 8) else '00000000' end ))) + 1 edudays \n" +
                       "\n       , tsj.isoutsourcing                                                                       " +
                       "\n  from                                                                                           " +
                       "\n       tz_student tst, tz_subjseq tss, tz_subj tsj                               " +
                       "\n where 1=1                                                                                      " +
                       "\n       and tst.userid      = ':userid'                                                            " +
                       "\n       and tst.subj        = tss.subj                                                             " +
                       "\n       and tst.year        = tss.year                                                             " +
                       "\n       and tst.subjseq     = tss.subjseq                                                          " +
                       "\n       and tst.subj        = tsj.subj                                                             " +
//                       "\n       and tsj.isonoff     = 'ON'                                                                 " +
//                       "\n       and tsj.subjclass   = tsa.subjclass                                                        " +
//                       "\n       and tsa.middleclass = '000'			                                                     " +
//                       "\n       and tsa.lowerclass  = '000'				                                                 " +
                       "\n       and tss.propstart   <= substr(to_char(sysdate,'YYYYMMDDHH24MISS'), 1, 10)                                    " +
                       "\n       and tss.eduend      >= substr(to_char(sysdate,'YYYYMMDDHH24MISS'), 1, 10)                                    " +
//                       "\n       and tss.isblended   = 'N'                                                                  " +
//                       "\n       and tss.isexpert      = 'N'                                                                  " +
                       "\n      and course = '000000'                                                                     " +
                       "\n order by                                                                                       " +
                       "\n       tss.edustart, GET_SUBJCLASSNM(tsj.upperclass, '000', '000'), tsj.subjnm                                                    ";

               
               sql = sql.replaceAll( ":userid", v_user_id );
               
               ls1 = connMgr.executeQuery(sql);

               while ( ls1.next() ) { 
                   dbox = ls1.getDataBox();
                   if ( ls1.getString("isonoff").equals("OFF") )
                   {
                   	if(ls1.getString("isattendchk").equals("Y") ){  //�⼮üũ �����Ǿ�������츸.
                         int isAtt = isAttendenceAvailable( ls1.getString("subj"), ls1.getString("year"), ls1.getString("subjseq"), v_user_id, v_ip );
                         dbox.put( "d_isatt", new Integer(isAtt) );
                       }
                   }
                   else
                   {
                       dbox.put( "d_isatt", "CYBER" );
                   }
                   list1.add(dbox);
               }
           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
           } finally { 
               if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }

           return list1;
      }
       
	public static String getYear(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
		DBConnectionManager	connMgr	= null;
		PreparedStatement   pstmt   = null;        
		ListSet             ls      = null;
		String              sql     = "";
		String result = "";
	        
		try { 
			String v_userid = box.getSession("userid");
			String v_sysyear = FormatDate.getDate("yyyy");
			//String v_stoldyear = box.getStringDefault("p_stoldyear", v_sysyear);
			String v_stoldyear = box.getStringDefault("p_stoldyear", "ALL");
    
			connMgr = new DBConnectionManager();
    
			sql = "\n select distinct year "
				+ "\n from   tz_stold "
				+ "\n where  userid = " + StringManager.makeSQL(v_userid)
               	+ "\n union  "
               	+ "\n select " + StringManager.makeSQL(v_sysyear) + " year "
               	+ "\n from   dual "
               	+ "\n order  by year desc ";		
    
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
            ls = new ListSet(pstmt);     
            
            result += SelectSubjBean.getSelectTag( ls, isChange, isALL, "p_stoldyear", v_stoldyear);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }         
	
	
	public DataBox selectBookDeliveryInfo(RequestBox box) throws Exception {
		
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;

        DataBox           dbox    	= null;
        String              sql     = "";
		String v_subj 				= box.getString("p_subj");
		String v_year 				= box.getString("p_year");
		String v_subjseq 			= box.getString("p_subjseq");
		String v_userid 			= box.getSession("userid");
		
        try { 
            connMgr = new DBConnectionManager();
              
            sql = "    SELECT a.* FROM TZ_BOOKDELIVERY a "            	
            	+ "\n   WHERE  a.SUBJ 		= " + StringManager.makeSQL(v_subj)
            	+ "\n     AND  a.YEAR 		= " + StringManager.makeSQL(v_year)
            	+ "\n     AND  a.SUBJSEQ 	= " + StringManager.makeSQL(v_subjseq)
            	+ "\n     AND  a.USERID 	= " + StringManager.makeSQL(v_userid);             	  
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            	dbox = ls.getDataBox();              
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return dbox;			
	}   	
}
