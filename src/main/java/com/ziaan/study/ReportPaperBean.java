// **********************************************************
//  1. 제      목: PROJECT ADMIN BEAN
//  2. 프로그램명: ProjectAdminBean.java
//  3. 개      요: 과제 관리자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정: 2005. 11.24 이경배
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
     * 리포트출제관리 과정기수별 리포트 출제 현황 
     * @param box
     * @return
     * @throws Exception
     */
    /**
    과목별 과제 그룹 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectReportPaperPeriod(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          		= null;
        String sql          		= "";
        DataBox dbox   				= null;
        ArrayList list				= null;
        String  v_subj      		= box.getString("s_subjcourse");        // 과목
        String  v_year      		= box.getString("s_gyear");          	// 년도
        String  v_subjseq      		= box.getString("s_subjseq");         	// 과목기수
        
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
     * 과제 문제 등록전 과목 리스트 
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
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수                
        int     l           = 0;
        //String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        //String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        //String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
        
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        //String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_action   = box.getString("s_action");
        
        String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서
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
                sql1 += "        a.edustart, ";    // 교육기간 시작일
                sql1 += "        a.eduend, ";    // 교육기간 마감일                
                sql1 += "        (select classname from tz_subjatt WHERE upperclass=a.scupperclass and  middleclass = '000' and lowerclass = '000') classname, ";
                //sql1 += "        (select count(distinct projseq) from tz_projord where subj=a.subj and year=a.year and subjseq=a.subjseq) projseqcnt, ";
                //sql1 += "        (select count(ordseq) from tz_projord where subj=a.subj and year=a.year and subjseq=a.subjseq) ordseqcnt ";
                
                sql1 += "        0 projseqcnt, ";
                sql1 += "        0 ordseqcnt ";
                
                sql1 += "from    VZ_SCSUBJSEQ a where 1 = 1 ";
                if ( !ss_grcode.equals("ALL") ) { 		//교육그룹
                    sql1 += " and a.grcode = '" + ss_grcode + "'";
                } 
                if ( !ss_gyear.equals("ALL") ) { 		//년도
                    sql1 += " and a.gyear = '" + ss_gyear + "'";
                } 
                if ( !ss_grseq.equals("ALL") ) {		// 교육기수
                    sql1 += " and a.grseq = '" + ss_grseq + "'";
                }

                if ( !ss_uclass.equals("ALL") ) { 		//대분류
                    sql1 += " and a.scupperclass = '" + ss_uclass + "'";
                }
                
                if ( !ss_mclass.equals("ALL") ) { 		//중분류
                    sql1 += " and a.scmiddleclass = '" + ss_mclass + "'";
                }
                
                if ( !ss_lclass.equals("ALL") ) { 		//소분류
                    sql1 += " and a.sclowerclass = '" + ss_lclass + "'";
                }
                
                if ( !ss_subjcourse.equals("ALL") ) { 		//코스 KT는 안쓴다.
                    sql1 += " and a.scsubj = '" + ss_subjcourse + "'";
                }
                if ( !ss_subjseq.equals("ALL") ) { 			//과정기수
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

	                if ( !ss_uclass.equals("ALL") ) { 		//대분류
	                    sql1 += " and a.upperclass = '" + ss_uclass + "'";
	                }
	                
	                if ( !ss_mclass.equals("ALL") ) { 		//중분류
	                    sql1 += " and a.middleclass = '" + ss_mclass + "'";
	                }
	                
	                if ( !ss_lclass.equals("ALL") ) { 		//소분류
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
    과제 문제 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectReportQuestionsList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        ReportData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // sql += "(select count(*) from TZ_PROJGRP where subj=A.subj and year=A.year "; 11/26 tz_projgrp 용도 알아낼 것
            // sql += "and subjseq=A.subjseq and ordseq=A.ordseq) as grcnt,A.groupcnt ";
            /*
             * 
             * 2008.11.19 과목별 과제 출제된 리스트를 가져와야 해서 주석처리 다시 쿼리 만들어야함.
             * 제길..아..과제 만들기 조낸 싫어..시러시러시러시러시러시러시러시러시럿
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
            sql += "         A.upfile2, decode(A.useyn, 'Y', '사용', '미사용')  useyn,                                                                       ";
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
            sql += "         A.upfile2, decode(A.useyn, 'Y', '사용', '미사용')  useyn \n";
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
     과제 등록
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
         String v_subj               = box.getString("p_subj");          // 과목
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
         String v_ansyn              = box.getString("ansyn");     // 답안제출옵션
         String v_useyn              = box.getString("useyn");     // 사용여부          
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
/* 2008.11.19 오충현 수정 
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
      과제 수정 페이지
      @param box      receive from the form object and session
      @return ProjectData   
      */
       public ReportData updateReportQuestionsPage(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          ListSet ls          = null;
          String sql          = "";
          ReportData data   = null;
          String  v_subj      = box.getString("p_subj");          // 과목
          //String  v_year      = box.getString("p_year");          // 년도
          //String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
          //String  v_lesson     = box.getString("p_lesson");         // 일차
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
       과제 수정
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
           String v_check1             = box.getString("p_check1");    // 첨부파일1 이전파일 삭제
           String v_check2             = box.getString("p_check2");    // 첨부파일2 아전파일 삭제
           
           String v_subj               = box.getString("p_subj");          // 과목
           
           String v_upfilesize         = "";
           String v_upfilesize2        = "";
           String v_ansyn              = box.getString("ansyn");     // 답안제출옵션
           String v_useyn              = box.getString("useyn");     // 사용여부   
                                            
           int    v_score              = box.getInt("p_score");        
           //int    v_groupcnt           = box.getInt("p_groupcnt");
           int    v_ordseq             = box.getInt("p_ordseq"); 
           if ( v_newFileName1.length() == 0) {   v_newFileName1 = v_upfile;   }
           if ( v_newFileName2.length() == 0) {   v_newFileName2 = v_upfile2;   }
           
           // 기존 파일정보
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
                   //    isOk = -1; // 배정된 학습자가 있어 삭제할 수 없습니다.
                   //    
                   // } else { 
                   	
                   	                        
   				            // 업로드한 파일이 없을 경우
   				            if ( v_realFileName1.equals("") ) { 
   				                // 기존파일 삭제
   				                if ( v_check1.equals("Y") ) { 
   				                	FileManager.deleteFile(v_newFileName1);
   				                    v_newFileName1   = "";
   				                    v_realFileName1  = "";
   				                } else { 
   				                // 기존파일 유지
   				                    v_newFileName1   = v_oldupfile;
   				                    v_realFileName1  = v_oldrealfile;
   				                }
   				            }
   				            
   				            // 업로드한 파일이 없을 경우
   				            if ( v_realFileName2.equals("") ) { 
   				                // 기존파일 삭제
   				                if ( v_check2.equals("Y") ) { 
   				                	FileManager.deleteFile(v_newFileName2);
   				                    v_newFileName2   = "";
   				                    v_realFileName2  = "";
   				                } else { 
   				                // 기존파일 유지
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
        과제 삭제
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
            String  v_subj      = box.getString("p_subj");          // 과목
            String  v_year      = box.getString("p_year");          // 년도
            String  v_subjseq   = box.getString("p_subjseq");       // 과목기수        
            int     v_ordseq    = box.getInt("p_ordseq");           // 과제번호  


            try { 
                connMgr = new DBConnectionManager();

                
                    
                    sql1 = "select  count(userid)  ";
                    sql1 += "from    TZ_PROJASSIGN ";
                    sql1 += "where   subj='" + v_subj + "' and year='" + v_year + "' and ";
                    sql1 += "        subjseq='" + v_subjseq + "' and ordseq = " + v_ordseq ;

                    ls = connMgr.executeQuery(sql1);  
                
                    if ( ls.next() ) { 
                        if ( ls.getInt(1) != 0) { 
                            isOk = -1; // 배정된 학습자가 있어 삭제할 수 없습니다.
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
         과제문제 리스트 (문제를 문제지에 등록시키기 위해 문제 정보 select)
         @param box      receive from the form object and session
         @return ProjectData   
         */
          public ArrayList selectQuestionList(RequestBox box) throws Exception { 
             DBConnectionManager connMgr	= null;
             ListSet ls          			= null;
             String sql          			= "";
             ArrayList list   				= null;
             DataBox dbox    				= null;
             String  v_subj      			= box.getString("p_subj");          // 과목
             String  v_year      			= box.getString("p_year");          // 년도
             String  v_subjseq   			= box.getString("p_subjseq");       // 과목기수
             
             try { 
            	 connMgr = new DBConnectionManager();

                 // select reptype,expiredate,title,contents,score,groupcnt,upfile,upfile2
                 //2008.12.03 테이블변경으로 수정
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
          설문 문제지 등록
          @param box      receive from the form object and session
          @return isOk    
          **/  
          public int insertPaper(RequestBox box) throws Exception { 
              DBConnectionManager	connMgr	= null;
              int isOk = 0;

              //String v_grcode       = box.getString("p_grcode");
              String v_year         = box.getString("p_year");	//년도
              String v_subj         = box.getString("p_subj");	//과목
              String v_subjseq      = box.getString("p_subjseq");//학기
              String v_sulnums      = box.getString("p_sulnums");	//설문은 1,2,3이런형식으로 설문 문제 번호가 넘어온다. 평가에서는 다름 수정해야함...
              String v_title		= box.getString("p_title");
              int    v_reportpapernum  = 0;
              String v_projrepstart = box.getString("p_sdate");	//제출시작일
              String v_projrepend   = box.getString("p_edate");	//제출종료일
       
              String v_luserid   	= box.getSession("userid");		//최종수정자
              
              String v_projgubun 	= box.getString("p_projgubun");	//리포트타입(중간:M, 기말:F)
              String v_reptype 		= box.getString("p_reptype");	//리포트유형(단답형:S, 서술형: P)
              try { 

                  connMgr = new DBConnectionManager();
                  connMgr.setAutoCommit(false);

                  v_reportpapernum = getPapernumSeq(v_subj, v_year);	//그룹번호 grpseq

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
          과제 문제지 등록
          @param  connMgr
          @param  p_year       년도
          @param  p_subj       과목
          @param  p_grpseq     문제지번호
          @param  p_ordseqs    문제번호
          @param  p_luserid    작성자               
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
	                  //tz_projmap 에 과제번호와 그룹번호를 맵핑
	                  //for돌려서 문제번호당 그룹번호를 넣어야한다.
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
          과목별 과제 그룹 리스트
          @param box      receive from the form object and session
          @return ArrayList
          */
           public ArrayList selectReportGroupList(RequestBox box) throws Exception { 
              DBConnectionManager	connMgr	= null;
              ListSet ls          		= null;
              ArrayList list      		= null;
              String sql          		= "";
              DataBox dbox   			= null;
              String  v_subj      		= box.getString("s_subjcourse");        // 과목
              String  v_year      		= box.getString("s_gyear");          	// 년도
              String  v_subjseq      	= box.getString("s_subjseq");         	// 과목기수
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
           과목별 과제 그룹 리스트
           @param box      receive from the form object and session
           @return ArrayList
           */
            public ArrayList selectPaperQuestionList(RequestBox box) throws Exception { 
               DBConnectionManager	connMgr	= null;
               ListSet ls          		= null;
               ArrayList list      		= null;
               String sql          		= "";
               DataBox dbox   			= null;
               //String  v_subj      		= box.getString("s_subjcourse");        // 과목
               //String  v_year      		= box.getString("s_gyear");          	// 년도
               //String  v_subjseq      	= box.getString("s_subjseq");         	// 과목기수
               String  v_subj      		= box.getString("p_subj");        		// 과목
               String  v_year      		= box.getString("p_year");          	// 년도
               String  v_subjseq      	= box.getString("p_subjseq");         	// 과목기수
               String  v_grpseq      	= box.getString("p_grpseq");         	// 그룹번호
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
            설문 문제지 등록
            @param box      receive from the form object and session
            @return isOk    
            **/  
            public int updatePaper(RequestBox box) throws Exception { 
                DBConnectionManager	connMgr	= null;
                int isOk = 0;
                
                String v_year         = box.getString("p_year");		//년도
                String v_subj         = box.getString("p_subj");		//과목
                String v_subjseq      = box.getString("p_subjseq");		//학기
                String v_sulnums      = box.getString("p_sulnums");		//설문번호다. 과제에서는 안쓴다. 지우기싫다..귀찮다.이렇게라도 써주니 고맙지?건들지마 주겨버린다
                String v_title		= box.getString("p_title");
                int    v_reportpapernum  = 0;
                String v_projrepstart = box.getString("p_sdate");		//제출시작일
                String v_projrepend   = box.getString("p_edate");		//제출종료일
         
                String v_luserid   = box.getSession("userid");			//최종수정자
                
                String v_projgubun = box.getString("p_projgubun");		//리포트유형(중간,기말)
                String v_reptype = box.getString("p_reptype");			//리포트타입(단답형:S, 서술형:P)
                
                int v_grpseq		= box.getInt("p_grpseq");			//문제지 번호
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
            과제 문제지 수정
            @param  connMgr
            @param  p_year       년도
            @param  p_subj       과목
            @param  p_grpseq     문제지번호
            @param  p_ordseqs    문제번호
            @param  p_luserid    작성자               
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
                	// 수정 하기 전 해당 문제지의 배정 여부를 확인하고 배정된 문제지면 수정,삭제가 불가능하다.
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
                    
                    /* 일단 주석해제 나중에 다시 풀어야함 10.01.18 kjm */
                	//if ( v_cnt != 0) { 
                    	//isOk = -1; // 배정된 학습자가 있어 수정 할 수 없습니다.
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
                    		isOk = -2; // 총점이 100점이 아닙니다. 문제지를 확인해주세요
                    	}
                    	
                    	if(isOk != -2){	// 총점이 100점이 아니면 수정할수 없다. 
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

		                    //tz_projmap 에 과제번호와 그룹번호를 맵핑
		                    //이전 문제들을 지우고 새로운 문제를 넣는다.
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
		                    //for돌려서 문제번호당 그룹번호를 넣어야한다.
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
            과제 문제지 삭제
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
                    	isOk = -1; // 배정된 학습자가 있어 삭제할 수 없습니다.
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
                    
                    //tz_projmap 에 과제번호와 그룹번호를 맵핑
                    //이전 문제들을 지우고 새로운 문제를 넣는다.
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
                    //for돌려서 문제번호당 그룹번호를 넣어야한다.
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