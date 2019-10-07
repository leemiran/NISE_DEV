package egovframework.adm.tut.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("tutorDAO")
public class TutorDAO extends EgovAbstractDAO {

	
	/**
	 * 강사 리스트 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectTutorTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("tutorDAO.selectTutorTotCnt", commandMap);
	}
	
	
	/**
	 * 강사 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTutorList(Map<String, Object> commandMap) throws Exception{
		return list("tutorDAO.selectTutorList", commandMap);
	}
	
	/**
	 * 강사 강의 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTutorSubjList(Map<String, Object> commandMap) throws Exception{
		return list("tutorDAO.selectTutorSubjList", commandMap);
	}
	
	/**
	 * 강사 강의 이력 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTutorSubjHistoryList(Map<String, Object> commandMap) throws Exception{
		return list("tutorDAO.selectTutorSubjHistoryList", commandMap);
	}
	
	/**
	 * 강사 정보 보기 화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectTutorView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("tutorDAO.selectTutorView", commandMap);
	}
	
	/**
	 * 강사 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTutor(Map<String, Object> commandMap) throws Exception{
		return insert("tutorDAO.insertTutor", commandMap);
	}
	
	/**
	 * 강사 아이디 중복체크 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectTutorUserIdCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("tutorDAO.selectTutorUserIdCount", commandMap);
	}
	
	
	/**
	 * 강사 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateTutor(Map<String, Object> commandMap) throws Exception{
		return update("tutorDAO.updateTutor", commandMap);
	}
	
	
	/**
	 * 강사 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteTutor(Map<String, Object> commandMap) throws Exception{
		return delete("tutorDAO.deleteTutor", commandMap);
	}

	/**
	 * 강사 과정 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTutorSubj(Map<String, Object> commandMap) throws Exception{
		return insert("tutorDAO.insertTutorSubj", commandMap);
	}
	
	
	/**
	 * 강사 과정 모두 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteTutorSubj(Map<String, Object> commandMap) throws Exception{
		return delete("tutorDAO.deleteTutorSubj", commandMap);
	}









}
