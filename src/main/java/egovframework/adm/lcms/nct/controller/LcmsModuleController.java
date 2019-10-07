/* 
 * LcmsModuleController.java		1.00	2011-10-11 
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
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController; 
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.com.utl.fcc.service.EgovStringUtil;

import egovframework.adm.lcms.cts.service.LcmsOrganizationService;
import egovframework.adm.lcms.nct.service.LcmsModuleService;
import egovframework.adm.lcms.nct.domain.LcmsModule;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsModuleController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


@Controller
public class LcmsModuleController { 
	/** log */ 
	protected static final Log LOG = LogFactory.getLog( LcmsModuleController.class);
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	/** LcmsModule */
	@Resource(name = "lcmsModuleService")
	 private LcmsModuleService lcmsModuleService;
	
	/** lcmsOrganizationService */
	@Resource(name = "lcmsOrganizationService")
	private LcmsOrganizationService lcmsOrganizationService;

    @RequestMapping(value="/adm/lcms/nct/LcmsModulePageList.do")
    public String pageList( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        int totCnt = lcmsModuleService.selectLcmsModulePageListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

        List list = lcmsModuleService.selectLcmsModulePageList(commandMap);
        model.addAttribute("list", list);	
        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsModulePageList";
    }


    @RequestMapping(value="/adm/lcms/nct/LcmsModuleList.do")
    public String list( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        List list = lcmsModuleService.selectLcmsModuleList(commandMap);
        model.addAttribute("list", list);	

        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsModuleList";
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsModuleView.do")
    public String view( 
    HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap(); 
        output.putAll((Map)lcmsModuleService.selectLcmsModule(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsModuleView";
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsModuleInsertForm.do")
    public String insertForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsModuleInsert";
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsModuleInsert.do")
    public String insert( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= "";         String forwardUrl 	= ""; 
        Object output = lcmsModuleService.insertLcmsModule( commandMap);
        commandMap.put("output"," output"); 
        resultMsg = egovMessageSource.getMessage("success.common.insert"); 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/lcms/nct/LcmsModulePageList.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsModuleUdateForm.do")
    public String updateForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap();         output.putAll((Map)lcmsModuleService.selectLcmsModule(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/lcms/nct/LcmsModuleUpdate";
    }
    
    /**
     * Module 수정 팝업
     * @param request
     * @param response
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/adm/lcms/nct/lcmsModuleUpdatePage.do")
    public String updatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
    	
    	Map data = lcmsModuleService.selectLcmsModuleData(commandMap);
    	model.addAttribute("data", data);
    	model.addAllAttributes(commandMap);
    	return "adm/lcms/nct/admLcmsModuleUpdatePopup";
    }

    /**
     * Module update
     * @param request
     * @param response
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/adm/lcms/nct/lcmsModuleUpdate.do")
    public String update(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg = ""; 
        int result = lcmsModuleService.updateLcmsModule( commandMap);
        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.update"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.update"); 
         } 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        return "adm/lcms/nct/admLcmsModuleUpdatePopup"; 
    }

    
    /**
     * 자이닉스 모듈 삭제
     * @param request
     * @param response
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/adm/lcms/nct/LcmsModuleDelete.do")
    public String delete( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 
        
        int check = lcmsOrganizationService.checkCourseMapping(commandMap);

        if( check == 0){
        	String[] module = EgovStringUtil.getStringSequence(commandMap,"chk");
        	commandMap.put("module", module);
        	List filePath = lcmsModuleService.selectModulePath(commandMap);
        	
        	int result = lcmsModuleService.deleteLcmsModule( commandMap);
        	
        	FileController file = new FileController();
        	for( int i=0; i<filePath.size(); i++ ){
        		//선택차시 컨텐츠파일 삭제
        		boolean deleteResult = file.deleteDirector(Globals.CONTNET_REAL_PATH+((Map)filePath.get(i)).get("modulePath"));
        	}
        	if(result > 0){  
        		resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        	}else{ 
        		resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
        	} 
        	
        }else{
        	resultMsg = "이미 사용중인 교과는 수정할 수 없습니다.";
        }

 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        
        return "forward:/adm/xin/xinicsContentList.do"; 
    }

    @RequestMapping(value="/adm/lcms/nct/LcmsModuleDeleteAll.do")
    public String deleteAll( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        commandMap.put("module", EgovStringUtil.getStringSequence(commandMap, "chk")); 
        int result = lcmsModuleService.deleteLcmsModuleAll( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/lcms/nct/LcmsModulePageListForm.do";
        return forwardUrl; 
    }

}
