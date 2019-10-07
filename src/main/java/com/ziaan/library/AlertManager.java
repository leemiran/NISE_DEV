
package com.ziaan.library;

import java.io.PrintWriter;
import java.util.Enumeration;

 /**
 * <p> ����: Alert �޽������� ���̺귯��</p> 
 * <p> ����: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author ������
 *@date 2003. 12
 *@version 1.0
 */
public class AlertManager { 

    /**
    * ���� or ������ ����� ó���Ǿ��� ��쿡 ������ �޽����� JavaScript�� alert â���� �����ش�.
    @param out  PrintWriter class
    @param msg  �޽���
    @param url  �̵��� ������ URL
    @param box  RequestBox class
    @param isOpenWin  ���� â�� Openwindow �ΰ� ����
    @param isClosed  Openwindow â�� close ���� ����
    @param isHomepage  Homepage���� ����θ� �������� ����
    */
    public void alertOkMessage(PrintWriter out, String msg, String url, RequestBox box, boolean isOpenWin, boolean isClosed, boolean isHomepage) { 
        out.println("<html > <head > <meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\" /><meta http-equiv='Content-Type' content='text/html; charset=euc-kr' > </head > <body>");
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
        out.println("var frm = document.getElementById('form1')");
        
        if ( msg.equals("insert.ok") ) { 
        	//out.println("alert('2222222222222222222222')");
            out.println("alert('��� �Ǿ����ϴ�.')");
            //out.println("alert('222222222222222222222222')");
        }
        else if ( msg.equals("save.ok") ) { 
            out.println("alert('���� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("update.ok") ) { 
            out.println("alert('���� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("delete.ok") ) { 
            out.println("alert('���� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("reply.ok") ) { 
            out.println("alert('��� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("scomodify.ok") ) { 
            out.println("alert('�����Ǿ����ϴ�.')");
        }

        //// //// //// //// ///��Message�߰�//// //// //// //// //// ///
        else if ( msg.equals("confirm.ok") ) { 
            out.println("alert('��û����� ����Ǿ����ϴ�.')");
        }
        else if ( msg.equals("propose.ok") ) { 
            out.println("alert('��û �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("propcancel.ok") ) { 
            out.println("alert('��ҽ�û �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("approvalreport.ok") ) { 
            out.println("alert('������ �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("approval.ok") ) { 
            out.println("alert('����ó�� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("appcancel.ok") ) { 
            out.println("alert('�������ó�� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("forceprop.ok") ) { 
            out.println("alert('�԰�ó�� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("changeseq.ok") ) { 
            out.println("alert('���������� ���εǾ����ϴ�.')");
        }
        else if ( msg.equals("autoassign.ok") ) { 
            out.println("alert('������п� �����Ͽ����ϴ�.')");
        }
        else if ( msg.equals("savechangeseq.ok") ) { 
            out.println("alert('�������� ó�� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("studentinsert.ok") ) { 
            out.println("alert('������ �߰� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("reject.ok") ) { 
            out.println("alert('�ݷ�ó�� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("studentapproval.ok") ) { 
            out.println("alert('����ó�� �Ǿ����ϴ�.')");
        }
        else if ( msg.equals("studentvnogoyong.ok") ) { 
            out.println("alert('����ó�� �Ǿ����ϴ�.')");
        } else if( msg.equals("login.fail")) {
            out.println("alert('�α��� �� �̿����ּ���.');");
        }

        //// //// //// //// ///��Message�߰�//// //// //// //// //// ///

        else if ( msg.equals("mail.ok") ) { 
            String v_mailcnt = box.getString("p_mailcnt");
            out.println("alert('" + v_mailcnt + "���� ������ �߼۵Ǿ����ϴ�.')");
        }
        else if ( !msg.equals("") ) { 
            out.println("alert('" + msg + "')");
        }
        
        /*
        if( msg.equals("login.fail")) {
        	out.println("if(parent.parent) {");
        	out.println("    if(parent.parent.opener) {");
        	out.println("        parent.parent.opener.location.href = '" + url + "';");
        	out.println("        parent.parent.close();");
        	out.println("    } else {");
        	out.println("        parent.parent.location.href = '" + url + "';");
        	out.println("    }");
        	out.println("} else if(parent) {");
        	out.println("    if(parent.parent.opener) {");
        	out.println("        parent.opener.location.href = '" + url + "';");
        	out.println("        parent.close();");
        	out.println("    } else {");
        	out.println("        parent.location.href = '" + url + "';");
        	out.println("    }");
        	out.println("} else if(opener) {");
        	out.println("    opener.location.href = '" + url + "';");
        	out.println("    this.close();");
        	out.println("} else {");
        	out.println("    location.href = '" + url + "';");
        	out.println("}");
        }        
        else 
        */
        if ( !isOpenWin) {       //      openwindow �� �ƴѰ��
        //  System.out.println(box);
            out.println("frm.action = '" + url + "';");
            out.println("frm.submit();");
        }
        else if ( isOpenWin && isClosed && !isHomepage) {        //      openwindow �� close �Ǵ� ���
            out.println("frm.target = window.opener.name;");
            out.println("frm.action = '" + url + "';");
            out.println("frm.submit();");
            out.println("self.close();");
        }
        else if ( isOpenWin && !isClosed) {         //      openwindow �� ��ӿ������ִ� ���
            out.println("frm.target = window.opener.name;");
            out.println("frm.action = '" + url + "';");
            out.println("frm.submit();");
        }
        else if ( isOpenWin && isClosed && isHomepage) {         //      Homepage���� ����θ� ���� ���
            out.println("frm.target = top.window.opener.name;");
            out.println("frm.action = '" + url + "';");
            out.println("frm.submit();");
            out.println("top.self.close();");
        }
        
        out.println("</script > ");
        out.println("</body > ");        
        out.println("</html > ");
        out.flush();
    }

    /**
    * ���� or ������ ����� ó���Ǿ��� ��쿡 ������ �޽����� JavaScript�� alert â���� �����ش�. isOpenWin/isClosed/isHomepage �� ��� false �� ���õȴ�.
    @param out  PrintWriter class
    @param msg  �޽���
    @param url  �̵��� ������ URL
    @param box  RequestBox class
    */
    public void alertOkMessage(PrintWriter out, String msg, String url, RequestBox box) { 
        this.alertOkMessage(out, msg, url, box, false, false, false);
    }

    /**
    * ���� or ������ ����� ó���Ǿ��� ��쿡 ������ �޽����� JavaScript�� alert â���� �����ش�. isHomepage �� ��� false �� ���õȴ�.
    @param out  PrintWriter class
    @param msg  �޽���
    @param url  �̵��� ������ URL
    @param box  RequestBox class
    */
    public void alertOkMessage(PrintWriter out, String msg, String url, RequestBox box, boolean isOpenWin, boolean isClosed) { 
        this.alertOkMessage(out, msg, url, box, isOpenWin, isClosed, false);
    }
    
    public void alertFailMessage(PrintWriter out, String msg, RequestBox box) {
        this.alertFailMessage(out, msg);    	    	
    }

    /**
    * ���� or ������ ó���� �������� ��쿡 ������ �޽����� JavaScript�� alert â���� �����ش�. history.back(-1); �̺�Ʈ �߻�
    @param out  PrintWriter class
    @param msg  �޽���
    */
    public void alertFailMessage(PrintWriter out, String msg) { 
        out.println("<html > ");
        out.println("<head > <meta http-equiv='Content-Type' content='text/html; charset=euc-kr'> <script language = 'javascript' > ");
        if ( msg.equals("insert.fail") ) { 
            out.println("alert('��Ͽ� �����߽��ϴ�.')");
        }
        else if ( msg.equals("save.fail") ) { 
            out.println("alert('���忡 �����߽��ϴ�.')");
        }
        else if ( msg.equals("update.fail") ) { 
            out.println("alert('������ �����߽��ϴ�.')");
        }
        else if ( msg.equals("delete.fail") ) { 
            out.println("alert('������ �����߽��ϴ�.')");
        }
        else if ( msg.equals("reply.fail") ) { 
            out.println("alert('��Ͽ� �����߽��ϴ�.')");
        }


        //// //// //// //// ///Message�߰�//// //// //// //// //// ///
        else if ( msg.equals("confirm.fail") ) { 
            out.println("alert('��û��� ���忡 �����߽��ϴ�!')");
        }
        else if ( msg.equals("propose.fail") ) { 
            out.println("alert('��û�� �����߽��ϴ�!')");
        }
        else if ( msg.equals("propcancel.fail") ) { 
            out.println("alert('��ҽ�û�� �����߽��ϴ�!')");
        }
        else if ( msg.equals("approvalreport.fail") ) { 
            out.println("alert('�����ſ� �����߽��ϴ�!')");
        }
        else if ( msg.equals("approval.fail") ) { 
            out.println("alert('����ó���� �����߽��ϴ�!')");
        }
        else if ( msg.equals("appcancel.fail") ) { 
            out.println("alert('�������ó���� �����߽��ϴ�!')");
        }
        else if ( msg.equals("forceprop.fail") ) { 
            out.println("alert('�����԰�ó���� �����߽��ϴ�!')");
        }
        else if ( msg.equals("changeseq.fail") ) { 
            out.println("alert('���������� ���ο� �����߽��ϴ�!')");
        }
        else if ( msg.equals("autoassign.fail") ) { 
            out.println("alert('������п� �����߽��ϴ�!')");
        }
        else if ( msg.equals("savechangeseq.fail") ) { 
            out.println("alert('�������� ó���� �����߽��ϴ�!')");
        }
        else if ( msg.equals("studentinsert.fail") ) { 
            out.println("alert('������ �߰��� �����߽��ϴ�!')");
        }
        else if ( msg.equals("reject.fail") ) { 
            out.println("alert('�ݷ�ó���� �����߽��ϴ�!')");
        }
        else if ( msg.equals("studentapproval.fail") ) { 
            out.println("alert('����ó���� �����߽��ϴ�.')");
        }
        else if ( msg.equals("studentvnogoyong.fail") ) { 
            out.println("alert('����ó���� �����߽��ϴ�.')");
        }

        //// //// //// //// ///Message�߰�//// //// //// //// //// ///

        else if ( msg.equals("mail.fail") ) { 
            out.println("alert('���Ϲ߼ۿ� �����߽��ϴ�.')");
        }
        else if ( msg.equals("scomodify.fail") ) { 
            out.println("alert('������ ���� �������� �־� ������ �� �����ϴ�.')");
        }
        else if ( msg.equals("scodelete.fail") ) { 
            out.println("alert('���� ���εǾ� �־� ������ �� �����ϴ�.')");
        }
        else if ( msg.equals("scodeletejindo.fail") ) { 
            out.println("alert('������ ���� �������� �־� ������ �� �����ϴ�.')");
        }
        else if ( msg.equals("compcondition.fail") ) { 
            out.println("alert('�����Ͻ� ȸ��� �̹� ��ϵǾ��ֽ��ϴ�.')");
        }
        else if ( msg.equals("blackcondition.fail") ) { 
            out.println("alert('�Է��Ͻ� ������ �̹� ��ϵǾ��ֽ��ϴ�.')");
        }
        else if ( msg.equals("recom.fail") ) { 
            out.println("alert('�̹� ��õ�ϼ̽��ϴ�.')");
        }
        else if ( !msg.equals("") ) { 
            out.println("alert('" + msg + "')");
        }

        out.println("history.back(-1);");
        out.println("</script > ");
        out.println("</head > ");
        out.println("</html > ");        
        out.flush();
    }

     /**
    * �޽����� JavaScript�� alert â���� �����ش�. history.back(-1); �̺�Ʈ �߻�
    @param out  PrintWriter class
    @param msg  �޽���
    */
    public static void historyBack(PrintWriter out, String msg) { 
        out.println("<html > ");
        out.println("<head > ");
        out.println("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr' >");        
        out.println("</head > ");  
        out.println("<body > ");         
        out.println("<script language = 'javascript' > ");           
        out.println("alert('" + msg + "')");
        out.println("history.back(-1);");
        out.println("</script > ");
        out.println("</body > ");        
        out.println("</head > ");
        out.println("</html > ");
        out.flush();
    }

    /**
    * �޽����� JavaScript�� alert â���� �����ش�. self.close(); �̺�Ʈ �߻�
    @param out  PrintWriter class
    @param msg  �޽���
    */
    public void selfClose(PrintWriter out, String msg) { 
        out.println("<html > ");
        out.println("<head > <meta http-equiv='Content-Type' content='text/html; charset=euc-kr' > <script language = 'javascript' > ");

        if ( msg.equals("teamproject.not") ) { 
            out.println("alert('��ϵ� ��������Ʈ�� �����ϴ�.')");
        } else if ( !msg.equals("") ) { 
            out.println("alert('" + msg + "')");
        }
        out.println("self.opener=self;");
        out.println("self.close();");
        out.println("</script > ");
        out.println("</head > ");
        out.println("</html > ");
        out.flush();
    }

    public void confirmMessage(PrintWriter out, String msg, String url, RequestBox box) { 
        out.println("<html > <head > <meta http-equiv='Content-Type' content='text/html; charset=euc-kr' > </head > ");
        out.println("<DIV id='tmp' style='visibility:hidden; display:none' > ");

        out.println("<form name = 'form1' method='post' > ");

        Enumeration e1 = box.keys();
        while ( e1.hasMoreElements() ) { 
            String v_key = (String)e1.nextElement();
            String v_value = box.get(v_key).toString();
            out.println("<input type = 'hidden' name = '" + v_key + "' value = '" + v_value + "' > ");
        }
        out.println("</form > ");
        out.println("</DIV > ");

        out.println("<script language = 'javascript' > ");
        out.println("if ( confirm('" + msg + "')) { ");
        out.println("    document.form1.action = '" + url + "'");
        out.println("    document.form1.submit()");
        out.println("}    ");
        out.println("else { ");
        out.println("    self.close();");
        out.println("}    ");
        out.println("</script > ");
        out.println("</head > ");
        out.println("</html > ");
        out.flush();
    }
    
    public void confirmMessage2(PrintWriter out, String msg, String url, String url2, RequestBox box) { 
        out.println("<html > <head > <meta http-equiv='Content-Type' content='text/html; charset=euc-kr' > </head > ");
        out.println("<DIV id='tmp' style='visibility:hidden; display:none' > ");

        out.println("<form name = 'form1' method='post' > ");

        Enumeration e1 = box.keys();
        while ( e1.hasMoreElements() ) { 
            String v_key = (String)e1.nextElement();
            String v_value = box.get(v_key).toString();
            out.println("<input type = 'hidden' name = '" + v_key + "' value = '" + v_value + "' > ");
        }
        out.println("</form > ");
        out.println("</DIV > ");

        out.println("<script language = 'javascript' > ");
        out.println("if ( confirm('" + msg + "')) { ");
        out.println("    document.form1.action = '" + url2 + "'");
        out.println("    document.form1.submit()");
        out.println("}    ");
        out.println("else { ");
        out.println("    document.form1.action = '" + url + "'");
        out.println("    document.form1.submit()");
        out.println("}    ");
        out.println("</script > ");
        out.println("</head > ");
        out.println("</html > ");
        out.flush();
    }
    
    public void confirmMessage3(PrintWriter out, String msg, String url, RequestBox box) { 
    	out.println("<html > <head > <meta http-equiv='Content-Type' content='text/html; charset=euc-kr' > </head > ");
    	out.println("<DIV id='tmp' style='visibility:hidden; display:none' > ");
    	
    	out.println("<form name = 'form1' method='post' > ");
    	
    	Enumeration e1 = box.keys();
    	while ( e1.hasMoreElements() ) { 
    		String v_key = (String)e1.nextElement();
    		String v_value = box.get(v_key).toString();
    		out.println("<input type = 'hidden' name = '" + v_key + "' value = '" + v_value + "' > ");
    	}
    	out.println("</form > ");
    	out.println("</DIV > ");
    	
    	out.println("<script language = 'javascript' > ");
    	out.println("if ( confirm('" + msg + "')) { ");
    	out.println("   window.opener.top.location.href = '" + url + "'");
    	out.println("   self.close();    ");
    	out.println("}    ");
    	out.println("else { ");
    	out.println("    self.close(); ");
    	out.println("}    ");
    	out.println("</script > ");
    	out.println("</head > ");
    	out.println("</html > ");
    	out.flush();
    }
}

