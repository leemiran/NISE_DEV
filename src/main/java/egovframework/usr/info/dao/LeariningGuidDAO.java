package egovframework.usr.info.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("leariningGuidDAO")
public class LeariningGuidDAO extends EgovAbstractDAO{
	
	public List<?> selectEduTrainingScheduleList(Map<String, Object> commandMap) throws Exception{
		return list("leariningGuidDAO.selectEduTrainingScheduleList", commandMap);
	}
	
	public List selectEduTrainingFileList(Map<String, Object> commandMap) throws Exception{
		return list("leariningGuidDAO.selectEduTrainingFileList", commandMap);
	}
	
	public List<?> selectEduTrainingCourseList(Map<String, Object> commandMap) throws Exception{
		return list("leariningGuidDAO.selectEduTrainingCourseList", commandMap);
	}
	
	public Map<?, ?> selectEduTrainingView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("leariningGuidDAO.selectEduTrainingView", commandMap);
	}
	
}
