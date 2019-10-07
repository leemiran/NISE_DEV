package egovframework.usr.stu.std.service.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ziaan.exam.ExamBean;
import com.ziaan.library.AlertManager;
import com.ziaan.library.CalcUtil;
import com.ziaan.library.DataBox;
import com.ziaan.library.EduEtc1Bean;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.StringManager;
import com.ziaan.research.SulmunSubjUserBean;

import egovframework.adm.fin.dao.FinishManageDAO;
import egovframework.adm.fin.service.FinishManageService;
import egovframework.adm.stu.dao.StuMemberDAO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usr.stu.std.dao.StudyExamDAO;
import egovframework.usr.stu.std.service.StudyExamService;
import org.apache.log4j.Logger;

@Service("studyExamService")
public class StudyExamServiceImpl extends EgovAbstractServiceImpl implements StudyExamService{
	
	@Resource(name="studyExamDAO")
    private StudyExamDAO studyExamDAO;
	
	@Resource(name="finishManageDAO")
	private FinishManageDAO finishManageDAO;
	
	@Resource(name="stuMemberDAO")
    private StuMemberDAO stuMemberDAO;
	
	
	public final static String SPLIT_COMMA      = ",";
    public final static String SPLIT_COLON      = ":";
    
    public static int EXAM     =  2;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
	public int getExamResultChk(Map<String, Object> commandMap) throws Exception{
		return studyExamDAO.getExamResultChk(commandMap);
	}
	

	public List selectPaperQuestionExampleList(Map<String, Object> commandMap) throws Exception{
		
		//문제문행 변경		
		String examBankchangeYn = commandMap.get("examBankchangeYn") !=null ? commandMap.get("examBankchangeYn").toString() : "N";
				
		
		List QuestionExampleDataList = null;
		int exam = studyExamDAO.getExamResultChk(commandMap);
		Vector v_examnums = null;
		
		//문제은행 변경후
		if("Y".equals(examBankchangeYn)){
			QuestionExampleDataList = studyExamDAO.selectPaperQuestionExampleBankList(commandMap);
		}

		
		if( exam == 0 ){			
			//문제은행 변경전
			if("N".equals(examBankchangeYn)){				
				v_examnums = this.getExamnums(commandMap);
			}
			
		}else{
			
			if("N".equals(examBankchangeYn)){				

				Map exam_map = studyExamDAO.getExamResult(commandMap);		//이전 평가 문제 가져오기
				String v_ended = exam_map.get("ended") == null ? "" : (String)exam_map.get("ended");
				String v_examnum = (String)exam_map.get("exam");
				int v_extra_time = Integer.valueOf(exam_map.get("extraTime").toString());
				int v_userretry = Integer.valueOf(exam_map.get("userretry").toString());
				if(v_ended.equals("") && v_extra_time > 0) { // 제출시간이 없으면 제출하지 않았으므로 재응시가 아니다. 그러면 같은문제를 봐야한다. 
	        		Vector vc = new Vector();
	        		String[] arr = v_examnum.split(",");
	        		for( int i=0; i<arr.length; i++ ){
	        			if( !arr[i].equals("") ){
	        				vc.add(arr[i]);
	        			}
	        		}
	        		v_examnums = vc;
	        	} else {
	        		if( v_userretry > 0 ){
	        			v_examnums = this.getExamnums(commandMap);
	        			String strexamnum = "";
	            		Enumeration em = v_examnums.elements();
	            		while(em.hasMoreElements()){
	            			strexamnum  += (String)em.nextElement() + ",";
	            		}
	            		commandMap.put("strexamnum", strexamnum);
	            		
	            		studyExamDAO.updateExam(commandMap);
	        		}
	        	}
			}else{
				String v_exam = "";
				String tmp = "";
				for(int i = 0;  i < QuestionExampleDataList.size(); i++ ){
			        Map m2 = (Map)QuestionExampleDataList.get(i);
			        if( !tmp.equals(m2.get("examnum").toString()) ){
			        	v_exam += m2.get("examnum")+",";
			        	tmp = m2.get("examnum").toString();
			        }
				}
				commandMap.put("strexamnum", v_exam);				
        		studyExamDAO.updateExamResultExam(commandMap);
			}
			
		}
		
		
		
		if("N".equals(examBankchangeYn)){		
			if(v_examnums != null) { 
				
				String[] p_examnums = new String[v_examnums.size()];
		        for ( int i = 0; i < v_examnums.size(); i++ ) { 
		        	p_examnums[i] = i+"!_"+(String)v_examnums.get(i);
		        }
				commandMap.put("p_examnums", p_examnums);
	            // 평가번호에 해당하는 문제리스트를 만든다. 리스트((설문번호1, 보기1,2,3..))
				QuestionExampleDataList = studyExamDAO.selectPaperQuestionExampleList(commandMap);	            
	        }
		}
		
		

		if ( QuestionExampleDataList == null ) { 
		    QuestionExampleDataList = new ArrayList();
		}
		
		
		return QuestionExampleDataList;
	}
	
	public Map getPaperData(Map<String, Object> commandMap) throws Exception{
		return studyExamDAO.getPaperData(commandMap);
	}
	
	public Map selectStartTime(Map<String, Object> commandMap) throws Exception{
		return studyExamDAO.selectStartTime(commandMap);
	}
	
	
	public int insertExamAnswerFinish(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			String v_isonoff   = (String)commandMap.get("p_isonoff"); //과정분류 독서통신 : RC 는 설문을 하지 않는다. 
			String v_examtype  = (String)commandMap.get("p_examtype");
//			commandMap.put("p_year", (String)commandMap.get("p_gyear"));	// 설문페이지에 넘겨주기 위한 year 셋팅
			
			if("E".equals(v_examtype) && !"RC".equals(v_isonoff)) {	//온라인평가일 때, 독서통신이 아닐때 만  설문으로 넘어가도록 한다. 
				
				
				
				// p_num 파라미터없음 시험 완료 후 설문 진행안됨 2012.04.20
				
				
//				SulmunSubjUserBean sulbean = new SulmunSubjUserBean();
//				ArrayList list = sulbean.SelectUserSubjSulnum(box);
//				
//				int v_sulpapernum 			= 0;
//				String v_grcode 			= "";
//				String v_subj				= "";
//				String v_sulpapernm 		= "";
//				String v_sulnums 			= "";
//				if( box.getInt("p_num") > 0 ){
//					for(int i=0; i < list.size(); i++){
//						DataBox dbox = (DataBox)list.get(i);
//						v_sulpapernum = dbox.getInt("d_sulpapernum");
//						v_grcode 	  = dbox.getString("d_grcode");		//실제그룹코드
//						v_subj		  = dbox.getString("d_subj");
//						v_sulpapernm = dbox.getString("d_sulpapernm");
//						v_sulnums = dbox.getString("d_sulnums");
//						// SULPAPERNM SULTYPE SUBJNM GRCODE YEAR SUBJSEQ SUBJ STDT ENDT
//					}
//			        
//					box.put("p_sulpapernum", v_sulpapernum);		// 설문 문제지 보여주기 위한 설문번호 셋팅
//					box.put("p_sulpapernm", v_sulpapernm);
//					box.put("p_grcode", "ALL");						// 과정설문을 보여주는 거라서 ALL의 값이  GRCODE에 들어간다
//					box.put("s_grcode", v_grcode);					// tz_suleach 들어갈 교육그룹코드 
//					box.put("p_subj", "ALL");						// 과정설문을 보여주는 거라서 ALL의 값이  SUBJ에 들어간다
//					box.put("s_subj", v_subj);						// tz_suleach 들어갈 과정코드
//					box.put("p_sulnums", v_sulnums);				// 설문번호도 넘겨줘야한다.
//					box.put("p_isSubjSul", "true");				    // 교육후기 여부
//					box.put("p_process", "SulmunUserPaperListPage");
//					v_url  = "/servlet/controller.research.SulmunSubjUserServlet";
//					v_msg  = "제출되었습니다. 설문창으로 이동합니다. 설문을 작성해주세요.";
//				}else{
					// 독서통신일때 리포트가 있으면 리포트 창으로 간다. 
//					// 독서통신이면 리포트 여부 확인하고 리포트 창을 띄운다.
//					if(v_isonoff.equals("OFF")){
//						//오프라인과정 학습평가후에 목록으로 돌아기위해
//						AlertManager alert = new AlertManager();
//						v_url  = "/servlet/controller.exam.ExamUserServlet";
//						box.put("p_process", "selectOffStudyExam");
//						v_msg  = "제출되었습니다.";
//						alert.alertOkMessage(out, v_msg, v_url , box);
//					}else{
					
						/*
						v_url  = "/servlet/controller.exam.ExamUserServlet";
						v_msg  = "제출되었습니다. 창이 닫힙니다.";
						box.put("p_process", "ExamUserPaperClosed");
						box.put("p_end", "0");
						*/
//					}
//				}
				isOk = 1;
			}else {
//				if(v_isonoff.equals("OFF")){
//					//오프라인과정 학습평가후에 목록으로 돌아기위해
//					AlertManager alert = new AlertManager();
//					v_url  = "/servlet/controller.exam.ExamUserServlet";
//					box.put("p_process", "selectOffStudyExam");
//					v_msg  = "제출되었습니다.";
//					alert.alertOkMessage(out, v_msg, v_url , box);
//				}else{
//					v_url  = "/servlet/controller.exam.ExamUserServlet";
//					v_msg  = "제출되었습니다. 창이 닫힙니다.";
//					box.put("p_process", "ExamUserPaperClosed");
//					box.put("p_end", "0");
//				}
				
				isOk = 1;
			}
			
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		
		return isOk;
	}
	
	
	
	public int InsertResult(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		Vector v_result = new Vector();
		String v_ended 	   = commandMap.get("p_ended") == null || commandMap.get("p_ended").toString().equals("") ? FormatDate.getDate("yyyyMMddHHmmss") : commandMap.get("p_ended").toString();	// 평가 끝 시간
		
		String v_started   = commandMap.get("started") == null || commandMap.get("started").toString().equals("") ? FormatDate.getDate("yyyyMMddHHmmss") : commandMap.get("started").toString();		// 평가 시작 시간
		
		String v_subj      = (String)commandMap.get("p_subj");
        String v_year      = (String)commandMap.get("p_gyear");
        String v_subjseq   = (String)commandMap.get("p_subjseq");

        String v_lesson    = (String)commandMap.get("p_lesson");
        String v_examtype  = (String)commandMap.get("p_examtype");
        int    v_papernum  = Integer.valueOf(commandMap.get("p_papernum")+"");
        String v_userid    = (String)commandMap.get("p_userid");
        String v_answer    = commandMap.get("answer") == null ? "" : (String)commandMap.get("answer");
        String v_exam      = (String)commandMap.get("p_exam");
        
        
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
		 
		 
		 
        double v_time = 0;

        int    v_userretry  = Integer.valueOf(commandMap.get("p_userretry") == null ? "0" : commandMap.get("p_userretry")+"");		// 재시험 가능 횟수
        
		try{
			if ( !isCurrentStudent(commandMap).equals("Y")) return 97;
			
			
            // 평가지,평가자별 카운트j
            int v_exist = studyExamDAO.chkResultExist(commandMap);
            logger.info("평가지, 평가자별 카운트 : "+ v_exist);

            // 평가점수채점
			v_result = this.getScore(commandMap);
			logger.info("평가점수 채점결과 : "+v_result);

			int v_score     = Integer.parseInt((String)v_result.get(0)); // 점수
			
			logger.info("점수 : "+v_score);
            
			int v_answercnt = Integer.parseInt((String)v_result.get(1));
			String v_corrected = (String)v_result.get(2);

			v_time = v_ended.equals("")?0:FormatDate.getMinDifference(v_started, v_ended);
					
            StringTokenizer st2 = new StringTokenizer(v_answer, ",");
            v_answer = "";
            while ( st2.hasMoreElements() ) {                 
	            String s = StringManager.trim((String)st2.nextToken() );
		
	            if ( s.length() == 0) v_answer = v_answer + " " + ",";
	            else v_answer = v_answer + s + ",";
	        }
            if ( v_userretry != 0 && v_exist != 0 ) v_userretry = v_userretry-1;

            commandMap.put("v_score", v_score);
            commandMap.put("v_answercnt", v_answercnt);
            commandMap.put("v_time", v_time);
            commandMap.put("v_answer", v_answer);
            commandMap.put("v_corrected", v_corrected);
            commandMap.put("v_userretry", v_userretry);
            if ( v_exist == 0 ) { // 평가지,평가자별 카운트
				studyExamDAO.insertTZ_examresult(commandMap);
				//점수재계산 및 update
				this.calc_score(commandMap);
	                              

            } else { 
            	
            	// 기존점수가 높으면 업데이트 안됨.
            	int is_score = studyExamDAO.IsResultScore(commandMap);

            	// 획득점수 > 기존점수
            	if ( ( v_score > is_score ) ) { 
            		
            		studyExamDAO.updateTZ_examresult(commandMap);
    				
            		this.calc_score(commandMap);
            	} else {
            		int z = studyExamDAO.SelectEndedTime(commandMap);
            		if( z > 0){	/* && 응시완료시간이 있는지 */
						 // 재응시횟수 업데이트
						 //isOk = UpdateUserUserretry(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype, v_papernum, v_userid, v_userretry);           	  
            		}
				}
						
            }
            
            // 제출 업데이트
            if(null != commandMap.get("submitYn")) {
            	studyExamDAO.updateExamResultSubmit(commandMap);
            }
            
		}catch(Exception ex){
			isOk = -1;
			ex.printStackTrace();
		}
		return isOk;//studyExamDAO.InsertResult(commandMap);
	}
	
	public Vector getScore(Map<String, Object> commandMap) throws Exception{
		
		Vector    v_examnums    = new Vector();
		int v_examnum = 0;
        Vector    v_answers = new Vector();
        String v_answer = "";

		StringTokenizer st1   = null;
        StringTokenizer st2  = null;
        
        String p_exam = (String)commandMap.get("p_exam");
        String p_answer = commandMap.get("answer") == null ? "" : (String)commandMap.get("answer");
        int p_exampoint = Integer.valueOf( ( commandMap.get("p_exampoint") == null || commandMap.get("p_exampoint").equals("") ) ? "0" : commandMap.get("p_exampoint")+"" );
        
        
        //문제문행 변경
        String examBankchangeYn = commandMap.get("examBankchangeYn") !=null ? commandMap.get("examBankchangeYn").toString() : "N";
        String p_exam_subj = commandMap.get("p_exam_subj") !=null ? commandMap.get("p_exam_subj").toString() : "";
        String o_p_subj = commandMap.get("p_subj") !=null ? commandMap.get("p_subj").toString() : "";
        
        int    v_score   = 0;
		int    v_answercnt = 0;
		String v_iscorrect = ""; 
		int    v_temp = 0;
		
		p_answer = p_answer.equals("") ? p_exam : p_answer;

		Vector v_result = new Vector();

        try { 
            st1 = new StringTokenizer(p_exam, SPLIT_COMMA);
            while ( st1.hasMoreElements() ) { 
            	String num = StringManager.trim((String)st1.nextToken() );
                v_examnums.add(num);
                logger.info("Examnum : "+num);
            }
			
			st2 = new StringTokenizer(p_answer, SPLIT_COMMA);


            while ( st2.hasMoreElements() ) { 
				String s = StringManager.trim((String)st2.nextToken() );// System.out.println("s :" + s + "a");
				logger.info("Answer : "+s);

                v_answers.add(s);
            }
            
            //문제문행 변경
            if("Y".equals(examBankchangeYn)){
            	commandMap.put("p_subj", p_exam_subj);
			}
            
			for ( int i =0; i < v_examnums.size() ; i++ ) { 

                   v_examnum = Integer.parseInt((String)v_examnums.get(i));
				   v_answer =  v_answers != null && v_answers.size() > 0 ? (String)v_answers.get(i) : "0";
				   
				   commandMap.put("v_examnum", v_examnum);
				   commandMap.put("v_answer", v_answer);
				   
				   
				   commandMap.put("v_examtype", studyExamDAO.getExamType(commandMap));

                   v_temp = studyExamDAO.MakeExamResult(commandMap);

				   v_score += (v_temp * p_exampoint);
				   v_answercnt += v_temp;
				   if ( i == v_examnums.size()-1) { 
					   v_iscorrect += String.valueOf(v_temp);
				   } else { 
				       v_iscorrect += String.valueOf(v_temp) + ",";
				   }

			}
			
			//문제문행 변경
            if("Y".equals(examBankchangeYn)){
            	commandMap.put("p_subj", o_p_subj);
			}
            
		    v_result.add(String.valueOf(v_score));
			v_result.add(String.valueOf(v_answercnt));
			v_result.add(v_iscorrect);

        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        return v_result;
	}
	
	public String isCurrentStudent(Map<String, Object> commandMap) throws Exception{
		String result = "Y";
		String p_year = (String)commandMap.get("p_year");
		String p_subjseq = (String)commandMap.get("p_subjseq");
		if ( p_year.equals("PREV") || p_subjseq.equals("PREV") ) { 
            return "Y";
        }
		
		int v_sysdate   = Integer.parseInt(FormatDate.getDate("yyyyMMddhh") );
		
		int check = studyExamDAO.selectStudentCount(commandMap);
		if ( check > 0 ) { //기존에는 교육기간 체크였지만 시험기간으로 대체
			int cnt = studyExamDAO.checkPaper(commandMap);
			if( cnt == 0 ) result = "N";
		}
		return result;
	}
	
	
	/**
	 * 재채점용 업데이트 쿼리
	 * @param commandMap
	 * @throws Exception
	 */
	public void updateTZ_examresultReRating(Map<String, Object> commandMap) throws Exception{
		studyExamDAO.updateTZ_examresultReRating(commandMap);
	}
	
	
	/**
	 * 점수재계산 사용자의 성정보기에서 사용됨 미적용중..
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int calc_score(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			int studyCount = finishManageDAO.selectStudyCount(commandMap);
			if( studyCount > 0 ){
				// 수료처리 완료여부, 학습중 검토
				Map subjInfo = finishManageDAO.SelectSubjseqInfoDbox(commandMap);
				if( subjInfo.get("isclosed") != null && subjInfo.get("isclosed").toString().equals("Y") ){
					return -1; //이미 수료처리 되었습니다.
				}
				
				
				commandMap.put("p_whtest", subjInfo.get("whtest"));
				commandMap.put("p_wftest", subjInfo.get("wftest")); //온라인평가
				commandMap.put("p_wmtest", subjInfo.get("wmtest")); //출석평가
				commandMap.put("p_wreport", subjInfo.get("wreport")); //과제평가
				commandMap.put("p_wetc1", subjInfo.get("wetc1"));
				commandMap.put("p_wetc2", subjInfo.get("wetc2"));  //참여도
				
				String contenttype = (String)subjInfo.get("contenttype");
				
				commandMap.put("p_userid", commandMap.get("userid"));
				
				System.out.println("===================== p_whtest "+subjInfo.get("whtest")+"");
				System.out.println("===================== p_wftest "+subjInfo.get("wftest")+"");
				System.out.println("===================== p_wmtest "+subjInfo.get("wmtest")+"");
				System.out.println("===================== p_wreport "+subjInfo.get("wreport")+"");
				System.out.println("===================== p_wetc1 "+subjInfo.get("wetc1")+"");
				System.out.println("===================== p_wetc2 "+subjInfo.get("wetc2")+"");
				System.out.println("===================== contenttype "+subjInfo.get("contenttype")+"");
				System.out.println("===================== p_userid "+commandMap.get("userid")+"");
				
				//수료대상자 리스트
	        	List student = finishManageDAO.selectCompleteStudent(commandMap);
	        	
	        	//수료조건삭제
	        	finishManageDAO.deleteUsergraduated(commandMap);
	        	
	        	//수료조건 확인
	        	String[] userGraduated = isgraduatedCheck(commandMap, student, subjInfo);
	        	commandMap.put("userGraduated", userGraduated);
	        	
	        	System.out.println("=================== userGraduated "+commandMap.get("userGraduated")+"");
	        	
	        	// 컨텐츠타입이 'L'인것은 위탁과정이므로 재산정 필요없음
	        	// 2012.03.21 컨텐츠타입에는 L코드없음. 다른 버전의 프로세스인듯
	        	// 'L'일경우 student 테이블의 graduated만 업데이트함.
	        	if( !contenttype.equals("L") ){
	        		finishManageDAO.updateCompleteStudent(commandMap);
	        	}
	        	
				
			}
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		
		return isOk;
	}
	
	
	/**
	 * 점수재계산 사용자의 성정보기에서 사용됨- 관리자모드용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int calc_score_admin(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			int studyCount = finishManageDAO.selectStudyCount(commandMap);
			if( studyCount > 0 ){
				// 수료처리 완료여부, 학습중 검토
				Map subjInfo = finishManageDAO.SelectSubjseqInfoDbox(commandMap);
				if( subjInfo.get("isclosed") != null && subjInfo.get("isclosed").toString().equals("Y") ){
					return -1; //이미 수료처리 되었습니다.
				}
				
				
				commandMap.put("p_whtest", subjInfo.get("whtest"));
				commandMap.put("p_wftest", subjInfo.get("wftest"));
				commandMap.put("p_wmtest", subjInfo.get("wmtest"));
				commandMap.put("p_wreport", subjInfo.get("wreport"));
				commandMap.put("p_wetc1", subjInfo.get("wetc1"));
				commandMap.put("p_wetc2", subjInfo.get("wetc2"));
				
				String contenttype = (String)subjInfo.get("contenttype");
				
				//수료대상자 리스트
	        	List student = finishManageDAO.selectCompleteStudent(commandMap);
	        	
	        	//수료조건삭제
	        	finishManageDAO.deleteUsergraduated(commandMap);
	        	
	        	//수료조건 확인
	        	String[] userGraduated = isgraduatedCheck(commandMap, student, subjInfo);
	        	commandMap.put("userGraduated", userGraduated);
	        	
	        	// 컨텐츠타입이 'L'인것은 위탁과정이므로 재산정 필요없음
	        	// 2012.03.21 컨텐츠타입에는 L코드없음. 다른 버전의 프로세스인듯
	        	// 'L'일경우 student 테이블의 graduated만 업데이트함.
	        	if( !contenttype.equals("L") ){
	        		finishManageDAO.updateCompleteStudent(commandMap);
	        	}
	        	
				
			}
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		
		return isOk;
	}
	
	public Vector getExamnums(Map<String, Object> commandMap) throws Exception{
		List   examlist   = new ArrayList();
		List   list    = null;
		List   list2 = null;
		Vector v_lessons = null;
        Vector v_examnums = new Vector();
        Vector v_realrkeys = null; // jkh 0228
		Vector v = new Vector();
        Vector v_realnums = null;
		Vector v_level1Obnums = null;
		Vector v_level2Obnums = null;
		Vector v_level3Obnums = null;
		Vector v_level1Subnums = null;
		Vector v_level2Subnums = null;
		Vector v_level3Subnums = null;
		Vector v_level1OXnums = null;
		Vector v_level2OXnums = null;
		Vector v_level3OXnums = null;
		
		try{
			list = this.getLevelQuestionList(commandMap);
			
			v_lessons = (Vector)list.get(0);
            v_level1Obnums = (Vector)list.get(1);
            v_level2Obnums = (Vector)list.get(2);
            v_level3Obnums = (Vector)list.get(3);
            v_level1Subnums = (Vector)list.get(4);
            v_level2Subnums = (Vector)list.get(5);
            v_level3Subnums = (Vector)list.get(6);
            v_level1OXnums = (Vector)list.get(7);
            v_level2OXnums = (Vector)list.get(8);
            v_level3OXnums = (Vector)list.get(9);

            v_realnums = new Vector();
            v_realrkeys = new Vector();
            String numandrkey = ""; 
            StringTokenizer st1= null;	

            for ( int i = 0; i < v_lessons.size() ; i++ ) { 
            	for ( int j = 1; j <= 3; j++ ) { 
            		Integer type = new Integer(j);
            		for ( int k = 1; k <= 3 ; k++ ) { 
            			Integer level = new Integer(k);
            			commandMap.put("pp_lesson", (String)v_lessons.get(i));
            			commandMap.put("pp_level", level.toString());
            			commandMap.put("pp_type", type);
            			List tmp = studyExamDAO.getQuestionList(commandMap);			//문제은행에서 문제를 가져온다.
            			for( int idx=0; idx<tmp.size(); idx++ ){
            				Map m = (Map)tmp.get(idx);
            				logger.info("KEY ::: "+m.get("rkey"));
            				v.add(m.get("rkey"));
            			}
            			Random ran = new Random();
            			
            		}
            	}
            }
            for ( int p=0; p < v.size() ; p++ ) { 							        
            	numandrkey = (String)v.get(p);
            	st1 = new StringTokenizer(numandrkey, ",");
            	v_realnums.add(st1.nextToken() );
            	v_realrkeys.add(st1.nextToken() );
            	
            }
            
            int  ss = v.size();
            
            Random ran = new Random();
            int [] num =new int [ss];
            int bun = 0;
            
            for ( int q = 0 ; q < ss ; q++ ) { 
            	bun = ran.nextInt(ss); 
            	
            	int breakint = 0;
            	int isequal = 0;
            	while ( isequal < 1) { 
            		bun = ran.nextInt(ss); 
            		for ( int a = 0 ; a < q ; a++ ) { 
            			if ( 	num[a] == bun) { 
            				isequal = 0;
            				break;
            			} else { 	isequal = 1;  }
            		}	
            		breakint++;
            		if ( breakint > 10000) { break; }
            	} 
            	num[q] = bun;
            }
            
            
            for ( int p=0; p < num.length ; p++ ) { 
            	v_examnums.add((String)v_realnums.get(num[p]));
            }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return v_examnums;
	}
	
	
	public List getLevelQuestionList(Map<String, Object> commandMap) throws Exception{
		Map m = studyExamDAO.getExamPaperData(commandMap);
		List list = new ArrayList();
		
		Vector v_lessons1 = new Vector();
        Vector v_level1Obnums = new Vector();
        Vector v_level2Obnums = new Vector();
        Vector v_level3Obnums = new Vector();
        Vector v_level1Subnums = new Vector();
        Vector v_level2Subnums = new Vector();
        Vector v_level3Subnums = new Vector();
        Vector v_level1OXnums = new Vector();
        Vector v_level2OXnums = new Vector();
        Vector v_level3OXnums = new Vector();
        
        Vector v_sulnums2 = new Vector();
        StringTokenizer st = null;
        StringTokenizer st2 = null;
        StringTokenizer st3= null;	
		try{
			
			st = new StringTokenizer((String)m.get("level1text"), "/");
			logger.info("level1 :::  "+(String)m.get("level1text"));
			while ( st.hasMoreElements() ) { 
		        
				String lst = st.nextToken();
				st2 = new StringTokenizer( lst, "|");		
				
				String lst2 = st2.nextToken();
				st3 = new StringTokenizer( lst2, ",");
				
				String ss = st3.nextToken();
				if ( ss.length() == 1) { 
				    ss = "00" + ss; 
			   } else if ( ss.length() == 2) { 
			   		ss = "0" + ss;	
			  }
				v_lessons1.add(ss);
				v_level1Obnums.add(st3.nextToken() );

                // 주관식
                lst2 = st2.nextToken();
		        st3 = new StringTokenizer( lst2, ",");
                String s = st3.nextToken();                
			    v_level1Subnums.add(st3.nextToken() );

                // OX식
                lst2 = st2.nextToken();
		        st3 = new StringTokenizer( lst2, ",");
                String sOX1 = st3.nextToken();                
			    v_level1OXnums.add(st3.nextToken() );			    
			}

			st = new StringTokenizer((String)m.get("level2text"), "/");
			logger.info("level2 :::  "+(String)m.get("level1text"));
			while ( st.hasMoreElements() ) { 
                String lst = st.nextToken();
				st2 = new StringTokenizer( lst, "|");
                  String lst2 = st2.nextToken();

			      st3 = new StringTokenizer( lst2, ",");
				  String ss = st3.nextToken();
				  v_level2Obnums.add(st3.nextToken() );

                  lst2 = st2.nextToken();
			      st3 = new StringTokenizer( lst2, ",");
                  String s = st3.nextToken();
				  v_level2Subnums.add(st3.nextToken() );

                  lst2 = st2.nextToken();
			      st3 = new StringTokenizer( lst2, ",");
                  String sOX2 = st3.nextToken();
				  v_level2OXnums.add(st3.nextToken() );
				  
				  
			}
			st = new StringTokenizer((String)m.get("level3text"), "/");
			logger.info("level3 :::  "+(String)m.get("level1text"));
			while ( st.hasMoreElements() ) { 
                String lst = st.nextToken();
				st2 = new StringTokenizer( lst, "|");
                      String lst2 = st2.nextToken();

				      st3 = new StringTokenizer( lst2, ",");
					  String ss = st3.nextToken();

					  v_level3Obnums.add(st3.nextToken() );

                      lst2 = st2.nextToken();
				      st3 = new StringTokenizer( lst2, ",");
                      String s = st3.nextToken();
					  v_level3Subnums.add(st3.nextToken() );
					  
                      lst2 = st2.nextToken();
				      st3 = new StringTokenizer( lst2, ",");
                      String sOX3 = st3.nextToken();
					  v_level3OXnums.add(st3.nextToken() );					  
			}

            list.add(v_lessons1);
            list.add(v_level1Obnums);  // 상 - 객관식
            list.add(v_level2Obnums);  // 중 - 객관식
            list.add(v_level3Obnums);  // 하 - 객관식
            list.add(v_level1Subnums); // 상 - 주관식  
            list.add(v_level2Subnums); // 중 - 주관식 
            list.add(v_level3Subnums); // 하 - 주관식 
            list.add(v_level1OXnums); // 상 - OX식 
            list.add(v_level2OXnums); // 중 - OX식 
            list.add(v_level3OXnums); // 하 - OX식    
		}catch(Exception ex ){
			ex.printStackTrace();
		}
		return list;
	}
	
	
	// 수료조건 확인
	public String[] isgraduatedCheck(Map commandMap, List list, Map subjInfo) throws Exception{
		String[] result = new String[list.size()];
		if( list != null && list.size() > 0 ){
			for(int i=0; i<list.size(); i++){
				Map data = (Map)list.get(i);
				String v_notgraducd = "";
				String isgraduated = "";
				System.out.println("====================== score  "+data.get("score")+"");
				System.out.println("====================== gradscore  "+returnDouble(subjInfo.get("gradscore")));
				double score = returnDouble(data.get("score"));
				if( score > 100){
					score = 100;
				}
				// 총점 체크
	            if ( score < returnDouble(subjInfo.get("gradscore")) ) { 
	                v_notgraducd += "06,"; // 06 = 성적미달   - 총점점수 체크
	            }
	            
	            
	            System.out.println("================= examFlag "+(String)data.get("examFlag"));
	            // 2008.12.13 수정
	            // 평가 제출여부(05)
	            commandMap.put("pp_userid", data.get("userid"));
	            if ("N".equals( (String)data.get("examFlag") )) {
	            	v_notgraducd += "05,"; // 05 = 평가 미제출
	            }
	            
	            System.out.println("================= mtest "+data.get("mtest")+"");
	            System.out.println("================= gradexam "+returnDouble(subjInfo.get("gradexam")));
	            // 평가
	            if ( returnDouble(data.get("mtest")) < returnDouble(subjInfo.get("gradexam")) ) { 
	                v_notgraducd += "07,"; // 07 = 평가점수미달
	            }
	            System.out.println("================= htest "+data.get("htest")+"");
	            System.out.println("================= gradhtest "+returnDouble(subjInfo.get("gradhtest")));
	            if ( returnDouble(data.get("htest")) < returnDouble(subjInfo.get("gradhtest")) ) { 
	            	v_notgraducd += "07,"; // 07 = 평가점수미달
	            }
	            System.out.println("================= ftest "+data.get("ftest")+"");
	            System.out.println("================= gradftest "+returnDouble(subjInfo.get("gradftest")));
	            if ( returnDouble(data.get("ftest")) < returnDouble(subjInfo.get("gradftest")) ) { 
	            	v_notgraducd += "07,"; // 07 = 평가점수미달
	            }
	            
	            System.out.println("================= report "+data.get("report")+"");
	            System.out.println("================= gradreport "+returnDouble(subjInfo.get("gradreport")));
	            // 리포트
	            if ( returnDouble(data.get("report")) < returnDouble(subjInfo.get("gradreport")) ) { 
	            	v_notgraducd += "08,"; // 08 = 리포트점수미달
	            }
	            
	            System.out.println("================= etc2 "+data.get("etc2")+"");
	            System.out.println("================= wetc1 "+returnDouble(subjInfo.get("wetc1")));
	            System.out.println("================= etc2 "+returnDouble(subjInfo.get("etc2")));
	            //참여도(출석점수)
	            if ( returnDouble(data.get("etc2")) < returnDouble(subjInfo.get("wetc1")) || returnDouble(data.get("etc2")) == 0 ) { 
	            	 v_notgraducd += "13,"; // 13 = 출석점수미달
	            }
	            
	            /*
	            // 2008.12.13 수정
	            // 이러닝과정 - 수료기준(접속횟수, 학습시간)에 부합하는지 확인
	            if (subjseqdata.getIsonoff().equals("ON")) {
	            	
	            	// 학습횟수(09)
	            	if ( "N".equals(getStudyCountYn(connMgr, p_subj, p_year, p_subjseq, p_userid, subjseqdata))) {
	            		v_notgraducd += "09,"; // 09 = 학습횟수미달
	            	}
	            	
	            	// 접속시간(12)
	            	if ( "N".equals(getStudyTimeYn(connMgr, p_subj, p_year, p_subjseq, p_userid, subjseqdata))) {
	            		v_notgraducd += "12,"; // 12 = 접속시간미달
	            	}
	            }
	            */
	            
	            if ( !v_notgraducd.equals("") ) { 
	            	v_notgraducd = v_notgraducd.substring(0,v_notgraducd.length()-1);
	            }
	            
	            if (    score >= returnDouble(subjInfo.get("gradscore"))
	                    &&  returnDouble(data.get("tstep")) >= returnDouble(subjInfo.get("gradstep"))
	                    &&  v_notgraducd.length() == 0                                                                             
	                    &&  ( 
	                    		((subjInfo.get("isonoff").toString().equals("ON") || subjInfo.get("isonoff").toString().equals("RC")) && returnDouble(subjInfo.get("gradexam")) <= returnDouble(data.get("mstep"))) // && data.getGradexamcnt()    > 0
	                    		|| subjInfo.get("isonoff").toString().equals("OFF")
	                        )                                                                                                                                 
	                                                                                                                                 
	                    &&  (       
	                    		(   ((subjInfo.get("isonoff").toString().equals("ON") || subjInfo.get("isonoff").toString().equals("RC")) && returnDouble(subjInfo.get("gradftest")) <= returnDouble(data.get("ftest")) ) //&& data.getGradftestcnt()  > 0
	                                 || (subjInfo.get("isonoff").toString().equals("OFF") && returnDouble(subjInfo.get("gradftest")) <= returnDouble(data.get("ftest")) ) //&& data.getFtest() > 0
	                            )  
	                                                                              
	                        )                                                                                                                                 
	                    &&  (       
	                    		(   ((subjInfo.get("isonoff").toString().equals("ON") || subjInfo.get("isonoff").toString().equals("RC"))  && returnDouble(subjInfo.get("gradreport")) <= returnDouble(data.get("report")) ) //&& data.getGradreportcnt()> 0
	                                 || (subjInfo.get("isonoff").toString().equals("OFF") && returnDouble(subjInfo.get("gradreport")) <= returnDouble(data.get("report")) ) //&& data.getReport()       > 0
	                             )
	                                                                               
	                        )     
	                   &&  (       
	                		(   ((subjInfo.get("isonoff").toString().equals("ON") || subjInfo.get("isonoff").toString().equals("RC"))  && returnDouble(subjInfo.get("wetc1")) <= returnDouble(data.get("etc2")) && returnDouble(data.get("etc2"))!=0 ) //&& data.getGradreportcnt()> 0
	                             || (subjInfo.get("isonoff").toString().equals("OFF") && returnDouble(subjInfo.get("wetc1")) <= returnDouble(data.get("etc2")) ) //&& data.getReport()       > 0
	                         ) //출석점수
	                                                                           
	                     )     
	                   ) { // 전체 조건에 맞으면 수료                                                                                                         
	            	isgraduated = "Y";
	            
	        
	            
	            } else { 
	            	isgraduated = "N"; //미수료 시 U로 한다.
	            }
	            
	            // 기타 조건으로 미수료
	            if ( (isgraduated).equals("")) { 
	            	isgraduated = "N";
	            }
	            
	            result[i] = data.get("userid")+"!_"+v_notgraducd+"!_"+isgraduated;
	            
	            System.out.println("=========================== result["+i+"] " + result[i]);
	            
	            //수료정보 insert
	            commandMap.put("notgraducd", v_notgraducd);
	            commandMap.put("isgraduated", isgraduated);
	            commandMap.put("usergraduated", result[i]);	            
	            finishManageDAO.insertUsergraduated(commandMap);
	            
			}
		}
		
		return result;
	}
	
	public double returnDouble(Object obj){
		double ddb = 0.00;
		try{
			ddb = Double.parseDouble(obj.toString());
		} catch (Exception e) {
			return ddb;
		}
		
		return ddb;
	}
	
	public List selectPaperQuestionExampleBankList(Map<String, Object> commandMap) throws Exception{
		
		List QuestionExampleDataList = null;	
        QuestionExampleDataList = studyExamDAO.selectPaperQuestionExampleBankList(commandMap);		
		return QuestionExampleDataList;
	}
	
	public List selectExamBankPaperModifyList(Map<String, Object> commandMap) throws Exception{
		
		List QuestionExampleDataList = null;	
        QuestionExampleDataList = studyExamDAO.selectExamBankPaperModifyList(commandMap);		
		return QuestionExampleDataList;
	}
	
	//제출
	public int updateTZ_examresultEnded(Map<String, Object> commandMap) throws Exception{
		return studyExamDAO.updateTZ_examresultEnded(commandMap);
	}

	public Map<String, Object> getExamResultInfo(Map<String, Object> commandMap) {
		// TODO Auto-generated method stub
		return studyExamDAO.getExamResultInfo(commandMap);
	}

	public List getExamInProgress(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return studyExamDAO.getExamInProgress(paramMap);
	}

	public String getRetryExamChangeYn(Map<String, Object> commandMap) {
		// TODO Auto-generated method stub
		return studyExamDAO.getRetryExamChangeYn(commandMap);
	}

	public void updateRetryExamChange(Map<String, Object> commandMap) {
		// TODO Auto-generated method stub
		studyExamDAO.updateRetryExamChange(commandMap);
	}
	
	public String selectExamResultSubmit(Map<String, Object> commandMap) {
		return studyExamDAO.selectExamResultSubmit(commandMap);
	}
	
	
}
