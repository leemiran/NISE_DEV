package egovframework.com.pop.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.stu.dao.StuMemberDAO;
import egovframework.com.pop.dao.StudyScoreDAO;
import egovframework.com.pop.service.StudyScoreService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("studyScoreService")
public class StudyScoreServiceImpl extends EgovAbstractServiceImpl implements StudyScoreService{
	
	@Resource(name="studyScoreDAO")
    private StudyScoreDAO studyScoreDAO;

	@Resource(name="stuMemberDAO")
    private StuMemberDAO stuMemberDAO;
	
	public Map selectEduScore(Map<String, Object> commandMap) throws Exception{
		
		//학습자 참여도 점수 넣어주기
		 stuMemberDAO.updateUserAttendanceStudentScore(commandMap);
		 
		return studyScoreDAO.selectEduScore(commandMap);
	}
	
	public List selectExamData(Map<String, Object> commandMap) throws Exception{
		return studyScoreDAO.selectExamData(commandMap);
	}
	
	public Map selectProjData(Map<String, Object> commandMap) throws Exception{
		return studyScoreDAO.selectProjData(commandMap);
	}
	
	public Map selectSulData(Map<String, Object> commandMap) throws Exception{
		return studyScoreDAO.selectSulData(commandMap);
	}
}
