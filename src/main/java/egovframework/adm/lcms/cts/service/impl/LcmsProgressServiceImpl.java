/* 
 * LcmsProgressServiceImpl.java		1.00	2011-10-17 
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
import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.cts.service.LcmsProgressService;
import egovframework.adm.lcms.cts.dao.LcmsProgressDAO;
import egovframework.adm.lcms.cts.domain.LcmsProgress;
import egovframework.adm.stu.dao.StuMemberDAO;
import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.com.lcms.len.dao.LearningDAO;
import egovframework.com.lcms.len.domain.LcmsLearning;

import org.apache.log4j.Logger;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsProgressServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-17 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsProgressService")
public class LcmsProgressServiceImpl extends EgovAbstractServiceImpl implements LcmsProgressService {
    @Resource(name="lcmsProgressDAO")
    private LcmsProgressDAO lcmsProgressDAO;
    
    @Resource(name="stuMemberDAO")
    private StuMemberDAO stuMemberDAO;
    
    
    @Resource(name="learningDAO")
    private LearningDAO learningDAO;
    
    /** AJAX 공통 서비스 */
    @Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    public List selectLcmsProgressPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsProgressDAO.selectLcmsProgressPageList( commandMap);
    }

    public int selectLcmsProgressPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsProgressDAO.selectLcmsProgressPageListTotCnt( commandMap);
    }

    public List selectLcmsProgressList( Map<String, Object> commandMap) throws Exception {
    	if( commandMap.get("studyType").toString().equals("NEW")){
    		return lcmsProgressDAO.selectLcmsProgressList( commandMap);
    	}else{
    		return lcmsProgressDAO.selectLcmsOldProgressList( commandMap);
    	}
    }

    public Object selectLcmsProgress( Map<String, Object> commandMap) throws Exception {
        return lcmsProgressDAO.selectLcmsProgress( commandMap);
    }

    public Object insertLcmsProgress( Map<String, Object> commandMap) throws Exception {
        return lcmsProgressDAO.insertLcmsProgress( commandMap);
    }

    public int updateLcmsProgress( Map<String, Object> commandMap) throws Exception {
    	
    	int result = lcmsProgressDAO.updateLcmsProgress(commandMap);
    	
    	 result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateXiniceProgress");
    	 //result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateEduUserProgress");
    	 if( commandMap.get("subjseq") != null && !commandMap.get("subjseq").toString().equals("000") ){
    		 HashMap<String, Object> mm = new HashMap<String, Object>();
    		 mm.put("p_subj", commandMap.get("subj"));
    		 mm.put("p_year", commandMap.get("year"));
    		 mm.put("p_subjseq", commandMap.get("subjseq"));
    		 mm.put("p_userid", commandMap.get("userid"));
    		 //등록여부 체크
    		 int cnt = stuMemberDAO.selectAttendCount(mm);
    		 if( cnt == 0 ) stuMemberDAO.insertUserAttendance(mm);
    		 
    		//학습자 참여도 점수 넣어주기
    		 stuMemberDAO.updateUserAttendanceStudentScore(mm);
    	 }

        return result;
    }

    public int updateFieldLcmsProgress( Map<String, Object> commandMap) throws Exception {
        return lcmsProgressDAO.updateLcmsProgress( commandMap);
    }

    public int deleteLcmsProgress( Map<String, Object> commandMap) throws Exception {
        return lcmsProgressDAO.deleteLcmsProgress( commandMap);
    }

    public int deleteLcmsProgressAll( Map<String, Object> commandMap) throws Exception {
        return lcmsProgressDAO.deleteLcmsProgressAll( commandMap);
    }

    
    public Object existLcmsProgress( LcmsProgress lcmsProgress) throws Exception {
        return lcmsProgressDAO.existLcmsProgress( lcmsProgress);
    }
    
    
    public int updateCotiLcmsProgress( Map<String, Object> commandMap) throws Exception {
    	
    	LcmsProgress progress = new LcmsProgress(); 
    	
    	Map progressInfo = (Map)lcmsProgressDAO.selectLcmsProgress(commandMap); 
    	
    	if(progressInfo != null){
			 LcmsScormModel.bind(progressInfo,progress); 
    	}
    	
    	if(progress.getLessonstatus()== null){
    		progress.setLessonstatus("N");
    	}
    	
    	int result = 0;
    	if( !progress.getLessonstatus().equals("Y")){
	    	if(progressInfo != null){
	    		 result = lcmsProgressDAO.updateLcmsProgress(commandMap);
	    	}else{  		
	    		commandMap.put("lessonCount", "1");  
	    		Object result1 = lcmsProgressDAO.insertLcmsProgress(commandMap);
	    	}
    	} 
    	
    	if(commandMap.get("gubun").toString().equals("true")){
    		result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateCotiProgress");
    		//result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateEduUserProgress");
    		
    		if( commandMap.get("subjseq") != null && !commandMap.get("subjseq").toString().equals("000") ){
    			HashMap<String, Object> mm = new HashMap<String, Object>();
    			mm.put("p_subj", commandMap.get("subj"));
    			mm.put("p_year", commandMap.get("year"));
    			mm.put("p_subjseq", commandMap.get("subjseq"));
    			mm.put("p_userid", commandMap.get("userid"));
    			//등록여부 체크
    			int cnt = stuMemberDAO.selectAttendCount(mm);
    			if( cnt == 0 ) stuMemberDAO.insertUserAttendance(mm);
    			
    			//학습자 참여도 점수 넣어주기
	    		 stuMemberDAO.updateUserAttendanceStudentScore(mm);
    		}
    	}
    	
        return result;
    }
    
    public int updateNormalLcmsProgress( Map<String, Object> commandMap) throws Exception {
    	
    	LcmsProgress progress = new LcmsProgress(); 
    	
    	
    	
    	
    	int result = 0;
    	
    	
		 HashMap<String, Object> hm = new HashMap<String, Object>();
		 hm.put("p_subj", commandMap.get("subj"));
		 hm.put("p_year", commandMap.get("year"));
		 hm.put("p_subjseq", commandMap.get("subjseq"));

    	//과정의 대한 정보 - 복습으로 인한 진도율을 막기 위하여 추가함
    	Map subjseqMap = learningDAO.selectSubjseq(hm);
    	
    	logger.info(subjseqMap);
    	
    	
    	
    	if(subjseqMap != null && subjseqMap.get("edustatus").equals("Y"))		//학습기간이라면 진도를 저장한다.
    	{
    	
	    	if( commandMap.get("studyType").toString().equals("NEW")){
	    		Map progressInfo = (Map)lcmsProgressDAO.selectLcmsProgress(commandMap); 
	    		
	    		if(progressInfo != null){
	    			LcmsScormModel.bind(progressInfo,progress); 
	    		}
	    		
	    		if(progress.getLessonstatus()== null){
	    			progress.setLessonstatus("N");
	    		}
	    		
	    		if( !progress.getLessonstatus().equals("Y")){
	    			if(progressInfo != null){
	    				result = lcmsProgressDAO.updateLcmsProgress(commandMap);
	    			}else{  		
	    				commandMap.put("lessonCount", "1");  
	    				Object result1 = lcmsProgressDAO.insertLcmsProgress(commandMap);
	    			}
	    		} 
	    		
	    		if(commandMap.get("gubun").toString().equals("true")){
	    			result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateNormalProgress");
	//    			result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateEduUserProgress");
	    			if( commandMap.get("subjseq") != null && !commandMap.get("subjseq").toString().equals("000") ){
	    	    		 HashMap<String, Object> mm = new HashMap<String, Object>();
	    	    		 mm.put("p_subj", commandMap.get("subj"));
	    	    		 mm.put("p_year", commandMap.get("year"));
	    	    		 mm.put("p_subjseq", commandMap.get("subjseq"));
	    	    		 mm.put("p_userid", commandMap.get("userid"));
	    	    		 //등록여부 체크
	    	    		 int cnt = stuMemberDAO.selectAttendCount(mm);
	    	    		 if( cnt == 0 ) stuMemberDAO.insertUserAttendance(mm);
	    	    		 //학습자 참여도 점수 넣어주기
	    	    		 stuMemberDAO.updateUserAttendanceStudentScore(mm);
	    	    	 }
	    		}   	
	    		
	    	}else{
	    		
	    		commandMap.put("contentLessonAllView", subjseqMap.get("contentlessonallview"));	//contentLessonAllView 전체 lesson 보기YN
	    		//테스트 Y
	    		//commandMap.put("contentLessonAllView", "Y");	//contentLessonAllView 전체 lesson 보기YN
	    		//System.out.println("contentLessonAllView ---> "+commandMap.get("contentLessonAllView"));
	    		
	    		Map progressInfo = (Map)lcmsProgressDAO.selectLcmsProgressOld(commandMap);
	    		if(progressInfo != null){
	    			//LcmsScormModel.bind(progressInfo,progress);
	    			logger.info("##### progress.getLessonstatus() : " + progress.getLessonstatus());
	    			logger.info("##### progressInfo.lessonstatus : " + progressInfo.get("lessonstatus"));
	    			logger.info("##### module : " + commandMap.get("p_module")+"");
	    			
	    			//학습시간 update
	    			result = lcmsProgressDAO.updateLcmsProgressOld(commandMap);
	    			
	    			String endCommit = lcmsProgressDAO.selectEndCommitYn(commandMap);
	    			
	    			logger.info("##### module : " + endCommit);
	    			
	    			if( progressInfo.get("lessonstatus") != null && !progress.getLessonstatus().equals("Y")){
	    				if(endCommit.equals("Y")){
	    					logger.info("##### 전체 insert ###############");
	    					result = 1;
	    					lcmsProgressDAO.updateLcmsProgressComplete(commandMap);
	    					
	    					lcmsProgressDAO.insertLcmsProgressComplete(commandMap);
	    					
	    					lcmsProgressDAO.updateLcmsProgressFinalStatus(commandMap);
	    				}else{
	    					//학습시간 update 위쪽으로 이동
	    					//result = lcmsProgressDAO.updateLcmsProgressOld(commandMap);
	    				}	
	    				
	    			} 
	    		}else{
	    			progress.setLessonstatus("N");
	    			commandMap.put("lessonCount", "1");  
	    			Object result1 = lcmsProgressDAO.insertLcmsOldProgress(commandMap);
	    		}
	    		
	    		
	    		if(commandMap.get("gubun").toString().equals("true")){
	    			//상위자격 취득을 위한 사전연수 36차시를 30차시처럼
	    			String subjOneThree =  commandMap.get("subj").toString();
	    			System.out.println("updateNormalLcmsProgress subjOneThree ----> "+subjOneThree);
	    			String subjOneThreeYn ="N";
	    			if("PRF150017".equals(subjOneThree) || "PRF170004".equals(subjOneThree)) {
	    				subjOneThreeYn ="Y";
	    				commandMap.put("subjOneThreeYn", subjOneThreeYn);
	    			}	    	
	    			System.out.println("updateNormalLcmsProgress subjOneThreeYn ----> "+subjOneThreeYn);
	    			
	    			result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateNormalOldProgress");
	//    			result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateEduUserOldProgress");
	    			
	    			if( commandMap.get("subjseq") != null && !commandMap.get("subjseq").toString().equals("000") ){
	    	    		 HashMap<String, Object> mm = new HashMap<String, Object>();
	    	    		 mm.put("p_subj", commandMap.get("subj"));
	    	    		 mm.put("p_year", commandMap.get("year"));
	    	    		 mm.put("p_subjseq", commandMap.get("subjseq"));
	    	    		 mm.put("p_userid", commandMap.get("userid"));
	    	    		 //등록여부 체크
	    	    		 int cnt = stuMemberDAO.selectAttendCount(mm);
	    	    		 if( cnt == 0 ) stuMemberDAO.insertUserAttendance(mm);
	    	    		 
	    	    		//학습자 참여도 점수 넣어주기
	    	    		 stuMemberDAO.updateUserAttendanceStudentScore(mm);
	    	    	 }
	    		}   	
	    	}
    	}
    	
    	return result;
    }
    
    public int updateScormLcmsProgress( Map<String, Object> commandMap) throws Exception {
            int result = 0;
    		result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateScormProgress");
    		//result += commonAjaxManageService.insertCommonAjaxManageInt(commandMap, "lcmsProgressDAO.updateEduUserProgress");
    		
    		if( commandMap.get("subjseq") != null && !commandMap.get("subjseq").toString().equals("000") ){
    			HashMap<String, Object> mm = new HashMap<String, Object>();
    			mm.put("p_subj", commandMap.get("subj"));
    			mm.put("p_year", commandMap.get("year"));
    			mm.put("p_subjseq", commandMap.get("subjseq"));
    			mm.put("p_userid", commandMap.get("userid"));
    			//등록여부 체크
    			int cnt = stuMemberDAO.selectAttendCount(mm);
    			if( cnt == 0 ) stuMemberDAO.insertUserAttendance(mm);
    			//학습자 참여도 점수 넣어주기
	    		 stuMemberDAO.updateUserAttendanceStudentScore(mm);
    		}
    	
    	return result;
    }

	public Map selectSubjseq(HashMap<String, Object> hm) throws Exception {
		return learningDAO.selectSubjseq(hm);
	}

    
}
