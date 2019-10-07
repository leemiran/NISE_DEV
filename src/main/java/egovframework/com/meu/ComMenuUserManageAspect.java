package egovframework.com.meu;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.util.StopWatch;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.lgn.domain.LoginVO;
//import egovframework.com.sec.ram.security.userdetails.util.EgovUserDetailsHelper;

import java.io.PrintWriter;
import java.lang.annotation.*;

@SuppressWarnings("unchecked")
public class ComMenuUserManageAspect {
	
	//public static Logger log = Logger.getLogger(this.getClass());
	 private static Log log = LogFactory.getLog(ComMenuManageAspect.class);

	
	@SuppressWarnings("finally")
	public Object aroundUserMenuController(ProceedingJoinPoint joinPoint) throws Throwable {
		Object retValue = joinPoint.proceed();
		
	      if ((retValue != null) && (retValue.toString().indexOf("forward:") < 0)){                                                                                                                   
			
			try {
				Object[] inObj = joinPoint.getArgs();
				inObj = joinPoint.getArgs();
				
				Map requestMap = null;
				
				for (int i = 0; i < inObj.length; i++) {
					
					Iterator iterator = null;
					
					if (inObj[i] instanceof Map && !(inObj[i] instanceof ModelMap)) {
						requestMap = (Map) inObj[i];
					}
				}
				if (requestMap != null) {
					
					Model model = null;
					Map inputMap = new HashMap();
	
					
					HttpServletRequest httprequest = null;
					for ( Object o : joinPoint.getArgs() ){
						if ( o instanceof HttpServletRequest ) {
							httprequest = (HttpServletRequest)o;
						}
				    }
					
					HttpSession session = httprequest.getSession();
					
		            Enumeration senumeration = session.getAttributeNames();

		            while (senumeration.hasMoreElements()) {
		                String skey = (String) senumeration.nextElement();
		                //System.out.println("session.getAttribute("+skey+") : "+session.getAttribute(skey));
		            }
					
					//아이디 잃었을시	
		            
		            if(1==2){
						if ( null == session.getAttribute("suserId") || ((String) session.getAttribute("suserId")).equals("")) {
							
							PrintWriter out = null;
							HttpServletResponse response = null;
							StringBuffer html = new StringBuffer();
							
							
							for ( Object o : joinPoint.getArgs() ){
								if ( o instanceof HttpServletResponse ) {
									((HttpServletResponse) o).setContentType("text/html;charset=UTF-8");
									out = ((HttpServletResponse) o).getWriter();
								}
						    }
							
							
							html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"  xml:lang=\"ko\" lang=\"ko\">"+"\n");
							html.append("<HEAD>"+"\n");
							html.append("<TITLE>국립특수교육원부설원격교육연수원</TITLE>"+"\n");
							html.append("</HEAD>"+"\n");
							html.append("<BODY onload=\"alert('개인정보보호를 위하여 아래의 경우\n자동 로그아웃되니 재로그인 하여 주십시오.\n(60분이상 미사용등)');if(opener){ window.close(); } else {document.redirectForm.submit();}\">"+"\n");
							html.append("<FORM name='redirectForm' id='redirectForm' method='post' target='_top' action='http://iedu.nise.go.kr/'>"+"\n");
						    html.append("</FORM>"+"\n");
						    html.append("</BODY>"+"\n");
						    html.append("</HTML>"+"\n");
						     
						    out.print(html.toString());
						    out.flush();
						    out.close();
							
						}else{
						}
		            }
				}
			} catch (Throwable e) {
				throw e;
			} finally {
				return retValue;
			}
		
	      } else {                                                                                                            
		        redirect( "",retValue.toString().replaceAll("forward:", ""),joinPoint);
		        return null;
		  } 

	}
	
    @SuppressWarnings("null")
	public static void redirect( String message, String viewName,ProceedingJoinPoint joinPoint) {          
		   boolean makeLog = true;                                         
		   PrintWriter out = null;  
		   HttpServletResponse response = null;

	
		   try {       
				
				Map model = null;
				
				for ( Object o : joinPoint.getArgs() ){ 
					if ( o instanceof HttpServletResponse ) {
						((HttpServletResponse) o).setContentType("text/html;charset=UTF-8");
						out = ((HttpServletResponse) o).getWriter();
					} 
					
				    if (( o instanceof Map ) && (o instanceof ModelMap)) {
						model = (Map)o;
					} 
			    }
			//response.setContentType("text/html;charset=UTF-8");	
			//out =response.getWriter();
			 StringBuffer htmlTemp = new StringBuffer();	
			 String resultMsg = "";
			 String isClose = "";
				
		     if (model != null) {  
		    	 Set<Map.Entry<String, Object>> s1 = model.entrySet(); //이 맵에 포함되는 맵 Set 뷰를 돌려줌
		    	    for (Map.Entry<String, Object> me : s1) {
		    	        
		    	    	if( !(me.getValue()instanceof String[])){
		    	    		if(me.getKey().equals("isClose")){
			    	    			if("OK".equals(me.getValue()))
			    	    				isClose = " window.close(); ";
			    	    	}
		    	    		
		    	    		if(!me.getKey().equals("resultMsg")){
		    	    			
		    	    			Object temp = "";
		    	    			if(me.getValue() instanceof String ){
			    	    			String strtemp = (String) me.getValue();
			    	    			strtemp = strtemp.replaceAll("'", "");
			    	    			temp = strtemp;
		    	    			}else{
		    	    				Object objtemp =  me.getValue();
		    	    				temp = objtemp;
		    	    			}
		    	    			
		    	    			htmlTemp.append("<input type='hidden' id='" + me.getKey()+ "' name='" +me.getKey()+ "' value='" + temp + "'>"+"\n");
		    	    			
		    	    		}else{
		    	    			
		    	    		String temp = (String)me.getValue();
		    	    		if(temp.indexOf("/n") > -1) {	
		    	    			if(temp.indexOf("//n") < 1){
		    	    				temp.replaceAll("/n", "//n");
		    	    			}
		    	    		}	
		    	    			
		    	    			resultMsg = "alert('"+ temp +"');";
		    	    		}
		    	    	}
		    	    	//System.out.println("<input type='hidden' id='" + me.getKey()+ "' name='" +me.getKey()+ "' value='" + me.getValue() + "'>");
		    	    }
		     }
			     
			StringBuffer html = new StringBuffer();
			html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"  xml:lang=\"ko\" lang=\"ko\">"+"\n");
			html.append("<HEAD>"+"\n");
			html.append("<TITLE>국립특수교육원부설원격교육연수원</TITLE>"+"\n");
			html.append("<base target=\"_self\">"+"\n");
			html.append("</HEAD>"+"\n");  
			html.append("<BODY onload=\""+resultMsg+"document.redirectForm.submit(); "+isClose+" \">"+"\n");
			//html.append("<BODY onload=\""+resultMsg+"doInit();\">"+"\n");
			html.append("<FORM name='redirectForm' id='redirectForm' method='post' action='" + viewName + "'>"+"\n");
			//html.append("<FORM name='redirectForm' id='redirectForm' method='post' >"+"\n");
		    
		    boolean isElement = true;
		     
		    html.append(htmlTemp.toString());
                                                                                                                                     
		    html.append("</FORM>"+"\n");                                                                                                                  
		    html.append("</BODY>"+"\n"); 				
		    html.append("</HTML>"+"\n");
		     
		    //System.out.println(html.toString());
		    out.print(html.toString());
		     
		     
		   } catch (Exception e) {                                                                                                                    
		     e.printStackTrace();                                                                                                                     
		   } finally {                                                                                                                                
		     out.flush();                                                                                                                             
		     out.close();                                                                                                                             
		   }                                                                                                                                          
		 }                                                                                                                                            

}