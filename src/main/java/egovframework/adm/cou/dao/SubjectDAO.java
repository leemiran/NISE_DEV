package egovframework.adm.cou.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("subjectDAO")
public class SubjectDAO extends EgovAbstractDAO {

	/**
	 * 과정목록총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSubjectListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("subjectDAO.selectSubjectListTotCnt", commandMap);
	}
	
	/**
	 * 과정 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSubjectList(Map<String, Object> commandMap) throws Exception{
		return list("subjectDAO.selectSubjectList", commandMap);
	}
	public List selectAreaList(Map<String, Object> commandMap) {
		return list("subjectDAO.selectAreaList", commandMap);
	}
	/**
	 * 과정상세화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSubjectView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("subjectDAO.selectSubjectView", commandMap);
	}
	
	
	
	/**
	 * 과정 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertSubject(Map<String, Object> commandMap) throws Exception{
		return insert("subjectDAO.insertSubject", commandMap);
	}
	
	
	/**
	 * 과정 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSubject(Map<String, Object> commandMap) throws Exception{
		return update("subjectDAO.updateSubject", commandMap);
	}
	
	
	/**
	 * 과정 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteSubject(Map<String, Object> commandMap) throws Exception{
		return delete("subjectDAO.deleteSubject", commandMap);
	}
	
	/**
	 * 이어보기 관련정보테이블
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public void insertSubjContInfo(Map<String, Object> commandMap) throws Exception{
		insert("subjectDAO.insertSubjContInfo", commandMap);
	}
	
	/**
	 * 교육그룹 연결
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public void insertGrpSubj(Map<String, Object> commandMap) throws Exception{
		insert("subjectDAO.insertGrpSubj", commandMap);
	}
	
	/**
	 * 기수명 수정하기 - 과정수정시 같이 수정하여 준다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSubjSeq(Map<String, Object> commandMap) throws Exception{
		return update("subjectDAO.updateSubjSeq", commandMap);
	}
	
	/**
	 * tz_grsubj 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteGrSubj(Map<String, Object> commandMap) throws Exception{
		return delete("subjectDAO.deleteGrSubj", commandMap);
	}
	
	/**
	 * tz_bds 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteBds(Map<String, Object> commandMap) throws Exception{
		return delete("subjectDAO.deleteBds", commandMap);
	}
	
	/**
	 * tz_preview 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deletePreview(Map<String, Object> commandMap) throws Exception{
		return delete("subjectDAO.deletePreview", commandMap);
	}
	
	/**
	 * 나의관심과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectConcernInfoList(Map<String, Object> commandMap) throws Exception{
		return list("subjectDAO.selectConcernInfoList", commandMap);
	}
	
	
	/**
	 * 관심과정 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteConcernInfo(Map<String, Object> commandMap) throws Exception{
		return delete("subjectDAO.deleteConcernInfo", commandMap);
	}
	
	
	/**
	 * 등록된 관심과정 개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectConcernInfoCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("subjectDAO.selectConcernInfoCnt", commandMap);
	}
	
	/**
	 * 관심과정 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public void insertConcernInfo(Map<String, Object> commandMap) throws Exception{
		insert("subjectDAO.insertConcernInfo", commandMap);
	}


	/**
	 * 직무관련 -기타 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSubjEtcCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("subjectDAO.selectSubjEtcCount", commandMap);
	}





}
