// **********************************************************
//  1. ��      ��: �޴� ����
//  2. ���α׷��� : MenuAuthAdminBean.java
//  3. ��      ��: �޴� ����
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2004. 11.  09
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
public class MenuAuthAdminBean { 
	private static final String CONFIG_NAME = "cur_nrm_grcode";
	
    public MenuAuthAdminBean() { }

    /**
    ���� ����
    @param box          receive from the form object and session
    @return result      ���� ����
    */  
    public int selectCountGadmin(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select count(*) cnt from TZ_GADMIN  ";

            ls = connMgr.executeQuery(sql);

            if  ( ls.next() ) { 
                result = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
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
    �޴����Ѽ���ȭ�� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   ����Ʈ
    */  
    public ArrayList selectListMenuAuth(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        ArrayList list2 = null;
        MenuAuthData data2 = null;

//        String v_grcode  = box.getString("p_grcode");
		String v_systemgubun = box.getStringDefault("p_systemgubun","1");
		String v_grcode  = CodeConfigBean.getConfigValue(CONFIG_NAME);
		
        String sql_add1 = "";                                         // �������� ���� �߰� ����(�����ڵ�)
        String sql_add2 = "";                                         // �������� ���� �߰� ����(�ش����(�б�/����))
        ArrayList list1 = (ArrayList)selectListGadmin(box);           // ������������Ʈ
        GadminData data1 = null;
        String v_gadmin    = "";
        int    v_gadmincnt = list1.size();
		
        try { 
            connMgr = new DBConnectionManager();

            //   ���� ���� ���� ����
            for ( int i = 0; i < v_gadmincnt; i++ ) { 
                data1   = (GadminData)list1.get(i);
                v_gadmin   = data1.getGadmin();

                // �����ڵ�
                if ( i == 0 ) {                                           // ó�� ���� ������ ���Ͽ�
                   sql_add1 += ", ";
                } else {                                                // ���ڰ� ���ս� ���̿� '/' �߰�
                   sql_add1 += " || '/' ||  ";
                }

                // ������ ���Ѻ� ��� ���� ID colnum
                sql_add1 += StringManager.makeSQL(v_gadmin) ;
                if ( (i +1) == v_gadmincnt ) sql_add1 += " as gadmin ";   // ������ �ο��� ���

                // �ش����(�б�/����)
                if ( i == 0 ) {                                           // ó�� ���� ������ ���Ͽ�
                   sql_add2 += ", ";
                } else {                                                // ���ڰ� ���ս� ���̿� '/' �߰�
                   sql_add2 += " || '/' ||  ";
                }
               // ������ ���Ѻ� ���Ѱ�(�б�/����) �÷� (row = >  colum ���� ��ȯ)
               // ���� : �ش簪�� ��ġ ������ ���� ���а� (�Ǿռ��� ���ڸ�) �߰����� (  i  + ���Ѱ�(�б�/����)  )
                sql_add2 += " max(decode(gadmin, "  + StringManager.makeSQL(v_gadmin) + ", '" + i + "' || control ,'" + i + "')) ";
                if ( (i +1) == v_gadmincnt ) sql_add2 += " as control ";   // ������ �ο��� ���
            }

            list2 = new ArrayList();

            /*
            sql  = " select grcode, menu, menunm, levels, seq                                  ";

            // ���� ���� ���� ���� �߰�
            sql += sql_add1;
            sql += sql_add2;

            sql += "   from                                                                     ";
            sql += "      (                                                                     ";
            sql += "        select a.grcode grcode, a.menu menu, a.modulenm menunm, a.seq seq,   ";
            sql += "               b.levels levels, c.gadmin gadmin, c.control control          ";
            sql += "          from tz_menusub a, tz_menu b, tz_menuauth c                       ";
            sql += "          where a.grcode = b.grcode                                         ";
            sql += "           and a.menu   = b.menu                                            ";
            sql += "           and a.grcode = c.grcode( +)                                       ";
            sql += "           and a.menu   = c.menu( +)                                         ";
            sql += "           and a.menu   = c.menu( +)                                         ";
            sql += "           and a.seq    = c.menusubseq( +)                                   ";
            sql += "           and a.grcode = " + StringManager.makeSQL(v_grcode);
            sql += "           and b.systemgubun = " + StringManager.makeSQL(v_systemgubun);
            sql += "       )                                                                    ";
            sql += " group by grcode, menu, menunm, seq, levels                                 ";
            sql += " order by grcode asc , menu asc, seq asc                                    ";
            */
            
            sql  = "SELECT GRCODE  \n";
            sql += "     , MENU  \n";
            sql += "     , MENUNM  \n";
            sql += "     , LEVELS  \n";
            sql += "     , ORDERS  \n";
            sql += "     , SEQ  \n";
            sql += sql_add1;
            sql += sql_add2;
            sql += "FROM  (SELECT A.GRCODE GRCODE  \n";
            sql += "            , A.MENU MENU  \n";
            sql += "            , A.MODULENM MENUNM  \n";
            sql += "            , A.SEQ SEQ  \n";
            sql += "            , B.LEVELS LEVELS  \n";
            sql += "            , C.GADMIN GADMIN  \n";
            sql += "            , C.CONTROL CONTROL  \n";
            sql += "            , B.ORDERS  \n";
            sql += "            , B.PARENT  \n";
            sql += "       FROM   TZ_MENUSUB A  \n";
            sql += "            , TZ_MENU B  \n";
            sql += "            , TZ_MENUAUTH C  \n";
            sql += "       WHERE  A.GRCODE = B.GRCODE  \n";
            sql += "       AND    A.MENU   = B.MENU  \n";
            sql += "       AND    A.GRCODE = C.GRCODE (+)  \n";
            sql += "       AND    A.MENU   = C.MENU (+)  \n";
            sql += "       AND    A.MENU   = C.MENU (+)  \n";
            sql += "       AND    A.SEQ    = C.MENUSUBSEQ (+)  \n";
            sql += "       AND    A.GRCODE = " + StringManager.makeSQL(v_grcode) + "  \n";
            sql += "       AND    B.SYSTEMGUBUN = " + StringManager.makeSQL(v_systemgubun) + "  \n";
            sql += "      ) Y  \n";
            sql += " GROUP BY  \n";
            sql += "       GRCODE  \n";
            sql += "     , MENU  \n";
            sql += "     , MENUNM  \n";
            sql += "     , SEQ  \n";
            sql += "     , LEVELS  \n";
            sql += "     , ORDERS  \n";
            sql += "     , PARENT  \n";
            sql += " ORDER BY  \n";
            sql += "       GRCODE ASC  \n";
            sql += "     ,(SELECT ORDERS FROM TZ_MENU WHERE MENU = Y.PARENT) ASC  \n";
            sql += "     , PARENT ASC  \n";
            sql += "     , LEVELS ASC  \n";
            sql += "     , ORDERS ASC  \n";
            sql += "     , SEQ ASC  \n";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data2 = new MenuAuthData();

                data2.setGrcode( ls.getString("grcode") );
                data2.setMenu( ls.getString("menu") );
                data2.setMenunm( ls.getString("menunm") );
                data2.setLevels( ls.getInt("levels") );
                data2.setSeq( ls.getInt("seq") );
                if ( v_gadmincnt > 0 ) { 
                   data2.setGadmin( ls.getString("gadmin") );
                   data2.setControl( ls.getString("control") );
                }

                list2.add(data2);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list2;
    }


    /**
    �޴����Ѽ����Ͽ� �����Ҷ�
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */      
    public int updateMenuAuth(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;
        int isOk2_check = 0;

		String v_grcode  = CodeConfigBean.getConfigValue(CONFIG_NAME);        
        String s_userid  = box.getSession("userid");
        String v_systemgubun = box.getString("p_systemgubun");

        ArrayList list1  = (ArrayList)selectListGadmin(box);           // ������������Ʈ
        GadminData data1 = null;
        String v_gadmin_org    = "";
        int    v_gadmincnt = list1.size();                            // ������������

        String v_menu    = "";
        String v_gadmin  = "";
        String v_control = "";
        int v_menusubseq = 0;

        Vector v_vecmenu[]       = new Vector[v_gadmincnt];
        Vector v_vecmenusubseq[] = new Vector[v_gadmincnt];
        Vector v_vecgadmin[]     = new Vector[v_gadmincnt];
		System.out.println("v_gadmincntâ�ƾ� �����=" +v_gadmincnt);
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ���� ����Ÿ ����
            sql1  = " delete from TZ_MENUAUTH   ";
            sql1 += "   where grcode = ? "; // and systemgubun = ?   ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_grcode);
//            pstmt1.setString(2, v_systemgubun);
            isOk1 = pstmt1.executeUpdate();

            // ����� �ڷ� ���
            sql2  = " insert into TZ_MENUAUTH  (grcode, gadmin, menusubseq, menu, control, systemgubun, luserid, ldate)       ";
            sql2 += "                  values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
            pstmt2 = connMgr.prepareStatement(sql2);

            isOk2 = 1;
            //   ���� ������ŭ ����
            for ( int i = 0; i < v_gadmincnt; i++ ) { 
            
                data1   = (GadminData)list1.get(i);
                v_gadmin_org   = data1.getGadmin();

                v_vecmenu[i]       = box.getVector("p_menu" + v_gadmin_org);
                v_vecmenusubseq[i] = box.getVector("p_menusubseq" + v_gadmin_org);
                v_vecgadmin[i]     = box.getVector("p_gadmin" + v_gadmin_org);

               // �޴� ������ŭ ����
               for ( int j = 0; j < v_vecmenu[i].size() ; j++ ) { 
               	   isOk2_check = 0;
               	   
                   v_menu       = (String)v_vecmenu[i].elementAt(j);
                   v_menusubseq = StringManager.toInt((String)v_vecmenusubseq[i].elementAt(j));
                   v_gadmin     = (String)v_vecgadmin[i].elementAt(j);
                   v_control = StringManager.chkNull(box.getString("p_" + v_gadmin_org + "R" + j + i)) + StringManager.chkNull(box.getString("p_" + v_gadmin_org + "W" + j + i));

					 System.out.println("=" + i + "��° ���� : " + v_menu + ", " +j + "��° �޴� : " + v_gadmin + "RW : " + v_control + "\n");
					 System.out.println(i + ":" +j + ": /" + v_grcode + "/" + v_gadmin  + "/" +  v_menusubseq + "/" +  v_menu + "/" + v_control);

                   // DB INSERT
                   pstmt2.setString(1, v_grcode);
                   pstmt2.setString(2, v_gadmin);
                   pstmt2.setInt(3, v_menusubseq);
                   pstmt2.setString(4, v_menu);
                   pstmt2.setString(5, v_control);
                   pstmt2.setString(6, v_systemgubun);
                   pstmt2.setString(7, s_userid);

                   isOk2_check = pstmt2.executeUpdate();
                   if ( isOk2_check == 0) isOk2 = 0;

                   System.out.println("isOk2_check=" +isOk2_check);

               }
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
