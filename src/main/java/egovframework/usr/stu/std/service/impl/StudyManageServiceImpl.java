package egovframework.usr.stu.std.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.lcms.cts.dao.LcmsProgressDAO;
import egovframework.adm.stu.dao.StuMemberDAO;
import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.com.lcms.len.dao.LearningDAO;
import egovframework.com.utl.fcc.service.EgovDateUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usr.stu.std.dao.StudyManageDAO;
import egovframework.usr.stu.std.service.StudyManageService;

import org.apache.log4j.Logger;

@Service("studyManageService")
public class StudyManageServiceImpl extends EgovAbstractServiceImpl implements StudyManageService{
	 
	@Resource(name="studyManageDAO")
    private StudyManageDAO studyManageDAO;
	
	@Resource(name="stuMemberDAO")
    private StuMemberDAO stuMemberDAO;
	
	@Resource(name="lcmsProgressDAO")
    private LcmsProgressDAO lcmsProgressDAO;
	
	@Resource(name="learningDAO")
	private LearningDAO learningDAO;
	
	/** AJAX 공통 서비스 */
    @Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
	public String getProgress(Map<String, Object> commandMap) throws Exception{
		String progress = "0";
		
		Map subjMap = studyManageDAO.getContenttype(commandMap);
		if(subjMap != null)
		{
			commandMap.put("p_contenttype", subjMap.get("contenttype"));
			commandMap.put("p_studyType", subjMap.get("studyType"));
			commandMap.put("p_lcmstype", subjMap.get("studyType"));
		}
		
		String contenttype = (String)commandMap.get("p_contenttype");
		
		if( commandMap.get("p_lcmstype") != null && !commandMap.get("p_lcmstype").toString().equals("OLD") ){
			if( contenttype.equals("S") ){
				progress = studyManageDAO.getNewScormProgress(commandMap);
			}else{
				progress = studyManageDAO.getNewNonScormProgress(commandMap);
			}
		}else{
			//상위자격 취득을 위한 사전연수 36차시를 30차시처럼
			String subjOneThree =  commandMap.get("p_subj").toString();
			System.out.println("getProgress subjOneThree ----> "+subjOneThree);
			String subjOneThreeYn ="N";
			if("PRF150017".equals(subjOneThree) || "PRF170004".equals(subjOneThree)) {
				subjOneThreeYn ="Y";
				commandMap.put("subjOneThreeYn", subjOneThreeYn);
			}	
			System.out.println("getProgress subjOneThreeYn ----> "+subjOneThreeYn);
			progress = studyManageDAO.getProgress(commandMap);
		}
		return progress;
	}
	
	public String getPromotion( Map<String, Object> commandMap) throws Exception {
    	String result = "0";
    	float percent     = (float)0.0;
    	try{
    		Map subjseqMap = learningDAO.selectSubjseq(commandMap);
            String v_today    = (String)subjseqMap.get("today");
            String v_edustart = (String)subjseqMap.get("edustart");
            String v_eduend   = (String)subjseqMap.get("eduend");
            int v_nowday = EgovDateUtil.datediff("date",v_edustart,v_today);
            int v_allday = EgovDateUtil.datediff("date",v_edustart,v_eduend);
            if ( v_allday != 0 ) { 
                percent = (float)((v_nowday * 100) / (float)v_allday);
                if ( percent <= 0.0) percent=0;
                else if ( percent > 100.0) percent=100;
                result =  new DecimalFormat("0.00").format(percent);
            }
    	}catch(Exception ex){
    		result = "0";
    		ex.printStackTrace();
    	}
    	return result;
    }
	
	public List SelectEduTimeCountOBC(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.SelectEduTimeCountOBC(commandMap);
	}
	
	public Map SelectEduScore(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.SelectEduScore(commandMap);
	}

	public Map getTutorInfo(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.getTutorInfo(commandMap);
	}
	
	public List selectListOrderPerson(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectListOrderPerson(commandMap);
	}
	
	public Map getStudyChasi(Map<String, Object> commandMap) throws Exception{
		Map result = new HashMap();
		
		result.put("attendCnt", studyManageDAO.selectAttendCnt(commandMap));
		result.put("wstep", studyManageDAO.selectWstep(commandMap));
		
		String contenttype = (String)commandMap.get("p_contenttype");
		
		if( commandMap.get("p_lcmstype") != null && commandMap.get("p_lcmstype").toString().equals("OLD") ){
			result.put("datecnt", studyManageDAO.selectOldlesson(commandMap));
			result.put("edudatecnt", studyManageDAO.selectOldProgress(commandMap));
		}else{
			if( contenttype.equals("S") ){
				result.put("datecnt", studyManageDAO.selectNewScormlesson(commandMap));
				result.put("edudatecnt", studyManageDAO.selectNewScormProgress(commandMap));
			}else{
				result.put("datecnt", studyManageDAO.selectNewNonscormlesson(commandMap));
				result.put("edudatecnt", studyManageDAO.selectNewNonscormProgress(commandMap));
			}
		}
		return result;
	}
	
	public List selectGongList(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectGongList(commandMap);
	}
	
	public List selectBoardList(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectBoardList(commandMap);
	}
	
	public List selectQnaBoardList(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectQnaBoardList(commandMap);
	}
	
	public int getSulData(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.getSulData(commandMap);
	}
	
	public int getUserData(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.getUserData(commandMap);
	}
	
	public List getSulDate(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.getSulDate(commandMap);
	}
	
	public Map getContenttype(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.getContenttype(commandMap);
	}
	
	public List selectItemList(Map<String, Object> commandMap) throws Exception{
		List list = new ArrayList();
	//	System.out.println("========================  컨텐츠 타입 :"+commandMap.get("p_studyType") + "");
		if( commandMap.get("p_studyType").toString().equals("OLD") ){ //기존 lcms
	//		System.out.println("================================ 기존 lcms(OLD) ========================================");
			list = studyManageDAO.selectOldItemList(commandMap);
	//		System.out.println("================================ 기존 lcms(OLD)// ========================================");
		}else{ //신규lcms
			if( commandMap.get("p_contenttype").toString().equals("S") ){ //scorm
	//			System.out.println("================================ 스콤 컨텐츠(S) ========================================");
	//			System.out.println("================================ selectLcmsCourseMapCount ========================================");
				int cnt = studyManageDAO.selectLcmsCourseMapCount(commandMap);
	//			System.out.println("================================ selectLcmsCourseMapCount// ========================================");
				if( cnt == 0 ){
	//				System.out.println("================================ insertLcmsCourseMap(cnt가 0일때 실행) ========================================");
					studyManageDAO.insertLcmsCourseMap(commandMap);
	//				System.out.println("================================ insertLcmsCourseMap// ========================================");
				}
	//			System.out.println("================================ 스콤 아이템리스트 ========================================");
				list = studyManageDAO.selectNewScormItemList(commandMap);
	//			System.out.println("================================ 스콤 아이템리스트// ========================================");
	//			System.out.println("================================ 스콤 컨텐츠(S)// ========================================");
			}else if(commandMap.get("p_contenttype").toString().equals("X")){ //xinics
	//			System.out.println("================================ 자이닉스 아이템리스트// ========================================");
				list = studyManageDAO.selectNewXiniceItemList(commandMap);
	//			System.out.println("================================ 자이닉스 아이템리스트// ========================================");
			}else{ // nonscorm, coti, player
	//			System.out.println("================================ 논스콤 아이템리스트// ========================================");
				list = studyManageDAO.selectNewNonScormItemList(commandMap);
	//			System.out.println("================================ 논스콤 아이템리스트// ========================================");
			}
		}
		return list;
	}
	
	public List selectOldItemMobileList(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectOldItemMobileList(commandMap);
	}
	
	
	public int selectLcmsCourseMapCount(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectLcmsCourseMapCount(commandMap);
	}
	
	public int insertLcmsCourseMap(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			studyManageDAO.insertLcmsCourseMap(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		
		return isOk;
	}
	
	public List selectSulpaperList(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectSulpaperList(commandMap);
	}
	
	public List selectReportList(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectReportList(commandMap);
	}
	
	public List selectExamList(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectExamList(commandMap);
	}
	
	public Integer checkDuplicateIP(Map<String, Object> commandMap) throws Exception {
		return studyManageDAO.checkDuplicateIP(commandMap);
	}
	
	/**
	 * 모바일앱으로 컨텐츠 정보를 넘기기 위한 메소드 - 모바일로만 진행되는 과정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectMobileContentView(Map<String, Object> commandMap) throws Exception{
			return studyManageDAO.selectMobileContentView(commandMap);
	}
	
	
	/**
	 * 모바일앱으로 컨텐츠 정보를 넘기기 위한 메소드 - 모바일을 지원한 과정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectMobileContentOldView(Map<String, Object> commandMap) throws Exception{
			return studyManageDAO.selectMobileContentOldView(commandMap);
	}
	
	
	/**
	 * 모바일챕터리스트(웹에서의 페이지 레슨단위)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectMobileChapterList(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectMobileChapterList(commandMap);
	}
	
	/**
	 * 모바일 지원과정인지를 판단한다.
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String selectMobileSubject(Map<String, Object> commandMap) throws Exception{
		return studyManageDAO.selectMobileSubject(commandMap);
	}
	
	/**
	 * 모바일 앱에서 넘오언 진도 정보를 저장한다 - insert/update
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean insertUpdateMobileProgressAction(Map<String, Object> commandMap) throws Exception{
		boolean isOk = false;
		
		//모바일에서 넘어오는 컨텐츠 아이디 값 : subj || '||' || year || '||' || subjseq || '||' || module || '||' || lesson
		String CONTENT_ID = commandMap.get("CONTENT_ID") + "";
		//앱에서 넘오온 학습자 아이디
		String p_userid = commandMap.get("ID") + "";
		
		//챕터 - Lesson 아이디
		String CHAPTER_ID = commandMap.get("CHAPTER_ID") + "";
		
		//북마크 정보 book_mark
		String book_mark = (String)commandMap.get("BOOK_MARK");
		
		//챕터완료여부
//		String p_end = commandMap.get("p_end") + "";
		
		
		try{
			
			//이전재생시간(초) ------ 모바일 앱 >> 앱으로 보내서 이어보기를 하는 시간
			int CONTINUE_TIME = Integer.parseInt(commandMap.get("CONTINUE_TIME") + "");
			//실제총재생시간(초) ------ 모바일 앱 >> 진짜 진도 시간
			int PLAYING_TIME = Integer.parseInt(commandMap.get("PLAYING_TIME") + "");
			
			StringTokenizer st  = new StringTokenizer( CONTENT_ID, "||");
			
			
			
			if(st.hasMoreTokens())
			{	
		        String p_subj              = st.nextToken();
		        String p_year              = st.nextToken();
		        String p_subjseq           = st.nextToken();
		        String p_module           = st.nextToken();
		        String p_lesson           = st.nextToken();
		        
		        
		        commandMap.put("p_subj", 	p_subj);
	        	commandMap.put("p_year", 	p_year);
	        	commandMap.put("p_subjseq", p_subjseq);
	        	commandMap.put("p_module", 	p_module);
	        	
	        	
	        	
		        //모바일지원과정인지를 확인한다.
		        String mobileYn = studyManageDAO.selectMobileSubject(commandMap);
		        
		        if(mobileYn != null && mobileYn.equals("Y"))		//모바일지원과정일경우
				{
		        	 commandMap.put("subj", 	p_subj);
		        	 commandMap.put("year", 	p_year);
		        	 commandMap.put("subjseq", p_subjseq);
		        	 commandMap.put("module", 	p_module);
		        	 commandMap.put("lesson", 	CHAPTER_ID);
		        	 commandMap.put("userid", 	p_userid);
		        	 
		        	 
		        	Map progressInfo = (Map)lcmsProgressDAO.selectLcmsProgressOld(commandMap);
		        	

	    			String endCommitYn = lcmsProgressDAO.selectMobileEndCommitYn(commandMap);
	    			
	    			
		        	//진도 저장
		    		if(progressInfo != null){
		    			logger.info("##### progressInfo.lessonstatus : " + progressInfo.get("lessonstatus"));
		    			
		    			
		    			if(endCommitYn.equals("Y")){
		    				commandMap.put("p_end", "Y");
		    				lcmsProgressDAO.updateLcmsProgressOld(commandMap);
		    				lcmsProgressDAO.insertMobileLcmsProgressComplete(commandMap);
		    			}else{
		    				lcmsProgressDAO.updateLcmsProgressOld(commandMap);
		    			}
		    			
		    			
		    		}else{
		    			if(endCommitYn.equals("Y")){
		    				commandMap.put("lessonCount", "1");  
		    				commandMap.put("p_end", "Y");
			    			Object result1 = lcmsProgressDAO.insertLcmsOldProgress(commandMap);	
		    			}else{
		    				commandMap.put("lessonCount", "1");  
			    			Object result1 = lcmsProgressDAO.insertLcmsOldProgress(commandMap);
		    			}
		    		}
		    		
		    		
		    		//북마크 정보 저장
		    		if(book_mark != null && !book_mark.equals(""))
		    		{
		    			int count = lcmsProgressDAO.selectLcmsMobileBookMarkCount(commandMap);
		    			if(count > 0)
		    				lcmsProgressDAO.updateLcmsMobileBookMark(commandMap);
		    			else
		    				lcmsProgressDAO.insertLcmsMobileBookMark(commandMap);
		    		}
		    		
		    		//전체 진도율 반영
		    		if(commandMap.get("p_end") != null && commandMap.get("p_end").toString().equals("Y")){
		    			//상위자격 취득을 위한 사전연수 36차시를 30차시처럼
		    			String subjOneThree =  commandMap.get("subj").toString();
		    			System.out.println("insertUpdateMobileProgressAction subjOneThree ----> "+subjOneThree);
		    			String subjOneThreeYn ="N";
		    			if("PRF150017".equals(subjOneThree) || "PRF170004".equals(subjOneThree)) {
		    				subjOneThreeYn ="Y";
		    				commandMap.put("subjOneThreeYn", subjOneThreeYn);
		    			}
		    			System.out.println("insertUpdateMobileProgressAction subjOneThreeYn ----> "+subjOneThreeYn);
		    			
		    			commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateNormalOldProgress");
		//    			result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateEduUserOldProgress");
		    			
		    		} 
		        	
				}
		        else
		        {
		        	commandMap.put("p_lesson", 	p_lesson);
		        	
		        	
			       // commandMap.put("p_totalTime", 	p_totalTime);
			        //progresss에 초기 값이 있는지 없는지를 검사한다.
					int progressCount = studyManageDAO.selectMobileProgressCount(commandMap);
			        Map lessonInfo = studyManageDAO.selectMobileLessonInfo(commandMap);
			        
			        if(lessonInfo != null)
			        {
			        	//동영상 전체 시간
			        	int p_eduTime = Integer.parseInt(lessonInfo.get("eduTime") + "");
			        	//동영상 시작 경로
			        	String p_location = lessonInfo.get("starting") + "";
			        	//학습완료여부
			        	//String p_lessonstatus = "N";
			        	
			        	//동영상 시간이 전체시간보다 크거나 같은면 완료로 인정한다.
			        	//if(PLAYING_TIME >= eduTime)
			        	//{
			        	//	p_lessonstatus = "Y";
			        	//}
			        	
			        	commandMap.put("p_eduTime", 	p_eduTime);
			        	commandMap.put("p_location", 	p_location);
			        	
			        	if(progressCount > 0)
			        	{
			        		//수정
			        		studyManageDAO.updateMobileProgress(commandMap);
			        	}
			        	else
			        	{
			        		//등록
			        		studyManageDAO.insertMobileProgress(commandMap);
			        	}
			        	
			        }
		        
		        }
		        
		        
		        

	        	HashMap<String, Object> mm = new HashMap<String, Object>();
        		mm.put("p_subj", 	p_subj);
        		mm.put("p_year", 	p_year);
        		mm.put("p_subjseq", p_subjseq);
        		mm.put("p_userid", 	p_userid);
        		
        		//등록이 되어 있는지를 체크한다.
        		int cnt = stuMemberDAO.selectAttendCount(mm);
        		//출석등록이 안되어 잇다면
        		if(cnt == 0) stuMemberDAO.insertUserAttendance(mm);
        		
        		//학습자 참여도 점수 넣어주기
	    		 stuMemberDAO.updateUserAttendanceStudentScore(mm);
	        	
	        	
	        	
	        	isOk = true;
	        	
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isOk;
	}
	
	//모바일 학습여부 정보
	public Map selectOldItemMobileStatus(Map<String, Object> commandMap) throws Exception{
			return studyManageDAO.selectOldItemMobileStatus(commandMap);
	}
	
	
}
