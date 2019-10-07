
package com.ziaan.library;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

 /**
 * <p> ����: request/multipart �� session ���� ���̺귯��</p> 
 * <p> ����: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author ������
 *@date 2003. 12
 *@version 1.0
 */
public class RequestManager { 
    /** 
    * ���ھ���  ������
    */
    public RequestManager() { }

    /**
    *request ��ü�� �޾� ���ǽð� ����, ��Ƽ��Ʈ ��ó��, box hashtable �� session , parameters �� ��� ��ȯ�Ѵ�.
    @param request HttpServletRequest �� request ��ü�� ���ڷ� ����
    @return RequestBox request ��ü���� ���� �Ķ���� name �� value, session ��ü�� ���� hashtable ��ü�� ��ȯ��
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
                    

		//���� ����� ���� ó��...//file����� ���� ���긴 ��Ī�� �����´�.. ������ default�� �����´�.
		//11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
		//���� ����� ���� ó��...//file����� ���� ���긴 ��Ī�� �����´�.. ������ default�� �����´�.
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
 // ������ ���丮 �̻� ����....
/*                if ( !v_subj.equals("") && !v_year.equals("") ) {     											//      ���� ���� ��츸 �ش��                    
               	
                    File yearDir = new java.io.File(v_updir + File.separator + v_year); //      '����' ���丮 

                    if ( !yearDir.isDirectory() ) {                      									//  ���� ���丮 ������ ���� ����  
                        yearDir.mkdir();                      
                    }
                    File subjDir = new java.io.File(yearDir, v_subj);                		//  '����' ���丮 

                    if ( !subjDir.isDirectory() ) {                      									//  ���� ���丮 ������ ���� ����  
                        subjDir.mkdir();                      
                    }
                    v_updir = v_updir + v_year + File.separator + v_subj;               //      ���� ����� ������ ����Ǵ� ���

                }
                */
                
                //multi = new MultipartRequest(request, v_updir, v_servletName, v_userid);         
                multi = new MultipartRequest(request, v_updir, v_servletName, v_userid, v_filesize);
          
                multi.readRequest();        //      ���� �� request�� ���ε�ȴ�
                box = multi.getBox(request);     
                

                ///////////////////////////////////////////////////////////////////////////////////////////////////
                //22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222
                //	���� �뷮�� ���� Ȯ���ڸ� üũ�Ѵ�.
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

            	if (i==0) {//���Ͽ뷮�� ���� �ʾƼ� ÷�ΰ� ��ҵ� ������ �����...
            		   //1.���� ���ε尡 �뷮���� ���������� �˸���....
            		   //2.�̹� �ø� ���ϵ��� �����Ѵ�.
            		box.put("p_isuploadok","0");	//1.................
            		while(fn.hasMoreElements()) {//���� ����...
            			//System.out.println("474747474747474747474747474======"+multi.getNewFileName((String)fn.nextElement()));
            			FileManager.deleteFile(multi.getNewFileName((String)fn.nextElement()));//2............................
            		}            		
            	} else {	//Ȯ���ڰ� ���� ������ ���...
            		while(fn.hasMoreElements()) {//���� Ȯ���� ��...            			
            			
            			v_tmpfile = multi.getNewFileName((String)fn.nextElement());	//÷������
            			v_tmp	= v_tmpfile.length();		//÷������ ����
            			v_filetypename = "";//÷������ Ȯ����...            	
            			
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
     *request ��ü�� �޾� ���ǽð� ����, ��Ƽ��Ʈ ��ó��, box hashtable �� session , parameters �� ��� ��ȯ�Ѵ�.
     @param request HttpServletRequest �� request ��ü�� ���ڷ� ����
     @return RequestBox request ��ü���� ���� �Ķ���� name �� value, session ��ü�� ���� hashtable ��ü�� ��ȯ��
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
             request.setCharacterEncoding("utf-8");  //�ٱ������� UTF-8
             
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
                  				
                 if(v_isreport.equals("Y")){     											//      ����Ʈ ���� ��츸 �ش��
                 
 	                if(!v_subj.equals("") && !v_year.equals("") && !v_subjseq.equals("") && !v_class.equals("") && !v_weeklyseq.equals("") && !v_weeklysubseq.equals("")) {                    
 						File subjDir = new java.io.File(v_updir + File.separator + v_subj); //      '�����ڵ�' ���丮 
 						if(!subjDir.isDirectory()) {                      									//  �����ڵ� ���丮 ������ ���� ����  
 						    subjDir.mkdir();                      
 						}             	
 						File yearDir = new java.io.File(subjDir, v_year); //      '����' ���丮 
 						if(!yearDir.isDirectory()) {                      									//  ���� ���丮 ������ ���� ����  
 						    yearDir.mkdir();                      
 						}
 						File subjseqDir = new java.io.File(yearDir, v_subjseq);                		//  '�б�' ���丮 
 						if(!subjseqDir.isDirectory()) {                      									//  �б� ���丮 ������ ���� ����  
 						    subjseqDir.mkdir();                      
 						}
 						File classDir = new java.io.File(subjseqDir, v_class);                		//  '�й�' ���丮 
 						if(!classDir.isDirectory()) {                      									//  �й� ���丮 ������ ���� ����  
 						    classDir.mkdir();                      
 						}
 						File weeklyseqDir = new java.io.File(classDir, v_weeklyseq);                		//  '����' ���丮 
 						if(!weeklyseqDir.isDirectory()) {                      									//  ���� ���丮 ������ ���� ����  
 						    weeklyseqDir.mkdir();                      
 						}
 						File weeklysubseqDir = new java.io.File(weeklyseqDir, v_weeklysubseq);                		//  'ȸ��' ���丮 
 						if(!weeklysubseqDir.isDirectory()) {                      									//  ȸ�� ���丮 ������ ���� ����  
 						    weeklysubseqDir.mkdir();                      
 						}

 	                    v_updir = v_updir + v_subj + File.separator + v_year + File.separator + v_subjseq + File.separator + v_class + File.separator + v_weeklyseq + File.separator + v_weeklysubseq;               //      ����Ʈ ����� ������ ����Ǵ� ���
 	                }
 	                
                 }
                 
                 if(v_isDummyBlog.equals("Y"))
                 {
                 	
                 }else {
 	                if(v_isblog.equals("Y")){
 	                	File subjDir = new java.io.File(v_updir + "/" + v_blogid);
 	                	if(!subjDir.isDirectory()) {                      									//  �����ڵ� ���丮 ������ ���� ����  
 						    subjDir.mkdir();                      
 						}
 	                	v_updir = v_updir + "/" + v_blogid;
 	                }
                 }
                 
                 multi = new MultipartRequest(request, v_updir, v_servletName, v_userid);         
           
                 multi.readRequest();        //      ���� �� request�� ���ε�ȴ�
                 
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
             
             //box.createTranslate(); //�ٱ��� ����
             
          }catch(Exception ex) {
             ex.printStackTrace(); 
         }
      //   if(box.getString("s_grcode").equals("")) box.put("s_grcode", "N000001");
         return box;
     }

     
    /**
    * ContentType ���� Multipart form �ΰ� ���θ� Ȯ���Ѵ�
    @param request HttpServletRequest �� request ��ü�� ���ڷ� ����
    @return Multipart form �̸� true �� ��ȯ��
    */
    public static boolean isMultipartForm(HttpServletRequest request) { 
        String v_contentType = StringManager.chkNull(request.getContentType() );

        return v_contentType.indexOf("multipart/form-data") >= 0;           //      Multipart �� �Ѿ�Դ��� ����
    }
    
    /**
    * ������ξּ� �������� �����Ͽ� ��ȯ��
    @param servletPath ���� ��θ� ���ڷ� ����
    @return �������� ��ȯ��
    */
    public static String getServetName(String servletPath) { 
    	// EduStart, EduProgress ���� ���� ���� Servlet ���� ������ �̸��� �ƴ϶� ���� ���Ƿ� �̸� ������ @ 20100520 Parks
    	String Servletnm = servletPath.substring(servletPath.lastIndexOf(".") +1);
    	Servletnm = Servletnm.replace("Servlet", "");
    	return Servletnm;
    }
            
    /**
    * ��Ű�� ���� box ��ü�� ��´�.
    @param request HttpServletRequest �� request ��ü�� ���ڷ� ����
    @return RequestBox request ��ü���� ��Ű�� name �� value �� ���� hashtable ��ü�� ��ȯ��
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
     * ������ξּ� �������� �����Ͽ� ��ȯ��
     @param servletPath ���� ��θ� ���ڷ� ����
     @return �������� ��ȯ��
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
      * ���ε� ���丮 ����
      @param �������� ����, ������ ���� ���丮��
      @return ������ ���� ���丮����
      */
      public static File makeUploadDir(File updir, String subdir) {
          File tempDir = new java.io.File(updir, subdir);
          if(!tempDir.isDirectory()) { tempDir.mkdir(); }

          return tempDir;
      }
}
