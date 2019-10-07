package egovframework.svt.cert;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.ModelMap;

import egovframework.svt.find.IdPwdFindService;
import egovframework.svt.cert.EpkiHtml5Service;

@Controller
public class CertEpkiController {

	protected Log log = LogFactory.getLog(this.getClass());

	@Autowired
	EpkiService epkiService;

	@Autowired
	IdPwdFindService idPwdFindService;

	@Autowired
	EpkiHtml5Service epkiHtml5Service;

	@RequestMapping("/cert/epki/epkiResult.do")
	public String epkiResult(Model model, HttpServletRequest request)
			throws Exception {
		model.addAttribute("isCert", epkiService.isCert(request));
		return "svt/cert/epki/epkiResult";
	}

	@RequestMapping("/cert/epki/findId.do")
	public String findId(Model model, HttpServletRequest request,
			Map<String, Object> commandMap) throws Exception {
		String userDn = epkiService.getUserDN(request);
		if ("" == userDn) {
			model.addAttribute("resultMsg", "인증서 정보가 일치하지 않습니다.");
			return "forward:/usr/mem/userIdPwdSearch01.do";
		}
		String realName = String.valueOf(commandMap.get("realName"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("certGubun", "E");
		paramMap.put("epkiDn", userDn);
		paramMap.put("name", realName);

		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "아아디/비밀번호찾기");
		commandMap.put("menu_sub", "2");
		commandMap.put("menu_main", "7");
		commandMap.put("p_name", realName);
		model.addAllAttributes(commandMap);
		return idPwdFindService.findId(paramMap, model);
	}

	@RequestMapping("/cert/epki/findPwd.do")
	public String findPwd(Model model, HttpServletRequest request,
			Map<String, Object> commandMap) throws Exception {
		String userDn = epkiService.getUserDN(request);
		if ("" == userDn) {
			model.addAttribute("resultMsg", "인증서 정보가 일치하지 않습니다.");
			return "forward:/usr/mem/userIdPwdSearch01.do";
		}
		String userid = String.valueOf(commandMap.get("userid"));
		String realName = String.valueOf(commandMap.get("realName"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("certGubun", "E");
		paramMap.put("epkiDn", userDn);
		paramMap.put("name", realName);
		paramMap.put("userid", userid);

		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "아아디/비밀번호찾기");
		commandMap.put("menu_sub", "2");
		commandMap.put("menu_main", "7");
		commandMap.put("p_mode", "pwdcheck");
		model.addAllAttributes(commandMap);
		return idPwdFindService.findPwd(paramMap, model);
	}

	@RequestMapping("/cert/epki/findIdHtml5.do")
	public String findIdHtml5(ModelMap model, HttpServletRequest request,
			Map<String, Object> commandMap) throws Exception {

		// EPKI DN
		epkiHtml5Service.getHtml5Dn(request, model, commandMap);
		String strUserDN = commandMap.get("dn") != null ? commandMap.get("dn")
				.toString() : "";

		if ("" == strUserDN) {
			model.addAttribute("resultMsg", "인증서 정보가 일치하지 않습니다.");
			return "forward:/usr/mem/userIdPwdSearch01.do";
		}
		String realName = String.valueOf(commandMap.get("epki_id_realName"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("certGubun", "E");
		paramMap.put("epkiDn", strUserDN);
		paramMap.put("name", realName);

		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "아아디/비밀번호찾기");
		commandMap.put("menu_sub", "2");
		commandMap.put("menu_main", "7");
		commandMap.put("p_name", realName);
		model.addAllAttributes(commandMap);
		return idPwdFindService.findIdHtml5(paramMap, model);
	}

	@RequestMapping("/cert/epki/findPwHtml5.do")
	public String findPwdHtml5(ModelMap model, HttpServletRequest request,
			Map<String, Object> commandMap) throws Exception {

		// EPKI DN
		epkiHtml5Service.getHtml5Dn(request, model, commandMap);
		String strUserDN = commandMap.get("dn") != null ? commandMap.get("dn")
				.toString() : "";

		if ("" == strUserDN) {
			model.addAttribute("resultMsg", "인증서 정보가 일치하지 않습니다.");
			return "forward:/usr/mem/userIdPwdSearch01.do";
		}
		String userid = String.valueOf(commandMap.get("epki_pw_userid"));
		String realName = String.valueOf(commandMap.get("epki_pw_realName"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("certGubun", "E");
		paramMap.put("epkiDn", strUserDN);
		paramMap.put("name", realName);
		paramMap.put("userid", userid);

		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "아아디/비밀번호찾기");
		commandMap.put("menu_sub", "2");
		commandMap.put("menu_main", "7");
		commandMap.put("p_mode", "pwdcheck");
		model.addAllAttributes(commandMap);
		return idPwdFindService.findPwdHtml5(paramMap, model);
	}

}
