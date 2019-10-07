/* 
 * LcmsSerializerServiceImpl.java		1.00	2011-09-14 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.service.impl;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;

import org.adl.sequencer.ADLSeqUtilities;
import org.adl.sequencer.SeqActivityTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.excel.EgovExcelService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.cts.service.LcmsSerializerService;
import egovframework.adm.lcms.cts.dao.LcmsSerializerDAO;
import egovframework.adm.lcms.cts.domain.LcmsSerializer;
import egovframework.com.lcms.len.domain.LcmsToc;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSerializerServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsSerializerService")
public class LcmsSerializerServiceImpl extends EgovAbstractServiceImpl implements LcmsSerializerService {
    @Resource(name="lcmsSerializerDAO")
    private LcmsSerializerDAO lcmsSerializerDAO;

    /**
    public List selectLcmsSerializerPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsSerializerDAO.selectLcmsSerializerPageList( commandMap);
    }

    public int selectLcmsSerializerPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsSerializerDAO.selectLcmsSerializerPageListTotCnt( commandMap);
    }

    public List selectLcmsSerializerList( Map<String, Object> commandMap) throws Exception {
        return lcmsSerializerDAO.selectLcmsSerializerList( commandMap);
    }
    **/
    /**
     * 학습창 Serializer select
     */
    public Object selectLcmsSerializer( Map<String, Object> commandMap) throws Exception {
    	
        LcmsSerializer output = new LcmsSerializer();
        SeqActivityTree activityTree = null;
        
	    output = (LcmsSerializer)lcmsSerializerDAO.selectLcmsSerializer(commandMap);	
	    
	   	 if(output != null)
			 output.setSerializer(LcmsScormModel.read(output.getSerializer()));
	   	/** 
	   	 * 
	   	 * LcmsScormModel.read(Object object) 사용 
	    if(output != null){
		     if(output.getSerializer() != null){
				Blob tbblob = (Blob)output.getSerializer();
				InputStream is = tbblob.getBinaryStream();
				ObjectInputStream iop = new ObjectInputStream(is);
				Object obj = iop.readObject();
				iop.close();
				is.close();
				output.setSerializer(obj);   
		     }
	    }
	    **/
        return output;
    }

    /**
    public Object insertLcmsSerializer( Map<String, Object> commandMap) throws Exception {
        return lcmsSerializerDAO.insertLcmsSerializer( commandMap);
    }

    public int updateLcmsSerializer( Map<String, Object> commandMap) throws Exception {
        return lcmsSerializerDAO.updateLcmsSerializer( commandMap);
    }

    public int updateFieldLcmsSerializer( Map<String, Object> commandMap) throws Exception {
        return lcmsSerializerDAO.updateLcmsSerializer( commandMap);
    }

    public int deleteLcmsSerializer( Map<String, Object> commandMap) throws Exception {
        return lcmsSerializerDAO.deleteLcmsSerializer( commandMap);
    }

    public int deleteLcmsSerializerAll( Map<String, Object> commandMap) throws Exception {
        return lcmsSerializerDAO.deleteLcmsSerializerAll( commandMap);
    }

    
    public Object existLcmsSerializer( LcmsSerializer lcmsSerializer) throws Exception {
        return lcmsSerializerDAO.existLcmsSerializer( lcmsSerializer);
    }
    **/
    
}
