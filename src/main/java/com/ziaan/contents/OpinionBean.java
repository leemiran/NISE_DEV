// **********************************************************
//  1. 제      목: 의견달기
//  2. 프로그램명 : OpinionBean.java
//  3. 개      요: 의견달기
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7.  2
//  7. 수      정:
// **********************************************************

package com.ziaan.contents;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * 의견 달기(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */

/*
subj, year, subjseq, lesson, lessonseq, seq, userid, answer, luserid, ldate
*/

public class OpinionBean { 

    public OpinionBean() { }

    /**
    의견  작성
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int saveOpinion(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String sql1 = "";
        int isOk = 0;

        String v_subj    = box.getSession("s_subj");
        String v_year    = box.getSession("s_year");
        String v_subjseq = box.getSession("s_subjseq");
        String v_lesson  = box.getString("v_lesson");
        int v_seq        = 0;
        String v_answer  = box.getString("p_contents");
        String v_tabseq    = box.getString ("p_tabseq"   );
        
        String s_userid = box.getSession("userid");
        String s_name      = box.getSession("name");

        try { 
            connMgr = new DBConnectionManager();

            sql  = "select max(seq) from TZ_OPINION  ";
            sql += "    where  subj = " + StringManager.makeSQL(v_subj);
            sql += "    and year = " + StringManager.makeSQL(v_year);
            sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "    and lesson = " + StringManager.makeSQL(v_lesson);

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
                v_seq = ls.getInt(1) + 1;
            } else { 
                v_seq = 1;
            }

            sql1  = " INSERT INTO TZ_OPINION(SUBJ, YEAR, SUBJSEQ, LESSON, SEQ, USERID, ANSWER, QUESTIONNO, NAME, LDATE)  ";
            sql1 += "                values (? ,? ,? ,? ,? ,? ,? ,?, ? , to_char(sysdate, 'YYYYMMDDHH24MISS'))             ";

            pstmt = connMgr.prepareStatement(sql1);

            pstmt.setString(1, v_subj);
            pstmt.setString(2, v_year);
            pstmt.setString(3, v_subjseq);
            pstmt.setString(4, v_lesson);
            pstmt.setInt(5, v_seq);
            pstmt.setString(6, s_userid);
            pstmt.setString(7, v_answer);
            pstmt.setString(8, v_tabseq);
            pstmt.setString(9, s_name);

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
     
     /**
     등록된 내용 보여주기
     @param DBConnectionManager  
     @param String  과목,년도,기수,id
     @return String  
     */  
     public ArrayList selectOpinion(RequestBox box) throws Exception {               
         DBConnectionManager connMgr = null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         DataBox             dbox    = null;
         int v_pageno                = box.getInt("p_pageno");
         String sql                  = "";

         String  v_tabseq = box.getString("p_tabseq");
         String  v_subj      = box.getSession("s_subj");
         String  v_year      = box.getSession("s_year");
         String  v_subjseq   = box.getSession("s_subjseq");
         String  v_lesson  = box.getString("v_lesson");
         
         try { 
             connMgr = new DBConnectionManager();
             
             sql  = " select a.subj subj, a.year year, a.subjseq subjseq, a.lesson lesson,       ";
             sql += "        a.seq seq, a.userid userid, b.name name, a.answer as content, a.ldate ldate ";
             sql += "   from TZ_OPINION a, TZ_MEMBER b                                                                  ";
             sql += "  where a.userid = b.userid                                                                        ";
             sql += "    and a.subj = " + StringManager.makeSQL(v_subj);
             sql += "    and a.year = " + StringManager.makeSQL(v_year);
             sql += "    and a.subjseq = " + StringManager.makeSQL(v_subjseq);
             //sql += "    and a.lesson = " + StringManager.makeSQL(v_lesson);
             sql += "    and a.QUESTIONNO = " + StringManager.makeSQL(v_tabseq);
             sql += " order by ldate desc";
             
             System.out.println("talkList = "+sql);
             
             ls = connMgr.executeQuery(sql);
             
             ls.setPageSize(10);                         //  페이지당 row 갯수를  세팅한다
             ls.setCurrentPage(v_pageno);                //     현재페이지번호를 세팅한다.
             int total_page_count = ls.getTotalPage();   //     전체 페이지 수를 반환한다
             int total_row_count = ls.getTotalCount();   //     전체 row 수를 반환한다
             
             list = new ArrayList();
             
             OpinionData data = new OpinionData();

             while ( ls.next() ) { 
                 dbox = ls.getDataBox();
                 dbox.put("displaynum",total_row_count - ls.getRowNum() + 1);
                 dbox.put("total_page_count",total_page_count);
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

}
