<%@ page import="java.io.*,java.util.jar.*" contentType="text/html; charset=euc-kr" %>
<%@ include file="commonlib.h"%>
<html>
   <head><title>RDServer Setup</title></head>
	<style>
	<%@ include file="setup.css" %>
	</style>

	<script language="JavaScript">
		function gotoNext()
		{
		   document.step_agreement.submit();
		}
		function showAlert()
		{
			alert("You must accept the terms in order to continue..");
		}
	</script>

	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">

	   <form name="step_agreement" method="post" action="setup1.jsp">

		<table>
		<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> License</td>
		<tr>
		<td bgcolor=#DBDBEA  colspan=2 align=center>RDServer License Agreement</td>
		<tr>
		<td bgcolor=#3399CC  height=2 colspan=2></td>
		<tr>
		</table>
		<br>

		<table>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
		</table>

		<iframe src="license.jsp" width="610" height="250" frameborder="1" scrolling="yes">
		</iframe>
		<table>
		<tr>
			<td width=605>
				Do you accept all the terms of the preceding License Agreement?<br> 
				You must accept the agreement to install RDServer.<br>
			</td>
		</tr>
		<tr>
			<td width=605 align='right'>
				<input type="button" name="yes" width="100" value="Yes" class="buttonstyle1" onClick="gotoNext()">
				<input type="button" name="no" width="100" value="No" class="buttonstyle1" onClick="showAlert()">
			</td>
		</tr>	
		</table>

		<table>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
		</table>

   	</form>
	</body>
</html>