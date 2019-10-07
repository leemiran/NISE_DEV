package egovframework.com.aja.sch.controller;


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

import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;

import egovframework.com.aja.service.CommonAjaxManageService;

/**
 * 중복체크관련 처리
 */
@Controller
public class SearchManageAjaxController {

    /** log */
    protected static final Log log = LogFactory.getLog(SearchManageAjaxController.class);

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
     * 검색 교육기수
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    
    @RequestMapping(value = "/com/aja/sch/selectGrSeqList.do")
    public ModelAndView selectGrSeqList( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	Object result = null;
    	
    	ModelAndView modelAndView = new ModelAndView();
    	if( request.getParameter("searchGyear") != null ){
    		commandMap.put("searchGyear", request.getParameter("searchGyear"));
    	}
    	result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "searchBarsDAO.selectGrSeqAjaxList");
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	return modelAndView;
    }
    
    
    
    /**
     * 검색 과정조회
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    
    @RequestMapping(value = "/com/aja/sch/selectSubjList.do")
    public ModelAndView SelectCurmSearch( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	Object result = null;
    	
    	ModelAndView modelAndView = new ModelAndView();
    	if( request.getParameter("searchSubjnm") != null ){
    		commandMap.put("searchSubjnm", request.getParameter("searchSubjnm"));
    	}
    	result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "searchBarsDAO.selectSubjAjaxList");
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	return modelAndView;
    }
    
    /**
     * 검색 기수조회
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */

    @RequestMapping(value = "/com/aja/sch/selectSubjseqList.do") 
    public ModelAndView SelectCurmSeqSearch( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
        Object result = null;
        ModelAndView modelAndView = new ModelAndView();
        
        if( request.getParameter("searchSubjnm") != null ){
        	commandMap.put("searchSubjnm", request.getParameter("searchSubjnm"));
        }
        result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "searchBarsDAO.selectSubjseqAjaxList");

        Map resultMap = new HashMap();
        resultMap.put("result", result);
        modelAndView.addAllObjects(resultMap);
        modelAndView.setViewName("jsonView");
        return modelAndView;
    }
    
    
    /**
     * 사용자의 과정리스트 조회
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */

    @RequestMapping(value = "/com/aja/sch/selectUserSubjList.do") 
    public ModelAndView selectUserSubjList( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
        Object result = null;
        ModelAndView modelAndView = new ModelAndView();
        
        commandMap.put("searchUserid", request.getParameter("searchUserid"));
        result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "searchBarsDAO.selectUserSubjAjaxListList");

        Map resultMap = new HashMap();
        resultMap.put("result", result);
        modelAndView.addAllObjects(resultMap);
        modelAndView.setViewName("jsonView");
        return modelAndView;
    }
    
    
    /**
     * 검색 교육기관코드 조회
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */

    @RequestMapping(value = "/com/aja/sch/selectEduOrgList.do") 
    public ModelAndView selectEduOrgList( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
        Object result = null;
        ModelAndView modelAndView = new ModelAndView();
        
        if( request.getParameter("searchAgencyCode") != null ){
        	commandMap.put("p_gubun", request.getParameter("searchAgencyCode"));
        }
        result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "codeManageDAO.selectEduOrgList");

        Map resultMap = new HashMap();
        resultMap.put("result", result);
        modelAndView.addAllObjects(resultMap);
        modelAndView.setViewName("jsonView");
        return modelAndView;
    }
}