package egovframework.adm.lcms.xin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.nct.dao.LcmsLessonDAO;
import egovframework.adm.lcms.nct.dao.LcmsModuleDAO;
import egovframework.adm.lcms.xin.service.XinicsContentService;
import egovframework.adm.lcms.xin.dao.XinicsContentDAO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("xinicsContentService")
public class XinicsContentServiceImpl extends EgovAbstractServiceImpl implements XinicsContentService{

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="xinicsContentDAO")
    private XinicsContentDAO xinicsContentDAO;
	
	@Resource(name="lcmsModuleDAO")
	private LcmsModuleDAO lcmsModuleDAO;
	
	@Resource(name="lcmsLessonDAO")
	private LcmsLessonDAO lcmsLessonDAO;
	
	
	/**
	 * 자이닉스교과 총수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectXinicsContentListTotCnt(Map<String, Object> commandMap) throws Exception{
		return xinicsContentDAO.selectXinicsContentListTotCnt(commandMap);
	}
	
	/**
	 * 자이닉스교과 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectXinicsContentList(Map<String, Object> commandMap) throws Exception{
		return xinicsContentDAO.selectXinicsContentList(commandMap);
	}
	
	/**
	 * 자이닉스컨텐츠 등록
	 */
	public int insertXinicsContent(ArrayList dataList) throws Exception{
		int isOk = 1;
		try{
    		Map inputMap = null;
    		int orderNum = 1;
    		for( int idx=0; idx<dataList.size(); idx++ ){
    			LcmsScormModel scorm = new LcmsScormModel();
    			
    			Map<String, Object> commandMap = (Map)dataList.get(idx);
    			
    			ArrayList moduleList = (ArrayList)commandMap.get("organization");
    			ArrayList itemResourceList = (ArrayList)commandMap.get("itemResourceList");
    			ArrayList dependencyList = (ArrayList)commandMap.get("dependencyList");
    			ArrayList itemList = (ArrayList)commandMap.get("itemList");
    			String userid = (String)commandMap.get("userid");
    			String subj = (String)commandMap.get("course");
    			
    			String[] module = new String[moduleList.size()];
    			for( int i=0; i<moduleList.size(); i++ ){
    				inputMap = (Map)moduleList.get(i);
    				inputMap.put("userId", userid);
    				inputMap.put("subj", subj);
    				
    				module[i] = lcmsModuleDAO.selectModuleNum(inputMap);
    				
    				inputMap.put("module", module[i]);
    				
    				if (((String)inputMap.get("orgTitle")).length() > 200) {//"Title 이 테이블 칼럼의 Max 값을 초과합니다.");
    					isOk = 10;
    					return isOk;
    				}
    				lcmsModuleDAO.insertLcmsModule(inputMap);
    				inputMap.put("orderNum", orderNum++);
    				for( int j=0; j<itemList.size(); j++ ){
    					Map temp = (Map)itemList.get(i);
    					String path = (String)temp.get("imsPath");
    					path = path.substring(path.lastIndexOf("/"), path.length());
    					
    					inputMap.put("lessonName", temp.get("itemTitle"));
//    					inputMap.put("starting", crsCode + path + temp.get("itemStartFile"));
    					inputMap.put("starting", subj + path + "/default.htm"); //학습창 새창으로열리지 않도록 픽스
    					lcmsLessonDAO.insertLcmsLessonXinics(inputMap);
    				}
    			}
    		}
    	}catch(Exception ex){
    		isOk = 20;
    		log.info("LcmsOrganizationServiceImpl Exception insertLcmsOrganization");
    		log.info("Exception : "+ex);
    		log.info(" "+ex.getMessage());
    		log.info(" "+ex.getStackTrace());
    	}
		return isOk; 
	}
	
	/**
	 * 자이닉스 컨텐츠 차시리스트
	 */
	public List selectXinicsOrgList(Map<String, Object> commandMap) throws Exception{
		return xinicsContentDAO.selectXinicsOrgList(commandMap);
	}
	
}
