<%@page import="java.lang.*,java.io.*,java.util.*,m2soft.rdsystem.server.core.install.Message" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);
%>

<STYLE> BODY {border: 0px black solid;border-top: none;} </STYLE>
<body bgcolor="#FFFFFF" SCROLL ="no" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<script>
   JLCaptionFrame_printObj(window,"mainfolder","<%=Message.get("jmainfolder_01")%>","JMainTree.jsp");

	window.onresize = JLCaptionFrame_setSize;
	JLCaptionFrame_setSize();
</script>


</body>
