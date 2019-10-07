package egovframework.adm.prop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import egovframework.adm.prop.dao.ApprovalDAO;
import egovframework.adm.prop.service.ApprovalService;
import egovframework.adm.sms.SMSSenderDAO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.svt.valid.ValidService;

@Service("approvalService")
public class ApprovalServiceImpl extends EgovAbstractServiceImpl implements ApprovalService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="approvalDAO")
    private ApprovalDAO approvalDAO;
	
	/** SMSSender */
	@Resource(name = "SMSSenderDAO")
	SMSSenderDAO SMSSenderDAO;
	
	@Autowired
	ValidService validService;
	
	/**
	 * 신청승인목록 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectApprovalList(Map<String, Object> commandMap) throws Exception {
		return approvalDAO.selectApprovalList(commandMap);
	}
	
	/**
	 * 신청승인 삭제자 목록 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectApprovalDeleteList(Map<String, Object> commandMap)	throws Exception {
		return approvalDAO.selectApprovalDeleteList(commandMap);
	}
	
	/**
	 * 신청승인 비고항목 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectApprovaEtcView(Map<String, Object> commandMap) throws Exception {
		return approvalDAO.selectApprovaEtcView(commandMap);
	}
	
	
	/**
	 * 수강신청 비고 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateApprovalEtc(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
			int cnt = approvalDAO.updateApprovalEtc(commandMap);
			
			if(cnt > 0)	isok = true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
		
	}
	
	
	
	/**
	 * 수강신청 처리상태(승인, 취소, 반려, 삭제) 이하 정보 수정 및 수강생으로 등록처리 프로세스
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean approvalProcess(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
//			체크박스 선택여부(1:checked/0:not checked)
			String []  v_checkvalue = (String []) commandMap.get("_Array_checkvalue");
			
//			과목코드 , 기수, 아이디
			String []  v_param = (String []) commandMap.get("_Array_p_params");
//			변경된 데이터 - 미처리/승인/반려/삭제
			String []  v_chk = (String []) commandMap.get("_Array_p_chkfinal");
//			기존 DB - 미처리/승인/반려/삭제
			String []  v_oldchk = (String []) commandMap.get("_Array_p_final_chk");
			
//			결제정보 : 주문번호
			String []  v_order_id = (String []) commandMap.get("_Array_p_order_id");
//			결제정보 : 결제수단
			String []  v_paycd = (String []) commandMap.get("_Array_p_paycd");
//			결제정보 : 입금일자
			String []  v_enter_dt = (String []) commandMap.get("_Array_p_enter_dt");	
//			결제정보 : 무통장(OB), 교육청일괄납부방법(PB)
			String []  v_paytype = (String []) commandMap.get("_Array_p_paytype");				
			
			
			 
			
//			관리자 아이디
			String  v_luserid = (String) commandMap.get("userid");
			
			
			
			if(v_param != null)
			{
				for ( int i = 0 ; i < v_checkvalue.length; i++ ) { 
					
					//v_checkvalue 1인 경우(선택된 수강생)만  저자을 실행한다.
					if(v_checkvalue[i].equals("1"))
					{
					
						StringTokenizer st  = new StringTokenizer( v_param[i], ",");
		                String v_scsubj              = st.nextToken();
		                String v_scyear              = st.nextToken();
		                String v_scsubjseq           = st.nextToken();
		                String v_userid           = st.nextToken();
		                
		                HashMap<String, Object> mm = new HashMap<String, Object>();
	                	
		                if(v_chk[i].equals("N"))
	                		mm.put("v_rejectkind", "P");
	                	else
	                		mm.put("v_rejectkind", "");
	                	
	                	mm.put("v_chk", v_chk[i]);
	                	mm.put("v_oldchk", v_oldchk[i]);
	                	mm.put("v_rejectedreason", "");
	                	mm.put("v_luserid", v_luserid);
	                	mm.put("v_userid", v_userid);
	                	mm.put("v_scsubj", v_scsubj);
	                	mm.put("v_scyear", v_scyear);
	                	mm.put("v_scsubjseq", v_scsubjseq);
	                	
	                	//결제정보
	                	mm.put("v_order_id", v_order_id[i]);
	                	mm.put("v_paycd", v_paycd[i]);
	                	mm.put("v_enter_dt", v_enter_dt[i]);
	                	mm.put("v_paytype", v_paytype[i]);
	                	
	                	//수강상태 변경 로직..
	                	int cnt = this.execApprovalProcess(mm);
	                	
	                	if(cnt == 0) 
	                	{
	                		return false;
	                	}
		                
					}
				}
				isok = true;
			}
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
		
	}
	
	
	/**
	 * 수강생관리 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectStudentManagerList(Map<String, Object> commandMap) throws Exception{
		return approvalDAO.selectStudentManagerList(commandMap);
	}
	
	
	/**
	 * 수강생관리 과정완료여부 및 과정상태확인 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectStudentManagerIsClosed(Map<String, Object> commandMap) throws Exception{
		return approvalDAO.selectStudentManagerIsClosed(commandMap);
	}
	
	
	/**
	 * 선정할 교육대상자 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectAcceptTargetMemberList(Map<String, Object> commandMap) throws Exception{
		return approvalDAO.selectAcceptTargetMemberList(commandMap);
	}
	
	
	/**
	 * 선정된 교육대상자 입과처리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> acceptTargetMember(Map<String, Object> commandMap) throws Exception{
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean isok = false;
		
		try{
			
			
//			체크박스 선택여부
			String []  v_checkvalue = (String []) commandMap.get("_Array_p_checks");
			
			
			
//			관리자 아이디
			String  v_luserid = (String) commandMap.get("userid");
			
			String  v_scsubj = (String) commandMap.get("p_subj");
			String  v_scyear = (String) commandMap.get("p_year");
			String  v_scsubjseq = (String) commandMap.get("p_subjseq");
			
			
			if(v_checkvalue != null)
			{
				List<String> missAreaCodeUserids = new ArrayList<String>();
				
				for ( int i = 0 ; i < v_checkvalue.length; i++ ) {
					
					HashMap<String, Object> mm = new HashMap<String, Object>();
					int cnt = 0;
					Object obj = null;
					
					
					String v_userid = v_checkvalue[i];
						
					mm.put("v_userid", v_userid);
//                	mm.put("v_oldchk", v_oldchk[i]);
                	mm.put("v_rejectedreason", "");
                	mm.put("v_luserid", v_luserid);
                	mm.put("v_userid", v_userid);
                	mm.put("v_scsubj", v_scsubj);
                	mm.put("v_scyear", v_scyear);
                	mm.put("v_scsubjseq", v_scsubjseq);
                	mm.put("v_isdinsert", "Y");
                	mm.put("v_chkfirst", "Y");
                	mm.put("v_chkfinal", "Y");
                	// 회원의 지역을 가져온다
                	String areaCode = validService.validUserArea(v_userid);
                	if(areaCode.length() != 3) {
                		missAreaCodeUserids.add(v_userid);
                		continue;
                	}
                	mm.put("areaCode", areaCode);
                	
                	//등록되어 있는지를 검사한다.
                	int propCnt = approvalDAO.selectProposeCount(mm);
                	
                	//tz_propose update
                	if(propCnt > 0)
                	{
                		cnt = approvalDAO.updatePropose(mm);
                	}
                	//tz_propose insert
                	else
                	{
                		obj = approvalDAO.insertPropose(mm);
                	}
                	
                	
                	//결재정보 등록하기
                	obj = approvalDAO.insertPaPayment(mm);
                	if(obj != null)
                	{
                		mm.put("p_order_id", obj);
                		
//                		주문번호를 신청 테이블에 넣는다. 
                		cnt = approvalDAO.updateProposeOrdering(mm);
                	}
                	
                	
                	//수강테이블 등록
                	int stuCnt = approvalDAO.selectApprovalStudentCount(mm);
                	if(stuCnt == 0)
                	{
                		approvalDAO.insertApprovalStudent(mm);
                	}
                	
                	
				}	
				isok = true;
				if(missAreaCodeUserids.size() > 0) {
					int successCnt = v_checkvalue.length - missAreaCodeUserids.size();
					String missAreaCodeMsg = String.valueOf(successCnt) + "건 성공, " + StringUtils.collectionToDelimitedString(missAreaCodeUserids, ", ") + " 선정불가. 회원구분에 따른 나이스번호, 시도교육청, 도로명 우편번호 코드를 확인해 주세요.";
					returnMap.put("missAreaCodeMsg", missAreaCodeMsg);
					isok = false;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		returnMap.put("isok", isok);
		return returnMap;
		
	}
	
	
	/**
	 * 수강신청 (승인, 취소, 반려, 삭제) 이하 정보 수정 및 수강생으로 등록처리 프로세스
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int execApprovalProcess(HashMap<String, Object> mm) throws Exception{
		int cnt = 0; 
		
		String v_chk = (String)mm.get("v_chk");
		
		//삭제처리가 아니라면 신청테이블을 업데이트 한다.
        if(!v_chk.equals("D"))
        {
        	cnt += approvalDAO.updateApprovalPropose(mm);
        	
        	log.info(this.getClass().getName() + " Propose chkfinal Update(처리상태) Count :" + cnt);
        	
        	if(v_chk.equals("Y"))	//승인시
            {
            	
            	//독서통신 상태값 변경
            	mm.put("v_status", "W");
            	cnt += approvalDAO.updateApprovalProposeBook(mm);
            	
            	//수강테이블 등록
            	int stuCnt = approvalDAO.selectApprovalStudentCount(mm);
            	if(stuCnt == 0)
            	{
            		approvalDAO.insertApprovalStudent(mm);
            		//문자 발송 로직
            		
/*            		[국립특수교육원원격교육연수원] 
            				'해당과정명' 연수 신청이 완료되었습니다. 
            				'연수시작일'부터 연수가 시작되오니 학습공지사항 필독 하신 후 연수 들어주시기 바랍니다. 
            				유익한 연수가 되시길 바랍니다. 
            				(' ' 안에있는 것은 자동으로 해당 과정명과 날짜가 나와야 함) 
*/
            		
            		Map view = approvalDAO.selectSubject(mm);
            		if(view != null){
            			String str = view.get("scsubjnm") +"연수 신청이 완료되었습니다. "+ view.get("edustart")+"부터 연수가 시작되오니 학습공지사항 필독 하신 후 연수 들어주시기 바랍니다. 유익한 연수가 되시길 바랍니다.";
                		
                		Map<String, Object> commandMap = new HashMap();
                		
                		commandMap.put("content", str); 
                		commandMap.put("p_handphone", view.get("handphone")); 
                		commandMap.put("smsMms", "mms"); 
                		commandMap.put("p_subject", "[국립특수교육원원격교육연수원] "); 
                		boolean isOk = SMSSenderDAO.dacomSmsSender(commandMap);
            		}
            		
            			
            	}
            	
            }
            else
            {
            	//tz_student 삭제
            	cnt += approvalDAO.deleteApprovalStudent(mm);
            	
            	
            	//독서통신 상태값 변경
            	if(v_chk.equals("N"))	
            		mm.put("v_status", "N");	//반려시
            	else
            		mm.put("v_status", "W");	// 미처리
            	cnt += approvalDAO.updateApprovalProposeBook(mm);
            	
            }
        }
        else	//삭제시
        {
        	//독서통신 상태값 변경
        	mm.put("v_status", "R");	//R:제거  
        	cnt += approvalDAO.updateApprovalProposeBook(mm);
        	
        	//삭제로그를 남긴다.
        	mm.put("v_cancelkind", "D");
        	mm.put("v_reason", "운영자삭제");
        	mm.put("v_reasoncd", "99");
        	int v_seq = Integer.parseInt(approvalDAO.insertApprovalCancel(mm)+"");
        	log.info(this.getClass().getName() + " Propose tz_cancel table seq :" + v_seq);
        	
        	//tz_student 삭제
        	cnt += approvalDAO.deleteApprovalStudent(mm);
        	
        	//tz_propose 삭제
        	cnt += approvalDAO.deleteApprovalPropose(mm);
        	
        	//삭제시에는 결제정보를 사용안함으로 변경한다. pa_payment
        	cnt += approvalDAO.updateApprovalPaPaymentUseYn(mm);
        }
        
        //결재정보 테이블(pa_payment) 입금일자와 결재수단(무통장, 교육청일괄납부)일때만 업데이트 처리한다.
        cnt += approvalDAO.updateApprovalPaPaymentEnterDtType(mm);
        
        
        return cnt;
	}
	
	
	/**
	 * 수강생관리 프로세스
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean studentManagerProcess(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
//			체크박스 선택여부(1:checked/0:not checked)
			String []  v_checkvalue = (String []) commandMap.get("_Array_p_checks");
			
			String v_chk = (String) commandMap.get("p_chkfinal");
			
			
//			관리자 아이디
			String  v_luserid = (String) commandMap.get("userid");
			
			
			
			if(v_checkvalue != null)
			{
				for ( int i = 0 ; i < v_checkvalue.length; i++ ) { 
					
					StringTokenizer st  = new StringTokenizer( v_checkvalue[i], ",");
	                String v_scsubj              = st.nextToken();
	                String v_scyear              = st.nextToken();
	                String v_scsubjseq           = st.nextToken();
	                String v_userid           = st.nextToken();
	                String v_order_id           = st.nextToken();
	                
	                HashMap<String, Object> mm = new HashMap<String, Object>();
                	
	                if(v_chk.equals("N"))
                		mm.put("v_rejectkind", "P");
                	else
                		mm.put("v_rejectkind", "");
                	
                	mm.put("v_chk", v_chk);
                	mm.put("v_oldchk", "");
                	mm.put("v_rejectedreason", "");
                	mm.put("v_luserid", v_luserid);
                	mm.put("v_userid", v_userid);
                	mm.put("v_scsubj", v_scsubj);
                	mm.put("v_scyear", v_scyear);
                	mm.put("v_scsubjseq", v_scsubjseq);
                	
                	//결제정보
                	mm.put("v_order_id", v_order_id);
                	
                	
                	
                	//수강상태 변경 로직..
                	int cnt = this.execApprovalProcess(mm);
                	
                	
                	
                	
                	
                	if(cnt == 0) 
                	{
                		return false;
                	}
	                
				}
				isok = true;
			}
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
		
	}
	
	
	/**
	 * 취소/반려자 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectPropCancelMemberList(Map<String, Object> commandMap) throws Exception{
		return approvalDAO.selectPropCancelMemberList(commandMap);
	}
	
	
	/**
	 * 환불일자 업데이트 하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updatePropCancelMemberRePay(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		
//		체크박스 선택여부(1:checked/0:not checked)
		String []  v_checkvalue = (String []) commandMap.get("_Array_checkvalue");
		
//		주문번호
		String []  v_order_id = (String []) commandMap.get("_Array_order_id");
//		환불일자
		String []  v_repay_dt = (String []) commandMap.get("_Array_repay_dt");
//		입금액
		String []  v_amount = (String []) commandMap.get("_Array_amount");
//		수강생
		String []  v_userid = (String []) commandMap.get("_Array_userid");		
//      환불여부
		String []  v_repayYn = (String []) commandMap.get("_Array_repayYn");	
		
		
		try{
			if(v_checkvalue != null)
			{
				for ( int i = 0 ; i < v_checkvalue.length; i++ ) { 
					//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    v_checkvalue   " +v_checkvalue[i]);
					//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    v_order_id   " +v_order_id[i]);
					//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    v_amount   " +v_amount[i]);
					//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    v_userid   " +v_userid[i]);
					//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    v_repayYn   " +v_repayYn[i]);
					
					//v_checkvalue 1인 경우만  저장을 실행한다.
					if(v_checkvalue[i].equals("1"))
					{
		                HashMap<String, Object> mm = new HashMap<String, Object>();
		                mm.put("v_order_id", v_order_id[i]);
		                mm.put("v_repay_dt", v_repay_dt[i]);
		                mm.put("v_amount", v_amount[i]);
		                mm.put("v_userid", v_userid[i]);
		                mm.put("v_repayYn", v_repayYn[i]);
		                
		                log.info(this.getClass().getName() + " update Map : " + mm);
		                
		                int cnt = approvalDAO.updatePropCancelMemberRePay(mm);
					}
				}
			}
	                
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	/**
	 * 사용자 수강신청/반려 리스트 #1
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserCancelPosibleList(Map<String, Object> commandMap) throws Exception{
		return approvalDAO.selectUserCancelPosibleList(commandMap);
	}
	
	
	/**
	 * 사용자 수강신청/반려 리스트 #3
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserCourseCancelList(Map<String, Object> commandMap) throws Exception{
		return approvalDAO.selectUserCourseCancelList(commandMap);
	}
	
	
	
	/**
	 * 사용자 취소/반려 리스트 #3
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserCancelList(Map<String, Object> commandMap) throws Exception{
		return approvalDAO.selectUserCancelList(commandMap);
	}
	
	
	/**
	 * 사용자 수강취소 프로세스 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean propUserCancelAction(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
			
			String  p_subj = (String) commandMap.get("p_subj");
			String  p_year = (String) commandMap.get("p_year");
			String  p_subjseq = (String) commandMap.get("p_subjseq");
			
			
//			사용자 아이디
			String  p_userid = (String) commandMap.get("userid");
//			결제코드
			String  p_order_id = (String) commandMap.get("p_order_id");
//			취소사유코드
			String  p_reasoncd = (String) commandMap.get("p_reasoncd");
//			기타취소사유
			String  p_reason = (String) commandMap.get("p_reason");
			
			
            HashMap<String, Object> mm = new HashMap<String, Object>();
        	
        	
        	mm.put("v_luserid", p_userid);
        	mm.put("v_userid", p_userid);
        	mm.put("v_scsubj", p_subj);
        	mm.put("v_scyear", p_year);
        	mm.put("v_scsubjseq", p_subjseq);
//        	취소코드
        	mm.put("v_rejectkind", p_reasoncd);
//        	취소사유
        	mm.put("v_rejectedreason", p_reason);
//        	결제정보
        	mm.put("v_order_id", p_order_id);
        	
        	int cnt = 0;
        	
        	//독서통신 상태값 변경
        	mm.put("v_status", "R");	//R:제거  
        	cnt += approvalDAO.updateApprovalProposeBook(mm);
        	
        	//삭제로그를 남긴다.
        	mm.put("v_cancelkind", "P");
        	mm.put("v_chkfinal", "N");
        	mm.put("v_reason", p_reason);
        	mm.put("v_reasoncd", p_reasoncd);
        	
        	
        	int v_seq = Integer.parseInt(approvalDAO.insertApprovalCancel(mm)+"");
        	
        	log.info(this.getClass().getName() + " Propose tz_cancel table seq :" + v_seq);
        	
        	//tz_student 삭제
        	cnt += approvalDAO.deleteApprovalStudent(mm);
        	
        	//tz_propose 삭제
        	cnt += approvalDAO.deleteApprovalPropose(mm);
        	
        	//삭제시에는 결제정보를 사용안함으로 변경한다. pa_payment
        	cnt += approvalDAO.updateApprovalPaPaymentUseYn(mm);
        	
        	
        	isok = true;
                	
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
		
	}
	
	/**
	 * 수강신청 결제정보변경
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean studentPayTypeProcess(Map<String, Object> commandMap)  throws Exception{
		boolean isok = false;
		
		try{
			
			//수강신청 결제정보 업데이트
			int cnt1 = approvalDAO.updateProposePaytype(commandMap);
			//결제정보 결제방법 업데이트
			int cnt2 = approvalDAO.updatePaymentPaytype(commandMap);
			
			if((cnt1+cnt2) > 1)        
			{
				isok = true;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
		
	}

	/**
	 * 과정의 기수 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSubjSeqList(Map<String, Object> commandMap) throws Exception {
		return approvalDAO.selectSubjSeqList(commandMap);
	}

	/**
	 * 수강생 기수 정보 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateMemberSubjSeqInfo(Map<String, Object> commandMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int totCnt=0;
		log.error("commandMap : "+commandMap);
		String [] userids = (commandMap.get("ajp_userids")+"").split(",");
		
		if(userids.length > 0){			
			for(int i = 0 ; i < userids.length ; i++){			
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("ajp_subj"			, commandMap.get("ajp_subj"));
				paramMap.put("ajp_year"			, commandMap.get("ajp_year"));
				paramMap.put("ajp_newsubjseq"	, commandMap.get("ajp_newsubjseq"));
				paramMap.put("ajp_oldsubjseq"	, commandMap.get("ajp_oldsubjseq"));
				paramMap.put("ajp_userid"			, userids[i]);
				log.error(i+" : "+paramMap);
			int checkCnt = approvalDAO.selectMemberSubjSeqInfo(paramMap);	
			
				if(checkCnt > 0 ){
					//resultMap.put("resultYn", "A");
				}else{
					int resultCnt  = approvalDAO.updateMemberSubjSeqInfo(paramMap);		
					if(resultCnt > 0 ){
						//resultMap.put("resultYn", "Y");
						totCnt++;
					}else{
						//resultMap.put("resultYn", "N");
					}
				}
			}
		}
		resultMap.put("resultYn", "Y");
		resultMap.put("totCnt", totCnt+"");
		return resultMap;
	}

	/**
	 * 기수 리스트 조회
	 * @param commandMap
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	public List<Map<String, Object>> studentManagerView(	Map<String, Object> commandMap) throws Exception {
		return approvalDAO.studentManagerView(commandMap);
	}
	
	/**
	 * 재수강 가능한 기수 리스트 조회
	 * @param commandMap
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	public List<Map<String, Object>> studentSubjseqList(	Map<String, Object> commandMap) throws Exception {
		return approvalDAO.studentSubjseqList(commandMap);
	}
	
	
	/**
	 * 수강생 기수 정보 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateMemberSubjseqProc(Map<String, Object> commandMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int totCnt=0;
		log.error("commandMap : "+commandMap);
		//String [] userids = (commandMap.get("ajp_userids")+"").split(",");
		
		
		
		int checkCnt = approvalDAO.selectMemberSubjSeqInfo(commandMap);	
		
		if(checkCnt > 0 ){
			//resultMap.put("resultYn", "A");
		}else{
			
			//{call PROC_MEMBER_SUBJSEQ_UPDATE(#ajp_year#, #ajp_subj#, #ajp_newsubjseq#, #ajp_userids#, #ajp_oldsubjseq#, #p_reSubjseq_1#, #p_reSubjseq_2#, #p_reSubjseq_3#, #p_reSubjseq_4#, #p_reSubjseq_comment#, #userid#)}
			//{call PROC_MEMBER_SUBJSEQ_UPDATE(##, ##, ##, ##, ##, ##, ##, ##, ##, ##, ##)}
			
			System.out.println("ajp_year ----> "+ commandMap.get("ajp_year").toString());
			System.out.println("ajp_subj ----> "+ commandMap.get("ajp_subj").toString());
			System.out.println("ajp_newsubjseq ----> "+ commandMap.get("ajp_newsubjseq").toString());
			System.out.println("ajp_userids ----> "+ commandMap.get("ajp_userids").toString());
			System.out.println("ajp_oldsubjseq ----> "+ commandMap.get("ajp_oldsubjseq").toString());
			System.out.println("p_reSubjseq_1 ----> "+ commandMap.get("p_reSubjseq_1").toString());
			System.out.println("p_reSubjseq_2 ----> "+ commandMap.get("p_reSubjseq_2").toString());
			System.out.println("p_reSubjseq_3 ----> "+ commandMap.get("p_reSubjseq_3").toString());
			System.out.println("p_reSubjseq_4 ----> "+ commandMap.get("p_reSubjseq_4").toString());
			System.out.println("p_reSubjseq_comment ----> "+ commandMap.get("p_reSubjseq_comment").toString());
			System.out.println("userid ----> "+ commandMap.get("userid").toString());
			
			
			int resultCnt  = approvalDAO.updateMemberSubjseqProc(commandMap);		
			if(resultCnt > 0 ){
				//resultMap.put("resultYn", "Y");
				totCnt++;
			}else{
				//resultMap.put("resultYn", "N");
			}
		}
		
		/*
		if(userids.length > 0){			
			for(int i = 0 ; i < userids.length ; i++){			
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("ajp_subj"			, commandMap.get("ajp_subj"));
				paramMap.put("ajp_year"			, commandMap.get("ajp_year"));
				paramMap.put("ajp_newsubjseq"	, commandMap.get("ajp_newsubjseq"));
				paramMap.put("ajp_oldsubjseq"	, commandMap.get("ajp_oldsubjseq"));
				paramMap.put("ajp_userid"			, userids[i]);
				log.error(i+" : "+paramMap);
				
				int checkCnt = approvalDAO.selectMemberSubjSeqInfo(paramMap);	
			
				if(checkCnt > 0 ){
					//resultMap.put("resultYn", "A");
				}else{
					int resultCnt  = approvalDAO.updateMemberSubjseqProc(paramMap);		
					if(resultCnt > 0 ){
						//resultMap.put("resultYn", "Y");
						totCnt++;
					}else{
						//resultMap.put("resultYn", "N");
					}
				}
			}
		}*/
		resultMap.put("resultYn", "Y");
		resultMap.put("totCnt", totCnt+"");
		return resultMap;
	}
	
	/**
	 * 최초 신청 기수 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map selectStudentSubjseqView(Map<String, Object> commandMap) throws Exception {
		return approvalDAO.selectStudentSubjseqView(commandMap);
	}
	
}
