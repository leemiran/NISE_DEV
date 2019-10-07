package egovframework.adm.fin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("finishInputDAO")
public class FinishInputDAO extends EgovAbstractDAO{

	public int insertExcelData(Map<String, Object> commandMap) throws Exception{
		return update("finishInputDAO.insertExcelData", commandMap);
	}
}
