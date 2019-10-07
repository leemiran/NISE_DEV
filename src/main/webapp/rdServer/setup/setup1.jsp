<%@ page import="java.io.*,java.util.jar.*" contentType="text/html; charset=euc-kr" %>
<%@ include file="commonlib.h"%>
<html>
   <head><title>RDServer Setup - Step 1</title></head>
	<style>
	<%@ include file="setup.css" %>
	</style>
<!-- ////////////////Start script -->
	<script language="JavaScript">
		function callNextSetup()
		{
			if(!document.step1.server_home.value){
				alert(document.step1.errmsg.value);
				document.step1.server_home.focus();
				return;
			}

		   document.step1.submit();
		}
	</script>

<!-- ////////////////End script -->
	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">

	   <form name="step1" method="post" action="setup2.jsp">
	   <input type="hidden" name="errmsg" value="Enter the RDServer home directory">
	   <input type="hidden" name="chktype" value="step1">

 <!-- ///////////////STEP HEADER -->
		<table>
		<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Step 1</td>
		<tr>
		<td bgcolor=#DBDBEA  colspan=2 align=center>Confirm RDServer Home Directory</td>
		<tr>
		<td bgcolor=#3399CC  height=2 colspan=2></td>
		<tr>
		</table>
		<br>

<%
   String tmpRDHomeDir = "";
   String prev_setting_home = request.getParameter("server_home");
   if(prev_setting_home != null)
   {
   	if(prev_setting_home.equals("null"))
   		prev_setting_home = "";
   	tmpRDHomeDir = prev_setting_home;
   }
   else
		tmpRDHomeDir = getServerConfigPath(out,application,request);

	String directory_search_msg = "RDServer home directory is found as belows..";
	if(tmpRDHomeDir.indexOf("RDServer") == -1)
		directory_search_msg = "Unable to find the RDServer home directory.";
%>
<!--///////////////STEP HEADER END -->

<!--///////////////Navigation Button Beginning--->
		<table>
			<td align=right>
			<a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
		</table>
<!--///////////////Navigation Button End--->

<!--//////////////input the text directory and begin to check screen -->
		<table border=0 width=605>
			<td colspan=2><br><li><%=directory_search_msg%></li><br><br><li>Click on <font color="BLUE">Next</font>, if it is right.. </li><br><br> --	Result	--</td>
			<tr>
			<td valign=center><input type ="text" name="server_home" size=100 value="<%=tmpRDHomeDir%>" class="style2"></td>
			<tr>
			<td colspan=2><br><li>If the found directory is incorrect, install RDServer manually by referring the <u>Installation Guide</u> provided in RDServer/doc..</li></td>
		</table>
<!--//////////////nput the text directory and stop to check screen  -->

		<br>
		<br>

<!--///////////////Navigation Button Beginning--->
		<table>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
			<td align=right>
			<a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
		</table>
<!--///////////////Navigation Button End --->

   </form>
	</body>
</html>