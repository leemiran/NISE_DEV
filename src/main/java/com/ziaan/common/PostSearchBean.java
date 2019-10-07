// **********************************************************
//  1. ��      ��: �����ȣ �˻� �����ϴ� BEAN
//  2. ���α׷��� : PostSearchBean.java
//  3. ��      ��: �����ȣ �˻��� ó���Ѵ�.
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��:  2003. 7. 7
//  7. ��      ��:
// **********************************************************

package com.ziaan.common;

import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

public class PostSearchBean { 

    public PostSearchBean() { }

    
    /**
    �����ȣ �˻� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   �����ȣ ����Ʈ
    */
    public ArrayList selectPostcodeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        PostSearchData data    = null;
        String  v_dong      = box.getString("p_dong");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select zipcode,sido,gugun,dong,bunji ";
            sql += " from TZ_ZIPCODE ";
            sql += " where dong like '%" + v_dong + "%'";
            sql += " order by sido,gugun,dong,bunji ";

	    ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new PostSearchData();
                data.setZipcode( ls.getString("zipcode") );
                data.setSido( ls.getString("sido") );
                data.setGugun( ls.getString("gugun") );
                data.setDong( ls.getString("dong") );
                data.setBunji( ls.getString("bunji") );
                list.add(data);
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

}