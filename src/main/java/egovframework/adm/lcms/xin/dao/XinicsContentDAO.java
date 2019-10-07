package egovframework.adm.lcms.xin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("xinicsContentDAO")
public class XinicsContentDAO extends EgovAbstractDAO{
	
	/**
	 * 자이닉스교과 총수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectXinicsContentListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("xinicsContentDAO.selectXinicsContentListTotCnt", commandMap);
	}
	
	/**
	 * 자이닉스교과 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectXinicsContentList(Map<String, Object> commandMap) throws Exception{
		return list("xinicsContentDAO.selectXinicsContentList", commandMap);
	}
	
	/**
	 * 자이닉스컨텐츠 차시리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectXinicsOrgList(Map<String, Object> commandMap) throws Exception{
		return list("xinicsContentDAO.selectXinicsOrgList", commandMap);
	}

	
}
