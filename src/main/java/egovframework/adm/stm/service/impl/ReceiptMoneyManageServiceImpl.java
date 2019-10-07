package egovframework.adm.stm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import egovframework.adm.stm.dao.ReceiptMoneyManageDAO;
import egovframework.adm.stm.service.ReceiptMoneyManageService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * 결제관리
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   수정일      		수정자           수정내용
 *  -------    		--------    ---------------------------
 *   2014.12.23  	유상도          	 최초 생성
 * </pre>
 */

@Service("receiptMoneyManageService")
public class ReceiptMoneyManageServiceImpl extends EgovAbstractServiceImpl implements ReceiptMoneyManageService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="receiptMoneyManageDAO")
    private ReceiptMoneyManageDAO receiptMoneyManageDAO;
	
	
	
	/**
	 * 과정별 입금 내역 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectSubjReceiptMoneyList(Map<String, Object> map) throws Exception{
		return receiptMoneyManageDAO.selectSubjReceiptMoneyList(map);
	}
	
	
	/**
	 * 교육청 수납일자 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public int selectSubjseqDepositDateCnt(Map<String, Object> commandMap) throws Exception{
		return receiptMoneyManageDAO.selectSubjseqDepositDateCnt(commandMap);
	}
	
	/**
	 * 교육청 수납일자 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public Object selectSubjseqDepositDate(Map<String, Object> commandMap) throws Exception{
		return receiptMoneyManageDAO.selectSubjseqDepositDate(commandMap);
	}
	
	/**
	 * 교육청 수납일자 등록
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public int insertSubjseqDepositDate(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{			
			receiptMoneyManageDAO.insertSubjseqDepositDate(commandMap);			
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 교육청 수납일자 수정
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public int updateSubjseqDepositDate(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{			
			receiptMoneyManageDAO.updateSubjseqDepositDate(commandMap);			
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 과정별 입금 내역 total 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectSubjReceiptMoneyTotalList(Map<String, Object> map) throws Exception{
		return receiptMoneyManageDAO.selectSubjReceiptMoneyTotalList(map);
	}
	
	
	/**
	 * 교육청 수납일자 삭제
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public boolean deleteSubjseqDepositDate(Map<String, Object> commandMap)	throws Exception {
		boolean isok = false;
		try {
			
			receiptMoneyManageDAO.deleteSubjseqDepositDate(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}
	
	
	/**
	 * 과정별 입금 내역 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectYearMoneyTotalizationList(Map<String, Object> map) throws Exception{
		return receiptMoneyManageDAO.selectYearMoneyTotalizationList(map);
	}
	
	/**
     * 연도별입금내역 데이터 생성
     * @param commandMap
     * @return
     */
	public int insertYearReceiptMoneyStatus(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{	
			receiptMoneyManageDAO.deleteYearReceiptMoneyStatus(commandMap);			
			receiptMoneyManageDAO.insertYearReceiptMoneyStatus(commandMap);			
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		return isOk;
	}
	
	/**
     * 연도별입금내역 데이터 수정
     * @param commandMap
     * @return
     */
	public int yearReceiptMoneyStatusUpdate(Map<String, Object> commandMap) throws Exception {
		int resultCnt =0;
		List<Map<String, Object>> list = receiptMoneyManageDAO.selectYearMoneyTotalizationList(commandMap);
		log.error("totalCnt["+list.size()+"]");
		try{		
			for(Map<String, Object> map : list){
				Map<String, Object> paramMap = new HashMap<String, Object>();
				
				String year = map.get("year")+"";
				String upperclassno = map.get("upperclassno")+"";
				String subj = map.get("subj")+"";
				String subjseq = map.get("oSubjseq")+"";
				String deptCd = map.get("deptCd")+"";
				String deptNm = map.get("deptNm")+"";			
				String type = map.get("type")+"";				
				String deptIdx = map.get("deptIdx")+"";
				String pay = map.get("pay")+"";
				
				
				paramMap.put("year"			, year);
				paramMap.put("upperclassno"	, upperclassno);
				paramMap.put("subj"		, subj);
				paramMap.put("subjseq"	, subjseq);
				paramMap.put("deptCd"	, deptCd);			
				paramMap.put("deptNm"	, deptNm);
				paramMap.put("type"	, type);				
				paramMap.put("deptIdx"	, deptIdx);
				
				System.out.println("year -----> "+ year);
				System.out.println("upperclassno -----> "+ upperclassno);
				System.out.println("subj -----> "+ subj);
				System.out.println("subjseq -----> "+ subjseq);
				System.out.println("deptCd -----> "+ deptCd);
				System.out.println("deptNm -----> "+ deptNm);
				System.out.println("type -----> "+ type);
				System.out.println("deptIdx -----> "+ deptIdx);
				System.out.println("pay -----> "+ pay);
				
				
				
				paramMap.put("biyong"			, commandMap.get("biyong_"+subj+"_"+subjseq+"_"+deptIdx));
				paramMap.put("deptCnt"			, commandMap.get("deptCnt_"+subj+"_"+subjseq+"_"+deptIdx));
				paramMap.put("isgraduatedYDeptcnt"			, commandMap.get("isgraduatedYDeptcnt_"+subj+"_"+subjseq+"_"+deptIdx));
				paramMap.put("payDeptCnt"			, commandMap.get("payDeptCnt_"+subj+"_"+subjseq+"_"+deptIdx));
				//paramMap.put("pay"			, commandMap.get("pay_"+subj+"_"+subjseq+"_"+deptIdx));
				paramMap.put("payCnt"			, commandMap.get("payCnt_"+subj+"_"+subjseq+"_"+deptIdx+"_"+type));
				paramMap.put("payAmount"			, commandMap.get("payAmount_"+subj+"_"+subjseq+"_"+deptIdx+"_"+type));
				
				if("M99".equals(deptIdx)){
					paramMap.put("deptAmount"			, commandMap.get("deptAmount_"+subj+"_"+subjseq+"_"+deptIdx));
				}
				if(!"M99".equals(deptIdx)){
					paramMap.put("deptAmount"			, commandMap.get("deptAmount_"+subj+"_"+subjseq+"_"+type));
				}
				paramMap.put("subjseqAmount"			, commandMap.get("subjseqAmount_"+subj+"_"+subjseq));
				paramMap.put("isgraduatedNDeptcnt"			, commandMap.get("isgraduatedNDeptcnt_"+subj+"_"+subjseq+"_"+deptIdx));
				
						
				
				log.error("paramMap["+paramMap+"]");
				resultCnt += receiptMoneyManageDAO.updateYearReceiptMoneyStatus(paramMap);				
				receiptMoneyManageDAO.updateYearReceiptMoneyStatus_2(paramMap);
				
			}
		}catch(Exception e){
			log.error(e);
		}
		
		return resultCnt;
	}
	
	
	/**
	 * 연도별입금내역 데이터 total 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectYearMoneyTotalizationTotalList(Map<String, Object> map) throws Exception{
		return receiptMoneyManageDAO.selectYearMoneyTotalizationTotalList(map);
	}

	
}
