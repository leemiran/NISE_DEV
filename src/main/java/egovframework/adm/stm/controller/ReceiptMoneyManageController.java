package egovframework.adm.stm.controller;

import java.util.ArrayList;
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

import egovframework.adm.stm.service.ReceiptMoneyManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;

import com.ziaan.research.SulmunQuestionExampleData;
import com.ziaan.research.SulmunSubjectReceiptMoneyData;

/**
 * 결제관리
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   수정일      		수정자           수정내용
 *  -------    		--------    ---------------------------
 *   2014.12.23  	유상도          	 최초 생성
 * </pre>
 */

@Controller
public class ReceiptMoneyManageController {
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
	
	/** studyExamService */
	@Resource(name = "receiptMoneyManageService")
    private ReceiptMoneyManageService receiptMoneyManageService;

	/**
	 * 과정별 입금 내역 조회
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stm/subjReceiptMoneyList.do")
	public String subjReceiptMoneyList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		log.error("과정별 입금 내역 조회");
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> totalList = null;
		if(commandMap.containsKey("admMenuInitOption") && "Y".equals(commandMap.get("admMenuInitOption"))){
			
		}else{
			resultList = receiptMoneyManageService.selectSubjReceiptMoneyList(commandMap);
			if(resultList.size() > 0){
				String payTotalCnt = resultList.get(0).get("payTotalCnt")+"";//징수인원
				String totalAmount = resultList.get(0).get("totalAmount")+"";//실입금액
				log.error("징수인원["+payTotalCnt+"]");
				log.error("실입금액["+totalAmount+"]");
				model.addAttribute("payTotalCnt", payTotalCnt);	
				model.addAttribute("totalAmount", totalAmount);	
				model.addAttribute("resultList", resultList);
				
				totalList = receiptMoneyManageService.selectSubjReceiptMoneyTotalList(commandMap);
				model.addAttribute("totalList", totalList);	
				
			}
		}
			
		model.addAllAttributes(commandMap);
		
		return "adm/stm/subjReceiptMoneyList";
	}
	
	/**
	 * 년도별 입금 내역 조회
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stm/yearReceiptMoneyList.do")
	public String yearReceiptMoneyList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		log.error("년도별 입금 내역 조회");
		
		String p_searchYn = commandMap.get("p_searchYN") !=null ? (String)commandMap.get("p_searchYN") : "N";
		
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> totalList = null;
		if("Y".equals(p_searchYn)){		
			list = receiptMoneyManageService.selectSubjReceiptMoneyList(commandMap);		
		
			String payTotalCnt = list.get(0).get("payTotalCnt")+"";//징수인원
			String totalAmount = list.get(0).get("totalAmount")+"";//실입금액
			log.error("징수인원["+payTotalCnt+"]");
			log.error("실입금액["+totalAmount+"]");
			model.addAttribute("payTotalCnt", payTotalCnt);	
			model.addAttribute("totalAmount", totalAmount);	
			model.addAttribute("list", list);	
			
			
			totalList = receiptMoneyManageService.selectSubjReceiptMoneyTotalList(commandMap);
			model.addAttribute("totalList", totalList);	
			
		}
		model.addAllAttributes(commandMap);
		
		return "adm/stm/yearReceiptMoneyList";
	}
	
	/**
	 * 과정별 입금 내역 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	@RequestMapping(value="/adm/stm/subjReceiptMoneyExcelDown.do")
	public String subjReceiptMoneyExcelDown(HttpServletRequest request,HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		log.error("과정별 입금 내역 엑셀 다운로드");
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
			resultList = receiptMoneyManageService.selectSubjReceiptMoneyList(commandMap);
			String payTotalCnt = resultList.get(0).get("payTotalCnt")+"";//징수인원
			String totalAmount = resultList.get(0).get("totalAmount")+"";//실입금액
			log.error("징수인원["+payTotalCnt+"]");
			log.error("실입금액["+totalAmount+"]");
			ArrayList list = new ArrayList();
			
			SulmunSubjectReceiptMoneyData data =null;
			for(int i=0 ; i < resultList.size() ; i++){
				data = new SulmunSubjectReceiptMoneyData();
				data.setSubj(nullChack(resultList.get(i).get("subj")));
				data.setSubjnm(nullChack(resultList.get(i).get("subjnm")));
				data.setSubjseqLag(nullChack(resultList.get(i).get("subjseqLag")));
				data.setSubjseq(nullChack(resultList.get(i).get("subjseq")));
				data.setSubjseqLead(nullChack(resultList.get(i).get("subjseqLead")));
				data.setEdustart(nullChack(resultList.get(i).get("edustart")));
				data.setEduend(nullChack(resultList.get(i).get("eduend")));
				data.setEdutimes(nullChack(resultList.get(i).get("edutimes")));
				data.setBiyong(nullChack(resultList.get(i).get("biyong")));
				data.setEdumans(nullChack(resultList.get(i).get("edumans")));
				data.setDeptNm(nullChack(resultList.get(i).get("deptNm")));
				data.setPay(nullChack(resultList.get(i).get("pay")));
				data.setPayCnt(nullChack(resultList.get(i).get("payCnt")));
				data.setIsgraduatedYCnt(nullChack(resultList.get(i).get("isgraduatedYCnt")));
				data.setIsgraduatedNCnt(nullChack(resultList.get(i).get("isgraduatedNCnt")));
				data.setIsgraduatedYDeptcnt(nullChack(resultList.get(i).get("isgraduatedYDeptcnt")));
				data.setIsgraduatedNDeptcnt(nullChack(resultList.get(i).get("isgraduatedNDeptcnt")));
				data.setIsgraduatedYSubjseqcnt(nullChack(resultList.get(i).get("isgraduatedYSubjseqcnt")));
				data.setIsgraduatedNSubjseqcnt(nullChack(resultList.get(i).get("isgraduatedNSubjseqcnt")));
				data.setPayDeptCnt(nullChack(resultList.get(i).get("payDeptCnt")));
				data.setPaySubjseqCnt(nullChack(resultList.get(i).get("paySubjseqCnt")));
				data.setPaySubjCnt(nullChack(resultList.get(i).get("paySubjCnt")));
				data.setPayTotalCnt(nullChack(resultList.get(i).get("payTotalCnt")));
				data.setSubjseqTot(nullChack(resultList.get(i).get("subjseqTot")));
				data.setTotCnt(nullChack(resultList.get(i).get("totCnt")));
				data.setPayAmount(nullChack(resultList.get(i).get("payAmount")));
				data.setDeptAmount(nullChack(resultList.get(i).get("deptAmount")));
				data.setSubjseqAmount(nullChack(resultList.get(i).get("subjseqAmount")));
				data.setSubjAmount(nullChack(resultList.get(i).get("subjAmount")));
				data.setTotalAmount(nullChack(resultList.get(i).get("totalAmount")));
				data.setDeptLag(nullChack(resultList.get(i).get("deptLag")));
				data.setDeptIdx(nullChack(resultList.get(i).get("deptIdx")));
				data.setDeptLead(nullChack(resultList.get(i).get("deptLead")));
				data.setAllSubjseqCnt(nullChack(resultList.get(i).get("allSubjseqCnt")));
				data.setAllDeptCnt(nullChack(resultList.get(i).get("allDeptCnt")));
				data.setDeptCnt(nullChack(resultList.get(i).get("deptCnt")));
				data.setAuthDate(nullChack(resultList.get(i).get("authDate")));
				data.setAllDeptPayCnt(nullChack(resultList.get(i).get("allDeptPayCnt")));
				data.setType(nullChack(resultList.get(i).get("type")));
				data.setDpDeptAmount(nullChack(resultList.get(i).get("dpDeptAmount")));
				data.setDpTotalAmount(nullChack(resultList.get(i).get("dpTotalAmount")));
				data.setDeptCd(nullChack(resultList.get(i).get("deptCd")));
				data.setYear(nullChack(resultList.get(i).get("year")));
				data.setoSubjseq(nullChack(resultList.get(i).get("oSubjseq")));
				data.setDepositDate(nullChack(resultList.get(i).get("depositDate")));
				data.setPayLag(nullChack(resultList.get(i).get("payLag")));
				data.setPayNm(nullChack(resultList.get(i).get("payNm")));
				data.setReamount(nullChack(resultList.get(i).get("reamount")));
				data.setReamountcnt(nullChack(resultList.get(i).get("reamountcnt")));
				data.setBiyong2(nullChack(resultList.get(i).get("biyong2")));
				
				
				
				list.add(data);
			}
			
			
			String p_searchYn = commandMap.get("p_searchYN") !=null ? (String)commandMap.get("p_searchYN") : "N";
			
			
			List<Map<String, Object>> totalList = null;
			if("Y".equals(p_searchYn)){		
				totalList = receiptMoneyManageService.selectSubjReceiptMoneyTotalList(commandMap);
				model.addAttribute("totalList", totalList);	
			}
			
			
			
			model.addAllAttributes(commandMap);
			
			model.addAttribute("payTotalCnt", payTotalCnt);	
			model.addAttribute("totalAmount", totalAmount);	
			model.addAttribute("resultList", resultList);
			model.addAttribute("list", list);
					
			return "adm/stm/subjReceiptMoneyListxls";
	}
	
	private String nullChack(Object param){
		String returnParam="";
		if(param != null){
			returnParam = param+"";
		}
		return returnParam;
	}
	
	/**
	 * RD출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stm/subjReceipMomeyPrint.do")
	public String subjReceipMomeyPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
				
		String return_page = "adm/stm/subjReceiptMoneyPrint";
		String ses_search_gyear = (String)commandMap.get("ses_search_gyear");
		
		if(ses_search_gyear.equals("")){
			ses_search_gyear = "-1";
		}
		if(commandMap.get("rd_gubun").equals("B")){
			return_page = "adm/stm/yearReceiptMoneyPrint";
		}
		commandMap.remove("ses_search_gyear");
		commandMap.put("ses_search_gyear", ses_search_gyear);
		model.addAllAttributes(commandMap);
		
		return return_page;
	}
	
	
	/**
	 * 교육청 수납일자
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stm/subjReceipMomeyObIpdate.do")
	public String subjReceipMomeyObIpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		log.debug("교육청 수납일자");
		
		Object subjseqDepositDate = receiptMoneyManageService.selectSubjseqDepositDate(commandMap);
		
		model.addAttribute("view", subjseqDepositDate);
		model.addAllAttributes(commandMap);
		
		return "adm/stm/subjReceipMomeyObIpdate";
	}
	
	
	/**
	 * 교육청 수납일자 등록/수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/subjReceipMomeyObIpdateAction.do")
	public String subjReceipMomeyObIpdateAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		log.debug("교육청 수납일자 등록/수정");
	
		List<Map<String, Object>> list = null;
				
		int subjseqDepositDateCnt = receiptMoneyManageService.selectSubjseqDepositDateCnt(commandMap);
		
		int isOk = 0;
		String resultMsg = "";
		if(subjseqDepositDateCnt == 0){
			//등록
			isOk = receiptMoneyManageService.insertSubjseqDepositDate(commandMap);
			if(isOk == 1)			{
				resultMsg = egovMessageSource.getMessage("success.common.insert");
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.insert");
			}
		}
		
		if(subjseqDepositDateCnt == 1){
			//수정
			isOk = receiptMoneyManageService.updateSubjseqDepositDate(commandMap);	
			if(isOk == 1)	
			{
				resultMsg = egovMessageSource.getMessage("success.common.update");
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.update");
			}
		}
		
		model.addAttribute("isClose", "OK");
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		String url = "adm/stm/subjReceiptMoneyList.do";
		
		return "forward:" + url;
	}
	
	
	/**
	 * 교육청 수납일자 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stm/subjReceipMomeyObIpdateDelete.do")
	public String subjReceipMomeyObIpdateDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		log.debug("교육청 수납일자 삭제");	
		
		String resultMsg = "";
		boolean isOk = receiptMoneyManageService.deleteSubjseqDepositDate(commandMap);
		
		if(isOk){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/stm/subjReceiptMoneyList.do";
		
	}
	
	
	
	/**
	 * 년도별 입금 내역 조회
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stm/yearMoneyTotalizationList.do")
	public String yearMoneyTotalizationList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		log.error("년도별 입금 내역 조회");
		//String p_searchYn = commandMap.get("p_searchYN") !=null ? (String)commandMap.get("p_searchYN") : "N";
		
		String returnUrl = "adm/stm/yearMoneyTotalizationList";
		String viewPage = commandMap.get("viewPage") != null ? commandMap.get("viewPage").toString() : "";
		if("E".equals(viewPage)){
			returnUrl = "adm/stm/yearMoneyTotalizationExcel";
		}
		List<Map<String, Object>> resultList = receiptMoneyManageService.selectYearMoneyTotalizationList(commandMap);		
		model.addAttribute("resultList", resultList);
		
		List<Map<String, Object>> totalList = receiptMoneyManageService.selectYearMoneyTotalizationTotalList(commandMap);
		model.addAttribute("totalList", totalList);
		
		
		model.addAllAttributes(commandMap);
		
		if(resultList.size() > 0){
			model.addAttribute("totalCnt", resultList.size()+"");
		}else{
			model.addAttribute("totalCnt", "0");
		}
		return returnUrl;
	}
	
	
	/**
	 * 연도별입금내역 데이터 생성
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stm/yearReceiptMoneyStatus.do")
	public String yearReceiptMoneyStatus(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int isOk = 0;
		String resultMsg = "";
		
		System.out.println("aa");
		
		//등록
		isOk = receiptMoneyManageService.insertYearReceiptMoneyStatus(commandMap);
		if(isOk == 1)			{
			resultMsg = egovMessageSource.getMessage("success.common.insert");
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		
		model.addAttribute("resultMsg", "데이터생성이 정상적으로 처리 되었습니다.");
		model.addAllAttributes(commandMap);
		return "forward:/adm/stm/yearMoneyTotalizationList.do";
	}
	
	/**
	 * 연도별입금내역 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/stm/yearReceiptMoneyStatusUpdate.do")
	public String admYearEduStatusUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		int result = receiptMoneyManageService.yearReceiptMoneyStatusUpdate(commandMap);		
		
		model.addAttribute("resultMsg", egovMessageSource.getMessage("success.common.update"));
		model.addAllAttributes(commandMap);
		return "forward:/adm/stm/yearMoneyTotalizationList.do";
	}
	
}
