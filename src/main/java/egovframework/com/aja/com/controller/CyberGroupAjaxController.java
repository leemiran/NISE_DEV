package egovframework.com.aja.com.controller;

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

/**
 * 공통조회 처리 
 */
@Controller
public class CyberGroupAjaxController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(CyberGroupAjaxController.class);
    
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
     * 교육훈련계획년도 조회
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return 
     * @exception Exception
     */	
    @RequestMapping(value="/com/aja/com/planRegistYearSelectForAjax.do")  
    public ModelAndView comPlanRegistYearSelectForAjax(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {	
    	//int result = 0;
		Map input = new HashMap();
		Object result = null;
		
		ModelAndView modelAndView = new ModelAndView();
		result = commonAjaxManageService.selectCommonAjaxManageList(commandMap,"planRegistDAO.selectPlanRegistYearList");
		
		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");

		return modelAndView;
    }
    
    /**
     * 사이버 과정그룹 선택
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return 
     * @exception Exception
     */	
    @RequestMapping(value="/com/aja/com/cyberGrpSelectForAjax.do")  
    public ModelAndView comCyberGrpSelectForAjax(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {	
    	//int result = 0;
		Map input = new HashMap();
		Object result = null;
		
		ModelAndView modelAndView = new ModelAndView();
		result = commonAjaxManageService.selectCommonAjaxManageList(commandMap,"courseClassDAO.selectGrpAjaxManageList99");
		
		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");

		return modelAndView;
    }
    	
}