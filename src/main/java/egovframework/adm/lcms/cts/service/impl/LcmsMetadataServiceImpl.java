/* 
 * LcmsCourseOrgServiceImpl.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.service.impl;

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

import egovframework.adm.lcms.cts.service.LcmsMetadataService;
import egovframework.adm.lcms.cts.dao.LcmsMetadataDAO;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsMetadataServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsMetadataService")
public class LcmsMetadataServiceImpl extends EgovAbstractServiceImpl implements LcmsMetadataService {
    @Resource(name="lcmsMetadataDAO")
    private LcmsMetadataDAO lcmsMetadataDAO;


    public int insertMetadata(Map<String, Object> metadataMap) throws Exception {
        int isOk = 1;
        try{
        	lcmsMetadataDAO.selectLcmsMetadataSeq(metadataMap);
        }catch(Exception ex){
        	isOk = 0;
        	ex.printStackTrace();
        }
        return isOk;
    }
    
    public List selectLcmsMetadataElementList(Map<String, Object> commandMap) throws Exception{
    	return lcmsMetadataDAO.selectLcmsMetadataElementList(commandMap);
    }
    
    public int updateLcmsMetadataElement(Map<String, Object> commandMap) throws Exception{
    	int isOk = 1;
    	String[] val = (String[])commandMap.get("elementVal");
    	String[] seq = (String[])commandMap.get("metadataSeq");
    	String[] name = (String[])commandMap.get("elementName");
    	String[] title = (String[])commandMap.get("elementTitle");
    	String[] path = (String[])commandMap.get("elementPath");
    	String[] ele = (String[])commandMap.get("elementSeq");
    	
    	int metadataSeq = lcmsMetadataDAO.selectLcmsMetadataCount(commandMap);
    	commandMap.put("metadataSeq", metadataSeq);
    	if( metadataSeq == 0 ){
    		metadataSeq = lcmsMetadataDAO.selectLcmsMetadataSeq(commandMap);
    		commandMap.put("metadataSeq", metadataSeq);
    		commandMap.put("metatype", "LOM");
    		lcmsMetadataDAO.insertLcmsMetadata(commandMap);
    	}
    	int elementCount = lcmsMetadataDAO.selectLcmsMetadataElementCount(commandMap);
    	Map inputMap;
    	if( elementCount == 0 ){
    		for( int i=0; i<val.length; i++ ){
    			inputMap = new HashMap();
    			inputMap.put("elementSeq", lcmsMetadataDAO.selectElementSeq(commandMap));
    			inputMap.put("metadataSeq", metadataSeq);
				inputMap.put("elementName", name[i]);
				inputMap.put("elementVal", val[i]);
				inputMap.put("elementTitle", title[i]);
				inputMap.put("elementPath", path[i]);
				inputMap.put("preGroup", 1);
				inputMap.put("selfGroup", 1);
    			lcmsMetadataDAO.insertMetadataElement(inputMap);
    		}
    	}else{
    		for( int i=0; i<val.length; i++ ){
    			inputMap = new HashMap();
    			inputMap.put("elementSeq", ele[i]);
    			inputMap.put("metadataSeq", seq[i]);
    			inputMap.put("elementVal", val[i]);
    			isOk = isOk * lcmsMetadataDAO.updateLcmsMetadataElement(inputMap);
    		}
    	}
    	
    	return isOk;
    }
}
