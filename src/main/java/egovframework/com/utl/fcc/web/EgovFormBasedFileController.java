package egovframework.com.utl.fcc.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.utl.fcc.service.EgovFormBasedFileUtil;
import egovframework.com.utl.fcc.service.EgovFormBasedFileVo;

/**
 * @Class Name  : EgovFormBasedFileController.java
 * @Description : Form-based File Upload Servlet
 * @Modification Information
 * 
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.08.26       한성곤                  최초 생성
 *
 * @author 공통컴포넌트 개발팀 한성곤
 * @since 2009.08.26
 * @version 1.0
 * @see  
 */
@Controller
public class EgovFormBasedFileController {
    protected final Log logger = LogFactory.getLog(getClass());
    
    /** 첨부파일 위치 지정 */
    private final String uploadDir = EgovProperties.getProperty("Globals.fileStorePath");
    
    /** 첨부 최대 파일 크기 지정 */
    private final long maxFileSize = 1024 * 1024 * 100;   //업로드 최대 사이즈 설정 (100M)
    
    /**
     * File upload, download 테스트 화면 이동.
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/EgovFormBasedFileForm.do")
    public String goForm() throws Exception {
	return "cmm/utl/EgovFormBasedFile";
    }
    
    /**
     * File upload 처리.
     * 
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/EgovFormBasedFile.do",method=RequestMethod.POST)
    public String upload(HttpServletRequest request, Model model) throws Exception {
	String jspPath = "cmm/utl/EgovFormBasedFile";
	
	//request.setCharacterEncoding("UTF-8");

	//System.out.println("Upload Dir. : " + uploadDir);
	logger.debug("Upload Dir. : " + uploadDir);

	List<EgovFormBasedFileVo> list = EgovFormBasedFileUtil.uploadFiles(request, uploadDir, maxFileSize);

	model.addAttribute("list", list);
	
	return jspPath;
    }
    
    /**
     * File download 처리.
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/EgovFormBasedFile.do",method=RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
	String subPath = request.getParameter("path");
	String physical = request.getParameter("physical");
	String filename = request.getParameter("filename");
	
	//System.out.println("filename = " + filename);
	logger.debug("filename = " + filename);
	
	EgovFormBasedFileUtil.downloadFile(response, uploadDir, subPath, physical, filename);
    }
}
