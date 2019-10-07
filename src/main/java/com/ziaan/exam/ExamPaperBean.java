// **********************************************************
// 1. 제      목:
// 2. 프로그램명: ExamPaperBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-29
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.exam;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.SelectionUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class ExamPaperBean { 


    public ExamPaperBean() { }



    /**
    평가지 리스트  
    @param box
    @return ArrayList
    */
    public ArrayList selectPaperList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String  ss_grcode      = box.getStringDefault("s_grcode", "ALL");           // 교육그룹
        String  ss_gyear       = box.getStringDefault("s_gyear", "ALL");            // 년도
        String  ss_grseq       = box.getStringDefault("s_grseq", "ALL");            // 교육기수
        String  ss_upperclass  = box.getStringDefault("s_upperclass", "ALL");
        String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");
        String  ss_lowerclass  = box.getStringDefault("s_lowerclass", "ALL");
        String  ss_subjcourse  = box.getStringDefault("s_subjcourse","ALL");    // 과목&코스
        String  ss_year        = "ALL";
        String  ss_subjseq     = box.getStringDefault("s_subjseq","ALL");

        String v_action     = box.getStringDefault("p_action","change");

        try { 
            connMgr = new DBConnectionManager();
            if ( v_action.equals("go") ) { 
                list = getPaperList(connMgr, ss_grcode, ss_gyear, ss_grseq, ss_upperclass, ss_middleclass,ss_lowerclass, ss_subjcourse, ss_year, ss_subjseq);
            } else { 
                list = new ArrayList();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    평가지 리스트  
    @param box
    @return ArrayList
    */
    public ArrayList getPaperList(DBConnectionManager connMgr, String p_grcode, String p_gyear,  String p_grseq, String p_upperclass, 
                                  String p_middleclass, String p_lowerclass, String p_subj, String p_year, String p_subjseq) throws Exception { 
        ArrayList list = new ArrayList();
        ArrayList blist = null;
        ListSet             ls      = null;
        String sql  = "";
        DataBox dbox    = null;
        String v_subj_bef = "";

        String sql1 = "";
        String sql2 = "";
        PreparedStatement   pstmt1  = null;
        PreparedStatement   pstmt2  = null;
        int index = 0;
        int v_studentcnt     = 0;
        int v_examstudentcnt = 0;

        try { 

            sql = "select b.subj,   a.year,    a.subjseq,   a.lesson, ";
            sql += "       a.examtype,  a.papernum,   a.lessonstart, a.lessonend,  a.examtime, a.exampoint, a.examcnt, a.totalscore,  ";
            sql += "       a.cntlevel1, a.cntlevel2, a.cntlevel3, a.isopenanswer, ";
            sql += "       a.isopenexp,  a.retrycnt, a.progress, b.subjnm,  b.subjseqgr, GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", nvl(a.examtype, '')) examtypenm ";
            sql += "  from tz_exampaper a, ";
            sql += "       tz_subjseq b";

            sql += " where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq ";

            if ( !p_grcode.equals("ALL"))
                sql += "   and b.grcode       = " + SQLString.Format(p_grcode);
            
            if ( !p_gyear.equals("ALL"))
                sql += "   and b.gyear       = " + SQLString.Format(p_gyear);
            
            if ( !p_grseq.equals("ALL"))
                sql += "   and b.grseq       = " + SQLString.Format(p_grseq);
            
            if ( !p_subj.equals("ALL"))
                sql += "   and b.subj       = " + SQLString.Format(p_subj);

            if ( !p_year.equals("ALL"))
               sql += "   and a.year       = " + SQLString.Format(p_year);
            if ( !p_subjseq.equals("ALL"))
                sql += "   and a.subjseq    = " + SQLString.Format(p_subjseq);
            sql += " order by a.subj, a.year, a.subjseq, a.lesson, a.examtype  ";

            System.out.println(sql);
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                if ( index == 0 ) { 
                    sql1 = " select count(*) cnt ";
                    sql1 += "   from tz_student   ";
                    sql1 += "  where subj    = ? ";
                    sql1 += "    and year    = ? ";
                    sql1 += "    and subjseq = ? ";
                    pstmt1 = connMgr.prepareStatement(sql1);

                    sql2 = " select count(*) cnt ";
                    sql2 += "   from tz_examresult   ";
                    sql2 += "  where subj    = ? ";
                    sql2 += "    and year    = ? ";
                    sql2 += "    and subjseq = ? ";
                    sql2 += "    and lesson  = ? ";
                    sql2 += "    and examtype   = ? ";
                    sql2 += "    and papernum= ? ";
                    
                    System.out.println("sql 2 : " + sql2);
                    
                    pstmt2 = connMgr.prepareStatement(sql2);
                    
                }
                dbox = ls.getDataBox();
				v_studentcnt = getStudentCount(pstmt1, p_subj, p_gyear, p_subjseq);

                if ( dbox.getInt("d_papernum") == 0 ) { 
                    v_examstudentcnt = 0;
                } else { 
                    v_examstudentcnt = getExamStudentCount(pstmt2, dbox);
                }
                    String v_string1 = new String(String.valueOf(v_studentcnt));
                    String v_string2 = new String(String.valueOf(v_examstudentcnt));

                    blist = new ArrayList();
					blist.add(0, dbox);
                    blist.add(1, v_string1);
                    blist.add(2, v_string2);

					
				    list.add(blist);

                index++;
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
        }

        return list;
    }


    /**
    학생수 구하기 
    @param box
    @return int
    */
    public int getStudentCount(PreparedStatement pstmt, String p_subj, String p_gyear, String p_subjseq) throws Exception { 
        int v_studentcnt = 0;
        ResultSet  rs = null;

        try { 
            pstmt.setString(1, p_subj);
            pstmt.setString(2, p_gyear);
            pstmt.setString(3, p_subjseq);
            rs = pstmt.executeQuery();
            if ( rs.next() ) { 
                v_studentcnt = rs.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
        }

        return v_studentcnt;
    }

    /**
    응시한 학생수 구하기 
    @param box
    @return int
    */
    public int getExamStudentCount(PreparedStatement pstmt, DataBox dbox) throws Exception { 
        int v_studentcnt = 0;
        ResultSet  rs = null;

        try { 
            pstmt.setString(1, dbox.getString("d_subj") );
            pstmt.setString(2, dbox.getString("d_year") );
            pstmt.setString(3, dbox.getString("d_subjseq") );
            pstmt.setString(4, dbox.getString("d_lesson") );
            pstmt.setString(5, dbox.getString("d_examtype") );
            pstmt.setInt   (6, dbox.getInt("d_papernum") );
            rs = pstmt.executeQuery();
            if ( rs.next() ) { 
                v_studentcnt = rs.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
        }

        return v_studentcnt;
    }


    /**
    문제정보 데이타 
    @param box
    @return DataBox
    */
    public DataBox selectExamPaperData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox             dbox    = null;

        String v_subj    = box.getString("p_subj");
        String v_gyear  = box.getString("p_gyear");
        String v_subjseq  = box.getString("p_subjseq");
		String v_lesson  = box.getString("p_lesson");
        String v_examtype   = box.getString("p_examtype");
        int v_papernum  = box.getInt("p_papernum");

        try { 
            connMgr = new DBConnectionManager();
            dbox = getExamPaperData(connMgr, v_subj, v_gyear, v_subjseq, v_lesson, v_examtype, v_papernum);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }


    /**
    문제지 정보
    @param box          receive from the form object and session
    @return DataBox   
    */
    public DataBox getExamPaperData(DBConnectionManager connMgr, String p_subj, String p_gyear, String p_subjseq, String p_lesson, String p_examtype, int p_papernum) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;

        try { 
            sql = "select a.subj,   a.year,   a.subjseq,   a.lesson, ";
            sql += "       a.examtype,   a.papernum,  a.lessonstart, a.lessonend, ";
            sql += "       a.examtime,   a.exampoint,   a.examcnt,  a.totalscore,  a.cntlevel1, a.cntlevel2, a.cntlevel3, a.level1text, a.level2text, a.level3text, ";
            sql += "       a.isopenanswer,  a.isopenexp, a.retrycnt, a.progress, GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", nvl(a.examtype, '')) examtypenm, startdt, enddt ";
            sql += "  from tz_exampaper a ";
            sql += " where a.subj    = " + SQLString.Format(p_subj);
            sql += "   and a.year  = " + SQLString.Format(p_gyear);
            sql += "   and a.subjseq  = " + SQLString.Format(p_subjseq);
			sql += "   and a.lesson  = " + SQLString.Format(p_lesson);
            sql += "   and a.examtype   = " + SQLString.Format(p_examtype);
            sql += "   and a.papernum  = " + SQLString.Format(p_papernum);

            System.out.println("문제지 정보 : " + sql);
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return dbox;
    }
    
    public boolean isExamResult(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        boolean result = false;

        try { 
            String v_subj    = box.getString("p_subj");
            String v_gyear  = box.getString("p_gyear");
            String v_subjseq  = box.getString("p_subjseq");
            String v_lesson  = box.getString("p_lesson");
            String v_examtype   = box.getString("p_examtype");
            int v_papernum  = box.getInt("p_papernum");
        
            connMgr = new DBConnectionManager();
            
            sql = "select subj";
            sql += "  from tz_examresult ";
            sql += " where subj    = " + SQLString.Format(v_subj);
            sql += "   and year  = " + SQLString.Format(v_gyear);
            sql += "   and subjseq  = " + SQLString.Format(v_subjseq);
            sql += "   and lesson  = " + SQLString.Format(v_lesson);
            sql += "   and examtype   = " + SQLString.Format(v_examtype);
            sql += "   and papernum  = " + SQLString.Format(v_papernum);

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
                result = true;
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }
    
    
    /**
    평가문제지  등록 
    @param box
    @return int
    */    
    public int insertExamPaper(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String              sql     = "";
        ListSet             ls      = null;
		DataBox             dbox    = null;
        int isOk = 0;

        String v_subj          = box.getString("p_subj");
        String v_gyear          = box.getString("p_gyear");
        String v_subjseq       = box.getString("p_subjseq");

        String v_luserid    = box.getSession("userid");

        int  v_papernum = 0;

        try { 
            connMgr = new DBConnectionManager();
            
            connMgr.setAutoCommit(false);
            v_papernum = getPapernumSeq(v_subj, v_gyear, v_subjseq);

            sql = "select a.subj,      a.lesson, ";
            sql += "       a.examtype,     a.lessonstart, a.lessonend, ";
            sql += "       a.examtime,   a.exampoint,   a.examcnt,  a.totalscore,  a.cntlevel1, a.cntlevel2, a.cntlevel3, a.level1text, a.level2text, a.level3text, ";
            sql += "       a.isopenanswer,  a.isopenexp, a.retrycnt, a.progress, startdt, enddt ";
            sql += "  from tz_exammaster a ";
            sql += " where a.subj    = " + SQLString.Format(v_subj);
            
            ls = connMgr.executeQuery(sql); 

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                 String v_lesson    = dbox.getString("d_lesson");
                 String v_examtype     = dbox.getString("d_examtype");
                 String v_lessonstart = dbox.getString("d_lessonstart");
                 String v_lessonend   = dbox.getString("d_lessonend");
                 String v_examtime   = dbox.getString("d_examtime");
                 int v_exampoint = dbox.getInt   ("d_exampoint");
                 int v_examcnt = dbox.getInt   ("d_examcnt");
                 int v_totalscore = dbox.getInt   ("d_totalscore");
                 String v_level1text  = dbox.getString("d_level1text");
                 String v_level2text  = dbox.getString("d_level2text");
                 String v_level3text  = dbox.getString("d_level3text");
                 int v_cntlevel1  = dbox.getInt("d_cntlevel1");
                 int v_cntlevel2  = dbox.getInt("d_cntlevel2");
                 int v_cntlevel3  = dbox.getInt("d_cntlevel3");
                 String v_isopenanswer = dbox.getString("d_isopenanswer");
                 String v_isopenexp  = dbox.getString("d_isopenexp");
                 int v_retrycnt = dbox.getInt("d_retrycnt");
                 int v_progress = dbox.getInt("d_progress");
                 String v_startdt 		= dbox.getString("d_startdt");
                 String v_enddt  		= dbox.getString("d_enddt");

                isOk = insertTZ_exampaper(connMgr, v_subj, v_gyear, v_subjseq, v_lesson, v_examtype, v_papernum, v_lessonstart, v_lessonend, v_examtime, v_exampoint, v_examcnt, v_totalscore, v_cntlevel1, v_cntlevel2, v_cntlevel3, v_level1text, v_level2text, v_level3text, v_isopenanswer, v_isopenexp, v_retrycnt, v_progress, v_luserid, v_startdt, v_enddt);
  
				v_papernum++;
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
    
    
    /**
    평가문제지  등록 
    @param box
    @return int
    */    
    public int insertExamPaperGrseq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String              sql     = "";
        ListSet             ls      = null;
		DataBox             dbox    = null;
        int isOk = 0;
        int isOk2 = 0;        

        
        String v_grcode     = box.getString("p_grcode");
        String v_gryear     = box.getString("p_gyear");
        String v_grseq      = box.getString("p_grseq");        	

        String v_luserid    = box.getSession("userid");

        int  v_papernum = 0;

        try { 
            connMgr = new DBConnectionManager();
            
            connMgr.setAutoCommit(false);
      
            sql = "select    a.subj, a.year, a.subjseq, a.subjnm, ";
            sql += "\n       c.lesson,  c.examtype,     c.lessonstart, c.lessonend,c.lesson,  c.examtype,     c.lessonstart, c.lessonend, ";
            sql += "\n       c.examtime,   c.exampoint,   c.examcnt,  c.totalscore,  c.cntlevel1, c.cntlevel2, c.cntlevel3, c.level1text, c.level2text, c.level3text, ";
            sql += "\n       c.isopenanswer,  c.isopenexp, c.retrycnt, c.progress, b.startdt, b.enddt  ";
            sql += "\n  from tz_subjseq  a,  tz_exampaper b, tz_exammaster c  ";
            sql += "\n where a.subj = b.subj(+) and a.year = b.year(+) and a.subjseq = b.subjseq(+) and a.subj=c.subj and b.papernum is null ";
            sql += "\n   and a.grcode   = " + SQLString.Format(v_grcode);    
            sql += "\n   and a.gyear    = " + SQLString.Format(v_gryear);                 
            sql += "\n   and a.grseq    = " + SQLString.Format(v_grseq); 
            sql += "\n order by a.subj ";         
          
            ls = connMgr.executeQuery(sql); 

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                 String v_subj    	    = dbox.getString("d_subj");
                 String v_gyear    	    = dbox.getString("d_year");
                 String v_subjseq       = dbox.getString("d_subjseq");                                
                 String v_lesson    	= dbox.getString("d_lesson");
                 String v_examtype     	= dbox.getString("d_examtype");
                 String v_lessonstart 	= dbox.getString("d_lessonstart");
                 String v_lessonend   	= dbox.getString("d_lessonend");
                 String v_examtime   	= dbox.getString("d_examtime");
                 int v_exampoint 		= dbox.getInt("d_exampoint");
                 int v_examcnt 			= dbox.getInt("d_examcnt");
                 int v_totalscore 		= dbox.getInt("d_totalscore");
                 String v_level1text  	= dbox.getString("d_level1text");
                 String v_level2text  	= dbox.getString("d_level2text");
                 String v_level3text  	= dbox.getString("d_level3text");
                 int v_cntlevel1  		= dbox.getInt("d_cntlevel1");
                 int v_cntlevel2  		= dbox.getInt("d_cntlevel2");
                 int v_cntlevel3  		= dbox.getInt("d_cntlevel3");
                 String v_isopenanswer 	= dbox.getString("d_isopenanswer");
                 String v_isopenexp  	= dbox.getString("d_isopenexp");
                 int v_retrycnt 		= dbox.getInt("d_retrycnt");
                 int v_progress 		= dbox.getInt("d_progress");
                 String v_startdt 		= dbox.getString("d_startdt");
                 String v_enddt  		= dbox.getString("d_enddt");
                 
                 v_papernum  = getPapernumSeq(v_subj, v_gyear, v_subjseq);
                 
                isOk = insertTZ_exampaper(connMgr, v_subj, v_gyear, v_subjseq, v_lesson, v_examtype, v_papernum, v_lessonstart, v_lessonend, v_examtime, v_exampoint, v_examcnt, v_totalscore, v_cntlevel1, v_cntlevel2, v_cntlevel3, v_level1text, v_level2text, v_level3text, v_isopenanswer, v_isopenexp, v_retrycnt, v_progress, v_luserid, v_startdt, v_enddt);
                isOk2 = isOk2 + isOk;
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk2;
    }    


	
	  /**
    평가문제지  등록 (기수복사용)
    @param subj, year, subseq,userid
    @return int
    */ 
    public int insertExamPaper(String v_subj, String v_gyear, String v_subjseq, String v_luserid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String              sql     = "";
        ListSet             ls      = null;
		DataBox             dbox    = null;
        int isOk = 0;

        int  v_papernum = 0;

        try { 
            connMgr = new DBConnectionManager();
            
            connMgr.setAutoCommit(false);
            v_papernum = getPapernumSeq(v_subj, v_gyear, v_subjseq);

            sql = "select a.subj,      a.lesson, ";
            sql += "       a.examtype,     a.lessonstart, a.lessonend, ";
            sql += "       a.examtime,   a.exampoint,   a.examcnt,  a.totalscore,  a.cntlevel1, a.cntlevel2, a.cntlevel3, a.level1text, a.level2text, a.level3text, ";
            sql += "       a.isopenanswer,  a.isopenexp, a.retrycnt, a.progress, a.startdt, a.enddt ";
            sql += "  from tz_exammaster a ";
            sql += " where a.subj    = " + SQLString.Format(v_subj);
    
            ls = connMgr.executeQuery(sql); 

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                 String v_lesson    = dbox.getString("d_lesson");
                 String v_examtype     = dbox.getString("d_examtype");
                 String v_lessonstart = dbox.getString("d_lessonstart");
                 String v_lessonend   = dbox.getString("d_lessonend");
                 String v_examtime   = dbox.getString("d_examtime");
                 int v_exampoint = dbox.getInt   ("d_exampoint");
                 int v_examcnt = dbox.getInt   ("d_examcnt");
                 int v_totalscore = dbox.getInt   ("d_totalscore");
                 String v_level1text  = dbox.getString("d_level1text");
                 String v_level2text  = dbox.getString("d_level2text");
                 String v_level3text  = dbox.getString("d_level3text");
                 int v_cntlevel1  = dbox.getInt("d_cntlevel1");
                 int v_cntlevel2  = dbox.getInt("d_cntlevel2");
                 int v_cntlevel3  = dbox.getInt("d_cntlevel3");
                 String v_isopenanswer = dbox.getString("d_isopenanswer");
                 String v_isopenexp  = dbox.getString("d_isopenexp");
                 int v_retrycnt = dbox.getInt("d_retrycnt");
                 int v_progress = dbox.getInt("d_progress");
                 String v_startdt 		= dbox.getString("d_startdt");
                 String v_enddt  		= dbox.getString("d_enddt");
                isOk = insertTZ_exampaper(connMgr, v_subj, v_gyear, v_subjseq, v_lesson, v_examtype, v_papernum, v_lessonstart, v_lessonend, v_examtime, v_exampoint, v_examcnt, v_totalscore, v_cntlevel1, v_cntlevel2, v_cntlevel3, v_level1text, v_level2text, v_level3text, v_isopenanswer, v_isopenexp, v_retrycnt, v_progress, v_luserid, v_startdt, v_enddt);
  
				v_papernum++;
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


	  /**
    평가문제지  등록
    @param connMgr,
    @return int
    */ 
    public int insertTZ_exampaper(DBConnectionManager connMgr, String p_subj, String p_gyear, String p_subjseq, String p_lesson, String p_examtype, int p_papernum, String p_lessonstart, String p_lessonend, 
		                    String p_examtime,    int    p_exampoint,     int p_examcnt,             int p_totalscore, 
                            int    p_cntlevel1,  int    p_cntlevel2,  int    p_cntlevel3,  String p_level1text, String p_level2text, String p_level3text, String p_isopenanswer,
                            String    p_isopenexp,  int     p_retrycnt,       int p_progress,         String p_luserid, String p_startdt, String p_enddt ) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_EXAMPAPER table
            sql =  " insert into TZ_EXAMPAPER ";
            sql +=  " (subj,   year,   subjseq,   lesson,   ";
            sql +=  "  examtype,  papernum,   lessonstart, lessonend, examtime,  exampoint,  examcnt,  totalscore, ";
            sql +=  "  cntlevel1, cntlevel2, cntlevel3, level1text, level2text, level3text, isopenanswer,  isopenexp, ";
            sql +=  "  retrycnt, progress, luserid,   ldate, startdt, enddt   ) ";
            sql +=  " values ";
            sql +=  " (?,         ?,        ?,       ?,  ";
            sql +=  "  ?,         ?,         ?,         ?,            ?,               ?,             ?,             ?, ";
            sql +=  "  ?,         ?,         ?,         ?,            ?,                ?,            ?,                 ?,  ";
            sql +=  "  ?,         ?,         ?,       ?,    ?,   ?  ) ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, p_subj);
            pstmt.setString( 2, p_gyear);
            pstmt.setString( 3, p_subjseq);
            pstmt.setString( 4, p_lesson);
            pstmt.setString( 5, p_examtype);
			pstmt.setInt   ( 6, p_papernum);
            pstmt.setString( 7, p_lessonstart);
            pstmt.setString( 8, p_lessonend);
            pstmt.setString( 9, p_examtime);
			pstmt.setInt   ( 10, p_exampoint);
            pstmt.setInt   ( 11, p_examcnt);
            pstmt.setInt   (12, p_totalscore);
            pstmt.setInt(13, p_cntlevel1);
            pstmt.setInt(14, p_cntlevel2);
            pstmt.setInt(15, p_cntlevel3);
            pstmt.setString(16, p_level1text);
            pstmt.setString(17, p_level2text);
            pstmt.setString(18, p_level3text);
            pstmt.setString(19, p_isopenanswer);
            pstmt.setString(20, p_isopenexp);
            pstmt.setInt   (21, p_retrycnt);
            pstmt.setInt   (22, p_progress);
            pstmt.setString(23, p_luserid);
            pstmt.setString(24, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString   (25, p_startdt);
            pstmt.setString(26, p_enddt);
            
            isOk = pstmt.executeUpdate();
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


	  /**
    평가문제지  수정 
    @param box
    @return int
    */ 
    public int updateExamPaper(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        ResultSet rs = null; 

				String  v_subj      = box.getString("p_subj");
        String  v_lesson    = box.getString("p_lesson");
        String  v_gyear    = box.getString("p_gyear");
        String  v_subjseq    = box.getString("p_subjseq");
        String  v_examtype     = box.getString("p_examtype");
        int     v_papernum = box.getInt   ("p_papernum");

        String  v_lessonstart = box.getString("p_lessonstart");
        String  v_lessonend   = box.getString("p_lessonend");
        String  v_examtime   = box.getString("p_examtime");
        int     v_exampoint = box.getInt   ("p_exampoint");
        int     v_examcnt = box.getInt   ("p_examcnt");
        int     v_totalscore = box.getInt   ("p_totalscore");
        String  v_level1text  = box.getString("p_level1text");
        String  v_level2text  = box.getString("p_level2text");
        String  v_level3text  = box.getString("p_level3text");
        int  v_cntlevel1  = box.getInt("p_cntlevel1");
        int  v_cntlevel2  = box.getInt("p_cntlevel2");
        int  v_cntlevel3  = box.getInt("p_cntlevel3");
        String  v_isopenanswer = box.getString("p_isopenanswer");
        String  v_isopenexp  = box.getString("p_isopenexp");
        int     v_retrycnt = box.getInt("p_retrycnt");
        int     v_progress = box.getInt("p_progress");
        String  v_startdt = box.getString("p_startdt");
        String  v_enddt  = box.getString("p_enddt");
        
        String  v_luserid   = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql = " select count(userid) cnt from tz_examresult where subj='" +v_subj + "' and year = '" + v_gyear + "' and lesson='" +v_lesson + "' and examtype='" +v_examtype + "' AND SUBJSEQ = '"+v_subjseq+"' AND PAPERNUM = '"+v_papernum+"'";
            
            System.out.println("응시자 쿼리 : " + sql);
            
            pstmt = connMgr.prepareStatement(sql);
            rs = pstmt.executeQuery();
		    		if ( rs.next() ) { 
		    				if ( rs.getInt("cnt") > 0) isOk = -2;
		    		}	
						
						if ( isOk == 0) {  							
            	isOk = updateTZ_exampaper(connMgr, v_subj, v_gyear, v_subjseq, v_lesson, v_examtype, v_papernum, v_lessonstart, v_lessonend, v_examtime, v_exampoint, v_examcnt, v_totalscore, v_cntlevel1, v_cntlevel2, v_cntlevel3, v_level1text, v_level2text, v_level3text,  v_isopenanswer, v_isopenexp, v_retrycnt, v_progress, v_luserid, v_startdt, v_enddt);
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


	  /**
    평가문제지  수정 
    @param box
    @return int
    */ 
    public int updateTZ_exampaper(DBConnectionManager connMgr, String p_subj, String p_gyear, String p_subjseq, String p_lesson, String p_examtype, int p_papernum, String p_lessonstart, String p_lessonend, 
		                    String p_examtime,    int    p_exampoint,     int p_examcnt,             int p_totalscore, 
                            int    p_cntlevel1,  int    p_cntlevel2,  int    p_cntlevel3,  String p_level1text,  String p_level2text,  String p_level3text,  String p_isopenanswer,
                            String    p_isopenexp,  int     p_retrycnt,          int p_progress,      String p_luserid, String p_startdt, String p_enddt) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // update TZ_EXAMMASTER table
            sql =  " update TZ_EXAMPAPER ";
            sql +=  "     set   examtime = ?, ";
            sql +=  "        exampoint = ?, ";
            sql +=  "        examcnt = ?, ";
            sql +=  "        totalscore  = ?, ";
            sql +=  "        cntlevel1  = ?, ";
            sql +=  "        cntlevel2 = ?, ";
            sql +=  "        cntlevel3 = ?, ";
            sql +=  "        level1text  = ?, ";
            sql +=  "        level2text = ?, ";
            sql +=  "        level3text = ?, ";
            sql +=  "        isopenanswer  = ?, ";
            sql +=  "        isopenexp = ?, ";
            sql +=  "        retrycnt   = ?, ";
            sql +=  "        progress   = ?, ";
            sql +=  "        luserid     = ?,  ";
            sql +=  "        ldate     = ?,  ";
            sql +=  "        startdt     = ?,  ";
            sql +=  "        enddt     = ?  ";
            sql +=  "  where subj      = ?  ";
            sql +=  "    and year    = ?  ";
            sql +=  "    and subjseq    = ?  ";
			sql +=  "    and lesson    = ?  ";
            sql +=  "    and examtype     = ?  ";
            sql +=  "    and papernum     = ?  ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, p_examtime);
            pstmt.setInt   ( 2, p_exampoint);
            pstmt.setInt   ( 3, p_examcnt);
            pstmt.setInt   ( 4, p_totalscore);
            pstmt.setInt( 5, p_cntlevel1);
            pstmt.setInt   ( 6, p_cntlevel2);
            pstmt.setInt   ( 7, p_cntlevel3);
            pstmt.setString( 8, p_level1text);
            pstmt.setString   ( 9, p_level2text);
            pstmt.setString   ( 10, p_level3text);
            pstmt.setString(11, p_isopenanswer);
            pstmt.setString(12, p_isopenexp);
            pstmt.setInt    (13, p_retrycnt);
            pstmt.setInt    (14, p_progress);
            pstmt.setString(15, p_luserid);
            pstmt.setString(16, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(17, p_startdt);
            pstmt.setString(18, p_enddt);
            pstmt.setString(19, p_subj);
            pstmt.setString(20, p_gyear);
            pstmt.setString(21, p_subjseq);
            pstmt.setString(22, p_lesson);
            pstmt.setString(23, p_examtype);
            pstmt.setInt(24, p_papernum);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    

	  /**
    평가문제지  삭제  
    @param box
    @return int
    */     
    public int deleteExamPaper(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         int isOk = 0;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
	    	ResultSet rs = null; 
	    	
	String  v_subj      = box.getString("p_subj");
        String  v_lesson    = box.getString("p_lesson");
        String  v_gyear    = box.getString("p_gyear");
        String  v_subjseq    = box.getString("p_subjseq");
        String  v_examtype     = box.getString("p_examtype");
        int     v_papernum = box.getInt   ("p_papernum");
        String  v_duserid   = box.getSession("userid");

         try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
             
            sql = " select count(userid) cnt from tz_examresult where subj='" +v_subj + "'  and lesson='" +v_lesson + "' and examtype='" +v_examtype + "' ";
            pstmt = connMgr.prepareStatement(sql);
            rs = pstmt.executeQuery();
		    		if ( rs.next() ) { 
		    				if ( rs.getInt("cnt") > 0) isOk = -2;
		    		}	
						
						if ( isOk == 0) {             
               isOk = deleteTZ_exampaper(connMgr, v_subj, v_lesson, v_gyear, v_subjseq, v_examtype, v_papernum);
            }
         }
         catch ( Exception ex ) { 
             isOk = 0;
             connMgr.rollback();
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception(ex.getMessage() );
         }
         finally { 
             if ( isOk > 0 ) { connMgr.commit(); box.put("p_sulnum", String.valueOf("0") ); }
             if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }                  
             if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return isOk;
    }


	  /**
    평가문제지  삭제 
    @param box
    @return int
    */ 
    public int deleteTZ_exampaper(DBConnectionManager connMgr, String p_subj, String p_lesson, String p_gyear, String p_subjseq, String p_examtype, int p_papernum) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // update TZ_EXAMMASTER table
            sql =  " delete from TZ_EXAMPAPER ";
            sql +=  "  where subj      = ?  ";
            sql +=  "    and year    = ?  ";
            sql +=  "    and subjseq    = ?  ";
      //      sql +=  "    and lesson    = ?  ";
     //       sql +=  "    and examtype     = ?  ";
       //     sql +=  "    and papernum     = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_subj);
            pstmt.setString(2, p_gyear);
            pstmt.setString(3, p_subjseq);
       //     pstmt.setString(4, p_lesson);
       //     pstmt.setString(5, p_examtype);
      //      pstmt.setInt(6, p_papernum);

            isOk = pstmt.executeUpdate();
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
    평가문제 리스트  
    @param box
    @return Vector
    */     
    public Vector SelectQuestionList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
		Vector v_examnums = null;

        String v_subj    = box.getString("p_subj");
        String v_gyear    = box.getString("p_gyear");
        String v_subjseq = box.getString("p_subjseq");
        String v_lesson  = box.getString("p_lesson");
        String v_examtype   = box.getString("p_examtype");
        int    v_papernum= box.getInt("p_papernum");
        String v_lessonstart = box.getString("p_lessonstart");
        
        try { 
            connMgr = new DBConnectionManager();

            v_examnums = getExamnums(connMgr, v_subj, v_gyear, v_subjseq, v_lesson, v_lessonstart, v_examtype,  v_papernum, box);
   
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_examnums;
    }


    /**
    학습창에서 평가하기 페이지
    @param box          receive from the form object and session
    @return ArrayList   
    */	
	public ArrayList SelectPaperQuestionExampleList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr				= null;
        PreparedStatement pstmt					= null;
	    Vector v_examnums 						= null;
        ArrayList QuestionExampleDataList  		= null;
        ListSet ls 								= null;
        String sql 								= "";
        DataBox dbox 							= null;
        StringTokenizer st = null;
        String v_subj    		= box.getString("p_subj");
        String v_gyear    		= box.getString("p_gyear");
        String v_subjseq 		= box.getString("p_subjseq");
        String v_lesson  		= box.getString("p_lesson");
        String v_lessonstart 	= box.getString("p_lessonstart");
        String v_examtype 		= box.getString("p_examtype");
        int    v_papernum		= box.getInt   ("p_papernum");
        String v_examnum = "";
        String v_ended = "";
        int    v_userretry = 0;
        int    v_extra_time = 0;


        try { 
            connMgr = new DBConnectionManager();
            
            sql  = "SELECT A.EXAM  \n";
            sql += "     , A.ENDED  \n";
            sql += "     , A.USERRETRY  \n";
            sql += "     , B.EXAMTIME - TRUNC((SYSDATE - TO_DATE(A.STARTED,'YYYYMMDDHH24MISS')) * 24 * 60) AS EXTRA_TIME  \n";
            sql += "FROM   TZ_EXAMRESULT A  \n";
            sql += "     , TZ_EXAMPAPER B  \n";
            sql += "WHERE  A.SUBJ = B.SUBJ  \n";
            sql += "AND    A.YEAR = B.YEAR  \n";
            sql += "AND    A.SUBJSEQ = B.SUBJSEQ  \n";
            sql += "AND    A.LESSON = B.LESSON  \n";
            sql += "AND    A.EXAMTYPE = B.EXAMTYPE  \n";
            sql += "AND    A.PAPERNUM = B.PAPERNUM  \n";
            sql += "AND    A.SUBJ = " + StringManager.makeSQL(v_subj) + "  \n";
            sql += "AND    A.YEAR = " + StringManager.makeSQL(v_gyear) + "  \n";
            sql += "AND    A.SUBJSEQ = " + StringManager.makeSQL(v_subjseq) + "  \n";
            sql += "AND    A.LESSON = " + StringManager.makeSQL(v_lesson) + "  \n";
            sql += "AND    A.EXAMTYPE = " + StringManager.makeSQL(v_examtype) + "  \n";
            sql += "AND    A.PAPERNUM = " + SQLString.Format(v_papernum) + "  \n";
            sql += "AND    A.USERID = " + StringManager.makeSQL(box.getSession("userid")) + "  \n";
            
            System.out.println("@@@@@@@"+sql); 
            
            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) {
                dbox = ls.getDataBox();
                v_examnum		= ls.getString("exam");
                v_ended			= ls.getString("ended");
                v_userretry		= ls.getInt("userretry");
                v_extra_time	= ls.getInt("extra_time");
			}
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if(v_examnum.equals("")){	// 처음들어오면 문제번호가 없으므로 문제번호를 랜덤으로 구해야한다.
	            // 평가문제 번호 - 랜덤함수로 평가문제번호를 구한다. 
	            v_examnums = getExamnums(connMgr, v_subj, v_gyear, v_subjseq, v_lesson, v_lessonstart, v_examtype,  v_papernum, box);

            } else {	// 처음들어오는게 아니면 평가문제번호가 들어있다.
            	if(v_ended.equals("") && v_extra_time > 0) { // 제출시간이 없으면 제출하지 않았으므로 재응시가 아니다. 그러면 같은문제를 봐야한다. 
            		box.put("examnums", v_examnum);
            		Vector vc = box.getVector("examnums");
            		v_examnums = vc;
            	} else {
            		if(v_userretry > 0) { // 재응시 횟수가 0보다 커야만 재응시가 가능하다.
	            		// 재응시의 경우 새로 응시하는것이기 때문에 문제번호가 새로 들어가면서 몇가지의 값이  reset된다.
	            		// 평가문제 번호 - 랜덤함수로 평가문제번호를 구한다. 
	            		v_examnums = getExamnums(connMgr, v_subj, v_gyear, v_subjseq, v_lesson, v_lessonstart, v_examtype,  v_papernum, box);
	            		String strexamnum = "";
	            		Enumeration em = v_examnums.elements();
	            		while(em.hasMoreElements()){
	            			strexamnum  += (String)em.nextElement() + ",";
	            		}
	            		
	            		sql  = "\r\n UPDATE TZ_EXAMRESULT SET STARTED = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ANSWER = NULL, ANSWERCNT = 0, EXAM = ?, EXAMCNT = 0, EXAMPOINT=0 ,ENDED = NULL, SCORE = 0, CORRECTED = 0, USERRETRY = USERRETRY - 1 ";
	                    sql += "\r\n WHERE SUBJ 		= " + StringManager.makeSQL(v_subj);
	                    sql += "\r\n   AND LESSON 		= " + StringManager.makeSQL(v_lesson);
	                    sql += "\r\n   AND EXAMTYPE 	= " + StringManager.makeSQL(v_examtype);
	                    sql += "\r\n   AND YEAR 		= " + StringManager.makeSQL(v_gyear);
	                    sql += "\r\n   AND SUBJSEQ 		= " + StringManager.makeSQL(v_subjseq);
	                    sql += "\r\n   AND PAPERNUM 	= " + v_papernum;
	                    sql += "\r\n   AND USERID 		= " + StringManager.makeSQL(box.getSession("userid"));
//System.out.println("@@@@@@@"+sql); 
	                    pstmt = connMgr.prepareStatement(sql);
	                    pstmt.setString(1, strexamnum);
	                    int isOk  = pstmt.executeUpdate();
	                    if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            		}
            	}
            }

            if(v_examnums != null) { 
                // 평가번호에 해당하는 문제리스트를 만든다. 리스트((설문번호1, 보기1,2,3..))
                QuestionExampleDataList = getExampleData(connMgr, v_subj, v_examnums);
            }

			if ( QuestionExampleDataList == null ) { 
			    QuestionExampleDataList = new ArrayList();
			}
        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally {
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        	if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return QuestionExampleDataList;
    }

    /**
    평가번호에 해당하는 문제리스트
    @param box          receive from the form object and session
    @return QuestionExampleData   평가문제
    */
    public ArrayList getExampleData(DBConnectionManager connMgr, String p_subj,  Vector p_examnums) throws Exception { 
        Hashtable hash = new Hashtable();
        ArrayList blist = new ArrayList();
		ArrayList           list    = null;

        ListSet ls  = null;
        String sql  = "";
        DataBox             dbox    = null;
        StringTokenizer st = null;

        String v_examnums = "";
        for ( int i = 0; i < p_examnums.size(); i++ ) { 
            v_examnums += (String)p_examnums.get(i);
            if ( i<p_examnums.size()-1) { 
                v_examnums += ",";
            }
        }
        if ( v_examnums.equals("")) v_examnums = "-1";

        try { 

			st = new StringTokenizer(v_examnums, ",");

            while ( st.hasMoreElements() ) { 

            	int examnum = Integer.parseInt(st.nextToken() );
            	sql = "select a.subj,     a.examnum,  a.lesson,  a.examtype, ";
	            sql += "       a.examtext,      a.exptext,   a.levels,  a.selcount,  a.saveimage,   a.saveaudio, ";
	            sql += "       a.savemovie,  a.saveflash, a.realimage,   a.realaudio, a.realmovie, a.realflash, ";
	            sql += "       b.selnum,   b.seltext,  b.isanswer,  ";
	            sql += "       b.saveselimage,   b.realselimage,  b.isselimage,  ";
	            sql += "       c.codenm    examtypenm, ";
	            sql += "       d.codenm    levelsnm    ";
	            sql += "  from tz_exam      a, ";
	            sql += "       tz_examsel   b, ";
	            sql += "       tz_code      c, ";
	            sql += "       tz_code      d  ";
	            sql += " where a.subj     = b.subj( +) ";
	            sql += "   and a.examnum  = b.examnum( +) ";
	            sql += "   and a.examtype = c.code ";
	            sql += "   and a.levels   = d.code ";
	            sql += "   and a.subj     = " + SQLString.Format(p_subj);
	            sql += "   and a.examnum  = " + examnum;
	            sql += "   and c.gubun    = " + SQLString.Format(ExamBean.EXAM_TYPE);
	            sql += "   and d.gubun    = " + SQLString.Format(ExamBean.EXAM_LEVEL);
	            sql += "   and a.isuse    = 'Y'       \n";
	            sql += " order by a.examnum, b.selnum ";

            	ls = connMgr.executeQuery(sql);
				list =  new ArrayList();

            	while ( ls.next() ) { 
                	dbox = ls.getDataBox();
                	list.add(dbox);

				}
			    blist.add(list);
			    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return blist;
    }
    /**
    평가시험 보는 시작시간을 구해 평가를 보는 시간을 제어
    @param box          receive from the form object and session
    @return QuestionExampleData   평가문제
    */	
	public DataBox selectStartTime(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr				= null;
        ListSet ls 								= null;
        DataBox dbox 							= null;
        String sql 								= "";
        
        String v_subj    		= box.getString("p_subj");
        String v_gyear    		= box.getString("p_gyear");
        String v_subjseq 		= box.getString("p_subjseq");
        String v_lesson  		= box.getString("p_lesson");
        String v_lessonstart 	= box.getString("p_lessonstart");
        String v_examtype 		= box.getString("p_examtype");
        int    v_papernum		= box.getInt   ("p_papernum");

        String v_started 		= "";
        
        try { 
            connMgr = new DBConnectionManager();
            
            sql  = "SELECT CASE WHEN D.ENDED IS NOT NULL OR D.ENDED != '' THEN A.EXAMTIME  \n";
            sql += "            ELSE DECODE(D.STARTED, NULL, A.EXAMTIME, '', A.EXAMTIME  \n";
            sql += "                      , A.EXAMTIME - TRUNC((SYSDATE - TO_DATE(D.STARTED,'YYYYMMDDHH24MISS')) * 24 * 60))  \n";
            sql += "       END AS EXTRATIME, D.STARTED, D.ANSWER  \n";
            sql += "FROM   TZ_EXAMPAPER A  \n";
            sql += "     , TZ_EXAMRESULT D  \n";
            sql += "WHERE  A.SUBJ = D.SUBJ (+)  \n";
            sql += "AND    A.YEAR = D.YEAR (+)  \n";
            sql += "AND    A.SUBJSEQ = D.SUBJSEQ (+)  \n";
            sql += "AND    A.LESSON = D.LESSON (+)  \n";
            sql += "AND    A.EXAMTYPE = D.EXAMTYPE (+)  \n";
            sql += "AND    A.PAPERNUM = D.PAPERNUM (+)  \n";
            sql += "AND    D.USERID (+) = " + StringManager.makeSQL(box.getSession("userid")) + "  \n";
            sql += "AND    A.SUBJ = " + StringManager.makeSQL(v_subj) + "  \n";
            sql += "AND    A.YEAR = " + StringManager.makeSQL(v_gyear) + "  \n";
            sql += "AND    A.SUBJSEQ = " + StringManager.makeSQL(v_subjseq) + "  \n";
            sql += "AND    A.LESSON = " + StringManager.makeSQL(v_lesson) + "  \n";
            sql += "AND    A.EXAMTYPE = " + StringManager.makeSQL(v_examtype) + "  \n";
            sql += "AND    A.PAPERNUM = " + SQLString.Format(v_papernum) + "  \n";
            sql += "ORDER BY  \n";
            sql += "       A.SUBJ  \n";
            sql += "     , A.YEAR  \n";
            sql += "     , A.SUBJSEQ  \n";
            sql += "     , A.LESSON  \n";
            sql += "     , A.EXAMTYPE  \n";

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) {
                dbox = ls.getDataBox();
			}
            
        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally {
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }


    /**
    평가 번호 구하기
    @param box          receive from the form object and session
    @return Vector
    */
    public Vector getExamnums(DBConnectionManager connMgr,
                        String p_subj, String p_gyear, String p_subjseq, String p_lesson, String p_lessonstart, String p_examtype,
                        int p_papernum, RequestBox box) throws Exception { 
   
		ArrayList examlist   = new ArrayList();
		ArrayList           list    = null;
		ArrayList list2 = null;
		Vector v_lessons = null;
        Vector v_examnums = new Vector();
        Vector v_realrkeys = null; // jkh 0228
		Vector v = null;
        Vector v_realnums = null;
		Vector v_level1Obnums = null;
		Vector v_level2Obnums = null;
		Vector v_level3Obnums = null;
		Vector v_level1Subnums = null;
		Vector v_level2Subnums = null;
		Vector v_level3Subnums = null;
		Vector v_level1OXnums = null;
		Vector v_level2OXnums = null;
		Vector v_level3OXnums = null;
		
		int lessonstart = Integer.parseInt(p_lessonstart);

		try { 
			//examlist   = getPaperQuestionList(connMgr, p_subj, p_gyear, p_subjseq, p_lesson, p_examtype, p_papernum, box);
	          list = getLevelQuestionList(connMgr, p_subj, p_gyear, p_subjseq, p_lesson, p_examtype, p_papernum, box);

			  v_lessons = (Vector)list.get(0);
              v_level1Obnums = (Vector)list.get(1);
              v_level2Obnums = (Vector)list.get(2);
              v_level3Obnums = (Vector)list.get(3);
              v_level1Subnums = (Vector)list.get(4);
              v_level2Subnums = (Vector)list.get(5);
              v_level3Subnums = (Vector)list.get(6);
              v_level1OXnums = (Vector)list.get(7);
              v_level2OXnums = (Vector)list.get(8);
              v_level3OXnums = (Vector)list.get(9);

              v_realnums = new Vector();
              v_realrkeys = new Vector();
              String numandrkey = ""; 
              StringTokenizer st1= null;	

              for ( int i = 0; i < v_lessons.size() ; i++ ) { 
				  for ( int j = 1; j <= 3; j++ ) { 
					  Integer type = new Integer(j);
					  for ( int k = 1; k <= 3 ; k++ ) { 
						  Integer level = new Integer(k);
             			  v = getQuestionList(connMgr, p_subj, (String)v_lessons.get(i), type.toString() , level.toString() );
						  Random ran = new Random();

                                
                          if ( j == 1 && k == 1) { 
						     int  ss = Integer.parseInt((String)v_level1Obnums.get(i));
								     
							 if ( v.size() > 0 && ss > 0) {
							     for ( int p=0; p < ss ; p++ ) { 							        
							     	numandrkey = (String)v.get(p);
							     	st1 = new StringTokenizer(numandrkey, ",");
							     	v_realnums.add(st1.nextToken() );
							     	v_realrkeys.add(st1.nextToken() );
     	
							     }
							 }
						  } else if ( j == 1 && k == 2) { 
						     int  ss = Integer.parseInt((String)v_level2Obnums.get(i));
							 if ( v.size() > 0 && ss > 0) { 
							     for ( int p=0; p < ss ; p++ ) { 
									 numandrkey = (String)v.get(p); 
							     	st1 = new StringTokenizer(numandrkey, ",");
							     	v_realnums.add(st1.nextToken() );
							     	v_realrkeys.add(st1.nextToken() );
							     }
							 }
						  } else if ( j == 1 && k == 3) { 
						     int  ss = Integer.parseInt((String)v_level3Obnums.get(i));
							 if ( v.size() > 0 && ss > 0) { 
							     for ( int p=0; p < ss ; p++ ) { 
									 numandrkey = (String)v.get(p); 
							     	st1 = new StringTokenizer(numandrkey, ",");
							     	v_realnums.add(st1.nextToken() );
							     	v_realrkeys.add(st1.nextToken() );
							     }
							 }
						  } else if ( j == 2 && k == 1) { 
						     int  ss = Integer.parseInt((String)v_level1Subnums.get(i));
							 if ( v.size() > 0 && ss > 0) { 
							     for ( int p=0; p < ss ; p++ ) { 
									 numandrkey = (String)v.get(p); 
							     	st1 = new StringTokenizer(numandrkey, ",");
							     	v_realnums.add(st1.nextToken() );
							     	v_realrkeys.add(st1.nextToken() );
							     }
							 }
						  } else if ( j == 2 && k == 2) { 
						     int  ss = Integer.parseInt((String)v_level2Subnums.get(i));
							 if ( v.size() > 0 && ss > 0) { 
							     for ( int p=0; p < ss ; p++ ) { 
									 numandrkey = (String)v.get(p); 
							     	st1 = new StringTokenizer(numandrkey, ",");
							     	v_realnums.add(st1.nextToken() );
							     	v_realrkeys.add(st1.nextToken() );
							     }
							 }
						  } else if ( j == 2 && k == 3) { 
						     int  ss = Integer.parseInt((String)v_level3Subnums.get(i));
							 if ( v.size() > 0 && ss > 0) { 
							     for ( int p=0; p < ss ; p++ ) { 
									 numandrkey = (String)v.get(p); 
							     	st1 = new StringTokenizer(numandrkey, ",");
							     	v_realnums.add(st1.nextToken() );
							     	v_realrkeys.add(st1.nextToken() );
							     }
							 }
							 
                         } else if ( j == 3 && k == 1) { 
						     int  ss = Integer.parseInt((String)v_level1OXnums.get(i));
								     
							 if ( v.size() > 0 && ss > 0) {
							     for ( int p=0; p < ss ; p++ ) { 							        
							     	numandrkey = (String)v.get(p);
							     	st1 = new StringTokenizer(numandrkey, ",");
							     	v_realnums.add(st1.nextToken() );
							     	v_realrkeys.add(st1.nextToken() );
     	
							     }
							 }
						  } else if ( j == 3 && k == 2) { 
						     int  ss = Integer.parseInt((String)v_level2OXnums.get(i));
							 if ( v.size() > 0 && ss > 0) { 
							     for ( int p=0; p < ss ; p++ ) { 
									 numandrkey = (String)v.get(p); 
							     	st1 = new StringTokenizer(numandrkey, ",");
							     	v_realnums.add(st1.nextToken() );
							     	v_realrkeys.add(st1.nextToken() );
							     }
							 }
						  } else if ( j == 3 && k == 3) { 
						     int  ss = Integer.parseInt((String)v_level3OXnums.get(i));
							 if ( v.size() > 0 && ss > 0) { 
							     for ( int p=0; p < ss ; p++ ) { 
									 numandrkey = (String)v.get(p); 
							     	st1 = new StringTokenizer(numandrkey, ",");
							     	v_realnums.add(st1.nextToken() );
							     	v_realrkeys.add(st1.nextToken() );
							     }
							 }
						  }
						  
						  
                      }
                  }
              }

			  int  ss = v_realnums.size();
		  
							 // 랜덤 구하기
                      /*       Random ran = new Random();
                             boolean[] b = new boolean[ss];
							 int [] i = new int [ss];
							 int cnt=0;
							 while ( cnt<ss) { 
								 int r = ran.nextInt(ss); 
								 if ( b[r]) continue;
								 else { 
									 b[r] = true;
									 i[cnt] = r; 
									 cnt++;
                                 }
                             }

                             int [] num =new int [ss];
							 int idx = 0;
							 for ( int k=0; k<b.length; k++ ) { 
								 if ( b[k]) num[idx++] = i[k];
                             }*/
                             
                             Random ran = new Random();
                             int [] num =new int [ss];
							 int bun = 0;
							
                             for ( int q = 0 ; q < ss ; q++ ) { 
                                 bun = ran.nextInt(ss); 
                                 
                                 	  int breakint = 0;
                                 	  int isequal = 0;
                                      while ( isequal < 1) { 
                                      	bun = ran.nextInt(ss); 
                                      	for ( int a = 0 ; a < q ; a++ ) { 
                                      		if ( 	num[a] == bun) { 
                                      			isequal = 0;
                                      			break;
                                      		} else { 	isequal = 1;  }
                                      	}	
                                              breakint++;
                                              if ( breakint > 10000) { break; }
                                      } 
                                  
								 num[q] = bun;
							 }
							 

							     for ( int p=0; p < num.length ; p++ ) { 
									 v_examnums.add((String)v_realnums.get(num[p]));
							     }

        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
   
        return v_examnums;
    }


    /**
    평가 문제 구성  구하기
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList getLevelQuestionList(DBConnectionManager connMgr,
                        String p_subj, String p_gyear, String p_subjseq, String p_lesson, String p_examtype,
                        int p_papernum, RequestBox box) throws Exception { 
   
		DataBox             dbox    = null;
		ArrayList           list    = null;
        Vector v_lessons1 = new Vector();
        Vector v_level1Obnums = new Vector();
        Vector v_level2Obnums = new Vector();
        Vector v_level3Obnums = new Vector();
        Vector v_level1Subnums = new Vector();
        Vector v_level2Subnums = new Vector();
        Vector v_level3Subnums = new Vector();
        Vector v_level1OXnums = new Vector();
        Vector v_level2OXnums = new Vector();
        Vector v_level3OXnums = new Vector();
        
        Vector v_sulnums2 = new Vector();
        StringTokenizer st = null;
        StringTokenizer st2 = null;
        StringTokenizer st3= null;	


		try { 
            dbox = getExamPaperData(connMgr, p_subj, p_gyear, p_subjseq, p_lesson, p_examtype, p_papernum);
            list = new ArrayList(); 

			st = new StringTokenizer(dbox.getString("d_level1text"), "/");
			
			while ( st.hasMoreElements() ) { 
        
				String lst = st.nextToken();
				st2 = new StringTokenizer( lst, "|");		
				
				String lst2 = st2.nextToken();
				st3 = new StringTokenizer( lst2, ",");
				
				String ss = st3.nextToken();
				if ( ss.length() == 1) { 
				    ss = "00" + ss; 
			   } else if ( ss.length() == 2) { 
			   		ss = "0" + ss;	
			  }
				v_lessons1.add(ss);
				v_level1Obnums.add(st3.nextToken() );

                // 주관식
                lst2 = st2.nextToken();
		        st3 = new StringTokenizer( lst2, ",");
                String s = st3.nextToken();                
			    v_level1Subnums.add(st3.nextToken() );

                // OX식
                lst2 = st2.nextToken();
		        st3 = new StringTokenizer( lst2, ",");
                String sOX1 = st3.nextToken();                
			    v_level1OXnums.add(st3.nextToken() );			    
			}

			st = new StringTokenizer(dbox.getString("d_level2text"), "/");
			while ( st.hasMoreElements() ) { 
                String lst = st.nextToken();
				st2 = new StringTokenizer( lst, "|");
                  String lst2 = st2.nextToken();

			      st3 = new StringTokenizer( lst2, ",");
				  String ss = st3.nextToken();
				  v_level2Obnums.add(st3.nextToken() );

                  lst2 = st2.nextToken();
			      st3 = new StringTokenizer( lst2, ",");
                  String s = st3.nextToken();
				  v_level2Subnums.add(st3.nextToken() );

                  lst2 = st2.nextToken();
			      st3 = new StringTokenizer( lst2, ",");
                  String sOX2 = st3.nextToken();
				  v_level2OXnums.add(st3.nextToken() );
				  
				  
			}
			st = new StringTokenizer(dbox.getString("d_level3text"), "/");
			while ( st.hasMoreElements() ) { 
                String lst = st.nextToken();
				st2 = new StringTokenizer( lst, "|");
                      String lst2 = st2.nextToken();

				      st3 = new StringTokenizer( lst2, ",");
					  String ss = st3.nextToken();

					  v_level3Obnums.add(st3.nextToken() );

                      lst2 = st2.nextToken();
				      st3 = new StringTokenizer( lst2, ",");
                      String s = st3.nextToken();
					  v_level3Subnums.add(st3.nextToken() );
					  
                      lst2 = st2.nextToken();
				      st3 = new StringTokenizer( lst2, ",");
                      String sOX3 = st3.nextToken();
					  v_level3OXnums.add(st3.nextToken() );					  
			}

            list.add(v_lessons1);
            list.add(v_level1Obnums);  // 상 - 객관식
            list.add(v_level2Obnums);  // 중 - 객관식
            list.add(v_level3Obnums);  // 하 - 객관식
            list.add(v_level1Subnums); // 상 - 주관식  
            list.add(v_level2Subnums); // 중 - 주관식 
            list.add(v_level3Subnums); // 하 - 주관식 
            list.add(v_level1OXnums); // 상 - OX식 
            list.add(v_level2OXnums); // 중 - OX식 
            list.add(v_level3OXnums); // 하 - OX식             
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        return list;
    }

    /**
    평가문제 리스트 
    @param box          receive from the form object and session
    @return Vector   
    */
    public Vector getQuestionList(DBConnectionManager connMgr, String p_subj, String p_lesson, String p_examtype, String p_levels) throws Exception { 
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;
        Vector v_examnums = new Vector();

        try { 
            sql = "select a.subj,   a.examnum, a.lesson,  a.examtype, ";
            sql += "       a.examtext,    a.exptext,     ";
            sql += "       a.levels,   a.selcount,  a.saveimage,   a.saveaudio, a.savemovie, a.saveflash,  a.realimage,   a.realaudio, a.realmovie, a.realflash, DBMS_RANDOM.RANDOM rkey ";
            sql += "  from tz_exam   a ";
            sql += "   where a.subj     = " + SQLString.Format(p_subj);
            sql += "   and a.lesson    = " + SQLString.Format(p_lesson);          
			sql += "   and a.examtype    = " + SQLString.Format(p_examtype);
            sql += "   and a.levels    = " + SQLString.Format(p_levels);
            sql += "   and a.isuse      = 'Y' \n";
      //      sql += " order by a.examnum ";
            sql += " order by rkey "; // random 적용 jkh 2005.02.28

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                // v_examnums.add(dbox.getString("d_examnum") );
                v_examnums.add(dbox.getString("d_examnum") + "," +dbox.getString("d_rkey") );
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_examnums;
    }

    /**
    문제지 데이타 
    @param box          receive from the form object and session
    @return PaperData
    */
    public DataBox getPaperData(RequestBox box) throws Exception { 
    
		DBConnectionManager connMgr     = null;

        DataBox     dbox = null;

        String v_grcode    = box.getString("p_grcode");
        String v_subj      = box.getString("p_subj");
        java.util.Date d_now = new java.util.Date();
        String v_gyear    = box.getStringDefault("p_gyear", String.valueOf(d_now.getYear() +1900));
        String v_subjseq   = box.getString("p_subjseq");
		int v_papernum = box.getInt("p_papernum");


        int    v_sulpapernum = box.getInt("p_sulpapernum");

        try { 
            connMgr = new DBConnectionManager();
            dbox =  getPaperData(connMgr, v_grcode, v_subj, v_gyear, v_subjseq, v_papernum);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }

    /**
    문제지 데이타 
    @param box          receive from the form object and session
    @return PaperData
    */
    public DataBox getPaperData(DBConnectionManager connMgr, String p_grcode, String p_subj, String p_gyear, String p_subjseq, int p_papernum) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        DataBox     dbox = null;

        try { 
                sql = getPaperListSQL(p_grcode, p_subj, p_gyear, p_subjseq, p_papernum);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
				dbox = ls.getDataBox();
            
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        if ( dbox == null ) dbox = new DataBox("resoponsebox");

        return dbox;
    }


    /**
    리스트 sql문 
    @param box          receive from the form object and session
    @return String
    */
    public String getPaperListSQL(String p_grcode, String p_subj, String p_gyear, String p_subjseq, int p_papernum) throws Exception { 
        String              sql     = "";
   
            sql = "select b.subj,   a.year,    a.subjseq,   a.lesson, ";
            sql += "       a.examtype,  a.papernum,   a.lessonstart, a.lessonend,  a.examtime, a.exampoint, a.examcnt, a.totalscore,  ";
            sql += "       a.cntlevel1, a.cntlevel2, a.cntlevel3, a.isopenanswer, ";
            sql += "       a.isopenexp,  a.retrycnt,  a.progress, b.subjnm,  GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", nvl(a.examtype, '')) examtypenm ";
            sql += "     , (CASE WHEN TO_CHAR(SYSDATE,'YYYYMMDD') BETWEEN A.STARTDT AND A.ENDDT THEN 'Y' ELSE 'N' END) AS CHCK \n";
            sql += "  from tz_exampaper a, ";
            sql += "       tz_subj       b  ";
            sql += " where a.subj( +)        = b.subj  ";
            sql += "   and a.subj   = " + SQLString.Format(p_subj);
            sql += "   and a.year   = " + SQLString.Format(p_gyear);
            sql += "   and a.subjseq   = " + SQLString.Format(p_subjseq);
            sql += "   and a.papernum   = " + SQLString.Format(p_papernum);
            sql += " order by a.subj, a.year, a.subjseq, a.lesson, a.examtype  ";	

        return sql;
    }

    /**
    일련번호 구하기 
    @param box          receive from the form object and session
    @return int
    */
    public int getPapernumSeq(String p_subj, String p_gyear, String p_subjseq) throws Exception { 
        Hashtable maxdata = new Hashtable();
        maxdata.put("seqcolumn","papernum");
        maxdata.put("seqtable","tz_exampaper");
        maxdata.put("paramcnt","3");
        maxdata.put("param0","subj");
        maxdata.put("subj",   SQLString.Format(p_subj));
        maxdata.put("param1","year");
        maxdata.put("year",   SQLString.Format(p_gyear));
        maxdata.put("param2","subjseq");
        maxdata.put("subjseq",   SQLString.Format(p_subjseq));

        return SelectionUtil.getSeq(maxdata);
    }
    
    
    /**
     기간 수정
    @param box
    @return int
    */ 
    public int updateExamDate(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1  = null;
        String              sql     = "";
        int isOk1 =0;

		String  v_subj      = box.getString("p_subj");
        String  v_gyear    = box.getString("p_gyear");
        String  v_subjseq    = box.getString("p_subjseq");
        String  v_startdt    = box.getString("p_startdt");
        String  v_enddt    = box.getString("p_enddt");
       
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql = "	update tz_exampaper 					\n"
            	+ " 		set startdt = REPLACE('"+v_startdt+"', '-', ''), enddt = REPLACE('"+v_enddt+"', '-', '') \n"
            	+ " 		where subj = '"+v_subj+"'					\n"
            	+ " 		and subjseq = '"+v_subjseq+"'					\n"
            	+ " 		and year = '"+v_gyear+"'					\n";
   
            pstmt1 = connMgr.prepareStatement(sql);

//System.out.println("-=---------------"+sql);        

            isOk1 = pstmt1.executeUpdate();
            
//System.out.println("isOk1 ::::::::"+isOk1);
		
		 if ( isOk1 > 0  ) { 
             connMgr.commit();
         } else { 
             connMgr.rollback();
         }
     } catch ( Exception ex ) { 
         connMgr.rollback();
         ErrorManager.getErrorStackTrace(ex, box, sql);
         throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
     } finally { 
         if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
         if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
     }

     return isOk1;
 }

}
