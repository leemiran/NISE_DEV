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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ziaan.library.CalcUtil;


import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import egovframework.com.lcms.len.service.LearningService;
import egovframework.com.lcms.len.dao.LearningDAO;
import egovframework.com.lcms.len.domain.LcmsToc;
import egovframework.com.utl.fcc.service.EgovDateUtil;

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


@Service("learningService")
public class LearningServiceImpl extends EgovAbstractServiceImpl implements LearningService {
    @Resource(name="learningDAO")
    private LearningDAO learningDAO;


    public Object selectLearningCrsInfo( Map<String, Object> commandMap) throws Exception {
    	Object  output = (Object)learningDAO.selectLearningCrsInfo( commandMap);	
        return output;
    }
    
    public Map selectMasterform( Map<String, Object> commandMap) throws Exception {
    	return learningDAO.selectMasterform( commandMap);	
    }
    
    public int writeLog( Map<String, Object> commandMap) throws Exception {
    	int isOk = 1;
    	try{
    		int cnt = learningDAO.selectStudyCount(commandMap);
    		if( cnt > 0){
    			learningDAO.updateStudyCount(commandMap);
    		}else{
    			learningDAO.insertStudyCount(commandMap);
    		}
    	}catch(Exception ex){
    		isOk = 0;
    		ex.printStackTrace();
    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    	}
    	return isOk;
    }
    
    public List selectEduScore( Map<String, Object> commandMap) throws Exception {
    	return learningDAO.selectEduScore(commandMap);
    }
    
    public List selectEduScore2( Map<String, Object> commandMap) throws Exception {
    	return learningDAO.selectEduScore2(commandMap);
    }
    
    public int selectUserPage( Map<String, Object> commandMap) throws Exception {
    	return learningDAO.selectUserPage(commandMap);
    }

    public List SelectEduList( Map<String, Object> commandMap) throws Exception {
    	return learningDAO.SelectEduList(commandMap);
    }

    public List selectEduTimeCountOBC( Map<String, Object> commandMap) throws Exception {
    	return learningDAO.selectEduTimeCountOBC(commandMap);
    }

    public int isReportAssign( Map<String, Object> commandMap) throws Exception {
    	return learningDAO.isReportAssign(commandMap);
    }
    
    public int updateReportAssign( Map<String, Object> commandMap) throws Exception {
    	int isOk = 1;
    	try{
    		String v_projseq = "";
    		String v_userid  = (String)commandMap.get("userid");
    		if( commandMap.get("gadmin").toString().equals("ZZ") ){
    			if( commandMap.get("p_assign") == null || commandMap.get("p_assign").toString().equals("") ){
    				List list = learningDAO.selectProjseq(commandMap);
    				if( list != null ){
    					Map m = (Map)list.get(0);
    					v_projseq = (String)m.get("grpseq");
    				}
    			}else{
    				v_projseq= (String)commandMap.get("p_projseq");
	    			v_userid = (String)commandMap.get("p_userid");
    			}
    			commandMap.put("v_projseq", v_projseq);
    			commandMap.put("v_userid", v_userid);
    			learningDAO.insertProjassign(commandMap);
    		}
    	}catch(Exception ex){
    		
    	}
    	return isOk;
    }
    
    public String getPromotion( Map<String, Object> commandMap) throws Exception {
    	String result = "0";
    	float percent     = (float)0.0;
    	try{
    		Map subjseqMap = learningDAO.selectSubjseq(commandMap);
            String v_today    = (String)subjseqMap.get("today");
            String v_edustart = (String)subjseqMap.get("edustart");
            String v_eduend   = (String)subjseqMap.get("eduend");
            int v_nowday = EgovDateUtil.datediff("date",v_edustart,v_today);
            int v_allday = EgovDateUtil.datediff("date",v_edustart,v_eduend);
            if ( v_allday != 0 ) { 
                percent = (float)((v_nowday * 100) / (float)v_allday);
                if ( percent <= 0.0) percent=0;
                else if ( percent > 100.0) percent=100;
                result =  new DecimalFormat("0.00").format(percent);
            }
    	}catch(Exception ex){
    		result = "0";
    		ex.printStackTrace();
    	}
    	return result;
    }
    
    public String getProgress( Map<String, Object> commandMap) throws Exception {
    	String result = "0";
    	float percent     = (float)0.0;
    	try{
    		
    		commandMap.put("p_gubun", CalcUtil.ALL);
    		
    	}catch(Exception ex){
    		result = "0";
    		ex.printStackTrace();
    	}
    	return result;
    }
    
    
}
