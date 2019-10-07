package egovframework.com.utl.fcc.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * Date 관한 Util 테스트를 위한 화면 Controller
 * @author 공통서비스 개발팀 이중호
 * @since 2009.04.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.01  이중호          최초 생성
 *
 * </pre>
 */
@Controller
public class EgovDateUtilController {

    protected final Log logger = LogFactory.getLog(getClass());

    @RequestMapping(value="/utl/fcc/EgovDateUtil.do")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("EgovDateUtil start....");
        Map<String, Object> cmdModel = new HashMap<String, Object>();
        
        // 이동할 JSP
        String jspStr = "";
        // 결과정보
        String resultStr = "";
        // 실행명령어
        String cmdStr = request.getParameter("cmdStr");
        
        if(cmdStr==null||cmdStr.equals("")){
        	cmdStr="";
        }


				// 실행명령어에 따른 JSP 할당
				if(cmdStr.equals("ComUtlFccSlrcldLrrCnvr1")){ // test 샘플용 경로
					jspStr = "cmm/utl/EgovSlrcldLrrCnvr";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					}
				} else if(cmdStr.equals("ComUtlFccSlrcldLrrCnvr2")){ // test 샘플용 경로
					jspStr = "cmm/utl/EgovSlrcldLrrCnvr";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDateValidCeck1")) {
					jspStr = "cmm/utl/EgovDateValidCeck";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDateValidCeck2")) {
					jspStr = "cmm/utl/EgovDateValidCeck";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDateValidCeck3")) {
					jspStr = "cmm/utl/EgovDateValidCeck";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDeRndmCreate")) {
					jspStr = "cmm/utl/EgovDeRndmCreate";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDateCnvr1")) {
					jspStr = "cmm/utl/EgovDateCnvr";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDateCnvr2")) {
					jspStr = "cmm/utl/EgovDateCnvr";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDateCnvr3")) {
					jspStr = "cmm/utl/EgovDateCnvr";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDateCnvr4")) {
					jspStr = "cmm/utl/EgovDateCnvr";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDfkCnvr1")) {
					jspStr = "cmm/utl/EgovDfkCnvr";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index.jsp";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDfkCnvr2")) {
					jspStr = "cmm/utl/EgovDfkCnvr";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index.jsp";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDfkCnvr3")) {
					jspStr = "cmm/utl/EgovDfkCnvr";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index.jsp";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccWeekCalc1")) {
					jspStr = "cmm/utl/EgovWeekCalc";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccWeekCalc2")) {
					jspStr = "cmm/utl/EgovWeekCalc";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccWeekCalc3")) {
					jspStr = "cmm/utl/EgovWeekCalc";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else if (cmdStr.equals("ComUtlFccDeRndmCreate")) {
					jspStr = "cmm/utl/EgovDeRndmCreate";
					try {
						cmdModel.put("resultStr", "UTILITY 직접 호출"); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						jspStr = "/index";
						e.printStackTrace();
					} 
				} else {
					jspStr = "/index";
				}

				logger.info("Returning EgovDateUtil view with cmdStr, resultStr : " + cmdStr +" , "+ resultStr);
				logger.info("EgovDateUtil end....");
				return new ModelAndView(jspStr, "cmdModel", cmdModel);
		}
}