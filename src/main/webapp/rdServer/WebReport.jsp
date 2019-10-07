<%@ page
   	contentType="text/html; charset=8859_1"
   	import="java.util.*, java.io.*, java.net.*" 
%>


<%
		String strSrc = request.getParameter("src");
  	String strParam = request.getParameter("param");
  	String tmpParam = "";
  	
  	if ((strParam == null) || (strParam.equals("")) || (strParam.equals("null"))) {
        	strParam = " ";
    } else {
     			tmpParam = " /rf [http://192.168.10.6/rd/RDServer/rdagent.jsp] /rsn [rds] /rp ";
     			String strout = ":"; 		// replace this
     			String temp = strParam; 	// temporary holder
     			int pos = 0;
     			int intLen = 0;
     			while (temp.indexOf(strout)>-1) {
        				pos  = temp.indexOf(strout);
        				intLen = temp.length();
        				tmpParam = tmpParam + " [" + (temp.substring(0, pos)) + "]";
        				temp = temp.substring((pos + 1), intLen);
        	}
  				tmpParam = tmpParam + " [" + temp + "]";
     			strParam = tmpParam;
    }
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<title>엠투소프트</title>
</head>

<script language="JavaScript">
<!--
		function loadForm()	{
				login.action = "WebReport_DS.jsp";
				login.submit();
		}
-->
</script> 

<body oncontextmenu="return false" onselectstart="return false" ondragstart="return false" onload="JavaScript:loadForm()" toolbar=no fullscreen=no menubar=no location=no scrollbar=no leftmargin=0 topmargin=0>


<form name="login" action="WebReport_DS.jsp" method="post">
		<input type="hidden" name="strSrc" value="<%= strSrc %>">
		<input type="hidden" name="strParam" value="<%= strParam %>">
</form>

</body>
</html>
