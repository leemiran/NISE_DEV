package egovframework.com.aja.service.impl;


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

import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.com.aja.dao.CommonAjaxManageDAO;

/** 
 * AJAX 호출에 관한 공통 비즈니스 구현 클래스를 정의한다.
 */

@Service("commonAjaxManageService")
public class CommonAjaxManageServiceImpl extends EgovAbstractServiceImpl implements CommonAjaxManageService{

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="commonAjaxManageDAO")
    private CommonAjaxManageDAO commonAjaxManageDAO;

	@Resource(name = "excelZipService")
    private EgovExcelService excelZipService;
	@Resource(name = "multipartResolver")
	CommonsMultipartResolver mailmultipartResolver;

	/**
	 * 공통 조회 List
	 * @param commandMap Map<String, Object> 
	 * @return int
	 * @exception Exception
	 */
    public List selectCommonAjaxManageList(Map<String, Object> commandMap, String sqlStr) throws Exception {
        return commonAjaxManageDAO.selectCommonAjaxManageList(commandMap, sqlStr);
	}
	
    /**
	 * 공통 조회 int
	 * @param commandMap Map<String, Object> 
	 * @return int
	 * @exception Exception
	 */
    public int selectCommonAjaxManageInt(Map<String, Object> commandMap, String sqlStr) throws Exception {
        return commonAjaxManageDAO.selectCommonAjaxManageInt(commandMap, sqlStr);
	}
    
    /**
     * 공통 Insert int
     * @param commandMap Map<String, Object> 
     * @return int
     * @exception Exception
     */
    public int insertCommonAjaxManageInt(Map<String, Object> commandMap, String sqlStr) throws Exception {
    	int isOk = 1;
    	try{
    		commonAjaxManageDAO.insertCommonAjaxManageInt(commandMap, sqlStr);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return isOk;
    }
    
    /**
	 * 공통 조회 Object
	 * @param commandMap Map<String, Object> 
	 * @return int
	 * @exception Exception
	 */
    public Object selectCommonAjaxManageObject(Map<String, Object> commandMap, String sqlStr) throws Exception {
        return commonAjaxManageDAO.selectCommonAjaxManageObject(commandMap, sqlStr);
	}
    
    
    /**
     * 공통 조회 map
     * @param commandMap Map<String, Object> 
     * @return int
     * @exception Exception
     */
    public Map selectCommonAjaxManageMap(Map<String, Object> commandMap, String sqlStr) throws Exception {
    	return commonAjaxManageDAO.selectCommonAjaxManageMap(commandMap, sqlStr);
    }
    
    /**
     * 공통 Update int
     * @param commandMap Map<String, Object> 
     * @return int
     * @exception Exception
     */
    public int updateCommonAjaxManageInt(Map<String, Object> commandMap, String sqlStr) throws Exception {
    	int isOk = 1;
    	try{
    		commonAjaxManageDAO.updateCommonAjaxManageInt(commandMap, sqlStr);
    	}catch(Exception ex){
    		isOk = 0;
    		ex.printStackTrace();
    	}
    	return isOk;
    }
    
    public boolean nicePersonalNumOverlap(Map<String, Object> commandMap, String sqlStr)
	throws Exception {
	boolean isOk = false;
	
	try{
		
		String  p_userid = (String) commandMap.get("p_userid");
		String  p_nicePersonalNum = (String) commandMap.get("p_nicePersonalNum");
		
		System.out.println("@@@@@@@@@@@@@@@@@@ p_userid  "+p_userid);
		System.out.println("@@@@@@@@@@@@@@@@@@ p_nicePersonalNum  "+p_nicePersonalNum);
		
		HashMap<String, Object> mm = new HashMap<String, Object>();
		
		mm.put("p_userid", p_userid);
		mm.put("p_nicePersonalNum", p_nicePersonalNum);
		
		int nicePersonalNumOverlap = commonAjaxManageDAO.nicePersonalNumOverlap(mm, sqlStr);
		System.out.println("@@@@@@@@@@@@@@@@@    "+nicePersonalNumOverlap);
		if(nicePersonalNumOverlap > 0){
			isOk = true;
		}else{
			isOk = false;
		}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	
	return isOk;
}

    public Map nicePersonalChkValue(Map<String, Object> commandMap) throws Exception {
//, "commonAjaxDAO.nicePersonalChkValue"
	return commonAjaxManageDAO.nicePersonalChkValue(commandMap);
}
    
}