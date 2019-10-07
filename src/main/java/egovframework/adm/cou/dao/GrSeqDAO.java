package egovframework.adm.cou.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("grSeqDAO")
public class GrSeqDAO extends EgovAbstractDAO {

	/**
	 * 기수목록총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqEmptyList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectGrSeqEmptyList", commandMap);
	}

	/**
	 * 지정된 기수/코스가 없는 교육차수Record Add
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectGrSeqList", commandMap);
	}
	
	/**
	 * 교육기수에 신청 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqStudentProposeList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectGrSeqStudentProposeList", commandMap);
	}

	
	/**
	 * 교육기수에 취소 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqStudentCancelList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectGrSeqStudentCancelList", commandMap);
	}

	
	/**
	 * 교육기수에 신청 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqStudentStudentList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectGrSeqStudentStudentList", commandMap);
	}

	
	/**
	 * 교육기수에 수료자 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqStudentStoldList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectGrSeqStudentStoldList", commandMap);
	}
	
	/**
	 * 교육기수 상세 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqDetailList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectGrSeqDetailList", commandMap);
	}

	
	/**
	 * 기수상세화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectGrSeqView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("grSeqDAO.selectGrSeqView", commandMap);
	}
	
	/**
	 * 복사할 교육그룹 또는 과정을 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqMakeOption(Map<String, Object> commandMap) throws Exception {
		return list("grSeqDAO.selectGrSeqMakeOption", commandMap);
	}
	
	
	/**
	 * 해당기수가 존재하는지를 체크하기 위하여 카운트 한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSubjSeqCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("grSeqDAO.selectSubjSeqCnt", commandMap);
	}

	
	/**
	 * 기수 등록시 게시판 등록하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertSubjBbs(Map<String, Object> commandMap) throws Exception{
		delete("grSeqDAO.deleteSubjBbs", commandMap);
		return insert("grSeqDAO.insertSubjBbs", commandMap);
	}
	
	
	/**
	 * 기수 등록시 Report 출제정보 Copy tz_progjgrp 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertProjGrp(Map<String, Object> commandMap) throws Exception{
		delete("grSeqDAO.deleteProjGrp", commandMap);
		return insert("grSeqDAO.insertProjGrp", commandMap);
	}
	
	/**
	 * 기수 등록시 Report 출제정보 Copy tz_progjmap 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertProjMap(Map<String, Object> commandMap) throws Exception{
		delete("grSeqDAO.deleteProjMap", commandMap);
		return insert("grSeqDAO.insertProjMap", commandMap);
	}
	
	
	
	/**
	 * 기수 등록시 시험정보 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertExamPaper(Map<String, Object> commandMap) throws Exception{
		delete("grSeqDAO.deleteExamPaper", commandMap);
		return insert("grSeqDAO.insertExamPaper", commandMap);
	}
	
	
	
	/**
	 * 교육 기수 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertGrSeq(Map<String, Object> commandMap) throws Exception{
		return insert("grSeqDAO.insertGrSeq", commandMap);
	}
	
	
	
	/**
	 * 기수 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertSubjSeq(Map<String, Object> commandMap) throws Exception{
		return insert("grSeqDAO.insertSubjSeq", commandMap);
	}
	
	
	/**
	 * 교육기수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateGrSeq(Map<String, Object> commandMap) throws Exception{
		return update("grSeqDAO.updateGrSeq", commandMap);
	}
	
	
	/**
	 * 교육 기수 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteGrSeq(Map<String, Object> commandMap) throws Exception{
		return delete("grSeqDAO.deleteGrSeq", commandMap);
	}
	
	/**
	 * 해당교육기수의 수강생이 존재하는지를 체크한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectProposeCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("grSeqDAO.selectProposeCnt", commandMap);
	}
	
	/**
	 * 과정 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteCourseSeq(Map<String, Object> commandMap) throws Exception{
		return delete("grSeqDAO.deleteCourseSeq", commandMap);
	}
	
	/**
	 * 기수관련 테이블 모두 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteSubjSeqAll(Map<String, Object> commandMap) throws Exception{
		return delete("grSeqDAO.deleteSubjSeqAll", commandMap);
	}
	
	
	/**
	 * 기수 - 출석고사장 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqSchoolList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectGrSeqSchoolList", commandMap);
	}

	
	/**
	 * 기수 상세정보 - 수정화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectGrSeqDetailView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("grSeqDAO.selectGrSeqDetailView", commandMap);
	}
	
	/**
	 * 현재새로운기수 수정시 기수가 존재하는지를 체크한다. 
	 * @param commandMap
	 * @return 기수의 개수를 반환한다.
	 * @throws Exception
	 */
	public int selectCheckSubjseq(Map<String, Object> commandMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("grSeqDAO.selectCheckSubjseq", commandMap);
	}
	
	/**
	 * 단일 기수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSubjseq(Map<String, Object> commandMap) throws Exception{
		return update("grSeqDAO.updateSubjseq", commandMap);
	}
	
	/**
	 * 교육기수명 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectGrSeqName(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("grSeqDAO.selectGrSeqName", commandMap);
	}
	
	
	/**
	 * 교육기수 일괄 수정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUpdateSubjCourseList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectUpdateSubjCourseList", commandMap);
	}
	
	/**
	 * 교육기수 일괄 지정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectAssignSubjCourseList(Map<String, Object> commandMap) throws Exception{
		return list("grSeqDAO.selectAssignSubjCourseList", commandMap);
	}
	
	
	/**
	 * 일일 학습제한 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateEdulimit(Map<String, Object> commandMap) throws Exception{
		return update("grSeqDAO.updateEdulimit", commandMap);
	}
	
	
	/**
	 * 일일 수강신청기간 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updatePropose(Map<String, Object> commandMap) throws Exception{
		return update("grSeqDAO.updatePropose", commandMap);
	}
	
	/**
	 * 일일 학습기간 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateEdu(Map<String, Object> commandMap) throws Exception{
		return update("grSeqDAO.updateEdu", commandMap);
	}
	
	/**
	 * 일일 학습제한 교육기수(GrSeq) 전체 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateGrSeqEdulimit(Map<String, Object> commandMap) throws Exception{
		return update("grSeqDAO.updateGrSeqEdulimit", commandMap);
	}


	/**
	 * 패키지과정 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertCourseSeq(Map<String, Object> commandMap) throws Exception{
		return insert("grSeqDAO.insertCourseSeq", commandMap);
	}

	// 2017 추가
	public List<?> selectAreaCodeList() throws Exception {
		return list("grSeqDAO.selectAreaCodeList", null);
	}

	public int deleteSubjseqArea(Map<String, Object> commandMap) throws Exception {
		return delete("grSeqDAO.deleteSubjseqArea", commandMap);
	}

	public Object insertSubjseqArea(Map<String, Object> insertMap) throws Exception {
		return insert("grSeqDAO.insertSubjseqArea", insertMap);
	}

	public String selectSubjseqAreaCodeConcat(Map<String, Object> commandMap) {
		return (String) selectByPk("grSeqDAO.selectSubjseqAreaCodeConcat", commandMap);
	}
	// 2017 추가 끝
	
	/**
	 * 과정 복습 / 수료처리  여부 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSubjseqReviewIsclosed(Map<String, Object> commandMap) throws Exception{
		return update("grSeqDAO.updateSubjseqReviewIsclosed", commandMap);
	}
	
	/**
	 * 과정 복습 / 수료번호 초기화
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteStudentSerno(Map<String, Object> commandMap) throws Exception{
		return update("grSeqDAO.deleteStudentSerno", commandMap);
	}

}