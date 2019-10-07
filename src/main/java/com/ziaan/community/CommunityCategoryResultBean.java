// **********************************************************
// 1. 제      목:
// 2. 프로그램명: CommunityCategoryResultBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-29
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.community;

//import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityCategoryResultBean { 
    private ConfigSet config;
    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      파일업로드되는 tag name
//    private static final int FILE_LIMIT = 1;                    //    페이지에 세팅된 파일첨부 갯수


    public CommunityCategoryResultBean() { 
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
    * 커뮤니티 조회리스트
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 조회리스트
    * @throws Exception
    */
    public ArrayList selectCateGoryList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");

        String v_searchtext    = box.getString("p_searchtext");
        String v_select        = box.getString("p_select");
        String v_orderby       = box.getString("p_orderby");
        String v_type_m        = box.getString("p_type_m");
        String v_loc_fg        = box.getStringDefault("p_loc_fg","1");
        int    v_pageno        = box.getInt("p_pageno");
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");
        try { 
            connMgr = new DBConnectionManager();

            if ( !"2".equals(v_loc_fg)) { // 홍보가아닌경우
//               sql  = " select a.*,rownum rowseq  from ("
                     sql= "   select a.cmuno           cmuno           , a.cmu_nm        cmu_nm      , a.in_method_fg     in_method_fg   , a.search_fg search_fg"
                     + "              , a.data_passwd_fg  data_passwd_fg  , a.display_fg    display_fg  , a.type_l           type_l         ,a.type_m type_m"
                     + "             , substr(a.intro,1,100)||'...'           intro           , a.img_path      img_path    , a.layout_fg        layout_fg      , a.html_skin_fg html_skin_fg"
                     + "              , a.read_cnt        read_cnt        , a.member_cnt    member_cnt  , a.close_fg         close_fg"
                     + "             , a.close_reason    close_reason    , a.close_dte     close_dte   , a.close_userid     close_userid"
                     + "              , a.hold_fg         hold_fg         , a.accept_dte    accept_dte  , a.accept_userid    accept_userid  , a.register_dte  register_dte"
                     + "             , a.register_userid register_userid , a.modifier_dte  modifier_dte, a.modifier_userid  modifier_userid "
                     + "              , b.kor_name        kor_name        , c.codenm      grade_nm"
                     + "              , nvl(d.savefile,'')        hongbo_savefile , nvl(d.contents,'')      hongbo_contents"
                     + "    from tz_cmubasemst a "
                     + "        ,(select cmuno,userid,kor_name,grade from tz_cmuusermst where grade='01' and close_fg='1') b"
                     + "        ,(select cmuno cmuno,grcode grcode,kor_nm codenm from tz_cmugrdcode) c"
                     + "        ,tz_cmuhongbo d"
                     + "  where a.cmuno  = b.cmuno"
                     + "    and b.cmuno  = c.cmuno"
                     + "    and b.grade  = c.grcode"
                     + "    and b.cmuno  = d.cmuno(+) "
                     + "    and a.close_fg  = '1'";
            } else { 
//               sql  = " select a.*,rownum rowseq from ("
                     sql= "   select a.cmuno           cmuno           , a.cmu_nm        cmu_nm      , a.in_method_fg     in_method_fg   , a.search_fg search_fg"
                     + "              , a.data_passwd_fg  data_passwd_fg  , a.display_fg    display_fg  , a.type_l           type_l         ,a.type_m type_m"
                     + "             , substr(a.intro,1,100)||'...'           intro           , a.img_path      img_path    , a.layout_fg        layout_fg      , a.html_skin_fg html_skin_fg"
                     + "              , a.read_cnt        read_cnt        , a.member_cnt    member_cnt  , a.close_fg         close_fg"
                     + "             , a.close_reason    close_reason    , a.close_dte     close_dte   , a.close_userid     close_userid"
                     + "              , a.hold_fg         hold_fg         , a.accept_dte    accept_dte  , a.accept_userid    accept_userid  , a.register_dte  register_dte"
                     + "             , a.register_userid register_userid , a.modifier_dte  modifier_dte, a.modifier_userid  modifier_userid "
                     + "              , b.kor_name        kor_name        , c.codenm      grade_nm"
                     + "              , nvl(d.savefile,'')        hongbo_savefile , nvl(d.contents,'')      hongbo_contents"
                     + "    from tz_cmubasemst a "
                     + "        ,(select cmuno,userid,kor_name,grade from tz_cmuusermst where grade='01' and close_fg='1') b"
                     + "        ,(select cmuno cmuno,grcode grcode,kor_nm codenm from tz_cmugrdcode) c"
                     + "        ,tz_cmuhongbo d"
                     + "  where a.cmuno  = b.cmuno"
                     + "    and b.cmuno  = c.cmuno"
                     + "    and b.cmuno  = d.cmuno(+) "
                     + "    and b.grade  = c.grcode"
                     + "    and a.close_fg  = '1'";
            }
            if ( v_type_m.length() > 0) { 
               sql += "    and a.type_m  = '" +v_type_m + "'";
               if ( !v_searchtext.equals("") ) {      // 검색어가 있으면
                    if ( v_select.equals("cmu_nm"))      sql += " and lower(a.cmu_nm)    like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
//                    if ( v_select.equals("intro"))      sql += " and lower(a.intro)    like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";  
                    if ( v_select.equals("kor_name"))   sql += " and lower(b.kor_name) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
               }
            } else { 
               if ( !v_searchtext.equals("") ) {      // 검색어가 있으면
                    if ( v_select.equals("cmu_nm"))      sql += " and lower(a.cmu_nm)    like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    if ( v_select.equals("intro"))      sql += " and lower(a.intro)    like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";  
                    if ( v_select.equals("kor_name"))   sql += " and lower(b.kor_name) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
               }
            }
            if ( "4".equals(v_loc_fg)) { // 인기커뮤니티
               sql += "    and a.hold_fg  = 1";
            }
            
            if ( v_orderby.equals("cmu_nm"))    sql += " order by a.cmu_nm asc";
            if ( v_orderby.equals("kor_name"))  sql += " order by b.kor_name asc";
            if ( v_orderby.equals("accept_dte"))sql += " order by a.accept_dte asc";
//            if ( v_orderby.equals("intro"))     sql += " order by a.intro asc";

//            sql += " ) a";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);                         // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                 // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();    // 전체 row 수를 반환한다
System.out.println(sql);
System.out.println("정렬순서 = "+v_orderby);
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
    * 홍보정보 조회
    * @param box          receive from the form object and session
    * @return ArrayList   홍보 데이터
    * @throws Exception
    */
    public ArrayList selectHongbo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
//        PreparedStatement   pstmt = null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;
//        int                 isOK    =1;

//        String  v_static_cmuno = box.getString("p_static_cmuno"); 
        String  v_cmuno = box.getString("p_cmuno"); 
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");

 

 
        try { 
            connMgr = new DBConnectionManager();
                 sql  = " select a.cmuno           cmuno           , a.cmu_nm        cmu_nm      , a.in_method_fg     in_method_fg   , a.search_fg search_fg"
                     + "              , a.data_passwd_fg  data_passwd_fg  , a.display_fg    display_fg  , a.type_l           type_l         ,a.type_m type_m"
                     + "             , a.intro           intro           , a.img_path      img_path    , a.layout_fg        layout_fg      , a.html_skin_fg html_skin_fg"
                     + "              , a.read_cnt        read_cnt        , a.member_cnt    member_cnt  , a.close_fg         close_fg"
                     + "             , a.close_reason    close_reason    , a.close_dte     close_dte   , a.close_userid     close_userid"
                     + "              , a.hold_fg         hold_fg         , a.accept_dte    accept_dte  , a.accept_userid    accept_userid  , a.register_dte  register_dte"
                     + "             , a.register_userid register_userid , a.modifier_dte  modifier_dte, a.modifier_userid  modifier_userid "
                     + "              , b.kor_name        kor_name        , c.codenm      grade_nm"
                     + "              , nvl(d.savefile,'')        hongbo_savefile , nvl(d.contents,'')      hongbo_contents"
                     + "    from tz_cmubasemst a "
                     + "        ,(select cmuno,userid,kor_name,grade from tz_cmuusermst where grade='01' and close_fg='1') b"
                     + "        ,(select cmuno cmuno,grcode grcode,kor_nm codenm from tz_cmugrdcode) c"
                     + "        ,tz_cmuhongbo d"
                     + "  where a.cmuno  = b.cmuno"
                     + "    and b.cmuno  = c.cmuno"
                     + "    and b.cmuno  = d.cmuno(+) "
                     + "    and b.grade  = c.grcode"
                     + "    and a.close_fg  = '1'"
                     + "  and a.cmuno        = '" +v_cmuno + "'"
                  ;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
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
}