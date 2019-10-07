package egovframework.com.pop.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SearchPopupService {
	
	public int searchGrcodeListTotCnt(Map<String, Object> commandMap) throws Exception;
	public List searchGrcodeList(Map<String, Object> commandMap) throws Exception;

	public int searchSubjListTotCnt(Map<String, Object> commandMap) throws Exception;
	public List searchSubjList(Map<String, Object> commandMap) throws Exception;
	
	public int searchCompListTotCnt(Map<String, Object> commandMap) throws Exception;
	public List searchCompList(Map<String, Object> commandMap) throws Exception;
	
	public int searchMemberListTotCnt(Map<String, Object> commandMap) throws Exception;
	public List searchMemberList(Map<String, Object> commandMap) throws Exception;
	
	public Map<?, ?> searchMemberInfoPopup(Map<String, Object> commandMap) throws Exception;

	
	public int searchTutorListTotCnt(Map<String, Object> commandMap) throws Exception;
	public List searchTutorList(Map<String, Object> commandMap) throws Exception;

	
	public List searchZipcodeRoadList(Map<String, Object> commandMap) throws Exception;
	public List searchZipcodeList(Map<String, Object> commandMap) throws Exception;
	
	public List selectSido(Map<String, Object> commandMap) throws Exception;

	//gugun 조회
	public List selectGugun(Map<String, Object> commandMap) throws Exception;
	
	public int searchManagerSubjListTotCnt(Map<String, Object> commandMap) throws Exception;
	public List searchManagerSubjList(Map<String, Object> commandMap) throws Exception;

}
