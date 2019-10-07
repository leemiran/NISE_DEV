package egovframework.adm.hom.trs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.adm.hom.trs.dao.TrainingDAO;
import egovframework.adm.hom.trs.service.TrainingService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import org.apache.log4j.Logger;

@Service("trainingService")
public class TrainingServiceImpl extends EgovAbstractServiceImpl implements TrainingService{
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="trainingDAO")
    private TrainingDAO trainingDAO;
	
	
	
	public int selectTrainingListTotCnt(Map<String, Object> commandMap) throws Exception{
		return trainingDAO.selectTrainingListTotCnt(commandMap);
	}
	
	/**
	 * 연간연수 일정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTrainingList(Map<String, Object> commandMap) throws Exception {
		return trainingDAO.selectTrainingList(commandMap);
	}
	
	//연간연수 일정 등록
	public boolean insertTraining(Map<String, Object> commandMap, List<?> fileList) throws Exception{
		
		boolean isok = false;
		
		//일련번호
		int p_seq = trainingDAO.selectTrainingSeq(commandMap);		
		commandMap.put("p_seq", p_seq);		
		
		try {
			Object o = trainingDAO.insertTraining(commandMap);
			
			//파일인서트
			if(fileList != null)
			{
				for(int i=0; i<fileList.size(); i++)
				{
					HashMap fileh = (HashMap)fileList.get(i);
					commandMap.putAll(fileh);
					
					o = trainingDAO.insertTrainingFile(commandMap);
					
					logger.info(this.getClass().getName() + " 교과목 및 시간 배당  파일 인서트 seq : " + o);
				}
			}
			isok = true;
						
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	//사용여부 N
	public int updateTrainingUseN(Map<String, Object> commandMap) throws Exception{
		return trainingDAO.updateTrainingUseN(commandMap);
	}
	
	//보기
	public Map<?, ?> selectTrainingView(Map<String, Object> commandMap) throws Exception {
		return trainingDAO.selectTrainingView(commandMap);
	}
	
	//파일
	public List selectTrainingFileList(Map<String, Object> commandMap) throws Exception{
		return trainingDAO.selectTrainingFileList(commandMap);
	}
	
	public boolean updateTraining(Map<String, Object> commandMap, List<?> fileList) throws Exception{
		boolean isok = false;
		
		String [] _Array_p_fileseq = (String []) commandMap.get("_Array_p_fileseq");
		String p_tabseq = (String) commandMap.get("p_tabseq");
		String p_seq = (String) commandMap.get("p_seq");
		
		
		try {
//			게시글수정
			trainingDAO.updateTraining(commandMap);
			
			//파일인서트
			if(fileList.size() > 0)
			{
				//파일 삭제
				trainingDAO.deleteTrainingFile(commandMap);
				
				for(int i=0; i<fileList.size(); i++)
				{
					HashMap fileh = (HashMap)fileList.get(i);
					commandMap.putAll(fileh);
					
					Object o = trainingDAO.insertTrainingFile(commandMap);
					
					logger.info(this.getClass().getName() + " 교과목 및 시간 배당  파일 인서트 seq : " + o);
				}
			}
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	public boolean deleteTraining(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try {
			//연간연수
			trainingDAO.deleteTraining(commandMap);
			
			//파일삭제
			trainingDAO.deleteTrainingFile(commandMap);
			
			//연간연수 세부일정 삭제
			trainingDAO.deleteTrainingSchedule(commandMap);
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}
	
	public int selectTrainingScheduleListTotCnt(Map<String, Object> commandMap) throws Exception{
		return trainingDAO.selectTrainingScheduleListTotCnt(commandMap);
	}
	
	public List<?> selectTrainingScheduleList(Map<String, Object> commandMap) throws Exception {
		return trainingDAO.selectTrainingScheduleList(commandMap);
	}

	public Map<?, ?> selectTrainingScheduleView(Map<String, Object> commandMap) throws Exception {
		return trainingDAO.selectTrainingScheduleView(commandMap);
	}
	
	//연간연수 일정 등록
	public boolean insertTrainingSchedule(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;		
		
		try {
			Object o = trainingDAO.insertTrainingSchedule(commandMap);				
			isok = true;
						
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	public boolean updateTrainingSchedule(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		
		try {
			Object o = trainingDAO.updateTrainingSchedule(commandMap);
			isok = true;
						
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}

	public boolean deleteTrainingSchedule(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try {
			//연간연수 세부일정 삭제
			trainingDAO.deleteTrainingSchedule(commandMap);
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}
	
	public boolean updateTrainingScheduleOrderNum(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		
		try {
			//순서
			String []  v_param = (String []) commandMap.get("_Array_p_params");
			
			if(v_param != null)
			{
				for ( int i = 0 ; i < v_param.length; i++ ) {
					System.out.println("v_param ---> "+v_param[i]);					
					commandMap.put("p_iseq", v_param[i]);
					commandMap.put("p_order_num", i+1);
					Object o = trainingDAO.updateTrainingScheduleOrderNum(commandMap);					
				}
			}			
			
			isok = true;
						
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	public int selectTrainingCourseListTotCnt(Map<String, Object> commandMap) throws Exception{
		return trainingDAO.selectTrainingCourseListTotCnt(commandMap);
	}
	
	public List<?> selectTrainingCourseList(Map<String, Object> commandMap) throws Exception {
		return trainingDAO.selectTrainingCourseList(commandMap);
	}
	
	public Map<?, ?> selectTrainingCourseView(Map<String, Object> commandMap) throws Exception {
		return trainingDAO.selectTrainingCourseView(commandMap);
	}
	
	//연간연수 일정 등록
	public boolean insertTrainingCourse(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;		
		
		try {
			Object o = trainingDAO.insertTrainingCourse(commandMap);				
			isok = true;
						
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	public boolean updateTrainingCourse(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		
		try {
			Object o = trainingDAO.updateTrainingCourse(commandMap);
			isok = true;
						
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}

	public boolean deleteTrainingCourse(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try {
			//연간연수 세부일정 삭제
			trainingDAO.deleteTrainingCourse(commandMap);
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}
	
	public boolean updateTrainingCourseOrderNum(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		
		try {
			//순서
			String []  v_param = (String []) commandMap.get("_Array_p_params");
			
			if(v_param != null)
			{
				for ( int i = 0 ; i < v_param.length; i++ ) {
					System.out.println("v_param ---> "+v_param[i]);					
					commandMap.put("p_iseq", v_param[i]);
					commandMap.put("p_order_num", i+1);
					Object o = trainingDAO.updateTrainingCourseOrderNum(commandMap);					
				}
			}			
			
			isok = true;
						
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
}
