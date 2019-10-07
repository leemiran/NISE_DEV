<%@ page import="java.util.*,m2soft.rdsystem.server.core.rddbagent.beans.*"%>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page session="false" %>
<%@ include file="session.h"%>
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
	initArg(request,out);
   String filename = request.getParameter("targetpath");
   String strfilename = null;
	String filedesc = null;
   int idx = filename.lastIndexOf("/");
   strfilename = filename.substring(idx+1);

	// 파일에서 관리하는 경우
	if(docinfo.equals("no"))
	{
		BeansFileDescription fd = new BeansFileDescription();
		filedesc = fd.getdesc(strfilename);
	}
	// DB에서 관리하는 경우
	else
	{
		FileDescription fd = new FileDescription();
		filedesc = fd.getdesc(strfilename, servicename);
	}

%>
<html>
    <title><%= Message.get("JFileDescription_02") %></title>
    <style><%@ include file="addschedule.css" %></style>

<script language="JavaScript">
         function set_mrdfilename(X){
            parent.document.scform.schedulefile.value = X;
         }

         function submitUp() {
            var frm = document.upform;
            var filepath,ext;

            filepath = document.setmessage.mrdfileupload_03.value;

            var param = "?filedesc="+frm.filedesc.value + "&filepath="+document.setmessage.mrdfileupload_03.value;
            frm.action="JFileDescMod.jsp"+param;
            frm.submit();
       }

      </script>

  </head>

<%
     debugPrint(Message.get("JFileDescription_03"));
%>

	<form name="setmessage">
		<input type="hidden" name="mrdfileupload_02" value=<%= Message.get("JFileDescription_03") %> >
		<input type="hidden" name="mrdfileupload_03" value=<%= filename %> >
	</form>

   <form name="upform" method="post" action="JFileDescMod.jsp" encType="multipart/form-data">
   <table border=0 cellpadding=0 cellspacing=3 align=center>
      <tr>
      	<td>
      	<%= Message.get("mrdfilelist_11") %>
      	</td>
      </tr>
      <tr>
      	<td>
      	<input type="text" name="filedesc" size=80 class="style1" value="<%= filedesc %>">
      	</td>
      </tr>
    </table>
   </form>

<%
{

   int btnX = 400;
   int btnY = 125;
   JLButton btn = new JLButton();
   btn.printButton(btnX,btnY,"btnupload",105,3,Message.get("JFileDescription_04"),"submitUp()","images/button_icon_arrowup.gif",20);

}
%>
</body>
</html>