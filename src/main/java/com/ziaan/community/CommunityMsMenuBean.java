// **********************************************************
// 1. 제      목:
// 2. 프로그램명: CommunityMsMenuBean.java
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

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityMsMenuBean { 
//    private ConfigSet config;
//    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      파일업로드되는 tag name
//    private static final int FILE_LIMIT = 1;                    //    페이지에 세팅된 파일첨부 갯수


    public CommunityMsMenuBean() { 
        try { 
//            config = new ConfigSet();
//            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
//            row = 10; // 강제로 지정
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

 /**
 메뉴명 중복체크 조회
 @param box     receive from the form object and session
 @return int    row count
 */     
    public int selectCmuNmRowCnt(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
//        ArrayList list = new ArrayList();
        ListSet   ls   = null;
        String    sql  = "";
        int       iRowCnt=0;
        try { 
            connMgr = new DBConnectionManager();
            sql = " select count(*) rowcnt"
                 + "   from tz_cmumenu "
                 + "  where cmuno = '" +box.getStringDefault("p_cmuno","") + "'"
                 + "    and brd_fg ='" +box.getStringDefault("p_brd_fg","") + "'"
                 + "    and title = " + SQLString.Format(box.getStringDefault("p_title",""))
                 ;
            ls = connMgr.executeQuery(sql);
            System.out.println(sql);
            while ( ls.next() ) { 
                  iRowCnt = ls.getInt("rowcnt");
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
        return iRowCnt;
    }

    /**
    * 커뮤니티 메뉴정보
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 메뉴정보
    * @throws Exception
    */
    public String getSingleColumn(RequestBox box,String v_cmuno,String v_menuno,String v_column) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
//        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        DataBox             dbox    = null;

        String              v_ret   = "";
        try { 
            connMgr = new DBConnectionManager();

            sql  = "\n select " +v_column
                  + "\n   from tz_cmumenu"
                  + "\n  where cmuno        = '" +v_cmuno     + "'" 
                  + "\n    and menuno       = '" +v_menuno + "'";
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_ret = ls.getString(1);
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
        return v_ret;
    }

    /**
    * 커뮤니티 메뉴테이블 칼럼정보 
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 메뉴테이블 칼럼정보 
    * @throws Exception
    */
    public String getSingleColumn1(String v_cmuno,String v_menuno,String v_column) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
//        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        DataBox             dbox    = null;

        String              v_ret   = "";
        try { 
            connMgr = new DBConnectionManager();

            sql  = "\n select " +v_column
                  + "\n   from tz_cmumenu"
                  + "\n  where cmuno        = '" +v_cmuno     + "'" 
                  + "\n    and menuno       = '" +v_menuno + "'";
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_ret = ls.getString(1);
            }
        }
        catch ( Exception ex ) { 
//               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return v_ret;
    }

    /**
    * 커뮤니티 메뉴리스트
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 메뉴리스트
    * @throws Exception
    */
    public ArrayList selectleftList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_brd_fg    = box.getStringDefault("p_brd_fg","2");

//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");

        try { 
            connMgr = new DBConnectionManager();

//            sql  = "\n select a.*,rownum rowseq from ("
              sql = "\n   select  cmuno, menuno, title, read_cd, write_cd, arrange, fileadd_fg,filecnt "
                  + "\n               , directory_FG, directory_memo,brd_fg, root, parent, lv, position,limit_list"
                  + "\n               , register_userid, register_dte, modifier_userid    "
                  + "\n               , modifier_dte, del_fg "
                  + "\n           from tz_cmumenu "
                  + "\n           where cmuno        = '" +v_cmuno     + "'" 
                  + "\n             and brd_fg   = '" +v_brd_fg + "'"
                  + "\n             and del_fg   ='N'"
                  + "\n           order by root asc,position asc, lv asc";
//                 + "\n ) a";
                 
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

    /**
    * 커뮤니티 메뉴정보
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 메뉴리정보
    * @throws Exception
    */
    public ArrayList selectSingleMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_menuno        = box.getString("p_menuno");

//        String v_brd_fg    = box.getStringDefault("p_brd_fg","2");

//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");

        try { 
            connMgr = new DBConnectionManager();

            sql  = "\n         select  cmuno, menuno, title, read_cd, write_cd, arrange, fileadd_fg,filecnt "
                  + "\n               , directory_FG, directory_memo,brd_fg, root, parent, lv, position,limit_list"
                  + "\n               , register_userid, register_dte, modifier_userid    "
                  + "\n               , modifier_dte, del_fg "
                  + "\n           from tz_cmumenu "
                  + "\n           where cmuno    = '" +v_cmuno     + "'" 
                  + "\n             and menuno   = '" +v_menuno + "'"
                  + "\n             and del_fg   ='N'";
                 
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

    /**
    * 커뮤니티 메뉴리스트
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 메뉴리스트
    * @throws Exception
    */
    public ArrayList selectleftCbxList(String v_cmuno,String v_brd_fg) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

        try { 
            connMgr = new DBConnectionManager();

//            sql  = "\n select a.*,rownum rowseq from ( "
              sql = "\n   select  cmuno, menuno, title, read_cd,read_cd readcd,write_cd, arrange, fileadd_fg,filecnt "
                  + "\n               , directory_FG, directory_memo,brd_fg, root, parent, lv, position,limit_list"
                  + "\n               , register_userid, register_dte, modifier_userid    "
                  + "\n               , modifier_dte, del_fg "
                  + "\n           from tz_cmumenu "
                  + "\n           where cmuno    = '" +v_cmuno     + "'" 
                  + "\n             and brd_fg   = '" +v_brd_fg + "'"
                  + "\n             and del_fg   ='N'"
                  + "\n           order by root asc,position asc, lv asc";
//                 + "\n ) a";
              
           // System.out.println("=====커뮤니티 메뉴리스트========");
           // System.out.println(sql);
           // System.out.println("=======selectleftCbxList==============");
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }

    /**
    * 메뉴등록하기
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;
        int         v_menuno    = 0; 
//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_brd_fg        = box.getStringDefault("p_brd_fg","1");
        int    v_ins_position  = box.getInt("p_ins_position");

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");


         
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 


           sql  =" update tz_cmumenu set    position = position +1"
                + "  where cmuno          = ?"
                + "    and brd_fg          = ?"
                + "    and position       > ?"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, v_cmuno              );
           pstmt.setString(2, v_brd_fg   );
           pstmt.setInt   (3, v_ins_position       );
           pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }

           sql = "select nvl(max(menuno), 0) from tz_cmumenu where cmuno = '" +v_cmuno + "'";
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               v_menuno = ls.getInt(1) + 1;
           } 

           sql  =" insert into tz_cmumenu ( cmuno          ,menuno         ,title          ,read_cd        "
                + "                         ,write_cd       ,arrange        ,fileadd_fg     ,filecnt        "
                + "                         ,directory_fg   ,directory_memo ,brd_fg         ,root           ,parent         "
                + "                         ,lv             ,position       ,limit_list     ,register_userid,register_dte   "
                + "                         ,modifier_userid,modifier_dte   ,del_fg                         )"
                + "                 values  (?,?,?,?"
                + "                         ,?,?,?,?"
                + "                         ,?,?,?,?,?"
                + "                         ,?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                         ,?,to_char(sysdate,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , v_cmuno                            );// 커뮤니팁먼호
           pstmt.setInt   (2 , v_menuno                           );// 메뉴일련번호
           pstmt.setString(3 , box.getString("p_ins_title" )      );// 제목
           pstmt.setString(4 , box.getString("p_ins_read_cd" )    );// 읽기권한
           pstmt.setString(5 , box.getString("p_ins_write_cd" )   );// 쓰기권한
           pstmt.setString(6 , "title"     );// 정렬
           pstmt.setString(7 , "N"                                );// 파일첨부구분
           pstmt.setInt   (8 , 0                              );// 첨부파일갯수
           pstmt.setString(9 , box.getString("p_ins_directory_fg") );// 디렉토리구분
           pstmt.setString(10, box.getString("p_ins_directory_memo" ));
           pstmt.setString(11, box.getString("p_brd_fg" )         );// 자료실구분
           
           if ( box.getInt("p_lv" ) +1 == 1) { 
              pstmt.setInt   (12, v_menuno         );// 
              pstmt.setInt   (13, v_menuno        );// 
           } else { 
              pstmt.setInt   (12, box.getInt("p_root" )          );// 
              pstmt.setInt   (13, box.getInt("p_menuno" )        );// 

           }
           pstmt.setInt   (14, box.getInt("p_lv" ) +1          );// 
           pstmt.setInt   (15, box.getInt("p_position" ) +1          );// 
           pstmt.setString   (16, box.getString("p_ins_limit_list" ));//
           pstmt.setString(17, s_userid);// 게시자
           pstmt.setString(18, s_userid );// 수정자
           isOk1 = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
//           String sql1 = "select directory_memo from tz_cmumenu where cmuno = '" +v_cmuno + "' and menuno =" +v_menuno;
//           connMgr.setOracleCLOB(sql1, box.getString("p_ins_directory_memo" ));


//           String sql2 = "select limit_list from tz_cmumenu where cmuno = '" +v_cmuno + "' and menuno =" +v_menuno;
//           connMgr.setOracleCLOB(sql2, box.getString("p_ins_limit_list" ));


            if ( isOk1 > 0 ) { 
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
        return isOk1*isOk2;
    }



    /**
    * 메뉴수정하기
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        int    v_menuno         = box.getInt("p_menuno");
//        String v_brd_fg        = box.getStringDefault("p_brd_fg","1");
//        int    v_ins_position  = box.getInt("p_ins_position");

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");


         
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           sql  =" update tz_cmumenu  set write_cd     =?"
                + "                       ,read_cd  =?"
                + "                       ,arrange  =?"
                + "                       ,fileadd_fg  =?"
                + "                       ,filecnt  =?"
                + "                       ,modifier_userid  =?"
                + "                       ,modifier_dte=to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                       ,limit_list=?"
                + "                       ,title=?"                
                + "            where cmuno = ?"
                + "              and menuno=?";
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , box.getString("p_write_cd" )   );// 쓰기권한
           pstmt.setString(2 , box.getString("p_read_cd" )    );// 읽기권한
           pstmt.setString(3 , box.getString("p_arrange" )    );// 정렬
           pstmt.setString(4 , box.getStringDefault("p_fileadd_fg","N" )    );// 파일첨부구분
           pstmt.setString(5 , box.getStringDefault("p_filecnt","0" )    );// 첨부파일갯수
           pstmt.setString(6, s_userid );// 수정자
           pstmt.setString(7, box.getString("p_limit_list" ));
           pstmt.setString(8, box.getString("p_title" ));           
           pstmt.setString(9, v_cmuno                            );// 커뮤니팁먼호
           pstmt.setInt   (10, v_menuno                           );// 메뉴일련번호
           isOk1 = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
//           String sql2 = "select limit_list from tz_cmumenu where cmuno = '" +v_cmuno + "' and menuno =" +v_menuno;
//           connMgr.setOracleCLOB(sql2, box.getString("p_limit_list" ));


            if ( isOk1 > 0 ) { 
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
        return isOk1*isOk2;
    }


    /**
    * 메뉴삭제하기
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */

    public int deleteMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;
//        int         v_menuno    = 0; 
//        String v_static_cmuno  = box.getString("p_static_cmuno");
//        String v_cmuno         = box.getString("p_cmuno");
//        String v_brd_fg        = box.getStringDefault("p_brd_fg","1");
//        int    v_ins_position  = box.getInt("p_ins_position");

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");


         
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           sql  =" update tz_cmumenu  set del_fg     ='Y'"
                + "                       ,modifier_userid  =?"
                + "                       ,modifier_dte=to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "            where cmuno = ?"
                + "              and menuno=?";
                
           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1, s_userid );// 수정자
           pstmt.setString(2 , box.getString("p_cmuno")                            );// 커뮤니팁먼호
           pstmt.setInt   (3 , box.getInt("p_menuno")                           );// 메뉴일련번호
           isOk1 = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }

            if ( isOk1 > 0 ) { 
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
        return isOk1*isOk2;
    }

}
