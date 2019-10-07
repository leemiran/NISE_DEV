
package com.ziaan.library;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

 /**
 * <p> 제목: request/multipart 와 session 관련 라이브러리</p> 
 * <p> 설명: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author 이정한
 *@date 2003. 12
 *@version 1.0
 */
public class RequestManager { 
    /** 
    * 인자없는  생성자
    */
    public RequestManager() { }

    /**
    *request 객체를 받아 세션시간 설정, 멀티파트 폼처리, box hashtable 에 session , parameters 를 담아 반환한다.
    @param request HttpServletRequest 의 request 객체를 인자로 받음
    @return RequestBox request 객체에서 받은 파라미터 name 과 value, session 객체를 담은 hashtable 객체를 반환함
    */
    public static RequestBox getBox(HttpServletRequest request)  {                
        ConfigSet conf = null;
        HttpSession session = null;
        RequestBox box = null;
        MultipartRequest multi = null;
        String filetype = "";
        int typenum = 0;
        String v_userid = "";
        
        try { 
            conf = new ConfigSet();
            
            session = request.getSession(true);            
    
            if ( isMultipartForm(request)) { 
                String v_servletName = getServetName(request.getRequestURI() ); //System.out.println("v_servletName : " + v_servletName);
                String v_currentDate = FormatDate.getDate("yyyyMMddHHmmss");

                if ( session.getAttribute("userid") != null ) 
                    v_userid = session.getAttribute("userid").toString();

                String v_dirKey = conf.getDir(conf.getProperty("dir.upload"), v_servletName);
                // System.out.println("v_dirKey ==  ==  ==  == = >>  >>  > " +v_dirKey);
                    

		//파일 사이즈를 위한 처리...//file사이즈를 위한 서브릿 명칭을 가져온다.. 없으면 default로 가져온다.
		//11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
		//파일 사이즈를 위한 처리...//file사이즈를 위한 서브릿 명칭을 가져온다.. 없으면 default로 가져온다.
		String v_filesizekey	= conf.getDir(conf.getProperty("file.size"), v_servletName);						
		int    v_filesize	= conf.getInt("file.size."+v_filesizekey);
		
		String v_filetypekey	= conf.getDir(conf.getProperty("file.type"), v_servletName);						
		String v_filetype	= conf.getProperty("file.type."+v_filetypekey);		
		//System.out.println("\n\n v_servletName0000000000000000000000000 : " + v_filesizekey);
		//11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
		//System.out.println("\n\n v_servlettypetypetype0000000000000000000000000 : " + v_filetype);
		
		                    
                String v_updir = conf.getProperty("dir.upload." + v_dirKey);               
                
                String v_subj = StringManager.chkNull(request.getParameter("g_subj") );
                String v_year = StringManager.chkNull(request.getParameter("g_year") );
 // 재제출 디렉토리 이상 막음....
/*                if ( !v_subj.equals("") && !v_year.equals("") ) {     											//      과제 제출 경우만 해당됨                    
               	
                    File yearDir = new java.io.File(v_updir + File.separator + v_year); //      '연도' 디렉토리 

                    if ( !yearDir.isDirectory() ) {                      									//  연도 디렉토리 없으면 새로 생성  
                        yearDir.mkdir();                      
                    }
                    File subjDir = new java.io.File(yearDir, v_subj);                		//  '과정' 디렉토리 

                    if ( !subjDir.isDirectory() ) {                      									//  과정 디렉토리 없으면 새로 생성  
                        subjDir.mkdir();                      
                    }
                    v_updir = v_updir + v_year + File.separator + v_subj;               //      과제 제출시 파일이 저장되는 경로

                }
                */
                
                //multi = new MultipartRequest(request, v_updir, v_servletName, v_userid);         
                multi = new MultipartRequest(request, v_updir, v_servletName, v_userid, v_filesize);
          
                multi.readRequest();        //      파일 및 request가 업로드된다
                box = multi.getBox(request);     
                

                ///////////////////////////////////////////////////////////////////////////////////////////////////
                //22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222
                //	파일 용량및 파일 확장자를 체크한다.
                //////////////////////////////////////////////////////////////////////////////////////////////////
            Hashtable h = new Hashtable();
            Hashtable hfn = new Hashtable();
            h = multi.getFileUploadStatus();
            hfn = multi.newFileParameters;
            Enumeration e = h.elements();            
            Enumeration fn = hfn.keys();
            
            String v_filetypename = "";
            int v_tmp	= 0;
            String v_tmpfile	= "";
            
            int i = 0;            
            box.put("p_isuploadok","1");                           

            while(e.hasMoreElements()){
            		//i = e.nextElement();
            		i = ((Integer)e.nextElement()).intValue();

            	if (i==0) {//파일용량이 맞지 않아서 첨부가 취소된 파일이 생겼다...
            		   //1.파일 업로드가 용량으로 실패했음을 알리고....
            		   //2.이미 올린 파일들을 삭제한다.
            		box.put("p_isuploadok","0");	//1.................
            		while(fn.hasMoreElements()) {//파일 삭제...
            			//System.out.println("474747474747474747474747474======"+multi.getNewFileName((String)fn.nextElement()));
            			FileManager.deleteFile(multi.getNewFileName((String)fn.nextElement()));//2............................
            		}            		
            	} else {	//확장자가 맞지 않으면 취소...
            		while(fn.hasMoreElements()) {//파일 확장자 비교...            			
            			
            			v_tmpfile = multi.getNewFileName((String)fn.nextElement());	//첨부파일
            			v_tmp	= v_tmpfile.length();		//첨부파일 길이
            			v_filetypename = "";//첨부파일 확장자...            	
            			
            			if (v_tmp>3) {
            				v_filetypename = v_tmpfile.substring(v_tmp-3,v_tmp);
            				v_filetypename = v_filetypename.toLowerCase();
            			}
            			
            			if (v_filetype.indexOf(v_filetypename)<0) {
            				box.put("p_isuploadok","2");
            			}            			
            		}
            	}                
            }            
                ///////////////////////////////////////////////////////////////////////////////////////////////////
                //22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222
                ////////////////////////////////////////////////////////////////////////////////////////////////// 
                                
                box.put("p_updir", v_updir);                 
            } else {                
                box = new RequestBox("requestbox");
                Enumeration e1 = request.getParameterNames();
                                
                if(request.getCharacterEncoding() == null){
                
                	while ( e1.hasMoreElements() ) { 
                		String key = (String)e1.nextElement();
                		box.put(key, StringManager.korEncode(request.getParameterValues(key),key));
                	}
                }
                else{
                	while ( e1.hasMoreElements() ) { 
                		String key = (String)e1.nextElement();
                		box.put(key, request.getParameterValues(key));
                		
                	}
                	
                }
                /*
                while ( e1.hasMoreElements() ) { 
                    String key = (String)e1.nextElement();
                    String value = request.getParameter(key);
                    
                    try{
                    	value = new String(value.getBytes("8859_1"), "EUC-KR");
                    } catch (Exception e){
                    	value = value;
                    }
                    
                    box.put(key, value);
                }
                */
            }
            box.put("reqeustServletName", request.getRequestURI());
            box.put("session", session);
            box.put("userip", request.getRemoteAddr());

            box.put("p_grservlet", getServetName(request.getRequestURI())+"Servlet");
         } catch ( Exception ex ) { 
            ex.printStackTrace(); 
        }
     //   if ( box.getString("s_grcode").equals("")) box.put("s_grcode", "N000001");
        return box;
    }
    
    
    /**
     *request 객체를 받아 세션시간 설정, 멀티파트 폼처리, box hashtable 에 session , parameters 를 담아 반환한다.
     @param request HttpServletRequest 의 request 객체를 인자로 받음
     @return RequestBox request 객체에서 받은 파라미터 name 과 value, session 객체를 담은 hashtable 객체를 반환함
     */
     public static RequestBox getBox2(HttpServletRequest request)  {                
         ConfigSet conf = null;
         HttpSession session = null;
         RequestBox box = null;
         MultipartRequest multi = null;
         String filetype = "";
         int typenum = 0;
         String v_userid = "";
         String v_blogid = "";
         
         try { 
             conf = new ConfigSet();
             request.setCharacterEncoding("utf-8");  //다국어지원 UTF-8
             
             session = request.getSession(true);            
     
             if(isMultipartForm(request)) {
                 String v_servletName = getServetName(request.getRequestURI());//System.out.println("v_servletName : " + v_servletName);
                 
                 String v_currentDate = FormatDate.getDate("yyyyMMddHHmmss");

                 if(session.getAttribute("userid") != null) 
                     v_userid = session.getAttribute("userid").toString();
                 
                 if(session.getAttribute("blogid") != null) 
                     v_blogid = session.getAttribute("blogid").toString();

                 String v_dirKey = conf.getDir(conf.getProperty("dir.upload"), v_servletName);
                     
                 String v_updir = conf.getProperty("dir.upload." + v_dirKey);

 				String v_subj = StringManager.chkNull((String)session.getAttribute("psubj"));
 				String v_year = StringManager.chkNull((String)session.getAttribute("pyear"));
 				String v_subjseq = StringManager.chkNull((String)session.getAttribute("psubjseq"));
 				String v_class = StringManager.chkNull((String)session.getAttribute("pclass"));
 				String v_weeklyseq = StringManager.chkNull((String)session.getAttribute("pweeklyseq"));
 				String v_weeklysubseq = StringManager.chkNull((String)session.getAttribute("pweeklysubseq"));
 				
 				String v_isreport = StringManager.chkNull((String)session.getAttribute("pisreport"));
 				String v_isblog = StringManager.chkNull((String)session.getAttribute("pisblog"));
 				String v_isDummyBlog = StringManager.chkNull((String)session.getAttribute("p_dummyBlog"));
                  				
                 if(v_isreport.equals("Y")){     											//      리포트 제출 경우만 해당됨
                 
 	                if(!v_subj.equals("") && !v_year.equals("") && !v_subjseq.equals("") && !v_class.equals("") && !v_weeklyseq.equals("") && !v_weeklysubseq.equals("")) {                    
 						File subjDir = new java.io.File(v_updir + File.separator + v_subj); //      '과정코드' 디렉토리 
 						if(!subjDir.isDirectory()) {                      									//  과정코드 디렉토리 없으면 새로 생성  
 						    subjDir.mkdir();                      
 						}             	
 						File yearDir = new java.io.File(subjDir, v_year); //      '연도' 디렉토리 
 						if(!yearDir.isDirectory()) {                      									//  연도 디렉토리 없으면 새로 생성  
 						    yearDir.mkdir();                      
 						}
 						File subjseqDir = new java.io.File(yearDir, v_subjseq);                		//  '학기' 디렉토리 
 						if(!subjseqDir.isDirectory()) {                      									//  학기 디렉토리 없으면 새로 생성  
 						    subjseqDir.mkdir();                      
 						}
 						File classDir = new java.io.File(subjseqDir, v_class);                		//  '분반' 디렉토리 
 						if(!classDir.isDirectory()) {                      									//  분반 디렉토리 없으면 새로 생성  
 						    classDir.mkdir();                      
 						}
 						File weeklyseqDir = new java.io.File(classDir, v_weeklyseq);                		//  '주차' 디렉토리 
 						if(!weeklyseqDir.isDirectory()) {                      									//  주차 디렉토리 없으면 새로 생성  
 						    weeklyseqDir.mkdir();                      
 						}
 						File weeklysubseqDir = new java.io.File(weeklyseqDir, v_weeklysubseq);                		//  '회차' 디렉토리 
 						if(!weeklysubseqDir.isDirectory()) {                      									//  회차 디렉토리 없으면 새로 생성  
 						    weeklysubseqDir.mkdir();                      
 						}

 	                    v_updir = v_updir + v_subj + File.separator + v_year + File.separator + v_subjseq + File.separator + v_class + File.separator + v_weeklyseq + File.separator + v_weeklysubseq;               //      리포트 제출시 파일이 저장되는 경로
 	                }
 	                
                 }
                 
                 if(v_isDummyBlog.equals("Y"))
                 {
                 	
                 }else {
 	                if(v_isblog.equals("Y")){
 	                	File subjDir = new java.io.File(v_updir + "/" + v_blogid);
 	                	if(!subjDir.isDirectory()) {                      									//  과정코드 디렉토리 없으면 새로 생성  
 						    subjDir.mkdir();                      
 						}
 	                	v_updir = v_updir + "/" + v_blogid;
 	                }
                 }
                 
                 multi = new MultipartRequest(request, v_updir, v_servletName, v_userid);         
           
                 multi.readRequest();        //      파일 및 request가 업로드된다
                 
                 box = multi.getBox(request);     
                 
                 box.put("p_updir", v_updir);                 
             }
             else {                
                 box = new RequestBox("requestbox");
                 Enumeration e1 = request.getParameterNames();
                 while(e1.hasMoreElements()){
                     String key = (String)e1.nextElement();
                     box.put(key, request.getParameterValues(key));
                 }
             } 
             box.put("session", session);
             box.put("userip", request.getRemoteAddr());
             
             //box.createTranslate(); //다국어 언어선택
             
          }catch(Exception ex) {
             ex.printStackTrace(); 
         }
      //   if(box.getString("s_grcode").equals("")) box.put("s_grcode", "N000001");
         return box;
     }

     
    /**
    * ContentType 에서 Multipart form 인가 여부를 확인한다
    @param request HttpServletRequest 의 request 객체를 인자로 받음
    @return Multipart form 이면 true 를 반환함
    */
    public static boolean isMultipartForm(HttpServletRequest request) { 
        String v_contentType = StringManager.chkNull(request.getContentType() );

        return v_contentType.indexOf("multipart/form-data") >= 0;           //      Multipart 로 넘어왔는지 여부
    }
    
    /**
    * 서블릿경로애서 서블릿명을 추출하여 반환함
    @param servletPath 서블릿 경로를 인자로 받음
    @return 서블릿명을 받환함
    */
    public static String getServetName(String servletPath) { 
    	// EduStart, EduProgress 같은 서블릿 들은 Servlet 으로 끝나는 이름이 아니라 에러 나므로 이를 수정함 @ 20100520 Parks
    	String Servletnm = servletPath.substring(servletPath.lastIndexOf(".") +1);
    	Servletnm = Servletnm.replace("Servlet", "");
    	return Servletnm;
    }
            
    /**
    * 쿠키를 담은 box 객체를 얻는다.
    @param request HttpServletRequest 의 request 객체를 인자로 받음
    @return RequestBox request 객체에서 쿠키의 name 과 value 를 담은 hashtable 객체를 반환함
    */
    public static RequestBox getBoxFromCookie(HttpServletRequest request)  { 
        RequestBox cookiebox = new RequestBox("cookiebox");
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if ( cookies == null ) return cookiebox;
        
        for ( int i = 0; cookies != null && i< cookies.length; i++ ) { 
            String key = cookies[i].getName();
            String value = cookies[i].getValue();
            if ( value == null ) value = "";
            String[] values = new String[1];
            values[0] = value;
            cookiebox.put(key,values);
        }

        return cookiebox;
    }
    
    /**
     * 서블릿경로애서 서블릿명을 추출하여 반환함
     @param servletPath 서블릿 경로를 인자로 받음
     @return 서블릿명을 받환함
     */
     public static String getFileHint(String filehint) {
       try{
            return filehint.substring(0, filehint.lastIndexOf("Servlet"));
        }catch(Exception ex) {
         System.out.println(filehint);
            return filehint;
       }
     }
     
     /**
      * 업로드 디렉토리 생성
      @param 상위폴더 파일, 생성할 하위 디렉토리명
      @return 생성된 하위 디렉토리파일
      */
      public static File makeUploadDir(File updir, String subdir) {
          File tempDir = new java.io.File(updir, subdir);
          if(!tempDir.isDirectory()) { tempDir.mkdir(); }

          return tempDir;
      }
}
