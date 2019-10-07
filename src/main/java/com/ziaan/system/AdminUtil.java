// **********************************************************
// 1. ��      ��:
// 2. ���α׷���: AdminUtil.java
// 3. ��      ��:
// 4. ȯ      ��: JDK 1.3
// 5. ��      ��: 0.1
// 6. ��      ��: Administrator 2003-08-07
// 7. ��      ��:
// 
// **********************************************************

package com.ziaan.system;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.ziaan.homepage.LoginBean;
import com.ziaan.library.AlertManager;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class AdminUtil { 
    public static int NOADMIN = 1;
    public static int READ    = 2;
    public static int WRITE   = 4;

    public static int ONLY_READ_PAGE      = 1;
    public static int READPAGE_WITH_WRITE = 2;
    public static int WRITE_ACTION        = 4;

    public static int RETURN_ACTION       = 1;
    public static int CONTINUE_ACTION     = 2;
    public static int READ_BUT_WARINING   = 4;

    private boolean check = false; // true; == > �׽�Ʈ�� ���Ͽ� �ӽ� ����.(����üũ����)

    private AdminUtil(boolean pcheck)  { 
        check = pcheck;  // �ʱ�ȭ ���Ͽ��� �о�� ���� false �� ����üũ ���� �ʴ´�.
    }

    public static AdminUtil getInstance() throws Exception { 
		AdminUtil instance = null;
		instance = new AdminUtil(true);
        
        return instance;
    }

    public int getServletRight(String p_servlet, String p_process, String p_gadmin) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		String            sql   = "";

        int control     = AdminUtil.NOADMIN;
        int servlettype = AdminUtil.ONLY_READ_PAGE;
        int admin_check = AdminUtil.RETURN_ACTION;
        String v_temp   = "";

        try { 
			sql = "select b.servlettype, c.control ";
			sql += "  from tz_menusub          a, ";
			sql += "	      tz_menusubprocess   b, ";
			sql += "	      tz_menuauth         c  ";  
			sql += " where a.grcode = b.grcode  ";
			sql += "   and a.menu   = b.menu    ";
			sql += "   and a.seq    = b.seq     ";  
			sql += "   and a.grcode = c.grcode  "; 
			sql += "   and a.menu   = c.menu    ";
			sql += "   and a.seq    = c.menusubseq ";
			sql += "   and a.servlet = " + SQLString.Format(p_servlet) ;
			sql += "   and b.process = " + SQLString.Format(p_process) ;
			sql += "   and c.gadmin  = " + SQLString.Format(p_gadmin) ;
        	
			connMgr = new DBConnectionManager();
			ls = connMgr.executeQuery(sql);
			while ( ls.next() ) { 
				v_temp = ls.getString("control");
				if ( v_temp.equals("r") ) { 
					control = AdminUtil.READ;
				} else if ( v_temp.equals("rw") ) { 
					control = AdminUtil.WRITE;
				}
				v_temp = StringManager.trim( ls.getString("servlettype") );
                servlettype = Integer.parseInt(v_temp);//       Integer.valueOf(v_temp).intValue();
               
            }

            if ( control == AdminUtil.NOADMIN) { 
                admin_check = AdminUtil.RETURN_ACTION;
            } else if ( control == AdminUtil.READ) { 
                if ( servlettype == AdminUtil.ONLY_READ_PAGE) { 
                    admin_check = AdminUtil.CONTINUE_ACTION;
                } else if ( servlettype == AdminUtil.READPAGE_WITH_WRITE) { 
                    admin_check = AdminUtil.READ_BUT_WARINING;
                } else if ( servlettype == AdminUtil.WRITE_ACTION) { 
                    admin_check = AdminUtil.RETURN_ACTION;
                }
            } else if ( control == AdminUtil.WRITE) { 
                admin_check = AdminUtil.CONTINUE_ACTION;
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls   != null ) { try { ls.close(); } catch ( Exception e10 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

        return admin_check;
    }

    public boolean checkRWRight(String p_servlet, String p_process, PrintWriter out, RequestBox  box) throws Exception { 
        boolean v_check = false;
        String  v_gadmin = "";
		
		try { 
			if ( check == false) { 
				v_check = true;
			} else { 
		        //v_check = checkLoginPopup(out, box);
				v_check = true;
				v_gadmin = box.getSession("gadmin");
				if ( v_check) { 
					int v_servletright = getServletRight(p_servlet, p_process, v_gadmin);
					if ( v_servletright == AdminUtil.RETURN_ACTION) { 
						v_check = false;
						AlertManager.historyBack(out, "��������� �����ϴ�.");
					} else if ( v_servletright == AdminUtil.READ_BUT_WARINING) { 
						v_check = true;
						box.put("p_warnmsg","<font color='red' > ��ȸ�� ���������� �����ư�� ����Ҽ� �����ϴ�.</font > ");
					} else if ( v_servletright == AdminUtil.CONTINUE_ACTION) { 
						v_check = true;
					}
				}
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		}
		return v_check;
    }

    public boolean checkLogin(PrintWriter out, RequestBox  box) throws Exception { 
        boolean v_check = true;
        String  v_userid = "";
        String  v_url  = "";
        String  v_msg  = "";
        v_userid = box.getSession("userid");
		AlertManager alert = new AlertManager();
//        String s_gubun = box.getSession("gubun");
         
        if ( v_userid.equals("") ) { 

            v_check = false;
            /*if("admin".equals(s_gubun)) {
                v_url  = "/ultra/";
            } else {
                v_url  = "/";
            } */
            //v_url = "/login.jsp";
            v_url = "/servlet/controller.homepage.LoginServlet?p_process=loginPage";
            
            v_msg  = "login.fail"; //"�α��� �� �̿����ּ���";
			alert.alertOkMessage(out, v_msg, v_url , box);
        }

        return v_check;
    }

    public boolean checkLoginPopup(PrintWriter out, RequestBox  box) throws Exception { 
        boolean v_check = true;
        String  v_userid = "";
        String  v_url  = "/";
        String  v_msg  = "";
        v_userid = box.getSession("userid");
		AlertManager alert = new AlertManager();
		
        if ( v_userid.equals("") ) { 
 
            v_check = false;
            v_msg  = "login.fail";
			alert.alertOkMessage(out, v_msg, v_url , box,true,true,false);
        }

        return v_check;
    }
    
    public boolean checkLoginPopup2(PrintWriter out, RequestBox  box) throws Exception { 
    	boolean v_check = true;
    	String  v_userid = "";
    	String  v_url  = "/";
    	String  v_msg  = "";
    	v_userid = box.getSession("userid");
    	AlertManager alert = new AlertManager();
    	
    	if ( v_userid.equals("") ) { 
    		out.println("<html > <head > <meta http-equiv='Content-Type' content='text/html; charset=euc-kr' > </head > <body>");
            out.println("<DIV id='tmp' style='visibility:hidden; display:none' > ");

            out.println("<form name='form1' id='form1' method='post' > ");

            Enumeration e1 = box.keys();
            while ( e1.hasMoreElements() ) { 
                String v_key = (String)e1.nextElement();
                String v_value = box.get(v_key).toString();
                out.println("<input type = 'hidden' name = '" + v_key + "' value = '" + v_value + "' > ");
            }
            out.println("</form > ");
            out.println("</DIV > ");

            out.println("<script language = 'javascript' > ");
//            out.println("var frm = document.getElementById('form1')");
    		out.println("alert('��ð� ���콺 �������� ���ų� �� �� ���� ������ ���� �α׾ƿ��Ǿ����ϴ�. �ٽ� �α��� ���ּ���.');");
    		out.println("parent.opener.location='/servlet/controller.homepage.LoginServlet?p_process=loginPage';");
            out.println("parent.close();");
            out.println("</script > ");
            out.println("</body > ");        
            out.println("</html > ");
            out.flush();
    	}
    	
    	return v_check;
    }
    
	public boolean checkLoginHome(PrintWriter out, RequestBox  box) throws Exception { 
		 boolean v_check = true;
		 String  v_userid = "";
		 String v_url  = "";
		 String v_msg  = "";
		 v_userid = box.getSession("userid");

		 AlertManager alert = new AlertManager();
		 
        if ( v_userid.equals("") ) { 

			 v_check = false;

			 v_url  = "/servlet/controller.homepage.LoginServlet?p_process=loginPage";
			 
			 v_msg  = "�α��� �� �̿����ּ���";
			 alert.alertOkMessage(out, v_msg, v_url , box, false, false);
		 }
		 return v_check;
	 }     

	public boolean checkLoginHomePopup(PrintWriter out, RequestBox  box) throws Exception { 
		 boolean v_check = true;
		 String  v_userid = "";
		 String v_url  = "";
		 String v_msg  = "";
		 v_userid = box.getSession("userid");

		 AlertManager alert = new AlertManager();
		 
         if ( v_userid.equals("") ) { 

			 v_check = false;

			 v_url  = "/servlet/controller.homepage.LoginServlet?p_process=loginPopPage";
			 
			 v_msg  = "�α��� �� �̿����ּ���";
			 alert.alertOkMessage(out, v_msg, v_url , box, false, false);
		 }
		 return v_check;
	 }    
	 
	 
    public boolean checkManager(PrintWriter out, RequestBox  box) throws Exception {
        boolean v_check = true;
        String  v_managergubun  = box.getSession("managergubun");
        String  v_gadmin        = box.getSession("gadmin");
        String  v_url           = "";
        String  v_msg           = "";
        AlertManager alert = new AlertManager();
        
        if ( v_gadmin.indexOf("A1") > 0 ) {
            v_check = true;
        } else if (v_managergubun.equals("")) {
            v_check = false;
            v_url  = "/servlet/controller.homepage.MainServlet";
            v_msg  = "��������� �����ϴ�.";
            alert.alertOkMessage(out, v_msg, v_url , box);
        }
        return v_check;
    }    
    
    public boolean checkLoginMentor(PrintWriter out, RequestBox  box) throws Exception { 
        boolean v_check = true;
        String  v_userid = "";
        String  v_url  = "";
        String  v_msg  = "";
        String  v_mentologin = "";        
        v_mentologin = box.getSession("mentorLogin");
		AlertManager alert = new AlertManager();       
        if ( v_mentologin.equals("") ) { 

            v_check = false;
            v_url = "/servlet/controller.mentor.MentorMainServlet?p_process=mentorMain";
            
            v_msg  = "login.fail"; //"�α��� �� �̿����ּ���";
			alert.alertOkMessage(out, v_msg, v_url , box);
        }

        return v_check;
    }
    
    /**
	* zu_Common.jsp ���� �ʿ��Ѱ��� �����´�.
	* @param box          receive from the form object and session
	* @return ArrayList   ���� ����Ʈ
	* @throws Exception
    */
	public static ArrayList checkLimit(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		DataBox             dbox    = null;

		try{
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			
			sql  = " SELECT SEQ, STHH, ENHH, WW, ISUSE, LDATE 					\n";	 
			sql += " ,(SELECT TO_CHAR(SYSDATE,'hh24') FROM DUAL) todayhh		\n";	// ���� ����ð��� ���Ѵ�. 
			sql += " FROM TZ_TIMELIMIT 											\n";
			sql += " WHERE 1=1													\n";
			sql += "   AND ISUSE = 'Y'											\n";
			ls = connMgr.executeQuery(sql);
			
			while ( ls.next() ) { 
	            dbox = ls.getDataBox(); 
	            list.add(dbox);               
	        }
			if ( ls != null ) {ls.close();}
			
		}catch (Exception ex) {
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
