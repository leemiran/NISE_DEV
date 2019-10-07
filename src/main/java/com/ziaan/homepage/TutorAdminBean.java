// **********************************************************
//  1. 제      목: 튜터
//  2. 프로그램명 : QnaBean.java
//  3. 개      요: QNA 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7.  14
//  7. 수      정:
// **********************************************************
package com.ziaan.homepage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FreeMailBean;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.SmsSendBean;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TutorAdminBean { 
    private ConfigSet config;
    private int row; 
	private String v_gubun = "05";
	
	
    public TutorAdminBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

   

    /**
    * 화면 리스트
    * @param box          receive from the form object and session
    * @return ArrayList    리스트
    
    */
    public ArrayList selectListContact(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
		String              sql1    = "";
        DataBox             dbox    = null;
		
		String v_searchtext = box.getString("p_searchtext");
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
			
            list = new ArrayList();
            
            sql += " SELECT seq, field,  name, birth_date, job, address,   \n";
            sql += "        phone, email, achievement, stu_exp, tutor_exp,  \n";
            sql += "        career, lecture_field, writing, certificate, ldate  \n";
            sql += "  FROM TZ_TUTOR_RECRUIT \n ";
            
            if ( !v_searchtext.equals("") ) {
                v_pageno = 1;
                    sql += " WHERE name LIKE " + StringManager.makeSQL("%" + v_searchtext + "%");
            }

            sql += " ORDER BY ldate DESC ";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);                             //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                     //     현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();        //     전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();        //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
                
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    * 화면 상세보기
    * @param box          receive from the form object and session
    * @return HomePageQnaData     조회한 상세정보
    * @throws Exception
    */
   public DataBox selectViewContact(RequestBox box) throws Exception { 
	   DBConnectionManager	connMgr	= null;
       ListSet             ls      = null;
       String              sql     = "";
		
       DataBox             dbox    = null;

       int    v_seq   = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();
			
            sql =  " SELECT seq, field,  name, birth_date, job, address,   \n";
            sql += "        phone, email, achievement, stu_exp, tutor_exp,  \n";
            sql += "        career, lecture_field, writing, certificate, ldate  \n";
            sql += "  FROM TZ_TUTOR_RECRUIT WHERE seq = " + SQLString.Format(v_seq) ;
            
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
            }   
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }

    /**
    *  삭제할때
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteContact(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;        
		String              sql     = "";
      
        int isOk1 = 1;
        int    v_seq     = box.getInt("p_seq");
		
        try { 
            connMgr = new DBConnectionManager();
           
                sql  = " delete from TZ_TUTOR_RECRUIT  \n";
                sql += "  where seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql);
                pstmt1.setInt(1, v_seq);
			
            isOk1 = pstmt1.executeUpdate();
		 } catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }  
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }            
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1 ;
    }	    


	
}
