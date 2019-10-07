// **********************************************************
// 1. 제      목: 학습 종료 관련 BEAN 
// 2. 프로그램명: FinishBean.java
// 3. 개      요: 학습 종료 관련 BEAN
// 4. 환      경: JDK 1.4
// 5. 버      젼: 0.1
// 6. 작      성: S.W.Kang 2004. 12. 5
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.complete          ;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import com.ziaan.common.GetCodenm;
import com.ziaan.common.MileageManager;
import com.ziaan.course.SubjseqData;
import com.ziaan.library.CalcUtil;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.propose.AcceptFileToDBBean;
import com.ziaan.scorm.StringUtil;
import com.ziaan.study.StudyStatusData;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class FinishBean { 
    public static final int     FINISH_COMPLETE         = 0; // 수료처리 종료
    public static final int     FINISH_CANCEL           = 1; // 수료취소 가능
    public static final int     FINISH_PROCESS          = 3; // 수료처리
    public static final int     SCORE_COMPUTE           = 4; // 점수재계산

    public static final String  ONOFF_GUBUN             = "0004"  ;
    public static final String  SUBJ_NOT_INCLUDE_COURSE = "000000";
    public static Logger logger = Logger.getLogger(FinishBean.class);
    public FinishBean() { }


    /**
    수료처리 리스트 화면
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectCompleteList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        ListSet 			ls2 				= null;        
        StringBuffer        sbSQL               = new StringBuffer("");
        ArrayList           list                = new ArrayList();
        ArrayList           list2               = new ArrayList();        
        
        DataBox             dbox                = null;
        DataBox             dbox2               = null;                                                       
                                                
        String              v_grcode            = box.getStringDefault("s_grcode","ALL");
        String              v_gyear             = box.getString("s_gyear");
        String              v_grseq             = box.getStringDefault("s_grseq","ALL");
        String              v_uclass            = box.getStringDefault("s_upperclass","ALL");
        String              v_mclass            = box.getStringDefault("s_middleclass","ALL");
        String              v_lclass            = box.getStringDefault("s_lowerclass","ALL");
        String              v_subj              = box.getStringDefault("s_subjcourse","ALL");
        String              v_subjseq           = box.getStringDefault("s_subjseq","ALL");
                            
        String              v_orderColumn       = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String              v_orderType         = box.getString("p_orderType");                 // 정렬할 순서
        
        String  v_Bcourse    = ""; // 이전코스
        String  v_course     = ""; // 현재코스
        String  v_Bcourseseq = ""; // 이전코스기수
        String  v_courseseq  = ""; // 현재코스기수
        String sql2 ="";

        
        try { 
            connMgr = new DBConnectionManager();
        
            sbSQL.append(" select  a.subj                                                                       \n")
                 .append("     ,   a.year                                                                       \n")
                 .append("     ,   a.subjseq                                                                    \n")
                 .append("     ,   a.subjseqgr                                                                  \n")
                 .append("     ,   a.subjnm                                                                     \n")               
                 .append("     ,   a.edustart 									                                \n")
                 .append("     ,   a.eduend                               										\n")
                 .append("     ,   a.isclosed                                                                   \n")
                 .append("     ,   a.isonoff                                                                    \n")
                 .append("     ,   get_codenm('0004',a.isonoff)isonoffval                                       \n")                 
                 .append("     ,   a.iscpresult                                                                 \n")           // 외주업체결과등록
                 .append("     ,   a.iscpflag                                                                   \n")           // 외주업체결과승인
                 .append("     ,   a.isoutsourcing                                                              \n")           // 위탁교육
                 .append("     ,   (                                                                            \n")
                 .append("             select  count(*)                                                         \n")
                 .append("             from    tz_student                                                       \n")
                 .append("             where   subj    = a.subj                                                 \n")
                 .append("             and     year    = a.year                                                 \n")
                 .append("             and     subjseq = a.subjseq                                              \n")
                 .append("         )                                       studentcnt                           \n")
                 .append("     ,   decode(a.isclosed, 'Y', (                                                    \n")
                 .append("                                     select  count(*)                                 \n")
                 .append("                                     from    tz_stold                                 \n")
                 .append("                                     where   subj        = a.subj                     \n")
                 .append("                                     and     year        = a.year                     \n")
                 .append("                                     and     isgraduated = 'Y'                        \n")
                 .append("                                     and     subjseq     = a.subjseq                  \n")
                 .append("                                  ), 0)          stoldycnt                            \n")
                 
                 .append("     ,   decode(a.isclosed, 'N', (                                                    \n")
                 .append("                                     select  count(*)                                 \n")
                 .append("                                     from    tz_stold                                 \n")
                 .append("                                     where   subj        = a.subj                     \n")
                 .append("                                     and     year        = a.year                     \n")
                 .append("                                     and     isgraduated = 'Y'                        \n")
                 .append("                                     and     subjseq     = a.subjseq                  \n")
                 .append("                                  ), 0)          stoldycnt1                            \n")
                 
                 .append("     ,   decode(a.isclosed, 'Y', (                                                    \n")
                 .append("                                     select  count(*)                                 \n")
                 .append("                                     from    tz_stold                                 \n")
                 .append("                                     where   subj        = a.subj                     \n")
                 .append("                                     and     year        = a.year                     \n")
                 .append("                                     and     isgraduated = 'N'                        \n")
                 .append("                                     and     subjseq     = a.subjseq                  \n")
                 .append("                                 ), 0)           stoldncnt                            \n")
                 
                 .append("     ,   decode(a.isclosed, 'N', (                                                    \n")
                 .append("                                     select  count(*)                                 \n")
                 .append("                                     from    tz_stold                                 \n")
                 .append("                                     where   subj        = a.subj                     \n")
                 .append("                                     and     year        = a.year                     \n")
                 .append("                                     and     isgraduated = 'N'                        \n")
                 .append("                                     and     subjseq     = a.subjseq                  \n")
                 .append("                                 ), 0)           stoldncnt1                            \n")
                 
                 .append("     ,   (                                                                            \n")
                 .append("             select  max(ldate)                                                       \n")
                 .append("             from    tz_stold x                                                       \n")
                 .append("             where   x.subj      = a.subj                                             \n")
                 .append("             and     x.year      = a.year                                             \n")
                 .append("             and     x.subjseq   = a.subjseq                                          \n")
                 .append("         )                                       stolddate                            \n")
                 .append("     ,   (                                                                            \n")
                 .append("             select  recalcudate                                                      \n")
                 .append("             from    tz_subjseq x                                                     \n")
                 .append("             where   x.subj      = a.subj                                             \n")
                 .append("             and     x.year      = a.year                                             \n")
                 .append("             and     x.subjseq   = a.subjseq                                          \n")
                 .append("         )                                       recalcudate                          \n")
                 .append("     , a.course                                                                       \n")
                 .append("     , a.cyear                                                                        \n")
                 .append("     , a.courseseq                                                                    \n")
                 .append("     , a.coursenm                                                                     \n")                 
                 .append("     , b.ldate course_stolddate														\n")
                 .append(" from    vz_scsubjseq    a, (															\n")
                 .append("	select course, cyear, courseseq, max(ldate) ldate									\n")
                 .append("	from tz_coursestold																	\n")
                 .append("	group by course, cyear, courseseq													\n")
                 .append(" ) b															                        \n")
                 .append(" where   a.course = b.course(+)                                                       \n")
                 .append(" and     a.cyear = b.cyear(+)															\n")
                 .append(" and     a.courseseq = b.courseseq(+)													\n");
                
            if ( !v_grcode.equals("ALL") && !v_grcode.equals("----") ) { 
                sbSQL.append(" and a.grcode         = " + SQLString.Format(v_grcode ) + "                       \n");
            }
            
            if ( !v_gyear.equals("ALL") ) { 
                sbSQL.append(" and a.gyear          = " + SQLString.Format(v_gyear  ) + "                       \n");
            }
            
            if ( !v_grseq.equals("ALL") ) { 
                sbSQL.append(" and a.grseq          = " + SQLString.Format(v_grseq  ) + "                       \n");
            }
            
            if ( !v_uclass.equals("ALL") ) { 
                sbSQL.append(" and a.scupperclass   = " + SQLString.Format(v_uclass ) + "                       \n");
            }
            
            if ( !v_mclass.equals("ALL") ) { 
                sbSQL.append(" and a.scmiddleclass  = " + SQLString.Format(v_mclass ) + "                       \n");
            }   
            
            if ( !v_lclass.equals("ALL") ) { 
                sbSQL.append(" and a.sclowerclass   = " + SQLString.Format(v_lclass ) + "                       \n");
            }
            
            if ( !v_subj.equals("ALL") ) { 
                sbSQL.append(" and a.scsubj         = " + SQLString.Format(v_subj   ) + "                       \n");
            }
            
            if ( !v_subjseq.equals("ALL") ) { 
                sbSQL.append(" and a.scsubjseq      = " + SQLString.Format(v_subjseq) + "                       \n");
            }
                
            if ( v_orderColumn.equals("") ) { 
                sbSQL.append(" order by a.course, a.cyear, a.courseseq, a.subjnm, a.subjseq                                                     \n");
            } else { 
                sbSQL.append(" order by a.course, a.cyear, a.courseseq, " + v_orderColumn + v_orderType + "                                     \n");
            }
            
          //  System.out.println(this.getClass().getName() + "." + "SelectCompleteList() Printing Order  ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
System.out.println("수료 ::::::::::::::" + sbSQL.toString());                
            ls  = connMgr.executeQuery(sbSQL.toString());
                
            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
             
                dbox.put("d_studentcnt"     , new Integer( ls.getInt("studentcnt"   )));
                dbox.put("d_stoldycnt"      , new Integer( ls.getInt("stoldycnt"    )));
                dbox.put("d_stoldncnt"      , new Integer( ls.getInt("stoldncnt"    )));
                dbox.put("d_stoldncnt1"      , new Integer( ls.getInt("stoldncnt1"    )));
               
                
                if ( dbox.getInt("d_studentcnt") > 0) { 
                    list.add(dbox); 
                }                                
            }
            
            for ( int i = 0;i < list.size(); i++ ) { 
                dbox2       =   (DataBox)list.get(i);
                v_course    =   dbox2.getString("d_course");
                v_courseseq =   dbox2.getString("d_courseseq");
                if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                    sql2 = "select count(subj) cnt, sum(case when isclosed ='Y' then 1 else 0 end) sum from TZ_SUBJSEQ where 1=1 \n";
                    sql2 += "and course = " + StringManager.makeSQL(v_course) + " and courseseq = " +StringManager.makeSQL(v_courseseq) + "	\n";
                         
                    if ( !v_grcode.equals("ALL") ) { 
                        sql2 += " and grcode = " + StringManager.makeSQL(v_grcode) + "	\n";
                    }
                    if ( !v_gyear.equals("ALL") ) { 
                        sql2 += " and gyear = " + StringManager.makeSQL(v_gyear) + "	\n";
                    }
                    if ( !v_grseq.equals("ALL") ) { 
                        sql2 += " and grseq = " + StringManager.makeSQL(v_grseq) + "	\n";
                    }
                    
                    if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
                    ls2 = connMgr.executeQuery(sql2);	
                    if(ls2.next()){
                        dbox2.put("d_rowspan", new Integer(ls2.getInt("cnt")));
                        dbox2.put("d_sum", new Integer(ls2.getInt("sum")));
                        dbox2.put("d_isnewcourse", "Y");                            
                    }
                }else{
                	dbox2.put("d_rowspan", new Integer(0));
                	dbox2.put("d_sum", new Integer(0));
                    dbox2.put("d_isnewcourse", "N");
                }
                v_Bcourse   =   v_course;
                v_Bcourseseq=   v_courseseq;
                list2.add(dbox2);
                if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }

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

 // 학습자 인적분석
    public ArrayList ScoreCntList(RequestBox box) throws Exception { 
        DBConnectionManager    connMgr = null;
        ListSet ls1         = null;
        ArrayList list2     = null;
        String sql1         = "";
        DataBox             dbox                = null;
        
        String  			ss_grcode   = box.getStringDefault( "s_grcode"		  , "ALL" );  // 교육그룹
        String              ss_gyear    = box.getStringDefault( "s_gyear"         , "ALL" );  // 년도
        String              ss_subjcourse=box.getStringDefault( "s_subjcourse"    , "ALL" );  // 과목&코스
        String              ss_subjseq  = box.getStringDefault( "s_subjseq"       , "ALL" );  // 과목 기수

        try { 

                connMgr = new DBConnectionManager();
                list2 = new ArrayList();


	
	                sql1  = "\n   SELECT editscore, COUNT(*) as cnt   ";

	                sql1 += "\n   FROM TZ_STUDENT a";
	                sql1 += "\n   WHERE subjseq = " +SQLString.Format(ss_subjseq);
	                sql1 += "\n   and subj = " +SQLString.Format(ss_subjcourse);
	                sql1 += "\n   and year = " +SQLString.Format(ss_gyear);
	                sql1 += "\n   AND SCORE >= 60 ";
	                sql1 += "\n   and editscore >= 80 ";
	                sql1 += "\n   AND ETC2 <> 0 ";
	                sql1 += "\n   group by a.editscore ";
	                sql1 += "\n   order by editscore desc ";
                    
                ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                	 dbox    = ls1.getDataBox();
                	 list2.add(dbox); 
                }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list2;
    }
    
    
    
    
    /**
    수료처리 리스트 화면
    @param box          receive from the form object and session
    @return ArrayList
    */
    /*public ArrayList SelectCompleteList_OLD(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        ArrayList list = new ArrayList();
        CourseFinishData  data = null;
        FinishData     subdata = null;

        Hashtable finishinfo = null;

        String v_grcode   = box.getStringDefault("s_grcode","ALL");
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getStringDefault("s_grseq","ALL");
        String v_uclass   = box.getStringDefault("s_upperclass","ALL");
        String v_mclass   = box.getStringDefault("s_middleclass","ALL");
        String v_lclass   = box.getStringDefault("s_lowerclass","ALL");
        String v_subj     = box.getStringDefault("s_subjcourse","ALL");
        String v_subjseq  = box.getStringDefault("s_subjseq","ALL");

        String v_action   = box.getStringDefault("p_action","change");

        String v_course_bef = "";
        String v_temp = "";
        String v_approvalstatus = "";

        try { 
            if ( v_action.equals("go") ) { 
                sql = "select a.subj,      a.year,    a.subjseq, a.subjseqgr,   a.subjnm,";
                sql += "       a.grcode,    a.gyear,   a.grseq,   ";
                sql += "       a.course,    a.cyear,   a.courseseq, a.coursenm, ";
                sql += "       a.propstart, a.propend, nvl(a.edustart,'') edustart,  nvl(a.eduend,'') eduend,  ";
                sql += "       a.isclosed,  a.isonoff, b.codenm isonoffnm,a.contenttype  ";
                // sql += "		  nvl((select isapproval from tz_approval where subj=a.subj and year=a.year and subjseq=a.subjseq and gubun=4 and rownum=1),'') approvalstatus ";
                sql += "  from vz_scsubjseq a, ";
                sql += "       tz_code      b  ";
                sql += " where a.isonoff = b.code ";
                sql += "   and b.gubun   = " + SQLString.Format(FinishBean.ONOFF_GUBUN);
                if ( !v_grcode.equals("ALL") ) { 
                    sql += "   and a.grcode = " + SQLString.Format(v_grcode);
                }
                if ( !v_gyear.equals("ALL") ) { 
                    sql += "   and a.gyear = " + SQLString.Format(v_gyear);
                }
                if ( !v_grseq.equals("ALL") ) { 
                    sql += "   and a.grseq = " + SQLString.Format(v_grseq);
                }
                if ( !v_uclass.equals("ALL") ) { 
                    sql += "   and a.scupperclass = " + SQLString.Format(v_uclass);
                }
                if ( !v_mclass.equals("ALL") ) { 
                    sql += "   and a.scmiddleclass = " + SQLString.Format(v_mclass);
                }
                if ( !v_lclass.equals("ALL") ) { 
                    sql += "   and a.sclowerclass = " + SQLString.Format(v_lclass);
                }
                if ( !v_subj.equals("ALL") ) { 
                    sql += "   and a.scsubj = " + SQLString.Format(v_subj);
                }
                if ( !v_subjseq.equals("ALL") ) { 
                    sql += "   and a.scsubjseq = " + SQLString.Format(v_subjseq);
                }
                sql += " order by a.grcode, a.gyear, a.grseq, a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq ";

				// //System.out.println("sql == > " + sql);

                connMgr = new DBConnectionManager();
                ls = connMgr.executeQuery(sql);
                
                while ( ls.next() ) { 
                    if ( !v_course_bef.equals( ls.getString("course") +ls.getString("cyear") +ls.getString("courseseq")) || ls.getString("course").equals(FinishBean.SUBJ_NOT_INCLUDE_COURSE)) { 
                        data = new CourseFinishData();
                        data.setCourse( ls.getString("course") );
                        data.setCyear( ls.getString("cyear") );
                        data.setCourseseq( ls.getString("courseseq") );
                        data.setCoursenm( ls.getString("coursenm") );
                        data.setGrcode( ls.getString("grcode") );
                        data.setGyear( ls.getString("gyear") );
                        data.setGrseq( ls.getString("grseq") );
                    }
                    subdata = new FinishData();

                    // 과목기수 정보
                    subdata.setSubj( ls.getString("subj") );
                    subdata.setYear( ls.getString("year") );
                    subdata.setSubjseq( ls.getString("subjseq") );
                    subdata.setSubjseqgr( ls.getString("subjseqgr") );
                    subdata.setSubjnm( ls.getString("subjnm") );
                    subdata.setPropstart( ls.getString("propstart") );
                    subdata.setPropend( ls.getString("propend") );
                    subdata.setEdustart( ls.getString("edustart") );
                    subdata.setEduend( ls.getString("eduend") );
                    subdata.setIsclosed( ls.getString("isclosed") );
                    subdata.setIsonoff( ls.getString("isonoff") );
                    subdata.setIsonoffnm( ls.getString("isonoffnm") );
                    subdata.setContenttype( ls.getString("contenttype") );

                    // v_approvalstatus = ls.getString("approvalstatus");
                    // subdata.setApprovalstatus(v_approvalstatus); // 결재정보
                    
                    // if      (v_approvalstatus.equals("Y")) subdata.setApprovalstatusdesc("결재완료");
                    // else if ( v_approvalstatus.equals("N")) subdata.setApprovalstatusdesc("반려");
                    // else if ( v_approvalstatus.equals("B")) subdata.setApprovalstatusdesc("상신중");
                    // else if ( v_approvalstatus.equals("M")) subdata.setApprovalstatusdesc("미상신");
                    // else subdata.setApprovalstatusdesc("");

                    // 학습자 숫자 정보
                    finishinfo = getFinishListInfo(connMgr, subdata.getSubj(), subdata.getYear(), subdata.getSubjseq() );
                    subdata.setProposecnt(Integer.parseInt((String)finishinfo.get("proposecnt")));
                    subdata.setFirstapprovecnt(Integer.parseInt((String)finishinfo.get("firstapprovecnt")));
                    subdata.setFinalapprovecnt(Integer.parseInt((String)finishinfo.get("finalapprovecnt")));
                    subdata.setNotyetapprovecnt(Integer.parseInt((String)finishinfo.get("notyetapprovecnt")));// 미승인
                    subdata.setStudentcnt(Integer.parseInt((String)finishinfo.get("studentcnt")));
                    subdata.setGradcnt(Integer.parseInt((String)finishinfo.get("stoldgradcnt")));
                    subdata.setNotgradcnt(Integer.parseInt((String)finishinfo.get("stoldnotgradcnt")));
                    setFinishListInfo(subdata, finishinfo);
                    
                    data.add(subdata);
                    if ( !v_course_bef.equals(data.getCourse() +data.getCyear() +data.getCourseseq() ) || data.getCourse().equals(FinishBean.SUBJ_NOT_INCLUDE_COURSE)) { 
                        list.add(data);
                        v_course_bef = data.getCourse() +data.getCyear() +data.getCourseseq();
                    }
                }
            }

            // 코스 관련 메시지 계산
            // String  coursecompletemsg;
            // double  coursecompleterate;
            for ( int i = 0; i<list.size(); i++ ) { 
                data = (CourseFinishData)list.get(i);
                if ( !data.equals(FinishBean.SUBJ_NOT_INCLUDE_COURSE)) { 
                    data.setCoursecompletemsg("Y");

                    finishinfo = getCourseStudentCntInfo(connMgr, data.getCourse(), data.getCyear(), subdata.getSubjseq() );
                    data.setCoursestudentcnt(subdata.getStudentcnt() );
                    data.setCoursegradcnt(getInt((String)finishinfo.get("coursegradcnt")));
                    if ( data.getCoursestudentcnt() == 0 ) { 
                        data.setCoursecompleterate(0);
                    } else { 
                        data.setCoursecompleterate(data.getCoursegradcnt()/data.getCoursestudentcnt()*100);
                    }

                    for ( int k=0; k<data.size(); k++ ) { 
                        subdata = (FinishData)data.get(k);
                        if ( subdata.getIsclosed().equals("N") ) { 
                            data.setCoursecompletemsg("N");
                        }
                    }
                }
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
        return list;
    }
    */
    
    
    /**
    수료처리 상세 화면(수강생 목록)
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList CompleteStudentList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ListSet             ls                  = null;
        ListSet             ls2                 = null;
        ListSet             ls3                 = null;
        ListSet             ls4                 = null;
        ArrayList           list                = null;
        ArrayList           list2               = null;
        ArrayList           list3               = null;
        DataBox             dbox                = null;
        DataBox             dbox2               = null;
        DataBox             dbox3               = null;
        DataBox             dboxgradu           = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        String              v_notgraducddesc    = "";
        String 				sql 				= "";
        String 				sql2 				= "";
        
        int                 iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String              s_grcode            = box.getStringDefault("s_grcode"       , "ALL");
        String              s_gyear             = box.getString       ("s_gyear"               );
        String              s_grseq             = box.getStringDefault("s_grseq"        , "ALL");
        String              s_uclass            = box.getStringDefault("s_upperclass"   , "ALL");
        String              s_mclass            = box.getStringDefault("s_middleclass"  , "ALL");
        String              s_lclass            = box.getStringDefault("s_lowerclass"   , "ALL");
        String              s_subj              = box.getStringDefault("s_subjcourse"   , "ALL");
        String              s_subjseq           = box.getStringDefault("s_subjseq"      , "ALL");

        
        String              v_subj              = box.getString       ("p_subj"         );
        String              v_year              = box.getString       ("p_year"         );
        String              v_subjseq           = box.getString       ("p_subjseq"      );
        String              v_isonoff           = box.getString       ("p_isonoff"      );
        String              v_isclosed          =  "N";
        
        String              v_orderColumn       = box.getString       ("p_orderColumn"  );  // 정렬할 컬럼명
        String              v_orderType         = box.getString       ("p_orderType"    );  // 정렬할 순서
        
        int              v_mgubun         = box.getInt       ("p_mgubun");  // 수료완료 구분 1:이면 수료가 안됨, 0이면 수료완료가 됨.
        
        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();
            list2   = new ArrayList();
            list3   = new ArrayList();
            
            sbSQL.append(" select  isclosed                                                 \n")
                 .append(" from    tz_subjseq                                               \n")
                 .append(" where   subj    = " + StringManager.makeSQL(v_subj       ) + "   \n")
                 .append(" and     year    = " + StringManager.makeSQL(v_year       ) + "   \n")
                 .append(" and     subjseq = " + StringManager.makeSQL(v_subjseq    ) + "   \n");
                 
            ls2 = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls2.next() ) { 
                v_isclosed = ls2.getString(1);
            } 
            
            sbSQL.setLength(0);

            sbSQL.append(" select   ROW_NUMBER() OVER(ORDER BY  a.score  DESC,a.avmtest DESC,a.avreport  DESC, a.avftest  DESC, a.avetc2  DESC, fn_crypt('2', b.birth_date, 'knise') asc   ) AS RANKING      \n")
                 .append("     ,   a.userid                                                                  \n")
                 .append("     ,   a.score                                                                  \n")
                 .append("     ,   a.tstep                                                                  \n")
                 .append("     ,   a.mtest                                                                  \n")
                 .append("     ,   a.ftest                                                                  \n")
                 .append("     ,   a.htest                                                                  \n")
                 .append("     ,   a.act                                                                    \n")
                 .append("     ,   a.report                                                                 \n")
               //  .append("     ,   a.etc1                                                                   \n")
                 .append("     ,(select count(*) as cnt  from  tz_attendance f , vz_scsubjseq c where a.subj=f.subj and a.year=f.year and b.userid=f.userid and f.subj=c.subj and f.subjseq=c.subjseq and f.year=c.year and isattend ='O' and  f.attdate between  substr(c.EDUSTART,0,8) and substr(c.EDUEND,0,8)) as etc1  \n")
                 .append("     ,   a.etc2                                                                   \n")
                 .append("     ,   a.avtstep                                                                \n")
                 .append("     ,   a.avmtest                                                                \n")
                 .append("     ,   a.avftest                                                                \n")
                 .append("     ,   a.avhtest                                                                \n")
                 .append("     ,   a.avact                                                                  \n")
                 .append("     ,   a.avreport                                                               \n")
                 .append("     ,   a.avetc1                                                                 \n")
                 .append("     ,   a.avetc2                                                                 \n")
                 .append("     ,   a.isgraduated                                                            \n")
                 .append("     ,   decode(a.isgraduated, 'Y', '수료', '미수료') isgraduateddesc              \n")
                 .append("     ,   '' branchnm                                             					\n")
                 .append("     ,   a.ldate                                                                  \n")
				 .append("     ,   nvl(b.position_nm,'-') as position_nm									\n")
				 .append("     ,   b.lvl_nm																	\n")				 
				 .append("     ,   a.study_count                                           					\n")
				 .append("     ,   a.study_time                                            					\n")
	             .append("     ,   b.name                                                         			\n")
	             .append("     ,   b.hometel                                                	 			\n")
	             .append("     ,   b.handphone                                               				\n")
	             .append("     ,   b.email                                                   				\n")
	             .append("     ,   get_compnm(b.comp) compnm                                   				\n")  
            
            	 .append("     ,   a.subjseq                                                   				\n")
            	 .append("     ,   fn_crypt('2', b.birth_date, 'knise') birth_date                                                    				\n")
            	 .append("     ,   b.user_path                                                   			\n")
            	 .append("     ,   a.etc1                                                   				\n")
            	 .append("     ,   a.etc2, b.cert ,a.editlink,a.editscore                    				\n");
            	 
            
            if ( v_isclosed.equals("Y") && v_mgubun ==0 ) { 
                // 수료처리 완료시 TZ_STOLD TABLE에서 조회

                sbSQL.append("     ,   a.notgraducd                                                         \n")
                     .append("     ,   a.notgraduetc                                                        \n")
                     .append("     ,   (                                                                    \n")
                     .append("             select  codenm                                                   \n")
                     .append("             from    tz_code                                                  \n")
                     .append("             where   gubun   = '0028'                                         \n")
                     .append("             and     code    = a.notgraduetc                                  \n")
                     .append("            )           notgraduetcdesc                                       \n")
                     .append("     ,   0           samtotal                                                 \n")
                     .append("     ,   a.serno                                                 \n")
                     .append(" from    tz_stold             a                                               \n");
            } else { 
                // 수료처리 미완료시 TZ_STUDENT TABLE에서 조회
                sbSQL.append("     ,    (                                                                   \n")
                     .append("              select  notgraducd                                              \n")
                     .append("              from    tz_stold                                                \n")
                     .append("              where   subj    = a.subj                                        \n")
                     .append("              and     year    = a.year                                        \n")
                     .append("              and     subjseq = a.subjseq                                     \n")
                     .append("              and     userid  = a.userid                                      \n")
                     .append("          )           notgraducd                                              \n")
                     .append("      ,   a.notgraduetc                                                       \n")
                     .append("      ,   (                                                                   \n")
                     .append("              select  codenm                                                  \n")
                     .append("              from    tz_code                                                 \n")
                     .append("              where   gubun   = '0028'                                        \n")
                     .append("              and     code    = a.notgraduetc                                 \n")
                     .append("          )          notgraduetcdesc                                          \n")
                     .append("      ,   a.samtotal  samtotal                                                \n")
                     .append("      ,   a.serno  			                                                \n")
                     .append(" from     tz_student a                                                        \n");
            }

            sbSQL.append("       , tz_member b												                \n")
                 .append(" where   a.userid    = b.userid(+)								                \n")
                 .append(" and     a.subj      = " + StringManager.makeSQL( v_subj    ) + "                 \n")
                 .append(" and     a.year      = " + StringManager.makeSQL( v_year    ) + "                 \n")
                 .append(" and     a.subjseq   = " + StringManager.makeSQL( v_subjseq ) + "                 \n")
                 .append(" and     a.score   >=60                                                           \n")  //총점이 60점 이상인 경우의 대상자만 해당된다.
                 .append(" and     a.etc2   <> 0                                                           \n")  //총점이 60점 이상인 경우의 대상자만 해당된다.
                 .append(" order by a.score desc,a.avmtest desc, a.avreport desc, a.avftest desc, a.avetc2 desc, fn_crypt('2', b.birth_date, 'knise') asc                \n");
                 
          //  if ( v_orderColumn.equals("") ) { 
          //      sbSQL.append(" order by a.score  DESC,a.avreport,a.avftest, a.avetc2, b.birth_date                \n");
          //  } else { 
          //      sbSQL.append(" order by " + v_orderColumn + v_orderType + " \n");
          //  }
            
          //  System.out.println(this.getClass().getName() + "." + "CompleteStudentList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            System.out.println("수료페지-----------------------------------------\n" +sbSQL.toString());         
            ls  = connMgr.executeQuery(sbSQL.toString());
                        
            while ( ls.next() ) { 
                dbox    = ls.getDataBox();

                if ("RC".equals(v_isonoff)) {
	                // 독서교육 개월차 평가결과
	                sql = "\n select t1.subj "
	                	+ "\n      , t1.year "
	                	+ "\n      , t1.subjseq "
	                	+ "\n      , t1.userid "
	                	+ "\n      , t1.month "
	                    + "\n      , t1.bookcode "
	                    + "\n      , decode(nvl(t2.subj,'N'),'N','N','Y') as exam_yn "
	                    + "\n      , nvl(t3.final_yn,'N') as final_yn "
	                    + "\n      , nvl(t3.marking_yn,'N') as marking_yn "
	                    + "\n      , nvl(t3.totalscore,0) as totalscore "
	                    + "\n from   tz_proposebook t1 "
	                    + "\n      , tz_bookexam_paper t2 "
	                    + "\n      , tz_bookexam_result t3 "
	                    + "\n where  t1.subj = t2.subj(+) "
	                    + "\n and    t1.year = t2.year(+) "
	                    + "\n and    t1.subjseq = t2.subjseq(+) "
	                    + "\n and    t1.month = t2.month(+) "
	                    + "\n and    t1.bookcode = t2.bookcode(+) "
	                    + "\n and    t1.subj = t3.subj(+) "
	                    + "\n and    t1.year = t3.year(+) "
	                    + "\n and    t1.subjseq = t3.subjseq(+) "
	                    + "\n and    t1.month = t3.month(+) "
	                    + "\n and    t1.bookcode = t3.bookcode(+) "
	                    + "\n and    t1.userid = t3.userid(+) "
	                    + "\n and    t1.subj = " + StringManager.makeSQL(v_subj) 
	                    + "\n and    t1.year = " + StringManager.makeSQL(v_year)
	                    + "\n and    t1.subjseq = " + StringManager.makeSQL(v_subjseq)
	                    + "\n and    t1.userid = " + StringManager.makeSQL(ls.getString("userid"));
	                ls3 = connMgr.executeQuery(sql);
	                while ( ls3.next() ) { 
	                	dbox2 = ls3.getDataBox();
	                	dbox2.put("d_totalscore"	, new Double( ls3.getDouble("totalscore"))); 
	                	list2.add(dbox2);
	                }
	                if ( ls3 != null )  { try { ls3.close(); } catch ( Exception e ) { } }
	                
	                dbox.put("bookexam_result", list2);
                }
                
                if("ON".equals(v_isonoff) || "OFF".equals(v_isonoff)){
                	sql2 = "select " 
                		 +  "     ( " 
                		 +  "      select count(distinct projgubun) \n " 
                		 +  "        from tz_projgrp \n " 
                		 +  "       where subj = "+StringManager.makeSQL(v_subj)+ " \n " 
                		 +  "         and year = " + StringManager.makeSQL(v_year) + " \n "
                		 +  "         and subjseq = " + StringManager.makeSQL(v_subjseq) + " \n "
                		 +  "     ) as proj_cnt, \n " 
                		 +  "     ( \n " 
                		 +  "      select count(distinct grpseq) \n " 
                		 +  "        from tz_projrep \n " 
                		 +  "        where subj = "+StringManager.makeSQL(v_subj)+ " \n "  
                		 +  "        and year = " + StringManager.makeSQL(v_year) + " \n "
                		 +  "        and subjseq = " + StringManager.makeSQL(v_subjseq) + " \n "
                		 +  "        and projid = " + StringManager.makeSQL(ls.getString("userid")) + " \n "
                		 +  "     ) as proj_user_cnt, \n " 
                		 +  "     ( \n " 
                		 +  "      select count(papernum) \n " 
                		 +  "        from tz_exampaper \n " 
                		 +  "       where subj = "+StringManager.makeSQL(v_subj)+ " \n " 
                		 +  "         and year = " + StringManager.makeSQL(v_year) + " \n "
                		 +  "         and subjseq = " + StringManager.makeSQL(v_subjseq) + " \n "
                		 +  "         and examtype in ('M') \n " 
                		 +  "     ) as exam_cnt_m, \n " 
                		 +  "     ( \n " 
                		 +  "      select count(papernum) \n " 
                		 +  "        from tz_exampaper \n " 
                		 +  "       where subj = "+StringManager.makeSQL(v_subj)+ " \n "  
                		 +  "         and year = " + StringManager.makeSQL(v_year) + " \n "
                		 +  "         and subjseq = " + StringManager.makeSQL(v_subjseq) + " \n "
                		 +  "         and examtype in ('E') \n " 
                		 +  "     ) as exam_cnt_e, \n " 
                		 +  "     ( \n " 
                		 +  "      select count(score) \n " 
                		 +  "       from tz_examresult \n " 
                		 +  "      where subj = "+StringManager.makeSQL(v_subj)+ " \n "  
                		 +  "        and year = " + StringManager.makeSQL(v_year) + " \n "  
                		 +  "        and subjseq = " + StringManager.makeSQL(v_subjseq) + " \n " 
                		 +  "        and examtype = 'M' \n " 
                		 +  "        and userid = " + StringManager.makeSQL(ls.getString("userid")) + " \n " 
                		 +  "     ) as exam_user_cnt_m, \n " 
                		 +  "     ( \n " 
                		 +  "      select count(score) \n " 
                		 +  "       from tz_examresult \n " 
                		 +  "      where subj = "+StringManager.makeSQL(v_subj)+ " \n " 
                		 +  "        and year = " + StringManager.makeSQL(v_year) + " \n "  
                		 +  "        and subjseq = " + StringManager.makeSQL(v_subjseq) + " \n "
                		 +  "        and examtype = 'E' \n " 
                		 +  "        and userid = " + StringManager.makeSQL(ls.getString("userid")) + " \n " 
                		 +  "     ) as exam_user_cnt_e \n " 
                		 +  " from dual \n ";
                	
	                ls4 = connMgr.executeQuery(sql2);
	                while ( ls4.next() ) { 
	                	dbox3 = ls4.getDataBox();
	                	list3.add(dbox3);
	                }
	                if ( ls4 != null )  { try { ls4.close(); } catch ( Exception e ) { } }
	                
	                dbox.put("reportexamInfo", list3);
                }
                list.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
        }

        return list;        
    }
    
    
    /**
    수료처리 상세 화면(수강생 목록)
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList OffStudentInfo(String v_year, String v_subj, String v_subjseq, String v_userid) throws Exception { 
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ListSet             ls                  = null;
        ListSet             ls2                 = null;
        ArrayList           list                = null;
        DataBox             dbox                = null;
        DataBox             dboxgradu           = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        String              v_notgraducddesc    = "";
        
        int                 iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();
            
            sbSQL.setLength(0);

            sbSQL.append(" select  a.userid                                                                 \n")
                 .append("     ,   a.score                                                                  \n")
                 .append("     ,   a.tstep                                                                  \n")
                 .append("     ,   a.mtest                                                                  \n")
                 .append("     ,   a.ftest                                                                  \n")
                 .append("     ,   a.htest                                                                  \n")
                 .append("     ,   a.act                                                                    \n")
                 .append("     ,   a.report                                                                 \n")
                 .append("     ,   a.etc1                                                                   \n")
                 .append("     ,   a.etc2                                                                   \n")
                 .append("     ,   a.avtstep                                                                \n")
                 .append("     ,   a.avmtest                                                                \n")
                 .append("     ,   a.avftest                                                                \n")
                 .append("     ,   a.avhtest                                                                \n")
                 .append("     ,   a.avact                                                                  \n")
                 .append("     ,   a.avreport                                                               \n")
                 .append("     ,   a.avetc1                                                                 \n")
                 .append("     ,   a.avetc2                                                                 \n")
                 .append("     ,   a.isgraduated                                                            \n")
                 .append("     ,   decode(a.isgraduated, 'Y', '수료', '미수료') isgraduateddesc               \n")
                 .append("     ,   a.ldate                                                                  \n")
                 .append(" from     tz_student a                                                            \n");

            sbSQL.append(" where   a.subj      = " + StringManager.makeSQL( v_subj    ) + "                 \n")
                 .append(" and     a.year      = " + StringManager.makeSQL( v_year    ) + "                 \n")
                 .append(" and     a.subjseq   = " + StringManager.makeSQL( v_subjseq ) + "                 \n")
                 .append(" and     a.userid    = " + StringManager.makeSQL( v_userid  ) + "                 \n");
       
            //System.out.println(this.getClass().getName() + "." + "CompleteStudentList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls  = connMgr.executeQuery(sbSQL.toString());
                        
            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                dbox.put("userid"           , ls.getString("userid"     ));
                dbox.put("score"            , new Double( ls.getDouble("score")));      // 취득점수
                dbox.put("d_ldate"          , ls.getString("ldate"      ));             // 수정일자
                                
                dbox.put("tstep"            , new Integer( ls.getInt   ("tstep" )));    // 가중치적용-진도율
                dbox.put("mtest"            , new Integer( ls.getInt   ("mtest" )));    // 가중치적용-중간평가평균
                dbox.put("ftest"            , new Integer( ls.getInt   ("ftest" )));    // 가중치적용-최종평가평균
                dbox.put("htest"            , new Integer( ls.getInt   ("htest" )));    // 가중치적용-형성평가평균
                dbox.put("d_sumtest"        , new Double ( ls.getDouble("mtest") + ls.getDouble("ftest") + ls.getDouble("htest") )); // -평가총점
                dbox.put("act"              , new Integer( ls.getInt   ("act"   )));    // 가중치적용-액티비티점수
                dbox.put("report"           , new Integer( ls.getInt   ("report")));    // 가중치적용-리포트평균
                dbox.put("etc1"             , new Integer( ls.getInt   ("etc1"  )));    // 가중치적용-참여도
                dbox.put("etc2"             , new Integer( ls.getInt   ("etc2"  )));    // 가중치적용-토론참여도
                                
                dbox.put("avtstep"          , new Double( ls.getDouble("avtstep")));    // 가중치적용-진도율
                dbox.put("avmtest"          , new Double( ls.getDouble("avmtest")));    // 가중치적용-중간평가평균
                dbox.put("avftest"          , new Double( ls.getDouble("avftest")));    // 가중치적용-최종평가평균
                dbox.put("avhtest"          , new Double( ls.getDouble("avhtest")));    // 가중치적용-형성평가평균
                dbox.put("d_sumavtest"      , new Double( ls.getDouble("avmtest") + ls.getDouble("avftest") + ls.getDouble("avhtest") )); // 가중치적용-평가총점
                dbox.put("avact"            , new Double( ls.getDouble("avact"  )));    // 가중치적용-액티비티점수
                dbox.put("avreport"         , new Double( ls.getDouble("avreport")));   // 가중치적용-리포트평균
                dbox.put("avetc1"           , new Double( ls.getDouble("avetc1" )));    // 가중치적용-참여도(출석)
                dbox.put("avetc2"           , new Double( ls.getDouble("avetc2" )));    // 가중치적용-참여도
                dbox.put("d_sumavetc"           , new Double( ls.getDouble("avetc1") + ls.getDouble("avetc2"))); // 가중치적용-기타총점
                
                list.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, null, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
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

        return list;        
    }
    
    
    
	public String getNotGraduCodeName(DBConnectionManager connMgr, String v_NotGraduCd) throws Exception { 
		StringTokenizer st = new StringTokenizer(v_NotGraduCd, ",");
        String token = "";
        String              sql     = "";
        String v_notgraducddesc = "";
        
        ListSet 	ls   			= null;
        
        try { 
	        for ( int i = 0; st.hasMoreElements(); i++ ) { 
	        	v_notgraducddesc = "";
	        	int v_tokensize = st.countTokens();
	        	for ( int j = 0; j<v_tokensize ; j++ ) { 
	        		token = StringManager.trim(st.nextToken() );
	        		sql = "select codenm from tz_code where gubun = '0028' and code = '" + token + "'";
	        		ls = connMgr.executeQuery(sql);
	        		if ( ls.next() ) { 
	        		// 	//System.out.println("ls name = " + ls.getString("codenm") );
	        			v_notgraducddesc +=ls.getString("codenm") + ",";
	        		}
	            	// //System.out.println("j = " +j + "  token=" +token);
	            	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
	            }
	            
	            if ( !v_notgraducddesc.equals("") ) { 
	            	v_notgraducddesc=v_notgraducddesc.substring(0,v_notgraducddesc.length()-1);
	            }
	        }
	    }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return v_notgraducddesc;
	}

    public Hashtable getCourseStudentCntInfo(DBConnectionManager connMgr, String p_course, String p_cyear, String p_courseseq) throws Exception { 
        Hashtable finishinfo = new Hashtable();
        ListSet             ls      = null;
        String sql  = "";

        int v_coursegradcnt    = 0;

        try { 
            // 수료인원
            sql = "select count(*) studentcnt, ";
            sql += "       sum(decode(ISGRADUATED,'Y',1,0)) gradcnt ";
            sql += "  from tz_coursestold ";
            sql += " where course    = " + SQLString.Format(p_course);
            sql += "   and cyear     = " + SQLString.Format(p_cyear);
            sql += "   and courseseq = " + SQLString.Format(p_courseseq);
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_coursegradcnt = ls.getInt("gradcnt");

                finishinfo.put("coursegradcnt", String.valueOf(v_coursegradcnt));
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return finishinfo;
    }

    public Hashtable getFinishListInfo(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        Hashtable finishinfo = new Hashtable();
        ListSet             ls      = null;
        String sql  = "";

        int v_proposecnt      = 0;
        int v_firstapprovecnt = 0;
        int v_finalapprovecnt = 0;
        int v_notyetfirstapprovecnt = 0;
        int v_notyetfinalapprovecnt = 0;
        int v_notyetapprovecnt = 0; // 미승인

        int v_studentcnt     = 0;
        int v_studentgradcnt = 0;

        int v_stoldcnt       = 0; // 수료처리 총인원
        int v_stoldgradcnt   = 0; // 수료 인원
        int v_stoldnotgradcnt= 0; // 미수료 인원

        try { 
            // 신청인원, 1차 승인인원, 2차 승인인원(최종승인),미승인인원
            sql = "select count(*) proposecnt, ";
          
			sql += "       sum(decode(chkfirst,'Y',1,0)) firstapprovecnt, ";	// 1차 승인인원
            sql += "       sum(decode(chkfinal,'Y',1,0)) finalapprovecnt,  "; // 2차 승인인원
            sql += "       sum(decode(chkfirst,NULL,1,'N',1,0)) notyetfirstapprovecnt, "; // 1차 미승인인원
			sql += "       sum(decode(chkfinal,NULL,1,'N',1,0)) notyetfinalapprovecnt,  ";// 2차 미승인인원
			sql += "       sum(decode(chkfinal,'M',1,'B',1,0)) notyetapprovecnt  ";		 // 미승인
            sql += "  from tz_propose ";
            sql += " where subj    = " + SQLString.Format(p_subj);
            sql += "   and year    = " + SQLString.Format(p_year);
            sql += "   and subjseq = " + SQLString.Format(p_subjseq);
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_proposecnt      = ls.getInt("proposecnt");
                v_firstapprovecnt = ls.getInt("firstapprovecnt");
                v_finalapprovecnt = ls.getInt("finalapprovecnt");
                v_notyetfirstapprovecnt = ls.getInt("notyetfirstapprovecnt");
                v_notyetfinalapprovecnt = ls.getInt("notyetfinalapprovecnt");
                v_notyetapprovecnt 		= ls.getInt("notyetapprovecnt");

                finishinfo.put("proposecnt",      String.valueOf(v_proposecnt));
                finishinfo.put("firstapprovecnt", String.valueOf(v_firstapprovecnt));
                finishinfo.put("finalapprovecnt", String.valueOf(v_finalapprovecnt));
                finishinfo.put("notyetfirstapprovecnt", String.valueOf(v_notyetfirstapprovecnt));
                finishinfo.put("notyetfinalapprovecnt", String.valueOf(v_notyetfinalapprovecnt));
                finishinfo.put("notyetapprovecnt", 		String.valueOf(v_notyetapprovecnt)); // 미승인
            }

            // 입과인원
            sql = "select count(*) studentcnt, ";
            sql += "       sum(decode(ISGRADUATED,'Y',1,0)) studentgradcnt ";
            sql += "  from tz_student ";
            sql += " where subj    = " + SQLString.Format(p_subj);
            sql += "   and year    = " + SQLString.Format(p_year);
            sql += "   and subjseq = " + SQLString.Format(p_subjseq);
            ls.close();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_studentcnt = ls.getInt("studentcnt");
                v_studentgradcnt = ls.getInt("studentgradcnt");

                finishinfo.put("studentcnt", String.valueOf(v_studentcnt));
                finishinfo.put("studentgradcnt", String.valueOf(v_studentgradcnt));
            }

            // 수료인원, 미수료인원
            sql = "select count(*) stoldcnt, ";
            sql += "       sum(decode(ISGRADUATED,'Y',1,0)) stoldgradcnt, ";
            sql += "       sum(decode(ISGRADUATED,'N',1,0)) stoldnotgradcnt ";
            sql += "  from tz_stold ";
            sql += " where subj    = " + SQLString.Format(p_subj);
            sql += "   and year    = " + SQLString.Format(p_year);
            sql += "   and subjseq = " + SQLString.Format(p_subjseq);
            ls.close();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_stoldcnt     = ls.getInt("stoldcnt");
                v_stoldgradcnt = ls.getInt("stoldgradcnt");
                v_stoldnotgradcnt = ls.getInt("stoldnotgradcnt");

                finishinfo.put("stoldcnt"		, String.valueOf(v_stoldcnt));
                finishinfo.put("stoldgradcnt"	, String.valueOf(v_stoldgradcnt));
                finishinfo.put("stoldnotgradcnt"	, String.valueOf(v_stoldnotgradcnt));
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return finishinfo;
    }

    public int getInt(String string) throws Exception { 
        int value = 0;
        if ( string.equals("") || string == null ) { 
            value = 0;
        } else { 
            value = Integer.valueOf(string).intValue();
        }
        return value;
    }


    public void setFinishListInfo(FinishData data, Hashtable info)  throws Exception { 
        String currdate = FormatDate.getDate("yyyyMMddhh");

        // 신청상태 메시지 [이전|진행중|완료]
        if ( Integer.valueOf(currdate).intValue() <  getInt(data.getPropstart() )) { 
            data.setProposemsg("이전");
        } else if  (Integer.valueOf(currdate).intValue() > getInt(data.getPropend() )) { 
            data.setProposemsg("완료");
        } else  { 
            data.setProposemsg("진행중");
        }

        // 교육 메시지 [이전|교육중|종료]
        if ( Integer.valueOf(currdate).intValue() < getInt(data.getEdustart() )) { 
            data.setProposemsg("이전");
        } else if  (Integer.valueOf(currdate).intValue() > getInt(data.getEduend() )) { 
            data.setProposemsg("종료");
        } else  { 
            data.setProposemsg("교육중");
        }

        // 승인1상태 메시지 [대상없음|00명 미처리|완료]
        if ( Integer.parseInt((String)info.get("proposecnt")) == 0 ) { 
            data.setFirstapprovemsg("대상<br > 없음");
        } else if ( Integer.parseInt((String)info.get("proposecnt")) == Integer.parseInt((String)info.get("firstapprovecnt"))) { 
            data.setFirstapprovemsg("완료");
        } else { 
            data.setFirstapprovemsg((String)info.get("notyetfirstapprovecnt") + "명<br > 미처리");
        }

        // 승인2상태 메시지 [대상없음|00명 미처리|완료]
        if ( Integer.parseInt((String)info.get("proposecnt")) == 0 ) { 
            data.setFinalapprovemsg("대상<br > 없음");
        } else if ( Integer.parseInt((String)info.get("proposecnt")) == Integer.parseInt((String)info.get("finalapprovecnt"))) { 
            data.setFinalapprovemsg("완료");
        } else { 
            data.setFinalapprovemsg((String)info.get("notyetfinalapprovecnt") + "명<br > 미처리");
        }

        // 교육메시지

        // 과목상태 메시지 [교육중|미처리|완료]
        int nTemp0 = 0;
        int nTemp1 = 0;
        if ( data.getIsclosed().equals("N") ) { 
            nTemp0 = Integer.parseInt((String)info.get("studentgradcnt") );
            nTemp1 = Integer.parseInt((String)info.get("studentcnt") );
            if ( nTemp1 == 0 ) { 
                data.setSubjectcompleterate(0);
            } else { 
                data.setSubjectcompleterate((float)nTemp0/nTemp1*100);
            }
            
            if ( Integer.valueOf(currdate).intValue() <= getInt(data.getEduend() )) { 
                data.setSubjectcompletemsg("교육중");
            } else if ( !data.getEduend().equals("") && Integer.valueOf(currdate).intValue() > getInt(data.getEduend() )) { 
            	data.setSubjectcompletemsg("완료");
            } else { 
                // data.setSubjectcompletemsg("미처리");
                data.setSubjectcompletemsg("");
            }
        } else { 
            nTemp0 = Integer.parseInt((String)info.get("stoldgradcnt") );
            nTemp1 = Integer.parseInt((String)info.get("stoldcnt") );
            if ( nTemp1 == 0 ) { 
                data.setSubjectcompleterate(0);
            } else { 
                data.setSubjectcompleterate((float)nTemp0/nTemp1*100);
            }
            data.setSubjectcompletemsg("완료");
        }

		// 결재처리 버튼 - 2005.01.01 이후
		// 결재 완료시(Y) - 수료처리 종료(FINISH_COMPLETE)
		// 결재 미완료시-수료처리시 - 수료처리 취소 가능(FINISH_CANCEL)
		// 결재 미완료시-수료미처리시 - 수료처리 가능(FINISH_PROCESS)
		
		// 결재정보가져오기
		
		if ( data.getApprovalstatus().equals("Y") ) { // 결재완료시
			// 결재 완료
			data.setSubjectaction(FinishBean.FINISH_COMPLETE);
		} else { 
			// 수료처리 완료시 취소가능
			if ( data.getIsclosed().equals("Y") ) { 
				data.setSubjectaction(FinishBean.FINISH_CANCEL);
			} else  { 
				// 과목이 종료되어야 수료처리 가능
				// 과목운영날짜가 있고 수강생이 있을경우에만 수료처리 가능

				if ( !data.getEduend().equals("")&& Integer.parseInt((String)info.get("studentcnt")) != 0 ) { 
					if ( Integer.valueOf(currdate).intValue() > getInt(data.getEduend() ))
					data.setSubjectaction(FinishBean.FINISH_PROCESS);
				}
			}
		}

        // 과목처리 버튼 [처리완료|처리완료 +취소|수료처리|수료율체크] - 이전
        // if ( data.getIsclosed().equals("Y") ) { 
        //    // 수료처리 했으나 학습종료일 이후 2일 이내면 취소버튼 생김
        //    if  (FormatDate.datediff("d",currdate, data.getEduend() ) > 2) { 
        //        data.setSubjectaction(FinishBean.FINISH_COMPLETE);
        //    } else { 
        //        data.setSubjectaction(FinishBean.FINISH_CANCEL);
        //    }
        // } else { 
        //    if  (Integer.valueOf(currdate).intValue() > getInt(data.getEduend() )) { 
        //        data.setSubjectaction(FinishBean.FINISH_PROCESS);
        //    } else { 
        //        data.setSubjectaction(FinishBean.SCORE_COMPUTE);
        //    }
        // }

    }


    /**
    * 과목기수 정보
    *@param box          receive from the form object and session
    *@return ArrayList
    */
    public SubjseqData getSubjseqInfo(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        ListSet         ls      = null;
        StringBuffer    sbSQL   = new StringBuffer("");
        SubjseqData     data    = new SubjseqData();
        
        int             iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            // 과목기수 정보
            sbSQL.append(" select  b.isclosed  , substr(b.edustart,1,8) edustart    , substr(b.eduend, 1, 8) eduend \n")
                 .append("     ,   b.wstep     , b.wmtest      , b.wftest                   \n")
                 .append("     ,   b.whtest    , b.wreport     , b.wact                     \n")
                 .append("     ,   b.wetc1     , b.wetc2       , b.gradscore                \n")
                 .append("     ,   b.gradstep  , b.gradexam    , b.gradftest                \n")
                 .append("     ,   b.gradhtest , b.gradreport  , b.grcode                   \n")
                 .append("     ,   b.grseq     , b.gyear       , b.subjnm                   \n")
                 .append("     ,   b.gradexam_flag  , b.gradftest_flag   , b.gradhtest_flag \n")
                 .append("     ,   b.gradreport_flag, a.isonoff                             \n")
                 .append("     ,   b.isblended , b.isattendchk, b.attstime, b.attetime      \n")
                 .append("     ,   b.study_count, b.study_time							    \n")
                 .append(" from    tz_subj     a                                            \n")
                 .append("     ,   tz_subjseq  b                                            \n")
                 .append(" where   a.subj    = b.subj                                       \n")
                 .append(" and     b.subj    = " + SQLString.Format(p_subj     ) + "        \n")
                 .append(" and     b.year    = " + SQLString.Format(p_year     ) + "        \n")
                 .append(" and     b.subjseq = " + SQLString.Format(p_subjseq  ) + "        \n");
                 
//            //System.out.println(this.getClass().getName() + "." + "getSubjseqInfo() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            logger.info(this.getClass().getName() + "");
            logger.info(sbSQL.toString());
            
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                data.setIsclosed    (      ls.getString("isclosed"      ));
                data.setEdustart    (      ls.getString("edustart"      ));
                data.setEduend      (      ls.getString("eduend"        ));
                                    
                data.setWstep       ( (int)ls.getDouble("wstep"         ));
                data.setWmtest      ( (int)ls.getDouble("wmtest"        ));
                data.setWftest      ( (int)ls.getDouble("wftest"        ));
                data.setWhtest      ( (int)ls.getDouble("whtest"        ));
                data.setWreport     ( (int)ls.getDouble("wreport"       ));
                data.setWact        ( (int)ls.getDouble("wact"          ));
                data.setWetc1       ( (int)ls.getDouble("wetc1"         ));
                data.setWetc2       ( (int)ls.getDouble("wetc2"         ));

                data.setGradscore   (      ls.getInt("gradscore"        ));
                data.setGradstep    (      ls.getInt("gradstep"         ));
                data.setGradexam    (      ls.getInt("gradexam"         ));
                data.setGradftest   (      ls.getInt("gradftest"        ));
                data.setGradhtest   (      ls.getInt("gradhtest"        ));
                data.setGradreport  (      ls.getInt("gradreport"       ));
                data.setGradexam_flag  (   ls.getString("gradexam_flag"  ));
                data.setGradftest_flag (   ls.getString("gradftest_flag" ));
                data.setGradhtest_flag (   ls.getString("gradhtest_flag" ));
                data.setGradreport_flag(   ls.getString("gradreport_flag"));
                
                data.setGrcode      (      ls.getString("grcode"        ));
                data.setGyear       (      ls.getString("gyear"         ));
                data.setGrseq       (      ls.getString("grseq"         ));
                data.setSubjnm      (      ls.getString("subjnm"        ));
                data.setGrcodenm    (      GetCodenm.get_grcodenm(data.getGrcode()));
                data.setGrseqnm     (      GetCodenm.get_grseqnm (data.getGrcode(), data.getGyear(), data.getGrseq()));

                data.setIsonoff     (      ls.getString("isonoff"       ));
//                data.setIsBlended   (      ls.getString("isblended"     ));
                data.setIsattendchk (      ls.getString("isattendchk"   ));
                data.setAttstime    (      ls.getString("attstime"     ));
                data.setAttetime    (      ls.getString("attetime"     ));

                data.setStudy_count(ls.getInt("study_count")); // 수료기준-접속횟수
                data.setStudy_time (ls.getInt("study_time" )); // 수료기준-학습시간
            }
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
        }

        return data;
    }


	/**
    수료처리 상세화면 인포정보
    @param box          receive from the form object and session
    @return ArrayList
    */       
    public DataBox getSubjseqInfoDbox(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        // 과목기수 정보
        sql = " select "; //b.isgoyong,   
        // decode((select count(*) from tz_stold where subj=b.subj and year=b.year and subjseq=b.subjseq),0,'N','Y') isclosed,  
        sql += "        b.isclosed,   ";
        sql += "        b.edustart, b.eduend,  b.wstep,      b.wmtest,    b.wftest,   b.whtest,	b.wreport, ";
        sql += "        b.wact,       b.wetc1,     b.wetc2, ";
        sql += "        b.gradscore,  b.gradstep,  b.gradexam, b.gradftest, b.gradhtest, b.gradreport,";
        sql += "        b.grcode,     b.grseq,     b.gyear,    b.subjnm, ";
        sql += "        b.isblended,                                                ";
        sql += "        b.gradftest_flag,     b.gradhtest_flag,     b.gradexam_flag,    b.gradreport_flag, ";
        sql += "        DECODE(b.gradftest_flag , 'R', '필수', '선택') gradftest_flagName  ,              \n";
        sql += "        DECODE(b.gradhtest_flag , 'R', '필수', '선택') gradhtest_flagName  ,              \n";
        sql += "        DECODE(b.gradexam_flag  , 'R', '필수', '선택') gradexam_flagName   ,              \n";
        sql += "        DECODE(b.gradreport_flag, 'R', '필수', '선택') gradreport_flagName ,              \n";
        sql += "        a.isonoff,    b.biyong,	  (select grcodenm from tz_grcode where grcode = b.grcode) grcodenm,  ";
        sql += "		   a.isoutsourcing, ";
        sql += "		   b.iscpresult, ";
        sql += "		   b.subjseqgr,";
        sql += "		   b.iscpflag, ";
        // sql += "		   nvl((select isapproval from tz_approval where gubun = 4 and subj=b.subj and year=b.year and subjseq=b.subjseq),'') isapproval, ";
        sql += "		   (select count(*) from tz_student where subj=b.subj and year=b.year and subjseq=b.subjseq) studentcnt, ";
        sql += "		   (select count(*) from tz_stold where subj=b.subj and year=b.year and subjseq=b.subjseq) stoldcnt, ";
        sql += "		   (select count(*) from tz_stold where subj=b.subj and year=b.year and subjseq=b.subjseq and isgraduated='Y') stoldycnt, ";
        sql += "		   (select count(*) from tz_stold where subj=b.subj and year=b.year and subjseq=b.subjseq and isgraduated='N') stoldncnt ";
        sql += "      , nvl(b.study_count,0) as study_count ";
        sql += "      , nvl(b.study_time,0) as study_time ";
        sql += "      , a.subj_gu ";
        sql += "      , b.mtest_start ";
        sql += "      , b.mtest_end ";
        sql += "      , b.ftest_start ";
        sql += "      , b.ftest_end ";
        sql += "      , b.mreport_start ";
        sql += "      , b.mreport_end ";
        sql += "      , b.freport_start ";
        sql += "      , b.freport_end ";
        sql += "      , a.edutimes ";
        
        sql += "   from tz_subj     a, ";
        sql += "        tz_subjseq  b";
        sql += "  where a.subj    = b.subj ";
        sql += "    and b.subj    = " + SQLString.Format(p_subj);
        sql += "    and b.year    = " + SQLString.Format(p_year);
        sql += "    and b.subjseq = " + SQLString.Format(p_subjseq);

        try { 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) {              
            	dbox = ls.getDataBox();
                //dbox.put("isgoyong",ls.getString("isgoyong") );
                dbox.put("isclosed",ls.getString("isclosed") );
                dbox.put("edustart",ls.getString("edustart") );
                dbox.put("eduend",  ls.getString("eduend") );

                dbox.put("wstep",   new Double( ls.getDouble("wstep")));
                dbox.put("wmtest",  new Double( ls.getDouble("wmtest")));
                dbox.put("wftest",  new Double( ls.getDouble("wftest")));
                dbox.put("whtest",  new Double( ls.getDouble("whtest")));
                dbox.put("wreport", new Double( ls.getDouble("wreport")));
                dbox.put("wact",    new Double( ls.getDouble("wact")));
                dbox.put("wetc1",   new Double( ls.getDouble("wetc1")));
                dbox.put("wetc2",   new Double( ls.getDouble("wetc2")));

                dbox.put("gradscore", new Integer( ls.getInt("gradscore")));
                dbox.put("gradstep",  new Integer( ls.getInt("gradstep")));
                dbox.put("gradexam",  new Integer( ls.getInt("gradexam")));
                dbox.put("gradftest", new Integer( ls.getInt("gradftest")));
                dbox.put("gradhtest", new Integer( ls.getInt("gradhtest")));
                dbox.put("gradreport",new Integer( ls.getInt("gradreport")));
                
                dbox.put("gradexam_flag"  , ls.getString("gradexam_flag"  ));
                dbox.put("gradftest_flag" , ls.getString("gradftest_flag" ));
                dbox.put("gradhtest_flag" , ls.getString("gradhtest_flag" ));
                dbox.put("gradreport_flag", ls.getString("gradreport_flag"));
                
                dbox.put("gradexam_flagname"  , ls.getString("gradexam_flagname"  ));
                dbox.put("gradftest_flagname" , ls.getString("gradftest_flagname" ));
                dbox.put("gradhtest_flagname" , ls.getString("gradhtest_flagname" ));
                dbox.put("gradreport_flagname", ls.getString("gradreport_flagname"));

                dbox.put("grcode",ls.getString("grcode") );
                dbox.put("gyear",ls.getString("gyear") );
                dbox.put("grseq",ls.getString("grseq") );
                dbox.put("subjnm",ls.getString("subjnm") );
                dbox.put("grcodenm",ls.getString("grcodenm") );

                dbox.put("isonoff",ls.getString("isonoff") );
                dbox.put("biyong",new Integer( ls.getInt("biyong")));
                
                dbox.put("isoutsourcing",ls.getString("isoutsourcing") );
                dbox.put("iscpresult",ls.getString("iscpresult") );
                dbox.put("iscpflag",ls.getString("iscpflag") );
                // dbox.put("d_isapproval",ls.getString("isapproval") );
                dbox.put("subjseqgr",ls.getString("subjseqgr") );
                
                dbox.put("d_studentcnt",new Integer( ls.getInt("studentcnt")));
                dbox.put("d_stoldcnt",new Integer( ls.getInt("stoldcnt")));
                dbox.put("d_stoldycnt",new Integer( ls.getInt("stoldycnt")));
                dbox.put("d_stoldncnt",new Integer( ls.getInt("stoldncnt")));

                dbox.put("study_count",new Integer( ls.getInt("study_count")));
                dbox.put("study_time",new Integer( ls.getInt("study_time")));
                dbox.put("subj_gu",ls.getString("subj_gu") );
                dbox.put("d_edutimes",ls.getString("edutimes") );
                
            }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return dbox;
    }

    

    /**
    학습자 커서 (목록)
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList getFinishTargetStudent(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet         ls      = null;
        StringBuffer    sbSQL   = new StringBuffer("");
        ArrayList       list    = new ArrayList();
        StoldData       data    = null;
        
        int             iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            // 학습자 커서
            sbSQL.append(" select  a.subj                                                      \n") 
                 .append("     ,   a.year                                                      \n")
                 .append("     ,   a.subjseq                                                   \n")
                 .append("     ,   a.userid                                                    \n")
                 .append("     ,   b.name                                                      \n")
                 .append("     ,   b.comp                                                      \n")
                 .append("     ,   b.hometel                                                   \n")
                 .append("     ,   b.handphone                                                 \n")
                 .append("     ,   b.email                                                     \n")
                 .append("     ,   a.isb2c                                                     \n")
                 .append("     ,   a.tstep                                                     \n")
                 .append("     ,   a.avtstep                                                   \n")
                 .append("     ,   a.avmtest                                                   \n")
                 .append("     ,   a.avftest                                                   \n")
                 .append("     ,   a.avhtest                                                   \n")
                 .append("     ,   a.avreport                                                  \n")
                 .append("     ,   a.avact                                                     \n")
                 .append("     ,   a.avetc1                                                    \n")
                 .append("     ,   a.avetc2                                                    \n")
                 .append("     ,   a.samtotal                                                  \n")
                 .append("     ,   a.study_count                                               \n")
                 .append("     ,   a.study_time                                                \n")
                 .append("     ,   b.post                                                      \n")
                 .append("     ,   b.dept_cd                                                   \n")
                 .append("     ,   b.job_cd		                                               \n")
                 .append("     ,   a.editlink		                                               \n")
                 .append("     ,   a.editscore		                                               \n")
                 .append("     ,   a.examnum		                                               \n")
                 .append(" from    tz_student  a                                               \n")
                 .append("     ,   tz_member   b                                               \n")
                 .append(" where   a.userid  = b.userid                                        \n")
                 .append(" and     a.subj    = " + SQLString.Format(p_subj     ) + "           \n")
                 .append(" and     a.year    = " + SQLString.Format(p_year     ) + "           \n")
                 .append(" and     a.subjseq = " + SQLString.Format(p_subjseq  ) + "           \n");
                
            if ( !p_userid.equals("ALL") ) { 
                sbSQL.append(" and     a.userid  = " + SQLString.Format(p_userid   ) + "       \n");
            }
            
            sbSQL.append(" order by a.userid                                                   \n");
            
//            //System.out.println(this.getClass().getName() + "." + "getFinishTargetStudent() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
        
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                data    = new StoldData();
                
                data.setSubj        ( ls.getString("subj"       ) );
                data.setYear        ( ls.getString("year"       ) );
                data.setSubjseq     ( ls.getString("subjseq"    ) );
                data.setUserid      ( ls.getString("userid"     ) );
                data.setName        ( ls.getString("name"       ) );
                data.setComp        ( ls.getString("comp"       ) );
                data.setIsb2c       ( ls.getString("isb2c"      ) );
                data.setTstep       ( ls.getDouble("tstep"      ) );
                data.setAvtstep     ( ls.getDouble("avtstep"    ) );
                data.setAvmtest     ( ls.getDouble("avmtest"    ) );
                data.setAvftest     ( ls.getDouble("avftest"    ) );
                data.setAvhtest     ( ls.getDouble("avhtest"    ) );
                data.setAvreport    ( ls.getDouble("avreport"   ) );
                data.setAvact       ( ls.getDouble("avact"      ) );
                data.setAvetc1      ( ls.getDouble("avetc1"     ) );
                data.setAvetc2      ( ls.getDouble("avetc2"     ) );
                data.setSamtotal    ( ls.getDouble("samtotal"   ) );    // 삼진아웃                
                data.setStudy_count ( ls.getInt   ("study_count") );    // 접속횟수                
                data.setStudy_time  ( ls.getInt   ("study_time" ) );    // 학습시간                

                data.setPost        ( ls.getString("post"       ) );    // 직급코드                
                data.setDept_cd     ( ls.getString("dept_cd"    ) );    // 팀코드                
                data.setJob_cd      ( ls.getString("job_cd"     ) );    // 직무코드                
                
                data.setEditlink    ( ls.getDouble("editlink"     ) );    // 조정등수
                data.setEditscore   ( ls.getDouble("editscore"     ) );    // 조정점수                
                data.setExamnum   ( ls.getString("examnum"     ) );    // 수험번호                
                
                list.add(data);
            }
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
        }

        return list;
    }

    
    /**
    학습자 커서 (목록)
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList getFinishTargetStudent2(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid,Vector puserids) throws Exception { 
        ListSet         ls      = null;
        StringBuffer    sbSQL   = new StringBuffer("");
        ArrayList       list    = new ArrayList();
        StoldData       data    = null;
        
        int             iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String auserid ="";
        
          for(int i=0; i < puserids.size();i++){
        	  
        	  auserid=auserid+",'"+puserids.elementAt(i).toString()+"'";
          }
          //System.out.println("substring 1=======>"+auserid);
          auserid = auserid.substring(1);
          //System.out.println("substring 2====>"+auserid);
          

        try { 
            // 학습자 커서
            sbSQL.append(" select  a.subj                                                      \n") 
                 .append("     ,   a.year                                                      \n")
                 .append("     ,   a.subjseq                                                   \n")
                 .append("     ,   a.userid                                                    \n")
                 .append("     ,   b.name                                                      \n")
                 .append("     ,   b.comp                                                      \n")
                 .append("     ,   b.hometel                                                   \n")
                 .append("     ,   b.handphone                                                 \n")
                 .append("     ,   b.email                                                     \n")
                 .append("     ,   a.isb2c                                                     \n")
                 .append("     ,   a.tstep                                                     \n")
                 .append("     ,   a.avtstep                                                   \n")
                 .append("     ,   a.avmtest                                                   \n")
                 .append("     ,   a.avftest                                                   \n")
                 .append("     ,   a.avhtest                                                   \n")
                 .append("     ,   a.avreport                                                  \n")
                 .append("     ,   a.avact                                                     \n")
                 .append("     ,   a.avetc1                                                    \n")
                 .append("     ,   a.avetc2                                                    \n")
                 .append("     ,   a.samtotal                                                  \n")
                 .append("     ,   a.study_count                                               \n")
                 .append("     ,   a.study_time                                                \n")
                 .append("     ,   b.post                                                      \n")
                 .append("     ,   b.dept_cd                                                   \n")
                 .append("     ,   b.job_cd		                                               \n")
                 .append(" from    tz_student  a                                               \n")
                 .append("     ,   tz_member   b                                               \n")
                 .append(" where   a.userid  = b.userid                                        \n")
                 .append(" and     a.subj    = " + SQLString.Format(p_subj     ) + "           \n")
                 .append(" and     a.year    = " + SQLString.Format(p_year     ) + "           \n")
                 .append(" and     a.subjseq = " + SQLString.Format(p_subjseq  ) + "           \n")
                 .append(" and     a.userid  in(" + auserid + " )     \n");
          
            
            sbSQL.append(" order by a.userid                                                   \n");
            
            //System.out.println(this.getClass().getName() + "." + "getFinishTargetStudent() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
        
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                data    = new StoldData();
                
                data.setSubj        ( ls.getString("subj"       ) );
                data.setYear        ( ls.getString("year"       ) );
                data.setSubjseq     ( ls.getString("subjseq"    ) );
                data.setUserid      ( ls.getString("userid"     ) );
                data.setName        ( ls.getString("name"       ) );
                data.setComp        ( ls.getString("comp"       ) );
                data.setIsb2c       ( ls.getString("isb2c"      ) );
                data.setTstep       ( ls.getDouble("tstep"      ) );
                data.setAvtstep     ( ls.getDouble("avtstep"    ) );
                data.setAvmtest     ( ls.getDouble("avmtest"    ) );
                data.setAvftest     ( ls.getDouble("avftest"    ) );
                data.setAvhtest     ( ls.getDouble("avhtest"    ) );
                data.setAvreport    ( ls.getDouble("avreport"   ) );
                data.setAvact       ( ls.getDouble("avact"      ) );
                data.setAvetc1      ( ls.getDouble("avetc1"     ) );
                data.setAvetc2      ( ls.getDouble("avetc2"     ) );
                data.setSamtotal    ( ls.getDouble("samtotal"   ) );    // 삼진아웃                
                data.setStudy_count ( ls.getInt   ("study_count") );    // 접속횟수                
                data.setStudy_time  ( ls.getInt   ("study_time" ) );    // 학습시간                

                data.setPost        ( ls.getString("post"       ) );    // 직급코드                
                data.setDept_cd     ( ls.getString("dept_cd"    ) );    // 팀코드                
                data.setJob_cd      ( ls.getString("job_cd"     ) );    // 직무코드                
                
                list.add(data);
            }
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
        }

        return list;
    }

    /**
    수료정보 삭제
    @param box          receive from the form object and session
    @return int
    */
    public int deleteStoldTable(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
        int                 iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            sbSQL.append(" delete from tz_stold     \n")
                 .append(" where    subj    = ?     \n")
                 .append(" and      year    = ?     \n")
                 .append(" and      subjseq = ?     \n");
                 
            //System.out.println(this.getClass().getName() + "." + "deleteStoldTable() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
        
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString( 1, p_subj      );
            pstmt.setString( 2, p_year      );
            pstmt.setString( 3, p_subjseq   );
            
            isOk    = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            isOk    = -1;
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = -1;
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }
    
    /**
    수료정보 삭제
    @param box          receive from the form object and session
    @return int
    */
    public int deleteStoldTable2(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq,Vector p_userids) throws Exception { 
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
        int                 iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
        	
        	
        	
        	
	            sbSQL.append(" delete from tz_stold     \n")
	                 .append(" where    subj    = ?     \n")
	                 .append(" and      year    = ?     \n")
	                 .append(" and      subjseq = ?     \n")
	                 .append(" and      userid = ?     \n");
	            pstmt   = connMgr.prepareStatement(sbSQL.toString());
	                 
	           // System.out.println(this.getClass().getName() + "." + "deleteStoldTable() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
	            for(int i=0;i< p_userids.size();i++){
		            
		            
		            pstmt.setString( 1, p_subj      );
		            pstmt.setString( 2, p_year      );
		            pstmt.setString( 3, p_subjseq   );
		            pstmt.setString( 4, p_userids.elementAt(i).toString()   );
		            
		            isOk    = pstmt.executeUpdate();
        	    }
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            isOk    = -1;
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = -1;
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }


    public int chkRemainReport(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet ls1 =  null;
        ListSet ls2 =  null;
        String              sql     = "";

        int v_allcnt = 0;
        int v_notcnt = 0;
        
        try { 
            // 미채점된 Report존재여부 체크
            //// sql = " select count(distinct ordseq) allcnt";
            //// sql += "   from tz_projrep ";
            //// sql += "  where subj    = " + SQLString.Format(p_subj);
            //// sql += "    and year    = " + SQLString.Format(p_year);
            //// sql += "    and subjseq = " + SQLString.Format(p_subjseq);
            //// sql += "    and projid = " + SQLString.Format(p_userid);
            //// ls1 = connMgr.executeQuery(sql);
            //// while ( ls1.next() ) { 
            ////    v_allcnt = ls1.getInt("allcnt");
            //// }

        	/*
            sql = " select count(distinct ordseq) notcnt";
            sql += "   from tz_projrep ";
            sql += "  where subj    = " + SQLString.Format(p_subj);
            sql += "    and year    = " + SQLString.Format(p_year);
            sql += "    and subjseq = " + SQLString.Format(p_subjseq);
            sql += "    and projid = " + SQLString.Format(p_userid);
            sql += "    and score is not null ";
            // sql += "    and score > 0 ";
            sql += "    and isfinal = 'N' ";
            */
        	sql = "\n select count(distinct a.grpseq) notcnt "
        		+ "\n from   tz_projassign a, tz_projrep b "
                + "\n where  a.subj = b.subj "
                + "\n and    a.year = b.year "
                + "\n and    a.subjseq = b.subjseq "
                + "\n and    a.userid = b.projid "
                + "\n and    a.grpseq = b.grpseq "
                + "\n and    a.subj    = " + SQLString.Format(p_subj)
                + "\n and    a.year    = " + SQLString.Format(p_year)
                + "\n and    a.subjseq = " + SQLString.Format(p_subjseq)
                + "\n and    a.userid  = " + SQLString.Format(p_userid)
                + "\n and    (a.totalscore is null or a.totalscore = '') "
                + "\n and    nvl(b.isfinal,'N') = 'Y' ";
                
            ls2 = connMgr.executeQuery(sql);
            while ( ls2.next() ) { 
                v_notcnt = ls2.getInt("notcnt");
            }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
        }

        //// return (v_allcnt - v_notcnt);
        return v_notcnt;

    }
    
    
    /**
    재계산일자 업데이트
    @param box          receive from the form object and session
    @return int
    */
    public int UpdateRecalcudate(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        sql = " update tz_subjseq ";
        sql +="     set recalcudate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
        sql += "  where subj     = ? ";
        sql += "    and year     = ? ";
        sql += "    and subjseq  = ? ";

        try { 
            pstmt = connMgr.prepareStatement(sql);
        
            pstmt.setString( 1, p_subj);
            pstmt.setString( 2, p_year);
            pstmt.setString( 3, p_subjseq);
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        }
        catch ( Exception ex ) { 
            isOk = -1;
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


	/**
    수료필드 업데이트
    @param box          receive from the form object and session
    @return int
    */
    public int setCloseColumn(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_isclosed) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        sql = " update tz_subjseq ";
        sql +="     set isclosed = ? ";
        sql += "  where subj     = ? ";
        sql += "    and year     = ? ";
        sql += "    and subjseq  = ? ";

        try {
            
            pstmt = connMgr.prepareStatement(sql);
        
            pstmt.setString( 1, p_isclosed);
            pstmt.setString( 2, p_subj);
            pstmt.setString( 3, p_year);
            pstmt.setString( 4, p_subjseq);
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        }
        catch ( Exception ex ) { 
            isOk = -1;
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


    /**
    사이버 과목 수료처리
    @param box      receive from the form object
    @return int
    */
    public int SubjectComplete(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 1;
        ArrayList           list        = null;                                 // 수료대상학생 정보 커서
        StoldData           data        = null;
        SubjseqData         subjseqdata = null;
        
        int                 iSysAdd     = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        // 수료처리시 필요한 기본 변수들
        String              v_subj      = box.getString("p_subj"    );
        String              v_year      = box.getString("p_year"    );
        String              v_subjseq   = box.getString("p_subjseq" );
        String              v_userid    = "";
        String              v_luserid   = box.getSession("userid"   );
        String              v_currdate  = FormatDate.getDate("yyyyMMddHH");     // 현재시각 년 +월 +일 +시
        int                 v_biyong    = 0;                                    // 수강료
        
        // 수료번호 -  수료처리 결재완료시 수료번호를 생성한다.
        int                 v_serno_cnt = 0;
        String              v_serno     = "";
        double              v_samtotal  = 0;

        boolean             v_isexception = false;
        //      1 : "정상적으로 수료처리 되었습니다."
        //      2 : "이미 수료처리 되었습니다."
        //      3 : "과목시작후 가능합니다.
        //      4 : "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]"
        //      5 : "학습자의 리포트 중 " + String.valueOf(v_remainReportcnt) + " 개가 미채점되었습니다. \n\n수료처리취소후 점검하여 주십시오";
        //      "excaption 발생"
        String              v_return_msg  = "";
        // 삼진아웃 대상자
        AcceptFileToDBBean  afbean        = new AcceptFileToDBBean();
        int                 isafok        = afbean.deleteStroutProc(box);
        
        String  sql = "";
        ListSet ls  = null;
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // 수료처리 완료여부, 학습중 검토
            subjseqdata = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            
            
            logger.info(" 수료여부 : " + subjseqdata);
            
            if ( subjseqdata.getIsclosed().equals("Y") ) { 
                v_return_msg    = "이미 수료처리 되었습니다.";
                isOk            = 2;
                return isOk;
            // } else if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEduend() ))) { 
            } else if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEdustart() ))) { 
                // v_return_msg  = "과목종료후 가능합니다.";
                v_return_msg    = "학습시작후 가능합니다.";
                isOk            = 3;
                return isOk;
            }

            // 수료 정보 삭제
            deleteStoldTable( connMgr, v_subj, v_year, v_subjseq );
            
            // 수료 정보 입력 psmt
            sbSQL.append(" insert into tz_stold                                                                     \n")
                 .append(" (                                                                                        \n")
                 .append("         subj                                , year          , subjseq   , userid         \n")
                 .append("     ,   name                                , comp          , score     , tstep          \n")
                 .append("     ,   mtest                               , ftest         , report    , act            \n")
                 .append("     ,   etc1                                , etc2          , avtstep   , avmtest        \n")
                 .append("     ,   avftest                             , avreport      , avact     , avetc1         \n")
                 .append("     ,   avetc2                              , isgraduated   , isb2c     , edustart       \n")
                 .append("     ,   eduend                              , serno         , isrestudy , luserid        \n")
                 .append("     ,   ldate                               , htest         , avhtest   , notgraducd     \n")
                 .append("     ,   study_count                         , study_time								    \n")
                 .append("     ,   post                                , dept_cd       , job_cd					    \n")
                 .append("     ,   editlink                            , editscore    				    \n")
                 .append(" ) values (                                                                               \n")
                 .append("         ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   to_char(sysdate, 'yyyymmddhh24miss'), ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             					            \n")
                 .append("     ,   ?                                   , ?             , ?				            \n")
                 .append("     ,   ?                                   , ?             				                \n")
                 .append(" )                                                                                        \n");
            
           logger.info(this.getClass().getName() + "." + "SubjectComplete() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());

            // 학습자 커서
            list = getFinishTargetStudent(connMgr, v_subj, v_year, v_subjseq, "ALL");
            
            for ( int i = 0; i<list.size(); i++ ) { 
                data        = (StoldData)list.get(i);

                data.setWstep       ( subjseqdata.getWstep      () );
                data.setWmtest      ( subjseqdata.getWmtest     () );
                data.setWftest      ( subjseqdata.getWftest     () );
                data.setWhtest      ( subjseqdata.getWhtest     () );
                data.setWreport     ( subjseqdata.getWreport    () );
                data.setWact        ( subjseqdata.getWact       () );
                data.setWetc1       ( subjseqdata.getWetc1      () );
                data.setWetc2       ( subjseqdata.getWetc2      () );
                data.setGradscore   ( subjseqdata.getGradscore  () );
                data.setGradstep    ( subjseqdata.getGradstep   () );
                data.setGradexam    ( subjseqdata.getGradexam   () );
                data.setGradreport  ( subjseqdata.getGradreport () );
              //  data.setExamnum     ( subjseqdata.getExamnum () );
                
                v_userid    = data.getUserid    ();
                v_samtotal  = data.getSamtotal  ();  // 삼진아웃

                // 삼진아웃 시 미수료
                /*
                if ( v_samtotal > 2) { 
                    v_isexception   = true;
                    v_return_msg    = v_userid + " 학습자는 삼진아웃 상태입니다. "; 
                    isOk            = 6;
                    return  isOk;                    
                }*/
                
                // 미채점 리포트 갯수 확인 -->온라인 과제로 대체 함 2010.05.20
                int v_remainReportcnt   = chkRemainReport(connMgr, v_subj, v_year, v_subjseq, v_userid);
                
             
                if ( v_remainReportcnt > 0 ) { 
                    v_isexception   = true;
                    v_return_msg    = v_userid + "학습자의 리포트 중 " + String.valueOf(v_remainReportcnt) + " 개가 미채점되었습니다.";
                    isOk            = 5;
                    return isOk;
                }
                
                // 점수 재계산
                try { 
                    calc_score(connMgr, "ALL", v_subj, v_year, v_subjseq, v_userid, v_luserid, data);
                } catch  ( Exception ex ) { 
                    v_isexception   = true;
                    v_return_msg    = "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]";
                    v_return_msg   += ex.getMessage();

                    isOk            = 4;
                    return  isOk;
                }
                
                // 수료정보 INSERT
                if ( data.getIsgraduated().equals("Y") ) { 
                    v_serno_cnt++;
                    
                    // 수료증번호 발급            
                    v_serno         = getCompleteSerno(connMgr, v_subj, v_year, v_subjseq, v_userid);
                    
                    // 학습자 테이블 수료='Y' update
                    updateStudentIsgraduated(connMgr, v_subj, v_year, v_subjseq, v_userid, "Y");         
                } else { 
                    v_serno = "";
                }
                
                pstmt.setString( 1, v_subj                    );
                pstmt.setString( 2, v_year                    );
                pstmt.setString( 3, v_subjseq                 );
                pstmt.setString( 4, v_userid                  );
                                                            
                pstmt.setString( 5, data.getName            ());
                pstmt.setString( 6, data.getComp            ());
                pstmt.setDouble( 7, data.getScore           ());
                pstmt.setDouble( 8, data.getTstep           ());
                pstmt.setDouble( 9, data.getMtest           ());
                                                            
                pstmt.setDouble(10, data.getFtest           ());
                pstmt.setDouble(11, data.getReport          ());
                pstmt.setDouble(12, data.getAct             ());
                pstmt.setDouble(13, data.getEtc1            ());
                                                            
                pstmt.setDouble(14, data.getEtc2            ());
                pstmt.setDouble(15, data.getAvtstep         ());
                pstmt.setDouble(16, data.getAvmtest         ());
                pstmt.setDouble(17, data.getAvftest         ());
                                                            
                pstmt.setDouble(18, data.getAvreport        ());
                pstmt.setDouble(19, data.getAvact           ());
                pstmt.setDouble(20, data.getAvetc1          ());
                pstmt.setDouble(21, data.getAvetc2          ());
                                                            
                pstmt.setString(22, data.getIsgraduated     ());
                pstmt.setString(23, data.getIsb2c           ());
                pstmt.setString(24, subjseqdata.getEdustart ());
                pstmt.setString(25, subjseqdata.getEduend   ());

                pstmt.setString(26, v_serno                   );    // 수료증번호
                pstmt.setString(27, data.getIsrestudy       ());  
                pstmt.setString(28, v_luserid                 );
                pstmt.setDouble(29, data.getHtest           ());
                pstmt.setDouble(30, data.getAvhtest         ());
                pstmt.setString(31, data.getNotgraducd      ());

                pstmt.setInt   (32, data.getStudy_count     ());
                pstmt.setInt   (33, data.getStudy_time      ());

                pstmt.setString(34, data.getPost            ());
                pstmt.setString(35, data.getDept_cd         ());
                pstmt.setString(36, data.getJob_cd          ());
                
                pstmt.setDouble(37, data.getEditlink        ());
                pstmt.setDouble(38, data.getEditscore       ());
              //  pstmt.setString(39, data.getExamnum       ());
                
                isOk    = pstmt.executeUpdate();
                //if ( pstmt != null ) { pstmt.close(); }
                
                // 수료증번호 발급
                isOk    = updateStudentSerno(connMgr, v_serno, v_subj, v_year, v_subjseq, v_userid);
                
                // 수료/미수료 통보 메일 보낼 자리
                // sendFinishMail(connMgr, box, v_userid, subjseqdata.getIsonoff(), subjseqdata.getSubjnm(), data.getIsgraduated(), subjseqdata.getBiyong() );
                
                if ( data.getIsgraduated().equals("N") ) {
                    //if ( !subjseqdata.getIsBlended().equals("Y") ) {                    
                        //MileageManager.insertMileage(connMgr, "00000000000000000006", v_userid);
                    
                        afbean.insertSubjseqStrOut(connMgr, box, v_userid, data.getName(), subjseqdata.getEduend().substring(0, 8), v_year, v_subj, v_subjseq );
                    //}
                }/* else {
                    if ( !subjseqdata.getIsBlended().equals("Y") ) {                    
                        if ( data.getScore() >= 100 )
                            MileageManager.insertMileage(connMgr, "00000000000000000005", v_userid);
                        else
                            MileageManager.insertMileage(connMgr, "00000000000000000004", v_userid);
                    }    
                }    */
           }
            
           // 수료 필드 수정 - isclosed = 'Y'
           isOk        = setCloseColumn(connMgr, v_subj, v_year, v_subjseq, "Y");
        } catch ( SQLException e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( !v_return_msg.equals("") )
                box.put("p_return_msg", v_return_msg);
                
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { }
            }
                
            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception ) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }
        
        return isOk;
    }
    
    
    /**
    사이버 과목 수료처리(체크박스)
    @param box      receive from the form object
    @return int
    */
    public int SubjectComplete2(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 1;
        ArrayList           list        = null;                                 // 수료대상학생 정보 커서
        StoldData           data        = null;
        SubjseqData         subjseqdata = null;
        
        int                 iSysAdd     = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        // 수료처리시 필요한 기본 변수들
        String              v_subj      = box.getString("p_subj"    );
        String              v_year      = box.getString("p_year"    );
        String              v_subjseq   = box.getString("p_subjseq" );
        String              v_userid    = "";
        String              v_luserid   = box.getSession("userid"   );
        String              v_currdate  = FormatDate.getDate("yyyyMMddHH");     // 현재시각 년 +월 +일 +시
        int                 v_biyong    = 0;                                    // 수강료
        
        // 수료번호 -  수료처리 결재완료시 수료번호를 생성한다.
        int                 v_serno_cnt = 0;
        String              v_serno     = "";
        double              v_samtotal  = 0;

        boolean             v_isexception = false;
        //      1 : "정상적으로 수료처리 되었습니다."
        //      2 : "이미 수료처리 되었습니다."
        //      3 : "과목시작후 가능합니다.
        //      4 : "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]"
        //      5 : "학습자의 리포트 중 " + String.valueOf(v_remainReportcnt) + " 개가 미채점되었습니다. \n\n수료처리취소후 점검하여 주십시오";
        //      "excaption 발생"
        String              v_return_msg  = "";
        // 삼진아웃 대상자
        AcceptFileToDBBean  afbean        = new AcceptFileToDBBean();
        int                 isafok        = afbean.deleteStroutProc(box);
        
        String  sql = "";
        ListSet ls  = null;
        
        Vector vecusers = box.getVector("p_userids");
        
        int vlistcnt = box.getInt("p_listcnt");
        
        int vmisu = box.getInt("p_mgubun");
        int vcnt2 = box.getInt("p_cnt2");
        int vcnt3 = box.getInt("p_cnt3");
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
           // 수료처리 완료여부, 학습중 검토
            subjseqdata = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEdustart() ))) { 
                // v_return_msg  = "과목종료후 가능합니다.";
                v_return_msg    = "학습시작후 가능합니다.";
                isOk            = 3;
                return isOk;
            }

            // 수료 정보 삭제
             deleteStoldTable2( connMgr, v_subj, v_year, v_subjseq,vecusers );
            
            // 수료 정보 입력 psmt
            sbSQL.append(" insert into tz_stold                                                                     \n")
                 .append(" (                                                                                        \n")
                 .append("         subj                                , year          , subjseq   , userid         \n")
                 .append("     ,   name                                , comp          , score     , tstep          \n")
                 .append("     ,   mtest                               , ftest         , report    , act            \n")
                 .append("     ,   etc1                                , etc2          , avtstep   , avmtest        \n")
                 .append("     ,   avftest                             , avreport      , avact     , avetc1         \n")
                 .append("     ,   avetc2                              , isgraduated   , isb2c     , edustart       \n")
                 .append("     ,   eduend                              , serno         , isrestudy , luserid        \n")
                 .append("     ,   ldate                               , htest         , avhtest   , notgraducd     \n")
                 .append("     ,   study_count                         , study_time								    \n")
                 .append("     ,   post                                , dept_cd       , job_cd    , examnum					    \n")
                 .append(" ) values (                                                                               \n")
                 .append("         ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   to_char(sysdate, 'yyyymmddhh24miss'), ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             					            \n")
                 .append("     ,   ?                                   , ?             , ?			,?	            \n")
                 .append(" )                                                                                        \n");
            
            //System.out.println(this.getClass().getName() + "." + "SubjectComplete() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());

            // 학습자 커서
            list = getFinishTargetStudent2(connMgr, v_subj, v_year, v_subjseq, "ALL",vecusers);
            
            for ( int i = 0; i<list.size(); i++ ) { 
                data        = (StoldData)list.get(i);

                data.setWstep       ( subjseqdata.getWstep      () );
                data.setWmtest      ( subjseqdata.getWmtest     () );
                data.setWftest      ( subjseqdata.getWftest     () );
                data.setWhtest      ( subjseqdata.getWhtest     () );
                data.setWreport     ( subjseqdata.getWreport    () );
                data.setWact        ( subjseqdata.getWact       () );
                data.setWetc1       ( subjseqdata.getWetc1      () );
                data.setWetc2       ( subjseqdata.getWetc2      () );
                data.setGradscore   ( subjseqdata.getGradscore  () );
                data.setGradstep    ( subjseqdata.getGradstep   () );
                data.setGradexam    ( subjseqdata.getGradexam   () );
                data.setGradreport  ( subjseqdata.getGradreport () );
                data.setExamnum  ( subjseqdata.getExamnum () );
                
                v_userid    = data.getUserid    ();
                v_samtotal  = data.getSamtotal  ();  // 삼진아웃
                
              //  System.out.println("v_userid==========>\n"+v_userid);

              
             /*   
                // 미채점 리포트 갯수 확인 -->온라인 과제로 대체 함 2010.05.20
                int v_remainReportcnt   = chkRemainReport(connMgr, v_subj, v_year, v_subjseq, v_userid);
                
             
                if ( v_remainReportcnt > 0 ) { 
                    v_isexception   = true;
                    v_return_msg    = v_userid + "학습자의 리포트 중 " + String.valueOf(v_remainReportcnt) + " 개가 미채점되었습니다.";
                    isOk            = 5;
                    return isOk;
                }*/
                
                
                // 점수 재계산
                try { 
                    calc_score(connMgr, "ALL", v_subj, v_year, v_subjseq, v_userid, v_luserid, data);
                } catch  ( Exception ex ) { 
                    v_isexception   = true;
                    v_return_msg    = "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]";
                    v_return_msg   += ex.getMessage();

                    isOk            = 4;
                    return  isOk;
                }
                
                // 수료정보 INSERT
                if ( data.getIsgraduated().equals("Y") ) { 
                    v_serno_cnt++;
                    
                    // 수료증번호 발급            
                    v_serno         = getCompleteSerno(connMgr, v_subj, v_year, v_subjseq, v_userid);
                    
                    // 학습자 테이블 수료='Y' update
                    updateStudentIsgraduated(connMgr, v_subj, v_year, v_subjseq, v_userid, "Y");         
                } else { 
                    v_serno = "";
                }
                
                pstmt.setString( 1, v_subj                    );
                pstmt.setString( 2, v_year                    );
                pstmt.setString( 3, v_subjseq                 );
                pstmt.setString( 4, v_userid                  );
                                                            
                pstmt.setString( 5, data.getName            ());
                pstmt.setString( 6, data.getComp            ());
                pstmt.setDouble( 7, data.getScore           ());
                pstmt.setDouble( 8, data.getTstep           ());
                pstmt.setDouble( 9, data.getMtest           ());
                                                            
                pstmt.setDouble(10, data.getFtest           ());
                pstmt.setDouble(11, data.getReport          ());
                pstmt.setDouble(12, data.getAct             ());
                pstmt.setDouble(13, data.getEtc1            ());
                                                            
                pstmt.setDouble(14, data.getEtc2            ());
                pstmt.setDouble(15, data.getAvtstep         ());
                pstmt.setDouble(16, data.getAvmtest         ());
                pstmt.setDouble(17, data.getAvftest         ());
                                                            
                pstmt.setDouble(18, data.getAvreport        ());
                pstmt.setDouble(19, data.getAvact           ());
                pstmt.setDouble(20, data.getAvetc1          ());
                pstmt.setDouble(21, data.getAvetc2          ());
                                                            
                pstmt.setString(22, data.getIsgraduated     ());
                pstmt.setString(23, data.getIsb2c           ());
                pstmt.setString(24, subjseqdata.getEdustart ());
                pstmt.setString(25, subjseqdata.getEduend   ());

                pstmt.setString(26, v_serno                   );    // 수료증번호
                pstmt.setString(27, data.getIsrestudy       ());  
                pstmt.setString(28, v_luserid                 );
                pstmt.setDouble(29, data.getHtest           ());
                pstmt.setDouble(30, data.getAvhtest         ());
                pstmt.setString(31, data.getNotgraducd      ());

                pstmt.setInt   (32, data.getStudy_count     ());
                pstmt.setInt   (33, data.getStudy_time      ());

                pstmt.setString(34, data.getPost            ());
                pstmt.setString(35, data.getDept_cd         ());
                pstmt.setString(36, data.getJob_cd          ());
                pstmt.setString(37, data.getExamnum          ());
                
                isOk    = pstmt.executeUpdate();
                //if ( pstmt != null ) { pstmt.close(); }
                
                // 수료증번호 발급
                isOk    = updateStudentSerno(connMgr, v_serno, v_subj, v_year, v_subjseq, v_userid);
                
                // 수료/미수료 통보 메일 보낼 자리
                // sendFinishMail(connMgr, box, v_userid, subjseqdata.getIsonoff(), subjseqdata.getSubjnm(), data.getIsgraduated(), subjseqdata.getBiyong() );
                
                if ( data.getIsgraduated().equals("N") ) {
                        afbean.insertSubjseqStrOut(connMgr, box, v_userid, data.getName(), subjseqdata.getEduend().substring(0, 8), v_year, v_subj, v_subjseq );
                   
                }
                
            }
            
           // 수료 필드 수정 - isclosed = 'Y'
           //전체 수료자 카운트와 수료처리되는 수료자의 수가 갔다면 Y 그렇치 않으면 N으로 남겨 주어야 함.
            
          //  System.out.println("vecusers.size()========>"+vecusers.size());
         //   System.out.println("vcnt2========>"+vcnt2);
            if(vlistcnt ==( vecusers.size()+vcnt2+vcnt3)){
            	 isOk        = setCloseColumn(connMgr, v_subj, v_year, v_subjseq, "Y");
            }else{
            	 isOk        = setCloseColumn(connMgr, v_subj, v_year, v_subjseq, "N");
            }
            
          
           
        } catch ( SQLException e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( !v_return_msg.equals("") )
                box.put("p_return_msg", v_return_msg);
                
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { }
            }
                
            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception ) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }
        
        return isOk;
    }


    /**
    사이버 과목 점수재계산
    @param  box      receive from the form object
    @return int
    */
    public int SubjectCompleteRerating(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager connMgr         = null;
        ArrayList           list            = null;   // 수료대상학생 정보 커서
        StoldData           data            = null;
        SubjseqData         subjseqdata     = null;
        int                 isOk            = 1;
        
        // 수료처리시 필요한 기본 변수들
        String              v_subj          = box.getString ("p_subj"   );
        String              v_year          = box.getString ("p_year"   );
        String              v_subjseq       = box.getString ("p_subjseq");
        String              v_userid        = "";
        String              v_luserid       = box.getSession("userid"   );
                                            
        String              v_currdate      = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년 +월 +일 +시

        boolean             v_isexception   = false;
        String              v_return_msg    = "";

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료처리 완료여부, 학습중 검토
            subjseqdata = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            
            if ( subjseqdata.getIsclosed().equals("Y") ) { 
                v_return_msg    = "이미 수료처리 되었습니다.";
                isOk            = 2;
                
                return isOk;
            } else if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEdustart())) ) { 
                v_return_msg  = "학습시작후 가능합니다.";
                
                isOk            = 3;
                return isOk;
            }
            
            // 학습자 커서
            list        = getFinishTargetStudent(connMgr, v_subj, v_year, v_subjseq, "ALL");

            for ( int i = 0; i<list.size(); i++ ) { 
                data        = (StoldData)list.get(i);

                data.setWstep    (subjseqdata.getWstep()        );
                data.setWmtest   (subjseqdata.getWmtest()       );
                data.setWftest   (subjseqdata.getWftest()       );
                data.setWhtest   (subjseqdata.getWhtest()       );
                data.setWreport  (subjseqdata.getWreport()      );
                data.setWact     (subjseqdata.getWact()         );
                data.setWetc1    (subjseqdata.getWetc1()        );
                data.setWetc2    (subjseqdata.getWetc2()        );
                data.setGradscore(subjseqdata.getGradscore()    );
                data.setGradstep (subjseqdata.getGradstep()     );
                data.setGradexam (subjseqdata.getGradexam()     );
                data.setGradreport(subjseqdata.getGradreport()  );

                v_userid    = data.getUserid();

                // 점수 재계산
                try { 
                    isOk    = calc_score(connMgr, "ALL", v_subj, v_year, v_subjseq, v_userid, v_luserid, data);
                } catch  ( Exception ex ) { 
                    v_isexception   = true;
                    v_return_msg    = "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]";
                    v_return_msg   += ex.getMessage();

                    isOk            = 4;
                    return isOk;
                }
           }  // for end
           
           isOk = UpdateRecalcudate(connMgr, v_subj, v_year, v_subjseq);
        } catch ( Exception e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( !v_return_msg.equals(""))
                box.put("p_return_msg", v_return_msg);
                
            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }

        return isOk;        
    }
    
    
    public boolean  sendFinishMail(DBConnectionManager connMgr, RequestBox box, String p_userid, String p_isonoff, String p_subjnm, String  p_isgraduated, int p_biyong) throws Exception { 
        boolean isMailed = false;
        String v_mailContent = "";

//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
        String  v_toCono   = "";
        String  v_toEmail  = "";
        String  v_ismailing= "";
        String  v_toname   = ""; // 님이 tz_member
        String  v_thismon = StringManager.substring(FormatDate.getDate("yyyyMMddhh"),4,6);	// 현재월

        ListSet ls  = null;
        String  sql = "";

        sql = "select cono, email, name, ismailing ";
        sql += "  from tz_member ";
        sql += " where userid = " + SQLString.Format(p_userid);

        try { 
//// //// //// //// ////  폼메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
            String v_sendhtml = "";
            FormMail fmail = null;
            MailSet mset = null;
            String v_mailTitle = "";

            if ( p_isgraduated.equals("Y") ) { 
                v_sendhtml = "mail4.html";
                fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우
                mset = new MailSet(box);        //      메일 세팅 및 발송
                v_mailTitle = "안녕하세요? 현대/기아자동차 사이버 연수원 운영자입니다.(수료안내)";
            } else { 
                v_sendhtml = "mail5.html";
                fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우
                mset = new MailSet(box);        //      메일 세팅 및 발송
                v_mailTitle = "안녕하세요? 현대/기아자동차 사이버 연수원 운영자입니다.(미수료안내)";
            }
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /
            mset.setSender(fmail);     //  메일보내는 사람 세팅

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_toCono  = ls.getString("cono");
                v_toEmail = ls.getString("email");
                v_toname  = ls.getString("name");
                v_ismailing = ls.getString("ismailing");
            }
            if ( p_isonoff.equals("ON") ) { p_isonoff="사이버과목" ; }
            else if ( p_isonoff.equals("OFF") ) { p_isonoff="집합과목" ; }

            fmail.setVariable("toname",   v_toname);
            fmail.setVariable("isonoff",  p_isonoff);
            fmail.setVariable("subjnm",   p_subjnm);
            fmail.setVariable("biyong",   String.valueOf(p_biyong));// 수강료
            fmail.setVariable("month",	  v_thismon);// 현재월

            v_mailContent = fmail.getNewMailContent();

            // isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return isMailed;
    }

    
    // 사이버 수료취소 
    public int SubjectCompleteCancel(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	        = null;
        int                 isOk            = 0;
        String              v_subj          = box.getString("p_subj"    );
        String              v_year          = box.getString("p_year"    );
        String              v_subjseq       = box.getString("p_subjseq" );
        SubjseqData         subjseqdata     = null;
        boolean             v_isexception   = false;
        AcceptFileToDBBean  afbean          = new AcceptFileToDBBean();

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료처리 완료여부, 학습중 검토
            subjseqdata = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);

            // if ( subjseqdata.getIsclosed().equals("Y") ) { 
            // 수료 정보 삭제
            isOk        = deleteStoldTable(connMgr, v_subj, v_year, v_subjseq);

            // 수료 필드 수정 - isclosed = 'N'
            isOk        = setCloseColumn(connMgr, v_subj, v_year, v_subjseq, "N");
                
            // 외주과목 최종확인 N
            isOk        = this.updateIsCpflag(connMgr, v_subj, v_year, v_subjseq, "N");
            // }
           
            // 수강 제약 조건 (해당기수) 등록자 삭제  
            afbean.deleteSubjseqStrOut(connMgr, v_subj, v_year, v_subjseq);
            
            /*
            // 학습자 커서
            ArrayList list = getFinishTargetStudent(connMgr, v_subj, v_year, v_subjseq, "ALL");
            StoldData data = null;
            for ( int i = 0; i<list.size(); i++ ) { 
                data        = (StoldData)list.get(i);

	            // 학습자 테이블 수료='N' update
	            updateStudentIsgraduated(connMgr, v_subj, v_year, v_subjseq, data.getUserid(), "N");        
            }
            */
        } catch ( Exception e ) {
            v_isexception   = true;
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }
        
        return isOk;
    }

    
    // isOk = 1 : "에러가 없는 경우"
    // isOk = 2 : "이미 수료처리 되었습니다."
    // isOk = 3 : 기타 에러
    // isOk = 4 : calc_score() 계산에서 에러
    public int ScoreCompute(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	        = null;
        int                 isOk            = 1;
        String              v_subj          = box.getString("p_subj");
        String              v_year          = box.getString("p_year");
        String              v_subjseq       = box.getString("p_subjseq");
        String              v_luserid       = box.getSession("userid");

        StoldData           data            = null;
        SubjseqData         subjseqdata     = null;
        ArrayList           list            = null;   // 수료대상학생 정보 커서

        boolean             v_isexception   = false;
        String              v_return_msg    = "";

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료처리 완료여부, 학습중 검토
            subjseqdata = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            
            if ( subjseqdata.getIsclosed().equals("Y") ) { 
                v_return_msg    = "이미 수료처리 되었습니다.";
                isOk            = 2;
                return isOk;
            }

            // 학습자 커서
            list    = getFinishTargetStudent(connMgr, v_subj, v_year, v_subjseq, "ALL");
            
            for ( int i = 0; i<list.size(); i++ ) { 
                data    = (StoldData)list.get(i);

                data.setWstep    ( subjseqdata.getWstep      ());
                data.setWmtest   ( subjseqdata.getWmtest     ());
                data.setWftest   ( subjseqdata.getWftest     ());
                data.setWreport  ( subjseqdata.getWreport    ());
                data.setWact     ( subjseqdata.getWact       ());
                data.setWetc1    ( subjseqdata.getWetc1      ());
                data.setWetc2    ( subjseqdata.getWetc2      ());
                data.setGradscore( subjseqdata.getGradscore  ());
                data.setGradstep ( subjseqdata.getGradstep   ());

                // 점수 재계산
                try { 
                    v_return_msg    = "";
                    
                    calc_score(connMgr, "ALL", v_subj, v_year, v_subjseq, data.getUserid(), v_luserid, data);
                } catch  ( Exception ex ) { 
                    v_isexception   = true;
                    
                    v_return_msg    = "수료처리 == > 점수 재산정 중 문제발생함[" + data.getUserid() + "]";
                    v_return_msg   += ex.getMessage();
                    isOk            = 4;
                    
                    return isOk;
                }
            }
        } catch ( Exception ex ) { 
            v_isexception   = true;
            isOk            = 3;
            v_return_msg    = ex.getMessage();
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( !v_return_msg.equals(""))
                box.put("p_return_msg", v_return_msg);

            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }
        
        return isOk;
    }


    /**
    사이버 점수 계산 
    @param box          receive from the form object and session 8
    @return int
    */
    public int calc_score(DBConnectionManager connMgr, String p_gubun, String p_subj, String p_year, String p_subjseq, String p_userid, String p_luserid, StoldData data) throws Exception { 
        PreparedStatement   pstmt           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        ListSet             ls              = null;
        int                 isOk            = 0;
        
        int                 iSysAdd         = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_contenttype   = "";
        String              v_subjgu   = "";

        try { 
            sbSQL.append(" select  nvl(contenttype,'N') contenttype, subj_gu    \n")
                 .append(" from    tz_subj                                      \n")
                 .append(" where   subj    = " + SQLString.Format(p_subj) + "   \n")
                 .append(" and     rownum  = 1                                  \n");
            
            //System.out.println(this.getClass().getName() + "." + "calc_score() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
                v_contenttype   = ls.getString("contenttype");
                v_subjgu   		= ls.getString("subj_gu");
            }
            
            ls.close();
            
            sbSQL.setLength(0);
            
           // System.out.println("v_contenttype====>"+v_contenttype);
            
            // 컨텐츠타입이 'L'인것은 위탁과정이므로 재산정 필요없음
            if ( !v_contenttype.equals("L") ) { 
              //  isOk = calc_step        (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
                isOk = calc_exam        (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
                isOk = calc_exam2        (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
                isOk = calc_report      (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
                isOk = calc_activity    (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
                isOk = calc_etc         (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
                isOk = calc_isgraduated (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
                
                // 점수 수정
                sbSQL.append(" update tz_student set        \n")
                     .append("         score       = ?      \n")
                     .append("     ,   tstep       = ?      \n")
                    // .append("     ,   mtest       = ?      \n")
                     .append("     ,   ftest       = ?      \n")
                     .append("     ,   htest       = ?      \n")
                     .append("     ,   report      = ?      \n")
                     .append("     ,   act         = ?      \n")
                     .append("     ,   etc1        = ?      \n")
                     .append("     ,   etc2        = ?      \n")
                     .append("     ,   avtstep     = ?      \n")
                     .append("     ,   avmtest     = ?      \n")
                     .append("     ,   avftest     = ?      \n")
                     .append("     ,   avhtest     = ?      \n")
                     .append("     ,   avreport    = ?      \n")
                     .append("     ,   avact       = ?      \n")
                     .append("     ,   avetc1      = ?      \n")
                     .append("     ,   avetc2      = ?      \n")
                     .append("     ,   luserid     = ?      \n")
                     .append("     ,   ldate       = ?      \n")
                     .append("     ,   isgraduated = ?      \n")
                     .append("     ,   mreport     = ?      \n")
                     .append("     ,   freport     = ?      \n")
                     .append(" where   subj        = ?      \n")
                     .append(" and     year        = ?      \n")
                     .append(" and     subjseq     = ?      \n")
                     .append(" and     userid      = ?      \n");
                
                System.out.println(this.getClass().getName() + "." + "calc_score() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                     
                pstmt   = connMgr.prepareStatement(sbSQL.toString());
                
                pstmt.setDouble( 1, data.getScore       ()  );
                pstmt.setDouble( 2, data.getTstep       ()  );
            //    pstmt.setDouble( 3, data.getMtest       ()  );
                pstmt.setDouble( 3, data.getFtest       ()  );
                pstmt.setDouble( 4, data.getHtest       ()  );
                pstmt.setDouble( 5, data.getReport      ()  );
                pstmt.setDouble( 6, data.getAct         ()  );
                pstmt.setDouble( 7, data.getEtc1        ()  );
                pstmt.setDouble( 8, data.getEtc2        ()  );
                pstmt.setDouble(9, data.getAvtstep     ()  );
                pstmt.setDouble(10, data.getAvmtest     ()  );
                pstmt.setDouble(11, data.getAvftest     ()  );
                pstmt.setDouble(12, data.getAvhtest     ()  );
                pstmt.setDouble(13, data.getAvreport    ()  );
                pstmt.setDouble(14, data.getAvact       ()  );
                pstmt.setDouble(15, data.getAvetc1      ()  );
                pstmt.setDouble(16, data.getAvetc2      ()  );
                pstmt.setString(17, p_luserid)          ;
                pstmt.setString(18, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt.setString(19, data.getIsgraduated ()  );
                pstmt.setDouble(20, data.getMreport     ()  );
                pstmt.setDouble(21, data.getFreport     ()  );
                pstmt.setString(22, p_subj                  );
                pstmt.setString(23, p_year                  );
                pstmt.setString(24, p_subjseq               );
                pstmt.setString(25, p_userid                );
                
                isOk = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
            } else {  // LINK 외주타입
                // tz_student 
                sbSQL.append(" select  score       , mtest     , ftest         , htest      \n")
                     .append("     ,   report      , act       , etc1          , etc2       \n")
                     .append("     ,   avtstep     , avmtest   , avftest       , avhtest    \n")
                     .append("     ,   avreport    , avact     , avetc1        , avetc2     \n")
                     .append("     ,   luserid     , ldate     , isgraduated                \n")   
                     .append(" from    tz_student                                           \n")
                     .append(" where   subj    = " + SQLString.Format(p_subj       ) + "    \n")
                     .append(" and     year    = " + SQLString.Format(p_year       ) + "    \n")
                     .append(" and     subjseq = " + SQLString.Format(p_subjseq    ) + "    \n")
                     .append(" and     userid  = " + SQLString.Format(p_userid     ) + "    \n");
                
                //System.out.println(this.getClass().getName() + "." + "calc_score() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
              
                ls  = connMgr.executeQuery(sbSQL.toString());
                
                if ( ls.next() ) { 
                    // ls.getDouble("score");
                    // ls.getDouble("mtest");
                    // ls.getDouble("ftest");
                    // ls.getDouble("htest");                   
                    // ls.getDouble("report");                  
                    // ls.getDouble("act");                 
                    // ls.getDouble("etc1");                    
                    // ls.getDouble("etc2");                    
                    // ls.getDouble("avtstep");                 
                    // ls.getDouble("avmtest");                 
                    // ls.getDouble("avftest");                 
                    // ls.getDouble("avhtest");                 
                    // ls.getDouble("avreport");                    
                    // ls.getDouble("avact");                   
                    // ls.getDouble("avetc1");                  
                    // ls.getDouble("avetc2");                  
                    // ls.getString("luserid");                 
                    // ls.getString("ldate");                   
                    // ls.getString("isgraduated");
                    
                    data.setIsgraduated( ls.getString("isgraduated") );
                }
                
                ls.close();
                
                sbSQL.setLength(0);
                
                sbSQL.append(" update tz_student set        \n")
                     .append("         luserid     = ?      \n")
                     .append("     ,   ldate       = ?      \n")
                     .append("     ,   isgraduated = ?      \n")
                     .append(" where   subj        = ?      \n")
                     .append(" and     year        = ?      \n")
                     .append(" and     subjseq     = ?      \n")
                     .append(" and     userid      = ?      \n");
                
                //System.out.println(this.getClass().getName() + "." + "calc_score() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
                pstmt = connMgr.prepareStatement(sbSQL.toString());
                
                pstmt.setString(1, p_luserid    );
                pstmt.setString(2, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt.setString(3, data.getIsgraduated() );
                pstmt.setString(4, p_subj       );
                pstmt.setString(5, p_year       );
                pstmt.setString(6, p_subjseq    );
                pstmt.setString(7, p_userid     );
                
                isOk = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
            }        
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
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }

    
    
    /** Added by LeeSuMin 2004.02.23 */
    public boolean islink_subj(DBConnectionManager connMgr, String p_subj) throws Exception { 
        ListSet ls  = null;
            String sql  = "";
            String v_contenttype = "";
            boolean blchk   = false;

        try { 
            sql = "select contenttype from tz_subj where subj= " +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);
            ls.next();
            v_contenttype = ls.getString("contenttype");
            if ( v_contenttype.trim().equals("L")) blchk = true;

        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return blchk;
    }


	/**
    점수 계산 (세션변수에서 사용자 id를 가져온다.)
    @param box          receive from the form object and session 6
    @return int
    */                                      
    public int calc_score(DBConnectionManager connMgr, int p_gubun, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        ListSet             ls      = null;
        int isOk = 0;

        StoldData data = new StoldData();
        SubjseqData subjseqdata = null;
        String v_luserid  = p_userid; // 세션변수에서 사용자 id를 가져온다.
        String v_return_msg = "";
        String v_contenttype = "";
        String v_subjgu = "";
        ArrayList           list    = null;   // 수료대상학생 정보 커서
        int cnt = 0;

        try { 
            
            sql  = " select count(*) cnt from tz_student ";
			sql += " where  subj = '" +p_subj + "' and year='" +p_year + "' and subjseq = '" +p_subjseq + "' and userid = '" +p_userid + "'";
			ls = connMgr.executeQuery(sql);
			
			if ( ls.next() ) { 
				cnt = ls.getInt("cnt");
		    }
		    ls.close();
		    
			if ( cnt > 0) { 

				// 수료처리 완료여부, 학습중 검토
				subjseqdata = getSubjseqInfo(connMgr, p_subj, p_year, p_subjseq);
              
				if ( subjseqdata.getIsclosed().equals("Y") ) { 
					v_return_msg  = "이미 수료처리 되었습니다.";
					return 2;
				}
			  
				// 컨텐츠타입이 'L'인것은 위탁과정이므로 재산정 필요없음
				sql = "\n select contenttype, subj_gu "
					+ "\n from   tz_subj "
					+ "\n where  subj=" +SQLString.Format(p_subj);
				ls = connMgr.executeQuery(sql);
			  
				if ( ls.next() ) { 
					v_contenttype = ls.getString("contenttype");
					v_subjgu = ls.getString("subj_gu");
				}
				ls.close();
			  
				// 학습자 커서
				list = getFinishTargetStudent(connMgr, p_subj, p_year, p_subjseq, p_userid);
              
				for ( int i = 0; i<list.size(); i++ ) { 
					data  = (StoldData)list.get(i);
              
					data.setWstep    (subjseqdata.getWstep() );
					data.setWmtest   (subjseqdata.getWmtest() );
					data.setWhtest   (subjseqdata.getWhtest() );
					data.setWftest   (subjseqdata.getWftest() );
					data.setWreport  (subjseqdata.getWreport() );
					data.setWact     (subjseqdata.getWact() );
					data.setWetc1    (subjseqdata.getWetc1() );
					data.setWetc2    (subjseqdata.getWetc2() );
					data.setGradscore(subjseqdata.getGradscore() );
					data.setGradstep (subjseqdata.getGradstep() );
					data.setGradexam (subjseqdata.getGradexam() );
					data.setGradreport (subjseqdata.getGradreport() );
					data.setGradftest (subjseqdata.getGradftest() );
					data.setGradhtest (subjseqdata.getGradhtest() );
              
					if ( p_gubun == CalcUtil.STEP) { 
						if ( !v_contenttype.equals("L") ) { 
							isOk = calc_step        (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
						}
					} else if ( p_gubun == CalcUtil.EXAM) { 
						isOk = calc_exam        (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
					} else if ( p_gubun == CalcUtil.REPORT) { 
						isOk = calc_report      (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
					} else if ( p_gubun == CalcUtil.ETC) { 
						isOk = calc_etc         (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
					} else if ( p_gubun == CalcUtil.ACTIVITY) { 
						isOk = calc_activity    (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
					} else if ( p_gubun == CalcUtil.ALL) { 

						if ( !v_contenttype.equals("L") ) { 
							isOk = calc_step        (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
						}
                      
						if ( !v_contenttype.equals("L") ) { 
							isOk = calc_exam        (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
							isOk = calc_report      (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
							isOk = calc_etc         (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
							isOk = calc_activity    (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
						}
					}
              
					isOk = calc_isgraduated (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
              
					if ( p_gubun == CalcUtil.STEP) { 
                      	sql = " update tz_student ";
                      	sql += "    set score   = ?, ";
                      	if ( !v_contenttype.equals("L") ) { 
                      	sql += "        tstep   = ?, ";
                      	}
                      	sql += "        avtstep = ?, ";
                      	sql += "        isgraduated= ?, ";
                      	sql += "        luserid = ?, ";
                      	sql += "        ldate   = ? ";
                      	sql += "  where subj    = ? ";
                      	sql += "    and year    = ? ";
                      	sql += "    and subjseq = ? ";
                      	sql += "    and userid  = ? ";
                      	
                      	pstmt = connMgr.prepareStatement(sql);
                      	
                      	pstmt.setDouble( 1, data.getScore() );
                      	if ( !v_contenttype.equals("L") ) { 
                      		pstmt.setDouble( 2, data.getTstep() );
                      		pstmt.setDouble( 3, data.getAvtstep() );
                      		pstmt.setString( 4, data.getIsgraduated() );
                      		pstmt.setString( 5, v_luserid);
                      		pstmt.setString( 6, FormatDate.getDate("yyyyMMddHHmmss") );
                      		pstmt.setString( 7, p_subj);
                      		pstmt.setString( 8, p_year);
                      		pstmt.setString( 9 , p_subjseq);
                      		pstmt.setString(10, p_userid);
                      	} else { 
                      		pstmt.setDouble( 2, data.getAvtstep() );
                      		pstmt.setString( 3, data.getIsgraduated() );
                      		pstmt.setString( 4, v_luserid);
                      		pstmt.setString( 5, FormatDate.getDate("yyyyMMddHHmmss") );
                      		pstmt.setString( 6, p_subj);
                      		pstmt.setString( 7, p_year);
                      		pstmt.setString( 8 , p_subjseq);
                      		pstmt.setString( 9, p_userid);
                      	}
					} else if ( p_gubun == CalcUtil.EXAM) { 
						sql = " update tz_student ";
						sql += "    set score   = ?, ";
					//	sql += "        mtest   = ?, ";
						sql += "        htest   = ?, ";
						sql += "        ftest   = ?, ";
						sql += "        avmtest = ?, ";
						sql += "        avhtest = ?, ";
						sql += "        avftest = ?, ";
						sql += "        isgraduated= ?, ";
						sql += "        luserid = ?, ";
						sql += "        ldate   = ? ";
						sql += "  where subj    = ? ";
						sql += "    and year    = ? ";
						sql += "    and subjseq = ? ";
						sql += "    and userid  = ? ";
						  
						pstmt = connMgr.prepareStatement(sql);
						 
//						pstmt.setDouble( 1, data.getScore() );
//						pstmt.setDouble( 2, data.getMtest() );
//						pstmt.setDouble( 3, data.getHtest() );
//						pstmt.setDouble( 4, data.getFtest() );
//						pstmt.setDouble( 5, data.getAvmtest() );
//						pstmt.setDouble( 6, data.getAvhtest() );
//						pstmt.setDouble( 7, data.getAvftest() );
//						pstmt.setString( 8, data.getIsgraduated() );
//						pstmt.setString( 9, v_luserid);
//						pstmt.setString(10, FormatDate.getDate("yyyyMMddHHmmss") );
//						pstmt.setString(11, p_subj);
//						pstmt.setString(12, p_year);
//						pstmt.setString(13, p_subjseq);
//						pstmt.setString(14, p_userid);
						
						pstmt.setDouble( 1, data.getScore() );
						pstmt.setDouble( 2, data.getHtest() );
						pstmt.setDouble( 3, data.getFtest() );
						pstmt.setDouble( 4, data.getAvmtest() );
						pstmt.setDouble( 5, data.getAvhtest() );
						pstmt.setDouble( 6, data.getAvftest() );
						pstmt.setString( 7, data.getIsgraduated() );
						pstmt.setString( 8, v_luserid);
						pstmt.setString(9, FormatDate.getDate("yyyyMMddHHmmss") );
						pstmt.setString(10, p_subj);
						pstmt.setString(11, p_year);
						pstmt.setString(12, p_subjseq);
						pstmt.setString(13, p_userid);
						
                  } else if ( p_gubun == CalcUtil.REPORT) {
                	  sql = " update tz_student ";
                	  sql += "    set score   = ? ";
                	  sql += "      , report  = ? ";
                	  sql += "      , avreport= ? ";
                	  sql += "      , isgraduated= ? ";
                	  sql += "      , luserid = ? ";
                	  sql += "      , ldate   = ? ";
                	  sql += "      , mreport = ? ";
                	  sql += "      , freport = ? ";
                	  sql += "  where subj    = ? ";
                	  sql += "    and year    = ? ";
                	  sql += "    and subjseq = ? ";
                	  sql += "    and userid  = ? ";

                	  pstmt = connMgr.prepareStatement(sql);

                	  pstmt.setDouble( 1, data.getScore() );
                	  pstmt.setDouble( 2, data.getReport() );
                	  pstmt.setDouble( 3, data.getAvreport() );
                	  pstmt.setString( 4, data.getIsgraduated() );
                	  pstmt.setString( 5, v_luserid);
                	  pstmt.setString( 6, FormatDate.getDate("yyyyMMddHHmmss") );
                	  pstmt.setDouble( 7, data.getMreport());
                	  pstmt.setDouble( 8, data.getFreport());
                	  pstmt.setString( 9, p_subj);
                	  pstmt.setString(10, p_year);
                	  pstmt.setString(11, p_subjseq);
                	  pstmt.setString(12, p_userid);

                  } else if ( p_gubun == CalcUtil.ACTIVITY) { 
                      // 점수 수정
                      sql = " update tz_student ";
                      sql += "    set score   = ?, ";
                      sql += "        act     = ?, ";
                      sql += "        avact   = ?, ";
                      sql += "        isgraduated= ?, ";
                      sql += "        luserid = ?, ";
                      sql += "        ldate   = ? ";
                      sql += "  where subj    = ? ";
                      sql += "    and year    = ? ";
                      sql += "    and subjseq = ? ";
                      sql += "    and userid  = ? ";
              
                      pstmt = connMgr.prepareStatement(sql);
              
                      pstmt.setDouble( 1, data.getScore() );
                      pstmt.setDouble( 2, data.getAct() );
                      pstmt.setDouble( 3, data.getAvact() );
                      pstmt.setString( 4, data.getIsgraduated() );
                      pstmt.setString( 5, v_luserid);
                      pstmt.setString( 6, FormatDate.getDate("yyyyMMddHHmmss") );
                      pstmt.setString( 7, p_subj);
                      pstmt.setString( 8, p_year);
                      pstmt.setString( 9, p_subjseq);
                      pstmt.setString(10, p_userid);
                  } else if ( p_gubun == CalcUtil.ETC) { 
                      // 점수 수정
                      sql = " update tz_student ";
                      sql += "    set score   = ?, ";
                      sql += "        etc1    = ?, ";
                      sql += "        etc2    = ?, ";
                      sql += "        avetc1  = ?, ";
                      sql += "        avetc2  = ?, ";
                      sql += "        isgraduated= ?, ";
                      sql += "        luserid = ?, ";
                      sql += "        ldate   = ? ";
                      sql += "  where subj    = ? ";
                      sql += "    and year    = ? ";
                      sql += "    and subjseq = ? ";
                      sql += "    and userid  = ? ";
              
                      pstmt = connMgr.prepareStatement(sql);
              
                      pstmt.setDouble( 1, data.getScore() );
                      pstmt.setDouble( 2, data.getEtc1() );
                      pstmt.setDouble( 3, data.getEtc2() );
                      pstmt.setDouble( 4, data.getAvetc1() );
                      pstmt.setDouble( 5, data.getAvetc2() );
                      pstmt.setString( 6, data.getIsgraduated() );
                      pstmt.setString( 7, v_luserid);
                      pstmt.setString( 8, FormatDate.getDate("yyyyMMddHHmmss") );
                      pstmt.setString( 9, p_subj);
                      pstmt.setString(10, p_year);
                      pstmt.setString(11, p_subjseq);
                      pstmt.setString(12, p_userid);
                  } else if ( p_gubun == CalcUtil.ALL) { 
                	  if ( !v_contenttype.equals("L") ) { 
	                      // 점수 수정
	                      sql = " update tz_student ";
	                      sql += "    set score   = ?, ";
	                      sql += "        tstep   = ?, ";
	                  //    sql += "        mtest   = ?, ";
	                      sql += "        htest   = ?, ";
	                      sql += "        ftest   = ?, ";
	                      sql += "        report  = ?, ";
	                      sql += "        act     = ?, ";
	                      sql += "        etc1    = ?, ";
	                      sql += "        etc2    = ?, ";
	                      sql += "        avtstep = ?, ";
	                      sql += "        avmtest = ?, ";
	                      sql += "        avhtest = ?, ";
	                      sql += "        avftest = ?, ";
	                      sql += "        avreport= ?, ";
	                      sql += "        avact   = ?, ";
	                      sql += "        avetc1  = ?, ";
	                      sql += "        avetc2  = ?, ";
	                      sql += "        isgraduated= ?, ";
	                      sql += "        luserid = ?, ";
	                      sql += "        ldate   = ?, ";
	                      sql += "        mreport = ?, ";
	                      sql += "        freport = ? ";
	                      sql += "  where subj    = ? ";
	                      sql += "    and year    = ? ";
	                      sql += "    and subjseq = ? ";
	                      sql += "    and userid  = ? ";
	              
	                      pstmt = connMgr.prepareStatement(sql);
	              
	                      pstmt.setDouble( 1, data.getScore() );
	                      pstmt.setDouble( 2, data.getTstep() );
	                  //    pstmt.setDouble( 3, data.getMtest() );
	                      pstmt.setDouble( 3, data.getHtest() );
	                      pstmt.setDouble( 4, data.getFtest() );
	                      pstmt.setDouble( 5, data.getReport() );
	                      pstmt.setDouble( 6, data.getAct() );
	                      pstmt.setDouble( 7, data.getEtc1() );
	                      pstmt.setDouble( 8, data.getEtc2() );
	                      pstmt.setDouble(9, data.getAvtstep() );
	                      pstmt.setDouble(10, data.getAvmtest() );
	                      pstmt.setDouble(11, data.getAvhtest() );
	                      pstmt.setDouble(12, data.getAvftest() );
	                      pstmt.setDouble(13, data.getAvreport() );
	                      pstmt.setDouble(14, data.getAvact() );
	                      pstmt.setDouble(15, data.getAvetc1() );
	                      pstmt.setDouble(16, data.getAvetc2() );
	                      pstmt.setString(17, data.getIsgraduated() );
	                      pstmt.setString(18, v_luserid);
	                      pstmt.setString(19, FormatDate.getDate("yyyyMMddHHmmss") );
	                      pstmt.setDouble(20, data.getMreport() );
	                      pstmt.setDouble(21, data.getFreport() );
	                      pstmt.setString(22, p_subj);
	                      pstmt.setString(23, p_year);
	                      pstmt.setString(24, p_subjseq);
	                      pstmt.setString(25, p_userid);
                     
                	  } else { 
	                      sql = " update tz_student ";
	                      sql += "    set score   = score ";
	                      sql += "  where subj    = ? ";
	                      sql += "    and year    = ? ";
	                      sql += "    and subjseq = ? ";
	                      sql += "    and userid  = ? ";
	              
	                      pstmt = connMgr.prepareStatement(sql);
	                      
	                      pstmt.setString(1, p_subj);
	                      pstmt.setString(2, p_year);
	                      pstmt.setString(3, p_subjseq);
	                      pstmt.setString(4, p_userid);
	                    
                	  }
                  }
                  isOk = pstmt.executeUpdate();
                  if ( pstmt != null ) { pstmt.close(); }
              }
			}
		}
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    
    
    
    
    
    
    /**
    스콤용 점수 계산 (스콤진도나갈때 점수재계산하는 함수)
    @param box          receive from the form object and session 6
    @return int
    */                                      
    public int scorm_calc_score(DBConnectionManager connMgr, int p_gubun, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        ListSet             ls      = null;
        int isOk = 0;

        StoldData data = new StoldData();
        SubjseqData subjseqdata = null;
        String v_luserid  = p_userid; // 세션변수에서 사용자 id를 가져온다.
        String v_return_msg = "";
        String v_contenttype = "";
        ArrayList           list    = null;   // 수료대상학생 정보 커서
        int cnt = 0;

        try { 
            

              // 수료처리 완료여부, 학습중 검토
              subjseqdata = getSubjseqInfo(connMgr, p_subj, p_year, p_subjseq);
              
			  sql = "select contenttype from tz_subj where subj=" +SQLString.Format(p_subj);
			  ls = connMgr.executeQuery(sql);

			  if ( ls.next() ) { 
			  	v_contenttype = ls.getString("contenttype");
			  }
			  ls.close();
			  
              // 학습자 커서
              list = getFinishTargetStudent(connMgr, p_subj, p_year, p_subjseq, p_userid);
              
              for ( int i = 0; i<list.size(); i++ ) { 
                  data  = (StoldData)list.get(i);
                  data.setWstep    (subjseqdata.getWstep() );
                  data.setWmtest   (subjseqdata.getWmtest() );
                  data.setWhtest   (subjseqdata.getWhtest() );
                  data.setWftest   (subjseqdata.getWftest() );
                  data.setWreport  (subjseqdata.getWreport() );
                  data.setWact     (subjseqdata.getWact() );
                  data.setWetc1    (subjseqdata.getWetc1() );
                  data.setWetc2    (subjseqdata.getWetc2() );
                  data.setGradscore(subjseqdata.getGradscore() );
                  data.setGradstep (subjseqdata.getGradstep() );
                  data.setGradexam (subjseqdata.getGradexam() );
                  data.setGradreport (subjseqdata.getGradreport() );
                  data.setGradftest (subjseqdata.getGradftest() );
                  data.setGradhtest (subjseqdata.getGradhtest() );
              
                  isOk = calc_step(connMgr, p_subj, p_year, p_subjseq, p_userid, data);
                  isOk = calc_isgraduated(connMgr, p_subj, p_year, p_subjseq, p_userid, data);
              
                  sql = " update tz_student ";
                  sql += "    set score   = ?, ";
                  sql += "        tstep   = ?, ";
                  sql += "        avtstep = ?, ";
                  sql += "        isgraduated= ?, ";
                  sql += "        luserid = ?, ";
                  sql += "        ldate   = ? ";
                  sql += "  where subj    = ? ";
                  sql += "    and year    = ? ";
                  sql += "    and subjseq = ? ";
                  sql += "    and userid  = ? ";
                  
                  pstmt = connMgr.prepareStatement(sql);
                  
                  pstmt.setDouble( 1, data.getScore() );
                  pstmt.setDouble( 2, data.getTstep() );
                  pstmt.setDouble( 3, data.getAvtstep() );
                  pstmt.setString( 4, data.getIsgraduated() );
                  pstmt.setString( 5, v_luserid);
                  pstmt.setString( 6, FormatDate.getDate("yyyyMMddHHmmss") );
                  pstmt.setString( 7, p_subj);
                  pstmt.setString( 8, p_year);
                  pstmt.setString( 9 , p_subjseq);
                  pstmt.setString(10, p_userid);
                  
                  isOk = pstmt.executeUpdate();
                  
                  if ( pstmt != null ) { pstmt.close(); }
              }
            // }
            
            
			
			// 	//System.out.println("pgubun == > " + p_gubun);

		}
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    
    
    /**
    종합점수계산 
    @param box          receive from the form object and session 8
    @return int
    */
    public int calc_step(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls              = null;
        StringBuffer    sbSQL           = new StringBuffer("");
        StringBuffer    sbSQL1          = new StringBuffer("");
        int             isOk            = 0;

        int             iSysAdd         = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String          v_contenttype   = "";
        int             v_datecnt       = 0;
        int             v_edudatecnt    = 0;
        boolean         isPageCheck= false;
        
        try { 
            sbSQL.append(" select  nvl(contenttype,'N') contenttype             \n")
                 .append(" from    tz_subj                                      \n")
                 .append(" where   subj   = " + SQLString.Format(p_subj) + "    \n");
            
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_contenttype   = ls.getString("contenttype");
            }
            
            ls.close();
            
            sbSQL.setLength(0);
            
            if ( v_contenttype.equals("O") || v_contenttype.equals("OA") ) {	// OBC, OBC-Author
            	
            	// 전체 OID 갯수
                sbSQL.append(" select  count(*) datecnt                                     \n")
                     .append(" from    tz_subjobj                                           \n")
                     .append(" where   subj = " + SQLString.Format(p_subj) + "              \n")
                     .append(" and     type = 'SC'                                          \n");

                // 학습한 OID 갯수
                sbSQL1.append(" SELECT  count(*) edudatecnt                                 \n")                            
                      .append(" FROM    tz_progress a                                       \n")
                      .append("     ,   tz_subjobj  b                                       \n")
                      .append(" WHERE   a.subj      = " + SQLString.Format(p_subj   ) + "   \n")
                      .append(" AND     a.year      = " + SQLString.Format(p_year   ) + "   \n")
                      .append(" AND     a.subjseq   = " + SQLString.Format(p_subjseq) + "   \n")
                      .append(" AND     a.userid    = " + SQLString.Format(p_userid ) + "   \n")
                      .append(" AND     a.FIRST_END IS NOT NULL                             \n")
                      .append(" AND     a.subj      = b.subj                                \n")
                      .append(" AND     a.lesson    = b.lesson                              \n")
                      .append(" AND     a.oid       = b.oid                                 \n")
                      .append(" AND     nvl(lesson_count, 0) > 0                            \n");

            } else if ( v_contenttype.equals("S") ) {	// SCORM2004
                
                // 전체 sco 갯수
                sbSQL.append( " SELECT COUNT (*) datecnt                               \n" )
					 .append( " FROM tys_item a, tys_resource b, tz_subj_contents c    \n" )
					 .append( " WHERE 1 = 1                                            \n" )
					 .append( "    AND a.course_code = b.course_code                   \n" )
					 .append( "    AND b.course_code = c.course_code                   \n" )
					 .append( "    AND a.org_id = b.org_id                             \n" )
					 .append( "    AND b.org_id = c.org_id                             \n" )
					 .append( "    AND a.item_id = b.item_id                           \n" )
					 .append( "    AND b.res_scorm_type = 'sco'                        \n" )
					 .append( "    AND c.subj = " + SQLString.Format(p_subj) + "       \n" );

                // 학습한 sco 갯수
                sbSQL1.append(" select  count(lesson) edudatecnt     			            \n")
                      .append(" from    tz_progress                                         \n")
                      .append(" where   subj        = " + SQLString.Format(p_subj   ) + "   \n")
                      .append(" and     year        = " + SQLString.Format(p_year   ) + "   \n")
                      .append(" and     subjseq     = " + SQLString.Format(p_subjseq) + "   \n")
                      .append(" and     userid      = " + SQLString.Format(p_userid ) + "   \n")
                      .append(" and     first_edu  IS NOT NULL                              \n")
                      .append(" and     nvl(lesson_count, 0) > 0                            \n");

            } else {	// Normal, KT
            	int v_lessonCnt = 0;
            	
            	sbSQL.setLength(0);
            	sbSQL.append(" select count(*) cnt from tz_subjlesson_page where subj = "+SQLString.Format(p_subj) + " \n "); //차시별 페이지 정보가 있는지 확인
            	ls  = connMgr.executeQuery(sbSQL.toString());
            	
                while ( ls.next() ) { 
                    v_lessonCnt   = ls.getInt("cnt");
                    if(v_lessonCnt > 0){
                    	isPageCheck = true;
                    }
                }
                
                if( ls != null) { ls.close(); }
                
                sbSQL.setLength(0);
                
            	if(isPageCheck){ //페이지별 진도
            		
            	    //전체 페이지 갯수
	                sbSQL.append(" select  count(*) datecnt                                \n")
	                     .append(" from    tz_subjlesson_page                                   \n")
	                     .append(" where   subj = " + SQLString.Format(p_subj) + "              \n")
	           	         .append(" and     lesson != '00' and lesson != '99'                    \n");
	                
	                // 학습한 차시별 페이지 갯수
	                sbSQL1.append("   select a.subj, a.year, a.subjseq, a.lesson, a.stu_page, b.pagenum edudatecnt ")
	                     .append("     from tZ_progress a, tz_subjlesson_page b ") 
	                     .append("    where 1=1 ") 
	                     .append("      and a.subj = b.subj ") 
	                     .append("      and a.lesson = b.lesson ") 
	                     .append("      and a.stu_page = substr(b.starting, instr(b.starting, '/', -1) + 1) ") 
		                 .append("      and a.subj = "+SQLString.Format(p_subj)+" \n ") 
		                 .append("      and a.year = "+SQLString.Format(p_year   )+" \n ") 
		                 .append("      and a.subjseq = "+SQLString.Format(p_subjseq)+" \n ") 
		                 .append("      and a.userid = "+SQLString.Format(p_userid)+" \n ") 
		                 .append("      --and a.lesson = (select max(lesson) from tz_progress where subj="+SQLString.Format(p_subj)+" and year="+SQLString.Format(p_year)+" and subjseq="+SQLString.Format(p_subjseq)+" and userid="+SQLString.Format(p_userid)+") \n ") 
		                 .append("      --and     nvl(lesson_count, 0) > 0 ") 
		                 .append("    group by a.subj, a.year, a.subjseq, a.lesson, a.stu_page, b.pagenum ") ;
                }else{
                	
	            	// 전체 차시 갯수
	                sbSQL.append(" select  count(*) datecnt                                     \n")
	                     .append(" from    tz_subjlesson                                        \n")
	                     .append(" where   subj = " + SQLString.Format(p_subj) + "              \n")
	                	 .append(" and     lesson != '00' and lesson != '99'                    \n");
	                
	                // 학습한 차시 갯수
	                sbSQL1.append(" select  count(lesson) edudatecnt     			            \n")
	                      .append(" from    tz_progress                                         \n")
	                      .append(" where   subj        = " + SQLString.Format(p_subj   ) + "   \n")
	                      .append(" and     year        = " + SQLString.Format(p_year   ) + "   \n")
	                      .append(" and     subjseq     = " + SQLString.Format(p_subjseq) + "   \n")
	                      .append(" and     userid      = " + SQLString.Format(p_userid ) + "   \n")
	                      .append(" and     first_edu  IS NOT NULL                              \n")
	                      .append(" and     nvl(lesson_count, 0) > 0                            \n");
                }
                
            }

            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_datecnt   = ls.getInt("datecnt");
            }

            if( ls != null) { ls.close(); }
            
            ls  = connMgr.executeQuery(sbSQL1.toString());
            
            while ( ls.next() ) { 
            	if(isPageCheck){
            		v_edudatecnt += ls.getInt("edudatecnt");
            	}else{
            		v_edudatecnt = ls.getInt("edudatecnt");
            	}
            }
            
            if( ls != null) { ls.close(); }

            if ( v_edudatecnt == 0 ) { 
                data.setTstep(0);
            } else  { 
                data.setTstep((double)Math.round((double)v_edudatecnt / v_datecnt * 100 * 100) / 100);
            }

            if ( data.getTstep() > 100 ) { 
                data.setTstep(100);
            }

            if ( data.getWstep() == 0 ) { 
                data.setAvtstep(0);
            } else { 
                data.setAvtstep((double)Math.round(data.getTstep()*data.getWstep()) / 100);
            }

            if ( data.getAvtstep() > data.getWstep() ) { 
                data.setAvtstep(data.getWstep());
            }
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
        }

        return isOk;
    }    

    
    
	/**
    종합점수계산_calc_offtstep
   @param box          receive from the form object and session 8
   @return int
   */
	 public int calc_offtstep(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception {
        ListSet ls  = null;
        ListSet ls1  = null;
        ListSet ls2  = null;
        ListSet ls3  = null;
        ListSet ls4  = null;
        ListSet ls5  = null;       
        PreparedStatement pstmt = null;
        String sql  = "";
        String sql1 = "";
	 	int isOk = 1;

        String v_contenttype = "";
        String sql_datecnt    = "";
        String sql_edudatecnt = "";
        int v_datecnt = 0;
        int v_edudatecnt = 0;
		
		double v_tstepsum = 0.0;
		double v_avtstepsum = 0.0;
		double v_tstep = 0.0;
        double v_wstep = 0.0;
        double v_etc1  = 0.0;
        
        String v_isattend = "";
        String v_demerit_code = "";
        String v_attdate  = "";
        ConfigSet conf = new ConfigSet();
        int vio_cnt = 0;
        try {

            sql1 =" select                                                                  \n";
            sql1+="   x.subj, x.year, x.subjseq, x.userid, x.lectdate, c.attdate, c.atttime, c.isattend \n";
            sql1+=" from tz_attendance c,                                                   \n";
            sql1+=" (                                                                       \n";
            sql1+=" select                                                                  \n";
            sql1+="   a.subj, a.year, a.subjseq, a.lectdate, a.class, '"+p_userid+"' userid \n";
            sql1+=" from                                                                    \n";
            sql1+="   tz_offsubjlecture a,                                                  \n";
            sql1+="   tz_student b                                                          \n";
            sql1+=" where                                                                   \n";
            sql1+="   a.subj = b.subj                                                       \n";
            sql1+="   and a.year = b.year                                                   \n";
            sql1+="   and a.subjseq = b.subjseq                                             \n";
            sql1+="   and a.class = b.class                                                 \n";
            sql1+="   and a.subj = '"+p_subj+"'                                             \n";
            sql1+="   and a.year = '"+p_year+"'                                             \n";
            sql1+="   and a.subjseq = '"+p_subjseq+"'                                       \n";
            sql1+="   and b.userid = '"+p_userid+"'                                         \n";
            //sql1+="   and a.lectdate <= to_char(sysdate, 'yyyymmdd')                        \n";
            sql1+="   and lectdate||decode((select upperclass from tz_subj where tz_subj.subj = a.subj), 'A01', '1650', 'C01', '2150', '1650' ) ";
            sql1+="   <= to_char(sysdate, 'yyyymmddhh24mi')                          \n";
            sql1+=" group by                                                                \n";
            sql1+="   a.subj, a.year, a.subjseq, a.lectdate, a.class                        \n";
            sql1+=" ) x                                                                     \n";
            sql1+=" where                                                                   \n";
            sql1+="  x.subj = c.subj(+)                                                     \n";
            sql1+="  and x.year = c.year(+)                                                 \n";
            sql1+="  and x.subjseq = c.subjseq(+)                                           \n";
            sql1+="  and x.userid = c.userid(+)                                             \n";
            sql1+="  and x.lectdate = c.attdate(+)                                          \n";
            sql1+=" order by                                                                \n";
            sql1+="  x.lectdate                                                             \n";
            //System.out.println("sql1계산==============>>>>>"+sql1);
            ls2 = connMgr.executeQuery(sql1);

            //isattend Y-출석, L-지각10분초과1시간이내, M-1시간초과 ''-결석
            while(ls2.next()){
              v_isattend = ls2.getString("isattend");
              if(v_isattend.equals("Y")){  //출석 delete
                
              }
              else{ //출석하지 않았을경우 처리
                v_attdate  = ls2.getString("attdate");
                if(v_isattend.equals("L")){  //L-지각10분초과1시간이내 tz_student_violation 에 insert
                  v_demerit_code = conf.getProperty("demerit_code.late1.value");
                }else if(v_isattend.equals("M")){  //M-1시간초과  tz_student_violation 에 insert
                  v_demerit_code = conf.getProperty("demerit_code.late2.value");
                }else if(v_isattend.equals("")){  //결석 tz_student_violation 에 insert
                  v_demerit_code = conf.getProperty("demerit_code.absent.value");
                  v_attdate = ls2.getString("lectdate");
                }
                sql1 = get_violation_SqlString(p_subj, p_year,p_subjseq,p_userid,v_attdate, v_demerit_code);
                //System.out.println("sql지각??결석??========="+v_isattend+"===============>>>>"+sql1);
                
                ls3 = connMgr.executeQuery(sql1);
                if(ls3.next()){
                  vio_cnt = ls3.getInt("cnt");
                }
                //System.out.println("vio_cnt========/////////////++++++>>>>>"+vio_cnt);
                ls3.close();
                
                //vio_cnt 값이 없으면 insert해준다.
                if(vio_cnt == 0) {
                  int v_serialno   =0;
                  double v_demerit =0;
                  sql1 = " select nvl(max(SERIAL_NO), 0) from TZ_STUDENT_VIOLATION where subj='"+p_subj+"' and year='"+p_year+"' and subjseq = '"+p_subjseq+"' ";
			      ls4 = connMgr.executeQuery(sql1);
			      if(ls4.next()){ v_serialno = ls4.getInt(1) + 1;    }

			      sql1 = "select demerit from TZ_VIOLATION_INFO where top_code = substr('"+v_demerit_code+"',1,2) and middle_code = substr('"+v_demerit_code+"',3,3)";
			      ls5 = connMgr.executeQuery(sql1);
			      if(ls5.next()){ v_demerit = ls5.getDouble(1);    }

			      sql1 =  "insert into TZ_STUDENT_VIOLATION(subj,year,subjseq,userid,login_date,serial_no,demerit_code,demerit_score) ";
			      sql1 += " values (?, ?, ?, ?, ?, ?, ?, ?)           ";

			      pstmt = connMgr.prepareStatement(sql1);
			      pstmt.setString(1, p_subj);
			      pstmt.setString(2, p_year);
			      pstmt.setString(3, p_subjseq);
			      pstmt.setString(4, p_userid);
			      pstmt.setString(5, v_attdate);
			      pstmt.setInt   (6, v_serialno);
			      pstmt.setString(7, v_demerit_code);
			      pstmt.setDouble(8, v_demerit);
			      isOk = pstmt.executeUpdate();
			      if ( pstmt != null ) { pstmt.close(); }
			      //System.out.println("isOk222222222222222=================================>>>>>"+isOk);
                }
              }
            }
            ls2.close();

			sql =" select a.wstep ";  //적용 가중치
			sql+=" 	  ,sum(b.demerit_score) as dscore "; //총 감점
			sql+=" 	  ,CASE WHEN a.wstep - sum(b.demerit_score) > 0                          ";
			sql+=" 	        THEN a.wstep - sum(b.demerit_score) ELSE SUM(0) END AS avtstepsum  ";  //감점적용후 출석률점수
			sql+=" from tz_subjseq a, TZ_STUDENT_VIOLATION b ";
			sql+="  where a.subj = b.subj                                                    ";
			sql+="  and a.year = b.year                                                    ";
			sql+="  and a.subjseq = b.subjseq                                                    ";
			sql+="  and   a.subj =		" + SQLString.Format(p_subj);
			sql+="  and   a.year =		" + SQLString.Format(p_year);
			sql+="  and   a.subjseq =	" + SQLString.Format(p_subjseq);
			sql+="  and   b.userid =	" + SQLString.Format(p_userid);
			sql+="  group by a.wstep                                                         ";
			//System.out.println("진도계산 테스트===>>"+sql);

            ls = connMgr.executeQuery(sql);
            if (ls.next()) {
				v_etc1       = ls.getDouble("dscore");         //감점총점
				v_avtstepsum = ls.getDouble("avtstepsum");     //감점적용후 근태(출석)점수(가중치점수)
				v_wstep      = ls.getDouble("wstep");          //가중치
				v_tstepsum   = (double)Math.round(v_avtstepsum*100/v_wstep);  //취득점수 역환산
				
				//System.out.println("v_etc1="      +v_etc1       );
				//System.out.println("v_avtstepsum="+v_avtstepsum );
				//System.out.println("v_wstep="     +v_wstep      );
				//System.out.println("v_tstepsum="  +v_tstepsum   );
				
				if (v_tstepsum == 0) {
                  data.setTstep(0);
                  data.setAvtstep(0);
			  	  //data.setEtc1(v_etc1);
			    } else {
                  data.setAvtstep(v_avtstepsum);
			    	data.setTstep(v_tstepsum);
			    	//data.setEtc1(v_etc1);
			    }
			}else{  //감점항목이 전혀 없는경우
			  sql = "select wstep from tz_subjseq ";
			  sql+= "where ";
			  sql+="   subj =		" + SQLString.Format(p_subj);
			  sql+="  and   year =		" + SQLString.Format(p_year);
			  sql+="  and   subjseq =	" + SQLString.Format(p_subjseq);
			  ls1 = connMgr.executeQuery(sql);
              if(ls1.next()){
			   data.setTstep(100);
               data.setAvtstep(ls1.getDouble("wstep"));
			   //data.setEtc1(v_etc1);
			  }
			  ls1.close();
			}
      ls.close();

			//System.out.println("data.getAvtstep()=====================>>L>>>>"+data.getAvtstep());

            //if (data.getAvtstep() > v_wstep) {
            //    data.setAvtstep(v_wstep);
             //}
         }
        catch (Exception ex) {
            isOk = -1;
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
            if(ls3 != null) { try { ls3.close(); }catch (Exception e) {} }
            if(ls4 != null) { try { ls4.close(); }catch (Exception e) {} }
            if(ls5 != null) { try { ls5.close(); }catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
        }

        return isOk;
    }
    
    public String get_violation_SqlString(String p_subj, String p_year, String p_subjseq, String p_userid, String p_attdate, String p_demerit_code) {
      String sql1 = "";

      sql1 = "select ";
      sql1+= "  count(*) cnt from tz_student_violation ";
      sql1+= "where ";
      sql1+= "  subj = '"+p_subj+"' and year = '"+p_year+"' and subjseq = '"+p_subjseq+"' and userid = '"+p_userid+"' ";
      sql1+= "  and login_date    = '"+p_attdate+"'";
      sql1+= "  and demerit_code = '"+p_demerit_code+"'";
      return sql1;
    }
    
	/**
    집합 점수 계산
    @param box          receive from the form object and session 8
    @return int
    */
    public int calc_offscore(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_luserid, StoldData data) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
        String v_contenttype = "";
        ListSet ls = null;
        double v_demerit_score = 0;

        double v_ftest   = 0;
        //double v_test1   = 0;
        //double v_test2   = 0;
        //double v_test3   = 0;
        //double v_test4   = 0;
        double v_report  = 0;
        double v_wftest  = 0;
        //double v_wtest1  = 0;
        //double v_wtest2  = 0;
        //double v_wtest3  = 0;
        //double v_wtest4  = 0;
        double v_wreport = 0;
        double v_etc2    = 0;

        double v_avftest   = 0;
        //double v_avtest1   = 0;
        //double v_avtest2   = 0;
        //double v_avtest3   = 0;
        //double v_avtest4   = 0;
        double v_avreport  = 0;

        double v_score = 0;
        
        String v_stdgubun = "";

        int isOk = 0;

         int cnt = 0;

         try {
			  //출석률,이론평가1, 이론평가2, 실습평가1, 실습평가2, 실습평가3

			  data.setSubj     (p_subj);
              data.setYear     (p_year);
              data.setSubjseq  (p_subjseq);
              data.setUserid   (p_userid);

			  //감점 적용 출석률
			  isOk = calc_offtstep(connMgr, p_subj, p_year, p_subjseq, p_userid, data);
			  
			  
			  //wtest1 - 이론평가2
			  //wtest2 - 실습평가1
			  //wtest3 - 실습평가2
			  //wtest4 - 실습평가3

			  sql= " select      \n";
              sql+= "   a.ftest,   \n";    //이론평가1
              //sql+= "   a.test1,  a.test2,  a.test3,  a.test4,  \n";
              sql+= "   a.report, \n";
              sql+= "   b.wftest,\n";    //이론평가1 가중치
              //sql+= "   b.wtest1, b.wtest2, b.wtest3, b.wtest4, \n";
              sql+= "   b.wreport , \n";    //레포트 가중치
              //sql+= "   a.stdgubun \n";    //학생구분 가점
              sql+= " from \n";
              sql+= "   tz_student a, tz_subjseq b \n";
              sql+= " where\n";
              sql+= "   a.subj = b.subj\n";
              sql+= "   and a.year = b.year\n";
              sql+= "   and a.subjseq = b.subjseq\n";
              sql+= "   and a.subj = '"+p_subj+"'\n";
              sql+= "   and a.year = '"+p_year+"'\n";
              sql+= "   and a.subjseq = '"+p_subjseq+"'\n";
              sql+= "   and a.userid  = '"+p_userid+"'\n";

              ls = connMgr.executeQuery(sql);

              if(ls.next()){
                v_ftest   = ls.getDouble("ftest");
                //v_test1   = ls.getDouble("test1");
                //v_test2   = ls.getDouble("test2");
                //v_test3   = ls.getDouble("test3");
                //v_test4   = ls.getDouble("test4");
                v_report  = ls.getDouble("report");
                v_wftest  = ls.getDouble("wftest");
                //v_wtest1  = ls.getDouble("wtest1");
                //v_wtest2  = ls.getDouble("wtest2");
                //v_wtest3  = ls.getDouble("wtest3");
                //v_wtest4  = ls.getDouble("wtest4");
                v_wreport = ls.getDouble("wreport");
                //v_avftest = (double)Math.round(v_ftest*v_wftest)/100;
                //v_avtest1 = (double)Math.round(v_test1*v_wtest1)/100;
                //v_avtest2 = (double)Math.round(v_test2*v_wtest2)/100;
                //v_avtest3 = (double)Math.round(v_test3*v_wtest3)/100;
                //v_avtest4 = (double)Math.round(v_test4*v_wtest4)/100;
                //v_avreport= (double)Math.round(v_report*v_wreport)/100;
                //v_etc2    = ls.getDouble("addscore");  //학생가점
                
                //v_stdgubun = ls.getString("stdgubun");
                
                data.setWftest(v_wftest);
                data.setWreport(v_wreport);
                
                isOk = calc_exam        (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
                isOk = calc_report      (connMgr, p_subj, p_year, p_subjseq, p_userid, data);

                //data.setFtest   (v_ftest);
                data.setFtest   (data.getFtest());
                //data.setTest1   (v_test1);
                //data.setTest2   (v_test2);
                //data.setTest3   (v_test3);
                //data.setTest4   (v_test4);
                data.setReport  (data.getReport());
                data.setWftest  (v_wftest);
                //data.setWtest1  (v_wtest1);
                //data.setWtest2  (v_wtest2);
                //data.setWtest3  (v_wtest3);
                //data.setWtest4  (v_wtest4);
                data.setWreport (v_wreport);
                //data.setAvftest (v_avftest);
                data.setAvftest (data.getAvftest());
                //data.setAvtest1 (v_avtest1);
                //data.setAvtest2 (v_avtest2);
                //data.setAvtest3 (v_avtest3);
                //data.setAvtest4 (v_avtest4);
                data.setAvreport(data.getAvreport());
                //data.setEtc2    (v_etc2);
                //data.setAvetc2  (v_etc2);
                //data.setStdgubun(v_stdgubun);
              }
              //System.out.println("data.getAvreport()====>>>>>>>"+data.getAvreport());
              //System.out.println("data.getTstep()====>>>>>>>"+data.getTstep());
              //System.out.println("data.getEtc2();====>>>>>>>"+data.getEtc2());

              isOk = calc_isgraduated(connMgr, p_subj, p_year, p_subjseq, p_userid, data);   //수료여부

              // 점수 수정
              sql = " update tz_student ";
              sql+= "    set score   = ?, ";  //1
              sql+= "        tstep   = ?, ";  //2
            //  sql+= "        mtest   = ?, ";  //3 
              sql+= "        ftest   = ?, ";  //4 
              sql+= "        htest   = ?, ";  //5 
              sql+= "        report  = ?, ";  //6 
              sql+= "        act     = ?, ";  //7 
              sql+= "        etc1    = ?, ";  //8 
              sql+= "        etc2    = ?, ";  //9 
              //sql+= "        test1   = ?, ";  //10
              //sql+= "        test2   = ?, ";  //11
              //sql+= "        test3   = ?, ";  //12
              //sql+= "        test4   = ?, ";  //13
              sql+= "        avtstep = ?, ";  //14
              sql+= "        avmtest = ?, ";  //15
              sql+= "        avftest = ?, ";  //16
              sql+= "        avhtest = ?, ";  //17
              sql+= "        avreport= ?, ";  //18
              sql+= "        avact   = ?, ";  //19
              sql+= "        avetc1  = ?, ";  //20
              sql+= "        avetc2  = ?, ";  //21
              //sql+= "        avtest1 = ?, ";  //22
              //sql+= "        avtest2 = ?, ";  //23
              //sql+= "        avtest3 = ?, ";  //24
              //sql+= "        avtest4 = ?, ";  //25
              sql+= "        luserid = ?, ";  //26
              sql+= "        ldate   = ?, ";  //27
              sql+= "        isgraduated = ? ";  //28
              sql+= "  where subj    = ? ";  //29
              sql+= "    and year    = ? ";  //30
              sql+= "    and subjseq = ? ";  //31
              sql+= "    and userid  = ? ";  //32

              pstmt = connMgr.prepareStatement(sql);
              pstmt.setDouble(1 ,  data.getScore());
              pstmt.setDouble(2 ,  data.getTstep());
             // pstmt.setDouble(3 ,  data.getMtest());
              pstmt.setDouble(3 ,  data.getFtest());
              pstmt.setDouble(4 ,  data.getHtest());//System.out.println("data.getHtest()" + data.getHtest());
              pstmt.setDouble(5 ,  data.getReport());
              pstmt.setDouble(6 ,  data.getAct());
              pstmt.setDouble(7 ,  data.getEtc1());
              pstmt.setDouble(8 ,  data.getEtc2());
              //pstmt.setDouble(10,  data.getTest1());
              //pstmt.setDouble(11, data.getTest2());
              //pstmt.setDouble(12, data.getTest3());
              //pstmt.setDouble(13, data.getTest4());
              pstmt.setDouble(9, data.getAvtstep() );
              pstmt.setDouble(10, data.getAvmtest() );
              pstmt.setDouble(11, data.getAvftest() );
              pstmt.setDouble(12, data.getAvftest() );//System.out.println("data.getAvhtest()" + data.getAvhtest());
              pstmt.setDouble(13, data.getAvreport());
              pstmt.setDouble(14, data.getAvact()   );
              pstmt.setDouble(15, data.getAvetc1()  );
              pstmt.setDouble(16, data.getAvetc2()  );
              //pstmt.setDouble(22, data.getAvtest1() );
              //pstmt.setDouble(23, data.getAvtest2() );
              //pstmt.setDouble(24, data.getAvtest3() );
              //pstmt.setDouble(25, data.getAvtest4() );
              pstmt.setString(17, p_luserid);
              pstmt.setString(18, FormatDate.getDate("yyyyMMddHHmmss"));
              pstmt.setString(19, data.getIsgraduated());
              pstmt.setString(20, p_subj);
              pstmt.setString(21, p_year);
              pstmt.setString(22, p_subjseq);
              pstmt.setString(23, p_userid);

              isOk = pstmt.executeUpdate();
              if ( pstmt != null ) { pstmt.close(); }
        }
        catch (Exception ex) {
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
        }
        return isOk;
    }
		
		/**
    종합점수계산_yeslearn 
    @param box          receive from the form object and session 8
    @return int
    */
    public int calc_step_yeslearn(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet ls  = null;
        String sql  = "";
        int isOk = 0;

        String v_contenttype = "";
        String sql_datecnt    = "";
        String sql_edudatecnt = "";
        int v_datecnt = 0;
        int v_edudatecnt = 0;
        double v_tstep = 0.0;
        double v_wstep = 0.0;

        try { 
            sql = " select nvl(contenttype,'N') contenttype ";
            sql += "   from tz_subj  ";
            sql += "  where subj   = " + SQLString.Format(p_subj);
            sql += "    and rownum = 1 ";

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
                v_contenttype = ls.getString("contenttype");
            }
            ls.close();
            
            sql  = " select a.tstep, ";
            sql += " 				b.wstep ";
            sql += "   from TZ_STUDENT a,";
            sql += "        TZ_SUBJSEQ b";
            sql += "  where a.subj = b.subj";
            sql += "    and a.year = b.year";
            sql += "    and a.subjseq = b.subjseq";
            sql += "    and a.subj=" +SQLString.Format(p_subj);
            sql += "    and a.year=" +SQLString.Format(p_year);
            sql += "    and a.subjseq=" +SQLString.Format(p_subjseq);
            sql += "    and a.userid=" +SQLString.Format(p_userid);
            
            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
            	v_tstep = ls.getDouble("tstep");
            	v_wstep = ls.getDouble("wstep");
            }
            ls.close();

            if ( v_wstep == 0 ) { 
                data.setAvtstep(0);
            } else { 
                data.setAvtstep((double)Math.round(v_tstep*v_wstep)/100);
            }

            if ( data.getAvtstep() > v_wstep) { 
                data.setAvtstep(v_wstep);
            }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    
    
    /**
        점수계산 (평가)
        @param  box      receive from the form object
        @return int
    */
    public int calc_exam(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls                  = null;
        StringBuffer    sbSQL               = new StringBuffer("");
        int             isOk                = 0;
        
        int             iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        int             v_ex1cnt            = 0;
        int             v_ex2cnt            = 0;
        
        double          v_ex1result_grade   = 0;
        double          v_ex2result_grade   = 0;
        double          v_ex3result_grade   = 0;
        String          v_lastdate          = "";

        try { 
            /*// START :: Middle Exam -------------------------------------------
            sbSQL.append(" select  count(*)    ex1cnt                               \n")
                 .append(" from    tz_exammaster                                    \n")
                 .append(" where   subj        = " + SQLString.Format(p_subj) + "   \n")
                 .append(" and     examtype    =  'M'                               \n");
            
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_ex1cnt            = ls.getInt("ex1cnt");
            }
            
            ls.close();
            
            sbSQL.setLength(0);
            
            sbSQL.append(" select  sum(score) ex1result_grade, count(*) cnt             \n")
                 .append(" from    tz_examresult                                        \n")
                 .append(" where   subj        = " + SQLString.Format(p_subj   ) + "    \n")
                 .append(" and     year        = " + SQLString.Format(p_year   ) + "    \n")
                 .append(" and     subjseq     = " + SQLString.Format(p_subjseq) + "    \n")
                 .append(" and     userid      = " + SQLString.Format(p_userid ) + "    \n")
                 .append(" and     examtype    = 'M'                                    \n");
            
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_ex1result_grade   = ls.getDouble("ex1result_grade");
                data.setGradexamcnt(ls.getInt("cnt"));
            }
            
            ls.close();

            if ( v_ex1cnt == 0 ) { 
                data.setMtest(0);
            } else  { 
                data.setMtest((double)Math.round((double)v_ex1result_grade/v_ex1cnt*100)/100);
            }
            
            data.setAvmtest((double)Math.round(data.getMtest()*data.getWmtest() )/100);*/
            //** END :: Middle Exam -------------------------------------------
            
            
            // START :: 형성평가 --------------------------------------------------
            sbSQL.setLength(0);
            
            sbSQL.append(" select  count(*)  ex2cnt                                 \n")
                 .append(" from    tz_exammaster                                    \n")
                 .append(" where   subj        = " + SQLString.Format(p_subj) + "   \n")
                 .append(" and     examtype    = 'H'                                \n");
                 
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_ex2cnt            = ls.getInt("ex2cnt");
            }
            
            ls.close();
            
            sbSQL.setLength(0);
            
            sbSQL.append(" select  sum(score) ex2result_grade, count(*) cnt         \n")
                 .append(" from    tz_examresult                                    \n")
                 .append(" where   subj    = " + SQLString.Format(p_subj   ) + "    \n")
                 .append(" and     year    = " + SQLString.Format(p_year   ) + "    \n")
                 .append(" and     subjseq = " + SQLString.Format(p_subjseq) + "    \n")
                 .append(" and     userid  = " + SQLString.Format(p_userid ) + "    \n")
                 .append(" and     examtype= 'H'                                    \n");
                 
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_ex2result_grade   = ls.getDouble("ex2result_grade");
                data.setGradhtestcnt(ls.getInt("cnt"));
            }
            
            ls.close();
            
            if ( v_ex2cnt == 0 ) { 
                data.setHtest(0);
            } else  { 
                data.setHtest((double)Math.round((double)v_ex2result_grade/v_ex2cnt*100)/100);
            }
            
            data.setAvhtest((double)Math.round(data.getHtest()*data.getWhtest() )/100);
            //** END :: 형성평가 --------------------------------------------------
            
            
            // START :: Total Exam --------------------------------------------
            sbSQL.setLength(0);
            
            sbSQL.append(" select  lesson      lastdate                             \n")
                 .append(" from    tz_exammaster                                    \n")
                 .append(" where   subj        = " + SQLString.Format(p_subj) + "   \n")
                 .append(" and     examtype    = 'E'                                \n");

            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_lastdate  = ls.getString("lastdate");
            }
            
            ls.close();
            
            if ( v_lastdate.equals("") ) { 
                data.setFtest   (0);
                data.setAvftest (0);
            } else { 
                sbSQL.setLength(0);
                
                sbSQL.append(" select  score   ex3result_grade,  count(*) over() cnt    \n")
                     .append(" from    tz_examresult                                    \n")
                     .append(" where   subj    = " + SQLString.Format(p_subj   ) + "    \n")
                     .append(" and     year    = " + SQLString.Format(p_year   ) + "    \n")
                     .append(" and     subjseq = " + SQLString.Format(p_subjseq) + "    \n")
                     .append(" and     userid  = " + SQLString.Format(p_userid ) + "    \n")
                     .append(" and     examtype   = 'E'                                 \n");
                
                ls  = connMgr.executeQuery(sbSQL.toString());
                
                while ( ls.next() ) { 
                    v_ex3result_grade = ls.getDouble("ex3result_grade");
                    data.setGradftestcnt(ls.getInt("cnt"));
                }
                
                ls.close();
                
                data.setFtest   ((double)(v_ex3result_grade*100)/100);
                data.setAvftest ((double)Math.round(data.getFtest()*data.getWftest() )/100);
            }
            //** END :: Total Exam --------------------------------------------
            
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
        }
        
       return isOk;
    }
    
    //중강편가(출석시험)
    public int calc_exam2(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls                  = null;
        StringBuffer    sbSQL               = new StringBuffer("");
        int             isOk                = 0;
        
        int             iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        int             v_ex1cnt            = 0;
        int             v_ex2cnt            = 0;
        
        double          v_ex1result_grade   = 0;
        double          v_ex2result_grade   = 0;
        double          v_ex3result_grade   = 0;
        String          v_lastdate          = "";

        try { 
                        
            //2.tz_student에 있는 mtest의 값을 가지고 온다.
            
            sbSQL.append(" select  mtest ex1result_grade             \n")
	            .append(" from    tz_student                                        \n")
	            .append(" where   subj        = " + SQLString.Format(p_subj   ) + "    \n")
	            .append(" and     year        = " + SQLString.Format(p_year   ) + "    \n")
	            .append(" and     subjseq     = " + SQLString.Format(p_subjseq) + "    \n")
	            .append(" and     userid      = " + SQLString.Format(p_userid ) + "    \n");
            
       
		       ls  = connMgr.executeQuery(sbSQL.toString());
		       
		       while ( ls.next() ) { 
		           v_ex1result_grade   = ls.getDouble("ex1result_grade");
		         
		       }
		       
		       ls.close();
		       
		       data.setMtest   ((double)(v_ex1result_grade*100)/100);  //출석시험 원 점수
               data.setAvmtest ((double)Math.round(data.getMtest()*data.getWmtest() )/100); //원점수*가중치/100     
		      		           
            
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
        }
        
       return isOk;
    }
    

    /**
    리포트 점수 계산
    @param box          receive from the form object and session
    @return int
    */
    public int calc_report2(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls1         = null;
        ListSet         ls2         = null;
        StringBuffer    sbSQL       = new StringBuffer("");
        int             isOk        = 0;
        
        int             iSysAdd     = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String v_projgrp    = "";
        String v_ordseq     = "";
        int    v_repcnt     = 0;
        int    v_score      = 0;
        int    v_score_sum  = 0;
        double v_totalScore = 0;
        double v_grpScore   = 0;
        double v_mreport	= 0;
        double v_freport	= 0;

        try { 
            /*-------- Calc  Grade of Report ------------------------------------------------------*/
        	// 리포트 배정받은 갯수
            sbSQL.append(" select  count(grpseq) as repcnt                              \n")
                 .append(" from    tz_projassign                                        \n")
                 .append(" where   subj        = " + SQLString.Format(p_subj   ) + "    \n")
                 .append(" and     year        = " + SQLString.Format(p_year   ) + "    \n")
                 .append(" and     subjseq     = " + SQLString.Format(p_subjseq) + "    \n")
                 .append(" and     userid      = " + SQLString.Format(p_userid ) + "    \n");
                 
            ls1  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls1.next() ) { 
                v_repcnt    = ls1.getInt("repcnt");
            }
            
            data.setGradreportcnt(v_repcnt);
            
            ls1.close();

            if ( v_repcnt == 0 ) { 
                data.setReport  (0);
                data.setAvreport(0);
            } else { 
            

            	// 2008.11.28 김미향 수정
            	// 배정받은 리포트 문제지 번호 가져옴.
            	sbSQL.setLength(0);
                sbSQL.append(" select  grpseq			                                    \n")
	                 .append(" from    tz_projassign                                        \n")
	                 .append(" where   subj        = " + SQLString.Format(p_subj   ) + "    \n")
	                 .append(" and     year        = " + SQLString.Format(p_year   ) + "    \n")
	                 .append(" and     subjseq     = " + SQLString.Format(p_subjseq) + "    \n")
	                 .append(" and     userid      = " + SQLString.Format(p_userid ) + "    \n");
                
		        ls1  = connMgr.executeQuery(sbSQL.toString());

		        v_mreport = 0;
		        v_freport = 0;
            	v_totalScore = 0;
            	v_grpScore   = 0;
		        while ( ls1.next() ) { 
	            	// 문제지 총점과 문제지별 맞은 점수
                    sbSQL.setLength(0);
                    sbSQL.append(" select  b.projgubun                        								\n")
                         .append("      ,  nvl(a.totalscore, 0) as totalscore                        		\n")
                         .append("      ,  nvl(b.grptotalscore, 0) as grptotalscore                        	\n")
                         .append(" from    tz_projassign a, tz_projgrp b                                	\n")
                         .append(" where   a.subj    = b.subj    											\n")
                         .append(" and     a.year    = b.year    											\n")
                         .append(" and     a.subjseq = b.subjseq    										\n")
                         .append(" and     a.grpseq  = b.grpseq    											\n")
                         .append(" and     a.subj    = " + SQLString.Format( p_subj              ) + "    	\n")
                         .append(" and     a.year    = " + SQLString.Format( p_year              ) + "    	\n")
                         .append(" and     a.subjseq = " + SQLString.Format( p_subjseq           ) + "    	\n")
                         .append(" and     a.grpseq  = " + SQLString.Format( ls1.getInt("grpseq")) + "    	\n")
                         .append(" and     a.userid  = " + SQLString.Format( p_userid            ) + "    	\n");
                         
                    ls2 = connMgr.executeQuery(sbSQL.toString());
                    
                    while ( ls2.next() ) { 
                    	v_score = ls2.getInt("totalscore");
                    	
                    	if ("M".equals(ls2.getString("projgubun"))) {
                    		v_mreport += v_score;
                    	}
                    	if ("F".equals(ls2.getString("projgubun"))) {
                    		v_freport += v_score;
                    	}
                    	v_totalScore += v_score;
                    	v_grpScore   += ls2.getInt("grptotalscore");
                    }
                    if (ls2 != null) { try { ls2.close(); } catch ( Exception e ) { } }
                }
                if (ls1 != null) { try { ls1.close(); } catch ( Exception e ) { } }

                // 문제지의 총점이 유동적이기 때문에(100점이 아닐수 있다) 맞은점수를 100점 만점으로 환산한다.
                if(v_totalScore > 0 && v_grpScore > 0){
                	data.setReport( Double.parseDouble(String.format("%.2f", (v_totalScore / v_grpScore))) * 100 );        // 맞은점수/문제지총점 * 100
                }
                else{
                	data.setReport(0);
                }
                
                if(v_totalScore > 0 && v_grpScore > 0){
                	data.setAvreport( (data.getReport() * data.getWreport()) / 100 );		// (맞은점수/문제지총점 * 100) * 가중치
                }
                else{
                	data.setAvreport(0);
                }
                
                data.setMreport(v_mreport); // 중간리포트점수
                data.setFreport(v_freport); // 최종리포트점수
                
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
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
        }

        return isOk;
    }
    
    /**
    리포트 점수 계산
    @param box          receive from the form object and session
    @return int
    */
    public int calc_report(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls1         = null;
        ListSet         ls2         = null;
        StringBuffer    sbSQL       = new StringBuffer("");
        int             isOk        = 0;
        
        int             iSysAdd     = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String v_projgrp    = "";
        String v_ordseq     = "";
        double    v_repcnt     = 0;
        int    v_score      = 0;
        int    v_score_sum  = 0;
        double v_totalScore = 0;
        double v_grpScore   = 0;
        double v_mreport	= 0;
        double v_freport	= 0;

        try { 
            /*-------- Calc  Grade of Report ------------------------------------------------------*/
        	// 온라인과제점수
            sbSQL.append(" select  score                                            \n")
                 .append(" from    TU_PROJREP                                        \n")
                 .append(" where   grcode      = 'N000001'    \n")
                 .append(" and     subj        = " + SQLString.Format(p_subj   ) + "    \n")
                 .append(" and     year        = " + SQLString.Format(p_year   ) + "    \n")
                 .append(" and     subjseq     = " + SQLString.Format(p_subjseq) + "    \n")
                 .append(" and     projid      = " + SQLString.Format(p_userid ) + "    \n");
                 
            ls1  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls1.next() ) { 
                v_repcnt    = ls1.getDouble("score");
            }
            
            data.setReport(v_repcnt);
            data.setAvreport((double)Math.round(v_repcnt*data.getWreport() )/100);
           
//            data.setMreport(v_mreport); // 중간리포트점수
//            data.setFreport(v_repcnt); // 최종리포트점수
         
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
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
        }

        return isOk;
    }
    
    
        
    /**
    점수계산 (activity) 향후 사용할 때 주석처리된 쿼리를 복원한다.
    @param  box      receive from the form object
    @return int
    */
    public int calc_activity(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls          = null;
        StringBuffer    sbSQL       = new StringBuffer("");
        int             isOk        = 0;
        
        int             iSysAdd     = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        //int             v_actcnt    = 0;
        int v_act = 0;
        
        try { 
            /*-------- Calc  Grade of Activity -----------------------------------------------------*/
            /* 3) Activity점수  = Activity평균*Activity가중치 = [ SUM(Act점수/만점점수) / count(Act) ] * 100 * Act가중치*/
            /*
            sbSQL.append(" select  count(*) actcnt                              \n")
                 .append(" from    tz_activity                                  \n")
                 .append(" where   subj = " + SQLString.Format(p_subj) + "      \n");
            
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
               v_actcnt = ls.getInt("actcnt");
            }
            
            ls.close();
            
            sbSQL.setLength(0);
            

            if ( v_actcnt == 0 ) { 
                data.setAct  (0);
                data.setAvact(0);
            } else { 
                sbSQL.append(" select  (nvl(sum( nvl(a.point,0) ),0) / " + SQLString.Format(v_actcnt) + " )  act                                                            \n")
                     .append("     ,   (nvl(sum( nvl(a.point,0)/b.point ),0) / " + SQLString.Format(v_actcnt) + " )* " + SQLString.Format(data.getWact() ) + " avact        \n")
                     .append(" from    tz_activity_ans a                                                                                                                    \n")
                     .append("     ,   tz_activity     b                                                                                                                    \n")
                     .append(" where   a.subj      = b.subj                                                                                                                 \n")
                     .append(" and     a.lesson    = b.lesson(+)                                                                                                            \n")
                     .append(" and     a.seq       = b.seq(+)                                                                                                               \n")
                     .append(" and     a.subj      = " + SQLString.Format(p_subj   ) + "                                                                                    \n")
                     .append(" and     a.year      = " + SQLString.Format(p_year   ) + "                                                                                    \n")
                     .append(" and     a.subjseq   = " + SQLString.Format(p_subjseq) + "                                                                                    \n")
                     .append(" and     a.userid    = " + SQLString.Format(p_userid ) + "                                                                                    \n");
                     
                //System.out.println(this.getClass().getName() + "." + "calc_activity() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                ls  = connMgr.executeQuery(sbSQL.toString());
                
                while ( ls.next() ) { 
                    data.setAct  ( ls.getDouble("act"   ));
                    data.setAvact( ls.getDouble("avact" ));
                }
            }
            */
            /*-------- Calc  Grade of ACT : 교수가점 점수 반영-----------------------------------------------------*/
            sbSQL.append(" select  act                                              \n")
                 .append(" from    tz_student                                       \n")
                 .append(" where   subj    = " + SQLString.Format(p_subj   ) + "    \n")
                 .append(" and     year    = " + SQLString.Format(p_year   ) + "    \n")
                 .append(" and     subjseq = " + SQLString.Format(p_subjseq) + "    \n")
                 .append(" and     userid  = " + SQLString.Format(p_userid ) + "    \n");
                 
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_act  = ls.getInt("act");
            }

            data.setAct(v_act);
            data.setAvact(v_act);

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
        }

       return isOk;
    }
    
        
    /**
    점수계산 (기타)
    @param  box      receive from the form object
    @return int
    */
    public int calc_etc(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls          = null;
        StringBuffer    sbSQL       = new StringBuffer("");
        int             isOk        = 0;
        
        int             iSysAdd     = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        double          v_etc1      = 0;
        double          v_etc2      = 0;

        try { 
            /*-------- Calc  Grade of ETC : 참여도 점수 반영-----------------------------------------------------*/
          /*  sbSQL.append(" select  etc1                                             \n")
                 .append("     ,   etc2                                             \n")
                 .append(" from    tz_student                                       \n")
                 .append(" where   subj    = " + SQLString.Format(p_subj   ) + "    \n")
                 .append(" and     year    = " + SQLString.Format(p_year   ) + "    \n")
                 .append(" and     subjseq = " + SQLString.Format(p_subjseq) + "    \n")
                 .append(" and     userid  = " + SQLString.Format(p_userid ) + "    \n");*/
        	
        	/*
	        	sbSQL.append(" select                                       \n")  
		        	.append(" t.etc1                                        \n")  
		        	.append(" ,case when t.etc1 >= 10 then 10               \n")  
		        	.append("       when t.etc1 = 9 then 9                  \n")  
		        	.append("       when t.etc1 = 8 then 8                  \n")  
		        	.append("       when t.etc1 = 7 then 7                  \n")  
		        	.append("       when t.etc1 = 6 then 6                  \n")  
		        	.append("       when t.etc1 = 5 then 5                  \n")  
		        	.append("       when t.etc1 = 4 then 5 else 0 end etc2  \n")  
		        	.append(" from(                                         \n")  
		        	.append(" select count(0) as etc1                       \n")  
		        	.append(" from tz_attendance                            \n")  
		        	.append(" where                                         \n")  
		        	.append(" subj =" + SQLString.Format(p_subj) + "         \n")  
		        	.append(" and year=" + SQLString.Format(p_year) + "      \n")  
		        	.append(" and subjseq=" + SQLString.Format(p_subjseq) + "   \n")  
		        	.append(" and userid=" + SQLString.Format(p_userid ) + "    \n")  
		        	.append(" and isattend='O'                              \n")  
		        	.append(" ) t                                           \n"); */
	        	
	        	sbSQL.append(" select                                                               \n")
		        	.append(" y.etc1                                                                    \n")
		        	.append(" ,case when y.times >= 60 and y.etc1 >= 10 then 100                         \n")
		        	.append("       when y.times >= 60 and y.etc1 = 9 then 90                            \n")
		        	.append("       when y.times >= 60 and y.etc1 = 8 then 80                            \n")
		        	.append("       when y.times >= 60 and y.etc1 = 7 then 70                            \n")
		        	.append("       when y.times >= 60 and y.etc1 = 6 then 60                            \n")
		        	.append("       when y.times >= 60 and y.etc1 = 5 then 50                            \n")
		        	.append("       when y.times >= 60 and y.etc1 = 4 then 40                            \n")
		        	.append("       when y.times < 60  and y.etc1 >= 5 then 100                          \n")
		        	.append("       when y.times < 60  and y.etc1 = 4 then 90                           \n")
		        	.append("       when y.times < 60  and y.etc1 = 3 then 80                           \n")
		        	.append("       else 0 end etc2                                                     \n")
		        	.append(" from                                                                      \n")
		        	.append(" (                                                                         \n")
		        	.append("  select                                                                   \n")
		        	.append(" t.etc1                                                                    \n")
		        	.append(" ,(select nvl(edutimes,0) from tz_subj where subj=" + SQLString.Format(p_subj) + ") as times    \n")
		        	.append(" from(                                                                     \n")
		        	.append(" select count(0) as etc1                                                   \n")
		        	.append(" from tz_attendance a, tz_student b  ,vz_scsubjseq c                                    \n")
		        	.append(" where                                                                     \n")
		        	.append(" a.subj =" + SQLString.Format(p_subj) + "                                    \n")
		        	.append(" and a.year=" + SQLString.Format(p_year) + "                                 \n")
		        	.append(" and a.subjseq=" + SQLString.Format(p_subjseq) + "                           \n")
		        	.append(" and a.userid=" + SQLString.Format(p_userid ) + "                            \n")
		        	.append(" and a.isattend='O'                                                          \n")
		        	.append(" and a.subj=b.subj                                                         \n")
		        	.append(" and a.year=b.year                                                         \n")
		        	.append(" and a.userid=b.userid                                                         \n")
		        	.append(" and a.subj=c.subj                                                       \n")    
		        	.append(" and a.year=c.year   						  \n")  
		        	.append(" and a.subjseq=c.subjseq  					\n") 
		        	.append(" and  a.attdate between  substr(c.EDUSTART,0,8) and substr(c.EDUEND,0,8)   \n")
		        	.append(" ) t) y                                                                    \n");
                 
   
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_etc1  = ls.getDouble("etc1");
                v_etc2  = ls.getDouble("etc2");
            }

            data.setEtc1(v_etc1);
            data.setEtc2(v_etc2);

            data.setAvetc1((double)Math.round(v_etc1*data.getWetc1() )/100);
            data.setAvetc2((double)Math.round(v_etc2*data.getWetc2() )/100);
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
        }

        return isOk;
    }
		

    /**
    수료여부 계산
    @param  box      receive from the form object
    @return int
    */
    public int calc_isgraduated(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls1             = null;
        ListSet         ls2             = null;
        StringBuffer    sbSQL           = new StringBuffer("");
        int             isOk            = 0;
        
        int             iSysAdd         = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        SubjseqData     subjseqdata     = null;
        String          v_notgraducd    = "";

        //int             v_cnt1          = 0;
        //int             v_cnt2          = 0;
        
        String  sql = "";
        ListSet ls = null;
        int     v_cnt = 0;

        try { 
            /*-------- Calc  Final Grade  ----------------------------------------------------*/
           /* data.setScore( data.getAvtstep() + data.getAvmtest () + data.getAvftest() 
                         + data.getAvhtest() + data.getAvreport() + data.getAvact  ()   
                         + data.getAvetc1 ()  + data.getAvetc2 () );*/
         
            data.setScore( data.getAvmtest () + data.getAvftest() 
                    + data.getAvhtest() + data.getAvreport() + data.getAvact  ()   
                    + data.getAvetc2 () );
        	
        	
    
            if ( data.getScore() > 100 ) { 
                data.setScore(100);
            }
        
            // 과목기수 정보
            subjseqdata     = getSubjseqInfo(connMgr, p_subj, p_year, p_subjseq);
            
            // 미수료사유 코드(tz_code 의 gubun = '0028')
            // 총점 체크
            if ( data.getScore() < subjseqdata.getGradscore() ) { 
                v_notgraducd += "06,"; // 06 = 성적미달   - 총점점수 체크
            }
            
            // 진도율 체크
          //  if ( data.getTstep() < subjseqdata.getGradstep() ) { 
           //     v_notgraducd += "01,"; // 01 = 진도미달 - 자동계산(진도율 체크)
           // }
            
         

            // 2008.12.13 수정
            // 평가 제출여부(05)
            if ("N".equals( getSubmitExamYn(connMgr, p_subj, p_year, p_subjseq, p_userid) )) {
            	v_notgraducd += "05,"; // 05 = 평가 미제출
            }
            
            // 리포트 제출여부(03)
            //if ("N".equals( getSubmitReportYn(connMgr, p_subj, p_year, p_subjseq, p_userid) )) {
            	//v_notgraducd += "03,"; // 03 = 리포트 미제출
          // }
            
            // 평가
            if ( data.getMtest() < subjseqdata.getGradexam() ) { 
                v_notgraducd += "07,"; // 07 = 평가점수미달
            }

            if ( data.getHtest() < subjseqdata.getGradhtest() ) { 
                v_notgraducd += "07,"; // 07 = 평가점수미달
            }

            if ( data.getFtest() < subjseqdata.getGradftest() ) { 
                v_notgraducd += "07,"; // 07 = 평가점수미달
            }
            
            // 리포트
            if ( data.getReport() < subjseqdata.getGradreport() ) { 
                v_notgraducd += "08,"; // 08 = 리포트점수미달
            }
            
            //참여도(출석점수)
            if ( data.getEtc2() < subjseqdata.getWetc1() || data.getEtc2()==0 ) { 
                v_notgraducd += "13,"; // 13 = 출석점수미달
            }
            
            // 2008.12.13 수정
            // 이러닝과정 - 수료기준(접속횟수, 학습시간)에 부합하는지 확인
            if (subjseqdata.getIsonoff().equals("ON")) {
            	
            
            	
            	// 학습횟수(09)
            	if ( "N".equals(getStudyCountYn(connMgr, p_subj, p_year, p_subjseq, p_userid, subjseqdata))) {
            		v_notgraducd += "09,"; // 09 = 학습횟수미달
            	}

            	// 접속시간(12)
            	if ( "N".equals(getStudyTimeYn(connMgr, p_subj, p_year, p_subjseq, p_userid, subjseqdata))) {
            		v_notgraducd += "12,"; // 12 = 접속시간미달
            	}
            }

            if ( !v_notgraducd.equals("") ) { 
                v_notgraducd = v_notgraducd.substring(0,v_notgraducd.length()-1);
                
            }
            
            data.setNotgraducd(v_notgraducd);
            

// data.getMtest() --  subjseqdata.getGradexam() 중간
// data.getHtest() --  subjseqdata.getGradhtest() 형성
// data.getFtest() --  subjseqdata.getGradftest() 최종
            
            if (    data.getScore   () >= subjseqdata.getGradscore  ()                                                                               
                    &&  data.getTstep   () >= subjseqdata.getGradstep   ()                                                                                
                    &&  v_notgraducd.length() == 0                                                                             
                    &&  ( 
                    		((subjseqdata.getIsonoff().equals("ON") || subjseqdata.getIsonoff().equals("RC")) && subjseqdata.getGradexam() <= data.getMtest()) // && data.getGradexamcnt()    > 0
                    		|| (subjseqdata.getIsonoff().equals("OFF"))
                        )                                                                                                                                 
                                                                                                                                 
                    &&  (       
                    		(   ((subjseqdata.getIsonoff().equals("ON") || subjseqdata.getIsonoff().equals("RC")) && subjseqdata.getGradftest() <= data.getFtest() ) //&& data.getGradftestcnt()  > 0
                                 || (subjseqdata.getIsonoff().equals("OFF") && subjseqdata.getGradftest() <= data.getFtest() ) //&& data.getFtest() > 0
                            )  
                                                                              
                        )                                                                                                                                 
                    &&  (       
                    		(   ((subjseqdata.getIsonoff().equals("ON") || subjseqdata.getIsonoff().equals("RC"))  && subjseqdata.getGradreport() <= data.getReport() ) //&& data.getGradreportcnt()> 0
                                 || (subjseqdata.getIsonoff().equals("OFF") && subjseqdata.getGradreport() <= data.getReport() ) //&& data.getReport()       > 0
                             )
                                                                               
                        )     
                   &&  (       
                		(   ((subjseqdata.getIsonoff().equals("ON") || subjseqdata.getIsonoff().equals("RC"))  && subjseqdata.getWetc1() <= data.getEtc2() && data.getEtc2()!=0 ) //&& data.getGradreportcnt()> 0
                             || (subjseqdata.getIsonoff().equals("OFF") && subjseqdata.getWetc1() <= data.getEtc2() ) //&& data.getReport()       > 0
                         ) //출석점수
                                                                           
                     )     
                   ) { // 전체 조건에 맞으면 수료                                                                                                         
                data.setIsgraduated("Y");
            
        
            
            } else { 
                data.setIsgraduated("N"); //미수료 시 U로 한다.
            }
            
            // 기타 조건으로 미수료
            if ( StringManager.chkNull(data.getIsgraduated()).equals("")) { 
                data.setIsgraduated("N");
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
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
        }

        return isOk;
    }


    
    // isOk = 1 : "에러가 없는 경우"
    // isOk = 2 : "이미 수료처리 되었습니다."
    // isOk = 3 : 기타 에러
    // isOk = 4 : calc_score() 계산에서 에러
    public int CourseScoreCompute(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement pstmt_select = null;
        PreparedStatement pstmt_update = null;
        PreparedStatement pstmt_insert = null;

        int isOk = 1;
        String v_course    = box.getString("p_course");
        String v_cyear     = box.getString("p_cyear");
        String v_courseseq = box.getString("p_courseseq");
        String v_luserid   = box.getSession("userid");

        Hashtable  courseseqdata = null;
        int v_gradscore   = 0;
        int v_gradfailcnt = 0;
        int v_subjcnt     = 1;

        ArrayList list  = null;   // 수료대상학생 정보 커서
        CourseScoreData coursedata = null;
        SubjScoreData   subjdata   = null;

        double v_score = 0;
        int    v_graduatedcnt = 0;

        boolean v_isexception = false;
        String v_return_msg = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료처리 완료여부, 학습중 검토
            courseseqdata = getCourseseqInfo(connMgr, v_course, v_cyear, v_courseseq);
            v_gradscore   = Integer.valueOf((String)courseseqdata.get("gradscore")).intValue();
            v_gradfailcnt = Integer.valueOf((String)courseseqdata.get("gradfailcnt")).intValue();
            v_subjcnt     = Integer.valueOf((String)courseseqdata.get("subjcnt")).intValue();

            // 학습자 커서
            list = getCourseFinishTargetStudent(connMgr, v_course, v_cyear, v_courseseq, "ALL");
            for ( int i = 0; i<list.size(); i++ ) { 
                if ( i == 0 ) { 
                    // pstmt 만든다.
                    pstmt_select = getPreparedStatement(connMgr, "COURSE_STOLD", "select");
                    pstmt_update = getPreparedStatement(connMgr, "COURSE_STOLD", "update");
                    pstmt_insert = getPreparedStatement(connMgr, "COURSE_STOLD", "insert");
                }

                coursedata  = (CourseScoreData)list.get(i);
                v_score = 0;
                v_graduatedcnt = 0;
                for ( int k=0; k<coursedata.size(); k++ ) { 
                    subjdata = (SubjScoreData)coursedata.get(k);
                    if ( subjdata.getIsgraduated().equals("Y") ) { 
                        v_graduatedcnt++;
                    }
                    v_score += subjdata.getScore();
                }
                coursedata.setScore(v_score/v_subjcnt);
                coursedata.setGraduatedcnt(v_graduatedcnt);

                if ( v_subjcnt - v_graduatedcnt > v_gradfailcnt) { 
                    coursedata.setIsgraduated("N");
                } else { 
                    coursedata.setIsgraduated("Y");
                }

                // 점수 재계산
                try { 
                    v_return_msg = "";
                    calc_course_score(pstmt_select, pstmt_update, pstmt_insert,  coursedata, v_luserid);
                } catch  ( Exception ex ) { 
                    v_isexception = true;
                    v_return_msg = "패키지 수료처리 == > 점수 재산정 중 문제발생함[" + coursedata.getUserid() + "]";
                    v_return_msg += ex.getMessage();
                    isOk = 4;
                    return isOk;
                }
            }
        }
        catch ( Exception ex ) { 
            v_isexception = true;
            isOk = 3;
            v_return_msg = ex.getMessage();
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( !v_return_msg.equals(""))
                box.put("p_return_msg", v_return_msg);

            if ( pstmt_select != null ) { try { pstmt_select.close(); } catch ( Exception e ) { } }
            if ( pstmt_update != null ) { try { pstmt_update.close(); } catch ( Exception e ) { } }
            if ( pstmt_insert != null ) { try { pstmt_insert.close(); } catch ( Exception e ) { } }

            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e10 ) { }
            }
        }
        return isOk;
    }
		
    public Hashtable getCourseseqInfo(DBConnectionManager connMgr, String p_course, String p_cyear, String p_courseseq) throws Exception { 
        ListSet             ls      = null;
        String              sql     = "";
        Hashtable outputdata = new Hashtable();


        try { 
            // 과목기수 정보
            sql = " select gradscore, gradfailcnt ";
            sql += "   from tz_courseseq ";
            sql += "  where course    = " + SQLString.Format(p_course);
            sql += "    and cyear     = " + SQLString.Format(p_cyear);
            sql += "    and courseseq = " + SQLString.Format(p_courseseq);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                outputdata.put("gradscore",   String.valueOf( ls.getInt("gradscore")));
                outputdata.put("gradfailcnt", String.valueOf( ls.getInt("gradfailcnt")));
            }

            sql = " select count(*) subjcnt ";
            sql += "   from tz_coursesubj ";
            sql += "  where course    = " + SQLString.Format(p_course);

            ls.close();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                outputdata.put("subjcnt", String.valueOf( ls.getInt("subjcnt")));
            }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return outputdata;
    }


	/**
    학습자 커서 (CourseScoreCompute() 에서 쓰임.)
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList getCourseFinishTargetStudent(DBConnectionManager connMgr, String p_course, String p_cyear, String p_courseseq, String p_userid) throws Exception { 
        ListSet ls =  null;
        String              sql     = "";
        ArrayList list = new ArrayList();
        CourseScoreData coursedata = null;
        SubjScoreData   subjdata   = null;
        String   v_user_bef = "";

        // 학습자 커서
        sql = " select a.course, a.cyear, a.courseseq, b.subj, b.year, b.subjseq, ";
       sql += "        b.userid, b.score, b.isgraduated ";
        sql += "   from tz_subjseq  a, ";
        sql += "        tz_stold    b  ";
        sql += "  where a.subj      = b.subj  ";
        sql += "    and a.year      = b.year  ";
        sql += "    and a.subjseq   = b.subjseq  ";
        sql += "    and a.course    = " + SQLString.Format(p_course);
        sql += "    and a.cyear     = " + SQLString.Format(p_cyear);
        sql += "    and a.courseseq = " + SQLString.Format(p_courseseq);
        if ( !p_userid.equals("ALL") ) { 
            sql += "    and b.userid = " + SQLString.Format(p_userid);
        }
        sql += "  order by b.userid, b.subj, b.year, b.subjseq ";

        try { 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                if ( !v_user_bef.equals( ls.getString("userid"))) { 
                    coursedata = new CourseScoreData();
                    coursedata.setCourse( ls.getString("course") );
                    coursedata.setCyear( ls.getString("cyear") );
                    coursedata.setCourseseq( ls.getString("courseseq") );
                    coursedata.setUserid( ls.getString("userid") );
                }
                subjdata = new SubjScoreData();
                subjdata.setSubj( ls.getString("subj") );
                subjdata.setYear( ls.getString("year") );
                subjdata.setSubjseq( ls.getString("subjseq") );
                subjdata.setUserid(coursedata.getUserid() );
                subjdata.setScore( ls.getDouble("score") );
                subjdata.setIsgraduated( ls.getString("isgraduated") );

                coursedata.add(subjdata);

                if ( !v_user_bef.equals(coursedata.getUserid() )) { 
                    list.add(coursedata);
                    v_user_bef = coursedata.getUserid();
                }
            }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return list;
    }
                                         
	/**
    sql문 작성 
    @param p_gubun         
    @param p_command         
    @return PreparedStatement
    */                                        
    public PreparedStatement getPreparedStatement(DBConnectionManager connMgr, String p_gubun, String p_command) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        if ( p_gubun.equals("COURSE_STOLD") ) { 
            if ( p_command.equals("select") ) { 
                sql = " select count(*) cnt ";
                sql += "   from tz_coursestold ";
                sql += "  where course    = ? ";
                sql += "    and cyear     = ? ";
                sql += "    and courseseq = ? ";
                sql += "    and userid    = ? ";
            } else if ( p_command.equals("update") ) { 
                sql = " update tz_coursestold ";
                sql += "    set gradudatedcnt = ?, ";
                sql += "        score         = ?, ";
                sql += "        isgraduated   = ?, ";
                sql += "        luserid       = ?, ";
                sql += "        ldate         = ?  ";
                sql += "  where course        = ?  ";
                sql += "    and cyear         = ?  ";
                sql += "    and courseseq     = ?  ";
                sql += "    and userid        = ?  ";
            } else if ( p_command.equals("insert") ) { 
                sql = " insert into tz_coursestold ";
                sql += "  (course,        cyear, courseseq,   userid, ";
                sql += "   gradudatedcnt, score, isgraduated ,indate, ";
                sql += "   luserid, ldate) ";
                sql += "  values  ";
                sql += "  (?,             ?,     ?,           ?, ";
                sql += "   ?,             ?,     ? ,          ?, ";
                sql += "   ?,             ?) ";
            }
        } else if ( p_gubun.equals("SUBJECT_STOLD") ) { 
            if ( p_command.equals("select") ) { 
                sql = " select count(*) cnt ";
                sql += "   from tz_stold ";
                sql += "  where subj      = ? ";
                sql += "    and year      = ? ";
                sql += "    and subjseq   = ? ";
                sql += "    and userid    = ? ";
            } else if ( p_command.equals("update") ) { 
                sql = " update tz_stold ";
                sql += "    set name     = ?, ";
                sql += "        comp     = ?, ";
                sql += "        score    = ?, ";
                sql += "        tstep    = ?, ";
             //   sql += "        mtest    = ?, ";
                sql += "        ftest    = ?, ";
                sql += "        report   = ?, ";
                sql += "        act      = ?, ";
                sql += "        etc1     = ?, ";
                sql += "        etc2     = ?, ";
                sql += "        avtstep  = ?, ";
                sql += "        avmtest  = ?, ";
                sql += "        avftest  = ?, ";
                sql += "        avreport = ?, ";
                sql += "        avact    = ?, ";
                sql += "        avetc1   = ?, ";
                sql += "        avetc2   = ?, ";
                sql += "        isgraduated = ?, ";
                sql += "        notgraducd = ?, notgraduetc='',";
                sql += "        isb2c    = ?, ";
                sql += "        edustart = ?, ";
                sql += "        eduend   = ?, ";
                sql += "        serno    = ?, ";
                sql += "        isrestudy= ?, ";
                sql += "        luserid  = ?, ";
                sql += "        ldate    = ? ";
                sql += "  where subj     = ? ";
                sql += "    and year     = ? ";
                sql += "    and subjseq  = ? ";
                sql += "    and userid   = ? ";
            } else if ( p_command.equals("insert") ) { 
                sql = " insert into tz_stold ";
                sql += "  ( subj       , year       , subjseq   , userid ";
                sql += "  , name       , comp       , score     , tstep  	, mtest ";
                sql += "  , ftest      , report     , act       , etc1 ";
                sql += "  , etc2       , avtstep    , avmtest   , avftest ";
                sql += "  , avreport   , avact      , avetc1    , avetc2 ";
                sql += "  , isgraduated, notgraducd , isb2c     , edustart	, eduend ";
                sql += "  , serno      , isrestudy  , luserid   , ldate ) ";    
                sql += " values ";
                sql += "  ( ?, ?, ?, ? ";
                sql += "  , ?, ?, ?, ?, ? ";
                sql += "  , ?, ?, ?, ? ";
                sql += "  , ?, ?, ?, ? ";
                sql += "  , ?, ?, ?, ? ";
                sql += "  , ?, ?, ?, ?, ? ";
                sql += "  , ?, ?, ?, ? ) ";  
            }
        } else if ( p_gubun.equals("SUBJECT_STUDENT") ) { 
            if ( p_command.equals("update") ) { 
                sql = " update tz_student ";
                sql += "    set score   = ?, ";
                sql += "        tstep   = ?, ";
             //   sql += "        mtest   = ?, ";
                sql += "        ftest   = ?, ";
                sql += "        report  = ?, ";
                sql += "        act     = ?, ";
                sql += "        etc1    = ?, ";
                sql += "        etc2    = ?, ";
                sql += "        avtstep = ?, ";
                sql += "        avmtest = ?, ";
                sql += "        avftest = ?, ";
                sql += "        avreport= ?, ";
                sql += "        avact   = ?, ";
                sql += "        avetc1  = ?, ";
                sql += "        avetc2  = ?, ";
                sql += "        isgraduated= ?, ";
                sql += "        luserid = ?, ";
                sql += "        ldate   = ?, ";
                sql += "        serno   = ? ";
                sql += "  where subj    = ? ";
                sql += "    and year    = ? ";
                sql += "    and subjseq = ? ";
                sql += "    and userid  = ? ";
            }
        } else if ( p_gubun.equals("MEMBER") ) { 
            if ( p_command.equals("select") ) { 
                sql = " select name, comp";
                sql += "   from tz_member ";
                sql += "  where userid = ? ";
            }
        } 
     
        
//System.out.println("sql == > " + sql);

        try { 
            pstmt = connMgr.prepareStatement(sql);
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
        }
        return pstmt;
    }

    public int calc_course_score(PreparedStatement pstmt_select, PreparedStatement pstmt_update, PreparedStatement pstmt_insert,
                                 CourseScoreData coursedata, String p_luserid) throws Exception { 

        ResultSet rs = null;
        int isOk = 0;

        int v_count = 0;

        try { 
            pstmt_select.setString( 1, coursedata.getCourse() );
            pstmt_select.setString( 2, coursedata.getCyear() );
            pstmt_select.setString( 3, coursedata.getCourseseq() );
            pstmt_select.setString( 4, coursedata.getUserid() );

            rs =  pstmt_select.executeQuery();
            while ( rs.next() ) { 
                v_count = rs.getInt("cnt");
            }

            if ( v_count > 0 ) {  // udpate
                pstmt_update.setInt   ( 1, coursedata.getGraduatedcnt() );
                pstmt_update.setDouble( 2, coursedata.getScore() );
                pstmt_update.setString( 3, coursedata.getIsgraduated() );
                pstmt_update.setString( 4, p_luserid);
                pstmt_update.setString( 5, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt_update.setString( 6, coursedata.getCourse() );
                pstmt_update.setString( 7, coursedata.getCyear() );
                pstmt_update.setString( 8, coursedata.getCourseseq() );
                pstmt_update.setString( 9, coursedata.getUserid() );

                isOk = pstmt_update.executeUpdate();
            } else if ( v_count == 0 ) {  // insert
                pstmt_insert.setString( 1, coursedata.getCourse() );
                pstmt_insert.setString( 2, coursedata.getCyear() );
                pstmt_insert.setString( 3, coursedata.getCourseseq() );
                pstmt_insert.setString( 4, coursedata.getUserid() );
                pstmt_insert.setInt   ( 5, coursedata.getGraduatedcnt() );
                pstmt_insert.setDouble( 6, coursedata.getScore() );
                pstmt_insert.setString( 7, coursedata.getIsgraduated() );
                pstmt_insert.setString( 8, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt_insert.setString( 9, p_luserid);
                pstmt_insert.setString(10, FormatDate.getDate("yyyyMMddHHmmss") );

                isOk = pstmt_insert.executeUpdate();
            }
        }
        catch ( Exception ex ) { 
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
        }
        return isOk;
    }

    /**
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectOffCompleteList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
        SubjseqData data = null;

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        
        String  v_orderColumn   = box.getString("p_orderColumn");           	// 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");           		// 정렬할 순서


        try { 
            connMgr = new DBConnectionManager();
            list = getOffCompleteList(connMgr, v_subj, v_year, v_subjseq, v_orderColumn, v_orderType);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }

    public ArrayList getOffCompleteList(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String v_orderColumn, String v_orderType) throws Exception { 
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        StoldData data = null;

        try { 
            sql = "select a.subj,  a.year, a.subjseq, a.userid, ";
            sql += "       b.comp,   ";
            sql += "       b.jikup, "; 
            sql += "       b.name,  ";
            sql += "       a.tstep, a.mtest, a.ftest, a.report, a.htest, ";
            sql += "       a.etc1,  a.etc2,  a.score, ";
            sql += "       (select isgraduated from tz_stold where subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid) isgraduated,   ";
            sql += "       (select notgraducd from tz_stold where subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid) notgraducd,   ";
            sql += "       (select notgraduetc from tz_stold where subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid) notgraduetc,   ";
            sql += "	   	  (select codenm from tz_code where gubun='0028' and code=(select notgraduetc from tz_stold where subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid)) notgraduetcdesc ";
            sql += "  from tz_student  a,  ";
            sql += "        tz_member   b  ";
            sql += " where a.userid  = b.userid ";
            sql += "   and a.subj    = " + SQLString.Format(p_subj);
            sql += "   and a.year    = " + SQLString.Format(p_year);
            sql += "   and a.subjseq = " + SQLString.Format(p_subjseq);
            
            if ( v_orderColumn.equals("") ) { 
    			sql += " order by b.name  ";
			} else { 
			    sql += " order by " + v_orderColumn + v_orderType;
			}


            

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                data = new StoldData();

                data.setSubj   ( ls.getString("subj") );
                data.setYear   ( ls.getString("year") );
                data.setSubjseq( ls.getString("subjseq") );
                data.setUserid ( ls.getString("userid") );
                
                data.setComp   ( ls.getString("comp") );
//                data.setCompnm ( ls.getString("compnm") );
                data.setName   ( ls.getString("name") );
                data.setTstep  ( ls.getDouble("tstep") );
                data.setMtest  ( ls.getDouble("mtest") );
                data.setFtest  ( ls.getDouble("ftest") );
                data.setHtest  ( ls.getDouble("htest") );
                data.setReport ( ls.getDouble("report") );
                data.setEtc1   ( ls.getDouble("etc1") );
                data.setEtc1   ( ls.getDouble("etc1") );
                data.setScore  ( ls.getDouble("score") );
                data.setIsgraduated( ls.getString("isgraduated") );
                data.setNotgraducd( ls.getString("notgraducd") );
                data.setNotgraduetc( ls.getString("notgraduetc") );
                data.setNotgraduetcdesc( ls.getString("notgraduetcdesc") );
				data.setNotgraducddesc(getNotGraduCodeName(connMgr, ls.getString("notgraducd")));

                list.add(data);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return list;
    }

    public SubjseqData SelectSubjseqInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
        SubjseqData data = null;

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");

        try { 
            connMgr = new DBConnectionManager();
            data = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return data;
    }
    
    
	/**
    수료처리 상세화면 인포정보
    @param box          receive from the form object and session
    @return ArrayList
    */    
    public DataBox SelectSubjseqInfoDbox(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr     = null;
        ArrayList           list        = null;
		DataBox             dbox        = null;
        String              v_subj      = box.getString("p_subj"    );
        String              v_year      = box.getString("p_year"    );
        String              v_subjseq   = box.getString("p_subjseq" );

        try { 
            connMgr = new DBConnectionManager();
            dbox    = getSubjseqInfoDbox(connMgr, v_subj, v_year, v_subjseq);
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }
        return dbox;
    }
    
	/**
	집합과목 수료처리
	@param box      receive from the form object
	@return int
	*/		
    public int OffSubjectCompleteNew(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt_select = null;
        PreparedStatement pstmt_update = null;
        PreparedStatement pstmt_insert = null;
        PreparedStatement pstmt_update_student = null;
        PreparedStatement pstmt_select_member  = null;
        ResultSet rs = null;
        
        ListSet             ls      = null;  
        String              sql     = "";

        // 수료처리시 필요한 기본 변수들
        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_value1  = box.getString("p_value");        // String with userids
        String v_value2  = "";
        // String v_isclosed= box.getStringDefault("p_isclosed", "Y");
        String v_isclosed = "Y";
        String v_userid  = "";
        String v_luserid = box.getSession("userid");

        double v_tstep   = 0;
        double v_mtest   = 0;
        double v_htest   = 0;
        double v_ftest   = 0;
        double v_report  = 0;
        double v_etc1    = 0;
        double v_etc2    = 0;
        String v_isgoyong= "";        
        double v_samtotal= 0;  // 삼진아웃

        StringTokenizer st1 = null;
        StringTokenizer st2 = null;

        StoldData   data = new StoldData();
        SubjseqData subjseqdata = null;
		String v_currdate = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년 +월 +일 +시
        int isOk = 1;
        boolean v_isexception = false;
        String   v_return_msg  = "";
//      1 : "정상적으로 수료처리 되었습니다."
//      2 : "이미 수료처리 되었습니다."
//      3 : "과목시작후 가능합니다.
//      4 : "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]"
//      5 : "학습자의 리포트 중 " + String.valueOf(v_remainReportcnt) + "' 개가 미채점되었습니다. \n\n수료처리취소후 점검하여 주십시오";
//      "excaption 발생"

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료처리 완료여부, 학습중 검토
            subjseqdata             = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            if ( subjseqdata.getIsclosed().equals("Y") ) { 
                v_return_msg    = "이미 수료처리 되었습니다.";
                isOk            = 2;
                return isOk;
            // } else if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEduend() ))) { 
            } else if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEdustart() ))) { 
                // v_return_msg  = "과목종료후 가능합니다.";
                v_return_msg    = "학습시작후 가능합니다.";
                isOk            = 3;
                return isOk;
            }
            
            // 수료 정보 삭제
            deleteStoldTable( connMgr, v_subj, v_year, v_subjseq );
            
            data.setSubj        (v_subj                     );
            data.setYear        (v_year                     );
            data.setSubjseq     (v_subjseq                  );
            data.setWstep       (subjseqdata.getWstep()     );
            data.setWmtest      (subjseqdata.getWmtest()    );
            data.setWftest      (subjseqdata.getWftest()    );
            data.setWhtest      (subjseqdata.getWhtest()    );
            data.setWreport     (subjseqdata.getWreport()   );
            data.setWact        (subjseqdata.getWact()      );
            data.setWetc1       (subjseqdata.getWetc1()     );
            data.setWetc2       (subjseqdata.getWetc2()     );
            data.setGradscore   (subjseqdata.getGradscore() );
            data.setGradstep    (subjseqdata.getGradstep()  );
            data.setGradexam    (subjseqdata.getGradexam()  );
            data.setGradreport  (subjseqdata.getGradreport());
            data.setEdustart    (subjseqdata.getEdustart()  );
            data.setEduend      (subjseqdata.getEduend()    );
            
            pstmt_update_student    = getPreparedStatement(connMgr, "SUBJECT_STUDENT"   , "update");
            pstmt_select            = getPreparedStatement(connMgr, "SUBJECT_STOLD"     , "select");
            pstmt_update            = getPreparedStatement(connMgr, "SUBJECT_STOLD"     , "update");
            pstmt_insert            = getPreparedStatement(connMgr, "SUBJECT_STOLD"     , "insert");
            pstmt_select_member     = getPreparedStatement(connMgr, "MEMBER"            , "select");

            sql                     = " SELECT  userid                                                  \n"
                                    + "     ,   NVL(tstep , 0) tstep                                    \n"
                                    + "     ,   NVL(ftest , 0) ftest                                    \n"
                                    + "     ,   NVL(mtest , 0) mtest                                    \n"
                                    + "     ,   NVL(htest , 0) htest                                    \n"
                                    + "     ,   NVL(report, 0) report                                   \n"
                                    + "     ,   NVL(etc1  , 0) etc1                                     \n"
                                    + "     ,   NVL(etc2  , 0) etc2                                     \n"
                                    + " FROM    tz_student                                              \n"
                                    + " WHERE   subj        = " + SQLString.Format(v_subj       )   + " \n"
                                    + " AND     year        = " + SQLString.Format(v_year       )   + " \n"
                                    + " AND     subjseq     = " + SQLString.Format(v_subjseq    )   + " \n";
            
            ls                      = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                v_userid = "";
                v_etc2   = 0;
                v_etc1   = 0;
                v_report = 0;
                v_ftest  = 0;
                v_mtest  = 0;
                v_tstep  = 0;

                v_userid = ls.getString("userid");
                v_tstep  = ls.getDouble("tstep" );
                v_ftest  = ls.getDouble("ftest" );
                v_htest  = ls.getDouble("htest" );
                v_mtest  = ls.getDouble("mtest" );
                v_report = ls.getDouble("report");
                v_etc1   = ls.getDouble("etc1"  );
                v_etc2   = ls.getDouble("etc2"  );

                data.setUserid  (v_userid   );
                data.setScore   (0          );
                data.setTstep   (v_tstep    );
                data.setMtest   (v_mtest    );
                data.setFtest   (v_ftest    );
                data.setHtest   (v_htest    );
                data.setReport  (v_report   );
                data.setAct     (0          );
                data.setEtc1    (v_etc1     );
                data.setEtc2    (v_etc2     );
                data.setAvtstep (0          );
                data.setAvmtest (0          );
                data.setAvftest (0          );
                data.setAvreport(0          );
                data.setAvact   (0          );
                data.setAvetc1  (0          );
                data.setAvetc2  (0          );

                data.setIsgraduated("Y");
                
                // 삼진아웃 시 미수료
                /*
                if ( v_samtotal > 2) { 
                    v_isexception = true;
                    v_return_msg = v_userid + " 학습자는 삼진아웃 상태입니다. "; 
                    isOk = 6;
                    return isOk;                    
                }*/
                                                
                data.setIsgraduated("Y");

                pstmt_select_member.setString( 1, data.getUserid() );

                rs =  pstmt_select_member.executeQuery();

                while ( rs.next() ) { 
                    data.setName(rs.getString("name") );
                    data.setComp(rs.getString("comp") );
                    //data.setJik(rs.getString("jikwi") );
                }

                // SCORE 계산
                calc_offsubj_score(connMgr, data);

                // 1.tz_student update
                // 2.tz_stold update or insert
                update_offsubj_score(connMgr, pstmt_update_student, pstmt_select, pstmt_insert, pstmt_update, data, v_luserid);
                
/*
                if ( !subjseqdata.getIsBlended().equals("Y") ) {                    
                    if ( data.getIsgraduated().equals("N") ) {
                        MileageManager.insertMileage(connMgr, "00000000000000000006", v_userid);
                    } else {
                        if ( data.getScore() >= 100 )
                            MileageManager.insertMileage(connMgr, "00000000000000000005", v_userid);
                        else
                            MileageManager.insertMileage(connMgr, "00000000000000000004", v_userid);
                    }
                } 
*/    
            } // while end
            
            // 수료필드 업데이트
            setCloseColumn(connMgr, v_subj, v_year, v_subjseq, v_isclosed);
        }
        catch ( Exception ex ) { ex.printStackTrace();
            isOk = 100;
            v_isexception = true;
            v_return_msg = ex.getMessage();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            try { 
                if ( !v_return_msg.equals("")) box.put("p_return_msg", v_return_msg);
            } catch ( Exception e ) { e.printStackTrace(); }

			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt_select != null ) { try { pstmt_select.close(); } catch ( Exception e ) { } }
            if ( pstmt_update != null ) { try { pstmt_update.close(); } catch ( Exception e ) { } }
            if ( pstmt_insert != null ) { try { pstmt_insert.close(); } catch ( Exception e ) { } }
            if ( pstmt_update_student != null ) { try { pstmt_update_student.close(); } catch ( Exception e ) { } }
            if ( pstmt_select_member != null ) { try { pstmt_select_member.close(); } catch ( Exception e ) { } }

            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e10 ) { }
            }
        }
        return isOk;
    }
    
    
    
    /**
    집합과목 수료처리
    @param box      receive from the form object
    @return int
    */      
    public int OffSubjectCompleteNew1(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt_select = null;
        PreparedStatement pstmt_update = null;
        PreparedStatement pstmt_insert = null;
        PreparedStatement pstmt_update_student = null;
        PreparedStatement pstmt_select_member  = null;
        ResultSet rs = null;
        
        ListSet             ls      = null;  
        String              sql     = "";

        // 수료처리시 필요한 기본 변수들
        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_value1  = box.getString("p_value");        // String with userids
        String v_value2  = "";
        // String v_isclosed= box.getStringDefault("p_isclosed", "Y");
        String v_isclosed = "Y";
        String v_userid  = "";
        String v_luserid = box.getSession("userid");

        double v_tstep   = 0;
        double v_mtest   = 0;
        double v_htest   = 0;
        double v_ftest   = 0;
        double v_report  = 0;
        double v_etc1    = 0;
        double v_etc2    = 0;
        String v_isgoyong= "";        
        double v_samtotal= 0;  // 삼지아웃
        
        StringTokenizer st1 = null;
        StringTokenizer st2 = null;

        StoldData   data = new StoldData();
        SubjseqData subjseqdata = null;
        String v_currdate = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년 +월 +일 +시
        int isOk = 1;
        boolean v_isexception = false;
        String   v_return_msg  = "";
//      1 : "정상적으로 수료처리 되었습니다."
//      2 : "이미 수료처리 되었습니다."
//      3 : "과목시작후 가능합니다.
//      4 : "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]"
//      5 : "학습자의 리포트 중 " + String.valueOf(v_remainReportcnt) + "' 개가 미채점되었습니다. \n\n수료처리취소후 점검하여 주십시오";
//      "excaption 발생"

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료처리 완료여부, 학습중 검토
            subjseqdata             = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            
            data.setSubj        (v_subj                     );
            data.setYear        (v_year                     );
            data.setSubjseq     (v_subjseq                  );
            data.setWstep       (subjseqdata.getWstep()     );
            data.setWmtest      (subjseqdata.getWmtest()    );
            data.setWftest      (subjseqdata.getWftest()    );
            data.setWhtest      (subjseqdata.getWhtest()    );
            data.setWreport     (subjseqdata.getWreport()   );
            data.setWact        (subjseqdata.getWact()      );
            data.setWetc1       (subjseqdata.getWetc1()     );
            data.setWetc2       (subjseqdata.getWetc2()     );
            data.setGradscore   (subjseqdata.getGradscore() );
            data.setGradstep    (subjseqdata.getGradstep()  );
            data.setGradexam    (subjseqdata.getGradexam()  );
            data.setGradreport  (subjseqdata.getGradreport());
            data.setEdustart    (subjseqdata.getEdustart()  );
            data.setEduend      (subjseqdata.getEduend()    );

            pstmt_update_student    = getPreparedStatement(connMgr, "SUBJECT_STUDENT"   , "update");
            pstmt_select            = getPreparedStatement(connMgr, "SUBJECT_STOLD"     , "select");
            pstmt_update            = getPreparedStatement(connMgr, "SUBJECT_STOLD"     , "update");
            pstmt_insert            = getPreparedStatement(connMgr, "SUBJECT_STOLD"     , "insert");
            pstmt_select_member     = getPreparedStatement(connMgr, "MEMBER"            , "select");


            sql                     = " select  userid                                                  \n"
                                    + "     ,   nvl(tstep , 0) tstep                                    \n"
                                    + "     ,   nvl(ftest , 0) ftest                                    \n"
                                    + "     ,   nvl(mtest , 0) mtest                                    \n"
                                    + "     ,   nvl(htest , 0) htest                                    \n"
                                    + "     ,   nvl(report, 0) report                                   \n"
                                    + "     ,   nvl(etc1  , 0) etc1                                     \n"
                                    + "     ,   nvl(etc2  , 0) etc2                                     \n"
                                    + " from    tz_student                                              \n"
                                    + " where   subj        = " + SQLString.Format(v_subj       )   + " \n"
                                    + " and     year        = " + SQLString.Format(v_year       )   + " \n"
                                    + " and     subjseq     = " + SQLString.Format(v_subjseq    )   + " \n";
            
            ls                      = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                v_userid = "";
                v_etc2   = 0;
                v_etc1   = 0;
                v_report = 0;
                v_ftest  = 0;
                v_mtest  = 0;
                v_tstep  = 0;
                
                v_userid = ls.getString("userid");
                v_tstep  = ls.getDouble("tstep" );
                v_ftest  = ls.getDouble("ftest" );
                v_htest  = ls.getDouble("htest" );
                v_mtest  = ls.getDouble("mtest" );
                v_report = ls.getDouble("report");
                v_etc1   = ls.getDouble("etc1"  );
                v_etc2   = ls.getDouble("etc2"  );
                    
                data.setUserid  (v_userid   );
                data.setScore   (0          );
                data.setTstep   (v_tstep    );
                data.setMtest   (v_mtest    );
                data.setFtest   (v_ftest    );
                data.setHtest   (v_htest    );
                data.setReport  (v_report   );
                data.setAct     (0          );
                data.setEtc1    (v_etc1     );
                data.setEtc2    (v_etc2     );
                data.setAvtstep (0          );
                data.setAvmtest (0          );
                data.setAvftest (0          );
                data.setAvreport(0          );
                data.setAvact   (0          );
                data.setAvetc1  (0          );
                data.setAvetc2  (0          );
                data.setIsgraduated("Y");
                
                // 삼진아웃 시 미수료
                /*
                if ( v_samtotal > 2) { 
                    v_isexception = true;
                    v_return_msg = v_userid + " 학습자는 삼진아웃 상태입니다. "; 
                    isOk = 6;
                    return isOk;                    
                }*/
                                                
                data.setIsgraduated("Y");

                pstmt_select_member.setString( 1, data.getUserid() );

                rs =  pstmt_select_member.executeQuery();

                while ( rs.next() ) { 
                    data.setName(rs.getString("name") );
                    data.setComp(rs.getString("comp") );
                    //data.setJik(rs.getString("jikwi") );
                }

                // SCORE 계산
                calc_offsubj_score(connMgr, data);

                // 1.tz_student update
                // 2.tz_stold update or insert
                update_offsubj_score(connMgr, pstmt_update_student, pstmt_select, pstmt_insert, pstmt_update, data, v_luserid);
                
                if ( data.getIsgraduated().equals("N") ) {
                    MileageManager.insertMileage(connMgr, "00000000000000000006", v_userid);
                } else {
                    if ( data.getScore() >= 100 )
                        MileageManager.insertMileage(connMgr, "00000000000000000005", v_userid);
                    else
                        MileageManager.insertMileage(connMgr, "00000000000000000004", v_userid);
                }    
            } // while end
            
            // 수료필드 업데이트
            setCloseColumn(connMgr, v_subj, v_year, v_subjseq, v_isclosed);
        }
        catch ( Exception ex ) { ex.printStackTrace();
            isOk = 100;
            v_isexception = true;
            v_return_msg = ex.getMessage();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            try { 
                if ( !v_return_msg.equals("")) box.put("p_return_msg", v_return_msg);
            } catch ( Exception e ) { e.printStackTrace(); }

            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt_select != null ) { try { pstmt_select.close(); } catch ( Exception e ) { } }
            if ( pstmt_update != null ) { try { pstmt_update.close(); } catch ( Exception e ) { } }
            if ( pstmt_insert != null ) { try { pstmt_insert.close(); } catch ( Exception e ) { } }
            if ( pstmt_update_student != null ) { try { pstmt_update_student.close(); } catch ( Exception e ) { } }
            if ( pstmt_select_member != null ) { try { pstmt_select_member.close(); } catch ( Exception e ) { } }

            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e10 ) { }
            }
        }
        return isOk;
    }


	/**
	집합과목 점수재계산
	@param box      receive from the form object
	@return int
	*/		
    public int OffSubjectCompleteRerating(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager	connMgr	                = null;
        
        PreparedStatement   pstmt_select            = null;
        PreparedStatement   pstmt_update            = null;
        PreparedStatement   pstmt_insert            = null;
        PreparedStatement   pstmt_update_student    = null;
        PreparedStatement   pstmt_select_member     = null;
        ResultSet           rs                      = null;
        
        ListSet             ls                      = null;  
        String              sql                     = "";

        // 수료처리시 필요한 기본 변수들
        String              v_subj                  = box.getString("p_subj");
        String              v_year                  = box.getString("p_year");
        String              v_subjseq               = box.getString("p_subjseq");
        String              v_value1                = box.getString("p_value");        // String with userids
        String              v_value2                = "";
        // String v_isclosed= box.getStringDefault("p_isclosed", "Y");
        String              v_isclosed              = "Y";
        String              v_userid                = "";
        String              v_luserid               = box.getSession("userid");

        double              v_tstep                 = 0;
        double              v_mtest                 = 0;
        double              v_htest                 = 0;
        double              v_ftest                 = 0;
        double              v_report                = 0;
        double              v_etc1                  = 0;
        double              v_etc2                  = 0;

        StringTokenizer     st1                     = null;
        StringTokenizer     st2                     = null;

        StoldData           data                    = new StoldData();
        SubjseqData         subjseqdata             = null;
		String              v_currdate              = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년 +월 +일 +시
        int                 isOk                    = 1;
        boolean             v_isexception           = false;
        String              v_return_msg            = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료처리 완료여부, 학습중 검토
            subjseqdata             = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            
            data.setSubj        (v_subj                     );
            data.setYear        (v_year                     );
            data.setSubjseq     (v_subjseq                  );
            data.setWstep       (subjseqdata.getWstep()     );
            data.setWmtest      (subjseqdata.getWmtest()    );
            data.setWftest      (subjseqdata.getWftest()    );
            data.setWhtest      (subjseqdata.getWhtest()    );
            data.setWreport     (subjseqdata.getWreport()   );
            data.setWact        (subjseqdata.getWact()      );
            data.setWetc1       (subjseqdata.getWetc1()     );
            data.setWetc2       (subjseqdata.getWetc2()     );
            data.setGradscore   (subjseqdata.getGradscore() );
            data.setGradstep    (subjseqdata.getGradstep()  );
            data.setGradexam    (subjseqdata.getGradexam()  );
            data.setGradreport  (subjseqdata.getGradreport());
            data.setEdustart    (subjseqdata.getEdustart()  );
            data.setEduend      (subjseqdata.getEduend()    );

            pstmt_update_student    = getPreparedStatement(connMgr, "SUBJECT_STUDENT"   , "update");
            pstmt_select            = getPreparedStatement(connMgr, "SUBJECT_STOLD"     , "select");
            pstmt_update            = getPreparedStatement(connMgr, "SUBJECT_STOLD"     , "update");
            pstmt_insert            = getPreparedStatement(connMgr, "SUBJECT_STOLD"     , "insert");
            pstmt_select_member     = getPreparedStatement(connMgr, "MEMBER"            , "select");


            sql                     = " SELECT  userid                                                  \n"
                                    + "     ,   NVL(tstep , 0) tstep                                    \n"
                                    + "     ,   NVL(ftest , 0) ftest                                    \n"
                                    + "     ,   NVL(mtest , 0) mtest                                    \n"
                                    + "     ,   NVL(htest , 0) htest                                    \n"
                                    + "     ,   NVL(report, 0) report                                   \n"
                                    + "     ,   NVL(etc1  , 0) etc1                                     \n"
                                    + "     ,   NVL(etc2  , 0) etc2                                     \n"
                                    + " FROM    tz_student                                              \n"
                                    + " WHERE   subj        = " + SQLString.Format(v_subj       )   + " \n"
                                    + " AND     year        = " + SQLString.Format(v_year       )   + " \n"
                                    + " AND     subjseq     = " + SQLString.Format(v_subjseq    )   + " \n";
            
			ls                      = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                v_userid = "";
                v_etc2   = 0;
                v_etc1   = 0;
                v_report = 0;
                v_ftest  = 0;
                v_mtest  = 0;
                v_tstep  = 0;

                v_userid = ls.getString("userid");
                v_tstep  = ls.getDouble("tstep" );
                v_ftest  = ls.getDouble("ftest" );
                v_htest  = ls.getDouble("htest" );
                v_mtest  = ls.getDouble("mtest" );
                v_report = ls.getDouble("report");
                v_etc1   = ls.getDouble("etc1"  );
                v_etc2   = ls.getDouble("etc2"  );
                
                //집합과정 감점/가점 적용 출석률계산과 출석률 셋팅
			    isOk = calc_offtstep(connMgr, v_subj, v_year, v_subjseq, v_userid, data);
                    
                data.setUserid  (v_userid   );
                data.setScore   (0          );
                //data.setTstep   (v_tstep    );
                data.setMtest   (v_mtest    );
                data.setFtest   (v_ftest    );
                data.setHtest   (v_htest    );
                data.setReport  (v_report   );
                data.setAct     (0          );
                data.setEtc1    (v_etc1     );
                data.setEtc2    (v_etc2     );
                data.setAvtstep (0          );
                data.setAvmtest (0          );
                data.setAvftest (0          );
                data.setAvreport(0          );
                data.setAvact   (0          );
                data.setAvetc1  (0          );
                data.setAvetc2  (0          );
                data.setIsgraduated("N");
                
                //System.out.println("ls.next() >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  > 1");
                pstmt_select_member.setString( 1, data.getUserid() );
                
                //System.out.println("ls.next() >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  > 2");

                rs =  pstmt_select_member.executeQuery();
                
                //System.out.println("ls.next() >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  > 3");

                while ( rs.next() ) { 
                    data.setName(rs.getString("name") );
                    data.setComp(rs.getString("comp") );
                }
                
                //System.out.println("ls.next() >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  > 4");

                // SCORE 계산
                calc_offsubj_score(connMgr, data);
                
                //System.out.println("ls.next() >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  > 5");
                

                // 1.tz_student update
                // 2.tz_stold update or insert
                update_offsubj_score(connMgr, pstmt_update_student, pstmt_select, pstmt_insert, pstmt_update, data, v_luserid);
            }
        } catch ( Exception ex ) { ex.printStackTrace();
            isOk            = 100;
            v_isexception   = true;
            v_return_msg =   ex.getMessage();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            try { 
                if ( !v_return_msg.equals("")) box.put("p_return_msg", v_return_msg);
            } catch ( Exception e ) { e.printStackTrace(); }

			if ( ls                     != null ) { try { ls.close();                   } catch ( Exception e ) { } }
            if ( pstmt_select           != null ) { try { pstmt_select.close();         } catch ( Exception e ) { } }
            if ( pstmt_update           != null ) { try { pstmt_update.close();         } catch ( Exception e ) { } }
            if ( pstmt_insert           != null ) { try { pstmt_insert.close();         } catch ( Exception e ) { } }
            if ( pstmt_update_student   != null ) { try { pstmt_update_student.close(); } catch ( Exception e ) { } }
            if ( pstmt_select_member    != null ) { try { pstmt_select_member.close();  } catch ( Exception e ) { } }

            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) {  connMgr.rollback(); } else { connMgr.commit(); }
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e10 ) { }
            }
        }
        return isOk;
    }
    

	/**
    집합과목 점수 계산 
    @param box          receive from the form object and session
    @return int
    */    
    public int calc_offsubj_score(DBConnectionManager connMgr, StoldData data) throws Exception { 
        int isOk = 1;
        try {
            data.setAvtstep((double)Math.round(data.getTstep()*data.getWstep() )/100);
            data.setAvreport((double)Math.round(data.getReport()*data.getWreport() )/100);
            data.setAvmtest((double)Math.round(data.getMtest()*data.getWmtest() )/100);
            data.setAvftest((double)Math.round(data.getFtest()*data.getWftest() )/100);
            data.setAvhtest((double)Math.round(data.getHtest()*data.getWhtest() )/100);
            data.setAvact ((double)Math.round(data.getAct()*data.getWact() )/100);
            data.setAvetc1((double)Math.round(data.getEtc1()*data.getWetc1() )/100);
            data.setAvetc2((double)Math.round(data.getEtc2()*data.getWetc2() )/100);

            isOk = calc_isgraduated(connMgr, data.getSubj(), data.getYear(), data.getSubjseq(), data.getUserid(), data);
        }
        catch ( Exception ex ) { 
            throw new Exception(ex.getMessage() );
        }
        finally { 
        }
        return isOk;
    }
                       
                       
    /**
    수료(TZ_STOLD), 수강생정보(TZ_STUDENT) 처리 
    @param  connMgr      receive from the form object and session
    @return int
    */                       
    public int update_offsubj_score(DBConnectionManager connMgr, PreparedStatement pstmt_update_student, PreparedStatement pstmt_select, 
                                    PreparedStatement pstmt_insert, PreparedStatement pstmt_update, StoldData data, String p_luserid)  throws Exception { 
        ResultSet   rs              = null;
        int         isOk            = 0;
        int         v_count         = 0;
        String      sserno          = "";
        String      ssubj           = data.getSubj       ();
        String      syear           = data.getYear       (); 
        String      ssubjseq        = data.getSubjseq    ();
        String      sisgraduated    = data.getIsgraduated();        
        
        try { 
            // 수료증번호 발급            
            if ( sisgraduated.equals("Y") ) { 
                sserno              = getCompleteSerno(connMgr, ssubj, syear, ssubjseq, data.getUserid() );
            }
            
            // 1.TZ_STOLD update or insert
            pstmt_select.setString( 1, ssubj            );
            pstmt_select.setString( 2, syear            );
            pstmt_select.setString( 3, ssubjseq         );            
            pstmt_select.setString( 4, data.getUserid() );

            rs  =  pstmt_select.executeQuery();
            
            while ( rs.next() ) { 
                v_count = rs.getInt("cnt");
            }

            if ( v_count > 0 ) {  // update
                pstmt_update.setString( 1, data.getName         () );
                pstmt_update.setString( 2, data.getComp         () );
                pstmt_update.setDouble( 3, data.getScore        () );
                pstmt_update.setDouble( 4, data.getTstep        () );
                pstmt_update.setDouble( 5, data.getMtest        () );
                pstmt_update.setDouble( 6, data.getFtest        () );
                pstmt_update.setDouble( 7, data.getReport       () );
                pstmt_update.setDouble( 8, data.getAct          () );
                pstmt_update.setDouble( 9, data.getEtc1         () );
                pstmt_update.setDouble(10, data.getEtc2         () );
                pstmt_update.setDouble(11, data.getAvtstep      () );
                pstmt_update.setDouble(12, data.getAvmtest      () );
                pstmt_update.setDouble(13, data.getAvftest      () );
                pstmt_update.setDouble(14, data.getAvreport     () );
                pstmt_update.setDouble(15, data.getAvact        () );
                pstmt_update.setDouble(16, data.getAvetc1       () );
                pstmt_update.setDouble(17, data.getAvetc2       () );
                pstmt_update.setString(18, data.getIsgraduated  () );
                pstmt_update.setString(19, data.getNotgraducd   () );
                pstmt_update.setString(20, data.getIsb2c        () );
                pstmt_update.setString(21, data.getEdustart     () );
                pstmt_update.setString(22, data.getEduend       () );
                pstmt_update.setString(23, sserno                  );                 // 수료증번호
                pstmt_update.setString(24, data.getIsrestudy    () );
                pstmt_update.setString(25, p_luserid               );
                pstmt_update.setString(26, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt_update.setString(27, data.getSubj         () );
                pstmt_update.setString(28, data.getYear         () );
                pstmt_update.setString(29, data.getSubjseq      () );
                pstmt_update.setString(30, data.getUserid       () );

                isOk    = pstmt_update.executeUpdate();
            } else if ( v_count == 0 ) {  // insert
                pstmt_insert.setString( 1, data.getSubj         () );
                pstmt_insert.setString( 2, data.getYear         () );
                pstmt_insert.setString( 3, data.getSubjseq      () );
                pstmt_insert.setString( 4, data.getUserid       () );
                pstmt_insert.setString( 5, data.getName         () );
                pstmt_insert.setString( 6, data.getComp         () );
                pstmt_insert.setDouble( 7, data.getScore        () );
                pstmt_insert.setDouble( 8, data.getTstep        () );
                pstmt_insert.setDouble( 9, data.getMtest        () );
                pstmt_insert.setDouble(10, data.getFtest        () );
                pstmt_insert.setDouble(11, data.getReport       () );
                pstmt_insert.setDouble(12, data.getAct          () );
                pstmt_insert.setDouble(13, data.getEtc1         () );
                pstmt_insert.setDouble(14, data.getEtc2         () );
                pstmt_insert.setDouble(15, data.getAvtstep      () );
                pstmt_insert.setDouble(16, data.getAvmtest      () );
                pstmt_insert.setDouble(17, data.getAvftest      () );
                pstmt_insert.setDouble(18, data.getAvreport     () );
                pstmt_insert.setDouble(19, data.getAvact        () );
                pstmt_insert.setDouble(20, data.getAvetc1       () );
                pstmt_insert.setDouble(21, data.getAvetc2       () );
                pstmt_insert.setString(22, data.getIsgraduated  () );
                pstmt_insert.setString(23, data.getNotgraducd   () );
                pstmt_insert.setString(24, data.getIsb2c        () );
                pstmt_insert.setString(25, data.getEdustart     () );
                pstmt_insert.setString(26, data.getEduend       () );
                pstmt_insert.setString(27, sserno                  );                 // 수료증번호
                pstmt_insert.setString(28, data.getIsrestudy    () );
                pstmt_insert.setString(29, p_luserid               );
                pstmt_insert.setString(30, FormatDate.getDate("yyyyMMddHHmmss") );

                isOk = pstmt_insert.executeUpdate();
            }
            
            // 2.TZ_STUDENT update
            pstmt_update_student.setDouble( 1, data.getScore        () );
            pstmt_update_student.setDouble( 2, data.getTstep        () );
            pstmt_update_student.setDouble( 3, data.getMtest        () );
            pstmt_update_student.setDouble( 4, data.getFtest        () );
            pstmt_update_student.setDouble( 5, data.getReport       () );
            pstmt_update_student.setDouble( 6, data.getAct          () );
            pstmt_update_student.setDouble( 7, data.getEtc1         () );
            pstmt_update_student.setDouble( 8, data.getEtc2         () );
            pstmt_update_student.setDouble( 9, data.getAvtstep      () );
            pstmt_update_student.setDouble(10, data.getAvmtest      () );
            pstmt_update_student.setDouble(11, data.getAvftest      () );
            pstmt_update_student.setDouble(12, data.getAvreport     () );
            pstmt_update_student.setDouble(13, data.getAvact        () );
            pstmt_update_student.setDouble(14, data.getAvetc1       () );
            pstmt_update_student.setDouble(15, data.getAvetc2       () );
            pstmt_update_student.setString(16, data.getIsgraduated  () );
            pstmt_update_student.setString(17, p_luserid               );
            pstmt_update_student.setString(18, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt_update_student.setString(19, sserno                  );        // 수료증번호            
            pstmt_update_student.setString(20, data.getSubj         () );
            pstmt_update_student.setString(21, data.getYear         () );
            pstmt_update_student.setString(22, data.getSubjseq      () );
            pstmt_update_student.setString(23, data.getUserid       () );
            
            isOk    = pstmt_update_student.executeUpdate();
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQLException : e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( rs != null ) { 
                try { 
                    rs.close();  
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }

    
	/**
	미수료사유 저장
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail
	**/     	    
	 public int updateNotgraducd(RequestBox box) throws Exception { 
	    
		DBConnectionManager connMgr     = null;        
		PreparedStatement   pstmt   = null;   
		String              sql     = "";
		int isOk   = 0;   
		
		String v_userid     = box.getString("p_userid");
		String v_subj		= box.getString("p_subj");
		String v_year		= box.getString("p_year");
		String v_subjseq	= box.getString("p_subjseq");
		String v_notgraducd	= box.getString("p_notgraducd");
	
		try { 
			connMgr = new DBConnectionManager();

	        sql = "update tz_stold set notgraduetc = ? , ldate=? where subj=? and year=? and subjseq=? and userid=?";
			
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, v_notgraducd);          
            pstmt.setString( 2, FormatDate.getDate("yyyyMMddHHmmss") );                      
            pstmt.setString( 3, v_subj);
            pstmt.setString( 4, v_year);
            pstmt.setString( 5, v_subjseq);
            pstmt.setString( 6, v_userid);
            
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
		}
		catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	 }


     /**
     미수료사유 일괄 변경
     @param box      receive from the form object and session
     @return isOk    1:insert success,0:insert fail
     **/             
     public int updateNotgraducdAll(RequestBox box) throws Exception { 
         DBConnectionManager connMgr         = null;
         PreparedStatement   pstmt           = null;
         StringBuffer        sbSQL           = new StringBuffer("");
         int                 isOk            = 0;
            
         int                 iSysAdd         = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
            
         String              v_notgraducds   = box.getString("p_notgraducds" ); 
         String              v_subj          = box.getString("p_subj"        );
         String              v_year          = box.getString("p_year"        );
         String              v_subjseq       = box.getString("p_subjseq"     );
         String              v_notgraducd    = box.getString("p_notgraducd"  );
         String              s_userid        = "";
            
         try { 
             connMgr             = new DBConnectionManager();
             connMgr.setAutoCommit(false);
                
             StringTokenizer st  = new StringTokenizer(v_notgraducds, "^");
                
             sbSQL.append(" update tz_stold set          \n")
                  .append("         notgraduetc = ?      \n")
                  .append("     ,   ldate       = ?      \n")
                  .append(" where   subj        = ?      \n")
                  .append(" and     year        = ?      \n")
                  .append(" and     subjseq     = ?      \n")
                  .append(" and     userid      = ?      \n");
                     
             //System.out.println(this.getClass().getName() + "." + "updateNotgraducdAll() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                     
             pstmt =  connMgr.prepareStatement(sbSQL.toString());
                
             while ( st.hasMoreElements() ) { 
                 s_userid        = StringManager.trim((String)st.nextToken() );
            
                 pstmt.setString(1, v_notgraducd    );          
                 pstmt.setString(2, FormatDate.getDate("yyyyMMddHHmmss") );                      
                 pstmt.setString(3, v_subj          );
                 pstmt.setString(4, v_year          );
                 pstmt.setString(5, v_subjseq       );
                 pstmt.setString(6, s_userid        );
                    
                 isOk    = pstmt.executeUpdate();
             }
             if ( pstmt != null ) { pstmt.close(); }
                
             if ( isOk > 0 ) {
                 connMgr.commit();
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

         return isOk;
     }

     
     /**
	 미수,미수료 저장
	 @param box      receive from the form object and session
	 @return isOk    1:insert success,0:insert fail
	 **/     	    
	 public int updateGraduated(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;        

		PreparedStatement   pstmt   = null;   
		String              sql     = "";
		int isOk = 0;  
		String sserno = "";		 
		
		String v_userid     = box.getString("p_userid");
		String v_subj		= box.getString("p_subj");
		String v_year		= box.getString("p_year");
		String v_subjseq	= box.getString("p_subjseq");
		String v_isgraduated= box.getString("p_isgraduated");
	
		try { 
			connMgr = new DBConnectionManager();

            // 수료증번호 발급            
            if ( v_isgraduated.equals("Y") ) { 
                sserno = getCompleteSerno(connMgr, v_subj, v_year, v_subjseq, v_userid); 
            }

            // TZ_STOLD UPDATE            			
			sql = "update tz_stold set isgraduated = ?, notgraducd='',notgraduetc='', serno = ? where subj=? and year=? and subjseq=? and userid=?";
			
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, v_isgraduated);
            pstmt.setString( 2, sserno);              
            pstmt.setString( 3, v_subj);
            pstmt.setString( 4, v_year);
            pstmt.setString( 5, v_subjseq);
            pstmt.setString( 6, v_userid);
            
            isOk = pstmt.executeUpdate();

            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            
            // TZ_STUDENT UPDATE
            if ( v_isgraduated.equals("Y") ) {
                isOk = updateStudentSerno(connMgr, sserno, v_subj, v_year, v_subjseq, v_userid);                       
            }         
            
            // TZ_STUDENT isgraduated- > UPDATE        
            // 학습자 테이블 update
            updateStudentIsgraduated(connMgr, v_subj, v_year, v_subjseq, v_userid, v_isgraduated);              
                        
		}   
		catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}   
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
	
	
	
	/**
	수료처리완료,미완료 저장
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail
	**/     	    
	 public int updateIsClosed(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_isclosed) throws Exception { 

		PreparedStatement   pstmt   = null;   
		String              sql     = "";
		int isOk = 0;   
		
		
	// 	//System.out.println("v_isclosed = " + v_isclosed);
	// 	//System.out.println("v_subj = " + v_subj);
	// 	//System.out.println("v_year = " + v_year);
	// 	//System.out.println("v_subjseq = " + v_subjseq);
			
				
		try { 
			
			sql = "update tz_subjseq set isclosed = ? where subj=? and year=? and subjseq=?";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, v_isclosed);
            pstmt.setString( 2, v_subj);
            pstmt.setString( 3, v_year);
            pstmt.setString( 4, v_subjseq);
            
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
     //      //System.out.println("isOk=" +isOk);
            
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
		}
		return isOk;
	}
	
     
	 /**
     외주업체 최종확인 저장
     @param box      receive from the form object and session
     @return isOk    1:insert success,0:insert fail
     **/             
     public int updateIsCpflag(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_iscpflag) throws Exception { 
         PreparedStatement   pstmt   = null;
         StringBuffer        sbSQL   = new StringBuffer("");
         int                 isOk    = 0;
            
         int                 iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

         try { 
             sbSQL.append(" update tz_subjseq set        \n")
                  .append("         iscpflag    = ?      \n")
                  .append(" where   subj        = ?      \n")
                  .append(" and     year        = ?      \n")
                  .append(" and     subjseq     = ?      \n");
                     
             //System.out.println(this.getClass().getName() + "." + "updateIsCpflag() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
             pstmt   = connMgr.prepareStatement(sbSQL.toString());
                
             pstmt.setString( 1, v_iscpflag  );
             pstmt.setString( 2, v_subj      );
             pstmt.setString( 3, v_year      );
             pstmt.setString( 4, v_subjseq   );
                
             isOk = pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
         } catch ( SQLException e ) {
             ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
             throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } catch ( Exception e ) {
             ErrorManager.getErrorStackTrace(e);
             throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } finally {
             if ( pstmt != null ) { 
                 try { 
                     pstmt.close(); 
                 } catch ( Exception e ) { } 
             }
         }
                
         return isOk;
     }

	
	/**
	집합과목 수료처리
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail
	**/     	    
	 public int updateOffSubjIsClosed(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;   
		String              sql     = "";
		int isOk = 0;   
		
		String v_subj  	= box.getString("p_subj");
		String v_year  	= box.getString("p_year");
		String v_subjseq  	= box.getString("p_subjseq");
		
	// 	//System.out.println("신 집합과목 수료처리");
	// 	//System.out.println("v_subj = " + v_subj);
	// 	//System.out.println("v_year = " + v_year);
	// 	//System.out.println("v_subjseq = " + v_subjseq);
			
				
		try { 
			connMgr = new DBConnectionManager();
			
			// 수료처리 완료로 저장
            //// isOk = this.updateIsClosed(connMgr, v_subj, v_year, v_subjseq, "Y");
            
       //     //System.out.println("isOk=" +isOk);
            
		}
		catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
	
	/**
	외주업체 과목 수료처리
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail
	**/     	    
	 public int updateOutSubjIsClosed(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;   
		PreparedStatement pstmt2 = null;   
		ListSet rs = null;
		DataBox dbox = null;
		String              sql     = "";
		String sql2 = "";
		int isOk = 0;   
		
		String v_subj  	= box.getString("p_subj");
		String v_year  	= box.getString("p_year");
		String v_subjseq  	= box.getString("p_subjseq");
		
		String              v_serno     = "";
		
		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
						
			// stold table delete
			// 수료 정보 삭제
            isOk = deleteStoldTable(connMgr, v_subj, v_year, v_subjseq);
            
			// Student data를 Stold 테이블에 복사
			sql = "\n insert into tz_stold "
				+ "\n    ( "
				+ "\n     subj       , year       , subjseq    , userid    , name "
				+ "\n   , comp       , score      , tstep      , mtest     , ftest "
				+ "\n   , report     , act        , etc1       , etc2      , avtstep "
				+ "\n   , avmtest    , avftest    , avreport   , avact     , avetc1 "
				+ "\n   , avetc2     , isgraduated, isrestudy  , isb2c     , edustart "
				+ "\n   , eduend     , luserid    , ldate      , htest     , avhtest "
				+ "\n   , notgraduetc, notgraducd , study_count, study_time "
				+ "\n   , post       , dept_cd    , job_cd, serno, examnum "
				+ "\n    ) "
				+ "\n select "
				+ "\n     a.subj       , a.year       , a.subjseq    , a.userid, get_name(a.userid) "
				+ "\n   , a.comp       , a.score      , a.tstep      , a.mtest , a.ftest "
				+ "\n   , a.report     , a.act        , a.etc1       , a.etc2  , a.avtstep "
				+ "\n   , a.avmtest    , a.avftest    , a.avreport   , a.avact , a.avetc1 "
				+ "\n   , a.avetc2     , a.isgraduated, a.isrestudy  , a.isb2c , b.edustart "
				+ "\n   , b.eduend     , a.luserid    , to_char(sysdate, 'yyyymmddhh24miss'), a.htest, a.avhtest "
				+ "\n   , a.notgraduetc, a.notgraduetc, a.study_count, b.study_time "
				+ "\n   , c.post       , c.dept_cd    , c.job_cd , serno, examnum"
				+ "\n from  tz_student a, tz_subjseq b, tz_member c "
				+ "\n where a.subj = b.subj "
				+ "\n and   a.year = b.year "
				+ "\n and   a.subjseq = b.subjseq "
				+ "\n and   a.userid = c.userid "
				+ "\n and   a.subj = ? "
				+ "\n and   a.year=? "
				+ "\n and   a.subjseq=? ";
			
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, v_subj);
            pstmt.setString( 2, v_year);
            pstmt.setString( 3, v_subjseq);
            
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
			// 수료처리 완료로 저장
            isOk = this.updateIsClosed(connMgr, v_subj, v_year, v_subjseq, "Y");
            
            // 최종확인 컬럼 업데이트
            isOk = this.updateIsCpflag(connMgr, v_subj, v_year, v_subjseq, "Y");
            
            connMgr.commit();
            
		}
		catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
	
	/**
	외주업체 과목 결과재요청
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail
	**/     	    
	 public int updateOutSubjReject(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;   
		String              sql     = "";
		int isOk = 0;   
		
		String v_subj  	= box.getString("p_subj");
		String v_year  	= box.getString("p_year");
		String v_subjseq  	= box.getString("p_subjseq");
		
		
	// 	//System.out.println("v_subj = " + v_subj);
	// 	//System.out.println("v_year = " + v_year);
	// 	//System.out.println("v_subjseq = " + v_subjseq);
			
				
		try { 
			connMgr = new DBConnectionManager();
			
			sql = "update tz_subjseq set iscpresult = 'N', iscpflag='N' where subj=? and year=? and subjseq=?";
			
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, v_subj);
            pstmt.setString( 2, v_year);
            pstmt.setString( 3, v_subjseq);
            
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
     //       //System.out.println("isOk=" +isOk);
            
           
		}
		catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
	
	
	/**
	집합과목 수료처리 업로드 저장
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail(수강생 아님)
	**/     	    
	 public int updateOffStudentScore(DBConnectionManager connMgr, Hashtable data ) throws Exception {
            PreparedStatement   pstmt               = null;
            StringBuffer        sbSQL               = new StringBuffer("");
            int                 isOk                = 1;
            
            int                 iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
            int                 iIdx                = 0;
            
            DataBox             dbox                = null;
            
            SubjseqData         sdata               = null;
            
            String              v_subj              = (String)data.get("p_subj"    );
            String              v_year              = (String)data.get("p_year"    );
            String              v_subjseq           = (String)data.get("p_subjseq" );
            String              v_userid            = (String)data.get("p_userid"  );
            double              v_tstep             = ((Double)data.get("p_tstep"  )).doubleValue();
            double              v_ftest             = ((Double)data.get("p_ftest"  )).doubleValue();
            double              v_report            = ((Double)data.get("p_report" )).doubleValue();
            double              v_etc1              = ((Double)data.get("p_etc1"   )).doubleValue();
            double              v_etc2              = 0.0;
            double              v_htest             = 0.0;
            double              v_mtest             = 0.0;
            double              v_act               = 0.0;
            double              v_score             = 0.0;
            
            double              v_avtstep           = 0.0;
            double              v_avftest           = 0.0;
            double              v_avreport          = 0.0;
            double              v_avetc1            = 0.0;
            double              v_avetc2            = 0.0;
            double              v_avhtest           = 0.0;
            double              v_avmtest           = 0.0;
            double              v_avact             = 0.0;
            
            try { 
                sdata            = getSubjseqInfo(connMgr, v_subj, v_year, StringUtil.lpad(v_subjseq, "0", 4));
                
                v_avtstep       = ((double)Math.round(v_tstep   * sdata.getWstep()     ) / 100);
                v_avreport      = ((double)Math.round(v_report  * sdata.getWreport()   ) / 100);
                v_avmtest       = ((double)Math.round(v_mtest   * sdata.getWmtest()    ) / 100);
                v_avftest       = ((double)Math.round(v_ftest   * sdata.getWftest()    ) / 100);
                v_avhtest       = ((double)Math.round(v_htest   * sdata.getWhtest()    ) / 100);
                v_avact         = ((double)Math.round(v_act     * sdata.getWact()      ) / 100);
                v_avetc1        = ((double)Math.round(v_etc1    * sdata.getWetc1()     ) / 100);
                v_avetc2        = ((double)Math.round(v_etc2    * sdata.getWetc2()     ) / 100);
                
                v_score         = v_avtstep + v_avreport + v_avmtest + v_avftest + v_avhtest + v_avact + v_avetc1 + v_avetc2;
                
                if ( v_score > 100 )
                    v_score     = 100.0;
                
                sbSQL.append(" Update Tz_Student SET                \n")
                     .append("         tstep    = ?                 \n")
                     .append("     ,   ftest    = ?                 \n")
                     .append("     ,   report   = ?                 \n")
                     .append("     ,   etc1     = ?                 \n")
                    // .append("     ,   mtest    = ?                 \n")
                     .append("     ,   htest    = ?                 \n")
                     .append("     ,   act      = ?                 \n")
                     .append("     ,   etc2     = ?                 \n")
                     .append("     ,   avtstep  = ?                 \n")
                     .append("     ,   avftest  = ?                 \n")
                     .append("     ,   avreport = ?                 \n")
                     .append("     ,   avetc1   = ?                 \n")
                     .append("     ,   avmtest  = ?                 \n")
                     .append("     ,   avhtest  = ?                 \n")
                     .append("     ,   avact    = ?                 \n")
                     .append("     ,   avetc2   = ?                 \n")
                     .append("     ,   score    = ?                 \n")
                     .append(" WHERE   UserId  = ?                  \n")
                     .append(" AND     Year    = ?                  \n")
                     .append(" AND     Subj    = ?                  \n")
                     .append(" AND     Subjseq = LPad(?, 4, '0')    \n");
        
                pstmt = connMgr.prepareStatement(sbSQL.toString());
                
                pstmt.setDouble( ++iIdx, v_tstep    );
                pstmt.setDouble( ++iIdx, v_ftest    );
                pstmt.setDouble( ++iIdx, v_report   );
                pstmt.setDouble( ++iIdx, v_etc1     );
               // pstmt.setDouble( ++iIdx, v_mtest    );
                pstmt.setDouble( ++iIdx, v_htest    );
                pstmt.setDouble( ++iIdx, v_act      );
                pstmt.setDouble( ++iIdx, v_etc2     );
                pstmt.setDouble( ++iIdx, v_avtstep  );
                pstmt.setDouble( ++iIdx, v_avftest  );
                pstmt.setDouble( ++iIdx, v_avreport );
                pstmt.setDouble( ++iIdx, v_avetc1   );
                pstmt.setDouble( ++iIdx, v_avmtest  );
                pstmt.setDouble( ++iIdx, v_avhtest  );
                pstmt.setDouble( ++iIdx, v_avact    );
                pstmt.setDouble( ++iIdx, v_avetc2   );
                pstmt.setDouble( ++iIdx, v_score    );
                pstmt.setString( ++iIdx, v_userid   );
                pstmt.setString( ++iIdx, v_year     );
                pstmt.setString( ++iIdx, v_subj     );
                pstmt.setString( ++iIdx, v_subjseq  );
                
//                System.out.println(" v_tstep    : " + v_tstep    );
//                System.out.println(" v_ftest    : " + v_ftest    );
//                System.out.println(" v_report   : " + v_report   );
//                System.out.println(" v_etc1     : " + v_etc1     );
//                System.out.println(" v_mtest    : " + v_mtest    );
//                System.out.println(" v_htest    : " + v_htest    );
//                System.out.println(" v_act      : " + v_act      );
//                System.out.println(" v_etc2     : " + v_etc2     );
//                System.out.println(" v_avtstep  : " + v_avtstep  );
//                System.out.println(" v_avftest  : " + v_avftest  );
//                System.out.println(" v_avreport : " + v_avreport );
//                System.out.println(" v_avetc1   : " + v_avetc1   );
//                System.out.println(" v_avmtest  : " + v_avmtest  );
//                System.out.println(" v_avhtest  : " + v_avhtest  );
//                System.out.println(" v_avact    : " + v_avact    );
//                System.out.println(" v_avetc2   : " + v_avetc2   );
//                System.out.println(" v_score    : " + v_score    );
//                System.out.println(" v_userid   : " + v_userid   );
//                System.out.println(" v_year     : " + v_year     );
//                System.out.println(" v_subj     : " + v_subj     );
//                System.out.println(" v_subjseq  : " + v_subjseq  );
//                
//                System.out.println(" v_userid    : " + v_userid    );
//                System.out.println(" v_year      : " + v_year      );
//                System.out.println(" v_subj      : " + v_subj      );
//                System.out.println(" v_subjseq   : " + v_subjseq   );

                isOk = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
            } catch ( SQLException e ) {
                isOk = 0;
                ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
                throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
            } catch ( Exception e ) {
                isOk = 0;
                ErrorManager.getErrorStackTrace(e, null, "");
                throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
            } finally {
                if ( pstmt != null ) { 
                    try { 
                        pstmt.close(); 
                    } catch ( Exception e ) { } 
                }
            }

            return isOk;
	}
     
     

        /**
        집합과목 수료처리 업로드 저장
        @param box      receive from the form object and session
        @return isOk    1:insert success,0:insert fail(수강생 아님)
        **/             
         public int updateOffStudentScore1(DBConnectionManager connMgr, Hashtable data ) throws Exception { 

            PreparedStatement   pstmt   = null;

            String              sql     = "";
            int isOk = 0;   
            
            String v_subj       = (String)data.get("p_subj");
            String v_year       = (String)data.get("p_year");
            String v_subjseq    = (String)data.get("p_subjseq");
            String v_userid     = (String)data.get("p_userid");
                                  
            double v_tstep      = ((Double)data.get("p_tstep")).doubleValue(); // 출석률 점수
            double v_ftest      = ((Double)data.get("p_ftest")).doubleValue(); // 평가 점수
            double v_report     = ((Double)data.get("p_report")).doubleValue(); // 리포트 점수
            double v_etc1       = ((Double)data.get("p_etc1")).doubleValue(); // 기타 점수

            try { 
            
                sql = "update tz_student set tstep=?, ftest=?, report=?, etc1=? where subj=? and year=? and subjseq=? and userid=?";
                
//                System.out.println(sql );
                

//                System.out.println(" v_subj     : " + v_subj    );
//                System.out.println(" v_year     : " + v_year    );
//                System.out.println(" v_subjseq  : " + v_subjseq );
//                System.out.println(" v_userid   : " + v_userid  );
//                System.out.println(" v_tstep    : " + v_tstep   );
//                System.out.println(" v_ftest    : " + v_ftest   );
//                System.out.println(" v_report   : " + v_report  );
//                System.out.println(" v_etc1     : " + v_etc1    );

                pstmt = connMgr.prepareStatement(sql);
                pstmt.setDouble( 1, v_tstep);
                pstmt.setDouble( 2, v_ftest);
                pstmt.setDouble( 3, v_report);
                pstmt.setDouble( 4, v_etc1);
                
                pstmt.setString( 5, v_subj);
                pstmt.setString( 6, v_year);
                pstmt.setString( 7, v_subjseq);
                pstmt.setString( 8, v_userid);
                
                isOk = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
                
            }
            catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex);
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            }
            return isOk;
        }
     
	
	
	/**
	교육주관 과목기수(subjseqgr)를 과목기수(subjseq)로 리턴
	@param  connMgr,subj,year,grcode
	@return subjseq
	**/     	    
	 public String selectGrSubjseq(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseqgr, String v_grcode) throws Exception { 

		String              sql     = "";
        Statement stmt = null;
        ResultSet rs = null;
        
        String v_subjseq = "";
				
		try { 
		
			stmt = connMgr.createStatement();
            
            sql = "select subjseq from tz_subjseq where subj = '" + v_subj + "' and year = '" + v_year + "' and subjseqgr = '" + v_subjseqgr + "' and grcode = '" + v_grcode + "' ";

	// 		//System.out.println("실제기수=" +sql);
            rs = stmt.executeQuery(sql);
            
            if ( rs.next() )
            	v_subjseq = rs.getString(1);
            
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
			if ( stmt != null ) { try { stmt.close(); } catch ( Exception e ) { } }
		}
		return v_subjseq;
	}

    /**
    수강생정보 수료증번호 UPDATE
    @param  connMgr      receive from the form object and session
    @param  serno    
    @param  subj
    @param  year
    @param  subjseq           
    @param  userid     
    @return int
    */    	    
	 public int updateStudentSerno(DBConnectionManager connMgr, String s_serno, String v_subj, String v_year, String v_subjseq, String v_userid) throws Exception { 

		PreparedStatement   pstmt   = null;   
		String              sql     = "";
		int isOk = 0;   
				
		try { 
           
    		sql = "update TZ_STUDENT set serno = ? where subj=? and year=? and subjseq=? and userid=?";
    			                                
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, s_serno);              
            pstmt.setString( 2, v_subj);    
            pstmt.setString( 3, v_year);    
            pstmt.setString( 4, v_subjseq); 
            pstmt.setString( 5, v_userid);  
                                                        
            isOk = pstmt.executeUpdate();              
            if ( pstmt != null ) { pstmt.close(); }
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
		}
		return isOk;
	}
	
	
    /**
    수료증번호 발급 처리
    @param  connMgr      receive from the form object and session
    @param  subj
    @param  year
    @param  subjseq           
    @param  userid     
    @return String
    */
    public String getCompleteSerno(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_userid) throws Exception { 	
        ListSet   ls = null;
        String sql   = "";
        String sserno= "";   
                
        try { 
            // TZ_STUDENT serno is check            
            sql = "\n select serno "
            	+ "\n from   tz_student "
            	+ "\n where  subj='" +v_subj + "' "
            	+ "\n and    year='" +v_year + "' "
            	+ "\n and    subjseq='" +v_subjseq + "' "
            	+ "\n and    userid='" +v_userid + "'  ";
 	//System.out.println("수료::::::::::::::::::::::" + sql);		
 			ls = connMgr.executeQuery(sql);
 			if ( ls.next() ) sserno = ls.getString("serno");
 			ls.close();
             
            if ( sserno.equals("") ) { 
                sserno = getMaxCompleteCode(connMgr, v_subj, v_year, v_subjseq); 
            }  
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
      //System.out.println("sserno------------ > " +sserno);
        return sserno;
    }   
    
    /**
    @param  connMgr      receive from the form object and session
    @param  subj
    @param  year
    @param  subjseq           
    @param  userid     
    @return String
    */
    public int getFtestValue(String v_subj, String v_year, String v_subjseq, String v_userid) throws Exception { 	
        DBConnectionManager	connMgr	= null;
        String sql   = "";
        int ftest= 0;   
                
        try { 
            connMgr = new DBConnectionManager();
            ftest = getFtestValue(connMgr, v_subj, v_year, v_subjseq, v_userid);
            
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return ftest;
    }   
    
    /**
    @param  connMgr      receive from the form object and session
    @param  subj
    @param  year
    @param  subjseq           
    @param  userid     
    @return String
    */
    public int getFtestValue(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_userid) throws Exception { 	
        ListSet   ls = null;
        String sql   = "";
        int ftest= 0;   
                
        try { 
            // TZ_STUDENT serno is check            
            sql = " select ftest      ";
            sql += " from   tz_student ";
            sql += " where  subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and userid='" +v_userid + "'  ";

 			ls = connMgr.executeQuery(sql);
 			if ( ls.next() ) ftest = ls.getInt("ftest");
 			ls.close();
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return ftest;
    }   
    
    
    /**
    @param  connMgr      receive from the form object and session
    @param  subj
    @param  year
    @param  subjseq           
    @param  userid     
    @return String
    */
    public int getAvFtestValue(String v_subj, String v_year, String v_subjseq, String v_userid) throws Exception { 	
        DBConnectionManager	connMgr	= null;
        String sql   = "";
        int avftest= 0;   
                
        try { 
            connMgr = new DBConnectionManager();
            avftest = getAvFtestValue(connMgr, v_subj, v_year, v_subjseq, v_userid);
            
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return avftest;
    }   
    
    
    /**
    @param  connMgr      receive from the form object and session
    @param  subj
    @param  year
    @param  subjseq           
    @param  userid     
    @return String
    */
    public int getAvFtestValue(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_userid) throws Exception { 	
        ListSet   ls = null;
        String sql   = "";
        int avftest= 0;   
                
        try { 
            // TZ_STUDENT serno is check            
            sql = " select avftest      ";
            sql += " from   tz_student ";
            sql += " where  subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and userid='" +v_userid + "'  ";

 			ls = connMgr.executeQuery(sql);
 			if ( ls.next() ) avftest = ls.getInt("avftest");
 			ls.close();
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return avftest;
    }   
    
                   
    /**
    수료증번호 구하기(yyyy-MM-00000)
    @param  connMgr      receive from the form object and session
    @return String
    */
    public String getMaxCompleteCode(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq) throws Exception { 
		
        String v_completecode = "";
        String v_maxcompletecode = "";
        int    v_maxno   = 0;

        ListSet             ls      = null;
        String sql  = "";
        
        String currdate = FormatDate.getDate("yyyyMM");
                
        try { 

        	/*
			sql = "select nvl(substr(eduend,1,6),to_char(sysdate, 'YYYYMM')) yymmdd from tz_subjseq ";
			sql += "where  subj = '" + v_subj + "' and ";
 			sql += "		  year = '" + v_year + "' and ";
 			sql += "		  subjseq = '" + v_subjseq + "'";
 			
 			ls = connMgr.executeQuery(sql);
 			
 			if ( ls.next() ) { 
 				currdate = ls.getString("yymmdd");
 			}
 			ls.close();

			// 수료증 번호 규칙 : 년 +월 +일련번호(5)(yyyy-MM-00001)
			// 수료증 번호 규칙 : 년월(YYYYMM)-과목코드(4)-기수(2)-일련번호(4)
			// sql = "select max(substr(serno,9,13)) maxno ";
 			// sql += "from tz_stold ";
 			// sql += "where		  substr(serno,1,8) = '" + currdate + "-' ";
 			
 			sql = "select 	max(substr(serno,16,19)) maxno ";
 			sql += "from 	tz_stold ";
 			sql += "where	subj = '" + v_subj + "' and ";
 			sql += "			year = '" + v_year + "' and ";
 			sql += "			subjseq = '" + v_subjseq + "' and ";
 			sql += "		   substr(serno,1,14) = '" + currdate + "-" + v_subj + "-" + v_subjseq.substring(2,4) + "'";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                 v_maxcompletecode = ls.getString("maxno");
            }

            if ( v_maxcompletecode.equals("") ) { 
                v_completecode = currdate + "-" + v_subj + "-" + v_subjseq.substring(2,4) + "-" + "0001";
            } else { 
                v_maxno = Integer.valueOf(v_maxcompletecode.substring(1)).intValue();
				v_completecode = currdate + "-" + v_subj + "-" + v_subjseq.substring(2,4) + "-" + new DecimalFormat("0000").format(v_maxno +1);
            }

            */

            
        	// 2009.11.09  수료증 번호 규칙
        	// 수료증 번호 규칙 :  [년도뒤2자리-비환급(02)-과정차수-순번]/[년도뒤2자리-환급(01)-과정차수-순번]
        	// ex) 09-01-0001-001
            sql = "\n select substr(eduend,3,2) as edu_year, decode(isgoyong,'Y','01','02') goyong "
            	+ "\n from   tz_subjseq "
            	+ "\n where  subj = " + SQLString.Format(v_subj)
            	+ "\n and    year = " + SQLString.Format(v_year)
            	+ "\n and    subjseq = " + SQLString.Format(v_subjseq);
            ls = connMgr.executeQuery(sql);
 			
            String v_edu_year = "";
            String v_isgoyong = "02";
 			if ( ls.next() ) { 
 				v_edu_year = ls.getString("edu_year");
 				v_isgoyong = ls.getString("goyong"); 				
 			}
 			ls.close();
 			
 			//sql = "\n select max(substr(serno,instr(serno,'-',1,4)+1,length(serno))) as maxno "
 			sql = "\n select max(serno) as maxno "
 				+ "\n from   tz_stold "
 				+ "\n where  subj = " + SQLString.Format(v_subj)
 				+ "\n and    year = " + SQLString.Format(v_year)
 				+ "\n and    subjseq = " + SQLString.Format(v_subjseq);
 				//+ "\n and    substr(serno,1,instr(serno,'-',1,4)-1) = '" + v_edu_year + "-" + v_isgoyong + "-" + v_subj + "-" + Integer.parseInt(v_subjseq) + "'";
            ls = connMgr.executeQuery(sql); 				
//System.out.println("수료번호쿼리ㄴ::"+sql);
            while ( ls.next() ) { 
                v_maxcompletecode = ls.getString("maxno");

            }
//System.out.println("v_maxcompletecode :::::::::[1]:::::::::::::::::::>>>>" + v_maxcompletecode);
            if ( v_maxcompletecode.equals("") ) { 
            	//v_completecode = v_edu_year + "-" + v_subj + "-" + Integer.parseInt(v_subjseq) + "-" + "001";
            	//v_completecode = v_edu_year + "-" + v_isgoyong +"-"+v_subj+ "-" + Integer.parseInt(v_subjseq) + "-" + "001";
            	v_completecode = "0001";
            } else { 
            	v_maxno = Integer.valueOf(v_maxcompletecode).intValue();
//            	v_completecode = v_edu_year + "-" + v_subj + "-" + Integer.parseInt(v_subjseq) + "-" + new DecimalFormat("000").format(v_maxno +1);
            	//v_completecode = v_edu_year + "-" + v_isgoyong +"-"+v_subj+ "-" + Integer.parseInt(v_subjseq) + "-" + new DecimalFormat("000").format(v_maxno +1);
            	v_completecode =  new DecimalFormat("0000").format(v_maxno +1);
//System.out.println("v_maxcompletecode :::::[2]:::::::::::::::::::::>>>>" + v_maxcompletecode);
            }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return v_completecode;
    }    
    

    /**
    tz_student isgraduated update 
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
    public int updateStudentIsgraduated(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_userid, String isgubun) throws Exception { 
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
        int                 iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            sbSQL.append(" update tz_student set    \n")
                 .append("         isgraduated = ?  \n")
                 .append(" where   subj        = ?  \n")
                 .append(" and     year        = ?  \n")
                 .append(" and     subjseq     = ?  \n");
            
            if ( !v_userid.equals("") ) { 
                sbSQL.append(" and  userid     = ?  \n");
            }
            
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString(1, isgubun     );
            pstmt.setString(2, v_subj      );
            pstmt.setString(3, v_year      );
            pstmt.setString(4, v_subjseq   );
            
            if ( !v_userid.equals("") ) {            
                pstmt.setString(5, v_userid);
            }
                        
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }
            
        return isOk;
    }
    

    /**
    수료 과목에 대한 댓글 리스트
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectSubjCommentList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        ArrayList           list                = new ArrayList();
                                    
        int                 iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        DataBox             dbox                = null;
                                                
        String              v_year              = box.getStringDefault("p_year", FormatDate.getDate("yyyy"));
        String              v_subjseq           = box.getString("p_subjseq" );
        String              v_subj              = box.getString("p_subj"    );
        String              v_rowcount          = box.getString("p_rowcount");
                            
        try { 
            connMgr = new DBConnectionManager();
        
            sbSQL.append(" SELECT  *                                                                                            \n")
                 .append(" FROM    (                                                                                            \n")
                 .append("             SELECT  a.subj                                                                           \n")            
                 .append("                 ,   a.year                                                                           \n")            
                 .append("                 ,   a.subjseq                                                                        \n")            
                 .append("                 ,   a.userid                                                                         \n")            
                 .append("                 ,   b.name                                                                           \n")            
                 .append("                 ,   a.selreturn                                                                      \n")
                 .append("                 ,   a.comments                                                                       \n")
                 .append("                 ,   TO_CHAR(TO_DATE(a.ldate, 'YYYYMMDDHH24MISS'), 'YYYY.MM.DD HH24:MI:SS')  ldate    \n")
                 .append("                 ,   COUNT(*) OVER() totcnt                                                           \n")
                 .append("             FROM    Tz_Stold_Comments   a                                                            \n")                   
                 .append("                 ,   Tz_Member           b                                                            \n")
                 .append("             WHERE   a.year          = " + SQLString.Format(v_year       ) + "                        \n")
                 .append("             AND     a.subj          = " + SQLString.Format(v_subj       ) + "                        \n");
            
            if ( !v_subjseq.equals("") )
                sbSQL.append("             AND     a.subjseq       = " + SQLString.Format(v_subjseq    ) + "                    \n");
            
            sbSQL.append("             AND     a.userid        = b.userid                                                       \n")
                 .append("             ORDER BY a.LDate Desc                                                                    \n")
                 .append("         )       v                                                                                    \n");
            
            if ( !v_rowcount.equals("ALL") )
                sbSQL.append(" WHERE   ROWNUM <= 3                                                                              \n");

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
    수료생 만족도 통계 리스트 화면
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectStOldStatisafactInfoList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        DataBox             dbox                = null;
        ArrayList           list                = new ArrayList();
                                    
        int                 iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_subj              = box.getString("p_subj");
        
        try { 
            connMgr = new DBConnectionManager();
        
            sbSQL.append(" SELECT  NVL(b.SixMMPercent  , a.SixMMPercent )  SixMMPercent                                                                                     \n")
                 .append("     ,   NvL(b.OneMMPercent  , a.OneMMPercent )  OneMMPercent                                                                                     \n")
                 .append("     ,   NvL(b.SixMMCnt      , a.SixMMCnt     )  SixMMCnt                                                                                         \n")
                 .append("     ,   NvL(b.OneMMCnt      , a.OneMMCnt     )  OneMMCnt                                                                                         \n")
                 .append("     ,   NvL(b.SixMMTotCnt   , a.SixMMTotCnt  )  SixMMTotCnt                                                                                      \n")
                 .append("     ,   NvL(b.OneMMTotCnt   , a.OneMMTotCnt  )  OneMMTotCnt                                                                                      \n")
                 .append("     ,   NvL(b.TotCnt        , a.TotCnt       )  TotCnt                                                                                           \n")
                 .append("     ,   NvL(b.SelReturn     , a.SelReturn    )  SelReturn                                                                                        \n")
                 .append(" FROM    (                                                                                                                                        \n")
                 .append("             SELECT  0 SixMMPercent, 0 OneMMPercent, 0 SixMMCnt, 0 OneMMCnt, 0 SixMMTotCnt, 0 OneMMTotCnt, 0 TotCnt, '5' SelReturn FROM DUAL      \n")
                 .append("             UNION ALL                                                                                                                            \n")
                 .append("             SELECT  0, 0, 0, 0, 0, 0, 0, '4' FROM DUAL                                                                                           \n")
                 .append("             UNION ALL                                                                                                                            \n")
                 .append("             SELECT  0, 0, 0, 0, 0, 0, 0, '3' FROM DUAL                                                                                           \n")
                 .append("             UNION ALL                                                                                                                            \n")
                 .append("             SELECT  0, 0, 0, 0, 0, 0, 0, '2' FROM DUAL                                                                                           \n")
                 .append("             UNION ALL                                                                                                                            \n")
                 .append("             SELECT  0, 0, 0, 0, 0, 0, 0, '1' FROM DUAL                                                                                           \n")
                 .append("         )       a                                                                                                                                \n")
                 .append("     ,   (                                                                                                                                        \n")
                 .append("             SELECT  SixMMCnt / OneMMTotCnt * 100    SixMMPercent                                                                                 \n")
                 .append("                 ,   OneMMCnt / OneMMTotCnt * 100    OneMMPercent                                                                                 \n")
                 .append("                 ,   SixMMCnt                                                                                                                     \n")
                 .append("                 ,   OneMMCnt                                                                                                                     \n")
                 .append("                 ,   SixMMTotCnt                                                                                                                  \n")
                 .append("                 ,   OneMMTotCnt                                                                                                                  \n")
                 .append("                 ,   TotCnt                                                                                                                       \n")
                 .append("                 ,   SelReturn                                                                                                                    \n")
                 .append("             FROM    (                                                                                                                            \n")
                 .append("                         SELECT   SelReturn                                                                                                       \n")
                 .append("                          ,   COUNT(                                                                                                              \n")
                 .append("                                     CASE WHEN        TO_CHAR(TO_DATE(LDate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')                                     \n")
                 .append("                                              BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -6), 'YYYYMMDD') AND TO_CHAR(SYSDATE, 'YYYYMMDD')               \n")
                 .append("                                          THEN        1                                                                                           \n")
                 .append("                                      END                                                                                                         \n")
                 .append("                                   )                  SixMMCnt                                                                                    \n")
                 .append("                          ,   NVL(SUM(COUNT(                                                                                                      \n")
                 .append("                                         CASE WHEN        TO_CHAR(TO_DATE(LDate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')                                 \n")
                 .append("                                                  BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -6), 'YYYYMMDD') AND TO_CHAR(SYSDATE, 'YYYYMMDD')           \n")
                 .append("                                              THEN        1                                                                                       \n")
                 .append("                                          END                                                                                                     \n")
                 .append("                                       )                                                                                                          \n")
                 .append("                                 ) OVER(), 0)             SixMMTotCnt                                                                             \n")
                 .append("                          ,   COUNT(                                                                                                              \n")
                 .append("                                     CASE WHEN        TO_CHAR(TO_DATE(LDate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')                                     \n")
                 .append("                                              BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYYMMDD') AND TO_CHAR(SYSDATE, 'YYYYMMDD')               \n")
                 .append("                                          THEN        1                                                                                           \n")
                 .append("                                      END                                                                                                         \n")
                 .append("                                   )                  OneMMCnt                                                                                    \n")
                 .append("                          ,   NVL(SUM(COUNT(                                                                                                      \n")
                 .append("                                         CASE WHEN        TO_CHAR(TO_DATE(LDate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')                                 \n")
                 .append("                                                  BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYYMMDD') AND TO_CHAR(SYSDATE, 'YYYYMMDD')           \n")
                 .append("                                              THEN        1                                                                                       \n")
                 .append("                                          END                                                                                                     \n")
                 .append("                                       )                                                                                                          \n")
                 .append("                                  ) OVER(), 0)        OneMMTotCnt                                                                                 \n")
                 .append("                          ,   SUM(COUNT(*)) OVER()    TotCnt                                                                                      \n")
                 .append("                         FROM    Tz_StOld_Comments                                                                                                \n")
                 .append("                         WHERE    Subj = " + SQLString.Format(v_subj) + "                                                                         \n")
                 .append("                         GROUP BY SelReturn                                                                                                       \n")
                 .append("                     )                                                                                                                            \n")
                 .append("         )        b                                                                                                                               \n")
                 .append(" WHERE   a.SelReturn = b.SelReturn(+)                                                                                                             \n")
                 .append(" ORDER BY a.SelReturn DESC                                                                                                                        \n");
                 
            //System.out.println(this.getClass().getName() + "." + "SelectStOldStatisafactInfoList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
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
    수료생 만족도 통계의 가장 많이 선택된 별표 정보
    @param box          receive from the form object and session
    @return ArrayList
    */
    public DataBox SelectStOldStatisafactInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        DataBox             dbox                = null;
                                    
        int                 iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_subj              = box.getString("p_subj");
        
        try { 
            connMgr = new DBConnectionManager();
        
            sbSQL.append(" SELECT  SixMMPercent                                                                                                                         \n")
                 .append("     ,   OneMMPercent                                                                                                                         \n")
                 .append("     ,   SixMMCnt                                                                                                                             \n")
                 .append("     ,   OneMMCnt                                                                                                                             \n")
                 .append("     ,   SixMMTotCnt                                                                                                                          \n")
                 .append("     ,   OneMMTotCnt                                                                                                                          \n")
                 .append("     ,   TotCnt                                                                                                                               \n")
                 .append("     ,   SelReturn                                                                                                                            \n")
                 .append(" FROM    (                                                                                                                                    \n")
                 //.append("             SELECT  SixMMCnt / OneMMTotCnt * 100    SixMMPercent                                                                             \n")
                 //.append("                 ,   OneMMCnt / OneMMTotCnt * 100    OneMMPercent                                                                             \n")
                 .append("            SELECT   nvl(SixMMCnt / decode(OneMMTotCnt,0,null,OneMMTotCnt*100),0) SixMMPercent                                                                             \n")
                 .append("                 ,   nvl(OneMMCnt / decode(OneMMTotCnt,0,null,OneMMTotCnt*100),0) OneMMPercent                                                                             \n")
                 .append("                 ,   SixMMCnt                                                                                                                 \n")
                 .append("                 ,   OneMMCnt                                                                                                                 \n")
                 .append("                 ,   SixMMTotCnt                                                                                                              \n")
                 .append("                 ,   OneMMTotCnt                                                                                                              \n")
                 .append("                 ,   TotCnt                                                                                                                   \n")
                 .append("                 ,   SelReturn                                                                                                                \n")
                 .append("             FROM    (                                                                                                                        \n")
                 .append("                         SELECT   SelReturn                                                                                                   \n")
                 .append("                          ,   COUNT(                                                                                                          \n")
                 .append("                                     CASE WHEN        TO_CHAR(TO_DATE(LDate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')                                 \n")
                 .append("                                              BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -6), 'YYYYMMDD') AND TO_CHAR(SYSDATE, 'YYYYMMDD')           \n")
                 .append("                                          THEN        1                                                                                       \n")
                 .append("                                      END                                                                                                     \n")
                 .append("                                   )                  SixMMCnt                                                                                \n")
                 .append("                          ,   NVL(SUM(COUNT(                                                                                                  \n")
                 .append("                                         CASE WHEN        TO_CHAR(TO_DATE(LDate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')                             \n")
                 .append("                                                  BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -6), 'YYYYMMDD') AND TO_CHAR(SYSDATE, 'YYYYMMDD')       \n")
                 .append("                                              THEN        1                                                                                   \n")
                 .append("                                          END                                                                                                 \n")
                 .append("                                       )                                                                                                      \n")
                 .append("                                 ) OVER(), 0)             SixMMTotCnt                                                                         \n")
                 .append("                          ,   COUNT(                                                                                                          \n")
                 .append("                                     CASE WHEN        TO_CHAR(TO_DATE(LDate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')                                 \n")
                 .append("                                              BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYYMMDD') AND TO_CHAR(SYSDATE, 'YYYYMMDD')           \n")
                 .append("                                          THEN        1                                                                                       \n")
                 .append("                                      END                                                                                                     \n")
                 .append("                                   )                  OneMMCnt                                                                                \n")
                 .append("                          ,   NVL(SUM(COUNT(                                                                                                  \n")
                 .append("                                         CASE WHEN        TO_CHAR(TO_DATE(LDate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')                             \n")
                 .append("                                                  BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYYMMDD') AND TO_CHAR(SYSDATE, 'YYYYMMDD')       \n")
                 .append("                                              THEN        1                                                                                   \n")
                 .append("                                          END                                                                                                 \n")
                 .append("                                       )                                                                                                      \n")
                 .append("                                  ) OVER(), 0)        OneMMTotCnt                                                                             \n")
                 .append("                          ,   SUM(COUNT(*)) OVER()    TotCnt                                                                                  \n")
                 .append("                         FROM    Tz_StOld_Comments                                                                                            \n")
                 .append("                         WHERE    Subj = " + SQLString.Format(v_subj) + "                                                                     \n")
                 .append("                         GROUP BY SelReturn                                                                                                   \n")
                 .append("                      ORDER BY 2 DESC, 1 DESC                                                                                                 \n")
                 .append("                     )                                                                                                                        \n")
                 .append("             WHERE    ROWNUM = 1                                                                                                              \n")
                 .append("          UNION ALL                                                                                                                           \n")
                 .append("          SELECT  0 SixMMPercent, 0 OneMMPercent, 0 SixMMCnt, 0 OneMMCnt, 0 SixMMTotCnt, 0 OneMMTotCnt, 0 TotCnt, '0' SelReturn FROM DUAL     \n")
                 .append("      )                                                                                                                                       \n")
                 .append(" WHERE    ROWNUM < 2                                                                                                                          \n");

            //System.out.println(this.getClass().getName() + "." + "SelectStOldStatisafactInfo() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
            ls  = connMgr.executeQuery(sbSQL.toString());
                
            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
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
        
        return dbox;
    }
    
    
    /**
    집합과정 성적관리
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectOffStudentList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        ListSet             ls                  = null;
        String				sql            		= "";
        ArrayList           list                = new ArrayList();
                                    
        int                 iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        DataBox             dbox                = null;
                                                
        String              v_grcode            = box.getStringDefault  ("s_grcode"       , "ALL");
        String              v_gyear             = box.getString         ("s_gyear"               );
        String              v_grseq             = box.getStringDefault  ("s_grseq"        , "ALL");
        String              v_subj              = box.getStringDefault  ("s_subjcourse"   , "ALL");
        String              v_subjseq           = box.getStringDefault  ("s_subjseq"      , "ALL");
        
        
        try { 
            connMgr = new DBConnectionManager();
        
            sql = "\n select a.year, to_number(trim(a.subjseq)) subjseq "
            	+ "\n      , a.subj "
            	+ "\n      , a.subjnm "
            	+ "\n      , b.userid "
            	+ "\n      , c.name "
            	+ "\n      , round(b.tstep, 2) tstep "
            	+ "\n      , round(b.ftest, 2) ftest "
            	+ "\n      , round(b.report, 2) report "
            	+ "\n      , round(b.etc1, 2) etc1 "
            	+ "\n      , get_compnm(c.comp) as companynm "
            	+ "\n      , get_deptnm(c.dept_cd) as deptnm "
            	+ "\n      , get_postnm(c.post) as jikwinm "
            	+ "\n from   vz_scsubjseq  a "
            	+ "\n      , tz_student b "
            	+ "\n      , tz_member c "
            	+ "\n where  a.year = b.year "
            	+ "\n and    a.subj = b.subj "
            	+ "\n and    a.subjseq = b.subjseq "
            	+ "\n and    b.userid  = c.userid "
        		+ "\n and    a.isonoff = 'OFF' ";    
            
            if ( !v_grcode.equals("ALL") && !v_grcode.equals("----") ) { 
            	sql += "\n and a.grcode  = " + SQLString.Format(v_grcode );
            }
            
            if ( !v_gyear.equals("ALL") ) { 
            	sql += "\n and a.gyear          = " + SQLString.Format(v_gyear  );
            }
            
            if ( !v_grseq.equals("ALL") ) { 
            	sql += "\n and a.grseq          = " + SQLString.Format(v_grseq  );
            }
            
            if ( !v_subj.equals("ALL") ) { 
            	sql += "\n and a.scsubj         = " + SQLString.Format(v_subj   );
            }
            
            if ( !v_subjseq.equals("ALL") ) { 
            	sql += "\n and a.scsubjseq      = " + SQLString.Format(v_subjseq);
            }
            
            sql += "\n order  by c.name, b.userid ";
                
            //System.out.println("sql = " + sql);
            ls  = connMgr.executeQuery(sql);
                
            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                dbox.put("d_ftest" , new Double(ls.getDouble("ftest")) );
                dbox.put("d_report", new Double(ls.getDouble("report")));
                dbox.put("d_tstep" , new Double(ls.getDouble("tstep")) );
                dbox.put("d_etc1"  , new Double(ls.getDouble("etc1"))  );
                
                list.add(dbox); 
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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
    집합과정일괄처리 리스트 화면 엑셀 다운로드
    @param box          receive from the form object and session
    @return ArrayList
    */
    public int ExcelDownSelectOffStudentList(RequestBox box, ArrayList list) throws Exception { 
		DataBox dbox = null;
		String fileNm = "집합과정일괄처리.xls";
		ConfigSet conf = new ConfigSet(); 
		int retCnt = 0;
		
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(conf.getProperty("dir.upload.default") + fileNm)); // 파일이름을 정하여 생성한다.
			
			WritableSheet sheet = workbook.createSheet("Sheet1", 0); // WritableSheet는 인터페이스

		    WritableCellFormat numberFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
		    WritableCellFormat dataFormat = new WritableCellFormat(); // 번호 셀 포멧 생성

		    // 번호 레이블 셀 포멧 구성(자세한 것은 링크 된 API를 참조하셈) 참고사항 : 여기 부분에서 WriteException이 발생하네요.
		    // 그러나 상단에서 미리 예외 처리를 해서 상관 없겠네요.
		
		    numberFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		    numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		  
			// 쉬트의 컬럼 넓이 설정
			
		    sheet.setColumnView( 0, 10); // 쉬트의 번호 컬럼(0번째)의 넓이 설정. setCloumnView(몇번째 컬럼, 넓이)
			sheet.setColumnView( 1, 10); // 쉬트의 이름 컬럼(1번째)의 넓이 설정
			sheet.setColumnView( 2, 10); // 쉬트의 비고 컬럼(2번째)의 넓이 설정
			sheet.setColumnView( 3, 10); // 쉬트의 비고 컬럼(3번째)의 넓이 설정
			sheet.setColumnView( 4, 10); // 쉬트의 비고 컬럼(4번째)의 넓이 설정
			sheet.setColumnView( 5, 10); // 쉬트의 비고 컬럼(5번째)의 넓이 설정
			sheet.setColumnView( 6, 10); // 쉬트의 비고 컬럼(6번째)의 넓이 설정
 
		    // 라벨을 이용하여 해당 셀에 정보 넣기 시작
			
			sheet.addCell(new Label(0, 0, "학습자 ID",		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(1, 0, "기수",			numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(2, 0, "성명",			numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(3, 0, "출석률점수",		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(4, 0, "평가점수", 	    numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(5, 0, "리포트점수",		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(6, 0, "참여도점수",		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입

		    retCnt = list.size();
			if(list!=null && retCnt > 0) {

				for(int i = 0; i < retCnt; i++) {
					dbox  = (DataBox)list.get(i);

					sheet.addCell(new Label( 0, i+1, dbox.getString("d_userid"),	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 1, i+1, dbox.getString("d_name"),		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 2, i+1, dbox.getString("d_subjseq"),	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 3, i+1, dbox.getString("d_tstep"),		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 4, i+1, dbox.getString("d_ftest"), 	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 5, i+1, dbox.getString("d_report"),	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 6, i+1, dbox.getString("d_etc1"),		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				}
			}
		    workbook.write();
		    workbook.close();
		}            
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex);
		}
		return retCnt;
    }

    /**
    개인집합과목 점수 업데이트
    @param box          receive from the form object and session
    @return int
    */
    public int UpdateOffSubjPerson(RequestBox box) throws Exception {
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 1;
        
        int                 iSysAdd             = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        int                 iIdx                = 0;
        
        DataBox             dbox                = null;
        
        SubjseqData         data                = null;
                                                
        String              v_subj              = box.getString("p_subj"    );
        String              v_year              = box.getString("p_year"    );
        String              v_subjseq           = box.getString("p_subjseq" );
        String              v_userid            = box.getString("p_userid"  );
        double              v_tstep             = box.getDouble("p_tstep"   );
        double              v_ftest             = box.getDouble("p_ftest"   );
        double              v_report            = box.getDouble("p_report"  );
        double              v_etc1              = box.getDouble("p_etc1"    );
        double              v_etc2              = 0.0;
        double              v_htest             = 0.0;
        double              v_mtest             = 0.0;
        double              v_act               = 0.0;
        double              v_score             = 0.0;
        
        double              v_avtstep           = 0.0;
        double              v_avftest           = 0.0;
        double              v_avreport          = 0.0;
        double              v_avetc1            = 0.0;
        double              v_avetc2            = 0.0;
        double              v_avhtest           = 0.0;
        double              v_avmtest           = 0.0;
        double              v_avact             = 0.0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            data            = getSubjseqInfo(connMgr, v_subj, v_year, StringUtil.lpad(v_subjseq, "0", 4));
            
            v_avtstep       = ((double)Math.round(v_tstep   * data.getWstep()     ) / 100);
            v_avreport      = ((double)Math.round(v_report  * data.getWreport()   ) / 100);
            v_avmtest       = ((double)Math.round(v_mtest   * data.getWmtest()    ) / 100);
            v_avftest       = ((double)Math.round(v_ftest   * data.getWftest()    ) / 100);
            v_avhtest       = ((double)Math.round(v_htest   * data.getWhtest()    ) / 100);
            v_avact         = ((double)Math.round(v_act     * data.getWact()      ) / 100);
            v_avetc1        = ((double)Math.round(v_etc1    * data.getWetc1()     ) / 100);
            v_avetc2        = ((double)Math.round(v_etc2    * data.getWetc2()     ) / 100);
            
            v_score         = v_avtstep + v_avreport + v_avmtest + v_avftest + v_avhtest + v_avact + v_avetc1 + v_avetc2;
            
            if ( v_score > 100 )
                v_score     = 100.0;
            
            sbSQL.append(" Update Tz_Student SET                \n")
                 .append("         tstep    = ?                 \n")
                 .append("     ,   ftest    = ?                 \n")
                 .append("     ,   report   = ?                 \n")
                 .append("     ,   etc1     = ?                 \n")
                // .append("     ,   mtest    = ?                 \n")
                 .append("     ,   htest    = ?                 \n")
                 .append("     ,   act      = ?                 \n")
                 .append("     ,   etc2     = ?                 \n")
                 .append("     ,   avtstep  = ?                 \n")
                 .append("     ,   avftest  = ?                 \n")
                 .append("     ,   avreport = ?                 \n")
                 .append("     ,   avetc1   = ?                 \n")
                 .append("     ,   avmtest  = ?                 \n")
                 .append("     ,   avhtest  = ?                 \n")
                 .append("     ,   avact    = ?                 \n")
                 .append("     ,   avetc2   = ?                 \n")
                 .append("     ,   score    = ?                 \n")
                 .append(" WHERE   UserId  = ?                  \n")
                 .append(" AND     Year    = ?                  \n")
                 .append(" AND     Subj    = ?                  \n")
                 .append(" AND     Subjseq = LPad(?, 4, '0')    \n");
    
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setDouble( ++iIdx, v_tstep    );
            pstmt.setDouble( ++iIdx, v_ftest    );
            pstmt.setDouble( ++iIdx, v_report   );
            pstmt.setDouble( ++iIdx, v_etc1     );
           // pstmt.setDouble( ++iIdx, v_mtest    );
            pstmt.setDouble( ++iIdx, v_htest    );
            pstmt.setDouble( ++iIdx, v_act      );
            pstmt.setDouble( ++iIdx, v_etc2     );
            pstmt.setDouble( ++iIdx, v_avtstep  );
            pstmt.setDouble( ++iIdx, v_avftest  );
            pstmt.setDouble( ++iIdx, v_avreport );
            pstmt.setDouble( ++iIdx, v_avetc1   );
            pstmt.setDouble( ++iIdx, v_avmtest  );
            pstmt.setDouble( ++iIdx, v_avhtest  );
            pstmt.setDouble( ++iIdx, v_avact    );
            pstmt.setDouble( ++iIdx, v_avetc2   );
            pstmt.setDouble( ++iIdx, v_score    );
            pstmt.setString( ++iIdx, v_userid   );
            pstmt.setString( ++iIdx, v_year     );
            pstmt.setString( ++iIdx, v_subj     );
            pstmt.setString( ++iIdx, v_subjseq  );
            /*
            System.out.println(" v_tstep    : " + v_tstep    );
            System.out.println(" v_ftest    : " + v_ftest    );
            System.out.println(" v_report   : " + v_report   );
            System.out.println(" v_etc1     : " + v_etc1     );
            System.out.println(" v_mtest    : " + v_mtest    );
            System.out.println(" v_htest    : " + v_htest    );
            System.out.println(" v_act      : " + v_act      );
            System.out.println(" v_etc2     : " + v_etc2     );
            System.out.println(" v_avtstep  : " + v_avtstep  );
            System.out.println(" v_avftest  : " + v_avftest  );
            System.out.println(" v_avreport : " + v_avreport );
            System.out.println(" v_avetc1   : " + v_avetc1   );
            System.out.println(" v_avmtest  : " + v_avmtest  );
            System.out.println(" v_avhtest  : " + v_avhtest  );
            System.out.println(" v_avact    : " + v_avact    );
            System.out.println(" v_avetc2   : " + v_avetc2   );
            System.out.println(" v_score    : " + v_score    );
            System.out.println(" v_userid   : " + v_userid   );
            System.out.println(" v_year     : " + v_year     );
            System.out.println(" v_subj     : " + v_subj     );
            System.out.println(" v_subjseq  : " + v_subjseq  );
            
            System.out.println(" v_userid    : " + v_userid    );
            System.out.println(" v_year      : " + v_year      );
            System.out.println(" v_subj      : " + v_subj      );
            System.out.println(" v_subjseq   : " + v_subjseq   );
             */
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            connMgr.commit();
        } catch ( SQLException e ) {
            try { connMgr.rollback(); } catch ( SQLException e1 ) {} 
            isOk = 0;
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            try { connMgr.rollback(); } catch ( SQLException e1 ) {} 
            isOk = 0;
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
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

        return isOk;
    }
    
    /**
    수료여부 수정
    @param box          receive from the form object and session
    @return int
    */
    public int updateStudentGraduated(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
        int                 iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            sbSQL.append(" update tz_student     	\n")
                 .append(" set    isgraduated = 'N' \n")
                 .append(" where  subj    = ?     	\n")
                 .append(" and    year    = ?     	\n")
                 .append(" and    subjseq = ?     	\n");
                 
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString( 1, p_subj      );
            pstmt.setString( 2, p_year      );
            pstmt.setString( 3, p_subjseq   );
            
            isOk    = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            isOk    = -1;
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = -1;
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }

    /**
     * 교수가점 저장
     * @param box
     * @return
     * @throws Exception 
     */
	public int UpdateActScore(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int isOk1 = 0;
        int isOk2 = 0;
        
        Vector v_id      = new Vector();
        Vector v_act     = new Vector();
        v_id             = box.getVector("p_id");
        v_act            = box.getVector("p_act");
        Enumeration em1  = v_id.elements();
        Enumeration em2  = v_act.elements();
        
        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");

        int pidx = 1;
        double p_act = 0;
        String p_id = "";
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.append(" update tz_student     	\n")
                 .append(" set    act = ?		 	\n")
                 .append(" where  subj    = ?     	\n")
                 .append(" and    year    = ?     	\n")
                 .append(" and    subjseq = ?     	\n")
                 .append(" and    userid  = ?     	\n");

            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            
            while ( em1.hasMoreElements() ) { 
            	pidx = 1;
            	p_id  = (String)em1.nextElement();
            	p_act = (double)Math.round(Double.parseDouble((String)em2.nextElement()));
            	
	            pstmt.setDouble(pidx++, p_act);
	            pstmt.setString(pidx++, v_subj   );
	            pstmt.setString(pidx++, v_year   );
	            pstmt.setString(pidx++, v_subjseq);
	            pstmt.setString(pidx++, p_id);
	            
	            isOk1 += pstmt.executeUpdate();
	            isOk2 = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.ACTIVITY, v_subj, v_year, v_subjseq, p_id);
            }            
            if ( pstmt != null ) { pstmt.close(); }

            connMgr.commit();
            
        } catch ( SQLException e ) {
            try { connMgr.rollback(); } catch ( SQLException e1 ) {} 
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            try { connMgr.rollback(); } catch ( SQLException e1 ) {} 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
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

        return isOk1;
	}

    /**
     * 토론참여도 저장
     * @param box
     * @return
     * @throws Exception 
     */
	public int UpdateEtc2Score(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int isOk1 = 0;
        int isOk2 = 0;
        
        Vector v_id      = new Vector();
        Vector v_etc1    = new Vector();        
        Vector v_etc2    = new Vector();
        v_id             = box.getVector("p_id");
        v_etc1           = box.getVector("p_etc1");        
        v_etc2           = box.getVector("p_etc2");
        Enumeration em  = v_id.elements();
        Enumeration em1  = v_etc1.elements();
        Enumeration em2  = v_etc2.elements();
        
        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");

        int pidx = 1;
        double p_etc1 = 0;        
        double p_etc2 = 0;
        String p_id = "";
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.append(" update tz_student     	\n")
                 .append(" set    etc1 = ?		 	\n")            
                 //.append(" set    etc2 = ?		 	\n")
                 .append(" where  subj    = ?     	\n")
                 .append(" and    year    = ?     	\n")
                 .append(" and    subjseq = ?     	\n")
                 .append(" and    userid  = ?     	\n");
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            while ( em1.hasMoreElements() ) { 
            	pidx = 1;
            	p_id  = (String)em.nextElement();
            	p_etc1 = (double)Math.round(Double.parseDouble((String)em1.nextElement()));            	
            	//p_etc2 = (double)Math.round(Double.parseDouble((String)em2.nextElement()));
            	
	            pstmt.setDouble(pidx++, p_etc1);
	            //pstmt.setDouble(pidx++, p_etc2);
	            pstmt.setString(pidx++, v_subj   );
	            pstmt.setString(pidx++, v_year   );
	            pstmt.setString(pidx++, v_subjseq);
	            pstmt.setString(pidx++, p_id);
	            
	            isOk1 += pstmt.executeUpdate();
	            isOk2 = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.ETC, v_subj, v_year, v_subjseq, p_id);
            }            
            if ( pstmt != null ) { pstmt.close(); }

            connMgr.commit();
            
        } catch ( SQLException e ) {
            try { connMgr.rollback(); } catch ( SQLException e1 ) {} 
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            try { connMgr.rollback(); } catch ( SQLException e1 ) {} 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
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

        return isOk1;
	}
	
    /**
     * 리포트 제출여부
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public String getSubmitReportYn(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet ls = null;
        String sql = "";
        String v_retVal = "";
        
        try { 
        	sql = "\n select c.subj "
        	    + "\n      , c.year "
        	    + "\n      , c.subjseq "
        	    + "\n      , c.userid "
        	    + "\n      , nvl(a.proj_cnt,0) "
        	    + "\n      , nvl(b.user_cnt, 0) as user_cnt "
        	    + "\n      , case when nvl(a.proj_cnt,0) > nvl(b.user_cnt, 0) then 'N' "
        	    + "\n             else 'Y' "
        	    + "\n        end as proj_flag "
        	    + "\n from  (select subj "
        	    + "\n             , year "
        	    + "\n             , subjseq "
        	    + "\n             , count(distinct projgubun) as proj_cnt "
        	    + "\n        from   tz_projgrp "
        	    + "\n        group by "
        	    + "\n               subj "
        	    + "\n             , year "
        	    + "\n             , subjseq) a "
        	    + "\n      ,(select subj "
        	    + "\n             , year "
        	    + "\n             , subjseq "
        	    + "\n             , projid as userid "
        	    + "\n             , count(distinct grpseq) as user_cnt "
        	    + "\n        from   tz_projrep "
        	    + "\n        group by "
        	    + "\n               subj "
        	    + "\n             , year "
        	    + "\n             , subjseq "
        	    + "\n             , projid) b "
        	    + "\n      , tz_student c "
        	    + "\n where  c.subj = b.subj (+) "
        	    + "\n and    c.year = b.year (+) "
        	    + "\n and    c.subjseq = b.subjseq (+) "
        	    + "\n and    c.userid = b.userid (+) "
        	    + "\n and    c.subj = a.subj (+) "
        	    + "\n and    c.year = a.year (+) "
        	    + "\n and    c.subjseq = a.subjseq (+) " 
        	    + "\n and    c.subj = " + SQLString.Format(p_subj)
        	    + "\n and    c.subj = " + SQLString.Format(p_year)
        	    + "\n and    c.subj = " + SQLString.Format(p_subjseq)
        	    + "\n and    c.userid = " + SQLString.Format(p_userid);
        	
            ls  = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
            	v_retVal = ls.getString("proj_flag");
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
        }

        return v_retVal;
    }

    /**
     * 평가 제출여부
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public String getSubmitExamYn(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet ls = null;
        String sql = "";
        String v_retVal = "";
        
        try { 
                   
        	sql = "\n select c.subj "
        	    + "\n      , c.year "
        	    + "\n      , c.subjseq "
        	    + "\n      , c.userid "
        	    + "\n      , nvl(a.exam_cnt,0) "
        	    + "\n      , nvl(b.user_cnt, 0) as user_cnt "
        	    + "\n      , case when nvl(a.exam_cnt,0) > nvl(b.user_cnt, 0) then 'N' "
        	    + "\n             else 'Y' "
        	    + "\n        end as exam_flag "
        	    + "\n from  (select subj "
        	    + "\n             , year "
        	    + "\n             , subjseq "
        	    + "\n             , count(papernum) as exam_cnt "
        	    + "\n        from   tz_exampaper "
        	    + "\n        group by "
        	    + "\n               subj "
        	    + "\n             , year "
        	    + "\n             , subjseq) a "
        	    + "\n      ,(select subj "
        	    + "\n             , year "
        	    + "\n             , subjseq "
        	    + "\n             , userid "
        	    + "\n             , count(papernum) as user_cnt "
        	    + "\n        from   tz_examresult "
        	    + "\n        group by "
        	    + "\n               subj "
        	    + "\n             , year "
        	    + "\n             , subjseq "
        	    + "\n             , userid) b "
        	    + "\n      , tz_student c "
        	    + "\n where  c.subj = b.subj (+) "
        	    + "\n and    c.year = b.year (+) "
        	    + "\n and    c.subjseq = b.subjseq (+) "
        	    + "\n and    c.userid = b.userid (+) "
        	    + "\n and    c.subj = a.subj (+) "
        	    + "\n and    c.year = a.year (+) "
        	    + "\n and    c.subjseq = a.subjseq (+) " 
        	    + "\n and    c.subj = " + SQLString.Format(p_subj)
        	    + "\n and    c.subj = " + SQLString.Format(p_year)
        	    + "\n and    c.subj = " + SQLString.Format(p_subjseq)
        	    + "\n and    c.userid = " + SQLString.Format(p_userid);
        	
            ls  = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
            	v_retVal = ls.getString("exam_flag");
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
        }

        return v_retVal;
    }

    /**
     * 학습횟수 수료여부
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public String getStudyCountYn(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, SubjseqData subjseqdata) throws Exception { 
        ListSet ls = null;
        String sql = "";
        String v_retVal = "";
        
        try { 
                   
            
        	sql = "\n select decode(sign(count(*)),1,'N','Y') as flag "
        		+ "\n from   tz_student a "
        		+ "\n where  a.subj = " + SQLString.Format(p_subj)
        		+ "\n and    a.year = " + SQLString.Format(p_year)
        		+ "\n and    a.subjseq = " + SQLString.Format(p_subjseq)
        		+ "\n and    a.userid = " + SQLString.Format(p_userid)
        		+ "\n and    nvl(a.study_count,0) < " + subjseqdata.getStudy_count();
        	ls  = connMgr.executeQuery(sql);

        	while(ls.next()) {
        		v_retVal = ls.getString("flag");
        	}
        	
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
        }

        return v_retVal;
    }
    
    /**
     * 접속시간 수료여부
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public String getStudyTimeYn(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, SubjseqData subjseqdata) throws Exception { 
        ListSet ls = null;
        String sql = "";
        String v_retVal = "";
        
        try { 
                   
            
        	sql = "\n select decode(sign(count(*)),1,'N','Y') as flag "
        		+ "\n from   tz_student a "
        		+ "\n where  a.subj = " + SQLString.Format(p_subj)
        		+ "\n and    a.year = " + SQLString.Format(p_year)
        		+ "\n and    a.subjseq = " + SQLString.Format(p_subjseq)
        		+ "\n and    a.userid = " + SQLString.Format(p_userid)
        		+ "\n and    nvl(a.study_time,0) < " + subjseqdata.getStudy_time();
        	ls  = connMgr.executeQuery(sql);

        	while(ls.next()) {
        		v_retVal = ls.getString("flag");
        	}
        	
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
        }

        return v_retVal;
    }

    /**
     * 학습횟수 재계산
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public int calc_studycount(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, SubjseqData subjseqData, StoldData data) throws Exception { 
        ListSet ls = null;
        String sql = "";
        int isOk = 0;
        
        try { 
                   
            
        	sql = "\n update tz_student "
        		+ "\n set    study_count = get_studycount(subj, year, subjseq, userid, "+StringManager.makeSQL(subjseqData.getEdustart())+", "+StringManager.makeSQL(subjseqData.getEduend())+") "
        		+ "\n where  subj = " + SQLString.Format(p_subj)
        		+ "\n and    year = " + SQLString.Format(p_year)
        		+ "\n and    subjseq = " + SQLString.Format(p_subjseq)
        		+ "\n and    userid = " + SQLString.Format(p_userid);
        	isOk = connMgr.executeUpdate(sql);
        	
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }

    /**
     * 접속시간 재계산
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public int calc_studytime(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, SubjseqData subjseqData, StoldData data) throws Exception { 
        ListSet ls = null;
        String sql = "";
        int isOk = 0;
        
        try { 
                   
            
        	sql = "\n update tz_student "
        		+ "\n set    study_time = get_studytime(subj, year, subjseq, userid, "+StringManager.makeSQL(subjseqData.getEdustart())+", "+StringManager.makeSQL(subjseqData.getEduend())+") "
        		+ "\n where  subj = " + SQLString.Format(p_subj)
        		+ "\n and    year = " + SQLString.Format(p_year)
        		+ "\n and    subjseq = " + SQLString.Format(p_subjseq)
        		+ "\n and    userid = " + SQLString.Format(p_userid);
        	isOk = connMgr.executeUpdate(sql);
        	
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }

    /**
     * 독서교육 수료처리
     * @param box
     * @return
     * @throws Exception
     */
    public int BookSubjectComplete(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 1;
        ArrayList           list        = null;                                 // 수료대상학생 정보 커서
        StoldData           data        = null;
        SubjseqData         subjseqdata = null;
        
        // 수료처리시 필요한 기본 변수들
        String              v_subj      = box.getString("p_subj"    );
        String              v_year      = box.getString("p_year"    );
        String              v_subjseq   = box.getString("p_subjseq" );
        String              v_userid    = "";
        String              v_luserid   = box.getSession("userid"   );
        String              v_currdate  = FormatDate.getDate("yyyyMMddHH");     // 현재시각 년 +월 +일 +시
        int                 v_biyong    = 0;                                    // 수강료
        
        // 수료번호 -  수료처리 결재완료시 수료번호를 생성한다.
        int                 v_serno_cnt = 0;
        String              v_serno     = "";
        double              v_samtotal  = 0;

        boolean             v_isexception = false;
        //      1 : "정상적으로 수료처리 되었습니다."
        //      2 : "이미 수료처리 되었습니다."
        //      3 : "과목시작후 가능합니다.
        //      4 : "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]"
        //      5 : "학습자의 리포트 중 " + String.valueOf(v_remainReportcnt) + " 개가 미채점되었습니다. \n\n수료처리취소후 점검하여 주십시오";
        //      "excaption 발생"
        String              v_return_msg  = "";
        
        String  sql = "";
        ListSet ls  = null;
        
        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // 수료처리 완료여부, 학습중 검토
            subjseqdata = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            
            if ( subjseqdata.getIsclosed().equals("Y") ) { 
                v_return_msg    = "이미 수료처리 되었습니다.";
                isOk            = 2;
                return isOk;
            // } else if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEduend() ))) { 
            } else if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEdustart() ))) { 
                // v_return_msg  = "과목종료후 가능합니다.";
                v_return_msg    = "학습시작후 가능합니다.";
                isOk            = 3;
                return isOk;
            }

            // 수료 정보 삭제
            deleteStoldTable( connMgr, v_subj, v_year, v_subjseq );
            
            // 수료 정보 입력 psmt
            sbSQL.append(" insert into tz_stold                                                                     \n")
                 .append(" (                                                                                        \n")
                 .append("         subj                                , year          , subjseq   , userid         \n")
                 .append("     ,   name                                , comp          , score     , tstep          \n")
                 .append("     ,   mtest                               , ftest         , report    , act            \n")
                 .append("     ,   etc1                                , etc2          , avtstep   , avmtest        \n")
                 .append("     ,   avftest                             , avreport      , avact     , avetc1         \n")
                 .append("     ,   avetc2                              , isgraduated   , isb2c     , edustart       \n")
                 .append("     ,   eduend                              , serno         , isrestudy , luserid        \n")
                 .append("     ,   ldate                               , htest         , avhtest   , notgraducd     \n")
                 .append("     ,   study_count                         , study_time								    \n")
                 .append("     ,   post                                , dept_cd       , job_cd		,examnum			    \n")
                 .append(" ) values (                                                                               \n")
                 .append("         ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             , ?         , ?              \n")
                 .append("     ,   to_char(sysdate, 'yyyymmddhh24miss'), ?             , ?         , ?              \n")
                 .append("     ,   ?                                   , ?             					            \n")
                 .append("     ,   ?                                   , ?             , ?			,?	            \n")
                 .append(" )                                                                                        \n");
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());

            // 학습자 커서
            list = getFinishTargetStudent(connMgr, v_subj, v_year, v_subjseq, "ALL");
            
            for ( int i = 0; i<list.size(); i++ ) { 
                data        = (StoldData)list.get(i);

                data.setWstep       ( subjseqdata.getWstep      () );
                data.setWmtest      ( subjseqdata.getWmtest     () );
                data.setWftest      ( subjseqdata.getWftest     () );
                data.setWhtest      ( subjseqdata.getWhtest     () );
                data.setWreport     ( subjseqdata.getWreport    () );
                data.setWact        ( subjseqdata.getWact       () );
                data.setWetc1       ( subjseqdata.getWetc1      () );
                data.setWetc2       ( subjseqdata.getWetc2      () );
                data.setGradscore   ( subjseqdata.getGradscore  () );
                data.setGradstep    ( subjseqdata.getGradstep   () );
                data.setGradexam    ( subjseqdata.getGradexam   () );
                data.setGradreport  ( subjseqdata.getGradreport () );
                data.setExamnum  ( subjseqdata.getExamnum () );
                
                v_userid    = data.getUserid    ();

                // 미채점 리포트 갯수 확인
                int v_remainReportcnt   = chkRemainReportBook(connMgr, v_subj, v_year, v_subjseq, v_userid);

                if ( v_remainReportcnt > 0 ) { 
                    v_isexception   = true;
                    v_return_msg    = v_userid + "학습자의 리포트 중 " + String.valueOf(v_remainReportcnt) + " 개가 미채점되었습니다.";
                    isOk            = 5;
                    return isOk;
                }
                
                // 점수 재계산
                try { 
                    calc_score_book(connMgr, "ALL", v_subj, v_year, v_subjseq, v_userid, v_luserid, data);
                } catch  ( Exception ex ) { 
                    v_isexception   = true;
                    v_return_msg    = "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]";
                    v_return_msg   += ex.getMessage();

                    isOk            = 4;
                    return  isOk;
                }
                
                // 수료정보 INSERT
                if ( data.getIsgraduated().equals("Y") ) { 
                    v_serno_cnt++;
                    
                    // 수료증번호 발급            
                    v_serno         = getCompleteSerno(connMgr, v_subj, v_year, v_subjseq, v_userid);
                    
                    // 학습자 테이블 수료='Y' update
                    updateStudentIsgraduated(connMgr, v_subj, v_year, v_subjseq, v_userid, "Y");         
                } else { 
                    v_serno = "";
                }
                
                pstmt.setString( 1, v_subj                    );
                pstmt.setString( 2, v_year                    );
                pstmt.setString( 3, v_subjseq                 );
                pstmt.setString( 4, v_userid                  );
                                                            
                pstmt.setString( 5, data.getName            ());
                pstmt.setString( 6, data.getComp            ());
                pstmt.setDouble( 7, data.getScore           ());
                pstmt.setDouble( 8, data.getTstep           ());
                pstmt.setDouble( 9, data.getMtest           ());
                                                            
                pstmt.setDouble(10, data.getFtest           ());
                pstmt.setDouble(11, data.getReport          ());
                pstmt.setDouble(12, data.getAct             ());
                pstmt.setDouble(13, data.getEtc1            ());
                                                            
                pstmt.setDouble(14, data.getEtc2            ());
                pstmt.setDouble(15, data.getAvtstep         ());
                pstmt.setDouble(16, data.getAvmtest         ());
                pstmt.setDouble(17, data.getAvftest         ());
                                                            
                pstmt.setDouble(18, data.getAvreport        ());
                pstmt.setDouble(19, data.getAvact           ());
                pstmt.setDouble(20, data.getAvetc1          ());
                pstmt.setDouble(21, data.getAvetc2          ());
                                                            
                pstmt.setString(22, data.getIsgraduated     ());
                pstmt.setString(23, data.getIsb2c           ());
                pstmt.setString(24, subjseqdata.getEdustart ());
                pstmt.setString(25, subjseqdata.getEduend   ());

                pstmt.setString(26, v_serno                   );    // 수료증번호
                pstmt.setString(27, data.getIsrestudy       ());  
                pstmt.setString(28, v_luserid                 );
                pstmt.setDouble(29, data.getHtest           ());
                pstmt.setDouble(30, data.getAvhtest         ());
                pstmt.setString(31, data.getNotgraducd      ());

                pstmt.setInt   (32, data.getStudy_count     ());
                pstmt.setInt   (33, data.getStudy_time      ());

                pstmt.setString(34, data.getPost            ());
                pstmt.setString(35, data.getDept_cd         ());
                pstmt.setString(36, data.getJob_cd          ());
                pstmt.setString(37, data.getExamnum         ());
                
                isOk    = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
                // 수료증번호 발급
                isOk    = updateStudentSerno(connMgr, v_serno, v_subj, v_year, v_subjseq, v_userid);
                
                // 수료/미수료 통보 메일 보낼 자리
                // sendFinishMail(connMgr, box, v_userid, subjseqdata.getIsonoff(), subjseqdata.getSubjnm(), data.getIsgraduated(), subjseqdata.getBiyong() );
                
           }
            
           // 수료 필드 수정 - isclosed = 'Y'
           isOk        = setCloseColumn(connMgr, v_subj, v_year, v_subjseq, "Y");
        } catch ( SQLException e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( !v_return_msg.equals("") )
                box.put("p_return_msg", v_return_msg);
                
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { }
            }
                
            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception ) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }
        
        return isOk;
    }

    /**
     * 독서교육 평가 채점안한것 구하기
     * @param connMgr
     * @param p_subj
     * @param p_year
     * @param p_subjseq
     * @param p_userid
     * @return
     * @throws Exception
     */
    public int chkRemainReportBook(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet ls1 =  null;
        String  sql = "";

        int v_allcnt = 0;
        int v_notcnt = 0;
        
        try { 
        	sql = "\n select count(*) notcnt "
        		+ "\n from   tz_bookexam_result "
                + "\n where  subj    = " + SQLString.Format(p_subj)
                + "\n and    year    = " + SQLString.Format(p_year)
                + "\n and    subjseq = " + SQLString.Format(p_subjseq)
                + "\n and    userid  = " + SQLString.Format(p_userid)
                + "\n and    nvl(marking_yn,'N') = 'N' "
                + "\n and    nvl(final_yn,'N') = 'Y' ";
                
            ls1 = connMgr.executeQuery(sql);
            while ( ls1.next() ) { 
                v_notcnt = ls1.getInt("notcnt");
            }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
        }

        return v_notcnt;

    }

    /**
     * 독서교육 점수 재계산
     * @param connMgr
     * @param p_gubun
     * @param p_subj
     * @param p_year
     * @param p_subjseq
     * @param p_userid
     * @param p_luserid
     * @param data
     * @return
     * @throws Exception
     */
    public int calc_score_book(DBConnectionManager connMgr, String p_gubun, String p_subj, String p_year, String p_subjseq, String p_userid, String p_luserid, StoldData data) throws Exception { 
        PreparedStatement   pstmt           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        ListSet             ls              = null;
        int                 isOk            = 0;
        
        String              v_contenttype   = "";
        String              v_subjgu   = "";

        try { 

            isOk = calc_report_book (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
            isOk = calc_isgraduated_book (connMgr, p_subj, p_year, p_subjseq, p_userid, data);
            
            // 점수 수정
            sbSQL.append(" update tz_student set        \n")
                 .append("         score       = ?      \n")
                 .append("     ,   tstep       = ?      \n")
                 //.append("     ,   mtest       = ?      \n")
                 .append("     ,   ftest       = ?      \n")
                 .append("     ,   htest       = ?      \n")
                 .append("     ,   report      = ?      \n")
                 .append("     ,   act         = ?      \n")
                 .append("     ,   etc1        = ?      \n")
                 .append("     ,   etc2        = ?      \n")
                 .append("     ,   avtstep     = ?      \n")
                 .append("     ,   avmtest     = ?      \n")
                 .append("     ,   avftest     = ?      \n")
                 .append("     ,   avhtest     = ?      \n")
                 .append("     ,   avreport    = ?      \n")
                 .append("     ,   avact       = ?      \n")
                 .append("     ,   avetc1      = ?      \n")
                 .append("     ,   avetc2      = ?      \n")
                 .append("     ,   luserid     = ?      \n")
                 .append("     ,   ldate       = ?      \n")
                 .append("     ,   isgraduated = ?      \n")
                 .append("     ,   mreport     = ?      \n")
                 .append("     ,   freport     = ?      \n")
                 .append(" where   subj        = ?      \n")
                 .append(" and     year        = ?      \n")
                 .append(" and     subjseq     = ?      \n")
                 .append(" and     userid      = ?      \n");
            
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setDouble( 1, data.getScore       ()  );
            pstmt.setDouble( 2, data.getTstep       ()  );
          //  pstmt.setDouble( 3, data.getMtest       ()  );
            pstmt.setDouble( 3, data.getFtest       ()  );
            pstmt.setDouble( 4, data.getHtest       ()  );
            pstmt.setDouble( 5, data.getReport      ()  );
            pstmt.setDouble( 6, data.getAct         ()  );
            pstmt.setDouble( 7, data.getEtc1        ()  );
            pstmt.setDouble( 8, data.getEtc2        ()  );
            pstmt.setDouble(9, data.getAvtstep     ()  );
            pstmt.setDouble(10, data.getAvmtest     ()  );
            pstmt.setDouble(11, data.getAvftest     ()  );
            pstmt.setDouble(12, data.getAvhtest     ()  );
            pstmt.setDouble(13, data.getAvreport    ()  );
            pstmt.setDouble(14, data.getAvact       ()  );
            pstmt.setDouble(15, data.getAvetc1      ()  );
            pstmt.setDouble(16, data.getAvetc2      ()  );
            pstmt.setString(17, p_luserid)          ;
            pstmt.setString(18, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(19, data.getIsgraduated ()  );
            pstmt.setDouble(20, data.getMreport     ()  );
            pstmt.setDouble(21, data.getFreport     ()  );
            pstmt.setString(22, p_subj                  );
            pstmt.setString(23, p_year                  );
            pstmt.setString(24, p_subjseq               );
            pstmt.setString(25, p_userid                );
            
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    /**
     * 독서교육 리포트 점수 재계산
     * @param connMgr
     * @param p_subj
     * @param p_year
     * @param p_subjseq
     * @param p_userid
     * @param data
     * @return
     * @throws Exception
     */
    public int calc_report_book(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls1         = null;
        ListSet         ls2         = null;
        StringBuffer    sbSQL       = new StringBuffer("");
        int             isOk        = 0;
        
        int             iSysAdd     = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        int    v_repcnt     = 0;
        double v_totalScore = 0;

        try { 
            /*-------- Calc  Grade of Report ------------------------------------------------------*/
        	// 독서교육 몇개월차인지 가져오기
            sbSQL.append(" select  count(*) as repcnt                               \n")
                 .append(" from    tz_proposebook a                                 \n")
                 .append(" where   subj    = " + SQLString.Format(p_subj   ) + "    \n")
                 .append(" and     year    = " + SQLString.Format(p_year   ) + "    \n")
                 .append(" and     subjseq = " + SQLString.Format(p_subjseq) + "    \n")
                 .append(" and     userid  = " + SQLString.Format(p_userid ) + "    \n");
                 
            ls1  = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls1.next() ) { 
                v_repcnt    = ls1.getInt("repcnt");
            }
            
            if ( v_repcnt == 0 ) { 
                data.setReport  (0);
                data.setAvreport(0);
            } else { 

            	sbSQL.setLength(0);
                sbSQL.append(" select  sum(nvl(totalscore,0)) as totalscore                 \n")
	                 .append(" from    tz_bookexam_result                                   \n")
	                 .append(" where   subj        = " + SQLString.Format(p_subj   ) + "    \n")
	                 .append(" and     year        = " + SQLString.Format(p_year   ) + "    \n")
	                 .append(" and     subjseq     = " + SQLString.Format(p_subjseq) + "    \n")
	                 .append(" and     userid      = " + SQLString.Format(p_userid ) + "    \n");
                
		        ls2  = connMgr.executeQuery(sbSQL.toString());

		        if ( ls2.next() ) { 
		        	v_totalScore = ls2.getDouble("totalscore");
                }

                data.setReport( Double.parseDouble(String.format("%.2f", (v_totalScore / v_repcnt))));        // 맞은점수/개월차
                data.setAvreport( (data.getReport() * data.getWreport()) / 100 );		// (맞은점수/개월차) * 가중치
                
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    
    /**
     * 독서교육 수료여부 계산
     * @param connMgr
     * @param p_subj
     * @param p_year
     * @param p_subjseq
     * @param p_userid
     * @param data
     * @return
     * @throws Exception
     */
    public int calc_isgraduated_book(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, StoldData data) throws Exception { 
        ListSet         ls1             = null;
        ListSet         ls2             = null;
        StringBuffer    sbSQL           = new StringBuffer("");
        int             isOk            = 0;
        
        int             iSysAdd         = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        SubjseqData     subjseqdata     = null;
        String          v_notgraducd    = "";

        String  sql = "";
        ListSet ls = null;
        int     v_cnt = 0;

        try { 
            /*-------- Calc  Final Grade  ----------------------------------------------------*/
            data.setScore( data.getAvreport() );
    
            if ( data.getScore() > 100 ) { 
                data.setScore(100);
            }
        
            // 과목기수 정보
            subjseqdata     = getSubjseqInfo(connMgr, p_subj, p_year, p_subjseq);
            
            // 미수료사유 코드(tz_code 의 gubun = '0028')
            // 총점 체크
            if ( data.getScore() < subjseqdata.getGradscore() ) { 
                v_notgraducd += "06,"; // 06 = 성적미달   - 총점점수 체크
            }
            
            // 리포트
            if (!getSubmitReportBook(connMgr, p_subj, p_year, p_subjseq, p_userid)) {
            	v_notgraducd += "03,"; // 03 = 리포트 미제출
            }
            
            // 리포트
            if (!getIsGradReportBook(connMgr, p_subj, p_year, p_subjseq, p_userid, subjseqdata.getGradreport())) { 
                v_notgraducd += "08,"; // 08 = 리포트점수미달
            }

            if ( !v_notgraducd.equals("") ) { 
                v_notgraducd = v_notgraducd.substring(0,v_notgraducd.length()-1);
                
            }
            
            data.setNotgraducd(v_notgraducd);
            
            if (v_notgraducd.length() == 0) { // 전체 조건에 맞으면 수료                                                                                                         
                data.setIsgraduated("Y");
            
            } else { 
                data.setIsgraduated("N");
            }
            
            // 기타 조건으로 미수료
            if ( StringManager.chkNull(data.getIsgraduated()).equals("")) { 
                data.setIsgraduated("N");
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
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
        }

        return isOk;
    }
    
    /**
     * 독서교육 개월차 리포트 수료기준에 맞는지 검사
     * @param connMgr
     * @param p_subj
     * @param p_year
     * @param p_subjseq
     * @param p_userid
     * @return
     * @throws Exception
     */
    public boolean getIsGradReportBook(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, int p_limit_score) throws Exception { 
        ListSet ls = null;
        String sql = "";
        boolean v_retVal = true;
        int cnt = 0;
        
        try { 
            sql = "\n select t1.subj "
            	+ "\n      , t1.year "
            	+ "\n      , t1.subjseq "
            	+ "\n      , t1.userid "
            	+ "\n      , t1.month "
                + "\n      , nvl(t3.totalscore,0) as totalscore "
                + "\n from   tz_proposebook t1 "
                + "\n      , tz_bookexam_paper t2 "
                + "\n      , tz_bookexam_result t3 "
                + "\n where  t1.subj = t2.subj(+) "
                + "\n and    t1.year = t2.year(+) "
                + "\n and    t1.subjseq = t2.subjseq(+) "
                + "\n and    t1.month = t2.month(+) "
                + "\n and    t1.bookcode = t2.bookcode(+) "
                + "\n and    t1.subj = t3.subj(+) "
                + "\n and    t1.year = t3.year(+) "
                + "\n and    t1.subjseq = t3.subjseq(+) "
                + "\n and    t1.month = t3.month(+) "
                + "\n and    t1.bookcode = t3.bookcode(+) "
                + "\n and    t1.userid = t3.userid(+) "
                + "\n and    t1.subj = " + StringManager.makeSQL(p_subj) 
                + "\n and    t1.year = " + StringManager.makeSQL(p_year)
                + "\n and    t1.subjseq = " + StringManager.makeSQL(p_subjseq)
                + "\n and    t1.userid = " + StringManager.makeSQL(p_userid);
        	
            ls  = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
            	// 월별 수료기준보다 작으면 미수료
            	if (ls.getDouble("totalscore") < p_limit_score) {
            		v_retVal = false;
            	}
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
        }

        return v_retVal;
    }
    
    /**
     * 독서교육 개월차 리포트 미제출했는지 체크
     * @param connMgr
     * @param p_subj
     * @param p_year
     * @param p_subjseq
     * @param p_userid
     * @return
     * @throws Exception
     */
    public boolean getSubmitReportBook(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet ls = null;
        String sql = "";
        boolean v_retVal = true;
        int cnt = 0;
        
        try { 
            sql = "\n select count(*) cnt "
                + "\n from   tz_proposebook t1 "
                + "\n      , tz_bookexam_paper t2 "
                + "\n      , tz_bookexam_result t3 "
                + "\n where  t1.subj = t2.subj "
                + "\n and    t1.year = t2.year "
                + "\n and    t1.subjseq = t2.subjseq "
                + "\n and    t1.month = t2.month "
                + "\n and    t1.bookcode = t2.bookcode "
                + "\n and    t1.subj = t3.subj(+) "
                + "\n and    t1.year = t3.year(+) "
                + "\n and    t1.subjseq = t3.subjseq(+) "
                + "\n and    t1.month = t3.month(+) "
                + "\n and    t1.bookcode = t3.bookcode(+) "
                + "\n and    t1.userid = t3.userid(+) "
                + "\n and    nvl(t3.final_yn,'N') = 'N' "
                + "\n and    t1.subj = " + StringManager.makeSQL(p_subj) 
                + "\n and    t1.year = " + StringManager.makeSQL(p_year)
                + "\n and    t1.subjseq = " + StringManager.makeSQL(p_subjseq)
                + "\n and    t1.userid = " + StringManager.makeSQL(p_userid);
        	
            ls  = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
            	if (ls.getInt("cnt") > 0) {
            		v_retVal = false;
            	}
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
        }

        return v_retVal;
    }
    
    /**
     * 독서교육 점수 재계산
     * @param box
     * @return
     * @throws Exception
     */
    public int BookSubjectCompleteRerating(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager connMgr         = null;
        ArrayList           list            = null;   // 수료대상학생 정보 커서
        StoldData           data            = null;
        SubjseqData         subjseqdata     = null;
        int                 isOk            = 1;
        
        // 수료처리시 필요한 기본 변수들
        String              v_subj          = box.getString ("p_subj"   );
        String              v_year          = box.getString ("p_year"   );
        String              v_subjseq       = box.getString ("p_subjseq");
        String              v_userid        = "";
        String              v_luserid       = box.getSession("userid"   );
                                            
        String              v_currdate      = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년 +월 +일 +시

        boolean             v_isexception   = false;
        String              v_return_msg    = "";

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료처리 완료여부, 학습중 검토
            subjseqdata = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            
            if ( subjseqdata.getIsclosed().equals("Y") ) { 
                v_return_msg    = "이미 수료처리 되었습니다.";
                isOk            = 2;
                
                return isOk;
            } else if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEdustart())) ) { 
                v_return_msg  = "학습시작후 가능합니다.";
                
                isOk            = 3;
                return isOk;
            }
            
            // 학습자 커서
            list        = getFinishTargetStudent(connMgr, v_subj, v_year, v_subjseq, "ALL");

            for ( int i = 0; i<list.size(); i++ ) { 
                data        = (StoldData)list.get(i);

                data.setWstep    (subjseqdata.getWstep()        );
                data.setWmtest   (subjseqdata.getWmtest()       );
                data.setWftest   (subjseqdata.getWftest()       );
                data.setWhtest   (subjseqdata.getWhtest()       );
                data.setWreport  (subjseqdata.getWreport()      );
                data.setWact     (subjseqdata.getWact()         );
                data.setWetc1    (subjseqdata.getWetc1()        );
                data.setWetc2    (subjseqdata.getWetc2()        );
                data.setGradscore(subjseqdata.getGradscore()    );
                data.setGradstep (subjseqdata.getGradstep()     );
                data.setGradexam (subjseqdata.getGradexam()     );
                data.setGradreport(subjseqdata.getGradreport()  );

                v_userid    = data.getUserid();

                // 점수 재계산
                try { 
                    isOk    = calc_score_book(connMgr, "ALL", v_subj, v_year, v_subjseq, v_userid, v_luserid, data);
                } catch  ( Exception ex ) { 
                    v_isexception   = true;
                    v_return_msg    = "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]";
                    v_return_msg   += ex.getMessage();

                    isOk            = 4;
                    return isOk;
                }
           }  // for end
           
           isOk = UpdateRecalcudate(connMgr, v_subj, v_year, v_subjseq);
        } catch ( Exception e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( !v_return_msg.equals(""))
                box.put("p_return_msg", v_return_msg);
                
            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }

        return isOk;        
    }

    /**
     * 독서교육 개인별 점수 재계산
     * @param box
     * @return
     * @throws Exception
     */
    public int BookSubjectCompleteReratingPerson(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager connMgr         = null;
        ArrayList           list            = null;   // 수료대상학생 정보 커서
        StoldData           data            = null;
        SubjseqData         subjseqdata     = null;
        int                 isOk            = 1;
        
        // 수료처리시 필요한 기본 변수들
        String              v_subj          = box.getString ("p_subj"   );
        String              v_year          = box.getString ("p_year"   );
        String              v_subjseq       = box.getString ("p_subjseq");
        String              v_userid        = box.getString ("p_userid" );
        String              v_luserid       = box.getSession("userid"   );
                                            
        String              v_currdate      = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년 +월 +일 +시

        boolean             v_isexception   = false;
        String              v_return_msg    = "";

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료처리 완료여부, 학습중 검토
            subjseqdata = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);
            
            if ( subjseqdata.getIsclosed().equals("Y") ) { 
                v_return_msg    = "이미 수료처리 되었습니다.";
                isOk            = 2;
                
                return isOk;
            } else if ( FormatDate.getDate2(v_currdate).before(FormatDate.getDate2(subjseqdata.getEdustart())) ) { 
                v_return_msg  = "학습시작후 가능합니다.";
                
                isOk            = 3;
                return isOk;
            }
            
            // 학습자 커서
            list        = getFinishTargetStudent(connMgr, v_subj, v_year, v_subjseq, v_userid);

            for ( int i = 0; i<list.size(); i++ ) { 
                data        = (StoldData)list.get(i);

                data.setWstep    (subjseqdata.getWstep()        );
                data.setWmtest   (subjseqdata.getWmtest()       );
                data.setWftest   (subjseqdata.getWftest()       );
                data.setWhtest   (subjseqdata.getWhtest()       );
                data.setWreport  (subjseqdata.getWreport()      );
                data.setWact     (subjseqdata.getWact()         );
                data.setWetc1    (subjseqdata.getWetc1()        );
                data.setWetc2    (subjseqdata.getWetc2()        );
                data.setGradscore(subjseqdata.getGradscore()    );
                data.setGradstep (subjseqdata.getGradstep()     );
                data.setGradexam (subjseqdata.getGradexam()     );
                data.setGradreport(subjseqdata.getGradreport()  );

                v_userid    = data.getUserid();

                // 점수 재계산
                try { 
                    isOk    = calc_score_book(connMgr, "ALL", v_subj, v_year, v_subjseq, v_userid, v_luserid, data);
                } catch  ( Exception ex ) { 
                    v_isexception   = true;
                    v_return_msg    = "수료처리 == > 점수 재산정 중 문제발생함[" + v_userid + "]";
                    v_return_msg   += ex.getMessage();

                    isOk            = 4;
                    return isOk;
                }
           }  // for end
           
           //isOk = UpdateRecalcudate(connMgr, v_subj, v_year, v_subjseq);
        } catch ( Exception e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( !v_return_msg.equals(""))
                box.put("p_return_msg", v_return_msg);
                
            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }

        return isOk;        
    }
    
    // 코스  수료처리 취소 
    public int CourseCompleteCancel(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	        = null;
        int                 isOk            = 0;
        String              v_subj          = box.getString("p_subj"    );
        String              v_year          = box.getString("p_year"    );
        String              v_subjseq       = box.getString("p_subjseq" );
        boolean             v_isexception   = false;

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 수료 정보 삭제
            isOk        = deleteCourseStoldTable(connMgr, v_subj, v_year, v_subjseq);
        } catch ( Exception e ) {
            v_isexception   = true;
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }
        
        return isOk;
    }
    
    /**
    코스 수료정보 삭제
    @param box          receive from the form object and session
    @return int
    */
    public int deleteCourseStoldTable(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
//        int                 iSysAdd = 0; // // 

        try { 
            sbSQL.append(" delete from tz_coursestold     \n")
                 .append(" where    course    = ?     \n")
                 .append(" and      cyear    = ?     \n")
                 .append(" and      courseseq = ?     \n");
                 
            //
        
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString( 1, p_subj      );
            pstmt.setString( 2, p_year      );
            pstmt.setString( 3, p_subjseq   );
            
            isOk    = pstmt.executeUpdate();
        } catch ( SQLException e ) {
            isOk    = -1;
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk    = -1;
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }
    
    public int SubjectComplete3(RequestBox box) throws Exception { 
        // DB 처리 관련 변수
        DBConnectionManager connMgr         = null;
        ListSet             ls                  = null;
        ListSet 			ls2 				= null;     
        ListSet 			ls3 				= null;
        ListSet 			ls4 				= null;
        int                 isOk            = 1;
        DataBox             dbox                = null;
        PreparedStatement   pstmt       = null;
        
        // 수료처리시 필요한 기본 변수들
        String              v_subj          = box.getString ("p_subj"   );
        String              v_year          = box.getString ("p_year"   );
        String              v_subjseq       = box.getString ("p_subjseq");
        String              v_luserid       = box.getSession("userid"   );
        boolean             v_isexception   = false;
        String              v_return_msg    = "";
        
        String sql1="";
        String sql2="";
        int mem_cnt=0;
        ArrayList           list                = new ArrayList();
        int ch_cnt=0;
        
        String sql3="";
        String sql4="";
        String sql5="";
        
        String sql6="";
        
        Vector verscore = new  Vector ();
        Vector vervar = new  Vector ();
        
        Vector verlink = new  Vector ();
        Vector veruserid = new  Vector ();
        DataBox             dbox2                = null;
        
        ListSet 			ls5 				= null;
        DataBox             dbox3                = null;
        
        Vector verscore2 = new  Vector ();
        Vector vervar2 = new  Vector ();
        ListSet 			ls6 				= null;
        ListSet 			ls7 				= null;
        
        Vector verrslt = new  Vector ();
        Vector verrsltcnt = new  Vector ();
        DataBox             dbox4                = null;
        int vcnt=0;
        
        ListSet 			ls8 				= null;
        DataBox             dbox5                = null;
        
        Vector versubj = new  Vector ();
        Vector veryear = new  Vector ();
        Vector versubjseq = new  Vector ();
        Vector veruserid2 = new  Vector ();
        Vector vereditlink = new  Vector ();
        
        String pattern = "######.##";
        DecimalFormat dformat = new DecimalFormat(pattern);        

        try { 
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            
            //1.수료처리할 학생의 총 인원수를 가지고 온다.            
            sql1="select  count(a.userid) as cnt \n";
            sql1+=" from     tz_student a \n";                                                            
            sql1+=" where     a.subj      =  " + SQLString.Format(v_subj ) + "    \n";                 
            sql1+=" and     a.year      =  " + SQLString.Format(v_year ) + "    \n";              
            sql1+=" and     a.subjseq   = " + SQLString.Format(v_subjseq ) + " \n";                
            sql1+=" and     a.score   >= 60   \n";
            sql1+=" and     a.etc2   <> 0 \n";
            
            logger.info("1. 수료처리학생인원  : " + sql1);
            
            ls  = connMgr.executeQuery(sql1);
            
            if ( ls.next() ) {
            	mem_cnt = ls.getInt(1);
            }
            ls.close();
            
            String sql11="";
            sql11="select sum(prv_num)  \n";
            sql11+=" from     tz_crt_var a \n";                                                            
            sql11+=" where     a.subj      =  " + SQLString.Format(v_subj ) + "    \n";                 
            sql11+=" and     a.year      =  " + SQLString.Format(v_year ) + "    \n";              
            sql11+=" and     a.subjseq   = " + SQLString.Format(v_subjseq ) + " \n";  
            
           // System.out.println("sql11=====>"+sql11);
            logger.info("2. 수료처리학생인원 : " + sql1);
            
            ls7  = connMgr.executeQuery(sql11);
            
            if ( ls7.next() ) {
            	vcnt = ls7.getInt(1);
            }
            ls7.close();
            
            
            //TZ_CRT_LANK 테이블에 업뎃한다.            
            sql6="select ROW_NUMBER() OVER(ORDER BY  a.score  DESC, a.avmtest DESC, a.avreport  DESC, a.avftest  DESC, a.avetc2  DESC, fn_crypt('2', b.birth_date, 'knise') asc ) as elink \n";
            sql6+=" ,a.userid ";
            sql6+=" from     tz_student a, tz_member b  \n";                                                            
            sql6+=" where   a.userid    = b.userid(+)	   \n";                 
            sql6+=" and     a.subj      =  " + SQLString.Format(v_subj ) + "    \n";
            sql6+=" and     a.year      =  " + SQLString.Format(v_year ) + "    \n";              
            sql6+=" and     a.subjseq   = " + SQLString.Format(v_subjseq ) + " \n";                
            sql6+=" and     a.score   >= 60   \n";
            sql6+=" and     a.etc2   <> 0   \n";
            
            logger.info("3. 수료처리학생인원 : " + sql6);
            ls4  = connMgr.executeQuery(sql6);
            
            while ( ls4.next() ) {
            	dbox2    = ls4.getDataBox();
            	verlink.add(dbox2.getString("d_elink"));
            	veruserid.add(dbox2.getString("d_userid"));
            }
            ls4.close();
            
            String sql7 ="update tz_student set editlink= ? where subj =?  and year =? and subjseq=? and userid =? ";
            
            logger.info("4. 수료처리학생인원 : " + sql7);
            
            
            
            pstmt = connMgr.prepareStatement(sql7);
            
            logger.info("verlink.size() :" + verlink.size());
            
            
            for(int p=0; p < verlink.size() ; p++){
     			pstmt.setString( 1, verlink.elementAt(p).toString());
     			pstmt.setString( 2, v_subj);
     			pstmt.setString( 3, v_year);
     			pstmt.setString( 4, v_subjseq);
     			pstmt.setString( 5, veruserid.elementAt(p).toString());
     			
//     			logger.info(" update tz_student set editlink =  '" + verlink.elementAt(p).toString() + "' where subj = '" + v_subj + "' and year = '" + v_year + "' and subjseq = '" + v_subjseq + "' and userid = '" + veruserid.elementAt(p).toString() + "'");
     			
     			isOk    = pstmt.executeUpdate();
            	
            }
          /*  //tz_stold 테이블에 업뎃
            String sql8 ="update tz_stold set editlink= ? where subj =?  and year =? and subjseq=? and userid =? ";
            pstmt = connMgr.prepareStatement(sql8);
            for(int p=0; p < verlink.size() ; p++){
     			pstmt.setString( 1, verlink.elementAt(p).toString());
     			pstmt.setString( 2, v_subj);
     			pstmt.setString( 3, v_year);
     			pstmt.setString( 4, v_subjseq);
     			pstmt.setString( 5, veruserid.elementAt(p).toString());
     			isOk    = pstmt.executeUpdate();
            	
            }*/
            
            //인원배정을 한다.
                       
             String sql9 =" select score,var from tz_crt_log_tbl WHERE score < 91 order by score asc ";
             
             logger.info("5. 수료처리학생인원 : " + sql9);
             
             
             ls5  = connMgr.executeQuery(sql9);
             
             while ( ls5.next() ) {
             	dbox3    = ls5.getDataBox();
             	verscore2.add(dbox3.getString("d_score"));
             	vervar2.add(dbox3.getString("d_var"));
             }
             ls5.close();
             
             String sql10 =" select rslt, count(rslt) cnt from tz_crt_var  \n";
                    sql10+=" where subj='"+v_subj+"' \n";
                    sql10+=" and year='"+v_year+"' \n";
                    sql10+=" and subjseq='"+v_subjseq+"' group by rslt  order by rslt desc \n";
            
            logger.info("6. 수료처리학생인원 : " + sql10);
             ls6  = connMgr.executeQuery(sql10);
             while ( ls6.next() ) {
             	 dbox4    = ls6.getDataBox();                 
                 verrslt.add(dbox4.getString("d_score"));
                 verrsltcnt.add(dbox4.getString("d_var"));
             }
             ls6.close();
             
             int getTotRecCnt =mem_cnt;
             int get1stCnt = vcnt;
             int gap_num = 0;
             float max_num = 0.0F;
             float min_num = 0.0F;
             float rate[] = new float[verscore2.size()];
             int rnd[] = new int[verscore2.size()];
             for(int i = 0; i < verscore2.size(); i++)
             {
                 rate[i] = Float.parseFloat(vervar2.elementAt(i).toString());
                 rnd[i] = (int)((double)((Float.parseFloat(vervar2.elementAt(i).toString()) * (float)getTotRecCnt) / 100F) + 0.5D);
                 
                // System.out.println(" rate[i]===>"+ rate[i]);
                // System.out.println(" rnd[i]===>"+ rnd[i]);
             }
             
           if(rnd[10] == 0)
                 rnd[10] = 1;
             get1stCnt = 0;
             for(int i = 0; i < 11; i++)
                 get1stCnt += rnd[i] * 2;

             get1stCnt -= rnd[10];
             int lst[] = new int[11];
             int snd[] = new int[11];
             if(get1stCnt == getTotRecCnt)
             {
                 for(int i = 0; i < 11; i++)
                     lst[i] = rnd[i];

             } else
             {
                 snd = new int[11];
                 if((getTotRecCnt - get1stCnt) % 2 != 0)
                 {
                     snd[10] = snd[10] + 1;
                     get1stCnt++;
                 }
                 float gap[] = new float[11];
                 for(int i = 0; i < 10; i++)
                     gap[i] = (float)rnd[i] - (rate[i] * (float)getTotRecCnt) / 100F;

                 if(get1stCnt - getTotRecCnt > 0)
                     for(; get1stCnt - getTotRecCnt > 0; get1stCnt -= 2)
                     {
                         gap_num = 9;
                         max_num = 0.0F;
                         for(int i = 9; i >= 0; i--)
                             if((gap[i] > max_num) & (snd[i] == 0))
                             {
                                 max_num = gap[i];
                                 gap_num = i;
                             }

                         snd[gap_num] = -1;
                     }

                 else
                     for(; get1stCnt - getTotRecCnt < 0; get1stCnt += 2)
                     {
                         gap_num = 9;
                         min_num = 0.0F;
                         for(int i = 9; i >= 0; i--)
                             if((gap[i] < min_num) & (snd[i] == 0))
                             {
                                 min_num = gap[i];
                                 gap_num = i;
                             }

                         snd[gap_num] = 1;
                     }

             }
             
             lst = new int[11];             
             for(int i = 0; i < 11; i++)
                 lst[i] = rnd[i] + snd[i];
             
             isOk    = deleteLank(connMgr,v_subj,v_year,v_subjseq );
             int lank = 1;
             for(int i = 0; i <= 10; i++)
             {
                 for(int j = 1; j < lst[i]; j++)
                	 isOk = setNum(connMgr,v_subj,v_year,v_subjseq, 100 - i, lst[i]);

                 for(int j = 0; j < lst[i]; j++)
                 {
                	 isOk  = setLank(connMgr,v_subj,v_year,v_subjseq, lank, 100 - i);
                     lank++;
                 }

             }

             for(int i = 9; i >= 0; i--)
             {
                 for(int j = 1; j <= lst[i]; j++)
                	 isOk = setNum(connMgr,v_subj,v_year,v_subjseq, 80 + i, lst[i]);

                 for(int j = 1; j <= lst[i]; j++)
                 {
                	 isOk  = setLank(connMgr,v_subj,v_year,v_subjseq,  lank, 80 + i);
                     lank++;
                 }

             }
           
        
            //2. TZ_CRT_LOG_TBL 테이블 ( 연수성적 분포 조건표 테이블)을 읽어온다.
            sql2="SELECT score, var FROM TZ_CRT_LOG_TBL ORDER BY score asc ";
            logger.info("6. 수료처리학생인원 : " + sql2);
            ls2 =  connMgr.executeQuery(sql2);
            
            while ( ls2.next() ) { 
                dbox    = ls2.getDataBox();
                verscore.add(dbox.getString("d_score"));
                vervar.add(dbox.getString("d_var"));
            }
            ls2.close();
            
            
                   sql4 ="select count(*) from tz_crt_var \n";
            	   sql4+=" where \n";
            	   sql4+=" subj=" + SQLString.Format(v_subj ) + " \n";
            	   sql4+=" and year =" + SQLString.Format(v_year ) + " \n";
            	   sql4+=" and subjseq=" + SQLString.Format(v_subjseq ) + " \n";
            	   
                  ls3  = connMgr.executeQuery(sql4);
                   
                   if ( ls3.next() ) {
                	   ch_cnt = ls3.getInt(1);
                   }
             
             if(ch_cnt > 0 ){
            	 
            	 sql5 ="delete from tz_crt_var ";
            	 sql5+=" where \n";
            	 sql5+=" subj=? \n";
            	 sql5+=" and year =? \n";
            	 sql5+=" and subjseq=?  \n";
            	 
	     			pstmt = connMgr.prepareStatement(sql5);
	     			pstmt.setString( 1, v_subj);
	     			pstmt.setString( 2, v_year);
	     			pstmt.setString( 3, v_subjseq);
	     			
	     			pstmt.executeUpdate();
             }
            
            
            //3. 1.2번의 데이터를 가지고 와서  TZ_CRT_VAR 조정표 테이블에 insert 한다.
            sql3 ="insert into tz_crt_var(SUBJ,YEAR,SUBJSEQ,SCORE,VAR,RATIO,PRV_NUM,rslt) \n";
            sql3+="values( ?, ?, ?, ?, ?, ?, ? , ? ) \n";
            
            pstmt = connMgr.prepareStatement(sql3);
            
            pstmt.setString( 1, v_subj                    );
            pstmt.setString( 2, v_year                    );
            pstmt.setString( 3, v_subjseq                 );   
            
            double d1 =0;
            double d2 =0;
            double d3 =0;
            for(int i =0; i < verscore.size(); i++){
            	
            d1 = Double.parseDouble(vervar.elementAt(i).toString());
            d2 =Double.parseDouble(vervar.elementAt(i).toString());
           	d3 =Double.parseDouble(dformat.format(d2));  
//           	System.out.println("################################################################");
//           	System.out.println("d1 : " + d1);
//           	System.out.println("d2 : " + d2);
//            System.out.println("d3 : " + d3);
//            System.out.println("mem_cnt : " + mem_cnt);
//            System.out.println("Math.round(mem_cnt* d3)/100 :: " +  Math.round(mem_cnt* d3)/100 );
//            System.out.println("(mem_cnt* d3)/100 :: " + (mem_cnt* d3)/100 );
            
          	 pstmt.setString( 4, verscore.elementAt(i).toString() );
           	 pstmt.setString( 5, vervar.elementAt(i).toString()         );
           	 pstmt.setDouble(6, Math.round(mem_cnt* d3)/100 );
           	 pstmt.setDouble(7, Math.round(mem_cnt* d1)/100 );
           	 pstmt.setDouble(8, Math.abs( Math.round(mem_cnt* d3)/100- Math.round(mem_cnt* d1)/100 ) );
           	
           	 isOk    = pstmt.executeUpdate();
            }
                        
            //저장된 lank를 가지고 tz_stold와  tz_student의 link와 edit_score에 업뎃한다.
            
            String sql13="";
            String sql12="";
            
            sql13="select a.subj,a.year,a.subjseq, a.userid, a.editlink ";
    	    sql13+="FROM tz_student a  "; 
    	    sql13+=" where     a.subj      =  " + SQLString.Format(v_subj ) + "    \n";
    	    sql13+=" and     a.year      =  " + SQLString.Format(v_year ) + "    \n";              
    	    sql13+=" and     a.subjseq   = " + SQLString.Format(v_subjseq ) + " \n";    
    	    
    	   // System.out.println("sql13=======>"+sql13);
    	    
    	    logger.info("10. 수료처리학생인원 : " + sql13);
    	    
    	    
    	    ls8 =  connMgr.executeQuery(sql13);
    	    while(ls8.next()){
    	     dbox5    = ls8.getDataBox();                 
    	     versubj.add(dbox5.getString("d_subj"));
    	     veryear.add(dbox5.getString("d_year"));
    	     versubjseq.add(dbox5.getString("d_subjseq"));
    	     veruserid2.add(dbox5.getString("d_userid"));
    	     vereditlink.add(dbox5.getString("d_editlink"));	
    	    }
    	    ls8.close();
    	    
    	    
    	    sql12="update tz_student set editscore=(select score from tz_crt_lank where subj =? and year=? and subjseq=? and lank =? ) \n";
	    	sql12+=" where subj =? \n";
	    	sql12+=" and year =?  \n";
	    	sql12+=" and subjseq =? \n";
	    	sql12+=" and userid =?  \n";
	    	
	    	pstmt = connMgr.prepareStatement(sql12);
	    	
    	    for(int i=0; i < versubj.size();i++){
    	    	
    	    	
    	    /*	sql12="update tz_student set editscore=(select score from tz_crt_lank where subj ='"+versubj.elementAt(i).toString()+"' and year='"+veryear.elementAt(i).toString()+"' and subjseq='"+ versubjseq.elementAt(i).toString()+"' and lank ='"+vereditlink.elementAt(i).toString()+"' ) \n";
    	    	sql12+=" where subj ='"+versubj.elementAt(i).toString() +"' \n";
    	    	sql12+=" and year ='"+versubjseq.elementAt(i).toString() +"'   \n";
    	    	sql12+=" and subjseq ='"+versubjseq.elementAt(i).toString() +"' \n";
    	    	sql12+=" and userid ='"+veruserid2.elementAt(i).toString() +"'   \n";
    	    	
    	    	System.out.println("sql12=============>"+i+"========>"+sql12);*/
    	    	
    	    	
    	    	 pstmt.setString( 1, versubj.elementAt(i).toString() );
    	    	 pstmt.setString( 2, veryear.elementAt(i).toString() );
    	    	 pstmt.setString( 3, versubjseq.elementAt(i).toString() );
    	    	 pstmt.setString( 4, vereditlink.elementAt(i).toString() );
    	    	 pstmt.setString( 5, versubj.elementAt(i).toString() );
    	    	 pstmt.setString( 6, veryear.elementAt(i).toString() );
    	    	 pstmt.setString( 7, versubjseq.elementAt(i).toString() );
    	    	 pstmt.setString( 8, veruserid2.elementAt(i).toString() );
    	    	 isOk    = pstmt.executeUpdate();
    	    
    	    }
    	 /*   String sql14="";
    	    sql14="update tz_stold set editscore=(select score from tz_crt_lank where subj =? and year=? and subjseq=? and lank =? ) \n";
    	    sql14+=" where subj =? \n";
    	    sql14+=" and year =?  \n";
    	    sql14+=" and subjseq =? \n";
    	    sql14+=" and userid =?  \n";
	    	
	    	pstmt = connMgr.prepareStatement(sql14);
	    	
    	    for(int i=0; i < versubj.size();i++){
    	    	
    	    	
    	    	 pstmt.setString( 1, versubj.elementAt(i).toString() );
    	    	 pstmt.setString( 2, veryear.elementAt(i).toString() );
    	    	 pstmt.setString( 3, versubjseq.elementAt(i).toString() );
    	    	 pstmt.setString( 4, vereditlink.elementAt(i).toString() );
    	    	 pstmt.setString( 5, versubj.elementAt(i).toString() );
    	    	 pstmt.setString( 6, veryear.elementAt(i).toString() );
    	    	 pstmt.setString( 7, versubjseq.elementAt(i).toString() );
    	    	 pstmt.setString( 8, veruserid2.elementAt(i).toString() );
    	    	
    	    	 isOk    = pstmt.executeUpdate();
    	    
    	    }*/
    	    
    	    
            
            
    	    
            if(isOk > 0){
            	connMgr.commit();
            }else{
            	connMgr.rollback();
            }
            

        } catch ( Exception e ) {
            v_isexception   = true;
            v_return_msg    = e.getMessage();
            //System.out.println("v_return_msg====>"+v_return_msg);
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( !v_return_msg.equals(""))
                box.put("p_return_msg", v_return_msg);
                
            if ( connMgr != null ) { 
                try { 
                    if ( v_isexception) { 
                        connMgr.rollback();
                    } else { 
                        connMgr.commit();
                    }
                    
                    connMgr.setAutoCommit(true);
                    connMgr.freeConnection();
                } catch ( Exception e ) { }
            }
        }

        return isOk;        
    }
    
    /**
    tz_student isgraduated update 
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
    public int deleteLank(DBConnectionManager connMgr, String v_subj,String v_year, String v_subjseq ) throws Exception { 
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
        int                 iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            sbSQL.append(" delete tz_crt_lank where subj =? and year=? and subjseq=?  \n");
          
            
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString(1, v_subj     );
            pstmt.setString(2, v_year     );
            pstmt.setString(3, v_subjseq     );
            
                        
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }
            
        return isOk;
    }
    
    /**
    tz_student isgraduated update 
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
    public int setNum(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq,int score, int cnt) throws Exception { 
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
        int                 iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            sbSQL.append(" update tz_crt_var set last_num = ? WHERE subj=?  and year=? and subjseq=? AND score=?   \n");
          
            
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setInt(1, cnt     );
            pstmt.setString(2, v_subj     );
            pstmt.setString(3, v_year     );
            pstmt.setString(4, v_subjseq     );
            pstmt.setInt(5, score     );
            
                        
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }
            
        return isOk;
    }
    
    /**
    tz_student isgraduated update 
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
    public int setLank(DBConnectionManager connMgr, String v_subj,String v_year, String v_subjseq,int lank, int score ) throws Exception { 
        PreparedStatement   pstmt   = null;
        StringBuffer        sbSQL   = new StringBuffer("");
        int                 isOk    = 0;
        
        int                 iSysAdd = 0; // //System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 
            sbSQL.append(" insert into tz_crt_lank (subj,year,subjseq, lank, score) values(?,?,?,?,?)   \n");
          
            
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString(1, v_subj     );
            pstmt.setString(2, v_year     );
            pstmt.setString(3, v_subjseq     );
            pstmt.setInt(4, lank     );
            pstmt.setInt(5, score     );
            
                        
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
        }
            
        return isOk;
    }
    /** 
    엑셀 점수 등록
    @param box		receive from the form object and session
    @return str_result		결과저장
    */    
    public String excelMemberInsert(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls  = null; 
        ConfigSet conf = new ConfigSet();       
        int isOk = 0;
        int isOk1= 0; 
      
        String  s_grcode     = box.getString("s_grcode");
        String  s_gyear      = box.getString("s_gyear");
        String  s_grseq      = box.getString("s_grseq");
        String  s_subjcourse     = box.getString("s_subjcourse");
        String  s_subjseq      = box.getString("s_subjseq");
        String  s_grname      = box.getString("s_grname");
        String  s_subjnm      = box.getString("s_subjnm");
        String  s_subjseqnm      = box.getString("s_subjseqnm");
       
        String  v_luserid      = box.getSession("userid");
        String  v_realFileName = box.getRealFileName("p_file");
        String  v_newFileName  = box.getNewFileName("p_file");
        
        int     i=0;
        int irows =1; // 첫번째 로우는 데이터가 들어가지 않음
        PreparedStatement   pstmt   = null;
        PreparedStatement   pstmtj   = null;
        
        /**
   	    * 데이타 검증 관련 필드
   	    */
	    int is_inputok = 0;     // 에러 유무(정상 1, 공백 0)
	    int cnt_total = 0;      // 총 카운트
	    int cnt_succ = 0;       // 총 성공 카운트
	    int cnt_error = 0;      // 총 에러 카운트
	    String v_errnme="";     // 에러명
	    
	    //수험번호 코드
	    int cnt_examnum=0;
	    String str_examnum="";
	    //점수 
	    int cnt_score=0;
	    String str_score="";
//	    System.out.println("box값  :"+box);
	   
	    //결과 
	    String str_result = "";
	    
        Cell cell = null;
        Sheet sheet = null;
        Workbook workbook = null;
        
        try {
       	    connMgr = new DBConnectionManager();
     	    connMgr.setAutoCommit(false);
     	    workbook = Workbook.getWorkbook(new File(conf.getProperty("dir.upload.default")+v_newFileName));
//            System.out.println("workbook : "+workbook);
            sheet = workbook.getSheet(0);
            
            String sql1="",sql2="",sql="";
           
            for (i=1; i < sheet.getRows(); i++ ) {
	    	    v_errnme = "";
	    	    irows ++;
	    	    cnt_total ++;
	    	    
	            int j=0;
	            int v_excnt = 0;
	            int exam=0;
	            String v_serno= "";
	            String v_userid= "";
	            String v_examnum="";
	            String v_score="";
	            
	            v_examnum  		= sheet.getCell(j,i).getContents();
	            v_examnum =  v_examnum.replace("'", "");
	            v_score				= sheet.getCell(++j,i).getContents();
	            
	            exam = Integer.parseInt(v_examnum);
	            //수험번호 확인 체크
//	            System.out.println("exam :::: "+exam);
            	if (v_examnum.equals("")) { 
	        	    is_inputok = 1;
	        	    cnt_examnum++;
	      	        str_examnum += "["+irows+"로우]<br>";
	            }else{
//	            	if(exam < 10){
//	            		v_examnum = "000"+v_examnum; 	
//		            }else if(exam < 100){
//		            	v_examnum = "00"+v_examnum; 	
//		            }else if(exam < 1000){
//		            	v_examnum = "0"+v_examnum; 	
//		            }
	            	sql  = " select max(examnum) as examnum,max(serno) as serno, userid,count(*) as cnt from (select examnum,serno,userid, subj,subjseq,year from tz_student ";
	            	sql += " where subj = '"+s_subjcourse+"' and subjseq='"+s_subjseq+"' and year='"+s_gyear+"' and examnum='"+v_examnum+"')view1 group by subj,subjseq,year,userid    \n"; 
            	
	            	System.out.println("sql1  : \n"+sql);	
	            	
	            	ls = connMgr.executeQuery(sql);
	                
	                if (ls.next()) {
	                	v_excnt = ls.getInt("cnt");
	                	v_serno = ls.getString("serno");
	                	v_userid = ls.getString("userid");
	                }
	                if (v_excnt != 1) { 
		        	    is_inputok = 1;
		        	    cnt_examnum++;
		      	        str_examnum += "["+irows+"로우의 "+v_examnum+" 의 수험번호를 확인하세요.]<br>";
		            }
	            }
            	if(ls != null) {	
             	    ls.close();
                }
	            //점수 확인 체크 
	            if (v_score.equals("")) {
	        	    is_inputok = 1;
	        	    cnt_score++;
	      	        str_score += "["+irows+"로우의 점수를 입력해 주세요.]<br>";
	            }
//	            System.out.println("is_inputok : "+is_inputok);
	            if(is_inputok == 0) {
	            	
	                sql1  = " update tz_student set   ";
//	                sql1 += " serno = ? , mtest =?  "  ; // serno = 수료번호
	                sql1 += " mtest =?  " ;
	                sql1 += " where subj = '"+s_subjcourse+"' and subjseq='"+s_subjseq+"' and year='"+s_gyear+"' and examnum='"+v_examnum+"'  \n"; 
	                
	                pstmt = connMgr.prepareStatement(sql1);
	                //pstmt.setString(1, v_examnum);
	                pstmt.setString(1, v_score);
	                isOk = pstmt.executeUpdate();
	         //   	System.out.println("update  : \n"+sql1 +"\n v_score::" +v_score );				    
	                if(pstmt != null) {	
	             	    pstmt.close();
	                }
                	//수료처리후에 처리하기로 결정함. 10.06.04
//                	sql2  = " update tz_stold set  ";
//                	sql2 += " serno = ? , mtest =?  " ;
//	                sql2 += " where subj = '"+s_subjcourse+"' and subjseq='"+s_subjseq+"' and year='"+s_gyear+"' and userid='"+v_userid+"' \n";
//	                pstmtj = connMgr.prepareStatement(sql2);
//	                pstmtj.setString(1, v_examnum);
//	                pstmtj.setString(2, v_score);
//	                isOk1 = pstmtj.executeUpdate();
//	                if(pstmtj != null) {	
//	             	    pstmtj.close();
//	                }
	                cnt_succ ++;
//	                System.out.println("sql2  : \n"+sql2);
	            }else{
	        	    cnt_error ++;
	            }
	      
	            str_result = "처리결과 <br><br>총입력 : "+cnt_total +"건  <br><br>성공: "+cnt_succ +"건 실패: "+cnt_error  +"건<br><br>";
	   	        str_result += "[오류 건수 상세 내용]<br>";
	   	        str_result += "1.수험번호 없음 :   "+cnt_examnum+"건<br>"+str_examnum+"<br><br>";       	    
	   	        str_result += "2.점수 없음 :  "+cnt_score+"건<br>"+str_score+"<br><br>";
	        }   
            if(cnt_error > 0)  {
        	    connMgr.rollback();
            }else {
   	            connMgr.commit();
   	        }
           
        } catch (Exception e) {
       	    connMgr.rollback(); 
        } finally {
    	    if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
    	    if ( pstmtj != null ) { try { pstmtj.close(); } catch ( Exception e ) { } }
    	    if(ls != null) { try { ls.close(); } catch (Exception e) {} }
      	    if(connMgr != null) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); }catch (Exception e10) {} }
        }  
        return str_result;
    }
  
}
