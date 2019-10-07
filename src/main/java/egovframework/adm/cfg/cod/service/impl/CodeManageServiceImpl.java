package egovframework.adm.cfg.cod.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cfg.cod.dao.CodeManageDAO;
import egovframework.adm.cfg.cod.service.CodeManageService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("codeManageService")
public class CodeManageServiceImpl extends EgovAbstractServiceImpl implements CodeManageService{
	
	@Resource(name="codeManageDAO")
    private CodeManageDAO codeManageDAO;

	public List selectListCode(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectListCode(commandMap);
	}
	
	public Map selectViewCode(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectViewCode(commandMap);
	}
	
	public int updateCode(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.updateCode(commandMap);
	}
	
	public int deleteCode(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		codeManageDAO.deleteCode(commandMap);
		codeManageDAO.deleteSubCode(commandMap);
		return isOk;
	}
	
	public int insertCode(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			codeManageDAO.insertCode(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}

	public List selectSubListCode(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectSubListCode(commandMap);
	}
	
	public Map selectSubViewCode(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectSubViewCode(commandMap);
	}
	
	public int subUpdateData(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.subUpdateData(commandMap);
	}
	
	public int subDeleteData(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.subDeleteData(commandMap);
	}
	
	public int subInsertData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			codeManageDAO.subInsertData(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	
	/**
	 * 과정분류 대분류코드 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectCursBunryuList(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectCursBunryuList(commandMap);
	}
	
	
	/**
	 * 교육관련 코드 리스트(tk_edu000t)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectEduListCode(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectEduListCode(commandMap);
	}
	
	
	/**
	 * 설문지 리스트 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSulPaperList(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectSulPaperList(commandMap);
	}
	
	/**
	 * 출석고사장 학교 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSchoolRoomList(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectSchoolRoomList(commandMap);
	}
	
	
	/**
	 * 기관코드 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectEduOrgList(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectEduOrgList(commandMap);
	}
	
	/**
	 * 설문척도 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSulScaleList(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectSulScaleList(commandMap);
	}
	
	/**
	 * 검색에서 평가의 대한 종류를 가져오는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectExamResultPaperNum(Map<String, Object> commandMap) throws Exception{
		return codeManageDAO.selectExamResultPaperNum(commandMap);
	}
	
	
}
