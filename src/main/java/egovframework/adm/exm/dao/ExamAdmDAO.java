package egovframework.adm.exm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("examAdmDAO")
public class ExamAdmDAO extends EgovAbstractDAO {

	
	
	/**
	 * 평가문제 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamQuestList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamQuestList", commandMap);
	}
	
	/**
	 * 평가문제 미리보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamQuestPreviewList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamQuestPreviewList", commandMap);
	}

	/**
	 * 문제보기 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteExamSel(Map<String, Object> commandMap) throws Exception{
		return delete("examAdmDAO.deleteExamSel", commandMap);
	}
	
	/**
	 * 문제 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteExam(Map<String, Object> commandMap) throws Exception{
		return delete("examAdmDAO.deleteExam", commandMap);
	}
	
	
	/**
	 * 문항이 사용되었는지를 확인한다
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamQuestResultCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamQuestResultCount", commandMap);
	}
	
	
	/**
	 * 평가마스터 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamMasterList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamMasterList", commandMap);
	}
	
	
	

	/**
	 * 평가결과조회 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamResultList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamResultList", commandMap);
	}
	
	
	/**
	 * 평가결과 조회 전체평균정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamResultAvg(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectExamResultAvg", commandMap);
	}

	/**
	 * 학습자 재응시 횟수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int upddateExamUserRetry(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.upddateExamUserRetry", commandMap);
	}
	

	/**
	 * 평가자 시험 문제번호 모두 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserExamNumberList(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectUserExamNumberList", commandMap);
	}
	
	
	/**
	 * 학습자별 평가 문제지 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserExamPaperList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectUserExamPaperList", commandMap);
	}
	
	/**
	 * 학습자별 평가답안 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserExamResultList(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectUserExamResultList", commandMap);
	}

	/**
	 * 평가문제 Pool 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamQuestPoolList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamQuestPoolList", commandMap);
	}
	
	
	/**
	 * 평가문제 Pool 문제등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzExamPoolSelect(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertTzExamPoolSelect", commandMap);
	}
	
	
	/**
	 * 평가문제 Pool 문제보기 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzExamSelPoolSelect(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertTzExamSelPoolSelect", commandMap);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 평가문제 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectQuestExamView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectQuestExamView", commandMap);
	}
	
	/**
	 * 평가문제 해당 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectQuestExamSelList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectQuestExamSelList", commandMap);
	}
	
	/**
	 * 평가문제등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzExam(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertTzExam", commandMap);
	}
	
	
	/**
	 * 평가문제수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateTzExam(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateTzExam", commandMap);
	}
	
	/**
	 * 평가문제답안 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzExamSel(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertTzExamSel", commandMap);
	}
	
	/**
	 * 평가마스터관리에서 사용할 수 있는 문제의 개수를 뽑아오는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamLevelsCount(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectExamLevelsCount", commandMap);
	}
	
	/**
	 * 평가마스터 정보 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamMasterView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectExamMasterView", commandMap);
	}
	
	/**
	 * 평가마스터가 문제지에서 사용되었는지를 확인한다
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamMasterPaperCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamMasterPaperCount", commandMap);
	}
	
	
	/**
	 * 평가마스터등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzExamMaster(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertTzExamMaster", commandMap);
	}
	
	
	/**
	 * 평가마스터  삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteTzExamMaster(Map<String, Object> commandMap) throws Exception{
		return delete("examAdmDAO.deleteTzExamMaster", commandMap);
	}
	

	/**
	 * 평가마스터  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateTzExamMaster(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateTzExamMaster", commandMap);
	}
	
	
	
	
	
	
	/**
	 * 평가문제지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamPaperList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamPaperList", commandMap);
	}
	
	
	/**
	 * 평가문제지 정보 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamPaperView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectExamPaperView", commandMap);
	}
	
	/**
	 * 평가문제지 과정전체 등록시 사용 - 평가마스터 과정별 리스트 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamMasterSubjectList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamMasterSubjectList", commandMap);
	}
	
	/**
	 * 평가문제지 기수전체 등록시 사용 - 평가마스터 기수별 리스트 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamMasterGrSeqList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamMasterGrSeqList", commandMap);
	}
	
	/**
	 * 문제지 등록시 마스터가 존재하는지를 검사한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamMasterCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamMasterCount", commandMap);
	}
	
	/**
	 * 문제지가 출제가 되었는지를 검사한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamResultCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamResultCount", commandMap);
	}
	
	/**
	 * 평가문제지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertExamPaper(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertExamPaper", commandMap);
	}
	
	/**
	 * 평가문제지  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateExamPaper(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateExamPaper", commandMap);
	}
	
	
	/**
	 * 평가문제지 기간연장  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateExamPaperDate(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateExamPaperDate", commandMap);
	}
	
	/**
	 * 평가문제지  삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteExamPaper(Map<String, Object> commandMap) throws Exception{
		return delete("examAdmDAO.deleteExamPaper", commandMap);
	}

	/**
	 * 평가POOL 과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectExamPoolSubjList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamPoolSubjList", commandMap);
	}

	/**
	 * 평가POOL 팝업 과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List examPoolSubjListPop(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.examPoolSubjListPop", commandMap);
	}

	/**
	 * 평가POOL 과정 문제 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List examQuestionDetailList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.examQuestionDetailList", commandMap);
	}

	/**
	 * 평가POOL 과정 문제 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> examPoolSelect(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.examPoolSelect", commandMap);
	}

	public void insertQeustExcelToDB(Map inputMap) throws Exception{
		insert("examAdmDAO.insertQeustExcelToDB", inputMap);
	}

	public void insertQeustBogiExcelToDB(HashMap<String, Object> inputMap) throws Exception{
		insert("examAdmDAO.insertQeustBogiExcelToDB", inputMap);
	}
	
	
	/**
	 * 평가문제은행 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectExamBankList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamBankList", commandMap);
	}

	/**
	 * 평가문제은행 과정등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzExamSubj(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertTzExamSubj", commandMap);
	}
	
	/**
	 * 평가문제은행 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public Object selectTzExamSubj( Map<String, Object> commandMap) throws Exception{;
    	return (Object)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectTzExamSubj", commandMap);    	
	}
	
	/**
	 * 평가문제은행 수정
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public int updateTzExamSubj(Map<String, Object> commandMap) {
		return update("examAdmDAO.updateTzExamSubj", commandMap);
	}
	
	/**
	 * 평가문제은행 문제 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List examBankDetailList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.examBankDetailList", commandMap);
	}
	
	
	/**
	 * 평가마스터가 있는지
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamBankMasterCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamBankMasterCnt", commandMap);
	}
	
	
	/**
	 * 평가마스터등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzExamBankMaster(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertTzExamBankMaster", commandMap);
	}
	
	
	/**
	 * 평가문제지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertExamBankPaper(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertExamBankPaper", commandMap);
	}
	
	/**
	 * 평가마스터가 있는지
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamBankPaperNum(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamBankPaperNum", commandMap);
	}
	
	/**
	 * 평가문제 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertExamBankPaperSheet(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertExamBankPaperSheet", commandMap);
	}
	
	/**
	 * 평가마스터관리에서 사용할 수 있는 문제의 개수를 뽑아오는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamBankLevelsCount(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectExamBankLevelsCount", commandMap);
	}
	
	
	/**
	 * 평가문제지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamBankPaperView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectExamBankPaperView", commandMap);
	}
	
	/**
	 * 평가문제지  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateExamBankPaper(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateExamBankPaper", commandMap);
	}
	
	/**
	 * 출제문제 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteExamBankPaperSheet(Map<String, Object> commandMap) throws Exception{
		return delete("examAdmDAO.deleteExamBankPaperSheet", commandMap);
	}
	
	/**
	 * 콘텐츠 문제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamBankSubj(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectExamBankSubj", commandMap);
	}
	
	
	/**
	 * 문제지가 출제가 되었는지를 검사한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamBankResultCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamBankResultCount", commandMap);
	}
	
	//문제지 등록?
	public int selectExamBankPaperCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamBankPaperCount", commandMap);
	}
	
	/**
	 * 평가문제지 기간연장  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateExamBankPaperDate(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateExamBankPaperDate", commandMap);
	}
	
	/**
	 * 평가 기본정보 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateExamBankPaperBasic(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateExamBankPaperBasic", commandMap);
	}
	
	/**
	 * 평가문제지  삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteExamBankPaper(Map<String, Object> commandMap) throws Exception{
		return delete("examAdmDAO.deleteExamBankPaper", commandMap);
	}
	
	
	public void insertExamBankExcelToDB(Map inputMap) throws Exception{
		insert("examAdmDAO.insertExamBankExcelToDB", inputMap);
	}
	
	//
	public void insertExamBankBogiExcelToDB(HashMap<String, Object> inputMap) throws Exception{
		insert("examAdmDAO.insertExamBankBogiExcelToDB", inputMap);
	}
	
	
	/**
	 * 평가문제 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamBankView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectExamBankView", commandMap);
	}
	
	/**
	 * 평가문제 해당 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamBankSelList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamBankSelList", commandMap);
	}
	
	
	/**
	 * 평가문제수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateTzExamBank(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateTzExamBank", commandMap);
	}
	
	
	/**
	 * 평가 문제 정보 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateTzExamBankUseInfo(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateTzExamBankUseInfo", commandMap);
	}
	
	
	/**
	 * 문항이 사용되었는지를 확인한다
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamBankNumResultCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamBankNumResultCount", commandMap);
	}
	
	
	/**
	 * 평가 문제 정보 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateTzExamBankUseMaInfo(Map<String, Object> commandMap) throws Exception{
		return update("examAdmDAO.updateTzExamBankUseMaInfo", commandMap);
	}
	
	
	/**
	 * 평가문제 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteExamBankPaperSheetExam(Map<String, Object> commandMap) throws Exception{
		return delete("examAdmDAO.deleteExamBankPaperSheetExam", commandMap);
	}
	
	/**
	 * 평가문제 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertExamBankPaperSheetExam(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertExamBankPaperSheetExam", commandMap);
	}

	/**
	 * 기수에 문항이 사용되었는지를 확인한다
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamBankNumResultSubjseqCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamBankNumResultSubjseqCount", commandMap);
	}
	
	/**
	 * 평가문제 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertExamBankPaperSheetExamChange(Map<String, Object> commandMap) throws Exception{
		return insert("examAdmDAO.insertExamBankPaperSheetExamChange", commandMap);
	}
	
	
	/**
	 * 교체할 문제가 있는가?
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamBankPaperSheetExamCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamBankPaperSheetExamCount", commandMap);
	}
	
	/**
	 * 평가문제은행 다운로드
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamBankExcelDownList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamBankExcelDownList", commandMap);
	}
	
	/**
	 * 평가문제은행에서 사용할 수 있는 문제의 개수를 뽑아오는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamBankLevelsCnt(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("examAdmDAO.selectExamBankLevelsCnt", commandMap);
	}
	
	/**
	 * 등록할 문제 수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamBankLevelsInsCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("examAdmDAO.selectExamBankLevelsInsCnt", commandMap);
	}
	
	//문제 미리보기
	public List selectExamBankPaperExamViewList(Map<String, Object> commandMap) throws Exception{
		return list("examAdmDAO.selectExamBankPaperExamViewList", commandMap);
	}
	
}
