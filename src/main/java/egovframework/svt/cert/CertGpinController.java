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

import egovframework.svt.find.IdPwdFindService;

@Controller
public class CertGpinController {

	protected Log log = LogFactory.getLog(this.getClass());

	@Autowired
	IdPwdFindService idPwdFindService;

	@RequestMapping("/cert/gpin/findId.do")
	public String findId(Model model, HttpServletRequest request,
			Map<String, Object> commandMap) throws Exception {
		String virtualno = String.valueOf(commandMap.get("virtualNo"));
		String name = String.valueOf(commandMap.get("realName"));
		String birthDate = String.valueOf(commandMap.get("birthDate"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("certGubun", "G");
		paramMap.put("virtualno", virtualno);
		paramMap.put("name", name);
		paramMap.put("birthDate", birthDate);

		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "아아디/비밀번호찾기");
		commandMap.put("menu_sub", "2");
		commandMap.put("menu_main", "7");
		model.addAllAttributes(commandMap);
		return idPwdFindService.findId(paramMap, model);
	}

	@RequestMapping("/cert/gpin/findPwd.do")
	public String findPwd(Model model, HttpServletRequest request,
			Map<String, Object> commandMap) throws Exception {
		String virtualno = String.valueOf(commandMap.get("virtualNo"));
		String name = String.valueOf(commandMap.get("realName"));
		String birthDate = String.valueOf(commandMap.get("birthDate"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("certGubun", "G");
		paramMap.put("virtualno", virtualno);
		paramMap.put("name", name);
		paramMap.put("birthDate", birthDate);

		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "아아디/비밀번호찾기");
		commandMap.put("menu_sub", "2");
		commandMap.put("menu_main", "7");
		model.addAllAttributes(commandMap);
		return idPwdFindService.findPwd(paramMap, model);
	}
}
