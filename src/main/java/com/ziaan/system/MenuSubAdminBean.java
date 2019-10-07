// **********************************************************
//  1. ��      ��: ��� ����
//  2. ���α׷��� : MenuSubAdminBean.java
//  3. ��      ��: �޴� ����
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2004. 12.  16
//  7. ��      ��:
// **********************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * �޴� ����(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class MenuSubAdminBean { 
    private static final String CONFIG_NAME = "cur_nrm_grcode";
    public MenuSubAdminBean() { }


    /**
    �޴�ȭ�� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   �޴� ����Ʈ
    */
    public ArrayList selectListMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls = null, ls1 = null;
        ArrayList           list    = null;
        String sql = "", sql1 = "";
        MenuData data = null;

        String v_grcode  = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_searchtext = box.getString("p_searchtext");
        int    v_cnt = 0;

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();
            
            sql  = "SELECT MENU  \n";
            sql += "     , MENUNM  \n";
            sql += "     , UPPER  \n";
            sql += "     , PARENT  \n";
            sql += "     , PGM  \n";
            sql += "     , ISDISPLAY  \n";
            sql += "     , PARA1  \n";
            sql += "     , PARA2  \n";
            sql += "     , PARA3  \n";
            sql += "     , PARA4  \n";
            sql += "     , PARA5  \n";
            sql += "     , PARA6  \n";
            sql += "     , PARA7  \n";
            sql += "     , PARA8  \n";
            sql += "     , PARA9  \n";
            sql += "     , PARA10  \n";
            sql += "     , PARA11  \n";
            sql += "     , PARA12  \n";
            sql += "     , CREATED  \n";
            sql += "     , LUSERID  \n";
            sql += "     , LDATE  \n";
            sql += "     , LEVELS  \n";
            sql += "     , ORDERS  \n";
            sql += "     , SYSTEMGUBUN  \n";
            sql += "     ,(SELECT COUNT(*) FROM TZ_MENUSUB WHERE MENU = A.MENU) AS CNT  \n";
            sql += "FROM   TZ_MENU A  \n";
            sql += "WHERE  GRCODE  = " + StringManager.makeSQL(v_grcode) + "  \n";
            if(!v_searchtext.equals("")) {      //    �˻�� ������
            	sql += "AND    MENUNM LIKE " + StringManager.makeSQL("%" + v_searchtext + "%");
            }
            sql += "ORDER BY   \n";
            sql += "      (SELECT ORDERS  \n";
            sql += "       FROM   TZ_MENU  \n";
            sql += "       WHERE  MENU = A.PARENT) ASC  \n";
            sql += "     , PARENT ASC  \n";
            sql += "     , LEVELS ASC  \n";
            sql += "     , ORDERS ASC  \n";

            /*sql  = " select menu, menunm, upper, parent, pgm, isdisplay, para1, para2, para3, para4, ";
            sql += "        para5, para6, para7, para8, para9, para10, para11, para12,               ";
            sql += "        created, luserid, ldate, levels, orders, systemgubun                     ";
            sql += "   from TZ_MENU                                                                  ";
            sql += "  where grcode  = " + StringManager.makeSQL(v_grcode);

            if ( !v_searchtext.equals("") ) {      //    �˻�� ������
               sql += " and menunm like " + StringManager.makeSQL("%" + v_searchtext + "%");
            }
            sql += " order by menu asc, levels asc";*/

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
                data.setSystemgubun( ls.getString("systemgubun") );

                /*
				sql1 = "select count(*) from tz_menusub where menu='" +ls.getString("menu") + "'";
				ls1 = connMgr.executeQuery(sql1);
				if ( ls1.next() ) { 
					v_cnt = ls1.getInt(1);
				}
				ls1.close();
				data.setCnt(v_cnt);
				*/
                data.setCnt(ls.getInt("cnt"));
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
    ���ȭ�� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   ��� ����Ʈ
    */
    public ArrayList selectListMenuSub(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        MenuSubData data = null;

        String v_grcode  = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_menu    = box.getString("p_menu");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select menu, seq, servlet, modulenm, luserid, ldate    ";
            sql += "   from TZ_MENUSUB                                      ";
            sql += "  where grcode = " + StringManager.makeSQL(v_grcode);
            sql += "    and menu   = " + StringManager.makeSQL(v_menu);
            sql += " order by menu asc";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new MenuSubData();

                data.setMenu( ls.getString("menu") );
                data.setSeq( ls.getInt("seq") );
                data.setServlet( ls.getString("servlet") );
                data.setModulenm( ls.getString("modulenm") );
                data.setLuserid( ls.getString("luserid") );
                data.setLdate( ls.getString("ldate") );

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
    ���ȭ�� �󼼺���
    @param box          receive from the form object and session
    @return MenuData    ��ȸ�� ������
    */
    public MenuSubData selectViewMenuSub(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        MenuSubData data = null;

        String v_grcode = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_menu  = box.getString("p_menu");
        int    v_seq   = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select menu, seq, servlet, modulenm, luserid, ldate    ";
            sql += "   from TZ_MENUSUB                                      ";
            sql += "  where grcode = " + StringManager.makeSQL(v_grcode);
            sql += "    and menu   = " + StringManager.makeSQL(v_menu);
            sql += "    and seq    = " + v_seq;

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new MenuSubData();
                data.setMenu( ls.getString("menu") );
                data.setSeq( ls.getInt("seq") );
                data.setServlet( ls.getString("servlet") );
                data.setModulenm( ls.getString("modulenm") );
                data.setLuserid( ls.getString("luserid") );
                data.setLdate( ls.getString("ldate") );
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
    ��� ����Ҷ�
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertMenuSub(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        int isOk = 0;

        String v_grcode 	= CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_menu   	= box.getString("p_menu");
        int    v_seq    	= 0;
        String v_servlet 	= box.getString("p_servlet");
        String v_modulenm  	= box.getString("p_modulenm");
        String v_systemgubun= box.getString("p_systemgubun");

        String s_userid = box.getSession("userid");

        try { 
           connMgr = new DBConnectionManager();

           sql  = "select nvl(max(seq),-1) seq from TZ_MENUSUB  ";
           sql += " where grcode = " + StringManager.makeSQL(v_grcode);
           sql += "   and menu   = " + StringManager.makeSQL(v_menu);

           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_seq = ls.getInt(1) + 1;
           } else { 
               v_seq = 0;
           }

           sql1 =  "insert into TZ_MENUSUB(grcode, menu, seq, servlet, modulenm, systemgubun, luserid, ldate ) ";
           sql1 += "            values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))   ";

           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setString(1,  v_grcode);
           pstmt.setString(2,  v_menu);
           pstmt.setInt(3,  v_seq);
           pstmt.setString(4,  v_servlet);
           pstmt.setString(5,  v_modulenm);
           pstmt.setString(6,  v_systemgubun);
           pstmt.setString(7, s_userid);

           isOk = pstmt.executeUpdate();

           box.put("p_seq",String.valueOf(v_seq));
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
    ��� �����Ͽ� �����Ҷ�
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateMenuSub(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_grcode 	 = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_menu   	 = box.getString("p_menu");
        int    v_seq    	 = box.getInt("p_seq");
        String v_servlet 	 = box.getString("p_servlet");
        String v_modulenm  	 = box.getString("p_modulenm");
        String v_systemgubun = box.getString("p_systemgubun");

        String s_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " update TZ_MENUSUB set servlet = ? , modulenm = ?, luserid= ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') , systemgubun= ? ";
            sql += "  where grcode = ? and menu = ? and seq = ?                                                                  ";

            pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1,  v_servlet);
           pstmt.setString(2,  v_modulenm);
           pstmt.setString(3,  s_userid);
           pstmt.setString(4,  v_systemgubun);
           pstmt.setString(5,  v_grcode);
           pstmt.setString(6,  v_menu);
           pstmt.setInt(7,  v_seq);

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
    ��� �����Ҷ� - ���� ���μ��� ����
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int deleteMenuSub(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";

        int isOk1 = 0;
        int isOk2 = 0;

        String v_grcode  = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_menu   = box.getString("p_menu");
        int    v_seq = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1  = " delete from TZ_MENUSUB                       ";
            sql1 += "   where grcode = ? and menu = ? and seq = ?  ";

            sql2  = " delete from TZ_MENUSUBPROCESS    ";
            sql2 += "   where grcode = ? and menu = ? and seq = ?  ";


            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_grcode);
            pstmt1.setString(2, v_menu);
            pstmt1.setInt(3, v_seq);
            isOk1 = pstmt1.executeUpdate();


            pstmt2 = connMgr.prepareStatement(sql2);
            pstmt2.setString(1, v_grcode);
            pstmt2.setString(2, v_menu);
            pstmt2.setInt(3, v_seq);
            isOk2 = pstmt2.executeUpdate();

//            if ( isOk1 > 0 && isOk2 > 0) connMgr.commit();         //      2���� sql �� �� ���� delete �Ǿ�� �ϴ� ����̹Ƿ�
//            else connMgr.rollback();
            if ( isOk1 > 0 ) connMgr.commit();         //      ���� �з��� �ο찡 ������� isOk2 �� 0 �̹Ƿ� isOk2 > 0 ���� ����
            else connMgr.rollback();
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1 + "\r\n" +sql2);
            throw new Exception("sql = " + sql1 + "\r\n" + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
//        return isOk1*isOk2;
        return isOk1;
    }


    /**
    ���� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   ���� ����Ʈ
    */
    public ArrayList selectListMenuAuth(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        MenuAuthData data = null;

        String v_grcode  = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_menu    = box.getString("p_menu");
        String v_seq     = box.getString("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select a.gadmin,a.control,a.systemgubun, b.gadminnm  ";
            sql += "   from TZ_MENUAUTH a, TZ_GADMIN b     ";
            sql += "  where a.gadmin = b.gadmin             ";
            sql += "    and a.grcode     = " + StringManager.makeSQL(v_grcode);
            sql += "    and a.menu       = " + StringManager.makeSQL(v_menu);
            sql += "    and a.menusubseq = " + v_seq;
            sql += " order by a.gadmin asc";

            System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new MenuAuthData();

                data.setGadmin( ls.getString("gadmin") );
                data.setGadminnm( ls.getString("gadminnm") );
                data.setControl( ls.getString("control") );
                data.setSystemgubun( ls.getString("systemgubun") );
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
    ���� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   ����Ʈ
    */  
    public ArrayList selectListGadmin(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        GadminData data = null;

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select gadmin, gadminnm from TZ_GADMIN  ";
            sql += "  order by gadmin asc                    ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new GadminData();

                data.setGadmin( ls.getString("gadmin") );
                data.setGadminnm( ls.getString("gadminnm") );

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
    ���� ���
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertMenuAuth(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;
        int isOk2_check = 0;
		
		ArrayList list1  = (ArrayList)selectListGadmin(box);           // ������������Ʈ
        int    v_gadmincnt = list1.size();     
		
        String v_grcode  	 = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_systemgubun = box.getString("p_systemgubun");
        String v_menu    	 = box.getString("p_menu");
        int    v_seq     	 = box.getInt("p_seq");
        // Vector v_vgadmin  	 = box.getVector("p_gadmin");
        // Vector v_vcontrolr 	 = box.getVector("p_controlR");
        // Vector v_vcontrolw 	 = box.getVector("p_controlW");
        
        Vector v_vgadmin[]     = new Vector[v_gadmincnt];
        Vector v_vcontrolr[]   = new Vector[v_gadmincnt];
        Vector v_vcontrolw[]   = new Vector[v_gadmincnt];
        
        String v_gadmin    	 = "";
        String v_controlr  	 = "";
        String v_controlw  	 = "";
        String v_control   	 = "";

        String s_userid 	 = box.getSession("userid");
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ���� ����Ÿ ����
            sql1  = " delete from TZ_MENUAUTH                             ";
            sql1 += "   where grcode = ? and menu  = ? and menusubseq = ? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_grcode);
            pstmt1.setString(2, v_menu);
            pstmt1.setInt(3, v_seq);
            isOk1 = pstmt1.executeUpdate();
            // ����� �ڷ� ���
            sql2  = " insert into TZ_MENUAUTH (grcode, gadmin, menusubseq, menu, control, systemgubun, luserid, ldate) ";
            sql2 += "                   values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))    ";
            pstmt2 = connMgr.prepareStatement(sql2);

            isOk2 = 1;

			for ( int i = 0; i < v_gadmincnt; i++ ) { 

                v_vgadmin[i]    = box.getVector("p_gadmin" + i);
                v_vcontrolr[i] 	= box.getVector("p_controlR" + i);
                v_vcontrolw[i]  = box.getVector("p_controlW" + i);
			
            //   ���� ������ŭ ����
               
               v_gadmin = (String)v_vgadmin[i].elementAt(0);
               
               if ( v_vcontrolr[i].size() > 0) v_controlr = (String)v_vcontrolr[i].elementAt(0);
               else							  v_controlr = "";
               if ( v_vcontrolw[i].size() > 0) v_controlw = (String)v_vcontrolw[i].elementAt(0);
               else							  v_controlw = "";

               v_control = StringManager.chkNull(v_controlr) + StringManager.chkNull(v_controlw);
               
               // DB INSERT
               pstmt2.setString(1, v_grcode);
               pstmt2.setString(2, v_gadmin);
               pstmt2.setInt(3, v_seq);
               pstmt2.setString(4, v_menu);
               pstmt2.setString(5, v_control);
               pstmt2.setString(6, v_systemgubun);
               pstmt2.setString(7, s_userid);
               isOk2_check = pstmt2.executeUpdate();
               System.out.println("isOk2_check=" +isOk2_check);
               
               if ( isOk2_check == 0) isOk2 = 0;
               
            }
            // ���� ��ϵ� �ڷᰡ ���� �� �����Ƿ� isOk1 üũ ����
            if ( isOk2 > 0) connMgr.commit();
            else connMgr.rollback();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk2;
    }


}
