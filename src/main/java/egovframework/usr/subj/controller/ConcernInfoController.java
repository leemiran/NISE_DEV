package egovframework.usr.subj.controller;

import java.util.List;
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
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class ConcernInfoController {

	/** log */
	protected static final Log log = LogFactory.getLog(ConcernInfoController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
    /** subjectService */
	@Resource(name = "subjectService")
    private SubjectService subjectService;
	
	
	
	
	/**
	 * 마이페이지 > 나의 관심과정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/concernInfoList.do")
	public String concernInfoList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List<?> list = subjectService.selectConcernInfoList(commandMap);
		
		model.addAttribute("list", list);	
		
		
		model.addAllAttributes(commandMap);
		return "usr/mpg/concernInfoList";
	}
	
	
	/**
	 * 마이페이지 > 나의 관심과정 등록/삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/concernInfoAction.do")
	public String concernInfoAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String p_process = (String)commandMap.get("p_process");
		boolean isok = false;
		
		String resultMsg = "";
		String url = "forward:/usr/mpg/concernInfoList.do";
		
		
		//이전의 URI로 다시 보내기 위하여 등록한다.
		//없다면 기본 페이지로 돌아간다.
		String prevUrl = this.getPrevURL(request);
		
		if(prevUrl != null)
		{
			url = "forward:" + prevUrl;
			log.info(" prevUrl : " + prevUrl);
		}
		
		//과정목록에서 저장 && 과정보기에서 저장하기
		if(p_process.equals("insert"))
		{
			isok = subjectService.insertConcernInfo(commandMap);
			
			if(isok) {
				resultMsg = egovMessageSource.getMessage("success.common.insert");
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.insert");
			}
			
		}
		else if(p_process.equals("delete"))
		{
			isok = subjectService.deleteConcernInfo(commandMap);
			
			if(isok) {
				resultMsg = egovMessageSource.getMessage("success.common.delete");
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.delete");
			}
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	/**
	 * 이전 url 알아오기
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String getPrevURL(HttpServletRequest request) 
	{ 
		String url = request.getHeader("REFERER");
		
		if(url.lastIndexOf('?') != -1)
		{
			url = url.substring(0, url.lastIndexOf('?'));
		}
		
		if(url.indexOf('/') != -1)
		{
			url = url.substring(url.indexOf('/')+1);
			url = url.substring(url.indexOf('/')+1);
			url = url.substring(url.indexOf('/'));
		}
		
		return url;  
	}

}
