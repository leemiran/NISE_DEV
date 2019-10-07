package egovframework.mbl.app.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.cou.service.SubjectService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.mbl.app.service.appCheckerService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class appCheckerController {

	/** log */
	protected static final Log log = LogFactory.getLog(appCheckerController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	 /** appCheckerService */
	@Resource(name = "appCheckerService")
    private appCheckerService appCheckerService;
	
	
	/**
	 * 모바일 > app 버젼 체커
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/std/CheckAppVersion.do")
	public String CheckAppVersion(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		boolean isOk = appCheckerService.selectAppCheckerResult(commandMap);
		
		
		model.addAttribute("isOk", isOk);
		
		return "mbl/std/CheckAppVersion";
	}
	
	/**
	 * 모바일 해쉬
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/std/checkAppSha.do")
	public String checkAppSha(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		   /*String makeSha = this.makeSha("KNISE");
		   model.addAttribute("isOk", makeSha);*/
		
		final String ohashValue = "ZZ1gt2iRv2baDJdcebFhR4YvlUg=";
		String resultMsg = "FALSE";
		String resultCode = "200";
		
		String hashValue = commandMap.get("hashValue") != null ? commandMap.get("hashValue").toString() : "";
		if(ohashValue.equals(hashValue)){
			resultMsg = "TRUE";
			resultCode = "100";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAttribute("resultCode", resultCode);
		model.addAllAttributes(commandMap);
		return "mbl/std/checkAppSha";
	}
		  
	public static String makeSha(String inputText) throws NoSuchAlgorithmException{
	   
	   MessageDigest md = MessageDigest.getInstance("SHA-1");
	   md.update(inputText.getBytes());
	   byte[] digest = md.digest();
	   
	   System.out.println(md.getAlgorithm());
	   System.out.println(digest.length);
	   
	   StringBuffer sb = new StringBuffer();
	   for(byte b : digest){
		   System.out.print(Integer.toHexString(b & 0xff) + "");
		   sb.append(Integer.toHexString(b & 0xff));
	   }
	   
	   System.out.println("\n\nReturn String : " + sb.toString());
	   return sb.toString();
	  
	}
	
	
	
}
