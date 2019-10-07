package egovframework.adm.com.cmp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("selectCompanyDAO")
public class SelectCompanyDAO extends EgovAbstractDAO{

	public List getCompany1(Map<String, Object> commandMap) throws Exception{
		return list("selectCompanyDAO.getCompany1", commandMap);
	}
	
	public List getCompany2(Map<String, Object> commandMap) throws Exception{
		return list("selectCompanyDAO.getCompany2", commandMap);
	}
	
	public List getCompany3(Map<String, Object> commandMap) throws Exception{
		return list("selectCompanyDAO.getCompany3", commandMap);
	}
	
}
