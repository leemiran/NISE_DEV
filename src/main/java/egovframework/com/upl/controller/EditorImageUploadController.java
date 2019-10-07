/* 
 * EditorImageUploadController.java		1.00	2010-09-02 
 *
 * Copyright (c) 2008 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of David.Kim(KJS).  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with David.Kim(KJS).
 */
package egovframework.com.upl.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import egovframework.rte.fdl.property.EgovPropertyService;



/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : EditorImageUploadController.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2010-09-02 created by ?
 * 1.1	
 * </pre>
 */

@SuppressWarnings("unchecked")
@Controller
public class EditorImageUploadController extends AbstractController {
	
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    @RequestMapping(value="/editorImageUploadController.do")
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
    	MultipartHttpServletRequest mpRequest = (MultipartHttpServletRequest) request;
		Iterator fileNameIterator = mpRequest.getFileNames();
		
		while (fileNameIterator.hasNext()) {
			
			try {
				MultipartFile multipartFile = mpRequest.getFile((String) fileNameIterator.next());
				String storePath = null;
				String filePath = "";
				
				String editorId = "";
				editorId = request.getParameter("editorId");
				
				if(editorId == null || editorId.equals("")){
					editorId = "contsText";
				}
				if ("".equals(storePath) || storePath == null) {
					 filePath = propertiesService.getString("Globals.fileStorePath");
				} else {
				     filePath = propertiesService.getString("Globals.fileStorePath") + storePath + "/";
				}
				
				filePath = filePath + "images/editorImage/";
				
				File directory = new File(filePath);
				if (!directory.isDirectory()) {
					directory.mkdirs();
				}
				
				File file = new File(filePath + multipartFile.getOriginalFilename());
				
				/*System.out.println(filePath);
				System.out.println(multipartFile.getOriginalFilename());*/
				file = this.rename(file);
				
				multipartFile.transferTo( file);
				
				String fileName = "/upload/images/editorImage/" + file.getName();
				//fileName = URLEncoder.encode(fileName, "UTF-8");
				System.out.println(fileName);
				
				
				PrintWriter out = null;
				
				try {
					response.setContentType("text/html;charset=UTF-8");
					out = response.getWriter();
					out.print("<HTML>");
					out.print("<BODY>");
					out.println("<script language=javascript>");
					out.println("	parent.parent.insertIMG('" + fileName + "','"+editorId+"');");
					out.println("	window.location='/script/editor/imgupload.jsp?editorId="+editorId+"';");
					out.println("	parent.parent.oEditors.getById['"+editorId+"'].exec('SE_TOGGLE_FILEUPLOAD_LAYER');");
					out.println("</script>");
					out.print("</BODY>");
					out.print("</HTML>");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					out.flush();
					out.close();
				}
				// response 처리 요망
			} catch (Exception e) {
				e.printStackTrace();
				// response 처리 요망
			}
		}
		return null;
	}
	
	
    public File rename(File f)                                     
	  {                                                              
	    if (createNewFile(f)) {                                      
	      return f;                                                  
	    }                                                            
	    String name = f.getName();                                   
	    String body = null;
	    String body2 = null;
	    String ext = null;
	    
	    String v_currentDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());    
	    
	    int dot = name.lastIndexOf(".");                             
	    if (dot != -1) {                                             
	    	//body = name.substring(0, dot);
	    	
	    	body = v_currentDate;
	    	ext = name.substring(dot);                                 
	    }                                                            
	    else {                                                       
	      body = name;                                               
	      ext = "";                                                  
	    }
	                                                                 
	    int count = 0;                                               
	    while ((!(createNewFile(f))) && (count < 9999)) {            
	      ++count;                                                   
	      String newName = body +"_"+ count + ext;                       
	      f = new File(f.getParent(), newName);                      
	    }                                                            
	                                                                 
	    return f;                                                    
	  }  
	  
	  private boolean createNewFile(File f) {
		  try {
			  return f.createNewFile();
		  } catch (IOException ignored) {}
		  return false;
	  }

}
