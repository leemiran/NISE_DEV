package egovframework.adm.lcms.nuc.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.lcms.api.com.Util;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.lcms.com.service.CommonContentService;
import egovframework.adm.lcms.nct.service.LcmsModuleService;
import egovframework.adm.lcms.nuc.service.NotUseContentService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.utl.fcc.service.EgovStringUtil;

@Controller
public class NotUseContentController {

	/** log */
	protected static final Log log = LogFactory.getLog(NotUseContentController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	/** lcmsContentService */
	@Resource(name = "notUseContentService")
	NotUseContentService notUseContentService;
	
	/** commonContentService */
	@Resource(name = "commonContentService")
	CommonContentService commonContentService;
	
	
	
	@RequestMapping(value="/adm/lcms/nuc/notUseContenList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if( ((String)commandMap.get("gadmin")).startsWith("M") ){
			commandMap.put("type", true);
		}

		int totCnt = notUseContentService.selectNotUseContenListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

		List list = notUseContentService.selectNotUseContenListList(commandMap);
		
		model.addAttribute("list", list);
				
		model.addAllAttributes(commandMap);
		return "adm/lcms/nuc/notUseContenList";
	}
	
}