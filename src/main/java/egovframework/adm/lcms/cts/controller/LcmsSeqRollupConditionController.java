/* 
 * LcmsSeqRollupConditionController.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsSeqRollupConditionService;
import egovframework.adm.lcms.cts.domain.LcmsSeqRollupCondition;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRollupConditionController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Controller
public class LcmsSeqRollupConditionController { 
/** log */ 
protected static final Log LOG = LogFactory.getLog( LcmsSeqRollupConditionController.class);

/** EgovPropertyService */
@Resource(name = "propertiesService")
protected EgovPropertyService propertiesService;

/** EgovMessageSource */
@Resource(name="egovMessageSource")
EgovMessageSource egovMessageSource;

/** PagingManageController */
@Resource(name = "pagingManageController")
private PagingManageController pagingManageController;

/** lcmsSeqRollupConditionService */
@Resource(name = "lcmsSeqRollupConditionService")
 private LcmsSeqRollupConditionService lcmsSeqRollupConditionService;

    @RequestMapping(value="/adm/cts/LcmsSeqRollupConditionPageList.do")
    public String pageList( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        int totCnt = lcmsSeqRollupConditionService.selectLcmsSeqRollupConditionPageListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

        List list = lcmsSeqRollupConditionService.selectLcmsSeqRollupConditionPageList(commandMap);
        model.addAttribute("list", list);	
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqRollupConditionPageList";
    }


    @RequestMapping(value="/adm/cts/LcmsSeqRollupConditionList.do")
    public String list( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        List list = lcmsSeqRollupConditionService.selectLcmsSeqRollupConditionList(commandMap);
        model.addAttribute("list", list);	

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqRollupConditionList";
    }

    @RequestMapping(value="/adm/cts/LcmsSeqRollupConditionView.do")
    public String view( 
    HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap(); 
        output.putAll((Map)lcmsSeqRollupConditionService.selectLcmsSeqRollupCondition(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqRollupConditionView";
    }

    @RequestMapping(value="/adm/cts/LcmsSeqRollupConditionInsertForm.do")
    public String insertForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqRollupConditionInsert";
    }

    @RequestMapping(value="/adm/cts/LcmsSeqRollupConditionInsert.do")
    public String insert( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= "";         String forwardUrl 	= ""; 
        Object output = lcmsSeqRollupConditionService.insertLcmsSeqRollupCondition( commandMap);
        commandMap.put("output"," output"); 
        resultMsg = egovMessageSource.getMessage("success.common.insert"); 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsSeqRollupConditionPageList.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsSeqRollupConditionUdateForm.do")
    public String updateForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap();         output.putAll((Map)lcmsSeqRollupConditionService.selectLcmsSeqRollupCondition(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqRollupConditionUpdate";
    }

    @RequestMapping(value="/adm/cts/LcmsSeqRollupConditionUpdate.do")
    public String update( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg = ""; 
        String forwardUrl = ""; 

        int result = lcmsSeqRollupConditionService.updateLcmsSeqRollupCondition( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.update"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.update"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsSeqRollupConditionUdateForm.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsSeqRollupConditionDelete.do")
    public String delete( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        int result = lcmsSeqRollupConditionService.deleteLcmsSeqRollupCondition( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsSeqRollupConditionPageListForm.do";
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsSeqRollupConditionDelete.do")
    public String deleteAll( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        commandMap.put("rlcIdxNum", EgovStringUtil.getStringSequence(commandMap, "chk")); 
        int result = lcmsSeqRollupConditionService.deleteLcmsSeqRollupConditionAll( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsSeqRollupConditionPageListForm.do";
        return forwardUrl; 
    }

}
