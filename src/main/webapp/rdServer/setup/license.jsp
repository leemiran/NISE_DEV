<%@ page import="java.io.*,java.util.jar.*" contentType="text/html; charset=euc-kr" %>
<%@ include file="commonlib.h"%>
<html>
   <head><title>RDServer Setup</title></head>
	<style>
	<%@ include file="setup.css" %>
	</style>

	<body>
	<%
		String serverHome = getServerConfigPath(out,application,request);
		String licensePath = serverHome+"/license.txt";

		File licenseFile = new File(licensePath);

		if(licenseFile.exists())
		{
			BufferedReader br = new BufferedReader(new FileReader(licensePath));

			String temp = "";
			while((temp = br.readLine()) != null)
			{
				out.println(temp+"<br>");
			}
		}
		else
		{
			%>
			<script>alert("Error reported: unable to find the license agreement file");</script>
			<%
		}
	%>
	</body>
</html>