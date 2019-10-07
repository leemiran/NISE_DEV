/* 
 * LcmsSeqMapInfoController.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsSeqMapInfoService;
import egovframework.adm.lcms.cts.domain.LcmsSeqMapInfo;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqMapInfoController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Controller
public class LcmsSeqMapInfoController { 
/** log */ 
protected static final Log LOG = LogFactory.getLog( LcmsSeqMapInfoController.class);

/** EgovPropertyService */
@Resource(name = "propertiesService")
protected EgovPropertyService propertiesService;

/** EgovMessageSource */
@Resource(name="egovMessageSource")
EgovMessageSource egovMessageSource;

/** PagingManageController */
@Resource(name = "pagingManageController")
private PagingManageController pagingManageController;

/** lcmsSeqMapInfoService */
@Resource(name = "lcmsSeqMapInfoService")
 private LcmsSeqMapInfoService lcmsSeqMapInfoService;

    @RequestMapping(value="/adm/cts/LcmsSeqMapInfoPageList.do")
    public String pageList( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        int totCnt = lcmsSeqMapInfoService.selectLcmsSeqMapInfoPageListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

        List list = lcmsSeqMapInfoService.selectLcmsSeqMapInfoPageList(commandMap);
        model.addAttribute("list", list);	
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqMapInfoPageList";
    }


    @RequestMapping(value="/adm/cts/LcmsSeqMapInfoList.do")
    public String list( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        List list = lcmsSeqMapInfoService.selectLcmsSeqMapInfoList(commandMap);
        model.addAttribute("list", list);	

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqMapInfoList";
    }

    @RequestMapping(value="/adm/cts/LcmsSeqMapInfoView.do")
    public String view( 
    HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap(); 
        output.putAll((Map)lcmsSeqMapInfoService.selectLcmsSeqMapInfo(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqMapInfoView";
    }

    @RequestMapping(value="/adm/cts/LcmsSeqMapInfoInsertForm.do")
    public String insertForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqMapInfoInsert";
    }

    @RequestMapping(value="/adm/cts/LcmsSeqMapInfoInsert.do")
    public String insert( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= "";         String forwardUrl 	= ""; 
        Object output = lcmsSeqMapInfoService.insertLcmsSeqMapInfo( commandMap);
        commandMap.put("output"," output"); 
        resultMsg = egovMessageSource.getMessage("success.common.insert"); 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsSeqMapInfoPageList.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsSeqMapInfoUdateForm.do")
    public String updateForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap();         output.putAll((Map)lcmsSeqMapInfoService.selectLcmsSeqMapInfo(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsSeqMapInfoUpdate";
    }

    @RequestMapping(value="/adm/cts/LcmsSeqMapInfoUpdate.do")
    public String update( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg = ""; 
        String forwardUrl = ""; 

        int result = lcmsSeqMapInfoService.updateLcmsSeqMapInfo( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.update"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.update"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsSeqMapInfoUdateForm.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsSeqMapInfoDelete.do")
    public String delete( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        int result = lcmsSeqMapInfoService.deleteLcmsSeqMapInfo( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsSeqMapInfoPageListForm.do";
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsSeqMapInfoDelete.do")
    public String deleteAll( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        commandMap.put("rlcIdxNum", EgovStringUtil.getStringSequence(commandMap, "chk")); 
        int result = lcmsSeqMapInfoService.deleteLcmsSeqMapInfoAll( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsSeqMapInfoPageListForm.do";
        return forwardUrl; 
    }

}
