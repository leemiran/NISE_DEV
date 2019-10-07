package egovframework.adm.cmg.sab.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cmg.qna.dao.CourseQnaDAO;
import egovframework.adm.cmg.sab.dao.StudyAdminBoardDAO;
import egovframework.adm.cmg.sab.service.StudyAdminBoardService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("studyAdminBoardService")
public class StudyAdminBoardServiceImpl extends EgovAbstractServiceImpl implements StudyAdminBoardService{
	
	@Resource(name="studyAdminBoardDAO")
    private StudyAdminBoardDAO studyAdminBoardDAO;

	public List selectAdminList(Map<String, Object> commandMap) throws Exception{
		return studyAdminBoardDAO.selectAdminList(commandMap);
	}
}
