package egovframework.adm.cmg.sab.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyAdminBoardDAO")
public class StudyAdminBoardDAO extends EgovAbstractDAO{

	public List selectAdminList(Map<String, Object> commandMap) throws Exception{
		return list("studyAdminBoardDAO.selectAdminList", commandMap);
	}
}
