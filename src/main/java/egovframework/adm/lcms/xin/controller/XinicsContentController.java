package egovframework.adm.lcms.xin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.nct.service.LcmsModuleService;
import egovframework.adm.lcms.xin.model.LcmsXinicsModel;
import egovframework.adm.lcms.xin.service.XinicsContentService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class XinicsContentController {

	/** log */
    protected static final Log log = LogFactory.getLog(XinicsContentController.class);

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
    /** xinicsContentService */
	@Resource(name = "xinicsContentService")
    private XinicsContentService xinicsContentService;
	
	/** xinicsContentService */
	@Resource(name = "lcmsModuleService")
	private LcmsModuleService lcmsModuleService;
	
	/**
	 * 자이닉스 교과리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/xin/xinicsContentList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		int totCnt = xinicsContentService.selectXinicsContentListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = xinicsContentService.selectXinicsContentList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/xin/xinicsContentList";
	}
	
	@RequestMapping(value="/adm/lcms/xin/xinicsContentInsert.do")
	public String insert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
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
		
		LcmsXinicsModel xinics = new LcmsXinicsModel();
		ArrayList dataList = xinics.getData(request, commandMap, imsPath);
		
		result = xinicsContentService.insertXinicsContent(dataList);
		for( int i=0; i<imsPath.size(); i++ ){
			FileInputStream inputStream = null;
			FileOutputStream outputStream = null;
			try{
				String path = (String)imsPath.get(i);
				inputStream = new FileInputStream(Globals.CONTNET_REAL_PATH+"cscript.js");
				outputStream = new FileOutputStream(Globals.CONTNET_REAL_PATH+path.substring((EgovProperties.getProperty("Globals.contentFileStore")+commandMap.get("userid")).length()+1)+"/cscripts/cscript.js");
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
			FileChannel fcin = inputStream.getChannel();
			FileChannel fcout = outputStream.getChannel(); 
			
			long size = 0;     
			try {
				size = fcin.size();
				fcin.transferTo(0, size, fcout);
				fcout.close();
				fcin.close();
				outputStream.close();
				inputStream.close();
			}catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		if( result == 1 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			 if( result == 10 ){
				 resultMsg = "Title 이 테이블 칼럼의 Max 값을 초과합니다.";
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
	
	
	@RequestMapping(value="/adm/lcms/xin/xinicsOrgList.do")
	public String orgList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List list = xinicsContentService.selectXinicsOrgList(commandMap);
		
		Map data = lcmsModuleService.selectSaveInfoData(commandMap);
		
		model.addAttribute("list", list);	
		model.addAttribute("data", data);	
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/xin/xinicsOrgList";
	}
}
