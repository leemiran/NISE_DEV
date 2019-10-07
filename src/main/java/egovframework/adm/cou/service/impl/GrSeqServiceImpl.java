package egovframework.adm.cou.service.impl;

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

import egovframework.adm.cou.dao.GrSeqDAO;
import egovframework.adm.cou.service.GrSeqService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.adm.fin.dao.FinishManageDAO;


@Service("grSeqService")
public class GrSeqServiceImpl extends EgovAbstractServiceImpl implements GrSeqService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="grSeqDAO")
    private GrSeqDAO grSeqDAO;
	
	@Resource(name="finishManageDAO")
    private FinishManageDAO finishManageDAO;
	
	/**
	 * 기수목록 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqList(Map<String, Object> commandMap)
			throws Exception {
		return grSeqDAO.selectGrSeqList(commandMap);
	}

	/**
	 * 지정된 기수/코스가 없는 교육차수Record Add
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqEmptyList(Map<String, Object> commandMap)
			throws Exception {
		return grSeqDAO.selectGrSeqEmptyList(commandMap);
	}
	
	/**
	 * 기수상세화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectGrSeqView(Map<String, Object> commandMap) throws Exception {
		return grSeqDAO.selectGrSeqView(commandMap);
	}
	
	/**
	 * 기수 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertSubjSeq(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
			String p_grseq = (String)grSeqDAO.insertGrSeq(commandMap);
			commandMap.put("p_grseq", p_grseq);
			
			
			String p_makeoption = (String)commandMap.get("p_makeoption");
			String p_package = (String)commandMap.get("p_package");
			
			
			log.info(this.getClass().getName() + " p_makeoption : " + p_makeoption);
			
			
			//기수생성이 MAKE_ALL, SELECT_ALL 일때만 진행한다.
			if(p_makeoption.equals("MAKE_ALL") || p_makeoption.equals("SELECT_ALL"))
			{
				List list = grSeqDAO.selectGrSeqMakeOption(commandMap);
				
				log.info(this.getClass().getName() + " selectGrSeqMakeOption list count() : " + list.size());
				
				for(int i=0; i<list.size(); i++)
				{
					Map mm = (Map)list.get(i);
//					과정코드 
					commandMap.put("p_subj", mm.get("subjcourse"));
					
//					기수생성 프로세스 전체
					this.createAllSubjSeq(commandMap);
					
				}
				
			}
			else if(p_package != null && p_package.equals("Y"))				//패키지 과정일때 저장한다.
			{
				grSeqDAO.insertCourseSeq(commandMap);
			}
			
			isok = true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
	}
	
//	기수생성 프로세스
	public boolean createAllSubjSeq(Map<String, Object> commandMap) throws Exception {
		
		boolean isok = false;
		
		try{
	//		기수가 존재하는지를 체크하는 카운트 쿼리
			Integer p_subjseqcnt = grSeqDAO.selectSubjSeqCnt(commandMap);
			commandMap.put("p_subjseqcnt", p_subjseqcnt);
			
	//		기수등록
			Object obj = grSeqDAO.insertSubjSeq(commandMap);
			
			log.info(this.getClass().getName() + " : subjseq = " + obj);
			
			if(obj != null)
			{
	//			기수코드등록
				commandMap.put("p_subjseq", obj);
				
	//			게시판 등록
				commandMap.put("p_type", "SD");
				commandMap.put("p_subj_bbs_title", "과정 " + commandMap.get("p_subjseq") + "차수 자료실");
				grSeqDAO.insertSubjBbs(commandMap);
				
				commandMap.put("p_type", "SQ");
				commandMap.put("p_subj_bbs_title", "과정 " + commandMap.get("p_subjseq") + "차수 질문방");
				grSeqDAO.insertSubjBbs(commandMap);
				
				commandMap.put("p_type", "SB");
				commandMap.put("p_subj_bbs_title", "과정 " + commandMap.get("p_subjseq") + "차수 게시판");
				grSeqDAO.insertSubjBbs(commandMap);
				
				//Report 출제정보 Copy tz_projgrp 등록
				grSeqDAO.insertProjGrp(commandMap);
	
				//Report 출제정보 Copy tz_projmap 등록
				grSeqDAO.insertProjMap(commandMap);
	
				//시험지정보 등록
				grSeqDAO.insertExamPaper(commandMap);
				
			}
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	
	/**
	 * 교육 기수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateGrSeq(Map<String, Object> commandMap) throws Exception{
		int isOk = 0;
		try{
			//기수수정
			isOk = grSeqDAO.updateGrSeq(commandMap);
			
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	
	/**
	 * 교육 기수, 기수 전체 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteGrSeq(Map<String, Object> commandMap) throws Exception{
		int isOk = 0;
		try{
//			과정삭제
			grSeqDAO.deleteCourseSeq(commandMap);
			
//			기수전체삭제
			this.deleteSubjSeq(commandMap);
			
//			교육기수삭제
			grSeqDAO.deleteGrSeq(commandMap);
			
			isOk = 1;
			
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	
	/**
	 * 기수 전체 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSubjSeq(Map<String, Object> commandMap) throws Exception{
		
		boolean isOk = false;
		
		try{
			
			ArrayList<String>   sqlArr          = new ArrayList<String>();

			sqlArr.add("TZ_MASTERSUBJ"		); // 마스터과목 매칭
			sqlArr.add("tz_progress"        ); // 진도
	        // sqlArr.add("tz_progressobj"  );
	        // sqlArr.add("tz_progress_sco" );
	        // sqlArr.add("tz_vodprogress"  );
	        sqlArr.add("tz_projgrp"         );//리포트 문제지
	        sqlArr.add("tz_projmap"         );//리포트 문제매핑         
	        sqlArr.add("tz_projrep"         );//리포트 제출
	        sqlArr.add("tz_exampaper"       );//평가 문제지         
	        sqlArr.add("tz_examresult"      );//평가 결과
	        // sqlArr.add("tz_activity_ans" );
	        // sqlArr.add("tz_opin"         );
	        // sqlArr.add("tz_expect"       );
	        sqlArr.add("tz_gong"            );//공지
	        sqlArr.add("tz_toron"           );//토론방
	        sqlArr.add("tz_torontp"         );//토론의견
	        // sqlArr.add("tz_qna"          );
	        // sqlArr.add("tz_objqna"       );
	        sqlArr.add("tz_class"           );//분반
	        sqlArr.add("tz_classtutor"      );//강사분반
	        // sqlArr.add("tz_sulresult"    );
	        // sqlArr.add("tz_sulresultp"   );
	        sqlArr.add("tz_suleach"         );//설문결과
	        // sqlArr.add("tz_bookmark"     );
	        sqlArr.add("tz_propose"         );//수강신청정보
	        sqlArr.add("tz_student"         );//수강생정보
	        sqlArr.add("tz_stold"           );//수료정보
	        // sqlArr.add("tz_subjseqtemp"  );
	        sqlArr.add("tz_subjseq"         );//과정차수정보
	        sqlArr.add("tz_offsubjlecture"  );//집합과정정보
	        sqlArr.add("tz_bds"  			 );//게시판마스터  
	        sqlArr.add("tz_board"  		 );//게시판            

	     // 관련 테이블 삭제
            for ( int i = 0; i<sqlArr.size(); i++ ) {
            	
            	
            	commandMap.put("p_dynamic_table_name", (String)sqlArr.get(i));
            	
	//			기수관련 테이블 모두 삭제
				grSeqDAO.deleteSubjSeqAll(commandMap);
            }
			
			isOk = true;
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 해당교육기수의 수강생이 존재하는지를 체크한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectProposeCnt(Map<String, Object> commandMap) throws Exception{
		return grSeqDAO.selectProposeCnt(commandMap);
	}
	
	
	
	/**
	 * 교육기수에 수강, 신청, 승인, 취소, 수료자 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqStudentList(Map<String, Object> commandMap) throws Exception{
		
		String p_gubun = (String)commandMap.get("p_gubun");
		List<?> list = null;
		
		//신청
		if(p_gubun.equals("propose"))
		{
			list = grSeqDAO.selectGrSeqStudentProposeList(commandMap);
		}
		//취소
		else if(p_gubun.equals("cancel"))
		{
			list = grSeqDAO.selectGrSeqStudentCancelList(commandMap);
		}
		//승인
		else if(p_gubun.equals("student"))
		{
			list = grSeqDAO.selectGrSeqStudentStudentList(commandMap);
		}
		//수료
		else if(p_gubun.equals("stold"))
		{
			list = grSeqDAO.selectGrSeqStudentStoldList(commandMap);
		}
		
		return list;
		
		
	}
	
	
	
	/**
	 * 교육기수 상세 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqDetailList(Map<String, Object> commandMap) throws Exception{
		return grSeqDAO.selectGrSeqDetailList(commandMap);
	}

	
	
	/**
	 * 기수 - 출석고사장 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectGrSeqSchoolList(Map<String, Object> commandMap) throws Exception{
		return grSeqDAO.selectGrSeqSchoolList(commandMap);
	}

	
	/**
	 * 기수 상세정보 - 수정화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectGrSeqDetailView(Map<String, Object> commandMap) throws Exception {
		return grSeqDAO.selectGrSeqDetailView(commandMap);
	}
	
	/**
	 * 현재새로운기수 수정시 기수가 존재하는지를 체크한다. 
	 * @param commandMap
	 * @return 기수의 개수를 반환한다.
	 * @throws Exception
	 */
	public int selectCheckSubjseq(Map<String, Object> commandMap) throws Exception {
		return grSeqDAO.selectCheckSubjseq(commandMap);
	}
	
	/**
	 * 단일 기수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateSubjseq(Map<String, Object> commandMap) throws Exception{
		boolean isOk = false;
		
		try{
			//출석고사장 체크하기
			String p_neweroom = "";
			
			
			String []  vec = (String[]) commandMap.get("_Array_p_eroomseq");
	        if(vec != null)
	        {
		        for(int p=0; p < vec.length; p++){
		        	
		        	p_neweroom = p_neweroom + vec[p] + ",";
		        }
	        }
	        
	        commandMap.put("p_neweroom", p_neweroom);
	        
	        log.info("p_neweroom : " + p_neweroom);
	        
			
			int cnt = grSeqDAO.updateSubjseq(commandMap);
			
			// 2017 추가
			// 지역 등록
			// 전체 삭제
			if(2017 <= Integer.parseInt(commandMap.get("p_year").toString())) {
				grSeqDAO.deleteSubjseqArea(commandMap);
				
				Map<String, Object> insertMap = new HashMap<String, Object>();
				insertMap.put("p_subj", commandMap.get("p_subj"));
				insertMap.put("p_year", commandMap.get("p_year"));
				insertMap.put("p_subjseq", commandMap.get("p_subjseq"));
				for(String areaCode: (String[]) commandMap.get("arrAreaCode")) {
					// 등록
					insertMap.put("areaCode", areaCode);
					grSeqDAO.insertSubjseqArea(insertMap);
				}
			}
			// 2017 추가 끝
			
			if(commandMap.get("p_newsubjseq") != null && !commandMap.get("p_newsubjseq").equals(""))
			{
				int subjseq = Integer.parseInt(commandMap.get("p_subjseq") + "");
	            int newsubjseq = Integer.parseInt(commandMap.get("p_newsubjseq") + "");
	           
	            if (subjseq != newsubjseq) {
	            	
	            	//LPAD 4자리수..
	            	 String result = commandMap.get("p_newsubjseq")+"";
	            	 int templen   = 4 - result.length();
	            	 
	            	 for (int i = 0; i < templen; i++){
	                     result = "0" + result;
	            	 }
	               
	            	 
	            	commandMap.put("p_subjseq", result);
	            	
		//			게시판 등록
					commandMap.put("p_type", "SD");
					commandMap.put("p_subj_bbs_title", "과정 " + commandMap.get("p_subjseq") + "차수 자료실");
					grSeqDAO.insertSubjBbs(commandMap);
					
					commandMap.put("p_type", "SQ");
					commandMap.put("p_subj_bbs_title", "과정 " + commandMap.get("p_subjseq") + "차수 질문방");
					grSeqDAO.insertSubjBbs(commandMap);
					
					commandMap.put("p_type", "SB");
					commandMap.put("p_subj_bbs_title", "과정 " + commandMap.get("p_subjseq") + "차수 게시판");
					grSeqDAO.insertSubjBbs(commandMap);
					
					//Report 출제정보 Copy tz_projgrp 등록
					grSeqDAO.insertProjGrp(commandMap);
		
					//Report 출제정보 Copy tz_projmap 등록
					grSeqDAO.insertProjMap(commandMap);
		
					//시험지정보 등록
					grSeqDAO.insertExamPaper(commandMap);
	            }
			}
			
			
			if(cnt > 0) isOk = true;
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isOk;
		
	}
	
	/**
	 * 교육기수명 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectGrSeqName(Map<String, Object> commandMap) throws Exception {
		return grSeqDAO.selectGrSeqName(commandMap);
	}
	
	
	/**
	 * 교육기수 일괄 수정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUpdateSubjCourseList(Map<String, Object> commandMap) throws Exception{
		return grSeqDAO.selectUpdateSubjCourseList(commandMap);
	}
	
	/**
	 * 교육기수 일괄 지정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectAssignSubjCourseList(Map<String, Object> commandMap) throws Exception{
		return grSeqDAO.selectAssignSubjCourseList(commandMap);
	}
	
	
	/**
	 * 일일 학습제한 저장하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateEdulimit(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
			String p_edulimit = (String) commandMap.get("p_edulimit");
			String userid = (String) commandMap.get("userid");
			
			HashMap<String, Object> mm = new HashMap<String, Object>();
        	mm.put("p_edulimit", p_edulimit);
        	mm.put("userid", userid);
        	
			String []  vec_chk = (String[]) commandMap.get("_Array_p_chk");
			
	        if(vec_chk != null)
	        {
		        for(int i=0; i < vec_chk.length; i++){
		        	String chk                 = vec_chk[i];
		        	
		        	StringTokenizer st  = new StringTokenizer(chk, ",");
	                String p_subj              = st.nextToken();
	                String p_year              = st.nextToken();
	                String p_subjseq           = st.nextToken();
		        	
		        	mm.put("p_subj", p_subj);
		        	mm.put("p_year", p_year);
		        	mm.put("p_subjseq", p_subjseq);
		        	
		        	
		        	int cnt = grSeqDAO.updateEdulimit(mm);
		        	
		        	log.info(this.getClass().getName() + " updateEdulimit cnt : " + cnt);
		        	
		        }
		        isok = true;
	        }
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
	}
	
	
	/**
	 * 일일 수강신청기간 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updatePropose(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
			String p_propstart = (String) commandMap.get("p_propstart");
			String p_propend = (String) commandMap.get("p_propend");
			
			String userid = (String) commandMap.get("userid");
			
			HashMap<String, Object> mm = new HashMap<String, Object>();
			mm.put("p_propstart", p_propstart);
			mm.put("p_propend", p_propend);
        	mm.put("userid", userid);
        	
			String []  vec_chk = (String[]) commandMap.get("_Array_p_chk");
			
	        if(vec_chk != null)
	        {
		        for(int i=0; i < vec_chk.length; i++){
		        	String chk                 = vec_chk[i];
		        	
		        	StringTokenizer st  = new StringTokenizer(chk, ",");
	                String p_subj              = st.nextToken();
	                String p_year              = st.nextToken();
	                String p_subjseq           = st.nextToken();
		        	
		        	mm.put("p_subj", p_subj);
		        	mm.put("p_year", p_year);
		        	mm.put("p_subjseq", p_subjseq);
		        	
		        	
		        	int cnt = grSeqDAO.updatePropose(mm);
		        	
		        	log.info(this.getClass().getName() + " updatePropose cnt : " + cnt);
		        	
		        }
		        isok = true;
	        }
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
	}
	
	/**
	 * 일일 학습기간 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateEdu(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
			String p_edustart = (String) commandMap.get("p_edustart");
			String p_eduend = (String) commandMap.get("p_eduend");
			
			String userid = (String) commandMap.get("userid");
			
			HashMap<String, Object> mm = new HashMap<String, Object>();
			mm.put("p_edustart", p_edustart);
			mm.put("p_eduend", p_eduend);
        	mm.put("userid", userid);
        	
			String []  vec_chk = (String[]) commandMap.get("_Array_p_chk");
			
	        if(vec_chk != null)
	        {
		        for(int i=0; i < vec_chk.length; i++){
		        	String chk                 = vec_chk[i];
		        	
		        	StringTokenizer st  = new StringTokenizer(chk, ",");
	                String p_subj              = st.nextToken();
	                String p_year              = st.nextToken();
	                String p_subjseq           = st.nextToken();
		        	
		        	mm.put("p_subj", p_subj);
		        	mm.put("p_year", p_year);
		        	mm.put("p_subjseq", p_subjseq);
		        	
		        	
		        	int cnt = grSeqDAO.updateEdu(mm);
		        	
		        	log.info(this.getClass().getName() + " updateEdu cnt : " + cnt);
		        	
		        }
		        isok = true;
	        }
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
	}
	
	/**
	 * 일일 학습제한 교육기수(GrSeq) 전체 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateGrSeqEdulimit(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
        	int cnt = grSeqDAO.updateGrSeqEdulimit(commandMap);
        	
        	log.info(this.getClass().getName() + " updateGrSeqEdulimit cnt : " + cnt);
	        	
	        isok = true;
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
	}
	
	
	/**
	 * 기수 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean assignSave(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
			String []  vec_chk = (String[]) commandMap.get("_Array_p_chk");
			
	        if(vec_chk != null)
	        {
		        for(int i=0; i < vec_chk.length; i++){
		        	String chk                 = vec_chk[i];
//					과정코드 
					commandMap.put("p_subj", chk);
					
//					기수생성 프로세스 전체
					this.createAllSubjSeq(commandMap);
					
				}
		        isok = true;
			}	
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
	}

	// 2017 추가
	public List<?> selectAreaCodeList() throws Exception {
		return grSeqDAO.selectAreaCodeList();
	}

	public String selectSubjseqAreaCodeConcat(Map<String, Object> commandMap) throws Exception {
		return grSeqDAO.selectSubjseqAreaCodeConcat(commandMap);
	}
	// 2017 추가 끝
	
	/**
	 * 과정 복습 / 수료처리  여부 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSubjseqReviewIsclosed(Map<String, Object> commandMap) throws Exception{
		int isOk = 0;
		try{		
			//기수 복습 수정하기
			isOk += grSeqDAO.updateSubjseqReviewIsclosed(commandMap);
			
			String p_isclosed = (String)commandMap.get("p_isclosed");
			if("N".equals(p_isclosed)){
				//수료정보 삭제
				finishManageDAO.deleteStoldTable(commandMap);		
				//수료번호 update
				grSeqDAO.deleteStudentSerno(commandMap);			
				
			}
			
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
}
