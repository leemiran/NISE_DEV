// **********************************************************
// 1. 제      목:
// 2. 프로그램명: CommunityCreateBean.java
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
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityCreateBean { 
//    private ConfigSet config;
//    private int row;

    public CommunityCreateBean() { 
        try { 
//            config = new ConfigSet();
//            row = Integer.parseInt(config.getProperty("page.manage.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
//            row = 10; // 강제로 지정
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
 
 /**
 커뮤니티 분류 조회
 @param gubun        코드구분
 @return ArrayList   커뮤니티 분류리스트
 */     
    public ArrayList selectCodeType_L(String gubun) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList list = new ArrayList();
        ListSet   ls   = null;
        String    sql  = "";
        DataBox             dbox    = null;
        try { 
            connMgr = new DBConnectionManager();
            sql = " select gubun, levels, code, codenm, upper, parent, luserid, ldate"
                 + "   from tz_code "
                 + "  where gubun = " + SQLString.Format(gubun)
                 ;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                   dbox = ls.getDataBox();
                   list.add(dbox);
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
        return list;
    }

    /**
    * 커뮤니티 칼럼정보
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 메뉴정보
    * @throws Exception
    */
    public String getSingleColumn(String v_cmuno,String v_column) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
//        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        DataBox             dbox    = null;

        String              v_ret   = "";
        try { 
            connMgr = new DBConnectionManager();

            sql  = "\n select " +v_column
                  + "\n   from tz_cmubasemst"
                  + "\n  where cmuno        = '" +v_cmuno     + "'" ;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_ret = ls.getString(1);
            }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return v_ret;
    }

 /**
 커뮤니티명 중복체크 조회
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
                 + "   from tz_cmubasemst "
                 + "  where cmu_nm = " + SQLString.Format(box.getStringDefault("p_cmu_nm",""));
            
            ls = connMgr.executeQuery(sql);
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
    * 커뮤니티 만들기
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertBaseMst(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";
        String      createCmuno = "";
        int         isOk        = 0;
//        int         v_seq       = 0;

//        String v_adtitle   = box.getString("p_adtitle");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        String v_grcode = "";
        String thisYear= "";
//        String v_templetfile = "";

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 
           thisYear = FormatDate.getDate("yyyyMMdd").substring(0,4);
           createCmuno = thisYear + "000001";
           sql  = "select nvl(max(cmuno),'" +thisYear + "000000') from tz_cmubasemst where cmuno like  '" +thisYear + "%'";
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               createCmuno = String.valueOf( ls.getInt(1) + 1);
           } 

           sql  =" insert into tz_cmubasemst ( cmuno     , cmu_nm         , in_method_fg, search_fg   , data_passwd_fg"
             + "                           , display_fg  , type_l         , type_m      , intro       , img_path"
             + "                           , layout_fg   , html_skin_fg   , read_cnt    , member_cnt  , close_fg"
             + "                           , register_dte, register_userid"
             + "                           , modifier_dte, modifier_userid)"
             + "                   values  (?,?,?,?,?"
             + "                           ,?,?,?,?,?"
             + "                           ,?,?,0,1,'0'"
             + "                           ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),?"
             + "                           ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),?)"
             ;
           pstmt = connMgr.prepareStatement(sql);
           
           pstmt.setString(1, createCmuno                       );// 커뮤니티번호
           pstmt.setString(2, box.getString("p_cmu_nm")          );// 커뮤니티명
           pstmt.setString(3, box.getString("p_in_method_fg")   );// 회원가입방식
           pstmt.setString(4, box.getString("p_search_fg")      );// 자료실검색허용ㅇ
           pstmt.setString(5, box.getStringDefault("p_data_passwd_fg","N") );// 자료암호화구분
           pstmt.setString(6, box.getString("p_display_fg")     );// 커뮤니티공개구분
           pstmt.setString(7, box.getString("p_type_l")         );// 대분류
           pstmt.setString(8, box.getString("p_type_m")         );// 중분류
           pstmt.setString(9, v_intro         );
           pstmt.setString(10, box.getNewFileName("p_img_path")  );// 이미지
           pstmt.setString(11, box.getString("p_layout_fg")     );// 레이아웃
           pstmt.setString(12, box.getString("p_html_skin_fg")  );// 화면스킨
           pstmt.setString(13, s_userid                         );// 등록자
           pstmt.setString(14, s_userid                         );// 수정자
           isOk = pstmt.executeUpdate();
           
//           if ( pstmt != null ) { pstmt.close(); }

//           sql1 = "select intro from tz_cmubasemst where cmuno = '" + createCmuno + "'";
//           connMgr.setOracleCLOB(sql1, v_intro);       //      (기타 서버 경우)

           // 커뮤니티 등급코드생성
           sql  =" insert into tz_cmugrdcode (  cmuno           , grcode       , kor_nm          ,eng_nm        , descript "
                + "                            ,register_userid , register_dte , modifier_userid , modifier_dte , del_fg)"
                + "                   values  (?,?,?,'',''"
                + "                           ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')";
           
           pstmt = connMgr.prepareStatement(sql);

           sql1  = " select code,codenm from tz_code where gubun ='0053' order by code asc";
           ls = connMgr.executeQuery(sql1);
           
//           if ( pstmt != null ) { pstmt.close(); }
           while ( ls.next() ) { 
                v_grcode=ls.getString("code") ;
                pstmt.setString(1, createCmuno            );
                pstmt.setString(2, ls.getString("code")   );
                pstmt.setString(3, ls.getString("codenm") );
                pstmt.setString(4, s_userid               );
                pstmt.setString(5, s_userid               );
                pstmt.executeUpdate();
           }   
//           if ( pstmt != null ) { pstmt.close(); }
           ls.close();

     // 회원등록

           sql  =" insert into tz_cmuusermst (cmuno        , userid      , kor_name       , eng_name      , email \n"
                + "                          , tel         , mobile      , duty           , wk_area       , grade \n"
                + "                          , request_dte , license_dte , license_userid , close_fg      , close_reason \n"
                + "                          , close_dte   , intro       , recent_dte     , visit_num     , search_num \n"
                + "                          , register_num, modifier_dte)"
                + "             values (       ?           , ?           , ?          , ''             , ?  \n"
                + "                          , ?           , ?           ,''          , ''             ,'01'  \n"
                + "                          ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),to_char(sysdate,'YYYYMMDDHH24MISS'),?,'1' ,'' \n"
                + "                          ,''           ,''           ,''          ,0               ,0   \n"
                + "                          ,0            ,to_char(sysdate,'YYYYMMDDHH24MISS'))";

           pstmt = connMgr.prepareStatement(sql);
           //sql1  = " select userid,name,email,hometel,handphone,comptel,work_plcnm from tz_member where userid ='" +s_userid + "'";
           //sql1  = " select userid,name,email,'' hometel,handphone,comptel,'' work_plcnm from tz_member where userid ='" +s_userid + "'";
           sql1  = " select userid, name, email, hometel, handphone from tz_member where userid = " + SQLString.Format(s_userid);
           ls = connMgr.executeQuery(sql1);
           
//           if ( pstmt != null ) { pstmt.close(); }
           
           while ( ls.next() ) { 
                pstmt.setString(1, createCmuno );
                pstmt.setString(2, ls.getString("userid")   );
                pstmt.setString(3, ls.getString("name") );
                pstmt.setString(4, ls.getString("email") );
                pstmt.setString(5, ls.getString("hometel") );
                pstmt.setString(6, ls.getString("handphone") );
                pstmt.setString(7, s_userid );
                //pstmt.setString(7, ls.getString("comptel") );
                //pstmt.setString(8, ls.getString("work_plcnm") );
                pstmt.executeUpdate();
           }   
           ls.close();

           // 공지사항 게시판메뉴생성
           sql  =" insert into tz_cmumenu ( cmuno          ,menuno         ,title          ,read_cd        "
                + "                         ,write_cd       ,arrange        ,fileadd_fg     ,filecnt        "
                + "                         ,directory_fg   ,brd_fg         ,root           ,parent         " //directory_memo ,
                + "                         ,lv             ,position       ,register_userid,register_dte   " //limit_list     ,
                + "                         ,modifier_userid,modifier_dte   ,del_fg                         )"
                + "                 values  (?,?,?,?"
                + "                         ,?,?,?,?"
                + "                         ,?,?,?,?"//empty_clob(),
                + "                         ,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')" //empty_clob(),
                + "                         ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , createCmuno      );// 커뮤니팁먼호
           pstmt.setInt   (2 , 1            );// 메뉴일련번호
           pstmt.setString(3 , "공지사항"     );// 제목
           pstmt.setString(4 , v_grcode     );// 읽기권한
           pstmt.setString(5 , "02"          );// 쓰기권한 (grcode 였지만 부시샵까지만 작성가능하게 바꿈)
           pstmt.setString(6 , "register_dte"      );// 정렬
           pstmt.setString(7 , "Y"          );// 파일첨부구분
           pstmt.setInt   (8 , 5            );// 첨부파일갯수
           pstmt.setString(9 , "file"       );// 디렉토리구분
           pstmt.setString(10,"0"           );// 자료실구분
           pstmt.setInt   (11, 1            );// Root
           pstmt.setInt   (12, 1            );// Parent
           pstmt.setInt   (13, 1            );// 
           pstmt.setInt   (14, 1            );// 
           pstmt.setString(15, s_userid     );// 게시자
           pstmt.setString(16, s_userid     );// 수정자
           pstmt.executeUpdate();
//           if ( pstmt != null ) { pstmt.close(); }

           // 앨범게시판생성
           sql  =" insert into tz_cmumenu (  cmuno          ,menuno         ,title          ,read_cd        "
                + "                         ,write_cd       ,arrange        ,fileadd_fg     ,filecnt        "
                + "                         ,directory_fg   ,brd_fg         ,root           ,parent         " //directory_memo ,
                + "                         ,lv             ,position       ,register_userid,register_dte   " //limit_list     ,
                + "                         ,modifier_userid,modifier_dte   ,del_fg                         )"
                + "                 values  (?,?,?,?"
                + "                         ,?,?,?,?"
                + "                         ,?,?,?,?" //empty_clob(),
                + "                         ,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')" //empty_clob(),
                + "                         ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , createCmuno      );// 커뮤니팁먼호
           pstmt.setInt   (2 , 2            );// 메뉴일련번호
           pstmt.setString(3 , "앨범"     );// 제목
           pstmt.setString(4 , v_grcode     );// 읽기권한
           pstmt.setString(5 , v_grcode     );// 쓰기권한
           pstmt.setString(6 , "register_dte"      );// 정렬
           pstmt.setString(7 , "Y"          );// 파일첨부구분
           pstmt.setInt   (8 , 1            );// 첨부파일갯수
           pstmt.setString(9 , "file"       );// 디렉토리구분
           pstmt.setString(10,"3"           );// 자료실구분
           pstmt.setInt   (11, 2            );// Root
           pstmt.setInt   (12, 2            );// Parent
           pstmt.setInt   (13, 1            );// 
           pstmt.setInt   (14, 2            );// 
           pstmt.setString(15, s_userid     );// 게시자
           pstmt.setString(16, s_userid     );// 수정자
           pstmt.executeUpdate();
//           if ( pstmt != null ) { pstmt.close(); }
           // 게시판생성
           sql  =" insert into tz_cmumenu (  cmuno          ,menuno         ,title          ,read_cd        "
                + "                         ,write_cd       ,arrange        ,fileadd_fg     ,filecnt        "
                + "                         ,directory_fg   ,brd_fg         ,root           ,parent         " //directory_memo ,
                + "                         ,lv             ,position       ,register_userid,register_dte   " //limit_list     ,
                + "                         ,modifier_userid,modifier_dte   ,del_fg                         )"
                + "                 values  (?,?,?,?"
                + "                         ,?,?,?,?"
                + "                         ,?,?,?,?" //empty_clob(),
                + "                         ,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')" //empty_clob(),
                + "                         ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , createCmuno      );// 커뮤니팁먼호
           pstmt.setInt   (2 , 3            );// 메뉴일련번호
           pstmt.setString(3 , "게시판"     );// 제목
           pstmt.setString(4 , v_grcode     );// 읽기권한
           pstmt.setString(5 , v_grcode     );// 쓰기권한
           pstmt.setString(6 , "register_dte"      );// 정렬
           pstmt.setString(7 , "Y"          );// 파일첨부구분
           pstmt.setInt   (8 , 1            );// 첨부파일갯수
           pstmt.setString(9 , "file"       );// 디렉토리구분
           pstmt.setString(10,"2"           );// 자료실구분
           pstmt.setInt   (11, 3            );// Root
           pstmt.setInt   (12, 3            );// Parent
           pstmt.setInt   (13, 1            );// 
           pstmt.setInt   (14, 1            );// 
           pstmt.setString(15, s_userid     );// 게시자
           pstmt.setString(16, s_userid     );// 수정자
           pstmt.executeUpdate();           
//           if ( pstmt != null ) { pstmt.close(); }
           // 자료실생성
           sql  =" insert into tz_cmumenu (  cmuno          ,menuno         ,title          ,read_cd        "
                + "                         ,write_cd       ,arrange        ,fileadd_fg     ,filecnt        "
                + "                         ,directory_fg   ,brd_fg         ,root           ,parent         " //directory_memo ,
                + "                         ,lv             ,position       ,register_userid,register_dte   " //limit_list     ,
                + "                         ,modifier_userid,modifier_dte   ,del_fg                         )"
                + "                 values  (?,?,?,?"
                + "                         ,?,?,?,?"
                + "                         ,?,?,?,?" //empty_clob(),
                + "                         ,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')" //empty_clob(),
                + "                         ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , createCmuno      );// 커뮤니팁먼호
           pstmt.setInt   (2 , 4            );// 메뉴일련번호
           pstmt.setString(3 , "자료실"     );// 제목
           pstmt.setString(4 , v_grcode     );// 읽기권한
           pstmt.setString(5 , v_grcode     );// 쓰기권한
           pstmt.setString(6 , "register_dte"      );// 정렬
           pstmt.setString(7 , "Y"          );// 파일첨부구분
           pstmt.setInt   (8 , 1            );// 첨부파일갯수
           pstmt.setString(9 , "file"       );// 디렉토리구분
           pstmt.setString(10,"1"           );// 자료실구분
           pstmt.setInt   (11, 4            );// Root
           pstmt.setInt   (12, 4            );// Parent
           pstmt.setInt   (13, 1            );// 
           pstmt.setInt   (14, 1            );// 
           pstmt.setString(15, s_userid     );// 게시자
           pstmt.setString(16, s_userid     );// 수정자
           pstmt.executeUpdate();                  
//           if ( pstmt != null ) { pstmt.close(); }
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

    /**
    * 커뮤니티 정보변경
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateBaseMst(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
        int         isOk        = 0;
//        int         v_seq       = 0;

        String v_cmuno     = box.getString("p_cmuno");
//        String v_adtitle   = box.getString("p_adtitle");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

//        String thisYear= "";
//        String v_templetfile = "";

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 


           sql  =" update tz_cmubasemst set  cmu_nm             =?   "
                + "                         , in_method_fg       =?   "
                + "                         , search_fg          =?   "
                + "                         , data_passwd_fg     =?   "
                + "                         , display_fg         =?   "
                + "                         , type_l             =?   "  
                + "                         , type_m             =?   "
                + "                         , intro              =?   "
                + "                         , img_path           =?   "
                + "                         , layout_fg          =?   "
                + "                         , html_skin_fg       =?   "
                + "                         , modifier_dte       =to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                         , modifier_userid    =?   "
                + "                where cmuno = ?"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, box.getString("p_cmu_nm")          );// 커뮤니티명
           pstmt.setString(2, box.getString("p_in_method_fg")   );// 회원가입방식
           pstmt.setString(3, box.getString("p_search_fg")      );// 자료실검색허용ㅇ
           pstmt.setString(4, box.getStringDefault("p_data_passwd_fg","N") );// 자료암호화구분
           pstmt.setString(5, box.getString("p_display_fg")     );// 커뮤니티공개구분
           pstmt.setString(6, box.getString("p_type_l")         );// 대분류
           pstmt.setString(7, box.getString("p_type_m")         );// 중분류
           pstmt.setString(8, v_intro         );
           pstmt.setString(9, box.getNewFileName("p_img_path")  );// 이미지
           pstmt.setString(10, box.getString("p_layout_fg")     );// 레이아웃
           pstmt.setString(11, box.getString("p_html_skin_fg")  );// 화면스킨
           pstmt.setString(12, s_userid                         );// 수정자
           pstmt.setString(13, v_cmuno                          );// 커뮤니티번호
           isOk = pstmt.executeUpdate();
//           if ( pstmt != null ) { pstmt.close(); }
           // 커뮤니티 설명 저장 
//           sql1 = "select intro from tz_cmubasemst where cmuno = '" + v_cmuno + "'";
//           connMgr.setOracleCLOB(sql1, v_intro);  

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

    /**
    * 기본정보 조회
    * @param box          receive from the form object and session
    * @return ArrayList   기본정보 데이터
    * @throws Exception
    */
    public ArrayList selectBaseMst(RequestBox box) throws Exception { 
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
  
            sql  =  " select a.cmuno cmuno, a.cmu_nm cmu_nm, a.in_method_fg in_method_fg, a.search_fg search_fg, a.data_passwd_fg data_passwd_fg, a.display_fg display_fg, a.type_l type_l "
                  + "       , a.type_m, a.intro intro, a.img_path img_path, a.layout_fg layout_fg, a.html_skin_fg html_skin_fg, a.read_cnt read_cnt, a.member_cnt member_cnt, a.close_fg close_fg"
                  + "       , a.close_reason close_reason, a.close_dte close_dte, a.close_userid close_userid, a.hold_fg hold_fg, a.accept_dte accept_dte, a.accept_userid accept_userid, a.register_dte register_dte"
                  + "       , a.register_userid  register_userid, a.modifier_dte modifier_dte, a.modifier_userid modifier_userid"
                  + "       , nvl(d.savefile,'')        hongbo_savefile , nvl(d.contents,'')      hongbo_contents"
                  + "   from  tz_cmubasemst a"
                  + "        ,tz_cmuhongbo d"
                  + "  where a.cmuno        = '" +v_cmuno + "'"
                  + "    and a.cmuno  = d.cmuno(+) "
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

 /**
  * 선택된 자료파일 DB에서 삭제
  * @param connMgr   DB Connection Manager
  * @param box    receive from the form object and session
  * @return
  * @throws Exception
  */
 public int deleteSingleFile( RequestBox box) throws Exception { 
   DBConnectionManager connMgr     = null;
   PreparedStatement   pstmt   = null;
   ListSet             ls      = null;
   String              sql     = "";
//   String              sql2    = "";
   int                 isOk2   = 1;



//  String s_userid = box.getSession("userid");
  String v_cmuno  = box.getString("p_cmuno");
  try { 
       connMgr = new DBConnectionManager();
       connMgr.setAutoCommit(false); 

       sql  =" update tz_cmubasemst set img_path='' "
           + "  where cmuno        = ?"
       ;
       pstmt = connMgr.prepareStatement(sql);
       pstmt.setString(1, v_cmuno       );// 커뮤니티번호
       isOk2 = pstmt.executeUpdate();
//       if ( pstmt != null ) { pstmt.close(); }
       if ( isOk2 > 0 ) { 
          if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
       }
  }
  catch ( Exception ex ) { 
      ErrorManager.getErrorStackTrace(ex, box, sql);
   throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
  }
  finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
  }
  return isOk2;
 }
 
    /**
    * 커뮤니티 조회수 변경
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateCmuHit(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
        int         isOk        = 0;
//        int         v_seq       = 0;

        String v_cmuno     = box.getString("p_cmuno");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 


           sql  ="	update tz_cmubasemst  \n" +
           	 "	set total_cnt = nvl(total_cnt,0)+1  \n" +
           	 "	   ,today_cnt = (case when substr(nvl(hit_dte,to_char(sysdate,'YYYYMMDDHH24MISS')),0,8) = to_char(sysdate,'YYYYMMDD') then nvl(today_cnt,0)+1 else 1 end)  \n" +
           	 "	   ,hit_dte = to_char(sysdate,'YYYYMMDDHH24MISS')  \n" +
           	 "	where cmuno = ? ";
           	 
           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1, v_cmuno);// 커뮤니티번호                
           isOk = pstmt.executeUpdate();
//           if ( pstmt != null ) { pstmt.close(); }
           // 커뮤니티 설명 저장 
           /*
           sql1 = "select intro from tz_cmubasemst where cmuno = '" + v_cmuno + "'";
           connMgr.setOracleCLOB(sql1, v_intro);  
           */

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