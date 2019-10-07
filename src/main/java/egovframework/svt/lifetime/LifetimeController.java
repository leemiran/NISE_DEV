package egovframework.svt.lifetime;

import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LifetimeController {

	/** log */
	protected static final Log log = LogFactory.getLog(LifetimeController.class);
	
	@Autowired
	LifetimeService lifetimeService;
	
	@RequestMapping(value="/lifetime/join.do")
	public String join(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		if(null == request.getSession().getAttribute("userid")) {
			model.addAttribute("resultCode", "NONE_USERID");
			model.addAttribute("resultMsg", "로그인을 하셔야 접근 가능한 페이지입니다.");
			return "jsonView";
		}
		
		Map<String, Object> resultMap = lifetimeService.join(request);
		model.addAllAttributes(resultMap);
		return "jsonView"; 
	}
	
	@RequestMapping(value="/lifetime/putSubj.do")
	public String putSubj(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		Map<String, Object> resultMap = lifetimeService.putSubj(commandMap);
		model.addAllAttributes(resultMap);
		return "jsonView";
	}
	
	@RequestMapping(value="/svt/lifetime/subjInfoLifetimeXml.do")
	public String subjInfoLifetimeXml(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		List list = lifetimeService.selectSubjInfoDetailLifetime(commandMap);		
		model.addAttribute("list", list);
		return "/svt/lifetime/subjInfoLifetimeXml";
	}
	
	@RequestMapping(value="/svt/lifetime/subjInfoDetailLifetimeXml.do")
	public String subjInfoDetailLifetimeXml(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		List list = lifetimeService.selectSubjInfoDetailLifetime(commandMap);		
		model.addAttribute("list", list);
		return "/svt/lifetime/subjInfoDetailLifetimeXml";
	}
	
}
