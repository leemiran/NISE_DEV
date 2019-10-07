package egovframework.adm.hom.trs.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("trainingDAO")
public class TrainingDAO extends EgovAbstractDAO {

	public int selectTrainingListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("trainingDAO.selectTrainingListTotCnt", commandMap);
	}
	
	/**
	 * 설문 문제 관리 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTrainingList(Map<String, Object> commandMap) throws Exception{
		return list("trainingDAO.selectTrainingList", commandMap);
	}
	
	
	//연간연수 일정 등록
	public Object insertTraining(Map<String, Object> commandMap) throws Exception{
		return insert("trainingDAO.insertTraining", commandMap);
	}
	
	
	//교과목 및 시간 배당  파일
	public Object insertTrainingFile(Map<String, Object> commandMap) throws Exception{
		return insert("trainingDAO.insertTrainingFile", commandMap);
	}
	
	//사용여부 N
	public int updateTrainingUseN(Map<String, Object> commandMap) throws Exception{
		return update("trainingDAO.updateTrainingUseN", commandMap);
	}
	
	public int selectTrainingSeq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("trainingDAO.selectTrainingSeq", commandMap);
	}
	
	//보기
	public Map<?, ?> selectTrainingView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("trainingDAO.selectTrainingView", commandMap);
	}
		
	//파일
	public List selectTrainingFileList(Map<String, Object> commandMap) throws Exception{
		return list("trainingDAO.selectTrainingFileList", commandMap);
	}
	
	public int updateTraining(Map<String, Object> commandMap) throws Exception{
		return update("trainingDAO.updateTraining", commandMap);
	}
	
	public int deleteTrainingFile(Map<String, Object> commandMap) throws Exception{
		return delete("trainingDAO.deleteTrainingFile", commandMap);
	}
	
	public int deleteTraining(Map<String, Object> commandMap) throws Exception{
		return delete("trainingDAO.deleteTraining", commandMap);
	}
	
	public int deleteTrainingSchedule(Map<String, Object> commandMap) throws Exception{
		return delete("trainingDAO.deleteTrainingSchedule", commandMap);
	}
	
	public int selectTrainingScheduleListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("trainingDAO.selectTrainingScheduleListTotCnt", commandMap);
	}
	
	public List<?> selectTrainingScheduleList(Map<String, Object> commandMap) throws Exception{
		return list("trainingDAO.selectTrainingScheduleList", commandMap);
	}
	
	public Map<?, ?> selectTrainingScheduleView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("trainingDAO.selectTrainingScheduleView", commandMap);
	}
	
	public Object insertTrainingSchedule(Map<String, Object> commandMap) throws Exception{
		return insert("trainingDAO.insertTrainingSchedule", commandMap);
	}

	public int updateTrainingSchedule(Map<String, Object> commandMap) throws Exception{
		return update("trainingDAO.updateTrainingSchedule", commandMap);
	}
	
	public int updateTrainingScheduleOrderNum(Map<String, Object> commandMap) throws Exception{
		return update("trainingDAO.updateTrainingScheduleOrderNum", commandMap);
	}
	
	public int selectTrainingCourseListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("trainingDAO.selectTrainingCourseListTotCnt", commandMap);
	}
	
	public List<?> selectTrainingCourseList(Map<String, Object> commandMap) throws Exception{
		return list("trainingDAO.selectTrainingCourseList", commandMap);
	}
	
	public Map<?, ?> selectTrainingCourseView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("trainingDAO.selectTrainingCourseView", commandMap);
	}

	public Object insertTrainingCourse(Map<String, Object> commandMap) throws Exception{
		return insert("trainingDAO.insertTrainingCourse", commandMap);
	}
	
	public int updateTrainingCourse(Map<String, Object> commandMap) throws Exception{
		return update("trainingDAO.updateTrainingCourse", commandMap);
	}
	
	public int deleteTrainingCourse(Map<String, Object> commandMap) throws Exception{
		return delete("trainingDAO.deleteTrainingCourse", commandMap);
	}
	
	public int updateTrainingCourseOrderNum(Map<String, Object> commandMap) throws Exception{
		return update("trainingDAO.updateTrainingCourseOrderNum", commandMap);
	}
	
}
