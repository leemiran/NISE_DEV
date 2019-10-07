// **********************************************************
//  1. 제      목: 우편번호 검색 제어하는 BEAN
//  2. 프로그램명 : PostSearchBean.java
//  3. 개      요: 우편번호 검색을 처리한다.
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7. 7
//  7. 수      정:
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
    우편번호 검색 리스트
    @param box          receive from the form object and session
    @return ArrayList   우편번호 리스트
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