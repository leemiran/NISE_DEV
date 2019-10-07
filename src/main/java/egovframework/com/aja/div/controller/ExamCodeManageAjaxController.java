package egovframework.com.aja.div.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.com.aja.service.CommonAjaxManageService;

/**
 * 세부평가 리스트 
 */
@Controller
public class ExamCodeManageAjaxController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(ExamCodeManageAjaxController.class);
    
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
     * 기수  >> 세부평가항목 리스트
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return modelAndView
     * @exception Exception
     */	
    @RequestMapping(value="/com/aja/div/comExamCodeList.do")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {	

    	Object result = null;
		
		ModelAndView modelAndView = new ModelAndView();
		result = commonAjaxManageService.selectCommonAjaxManageList(commandMap,"scoDivDAO.selectScoExamCodeList");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");

		return modelAndView;
    }
    
}