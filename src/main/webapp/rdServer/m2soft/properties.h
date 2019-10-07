<%@ page import="java.io.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.log.m.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.beans.*" %>
<% String contentType = Message.getcontentType(); response.setContentType(contentType); %>
<%!
   String mrdpath,webpath,servicename,serverip,ocxhtmpath,logpath,cabpath,datapath,smtpname,adminemail,encoding,cabversion,mailtemplete,fileseparator,viewerexepath,spath="../rdagnet.jsp",usewaspool,os,language,userdefinecharset,fileformatyyyyMMdd,dmserver,dmserverport,dm_webcontextpath,dm_webserviceport,smsid,smspass,smssender,sessiontimeout;
   String mrdfile_abs_dir;
   boolean hangleflag = true;
   boolean dbencoding = true; 
   boolean downencoding = true;  
   RdPropertyManager rp = null;   
   static int ROWCOUNT = 20;
   static String DIRSEPARATOR ="/";
   static String ADMINGROUP ="Administrator";
   String sessionUser = null;
   String sessionGroup = null;
   String xpos = null;
   String ypos = null;
   //sjs 04.13 For saving the current status of each properties object
   String crp = null;
   String clp = null;
   String cdp = null;
   String docinfo = null;
   
   private boolean isNongHyup;
%>
<%
	try{
      rp = RdPropertyManager.getInstance();
      if(rp == null){
         RdPropertyManager.init(RDUtil.getRdPropPath());
         rp = RdPropertyManager.getInstance();
      }
      else if(rp.getProperty("server.webpath") == null){
      	//sjs 04.08
      	RdPropertyManager.decodeInstance();
      	rp = RdPropertyManager.getInstance();
      }
      servicename  = rp.getProperty("server.servicename","");
      webpath  = rp.getProperty("server.webpath","");
      mrdpath = rp.getProperty("server.mrdpath","");  
      mrdfile_abs_dir = webpath+DIRSEPARATOR+mrdpath;
      ocxhtmpath = rp.getProperty("server.ocxhtmpath",""); 
      datapath = rp.getProperty("server.datapath","");
      serverip = rp.getProperty("server.serverip","");
      usewaspool = rp.getProperty("server.usewaspool","");
      if(usewaspool == null)
         usewaspool = "false";
      else
         usewaspool = usewaspool.toLowerCase();
      os = System.getProperty("os.name");
      language = Message.getLanguage();  
      
  	  if(language == null)
		language = "korean";
	  else
		language = language.toLowerCase();
	
	  encoding = rp.getProperty("server.encoding");  
      if(encoding == null)
         encoding = "false";
      cabpath = rp.getProperty("server.cabpath");  
	  cabversion = rp.getProperty("server.cabversion");
	  if(cabversion == null) cabversion ="3,0,0,383";
	  datapath = rp.getProperty("server.datapath");  
	  smtpname = rp.getProperty("server.smtpname");  
	  fileseparator = rp.getProperty("file.separator","^@^");  
	  viewerexepath = rp.getProperty("file.viewerexepath","C:/Program Files/M2Soft/Report Designer 3.0/rdviewer.exe");  
      adminemail = rp.getProperty("server.adminemail"); 
      
      mailtemplete = rp.getProperty("server.mailtemplete");
      if(mailtemplete == null) mailtemplete = "";
         mrdfile_abs_dir = webpath+"/"+mrdpath;
      
      userdefinecharset = rp.getProperty("server.userdefinecharset");
      if(userdefinecharset == null) userdefinecharset = "EUC-KR";

      fileformatyyyyMMdd = rp.getProperty("schedule.fileformatyyyyMMdd","0");
      
      dmserver = rp.getProperty("dm.serverip","");
      dmserverport = rp.getProperty("dm.port","");
      dm_webcontextpath = rp.getProperty("dm.webcontextpath","");
      dm_webserviceport = rp.getProperty("dm.webserviceport","");  
      
      smsid = rp.getProperty("sms.id","");
      smspass = rp.getProperty("sms.pass","");
      smssender = rp.getProperty("sms.sender","");

	  docinfo = rp.getProperty("docinfo.todb","no");
     sessiontimeout = rp.getProperty("server.sessiontimeout", "3");
     
     String strNongHyup = rp.getProperty("server.nonghyup");
     if(strNongHyup != null)
     {
        if(strNongHyup.toLowerCase().equals("yes"))
           isNongHyup = true;
        else
           isNongHyup = false;
     }
     else
     {
        isNongHyup = false;
     }
      
	}catch (Exception e){
	   e.printStackTrace();
	}
	try{
		sessionGroup = BeansAuthentication.getSessionUserGroup();
		sessionUser  = BeansAuthentication.getSessionUser();
	}catch (IllegalStateException ex){
	   sessionUser = sessionGroup = null;
	   
    }
    
%>