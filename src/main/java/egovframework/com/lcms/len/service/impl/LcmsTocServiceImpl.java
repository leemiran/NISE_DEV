/* 
 * LcmsTocServiceImpl.java		1.00	2011-09-14 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.com.lcms.len.service.impl;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Blob;
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

import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.com.lcms.len.service.LcmsTocService;
import egovframework.com.lcms.len.dao.LcmsTocDAO;
import egovframework.com.lcms.len.domain.LcmsToc;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsTocServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsTocService")
public class LcmsTocServiceImpl extends EgovAbstractServiceImpl implements LcmsTocService {
    @Resource(name="lcmsTocDAO")
    private LcmsTocDAO lcmsTocDAO;

    public List selectLcmsTocPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsTocDAO.selectLcmsTocPageList( commandMap);
    }

    public int selectLcmsTocPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsTocDAO.selectLcmsTocPageListTotCnt( commandMap);
    }

    public List selectLcmsTocList( Map<String, Object> commandMap) throws Exception {
        return lcmsTocDAO.selectLcmsTocList( commandMap);
    }

    public Object selectLcmsToc( Map<String, Object> commandMap) throws Exception {
    	 LcmsToc output = new LcmsToc();
    	 output = (LcmsToc)lcmsTocDAO.selectLcmsToc( commandMap);	
    	
    	 if(output != null)
    		 output.setSerializer(LcmsScormModel.read(output.getSerializer()));

        return output;
    }

    public Object insertLcmsToc( Map<String, Object> commandMap) throws Exception {
    	commandMap.put("serializer",LcmsScormModel.write(commandMap.get("serializer")));
        return lcmsTocDAO.insertLcmsToc( commandMap);
    }

    public int updateLcmsToc( Map<String, Object> commandMap) throws Exception {
    	commandMap.put("serializer",LcmsScormModel.write(commandMap.get("serializer")));
        return lcmsTocDAO.updateLcmsToc( commandMap);
    }

    public int updateFieldLcmsToc( Map<String, Object> commandMap) throws Exception {
        return lcmsTocDAO.updateFieldLcmsToc( commandMap);
    }

    public int deleteLcmsToc( Map<String, Object> commandMap) throws Exception {
        return lcmsTocDAO.deleteLcmsToc( commandMap);
    }

    public int deleteLcmsTocAll( Map<String, Object> commandMap) throws Exception {
        return lcmsTocDAO.deleteLcmsTocAll( commandMap);
    }

    
    public Object existLcmsToc( LcmsToc lcmsToc) throws Exception {
        return lcmsTocDAO.existLcmsToc( lcmsToc);
    }

    
}
