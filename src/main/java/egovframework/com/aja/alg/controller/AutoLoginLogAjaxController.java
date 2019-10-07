package egovframework.com.aja.alg.controller;

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

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
/**
 * 중복체크관련 처리 
 */
@Controller
public class AutoLoginLogAjaxController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(AutoLoginLogAjaxController.class);
    
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
     * AutoLogin 로그 저장
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
	
    @RequestMapping(value = "/com/aja/alg/insertAutoLoginLog.do")
    public ModelAndView insertAutoLoginLog( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	
    	Object result = null;
    	
    	ModelAndView modelAndView = new ModelAndView();
    	commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "commonAjaxDAO.insertAutoLoginLog");
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
    } 
   
    	
}