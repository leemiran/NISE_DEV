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

import com.ziaan.library.DBConnectionManager;
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
public class CommunityFrJoinBean { 
//    private ConfigSet config;
//    private int row;

    public CommunityFrJoinBean() { 
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
    * 회원가입하기
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertFrJoin(RequestBox box) throws Exception { 

    	//System.out.println("=======회원가입하기===========");
    	//System.out.println("======= insertFrJoin ========");


        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";
//        String      createCmuno = "";
        int         isOk        = 1;
//        int         v_seq       = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");
        String v_grade     = "";
        String v_in_method_fg="1";
        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 
             
//             sql1 = "select userid from tz_cmuusermst where userid = " + StringManager.makeSQL(s_userid);
             sql1 = "select userid from tz_cmuusermst where userid = " + StringManager.makeSQL(s_userid)
             + " and cmuno ='" +v_cmuno + "'";          
             
             ls = connMgr.executeQuery(sql1);
             if(ls.next()) {
            	 return 0;
             }
             
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

//             CommunityIndexBean bean = new CommunityIndexBean();
//             ArrayList list = bean.selectTz_Member(box); 
//             DataBox dbox = (DataBox)list.get(0);

             // 최하위등급구하기
             sql1  = " select max(grcode) grcode from tz_cmugrdcode where cmuno ='" +v_cmuno + "'";
             
             //System.out.println("=======최하위등급구하기===========");
             //System.out.println(sql1);
         	 //System.out.println("======= insertFrJoin ========");
             ls = connMgr.executeQuery(sql1);
             while ( ls.next() ) { 
                   v_grade= ls.getString("grcode");
             }
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

             // 회원가입방식
             sql1  = " select  in_method_fg from tz_cmubasemst where cmuno ='" +v_cmuno + "'";
             
             //System.out.println("=======회원가입방식===========");
             //System.out.println(sql1);
         	 //System.out.println("======= 1이면 신청즉시 가입 2 이면 승인후 가입 ========");
             ls = connMgr.executeQuery(sql1);
             while ( ls.next() ) { 
                   v_in_method_fg= ls.getString("in_method_fg");
             }
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }


             if ( "1".equals(v_in_method_fg)) { // 신청즉시가입
                sql  ="\n insert into tz_cmuusermst (cmuno       , userid      , kor_name    , eng_name      , email"
                     + "\n                                   , mobile      , office_tel  , duty          , wk_area"
                     + "\n                          , grade       , request_dte , license_dte , license_userid, close_fg"
                     + "\n                          , close_reason, close_dte   , intro       , recent_dte    , visit_num"
                     + "\n                          , search_num  , register_num, modifier_dte)"
                     + "\n             values (       ?           , ?           , ?           ,''             , ?"
                     + "\n                                    , ?           , ?           ,''             , ?"
                     //+ "\n                          , ?           , ?           , ?           ,''             , ?"
                     + "\n                          , ?           ,to_char(sysdate,'YYYYMMDDHH24MISS'),to_char(sysdate,'YYYYMMDDHH24MISS'),?,'1'"
                     + "\n                          , ? ,''           ,? ,''             ,0"
                     + "\n                          ,0            ,0            ,to_char(sysdate,'YYYYMMDDHH24MISS'))";
                 
//                System.out.println("=======신청즉시가입===========");
//                System.out.println(sql);
//            	System.out.println("======= insertFrJoin ========");
            	

                sql1  = " select userid,name,email,handphone from tz_member where userid ='" +s_userid + "'";
                
                //System.out.println("=======신청즉시가입2===========");
                //System.out.println(sql1);
           	    //System.out.println("======= insertFrJoin ========");
                ls = connMgr.executeQuery(sql1);
                while ( ls.next() ) { 
                	pstmt = connMgr.prepareStatement(sql);
                	pstmt.setString(1, v_cmuno            );
                	pstmt.setString(2, ls.getString("userid")   );
                	pstmt.setString(3, ls.getString("name") );
                	pstmt.setString(4, ls.getString("email") );
                	//pstmt.setString(5, ls.getString("hometel") );
                	pstmt.setString(5, ls.getString("handphone") );
                	pstmt.setString(6, "");//ls.getString("comptel") );
                	pstmt.setString(7, "");//ls.getString("work_plcnm") );
                	pstmt.setString(8, v_grade);
                	pstmt.setString(9, s_userid );
                	pstmt.setString(10, "" );
                	pstmt.setString(11, v_intro );
                	
                	isOk=pstmt.executeUpdate();
                	if ( pstmt != null ) { pstmt.close(); }
                }  
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
//                sql1 = "select intro from tz_cmuusermst where cmuno = '" + v_cmuno  + "' and userid ='" +s_userid + "'";
//                connMgr.setOracleCLOB(sql1, v_intro);  
//                sql1 = "select close_reason from tz_cmuusermst where cmuno = '" + v_cmuno + "' and userid ='" +s_userid + "'";
//                connMgr.setOracleCLOB(sql1, "");     

                sql1  = " update tz_cmubasemst set MEMBER_CNT = MEMBER_CNT +1 where cmuno ='" +v_cmuno + "'";
                
                //System.out.println("=======신청즉시가입2===========");
                //System.out.println(sql1);
           	    //System.out.println("======= insertFrJoin ========");
                pstmt = connMgr.prepareStatement(sql1);
                pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
             } else { // 승인후 가입

                sql  ="\n insert into tz_cmuusermst (cmuno       , userid      , kor_name    , eng_name      , email"
                     + "\n                                   , mobile      , office_tel  , duty          , wk_area"
                     + "\n                          , grade       , request_dte , license_dte , license_userid, close_fg"
                     + "\n                          , close_reason, close_dte   , intro       , recent_dte    , visit_num"
                     + "\n                          , search_num  , register_num, modifier_dte)"
                     + "\n             values (       ?           , ?           , ?           ,''             , ?"
                     + "\n                                     , ?           , ?           ,''             , ?"
                     //+ "\n                          , ?           , ?           , ?           ,''             , ?"
                     + "\n                          , ?           ,to_char(sysdate,'YYYYMMDDHH24MISS'),'','','0'"
                     + "\n                          , ? ,''       , ? ,''             ,0"
                     + "\n                          ,0            ,0            ,to_char(sysdate,'YYYYMMDDHH24MISS'))";
                
                //System.out.println("=======승인후 가입===========");
                //System.out.println(sql);
           	    //System.out.println("======= insertFrJoin ========");
                
                pstmt = connMgr.prepareStatement(sql);
                
                if ( pstmt != null ) { pstmt.close(); }
                
           	    sql1  = " select userid,name,email,handphone,'' work_plcnm from tz_member where userid =" +StringManager.makeSQL(s_userid) + " \n"; //comptel,
                
                //System.out.println("=======승인후 가입2===========");
                //System.out.println(sql1);
           	    //System.out.println("======= insertFrJoin ========");
                ls = connMgr.executeQuery(sql1);
                while ( ls.next() ) { 
                     pstmt.setString(1, v_cmuno            );
                     pstmt.setString(2, ls.getString("userid")   );
                     pstmt.setString(3, ls.getString("name") );
                     pstmt.setString(4, ls.getString("email") );
                     //pstmt.setString(5, ls.getString("hometel") );
                     pstmt.setString(5, ls.getString("handphone") );
                     pstmt.setString(6,"");// ls.getString("comptel") );
                     pstmt.setString(7,"");// ls.getString("work_plcnm") );
                     pstmt.setString(8, v_grade);
                     pstmt.setString(9, "");
                     pstmt.setString(10, v_intro);
                     isOk=pstmt.executeUpdate();
                     if ( pstmt != null ) { pstmt.close(); }
                }   
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
//                sql1 = "select intro from tz_cmuusermst where cmuno = '" + v_cmuno + "' and userid ='" +s_userid + "'";
//                connMgr.setOracleCLOB(sql1, v_intro);     

//                sql1 = "select close_reason from tz_cmuusermst where cmuno = '" + v_cmuno + "' and userid ='" +s_userid + "'";
//                connMgr.setOracleCLOB(sql1, "");     
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



    /**
    * 회원정보수정
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateFrMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
//        String      createCmuno = "";
        int         isOk        = 1;
//        int         v_seq       = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
//        String v_cmuno         = box.getString("p_cmuno");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");
//        String v_grade     = "";
//        String v_in_method_fg="1";
        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 


             sql  =" update tz_cmuusermst  set  tel         =?"
                  + "                          , mobile      =?"
                  + "                          , office_tel  =?"
                  + "                          , fax         =?"
                  + "                          , email       =?"
                  + "                          , duty        =?"  
                  + "                          , wk_area     =?"
                  + "                          , intro       =?"
                  + "                          , modifier_dte=to_char(sysdate,'YYYYMMDDHH24MISS')"
                  + "                where  cmuno  =?"
                  + "                  and  userid =?";
             pstmt = connMgr.prepareStatement(sql);

             pstmt.setString(1, box.getString("p_tel")            );
             pstmt.setString(2, box.getString("p_mobile")            );
             pstmt.setString(3, box.getString("p_office_tel")            );
             pstmt.setString(4, box.getString("p_fax")            );
             pstmt.setString(5, box.getString("p_email")            );
             pstmt.setString(6, box.getString("p_duty")            );
             pstmt.setString(7, box.getString("p_wk_area")            );
             pstmt.setString(8, v_intro            );
             pstmt.setString(9, box.getString("p_cmuno")            );
             pstmt.setString(10, s_userid    );
             isOk=pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
//             sql1 = "select intro from tz_cmuusermst where cmuno = '" + v_cmuno + "' and userid ='" +s_userid + "'";
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



    /**
    * 회원정보수정
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateFrEmailMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
//        String      createCmuno = "";
        int         isOk        = 1;
        int         isOk1        = 1;
//        int         v_seq       = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
//        String v_cmuno         = box.getString("p_cmuno");
//        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");
//        String v_grade     = "";
//        String v_in_method_fg="1";
        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 

             sql  =" update tz_member  set  email       =?"
                  + "                where  userid =?";
             pstmt = connMgr.prepareStatement(sql);
             pstmt.setString(1, box.getString("p_email")            );
             pstmt.setString(2, s_userid    );
             isOk=pstmt.executeUpdate();

             if ( pstmt != null ) { pstmt.close(); }

             sql  =" update tz_cmuusermst  set email       =?"
                  + "                          , modifier_dte=to_char(sysdate,'YYYYMMDDHH24MISS')"
                  + "                where  cmuno  =?"
                  + "                  and  userid =?";
             pstmt = connMgr.prepareStatement(sql);

             pstmt.setString(1, box.getString("p_email")            );
             pstmt.setString(2, box.getString("p_cmuno")            );
             pstmt.setString(3, s_userid    );
             isOk1=pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
             if ( (isOk *isOk1) > 0 ) { 
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
        return isOk *isOk1;
    }



    /**
    * 최근접속일수정
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateFrRecentDte(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
//        String      createCmuno = "";
        int         isOk        = 1;
        int         isOk1        = 1;
//        int         v_seq       = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");
//        String v_grade     = "";
//        String v_in_method_fg="1";
        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 


             sql  =" update tz_cmuusermst  set  recent_dte=to_char(sysdate,'YYYYMMDDHH24MISS')"
                  + "                where  cmuno  =?"
                  + "                  and  userid =?";
             pstmt = connMgr.prepareStatement(sql);

             pstmt.setString(1, v_cmuno           );
             pstmt.setString(2, s_userid    );
             isOk1=pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
             if ( (isOk *isOk1) > 0 ) { 
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
        return isOk *isOk1;
    }
}
