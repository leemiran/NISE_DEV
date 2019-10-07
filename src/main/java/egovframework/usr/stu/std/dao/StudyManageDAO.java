package egovframework.usr.stu.std.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyManageDAO")
public class StudyManageDAO extends EgovAbstractDAO{

	public String getProgress(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("studyManageDAO.getProgress", commandMap);
	}
	
	public String getNewScormProgress(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("studyManageDAO.getNewScormProgress", commandMap);
	}
	
	public String getNewNonScormProgress(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("studyManageDAO.getNewNonScormProgress", commandMap);
	}
	
	public List SelectEduTimeCountOBC(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.SelectEduTimeCountOBC", commandMap);
	}
	
	public Map SelectEduScore(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyManageDAO.SelectEduScore", commandMap);
	}
	
	public Map getTutorInfo(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyManageDAO.getTutorInfo", commandMap);
	}
	
	public List selectListOrderPerson(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectListOrderPerson", commandMap);
	}
	
	public Object selectWstep(Map<String, Object> commandMap) throws Exception{
		return (Object)selectByPk("studyManageDAO.selectWstep", commandMap);
	}
	
	public int selectOldProgress(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectOldProgress", commandMap);
	}
	
	public int selectOldlesson(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectOldlesson", commandMap);
	}
	
	public int selectAttendCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectAttendCnt", commandMap);
	}
	
	public int selectNewScormlesson(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectNewScormlesson", commandMap);
	}
	
	public int selectNewScormProgress(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectNewScormProgress", commandMap);
	}
	
	public int selectNewNonscormlesson(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectNewNonscormlesson", commandMap);
	}
	
	public int selectNewNonscormProgress(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectNewNonscormProgress", commandMap);
	}
	
	public List selectGongList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectGongList", commandMap);
	}
	
	public List selectBoardList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectBoardList", commandMap);
	}
	
	public List selectQnaBoardList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectQnaBoardList", commandMap);
	}
	
	public int getSulData(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.getSulData", commandMap);
	}
	
	public int getUserData(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.getUserData", commandMap);
	}
	
	public List getSulDate(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.getSulDate", commandMap);
	}
	
	public Map getContenttype(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyManageDAO.getContenttype", commandMap);
	}
	
	public List selectOldItemList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectOldItemList", commandMap);
	}
	
	public List selectOldItemMobileList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectOldItemMobileList", commandMap);
	}
	
	public List selectNewScormItemList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectNewScormItemList", commandMap);
	}
	
	public List selectNewNonScormItemList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectNewNonScormItemList", commandMap);
	}
	
	public List selectNewXiniceItemList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectNewXiniceItemList", commandMap);
	}
	
	public int selectLcmsCourseMapCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectLcmsCourseMapCount", commandMap);
	}
	
	public Object insertLcmsCourseMap(Map<String, Object> commandMap) throws Exception{
		return insert("studyManageDAO.insertLcmsCourseMap", commandMap);
	}
	
	public List selectSulpaperList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectSulpaperList", commandMap);
	}
	
	public List selectReportList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectReportList", commandMap);
	}
	
	public List selectExamList(Map<String, Object> commandMap) throws Exception{
		return list("studyManageDAO.selectExamList", commandMap);
	}
	
	public Integer checkDuplicateIP(Map<String, Object> commandMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.checkDuplicateIP", commandMap);
	}
	
	/**
	 * 모바일앱으로 컨텐츠 정보를 넘기기 위한 메소트 - 모바일로만 진행되는 과정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectMobileContentView(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyManageDAO.selectMobileContentView", commandMap);
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
		return (Map)selectByPk("studyManageDAO.selectMobileContentOldView", commandMap);
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
		return list("studyManageDAO.selectMobileChapterList", commandMap);
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
		return (String)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectMobileSubject", commandMap);
	}
	
	/**
	 * 모바일앱에서 넘어온 정보를 tb_lcms_progress 테이블에 존재하는지를 검사한다.
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int selectMobileProgressCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyManageDAO.selectMobileProgressCount", commandMap);
	}
	
	/**
	 * 모바일 동영상 학습의 필요한 학습시간과 시작페이지 정보를 얻어온다.
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectMobileLessonInfo(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyManageDAO.selectMobileLessonInfo", commandMap);
	}
	
	
	/**
	 * 모바일 앱에서 넘오언 진도 정보를 저장한다 - insert
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertMobileProgress(Map<String, Object> commandMap) throws Exception{
		return insert("studyManageDAO.insertMobileProgress", commandMap);
	}
	
	
	/**
	 * 모바일 앱에서 넘오언 진도 정보를 저장한다 - update
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int updateMobileProgress(Map<String, Object> commandMap) throws Exception{
		return update("studyManageDAO.updateMobileProgress", commandMap);
	}
	
	//모바일 학습여부 정보
	public Map selectOldItemMobileStatus(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyManageDAO.selectOldItemMobileStatus", commandMap);
	}
	
	
}
