package egovframework.usr.stu.std.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ziaan.library.SQLString;

import egovframework.adm.stu.dao.StuMemberDAO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usr.stu.cou.dao.CourseStudyDAO;
import egovframework.usr.stu.std.dao.StudySurveyDAO;
import egovframework.usr.stu.std.service.StudySurveyService;

@Service("studySurveyService")
public class StudySurveyServiceImpl extends EgovAbstractServiceImpl implements StudySurveyService{
	
	@Resource(name="studySurveyDAO")
    private StudySurveyDAO studySurveyDAO;

	@Resource(name="courseStudyDAO")
    private CourseStudyDAO courseStudyDAO;
	
	@Resource(name="stuMemberDAO")
    private StuMemberDAO stuMemberDAO;
	
	
	
	
	public List selectEducationSubjectList(Map<String, Object> commandMap) throws Exception{
		return studySurveyDAO.selectEducationSubjectList(commandMap);
	}
	
	public List selectUserList(Map<String, Object> commandMap) throws Exception{
		return studySurveyDAO.selectUserList(commandMap);
	}
	
	public List selectPaperQuestionExampleList(Map<String, Object> commandMap) throws Exception{
		
		List list = new ArrayList();
		//try{
			int p_sulpapernum = Integer.valueOf(commandMap.get("p_sulpapernum").toString());
			if( p_sulpapernum == 0 ){
				// 교육후기 입력을 위한 과정설문 여부 확인
            	String v_isSubjSul = studySurveyDAO.getIsSubjSul(commandMap);
            	commandMap.put("p_isSubjSul", v_isSubjSul);
            	
            	p_sulpapernum = studySurveyDAO.getPapernumSeq(commandMap)-1;
                commandMap.put("p_sulpapernum", String.valueOf(p_sulpapernum));
			}
			String sulnum = studySurveyDAO.getSulNums(commandMap);
			String[] sulnums = sulnum.split(",");
			String[] newSulnums = new String[sulnums.length];
			for( int i=0; i<sulnums.length; i++ ){
				newSulnums[i] = sulnums[i]+"!_"+i;
			}
			commandMap.put("sulnums", newSulnums);
			list = studySurveyDAO.selectPaperQuestionExampleList(commandMap);
		//}catch(Exception ex){
		//	ex.printStackTrace();
		//}
		return list;
	}
	
	public int insertSulmunUserResult(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			String sulnum = studySurveyDAO.getSulNums(commandMap);
			String[] sulnums = sulnum.split(",");
			String[] newSulnums = new String[sulnums.length];
			String p_distcode10 = "";
			String p_answer = "";
			for( int i=0; i<sulnums.length; i++ ){
				String v_answer = (String)commandMap.get(sulnums[i]);
				
				if(v_answer != null)
					p_answer += v_answer.replace(",", " ");
				else
					//p_answer += v_answer;
					p_answer += 5;
				
				
				newSulnums[i] = sulnums[i] + "!_" + v_answer;
				if( i<(sulnums.length-1) ) p_answer += ",";
				
				if( commandMap.get(sulnums[i]+"|10") != null ){
					String v_distcode10 = (String)commandMap.get(sulnums[i]+"|10");
					System.out.println("v_distcode10 ----> "+v_distcode10);
					if(v_distcode10 != null)
						p_distcode10 += v_distcode10.replace(",", " ");
					else
						//p_distcode10 += v_distcode10;
						p_distcode10 += "";
					
				}
			}
			
			commandMap.put("sulnums", newSulnums);
			commandMap.put("p_sulnum", sulnum);
			commandMap.put("p_answer", p_answer);
			commandMap.put("p_distcode10", p_distcode10);
			
			studySurveyDAO.insertSulmunUserResult(commandMap);
			courseStudyDAO.whenSubjCommentsInsert(commandMap);
			
			
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
