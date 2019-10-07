// **********************************************************
// 1. 제      목:
// 2. 프로그램명: CommunityAdminPoliceBean.java
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
public class CommunityAdminPoliceBean { 
    private ConfigSet config;
    private int row;

    public CommunityAdminPoliceBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.manage.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
            row = 10; // 강제로 지정
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
 
    /**
    * 불량 커뮤니티 답변
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updatePolice(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";

        int         isOk        = 0;

        String v_policeno  = box.getString("p_policeno");
        //String v_repmemo   = StringManager.replace(box.getString("content"),"<br > ","\n");
        String v_repmemo   = box.getString("p_content");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*           
           ConfigSet conf = new ConfigSet();
           SmeNamoMime namo = new SmeNamoMime(v_repmemo); // 객체생성 
           boolean result = namo.parse(); // 실제 파싱 수행 

           if ( !result ) { // 파싱 실패시 
               return 0;
           }
           if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
               String v_server = conf.getProperty("autoever.url.value");
               String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
               String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
               String prefix = "Community" + v_policeno;         // 파일명 접두어
               result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
           }
           if ( !result ) { // 파일저장 실패시 
               return 0;
           }
           v_repmemo = namo.getContent(); // 최종 컨텐트 얻기
*/           

           sql  =" update tz_cmupolice set  str_fg=2"
                + "                         ,str_dte=to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                         ,repmemo=?"
                + "                         ,str_userid=?"
                + "  where policeno =?";
           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1, v_repmemo);
           pstmt.setString   (2, s_userid                       );
           pstmt.setString   (3, v_policeno                       );
           isOk = pstmt.executeUpdate();

//           sql1 = "select repmemo from tz_cmupolice where policeno = '" + v_policeno + "'";
//           connMgr.setOracleCLOB(sql1, v_repmemo);       //      (기타 서버 경우)


               // 일련번호 구하기
               int v_mailno=0;
               sql1 = "select nvl(max(MAILNO), 0)   from TZ_CMUMAIL ";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) v_mailno = ls.getInt(1);


               sql  =" insert into TZ_CMUMAIL ( mailno, userid, kor_nm, recv_email"
                    + "                       ,cmuno, cmu_nm, SEND_USERID,send_email, title, content"
                    + "                       ,loc_fg,loc_nm,regster_dte, send_fg)"
                    + "               values  (?,?,?,?"
                    + "                       ,?,?,?,?,?,?"
                    + "                       ,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),'N')"
                    ;
               pstmt = connMgr.prepareStatement(sql);


               // 발신자 이메일
               String v_tmp_send_email= "";
               sql1 = "select email   from tz_member where userid = '" +s_userid + "' ";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) v_tmp_send_email = ls.getString(1);


               sql1 = "select a.policeno, a.cmuno  , a.cmu_nm   , a.userid,b.name,b.email "
                     + "  from tz_cmupolice a,tz_member b "
                     + " where a.userid   = b.userid "
                     + "   and a.policeno ='" +v_policeno + "'";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) { 
                     v_mailno =v_mailno +1;
                     pstmt.setInt   (1, v_mailno                                );// 일련번호
                     pstmt.setString(2, ls.getString(4)                         );// 수신자아이디
                     pstmt.setString(3, ls.getString(5)                         );// 수신자명
                     pstmt.setString(4, ls.getString(6)                         );// 수신자이메일
                     pstmt.setString(5, ls.getString(2)                         );// 커뮤니티먼호
                     pstmt.setString(6, ls.getString(3)                            );// 커뮤니티명
                     pstmt.setString(7 ,s_userid                                );// 발신자아이디
                     pstmt.setString(8 ,v_tmp_send_email                        );// 발신자이메일
                     pstmt.setString(9 , ""                                     );// 제목
                     pstmt.setString(10, v_repmemo);
                     pstmt.setString(11, "6"                                    );// 구분
                     pstmt.setString(12, "불건전커뮤니티신고"                     );// 구분명
                     isOk = pstmt.executeUpdate();
                     
                     if ( pstmt != null ) { pstmt.close(); }
                     
//                     sql1 = "select content  from TZ_CMUMAIL where mailno = '" +v_mailno + "'";
//                     connMgr.setOracleCLOB(sql1, v_repmemo);
                     if ( isOk > 0 ) { 
                         if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
                     }
               }
               if ( pstmt != null ) { pstmt.close(); }

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
    * 리스트
    * @param box          receive from the form object and session
    * @return ArrayList   리스트
    * @throws Exception
    */
    public ArrayList selectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;

        String v_str_fg  = box.getStringDefault("p_str_fg","1");
//        String s_userid             = box.getSession("userid");
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            sql  ="\n  select    a.policeno  policeno , a.cmuno   cmuno , a.cmu_nm    cmu_nm  "
                 + "\n           ,a.userid    userid   , a.email   email , a.content   content "
                 + "\n           ,a.singo_dte singo_dte, a.str_fg  str_fg, a.repmemo   repmemo "
                 + "\n           ,a.str_dte   str_dte  , a.str_userid    , d.cmu_nm    cmu_nm  "
                 + "\n           ,e.kor_name  room_master,e.userid  room_masterid, d.member_cnt member_cnt,d.accept_dte accept_dte,d.intro intro"
                 //+ "\n           ,b.deptnam         bdeptnam                ,b.jikupnm              bjikupnm ,b.jikwinm         bjikwinm                "     
                 + "\n           ,b.name                 bname"          
                 + "\n           ,b.userid                 buserid"
                 //+ "\n           ,nvl(c.deptnam,'') cdeptnam                ,nvl(c.jikupnm,'')      cjikupnm ,nvl(c.jikwinm,'') cjikwinm                "     
                 + "\n           ,nvl(c.name,'')         cname"          
                 + "\n           ,c.userid                 cuserid"
                 + "\n    from tz_cmupolice a,tz_member b,tz_member c,tz_cmubasemst d"
                 + "\n       ,(select cmuno,userid,kor_name,grade from tz_cmuusermst where grade='01' and close_fg='1') e"
                 + "\n   where a.cmuno            = d.cmuno  "
                 + "\n     and a.userid           = b.userid "  
                 + "\n     and d.cmuno            = e.cmuno  "
                 + "\n     and a.str_userid       = c.userid(+) "  
                 + "\n     and a.str_fg           = '" +v_str_fg + "'"
                 + "\n  order by a.policeno desc";
            
            System.out.println("==========  receive from the form object and session=========");
            System.out.println(sql);
            System.out.println("==========selectList=============");
            
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
    * 조회
    * @param box          receive from the form object and session
    * @return ArrayList   조회
    * @throws Exception
    */
    public ArrayList selectView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;

        String v_policeno  = box.getString("p_policeno");
//        String s_userid             = box.getSession("userid");
//        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            sql  ="\n  select    a.policeno  policeno , a.cmuno   cmuno , a.cmu_nm    cmu_nm  "
                 + "\n           ,a.userid    userid   , a.email   email , a.content   content "
                 + "\n           ,a.singo_dte singo_dte, a.str_fg  str_fg, a.repmemo   repmemo "
                 + "\n           ,a.str_dte   str_dte  , a.str_userid    , d.cmu_nm    cmu_nm  "
                 + "\n           ,e.kor_name  room_master,d.member_cnt member_cnt,d.accept_dte accept_dte,d.intro intro"
                 //+ "\n           ,b.deptnam         bdeptnam                ,b.jikupnm              bjikupnm ,b.jikwinm         bjikwinm                "     
                 + "\n           ,b.name                 bname"          
                 //+ "\n           ,nvl(c.deptnam,'') cdeptnam                ,nvl(c.jikupnm,'')      cjikupnm ,nvl(c.jikwinm,'') cjikwinm                "     
                 + "\n           ,nvl(c.name,'')         cname"          
                 + "\n    from tz_cmupolice a,tz_member b,tz_member c,tz_cmubasemst d"
                 + "\n       ,(select cmuno,userid,kor_name,grade from tz_cmuusermst where grade='01' and close_fg='1') e"
                 + "\n   where a.cmuno            = d.cmuno  "
                 + "\n     and a.userid           = b.userid "  
                 + "\n     and d.cmuno            = e.cmuno  "
                 + "\n     and a.str_userid       = c.userid(+) "  
                 + "\n     and a.policeno         = " + StringManager.makeSQL(v_policeno);

            
            
            System.out.println("=================");
            System.out.println(sql);
            System.out.println("=================");
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