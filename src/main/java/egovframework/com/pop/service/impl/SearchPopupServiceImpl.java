package egovframework.com.pop.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.pop.dao.SearchPopupDAO;
import egovframework.com.pop.service.SearchPopupService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("searchPopupService")
public class SearchPopupServiceImpl extends EgovAbstractServiceImpl implements SearchPopupService{
	
	@Resource(name="searchPopupDAO")
    private SearchPopupDAO searchPopupDAO;
	
	/**
	 * 교육그룹 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchGrcodeListTotCnt(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchGrcodeListTotCnt(commandMap);
	}
	public List searchGrcodeList(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchGrcodeList(commandMap);
	}

	/**
	 * 과정 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchSubjListTotCnt(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchSubjListTotCnt(commandMap);
	}
	public List searchSubjList(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchSubjList(commandMap);
	}
	
	/**
	 * 회사 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchCompListTotCnt(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchCompListTotCnt(commandMap);
	}
	public List searchCompList(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchCompList(commandMap);
	}
	
	/**
	 * 회원 조회
	 * @param commandMap
	 * @return 
	 * @throws Exception
	 */
	public int searchMemberListTotCnt(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchMemberListTotCnt(commandMap);
	}
	public List searchMemberList(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchMemberList(commandMap);
	}
	
	/**
	 * 개별 회원정보 보기
	 * @param commandMap
	 * @return 
	 * @throws Exception
	 */
	public Map<?, ?> searchMemberInfoPopup(Map<String, Object> commandMap) throws Exception {
		return searchPopupDAO.searchMemberInfoPopup(commandMap);
	}
	
	
	/**
	 * 강사 조회
	 * @param commandMap
	 * @return 
	 * @throws Exception
	 */
	public int searchTutorListTotCnt(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchTutorListTotCnt(commandMap);
	}
	public List searchTutorList(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchTutorList(commandMap);
	}

	
	/**
	 * 우편번호 조회 - 지번이름으로 검색
	 * @param commandMap
	 * @return 
	 * @throws Exception
	 */
	public List searchZipcodeList(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchZipcodeList(commandMap);
	}

	/**
	 * 우편번호 조회 - 도로명으로 검색
	 * @param commandMap
	 * @return 
	 * @throws Exception
	 */
	public List searchZipcodeRoadList(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchZipcodeRoadList(commandMap);
	}
	
	
	/**
	 * sido 조회 - 도로명으로 검색
	 * @param commandMap
	 * @return 
	 * @throws Exception
	 */
	public List selectSido(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.selectSido(commandMap);
	}

	/**
	 * gugun 조회 - 도로명으로 검색
	 * @param commandMap
	 * @return 
	 * @throws Exception
	 */
	public List selectGugun(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.selectGugun(commandMap);
	}
	
	
	/**
	 * 과정 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int searchManagerSubjListTotCnt(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchManagerSubjListTotCnt(commandMap);
	}
	public List searchManagerSubjList(Map<String, Object> commandMap) throws Exception{
		return searchPopupDAO.searchManagerSubjList(commandMap);
	}
		
	

}