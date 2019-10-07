package egovframework.adm.cou.controller;

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
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.adm.cou.service.GrSeqService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.sch.service.SearchBarsService;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class GrSeqController {
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
	
	
    /** grSeqService */
	@Resource(name = "grSeqService")
    private GrSeqService grSeqService;
	
	/** portalActionMainService */
	@Resource(name = "searchBarsService")
	SearchBarsService searchBarsService;
    

	/**
	 * 교육기수 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		String gadmin = (String) commandMap.get("gadmin");
		
		//관리자권한 코드 넣기 - 이에 따라 가져오는 값이 달라진다.
		if(gadmin != null)
			commandMap.put("gadminFirstString", gadmin.substring(0, 1));
		
		List<Map> list = (List<Map>) grSeqService.selectGrSeqEmptyList(commandMap);
		
		List<?> list1 = grSeqService.selectGrSeqList(commandMap);
		
		for(int i=0; i<list1.size(); i++)
		{
			Map m = (Map)list1.get(i);
			
			list.add(m);
		}
		
		
		
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/cou/grSeqList";
	}
	
	
	/**
	 * 기수 상세 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqDetailList.do")
	public String pageDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		List<?> list = grSeqService.selectGrSeqDetailList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/cou/grSeqDetailList";
	}
	
		
	/**
	 * 기수에 수강을 신청, 취소, 수료, 승인된 학습자의 리스트 정보
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqStudentList.do")
	public String pageStudentList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		List<?> list = grSeqService.selectGrSeqStudentList(commandMap);
		
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/cou/grSeqStudentList";
	}
	

	/**
	 * 기수 상세정보 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqDetailView.do")
	public String pageDetailView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
//		기수정보
        Map<?, ?> view = grSeqService.selectGrSeqDetailView(commandMap);
//		학교리스트
//        List<?> list = grSeqService.selectGrSeqSchoolList(commandMap);
        
        model.addAttribute("view", view);
//        model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
		
		// 2017 추가
		List<?> areaCodeList = grSeqService.selectAreaCodeList();
		model.addAttribute("areaCodeList", areaCodeList);
		String subjseqAreaCodeConcat = grSeqService.selectSubjseqAreaCodeConcat(commandMap);
		model.addAttribute("subjseqAreaCodeConcat", subjseqAreaCodeConcat);
		// 2017 추가 끝
		
		return "adm/cou/grSeqDetailView";
	}
	
	
	/**
	 * 교육기수추가 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqInsert.do")
	public String pageInsert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		List year_list = searchBarsService.selectSearchYearList(commandMap);
    	model.addAttribute("year_list", year_list);
    	
		model.addAllAttributes(commandMap);
		
		return "adm/cou/grSeqInsert";
	}
	
	
	/**
	 * 교육기수추가 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqUpdate.do")
	public String pageUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		Map view = grSeqService.selectGrSeqView(commandMap);
    	
		model.addAttribute("view", view);
		model.addAllAttributes(commandMap);
		
		return "adm/cou/grSeqUpdate";
	}
	
	
	/**
	 * 교육기수 등록/수정/삭제 작업처리
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqAction.do")
	public String pageAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "/adm/cou/grSeqInsert.do";
		if(p_process != null)
		{
			if(p_process.equals("insert"))
			{
				boolean isok = grSeqService.insertSubjSeq(commandMap);
				
				if(isok)
				{
					resultMsg = egovMessageSource.getMessage("success.common.insert");
					
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.insert");
				}
				model.addAttribute("isOpenerReload", "OK");
				model.addAttribute("isClose", "OK");
			}
			else if(p_process.equals("update"))
			{
				int cnt = grSeqService.updateGrSeq(commandMap);
				
				if(cnt > 0)
				{
					resultMsg = egovMessageSource.getMessage("success.common.update");
					model.addAttribute("isOpenerReload", "OK");
					model.addAttribute("isClose", "OK");
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.update");
				}
				
				
				url = "/adm/cou/grSeqUpdate.do";
			}
			
			else if(p_process.equals("delete"))
			{
				int proposeCnt = grSeqService.selectProposeCnt(commandMap);
				
				
				//해당 교육기수의 수강생이 없다면..
				if(proposeCnt == 0)
				{
					int cnt = grSeqService.deleteGrSeq(commandMap);
					
					if(cnt > 0)
					{
						resultMsg = egovMessageSource.getMessage("success.common.delete");
						model.addAttribute("isClose", "OK");
						model.addAttribute("isOpenerReload", "OK");
						
					}else{
						resultMsg = egovMessageSource.getMessage("fail.common.delete");
					}
				}
				else
				{
					resultMsg = egovMessageSource.getMessage("success.common.propose.not.delete");
				}
					
				url = "/adm/cou/grSeqUpdate.do";
			}
			
		}
		
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	
	/**
	 * 기수 등록/수정/삭제 작업처리
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqDetailAction.do")
	public String pageDetailAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model
			// 2017 추가
			, @RequestParam(value = "areaCode", required = false) String[] arrAreaCode)
			// 2017 추가 끝
					throws Exception {
		// 2017 추가
		commandMap.put("arrAreaCode", arrAreaCode);
		// 2017 추가 끝
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "/adm/cou/grSeqDetailList.do";
		
		boolean isok = false;
		
		
		if(p_process != null)
		{
			if(p_process.equals("insert"))
			{
				int seqCnt = Integer.parseInt((String)commandMap.get("p_seqcnt"));
				
				for(int i=0; i<seqCnt; i++)
				{
					isok = grSeqService.createAllSubjSeq(commandMap);
					
					if(isok == false) break;
				}
				
				if(isok)
				{
					resultMsg = egovMessageSource.getMessage("success.common.insert");
					
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.insert");
				}
			}
			else if(p_process.equals("update"))
			{
				log.info("p_eroomseq : " + commandMap.get("p_eroomseq"));
				
				int cnt = 0;
				
				int subjseq = Integer.parseInt(commandMap.get("p_subjseq") + "");
	            int newsubjseq = Integer.parseInt(commandMap.get("p_newsubjseq") + "");
	           
	            if (subjseq != newsubjseq) {
	            	cnt = grSeqService.selectCheckSubjseq(commandMap);
	            }
	            else
	            {
	            	// 그냥 넘어온 데이터를 비운다.
	            	commandMap.put("p_newsubjseq", "");
	            }
	            
	            if (cnt > 0)
	            {
	            	resultMsg = egovMessageSource.getMessage("fail.common.update.subjseq.same.count");
	            }
	            else
	            {
	            	isok = grSeqService.updateSubjseq(commandMap);
	            	
	            	if(isok)
					{
						resultMsg = egovMessageSource.getMessage("success.common.update");
						
					}else{
						resultMsg = egovMessageSource.getMessage("fail.common.update");
					}
	            	
	            }
	            
	            model.addAttribute("isClose", "OK");
				model.addAttribute("isOpenerReload", "OK");
	            
			}
			
			else if(p_process.equals("delete"))
			{
				isok = grSeqService.deleteSubjSeq(commandMap);
				
				if(isok)
				{
					resultMsg = egovMessageSource.getMessage("success.common.delete");
					
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.delete");
				}
			}
		}
		
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	/**
	 * 과정일괄 수정화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqUpdateSubjCourseList.do")
	public String pageUpdateSubjCourse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		Map view = grSeqService.selectGrSeqName(commandMap);
		model.addAttribute("view", view);
		
		List list = grSeqService.selectUpdateSubjCourseList(commandMap);
		model.addAttribute("list", list);
		
		
		model.addAllAttributes(commandMap);
		
		return "adm/cou/grSeqUpdateSubjCourseList";
	}
	
	
	
	/**
	 * 과정일괄 수정화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqAssignSubjCourseList.do")
	public String pageAssignSubjCourse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		Map view = grSeqService.selectGrSeqName(commandMap);
		model.addAttribute("view", view);
		
		List list = grSeqService.selectAssignSubjCourseList(commandMap);
		model.addAttribute("list", list);
		
		
		model.addAllAttributes(commandMap);
		
		return "adm/cou/grSeqAssignSubjCourseList";
	}
	
	
	/**
	 * 과정 일괄 수정 작업 (일일 최대학습량, 수강신청종료일시, 학습시작일시, 학습종료일시)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqUpdateSubjCourseAction.do")
	public String pageUpdateSubjCourseAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "/adm/cou/grSeqUpdateSubjCourseList.do";
		
		boolean isok = false;
		
		
		if(p_process != null)
		{
			//일일최대학습량 수정
			if(p_process.equals("updateEdulimit"))
			{
				isok = grSeqService.updateEdulimit(commandMap);
            	
            	if(isok)
				{
					resultMsg = egovMessageSource.getMessage("success.common.update");
					
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.update");
				}
			}
			//수강신청기간 수정
			else if(p_process.equals("updatePropose"))
			{
				isok = grSeqService.updatePropose(commandMap);
            	
            	if(isok)
				{
					resultMsg = egovMessageSource.getMessage("success.common.update");
					
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.update");
				}
			}
			//학습기간 수정
			else if(p_process.equals("updateEdu"))
			{
				isok = grSeqService.updateEdu(commandMap);
            	
            	if(isok)
				{
					resultMsg = egovMessageSource.getMessage("success.common.update");
					
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.update");
				}
			}
		}
		
		model.addAttribute("isClose", "OK");
		model.addAttribute("isOpenerReload", "OK");
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	
	/**
	 * 과정 일괄 지정 화면 작업 (일일 최대학습량, 수강신청종료일시, 학습시작일시, 학습종료일시, 일괄 과정 지정 연결)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/grSeqAssignSubjCourseAction.do")
	public String pageAssignSubjCourseAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "/adm/cou/grSeqAssignSubjCourseList.do";
		
		boolean isok = false;
		
		
		if(p_process != null)
		{
			//일일최대학습량 수정
			if(p_process.equals("edulimit"))
			{
				isok = grSeqService.updateGrSeqEdulimit(commandMap);
            	
            	if(isok)
				{
					resultMsg = egovMessageSource.getMessage("success.common.update");
					
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.update");
				}
			}
			//일괄과정 지정 
			else if(p_process.equals("assignSave"))
			{
				isok = grSeqService.assignSave(commandMap);
            	
            	if(isok)
				{
					resultMsg = egovMessageSource.getMessage("success.common.save");
					
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.save");
				}
			}
		}
		
		model.addAttribute("isClose", "OK");
		model.addAttribute("isOpenerReload", "OK");
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	/**
	 * 기수 복습 / 수료처리 여부 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/reviewIsclosedAction.do")
	public String reviewAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "/adm/cou/grSeqDetailList.do";
		
		int isok = 0;		
		isok = grSeqService.updateSubjseqReviewIsclosed(commandMap);
		System.out.println("isok ----> "+isok);
	           	
    	if(isok>0){
			resultMsg = egovMessageSource.getMessage("success.common.update");			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
    	
    	model.addAttribute("isClose", "OK");
		model.addAttribute("isOpenerReload", "OK");
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
}
