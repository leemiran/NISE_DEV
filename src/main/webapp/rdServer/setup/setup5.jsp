<%@ page import="java.io.*,java.util.jar.*,java.util.*" contentType="text/html; charset=euc-kr" %>
<%@ include file="commonlib.h"%>
<%
/////////Step 2 - Setting RDServer Classpath,Step 3 transfer the value of directory of server_home and server_lib when invoking/////////////////////////// -->

  String tmpRDHomeDir = getServerConfigPath(out,application,request);

  String setupfilename = "/rdserversetup.config";

  File f = new File(tmpRDHomeDir + setupfilename);

  //Start in case of saving command
  if(request.getParameter("opcode") != null && request.getParameter("opcode").equals("save"))
  {

		//rd.properties setting the important key of the properties in the process of set up, other keys are set to default and save

		  String server_serverip_comment ="#Web server ip and port number";

		  String server_serverip = "server.serverip="+request.getParameter("serverip");

		  String server_adminemail_comment="#Email address of administrator";

		  String server_adminemail = "server.adminemail="+request.getParameter("adminemail");

		  String server_webpath_comment="#Default web root";

		  String server_webpath = "server.webpath="+request.getParameter("webpath");

		  String server_contextpath_comment = "#server.contextpath";

		  String server_contextpath = "server.contextpath="+request.getParameter("contextpath");

		  String server_cabpath_comment = "#Http path for downloading rdviewer.cab";

		  String server_cabpath = "server.cabpath="+request.getParameter("cabpath");

		  String file_viewerexepath_comment = "#Installed path of reviewer installed on user's pc";

		  String file_viewerexepath = "file.viewerexepath="+request.getParameter("viewerexepath");

		  String server_servicename_comment = "#Database service name from db.properties (one of service names defined in db.properties)";

		  String server_servicename = "server.servicename="+request.getParameter("servicename");

		  String server_mrdpath_comment = "#Path of saving mrd file produced by scheduling";

		  String server_mrdpath = "server.mrdpath="+request.getParameter("mrdpath");

		  String server_ocxhtmpath_comment = "#Path of saving html file to be sent to users after executing each scheduling task";

		  String server_ocxhtmpath = "server.ocxhtmpath="+request.getParameter("ocxhtmpath");

		  String server_datapath_comment = "#Path of saving data files used in scheduling";

		  String server_datapath = "server.datapath="+ request.getParameter("datapath");

		  String server_cabversion_comment = "#server.cabversion";

		  String server_cabversion = "server.cabversion="+ request.getParameter("cabversion");

		  String cab_unicode_comment = "#if cab is unicode version,set this TRUE, otherwise set it FALSE (version 5.0 or later)";

		  String cab_unicode = "cab.unicode="+ request.getParameter("unicodecab");

		  String file_separator_comment = "#Separator used when saving to data file";

		  String file_separator = "file.separator="+request.getParameter("separator");

		  String server_mailtemplete_comment  = "#server.mailtemplete";

		  String server_mailtemplete = "server.mailtemplete="+request.getParameter("mailtemplete");

		  String server_usewaspool_comment = "#If the web application server uses connection pools, set this TRUE";

		  String server_usewaspool = "server.usewaspool="+request.getParameter("usewaspool");

		  String server_language_comment = "#RDServer resource language ( korean, english )";

		  String server_language = "server.language="+request.getParameter("language");

		  String server_userdefinecharset_comment = "#Server language character set (EUC-KR, SHIFT_JIS, gb2312,8859_1 (default: iso-88591) )";

		  String server_userdefinecharset = "server.userdefinecharset="+request.getParameter("userdefinecharset");

		  String server_createtable_comment = "#Creating the management tables automatically by setting this TRUE";

		  String server_createtable = "server.createtable="+request.getParameter("createtable");

		  String server_sessiontimeout_comment = "#Session timeout, default = 3 mins";

		  String server_sessiontimeout = "server.sessiontimeout="+request.getParameter("sessiontimeout");

		  String server_showlogin_comment = "#authentication for ocx";

		  String server_showlogin = "server.showlogin="+request.getParameter("showlogin");

		  String file_xpos_comment = "#Option value for /rpxpos parameter";

		  String file_xpos = "file.xpos=0";

		  String file_ypos_comment = "#Option value for /rpypos parameter";

		  String file_ypos="file.ypos=0";

		  String schedule_fileformatyyyyMMdd_comment = "#Format of names of scheduled files ( if this set to 1, yyyyMMdd else yyyyMMddhhmmss+serial)";

		  String schedule_fileformatyyyyMMdd = "schedule.fileformatyyyyMMdd=0";

		  String sms_id_comment ="#SMS server login id (SMS alert)";

		  String sms_id = "sms.id=";

		  String sms_password_comment = "#SMS server password (SMS alert)";

		  String sms_password = "sms.password=";

		  String sms_sender_comment = "#Phone number of SMS sender";

		  String sms_sender = "sms.sender=";

		  String docinfo_todb_comment = "#Documents directory authentication save option";

		  String docinfo_todb = "docinfo.todb=no";

		  String mailer_notifydomain_comment = "#Domain name for notifying to a receiving mail server.\n#(If the receivng mail server has a spam-mail filter, this domain name is used to reply for the confirmation. eg) Naver, Hanmail, etc. )";

   	  String mailer_notifydomain = "mailer.notifydomain=";

 		  String mailer_dns_comment = "#DNS Server IP (default: 61.37.47.3(M2Soft dns server))";

		  String mailer_dns = "mailer.dns=";

		  String schedule_paramseparator_comment = "#Separator for dealing with multi-parameter in scheduling registering(default: ;)";

		  String schedule_paramseparator = "schedule.paramseparator=;";

		  String server_nonghyup_comment = "#Define RDServer is Nonghyup Version";

		  String server_nonghyup = "server.nonghyup=no";

		  String server_rddir_comment ="#RDServer directory name\n#ex) WebROOT/RDServer  --> rddir=RDServer  , WebROOT/RDSCServer  -->  rddir=RDSCServer";

   	  String server_rddir="server.rddir=RDServer";

		  String data_buffersize_comment = "#OutputStream data buffer size for reports";

		  String data_buffersize = "data.buffersize=256";


		  String server_definedPeriod_comment = "# definedPeriod for user definition scheduling";

		  String server_definedPeriod = "server.definedPeriod=";

		  String server_jsppath_comment = "# Path to the jsp file which contains data of scheduling dates for corresponding user defined period";

		  String server_jsppath = "server.jsppath=";

		  String server_exportmaxuser_comment = "# ServerSide Export maximum of user";

		  String server_exportmaxuser = "server.exportmaxuser=";

		  String proframe_category_comment = "# Proframe category, example)PROFRAME_NAME@IP:PORT";

		  String proframe_category = "proframe.category=";


//Symmetry comment array and the corresponding key values 
		  String[] comments = {server_serverip_comment,server_adminemail_comment,server_webpath_comment,server_contextpath_comment,server_cabpath_comment,file_viewerexepath_comment,server_servicename_comment,server_mrdpath_comment,server_ocxhtmpath_comment,server_datapath_comment,server_cabversion_comment,cab_unicode_comment,file_separator_comment,server_mailtemplete_comment,server_usewaspool_comment,server_language_comment,server_userdefinecharset_comment,server_createtable_comment,server_sessiontimeout_comment,server_showlogin_comment,file_xpos_comment,file_ypos_comment,schedule_fileformatyyyyMMdd_comment,sms_id_comment,sms_password_comment,sms_sender_comment,docinfo_todb_comment,mailer_notifydomain_comment,mailer_dns_comment,schedule_paramseparator_comment,server_nonghyup_comment,server_rddir_comment,data_buffersize_comment,server_definedPeriod_comment,server_jsppath_comment,server_exportmaxuser_comment,proframe_category_comment};

		  String[] keyandvalues = {server_serverip,server_adminemail,server_webpath,server_contextpath,server_cabpath,file_viewerexepath,server_servicename,server_mrdpath,server_ocxhtmpath,server_datapath,server_cabversion,cab_unicode,file_separator,server_mailtemplete,server_usewaspool,server_language,server_userdefinecharset,server_createtable,server_sessiontimeout,server_showlogin,file_xpos,file_ypos,schedule_fileformatyyyyMMdd,sms_id,sms_password,sms_sender,docinfo_todb,mailer_notifydomain,mailer_dns,schedule_paramseparator,server_nonghyup,server_rddir,data_buffersize,server_definedPeriod,server_jsppath,server_exportmaxuser,proframe_category};

		  Properties ps = new Properties();

        FileInputStream inputstream = new FileInputStream(f);

        String prp_path = "";

        boolean ret = true;
		  try
		  {
		  	  ps.load(inputstream);
		  	  prp_path = ps.getProperty("RDSERVER_PROPERTEIS_DIR",null);
		  	  if(prp_path == null || prp_path.equals(""))
		  	  {
		  	  		prp_path = tmpRDHomeDir+"/conf/rd.properties";
		  	  }
		  	  else
		  	  {
		  	      prp_path += "/rd.properties";
		  	  }

		  	   File rdprop_file = new File(prp_path);


//back up and save
				if(rdprop_file.exists())
				{
					String bakupf = prp_path+System.currentTimeMillis()+".bak";
					copyFile(prp_path,bakupf);

					//read mode
					if(!rdprop_file.canWrite()){
						rdprop_file.delete();
					}
				}
//end

		  	  PrintWriter outwriter = null;

		  	  try
		  	  {
					outwriter  = new PrintWriter(new BufferedWriter(new FileWriter(prp_path)));

			      outwriter.println("#Update Time: Reports Designer Server - rd.properties " + new java.text.SimpleDateFormat("'['yyyy-MM-dd'] 'a' 'hh:mm:ss.SS' '").format( new java.util.Date()));

					for (int i = 0; i< comments.length ; i++)
					{
						outwriter.println(comments[i]);
						outwriter.println(keyandvalues[i]);
						outwriter.println();
					}

			  }
			  catch (Exception ex)
			  {
			     out.println ("Error: <font color=red><pre>"+ getStackTraceAsString(ex));
			     ret = false;
			  }
			  finally
			  {
			  		if(outwriter != null)
			  			outwriter.close();
			  }

			  if(!ret)
			  	   return;

		  }
		  catch (FileNotFoundException ex)
		  {
		  		out.println("Error: Unable to read the property files previously defined at RDServer Setup - Step 1 "+ ex.toString());
		  		ret = false;
		  }
		  finally
		  {
		     if(inputstream != null)
		       inputstream.close();
		  }

//update the config path of rdagent.jsp 
       saveRdAgentConfigPath(tmpRDHomeDir + "/rdagent.jsp",tmpRDHomeDir);
//update RDServer URL of rdserverexport.jsp //kokim 2006.03.27
		 saveServerExportRdUrl(tmpRDHomeDir + "/rdserverexport.jsp", server_cabpath.substring(15,server_cabpath.length()));

//update RDServer URL of rdviewer.jsp //shkim 2006/07/12
		 saveServerExportRdUrl(tmpRDHomeDir + "/rdviewer.jsp", server_cabpath.substring(15,server_cabpath.length()));

//update rdserverip:port of requestform.jsp.	//kokim 2006.05.23
		 saveRequestFormRdUrl(tmpRDHomeDir + "/sample/serverexport/requestform.jsp", request.getParameter("serverip"));

        if(ret)
		  {
				pageContext.forward("setup6.jsp");

   	  }
  //end in case of saving command

  }
////finish saving and start message
  else if(request.getParameter("opcode") != null && request.getParameter("opcode").equals("complete") )
  {
  		String msg = request.getParameter("msg");

		out.println("<html>");
		out.println("<head><title>RDServer Setup - Step 5</title></head>");
		out.println("<style>");
		out.println("body, table, tr, td,select {font-family:Tahoma, µ¸¿ò, ±¼¸², verdana; font-size:9pt;color:#222222;line-height:170%}");
		out.println("TD");
		out.println("{");
		out.println("SCROLLBAR-FACE-COLOR: white;");
		out.println("FONT-SIZE: 9pt;");
		out.println("SCROLLBAR-HIGHLIGHT-COLOR: white;");
		out.println("SCROLLBAR-SHADOW-COLOR: #dfdfdf;");
		out.println("SCROLLBAR-3DLIGHT-COLOR: #dfdfdf;");
		out.println("SCROLLBAR-ARROW-COLOR: #dfdfdf;");
		out.println("SCROLLBAR-TRACK-COLOR: white;");
		out.println("FONT-FAMILY: ±¼¸²;");
		out.println("COLOR: black;");
		out.println("SCROLLBAR-DARKSHADOW-COLOR: black");
		out.println("}");

	   out.println("</style>");

		out.println("<script language=JavaScript>");
		out.println("		function callNextSetup()");
		out.println("				{");
		out.println("				   document.step5.submit();");
		out.println("				}");

		out.println("				function callPrevSetup()");
		out.println("				{");
		out.println("					document.step5.action ='setup5.jsp';");
		out.println("				   document.step5.submit();");
		out.println("				}");

		out.println("			</script>");

		out.println("			<body class=header leftmargin=20 topmargin=10 marginwidth=0 marginheight=0>");


		out.println("			   <form name=step5 method=post action=setup6.jsp>");
		out.println("			   <input type=hidden name=opcode value=next>");


		out.println("				<table>");
		out.println("				<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>");
		out.println("				<tr>");
		out.println("				<td bgcolor=#33CCCC  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Step 5</td>");
		out.println("				<tr>");
		out.println("				<td bgcolor=#33FFFF  colspan=2 align=center>RDServer Environment Setup</td>");
		out.println("				<tr>");
		out.println("				<td bgcolor=#3399CC  height=2 colspan=2></td>");
		out.println("				<tr>");
		out.println("</table>");

		out.println("<br>");
		out.println("<table>");
		out.println("<td align=right>");
		out.println("<a href=\"javascript:callPrevSetup();\"><img src=\"images/prev_up.gif\" border=0 hspace=10></a><a href=\"javascript:callNextSetup();\"><img src=\"images/next_up.gif\" border=0></a>");
		out.println("</td>");
		out.println("<tr>");
		out.println("<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>");
		out.println("<tr>");
		out.println("</table>");

		out.println("<br>");
		out.println("<table>");
		out.println("<td width=605>");
  		out.println("<font size=4><b>Settings are saved <br> "+msg +" </font><br>");
		out.println("</td>");
		out.println("</table>");
		out.println("<br>");
		out.println("<br>");

		out.println("<table>");
		out.println("<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>");
		out.println("<tr>");
		out.println("<td align=right>");
		out.println("<a href=\"javascript:callPrevSetup();\"><img src=\"images/prev_up.gif\" border=0 hspace=10></a><a href=\"javascript:callNextSetup();\"><img src=\"images/next_up.gif\" border=0></a>");
	   out.println("</td>");

		out.println("</table>");

      return;
////finish saving and end of message screen
  }
%>
<html>
<head><title>RDServer Setup - Step 5</title></head>
	<style>
	<%@ include file="setup.css" %>
	</style>

<!-- ////////////////Start script-->
	<script language="JavaScript">
		function callNextSetup()
		{
		   document.step5.submit();
		}

		function callPrevSetup()
		{
			document.step5.action ='setup4.jsp';
		   document.step5.submit();
		}

	function OnClickServiceList()
	{
		document.step5.servicename.value = document.step5.servicelist.value;
	}


	</script>

<!-- ////////////////End script-->
	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">


	   <form name="step5" method="post" action="setup5.jsp">
	   <input type="hidden" name="opcode" value="save">


 <!-- ///////////////STEP HEADER -->
		<table>
		<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Step 5</td>
		<tr>
		<td bgcolor=#DBDBEA  colspan=2 align=center>RDServer Environment Setup (rd)</td>
		<tr>
		<td bgcolor=#3399CC  height=2 colspan=2></td>
		<tr>
		</table>

<!-- /////////////STEP HEADER END -->

<br>

<!--///////////////Navigation Button Begining--->
		<table>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
		</table>
<!--///////////////Navigation Button End--->

<!-- ////////////setting rd.properties -->

	<%
	String file="",db="";	file = "checked";
	//String serveripport = java.net.InetAddress.getLocalHost().getHostAddress()+":"+request.getServerPort();
	String serveripport = "localhost";

   //It usually occur the Exception when retrieving localhost ip in case of host name is Hangeul, plz set ip as localhost sunhee 2005.07.18
	try {
		serveripport = java.net.InetAddress.getLocalHost().getHostAddress();
   }
   catch (Exception ex) {
   }

   serveripport += ":"+request.getServerPort();

	String serverhome = getServerConfigPath(out,application,request);
	String default_rdproperties = serverhome +"/conf/rd.properties";
	String default_dbproperties = serverhome +"/conf/db.properties";
	String default_cab_version = null;
	String default_cab_unicode = "FALSE";
	String default_language = "korean";
	String default_userdefinecharset = "EUC-KR";
	String default_viewerexepath = "C:/Program Files/M2Soft/Report Designer 5.0/rdviewer.exe";
	Properties prop = new Properties();
	Vector v_serviceList = new Vector();

	FileInputStream in = null;
	try {
		  in = new FileInputStream(default_rdproperties);
		  prop.load(in);
		  default_cab_version = prop.getProperty("server.cabversion");
		  default_cab_unicode = prop.getProperty("cab.unicode");
		  default_language = prop.getProperty("server.language");
		  default_userdefinecharset =  prop.getProperty("server.userdefinecharset");
		  default_viewerexepath = prop.getProperty("file.viewerexepath");
   }
   catch (Exception ex) {
   }
   finally
   {
   	if(in != null)
   		try {
   			in.close();
		   }
		   catch (Exception ex) {
		   }
   }

	try
	{
		in = new FileInputStream(default_dbproperties);
		prop.load(in);

		String urlname;
		int inx;

	   for (Enumeration e = prop.propertyNames() ; e.hasMoreElements() ;) {
	      urlname = (String)e.nextElement();
	      inx = urlname.indexOf(".url");
	      if(inx != -1)
	         v_serviceList.add(urlname.substring(0,inx));
     	}

   }
   catch (Exception ex) {
   }
   finally
   {
   	if(in != null)
   		try {
   			in.close();
		   }
		   catch (Exception ex) {
		   }
   }


	%>
	<table>
	<tr>
	<td width=600 bgcolor=#EFDFFF colspan=2>
		#Web server ip and port number
	</td>
	<tr>
	<td width=200>server.serverip</td>
	<td width=400><input type ="text" name="serverip" size=76 value="<%=serveripport%>" class="style2"></td>
	<tr>
	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Email address of administrator
	</td>
	<tr>
	<td width=200>server.adminemail</td>
	<td width=400><input type ="text" name="adminemail" size=76 value="administrator@mymail.com" class="style2"></td>
	<tr>

	<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>
	#RDServer resource language ( korean, english )
	</td>
	<tr>
	<td width=200>server.language</td>
	<td width=400><input type ="text" name="language" size=76 value="<%=default_language%>" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Server resource language character set (EUC-KR, SHIFT_JIS, UTF-8,gb2312,8859_1 (default: iso-88591) )
	</td>
	<tr>
	<td width=200>server.userdefinecharset</td>
	<td width=400><input type ="text" name="userdefinecharset" size=76 value="<%=default_userdefinecharset%>" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Report session timeout, default = 3 mins
	</td>
	<tr>
	<td width=200>server.sessiontimeout</td>
	<td width=400><input type ="text" name="sessiontimeout" size=76 value="3" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Authentication for ocx ( 0 or 1)
	</td>
	<tr>
	<td width=200>server.showlogin</td>
	<td width=400><input type ="text" name="showlogin" size=76 value="0" class="style2"></td>
	<tr>


	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Path default web root home (rdserver parent directory path)
	</td>
	<tr>
	<td width=200>server.webpath</td>
	<td width=400><input type ="text" name="webpath" size=76 value="<%=new File(serverhome).getParent().replace('\\','/')%>" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Context Path
	</td>
	<tr>
	<td width=200>server.contextpath</td>
	<td width=400><input type ="text" name="contextpath" size=76 value="/" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Http path for downloading rdviewer.cab (use scheduling file)
	</td>
	<tr>
	<td width=200>server.cabpath</td>
	<td width=400><input type ="text" name="cabpath" size=76 value="http://<%=serveripport%><%=request.getContextPath()%><%=request.getServletPath().substring(0,  request.getServletPath().indexOf("/setup/setup5.jsp"))%>" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#rdviewer.cab version
	</td>
	<tr>
	<td width=200>server.cabversion</td>
	<%
		if(default_cab_version == null || default_cab_version.equals(""))
			out.println("<td width=400><input type=text name=\"cabversion\" size=76 value=\"5,0,0,112\" class=\"style2\"></td>");
		else
		   out.println("<td width=400><input type=text name=\"cabversion\" size=76 value=\""+default_cab_version+"\" class=\"style2\"></td>");
	%>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#if cab is unicode version,set this TRUE, otherwise set it FALSE (version 5.0 or later)
	</td>
	<tr>
	<td width=200>cab.unicode</td>
	<td width=400><input type ="text" name="unicodecab" size=76 value="<%=default_cab_unicode%>" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Installed path of reviewer installed on server's pc (use only os : windows)
	</td>
	<tr>
	<td width=200>file.viewerexepath</td>
	<td width=400><input type ="text" name="viewerexepath" size=76 value="<%=default_viewerexepath%>" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Database service name for scheduling (one of service names defined in db.properties - STEP4)
	</td>
	<tr>
	<td width=200>server.servicename</td>
<!--	<td width=400><input type ="text" name="servicename" size=30 value="" class="style2">	-->
	<%

	   if(v_serviceList.size() > 0)
	   {
			out.println("<td width=400><input type =\"text\" name=\"servicename\" size=30 value=\""+(String)v_serviceList.get(0)+"\" class=\"style2\">");
	      out.println("<select name=\"servicelist\" onClick=\"OnClickServiceList()\">");
	      for (int i = 0; i< v_serviceList.size(); i++)
	         	out.println ("<option value=\""+(String)v_serviceList.get(i) +"\">"+(String)v_serviceList.get(i)+"</option>");
	      out.println("</select>");
	   }
		else
		{
			out.println("<td width=400><input type =\"text\" name=\"servicename\" size=30 value=\"\" class=\"style2\">");
		}
   %>

	</td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#If the web application server uses connection pools, set this TRUE
	</td>
	<tr>
	<td width=200>server.usewaspool</td>
	<td width=400><input type ="text" name="usewaspool" size=76 value="FALSE" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Creating the management tables automatically by setting this TRUE (or FALSE)
	</td>
	<tr>
	<td width=200>server.createtable</td>
	<td width=400><input type ="text" name="createtable" size=76 value="TRUE" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Relative path of saving mrd file produced by scheduling (server.webpath)
	</td>
	<tr>
	<td width=200>server.mrdpath</td>
	<td width=400><input type ="text" name="mrdpath" size=76 value="<%=(new File(serverhome)).getName()%>/reports" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Relative path of saving data files used in scheduling (server.webpath)
	</td>
	<tr>
	<td width=200>server.datapath</td>
	<td width=400><input type ="text" name="datapath" size=76 value="<%=(new File(serverhome)).getName()%>/reports/data" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Relative path of saving html file to be sent to users after executing each scheduling task (server.webpath)
	</td>
	<tr>
	<td width=200>server.ocxhtmpath</td>
	<td width=400><input type ="text" name="ocxhtmpath" size=76 value="<%=(new File(serverhome)).getName()%>/ocxhtm" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#Separator used when saving to scheduling data file
	</td>
	<tr>
	<td width=200>file.separator</td>
	<td width=400><input type ="text" name="separator" size=76 value="^@^" class="style2"></td>
	<tr>

	<td width=600 bgcolor=#EFDFFF colspan=2>
	#server.mailtemplete
	</td>
	<tr>
	<td width=200>server.mailtemplete</td>
	<td width=400><input type ="text" name="mailtemplete" size=76 value="<%=getServerConfigPath(out,application,request)%>/ocxhtm/mailtemplate.html" class="style2"></td>
	<tr>
	</table>

<!-- ////////////end rd.properties setting  -->

<!--///////////////Navigation Button Begining--->
		<table>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
	   </td>

		</table>
<!--///////////////Navigation Button End--->

   </form>
	</body>
</html>