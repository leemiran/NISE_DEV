package egovframework.usr.subj.service.impl;

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

import egovframework.adm.prop.dao.ApprovalDAO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.svt.valid.ValidService;
import egovframework.usr.subj.dao.UserSubjectDAO;
import egovframework.usr.subj.service.UserSubjectService;

@Service("userSubjectService")
public class UserSubjectServiceImpl extends EgovAbstractServiceImpl implements UserSubjectService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="approvalDAO")
    private ApprovalDAO approvalDAO;
	
	@Resource(name="userSubjectDAO")
    private UserSubjectDAO userSubjectDAO;
	
	@Autowired
	ValidService validService;
	
	/**
	 * 과정목록 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserSubjectList(Map<String, Object> commandMap) throws Exception {
		return userSubjectDAO.selectUserSubjectList(commandMap);
	}
	
	
	/**
	 * 과정 기수 정보 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserSubjectSeqList(Map<String, Object> commandMap) throws Exception{
		return userSubjectDAO.selectUserSubjectSeqList(commandMap);
	}
	
	
	/**
	 * 과정 교육후기 정보 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserStoldCommentList(Map<String, Object> commandMap) throws Exception{
		return userSubjectDAO.selectUserStoldCommentList(commandMap);
	}
	
	/**
	 * 과정 교육후기 정보 전체개수 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectUserStoldCommentTotCnt(Map<String, Object> commandMap) throws Exception{
		return userSubjectDAO.selectUserStoldCommentTotCnt(commandMap);
	}
	
	/**
	 * 과정상세정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserSubjectView(Map<String, Object> commandMap) throws Exception {
		return userSubjectDAO.selectUserSubjectView(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 17. 오후 9:09:41 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.usr.subj.service.UserSubjectService#niceNumCheck(java.util.Map)
	 */
	public Map<?, ?> niceNumCheck(Map<String, Object> commandMap) throws Exception {
		return userSubjectDAO.niceNumCheck(commandMap);
	}
	
	/**
	 * 수강신청 > 수강제약사항 체크하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserSubjProposeCheck(Map<String, Object> commandMap) throws Exception {
		return userSubjectDAO.selectUserSubjProposeCheck(commandMap);
	}
	
	/**
	 * 수강신청 > 과정정보 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserProposeSubjInfo(Map<String, Object> commandMap) throws Exception {
		return userSubjectDAO.selectUserProposeSubjInfo(commandMap);
	}
	
	/**
	 * 수강신청 > 결재아이디 생성하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserProposeGetOrderId(Map<String, Object> commandMap) throws Exception {
		return userSubjectDAO.selectUserProposeGetOrderId(commandMap);
	}
	
	

	/**
	 * 사용자 수강신청 등록(무통장/교육청일괄납부)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertUserProposeOB(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
			
			
			String  p_subj = (String) commandMap.get("p_subj");
			String  p_year = (String) commandMap.get("p_year");
			String  p_subjseq = (String) commandMap.get("p_subjseq");
			String  p_usebook_yn = (String) commandMap.get("p_usebook_yn");
			
			
			HashMap<String, Object> subjMap = new HashMap<String, Object>();
			subjMap.put("p_subj", p_subj);
			subjMap.put("p_year", p_year);
			subjMap.put("p_subjseq", p_subjseq);
			
			//과정이 패키지 과정인지를 확인하기 위하여 넣는다. row가 두개 이상이면 모두 수강신청을 한다.
			List subjinfo =  userSubjectDAO.selectUserProposeSubjInfo(subjMap);
			
			
			
			
			if(subjinfo != null)
			{
			
				String [] arr_lec_sel_no = new String[subjinfo.size()];
				
				if(commandMap.get("p_lec_sel_no") instanceof String[])
				{
					arr_lec_sel_no = (String [])commandMap.get("p_lec_sel_no");
				}
				else
				{
					arr_lec_sel_no[0] = commandMap.get("p_lec_sel_no")+"";
				}
				
				for(int i = 0; i<subjinfo.size(); i++)
				{
					
					EgovMap info = (EgovMap)subjinfo.get(i);
					
					commandMap.put("p_subj", info.get("subj"));
					commandMap.put("p_year", info.get("year"));
					commandMap.put("p_subjseq", info.get("subjseq"));
					commandMap.put("p_lec_sel_no", arr_lec_sel_no[i]);
					
					if("Y".equals(info.get("proposetype"))){
						commandMap.put("p_chkfinal", info.get("proposetype"));
					}					

					// 지역 추가
					commandMap.put("areaCode", validService.validUserArea(String.valueOf(commandMap.get("userid"))));
					//수강등록
					userSubjectDAO.insertUserPropose(commandMap);
					
					if("Y".equals(info.get("proposetype"))){
						HashMap<String, Object> mm = new HashMap<String, Object>();
						mm.put("v_userid", commandMap.get("userid"));
						mm.put("v_scsubj", commandMap.get("p_subj"));
						mm.put("v_scyear", commandMap.get("p_year"));
						mm.put("v_scsubjseq", commandMap.get("p_subjseq"));
						mm.put("v_luserid", commandMap.get("userid"));
						
						//수강테이블 등록
		            	int stuCnt = approvalDAO.selectApprovalStudentCount(mm);
		            	if(stuCnt == 0)
		            	{
		            		approvalDAO.insertApprovalStudent(mm);
		            	}
					}
				
				}
			}
			else
			{
				EgovMap info = (EgovMap)subjinfo.get(0);
				
				if("Y".equals(info.get("proposetype"))){
					commandMap.put("p_chkfinal", info.get("proposetype"));
				}
				
				//수강등록
				userSubjectDAO.insertUserPropose(commandMap);
				
				if("Y".equals(info.get("proposetype"))){
					HashMap<String, Object> mm = new HashMap<String, Object>();
					mm.put("v_userid", commandMap.get("userid"));
					mm.put("v_scsubj", commandMap.get("p_subj"));
					mm.put("v_scyear", commandMap.get("p_year"));
					mm.put("v_scsubjseq", commandMap.get("p_subjseq"));
					mm.put("v_luserid", commandMap.get("userid"));
					
					//수강테이블 등록
	            	int stuCnt = approvalDAO.selectApprovalStudentCount(mm);
	            	if(stuCnt == 0)
	            	{
	            		approvalDAO.insertApprovalStudent(mm);
	            	}
				}
            	
			}
			
			//결제등록
			userSubjectDAO.insertUserPayment(commandMap);
			
			
			if("Y".equals(p_usebook_yn)){
				//주소업데이트
				userSubjectDAO.updateUserMemberAddress(commandMap);
			}			
			
			isok = true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
		
	}
	
	
	/**
	 * 사용자 수강신청 등록(PG사 계좌이체/신용카드)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertUserProposePG(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try{
			
//			사용자 아이디
			String  p_userid = (String) commandMap.get("userid");
//			결제코드
			String  p_order_id = (String) commandMap.get("p_order_id");

			
			
			
			String  p_subj = (String) commandMap.get("p_subj");
			String  p_year = (String) commandMap.get("p_year");
			String  p_subjseq = (String) commandMap.get("p_subjseq");
			
			HashMap<String, Object> subjMap = new HashMap<String, Object>();
			subjMap.put("p_subj", p_subj);
			subjMap.put("p_year", p_year);
			subjMap.put("p_subjseq", p_subjseq);
			
			//과정이 패키지 과정인지를 확인하기 위하여 넣는다. row가 두개 이상이면 모두 수강신청을 한다.
			List subjinfo =  userSubjectDAO.selectUserProposeSubjInfo(subjMap);
			
			
			//연수지명번호
			StringTokenizer st = new StringTokenizer(commandMap.get("p_lec_sel_no")+"", "Ω");
			
			ArrayList array_p_lec_sel_no = new ArrayList();
			
			int leccount = 0;
			
			System.out.println("### st : " + st);
			System.out.println("### subjinfo.size() : " + subjinfo.size());
			
			
			
			
			while(st.hasMoreTokens())
			{
				String temp = st.nextToken();
				System.out.println("### temp : " + temp);
				array_p_lec_sel_no.add(temp);
				leccount++;
			}
			
			
			if(subjinfo != null)
			{
			
				for(int i = 0; i<subjinfo.size(); i++)
				{
					
					EgovMap info = (EgovMap)subjinfo.get(i);
					
					commandMap.put("p_subj", info.get("subj"));
					commandMap.put("p_year", info.get("year"));
					commandMap.put("p_subjseq", info.get("subjseq"));
					
					
					System.out.println("array_p_lec_sel_no.size()  11111111111111 --------> "+ array_p_lec_sel_no.size());
					System.out.println("array_p_lec_sel_no.size()  11111111111111 --------> "+ array_p_lec_sel_no.size());
					System.out.println("leccount  11111111111111 --------> "+ leccount);
					
					if(array_p_lec_sel_no.size() > 0 &&  i < leccount){
						commandMap.put("p_lec_sel_no", array_p_lec_sel_no.get(i));
						
					}	
					
					
					//수강등록 확인
					int userProposeCnt = userSubjectDAO.selectUserProposeCnt(commandMap);					
					
					System.out.println("p_order_id 11111111111111 --------> "+ p_order_id);					
					System.out.println("subj 11111111111111 --------> "+ info.get("subj"));
					System.out.println("year 11111111111111 --------> "+ info.get("year"));
					System.out.println("subjseq 11111111111111 --------> "+ info.get("subjseq"));
					System.out.println("p_userid 11111111111111 --------> "+ p_userid);					
					System.out.println("userProposeCnt 11111111111111 --------> "+ userProposeCnt);
					
					if(userProposeCnt == 0){
						//수강등록
						userSubjectDAO.insertUserPropose(commandMap);
					}				
					
					System.out.println("p_order_id 2222222222222 --------> "+ p_order_id);		
					System.out.println("userProposeCnt 2222222222222 --------> "+ userProposeCnt);
					System.out.println("subj 2222222222222 --------> "+ info.get("subj"));
					System.out.println("year 2222222222222 --------> "+ info.get("year"));
					System.out.println("subjseq 2222222222222 --------> "+ info.get("subjseq"));
					System.out.println("p_userid 2222222222222 --------> "+ p_userid);
					
					
					//--------------------------------------------------------------------
					//수강등록 프로세스
					//--------------------------------------------------------------------
					HashMap<String, Object> mm = new HashMap<String, Object>();
		        	mm.put("v_luserid", p_userid);
		        	mm.put("v_userid", p_userid);
		        	mm.put("v_scsubj",  info.get("subj"));
		        	mm.put("v_scyear",  info.get("year"));
		        	mm.put("v_scsubjseq",  info.get("subjseq"));
		//        	결제정보
		        	mm.put("v_order_id", p_order_id);
		        	
					//독서통신 상태값 변경
		        	mm.put("v_status", "W");
		        	approvalDAO.updateApprovalProposeBook(mm);
		        	
		        	
		        	System.out.println("p_order_id 333333333333 --------> "+ p_order_id);
		        	System.out.println("userProposeCnt 333333333333 --------> "+ userProposeCnt);
					System.out.println("subj 333333333333 --------> "+ info.get("subj"));
					System.out.println("year 333333333333 --------> "+ info.get("year"));
					System.out.println("subjseq 333333333333 --------> "+ info.get("subjseq"));
					System.out.println("p_userid 333333333333 --------> "+ p_userid);
		        	
		        	//수강테이블 등록
		        	int stuCnt = approvalDAO.selectApprovalStudentCount(mm);
		        	
		        	System.out.println("p_order_id 44444444 --------> "+ p_order_id);	
		        	System.out.println("userProposeCnt 44444444 --------> "+ userProposeCnt);
					System.out.println("subj 44444444 --------> "+ info.get("subj"));
					System.out.println("year 44444444 --------> "+ info.get("year"));
					System.out.println("subjseq 44444444 --------> "+ info.get("subjseq"));
					System.out.println("p_userid 44444444 --------> "+ p_userid);
					
					System.out.println("stuCnt  --------> "+ stuCnt);
					
		        	if(stuCnt == 0)
		        	{
		        		approvalDAO.insertApprovalStudent(mm);
		        	}
		        	
		        	System.out.println("p_order_id 55555555 --------> "+ p_order_id);
		        	System.out.println("userProposeCnt 55555555 --------> "+ userProposeCnt);
					System.out.println("subj 55555555 --------> "+ info.get("subj"));
					System.out.println("year 55555555 --------> "+ info.get("year"));
					System.out.println("subjseq 55555555 --------> "+ info.get("subjseq"));
					System.out.println("p_userid 55555555 --------> "+ p_userid);
					
		        	//--------------------------------------------------------------------
				}
			} 
			else
			{
				System.out.println("subjinfo_null p_order_id 11111111111111 --------> "+ p_order_id);
				System.out.println("subjinfo_null subj 11111111111111 --------> "+ p_subj);
				System.out.println("subjinfo_null year 11111111111111 --------> "+ p_year);
				System.out.println("subjinfo_null subjseq 11111111111111 --------> "+ p_subjseq);
				System.out.println("subjinfo_null p_userid 11111111111111 --------> "+ p_userid);
				
				//수강등록 확인
				int userProposeCnt = userSubjectDAO.selectUserProposeCnt(commandMap);
				if(userProposeCnt == 0){
					//수강등록
					userSubjectDAO.insertUserPropose(commandMap);
				}
				
				System.out.println("subjinfo_null p_order_id 2222222222 --------> "+ p_order_id);
				System.out.println("subjinfo_null subj 2222222222 --------> "+ p_subj);
				System.out.println("subjinfo_null year 2222222222 --------> "+ p_year);
				System.out.println("subjinfo_null subjseq 2222222222 --------> "+ p_subjseq);
				System.out.println("subjinfo_null p_userid 2222222222 --------> "+ p_userid);
				
				//--------------------------------------------------------------------
				//수강등록 프로세스
				//--------------------------------------------------------------------
				HashMap<String, Object> mm = new HashMap<String, Object>();
	        	mm.put("v_luserid", p_userid);
	        	mm.put("v_userid", p_userid);
	        	mm.put("v_scsubj", p_subj);
	        	mm.put("v_scyear", p_year);
	        	mm.put("v_scsubjseq", p_subjseq);
	//        	결제정보
	        	mm.put("v_order_id", p_order_id);
	        	
				//독서통신 상태값 변경
	        	mm.put("v_status", "W");
	        	approvalDAO.updateApprovalProposeBook(mm);
	        	
	        	System.out.println("subjinfo_null p_order_id 33333333333 --------> "+ p_order_id);
	        	System.out.println("subjinfo_null subj 33333333333 --------> "+ p_subj);
				System.out.println("subjinfo_null year 33333333333 --------> "+ p_year);
				System.out.println("subjinfo_null subjseq 33333333333 --------> "+ p_subjseq);
				System.out.println("subjinfo_null p_userid 33333333333 --------> "+ p_userid);
	        	
	        	//수강테이블 등록
	        	int stuCnt = approvalDAO.selectApprovalStudentCount(mm);
	        	
	        	System.out.println("subjinfo_null subj 33333333333 --------> "+ p_subj);
				System.out.println("subjinfo_null year 33333333333 --------> "+ p_year);
				System.out.println("subjinfo_null subjseq 33333333333 --------> "+ p_subjseq);
				System.out.println("subjinfo_null p_userid 33333333333 --------> "+ p_userid);
				System.out.println("subjinfo_null stuCnt 33333333333 --------> "+ stuCnt);
				
				
	        	if(stuCnt == 0)
	        	{
	        		approvalDAO.insertApprovalStudent(mm);
	        	}
	        	
	        	System.out.println("subjinfo_null p_order_id 33333333333 --------> "+ p_order_id);
	        	System.out.println("subjinfo_null subj 4444444444 --------> "+ p_subj);
				System.out.println("subjinfo_null year 4444444444 --------> "+ p_year);
				System.out.println("subjinfo_null subjseq 4444444444 --------> "+ p_subjseq);
				System.out.println("subjinfo_null p_userid 4444444444 --------> "+ p_userid);				
				
	        	//--------------------------------------------------------------------
			}
        	
        	
        	
        	
        	
			System.out.println(" p_order_id 5555555555 --------> "+ p_order_id);
			System.out.println(" subj 5555555555 --------> "+ p_subj);
			System.out.println(" year 5555555555 --------> "+ p_year);
			System.out.println(" subjseq 5555555555 --------> "+ p_subjseq);
			System.out.println(" p_userid 5555555555 --------> "+ p_userid);		
        	
        	
			//주소업데이트
			userSubjectDAO.updateUserMemberAddress(commandMap);
			
			System.out.println(" p_order_id 66666666666 --------> "+ p_order_id);
			System.out.println(" subj 66666666666 --------> "+ p_subj);
			System.out.println(" year 66666666666 --------> "+ p_year);
			System.out.println(" subjseq 66666666666 --------> "+ p_subjseq);
			System.out.println(" p_userid 66666666666 --------> "+ p_userid);		
			
			
			//결제확인			
        	int payCnt = approvalDAO.selectPayMentCount(commandMap);
        	
        	if(payCnt == 0){        	
				//결제등록
				userSubjectDAO.insertUserPayment(commandMap);
        	}
			
			System.out.println(" subj 77777777777777 --------> "+ p_subj);
			System.out.println(" year 77777777777777 --------> "+ p_year);
			System.out.println(" subjseq 77777777777777 --------> "+ p_subjseq);
			System.out.println(" p_userid 77777777777777 --------> "+ p_userid);	
			
			
			isok = true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isok;
		
	}
	
	
	/**
	 * 사용자 수강신청 등록(PG사 계좌이체/신용카드)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteUserPropose(Map<String, Object> commandMap) throws Exception {
		boolean isok = false;
		int cnt = 0;
		
		try {
			//과목코드 , 기수, 아이디
			String v_scsubj = (String)commandMap.get("p_subj");
			String v_scyear = (String)commandMap.get("p_year");
			String v_scsubjseq = (String)commandMap.get("p_subjseq");
			String v_userid = (String) commandMap.get("userid");
			
			HashMap<String, Object> mm = new HashMap<String, Object>();
			mm.put("v_scsubj", v_scsubj);
			mm.put("v_scyear", v_scyear);
			mm.put("v_scsubjseq", v_scsubjseq);
			mm.put("v_userid", v_userid);
			
			//독서통신 상태값 변경
			mm.put("v_status", "R");	//R:제거  
			cnt += approvalDAO.updateApprovalProposeBook(mm);
			
			//삭제로그를 남긴다. 
			// v_luserid(관리자 아이디), v_order_id(주문 아이디) 항목이 없다. 없으면 null값으로 들어가려나.
			mm.put("v_cancelkind", "D");
			mm.put("v_reason", "회원삭제");
			mm.put("v_reasoncd", "99");
			int v_seq = Integer.parseInt(approvalDAO.insertApprovalCancel(mm) + "");
			log.info(this.getClass().getName() + " Propose tz_cancel table seq :" + v_seq);
			
			//tz_student 삭제
			cnt += approvalDAO.deleteApprovalStudent(mm);

			//tz_propose 삭제
			cnt += approvalDAO.deleteApprovalPropose(mm);

			//삭제시에는 결제정보를 사용안함으로 변경한다. pa_payment
			cnt += userSubjectDAO.updateApprovalPaPaymentUseYn(mm);

			isok = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return isok;
	}
	
	
	/**
	 * 모바일 과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectUserSubjectMobileList(Map<String, Object> commandMap) throws Exception{
		return userSubjectDAO.selectUserSubjectMobileList(commandMap);
	}
	
	/**
	 * 모바일 과정 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectUserSubjectMobileListTotCnt(Map<String, Object> commandMap) throws Exception{
		return userSubjectDAO.selectUserSubjectMobileListTotCnt(commandMap);
	}
}
