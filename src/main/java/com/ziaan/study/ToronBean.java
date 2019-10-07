// **********************************************************
//  1. 제      목: TORON BEAN
//  2. 프로그램명: ToronBean.java
//  3. 개      요: 토론방 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 박진희 2003. 7. 22
//  7. 수      정:
// **********************************************************
package com.ziaan.study;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class ToronBean { 

    public ToronBean() { }

    /**
     * 교육후기 평가 등록 
     * @param    box          receive from the form object and session
     * @return int  
     * @throws Exception
     */
    public int insertRecom(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet rs1   = null;
        PreparedStatement pstmt1  = null;
        String sql    = "";
        String sql1   = "";
        int isOk1     = 1;
        int v_recseq  = 0;
              
        String s_userid     = box.getSession("userid");      
        
        String v_subj   = "";
        String v_year   = "";
        String v_subjseq = "";
        String v_tpcode = "";
        int v_seq = box.getInt("p_seq");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // 추천했었는지 판단한다.
            sql = "select                                               " +
                  "       userid                                        " +
                  "  from                                               " +
                  "       TZ_Toronrecom                                 " +
                  " where                                               " +
                  "       SUBJ='"     + v_subj   + "'                 " +
                  "   and YEAR='"     + v_year      + "'                 " +
                  "   and SUBJSEQ='"  + v_subjseq      + "'                 " +
                  "   and TPCODE='"   + v_tpcode      + "'                 " +
                  "   and SEQ='"      + v_seq      + "'                 " +
                  "   and userid = '" + s_userid   + "'                 ";
            rs1 = connMgr.executeQuery(sql);
            if ( rs1.next() ) { 
                
                rs1.close();
                return -10;                
            }
            rs1.close();
            
            
            //----------------------   게시판 번호 가져온다 ----------------------------
            sql = "select nvl(max(recseq), 0) from TZ_Toronrecom " +
            " where                                               " +
            "       SUBJ='"     + v_subj   + "'                 " +
            "   and YEAR='"     + v_year      + "'                 " +
            "   and SUBJSEQ='"  + v_subjseq      + "'                 " +
            "   and TPCODE='"   + v_tpcode      + "'                 " +
            "   and SEQ='"      + v_seq      + "'                 ";
            rs1 = connMgr.executeQuery(sql);
            if ( rs1.next() ) { 
                v_recseq = rs1.getInt(1) + 1;
            }
            else
            {
                v_recseq = 1;
            }
            rs1.close();
            
            // ----------------------   게시판 table 에 입력  --------------------------
            sql1 =  "insert into                                             \n" +
                    "        tz_Toronrecom                           \n" +
                    "        (                                               \n" +
                    "         subj,                                        \n" +
                    "         year,                                           \n" +
                    "         subjseq,                                        \n" +
                    "         tpcode,                                        \n" +
                    "         seq,                                         \n" +
                    "         recseq,                                         \n" +
                    "         userid,                                         \n" +
                    "         indate                                         \n" +                    
                    "        )                                               \n" +
                    " values                                                 \n" +
                    "        (                                               \n" +
                    "         ?,                                             \n" +
                    "         ?,                                             \n" +
                    "         ?,                                             \n" +
                    "         ?,                                             \n" +
                    "         ?,                                             \n" +
                    "         ?,                                             \n" +
                    "         ?,                                             \n" +
                    "         to_char(sysdate, 'yyyymmddhh24miss')           \n" +
                    "        )                                               \n";

            pstmt1 = connMgr.prepareStatement(sql1);
            int sequnce = 1;
            pstmt1.setString(sequnce++, v_subj);
            pstmt1.setString(sequnce++, v_year);
            pstmt1.setString(sequnce++, v_subjseq);
            pstmt1.setString(sequnce++, v_tpcode);
            pstmt1.setInt(sequnce++, v_seq);
            pstmt1.setInt(sequnce++, v_recseq);
            pstmt1.setString(sequnce++, s_userid);
            
            
            isOk1 = pstmt1.executeUpdate();
            if (pstmt1 != null) { try { pstmt1.close(); } catch (Exception e) {} }
            sql1 = "update tz_Toron set agree=agree+1 " +
                   " where                                               " +
                   "       SUBJ='"     + v_subj   + "'                 " +
                   "   and YEAR='"     + v_year      + "'                 " +
                   "   and SUBJSEQ='"  + v_subjseq      + "'                 " +
                   "   and TPCODE='"   + v_tpcode      + "'                 " +
                   "   and SEQ='"      + v_seq      + "'                 ";
            
            pstmt1 = connMgr.prepareStatement(sql1);
            sequnce = 1;
            pstmt1.setString(sequnce++, v_subj);
            pstmt1.setString(sequnce++, v_year);
            pstmt1.setString(sequnce++, v_subjseq);
            pstmt1.setString(sequnce++, v_tpcode);
            pstmt1.setInt(sequnce++, v_seq);
            
            isOk1 = pstmt1.executeUpdate();            
            
            if ( isOk1 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        }

        catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }
    
    /**
    토론주제 등록
    @param box      receive from the form object and session
    @return int
    */
     public int insertTopic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt2 = null;
        String sql1         = "";
        String sql2         = "";
        ListSet ls1         = null;
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_title     = box.getString("p_title");         // 토론주제
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        String  v_started   = box.getString("p_started") +box.getString("p_stime");  // 토론 시작일
        String  v_ended     = box.getString("p_ended") +box.getString("p_ltime");    // 토론 종료일
        String  v_tpcode    = "";

        try { 
            connMgr = new DBConnectionManager();

            // select max(tpcode)
            sql1 = "select max(tpcode) from TZ_TORONTP";
            ls1 = connMgr.executeQuery(sql1);

            if ( ls1.next() ) { 
                v_tpcode = (StringManager.toInt( ls1.getString(1)) + 1) + "";
            }

            // insert TZ_TORONTP table
            sql2 =  "insert into TZ_TORONTP(year,subj,subjseq,tpcode,title,adcontent,aduserid,addate,started,ended,luserid,ldate) ";
            sql2 +=  "values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?,to_char(sysdate,'YYYYMMDDHH24MISS'))";
            pstmt2 = connMgr.prepareStatement(sql2);

            pstmt2.setString(1, v_year);
            pstmt2.setString(2, v_subj);
            pstmt2.setString(3, v_subjseq);
            pstmt2.setString(4, v_tpcode);
            pstmt2.setString(5, v_title);
//            pstmt2.setString(6, v_adcontent);
            pstmt2.setCharacterStream(6,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt2.setString(7, v_user_id);
            pstmt2.setString(8, v_started);
            pstmt2.setString(9, v_ended);
            pstmt2.setString(10, v_user_id);

            isOk = pstmt2.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null )     { try { ls1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    토론주제 수정
    @param box      receive from the form object and session
    @return int
    */
     public int updateTopic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        String sql1         = "";
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_title     = box.getString("p_title");         // 토론주제
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        String  v_started   = box.getString("p_started") +box.getString("p_stime");  // 토론 시작일
        String  v_ended     = box.getString("p_ended") +box.getString("p_ltime");    // 토론 종료일
        String  v_tpcode    = box.getString("p_tpcode");

        try { 
            connMgr = new DBConnectionManager();


            // update TZ_TORONTP table
            sql1 =  "update TZ_TORONTP set title=?,adcontent=?,started=?,ended=? ";
            sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_title);
//            pstmt1.setString(2, v_adcontent);
            pstmt1.setCharacterStream(2,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt1.setString(3, v_started);
            pstmt1.setString(4, v_ended);
            pstmt1.setString(5, v_subj);
            pstmt1.setString(6, v_year);
            pstmt1.setString(7, v_subjseq);
            pstmt1.setString(8, v_tpcode);

            isOk = pstmt1.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    토론주제 삭제
    @param box      receive from the form object and session
    @return int
    */
     public int deleteTopic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1         = "";
        String sql2         = "";
        int isOk1           = 0;
        int isOk2           = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // delete TZ_TORONTP table
            sql1 =  "delete from TZ_TORONTP ";
            sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_subj);
            pstmt1.setString(2, v_year);
            pstmt1.setString(3, v_subjseq);
            pstmt1.setString(4, v_tpcode);

            isOk1 = pstmt1.executeUpdate();

            // delete TZ_TORONTP table
            sql2 =  "delete from TZ_TORON ";
            sql2 +=  "where subj=? and year=? and subjseq=? and tpcode=? ";
            pstmt2 = connMgr.prepareStatement(sql2);

            pstmt2.setString(1, v_subj);
            pstmt2.setString(2, v_year);
            pstmt2.setString(3, v_subjseq);
            pstmt2.setString(4, v_tpcode);

            isOk2 = pstmt2.executeUpdate();

            if ( isOk1 > 0) connMgr.commit();
            else connMgr.rollback();
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1;
    }


    /**
    토론글 등록
    @param box      receive from the form object and session
    @return int
    */
     public int insertToron(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt2 = null;
        String sql1         = "";
        String sql2         = "";
        ListSet ls1         = null;
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        String  v_title     = box.getString("p_title");         // 제목
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        int     v_seq       = 0;

        try { 
            connMgr = new DBConnectionManager();

            // select max(seq)
            sql1 = "select nvl(max(seq), 0) from TZ_TORON ";
            ls1 = connMgr.executeQuery(sql1);
            if ( ls1.next() ) { v_seq = ls1.getInt(1) + 1;    }
            ls1.close();

            // insert TZ_TORON table
            sql2 =  "insert into TZ_TORON(subj,year,subjseq,tpcode,seq,refseq,title,adcontent,aduserid,addate,luserid,ldate) ";
            sql2 +=  "values (?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, to_char(sysdate,'YYYYMMDDHH24MISS'))";
            pstmt2 = connMgr.prepareStatement(sql2);

            pstmt2.setString(1, v_subj);
            pstmt2.setString(2, v_year);
            pstmt2.setString(3, v_subjseq);
            pstmt2.setString(4, v_tpcode);
            pstmt2.setInt(5, v_seq);
            pstmt2.setInt(6, v_seq);
            pstmt2.setString(7, v_title);
//            pstmt2.setString(8, v_adcontent);
            pstmt2.setCharacterStream(8,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt2.setString(9, v_user_id);
            pstmt2.setString(10, v_user_id);

            isOk = pstmt2.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null )     { try { ls1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    토론글 수정
    @param box      receive from the form object and session
    @return int
    */
     public int updateToron(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        String sql1         = "";
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        int     v_seq       = box.getInt("p_seq");              // 의견일련번호
        String  v_title     = box.getString("p_title");         // 토론주제
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        String  v_started   = box.getString("p_started") +box.getString("p_stime");  // 토론 시작일
        String  v_ended     = box.getString("p_ended") +box.getString("p_ltime");    // 토론 종료일

        try { 
            connMgr = new DBConnectionManager();


            // update TZ_TORONTP table
            sql1 =  "update TZ_TORON set title=?,adcontent=? ";
            sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? and seq=? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_title);
//            pstmt1.setString(2, v_adcontent);
            pstmt1.setCharacterStream(2,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt1.setString(3, v_subj);
            pstmt1.setString(4, v_year);
            pstmt1.setString(5, v_subjseq);
            pstmt1.setString(6, v_tpcode);
            pstmt1.setInt(7, v_seq);

            isOk = pstmt1.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    토론 답글 등록
    @param box      receive from the form object and session
    @return int
    */
     public int insertToronReply(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt3 = null;
        String sql1         = "";
        String sql2         = "";
        String sql3         = "";
        ListSet ls2         = null;
        int isOk1           = 0;
        int isOk3           = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        String  v_title     = box.getString("p_title");         // 제목
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        int     v_refseq    = box.getInt("p_refseq");           // 상위글 번호
        int     v_levels    = box.getInt("p_levels");
        int     v_position  = box.getInt("p_position");
        int     v_seq       = 0;

        try { 
            connMgr = new DBConnectionManager();

            // 기존 답변글 위치 한칸밑으로 변경
            sql1  = "update TZ_TORON ";
            sql1 += "   set position = position + 1 ";
            sql1 += " where refseq   = ? ";
            sql1 += "   and position > ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_refseq);
            pstmt1.setInt(2, v_position);
            isOk1 = pstmt1.executeUpdate();

            // select max(seq)
            sql2 = "select max(seq) from TZ_TORON";
            ls2 = connMgr.executeQuery(sql2);
            if ( ls2.next() ) { 
                v_seq = ls2.getInt(1) + 1;
            }

            // insert TZ_TORON table
            sql3 =  "insert into TZ_TORON(subj,year,subjseq,tpcode,seq,refseq,levels,position,title,adcontent,aduserid,addate,luserid,ldate) ";
            sql3 +=  "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, to_char(sysdate,'YYYYMMDDHH24MISS'))";
            pstmt3 = connMgr.prepareStatement(sql3);

            pstmt3.setString(1, v_subj);
            pstmt3.setString(2, v_year);
            pstmt3.setString(3, v_subjseq);
            pstmt3.setString(4, v_tpcode);
            pstmt3.setInt(5, v_seq);
            pstmt3.setInt(6, v_refseq);
            pstmt3.setInt(7, v_levels + 1);
            pstmt3.setInt(8, v_position + 1);
            pstmt3.setString(9, v_title);
//            pstmt3.setString(10, v_adcontent);
            pstmt3.setCharacterStream(10,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt3.setString(11, v_user_id);
            pstmt3.setString(12, v_user_id);

            isOk3 = pstmt3.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql3);
            throw new Exception("sql = " + sql3 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls2 != null )     { try { ls2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt3 != null )  { try { pstmt3.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk3;
    }

    /**
    토론글 삭제
    @param box      receive from the form object and session
    @return int
    */
     public int deleteToron(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        String sql1         = "";
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        int     v_seq       = box.getInt("p_seq");              // 의견일련번호

        try { 
            connMgr = new DBConnectionManager();

            // 답변 유무 체크(답변 있을시 삭제불가)
            if ( this.selectToronReply(v_seq) == 0 ) { 

                // delete TZ_TORONTP table
                sql1 =  "delete from TZ_TORON ";
                sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? and seq=?";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, v_subj);
                pstmt1.setString(2, v_year);
                pstmt1.setString(3, v_subjseq);
                pstmt1.setString(4, v_tpcode);
                pstmt1.setInt(5, v_seq);

                isOk = pstmt1.executeUpdate();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

   /**
   * 삭제시 하위 답변 유무 체크
   * @param seq          게시판 번호
   * @return result      0 : 답변 없음,    1 : 답변 있음
   * @throws Exception
   */
   public int selectToronReply(int seq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = "  select count(*) cnt                 ";
            sql += "  from                                ";
            sql += "    (select refseq, levels, position  ";
            sql += "      from TZ_TORON                   ";
            sql += "     where seq = " + seq;
            sql += "     ) a, TZ_TORON b                  ";
            sql += " where a.refseq = b.refseq            ";
            sql += "   and b.levels = (a.levels +1)        ";
            sql += "   and b.position = (a.position +1)    ";
//            System.out.println("sql ==  ==  ==  ==  ==  ==  == = > " +sql);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

    /**
    토론방 주제 조회
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ToronData selectTopic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls2         = null;
        String sql1         = "";
        String sql2         = "";
        int isOk            = 0;
        ToronData data2     = null;
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        try { 
            connMgr = new DBConnectionManager();

            // update TZ_TORONTP
            sql1 = "update TZ_TORONTP set cnt=cnt +1 ";
            sql1 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql1 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
            isOk = connMgr.executeUpdate(sql1);

            // select stated,ended,title,addate,adcontent,aduserid,name,cnt
            sql2 = "select A.started,A.ended,A.title,A.addate,A.adcontent,A.aduserid, ";
            sql2 += "(select name from TZ_MEMBER where userid = A.aduserid) as name,cnt ";
            sql2 += "from TZ_TORONTP A ";
            sql2 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql2 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
//            System.out.println("sql2 ==  ==  ==  == = > " +sql2);
            ls2 = connMgr.executeQuery(sql2);

            if ( ls2.next() ) { 
                data2=new ToronData();
                data2.setStarted( ls2.getString("started") );
                data2.setEnded( ls2.getString("ended") );
                data2.setTitle( ls2.getString("title") );
                data2.setAddate( ls2.getString("addate") );
                data2.setAdcontent( ls2.getCharacterStream("adcontent") );
                data2.setAduserid( ls2.getString("aduserid") );
                data2.setName( ls2.getString("name") );
                data2.setCnt( ls2.getInt("cnt") );
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data2;
    }

    /**
    토론글 조회
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ToronData selectToron(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls2         = null;
        String sql1         = "";
        String sql2         = "";
        ToronData data2     = null;
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        int     v_seq       = box.getInt("p_seq");              // 일련번호
        String s_userid     = box.getSession("userid");
        int     isOk1       = 0;
        int     l           = 0;
        try { 
            connMgr = new DBConnectionManager();

            // update TZ_TORONTP
            sql1 = "update TZ_TORON set cnt=cnt +1 ";
            sql1 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql1 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
            sql1 += " and seq=" +SQLString.Format(v_seq);
            isOk1 = connMgr.executeUpdate(sql1);

            // select seq,refseq,levels,position,title,adcontent,aduserid,cnt,addate,name
            sql2 = "select seq,refseq,levels,position,title,adcontent,aduserid,cnt,addate,agree,contrary, ";
            sql2 += "(select name from TZ_MEMBER where userid = A.aduserid) as name, ";
            sql2 += "(                                           "+
                    "           select decode(count(*), 0, 'Y', 'N')    "+
                    "           from TZ_TORON c, TZ_TORON d             "+
                    "           where c.refseq = d.refseq               "+
                    "           and d.levels = (c.levels +1)            "+
                    "           and d.position = (c.position +1)        "+
                    "           and c.seq = a.seq                       "+
                    "       ) delyn, (                                  "+
                    "   select count(*)                                 "+
                    "   from tz_board_agreeinfo                         "+
                    "   where subjseq = A.subjseq                       "+
                    "   and subj = A.subj                               "+
                    "   and year = A.year                               "+
                    "   and tpcode = A.tpcode                           "+
                    "   and seq = A.seq                                 "+
                    "   and userid = " +SQLString.Format(s_userid)       +
                    ") agree_count                                      ";
            sql2 += " from TZ_TORON A ";
            sql2 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql2 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
            sql2 += " and seq=" +SQLString.Format(v_seq);
//System.out.println("sql2 ==  ==  ==  ==  ==  == > " +sql2);
            ls2 = connMgr.executeQuery(sql2);
            
            if ( ls2.next() ) { 
                data2=new ToronData();
                data2.setSeq( ls2.getInt("seq") );
                data2.setRefseq( ls2.getInt("refseq") );
                data2.setLevels( ls2.getInt("levels") );
                data2.setPosition( ls2.getInt("position") );
                data2.setTitle( ls2.getString("title") );
                data2.setAdcontent( ls2.getCharacterStream("adcontent") );
                data2.setAduserid( ls2.getString("aduserid") );
                data2.setCnt( ls2.getInt("cnt") );
                data2.setAddate( ls2.getString("addate") );
                data2.setName( ls2.getString("name") );
                data2.setDelYn( ls2.getString("delyn"));
                data2.setAgreecount( ls2.getInt("agree_count"));
                data2.setAgree( ls2.getInt("agree"));
                data2.setContrary( ls2.getInt("contrary"));
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data2;
    }

    /**
    토론글 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectToronList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        ToronData data1     = null;
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        int     l           = 0;
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            // select seq,title,aduserid,addate,levels,name
            sql1 = "select seq,title,aduserid,addate,levels, ";
            sql1 += "(select name from TZ_MEMBER where userid = A.aduserid) as name ";
            sql1 += " from TZ_TORON A ";
            sql1 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql1 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
            sql1 += " order by A.refseq desc, A.position asc ";
//            System.out.println("sql1 ==  ==  ==  ==  ==  == > " +sql1);
            ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    data1 = new ToronData();
                    data1.setSeq( ls1.getInt("seq") );
                    data1.setTitle( ls1.getString("title") );
                    data1.setAduserid( ls1.getString("aduserid") );
                    data1.setAddate( ls1.getString("addate") );
                    data1.setLevels( ls1.getInt("levels") );
                    data1.setName( ls1.getString("name") );
                    list1.add(data1);
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /**
    토론방 주제 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectTopicList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        ToronData data1     = null;
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        int     l           = 0;
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            // select tpcode,title,aduserid,cnt,addate,started,ended,name
            sql1 = "select tpcode,title,aduserid,cnt,addate,started,ended, ";
            sql1 += "(select name from TZ_MEMBER where userid = A.aduserid) as name ";
            sql1 += "  from TZ_TORONTP A ";
            sql1 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql1 += " and subjseq=" +SQLString.Format(v_subjseq);
            sql1 += " order by tpcode desc";
//            System.out.println("sql1 ==  ==  ==  ==  ==  == > " +sql1);
            ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    data1 = new ToronData();
                    data1.setTpcode( ls1.getString("tpcode") );
                    data1.setTitle( ls1.getString("title") );
                    data1.setAduserid( ls1.getString("aduserid") );
                    data1.setCnt( ls1.getInt("cnt") );
                    data1.setAddate( ls1.getString("addate") );
                    data1.setStarted( ls1.getString("started") );
                    data1.setEnded( ls1.getString("ended") );
                    data1.setName( ls1.getString("name") );
                    list1.add(data1);
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

     /**
     찬성 반대 카운트 업데이트
     @param box      receive from the form object and session
     @return int
     */
      public int updateColumn(RequestBox box) throws Exception { 
         DBConnectionManager  connMgr = null;
         PreparedStatement pstmt1 = null;
         String sql1         = "";
         
         ListSet ls2         = null;
         String sql2         = "";
         String sql3         = "";
         int v_seq2          = 0;
         PreparedStatement pstmt3 = null;
         PreparedStatement pstmt = null;
         int isOk3            = 0;
         
         int isOk            = 0;
         String  v_user_id   = box.getSession("userid");
         String  v_year      = box.getString("p_year");          // 년도
         String  v_subj      = box.getString("p_subj");          // 과목
         String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
         String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
         int     v_seq       = box.getInt("p_seq");              // 의견일련번호
         String  v_title     = box.getString("p_title");         // 토론주제
         String  v_adcontent = box.getString("p_adcontent");     // 내용
         String  v_started   = box.getString("p_started") +box.getString("p_stime");  // 토론 시작일
         String  v_ended     = box.getString("p_ended") +box.getString("p_ltime");    // 토론 종료일
         String  v_column    = box.getString("p_column");
         String  v_gubun = "N";
         
         if("AGREE".equals(v_column)) {
             v_gubun = "Y";
         } 
         
         try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
             
             sql1 =  "update TZ_TORON set " + v_column + " = NVL(" + v_column + ", 0) + 1 ";
             sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? and seq=? ";
             pstmt1 = connMgr.prepareStatement(sql1);

             /*pstmt1.setString(1, v_column);
             pstmt1.setString(2, v_column);*/
             pstmt1.setString(1, v_subj);
             pstmt1.setString(2, v_year);
             pstmt1.setString(3, v_subjseq);
             pstmt1.setString(4, v_tpcode);
             pstmt1.setInt   (5, v_seq);

             isOk = pstmt1.executeUpdate();
             
             sql2 = "select max(recseq) from tz_board_agreeinfo     \n"
                  + "where subj = ?                                 \n"
                  + "and year = ?                                   \n"
                  + "and subjseq = ?                                \n"
                  + "and tpcode = ?                                 \n"
                  + "and seq = ?                                    \n";                 
                
             pstmt = connMgr.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             pstmt.setString(1, v_subj);
             pstmt.setString(2, v_year);
             pstmt.setString(3, v_subjseq);
             pstmt.setString(4, v_tpcode);
             pstmt.setInt(5, v_seq);
             ls2 = new ListSet(pstmt);        //      ListSet (ResultSet) 객체생성
             if ( ls2.next() ) { 
                 v_seq2 = ls2.getInt(1) + 1;
             }
             ls2.close();
             // insert tz_board_agreeinfo table // 찬성, 반대 이력
             sql3 =  "insert into tz_board_agreeinfo(subj, year, subjseq, tpcode, seq, recseq, userid, indate, gubun) ";
             sql3 +=  "values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?)";
             pstmt3 = connMgr.prepareStatement(sql3);

             pstmt3.setString(1, v_subj);
             pstmt3.setString(2, v_year);
             pstmt3.setString(3, v_subjseq);
             pstmt3.setString(4, v_tpcode);
             pstmt3.setInt(5, v_seq);
             pstmt3.setInt(6, v_seq2);
             pstmt3.setString(7, v_user_id);
             pstmt3.setString(8, v_gubun);
             
             isOk3 = pstmt3.executeUpdate();             
             
             if ( isOk > 0) {          
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
             } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
             }              
         } catch ( Exception ex ) { 
             connMgr.rollback();
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
        	 if ( ls2 != null )  { try { ls2.close(); } catch ( Exception e1 ) { } }
             if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
             if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
             if ( pstmt3 != null )  { try { pstmt3.close(); } catch ( Exception e1 ) { } }
             if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }
}