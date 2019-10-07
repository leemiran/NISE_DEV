// **********************************************************
//  1. 제      목: 로긴관리
//  2. 프로그램명 : MemberInfoBean.java
//  3. 개      요: 로그인,패스워드찾기
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7.  2
//  7. 수      정:
// **********************************************************
package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class MemberInfoBean { 

    public MemberInfoBean() { }

    /**
     * Login 정보 변경 (lgcnt:로그인횟수, lglast:최종로그인시간, lgip:로그인ip
     * @param box       receive from the form object and session
     * @return is_Ok    1 : success      2 : fail
     */
    public int updateLoginData(String p_userid, String p_userip) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int is_Ok = 0;
        String v_userid = p_userid;
        String v_userip = p_userip;

        try { 
              connMgr = new DBConnectionManager();

              sql  = " update TZ_MEMBER                       ";
              sql += " set lgcnt=lgcnt +1, lglast= to_char(sysdate, 'YYYYMMDDHH24MISS'), lgip=" +StringManager.makeSQL(v_userip);
              sql += " where userid = " + StringManager.makeSQL(v_userid);

// System.out.println("Loindata sql == > " + sql);
            is_Ok = connMgr.executeUpdate(sql);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }

   /**
     * 개인정보 조회
     * @param box       receive from the form object and session
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList memberInfoView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_userid = box.getSession("userid");

        try { 
              connMgr = new DBConnectionManager();

              list = new ArrayList();

/* 2006.06.15 - 수정 - 쿼리변경

              sql  = " select userid, birth_date, name, email, cono, comp, get_compnm(comp,2,2) compnm, pwd, ";
              sql += "        post1, post2, addr, addr2, hometel, handphone, comptel, tel_line,    ";
              sql += "        lgcnt, lgfirst, lglast, lgip, jikwi, get_jikwinm(jikwi,comp) jikwinm, work_plcnm, deptnam     ";
              sql += " from TZ_MEMBER                                               ";
              sql += " where userid = " + StringManager.makeSQL(v_userid);
 */
              sql = "SELECT                                                                    \n" +
                    "    userid,                                                               \n" + 
                    "    birth_date,                                                                \n" + 
                    "    name,                                                                 \n" + 
                    "    email,                                                                \n" + 
                    "    comp,                                                                 \n" + 
                    "    pwd,                                                                  \n" + 
                    "    post1,                                                                \n" + 
                    "    post2,                                                                \n" + 
                    "    address1,                                                             \n" + 
                    "    address2,                                                             \n" + 
                    "    hometel,                                                              \n" + 
                    "    handphone,                                                            \n" + 
                    "    comptel,                                                              \n" + 
                    "    tel_line,                                                             \n" + 
                    "    lgcnt,                                                                \n" + 
                    "    lglast,                                                               \n" + 
                    "    lgip                                                                  \n" + 
                    "FROM                                                                      \n" +
                    "    TZ_MEMBER                                                             \n" +
                    "WHERE                                                                     \n" +
                    "    userid = "+StringManager.makeSQL(v_userid)+"                          \n";
              
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
     * 개인정보 저장
     * @param box       receive from the form object and session
     * @return is_Ok    1 : update ok      2 : update fail
     * @throws Exception
     */
    public int memberInfoUpdate(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt     = null;
        PreparedStatement pstmt2    = null;
        String sql      = "";
        String sql2     = "";
        int is_Ok   = 0;
        int is_Ok2  = 0;

        String v_pwd        = box.getString("p_pwd");
        String v_email      = box.getString("p_email");
        String v_post1      = box.getString("p_post1");
        String v_post2      = box.getString("p_post2");
        String v_addr       = box.getString("p_addr");
        String v_addr2      = box.getString("p_addr2");
        String v_hometel    = box.getString("p_hometel");
        String v_handphone  = box.getString("p_handphone");
        String v_comptel    = box.getString("p_comptel");
        String v_tel_line   = box.getString("p_tel_line");
//        String v_jikwi        = box.getString("p_jikwi");
//        String v_jikwinm  = box.getString("p_jikwinm");
//        String v_work_plcnm   = box.getString("p_work_plcnm");
//        String v_deptnam  = box.getString("p_deptnam");
        String v_userid     = box.getSession("userid");

        try { 
             connMgr = new DBConnectionManager();

            sql  = " update TZ_MEMBER set                                               ";
            sql += "    pwd = ?, email = ?, post1 = ?, post2 = ?, address1 = ?, address2 = ?,  ";
            sql += "    hometel = ?, handphone = ?, comptel = ?, tel_line = ?,          ";
//            sql += "    work_plcnm = ?, deptnam = ?,            ";
//          sql += "    jikwi = ?, jikwinm = ?, work_plcnm = ?, deptnam = ?,            ";
            sql += "    ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')            ";
            sql += " where userid = ?                                                   ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_pwd);
            pstmt.setString(2, v_email);
            pstmt.setString(3, v_post1);
            pstmt.setString(4, v_post2);
            pstmt.setString(5, v_addr);
            pstmt.setString(6, v_addr2);
            pstmt.setString(7, v_hometel);
            pstmt.setString(8, v_handphone);
            pstmt.setString(9, v_comptel);
            pstmt.setString(10, v_tel_line);
//          pstmt.setString(11, v_jikwi);
//          pstmt.setString(12, v_jikwinm);
//            pstmt.setString(11, v_work_plcnm);
//            pstmt.setString(12, v_deptnam);
            pstmt.setString(11, v_userid);

            is_Ok = pstmt.executeUpdate();

            // update TZ_TUTOR table
            sql2 = "update TZ_TUTOR ";
            sql2 += "set     post1=?,";
            sql2 += "        post2=?, ";
            sql2 += "        add1=?, ";
            sql2 += "        add2=?, ";
            sql2 += "        phone=?, ";
            sql2 += "        handphone=?, ";
            sql2 += "        email=?, ";
            sql2 += "        luserid=?,";
            sql2 +=  "   ldate=to_char(sysdate, 'YYYYMMDD') where userid=?";

            pstmt2 = connMgr.prepareStatement(sql2);

            pstmt2.setString(1, v_post1);
            pstmt2.setString(2, v_post2);
            pstmt2.setString(3, v_addr);
            pstmt2.setString(4, v_addr2);
            pstmt2.setString(5, v_comptel);
            pstmt2.setString(6, v_handphone);
            pstmt2.setString(7, v_email);
            pstmt2.setString(8, v_userid);
            pstmt2.setString(9, v_userid);

            is_Ok2 = pstmt2.executeUpdate();


         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

        return is_Ok;
    }


    /**
     * 직위 셀렉트 박스
     * @param userid      유저아이디
     * @param name        셀렉트박스명
     * @param selected    선택값
     * @param event       이벤트명
     * @return
     * @throws Exception
     */
     public static String getJikwiSelect(String name, String selected, String event) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String result = null;
         String              sql     = "";
         int cnt = 0;

         result = "  <SELECT name='" + name + "' " + event + "  style='border-style:solid;border-width: 1px 1px 1px 1px;border-color:cccccc;color:333333;font-size:9pt;background-color:none;width : 120px;height:19px;font-size:9pt;' > \n";
         result +="  <option value='' >== 선택 == </option > ";

         try { 
             connMgr = new DBConnectionManager();

             sql  = " select jikwi, jikwinm    ";
             sql += "   from tz_jikwi                   ";
             sql += "  where grpcomp        = 'ZZZZ'    ";
             sql += " order by jikwi asc";

             ls = connMgr.executeQuery(sql);

             while ( ls.next() ) { 

                 result += " <option value=" + ls.getString("jikwi");
                 if ( selected.equals( ls.getString("jikwi"))) { 
                     result += " selected ";
                 }
                 result += " > " + ls.getString("jikwinm") + "</option > \n";
                 cnt++;
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
     * 권한 셀렉트 박스
     * @param userid      유저아이디
     * @param name        셀렉트박스명
     * @param selected    선택값
     * @param event       이벤트명
     * @return
     * @throws Exception
     */
     public static String getAuthSelect(String userid, String name, String selected, String event) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String result = null;
         String              sql     = "";
         int cnt = 0;

         result = "  <SELECT name='" + name + "' " + event + "  style='border-style:solid;border-width: 1px 1px 1px 1px;border-color:cccccc;color:333333;font-size:9pt;background-color:none;width : 120px;height:19px;font-size:9pt;' > \n";

         try { 
             connMgr = new DBConnectionManager();

             sql  = " select b.gadmin gadmin, b.gadminnm gadminnm    ";
             sql += "   from tz_manager a, tz_gadmin b               ";
             sql += "  where a.gadmin    = b.gadmin                  ";
             sql += "    and a.userid    = " + StringManager.makeSQL(userid);
             sql += "    and a.isdeleted = 'N'                       ";
             sql += "    and to_char(sysdate,'yyyymmdd') between a.fmon and a.tmon ";
             sql += " order by b.gadmin asc";

             ls = connMgr.executeQuery(sql);

             while ( ls.next() ) { 

                 result += " <option value=" + ls.getString("gadmin");
                 if ( selected.equals( ls.getString("gadmin"))) { 
                     result += " selected ";
                 }
                 result += " > " + ls.getString("gadminnm") + "</option > \n";
                 cnt++;
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

         result += "  <option value='ZZ'";

        if ( selected.equals("ZZ") || selected.equals("") ) { 
           result += " selected ";
        }

         result += " > 학습자</option > ";
         result += "  </SELECT > \n";

         if ( cnt == 0) result = "";
         return result;
     }



   /**
     * 개인정보 조회
     * @param  box       receive from the form object and session
     * @return DataBox   개인정보
     * @throws Exception
     */
    public DataBox getMemberInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_userid = box.getSession("userid");

        try { 
              connMgr = new DBConnectionManager();
/* 2006.06.15 수정 - 쿼리변경               
              sql  = " select userid, birth_date, name, email, cono, comp, get_compnm(comp,2,2) compnm, pwd,        ";
              sql += "        post1, post2, addr, addr2, hometel, handphone, comptel, tel_line,                ";
              sql += "        lgcnt, lglast, lgip, jikwi, get_jikwinm(jikwi,comp) jikwinm, work_plcnm, deptnam ";
              sql += "   from TZ_MEMBER                                               ";
              sql += "  where userid = " + StringManager.makeSQL(v_userid);
*/
              sql = "SELECT                                                                    \n" +
                    "    userid,                                                               \n" + 
                    "    birth_date,                                                                \n" + 
                    "    name,                                                                 \n" + 
                    "    email,                                                                \n" + 
                    "    comp,                                                                 \n" + 
                    "    pwd,                                                                  \n" + 
                    "    post1,                                                                \n" + 
                    "    post2,                                                                \n" + 
                    "    address1,                                                             \n" + 
                    "    address2,                                                             \n" + 
                    "    hometel,                                                              \n" + 
                    "    handphone,                                                            \n" + 
                    "    comptel,                                                              \n" + 
                    "    tel_line,                                                             \n" + 
                    "    lgcnt,                                                                \n" + 
                    "    lglast,                                                               \n" + 
                    "    lgip                                                                  \n" + 
                    "FROM                                                                      \n" +
                    "    TZ_MEMBER                                                             \n" +
                    "WHERE                                                                     \n" +
                    "    userid = "+StringManager.makeSQL(v_userid)+"                          \n";
              
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

}
