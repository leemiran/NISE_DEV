package egovframework.adm.cmg.prt.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cmg.prt.dao.PracticeStudyDAO;
import egovframework.adm.cmg.prt.service.PracticeStudyService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("practiceStudyService")
public class PracticeStudyServiceImpl extends EgovAbstractServiceImpl implements PracticeStudyService{
	
	@Resource(name="practiceStudyDAO")
    private PracticeStudyDAO practiceStudyDAO;

	public List practiceStudyList(Map<String, Object> commandMap) throws Exception{
		return practiceStudyDAO.practiceStudyList(commandMap);
	}
	
	public List practiceStudySubList(Map<String, Object> commandMap) throws Exception{
		return practiceStudyDAO.practiceStudySubList(commandMap);
	}
	
	public List subjmanPracticeStudyList(Map<String, Object> commandMap) throws Exception{
		return practiceStudyDAO.subjmanPracticeStudyList(commandMap);
	}
}
