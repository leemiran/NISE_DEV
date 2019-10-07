package egovframework.adm.book.controller;

import java.io.File;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.book.service.CpBookAdminService;
import egovframework.adm.cfg.amm.service.AdminMenuManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;

@Controller
public class CpBookAdminController {

	/** log */
	protected static final Log log = LogFactory.getLog(CpBookAdminController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "cpBookAdminService")
	CpBookAdminService cpBookAdminService;
	
	
	/**
	 * 연수생관리 > 교재/배송조회 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/book/selectCpBookList.do")
	public String selectCpBookList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = cpBookAdminService.selectList(commandMap);
		List cpList = cpBookAdminService.selectCPList(commandMap);
		List subjList = cpBookAdminService.selectSubj(commandMap);
		
		model.addAttribute("list", list);
		model.addAttribute("cpList", cpList);
		model.addAttribute("subjList", subjList);
		
		model.addAllAttributes(commandMap);
		return "adm/book/selectCpBookList";
	}
	
	/**
	 * 엑셀 다운
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/book/selectCpBookExcelDown.do")
	public ModelAndView excelDownload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		

		List<?> list = cpBookAdminService.selectList(commandMap);
		model.addAttribute("list", list);	
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("selectCpBookExcelView", "cpBookMap", map);
	}
	
	/**
	 * 배송상태 변경
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/book/updateCpBookStatus.do")
	public String updateCpBookStatus(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		boolean isOk = cpBookAdminService.updateCpbookStatus(commandMap);
		
		if(isOk){
			resultMsg = egovMessageSource.getMessage("success.common.save");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.save");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/book/selectCpBookList.do";
	}
	
	/**
	 * 정보 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/book/deleteCpBook.do")
	public String deleteCpBook(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		boolean isOk = cpBookAdminService.deleteCpBook(commandMap);
		
		if(isOk){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/book/selectCpBookList.do";
	}
	
	/**
	 * 엑셀 업로드 팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/book/cpBookExcelUploadPop.do")
	public String cpBookExcelUploadPop(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map infoMap = cpBookAdminService.selectSubjInfo(commandMap);
		
		model.addAttribute("infoMap", infoMap);
		model.addAllAttributes(commandMap);
		return "adm/book/cpBookExcelUploadPop";
	}
	
	/**
	 * 택배사 코드 엑셀 다운
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/book/excelDownForComp.do")
	public String selectDeliveryCompExcelList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = cpBookAdminService.selectDeliveryCompExcelList(commandMap);
		
		model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
		return "adm/book/selectDeliveryCompExcelList";
	}
	
	/**
	 * 엑셀 업로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/book/insertExcelFileToDB.do")
	public String insertExcelFileToDB(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//업로드 처리를 한다.
		List<Object> fileList = this.uploadFiles(request, commandMap);
		
		String result = cpBookAdminService.excelDownBookDelivery(commandMap, fileList);
		
		model.addAttribute("result", result);
		model.addAllAttributes(commandMap);
		return "adm/book/cpBookExcelUploadPop_P";
	}
	
	/**
	 * 업로드된 파일을 등록한다.
	 * 
	 * @param	contentPath	컨텐츠 경로
	 * @param	contentCode	컨텐츠 코드
	 * @return	Directory 생성 여부
	 * @throws Exception
	 */
	public List<Object> uploadFiles(HttpServletRequest request, Map<String, Object> commandMap) throws Exception {
		
		//기본 업로드 폴더
		String defaultDP = EgovProperties.getProperty("Globals.defaultDP");
		
		log.info("- 기본 업로드 폴더 : " + defaultDP);
		
		List<Object> list = new ArrayList<Object>();
		
		//저장경로 : dp\\bulletin
		
		//파일업로드를 실행한다.
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		
		java.util.Iterator<?> fileIter = mptRequest.getFileNames();
		 
		while (fileIter.hasNext()) {
			 MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			 
			 if (mFile.getSize() > 0) {
				 Object fileHm = new HashMap();
				 fileHm = EgovFileMngUtil.uploadContentFile(mFile, defaultDP + File.separator + "bulletin");
				 list.add(fileHm);
			 }
		}
		
		return list;
	}
	
}
