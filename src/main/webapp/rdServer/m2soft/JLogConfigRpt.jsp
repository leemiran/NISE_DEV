<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message "%>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="logproperties.h"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<%
   //sjs 04.14 get the current status of rd encryption
   clp = CurrentEncStatus.getLogEncStatus();
   CurrentEncStatus.setCurrentLogTab("reportlog");

   initArg(request,out);
   CInfo[] pools = RdMs.getJDBCStatus();

   String url = m_param.GetStrValue("opcode");

   if(url != null && url.equals("up") ) {

      p.put("log.moniteringlog",m_param.GetStrValue("islog"));
      logTrue = m_param.GetStrValue("islog");

      p.put("log.moniteringlogLevel",m_param.GetStrValue("level"));
      mlogLevel = m_param.GetIntValue("level");

      p.put("log.refreshMileTime",m_param.GetStrValue("refreshtime"));
      rMilisecond = m_param.GetIntValue("refreshtime");
      if(mlogLevel != 0){
         p.put("log.moniteringlogServiceName",m_param.GetStrValue("jdbcservicename"));
         logServiceName = m_param.GetStrValue("jdbcservicename");
      }

      //sjs 04.10 also pass the encrypt variable
      p.saveToFile(RDUtil.getLogPropPath(), clp);
      out.println("<body bgcolor="+m_sBtnFace+">");
      out.println("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
   }

   String uselog = "checked",deuselog="";
   if(logTrue != null && logTrue.equals("false")){
      deuselog="checked";
      uselog="";
   }

   String f="checked",d="";
   if(mlogLevel == 1){
      d="checked";
      f="";
   }

%>
<style>
<%@ include file="addschedule.css" %>
</style>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<script>
  function onup()
  {
      var frm = document.logconfig;
      frm.opcode.value='up';
      frm.submit();
  }
  function enableCheckServiceList(v)
  {
      var frm = document.logconfig;
      if(v == 2){
         if(frm.jdbcservicename != null)
            frm.jdbcservicename.disabled=true;
      }
      else{
         if(frm.jdbcservicename != null)
            frm.jdbcservicename.disabled=false;
      }

  }
</script>

<form method='POST' name='logconfig' action='JLogConfigRpt.jsp'>
<table border=0 cellspacing=1>
<td><%= Message.get("jlogconfigrpt_01")%></td>
<td>
<input type='radio' name='islog' value='true' <%=uselog%> ><%= Message.get("jlogconfigrpt_02")%>
<input type='radio' name='islog' value='false'<%=deuselog%> ><%= Message.get("jlogconfigrpt_03")%>
</td><tr>
<td colspan=2><hr></td><tr>
<td><%= Message.get("jlogconfigrpt_04")%></td>
<td>
<input type=text name='refreshtime' value='<%=rMilisecond%>' size=14 class='style2'>&nbsp;<%= Message.get("jlogconfigrpt_05")%>
</td><tr>
<td colspan=2><hr></td><tr>
<td><%= Message.get("jlogconfigrpt_06")%></td>
<td>
<input type='radio' name='level' value='0' <%=f%> onClick="enableCheckServiceList(2)">File
<input type='radio' name='level' value='1' <%=d%> onClick="enableCheckServiceList(1)">Database
</td><tr><tr>
<td colspan=2><hr></td><tr>
<td heght=30><%= Message.get("jlogconfigrpt_07")%></td><td>

<% if(pools != null && pools.length > 0){
      int i;
      if(d.equals("checked"))
         debugPrint("<select name='jdbcservicename'>");
      else
         debugPrint("<select name='jdbcservicename' disabled>");
      for(i=0;i<pools.length;i++){
         if(logServiceName.equals(pools[i].get1))
            debugPrint("<option value='"+pools[i].get1+"' selected>"+pools[i].get1+"</option>");
         else
            debugPrint("<option value='"+pools[i].get1+"'>"+pools[i].get1+"</option>");
      }
      debugPrint("</select>");
   }
%>
</td><tr>
<td>&nbsp;</td><td><font color=darkyellow><%= Message.get("jlogconfigrpt_08")%></font></td><tr>
<td colspan=2><hr></td><tr>
</table>

<%
   {
      JLButton btnadd = new JLButton();
      btnadd.printButton(223,320,"ref3",105,3,Message.get("jlogconfigrpt_09"),"onup()","images/edit.gif",20);
   }
%>
<input type='hidden' name='opcode' value='li'>
</form>
