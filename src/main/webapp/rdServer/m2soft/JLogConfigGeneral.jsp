<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File, m2soft.rdsystem.server.log.l.*,m2soft.rdsystem.server.core.install.Message " %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>

<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JLFrame.java"%>
<%@ include file="../control/lib/JLMultiFrame.java"%>

<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
   //sjs 04.14 get the current status of rd encryption
	clp = CurrentEncStatus.getLogEncStatus();
   CurrentEncStatus.setCurrentLogTab("generallog");

	String maxfilenum="", intervalofsweeper="";
   LogPropertyManager p = LogPropertyManager.getInstance();
   if(p.getProperty("log.errordirection") == null){
      //sjs 04.10
      LogPropertyManager.decodeInstance();
      p = LogPropertyManager.getInstance();
   }

   initArg(request,out);

   String url = m_param.GetStrValue("opcode");

   if(url != null && url.equals("up") ) {
      p.setProperty("log.maxfilenum",m_param.GetStrValue("maxfilenum"));
      p.setProperty("log.intervalofsweeper",m_param.GetStrValue("intervalofsweeper"));
      //sjs 04.10 also pass the encrypt variable
      p.saveToFile(RDUtil.getLogPropPath(), clp);
      out.println("<body bgcolor="+m_sBtnFace+">");
      out.println("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
   }

   maxfilenum = p.getProperty("log.maxfilenum","");
   intervalofsweeper = p.getProperty("log.intervalofsweeper","");
%>

<style>
<%@ include file="addschedule.css" %>
</style>

<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<script>
  function onup()
  {
      var frm = document.generallogconfig;
      frm.opcode.value='up';
      frm.submit();
  }
</script>

<form method='POST' name='generallogconfig' action='JLogConfigGeneral.jsp'>
<table border=0 cellspacing=1 >

<td><%= Message.get("jlogconfigrpt_15")%></td>
<td>
<input type=text name='maxfilenum' value='<%=maxfilenum%>' size=14 class='style2'>
</td><tr>
<td colspan=2><hr></td><tr>
<td><%= Message.get("jlogconfigrpt_16")%></td>
<td>
<input type=text name='intervalofsweeper' value='<%=intervalofsweeper%>' size=14 class='style2'>
</td><tr>
<td colspan=2><hr></td><tr>

</table>

<%
   {
      JLButton btnadd = new JLButton();
      btnadd.printButton(223,290,"ref3",105,3,Message.get("jlogconfigrpt_09"),"onup()","images/edit.gif",20);
   }
%>
<input type='hidden' name='opcode' value='li'>
</form>
