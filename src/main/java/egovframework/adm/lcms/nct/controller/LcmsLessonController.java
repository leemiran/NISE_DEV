/* 
 * LcmsLessonController.java		1.00	2011-10-11 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.nct.controller;

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

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController; 
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.com.utl.fcc.service.EgovStringUtil;

import egovframework.adm.lcms.nct.service.LcmsLessonService;
import egovframework.adm.lcms.nct.domain.LcmsLesson;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsLessonController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


@Controller
public class LcmsLessonController { 
/** log */ 
protected static final Log LOG = LogFactory.getLog( LcmsLessonController.class);

/** EgovPropertyService */
@Resource(name = "propertiesService")
protected EgovPropertyService propertiesService;

/** EgovMessageSource */
@Resource(name="egovMessageSource")
EgovMessageSource egovMessageSource;

/** PagingManageController */
@Resource(name = "pagingManageController")
private PagingManageController pagingManageController;

/** LcmsLesson */
@Resource(name = "lcmsLessonService")
 private LcmsLessonService lcmsLessonService;

    @RequestMapping(value="/adm/lcms/nct/LcmsLessonPageList.do")
    public String pageList( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        int totCnt = lcmsLessonService.selectLcmsLessonPageListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

        List list = lcmsLessonService.selectLcmsLessonPageList(commandMap);
        model.addAttribute("list", list);	
        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsLessonPageList";
    }


    @RequestMapping(value="/adm/lcms/nct/LcmsLessonList.do")
    public String list( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        List list = lcmsLessonService.selectLcmsLessonList(commandMap);
        model.addAttribute("list", list);	

        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsLessonList";
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsLessonView.do")
    public String view( 
    HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap(); 
        output.putAll((Map)lcmsLessonService.selectLcmsLesson(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsLessonView";
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsLessonInsertForm.do")
    public String insertForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsLessonInsert";
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsLessonInsert.do")
    public String insert( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= "";         String forwardUrl 	= ""; 
        Object output = lcmsLessonService.insertLcmsLesson( commandMap);
        commandMap.put("output"," output"); 
        resultMsg = egovMessageSource.getMessage("success.common.insert"); 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/lcms/nct/LcmsLessonPageList.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsLessonUdateForm.do")
    public String updateForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap();         output.putAll((Map)lcmsLessonService.selectLcmsLesson(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsLessonUpdate";
    }

    @RequestMapping(value="/adm/lcms/nct/lcmsLessonUpdate.do")
    public String update(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
    	String resultMsg = "";
    	int result = lcmsLessonService.updateLcmsLesson(commandMap);
    	
    	if( result > 0 ){
    		resultMsg = egovMessageSource.getMessage("success.common.update");
    	}else{
    		resultMsg = egovMessageSource.getMessage("fail.common.update");
    	}
    	
    	model.addAttribute("resultMsg", resultMsg);
    	model.addAllAttributes(commandMap);
    	
    	return "adm/lcms/nct/admLcmsLessonUpdatePopup";
    	
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsLessonDelete.do")
    public String delete( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        int result = lcmsLessonService.deleteLcmsLesson( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/lcms/nct/LcmsLessonPageListForm.do";
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsLessonDelete.do")
    public String deleteAll( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        commandMap.put("lesson", EgovStringUtil.getStringSequence(commandMap, "chk")); 
        int result = lcmsLessonService.deleteLcmsLessonAll( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/lcms/nct/LcmsLessonPageListForm.do";
        return forwardUrl; 
    }
    
    
    /**
     * 레슨정보 수정팝업
     * @param request
     * @param response
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/adm/lcms/nct/lcmsLessonUpdatePage.do")
    public String updatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
    	
    	Map data = lcmsLessonService.selectLessonData(commandMap);
    	
    	model.addAttribute("data", data);
    	model.addAllAttributes(commandMap);
    	
    	return "adm/lcms/nct/admLcmsLessonUpdatePopup";
    }
    
}
