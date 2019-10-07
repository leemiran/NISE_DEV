
package com.ziaan.library;

import java.io.PrintWriter;
import java.util.Enumeration;

 /**
 * <p> 제목: Alert 메시지관련 라이브러리</p> 
 * <p> 설명: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author 이정한
 *@date 2003. 12
 *@version 1.0
 */
public class AlertManager { 

    /**
    * 저장 or 삭제시 제대로 처리되었을 경우에 나오는 메시지를 JavaScript의 alert 창으로 보여준다.
    @param out  PrintWriter class
    @param msg  메시지
    @param url  이동할 페이지 URL
    @param box  RequestBox class
    @param isOpenWin  현재 창이 Openwindow 인가 여부
    @param isClosed  Openwindow 창이 close 될지 여부
    @param isHomepage  Homepage에서 운영본부를 열었는지 여부
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
            out.println("alert('등록 되었습니다.')");
            //out.println("alert('222222222222222222222222')");
        }
        else if ( msg.equals("save.ok") ) { 
            out.println("alert('저장 되었습니다.')");
        }
        else if ( msg.equals("update.ok") ) { 
            out.println("alert('수정 되었습니다.')");
        }
        else if ( msg.equals("delete.ok") ) { 
            out.println("alert('삭제 되었습니다.')");
        }
        else if ( msg.equals("reply.ok") ) { 
            out.println("alert('등록 되었습니다.')");
        }
        else if ( msg.equals("scomodify.ok") ) { 
            out.println("alert('수정되었습니다.')");
        }

        //// //// //// //// ///새Message추가//// //// //// //// //// ///
        else if ( msg.equals("confirm.ok") ) { 
            out.println("alert('신청결과가 저장되었습니다.')");
        }
        else if ( msg.equals("propose.ok") ) { 
            out.println("alert('신청 되었습니다.')");
        }
        else if ( msg.equals("propcancel.ok") ) { 
            out.println("alert('취소신청 되었습니다.')");
        }
        else if ( msg.equals("approvalreport.ok") ) { 
            out.println("alert('결재상신 되었습니다.')");
        }
        else if ( msg.equals("approval.ok") ) { 
            out.println("alert('결재처리 되었습니다.')");
        }
        else if ( msg.equals("appcancel.ok") ) { 
            out.println("alert('결재취소처리 되었습니다.')");
        }
        else if ( msg.equals("forceprop.ok") ) { 
            out.println("alert('입과처리 되었습니다.')");
        }
        else if ( msg.equals("changeseq.ok") ) { 
            out.println("alert('차수변경이 승인되었습니다.')");
        }
        else if ( msg.equals("autoassign.ok") ) { 
            out.println("alert('차수배분에 성공하였습니다.')");
        }
        else if ( msg.equals("savechangeseq.ok") ) { 
            out.println("alert('차수변경 처리 되었습니다.')");
        }
        else if ( msg.equals("studentinsert.ok") ) { 
            out.println("alert('교육생 추가 되었습니다.')");
        }
        else if ( msg.equals("reject.ok") ) { 
            out.println("alert('반려처리 되었습니다.')");
        }
        else if ( msg.equals("studentapproval.ok") ) { 
            out.println("alert('승인처리 되었습니다.')");
        }
        else if ( msg.equals("studentvnogoyong.ok") ) { 
            out.println("alert('변경처리 되었습니다.')");
        } else if( msg.equals("login.fail")) {
            out.println("alert('로그인 후 이용해주세요.');");
        }

        //// //// //// //// ///새Message추가//// //// //// //// //// ///

        else if ( msg.equals("mail.ok") ) { 
            String v_mailcnt = box.getString("p_mailcnt");
            out.println("alert('" + v_mailcnt + "명에게 메일이 발송되었습니다.')");
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
        if ( !isOpenWin) {       //      openwindow 가 아닌경우
        //  System.out.println(box);
            out.println("frm.action = '" + url + "';");
            out.println("frm.submit();");
        }
        else if ( isOpenWin && isClosed && !isHomepage) {        //      openwindow 가 close 되는 경우
            out.println("frm.target = window.opener.name;");
            out.println("frm.action = '" + url + "';");
            out.println("frm.submit();");
            out.println("self.close();");
        }
        else if ( isOpenWin && !isClosed) {         //      openwindow 가 계속열려져있는 경우
            out.println("frm.target = window.opener.name;");
            out.println("frm.action = '" + url + "';");
            out.println("frm.submit();");
        }
        else if ( isOpenWin && isClosed && isHomepage) {         //      Homepage에서 운영본부를 열은 경우
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
    * 저장 or 삭제시 제대로 처리되었을 경우에 나오는 메시지를 JavaScript의 alert 창으로 보여준다. isOpenWin/isClosed/isHomepage 가 모두 false 로 세팅된다.
    @param out  PrintWriter class
    @param msg  메시지
    @param url  이동할 페이지 URL
    @param box  RequestBox class
    */
    public void alertOkMessage(PrintWriter out, String msg, String url, RequestBox box) { 
        this.alertOkMessage(out, msg, url, box, false, false, false);
    }

    /**
    * 저장 or 삭제시 제대로 처리되었을 경우에 나오는 메시지를 JavaScript의 alert 창으로 보여준다. isHomepage 가 모두 false 로 세팅된다.
    @param out  PrintWriter class
    @param msg  메시지
    @param url  이동할 페이지 URL
    @param box  RequestBox class
    */
    public void alertOkMessage(PrintWriter out, String msg, String url, RequestBox box, boolean isOpenWin, boolean isClosed) { 
        this.alertOkMessage(out, msg, url, box, isOpenWin, isClosed, false);
    }
    
    public void alertFailMessage(PrintWriter out, String msg, RequestBox box) {
        this.alertFailMessage(out, msg);    	    	
    }

    /**
    * 저장 or 삭제시 처리가 실패했을 경우에 나오는 메시지를 JavaScript의 alert 창으로 보여준다. history.back(-1); 이벤트 발생
    @param out  PrintWriter class
    @param msg  메시지
    */
    public void alertFailMessage(PrintWriter out, String msg) { 
        out.println("<html > ");
        out.println("<head > <meta http-equiv='Content-Type' content='text/html; charset=euc-kr'> <script language = 'javascript' > ");
        if ( msg.equals("insert.fail") ) { 
            out.println("alert('등록에 실패했습니다.')");
        }
        else if ( msg.equals("save.fail") ) { 
            out.println("alert('저장에 실패했습니다.')");
        }
        else if ( msg.equals("update.fail") ) { 
            out.println("alert('수정에 실패했습니다.')");
        }
        else if ( msg.equals("delete.fail") ) { 
            out.println("alert('삭제에 실패했습니다.')");
        }
        else if ( msg.equals("reply.fail") ) { 
            out.println("alert('등록에 실패했습니다.')");
        }


        //// //// //// //// ///Message추가//// //// //// //// //// ///
        else if ( msg.equals("confirm.fail") ) { 
            out.println("alert('신청결과 저장에 실패했습니다!')");
        }
        else if ( msg.equals("propose.fail") ) { 
            out.println("alert('신청에 실패했습니다!')");
        }
        else if ( msg.equals("propcancel.fail") ) { 
            out.println("alert('취소신청에 실패했습니다!')");
        }
        else if ( msg.equals("approvalreport.fail") ) { 
            out.println("alert('결재상신에 실패했습니다!')");
        }
        else if ( msg.equals("approval.fail") ) { 
            out.println("alert('결재처리에 실패했습니다!')");
        }
        else if ( msg.equals("appcancel.fail") ) { 
            out.println("alert('결재취소처리에 실패했습니다!')");
        }
        else if ( msg.equals("forceprop.fail") ) { 
            out.println("alert('직접입과처리에 실패했습니다!')");
        }
        else if ( msg.equals("changeseq.fail") ) { 
            out.println("alert('차수변경이 승인에 실패했습니다!')");
        }
        else if ( msg.equals("autoassign.fail") ) { 
            out.println("alert('차수배분에 실패했습니다!')");
        }
        else if ( msg.equals("savechangeseq.fail") ) { 
            out.println("alert('차수변경 처리에 실패했습니다!')");
        }
        else if ( msg.equals("studentinsert.fail") ) { 
            out.println("alert('교육생 추가에 실패했습니다!')");
        }
        else if ( msg.equals("reject.fail") ) { 
            out.println("alert('반려처리에 실패했습니다!')");
        }
        else if ( msg.equals("studentapproval.fail") ) { 
            out.println("alert('승인처리에 실패했습니다.')");
        }
        else if ( msg.equals("studentvnogoyong.fail") ) { 
            out.println("alert('변경처리에 실패했습니다.')");
        }

        //// //// //// //// ///Message추가//// //// //// //// //// ///

        else if ( msg.equals("mail.fail") ) { 
            out.println("alert('메일발송에 실패했습니다.')");
        }
        else if ( msg.equals("scomodify.fail") ) { 
            out.println("alert('진도를 나간 수강생이 있어 수정할 수 없습니다.')");
        }
        else if ( msg.equals("scodelete.fail") ) { 
            out.println("alert('과목에 매핑되어 있어 삭제할 수 없습니다.')");
        }
        else if ( msg.equals("scodeletejindo.fail") ) { 
            out.println("alert('진도를 나간 수강생이 있어 삭제할 수 없습니다.')");
        }
        else if ( msg.equals("compcondition.fail") ) { 
            out.println("alert('선택하신 회사는 이미 등록되어있습니다.')");
        }
        else if ( msg.equals("blackcondition.fail") ) { 
            out.println("alert('입력하신 내용은 이미 등록되어있습니다.')");
        }
        else if ( msg.equals("recom.fail") ) { 
            out.println("alert('이미 추천하셨습니다.')");
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
    * 메시지를 JavaScript의 alert 창으로 보여준다. history.back(-1); 이벤트 발생
    @param out  PrintWriter class
    @param msg  메시지
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
    * 메시지를 JavaScript의 alert 창으로 보여준다. self.close(); 이벤트 발생
    @param out  PrintWriter class
    @param msg  메시지
    */
    public void selfClose(PrintWriter out, String msg) { 
        out.println("<html > ");
        out.println("<head > <meta http-equiv='Content-Type' content='text/html; charset=euc-kr' > <script language = 'javascript' > ");

        if ( msg.equals("teamproject.not") ) { 
            out.println("alert('등록된 팀프로젝트가 없습니다.')");
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

