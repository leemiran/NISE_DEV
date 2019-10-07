package egovframework.com.aja.org.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

/**
 * 중복체크관련 처리 
 */
@Controller
public class OrgCodeManageAjaxController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(OrgCodeManageAjaxController.class);
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
	
	/** AJAX 공통 서비스*/
	@Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;
	
	
    /**
     * 기관 코드 중복확인 체크
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return 
     * @exception Exception
     */	
    @RequestMapping(value="/com/aja/org/comOrgCodeChk.do")
    public void chkOrgCodeManage( HttpServletResponse response, HttpServletRequest request, Map<String, Object> commandMap) throws Exception, IOException {
    	
    	int isReady = commonAjaxManageService.selectCommonAjaxManageInt(commandMap,"orgCodeManageDAO.selectOrgCodeManageReady");

    	PrintWriter out = response.getWriter();
    	out.println(isReady);
    	out.close();
    }
   
    /**
     * 기관유형(대)
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return 
     * @exception Exception
     */	
    @RequestMapping(value="/com/aja/org/comLclsCdSelectForAjax.do")  
    public ModelAndView comLclsCdSelectForAjax(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {	
    	//int result = 0;
		Map input = new HashMap();
		Object result = null;
		
		ModelAndView modelAndView = new ModelAndView();
		
		result = commonAjaxManageService.selectCommonAjaxManageList(commandMap,"orgCodeManageDAO.selectComLclsCdSelectForAjax");
		
		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");

		return modelAndView;
    }
    
    /**
     * 기관유형(중)
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return 
     * @exception Exception
     */	
    @RequestMapping(value="/com/aja/org/comMclsCdSelectForAjax.do")  
    public ModelAndView comMclsCdSelectForAjax(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {	
    	//int result = 0;
		Map input = new HashMap();
		Object result = null;
		
		ModelAndView modelAndView = new ModelAndView();
		
		result = commonAjaxManageService.selectCommonAjaxManageList(commandMap,"orgCodeManageDAO.selectComMclsCdSelectForAjax");
		
		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");

		return modelAndView;
    }
    
    /**
     * 기관유형(소)
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return 
     * @exception Exception
     */	
    @RequestMapping(value="/com/aja/org/comSclsCdSelectForAjax.do")  
    public ModelAndView comSclsCdSelectForAjax(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {	
    	//int result = 0;
		Map input = new HashMap();
		Object result = null;
		
		ModelAndView modelAndView = new ModelAndView();
		
		result = commonAjaxManageService.selectCommonAjaxManageList(commandMap,"orgCodeManageDAO.selectComSclsCdSelectForAjax");
		
		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");

		return modelAndView;
    }
    
   
    	
}