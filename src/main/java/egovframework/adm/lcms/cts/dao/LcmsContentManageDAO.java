package egovframework.adm.lcms.cts.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("lcmsContentManageDAO")
public class LcmsContentManageDAO extends EgovAbstractDAO{

	
	 public String selectContentPath( Map<String, Object> commandMap) throws Exception{
        return (String)getSqlMapClientTemplate().queryForObject("lcmsContentManageDAO.selectContentPath", commandMap);
    }
}
