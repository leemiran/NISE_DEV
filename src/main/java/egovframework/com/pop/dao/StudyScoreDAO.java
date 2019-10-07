package egovframework.com.pop.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyScoreDAO")
public class StudyScoreDAO extends EgovAbstractDAO{

	public Map selectEduScore(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyScoreDAO.selectEduScore", commandMap);
	}
	
	public List selectExamData(Map<String, Object> commandMap) throws Exception{
		return list("studyScoreDAO.selectExamData", commandMap);
	}
	
	public Map selectProjData(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyScoreDAO.selectProjData", commandMap);
	}
	
	public Map selectSulData(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyScoreDAO.selectSulData", commandMap);
	}
}
