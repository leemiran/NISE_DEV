/**
 * @Class Name  : EgovFileUploadController.java
 * @Description : 파일 업로드 컨트롤러
 * @Modification Information
 * 
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.02.13       이삼섭                  최초 생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see 
 * 
 *  Copyright (C) 2008 by MOPAS  All right reserved.
 */
package egovframework.com.utl.fcc.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.service.EgovFileMngUtil;

@Controller
public class EgovFileUploadController {
	
    protected final Log logger = LogFactory.getLog(getClass());

    @SuppressWarnings("unchecked")
    @RequestMapping("/EgovFileUp.do")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

	MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
	Iterator fileIter = mptRequest.getFileNames();

	ArrayList result = new ArrayList();
	while (fileIter.hasNext()) {
	    MultipartFile mFile = mptRequest.getFile((String)fileIter.next());

	    /*
	     * System.out.println("mFile.getOriginalFilename() : "+mFile.getOriginalFilename()); 
	     * System.out.println("mFile.getName() : "+mFile.getName());
	     * System.out.println("mFile.getContentType() : "+mFile.getContentType());
	     * System.out.println("mFile.getSize() : "+mFile.getSize());
	     */

	    if (mFile.getSize() > 0) {
		HashMap map = EgovFileMngUtil.uploadFile(mFile);

		/*
		 * System.out.println("[ "+Globals.FILE_PATH+" : "+_map.get(Globals.FILE_PATH)+" ]");
		 * System.out.println("[ "+Globals.FILE_SIZE+" : "+_map.get(Globals.FILE_SIZE)+" ]");
		 * System.out.println("[ "+Globals.ORIGIN_FILE_NM+" : "+_map.get(Globals.ORIGIN_FILE_NM)+" ]");
		 * System.out.println("[ "+Globals.UPLOAD_FILE_NM+" : "+_map.get(Globals.UPLOAD_FILE_NM)+" ]");
		 * System.out.println("[ "+Globals.FILE_EXT+" : "+_map.get(Globals.FILE_EXT)+" ]");
		 */
		result.add(map);
	    }
	}
	String jspStr = "/cmm/utl/EgovFileUpload";
	
	return new ModelAndView(jspStr, "result", result);
    }
}
