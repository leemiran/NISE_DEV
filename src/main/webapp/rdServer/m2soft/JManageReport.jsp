<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message "%>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>

<%@ include file="../control/lib/JSContextMenu.jsp"%>

<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);
%>


<script>
	function onadd()
	{
		alert('onadd');
	}
	function ondel()
	{
		alert('ondel');
	}
</script>

<body bgcolor="#FFFFFF" SCROLL ="no" text="#000000" leftmargin="10" topmargin="15" marginwidth="0" marginheight="0">
<%=Message.get("jmanagereport_01")%>
            <iframe src="JRptList.jsp" width="780" height="464" frameborder=1 scrolling=auto></iframe>



<%
	{
		JLButton btnadd = new JLButton();
		btnadd.printButton(790-(220),520,"ref2",105,3,Message.get("jmanagereport_02"),"onadd()","images/button_icon_add.gif",20);
		btnadd.printButton(790-(106),520,"ref4",105,3,Message.get("jmanagereport_03"),"ondel()","images/button_icon_del.gif",20);
	}
%>

</body>
