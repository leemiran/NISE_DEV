// **********************************************************
//  1. 제      목: 접속통계
//  2. 프로그램명: MenuCountBean.java
//  3. 개      요: 접속통계
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7. 7
//  7. 수      정:
// **********************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;


public class MenuCountBean { 
    private static final String CONFIG_NAME = "cur_nrm_grcode";
    
    public MenuCountBean() { }

    /**
     * 로그 작성
     * @param box       receive from the form object and session
     * @return is_Ok    1 : log ok      2 : log fail
     * @throws Exception
     */
    public int writeLog(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int cnt  = 0;
        int is_Ok = 99;

        // String v_gubun = CodeConfigBean.getConfigValue(CONFIG_NAME);
		String v_gubun  = box.getString("topmenu");
        String v_menuid = box.getString("leftmenu");
        String v_orders = box.getString("orders");
        String v_grcode = box.getSession("tem_grcode");
        String s_userid = box.getSession("userid");
		
		if ( s_userid.equals("")) s_userid = "null";

        try { 
			
			if ( !v_gubun.equals("") && !v_menuid.equals("") && !v_orders.equals("") && !v_grcode.equals("") ) { 
				// 메뉴가 정상적으로 등록되지 않았을 경우

				connMgr = new DBConnectionManager();

				if ( !v_orders.equals("") && v_menuid.equals("") ) { 
					sql = "select                                                  " +
                          "       menuid                                            " +
                          "  from                                                   " +
                          "       tz_homemenu                                       " +
                          " where                                                   " +
                          "       grcode = " + StringManager.makeSQL(v_grcode) + "  " +
                          "   and gubun = " + StringManager.makeSQL(v_gubun) + "    " +
                          "   and orders = " + StringManager.makeSQL(v_orders);

					ls = connMgr.executeQuery(sql);

					if ( ls.next() ) { 
						v_menuid = ls.getString("menuid");
					}
				}

				sql1  = " select                    " +
                        "       count(*) cnt        ";
				sql1 += " from                      " +
                        "       TZ_MENUCOUNT        ";
				sql1 += " where                     " +
                        "       grcode = " + StringManager.makeSQL(v_grcode);
				sql1 += "   and gubun  = " + StringManager.makeSQL(v_gubun);
				sql1 += "   and menuid = " + StringManager.makeSQL(v_menuid);
				if ( !s_userid.equals(""))	sql1 += "   and userid = " + StringManager.makeSQL(s_userid);
				ls = connMgr.executeQuery(sql1);

				if ( ls.next() ) { 
					cnt = ls.getInt("cnt");
				}
				ls.close();
				if ( cnt > 0 ) {                         // update
					sql2  = "update                                 " +
                            "       TZ_MENUCOUNT                    " +
                            "   set                                 " +
                            "       cnt = cnt + 1					";
					sql2 += " where                                 " +
                            "       grcode    = ?                   " +
                            "       and gubun = ?                   " +
                            "       and menuid = ?                  " +
                            "       and userid = ?                  ";
					// System.out.println("sql2 == = >> " +sql2);
					pstmt = connMgr.prepareStatement(sql2);

					pstmt.setString(1,  v_grcode);
					pstmt.setString(2,  v_gubun);
					pstmt.setString(3,  v_menuid);
					pstmt.setString(4,  s_userid);

				}else {                                // insert
					sql3  = " insert into TZ_MENUCOUNT(grcode, gubun, menuid, userid, cnt)	";
					sql3 += " values (?, ?, ?, ?, ?)										";
					// System.out.println("sql3 == = >> " +sql3);
					pstmt = connMgr.prepareStatement(sql3);

					pstmt.setString(1,  v_grcode);
					pstmt.setString(2,  v_gubun);
					pstmt.setString(3,  v_menuid);
					pstmt.setString(4,  s_userid);
					pstmt.setInt(5,  1);
				}
				is_Ok = pstmt.executeUpdate();



// System.out.println("v_grcode == = >> " +v_grcode);
// System.out.println("v_gubun == = >> " +v_gubun);
// System.out.println("v_menuid == = >> " +v_menuid);
// System.out.println("s_userid == = >> " +s_userid);
// System.out.println(" ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == ");
			}
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }


    /**
     * URL 조회
     * @param box       receive from the form object and session
     * @return is_Ok    1 : log ok      2 : log fail
     * @throws Exception
     */
    public String getMenuUrl(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String sql1 = "";
        String menuurl = "";

        // String v_gubun = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_gubun = box.getString("topmenu");
        String v_menuid = box.getString("leftmenu");
        String v_orders = box.getString("orders");
        String v_grcode = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );
		if ( v_grcode.equals(""))	v_grcode = "N000001";

        try { 
            connMgr = new DBConnectionManager();
            sql1  = "select                                                 " +
                    "       menuurl                                         ";
            sql1 += "  from                                                 " +
                    "       TZ_HOMEMENU                                     ";
            sql1 += " where                                                 " +
                    "       grcode = " + StringManager.makeSQL(v_grcode) + "";
            sql1 += "   and gubun  = " + StringManager.makeSQL(v_gubun) + " ";
            if ( !v_menuid.equals("")) sql1 += "   and menuid = " + StringManager.makeSQL(v_menuid);
            if ( !v_orders.equals("")) sql1 += "   and orders = " + StringManager.makeSQL(v_orders);

			ls = connMgr.executeQuery(sql1);

            if ( ls.next() ) { 
                menuurl = ls.getString("menuurl");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return menuurl;
    }


    /**
    * 메뉴통계 카운트
    * @param  box          receive from the form object and session
    * @return result       메뉴통계카운트
    * @throws Exception
    */
    public ArrayList SelectMenuList(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        ArrayList list1     = null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String sql    = "";

		String v_grcode = box.getString("s_grcode");        
		if ( v_grcode.equals(""))	v_grcode = "N000001";

        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        String v_gadmin = "";
        
        if ( !s_gadmin.equals("") ) { 
          v_gadmin = StringManager.substring(s_gadmin, 0, 1);
        }
        

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

			sql = "select b.gubun, b.menuname, nvl(a.cnt,0) cnt, c.gubuncnt from "; 
			sql += " (select grcode, gubun, menuid, sum(cnt) cnt ";
			sql += "  from TZ_MENUCOUNT	";
			sql += "  where 1=1 ";
			if ( v_gadmin.equals("A")) sql += " and grcode = " + StringManager.makeSQL(v_grcode);	// 운영자일 경우에는 교육그룹으로 검색
			if ( v_gadmin.equals("Z")) sql += " and userid = " + StringManager.makeSQL(s_userid);	// 학습자일 경우에는 학습자로 검색
			sql += "  group by grcode,gubun,menuid ) a, ";
			sql += " TZ_HOMEMENU b, ";
			sql += "(select gubun, count(gubun) gubuncnt from tz_homemenu where grcode =" + StringManager.makeSQL(v_grcode) + "  group by gubun) c ";
			sql += " where a.grcode( +)=b.grcode and a.gubun( +)=b.gubun and a.menuid( +)=b.menuid and b.gubun != '0' and b.gubun=c.gubun ";
			sql += " and b.grcode = '" +v_grcode + "' ";
			sql += " order by b.grcode,b.gubun,b.orders";
			System.out.println("sql_menucount == >> " +sql);

			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				list1.add(dbox);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }


    /**
    * My Activity
    * @param  box          receive from the form object and session
    * @return result       My Activity
    * @throws Exception
    */
    public ArrayList SelectActivityList(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        ArrayList list1     = null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String sql    = "";

		String v_grcode = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );
		if ( v_grcode.equals(""))	v_grcode = "N000001";

        String s_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

			sql += "  select b.gubun, b.orders, b.menuname, nvl(a.cnt,0) cnt, c.gubuncnt from "; 
			sql += "	(select grcode, gubun, menuid, sum(cnt) cnt ";
			sql += "		from TZ_MENUCOUNT	";
			sql += "		where grcode=" +StringManager.makeSQL(v_grcode) + " and userid=" +StringManager.makeSQL(s_userid);	// 학습자일 경우에는 학습자로 검색
			sql += "		group by grcode,gubun,menuid ) a, ";
			sql += "   TZ_HOMEMENU b,  ";
			sql += "   (select gubun, count(gubun) gubuncnt from TZ_HOMEMENU where grcode=" +StringManager.makeSQL(v_grcode) + " group by gubun) c";
			sql += "   where b.grcode =" + StringManager.makeSQL(v_grcode);
			sql += "     and a.grcode( +)=b.grcode and a.gubun( +)=b.gubun and a.menuid( +)=b.menuid and b.gubun=c.gubun";
			sql += "   order by b.gubun, b.orders, b.menuname";

			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				list1.add(dbox);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }


    /*게시판 등록수 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectBoardCnt(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list1     = null;
        DataBox             dbox    = null;
        String  sql        = "";

        String  s_userid    = box.getSession("userid");
        String v_grcode = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql = "select										";
            sql += "  ( select count(seq) from TZ_HOMEQNA		";// Q/A
            sql += "     where tabseq = '5'";
            sql += "         and grcode    =" + StringManager.makeSQL(v_grcode);
            sql += "         and inuserid    =" + StringManager.makeSQL(s_userid);
            sql += "  ) qnacnt,								";
            sql += "  ( select count(seq) from TZ_HOMEQNA		";// 운영자에게
            sql += "     where tabseq = '101'";
            sql += "         and grcode    =" + StringManager.makeSQL(v_grcode);
            sql += "         and inuserid    =" + StringManager.makeSQL(s_userid);
            sql += "  ) contactcnt,								";
            sql += "  ( select count(seq) from TZ_HOMEQNA		";// 지식공유
            sql += "     where tabseq = '7'";
            sql += "         and grcode    =" + StringManager.makeSQL(v_grcode);
            sql += "         and inuserid    =" + StringManager.makeSQL(s_userid);
            sql += "  ) knowcnt,								";
            sql += "  ( select count(seq) from TZ_COMMENTQNA		";// 답글
            sql += "     where inuserid    =" + StringManager.makeSQL(s_userid);
            sql += "  ) commentcnt";		
            sql += " from dual ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    
    

    /**
    * 메뉴통계 카운트
    * @param  box          receive from the form object and session
    * @return result       메뉴통계카운트
    * @throws Exception
    */
    public ArrayList selectPreviewCount(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        ArrayList list1     = null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String sql    = "";

		String v_grcode = box.getString("s_grcode");        
		if ( v_grcode.equals(""))	v_grcode = "N000001";

        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        String v_gadmin = "";
        
        if ( !s_gadmin.equals("") ) { 
          v_gadmin = StringManager.substring(s_gadmin, 0, 1);
        }
        

        try { 
            connMgr = new DBConnectionManager();     
            
            
            list1 = new ArrayList();
            sql +=" SELECT                          \n";
            sql +="   GRCODE,                                                            \n";
            sql +="   SUBJCOURSE,                                                        \n";
            sql +="   SUBJNM,                        \n";
            sql +="   CNT                            \n";
            sql +=" FROM                              \n";
            sql +=" ( \n";
			sql +=" SELECT                                                                 \n";
            sql +="   A.GRCODE,                                                            \n";
            sql +="   A.SUBJCOURSE,                                                        \n";
            sql +="   (SELECT X.SUBJNM FROM TZ_SUBJ X WHERE X.SUBJ = A.SUBJCOURSE) SUBJNM, \n";
            sql +="   COUNT(B.USERID)   cnt                                                \n";
            sql +=" FROM                                                                   \n";
            sql +="   TZ_GRSUBJ A,                                                         \n";
            sql +="   TZ_PREVIEW_LOG B                                                     \n";
            sql +=" WHERE                                                                  \n";
            sql +="   A.GRCODE = B.GRCODE( +)                                               \n";
            sql +="   AND A.SUBJCOURSE = B.SUBJ( +)                                         \n";
            sql +="   AND A.SUBJCOURSE not in(select subj from tz_subj where isonoff= 'OFF')\n";
            sql +="   AND A.GRCODE = '" +v_grcode + "'                                        \n";
            sql +=" GROUP BY                                                               \n";
            sql +="   A.GRCODE,  A.SUBJCOURSE                                              \n";
            sql +=" ) \n";
            sql +=" ORDER BY \n";
            sql +=" SUBJNM \n";
            
			System.out.println("sql_previewcount111 == >> " +sql);

			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				list1.add(dbox);
            }

        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    /**
    * 메뉴통계 카운트
    * @param  box          receive from the form object and session
    * @return result       메뉴통계카운트
    * @throws Exception
    */
    public ArrayList selectEmergency(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        ArrayList list1     = null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String sql    = "";

		String v_grcode = box.getString("s_grcode");        
		if ( v_grcode.equals(""))	v_grcode = "N000001";

        String s_gadmin = box.getSession("gadmin");
        String s_userid = box.getSession("userid");
        String v_gadmin = "";
        String v_today    = FormatDate.getDate("yyyyMMddHHmmss");
        String v_prevHour = FormatDate.getDayAdd(v_today,"yyyyMMddHHmmss","hour",-6);
        
        
        if ( !s_gadmin.equals("") ) { 
          v_gadmin = StringManager.substring(s_gadmin, 0, 1);
        }
        

        try { 
            connMgr = new DBConnectionManager();     
            
            System.out.println("3시간전 == = >>  >>  > " +FormatDate.getDayAdd("20051129021924","yyyyMMddHHmmss","hour",-3) );
            
            
            
            list1 = new ArrayList();
            sql += " select          \n";    
            sql += "   a.userid, \n";
            sql += "   a.lgip, \n";
            sql += "   a.ldate, \n";
            sql += "   b.name, \n";
            sql += "   get_compnm(comp, 2,2 ) conpnm, \n";
            sql += "   get_jikwinm(b.jikwi, b.comp) jikwinm\n";
            sql += " from                \n";
            sql += "   tz_loginid a, tz_member b \n";
            sql += " where\n";
            sql += "   a.userid = b.userid\n";
            sql += "  and a.ldate between  '" +v_prevHour + "' and '" +v_today + "'";
            sql += " order by \n";
            sql += "   a.ldate desc \n";

			System.out.println("sql_ == >> " +sql);

			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				list1.add(dbox);
            }

        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }


}