package egovframework.adm.lcms.cts.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.lcms.cts.service.LcmsFileService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class LcmsFileController {

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

	/** lcmsFileService */
	@Resource(name = "lcmsFileService")
	 private LcmsFileService lcmsFileService;
	
	@RequestMapping(value="/adm/cts/LcmsFileInsert.do")
    public String insert(  HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> commandMap, ModelMap model) throws Exception { 
		
		String resultMsg = "";
		lcmsFileService.insertLcmsFile(commandMap);
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "";
	}
}
