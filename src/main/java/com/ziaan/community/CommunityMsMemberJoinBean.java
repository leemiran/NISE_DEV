// **********************************************************
// 1. 제      목: 커뮤니티 회원가입
// 2. 프로그램명: CommunityMsMemberJoinBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-29
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.community;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityMsMemberJoinBean { 
    private ConfigSet config;
    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      파일업로드되는 tag name
//    private static final int FILE_LIMIT = 1;                    //    페이지에 세팅된 파일첨부 갯수


    public  CommunityMsMemberJoinBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
            row = 10; // 강제로 지정
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    * 커뮤니티 회원조회
    * @param box          receive from the form object and session
    *        close_fg     해지구분
    * @return ArrayList   커뮤니티 회원리스트
    * @throws Exception
    */
    public ArrayList selectMemberList(RequestBox box,String v_close_fg) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_searchtext    = box.getString("p_searchtext");
        String v_select        = box.getString("p_select");
        int    v_pageno        = box.getInt("p_pageno");

        String v_gubun         = box.getString("gubun");
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");

        try { 
            connMgr = new DBConnectionManager();

//            sql  = "\n select a.*,rownum rowseq from ("
              sql = "\n  select     a.cmuno           cmuno         , a.userid          userid        "
                  + "\n           , a.kor_name        kor_name      , a.eng_name        eng_name      "
                  + "\n           , a.email           email         , a.tel             tel           "
                  + "\n           , a.mobile          mobile        , a.office_tel      office_tel    "
                  + "\n           , a.duty            duty          , a.wk_area         wk_area       "
                  + "\n           , a.grade           grade         , a.request_dte     request_dte   "
                  + "\n           , a.license_dte     license_dte   , a.license_userid  license_userid"
                  + "\n           , a.close_fg        close_fg      , a.close_reason    close_reason  "
                  + "\n           , a.close_dte       close_dte     , a.intro           intro         "
                  + "\n           , a.recent_dte      recent_dte    , a.visit_num       visit_num     "
                  + "\n           , a.search_num      search_num    , a.register_num    register_num  "
                  + "\n           , a.modifier_dte    modifier_dte  , c.kor_nm          grade_kor_nm"
                  //+ "\n           ,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                  + "\n    from tz_cmuusermst a,tz_member b,tz_cmugrdcode c "
                  + "\n   where a.userid = b.userid "  
                  + "\n     and a.cmuno  = c.cmuno"
                  + "\n     and a.grade  = c.grcode"
                  + "\n     and a.cmuno  = '" +v_static_cmuno + "'"; 
                  if(!"0".equals(v_gubun)) {
                	  sql+= "\n     and a.close_fg = '" +v_close_fg + "'";
                  } else {
                	  sql+= "\n     and a.close_fg != " + StringManager.makeSQL(v_gubun);
                  }
                  sql+= "\n     and a.grade     != '01'";
            if ( !v_searchtext.equals("") ) {      // 검색어가 있으면
                 if ( v_select.equals("userid"))   sql += "\n  and lower(a.userid) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("kor_name")) sql += "\n  and a.kor_name like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i 용
            }
            sql += "\n   order by a.REQUEST_DTE desc ";
//          sql += "\n  order by a.kor_name asc";
//                 + "\n  ) a";
                 
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);                         // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                 // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();    // 전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum"  , new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount" , new Integer(row));
                list.add(dbox);
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



    /**
    * 커뮤니티 회원 가입 승인 및 거부
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateCmuUserCloseFg(RequestBox box) throws Exception { 
    	DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet   ls          = null;
        String    sql         = "";
        String    sql1        = "";
        int       isOk        = 0;
        int       v_seq       = 0;

        String    v_cmuno         = box.getString("p_static_cmuno");
        String    v_close_fg      = box.getString("p_close_fg");
        Vector    v_list_userid   = box.getVector("p_list_userid");
        String    v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");
        String    s_userid        = box.getSession("userid");
        String    s_name          = box.getSession("name");


        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 


             for ( int i = 0;i<v_list_userid.size();i++ ) { 
                 if ( "1".equals(v_close_fg)) { 
                   System.out.println(".(String)v_list_userid.elementAt(i):" +(String)v_list_userid.elementAt(i));
                   sql  =" update tz_cmuusermst set  close_fg           =?   "
                        + "                          ,license_dte    =to_char(sysdate, 'YYYYMMDDHH24MISS')"
                        + "                          ,license_userid =?"
                        + "                     where cmuno = ?"
                        + "                       and userid =?";
                   pstmt = connMgr.prepareStatement(sql);
                   pstmt.setString(1, v_close_fg         );
                   pstmt.setString(2, s_userid         );
                   pstmt.setString(3, v_cmuno         );
                   pstmt.setString(4, (String)v_list_userid.elementAt(i));
                   isOk = pstmt.executeUpdate();
                   if ( pstmt != null ) { pstmt.close(); }
                 } else if ( "2".equals(v_close_fg)) { 
                   sql = "delete from tz_cmuusermst where cmuno = ?"
                        + "                       and userid =?";
                   pstmt = connMgr.prepareStatement(sql);
                   pstmt.setString(1, v_cmuno         );
                   pstmt.setString(2, (String)v_list_userid.elementAt(i));
                   isOk = pstmt.executeUpdate();
                   if ( pstmt != null ) { pstmt.close(); }
                 }            
             }
            if ( isOk > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }

}
