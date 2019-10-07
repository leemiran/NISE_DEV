// **********************************************************
// 1. 제      목:
// 2. 프로그램명: ExamQuestionBean.java
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
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
public class ExamQuestionBean {
    private ConfigSet config;

    public ExamQuestionBean() { 
        try { 
            config = new ConfigSet();
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    
    /**
    평가 문제 리스트 
    @param box          receive from the form object and session
    @return ArrayList   평가문제 리스트
    */
    public ArrayList selectQuestionList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	        = null;
        ArrayList           list            = null;

        String              ss_subjcourse   = box.getString("s_subjcourse");
        String              v_action        = box.getStringDefault("p_action",  "change");

        try { 
            if ( v_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list    = getQuestionList(connMgr, ss_subjcourse);
            } else { 
                list = new ArrayList();
            }
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

        return list;
    }            

    
    /**
    평가 문제 리스트 
    @param box          receive from the form object and session
    @return ArrayList   평가문제 리스트
    */
    public ArrayList getQuestionList(DBConnectionManager connMgr, String p_subj) throws Exception { 
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;

        try { 
            
            sql  = "\n select a.subj,   a.examnum, a.lesson,  a.examtype, ";
            sql += "\n        a.examtext,    a.exptext,     ";
            sql += "\n        a.levels,   a.selcount,  a.saveimage,   a.saveaudio, a.savemovie, a.saveflash,  a.realimage,   a.realaudio, a.realmovie, a.realflash, ";
            sql += "\n        b.codenm    examtypenm, ";
            sql += "\n        c.codenm    levelsnm, a.isuse    ";
            sql += "\n      , decode(( ";
            sql += "\n                select count(*) cnt ";
            sql += "\n                from   tz_examresult ";
            sql += "\n                where  subj = " + StringManager.makeSQL(p_subj);
            sql += "\n                and    ',' || exam || ',' like '%,' || a.examnum || ',%' " ;
            sql += "\n               ),0,'Y','N') as delYn ";
            sql += "\n from   tz_exam   a, ";
            sql += "\n        tz_code   b, ";
            sql += "\n        tz_code   c  ";
            sql += "\n where  a.examtype = b.code ";
            sql += "\n and    a.levels   = c.code ";
            sql += "\n and    a.subj     = " + StringManager.makeSQL(p_subj);
            sql += "\n and    b.gubun    = " + StringManager.makeSQL(ExamBean.EXAM_TYPE);
            sql += "\n and    c.gubun    = " + StringManager.makeSQL(ExamBean.EXAM_LEVEL);
            sql += "\n order  by a.examnum ";

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return list;
    }

    /**
    문제 내용 보기
    @param box          receive from the form object and session
    @return QuestionExampleData   평가문제
    */
    public ArrayList selectExampleData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_subj     = box.getString("p_subj");
        String v_grcode     = box.getString("p_grcode");
        int    v_examnum  = box.getInt("p_examnum");
        String v_action   = box.getStringDefault("p_action","change");

        try { 
            if ( v_action.equals("go") && v_examnum > 0 ) { 
                connMgr = new DBConnectionManager();
                list = getExampleData(connMgr, v_subj,  v_examnum);
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
    문제 내용 보기    
    @param box          receive from the form object and session
    @return QuestionExampleData   평가문제
    */
    public ArrayList getExampleData(DBConnectionManager connMgr, String p_subj,  int p_examnum) throws Exception { 
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;

        try { 
            sql = "select a.subj,     a.examnum,  a.lesson,  a.examtype, ";
            sql += "       a.examtext,      a.exptext,   a.levels,  a.selcount,  a.saveimage,   a.saveaudio, ";
            sql += "       a.savemovie,  a.saveflash, a.realimage,   a.realaudio, a.realmovie, a.realflash, ";
            sql += "       b.selnum,   b.seltext,  b.isanswer,  ";
            sql += "       b.saveselimage,   b.realselimage,  b.isselimage,  ";
            sql += "       c.codenm    examtypenm, ";
            sql += "       d.codenm    levelsnm, a.isuse    ";
            sql += "  from tz_exam      a, ";
            sql += "       tz_examsel   b, ";
            sql += "       tz_code      c, ";
            sql += "       tz_code      d  ";
            sql += " where a.subj     = b.subj( +) ";
            sql += "   and a.examnum  = b.examnum( +) ";
            sql += "   and a.examtype = c.code ";
            sql += "   and a.levels   = d.code ";
            sql += "   and a.subj     = " + SQLString.Format(p_subj);
            sql += "   and a.examnum  = " + SQLString.Format(p_examnum);
            sql += "   and c.gubun    = " + SQLString.Format(ExamBean.EXAM_TYPE);
            sql += "   and d.gubun    = " + SQLString.Format(ExamBean.EXAM_LEVEL);
            sql += " order by a.examnum, b.selnum ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return list;
    }

    /**
    풀등록 에서 평가문제 리스트 
    @param box          receive from the form object and session
    @return ArrayList   평가문제 리스트
    */
    public ArrayList selectQuestionPool(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr     = null;
        ArrayList           list        = null;

        String              v_subj      = box.getString("p_subj");
        String              v_action    = box.getStringDefault("p_action",  "change");

        try { 
            if ( v_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list    = getQuestionPool(connMgr, v_subj);
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
    풀등록 에서 평가문제 리스트     
    @param box          receive from the form object and session
    @return ArrayList   평가문제 리스트
    */
    public ArrayList getQuestionPool(DBConnectionManager connMgr, String p_subj) throws Exception { 
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        StringBuffer        sbSQL   = new StringBuffer("");
        DataBox             dbox    = null;
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        try { 

            sbSQL.append(" select  a.subj                                                               \n")
                 .append("     ,   a.examnum                                                            \n")
                 .append("     ,   a.lesson                                                             \n")
                 .append("     ,   a.examtype                                                           \n")
                 .append("     ,   a.examtext                                                           \n")
                 .append("     ,   a.exptext                                                            \n")
                 .append("     ,   a.levels                                                             \n")
                 .append("     ,   a.selcount                                                           \n")
                 .append("     ,   a.saveimage                                                          \n")
                 .append("     ,   a.saveaudio                                                          \n")
                 .append("     ,   a.savemovie                                                          \n")
                 .append("     ,   a.saveflash                                                          \n")
                 .append("     ,   a.realimage                                                          \n")
                 .append("     ,   a.realaudio                                                          \n")
                 .append("     ,   a.realmovie                                                          \n")
                 .append("     ,   a.realflash                                                          \n")
                 .append("     ,   b.codenm        examtypenm                                           \n")
                 .append("     ,   c.codenm        levelsnm                                             \n")
                 .append("     ,   d.subjnm        subjnm                                               \n")
                 .append(" from    tz_exam         a                                                    \n")
                 .append("     ,   tz_code         b                                                    \n")
                 .append("     ,   tz_code         c                                                    \n")
                 .append("     ,   tz_subj         d                                                    \n")
                 .append(" where   a.examtype   = b.code                                                \n")
                 .append(" and     a.levels    = c.code                                                 \n")
                 .append(" and     a.subj      = d.subj                                                 \n")
                 .append(" and     a.subj    ! = " + SQLString.Format(p_subj               ) + "        \n")
                 .append(" and     b.gubun     = " + SQLString.Format(ExamBean.EXAM_TYPE   ) + "        \n")
                 .append(" and     c.gubun     = " + SQLString.Format(ExamBean.EXAM_LEVEL  ) + "        \n")
                 .append(" order by a.subj, a.examnum                                                   \n");
                 
            System.out.println(this.getClass().getName() + "." + "selectListCourseProgress() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                dbox    = ls.getDataBox();

                list.add(dbox);
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
    평가 문제 를 찾기위한 Pool
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public ArrayList selectQuestionPoolList(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String              sql     = "";        
        
        try {      
            String ss_searchtype = box.getString("s_searchtype");
            String ss_searchtext = box.getString("s_searchtext");
            
            String v_action  = box.getString("p_action");
            String v_subj  = box.getString("p_subj");
             
            list = new ArrayList();
            
            if ( v_action.equals("go") ) {   
                connMgr = new DBConnectionManager();
                
            sql = "select a.subj,   a.examnum, a.lesson,  a.examtype, ";
            sql += "       a.examtext,    a.exptext,     ";
            sql += "       a.levels,   a.selcount,  a.saveimage,   a.saveaudio, a.savemovie, a.saveflash,  a.realimage,   a.realaudio, a.realmovie, a.realflash, ";
            sql += "       b.codenm    examtypenm, ";
            sql += "       c.codenm    levelsnm,    ";
            sql += "       d.subjnm    subjnm    ";
            sql += "  from tz_exam   a, ";
            sql += "       tz_code   b, ";
            sql += "       tz_code   c,  ";
            sql += "       tz_subj   d  ";
            sql += "   where a.examtype = b.code ";
            sql += "   and a.levels   = c.code ";
            sql += "   and a.subj   = d.subj ";
            sql += "   and a.subj    != " + SQLString.Format(v_subj);
            sql += "   and b.gubun    = " + SQLString.Format(ExamBean.EXAM_TYPE);
            sql += "   and c.gubun    = " + SQLString.Format(ExamBean.EXAM_LEVEL);

                if ( ss_searchtype.equals("1") ) {  // 과목명
                    sql += "  and d.subjnm like " + SQLString.Format("%" +ss_searchtext + "%");
                } 
                else if ( ss_searchtype.equals("2") ) {  // 문제
                    sql += "  and a.examtext like " + SQLString.Format("%" +ss_searchtext + "%");
                } 
                else if ( ss_searchtype.equals("3") ) {  // 난이도
                    sql += "  and c.codenm like " + SQLString.Format("%" +ss_searchtext + "%");
                }
				else if ( ss_searchtype.equals("4") ) {  // 문제분류
                    sql += "  and b.codenm like " + SQLString.Format("%" +ss_searchtext + "%");
                }

            sql += " order by a.subj, a.examnum ";
                
                ls = connMgr.executeQuery(sql);// System.out.println(sql);
                
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();
                    
                    list.add(dbox);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }	

    /**
    문제 등록   
    @param box          receive from the form object and session
    @return int
    */
    public int insertQuestion(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String  v_subj     = box.getString("p_subj");
        String  v_grcode    = box.getString("p_grcode");
        int     v_examnum  = 0;
        String  v_lesson   = box.getString("p_lesson");
        String  v_examtype = box.getString("p_examtype");
        String  v_examtext     = box.getString("p_examtext");
        String  v_exptext  = box.getString("p_exptext");
        String  v_levels   = box.getString("p_levels");
        String  v_isuse    = box.getString("p_isuse");

        int    v_selnum     = 0;
        Vector v_seltexts   = box.getVector("p_seltext");
        String v_seltext    = "";
        Vector v_isanswers  = box.getVector("p_isanswer");
        String v_isanswer   = "";

        int v_checked_selnum = 0;

        String v_luserid    = box.getSession("userid");

		int    v_selcount     = box.getInt("p_selcount1");
        
        
        String v_selsaveimage       = "";
        String v_selrealimage       = "";
        String v_isselimage         = "";
        String v_selimagedel        = "";
        String v_orgselsaveimage    = "";
        String v_orgselrealimage    = "";
        
		String v_saveimage       = box.getNewFileName ("p_img1");
		String v_realimage   = box.getRealFileName("p_img1");
        String v_saveaudio     = box.getNewFileName ("p_audio1");
		String v_realaudio   = box.getRealFileName("p_audio1");
        String v_savemovie  = box.getNewFileName("p_movie1");
		String v_realmovie   = box.getRealFileName("p_movie1");
		String v_saveflash      = box.getNewFileName ("p_flash1");
		String v_realflash   = box.getRealFileName("p_flash1");

        try { 
            v_examnum = getExamnumSeq(v_subj);

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            isOk = insertTZ_exam(connMgr,   v_subj,     v_examnum,  v_lesson,   v_examtype,    v_examtext,     v_exptext,    v_levels,  v_selcount,   v_saveimage,   v_saveaudio, v_savemovie,   v_saveflash,   v_realimage,   v_realaudio, v_realmovie,   v_realflash, v_luserid, v_isuse );
            
            sql =  "insert into TZ_EXAMSEL(subj, examnum, selnum, seltext, isanswer, luserid, ldate, saveselimage, realselimage, isselimage) ";
            sql +=  " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = connMgr.prepareStatement(sql);

            for ( int i=1; i<=10; i++ ) { 
                v_seltext           = box.getString("p_seltext" + String.valueOf(i));
                v_selsaveimage      = box.getNewFileName("p_selimage"  + String.valueOf(i));
                v_selrealimage      = box.getRealFileName("p_selimage" + String.valueOf(i));
                v_isselimage    = !v_selrealimage.equals("") ? "Y" : "N";
                
                if ( !v_seltext.trim().equals("") ) { 
                    v_selnum++;
                    v_isanswer = box.getString("p_isanswer" +String.valueOf(i));
                    isOk = insertTZ_examsel(pstmt, v_subj,  v_examnum, v_selnum, v_seltext, v_isanswer, v_luserid, v_selsaveimage, v_selrealimage, v_isselimage, v_selimagedel);
                }
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    풀 문제 등록   
    @param box          receive from the form object and session
    @return int
    */
    public int insertQuestionPool(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        StringTokenizer st = null;
        String v_tokens  = "";
		ArrayList list = new ArrayList();
		DataBox             dbox    = null;

        String  v_subj     = box.getString("p_subj");
        String v_luserid    = box.getSession("userid");
        int v_examnum    = 0;

		String s_subj = "";
		int s_examnum = 0;

        Vector  v_checks    = box.getVector("p_checks");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            v_examnum = getExamnumSeq(v_subj);

            for ( int i = 0; i < v_checks.size(); i++ ) { 
               v_tokens = (String)v_checks.elementAt(i); 
			   st = new StringTokenizer(v_tokens,"|");
			       s_subj = (String)st.nextToken();
				   s_examnum = Integer.parseInt((String)st.nextToken() );
                   list = getExampleData(connMgr, s_subj, s_examnum);
                   dbox = (DataBox)list.get(0);

                   String v_lesson = "001";
				   String v_examtype = dbox.getString("d_examtype");
				   String v_examtext = dbox.getString("d_examtext");
				   String v_exptext = dbox.getString("d_exptext");
				   String v_levels = dbox.getString("d_levels");
				   int v_selcount = dbox.getInt("d_selcount");
				   String v_saveimage = dbox.getString("d_saveimage");
				   String v_saveaudio = dbox.getString("d_saveaudio");
				   String v_savemovie = dbox.getString("d_savemovie");
				   String v_saveflash = dbox.getString("d_saveflash");
				   String v_realimage = dbox.getString("d_realimage");
				   String v_realaudio = dbox.getString("d_realaudio");
				   String v_realmovie = dbox.getString("d_realmovie");
				   String v_realflash = dbox.getString("d_realflash");

			   isOk = insertTZ_exam(connMgr,   v_subj,     v_examnum,  v_lesson,   v_examtype,    v_examtext,     v_exptext,    v_levels,  v_selcount,   v_saveimage,   v_saveaudio, v_savemovie,   v_saveflash,   v_realimage,   v_realaudio, v_realmovie,   v_realflash, v_luserid, "Y" );
    
			   sql =  "insert into TZ_EXAMSEL(subj, examnum, selnum, seltext, isanswer, luserid, ldate) ";
               sql +=  " values (?, ?, ?, ?, ?, ?, ?)";
               pstmt = connMgr.prepareStatement(sql);

               for ( int j=0; j<list.size(); j++ ) { 
                  dbox  = (DataBox)list.get(j);
				  int v_selnum = dbox.getInt("d_selnum");
				  String v_seltext = dbox.getString("d_seltext");
				  String v_isanswer = dbox.getString("d_isanswer");
                  
					  isOk = insertTZ_examsel(pstmt, v_subj,  v_examnum, v_selnum, v_seltext, v_isanswer, v_luserid);
               }
			   v_examnum++;
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    등록   
    @param box          receive from the form object and session
    @return int
    */
    public int insertTZ_exam(DBConnectionManager connMgr,
                            String  p_subj,     int     p_examnum,  String  p_lesson,   String  p_examtype,
                            String  p_examtext,     String  p_exptext,   String  p_levels,   int p_selcount, String  p_saveimage,   String  p_saveaudio, String p_savemovie, 
		                    String p_saveflash, String  p_realimage,   String  p_realaudio, String p_realmovie, String p_realflash, String p_luserid, String p_isuse ) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_EXAM table
            sql =  " insert into TZ_EXAM ";
            sql +=  " (subj,        examnum,    lesson,     examtype, ";
            sql +=  "  examtext,        exptext,    ";
            sql +=  "  levels,       selcount,  saveimage,     saveaudio,     savemovie,   saveflash,  realimage,     realaudio,     realmovie,   realflash, ";
            sql +=  "  luserid,      ldate, isuse   ) ";
            sql +=  " values ";
            sql +=  " (?,         ?,              ?,              ?, ";
            sql +=  "  ?,         ?,     ";
            sql +=  "  ?,         ?,          ?,          ?,            ?,              ?,             ?,                 ?,            ?,              ?, ";
            sql +=  "  ?,         ?, ?) ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, p_subj);
            pstmt.setInt   ( 2, p_examnum);
            pstmt.setString( 3, p_lesson);
            pstmt.setString( 4, p_examtype);
            pstmt.setString( 5, p_examtext);
            pstmt.setString( 6, p_exptext);
            pstmt.setString( 7, p_levels);
            pstmt.setInt   (8, p_selcount);
            pstmt.setString(9, p_saveimage);
            pstmt.setString(10, p_saveaudio);
            pstmt.setString(11, p_savemovie);
            pstmt.setString(12, p_saveflash);
            pstmt.setString(13, p_realimage);
            pstmt.setString(14, p_realaudio);
            pstmt.setString(15, p_realmovie);
            pstmt.setString(16, p_realflash);
            pstmt.setString(17, p_luserid);
            pstmt.setString(18, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(19, p_isuse    );

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
    문제 보기 등록   
    @param box          receive from the form object and session
    @return int
    */
    public int insertTZ_examsel(PreparedStatement pstmt, String p_subj,  int p_examnum, int p_selnum, String p_seltext, String p_isanswer, String p_luserid, String p_selsaveimage, String p_selrealimage, String p_isselimage, String p_selsaveimagedel) throws Exception {
        int isOk = 0;

        try { 
            pstmt.setString(1, p_subj);
            pstmt.setInt   (2, p_examnum);
            pstmt.setInt   (3, p_selnum);
            pstmt.setString(4, p_seltext);
            pstmt.setString(5, p_isanswer);
            pstmt.setString(6, p_luserid);
            pstmt.setString(7, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(8, p_selsaveimage);
            pstmt.setString(9, p_selrealimage);
            pstmt.setString(10, p_isselimage  );
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        return isOk;
    }


    /**
    문제 보기 등록   
    @param box          receive from the form object and session
    @return int
    */
    public int insertTZ_examsel(PreparedStatement pstmt, String p_subj,  int p_examnum, int p_selnum, String p_seltext, String p_isanswer, String p_luserid) throws Exception {  
        int isOk = 0;

        try { 
            pstmt.setString(1, p_subj);
            pstmt.setInt   (2, p_examnum);
            pstmt.setInt   (3, p_selnum);
            pstmt.setString(4, p_seltext);
            pstmt.setString(5, p_isanswer);
            pstmt.setString(6, p_luserid);
            pstmt.setString(7, FormatDate.getDate("yyyyMMddHHmmss") );
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        return isOk;
    }


    /**
    일련번호 구하기 
    @param p_subj
    @return int
    */
    public int getExamnumSeq(String p_subj) throws Exception { 
        Hashtable maxdata = new Hashtable();
        maxdata.put("seqcolumn","examnum");
        maxdata.put("seqtable","tz_exam");
        maxdata.put("paramcnt","1");
        maxdata.put("param0","subj");
        maxdata.put("subj",   SQLString.Format(p_subj));

        return SelectionUtil.getSeq(maxdata);
    }


    /**
    문제 수정
    @param box          receive from the form object and session
    @return int  
    */
    public int updateQuestion(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
		    ResultSet rs = null;

		
        String  v_subj     = box.getString("p_subj");
        String  v_grcode    = box.getString("p_grcode");
		    int     v_examnum     = box.getInt("p_examnum");
        String  v_lesson   = box.getString("p_lesson");
        String  v_examtype = box.getString("p_examtype");
        String  v_examtext     = box.getString("p_examtext");
        String  v_exptext  = box.getString("p_exptext");
        String  v_levels   = box.getString("p_levels");
        String  v_isuse     = box.getStringDefault("p_isuse", "Y");

        int    v_selnum     = 0;
        Vector v_seltexts   = box.getVector("p_seltext");
        String v_seltext    = "";
        Vector v_isanswers  = box.getVector("p_isanswer");
        String v_isanswer   = "";

        int v_checked_selnum = 0;

        String v_luserid    = box.getSession("userid");

		int    v_selcount     = box.getInt("p_selcount1");

		String v_saveimage       = box.getNewFileName ("p_img1");
		String v_realimage   = box.getRealFileName("p_img1");
        String v_saveaudio     = box.getNewFileName ("p_audio1");
		String v_realaudio   = box.getRealFileName("p_audio1");
        String v_savemovie  = box.getNewFileName("p_movie1");
		String v_realmovie   = box.getRealFileName("p_movie1");
		String v_saveflash      = box.getNewFileName ("p_flash1");
		String v_realflash   = box.getRealFileName("p_flash1");

        String v_imgfile      = box.getString("p_img2");
        String v_audiofile      = box.getString("p_audio2");
        String v_moviefile      = box.getString("p_movie2");
        String v_flashfile      = box.getString("p_flash2");
        String v_realimgfile  = box.getString("p_img3");
        String v_realaudiofile  = box.getString("p_audio3");
        String v_realmoviefile  = box.getString("p_movie3");
        String v_realflashfile  = box.getString("p_flash3");
        int sulcnt  = 10;  // 2005.8.20 by 정은년
        
        String v_selsaveimage       = "";
        String v_selrealimage       = "";
        String v_isselimage         = "";
        String v_selimagedel        = "";
        String v_orgselsaveimage    = "";
        String v_orgselrealimage    = "";

        String v_selSaveimage    = "";
        
        if ( v_saveimage.length()      == 0 ) { v_saveimage       = v_imgfile;     }
        if ( v_saveaudio.length()    == 0 ) { v_saveaudio     = v_audiofile;     }
        if ( v_savemovie.length()     == 0 ) { v_savemovie      = v_moviefile;     }
        if ( v_saveflash.length()     == 0 ) { v_saveflash      = v_flashfile;     }
        if ( v_realimage.length()     == 0 ) { v_realimage      = v_realimgfile;     }
        if ( v_realaudio.length()  == 0 ) { v_realaudio   = v_realaudiofile; }
        if ( v_realmovie.length() == 0 ) { v_realmovie = v_realmoviefile; }
        if ( v_realflash.length() == 0 ) { v_realflash  = v_realflashfile; }

        
        if(box.getString("p_realimage").equals("Y")){
        	v_selSaveimage = v_saveimage;
        	v_saveimage = "";
        	v_realimage = "";
        }

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // 수정여부 체크
            sql = " select count(*) cnt from tz_examresult where subj='" +v_subj + "' and ','||exam||',' like '%," +v_examnum + ",%' ";
            pstmt = connMgr.prepareStatement(sql);
            rs = pstmt.executeQuery();
		    		if ( rs.next() ) { 
		    				if ( rs.getInt("cnt") > 0) isOk = -2;
		    		}
		    		            
//						if ( isOk == 0) { 
	            isOk = updateTZ_exam(connMgr,   v_subj,     v_examnum,  v_lesson,   v_examtype,    v_examtext,     v_exptext,    v_levels,  v_selcount,   v_saveimage,   v_saveaudio, v_savemovie,   v_saveflash,   v_realimage,   v_realaudio, v_realmovie,   v_realflash, v_luserid, v_isuse );
	
	            if(!v_selSaveimage.equals("")){
	            	FileManager.deleteFile(v_selSaveimage); 
	            }
	            
	            isOk = deleteTZ_examsel(connMgr, v_subj, v_examnum);
	
	            sql =  "insert into TZ_EXAMSEL(subj, examnum, selnum, seltext, isanswer, luserid, ldate, saveselimage, realselimage, isselimage) ";
	            sql +=  " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            pstmt = connMgr.prepareStatement(sql);
	
	            if ( v_examtype.equals("3")) sulcnt = 2; // OX식 
	
	            for ( int i=1; i<=sulcnt; i++ ) { 
	            	v_isselimage         = "Y";
                    v_seltext           = box.getString("p_seltext" + String.valueOf(i));
                    v_selsaveimage      = box.getNewFileName("p_selimage"  + String.valueOf(i));
                    v_selrealimage      = box.getRealFileName("p_selimage" + String.valueOf(i));
                    v_selimagedel       = box.getString("p_selimagedel"    + String.valueOf(i));
                    v_orgselsaveimage   = box.getString("p_selsaveimage"   + String.valueOf(i));
                    v_orgselrealimage   = box.getString("p_selrealimage"   + String.valueOf(i));
                    //v_isselimage    = !v_selrealimage.equals("") ? "Y" : "N";
                    if(v_selrealimage.equals("") && v_orgselrealimage.equals("")){
                    	v_isselimage = "N";
                    }
                    
                    if ( !v_selimagedel.equals("") ) {
                        FileManager.deleteFile(v_orgselsaveimage);         //   DB 에서 모든처리가 완료되면 해당 첨부파일 삭제
//                        FileManager.deleteFile(config.getProperty("dir.upload.exam") + v_orgselsaveimage);         //   DB 에서 모든처리가 완료되면 해당 첨부파일 삭제
                        v_selrealimage  = "";
                        v_selsaveimage  = "";
                        v_isselimage    = "N";
                    }
                    
                    if ( v_selsaveimage.equals("") && !v_selimagedel.equals("Y") ) {
                        v_selsaveimage = v_orgselsaveimage;
                        v_selrealimage = v_orgselrealimage;
                    }
                    
	                if ( !v_seltext.trim().equals("") ) { 
	                    v_selnum++;
	                    v_isanswer = box.getString("p_isanswer" +String.valueOf(i));
	                    isOk = insertTZ_examsel(pstmt, v_subj,  v_examnum, v_selnum, v_seltext, v_isanswer, v_luserid, v_selsaveimage, v_selrealimage, v_isselimage, v_selimagedel);
	                
	                }
	            }
	
	/*
	            for ( int i = 0; i<v_seltexts.size(); i++ ) { 
	                v_seltext  = (String)v_seltexts.get(i);
	
	                if ( !v_seltext.trim().equals("") ) { 
	                    v_selnum++;
	                    v_isanswer = "N";
	                    for ( int j=0; j<v_isanswers.size(); j++ ) { 
	                        v_checked_selnum  = Integer.valueOf((String)v_isanswers.get(j)).intValue();
	                        if ( i +1 == v_checked_selnum) { 
	                            v_isanswer = "Y";
	                        }
	                    }
	
	                    isOk = InsertTZ_examsel(pstmt, v_subj, v_examnum, v_selnum, v_seltext, v_isanswer, v_luserid);
	                }
	            }
	*/
	
//				    }
				    
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }                
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }                    
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
     }


    /**
    문제 삭제 
    @param box
    @return int
    */
     public int deleteQuestion(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          int isOk = 0;

          String v_subj       = box.getString("p_subj");
          int    v_examnum     = box.getInt("p_examnum");
          String v_duserid    = box.getSession("userid");

          try { 
              connMgr = new DBConnectionManager();
              connMgr.setAutoCommit(false);

              isOk = deleteTZ_exam(connMgr, v_subj, v_examnum, v_duserid);
              if ( isOk > 0) { 
                isOk = deleteTZ_examsel(connMgr, v_subj, v_examnum);
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
              if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }
          return isOk;
      }


    /**
    문제 수정  
    @param box
    @return int
    */
    public int updateTZ_exam(DBConnectionManager connMgr,
                            String  p_subj,     int     p_examnum,  String  p_lesson,   String  p_examtype,
                            String  p_examtext,     String  p_exptext,   String  p_levels,   int p_selcount, String  p_saveimage,   String  p_saveaudio, String p_savemovie, 
		                    String p_saveflash, String  p_realimage,   String  p_realaudio, String p_realmovie, String p_realflash, String p_luserid, String p_isuse ) throws Exception { 


        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // update TZ_EXAM table

            sql =  " update TZ_EXAM ";
            sql +=  "    set lesson   = ?, ";
            sql +=  "        examtype = ?, ";
            sql +=  "        examtext     = ?, ";
            sql +=  "        exptext  = ?, ";
            sql +=  "        levels   = ?, ";
            sql +=  "        selcount   = ?, ";
            sql +=  "        saveimage   = ?, ";
            sql +=  "        saveaudio = ?, ";
            sql +=  "        savemovie = ?, ";
            sql +=  "        saveflash      = ?, ";
            sql +=  "        realimage   = ?, ";
            sql +=  "        realaudio = ?, ";
            sql +=  "        realmovie  = ?, ";
            sql +=  "        realflash  = ?, ";
            sql +=  "        luserid  = ?, ";
            sql +=  "        ldate    = ?,  ";
            sql +=  "        isuse    = ?  ";
            sql +=  "  where subj     = ?  ";
            sql +=  "    and examnum  = ?  ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, p_lesson);
            pstmt.setString( 2, p_examtype);
            pstmt.setString( 3, p_examtext);
            pstmt.setString( 4, p_exptext);
            pstmt.setString( 5, p_levels);
            pstmt.setInt   (6, p_selcount);
            pstmt.setString(7, p_saveimage);
            pstmt.setString(8, p_saveaudio);
            pstmt.setString(9, p_savemovie);
            pstmt.setString(10, p_saveflash);
            pstmt.setString(11, p_realimage);
            pstmt.setString(12, p_realaudio);
            pstmt.setString(13, p_realmovie);
            pstmt.setString(14, p_realflash);
            pstmt.setString(15, p_luserid);
            pstmt.setString(16, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString( 17, p_isuse);
            pstmt.setString( 18, p_subj);
            pstmt.setInt( 19, p_examnum);
            
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
    문제 삭제 
    @param box
    @return int
    */
    public int deleteTZ_exam(DBConnectionManager connMgr, String p_subj, int p_examnum, String p_duserid) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
	    	ResultSet rs = null;

        try { 
            // 삭제체크
            // sql = " select exam  from tz_examresult where subj='" +p_subj + "' ";
            // sql += " and ((exam like '%" +SQLString.Format(p_examnum) + ",%') or (exam  like '%," +SQLString.Format(p_examnum) + ",%') or (exam = '" +SQLString.Format(p_examnum) + "') or (exam like '%," +SQLString.Format(p_examnum) + "%')) ";
            
            sql = " select count(*) cnt from tz_examresult where subj='" +p_subj + "' and ','||exam||',' like '%," +p_examnum + ",%' ";
            pstmt = connMgr.prepareStatement(sql);
            rs = pstmt.executeQuery();
		    		if ( rs.next() ) { 
		    				if ( rs.getInt("cnt") > 0) isOk = -2;
		    		}	
		    		
   		
                        
            if ( isOk == 0) { 
                
                sql =  " delete from TZ_EXAM ";
                sql +=  "  where subj     = ?  ";
                sql +=  "    and examnum  = ?  ";
    
                pstmt = connMgr.prepareStatement(sql);
                
    						pstmt.setString(1, p_subj);
                pstmt.setInt   (2, p_examnum);
    
                isOk = pstmt.executeUpdate();
            }
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
           if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }           
       }
       return isOk;
    }


    /**
    문제 보기 삭제 
    @param box
    @return int
    */
    public int deleteTZ_examsel(DBConnectionManager connMgr, String p_subj, int p_examnum) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // delete TZ_EXAMSEL table
            sql =  " delete from TZ_EXAMSEL ";
            sql +=  "  where subj     = ?  ";
            sql +=  "    and examnum   = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_subj);
            pstmt.setInt   (2, p_examnum);

            isOk = pstmt.executeUpdate();
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception(ex.getMessage() );
       }
       finally { 
           if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
       }
//     return isOk; // 주관식,논술식일경우 없을수 있으므로
       return 1;
    }



    /**
	과목 컨텐츠타입 구하기
    @param box          receive from the form object and session
    @return string
    */
    public String getContentType(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String              sql     = "";
		String p_subj = box.getString("p_subj");
        String result = "";
        try { 
			connMgr = new DBConnectionManager();
            sql =  " select contenttype from tz_subj where subj=" +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
				dbox = ls.getDataBox();
                result = dbox.getString("d_contenttype");
            }
       }
       catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }
       return result;
    }
    

    /**
     * 선택한 문제 미리보기
     * @param connMgr
     * @param p_subj
     * @param p_examnum
     * @return
     * @throws Exception
     */
    public ArrayList selectExamQuestion(RequestBox box) throws Exception { 
        ArrayList list = null;
        ArrayList eqlist = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox = null;
        DBConnectionManager	connMgr	= null;

		String v_subj = box.getString("p_subj");
		String v_chknum = box.getString("p_chknum");
		String[] v_examnums = {};
		if (v_chknum.length() > 0) {
			v_examnums = v_chknum.split(",");
		}

        try { 
        	connMgr = new DBConnectionManager();
        	eqlist = new ArrayList();
        	
            for ( int i = 0; i<v_examnums.length; i++ ) { 

                sql = "\n select a.subj, a.examnum, a.lesson, a.examtype "
                	+ "\n      , a.examtext, a.exptext, a.levels, a.selcount, a.saveimage, a.saveaudio "
                	+ "\n      , a.savemovie, a.saveflash, a.realimage, a.realaudio, a.realmovie, a.realflash "
                	+ "\n      , b.selnum, b.seltext, nvl(b.isanswer,'N') as isanswer "
                	+ "\n      , b.saveselimage, b.realselimage, b.isselimage "
                	+ "\n      , c.codenm as examtypenm "
                	+ "\n      , d.codenm as levelsnm, a.isuse "
                	+ "\n from   tz_exam      a "
                	+ "\n      , tz_examsel   b "
                	+ "\n      , tz_code      c "
                	+ "\n      , tz_code      d "
                	+ "\n where  a.subj     = b.subj(+) "
                	+ "\n and    a.examnum  = b.examnum(+) "
                	+ "\n and    a.examtype = c.code "
                	+ "\n and    a.levels   = d.code "
                	+ "\n and    a.subj     = " + SQLString.Format(v_subj)
                	+ "\n and    a.examnum  = " + SQLString.Format(v_examnums[i])
                	+ "\n and    c.gubun    = " + SQLString.Format(ExamBean.EXAM_TYPE)
                	+ "\n and    d.gubun    = " + SQLString.Format(ExamBean.EXAM_LEVEL)
                	+ "\n order  by a.examnum, b.selnum ";

                ls = connMgr.executeQuery(sql);
				list =  new ArrayList();

                while ( ls.next() ) { 
                    dbox = ls.getDataBox();
                    list.add(dbox);
                }
                eqlist.add(list);
			    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return eqlist;
    }
    

    /**
     * 선택해서 삭제하기
     * @param box
     * @return 삭제안되는 문제번호
     * @throws Exception
     */
    public String checkDeleteQuestion(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String result = "";
        int isOk = 0;
        int isOk2 = 0;

        String v_subj   = box.getString("p_subj");
        Vector v_checks = box.getVector("p_checks");
        String v_examnum = "";

        try { 
    		connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            for ( int i = 0; i < v_checks.size(); i++ ) { 
            	v_examnum = (String)v_checks.elementAt(i); 
    		
            	isOk = deleteTZ_exam(connMgr, v_subj, Integer.parseInt(v_examnum), "");
            	
	    		if ( isOk > 0) { 
	    			isOk2 = deleteTZ_examsel(connMgr, v_subj, Integer.parseInt(v_examnum));
	    		} else {
	    			result += "[" + v_examnum + "], ";
	    		}
            }
            
            if (result.length() > 0) {
            	result = StringManager.substring(result,0,result.length()-2) + "번호의 문제는 이미 사용중이므로 삭제가 불가능합니다.";
            }
        }
        catch ( Exception ex ) { 
        	isOk = 0;
        	connMgr.rollback();
        	ErrorManager.getErrorStackTrace(ex);
        	throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
        	if ( connMgr != null ) { try {  connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
	}    
}
