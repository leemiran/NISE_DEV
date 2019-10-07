// **********************************************************
//  1. ��      ��: SELECT BOX
//  2. ���α׷���: SelectBoxBean.java
//  3. ��      ��: SELECT BOX ���� 
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��:  2004. 1. 30
//  7. ��      ��: ������ 2004. 1. 30
// **********************************************************

package com.ziaan.common;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.SQLString;

/**
 * SELECT BOX ����  Class
 *
 * @date   : 2003. 5
 * @author : J. S. J.
 */
public class SelectBoxBean { 

    public SelectBoxBean() { }
    
    /**
    *  ����Ʈ�ڽ����� (����Ʈ, ����Ʈ�ڽ���,���ð�,�̺�Ʈ��)
    */
    public String getSelectBoxString (ArrayList list, String name, String selected, String event)  { 
        String result = "";

        result = "  <SELECT name=" + name + " " + event + " > \n";

        for ( int i = 0; i < list.size(); i++ ) { 
           DataBox dbox = (DataBox)list.get(i);  
            result += " <option value=" + dbox.getString("d_value");
            if ( selected.equals(dbox.getString("d_value"))) result += " selected ";
            result += " > " + dbox.getString("d_name") + "</option > \n";
        }
        result += "  </SELECT > \n";
        return result;
    }

    public DataBox setAllSelectBox(ListSet ls) throws Exception { 
        DataBox             dbox    = null;
        int columnCount = 0;
        try { 
            dbox = new DataBox("selectbox");

            ResultSetMetaData meta = ls.getMetaData();
            columnCount = meta.getColumnCount();// System.out.println("columnCount : " + columnCount);
            for ( int i = 1; i <= columnCount; i++ ) { 
                String columnName = meta.getColumnName(i).toLowerCase();// System.out.println("columnName : " + columnName);

                dbox.put("d_" + columnName, "ALL");
            }
        }
        catch ( Exception ex ) { ex.printStackTrace();
            throw new Exception("SelectBean.setAllDataBox()\r\n\"" + ex.getMessage() );
        }
        return dbox;
    }


   /**
   * �������� SELECT
   * @param box          receive from the form object and session
   * @return result      SELECT ���� ����
   */
    public static String get_SelectSubjLesson(String name, String selected, String subj) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String result = "";
		SelectBoxBean bean = new SelectBoxBean();
        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select lesson value,  lesson || ' ' ||sdesc name   ";
            sql += "   from tz_subjlesson              ";
            sql += "  where subj = " + SQLString.Format(subj);
            sql += "  order by lesson        ";

            ls = connMgr.executeQuery(sql);

//            dbox = bean.setAllSelectBox( ls);
//            list.add(dbox);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
            result =  bean.getSelectBoxString (list, name, selected, "");
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }


   /**
   * ��Ź������� SELECT
   * @param box          receive from the form object and session
   * @return result      SELECT ���� ����
   */
    public static String get_SelectConsignCom(String name, String selected, String event) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String result = "";
		SelectBoxBean bean = new SelectBoxBean();
        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select companyno value,  companyname name   ";
            sql += "   from tz_consigncom              ";
            // sql += "  where subj = " + SQLString.Format(subj);
            sql += "  order by companyno        ";

            ls = connMgr.executeQuery(sql);

            dbox = bean.setAllSelectBox( ls);
            list.add(dbox);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
            result =  bean.getSelectBoxString (list, name, selected, event);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }
    /**
     * �Ҽӱ���� �����´�.(������)
     * @param name
     * @param code
     * @param selected
     * @param isAll
     * @param event
     * @return
     * @throws Exception
     */
    public static String get_SelectComp(String name, String code, String selected, boolean isAll, String event) throws Exception {
      DBConnectionManager connMgr = null;
      ListSet ls = null;
      ArrayList list = null;
      String sql = "";
      DataBox dbox = null;
      String result = "";

      try {
        SelectBoxBean bean = new SelectBoxBean();
        connMgr = new DBConnectionManager();

        list = new ArrayList();

        sql =""
            +" select comp as value , compnm as name  "
            +" from tz_compclass  "
            //+" where pcode ='"+code+"'  "
            +" order by compnm ";

        ls = connMgr.executeQuery(sql);

        dbox = new DataBox("selectbox");
        if(isAll)
        {
          dbox.put("d_value", "ALL");
          dbox.put("d_name", "-����-");
          list.add(dbox);
        }

        while (ls.next()) {
          dbox = ls.getDataBox();
          list.add(dbox);
        }
        result =  bean.getSelectBoxString (list, name, selected, event);

      }
      catch (Exception ex) {
        ErrorManager.getErrorStackTrace(ex);
        throw new Exception(ex.getMessage());
      }
      finally {
        if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
      }
      return result;
    }
}
