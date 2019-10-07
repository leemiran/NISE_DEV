/* 
 * LcmsOrganizationServiceImpl.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.service.impl;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import org.adl.sequencer.SeqActivityTree;
import org.adl.validator.ADLValidatorOutcome;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.lcms.api.com.SetVO;
import org.lcms.api.com.Util;
import org.lcms.api.coursemapdao.MapStudyParamObject;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.tmax.tibero.jdbc.TbBlob;

import egovframework.rte.fdl.excel.EgovExcelService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.cts.service.LcmsOrganizationService;
import egovframework.adm.lcms.cts.dao.LcmsCourseMapDAO;
import egovframework.adm.lcms.cts.dao.LcmsCourseOrgDAO;
import egovframework.adm.lcms.cts.dao.LcmsFileDAO;
import egovframework.adm.lcms.cts.dao.LcmsItemDAO;
import egovframework.adm.lcms.cts.dao.LcmsItemResourceDAO;
import egovframework.adm.lcms.cts.dao.LcmsManiseqDAO;
import egovframework.adm.lcms.cts.dao.LcmsMetadataDAO;
import egovframework.adm.lcms.cts.dao.LcmsOrganizationDAO;
import egovframework.adm.lcms.cts.dao.LcmsOrganizationLogDAO;
import egovframework.adm.lcms.cts.dao.LcmsResourceDependencyDAO;
import egovframework.adm.lcms.cts.dao.LcmsScormSequenceDAO;
import egovframework.adm.lcms.cts.dao.LcmsSeqConditionTypeDAO;
import egovframework.adm.lcms.cts.dao.LcmsSeqMapInfoDAO;
import egovframework.adm.lcms.cts.dao.LcmsSeqObjectivesDAO;
import egovframework.adm.lcms.cts.dao.LcmsSeqRollupConditionDAO;
import egovframework.adm.lcms.cts.dao.LcmsSeqRollupRuleDAO;
import egovframework.adm.lcms.cts.dao.LcmsSeqRuleConditionDAO;
import egovframework.adm.lcms.cts.dao.LcmsSerializerDAO;
import egovframework.adm.lcms.cts.domain.LcmsOrganization;
import egovframework.adm.lcms.ims.mainfest.ImportMsg;
import egovframework.adm.lcms.ims.mainfest.ServerManifestHandler;
import egovframework.adm.lcms.ims.mainfest.manifestTableBean;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import org.apache.log4j.Logger;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsOrganizationServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsOrganizationService")
public class LcmsOrganizationServiceImpl extends EgovAbstractServiceImpl implements LcmsOrganizationService {
    @Resource(name="lcmsOrganizationDAO")
    private LcmsOrganizationDAO lcmsOrganizationDAO;
    
    @Resource(name="lcmsCourseOrgDAO")
    private LcmsCourseOrgDAO lcmsCourseOrgDAO;
    
    @Resource(name="lcmsMetadataDAO")
    private LcmsMetadataDAO lcmsMetadataDAO;
    
    @Resource(name="lcmsSerializerDAO")
    private LcmsSerializerDAO lcmsSerializerDAO;
    
    @Resource(name="lcmsManiseqDAO")
    private LcmsManiseqDAO lcmsManiseqDAO;
    
    @Resource(name="lcmsItemResourceDAO")
    private LcmsItemResourceDAO lcmsItemResourceDAO;
    
    @Resource(name="lcmsItemDAO")
    private LcmsItemDAO lcmsItemDAO;
    
    @Resource(name="lcmsResourceDependencyDAO")
    private LcmsResourceDependencyDAO lcmsResourceDependencyDAO;
    
    @Resource(name="lcmsScormSequenceDAO")
    private LcmsScormSequenceDAO lcmsScormSequenceDAO;
    
    @Resource(name="lcmsSeqConditionTypeDAO")
    private LcmsSeqConditionTypeDAO lcmsSeqConditionTypeDAO;
    
    @Resource(name="lcmsSeqRuleConditionDAO")
    private LcmsSeqRuleConditionDAO lcmsSeqRuleConditionDAO;
    
    @Resource(name="lcmsSeqRollupRuleDAO")
    private LcmsSeqRollupRuleDAO lcmsSeqRollupRuleDAO;
    
    @Resource(name="lcmsSeqRollupConditionDAO")
    private LcmsSeqRollupConditionDAO lcmsSeqRollupConditionDAO;
    
    @Resource(name="lcmsSeqObjectivesDAO")
    private LcmsSeqObjectivesDAO lcmsSeqObjectivesDAO;
    
    @Resource(name="lcmsSeqMapInfoDAO")
    private LcmsSeqMapInfoDAO lcmsSeqMapInfoDAO;
    
    @Resource(name="lcmsOrganizationLogDAO")
    private LcmsOrganizationLogDAO lcmsOrganizationLogDAO;
    
    @Resource(name="lcmsCourseMapDAO")
    private LcmsCourseMapDAO lcmsCourseMapDAO;
    
    @Resource(name="lcmsFileDAO")
    private LcmsFileDAO lcmsFileDAO;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    public List selectLcmsOrganizationPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationDAO.selectLcmsOrganizationPageList( commandMap);
    }

    public int selectLcmsOrganizationPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationDAO.selectLcmsOrganizationPageListTotCnt( commandMap);
    }

    public List selectLcmsOrganizationList( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationDAO.selectLcmsOrganizationList( commandMap);
    }

    public Object selectLcmsOrganization( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationDAO.selectLcmsOrganization( commandMap);
    }

    public int insertLcmsOrganization( ArrayList dataList ) throws Exception {
    	
    	int isOk = 1;
    	try{
    		Map orgMap = null;
    		Map metadataMap = null;
    		for( int idx=0; idx<dataList.size(); idx++ ){
    			LcmsScormModel scorm = new LcmsScormModel();
    			
    			Map<String, Object> commandMap = (Map)dataList.get(idx);
    			
    			ArrayList org = (ArrayList)commandMap.get("organization");
    			ArrayList met = (ArrayList)commandMap.get("metadata");
    			ArrayList ser = (ArrayList)commandMap.get("serializerList");
    			ArrayList itemResourceList = (ArrayList)commandMap.get("itemResourceList");
    			ArrayList itemFileList = (ArrayList)commandMap.get("file");
    			ArrayList dependencyList = (ArrayList)commandMap.get("dependencyList");
    			ArrayList itemList = (ArrayList)commandMap.get("itemList");
    			ArrayList sequencingList = (ArrayList)commandMap.get("sequencingList");
    			ArrayList conditionList = (ArrayList)commandMap.get("conditionList");
    			ArrayList ruleConditionList = (ArrayList)commandMap.get("ruleConditionList");
    			ArrayList rollupRuleList = (ArrayList)commandMap.get("rollupRuleList");
    			ArrayList rollupcondList = (ArrayList)commandMap.get("rollupcondList");
    			ArrayList objectiveList = (ArrayList)commandMap.get("objectiveList");
    			ArrayList mapInfoList = (ArrayList)commandMap.get("mapInfoList");
    			MapStudyParamObject mapobj = (MapStudyParamObject)commandMap.get("mapobj");
    			
    			String userid = (String)commandMap.get("userid");
    			
    			String[] seq = new String[org.size()];
    			for( int i=0; i<org.size(); i++ ){
    				orgMap = (Map)org.get(i);
    				metadataMap = (Map)met.get(i);
    				
    				seq[i] = String.valueOf(lcmsOrganizationDAO.getOrgSeqNum(orgMap));
    				
    				orgMap.put("orgSeq", seq[i]);
    				int crsOrgNo = lcmsCourseOrgDAO.selectCrsOrgNo(orgMap);
    				orgMap.remove("crsOrgNo");
    				orgMap.put("crsOrgNo", crsOrgNo);
    				
    				if (((String)orgMap.get("orgTitle")).length() > 100) {//"Title 이 테이블 칼럼의 Max 값을 초과합니다.");
    					isOk = 10;
    					return isOk;
    				}
    				if (lcmsOrganizationDAO.getOrgaidCnt(orgMap) > 0) {//"Organization Id 값이 중복되었습니다.\\n manifest file 의 항목을 확인하십시오.");
    					isOk = 20;
    					return isOk;
    				}
    				lcmsOrganizationDAO.insertLcmsOrganization(orgMap);
    				
    				if( orgMap.get("metaLocation") != null && !orgMap.get("metaLocation").toString().equals("") ){
    					metadataMap.put("objSeq", seq[i]);
    					metadataMap.put("userid", userid);
    					this.insertMetadata(metadataMap, scorm);
    				}
    			}
    			Map<String, Object> inputMap = null;
    			for( int i=0; i<ser.size(); i++ ){
    				inputMap = (Map)ser.get(i);
    				inputMap.put("orgSeq", seq[i]);
					lcmsSerializerDAO.insertLcmsSerializer(inputMap);
    			}
    			
    			inputMap = null;
    			int checkCount = 0;
    			String[][] retResource = new String[itemResourceList.size()][2];
    			for( int i=0; i<itemResourceList.size(); i++ ){
    				inputMap = (Map)itemResourceList.get(i);
    				int rsrcSeq = 0;
    				// maniseq 추가 컨텐츠등록순번
    				int maniseq = (Integer)lcmsManiseqDAO.insertLcmsManiseq(inputMap);
    				if( checkCount == 0 ){
    					String source_file = (String)inputMap.get("imsPath");//substring(0, filename.lastIndexOf(".zip"));
    					String to_folder = (String)inputMap.get("toFolder");
    					String to_file = "";
    					
    					if(!"null".equals(to_folder) && null != to_folder && !"".equals(to_folder)){
    						to_file = to_folder;
    					}else{
    						to_file = Globals.CONTNET_REAL_PATH+ File.separator+ maniseq;
    					}
    					
    					FileController.moveDirs(new File(source_file), new File(to_file));
    					checkCount++;
    				}
    				if( inputMap.get("rsrcTitle") != null && !inputMap.get("rsrcTitle").toString().equals("") ){
    					inputMap.put("maniSeq", maniseq);
    					
    					rsrcSeq = lcmsItemResourceDAO.selectLcmsItemResourceSeq(inputMap);
    					
    					retResource[i][0] = String.valueOf(rsrcSeq);
    					retResource[i][1] = (String)inputMap.get("rsrcId");
    					
    					inputMap.put("rsrcSeq", rsrcSeq);
    					inputMap.put("userid", userid);
    					
    					lcmsItemResourceDAO.insertLcmsItemResource(inputMap);
    					if( ((String)inputMap.get("metadataType")).equals("Y") && ((String)inputMap.get("flagtemplate")).equals("Y")){
    						metadataMap = new HashMap();
    						metadataMap.put("metadataSeq", lcmsMetadataDAO.selectLcmsMetadataSeq(inputMap));
    						metadataMap.put("objType", inputMap.get("objType"));
    						metadataMap.put("metaLocation", inputMap.get("metaLocation"));
    						
    						this.insertMetadata(metadataMap, scorm);
    					}
    					
    				}
    				Map fileMap;
    				for( int j=0; j<itemFileList.size(); j++ ){
    					fileMap = (Map)itemFileList.get(j);
    					if( i == Integer.valueOf((String)fileMap.get("idx")) ){
    						fileMap.put("rsrcSeq", rsrcSeq);
    						fileMap.put("userId", userid);
    						lcmsFileDAO.insertLcmsFile(fileMap);
    					}
    				}
    				
    			}
    			// 자료그룹등록 
    			inputMap = null;
    			for( int i=0; i<dependencyList.size(); i++ ){
    				inputMap = (Map)dependencyList.get(i);
    				logger.info("자료그룹 : "+retResource[(Integer)inputMap.get("rsrcIdx")]);
    				logger.info("자료그룹 : "+retResource[(Integer)inputMap.get("rsrcIdx")][0]);
    				if( !retResource[(Integer)inputMap.get("rsrcIdx")].equals("99") ){
    					inputMap.put("rsrcSeq", retResource[(Integer)inputMap.get("rsrcIdx")][0]);
    					inputMap.put("userid", userid);
    					lcmsResourceDependencyDAO.insertLcmsResourceDependency(inputMap);
    				}
    			}
    			
    			inputMap = null;
    			String newOrgSeq = "";
    			int item_org_no = 0;
				String[][] retItem = new String[itemList.size()][2];
				for( int i=0; i<itemList.size(); i++ ){
					inputMap = (Map)itemList.get(i);
					// seq
					int itemSeq = lcmsItemDAO.selectLcmsItemMaxSeq(inputMap);
					
					if (inputMap.get("itemParameters").toString().length() > 1000) {
						logger.debug("Parameter Attribute 의 길이가 테이블 칼럼의 Max 값을 초과합니다.");
						isOk = 30;
						return isOk;
					}
					logger.info("itemTitle : "+inputMap.get("itemTitle"));
					if (inputMap.get("itemTitle").toString().length() > 100) {
						logger.debug("Title 이 테이블 칼럼의 Max 값을 초과합니다.\n");
						isOk = 40;
						return isOk;
					}
					if (inputMap.get("dataFromLms").toString().length() > 4000) {
						logger.debug("dataFromLMS의 값이 테이블 칼럼의 Max 값을 초과합니다.\n");
						isOk = 50;
						return isOk;
					}
					
					logger.info("itemId : "+inputMap.get("itemId"));
					// 2005.3.1 linuzer, org_cd추가
					if (lcmsItemDAO.selectLcmsItemIdCnt(inputMap) > 0) {
						logger.debug("Item Id 값이 중복되었습니다.\n manifest file 의 항목을 확인하십시오.");
						isOk = 60;
						return isOk;
					}
					
					//setHigh_item_seq(parekey)
					int parekey = lcmsItemDAO.selectLcmsItemHighItem(inputMap);
					inputMap.put("orgSeq", seq[Integer.valueOf((String)inputMap.get("seqIdx"))]);
					inputMap.put("itemSeq", itemSeq);
					inputMap.put("highItemSeq", parekey);
					inputMap.put("userid", userid);
					
					if (!newOrgSeq.equals(seq[Integer.valueOf((String)inputMap.get("seqIdx"))])) {
						item_org_no = 1;
					}
					inputMap.put("orgItemNo", item_org_no);
					
					lcmsItemDAO.insertLcmsItem(inputMap);
					
					newOrgSeq = seq[Integer.valueOf((String)inputMap.get("seqIdx"))];
					item_org_no++;
					retItem[i][0] = (String)inputMap.get("seqIdx"); //sequence seq
					retItem[i][1] = String.valueOf(itemSeq); // item seq
					
					//LCMS_ITEM의 RSRC_NO COLUMN UPDATE
					if( inputMap.get("itemIdRef") != null && !inputMap.get("itemIdRef").toString().equals("") ){
						HashMap m = new HashMap();
						for( int j=0; j<retResource.length; j++ ){
							if( inputMap.get("itemIdRef").toString().equals(retResource[j][1]) ){
								m.put("rsrcId", retResource[j][0]);
							}
						}
						m.put("itemSeq", itemSeq);
						lcmsItemDAO.updateLcmsItemRsrcId(m);
					}
					metadataMap = new HashMap();
					if( inputMap.get("metaLocation") != null && !inputMap.get("metaLocation").toString().equals("") ){
						metadataMap.put("objSeq", itemSeq);
						metadataMap.put("objType", "ITM");
						metadataMap.put("userid", userid);
						metadataMap.put("metaLocation", (String)inputMap.get("imsPath")+File.separator+inputMap.get("metaLocation"));
						this.insertMetadata(metadataMap, scorm);
					}
				}
				
				inputMap = null;
				String[] sequencingSeq = new String[sequencingList.size()];
				for( int i=0; i<sequencingList.size(); i++ ){
					inputMap = (Map)sequencingList.get(i);
					//SEQUENCING 의 시퀀스 값 Return
					sequencingSeq[i] = String.valueOf(lcmsScormSequenceDAO.selectLcmsScormSequenceMaxSeq(inputMap));
					inputMap.put("seqIdxNum", sequencingSeq[i]);
					
					if( inputMap.get("itemIndex").toString().equals("") ){
						inputMap.put("itemSeq", 0);
					}else{
						inputMap.put("itemSeq", retItem[Integer.valueOf((String)inputMap.get("itemIndex"))][1]);
					}
					inputMap.put("orgSeq", seq[Integer.valueOf((String)inputMap.get("orgIndex"))]);
					inputMap.put("userid", userid);
					
					lcmsScormSequenceDAO.insertLcmsScormSequence(inputMap);
				}
				
				inputMap = null;
				String[] conditionSeq = new String[conditionList.size()];
				for( int i=0; i<conditionList.size(); i++ ){
					inputMap = (Map)conditionList.get(i);
					conditionSeq[i] = String.valueOf(lcmsSeqConditionTypeDAO.selectLcmsSeqConditionTypeMaxNum(inputMap));
					inputMap.put("ctIdxNum", conditionSeq[i]);
					inputMap.put("seqIdNum", sequencingSeq[Integer.valueOf((String)inputMap.get("idx"))]);
					
					lcmsSeqConditionTypeDAO.insertLcmsSeqConditionType(inputMap);
				}
				
				//SEQ_RULE_CONDITION 테이블
				inputMap = null;
				String[] ruleCondSeq = new String[ruleConditionList.size()];
				for( int i=0; i<ruleConditionList.size(); i++ ){
					inputMap = (Map)ruleConditionList.get(i);
					ruleCondSeq[i] = String.valueOf(lcmsSeqRuleConditionDAO.selectLcmsSeqRuleConditionMaxNum(inputMap)); 
					inputMap.put("rcIdxNum", ruleCondSeq[i]);
					inputMap.put("ctIdxNum", conditionSeq[Integer.valueOf((String)inputMap.get("index"))]);
					inputMap.put("userid", userid);
					
					lcmsSeqRuleConditionDAO.insertLcmsSeqRuleCondition(inputMap);
				}
				
				//SEQ_ROLLUP_RULE 테이블 INSERT
				inputMap = null;
				String[] rollupRuleSeq = new String[rollupRuleList.size()];
				for( int i=0; i<rollupRuleList.size(); i++ ){
					inputMap = (Map)rollupRuleList.get(i);
					rollupRuleSeq[i] = String.valueOf(lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRuleMaxNum(inputMap));
					inputMap.put("rrIdxNum", rollupRuleSeq[i]);
					inputMap.put("seqIdxNum", sequencingSeq[Integer.valueOf((String)inputMap.get("index"))]);
					inputMap.put("userid", userid);
					
					lcmsSeqRollupRuleDAO.insertLcmsSeqRollupRule(inputMap);
				}
				
				inputMap = null;
				String[] rollupCondSeq = new String[rollupcondList.size()];
				for( int i=0; i<rollupcondList.size(); i++ ){
					inputMap = (Map)rollupcondList.get(i);
					rollupCondSeq[i] = String.valueOf(lcmsSeqRollupConditionDAO.selectLcmsSeqRollupConditionMaxNum(inputMap));
					inputMap.put("rlcIdxNum", rollupCondSeq[i]);
					inputMap.put("rrIdxNum", rollupRuleSeq[Integer.valueOf((String)inputMap.get("index"))]);
					inputMap.put("userid", userid);
					
					lcmsSeqRollupConditionDAO.insertLcmsSeqRollupCondition(inputMap);
				}
				
				inputMap = null;
				String[] objectiveSeq = new String[objectiveList.size()];
				for( int i=0; i<objectiveList.size(); i++ ){
					inputMap = (Map)objectiveList.get(i);
					objectiveSeq[i] = String.valueOf(lcmsSeqObjectivesDAO.selectLcmsSeqObjectivesMaxNum(inputMap));
					inputMap.put("objectiveIdxNum", objectiveSeq[i]);
					inputMap.put("seqIdxNum", sequencingSeq[Integer.valueOf((String)inputMap.get("index"))]);
					inputMap.put("userid", userid);
					
					lcmsSeqObjectivesDAO.insertLcmsSeqObjectives(inputMap);
				}
				
				inputMap = null;
				String[] mapInfoSeq = new String[mapInfoList.size()];
				for( int i=0; i<mapInfoList.size(); i++ ){
					inputMap = (Map)mapInfoList.get(i);
					mapInfoSeq[i] = String.valueOf(lcmsSeqMapInfoDAO.selectLcmsSeqMapInfoMaxNum(inputMap));
					
					inputMap.put("rlcIdxNum", mapInfoSeq[i]);
					inputMap.put("objectiveIdxNum", objectiveSeq[Integer.valueOf((String)inputMap.get("index"))]);
					inputMap.put("userid", userid);
					
					lcmsSeqMapInfoDAO.insertLcmsSeqMapInfo(inputMap);
				}
				
//				inputMap = null;
//				for( int i=0; i<org.size(); i++ ){
//					int logSeq = lcmsOrganizationLogDAO.selectLcmsOrganizationLogMaxNum(inputMap);
//					if( retItem.length > 0 ){
//						inputMap.put("logSeq", logSeq);
//						inputMap.put("orgSeq", seq[Intger.valueOf()])
//					}
//				}
				
				
				// LCMS_COURSE_ORG INSERT (과정차시정보)
				inputMap = null;
				for( int i=0; i<seq.length; i++ ){
					inputMap = new HashMap();
					inputMap.put("courseSeq", commandMap.get("course"));
					inputMap.put("orgSeq", seq[i]);
					inputMap.put("crsOrgNo", 0);
					int crsOrgNo = lcmsCourseOrgDAO.selectCrsOrgNo(inputMap);
					inputMap.put("crsOrgNo", crsOrgNo);
					inputMap.put("userid", userid);
					lcmsCourseOrgDAO.insertLcmsCourseOrg(inputMap);
				}
				
				inputMap = null;
//				for( int i=0; i<seq.length; i++ ){
//					inputMap = new HashMap();
//					inputMap.put("orgSeq", seq[i]);
//					inputMap.put("courseCd", mapobj.getCourse_cd());
//					inputMap.put("lmsCourseCd", mapobj.getLms_course_cd());
//					inputMap.put("lmsWeekCd", mapobj.getLms_week_cd());
//					inputMap.put("serviceYn", mapobj.getService_yn());
//					inputMap.put("userid", userid);
//					
//					lcmsCourseMapDAO.insertLcmsCourseMap(inputMap);
//				}
    		}
    	}catch(Exception ex){
    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    		logger.info("LcmsOrganizationServiceImpl Exception insertLcmsOrganization");
    		logger.info("Exception : "+ex);
    		logger.info(" "+ex.getMessage());
    		logger.info(" "+ex.getStackTrace());
    	}
        return isOk;
    }
    
    public int insertMetadata(Map<String, Object> metadataMap, LcmsScormModel scorm) throws Exception{
    	int isOk = 1;
    	try{
    		metadataMap.put("metadataSeq", lcmsMetadataDAO.selectLcmsMetadataSeq(metadataMap));
//			metadataMap.put("metaType", scorm.setMetaData((String)metadataMap.get("metaLocation")));
			metadataMap.put("metaType", scorm.setMetaData((String)metadataMap.get("metaLocation")));
			Object result = lcmsMetadataDAO.insertLcmsMetadata(metadataMap);
			HashMap<String, Object> m = null;
			for( int metaIdx=0; metaIdx<scorm.rs_ele_val.size(); metaIdx++ ){
				m = new HashMap<String, Object>();
				m.put("elementSeq", 	lcmsMetadataDAO.selectElementSeq(metadataMap));
				m.put("metadataSeq", 	metadataMap.get("metadataSeq"));
				m.put("elementName", 	String.valueOf(scorm.rs_ele_name.elementAt(metaIdx)));
				m.put("elementPath", 	String.valueOf(scorm.rs_ele_path.elementAt(metaIdx)));
				m.put("elementVal", 	String.valueOf(scorm.rs_ele_val.elementAt(metaIdx)));
				m.put("langValue", 		String.valueOf(scorm.rs_ele_lang_val.elementAt(metaIdx)));
				m.put("preGroup", 		String.valueOf(scorm.rs_parent_group.elementAt(metaIdx)));
				m.put("selfGroup", 		String.valueOf(scorm.rs_self_group.elementAt(metaIdx)));
				
				lcmsMetadataDAO.insertMetadataElement(m);
			}
    	}catch(Exception ex){
    		isOk = 0;
    	}
    	return isOk;
    }
    
    public int updateLcmsOrganization( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationDAO.updateLcmsOrganization( commandMap);
    }

    public int updateFieldLcmsOrganization( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationDAO.updateLcmsOrganization( commandMap);
    }

    public int deleteLcmsOrganization( Map<String, Object> commandMap) throws Exception {
    	int result = 1;
    	try{
    		
    		lcmsCourseMapDAO.deleteLcmsCourseMap(commandMap);
    		lcmsCourseOrgDAO.deleteLcmsCourseOrg(commandMap);
    		lcmsSeqMapInfoDAO.deleteLcmsSeqMapInfo(commandMap);
    		lcmsSeqObjectivesDAO.deleteLcmsSeqObjectives(commandMap);
    		lcmsSeqRollupConditionDAO.deleteLcmsSeqRollupCondition(commandMap);
    		lcmsSeqRollupRuleDAO.deleteLcmsSeqRollupRule(commandMap);
    		lcmsSeqRuleConditionDAO.deleteLcmsSeqRuleCondition(commandMap);
    		lcmsSeqConditionTypeDAO.deleteLcmsSeqConditionType(commandMap);
    		lcmsScormSequenceDAO.deleteLcmsScormSequence(commandMap);
    		lcmsResourceDependencyDAO.deleteLcmsResourceDependency(commandMap);
    		lcmsFileDAO.deleteLcmsFile(commandMap);
    		lcmsItemResourceDAO.deleteLcmsItemResource(commandMap);
    		lcmsItemDAO.deleteLcmsItem(commandMap);
    		lcmsSerializerDAO.deleteLcmsSerializer(commandMap);
    		lcmsMetadataDAO.deleteLcmsMetadataElement(commandMap);
    		lcmsMetadataDAO.deleteLcmsMetadata(commandMap);
    		lcmsOrganizationDAO.deleteLcmsOrganization(commandMap);
    		
    	}catch(Exception ex){
    		result = 0;
    		ex.printStackTrace();
    	}
        return result;
    }

    public int deleteLcmsOrganizationAll( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationDAO.deleteLcmsOrganizationAll( commandMap);
    }

    
    public Object existLcmsOrganization( LcmsOrganization lcmsOrganization) throws Exception {
        return lcmsOrganizationDAO.existLcmsOrganization( lcmsOrganization);
    }
    
    public List selectOrganizationPathList( Map<String, Object> commandMap) throws Exception{
    	return lcmsOrganizationDAO.selectOrganizationPathList(commandMap);
    }
    
    public int checkCourseMapping(Map<String, Object> commandMap) throws Exception{
    	return lcmsOrganizationDAO.checkCourseMapping(commandMap);
    }
}
