// **********************************************************
//  1. ��      ��: PROPOSE STATUS ADMIN BEAN
//  2. ���α׷���: EduInfoBean.java
//  3. ��      ��: ��û ��Ȳ ������ bean
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: 
//  7. ��      ��:
// **********************************************************
package com.ziaan.common;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

public class EduInfoBean { 
    private ConfigSet config;
    private int row;
        
    public EduInfoBean() { }

    /**
    ��û��� ����Ʈ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectStduentMemberList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;       
        ArrayList list      = null;
        ArrayList list1     = null;
        DataBox dbox        = null;
        
        String sql1         = "";
        String sql2         = "";

        try { 

        }            
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
        }
        return list1;
    }     

}