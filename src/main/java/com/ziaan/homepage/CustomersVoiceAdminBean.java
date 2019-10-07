// **********************************************************
//  1. 제      목: QNA 관리
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
public class CustomersVoiceAdminBean { 
    private ConfigSet config;
    private int row; 
	private String v_gubun = "05";
	
	
    public CustomersVoiceAdminBean() { 
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
    * @return ArrayList   QNA 리스트
    
    */
    public ArrayList selectListContact(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
		String sql1 = "";
        DataBox             dbox    = null;
		
		String v_searchtext = box.getString("p_searchtext");
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
			
            list = new ArrayList();
            
            sql += " SELECT seq, userid, name, email, title, ldate, ismeet \n" +
            		" FROM tz_center_resboard ";
			
			
            if ( !v_searchtext.equals("") ) {
                v_pageno = 1;
                    sql += " WHERE title LIKE " + StringManager.makeSQL("%" + v_searchtext + "%");
            }
            sql += " ORDER BY ldate DESC ";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);             //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();       //     전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();    //     전체 row 수를 반환한다

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
			
            sql  = " SELECT seq, userid, (select handphone from tz_member z where a.userid=z.userid ) send_handphone ,  \n";
            sql += "        (select name from tz_member z where a.tel1=z.userid ) send_name ,  \n "; 
            sql += "        name, email, content, ldate, ismeet, amane, adate, acontent, savefile,realfile, title, tel1, \n ";
            sql += " (select name from tz_member z where a.amane=z.userid) as amanenm \n";
            sql += " FROM tz_center_resboard a WHERE seq = " + SQLString.Format(v_seq) ;
            
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
           
                sql  = " delete from TZ_CENTER_RESBOARD \n";
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


	 /**
    운영자에게 답변하여 저장할때
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateContact(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        
        ListSet rs1   = null;
        //DataBox dbox  = null;

        int v_seq = box.getInt("p_seq");

        String v_acontent = box.getString("p_content");
        String v_act =  box.getString("p_act");
        //String v_answer =  box.getString("p_answer");
        String v_tel1 =  box.getString("p_tel1");

        
        //Vector v_to = box.getVector("p_answer");
        //String v_schecks = "";
		String vv_userid = "";
		//String vv_handphone = "";
		
        //String v_tel1_handphone = "";
		
		/*
		for(int i = 0 ; i < v_to.size() ; i++) {
			v_schecks = (String)v_to.elementAt(i);
						
			StringTokenizer v_token = new StringTokenizer(v_schecks, "|");

			while(v_token.hasMoreTokens()) {
				vv_userid = v_token.nextToken();
				if(v_token.hasMoreTokens()) {
					vv_handphone = v_token.nextToken();
				}
			}        
		}
		*/
        String s_userid   = box.getSession("userid");
        
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = " UPDATE tz_center_resboard SET amane = ? , ismeet = ?,  tel1 = ?,             \n" +
            	   " adate = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), acontent = ? \n" +
//            	   " adate = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), acontent = empty_clob() \n" +
                   " WHERE seq = ? ";
            
            pstmt = connMgr.prepareStatement(sql);
            
            pstmt.setString(1, s_userid);
            pstmt.setString(2, v_act);
            if (  v_act.equals("U")    )           
            { pstmt.setString(3, vv_userid); } 
            else           
            { pstmt.setString(3, v_tel1); } 
            pstmt.setString(4, v_acontent);
            pstmt.setInt(5, v_seq);
            
            isOk = pstmt.executeUpdate();
            
           //String sql2 = "SELECT acontent FROM tz_center_resboard WHERE seq = " + v_seq ;
//            connMgr.setOracleCLOB(sql2, v_acontent);    
            
/*
            String sql3 = "select handphone from tz_member where userid = '" + v_tel1 + "'" ;
            rs1 = connMgr.executeQuery(sql3);
            for ( int i = 0; rs1.next(); i++ ) { 
                dbox = rs1.getDataBox();
                v_tel1_handphone = dbox.getString("d_handphone");
            }

            
            
         if (  v_act.equals("Y")    )
         { 
            box.put("p_mail_code","004");
            FreeMailBean fm = new FreeMailBean();
            fm.amailSendMail(box);
            
            String v_send_handphone = box.getString("p_send_handphone");
            String v_send_userid =  box.getString("pp_userid");
            
    		box.put("p_checks",v_send_userid );
            box.put("p_handphone",v_send_handphone);
            box.put("p_title","[인재개발원장에게 바란다]에 문의 답변 메일 발송했습니다.(KT인재개발원)");
            SmsSendBean ssbean = new SmsSendBean();
            ssbean.sendSms(box);
                       
         }

     	String[]  p_checks_in = new String[3]; 
    	String[]  p_checks_ha = new String[3]; 

         if (  v_act.equals("U")    )  // 인재육성...기획 분리...
         { 
    		box.put("p_checks",vv_userid );
            box.put("p_handphone",vv_handphone);
            box.put("p_title","[인재개발원장에게 바란다]에 답변부탁드립니다.");
            SmsSendBean ssbean = new SmsSendBean();
            ssbean.sendSms(box);
         }  else  if (  v_act.equals("F")    )
         {                              // 인재육성...기획 분리...
        	 
        	 if (v_tel1.equals("885120019") || v_tel1.equals("913033685")  ) {  // 기획이면
            	 p_checks_in[0] =  "865086044"; // 권혁렬
            	 p_checks_in[1] =  "791614034"; // 이돈주
            	 p_checks_in[2] =  "976130033"; // 이민수
            	 
            	 p_checks_ha[0] =  "01098881230";
            	 p_checks_ha[1] =  "01027773650";
            	 p_checks_ha[2] =  "01066555115";
        	 }
        	 else  if ( v_tel1.equals("785064206") || v_tel1.equals("872196137") ||
        			    v_tel1.equals("900020181") || v_tel1.equals("900036079") ||
        			    v_tel1.equals("852308078") || v_tel1.equals("791614034") ||
        			    v_tel1.equals("920010067") || v_tel1.equals("791199070")  )       	 
        	 {
        		 p_checks_in[0] =  "860400107"; // 최병만
            	 p_checks_in[1] =  "791614034"; // 이돈주
            	 p_checks_in[2] =  "976130033";
            	 
            	 p_checks_ha[0] =  "01072760300";
            	 p_checks_ha[1] =  "01027773650";
            	 p_checks_ha[2] =  "01066555115";
        	 }	 
        	 else
        	 {
        		 p_checks_in[0] =  "933280831"; // 최성환
            	 p_checks_in[1] =  "791614034"; // 이돈주
            	 p_checks_in[2] =  "976130033";
            	 
            	 p_checks_ha[0] =  "01073803803";
            	 p_checks_ha[1] =  "01027773650";
            	 p_checks_ha[2] =  "01066555115";
        	 }             
                
        	 System.out.println("p_checks_in[0]:"+p_checks_in[0]);
        	 System.out.println("p_checks_in[1]:"+p_checks_in[1]);
        	 System.out.println("p_checks_in[2]:"+p_checks_in[2]); 
        	  
        	    
         	 box.put("p_checks", p_checks_in);
             box.put("p_handphone", p_checks_ha);
             box.put("p_title","[인재개발원장에게 바란다]에 답변이 등록되었습니다."); 
             SmsSendBean ssbean = new SmsSendBean(); 
             ssbean.sendSms(box);	 
         }              	  
         else  if (  v_act.equals("R")    )
        {   

            box.put("p_checks", v_tel1);
            box.put("p_handphone", v_tel1_handphone);
            box.put("p_title","[인재개발원장에게 바란다]에 답변 재작성부탁드립니다."); 
            SmsSendBean ssbean = new SmsSendBean();
            ssbean.sendSms(box);	 
        }              	  
*/        
            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            connMgr.rollback();
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
        	if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }  
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


	/**
	 * 작성자에게 폼메일 발송
	 * @param box       receive from the form object and session
	 * @return is_Ok    1 : send ok      2 : send fail
	 * @throws Exception
	 */
	public int sendFormMail(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		Connection conn = null;
		ListSet             ls      = null;
		String              sql     = "";
		int is_Ok  = 0;    //  메일발송 성공 여부

		int v_seq = box.getInt("p_seq");
		

		String v_ansid      = box.getSession("p_userid");
		String v_anstitle   = box.getString("p_anstitle");
		String v_anscontent = box.getString("p_anscontent");
		
		try { 
			connMgr = new DBConnectionManager();

			sql  = " select a.seq seq, a.queid queid, a.quetitle quetitle, a.quecontent quecontent, ";
			sql += "        b.cono cono, b.name name, b.email email                 ";
			sql += "   from TZ_CONTACT a, TZ_MEMBER b                                               ";
			sql += "  where a.queid = b.userid                                                      ";
			sql += "    and a.seq  = " + v_seq;

			ls = connMgr.executeQuery(sql);

//// //// //// //// ////  폼메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
			String v_sendhtml = "contactus.html";

			FormMail fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우

			MailSet mset = new MailSet(box);               //      메일 세팅 및 발송


//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

			if ( ls.next() ) { 
                
				
                                 
				String v_queid      = ls.getString("queid");
				String v_quetitle   = ls.getString("quetitle");
				String v_quecontent = ls.getString("quecontent");
				String v_name       =  ls.getString("name");
				String v_toEmail    =  ls.getString("email");
				String v_toCono     =  ls.getString("cono");

				v_quecontent = StringManager.replace(v_quecontent,"\n","<br > ");
				v_anscontent = StringManager.replace(v_anscontent,"\n","<br > ");
				
				String v_mailTitle = v_anstitle;
				
				mset.setSender(fmail);     //  메일보내는 사람 세팅

				fmail.setVariable("quetitle", v_quetitle);
				fmail.setVariable("quecontent", v_quecontent);
				fmail.setVariable("anscontent", v_anscontent);				

				String v_mailContent = fmail.getNewMailContent();

				boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent, "1", v_sendhtml);

				if ( isMailed) is_Ok = 1;
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
		return is_Ok;
	}
}
