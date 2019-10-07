// **********************************************************
//  1. f      ��: ��İ�/ �Խ��� ��
//  2. �wα׷���: KnowBoardAdminBean.java
//  3. ��      ��: ��İ�/ ��
//  4. ȯ      ��: JDK 1.3
//  5. ��      o: 1.0 QnA
//  6. ��      ��: d:�� 2005. 9. 1
//  7. ��      d:
// **********************************************************
package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KnowBoardAdminBean { 
    
    public KnowBoardAdminBean() { }

    /**
    ���ڵ� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   ����Ʈ
    */
    public ArrayList SelectKonwCodeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list1 = null;
        ArrayList list2 = null;
        String sql  = "";
        DataBox             dbox    = null;

        String v_grcode   = box.getString("p_grcode");
        String v_action   = box.getString("p_action");        
        
        try { 
                
            if ( v_action.equals("go") ) { 
                sql = "select a.subjclass, a.upperclass, a.middleclass, a.lowerclass,  b.upperclassname, c.middleclassname, a.classname lowerclassname";
                sql += "  from TZ_KNOWCODE a,  ";
                sql += "  (select upperclass, classname  upperclassname  ";
                sql += "     from TZ_KNOWCODE  ";
                sql += "    where middleclass = '000'  ";
                sql += "      and lowerclass  = '000' and grcode='" +v_grcode + "' ) b,  ";
                sql += "  (select upperclass, middleclass, classname  middleclassname  ";
                sql += "     from TZ_KNOWCODE  ";
                sql += "    where middleclass != '000'  ";
                sql += "      and lowerclass  = '000' and grcode='" +v_grcode + "' ) c  ";
                sql += " where a.upperclass = b.upperclass  ";
                sql += "   and a.upperclass = c.upperclass( +)  ";
                sql += "   and a.middleclass = c.middleclass( +)  ";
                sql += "   and a.grcode='" +v_grcode + "'  ";
                sql += " order by a.subjclass, b.upperclassname  ";
    
    
  //  System.out.println("sql= > " + sql);
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                ls = connMgr.executeQuery(sql);
    
                while ( ls.next() ) { 
                    dbox=ls.getDataBox();
                    list1.add(dbox);
                }
    
                list2 = new ArrayList();
                for ( int i = 0; i<list1.size(); i++ ) { 
                    dbox = (DataBox)list1.get(i);
                    if ( dbox.getString("d_middleclass").equals("000") && dbox.getString("d_lowerclass").equals("000") ) { 
                        if ( getUpperCodeCnt(list1, dbox.getString("d_upperclass")) < 2) { 
                            list2.add(dbox);
                        }
                    } else if ( !dbox.getString("d_middleclass").equals("000") && dbox.getString("d_lowerclass").equals("000") ) { 
                        if ( getMiddleCodeCnt(list1, dbox.getString("d_upperclass"), dbox.getString("d_middleclass")) < 2) { 
                            list2.add(dbox);
                        }
                    } else if ( !dbox.getString("d_middleclass").equals("000") && !dbox.getString("d_lowerclass").equals("000") ) { 
                        list2.add(dbox);
                    }
                }
    
                String upperclass  = "";
                String upperclass2 = "";
                String middleclass = "";
                for ( int i = 0; i<list2.size(); i++ ) { 
                    dbox = (DataBox)list2.get(i);
                    if ( upperclass.equals(dbox.getString("d_upperclass"))) { 
                        dbox.put("d_upperrowspannum" , "0");
                    } else { 
                        dbox.put("d_upperrowspannum" , new Integer(getUpperCodeCnt(list2, dbox.getString("d_upperclass"))));
                        upperclass = dbox.getString("d_upperclass");
                    }
    
                    if ( upperclass2.equals(dbox.getString("d_upperclass")) && middleclass.equals(dbox.getString("d_middleclass"))) { 
                        dbox.put("d_middlerowspannum" , "0");
                    } else { 
                        dbox.put("d_middlerowspannum" , new Integer(getMiddleCodeCnt(list2, dbox.getString("d_upperclass"), dbox.getString("d_middleclass"))));
                        upperclass2 = dbox.getString("d_upperclass");
                        middleclass = dbox.getString("d_middleclass");
                    }
                }
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
    ��з��ڵ� ���� vȸ
    @param ArrayList   ��İ�/�з��ڵ� ����Ʈ
    @param String      ��з��ڵ�
    @return int         ��з��ڵ� ����
    */
    public int getUpperCodeCnt(ArrayList list1, String uppercode) { 
        int ncnt = 0;
        DataBox             dbox    = null;

        for ( int i = 0; i<list1.size(); i++ ) { 
            dbox = (DataBox)list1.get(i);

            if ( dbox.getString("d_upperclass").equals(uppercode)) { 
                ncnt++;
            }
        }

        return ncnt;
    }

    /**
    �ߺз��ڵ� ���� vȸ
    @param ArrayList   ��İ�/�з��ڵ� ����Ʈ
    @param String      ��з��ڵ�
    @param String      �ߺз��ڵ�
    @return int         �ߺз��ڵ� ����
    */
    public int getMiddleCodeCnt(ArrayList list1, String uppercode, String middlecode) { 
        int ncnt = 0;
        DataBox             dbox    = null;

        for ( int i = 0; i<list1.size(); i++ ) { 
            dbox = (DataBox)list1.get(i);

            if ( dbox.getString("d_upperclass").equals(uppercode) && dbox.getString("d_middleclass").equals(middlecode)) { 
                ncnt++;
            }
        }

        return ncnt;
    }


    /**
    ��ο� ��İ�/�з��ڵ� ���
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int InsertSalesCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        ListSet             ls      = null;
        DataBox             dbox    = null;        

        String v_grcode   = box.getString("p_grcode");
        String v_upperclass  = "";
        String v_middleclass = "";
        String v_lowerclass  = "";
        String v_classtype   = box.getString("p_classtype");
        String v_classcode   = box.getString("p_classcode");  // ��з� �϶��� �ڵ��=.
        String v_luserid     = box.getSession("userid");
        String v_classname   = box.getString("p_classname");  // �з��
                
        try { 
            connMgr = new DBConnectionManager();

            if ( v_classtype.equals("upper") ) { 
                v_upperclass  = v_classcode;
                v_middleclass = "000";
                v_lowerclass  = "000";
            } else if ( v_classtype.equals("middle") ) { 
                v_upperclass  = box.getString("s_upperclass");
                v_lowerclass  = "000";
                
                sql = " SELECT LPAD(max(nvl(middleclass, 000)) +1,3,'0') middleclass FROM TZ_KNOWCODE WHERE grcode='" +v_grcode + "' and upperclass='" +v_upperclass + "' ";
                ls = connMgr.executeQuery(sql);   
                if ( ls.next() ) { 
                    v_middleclass=ls.getString("middleclass");
                }
                            
            } else if ( v_classtype.equals("lower") ) { 
                v_upperclass  = box.getString("s_upperclass");
                v_middleclass = box.getString("s_middleclass");
                
                sql = " SELECT LPAD(max(nvl(lowerclass, 000)) +1,3,'0') lowerclass FROM TZ_KNOWCODE WHERE grcode='" +v_grcode + "' and upperclass='" +v_upperclass + "' and middleclass='" +v_middleclass + "' ";
                ls = connMgr.executeQuery(sql);   
                if ( ls.next() ) { 
                    v_lowerclass=ls.getString("lowerclass");
                }
            }
    
    
            String v_subjclass   = v_upperclass +v_middleclass +v_lowerclass;  // �ڵ�
        
            // insert TZ_KNOWCODE table
            sql =  "insert into TZ_KNOWCODE(grcode, subjclass,upperclass,middleclass,lowerclass,classname,luserid,ldate) ";
            sql +=  " values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'))";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_grcode);
            pstmt.setString(2, v_subjclass);
            pstmt.setString(3, v_upperclass);
            pstmt.setString(4, v_middleclass);
            pstmt.setString(5, v_lowerclass);
            pstmt.setString(6, v_classname);
            pstmt.setString(7, v_luserid);
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }            
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    ���õ� �з��ڵ� ��d
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int UpdateSalesCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_grcode     = box.getString("p_grcode");
        String v_subjclass  = box.getString("p_upperclass") + box.getString("p_middleclass") + box.getString("p_lowerclass");
        String v_classname  = box.getString("p_classname");
        String v_luserid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();


            // update TZ_KNOWCODE table
            sql = "update TZ_KNOWCODE set classname = ?, luserid = ?, ldate = to_char(sysdate,'YYYYMMDDHH24MISS') ";
            sql += " where subjclass = ? and grcode = ? ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_classname);
            pstmt.setString(2, v_luserid);
            pstmt.setString(3, v_subjclass);
            pstmt.setString(4, v_grcode);
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
    ���õ� �з��ڵ� ��f
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int DeleteSalesCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 1;
        ListSet             ls      = null;
        String v_grcode     = box.getString("p_grcode");
        String v_upperclass = box.getString("p_upperclass");
        String v_middleclass= box.getString("p_middleclass");
        String v_subjclass  = box.getString("p_upperclass") + box.getString("p_middleclass") + box.getString("p_lowerclass");

        try { 
            connMgr = new DBConnectionManager();

            // ��з��� ��� �ߺз� Ȯ��
            if ( v_middleclass.equals("000") ) { 
                sql = "select upperclass from TZ_KNOWCODE where upperclass = '" + v_upperclass + "' and middleclass != '000' and grcode='" +v_grcode + "' ";
                ls = connMgr.executeQuery(sql);

                if ( ls.next() ) { 
                    // �ߺз� �־� ��f�� �� ��=
                    isOk = -1;
                }
            } else { 

                // �ߺз��� ��� �Һз� Ȯ��
                if ( box.getString("p_lowerclass").equals("000") ) {                   
                    // sql = "select subjclass from tz_knowcode where subjclass = '" + v_subjclass + "' and grcode='" +v_grcode + "'";
                    sql = "select subjclass from TZ_KNOWCODE where ";
                    sql += "       upperclass = '" + v_upperclass + "' and middleclass='" +v_middleclass + "' and lowerclass != '000' and grcode='" +v_grcode + "' ";
    
                    ls = connMgr.executeQuery(sql);
    System.out.println(sql);
                    if ( ls.next() ) { 
                        // �Һз� �־� ��f�� �� ��=
                        isOk = -2;
                    }
                }
            }
            
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

            System.out.println("��� = " + isOk);
            if ( isOk == 1) { 
                // delete TZ_KNOWCODE table
                sql = "delete from TZ_KNOWCODE where subjclass = ? and grcode = ? ";

                pstmt = connMgr.prepareStatement(sql);

                pstmt.setString(1, v_subjclass);
                pstmt.setString(2, v_grcode);
                isOk = pstmt.executeUpdate();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
    
        

   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //     
   /***************************������ʹ� �ڵ� SELECT OPTION �±� ��n�1�**************************/
   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
   /**
   * ��з� SELECT
   * @param box          receive from the form object and session
   * @param isChange     onChage �Լ� ���/��
   * @param isALL        ��ü /��
   * @return result      ��з� SELECT BOX ���� ����
   */
    public static String getUpperClass(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "��з� ";
        boolean isVisible = false;
        String v_grcode     = box.getString("p_grcode");
        
        try { 
            String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    // 1�ܰ� ��ĺз�

            connMgr = new DBConnectionManager();

            sql = "select distinct upperclass, classname";
            sql += " from tz_knowcode";
            sql += " where middleclass = '000'";
            sql += " and lowerclass = '000'";
            sql += " and grcode = '" +v_grcode + "'";
            sql += " order by upperclass";

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);

            result += getSelectTag( ls, isChange, isALL, "s_upperclass", ss_upperclass);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


   /**
   * �ߺз� SELECT
   * @param box          receive from the form object and session
   * @param isChange     onChage �Լ� ���/��
   * @param isALL        ��ü /��
   * @return result      �ߺз� SELECT BOX ���� ����
   */
    public static String getMiddleClass(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "�ߺз� ";
        boolean isVisible = false;
        String v_grcode     = box.getString("p_grcode");
        try { 
                String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    //     1�ܰ� ��ĺз�
                String  ss_middleclass   = box.getStringDefault("s_middleclass","ALL");    //       2�ܰ� ��ĺз�
    // System.out.println("ss_middleclass" + ss_middleclass);
                connMgr = new DBConnectionManager();

                sql = "select distinct middleclass, classname";
                sql += " from tz_knowcode";
                sql += " where upperclass = " + SQLString.Format(ss_upperclass);
                sql += " and middleclass != '000'";
                sql += " and lowerclass = '000'";          
                sql += " and grcode = '" +v_grcode + "' ";                
                sql += " order by middleclass";

                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                ls = new ListSet(pstmt);

                result += getSelectTag( ls, isChange, isALL, "s_middleclass", ss_middleclass);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

   /**
   * �Һз� SELECT
   * @param box          receive from the form object and session
   * @param isChange     onChage �Լ� ���/��
   * @param isALL        ��ü /��
   * @return result      �Һз� SELECT BOX ���� ����
   */
    public static String getLowerClass(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "�Һз� ";
        boolean isVisible = false;
        String v_grcode     = box.getString("p_grcode");
        try { 
                String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    //     1�ܰ� ��ĺз�
                String  ss_middleclass   = box.getStringDefault("s_middleclass","ALL");    //       2�ܰ� ��ĺз�
                String  ss_lowerclass = box.getStringDefault("s_lowerclass","ALL");    //       3�ܰ� ��ĺз�

                connMgr = new DBConnectionManager();

                sql = "select distinct lowerclass, classname";
                sql += " from tz_knowcode";
                sql += " where upperclass = " + SQLString.Format(ss_upperclass);
                sql += " and middleclass = " + SQLString.Format(ss_middleclass);
                sql += " and lowerclass != '000'";
                sql += " and grcode = '" +v_grcode + "' ";                
                sql += " order by lowerclass";

                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                ls = new ListSet(pstmt);

                result += getSelectTag( ls, isChange, isALL, "s_lowerclass", ss_lowerclass);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

   /**
   * ȸ�� SELECT
   * @param box          receive from the form object and session
   * @param isChange     onChage �Լ� ���/��
   * @param isALL        ��ü /��
   * @return result      ȸ�� SELECT BOX ���� ����
   */
    public static String getComp(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "ȸ�� ";
        boolean isVisible = false;

        try { 
                String  ss_upperclass  = box.getStringDefault("s_upperclass","ALL");    // 1�ܰ� ��ĺз�
                String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");   // 2�ܰ� ��ĺз�
                String  ss_lowerclass  = box.getStringDefault("s_lowerclass","ALL");    // 3�ܰ� ��ĺз�
                String  ss_comp        = box.getStringDefault("s_comp","ALL");          // ȸ��

                connMgr = new DBConnectionManager();

                sql  = " select distinct a.comp, b.compnm   ";
                sql += "  from tz_knowcode a, tz_comp b ";
                sql += " where a.comp = b.comp              ";
                sql += "   and b.comptype ='2'              ";
                sql += " order by a.comp                    ";

                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                ls = new ListSet(pstmt);

                result += getSelectTag( ls, isChange, isALL, "s_comp", ss_comp);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

   /**
   * SELECT BOX ���� ����
   * @param box              receive from the form object and session
   * @param isChange         onChage �Լ� ���/��
   * @param isALL            ��ü /��
   * @param selname          SELECT BOX �̸�
   * @param optionselected   ���ð�
   * @return result      SELECT BOX ����
   */
    public static String getSelectTag(ListSet ls, boolean isChange, boolean isALL, String selname, String optionselected) throws Exception { 
        StringBuffer sb = null;
        // System.out.println("isChange" + isChange);System.out.println("isALL" + isALL);System.out.println("selname" + selname);System.out.println("optionselected" + optionselected);
        try { 
            sb = new StringBuffer();

            sb.append("<select name = \"" + selname + "\"");
            if ( isChange) sb.append(" onChange = \"whenSelection('change')\"");
            sb.append(" > \r\n");
            if ( isALL) { 
                sb.append("<option value = \"ALL\" > ALL</option > \r\n");
            } else if ( isChange) { 
                if ( selname.indexOf("year") == -1)
                    sb.append("<option value = \"----\" >== ���� == </option > \r\n");
            }

            while ( ls.next() ) { 
                ResultSetMetaData meta = ls.getMetaData();
                int columnCount = meta.getColumnCount();

                sb.append("<option value = \"" + ls.getString(1) + "\"");

                if ( optionselected.equals( ls.getString(1))) sb.append(" selected");

                sb.append(" > " + ls.getString(columnCount) + "</option > \r\n");
            }
            sb.append("</select > \r\n");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }

        return sb.toString();
    }    

   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //     
   /***************************������ʹ� �ڵ� SELECT OPTION �±� ��n�1� ��!**********************/
   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //    
}



