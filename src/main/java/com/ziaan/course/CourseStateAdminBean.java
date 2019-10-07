// **********************************************************
//  1. 제      목: 과목 운영정보 BEAN
//  2. 프로그램명:  CourseStateAdminBean.java
//  3. 개      요: 과목 운영정보 BEAN
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 8. 13
//  7. 수      정:
// **********************************************************
package com.ziaan.course;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class CourseStateAdminBean { 

    public CourseStateAdminBean() { }

    /**
     * 과목운영정보  리스트
     * @param box          receive from the form object and session
     * @return ArrayList   과목운영정보  리스트
     */
    public ArrayList selectListCourseState(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls1             = null;       
        ListSet             ls2             = null;
        ArrayList           list1           = null;
        ArrayList           list2           = null;
//        CourseStateData     data1           = null;
//        CourseStateData     data2           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
         
        String              v_Bcourse       = ""; // 이전코스
        String              v_course        = ""; // 현재코스
        String              v_Bcourseseq    = ""; // 이전코스기수
        String              v_courseseq     = ""; // 현재코스기수     
//        String              v_completion    = ""; // 수료율상태  
//        int                  l               = 0;
                
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // 교육그룹
        String              ss_gyear        = box.getStringDefault("s_gyear"        , "ALL");   // 년도
        String              ss_grseq        = box.getStringDefault("s_grseq"        , "ALL");   // 교육기수
        String              ss_upperclass       = box.getStringDefault("s_upperclass"       , "ALL");   
        String              ss_middleclass       = box.getStringDefault("s_middleclass"		, "ALL");
        String              ss_lowerclass       = box.getStringDefault("s_lowerclass"		, "ALL");
        String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // 과목&코스
        String              ss_subjseq      = box.getStringDefault("s_subjseq"      , "ALL");   // 과목 기수
        
        String              v_orderColumn   = box.getString("p_orderColumn" );                  // 정렬할 컬럼명
        String              v_orderType     = box.getString("p_orderType"   );           		// 정렬할 순서 
        DataBox             dbox 			= null;
        DataBox             dbox2 			= null;
        
        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();          
            list2   = new ArrayList();
            
            sbSQL.append(" select  a.grcode                                                                     \n")
                 .append("     ,   b.grcodenm                                                                   \n")
                 .append("     ,   a.gyear                                                                      \n")
                 .append("     ,   a.grseq                                                                      \n")
                 .append("     ,   a.course                                                                     \n")
                 .append("     ,   a.cyear                                                                      \n")
                 .append("     ,   a.scsubjseq                                                                  \n")
                 .append("     ,   a.courseseq                                                                  \n")
                 .append("     ,   a.coursenm                                                                   \n")
                 .append("     ,   a.subj                                                                       \n")
                 .append("     ,   a.year                                                                       \n")
                 .append("     ,   a.subjseq                                                                    \n")
                 .append("     ,   a.subjseqgr                                                                  \n")
                 .append("     ,   a.subjnm                                                                     \n")
                 .append("     ,   a.isonoff                                                                    \n")
                 .append("     ,   get_codenm('0004',a.isonoff) isonoffval                                      \n")                 
                 .append("     ,   a.edustart                                                                   \n")
                 .append("     ,   a.eduend                                                                     \n")
                 .append("     ,   a.wstep                                                                      \n")
                 .append("     ,   a.wmtest                                                                     \n")
                 .append("     ,   a.wftest                                                                     \n")
                 .append("     ,   a.whtest                                                                     \n")
                 .append("     ,   a.wreport                                                                    \n")
                 .append("     ,   a.wact                                                                       \n")
                 .append("     ,   a.wetc1																		\n")
                 .append("     ,   a.wetc2																		\n")
                 .append("     ,   a.gradstep                                                                   \n")
                 .append("     ,   a.gradexam                                                                   \n")
                 .append("     ,   a.gradftest                                                                  \n")
                 .append("     ,   a.gradreport                                                                 \n")
                 .append("     ,   a.sgradscore                                                                 \n")
                 .append("     ,   a.gradexam_flag																\n")
                 .append("	   ,   a.gradftest_flag																\n")
                 .append("	   ,   a.gradhtest_flag																\n")
                 .append("	   ,   a.gradreport_flag															\n")
                 .append("     ,   a.biyong                                                                     \n")
                 .append("     ,   a.ischarge                                                                   \n")
                 .append("     ,   a.edulimit                                                                   \n")
                 .append("     ,   a.study_count                                          						\n")
                 .append("     ,   (                                                                            \n")
                 .append("             select  count(*)                                                         \n")
                 .append("             from    TZ_EXAMPAPER                                                     \n")
                 .append("             where   subj        = A.subj                                             \n")
                 .append("             and     year        = A.year                                             \n")
                 .append("             and     subjseq     = A.subjseq                                          \n")
                 .append("             and     examtype    = 'M'                                                \n")
                 .append("          )                                      cnt_mexam                            \n")
                 .append("     ,   (                                                                            \n")
                 .append("             select  count(*)                                                         \n")
                 .append("             from    TZ_EXAMPAPER                                                     \n")
                 .append("             where   subj        = A.subj                                             \n")
                 .append("             and     year        = A.year                                             \n")
                 .append("             and     subjseq     = A.subjseq                                          \n")
                 .append("             and     examtype    = 'E'                                                \n")
                 .append("         )                                       cnt_texam                            \n")
                 .append("     ,   (                                                                            \n")
                 .append("             select  count(*)                                                         \n")
                 .append("             from    TZ_EXAMPAPER                                                     \n")                      
                 .append("             where   subj        = A.subj                                             \n")
                 .append("             and     year        = A.year                                             \n")
                 .append("             and     subjseq     = A.subjseq                                          \n")
                 .append("             and     examtype    = 'H'                                                \n")
                 .append("         )                                       cnt_hexam                            \n")
                 .append("     ,   (                                                                            \n")
                 .append("             select  count(distinct projgubun)                               			\n")
                 .append("             from    tz_projgrp                                                       \n")
                 .append("             where   subj        = a.subj                                             \n")
                 .append("             and     year        = a.year                                             \n")
                 .append("             and     subjseq     = a.subjseq                                          \n")
                 .append("         )                                       cnt_proj                             \n")
                 .append(" from    vz_scsubjseq        a                                                        \n")
                 .append("     ,   tz_grcode           b                                                        \n")
                 .append(" where   a.grcode    = b.grcode                                                       \n");
                 

            if ( !ss_grcode.equals("ALL") ) { 
                sbSQL.append(" and a.grcode         = " + StringManager.makeSQL(ss_grcode       ) + "           \n");
            }                                       
            if ( !ss_gyear.equals("ALL") ) {        
                sbSQL.append(" and a.gyear          = " + StringManager.makeSQL(ss_gyear        ) + "           \n");
            }                                       
            if ( !ss_grseq.equals("ALL") ) {        
                sbSQL.append(" and a.grseq          = " + StringManager.makeSQL(ss_grseq        ) + "           \n");
            }
            if ( !ss_upperclass.equals("ALL") ) { 
                sbSQL.append(" and a.scupperclass   = " + StringManager.makeSQL(ss_upperclass       ) + "           \n");
            }
            
            if ( !ss_middleclass.equals("ALL") ) { 
                sbSQL.append(" and a.scmiddleclass   = " + StringManager.makeSQL(ss_middleclass       ) + "           \n");
            }
            
            if ( !ss_lowerclass.equals("ALL") ) { 
                sbSQL.append(" and a.sclowerclass   = " + StringManager.makeSQL(ss_lowerclass       ) + "           \n");
            }
            
            if ( !ss_subjcourse.equals("ALL") ) { 
                sbSQL.append(" and a.scsubj         = " + StringManager.makeSQL(ss_subjcourse   ) + "           \n");
            }
            if ( !ss_subjseq.equals("ALL") ) { 
                sbSQL.append(" and a.scsubjseq      = " + StringManager.makeSQL(ss_subjseq      ) + "           \n");
            }
            
            if ( v_orderColumn.equals("") ) { 
                sbSQL.append(" order by a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq                                                               \n");
            } else { 
                sbSQL.append(" order by a.course, a.cyear, a.courseseq, " + v_orderColumn + v_orderType + "                                     \n");
            }

            ls1     = connMgr.executeQuery(sbSQL.toString());
            
            String sql = "";
            while ( ls1.next() ) { 
            	dbox = ls1.getDataBox();
            	dbox.put("d_wstep", new Double(ls1.getString("wstep")));
            	dbox.put("d_wmtest", new Double(ls1.getString("wmtest")));
            	dbox.put("d_wftest", new Double(ls1.getString("wftest")));
            	dbox.put("d_whtest", new Double(ls1.getString("whtest")));
            	dbox.put("d_wreport", new Double(ls1.getString("wreport")));
            	dbox.put("d_wact", new Double(ls1.getString("wact")));
            	dbox.put("d_wetc1", new Double(ls1.getString("wetc1")));
            	dbox.put("d_wetc2", new Double(ls1.getString("wetc2")));
            	
            	// 독서교육일 경우 개월차 도서정보를 보여주기 위해 가져온다.
            	list2.clear();
            	if ("RC".equals(ls1.getString("isonoff"))) {
	            	sql = "select t1.subj, t1.month, t1.bookcode, t2.bookname "
	            		+ "from   tz_subjbook t1 "
	            		+ "     , tz_bookinfo t2 "
	            		+ "where  t1.bookcode = t2.bookcode "
	            		+ "and    t1.subj = " + StringManager.makeSQL(ls1.getString("subj"));
	                ls2 = connMgr.executeQuery(sql);
	
	                while (ls2.next()) {
	                	dbox2 = ls2.getDataBox();
	                	list2.add(dbox2);
	                }
	                if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }	                
            	}
            	dbox.put("bookinfo", list2);
            	list1.add(dbox);
            }
               
            for ( int i = 0;i < list1.size(); i++ ) { 
                //data2       =   (CourseStateData)list1.get(i);
            	dbox2         =  (DataBox)list1.get(i);
                
                v_course    =  dbox2.getString("d_course");//  data2.getCourse();
                v_courseseq =  dbox2.getString("d_courseseq"); // data2.getCourseseq();
                
                sbSQL.setLength(0);
                
                if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                    sbSQL.append(" select  count(subj) cnt                                                  \n")
                         .append(" from    TZ_SUBJSEQ                                                       \n")
                         .append(" where   course      = " + StringManager.makeSQL( v_course       ) + "    \n")
                         .append(" and     courseseq   = " + StringManager.makeSQL( v_courseseq    ) + "    \n");
                         
                    if ( !ss_grcode.equals("ALL") ) { 
                        sbSQL.append(" and grcode       = " + StringManager.makeSQL( ss_grcode     ) + "    \n");
                    }                                                                                       
                    
                    if ( !ss_gyear.equals("ALL") ) {                                                        
                        sbSQL.append(" and gyear        = " + StringManager.makeSQL( ss_gyear      ) + "    \n");
                    }                                                                                       
                    
                    if ( !ss_grseq.equals("ALL") ) {                                                        
                        sbSQL.append(" and grseq        = " + StringManager.makeSQL( ss_grseq      ) + "    \n");
                    }
                    
                    ls2     = connMgr.executeQuery(sbSQL.toString());
                    
                    if ( ls2.next() ) { 
//                    	data2.setRowspan    ( ls2.getInt("cnt") );
//                        data2.setIsnewcourse( "Y"               );

                    	dbox2.put("d_rowspan", new Integer(ls2.getInt("cnt")));
                    	dbox2.put("d_isnewcourse", "Y");
                    }
                    
					ls2.close();
                } else { 
//                    data2.setRowspan(0);
//                    data2.setIsnewcourse("N");
                	dbox2.put("d_rowspan", new Integer("0"));
                	dbox2.put("d_isnewcourse", "N");
                }
                
                v_Bcourse       = v_course   ;
                v_Bcourseseq    = v_courseseq;
                
//                list2.add(data);
                list2.add(dbox2);
                
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
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
        }

        return list1;        
    }
}