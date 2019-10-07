package egovframework.svt.cert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CertSciContoller {

	@Autowired
	SciService sciService;

	@RequestMapping("/cert/sci/reqSci.do")
	public String reqSci(Model model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam String retType,
			@RequestParam(required = false) String addVar) throws Exception {

		if ("join".equals(retType)) {
			// sci 본인인증 세팅
			String seviceNo = "";
			if (request.isSecure()) {
				seviceNo = "009002";
			} else {
				seviceNo = "008002";
			}
			// dev
			// seviceNo = "010002";
			model.addAttribute("reqInfo",
					sciService.getEncriptData(seviceNo, response, ""));
		} else if ("dormant".equals(retType)) {
			String seviceNo = "009003";
			model.addAttribute("reqInfo",
					sciService.getEncriptData(seviceNo, response, addVar));
		}

		model.addAttribute("resultCode", "SUCCESS");
		model.addAttribute("retUrl",
				"32http://iedu.nise.go.kr/cert/sci/sciResult.do?retType="
						+ retType);
		return "jsonView";
	}

	@RequestMapping("/cert/sci/sciResult.do")
	public String sciResult(Model model, HttpServletRequest request,
			@RequestParam String retType) throws Exception {

		HttpSession session = request.getSession();

		if ("join".equals(retType)) {
			model.addAttribute("retUrl", "/usr/mem/memJoinStep03.do");
		} else if ("dormant".equals(retType)) {
			model.addAttribute("retUrl", "/usr/mem/memDormantPage02.do");
		}
		model.addAttribute("retType", retType);
		model.addAttribute("resultMap", sciService.parseResultToMap(request));

		// System.out.println("sciResult name ----> "+(String)sciService.parseResultToMap(request).get("name"));
		String p_o_realName = sciService.parseResultToMap(request).get("name") != null ? (String) sciService
				.parseResultToMap(request).get("name") : "";
		session.setAttribute("p_o_realName", p_o_realName); // 이름

		return "svt/cert/sci/sciResult";
	}

}
