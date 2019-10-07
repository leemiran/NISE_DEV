// **********************************************************
//  1. ��      ��: �޴� ����
//  2. ���α׷��� : MenuAdminBean.java
//  3. ��      ��: �޴� ����
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2004. 11.  8
//  7. ��      ��:
// **********************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * �޴� ����(ADMIN)
 *
 * @date   : 2004. 11
 * @author : S.W.Kang
 */
public class MenuAdminBean { 
    private static final String CONFIG_NAME = "cur_nrm_grcode";
    public MenuAdminBean() { }


    /**
    �޴�ȭ�� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   �޴� ����Ʈ
    */
    public ArrayList selectListMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        MenuData data = null;

//        String v_grcode  = box.getString("p_grcode");
        String v_grcode  = CodeConfigBean.getConfigValue(CONFIG_NAME);
        int    v_levels  = box.getInt("p_levels");
        if ( v_levels == 0 ) { v_levels = 1; }

        String v_upper  = box.getString("p_upper");
        String v_parent = box.getString("p_parent");
        String v_searchtext = box.getString("p_searchtext");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select menu, menunm, upper, parent, pgm, isdisplay, para1, para2, para3, para4, ";
            sql += "        para5, para6, para7, para8, para9, para10, para11, para12,               ";
            sql += "        created, luserid, ldate, levels, orders                                  ";
            sql += "   from TZ_MENU                                                                  ";
            sql += "  where grcode  = " + StringManager.makeSQL(v_grcode);
            sql += "    and levels = " + v_levels;
            if ( v_levels > 1) { 
              sql += "    and parent = " + StringManager.makeSQL(v_parent);
              sql += "    and upper = " + StringManager.makeSQL(v_upper);
            }

            if ( !v_searchtext.equals("") ) {      //    �˻�� ������
               sql += " and menunm like " + StringManager.makeSQL("%" + v_searchtext + "%");
            }
//            sql += " order by menu asc";
            sql += " order by orders asc";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new MenuData();

                data.setMenu( ls.getString("menu") );
                data.setMenunm( ls.getString("menunm") );
                data.setUpper( ls.getString("upper") );
                data.setParent( ls.getString("parent") );
                data.setPgm( ls.getString("pgm") );
                data.setIsdisplay( ls.getString("isdisplay") );
                data.setPara1( ls.getString("para1") );
                data.setPara2( ls.getString("para2") );
                data.setPara3( ls.getString("para3") );
                data.setPara4( ls.getString("para4") );
                data.setPara5( ls.getString("para5") );
                data.setPara6( ls.getString("para6") );
                data.setPara7( ls.getString("para7") );
                data.setPara8( ls.getString("para8") );
                data.setPara9( ls.getString("para9") );
                data.setPara10( ls.getString("para10") );
                data.setPara11( ls.getString("para11") );
                data.setPara12( ls.getString("para12") );
                data.setCreated( ls.getString("created") );
                data.setLuserid( ls.getString("luserid") );
                data.setLdate( ls.getString("ldate") );
                data.setLevels( ls.getInt("levels") );
                data.setOrders( ls.getInt("orders") );

                list.add(data);
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
    �޴�ȭ�� �󼼺���
    @param box          receive from the form object and session
    @return MenuData    ��ȸ�� ������
    */
   public MenuData selectViewMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        MenuData data = null;

//        String v_grcode = box.getString("p_grcode");
        String v_grcode = CodeConfigBean.getConfigValue(CONFIG_NAME);        
        int    v_levels = box.getInt("p_levels");
        String v_menu  = box.getString("p_menu");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select menu, menunm, upper, parent, pgm, isdisplay, para1, para2, para3, para4, ";
            sql += "        para5, para6, para7, para8, para9, para10, para11, para12,               ";
            sql += "        created, luserid, ldate, levels, orders, systemgubun                     ";
            sql += "   from TZ_MENU                                                                  ";
            sql += "  where grcode  = " + StringManager.makeSQL(v_grcode);
            sql += "    and levels = " + v_levels;
            sql += "    and menu   = " + StringManager.makeSQL(v_menu);
            
            System.out.println("=========" + sql);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new MenuData();
                data.setMenu( ls.getString("menu") );
                data.setMenunm( ls.getString("menunm") );
                data.setUpper( ls.getString("upper") );
                data.setParent( ls.getString("parent") );
                data.setPgm( ls.getString("pgm") );
                data.setIsdisplay( ls.getString("isdisplay") );
                data.setPara1( ls.getString("para1") );
                data.setPara2( ls.getString("para2") );
                data.setPara3( ls.getString("para3") );
                data.setPara4( ls.getString("para4") );
                data.setPara5( ls.getString("para5") );
                data.setPara6( ls.getString("para6") );
                data.setPara7( ls.getString("para7") );
                data.setPara8( ls.getString("para8") );
                data.setPara9( ls.getString("para9") );
                data.setPara10( ls.getString("para10") );
                data.setPara11( ls.getString("para11") );
                data.setPara12( ls.getString("para12") );
                data.setCreated( ls.getString("created") );
                data.setLuserid( ls.getString("luserid") );
                data.setLdate( ls.getString("ldate") );
                data.setLevels( ls.getInt("levels") );
                data.setOrders( ls.getInt("orders") );
                data.setSystemgubun( ls.getString("systemgubun") );

            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;
    }


    /**
    �޴� ����Ҷ�
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        int isOk = 0;
        String v_menu = "";

//        String v_grcode  = box.getString("p_grcode");
        String v_grcode = CodeConfigBean.getConfigValue(CONFIG_NAME);        
        int    v_levels = box.getInt("p_levels");
        int    levels_tmp = v_levels*2;
        int    v_orders = box.getInt("p_orders");
        String v_menunm = box.getString("p_menunm");
        String v_upper  = box.getString("p_upper");
        String v_parent = box.getString("p_parent");
        String v_systemgubun = box.getString("p_systemgubun");

        String v_pgm       = box.getString("p_pgm");
        String v_isdisplay = box.getString("p_isdisplay");
        String v_para1     = box.getString("p_para1");
        String v_para2     = box.getString("p_para2");
        String v_para3     = box.getString("p_para3");
        String v_para4     = box.getString("p_para4");
        String v_para5     = box.getString("p_para5");
        String v_para6     = box.getString("p_para6");
        String v_para7     = box.getString("p_para7");
        String v_para8     = box.getString("p_para8");
        String v_para9     = box.getString("p_para9");
        String v_para10    = box.getString("p_para10");
        String v_para11    = box.getString("p_para11");
        String v_para12    = box.getString("p_para12");

        
        String s_userid = box.getSession("userid");

        try { 
           connMgr = new DBConnectionManager();

           sql  = "select max(substr(menu, 1," + levels_tmp + ")) from TZ_MENU  ";
           sql += " where grcode = " + StringManager.makeSQL(v_grcode);
            if ( v_levels > 1) { 
              sql += "    and parent = " + StringManager.makeSQL(v_parent);
            }
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_menu = CodeConfigBean.addZero(StringManager.toInt( ls.getString(1)) + 1, levels_tmp);
                for ( int i=1; i<=(8-levels_tmp); i++ ) { 
                    v_menu = v_menu + "0";
                }
           } else { 
               v_menu = "01000000";
           }

           // 1���������� �ڱ��ڵ尡 �θ��ڵ�, �����ڵ尡 ��
           if ( v_parent.equals("") ) { 
             v_parent = v_menu;
             v_upper  = v_menu;
           }

           sql1 =  "insert into TZ_MENU(grcode, menu, menunm, upper, parent, pgm, isdisplay, para1, para2, para3, para4, para5, para6, ";
           sql1 += "                    para7, para8, para9, para10, para11, para12, created, luserid, ldate, levels, orders,systemgubun  ) ";
           sql1 += "            values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,                                       ";
           sql1 += "                    to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?,? )          ";

           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setString(1,  v_grcode);
           pstmt.setString(2,  v_menu);
           pstmt.setString(3,  v_menunm);
           pstmt.setString(4,  v_upper);
           pstmt.setString(5,  v_parent);
           pstmt.setString(6,  v_pgm);
           pstmt.setString(7,  v_isdisplay);
           pstmt.setString(8,  v_para1);
           pstmt.setString(9,  v_para2);
           pstmt.setString(10, v_para3);
           pstmt.setString(11, v_para4);
           pstmt.setString(12, v_para5);
           pstmt.setString(13, v_para6);
           pstmt.setString(14, v_para7);
           pstmt.setString(15, v_para8);
           pstmt.setString(16, v_para9);
           pstmt.setString(17, v_para10);
           pstmt.setString(18, v_para11);
           pstmt.setString(19, v_para12);
           pstmt.setString(20, s_userid);
           pstmt.setInt(21, v_levels);
           pstmt.setInt(22, v_orders);
           pstmt.setString(23, v_systemgubun);

           isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    �޴� �����Ͽ� �����Ҷ�
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_grcode  = CodeConfigBean.getConfigValue(CONFIG_NAME);        
        int    v_levels = box.getInt("p_levels");
        int    v_orders = box.getInt("p_orders");
        String v_menu   = box.getString("p_menu");
        String v_menunm = box.getString("p_menunm");

        String v_pgm       = box.getString("p_pgm");
        String v_isdisplay = box.getString("p_isdisplay");
        String v_para1     = box.getString("p_para1");
        String v_para2     = box.getString("p_para2");
        String v_para3     = box.getString("p_para3");
        String v_para4     = box.getString("p_para4");
        String v_para5     = box.getString("p_para5");
        String v_para6     = box.getString("p_para6");
        String v_para7     = box.getString("p_para7");
        String v_para8     = box.getString("p_para8");        
        String v_para9     = box.getString("p_para9");
        String v_para10    = box.getString("p_para10");
        String v_para11    = box.getString("p_para11");
        String v_para12    = box.getString("p_para12");

        String s_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " update TZ_MENU set menunm = ? , pgm = ?, isdisplay = ?, para1 = ?, para2 = ?, para3 = ?, para4 = ?, para5 = ?, ";
            sql += "                    para6 = ?, para7 = ?, para8 = ?, para9 = ?, para10 = ?, para11 = ?, para12 = ?,             ";
            sql += "                    luserid= ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'), orders =?                        ";
            sql += "  where grcode = ? and levels = ? and menu = ?                                                                  ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_menunm);
            pstmt.setString(2, v_pgm);
            pstmt.setString(3, v_isdisplay);
            pstmt.setString(4, v_para1);
            pstmt.setString(5, v_para2);
            pstmt.setString(6, v_para3);
            pstmt.setString(7, v_para4);
            pstmt.setString(8, v_para5);
            pstmt.setString(9, v_para6);
            pstmt.setString(10, v_para7);
            pstmt.setString(11, v_para8);
            pstmt.setString(12, v_para9);
            pstmt.setString(13, v_para10);
            pstmt.setString(14, v_para11);
            pstmt.setString(15, v_para12);
            pstmt.setString(16, s_userid);
            pstmt.setInt(17, v_orders);
            pstmt.setString(18, v_grcode);
            pstmt.setInt(19, v_levels);
            pstmt.setString(20, v_menu);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    �޴� �����Ҷ� - ���� �з� ����
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int deleteMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sql4 = "";

        int isOk1 = 0;
        int isOk2 = 0;
        int isOk3 = 0;
        int isOk4 = 0;

        String v_grcode  = CodeConfigBean.getConfigValue(CONFIG_NAME);        
        int    v_levels = box.getInt("p_levels");
        String v_menu   = box.getString("p_menu");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1  = " delete from TZ_MENU                            ";
            sql1 += "   where grcode = ? and levels = ? and menu = ?  ";

            sql2  = " delete from TZ_MENU    ";
            sql2 += "   where grcode = ? and (upper = ? or parent = ? ) ";

            sql3  = " delete from TZ_MENUSUB where menu = ?          ";

            sql4  = " delete from TZ_MENUSUBPROCESS where menu = ?   ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_grcode);
            pstmt1.setInt(2, v_levels);
            pstmt1.setString(3, v_menu);
            isOk1 = pstmt1.executeUpdate();


            pstmt2 = connMgr.prepareStatement(sql2);
            pstmt2.setString(1, v_grcode);
            pstmt2.setString(2, v_menu);
            pstmt2.setString(3, v_menu);
            isOk2 = pstmt2.executeUpdate();

            pstmt3 = connMgr.prepareStatement(sql3);
            pstmt3.setString(1, v_menu);
            isOk3 = pstmt3.executeUpdate();

            pstmt4 = connMgr.prepareStatement(sql4);
            pstmt4.setString(1, v_menu);
            isOk4 = pstmt4.executeUpdate();
            if ( isOk1 > 0 ) connMgr.commit();         //      ���� �з��� �ο찡 ������� isOk2 �� 0 �̹Ƿ� isOk2 > 0 ���� ����
            else connMgr.rollback();

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1 + "\r\n" +sql2);
            throw new Exception("sql = " + sql1 + "\r\n" + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { } }
            if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1;
    }


/************************************* �����Լ��� ***********************************************************/

    /**
    *  �޴��� (����,�޴�)
    */
    public static String getMenuName (String grcode, String menu) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        MenuData data = null;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select menunm from TZ_MENU        ";
            sql += "  where grcode  = " + StringManager.makeSQL(grcode);
            sql += "    and menu   = " + StringManager.makeSQL(menu);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new MenuData();
                data.setMenunm( ls.getString("menunm") );
                result = data.getMenunm();
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;

    }

    /**
    *  �޴����� (����,�޴�)
    */
    public static MenuData getMenuInfo (String grcode, String menu) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        MenuData data = null;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select menunm,upper,parent,systemgubun from TZ_MENU        ";
            sql += "  where grcode  = " + StringManager.makeSQL(grcode);
            sql += "    and menu   = " + StringManager.makeSQL(menu);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new MenuData();
                data.setMenunm( ls.getString("menunm") );
                data.setUpper( ls.getString("upper") );
                data.setParent( ls.getString("parent") );
                data.setSystemgubun( ls.getString("systemgubun") );
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;

    }
    
    /**
     *  �޴� Ÿ��Ʋ ��� ����
     */
    public static ArrayList getMenuTitlePathInfo (String pm_menu) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	String sql = "";
    	String result = "";
    	MenuData data = null;
    	ArrayList list = null;
    	
    	int v_max = 0;
    	
    	try {
    		list = new ArrayList();
    		connMgr = new DBConnectionManager();
    		
    		sql = "SELECT levels + 1  as max                                 ";
    		sql += "                        FROM tz_menu                                      ";
    		sql += "                       WHERE menu = "+StringManager.makeSQL(pm_menu);
    		
    		ls = connMgr.executeQuery(sql);
    		
    		while (ls.next()) {
    			v_max = ls.getInt("max");
    		}
    		if(ls != null) {try {ls.close();} catch(Exception e){}}
    		
    		sql  = "";
    		sql += " SELECT     grcode, menu, menunm, levels, upper, parent, pgm, systemgubun ";
    		sql += "       FROM tz_menu                                                       ";
    		sql += "      WHERE 1 = 1                                                         ";
    		sql += " START WITH menu = " + StringManager.makeSQL(pm_menu);
    		sql += " CONNECT BY PRIOR upper = menu                                            ";
    		sql += "        AND ROWNUM <   " + v_max;
    		
    		ls = connMgr.executeQuery(sql);
    		
    		while (ls.next()) {
    			data = new MenuData();
    			data.setGrcode     (ls.getString("grcode"     ));
    			data.setMenu       (ls.getString("menu"       ));
    			data.setMenunm     (ls.getString("menunm"     ));
    			data.setLevels     (ls.getInt   ("levels"     ));
    			data.setUpper      (ls.getString("upper"      ));
    			data.setParent     (ls.getString("parent"     ));
    			data.setPgm        (ls.getString("pgm"        ));
    			data.setSystemgubun(ls.getString("systemgubun"));
    			list.add(data);
    		}
    		
    	}
    	catch (Exception ex) {
    		ErrorManager.getErrorStackTrace(ex);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	}
    	finally {
    		if(ls != null) {try {ls.close();} catch(Exception e){}}
    		if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
    	}
    	
    	return list;
    	
    }
}
