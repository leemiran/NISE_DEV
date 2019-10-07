// **********************************************************
//  1. ��      ��: �н����
//  2. ���α׷��� : ManagerAdminBean.java
//  3. ��      ��: �н����
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2004. 11. 10
//  7. ��      ��:
// **********************************************************

package com.ziaan.system;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * ��� ����(ADMIN)
 *
 * @date   : 2004. 11
 * @author : S.W.Kang
 */
public class ManagerAdminBean { 

    public ManagerAdminBean() { }

    /**
    ���ȭ�� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   ��� ����Ʈ
    */
    public ArrayList selectListManager(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_searchtext     = box.getString("p_searchtext");
        String v_search         = box.getString("p_search");
        String ss_gadmin        = box.getStringDefault("s_gadmin","ALL");
        String v_orderColumn    = box.getString("p_orderColumn");           	// ������ �÷���
        String v_orderType      = box.getString("p_orderType");           		// ������ ����
        StringTokenizer st      = new StringTokenizer(ss_gadmin,",");
        if ( st.hasMoreElements() ) { 
            ss_gadmin           = (String)st.nextToken();
		}

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

/* 2006.06.15 - ����
            sql  = " select a.userid userid, a.gadmin gadmin, c.gadminnm gadminnm,          ";
            sql += "        a.comp comp, a.isdeleted isdeleted, a.fmon fmon, a.tmon tmon,   ";
            sql += "        a.commented commented, a.luserid luserid, a.ldate ldate,        ";
            sql += "        b.jikwi jikwi,get_jikwinm(b.jikwi,b.comp) jikwinm, b.cono cono, ";
            sql += "        b.name name, get_compnm(b.comp,2,2) compnm,                      ";
            sql += "        b.jikwi jikwi,b.cono cono, b.name name                          ";
            sql += "   from TZ_MANAGER a, TZ_MEMBER b, TZ_GADMIN c                          ";
            sql += "  where a.userid = b.userid                                             ";
            sql += "    and a.gadmin = c.gadmin                                             ";
            sql += "    and a.isdeleted = 'N'                                               ";
            sql += "    and isView = 'Y'                                                ";
*/
            
            sql  = "SELECT                                               \n" +
                   "    a.userid                          userid,        \n" +
                   "    a.gadmin                          gadmin,        \n" +
                   "    c.gadminnm                        gadminnm,      \n" +
                   "    a.comp                            comp,          \n" +
                   "    a.isdeleted                       isdeleted,     \n" +
                   "    a.fmon                            fmon,          \n" +
                   "    a.tmon                            tmon,          \n" +
                   "    a.commented                       commented,     \n" +
                   "    a.luserid                         luserid,       \n" +
                   "    a.ldate                           ldate,         \n" +
                // "    b.cono                            cono,          \n" +                 
                // "    get_compnm(b.comp,2,2)            compnm,        \n" +
                // "    b.jikwi                           jikwi,         \n" +
                // "    get_jikwinm(b.jikwi,b.comp)       jikwinm,       \n" +
                // "    b.cono                            cono,          \n" +
                   "    b.name                            name           \n" +
                   "FROM                                                 \n" +
                   "    TZ_MANAGER                        a,             \n" +
                   "    TZ_MEMBER                         b,             \n" +
                   "    TZ_GADMIN                         c              \n" +
                   "WHERE                                                \n" +
                   "        a.userid    = b.userid                       \n" +
                   "    AND a.gadmin    = c.gadmin                       \n" +
                   "    AND a.isdeleted = 'N'                            \n" +
                   "    AND isView      = 'Y'                            \n";
            
            if ( !v_searchtext.equals("") ) {                            //    �˻�� ������
                if ( v_search.equals("name") ) {                          //    �������� �˻��Ҷ�
                    sql += " and b.name like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("userid") ) {                   //    �������� �˻��Ҷ�
                    sql += " and upper(b.userid) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
            }
            if ( !ss_gadmin.equals("ALL") ) { 
                sql += " and c.gadmin = " + StringManager.makeSQL(ss_gadmin);
            }
            

             if ( v_orderColumn.equals("") ) { 
    			sql += " order by a.gadmin asc ";
			 } else { 
			    sql += " order by b." + v_orderColumn + v_orderType;
			 }
			//System.out.println(sql);


            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
				dbox = ls.getDataBox();

                list.add(dbox);
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
    ���ȭ�� �󼼺���
    @param box              receive from the form object and session
    @return DataBox     ��ȸ�� ������
    */
   public DataBox selectViewManager(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_userid  = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin");

        try { 
            connMgr = new DBConnectionManager();
/* 2006.06.15 ����
            sql  = " select a.userid userid, a.gadmin gadmin, c.gadminnm gadminnm,          ";
            sql += "        a.comp comp, a.isdeleted isdeleted, a.fmon fmon, a.tmon tmon,   ";
            sql += "        a.commented commented, a.luserid luserid, a.ldate ldate,        ";
            sql += "        b.jikwi jikwi,get_jikwinm(b.jikwi,b.comp) jikwinm, b.cono cono, ";
            sql += "        b.name name, get_compnm(b.comp,2,2) compnm,                      ";
            sql += "        b.jikwi jikwi,b.cono cono,b.name name                           ";
            sql += "   from TZ_MANAGER a, TZ_MEMBER b, TZ_GADMIN c                          ";
            sql += "  where a.userid = b.userid                                             ";
            sql += "    and a.gadmin = c.gadmin                                             ";
            sql += "    and a.userid  = " + StringManager.makeSQL(v_userid);
            sql += "    and a.gadmin = " + StringManager.makeSQL(v_gadmin);
*/
            sql  = "SELECT                                                       \n" +
                   "    a.userid                       userid,                   \n" +
                   "    a.gadmin                       gadmin,                   \n" +
                   "    c.gadminnm                     gadminnm,                 \n" +
                   "    a.comp                         comp,                     \n" +
                   "    a.isdeleted                    isdeleted,                \n" +
                   "    a.fmon                         fmon,                     \n" +
                   "    a.tmon                         tmon,                     \n" +
                   "    a.commented                    commented,                \n" +
                   "    a.luserid                      luserid,                  \n" +
                   "    a.ldate                        ldate,                    \n" +
                   "    b.name                         name,                     \n" +
                   "    b.name                         name                      \n" +
                   "FROM                                                         \n" +
                   "    TZ_MANAGER                     a,                        \n" +
                   "    TZ_MEMBER                      b,                        \n" +
                   "    TZ_GADMIN                      c                         \n" +
                   "WHERE                                                        \n" +
                   "        a.userid = b.userid                                  \n" +
                   "    AND a.gadmin = c.gadmin                                  \n" +
                   "    AND a.userid = " + StringManager.makeSQL(v_userid) + "   \n" +
                   "    AND a.gadmin = " + StringManager.makeSQL(v_gadmin) + "   \n";            
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox=ls.getDataBox();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }


    /**
    ���ȭ�� �󼼺��� - �׷�
    @param box          receive from the form object and session
    @return ArrayList   ���� �����׷� ����Ʈ
    */
    public ArrayList selectViewManagerGrcode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox  dbox = null;

        String v_userid  = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select a.grcode, b.grcodenm from TZ_GRCODEMAN a , TZ_GRCODE b   ";
            sql += "  where a.grcode = b.grcode                                      ";
            sql += "    and a.userid = " + StringManager.makeSQL(v_userid);
            sql += "    and a.gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += " order by a.grcode asc                                           ";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    ���ȭ�� �󼼺��� - ����
    @param box          receive from the form object and session
    @return ArrayList   ���� ���� ����Ʈ
    */
    public ArrayList selectViewManagerSubj(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_userid  = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select a.subj, b.subjnm from TZ_SUBJMAN a , TZ_SUBJ b   ";
            sql += "  where a.subj = b.subj                                  ";
            sql += "    and a.userid = " + StringManager.makeSQL(v_userid);
            sql += "    and a.gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += " order by a.subj asc                                     ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    ���ȭ�� �󼼺��� - ȸ��
    @param box          receive from the form object and session
    @return ArrayList   ���� ȸ�� ����Ʈ
    */
    public ArrayList selectViewManagerComp(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_userid  = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select a.comp, '' as groupsnm , b.compnm as companynm , b.compnm ";
            sql += " from   tz_compman a , tz_compclass b   ";
            sql += " where  a.comp = b.comp                                  ";
            sql += " and    a.userid = " + StringManager.makeSQL(v_userid);
            sql += " and    a.gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += " order  by a.comp asc                ";
            /*
            sql = "select a.userid, a.comp, b.branchnm companynm, b.branchnm compnm           \n"
                + "from tz_COMPMAN a, tz_branch b                                             \n"
                + "where a.comp = b.branchcode                                                \n";
            sql += "    and a.userid = " + StringManager.makeSQL(v_userid);
            sql += "    and a.gadmin = " + StringManager.makeSQL(v_gadmin);
            */
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
    
    /**
    ���ȭ�� �󼼺��� - ȸ��(����� ���� ȭ��)
    @param box          receive from the form object and session
    @return ArrayList   ���� ȸ�� ����Ʈ
    */
    public ArrayList selectListManagerComp(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_gadmin = "K2";

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            /*sql  = " select a.comp, b.groupsnm , b.companynm , b.compnm from TZ_COMPMAN a , TZ_COMP b   ";
            sql += "  where a.comp = b.comp                                  ";
            sql += "    and a.userid = " + StringManager.makeSQL(v_userid);
            sql += "    and a.gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += " order by a.comp asc                ";*/
            
            sql = "select a.userid, a.comp, b.branchnm companynm, b.branchnm compnm           \n"
                + "from tz_COMPMAN a, tz_branch b                                             \n"
                + "where a.comp = b.branchcode                                                \n";
            //sql += "    and a.userid = " + StringManager.makeSQL(v_userid);
            sql += "    and a.gadmin = " + StringManager.makeSQL(v_gadmin);
            
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    ���ȭ�� �󼼺��� - ȸ��
    @param box          receive from the form object and session
    @return ArrayList   ���� ȸ�� ����Ʈ
    */
    public ArrayList selectViewManagerOutComp(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_userid  = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select a.comp, b.cpnm from TZ_OUTCOMPMAN a , TZ_CPINFO b   ";
            sql += "  where a.comp = b.cpseq                                  ";
            sql += "    and a.userid = " + StringManager.makeSQL(v_userid);
            sql += "    and a.gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += " order by a.comp asc                ";
            ls = connMgr.executeQuery(sql);


            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    ���ȭ�� �󼼺��� - �μ�
    @param box          receive from the form object and session
    @return ArrayList   ���� �μ� ����Ʈ
    */
    public ArrayList selectViewManagerDept(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_userid  = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = "\n select a.comp, '' as groupsnm , b.compnm as companynm , '' as gpmnm, '' as deptnm , b.compnm ";
            sql += "\n from   tz_compman a , tz_compclass b   ";
            sql += "\n where  a.comp = b.comp                                  ";
            sql += "\n and    a.userid = " + StringManager.makeSQL(v_userid);
            sql += "\n and    a.gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += "\n order  by a.comp asc                ";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    ��� ����Ҷ�
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertManager(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        int isOk = 0;
        int isOk1 = 0;
        int isOk2 = 0;
        int v_cnt = 0;       // �ߺ�üũ

        String v_userid    = box.getString("p_userid");
        String v_gadmin    = box.getString("p_gadmin");
        String v_comp      = box.getString("p_comp");
        String v_isdeleted = "N";
        String v_fmon      = box.getString("p_fmon");
        String v_tmon      = box.getString("p_tmon");
        String v_commented = box.getString("p_commented");

        String s_userid = box.getSession("userid");

        
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);

           sql  = "select count(*) from TZ_MANAGER ";
           sql += " where userid  = " + StringManager.makeSQL(v_userid);
           sql += "   and gadmin = " + StringManager.makeSQL(v_gadmin);
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
              v_cnt = ls.getInt(1);
           }
          //System.out.println(sql);
          if ( v_cnt > 0 ) {     // ���� ��ϵǾ�������
              isOk = 0;
          } else { 
           sql1 =  "insert into TZ_MANAGER(userid, gadmin, comp, isdeleted, fmon, tmon, commented, luserid, ldate)  ";
           sql1 += "               values (?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))               ";
           
           pstmt = connMgr.prepareStatement(sql1);
           pstmt.setString(1, v_userid);
           pstmt.setString(2, v_gadmin);
           pstmt.setString(3, v_comp);
           pstmt.setString(4, v_isdeleted);
           pstmt.setString(5, v_fmon);
           pstmt.setString(6, v_tmon);
           pstmt.setString(7, v_commented);
           pstmt.setString(8, s_userid);
           
           isOk = pstmt.executeUpdate();
          }

          isOk1 = insertManagerSub(box);
          isOk2 = insertMenuAuth(box);
       
          if ( isOk > 0 && isOk1 > 0 && isOk2 > 0 ) { 
          	connMgr.commit();
          } else { 
            connMgr.rollback();
          }
          isOk = isOk1 * isOk2 * isOk;
        }

        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    ��� ����Ҷ� - ��������
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertManagerSub(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        int v_cnt = 0;       // �ߺ�üũ

        String v_userid     = box.getString("p_userid");
        String v_gadmin     = box.getString("p_gadmin");
        String v_gadminview = box.getString("p_gadminview");

        String v_isneedgrcode  = "";
        String v_isneedsubj    = "";
        String v_isneedcomp    = "";
        String v_isneedoutcomp = "";
        String v_isneeddept    = "";

        // �ڵ���� (�����ڵ� + "," + �����׷��ʿ俩��  + "," + �����ڵ��ʿ俩�� + "," + ȸ���ڵ��ʿ俩�� + "," + �μ��ڵ��ʿ俩��)
        StringTokenizer st = new StringTokenizer(v_gadminview,",");
        
        // �׷�/����/ȸ��/�μ� �ʿ俩��
        if ( st.hasMoreElements() ) { 
            v_gadmin        = (String)st.nextToken();
            v_isneedgrcode  = (String)st.nextToken();   //�����׷������
            v_isneedsubj    = (String)st.nextToken();   //���������
            v_isneedcomp    = (String)st.nextToken();   //����������
            v_isneeddept    = (String)st.nextToken();
            v_isneedoutcomp = (String)st.nextToken();
        }

        // �׷��ڵ�
        Vector v_vgrcode = box.getVector("p_grcode");
        String v_sgrcode = "";
        // �����ڵ�
        Vector v_vsubj = box.getVector("p_subj");
        String v_ssubj = "";
        // ȸ���ڵ�
        Vector v_vcomp = box.getVector("p_company");
        String v_scomp = "";

        // ȸ���ڵ�
        Vector v_voutcomp = box.getVector("p_outcompany");
        String v_soutcomp = "";

        // �μ��ڵ�
        Vector v_vdept = box.getVector("p_dept");
        String v_sdept = "";

        String s_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            isOk = 1;                               // �׷�/����/�μ� �ڵ� ��� �������(UltraVisor,SuperVisor)
            
            // �׷��ڵ� �ʿ俩��
            if ( v_isneedgrcode.equals("Y") ) { 
                for ( int i = 0;i<v_vgrcode.size();i++ ) { 
                    v_sgrcode = (String)v_vgrcode.elementAt(i);
                    sql =  "insert into TZ_GRCODEMAN(userid, gadmin, grcode, luserid, ldate)         ";
                    sql += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                    
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_userid);
                    pstmt.setString(2, v_gadmin);
                    pstmt.setString(3, v_sgrcode);
                    pstmt.setString(4, s_userid);
                    isOk = pstmt.executeUpdate();
                }
            }
            // �����ڵ� �ʿ俩��
            if ( v_isneedsubj.equals("Y") ) { 
                for ( int i = 0;i<v_vsubj.size();i++ ) { 
                    v_ssubj = (String)v_vsubj.elementAt(i);
                    sql =  "insert into TZ_SUBJMAN(userid, gadmin, subj, luserid, ldate)             ";
                    sql += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                    
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_userid);
                    pstmt.setString(2, v_gadmin);
                    pstmt.setString(3, v_ssubj);
                    pstmt.setString(4, s_userid);
                    isOk = pstmt.executeUpdate();
                }
            }
            // ȸ���ڵ� �ʿ俩�� - ����������
            if ( v_isneedcomp.equals("Y") ) { 
                for ( int i = 0;i<v_vcomp.size();i++ ) { 
                    v_scomp = (String)v_vcomp.elementAt(i);
                    sql =  "insert into TZ_COMPMAN(userid, gadmin, comp, luserid, ldate)             ";
                    sql += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                    
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_userid);
                    pstmt.setString(2, v_gadmin);
                    pstmt.setString(3, v_scomp);
                    pstmt.setString(4, s_userid);
                    isOk = pstmt.executeUpdate();
                }
            }
            // �ܺξ�üȸ���ڵ� �ʿ俩��
            if ( v_isneedoutcomp.equals("Y") ) { 
                for ( int i = 0;i<v_voutcomp.size();i++ ) { 
                    v_soutcomp = (String)v_voutcomp.elementAt(i);
                    sql =  "insert into TZ_OUTCOMPMAN(userid, gadmin, comp, luserid, ldate)             ";
                    sql += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";

                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_userid);
                    pstmt.setString(2, v_gadmin);
                    pstmt.setString(3, v_soutcomp);
                    pstmt.setString(4, s_userid);
                    isOk = pstmt.executeUpdate();
                }
            }
            // �μ��ڵ� �ʿ俩��
            if ( v_isneeddept.equals("Y") ) { 
                for ( int i = 0;i<v_vdept.size();i++ ) { 
                    v_sdept = (String)v_vdept.elementAt(i);
                    sql =  "insert into TZ_COMPMAN(userid, gadmin, comp, luserid, ldate)             ";
                    sql += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                    
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_userid);
                    pstmt.setString(2, v_gadmin);
                    pstmt.setString(3, v_sdept);
                    pstmt.setString(4, s_userid);
                    isOk = pstmt.executeUpdate();
                }
            }

            //System.out.println("insertManagerSub ==  == = >>  >> " +isOk);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    ��� �����Ͽ� �����Ҷ�
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateManager(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk  = 0;
        int isOk1 = 0;

        String v_userid     = box.getString("p_userid");
        String v_gadmin    = box.getString("p_gadmin");
        String v_comp      = box.getString("p_comp");
        String v_fmon      = box.getString("p_fmon");
        String v_tmon      = box.getString("p_tmon");
        String v_commented = box.getString("p_commented");

        String s_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = " update TZ_MANAGER set comp = ? , fmon = ?, tmon = ?, commented = ?, luserid= ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += "  where userid  = ?  and gadmin = ? ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_comp);
            pstmt.setString(2, v_fmon);
            pstmt.setString(3, v_tmon);
            pstmt.setString(4, v_commented);
            pstmt.setString(5, s_userid);
            pstmt.setString(6, v_userid);
            pstmt.setString(7, v_gadmin);

            isOk  = pstmt.executeUpdate();
            isOk1 = updateManagerSub(box);

            if ( isOk > 0 && isOk1 > 0) { 
            	connMgr.commit();
            	isOk = 1;
            } else { 
                connMgr.rollback();
                isOk = 0;
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    ��� �����Ҷ� - ��������
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
    public int updateManagerSub(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1  = "";
        String sql2 = "";
        int isOk = 0;
        int v_cnt = 0;       // �ߺ�üũ

        String v_userid     = box.getString("p_userid");
        String v_gadmin     = box.getString("p_gadmin");
        String v_gadminview = box.getString("p_gadminview");
        String v_isneedgrcode  = "";
        String v_isneedsubj    = "";
        String v_isneedcomp    = "";
		String v_isneeddept    = "";
		String v_isneedoutcomp = "";

        // �ڵ���� (�����ڵ� + "," + �����׷��ʿ俩��  + "," + �����ڵ��ʿ俩�� + "," + ȸ���ڵ��ʿ俩�� + "," + �μ��ڵ��ʿ俩��)
        StringTokenizer st = new StringTokenizer(v_gadminview,",");
        int j = 0;


        // �׷�/����/ȸ��/�μ� �ʿ俩��
        if ( st.hasMoreElements() ) { 
            v_gadmin        = (String)st.nextToken();
            v_isneedgrcode  = (String)st.nextToken();
            v_isneedsubj    = (String)st.nextToken();
            v_isneedcomp    = (String)st.nextToken();
            v_isneeddept    = (String)st.nextToken();
            v_isneedoutcomp = (String)st.nextToken();
        }


        // System.out.println(v_gadmin       );
        // System.out.println(v_isneedgrcode );
        // System.out.println(v_isneedsubj   );
        // System.out.println(v_isneedcomp   );
        // System.out.println(v_isneeddept   );
        // System.out.println(v_isneedoutcomp);


        // �׷��ڵ� - v_gadmin(H)
        Vector v_vgrcode = box.getVector("p_grcode");
        String v_sgrcode = "";

        // �����ڵ� - v_gadmin(F,P)
        Vector v_vsubj = box.getVector("p_subj");
        String v_ssubj = "";

        // ȸ���ڵ� - v_gadmin(K)
        Vector v_vcomp = box.getVector("p_company");
        String v_scomp = "";

        // ȸ���ڵ� - v_gadmin(S,T,M)
        Vector v_voutcomp = box.getVector("p_outcompany");
        String v_soutcomp = "";

        // �μ��ڵ� - v_gadmin(K)
        Vector v_vdept = box.getVector("p_dept");
        String v_sdept = "";

        String s_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            isOk = 1;                               // �׷�/����/�μ� �ڵ� ��� �������(UltraVisor,SuperVisor)
            // �׷��ڵ� �ʿ俩��
            if ( v_isneedgrcode.equals("Y") ) { 
                // ���� ����Ÿ ����
                sql1 =  " delete from TZ_GRCODEMAN ";
                sql1 += "  where userid = ?        ";
                sql1 += "    and gadmin = ?        ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, v_userid);
                pstmt1.setString(2, v_gadmin);
                isOk = pstmt1.executeUpdate();
                // ���
                sql2 =  "insert into TZ_GRCODEMAN(userid, gadmin, grcode, luserid, ldate)        ";
                sql2 += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                pstmt2 = connMgr.prepareStatement(sql2);
                for ( int i = 0;i<v_vgrcode.size();i++ ) { 
                    v_sgrcode = (String)v_vgrcode.elementAt(i);
                    pstmt2.setString(1, v_userid);
                    pstmt2.setString(2, v_gadmin);
                    pstmt2.setString(3, v_sgrcode);
                    pstmt2.setString(4, s_userid);
                    isOk = pstmt2.executeUpdate();
                }
            }
            // �����ڵ� �ʿ俩��
            if ( v_isneedsubj.equals("Y") ) { 
                // ���� ����Ÿ ����
                sql1 =  " delete from TZ_SUBJMAN ";
                sql1 += "  where userid = ?        ";
                sql1 += "    and gadmin = ?        ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, v_userid);
                pstmt1.setString(2, v_gadmin);
                isOk = pstmt1.executeUpdate();
                // ���
                sql2 =  "insert into TZ_SUBJMAN(userid, gadmin, subj, luserid, ldate)             ";
                sql2 += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                pstmt2 = connMgr.prepareStatement(sql2);
                for ( int i = 0;i<v_vsubj.size();i++ ) { 
                    v_ssubj = (String)v_vsubj.elementAt(i);
                    pstmt2.setString(1, v_userid);
                    pstmt2.setString(2, v_gadmin);
                    pstmt2.setString(3, v_ssubj);
                    pstmt2.setString(4, s_userid);
                    isOk = pstmt2.executeUpdate();
                }
            }
            // ȸ���ڵ� �ʿ俩��
            if ( v_isneedcomp.equals("Y") ) { 
                // ���� ����Ÿ ����
                sql1 =  " delete from TZ_COMPMAN ";
                sql1 += "  where userid = ?        ";
                sql1 += "    and gadmin = ?        ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, v_userid);
                pstmt1.setString(2, v_gadmin);
                isOk = pstmt1.executeUpdate();
                // ���
                sql2 =  "insert into TZ_COMPMAN(userid, gadmin, comp, luserid, ldate)             ";
                sql2 += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                pstmt2 = connMgr.prepareStatement(sql2);
                for ( int i = 0;i<v_vcomp.size();i++ ) { 
                    v_scomp = (String)v_vcomp.elementAt(i);
                    pstmt2.setString(1, v_userid);
                    pstmt2.setString(2, v_gadmin);
                    pstmt2.setString(3, v_scomp);
                    pstmt2.setString(4, s_userid);
                    isOk = pstmt2.executeUpdate();
                }
            }
            // �����ڵ� �ʿ俩��
            if ( v_isneedoutcomp.equals("Y") ) { 
                // ���� ����Ÿ ����
                sql1 =  " delete from TZ_OUTCOMPMAN ";
                sql1 += "  where userid = ?        ";
                sql1 += "    and gadmin = ?        ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, v_userid);
                pstmt1.setString(2, v_gadmin);
                isOk = pstmt1.executeUpdate();
                // ���
                sql2 =  "insert into TZ_OUTCOMPMAN(userid, gadmin, comp, luserid, ldate)             ";
                sql2 += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                pstmt2 = connMgr.prepareStatement(sql2);
                for ( int i = 0;i<v_voutcomp.size();i++ ) { 
                    v_soutcomp = (String)v_voutcomp.elementAt(i);
                    pstmt2.setString(1, v_userid);
                    pstmt2.setString(2, v_gadmin);
                    pstmt2.setString(3, v_soutcomp);
                    pstmt2.setString(4, s_userid);
                    isOk = pstmt2.executeUpdate();
                }
            }

            // �μ��ڵ� �ʿ俩��
            if ( v_isneeddept.equals("Y") ) { 
                // ���� ����Ÿ ����
                sql1 =  " delete from TZ_COMPMAN ";
                sql1 += "  where userid = ?        ";
                sql1 += "    and gadmin = ?        ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, v_userid);
                pstmt1.setString(2, v_gadmin);
                isOk = pstmt1.executeUpdate();
                // ���
                sql2 =  "insert into TZ_COMPMAN(userid, gadmin, comp, luserid, ldate)             ";
                sql2 += "               values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                pstmt2 = connMgr.prepareStatement(sql2);
                /*
                 * 2006.09.27 ���� - ���� �����ڴ� �� ������ ���ؼ��� ������ 
                for ( int i = 0;i<v_vdept.size();i++ ) { 
                    v_sdept = (String)v_vdept.elementAt(i);
                    pstmt2.setString(1, v_userid);
                    pstmt2.setString(2, v_gadmin);
                    pstmt2.setString(3, v_sdept);
                    pstmt2.setString(4, s_userid);
                    isOk = pstmt2.executeUpdate();
                }
                */
                v_sdept = box.getString("p_company");
                pstmt2.setString(1, v_userid);
                pstmt2.setString(2, v_gadmin);
                pstmt2.setString(3, v_sdept);
                pstmt2.setString(4, s_userid);
                isOk = pstmt2.executeUpdate();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    ��� �����Ҷ�
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int deleteManager(RequestBox box) throws Exception { 
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

        String v_userid     = box.getString("p_userid");
        String v_gadmin     = box.getString("p_gadmin");
        String v_isdeleted = "Y";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ������ TABLE ���� -- isdeleted �ʵ忡 Y ����
            sql1  = "  delete from TZ_MANAGER 	  ";
            sql1 += "   where userid = ?		  ";
            sql1 += "     and gadmin = ?		  ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_userid);
            pstmt1.setString(2, v_gadmin);
            isOk1 = pstmt1.executeUpdate();

            // �����׷� TABLE ����
            sql2  = " delete from TZ_GRCODEMAN    ";
            sql2 += "  where userid  = ?          ";
            sql2 += "    and gadmin  = ?          ";

            pstmt2 = connMgr.prepareStatement(sql2);
            pstmt2.setString(1, v_userid);
            pstmt2.setString(2, v_gadmin);
            isOk2 = pstmt2.executeUpdate();


            // ����ȸ��/�μ����� TABLE ����
            sql3  = " delete from TZ_COMPMAN      ";
            sql3 += "  where userid  = ?          ";
//            sql3 += "    and gadmin  = ?          ";

            pstmt3 = connMgr.prepareStatement(sql3);
            pstmt3.setString(1, v_userid);
//            pstmt3.setString(2, v_gadmin);
            isOk3 = pstmt3.executeUpdate();

            // �������� TABLE ����
            sql4  = " delete from TZ_SUBJMAN      ";
            sql4 += "  where userid  = ?          ";
            sql4 += "    and gadmin  = ?          ";

            pstmt4 = connMgr.prepareStatement(sql4);
            pstmt4.setString(1, v_userid);
            pstmt4.setString(2, v_gadmin);
            isOk4 = pstmt4.executeUpdate();

            if ( isOk1 > 0 ) connMgr.commit();         //  �׷�, ����, ȸ��(�μ�) ����� �ܿ��� ���� ���� ���̺��� �����Ƿ�
            else connMgr.rollback();

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { } }
            if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
//        return isOk1*isOk2;
        return isOk1;
    }





/*---------------------------------------------------------------------------------------------------------------------------*/
    /**
    �μ������� �����λ���������
    @param box          receive from the form object and session
    @return String      ��������
    */
    public String getManagerDept(String v_userid, String v_gadmin) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        int    i = 0;

        try { 
            connMgr = new DBConnectionManager();
            sql  = " select comp comp from TZ_COMPMAN  ";
            sql += "  where userid = " + StringManager.makeSQL(v_userid);
            sql += "    and gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += " order by comp asc            ";
            ls = connMgr.executeQuery(sql);
            // System.out.println("comp_sql=" +sql);

            while ( ls.next() ) { 
                if ( i == 0) result  = " ( ";
                else        result += ", ";

                result += StringManager.makeSQL( ls.getString("comp") );
                i++;
            }
            if ( i > 0) result += " ) ";
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

	/**
    	���� ����� �� �ش� ���� ������ �޴� ������ �°��Ѵ�.
    	@param box      receive from the form object and session
    	@return isOk    1:insert success,0:insert fail
    */
    public int insertMenuAuth(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String v_userid = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin");
        String              sql     = "";

        int isOk1  = 0, isOk2 = 0;		 // ó�����

        String s_userid    = box.getSession("userid");

        try { 
           connMgr = new DBConnectionManager();

		   sql  = "delete from TZ_ADMINMENUAUTH where userid=" + StringManager.makeSQL(v_userid) + " ";
           
           //System.out.println("sql : " + sql);
           
		   isOk1 = connMgr.executeUpdate(sql);

		   sql  = "insert into                                            " +
                  "        TZ_ADMINMENUAUTH	                              ";
		   sql += "select                                                 " +
                  "        grcode,                                        ";
		   sql += "        menu,                                          ";
		   sql += "	       menusubseq,                                    ";
		   sql += "	       " + StringManager.makeSQL(v_userid) + ",                             ";
		   sql += "	       control,                                       ";
		   sql += "	       " + StringManager.makeSQL(s_userid) + ",                             ";
		   sql += "	       to_char(sysdate,'YYYYMMDDHH24MISS')            ";
		   sql += "  from                                                 " +
                  "        TZ_MENUAUTH                                    ";
		   sql += " where                                                 " +
                  "        gadmin='" +v_gadmin + "'                       ";
           
           //System.out.println("sql : " + sql);

           isOk2 = connMgr.executeUpdate(sql);

           if ( isOk2 > 0) connMgr.commit();         //  �׷�, ����, ȸ��(�μ�) ����� �ܿ��� ���� ���� ���̺��� �����Ƿ�
           else connMgr.rollback();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk2;
    }

    /**
    ���� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   ���� ����Ʈ
    */
    public ArrayList selectBranchList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();
            
            sql  = "SELECT                                               \n" +
                   "    branchcode                                       \n" +
                   ",   branchnm                                         \n" +   
                   "FROM                                                 \n" +
                   "    tz_branch                                        \n";
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                list.add(dbox);
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
}
