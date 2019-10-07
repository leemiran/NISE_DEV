package egovframework.adm.com.main.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("mainManageDAO")
public class MainManageDAO extends EgovAbstractDAO{

	
	/**
	 * 관리자 메인 과정리스트 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectTotalSubSeqList(Map<String, Object> commandMap) throws Exception{
    	return list("MainManageDAO.selectTotalSubSeqList", commandMap);
    }
    
    /**
	 * 관리자 메인 공지사항 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectNoticeBoardList(Map<String, Object> commandMap) throws Exception{
    	return list("MainManageDAO.selectNoticeBoardList", commandMap);
    }
    
    
    /**
	 * 관리자 메인 질문방, 자료실 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectPdsBoardList(Map<String, Object> commandMap) throws Exception{
    	return list("MainManageDAO.selectPdsBoardList", commandMap);
    }

    /**
	 * 관리자 메인 연수문의 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectQnaBoardList(Map<String, Object> commandMap) {
		return list("MainManageDAO.selectQnaBoardList", commandMap);
	}

	/**
     * 당해 연도 운영 현황 데이터 생성
     * @param commandMap
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> admYearEduStatus(Map<String, Object> commandMap) throws Exception{
		return list("MainManageDAO.admYearEduStatus", commandMap);
	}

	/**
     * 당해 연도 운영 현황 내역 조회
     * @param commandMap
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> admYearEduStatusList(Map<String, Object> commandMap) throws Exception{
		return list("MainManageDAO.admYearEduStatusList", commandMap);
	}

	/**
     * 당해 연도 운영 현황 등록
     * @param commandMap
     * @return
     */
	public int admYearEduStatusInsert(Map<String, Object> map) throws Exception{
		return update("MainManageDAO.admYearEduStatusInsert", map);
	}

	/**
     * 당해 연도 운영 현황 수정
     * @param commandMap
     * @return
     */
	public int admYearEduStatusUpdate(Map<String, Object> commandMap) throws Exception{
		return update("MainManageDAO.admYearEduStatusUpdate", commandMap);
	}

	/**
     * 당해 연도 운영 현황 삭제
     * @param commandMap
     * @return
     */
	public int admYearEduStatusDelete(Map<String, Object> commandMap) throws Exception{
		return delete("MainManageDAO.admYearEduStatusDelete", commandMap);
	}
    
    
    
	
}
