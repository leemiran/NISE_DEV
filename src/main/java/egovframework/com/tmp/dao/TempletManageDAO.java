package egovframework.com.tmp.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("templetManageDAO")
public class TempletManageDAO extends EgovAbstractDAO{

	public String getGrcodeType(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("templetManageDAO.getGrcodeType", commandMap);
	}
}
