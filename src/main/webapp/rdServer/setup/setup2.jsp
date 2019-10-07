<%@ page import="java.io.*,java.util.jar.*" contentType="text/html; charset=euc-kr" %>
<%@ include file="commonlib.h"%>
<%
/////////Step 2 - Set RDServer Classpath, Step 3 transfer the value of directory when invoking server_lib/////////////////////////// -->

  //1)Read RD Home Directory
  String server_home = getServerConfigPath(out,application,request);

  //application.getRealPath("/")" --> docBase
  String docBase = application.getRealPath("/");
  if(docBase.endsWith("/") || docBase.endsWith("\\"))
     docBase = docBase.substring(0, docBase.length() - 1);

  if(new File(docBase+"/WEB-INF").exists() && (!new File(docBase+"/WEB-INF/lib").exists()))
     new File(docBase+"/WEB-INF/lib").mkdirs();

  //2)Search location of the basic classpath 
  String tmp_webinf_lib = docBase+"/WEB-INF/lib";

  String tmp_webinf_classes = docBase+"/WEB-INF/classes";

  String tmp_web_lib = "";

  //3)Search lib and classes. Order: 1. the location of setting lib in before, 2.lib, 3.classes
  if(new File(tmp_webinf_lib).exists() && new File(tmp_webinf_lib).isDirectory())
  		tmp_web_lib = tmp_webinf_lib;
  else
  {
	  if (new File(tmp_webinf_classes).exists() && new File(tmp_webinf_classes).isDirectory())
	  		tmp_web_lib = tmp_webinf_classes;
  }

  if(tmp_web_lib != null)
  		tmp_web_lib = tmp_web_lib.replace('\\','/');


%>
<html>
<head><title>RDServer Setup - Step 2</title></head>
	<style>
	<%@ include file="setup.css" %>
	</style>

<!-- ////////////////Begining Script-->
	<script language="JavaScript">
		function CheckDir()
		{
		   var serverdir = document.step2.server_lib.value;
		   window.open("setup_checkdir.jsp?server_dir="+ serverdir +"&chktype=step2", "NewWindow",'Width=300,Height=50,left=300,top=400,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0');
		}

		function callNextSetup()
		{
			if(!document.step2.server_lib.value){
				alert(document.step2.errmsg.value);
				 document.step2.server_lib.focus();
				return;
			}
		   document.step2.submit();
		}

		function callPrevSetup()
		{
			document.step2.action ='setup1.jsp';
		   document.step2.submit();
		}

	</script>

<!-- ////////////////Script End-->
	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">


	   <form name="step2" method="post" action="setup3.jsp">
	   <input type="hidden" name="errmsg" value="Enter the directory path where RDServer classes are to be located">


 <!-- ///////////////STEP HEADER-->
		<table>
		<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Step 2</td>
		<tr>
		<td bgcolor=#DBDBEA  colspan=2 align=center>Configure RDServer CLASSPATH</td>
		<tr>
		<td bgcolor=#3399CC  height=2 colspan=2></td>
		<tr>
		</table>
		<br>

<!--///////////////STEP HEADER END -->

<!--///////////////Begin Navigation Button--->
		<table>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
		</table>
<!--///////////////Navigation Button End--->


<!--//////////////Input the text directory and begin to check screen-->
		<table width=605 border=0>
		<%

		  if(!new File(tmp_web_lib).exists())
		  {
		      String waskind = application.getServerInfo().toLowerCase();
		      String example_lib_path = "C:/tomcat/common/lib";
		      if(waskind.indexOf("jeus") != -1)
		         example_lib_path = "jeus-version/lib/application/";
            else if(waskind.indexOf("websphere") != -1)
               example_lib_path = "Websphere/Appserver/lib";
		      else if(waskind.indexOf("resin") != -1)
		         example_lib_path = "resin-version/common/lib/";
            else if(waskind.indexOf("oracle9i") != -1)
               example_lib_path = "OC4J_HOME/j2ee/home/lib";

		      out.println("<td colspan=2>Enter the CLASSPATH (Directory) which is recognized by the Web Application Server, then click on \"OK\" <br><br> --Reference--<br>Input eg)"+ example_lib_path + tmp_web_lib+"</td>");

				String prev_setting_lib = request.getParameter("server_lib");

				if(prev_setting_lib != null && !prev_setting_lib.equals(""))
					tmp_web_lib = prev_setting_lib;
				else
					tmp_web_lib = "";
		  }
		  else
		  {
		      out.println("<td colspan=2><br><li>The CLASSPATH is found as belows..</li><br><br><li>The CLASSPATH settings may differ on the type of WAS, hence adjust the path according to the application deployment policy if needed..</li> <br><br><li>Click on <font color=\"BLUE\">Next</font>, if it is right. Otherwise enter directly to the field. Click on <font color=\"BLUE\">OK</font> to confirm..</li><br><br> --	Result	--</td>");
		  }
		%>
		<tr>
		<td valign=center><input type ="text" name="server_lib" size=100 value="<%=tmp_web_lib%>" class="style2"></td><td><a href="javascript:CheckDir();"><img src="images/enter_up.gif" border=0 valign="bottom" align="right"></td>
		</table>
<!--//////////////END -->

		<br>
		<br>

<!--///////////////Navigation Button Begining--->
		<table>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
			<td  align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
		</table>
<!--///////////////Navigation Button END--->

   </form>
	</body>
</html>