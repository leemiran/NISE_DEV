/*
 * @(#)EduProgress.java
 *
 * Copyright(c) 2008, Jin-pil Chung
 * All rights reserved.
 */

package controller.lcms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ziaan.lcms.EduAuthBean;
import com.ziaan.lcms.EduLogBean;
import com.ziaan.lcms.EduProgressBean;
import com.ziaan.lcms.EduSession;
import com.ziaan.lcms.MasterformConfigBean;
import com.ziaan.library.AlertManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.RequestBox;
import com.ziaan.library.RequestManager;
import com.ziaan.system.AdminUtil;

/**
 * 진도체크
 * 
 * @version		1.0, 2008/11/20
 * @author		Chung Jin-pil
 */
public class EduProgress extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet
{
	private Logger logger = Logger.getLogger(this.getClass());
	
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException,
            java.io.IOException
    {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException,
            java.io.IOException
    {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");	

        PrintWriter out = response.getWriter();
        RequestBox box = RequestManager.getBox(request);
        
        String v_process = box.getString("p_process");

        try
        {        
            if (ErrorManager.isErrorMessageView())
            {
                box.put("error.out", out);
            }
            /*
    		if ( !AdminUtil.getInstance().checkLoginPopup(out, box)) { 
				return;
			}
			*/

			if (v_process.equals("eduCheck")) {					// 진도체크 
				this.eduCheck(request, response, box, out);
			} else if (v_process.equals("eduExitAx")) { 		// 학습창 종료체크 Ajax
				this.eduExitAx(request, response, box, out);
	        } else if (v_process.equals("eduExit")) { 			// 학습창 종료체크 Form
	        	this.eduExit(request, response, box, out);
	        } else if (v_process.equals("delBetaProgress")) {	// 베타테스트 진도삭제
	        	this.delBetaProgress(request, response, box, out);
	        }
        }
        catch (Exception ex)
        {
            ErrorManager.getErrorStackTrace(ex, out);
        }
    }

    /**
     * 베타테스트 진도삭제
     * 
     * @param request
     * @param response
     * @param box
     * @param out
     */
    private void delBetaProgress(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
    	try {
    		
    		EduProgressBean eduProgressBean = new EduProgressBean();
    		boolean isOk = eduProgressBean.delBetaProgress(box);
    		
            AlertManager alert = new AlertManager();
            String v_msg = "";
            if ( isOk ) { 
                v_msg = "delete.ok";
                String forwardURL = "/servlet/controller.lcms.ContentsServlet?p_process=listPage";
                alert.alertOkMessage(out, v_msg, forwardURL, box);
            } else { 
                v_msg = "delete.fail";
                alert.alertFailMessage(out, v_msg);
            }
    		
    	} catch (Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, out);
    		throw new Exception("eduExit()\r\n" + ex.getMessage());
    	}    	
	}

	/**
     * 학습창 접속 종료 로그
     * 
     * @param request
     * @param response
     * @param box
     * @param out
     * @throws Exception
     */
    private void eduExit(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
    	try {
    		boolean result = false;
    		
    		if("Y".equals(box.getSession("s_review"))){
    			
    		}else{
    		
	    		EduLogBean eduLogBean = new EduLogBean();
	    		if ( EduSession.getEduCheckAuth(box) == EduAuthBean.EDU_AUTH_AUTHORIZED ) {
	    			result = eduLogBean.eduExitLog(box);
	    		}
	    		
    		}

    		logger.debug( "종료 체크 결과 : " + result );
    	} catch (Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, out);
    		throw new Exception("eduExit()\r\n" + ex.getMessage());
    	}
    }
    
    /**
     * 학습창 접속 종료 로그
     * 
     * @param request
     * @param response
     * @param box
     * @param out
     * @throws Exception
     */
    private void eduExitAx(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
    	String result = "FALSE";
    	
		try {
			
			EduLogBean eduLogBean = new EduLogBean();
			
			if("Y".equals(box.getSession("s_review"))){
				result = "TRUE";
			}else{
			
				if ( EduSession.getEduCheckAuth(box) == EduAuthBean.EDU_AUTH_AUTHORIZED ) {
					result = eduLogBean.eduExitLog(box) ? "TRUE":"FALSE";
				} else {
					result = "FALSE";
				}
				
			}

		} catch (Exception ex) {
			result = "FALSE";
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("eduExitAx()\r\n" + ex.getMessage());
		}
		
		out.print(new Boolean(result).toString());
	}

    /**
     * 진도체크
     * 
     * @param request
     * @param response
     * @param box
     * @param out
     * @throws Exception
     */
	private void eduCheck(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
		
			//System.out.println( "userid:" +  box.getSession("userid"));
			String v_url = "/lcmsN/learn/user/lcms/z_EduChk_after.jsp";
			if( box.getSession("userid") == null || "".equals(box.getSession("userid")) ){
				forwardUrl(request, response, "/lcmsN/learn/user/lcms/z_EduChk_logout.jsp");
			}
			
			EduProgressBean epb = new EduProgressBean();
			String eduAuth = EduSession.getEduCheckAuth(box);
			box.put("realPath", request.getRealPath("/"));
			if("Y".equals(box.getSession("s_review"))){
				v_url += "?p_check=true";
				forwardUrl(request, response, v_url);
			}else{
				
				boolean result = false;
				if ( eduAuth == EduAuthBean.EDU_AUTH_AUTHORIZED ) {
					result = epb.eduCheck(box);
				} else if ( eduAuth == EduAuthBean.EDU_AUTH_BETATEST ) {
					result = epb.eduCheckForBetatest(box);
				} else {
					result = true;
				}
	
				
				if ( !result ) {
					v_url += "?p_check=false";
//					String message = epb.getEduCheckMessage();
//					
//					AlertManager alert = new AlertManager();
//					alert.alertOkMessage(out, message, v_url, box);
					forwardUrl(request, response, v_url);
				} else {
					v_url += "?p_check=true";
//					forwardUrl(request, response, v_url);
					forwardUrl(request, response, v_url);
				}
				
			}
			
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("eduCheck()\r\n" + ex.getMessage());
		}
	}

    private void forwardUrl(HttpServletRequest request, HttpServletResponse response, String forwardUrl)
            throws ServletException, IOException
    {
        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(forwardUrl);
        rd.forward(request, response);
    }
}
