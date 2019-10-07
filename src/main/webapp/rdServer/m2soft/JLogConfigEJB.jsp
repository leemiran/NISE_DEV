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
   CurrentEncStatus.setCurrentLogTab("ejblog");

	String ejb_jndiname="", ejb_methodname="";
   LogPropertyManager p = LogPropertyManager.getInstance();
   if(p.getProperty("log.errordirection") == null){
      //sjs 04.10
      LogPropertyManager.decodeInstance();
      p = LogPropertyManager.getInstance();
   }

   initArg(request,out);

   String url = m_param.GetStrValue("opcode");

   if(url != null && url.equals("up") ) {
      p.setProperty("log.ejb_jndiname",m_param.GetStrValue("ejb_jndiname"));
      p.setProperty("log.ejb_methodname",m_param.GetStrValue("ejb_methodname"));
      //sjs 04.10 also pass the encrypt variable
      p.saveToFile(RDUtil.getLogPropPath(), clp);
      out.println("<body bgcolor="+m_sBtnFace+">");
      out.println("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
   }

   ejb_jndiname = p.getProperty("log.ejb_jndiname","");
   ejb_methodname = p.getProperty("log.ejb_methodname","");
%>

<style>
<%@ include file="addschedule.css" %>
</style>

<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<script>
  function onup()
  {
      var frm = document.ejblogconfig;
      frm.opcode.value='up';
      frm.submit();
  }
</script>

<form method='POST' name='ejblogconfig' action='JLogConfigEJB.jsp'>
<table border=0 cellspacing=1 >

<td><%= Message.get("jlogconfigrpt_19")%></td>
<td>
<input type=text name='ejb_jndiname' value='<%=ejb_jndiname%>' size=14 class='style2'>
</td><tr>
<td colspan=2><hr></td><tr>
<td><%= Message.get("jlogconfigrpt_20")%></td>
<td>
<input type=text name='ejb_methodname' value='<%=ejb_methodname%>' size=14 class='style2'>
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
