package egovframework.com.pag.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 관리자 코드 처리
 */
@Controller
public class PagingManageController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(PagingManageController.class);
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    
    /**
     * 페이징처리를 한다 
     * @param Map commandMap, ModelMap model
     * @exception Exception
     */	
    public void PagingManage( Map<String, Object> commandMap, ModelMap model, int TotCnt) throws Exception { 

    	int currentPageNo;
    	int pageUnit;
    	int pageSize;
    	
    	try{
    		currentPageNo 	= Integer.parseInt((String) commandMap.get("pageIndex"));	
    	} catch (Exception e){
    		currentPageNo 	= 1;
    	}
    	try{
    		pageUnit 		= Integer.parseInt((String) commandMap.get("pageUnit"));
    	} catch (Exception e){
    		pageUnit		= propertiesService.getInt("pageUnit");
    		commandMap.put("pageUnit", pageUnit);
    	}
    	try{
    		pageSize 		= propertiesService.getInt("pageSize");
    	} catch (Exception e){
    		pageSize		= propertiesService.getInt("pageSize");
    	}
    	
    	/* S:PAGEING */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(currentPageNo);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		/* E:PAGEING */
		
		commandMap.put("pageIndex", currentPageNo);
		commandMap.put("firstIndex", paginationInfo.getFirstRecordIndex());
		commandMap.put("lastIndex", paginationInfo.getLastRecordIndex());
		commandMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
		
		int totCnt = TotCnt;
		commandMap.put("pageTotCnt", totCnt);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
    	
		model.addAllAttributes(commandMap);
		
    }
       	
}