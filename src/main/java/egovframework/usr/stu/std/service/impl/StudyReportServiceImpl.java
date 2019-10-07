package egovframework.usr.stu.std.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.stu.dao.StuMemberDAO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usr.stu.std.dao.StudyReportDAO;
import egovframework.usr.stu.std.service.StudyReportService;

@Service("studyReportService")
public class StudyReportServiceImpl extends EgovAbstractServiceImpl implements StudyReportService{
	
	@Resource(name="studyReportDAO")
    private StudyReportDAO studyReportDAO;

	@Resource(name="stuMemberDAO")
    private StuMemberDAO stuMemberDAO;
	
	
	public Map selectViewOrderStu(Map<String, Object> commandMap) throws Exception{
		return studyReportDAO.selectViewOrderStu(commandMap);
	}
	
	public Map selectProfData(Map<String, Object> commandMap) throws Exception{
		return studyReportDAO.selectProfData(commandMap);
	}
	
	public List selectProfFiles(Map<String, Object> commandMap) throws Exception{
		return studyReportDAO.selectProfFiles(commandMap);
	}
	
	public int insertProfData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			
			List multiFiles = (ArrayList)commandMap.get("multiFiles");
			if( multiFiles != null && multiFiles.size() > 0 ){
				for( int i=0; i<multiFiles.size(); i++ ){
					commandMap.putAll((Map)multiFiles.get(i));
				}
			}
			
			int score = studyReportDAO.selectProfScore(commandMap);
			commandMap.put("submitscore", score);
			int ok = studyReportDAO.updateProf(commandMap);
			if( ok < 1 ){
				studyReportDAO.insertProf(commandMap);
			}
			
			HashMap<String, Object> mm = new HashMap<String, Object>();
			 mm.put("p_subj", commandMap.get("p_subj"));
			 mm.put("p_year", commandMap.get("p_year"));
			 mm.put("p_subjseq", commandMap.get("p_subjseq"));
			 mm.put("p_userid", commandMap.get("userid"));
	        
	      //등록여부 체크
			 int cnt = stuMemberDAO.selectAttendCount(mm);
			 if( cnt == 0 ) stuMemberDAO.insertUserAttendance(mm);
			 //학습자 참여도 점수 넣어주기
			 stuMemberDAO.updateUserAttendanceStudentScore(mm);
			 
			 
			 
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
}
