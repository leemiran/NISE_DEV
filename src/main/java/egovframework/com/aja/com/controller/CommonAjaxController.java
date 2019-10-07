package egovframework.com.aja.com.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;

import egovframework.com.aja.service.CommonAjaxManageService;

/**
 * 중복체크관련 처리
 */
@Controller
public class CommonAjaxController {

    /** log */
    protected static final Log LOG = LogFactory.getLog(CommonAjaxController.class);

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /** EgovMessageSource */
    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    /** AJAX 공통 서비스 */
    @Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;

    /**
     * 메뉴구조 콤보 조회
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/aja/com/selectMenuCombo.do")
    public ModelAndView selectMenuCombo( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	
    	Object result = null;
    	
    	ModelAndView modelAndView = new ModelAndView();
    	if( commandMap.get("mainMenuCode") != null ){
    		result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "commonAjaxDAO.selectDetailMenuCombo");
    	}else{
    		result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "commonAjaxDAO.selectMainMenuCombo");
    	}
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
    }
    
    /**
     * RD프린트 후 로그 저장
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/aja/com/insertPrintLog.do")
    public ModelAndView insertPrintLog( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	
    	Object result = null;
    	
    	ModelAndView modelAndView = new ModelAndView();
    	commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "commonAjaxDAO.insertPrintLog");
    	
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
    }
    
    
    /**
     * 진도 프로시저 맞추기 
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/aja/com/insertProgressFix.do")
    public ModelAndView insertProgressFix( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	
    	int result = 0;
    	
    	ModelAndView modelAndView = new ModelAndView();
    	result = commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "commonAjaxDAO.insertProgressFix");
    	
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	return modelAndView;
    }
    
    /**
     * 나이스 개인번호 중복확인
     * @param request
     * @param response
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/com/aja/mem/nicePersonalNumOverlap.do")
	public ModelAndView nicePersonalNumOverlap(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		boolean isok = commonAjaxManageService.nicePersonalNumOverlap(commandMap, "commonAjaxDAO.nicePersonalNumOverlap");
		
		Map resultMap = new HashMap();
    	resultMap.put("result", isok);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
	}
    
    @RequestMapping(value="/com/aja/mem/nicePersonalChkValue.do")
	public ModelAndView nicePersonalChkValue(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
    	HttpSession session = request.getSession();
		
		String userid = (String) session.getAttribute("userid");

		ModelAndView modelAndView = new ModelAndView();
		
		commandMap.put("userid", userid);
		
		Map view = commonAjaxManageService.nicePersonalChkValue(commandMap);
		
		
		Map resultMap = new HashMap();
    	resultMap.put("result", view);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
	}

    
    /**
     * 출서고사장 맵 주소 불러오기
     * @param request
     * @param response
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/com/aja/svc/schoolAreaMapUrl.do")
	public ModelAndView schoolAreaMapUrl(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		ModelAndView modelAndView = new ModelAndView();
		Object result = null;
		result = commonAjaxManageService.selectCommonAjaxManageMap(commandMap, "codeManageDAO.selectSchoolRoomList");
		
		Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
	}

}