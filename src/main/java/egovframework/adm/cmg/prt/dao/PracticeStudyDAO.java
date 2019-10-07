package egovframework.adm.cmg.prt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("practiceStudyDAO")
public class PracticeStudyDAO extends EgovAbstractDAO{

	public List practiceStudyList(Map<String, Object> commandMap) throws Exception{
		return list("practiceStudyDAO.practiceStudyList", commandMap);
	}
	
	public List practiceStudySubList(Map<String, Object> commandMap) throws Exception{
		return list("practiceStudyDAO.practiceStudySubList", commandMap);
	}
	
	public List subjmanPracticeStudyList(Map<String, Object> commandMap) throws Exception{
		return list("practiceStudyDAO.subjmanPracticeStudyList", commandMap);
	}
}
