package egovframework.com.sch.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.sch.service.SearchBarsService;

import egovframework.adm.cfg.mem.service.MemberSearchService;


/**
 * 관리자 공통 검색바 사이버/집합 전체
 */
@Controller
public class SearchBarsController {

	/** log */
    protected static final Log log = LogFactory.getLog(SearchBarsController.class);
    
    /** portalActionMainService */
	@Resource(name = "searchBarsService")
	SearchBarsService searchBarsService;
    
	/** MemberSearchService */
	@Resource(name = "memberSearchService")
	MemberSearchService memberSearchService;
	
	/**
     * 관리자 공통 검색바 
     * @param Map commandMap, ModelMap model
     * @return 출력페이지정보 "com/sch/admSearchBars.jsp"
     * @exception Exception
     */	
    @RequestMapping(value="/com/sch/admSearchBars.do")
    public String staEduResultList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
    	
    	response.setHeader("Cache-Control","no-store");   
    	response.setHeader("Pragma","no-cache");   
    	response.setDateHeader("Expires",0);   

    	
    	if(commandMap.get("ses_search_gyear") == null){
    		String v_year  = new SimpleDateFormat("yyyy").format(new Date() );    	
    		commandMap.put("ses_search_gyear", v_year);
    	}
    	
    	
    	List year_list = searchBarsService.selectSearchYearList(commandMap);
    	model.addAttribute("year_list", year_list);
    	
    	List subjectList = memberSearchService.selectSubjectList(commandMap);
		model.addAttribute("subjectList", subjectList);
		
    	
		model.addAllAttributes(commandMap);
		return "com/sch/admSearchBars";
      	
    }
    
   
    
    
}
