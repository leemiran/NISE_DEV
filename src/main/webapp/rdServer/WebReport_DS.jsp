<%@ page
   	contentType="text/html; charset=8859_1"
%>

<%
    	String strSrc = request.getParameter("strSrc");
    	String strParam = request.getParameter("strParam");
    
    	int type = 2; //type 은 0 ~ 10 까지 
    	strSrc = new String(enc.C.process(strSrc,type));
    	strParam = new String(enc.C.process(strParam,type));
%>

<HTML>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<title></title>
</head>

<body onload="JavaScript:loadForm()" toolbar=no fullscreen=no menubar=no location=no scrollbar=no leftmargin=0 topmargin=0 oncontextmenu="return false" onselectstart="return false" ondragstart="return false">

<script src="rdviewer.js"></script>

<script language="JavaScript">
<!--
		function loadForm() {
    		Rdviewer.SetParameterEncrypt(1);
     		Rdviewer.SetKindOfParam(<%=type%>);
     		Rdviewer.AutoAdjust = false;
     		Rdviewer.ZoomRatio = 100;
     		Rdviewer.SetBackgroundColor(255,255,255); 		
     		Rdviewer.FileOpen("<%= strSrc %>", "<%= strParam %>");	
    }
-->
</script>
 
</BODY>
</HTML>
