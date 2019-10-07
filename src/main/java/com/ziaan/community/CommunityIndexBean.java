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
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityIndexBean { 
//    private ConfigSet config;
//    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      파일업로드되는 tag name
//    private static final int FILE_LIMIT = 1;                    //    페이지에 세팅된 파일첨부 갯수


    public CommunityIndexBean() { 
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
    * 커뮤니티 메인 화면
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 메인화면 데이터 리스트
    * @throws Exception
    */
    public ArrayList selectMainIndex(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();

        ArrayList           list1    = new ArrayList();
        ArrayList           list2    = new ArrayList();
        ArrayList           list3    = new ArrayList();
        ArrayList           list4    = new ArrayList();
        ArrayList           list5    = new ArrayList();
        ArrayList           list6    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

        try { 
            connMgr = new DBConnectionManager();

            // 공지사항
            sql  = " select a.faq_type faq_type,a.faqno faqno,a.title title,a.content content,a.read_cnt read_cnt"
                 + "       ,a.add_cnt add_cnt,a.parent parent,a.lv lv,a.position position"
                 + "       ,a.register_userid register_userid,a.register_dte register_dte"
                 + "       ,a.modifier_userid modifier_userid,a.modifier_dte modifier_dte"
                 + "       ,b.userid userid,fn_crypt('2', b.birth_date, 'knise') birth_date,b.name name,b.email email 	 "	//,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                 + "   from tz_cmufaq a,tz_member b "
                 + "  where a.register_userid = b.userid "  
                 + "    and a.faq_type        = 'DIRECT'" 
                 + " order by a.root desc,a.position asc";
//                 + " ) a";
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(3);                         // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(1);                      // 현재페이지번호를 세팅한다.
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }
            ls.close();

            // 홍보방
//            sql  = " select a.*,rownum rowseq from ("
                 sql= " select a.cmuno cmuno,a.cmu_nm cmu_nm"
                 + "   from tz_cmubasemst a,tz_cmuhongbo b "
                 + "  where a.cmuno = b.cmuno "  
                 + "     and a.close_fg ='1'"
                 + " order by b.modifier_dte desc";
//                 + " ) a";
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(3);                         // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(1);                      // 현재페이지번호를 세팅한다.
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list2.add(dbox);
            }
            ls.close();


            // 신규커뮤니티
//            sql  = " select a.*,rownum rowseq from ("
                 sql = " select a.cmuno cmuno,a.cmu_nm cmu_nm"
                 + "   from tz_cmubasemst a"
                 + "  where a.close_fg ='1'"
                 + " order by a.register_dte desc";
//                 + " ) a";
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(3);                         // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(1);                      // 현재페이지번호를 세팅한다.
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list3.add(dbox);
            }
            ls.close();

            // 인기커뮤니티
//            sql  = " select a.*,rownum rowseq from ("
                 sql= " select a.cmuno cmuno,a.cmu_nm cmu_nm"
                 + "   from tz_cmubasemst a"
                 + "  where a.close_fg ='1'"
                 + "     and a.hold_fg   ='1'"
                 + " order by a.register_dte desc";
//                 + " ) a";
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(3);                         // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(1);                      // 현재페이지번호를 세팅한다.
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list4.add(dbox);
            }
            ls.close();


            // 커뮤니티 중분류
            sql  = "\n select b.parent type_l,b.code type_m,b.codenm type_m_nm,nvl(c.cnt,0) cnt"
                  + "\n   from tz_codegubun a,tz_code b"
                  + "\n       ,(select type_l,type_m,count(*) cnt from tz_cmubasemst where close_fg='1' group by type_l,type_m) c"
                  + "\n  where a.gubun = b.gubun"
                  + "\n    and b.code  = c.type_m(+) "
                  + "\n    and a.gubun ='0052'"
                  + "\n    and b.levels=2"
                  + "\n  order by a.gubun,b.code";
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list5.add(dbox);
            }
            ls.close();

            // 커뮤니티 대분류
            sql  = " select b.code type_l,b.codenm type_l_nm,count(*) cnt"
                  + "   from tz_codegubun a,tz_code b"
                  + "  where a.gubun = b.gubun"
                  + "    and a.gubun ='0052'"
                  + "    and b.levels=1"
                  + "  group  by b.code,b.codenm";
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list6.add(dbox);
            }
            ls.close();

            list.add(list1);
            list.add(list2);
            list.add(list3);
            list.add(list4);
            list.add(list5);
            list.add(list6);
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
    * 나의커뮤니티
    * @param box          receive from the form object and session
    * @return String   나의커뮤니티 리스트
    * @throws Exception
    */
    public String  selectMyCuminity(String v_userid,String v_cmuno) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
//        ArrayList           list    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        String              ret     = "";
//        DataBox             dbox    = null;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select b.cmuno cmuno,b.cmu_nm cmu_nm"
                 + "   from tz_cmuusermst a,tz_cmubasemst b"
                 + "  where a.cmuno     = b.cmuno"  
                 + "    and a.userid    = " +StringManager.makeSQL(v_userid)
                 + "    and b.close_fg  ='1'"
                 + "     and a.close_fg  ='1'"
                 + " order by b.cmu_nm asc";
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                if ( ls.getString("cmuno").equals(v_cmuno)) { 
                  ret += " <option value='" +ls.getString("cmuno") + "' selected > " +ls.getString("cmu_nm") + "</option > ";
                } else { 
                  ret += " <option value='" +ls.getString("cmuno") + "' > " +ls.getString("cmu_nm") + "</option > ";
                }
            }
            ls.close();

        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return ret;
    }

    /**
    * 내가 가입한 커뮤니티 리스트
    * @param box          receive from the form object and session
    * @return ArrayList   사용자 정보 데이터
    * @throws Exception
    */
    public ArrayList selectMyCommunity(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
//        PreparedStatement   pstmt = null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;
//        int                 isOK    =1;
        
        String s_userid        = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            sql  =  " select b.cmuno cmuno,b.cmu_nm cmu_nm, a.grade"
                 + "   from tz_cmuusermst a,tz_cmubasemst b"
                 + "  where a.cmuno     = b.cmuno"  
                 + "    and a.userid    = " +StringManager.makeSQL(s_userid)
                 + "    and b.close_fg  ='1'"
                 + "     and a.close_fg  ='1'"
                 + " order by a.grade";
                  
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
    * 내가 가입한 커뮤니티 리스트
    * @param box          receive from the form object and session
    * @return ArrayList   사용자 정보 데이터
    * @throws Exception
    */
    public ArrayList selectMyCommunity1(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
//        PreparedStatement   pstmt = null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;
//        int                 isOK    =1;
        
        String s_userid        = box.getString("p_searchuserid");
        
        try { 
            connMgr = new DBConnectionManager();
            sql  =  " select b.cmuno cmuno,b.cmu_nm cmu_nm, a.grade"
                 + "   from tz_cmuusermst a,tz_cmubasemst b"
                 + "  where a.cmuno     = b.cmuno"  
                 + "    and a.userid    = " +StringManager.makeSQL(s_userid)
                 + "    and b.close_fg  ='1'"
                 + "     and a.close_fg  ='1'"
                 + " order by a.grade";
                  
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
    * 메일전송시 사용하는 사용자정보
    * @param box          receive from the form object and session
    * @return ArrayList   사용자 정보 데이터
    * @throws Exception
    */
    public ArrayList selectSendMailData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
//        PreparedStatement   pstmt = null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;
//        int                 isOK    =1;
        String v_parent_userid         = box.getString("p_parent_userid");
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");
        try { 
            connMgr = new DBConnectionManager();
            sql  =  " select a.userid         userid  , fn_crypt('2', a.birth_date, 'knise')          birth_date     , fn_crypt('2', a.pwd, 'knise')          pwd"
                   + "      , a.name           name    , a.email          email     "	//	, a.cono         cono"
                   + "      , a.post1          post1   , a.post2          post2     "	//	, a.addr         addr"
                   + "      , a.hometel        hometel , a.handphone      handphone , a.comptel      comptel"
                   + "      , a.tel_line       tel_line, a.comp           comp      , a.indate       indate"
                   + "      , a.lgcnt          lgcnt   , a.lglast         lglast    , a.lgip         lgip" 
								   + "      , a.ldate          ldate     , a.lgfirst      lgfirst , b.gubun        gubun"                   
                   + "   from tz_member a,tz_compclass b"
                   + "  where a.comp = b.comp "
                   + "     and a.userid        = " +StringManager.makeSQL(v_parent_userid)
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
    * 사용자 정보 조회
    * @param box          receive from the form object and session
    * @return ArrayList   사용자 정보 데이터
    * @throws Exception
    */
    public ArrayList selectTz_Member(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();

        String              sql     = "";
        DataBox             dbox    = null;
        String s_userid        = box.getSession("userid");
 
        try { 
            connMgr = new DBConnectionManager();
            sql  =  "\n select a.userid         userid  , fn_crypt('2', a.birth_date, 'knise')          birth_date     , fn_crypt('2', a.pwd, 'knise')          pwd"
            	   + "\n      , a.name           name      , a.email      email     "
                   + "\n      , a.zip_cd         post1     , a.handphone  handphone  "
                   + "\n      , a.comp           comp      , a.indate     indate"
                   + "\n      , a.lgcnt          lgcnt     , a.lglast     lglast    , a.lgip         lgip" 
                   + "\n      , a.ldate          ldate     , a.lgfirst    lgfirst , b.gubun        gubun"                   
                   + "\n   from tz_member a,tz_compclass b"
                   + "\n  where a.comp = b.comp(+) "
                   + "\n     and a.userid        = " +StringManager.makeSQL(s_userid)
                  ;
            

            //System.out.println("===========사용자 정보 조회============");
            //System.out.println(sql);
            //System.out.println("========selectTz_Member============");

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
    * 커뮤니티 등급조회
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 등급 데이터
    * @throws Exception
    */
    public ArrayList selectTz_Cmugrdcode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
//        PreparedStatement   pstmt = null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;
//        int                 isOK    =1;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");

//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");

 

 
        try { 
            connMgr = new DBConnectionManager();
            sql  =  " select cmuno,grcode,kor_nm,eng_nm,descript,register_userid,register_dte,modifier_userid,modifier_dte"
                  + "   from tz_cmugrdcode"
                  + "  where cmuno        = " +StringManager.makeSQL(v_cmuno)
                  + " order by grcode asc"
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
//        String    sql1        = "";
        int       isOk        = 0;
//        int       v_seq       = 0;

        String    v_cmuno         = box.getString("p_cmuno");
//        String    v_close_fg      = box.getString("p_close_fg");
//        String    v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");
        String    s_userid        = box.getSession("userid");
//        String    s_name          = box.getSession("name");


        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 
             
             sql = "delete from tz_cmuusermst 	\n" 
             	 + "where cmuno = ?				\n"
                 + "and userid =?				\n";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_cmuno);
            pstmt.setString(2, s_userid);
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
//             sql  =" update tz_cmuusermst set  close_fg           =2   "
//                  + "                          ,close_dte    =dbo.to_date(getdate(),'YYYYMMDDHH24MISS')"
//                  + "                          ,close_reason = ?"
//                  + "                     where cmuno = ?"
//                  + "                       and userid =?";
//             pstmt = connMgr.prepareStatement(sql);
//             pstmt.setString(1, v_intro        );
//             pstmt.setString(2, v_cmuno         );
//             pstmt.setString(3, s_userid);
//             isOk = pstmt.executeUpdate();
//             sql1 = "select close_reason from tz_cmuusermst where cmuno = " + StringManager.makeSQL(v_cmuno) + " and userid=" +StringManager.makeSQL(s_userid);
//             connMgr.setOracleCLOB(sql1, v_intro);  

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