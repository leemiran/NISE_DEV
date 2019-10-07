package egovframework.adm.hom.trs.service;

import java.util.List;
import java.util.Map;

public interface TrainingService {
	
	
	public int selectTrainingListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 연간연수 일정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectTrainingList(Map<String, Object> commandMap) throws Exception;
	
	//연간연수 일정 등록
	public boolean insertTraining(Map<String, Object> commandMap, List<?> fileList) throws Exception;
	
	//사용여부 N
	public int updateTrainingUseN(Map<String, Object> commandMap) throws Exception;
	
	//보기
	public Map<?, ?> selectTrainingView(Map<String, Object> commandMap) throws Exception ;

	//파일
	public List selectTrainingFileList(Map<String, Object> commandMap) throws Exception;
	
	public boolean updateTraining(Map<String, Object> commandMap, List<?> fileList) throws Exception;
	
	public boolean deleteTraining(Map<String, Object> commandMap) throws Exception;
	
	public int selectTrainingScheduleListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	List<?> selectTrainingScheduleList(Map<String, Object> commandMap) throws Exception;
	
	public Map<?, ?> selectTrainingScheduleView(Map<String, Object> commandMap) throws Exception ;
	
	public boolean insertTrainingSchedule(Map<String, Object> commandMap) throws Exception;
	
	public boolean updateTrainingSchedule(Map<String, Object> commandMap) throws Exception;
	
	public boolean deleteTrainingSchedule(Map<String, Object> commandMap) throws Exception;
	
	public boolean updateTrainingScheduleOrderNum(Map<String, Object> commandMap) throws Exception;
	
	public int selectTrainingCourseListTotCnt(Map<String, Object> commandMap) throws Exception;

	List<?> selectTrainingCourseList(Map<String, Object> commandMap) throws Exception;
	
	public Map<?, ?> selectTrainingCourseView(Map<String, Object> commandMap) throws Exception ;
	
	public boolean insertTrainingCourse(Map<String, Object> commandMap) throws Exception;
	
	public boolean updateTrainingCourse(Map<String, Object> commandMap) throws Exception;
	
	public boolean deleteTrainingCourse(Map<String, Object> commandMap) throws Exception;
	
	public boolean updateTrainingCourseOrderNum(Map<String, Object> commandMap) throws Exception;
	
}
