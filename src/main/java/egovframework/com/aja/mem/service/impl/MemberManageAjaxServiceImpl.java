package egovframework.com.aja.mem.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.excel.EgovExcelService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import egovframework.com.aja.mem.service.MemberManageAjaxService;
import egovframework.com.aja.mem.dao.MemberManageAjaxDAO;




/** 
 * 회원 관련 처리 비즈니스 구현 클래스를 정의한다.
 */

@Service("memberManageAjaxService")
public class MemberManageAjaxServiceImpl extends EgovAbstractServiceImpl implements MemberManageAjaxService{

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="memberManageAjaxDAO")
    private MemberManageAjaxDAO memberManageAjaxDAO;

	@Resource(name = "excelZipService")
    private EgovExcelService excelZipService;
	@Resource(name = "multipartResolver")
	CommonsMultipartResolver mailmultipartResolver;

    /**
     * 비밀번호 초기화
     * @param commandMap Map<String, Object> 
     * @return int
     * @exception Exception
     */
    public int updatePWInitialization(Map<String, Object> commandMap) throws Exception {
    	
    	int isOk = 1;
    	
    	Map<String,Object> inputMap = new HashMap<String,Object>(); 
    	
    	inputMap.put("targetUserId", commandMap.get("targetUserId"));
    	inputMap.put("suserId", commandMap.get("suserId"));
    	
    	//비밀번호가 초기화되는  회원정보
    	Map<String, Object> MemberInfo = (Map)memberManageAjaxDAO.selectMemberInfo(inputMap);
    	
    	inputMap.put("pw", MemberInfo.get("pw"));
    	inputMap.put("aspCode", MemberInfo.get("aspCode"));
    	inputMap.put("tempPw", MemberInfo.get("tempPw"));    	
    	
    	//회원정보에 임시비밀번호 여부 체크
    	int isOk1 = memberManageAjaxDAO.updateMemberInfoPw(inputMap);
    	
    	//임시비밀번호 변경 내역 저장
    	String isOk2 = memberManageAjaxDAO.insertPWInitialization(inputMap);

    	//보내는 회원정보
    	Map<String, Object> MemberSenderInfo = (Map)memberManageAjaxDAO.selectMemberSenderInfo(inputMap);
    	

/*
    	System.out.println("isOk1 : "+ isOk1);
    	System.out.println("isOk2 : "+ isOk2);
    	System.out.println("isOk3 : "+ isOk3);
*/  	
    	return isOk1;
    	
    }
    
   
}