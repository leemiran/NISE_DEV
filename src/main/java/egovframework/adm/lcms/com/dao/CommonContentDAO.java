package egovframework.adm.lcms.com.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("commonContentDAO")
public class CommonContentDAO extends EgovAbstractDAO{

	public Map selectMasterformData(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("commonContentDAO.selectMasterformData", commandMap);
	}
	
	public List selectMFMenuList(Map<String, Object> commandMap) throws Exception{
		return list("commonContentDAO.selectMFMenuList", commandMap);
	}
	
	public List selectMFSubjList(Map<String, Object> commandMap) throws Exception{
		return list("commonContentDAO.selectMFSubjList", commandMap);
	}
	
	public int updateMasterform(Map<String, Object> commandMap) throws Exception{
		return update("commonContentDAO.updateMasterform", commandMap);
	}
	
	public void deleteMFSubjList(Map<String, Object> commandMap) throws Exception{
		delete("commonContentDAO.deleteMFSubjList", commandMap);
	}
	
	public void insertMFSubjList(Map<String, Object> commandMap) throws Exception{
		insert("commonContentDAO.insertMFSubjList", commandMap);
	}
}