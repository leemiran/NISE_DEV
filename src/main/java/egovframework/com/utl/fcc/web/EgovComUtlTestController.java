/**
 * @Class Name  : EgovComUtlTestController.java
 * @Description : 공통서비스 요소기술 테스트 컨트롤러
 * @Modification Information
 * 
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.02.13       이삼섭                  최초 생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see 
 * 
 *  Copyright (C) 2008 by MOPAS  All right reserved.
 */
package egovframework.com.utl.fcc.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.utl.cas.service.EgovMessageUtil;
import egovframework.com.utl.cas.service.EgovSessionCookieUtil;
import egovframework.com.utl.fcc.service.EgovStringUtil;
//import egovframework.com.utl.fcc.service.EgovUtilTestService;

@Controller
public class EgovComUtlTestController {

    protected final Log logger = LogFactory.getLog(getClass());

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/EgovComUtlTest.do")
    public String handleRequest(
	    @RequestParam("cmdStr") String cmdStr, 
	    @RequestParam("srcString") String srcString,
	    @RequestParam("isCapital") String isCapital,
	    @RequestParam("source") String source,
	    @RequestParam("subject") String subject,
	    @RequestParam("object") String object,
	    @RequestParam("sourceStr") String sourceStr,
	    @RequestParam("compareStr") String compareStr,
	    @RequestParam("searchStr") String searchStr,
	    @RequestParam("strCode") String strCode,
	    @RequestParam("keyStr") String keyStr,
	    @RequestParam("oprStr") String oprStr,
	    @RequestParam("valStr") String valStr,
	    HttpServletRequest request,
	    ModelMap model) throws Exception {
   	
	logger.info("EgovComUtlTestController start....");
	Map cmdModel = new HashMap();
	//EgovUtilTestService utilTestService = new EgovUtilTestService();
	String jspStr = "";
	String resultStr = "";

	if (cmdStr.equals("REQ-COM-139")) {
	    
	    String result = "";
	    if (isCapital.equals("Y")) {
		result = EgovStringUtil.upperCase(srcString);
	    } else {
		result = EgovStringUtil.lowerCase(srcString);
	    }
	    
	    model.addAttribute("result", result);
	    jspStr = "cmm/utl/EgovStringCase";
	    
	} else if (cmdStr.equals("REQ-COM-140")) {
	    
	    cmdModel.put("result", EgovStringUtil.replace(source, subject, object));

	    model.addAttribute("result", EgovStringUtil.replace(source, subject, object));
	    jspStr = "cmm/utl/EgovStringReplace";

	} else if (cmdStr.equals("REQ-COM-141")) {

	    model.addAttribute("result", EgovStringUtil.decode(sourceStr, compareStr, "SAME STRING VALUE", "NOT SAME STING VALUE"));
	    jspStr = "cmm/utl/EgovStringValidation";

	} else if (cmdStr.equals("REQ-COM-142")) {

	    int index = EgovStringUtil.indexOf(source, searchStr);

	    if (index == -1) {
		model.addAttribute("result", "NOT EXIST");
	    } else {
		model.addAttribute("result", "EXIST [" + index + "" + "]");
	    }
	    jspStr = "cmm/utl/EgovStringIndex";

	} else if (cmdStr.equals("REQ-COM-126") || cmdStr.equals("REQ-COM-127") || cmdStr.equals("REQ-COM-128") || cmdStr.equals("REQ-COM-129")) {

	    if (cmdStr.equals("REQ-COM-126")) {
		model.addAttribute("result", EgovMessageUtil.getWarnMsg(strCode));
	    } else if (cmdStr.equals("REQ-COM-127")) {
		model.addAttribute("result", EgovMessageUtil.getErrorMsg(strCode));
	    } else if (cmdStr.equals("REQ-COM-128")) {
		model.addAttribute("result", EgovMessageUtil.getInfoMsg(strCode));
	    } else {
		model.addAttribute("result", EgovMessageUtil.getConfirmMsg(strCode));
	    }
	    jspStr = "cmm/utl/EgovMessage";
	    
	} else if (cmdStr.equals("REQ-COM-133")) {

	    jspStr = "cmm/utl/EgovFileUp";
	    
	} else if (cmdStr.equals("REQ-COM-134")) {

	    jspStr = "cmm/utl/EgovFileDown";
	    
	} else if (cmdStr.equals("REQ-COM-107")) {

	    if ("C".equals(oprStr)) {
		EgovSessionCookieUtil.setSessionAttribute(request, keyStr, valStr);
	    } else if ("D".equals(oprStr)) {
		EgovSessionCookieUtil.removeSessionAttribute(request, keyStr);
	    }
	    String result = EgovSessionCookieUtil.getSessionValuesString(request);

	    //request.setAttribute("result", result);
	    model.addAttribute("result", result);
	    //jspStr = "/jsp/com/utl/EgovSession.jsp";
	    jspStr = "cmm/utl/EgovSession";
	    
	}
	logger.info((new StringBuilder("Returning EgovComUtlTestController view with cmdStr, resultStr : ")).append(cmdStr).append(" , ").append(
		resultStr).toString());
	logger.info("EgovComUtlTestController end....");

	return jspStr;
    }
}
