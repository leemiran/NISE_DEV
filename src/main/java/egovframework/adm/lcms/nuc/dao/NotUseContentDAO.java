package egovframework.adm.lcms.nuc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("notUseContentDAO")
public class NotUseContentDAO extends EgovAbstractDAO{

	public int selectNotUseContenListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("notUseContentDAO.selectNotUseContenListTotCnt", commandMap);
	}
	
	public List selectNotUseContenListList(Map<String, Object> commandMap) throws Exception{
		return list("notUseContentDAO.selectNotUseContenListList", commandMap);
	}
	
}