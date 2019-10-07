/**
 * @Class Name  : EgovFileDownController.java
 * @Description : 파일다운로드 테스트 컨트롤러
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;

@Controller
public class EgovFileDownController  {

    /**
     * 파일 다운로드 기능을 제공한다.
     */
    @RequestMapping(value = "/EgovFileDown.do")
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
	String stordFilePath = EgovProperties.getProperty("Globals.fileStorePath");

	String filename = request.getParameter("filename");
	String original = request.getParameter("original");

	if ("".equals(filename)) {
	    request.setAttribute("message", "File not found.");
	    return "cmm/utl/EgovFileDown";
	}

	if ("".equals(original)) {
	    original = filename;
	}

	request.setAttribute("downFile", stordFilePath + filename);
	request.setAttribute("orginFile", original);

	EgovFileMngUtil.downFile(request, response);
	//String jspStr = "/jsp/com/utl/EgovFileDown.jsp";

	return "cmm/utl/EgovFileDown";
    }
}
