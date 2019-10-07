package egovframework.com.inc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	Logger log = Logger.getLogger(this.getClass());
	
	/**
   	* Login Check
   	* @param HttpServletRequest request, HttpServletResponse response, Object handler
   	* @return boolean
   	* @throws Exception  
   	*/
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		log.info(" Execute Session Inpterceptor - PreHandle ");
		
		HttpSession session = request.getSession();   
		String userId = (String)session.getAttribute("suserId");
		
		if (userId == null) { 
			response.sendRedirect("/adm/lgn/LoginUsr.do");
			log.info("PreHandle | false");
			return false;
		}
		
		
		return true;
	}
}



