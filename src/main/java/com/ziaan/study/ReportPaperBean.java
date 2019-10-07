// **********************************************************
//  1. ��      ��: PROJECT ADMIN BEAN
//  2. ���α׷���: ProjectAdminBean.java
//  3. ��      ��: ���� ������ bean
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: 
//  7. ��      ��: 2005. 11.24 �̰��
// **********************************************************
package com.ziaan.study;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ziaan.library.CalcUtil;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.research.SulmunAllBean;
import com.ziaan.system.SelectionUtil;

public class ReportPaperBean { 

    public ReportPaperBean() { }
    
    /**
     * ����Ʈ�������� ��������� ����Ʈ ���� ��Ȳ 
     * @param box
     * @return
     * @throws Exception
     */
    /**
    ���� ���� �׷� ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectReportPaperPeriod(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          		= null;
        String sql          		= "";
        DataBox dbox   				= null;
        ArrayList list				= null;
        String  v_subj      		= box.getString("s_subjcourse");        // ����
        String  v_year      		= box.getString("s_gyear");          	// �⵵
        String  v_subjseq      		= box.getString("s_subjseq");         	// ������
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = " SELECT SUBJ, SUBJNM											\n";
            sql += " 	,MREPORT_START 											\n";
            sql += " 	,MREPORT_END 											\n";
            sql += " 	,FREPORT_START 											\n";
            sql += " 	,FREPORT_END 											\n";
            sql += " FROM TZ_SUBJSEQ 											\n";
            sql += " WHERE SUBJ = " + StringManager.makeSQL(v_subj)+" 			\n";
            sql += "   AND YEAR = " + StringManager.makeSQL(v_year)+" 			\n";
            sql += "   AND SUBJSEQ = " + StringManager.makeSQL(v_subjseq)+" 	\n";
                       
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
          	  dbox = ls.getDataBox();
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
     * ���� ���� ����� ���� ����Ʈ 
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectReportQuestionsAList(RequestBox box) throws Exception { 
    	DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;       
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        ReportData data1   = null;
        ReportData data2   = null;
        String  v_Bcourse   = ""; // �����ڽ�
        String  v_course    = ""; // �����ڽ�
        String  v_Bcourseseq= ""; // �����ڽ����
        String  v_courseseq = ""; // �����ڽ����                
        int     l           = 0;
        //String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // �����׷�
        //String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // �⵵
        //String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // �������
        
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // ����з�
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // ����з�
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // ����з�
        
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// ����&�ڽ�
        //String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // ���� ���
        String  ss_action   = box.getString("s_action");
        
        String  v_orderColumn   = box.getString("p_orderColumn");               // ������ �÷���
        String  v_orderType     = box.getString("p_orderType");                 // ������ ����
        try { 
            if ( ss_action.equals("go") ) {  
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();
                    
                // select course,cyear,courseseq,coursenm,subj,year,subjseq,isclosed,subjnm,isonoff
                /*
                sql1 = "select  distinct ";
                sql1 += "        ";
                sql1 += "        ";
                sql1 += "        ";
                sql1 += "        a.subj,";
                sql1 += "        a.year,";
                sql1 += "        a.subjseq,";
                sql1 += "        a.subjseqgr,";
                sql1 += "        a.isclosed,";
                sql1 += "        a.subjnm,";
                sql1 += "        a.isonoff, ";
                sql1 += "        a.edustart, ";    // �����Ⱓ ������
                sql1 += "        a.eduend, ";    // �����Ⱓ ������                
                sql1 += "        (select classname from tz_subjatt WHERE upperclass=a.scupperclass and  middleclass = '000' and lowerclass = '000') classname, ";
                //sql1 += "        (select count(distinct projseq) from tz_projord where subj=a.subj and year=a.year and subjseq=a.subjseq) projseqcnt, ";
                //sql1 += "        (select count(ordseq) from tz_projord where subj=a.subj and year=a.year and subjseq=a.subjseq) ordseqcnt ";
                
                sql1 += "        0 projseqcnt, ";
                sql1 += "        0 ordseqcnt ";
                
                sql1 += "from    VZ_SCSUBJSEQ a where 1 = 1 ";
                if ( !ss_grcode.equals("ALL") ) { 		//�����׷�
                    sql1 += " and a.grcode = '" + ss_grcode + "'";
                } 
                if ( !ss_gyear.equals("ALL") ) { 		//�⵵
                    sql1 += " and a.gyear = '" + ss_gyear + "'";
                } 
                if ( !ss_grseq.equals("ALL") ) {		// �������
                    sql1 += " and a.grseq = '" + ss_grseq + "'";
                }

                if ( !ss_uclass.equals("ALL") ) { 		//��з�
                    sql1 += " and a.scupperclass = '" + ss_uclass + "'";
                }
                
                if ( !ss_mclass.equals("ALL") ) { 		//�ߺз�
                    sql1 += " and a.scmiddleclass = '" + ss_mclass + "'";
                }
                
                if ( !ss_lclass.equals("ALL") ) { 		//�Һз�
                    sql1 += " and a.sclowerclass = '" + ss_lclass + "'";
                }
                
                if ( !ss_subjcourse.equals("ALL") ) { 		//�ڽ� KT�� �Ⱦ���.
                    sql1 += " and a.scsubj = '" + ss_subjcourse + "'";
                }
                if ( !ss_subjseq.equals("ALL") ) { 			//�������
                    sql1 += " and a.scsubjseq = '" + ss_subjseq + "'";
                }
                
                if ( v_orderColumn.equals("") ) { 
                    sql1 += " order by  a.subj, a.year, a.subjseq ";
                } else { 
                    sql1 += " order by " + v_orderColumn + v_orderType;
                }
                
                
                ls1 = connMgr.executeQuery(sql1);
                
                    while ( ls1.next() ) { 
                        data1 = new ReportData();
                        data1.setClassname( ls1.getString("classname") );
                        //data1.setCourse( ls1.getString("course") );
                        //data1.setCyear( ls1.getString("cyear") );
                        //data1.setCourseseq( ls1.getString("courseseq") );
                        //data1.setCoursenm( ls1.getString("coursenm") );                    
                        data1.setSubj( ls1.getString("subj") );
                        data1.setYear( ls1.getString("year") );              
                        data1.setSubjseq( ls1.getString("subjseq") );
                        data1.setSubjseqgr( ls1.getString("subjseqgr") );
                        data1.setSubjnm( ls1.getString("subjnm") );
                        data1.setProjseqcnt( ls1.getInt("projseqcnt") );
                        data1.setOrdseqcnt( ls1.getInt("ordseqcnt") );
                        data1.setIsclosed( ls1.getString("isclosed") );                            
                        data1.setIsonoff( ls1.getString("isonoff") );     
                        data1.setEdustart( ls1.getString("edustart") );                             
                        data1.setEduend( ls1.getString("eduend") );     
                            
                        list1.add(data1);
                    }
                    for ( int i = 0;i < list1.size(); i++ ) { 
                        data2       =   (ReportData)list1.get(i);
                        v_course    =   data2.getCourse();
                        v_courseseq =   data2.getCourseseq();
                        if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                            sql2 = "select count(subj) cnt from VZ_SCSUBJSEQ ";
                            sql2 += "where course = '" + v_course + "' and courseseq = '" +v_courseseq + "' ";
                            if ( !ss_grcode.equals("ALL") ) { 
                                sql2 += " and grcode = '" + ss_grcode + "'";
                            } 
                            if ( !ss_gyear.equals("ALL") ) { 
                                sql2 += " and gyear = '" + ss_gyear + "'";
                            } 
                            if ( !ss_grseq.equals("ALL") ) { 
                                sql2 += " and grseq = '" + ss_grseq + "'";
                            }
                            if ( !ss_uclass.equals("ALL") ) { 
                                sql2 += " and scupperclass = '" + ss_uclass + "'";
                            }

                            if ( !ss_mclass.equals("ALL") ) { 
                                sql2 += " and scmiddleclass = '" + ss_mclass + "'";
                            }

                            if ( !ss_lclass.equals("ALL") ) { 
                                sql2 += " and sclowerclass = '" + ss_lclass + "'";
                            }


                            if ( !ss_subjcourse.equals("ALL") ) { 
                                sql2 += " and scsubj = '" + ss_subjcourse + "'";
                            }
                            if ( !ss_subjseq.equals("ALL") ) { 
                                sql2 += " and scsubjseq = '" + ss_subjseq + "'";
                            }
//                            System.out.println("sql2 ==  ==  ==  ==  ==  == > " +sql2);
                            ls2 = connMgr.executeQuery(sql2);   
                            if ( ls2.next() ) { 
                                data2.setRowspan( ls2.getInt("cnt") );
                                data2.setIsnewcourse("Y");
                            }
                        } else { 
                            data2.setRowspan(0);
                            data2.setIsnewcourse("N");
                        }
                        v_Bcourse   =   v_course;
                        v_Bcourseseq=   v_courseseq;
                        list2.add(data2);
                        if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    }*/
                
                sql1  = " select subj , subjnm , ";
                sql1 += " (select classname from tz_subjatt WHERE upperclass=a.upperclass and  middleclass = '000' and lowerclass = '000') classname, ";
                //sql1 += "        (select count(distinct projseq) from tz_projord where subj=a.subj and year=a.year and subjseq=a.subjseq) projseqcnt, ";
                sql1 += "        (select count(ordseq) from tz_projord where subj=a.subj) ordseqcnt ";
                sql1 += " from tz_subj a where 1 = 1 ";

	                if ( !ss_uclass.equals("ALL") ) { 		//��з�
	                    sql1 += " and a.upperclass = '" + ss_uclass + "'";
	                }
	                
	                if ( !ss_mclass.equals("ALL") ) { 		//�ߺз�
	                    sql1 += " and a.middleclass = '" + ss_mclass + "'";
	                }
	                
	                if ( !ss_lclass.equals("ALL") ) { 		//�Һз�
	                    sql1 += " and a.lowerclass = '" + ss_lclass + "'";
	                }
	                if ( !ss_subjcourse.equals("ALL") ) { 
                        sql1 += " and subj = '" + ss_subjcourse + "'";
                    }
	                
	                ls1 = connMgr.executeQuery(sql1);
	                
                    while ( ls1.next() ) { 
                        data1 = new ReportData();
                        data1.setClassname( ls1.getString("classname") );
                        //data1.setCourse( ls1.getString("course") );
                        //data1.setCyear( ls1.getString("cyear") );
                        //data1.setCourseseq( ls1.getString("courseseq") );
                        //data1.setCoursenm( ls1.getString("coursenm") );                    
                        data1.setSubj( ls1.getString("subj") );
                        //data1.setYear( ls1.getString("year") );              
                        //data1.setSubjseq( ls1.getString("subjseq") );
                        //data1.setSubjseqgr( ls1.getString("subjseqgr") );
                        data1.setSubjnm( ls1.getString("subjnm") );
                        //data1.setProjseqcnt( ls1.getInt("projseqcnt") );
                        data1.setOrdseqcnt( ls1.getInt("ordseqcnt") );
                        //data1.setIsclosed( ls1.getString("isclosed") );                            
                        //data1.setIsonoff( ls1.getString("isonoff") );     
                        //data1.setEdustart( ls1.getString("edustart") );                             
                        //data1.setEduend( ls1.getString("eduend") );     
                            
                        list1.add(data1);
                    }
                }
        }
        catch ( Exception ex ) { 
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
    ���� ���� ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectReportQuestionsList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        ReportData data   = null;
        String  v_subj      = box.getString("p_subj");          // ����
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // sql += "(select count(*) from TZ_PROJGRP where subj=A.subj and year=A.year "; 11/26 tz_projgrp �뵵 �˾Ƴ� ��
            // sql += "and subjseq=A.subjseq and ordseq=A.ordseq) as grcnt,A.groupcnt ";
            /*
             * 
             * 2008.11.19 ���� ���� ������ ����Ʈ�� �����;� �ؼ� �ּ�ó�� �ٽ� ���� ��������.
             * ����..��..���� ����� ���� �Ⱦ�..�÷��÷��÷��÷��÷��÷��÷��÷��÷�
            sql = "select   A.projseq, ";
            sql += "         A.ordseq,";
            sql += "         nvl(A.lesson,'') lesson,";
            sql += "         nvl((select sdesc from tz_subjlesson where subj = a.subj and lesson = a.lesson),'') lessonnm,";
            sql += "         A.reptype,";
            sql += "         A.isopen,";
            sql += "         A.isopenscore,";
            sql += "         A.title,";
            sql += "         A.score,";
            sql += "         nvl(A.expiredate,'') expiredate, ";
            sql += "         (select count(*) from TZ_STUDENT where subj=A.subj and year=A.year and subjseq=A.subjseq) as tocnt, ";
            sql += "         99 as grcnt,";
            sql += "         A.groupcnt, ";
            sql += "         isusedcopy, ";
            sql += "         (select count(*) from TZ_PROJORD where subj=A.subj and year=A.year and subjseq=A.subjseq and projseq=a.projseq) as rowspan,     ";
            sql += "         (select min(ordseq) from tz_projord where subj=a.subj and year=a.year and subjseq=a.subjseq and projseq=a.projseq ) rowspanseq, ";
            sql += "         A.upfile2, decode(A.useyn, 'Y', '���', '�̻��')  useyn,                                                                       ";
            sql += "         (select eduend from tz_subjseq x where x.subj=a.subj and x.year = a.year and x.subjseq = a.subjseq ) eduend                     ";
            sql += "from     TZ_PROJORD A ";
            sql += "where    A.subj='" +v_subj + "' and A.year='" +v_year + "' and A.subjseq='" +v_subjseq + "' ";
            sql += "order by a.projseq,A.ordseq";
Log.info.println("sql ==  ==  ==  ==  ==  == > " +sql);
             ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    data = new ProjectData();
                    data.setProjseq( ls.getInt("projseq") );
                    data.setOrdseq( ls.getInt("ordseq") );
                    data.setLesson( ls.getString("lesson") );
                    data.setLessonnm( ls.getString("lessonnm") );
                    data.setReptype( ls.getString("reptype") );
                    data.setIsopen( ls.getString("isopen") );
                    data.setIsopenscore( ls.getString("isopenscore") );
                    data.setTitle( ls.getString("title") );
                    data.setScore( ls.getInt("score") );
                    data.setTocnt( ls.getString("tocnt") );     
                    data.setGrcnt( ls.getString("grcnt") ); 
                    data.setExpiredate( ls.getString("expiredate") ); 
                    data.setGroupcnt( ls.getString("groupcnt") ); 
                    data.setRowspan( ls.getInt("rowspan") ); 
                    data.setIsusedcopy( ls.getString("isusedcopy") ); 
                    data.setRowspanseq( ls.getInt("rowspanseq") );

                    data.setUpfile2( ls.getString("upfile2") ); 
                    data.setUseyn( ls.getString("useyn") );                                       
                    data.setEduend( ls.getString("eduend") );                                       
                    list.add(data);
                }
                
                */
            sql = "select   A.subj,			\n";
            sql += "         A.ordseq,	\n";
            sql += "         A.reptype,	\n";
            sql += "         A.isopen,	\n";
            sql += "         A.isopenscore,	\n";
            sql += "         A.title,	\n";
            sql += "         A.score,	\n";
            sql += "         (select count(*) from TZ_PROJORD where subj=A.subj and ordseq=a.ordseq) as rowspan,     \n";
            sql += "         (select min(ordseq) from tz_projord where subj=a.subj and ordseq=a.ordseq ) rowspanseq, ";
            sql += "         A.upfile2, decode(A.useyn, 'Y', '���', '�̻��')  useyn \n";
            sql += "from     TZ_PROJORD A \n";
            sql += "where    A.subj='" +v_subj + "'  \n";
            sql += "order by A.ordseq \n";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new ReportData();
                //data.setProjseq( ls.getInt("projseq") );
                data.setSubj( ls.getString("subj") );
                data.setOrdseq( ls.getInt("ordseq") );
                //data.setLesson( ls.getString("lesson") );
                //data.setLessonnm( ls.getString("lessonnm") );
                data.setReptype( ls.getString("reptype") );
                data.setIsopen( ls.getString("isopen") );
                data.setIsopenscore( ls.getString("isopenscore") );
                data.setTitle( ls.getString("title") );
                data.setScore( ls.getInt("score") );
                //data.setTocnt( ls.getString("tocnt") );     
                //data.setGrcnt( ls.getString("grcnt") ); 
                //data.setExpiredate( ls.getString("expiredate") ); 
                //data.setGroupcnt( ls.getString("groupcnt") ); 
                data.setRowspan( ls.getInt("rowspan") ); 
                //data.setIsusedcopy( ls.getString("isusedcopy") ); 
                data.setRowspanseq( ls.getInt("rowspanseq") );

                data.setUpfile2( ls.getString("upfile2") ); 
                data.setUseyn( ls.getString("useyn") );                                       
                //data.setEduend( ls.getString("eduend") );                                       
                list.add(data);
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
     ���� ���
     @param box      receive from the form object and session
     @return int
     */
      public int insertReportQuestions(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;     
         ListSet ls                  = null;                   
         PreparedStatement pstmt2    = null;        
         String sql1                 = "";
         String sql2                 = "";        
         int isOk                    = 0;
         String v_user_id            = box.getSession("userid");        
         String v_subj               = box.getString("p_subj");          // ����
         String v_reptype            = box.getString("p_reptype");
         String v_title              = box.getString("p_title");
         String v_contents           = box.getString("p_contents");
         String v_isopen             = box.getString("p_isopen");
         String v_isopenscore        = box.getString("p_isopenscore");
         String v_score              = box.getString("p_score");
         String v_realFileName1      = box.getRealFileName("p_file1");
         String v_newFileName1       = box.getNewFileName("p_file1");
         String v_realFileName2      = box.getRealFileName("p_file2");
         String v_newFileName2       = box.getNewFileName("p_file2");
         int    v_groupcnt           = box.getInt("p_groupcnt");
         String v_ansyn              = box.getString("ansyn");     // �������ɼ�
         String v_useyn              = box.getString("useyn");     // ��뿩��          
         int    v_max                = 0;
         int    v_ordseq             = 0;
         int 	index 				 = 1;
                 
         try { 
             connMgr = new DBConnectionManager();
             sql1  = "select max(ordseq) from TZ_PROJORD ";
             sql1 += "where subj='" +v_subj + "' ";            
             ls = connMgr.executeQuery(sql1);
             
             if ( ls.next() ) { 
                 v_max = ls.getInt(1);
                 if ( v_max > 0) { v_ordseq = v_max + 1; }
                 else          { v_ordseq = 1;         }
             }
/* 2008.11.19 ������ ���� 
             sql2  = "insert into TZ_PROJORD(subj,year,subjseq,ordseq,projseq,lesson,reptype,";
             sql2 += "expiredate,title,contents,score,upfile,upfile2,realfile,realfile2,luserid,groupcnt,ldate,ansyn,useyn) ";
             sql2 += "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),?,?)";
*/             
             
             sql2  = "insert into TZ_PROJORD( ";
             sql2 += "					subj, ordseq, reptype, title, contents,			";//5
             sql2 += " 					score, upfile, upfile2, realfile, realfile2,	";//5 
             sql2 += " 					luserid, ldate, ansyn, useyn 					";//4
             sql2 += " 					) 												";
             sql2 += " values(";
             sql2 += " 			?,?,?,?,?, ";	//5
             sql2 += " 			?,?,?,?,?, ";//5
             sql2 += " 			?,to_char(sysdate,'YYYYMMDDHH24MISS'),?,? ";//4
             sql2 += " )";
             
             
             pstmt2 = connMgr.prepareStatement(sql2);

             pstmt2.setString(index++,v_subj);				//1
             pstmt2.setInt(index++,v_ordseq);
             pstmt2.setString(index++,v_reptype);
             pstmt2.setString(index++,v_title);
             pstmt2.setString(index++,v_contents);			//5
             pstmt2.setString(index++,v_score);
             pstmt2.setString(index++,v_newFileName1);
             pstmt2.setString(index++,v_newFileName2);
             pstmt2.setString(index++,v_realFileName1);
             pstmt2.setString(index++,v_realFileName2);		//10
             pstmt2.setString(index++,v_user_id);
             //pstmt2.setInt(index++,v_groupcnt);
             pstmt2.setString(index++,v_ansyn);
             pstmt2.setString(index++,v_useyn);            	//14
             isOk = pstmt2.executeUpdate();
            
         } catch ( Exception ex ) {                        
             throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
             if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }
      
      /**
      ���� ���� ������
      @param box      receive from the form object and session
      @return ProjectData   
      */
       public ReportData updateReportQuestionsPage(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          ListSet ls          = null;
          String sql          = "";
          ReportData data   = null;
          String  v_subj      = box.getString("p_subj");          // ����
          //String  v_year      = box.getString("p_year");          // �⵵
          //String  v_subjseq   = box.getString("p_subjseq");       // ������
          //String  v_lesson     = box.getString("p_lesson");         // ����
          int     v_ordseq    = box.getInt("p_ordseq");        
          
          try { 
              connMgr = new DBConnectionManager();

              // select reptype,expiredate,title,contents,score,groupcnt,upfile,upfile2
              sql = "select ordseq, reptype,title,contents,score,upfile,upfile2,realfile,realfile2 ";
              sql += "       ,ansyn, useyn ";            
              sql += "from TZ_PROJORD ";
              sql += "where subj='" +v_subj + "' ";
              sql += "and ordseq='" +v_ordseq + "'";

               ls = connMgr.executeQuery(sql);

                  if ( ls.next() ) { 
                      data = new ReportData();
                      //data.setProjseq( ls.getInt("projseq") );
                      data.setOrdseq( ls.getInt("ordseq") );
                      
                      data.setReptype( ls.getString("reptype") );
                      data.setTitle( ls.getString("title") );
                      data.setContents( ls.getString("contents") );
                      data.setScore( ls.getInt("score") );
                      data.setUpfile( ls.getString("upfile") );
                      data.setUpfile2( ls.getString("upfile2") );
                      data.setRealfile( ls.getString("realfile") );
                      data.setRealfile2( ls.getString("realfile2") );
                      data.setAnsyn( ls.getString("ansyn") );
                      data.setUseyn( ls.getString("useyn") );
                  }
          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex, box, sql);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
          } finally { 
              if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return data;        
      }


       /**
       ���� ����
       @param box      receive from the form object and session
       @return int
       */
        public int updateReportQuestions(RequestBox box) throws Exception { 
           DBConnectionManager	connMgr	= null;
           PreparedStatement pstmt     = null;
           String sql                  = "";
           ListSet ls                  = null;           
           int isOk                    = 0;
           String v_user_id            = box.getSession("userid");
           String v_reptype            = box.getString("p_reptype");
           String v_title              = box.getString("p_title");
           String v_contents           = box.getString("p_contents");
           String v_isopen             = box.getString("p_isopen");
           String v_isopenscore        = box.getString("p_isopenscore");
           String v_realFileName1      = box.getRealFileName("p_file1");
           String v_newFileName1       = box.getNewFileName("p_file1");
           String v_realFileName2      = box.getRealFileName("p_file2");
           String v_newFileName2       = box.getNewFileName("p_file2");
           String v_upfile             = box.getString("p_upfile");
           String v_upfile2            = box.getString("p_upfile2");
           String v_realfile           = box.getString("p_realfile");
           String v_realfile2          = box.getString("p_realfile2");     
           String v_check1             = box.getString("p_check1");    // ÷������1 �������� ����
           String v_check2             = box.getString("p_check2");    // ÷������2 �������� ����
           
           String v_subj               = box.getString("p_subj");          // ����
           
           String v_upfilesize         = "";
           String v_upfilesize2        = "";
           String v_ansyn              = box.getString("ansyn");     // �������ɼ�
           String v_useyn              = box.getString("useyn");     // ��뿩��   
                                            
           int    v_score              = box.getInt("p_score");        
           //int    v_groupcnt           = box.getInt("p_groupcnt");
           int    v_ordseq             = box.getInt("p_ordseq"); 
           if ( v_newFileName1.length() == 0) {   v_newFileName1 = v_upfile;   }
           if ( v_newFileName2.length() == 0) {   v_newFileName2 = v_upfile2;   }
           
           // ���� ��������
           String v_oldupfile = v_upfile;
           String v_oldrealfile = v_realfile;
           String v_oldupfile2 = v_upfile2;
           String v_oldrealfile2 = v_realfile2;
           
           int index = 1;
                   
           try { 
               connMgr = new DBConnectionManager();
               /*
               sql = "select  count(userid)  ";
               sql += "from    TZ_PROJASSIGN ";
               sql += "where   subj='" + v_subj + "' ";
               sql += "  and   ordseq = " + v_ordseq ;

               ls = connMgr.executeQuery(sql);                  
               //if ( ls.next() ) { 
               if ( true ) {*/
                   // if ( ls.getInt(1) != 0) { 
                   //    isOk = -1; // ������ �н��ڰ� �־� ������ �� �����ϴ�.
                   //    
                   // } else { 
                   	
                   	                        
   				            // ���ε��� ������ ���� ���
   				            if ( v_realFileName1.equals("") ) { 
   				                // �������� ����
   				                if ( v_check1.equals("Y") ) { 
   				                	FileManager.deleteFile(v_newFileName1);
   				                    v_newFileName1   = "";
   				                    v_realFileName1  = "";
   				                } else { 
   				                // �������� ����
   				                    v_newFileName1   = v_oldupfile;
   				                    v_realFileName1  = v_oldrealfile;
   				                }
   				            }
   				            
   				            // ���ε��� ������ ���� ���
   				            if ( v_realFileName2.equals("") ) { 
   				                // �������� ����
   				                if ( v_check2.equals("Y") ) { 
   				                	FileManager.deleteFile(v_newFileName2);
   				                    v_newFileName2   = "";
   				                    v_realFileName2  = "";
   				                } else { 
   				                // �������� ����
   				                    v_newFileName2   = v_oldupfile2;
   				                    v_realFileName2  = v_oldrealfile2;
   				                }
   				            }
   				
   				
   				            sql  = "update TZ_PROJORD set reptype=?,title=?,";//2 ,3~9
   				            sql += "contents=?,score=?,upfile=?,upfile2=?,realfile=?,realfile2=?,luserid=?,ldate=to_char(sysdate,'YYYYMMDDHH24MISS'), ";
   				            sql += "isopen=?,isopenscore=?, ansyn=?, useyn=? ";//10~13
   				            sql += "where subj='" +v_subj + "' ";
   				            sql += "and ordseq='" +v_ordseq + "' ";
   				            
   				            pstmt = connMgr.prepareStatement(sql);
   				           // System.out.println("sql ==  ==  ==  ==  == > " +sql);
   				            
   				            pstmt.setString(index++,v_reptype);//1
   				            pstmt.setString(index++,v_title);
   				            pstmt.setString(index++,v_contents);
   				            pstmt.setInt(index++,v_score);
   				            pstmt.setString(index++,v_newFileName1);//5
   				            pstmt.setString(index++,v_newFileName2);
   				            pstmt.setString(index++,v_realFileName1);
   				            pstmt.setString(index++,v_realFileName2);
   				            pstmt.setString(index++,v_user_id);
   				            pstmt.setString(index++,v_isopen);//10
   				            pstmt.setString(index++,v_isopenscore);
   				            pstmt.setString(index++,v_ansyn);
   				            pstmt.setString(index++,v_useyn);   //13            
   				            isOk = pstmt.executeUpdate();
           
                   // }
//                }            
           } catch ( Exception ex ) {                        
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
           } finally { 
           	  if ( ls != null )  { try { ls.close(); } catch ( Exception e ) { } }
               if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }

           return isOk;
       }
        /**
        ���� ����
        @param box      receive from the form object and session
        @return int
        */
         public int deleteProjectReport(RequestBox box) throws Exception { 
            DBConnectionManager	connMgr	= null;
            PreparedStatement pstmt2 = null; 
            ListSet ls         = null;     
            String sql1         = ""; 
            String sql2         = ""; 
            int isOk            = 0;
            String  v_subj      = box.getString("p_subj");          // ����
            String  v_year      = box.getString("p_year");          // �⵵
            String  v_subjseq   = box.getString("p_subjseq");       // ������        
            int     v_ordseq    = box.getInt("p_ordseq");           // ������ȣ  


            try { 
                connMgr = new DBConnectionManager();

                
                    
                    sql1 = "select  count(userid)  ";
                    sql1 += "from    TZ_PROJASSIGN ";
                    sql1 += "where   subj='" + v_subj + "' and year='" + v_year + "' and ";
                    sql1 += "        subjseq='" + v_subjseq + "' and ordseq = " + v_ordseq ;

                    ls = connMgr.executeQuery(sql1);  
                
                    if ( ls.next() ) { 
                        if ( ls.getInt(1) != 0) { 
                            isOk = -1; // ������ �н��ڰ� �־� ������ �� �����ϴ�.
                        } else { 
                            // Delete tz_projrep
                            sql2 = "delete  TZ_PROJORD ";
                            sql2 += "where   subj = ? and year=? and subjseq=? and ordseq = ? ";
                            pstmt2 = connMgr.prepareStatement(sql2);
                
                            pstmt2.setString(1, v_subj);
                            pstmt2.setString(2, v_year);
                            pstmt2.setString(3, v_subjseq);
                            pstmt2.setInt(4, v_ordseq);
                            
                            isOk = pstmt2.executeUpdate();
                        }
                     }

            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, box, sql2);
                throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
            } finally { 
                if ( ls != null )  { try { ls.close(); } catch ( Exception e1 ) { } }     
                if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }     
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }

            return isOk;
        }
         /**
         �������� ����Ʈ (������ �������� ��Ͻ�Ű�� ���� ���� ���� select)
         @param box      receive from the form object and session
         @return ProjectData   
         */
          public ArrayList selectQuestionList(RequestBox box) throws Exception { 
             DBConnectionManager connMgr	= null;
             ListSet ls          			= null;
             String sql          			= "";
             ArrayList list   				= null;
             DataBox dbox    				= null;
             String  v_subj      			= box.getString("p_subj");          // ����
             String  v_year      			= box.getString("p_year");          // �⵵
             String  v_subjseq   			= box.getString("p_subjseq");       // ������
             
             try { 
            	 connMgr = new DBConnectionManager();

                 // select reptype,expiredate,title,contents,score,groupcnt,upfile,upfile2
                 //2008.12.03 ���̺������� ����
            	 sql = "select ordseq,title,contents,score,upfile,upfile2,realfile,realfile2 ";
                 sql += "     , useyn ";
                 sql += "from TZ_PROJORD ";
                 sql += "where  useyn='Y' and subj = '" +v_subj + "' ";

                 ls = connMgr.executeQuery(sql);
                 list = new ArrayList();
                 while ( ls.next() ) {
                     dbox = ls.getDataBox();
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
          ���� ������ ���
          @param box      receive from the form object and session
          @return isOk    
          **/  
          public int insertPaper(RequestBox box) throws Exception { 
              DBConnectionManager	connMgr	= null;
              int isOk = 0;

              //String v_grcode       = box.getString("p_grcode");
              String v_year         = box.getString("p_year");	//�⵵
              String v_subj         = box.getString("p_subj");	//����
              String v_subjseq      = box.getString("p_subjseq");//�б�
              String v_sulnums      = box.getString("p_sulnums");	//������ 1,2,3�̷��������� ���� ���� ��ȣ�� �Ѿ�´�. �򰡿����� �ٸ� �����ؾ���...
              String v_title		= box.getString("p_title");
              int    v_reportpapernum  = 0;
              String v_projrepstart = box.getString("p_sdate");	//���������
              String v_projrepend   = box.getString("p_edate");	//����������
       
              String v_luserid   	= box.getSession("userid");		//����������
              
              String v_projgubun 	= box.getString("p_projgubun");	//����ƮŸ��(�߰�:M, �⸻:F)
              String v_reptype 		= box.getString("p_reptype");	//����Ʈ����(�ܴ���:S, ������: P)
              try { 

                  connMgr = new DBConnectionManager();
                  connMgr.setAutoCommit(false);

                  v_reportpapernum = getPapernumSeq(v_subj, v_year);	//�׷��ȣ grpseq

                  isOk = insertTZ_reportpaper(connMgr, v_subj, v_year, v_subjseq, v_reportpapernum, v_sulnums, v_luserid, v_projgubun, v_projrepstart, v_projrepend, v_title, v_reptype);
                  
              } catch ( Exception ex ) { 
                  isOk = 0;
                  connMgr.rollback();
                  ErrorManager.getErrorStackTrace(ex);
                  throw new Exception(ex.getMessage() );
              } finally { 
                  if ( isOk > 0 ) { connMgr.commit(); }
                  if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
              }

              return isOk;
          }
          
          /**
          ���� ������ ���
          @param  connMgr
          @param  p_year       �⵵
          @param  p_subj       ����
          @param  p_grpseq     ��������ȣ
          @param  p_ordseqs    ������ȣ
          @param  p_luserid    �ۼ���               
          @return isOk    
          @return isOk1    
          **/  
          public int insertTZ_reportpaper(DBConnectionManager connMgr, String p_subj
        		  , String p_year, String p_subjseq, int p_grpseq, String p_ordseqs
        		  , String p_luserid, String p_projgubun, String p_sdate, String p_edate
        		  , String p_title, String p_reptype) throws Exception { 
        	  
              PreparedStatement   pstmt   = null;
              PreparedStatement   pstmt1   = null;
              ListSet ls          			= null;
              ArrayList list = null;
              String              sql     = "";
              String              sql1    = "";
              String              sql2    = "";
              int 				  isOk 	  = 0;
              int 				  isOk1	  = 0;
              int 				  score	  = 0;
              int 				  totalscore = 0;
              try {
            	  String[] v_ordseq = p_ordseqs.split(",");
            	  for(int j = 0; j < v_ordseq.length; j++){
            		  sql2  = " select score from tz_projord ";
            		  sql2 += " where ordseq =  " + v_ordseq[j];
            		  sql2 += "   and subj   =  " + StringManager.makeSQL(p_subj);
            		  
            		  ls = connMgr.executeQuery(sql2);
            		  if ( ls.next() ) {
            			  score = ls.getInt("score");
            			  totalscore += score;
            		  }
            		  if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            	  }
            	  if(totalscore != 100){
            		  isOk = -1;
            	  }
            	  if(isOk != -1){
	                  // insert TZ_SULPAPER table
	                  sql =  "insert into TZ_projgrp ";
	                  sql +=  "( subj, year, subjseq, grpseq, projgubun    ";	//5
	                  sql +=  "  ,sdate, edate, luserid, ldate, grpseqnm, reptype, grptotalscore)   ";	//7
	                  sql +=  " values ";
	                  sql +=  "( ?,?,?,?,? ";
	                  sql +=  "  ,?,?,?,?,?,?,? ) ";
	
	                  pstmt = connMgr.prepareStatement(sql);
	                  
	                  pstmt.setString( 1, p_subj							);
	                  pstmt.setString( 2, p_year							);
	                  pstmt.setString( 3, p_subjseq 						);
	                  pstmt.setInt	 ( 4, p_grpseq							);
	                  pstmt.setString( 5, p_projgubun						);            
	                  pstmt.setString( 6, p_sdate							);
	                  pstmt.setString( 7, p_edate							);
	                  pstmt.setString( 8, p_luserid							);
	                  pstmt.setString( 9, FormatDate.getDate("yyyyMMddHHmmss"));
	                  pstmt.setString( 10, p_title							);
	                  pstmt.setString( 11, p_reptype						);
	                  pstmt.setInt   ( 12, totalscore						);
	
	                  isOk = pstmt.executeUpdate();
	                  //tz_projmap �� ������ȣ�� �׷��ȣ�� ����
	                  //for������ ������ȣ�� �׷��ȣ�� �־���Ѵ�.
	                  v_ordseq = p_ordseqs.split(",");
	                  for(int i = 0; i < v_ordseq.length; i++){
		                  sql1 =  "insert into TZ_projmap ";
		                  sql1 +=  "( subj, year, subjseq,      ";
		                  sql1 +=  "  grpseq, ordseq, luserid, ldate)   ";
		                  sql1 +=  " values ";
		                  sql1 +=  "(?,?,?, ";
		                  sql1 +=  " ?,?,?,? ) ";
		                  pstmt1 = connMgr.prepareStatement(sql1);
		
		                  pstmt1.setString( 1, p_subj);
		                  pstmt1.setString( 2, p_year);
		                  pstmt1.setString( 3, p_subjseq );
		                  pstmt1.setInt	  ( 4, p_grpseq);
		                  pstmt1.setInt   ( 5, Integer.parseInt(v_ordseq[i]));            
		                  pstmt1.setString( 6, p_luserid);
		                  pstmt1.setString( 7, FormatDate.getDate("yyyyMMddHHmmss"));
		                  isOk1 = pstmt1.executeUpdate();
		                  
		                  if(isOk1 >0){connMgr.commit();}
		                  else{connMgr.rollback();}
		                  if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
	                  }
            	  }
             }
             catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex);
                 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
             }
             finally { 
                 if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
                 if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                 if ( isOk > 0 ) { connMgr.commit(); }
             }
             return isOk;
          }
          public int getPapernumSeq(String p_subj, String p_year) throws Exception { 
              Hashtable maxdata = new Hashtable();
              maxdata.put("seqcolumn","grpseq");
              maxdata.put("seqtable","tz_projgrp");
              maxdata.put("paramcnt","2");
              maxdata.put("param0","subj");
              maxdata.put("param1","year");
              maxdata.put("subj",   SQLString.Format(p_subj));
              maxdata.put("year",   SQLString.Format(p_year));

              return SelectionUtil.getSeq(maxdata);
          }
          
          /**
          ���� ���� �׷� ����Ʈ
          @param box      receive from the form object and session
          @return ArrayList
          */
           public ArrayList selectReportGroupList(RequestBox box) throws Exception { 
              DBConnectionManager	connMgr	= null;
              ListSet ls          		= null;
              ArrayList list      		= null;
              String sql          		= "";
              DataBox dbox   			= null;
              String  v_subj      		= box.getString("s_subjcourse");        // ����
              String  v_year      		= box.getString("s_gyear");          	// �⵵
              String  v_subjseq      	= box.getString("s_subjseq");         	// ������
              try { 
                  connMgr = new DBConnectionManager();
                  list = new ArrayList();
                  sql  = " select distinct a.grpseq, a.subj, a.year, a.subjseq, a.projgubun, a.sdate, a.edate, a.grpseqnm, a.reptype \n";
                  sql += " , (select count(*) from tz_projmap m where m.grpseq = a.grpseq and m.subj = " + StringManager.makeSQL(v_subj) 
                  		+ " and m.subjseq = " + StringManager.makeSQL(v_subjseq) + " and m.year = " + StringManager.makeSQL(v_year) + ") qcnt  \n";
                  sql += " from tz_projgrp a, tz_projmap b 				\n";
                  sql += " where 1=1 									\n";
                  sql += "   and a.subj = b.subj(+) 					\n";
                  sql += "   and a.year = b.year(+) 					\n";
                  sql += "   and a.subjseq = b.subjseq(+) 				\n";
                  sql += "   and a.grpseq = b.grpseq(+) 				\n";
                  sql += "   and a.subj = " + StringManager.makeSQL(v_subj);
                  sql += "   and a.year = " + StringManager.makeSQL(v_year);
                  sql += "   and a.subjseq =  "+ StringManager.makeSQL(v_subjseq);
                  sql += "   order by a.projgubun desc , a.grpseq asc \n";
                  
                  ls = connMgr.executeQuery(sql);

                  while ( ls.next() ) { 
                	  dbox = ls.getDataBox();
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
           ���� ���� �׷� ����Ʈ
           @param box      receive from the form object and session
           @return ArrayList
           */
            public ArrayList selectPaperQuestionList(RequestBox box) throws Exception { 
               DBConnectionManager	connMgr	= null;
               ListSet ls          		= null;
               ArrayList list      		= null;
               String sql          		= "";
               DataBox dbox   			= null;
               //String  v_subj      		= box.getString("s_subjcourse");        // ����
               //String  v_year      		= box.getString("s_gyear");          	// �⵵
               //String  v_subjseq      	= box.getString("s_subjseq");         	// ������
               String  v_subj      		= box.getString("p_subj");        		// ����
               String  v_year      		= box.getString("p_year");          	// �⵵
               String  v_subjseq      	= box.getString("p_subjseq");         	// ������
               String  v_grpseq      	= box.getString("p_grpseq");         	// �׷��ȣ
               try { 
                   connMgr = new DBConnectionManager();
                   list = new ArrayList();
                   sql  = " select 																\n";
                   sql += " 	    a.subj, a.year,a.grpseq, a.subjseq  						\n";
                   sql += " 	,   b.ordseq, b.title, b.contents, b.score  					\n";
                   sql += " 	,   b.upfile, b.upfile2, b.realfile, b.realfile2				\n";
                   sql += " 	,   b.useyn														\n";
                   sql += " from tz_projmap a, tz_projord b  									\n";
                   sql += " where a.ordseq = b.ordseq 											\n";
                   sql += "   and a.subj = b.subj 												\n";
                   sql += "   and a.subj = " + StringManager.makeSQL(v_subj);
                   sql += "   and a.subjseq = " + StringManager.makeSQL(v_subjseq);
                   sql += "   and a.year = " + StringManager.makeSQL(v_year);
                   sql += "   and a.grpseq = " + StringManager.makeSQL(v_grpseq);
                   sql += "   order by b.ordseq ";
                   
                   ls = connMgr.executeQuery(sql);

                   while ( ls.next() ) { 
                 	  dbox = ls.getDataBox();
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
            ���� ������ ���
            @param box      receive from the form object and session
            @return isOk    
            **/  
            public int updatePaper(RequestBox box) throws Exception { 
                DBConnectionManager	connMgr	= null;
                int isOk = 0;
                
                String v_year         = box.getString("p_year");		//�⵵
                String v_subj         = box.getString("p_subj");		//����
                String v_subjseq      = box.getString("p_subjseq");		//�б�
                String v_sulnums      = box.getString("p_sulnums");		//������ȣ��. ���������� �Ⱦ���. �����ȴ�..������.�̷��Զ� ���ִ� ����?�ǵ����� �ְܹ�����
                String v_title		= box.getString("p_title");
                int    v_reportpapernum  = 0;
                String v_projrepstart = box.getString("p_sdate");		//���������
                String v_projrepend   = box.getString("p_edate");		//����������
         
                String v_luserid   = box.getSession("userid");			//����������
                
                String v_projgubun = box.getString("p_projgubun");		//����Ʈ����(�߰�,�⸻)
                String v_reptype = box.getString("p_reptype");			//����ƮŸ��(�ܴ���:S, ������:P)
                
                int v_grpseq		= box.getInt("p_grpseq");			//������ ��ȣ
                try { 

                    connMgr = new DBConnectionManager();
                    connMgr.setAutoCommit(false);
                    
                    isOk = updateTZ_reportpaper(connMgr, v_subj, v_year, v_subjseq, v_grpseq, v_sulnums, v_luserid, v_projgubun, v_projrepstart, v_projrepend, v_title, v_reptype);
                    
                } catch ( Exception ex ) { 
                    isOk = 0;
                    connMgr.rollback();
                    ErrorManager.getErrorStackTrace(ex);
                    throw new Exception(ex.getMessage() );
                } finally { 
                    if ( isOk > 0 ) { connMgr.commit(); }
                    if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
                }

                return isOk;
            }
			/**
            ���� ������ ����
            @param  connMgr
            @param  p_year       �⵵
            @param  p_subj       ����
            @param  p_grpseq     ��������ȣ
            @param  p_ordseqs    ������ȣ
            @param  p_luserid    �ۼ���               
            @return isOk    
            @return isOk1    
            **/  
            public int updateTZ_reportpaper(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, int p_grpseq, String p_ordseqs, String p_luserid, String p_projgubun, String p_sdate, String p_edate, String p_title, String p_reptype) throws Exception { 
                PreparedStatement   pstmt   = null;
                PreparedStatement   pstmt1   = null;
                PreparedStatement   pstmt2   = null;
                String              sql     = "";
                String              sql1    = "";
                String              sql2    = "";
                String              sql3    = "";
                int 				  isOk 	  = 0;
                int 				  isOk1	  = 0;
                int 				  isOk2	  = 0;
                int 				  v_cnt	  = 0;
                ListSet ls        			  = null;
                int 				  score	  = 0;
                int 				  totalscore = 0;
                
                try {
                	// ���� �ϱ� �� �ش� �������� ���� ���θ� Ȯ���ϰ� ������ �������� ����,������ �Ұ����ϴ�.
                	sql3 = " SELECT COUNT(USERID) cnt		\n";
                    sql3+= "   FROM TZ_PROJASSIGN 			\n";
                    sql3+= " WHERE 1=1 						\n";
                    sql3+= "   AND SUBJ = " + StringManager.makeSQL(p_subj);
                    sql3+= "   AND SUBJSEQ = " + StringManager.makeSQL(p_subjseq);
                    sql3+= "   AND YEAR = " + StringManager.makeSQL(p_year);
                    sql3+= "   AND GRPSEQ = " + p_grpseq;
                    ls = connMgr.executeQuery(sql3);
                    
                    if ( ls.next() ) { 
                    	v_cnt = ls.getInt(1);
                    }
                    if(ls != null){ ls.close(); }
                    
                    /* �ϴ� �ּ����� ���߿� �ٽ� Ǯ����� 10.01.18 kjm */
                	//if ( v_cnt != 0) { 
                    	//isOk = -1; // ������ �н��ڰ� �־� ���� �� �� �����ϴ�.
                    //} else {
	                    String[] v_ordseq = p_ordseqs.split(",");
	                  	for(int j = 0; j < v_ordseq.length; j++){
	                  	  sql2  = " select score from tz_projord ";
	                  	  sql2 += " where ordseq =  " + v_ordseq[j];
	                  	  sql2 += "   and subj   =  " + StringManager.makeSQL(p_subj);
	                  	  
	                  	  ls = connMgr.executeQuery(sql2);
	                  	  if ( ls.next() ) {
	                  		  score = ls.getInt("score");
	                  		  totalscore += score;
	                  	  }
	                  	}
                    	if(totalscore != 100){
                    		isOk = -2; // ������ 100���� �ƴմϴ�. �������� Ȯ�����ּ���
                    	}
                    	
                    	if(isOk != -2){	// ������ 100���� �ƴϸ� �����Ҽ� ����. 
		                	// update tz_projgrp                   
		                    sql  = " update tz_projgrp set   																				\n";
		                    sql += " 	sdate = ?, edate = ?, luserid = ?,																	\n";
		                    sql += " 	ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'), grpseqnm = ?, projgubun = ?, reptype = ?,				\n";
		                    sql += "	grptotalscore = ?																					\n";
		                    sql += " where 1=1																								\n";
		                    sql += "   AND SUBJ = ?																							\n";
		                    sql += "   AND YEAR = ?																							\n";
		                    sql += "   AND SUBJSEQ = ? 																						\n";
		                    sql += "   AND GRPSEQ = ?  																						\n";
		                    pstmt = connMgr.prepareStatement(sql);
		                    pstmt.setString( 1, p_sdate);
		                    pstmt.setString( 2, p_edate);
		                    pstmt.setString( 3, p_luserid);
		                    pstmt.setString( 4, p_title);
		                    pstmt.setString( 5, p_projgubun);
		                    pstmt.setString( 6, p_reptype);
		                    pstmt.setInt   ( 7, totalscore);
		                    pstmt.setString( 8, p_subj);
		                    pstmt.setString( 9, p_year);
		                    pstmt.setString( 10, p_subjseq);
		                    pstmt.setInt   ( 11, p_grpseq);

		                    isOk = pstmt.executeUpdate();

		                    //tz_projmap �� ������ȣ�� �׷��ȣ�� ����
		                    //���� �������� ����� ���ο� ������ �ִ´�.
		                    sql2  = " delete from tz_projmap \n";
		                    sql2 += " where grpseq = ? ";
		                    sql2 += " and  subj = ? ";
		                    sql2 += " and  subjseq = ? ";
		                    sql2 += " and  year = ? ";
		                    pstmt2 = connMgr.prepareStatement(sql2);
		                    pstmt2.setInt( 1, p_grpseq);
		                    pstmt2.setString( 2, p_subj);
		                    pstmt2.setString( 3, p_subjseq);
		                    pstmt2.setString( 4, p_year);
		                    isOk2 = pstmt2.executeUpdate();
		                    if ( isOk2 > 0 ) { connMgr.commit(); }
		                    //for������ ������ȣ�� �׷��ȣ�� �־���Ѵ�.
		                    v_ordseq = p_ordseqs.split(",");
		                    for(int i = 0; i < v_ordseq.length; i++){
		  	                  sql1 =  "insert into TZ_projmap ";
		  	                  sql1 +=  "( subj, year, subjseq,      ";
		  	                  sql1 +=  "  grpseq, ordseq, luserid, ldate)   ";
		  	                  sql1 +=  " values ";
		  	                  sql1 +=  "(?,?,?, ";
		  	                  sql1 +=  " ?,?,?,? ) ";
		  	                  pstmt1 = connMgr.prepareStatement(sql1);
		  	
		  	                  pstmt1.setString( 1, p_subj);
		  	                  pstmt1.setString( 2, p_year);
		  	                  pstmt1.setString( 3, p_subjseq );
		  	                  pstmt1.setInt	  ( 4, p_grpseq);
		  	                  pstmt1.setInt   ( 5, Integer.parseInt(v_ordseq[i]));            
		  	                  pstmt1.setString( 6, p_luserid);
		  	                  pstmt1.setString( 7, FormatDate.getDate("yyyyMMddHHmmss"));
		  	                  isOk1 = pstmt1.executeUpdate();
		  	                if (pstmt1 != null) { try { pstmt1.close(); } catch (Exception e) {} }
		                    }
                    	}
                    //}
               }
               catch ( Exception ex ) { 
                   ErrorManager.getErrorStackTrace(ex);
                   throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
               }
               finally { 
            	   if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                   if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                   if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
                   if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
                   if ( isOk > 0 ) { connMgr.commit(); }
                   if ( isOk1 > 0 ) { connMgr.commit(); }else{connMgr.rollback();}
               }
               return isOk;
            }
            /**
            ���� ������ ����
            @param  connMgr
            **/  
            public int deletePaper(RequestBox box) throws Exception {  
            	DBConnectionManager connMgr   = null;
            	PreparedStatement   pstmt     = null;
                PreparedStatement   pstmt1    = null;
                PreparedStatement   pstmt2    = null;
                String              sql       = "";
                String              sql1      = "";
                String              sql2      = "";
                int 				isOk 	  = 0;
                int 				isOk1	  = 0;
                int 				isOk2	  = 0;
                int 				v_cnt	  = 0;
                
                ListSet ls        = null;
                String v_subj 			= box.getString("p_subj");
                String v_year 			= box.getString("p_year");
                String v_subjseq 		= box.getString("p_subjseq");
                String v_grpseq 		= box.getString("p_grpseq");

                try {
                	connMgr = new DBConnectionManager();
                    connMgr.setAutoCommit(false);                    
                    
                    sql = " SELECT COUNT(USERID) cnt		\n";
                    sql+= "   FROM TZ_PROJASSIGN 			\n";
                    sql+= " WHERE 1=1 						\n";
                    sql+= "   AND SUBJ = " + StringManager.makeSQL(v_subj);
                    sql+= "   AND GRPSEQ = " + v_grpseq;
                    ls = connMgr.executeQuery(sql);
                    
                    if ( ls.next() ) { 
                    	v_cnt = ls.getInt(1);
                    }
                    ls.close();
                    if ( v_cnt != 0) { 
                    	isOk = -1; // ������ �н��ڰ� �־� ������ �� �����ϴ�.
                    } else {
                    	sql1  = " delete from tz_projmap 		\n";
                    	sql1 += " where grpseq 	= ? 			\n";
                    	sql1 += " and  subj 		= ? 			\n";
                    	sql1 += " and  subjseq 	= ?		 		\n";
                    	sql1 += " and  year 		= ? 			\n";
                    	
                    	pstmt = connMgr.prepareStatement(sql1);
                    	pstmt.setString( 1, v_grpseq		);
                    	pstmt.setString( 2, v_subj			);
                    	pstmt.setString( 3, v_subjseq		);
                    	pstmt.setString( 4, v_year			);
                    	isOk1 = pstmt.executeUpdate();
                    	
                    	sql2  = " delete from tz_projgrp 		\n";
                    	sql2 += " where grpseq 	= ? 			\n";
                    	sql2 += " and  subj 		= ? 			\n";
                    	sql2 += " and  subjseq 	= ?		 		\n";
                    	sql2 += " and  year 		= ? 			\n";
                    	
                    	pstmt2 = connMgr.prepareStatement(sql2);
                    	pstmt2.setString( 1, v_grpseq		);
                    	pstmt2.setString( 2, v_subj			);
                    	pstmt2.setString( 3, v_subjseq		);
                    	pstmt2.setString( 4, v_year			);
                    	isOk2 = pstmt2.executeUpdate();
                    	
                    	isOk = isOk1 * isOk2;
                    }
                	// update tz_projgrp                   
                    /*sql  = " update tz_projgrp set   \n";
                    sql += " 	sdate = ? 						   ,	edate 		= ?,	luserid 	= ?		, 					\n";
                    sql += " 	ldate = to_char(sysdate, 'yyyyMMddHHmmss'), 	grpseqnm 	= ?, 	projgubun 	= ? , reptype = ?	\n";
                    sql += " where ";
                    sql += " 	grpseq = "+ p_grpseq;
                    pstmt.setString( 1, p_sdate);
                    pstmt.setString( 2, p_edate);
                    pstmt.setString( 3, p_luserid);
                    pstmt.setString( 4, p_title);
                    pstmt.setString( 5, p_projgubun);
                    pstmt.setString( 6, p_reptype);
                    
                    isOk = pstmt.executeUpdate();
                    
                    //tz_projmap �� ������ȣ�� �׷��ȣ�� ����
                    //���� �������� ����� ���ο� ������ �ִ´�.
                    sql2  = " delete from tz_projmap \n";
                    sql2 += " where grpseq = ? ";
                    sql2 += " and  subj = ? ";
                    sql2 += " and  subjseq = ? ";
                    sql2 += " and  year = ? ";
                    pstmt2 = connMgr.prepareStatement(sql2);
                    pstmt2.setInt( 1, p_grpseq);
                    pstmt2.setString( 2, p_subj);
                    pstmt2.setString( 3, p_subjseq);
                    pstmt2.setString( 4, p_year);
                    isOk2 = pstmt2.executeUpdate();
                    if ( isOk2 > 0 ) { connMgr.commit(); }
                    //for������ ������ȣ�� �׷��ȣ�� �־���Ѵ�.
                    String[] v_ordseq = p_ordseqs.split(",");
                    for(int i = 0; i < v_ordseq.length; i++){
  	                  sql1 =  "insert into TZ_projmap ";
  	                  sql1 +=  "( subj, year, subjseq,      ";
  	                  sql1 +=  "  grpseq, ordseq, luserid, ldate)   ";
  	                  sql1 +=  " values ";
  	                  sql1 +=  "(?,?,?, ";
  	                  sql1 +=  " ?,?,?,? ) ";
  	                  pstmt1 = connMgr.prepareStatement(sql1);
  	
  	                  pstmt1.setString( 1, p_subj);
  	                  pstmt1.setString( 2, p_year);
  	                  pstmt1.setString( 3, p_subjseq );
  	                  pstmt1.setInt	  ( 4, p_grpseq);
  	                  pstmt1.setInt   ( 5, Integer.parseInt(v_ordseq[i]));            
  	                  pstmt1.setString( 6, p_luserid);
  	                  pstmt1.setString( 7, FormatDate.getDate("yyyyMMddHHmmss"));
  	                  isOk1 = pstmt1.executeUpdate();
                    }*/
               }
               catch ( Exception ex ) { 
                   ErrorManager.getErrorStackTrace(ex);
                   throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
               }
               finally { 
            	   if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                   if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                   if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
                   if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
                   if ( isOk > 0 ) { connMgr.commit(); }
                   if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
                   
               }
               return isOk;
            }
            
}