<%@ page import="java.util.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.role.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.log.m.*" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JLToolBar.java"%>
<%@ include file="../control/lib/JSMenuBar.jsp"%>
<%@ include file="../control/lib/JLMenuBar.java"%>
<style>
      <%@ include file="icis.css" %>
</style>

<%

	initArg(request,out);
   if(!RdMs.getRptServerStatus()){
      out.println("<body bgcolor=#d4d0c8 text=#000000 leftmargin=15 topmargin=10 marginwidth=0 marginheight=0>");
      out.println("<img src=images/arrow_right.gif hspace=10>" + Message.get("jrptmoniter_01") + "<img src=images/ok.gif> Not loading");
      return;
   }

%>
<script>
   var isIE=document.all?true:false;
   var isNS4=document.layers?true:false;
   var isNS6=!isIE&&document.getElementById?true:false;
   var _w = '';
   if (isNS4) _w = '<br><layer id=ones name=ones top=130><table border=0><td height=20 bgcolor=red><font color=white><%=Message.get("Security17")%></td></table></layer>';
   if (isIE||isNS6) _w = '<br><div id=ones name=ones top=130><table border=0><td height=20 bgcolor=red><font color=white><%=Message.get("Security17")%></td></table></div>';
   document.write(_w);

   function replaceIt(_v)
   {

      var _n=_v;
      if (isNS4)
      {
         document.ones.document.open();
         document.ones.document.write(_n);
         document.ones.document.close();
      }
      if (isIE)
      {
         document.all.ones.innerHTML='<table border=0><td height=20 bgcolor=red><font color=white>'+_n+'</td></table>';
      }

      if(isNS6)
      {
         document.getElementById("ones").innerHTML='<table border=0><td height=20 bgcolor=red><font color=white>'+_n+'</td></table>';
      }
   }

   function openTarget(url,menu_name)
   {
      document.all.body.src= url;
      replaceIt(menu_name);
   }
</script>
</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<%
      JLToolBar toolBar = new JLToolBar(out);
      toolBar.init("securitytoolbar");
      toolBar.setPosition(0,0,-1,-1,-1,-1);
      {
         JLMenuBar menuBar = new JLMenuBar(out);
         int nHeight=30;
         menuBar.init("mbMain",-1,25);

         //menuBar.addMenu("mService",100,Message.get("Security16"),"window.openTarget('JSecurityService.jsp','"+ Message.get("Security16")+ "');","images/up_left.gif","ab1234",0,true); //차후 서비스 예정
         menuBar.addMenu("mUser",80,Message.get("Security17"),"window.openTarget('JSecurityGroup.jsp','"+Message.get("Security17") + "');","images/up_left.gif","ab1234",0,true);
         menuBar.addMenu("mGroup",80,Message.get("Security18"),"window.openTarget('JSecurityUser.jsp','"+Message.get("Security18") + "');","images/up_left.gif","ab1234",0,true);
         menuBar.addMenu("mLog",160,Message.get("Security19"),"window.openTarget('JSecurityLoginInfo.jsp','"+ Message.get("Security19") + "');","images/up_left.gif","ab1234",0,true);
         toolBar.addBar("mbMain",menuBar,-1,-1,-1,20);
      }
      toolBar.printObj(16,20);

%>
<iframe frameborder="0" height="370" scrolling="no" src="JSecurityGroup.jsp" width="800" name="body" STYLE="border-style:solid;border-width:1;"  bgcolor="<%=m_sBtnFace%>"></iframe><br>
</body>
</html>

