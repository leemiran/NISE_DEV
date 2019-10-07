package egovframework.adm.prop.controller;

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

import egovframework.adm.prop.service.ApprovalService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class StudentManagerController {
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
	
	
    /** approvalService */
	@Resource(name = "approvalService")
    private ApprovalService approvalService;
	
	

	/**
	 * 수강생관리 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/studentManagerList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		Map<?, ?> view = approvalService.selectStudentManagerIsClosed(commandMap);
		model.addAttribute("view", view);	
		
		List<?> seqList = approvalService.selectSubjSeqList(commandMap);
		model.addAttribute("seqList", seqList);
		
		List<?> list = null; 
		if(commandMap.get("ses_search_subjseq") !=null){
			list = approvalService.selectStudentManagerList(commandMap);
		}
		
		model.addAttribute("list", list);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/prop/studentManagerList";
	}
	
	
	/**
	 * 수강생관리 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/studentManagerExcelDown.do")
	public ModelAndView excelDownload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		

		List<?> list = approvalService.selectStudentManagerList(commandMap);
		model.addAttribute("list", list);	
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("studentManagerExcelView", "studentManagerMap", map);
	}
	
	
	/**
	 * 선정할 교육대상자 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/acceptTargetMemberList.do")
	public String acceptTargetMemberList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		Map<?, ?> view = approvalService.selectStudentManagerIsClosed(commandMap);
		model.addAttribute("view", view);	
		
		
		List<?> list = approvalService.selectAcceptTargetMemberList(commandMap);
		model.addAttribute("list", list);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/prop/acceptTargetMemberList";
	}
	
	
	/**
	 * 선정할 교육대상자 추가하기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/acceptTargetMemberAction.do")
	public String acceptTargetMemberAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		String resultMsg = "";
		String url = "/adm/prop/acceptTargetMemberList.do";
		
		
		Map<String, Object> resultMap = approvalService.acceptTargetMember(commandMap);
		
		boolean isok = (Boolean) resultMap.get("isok");
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.save");
		}else{
			if(null != resultMap.get("missAreaCodeMsg")) {
				resultMsg = resultMap.get("missAreaCodeMsg").toString();
			} else {
				resultMsg = egovMessageSource.getMessage("fail.common.save");
			}
		}
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	/**
	 * 수강신청 처리상태(승인, 취소, 반려, 삭제) 이하 정보 수정 및 수강생으로 등록처리 프로세스
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/studentManagerAction.do")
	public String studentManagerAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/adm/prop/studentManagerList.do";
		
		boolean isok = approvalService.studentManagerProcess(commandMap);
		
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
	 *  수강신청 결제 방법 변경 페이지(무통장, 교육청일경우 결제계좌에서 전송이 안되어 없을시 변경해줌
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/studentPayTypeView.do")
	public String studentPayTypeView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		model.addAllAttributes(commandMap);
		return "adm/prop/studentPayTypeView";
	}
	
	
	
	
	/**
	 *  수강신청 결제 방법 변경 프로세스(무통장, 교육청일경우 결제계좌에서 전송이 안되어 없을시 변경해줌
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/studentPayTypeAction.do")
	public String studentPayTypeAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/adm/prop/studentPayTypeView.do";
		
		boolean isok = approvalService.studentPayTypeProcess(commandMap);
		
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
     * 설문 척도 보기 리스트 - ajax
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */

    @RequestMapping(value = "/adm/prop/updateMemberSubjSeqInfo.do") 
    public ModelAndView updateMemberSubjSeqInfo( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
        Object result = null;
        ModelAndView modelAndView = new ModelAndView();
        
        Map<String, Object> map = approvalService.updateMemberSubjSeqInfo(commandMap);

        Map resultMap = new HashMap();
        resultMap.put("result", map.get("resultYn"));
        modelAndView.addAllObjects(resultMap);
        modelAndView.setViewName("jsonView");
        return modelAndView;
    }
    
    /**
	 *  기수변경 화면으로 이동
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/studentManagerView.do")
	public String studentManagerView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		log.error(commandMap);
		
		//List<Map<String, Object>> resultList = approvalService.studentManagerView(commandMap);		
		//최초 신청 기수
		Map subjseqInfo = approvalService.selectStudentSubjseqView(commandMap);
		
		//재수강 가능한 기수 리스트 조회
		List<Map<String, Object>> resultList = approvalService.studentSubjseqList(commandMap);
		
		model.addAttribute("subjseqInfo", subjseqInfo);
		model.addAttribute("resultList", resultList);
		
		model.addAllAttributes(commandMap);
		
		return "adm/prop/studentManagerView";
	}
	
	/**
	 *  기수변경
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/updateSubjseq.do")
	public String updateSubjseq(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		log.error("기수변경");
		
		//Map<String, Object> map = approvalService.updateMemberSubjSeqInfo(commandMap);
		
		try {
		
			Map<String, Object> map = approvalService.updateMemberSubjseqProc(commandMap);
			
			model.addAttribute("map", map);
			
			String resultMsg = "";
			
			if("Y".equals(map.get("resultYn")))
			{
				resultMsg = egovMessageSource.getMessage("success.common.save");
				
			}else if("A".equals(map.get("resultYn"))){
				
				resultMsg = egovMessageSource.getMessage("common.isExist.msg");
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.save");
			}
			
			model.addAttribute("resultMsg", resultMsg);
			
			model.addAllAttributes(commandMap);
			
			
		} catch (Exception e) {
			System.out.println("e -------> "+e);
		}
		return "adm/prop/studentManagerView";
	}
	
}
