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
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.CalcUtil;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.FreeMailBean;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class ReportAdminBean { 

    public ReportAdminBean() { }
    /**
     * 
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
    과제 출제리스트
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
                    data = new ReportData();
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
            //2008.12.2 테이블을 지금 바꾸게 만드냐
            
            sql = "select   A.subj,												\n";
            sql += "         A.ordseq,											\n";
            sql += "         A.title,											\n";
            sql += "         A.score,											\n";
            sql += "         A.upfile2,											\n";
            sql += "         decode(A.useyn, 'Y', '사용', '미사용') as useyn		\n";
            sql += "from     TZ_PROJORD A 										\n";
            sql += "where    A.subj='" +v_subj + "'  							\n";
            sql += "order by A.ordseq	 										\n";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new ReportData();
                data.setSubj( ls.getString("subj") );
                data.setOrdseq( ls.getInt("ordseq") );
                data.setTitle( ls.getString("title") );
                data.setScore( ls.getInt("score") );
                data.setUseyn( ls.getString("useyn") );
                data.setUpfile2( ls.getString("upfile2") ); 
                //data.setReptype( ls.getString("reptype") );
                //data.setProjseq( ls.getInt("projseq") );
                //data.setLesson( ls.getString("lesson") );
                //data.setLessonnm( ls.getString("lessonnm") );
                //data.setIsopen( ls.getString("isopen") );
                //data.setIsopenscore( ls.getString("isopenscore") );
                //data.setTocnt( ls.getString("tocnt") );     
                //data.setGrcnt( ls.getString("grcnt") ); 
                //data.setExpiredate( ls.getString("expiredate") ); 
                //data.setGroupcnt( ls.getString("groupcnt") ); 
                //data.setRowspan( ls.getInt("rowspan") ); 
                //data.setIsusedcopy( ls.getString("isusedcopy") ); 
                //data.setRowspanseq( ls.getInt("rowspanseq") );

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
         //int    v_groupcnt           = box.getInt("p_groupcnt");
         //String v_ansyn              = box.getString("ansyn");     // 답안제출옵션
         String v_useyn              = box.getString("useyn");     // 사용여부          
         int    v_max                = 0;
         int    v_ordseq             = 0;
         int 	index 				 = 1;
                 
         try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
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
/* 2008.12.03 수정            
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
  */           
             sql2  = "insert into TZ_PROJORD( ";
             sql2 += "					subj, ordseq, title, contents, score,		";	//5
             sql2 += " 					upfile, upfile2, realfile, realfile2,		";	//4 
             sql2 += " 					luserid, ldate, useyn 						";	//3
             sql2 += " 					) 											";
             sql2 += " values(";
             sql2 += " 			?,?,?,?,?, 											";	//5
             sql2 += " 			?,?,?,?, 											";	//4
             sql2 += " 			?,to_char(sysdate,'YYYYMMDDHH24MISS'),? 			";	//3->2
             sql2 += " )";
             
             pstmt2 = connMgr.prepareStatement(sql2);

             pstmt2.setString(index++,	v_subj		);//1
             pstmt2.setInt	 (index++,	v_ordseq	);
             pstmt2.setString(index++,v_title		);
             pstmt2.setString(index++,v_contents	);//4
             pstmt2.setString(index++,v_score		);
             pstmt2.setString(index++,v_newFileName1);
             pstmt2.setString(index++,v_newFileName2);
             pstmt2.setString(index++,v_realFileName1);
             pstmt2.setString(index++,v_realFileName2);//9
             pstmt2.setString(index++,v_user_id		);
             pstmt2.setString(index++,v_useyn		);//11

             //pstmt2.setString(index++,v_reptype);
             //pstmt2.setInt(index++,v_groupcnt);
             //pstmt2.setString(index++,v_ansyn);
             
             isOk = pstmt2.executeUpdate();
            
             if (isOk > 0) {
            	 connMgr.commit();
             }
         } catch ( Exception ex ) {                        
             if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
             throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
             if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }
      
      /**
      과제 수정 페이지
      @param box      receive from the form object and session
      @return ReportData   
      */
       public ReportData updateReportQuestionsPage(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          ListSet ls          = null;
          String sql          = "";
          ReportData data   = null;
          String  v_subj      = box.getString("p_subj");          // 과목
          int     v_ordseq    = box.getInt("p_ordseq");        
          
          try { 
              connMgr = new DBConnectionManager();

              // select reptype,expiredate,title,contents,score,groupcnt,upfile,upfile2
              sql = "select ordseq,title,contents,score,upfile,upfile2,realfile,realfile2 ";
              sql += "       ,useyn ";            
              sql += "from TZ_PROJORD ";
              sql += "where subj='" +v_subj + "' ";
              sql += "and ordseq='" +v_ordseq + "'";

              ls = connMgr.executeQuery(sql);

              if ( ls.next() ) { 
                  data = new ReportData();
                  
                  data.setOrdseq	( ls.getInt		("ordseq") 	);
                  data.setTitle		( ls.getString	("title") 	);
                  data.setContents	( ls.getString	("contents"));
                  data.setScore		( ls.getInt		("score") 	);
                  data.setUpfile	( ls.getString	("upfile") 	);
                  data.setUpfile2	( ls.getString	("upfile2") );
                  data.setRealfile	( ls.getString	("realfile"));
                  data.setRealfile2	( ls.getString	("realfile2"));
                  data.setUseyn		( ls.getString	("useyn") 	);
                  
                  //data.setReptype( ls.getString("reptype") );
                  //data.setProjseq( ls.getInt("projseq") );
                  //data.setAnsyn( ls.getString("ansyn") );
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
           
           //String v_reptype            = box.getString("p_reptype");
           
           //String v_upfilesize         = "";
           //String v_upfilesize2        = "";
           //String v_ansyn              = box.getString("ansyn");     // 답안제출옵션
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
               connMgr.setAutoCommit(false);
               
               sql = "select  count(grpseq)  ";
               sql += "from   TZ_PROJMAP ";
               sql += "where   subj='" + v_subj + "' ";
               sql += "  and   ordseq = " + v_ordseq ;

               ls = connMgr.executeQuery(sql);                  
               if ( ls.next() ) { 
                    //if ( ls.getInt(1) != 0 && (box.getString("p_ischanged").equals("1") || box.getString("p_isusechanged").equals("0"))) { 
                       //isOk = -1; // 배정된 리포트문제지가 있어 수정할수 없습니다.
                       
                    //} else { 
                   	
                   	                        
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
   				
			            //2008.12.03 수정 테이블 변경으로인해...
			            sql  = "update TZ_PROJORD set title=?, contents=?, score=?,               	";//3
			            sql += "	upfile=?, upfile2=?, realfile=?, realfile2=?, 					";//4
			            sql += "	luserid=?, ldate=to_char(sysdate,'YYYYMMDDHH24MISS'), useyn=? 	";//2  ? 총 9개
			            sql += "where subj='" +v_subj + "' ";
			            sql += "and ordseq='" +v_ordseq + "' ";
			            
			            pstmt = connMgr.prepareStatement(sql);
			           // System.out.println("sql ==  ==  ==  ==  == > " +sql);
			            
			            //pstmt.setString(index++,v_reptype);//1
			            pstmt.setString(index++,v_title			);//1
			            pstmt.setString(index++,v_contents		);
			            pstmt.setInt   (index++,v_score			);
			            pstmt.setString(index++,v_newFileName1	);//4
			            pstmt.setString(index++,v_newFileName2	);
			            pstmt.setString(index++,v_realFileName1	);
			            pstmt.setString(index++,v_realFileName2	);
			            pstmt.setString(index++,v_user_id		);
			            pstmt.setString(index++,v_useyn			);//9
			            
			            //pstmt.setString(index++,v_isopen);
			            //pstmt.setString(index++,v_isopenscore);
			            //pstmt.setString(index++,v_ansyn);
			            isOk = pstmt.executeUpdate();
           
                    //}
               }
               if (isOk > 0) {
              	 connMgr.commit();
               }
           } catch ( Exception ex ) {                        
               if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
           } finally { 
           	  if ( ls != null )  { try { ls.close(); } catch ( Exception e ) { } }
               if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
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
            int     v_ordseq    = box.getInt("p_ordseq");           // 과제번호  

            //String  v_year      = box.getString("p_year");          // 년도
            //String  v_subjseq   = box.getString("p_subjseq");       // 과목기수        


            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);
                
                /**
                 * 2008.12.03 오충현 수정 각 문제는 과목별 문제 이다. 문제지에 들어있으면 삭제를 하면 안된다.  
                sql1 = "select  count(userid)  ";
                sql1 += "from    TZ_PROJASSIGN ";
                sql1 += "where   subj='" + v_subj + "' and year='" + v_year + "' and ";
                sql1 += "        subjseq='" + v_subjseq + "' and ordseq = " + v_ordseq ;
                */
                // 문제지는 과목,년도,학기,문제지번호가 key 이다.
                sql1  = " select  count(ordseq) 								\n";
                sql1 += " from TZ_PROJMAP 										\n";
                sql1 += " where   subj='" + v_subj + "' and ordseq = " + v_ordseq;
                
                ls = connMgr.executeQuery(sql1);  
            
                if ( ls.next() ) { 
                    if ( ls.getInt(1) != 0) { 
                        isOk = -1; // 배정된 문제지가 있어 삭제할 수 없습니다.
                    } else { 
                        // Delete tz_projrep
                        sql2 = "delete  TZ_PROJORD ";
                        sql2 += "where   subj = ? and ordseq = ? ";
                        pstmt2 = connMgr.prepareStatement(sql2);
            
                        pstmt2.setString(1, v_subj		);
                        pstmt2.setInt	(2, v_ordseq	);
                        
//                            pstmt2.setString(2, v_year);
//                            pstmt2.setString(3, v_subjseq);
                        
                        isOk = pstmt2.executeUpdate();
                    }
                }
                if (isOk > 0) {
                	connMgr.commit();
                }

            } catch ( Exception ex ) { 
                if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
                ErrorManager.getErrorStackTrace(ex, box, sql2);
                throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
            } finally { 
                if ( ls != null )  { try { ls.close(); } catch ( Exception e1 ) { } }     
                if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }     
                if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }

            return isOk;
        }
         /**
          * 과제 제출 개수
          * @param box
          * @return
          * @throws Exception
          */
         public int getAdminData(RequestBox box) throws Exception { 

             DBConnectionManager	connMgr	= null;
             String v_subj = box.getString("p_subj");
             String v_year = box.getString("p_year");
             String v_subjseq = box.getString("p_subjseq");
             
             int v_result = 0;
             ListSet             ls      = null;
             String sql  = "";
             DataBox             dbox    = null;

             try { 
                 connMgr = new DBConnectionManager();
                 // sql = "select nvl(max(projseq),0) projseq ";

//     			sql = " select nvl(count(ordseq),0) projseq  ";
//                 sql += "  from tz_projord  ";
//                 sql += " where subj = " + SQLString.Format(v_subj);
//                 sql += " and year = " + SQLString.Format(v_year);
//                 sql += " and subjseq = " + SQLString.Format(v_subjseq);
//     			sql += " group by projseq ";
     			
//     			System.out.println("sql_projord == = >>  >>  > " +sql);
                 
                 
                 sql  = " select nvl(count(grpseq),0) grpseq \n";
                 sql += "  from tz_projgrp  ";
                 sql += " where subj = " + SQLString.Format(v_subj);
                 sql += " and year = " + SQLString.Format(v_year);
                 sql += " and subjseq = " + SQLString.Format(v_subjseq);
                 sql += " group by grpseq ";
                 
                 ls = connMgr.executeQuery(sql);

                 if ( ls.next() ) { 
                     dbox = ls.getDataBox();
                     v_result = dbox.getInt("d_projseq");
                 }
             
             } catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex);
                 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
             } finally { 
                 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             }

             return v_result;
         }
         /**
         과제평가관리 리스트
         @param box      receive from the form object and session
         @return ArrayList   
         */
          public ArrayList selectReportSubmitAList(RequestBox box) throws Exception { 
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
             String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
             String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
             String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수

             String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
             String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과목분류
             String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
             
             String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
             String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
             String  ss_action   = box.getString("s_action");
             
             String v_userid = box.getSession("userid");
             String s_gadmin = box.getSession("gadmin");
             String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
             
             String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
             String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서
             
             try { 
                 if ( ss_action.equals("go") ) {  
                     connMgr = new DBConnectionManager();
                     list1 = new ArrayList();
                     list2 = new ArrayList();
                         
                     if ( v_gadmin.equals("P") ) {      //          강사권한 경우 해당 클래스의 학습자 리스트만 
                         sql1 = "select a.course,a.cyear,a.courseseq,a.coursenm,a.subj,a.year,a.subjseq,a.subjseqgr,a.isclosed,a.subjnm,a.isonoff, a.edustart, a.eduend, ";
                         sql1 += "        (select count(*) from TZ_PROJASSIGN where subj=A.subj and year=A.year ";
                         sql1 += "        and subjseq=A.subjseq  and uprepfile is not null and userid in (select userid from tz_student where subj = b.subj and year = b.year and subjseq = b.subjseq and class = b.class)) as pjcnt, ";
                         sql1 += "        (select count(*) from TZ_PROJASSIGN where subj=A.subj and year=A.year ";
                         sql1 += "        and subjseq=A.subjseq and totalscore is null and uprepfile is not null and userid in (select userid from tz_student where subj = b.subj and year = b.year and subjseq = b.subjseq and class = b.class)) as micnt ";
                         sql1 += " from   VZ_SCSUBJSEQ  A ,";
//                         sql1 += "        (select subj ";
//                         sql1 += "         from   tz_subjman ";
//                         sql1 += "         where  substr(gadmin,1,1) = 'P' and userid = '" +v_userid + "' ) B ";
//                         sql1 += " where  a.subj = b.subj  ";
                         sql1 += "        (select subj,year,subjseq,class ";
                         sql1 += "         from   tz_classtutor ";
                         sql1 += "         where  tuserid = '" +v_userid + "' ) B ";
                         sql1 += " where  a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq ";
                     } else { 
                         sql1 = "select a.course,a.cyear,a.courseseq,a.coursenm,a.subj,a.year,a.subjseq,a.subjseqgr,a.isclosed,a.subjnm,a.isonoff, a.edustart, a.eduend, ";
                         sql1 += "        (select count(*) from TZ_PROJASSIGN where subj=A.subj and year=A.year ";
                         sql1 += "        and subjseq=A.subjseq and uprepfile is not null) as pjcnt, ";
                         sql1 += "        (select count(*) from TZ_PROJASSIGN where subj=A.subj and year=A.year ";
                         sql1 += "        and subjseq=A.subjseq and totalscore is null and uprepfile is not null) as micnt ";
                         sql1 += " from VZ_SCSUBJSEQ  A where 1=1 ";
                         
                     }
                     
                     
                     
                     if ( !ss_grcode.equals("ALL") ) { 
                         sql1 += " and a.grcode = '" + ss_grcode + "'";
                     } 
                     if ( !ss_gyear.equals("ALL") ) { 
                         sql1 += " and a.gyear = '" + ss_gyear + "'";
                     } 
                     if ( !ss_grseq.equals("ALL") ) { 
                         sql1 += " and a.grseq = '" + ss_grseq + "'";
                     }
                     
                     if ( !ss_uclass.equals("ALL") ) { 
                         sql1 += " and a.scupperclass = '" + ss_uclass + "'";
                     }
                     if ( !ss_mclass.equals("ALL") ) { 
                         sql1 += " and a.scmiddleclass = '" + ss_mclass + "'";
                     }
                     if ( !ss_lclass.equals("ALL") ) { 
                         sql1 += " and a.sclowerclass = '" + ss_lclass + "'";
                     }

                     if ( !(ss_subjcourse.equals("ALL") || ss_subjcourse.equals("----"))) { 
                         sql1 += " and a.scsubj = '" + ss_subjcourse + "'";
                     }
                     if ( !ss_subjseq.equals("ALL") ) { 
                         sql1 += " and a.scsubjseq = '" + ss_subjseq + "'";
                     }
                     
                     
                     if ( v_orderColumn.equals("") ) { 
                         sql1 += " order by a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq ";
                     } else { 
                         sql1 += " order by " + v_orderColumn + v_orderType;
                     }
                     
                     ls1 = connMgr.executeQuery(sql1);
                     
                     while ( ls1.next() ) { 
                         data1 = new ReportData();                    
                         data1.setCourse( ls1.getString("course") );
                         data1.setCyear( ls1.getString("cyear") );
                         data1.setCourseseq( ls1.getString("courseseq") );
                         data1.setCoursenm( ls1.getString("coursenm") );                      
                         data1.setSubj( ls1.getString("subj") );    
                         data1.setYear( ls1.getString("year") );              
                         data1.setSubjseq( ls1.getString("subjseq") );
                         data1.setSubjseqgr( ls1.getString("subjseqgr") );
                         data1.setIsclosed( ls1.getString("isclosed") );
                         data1.setSubjnm( ls1.getString("subjnm") );                    
                         data1.setIsonoff( ls1.getString("isonoff") );
                         data1.setEdustart( ls1.getString("edustart") );                             
                         data1.setEduend( ls1.getString("eduend") );         
                         data1.setPjcnt( ls1.getString("pjcnt") );  
                         data1.setMicnt( ls1.getString("micnt") );  
                         data1.setMicnt( ls1.getString("micnt") );
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
                             if ( !ss_uclass.equals("ALL") ) { 
                                 sql2 += " and sclowerclass = '" + ss_lclass + "'";
                             }

                             if ( !ss_subjcourse.equals("ALL") ) { 
                                 sql2 += " and scsubj = '" + ss_subjcourse + "'";
                             }
                             if ( !ss_subjseq.equals("ALL") ) { 
                                 sql2 += " and scsubjseq = '" + ss_subjseq + "'";
                             }
//                                 System.out.println("sql2 ==  ==  ==  ==  ==  == > " +sql2);
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
                     }                
                 }
             } catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex, box, sql1);
                 throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
             } finally { 
                 if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
                 if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             }

             return list2;        
         }
          /**
          과제 평가리스트
          @param box      receive from the form object and session
          @return ArrayList   
          */
           public ArrayList selectReportSubmitList(RequestBox box) throws Exception { 
              DBConnectionManager	connMgr	= null;
              ListSet ls          = null;
              ArrayList list      = null;
              String sql          = "";
              ReportData data   = null;
              String  v_subj      = box.getString("p_subj");          // 과목
              String  v_year      = box.getString("p_year");          // 년도
              String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
              String v_userid = box.getSession("userid");
              String s_gadmin = box.getSession("gadmin");
              String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
              
              try { 
                  connMgr = new DBConnectionManager();
                  list = new ArrayList();

                  if ( v_gadmin.equals("P") ) {      //          강사권한 경우 해당 클래스의 학습자 리스트만 
                	  	sql = "\n select a.grpseq "
                		    + "\n      , a.grpseqnm title "
                		    + "\n      , a.reptype "
                		    + "\n      , ( "
                		    + "\n         select count(*) "
                		    + "\n         from   tz_projassign "
                		    + "\n         where  subj = a.subj "
                		    + "\n         and    year = a.year "
                		    + "\n         and    subjseq = a.subjseq "
                		    + "\n         and    grpseq = a.grpseq "
                		    + "\n         and    userid in ( "
                		    + "\n                           select userid "
                		    + "\n                           from   tz_student "
                		    + "\n                           where  subj = b.subj "
                		    + "\n                           and    year = b.year "
                		    + "\n                           and    subjseq = b.subjseq "
                		    + "\n                           and    class = b.class "
                		    + "\n                          ) "
                		    + "\n        ) as assigncnt "
                		    + "\n      , ( "
                		    + "\n         select count(*) "
                		    + "\n         from   tz_projassign "
                		    + "\n         where  subj = a.subj "
                		    + "\n         and    year = a.year "
                		    + "\n         and    subjseq = a.subjseq "
                		    + "\n         and    grpseq = a.grpseq "
                		    + "\n         and    repdate is not null "
                		    + "\n         and    userid in ( "
                		    + "\n                           select userid "
                		    + "\n                           from   tz_student "
                		    + "\n                           where  subj = b.subj "
                		    + "\n                           and    year = b.year "
                		    + "\n                           and    subjseq = b.subjseq "
                		    + "\n                           and    class = b.class "
                		    + "\n                          ) "
                		    + "\n        ) as jccnt  "
                		    + "\n      , ( "
                		    + "\n         select count(*) "
                		    + "\n         from   tz_projassign "
                		    + "\n         where  subj = a.subj "
                		    + "\n         and    year = a.year "
                		    + "\n         and    subjseq = a.subjseq "
                		    + "\n         and    grpseq = a.grpseq "
                		    + "\n         and    totalscore is not null "
                		    + "\n         and    userid in ( "
                		    + "\n                           select userid "
                		    + "\n                           from   tz_student "
                		    + "\n                           where  subj = b.subj "
                		    + "\n                           and    year = b.year "
                		    + "\n                           and    subjseq = b.subjseq "
                		    + "\n                           and    class = b.class "
                		    + "\n                          ) "
                		    + "\n        ) as pgcnt "
                		    + "\n      , ( "
                		    + "\n         select count(*) "
                		    + "\n         from   tz_projassign "
                		    + "\n         where  subj = a.subj "
                		    + "\n         and    year = a.year "
                		    + "\n         and    subjseq = a.subjseq "
                		    + "\n         and    grpseq = a.grpseq "
                		    + "\n         and    uprepfile is not null "
                		    + "\n         and    totalscore is null "
                		    + "\n         and    userid in ( "
                		    + "\n                           select userid "
                		    + "\n                           from   tz_student "
                		    + "\n                           where  subj = b.subj "
                		    + "\n                           and    year = b.year "
                		    + "\n                           and    subjseq = b.subjseq "
                		    + "\n                           and    class = b.class "
                		    + "\n                          ) "
                		    + "\n        ) as micnt "
                		    + "\n from   tz_projgrp a "
                		    + "\n      , ( "
                		    + "\n         select subj, year, subjseq, class "
                		    + "\n         from   tz_classtutor "
                		    + "\n         where  subj = " + StringManager.makeSQL(v_subj)
                		    + "\n         and    year = " + StringManager.makeSQL(v_year)
                		    + "\n         and    subjseq = " + StringManager.makeSQL(v_subjseq)
                		    + "\n         and    tuserid = " + StringManager.makeSQL(v_userid)
                		    + "\n        ) b "
                		    + "\n where  a.subj = " + StringManager.makeSQL(v_subj)
                		    + "\n and    a.year = " + StringManager.makeSQL(v_year)
                		    + "\n and    a.subjseq = " + StringManager.makeSQL(v_subjseq)
                		    + "\n and    a.subj = b.subj "
                		    + "\n and    a.year = b.year "
                		    + "\n and    a.subjseq = b.subjseq "
                		    + "\n order  by a.grpseq ";
                      
                  } else { 
                	  	sql = "\n select a.grpseq "
	            		    + "\n      , a.grpseqnm title "
	            		    + "\n      , a.reptype "
	            		    + "\n      , ( "
	            		    + "\n         select count(*) "
	            		    + "\n         from   tz_projassign "
	            		    + "\n         where  subj = a.subj "
	            		    + "\n         and    year = a.year "
	            		    + "\n         and    subjseq = a.subjseq "
	            		    + "\n         and    grpseq = a.grpseq "
	            		    + "\n        ) as assigncnt /* 배정인원 */"
	            		    + "\n      , ( "
	            		    + "\n         select count(*) "
	            		    + "\n         from   tz_projassign "
	            		    + "\n         where  subj = a.subj "
	            		    + "\n         and    year = a.year "
	            		    + "\n         and    subjseq = a.subjseq "
	            		    + "\n         and    grpseq = a.grpseq "
	            		    + "\n         and    repdate is not null "
	            		    + "\n        ) as jccnt  /* 제출인원 */"
	            		    + "\n      , ( "
	            		    + "\n         select count(*) "
	            		    + "\n         from   tz_projassign "
	            		    + "\n         where  subj = a.subj "
	            		    + "\n         and    year = a.year "
	            		    + "\n         and    subjseq = a.subjseq "
	            		    + "\n         and    grpseq = a.grpseq "
	            		    + "\n         and    totalscore is not null "
	            		    + "\n        ) as pgcnt /* 채점인원 */"
	            		    + "\n      , ( "
	            		    + "\n         select count(*) "
	            		    + "\n         from   tz_projassign "
	            		    + "\n         where  subj = a.subj "
	            		    + "\n         and    year = a.year "
	            		    + "\n         and    subjseq = a.subjseq "
	            		    + "\n         and    grpseq = a.grpseq "
	            		    + "\n         and    uprepfile is not null "
	            		    + "\n         and    totalscore is null "
	            		    + "\n        ) as micnt /* 미채점인원 */"
	            		    + "\n from   tz_projgrp a "
	            		    + "\n where  a.subj = " + StringManager.makeSQL(v_subj)
	            		    + "\n and    a.year = " + StringManager.makeSQL(v_year)
	            		    + "\n and    a.subjseq = " + StringManager.makeSQL(v_subjseq)
	            		    + "\n order  by a.grpseq ";
                  }
                  
                  ls = connMgr.executeQuery(sql);

                  while ( ls.next() ) { 
                      data = new ReportData();
                      data.setGrpseq( ls.getInt("grpseq") );
                      data.setReptype( ls.getString("reptype") );
                      data.setTitle( ls.getString("title") );
                      data.setMicnt( ls.getString("micnt") );
                      data.setPgcnt( ls.getString("pgcnt") );
                      data.setJccnt( ls.getString("jccnt") );
                      data.setAssigncnt( ls.getString("assigncnt") );
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
           과제 리스트 전체 - 전체 과제
           @param box      receive from the form object and session
           @return ArrayList
           */
           public ArrayList selectReportDetailListAll(RequestBox box) throws Exception { 
               DBConnectionManager connMgr           = null;
               DataBox             dbox            = null;
               ListSet             ls1             = null;
               ArrayList           list1           = null;
               StringBuffer        sbSQL           = new StringBuffer("");
               
               int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
               
               String              v_subj          = box.getString("p_subj"        );  // 과목
               String              v_year          = box.getString("p_year"        );  // 년도
               String              v_subjseq       = box.getString("p_subjseq"     );  // 과목기수
               String              v_grpseq        = box.getString("p_grpseq"      );  // 문제지번호
               String              v_class         = box.getStringDefault("p_class", "ALL" );  // 분반
                                   
               String              v_orderColumn   = box.getString("p_orderColumn" );  // 정렬할 컬럼명
               String              v_orderType     = box.getString("p_orderType"   );  // 정렬할 순서
               String              v_iscopy        = box.getString("p_iscopy"      );  // 모사답안 검색 유무
                  
               try { 
                   connMgr     = new DBConnectionManager();
                   list1       = new ArrayList();
                   

                   /*sbSQL.append(" SELECT                                                                          \n")
                        //.append("     ,   a.ordseq                                                                         \n")
                        .append("        distinct a.grpseq                                                                         \n")
                        .append("     ,   b.seq                                                                            \n")
                        //.append("     ,   nvl(b.resend    , 0  ) resend                                                    \n")
                        //.append("     ,   nvl(b.copyupnm  , 'N') copyupnm                                                  \n")
                        //.append("     ,   nvl(b.copysize  , 'N') copysize                                                  \n")
                        //.append("     ,   nvl(b.copybyte  , 'N') copybyte                                                  \n")
                        .append("     ,   (                                                                                \n")
                        .append("             SELECT  grpseqnm                                                             \n")
                        .append("             FROM    tz_projgrp                                                           \n")
                        .append("             WHERE   subj    = a.subj                                                     \n")
                        .append("             and     year    = a.year                                                     \n")
                        .append("             and     subjseq = a.subjseq                                                  \n")
                        .append("             and     grpseq  = a.grpseq                                                   \n")
                        .append("         )                       assigntitle                                              \n")
                        .append("     ,   a.userid    projgrp                                                              \n")
                        .append("     ,   c.name      projname                                                             \n")
                        //.append("     ,   b.title                                                                          \n")
                        //.append("     ,   b.realfile                                                                       \n")
                        //.append("     ,   b.upfile                                                                         \n")
                        //.append("     ,   b.upfilesize                                                                     \n")
                        //.append("     ,   b.contentsbyte                                                                   \n")
                        //.append("     ,   b.indate                                                                         \n")
                        //.append("     ,   b.score                                                                          \n")
                        .append("     ,   nvl(b.isret     , 'N') isret                                                     \n")
                        .append("     ,   (                                                                                \n")
                        .append("             SELECT  tuserid                                                              \n")
                        .append("             FROM    tz_classtutor                                                        \n")
                        .append("             WHERE   subj    = a.subj                                                     \n")
                        .append("             and     year    = a.year                                                     \n")
                        .append("             and     subjseq = a.subjseq                                                  \n")
                        .append("             and     class   = ( SELECT  class                                            \n")
                        .append("                                 FROM    tz_student                                       \n")
                        .append("                                 WHERE   subj    = a.subj                                 \n")
                        .append("                                 and     year    = a.year                                 \n")
                        .append("                                 and     subjseq = a.subjseq                              \n")
                        .append("                                 and     userid  = a.userid                               \n")
                        .append("                                )                                                         \n")
                        .append("         )                       tuserid                                                  \n")
                        .append("     ,  (                                                                                 \n")
                        .append("             SELECT  name                                                                 \n")
                        .append("             FROM    tz_member                                                            \n")
                        .append("             WHERE   userid  = (                                                          \n")
                        .append("                                 SELECT  tuserid                                          \n")
                        .append("                                 FROM    tz_classtutor                                    \n")
                        .append("                                 WHERE   subj    = a.subj                                 \n")
                        .append("                                 and     year    = a.year                                 \n")
                        .append("                                 and     subjseq = a.subjseq                              \n")
                        .append("                                 and     class   = (                                      \n")
                        .append("                                                     SELECT  class                        \n")
                        .append("                                                     FROM    tz_student                   \n")
                        .append("                                                     WHERE   subj    = a.subj             \n")
                        .append("                                                     and     year    = a.year             \n")
                        .append("                                                     and     subjseq = a.subjseq          \n")
                        .append("                                                     and     userid  =a.userid            \n")
                        .append("                                                    )                                     \n")
                        .append("                               )                                                          \n")
                        .append("         )                       tname                                                    \n")
                        .append("     ,  (                                                                                 \n")
                        .append("         SELECT  projgubun reptype                                                                  \n")
                        .append("         FROM    tz_projgrp                                                               \n")
                        .append("         WHERE   subj    = a.subj                                                         \n")
                        .append("         and     year    = a.year                                                         \n")
                        .append("         and     subjseq = a.subjseq                                                      \n")
                        .append("         and     grpseq  = b.grpseq                                                       \n")
                        .append("        ) as projgubun                                                  					\n")
                        .append("     ,  (                                                                                 \n")
                        .append("         SELECT reptype                                                                  	\n")
                        .append("         FROM    tz_projgrp                                                               \n")
                        .append("         WHERE   subj    = a.subj                                                         \n")
                        .append("         and     year    = a.year                                                         \n")
                        .append("         and     subjseq = a.subjseq                                                      \n")
                        .append("         and     grpseq  = b.grpseq                                                       \n")
                        .append("        ) reptype                                                  						\n")
                        .append("     ,  NVL(TOTALSCORE,0) totalscore                                                  		\n")
                        
                        .append("     ,   a.repdate   --제출일자                                               										\n")
                        .append("     ,   b.ldate      --채점일자                                                                    							\n")
                        .append("     ,   b.retdate    --반려날짜                                                                    							\n")
                        .append(" FROM    tz_projassign       a                                                            \n")
                        .append("     ,   tz_projrep          b                                                            \n")
                        .append("     ,   tz_member           c                                                            \n")
                        .append(" WHERE   a.subj      = b.subj                                                             \n")
                        .append(" and     a.year      = b.year                                                             \n")
                        .append(" and     a.subjseq   = b.subjseq                                                          \n")
                        .append(" and     a.grpseq    = b.grpseq                                                           \n")
                        .append(" and     a.userid    = b.projid                                                           \n")
                        .append(" and     a.userid    = c.userid                                                           \n");*/
                   sbSQL.append(" SELECT distinct A.USERID projgrp    											\n")
                   		.append(" 		, GET_NAME(A.USERID) projname    								\n")
                   		.append(" 		, A.GRPSEQ    													\n")
                   		.append("  		, (SELECT GRPSEQNM    											\n")
                   		.append("  		   FROM TZ_PROJGRP   											\n")
                   		.append(" 		   WHERE SUBJ = A.SUBJ    										\n")
                   		.append(" 		     AND YEAR = A.YEAR    										\n")
                   		.append(" 		     AND SUBJSEQ = A.SUBJSEQ    								\n")
                   		.append(" 		     AND GRPSEQ = A.GRPSEQ    									\n")
                   		.append(" 		  ) assigntitle    												\n")
                   		.append(" 		, NVL((SELECT ISRET    											\n")
                   		.append(" 		       FROM TZ_PROJREP    										\n")
                   		.append(" 		   	   WHERE SUBJ = A.SUBJ    									\n")
                   		.append(" 		         AND YEAR = A.YEAR    									\n")
                   		.append(" 			     AND SUBJSEQ = A.SUBJSEQ    							\n")
                   		.append(" 			     AND GRPSEQ = A.GRPSEQ    								\n")
                   		.append(" 			     AND PROJID = A.USERID    								\n")
                   		.append(" 			     AND ISFINAL = 'Y'    									\n")
                   		.append(" 			   GROUP BY ISRET    										\n")
                   		.append(" 			   ),'N') ISRET    											\n")
                   		.append(" 		, (SELECT TUSERID    											\n")
                   		.append(" 		   FROM TZ_CLASSTUTOR    										\n")
                   		.append(" 		   WHERE SUBJ = A.SUBJ    										\n")
                   		.append(" 		     AND YEAR = A.YEAR    										\n")
                   		.append(" 		     AND SUBJSEQ = A.SUBJSEQ    								\n")
                   		.append(" 		     AND CLASS = ( SELECT CLASS    								\n")
                   		.append(" 		                   FROM TZ_STUDENT    							\n")
                   		.append(" 		                   WHERE SUBJ= A.SUBJ    						\n")
                   		.append(" 		                     AND YEAR = A.YEAR    						\n")
                   		.append("  		                     AND SUBJSEQ = A.SUBJSEQ   					\n")
                   		.append(" 		                     AND USERID = A.USERID    					\n")
                   		.append(" 		                 )    											\n")
                   		.append(" 		   ) TUSERID    												\n")
                   		.append(" 		, (SELECT NAME    												\n")
                   		.append(" 		   FROM TZ_MEMBER    											\n")
                   		.append(" 		   WHERE USERID = (SELECT TUSERID    							\n")
                   		.append(" 		                   FROM TZ_CLASSTUTOR    						\n")
                   		.append(" 		                   WHERE SUBJ = A.SUBJ    						\n")
                   		.append(" 		                     AND YEAR = A.YEAR    						\n")
                   		.append(" 		                     AND SUBJSEQ = A.SUBJSEQ    				\n")
                   		.append("  		                     AND CLASS = (SELECT CLASS   				\n")
                   		.append(" 		                                  FROM TZ_STUDENT    			\n")
                   		.append(" 		                                  WHERE SUBJ = A.SUBJ    		\n")
                   		.append(" 		                                    AND YEAR = A.YEAR    		\n")
                   		.append(" 		                                    AND SUBJSEQ = A.SUBJSEQ    	\n")
                   		.append(" 		                                    AND USERID = A.USERID    	\n")
                   		.append(" 		                                  )    							\n")
                   		.append(" 		                   )    										\n")
                   		.append("  		   ) TNAME   													\n")
                   		.append(" 		,  (SELECT PROJGUBUN    										\n")
                   		.append(" 		    FROM TZ_PROJGRP   	 										\n")
                   		.append(" 		    WHERE SUBJ = A.SUBJ  										\n")
                   		.append(" 		      AND YEAR = A.YEAR    										\n")
                   		.append(" 		      AND SUBJSEQ = A.SUBJSEQ    								\n")
                   		.append(" 		      AND GRPSEQ = A.GRPSEQ    									\n")
                   		.append(" 		    ) AS PROJGUBUN   	 										\n")
                   		.append(" 		, (SELECT REPTYPE    											\n")
                   		.append(" 		   FROM TZ_PROJGRP    											\n")
                   		.append(" 		   WHERE SUBJ = A.SUBJ    										\n")
                   		.append(" 		     AND YEAR = A.YEAR    										\n")
                   		.append(" 		     AND SUBJSEQ = A.SUBJSEQ    								\n")
                   		.append(" 		     AND GRPSEQ = A.GRPSEQ    									\n")
                   		.append(" 		   ) AS REPTYPE    												\n")
                   		.append(" 		, NVL(TOTALSCORE, 0) TOTALSCORE    								\n")
                   		.append(" 		, A.INDATE  -- 최초제출일자   						 					\n")
                   		.append(" 		, A.REPDATE -- 제출일자   						 					\n")
                   		.append(" 		, A.LDATE   -- 채점일    											\n")
                   		.append(" 		, (SELECT RETDATE    											\n")
                   		.append(" 		   FROM TZ_PROJREP     											\n")
                   		.append(" 		   WHERE SUBJ = A.SUBJ    										\n")
                   		.append(" 		     AND YEAR = A.YEAR    										\n")
                   		.append(" 		     AND SUBJSEQ = A.SUBJSEQ		    						\n")
                   		.append(" 		     AND GRPSEQ = A.GRPSEQ		    							\n")
                   		.append(" 		     AND PROJID = A.USERID		    							\n")
                   		.append(" 		     AND ISFINAL = 'Y'		   	 								\n")
                   		.append(" 		     AND ROWNUM =1		   		 								\n")
                   		.append(" 		   ) RETDATE -- 반려일		    								\n")
                   		.append(" 	    , ( SELECT SEQ 													\n")
	                    .append(" 	        FROM TZ_PROJREP												\n")
	                    .append(" 	 	    WHERE SUBJ      = A.SUBJ 									\n")
 	                    .append(" 	  	      AND YEAR      = A.YEAR 									\n")
 	                    .append(" 		      AND SUBJSEQ   = A.SUBJSEQ 								\n")
 	                    .append(" 		      AND PROJID = A.USERID 									\n")
 	                    .append(" 		      AND GRPSEQ    = A.GRPSEQ 									\n")
 	                    .append(" 		      AND ISFINAL = 'Y' 										\n")
 	                    .append(" 	 	    GROUP BY SEQ												\n")
     	                .append(" 	       ) SEQ -- 재제출여부는  SEQ > 1 이면 재제출된것 					\n")
     	                .append(" 	     , a.uprepfile, a.realrepfile, a.uprepfile2, a.realrepfile2		\n")
     	                .append(" 	     , a.upfile_size, a.contents_byte, a.user_ip, d.class			\n")  
     	                .append(" 	     , a.copyupnm, a.copysize, a.copybyte							\n")          	                
                    	.append(" FROM TZ_PROJASSIGN a, tz_projrep b, tz_student d 						\n")
                   		.append(" WHERE 1=1		    													\n")
	                    .append(" and a.subj = b.subj(+)		    									\n")
	                    .append(" and a.subjseq = b.subjseq(+)		    								\n")
	                    .append(" and a.year = b.year(+)	    										\n")
	                    .append(" and a.grpseq = b.grpseq(+)		    								\n")
	                    .append(" and a.userid = b.projid(+)		    								\n")
                   		.append(" and b.isfinal = 'Y'	    											\n")
						.append( "and a.subj = d.subj          											\n")
						.append( "and a.year = d.year           										\n")
						.append( "and a.subjseq = d.subjseq     										\n")
						.append( "and a.userid = d.userid       										\n");                   

                   sbSQL.append(" and a.subj = " + StringManager.makeSQL(v_subj) + "					\n")
                        .append(" and a.year = " + StringManager.makeSQL(v_year) + "					\n")
                        .append(" and a.subjseq = " + StringManager.makeSQL(v_subjseq) + "				\n");
                   
                   if ( !v_class.equals("ALL") ) {
                	   sbSQL.append(" and d.class = " + StringManager.makeSQL(v_class) + "				\n");
                   }
        
                   if ( !v_grpseq.equals("") ) {
                	   sbSQL.append(" and a.grpseq = " + StringManager.makeSQL(v_grpseq   ) + "			\n");
                   }
                    
                   if ( v_iscopy.equals("") ) {
                       if ( v_orderColumn.equals("") ) {
                           sbSQL.append(" order by a.grpseq                      \n");
                       } else { 
                           sbSQL.append(" order by " + v_orderColumn + v_orderType + "  \n");
                       }
                   } else { 
                       sbSQL.append(" order by a.userid, " + v_orderColumn + " \n");         
                   }
                   
                   //System.out.println(this.getClass().getName() + "." + "selectReportDetailListAll() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                   ls1         = connMgr.executeQuery(sbSQL.toString());

                   while ( ls1.next() ) { 
                       dbox    = ls1.getDataBox(); 
                       list1.add(dbox);               
                   }
               } catch ( SQLException e ) {
                   ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
                   throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
               } catch ( Exception e ) {
                   ErrorManager.getErrorStackTrace(e, box, "");
                   throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
               } finally {
                   if ( ls1 != null ) { 
                       try { 
                           ls1.close();  
                       } catch ( Exception e ) { } 
                   }
                   
                   if ( connMgr != null ) { 
                       try { 
                           connMgr.freeConnection(); 
                       } catch ( Exception e ) { } 
                   }
               }

               return list1;
         }
           /**
           교육완료 후 재제출 리스트 - 배정받은 수강생
           @param box      receive from the form object and session
           @return ProjectData   
           */
            public ArrayList selectReportEndAssignList(RequestBox box) throws Exception { 
               DBConnectionManager	connMgr	= null;
               ListSet ls          = null;
               ArrayList list      = null;
               DataBox dbox        = null;
               String  sql         = "";     
               String  wsql        = "";             

               String  v_subj      = box.getString("p_subj");          // 과목
               String  v_year      = box.getString("p_year");          // 년도
               String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
               int     v_grpseq    = box.getInt("p_grpseq");           // 과제문제지번호

               try { 
                   connMgr = new DBConnectionManager();
                   list = new ArrayList();
                   /*
                   //wsql= "  subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and ordseq=" +v_ordseq + " ";
                   wsql= "  subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and grpseq=" +v_ordseq + " ";
                   
                   sql = " SELECT userid, name, deptnam, title, nvl(grpseq,0) ordseq, indate, isret, resend  "
               	   //sql = " SELECT userid, name, deptnam, title, nvl(ordseq,0) ordseq, indate, isret, resend  "
                       + " FROM                                                                                                             "
                       + "     ( select a.userid, b.name, '' deptnam  from tz_student a, tz_member b                                         "
                       + "       where a.userid=b.userid and a.subj='" +v_subj + "' and a.year='" +v_year + "' and a.subjseq='" +v_subjseq + "' and  "
                       + "             a.userid in (select distinct userid                                                                  "
                       + "                            from TZ_PROJASSIGN                                                                    "
                       + "                           where " +wsql + "             ) ) A,                                                      " // 배정받은 수강생 정보
                       + "     ( select title, grpseq, indate, projid, isret, nvl(resend, 0) resend                                         "
                       + "       from tz_projrep where " +wsql + "  ) B                                                                        " // 과제결과           
                       + " WHERE A.userid=B.projid( +)                                          ";
                   */
                   /*wsql= "  subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and grpseq=" +v_ordseq + " ";
                   sql  = " SELECT 	 																	\n";
                   sql += " 	a.userid, name, deptnam, nvl(grpseq,0) ordseq, ldate, b.grpseqnm  title \n";
                   sql += " FROM																		\n";
                   sql += " ( select a.userid, b.name, '' deptnam  from tz_student a, tz_member b		\n";
                   sql += " where a.userid=b.userid 													\n";
                   sql += " and a.subj= " + StringManager.makeSQL(v_subj								  );
                   sql += " and a.year= " + StringManager.makeSQL(v_year								  );
                   sql += " and a.subjseq = " + StringManager.makeSQL(v_subjseq							  );
                   sql += " and a.userid in 															\n";
                   sql += " (select distinct userid from TZ_PROJASSIGN 									\n";
                   sql += " where   subj =" + StringManager.makeSQL(v_subj) 
                   					+ " and year=" + StringManager.makeSQL(v_year) 
                   					+ " and subjseq=" + StringManager.makeSQL(v_subjseq) 
                   					+ " and grpseq=" + v_ordseq + " ) ) A,								\n";
                   sql += " ( select d.grpseq, d.ldate, d.userid, c.grpseqnm									\n";
                   sql += " from tz_projassign	d, tz_projgrp c											\n";
                   sql += " where d.subj = c.subj 														\n";
                   sql += "   and d.year = c.year 														\n";
                   sql += "   and d.subjseq = c.subjseq 												\n";
                   sql += "   and d.grpseq = c.grpseq 													\n";
                   sql += "   and d.subj=" + StringManager.makeSQL(v_subj								);
                   sql += "   and d.year=" + StringManager.makeSQL(v_year								);
                   sql += "   and d.subjseq=" + StringManager.makeSQL(v_subjseq							);
                   sql += "   and d.grpseq=" + v_ordseq + "    ) B										\n";
                   sql += " WHERE A.userid=B.userid(+)													\n";*/
                   
                   sql  = " SELECT 													\n";
                   sql += " 	  A.USERID 											\n";
                   sql += " 	, GET_NAME(A.USERID) name							\n";
                   sql += " 	, ( SELECT 											\n";
                   sql += " 			GRPSEQNM    								\n";
                   sql += " 		FROM TZ_PROJGRP 								\n";
                   sql += " 		WHERE SUBJ      = A.SUBJ 						\n";
                   sql += " 		  AND SUBJSEQ   = A.SUBJSEQ 					\n";
                   sql += " 	      AND YEAR      = A.YEAR 						\n";
                   sql += " 		  AND GRPSEQ    = A.GRPSEQ 						\n";
                   sql += " 	   ) TITLE 											\n";
                   sql += " 	, ( SELECT 											\n";
                   sql += " 			REPTYPE	    								\n";
                   sql += " 		FROM TZ_PROJGRP 								\n";
                   sql += " 		WHERE SUBJ      = A.SUBJ 						\n";
                   sql += " 		  AND SUBJSEQ   = A.SUBJSEQ 					\n";
                   sql += " 	      AND YEAR      = A.YEAR 						\n";
                   sql += " 		  AND GRPSEQ    = A.GRPSEQ 						\n";
                   sql += " 	   ) REPTYPE 										\n";
                   sql += " 	, A.GRPSEQ 											\n";
                   sql += " 	, A.REPDATE -- 제출일				 					\n";
                   sql += " 	, A.LDATE -- 채점일				 					\n";
                   sql += " 	,( SELECT ISRET 									\n";
                   sql += " 	   FROM TZ_PROJREP									\n";
                   sql += " 	   WHERE SUBJ      = A.SUBJ 						\n";
                   sql += " 		 AND YEAR      = A.YEAR 						\n";
                   sql += " 	     AND SUBJSEQ   = A.SUBJSEQ 						\n";
                   sql += " 		 AND PROJID    = A.USERID  						\n";
                   sql += " 	     AND ISFINAL   = 'Y'  							\n";
                   sql += " 	     AND ISRET is not null 							\n";
                   sql += " 	   GROUP BY ISRET 									\n";
                   sql += " 	  ) ISRET --반려여부 									\n";
                   sql += " 	, ( SELECT SEQ 										\n";
                   sql += " 	    FROM TZ_PROJREP									\n";
                   sql += " 		WHERE SUBJ      = A.SUBJ 						\n";
                   sql += " 	  	  AND YEAR      = A.YEAR 						\n";
                   sql += " 		  AND SUBJSEQ   = A.SUBJSEQ 					\n";
                   sql += " 		  AND PROJID = A.USERID 						\n";
                   sql += " 		  AND GRPSEQ    = A.GRPSEQ 						\n";
                   sql += " 		  AND ISFINAL = 'Y' 							\n";
                   sql += " 	 	GROUP BY SEQ									\n";
                   sql += " 	   ) SEQ -- 재제출여부는  SEQ > 1 이면 재제출된것 			\n";
                   sql += " 	, ( SELECT COUNT(*)									\n";
                   sql += " 	    FROM TZ_PROJREP									\n";
                   sql += " 		WHERE SUBJ      = A.SUBJ 						\n";
                   sql += " 	  	  AND YEAR      = A.YEAR 						\n";
                   sql += " 		  AND SUBJSEQ   = A.SUBJSEQ 					\n";
                   sql += " 		  AND PROJID = A.USERID 						\n";
                   sql += " 		  AND GRPSEQ    = A.GRPSEQ 						\n";
                   sql += " 		  AND ISFINAL = 'Y' 							\n";
                   sql += " 	   ) CNT -- CNT > 0 이면 제출여부가 있는것임 				\n";
                   sql += " FROM TZ_PROJASSIGN A 									\n";
                   sql += " WHERE A.SUBJ     = " + StringManager.makeSQL(v_subj);
                   sql += "   AND A.YEAR     = " + StringManager.makeSQL(v_year);
                   sql += "   AND A.SUBJSEQ  = " + StringManager.makeSQL(v_subjseq);
                   sql += "   AND A.GRPSEQ   = " + v_grpseq;
                   
                   
//                   System.out.println("sql" + sql);

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
            교육완료 후 재제출 리스트 - 다른 과제에도 배정 받지 않은 수강생
            @param box      receive from the form object and session
            @return ProjectData   
            */
             public ArrayList selectReportEndAssignNotList(RequestBox box) throws Exception { 
                DBConnectionManager	connMgr	= null;
                ListSet ls          = null;
                ArrayList list      = null;
                DataBox dbox        = null;
                String  sql         = "";     
                String  wsql        = "";             

                String  v_subj      = box.getString("p_subj");          // 과목
                String  v_year      = box.getString("p_year");          // 년도
                String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
                int     v_ordseq    = box.getInt("p_ordseq");           // 과제문제번호
                int     v_projseq   = box.getInt("p_projseq");          // 과제셋트번호
                
                try { 
                    connMgr = new DBConnectionManager();
                    list = new ArrayList();

                    wsql= "  subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' ";
                    
                    sql = " select a.userid, b.name, '' deptnam, '' title, 0 ordseq, '' indate, '' isret, '' resend  "  
                        + "   from tz_student a, tz_member b                                                        "
                        + "  where a.userid=b.userid and a.subj='" +v_subj + "' and a.year='" +v_year + "' and a.subjseq='" +v_subjseq + "'   "
                        + "    and a.userid not in (select distinct userid                                                              "
                        + "                           from TZ_PROJASSIGN                                                                "
                        + "                          where " +wsql + " and ( grpseq=" +v_ordseq + "))                "; // 배정 안 받은 수강생 정보

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
             교육완료 후 재제출 등록
             @param box      receive from the form object and session
             @return int
             */
              public int insertReportEndSubmit(RequestBox box) throws Exception { 
                 DBConnectionManager	connMgr	= null;
                 ListSet ls                  = null;
                 ListSet ls3                 = null;
                 PreparedStatement pstmt2    = null;
                 String sql1                 = "";
                 String sql2                 = "";
                 String sql3                 = "";
                 int isOk                    = 0;
                 String  v_user_id           = box.getString("p_userid");        
                 String  v_subj              = box.getString("p_subj");          // 과목
                 String  v_year              = box.getString("p_year");          // 년도
                 String  v_subjseq           = box.getString("p_subjseq");       // 과목기수
                 int     v_projseq           = box.getInt("p_projseq");
                 int     v_ordseq            = box.getInt("p_ordseq");

                 String v_title              = box.getString("p_title");
                 String v_contents           = box.getString("p_contents");
                 String v_contentsbyte       = box.getString("p_contentsbyte");        
                 String v_realFileName1      = box.getRealFileName("p_file1");
                 String v_newFileName1       = box.getNewFileName("p_file1");
                 String v_upfile1            = box.getString("p_upfile1");
                 String v_check              = box.getString("p_check");
                         
                 if ( v_newFileName1.length() == 0) {   v_newFileName1 = v_upfile1;   }
                 
                 ConfigSet conf = new ConfigSet();
                 File reportFile = new File(conf.getProperty("dir.upload.project"), v_year + File.separator + v_subj  + File.separator + v_newFileName1);



                 String fileSize = (reportFile.length() ) + "";

                 // 기존 파일정보
                 String v_oldupfile = "";
                 String v_oldrealfile = "";
                 String v_oldupfilesize = "";
                 
                 try { 
                     connMgr = new DBConnectionManager();
                     connMgr.setAutoCommit(false);
                     
                     sql1  = "select seq from TZ_PROJREP ";
                     sql1 += " where subj=" +SQLString.Format(v_subj);
                     sql1 += "   and year=" +SQLString.Format(v_year);
                     sql1 += "   and subjseq=" +SQLString.Format(v_subjseq);
                     sql1 += "   and ordseq=" +SQLString.Format(v_ordseq);
                     sql1 += "   and projid=" +SQLString.Format(v_user_id);
                     ls = connMgr.executeQuery(sql1);
//                     System.out.println("sql1 ==  ==  ==  ==  == > " +sql1);

                     // 기존파일 정보 불러오기
                     sql3 = "select  upfile,";
                     sql3 += "        upfilesize,";
                     sql3 += "        realfile  ";
                     sql3 += "from    TZ_PROJREP ";
                     sql3 += "where   subj    = '" + v_subj    + "' and ";
                     sql3 += "        year    = '" + v_year    + "' and ";
                     sql3 += "        subjseq = '" + v_subjseq + "' and ";
                     sql3 += "        ordseq  = '" + v_ordseq  + "' and ";
                     sql3 += "        projid  = '" + v_user_id  + "' ";
                     ls3 = connMgr.executeQuery(sql3);
                     if ( ls3.next() ) { 
                         v_oldupfile     = ls3.getString("upfile");
                         v_oldrealfile   = ls3.getString("realfile");
                         v_oldupfilesize = ls3.getString("upfilesize");
                     }
                     
                     // 업로드한 파일이 없을 경우
                     if ( v_realFileName1.equals("") ) { 
                         // 기존파일 삭제
                         if ( v_check.equals("Y") ) { 
                             v_newFileName1   = "";
                             fileSize         = "";
                             v_realFileName1  = "";
                         } else { 
                         // 기존파일 유지
                             v_newFileName1   = v_oldupfile;
                             fileSize         = v_oldupfilesize;
                             v_realFileName1  = v_oldrealfile;
                         }
                     }

                     if ( ls.next() ) { 
                         sql2  = "update TZ_PROJREP set resend=1, title=?,contents=empty_clob(),upfile=?,upfilesize=?, realfile=?, ldate=to_char(sysdate,'YYYYMMDDHH24MISS'),indate=to_char(sysdate,'YYYYMMDDHH24MISS') ";
                         sql2 += "where subj=? and year=? and subjseq=? ";
                         sql2 += "and ordseq=? and projid=? ";                
                         pstmt2 = connMgr.prepareStatement(sql2);
                         pstmt2.setString(1,v_title);
                         // pstmt2.setString(2,v_contents);
                         pstmt2.setString(2,v_newFileName1);
                         pstmt2.setString(3,fileSize);
                         pstmt2.setString(4,v_realFileName1);
                         //pstmt2.setString(5,v_user_id);
                         pstmt2.setString(5,v_subj);
                         pstmt2.setString(6,v_year);
                         pstmt2.setString(7,v_subjseq);
                         pstmt2.setInt(8,v_ordseq);
                         pstmt2.setString(9,v_user_id);
                         isOk = pstmt2.executeUpdate();  
                         if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
                         
                     } else { 
                     
                         sql2  = "insert into TZ_PROJREP(subj,year,subjseq,ordseq,projid,seq,lesson,title,contents,  upfile,upfilesize,realfile,luserid,ldate, ";
                         sql2 += "                       indate, contentsbyte, projseq ) ";
                         sql2 += "values(?,?,?,?,?,?,?,?,?,  ?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'), ";
//                         sql2 += "values(?,?,?,?,?,?,?,?,empty_clob(),  ?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'), ";
                         sql2 += "       (select eduend from tz_subjseq where subj=? and year=? and subjseq=?) , ?, ? )";                
                         pstmt2 = connMgr.prepareStatement(sql2);

                         pstmt2.setString(1,v_subj);
                         pstmt2.setString(2,v_year);
                         pstmt2.setString(3,v_subjseq);
                         pstmt2.setInt   (4,v_ordseq);
                         pstmt2.setString(5,v_user_id);
                         pstmt2.setInt   (6,1);
                         pstmt2.setString(7,"000");                                
                         pstmt2.setString(8,v_title);
                         pstmt2.setString(9,v_contents);

                         pstmt2.setString(10,v_newFileName1);
                         pstmt2.setString(11,fileSize);
                         pstmt2.setString(12,v_realFileName1);
                         pstmt2.setString(13,v_user_id);

                         pstmt2.setString(14,v_subj);
                         pstmt2.setString(15,v_year);
                         pstmt2.setString(16,v_subjseq);                

                         pstmt2.setString(17,v_contentsbyte);
                         pstmt2.setInt   (18,v_projseq);                
                         isOk = pstmt2.executeUpdate();
                         if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }

                     }
                     
                     sql2 = "select contents from TZ_PROJREP ";
                     sql2 = "  where SUBJ = '" +v_subj + "' AND YEAR = '" +v_year + "' AND SUBJSEQ = '" +v_subjseq + "' ";
                     sql2 = "  AND ORDSEQ = '" +v_ordseq + "' AND PROJID = '" +v_user_id + "'";
//         			connMgr.setOracleCLOB(sql2, v_contents);	// 오라클
                     
                     if ( isOk > 0) { 
                       connMgr.commit();
                     } else { 
                       connMgr.rollback();
                     }
                    
                 } catch ( Exception ex ) {                        
                     if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
                     throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
                 } finally { 
                     if ( ls3 != null )      { try { ls3.close(); } catch ( Exception e ) { } }
                     if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
                     if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
                     if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
                     if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
                 }

                 return isOk;
             }
              /**
              과제 제출내역 관리
              @param box      receive from the form object and session
              @return ProjectData   
              */
               public ArrayList selectReportSubmitOpen(RequestBox box) throws Exception { 
                  DBConnectionManager	connMgr	= null;
                  ListSet ls          = null;
                  String sql          = "";
                  ArrayList list   	  = null;
                  DataBox dbox 		  = null;
                  String  v_subj      = box.getString("p_subj");          // 과목
                  String  v_year      = box.getString("p_year");          // 년도
                  String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
                  int     v_grpseq    = box.getInt("p_grpseq");
                  int     v_seq       = box.getInt("p_seq");
                  //String  v_lesson    = box.getString("p_lesson");
                  String  v_projgrp   = box.getString("p_projgrp");
                  
                  try {
                	  connMgr = new DBConnectionManager();
                	  
                	  sql  = " SELECT A.TITLE, A.CONTENTS, A.ORDSEQ 			\n";
                	  sql += " 	  , A.REALFILE, UPFILE -- 출제파일				\n";
                	  sql += " 	  , A.REALFILE2, UPFILE2 -- 정답파일 			\n";
                	  sql += "	  , B.SUBJ, B.YEAR, B.SUBJSEQ, B.GRPSEQ			\n";
                	  sql += "	  , C.CONTENTS repcontents, c.seq				\n";
                	  sql += "	  , A.SCORE qscore, C.SCORE repscore			\n";
                	  sql += "	  , C.ISOPENEXP, C.CORRECTION					\n";
                	  sql += "    ,retreason, retdate, isret, get_name(retuserid) retuserid";
                	  sql += " FROM TZ_PROJORD A , TZ_PROJMAP B, TZ_PROJREP C	\n";
                	  sql += " WHERE A.ORDSEQ 	= B.ORDSEQ						\n";
                	  sql += "   AND A.SUBJ 		= B.SUBJ 					\n";
                	  sql += "   AND B.SUBJ       = C.SUBJ						\n";
                	  sql += "   AND B.SUBJSEQ    = C.SUBJSEQ					\n";
                	  sql += "   AND B.YEAR       = C.YEAR						\n";
                	  sql += "   AND B.GRPSEQ     = C.GRPSEQ					\n";
                	  sql += "   AND B.ORDSEQ     = C.ORDSEQ					\n";
                	  sql += "   AND C.ISFINAL    = 'Y' 						\n"; 
                	  sql += "   AND B.GRPSEQ 	= " +v_grpseq + " 				\n";
                	  sql += "   AND B.YEAR 		= '" +v_year + "' 			\n";
                	  sql += "   AND B.SUBJ 		= '" +v_subj + "' 			\n";
                	  sql += "   AND B.SUBJSEQ 	= '" +v_subjseq + "' 			\n";
                	  sql += "   AND C.PROJID 	= '" +v_projgrp + "' 			\n";
                	  System.out.println("sql==\n"+sql);
                	                                                                                                                                                                                                                                                                                                                                                  
                      ls 	= connMgr.executeQuery(sql);
                      list 	= new ArrayList();
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
              리포트 평가 내역 및 서술형 && 단답형 파일 내용보여주기 단답형 파일은 보여주지 않는다. 
              @param box      receive from the form object and session
              @return ProjectData   
                */
               public DataBox selectReportSubmitOpen2(RequestBox box) throws Exception { 
            	   DBConnectionManager	connMgr	= null;
            	   ListSet ls          = null;
            	   String sql          = "";
            	   ArrayList list   	  = null;
            	   DataBox dbox 		  = null;
            	   String  v_subj      = box.getString("p_subj");          // 과목
            	   String  v_year      = box.getString("p_year");          // 년도
            	   String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
            	   int     v_grpseq    = box.getInt   ("p_grpseq");
            	   int     v_seq       = box.getInt   ("p_seq");
            	   String  v_projgrp   = box.getString("p_projgrp");	//과제제출자 아이디 
            	   
            	   try { 
            		   connMgr = new DBConnectionManager();
            		   
            		   sql   = " SELECT  \n";
                       sql  += " 		  A.GRPSEQ, A.SUBJ, A.SUBJSEQ   						\n";
                       sql  += " 		, A.YEAR, A.USERID, A.ADDRESS   						\n";
                       sql  += " 		, A.SUCCESS, A.FAIL, A.REPDATE, A.REVIEW   						\n";
                       sql  += " 		, A.TOTALSCORE --채점 총점  							\n";
                       sql  += " 		, B.GRPTOTALSCORE AS SCORE --문제 총점 	 				\n";
                       sql  += " 		, A.REALREPFILE --실제답안파일		 	 				\n";
                       sql  += " 		, A.UPREPFILE   --업로드답안파일		 	 			\n";
                       sql  += " 		, A.REALREPFILE2 --실제답안파일		 	 				\n";
                       sql  += " 		, A.UPREPFILE2   --업로드답안파일		 	 			\n";
                       sql  += " 		, A.USERID   	--제출자 아이디		 	 				\n";
                       sql  += " FROM TZ_PROJASSIGN A, TZ_PROJGRP B  							\n";
                       sql  += " WHERE 1 = 1   													\n";
                       sql  += "   AND A.GRPSEQ = B.GRPSEQ  									\n";
                       sql  += "   AND A.SUBJ = B.SUBJ 											\n";
                       sql  += "   AND A.SUBJSEQ = B.SUBJSEQ 									\n";
                       sql  += "   AND A.YEAR = B.YEAR  										\n";
                       sql  += "   AND A.SUBJ     = " +StringManager.makeSQL(v_subj)    + "		\n";
                       sql  += "   AND A.SUBJSEQ  = " +StringManager.makeSQL(v_subjseq) + " 	\n";
                       sql  += "   AND A.YEAR     = " +StringManager.makeSQL(v_year)    + "		\n";
                       sql  += "   AND A.GRPSEQ   = " +v_grpseq  + " 							\n";
                       sql  += "   AND A.userid   = " +StringManager.makeSQL(v_projgrp)  + "	\n";
            		   
            		   ls 	= connMgr.executeQuery(sql);
            		   
            		   while ( ls.next() ) {
            			   dbox = ls.getDataBox();
            		   }
            		   
            	   } catch ( Exception ex ) { 
            		   ErrorManager.getErrorStackTrace(ex, box, sql);
            		   throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            	   } finally { 
            		   if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            		   if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            	   }
            	   
            	   return dbox;        
               }
               /**
               과제 제출 인원 명단
               @param box      receive from the form object and session
               @return ArrayList
               */
                public ArrayList selectReportSubmitListOpen(RequestBox box) throws Exception { 
                   DBConnectionManager	connMgr	= null;
                   ListSet ls          = null;
                   ArrayList list      = null;
                   String sql          = "";
                   ProjectData data   = null;
                   String  v_subj      = box.getString("p_subj");          // 과목
                   String  v_year      = box.getString("p_year");          // 년도
                   String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
                   int     v_ordseq    = box.getInt("p_ordseq");
                   String  v_projgrp   = box.getString("p_projgrp");
                   try { 
                       connMgr = new DBConnectionManager();
                       list = new ArrayList();
                       
                       // select userid,name
                       sql = "select A.projid userid, ";
                       sql += "(select name from TZ_MEMBER where userid=A.projid) as name ";
           ///           sql += "from TZ_PROJGRP A ";
                       sql += "from TZ_PROJREP A ";
                       sql += "where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' ";
           ///           sql += "and ordseq='" +v_ordseq + "' and projid='" +v_projgrp + "'";
                       sql += "and ordseq='" +v_ordseq + "' ";
//                       System.out.println("33333 sql" +sql);
                       ls = connMgr.executeQuery(sql);

                       while ( ls.next() ) { 
                           data = new ProjectData();
                           data.setUserid( ls.getString("userid") );
                           data.setName( ls.getString("name") );
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
                과제 점수관리 (채점)
                @param box      receive from the form object and session
                @return int
                */
                 public int updateReportJudge(RequestBox box) throws Exception { 
                    DBConnectionManager	connMgr	= null;
                    PreparedStatement pstmt2 = null;
                    ListSet ls1         = null;     
                    ListSet ls2         = null;     
                    String sql          = "";
                    String sql1         = ""; 
                    String sql2         = ""; 
                    String sql3         = ""; 
                    int isOk            = 0;
                   
                    String  v_subj      = box.getString("p_subj");          // 과목
                    String  v_year      = box.getString("p_year");          // 년도
                    String  v_subjseq   = box.getString("p_subjseq");       // 과목기수        
                    String  v_projgrp   = box.getString("p_projgrp");       
                    String  v_reptype   = box.getString("p_reptype");
                    //String  v_address   = box.getString("p_address");		// 인사말
                    //String  v_success   = box.getString("p_success");		// 잘된 점 
                    //String  v_fail	    = box.getString("p_fail");		// 잘못된 점
                    String  v_review     = box.getString("p_review");        // 총평
                    String  v_correction = box.getString("p_correction");    //첨삭지도
                    
                    //v_address = StringManager.replace(v_address, "'", "`");
                    //v_success = StringManager.replace(v_success, "'", "`");
                    //v_fail = StringManager.replace(v_fail, "'", "`");
                    v_review = StringManager.replace(v_review, "'", "`");

                    String  v_isret     = box.getString("p_isret");     // 반려여부
                    
                    Vector score_mas	= box.getVector("p_score_mas");	// 문제별 점수 
                    Vector ordseq		= box.getVector("p_ordseq");	// 문제 번호

                    //int     v_score_mas = box.getInt("p_score_mas");	//한문제에서 여러 문제로 바뀌어서 주석
                    int     v_grpseq    = box.getInt("p_grpseq");
                    int     v_totalscore = 0;
                    int     v_cnt       = 0;
                    long    v_finalscore= 0;        

                    try { 
                        connMgr = new DBConnectionManager();
                        connMgr.setAutoCommit(false);
                        
                        // 반려일경우 반려정보만 반영 (by 정은년 , 점수=0점 처리, 2005.9.13 retmailing 추가)
                        if ( v_isret.equals("Y") ) { 
                       	 /*sql2 = "update TZ_PROJREP set retmailing='N', resend=0, score=0, score_mas=0, isret='Y',isfinal='N',retreason='" +v_retreason + "',  ldate=to_char(sysdate,'YYYYMMDDHH24MISS') , ";
                           sql2 += "                      retdate=NVL(retdate, to_char(sysdate,'YYYYMMDDHH24MISS')), luserid=NVL(luserid, '"+box.getSession("userid")+"'), retuserid=NVL(retuserid, '" +box.getSession("userid") + "') ";
                           sql2 += "where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' ";
                           sql2 += "  and ordseq='" +v_ordseq + "' and projid='" +v_projgrp + "'";
                       	isOk = connMgr.executeUpdate(sql2);
                       	*/
                       	//2008.12.08 수정 반려인 경우 tz_projrep의 점수가 0점처리 and tz_projassign의 totalscore 점수 0점처리 
	                       	for(int i = 0; i < score_mas.size() ; i++ ) {
	                       		int v_score_mas = Integer.parseInt((String)score_mas.elementAt(i));
	                       		int v_ordseq = Integer.parseInt((String)ordseq.elementAt(i));
	                       		
	                       		sql2  = " UPDATE TZ_PROJREP SET 									\n";
	                       		sql2 += " 			, score=0 										\n";
	                       		sql2 += " 			, score_mas=0 									\n";
	                       		sql2 += " 			, isret='Y' 									\n";
	                       		sql2 += " 			, correction = empty_clob()                     \n";	                       		
	                       		sql2 += " 			, retreason = ?                                  \n";	                       		
	                       		sql2 += " 			, retdate = to_char(sysdate,'YYYYMMDDHH24MISS') \n";
	                       		sql2 += " 			, retuserid = ? 	\n";
	                       		sql2 += " 			, ldate   = to_char(sysdate,'YYYYMMDDHH24MISS') \n";	// 채점일
	                       		sql2 += " WHERE SUBJ 	= ? 							\n";
	                       		sql2 += "   AND YEAR 	= ?							    \n";
	                       		sql2 += "   AND SUBJSEQ = ? 						    \n";
	                       		sql2 += "   AND PROJID 	= ?						        \n";
	                       		sql2 += "   AND GRPSEQ 	= ?						        \n";
	                       		sql2 += "   AND ORDSEQ 	= ? 						    \n";
	                       		sql2 += "   AND ISFINAL = 'Y'										\n";
	                       		
	                       		pstmt2 = connMgr.prepareStatement(sql2);
	                       		pstmt2.setString(1, box.getString("p_retreason"));
	                       		pstmt2.setString(2, box.getSession("userid"));
	                       		pstmt2.setString(3, v_subj);
	                       		pstmt2.setString(4, v_year);
	                       		pstmt2.setString(5, v_subjseq);
	                       		pstmt2.setString(6, v_projgrp);
	                       		pstmt2.setInt   (7, v_grpseq);
	                       		pstmt2.setInt   (8, v_ordseq);
	                       		
	                       		isOk = pstmt2.executeUpdate();
	                       		if ( pstmt2 != null ) { pstmt2.close(); }
	                       		
	                            // 컨텐츠를 입력한다.
	                            String sql7 = "select correction from TZ_PROJREP where SUBJ = '" + v_subj + "' and YEAR = '" +v_year + "' AND SUBJSEQ = '" +v_subjseq + "' AND PROJID = '" +v_projgrp + "' AND GRPSEQ = " +v_grpseq + " AND ORDSEQ = " +v_ordseq + " AND ISFINAL = 'Y' ";
	                            connMgr.setOracleCLOB(sql7, v_correction);
	                       	}

                        	sql3  = " UPDATE TZ_PROJASSIGN SET 									\n";
                        	sql3 += " 					  totalscore = 0 						\n";
                        	//sql3 += " 					, address 	 = " + SQLString.Format(v_address) + " 		\n";
                        	//sql3 += " 					, success 	 = " + SQLString.Format(v_success) + " 		\n";
                        	//sql3 += " 					, fail 		 = " + SQLString.Format(v_fail) + " 		\n";
                        	sql3 += " 					, review 	= empty_clob()		\n";
                        	sql3 += " 					, ldate   = to_char(sysdate,'YYYYMMDDHH24MISS')	\n";
                        	sql3 += " WHERE SUBJ 	= ? 							\n";
                        	sql3 += "   AND YEAR 	= ? 							\n";
                        	sql3 += "   AND SUBJSEQ = ? 						\n";
                        	sql3 += "   AND USERID 	= ? 						\n";
                        	sql3 += "   AND GRPSEQ 	= ? 						\n";
                        	
                       		pstmt2 = connMgr.prepareStatement(sql3);
                       		pstmt2.setString(1, v_subj);
                       		pstmt2.setString(2, v_year);
                       		pstmt2.setString(3, v_subjseq);
                       		pstmt2.setString(4, v_projgrp);
                       		pstmt2.setInt   (5, v_grpseq);
                       		
                       		isOk = pstmt2.executeUpdate();
                        	if ( pstmt2 != null ) { pstmt2.close(); }
                        	
                            // 컨텐츠를 입력한다.
                            String sql4 = "select review from TZ_PROJASSIGN where SUBJ = '" + v_subj + "' and YEAR = '" +v_year + "' AND SUBJSEQ = '" +v_subjseq + "' AND USERID 	= '" +v_projgrp + "' AND GRPSEQ 	= '" +v_grpseq + "' ";
                            connMgr.setOracleCLOB(sql4, v_review);
                      	
                            // 반려일때 학습자에게 반려메일을 전송한다.
                            // 메일 보내기 시작

                            // 학습자 정보

//                            sql  = "select  (select name  from tz_member where userid = tz_projrep.projid) name, ";
//                            sql += "        A.userid, ";
//                            sql += "        (select email from tz_member where userid = tz_projrep.projid) email, ";
//                            sql += "        (SELECT GRPSEQNM FROM TZ_PROJGRP WHERE ), ";
//                            sql += "        (select subjnm from tz_subj where subj = tz_projrep.subj) subjnm,";
//                            //sql += "        retreason ";
//                            sql += "from    tz_projassign A";
//                            sql += "where   A.subj    = '" + v_subj + "' and ";
//                            sql += "        A.year    = '" + v_year + "' and ";
//                            sql += "        A.subjseq = '" + v_subjseq + "' and ";
//                            //sql += "        ordseq  = '" + v_ordseq + "' and ";
//                            sql += "        A.grpseq  = '" + v_grpseq + "' and ";
//                            //sql += "        projid  = '" + v_projgrp + "' ";
//                            sql += "        A.userid  = '" + v_projgrp + "' ";
                        	
                        	
                        	
                        	/*
                        	sql  = " SELECT (SELECT NAME 								\n";
                        	sql += " 		 FROM TZ_MEMBER 							\n";
                        	sql += " 		 WHERE USERID = A.USERID 					\n";
                        	sql += " 		) NAME, 									\n";
                        	sql += " 		A.USERID,									\n";
                        	sql += " 		(SELECT EMAIL								\n";
                        	sql += " 		 FROM TZ_MEMBER								\n";
                        	sql += " 		 WHERE USERID = A.USERID 					\n";
                        	sql += " 		) EMAIL,									\n";
                        	sql += " 		(SELECT GRPSEQNM FROM TZ_PROJGRP			\n";
                        	sql += " 		 WHERE SUBJ =A.SUBJ							\n";
                        	sql += " 		   AND SUBJSEQ = A.SUBJSEQ 					\n";
                        	sql += " 		   AND YEAR = A.YEAR						\n";
                        	sql += " 		   AND GRPSEQ =A.GRPSEQ						\n";
                        	sql += " 		) TITLE,									\n";
                        	sql += " 		(SELECT SUBJNM 								\n";
                        	sql += " 		 FROM TZ_SUBJ 								\n";
                        	sql += " 		 WHERE SUBJ = A.SUBJ						\n";
                        	sql += " 		) SUBJNM									\n";
                        	sql += " FROM TZ_PROJASSIGN A								\n";
                        	sql += " WHERE A.SUBJ = " + StringManager.makeSQL(v_subj		);
                        	sql += "   AND A.YEAR = " + StringManager.makeSQL(v_year		);
                        	sql += "   AND A.SUBJSEQ = " + StringManager.makeSQL(v_subjseq	);
                        	sql += "   AND GRPSEQ = " + v_grpseq;
                        	sql += "   AND USERID = " + StringManager.makeSQL(v_projgrp		);
                            
                            ls2 = connMgr.executeQuery(sql);
                            
                            if ( ls2.next() ) { 
                
//                                String v_sendhtml = "RejectReport.html";
                
                                // 보내는 사람 세팅
                                box.put("p_fromEmail",box.getSession("email") );
                                box.put("p_fromName", box.getSession("name") );
                                box.put("p_comptel","");
                                
                                String v_userid    =  ls2.getString("userid");
                                String v_name      =  ls2.getString("name");
                                String v_toEmail   =  ls2.getString("email");
                                String v_report    =  ls2.getString("title");
                                String v_subjnm    =  ls2.getString("subjnm");
                                // 받는사람 세팅 & 메일 정보 
                                box.put("to", v_userid+":"+v_toEmail+":"+v_name);
                                box.put("p_title", v_report);
                                box.put("p_content", "반려되었습니다.");
                                box.put("p_mail_code", "001");
                                int mailOk = 0;
                                
                                FreeMailBean bean = new FreeMailBean();
                          
                                mailOk = bean.amailSendMail(box);
                
//                                FormMail fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우
//                
//                                MailSet mset = new MailSet(box);               //      메일 세팅 및 발송
//                
//                                //String v_mailTitle = "오토에버 과제 반려 안내 메일입니다.";
//                                String v_mailTitle = "KT 리포트 반려 안내 메일입니다.";
//                                String v_isMailing =  "1";              // email로만 전송
//                                String v_name      =  ls2.getString("name");
//                                String v_toCono    =  ls2.getString("tocono");
//                                String v_toEmail   =  ls2.getString("email");
//                                String v_report    =  ls2.getString("title");
//                                String v_subjnm    =  ls2.getString("subjnm");
//                                //String v_content   =  ls2.getString("retreason"); // 반려
//                                String v_content   =  "반려되었습니다."; 				// 반려
//                
//                                box.put("v_name",v_name);
//                                box.put("v_toEmail",v_toEmail);
//                                
//                
//                                if ( !v_toEmail.equals("")) 
//                                { 
//                                    mset.setSender(fmail);                      // 메일보내는 사람 세팅
//                                    fmail.setVariable("name",   v_name);        // 수신자명
//                                    fmail.setVariable("subjnm", v_subjnm);      // 과목명
//                                    fmail.setVariable("report", v_report);      // 과제 제출한 제목
//                                    fmail.setVariable("content", StringManager.replace(v_content,"\r\n","<br > ") );   // 반려사유
//                                    
////                                    String v_mailContent = fmail.getNewMailContent();
////                                    boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent, v_isMailing, v_sendhtml);
//                    
////                                    System.out.println("반려메일 전송 여부 = " + isMailed);
//                                }
                            } // 메일 보내기 끝
							*/
                        	
                        // 반려가 아닌 경우...
                        } else {
                        	
// 2008.12.08 수정                         	
//                            sql2 = " update TZ_PROJREP set resend=0, score_mas=" +v_score_mas + ",score=" +v_score_mas + ",tucontents='" +v_tucontents + "',isret='N',isfinal='Y', ldate=NVL(ldate, to_char(sysdate,'YYYYMMDDHH24MISS')),tudate=NVL(tudate, to_char(sysdate,'YYYYMMDDHH24MISS'))   ";
//                            // sql2 = " update TZ_PROJREP set resend=0, score_mas=" +v_score_mas + ",score=" +v_score_mas + ",tucontents='" +v_tucontents + "',isret=isret,isfinal='Y', ldate=to_char(sysdate,'YYYYMMDDHH24MISS'),tudate=to_char(sysdate,'YYYYMMDDHH24MISS')  ";
//                            sql2 += " where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' ";
//                            sql2 += " and   ordseq='" +v_ordseq + "' and projid='" +v_projgrp + "'";
                        	for(int i = 0; i < score_mas.size() ; i++ ) {
	                       		int v_score_mas = Integer.parseInt((String)score_mas.elementAt(i));
	                       		int v_ordseq = Integer.parseInt((String)ordseq.elementAt(i));
	                       		String v_isopenexp = box.getStringDefault("p_isopenexp" + v_ordseq, "N");
	                        	
	                       		sql2  = " UPDATE TZ_PROJREP SET 									\n";
	                        	sql2 += " 			  retmailing  ='N' 								\n";
	                        	sql2 += " 			, score       = ? 				\n";
	                        	sql2 += " 			, score_mas   = ? 				\n";
	                        	sql2 += " 			, isopenexp   = ? 				\n";
	                        	sql2 += " 			, isret='N' 									\n";
	                        	sql2 += " 			, ldate      = to_char(sysdate,'YYYYMMDDHH24MISS') \n";	//채점일
	                        	sql2 += " 			, correction = empty_clob()                                   \n";	//첨삭지도
	                        	sql2 += " WHERE SUBJ 	= ? 						\n";
	                        	sql2 += "   AND YEAR 	= ? 						\n";
	                        	sql2 += "   AND SUBJSEQ = ? 						\n";
	                        	sql2 += "   AND PROJID 	= ? 						\n";
	                        	sql2 += "   AND GRPSEQ 	= ? 						\n";
	                        	sql2 += "   AND ORDSEQ 	= ? 						\n";
	                        	sql2 += "   AND ISFINAL = 'Y'										\n";
	                        	
	                       		pstmt2 = connMgr.prepareStatement(sql2);
	                       		pstmt2.setInt(1, v_score_mas);
	                       		pstmt2.setInt(2, v_score_mas);
	                       		pstmt2.setString(3, v_isopenexp);
	                       		pstmt2.setString(4, v_subj);
	                       		pstmt2.setString(5, v_year);
	                       		pstmt2.setString(6, v_subjseq);
	                       		pstmt2.setString(7, v_projgrp);
	                       		pstmt2.setInt   (8, v_grpseq);
	                       		pstmt2.setInt   (9, v_ordseq);
	                       		
	                       		isOk = pstmt2.executeUpdate();
	                        	if ( pstmt2 != null ) { pstmt2.close(); }
	                        	
	                            // 컨텐츠를 입력한다.
	                            String sql6 = "select correction from TZ_PROJREP where SUBJ = '" + v_subj + "' and YEAR = '" +v_year + "' AND SUBJSEQ = '" +v_subjseq + "' AND PROJID = '" +v_projgrp + "' AND GRPSEQ = " +v_grpseq + " AND ORDSEQ = " +v_ordseq + " AND ISFINAL = 'Y' ";
	                            connMgr.setOracleCLOB(sql6, v_correction);
	                        	
	                        	v_totalscore += v_score_mas;
                        	}
                        	sql3  = " UPDATE TZ_PROJASSIGN SET 									\n";
                        	sql3 += " 					  totalscore =  ?	\n";
                        	//sql3 += " 					, address 	 =  " + SQLString.Format(v_address)    + " 	\n";
                        	//sql3 += " 					, success 	 =  " + SQLString.Format(v_success)    + " 	\n";
                        	//sql3 += " 					, fail 		 =  " + SQLString.Format(v_fail)       + " 	\n";
                        	sql3 += " 					, review 		 =  empty_clob() 	\n";
                        	sql3 += " 					, ldate      = to_char(sysdate,'YYYYMMDDHH24MISS')	\n";
                        	
                        	sql3 += " WHERE SUBJ 	= ? 							\n";
                        	sql3 += "   AND YEAR 	= ? 							\n";
                        	sql3 += "   AND SUBJSEQ = ? 						\n";
                        	sql3 += "   AND USERID 	= ? 						\n";
                        	sql3 += "   AND GRPSEQ 	= ? 						\n";
                        	
                       		pstmt2 = connMgr.prepareStatement(sql3);
                       		pstmt2.setInt(1, v_totalscore);
                       		pstmt2.setString(2, v_subj);
                       		pstmt2.setString(3, v_year);
                       		pstmt2.setString(4, v_subjseq);
                       		pstmt2.setString(5, v_projgrp);
                       		pstmt2.setInt   (6, v_grpseq);
                            
                       		isOk = pstmt2.executeUpdate();
                            if ( pstmt2 != null ) { pstmt2.close(); }
                            
                            // 컨텐츠를 입력한다.
                            String sql5 = "select review from TZ_PROJASSIGN where SUBJ = '" + v_subj + "' and YEAR = '" +v_year + "' AND SUBJSEQ = '" +v_subjseq + "' AND USERID 	= '" +v_projgrp + "' AND GRPSEQ 	= '" +v_grpseq + "' ";
                            connMgr.setOracleCLOB(sql5, v_review);
                            
                        }
                        // isOk = pstmt2.executeUpdate();
                        // ==  ==  ==  == =과제 점수반영
                        isOk = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.REPORT,v_subj,v_year,v_subjseq,v_projgrp);

                        if ( isOk > 0) { 
                        	connMgr.commit();
                        } else { 
                        	connMgr.rollback();
                        }
                    } catch ( Exception ex ) { 
                        if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
                        ErrorManager.getErrorStackTrace(ex, box, sql3);
                        throw new Exception("sql3 = " + sql3 + "\r\n" + ex.getMessage() );
                    } finally { 
                        if ( ls1 != null )  { try { ls1.close(); } catch ( Exception e1 ) { } }       
                        if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }     
                        if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
                        if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
                    }

                    return isOk;
                }
                 /**
                 교육완료 후 재제출 등록 (답변형 등록)
                 @param box      receive from the form object and session
                 @return int
                   */
                  public int ReportInsertS(RequestBox box) throws Exception { 
                     DBConnectionManager connMgr = null;
                     ListSet ls = null;
                     PreparedStatement pstmt = null;
                     PreparedStatement pstmt2 = null;
                     String sql = "";
                     String sql2 = "";
                     String sql3 = "";
                     int isOk = 0;
                     int isOk1 = 0;
                     int v_max = 0;

                     String  s_userid = box.getSession("userid");			// 사용자아이디
                     String  v_subj = box.getString("p_subj");          // 과정
                     String  v_year = box.getString("p_year");          // 년도
                     String  v_subjseq = box.getString("p_subjseq");       // 과정기수        
                     String  v_grpseq = box.getString("p_grpseq");		// 문제지번호
                     String  v_userid = box.getString("p_userid");		// 배정자 아이디
                     
                     Vector  ordseq	= box.getVector("p_ordseq");		//문제번호
                     Vector  answer = box.getVector("p_answer");		//답변
                     int v_seq = 0;
                     int v_preseq = 0;
                     
                     try { 
                    	 connMgr = new DBConnectionManager();
                         connMgr.setAutoCommit(false);
                    	// 재제출은 seq를 해당 과정,년도,학기,문제지번호,학생의 값을 select해서 max + 1을 해준다. 
                    	sql3 = "  SELECT MAX(SEQ) SEQMAX \n";
                    	sql3 += " FROM TZ_PROJREP \n";
                    	sql3 += " WHERE SUBJ = " + StringManager.makeSQL(v_subj);
                    	sql3 += "   AND YEAR = " + StringManager.makeSQL(v_year);
                    	sql3 += "   AND SUBJSEQ =  " + StringManager.makeSQL(v_subjseq);
                    	sql3 += "   AND GRPSEQ =  " + StringManager.makeSQL(v_grpseq);
                    	sql3 += "   AND PROJID =  " + StringManager.makeSQL(v_userid);
                    	 
                        ls = connMgr.executeQuery(sql3);
                        if ( ls.next() ) { 
                            v_max = ls.getInt(1);
							if ( v_max > 0) { v_seq = v_max + 1; }	//답변한 내용이 있으면 값이 1이상이다. 그러면 +1 시켜준다. 
                            else          { v_seq = 1; 			 }	//답변한 내용이 없으면 값이 안나올지도 모른다. 그러면 1로 넣어준다. 
                        }
                        if ( ls != null )  ls.close();
                        
                        if(v_seq != 1) {	//제출되지 않은 리포트는 isfinal 을 N으로 바꿀수가 없으니 그냥 넘겨야한다.
                        	// 이전 seq 번호를 가지고 해당 seq 의 isfanal 을 N 으로 바꾸어준다.
	                        sql3 = " UPDATE TZ_PROJREP SET 							\n";
	                        sql3 += " 	ISFINAL = 'N'	 							\n";
	                        sql3 += " WHERE SUBJ = " + StringManager.makeSQL(v_subj);
	                        sql3 += "   AND YEAR = " + StringManager.makeSQL(v_year);
	                    	sql3 += "   AND SUBJSEQ =  " + StringManager.makeSQL(v_subjseq);
	                    	sql3 += "   AND GRPSEQ =  " + StringManager.makeSQL(v_grpseq);
	                    	sql3 += "   AND PROJID =  " + StringManager.makeSQL(v_userid);
	                    	sql3 += "   AND SEQ =  " + v_max;
	                        isOk = 0;
	                        isOk = connMgr.executeUpdate(sql3);
                        }
                        
                        sql3 = " UPDATE TZ_PROJassign SET 							\n";
                        sql3 += " 	  totalscore = null	 							\n";
                        sql3 += " 	, ldate = null									\n";                        
                        sql3 += " 	, luserid = null								\n";                        
                        sql3 += " WHERE SUBJ = " + StringManager.makeSQL(v_subj);
                        sql3 += "   AND YEAR = " + StringManager.makeSQL(v_year);
                    	sql3 += "   AND SUBJSEQ =  " + StringManager.makeSQL(v_subjseq);
                    	sql3 += "   AND GRPSEQ =  " + StringManager.makeSQL(v_grpseq);
                    	sql3 += "   AND userid =  " + StringManager.makeSQL(v_userid);
                    	isOk = 0;
                    	isOk = connMgr.executeUpdate(sql3);
                    	
                     // 파일 생성 부분 추가
                     // 주의 : 파일이 생성되지 않는다면 에러메세지 형태로 아래의 부분이 수행되면 안됩니다.
                     	String answerFile = "";
                    	String upfilename = "";
                        String v_currentDate = FormatDate.getDate("yyyyMMddHHmmss");
                        ConfigSet conf = new ConfigSet();
                         
                        upfilename = "_" +v_currentDate + "_" + v_userid ;       // 새로운 파일명을 만든다

             			for ( int j=0; j < answer.size();j++ ) { 
             				answerFile += (String)answer.elementAt(j);
             				 
             			}
             			String str = answerFile; // 문자입력을 받는다. 
             			String newfilename = "Report_file"+ upfilename +".txt";
                         File save_txt = new File(conf.getProperty("dir.upload.report")+ newfilename); // 저장 될 파일명
                         PrintWriter pw = new PrintWriter(new FileWriter(save_txt,true));

                         pw.println(str); // 파일 쓰기과정

                         pw.flush(); // 버퍼에 남아 있을 수 있는 데이터 찌거기 제거.
                         pw.close(); // 파일을 쓰고 나면 끝을 알려야 한다(파일을 다 쓰고 닫기).
                     // -------------

                         
                         
                         sql =  " insert into TZ_PROJREP(seq,subj,year,subjseq,grpseq,ordseq,projid,luserid,ldate, contents, ISFINAL, isret, retmailing ) 	\n";
                         sql += " values ( ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, 'Y','N','N' )		 					\n";
                         pstmt = connMgr.prepareStatement(sql);
                         for ( int i=0; i < answer.size();i++ ) { 
                             String  v_answer = (String)answer.elementAt(i);
                             int     v_ordseq = Integer.parseInt((String)ordseq.elementAt(i));
                             
                             pstmt.setInt   (1,v_seq);
                             pstmt.setString(2,v_subj);
                             pstmt.setString(3,v_year);
                             pstmt.setString(4,v_subjseq);
                             pstmt.setInt   (5,Integer.parseInt(v_grpseq));
                             pstmt.setInt   (6,v_ordseq);
                             pstmt.setString(7,v_userid);
                             pstmt.setString(8,s_userid);
                             pstmt.setString(9,v_answer);
                             isOk = 0;
                             isOk = pstmt.executeUpdate();
                         
                         }
                         
                         // 파일이 생성되면 파일명을 저장하도록 합니다.
                         if ( isOk >0 ) {
             	            sql2  = " UPDATE TZ_PROJASSIGN SET REALREPFILE = ?, UPREPFILE =? 			\n";
             	            sql2 += " 		, repdate = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')				\n";
             	            sql2 += " 		, LUSERID = ?, REALREPFILE2 = ?, UPREPFILE2 =?	\n";
             	            sql2 += " WHERE SUBJ = '" + v_subj + "'\n";
             	            sql2 += "	AND YEAR= '" + v_year + "'\n";
             	            sql2 += "	AND SUBJSEQ= '" + v_subjseq + "'\n";
             	            sql2 += "	AND grpseq= '" + v_grpseq + "'\n";
             	            sql2 += "	AND userid= '" + v_userid + "'\n";
             	            pstmt2 = connMgr.prepareStatement(sql2);
             	            
             	            pstmt2.setString(1, newfilename );
             	            pstmt2.setString(2, newfilename );
             	            pstmt2.setString(3, s_userid );
             	            pstmt2.setString(4, box.getRealFileName("p_file2"));
             	            pstmt2.setString(5, box.getNewFileName("p_file2"));
             	            isOk1 = pstmt2.executeUpdate();
                         }
                         
             			if ( isOk > 0 && isOk1 > 0 ) {  
             				connMgr.commit();
             			} else { 
             				connMgr.rollback();
             			}            
                     } catch ( Exception ex ) { 
                         if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
                         throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
                     } finally { 
                         if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
                         if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
                         if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
                         if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
                         if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
                     }

                     return isOk;
                 }
                  /**
                 교육완료 후 재제출 등록 (답변형 등록)
                 @param box      receive from the form object and session
                 @return int
                   */
                  public int ReportInsertP(RequestBox box) throws Exception { 
                	  DBConnectionManager connMgr = null;
                	  ListSet ls = null;
                	  PreparedStatement pstmt = null;
                	  PreparedStatement pstmt2 = null;
                	  String sql = "";
                	  String sql2 = "";
                	  String sql3 = "";
                	  int isOk = 0;
                	  int isOk1 = 0;
                	  int v_max = 0;
                	  
                	  String  s_userid = box.getSession("userid");			// 사용자아이디
                	  String  v_subj = box.getString("p_subj");          // 과정
                	  String  v_year = box.getString("p_year");          // 년도
                	  String  v_subjseq = box.getString("p_subjseq");       // 과정기수        
                	  String  v_grpseq = box.getString("p_grpseq");		// 문제지번호
                	  String  v_userid = box.getString("p_userid");		// 배정자 아이디
                	  String v_realFileName1      = box.getRealFileName("p_file1");	//서술형 답변 파일
                      String v_newFileName1       = box.getNewFileName ("p_file1");	//서술형 답변파일
                	  Vector  ordseq	= box.getVector("p_ordseq");		//문제번호
                	  Vector  answer = box.getVector("p_answer");		//답변
                	  int v_seq = 0;
                	  int v_preseq = 0;
                	  
                	  try { 
                		  connMgr = new DBConnectionManager();
                		  connMgr.setAutoCommit(false);
                		  // 재제출은 seq를 해당 과정,년도,학기,문제지번호,학생의 값을 select해서 max + 1을 해준다. 
                		  sql3 = "  SELECT MAX(SEQ) SEQMAX \n";
                		  sql3 += " FROM TZ_PROJREP \n";
                		  sql3 += " WHERE SUBJ = " + StringManager.makeSQL(v_subj);
                		  sql3 += "   AND YEAR = " + StringManager.makeSQL(v_year);
                		  sql3 += "   AND SUBJSEQ =  " + StringManager.makeSQL(v_subjseq);
                		  sql3 += "   AND GRPSEQ =  " + StringManager.makeSQL(v_grpseq);
                		  sql3 += "   AND PROJID =  " + StringManager.makeSQL(v_userid);
                		  
//                		  System.out.println("seq 값구하기  sql::>" +sql3);
                		  ls = connMgr.executeQuery(sql3);
                		  if ( ls.next() ) { 
                			  v_max = ls.getInt(1);
                			  if ( v_max > 0) { v_seq = v_max + 1; }	//답변한 내용이 있으면 값이 1이상이다. 그러면 +1 시켜준다. 
                			  else          { v_seq = 1;         }	//답변한 내용이 없으면 값이 안나올지도 모른다. 그러면 1로 넣어준다. 
                		  }
                		  if ( ls != null )  ls.close(); 
                		  if(v_seq != 1) {	//제출되지 않은 리포트는 isfinal 을 N으로 바꿀수가 없으니 그냥 넘겨야한다.
	                		  // 이전 seq 번호를 가지고 해당 seq 의 isfanal 을 N 으로 바꾸어준다. 
	                		  sql3 = " UPDATE TZ_PROJREP SET 							\n";
	                		  sql3 += " 	ISFINAL = 'N'	 							\n";
	                		  sql3 += " WHERE SUBJ = " + StringManager.makeSQL(v_subj);
	                		  sql3 += "   AND YEAR = " + StringManager.makeSQL(v_year);
	                		  sql3 += "   AND SUBJSEQ =  " + StringManager.makeSQL(v_subjseq);
	                		  sql3 += "   AND GRPSEQ =  " + StringManager.makeSQL(v_grpseq);
	                		  sql3 += "   AND PROJID =  " + StringManager.makeSQL(v_userid);
	                		  sql3 += "   AND SEQ =  " + v_max;
	                		  isOk = 0;
	                		  isOk = connMgr.executeUpdate(sql3);
                		  }
                			  sql3 = " UPDATE TZ_PROJassign SET 						\n";
	                		  sql3 += " 	  totalscore = null	 						\n";
	                		  sql3 += " 	, repdate = null	 						\n";                        
	                		  sql3 += " WHERE SUBJ = " + StringManager.makeSQL(v_subj);
	                		  sql3 += "   AND YEAR = " + StringManager.makeSQL(v_year);
	                		  sql3 += "   AND SUBJSEQ =  " + StringManager.makeSQL(v_subjseq);
	                		  sql3 += "   AND GRPSEQ =  " + StringManager.makeSQL(v_grpseq);
	                		  sql3 += "   AND userid =  " + StringManager.makeSQL(v_userid);
	                		  isOk = 0;
	                		  isOk = connMgr.executeUpdate(sql3);

                		  if(isOk > 0 ){      
                			  sql =  " insert into TZ_PROJREP(seq, subj, year, subjseq, grpseq, ordseq, projid, luserid, ldate, ISFINAL, isret, retmailing   ) 	\n";
            	              sql += " values ( ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'),'Y','N','N' )		 					\n";
                	   														
                	             
                	             for ( int i=0; i < ordseq.size();i++ ) {
                	                 int v_ordseq = Integer.parseInt((String)ordseq.elementAt(i));
                	                 pstmt = null;
                	                 pstmt = connMgr.prepareStatement(sql);
                	                 pstmt.setInt   (1, v_seq);
                	                 pstmt.setString(2, v_subj);
                	                 pstmt.setString(3, v_year);
                	                 pstmt.setString(4, v_subjseq);
                	                 pstmt.setInt   (5, Integer.parseInt(v_grpseq));
                	                 pstmt.setInt   (6, v_ordseq);
                	                 pstmt.setString(7, s_userid);
                	                 pstmt.setString(8, s_userid);
                	                 isOk = 0;
                	                 isOk = pstmt.executeUpdate();
                	                 
                	                 if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
                	             
                	             }
                	             if ( isOk >0 ) {
                	 	            sql2  = " UPDATE TZ_PROJASSIGN SET REALREPFILE = ?, UPREPFILE =? 			\n";
                	 	            sql2 += " 		, repdate = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')				\n";
                	 	            sql2 += " 		, LUSERID = ?, REALREPFILE2 = ?, UPREPFILE2 =?  \n";
                	 	            sql2 += " WHERE SUBJ = '" + v_subj + "'\n";
                	 	            sql2 += "	AND YEAR= '" + v_year + "'\n";
                	 	            sql2 += "	AND SUBJSEQ= '" + v_subjseq + "'\n";
                	 	            sql2 += "	AND grpseq= '" + v_grpseq + "'\n";
                	 	            sql2 += "	AND userid= '" + s_userid + "'\n";
                	 	            pstmt2 = connMgr.prepareStatement(sql2);
                	 	            pstmt2.setString(1, v_realFileName1 );
                	 	            pstmt2.setString(2, v_newFileName1  );
                	 	            pstmt2.setString(3, s_userid );
                	 	            pstmt2.setString(4, box.getRealFileName("p_file2"));
                	 	            pstmt2.setString(5, box.getNewFileName("p_file2"));
                	 	            isOk1 = pstmt2.executeUpdate();
                	             }
                		  }
	        	 			if ( isOk > 0 && isOk1 >0) { 
	        	 				connMgr.commit();
	        	 			} else { 
	        	 				connMgr.rollback();
	        	 			}
                	 			
                	  } catch ( Exception ex ) { 
                          if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
                		  throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
                	  } finally { 
                		  if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
                		  if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
                		  if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
                		  if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
                		  if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
                	  }
                	  
                	  return isOk;
                  }
                  
	/**
	 * 수료처리 여부 가져오기(수료처리가 되면 평가를 할 수 없음)
	 * @param box
	 * @return
	 * @throws Exception 
	 */
	public String getStoldYn(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		String sql = "";
		String retVal = "N";
  
		String v_subj = box.getString("p_subj");          // 과정
		String v_year = box.getString("p_year");          // 년도
		String v_subjseq = box.getString("p_subjseq");       // 과정기수        
		String v_userid = box.getString("p_userid");		// 배정자 아이디
  
		try { 
			connMgr = new DBConnectionManager();

			sql = "select decode(count(*), 0, 'N', 'Y') "
				+ "from   tz_stold "
				+ "where  subj = " + StringManager.makeSQL(v_subj)
				+ "and    year = " + StringManager.makeSQL(v_year)
				+ "and    subjseq = " + StringManager.makeSQL(v_subjseq)
				+ "and    userid = " + StringManager.makeSQL(v_userid);
			ls = connMgr.executeQuery(sql);
			
			if (ls.next()) {
				retVal = ls.getString(1);
			}
			
		} catch ( Exception ex ) { 
			if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return retVal;
	}
 
	
    /**
    모사답안 view
    @param box      receive from the form object and session
    @return ProjectData   
    */
     public ArrayList selectReportCopyView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String  sql         = "";
        String  wsql        = "";     
        String  wsql2       = "";          
        DataBox dbox    	= null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_wdata     = box.getString("p_wdata");         // 조회 데이타(출제번호,제출자,일련번호)

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            String[] tokens = v_wdata.split("\\^");
            for ( int i = 0; i<tokens.length; i++ ) { 
                String[] tokens1 = tokens[i].split("\\|");  
                if ( i != 0) wsql += " or ";                 
                wsql  += " (a.grpseq='" +tokens1[0] + "' and userid='" +tokens1[1] + "') \n";              
            }
       		
            sql = " select a.grpseq, userid, get_name(userid) name, repdate, contents, uprepfile, realrepfile, uprepfile2, realrepfile2, upfile_size, contents_byte, copyupnm, copysize, copybyte,  \n"
                + "        retreason, retdate, isret, retuserid \n"    
                + " from TZ_PROJASSIGN a, \n"
                + " (select distinct subj, year, subjseq, grpseq, projid, retreason, retdate, isret, get_name(retuserid) retuserid from TZ_PROJREP b where isfinal = 'Y' and subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "') b \n"
                + " where  a.subj=b.subj and a.year=b.year and a.subjseq=b.subjseq and a.userid=b.projid and a.grpseq=b.grpseq \n" 
                + "   and a.subj='" +v_subj + "' and a.year='" +v_year + "' and a.subjseq='" +v_subjseq + "'   \n"
                + "   and (" +wsql + ")";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox    = ls.getDataBox(); 
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
     모사답안 반려 처리
     @param box      receive from the form object and session
     @return ProjectData   
     */
      public int updateReportJudgeCopy(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;        
         int isOk            = 0;        
         String  sql1        = "";
         String  v_subj      = box.getString("p_subj");          // 과목
         String  v_year      = box.getString("p_year");          // 년도
         String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
         int     v_copycnt   = Integer.parseInt(box.getString("p_copycnt") );       // 모사답안비교갯수
         String  v_grpseq    = "";                               // 출제번호            
         String  v_ordseq    = "";                               // 출제번호   
         String  v_projgrp   = "";                               // 제출자     
         String  v_isret     = "";                               // 반려여부   
         String  v_retreason = "";                               // 반려내용
         String  v_copyupnm  = "";
         String  v_copysize  = "";
         String  v_copybyte  = "";         
                                    
         try {                      
             connMgr = new DBConnectionManager();
                                                
             for ( int i = 0; i<v_copycnt; i++ ) 
             { 
            	 v_grpseq    = box.getString("p_grpseq" +String.valueOf(i));     
                 v_projgrp   = box.getString("p_projid" +String.valueOf(i));     
                 v_isret     = box.getString("p_isret" +String.valueOf(i));      
                 v_retreason = box.getString("p_retreason" +String.valueOf(i));  
                 v_copyupnm = box.getString("p_copyupnm" + String.valueOf(i));
                 v_copysize = box.getString("p_copysize" + String.valueOf(i));
                 v_copybyte = box.getString("p_copybyte" + String.valueOf(i));
                 
                 box.put("p_grpseq",    v_grpseq);   
                 box.put("p_projgrp",   v_projgrp);
                 box.put("p_isret",     v_isret); 
                 box.put("p_retreason", v_retreason);  
                 box.put("p_copyupnm",  v_copyupnm);  
                 box.put("p_copysize",  v_copysize);  
                 box.put("p_copybyte",  v_copybyte);                   
                    
                 if ( v_isret.equals("Y") ) {  
                     // 과제(반려)적용
                     isOk = updateProjectJudge(box);
           
                 } else { 
                     sql1= " update TZ_PROJREP set isret='N', isfinal='Y', retreason='', retdate='', retuserid=''  "
                         + " where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' "
                         + " and   grpseq='" +v_grpseq + "' and projid='" +v_projgrp + "'";
                     
                     isOk = connMgr.executeUpdate(sql1);
                 }
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }  
      
      
      /**
      과제 점수관리
      @param box      receive from the form object and session
      @return int
      */
       public int updateProjectJudge(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;   
          ListSet ls          = null;
          String sql          = "";           
          String sql1         = ""; 
          String sql2         = ""; 
          int isOk            = 0;
          int isOk1           = 0;
          int isOk2           = 0;          
          String  v_subj      = box.getString("p_subj");          // 과목
          String  v_year      = box.getString("p_year");          // 년도
          String  v_subjseq   = box.getString("p_subjseq");       // 과목기수        
          String  v_projgrp   = box.getString("p_projgrp");       
          String  v_retreason = box.getString("p_retreason"); // 반려사유
          String  v_copyupnm  = box.getString("p_copyupnm"); // 파일명
          String  v_copysize  = box.getString("p_copysize"); // 사이즈
          String  v_copybyte  = box.getString("p_copybyte"); // 내용          
          
          int     v_grpseq    = box.getInt("p_grpseq");             

          try { 
              connMgr = new DBConnectionManager();
             		                    
	            sql1  = " UPDATE TZ_PROJASSIGN SET 									\n";
	          	sql1 += " 					  totalscore = 0 						\n";
	          	sql1 += " 					, luserid 	 = " + SQLString.Format(box.getSession("userid")) + " 		\n";
	          	sql1 += " 					, ldate   = to_char(sysdate,'YYYYMMDDHH24MISS')	\n";
	          	sql1 += " 					, copyupnm 	 = " + SQLString.Format(v_copyupnm) + " 		\n";
	          	sql1 += " 					, copysize 	 = " + SQLString.Format(v_copysize) + " 		\n";
	          	sql1 += " 					, copybyte 	 = " + SQLString.Format(v_copybyte) + " 		\n";	          	
	          	sql1 += " WHERE SUBJ 	= '" +v_subj + "' 							\n";
	          	sql1 += "   AND YEAR 	= '" +v_year + "' 							\n";
	          	sql1 += "   AND SUBJSEQ = '" +v_subjseq + "' 						\n";
	          	sql1 += "   AND USERID 	= '" +v_projgrp + "' 						\n";
	          	sql1 += "   AND GRPSEQ 	= '" +v_grpseq + "' 						\n";
	          	
	          	isOk1 = connMgr.executeUpdate(sql1);          	
          	
           		sql2  = " UPDATE TZ_PROJREP SET 									\n";
           		sql2 += " 			, score=0 										\n";
           		sql2 += " 			, score_mas=0 									\n";
           		sql2 += " 			, isret='Y' 									\n";
           		sql2 += " 			, retreason='"+ v_retreason +"' \n";	                       		
           		sql2 += " 			, retdate = to_char(sysdate,'YYYYMMDDHH24MISS') \n";
           		sql2 += " 			, retuserid = '"+box.getSession("userid")+"' 	\n";
           		sql2 += " WHERE SUBJ 	= '" +v_subj + "' 							\n";
           		sql2 += "   AND YEAR 	= '" +v_year + "' 							\n";
           		sql2 += "   AND SUBJSEQ = '" +v_subjseq + "' 						\n";
           		sql2 += "   AND PROJID 	= '" +v_projgrp + "' 						\n";
           		sql2 += "   AND GRPSEQ 	= '" +v_grpseq + "' 						\n";
           		sql2 += "   AND ISFINAL = 'Y'										\n";
           		
                isOk2 = connMgr.executeUpdate(sql2);                               
                
                if(isOk1 * isOk2 > 0)           
            	  isOk = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.REPORT,v_subj,v_year,v_subjseq,v_projgrp);              

          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex, box, sql2);
              throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
          } finally {       
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return isOk;
      }   
       
       
       /**
       모사답안비교 체크 등록 처리 
       @param box      receive from the form object and session
       @return int
       */
        public int updateReportCopyRegist(RequestBox box) throws Exception { 
           DBConnectionManager	connMgr	= null;
           PreparedStatement   pstmt   = null;          
           int isOk            = 0;
           String sql          = "";
           
           String  v_subj      = box.getString("p_subj");          // 과목
           String  v_year      = box.getString("p_year");          // 년도
           String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
           int     v_listcnt   = box.getInt("p_listcnt");          // 전체리스트갯수        
           String  v_copyupnm  = "";
           String  v_copysize  = "";
           String  v_copybyte  = "";
           String  v_projrep   = "";

           try { 
               connMgr = new DBConnectionManager();
              
               for ( int i = 0; i < v_listcnt; i++ ) { 
                   String[] v_copy = new String[3];
                   v_copyupnm = box.getString("p_copyupnm" + i);
                   v_copysize = box.getString("p_copysize" + i);
                   v_copybyte = box.getString("p_copybyte" + i);
                   v_projrep  = box.getString("p_projrep" + i);
                   String[] tokens = v_projrep.split("\\|");    // 개별 정보
                   
                   // 첨부파일
                   if ( !v_copyupnm.equals("")) v_copy[0] = "Y";                            
                   // 파일사이즈
                   if ( !v_copysize.equals("")) v_copy[1] = "Y"; 
                   // 내용바이트수
                   if ( !v_copybyte.equals("")) v_copy[2] = "Y";   
       
                   sql = " update tz_projassign set copyupnm=?, copysize=?, copybyte=?  "
                       + " where subj=? and year=? and subjseq=? and userid=? and grpseq=? ";
       
                   pstmt = connMgr.prepareStatement(sql);
                   pstmt.setString(1, v_copy[0]);
                   pstmt.setString(2, v_copy[1]);
                   pstmt.setString(3, v_copy[2]);
                   pstmt.setString(4, v_subj   );
                   pstmt.setString(5, v_year   );
                   pstmt.setString(6, v_subjseq);
                   pstmt.setString(7, tokens[0]);
                   pstmt.setInt   (8, Integer.parseInt(tokens[1]));          
                   isOk = pstmt.executeUpdate(); 
                   if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
               }
           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
           } finally { 
        	   if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }

           return isOk;        
       }             
      
     

}