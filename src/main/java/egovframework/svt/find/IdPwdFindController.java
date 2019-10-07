package egovframework.svt.find;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.svt.cert.EpkiHtml5Service;

import egovframework.svt.cert.EpkiService;

@Controller
public class IdPwdFindController {

	
	@Autowired
	EpkiService epkiService;
	
	@Autowired
	IdPwdFindService idPwdFindService;
	
	@Resource(name = "propertiesService")
    protected EgovPropertyService  propertyService;

	@Autowired
	EpkiHtml5Service epkiHtml5Service;

	@RequestMapping("/usr/mem/userIdPwdSearch01.do")
	public String idPwdFind(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		model.addAllAttributes(commandMap);
		
		//old epki
		//model.addAttribute("strServerCert", epkiService.getServerCert());
		
		if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {
			//html5 epki 인증 세팅
			epkiHtml5Service.getHtml5ServerCert(request, model);
		}		
		
		return "svt/find/idPwdFind";
	}
	
	@RequestMapping(value = "/adm/userPwdInit.do", method = RequestMethod.POST)
	public String userPwdInit(Model model, HttpServletRequest request, Map<String, Object> commandMap) throws Exception {
		Map<String, Object> resultMap = idPwdFindService.userPwdInit(commandMap);
		model.addAllAttributes(resultMap);
		return "jsonView";
	}
	
}
