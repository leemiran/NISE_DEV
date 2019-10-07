/*
 * @(#)ScormImportBean.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import com.ziaan.library.AlertManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.scorm2004.runtime.server.FileHandler;
import com.ziaan.scorm2004.runtime.server.LMSManifestHandler;
import com.ziaan.scorm2004.validator.ADLValidatorOutcome;


/**
 * 컨텐츠 Import 관련 빈
 *
 * @version 1.0 2006. 4. 24.
 * @author Jin-pil Chung
 *
 */
public class ScormImportBean
{
    private static boolean _Debug = false;
    
    public static final String UPLOAD_TEMP_DIR = "tempUploads";
    public static int UPLOAD_SIZE_LIMIT = 1000 * 1024 * 1024;       // 업로드 최대 용량 : 100 Mbyte
    
    private Map uploadTempFiles( HttpServletRequest request, PrintWriter out, String uploadDir )
    {
        ServletRequestContext context = new ServletRequestContext( request );
        boolean isMultipart = ServletFileUpload.isMultipartContent( context );
        
        HashMap formMap = null;
        AlertManager alert = new AlertManager();

        try 
        {
            request.setCharacterEncoding("euc-kr");
            
            // Upload File 처리
            if ( isMultipart )
            {
                formMap = new HashMap();
                
//                uploadDir = theWebPath + "tempUploads" + fileSeparator + sessionID;
                
                File tempUploadDir = new File(uploadDir);
                
                if ( !tempUploadDir.isDirectory() )
                {
                    tempUploadDir.mkdirs();
                }
                
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setHeaderEncoding(request.getCharacterEncoding()); 
                upload.setSizeMax(UPLOAD_SIZE_LIMIT);
                
                List items = upload.parseRequest(request);

                String name = "";
                String value = "";
                
                Iterator iter = items.iterator();
                while( iter.hasNext() )
                {
                    FileItem item = (FileItem) iter.next();
                    
                    if ( item.isFormField() )
                    {
                        name = item.getFieldName();
                        value = item.getString("euc-kr");
                        formMap.put( name, value );
                    }
                    else
                    {
                        String fieldName = item.getFieldName();
                        String fileName =  item.getName().substring(item.getName().lastIndexOf("\\")+1);
                        String contentType = item.getContentType();
                        boolean isInMemory = item.isInMemory();
                        long sizeInBytes = item.getSize();
    
                        if ( contentType.equals("application/x-zip-compressed") ||
                                contentType.equals("text/xml") )
                        {
                            // temp 디렉토리에 업로드 파일 저장
                            File uploadedFile = new File( uploadDir + File.separator + fileName );
                            item.write(uploadedFile);
                            
                            if ( _Debug )
                            {
                                System.out.println( "Uploaded File : " + uploadedFile.getAbsolutePath() );
                            }
                            
                            formMap.put( "p_file", uploadedFile.getAbsolutePath() );
                        }
                        else
                        {
                            alert.alertFailMessage( out, "XML, ZIP 형식의 파일만 업로드 가능합니다." );
                            if ( _Debug )
                            {
                                System.out.println( "XML, ZIP 형식의 파일만 업로드 가능합니다." );
                            }
                        }
                    }
                }
            }
        }
        catch ( SizeLimitExceededException see ) 
        {
            alert.alertFailMessage( out, UPLOAD_SIZE_LIMIT/1000 + "kbyte 이상인 파일은 업로드가 불가능합니다." );
            System.out.println( "FILE 업로드 용량 초과!!" );
            see.printStackTrace();
            ErrorManager.getErrorStackTrace(see, out);
        }
        catch ( FileUploadException fue )
        {
            alert.alertFailMessage( out, "파일 업로드 중에 오류가 발생했습니다." );
            System.out.println( "FILE UPLOAD EXCEPTION!!" );
            fue.printStackTrace();
            ErrorManager.getErrorStackTrace(fue, out);
        }
        catch( Exception e )
        {
            e.printStackTrace();
            ErrorManager.getErrorStackTrace(e, out);
        }

        return formMap;
    }
    
    public Map importCourse(HttpServletRequest request, PrintWriter out, String theWebPath) throws Exception
    {
        Map resultMap = new HashMap();
        // List errorList = new ArrayList();
        
        try
        {
        
            // SampleRTE 임시 디렉토리 경로 생성, Rule : /WebRoot/tempUploads/SessionID/
            // HttpSession session = request.getSession(true);
            // String sessionID = session.getId();
            // String uploadDir = theWebPath + UPLOAD_TEMP_DIR + File.separator + sessionID;
            String uploadDir = theWebPath + UPLOAD_TEMP_DIR;
            if ( _Debug )
            {
                System.out.println("###### WEB PATH : " + theWebPath);
                System.out.println("###### UPLOAD_TEMP_DIR PATH : " + uploadDir);
            }
            
            // 임시 디렉토리에 파일 업로드, form Parameter값을 갖고 있는 formMap 반환 받음
            Map formMap = uploadTempFiles(request, out, uploadDir);
            
            String courseType = (String) formMap.get("p_course_type");
            
            // Validation 여부 결정
            // true  : 전체적인 Validation 수행 - PIF 일 땐  true 
            // false : IMSManifest.xml 파일 존재 여부와, Wellform 문서인지만 검사함) - MANI 일 땐 false
            boolean validation = false;
            String iTestType = "";
            
            if ( courseType.equals("CPKG") || courseType.equals("SCOS") )
            {
                validation = true;
                iTestType = "pif";
            }
            else if ( courseType.equals("MANI") )
            {
                validation = false;
                iTestType = "";
            }
    
            
            // LMSManifestHandler에서 Validation 수행
            String theXSDPath = theWebPath.substring(0, theWebPath.lastIndexOf(File.separator));
            
            if ( _Debug )
            {
                System.out.println("###### XSD PATH : " + theXSDPath);
            }
            
            LMSManifestHandler myManifestHandler = new LMSManifestHandler(theXSDPath);
            myManifestHandler.setCourseName( (String) formMap.get("p_coursenm") );
    //        myManifestHandler.setWebPath(theWebPath);
            myManifestHandler.setFormMap(formMap);
    
            // Validation 결과 객체 반환
            ADLValidatorOutcome result =
                    (ADLValidatorOutcome) myManifestHandler.processPackage( (String) formMap.get("p_file"), iTestType, validation );
            
            // 임시 디렉토리에 업로드 된 파일 삭제
            deleteTempFiles( uploadDir );
            
            
            if ( (result.getDoesIMSManifestExist() && result.getIsWellformed() && validation == false) || 
                 (result.getDoesIMSManifestExist() && result.getIsWellformed() && result.getIsValidRoot() &&
                  result.getIsValidToSchema() && result.getIsValidToApplicationProfile() && result.getDoRequiredCPFilesExist()) )
            {
                String courseCode = myManifestHandler.getCourseCode();
    
                resultMap.put("RESULT", "success" );
                resultMap.put("COURSE_CODE", courseCode );
    
                if ( _Debug )
                {
                    System.out.println( "RESULT      : " + "success" );
                    System.out.println( "COURSE_CODE : " + courseCode );
                }
                
                if ( validation )
                {
                    resultMap.put("VALIDATION", "true" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "VALIDATION  : " + "true" );
                    }
                }
                else
                {
                    resultMap.put("VALIDATION", "false" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "VALIDATION  : " + "false" );
                    }
                }
            }
            else
            {
                resultMap.put("RESULT", "fail" );
                
                if ( _Debug )
                {
                    System.out.println( "RESULT      : " + "fail" );
                }
                
                if ( validation )
                {
                    resultMap.put("VALIDATION", "true" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "VALIDATION  : " + "true" );
                    }
                }
                else
                {
                    resultMap.put("VALIDATION", "false" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "VALIDATION : " + "false" );
                    }
                }
                
                if ( result.getDoesIMSManifestExist() )
                {
                    resultMap.put("MANIFESTEXISTS", "true" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "MANIFESTEXISTS : " + "true" );
                    }
                }
                else
                {
//                    errorList.add( "* imsmanifest.xml file is not located at the root of the package.");
                    resultMap.put("MANIFESTEXISTS", "false" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "MANIFESTEXISTS : " + "false" );
                    }
                }
                
                if ( result.getIsWellformed() )
                {
                    resultMap.put("WELLFORMED", "true" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "WELLFORMED : " + "true" );
                    }
                }
                else
                {
//                    errorList.add( "* The imsmanifest.xml file is not well-formed.");
                    resultMap.put("WELLFORMED", "false" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "WELLFORMED : " + "false" );
                    }
                }
                
                if ( result.getIsValidRoot() )
                {
                    resultMap.put("VALIDROOT", "true" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "VALIDROOT : " + "true" );
                    }
                }
                else
                {
//                    errorList.add( "* The root element does not belong to the expected namespace.");
                    resultMap.put("VALIDROOT", "false" );

                    if ( _Debug )
                    {
                        System.out.println( "VALIDROOT : " + "false" );
                    }
                }
                
                if ( result.getIsValidToSchema() )
                {
                    resultMap.put("VALIDTOSCHEMA", "true" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "VALIDTOSCHEMA : " + "true" );
                    }
                }
                else
                {
//                    errorList.add( "* The imsmanifest.xml file is not valid against the schemas.");
                    resultMap.put("VALIDTOSCHEMA", "false" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "VALIDTOSCHEMA : " + "false" );
                    }
                }
                
                if ( result.getIsValidToApplicationProfile() )
                {
                    resultMap.put("VALIDTOPROFILE", "true" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "VALIDTOPROFILE : " + "true" );
                    }
                }
                else
                {
//                    errorList.add( "* The imsmanifest.xml file is not valid to the requirements defined in SCORM 2004.");
                    resultMap.put("VALIDTOPROFILE", "false" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "VALIDTOPROFILE : " + "false" );
                    }
                }
                
                if ( result.getDoRequiredCPFilesExist() )
                {
                    resultMap.put("REQUIREDFILES", "true" );
                    
                    if ( _Debug )
                    {
                        System.out.println( "REQUIREDFILES : " + "true" );
                    }
                }
                else
                {
//                    errorList.add( "Contents File이 존재하지 않습니다.");
                    resultMap.put("REQUIREDFILES", "false" );

                    if ( _Debug )
                    {
                        System.out.println( "REQUIREDFILES : " + "false" );
                    }
                }
                
//                resultMap.put( "ERROR", errorList );
            }
        }
        catch( Exception e )
        {
            System.out.println( "컨텐츠 등록중에 오류 발생!!" );
            throw new Exception( e.getMessage() );
        }

        return resultMap;
    }

    private void deleteTempFiles(String uploadDir)
    {
        FileHandler fileHandler = new FileHandler();
        fileHandler.deleteTempFiles(uploadDir);
    }
}
