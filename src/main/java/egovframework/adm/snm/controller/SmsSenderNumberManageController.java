package egovframework.adm.snm.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.snm.service.SmsSenderNumberManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.snd.service.SendSmsMailManageService;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class SmsSenderNumberManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(SmsSenderNumberManageController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** sendMailManageService */
	@Resource(name = "sendSmsMailManageService")
	SendSmsMailManageService sendSmsMailManageService;
	
	/** sendMailManageService */
	@Resource(name = "smsSenderNumberManageService")
	SmsSenderNumberManageService smsSenderNumberManageService;
	
	/**
	 * 발신자 번호 조회
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/snm/SmsSenderNumberManage.do")
	public String SmsSenderNumberManage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String adminName = EgovProperties.getProperty("sms.admin.user");
		String [] adminTel = EgovProperties.getProperty("knise.admin.comptel").split("-");
		
		Map<String, Object> fromHp = sendSmsMailManageService.getSendFromHandPhon(commandMap);
		
		if(fromHp.containsKey("hometel") && fromHp.get("hometel") != null && !"".equals(fromHp.get("hometel"))){
			adminTel = (fromHp.get("hometel")+"").split("-");
		}
		
		model.addAttribute("adminName", adminName);
		model.addAttribute("adminTel1", adminTel[0]);
		model.addAttribute("adminTel2", adminTel[1]);
		model.addAttribute("adminTel3", adminTel[2]);
		
		model.addAllAttributes(commandMap);
		return "adm/snm/smsSenderNumberManage";
	}
	
	/**
	 * 발신자 번호 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/snm/updateSmsSenderNumberManage.do")
	public String updateSmsSenderNumberManage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		log.error("발신자 번호 수정");
		int fromHpCnt = smsSenderNumberManageService.updateSendFromHandPhon(commandMap);
		
		model.addAllAttributes(commandMap);
		return "forward:/adm/snm/SmsSenderNumberManage.do";
	}
	
	
}
