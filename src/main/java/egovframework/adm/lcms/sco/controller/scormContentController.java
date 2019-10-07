package egovframework.adm.lcms.sco.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import org.adl.sequencer.SeqActivityTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tmax.tibero.jdbc.TbBlob;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.property.EgovPropertyService;

import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.cts.service.LcmsOrganizationService;
import egovframework.adm.lcms.ims.mainfest.RestructureHandler;
import egovframework.adm.lcms.sco.service.ScormContentService;

/** 
 * 
 * @author 
 * @since 
 * @version 1.0
 * @see
 *
 * <pre>
 *
 * </pre>
 */
@Controller
public class scormContentController {
	
	/** log */
    protected static final Log log = LogFactory.getLog(scormContentController.class);

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
    /** CodeManageService */
	@Resource(name = "scormContentService")
    private ScormContentService scormContentService;
	
	/** lcmsOrganizationService */
	@Resource(name = "lcmsOrganizationService")
	private LcmsOrganizationService lcmsOrganizationService;
	
	
	/**
	 * 컨텐츠 목록 페이지
	 * @param curmQustManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/org/scormContentList.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/sco/scormContentList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		HttpSession session = request.getSession();
		String p_gadmin = session.getAttribute("gadmin") !=null ? (String)session.getAttribute("gadmin") : "" ;		
		//System.out.println("p_gadmin -----> "+p_gadmin);
		
		if(p_gadmin.equals("Q1")){
			return "forward:/adm/lcms/old/oldContentList.do?s_menu=29000000&s_submenu=29010000";
		}
		
		int totCnt = scormContentService.selectScormContentListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = scormContentService.selectScormContentPageList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);		
	  	return "adm/lcms/sco/scormContentList";
	}
	
	/**
	 * 컨텐츠 목록 페이지
	 * @param curmQustManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/sco/scormScoList.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/sco/scormScoList.do") 
	public String scoList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		
		List list = scormContentService.selectScormNewScoPageList(commandMap);
		
		Map info = scormContentService.selectScormCreateInfo(commandMap);
		
		model.addAttribute("list", list);	
		model.addAttribute("info", info);	
		model.addAllAttributes(commandMap);		
		return "adm/lcms/sco/scormScoList"; 
	}
	
	@RequestMapping(value="/adm/lcms/sco/scormRestructureContent.do")
	public String restructure(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String manifestPath = Globals.MANIFEST_PATH+commandMap.get("userid");
		
		File exFile = new File(manifestPath, "imsmanifest.xml");
		if( exFile.exists() ){
			exFile.delete();
		}
		FileController file = new FileController();
		file.makeManifest(manifestPath);
		
		model.addAttribute("manifestPath", manifestPath);
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/sco/scormRestructureContent";
	}
	
	/**
	 * 재구성 객체명 수정팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/sco/editAttributePopup.do")
	public String editAttributePage(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "adm/lcms/sco/admEditAttributePopup";
	}
	
	/**
	 * 재구성 객체명 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/sco/editAttribute.do")
	public String editAttribute(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		
		RestructureHandler rh = new RestructureHandler();
		String resultMsg = "";
		boolean result = rh.editItemTitle(commandMap);
		
		if(result ){  
    		resultMsg = egovMessageSource.getMessage("success.common.update"); 
    	}else{ 
    		resultMsg = egovMessageSource.getMessage("fail.common.update"); 
    	} 
		
		model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
		
		return "adm/lcms/sco/admEditAttributePopup";
	}
	
	
	@RequestMapping(value="/adm/lcms/sco/makeContent.do")
	public String makeContent(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int result = 1;
		
		FileController file = new FileController();
		RestructureHandler rh = new RestructureHandler();
		String[] rsrcSeq = rh.getRsrcSeq(commandMap);
		commandMap.put("rsrcSeq", rsrcSeq);
		List dirList = scormContentService.selectFileBaseDirList(commandMap);
		
		ArrayList imsPath = rh.copyContent(dirList, commandMap);
		
		String strSavePath = Globals.CONTNET_REAL_PATH+commandMap.get("subj");
		
		commandMap.put("strSavePath", strSavePath);
		
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
		
		
		return "adm/lcms/sco/scormRestructureContent";
	}
	
	@RequestMapping(value="/adm/lcms/sco/progressLogPopup.do")
	public String progressLogPoupu(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		
		
		String[] check = EgovStringUtil.getStringSequence(commandMap, "chk");
		commandMap.put("check", check);
		
		
		List progressList = scormContentService.selectProgressLogList(commandMap);
		
		model.addAttribute("progressList", progressList);
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/sco/progressLogPopup";
	}
	
	@RequestMapping(value="/adm/lcms/sco/deleteLog.do")
	public String deleteProgressLog(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		String[] check = EgovStringUtil.getStringSequence(commandMap, "chk");
		commandMap.put("check", check);
		
		int isOk = scormContentService.deleteProgressLog(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/lcms/sco/progressLogPopup.do";
	}
	
	
}
