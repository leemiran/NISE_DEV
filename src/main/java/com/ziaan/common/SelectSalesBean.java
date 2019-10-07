// **********************************************************
//  1. f      ��: ���� ��� SELECT BOX 
//  2. �wα׷���: SelectSalesBean.java
//  3. ��      ��: ���� ��� SELECT BOX 
//  4. ȯ      ��: JDK 1.3
//  5. ��      o: 1.0
//  6. ��      ��: ��d�� 2003. 4. 26
//  7. ��      d: ��d�� 2003. 4. 26
// **********************************************************

package com.ziaan.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

/**
 * ���� ��� SELECT BOX  Class
 *
 * @date   : 2005. 8
 * @author : j.h. lee
 */
public class SelectSalesBean { 

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

        try { 
            String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    // 1�ܰ� ����з�

            connMgr = new DBConnectionManager();

            sql = "select distinct upperclass, classname";
            sql += " from tz_salescode";
            sql += " where middleclass = '000'";
            sql += " and lowerclass = '000'";
            sql += " order by upperclass";

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);

            result += getSelectTag( ls, isChange, isALL, "s_upperclass", ss_upperclass);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
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

        try { 
                String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    //     1�ܰ� ����з�
                String  ss_middleclass   = box.getStringDefault("s_middleclass","ALL");    //       2�ܰ� ����з�
    // System.out.println("ss_middleclass" + ss_middleclass);
                connMgr = new DBConnectionManager();

                sql = "select distinct middleclass, classname";
                sql += " from tz_salescode";
                sql += " where upperclass = " + SQLString.Format(ss_upperclass);
                sql += " and middleclass != '000'";
                sql += " and lowerclass = '000'";
                sql += " order by middleclass";

                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                ls = new ListSet(pstmt);

                result += getSelectTag( ls, isChange, isALL, "s_middleclass", ss_middleclass);

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
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

        try { 
                String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    //     1�ܰ� ����з�
                String  ss_middleclass   = box.getStringDefault("s_middleclass","ALL");    //       2�ܰ� ����з�
                String  ss_lowerclass = box.getStringDefault("s_lowerclass","ALL");    //       3�ܰ� ����з�

                connMgr = new DBConnectionManager();

                sql = "select distinct lowerclass, classname";
                sql += " from tz_salescode";
                sql += " where upperclass = " + SQLString.Format(ss_upperclass);
                sql += " and middleclass = " + SQLString.Format(ss_middleclass);
                sql += " and lowerclass != '000'";
                sql += " order by lowerclass";

                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                ls = new ListSet(pstmt);

                result += getSelectTag( ls, isChange, isALL, "s_lowerclass", ss_lowerclass);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
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
                String  ss_upperclass  = box.getStringDefault("s_upperclass","ALL");    // 1�ܰ� ����з�
                String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");   // 2�ܰ� ����з�
                String  ss_lowerclass  = box.getStringDefault("s_lowerclass","ALL");    // 3�ܰ� ����з�
                String  ss_comp        = box.getStringDefault("s_comp","ALL");          // ȸ��

                connMgr = new DBConnectionManager();

                sql  = " select distinct a.comp, b.compnm   ";
                sql += "  from tz_salesproject a, tz_comp b ";
                sql += " where a.comp = b.comp              ";
                sql += "   and b.comptype ='2'              ";
                sql += " order by a.comp                    ";

                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                ls = new ListSet(pstmt);

                result += getSelectTag( ls, isChange, isALL, "s_comp", ss_comp);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }


   /**
   * ������ SELECT
   * @param box          receive from the form object and session
   * @param isChange     onChage �Լ� ���/��
   * @param isALL        ��ü /��
   * @return result      ������ SELECT BOX ���� ����
   */
    public static String getSuserid(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "������ ";
        boolean isVisible = false;

        try { 
                String  ss_upperclass  = box.getStringDefault("s_upperclass","ALL");    // 1�ܰ� ����з�
                String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");   // 2�ܰ� ����з�
                String  ss_lowerclass  = box.getStringDefault("s_lowerclass","ALL");    // 3�ܰ� ����з�
                String  ss_comp        = box.getStringDefault("s_comp","ALL");          // ȸ��
                String  ss_suserid     = box.getStringDefault("s_suserid","ALL");       // ������

                connMgr = new DBConnectionManager();

                sql  = " select distinct a.suserid, b.name    ";
                sql += "  from tz_salesproject a, tz_member b ";
                sql += " where a.suserid = b.userid           ";
                if ( !ss_comp.equals("ALL")) sql += " and a.comp = " + SQLString.Format(ss_comp);
                sql += " order by a.suserid                   ";

                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                ls = new ListSet(pstmt);

                result += getSelectTag( ls, isChange, isALL, "s_suserid", ss_suserid);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }

   /**
   * �w�gƮ SELECT
   * @param box          receive from the form object and session
   * @param isChange     onChage �Լ� ���/��
   * @param isALL        ��ü /��
   * @return result      �w�gƮ SELECT BOX ���� ����
   */
    public static String getProject(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "�w�gƮ ";
        boolean isVisible = false;

        try { 
                String  ss_project     = box.getStringDefault("s_project","ALL");       // �w�gƮ

                connMgr = new DBConnectionManager();

                sql  = " select distinct project, projectnm   ";
                sql += "  from VZ_SALESPROJECT                ";
                sql += " order by projectnm                   ";

                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                ls = new ListSet(pstmt);

                result += getSelectTag( ls, isChange, isALL, "s_project", ss_project);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }

   /**
   * �⵵ SELECT
   * @param box          receive from the form object and session
   * @param isChange     onChage �Լ� ���/��
   * @param isALL        ��ü /��
   * @return result      �⵵ SELECT BOX ���� ����
   */
    public static String getYear(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "�⵵ ";
        boolean isVisible = false;

        try { 
                String  ss_project_year = box.getStringDefault("s_project_year",FormatDate.getDate("yyyy") );  // �⵵

                connMgr = new DBConnectionManager();

                sql  = " select distinct project_year   ";
                sql += "  from TZ_SALESPLAN             ";
                sql += " union             ";
                sql  = " select distinct project_year   ";
                sql += "  from TZ_SALESACTUAL           ";
                sql += " order by project_year desc     ";

                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                ls = new ListSet(pstmt);

                result += getSelectTag( ls, isChange, isALL, "s_project_year", ss_project_year);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
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
            }
            else if ( isChange) { 
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
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }
        return sb.toString();
    }
}
