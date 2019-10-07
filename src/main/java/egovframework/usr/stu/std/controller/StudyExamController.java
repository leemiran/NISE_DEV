package egovframework.usr.stu.std.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ziaan.lcms.EduStartBean;

import egovframework.adm.exm.service.ExamAdmService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.usr.stu.std.service.StudyExamService;
import egovframework.usr.stu.std.service.StudyManageService;

@Controller
public class StudyExamController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudyExamController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** studyExamService */
	@Resource(name = "studyExamService")
	StudyExamService studyExamService;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	private StudyManageService studyManageService;
	
	 /** examAdmService */
	@Resource(name = "examAdmService")
    private ExamAdmService examAdmService;
	
	
	@RequestMapping(value="/usr/stu/std/userStudyExamList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		setSummaryInfo(commandMap, model);
		
		//시험
		List exam_list = studyManageService.selectExamList(commandMap);
		model.addAttribute("exam_list", exam_list);
		
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyExamList";
		
	}
	
	@RequestMapping(value="/usr/stu/std/userStudyCheckDuplicateIP.do")
	public String checkDuplicateIP(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss"); 
		
		cal.setTime(date);
		cal.add(Calendar.HOUR, -1);

		String user_now = sdformat.format(cal.getTime());
		
		commandMap.put("user_ip", request.getRemoteAddr());
		commandMap.put("user_now", user_now);
		
		Integer rtn = studyManageService.checkDuplicateIP(commandMap);
		model.addAttribute("rtn", rtn);
		
		return "usr/stu/std/userStudyCheckDuplicateIP";
	}
	
	@RequestMapping(value="/usr/stu/std/userStudyExamAnswerPage.do")
	public String detailPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String v_subj      = (String)commandMap.get("p_subj");
		String v_year      = (String)commandMap.get("p_year");
		String v_subjseq   = (String)commandMap.get("p_subjseq");		
		String p_exam_subj   = commandMap.get("p_exam_subj") != null ? (String)commandMap.get("p_exam_subj") : "";
		
		
		System.out.println("v_subj ----> "+v_subj);
		System.out.println("v_year ----> "+v_year);
		System.out.println("v_subjseq ----> "+v_subjseq);
		System.out.println("p_exam_subj ----> "+p_exam_subj);
		
		
		//보던문제 
		String ingExam = "N";
		//추가시험
		String retryExamChangeYn  ="N";
		
		//문제문행 변경
		String examBankchangeYn = "N";
		if(p_exam_subj.length() > 0){
			examBankchangeYn = "Y";
		}
		
		/*if(("PRF150001".equals(v_subj) && "2015".equals(v_year) && "0003".equals(v_subjseq)) || ("PRF150017".equals(v_subj) && "2015".equals(v_year) && "0001".equals(v_subjseq))){
			examBankchangeYn = "Y";
		}*/
		
		System.out.println("examBankchangeYn ----> "+examBankchangeYn);
		
		commandMap.put("examBankchangeYn", examBankchangeYn);
		
		String ses_search_subj = commandMap.get("p_subj").toString();
		String ses_search_year = commandMap.get("p_year").toString();
		String ses_search_subjseq = commandMap.get("p_subjseq").toString();		
		String p_lesson = "001";
		
		commandMap.put("ses_search_subj", ses_search_subj);
		commandMap.put("ses_search_year", ses_search_year);
		commandMap.put("ses_search_subjseq", ses_search_subjseq);
		commandMap.put("p_lesson", p_lesson);
		
		
		String examtabletemp = "temp";				//2차응시 테이블명 추가 문자(tz_examresulttemp)
		
		setSummaryInfo(commandMap, model);
		
		/**
		 * 1회응시 여부체크
		 */
		int exam_1 = studyExamService.getExamResultChk(commandMap);
		commandMap.put("examCnt", exam_1);

		/**
		 *  1차응시를 하였다면 
		 *  2회응시 여부체크를 체크한다.
		 */
		/*if(exam_1 > 0) {
			
			commandMap.put("examtabletemp", examtabletemp);
			
			int exam_2 = studyExamService.getExamResultChk(commandMap);
			
			if(exam_2 > 0)			//2차응시도 완료된 상태
			{
				commandMap.remove("examtabletemp");
			}
			else
			{
				// 2차응시가 가능한 상태
			}
			
		}*/
		
		
		List list = null;
		String resultMsg = "";
		
		if(exam_1 > 0) {
			//1차 시험 완료?
			String resultSubmit_1 =  studyExamService.selectExamResultSubmit(commandMap);
			if(resultSubmit_1.equals("Y")){	
				commandMap.put("examtabletemp", examtabletemp);
				//2차시험을 본적이 있는가?
				int exam_2 = studyExamService.getExamResultChk(commandMap);
				
				//2차시험을 완료 했는가?
				String resultSubmit_2 =  studyExamService.selectExamResultSubmit(commandMap);
				resultSubmit_2 = resultSubmit_2 !=null ? resultSubmit_2.toString() : "N";	
				
				
				//2차시험을 본적이 있으면
				if(exam_2>0){
					//2차시험 완료?
					if(resultSubmit_2.equals("Y")){
						// 추가시험 가능 여부 체크 (관리자에서 사용자 응시횟수 (재응시셋))						
						commandMap.remove("examtabletemp");						
						retryExamChangeYn = studyExamService.getRetryExamChangeYn(commandMap);  //userretry > 0 and submit_yn = 'Y'
						retryExamChangeYn = retryExamChangeYn !=null  ? retryExamChangeYn.toString() : "N";
						if("Y".equals(retryExamChangeYn)){							
							ingExam = "R";	//RETRY추가로 볼 때
							//문제 
							list = studyExamService.selectPaperQuestionExampleList(commandMap);
							model.addAttribute("PaperQuestionExampleList", list);
							
							String tmp = "";
							String p_answer = "";
							for(int i = 0;  i < list.size(); i++ ){
								Map m2 = (Map)list.get(i);
								if(!tmp.equals(m2.get("examnum").toString()) ){
									tmp = m2.get("examnum").toString();
									p_answer += "0,";
									System.out.println("p_answer  1111 ----> "+p_answer);
								}
							}
							p_answer = p_answer.substring(0, p_answer.length() - 1);
							System.out.println("p_answer  2222 ----> "+p_answer);
							System.out.println("ingExam  2222 ----> "+p_answer);
							commandMap.put("p_answer", p_answer);
							studyExamService.updateRetryExamChange(commandMap);							
						}
						
						
					}else{
						ingExam = "Y";
					}
				}
				
			}else{
				ingExam = "Y";
			}
			
			//보던 문제
			if(ingExam.equals("Y")){
				Map<String, Object> examResultInfo = studyExamService.getExamResultInfo(commandMap);
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				
				List<Object> examList = new ArrayList<Object>();
				int examIdx = 0;
				String[] myAnswerArr = String.valueOf(examResultInfo.get("answer")).split(",");
				for(String examnum: String.valueOf(examResultInfo.get("exam")).split(",")) {
					Map<String, Object> examMap = new HashMap<String, Object>();
					examMap.put("examnum", examnum);
					examMap.put("rn", examIdx + 1);
					examMap.put("myAnswer", myAnswerArr[examIdx]);
					
					examList.add(examMap);
					examIdx ++;
				}
				
				paramMap.put("examList", examList);
				paramMap.put("examsubj", p_exam_subj);
				
				list = studyExamService.getExamInProgress(paramMap);
				model.addAttribute("PaperQuestionExampleList", list);
			}
			//보던문제 end
		} 
		
		System.out.println("ingExam ----> "+ingExam);
		
		//처음시험
		if(ingExam.equals("N")){ 
			list = studyExamService.selectPaperQuestionExampleList(commandMap);
			model.addAttribute("PaperQuestionExampleList", list);
		}

		
		if(list == null || list.size() == 0) {
			resultMsg = "이미 평가가 완료되었습니다.";
		}// 평가 기간내에 있는지 확인 할것
		else if (list.size() > 0) {
			commandMap.put("p_subjsel", commandMap.get("p_subj") );
			commandMap.put("p_upperclass","ALL");
			Map data = studyExamService.getPaperData(commandMap);							// 문제지 설명정보         
			if ("N".equals(data.get("chck"))) {
				resultMsg = "이미 평가기간이 지났습니다.";
			} else {
				
				model.addAttribute("ExamPaperData", data);
				commandMap.remove("p_subjsel");
				
				
				String v_exam = "";
				String tmp = "";
				for(int i = 0;  i < list.size(); i++ ){
					Map m2 = (Map)list.get(i);
					if( !tmp.equals(m2.get("examnum").toString()) ){
						v_exam += m2.get("examnum")+",";
						tmp = m2.get("examnum").toString();
					}
				}
				commandMap.put("p_exam", v_exam);
				
				// 이전에 응시한 평가결과값이 존재하면 해당 결과의 시작 시간을 가져온다.
				Map dataMap = studyExamService.selectStartTime(commandMap);
				commandMap.put("extra_time", dataMap.get("extratime"));
				commandMap.put("started", dataMap.get("started"));
				commandMap.put("answer", dataMap.get("answer"));
				commandMap.put("dataMap", dataMap);
				
				int isOk = studyExamService.InsertResult(commandMap);
				
			}
			
		}
				
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyExamAnswerPage";
	}
	
	@RequestMapping(value="/usr/stu/std/userStudyExamAnswerFinish.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		int isOk = 0;
		String resultMsg = "";
		
		if(null != request.getSession().getAttribute("userid")) {
			String v_subj      = (String)commandMap.get("p_subj");
			String v_year      = (String)commandMap.get("p_year");
			String v_subjseq   = (String)commandMap.get("p_subjseq");		
			String p_exam_subj   = commandMap.get("p_exam_subj")  != null ? (String)commandMap.get("p_exam_subj") : "";
			
			System.out.println("v_subj ----> "+v_subj);
			System.out.println("v_year ----> "+v_year);
			System.out.println("v_subjseq ----> "+v_subjseq);
			
			//문제문행 변경
			String examBankchangeYn = "N";
			if(p_exam_subj.length() > 0){
				examBankchangeYn = "Y";
			}
			
			/*if(("PRF150001".equals(v_subj) && "2015".equals(v_year) && "0003".equals(v_subjseq)) || ("PRF150017".equals(v_subj) && "2015".equals(v_year) && "0001".equals(v_subjseq))){
			examBankchangeYn = "Y";
		}*/
			
			System.out.println("examBankchangeYn ----> "+examBankchangeYn);
			
			commandMap.put("examBankchangeYn", examBankchangeYn);
			commandMap.put("submitYn", "Y");
			
			isOk = studyExamService.InsertResult(commandMap);
			
			isOk = studyExamService.insertExamAnswerFinish(commandMap);
		}
		
		if( isOk > 0 ){
			resultMsg = "제출되었습니다.";
		}else{
			resultMsg = "제출에 실패하였습니다.";
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/usr/stu/std/userStudyExamList.do";
	}
	
	/**
	 * 사용자가 제출한 평가 미리 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyExamAnswerView.do")
	public String userStudyExamAnswerView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		//맵다시 처리하기
     	commandMap.put("ses_search_subj", commandMap.get("p_subj"));
     	commandMap.put("ses_search_gyear", commandMap.get("p_gyear"));
     	commandMap.put("ses_search_subjseq", commandMap.get("p_subjseq"));
     	commandMap.put("search_papernum", commandMap.get("p_papernum"));
		
		//전체 평균
		model.addAttribute("view", examAdmService.selectExamResultAvg(commandMap));
		
		//전체 문제 정보
		List paperList = examAdmService.selectUserPaperResultList(commandMap);
		model.addAttribute("paperList", paperList);
		
		//전체 답안정보 - 학습자
		Map userExamView = examAdmService.selectUserExamResultList(commandMap);
		model.addAttribute("userExamView", userExamView);
		
		
		//전체문제 답안정보 - 풀어서 보여주기
		List examList = examAdmService.selectUserExamResultAnswerList(userExamView);
		model.addAttribute("examList", examList);
		
		
		//맵 삭제
		commandMap.remove("ses_search_subj");
     	commandMap.remove("ses_search_gyear");
     	commandMap.remove("ses_search_subjseq");
     	commandMap.remove("search_papernum");
     	
     	
     	
		model.addAllAttributes(commandMap);
		
		return "usr/stu/std/userStudyExamAnswerView";
	}
	
	@RequestMapping(value="/usr/stu/std/popupExamAgree.do")
	public String popupExamAgree(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAllAttributes(commandMap);
		return "usr/stu/std/popupExamAgree";
	}
	
	
	
	public void setSummaryInfo(Map<String, Object> commandMap, ModelMap model) throws Exception{
		if( commandMap.get("studyPopup") == null || commandMap.get("studyPopup").toString().equals("") ){
			// 나의 진도율, 권장 진도율
			double progress = Double.parseDouble(studyManageService.getProgress(commandMap));
			double promotion = Double.parseDouble(studyManageService.getPromotion(commandMap));
			
			model.addAttribute("progress", String.valueOf(progress));
			model.addAttribute("promotion", String.valueOf(promotion));
			
			// 학습정보
			EduStartBean bean = EduStartBean.getInstance();
			List dataTime = studyManageService.SelectEduTimeCountOBC(commandMap);          // 학습시간,최근학습일,강의접근횟수
			model.addAttribute("EduTime", dataTime);
			
			Map data2 = studyManageService.SelectEduScore(commandMap);
			model.addAttribute("EduScore", data2);
			
			// 강사정보
			Map tutorInfo = studyManageService.getTutorInfo(commandMap);
			model.addAttribute("tutorInfo", tutorInfo);
			
			commandMap.put("p_grcode","N000001");
			commandMap.put("p_class","1");
			List list = studyManageService.selectListOrderPerson(commandMap);
			model.addAttribute("ReportInfo", list);
			
			// 총차시, 학습한 차시, 진도율, 과정구분
			Map map = studyManageService.getStudyChasi(commandMap);
			
			model.addAttribute("datecnt",    map.get("datecnt")); // 총차시
			model.addAttribute("edudatecnt", map.get("edudatecnt")); // 학습한 차시
			model.addAttribute("wstep",      map.get("wstep"));		 // 진도율
			model.addAttribute("attendCnt",      map.get("attendCnt"));		 // 출석개수
		}
	}
	
}
