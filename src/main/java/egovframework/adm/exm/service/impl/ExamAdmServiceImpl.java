package egovframework.adm.exm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ziaan.exam.ExamBean;

import egovframework.adm.exm.dao.ExamAdmDAO;
import egovframework.adm.exm.service.ExamAdmService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.com.utl.fcc.service.EgovStringUtil;

@Service("examAdmService")
public class ExamAdmServiceImpl extends EgovAbstractServiceImpl implements ExamAdmService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="examAdmDAO")
    private ExamAdmDAO examAdmDAO;
	
	
	
	/**
	 * 평가문제 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamQuestList(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamQuestList(commandMap);
	}
	
	/**
	 * 평가문제 미리보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamQuestPreviewList(Map<String, Object> commandMap) throws Exception{
		
		List list = null;
		
        String p_subj = commandMap.get("p_subj") + "";
        String v_chknum = commandMap.get("p_chknum") + "";

		String[] v_examnums = {};
		
		if (v_chknum.length() > 0) {
			v_examnums = v_chknum.split(",");
		}
		
		list = new ArrayList();
		
		for ( int i = 0; i<v_examnums.length; i++ ) { 
			HashMap mm = new HashMap();
			mm.put("p_subj", p_subj);
			mm.put("p_examnum", v_examnums[i]);
			
			
			//개별 문항 가져오기..
			List result = examAdmDAO.selectExamQuestPreviewList(mm);
			
			//문항 추가..
			list.add(result);
		}
		
		return list;
	}
	
	public boolean deleteQuestCheckExamList(Map<String, Object> commandMap) throws Exception {
		
		
		boolean isok = false;
		try {
			
			String p_subj   = commandMap.get("p_subj")+"";
	        String[] v_checks = EgovStringUtil.getStringSequence(commandMap, "p_checks");
	        
	        
	        
			for ( int i = 0; i < v_checks.length; i++ ) { 
				HashMap mm = new HashMap();
				mm.put("p_subj", p_subj);
				mm.put("v_examnum", v_checks[i]);
            	
            	int cnt = examAdmDAO.selectExamQuestResultCount(mm);
            	
            	if(cnt == 0)
            	{
            		int exCnt = examAdmDAO.deleteExam(mm);
            		if(exCnt > 0) examAdmDAO.deleteExamSel(mm);
            	}
			}
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}


	
	
	/**
	 * 평가마스터 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamMasterList(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamMasterList(commandMap);
	}
	
	/**
	 * 평가문제지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamPaperList(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamPaperList(commandMap);
	}
	
	/**
	 * 평가문제지 정보 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamPaperView(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamPaperView(commandMap);
	}
	
	
	/**
	 * 평가결과조회 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamResultList(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamResultList(commandMap);
	}
	
	
	/**
	 * 평가결과 조회 전체평균정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamResultAvg(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamResultAvg(commandMap);
	}
	
	/**
	 * 학습자 재응시 횟수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int upddateExamUserRetry(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.upddateExamUserRetry(commandMap);
	}
	
	/**
	 * 평가자 평가 정보 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserPaperResultList(Map<String, Object> commandMap) throws Exception{
		
		List<?> list = null;
		
		Map<?, ?> view = examAdmDAO.selectUserExamNumberList(commandMap);
		
		if(view != null)
		{
			//등록된 시험번호
			String commaExamPaperNums = view.get("exam") + "";
			
			//등록된 시험번호 정렬
			String commaExamPaperNumsOrderBy = "";
			//등록된 시험번호 재배열
			String newCommaExamPaperNums = "";
			
			
			StringTokenizer st = new StringTokenizer(commaExamPaperNums, ExamBean.SPLIT_COMMA);
			
			
			
			//정렬 쿼리 순서 만들기
			int i = 0;
			while ( st.hasMoreElements() ) {
				String examnum = st.nextToken();
				
				if(examnum != null)
				{
					i++;
					if(i > 1) 	
					{
						newCommaExamPaperNums += ExamBean.SPLIT_COMMA;
						commaExamPaperNumsOrderBy += ExamBean.SPLIT_COMMA;
					}
					
					newCommaExamPaperNums +=  examnum;
					commaExamPaperNumsOrderBy +=  examnum + ExamBean.SPLIT_COMMA + i;
				}
			}
			
			
			HashMap<String, Object> mm = new HashMap<String, Object>();
			
			//새로운 문제은행 코드
			String examsubj = view.get("examsubj") !=null ? view.get("examsubj").toString() : "";
			if(!"".equals(examsubj)){
				mm.put("p_subj", examsubj);	
			}else{
				mm.put("p_subj", commandMap.get("p_subj"));
			}			
			
			mm.put("p_commaExamPaperNums", newCommaExamPaperNums);
			mm.put("p_commaExamPaperNumsOrderBy", commaExamPaperNumsOrderBy);
			
			//전체 문제 가져오기
			list = examAdmDAO.selectUserExamPaperList(mm);
			
		}
		
		return list;
	}
	
	/**
	 * 학습자별 평가답안 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserExamResultList(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectUserExamResultList(commandMap);
	}
	
	
	/**
	 * 학습자별 평가답안 정보 - 풀어서보여주기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserExamResultAnswerList(Map<String, Object> userExamMap) throws Exception{
		
		ArrayList<Object> list = new ArrayList<Object>();
		
		StringTokenizer st = null;
		Vector<String> v_answer = null;
		
		StringTokenizer st2 = null;
		Vector<String> v_corrected = null;
		
		if(userExamMap != null)
		{
			st = new StringTokenizer(userExamMap.get("answer")+"", ExamBean.SPLIT_COMMA);
			v_answer = new Vector<String>();
			while ( st.hasMoreElements() ) { 
                v_answer.add(st.nextToken() );
			}
			
			
			st2 = new StringTokenizer(userExamMap.get("corrected")+"", ExamBean.SPLIT_COMMA);
			v_corrected = new Vector<String>();
			while ( st2.hasMoreElements() ) { 
                v_corrected.add(st2.nextToken() );
			}
		}
		
		list.add(v_answer);
		list.add(v_corrected);
		
		return list;
	}

	/**
	 * 평가문제 Pool 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamQuestPoolList(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamQuestPoolList(commandMap);
	}
	
	/**
	 * 평가문제 Pool 문제등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertTzExamPoolSelect(Map<String, Object> commandMap) throws Exception{
		
		StringTokenizer st = null;
		boolean isok = false;
		
		
		try {
			String p_userid   = commandMap.get("userid")+"";
			String p_subj   = commandMap.get("p_subj")+"";
	        String[] v_checks = EgovStringUtil.getStringSequence(commandMap, "p_checks");
	        
	        
	        for ( int i = 0; i < v_checks.length; i++ ) { 
	        		st = new StringTokenizer(v_checks[i],"|");
				   
				    String v_subj = (String)st.nextToken();
					String v_examnum = (String)st.nextToken();
					
					HashMap<String, Object> mm = new HashMap<String, Object>();
					mm.put("p_subj", p_subj);
					mm.put("p_userid", p_userid);
					mm.put("v_subj", v_subj);
					mm.put("v_examnum", v_examnum);
					
					//문제등록
					Object p_examnum = examAdmDAO.insertTzExamPoolSelect(mm);
					
					//문제보기등록
					if(p_examnum != null)
					{
						mm.put("p_examnum", p_examnum);
						
						examAdmDAO.insertTzExamSelPoolSelect(mm);
						
					}
	        }
	        
	        isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}
	
	
	/**
	 * 평가문제 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectQuestExamView(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectQuestExamView(commandMap);
	}
	
	/**
	 * 평가문제 해당 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectQuestExamSelList(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectQuestExamSelList(commandMap);
	}
	
	
	/**
	 * 평가문제 답안의 개수를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectQuestExamSelCountList(Map<String, Object> commandMap, int answerCnt, String questionType) throws Exception{
		
		int exSelCnt = 0;
		ArrayList list = new ArrayList();
		
		for(int i=1; i<=answerCnt; i++)
		{
			Object p_seltext = commandMap.get("p_seltext" + questionType + "_" + i);
			Object p_isanswer = commandMap.get("p_isanswer" + questionType + "_" + i);
			
			if(p_seltext != null && !p_seltext.equals(""))
			{
				exSelCnt++;
				Map exSelMap = new HashMap();
				exSelMap.put("v_seltext", p_seltext);
				if(p_isanswer != null && !p_isanswer.equals("")) exSelMap.put("v_isanswer", p_isanswer);
				exSelMap.put("v_selnum", exSelCnt);
				list.add(exSelMap);
			}
		}
		
		return list;
	}
	
	/**
	 * 평가문제 List객체 개수만큼의 답안을 등록한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertQuestExamSel(String p_examnum, String p_subj, String p_userid, List list) throws Exception{
		boolean isok = false;
		
		try {
			if(p_examnum != null)		//답안등록
			{
				for(int i=0; i<list.size(); i++)
				{
					Map exSelMap = (Map)list.get(i);	//원래 있던 보기에 다시 추가해 준다.
					exSelMap.put("v_subj", p_subj);
					exSelMap.put("v_userid", p_userid);
					exSelMap.put("v_examnum", p_examnum);
					
					examAdmDAO.insertTzExamSel(exSelMap);		//답안 등록하기
				}
			}
			 isok = true;
				
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	/**
	 * 평가문제등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertTzExam(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		//문제분류
		String p_examtype = commandMap.get("p_examtype")+"";
		String p_subj = commandMap.get("p_subj")+"";
		String p_isuse = commandMap.get("p_isuse")+"";
		String p_levels = commandMap.get("p_levels")+"";
		String p_examtext = commandMap.get("p_examtext")+"";
		String p_exptext = commandMap.get("p_exptext")+"";
		String p_userid = commandMap.get("userid")+"";
		String p_examiner = commandMap.get("p_examiner")+"";
		String p_lessonnum = commandMap.get("p_lessonnum")+"";
		
		try {
			HashMap<String, Object> exMap = new HashMap<String, Object>();
			exMap.put("v_examtype", p_examtype);
			exMap.put("v_subj", p_subj);
			exMap.put("v_isuse", p_isuse);
			exMap.put("v_levels", p_levels);
			exMap.put("v_examtext", p_examtext);
			exMap.put("v_exptext", p_exptext);
			exMap.put("v_userid", p_userid);
			exMap.put("v_examiner", p_examiner);
			exMap.put("v_lessonnum", p_lessonnum);
			
			if(p_examtype.equals(ExamBean.OBJECT_QUESTION))	//객관식
			{
				//답안갯수 및 답안 정보를 list객체에 넣는다.
				List list = this.selectQuestExamSelCountList(commandMap, 10, ExamBean.OBJECT_QUESTION);
				
				exMap.put("v_selcount", list.size());
				Object v_examnum = examAdmDAO.insertTzExam(exMap); 	//문제등록
				
				//답안등록
				this.insertQuestExamSel(v_examnum+"", p_subj, p_userid, list);
			}
			
			
			else if(p_examtype.equals(ExamBean.SUBJECT_QUESTION))	//주관식
			{
				//답안갯수 및 답안 정보를 list객체에 넣는다.
				List list = this.selectQuestExamSelCountList(commandMap, 10, ExamBean.SUBJECT_QUESTION);
				
				exMap.put("v_selcount", list.size());
				Object v_examnum = examAdmDAO.insertTzExam(exMap); 	//문제등록
				
				//답안등록
				this.insertQuestExamSel(v_examnum+"", p_subj, p_userid, list);
			}
			
			
			else if(p_examtype.equals(ExamBean.OX_QUESTION))	//ox
			{
				//답안갯수 및 답안 정보를 list객체에 넣는다.
				List list = this.selectQuestExamSelCountList(commandMap, 2, ExamBean.OX_QUESTION);
				
				exMap.put("v_selcount", list.size());
				Object v_examnum = examAdmDAO.insertTzExam(exMap); 	//문제등록
				
				//답안등록
				this.insertQuestExamSel(v_examnum+"", p_subj, p_userid, list);
			}
			
			
			 isok = true;
				
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}
	
	
	/**
	 * 평가문제수정
	 * @param commandMap
	 * @return -1 : 사용중인 문항임
	 * @return -2 : 에러
	 * @return  0 : 정상종료
	 * @throws Exception
	 */
	public int updateTzExam(Map<String, Object> commandMap) throws Exception{
//		return examAdmDAO.updateTzExam(commandMap);
		int rtn = -2;
		
		//문제분류
		
		String p_examnum = commandMap.get("p_examnum")+"";
		String p_examtype = commandMap.get("p_examtype")+"";
		String p_subj = commandMap.get("p_subj")+"";
		String p_isuse = commandMap.get("p_isuse")+"";
		String p_levels = commandMap.get("p_levels")+"";
		String p_examtext = commandMap.get("p_examtext")+"";
		String p_exptext = commandMap.get("p_exptext")+"";
		String p_userid = commandMap.get("userid")+"";
		
		
		try {
			
			
			HashMap<String, Object> exMap = new HashMap<String, Object>();
			exMap.put("v_examnum", p_examnum);
			exMap.put("p_subj", p_subj);
			
			//문항이 사용되어 졌는지를 검사한다.
			int cnt =  0; 	//examAdmDAO.selectExamQuestResultCount(exMap);
			
			
			if(cnt > 0)
			{
				rtn = -1;
			}
			else
			{
				//보기 답안 모두 삭제하기
				cnt = examAdmDAO.deleteExamSel(exMap);
				
				//추가했던과정코드 삭제
				exMap.remove("p_subj");
						
						
				if(cnt > 0)
				{
					exMap.put("v_examtype", p_examtype);
					exMap.put("v_subj", p_subj);
					exMap.put("v_isuse", p_isuse);
					exMap.put("v_levels", p_levels);
					exMap.put("v_examtext", p_examtext);
					exMap.put("v_exptext", p_exptext);
					exMap.put("v_userid", p_userid);
					
					
					if(p_examtype.equals(ExamBean.OBJECT_QUESTION))	//객관식
					{
						//답안갯수 및 답안 정보를 list객체에 넣는다.
						List list = this.selectQuestExamSelCountList(commandMap, 10, ExamBean.OBJECT_QUESTION);
						
						exMap.put("v_selcount", list.size());
						examAdmDAO.updateTzExam(exMap); 	//문제수정
						
						//답안등록
						this.insertQuestExamSel(p_examnum, p_subj, p_userid, list);
					}
					
					
					else if(p_examtype.equals(ExamBean.SUBJECT_QUESTION))	//주관식
					{
						//답안갯수 및 답안 정보를 list객체에 넣는다.
						List list = this.selectQuestExamSelCountList(commandMap, 10, ExamBean.SUBJECT_QUESTION);
						
						exMap.put("v_selcount", list.size());
						examAdmDAO.updateTzExam(exMap); 	//문제수정
						
						//답안등록
						this.insertQuestExamSel(p_examnum, p_subj, p_userid, list);
					}
					
					
					else if(p_examtype.equals(ExamBean.OX_QUESTION))	//ox
					{
						//답안갯수 및 답안 정보를 list객체에 넣는다.
						List list = this.selectQuestExamSelCountList(commandMap, 2, ExamBean.OX_QUESTION);
						
						exMap.put("v_selcount", list.size());
						examAdmDAO.updateTzExam(exMap); 	//문제수정
						
						//답안등록
						this.insertQuestExamSel(p_examnum, p_subj, p_userid, list);
					}
					
					rtn = 0;
				}
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			rtn = -2;
		}
		
		return rtn;
	}
	
	
	/**
	 * 평가마스터관리에서 사용할 수 있는 문제의 개수를 뽑아오는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamLevelsList(Map<String, Object> commandMap) throws Exception{
		
		ArrayList lessonlist = new ArrayList();
		String v_subj    		= commandMap.get("p_subj") + "";
        String v_lessonstart    = commandMap.get("p_lessonstart") + "";
        String v_lessonend    	= commandMap.get("p_lessonend") + "";

        int v_startlesson = Integer.parseInt(v_lessonstart);
        int v_endlesson = Integer.parseInt(v_lessonend);

		int v_lesson = 0;
		int v_examtype = 0;
		int v_levels = 0;
		
		try { 

            for ( int i = v_startlesson; i <= v_endlesson ;  i++ ) { 
            	
                 v_lesson = i;
				 ArrayList levelslist = new ArrayList();
				 
                 for ( int j = 1; j <= 3; j++ ) { 
                	 
                     v_levels = j;
                     ArrayList typelist = new ArrayList();
                     
                     for ( int k =1; k <= 3 ; k++ ) { 
                         v_examtype = k;

                         Map mm = new HashMap();
                         mm.put("v_subj", v_subj);
                         mm.put("v_lesson", v_lesson);
                         mm.put("v_examtype", v_examtype);
                         mm.put("v_levels", v_levels);

                         typelist.add(examAdmDAO.selectExamLevelsCount(mm));
					 }

                     levelslist.add(typelist);
                     
                 }
                 
                 lessonlist.add(levelslist);
            }
        } catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return lessonlist;
		
	}
	
	
	
	/**
	 * 평가마스터등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertTzExamMaster(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		
		try { 
			
			examAdmDAO.insertTzExamMaster(commandMap);
			
			
			isok = true;
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	
	/**
	 * 평가마스터 정보 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamMasterView(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamMasterView(commandMap);
	}
	
	/**
	 * 평가마스터가 문제지에서 사용되었는지를 확인한다
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamMasterPaperCount(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamMasterPaperCount(commandMap);
	}
	
	
	/**
	 * 평가마스터  삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTzExamMaster(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try { 
			int cnt = examAdmDAO.selectExamMasterPaperCount(commandMap);
			
			if(cnt == 0){
				examAdmDAO.deleteTzExamMaster(commandMap);
				isok = true;
			}
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	

	/**
	 * 평가마스터  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateTzExamMaster(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try { 
			int cnt = examAdmDAO.selectExamMasterPaperCount(commandMap);
			
			if(cnt == 0){
				examAdmDAO.updateTzExamMaster(commandMap);
				isok = true;
			}
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	
	
	
	
	/**
	 * 문제지가 출제가 되었는지를 검사한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamResultCount(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamResultCount(commandMap);
	}
	
	
	/**
	 * 과정별 평가문제지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int insertExamPaperSubject(Map<String, Object> commandMap) throws Exception{
		int isok = 0;	//에러
		
		try {
			
			//과정별 평가정보 불러오기
			List list = examAdmDAO.selectExamMasterSubjectList(commandMap);
			
			if(list != null && list.size() > 0)
			{
				for(int i=0; i<list.size(); i++)
				{
					HashMap mm = (HashMap)list.get(i);
					//평가문제지 등록
					Object obj = examAdmDAO.insertExamPaper(mm);
					log.info("p_papernum : " + obj);
				}
				
				
				isok = 1;	//정상종료
			}
			else
			{
				isok = -1;	//평가마스터가 없음..
			}
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	/**
	 * 기수별 평가문제지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int insertExamPaperGrSeq(Map<String, Object> commandMap) throws Exception{
		int isok = 0;	//에러
		
		
		try {
			//과정별 평가정보 불러오기
			List list = examAdmDAO.selectExamMasterGrSeqList(commandMap);
			if(list != null && list.size() > 0)
			{
				for(int i=0; i<list.size(); i++)
				{
					HashMap mm = (HashMap)list.get(i);
					
					//평가문제지 등록
					Object obj = examAdmDAO.insertExamPaper(mm);
					log.info("p_papernum : " + obj);
				}
	
				isok = 1;	//정상종료
			}
			else
			{
				isok = -1;	//추가할 문제지  없음
			}				
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	/**
	 * 평가문제지  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateExamPaper(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try { 
			int cnt = examAdmDAO.selectExamResultCount(commandMap);
			
			if(cnt == 0){
				examAdmDAO.updateExamPaper(commandMap);
				isok = true;
			}
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	
	/**
	 * 평가문제지 종료 기간연장
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateExamPaperDate(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try { 
			examAdmDAO.updateExamPaperDate(commandMap);
			isok = true;
		
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	/**
	 * 평가문제지  삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteExamPaper(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try { 
			int cnt = examAdmDAO.selectExamResultCount(commandMap);
			
			if(cnt == 0){
				examAdmDAO.deleteExamPaper(commandMap);
				isok = true;
			}
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}

	/**
	 * 평가POOL 과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectExamPoolSubjList(Map<String, Object> commandMap) throws Exception {
		return examAdmDAO.selectExamPoolSubjList(commandMap);
	}

	/**
	 * 평가POOL 팝업 과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List examPoolSubjListPop(Map<String, Object> commandMap) throws Exception {
		return examAdmDAO.examPoolSubjListPop(commandMap);
	}

	/**
	 * 평가POOL 과정 문제 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List examQuestionDetailList(Map<String, Object> commandMap)	throws Exception {
		return examAdmDAO.examQuestionDetailList(commandMap);
	}

	/**
	 * 평가 문제 복사
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> examQuestPoolCopy(Map<String, Object> commandMap)	throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		int resultCnt =0;
		if(commandMap.get("p_examnums") != null && !"".equals(commandMap.get("p_examnums"))){
			String []  examnums = commandMap.get("p_examnums").toString().split(";");
			for(int i= 0 ; i < examnums.length ; i++){
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("p_subj",  commandMap.get("t_subj"));
				paramMap.put("v_subj",  commandMap.get("p_subj"));
				paramMap.put("v_examnum", examnums[i]);
				paramMap.put("p_userid", commandMap.get("userid"));
				log.error(paramMap);
				Object p_examnum = examAdmDAO.insertTzExamPoolSelect(paramMap);
				//문제보기등록
				if(p_examnum != null)
				{
					paramMap.put("p_examnum", p_examnum);
					
					examAdmDAO.insertTzExamSelPoolSelect(paramMap);
					
				}
				resultCnt++;
			}						
		}else if("poolinsert".equals(commandMap.get("p_process"))){
			List<Map<String, Object>> paramList = examAdmDAO.examPoolSelect(commandMap);
			
			for(Map<String, Object> map : paramList){
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("p_subj",  commandMap.get("t_subj"));
				paramMap.put("v_subj",  commandMap.get("p_subj"));
				paramMap.put("v_examnum", map.get("examnum"));
				paramMap.put("p_userid", commandMap.get("userid"));
				log.error(paramMap);
				Object p_examnum = examAdmDAO.insertTzExamPoolSelect(paramMap);
				//문제보기등록
				if(p_examnum != null)
				{
					paramMap.put("p_examnum", p_examnum);
					
					examAdmDAO.insertTzExamSelPoolSelect(paramMap);
					
				}
				resultCnt++;
			}				
		}
		result.put("resultCnt", resultCnt);
		return result;
	}

	/**
	 * 문제 엑셀 업로드
	 */
	public void insertExamFileToDB(List result, Map<String, Object> commandMap)
			throws Exception {
		
		HashMap<String, Object> inputMap = new HashMap<String, Object>();
		String p_select = (String)commandMap.get("p_select"); 
		if(p_select.equals("1")){
			for(int i=0; i<result.size(); i++){
				
				Map resultMap = (Map)result.get(i);
				
				inputMap.put("v_subj", resultMap.get("parameter0"));
				inputMap.put("v_examnum", resultMap.get("parameter1"));
				inputMap.put("v_examtype", resultMap.get("parameter2"));
				inputMap.put("v_examtext", resultMap.get("parameter3"));
				inputMap.put("v_exptext", resultMap.get("parameter4"));
				inputMap.put("v_levels", resultMap.get("parameter5"));
				inputMap.put("v_selcount", resultMap.get("parameter6"));
				inputMap.put("v_userid", commandMap.get("userid"));
				
				examAdmDAO.insertQeustExcelToDB(inputMap);
			}
		}else if(p_select.equals("2")){
			for(int i=0; i<result.size(); i++){
				Map resultMap = (Map)result.get(i);
			
				inputMap.put("v_subj", resultMap.get("parameter0"));
				inputMap.put("v_examnum", resultMap.get("parameter1"));
				inputMap.put("v_selnum", resultMap.get("parameter2"));
				inputMap.put("v_seltext", resultMap.get("parameter3"));
				inputMap.put("v_isanswer", resultMap.get("parameter4"));
				inputMap.put("v_userid", commandMap.get("userid"));
				
				examAdmDAO.insertQeustBogiExcelToDB(inputMap);
			}
		}
	}
	
	/**
	 * 평가문제은행 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectExamBankList(Map<String, Object> commandMap) throws Exception {
		return examAdmDAO.selectExamBankList(commandMap);
	}

	
	/**
	 * 평가문제은행 과정등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertTzExamSubj(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		
		try { 
			
			examAdmDAO.insertTzExamSubj(commandMap);
			
			
			isok = true;
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	
	/**
	 * 평가문제은행 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public Object selectTzExamSubj(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectTzExamSubj(commandMap);
	}
	
	
	/**
	 * 문제은행 과정 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateTzExamSubj(Map<String, Object> commandMap)	throws Exception {
		
		boolean isok = false;
		
		try{
                	
        	//문제은행 과정 수정
        	int cnt = examAdmDAO.updateTzExamSubj(commandMap);
        	
        	if(cnt == 0) 
        	{
        		return false;
        	}   
			
			isok = true;
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return isok;
	}
	
	/**
	 * 평가문제은행 문제 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List examBankDetailList(Map<String, Object> commandMap)	throws Exception {
		return examAdmDAO.examBankDetailList(commandMap);
	}
	
	
	/**
	 * 평가마스터등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertExamBankTzExamMaster(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		
		try { 
			
			int examMasterCnt = examAdmDAO.selectExamBankMasterCnt(commandMap);
			if(examMasterCnt == 0){
				examAdmDAO.insertTzExamBankMaster(commandMap);
			}
			
			isok = true;
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	
	/**
	 * 기수별 평가문제지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int insertExamBankPaperGrSeq(Map<String, Object> commandMap) throws Exception{
		int isok = 0;	//에러
		
		
		try {
			//문제지 번호
			int p_papernum =  examAdmDAO.selectExamBankPaperNum(commandMap);
			commandMap.put("p_papernum", p_papernum);
			
			//문제지 등록
			Object obj = examAdmDAO.insertExamBankPaper(commandMap);			
			
			//평가문제 등록
			Object examBankPaperSheet = examAdmDAO.insertExamBankPaperSheet(commandMap);
			
			//평가 문제 정보 수정
			examAdmDAO.updateTzExamBankUseInfo(commandMap);
			
			isok = 1;	//정상종료		
							
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	
	/**
	 * 평가마스터관리에서 사용할 수 있는 문제의 개수를 뽑아오는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamBankLevelsList(Map<String, Object> commandMap) throws Exception{
		
		ArrayList lessonlist = new ArrayList();
		String v_subj    		= commandMap.get("p_subj") + "";
        String v_lessonstart    = commandMap.get("p_lessonstart") + "";
        String v_lessonend    	= commandMap.get("p_lessonend") + "";
        String s_setexamcnt    	= commandMap.get("p_setexamcnt") + "";
        String s_setexamlessoncnt    	= commandMap.get("p_setexamlessoncnt") + "";

        int v_setexamcnt = Integer.parseInt(s_setexamcnt);
        int v_setexamlessoncnt = Integer.parseInt(s_setexamlessoncnt);
        
        int v_startlesson = Integer.parseInt(v_lessonstart);
        int v_endlesson = Integer.parseInt(v_lessonend);

		int v_lesson = 0;
		int v_examtype = 0;
		int v_levels = 0;
		
		try { 

            for ( int i = v_startlesson; i <= v_endlesson ;  i++ ) { 
            	
                 v_lesson = i;
				 ArrayList levelslist = new ArrayList();
				 
                 for ( int j = 1; j <= 3; j++ ) { 
                	 
                     v_levels = j;
                     ArrayList typelist = new ArrayList();
                     
                     for ( int k =1; k <= 3 ; k++ ) { 
                         v_examtype = k;

                         Map mm = new HashMap();
                         mm.put("v_subj", v_subj);
                         mm.put("v_lesson", v_lesson);
                         mm.put("v_examtype", v_examtype);
                         mm.put("v_levels", v_levels);
                         mm.put("v_setexamcnt",v_setexamcnt);
                         mm.put("v_setexamlessoncnt",v_setexamlessoncnt);
                         typelist.add(examAdmDAO.selectExamBankLevelsCount(mm));
					 }

                     levelslist.add(typelist);
                     
                 }
                 
                 lessonlist.add(levelslist);
            }
        } catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return lessonlist;
		
	}
	
	/**
	 * 평가문제지  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateExamBankPaper(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try { 
			int cnt = examAdmDAO.selectExamBankResultCount(commandMap);
			
			if(cnt == 0){
				
				int p_examcnt = Integer.parseInt(commandMap.get("p_examcnt").toString());
				
				
				//출제건수 -1
				examAdmDAO.updateTzExamBankUseMaInfo(commandMap);
				
				//출제문제 삭제
				examAdmDAO.deleteExamBankPaperSheet(commandMap);
				
				int insCnt = examAdmDAO.selectExamBankLevelsInsCnt(commandMap);
				
				System.out.println("p_examcnt ------> "+p_examcnt);
				System.out.println("insCnt ------> "+insCnt);				
				
				if(p_examcnt != p_examcnt){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return isok; 
				}		
				
				//평가문제 등록
				Object examBankPaperSheet = examAdmDAO.insertExamBankPaperSheet(commandMap);
				//문제지 수정
				examAdmDAO.updateExamBankPaper(commandMap);
				
				//평가 문제 정보 수정
				examAdmDAO.updateTzExamBankUseInfo(commandMap);
				
				isok = true;
			}
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	
	/**
	 * 평가문제지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamBankPaperView(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankPaperView(commandMap);
	}
	
	/**
	 * 콘텐츠 문제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamBankSubj(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankSubj(commandMap);
	}
	
	//문제지 등록?
	public int selectExamBankPaperCount(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankPaperCount(commandMap);
	}
	
	
	/**
	 * 평가문제지 종료 기간연장
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateExamBankPaperDate(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try { 
			examAdmDAO.updateExamBankPaperDate(commandMap);
			isok = true;
		
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	/**
	 * 평가 기본정보 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateExamBankPaperBasic(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try { 
			examAdmDAO.updateExamBankPaperBasic(commandMap);
			isok = true;
		
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	/**
	 * 평가문제지  삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteExamBankPaper(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try { 
			int cnt = examAdmDAO.selectExamBankResultCount(commandMap);
			
			if(cnt == 0){
				//평가 문제 정보 수정
				examAdmDAO.updateTzExamBankUseMaInfo(commandMap);
				examAdmDAO.deleteExamBankPaperSheet(commandMap);
				examAdmDAO.deleteExamBankPaper(commandMap);
				isok = true;
			}
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	//출제된적이 있는가
	public int selectExamBankResultCount(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankResultCount(commandMap);
	}
	
	
	/**
	 * 문제 엑셀 업로드
	 */
	public void insertExamBankFileToDB(List result, Map<String, Object> commandMap)
			throws Exception {
		
		HashMap<String, Object> inputMap = new HashMap<String, Object>();
		String p_select = (String)commandMap.get("p_select"); 
		if(p_select.equals("1")){
			for(int i=0; i<result.size(); i++){
				
				Map resultMap = (Map)result.get(i);	
				
				int v_usecnt = resultMap.get("parameter16") !=null && !"".equals(resultMap.get("parameter16")) ? Integer.parseInt(resultMap.get("parameter16").toString()) : 0;
				
				inputMap.put("v_subj", commandMap.get("p_subj")	);				
				inputMap.put("v_examtype", resultMap.get("parameter0"));
				inputMap.put("v_examtext", resultMap.get("parameter1"));				
				inputMap.put("v_exptext", resultMap.get("parameter2"));
				inputMap.put("v_selcount", resultMap.get("parameter3"));
				
				inputMap.put("v_examiner", resultMap.get("parameter10"));
				inputMap.put("v_levels", resultMap.get("parameter11"));
				inputMap.put("v_lessonnum", resultMap.get("parameter12"));
				//inputMap.put("v_regyear", resultMap.get("parameter13"));
				//inputMap.put("v_firstyear", resultMap.get("parameter14"));
				//inputMap.put("v_lastyear", resultMap.get("parameter15"));
				inputMap.put("v_usecnt", v_usecnt);
				inputMap.put("v_isuse", resultMap.get("parameter13"));	
				inputMap.put("v_userid", commandMap.get("userid"));
				
				//문제등록
				examAdmDAO.insertExamBankExcelToDB(inputMap);
				int selcount = resultMap.get("parameter3") !=null ? Integer.parseInt(resultMap.get("parameter3").toString()) : 0;
				
				String[] sel = new String[5];
								
				sel[0] = resultMap.get("parameter4") != null ? resultMap.get("parameter4").toString() : "";				
				sel[1] = resultMap.get("parameter5") != null ? resultMap.get("parameter5").toString() : "";				
				sel[2] = resultMap.get("parameter6") != null ? resultMap.get("parameter6").toString() : "";				
				sel[3] = resultMap.get("parameter7") != null ? resultMap.get("parameter7").toString() : "";				
				sel[4] = resultMap.get("parameter8") != null ? resultMap.get("parameter8").toString() : "";				
				
				int isanswer = resultMap.get("parameter9") != null ? Integer.parseInt(resultMap.get("parameter9").toString()) : 0;
				
				if(selcount > 0){
					int selnum = 0;
					for(int ii=0; ii < selcount; ii++){
						
						String v_isanswer = "N";
						selnum = selnum+1;
						
						inputMap.put("v_selnum", selnum);
						inputMap.put("v_seltext", sel[ii]);
						
						if(selnum == isanswer){
							v_isanswer = "Y";
						}else{
							v_isanswer = "N";
						}						
						
						inputMap.put("v_isanswer", v_isanswer);
						//보기등록
						examAdmDAO.insertExamBankBogiExcelToDB(inputMap);
						
					}
				}
			}
		}/*else if(p_select.equals("2")){
			for(int i=0; i<result.size(); i++){
				Map resultMap = (Map)result.get(i);
			
				inputMap.put("v_subj", resultMap.get("parameter0"));
				inputMap.put("v_examnum", resultMap.get("parameter1"));
				inputMap.put("v_selnum", resultMap.get("parameter2"));
				inputMap.put("v_seltext", resultMap.get("parameter3"));
				inputMap.put("v_isanswer", resultMap.get("parameter4"));
				inputMap.put("v_userid", commandMap.get("userid"));
				
				examAdmDAO.insertQeustBogiExcelToDB(inputMap);
			}
		}*/
	}
	
	/**
	 * 평가문제 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectExamBankView(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankView(commandMap);
	}
	
	
	/**
	 * 평가문제 해당 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamBankSelList(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankSelList(commandMap);
	}
	
	/**
	 * 평가문제수정
	 * @param commandMap
	 * @return -1 : 사용중인 문항임
	 * @return -2 : 에러
	 * @return  0 : 정상종료
	 * @throws Exception
	 */
	public int updateTzExamBank(Map<String, Object> commandMap) throws Exception{
//		return examAdmDAO.updateTzExam(commandMap);
		int rtn = -2;
		
		//문제분류
		
		String p_examnum = commandMap.get("p_examnum")+"";
		String p_examtype = commandMap.get("p_examtype")+"";
		String p_subj = commandMap.get("p_subj")+"";
		String p_isuse = commandMap.get("p_isuse")+"";
		String p_levels = commandMap.get("p_levels")+"";
		String p_examtext = commandMap.get("p_examtext")+"";
		String p_exptext = commandMap.get("p_exptext")+"";
		String p_userid = commandMap.get("userid")+"";
		
		String p_examiner = commandMap.get("p_examiner")+"";
		String p_regyear = commandMap.get("p_regyear")+"";
		String p_firstyear = commandMap.get("p_firstyear")+"";
		String p_lastyear = commandMap.get("p_lastyear")+"";
		String p_usecnt = commandMap.get("p_usecnt")+"";
		String p_lessonnum = commandMap.get("p_lessonnum")+"";
		
		
		
		try {
			
			
			HashMap<String, Object> exMap = new HashMap<String, Object>();
			exMap.put("v_examnum", p_examnum);
			exMap.put("p_subj", p_subj);
			
			//문항이 사용되어 졌는지를 검사한다.
			int cnt =  0; 	//examAdmDAO.selectExamQuestResultCount(exMap);
			
			
			if(cnt > 0)
			{
				rtn = -1;
			}
			else
			{
				//보기 답안 모두 삭제하기
				cnt = examAdmDAO.deleteExamSel(exMap);
				
				
				System.out.println("cnt --> "+cnt);
				
				
				//추가했던과정코드 삭제
				exMap.remove("p_subj");
						
						
				/*if(cnt > 0)
				{*/
					exMap.put("v_examtype", p_examtype);
					exMap.put("v_subj", p_subj);
					exMap.put("v_isuse", p_isuse);
					exMap.put("v_levels", p_levels);
					exMap.put("v_examtext", p_examtext);
					exMap.put("v_exptext", p_exptext);
					exMap.put("v_userid", p_userid);
					
					exMap.put("v_examiner", p_examiner);
					exMap.put("v_regyear", p_regyear);
					exMap.put("v_firstyear", p_firstyear);
					exMap.put("v_lastyear", p_lastyear);
					exMap.put("v_usecnt", p_usecnt);
					exMap.put("v_lessonnum", p_lessonnum);
					
					
					if(p_examtype.equals(ExamBean.OBJECT_QUESTION))	//객관식
					{
						//답안갯수 및 답안 정보를 list객체에 넣는다.
						List list = this.selectQuestExamSelCountList(commandMap, 10, ExamBean.OBJECT_QUESTION);
						
						exMap.put("v_selcount", list.size());
						examAdmDAO.updateTzExamBank(exMap); 	//문제수정
						
						//답안등록
						this.insertQuestExamSel(p_examnum, p_subj, p_userid, list);
					}
					
					
					else if(p_examtype.equals(ExamBean.SUBJECT_QUESTION))	//주관식
					{
						//답안갯수 및 답안 정보를 list객체에 넣는다.
						List list = this.selectQuestExamSelCountList(commandMap, 10, ExamBean.SUBJECT_QUESTION);
						
						exMap.put("v_selcount", list.size());
						examAdmDAO.updateTzExamBank(exMap); 	//문제수정
						
						//답안등록
						this.insertQuestExamSel(p_examnum, p_subj, p_userid, list);
					}
					
					
					else if(p_examtype.equals(ExamBean.OX_QUESTION))	//ox
					{
						//답안갯수 및 답안 정보를 list객체에 넣는다.
						List list = this.selectQuestExamSelCountList(commandMap, 2, ExamBean.OX_QUESTION);
						
						exMap.put("v_selcount", list.size());
						examAdmDAO.updateTzExamBank(exMap); 	//문제수정
						
						//답안등록
						this.insertQuestExamSel(p_examnum, p_subj, p_userid, list);
					}
					
					rtn = 0;
				/*}*/
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			rtn = -2;
		}
		
		return rtn;
	}
	
	
	public boolean deleteExamBankCheckList(Map<String, Object> commandMap) throws Exception {
		
		
		boolean isok = false;
		try {
			
			String p_subj   = commandMap.get("p_subj")+"";
	        String[] v_checks = EgovStringUtil.getStringSequence(commandMap, "p_checks");
	        
	        
	        
			for ( int i = 0; i < v_checks.length; i++ ) { 
				HashMap mm = new HashMap();
				mm.put("p_subj", p_subj);
				mm.put("v_examnum", v_checks[i]);
            	//문제가 사용되었는지 확인
            	int cnt = examAdmDAO.selectExamBankNumResultCount(mm);
            	
            	if(cnt == 0)
            	{
            		int exCnt = examAdmDAO.deleteExam(mm);
            		if(exCnt > 0) examAdmDAO.deleteExamSel(mm);
            	}else{
            		return isok;
            	}
			}
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	/**
	 * 평가문제지 문제 삭제 후 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteExamBankPaperSheetExam(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		String p_exam_subj = commandMap.get("p_exam_subj").toString();
		String v_examnum = commandMap.get("p_examnum").toString();
		
		try { 
			commandMap.put("p_subj", p_exam_subj);
			commandMap.put("v_examnum", v_examnum);
			
			//문제가 사용되었는지 확인
			int cnt = examAdmDAO.selectExamBankNumResultSubjseqCount(commandMap);
			
			if(cnt == 0){
				//평가 문제 정보 수정				
				examAdmDAO.insertExamBankPaperSheetExam(commandMap);
				examAdmDAO.deleteExamBankPaperSheetExam(commandMap);
				isok = true;
			}

		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	
	/**
	 * 평가문제지 문제 삭제 후 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteExamBankPaperSheetExamChange(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		String p_exam_subj = commandMap.get("p_exam_subj").toString();
		String v_examnum = commandMap.get("p_examnum").toString();
		
		try { 
			commandMap.put("p_subj", p_exam_subj);
			commandMap.put("v_examnum", v_examnum);
			
			//문제가 사용되었는지 확인
			int cnt = examAdmDAO.selectExamBankNumResultSubjseqCount(commandMap);
			
			if(cnt == 0){
				//평가 문제 정보 수정
				examAdmDAO.deleteExamBankPaperSheetExam(commandMap);
				examAdmDAO.insertExamBankPaperSheetExamChange(commandMap);
				isok = true;
			}
			
		} catch ( Exception ex ) { 
        	ex.printStackTrace();
        }

        return isok;
	}
	
	
	/**
	 * 학습자 재응시 횟수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamBankNumResultSubjseqCount(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankNumResultSubjseqCount(commandMap);
	}
	
	/**
	 * 교체할 문제가 있는가?
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectExamBankPaperSheetExamCount(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankPaperSheetExamCount(commandMap);
	}
	
	/**
	 * 평가문제은행 다운로드
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectExamBankExcelDownList(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankExcelDownList(commandMap);
	}
	
	//출제가능한 문제
	public Map<?, ?> selectExamBankLevelsCnt(Map<String, Object> commandMap) throws Exception{
		return examAdmDAO.selectExamBankLevelsCnt(commandMap);
	}
	
	//문제 미리보기
	public List selectExamBankPaperExamViewList(Map<String, Object> commandMap) throws Exception{
		
		List examBankPaperExamViewList = null;	
        examBankPaperExamViewList = examAdmDAO.selectExamBankPaperExamViewList(commandMap);		
		return examBankPaperExamViewList;
	}
	
	
}

