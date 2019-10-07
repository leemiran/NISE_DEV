// **********************************************************
//  1. ��      ��: Out User BEAN
//  2. ���α׷���: SubjLimitBean.java
//  3. ��      ��: ���ĳ�Ʈ �ܺ� ������ bean
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: 
//  7. ��      ��:
// **********************************************************
package com.ziaan.propose;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

public class SubjLimitBean { 
    private ConfigSet 	config	;
    private int 		row		;
    

    public SubjLimitBean() { 
        try { 
            config 	= new ConfigSet();
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    
    public DataBox selectSubjLimit(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr 		= null;
        ListSet 			ls      		= null;
        ArrayList 			list  			= null;
        String 				sql     		= "";
        DataBox 			dbox 			= null;
        
        try { 
            connMgr 	= new DBConnectionManager();
            list 		= new ArrayList();
            
            sql += " SELECT GUBUN                   ";
            sql += "       , CNT                    ";
            sql += " FROM    Tz_SubjLimit           ";
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
            }
        } catch ( Exception ex ) { 
        	ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return dbox;        
    }
    
    
    /**
    * �����Ͽ� �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
     public int updateSubjLimit(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        
        String  v_gubun             = box.getString("p_gubun");      // ī�װ�   
        String  v_cnt               = box.getString("p_cnt"  );
        int     isOk                = 0;     

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = " update TZ_SubjLimit set gubun = ? , cnt = ?            ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1,  v_gubun);
            pstmt.setString(2,  v_cnt  );

            isOk = pstmt.executeUpdate();
        }
        catch(Exception ex) { 
            connMgr.rollback();         
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql  + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { }   }
            if ( connMgr != null )  { try {     connMgr.freeConnection(); } catch (Exception    e10 ) { }   }
        }
        return isOk;
    }    
}