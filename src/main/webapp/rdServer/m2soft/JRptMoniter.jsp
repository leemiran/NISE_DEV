<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message"%>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>

<%@ page session="false" %>
<%@ include file="session.h"%>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>


<%@ include file="../control/lib/JSTreeCtrl.jsp"%>
<%@ include file="../control/lib/JLTreeCtrl.java"%>

<%@ include file="../m2soft/JLCurUserTree.java"%>

<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<style>
<%@ include file="icis.css" %>
</style>
<%
	initArg(request,out);
%>
<script>
function onstart(url,l, t, w, h) {
   var windowprops = "location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no" +",left=" + l + ",top=" + t + ",width=" + w + ",height=" + h;
   popup = window.open(url,"MenuPopup",windowprops);
}
</script>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="0" marginwidth="0" marginheight="0">
<%
   if(RdMs.getRptServerStatus()){
      debugPrint("<img src=images/arrow_right.gif hspace=10>" + Message.get("jrptmoniter_01") + "<img src=images/ok.gif> Running <br>");
   }
   else{
      debugPrint("<img src=images/arrow_right.gif hspace=10>" + Message.get("jrptmoniter_01") + "<img src=images/ok.gif> Not Running  <br>");
      JLButton btnadd = new JLButton();
      btnadd.printButton(745-(350),21,"ref2",105,3,"Start&nbsp;","onstart('JRptStart.jsp',0,0,1,1)","images/ok.gif",20);
   }

   String info = "<font color=blue>";

   if(RdMs.getLimitUser() == 0)
      info += "<img src=images/arrow_right.gif hspace=10>" + Message.get("jrptmoniter_02") +  L.getUserName() + " Unlimited ";
   else
   	info += "<img src=images/arrow_right.gif hspace=10>" + Message.get("jrptmoniter_02") + L.getUserName() + " "+ RdMs.getLimitUser();


   if(L.isLiteVersion())
   	info += ", Server Lite Version";

   if(L.isUnicodeVersion())
   	info += ", Server Unicode Version";

      info += "</font> <br>";

	debugPrint(info);

   debugPrint("<img src=images/arrow_right.gif hspace=10>" + Message.get("jrptmoniter_03") + RdMs.getCurUser()+"<br>" );
   if(RdMs.getCurUser() > 0 ){
      Vector v= RdMs.getCurUserInfo();
      debugPrint("<table border=0><td width=30>&nbsp;</td><td width=150>");
      JLCurUserTree treeDoc = new JLCurUserTree(out,v);
   	treeDoc.setCodeHeader("");
      treeDoc.init("cUser");

%>
<div id="elMyTree" style="background-color:#ffffff ; POSITION: absolute; width:550px; height:300px; overflow:auto;">
      <script>
      	var <%="root0"%> = JLTreeCtrl_setRoot("cUser",
      		0,		// id of root
      		null,		// 
      		"Clients",	// label
      		"images/grp.gif", // shrink icon
      		"images/grp.gif", // expand icon
      		0,			// 1 : expand state , 0 : shrink state
      		1,			// each tree assigned a fixed id. this can be designated by developer.
      		"onclicknode('root')",	// click node, action happens
      		1,0);			// 
      					   // if there does not have the child node, download them from server.
      	 //alert("tree name : " + JLTreeCtrl_getTreeNameByIdx(1));
      </script>
<%
   	treeDoc.printTree("root0","0");
   	debugPrint("</td></table>");
	}
   if(RdMs.isDemo())
     debugPrint("<img src=images/arrow_right.gif hspace=10>Demo Version:<font color=red>"+RdMs.getDemoTerm()+"</font>" );
%>
</div>

