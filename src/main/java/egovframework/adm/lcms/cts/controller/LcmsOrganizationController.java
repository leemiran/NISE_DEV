/* 
 * LcmsOrganizationController.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController; 
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.com.utl.fcc.service.EgovStringUtil;

import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.cts.service.LcmsOrganizationService;
import egovframework.adm.lcms.cts.domain.LcmsOrganization;

/**
 * <pre>
 * system      :  
 * menu        : 
 * source      : LcmsOrganizationController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Controller
public class LcmsOrganizationController { 
/** log */ 
protected static final Log log = LogFactory.getLog( LcmsOrganizationController.class);

/** EgovPropertyService */
@Resource(name = "propertiesService")
protected EgovPropertyService propertiesService;

/** EgovMessageSource */
@Resource(name="egovMessageSource")
EgovMessageSource egovMessageSource;

/** PagingManageController */ 
@Resource(name = "pagingManageController")
private PagingManageController pagingManageController;

/** LcmsOrganizationService */
@Resource(name = "lcmsOrganizationService")
 private LcmsOrganizationService lcmsOrganizationService;

    @RequestMapping(value="/adm/lcms/cts/LcmsOrganizationPageList.do")
    public String pageList( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        int totCnt = lcmsOrganizationService.selectLcmsOrganizationPageListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

        List list = lcmsOrganizationService.selectLcmsOrganizationPageList(commandMap);
        model.addAttribute("list", list);	
        model.addAllAttributes(commandMap);	
        return "/adm/lcms/cts/LcmsOrganizationPageList";
    }


    @RequestMapping(value="/adm/clcms/ts/LcmsOrganizationList.do")
    public String list( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        List list = lcmsOrganizationService.selectLcmsOrganizationList(commandMap);
        model.addAttribute("list", list);	

        model.addAllAttributes(commandMap);	
        return "/adm/lcms/cts/LcmsOrganizationList";
    }

    @RequestMapping(value="/adm/lcms/cts/LcmsOrganizationView.do")
    public String view( 
    HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap(); 
        output.putAll((Map)lcmsOrganizationService.selectLcmsOrganization(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/lcms/cts/LcmsOrganizationView";
    }

    @RequestMapping(value="/adm/lcms/cts/LcmsOrganizationInsertForm.do")
    public String insertForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        model.addAllAttributes(commandMap);	
        return "/adm/lcms/cts/LcmsOrganizationInsert";
    }

    @RequestMapping(value="/adm/lcms/cts/LcmsOrganizationInsert.do")
    public String insert( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        int result = 0;
		String resultMsg = "";
		ArrayList imsPath = new ArrayList();
		ArrayList zFile = new ArrayList();
		
		FileController file = new FileController();
		String strSavePath = EgovProperties.getProperty("Globals.contentFileStore")+commandMap.get("userid")+"/"+commandMap.get("subj");
		
		// temp폴더 삭제 ( imsmanifest.xml을 불러오기위한 임시폴더 )
		boolean deleteResult = file.deleteDirector(strSavePath+"/tmp");
		commandMap.put("strSavePath", strSavePath);
		
		// 압축파일 해제
		String contents[] = (new File(strSavePath)).list();
		for(int j=0; j<contents.length; j++){
			String saveFile = contents[j];
			if( !(new File(strSavePath+"/"+saveFile)).isDirectory() ){
				imsPath = file.fileUnZip(strSavePath, saveFile);
			}
		}
		
		
		Collections.sort(imsPath);
		
		LcmsScormModel scorm = new LcmsScormModel();
		ArrayList dataList = scorm.getData(request, commandMap, imsPath);
		
		result = lcmsOrganizationService.insertLcmsOrganization(dataList);
		
		
		
		if( result == 1 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			 if( result == 10 ){
				 resultMsg = "Title 이 테이블 칼럼의 Max 값을 초과합니다.";
			 }else if( result == 20 ){
				 resultMsg = "Organization Id 값이 중복되었습니다. manifest file 의 항목을 확인하십시오.";
			 }else if( result == 30 ){
				 resultMsg = "Parameter Attribute 의 길이가 테이블 칼럼의 Max 값을 초과합니다.";
			 }else if( result == 40 ){
				 resultMsg = "Item Title 이 테이블 칼럼의 Max 값을 초과합니다.";
			 }else if( result == 50 ){
				 resultMsg = "dataFromLMS의 값이 테이블 칼럼의 Max 값을 초과합니다.";
			 }else if( result == 60 ){
				 resultMsg = "Item Id 값이 중복되었습니다. manifest file 의 항목을 확인하십시오.";
			 }else{
				 resultMsg = egovMessageSource.getMessage("fail.common.insert");
			 }
			 for( int i=0; i<imsPath.size(); i++ ){
				 file.deleteDirector((String)imsPath.get(i));
			 }
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/ims/imsManifestList";
    }
    
    
    @RequestMapping(value="/adm/lcms/cts/LcmsOrganizationCancel.do")
    public String cancel( 
    		HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> commandMap, ModelMap model) throws Exception { 
    	
    	String resultMsg = "";
    	
    	FileController file = new FileController();
    	String strSavePath = Globals.CONTNET_REAL_PATH+commandMap.get("SubDir");
    	
    	// temp폴더 삭제 ( imsmanifest.xml을 불러오기위한 임시폴더 )
		boolean deleteResult = file.deleteDirector(strSavePath+"/tmp");
		file.deleteZipFile(strSavePath, "zip");
    	
    	if( deleteResult ){
    		resultMsg = "취소 되었습니다.";
    	}else{
    		resultMsg = "취소처리중 오류로 인하여 파일이 정상적으로 삭제되지 않았습니다.";
    	}
    	
    	model.addAttribute("resultMsg", resultMsg);
    	model.addAllAttributes(commandMap);
    	
    	return "adm/lcms/ims/imsManifestList";
    }

    @RequestMapping(value="/adm/lcms/cts/LcmsOrganizationUdateForm.do")
    public String updateForm( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        Map output = new HashMap();         
        output.putAll((Map)lcmsOrganizationService.selectLcmsOrganization(commandMap));
        model.addAttribute("output",output); 
        model.addAllAttributes(commandMap);	
        return "/adm/lcms/cts/LcmsOrganizationUpdate";
    }

    @RequestMapping(value="/adm/lcms/cts/LcmsOrganizationUpdate.do")
    public String update( 
        HttpServletRequest request, HttpServletResponse response,
        Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg = ""; 
        String forwardUrl = ""; 

        int result = lcmsOrganizationService.updateLcmsOrganization( commandMap);

        if(result > 0){  
            resultMsg = egovMessageSource.getMessage("success.common.update"); 
        }else{ 
            resultMsg = egovMessageSource.getMessage("fail.common.update"); 
         } 
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/lcms/cts/LcmsOrganizationUdateForm.do"; 
        return forwardUrl; 
    }

    @RequestMapping(value="/adm/lcms/cts/LcmsOrganizationDelete.do")
    public String delete( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 

        String resultMsg 	= ""; 
        String forwardUrl 	= ""; 
        
        int check = lcmsOrganizationService.checkCourseMapping(commandMap);
        
        if( check == 0 ){
        	String[] orgSeq = EgovStringUtil.getStringSequence(commandMap,"chk");
        	commandMap.put("orgSeq", orgSeq);
        	
        	List filePath = lcmsOrganizationService.selectOrganizationPathList(commandMap);
        	
        	int result = lcmsOrganizationService.deleteLcmsOrganization( commandMap);
        	
//        	FileController file = new FileController();
//        	for( int i=0; i<filePath.size(); i++ ){
//        		//선택차시 컨텐츠파일 삭제
//        		boolean deleteResult = file.deleteDirector(Globals.CONTNET_REAL_PATH+((Map)filePath.get(i)).get("baseDir"));
//        	}
        	
//        	if(result > 0){  
        		resultMsg = egovMessageSource.getMessage("success.common.delete"); 
//        	}else{ 
//        		resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
//        	} 
        }else{
        	resultMsg = "이미 사용중인 교과는 수정할 수 없습니다.";
        }
 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
        forwardUrl = "forward:/adm/lcms/sco/scormScoList.do";
        return forwardUrl; 
    }

}
