package egovframework.adm.cmg.scm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyCommentManageDAO")
public class StudyCommentManageDAO extends EgovAbstractDAO{

	public List selectCommentDateList(Map<String, Object> commandMap) throws Exception{
		return list("studyCommentManageDAO.selectCommentDateList", commandMap);
	}
	
	public List selectCommentList(Map<String, Object> commandMap) throws Exception{
		return list("studyCommentManageDAO.selectCommentList", commandMap);
	}
	
	public List selectCommentSubList(Map<String, Object> commandMap) throws Exception{
		return list("studyCommentManageDAO.selectCommentSubList", commandMap);
	}
	
	public int updateCommentDateList(Map<String, Object> commandMap) throws Exception{
		return update("studyCommentManageDAO.updateCommentDateList", commandMap);
	}
}
