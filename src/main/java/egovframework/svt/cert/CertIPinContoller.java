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
public class CertIPinContoller {
	
	@Autowired
	IPinService ipinService;
	
	@RequestMapping("/cert/ipin/reqIPin.do")
	public String reqIPin(Model model, HttpServletRequest request, HttpServletResponse response 
			, @RequestParam String retType
			, @RequestParam(required=false) String addVar) throws Exception {
		
		model.addAttribute("m", "pubmain");
		model.addAttribute("enc_data", ipinService.getEncriptData(request, response, retType));

		model.addAttribute("resultCode", "SUCCESS");
		model.addAttribute("retUrl", "http://iedu.nise.go.kr/cert/ipin/ipinResult.do?retType=" + retType);
		//model.addAttribute("retUrl", "http://iedu.nise.go.kr/I-PIN2/ipin_process.jsp");
		return "jsonView";
	}
	
	@RequestMapping("/cert/ipin/ipinResult.do")
	public String ipinResult(Model model, HttpServletRequest request
			, @RequestParam String retType) throws Exception {
		
		HttpSession session = request.getSession();
		
		if("join".equals(retType)) {
			model.addAttribute("retUrl", "/usr/mem/memJoinStep03.do");
		} else if("dormant".equals(retType)) {
			model.addAttribute("retUrl", "/usr/mem/memDormantPage02.do");
		}
		model.addAttribute("retType", retType);
		model.addAttribute("resultMap", ipinService.parseResultToMap(request));
		
		//System.out.println("ipinResult name ----> "+(String)ipinService.parseResultToMap(request).get("name"));
		String p_o_realName = ipinService.parseResultToMap(request).get("name") != null ? (String)ipinService.parseResultToMap(request).get("name") : "";
		session.setAttribute("p_o_realName",p_o_realName);	// 이름
		
		return "svt/cert/ipin/ipinResult";
	}

}
