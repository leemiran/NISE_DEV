// **********************************************************
//  1. 제      목: SCO Locate Operation Bean
//  2. 프로그램명: SCOLocateBean.java
//  3. 개      요: SCO Locate관리에 관련된 Bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 박미복 2004. 11.17
//  7. 수      정: 박미복 2004. 11.17
// **********************************************************

package com.ziaan.beta;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

public class BetaSCOLocateBean { 

    public BetaSCOLocateBean() { }

    /**
    SCO 리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   Object리스트
    */
    public int SelectNextSCONumber(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        int next_num = 0;
        
        try { 
            connMgr = new DBConnectionManager();

            sql = "select max(sconumber) max_sco from tz_scolocate";

System.out.println(sql);                    
            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
            	next_num = ls.getInt("max_sco");
            }
            next_num++;
        }            
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return next_num;
    }
    
    /**
    Object등록
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail    
    **/
/*    public String InsertObject(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;        
        String              sql     = "";
        int isOk = 0, j=0;   
        
        String  v_luserid   = box.getSession("userid");
        String  v_oid       = "";
        String  results     = "";

        try { 
            connMgr = new DBConnectionManager();
            
            // get Object-ID
            // ms-sql : sql = "select cast(dbo.to_number(isnull(max(oid),'1000000000')) +1  as varchar(10)) oid from tz_object";
            sql = "select ltrim(to_char( to_number(nvl(max(oid),'1000000000')) +1, '0000000000'  )) oid from tz_object";

            try { 
                ls = connMgr.executeQuery(sql);
                ls.next();
                v_oid = ls.getString("oid");
            } catch (Exception se) { 
                v_oid = "1000000001";
            }
            
            // FILE MOVE & UNZIP
            results = this.controlObjectFile("insert",v_oid, box);

            if ( results.equals("OK") ) {                       
            
                // insert Object-Information into DB Table 'TZ_OBJECT' 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                sql = "insert into tz_object "
                + "(oid , otype, filetype, npage, sdesc,master "
                + " ,starting,server,subj,parameterstring,datafromlms "
                + " ,identifier,prerequisites,masteryscore,maxtimeallowed "
                + " ,timelimitaction,sequence,thelevel,luserid,ldate)"     
                + " values "
                + "(" + StringManager.makeSQL(v_oid)
                + "," + StringManager.makeSQL("SCO")
                + "," + StringManager.makeSQL("HTML")
                + "," + box.getInt("p_npage")
                + "," + StringManager.makeSQL(box.getString("p_sdesc"))
                + "," + StringManager.makeSQL(box.getString("p_master"))
                // + "," + StringManager.makeSQL("http:// " +GetCodenm.get_config("eduDomain") +GetCodenm.get_config("object_locate") +v_oid + "/1001.html")
                + "," + StringManager.makeSQL(GetCodenm.get_config("object_locate") +v_oid + "/1001.html")
                + "," + StringManager.makeSQL(box.getString("p_server"))
                + "," + StringManager.makeSQL(box.getString("p_subj")) 
                + "," + StringManager.makeSQL(box.getString("p_parameterstring"))
                + "," + StringManager.makeSQL(box.getString("p_datafromlms"))
                + "," + StringManager.makeSQL(box.getString("p_identifier"))
                + "," + StringManager.makeSQL(box.getString("p_prerequisites"))  
                + "," + box.getInt("p_masteryscore")
                + "," + StringManager.makeSQL(box.getString("p_maxtimeallowed")) 
                + "," + StringManager.makeSQL(box.getString("p_timelimitaction"))
                + "," + box.getInt("p_sequence")
                + "," + box.getInt("p_thelevel")
                + "," + StringManager.makeSQL("v_luserid")        
                + ", to_char(sysdate,'YYYYMMDDHH24MISS') "
                + ")";                                
                isOk = connMgr.executeUpdate(sql);
            }
            
        }
        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("results = " + results + "\r\n" + "sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return results;
    }*/      
}