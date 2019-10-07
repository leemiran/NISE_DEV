package egovframework.adm.lcms.com.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.lcms.com.service.CommonContentService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class CommonContentController {

	/** log */
	protected static final Log log = LogFactory.getLog(CommonContentController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** commonContentService */
	@Resource(name = "commonContentService")
	CommonContentService commonContentService;
	
	@RequestMapping(value="/adm/lcms/com/masterFormUpdatePage.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map data = commonContentService.selectMasterformData(commandMap);
		List mfList = commonContentService.selectMFMenuList(commandMap);
		List subList = commonContentService.selectMFSubjList(commandMap);
		
		model.addAttribute("data", data);
		model.addAttribute("mfList", mfList);
		model.addAttribute("subList", subList);
		model.addAllAttributes(commandMap);
		return "adm/lcms/com/masterFormUpdatePage";
	}
	
	@RequestMapping(value="/adm/lcms/com/masterFormUpdate.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = commonContentService.updateMasterform(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/lcms/com/masterFormUpdatePage.do";
	}
	
}