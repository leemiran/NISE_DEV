<%@ page contentType="text/html; charset=euc-kr" %>

<html>
	<style>
	<%@ include file="setup.css" %>
	</style>

	<%
	String waskind = getServletConfig().getServletContext().getServerInfo().toLowerCase();
	out.println("WAS Information: " + waskind);

	if(waskind.indexOf("apachejserv") != -1)
	{
	   out.println ("<br><font color=red>You must install RDServer manually by referring the Installation Guide provided in RDServer/doc..</font>");
		out.println ("<table>");
		out.println ("	<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>");
	   out.println ("<tr>");
		out.println ("<td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Main</td>");
		out.println ("<tr>");
		out.println ("</table>");


	   out.println ("<table>");
		out.println ("<tr>");
		out.println ("<td height=2 colspan=3><h2 align=center><i><font color=#94B6EF>Report Designer Server Setup Wizard</font></i></h2></td>");
		out.println ("<tr>");
		out.println ("<tr>");
		out.println ("<td width=96 height=2></td>");
		out.println ("<td width=250 bgcolor=#3399CC height=2></td>");
      out.println ("<td width=96 height=2></td>");
		out.println ("<tr>");
		out.println ("<tr>");
	   out.println ("<td width=96 height=2></td>");
		out.println ("<td align=center bgcolor=#3399CF width=250><img src=images/login.gif border=0></td>");
		out.println ("<td width=96 height=2></td>");
		out.println ("<tr>");
		out.println ("<tr>");
		out.println ("<td width=96 height=2></td>");
		out.println ("<td width=250 bgcolor=#3399CC height=2></td>");
		out.println ("<td width=96 height=2></td>");
		out.println ("<tr>");
		out.println ("<tr>");
		out.println ("<td width=45 height=2></td>");
		out.println ("<td width=400 align=left><font size=-2>Ref) If you can't see the image, copy image files to the web server folder</font></td>");
		out.println ("<td width=45 height=2></td>");
		out.println ("<tr>");
		out.println ("</table>");

	   return;
	}

	%>
	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">
		<table>
		<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Main</td>
		<tr>
		<td bgcolor=#DBDBEA  colspan=2 align=center>Starting to install RDServer. Click on "Install RDServer" from the left.</td>
		<tr>
		<td bgcolor=#3399CC  height=2 colspan=2></td>
		<tr>
		</table>
		<br>


		<table>
			<tr>
				<td height=2 colspan=3><h2 align="center"><i><font color=#94B6EF>Report Designer Server Setup Wizard</font></i></h2></td>
			<tr>
			<tr>
			    <td width=96 height=2></td>
				<td width=250 bgcolor=#3399CC height=2></td>
			    <td width=96 height=2></td>
			<tr>
			<tr>
			    <td width=96 height=2></td>
				<td align="center" bgcolor=#3399CF width=250><img src="images/login.gif" border=0></td>
			    <td width=96 height=2></td>
			<tr>
			<tr>
				<td width=96 height=2></td>
				<td width=250 bgcolor=#3399CC height=2></td>
				<td width=96 height=2></td>
			<tr>

			<tr>
				<td width=45 height=2></td>
				<td width=400 align=left><font size=-2>Ref) If you can't see the image, copy image files to the web server folder</font></td>
				<td width=45 height=2></td>
			<tr>

		</table>
	</body>
</html>