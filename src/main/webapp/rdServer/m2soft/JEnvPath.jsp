<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File ,m2soft.rdsystem.server.core.install.Message" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>

<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JLMenuBar.java"%>
<%@ include file="../control/lib/JLToolBar.java"%>
<%@ include file="../control/lib/JLSplitter.java"%>
<%@ include file="../control/lib/JLFrame.java"%>
<%@ include file="../control/lib/JLMultiFrame.java"%>
<%@ include file="../control/lib/JSContextMenu.jsp"%>
<%@ include file="../control/lib/JLContextMenu.java"%>
<%@ include file="../control/lib/JLPopupMenu.java"%>

<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);

%>
<%!
   String paramenvfilename=null;
   String fpath,envfilename;
%>
<%
   //sjs 04.14 get the current status of rd encryption
   crp = CurrentEncStatus.getRdEncStatus();
   CurrentEncStatus.setCurrentRdTab("pathgroup");
   
   envfilename = fpath = RDUtil.getRdPropPath();
   paramenvfilename =  m_param.GetStrValue("envfilename");

   if(paramenvfilename != null){
      rp.put("server.webpath",m_param.GetStrValue("webpath"));
      rp.put("server.cabpath",m_param.GetStrValue("cabpath"));
      if(os.toLowerCase().indexOf("window") != -1){
         rp.put("file.viewerexepath",m_param.GetStrValue("viewerexepath"));
      }
      rp.put("server.mrdpath",m_param.GetStrValue("mrdpath"));
      rp.put("server.ocxhtmpath",m_param.GetStrValue("ocxhtmpath"));
//      rp.put("server.logpath",m_param.GetStrValue("logpath"));
      rp.put("server.mrdpath",m_param.GetStrValue("mrdpath"));
      rp.put("server.datapath",m_param.GetStrValue("datapath"));
      rp.put("server.mailtemplete",m_param.GetStrValue("mailtemplete"));
      rp.put("server.cabversion",m_param.GetStrValue("cabversion"));

      //sjs 04.10 also pass the encrypt variable                
      rp.saveToFile(RDUtil.getRdPropPath(), crp);

      out.println("<body bgcolor="+m_sBtnFace+">");
      out.println("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
      debugPrint("<script>window.location='JEnvPath.jsp';</script>");
      return;
   }
%>


<html><head><title><%= Message.get("envfile_02") %></title>
<style><%@ include file="addschedule.css" %></style>
<script language="JavaScript">

function submitUp() {

   var frm = document.upform;
   var filepath,ext;

	if(frm.mrdpath.value == ""){
		alert(document.setmessage.envfile_03.value);
		frm.mrdpath.focus();
		return;
	}

	if(frm.datapath.value == ""){
		alert(document.setmessage.envfile_04.value);
		frm.datapath.focus();
		return;
	}

	if(frm.cabpath.value == ""){
		alert(document.setmessage.envfile_05.value);
		frm.cabpath.focus();
		return;
	}

	if(frm.ocxhtmpath.value == ""){
		alert(document.setmessage.envfile_06.value);
		frm.ocxhtmpath.focus();
		return;
	}

/*
	if(frm.logpath.value == ""){
		alert(document.setmessage.envfile_11.value);
		frm.logpath.focus();
		return;
	}
*/
	frm.action="JEnvPath.jsp";
	frm.submit();

}
</script>
</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">

<form name="setmessage">
	<input type="hidden" name="envfile_03" value=<%= Message.get("envfile_03") %> >
	<input type="hidden" name="envfile_04" value=<%= Message.get("envfile_04") %> >
	<input type="hidden" name="envfile_05" value=<%= Message.get("envfile_05") %> >
	<input type="hidden" name="envfile_06" value=<%= Message.get("envfile_06") %> >
	<input type="hidden" name="envfile_07" value=<%= Message.get("envfile_07") %> >
	<input type="hidden" name="envfile_08" value=<%= Message.get("envfile_08") %> >
	<input type="hidden" name="envfile_09" value=<%= Message.get("envfile_09") %> >
	<input type="hidden" name="envfile_10" value=<%= Message.get("envfile_10") %> >
	<input type="hidden" name="envfile_11" value=<%= Message.get("envfile_11") %> >
</form>
<form name="upform" method="post" action="JEnvPath.jsp">

<table border=0 cellpadding=0 cellspacing=1 >
<tr>
<td>
<INPUT type="hidden" name="envfilename" class="style4" readonly value="<%=RDUtil.getRdPropPath()%>" size=60><br>
</td>
<tr>
	<td colspan=2>
		<img src="images/button16.gif" hsapce=10><%= Message.get("envfile_14") %>
	</td>
<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name="webpath" size=80 class="style2" value="<%=webpath%>">
	</td>
<tr>
	<td colspan=2>
		<img src="images/button16.gif" hsapce=10><%= Message.get("envfile_15") %>
	</td>
<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name="mrdpath" size=80 class="style2" value="<%=mrdpath%>">
	</td>
<tr>
	<td colspan=2>
		<img src="images/button16.gif" hsapce=10><%= Message.get("envfile_16") %>
	</td>
<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name=datapath size=80 class="style2" value="<%=datapath%>">
	</td>
<tr>
	<td colspan=2>
     <img src="images/button16.gif" hsapce=10><%= Message.get("envfile_17") %>
	</td>
<tr>
	<td colspan=2>
      <img src="images/blank.gif" width=20><input type=text name=cabpath size=69 class="style2" value="<%=cabpath%>">&nbsp;<input type=text name=cabversion size=9 class="style2" value="<%=cabversion%>">
	</td>
<tr>
	<td colspan=2>
		<img src="images/button16.gif" hsapce=10><%= Message.get("envfile_18") %>
	</td>
<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name=ocxhtmpath size=80 class="style2" value="<%=ocxhtmpath%>">
	</td>

<tr>
<!--
	<td colspan=2>
		<img src="images/button16.gif" hsapce=10><%= Message.get("envfile_23") %><br>
	</td>
<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name=logpath size=80 class="style2" value="<%=logpath%>">
	</td>
-->
<% if(os.toLowerCase().indexOf("window") != -1){ %>
<tr>
   <td colspan=2>
     <img src="images/button16.gif" hsapce=10><%= Message.get("envfile_27") %><br>
      <img src="images/blank.gif" width=20><input type=text name=viewerexepath size=80 class="style2" value="<%=viewerexepath%>">
   </td>
</tr>
<% } %>

<tr>
   <td colspan=2>
      <img src="images/button16.gif"><%= Message.get("envfile_28") %><br>
      <img src="images/blank.gif" width=20><input type=text name=mailtemplete size=80 class="style2" value="<%=mailtemplete%>">
   </td>
<tr>

</table>
<%
   {
      JLButton btnadd = new JLButton();
      btnadd.printButton(35,420,"envup",105,3,Message.get("envfile_29"),"javascript:submitUp();","images/edit.gif",20);
   }
%>

</form>
</body>
</html>
