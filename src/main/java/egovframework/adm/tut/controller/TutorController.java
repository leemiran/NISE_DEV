package egovframework.adm.tut.controller;

import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.tut.service.TutorService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class TutorController {
	/** log */
	protected Log log = LogFactory.getLog(this.getClass());

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
    /** tutorService */
	@Resource(name = "tutorService")
    private TutorService tutorService;
	
	

	/**
	 * 강사 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/tut/tutorList.do")
	public String tutorList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		int totCnt = tutorService.selectTutorTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
		List<?> list = tutorService.selectTutorList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/tut/tutorList";
	}
	
	
	/**
	 * 강사 보기 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/tut/tutorView.do")
	public String tutorView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
        
		
		/**
		 * 강사 강의 리스트 조회
		 */
		List<?> subjList = tutorService.selectTutorSubjList(commandMap);
		
		model.addAttribute("subjList", subjList);
		
		/**
		 * 강사 강의 이력 리스트 조회
		 */
		List<?> subjHistList = tutorService.selectTutorSubjHistoryList(commandMap);
		
		model.addAttribute("subjHistList", subjHistList);
		
		
		/**
		 * 강사 정보 보기 화면
		 */
		Map<?, ?> view = tutorService.selectTutorView(commandMap);
		
		model.addAttribute("view", view);	
		model.addAllAttributes(commandMap);
		
		return "adm/tut/tutorView";
	}
	
	
	/**
	 * 강사 등록/수정/삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/tut/tutorAction.do")
	public String tutorAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
        
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "/adm/tut/tutorList.do";
		
		
		if(p_process != null)
		{
			if(p_process.equals("insert"))
			{
				int cnt = tutorService.insertTutor(commandMap);
				
				//정상등록
				if(cnt == 99)
				{
					resultMsg = egovMessageSource.getMessage("success.common.insert");
					
				}
				//중복된 아이디가 있음
				else if(cnt == 1)
				{
					resultMsg = egovMessageSource.getMessage("fail.common.userid.same.count");
					
				}
				//등록에러
				else{
					resultMsg = egovMessageSource.getMessage("fail.common.insert");
				}
			}
			else if(p_process.equals("update"))
			{
				int cnt = tutorService.updateTutor(commandMap);
				//정상수정
				if(cnt > 0)
				{
					resultMsg = egovMessageSource.getMessage("success.common.update");
					
				}
				//에러
				else
				{
					resultMsg = egovMessageSource.getMessage("fail.common.update");
				}
			}
			
			else if(p_process.equals("delete"))
			{
				int cnt = tutorService.deleteTutor(commandMap);
				//정상삭제
				if(cnt > 0)
				{
					resultMsg = egovMessageSource.getMessage("success.common.delete");
					
				}
				//에러
				else
				{
					resultMsg = egovMessageSource.getMessage("fail.common.delete");
				}
			}
			
		}
		
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	/**
	 * 강사 리스트 엑셀
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/tut/tutorExcelDown.do")
	public ModelAndView tutorExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = tutorService.selectTutorList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("tutorExcelView", "tutorMap", map);
	}
	
}
