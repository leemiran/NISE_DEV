package egovframework.com.sch.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.sch.dao.SearchBarsDAO;
import egovframework.com.sch.service.SearchBarsService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("searchBarsService")
public class SearchBarsServiceImpl extends EgovAbstractServiceImpl implements SearchBarsService{
	
	@Resource(name="searchBarsDAO")
    private SearchBarsDAO searchBarsDAO;

	public List selectSearchYearList(Map<String, Object> commandMap) throws Exception{
		return searchBarsDAO.selectSearchYearList(commandMap);
	}
	
}
