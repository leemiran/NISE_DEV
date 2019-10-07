package egovframework.adm.rsh.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("sulmunQuestDAO")
public class SulmunQuestDAO extends EgovAbstractDAO {

	
	/**
	 * 설문 문제 관리 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> sulmunAllQuestList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.sulmunAllQuestList", commandMap);
	}
	
	/**
	 * 설문문제 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectTzSulmunView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("sulmunQuestDAO.selectTzSulmunView", commandMap);
	}
	
	/**
	 * 설문문제답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTzSulmunSelectList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectTzSulmunSelectList", commandMap);
	}
	
	/**
	 * 설문문제 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzSulmun(Map<String, Object> commandMap) throws Exception{
		return insert("sulmunQuestDAO.insertTzSulmun", commandMap);
	}
	
	/**
	 * 설문문제 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateTzSulmun(Map<String, Object> commandMap) throws Exception{
		return update("sulmunQuestDAO.updateTzSulmun", commandMap);
	}
	
	/**
	 * 설문문제 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteTzSulmun(Map<String, Object> commandMap) throws Exception{
		return delete("sulmunQuestDAO.deleteTzSulmun", commandMap);
	}
	
	/**
	 * 설문문제가 설문지에서 사용되었는지를 검사하는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectTzSulPaperCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("sulmunQuestDAO.selectTzSulPaperCount", commandMap);
	}
	
	/**
	 * 설문문제 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzSulmunSelect(Map<String, Object> commandMap) throws Exception{
		return insert("sulmunQuestDAO.insertTzSulmunSelect", commandMap);
	}
	
	/**
	 * 설문문제 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteTzSulmunSelect(Map<String, Object> commandMap) throws Exception{
		return delete("sulmunQuestDAO.deleteTzSulmunSelect", commandMap);
	}
	
	
	
	
	/**
	 * 설문 척도 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectScaleList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectScaleList", commandMap);
	}
	
	/**
	 * 설문척도보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectScaleView(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectScaleView", commandMap);
	}
	
	/**
	 * 설문척도 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertScale(Map<String, Object> commandMap) throws Exception{
		return insert("sulmunQuestDAO.insertScale", commandMap);
	}
	
	/**
	 * 설문척도 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateScale(Map<String, Object> commandMap) throws Exception{
		return update("sulmunQuestDAO.updateScale", commandMap);
	}
	
	/**
	 * 설문척도 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteScale(Map<String, Object> commandMap) throws Exception{
		return delete("sulmunQuestDAO.deleteScale", commandMap);
	}
	
	/**
	 * 설문척도 보기 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertScaleSel(Map<String, Object> commandMap) throws Exception{
		return insert("sulmunQuestDAO.insertScaleSel", commandMap);
	}
	
	/**
	 * 설문척도 보기 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteScaleSel(Map<String, Object> commandMap) throws Exception{
		return delete("sulmunQuestDAO.deleteScaleSel", commandMap);
	}
	
	/**
	 * 설문척도가 설문에서 사용되었는지를 검사하는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSulScaleCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("sulmunQuestDAO.selectSulScaleCount", commandMap);
	}
	
	
	/**
	 * 설문지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulPaperList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectSulPaperList", commandMap);
	}
	
	/**
	 * 설문지 전체 문제 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulPaperAllQuestList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectSulPaperAllQuestList", commandMap);
	}
	
	/**
	 * 설문지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertSulPaper(Map<String, Object> commandMap) throws Exception{
		return insert("sulmunQuestDAO.insertSulPaper", commandMap);
	}
	
	/**
	 * 설문지 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSulPaper(Map<String, Object> commandMap) throws Exception{
		return update("sulmunQuestDAO.updateSulPaper", commandMap);
	}
	
	/**
	 * 설문지 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteSulPaper(Map<String, Object> commandMap) throws Exception{
		return delete("sulmunQuestDAO.deleteSulPaper", commandMap);
	}

	/**
	 * 설문지가 과정에서 사용되었는지를 검사하는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSulPaperCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("sulmunQuestDAO.selectSulPaperCount", commandMap);
	}
	
	
	/**
	 * 설문지 미리보기 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulPaperPreviewList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectSulPaperPreviewList", commandMap);
	}
	
	/**
	 * 과정의 대한 설문 문제 정보 모두 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulResultSulNumsList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectSulResultSulNumsList", commandMap);
	}
	
	/**
	 * 설문 학습자 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulResultAnswersList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectSulResultAnswersList", commandMap);
	}
	
	
	/**
	 * 설문의 대한 수강생 전체인원수를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSulStudentMemberCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("sulmunQuestDAO.selectSulStudentMemberCount", commandMap);
	}

	/**
	 * 교육후기 리스트
	 * @param commandMap
	 * @return
	 */
	public List<?> selectHukiList(Map<String, Object> commandMap) {
		return list("sulmunQuestDAO.selectHukiList", commandMap);
	}
	
	/**
	 * 표준편차
	 * @param commandMap
	 * @return
	 */
	public Map<?, ?> selectSulResultStddev(Map<String, Object> commandMap) {
		return (Map<?,?>)selectByPk("sulmunQuestDAO.selectSulResultStddev", commandMap);
	}
	
	/**
	 * 퍼센트 과정의 대한 설문 문제 정보 모두 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectPerSulResultSulNumsList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectPerSulResultSulNumsList", commandMap);
	}
			
	/**
	 * 퍼센트 설문 학습자 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectPerSulResultAnswersList(Map<String, Object> commandMap) throws Exception{
		return list("sulmunQuestDAO.selectPerSulResultAnswersList", commandMap);
	}
	
	/**
	 * 퍼센트 설문의 대한 수강생 전체인원수를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectPerSulStudentMemberCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("sulmunQuestDAO.selectPerSulStudentMemberCount", commandMap);
	}
	
}
