<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message, m2soft.rdsystem.server.core.rddbagent.role.*"%>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%!
	private XmlPropertyManager xp = null;

   public void jspInit()
   {
		// kokim 2004.10.27 for NongHyup
      xp = XmlPropertyManager.getInstance();
      if(xp==null)
      {
         try{
            XmlPropertyManager.init(RDUtil.getXmlPropPath());
         }catch(Exception e){
         }
         xp = XmlPropertyManager.getInstance();
      }
   }

	public void jspDestroy()
   {
   }
%>
<%@ page session="false" %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>

<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>

<%@ include file="../control/lib/JLTabCtrl.java"%>
<%@ include file="../control/lib/JSTabCtrl.jsp"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);
	String url1 = m_param.GetStrValue("listUrl");
	String url2 = m_param.GetStrValue("serviceUrl");
	String opcode = m_param.GetStrValue("opcode");
	String servicename = m_param.GetStrValue("serviceName");

   if(opcode != null)
	{
		if(opcode.equals("up"))
		{
			xp.put(servicename + ".list_url", url1);
			xp.put(servicename + ".service_url", url2);
		}
		else if(opcode.equals("del"))
		{
			xp.remove(servicename + ".list_url");
			xp.remove(servicename + ".service_url");
		}
		xp.saveToFile(RDUtil.getXmlPropPath(), "false");
		debugPrint("<body bgcolor="+m_sBtnFace+" text=#000000 leftmargin=15 topmargin=10 marginwidth=0 marginheight=0>");
		debugPrint("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
	}

%>
<style>
<%@ include file="icis.css" %>
</style>
<script>
function onup(v) {
 var frm = document.xmldetail;
 frm.opcode.value='up';
 frm.submit();
}
function ondel() {
 var frm = document.xmldetail;
 frm.opcode.value='del';
 frm.submit();
}

function hide(){
  window.status=" ";
}

</script>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0" onload='hide();'>
<%
   int btnX = 190;
   JLButton btnadd = new JLButton();
   btnadd.printButton(btnX,240,"ref2",105,3,Message.get("jrptjdbcpools_01"),"onup()","images/edit.gif",20);
   btnadd.printButton(btnX+110,240,"ref3",105,3,Message.get("jrptjdbcpools_02"),"ondel()","images/delete.gif",20);

%>
<form method='POST' name='xmldetail' action='JRptXmlProperty.jsp'>
<table>
<td>List URL</td><td><input type=text name='listUrl' value='<%=url1%>' size=70 class='style2'></td><tr>
<td>Service URL</td><td><input type=text name='serviceUrl' value='<%=url2%>' size=70 class='style2'></td><tr>
<input type=hidden name='serviceName' value='<%=servicename%>'>
<input type=hidden name='opcode'>
</table>
</form>
</body>
