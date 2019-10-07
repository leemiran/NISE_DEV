package egovframework.adm.cfg.cod.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("codeManageDAO")
public class CodeManageDAO extends EgovAbstractDAO{

	/**
	 * 시스템코드관리 대분류코드 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectListCode(Map<String, Object> commandMap) throws Exception{
		return list("codeManageDAO.selectListCode", commandMap);
	}
	
	/**
	 * 시스템코드관리 대분류코드 상세정보 조회 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map selectViewCode(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("codeManageDAO.selectViewCode", commandMap);
	}
	
	/**
	 * 시스템코드관리 대분류코드 수정 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateCode(Map<String, Object> commandMap) throws Exception{
		return update("codeManageDAO.updateCode", commandMap);
	}
	
	/**
	 * 시스템코드관리 대분류코드 삭제 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteCode(Map<String, Object> commandMap) throws Exception{
		return delete("codeManageDAO.deleteCode", commandMap);
	}
	
	/**
	 * 시스템코드관리 대분류코드 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public void insertCode(Map<String, Object> commandMap) throws Exception{
		insert("codeManageDAO.insertCode", commandMap);
	}
	
	/**
	 * 시스템코드관리 소분류코드 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteSubCode(Map<String, Object> commandMap) throws Exception{
		return delete("codeManageDAO.deleteSubCode", commandMap);
	}
	
	/**
	 * 시스템코드관리 소분류코드 리스트조회 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSubListCode(Map<String, Object> commandMap) throws Exception{
		return list("codeManageDAO.selectSubListCode", commandMap);
	}
	
	/**
	 * 시스템코드관리 소분류코드 상세정보 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map selectSubViewCode(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("codeManageDAO.selectSubViewCode", commandMap);
	}
	
	/**
	 * 시스템코드관리 소분류코드 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int subUpdateData(Map<String, Object> commandMap) throws Exception{
		return update("codeManageDAO.subUpdateData", commandMap);
	}
	
	/**
	 * 시스템코드관리 소분류코드 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int subDeleteData(Map<String, Object> commandMap) throws Exception{
		return delete("codeManageDAO.subDeleteData", commandMap);
	}
	
	/**
	 * 시스템코드관리 소분류코드 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public void subInsertData(Map<String, Object> commandMap) throws Exception{
		insert("codeManageDAO.subInsertData", commandMap);
	}
	
	/**
	 * 과정분류 분류코드 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectCursBunryuList(Map<String, Object> commandMap) throws Exception{
		return list("codeManageDAO.selectCursBunryuList", commandMap);
	}
	
	/**
	 * 교육관련 코드 리스트(tk_edu000t)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectEduListCode(Map<String, Object> commandMap) throws Exception{
		return list("codeManageDAO.selectEduListCode", commandMap);
	}
	
	
	/**
	 * 설문지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSulPaperList(Map<String, Object> commandMap) throws Exception{
		return list("codeManageDAO.selectSulPaperList", commandMap);
	}
	
	/**
	 * 출석고사장 학교 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSchoolRoomList(Map<String, Object> commandMap) throws Exception{
		return list("codeManageDAO.selectSchoolRoomList", commandMap);
	}
	
	
	/**
	 * 기관코드 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectEduOrgList(Map<String, Object> commandMap) throws Exception{
		return list("codeManageDAO.selectEduOrgList", commandMap);
	}
	
	/**
	 * 설문척도 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSulScaleList(Map<String, Object> commandMap) throws Exception{
		return list("codeManageDAO.selectSulScaleList", commandMap);
	}
	
	
	/**
	 * 검색에서 평가의 대한 종류를 가져오는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectExamResultPaperNum(Map<String, Object> commandMap) throws Exception{
		return list("codeManageDAO.selectExamResultPaperNum", commandMap);
	}
	
}
