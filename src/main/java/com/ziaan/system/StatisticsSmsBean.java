// **********************************************************
//  1. 제      목: 인사DB 검색
//  2. 프로그램명: MemberSearchBean.java
//  3. 개      요: 인사DB 검색
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성:  2004. 12. 20
//  7. 수      정:
// **********************************************************

package com.ziaan.system;

import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

public class StatisticsSmsBean {

    // private
    private ConfigSet config;
    private int         row;
    
    public StatisticsSmsBean() {
    }

    public ArrayList selectList(RequestBox box) throws Exception {                                                                                                                                                                                                                                               
        DBConnectionManager connMgr     = null;                                                                                                                                                                                           
        ListSet             ls          = null;                                                                                                                                                                                           
        ArrayList           list        = null;                                                                                                                                                                                           
        DataBox             dbox        = null;                                                                                                                                                                                           
        StringBuffer        sbSQL       = new StringBuffer("");                                                                                                                                                                           
                                                                                                                                                                                                                                          
        String              v_fromdate  = box.getString("p_fromdate").replaceAll("-", "");                                                                                                                                                
        String              v_todate    = box.getString("p_todate"  ).replaceAll("-", "") + "999999";                                                                                                                                     
                                                                                                                                                                                                                                          
        try {                                                                                                                                                                                                                             
            connMgr = new DBConnectionManager();                                                                                                                                                                                          
            list    = new ArrayList();                                                                                                                                                                                                    
                                                                                                                                                                                                                                          
            sbSQL.append(" SELECT SUBSTR(STD_DATE, 1, 8) STD_DATE, SUM(DECODE(Result, '100', 1, 0)) SuccCnt, SUM(DECODE(Result, '100', 0, 1)) FailCnt, COUNT(*) TotCnt  \n")
                 .append(" FROM   MT_FINISHEDT                                                                                                                          \n")
                 .append(" WHERE  SUBSTR(STD_DATE, 1, 8) BETWEEN " + SQLString.Format(v_fromdate) + " AND " + SQLString.Format(v_todate) + "                            \n")
                 .append(" GROUP BY SUBSTR(STD_DATE, 1, 8)                                                                                                              \n");
                                                                                                                                                                                                                                          
            System.out.println(sbSQL.toString());                                                                                                                                                                                         
                                                                                                                                                                                                                                          
            ls = connMgr.executeQuery(sbSQL.toString());                                                                                                                                                                                  
                                                                                                                                                                                                                                          
            while ( ls.next() ) {                                                                                                                                                                                                         
                dbox = ls.getDataBox();                                                                                                                                                                                                   
                list.add(dbox);                                                                                                                                                                                                           
            }                                                                                                                                                                                                                             
        } catch ( Exception ex ) {                                                                                                                                                                                                        
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());                                                                                                                                                                   
            throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );                                                                                                                                                
        } finally {                                                                                                                                                                                                                       
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }                                                                                                                                                           
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }                                                                                                                                      
        }                                                                                                                                                                                                                                 
                                                                                                                                                                                                                                          
        return list;                                                                                                                                                                                                                      
    }                                                                                                                                                                                                                                     
}