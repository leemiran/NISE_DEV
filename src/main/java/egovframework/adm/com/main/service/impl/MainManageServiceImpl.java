package egovframework.adm.com.main.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
/* 엑셀 처리 POI lib */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import egovframework.adm.com.main.service.MainManageService;
import egovframework.adm.com.main.dao.MainManageDAO;

/** 
 * 메뉴목록관리, 생성, 사이트맵을 처리하는 비즈니스 구현 클래스를 정의한다.
 * @author 개발환경 개발팀 이용
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  이  용          최초 생성
 *
 * </pre>
 */

@Service("mainManageService")
public class MainManageServiceImpl extends EgovAbstractServiceImpl implements MainManageService{

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="mainManageDAO")
    private MainManageDAO mainManageDAO;
	
	
	/**
	 * 관리자 메인 과정리스트 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectTotalSubSeqList(Map<String, Object> commandMap) throws Exception{
    	return mainManageDAO.selectTotalSubSeqList(commandMap);
    }
	
    /**
	 * 관리자 메인 공지사항 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectNoticeBoardList(Map<String, Object> commandMap) throws Exception{
    	return mainManageDAO.selectNoticeBoardList(commandMap);
    }
    
    
    /**
	 * 관리자 메인 질문방, 자료실 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectPdsBoardList(Map<String, Object> commandMap) throws Exception{
    	return mainManageDAO.selectPdsBoardList(commandMap);
    }

    /**
	 * 관리자 메인 연수문의 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectQnaBoardList(Map<String, Object> commandMap)
			throws Exception {
		return mainManageDAO.selectQnaBoardList(commandMap);
	}

	/**
     * 당해 연도 운영 현황 데이터 생성
     * @param commandMap
     * @return
     */
	public List<Map<String, Object>> admYearEduStatus(Map<String, Object> commandMap) throws Exception {
		List<Map<String, Object>> list = mainManageDAO.admYearEduStatus(commandMap);
		int cnt = 0;
		int delCnt = mainManageDAO.admYearEduStatusDelete(commandMap);
		log.error(list.size());
		try{			
			for(Map<String, Object> map : list){
				map.put("userid", commandMap.get("userid"));
				cnt += mainManageDAO.admYearEduStatusInsert(map);
			}
		}catch(Exception e){
			log.error(e);
			list = null;
		}
		log.error("당해 연도 운영 현황 등록 건수["+cnt+"]");
		return list;
	}

	/**
     * 당해 연도 운영 현황 내역 조회
     * @param commandMap
     * @return
     */
	public List<Map<String, Object>> admYearEduStatusList(Map<String, Object> commandMap) throws Exception {
		return mainManageDAO.admYearEduStatusList(commandMap);
	}

	/**
     * 당해 연도 운영 현황 수정
     * @param commandMap
     * @return
     */
	public int admYearEduStatusUpdate(Map<String, Object> commandMap) throws Exception {
		int resultCnt =0;
		List<Map<String, Object>> list = mainManageDAO.admYearEduStatusList(commandMap);
		log.error("totalCnt["+list.size()+"]");
		try{		
		for(Map<String, Object> map : list){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String grseq = map.get("grseq")+"";
			String subjseq = map.get("subjseq")+"";
			paramMap.put("year"			, map.get("year"));
			paramMap.put("grseq"		, grseq);
			paramMap.put("subjseq"	, subjseq);
			
			paramMap.put("planCnt"			, commandMap.get("planCnt_"+grseq+"_"+subjseq));
			paramMap.put("educationInsti"	, commandMap.get("educationInsti_"+grseq+"_"+subjseq));
			paramMap.put("incomeAmount"	, commandMap.get("incomeAmount_"+grseq+"_"+subjseq));
			paramMap.put("propoCnt"			, commandMap.get("propoCnt_"+grseq+"_"+subjseq));
			paramMap.put("groupTotal"		, commandMap.get("groupTotal_"+grseq+"_"+subjseq));
			paramMap.put("groupCnt"			, commandMap.get("groupCnt_"+grseq+"_"+subjseq));
			paramMap.put("presentCnt"		, commandMap.get("presentCnt_"+grseq+"_"+subjseq));
			paramMap.put("graduatedCnt"		, commandMap.get("graduatedCnt_"+grseq+"_"+subjseq));
			paramMap.put("normalProCnt"		, commandMap.get("normalProCnt_"+grseq+"_"+subjseq));
			paramMap.put("groupProCnt"		, commandMap.get("groupProCnt_"+grseq+"_"+subjseq));
			paramMap.put("chkfinalCnt"		, commandMap.get("chkfinalCnt_"+grseq+"_"+subjseq));
			
			
			log.error("paramMap["+paramMap+"]");
			resultCnt += mainManageDAO.admYearEduStatusUpdate(paramMap);
		}
		}catch(Exception e){
			log.error(e);
		}
		
		return resultCnt;
	}

	/**
     * 당해 연도 운영 현황 삭제
     * @param commandMap
     * @return
     */
	public int admYearEduStatusDelete(Map<String, Object> commandMap) throws Exception {
		return mainManageDAO.admYearEduStatusDelete(commandMap);
	}
    
    
	

}
