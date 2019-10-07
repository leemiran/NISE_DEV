package egovframework.adm.lcms.sco.service.impl;


import java.util.ArrayList;
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

import egovframework.adm.lcms.sco.service.ScormContentService;
import egovframework.adm.lcms.sco.dao.ScormContentDAO;

/** 
 * 코드목록관리, 생성 처리하는 비즈니스 구현 클래스를 정의한다.
 */

@Service("scormContentService")
public class ScormContentServiceImpl extends EgovAbstractServiceImpl implements ScormContentService{

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="scormContentDAO")
    private ScormContentDAO scormContentDAO;

	@Resource(name = "excelZipService")
    private EgovExcelService excelZipService;
	@Resource(name = "multipartResolver")
	CommonsMultipartResolver mailmultipartResolver;

	/**
	 * 과목 목록을 조회
	 * @param Map commandMap 
	 * @return List
	 * @exception Exception
	 */
	public List selectScormContentPageList(Map<String, Object> commandMap) throws Exception {
   		return scormContentDAO.selectScormContentList(commandMap);
	}
	
	/**
	 * 과목 목록 총건수를 조회한다.
	 * @param commandMap Map<String, Object> 
	 * @return int
	 * @exception Exception
	 */
    public int selectScormContentListTotCnt(Map<String, Object> commandMap) throws Exception {
        return scormContentDAO.selectScormContentListTotCnt(commandMap);
	}
    
    /**
     * 차시 목록을 조회
     * @param Map commandMap 
     * @return List
     * @exception Exception
     */
    public List selectScormNewScoPageList(Map<String, Object> commandMap) throws Exception {
		return scormContentDAO.selectScormNewScoPageList(commandMap);
    }
    
    /**
     * 차시 등록정보 조회
     * @param Map commandMap 
     * @return List
     * @exception Exception
     */
    public Map selectScormCreateInfo(Map<String, Object> commandMap) throws Exception {
    	return scormContentDAO.selectScormCreateInfo(commandMap);
    }
    
    
    public List selectFileBaseDirList(Map<String, Object> commandMap) throws Exception{
    	return scormContentDAO.selectFileBaseDirList(commandMap);
    }

    public List selectProgressLogList(Map<String, Object> commandMap) throws Exception{
    	return scormContentDAO.selectProgressLogList(commandMap);
    }
    
    public int deleteProgressLog(Map<String, Object> commandMap) throws Exception{
    	scormContentDAO.deleteTocLog(commandMap);
    	scormContentDAO.deleteLog(commandMap);
    	return 1;
    }
}