/* 
 * LcmsCourseOrgController.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsCourseOrgService;
import egovframework.adm.lcms.cts.domain.LcmsCourseOrg;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCourseOrgController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Controller
public class LcmsCourseOrgController { 
/** log */ 
protected static final Log LOG = LogFactory.getLog( LcmsCourseOrgController.class);

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
@Resource(name = "lcmsCourseOrgService")
 private LcmsCourseOrgService lcmsCourseOrgService;

    @RequestMapping(value="/adm/cts/LcmsCourseOrgPageList.do")
    public String pageList( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        int totCnt = lcmsCourseOrgService.selectLcmsCourseOrgPageListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

        List list = lcmsCourseOrgService.selectLcmsCourseOrgPageList(commandMap);
        model.addAttribute("list", list);	
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsCourseOrgPageList";
    }


    @RequestMapping(value="/adm/cts/LcmsCourseOrgList.do")
    public String list( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        List list = lcmsCourseOrgService.selectLcmsCourseOrgList(commandMap);
        model.addAttribute("list", list);	

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsCourseOrgList";
    }

    @RequestMapping(value="/adm/cts/LcmsCourseOrgView.do")
    public String view( 
    HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap(); 
        output.putAll((Map)lcmsCourseOrgService.selectLcmsCourseOrg(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsCourseOrgView";
    }

    @RequestMapping(value="/adm/cts/LcmsCourseOrgInsertForm.do")
    public String insertForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsCourseOrgInsert";
    }

    @RequestMapping(value="/adm/cts/LcmsCourseOrgInsert.do")
    public String insert( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= "";         String forwardUrl 	= ""; 
        Object output = lcmsCourseOrgService.insertLcmsCourseOrg( commandMap);
        commandMap.put("output"," output"); 
        resultMsg = egovMessageSource.getMessage("success.common.insert"); 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsCourseOrgPageList.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsCourseOrgUdateForm.do")
    public String updateForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap();         output.putAll((Map)lcmsCourseOrgService.selectLcmsCourseOrg(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsCourseOrgUpdate";
    }

    @RequestMapping(value="/adm/cts/LcmsCourseOrgUpdate.do")
    public String update( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg = ""; 
        String forwardUrl = ""; 

        int result = lcmsCourseOrgService.updateLcmsCourseOrg( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.update"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.update"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsCourseOrgUdateForm.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsCourseOrgDelete.do")
    public String delete( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        int result = lcmsCourseOrgService.deleteLcmsCourseOrg( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsCourseOrgPageListForm.do";
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsCourseOrgDelete.do")
    public String deleteAll( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        commandMap.put("orgSeq", EgovStringUtil.getStringSequence(commandMap, "chk")); 
        int result = lcmsCourseOrgService.deleteLcmsCourseOrgAll( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsCourseOrgPageListForm.do";
        return forwardUrl; 
    }

}
