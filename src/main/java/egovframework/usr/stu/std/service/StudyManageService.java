package egovframework.usr.stu.std.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StudyManageService {

	public String getProgress(Map<String, Object> commandMap) throws Exception;
	
	public String getPromotion(Map<String, Object> commandMap) throws Exception;
	
	public List SelectEduTimeCountOBC(Map<String, Object> commandMap) throws Exception;
	
	public Map SelectEduScore(Map<String, Object> commandMap) throws Exception;
	
	public Map getTutorInfo(Map<String, Object> commandMap) throws Exception;
	
	public List selectListOrderPerson(Map<String, Object> commandMap) throws Exception;
	
	public Map getStudyChasi(Map<String, Object> commandMap) throws Exception;
	
	public List selectGongList(Map<String, Object> commandMap) throws Exception;
	
	public List selectBoardList(Map<String, Object> commandMap) throws Exception;
	
	public List selectQnaBoardList(Map<String, Object> commandMap) throws Exception;
	
	public int getSulData(Map<String, Object> commandMap) throws Exception;
	
	public int getUserData(Map<String, Object> commandMap) throws Exception;
	
	public List getSulDate(Map<String, Object> commandMap) throws Exception;
	
	public Map getContenttype(Map<String, Object> commandMap) throws Exception;
	
	public List selectItemList(Map<String, Object> commandMap) throws Exception;
	
	public List selectOldItemMobileList(Map<String, Object> commandMap) throws Exception;
	
	public int selectLcmsCourseMapCount(Map<String, Object> commandMap) throws Exception;
	
	public int insertLcmsCourseMap(Map<String, Object> commandMap) throws Exception;
	
	public List selectSulpaperList(Map<String, Object> commandMap) throws Exception;
	
	public List selectReportList(Map<String, Object> commandMap) throws Exception;
	
	public List selectExamList(Map<String, Object> commandMap) throws Exception;
	
	public Integer checkDuplicateIP(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 모바일앱으로 컨텐츠 정보를 넘기기 위한 메소트 - 모바일로만 진행되는 과정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectMobileContentView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 모바일앱으로 컨텐츠 정보를 넘기기 위한 메소드 - 모바일을 지원한 과정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectMobileContentOldView(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 모바일챕터리스트(웹에서의 페이지 레슨단위)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectMobileChapterList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 모바일 지원과정인지를 판단한다.
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String selectMobileSubject(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 모바일 앱에서 넘오언 진도 정보를 저장한다 - insert/update
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean insertUpdateMobileProgressAction(Map<String, Object> commandMap) throws Exception;
	
	//모바일 학습여부 정보
	public Map selectOldItemMobileStatus(Map<String, Object> commandMap) throws Exception;
	
}
