package egovframework.adm.cfg.aut.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("menuAuthorityDAO")
public class MenuAuthorityDAO extends EgovAbstractDAO{

	public List selectListGadmin(Map<String, Object> commandMap) throws Exception{
		return list("menuAuthorityDAO.selectListGadmin", commandMap);
	}
	
	public List selectMenuAuthList(Map<String, Object> commandMap) throws Exception{
		return list("menuAuthorityDAO.selectMenuAuthList", commandMap);
	}
	
	public int menuAuthDelete(Map<String, Object> commandMap) throws Exception{
		return delete("menuAuthorityDAO.menuAuthDelete", commandMap);
	}
	
	public void menuAuthInsert(Map<String, Object> commandMap) throws Exception{
		insert("menuAuthorityDAO.menuAuthInsert", commandMap);
	}
}
