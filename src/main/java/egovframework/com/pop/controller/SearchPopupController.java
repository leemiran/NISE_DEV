package egovframework.com.pop.controller;

import java.net.URLDecoder;
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

import egovframework.adm.cfg.mem.service.MemberSearchService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.pop.service.SearchPopupService;

@Controller
public class SearchPopupController {

	/** log */
	protected static final Log log = LogFactory.getLog(SearchPopupController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
    @Resource(name = "pagingManageController")
    private PagingManageController pagingManageController;
	
	/** searchPopupService */
	@Resource(name = "searchPopupService")
	SearchPopupService searchPopupService;
	
	/** MemberSearchService */
	@Resource(name = "memberSearchService")
	MemberSearchService memberSearchService;
	
	/**
	 * 교육그룹선택팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/pop/searchGrcodePopup.do")
	public String grcodeListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = searchPopupService.searchGrcodeListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = searchPopupService.searchGrcodeList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "com/pop/searchGrcodePopup";
	}
	
	/**
	 * 과정선택팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/pop/searchSubjPopup.do")
	public String subjListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = searchPopupService.searchSubjListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = searchPopupService.searchSubjList(commandMap);
		model.addAttribute("list", list);
				
		model.addAllAttributes(commandMap);
		return "com/pop/searchSubjPopup";
	}
	
	/**
	 * 회사선택팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/pop/searchCompPopup.do")
	public String compListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String url = "com/pop/searchCompPopup2";
		int totCnt = searchPopupService.searchCompListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = searchPopupService.searchCompList(commandMap);
		model.addAttribute("list", list);
		
		if( commandMap.get("p_key2") != null ){
			String v_key2 = (String)commandMap.get("p_key2");
			if( v_key2.equals("p_comp") || v_key2.equals("p_cp") || v_key2.equals("p_producer") || v_key2.equals("p_owner") || v_key2.equals("p_cpinfo") || v_key2.equals("p_bpinfo") || v_key2.equals("p_cpbpinfo") || v_key2.equals("p_cpasinfo") ){
				url = "com/pop/searchCompPopup";
			}
		}else{
			url = "com/pop/searchCompPopup2";
		}
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	/**
	 * 회원선택팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/pop/searchMemberPopup.do")
	public String memberListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		int totCnt = searchPopupService.searchMemberListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = searchPopupService.searchMemberList(commandMap);
		model.addAttribute("list", list);
		
		
		model.addAllAttributes(commandMap);
		return "com/pop/searchMemberPopup";
	}
	
	
	
	/**
	 * 개별회원정보팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/pop/searchMemberInfoPopup.do")
	public String memberInfoPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", searchPopupService.searchMemberInfoPopup(commandMap));
		
		model.addAllAttributes(commandMap);
		return "com/pop/searchMemberInfoPopup";
	}
	

	/**
	 * 강사선택팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/pop/searchTutorPopup.do")
	public String tutorListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		int totCnt = searchPopupService.searchTutorListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = searchPopupService.searchTutorList(commandMap);
		model.addAttribute("list", list);
		
		
		model.addAllAttributes(commandMap);
		return "com/pop/searchTutorPopup";
	}
	

	/**
	 * 우편번호검색팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/pop/searchZipcodePopup.do")
	public String zipcodeListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List sidoList = searchPopupService.selectSido(commandMap);
		model.addAttribute("sidoList", sidoList);
		
		if( commandMap.get("search_dong") != null && !commandMap.get("search_dong").toString().equals("") ){
			
			List list = null;
			if( commandMap.get("p_gubun") != null && commandMap.get("p_gubun").toString().equals("road") ){
				
				
				
				list = searchPopupService.searchZipcodeRoadList(commandMap);
			}
			else {
				list = searchPopupService.searchZipcodeList(commandMap);
			}			
			
			model.addAttribute("list", list);
		}
		
		
		model.addAllAttributes(commandMap);
		return "com/pop/searchZipcodePopup";
	}
	
	/**
	 * 아이디 중복검색 팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	 @RequestMapping(value="/com/pop/existIdPopup.do")
	public String existIdPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int existCount = memberSearchService.selectExistId(commandMap);
		if( existCount == 0 ){
			commandMap.put("isOk", true);
		}
		model.addAllAttributes(commandMap);
		return "com/pop/existIdPopup";
	}
	 
	 /**
     * gugun 리스트 - ajax
     */

    @RequestMapping(value = "/com/pop/searchGugun.do") 
    public ModelAndView scaleViewAjax( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	
    	// 로컬 한글깨짐 방지를 위해 인코딩하기
        /*String sido =URLDecoder.decode(commandMap.get("sido").toString());        
        System.out.println("sido ----> "+sido);        
        commandMap.put("sido", sido);*/
        
        Object result = null;
        ModelAndView modelAndView = new ModelAndView();
        
        List<?> list = searchPopupService.selectGugun(commandMap);

        Map resultMap = new HashMap();
        resultMap.put("result", list);
        modelAndView.addAllObjects(resultMap);
        modelAndView.setViewName("jsonView");
        return modelAndView;
    }
    
    /**
	 * 과정선택팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/pop/searchManagerSubjPopup.do")
	public String managerSubjListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = searchPopupService.searchManagerSubjListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = searchPopupService.searchManagerSubjList(commandMap);
		model.addAttribute("list", list);
				
		model.addAllAttributes(commandMap);
		return "com/pop/searchManagerSubjPopup";
	}
	
		
}