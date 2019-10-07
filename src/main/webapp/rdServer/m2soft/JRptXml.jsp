<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message, m2soft.rdsystem.server.core.rddbagent.role.*, m2soft.rdsystem.server.core.rddbagent.util.*"%>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%!
	private String listUrl, serviceUrl;
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
%>
<style>
<%@ include file="icis.css" %>
</style>
<script>
   function hide(){
      window.status =' ';
   }

</script>
</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0" onLoad='hide();'>
<%
   JLTabCtrl tabCtrl = new JLTabCtrl(out);
   tabCtrl.init("xmltab",800,370);
	
	String[] services = NHServiceNTables.getServiceNames();

	for(int i=0; i<services.length; i++)
	{

		tabCtrl.addTab(services[i],90,15,1,services[i],"","images/icon_db.gif", "JRptXmlProperty.jsp?serviceName="+services[i]+"&listUrl="+xp.getProperty(services[i] + ".list_url") + "&serviceUrl="+xp.getProperty(services[i] + ".service_url"));
	}
	tabCtrl.printTabs();
%>
  <script>
  JLTabCtrl_focusPage("xmltab","<%=services[0]%>");

	function focusPage(idx)
	{
		<%
		  int size = services.length;
		  for(int i=0;i<size;i++){
			  out.println("if (idx == "+new Integer(i).toString()+")");
			  out.println("JLTabCtrl_focusPage(\"xmltab\",\""+services[i]+"\");");
		  }
		%>
	}

   </script>
</body>
