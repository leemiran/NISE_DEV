package egovframework.com.lcms.len.controller;

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
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ziaan.system.StudyCountBean;

import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.com.cmm.service.FileVO;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;

import egovframework.com.lcms.len.domain.LcmsLearning;
import egovframework.com.lcms.len.service.LearningService;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.usr.stu.std.service.StudyManageService;

import egovframework.adm.lcms.cts.domain.LcmsCmiObjectinfo;
import egovframework.adm.lcms.cts.domain.LcmsProgress;
import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.cts.service.LcmsCourseMapService;
import egovframework.adm.lcms.cts.service.LcmsProgressService;
import egovframework.adm.lgn.domain.SessionVO;
import egovframework.adm.lcms.nct.domain.LcmsLesson;


/**
 * 관리자 게시판 처리
 */
@Controller
public class learningController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(learningController.class);
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    /** AJAX 공통 서비스 */
    @Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;
    
    @Resource(name = "learningService")
    private LearningService learningService;
    
    @Resource(name = "studyManageService")
    private StudyManageService studyManageService;
    
    /** LcmsProgress */
    @Resource(name = "lcmsProgressService")
    private LcmsProgressService lcmsProgressService;
    
    /** LcmsCourseMap */
    @Resource(name = "lcmsCourseMapService")
     private LcmsCourseMapService lcmsCourseMapService;
    
 
    /**
     * 학습창 목록 리스트 조회 페이지
     * @param Map commandMap, ModelMap model
     * @return 출력페이지정보 "/com/lcms/len/learning.do"
     * @exception Exception
     */	
    @RequestMapping(value="/com/lcms/len/learning.do")
    public String selectLearnListForm( 
    		HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> commandMap, ModelMap model) throws Exception { 
		    model.addAllAttributes(commandMap);
		    if( commandMap.get("contentType") != null && ((String)commandMap.get("contentType")).equals("S") ){
//		    	lcmsCourseMapService.updateLcmsCourseMap(commandMap);
		    	int cnt = studyManageService.selectLcmsCourseMapCount(commandMap);
				if( cnt == 0 ){
					studyManageService.insertLcmsCourseMap(commandMap);
				}
				int courseMapSeq = lcmsCourseMapService.getCourseMapSeq(commandMap);
				commandMap.put("courseMapSeq", courseMapSeq);
		    }else{
		    	commandMap.put("courseMapSeq", "000");
		    }
		    
		    System.out.println("#######################################  frm.suryoLessonTime.vlaue :" +commandMap.get("suryoLessonTime")+"");
		   
		    Object obj = learningService.selectLearningCrsInfo(commandMap);
		    
		    
		    //lesson 전부 보여주는지 YN
		    Map m = studyManageService.getContenttype(commandMap);							
			commandMap.put("p_contentLessonAllView", m.get("contentLessonAllView"));				
				
				
		    //Null값 처리
	        Map output = new HashMap(); 
		    if(obj != null) 	output.putAll((Map)obj);
		    
		    
	        model.addAttribute("output",output); 
	        model.addAllAttributes(commandMap);	
		    

      	return "/com/lcms/len/learn";  	
    }
    
    /**
     * 진도/목차 현황
     * @param Map commandMap, ModelMap model
     * @return 출력페이지정보 "/com/lcms/len/learning.do"
     * @exception Exception
     */	
    @RequestMapping(value="/com/lcms/len/eduList.do")
    public String selectEduListPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
    	
    	
    	String subjseq = commandMap.get("p_subjseq") == null ? "000" : (String)commandMap.get("p_subjseq");
    	
    	Map mfData = learningService.selectMasterform(commandMap);
    	model.addAttribute("mfData", mfData);
    	
    	// 과목별 메뉴 접속 정보 추가
		commandMap.put("p_menu", "03");
		learningService.writeLog(commandMap);
		
		List eduScore = learningService.selectEduScore(commandMap);
		model.addAttribute("eduScore", eduScore);
		
		List eduScore2 = learningService.selectEduScore2(commandMap);
		model.addAttribute("eduScore2", eduScore2);
		
		int result = learningService.selectUserPage(commandMap);
		List eduList = new ArrayList();
		List eduTime = new ArrayList();
		if( result == 1 ){
			if( mfData.get("contenttype").toString().equals("N") || mfData.get("contenttype").toString().equals("K") ){
				//진도/목차 리스트 조회
				eduList = learningService.SelectEduList(commandMap);
				//학습시간,최근학습일,강의접근횟수
				eduTime = learningService.selectEduTimeCountOBC(commandMap);
			}
		}else if( subjseq.equals("000") || subjseq.equals("0000") ){
			if( mfData.get("contenttype").toString().equals("N") || mfData.get("contenttype").toString().equals("K") ){
				//진도/목차 리스트 조회
				eduList = learningService.SelectEduList(commandMap);
				//학습시간,최근학습일,강의접근횟수
				eduTime = learningService.selectEduTimeCountOBC(commandMap);
			}
		}else{
			commandMap.put("isClose", "OK");
			model.addAttribute("resultMsg", "개인화 페이지라 입과생이 아니면 보실 수 없습니다.");
		}
		
		model.addAttribute("eduList", eduList);
		model.addAttribute("eduTime", eduTime);
		
		
		if( subjseq.equals("000") || subjseq.equals("0000") || mfData.get("contenttype").toString().equals("L") ){
		}else{
			
			// 리포트 배정
			commandMap.put("p_projgubun", "M");
			int projcnt = learningService.isReportAssign(commandMap);
			if( projcnt == 0 ){
				learningService.updateReportAssign(commandMap);
			}
			commandMap.put("p_projgubun", "F");
			projcnt = learningService.isReportAssign(commandMap);
			if( projcnt == 0 ){
				learningService.updateReportAssign(commandMap);
			}
			
			/* ==  ==  ==  ==  ==   권장진도율, 자기진도율 시작 ==  ==  ==  ==  == */
			String promotion = learningService.getPromotion(commandMap);
			model.addAttribute("promotion", promotion);
			
			String progress = learningService.getProgress(commandMap);
		}
		
    	
    	model.addAllAttributes(commandMap);	
    	return "/com/lcms/len/learn";  	
    }
    
    
    
    
    /**
     * 학습창 종료시 진도율 저장
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/lcms/len/insertScormProgress.do")
    public ModelAndView updateScore( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	
    	Object result = null;
    	
    	ModelAndView modelAndView = new ModelAndView();
//    		result = lcmsProgressService.updateScormLcmsProgress(commandMap);
    		result = 1;
    		
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
    }
    
    
    /**
     * 자이닉스 학습 LESSON 가져오기  
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/lcms/len/xiniceStudyLesson.do")
    public ModelAndView xiniceStudyLesson( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {

    	LcmsLearning result = new LcmsLearning();
    	
    	
    	ModelAndView modelAndView = new ModelAndView();
    	Map lessonInfo = commonAjaxManageService.selectCommonAjaxManageMap(commandMap, "lcmsLessonDAO.selectLcmsLessonXinics");
 
    	
   	    LcmsLesson lesson = new LcmsLesson();
    	if(lessonInfo != null){
    		 LcmsScormModel.bind(lessonInfo,lesson);
    		 result.setLesson(lesson);
    	}
    	
    	Map progressInfo = commonAjaxManageService.selectCommonAjaxManageMap(commandMap, "lcmsProgressDAO.selectLcmsProgress"); 	
    	
    	LcmsProgress progress = new LcmsProgress();
    	if(progressInfo == null){
    		commandMap.put("lessonstatus", "N");    		
    		commandMap.put("lessonCount", "0");    		
    		Object resultInsert = commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.insertLcmsProgress");
    	}else{	
			 LcmsScormModel.bind(progressInfo,progress); 	
			 result.setProgress(progress); 
    	}
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView"); 	
    	return modelAndView;
    }
    /**
     * 자이닉스 학습 LESSON Progress 등록
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/lcms/len/updateXiniceProgress.do")
    public ModelAndView updateXiniceProgress( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	
    	LcmsLearning result = new LcmsLearning();
    	
    	ModelAndView modelAndView = new ModelAndView();
    	int lessonInfo = lcmsProgressService.updateLcmsProgress(commandMap);

    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView"); 	
    	return modelAndView;
    }
    
    
    /**
     * 중공교 학습 LESSON 가져오기  
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/lcms/len/cotiStudyLesson.do")
    public ModelAndView cotiStudyLesson( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {

    	LcmsLearning result = new LcmsLearning();
    	
    	
    	ModelAndView modelAndView = new ModelAndView();
    	
        List lesson = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "lcmsLessonDAO.selectLcmsLessonCoti");	
    	
    	List progress = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "lcmsProgressDAO.selectLcmsProgressCotiLast"); 	
    	
    	
		 result.setLessonList(lesson);
    	 result.setProgressList(progress); 
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView"); 	
    	return modelAndView;
    }
    
    
    /**
     * 중공교 학습 LESSON Progress 등록
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/lcms/len/insertCotiProgress.do")
    public ModelAndView updateCotiProgress( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {	

    	ModelAndView modelAndView = new ModelAndView();
    	int result = lcmsProgressService.updateCotiLcmsProgress(commandMap);
 
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView"); 	
    	return modelAndView;
    }
    /**
     * 중공교 학습 LESSON Progress Check
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    
    @RequestMapping(value = "/com/lcms/len/selectCotiProgressCheck.do")
    public ModelAndView selectCotiProgressCheck( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	ModelAndView modelAndView = new ModelAndView();
    	
    	LcmsLearning result = new LcmsLearning();
    	
        List progressList = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "lcmsProgressDAO.selectLcmsCotiProgressList"); 	
    	
    	result.setProgressList(progressList); 
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView"); 	
    	return modelAndView;
    }
    
    /**
     * Normal , Wmv LESSON  시작
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    
    @RequestMapping(value = "/com/lcms/len/normalStudyLesson.do")
    public ModelAndView selectNormalLesson ( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	ModelAndView modelAndView = new ModelAndView();
    	
   	    LcmsLearning result = new LcmsLearning();
   	    
   	    //시작시 넘어오는 모듈번호와 레슨번호를 저장한다.
   	    LcmsLesson startLesson = new LcmsLesson();
    	startLesson.setModule(commandMap.get("module")+"");
    	startLesson.setLesson(commandMap.get("lesson")+"");
    	
        List lesson = new ArrayList();
        List progress = new ArrayList();
        if( commandMap.get("studyType").toString().equals("NEW") ){
        	lesson = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "lcmsLessonDAO.selectLcmsLessonNormal");	
        	progress = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "lcmsProgressDAO.selectLcmsProgressNormalLast"); 	
        }else{
        	lesson = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "lcmsLessonDAO.selectLcmsLessonNormalOld");	
        	progress = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "lcmsProgressDAO.selectLcmsProgressNormalOldLast"); 	
        }
    	
        result.setLesson(startLesson);
        result.setLessonList(lesson);
        result.setProgressList(progress); 
    	 
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView"); 	
    	return modelAndView;
    }
    
    /**
     *  Wmv 플레이어 
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value="/player.do")
    public String view( 
    HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

    	model.addAllAttributes(commandMap);	
        return "/com/lcms/len/player";
    }
    
    
    /**
     * Normal LESSON Progress Check
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    
    @RequestMapping(value = "/com/lcms/len/selectNormalProgressCheck.do")
    public ModelAndView selectNormalProgressCheck( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	ModelAndView modelAndView = new ModelAndView();
    	//System.out.println("#########################  selectNormalProgressCheck ################################");
    	LcmsLearning result = new LcmsLearning();
    	
    	List progressList = new ArrayList();
    	if( commandMap.get("studyType") != null && commandMap.get("studyType").toString().equals("NEW")){
    		progressList = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "lcmsProgressDAO.selectLcmsCotiProgressList"); 	
    	}else{
    		progressList = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "lcmsProgressDAO.selectLcmsCotiOldProgressList"); 	
    	}
    	
    	result.setProgressList(progressList); 
    	//System.out.println("#########################  selectNormalProgressCheck end ################################");
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView"); 	
    	return modelAndView;
    }
    
    /**
     * Normal 학습 LESSON Progress 등록
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/lcms/len/insertNormalProgress.do")
    public ModelAndView updateNormalProgress( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {	
    	//System.out.println("########################### insertNormalProgress ###########################");
    	ModelAndView modelAndView = new ModelAndView();
    	int result = lcmsProgressService.updateNormalLcmsProgress(commandMap);
    	
    	LOG.info(commandMap);
    	//System.out.println("########################### insertNormalProgress end ###########################");
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView"); 	
    	return modelAndView;
    }
    
    @RequestMapping(value="/com/lcms/len/selectNonScormProgressData.do")
    public ModelAndView selectNonScormProgressData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception{
    	ModelAndView modelAndView = new ModelAndView();
    	
    	Map progressData = new HashMap();
    	if( commandMap.get("studyType") != null && commandMap.get("studyType").toString().equals("NEW") ){
    		progressData = commonAjaxManageService.selectCommonAjaxManageMap(commandMap, "LearningDAO.selectNonScormProgressData");
    	}else{
    		progressData = commonAjaxManageService.selectCommonAjaxManageMap(commandMap, "LearningDAO.selectNonScormProgressOldData");
    	}
    	
    	Map resultMap = new HashMap();
    	resultMap.put("result", progressData);
    	
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
    }
    
    @RequestMapping(value="/com/lcms/len/checkNonScormEduLimit.do")
    public ModelAndView checkNonScormEduLimit(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception{
    	ModelAndView modelAndView = new ModelAndView();
    	Map resultMap = new HashMap();
    	Map result = new HashMap();
    	result.put("eduLimit", "PLAY");
    	if( commandMap.get("review") == null || commandMap.get("review").toString().equals("N") ){
    		result.remove("eduLimit");
    		if( commandMap.get("studyType").toString().equals("NEW") ){
    			result = commonAjaxManageService.selectCommonAjaxManageMap(commandMap, "LearningDAO.checkNonScormEduLimit");
        	}else{
        		result = commonAjaxManageService.selectCommonAjaxManageMap(commandMap, "LearningDAO.checkNonScormEduLimitOld");
        	}
    	}
    	resultMap.put("result", result);
    	
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	return modelAndView;
    }
    
    /**
     * 차시별 total_time 가져오기
     * @param request
     * @param response
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/com/lcms/len/selectNormalProgressTotalTime.do")
    public ModelAndView selectNormalProgressTotalTime( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
    	ModelAndView modelAndView = new ModelAndView();
    	//System.out.println("#########################  selectNormalProgressTotalTime ################################");
    	int result = 0;
    	
    	HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("p_subj", commandMap.get("subj"));
		hm.put("p_year", commandMap.get("year"));
		hm.put("p_subjseq", commandMap.get("subjseq"));
		 
    	Map subjseqMap = lcmsProgressService.selectSubjseq(hm);
    	
    	if(subjseqMap != null && subjseqMap.get("edustatus").equals("Y")){
    		result = commonAjaxManageService.selectCommonAjaxManageInt(commandMap, "lcmsProgressDAO.selectNormalProgressTotalTime");
    	}
    	
    	//System.out.println("#########################  selectNormalProgressTotalTime end ################################");
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView"); 	
    	return modelAndView;
    }
         	
}