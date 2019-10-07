package egovframework.com.cmm.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name  : EgovFileManageUtil.java
 * @Description : 메시지 처리 관련 유틸리티
 * @Modification Information
 * 
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.02.13       이삼섭                  최초 생성
 *   
 *   
 *   2011.01.24       기존 소스 수정파일
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see 
 * 
 */
@Component("EgovFileManageUtil")
public class EgovFileManageUtil {

    public static final int BUFF_SIZE = 2048;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "egovFileIdGnrService")
    private EgovIdGnrService idgenService;

    Logger log = Logger.getLogger(this.getClass());

    /**
     * 첨부파일에 대한 목록 정보를 취득한다.
     * 
     * @param files
     * @return
     * @throws Exception
     * @deprecated
     */
    public List<FileManageVO> parseFileInf(Map<String, MultipartFile> files, String KeyStr, int fileKeyParam, String atchFileId, String storePath) throws Exception {
	int fileKey = fileKeyParam;
	
	String storePathString = "";
	String atchFileIdString = "";

	if ("".equals(storePath) || storePath == null) {
	    storePathString = propertyService.getString("Globals.fileStorePath");
	} else {
	    storePathString = propertyService.getString("Globals.fileStorePath") + storePath + "/";
	}


	if ("".equals(atchFileId) || atchFileId == null) {
	    atchFileIdString = idgenService.getNextStringId();
	} else {
	    atchFileIdString = atchFileId;
	}


	File saveFolder = new File(storePathString);
	
	if (!saveFolder.exists() || saveFolder.isFile()) {
	    saveFolder.mkdirs();
	}

	Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
	MultipartFile file;
	String filePath = "";
	List<FileManageVO> result  = new ArrayList<FileManageVO>();
	FileManageVO fvo;

	while (itr.hasNext()) {
	    Entry<String, MultipartFile> entry = itr.next();

	    file = entry.getValue();
	    String orginFileName = file.getOriginalFilename();
	    
	    //--------------------------------------
	    // 원 파일명이 없는 경우 처리
	    // (첨부가 되지 않은 input file type)
	    //--------------------------------------
	    if ("".equals(orginFileName)) {
		continue;
	    }
	    ////------------------------------------

	    int index = orginFileName.lastIndexOf(".");
	    //String fileName = orginFileName.substring(0, index);
	    String fileExt = orginFileName.substring(index + 1);
	    String newName = KeyStr + EgovStringUtil.getTimeStamp() + fileKey;
	    long _size = file.getSize();

	    if (!"".equals(orginFileName)) {
		filePath = storePathString + File.separator + newName;
		file.transferTo(new File(filePath));
	    }
	    fvo = new FileManageVO();
	    fvo.setFileExtsn(fileExt);
	    fvo.setFileStreCours(storePathString);
	    fvo.setFileMg(Long.toString(_size));
	    fvo.setOrignlFileNm(orginFileName);
	    fvo.setStreFileNm(newName);
	    fvo.setAtchFileId(atchFileIdString);
	    fvo.setFileSn(String.valueOf(fileKey));

	    //writeFile(file, newName, storePathString);
	    result.add(fvo);
	    
	    fileKey++;
	}

	return result;
    }
    
    /**
     * 첨부파일에 대한 목록 정보를 취득한다.
     * 
     * @param files
     * @return
     * @throws Exception
     */
    public List<FileManageVO> parseFileInf(Map<String, MultipartFile> files, String KeyStr, int fileKeyParam, String storePath) throws Exception {
    	int fileKey = fileKeyParam;
    	
    	String storePathString = "";
    	String atchFileIdString = "";
    	
    	if ("".equals(storePath) || storePath == null) {
    		storePathString = propertyService.getString("Globals.fileStorePath");
    	} else {
    		storePathString = propertyService.getString("Globals.fileStorePath") + storePath + "/";
    	}
    	
    	File saveFolder = new File(storePathString);
    	
    	if (!saveFolder.exists() || saveFolder.isFile()) {
    		saveFolder.mkdirs();
    	}
    	
    	Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
    	MultipartFile file;
    	String filePath = "";
    	List<FileManageVO> result  = new ArrayList<FileManageVO>();
    	FileManageVO fvo;
    	
    	while (itr.hasNext()) {
    		Entry<String, MultipartFile> entry = itr.next();
    		
    		file = entry.getValue();
    		String orginFileName = file.getOriginalFilename();
    		String fieldName = file.getName();
    		
    		
    		//--------------------------------------
    		// 원 파일명이 없는 경우 처리
    		// (첨부가 되지 않은 input file type)
    		//--------------------------------------
    		if ("".equals(orginFileName)) {
    			continue;
    		}
    		////------------------------------------
    		
    		int index = orginFileName.lastIndexOf(".");
    		String fileExt = orginFileName.substring(index + 1);
    		String newName = KeyStr + EgovStringUtil.getTimeStamp() + fileKey;
    		long _size = file.getSize();
    		
    		if (!"".equals(orginFileName)) {
    			filePath = storePathString + File.separator + newName + "." + fileExt;
    			file.transferTo(new File(filePath));
    		}
    		fvo = new FileManageVO();
    		fvo.setFieldNm(fieldName);
    		fvo.setFileExtsn(fileExt);
    		fvo.setFileStreCours(storePathString);
    		fvo.setFileMg(Long.toString(_size));
    		fvo.setOrignlFileNm(orginFileName);
    		fvo.setStreFileNm(newName);
    		fvo.setFileSn(String.valueOf(fileKey));
    		
    		result.add(fvo);
    		
    		fileKey++;
    	}
    	
    	return result;
    }

    /**
     * 첨부파일을 서버에 저장한다.
     * 
     * @param file
     * @param newName
     * @param stordFilePath
     * @throws Exception
     */
    protected void writeUploadedFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
	InputStream stream = null;
	OutputStream bos = null;
	
	try {
	    stream = file.getInputStream();
	    File cFile = new File(stordFilePath);

	    if (!cFile.isDirectory()) {
		boolean _flag = cFile.mkdir();
		if (!_flag) {
		    throw new IOException("Directory creation Failed ");
		}
	    }

	    bos = new FileOutputStream(stordFilePath + File.separator + newName);

	    int bytesRead = 0;
	    byte[] buffer = new byte[BUFF_SIZE];

	    while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
		bos.write(buffer, 0, bytesRead);
	    }
	} catch (FileNotFoundException fnfe) {
	    fnfe.printStackTrace();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (bos != null) {
		try {
		    bos.close();
		} catch (Exception ignore) {
		    log.debug("IGNORED: " + ignore.getMessage());
		}
	    }
	    if (stream != null) {
		try {
		    stream.close();
		} catch (Exception ignore) {
		    log.debug("IGNORED: " + ignore.getMessage());
		}
	    }
	}
    }	
	
    /**
     * 서버의 파일을 다운로드한다.
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    public static void downFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

	String downFileName = "";
	String orgFileName = "";

	if ((String)request.getAttribute("downFile") == null) {
	    downFileName = "";
	} else {
	    downFileName = (String)request.getAttribute("downFile");
	}

	if ((String)request.getAttribute("orginFile") == null) {
	    orgFileName = "";
	} else {
	    orgFileName = (String)request.getAttribute("orginFile");
	}

	File file = new File(downFileName);

	if (!file.exists()) {
	    throw new FileNotFoundException(downFileName);
	}

	if (!file.isFile()) {
	    throw new FileNotFoundException(downFileName);
	}
	
	
	
	byte[] b = new byte[BUFF_SIZE]; //buffer size 2K.
	
	//orgFileName = new String(orgFileName.getBytes(),"UTF-8");
	
	int fSize = (int)file.length();
	
//	response.setContentType("application/x-msdownload");
//	response.setHeader("Content-Disposition:", "attachment; filename=" + orgFileName);
//	response.setHeader("Content-Transfer-Encoding", "binary");
//	response.setHeader("Pragma", "no-cache");
//	response.setHeader("Expires", "0");
	
	response.setBufferSize(fSize);
	response.setContentType("application/x-msdownload");
	//response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fvo.getOrignlFileNm(), "utf-8") + "\"");
	setDisposition(orgFileName, request, response);
	response.setContentLength(fSize);

	BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
	BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
	int read = 0;

	while ((read = fin.read(b)) != -1) {
	    outs.write(b, 0, read);
	}

	outs.close();
	fin.close();
    }

    /**
     * 첨부로 등록된 파일을 서버에 업로드한다.
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public static HashMap<String, String> uploadFile(MultipartFile file) throws Exception {

	HashMap<String, String> map = new HashMap<String, String>();
	//Write File 이후 Move File????
	String newName = "";
	String stordFilePath = EgovProperties.getProperty("Globals.fileStorePath");
	String orginFileName = file.getOriginalFilename();

	int index = orginFileName.lastIndexOf(".");
	//String fileName = orginFileName.substring(0, _index);
	String fileExt = orginFileName.substring(index + 1);
	long size = file.getSize();

	//newName 은 Naming Convention에 의해서 생성
	newName = EgovStringUtil.getTimeStamp() + "." + fileExt;
	writeFile(file, newName, stordFilePath);
	//storedFilePath는 지정		
	map.put(Globals.ORIGIN_FILE_NM, orginFileName);
	map.put(Globals.UPLOAD_FILE_NM, newName);
	map.put(Globals.FILE_EXT, fileExt);
	map.put(Globals.FILE_PATH, stordFilePath);
	map.put(Globals.FILE_SIZE, String.valueOf(size));

	return map;
    }

    /**
     * 파일을 실제 물리적인 경로에 생성한다.
     * 
     * @param file
     * @param newName
     * @param stordFilePath
     * @throws Exception
     */
    protected static void writeFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
	InputStream stream = null;
	OutputStream bos = null;
	
	try {
	    stream = file.getInputStream();
	    File cFile = new File(stordFilePath);

	    if (!cFile.isDirectory())
		cFile.mkdir();

	    bos = new FileOutputStream(stordFilePath + File.separator + newName);

	    int bytesRead = 0;
	    byte[] buffer = new byte[BUFF_SIZE];

	    while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
		bos.write(buffer, 0, bytesRead);
	    }
	} catch (FileNotFoundException fnfe) {
	    fnfe.printStackTrace();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (bos != null) {
		try {
		    bos.close();
		} catch (Exception ignore) {
		    Logger.getLogger(EgovFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
		}
	    }
	    if (stream != null) {
		try {
		    stream.close();
		} catch (Exception ignore) {
		    Logger.getLogger(EgovFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
		}
	    }
	}
    }

    /**
     * 서버 파일에 대하여 다운로드를 처리한다.
     * 
     * @param response
     * @param streFileNm
     *            : 파일저장 경로가 포함된 형태
     * @param orignFileNm
     * @throws Exception
     */
    public void downFile(HttpServletResponse response, String streFileNm, String orignFileNm) throws Exception {
	String downFileName = streFileNm;
	String orgFileName = orignFileNm;

	File file = new File(downFileName);
	//log.debug(this.getClass().getName()+" downFile downFileName "+downFileName);
	//log.debug(this.getClass().getName()+" downFile orgFileName "+orgFileName);

	if (!file.exists()) {
	    throw new FileNotFoundException(downFileName);
	}

	if (!file.isFile()) {
	    throw new FileNotFoundException(downFileName);
	}

	//byte[] b = new byte[BUFF_SIZE]; //buffer size 2K.
	int fSize = (int)file.length();
	if (fSize > 0) {
	    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
	    String mimetype = "text/html"; //"application/x-msdownload"

	    response.setBufferSize(fSize);
	    response.setContentType(mimetype);
	    response.setHeader("Content-Disposition:", "attachment; filename=" + orgFileName);
	    response.setContentLength(fSize);
	    //response.setHeader("Content-Transfer-Encoding","binary");
	    //response.setHeader("Pragma","no-cache");
	    //response.setHeader("Expires","0");
	    FileCopyUtils.copy(in, response.getOutputStream());
	    in.close();
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	}
		
	/*
	String uploadPath = propertiesService.getString("fileDir");

	File uFile = new File(uploadPath, requestedFile);
	int fSize = (int) uFile.length();

	if (fSize > 0) {
	    BufferedInputStream in = new BufferedInputStream(new FileInputStream(uFile));

	    String mimetype = "text/html";

	    response.setBufferSize(fSize);
	    response.setContentType(mimetype);
	    response.setHeader("Content-Disposition", "attachment; filename=\""
					+ requestedFile + "\"");
	    response.setContentLength(fSize);

	    FileCopyUtils.copy(in, response.getOutputStream());
	    in.close();
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	} else {
	    response.setContentType("text/html");
	    PrintWriter printwriter = response.getWriter();
	    printwriter.println("<html>");
	    printwriter.println("<br><br><br><h2>Could not get file name:<br>" + requestedFile + "</h2>");
	    printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
	    printwriter.println("<br><br><br>&copy; webAccess");
	    printwriter.println("</html>");
	    printwriter.flush();
	    printwriter.close();
	}
	//*/
		
		
	/*		
	response.setContentType("application/x-msdownload");	        
	response.setHeader("Content-Disposition:", "attachment; filename=" + new String(orgFileName.getBytes(),"UTF-8" ));
	response.setHeader("Content-Transfer-Encoding","binary");
	response.setHeader("Pragma","no-cache");
	response.setHeader("Expires","0");

	BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
	BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
	int read = 0;
	
	while ((read = fin.read(b)) != -1) {
	    outs.write(b,0,read);
	}	
	log.debug(this.getClass().getName()+" BufferedOutputStream Write Complete!!! ");
		
	outs.close();
    	fin.close();		
	//*/
    }
    
    /**
     * 브라우저 구분 얻기.
     * 
     * @param request
     * @return
     */
    private static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }
    /**
     * Disposition 지정하기.
     * 
     * @param filename
     * @param request
     * @param response
     * @throws Exception
     */
    private static void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
	String browser = getBrowser(request);
	
	String dispositionPrefix = "attachment; filename=";
	String encodedFilename = null;
	
	if (browser.equals("MSIE")) {
	    encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
	} else if (browser.equals("Firefox")) {
	    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
	} else if (browser.equals("Opera")) {
	    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
	} else if (browser.equals("Chrome")) {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < filename.length(); i++) {
		char c = filename.charAt(i);
		if (c > '~') {
		    sb.append(URLEncoder.encode("" + c, "UTF-8"));
		} else {
		    sb.append(c);
		}
	    }
	    encodedFilename = sb.toString();
	} else {
	    //throw new RuntimeException("Not supported browser");
	    throw new IOException("Not supported browser");
	}
	
	response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

	if ("Opera".equals(browser)){
	    response.setContentType("application/octet-stream;charset=UTF-8");
	}
    }

}
