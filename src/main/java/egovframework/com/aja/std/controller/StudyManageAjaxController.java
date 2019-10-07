package egovframework.com.aja.std.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.lcms.ims.mainfest.RestructureHandler;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;


import egovframework.com.aja.lcm.AjaxXmlView;
import egovframework.com.aja.service.CommonAjaxManageService;





/**
 * 중복체크관련 처리 
 */
@Controller
public class StudyManageAjaxController {
   
	/** log */
    protected static final Log log = LogFactory.getLog(StudyManageAjaxController.class);
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
	
    /** pagingManageController */
    @Resource(name="pagingManageController")
    PagingManageController pagingManageController;
    
    
	/** AJAX 공통 서비스*/
	@Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;
	

	
    /**
     * 평가정보 등록
     * 
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/aja/std/examQuestionAnswerInsert.do")
    public ModelAndView selectMenuCombo(HttpServletRequest request, HttpServletResponse response, Map commandMap) throws Exception {
    	
    	boolean result = true;
    	
    	Object answer = commonAjaxManageService.selectCommonAjaxManageObject(commandMap, "studyExamDAO.selectExamresultAnswer");
    	int v_examcnt = Integer.valueOf( Integer.valueOf( ( commandMap.get("p_examcnt") == null || commandMap.get("p_examcnt").equals("") ) ? "0" : commandMap.get("p_examcnt")+"" ));
    	int v_qNumber = Integer.valueOf((String)commandMap.get("p_qNumber"));
    	String v_answer = (String)commandMap.get("p_answer");
    	String v_answerStr = "";
    	if( answer != null ){
    		Map m = (Map)answer;
    		String v_answerAlready = m.get("answer") == null ? "" : m.get("answer").toString();
    		String[] v_answerTemp = new String[v_examcnt];
    		
    		String[] v_answerDivision = v_answerAlready.split(",");
            
            for( int x = 0; x < v_answerDivision.length; x++ ){
            	//System.out.println("num= " + x + "   ------  "+ v_answerDivision[x]);
            	v_answerTemp[x] = v_answerDivision[x];
            } 
            
            v_answerTemp[v_qNumber-1] = v_answer;
            
            for( int x = 0; x < v_answerTemp.length; x++ ){
            	v_answerStr += (v_answerTemp[x] == null)?"":v_answerTemp[x];
            	if( x < v_answerTemp.length-1 ){
            		v_answerStr += ",";
            	}
            }
    	}
    	
    	commandMap.put("v_answerStr", v_answerStr);
    	
		commonAjaxManageService.updateCommonAjaxManageInt(commandMap, "studyExamDAO.updateExamresultAnswer");
    	
    	ModelAndView modelAndView = new ModelAndView();
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	return modelAndView;
    }

}