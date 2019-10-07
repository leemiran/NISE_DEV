package egovframework.svt.adm.snd.templete;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.svt.util.HtmlUtil;

@Controller
public class AdminSndTempleteController {

	/** log */
	protected static final Log log = LogFactory.getLog(AdminSndTempleteController.class);
	
	@Autowired
	AdminSndTempleteService adminSndTempleteService;
	
	@Autowired
	HtmlUtil htmlUtil;
	
	@RequestMapping(value="/adm/snd/templeteList.do")
	public String templeteList(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		List<?> templeteList = adminSndTempleteService.templeteList();
		model.addAttribute("templeteList", templeteList);

		return "svt/adm/snd/templeteList";
	}
	
	@RequestMapping(value="/adm/snd/templeteReg.do")
	public String templeteReg(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "svt/adm/snd/templeteReg";
	}

	@RequestMapping(value="/adm/snd/insertTemplete.do")
	public String insertTemplete(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "categoryNm");
		
		model.addAllAttributes(adminSndTempleteService.insertTemplete(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/snd/templeteDetail.do")
	public String templeteDetail(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		Map<String, String> templete = adminSndTempleteService.templeteDetail(commandMap.get("templeteSeq").toString());
		model.addAttribute("templete", templete);
		
		return "svt/adm/snd/templeteReg";
	}

	@RequestMapping(value="/adm/snd/updateTemplete.do")
	public String updateTemplete(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "categoryNm");
		
		model.addAllAttributes(adminSndTempleteService.updateTemplete(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/snd/deleteTemplete.do")
	public String deleteTemplete(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(adminSndTempleteService.deleteTemplete(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/snd/getTempleteContent.do")
	public String getTempleteContent(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map<String, String> templete = adminSndTempleteService.templeteDetail(commandMap.get("templeteSeq").toString());
		model.addAttribute("content", templete.get("content"));
		return "jsonView";
	}
	
}
