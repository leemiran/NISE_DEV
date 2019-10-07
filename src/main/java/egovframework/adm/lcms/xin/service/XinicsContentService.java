package egovframework.adm.lcms.xin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface XinicsContentService {
	
	/**
	 * 자이닉스교과 총수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectXinicsContentListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 자이닉스교과 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List selectXinicsContentList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 자이닉스컨텐츠 등록
	 * @param dataList
	 * @return
	 * @throws Exception
	 */
	int insertXinicsContent(ArrayList dataList) throws Exception;
	
	/**
	 * 자이닉스컨텐츠 차시리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List selectXinicsOrgList(Map<String, Object> commandMap) throws Exception;

}
