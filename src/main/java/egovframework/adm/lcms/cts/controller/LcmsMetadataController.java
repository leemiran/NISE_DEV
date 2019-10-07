/* 
 * LcmsMetadataController.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.controller;

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

import egovframework.adm.lcms.cts.service.LcmsMetadataService;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsMetadataController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Controller
public class LcmsMetadataController { 
/** log */ 
protected static final Log LOG = LogFactory.getLog( LcmsMetadataController.class);

/** EgovPropertyService */
@Resource(name = "propertiesService")
protected EgovPropertyService propertiesService;

/** EgovMessageSource */
@Resource(name="egovMessageSource")
EgovMessageSource egovMessageSource;

/** PagingManageController */
@Resource(name = "pagingManageController")
private PagingManageController pagingManageController;

/** lcmsMetadataService */
@Resource(name = "lcmsMetadataService")
 private LcmsMetadataService lcmsMetadataService;

	/**
	 * 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value="/adm/lcms/cts/LcmsMetadataPageList.do")
    public String pageList( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        model.addAllAttributes(commandMap);	
        return "/adm/lcms/cts/LcmsMetadataPageList";
    }
    
    /**
     * 메타데이터 수정팝업
     * @param request
     * @param response
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/adm/lcms/cts/LcmsMetadataUpdatePage.do")
    public String updatePage( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
    	
    	commandMap.put("metaType", "LOM");
    	
    	List metaList = lcmsMetadataService.selectLcmsMetadataElementList(commandMap);
    	
    	model.addAttribute("metaList", metaList);
    	model.addAllAttributes(commandMap);
    	
    	return "/adm/lcms/cts/admLcmsMetadataUpdate";
    }
    
    /**
     * 메타데이터 수정 Update
     * @param requet
     * @param response
     * @param commandMap
     * @param model
     * @return resultMsg
     * @throws Exception
     */
    @RequestMapping(value="/adm/lcms/cts/LcmsMetadataUpdate.do")
    public String update(HttpServletRequest requet, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
    	String resultMsg = "";
    	int result = lcmsMetadataService.updateLcmsMetadataElement(commandMap);
    	
    	if( result > 0 ){
    		resultMsg = egovMessageSource.getMessage("success.common.update");
    	}else{
    		resultMsg = egovMessageSource.getMessage("fail.common.update");
    	}
    	
    	model.addAttribute("resultMsg", resultMsg);
    	model.addAllAttributes(commandMap);
    	
    	return "/adm/lcms/cts/admLcmsMetadataUpdate";
    }


}
