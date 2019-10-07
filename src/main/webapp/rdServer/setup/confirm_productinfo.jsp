<%@ page contentType="text/html; charset=euc-kr" %> 
<%@ page language="java" import="java.io.*, m2soft.rdsystem.server.master.properties.*, m2soft.rdsystem.server.*" %>
<%!
	public String writeMsg(String msg)
	{
		String tmp="";
		tmp = "<li>" + msg + "</li>";
		return tmp;
	}

	public String writeErrMsg(String msg)
	{
		String tmp="";
		tmp = "<li><font color=RED>" + msg + "</font></li>";
		return tmp;
	}
%>

<html>
	<style>
	<%@ include file="setup.css" %>
	</style>

	<script language="JavaScript">
		function callPrevSetup()
		{
			document.confirm_productinfo.action ='confirm_dbconnect.jsp';
		   document.confirm_productinfo.submit();
		}
	</script>

	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">
		<form name="confirm_productinfo" method="post" action="">

		<table>
		<td bgcolor=#813E00  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#FFDC74  width=550 height=30><b>Report Designer5.0 Setup Confirmation</td><td bgcolor=#FF8A19 width=50 align=center><font color=#ffffff> Step 4</td>
		<tr>
		<td bgcolor=#FFEFBF  colspan=2 align=center>RDServer Product Information</td>
		<tr>
		<td bgcolor=#813E00  height=2 colspan=2></td>
		<tr>
		</table>
		<br>

		<table>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a>
			</td>
			<tr>
			<td bgcolor=#813E00  width=605 height=2 colspan=2></td>
			<tr>
		</table>

	<%
		try
		{
			ServerInfoManagerImpl simi = new ServerInfoManagerImpl();
			ServerInformation info = simi.getServerInformation();

			// Version
			out.println(writeMsg("Server version : " + info.getServerVersion()));

			// License
			String license = "";
			if(info.getLicenseUser() == 0)
				license += "Server license : "+ info.getCustomerName() +", Unlimited";
			else
				license += "Server license : "+ info.getCustomerName() +"," + info.getLicenseUser() + " Users";

			if(info.isLiteVersion())
				license += " (Lite) ";

			if(info.isUnicodeVersion())
				license += ", Unicode Version";

			out.println(writeMsg(license));

			// Port
			// out.println(writeMsg("Running port : "+ info.getServerServicePort()));

			// Last Version Date
			// out.println(writeMsg("Last Version Date : "+info.getPackagingDate()));

			// OS
			out.println(writeMsg("O.S :  "+info.getOsName()+" (Ver "+info.getOsVersion() +")"));
		}
		catch(Exception e)
		{
			out.println(writeErrMsg("Error reported : " + e.getMessage()));
		}
	%>

		<table>
			<td bgcolor=#813E00  width=605 height=2 colspan=2></td>
			<tr>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a>
			</td>
			<tr>
		</table>

	</body>
</html>