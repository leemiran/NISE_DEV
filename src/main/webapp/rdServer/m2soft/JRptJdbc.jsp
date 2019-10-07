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

<%@ include file="../control/lib/JLTabCtrl.java"%>
<%@ include file="../control/lib/JSTabCtrl.jsp"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%!
   //sjs 04.14
   private String status = null;
%>

<%
	 initArg(request,out);

   //sjs 04.14 get the current status of db encryption
   if (CurrentEncStatus.getDbEncStatus() == null)
      status = request.getParameter("dbEncStatus");
   else if (request.getParameter("encStatus") == null)
      status = CurrentEncStatus.getDbEncStatus();
   else
      status = request.getParameter("encStatus");

   CurrentEncStatus.setDbEncStatus(status);
   status = CurrentEncStatus.getDbEncStatus();

%>
<style>
<%@ include file="icis.css" %>
</style>
<script>
	function onadd()
	{
		alert('onadd');
	}
	function ondel()
	{
		alert('ondel');
	}
   function hide(){
      window.status =' ';
   }
   function encrypt() {

      var frm = document.encForm;
      var status = "<%=status%>";

      if(status == "true"){
         frm.action="JRptJdbc.jsp?encStatus=false";
      }
      else {
         frm.action="JRptJdbc.jsp?encStatus=true";
      }
      frm.submit();
   }
function onstart(url,l, t, w, h) {
   var windowprops = "location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no" +",left=" + l + ",top=" + t + ",width=" + w + ",height=" + h;
   popup = window.open(url,"MenuPopup",windowprops);
}


</script>
<style><%@ include file="addschedule.css" %></style>
</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0" onLoad='hide();'>
<!--sjs 04.13-->
<form name="encForm" action="" method="post">
<div align="left"><br>
   <input type="checkbox" name="enc" OnClick="javascript:encrypt();" <%if ( status.equals("true")) out.println("Checked");%>><font class="style4"><%out.println(Message.get("envfile_30"));%></font><br>
</div>
<br>
<%
   JLTabCtrl tabCtrl = new JLTabCtrl(out);
   tabCtrl.init("jdbctab",800,350);

   CInfo[] pools = RdMs.getJDBCStatus();
   Vector poolnames;
   String gname;
   String paramstr;
   StringBuffer strbuf;
   if(pools != null)
   {
      poolnames = new Vector(pools.length);
         for(int i=0;i<pools.length;i++){
            strbuf = new StringBuffer();
            gname=pools[i].get1+"group";
            poolnames.addElement(gname);
            strbuf.append("JRptJdbcPools.jsp?url=");
            if(pools[i].get2 != null)
               strbuf.append(java.net.URLEncoder.encode(pools[i].get2));
            strbuf.append("&user=");
            if(pools[i].get3 != null)
               strbuf.append(java.net.URLEncoder.encode(pools[i].get3));
            strbuf.append("&password=");
            if(pools[i].get4 != null)
               strbuf.append(java.net.URLEncoder.encode(pools[i].get4));
            strbuf.append("&maxconn=");
            strbuf.append(pools[i].get5);
            strbuf.append("&freeconn=");
            strbuf.append(pools[i].get6);
            strbuf.append("&curconn=");
            strbuf.append(pools[i].get7);
            strbuf.append("&poolname=");
            strbuf.append(pools[i].get1);
            tabCtrl.addTab(gname,90,15,1,pools[i].get1,"","images/icon_db.gif",strbuf.toString());
            strbuf=null;
         }
         //sjs 04.14 For setting the current tab
         if (CurrentEncStatus.getCurrentDbTab() != null)
            tabCtrl.setInitialPage(CurrentEncStatus.getCurrentDbTab());
         tabCtrl.printTabs();
////////////////
  %>
  <script>
  <%
  if(poolnames.size() > 0)
  {

	  out.println("JLTabCtrl_focusPage(\"jdbctab\",\""+(String)poolnames.elementAt(0) +"\");");

	  out.println("function focusPage(idx)");
	  out.println("{");

		     int size = poolnames.size();
		     for(int i=0;i<size;i++){
		        out.println("if (idx == "+new Integer(i).toString()+")");
				  out.println("JLTabCtrl_focusPage(\"jdbctab\",\""+(String)poolnames.elementAt(i)+"\");");
		     }

	  out.println("}");
	}
	%>


   </script>
<%
  }else
  {
     out.println("<img src=images/arrow_right.gif hspace=10>" + Message.get("jrptmoniter_01") + "<img src=images/ok.gif> Not loading");
  }
%>
</body>
