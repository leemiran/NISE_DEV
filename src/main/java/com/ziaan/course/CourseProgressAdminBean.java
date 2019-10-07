// **********************************************************
//  1. 제      목: 과목 기수별 진행상황 BEAN
//  2. 프로그램명:  CourseProgressAdminBean.java
//  3. 개      요: 과목 기수별 진행상황 BEAN
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 8. 13
//  7. 수      정:
// **********************************************************
package com.ziaan.course;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class CourseProgressAdminBean { 

    public CourseProgressAdminBean() { }

    /**
    과목 기수별 진행상황  리스트
    @param box          receive from the form object and session
    @return ArrayList   과목 기수별 진행상황  리스트
    */
    public ArrayList selectListCourseProgress(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls1             = null;       
        ListSet             ls2             = null;
        ArrayList           list1           = null;
        ArrayList           list2           = null;
        CourseProgressData  data1           = null;
        CourseProgressData  data2           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String              v_Bcourse       = ""; // 이전코스
        String              v_course        = ""; // 현재코스
        String              v_Bcourseseq    = ""; // 이전코스기수
        String              v_courseseq     = ""; // 현재코스기수     
        String              v_completion    = ""; // 수료율상태  
        
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // 교육그룹
        String              ss_gyear        = box.getStringDefault("s_gyear"        , "ALL");   // 년도
        String              ss_grseq        = box.getStringDefault("s_grseq"        , "ALL");   // 교육기수
        String              ss_uclass       = box.getStringDefault("s_upperclass"   , "ALL");   // 과목분류
        String              ss_mclass       = box.getStringDefault("s_middleclass"  , "ALL");   // 과목분류
        String              ss_lclass       = box.getStringDefault("s_lowerclass"   , "ALL");   // 과목분류
        String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // 과목&코스
        String              ss_subjseq      = box.getStringDefault("s_subjseq"      , "ALL");   // 과목 기수
        String              ss_isclosed     = box.getStringDefault("s_isclosed"     , "ALL");   // 현재상태  
        
        String              v_orderColumn   = box.getString("p_orderColumn"                );   // 정렬할 컬럼명
        String              v_orderType     = box.getString("p_orderType"                  );   // 정렬할 순서      

        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();          
            list2   = new ArrayList();        
            
            sbSQL.append(" SELECT  a.grcode                                                         \n")
                 .append("     ,   grcodenm                                                         \n")
                 .append("     ,   a.gyear                                                          \n")
                 .append("     ,   a.grseq                                                          \n")
                 .append("     ,   a.course                                                         \n")
                 .append("     ,   a.cyear                                                          \n")
                 .append("     ,   a.courseseq                                                      \n")
                 .append("     ,   a.coursenm                                                       \n")
                 .append("     ,   a.subj                                                           \n")
                 .append("     ,   a.year                                                           \n")
                 .append("     ,   a.subjseq                                                        \n")
                 .append("     ,   a.subjseqgr                                                      \n")
                 .append("     ,   a.subjnm                                                         \n")
                 .append("     ,   get_codenm('0004',a.isonoff) as isonoff                          \n")
                 .append("     ,   a.propstart                                                      \n")
                 .append("     ,   a.propend                                                        \n")
                 .append("     ,   a.edustart                                                       \n")
                 .append("     ,   a.eduend                                                         \n")
                 .append("     ,   (                                                                \n")
                 .append("             select  count(*)                                             \n")
                 .append("             from    TZ_PROPOSE                                           \n")
                 .append("             where   subj    = A.subj                                     \n")
                 .append("             and     year    = A.year                                     \n")
                 .append("             and     subjseq = A.subjseq                                  \n")
                 .append("         )                                   cnt_propose                  \n")
                 .append("     ,   (                                                                \n")
                 .append("             select  count(*)                                             \n")
                 .append("             from    TZ_PROPOSE                                           \n")
                 .append("             where   subj        = A.subj                                 \n")
                 .append("             and     year        = A.year                                 \n")
                 .append("             and     subjseq     = A.subjseq                              \n")
                 .append("             and     ischkfirst  = 'Y'                                    \n")
                 .append("         )                                   cnt_chkfirst                 \n")
                 .append("     ,   (                                                                \n")
                 .append("             select  count(*)                                             \n")
                 .append("             from    TZ_PROPOSE                                           \n")
                 .append("             where   subj        = A.subj                                 \n")
                 .append("             and     year        = A.year                                 \n")
                 .append("             and     subjseq     = A.subjseq                              \n")
                 .append("             and     chkfinal    = 'Y'                                    \n")
                 .append("         )                                   cnt_chkfinal                 \n")
                 .append("     ,   (                                                                \n")
                 .append("             select  count(*)                                             \n")
                 .append("             from    TZ_STUDENT                                           \n")
                 .append("             where   subj        = A.subj                                 \n")
                 .append("             and     year        = A.year                                 \n")
                 .append("             and     subjseq     = A.subjseq                              \n")
                 .append("         )                                   cnt_student                  \n")
                 .append("     ,   (                                                                \n")
                 .append("             select  count(*)                                             \n")
                 .append("             from    TZ_STOLD                                             \n")
                 .append("             where   subj        = A.subj                                 \n")
                 .append("             and     year        = A.year                                 \n")
                 .append("             and     subjseq     = A.subjseq                              \n")
                 .append("             and     isgraduated = 'Y'                                    \n")
                 .append("         )                                   cnt_pre                      \n")
                 .append("     ,   (                                                                \n")
                 .append("             select  count(*)                                             \n")
                 .append("             from    TZ_STOLD                                             \n")
                 .append("             where   subj    = A.subj                                     \n")
                 .append("             and     year    = A.year                                     \n")
                 .append("             and     subjseq = A.subjseq                                  \n")
                 .append("         )                                   cnt_after                    \n")
                 .append("     ,   c.muserid                                                        \n")
                 .append("     ,   get_name(c.muserid) as mname                                     \n")
                 .append(" FROM    vz_scsubjseq    a                                                \n")
                 .append("     ,   tz_grcode       b                                                \n")
                 .append("     ,   tz_subjseq      c                                                \n")
                 .append(" WHERE   a.grcode    = b.grcode                                           \n")
                 .append("   AND   a.subj = c.subj(+)                                               \n")
                 .append("   AND   a.year = c.year(+)                                               \n")
                 .append("   AND   a.subjseq = c.subjseq(+)                                         \n");

            if ( !ss_grcode.equals("ALL") ) { 
                sbSQL.append(" and a.grcode         = " + StringManager.makeSQL(ss_grcode       ) + "   \n");
            }                                                              
                                                                           
            if ( !ss_gyear.equals("ALL") ) {                               
                sbSQL.append(" and a.gyear          = " + StringManager.makeSQL(ss_gyear        ) + "   \n");
            }                                                              
                                                                           
            if ( !ss_grseq.equals("ALL") ) {                               
                sbSQL.append(" and a.grseq          = " + StringManager.makeSQL(ss_grseq        ) + "   \n");
            }                                                              
                                                                           
            if ( !ss_uclass.equals("ALL") ) {                              
                sbSQL.append(" and a.scupperclass   = " + StringManager.makeSQL(ss_uclass       ) + "   \n");
            }                                                              
                                                                           
            if ( !ss_mclass.equals("ALL") ) {                              
                sbSQL.append(" and a.scmiddleclass  = " + StringManager.makeSQL(ss_mclass       ) + "   \n");
            }
            
            if ( !ss_lclass.equals("ALL") ) { 
                sbSQL.append(" and a.sclowerclass   = " + StringManager.makeSQL(ss_lclass       ) + "   \n");
            }
            
            if ( !ss_subjcourse.equals("ALL") ) { 
                sbSQL.append(" and a.scsubj         = " + StringManager.makeSQL(ss_subjcourse   ) + "   \n");
            }
            
            if ( !ss_subjseq.equals("ALL") ) { 
                sbSQL.append(" and a.scsubjseq      = " + StringManager.makeSQL(ss_subjseq      ) + "   \n");
            }
            
            if ( !ss_isclosed.equals("ALL") ) { 
                sbSQL.append(" and a.isclosed       = " + StringManager.makeSQL(ss_isclosed     ) + "   \n");
             }            
             
            if ( v_orderColumn.equals("") ) { 
                sbSQL.append(" order by a.course, a.cyear, a.courseseq, a.subjnm \n");
            } else { 
                sbSQL.append(" order by a.course, a.cyear, a.courseseq, " + v_orderColumn + v_orderType + " \n");
            }

            ls1 = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls1.next() ) { 
                data1 = new CourseProgressData();

                data1.setGrcode         ( ls1.getString("grcode"        ));
                data1.setGrcodenm       ( ls1.getString("grcodenm"      ));
                data1.setGyear          ( ls1.getString("gyear"         ));
                data1.setGrseq          ( ls1.getString("grseq"         ));
                data1.setCourse         ( ls1.getString("course"        ));
                data1.setCyear          ( ls1.getString("cyear"         ));
                data1.setCourseseq      ( ls1.getString("courseseq"     ));
                data1.setCoursenm       ( ls1.getString("coursenm"      ));
                data1.setSubj           ( ls1.getString("subj"          ));
                data1.setYear           ( ls1.getString("year"          ));
                data1.setSubjseq        ( ls1.getString("subjseq"       ));
                data1.setSubjseqgr      ( ls1.getString("subjseqgr"     ));
                data1.setSubjnm         ( ls1.getString("subjnm"        ));
                data1.setIsonoff        ( ls1.getString("isonoff"       ));
                data1.setEdustart       ( ls1.getString("edustart"      ));
                data1.setEduend         ( ls1.getString("eduend"        ));
                data1.setPropstart      ( ls1.getString("propstart"     ));
                data1.setPropend        ( ls1.getString("propend"       ));
                data1.setCnt_propose    ( ls1.getInt   ("cnt_propose"   ));
                data1.setCnt_chkfirst   ( ls1.getInt   ("cnt_chkfirst"  ));
                data1.setCnt_chkfinal   ( ls1.getInt   ("cnt_chkfinal"  ));
                data1.setCnt_student    ( ls1.getInt   ("cnt_student"   ));
                data1.setCnt_completion ( ls1.getInt   ("cnt_pre"       ));
                data1.setMuserid		( ls1.getString("muserid"       ));
                data1.setMname	 	    ( ls1.getString("mname"       	));
                
				list1.add(data1);
            }
            
            if ( ls1 != null ) { 
                try { 
                    ls1.close(); 
                } catch ( Exception e ) { } 
            }                    
                
            for ( int i = 0;i < list1.size(); i++ ) { 
                data2       = (CourseProgressData)list1.get(i);
                
                v_course    = data2.getCourse();
                v_courseseq = data2.getCourseseq();
                
                sbSQL.setLength(0);
                
                if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 

                    sbSQL.append(" select  count(	subj) cnt                                                  \n")
                         .append(" from    TZ_SUBJSEQ                                                       \n")
                         .append(" where   course      = " + StringManager.makeSQL(v_course    )   + "      \n")
                         .append(" and     courseseq   = " + StringManager.makeSQL(v_courseseq )   + "      \n");
                    
                    if ( !ss_grcode.equals("ALL") ) { 
                        sbSQL.append(" and grcode   = " + StringManager.makeSQL(ss_grcode ) + "             \n");
                    }
                    
                    if ( !ss_gyear.equals("ALL") ) { 
                        sbSQL.append(" and gyear    = " + StringManager.makeSQL(ss_gyear  ) + "             \n");
                    }
                    
                    if ( !ss_grseq.equals("ALL") ) { 
                        sbSQL.append(" and grseq    = " + StringManager.makeSQL(ss_grseq  ) + "             \n");
                    }
                    
                    System.out.println(this.getClass().getName() + "." + "selectListCourseProgress() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                    ls2 = connMgr.executeQuery(sbSQL.toString());
                    
                    if ( ls2.next() ) { 
                        data2.setRowspan    ( ls2.getInt("cnt") );
                        data2.setIsnewcourse("Y"                );
                    }						
                } else { 
                    data2.setRowspan    (0);
                    data2.setIsnewcourse("N");
                }
                
                v_Bcourse       = v_course   ;
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
     * 과정기수별 인원배분 목록
     * @param box
     * @return
     * @throws Exception 
     */
	public ArrayList selectSubjseq(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls1     = null;       
        ArrayList           list    = null;
        String        		sql     = "";
        
        String              v_subj   	= box.getString("p_subj");  	// 과정
        String              v_year      = box.getString("p_year");   	// 년도
        String              v_subjseq   = box.getString("p_subjseq");   // 과정기수  
        String              v_except    = box.getString("p_except");    // 대상기수에서 제외(팝업에서는 선택된 기수는 제외시킴)
        
        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();          
            
            sql = "\n select a.subj "
            	+ "\n      , a.year "
            	+ "\n      , a.subjseq "
            	+ "\n      , a.propstart "
            	+ "\n      , a.propend "
            	+ "\n      , a.edustart "
            	+ "\n      , a.eduend "
            	+ "\n      , a.isdeleted "
                + "\n      , ( "
                + "\n          select  count(*) "
                + "\n          from    tz_propose "
                + "\n          where   subj    = a.subj "
                + "\n          and     year    = a.year "
                + "\n          and     subjseq = a.subjseq "
                + "\n        )                                   cnt_propose "
                + "\n      , ( "
                + "\n          select  count(*) "
                + "\n          from    tz_student "
                + "\n          where   subj        = a.subj "
                + "\n          and     year        = a.year "
                + "\n          and     subjseq     = a.subjseq "
                + "\n        )                                   cnt_student "
                + "\n from   tz_subjseq a "
                + "\n where  a.subj = " + StringManager.makeSQL(v_subj)
                + "\n and    a.year = " + StringManager.makeSQL(v_year)
                + "\n and    (substr(edustart,1,8), substr(eduend,1,8)) "
                + "\n                                in (select substr(edustart,1,8), substr(eduend,1,8) "
                + "\n                                    from   tz_subjseq "
                + "\n                                    where  subj = " + StringManager.makeSQL(v_subj)
                + "\n                                    and    year = " + StringManager.makeSQL(v_year)
                + "\n                                    and    subjseq = " + StringManager.makeSQL(v_subjseq) + ") ";

            if (v_except.equals("Y")) {
            	sql += "\n and    a.subjseq <> " + StringManager.makeSQL(v_subjseq);
            }
            ls1 = connMgr.executeQuery(sql);
            
            while ( ls1.next() ) { 
            	DataBox dbox = (DataBox)ls1.getDataBox();
				list.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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

        return list;
	}

	/**
	 * 과정기수별 인원배분
	 * @param box
	 * @return
	 * @throws Exception 
	 */
	public int insertMoveSubjseq(RequestBox box) throws Exception {
        int isOk = 0;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj    = box.getString("p_subj");		// 과정코드
        String v_year    = box.getString("p_year");		// 년도
        String v_subjseq = box.getString("p_subjseq");	// 기수
        
        String v_targetSubjseq = box.getString("p_target_subjseq");	// 대상기수
        int v_targetUsercnt = box.getInt("p_target_usercnt");		// 대상인원

        try { 
        	connMgr = new DBConnectionManager();
        	connMgr.setAutoCommit(false);
        	
        	// 대상기수에 수강생 등록
    		sql = "\n insert into tz_propose "
    			+ "\n ( "
    			+ "\n        subj "
    			+ "\n      , year "
    			+ "\n      , subjseq "
    			+ "\n      , userid "
    			+ "\n      , comp "
    			+ "\n      , jik "
    			+ "\n      , appdate "
    			+ "\n      , isdinsert "
    			+ "\n      , isb2c "
    			+ "\n      , ischkfirst "
    			+ "\n      , chkfirst "
    			+ "\n      , isproposeapproval "
    			+ "\n      , chiefdistxt "
    			+ "\n      , chiefmail "
    			+ "\n      , chiefuserid "
    			+ "\n      , chiefdate "
    			+ "\n      , chkfinal "
    			+ "\n      , proptxt "
    			+ "\n      , billstat "
    			+ "\n      , ordcode "
    			+ "\n      , cancelkind "
    			+ "\n      , luserid "
    			+ "\n      , ldate "
    			+ "\n      , rejectedreason "
    			+ "\n      , rejectkind "
    			+ "\n ) "
    			+ "\n select subj "
    			+ "\n      , year "
    			+ "\n      , ? as subjseq "
    			+ "\n      , userid "
    			+ "\n      , comp "
    			+ "\n      , jik "
    			+ "\n      , appdate "
    			+ "\n      , isdinsert "
    			+ "\n      , isb2c "
    			+ "\n      , ischkfirst "
    			+ "\n      , chkfirst "
    			+ "\n      , isproposeapproval "
    			+ "\n      , chiefdistxt "
    			+ "\n      , chiefmail "
    			+ "\n      , chiefuserid "
    			+ "\n      , chiefdate "
    			+ "\n      , chkfinal "
    			+ "\n      , proptxt "
    			+ "\n      , billstat "
    			+ "\n      , ordcode "
    			+ "\n      , cancelkind "
    			+ "\n      , luserid "
    			+ "\n      , ldate "
    			+ "\n      , rejectedreason "
    			+ "\n      , rejectkind "
    			+ "\n from   tz_propose "
    			+ "\n where  subj = ? "
    			+ "\n and    year = ? "
    			+ "\n and    subjseq = ? "
    			+ "\n and    rownum <= ? ";

    		pstmt   = connMgr.prepareStatement(sql);

    		pstmt.setString(1, v_targetSubjseq);
    		pstmt.setString(2, v_subj);
    		pstmt.setString(3, v_year);
    		pstmt.setString(4, v_subjseq);
    		pstmt.setInt   (5, v_targetUsercnt);

    		isOk    = pstmt.executeUpdate();
    		if ( pstmt != null ) { pstmt.close(); }
			if ( isOk > 0) {
				// 이전 과정기수 수강신청 테이블에서 삭제
				sql = "\n delete from tz_propose "
					+ "\n where  subj    = ? "
					+ "\n and    year    = ? "
					+ "\n and    subjseq = ? "
					+ "\n and    rownum <= ? ";
				
	    		pstmt   = connMgr.prepareStatement(sql);

	    		pstmt.setString(1, v_subj);
	    		pstmt.setString(2, v_year);
	    		pstmt.setString(3, v_subjseq);
	    		pstmt.setInt   (4, v_targetUsercnt);

	    		isOk    = pstmt.executeUpdate();
	    		if ( pstmt != null ) { pstmt.close(); }
			}
			
			if ( isOk > 0) { 
				if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e ) { } }
			} else { 
				if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e ) { } }
			}
			
        } catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, "");
			throw new Exception("\n e.getMessage() : [\n" + ex.getMessage() + "\n]");
        } finally { 
			if ( pstmt != null ) { 
				try { 
					pstmt.close();  
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
	 * 과정기수별 폐강처리
	 * @param box
	 * @return
	 * @throws Exception 
	 */
	public int deleteSubjseq(RequestBox box) throws Exception {
        int isOk = 0;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_userid  = box.getSession("userid");
        
        String v_subj    = box.getString("p_subj");		// 과정코드
        String v_year    = box.getString("p_year");		// 년도
        String v_subjseq = box.getString("p_subjseq");	// 기수
        
        try { 
        	connMgr = new DBConnectionManager();
        	connMgr.setAutoCommit(false);
        	
        	// 수강신청취소 테이블에 등록
    		sql = "\n insert into tz_cancel "
    			+ "\n ( "
    			+ "\n        subj "
    			+ "\n      , year "
    			+ "\n      , subjseq "
    			+ "\n      , userid "
    			+ "\n      , seq "
    			+ "\n      , cancelkind "
    			+ "\n      , canceldate "
    			+ "\n      , reason "
    			+ "\n      , luserid "
    			+ "\n      , ldate "
    			+ "\n      , reasoncd "
    			+ "\n      , isdeleted "
    			+ "\n ) "
    			+ "\n select subj "
    			+ "\n      , year "
    			+ "\n      , subjseq "
    			+ "\n      , userid "
    			+ "\n      , (select nvl(max(seq),0)+1 from tz_cancel where subj = a.subj and year = a.year and subjseq = a.subjseq and userid = a.userid) "
    			+ "\n      , 'F' "
    			+ "\n      , to_char(sysdate,'yyyymmddhh24miss') "
    			+ "\n      , '폐강' "
    			+ "\n      , ? "
    			+ "\n      , to_char(sysdate,'yyyymmddhh24miss') as ldate "
    			+ "\n      , '03' /* 폐강에 의한 취소 */ "
    			+ "\n      , 'Y'  /* 폐강여부 */ "
    			+ "\n from   tz_propose a "
    			+ "\n where  subj = ? "
    			+ "\n and    year = ? "
    			+ "\n and    subjseq = ? ";

    		pstmt   = connMgr.prepareStatement(sql);

    		pstmt.setString(1, v_userid);
    		pstmt.setString(2, v_subj);
    		pstmt.setString(3, v_year);
    		pstmt.setString(4, v_subjseq);

    		isOk    = pstmt.executeUpdate();
    		if ( pstmt != null ) { pstmt.close(); }

			if ( isOk > 0) {
				// 수강신청 테이블에서 삭제
				sql = "delete from tz_propose "
					+ "where  subj    = ? "
					+ "and    year    = ? "
					+ "and    subjseq = ? ";
				
	    		pstmt   = connMgr.prepareStatement(sql);

	    		pstmt.setString(1, v_subj);
	    		pstmt.setString(2, v_year);
	    		pstmt.setString(3, v_subjseq);

	    		isOk    = pstmt.executeUpdate();
	    		if ( pstmt != null ) { pstmt.close(); }
			}
			
			// 과정기수 테이블 수정
			sql = "update tz_subjseq "
				+ "set    isdeleted = 'Y' "
				+ "     , isvisible = 'N' "
				+ "where  subj    = ? "
				+ "and    year    = ? "
				+ "and    subjseq = ? ";
			
    		pstmt   = connMgr.prepareStatement(sql);

    		pstmt.setString(1, v_subj);
    		pstmt.setString(2, v_year);
    		pstmt.setString(3, v_subjseq);

    		isOk    = pstmt.executeUpdate();
    		if ( pstmt != null ) { pstmt.close(); }
			if ( isOk > 0) { 
				if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e ) { } }
			} else { 
				if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e ) { } }
			}
			
        } catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, "");
			throw new Exception("\n e.getMessage() : [\n" + ex.getMessage() + "\n]");
        } finally { 
			if ( pstmt != null ) { 
				try { 
					pstmt.close();  
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
}