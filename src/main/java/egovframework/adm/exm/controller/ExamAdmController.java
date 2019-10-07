package egovframework.adm.exm.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ziaan.library.StringManager;

import egovframework.adm.exm.service.ExamAdmService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.usr.stu.std.service.StudyExamService;

@Controller
public class ExamAdmController {
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
	
	
    /** examAdmService */
	@Resource(name = "examAdmService")
    private ExamAdmService examAdmService;
	
	/** studyExamService */
	@Resource(name = "studyExamService")
    private StudyExamService studyExamService;

	/**
	 * 평가문제 리스트 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestList.do")
	public String examQuestList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = examAdmService.selectExamQuestList(commandMap);
		
		model.addAttribute("list", list);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examQuestList";
	}
	
	
	/**
	 * 평가문제 보기(등록/수정) 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestView.do")
	public String examQuestView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
//		문제정보
		model.addAttribute("view", examAdmService.selectQuestExamView(commandMap));
		
//		답안정보
		List<?> list = examAdmService.selectQuestExamSelList(commandMap);
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examQuestView";
	}
	
	/**
	 * 평가 Pool 등록 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestActionView.do")
	public String examQuestActionView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {

		model.addAllAttributes(commandMap);
		
		return "adm/exm/examQuestPoolList";
	}
	
	/**
	 * 평가 문제 복사하기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestPoolCopy.do")
	public String examQuestPoolCopy(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		log.error("평가 문제 복사하기");
		Map<String, Object> result = examAdmService.examQuestPoolCopy(commandMap);
		
		commandMap.put("p_subj", commandMap.get("t_subj"));
		commandMap.put("p_subjnm", commandMap.get("t_subjnm"));
		
		List list = examAdmService.examQuestionDetailList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		model.addAttribute("resultMsg", egovMessageSource.getMessage("success.common.save"));
		
		return "adm/exm/examQuestPoolDetailList";
	}
	
	/**
	 * 평가문제 미리보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestPreview.do")
	public String examQuestPreview(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = examAdmService.selectExamQuestPreviewList(commandMap);
		
		model.addAttribute("list", list);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examQuestPreview";
	}
	
	/**
	 * 평가 문제 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestAction.do")
	public String examQuestAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "forward:/adm/exm/examQuestView.do";
		
		
		
		//선택된 모든 문제 삭제..[사용되어진 문제는 삭제 하지 않는다.]
		if(p_process.equals("checkDelete"))
		{
			boolean isok = examAdmService.deleteQuestCheckExamList(commandMap);
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common.delete");
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common.delete");
			
			url = "forward:/adm/exm/examQuestList.do";
		}
		//pool 등록
		else if(p_process.equals("poolinsert"))
		{
			boolean isok = examAdmService.insertTzExamPoolSelect(commandMap);
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common.save");
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common.save");
			
			url = "forward:/adm/exm/examQuestPoolList.do";
		}
		//문제등록
		else if(p_process.equals("insert"))
		{
			boolean isok = examAdmService.insertTzExam(commandMap);
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
			
		}
		//문제수정
		else if(p_process.equals("update"))
		{
			int isok = examAdmService.updateTzExam(commandMap);
			
			if(isok == -1)		//사용중인 문항
				resultMsg = egovMessageSource.getMessage("fail.common.status.count.save");
			else if(isok == -2)		//사용중인 문항//에러
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			else				//정상처리
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
		}
		//문제삭제
		else if(p_process.equals("delete"))
		{
			//체크된 문제삭제 로직을 다시쓰기 위하여 p_checks 에 문제번호를 등록한다.
			String p_examnum = commandMap.get("p_examnum")+"";
			
			commandMap.put("p_checks", p_examnum);
			boolean isok = examAdmService.deleteQuestCheckExamList(commandMap);
			commandMap.remove("p_checks");
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
		}
		
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	/**
	 * 평가마스터 리스트 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examMasterList.do")
	public String examMasterList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = examAdmService.selectExamMasterList(commandMap);
		
		model.addAttribute("list", list);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examMasterList";
	}
	
	
	/**
	 * 평가마스터 Step01
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examMasterViewStep01.do")
	public String examMasterViewStep01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_lesson = commandMap.get("p_lesson") + "";
		String p_examtype = commandMap.get("p_examtype") + "";
		int paperCnt = 0;
		
		
		if(p_examtype != null && p_lesson != null)
		{
			//평가마스터 정보
			Map masterMap = examAdmService.selectExamMasterView(commandMap);
			model.addAttribute("view", masterMap);
			//현재의 마스터가 평가지에서 사용되어졌는지를 확인한다
			paperCnt = examAdmService.selectExamMasterPaperCount(commandMap);
			
		}
		
		model.addAttribute("paperCnt", paperCnt);
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examMasterViewStep01";
	}
	
	/**
	 * 평가마스터 Step02
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examMasterViewStep02.do")
	public String examMasterViewStep02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		String p_process = commandMap.get("p_process") + "";
		int paperCnt = 0;
		
		//평가 레벨 정보
		List list = examAdmService.selectExamLevelsList(commandMap);
		model.addAttribute("list", list);
		
		if(p_process != null && p_process.equals("update"))
		{
			//평가마스터 정보
			Map masterMap = examAdmService.selectExamMasterView(commandMap);
			model.addAttribute("view", masterMap);
			
			//마스터 문제 정보 리스트 
			ArrayList masterList = this.makeExmaMasterNumberList(commandMap, masterMap);
			model.addAttribute("masterList", masterList);
			
			
			//현재의 마스터가 평가지에서 사용되어졌는지를 확인한다
			paperCnt = examAdmService.selectExamMasterPaperCount(commandMap);
		}
		
		model.addAttribute("paperCnt", paperCnt);
		
		
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examMasterViewStep02";
	}
	
	
	
	/**
	 * 평가마스터의 문제정보를 리스트 배열로 만들어서 리턴한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public ArrayList makeExmaMasterNumberList(Map<String, Object> commandMap, Map<String, Object> masterMap) throws Exception{
		ArrayList lessonlist = new ArrayList();
		
		StringTokenizer st = null;
        StringTokenizer st2 = null;
        StringTokenizer st3= null;	
        StringTokenizer sst= null;	
		StringTokenizer ssst= null;	
		
		String v_lessonstart    = commandMap.get("p_lessonstart")+"";
        String v_lessonend    = commandMap.get("p_lessonend")+"";

        int v_startlesson = Integer.parseInt(v_lessonstart);
        int v_endlesson = Integer.parseInt(v_lessonend);
        
		
		st = new StringTokenizer(masterMap.get("level1text")+"", "/");
		st2 = new StringTokenizer(masterMap.get("level2text")+"", "/");
		st3 = new StringTokenizer(masterMap.get("level3text")+"", "/");

        for ( int i = v_startlesson; i <= v_endlesson ;  i++ ) { 
            
            String ss = "";
    		String ss2 = "";
    		String ss3 = "";
    		String ss4 = "";				

			if ( st.hasMoreTokens() ) ss = st.nextToken();
			if ( st2.hasMoreTokens() ) ss2 = st2.nextToken();
			if ( st3.hasMoreTokens() ) ss3 = st3.nextToken();

			 ArrayList levelslist = new ArrayList();
             for ( int j = 1; j <= 3; j++ ) { 

                 if ( j == 1) { 
					 ss4 = ss;
                 } else if ( j == 2) { 
					 ss4 = ss2;
				 } else if ( j == 3) { 
					 ss4 = ss3;
				 }

                 sst = new StringTokenizer(ss4, "|"); // System.out.println("ss4 >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  > " + ss4);
	    
    		     ArrayList typelist = new ArrayList();
    		     
    		     String str1 = "";
    		     String str2 = "";
	     
                 for ( int k =1; k <= 3 ; k++ ) {   // by 정은년 2- > 3
                    
                    if ( sst.hasMoreTokens() ) 
                    str1 = sst.nextToken();
                    System.out.println("str1" + str1);
                    
                    ssst = new StringTokenizer(str1, ",");
	                for ( int l =1; l <= 3 ; l++ ) {  // by 정은년 2- > 3
			            if ( ssst.hasMoreTokens() ) { 
			                ssst.nextToken();
			                str2 = ssst.nextToken();
			                System.out.println("str2" + str2);
			                typelist.add(str2);
                        }                            
                    }
	            }
	            levelslist.add(typelist);
                 
             }
             lessonlist.add(levelslist);
        }
        
        return lessonlist;
	}
	
	
	
	
	/**
	 * 평가 마스터 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examMasterAction.do")
	public String examMasterAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "forward:/adm/exm/examMasterViewStep01.do";
		
		
		if(p_process.equals("insert"))
		{
			boolean isok = examAdmService.insertTzExamMaster(commandMap);
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
		}
		else if(p_process.equals("update"))
		{
			boolean isok = examAdmService.updateTzExamMaster(commandMap);
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
		}
		else if(p_process.equals("delete"))
		{
			boolean isok = examAdmService.deleteTzExamMaster(commandMap);
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
		}
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	/**
	 * 평가결과분석 리스트 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examResultList.do")
	public String examResultList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_target = commandMap.get("p_target") + "";
		
		String url = "adm/exm/examResultList";
		
		//엑셀다운로드
		if(p_target != null && p_target.equals("xlsdown"))  url = "adm/exm/examResultXls";
		
		List<?> list = null;
		System.out.println("ses_search_subjseq------> "+commandMap.get("ses_search_subjseq"));		
		if(commandMap.get("ses_search_subjseq") !=null){
	        //전체 평균
			model.addAttribute("view", examAdmService.selectExamResultAvg(commandMap));
			//전체리스트
			list = examAdmService.selectExamResultList(commandMap);
		}
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	/**
	 * 평가 재응시 수정페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examUserUpdateRetry.do")
	public String examUserUpdateRetry(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examUserUpdateRetry";
	}
	
	/**
	 * 평가 재응시 수정 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examUserUpdateRetryAction.do")
	public String examUserUpdateRetryAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		
		
		if(p_process.equals("update"))
		{
			int cnt = examAdmService.upddateExamUserRetry(commandMap);
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
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/exm/examUserUpdateRetry.do";
	}
	
	/**
	 * 평가 재채점 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examUserReRatingView.do")
	public String examUserReRatingView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		System.out.println("selectExamResultAvg ----> ");
		//전체 평균
		model.addAttribute("view", examAdmService.selectExamResultAvg(commandMap));
		
		System.out.println("selectUserPaperResultList ----> ");
		
		//전체 문제 정보
		List paperList = examAdmService.selectUserPaperResultList(commandMap);
		model.addAttribute("paperList", paperList);
		
		System.out.println("selectUserExamResultList ----> ");
		
		//전체 답안정보 - 학습자
		Map userExamView = examAdmService.selectUserExamResultList(commandMap);
		model.addAttribute("userExamView", userExamView);
		
		System.out.println("selectUserExamResultAnswerList ----> ");
		
		//전체문제 답안정보 - 풀어서 보여주기
		List examList = examAdmService.selectUserExamResultAnswerList(userExamView);
		model.addAttribute("examList", examList);

		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examUserReRatingView";
	}
	
	
	
	/**
	 * 평가 재채점 실행
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examUserReRatingAction.do")
	public String examUserReRatingAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String v_answer    = commandMap.get("answer") == null ? "" : (String)commandMap.get("answer");
        String v_exam      = (String)commandMap.get("p_exam");
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		
		Vector v_result = new Vector();
		v_result = studyExamService.getScore(commandMap);
		log.info("평가점수 채점결과 : "+v_result);
		
		int v_score     = Integer.parseInt((String)v_result.get(0)); // 점수
		
		log.info("점수 : "+v_score);
        
		int v_answercnt = Integer.parseInt((String)v_result.get(1));
		log.info("답안개수 : "+v_score);
		
		String v_corrected = (String)v_result.get(2);
		log.info("답안 맞은 틀림내역 : "+v_score);
		
		StringTokenizer st2 = new StringTokenizer(v_answer, ",");
        v_answer = "";
        while ( st2.hasMoreElements() ) {                 
            String s = StringManager.trim((String)st2.nextToken() );
	
            if ( s.length() == 0) v_answer = v_answer + " " + ",";
            else v_answer = v_answer + s + ",";
        }

        commandMap.put("v_score", v_score);
        commandMap.put("v_answercnt", v_answercnt);
        commandMap.put("v_answer", v_answer);
        commandMap.put("v_corrected", v_corrected);
        
        //점수재저장
        studyExamService.updateTZ_examresultReRating(commandMap);
		//전체점수저장
        studyExamService.calc_score_admin(commandMap);
		
		
		resultMsg = egovMessageSource.getMessage("success.common.update");
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/exm/examUserReRatingView.do";
	}
	
	/**
	 * 평가문제 Pool 과정  리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestPoolList.do")
	public String examQuestPoolList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
				
		//List list = examAdmService.selectExamQuestPoolList(commandMap);
		List list = examAdmService.selectExamPoolSubjList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examQuestPoolSubjList";
	}
	
	/**
	 * 평가문제 Pool 팝업 과정  리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examPoolSubjListPop.do")
	public String examPoolSubjListPop(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List list = examAdmService.examPoolSubjListPop(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examPoolSubjPopList";
	}
	
	/**
	 * 평가문제 Pool 과정의 문제 조회 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestionDetailList.do")
	public String examQuestionDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		log.error("평가문제 Pool 과정의 문제 조회 ");
		
		List list = examAdmService.examQuestionDetailList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examQuestPoolDetailList";
	}
	
	/**
	 * 평가문제 파일로 추가
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestFileUpload.do")
	public String examQuestFileUpload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examQuestFileUpload";
	}
	
	/**
	 * 평가문제 데이터베이스에 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examQuestFileUploadAction.do")
	public String examQuestFileUploadAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String p_select = (String)commandMap.get("p_select");
		String resultMsg = "";
		//기본 업로드 폴더
		String strSavePath = EgovProperties.getProperty("Globals.defaultDP") + "exam";
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		
		HashMap map = new HashMap();
		 
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			if (mFile.getSize() > 0) {
				map = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		String fileName = map.get("uploadFileName")+"";
		
		FileController file = new FileController();
		List result = file.getExcelDataList(strSavePath+"/"+fileName);
		//List result = file.getExcelDataList(strSavePath+"/1c01285a-d095-4f45-abf7-704763c6031e.xls");
		
		//데이터 베이스에 문제등록
		if(p_process.equals("insertFileToDB"))
		{
			examAdmService.insertExamFileToDB(result, commandMap);
			
		}
		
		resultMsg = "처리되었습니다.";
		
		model.addAttribute("list", result);
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		
		return "adm/exm/examQuestFileUpload";
	}
	
	
	/**
	 * 평가문제지 리스트 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examPaperList.do")
	public String examPaperList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = examAdmService.selectExamPaperList(commandMap);
		
		model.addAttribute("list", list);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examPaperList";
	}
	
	
	
	/**
	 * 평가문제지 미리보기 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examPaperPreview.do")
	public String examPaperPreview(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		Vector v_examnums = studyExamService.getExamnums(commandMap);
		List QuestionExampleDataList = null;
	
		if(v_examnums != null) { 
			
			String[] p_examnums = new String[v_examnums.size()];
	        for ( int i = 0; i < v_examnums.size(); i++ ) { 
	        	p_examnums[i] = i+"!_"+(String)v_examnums.get(i);
	        }
			commandMap.put("p_examnums", p_examnums);
            // 평가번호에 해당하는 문제리스트를 만든다. 리스트((설문번호1, 보기1,2,3..))
            QuestionExampleDataList = studyExamService.selectPaperQuestionExampleList(commandMap);
        }

		if ( QuestionExampleDataList == null ) { 
		    QuestionExampleDataList = new ArrayList();
		}
		
		
		model.addAttribute("PaperQuestionExampleList", QuestionExampleDataList);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/userStudyExamAnswerPage";
	}
	
	/**
	 * 평가문제지 Step01
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examPaperViewStep01.do")
	public String examPaperViewStep01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_lesson = commandMap.get("p_lesson") + "";
		String p_examtype = commandMap.get("p_examtype") + "";
		int paperCnt = 0;
		
		
		if(p_examtype != null && p_lesson != null)
		{
			//평가문제지 정보
			Map masterMap = examAdmService.selectExamPaperView(commandMap);
			model.addAttribute("view", masterMap);
			//현재의 문제지를 사용자가 풀었는지를 확인한다.
			paperCnt = examAdmService.selectExamResultCount(commandMap);
			
		}
		
		model.addAttribute("paperCnt", paperCnt);
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examPaperViewStep01";
	}
	
	/**
	 * 평가문제지 Step02
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examPaperViewStep02.do")
	public String examPaperViewStep02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		String p_process = commandMap.get("p_process") + "";
		int paperCnt = 0;
		
		//평가 레벨 정보
		List list = examAdmService.selectExamLevelsList(commandMap);
		model.addAttribute("list", list);
		
		//평가마스터 정보
		Map masterMap = examAdmService.selectExamMasterView(commandMap);
		model.addAttribute("view", masterMap);
		
		//마스터 문제 정보 리스트 
		ArrayList masterList = this.makeExmaMasterNumberList(commandMap, masterMap);
		model.addAttribute("masterList", masterList);
		
		//평가문제지 정보
		Map mm = examAdmService.selectExamPaperView(commandMap);
		model.addAttribute("paperView", mm);
		
		//현재의 문제지를 사용자가 풀었는지를 확인한다.
		paperCnt = examAdmService.selectExamResultCount(commandMap);
	
		model.addAttribute("paperCnt", paperCnt);
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examPaperViewStep02";
	}
	
	
	
	/**
	 * 평가 문제지 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examPaperAction.do")
	public String examPaperAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "forward:/adm/exm/examPaperList.do";
		
		
		if(p_process.equals("insert"))		//과정별등록
		{
			int isok = examAdmService.insertExamPaperSubject(commandMap);
			
			if(isok == 1)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			else if(isok == -1)
				resultMsg = egovMessageSource.getMessage("fail.subject.info");
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
		}
		else if(p_process.equals("insertGrSeq"))	//기수별 등록
		{
			int isok = examAdmService.insertExamPaperGrSeq(commandMap);
			
			if(isok == 1)
				resultMsg = egovMessageSource.getMessage("success.common.insert");
			else if(isok == -1)
				resultMsg = egovMessageSource.getMessage("fail.grseq.list.info");
			else
				resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		else if(p_process.equals("update"))	//문제지 수정
		{
			boolean isok = examAdmService.updateExamPaper(commandMap);
			//정상수정
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
		}
		else if(p_process.equals("updatedate"))	//기간연장
		{
			boolean isok = examAdmService.updateExamPaperDate(commandMap);
			//정상수정
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common.update");
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common.update");
			
			url = "forward:/adm/exm/examPaperViewStep01.do";
		}
		else if(p_process.equals("delete"))	//삭제
		{
			boolean isok = examAdmService.deleteExamPaper(commandMap);
			//정상수정
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
		}
		

		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	/**
	 * 문제 이미지 등록 팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examImageUploadPopup.do")
	public String examImageUploadPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examImageUploadPopup";
	}
	
	@RequestMapping(value="/adm/exm/examImageUploadAction.do")
	public String examImageUploadAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		//기본 업로드 폴더
		String strSavePath = EgovProperties.getProperty("Globals.defaultDP") + "exam/image";
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		
		HashMap<String, String> map = new HashMap<String, String>();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				map = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		
		
		model.addAttribute("img_path", "/dp/exam/image/" + map.get("uploadFileName"));
		model.addAllAttributes(commandMap);
		
		
		return "adm/exm/examImageUploadPopup";
	}
	
	
	/**
	 * 평가문제은행 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankList.do")
	public String examBankList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
				
		List list = examAdmService.selectExamBankList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankList";
	}
	
	
	/**
	 * 평가문제은행 과정등록 Form
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankSubjFormPop.do")
	public String examSubjFormPop(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
				
		String p_type = commandMap.get("p_type") !=null ? commandMap.get("p_type").toString() : "insert";
		Object examBankSubj = null;
		if(p_type.equals("modify")){
			examBankSubj = examAdmService.selectTzExamSubj(commandMap);
		}
		
		model.addAttribute("examBankSubj", examBankSubj);
		model.addAllAttributes(commandMap);
		
		
		return "adm/exm/examBankSubjFormPop";
	}
	
	
	/**
	 * 평가 문제 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankSubjAction.do")
	public String examBankSubjAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "forward:/adm/exm/examBankSubjFormPop.do";
		
		boolean isok = false;
		//문제은행 과정 등록
		if(p_process.equals("insert")){
			isok = examAdmService.insertTzExamSubj(commandMap);
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common.save");
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common.save");
			
		}else if(p_process.equals("modify")){
			isok = examAdmService.updateTzExamSubj(commandMap);
			
			if(isok){
				resultMsg = egovMessageSource.getMessage("success.common.update");
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.update");
			}
		}
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	/**
	 * 평가문제은행 과정의 문제 조회 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankDetailList.do")
	public String examBankDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		log.error("평가문제은행 과정의 문제 조회 ");
		
		List list = examAdmService.examBankDetailList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankDetailList";
	}
	
	
	/**
	 * new 평가문제지 리스트 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperList.do")
	public String examBankPaperList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_ori_subj = commandMap.get("ori_subj") !=null ? commandMap.get("ori_subj").toString() : "";	
		String p_ses_search_subj = commandMap.get("ses_search_subj") !=null ? commandMap.get("ses_search_subj").toString() : "";	
	
		ArrayList masterList = null;
		
		String p_process = commandMap.get("p_process") + "";
		int paperCnt = 0;
		
		String p_exam_subj = commandMap.get("p_exam_subj") !=null ? commandMap.get("p_exam_subj").toString() : "";
		int p_setexamcnt = commandMap.get("p_setexamcnt") !=null ? Integer.parseInt(commandMap.get("p_setexamcnt").toString()) : 0;
		int p_setexamlessoncnt = commandMap.get("p_setexamlessoncnt") !=null ? Integer.parseInt(commandMap.get("p_setexamlessoncnt").toString()) : 0;
		String exam_subj = "";
		
		//문제 콘텐츠
		List examBankList = examAdmService.selectExamBankList(commandMap);
		model.addAttribute("examBankList", examBankList);
		
		
		//평가지 tz_exampaper 정보
		Map masterMap = examAdmService.selectExamBankPaperView(commandMap);
		model.addAttribute("view", masterMap);
		
		String p_papernum = ""; 
				
		if(masterMap != null){				
			if(masterMap.get("papernum") !=null && masterMap.get("examsubj") !=null){
				
				p_papernum = masterMap.get("papernum").toString();
				commandMap.put("p_papernum", p_papernum);		
				exam_subj = masterMap.get("examsubj").toString();				
				p_setexamcnt = Integer.parseInt(masterMap.get("setexamcnt").toString());
				p_setexamlessoncnt = Integer.parseInt(masterMap.get("setexamlessoncnt").toString());
			}
			
			
		}		
		
		/*if("".equals(p_exam_subj)){			
			p_exam_subj = exam_subj;
		}*/		
		if(!p_ori_subj.equals(p_ses_search_subj)){			
			p_exam_subj = exam_subj;		
		}
		
		if(!"".equals(p_exam_subj)){	
			
			
			commandMap.put("v_subj", p_exam_subj);
			commandMap.put("p_subj", p_exam_subj);
			commandMap.put("p_exam_subj", p_exam_subj);
			commandMap.put("p_setexamcnt", p_setexamcnt);
			commandMap.put("p_setexamlessoncnt", p_setexamlessoncnt);
			
			
			//콘텐츠 문제
			Map examSubj =  examAdmService.selectExamBankSubj(commandMap);
			model.addAttribute("examSubj", examSubj);
			
			
			//평가 레벨 정보: 사용할 수 있는 문제의 개수를 뽑아오는 쿼리
			//List list = examAdmService.selectExamBankLevelsList(commandMap);
			//model.addAttribute("list", list);
			
			Map examBankLevelsCnt = examAdmService.selectExamBankLevelsCnt(commandMap);
			model.addAttribute("examBankLevelsCnt", examBankLevelsCnt);
			
			if(masterMap != null){
				if(masterMap.get("papernum") !=null){			
					//문제 정보 리스트 
					masterList = this.makeExmaMasterNumberList(commandMap, masterMap);
				}
			}
			model.addAttribute("masterList", masterList);		
			
		}
		
		paperCnt = examAdmService.selectExamBankResultCount(commandMap);
		
		model.addAttribute("paperCnt", paperCnt);
		
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankPaperList";
	}
	
	/**
	 * new 평가 문제지 과정 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperPop.do")
	public String examBankPaperPop(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		System.out.println("new 평가 문제지 과정 ");
		
		List list = examAdmService.selectExamBankList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankPaperPop";
	}
	
	
	
	/**
	 * 평가문제지 Step01
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperViewStep01.do")
	public String examBankPaperViewStep01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankPaperViewStep01";
	}
	
	
	/**
	 * 평가문제지 Step02
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperViewStep02.do")
	public String examBankPaperViewStep02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		String p_process = commandMap.get("p_process") + "";
		int paperCnt = 0;
		
		//평가 레벨 정보
		List list = examAdmService.selectExamLevelsList(commandMap);
		model.addAttribute("list", list);
		
		if(p_process != null && p_process.equals("update"))
		{
			//평가마스터 정보
			Map masterMap = examAdmService.selectExamMasterView(commandMap);
			model.addAttribute("view", masterMap);
			
			//마스터 문제 정보 리스트 
			ArrayList masterList = this.makeExmaMasterNumberList(commandMap, masterMap);
			model.addAttribute("masterList", masterList);
			
			
			//현재의 마스터가 평가지에서 사용되어졌는지를 확인한다
			paperCnt = examAdmService.selectExamMasterPaperCount(commandMap);
		}
		
		model.addAttribute("paperCnt", paperCnt);
		
		
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankPaperViewStep02";
	}
	
	
	
	/**
	 * 평가 문제지 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperAction.do")
	public String examBankPaperAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "forward:/adm/exm/examBankPaperList.do";
		
		
		if(p_process.equals("insertGrSeq"))	//기수별 등록
		{
					
			int examPaperCnt = examAdmService.selectExamBankPaperCount(commandMap);
						
			if(examPaperCnt == 0){
				System.out.println("ses_search_gyear ----> "+commandMap.get("ses_search_gyear"));			
						
				int isok = examAdmService.insertExamBankPaperGrSeq(commandMap);
				
				if(isok == 1)
					resultMsg = egovMessageSource.getMessage("success.common.insert");
				else if(isok == -1)
					resultMsg = egovMessageSource.getMessage("fail.grseq.list.info");
				else
					resultMsg = egovMessageSource.getMessage("fail.common.insert");
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.update.subjseq.same.count");				
			}			
			
		}
		else if(p_process.equals("update"))	//문제지 수정
		{
			boolean isok = examAdmService.updateExamBankPaper(commandMap);
			//정상수정
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			//model.addAttribute("isOpenerReload", "OK");
			//model.addAttribute("isClose", "OK");
		}
		else if(p_process.equals("updatedate"))	//기간연장
		{
			boolean isok = examAdmService.updateExamBankPaperDate(commandMap);
			//정상수정
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common.update");
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common.update");
			
			//url = "forward:/adm/exm/examPaperViewStep01.do";
		}
		else if(p_process.equals("basic"))	//평가 기본정보 수정
		{
			boolean isok = examAdmService.updateExamBankPaperBasic(commandMap);
			//정상수정
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common.update");
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common.update");
			
			//url = "forward:/adm/exm/examPaperViewStep01.do";
		}
		else if(p_process.equals("delete"))	//삭제
		{
			boolean isok = examAdmService.deleteExamBankPaper(commandMap);
			//정상수정
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			commandMap.put("p_setexamcnt", 0);
			commandMap.put("p_setexamlessoncnt", 0);
			
			//model.addAttribute("isOpenerReload", "OK");
			//model.addAttribute("isClose", "OK");
		}
		

		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	
	/**
	 * 평가문제지 미리보기 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperPreview.do")
	public String examBankPaperPreview(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
	
		List QuestionExampleDataList = null;
		
		
        QuestionExampleDataList = studyExamService.selectPaperQuestionExampleBankList(commandMap);
        
        
        System.out.println("examBankPaperPreview");
	
		
		model.addAttribute("PaperQuestionExampleList", QuestionExampleDataList);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/userStudyExamAnswerPage";
	}
	
	
	/**
	 * 평가문제 파일로 추가
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankFileUpload.do")
	public String examBankFileUpload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankFileUpload";
	}
	
	
	/**
	 * 평가문제 데이터베이스에 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankFileUploadAction.do")
	public String examBankFileUploadAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String p_select = (String)commandMap.get("p_select");
		String resultMsg = "";
		//기본 업로드 폴더
		String strSavePath = EgovProperties.getProperty("Globals.defaultDP") + "exam";
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		
		HashMap map = new HashMap();
		 
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			if (mFile.getSize() > 0) {
				map = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		String fileName = map.get("uploadFileName")+"";
		
		FileController file = new FileController();
		List result = file.getExcelDataList(strSavePath+"/"+fileName);
		//List result = file.getExcelDataList(strSavePath+"/1c01285a-d095-4f45-abf7-704763c6031e.xls");
		
		//데이터 베이스에 문제등록
		if(p_process.equals("insertFileToDB"))
		{
			examAdmService.insertExamBankFileToDB(result, commandMap);
			
		}
		
		resultMsg = "처리되었습니다.";
		
		model.addAttribute("list", result);
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		
		return "adm/exm/examBankFileUpload";
	}
	
	
	/**
	 * 평가문제 보기(등록/수정) 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankView.do")
	public String examBankView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		//문제정보
		model.addAttribute("view", examAdmService.selectExamBankView(commandMap));
		
		//답안정보
		List<?> list = examAdmService.selectExamBankSelList(commandMap);
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examQuestView";
	}
	
	
	/**
	 * 평가 문제 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankAction.do")
	public String examBankAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "forward:/adm/exm/examBankView.do";
		
		
		
		//선택된 모든 문제 삭제..[사용되어진 문제는 삭제 하지 않는다.]
		if(p_process.equals("checkDelete"))
		{
			boolean isok = examAdmService.deleteExamBankCheckList(commandMap);
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common.delete");
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common.delete");
			
			url = "forward:/adm/exm/examQuestList.do";
		}
		//pool 등록
		else if(p_process.equals("poolinsert"))
		{
			boolean isok = examAdmService.insertTzExamPoolSelect(commandMap);
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common.save");
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common.save");
			
			url = "forward:/adm/exm/examQuestPoolList.do";
		}
		//문제등록
		else if(p_process.equals("insert"))
		{
			boolean isok = examAdmService.insertTzExam(commandMap);
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
			
		}
		//문제수정
		else if(p_process.equals("update"))
		{
			int isok = examAdmService.updateTzExamBank(commandMap);
			
			if(isok == -1)		//사용중인 문항
				resultMsg = egovMessageSource.getMessage("fail.common.status.count.save");
			else if(isok == -2)		//사용중인 문항//에러
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			else				//정상처리
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
		}
		//문제삭제
		else if(p_process.equals("delete"))
		{
			//체크된 문제삭제 로직을 다시쓰기 위하여 p_checks 에 문제번호를 등록한다.
			String p_examnum = commandMap.get("p_examnum")+"";
			
			commandMap.put("p_checks", p_examnum);
			boolean isok = examAdmService.deleteExamBankCheckList(commandMap);
			commandMap.remove("p_checks");
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
		}
		
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	/**
	 * 평가문제지 미리보기 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperExamModify.do")
	public String examBankPaperModify(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
	
		List examBankPaperModifyList = null;
		
		
		examBankPaperModifyList = studyExamService.selectExamBankPaperModifyList(commandMap);
        
        
        System.out.println("examBankPaperPreview");
	
		
		model.addAttribute("PaperQuestionExampleList", examBankPaperModifyList);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankPaperExamModify";
	}
	
	
	
	/**
	 * 평가 문제지 삭제 등록 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperExamAction.do")
	public String examBankPaperExamAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "forward:/adm/exm/examBankPaperExamModify.do";
		
		int examCnt = examAdmService.selectExamBankPaperSheetExamCount(commandMap);
		if(examCnt > 0){
		
			boolean isok = examAdmService.deleteExamBankPaperSheetExam(commandMap);
			//정상수정
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);		
		}else{
			resultMsg = "대체할 문제가 없습니다.";
		}
		//model.addAttribute("isOpenerReload", "OK");
		//model.addAttribute("isClose", "OK");

		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	/**
	 * 평가 문제지 삭제 등록 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperExamChange.do")
	public String examBankPaperExamChange(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		log.error("시험문제지 문제 대체");
		String url = "adm/exm/examBankPaperExamChange";
		
		String p_subj = commandMap.get("p_exam_subj").toString();
		String p_examnum = commandMap.get("p_examnum").toString();
		
		
		
		commandMap.put("p_subj", p_subj);
		commandMap.put("v_examnum", p_examnum);
		
		String resultMsg = "";
		int useExam = examAdmService.selectExamBankNumResultSubjseqCount(commandMap);
		if(useExam == 0){
			//문제정보
			model.addAttribute("view", examAdmService.selectExamBankView(commandMap));
			
			//답안정보
			List<?> examSellist = examAdmService.selectExamBankSelList(commandMap);
			model.addAttribute("examSellist", examSellist);	
			
			List list = examAdmService.examBankDetailList(commandMap);
			model.addAttribute("list", list);
			
			
		}else{
			resultMsg = "사용중인 문항이 존재합니다.";
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
			
			url="forward:/adm/exm/examBankPaperExamModify.do";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return url;
	}
	
	/**
	 * 평가 문제지 대체 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperExamChangeAction.do")
	public String examBankPaperExamChangeAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		String resultMsg = "";
		String url = "forward:/adm/exm/examBankPaperExamModify.do";
		
		boolean isok = examAdmService.deleteExamBankPaperSheetExamChange(commandMap);
		
	
		//정상수정
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common.save");
		//에러
		else
			resultMsg = egovMessageSource.getMessage("fail.common.save");		

		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");


		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	/**
	 * 평가문제은행 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankExcelDown.do")
	public String examBankExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
				
		List list = examAdmService.selectExamBankExcelDownList(commandMap);
		model.addAttribute("list", list);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankExcelDown";
	}
	
	
	
	/**
	 * 응시(미제출) 제출
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examUserExamresultEnded.do")
	public String examUserExamresultEnded(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		String resultMsg = "";        
        //제출
		int cnt = studyExamService.updateTZ_examresultEnded(commandMap);
		//정상수정
		if(cnt > 0){
			resultMsg = egovMessageSource.getMessage("success.common.update");			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/exm/examUserReRatingView.do";
	}
	
	
	/**
	 * 문제 미리보기 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/exm/examBankPaperExamView.do")
	public String examBankPaperExamView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
	
		List examBankPaperExamViewList = null;
		
		
		examBankPaperExamViewList = examAdmService.selectExamBankPaperExamViewList(commandMap);
        
        
        System.out.println("examBankPaperPreview");
	
		
		model.addAttribute("examBankPaperExamViewList", examBankPaperExamViewList);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/exm/examBankPaperExamView";
	}
	
	@RequestMapping(value="/adm/exm/examSubmit.do")
	public String examSubmit(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model
			, @RequestParam(value="chkUser") String[] useridArr) throws Exception {
		String examtabletemp = "";
		for(String userid: useridArr) {
			for(int i=0; i<2; i++){
				if(i==1){
					examtabletemp = "temp";
				}else{
					examtabletemp = "";
				}
				commandMap.put("examtabletemp", examtabletemp);
				
				System.out.println("i ----> "+i);				
				System.out.println("examtabletemp ----> "+examtabletemp);
				
				commandMap.put("p_year", commandMap.get("p_gyear"));
				commandMap.put("submitYn", "Y");
				
				Map<String, Object> mergeMap = new HashMap<String, Object>();
				mergeMap.putAll(commandMap);
				
				// 문제번호, 체크한 답 가져오기
				mergeMap.put("userid", userid);
				mergeMap.put("p_userid", userid);
				Map<String, Object> examResultInfo = studyExamService.getExamResultInfo(mergeMap);
				
				if(examResultInfo!=null) {
					mergeMap.put("p_exam", examResultInfo.get("exam"));
					mergeMap.put("answer", examResultInfo.get("answer"));
					mergeMap.put("p_lesson", examResultInfo.get("lesson"));
					mergeMap.put("p_examtype", examResultInfo.get("examtype"));
					mergeMap.put("p_papernum", examResultInfo.get("papernum"));
					mergeMap.put("p_userretry", examResultInfo.get("userretry"));
					mergeMap.put("p_exampoint", examResultInfo.get("exampoint"));
					
					String p_exam_subj = examResultInfo.get("examsubj") != null ? (String) examResultInfo.get("examsubj") : "";
					//문제문행 변경
					String examBankchangeYn = "N";
					if(p_exam_subj.length() > 0){
						examBankchangeYn = "Y";
					}
					
					mergeMap.put("p_exam_subj", p_exam_subj);
					mergeMap.put("examBankchangeYn", examBankchangeYn);
					mergeMap.put("p_isadmin", "Y");
					
					studyExamService.InsertResult(mergeMap);
				}	
			}	
		}
		
		model.addAllAttributes(commandMap);
		return "forward:/adm/exm/examResultList.do";
	}
	
	
}
