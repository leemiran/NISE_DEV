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
import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import egovframework.adm.com.meu.service.MenuManageService;

import java.io.PrintWriter;

@SuppressWarnings("unchecked")
public class ComMenuManageAspect {
	
	 private static Log log = LogFactory.getLog(ComMenuManageAspect.class);

	/** MenuManageService */
	@Resource(name = "menuManageService")
	private MenuManageService menuManageService;
	


	
	
	@SuppressWarnings("finally")
	public Object aroundMenuController(ProceedingJoinPoint joinPoint) throws Throwable {

		Object retValue = joinPoint.proceed();
		
		log.info("joinPoint.proceed() : " + retValue);
		
		//검색액션인경우는 패스한다..
		if(retValue != null && retValue.equals("adm/sch/admSearchBars")) return retValue;
		
		if ((retValue != null) && (retValue.toString().indexOf("forward:") < 0)){                                                                                                                   
			
			try {
				Object[] inObj = joinPoint.getArgs();
				inObj = joinPoint.getArgs();
				
				Map request = null;
				
				for (int i = 0; i < inObj.length; i++) {
					
					Iterator iterator = null;
					
					if (inObj[i] instanceof Map && !(inObj[i] instanceof ModelMap)) {
						request = (Map) inObj[i];
					}
				}
				if (request != null) {
					
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
					
//					while (senumeration.hasMoreElements()) {
//						String skey = (String) senumeration.nextElement();
//						System.out.println("session.getAttribute("+skey+") : "+session.getAttribute(skey));
//					}
					
					//System.out.println("request gadmin ------> "+request.get("gadmin"));
					//System.out.println("session gadmin ------> "+session.getAttribute("gadmin"));
					
					//아이디 잃었을시					
					if ( null == session.getAttribute("userid") 
							|| ((String) session.getAttribute("userid")).equals("")
							|| null == session.getAttribute("gadmin") 
							|| ((String) session.getAttribute("gadmin")).equals("")
							|| "ZZ".equals((String)session.getAttribute("gadmin"))
						) {
						
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
						html.append("<BODY onload=\"alert('개인정보보호를 위하여 아래의 경우\\n자동 로그아웃되니 재로그인 하여 주십시오.\\n(60분이상 미사용등).');if(opener){opener.document.location.replace('/');window.close();} else{ document.redirectForm.submit(); }\">"+"\n");
						html.append("<FORM name='redirectForm' id='redirectForm' method='post' action='http://iedu.nise.go.kr/'>"+"\n");
						html.append("</FORM>"+"\n");
						html.append("</BODY>"+"\n");
						html.append("</HTML>"+"\n");
						
						out.print(html.toString());
						out.flush();
						out.close();
						
					}else{		
						
						Object[] obj = joinPoint.getArgs();
						
						for (int i = 0; i < obj.length; i++) {
							
							if (obj[i] instanceof Model) {
								inputMap = (Map) obj[i];
								model = (Model) obj[i];
								//메뉴리스트
								List list_admTopMenu = menuManageService.selectAdmTopMenuList(inputMap);
								model.addAttribute("list_admTopMenu", list_admTopMenu);
								List list_admLeftMenu = new ArrayList();
								if( inputMap.get("s_menu") != null ){
									list_admLeftMenu = menuManageService.selectAdmLeftMenuList(inputMap);
								}
								model.addAttribute("list_admLeftMenu", list_admLeftMenu);
								
								Map titleInfo = menuManageService.getTitleInfo(inputMap);
								model.addAttribute("titleInfo", titleInfo);
								
							}
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
			 StringBuffer htmlTemp = new StringBuffer();	
			 String resultMsg = "";
			 String isClose = "";
				
		     if (model != null) {  
		    	 Set<Map.Entry<String, Object>> s1 = model.entrySet(); //이 맵에 포함되는 맵 Set 뷰를 돌려줌
		    	    for (Map.Entry<String, Object> me : s1) {
		    	        
		    	    	if( !(me.getValue()instanceof String[])){
		    	    		if(me.getKey().equals("isOpenerReload")){
		    	    			if("OK".equals(me.getValue()))
		    	    				isClose += " opener.document.location.reload(); ";
		    	    		}
		    	    		
		    	    		if(me.getKey().equals("isClose")){
		    	    			if("OK".equals(me.getValue()))
		    	    				isClose += " window.close(); ";
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
		    	    }
		     }
			     
			StringBuffer html = new StringBuffer();
			html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"  xml:lang=\"ko\" lang=\"ko\">"+"\n");
			html.append("<HEAD>"+"\n");
			html.append("<TITLE>국립특수교육원부설원격교육연수원</TITLE>"+"\n");
			html.append("<base target=\"_self\">"+"\n");
			
			
			
			
			html.append("</HEAD>"+"\n");  
			html.append("<BODY onload=\""+resultMsg+"document.redirectForm.submit(); "+isClose+" \">"+"\n");
			html.append("<FORM name='redirectForm' id='redirectForm' method='post' action='" + viewName + "'>"+"\n");
		    
		    boolean isElement = true;
		     
		    html.append(htmlTemp.toString());
                                                                                                                                     
		    html.append("</FORM>"+"\n");                                                                                                                  
		    html.append("</BODY>"+"\n"); 				
		    html.append("</HTML>"+"\n");
		     
		    out.print(html.toString());
		     
		     
		   } catch (Exception e) {                                                                                                                    
		     e.printStackTrace();                                                                                                                     
		   } finally {                                                                                                                                
		     out.flush();                                                                                                                             
		     out.close();                                                                                                                             
		   }                                                                                                                                          
	}   
    
	public static String stripHTML(String str)throws Exception {
		if (str == null || str.length() == 0) return "&nbsp";

		String tmp = str.toLowerCase();
		
		tmp = tmp.replaceAll("<html(.*|)<body([^>]*)>", "");
	    tmp = tmp.replaceAll("</body(.*)</html>(.*)", "");
	    tmp = tmp.replaceAll("<[/]*(div|layer|body|html|head|meta|form|input|select|textarea|base)[^>]*>", "");
	    tmp = tmp.replaceAll("<(style|script|title|link)(.*)</(style|script|title)>", "");
	    tmp = tmp.replaceAll("<[/]*(script|style|title|xmp)>", "");
	    tmp = tmp.replaceAll("([a-z0-9]*script:)", "deny_$1");
	    tmp = tmp.replaceAll("<(\\?|%)", "<$1");
	    tmp = tmp.replaceAll("(\\?|%)>", "$1>");
	    tmp = tmp.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	    
		return trim(tmp);
	}
	
	public static String trim(String str) throws Exception {
		String result = "";

		if (str != null) result = str.trim();
		return result;
	}


}