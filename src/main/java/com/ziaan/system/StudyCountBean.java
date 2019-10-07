// **********************************************************
//  1. ��      ��: �н�â �������
//  2. ���α׷���: StudyCountBean.java
//  3. ��      ��: �������
//  4. ȯ      ��: JDK 1.5
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2009. 9. 28
//  7. ��      ��:
/*
  
MENU MENUNM			PGM
01	�н�����			/servlet/controller.study.SubjGongServlet?p_process=selectList
03	�н���Ȳ			/servlet/controller.lcms.EduStart?p_process=eduList
04	�н�Q&A			/servlet/controller.study.SubjQnaStudyServlet?p_process=SubjQnaFrame
05	�����Խ���		/servlet/controller.study.StudyBoardServlet
06	������н�		/servlet/controller.study.ToronServlet
07	�ڷ��			/servlet/controller.study.StudyDataServlet
08	�н�������		/servlet/controller.study.StudentListServlet?p_process=select
09	����Ʈ			/servlet/controller.study.ReportServlet?p_process=choicePage
10	�н���			/servlet/controller.study.StudyExamServlet
12	����				/servlet/controller.study.StudySulmunServlet
14	������			/servlet/controller.study.DicSubjStudyServlet?p_process=selectListPre
16     �н�����			/servlet/controller.EduProgress?p_process=eduExit
17	�������������	/servlet/controller.study.ContentsErrorDataServlet

// ���� �޴� ���� ���� �߰�
box.put("p_menu","01");
StudyCountBean scBean = new StudyCountBean();
scBean.writeLog(box);
*/
// **********************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;


public class StudyCountBean { 

    public StudyCountBean() { }

    /**
     * �α� �ۼ�
     * @param box       receive from the form object and session
     * @return is_Ok    1 : log ok      2 : log fail
     * @throws Exception
     */
    public int writeLog(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 	= "";
        String sql2 	= "";
        String sql3 	= "";
        int cnt  		= 0;
        int is_Ok 		= 0;

        String v_grcode     = box.getStringDefault(box.getSession("tem_grcode"),"N000001");
        String v_subj       = box.getStringDefault("p_subj", box.getSession("s_subj") );
        String v_year       = box.getStringDefault("p_year", box.getSession("s_year") );
        String v_subjseq    = box.getStringDefault("p_subjseq", box.getSession("s_subjseq") );
        String v_menu       = box.getString("p_menu");
        String s_userid     = box.getSession("userid");

        try { 
/*
System.out.println("v_grcode : '" + v_grcode + "'");
System.out.println("v_subj : '" + v_subj + "'");
System.out.println("v_year : '" + v_year + "'");
System.out.println("v_subjseq : '" + v_subjseq + "'");
System.out.println("v_menu : '" + v_menu + "'");
*/
            if ( !v_grcode.equals("") && !v_subj.equals("") && !v_year.equals("") && !v_subjseq.equals("") && !v_menu.equals("") ) { 
                // �޴��� ���������� ��ϵ��� �ʾ��� ���
                    connMgr = new DBConnectionManager();

                    sql1  = " select count(*) cnt ";
                    sql1 += " from TZ_STUDYCOUNT   ";
                    sql1 += " where grcode = " + StringManager.makeSQL(v_grcode);
                    sql1 += "   and subj  = " + StringManager.makeSQL(v_subj);
                    sql1 += "   and year  = " + StringManager.makeSQL(v_year);
                    sql1 += "   and subjseq  = " + StringManager.makeSQL(v_subjseq);
                    sql1 += "   and menu = " + StringManager.makeSQL(v_menu);
                    sql1 += "   and userid = " + StringManager.makeSQL(s_userid);

                    ls = connMgr.executeQuery(sql1);

                    if ( ls.next() ) { 
                        cnt = ls.getInt("cnt");
                    }

                    if ( cnt > 0 ) {                         // update
                        sql2  = " update TZ_STUDYCOUNT set cnt = cnt + 1                                 ";
                        sql2 += " where grcode    = ?  and subj = ?  and year = ? and subjseq = ? and menu = ? and userid = ? ";
                        pstmt = connMgr.prepareStatement(sql2);

                        pstmt.setString(1,  v_grcode);
                        pstmt.setString(2,  v_subj);
                        pstmt.setString(3,  v_year);
                        pstmt.setString(4,  v_subjseq);
                        pstmt.setString(5,  v_menu);
                        pstmt.setString(6,  s_userid);

                    } else {                                // insert
                        sql3  = " insert into TZ_STUDYCOUNT(grcode, subj, year, subjseq, menu, userid, cnt) ";
                        sql3 += " values (?, ?, ?, ?, ?, ?, ?       )                                       ";
                        pstmt = connMgr.prepareStatement(sql3);

                        pstmt.setString(1,  v_grcode);
                        pstmt.setString(2,  v_subj);
                        pstmt.setString(3,  v_year);
                        pstmt.setString(4,  v_subjseq);
                        pstmt.setString(5,  v_menu);
                        pstmt.setString(6,  s_userid);
                        pstmt.setInt(7,  1);
                    }
                    is_Ok = pstmt.executeUpdate();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }



    /**
    * My Activity
    * @param  box          receive from the form object and session
    * @return result       My Activity
    * @throws Exception
    */
    public ArrayList SelectActivityList(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        ArrayList 	list1   = null;
        ListSet  	ls      = null;
        DataBox     dbox    = null;
        String 		sql    	= "";

        String v_grcode     = box.getStringDefault(box.getSession("tem_grcode"),"N000001");
        String v_subj = box.getString("subj");
        String v_year = box.getString("year");
        String v_subjseq = box.getString("subjseq");
        String s_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql = "select b.menu, b.menunm, nvl(a.cnt,0) cnt from ";
            sql += "    (select grcode, subj, year, subjseq, menu, sum(cnt) cnt from TZ_STUDYCOUNT";
            sql += "        where grcode    =" + StringManager.makeSQL(v_grcode);
            sql += "        and subj        =" + StringManager.makeSQL(v_subj);
            sql += "        and year        =" + StringManager.makeSQL(v_year);
            sql += "        and subjseq     =" + StringManager.makeSQL(v_subjseq);
            sql += "        and userid      =" + StringManager.makeSQL(s_userid);
            sql += "        group by grcode, subj, year, subjseq, menu, userid ) a, TZ_MFSUBJ b ";
            sql += " where a.subj( +)=b.subj and a.menu( +)=b.menu and b.subj=" +StringManager.makeSQL(v_subj) + " and b.pgmtype='servlet'";
            sql += " order by b.orders";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }


    /*�н��ð� ��ȸ ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectLearningTime(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list1     = null;
        DataBox dbox    	= null;
        String  sql        	= "";

        String  v_subj      = box.getString("subj");        // ����
        String  v_subjseq   = box.getString("subjseq"); // ���� ���
        String  v_year      = box.getString("year");    // �⵵
        String  s_userid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql = "select min(A.first_edu) first_edu,                                  ";
            sql += "  max(A.first_end) first_end,                                       ";
            sql += "  sum(lesson_count) lesson_count,                                   ";
            sql += " (                                                                  ";
            sql += "     select tstep from TZ_STUDENT                                   ";
            sql += "     where subj      =" + StringManager.makeSQL(v_subj);
            sql += "         and year        =" + StringManager.makeSQL(v_year);
            sql += "         and subjseq     =" + StringManager.makeSQL(v_subjseq);
            sql += "         and userid      =" + StringManager.makeSQL(s_userid);
            sql += " ) tstep,                                                           ";
            // sql += "  trunc( ( sum(to_number(substr(A.total_time,1,2))*60*60            ";
            // sql += "      + to_number(substr(A.total_time,4,2))*60                      ";
            // sql += "      + to_number(substr(A.total_time,7,2))                         ";
            // sql += "      ) / (60*60) ),0) total_time,                                  ";
            // sql += "  trunc( sum(to_number(substr(A.total_time,1,2))*60*60             ";
            // sql += "      + to_number(substr(A.total_time,4,2))*60                      ";
            // sql += "      + to_number(substr(A.total_time,7,2))                         ";
            // sql += "      ) / 60) total_minute,                                         ";
            // sql += "  mod ( sum(to_number(substr(A.total_time,1,2))*60*60               ";
            // sql += "      + to_number(substr(A.total_time,4,2))*60                      ";
            // sql += "      + to_number(substr(A.total_time,7,2))                         ";
            // sql += "      ) , 60) total_sec                                             ";
            sql +="   trunc( ( sum(to_number(substr(A.total_time,1,2))) *60*60 + sum(to_number(substr(A.total_time,4,2)))*60 + sum(to_number(substr(A.total_time,7,2))) ) / (60*60) ,0) total_time,      ";
            sql +="   trunc( mod( (sum(to_number(substr(A.total_time,1,2))*60*60) + sum( to_number(substr(A.total_time,4,2))*60) + sum(to_number(substr(A.total_time,7,2))) )/60, 60), 0 ) total_minute, ";
            sql +="   mod ( sum(to_number(substr(A.total_time,1,2))*60*60 + to_number(substr(A.total_time,4,2))*60 + to_number(substr(A.total_time,7,2))) , 60) total_sec                                ";
            sql += " from TZ_PROGRESS A                                                 ";
            sql += " where subj      =" + StringManager.makeSQL(v_subj);
            sql += "     and year        =" + StringManager.makeSQL(v_year);
            sql += "     and subjseq     =" + StringManager.makeSQL(v_subjseq);
            sql += "     and userid      =" + StringManager.makeSQL(s_userid);
            sql += " group by subj, year, subjseq, userid";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /*�ֱ� �н���
    @param box      receive from the form object and session
    @return ArrayList
    */
     public String selectStudyDate(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String  sql        	= "";
        String result 		= "";

        String  v_subj      = box.getString("subj");        // ����
        String  v_subjseq   = box.getString("subjseq"); // ���� ���
        String  v_year      = box.getString("year");    // �⵵
        String  s_userid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            sql += " select ldate from TZ_SUBJLOGINID                ";
            sql += "  where subj      =" + StringManager.makeSQL(v_subj);
            sql += "    and year        =" + StringManager.makeSQL(v_year);
            sql += "    and subjseq     =" + StringManager.makeSQL(v_subjseq);
            sql += "    and userid      =" + StringManager.makeSQL(s_userid);
            sql += "  order by ldate desc    ";
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getString("ldate");
                if ( result.length() >=12) { 
                    result = FormatDate.getFormatDate(result,"yyyy.MM.dd HH:mm");
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

    /*���ǽ� ����Ƚ��
    @param box      receive from the form object and session
    @return ArrayList
    */
     public String selectStudyCount(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String  sql        	= "";
        String result 		= "";

        String  v_subj      = box.getString("subj");        // ����
        String  v_subjseq   = box.getString("subjseq"); // ���� ���
        String  v_year      = box.getString("year");    // �⵵
        String  s_userid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            sql += " select count(*) cnt from TZ_SUBJLOGINID                ";
            sql += " where subj      	=" + StringManager.makeSQL(v_subj);
            sql += "     and year       =" + StringManager.makeSQL(v_year);
            sql += "     and subjseq    =" + StringManager.makeSQL(v_subjseq);
            sql += "     and userid     =" + StringManager.makeSQL(s_userid);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = String.valueOf( ls.getInt("cnt") );
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

    /*������
    @param box      receive from the form object and session
    @return ArrayList
    */
     public String selectStudyStep(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String  sql        	= "";
        String result 		= "";

        String  v_subj      = box.getString("subj");        // ����
        String  v_subjseq   = box.getString("subjseq"); // ���� ���
        String  v_year      = box.getString("year");    // �⵵
        String  s_userid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            
            sql  = "    SELECT ROUND ((a.completed_educheck_cnt / b.total_cnt) * 100, 2) AS tstep ";
            sql += "    FROM (SELECT COUNT (*) completed_educheck_cnt  ";
            sql += "            FROM tz_progress   ";
            sql += "           WHERE subj = " + StringManager.makeSQL(v_subj);
            sql += "             AND YEAR = " + StringManager.makeSQL(v_year);
            sql += "             AND subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "             AND userid = " + StringManager.makeSQL(s_userid);
            sql += "             AND first_end is not null ";
            sql += "          ) a,   ";
            sql += "         (SELECT COUNT (*) total_cnt ";  
            sql += "            FROM TZ_SUBJLESSON  ";
            sql += "           WHERE subj = "+StringManager.makeSQL(v_subj)+" and lesson != '00' and lesson != '99') b ";

//            sql += " select tstep from TZ_STUDENT                                   ";
//            sql += "  where subj      =" + StringManager.makeSQL(v_subj);
//            sql += "    and year      =" + StringManager.makeSQL(v_year);
//            sql += "    and subjseq   =" + StringManager.makeSQL(v_subjseq);
//            sql += "    and userid    =" + StringManager.makeSQL(s_userid);
            ls = connMgr.executeQuery(sql);
//System.out.println("--����-"+sql);
            if ( ls.next() ) { 
                result = String.valueOf( ls.getFloat("tstep") );
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /*�Խ��� ��ϼ� ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectBoardCnt(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list1     = null;
        DataBox     dbox    = null;
        String  sql        	= "";

        String  v_subj      = box.getString("subj");        // ����
        String  v_subjseq   = box.getString("subjseq"); // ���� ���
        String  v_year      = box.getString("year");    // �⵵
        String  s_userid    = box.getSession("userid");
        String v_grcode 	= box.getSession("tem_grcode");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql = "select                                                       ";
            sql += "  ( select count(b.seq) from TZ_BDS a, TZ_BOARD b            ";
            sql += "     where a.tabseq = b.tabseq and a.type = 'SB'";
            sql += "         and a.grcode    =" + StringManager.makeSQL(v_grcode);
            sql += "         and a.subj      =" + StringManager.makeSQL(v_subj);
            sql += "         and a.year      =" + StringManager.makeSQL(v_year);
            sql += "         and a.subjseq   =" + StringManager.makeSQL(v_subjseq);
            sql += "         and b.userid    =" + StringManager.makeSQL(s_userid);
            sql += "  ) boardcnt,                                                ";
            sql += "  ( select count(b.seq) from TZ_BDS a, TZ_BOARD b            ";
            sql += "     where a.tabseq = b.tabseq and a.type = 'SD'";
            sql += "         and a.grcode    =" + StringManager.makeSQL(v_grcode);
            sql += "         and a.subj      =" + StringManager.makeSQL(v_subj);
            sql += "         and a.year      =" + StringManager.makeSQL(v_year);
            sql += "         and a.subjseq   =" + StringManager.makeSQL(v_subjseq);
            sql += "         and b.userid    =" + StringManager.makeSQL(s_userid);
            sql += "  ) datacnt,                                                 ";
            sql += "  ( select count(a.seq) from TZ_QNA a                        ";
            sql += "     where kind = '0'                                        ";
            sql += "         and a.subj      =" + StringManager.makeSQL(v_subj);
            sql += "         and a.year      =" + StringManager.makeSQL(v_year);
            sql += "         and a.subjseq   =" + StringManager.makeSQL(v_subjseq);
            sql += "         and a.inuserid  =" + StringManager.makeSQL(s_userid);
            sql += "  ) qnacnt,                                                  ";
            sql += "  ( select count(a.userid) from TZ_EXAMRESULT a          ";
            sql += "     where a.subj        =" + StringManager.makeSQL(v_subj);
            sql += "         and a.year      =" + StringManager.makeSQL(v_year);
            sql += "         and a.subjseq   =" + StringManager.makeSQL(v_subjseq);
            sql += "         and a.userid    =" + StringManager.makeSQL(s_userid);
            sql += "  ) examcnt,                                                 ";
            sql += "  ( select count(a.userid) from TZ_SULEACH a             ";
            sql += "     where a.subj        =" + StringManager.makeSQL(v_subj);
            sql += "         and a.grcode    =" + StringManager.makeSQL(v_grcode);
            sql += "         and a.year      =" + StringManager.makeSQL(v_year);
            sql += "         and a.subjseq   =" + StringManager.makeSQL(v_subjseq);
            sql += "         and a.userid    =" + StringManager.makeSQL(s_userid);
            sql += "  ) sulcnt,                                                  ";
            sql += "  ( select max(a.ldate) from TZ_PROJREP a                   ";
            sql += "     where a.subj        =" + StringManager.makeSQL(v_subj);
            sql += "         and a.year      =" + StringManager.makeSQL(v_year);
            sql += "         and a.subjseq   =" + StringManager.makeSQL(v_subjseq);
            sql += "         and a.projid    =" + StringManager.makeSQL(s_userid);
            sql += "  ) reportindate";
            sql += " from dual ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
     
     /*�⼮�Ⱓ
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList selectattend(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet ls          = null;
         ArrayList list5     = null;
         DataBox dbox    	= null;
         String  sql        	= "";

         String  v_subj      = box.getString("subj");        // ����
         String  v_subjseq   = box.getString("subjseq"); // ���� ���
         String  v_year      = box.getString("year");    // �⵵
         String  s_userid    = box.getSession("userid");
         try { 
             connMgr = new DBConnectionManager();
             list5 = new ArrayList();

             sql += " select substr(edustart,0,8) edustart, substr(eduend,0,8) eduend ,	\n"+
             		" (SELECT COUNT (*) FROM tz_attendance a \n"+
             		" 	WHERE a.subj = vz.subj	\n"+
             		"	AND a.YEAR = vz.YEAR	\n"+
             		"	 and a.subjseq = vz.subjseq	\n"+
             		"	AND attdate BETWEEN substr(edustart,0,8) AND substr(eduend,0,8)	\n"+
             		"	AND userid = '"+s_userid+"') AS attcnt" +
             		" from vz_scsubjseq vz		 \n"+
					" where subj=" + StringManager.makeSQL(v_subj)						+
					" and subjseq=" + StringManager.makeSQL(v_subjseq)					+
					" and year=" + StringManager.makeSQL(v_year);
//System.out.println("----sql--------------"+sql);
             ls = connMgr.executeQuery(sql);

             while ( ls.next() ) { 
                 dbox = ls.getDataBox();
                 list5.add(dbox);
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list5;
     }
      /*�⼮��
      @param box      receive from the form object and session
      @return ArrayList
      */
       public ArrayList selectattend2(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          ListSet ls          = null;
          ArrayList list6     = null;
          DataBox dbox    	= null;
          String  sql        	= "";
          String  sql1        	= "";

          String  v_subj      = box.getString("subj");        // ����
          String  v_subjseq   = box.getString("subjseq"); // ���� ���
          String  v_year      = box.getString("year");    // �⵵
          String  s_userid    = box.getSession("userid");
          String  v_edustart  = "";
          String  v_eduend  = "";

          try { 
              connMgr = new DBConnectionManager();
              list6 = new ArrayList();
              sql += " select substr(edustart,0,8) edustart, substr(eduend,0,8) eduend from vz_scsubjseq 	\n"+
				" where subj=" + StringManager.makeSQL(v_subj)						+
				" and subjseq=" + StringManager.makeSQL(v_subjseq)					+
				" and year=" + StringManager.makeSQL(v_year);

              ls = connMgr.executeQuery(sql);
              
              ls.next();
               v_edustart = ls.getString(1);
               v_eduend = ls.getString(2);
              ls.close();
              

              sql1 += " select								\n"
            	  +	 " 		a.date_seq								\n"
            	  +  "		,decode(b.isattend,'','X', b.isattend) as ist \n"
            	  +  "	from											\n" 
            	  +	 "		(											\n"
            	  +	 "		select										\n" 
            	  +  "			to_char(to_date('"+v_edustart+"', 'YYYYMMDD') + level- 1,'YYYYMMDD') as DATE_SEQ \n" 
            	  +  "		from dual										\n" 
            	  +  "		connect by level<=to_date('"+v_eduend+"', 'YYYYMMDD')-to_date('"+v_edustart+"', 'YYYYMMDD')+1	\n"
            	  +  "		) a left outer join (	\n"    
            	  +  "   select attdate,isattend \n"
            	  +  "   from tz_attendance	\n"
            	  +  "   where attdate between '"+v_edustart+"' and '"+v_eduend+"' \n"
            	  +  " 	 and subj=" + StringManager.makeSQL(v_subj)						
            	  +  "   and year=" + StringManager.makeSQL(v_year)
            	  +  " 	 and subjseq=" + StringManager.makeSQL(v_subjseq)
            	  +	 "   and userid=" + StringManager.makeSQL(s_userid)
            	  +  ") b \n"
            	  +  "on (a.date_seq = b.attdate)\n";
 	  
            	 
              ls = connMgr.executeQuery(sql1);

              while ( ls.next() ) { 
                  dbox = ls.getDataBox();
                  list6.add(dbox);
              }

          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex, box, sql);
              throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
          } finally { 
              if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return list6;
      }
       
       /*�⼮��
       @param box      receive from the form object and session
       @return ArrayList
       */
        public ArrayList selectattend3(RequestBox box) throws Exception { 
           DBConnectionManager	connMgr	= null;
           ListSet ls          = null;
           ArrayList list6     = null;
           DataBox dbox    	= null;
           String  sql        	= "";
           String  sql1        	= "";

           String  v_subj      = box.getString("p_subj");        // ����
           String  v_subjseq   = box.getString("p_subjseq"); // ���� ���
           String  v_year      = box.getString("p_year");    // �⵵
           String  s_userid    = box.getSession("userid");
           String  v_edustart  = "";
           String  v_eduend  = "";

           try { 
               connMgr = new DBConnectionManager();
               list6 = new ArrayList();
               sql += " select substr(edustart,0,8) edustart, substr(eduend,0,8) eduend from vz_scsubjseq 	\n"+
 				" where subj=" + StringManager.makeSQL(v_subj)						+
 				" and subjseq=" + StringManager.makeSQL(v_subjseq)					+
 				" and year=" + StringManager.makeSQL(v_year);

               ls = connMgr.executeQuery(sql);
               
               ls.next();
                v_edustart = ls.getString(1);
                v_eduend = ls.getString(2);
               ls.close();
               

               sql1 += " select								\n"
             	  +	 " 		a.date_seq								\n"
             	  +  "		,decode(b.isattend,'','X', b.isattend) as ist \n"
             	  +  "	from											\n" 
             	  +	 "		(											\n"
             	  +	 "		select										\n" 
             	  +  "			to_char(to_date('"+v_edustart+"', 'YYYYMMDD') + level- 1,'YYYYMMDD') as DATE_SEQ \n" 
             	  +  "		from dual										\n" 
             	  +  "		connect by level<=to_date('"+v_eduend+"', 'YYYYMMDD')-to_date('"+v_edustart+"', 'YYYYMMDD')+1	\n"
             	  +  "		) a left outer join (	\n"    
             	  +  "   select attdate,isattend \n"
             	  +  "   from tz_attendance	\n"
             	  +  "   where attdate between '"+v_edustart+"' and '"+v_eduend+"' \n"
             	  +  " 	 and subj=" + StringManager.makeSQL(v_subj)						
             	  +  "   and year=" + StringManager.makeSQL(v_year)
             	  +  " 	 and subjseq=" + StringManager.makeSQL(v_subjseq)
             	  +	 "   and userid=" + StringManager.makeSQL(s_userid)
             	  +  ") b \n"
             	  +  "on (a.date_seq = b.attdate)\n";
  	  
             	 
               ls = connMgr.executeQuery(sql1);

               while ( ls.next() ) { 
                   dbox = ls.getDataBox();
                   list6.add(dbox);
               }

           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
           } finally { 
               if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }

           return list6;
       }

}