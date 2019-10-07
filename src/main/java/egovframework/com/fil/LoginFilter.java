package egovframework.com.fil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

@SuppressWarnings("unchecked")

public class LoginFilter implements Filter {
	
	Logger log = Logger.getLogger(this.getClass());

    private static String ADMIN_LOGIN_PATH = "forward:/adm/lgn/LoginUsr.do";

    public void init(FilterConfig config) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        if (!isLogin(session) && !"Ok".equals(request.getParameter("LoginDo")) ) {
            request.getRequestDispatcher(ADMIN_LOGIN_PATH).forward(request,response);
            
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {}

    private boolean isLogin(HttpSession session) {
        
    	if (session == null) {
            return false;
        }
        
        if (session.getAttribute("sUserId") == null) {
            return false;
        }else{
        	log.info("sUserId : "+session.getAttribute("sUserId"));
        }
        
        return true;
    }
}

