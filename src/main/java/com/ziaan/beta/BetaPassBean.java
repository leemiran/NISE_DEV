/**
*베타테스트시스템의 비밀번호 빈
*<p> 제목:BetaPassBean.java</p> 
*<p> 설명:비밀번호 빈</p> 
*<p> Copyright: Copright(c)2004</p> 
*<p> Company: VLC</p> 
*@author 박준현
*@version 1.0
*/
package com.ziaan.beta;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;


public class BetaPassBean { 
  private ConfigSet config;

  public BetaPassBean() { 

  }

/**
    * 사용자의 id가 있는지 검색 
    * @param box          receive from the form object and session
    * @return isId	  검색한 id가 있으면 true를 리턴한다
    */	
  public boolean selectid(RequestBox box) throws Exception { 
	DBConnectionManager connMgr     = null;
    ListSet             ls      = null;
    String              sql     = "";
    DataBox             dbox    = null;
    boolean isId = false;
	String userid = "";

    String v_userid = box.getString("p_userid");
    String v_birth_date = box.getString("p_birth_date");
	  try { 
		connMgr = new DBConnectionManager();
		 sql = "select userid";
		 sql += " from tz_member where userid = " + SQLString.Format(v_userid) ;
		
		 ls = connMgr.executeQuery(sql);
		 while ( ls.next() ) { 
			userid = ls.getString("userid");
			if ( v_userid.equals(userid)) { 
				isId = true;
			}
			
		}
		ls.close();
	  }
		catch ( Exception ex ) { 
		  ErrorManager.getErrorStackTrace(ex, box, sql);
		  throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
		  if ( ls != null ) { 
			try { 
			  ls.close();
			}
			catch ( Exception e ) { }
		  }
		  if ( connMgr != null ) { 
			try { 
			  connMgr.freeConnection();
			}
			catch ( Exception e10 ) { }
		  }
		}
		
		return isId;
	}

	/**
    * 사용자의 주민번호가 맞는지 검색 
    * @param box          receive from the form object and session
    * @return isPwd	  검색한 값이 모두 일치하면 true를 리턴한다
    */	
  public boolean selectpwd(RequestBox box) throws Exception { 
	DBConnectionManager connMgr     = null;
    ListSet             ls      = null;
    String              sql     = "";
    DataBox             dbox    = null;
    boolean isPwd = false;
	String userid = "";
	String birth_date = "";

    String v_userid = box.getString("p_userid");
    String v_birth_date = box.getString("p_birth_date");
	  try { 
		connMgr = new DBConnectionManager();
		sql = "select userid, fn_crypt('2', birth_date, 'knise') birth_date";
		sql += " from tz_member where userid = " + SQLString.Format(v_userid) + " and birth_date = fn_crypt('1', " + SQLString.Format(v_birth_date) + ", 'knise')";

		 ls = connMgr.executeQuery(sql);
		 while ( ls.next() ) { 
			userid = ls.getString("userid");
			birth_date = ls.getString("birth_date");
			if ( v_userid.equals(userid)  && v_birth_date.equals(birth_date)) { 
				isPwd = true;
			}
			
		}
		ls.close();
	  }
		catch ( Exception ex ) { 
		  ErrorManager.getErrorStackTrace(ex, box, sql);
		  throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
		  if ( ls != null ) { 
			try { 
			  ls.close();
			}
			catch ( Exception e ) { }
		  }
		  if ( connMgr != null ) { 
			try { 
			  connMgr.freeConnection();
			}
			catch ( Exception e10 ) { }
		  }
		}
	
		return isPwd;
	}
   /**
    * 검색한 비밀번호를 사용자의 이메일로 발송한다
    * @param box          receive from the form object and session
    * @return isMailed	  메일발송에 성공하면 true를 리턴한다
    */

  public boolean selectPds(RequestBox box) throws Exception { 
    DBConnectionManager connMgr     = null;
    ListSet             ls      = null;
    String              sql     = "";
    DataBox             dbox    = null;
    boolean isMailed = false;

    String v_userid = box.getString("p_userid");
    String v_birth_date = box.getString("p_birth_date");
    String v_mailTitle = "HKMC운영자입니다!";

//// //// //// //// ////  프리메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
    String v_sendhtml = "LossPwd.html";
    FormMail fmail = new FormMail(v_sendhtml);
    MailSet mset = new MailSet(box); //      메일 세팅 및 발송
    mset.setSender(fmail); //  메일보내는 사람 세팅
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

    try { 
      connMgr = new DBConnectionManager();
      sql = "select name, email , fn_crypt('2', pwd, 'knise') pwd , cono";
      sql += " from tz_member where userid = " + SQLString.Format(v_userid) +
          " and birth_date = fn_crypt('1', " + SQLString.Format(v_birth_date) + ", 'knise')";
     
      ls = connMgr.executeQuery(sql);
      while ( ls.next() ) { 

        String v_toCono = ls.getString("cono");
        String v_toEmail = ls.getString("email");
        
        String v_pwd = ls.getString("pwd");
        String v_name = ls.getString("name");
        String v_content = v_name + "님의 비밀번호는 " + v_pwd + "입니다!!";
		fmail.setVariable("userid", v_userid);
        fmail.setVariable("name", ls.getString("name") );
        fmail.setVariable("pwd", ls.getString("pwd") );
        fmail.setVariable("content", v_content);
        String v_mailContent = fmail.getNewMailContent();
       
        isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle,
                                 v_mailContent, "1", v_sendhtml);

      }
      ls.close();


    }
    catch ( Exception ex ) { 
      ErrorManager.getErrorStackTrace(ex, box, sql);
      throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    }
    finally { 
      if ( ls != null ) { 
        try { 
          ls.close();
        }
        catch ( Exception e ) { }
      }
      if ( connMgr != null ) { 
        try { 
          connMgr.freeConnection();
        }
        catch ( Exception e10 ) { }
      }
    }
   
    return isMailed;
  }

}