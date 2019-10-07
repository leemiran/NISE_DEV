package egovframework.com.aja.mem.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mortbay.util.ajax.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;

import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.com.aja.mem.service.MemberManageAjaxService;

/**
 * 회원 관련 처리 
 */
@Controller
public class MemberManageAjaxController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(MemberManageAjaxController.class);
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
	/** AJAX 공통 서비스*/
	@Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;
	
	/** AJAX MemberManageAjaxService */
	@Resource(name = "memberManageAjaxService")
    private MemberManageAjaxService memberManageAjaxService;
 
	  /**
     * 비밀번호 초기화
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return 
     * @exception Exception
     */	
    @RequestMapping(value="/com/aja/mem/comPWInitializationForAjax.do")  
    public ModelAndView comPWInitializationForAjax(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {	
 
		Map input = new HashMap();
		Object result = null;
		
		ModelAndView modelAndView = new ModelAndView();
		result = memberManageAjaxService.updatePWInitialization(commandMap);

		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");

		return modelAndView;
    }
    	
}