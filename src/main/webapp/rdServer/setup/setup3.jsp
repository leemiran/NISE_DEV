<%@ page import="java.io.*,java.util.jar.*" contentType="text/html; charset=euc-kr" %>
<%@ include file="commonlib.h"%>
<%!
  String server_home = "";
  String server_lib = "";
  String server_config_filepath = "";
%><%
/////////Step 2 - Write the key of RDSERVER_HOME and RDSEVER_LIB into the RDServer/rdserversetup.config, install by unzip class file or copy lib-->

   server_home = getServerConfigPath(out,application,request);
   server_lib = request.getParameter("server_lib");

  if(server_home == null || server_lib == null || server_home.equals("") || server_lib.equals(""))
  	   return;
   server_config_filepath = server_home +"/rdserversetup.config";


%>
<html>
   <head><title>RDServer Setup - Step 3</title></head>
	<style>
	<%@ include file="setup.css" %>
	</style>

<!-- ////////////////Start Script -->
	<script language="JavaScript">
		function CheckDir()
		{
		   var serverdir = document.step2.server_lib.value;
		   window.open("setup_checkdir.jsp?server_dir="+ serverdir +"&chktype=step2", "NewWindow",'Width=300,Height=50,left=300,top=400,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0');
		}

		function callNextSetup()
		{
		   document.step3.submit();
		}

		function callPrevSetup()
		{
			document.step3.action ='setup2.jsp';
		   document.step3.submit();
		}

	</script>

<!-- ////////////////End Script-->
	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">
	   <form name="step3" method="post" action="setup4.jsp">


 <!-- ///////////////STEP HEADER -->
		<table>
		<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Step 3</td>
		<tr>
		<td bgcolor=#DBDBEA  colspan=2 align=center>Copy RDServer libraries to the WAS CLASSPATH and Save the Home Directory</td>
		<tr>
		<td bgcolor=#3399CC  height=2 colspan=2></td>
		<tr>
		</table>
		<br>

<!--///////////////STEP HEADER END -->

<!--///////////////Navigation Button Begining--->
		<table>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
		</table>
<!--///////////////Navigation Button END --->

<%
  File f = new File(server_home);
  if(!f.exists())
  {
      if(server_home.equals("null")) server_home = "";
  		out.println( "RDServer Home: <font color=red>\""+ server_home +"\" directory does not exist. Please confirm <br><br> <a href=setup1.jsp?server_home="+server_home+"><img src=images/prev_up.gif border=0 hspace=0></a></font>");
  		return;
  }

  File lf = new File(server_lib);
  if(!lf.exists())
  {
  		out.println("CLASSPATH: <br><font color=red>\""+ server_lib +"\" directory does not exist. Please confirm <br><br> <a href=setup2.jsp?server_lib="+server_lib +"><img src=images/prev_up.gif border=0 hspace=0></a></font>");
  		return;
  }

  File conf_file = new File(server_config_filepath);

  boolean error = false;
  if(!conf_file.exists())
  {
  		FileOutputStream con_out = null;
      try
      {
      	con_out = new FileOutputStream(conf_file);
	   }
	   catch (Exception ex)
	   {
	   	out.println(conf_file.toString() + "error occurred while saving the information...<pre>" + getStackTraceAsString(ex));
	   	error = true;
	   }
	   finally
	   {
	   	if(con_out != null)
	   		con_out.close();
	   }
  }

  if(error)
  	 return;

////////////////Check 2: Start to save location of RDSERVER HOME and CONF into Environment Configration File/////////////////////////////

  BufferedWriter fout = null;

	try
	{

		File ff = new File(server_config_filepath);
		if(ff.exists())
		{
			copyFile(server_config_filepath,server_config_filepath+System.currentTimeMillis()+".bak");

			if(!ff.canWrite())
				ff.delete();
		}


		if(server_lib != null)
  			server_lib = server_lib.replace('\\','/');

		if(server_home != null)
  			server_home = server_home.replace('\\','/');

		/// write the directory into rdserversetup.config 
		fout = new BufferedWriter(new FileWriter(conf_file));

      fout.write("#Update Time: Reports Designer Server - rdserversetup.config " + new java.text.SimpleDateFormat("'['yyyy-MM-dd'] 'a' ' hh:mm:ss.SS' '").format( new java.util.Date()));
      fout.newLine();

      fout.write("# Setup jsp: "+application.getRealPath(request.getServletPath()));
      fout.newLine();
		fout.write("# RDServer Location( servlet -> only absolute path , daemon -> absolute or relative path )");
		fout.newLine();
		fout.write("RDSERVER_DIR="+server_home);
		fout.newLine();
		fout.newLine();

		fout.write("# RDServer jar package and JDBC Location( servlet -> only absolute path , daemon -> absolute or relative path )");
		fout.newLine();
		fout.write("RDSERVER_LIB_DIR="+server_lib);
		fout.newLine();
		fout.newLine();

		fout.write("# RDServer properties Location( servlet -> only absolute path , daemon -> absolute or relative path )");
		fout.newLine();
		fout.write("# If you do not insert a value for this key, properties are being read from the RDSERVER_DIR+/conf as default. Otherwise, it is read from the path specified by the key.");
		fout.newLine();
		fout.write("RDSERVER_PROPERTIES_DIR="+server_home+"/conf");
		fout.newLine();
		fout.newLine();

		fout.write("# RDServer logs Location( servlet -> only absolute path , daemon -> absolute or relative path )");
		fout.newLine();
		fout.write("RDSERVER_LOG_DIR="+server_home+"/log");

		fout.flush();
		fout.close();

	}
	catch(Exception e)
	{
		out.println("<li><font color=red size=+1><b>Error: Error reported while saving the RDServer setup information.</b></li><br>" + getStackTraceAsString(e) +"</font>");
		error = true;
	}
	finally
	{
		try
		{
			if( fout != null )
				fout.close();
		} catch (Exception ce) {}
	}

////////////////check 2: END/////////////////////////////
	if(error)
		return;

	out.println ("<table>" );
	out.println ("<td width = 605>" );
	out.println ("<li><font size=+1><b>Successfully saved the RDServer setup configurations</b></font></li><br>" );
	out.println ("</td>" );
	out.println ("</table>" );

%>
	<table>
		<td width=605>
			<ul type="circle">
			<font face="Tahoma">
			<li>Location of RDServer Setup Configuration File</li>
			<li><%=server_config_filepath%></li>
			</font>
			</ul>
		</td>
	</table>


<!--//////////////Start to input text directory and check screen-->
		<table width=605>
		<tr>
		<td bgcolor=#DDDDDD>Contents of rdserversetup.config</td>
		<tr>
		<td bgcolor=#EEEEEE width=605><%
			BufferedReader in  = null;
			try {
				in = new BufferedReader(new FileReader(conf_file));
				String content;

				while ((content = in.readLine()) != null)
				{
				   if(content.indexOf("#") != -1)
				   	out.println ("<font face=\"Tahoma\">"+content+"</font><br>");
				   else
						out.println("<font color=blue face=\"Tahoma\">"+content+"</font><br>");
				}
		   }
		   catch (Exception ex)
		   {
		   	out.println("Error: <pre>" + getStackTraceAsString(ex));
		   }
		   finally
		   {
		   	if(in != null)
		   		in.close();
		   }
		%></td>
		</table>
<!--//////////////END -->
<br>

<%
////////////////Check 3: Begin to copy RD Class into WAS Classpath/////////////////////////////

	try
	{
      File [] rdserverJar = {new File(server_home+"/bin/rdserver.jar"),new File(server_home+"/bin/rdmaster.jar"),new File(server_home+"/lib/enc.jar")};

	   boolean ret = copyRDServerClass(rdserverJar,server_lib);

	   if(ret)
		{
			out.println ("<table>" );
			out.println ("<td width = 605>" );
	   	out.println ("<li><font size=+1><b>Successfully copied the RDServer libraries</b></li></font><br>" );
			out.println ("</td>" );
			out.println ("</table>" );
		}

		String windir = getWindowSystemDir();
		if(windir != null)
			ret = copyFile(server_home+"/bin/rdprn5.dll",windir+"rdprn5.dll");
   }
   catch (Exception ex)
   {
   	out.println("<li><font color=red size=+1><b>Error: Error reported while copying RDServer class files.</b></li><br>" + getStackTraceAsString(ex));
   	out.println("Copy "+server_home + "/bin/rdserver.jar and <br>"+  server_home + "/bin/rdmaster.jar to "+ server_lib +"</font>");
		error = true;
   }

////////////////Check 3: END/////////////////////////////
%>

	<table>
		<td width=605>
			<ul type="circle">
			<font face="Tahoma">
			<li>RDServer CLASSPATH</li>
			<li><%=server_lib%></li>
			</font>
			</ul>
		</td>
	</table>


<!--///////////////Navigation Button Begining--->
		<table>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
		   </td>
		</table>
<!--///////////////Navigation Button End--->

	</body>
</html>