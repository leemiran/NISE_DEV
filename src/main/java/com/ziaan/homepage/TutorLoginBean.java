// **********************************************************
//  1. 제      목: Correction ADMIN BEAN
//  2. 프로그램명: TutorLoginBean.java
//  3. 개      요: 첨삭관리 관리자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.homepage;
import java.sql.PreparedStatement;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;


public class TutorLoginBean { 

    public TutorLoginBean() { }

    /**********************************************************************
     * 어드민 창 띄우기 로그 : 튜터로그인
     * @param box       receive from the form object and session
     * @return is_Ok    1 : success      2 : fail
     **********************************************************************/
    public int tutorLogin(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String  sql1 = "";
        String  sql2 = "";
        ListSet ls1 = null;
        int  is_Ok = 0;
        int  v_serno = 0;
        
        String v_userid  = box.getSession("userid");
        // String v_userip = box.getString("p_userip");
        String v_userip = box.getSession("userip");

		try { 
           connMgr = new DBConnectionManager();
     
			 sql1  = "select nvl(max(serno),0) as serno from tz_tutorlog where tuserid=" + StringManager.makeSQL(v_userid);
			 ls1 = connMgr.executeQuery(sql1);
			 if ( ls1.next() ) { 
			  v_serno = ls1.getInt(1) + 1;
			 } else { 
			  v_serno = 1;
			 }
			 
			 sql2 =  "insert into tz_tutorlog(tuserid, serno, login,loginip) ";
			 sql2 += " values (?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?) ";
             
			 pstmt = connMgr.prepareStatement(sql2);
			 pstmt.setString(1,  v_userid);
			 pstmt.setInt(2,  v_serno);
			 pstmt.setString(3,  v_userip);
			 
			 is_Ok = pstmt.executeUpdate();   
			// LogDB.insertLog(box, "update", "tz_tutorlog", v_userid + "," +v_serno + "," +v_userip , "튜터로그인");   
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql2);
            throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { }}
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_serno;
    }

    /**********************************************************************
     * 어드민 창 띄우기 로그 : 튜터로그아웃
     * @param box       receive from the form object and session
     * @return is_Ok    1 : success      2 : fail
     **********************************************************************/

    public int tutorLogout(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        String  sql1 = "";
        String  sql2 = "";
        ListSet ls1 = null;
        ListSet ls2 = null;       
        int  is_Ok = 0;
        
        String  v_userid = box.getSession("userid");
        // String  v_userip  = box.getString("p_userip");
        String  v_userip  = box.getSession("userip");
        int  v_serno  = Integer.parseInt(box.getSession("serno") );
        
        try { 
              connMgr = new DBConnectionManager();

              sql1  = " update tz_tutorlog                       ";
              sql1 += " set logout=to_char(sysdate, 'YYYYMMDDHH24MISS') ";
              sql1 += "     , dtime = to_char(sysdate, 'YYYYMMDDHH24MISS')-login ";
              sql1 += " where tuserid  = " + StringManager.makeSQL(v_userid);
              sql1 += "   and serno  = " + v_serno;

			connMgr.executeUpdate(sql1);
			// LogDB.insertLog(box, "update", "tz_tutorlog", v_userid + "," +v_serno , "튜터로그아웃");  
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_serno;
    }

}