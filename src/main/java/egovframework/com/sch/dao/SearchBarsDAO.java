package egovframework.com.sch.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("searchBarsDAO")
public class SearchBarsDAO extends EgovAbstractDAO{

	public List selectSearchYearList(Map<String, Object> commandMap) throws Exception{
		return list("searchBarsDAO.selectSearchYearList", commandMap);
	}
}
