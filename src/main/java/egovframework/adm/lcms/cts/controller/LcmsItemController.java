/* 
 * LcmsItemController.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsItemService;
import egovframework.adm.lcms.cts.domain.LcmsItem;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsItemController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Controller
public class LcmsItemController { 
/** log */ 
protected static final Log LOG = LogFactory.getLog( LcmsItemController.class);

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
@Resource(name = "lcmsItemService")
 private LcmsItemService lcmsItemService;

    @RequestMapping(value="/adm/cts/LcmsItemPageList.do")
    public String pageList( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        int totCnt = lcmsItemService.selectLcmsItemPageListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

        List list = lcmsItemService.selectLcmsItemPageList(commandMap);
        model.addAttribute("list", list);	
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemPageList";
    }


    @RequestMapping(value="/adm/cts/LcmsItemList.do")
    public String list( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        List list = lcmsItemService.selectLcmsItemList(commandMap);
        model.addAttribute("list", list);	

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemList";
    }

    @RequestMapping(value="/adm/cts/LcmsItemView.do")
    public String view( 
    HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap(); 
        output.putAll((Map)lcmsItemService.selectLcmsItem(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemView";
    }

    @RequestMapping(value="/adm/cts/LcmsItemInsertForm.do")
    public String insertForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemInsert";
    }

    @RequestMapping(value="/adm/cts/LcmsItemInsert.do")
    public String insert( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= "";         String forwardUrl 	= ""; 
        Object output = lcmsItemService.insertLcmsItem( commandMap);
        commandMap.put("output"," output"); 
        resultMsg = egovMessageSource.getMessage("success.common.insert"); 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsItemPageList.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsItemUdateForm.do")
    public String updateForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap();         
        output.putAll((Map)lcmsItemService.selectLcmsItem(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/cts/LcmsItemUpdate";
    }

    @RequestMapping(value="/adm/cts/LcmsItemUpdate.do")
    public String update( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg = ""; 
        String forwardUrl = ""; 

        int result = lcmsItemService.updateLcmsItem( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.update"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.update"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsItemUdateForm.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsItemDelete.do")
    public String delete( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        int result = lcmsItemService.deleteLcmsItem( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsItemPageListForm.do";
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/cts/LcmsItemDelete.do")
    public String deleteAll( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 

        commandMap.put("orgSeq", EgovStringUtil.getStringSequence(commandMap, "chk")); 
        int result = lcmsItemService.deleteLcmsItemAll( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/cts/LcmsItemPageListForm.do";
        return forwardUrl; 
    }

}
