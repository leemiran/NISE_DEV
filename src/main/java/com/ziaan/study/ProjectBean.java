// **********************************************************
//  1. 제      목: PROJECT BEAN
//  2. 프로그램명: ProjectBean.java
//  3. 개      요: 과제 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 박진희 2003. 9. 01
//  7. 수      정:
// **********************************************************
package com.ziaan.study;
import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import com.ziaan.library.CalcUtil;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class ProjectBean { 

    public ProjectBean() { }

    /**
    과제와 액티비티 존재 여부
    @param box      receive from the form object and session
    @return ProjectData
    */
     public int selectChoicePage(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        String sql1         = "";
        String sql2         = "";
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_user_id   = box.getSession("userid");
        int     result      = 0;     // 1:과제만 있음,2:액티비티만 있음,3:과제와 액티비티가 있음
        try { 
            connMgr = new DBConnectionManager();

            // select from TZ_PROJORD
            sql1 = "select count(A.subj) cnt ";
            sql1 += "from TZ_PROJORD A ";
            sql1 += "where A.subj='" +v_subj + "' and A.year='" +v_year + "' and A.subjseq='" +v_subjseq + "' ";

            ls1 = connMgr.executeQuery(sql1);
            if ( ls1.next() && ls1.getInt("cnt") > 0 ) { 
                result = 1;     // 과제가 있는 경우
            }
//            System.out.println("result ==  ==  ==  ==  == > " +result);
            // select from TZ_ACTIVITY
            // sql2 = "select count(A.subj)  cnt ";
            // sql2 += "from TZ_ACTIVITY A ";
            // sql2 += "where A.subj='" +v_subj + "' ";
//            System.out.println("sql2 ==  ==  ==  ==  ==  == > " +sql2);
            // ls2 = connMgr.executeQuery(sql2);
            // if ( ls2.next() && ls2.getInt("cnt") > 0 ) { 
            //    if ( result == 1) {   result = 3;   // 과제와 액티비티가 있는 경우
            //    } else {             result = 2;  }// 액티비티만 있는 경우
            // }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

    /**
    학습창 과제 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectProjectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        ProjectData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_user_id   = box.getSession("userid");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // sql = "select   ordseq, \n";
            // sql += "         projseq, \n";
            // sql += "         lesson,\n";
            // sql += "         title,\n";
            // sql += "         score,\n";
            // sql += "         reptype,\n";
            // sql += "         projgrp,\n";
            // sql += "         score2,\n";
            // sql += "         cnt, \n";
            // sql += "         isret, \n";
            // sql += "         isfinal \n";
            // sql += "from    (select  A.ordseq, A.projseq, \n";
            // sql += "                 A.lesson,\n";
            // sql += "                 A.title,\n";
            // sql += "                 A.score,\n";
            // sql += "                 A.reptype,\n";
            // sql += "                 nvl((select isret \n";
            // sql += "                  from   tz_projrep \n";
            // sql += "                  where  subj=a.subj and \n";
            // sql += "                         year=a.year and \n";
            // sql += "                         subjseq=a.subjseq and ordseq=a.ordseq and projid=b.userid),'N') isret, \n";
            // sql += "                 nvl((select isfinal \n";
            // sql += "                  from   tz_projrep \n";
            // sql += "                  where  subj=a.subj and \n";
            // sql += "                         year=a.year and \n";
            // sql += "                         subjseq=a.subjseq and ordseq=a.ordseq and projid=b.userid),'N') isfinal, \n";
            // sql += "                (select  projgrp \n";
            // sql += "                 from    TZ_PROJGRP \n";
            // sql += "                 where   subj    = A.subj    and \n";
            // sql += "                         year    = A.year    and \n";
            // sql += "                         subjseq = A.subjseq and \n";
            // sql += "                         ordseq  = A.ordseq  and \n";
            // sql += "                         userid  = '" + v_user_id + "') projgrp, \n";
            // sql += "                (select  score \n";
            // sql += "                 from    TZ_PROJREP \n";
            // sql += "                 where   subj    = A.subj    and \n";
            // sql += "                         year    = A.year    and \n";
            // sql += "                         subjseq = A.subjseq and \n";
            // sql += "                         ordseq  = A.ordseq  and \n";
            // sql += "                         projid  = '" + v_user_id + "') score2, \n";
            // sql += "                (select  count(subj) \n";
            // sql += "                 from    TZ_PROJREP \n";
            // sql += "                 where   subj    = A.subj    and \n";
            // sql += "                         year    = A.year    and \n";
            // sql += "                         subjseq = A.subjseq and \n";
            // sql += "                         ordseq  = A.ordseq  and \n";
            // sql += "                         projid  = '" + v_user_id + "') cnt \n";
            // sql += "         from    TZ_PROJORD A ,\n";
            // sql += "                 TZ_PROJASSIGN B \n";
            // sql += "         where   A.reptype in ('R','C')          and \n";
            // sql += "                 A.useyn    != 'N'              and \n"; // 사용미사용여부
            // sql += "                 A.subj      = '" + v_subj + "'  and \n";
            // sql += "                 A.year      = '" + v_year + "'  and \n";
            // sql += "                 A.subjseq   = '" + v_subjseq + "' and \n";
            // sql += "                 A.subj      = B.subj            and \n";
            // sql += "                 A.year      = B.year            and \n";
            // sql += "                 A.subjseq   = B.subjseq         and \n";
            // sql += "                 A.ordseq    = B.ordseq          and \n";
            // sql += "                 B.userid    = '" + v_user_id + "' \n";
            // sql += "         order by A.ordseq,A.lesson) \n";
            // sql += "union all \n";
            // sql += "select   ordseq, projseq, \n";
            // sql += "         lesson,\n";
            // sql += "         title,\n";
            // sql += "         score,\n";
            // sql += "         reptype,\n";
            // sql += "         projgrp,\n";
            // sql += "         score2,\n";
            // sql += "         cnt, \n";
            // sql += "         isret, \n";
            // sql += "         isfinal \n";
            // sql += "from    (select  A.ordseq, A.projseq, \n";
            // sql += "                 A.lesson,\n";
            // sql += "                 A.title,\n";
            // sql += "                 A.score,\n";
            // sql += "                 A.reptype, \n";
            // sql += "                 nvl((select isret \n";
            // sql += "                  from   tz_projrep \n";
            // sql += "                  where  subj=a.subj and \n";
            // sql += "                         year=a.year and \n";
            // sql += "                         subjseq=a.subjseq and ordseq=a.ordseq and projid=b.userid),'N') isret, \n";
            // sql += "                 nvl((select isfinal \n";
            // sql += "                  from   tz_projrep \n";
            // sql += "                  where  subj=a.subj and \n";
            // sql += "                         year=a.year and \n";
            // sql += "                         subjseq=a.subjseq and ordseq=a.ordseq and projid=b.userid),'N') isfinal, \n";
            // sql += "                (select  projgrp \n";
            // sql += "                 from    TZ_PROJGRP \n";
            // sql += "                 where   subj    = A.subj        and \n";
            // sql += "                         year    = A.year        and \n";
            // sql += "                         subjseq = A.subjseq     and \n";
            // sql += "                         ordseq  = A.ordseq  and \n";
            // sql += "                         userid  = '" + v_user_id + "') projgrp, \n";
            // sql += "                (select  score \n";
            // sql += "                 from    TZ_PROJREP \n";
            // sql += "                 where   subj    = A.subj        and \n";
            // sql += "                         year    = A.year        and \n";
            // sql += "                         subjseq = A.subjseq     and \n";
            // sql += "                         ordseq  = A.ordseq) score2, \n";
            // sql += "                (select  count(subj) \n";
            // sql += "                 from    TZ_PROJREP \n";
            // sql += "                 where   subj    = A.subj        and \n";
            // sql += "                         year    = A.year        and \n";
            // sql += "                         subjseq = A.subjseq     and \n";
            // sql += "                         ordseq  = A.ordseq  and \n";
            // sql += "                         projid  = '" + v_user_id + "') cnt \n";
            // sql += "         from    TZ_PROJORD A, \n";
            // sql += "                 TZ_PROJASSIGN B \n";
            // sql += "         where   A.reptype   = 'P' and \n";
            // sql += "                 A.useyn    != 'N'              and \n"; // 사용미사용여부
            // sql += "                 A.subj      = '" + v_subj    + "' and \n";
            // sql += "                 A.year      = '" + v_year    + "' and \n";
            // sql += "                 A.subjseq   = '" + v_subjseq + "' and \n";
            // sql += "                 A.subj      = B.subj            and \n";
            // sql += "                 A.year      = B.year            and \n";
            // sql += "                 A.subjseq   = B.subjseq         and \n";
            // sql += "                 A.ordseq    = B.ordseq          and \n";
            // sql += "                 B.userid    = '" + v_user_id + "' \n";
            // sql += "         order by A.ordseq,A.lesson) \n";
            
            // edit by leechanghun 2005.11.22
            // 작업내용 : SQL 튜닝
            
            sql += " select  \n";
            sql += "   A.ordseq, \n";
            sql += "   A.projseq,\n";
            sql += "   A.lesson,\n";
            sql += "   A.title,\n";
            sql += "   A.score,\n";
            sql += "   A.reptype,\n";
            sql += "   nvl(c.isret ,'N') isret,\n";
            sql += "   nvl(c.isfinal ,'N') isfinal,\n";
            sql += "   (select  projgrp from TZ_PROJGRP where subj = A.subj and year = A.year and subjseq = A.subjseq and ordseq  = A.ordseq  and userid  = '" +v_user_id + "') projgrp,\n";
            sql += "   c.score score2,\n";
            sql += "   c.resend,\n";
            sql += "   decode(nvl(c.subj, '0'), '0', '0', '1') cnt  \n";
            sql += " from                                           \n";
            sql += "   TZ_PROJORD A ,                               \n";
            sql += "   TZ_PROJASSIGN B,                             \n";
            sql += "   TZ_PROJREP C                                 \n";
            sql += " where                                          \n";
            sql += "   A.reptype in ('R','C', 'P')                  \n";
            sql += "   and A.useyn    != 'N'                       \n";
            sql += "   and A.subj      = '" +v_subj + "'               \n";
            sql += "   and A.year      = '" +v_year + "'               \n";
            sql += "   and A.subjseq   = '" +v_subjseq + "'            \n";
            sql += "   and A.subj      = B.subj                     \n";
            sql += "   and A.year      = B.year                     \n";
            sql += "   and A.subjseq   = B.subjseq                  \n";
            sql += "   and A.ordseq    = B.ordseq                   \n";
            sql += "   and B.userid    = '" +v_user_id + "'            \n";
            sql += "   and A.SUBJ = C.SUBJ( +)                       \n";
            sql += "   AND A.YEAR = C.YEAR( +)                       \n";
            sql += "   AND A.SUBJSEQ = C.SUBJSEQ( +)                 \n";
            sql += "   AND A.ORDSEQ = C.ORDSEQ( +)                   \n";
            sql += "   and c.projid( +) = '" +v_user_id + "'            \n";
            sql += " order by A.ordseq,A.lesson                     \n";     

            System.out.println("sql------------------------------" +sql);
            ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    data = new ProjectData();
                    data.setOrdseq( ls.getInt("ordseq") );
                    data.setProjseq( ls.getInt("projseq") );
                    data.setLesson( ls.getString("lesson") );
                    data.setTitle( ls.getString("title") );
                    data.setScore( ls.getInt("score") );
                    data.setReptype( ls.getString("reptype") );
                    if ( ls.getString("reptype").equals("P") ) { 
                        data.setProjgrp( ls.getString("projgrp") );
                    } else { 
                        data.setProjgrp(v_user_id);
                    }
                    data.setScore2( ls.getInt("score2") );
                    data.setCnt( ls.getInt("cnt") );
                    data.setIsret( ls.getString("isret") );
                    data.setIsfinal( ls.getString("isfinal") );
                    data.setResend( ls.getInt("resend") );
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
    출제 과제 정보보기
    @param box      receive from the form object and session
    @return ProjectData
    */
     public ProjectData selectProjectOrd(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";
        ProjectData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        String  v_projgrp   = box.getString("p_projgrp");
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");
        try { 
            connMgr = new DBConnectionManager();

            // select title,contents,expiredate,upfile,upfile2
            sql = "select A.title,A.contents,A.expiredate,A.upfile,A.upfile2,a.realfile,a.realfile2, A.ANSYN, ";
            sql += "(select eduend from tz_subjseq x where x.subj=a.subj and x.year = a.year and x.subjseq = a.subjseq ) eduend ";
            sql += "from TZ_PROJORD A ";
            sql += "where A.subj='" +v_subj + "' and A.year='" +v_year + "' and A.subjseq='" +v_subjseq + "' ";
            sql += "and A.ordseq='" +v_ordseq + "' and A.lesson='" +v_lesson + "' ";
System.out.println("selectProjectOrd sql ==  ==  ==  ==  ==  == > " +sql);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new ProjectData();
                data.setTitle( ls.getString("title") );
                data.setContents( ls.getString("contents") );
                data.setExpiredate( ls.getString("expiredate") );
                data.setUpfile( ls.getString("upfile") );
                data.setUpfile2( ls.getString("upfile2") );
                data.setRealfile( ls.getString("realfile") );
                data.setRealfile2( ls.getString("realfile2") );
                data.setAnsyn( ls.getString("ANSYN") );
                data.setEduend( ls.getString("eduend") );
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
    과제 제출내용 보기
    @param box      receive from the form object and session
    @return ProjectData
    */
     public ProjectData selectProjectRep(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";
        ProjectData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        String  v_projgrp   = box.getString("p_projgrp");
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");
        int     v_getcnt    = box.getInt("p_getcnt"); // 제출갯수
        int     v_ishalf    = 0;  // 중간데이타 존재여부
        int     v_cnt       = 0;
        try { 
            connMgr = new DBConnectionManager();

            // 중간저장 테이블에 데이타 여부
            // if ( v_getcnt < 1) { 
            //    sql  = " select count(*) from tz_projrephalf ";
            //    sql += " where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and ordseq='" +v_ordseq + "' and projid = '" +v_projgrp + "' ";
            //    ls = connMgr.executeQuery(sql);
            //    if ( ls.next() ) v_ishalf = ls.getInt(1);
            // }
						
					 sql  = " select count(*) ";
					 sql += "   from TZ_PROJREP ";
					 sql += "  where subj = '" +v_subj + "'";
					 sql += "    and year = '" +v_year + "'";
					 sql += "    and subjseq = '" +v_subjseq + "'";
					 sql +=  "    and ordseq='" +v_ordseq + "' and projid='" +v_projgrp + "'";
					 System.out.println("sql == >> " +sql);
					 
					 ls = connMgr.executeQuery(sql);
					 if ( ls.next() ) { 
					 	v_cnt = ls.getInt(1);
					 }
					 ls.close();
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

            if ( v_cnt > 0 ) { 
                // 제출내용 보여줌
                sql = "select A.title,A.contents,A.score,A.upfile,A.realfile,A.upfilesize,A.tucontents,A.score_mas,a.isret,a.retreason, 'Y' issubmit ";
                sql += "from TZ_PROJREP A ";
                sql += "where A.subj='" +v_subj + "' and A.year='" +v_year + "' and A.subjseq='" +v_subjseq + "' ";
                sql += "and A.ordseq='" +v_ordseq + "' and A.projid='" +v_projgrp + "'";// and A.lesson='" +v_lesson + "' ";
                ls = connMgr.executeQuery(sql);
                if ( ls.next() ) { 
                    data = new ProjectData();
                    data.setTitle( ls.getString("title") );
                    data.setContents( ls.getCharacterStream("contents") );
                    data.setScore( ls.getInt("score") );
                    data.setUpfile( ls.getString("upfile") );
                    data.setRealfile( ls.getString("realfile") );
                    data.setUpfilesize( ls.getString("upfilesize") );
                    data.setTucontents( ls.getString("tucontents") );
                    data.setScore_mas( ls.getInt("score_mas") );
                    data.setIsret( ls.getString("isret") );
                    data.setRetreason( ls.getString("retreason") );
                    data.setIssubmit("Y");
                }
            } else { 
                // 중간저장 데이타 보여줌
                sql  = " select title, contents, upfile, realfile, upfilesize, ldate, 'N' issubmit  from tz_projrephalf ";
                sql += " where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and ordseq='" +v_ordseq + "' and projid = '" +v_projgrp + "' ";
                ls = connMgr.executeQuery(sql);
                if ( ls.next() ) { 
                    data = new ProjectData();
                    data.setTitle( ls.getString("title") );
                    data.setContents( ls.getCharacterStream("contents") );
                    data.setScore(0);
                    data.setUpfile( ls.getString("upfile") );
                    data.setRealfile( ls.getString("realfile") );
                    data.setUpfilesize( ls.getString("upfilesize") );
                    data.setTucontents("");
                    data.setScore_mas(0);
                    data.setIsret("");
                    data.setRetreason("");
                    data.setIssubmit("N");
                }
            }
            
            // System.out.println("data.setIssubmit(N) == = >> " +data.getIssubmit() );
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
    과제 제출갯수 반환
    @param box      receive from the form object and session
    @return ProjectData
    */
     public int selectProjectRepCount(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";
        ProjectData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        String  v_projgrp   = box.getString("p_projgrp");
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");
        int     v_getcnt    = box.getInt("p_getcnt"); // 제출갯수
        int     v_cnt       = 0;
        try { 
            connMgr = new DBConnectionManager();

        			 sql  = " select count(*) ";
					 sql += "   from TZ_PROJREP ";
					 sql += "  where subj = '" +v_subj + "'";
					 sql += "    and year = '" +v_year + "'";
					 sql += "    and subjseq = '" +v_subjseq + "'";
					 sql +=  "    and ordseq='" +v_ordseq + "' and projid='" +v_projgrp + "'";
					 System.out.println("sql == >> " +sql);
					 
					 ls = connMgr.executeQuery(sql);
					 if ( ls.next() ) { 
					 	v_cnt = ls.getInt(1);
					 }
					 ls.close();
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_cnt;
    }

    /**
    동료평가점수 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectCoprepList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        ProjectData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_projgrp   = box.getString("p_projgrp");          // 제출자 ID
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // select couserid,score,ldate,coname
            sql = "select couserid,score,ldate, ";
            sql += "(select name from TZ_MEMBER where userid=A.couserid) coname ";
            sql += "from TZ_COPREP A ";
            sql += "where subj=" +SQLString.Format(v_subj);
            sql += " and year=" +SQLString.Format(v_year) + " and subjseq=" +SQLString.Format(v_subjseq);
            sql += " and ordseq=" +SQLString.Format(v_ordseq) + " and userid=" +SQLString.Format(v_projgrp);
            // order by A.lesson) ";
            ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    data = new ProjectData();
                    data.setCouserid( ls.getString("couserid") );
                    data.setConame( ls.getString("coname") );
                    data.setScore( ls.getInt("score") );
                    data.setLdate( ls.getString("ldate") );
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
    과제 등록 및 수정
    @param box      receive from the form object and session
    @return int
    */
     public int ProjectHandling(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls                  = null;
        ListSet ls3                 = null;
        PreparedStatement pstmt2    = null;
        String sql 									= "";
        String sql1                 = "";
        String sql2                 = "";
        String sql3                 = "";
        int isOk                    = 0;
        String  v_user_id           = box.getSession("userid");
        String  v_subj              = box.getString("p_subj");          // 과목
        String  v_year              = box.getString("p_year");          // 년도
        String  v_subjseq           = box.getString("p_subjseq");       // 과목기수
        String  v_projgrp           = box.getString("p_projgrp");
        String  v_lesson            = box.getString("p_lesson");
        int     v_ordseq            = box.getInt("p_ordseq");
        int     v_projseq           = box.getInt("p_projseq"); // 문제셋트번호

        String v_title              = box.getString("p_title");
        String v_contents           = box.getString("p_content");  // p_contents
        String v_contentsbyte       = box.getString("p_contentsbyte");  // contents byte

        String v_realFileName1      = box.getRealFileName("p_file1");
        String v_newFileName1       = box.getNewFileName("p_file1");
        String v_upfile1            = box.getString("p_upfile1");
        String v_check              = box.getString("p_check");
        String v_ishtml             = box.getString("p_ishtml");
		
		int v_contentsbytes = 0;
		
		// 편집기 사용할때와 하지않을때 구분
		// edit by leech 05.11.30
		
		System.out.println("v_ishtml ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == >>  >> " +v_ishtml);
		
		if ( v_ishtml.equals("N") ) { 
		  v_contentsbytes = StringManager.toInt(v_contentsbyte) - 298;
	    } else { 
	      v_contentsbytes = StringManager.toInt(v_contentsbyte);
	    }
	    if ( v_contentsbytes < 0) { 
	        v_contentsbytes = 0;
	    }
		
		v_contentsbyte 		= String.valueOf(v_contentsbytes);
		
		
		
        if ( v_newFileName1.length() == 0) {   v_newFileName1 = v_upfile1;   }

System.out.println("v_realfilename1 ==  ==  ==  ==  ==  ==  ==  == =" +v_realFileName1);
System.out.println("v_newfilename1 ==  ==  ==  ==  ==  ==  ==  == =" +v_newFileName1);

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
            sql1 += " and year  =" +SQLString.Format(v_year) + " and subjseq=" +SQLString.Format(v_subjseq);
            sql1 += " and ordseq=" +v_ordseq + " and projid=" +SQLString.Format(v_projgrp);
            ls = connMgr.executeQuery(sql1);

            // 기존파일 정보 불러오기
            
            sql3 = "( ";
            sql3 += "select  upfile,";
            sql3 += "        upfilesize,";
            sql3 += "        realfile  ";
            sql3 += "from    tz_projrephalf ";
            sql3 += "where   ";
            sql3 += "        subj    = '" + v_subj    + "' and ";
            sql3 += "        year    = '" + v_year    + "' and ";
            sql3 += "        subjseq = '" + v_subjseq + "' and ";
            sql3 += "        ordseq  = '" + v_ordseq  + "' and ";
            sql3 += "        projid  = '" + v_projgrp  + "' ";
            sql3 += ") union ";
            sql3 += "( ";
            sql3 += "select  upfile,";
            sql3 += "        upfilesize,";
            sql3 += "        realfile  ";
            sql3 += "from    tz_projrep ";
            sql3 += "where   ";
            sql3 += "        subj    = '" + v_subj    + "' and ";
            sql3 += "        year    = '" + v_year    + "' and ";
            sql3 += "        subjseq = '" + v_subjseq + "' and ";
            sql3 += "        ordseq  = '" + v_ordseq  + "' and ";
            sql3 += "        projid  = '" + v_projgrp  + "' ";
            sql3 += ")";
            ls3 = connMgr.executeQuery(sql3);
            if ( ls3.next() ) { 
                v_oldupfile   = ls3.getString("upfile");
                v_oldrealfile = ls3.getString("realfile");
                v_oldupfilesize  = ls3.getString("upfilesize");
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

            if ( ls.next() ) { // retmailing='':반려메일링 초기값, resend=1:재제출임

                sql2  = "update TZ_PROJREP set retmailing='', resend=1, title=?,contents=empty_clob(),upfile=?,upfilesize=?, realfile=?, contentsbyte=?, ";
                sql2 += "          luserid=?,ldate=to_char(sysdate,'YYYYMMDDHH24MISS'),indate=to_char(sysdate,'YYYYMMDDHH24MISS'), projseq=?  ";
                sql2 += "where subj=? and year=? and subjseq=? ";
                sql2 += "and ordseq=? and projid=? ";
                
                pstmt2 = connMgr.prepareStatement(sql2);

                pstmt2.setString(1,v_title);
                pstmt2.setString(2,v_newFileName1);
                pstmt2.setString(3,fileSize);
                pstmt2.setString(4,v_realFileName1);
                pstmt2.setString(5,v_contentsbyte);
                pstmt2.setString(6,v_user_id);
                pstmt2.setInt   (7,v_projseq);
                pstmt2.setString(8,v_subj);
                pstmt2.setString(9,v_year);
                pstmt2.setString(10,v_subjseq);
                pstmt2.setInt   (11,v_ordseq);
                pstmt2.setString(12,v_projgrp);
                isOk = pstmt2.executeUpdate();
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
            } else { 
               
            
                sql2  = "insert into TZ_PROJREP(subj,year,subjseq,ordseq,projid,seq,lesson,title,contents,upfile,upfilesize,realfile,luserid,contentsbyte,projseq,ldate,indate) ";
                sql2 += "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),to_char(sysdate,'YYYYMMDDHH24MISS'))";
//                sql2 += "values(?,?,?,?,?,?,?,?,empty_clob(),?,?,?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),to_char(sysdate,'YYYYMMDDHH24MISS'))";
                pstmt2 = connMgr.prepareStatement(sql2);

                pstmt2.setString(1,v_subj);
                pstmt2.setString(2,v_year);
                pstmt2.setString(3,v_subjseq);
                pstmt2.setInt   (4,v_ordseq);
                pstmt2.setString(5,v_projgrp);
                pstmt2.setInt   (6,1);
                pstmt2.setString(7,v_lesson);
                pstmt2.setString(8,v_title);
                pstmt2.setString(9,v_contents);
                pstmt2.setString(10,v_newFileName1);
                pstmt2.setString(11,fileSize);
                pstmt2.setString(12,v_realFileName1);
                pstmt2.setString(13,v_user_id);
                pstmt2.setString(14,v_contentsbyte);
                pstmt2.setInt   (15,v_projseq);

                isOk = pstmt2.executeUpdate();
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
                
            }
           			           			
                // clob contents 등록
           			sql1  = "select contents from TZ_PROJREP ";
           			sql1 += "where subj='" +v_subj + "'";
           			sql1 += "  and year='" +v_year + "'";
           			sql1 += "  and subjseq='" +v_subjseq + "'";
           			sql1 += "  and ordseq=" +v_ordseq;
           			sql1 += "  and projid='" +v_projgrp + "'";
//           			connMgr.setOracleCLOB(sql1, v_contents);       //      (ORACLE 9i 서버) 
           			
           			System.out.println("sql1 ==  ==  ==  ==  ==  == =" +sql1);
            // 중간저장 데이타 삭제
            if ( isOk > 0) { 
               
                sql2 = " delete from tz_projrephalf where subj=? and year=? and subjseq=? and ordseq=?  and projid=? ";
                pstmt2 = connMgr.prepareStatement(sql2);
                pstmt2.setString(1,v_subj);
                pstmt2.setString(2,v_year);
                pstmt2.setString(3,v_subjseq);
                pstmt2.setInt   (4,v_ordseq);
                pstmt2.setString(5,v_user_id);
                pstmt2.executeUpdate();
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
            }
            
						if ( isOk > 0) { 
							connMgr.commit();
						} else { 
							connMgr.rollback();
						}
            
        } catch ( Exception ex ) { 
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
    COP 동료평가 페이지 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectProjectCopList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        ProjectData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        String  v_projgrp   = box.getString("p_projgrp");       // 제출자 ID
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // select name,projgrp,title,indate,score,score2
            sql = "select   A.projid,A.title,A.indate,A.score,";
            sql += "         (select score from TZ_COPREP where subj=A.subj and year=A.year and subjseq=A.subjseq ";
            sql += "                 and ordseq=A.ordseq and userid=A.projid and couserid=" +SQLString.Format(v_projgrp) + ") score2, ";
            sql += "         (select name from TZ_MEMBER where userid=B.userid) name ";
            sql += "from     TZ_PROJREP A,TZ_PROJGRP B ";
            sql += "where    A.subj=" +SQLString.Format(v_subj);
            sql += " and A.year=" +SQLString.Format(v_year) + " and A.subjseq=" +SQLString.Format(v_subjseq);
            sql += " and A.ordseq=" +SQLString.Format(v_ordseq) + " and A.subj=B.subj and A.year=B.year ";
            sql += " and A.subjseq=B.subjseq and A.ordseq=B.ordseq and A.projid=B.userid and B.userid < > " +SQLString.Format(v_projgrp);
            sql += " order by B.userid";

            ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    data = new ProjectData();
                    data.setName( ls.getString("name") );
                    data.setProjgrp( ls.getString("projgrp") );
                    data.setTitle( ls.getString("title") );
                    data.setIndate( ls.getString("indate") );
                    data.setScore( ls.getInt("score") );
                    data.setScore2( ls.getInt("score2") );
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
    COP 학습자 과제 평가
    @param box      receive from the form object and session
    @return int
    */
     public int updateProjectJudge(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        PreparedStatement pstmt3 = null;
        String sql1         = "";
        String sql2         = "";
        String sql3         = "";
        String sql4         = "";
        ProjectData data1  = null;
        int isOk1           = 0;
        int isOk2           = 0;
        int isOk3           = 0;
        int isOk4           = 0;
        String  v_userid    = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");


        // p_supcheck로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_score      = new Vector();
        Vector v_chk        = new Vector();
        v_score             = box.getVector("p_score");
        v_chk               = box.getVector("p_chk");
        Enumeration em1     = v_chk.elements();
        Enumeration em2     = v_score.elements();
        String v_projgrp    = "";   // 실제 넘어온 각각의 value
        int    v_chkscore   =  0;   // 실제 넘어온 각각의 value
        int    v_cnt        =  0;
        long   v_finalscore =  0;
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // insert TZ_COPREP table
            sql3 = "insert into TZ_COPREP(subj,year,subjseq,ordseq,userid,seq,couserid,score,luserid,ldate) ";
            sql3 += "values(?,?,?,?,?,1,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'))";
            pstmt3 = connMgr.prepareStatement(sql3);

            while ( em1.hasMoreElements() ) { 

                v_projgrp   = (String)em1.nextElement();
                v_chkscore  = Integer.parseInt((String)em2.nextElement() );

                // select cnt,score,score_mas
                sql1 = "select (select count(subj) from TZ_PROJGRP where subj=A.subj and year=A.year and subjseq=A.subjseq and ordseq=A.ordseq) cnt,";
                sql1 += "(select sum(score) from TZ_COPREP where subj=A.subj and year=A.year and subjseq=A.subjseq and ordseq=A.ordseq ";
                sql1 += "and userid=A.projid and couserid < > " +SQLString.Format(v_userid) + ") score, score_mas ";
                sql1 += "from TZ_PROJREP A ";
                sql1 += "where A.subj=" +SQLString.Format(v_subj);
                sql1 += " and A.year=" +SQLString.Format(v_year) + " and A.subjseq=" +SQLString.Format(v_subjseq);
                sql1 += " and A.ordseq=" +SQLString.Format(v_ordseq) + " and A.projid=" +SQLString.Format(v_projgrp);

                ls1 = connMgr.executeQuery(sql1);

                if ( ls1.next() ) { 
                    v_cnt = ls1.getInt("cnt");
                    v_finalscore = v_chkscore + ls1.getInt("score");
                    v_finalscore = v_finalscore / v_cnt;
                    v_finalscore = StrictMath.round( ls1.getInt("score_mas")*0.5 + v_finalscore*0.5);
                }

                // delete TZ_COPREP table
                sql2 = "delete TZ_COPREP  ";
                sql2 += "where subj=" +SQLString.Format(v_subj);
                sql2 += " and year=" +SQLString.Format(v_year) + " and subjseq=" +SQLString.Format(v_subjseq);
                sql2 += " and ordseq=" +SQLString.Format(v_ordseq) + " and userid=" +SQLString.Format(v_projgrp);
                sql2 += " and couserid=" +SQLString.Format(v_userid);
                isOk2= connMgr.executeUpdate(sql2);

                pstmt3.setString(1, v_subj);
                pstmt3.setString(2, v_year);
                pstmt3.setString(3, v_subjseq);
                pstmt3.setInt(4, v_ordseq);
                pstmt3.setString(5, v_projgrp);
                pstmt3.setString(6, v_userid);
                pstmt3.setInt(7, v_chkscore);
                pstmt3.setString(8, v_userid);
                isOk3 = pstmt3.executeUpdate();

                // update TZ_PROJREP table
                sql4 = "update TZ_PROJREP set score=" +SQLString.Format((int)v_finalscore);
                sql4 += "where subj=" +SQLString.Format(v_subj);
                sql4 += " and year=" +SQLString.Format(v_year) + " and subjseq=" +SQLString.Format(v_subjseq);
                sql4 += " and ordseq=" +SQLString.Format(v_ordseq) + " and projid=" +SQLString.Format(v_projgrp);
                isOk4= connMgr.executeUpdate(sql4);

                if ( isOk3 > 0 && isOk4 > 0) connMgr.commit();
                else connMgr.rollback();

                // ==  ==  ==  == =과제 점수반영
                int isOk5 = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.REPORT,v_subj,v_year,v_subjseq,v_projgrp);
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( pstmt3 != null )  { try { pstmt3.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk3*isOk4;
    }


    /**
    문제풀에서 문제  자동 배정
    @param box      receive from the form object and session
    @return int
    */
     /*public void updateProjectAssign(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement   pstmt   = null;
        String sql         = "";
        // String sql2          = "";

        // ListSet ls          = null;

        String  v_isgoyong  = "N";

        int isOk           = 0;

        String  v_userid    = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);


            // sql2 = "select isgoyong from tz_subjseq where subj='" + v_subj + "' and year='" + v_year + "' and subjseq='" + v_subjseq + "' and isgoyong='Y'";
            // ls = connMgr.executeQuery(sql2);

            // if ( ls.next() ) { 
            //  v_isgoyong = "Y";
            // }

            // System.out.println("v_isgoyong=" +v_isgoyong);

            sql = "insert into tz_projassign(subj, year, subjseq, ordseq, userid, projseq, ldate) \n";
            sql += "select '" + v_subj + "','" + v_year + "','" + v_subjseq + "',ordseq, '" + v_userid + "', projseq, to_char(sysdate, 'YYYYMMDDHHMMss') \n";
            sql += "from ( \n";
            sql += "select projseq, \n";
            sql += "    min(assigncnt) assigncnt, \n";
            sql += "    min(ordseq) ordseq \n";
            sql += "from  ( \n";
            sql += "     \n";// --사용자에게 배정이 안된 과제기수(projseq) 중에서
            sql += "    select a.projseq, \n";
            sql += "           a.ordseq, \n";
            sql += "           (select count(*) \n";
            sql += "            from   tz_projassign \n";
            sql += "            where  subj    = a.subj and \n";
            sql += "                   year    = a.year and \n";
            sql += "                   subjseq = a.subjseq and \n";
            sql += "                   ordseq  = a.ordseq) assigncnt \n";
            sql += "       from   tz_projord a \n";
            sql += "    where  a.subj    = '" + v_subj + "' and \n";
            sql += "           a.year    = '" + v_year + "' and \n";
            sql += "           a.subjseq = '" + v_subjseq + "' and \n";
            sql += "           a.projseq in ( \n";
            sql += "                          \n";// --사용자에게 배정이 안된 과제기수(projseq) 구하기 시작
            sql += "                         select distinct projseq \n";
            sql += "                         from   tz_projord \n";
            sql += "                         where  subj    = '" + v_subj + "' and \n";
            sql += "                                year    = '" + v_year + "' and \n";
            sql += "                                subjseq = '" + v_subjseq + "' and \n";
            sql += "                                projseq not in ( \n";
            sql += "                                        \n ";// --사용자에게 배정된 과제기수(projseq) 구하기 시작
            sql += "                                        select (select projseq \n";
            sql += "                                                from   tz_projord \n";
            sql += "                                                where  a.subj  = subj and \n";
            sql += "                                                       year    = a.year and \n";
            sql += "                                                       subjseq = a.subjseq and \n";
            sql += "                                                       ordseq  = a.ordseq) projseq \n";
            sql += "                                        from   tz_projassign a \n";
            sql += "                                        where  a.subj    = '" + v_subj + "' and \n";
            sql += "                                               a.year    = '" + v_year + "' and \n";
            sql += "                                               a.subjseq = '" + v_subjseq + "' and \n";
            sql += "                                               a.userid  = '" + v_userid + "' \n";
            sql += "                                        \n";// --사용자에게 배정된 과제기수(projseq) 구하기 종료
            sql += "                                ) \n";
            sql += "                         \n";// --사용자에게 배정이 안된 과제기수(projseq) 구하기 종료

            // if ( v_isgoyong.equals("Y") ) { 
            //  // 고용보험 과목일 경우에 대해서만 5배수 체크함.
            //  sql += "                         and "; // --과제 기수에 대해 5배수 이상 등록되어 있는 과제 기수만 배정하기 시작
            //  sql += "                         ";
            //  sql += "                             (select count(*) ";
            //  sql += "                              from   tz_projord ";
            //  sql += "                              where  subj    = a.subj and ";
            //  sql += "                                     year    = a.year and ";
            //  sql += "                                     subjseq = a.subjseq and ";
            //  sql += "                                     projseq = a.projseq ) >= 5 ";
            //  sql += "                             ";// --과제 기수에 대해 5배수 이상 등록되어 있는 과제 기수만 배정하기 종료
            // }

            sql += "            ) \n";
            sql += " \n";
            sql += ") a \n";
            sql += "group by a.projseq, a.assigncnt  \n";

            sql += "having    (  \n";

            sql += "         a.assigncnt < \n";
            sql += "         \n";
            sql += "         decode ( \n";
            sql += "         (select nvl(max((select count(*) \n";
            sql += "                 from   tz_projassign \n";
            sql += "                 where  subj=a.subj and \n";
            sql += "                        year = a.year and \n";
            sql += "                        subjseq = a.subjseq and \n";
            sql += "                        projseq = a.projseq and \n";
            sql += "                        ordseq = a.ordseq)),0) \n";
            sql += "         from   tz_projord a \n";
            sql += "         where  subj    = '" + v_subj + "' and \n";
            sql += "                year    = '" + v_year + "' and \n";
            sql += "                subjseq = '" + v_subjseq + "' and \n";
            sql += "                                        projseq = a.projseq ) \n";
            sql += "                                        , \n";
            sql += "         \n";
            sql += "         (select nvl(min((select count(*) \n";
            sql += "                 from   tz_projassign \n";
            sql += "                 where  subj=a.subj and \n";
            sql += "                        year = a.year and \n";
            sql += "                        subjseq = a.subjseq and \n";
            sql += "                        projseq = a.projseq and \n";
            sql += "                        ordseq = a.ordseq)),0) \n";
            sql += "         from   tz_projord a \n";
            sql += "         where  subj    = '" + v_subj + "' and  \n";
            sql += "                year    = '" + v_year + "' and \n";
            sql += "                subjseq = '" + v_subjseq + "' and \n";
            sql += "                projseq = a.projseq ) \n";
            sql += "                                        , \n";
            sql += "                                        \n";
            sql += "         (select nvl(max((select count(*) \n";
            sql += "                 from   tz_projassign \n";
            sql += "                 where  subj=a.subj and \n";
            sql += "                        year = a.year and \n";
            sql += "                        subjseq = a.subjseq and \n";
            sql += "                        projseq = a.projseq and \n";
            sql += "                        ordseq = a.ordseq)),0) +1 \n";
            sql += "         from   tz_projord a \n";
            sql += "         where  subj    = '" + v_subj + "' and  \n";
            sql += "                year    = '" + v_year + "' and \n";
            sql += "                subjseq = '" + v_subjseq + "' and \n";
            sql += "                projseq = a.projseq ) \n";
            sql += "                                        , \n";
            sql += "         \n";
            sql += "         (select nvl(max((select count(*) \n";
            sql += "                 from   tz_projassign \n";
            sql += "                 where  subj=a.subj and \n";
            sql += "                        year = a.year and \n";
            sql += "                        subjseq = a.subjseq and \n";
            sql += "                        projseq = a.projseq and \n";
            sql += "                        ordseq = a.ordseq)),0) \n";
            sql += "         from   tz_projord a \n";
            sql += "         where  subj    = '" + v_subj + "' and \n";
            sql += "                year    = '" + v_year + "' and \n";
            sql += "                subjseq = '" + v_subjseq + "' and \n";
            sql += "                projseq = a.projseq ) \n";
            sql += "                                        \n";
            sql += "         \n";
            sql += "         ) \n";
            sql += "         \n";
            sql += "         \n";
            sql += "         ) \n";



            sql += ") \n";



            pstmt = connMgr.prepareStatement(sql);
            // System.out.println("assign sql = " + sql);
Log.info.println("배정 >> " +sql + "<<배정 >> ");
            pstmt.executeUpdate();

            connMgr.commit();

            }

        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            // if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
    }*/

    /**
    문제풀에서 문제셋트  자동 배정
    @param box      receive from the form object and session
    @return int
    */
     public void updateProjectAssign(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        int isOk            = 0;

        String  v_userid    = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        int     v_projseq   = 1;                                // 셋트번호

        String  s_assign    = box.getString("p_assign");        // 배정flag(***운영자화면에서 재제출시..)
        int     s_projseq   = box.getInt("p_projseq");          // 셋트번호(***운영자화면에서 재제출시..)
        String  s_userid    = box.getString("p_userid");        // id      (***운영자화면에서 재제출시..)


        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);


            if ( s_assign.equals("") ) {        // 학습창에서 배정받음
                // 셋트번호 구함
                // sql = " select projseq "
                //    + " from (select  projseq, count(projseq) pcnt from TZ_PROJASSIGN  "
                //    + "       WHERE   SUBJ='" +v_subj + "' AND year='" +v_year + "' AND subjseq='" +v_subjseq + "'   "
                //    + "       group by projseq order by pcnt asc )  "
                //    + " where rownum=1 ";

                sql  = " select                                             \n";
                sql += "   a.subj,                                          \n";
                sql += "   a.year,                                          \n";
                sql += "   a.subjseq,                                       \n";
                sql += "   a.projseq ,                                      \n";
                sql += "   (select count(*) from tz_projassign x where a.subj = x.subj and a.year = x.year and a.subjseq = x.subjseq and a.projseq = x.projseq) pcnt    \n";
                sql += " from     \n";
                sql += "   tz_projord a    \n";
                sql += " where             \n";
                sql += "   a.subj = '" +v_subj + "'     \n";
                sql += "   and a.subjseq = '" +v_subjseq + "'     \n";
                sql += "   and a.year = '" +v_year + "'     \n";
				sql += "   and a.useyn = 'Y'     \n";
                sql += " group by    \n";
                sql += "   subj, year, subjseq, projseq    \n";
                sql += " order by pcnt,  projseq    \n";

                ls = connMgr.executeQuery(sql);
                if ( ls.next() ) v_projseq = ls.getInt("projseq");

            } else {                            // 운영자화면에서 재제출시 배정받음
                v_projseq= s_projseq;
                v_userid = s_userid;
System.out.println("s_userid 운영자화면에서 재제출시 배정받음");
            }
            
            System.out.println("sql과제 배정 ==  == >>  >>  > " +sql);
            System.out.println("v_projseq == = >>  >> " +v_projseq);

            // 배정정보 등록
            sql = " insert into tz_projassign(subj, year, subjseq, ordseq, userid, projseq, ldate) "
                + " select subj, year, subjseq, ordseq, ?, projseq, to_char(sysdate, 'YYYYMMDDHHMMss') "
                + " from (SELECT subj, year, subjseq, ordseq, projseq  "
                + "       FROM TZ_PROJORD WHERE subj=? AND year=? AND subjseq=? and projseq=? ) ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_userid);
            pstmt.setString(2, v_subj);
            pstmt.setString(3, v_year);
            pstmt.setString(4, v_subjseq);
            pstmt.setInt   (5, v_projseq);
            isOk = pstmt.executeUpdate();

            if ( isOk > 0 ) { 
                connMgr.commit();
            }

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
    }

    /**
    학습자 배정여부 확인
    @param box      receive from the form object and session
    @return int     배정셋트갯수
    */
     public int IsProjectAssign(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        int isOk            = 0;

        String  v_userid    = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        int     v_projseqcnt= 0;  // 셋트번호갯수

        try { 
            connMgr = new DBConnectionManager();

            sql = " SELECT count(grpseq) FROM TZ_PROJASSIGN WHERE SUBJ='" +v_subj + "' AND year='" +v_year + "' AND subjseq='" +v_subjseq + "' and userid='" +v_userid + "' ";

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) v_projseqcnt = ls.getInt(1);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_projseqcnt;
    }


    /**
    과제 중간 등록 및 수정
    @param box      receive from the form object and session
    @return int
    */
     public int ProjectHalfSave(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls                  = null;
        ListSet ls3                 = null;
        PreparedStatement pstmt2    = null;
        String sql1                 = "";
        String sql2                 = "";
        String sql3                 = "";
        int isOk                    = 0;
        String  v_user_id           = box.getSession("userid");
        String  v_subj              = box.getString("p_subj");          // 과목
        String  v_year              = box.getString("p_year");          // 년도
        String  v_subjseq           = box.getString("p_subjseq");       // 과목기수
        String  v_projgrp           = box.getString("p_projgrp");
        String  v_lesson            = box.getString("p_lesson");
        int     v_ordseq            = box.getInt("p_ordseq");  // 문제번호
        int     v_projseq           = box.getInt("p_projseq"); // 문제셋트번호

        String v_title              = box.getString("p_title");
        String v_contents           = box.getString("p_content");
        String v_contentsbyte       = box.getString("p_contentsbyte");  // contents byte
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
            // 기존파일 정보 불러오기
            sql3 = "select  upfile,";
            sql3 += "        upfilesize,";
            sql3 += "        realfile  ";
            sql3 += "from    TZ_PROJREPHALF ";
            sql3 += "where   subj    = '" + v_subj    + "' and ";
            sql3 += "        year    = '" + v_year    + "' and ";
            sql3 += "        subjseq = '" + v_subjseq + "' and ";
            sql3 += "        ordseq  = '" + v_ordseq  + "' and ";
            sql3 += "        projid  = '" + v_projgrp  + "' ";

            ls3 = connMgr.executeQuery(sql3);
            if ( ls3.next() ) { 
                v_oldupfile   = ls3.getString("upfile");
                v_oldrealfile = ls3.getString("realfile");
                v_oldupfilesize  = ls3.getString("upfilesize");
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

            // 중간저장 테이블에 데이타 여부
            int v_ishalf = 0;
            sql2  = " select count(*) cnt from tz_projrephalf ";
            sql2 += " where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and ordseq='" +v_ordseq + "' and projid = '" +v_projgrp + "' ";
            System.out.println("sql_halfproject ==  ==  == = >>  >>  >>  >>  >> " +sql2);
            ls = connMgr.executeQuery(sql2);
            if ( ls.next() ) v_ishalf = ls.getInt("cnt");
            if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
            
            System.out.println("v_ishalf ==  ==  == = >>  >>  >>  >>  >> " +v_ishalf);

            // 기존 중간저장 데이타가 있으면
            if ( v_ishalf > 0) { 
                sql2  = "update TZ_PROJREPHALF set title=?,contents=empty_clob(),upfile=?,upfilesize=?, realfile=?, contentsbyte=?, projseq=?, luserid=?,ldate=to_char(sysdate,'YYYYMMDDHH24MISS')  ";
                sql2 += "where subj=? and year=? and subjseq=? and ordseq=? and projid=? ";
                
                System.out.println("sql_update ==  ==  == = >>  >>  >>  >>  >> " +sql2);
                pstmt2 = connMgr.prepareStatement(sql2);
                pstmt2.setString(1,v_title);
                pstmt2.setString(2,v_newFileName1);
                pstmt2.setString(3,fileSize);
                pstmt2.setString(4,v_realFileName1);
                pstmt2.setString(5,v_contentsbyte);
                pstmt2.setInt   (6,v_projseq);
                pstmt2.setString(7,v_user_id);
                pstmt2.setString(8,v_subj);
                pstmt2.setString(9,v_year);
                pstmt2.setString(10,v_subjseq);
                pstmt2.setInt   (11,v_ordseq);
                pstmt2.setString(12,v_projgrp);

                isOk = pstmt2.executeUpdate();
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
                System.out.println("Update_IsOk ==  ==  == = >>  >>  >>  >>  >> " +isOk);
            } else { 
                sql2  = " insert into TZ_PROJREPHALF( subj, year, subjseq, ordseq, projid, title, contents, luserid, ldate,    ";
                sql2 += "         upfile, upfilesize, realfile, contentsbyte, projseq   ) ";
                sql2 += "values( ?,?,?,?,?,?,?,?, to_char(sysdate, 'YYYYMMDDHHMMSS'), ?,?,?,?,? )";
//                sql2 += "values( ?,?,?,?,?,?,empty_clob(),?, to_char(sysdate, 'YYYYMMDDHHMMSS'), ?,?,?,?,? )";
                // System.out.println("sql2 == = >> " +sql2);
                pstmt2 = connMgr.prepareStatement(sql2);
                pstmt2.setString(1,v_subj);
                pstmt2.setString(2,v_year);
                pstmt2.setString(3,v_subjseq);
                pstmt2.setInt   (4,v_ordseq);
                pstmt2.setString(5,v_projgrp);
                pstmt2.setString(6,v_title);
                pstmt2.setString(7,v_contents);
                pstmt2.setString(8,v_user_id);
                pstmt2.setString(9,v_newFileName1);
                pstmt2.setString(10,fileSize);
                pstmt2.setString(11,v_realFileName1);
                pstmt2.setString(12,v_contentsbyte);
                pstmt2.setInt   (13,v_projseq);
                isOk = pstmt2.executeUpdate();
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
                System.out.println("isOk == = >> " +isOk);
            }

            sql1  = "select contents from TZ_PROJREPHALF ";
           	sql1 += "where subj='" +v_subj + "'";
           	sql1 += "  and year='" +v_year + "'";
           	sql1 += "  and subjseq='" +v_subjseq + "'";
           	sql1 += "  and ordseq=" +v_ordseq;
           	sql1 += "  and projid='" +v_projgrp + "'";
           	
           	System.out.println("sql1 == = >>  >> " +sql1);
           	
           	System.out.println("v_contents ==  == >>  > " +v_contents);
           	
//           	connMgr.setOracleCLOB(sql1, v_contents);       // (ORACLE 9i 서버)
           	
           	System.out.println("isOk ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == >>  >>  > " +isOk);
            
            
            if ( isOk > 0) { 
            	System.out.println("connMgr.commit:connMgr.commit:connMgr.commit:connMgr.commit:connMgr.commit:connMgr.commit");
				connMgr.commit();
			} else { 
				connMgr.rollback();
			}
            

        } catch ( Exception ex ) { 
            throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls3 != null )     { try { ls3.close(); } catch ( Exception e ) { } }
            if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    학습창 과제 리스트(과거이력)
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectProjectListOld(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        DataBox dbox   = null;

        String  v_pkey     = box.getString("p_pkey");
        String  v_userid   = box.getString("p_userid");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = " select pkey, userid, filenm, title, submitdate, markingdate, rscore, redpen  ";
            sql += "   from cc_reportredpen                                                      ";
            sql += "  where pkey   = " + SQLString.Format(v_pkey);
            sql += "    and userid = " + SQLString.Format(v_userid);
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
    학습창 과제 리스트(과거이력)
    @param box      receive from the form object and session
    @return ArrayList
    */
     public DataBox selectProjectOld(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";
        DataBox dbox   = null;

        String  v_pkey     = box.getString("p_pkey");
        String  v_userid   = box.getString("p_userid");
		String  v_title    = box.getString("p_title");
        try { 
            connMgr = new DBConnectionManager();

            sql = " select pkey, userid, filenm, title, submitdate, markingdate, rscore, redpen  ";
            sql += "   from cc_reportredpen                                                     ";
            sql += "  where pkey   = " + SQLString.Format(v_pkey);
            sql += "    and userid = " + SQLString.Format(v_userid);
            sql += "    and title  = " + SQLString.Format(v_title);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
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
    교육완료 후 재제출 등록
    @param box      receive from the form object and session
    @return int
    */
     public int insertProjectEndSubmit(RequestBox box) throws Exception { 
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
            System.out.println("sql1 ==  ==  ==  ==  == > " +sql1);

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

                sql2  = "update TZ_PROJREP set ";
                sql2 += "resend=1, ";
                sql2 += "title=?,";
                sql2 += "contents=empty_clob(),";
                sql2 += "upfile=?,";
                sql2 += "upfilesize=?, ";
                sql2 += "realfile=?, ";
                sql2 += "isret='N', ";
                sql2 += "luserid=?,";
                sql2 += "ldate=to_char(sysdate,'YYYYMMDDHH24MISS'),";
                // sql2 += "indate=to_char(sysdate,'YYYYMMDDHH24MISS') ";
                sql2 += "indate=(select eduend from tz_subjseq where tz_subjseq.subj=TZ_PROJREP.subj and tz_subjseq.year=TZ_PROJREP.year and tz_subjseq.subjseq=TZ_PROJREP.subjseq)";
                sql2 += "where subj=? and year=? and subjseq=? ";
                sql2 += "and ordseq=? and projid=? ";                

                pstmt2 = connMgr.prepareStatement(sql2);
                pstmt2.setString(1,v_title);
                // pstmt2.setString(2,v_contents);
                pstmt2.setString(2,v_newFileName1);
                pstmt2.setString(3,fileSize);
                pstmt2.setString(4,v_realFileName1);
                pstmt2.setString(5,v_user_id);
                pstmt2.setString(6,v_subj);
                pstmt2.setString(7,v_year);
                pstmt2.setString(8,v_subjseq);
                pstmt2.setInt(9,v_ordseq);
                pstmt2.setString(10,v_user_id);
                isOk = pstmt2.executeUpdate();  
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
            } else { 
            
                sql2  = "insert into TZ_PROJREP(subj,year,subjseq,ordseq,projid,seq,lesson,title,contents,  upfile,upfilesize,realfile,luserid,ldate, ";
                sql2 += "                       indate, contentsbyte, projseq ) ";
                sql2 += "values(?,?,?,?,?,?,?,?,?,  ?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'), ";
//                sql2 += "values(?,?,?,?,?,?,?,?,empty_clob(),  ?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'), ";
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
            sql2 += "  where SUBJ = '" +v_subj + "' AND YEAR = '" +v_year + "' AND SUBJSEQ = '" +v_subjseq + "' ";
            sql2 += "  AND ORDSEQ = '" +v_ordseq + "' AND PROJID = '" +v_user_id + "'";
//			connMgr.setOracleCLOB(sql2, v_contents);	// 오라클
            
            if ( isOk > 0) { 
              connMgr.commit();
            } else { 
              connMgr.rollback();
            }
           
        } catch ( Exception ex ) {                        
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls3 != null )      { try { ls3.close(); } catch ( Exception e ) { } }
            if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }    
}