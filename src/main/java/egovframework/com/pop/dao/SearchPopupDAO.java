package egovframework.com.pop.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("searchPopupDAO")
public class SearchPopupDAO extends EgovAbstractDAO{
	
	/**
	 * 교육그룹 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchGrcodeListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("searchPopupDAO.searchGrcodeListTotCnt", commandMap);
	}
	public List searchGrcodeList(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.searchGrcodeList", commandMap);
	}

	/**
	 * 과정 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchSubjListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("searchPopupDAO.searchSubjListTotCnt", commandMap);
	}
	public List searchSubjList(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.searchSubjList", commandMap);
	}
	
	/**
	 * 회사 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchCompListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("searchPopupDAO.searchCompListTotCnt", commandMap);
	}
	public List searchCompList(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.searchCompList", commandMap);
	}
	
	/**
	 * 회원 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchMemberListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("searchPopupDAO.searchMemberListTotCnt", commandMap);
	}
	public List searchMemberList(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.searchMemberList", commandMap);
	}

	/**
	 * 개별 회원정보 보기
	 * @param commandMap
	 * @return 
	 * @throws Exception
	 */
	public Map<?, ?> searchMemberInfoPopup(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("searchPopupDAO.searchMemberInfoPopup", commandMap);
	}
	
	/**
	 * 강사 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchTutorListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("searchPopupDAO.searchTutorListTotCnt", commandMap);
	}
	public List searchTutorList(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.searchTutorList", commandMap);
	}

	
	/**
	 * 우편번호 조회 - 지번이름으로 검색
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List searchZipcodeList(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.searchZipcodeList", commandMap);
	}
	
	/**
	 * 우편번호 조회 - 도로명으로 검색
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List searchZipcodeRoadList(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.searchZipcodeRoadList", commandMap);
	}
	
	
	/**
	 * sido 조회 - 도로명으로 검색
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSido(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.selectSido", commandMap);
	}
	
	/**
	 * gugun 조회 - 도로명으로 검색
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectGugun(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.selectGugun", commandMap);
	}
	
	/**
	 * 과정 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchManagerSubjListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("searchPopupDAO.searchManagerSubjListTotCnt", commandMap);
	}
	public List searchManagerSubjList(Map<String, Object> commandMap) throws Exception{
		return list("searchPopupDAO.searchManagerSubjList", commandMap);
	}
	
}