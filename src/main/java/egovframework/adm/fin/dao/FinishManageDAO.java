package egovframework.adm.fin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("finishManageDAO")
public class FinishManageDAO extends EgovAbstractDAO{

	public List selectFinishCourseList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectFinishCourseList", commandMap);
	}
	
	public Map selectPackage(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("finishManageDAO.selectPackage", commandMap);
	}
	
	public List selectFinishStudentList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectFinishStudentList", commandMap);
	}
	
	public List selectBookList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectBookList", commandMap);
	}
	
	public List selectExamList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectExamList", commandMap);
	}
	
	public Map SelectSubjseqInfoDbox(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("finishManageDAO.SelectSubjseqInfoDbox", commandMap);
	}
	
	public List ScoreCntList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.ScoreCntList", commandMap);
	}
	
	public int getCntBookMonth(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("finishManageDAO.getCntBookMonth", commandMap);
	}
	
	public Object getCompleteSerno(Map<String, Object> commandMap) throws Exception{
		return (Object)selectByPk("finishManageDAO.getCompleteSerno", commandMap);
	}
	
	public String getMaxCompleteCode(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("finishManageDAO.getMaxCompleteCode", commandMap);
	}
	
	public int graduatedUpdate(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.graduatedUpdate", commandMap);
	}
	
	public int updateStudentSerno(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateStudentSerno", commandMap);
	}
	
	public int updateStudentIsgraduated(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateStudentIsgraduated", commandMap);
	}
	
	public int deleteStroutProc(Map<String, Object> commandMap) throws Exception{
		return delete("finishManageDAO.deleteStroutProc", commandMap);
	}
	
	public int deleteStroutYear(Map<String, Object> commandMap) throws Exception{
		return delete("finishManageDAO.deleteStroutYear", commandMap);
	}
	
	public int deleteStoldTable(Map<String, Object> commandMap) throws Exception{
		return delete("finishManageDAO.deleteStoldTable", commandMap);
	}
	
	public int chkRemainReport(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("finishManageDAO.chkRemainReport", commandMap);
	}
	
	public List selectCompleteStudent(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectCompleteStudent", commandMap);
	}
	
	public String getSubmitExamYn(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("finishManageDAO.getSubmitExamYn", commandMap);
	}
	
	public int updateCompleteStudent(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateCompleteStudent", commandMap);
	}
	
	public void insertStoldTable(Map<String, Object> commandMap) throws Exception{
		insert("finishManageDAO.insertStoldTable", commandMap);
	}
	
	public int updateStudentSernoAll(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateStudentSernoAll", commandMap);
	}
	
	public int setCloseColumn(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.setCloseColumn", commandMap);
	}
	
	public int updateIsCpflag(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateIsCpflag", commandMap);
	}
	
	public int updateOutSubjReject(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateOutSubjReject", commandMap);
	}
	
	public int updateRecalcudate(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateRecalcudate", commandMap);
	}
	
	public List suRoyJeungPrintList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.suRoyJeungPrintList", commandMap);
	}
	
	public int updateStudentEditlink(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateStudentEditlink", commandMap);
	}
	
	public List selectScoreVarList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectScoreVarList", commandMap);
	}
	
	public List selectCrtVarList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectCrtVarList", commandMap);
	}
	
	public int selectStudentTotalCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("finishManageDAO.selectStudentTotalCnt", commandMap);
	}
	
	public int selectVarSumCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("finishManageDAO.selectVarSumCnt", commandMap);
	}
	
	public int selectCrtVarCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("finishManageDAO.selectCrtVarCount", commandMap);
	}
	
	public int deleteLank(Map<String, Object> commandMap) throws Exception{
		return delete("finishManageDAO.deleteLank", commandMap);
	}
	
	public int setNum(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.setNum", commandMap);
	}
	
	public void setLank(Map<String, Object> commandMap) throws Exception{
		insert("finishManageDAO.setLank", commandMap);
	}
	
	public List selectScoreList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectScoreList", commandMap);
	}
	
	public int deleteCrtVar(Map<String, Object> commandMap) throws Exception{
		return delete("finishManageDAO.deleteCrtVar", commandMap);
	}
	
	public void insertCrtVar(Map<String, Object> commandMap) throws Exception{
		insert("finishManageDAO.insertCrtVar", commandMap);
	}
	
	public int updateStudentEditscore(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateStudentEditscore", commandMap);
	}
	
	public int selectStudyCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("finishManageDAO.selectStudyCount", commandMap);
	}
	
	
	/**
     * 개인별 수료리스트 - 모바일용
     * @return
     * @exception Exception
     */
	public List selectMblUserSuryuList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectMblUserSuryuList", commandMap);
	}
	
	
	/**
     * 이수관리 > 과거이수내역리스트
     * @return
     * @exception Exception
     */
	public List selectfinishOldList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectFinishOldList", commandMap);
	}
	
	/**
     * 이수관리 > 과거이수내역리스트 전체개수
     * @return
     * @exception Exception
     */
	public int selectfinishOldListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("finishManageDAO.selectFinishOldListTotCnt", commandMap);
	}
	
	/**
     * 나의학습방 > 나의 교육이력 >  과거이수내역리스트(사용자)
     * @return
     * @exception Exception
     */
	public List selectUserFinishOldList(Map<String, Object> commandMap) throws Exception{
		return list("finishManageDAO.selectUserFinishOldList", commandMap);
	}

	
	/**
     * 이수관리 > 과거이수내역 보기
     * @return
     * @exception Exception
     */
	public Map selectFinishOldView(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("finishManageDAO.selectFinishOldView", commandMap);
	}
	
	/**
     * 이수관리 > 과거이수내역 삭제
     * @return
     * @exception Exception
     */
	public int deleteFinishOld(Map<String, Object> commandMap) throws Exception{
		return delete("finishManageDAO.deleteFinishOld", commandMap);
	}
	
	/**
     * 이수관리 > 과거이수내역 등록
     * @return
     * @exception Exception
     */
	public void insertFinishOld(Map<String, Object> commandMap) throws Exception{
		insert("finishManageDAO.insertFinishOld", commandMap);
	}
	
	/**
     * 이수관리 > 과거이수내역 수정
     * @return
     * @exception Exception
     */
	public int updateFinishOld(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.updateFinishOld", commandMap);
	}

	/**
	 * 이수관리 > 엑셀출력
	 * @param commandMap
	 * @return
	 */
	public List finishStudentExcelList(Map<String, Object> commandMap) {
		return list("finishManageDAO.finishStudentExcelList", commandMap);
	}
	
	//수료증 출력여부
	public int suroyprintYnUpdate(Map<String, Object> commandMap) throws Exception{
		return update("finishManageDAO.suroyprintYnUpdate", commandMap);
	}
	
	//이수yn 삭제
	public int deleteUsergraduated(Map<String, Object> commandMap) throws Exception{
		return delete("finishManageDAO.deleteUsergraduated", commandMap);
	}
	
	/**
     * 이수관리 > 이수yn 등록
     * @return
     * @exception Exception
     */
	public void insertUsergraduated(Map<String, Object> commandMap) throws Exception{
		insert("finishManageDAO.insertUsergraduated", commandMap);
	}

	
}
