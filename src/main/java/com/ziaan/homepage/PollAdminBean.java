// **********************************************************
//  1. 제      목: Poll 관리
//  2. 프로그램명 : PollAdminBean.java
//  3. 개      요: Poll 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 이창훈 2005. 8.  2
//  7. 수      정:
// **********************************************************

package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

/**
 * Poll 관리(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class PollAdminBean { 

    public PollAdminBean() { }


   /**
   * Poll화면 리스트
   */
    public ArrayList selectListPoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        PollData data = null;
        DataBox             dbox    = null;

        String v_searchtext = box.getString("p_searchtext");
        //String s_grcode 		= box.getString("s_grcode");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();
            
			sql += " select seq, ";
			sql += "        title, ";
			sql += "        started, ";
			sql += "        ended, ";
			sql += "        (select count(*) from tz_polllog where seq=a.seq) cnt, ";
            sql += "   ( select grcodenm from tz_grcode c where c.grcode=a.grcode) grcodenm ";    						
			sql += "   from tz_poll a ";
			sql += " order by seq desc ";
  			
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
   * Poll화면 상세보기
   */
   public DataBox selectViewPoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        PollData data = null;
        DataBox             dbox    = null;

        int v_seq = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            // sql  = "   select A seq, B title, C started, D ended, E f_use, F luserid, G ldate,         ";
            // sql += "          ( select sum(cnt) from TZ_POllSEL where seq = A) total                   ";
            // sql += "    from                                                                           ";
            // sql += "        (  select seq A , title B, started C, ended D, f_use E, luserid F, ldate G ";
            // sql += "             from TZ_POLL                                                          ";
            // sql += "            where seq  = " + v_seq;
            // sql += "       )                                                                            ";
            // sql += " order by seq desc                                                                  ";
            
            
            sql = " select                                                      \n";
            sql += "   A seq,                                                    \n";
            sql += "   B title,                                                  \n";
            sql += "   C started,                                                \n";
            sql += "   D ended,                                                  \n";
            sql += "   E f_use,                                                  \n";
            sql += "   F luserid,                                                \n";
            sql += "   G ldate,                                                  \n";
            sql += "   H grcodenm,                                               \n";
            sql += "   I grcode,                                                 \n";
            sql += "   ( select sum(cnt) from TZ_POllSEL where seq = A) total    \n";
            sql += " from                                                        \n";
            sql += "   (                                                         \n";
            sql += "      select                                                 \n";
            sql += " 	   a.seq A ,                                             \n";
            sql += " 	   a.title B,                                            \n";
            sql += " 	   a.started C,                                          \n";
            sql += " 	   a.ended D,                                            \n";
            sql += " 	   a.f_use E,                                            \n";
            sql += " 	   a.luserid F,                                          \n";
            sql += " 	   a.ldate G,                                             \n";
            sql += " 	   (select                                               \n";
            sql += " 	     grcodenm                                            \n";
            sql += " 		from                                                 \n";
            sql += " 		  tz_grcode                                          \n";
            sql += " 		where                                                \n";
            sql += " 		  grcode = a.grcode) H,                              \n";
            sql += " 	   grcode I                                              \n";
            sql += "      from                                                   \n";
            sql += " 	   TZ_POLL a                                             \n";
            sql += "      where seq  = " + v_seq;
            sql += "   )                                                         \n";
            sql += "   order by seq desc                                         \n";
           
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }


  /**
   * Poll화면 상세보기 (검색조건에 따른 지문 및 투표결과)
   */
     public ArrayList selectResultPoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        PollSelData data = null;
        DataBox             dbox    = null;

        int v_seq = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select a.seq seq, a.selnum selnum, a.seltxt seltxt, nvl(b.cnt, 0) cnt  ";         
            sql += "   from TZ_POLLSEL a,                                                   ";
            sql += "        ( select seq, selnum, nvl(count(*),0) cnt                       ";
            sql += "            from TZ_POLLLOG                                             ";
            sql += "          where seq = " + v_seq;
            sql += "           group by seq, selnum                                         ";
            sql += "        ) b                                                             ";
            sql += " where a.seq = b.seq( +)                                                ";
            sql += "   and a.selnum = b.selnum( +)                                          ";
            sql += "   and a.seq = " + v_seq;
            sql += " order by a.selnum asc                                                  ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

  /**
   * Poll화면 상세보기 (유저 투표 결과 가지고 오지 않음)
   */
   public ArrayList selectResultPrePoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        // PollSelData data = null;
        DataBox             dbox    = null;

        int v_seq = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select seq, selnum, seltxt, 0 cnt  ";
            sql += "   from TZ_POLLSEL                  ";
            sql += "  where seq = " + v_seq;
            sql += " order by seq asc                   ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                // data = new PollSelData();
                // data.setSeq( ls.getInt("seq") );
                // data.setSelnum( ls.getInt("selnum") );
                // data.setSeltxt( ls.getString("seltxt") );
                // data.setCnt( ls.getInt("cnt") );
                // list.add(data);
                
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    * Poll 등록할때
    */
    public int insertPoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";

        int isOk1 = 0;
        int isOk2 = 0;
        int isOk2_check = 0;

        int v_seq = box.getInt("p_seq");
        String v_title = box.getString("p_title");
        String v_started = box.getString("p_started");
        String v_ended = box.getString("p_ended");
        String v_f_use = box.getString("p_f_use");
        String ss_grcode = box.getString("s_grcode");

        String s_userid = box.getSession("userid");

        Vector v_vseltxt = box.getVector("p_seltxt");
        String v_sseltxt = "";

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);

           sql1  = "select max(seq) from TZ_POLL  ";
           ls = connMgr.executeQuery(sql1);
           if ( ls.next() ) { 
               v_seq = ls.getInt(1) + 1;
           } else { 
               v_seq = 1;
           }

           sql2 =  "insert into TZ_POLL(seq, title, started, ended, f_use, luserid, ldate, grcode)       ";
           sql2 += "            values (?, ?, ?, ? , ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?) ";

           pstmt1 = connMgr.prepareStatement(sql2);

            pstmt1.setInt(1, v_seq);
            pstmt1.setString(2, v_title);
            pstmt1.setString(3, v_started);
            pstmt1.setString(4, v_ended);
            pstmt1.setString(5, v_f_use);
            pstmt1.setString(6, s_userid);
            pstmt1.setString(7, ss_grcode);

           isOk1 = pstmt1.executeUpdate();

           /* ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==       지문 등록      ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == =*/
           sql3 =  "insert into TZ_POLLSEL(seq, selnum, seltxt, cnt) ";
           sql3 += "                values(?, ?, ?, 0)               ";

           pstmt2 = connMgr.prepareStatement(sql3);

           isOk2 = 1;
           for ( int i = 0; i < v_vseltxt.size() ; i++ ) { 
               v_sseltxt = (String)v_vseltxt.elementAt(i);

               if ( !v_sseltxt.equals("") ) { 
                   pstmt2.setInt(1, v_seq);
                   pstmt2.setInt(2, (i +1));
                   pstmt2.setString(3, v_sseltxt);
                   isOk2_check = pstmt2.executeUpdate();
                   if ( isOk2_check == 0) isOk2 = 0;
               }
           }

            if ( isOk1 > 0 && isOk2 > 0) connMgr.commit();
            else connMgr.rollback();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1 * isOk2;
    }

    /**
    * Poll 수정하여 저장할때
    */
     public int updatePoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ResultSet rs1   = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        PreparedStatement pstmt5 = null;
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sql4 = "";
        String sql5 = "";
        int isOk1 = 0;
        int isOk2 = 0;
        int isOk2_check = 0;
        int cnt = 0;


        int v_seq        = box.getInt("p_seq");
        String v_title   = box.getString("p_title");
        String v_started = box.getString("p_started");
        String v_ended   = box.getString("p_ended");
        String v_f_use   = box.getString("p_f_use");
        String s_grcode  = box.getString("s_grcode");

        String s_userid = box.getSession("userid");

        Vector v_vseltxt = box.getVector("p_seltxt");
        String v_sseltxt = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1  = " update TZ_POLL set title = ? , started = ?, ended = ?, f_use = ?, luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'), grcode = ? ";
            sql1 += "  where seq = ?                                                                                                                ";

            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_title);
            pstmt1.setString(2, v_started);
            pstmt1.setString(3, v_ended);
            pstmt1.setString(4, v_f_use);
            pstmt1.setString(5, s_userid);
            pstmt1.setString(6, s_grcode);
            pstmt1.setInt(7, v_seq);

            isOk1 = pstmt1.executeUpdate();

           /* ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==       지문 작성, 수정, 삭제     ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == */
           sql2 =  " delete from TZ_POLLSEL        ";
           sql2 += "  where seq = ? and selnum = ? ";
           pstmt2 = connMgr.prepareStatement(sql2);

           sql3 =  " select count(*) cnt from TZ_POLLSEL  ";
           sql3 += "  where seq = ? and selnum = ?        ";
           pstmt3 = connMgr.prepareStatement(sql3);

           sql4 =  " update TZ_POLLSEL set seltxt = ? ";
           sql4 += "  where seq = ? and selnum = ?    ";
           pstmt4 = connMgr.prepareStatement(sql4);

           sql5 =  "insert into TZ_POLLSEL(seq, selnum, seltxt, cnt) ";
           sql5 += "                values(?, ?, ?, 0)               ";
           pstmt5 = connMgr.prepareStatement(sql5);

           isOk2 = 1;
           isOk2_check = 1;
           for ( int i = 0; i < v_vseltxt.size() ; i++ ) { 
               v_sseltxt = (String)v_vseltxt.elementAt(i);

               if ( v_sseltxt.equals("") ) {                 // 공백일 경우 삭제
                   pstmt2.setInt(1, v_seq);
                   pstmt2.setInt(2, (i +1));
                   // isOk2_check = pstmt2.executeUpdate(); 삭제는 반영되는 로우가 없을 수 잇으므로 체크 제외
                   pstmt2.executeUpdate();

               } else {                                    // 셀렉트 후 있으면 업데이트 없으면 인서트

                   pstmt3.setInt(1, v_seq);
                   pstmt3.setInt(2, (i +1));
                   try { 
                       rs1 = pstmt3.executeQuery();
                       if ( rs1.next() ) { 
                           cnt = rs1.getInt("cnt");
                       }
                   } catch ( Exception ex ) { 
                   } finally { 
                       if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
                   }

                   if ( cnt > 0 ) {                   // update
                       pstmt4.setString(1, v_sseltxt);
                       pstmt4.setInt(2, v_seq);
                       pstmt4.setInt(3, (i +1));
                       isOk2_check = pstmt4.executeUpdate();
                   } else {                          // insert
                       pstmt5.setInt(1, v_seq);
                       pstmt5.setInt(2, (i +1));
                       pstmt5.setString(3, v_sseltxt);
                       isOk2_check = pstmt5.executeUpdate();
                   }

               }
               if ( isOk2_check == 0) isOk2 = 0;

           }

           if ( isOk1 > 0 && isOk2 > 0) connMgr.commit();
           else connMgr.rollback();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e1 ) { } }
            if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e1 ) { } }
            if ( pstmt5 != null ) { try { pstmt5.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { };
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1 * isOk2;
    }

    /**
    * Poll 삭제할때 - Poll 지문 삭제
    */
    public int deletePoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";

        int isOk1 = 0;
        int isOk2 = 0;
        int isOk3 = 0;

        int v_seq = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1  = " delete from TZ_POLL  ";
            sql1 += "   where seq = ?      ";

            sql2  = " delete from TZ_POLLSEL ";
            sql2 += "   where seq = ?        ";

            sql3  = " delete from TZ_POLLLOG ";
            sql3 += "   where seq = ?        ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_seq);
            isOk1 = pstmt1.executeUpdate();

            pstmt2 = connMgr.prepareStatement(sql2);
            pstmt2.setInt(1, v_seq);
            isOk2 = pstmt2.executeUpdate();

            pstmt3 = connMgr.prepareStatement(sql2);
            pstmt3.setInt(1, v_seq);
            isOk3 = pstmt3.executeUpdate();

            if ( isOk1 > 0 && isOk2 > 0 ) connMgr.commit();
            else connMgr.rollback();

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1 + "\r\n" +sql2);
            throw new Exception("sql = " + sql1 + "\r\n" + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { };
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1*isOk2;
    }


}
