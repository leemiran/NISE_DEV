package egovframework.adm.stu.controller;

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

import sun.security.jca.GetInstance.Instance;

import egovframework.adm.stu.service.StuMemberService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class StuMemberController {
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
	
	
    /** stuMemberService */
	@Resource(name = "stuMemberService")
    private StuMemberService stuMemberService;
	
	

	/**
	 * 입과명단 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/stuMemberList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectStuMemberList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/stu/stuMemberList";
	}
	
	
	
	/**
	 * 종합학습현황 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/totalScoreMemberList.do")
	public String totalScoreList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
//		int totCnt = stuMemberService.selectTotalScoreMemberTotCnt(commandMap);
//        pagingManageController.PagingManage(commandMap, model, totCnt);
        
		
		List<?> list = null;
		if(commandMap.get("ses_search_grseq") !=null){
			list = stuMemberService.selectTotalScoreMemberList(commandMap);
		}
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/stu/totalScoreMemberList";
	}

	
	/**
	 * 입과인원 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/stuMemberCountList.do")
	public String stuMemberCountList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectStuMemberCountList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/stu/stuMemberCountList";
	}
	
	
	
	/**
	 * 온라인출석부 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/lectLearningTimeList.do")
	public String lectLearningTimeList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = null;
		if(commandMap.get("ses_search_subjseq") !=null){
			list = stuMemberService.selectLearningTimeList(commandMap);
		}
		
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/stu/lectLearningTimeList";
	}
	
	/**
	 * 개인별 온라인 학습시간 팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/personalTimeListPopUp.do")
	public String personalTimeListPopUp(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectPersonalTimeList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/stu/personalTimeListPopUp";
	}
	
	/**
	 * 수강완료처리
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/personalTimeAction.do")
	public String personalTimeAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String[] p_check = EgovStringUtil.getStringSequence(commandMap, "p_check_real");
		String p_subj = (String)commandMap.get("p_subj");
		String p_userid = (String)commandMap.get("p_userid");
		String p_subjseq = (String)commandMap.get("p_subjseq");
		String p_year = (String)commandMap.get("p_year");
		
		for(int i=0; i<p_check.length; i++){
			//System.out.println("=========  p_check["+i+"] :"+p_check[i]);
			String[] check = p_check[i].split(",");
			
			Map mm = new HashMap();
			mm.put("p_module", check[0]);
			mm.put("p_status", check[1]);
			mm.put("p_checked", check[2]);
			mm.put("p_subj", p_subj);
			mm.put("p_userid", p_userid);
			mm.put("p_subjseq", p_subjseq);
			mm.put("p_year", p_year);
			
			//System.out.println("@@@@@@@@@@@p_module "+mm.get("p_module")+"");
			//System.out.println("@@@@@@@@@@@p_status "+mm.get("p_status")+"");
			//System.out.println("@@@@@@@@@@@p_checked "+mm.get("p_checked")+"");
			//System.out.println("@@@@@@@@@@@p_subj "+mm.get("p_subj")+"");
			//System.out.println("@@@@@@@@@@@p_subjseq "+mm.get("p_subjseq")+"");
			//System.out.println("@@@@@@@@@@@p_userid "+mm.get("p_userid")+"");
			//System.out.println("@@@@@@@@@@@p_year "+mm.get("p_year")+"");
			
			if(mm.get("p_checked").equals("Y")){
				if(mm.get("p_status").equals("A")){
					//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 학습전 - 수료완료 인서트 ");
					stuMemberService.progressAction(mm, "insert");
				}else if(mm.get("p_status").equals("N")){
					stuMemberService.progressAction(mm, "delete");
					stuMemberService.progressAction(mm, "insert");
					//stuMemberService.progressAction(mm, "update");
				//	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 학습중 - 수료완료 업데이트 ");
				}else{
					//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 아무것도 안해 ");
				}
			}else{
				if(mm.get("p_status").equals("A")){
					//System.out.println("************************  아무것도 하지마 ");
				}else{
					stuMemberService.progressAction(mm, "delete");
					//System.out.println("************************  초기화  ");
				}
			}
		}
		
		HashMap inputMap = new HashMap();
		inputMap.put("subj", p_subj);
		inputMap.put("userid", p_userid);
		inputMap.put("subjseq", p_subjseq);
		inputMap.put("year", p_year);
		
		//진도율 저장
		stuMemberService.updateProgress(inputMap);
		
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/stu/personalTimeListPopUp.do";
	}
	
	/**
	 * 개인별 출석부 팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/personalAttendListPopUp.do")
	public String personalAttendListPopUp(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectAttendList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/stu/personalAttendListPopUp";
	}
	
	/**
	 * 개인별 출석부 등록 및 삭제(수정작업)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/personalAttendAction.do")
	public String personalAttendAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/adm/stu/personalAttendListPopUp.do";
		
		boolean isok = stuMemberService.updateAttendList(commandMap);
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.save");
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.save");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	/**
	 * 온라인출석부 엑셀
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/lectLearningTimeExcelDown.do")
	public ModelAndView lectLearningTimeExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectLearningTimeList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("lectLearningTimeExcelView", "lectLearningTimeMap", map);
	}
	
	/**
	 * 수강번호 생성 로직
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/stuMemberAction.do")
	public String pageapprovalAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/adm/stu/stuMemberList.do";
		
		boolean isok = stuMemberService.updateStuMemberExamNum(commandMap);
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.save");
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.save");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	
	/**
	 * 입과명단 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/stuMemberExcelDown.do")
	public ModelAndView excelDownload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List<?> list = stuMemberService.selectStuMemberList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("stuMemberExcelView", "stuMemberMap", map);
	}
	
	/**
	 * 종합학습현황 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/totalScoreMemberExcelDown.do")
	public ModelAndView totalScoreMemberExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List<?> list = stuMemberService.selectTotalScoreMemberList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("totalScoreMemberExcelView", "totalScoreMemberMap", map);
	}
	
	
	/**
	 * 입과인원 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stu/stuMemberCountExcelDown.do")
	public ModelAndView countExcelDownload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List<?> list = stuMemberService.selectStuMemberCountList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("stuMemberCountExcelView", "stuMemberCountMap", map);
	}
}
