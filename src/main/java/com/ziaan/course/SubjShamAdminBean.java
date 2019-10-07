// **********************************************************
//  1. 제      목: 과목모의학습
//  2. 프로그램명 : SubjShamAdminBean.java
//  3. 개      요: 과목모의학습 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  
//  7. 수      정:
// **********************************************************

package com.ziaan.course;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class SubjShamAdminBean {
	
    public SubjShamAdminBean() { }

    /**
     * 과목모의학습화면 리스트
     * @param box          receive from the form object and session
     * @return ArrayList   과목 리스트
     * @throws Exception
     */
    public ArrayList selectListSubjSham(RequestBox box) throws Exception {
        DBConnectionManager connMgr 		= null;
        ListSet 			ls 				= null;
        ArrayList 			list 		    = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        SubjectData 		data			= null;
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String              v_search        = box.getString         ("p_search"                 );
        String              ss_upperclass   = box.getStringDefault  ("s_upperclass"     ,"ALL"  );  // 과목대분류
        String              ss_middleclass  = box.getStringDefault  ("s_middleclass"    ,"ALL"  );  // 과목중분류
        String              ss_lowerclass   = box.getStringDefault  ("s_lowerclass"     ,"ALL"  );  // 과목소분류
        String              ss_subjcourse   = box.getStringDefault  ("s_subjcourse"     ,"ALL"  );  // 과목&코스
        
        try { 
            connMgr     = new DBConnectionManager();

            list        = new ArrayList();

            sbSQL.append(" SELECT  b.upperclass                             \n")
                 .append("     ,   b.classname                              \n")
                 .append("     ,   a.subj                                   \n")
                 .append("     ,   a.subjnm                                 \n")
                 .append("     ,   a.contenttype                            \n")
                 .append("     ,   a.isonoff                                \n")
                 .append(" FROM    TZ_SUBJ     a                            \n")
                 .append("     ,   TZ_SUBJATT  b                            \n")
                 .append(" WHERE   a.upperclass    = b.upperclass           \n")            
                 .append(" AND     b.middleclass   = '000'                  \n")
                 .append(" AND     b.lowerclass    = '000'                  \n");

            if ( !ss_subjcourse.equals("ALL") ) { 
                sbSQL.append(" AND  a.subj = " + SQLString.Format(ss_subjcourse) + "    \n");
            } else { 
                if ( !ss_upperclass.equals("ALL") ) { 
                    if ( !ss_upperclass.equals("ALL") ) { 
                        sbSQL.append(" AND  a.upperclass    = " + SQLString.Format(ss_upperclass)   + " \n");
                    }
                   
                    if ( !ss_middleclass.equals("ALL") ) { 
                        sbSQL.append(" AND  a.middleclass   = " + SQLString.Format(ss_middleclass)  + " \n");
                    }
                   
                    if ( !ss_lowerclass.equals("ALL") ) { 
                        sbSQL.append(" AND  a.lowerclass    = " + SQLString.Format(ss_lowerclass)   + " \n");
                    }
                }
            }
       
            if ( !v_search.equals("") ) { 
                sbSQL.append(" AND  a.subjnm like '%' || " + SQLString.Format(v_search) + " || '%'  \n");
            }
        
            sbSQL.append(" ORDER BY b.subjclass asc, a.subj asc                         \n");
            
            System.out.println(this.getClass().getName() + "." + "selectListSubjSham() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls          = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                data    = new SubjectData();

                data.setUpperclass  ( ls.getString("upperclass" ) );
                data.setClassname   ( ls.getString("classname"  ) );
                data.setSubj        ( ls.getString("subj"       ) );
                data.setSubjnm      ( ls.getString("subjnm"     ) );
                data.setContenttype ( ls.getString("contenttype") );
                data.setIsonoff     ( ls.getString("isonoff"    ) );
                
                list.add(data);
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
     * 기수별과목모의학습화면 리스트
     * @param box          receive from the form object and session
     * @return ArrayList   과목기수별 리스트
     * @throws Exception
     */
    public ArrayList selectViewSubjSham(RequestBox box) throws Exception {
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        ArrayList           list        = null;
        StringBuffer        strSQL       = new StringBuffer("");
        SubjseqData         data        = null;
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String 				v_subj       = box.getString("p_subj");

        try { 
            connMgr     = new DBConnectionManager();

            list        = new ArrayList();

            strSQL = new StringBuffer();
            strSQL.append("SELECT   a.subj 					\n") ;
            strSQL.append("     ,   a.year 					\n") ;
            strSQL.append("     ,   a.subjseq 				\n") ;
            strSQL.append("     ,   a.grcode 				\n") ;
            strSQL.append("     ,   a.gyear 				\n") ;
            strSQL.append("     ,   a.grseq 				\n") ;
            strSQL.append("     ,   a.isbelongcourse 		\n") ;
            strSQL.append("     ,   a.course 				\n") ;
            strSQL.append("     ,   a.cyear 				\n") ;
            strSQL.append("     ,   a.courseseq 			\n") ;
            strSQL.append("     ,   a.propstart 			\n") ;
            strSQL.append("     ,   a.propend 				\n") ;
            strSQL.append("     ,   a.edustart 				\n") ;
            strSQL.append("     ,   a.eduend 				\n") ;
            strSQL.append("     ,   a.isclosed 				\n") ;
            strSQL.append("     ,   a.subjnm 				\n") ;
            strSQL.append("     ,   b.isoutsourcing 		\n") ;
            strSQL.append("     ,   b.cp 					\n") ;
            strSQL.append("     ,   b.cpsubj 				\n") ;
            strSQL.append("     ,   b.eduurl 				\n") ;
            strSQL.append(" FROM    TZ_SUBJSEQ a, 			\n") ;
            strSQL.append("         tz_subj b 				\n") ;
            strSQL.append(" WHERE   a.subj = b.subj 		\n") ;
            strSQL.append("   and   a.subj = " + StringManager.makeSQL(v_subj) + "         \n");
            strSQL.append(" ORDER BY a.edustart desc ") ;
            
            System.out.println(this.getClass().getName() + "." + "selectViewSubjSham() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + strSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(strSQL.toString());

            while ( ls.next() ) { 
                data    = new SubjseqData();

                data.setSubj            ( ls.getString("subj"           ));
                data.setYear            ( ls.getString("year"           ));
                data.setSubjseq         ( ls.getString("subjseq"        ));
                data.setGrcode          ( ls.getString("grcode"         ));
                data.setGyear           ( ls.getString("gyear"          ));
                data.setGrseq           ( ls.getString("grseq"          ));
                data.setIsbelongcourse  ( ls.getString("isbelongcourse" ));
                data.setCourse          ( ls.getString("course"         ));
                data.setCyear           ( ls.getString("cyear"          ));
                data.setCourseseq       ( ls.getString("courseseq"      ));
                data.setPropstart       ( ls.getString("propstart"      ));
                data.setPropend         ( ls.getString("propend"        ));
                data.setEdustart        ( ls.getString("edustart"       ));
                data.setEduend          ( ls.getString("eduend"         ));
                data.setIsclosed        ( ls.getString("isclosed"       ));
                data.setSubjnm          ( ls.getString("subjnm"         ));
                data.setCpsubj(ls.getString("cpsubj"));			//cp과정코드 추가
                data.setEduurl(ls.getString("eduurl"));			//학습URL 추가
                data.setIsoutsourcing(ls.getString("isoutsourcing"));
                
                list.add(data);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, strSQL.toString());
            throw new Exception("\n SQL : [\n" + strSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
}
