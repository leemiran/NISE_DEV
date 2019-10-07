/* 
 * LcmsItemResourceController.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsItemResourceService;
import egovframework.adm.lcms.cts.domain.LcmsItemResource;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsItemResourceController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Controller
public class LcmsItemResourceController { 
/** log */ 
protected static final Log LOG = LogFactory.getLog( LcmsItemResourceController.class);

/** EgovPropertyService */
@Resource(name = "propertiesService")
protected EgovPropertyService propertiesService;

/** EgovMessageSource */
@Resource(name="egovMessageSource")
EgovMessageSource egovMessageSource;

/** PagingManageController */
@Resource(name = "pagingManageController")
private PagingManageController pagingManageController;

/** CodeManageService */
@Resource(name = "lcmsItemResourceService")
 private LcmsItemResourceService lcmsItemResourceService;

    @RequestMapping(value="/adm/cts/LcmsItemResourcePageList.do")
    public String pageList( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        int totCnt = lcmsItemResourceService.selectLcmsItemResourcePageListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

        List list = lcmsItemResourceService.selectLcmsItemResourcePageList(commandMap);
        model.addAttribute("list", list);	
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemResourcePageList";
    }


    @RequestMapping(value="/adm/cts/LcmsItemResourceList.do")
    public String list( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        List list = lcmsItemResourceService.selectLcmsItemResourceList(commandMap);
        model.addAttribute("list", list);	

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemResourceList";
    }

    @RequestMapping(value="/adm/cts/LcmsItemResourceView.do")
    public String view( 
    HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap(); 
        output.putAll((Map)lcmsItemResourceService.selectLcmsItemResource(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemResourceView";
    }

    @RequestMapping(value="/adm/cts/LcmsItemResourceInsertForm.do")
    public String insertForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemResourceInsert";
    }

    @RequestMapping(value="/adm/cts/LcmsItemResourceInsert.do")
    public String insert( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= "";         String forwardUrl 	= ""; 
        Object output = lcmsItemResourceService.insertLcmsItemResource( commandMap);
        commandMap.put("output"," output"); 
        resultMsg = egovMessageSource.getMessage("success.common.insert"); 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsItemResourcePageList.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsItemResourceUdateForm.do")
    public String updateForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap();         output.putAll((Map)lcmsItemResourceService.selectLcmsItemResource(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemResourceUpdate";
    }

    @RequestMapping(value="/adm/cts/LcmsItemResourceUpdate.do")
    public String update( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg = ""; 
        String forwardUrl = ""; 

        int result = lcmsItemResourceService.updateLcmsItemResource( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.update"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.update"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsItemResourceUdateForm.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsItemResourceDelete.do")
    public String delete( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        int result = lcmsItemResourceService.deleteLcmsItemResource( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsItemResourcePageListForm.do";
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsItemResourceDelete.do")
    public String deleteAll( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        commandMap.put("rsrcSeq", EgovStringUtil.getStringSequence(commandMap, "chk")); 
        int result = lcmsItemResourceService.deleteLcmsItemResourceAll( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsItemResourcePageListForm.do";
        return forwardUrl; 
    }

}
