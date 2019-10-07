package egovframework.adm.fin.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ziaan.library.CalcUtil;
import com.ziaan.library.SQLString;

import egovframework.adm.fin.dao.FinishManageDAO;
import egovframework.adm.fin.service.FinishManageService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.apache.log4j.Logger;

@Service("finishManageService")
public class FinishManageServiceImpl extends EgovAbstractServiceImpl implements FinishManageService{
	
	@Resource(name="finishManageDAO")
    private FinishManageDAO finishManageDAO;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public List selectFinishCourseList(Map<String, Object> commandMap) throws Exception{
		List list = finishManageDAO.selectFinishCourseList(commandMap);
		List result = new ArrayList();
		if( list != null && list.size() > 0 ){
			String Bcourse = "";
			String Bcourseseq = "";
			for(int i=0; i<list.size(); i++){
				Map inputMap = (Map)list.get(i);
				if( Integer.valueOf(inputMap.get("studentcnt").toString()) > 0 ){
					inputMap.put("ses_search_gyear", commandMap.get("ses_search_gyear"));
					inputMap.put("ses_search_grseq", commandMap.get("ses_search_grseq"));
					Map tmp = new HashMap();
					if( inputMap.get("course") != null && !inputMap.get("course").toString().equals("000000") && 
							!(Bcourse.equals((String)inputMap.get("course")) && Bcourseseq.equals((inputMap.get("courseseq")))) ){
						tmp = finishManageDAO.selectPackage(inputMap);
						tmp.put("isnewcourse", "Y");
					}else{
						tmp.put("rowspan", 0);
						tmp.put("cnt", 0);
						tmp.put("isnewcourse", "N");
					}
					Bcourse = (String)inputMap.get("course");
					Bcourseseq = (String)inputMap.get("courseseq");
					inputMap.putAll(tmp);
					result.add(inputMap);
				}
			}
		}
		return result;
	}
	
	public List selectFinishStudentList(Map<String, Object> commandMap) throws Exception{
		if( commandMap.get("p_isclosed").toString().equals("Y") && commandMap.get("p_mgubun").toString().equals("0") ){
			commandMap.put("selectTable", "A");
		}else{
			commandMap.put("selectTable", "B");
		}
		List result = new ArrayList();
		List tmp = new ArrayList();
		if( commandMap.get("p_isonoff").toString().equals("RC") ){
			List list = finishManageDAO.selectFinishStudentList(commandMap);
			if( list != null && list.size() > 0 ){
				for( int i=0; i<list.size(); i++ ){
					List list2 = new ArrayList();
					List list3 = new ArrayList();
					Map map = (Map)list.get(i);
					tmp = finishManageDAO.selectBookList(map);
					if( tmp != null && tmp.size() > 0 ){
						for(int j=0; j<tmp.size(); j++){
							Map m = (Map)tmp.get(j);
							list2.add(m);
						}
						map.put("bookexam_result", list2);
					}
					result.add(map);
				}
			}
		}else{
			result = finishManageDAO.selectExamList(commandMap);
		}
		return result;
	}
	
	public Map SelectSubjseqInfoDbox(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.SelectSubjseqInfoDbox(commandMap);
	}
	
	public List ScoreCntList(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.ScoreCntList(commandMap);
	}
	
	public int getCntBookMonth(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.getCntBookMonth(commandMap);
	}
	
	public int graduatedUpdate(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			String sserno = "";
			if( commandMap.get("p_isgraduated").toString().equals("Y") ){
				Object tmp = finishManageDAO.getCompleteSerno(commandMap);
				if( tmp != null && !tmp.toString().equals("") ){
					sserno = (String)tmp;
				}else{
					sserno = finishManageDAO.getMaxCompleteCode(commandMap);
				}
			}
			commandMap.put("p_sserno", sserno);
			finishManageDAO.graduatedUpdate(commandMap);
			if( commandMap.get("p_isgraduated").toString().equals("Y") ){
				finishManageDAO.updateStudentSerno(commandMap);
			}
			finishManageDAO.updateStudentIsgraduated(commandMap);
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		return isOk; 
	}
	
	/**
	 * 수료처리 취소
	 */
	public int subjectCompleteCancel(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		
		try{
			//수료정보 삭제
			finishManageDAO.deleteStoldTable(commandMap);
			
			// 수료 필드 수정 - isclosed = 'Y'
        	commandMap.put("p_isclosed", "N");
        	finishManageDAO.setCloseColumn(commandMap);
        	
        	// 외주과목 최종확인 N
        	commandMap.put("p_iscpflag", "N");
            finishManageDAO.updateIsCpflag(commandMap);
            
            commandMap.put("p_isgraduated", "N");
            finishManageDAO.updateStudentIsgraduated(commandMap);
			
            /**
        	 * tz_strout
        	 * 강 제약 조건 (해당기수) 등록자 삭제  처리안됨
        	 * afbean.deleteSubjseqStrOut(connMgr, v_subj, v_year, v_subjseq);
        	 * FinishBean.java 
        	 * 2693 Line
        	 */
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return isOk;
	}
	
	public int updateOutSubjReject(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.updateOutSubjReject(commandMap);
	}
	
	/**
	 * 수료처리
	 */
	public String completeUpdate(Map<String, Object> commandMap) throws Exception{
		String resultMsg = "수료처리가 완료되었습니다.";
		try{
			//기간이 지난 수강신청 제약조건 삭제
			finishManageDAO.deleteStroutProc(commandMap);
			finishManageDAO.deleteStroutYear(commandMap);
			
			
			String process = (String)commandMap.get("p_process");
			if(process.equals("subjectComplete")){
				// 수료처리 완료여부, 학습중 검토
				Map subjInfo = finishManageDAO.SelectSubjseqInfoDbox(commandMap);
				
				logger.info("subjInfo : " + subjInfo);
				
				if( subjInfo.get("isclosed") != null && subjInfo.get("isclosed").equals("Y") ){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return "이미 수료처리 되었습니다.";
				}else if( Integer.valueOf(subjInfo.get("today").toString()) < Integer.valueOf(subjInfo.get("edustart").toString().substring(0, 8))){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return "학습시작후 가능합니다.";
				}
				
				//수료정보 삭제
				finishManageDAO.deleteStoldTable(commandMap);
				
				// 미채점 리포트 갯수 확인 -->온라인 과제로 대체 함 2010.05.20
				int v_remainReportcnt = finishManageDAO.chkRemainReport(commandMap);
				if ( v_remainReportcnt > 0 ) { 
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return "학습자의 리포트 중 " + String.valueOf(v_remainReportcnt) + " 개가 미채점되었습니다.";
                }
				
				commandMap.put("p_whtest", subjInfo.get("whtest"));
				commandMap.put("p_wftest", subjInfo.get("wftest"));
				commandMap.put("p_wmtest", subjInfo.get("wmtest"));
				commandMap.put("p_wreport", subjInfo.get("wreport"));
				commandMap.put("p_wetc1", subjInfo.get("wetc1"));
				commandMap.put("p_wetc2", subjInfo.get("wetc2"));
				
				
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
            	if( !subjInfo.get("contenttype").toString().equals("L") ){
            		finishManageDAO.updateCompleteStudent(commandMap);
            	}
            	
            	//수료정보 등록
            	finishManageDAO.insertStoldTable(commandMap);
            	
            	//수강생 수료증번호 발급
            	finishManageDAO.updateStudentSernoAll(commandMap);
            	
            	/**
            	 * tz_strout
            	 * 수강신청 제약정보등록 처리안됨
            	 * afbean.insertSubjseqStrOut(connMgr, box, v_userid, data.getName(), subjseqdata.getEduend().substring(0, 8), v_year, v_subj, v_subjseq );
            	 * FinishBean.java 
            	 * 2158 Line
            	 */
            	
            	
            	// 수료 필드 수정 - isclosed = 'Y'
            	commandMap.put("p_isclosed", "Y");
            	finishManageDAO.setCloseColumn(commandMap);
			}
		}catch(Exception ex){
			resultMsg = "실패하였습니다.";
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		return resultMsg;
	}
	
	public int subjectCompleteRerating(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			// 수료처리 완료여부, 학습중 검토
			Map subjInfo = finishManageDAO.SelectSubjseqInfoDbox(commandMap);
			
			logger.info(" subjInfo : " + subjInfo);
			
			if( subjInfo.get("isclosed") != null && subjInfo.get("isclosed").equals("Y") ){
				return -1; //이미 수료처리 되었습니다.
			}else if( Integer.valueOf(subjInfo.get("today").toString()) < Integer.valueOf(subjInfo.get("edustart").toString().substring(0, 8))){
				return -2; //학습시작후 가능합니다.
			}
			
			commandMap.put("p_whtest", subjInfo.get("whtest"));
			commandMap.put("p_wftest", subjInfo.get("wftest"));	//가중치(%) 온라인시험
			commandMap.put("p_wmtest", subjInfo.get("wmtest"));	//가중치(%) 출석시험
			commandMap.put("p_wreport", subjInfo.get("wreport"));	//가중치(%) 온라인과제
			commandMap.put("p_wetc1", subjInfo.get("wetc1"));	//수료기준 참여도(출석일)
			commandMap.put("p_wetc2", subjInfo.get("wetc2"));		//가중치 참여도(출석일)
			
			
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
        	if( !subjInfo.get("contenttype").toString().equals("L") ){
        		finishManageDAO.updateCompleteStudent(commandMap);
        	}
        	
        	finishManageDAO.updateRecalcudate(commandMap);
			
		}catch(Exception ex){
			isOk = -99;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public List suRoyJeungPrintList(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.suRoyJeungPrintList(commandMap);
	}
	
	
	public int subjectComplete3(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		String pattern = "######.##";
        DecimalFormat dformat = new DecimalFormat(pattern);
		try{
			//Editlink set
			finishManageDAO.updateStudentEditlink(commandMap);
			Vector verscore = new Vector();
			Vector vervar = new Vector();
			Vector verscore2 = new Vector();
			Vector vervar2 = new Vector();
			Vector verrslt = new Vector();
			Vector verrsltcnt = new Vector();
			
			//80점~90점까지 조견표
			List list = finishManageDAO.selectScoreVarList(commandMap);
			
			System.out.println("list.size() ---------> "+list.size());
			
			if( list != null ){
				for( int i=0; i<list.size(); i++ ){
					Map m = (Map)list.get(i);
					verscore2.add(m.get("score"));
					vervar2.add(m.get("var"));
					
					System.out.println("verscore2 score  ---------> "+m.get("score"));
					System.out.println("vervar2 var  ---------> "+m.get("var"));
					
				}
			}
			//tz_crt_var 
			List list2 = finishManageDAO.selectCrtVarList(commandMap);
			
			if( list2 != null ){
				for( int i=0; i<list2.size(); i++ ){
					Map m = (Map)list2.get(i);
					verrslt.add(m.get("verrslt"));
					verrsltcnt.add(m.get("cnt"));
					
					System.out.println("verrslt verrslt  ---------> "+m.get("verrslt"));	//null
					System.out.println("verrsltcnt cnt  ---------> "+m.get("cnt"));
					
				}
			}
			//tz_student 60이상
			int mem_cnt = finishManageDAO.selectStudentTotalCnt(commandMap);
			
			System.out.println("mem_cnt  ---------> "+mem_cnt);
			
			//tz_crt_var
			int vcnt = finishManageDAO.selectVarSumCnt(commandMap);
			
			System.out.println("vcnt  ---------> "+vcnt);
			
			
			int getTotRecCnt =mem_cnt;
			int get1stCnt = vcnt;
			int gap_num = 0;
			float max_num = 0.0F;
			float min_num = 0.0F;
			
			System.out.println("verscore2.size()  ---------> "+verscore2.size());			
			
			float rate[] = new float[verscore2.size()];
			int rnd[] = new int[verscore2.size()];
			
			//80점~90점까지 조견표			
			for(int i = 0; i < verscore2.size(); i++){
				rate[i] = Float.parseFloat(vervar2.elementAt(i).toString());	//조견표 점수의 값
				//(조견표값*(tz_student 60이상인원/100))+0.5
				rnd[i] = (int)((double)((Float.parseFloat(vervar2.elementAt(i).toString()) * (float)getTotRecCnt) / 100F) + 0.5D);
				
				System.out.println("rate[i] "+ i +" ---------> "+rate[i]);
				System.out.println("rnd[i] "+ i +"  ---------> "+rnd[i]);
				
			}
			
			if(rnd[10] == 0)
				rnd[10] = 1;
			get1stCnt = 0;
			for(int i = 0; i < 11; i++){
				get1stCnt += rnd[i] * 2;
				
				System.out.println("for get1stCnt ---------> "+get1stCnt);
				
			}
			
			System.out.println("get1stCnt ---------> "+get1stCnt);
			System.out.println("get1stCnt ---------> "+get1stCnt);
			
			
			get1stCnt -= rnd[10];
			
			System.out.println("get1stCnt 2222 ---------> "+get1stCnt);
			System.out.println("getTotRecCnt ---------> "+getTotRecCnt);
			
			
			int lst[] = new int[11];
			int snd[] = new int[11];
			
			if(get1stCnt == getTotRecCnt){
				for(int i = 0; i < 11; i++){
					lst[i] = rnd[i];
					
					System.out.println("rnd[i] ---------> "+rnd[i]);
							
				}
				
			} else{
				snd = new int[11];
				
				System.out.println("getTotRecCnt ---------> "+getTotRecCnt);
				System.out.println("get1stCnt ---------> "+get1stCnt);
				
				if((getTotRecCnt - get1stCnt) % 2 != 0){
					
					System.out.println("snd[10] ---------> "+snd[10]);
					
					snd[10] = snd[10] + 1;
					
					System.out.println("snd[10] 222---------> "+snd[10]);
					
					get1stCnt++;
					
					System.out.println("get1stCnt 222---------> "+get1stCnt);
					
				}
				float gap[] = new float[11];
				for(int i = 0; i < 10; i++){
					
					System.out.println("(float)rnd[i] "+ i +" ---------> "+(float)rnd[i]);
					System.out.println("rate[i] "+ i +" ---------> "+rate[i]);
					System.out.println("(float)getTotRecCnt)  "+ i +"  ---------> "+(float)getTotRecCnt);
					
					
					gap[i] = (float)rnd[i] - (rate[i] * (float)getTotRecCnt) / 100F;
					
					System.out.println("gap[i] ---------> "+gap[i]);
					
					
				}
				
				System.out.println("get1stCnt ---------> "+get1stCnt);
				System.out.println("getTotRecCnt ---------> "+getTotRecCnt);
				
				if(get1stCnt - getTotRecCnt > 0)
					
					for(; get1stCnt - getTotRecCnt > 0; get1stCnt -= 2){
						
						
						
						gap_num = 9;
						max_num = 0.0F;
						for(int i = 9; i >= 0; i--)
							if((gap[i] > max_num) & (snd[i] == 0)){
								max_num = gap[i];
								gap_num = i;
								
								System.out.println("max_num "+i+" ---------> "+max_num);
								System.out.println("gap_num "+i+"  ---------> "+gap_num);
								
							}
						
						snd[gap_num] = -1;
					}
				
				else
					for(; get1stCnt - getTotRecCnt < 0; get1stCnt += 2){
						gap_num = 9;
						min_num = 0.0F;
						for(int i = 9; i >= 0; i--)
							if((gap[i] < min_num) & (snd[i] == 0)){
								min_num = gap[i];
								gap_num = i;
								
								System.out.println("max_num "+i+" ---------> "+min_num);
								System.out.println("gap_num "+i+"  ---------> "+gap_num);
								
							}
						
						snd[gap_num] = 1;
					}
				
			}
			
			
			
			
			lst = new int[11];             
			for(int i = 0; i < 11; i++){
				lst[i] = rnd[i] + snd[i];
				
				System.out.println("rnd[i] ---------> "+rnd[i]);
				System.out.println("snd[i] ---------> "+snd[i]);
				
			}
			
			finishManageDAO.deleteLank(commandMap);
			int lank = 1;
			for(int i = 0; i <= 10; i++){
				for(int j = 1; j < lst[i]; j++){
					commandMap.put("lst", lst[i]);
					commandMap.put("score", 100 - i);
					finishManageDAO.setNum(commandMap);
					
					System.out.println("lst 100 i :"+ i +"  j :"+ j +"---------> "+commandMap.get("lst"));
					System.out.println("score 100 i :"+ i +"  j :"+ j +" ---------> "+commandMap.get("score"));
					
				}
				
				
				for(int j = 0; j < lst[i]; j++){
					commandMap.put("lank", lank++);
					commandMap.put("score", 100 - i);
					finishManageDAO.setLank(commandMap);
					
					System.out.println("lank 100  i :"+ i +"  j :"+ j +"---------> "+commandMap.get("lank"));
					System.out.println("score 100  i :"+ i +"  j :"+ j +" ---------> "+commandMap.get("score"));
					
				}
				
			}
			
			for(int i = 9; i >= 0; i--){
				for(int j = 1; j <= lst[i]; j++){
					commandMap.put("lst", lst[i]);
					commandMap.put("score", 80 + i);
					finishManageDAO.setNum(commandMap);
					
					System.out.println("lst 80  i :"+ i +"  j :"+ j +"---------> "+commandMap.get("lst"));
					System.out.println("lst 80  i :"+ i +"  j :"+ j +"---------> "+commandMap.get("lst"));
					
					
				}
				
				for(int j = 1; j <= lst[i]; j++){
					commandMap.put("lank", lank++);
					commandMap.put("score", 80 + i);
					finishManageDAO.setLank(commandMap);
					
					System.out.println("lank 80 i :"+ i +"  j :"+ j +" ---------> "+commandMap.get("lank"));
					System.out.println("score 80  i :"+ i +"  j :"+ j +"---------> "+commandMap.get("score"));
					
				}
				
			}
			
			
			// TZ_CRT_LOG_TBL 테이블 ( 연수성적 분포 조건표 테이블)을 읽어온다.
			list = finishManageDAO.selectScoreList(commandMap);
			if( list != null ){
				for(int i=0; i<list.size(); i++){
					Map m = (Map)list.get(i);
					verscore.add(m.get("score"));
					vervar.add(m.get("var"));
					
					System.out.println("verscore "+ i +" ---------> "+m.get("score"));
					System.out.println("vervar  "+ i +"  ---------> "+m.get("var"));
					
					
				}
			}
			int ch_cnt = finishManageDAO.selectCrtVarCount(commandMap);
			System.out.println("ch_cnt ---------> "+ch_cnt);
			
			if( ch_cnt > 0 ){
				finishManageDAO.deleteCrtVar(commandMap);
			}
			System.out.println(" verscore.size() ---------> "+ verscore.size());
			
			double d1 =0;
			double d2 =0;
			double d3 =0;
			// 1.2번의 데이터를 가지고 와서  TZ_CRT_VAR 조정표 테이블에 insert 한다.
			for(int i =0; i < verscore.size(); i++){
				d1 = Double.parseDouble(vervar.elementAt(i).toString());
				
				System.out.println(" d1 ---------> "+ d1);
				
				d2 = Double.parseDouble(vervar.elementAt(i).toString());
				
				System.out.println(" d2 ---------> "+ d2);
				
				d3 = Double.parseDouble(dformat.format(d2));  
				
				System.out.println(" d3 ---------> "+ d3);
				System.out.println(" score ---------> "+ verscore.elementAt(i).toString());
				System.out.println(" var ---------> "+ vervar.elementAt(i).toString());
				System.out.println(" mem_cnt ---------> "+ mem_cnt);
				
				
				
				Map input = new HashMap();
				input.put("p_subj", commandMap.get("p_subj"));
				input.put("p_year", commandMap.get("p_year"));
				input.put("p_subjseq", commandMap.get("p_subjseq"));
				input.put("score", verscore.elementAt(i).toString());
				input.put("var", vervar.elementAt(i).toString());
				input.put("ratio", Math.round(mem_cnt* d3)/100);
				input.put("prv_num", Math.round(mem_cnt* d1)/100);
				input.put("rslt", Math.abs( Math.round(mem_cnt* d3)/100- Math.round(mem_cnt* d1)/100 ) );
				finishManageDAO.insertCrtVar(input);
				
				System.out.println(" ratio ---------> "+ input.get("ratio"));
				System.out.println(" prv_num ---------> "+ input.get("prv_num"));
				System.out.println(" rslt ---------> "+ input.get("rslt"));
				
				
				
			}
			//저장된 lank를 가지고  tz_student의 link와 edit_score에 업뎃한다.
			finishManageDAO.updateStudentEditscore(commandMap);
                       
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		return isOk;
	}
	
	
	
	
	
	
	// 수료조건 확인
	public String[] isgraduatedCheck(Map commandMap, List list, Map subjInfo) throws Exception{
		String[] result = new String[list.size()];
		if( list != null && list.size() > 0 ){
			for(int i=0; i<list.size(); i++){
				Map data = (Map)list.get(i);
				String v_notgraducd = "";
				String isgraduated = "";
				double score = returnDouble(data.get("score"));
				
				
				
				//학습창 학습 진도율 변수..
				double compJindo = returnDouble(subjInfo.get("ratewbt"));		//수료진도율
				
				if(compJindo < 50.0)
				{
					compJindo = 90.0;
				}
				else if(compJindo > 99.0)
				{
					compJindo = 100.0;
				}
				
				
//				if(subjInfo.get("upperclass") != null && subjInfo.get("upperclass").equals("PAR"))	//학부모연수이면 진도율 50%이다..
//				{
//					compJindo = 50.0;
//				}
//				
				if( score > 100){
					score = 100;
				}
				
				// 총점 체크
	            if ( score < returnDouble(subjInfo.get("gradscore")) ) { 
	                v_notgraducd += "06,"; // 06 = 성적미달   - 총점점수 체크
	            }

	            // 진도율    90 %이하 tz_subj.ratewbt ==============================================================
	            if ( returnDouble(data.get("tstep")) < compJindo ) { 
	                v_notgraducd += "10,"; // 10 = 진도율미달
	            }
	            
	            // wetc2 참여도(학습진도율)
	            if ( returnDouble(data.get("wetc2")) > 0 ) { 
	            	if(  ((subjInfo.get("isonoff").toString().equals("ON") || subjInfo.get("isonoff").toString().equals("RC"))  && returnDouble(subjInfo.get("wetc1")) > returnDouble(data.get("avetc2")) && returnDouble(data.get("avetc2"))!=0 )	// (진도율*참여도(학습진도율))/100 = round(etc2 * wetc2, 2)/100
	                             || (subjInfo.get("isonoff").toString().equals("OFF") && returnDouble(subjInfo.get("wetc1")) > returnDouble(data.get("etc2")) ) 
	                          //출석점수
	                                                                           
	                ){
	            		v_notgraducd += "20,"; 
	            	}
	            }
	            
	            
	            // 2008.12.13 수정
	            // 평가 제출여부(05)
	            commandMap.put("pp_userid", data.get("userid"));
	            if ("N".equals( (String)data.get("examFlag") )) {
	            	//v_notgraducd += "05,"; // 05 = 평가 미제출
	            }
	            
	            //출석시험
	            if ( returnDouble(data.get("mtest")) < returnDouble(subjInfo.get("gradexam")) ) { 
	                //v_notgraducd += "07,"; // 07 = 평가점수미달
	            }
	            if ( returnDouble(data.get("htest")) < returnDouble(subjInfo.get("gradhtest")) ) { 
	            	//v_notgraducd += "07,"; // 07 = 평가점수미달
	            }
	            //온라인시험
	            if ( returnDouble(data.get("ftest")) < returnDouble(subjInfo.get("gradftest")) ) { 
	            	//v_notgraducd += "07,"; // 07 = 평가점수미달
	            }
	            
	            //온라인과제
	            if ( returnDouble(data.get("report")) < returnDouble(subjInfo.get("gradreport")) ) { 
	            	//v_notgraducd += "08,"; // 08 = 리포트점수미달
	            }
	            
	            //참여도(출석점수)
	           // if ( returnDouble(data.get("etc2")) == 0 ) { 
	          //  	 v_notgraducd += "13,"; // 13 = 출석점수미달
	          //  }
	            
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
	            
	            //log.info("  ++++++++++++++++ " + score + " / " + returnDouble(subjInfo.get("gradscore")) + " || " + returnDouble(data.get("tstep"))  + " / " + returnDouble(subjInfo.get("gradstep")) + " || " + v_notgraducd.length());
	            
	            //System.out.println(" gradexam ---> "+returnDouble(subjInfo.get("gradexam")));
	            //System.out.println(" mstep ---> "+returnDouble(data.get("mstep")));
	            //System.out.println(" avmtest ---> "+returnDouble(data.get("avmtest")));
	            //System.out.println(" avetc2 ---> "+returnDouble(data.get("avetc2")));	//
	            
	            
	            if (    score >= returnDouble(subjInfo.get("gradscore"))	//총점
	                    &&  returnDouble(data.get("tstep")) >= returnDouble(subjInfo.get("gradstep"))	//진도율
	                    &&  v_notgraducd.length() == 0                                                                             
	                    &&  ( 
	                    		((subjInfo.get("isonoff").toString().equals("ON") || subjInfo.get("isonoff").toString().equals("RC")) && returnDouble(subjInfo.get("gradexam")) <= returnDouble(data.get("avmtest"))) // && data.getGradexamcnt()    > 0 // 수료기준 - 출석시험 <= 가중치적용 출석시험  //기존mstep
	                    		|| subjInfo.get("isonoff").toString().equals("OFF")
	                        )                                                                                                                                 
	                                                                                                                                 
	                    &&  (       
	                    		(   ((subjInfo.get("isonoff").toString().equals("ON") || subjInfo.get("isonoff").toString().equals("RC")) && returnDouble(subjInfo.get("gradftest")) <= returnDouble(data.get("avftest")) ) //&& data.getGradftestcnt()  > 0	//수료기준 - 온라인시험 <= examtype= 'E' 온라인평가 시험점수	//기존ftest
	                                 || (subjInfo.get("isonoff").toString().equals("OFF") && returnDouble(subjInfo.get("gradftest")) <= returnDouble(data.get("ftest")) ) //&& data.getFtest() > 0
	                            )  
	                                                                              
	                        )                                                                                                                                 
	                    &&  (       
	                    		(   ((subjInfo.get("isonoff").toString().equals("ON") || subjInfo.get("isonoff").toString().equals("RC"))  && returnDouble(subjInfo.get("gradreport")) <= returnDouble(data.get("avreport")) ) //&& data.getGradreportcnt()> 0	// 수료기준 - 온라인과제 <= 과제점수	//기존report
	                                 || (subjInfo.get("isonoff").toString().equals("OFF") && returnDouble(subjInfo.get("gradreport")) <= returnDouble(data.get("report")) ) //&& data.getReport()       > 0
	                            )	                                                                               
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
	
	
	/**
     * 개인별 수료리스트 - 모바일용
     * @return
     * @exception Exception
     */
	public List selectMblUserSuryuList(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.selectMblUserSuryuList(commandMap);
	}
	
	/**
     * 이수관리 > 과거이수내역리스트
     * @return
     * @exception Exception
     */
	public List selectfinishOldList(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.selectfinishOldList(commandMap);
	}
	
	/**
     * 이수관리 > 과거이수내역리스트 전체개수
     * @return
     * @exception Exception
     */
	public int selectfinishOldListTotCnt(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.selectfinishOldListTotCnt(commandMap);
	}
	
	/**
     * 나의학습방 > 나의 교육이력 >  과거이수내역리스트(사용자)
     * @return
     * @exception Exception
     */
	public List selectUserFinishOldList(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.selectUserFinishOldList(commandMap);
	}

	
	
	/**
     * 이수관리 > 과거이수내역 보기
     * @return
     * @exception Exception
     */
	public Map selectFinishOldView(Map<String, Object> commandMap) throws Exception{
		return finishManageDAO.selectFinishOldView(commandMap);
	}
	
	/**
     * 이수관리 > 과거이수내역 삭제
     * @return
     * @exception Exception
     */
	public int deleteFinishOld(Map<String, Object> commandMap) throws Exception{
		int cnt = 0;
		try{
			cnt = finishManageDAO.deleteFinishOld(commandMap);
		} catch (Exception e) {
			return 0;
		}
		
		return cnt;
	}
	
	/**
     * 이수관리 > 과거이수내역 등록
     * @return
     * @exception Exception
     */
	public void insertFinishOld(Map<String, Object> commandMap) throws Exception{
		finishManageDAO.insertFinishOld(commandMap);
	}
	
	/**
     * 이수관리 > 과거이수내역 수정
     * @return
     * @exception Exception
     */
	public int updateFinishOld(Map<String, Object> commandMap) throws Exception{
		int cnt = 0;
		try{
			cnt = finishManageDAO.updateFinishOld(commandMap);
		} catch (Exception e) {
			return 0;
		}
		
		return cnt;
	}

	/**
	 * 이수관리 엑셀출력
	 */
	public List finishStudentExcelList(Map<String, Object> commandMap)
			throws Exception {
		return finishManageDAO.finishStudentExcelList(commandMap);
	}
	
	//수료증 출력여부
	public int suroyprintYnUpdate(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{	
			
			finishManageDAO.suroyprintYnUpdate(commandMap);
			
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		return isOk; 
	}
}
