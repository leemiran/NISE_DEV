// **********************************************************
//  1. 제      목: COMPLETE STATUS ADMIN BEAN
//  2. 프로그램명: CompleteStatusAdminBean.java
//  3. 개      요: 수료 현황 관리자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 박진희 2003. 8. 21
//  7. 수      정: 2005. 11. 25 이경배 
// **********************************************************
package com.ziaan.complete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.common.GetCodenm;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.ManagerAdminBean;

public class CompleteStatusAdminBean { 
    private ConfigSet config;
    private int 		row;

    public CompleteStatusAdminBean() { 
        try { 
            config 	= new ConfigSet();
            row 	= Integer.parseInt(config.getProperty("page.manage.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    
    /**
    수료명단 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectCompleteMemberList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls1             = null;
        ListSet             ls2             = null;
        ArrayList           list1           = null;
        ArrayList           list2           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        CompleteStatusData1  data1           = null;
        CompleteStatusData1  data2           = null;
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_Bcourse       = ""; // 이전코스
        String              v_course        = ""; // 현재코스
        String              v_Bcourseseq    = ""; // 이전코스기수
        String              v_courseseq     = ""; // 현재코스기수
        int                 v_pageno        = box.getInt          ("p_pageno"              );
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL"); // 교육그룹
        String              ss_gyear        = box.getStringDefault("s_gyear"        , "ALL"); // 년도
        String              ss_grseq        = box.getStringDefault("s_grseq"        , "ALL"); // 교육기수
        String              ss_uclass       = box.getStringDefault("s_upperclass"   , "ALL"); // 과목분류
        String              ss_mclass       = box.getStringDefault("s_middleclass"  , "ALL"); // 과목분류
        String              ss_lclass       = box.getStringDefault("s_lowerclass"   , "ALL"); // 과목분류
        String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL"); // 과목&코스
        String              ss_subjseq      = box.getStringDefault("s_subjseq"      , "ALL"); // 과목 기수
        String              ss_company      = box.getStringDefault("s_company"      , "ALL"); // 회사
        String              ss_edustart     = box.getString       ("s_edustart"            ); // 교육시작일
        String              ss_eduend       = box.getString       ("s_eduend"              ); // 교육종료일
//        String              ss_selgubun     = box.getString       ("s_selgubun"            ); // 직군별:JIKUN,직급별:JIKUP,사업부별:GPM
//        String              ss_seltext      = box.getStringDefault("s_seltext"      , "ALL"); // 검색분류별 검색내용
//        String              ss_seldept      = box.getStringDefault("s_seldept"      , "ALL"); // 사업부별 부서 검색내용
//        String              ss_action       = box.getString       ("s_action"              );
        
        String              v_orderColumn   = box.getString       ("p_orderColumn"         ); // 정렬할 컬럼명
        String              v_orderType     = box.getString       ("p_orderType"           ); // 정렬할 순서

//        ManagerAdminBean    bean            = null;
//        String              v_sql_add       = "";
//        String              v_userid        = box.getSession("userid");
//        String              s_gadmin        = box.getSession("gadmin");
        
        int                 total_page_count= 0;                                              // 전체 페이지 수
        int                 total_row_count = 0;                                              // 전체 row 수

        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();
            list2   = new ArrayList();

            sbSQL.append(" select  C.grseq                      \n")
                 .append("     ,   C.course                     \n")
                 .append("     ,   C.cyear                      \n")
                 .append("     ,   C.courseseq                  \n")
                 .append("     ,   C.coursenm                   \n")
                 .append("     ,   C.subj                       \n")
                 .append("     ,   C.year                       \n")
                 .append("     ,   C.subjnm                     \n")
                 .append("     ,   C.subjseq                    \n")
                 .append("     ,   c.subjseqgr                  \n")
                 .append("     ,   A.serno                      \n")
                 .append("     ,   C.isonoff                    \n")
                 .append("     ,   b.hometel                    \n")
                 .append("     ,   b.handphone                  \n")
                 .append("     ,   fn_crypt('2', b.birth_date, 'knise') birth_date                      \n")
                 .append("     ,   B.userid                     \n")
                 .append("     ,   B.name                       \n")
                 .append("     ,   C.edustart                   \n")
                 .append("     ,   C.eduend                     \n")
                 .append("     ,   A.tstep                      \n")
                 .append("     ,   A.avtstep                    \n")
                 .append("     ,   C.biyong                     \n")
                 .append("     ,   C.goyongpricemajor           \n")
                 .append("     ,   C.goyongpriceminor           \n")
                 .append("     ,   C.isgoyong                   \n");
            
            if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                sbSQL.append("     ,   A.avreport   report          \n")
                     .append("     ,   A.avact      act             \n")
                     .append("     ,   A.avmtest    mtest           \n")
                     .append("     ,   A.avftest    ftest           \n")
                     .append("     ,   A.avetc1     etc1            \n")
                     .append("     ,   A.avetc2     etc2            \n")
                     .append("     ,   A.avhtest    htest           \n");
            } else {                                                   // 가중치비적용
                sbSQL.append("     ,   A.report     report          \n")
                     .append("     ,   A.act        act             \n")
                     .append("     ,   A.mtest      mtest           \n")
                     .append("     ,   A.ftest      ftest           \n")
                     .append("     ,   A.etc1       etc1            \n")
                     .append("     ,   A.etc2       etc2            \n")
                     .append("     ,   A.htest      htest           \n");
            }
            
            sbSQL.append("     ,    A.study_count                           \n")
            	 .append("     ,    A.study_time                            \n")
            	 .append("     ,    A.score                                 \n")
                 .append("     ,    A.isgraduated                           \n")
                 .append("     ,    B.email                                 \n")
                 .append("     ,    C.place                                 \n")
                 .append("     ,    get_codenm('0028',a.notgraduetc) codenm \n")
                 .append("     ,    get_compnm(b.comp) as companynm         \n")
                 .append("     ,    position_nm as deptnm        			\n")
                 .append("     ,    lvl_nm as jikwinm  						\n")
                 .append(" from     TZ_STOLD        A                       \n")
                 .append("     ,    TZ_MEMBER       B                       \n")
                 .append("     ,    VZ_SCSUBJSEQ    C                       \n")
                 //.append(" where    c.isclosed = 'Y'                        \n")
                 .append(" where    1=1				                        \n");
            
            if ( !ss_grcode.equals("ALL") ) { 
                 sbSQL.append(" and C.grcode        = " + SQLString.Format(ss_grcode    ) + "   \n");
            }
            
            if ( !ss_gyear.equals("ALL") ) { 
                sbSQL.append(" and C.gyear        = " + SQLString.Format(ss_gyear    ) + "   \n");
            }

            if ( !ss_grseq.equals("ALL") ) { 
                 sbSQL.append(" and C.grseq         = " + SQLString.Format(ss_grseq    ) + "   \n");
            }
            
            if ( !ss_uclass.equals("ALL") ) { 
                 sbSQL.append(" and C.scupperclass  = " + SQLString.Format(ss_uclass    ) + "   \n");
            }
            
            if ( !ss_mclass.equals("ALL") ) { 
                 sbSQL.append(" and C.scmiddleclass = " + SQLString.Format(ss_mclass    ) + "   \n");
            }
            
            if ( !ss_lclass.equals("ALL") ) { 
                 sbSQL.append(" and C.sclowerclass  = " + SQLString.Format(ss_lclass    ) + "   \n");
            }

            if ( !ss_subjcourse.equals("ALL") ) { 
                 sbSQL.append(" and C.scsubj        = " + SQLString.Format(ss_subjcourse) + "   \n");
            }
            
            if ( !ss_subjseq.equals("ALL") ) { 
                 sbSQL.append(" and C.scsubjseq     = " + SQLString.Format(ss_subjseq   ) + "   \n");
            }
            
            if ( !ss_edustart.equals("") ) { 
                 sbSQL.append(" and C.edustart     >= " + SQLString.Format(ss_edustart  ) + "   \n");
            }
            
            if ( !ss_eduend.equals("") ) { 
                 sbSQL.append(" and C.eduend       <= " + SQLString.Format(ss_eduend    ) + "   \n");
            }
            
            if ( !ss_company.equals("ALL") ) { 
          	   sbSQL.append(" and B.comp =" +StringManager.makeSQL(ss_company) + " \n");
             }

/*
                // 부서장일경우
                if ( s_gadmin.equals("K7") ) { 
                    bean        = new ManagerAdminBean();
                    v_sql_add   = bean.getManagerDept(v_userid, s_gadmin);
                    
                    if ( !v_sql_add.equals("") ) 
                        sbSQL.append(" and B.comp in " + v_sql_add; // 관리부서검색조건쿼리
                }

                if ( ss_selgubun.equals("JIKUN") && !ss_seltext.equals("ALL"))      {   // 직군별
                     sbSQL.append(" and B.jikun = " + SQLString.Format(ss_seltext);
                } else if ( ss_selgubun.equals("JIKWI") && !ss_seltext.equals("ALL") ) { // 직위별
                     sbSQL.append(" and B.jikwi = " + SQLString.Format(ss_seltext);
                } else if ( ss_selgubun.equals("GPM") && !ss_seltext.equals("ALL") ) {    // 사업부별
                     sbSQL.append(" and B.comp like " + SQLString.Format(GetCodenm.get_compval(ss_seltext));
                    if ( !ss_seldept.equals("ALL") ) { 
                     sbSQL.append(" and B.comp like " + SQLString.Format(GetCodenm.get_compval(ss_seldept));
                     }
                }
*/                
            
            sbSQL.append(" and  A.userid            = B.userid                                          \n")        
                 .append(" and  A.subj              = C.subj                                            \n")
                 .append(" and  A.year              = C.year                                            \n")
                 .append(" and  A.subjseq           = C.subjseq                                         \n");

            if ( v_orderColumn.equals("") ) { 
                sbSQL.append(" order by C.course, C.cyear, C.courseseq, A.userid, C.subj, C.subjseq, B.userid    \n");
           } else { 
                sbSQL.append(" order by " + v_orderColumn + v_orderType + "                            \n");
           }
            System.out.println("sbSQL.toString()===================="+sbSQL.toString());
            ls1 = connMgr.executeQuery(sbSQL.toString());

            ls1.setPageSize     ( row     );               // 페이지당 row 갯수를 세팅한다
            ls1.setCurrentPage  ( v_pageno);               // 현재페이지번호를 세팅한다.
            total_page_count        = ls1.getTotalPage();  // 전체 페이지 수를 반환한다
            total_row_count         = ls1.getTotalCount(); // 전체 row 수를 반환한다

            while ( ls1.next() ) { 
                data1   = new CompleteStatusData1();
                
                data1.setGrseq      ( ls1.getString("grseq"         ) );
                data1.setCourse     ( ls1.getString("course"        ) );
                data1.setCyear      ( ls1.getString("cyear"         ) );
                data1.setCourseseq  ( ls1.getString("courseseq"     ) );
                data1.setCoursenm   ( ls1.getString("coursenm"      ) );
                data1.setSubj       ( ls1.getString("subj"          ) );
                data1.setYear       ( ls1.getString("year"          ) );
                data1.setSubjseq    ( ls1.getString("subjseq"       ) );
                data1.setSubjseqgr  ( ls1.getString("subjseqgr"     ) );
                data1.setSubjnm     ( ls1.getString("subjnm"        ) );
                data1.setIsonoff    ( ls1.getString("isonoff"       ) );
                data1.setHometel    ( ls1.getString("hometel"       ) );
                data1.setHandphone  ( ls1.getString("handphone"     ) );
                data1.setUserid     ( ls1.getString("userid"        ) );
                data1.setName       ( ls1.getString("name"          ) );
                data1.setEdustart   ( ls1.getString("edustart"      ) );
                data1.setEduend     ( ls1.getString("eduend"        ) );
                data1.setTstep      ( ls1.getDouble("tstep"         ) );
                data1.setAvtstep    ( ls1.getDouble("avtstep"       ) );
                data1.setMtest      ( ls1.getDouble("mtest"         ) );
                data1.setFtest      ( ls1.getDouble("ftest"         ) );
                data1.setHtest      ( ls1.getDouble("htest"         ) );  // 2005.9.11 by정은년 (왜 이게 엄엇지??)
                
                data1.setReport     ( ls1.getDouble("report"        ) );
                data1.setAct        ( ls1.getDouble("act"           ) );
                data1.setEtc1       ( ls1.getDouble("etc1"          ) );

                data1.setStudy_count( ls1.getInt   ("study_count"   ) );
                data1.setStudy_time ( ls1.getInt   ("study_time"    ) );
                
                data1.setScore      ( ls1.getDouble("score"         ) );
                data1.setIsgraduated( ls1.getString("isgraduated"   ) );
                data1.setEmail      ( ls1.getString("email"         ) );
                data1.setPlace      ( ls1.getString("place"         ) );
                data1.setbirth_date      ( ls1.getString("birth_date"         ) );
                data1.setSerno      ( ls1.getString("serno"         ) );
                data1.setCodenm     ( ls1.getString("codenm"        ) );
                data1.setBiyong     ( ls1.getString("biyong"        ) );
                data1.setGoyongpricemajor     ( ls1.getString("goyongpricemajor"        ) );
                data1.setGoyongpriceminor     ( ls1.getString("goyongpriceminor"        ) );
                data1.setIsGoyong     ( ls1.getString("isgoyong"        ) );
                data1.setDispnum    ( total_row_count - ls1.getRowNum() + 1 );
                data1.setTotalPageCount ( total_page_count            );
                data1.setRowCount       ( row                         );

                data1.setCompanynm  ( ls1.getString("companynm") );
                data1.setDeptnm     ( ls1.getString("deptnm")    );
                data1.setJikwinm    ( ls1.getString("jikwinm")   );
                
                list1.add(data1);
            }
            
            for ( int i = 0 ; i < list1.size() ; i++ ) { 
                data2       =   (CompleteStatusData1)list1.get(i);
                v_course    =   data2.getCourse();
                v_courseseq =   data2.getCourseseq();
                
                sbSQL.setLength(0);
                
                if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq)) ) { 
                    sbSQL.append(" select  count(A.subj) cnt            \n")
                         .append(" from    VZ_SCSUBJSEQ    A            \n")
                         .append("     ,   TZ_STOLD        B            \n")
                         .append("     ,   TZ_MEMBER       C            \n")
                         .append(" where   A.subj      = B.subj         \n")
                         .append(" and     A.year      = B.year         \n")
                         .append(" and     A.subjseq   = B.subjseq      \n")
                         .append(" and     B.userid    = C.userid       \n");
               
                    if ( !ss_grcode.equals("ALL") ) { 
                        sbSQL.append(" and A.grcode         = " + SQLString.Format(ss_grcode        ) + "   \n");
                    }
                   
                    if ( !ss_grseq.equals("ALL") ) { 
                        sbSQL.append(" and A.grseq          = " + SQLString.Format(ss_grseq         ) + "   \n");
                    }
                   
                    if ( !ss_uclass.equals("ALL") ) { 
                        sbSQL.append(" and A.scupperclass   = " + SQLString.Format(ss_uclass        ) + "   \n");
                    }
                   
                    if ( !ss_mclass.equals("ALL") ) { 
                        sbSQL.append(" and A.scmiddleclass  = " + SQLString.Format(ss_mclass        ) + "   \n");
                    }
                   
                    if ( !ss_lclass.equals("ALL") ) { 
                        sbSQL.append(" and A.sclowerclass   = " + SQLString.Format(ss_lclass        ) + "   \n");
                    }

                    if ( !ss_subjcourse.equals("ALL") ) { 
                        sbSQL.append(" and A.scsubj         = " + SQLString.Format(ss_subjcourse    ) + "   \n");
                    }
                   
                    if ( !ss_subjseq.equals("ALL") ) { 
                        sbSQL.append(" and A.scsubjseq      = " + SQLString.Format(ss_subjseq       ) + "   \n");
                    }
                   
/*                        
                       if ( !ss_company.equals("ALL") ) { 
                           sbSQL.append(" and C.comp like '" +GetCodenm.get_compval(ss_company) + "'";
                       }
*/                        
                   
                    // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                    if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                        sbSQL.append(" and A.gyear          = " + SQLString.Format(ss_gyear         ) + "   \n");
                    }
                   
/*    
                       // 부서장일경우
                       if ( s_gadmin.equals("K7") ) { 
                           if ( !v_sql_add.equals("")) sql2 += " and C.comp in " + v_sql_add;      // 관리부서검색조건쿼리
                       }
    
                       if ( ss_selgubun.equals("JIKUN") && !ss_seltext.equals("ALL"))          {   // 직군별
                           sql2 += " and C.jikun = " +SQLString.Format(ss_seltext);
                       } else if ( ss_selgubun.equals("JIKWI") && !ss_seltext.equals("ALL") )  {   // 직위별
                           sql2 += " and C.jikwi = " +SQLString.Format(ss_seltext);
                       } else if ( ss_selgubun.equals("GPM") && !ss_seltext.equals("ALL") )    {   // 사업부별
                           sql2 += " and C.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seltext));
                           if ( !ss_seldept.equals("ALL") ) { 
                               sql2 += " and C.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seldept));
                           }
                       }
*/                        
                    
                    sbSQL.append(" and A.course         = " + SQLString.Format(v_course     ) + "   \n")     
                         .append(" and A.courseseq      = " + SQLString.Format(v_courseseq  ) + "   \n");
                         
                    ls2     = connMgr.executeQuery(sbSQL.toString());
                    
                    if ( ls2.next() ) { 
                        data2.setRowspan    ( ls2.getInt("cnt") );
                        data2.setIsnewcourse( "Y"               );
                    }
                } else { 
                    data2.setRowspan(0);
                    data2.setIsnewcourse("N");
                }
                
                v_Bcourse       = v_course;
                v_Bcourseseq    = v_courseseq;
                
                list2.add(data2);
                
                if ( ls2 != null ) { 
                    try { 
                        ls2.close(); 
                    } catch ( Exception e ) { } 
                }
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
            
            if ( ls2 != null ) { 
                try { 
                    ls2.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
        
        return list2;        
    }

     
    /**
    수료자 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectCompleteRosterList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 	= null;
        ListSet 			ls1         = null;
        ListSet 			ls2         = null;
        ArrayList 			list1     	= null;
        ArrayList 			list2     	= null;
        String 				sql1        = "";
        String 				sql2        = "";
        
        CompleteStatusData 	data1		= null;
        CompleteStatusData 	data2		= null;
        
        String  			v_Bcourse   = ""; // 이전코스
        String  			v_course    = ""; // 현재코스
        String  			v_Bcourseseq= ""; // 이전코스기수
        String  			v_courseseq = ""; // 현재코스기수
        
        int 				v_pageno    = box.getInt("p_pageno");
        String              ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" );  // 교육그룹
        String              ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // 년도
        String              ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // 교육기수
        String              ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // 과목분류
        String              ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // 과목분류
        String              ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // 과목분류
        String              ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // 과목&코스
        String              ss_subjseq  = box.getStringDefault  ( "s_subjseq"       , "ALL" );  // 과목 기수
        String              ss_company  = box.getStringDefault  ( "s_company"       , "ALL" );  // 회사
        String              ss_edustart = box.getStringDefault  ( "s_edustart"      , "ALL" );  // 교육시작일
        String              ss_eduend   = box.getStringDefault  ( "s_eduend"        , "ALL" );  // 교육종료일
        String              ss_selgubun = box.getString         ( "s_selgubun"              );  // 직군별:JIKUN,직급별:JIKUP,사업부별:GPM
        String              ss_seltext  = box.getStringDefault  ( "s_seltext"       , "ALL" );  // 검색분류별 검색내용
        String              ss_seldept  = box.getStringDefault  ( "s_seldept"       , "ALL" );  // 사업부별 부서 검색내용
        String              ss_action   = box.getString         ( "s_action"                );
        String              v_orderColumn= box.getString        ( "p_orderColumn"           );  // 정렬할 컬럼명

        ManagerAdminBean 	bean 		= null;
        String  			v_sql_add   = "";
        String  			v_userid    = box.getSession("userid");
        String  			s_gadmin    = box.getSession("gadmin");

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 	= new ArrayList();
                list2 	= new ArrayList();

                // select grseq,course,cyear,courseseq,coursenm,subj,year,subjnm,subjseq,isonoff,compnm,
                // jikwinm,userid,cono,name,edustart,eduend,score,isgraduated,email,ismailing
                sql1 = "select C.grseq,C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq,";
                sql1 += "C.isonoff,get_compnm(B.comp,3,5) compnm,get_jikwinm(B.jikwi,B.comp) jikwinm,get_jikupnm(B.jikup,B.comp) jikupnm,";
                sql1 += "B.userid,B.cono,B.name,C.edustart,C.eduend,A.score,A.isgraduated,B.email,B.ismailing ";
                sql1 += "from TZ_STOLD A,TZ_MEMBER B,VZ_SCSUBJSEQ C where A.isgraduated='Y' and  c.isclosed = 'Y' ";
                if ( !ss_grcode.equals("ALL") ) { 
                    sql1 += " and C.grcode = " +SQLString.Format(ss_grcode);
                }
                if ( !ss_grseq.equals("ALL") ) { 
                    sql1 += " and C.grseq = " +SQLString.Format(ss_grseq);
                }
                
                if ( !ss_uclass.equals("ALL") ) { 
                    sql1 += " and C.scupperclass = " +SQLString.Format(ss_uclass);
                }
                if ( !ss_mclass.equals("ALL") ) { 
                    sql1 += " and C.scmiddleclass = " +SQLString.Format(ss_mclass);
                }
                if ( !ss_lclass.equals("ALL") ) { 
                    sql1 += " and C.sclowerclass = " +SQLString.Format(ss_lclass);
                }

                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1 += " and C.scsubj = " +SQLString.Format(ss_subjcourse);
                }
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql1 += " and C.scsubjseq = " +SQLString.Format(ss_subjseq);
                }
                if ( !ss_company.equals("ALL") ) { 
                    sql1 += " and B.comp like '" +GetCodenm.get_compval(ss_company) + "'";
                }
                if ( !ss_edustart.equals("ALL") ) { 
                    sql1 += " and C.edustart >= " +SQLString.Format(ss_edustart);
                }
                if ( !ss_eduend.equals("ALL") ) { 
                    sql1 += " and C.eduend <= " +SQLString.Format(ss_eduend);
                }
                // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                    sql1 += " and C.gyear = " +SQLString.Format(ss_gyear);
                }

                // 부서장일경우
                if ( s_gadmin.equals("K7") ) { 
                    bean 		= new ManagerAdminBean();
                    v_sql_add   = bean.getManagerDept(v_userid, s_gadmin);
                    
                    if ( !v_sql_add.equals("") ) 
                    	sql1 += " and B.comp in " + v_sql_add;       // 관리부서검색조건쿼리
                }

                if ( ss_selgubun.equals("JIKUN") && !ss_seltext.equals("ALL") )      	{  // 직군별
                    sql1 += " and B.jikun = " +SQLString.Format(ss_seltext);
                } else if ( ss_selgubun.equals("JIKWI") && !ss_seltext.equals("ALL") ) 	{  // 직위별
                    sql1 += " and B.jikwi = " +SQLString.Format(ss_seltext);
                } else if ( ss_selgubun.equals("GPM") && !ss_seltext.equals("ALL") ) 	{  // 사업부별
                    sql1 += " and B.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seltext));
                    if ( !ss_seldept.equals("ALL") ) { 
                        sql1 += " and B.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seldept));
                     }
                }
                
                sql1 += " and A.userid=B.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq ";
                
                if ( !v_orderColumn.equals("") ) { 
                    v_orderColumn = "B." +v_orderColumn;
                    sql1 += " order by C.course,C.cyear,C.courseseq,C.subj,C.year,C.subjseq," +v_orderColumn;
                } else { 
                    sql1 += " order by C.course,C.cyear,C.courseseq,C.subj,C.year,C.subjseq,B.userid ";
                }

                ls1 = connMgr.executeQuery(sql1);

                ls1.setPageSize		( row		);          	// 페이지당 row 갯수를 세팅한다
                ls1.setCurrentPage	( v_pageno	);          	// 현재페이지번호를 세팅한다.
                int total_page_count 	= ls1.getTotalPage();  	// 전체 페이지 수를 반환한다
                int total_row_count 	= ls1.getTotalCount();  // 전체 row 수를 반환한다

                while ( ls1.next() ) { 
                    data1 = new CompleteStatusData();
                    
                    data1.setGrseq          ( ls1.getString("grseq")        );
                    data1.setCourse         ( ls1.getString("course")       );
                    data1.setCyear          ( ls1.getString("cyear")        );
                    data1.setCourseseq      ( ls1.getString("courseseq")    );
                    data1.setCoursenm       ( ls1.getString("coursenm")     );
                    data1.setSubj           ( ls1.getString("subj")         );
                    data1.setYear           ( ls1.getString("year")         );
                    data1.setSubjseq        ( ls1.getString("subjseq")      );
                    data1.setSubjnm         ( ls1.getString("subjnm")       );
                    data1.setIsonoff        ( ls1.getString("isonoff")      );
                    data1.setCompnm         ( ls1.getString("compnm")       );
                    data1.setJikwinm        ( ls1.getString("jikwinm")      );
                    data1.setJikupnm        ( ls1.getString("jikupnm")      );
                    data1.setUserid         ( ls1.getString("userid")       );
                    data1.setCono           ( ls1.getString("cono")         );
                    data1.setName           ( ls1.getString("name")         );
                    data1.setEdustart       ( ls1.getString("edustart")     );
                    data1.setEduend         ( ls1.getString("eduend")       );
                    data1.setScore          ( ls1.getInt("score")           );
                    data1.setIsgraduated    ( ls1.getString("isgraduated")  );
                    data1.setEmail          ( ls1.getString("email")        );
                    data1.setIsmailing      ( ls1.getString("ismailing")    );
                    data1.setDispnum        ( total_row_count - ls1.getRowNum() + 1);
                    data1.setTotalPageCount ( total_page_count              );
                    data1.setRowCount       ( row                           );
                    
                    list1.add(data1);
                }
                
                for ( int i = 0 ; i < list1.size() ; i++ ) { 
                    data2     	= (CompleteStatusData)list1.get(i);
                    v_course    = data2.getCourse();
                    v_courseseq = data2.getCourseseq();
                    
                    if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq)) ) { 
                        sql2 = "select count(A.subj) cnt from VZ_SCSUBJSEQ A,TZ_STOLD B,TZ_MEMBER C ";
                        sql2 += "where A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq ";
                        sql2 += "and B.isgraduated='Y' and B.userid=C.userid ";
                        
                        if ( !ss_grcode.equals("ALL") ) { 
                            sql2 += " and A.grcode = " +SQLString.Format(ss_grcode);
                        }
                        
                        if ( !ss_grseq.equals("ALL") ) { 
                            sql2 += " and A.grseq = " +SQLString.Format(ss_grseq);
                        }
                        
                        if ( !ss_uclass.equals("ALL") ) { 
                            sql2 += " and A.scupperclass = " +SQLString.Format(ss_uclass);
                        }
                        
                        if ( !ss_mclass.equals("ALL") ) { 
                            sql2 += " and A.scmiddleclass = " +SQLString.Format(ss_mclass);
                        }
                        
                        if ( !ss_lclass.equals("ALL") ) { 
                            sql2 += " and A.sclowerclass = " +SQLString.Format(ss_lclass);
                        }

                        if ( !ss_subjcourse.equals("ALL") ) { 
                            sql2 += " and A.scsubj = " +SQLString.Format(ss_subjcourse);
                        }
                        
                        if ( !ss_subjseq.equals("ALL") ) { 
                            sql2 += " and A.scsubjseq = " +SQLString.Format(ss_subjseq);
                        }
                        
                        if ( !ss_company.equals("ALL") ) { 
                            sql2 += " and C.comp like '" +GetCodenm.get_compval(ss_company) + "'";
                        }
                        
                        if ( !ss_edustart.equals("ALL") ) { 
                            sql2 += " and A.edustart >= " +SQLString.Format(ss_edustart);
                        }
                        
                        if ( !ss_eduend.equals("ALL") ) { 
                            sql2 += " and A.eduend <= " +SQLString.Format(ss_eduend);
                        }
                        
                        // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                        if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                            sql2 += " and A.gyear = " +SQLString.Format(ss_gyear);
                        }

                        // 부서장일경우
                        if ( s_gadmin.equals("K7") ) { 
                            if ( !v_sql_add.equals("")) sql2 += " and C.comp in " + v_sql_add;		// 관리부서검색조건쿼리
                        }

                        if ( ss_selgubun.equals("JIKUN") && !ss_seltext.equals("ALL"))      	{  	// 직군별
                            sql2 += " and C.jikun = " +SQLString.Format(ss_seltext);
                        } else if ( ss_selgubun.equals("JIKWI") && !ss_seltext.equals("ALL") ) 	{  	// 직위별
                            sql2 += " and C.jikwi = " +SQLString.Format(ss_seltext);
                        } else if ( ss_selgubun.equals("GPM") && !ss_seltext.equals("ALL") ) 	{  	// 사업부별
                            sql2 += " and C.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seltext));
                            if ( !ss_seldept.equals("ALL") ) { 
                                sql2 += " and C.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seldept));
                             }
                        }
                        
                        sql2 += "and A.course = " +SQLString.Format(v_course) + " and A.courseseq = " +SQLString.Format(v_courseseq);

                        ls2 = connMgr.executeQuery(sql2);
                        
                        if ( ls2.next() ) { 
                            data2.setRowspan( ls2.getInt("cnt") );
                            data2.setIsnewcourse("Y");
                        }
                        
                        if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    } else { 
                        data2.setRowspan(0);
                        data2.setIsnewcourse("N");
                    }
                    
                    v_Bcourse   	= v_course;
                    v_Bcourseseq	= v_courseseq;
                    
                    list2.add(data2);
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
    미수료자 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectNoneCompleteRosterList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 	= null;
        ListSet 			ls1         = null;
        ListSet 			ls2         = null;
        ArrayList 			list1     	= null;
        ArrayList 			list2     	= null;
        String 				sql1        = "";
        String 				sql2        = "";
        
        CompleteStatusData 	data1		= null;
        CompleteStatusData 	data2		= null;
        
        String  			v_Bcourse   = ""; // 이전코스
        String  			v_course    = ""; // 현재코스
        String  			v_Bcourseseq= ""; // 이전코스기수
        String  			v_courseseq = ""; // 현재코스기수
        
        int 				v_pageno        = box.getInt            ( "p_pageno"                );
        String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" ); // 교육그룹
        String              ss_gyear        = box.getStringDefault  ( "s_gyear"         , "ALL" ); // 년도
        String              ss_grseq        = box.getStringDefault  ( "s_grseq"         , "ALL" ); // 교육기수
        String              ss_uclass       = box.getStringDefault  ( "s_upperclass"    , "ALL" ); // 과목분류
        String              ss_mclass       = box.getStringDefault  ( "s_middleclass"   , "ALL" ); // 과목분류
        String              ss_lclass       = box.getStringDefault  ( "s_lowerclass"    , "ALL" ); // 과목분류
        String              ss_subjcourse   = box.getStringDefault  ( "s_subjcourse"    , "ALL" ); // 과목&코스
        String              ss_subjseq      = box.getStringDefault  ( "s_subjseq"       , "ALL" ); // 과목 기수
        String              ss_company      = box.getStringDefault  ( "s_company"       , "ALL" ); // 회사
        String              ss_edustart     = box.getStringDefault  ( "s_edustart"      , "ALL" ); // 교육시작일
        String              ss_eduend       = box.getStringDefault  ( "s_eduend"        , "ALL" ); // 교육종료일
        String              ss_selgubun     = box.getString         ( "s_selgubun"              ); // 직군별:JIKUN,직급별:JIKUP,사업부별:GPM
        String              ss_seltext      = box.getStringDefault  ( "s_seltext"       , "ALL" ); // 검색분류별 검색내용
        String              ss_seldept      = box.getStringDefault  ( "s_seldept"       , "ALL" ); // 사업부별 부서 검색내용
        String              ss_action       = box.getString         ( "s_action"                );
        String              v_orderColumn   = box.getString         ( "p_orderColumn"           );  // 정렬할 컬럼명

        ManagerAdminBean    bean            = null;
        String              v_sql_add       = "";
        String              v_userid        = box.getSession("userid");
        String              s_gadmin        = box.getSession("gadmin");

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 	= new ArrayList();
                list2 	= new ArrayList();

                // select grseq,course,cyear,courseseq,coursenm,subj,year,subjnm,subjseq,isonoff,compnm,
                // jikwinm,userid,cono,name,edustart,eduend,score,isgraduated,email,ismailing
                sql1 = "select C.grseq,C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq,";
                sql1 += "C.isonoff,get_compnm(B.comp,3,5) compnm,get_jikwinm(B.jikwi,B.comp) jikwinm,get_jikupnm(B.jikup,B.comp) jikupnm,";
                sql1 += "B.userid,B.cono,B.name,C.edustart,C.eduend,A.score,A.isgraduated,B.email,B.ismailing ";
                sql1 += "from TZ_STOLD A,TZ_MEMBER B,VZ_SCSUBJSEQ C where A.isgraduated='N' and c.isclosed = 'Y' ";
                
                if ( !ss_grcode.equals("ALL") ) { 
                    sql1 += " and C.grcode = " +SQLString.Format(ss_grcode);
                }
                
                if ( !ss_grseq.equals("ALL") ) { 
                    sql1 += " and C.grseq = " +SQLString.Format(ss_grseq);
                }
                
                if ( !ss_uclass.equals("ALL") ) { 
                    sql1 += " and C.scupperclass = " +SQLString.Format(ss_uclass);
                }
                
                if ( !ss_mclass.equals("ALL") ) { 
                    sql1 += " and C.scmiddleclass = " +SQLString.Format(ss_mclass);
                }
                
                if ( !ss_lclass.equals("ALL") ) { 
                    sql1 += " and C.sclowerclass = " +SQLString.Format(ss_lclass);
                }

                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1 += " and C.scsubj = " +SQLString.Format(ss_subjcourse);
                }
                
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql1 += " and C.scsubjseq = " +SQLString.Format(ss_subjseq);
                }
                
                if ( !ss_company.equals("ALL") ) { 
                    sql1 += " and B.comp like '" +GetCodenm.get_compval(ss_company) + "'";
                }
                
                if ( !ss_edustart.equals("ALL") ) { 
                    sql1 += " and C.edustart >= " +SQLString.Format(ss_edustart);
                }
                
                if ( !ss_eduend.equals("ALL") ) { 
                    sql1 += " and C.eduend <= " +SQLString.Format(ss_eduend);
                }
                
                // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                    sql1 += " and C.gyear = " +SQLString.Format(ss_gyear);
                }

                // 부서장일경우
                if ( s_gadmin.equals("K7") ) { 
                    bean 		= new ManagerAdminBean();
                    v_sql_add   = bean.getManagerDept(v_userid, s_gadmin);
                    
                    if ( !v_sql_add.equals("")) sql1 += " and B.comp in " + v_sql_add;     // 관리부서검색조건쿼리
                }

                if ( ss_selgubun.equals("JIKUN") && !ss_seltext.equals("ALL"))      	{  // 직군별
                    sql1 += " and B.jikun = " +SQLString.Format(ss_seltext);
                } else if ( ss_selgubun.equals("JIKWI") && !ss_seltext.equals("ALL") ) 	{  // 직위별
                    sql1 += " and B.jikwi = " +SQLString.Format(ss_seltext);
                } else if ( ss_selgubun.equals("GPM") && !ss_seltext.equals("ALL") ) 	{  // 사업부별
                    sql1 += " and B.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seltext));
                    
                    if ( !ss_seldept.equals("ALL") ) { 
                        sql1 += " and B.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seldept));
                     }
                }
                
                sql1 += " and A.userid=B.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq ";
                
                if ( !v_orderColumn.equals("") ) { 
                    v_orderColumn = "B." +v_orderColumn;
                    sql1 += " order by C.course,C.cyear,C.courseseq,C.subj,C.year,C.subjseq," +v_orderColumn;
                } else { 
                    sql1 += " order by C.course,C.cyear,C.courseseq,C.subj,C.year,C.subjseq,B.userid ";
                }

                ls1 = connMgr.executeQuery(sql1);

                ls1.setPageSize		( row     );             	// 페이지당 row 갯수를 세팅한다
                ls1.setCurrentPage	( v_pageno);               	// 현재페이지번호를 세팅한다.
                int total_page_count 	= ls1.getTotalPage();  	// 전체 페이지 수를 반환한다
                int total_row_count 	= ls1.getTotalCount();  // 전체 row 수를 반환한다

                while ( ls1.next() ) { 
                    data1 = new CompleteStatusData();
                    
                    data1.setGrseq      ( ls1.getString("grseq")        );
                    data1.setCourse     ( ls1.getString("course")       );
                    data1.setCyear      ( ls1.getString("cyear")        );
                    data1.setCourseseq  ( ls1.getString("courseseq")    );
                    data1.setCoursenm   ( ls1.getString("coursenm")     );
                    data1.setSubj       ( ls1.getString("subj")         );
                    data1.setYear       ( ls1.getString("year")         );
                    data1.setSubjseq    ( ls1.getString("subjseq")      );
                    data1.setSubjnm     ( ls1.getString("subjnm")       );
                    data1.setIsonoff    ( ls1.getString("isonoff")      );
                    data1.setCompnm     ( ls1.getString("compnm")       );
                    data1.setJikwinm    ( ls1.getString("jikwinm")      );
                    data1.setJikupnm    ( ls1.getString("jikupnm")      );
                    data1.setUserid     ( ls1.getString("userid")       );
                    data1.setCono       ( ls1.getString("cono")         );
                    data1.setName       ( ls1.getString("name")         );
                    data1.setEdustart   ( ls1.getString("edustart")     );
                    data1.setEduend     ( ls1.getString("eduend")       );
                    data1.setScore      ( ls1.getInt("score")           );
                    data1.setIsgraduated( ls1.getString("isgraduated")  );
                    data1.setEmail      ( ls1.getString("email")        );
                    data1.setIsmailing  ( ls1.getString("ismailing")    );
                    data1.setDispnum    ( total_row_count - ls1.getRowNum() + 1);
                    data1.setTotalPageCount ( total_page_count  );
                    data1.setRowCount       ( row               );
                    
                    list1.add(data1);
                }
                for ( int i = 0;i < list1.size(); i++ ) { 
                    data2    	= (CompleteStatusData)list1.get(i);
                    v_course    = data2.getCourse();
                    v_courseseq = data2.getCourseseq();
                    
                    if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq)) ) { 
                        sql2 = "select count(A.subj) cnt from VZ_SCSUBJSEQ A,TZ_STOLD B,TZ_MEMBER C ";
                        sql2 += "where A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq ";
                        sql2 += "and B.isgraduated='N' and B.userid=C.userid ";
                        
                        if ( !ss_grcode.equals("ALL") ) { 
                            sql2 += " and A.grcode = " +SQLString.Format(ss_grcode);
                        }
                        
                        if ( !ss_grseq.equals("ALL") ) { 
                            sql2 += " and A.grseq = " +SQLString.Format(ss_grseq);
                        }
                        
                        if ( !ss_uclass.equals("ALL") ) { 
                            sql2 += " and A.scupperclass = " +SQLString.Format(ss_uclass);
                        }  
                        
                        if ( !ss_mclass.equals("ALL") ) { 
                            sql2 += " and A.scmiddleclass = " +SQLString.Format(ss_mclass);
                        }
                        
                        if ( !ss_lclass.equals("ALL") ) { 
                            sql2 += " and A.sclowerclass = " +SQLString.Format(ss_lclass);
                        }

                        if ( !ss_subjcourse.equals("ALL") ) { 
                            sql2 += " and A.scsubj = " +SQLString.Format(ss_subjcourse);
                        }
                        
                        if ( !ss_subjseq.equals("ALL") ) { 
                            sql2 += " and A.scsubjseq = " +SQLString.Format(ss_subjseq);
                        }
                        
                        if ( !ss_company.equals("ALL") ) { 
                            sql2 += " and C.comp like '" +GetCodenm.get_compval(ss_company) + "'";
                        }
                        
                        if ( !ss_edustart.equals("ALL") ) { 
                            sql2 += " and A.edustart >= " +SQLString.Format(ss_edustart);
                        }
                        
                        if ( !ss_eduend.equals("ALL") ) { 
                            sql2 += " and A.eduend <= " +SQLString.Format(ss_eduend);
                        }
                        
                        // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                        if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                            sql2 += " and A.gyear = " +SQLString.Format(ss_gyear);
                        }

                        // 부서장일경우
                        if ( s_gadmin.equals("K7") ) { 
                            if ( !v_sql_add.equals("")) sql2 += " and C.comp in " + v_sql_add;	// 관리부서검색조건쿼리
                        }

                        if ( ss_selgubun.equals("JIKUN") && !ss_seltext.equals("ALL"))      	{  // 직군별
                            sql2 += " and C.jikun = " +SQLString.Format(ss_seltext);
                        } else if ( ss_selgubun.equals("JIKWI") && !ss_seltext.equals("ALL") ) 	{  // 직위별
                            sql2 += " and C.jikwi = " +SQLString.Format(ss_seltext);
                        } else if ( ss_selgubun.equals("GPM") && !ss_seltext.equals("ALL") ) 	{  // 사업부별
                            sql2 += " and C.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seltext));
                            
                            if ( !ss_seldept.equals("ALL") ) { 
                            	sql2 += " and C.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seldept));
                            }
                        }
                        
                        sql2 	+= "and A.course = " +SQLString.Format(v_course) + " and A.courseseq = " +SQLString.Format(v_courseseq);

                        ls2 	= connMgr.executeQuery(sql2);
                        
                        if ( ls2.next() ) { 
                            data2.setRowspan	( ls2.getInt("cnt") );
                            data2.setIsnewcourse( "Y"				);
                        }
                        
                        if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    } else { 
                        data2.setRowspan(0);
                        data2.setIsnewcourse("N");
                    }
                    
                    v_Bcourse   = v_course;
                    v_Bcourseseq= v_courseseq;
                    list2.add(data2);
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
    수료율 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectCompleteRateList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr		= null;
        ListSet 			ls1     	= null;
        ListSet 			ls2     	= null;
        ArrayList 			list1   	= null;
        ArrayList 			list2   	= null;
        String 				sql1    	= "";
        String 				sql2    	= "";
        
        CompleteStatusData 	data1		= null;
        CompleteStatusData 	data2		= null;
        
        String  			v_Bcourse	= ""; // 이전코스
        String  			v_course    = ""; // 현재코스
        String  			v_Bcourseseq= ""; // 이전코스기수
        String  			v_courseseq = ""; // 현재코스기수
		
        int 				v_pageno    = box.getInt			( "p_pageno"			    );
		String  			v_subjnm	= box.getStringDefault	( "p_subjnm"		, ""	); // 과목명 검색
        String  			ss_grcode   = box.getStringDefault	( "s_grcode"		, "ALL" ); // 교육그룹
        String  			ss_gyear    = box.getStringDefault	( "s_gyear"			, "ALL" );  // 년도
        String  			ss_grseq    = box.getStringDefault	( "s_grseq"			, "ALL" );  // 교육기수
        String  			ss_uclass   = box.getStringDefault	( "s_upperclass"	, "ALL" );  // 과목분류
        String  			ss_mclass   = box.getStringDefault	( "s_middleclass"	, "ALL" );  // 과목분류
        String  			ss_lclass   = box.getStringDefault	( "s_lowerclass"	, "ALL" );  // 과목분류
        String  			ss_subjcourse=box.getStringDefault	( "s_subjcourse"	, "ALL" );	// 과목&코스
        String  			ss_subjseq  = box.getStringDefault	( "s_subjseq"		, "ALL" );  // 과목 기수
        String  			ss_edustart = box.getStringDefault	( "s_edustart"		, "ALL" );  // 교육시작일
        String  			ss_eduend   = box.getStringDefault	( "s_eduend"		, "ALL" );	// 교육종료일
        String  			ss_action   = box.getString			( "s_action"				);
        String  			ss_company  = box.getStringDefault  ( "s_company"       , "ALL" );   // 사업부별 부서 검색내용

		String  			v_orderColumn = box.getString( "p_orderColumn"	);	// 정렬할 컬럼명
        String  			v_orderType   = box.getString( "p_orderType"	);  // 정렬할 순서
        
        
        try { 
                connMgr = new DBConnectionManager();
                list1 	= new ArrayList();
                list2 	= new ArrayList();

                String v_subQry1 = "";
                String v_subQry2 = "";
                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = " and comp = " + SQLString.Format(ss_company) + " ";
                	v_subQry2 = " and userid in (select userid from tz_member where comp = " + SQLString.Format(ss_company) + ") ";
                }

                // select grseq,course,cyear,courseseq,coursenm,subj,year,subjnm,subjseq,
                // edustart,eduend,educnt,gradcnt1,gradcnt2,isonoff
                sql1 = "select A.grseq,A.course,A.cyear,A.courseseq,A.coursenm,A.subj,A.year,A.subjnm,A.subjseq,A.subjseqgr,";
                sql1 += "A.edustart,A.eduend,A.isonoff, ";
                sql1 += "(select count(subj) from TZ_STUDENT where subj=A.subj and year=A.year and subjseq=A.subjseq"+v_subQry1+") educnt, ";
                sql1 += "decode(A.isclosed,\'Y\',(select count(subj) from TZ_STOLD where subj=A.subj and year=A.year and subjseq=A.subjseq and isgraduated='Y'"+v_subQry2+")) gradcnt1,";
                sql1 += "decode(A.isclosed,\'N\',0) gradcnt2, ";
                sql1 += "(decode(A.isclosed,\'Y\',(select count(subj) from TZ_STOLD where subj=A.subj and year=A.year and subjseq=A.subjseq and isgraduated='Y'"+v_subQry2+")) + ";
                sql1 += "decode(A.isclosed,\'N\',0) ) tgradcnt, "; // 정렬하기위해서
                sql1 += " (select nvl(avg(distcode1_avg),0.0) from tz_suleach where subj=A.subj and year=A.year and subjseq=A.subjseq and grcode=A.grcode and grcode< > 'ALL'"+v_subQry2+") distcode1_avg  ";  // 만족도
                sql1 += "from VZ_SCSUBJSEQ A ";
                sql1 += "where 1=1 ";
                
                if ( !ss_grcode.equals("ALL") ) { 
                    sql1 += " and A.grcode = " +SQLString.Format(ss_grcode);
                }
                
                if ( !ss_gyear.equals("ALL") ) { 
                    sql1 += " and A.gyear = " +SQLString.Format(ss_gyear);
                } 
                
                if ( !ss_grseq.equals("ALL") ) { 
                    sql1 += " and A.grseq = " +SQLString.Format(ss_grseq);
                } 
                
                if ( !ss_uclass.equals("ALL") ) { 
                    sql1 += " and A.scupperclass = " +SQLString.Format(ss_uclass);
                }
                
                if ( !ss_mclass.equals("ALL") ) { 
                    sql1 += " and A.scmiddleclass = " +SQLString.Format(ss_mclass);
                }
                
                if ( !ss_lclass.equals("ALL") ) { 
                    sql1 += " and A.sclowerclass = " +SQLString.Format(ss_lclass);
                }

                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1 += " and A.scsubj = " +SQLString.Format(ss_subjcourse);
                }
                
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql1 += " and A.scsubjseq = " +SQLString.Format(ss_subjseq);
                }
                
                if ( !ss_edustart.equals("ALL") ) { 
                    sql1 += " and A.edustart >= " +SQLString.Format(ss_edustart);
                }
                
                if ( !ss_eduend.equals("ALL") ) { 
                    sql1 += " and A.eduend <= " +SQLString.Format(ss_eduend);
                }
                
				// 과목명 검색
				if ( !v_subjnm.equals("") ) { 
					sql1 += " and A.subjnm like " + StringManager.makeSQL("%" + v_subjnm + "%")+ " ";
				}
				
				if ( v_orderColumn.equals("") ) { 
                	sql1 += " order by A.course,A.cyear,A.courseseq,A.subj,A.year,A.subjseq ";
				} else { 
				    sql1 += " order by " + v_orderColumn + v_orderType;
				}				
                
                ls1 = connMgr.executeQuery(sql1);


                ls1.setPageSize		( row	   );           	// 페이지당 row 갯수를 세팅한다
                ls1.setCurrentPage	( v_pageno );               // 현재페이지번호를 세팅한다.
                int total_page_count 	= ls1.getTotalPage();  	// 전체 페이지 수를 반환한다
                int total_row_count 	= ls1.getTotalCount();  // 전체 row 수를 반환한다

                while ( ls1.next() ) { 
                    data1 = new CompleteStatusData();
                    
                // select grseq,course,cyear,courseseq,coursenm,subj,year,subjnm,subjseq,
                // edustart,eduend,educnt,gradcnt1,gradcnt2
                    data1.setGrseq          ( ls1.getString("grseq")        );
                    data1.setCourse         ( ls1.getString("course")       );
                    data1.setCyear          ( ls1.getString("cyear")        );
                    data1.setCourseseq      ( ls1.getString("courseseq")    );
                    data1.setCoursenm       ( ls1.getString("coursenm")     );
                    data1.setSubj           ( ls1.getString("subj")         );
                    data1.setYear           ( ls1.getString("year")         );
                    data1.setSubjseq        ( ls1.getString("subjseq")      );
                    data1.setSubjseqgr      ( ls1.getString("subjseqgr")    );
                    data1.setSubjnm         ( ls1.getString("subjnm")       );
                    data1.setEdustart       ( ls1.getString("edustart")     );
                    data1.setEduend         ( ls1.getString("eduend")       );
                    data1.setEducnt         ( ls1.getInt("educnt")          );
                    data1.setGradcnt1       ( ls1.getInt("gradcnt1")        );
                    data1.setGradcnt2       ( ls1.getInt("gradcnt2")        );
                    data1.setIsonoff        ( ls1.getString("isonoff")      );
                    data1.setDistcode1avg   ( ls1.getDouble("distcode1_avg")); // 만족도
                    data1.setDispnum        ( total_row_count - ls1.getRowNum() + 1);
                    data1.setTotalPageCount ( total_page_count  );
                    data1.setRowCount       ( row               );
                    
                    list1.add(data1);
                }
                for ( int i = 0 ; i < list1.size() ; i++ ) { 
                    data2       = (CompleteStatusData)list1.get(i);
                    v_course    = data2.getCourse();
                    v_courseseq = data2.getCourseseq();
                    
                    if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq)) ) { 
                        sql2 = "select count(A.subj) cnt from VZ_SCSUBJSEQ A ";
                        sql2 += " where 1=1";
                        
                        if ( !ss_grcode.equals("ALL") ) { 
                            sql2 += " and A.grcode = " +SQLString.Format(ss_grcode);
                        }
                        
                        if ( !ss_grseq.equals("ALL") ) { 
                            sql2 += " and A.grseq = " +SQLString.Format(ss_grseq);
                        }
                        
                        if ( !ss_uclass.equals("ALL") ) { 
                            sql2 += " and A.scupperclass = " +SQLString.Format(ss_uclass);
                        }
                        
                        if ( !ss_mclass.equals("ALL") ) { 
                            sql2 += " and A.scmiddleclass = " +SQLString.Format(ss_mclass);
                        }
                        
                        if ( !ss_lclass.equals("ALL") ) { 
                            sql2 += " and A.sclowerclass = " +SQLString.Format(ss_lclass);
                        }

                        if ( !ss_subjcourse.equals("ALL") ) { 
                            sql2 += " and A.scsubj = " +SQLString.Format(ss_subjcourse);
                        }
                        
                        if ( !ss_subjseq.equals("ALL") ) { 
                            sql2 += " and A.scsubjseq = " +SQLString.Format(ss_subjseq);
                        }
                        
                        if ( !ss_edustart.equals("ALL") ) { 
                            sql2 += " and A.edustart >= " +SQLString.Format(ss_edustart);
                        }
                        
                        if ( !ss_eduend.equals("ALL") ) { 
                            sql2 += " and A.eduend <= " +SQLString.Format(ss_eduend);
                        }
                        
                        // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                        if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                            sql2 += " and A.gyear = " +SQLString.Format(ss_gyear);
                        }
                        
                        sql2 += "and A.course = " +SQLString.Format(v_course) + " and A.courseseq = " +SQLString.Format(v_courseseq);

                        ls2 = connMgr.executeQuery(sql2);
                        
                        if ( ls2.next() ) { 
                            data2.setRowspan( ls2.getInt("cnt") );
                            data2.setIsnewcourse("Y");
                        }
                        
                        if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    } else { 
                        data2.setRowspan(0);
                        data2.setIsnewcourse("N");
                    }
                    
                    v_Bcourse   =   v_course;
                    v_Bcourseseq=   v_courseseq;
                    
                    list2.add(data2);
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
    고용보험환급명단 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectHiringInsuranceReturnedList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 	= null;
        ListSet 			ls1     	= null;
        ListSet 			ls2     	= null;
        ArrayList 			list1   	= null;
        ArrayList 			list2   	= null;
        String 				sql1    	= "";
        String 				sql2    	= "";
        
        CompleteStatusData 	data1		= null;
        CompleteStatusData 	data2		= null;
        
        String  			v_Bcourse   = ""; // 이전코스
        String  			v_course    = ""; // 현재코스
        String  			v_Bcourseseq= ""; // 이전코스기수
        String  			v_courseseq = ""; // 현재코스기수

        String  			ss_grcode    = box.getStringDefault ( "s_grcode"		, "ALL" );  // 교육그룹
        String  			ss_gyear     = box.getStringDefault ( "s_gyear"			, "ALL" );  // 년도
        String  			ss_grseq     = box.getStringDefault ( "s_grseq"			, "ALL" );  // 교육기수
        String  			ss_uclass    = box.getStringDefault ( "s_upperclass"	, "ALL" );  // 과목분류
        String  			ss_mclass    = box.getStringDefault ( "s_middleclass"	, "ALL" );  // 과목분류
        String  			ss_lclass    = box.getStringDefault ( "s_lowerclass"	, "ALL" );  // 과목분류
        String  			ss_subjcourse=box.getStringDefault  ( "s_subjcourse"	, "ALL" );  // 과목&코스
        String  			ss_subjseq   = box.getStringDefault ( "s_subjseq"		, "ALL" );  // 과목 기수
        String  			ss_company   = box.getStringDefault ( "s_company"		, "ALL" );  // 회사
        String  			ss_selgubun  = box.getString		( "s_selgubun"				);  // 직군별:JIKUN,직급별:JIKUP,사업부별:GPM
        String  			ss_seltext   = box.getStringDefault ( "s_seltext"		, "ALL"	);  // 검색분류별 검색내용
        String  			ss_seldept   = box.getStringDefault ( "s_seldept"		, "ALL"	);	// 사업부별 부서 검색내용
        String  			ss_action    = box.getString		( "s_action"				);
        String  			v_orderColumn= box.getString		( "p_orderColumn"			);  // 정렬할 컬럼명

        try {
        	
        } catch ( Exception ex ) { 
        	ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            // if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list2;
    }

     
    /**
    폼메일 발송
    @param box      receive from the form object and session
    @return int
    */
    public int sendFormMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet 			ls 		= null;
        String 				sql 	= "";
        
        int 				cnt 	= 0;    //  메일발송이 성공한 사람수
        
        // p_checks로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector 				v_check1     = new Vector();
        v_check1            = box.getVector("p_checks");
        Enumeration 		em1     	= v_check1.elements();
        StringTokenizer 	st1 		= null;
        String 				v_checks    = "";
        String 				v_userid    = "";
        String 				v_subj      = "";
        String 				v_year      = "";
        String 				v_subjseq	= "";

        try { 
            connMgr = new DBConnectionManager();

//// //// //// //// ////  폼메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
            String 			v_sendhtml 	= "mail3.html";
            FormMail 		fmail 		= new FormMail	( v_sendhtml); // 폼메일발송인 경우
            MailSet 		mset 		= new MailSet	( box		); // 메일 세팅 및 발송
            String 			v_mailTitle = "안녕하세요? 경기여성개발능력센터 E-Learning 운영자입니다.(진도율안내)";
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

            while ( em1.hasMoreElements() ) { 
                v_checks    = (String)em1.nextElement();
                st1      	= new StringTokenizer(v_checks,",");
                
                while ( st1.hasMoreElements() ) { 
                    v_userid    = (String)st1.nextToken();
                    v_subj      = (String)st1.nextToken();
                    v_year      = (String)st1.nextToken();
                    v_subjseq   = (String)st1.nextToken();
                    
					break;
                }
                
                // select subjnm,passday,tstep,gradstep,gradscore,name,ismailing,cono,email
                sql = "select  B.subjnm,A.tstep,B.gradstep,B.sgradscore gradscore,D.name,D.ismailing,D.cono,D.email,";
                sql += "(to_number(to_char(sysdate, 'YYYYMMDD')) - to_number(substr(B.edustart,1,8))) passday ";
                sql += " from TZ_STUDENT A,VZ_SCSUBJSEQ B,TZ_MEMBER D ";
                sql += " where A.userid = " +SQLString.Format(v_userid);
                sql += " and A.subj = " +SQLString.Format(v_subj);
                sql += " and A.year = " +SQLString.Format(v_year);
                sql += " and A.subjseq = " +SQLString.Format(v_subjseq);
                sql += " and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and A.userid=D.userid ";
                sql += " group by B.subjnm,A.tstep,B.gradstep,B.sgradscore,D.name,D.ismailing,D.cono,D.email,B.edustart,B.eduend ";
                ls 	= connMgr.executeQuery(sql);
//                System.out.println("sql ==  ==  ==  == = > " +sql);

                while ( ls.next() ) { 
                    String v_toEmail 	= ls.getString( "email"		);
                    String v_toCono  	= ls.getString( "cono"		);
                    String v_ismailing	= ls.getString( "ismailing"	);

                    mset.setSender(fmail);     // 메일보내는 사람 세팅

                    fmail.setVariable ( "tstep"		, ls.getString("tstep") 	);
                    fmail.setVariable ( "subjnm"	, ls.getString("subjnm") 	);
                    fmail.setVariable ( "passday"	, ls.getString("passday") 	);
                    fmail.setVariable ( "tstep"		, ls.getString("tstep") 	);
                    fmail.setVariable ( "gradstep"	, ls.getString("gradstep") 	);
                    fmail.setVariable ( "gradscore"	, ls.getString("gradscore") );
                    fmail.setVariable ( "toname"	, ls.getString("name") 		);

                    String	v_mailContent 	= fmail.getNewMailContent();

                    boolean isMailed 		= mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml);
                    
                    if ( isMailed ) // 메일발송에 성공하면	 
                    	cnt++;     
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }
}