package egovframework.com.aja.cur.controller;

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
public class CurmManageAjaxController {

    /** log */
    protected static final Log LOG = LogFactory.getLog(CurmManageAjaxController.class);

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
     * 현재 등록되어 있는 교육연도
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */

    @RequestMapping(value = "/com/aja/cur/CurmYearSelect1.do")
    public ModelAndView handleRequest(
    		HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> commandMap) throws Exception {
        // int result = 0;
        Map input = new HashMap();
        Object result = null;

        ModelAndView modelAndView = new ModelAndView();

        result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "EduUserDAO.CurmYearSelect1ForAjax");

        Map resultMap = new HashMap();
        resultMap.put("result", result);
        modelAndView.addAllObjects(resultMap);
        modelAndView.setViewName("jsonView");

        return modelAndView;
    }

    /**
     * 현재 등록되어 있는 교육연도2
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */

    @RequestMapping(value = "/com/aja/cur/CurmYearSelect0.do")
    public ModelAndView handleRequest0(
    		HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> commandMap) throws Exception {
        // int result = 0;
        Map input = new HashMap();
        Object result = null;

        ModelAndView modelAndView = new ModelAndView();

        result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "planRegistDAO.selectPlanRegistYearList");

        Map resultMap = new HashMap();
        resultMap.put("TimCom", result);
        modelAndView.addAllObjects(resultMap);
        modelAndView.setViewName("jsonView");

        return modelAndView;
    }

    /**
     * 과정별 교과 시간조회
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */

    @RequestMapping(value = "/com/aja/cur/CurmTimeSelect.do")
    public ModelAndView SelectCurmTime(
    		HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> commandMap) throws Exception {
        // int result = 0;
        Object result = null;

        ModelAndView modelAndView = new ModelAndView();

        result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "TimeTableDAO.SelectComTime");

        Map resultMap = new HashMap();
        resultMap.put("result", result);
        modelAndView.addAllObjects(resultMap);
        modelAndView.setViewName("jsonView");

        return modelAndView;
    }

    /**
     * 강의실 조회
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */

    @RequestMapping(value = "/com/aja/cur/CurmRoomSelect.do")
    public ModelAndView SelectCurmdRoom(
    		HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> commandMap) throws Exception {
        // int result = 0;
        
        Object result = null;

        ModelAndView modelAndView = new ModelAndView();

        result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "TimeTableDAO.SelectCurmRoom");

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

    @RequestMapping(value = "/com/aja/cur/SelectCurmSeqSearch.do") 
    public ModelAndView SelectCurmSeqSearch(
    		HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> commandMap) throws Exception {
        Object result = null;

        ModelAndView modelAndView = new ModelAndView();
        //LOG.info(" select CurmNm : " + request.getParameter("selectCurmNm"));
        System.out.println(" selectCheckOption====================== : " + request.getParameter("selectCheckOption"));
        
        if( request.getParameter("selectCurmNm") != null ){
        	commandMap.put("selectCurmNm", request.getParameter("selectCurmNm"));
        }
        result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "planRegistDAO.selectPlanAjaxList");

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
    
    @RequestMapping(value = "/com/aja/cur/SelectCurmSearch.do")
    public ModelAndView SelectCurmSearch(
    		HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> commandMap) throws Exception {
    	Object result = null;
    	
    	ModelAndView modelAndView = new ModelAndView();
    	//LOG.info(" select CurmNm : " + request.getParameter("selectCurmNm"));
    	if( request.getParameter("selectCurmNm") != null ){
    		commandMap.put("selectCurmNm", request.getParameter("selectCurmNm"));
    	}
    	result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "planRegistDAO.selectCurmAjaxList");
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
    }
}