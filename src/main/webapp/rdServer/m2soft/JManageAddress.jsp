<%@page import="java.util.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.rddbagent.beans.*,m2soft.rdsystem.server.core.install.Message" %>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>

<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);
%>
<%!
   final static int LIST=1;
   final static int ADD=2;
   final static int MODIFY=3;
   final static int DELETE=4;
   final static int SCADD=5;
%>
<%

	String list = request.getParameter("list");
	String opcode = request.getParameter("opcode");
	String param = "";
	int status;

	if(opcode == null || opcode.equals("")) {
		status = LIST;
	}
	else
		status = Integer.parseInt(opcode);


	if((list == null) || list.equals(""))
	   list = "undefined";
%>
   <jsp:useBean id="scUser" class="m2soft.rdsystem.server.core.rddbagent.beans.BeansScuser" />
   <jsp:setProperty name="scUser" property="servicename" value="<%=servicename%>" />
   <jsp:setProperty name="scUser" property="logpath" value="<%=logpath%>" />
<!--  ";" is delimeter of deletion, if the ";" is contained in the ID, it is not allowed to delete, in another words, it is not allowed to insert the ID which contains ";". -->

<%
	if(status == DELETE) {

%>
   <jsp:setProperty name="scUser" property="deletelist" value="<%=list%>" />
<%
      scUser.getDelete();
      debugPrint("<script>window.location='JManageAddress.jsp';</script>");
	   return;
	}

  if(status == ADD)
  {   ///Add the receipter of Schedule Document
     String userid = request.getParameter("userid");
     String username = request.getParameter("username");
     String useremail = request.getParameter("useremail");
     String password = request.getParameter("userpass");
     String usergroup = request.getParameter("usergroup");

     if(dbencoding){
        userid = RDUtil.toHangleDecode(userid);
        username = RDUtil.toHangleDecode(username);
        useremail = RDUtil.toHangleDecode(useremail);
        password =  RDUtil.toHangleDecode(password);
        usergroup = RDUtil.toHangleDecode(usergroup);
     }

 %>
    <jsp:setProperty name="scUser" property="id" value="<%=userid%>" />
    <jsp:setProperty name="scUser" property="name" value="<%=username%>" />
    <jsp:setProperty name="scUser" property="email" value="<%=useremail%>" />
    <jsp:setProperty name="scUser" property="password" value="<%=password%>" />
    <jsp:setProperty name="scUser" property="usergroup" value="<%=usergroup%>" />
 <%
   scUser.getInsert();
   debugPrint("<script>window.location='JManageAddress.jsp';</script>");
 	return;
 }
%>

<!-- list -->
<html>
	<head>
		<style type="text/css">
			<%@ include file="default.css" %>
		</style>
		<script language="JavaScript">


			function checkList(emailList) {

				var p_code = emailList.split(";");
				for (var i = 0; i < p_code.length; i++ )
				{
					if (p_code[i] != 'undefined' && p_code[i] != null && p_code[i] != '') {
						var n = document.mailList.elements.length;
						for (var j=0; j<n; j++) {
							if (!document.mailList.elements[j].checked && document.mailList.elements[j].value==p_code[i]) {
								document.mailList.elements[j].click();
							}
						}
					}
				}

				return;
			}

	     	function checkEnd(opcode) {

				var n = document.mailList.elements.length;
				var str = "";
				var cnt = 1;
				for (var i=0; i<n; i++) {
					if (document.mailList.elements[i].checked) {
						if ( cnt == 1 ) 	str += document.mailList.elements[i].value;
						else 		str += ";" + document.mailList.elements[i].value;
						cnt++;
					}
				}

				if(opcode == 4) {  //user delete

					document.opcode.action="JManageAddress.jsp?opcode=4&list="+str+";";
					window.open(document.opcode.action,window.name);

				}else if(opcode == 2) { //user add

					if(!document.adduser.userid.value){
						alert(document.setmessage.scuser_12.value);
						document.adduser.userid.focus();
						return;
					}

					if(!document.adduser.username.value){
						alert(document.setmessage.scuser_13.value);
						document.adduser.username.focus();
						return;
					}

					if(!document.adduser.useremail.value){
						alert(document.setmessage.scuser_14.value);
						document.adduser.useremail.focus();
						return;
					}

               if(document.adduser.userpass.value != document.adduser.userpass1.value){
                  alert(document.setmessage.scuser_19.value);
                  document.adduser.userpass.focus();
                  return;
               }


               document.adduser.submit();

				}else{
				   var oPenerFrame = opener;
				   if(oPenerFrame != null){
   					oPenerFrame.document.scform.maillist.value = str;
   					self.close();
					}
				}

				return ;
			}

		function onclickuser(id)
		{
			var userid = "?userid=" + id;
		   var newWin = window.open("JUserModify.jsp" + userid, "NewWindow",'Width=500,Height=200,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=1,resizable=1');
		   self.name = "MainWindow";
		   newWin.opener = self;
		   newWin.focus();
		}
		</script>

		</head>
	<title><%= Message.get("scuser_01") %></title>

<body onLoad="checkList('<%=list%>');" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">

<form name="setmessage">
	<input type="hidden" name="scuser_12" value=<%= Message.get("scuser_12") %> >
	<input type="hidden" name="scuser_13" value=<%= Message.get("scuser_13") %> >
	<input type="hidden" name="scuser_14" value=<%= Message.get("scuser_14") %> >
   <input type="hidden" name="scuser_19" value=<%= Message.get("scuser_19") %> >
</form>

<form name="mailList">
<!-- <table width=100%><td><font color=darkblue><%= Message.get("scuser_02") %></font></td></table> -->

<table border="0" width=100% cellspacing="1" cellpadding="0" bordercolordark="white" bordercolorlight="7c7c7c">
	<tr bgcolor=<%=m_sBtnFace%>>
		<!-- <td align=center><b><font color=white>ID</td> -->
		<td align=center width="40%" height=25><b><font color=white><%= Message.get("scuser_03") %></td>
      <td align=center width="60%" height=25><b><font color=white><%= Message.get("scuser_04") %></td>
	</tr>

<%
   RDJDBCHelper userhelper = scUser.getSelect();
   String id,name,email;
   int count=0;
	try{
			if(userhelper != null) {
				while(userhelper.next()){
					
					String data1 = userhelper.getString(1);
					String data2 = userhelper.getString(2);
					String data3 = userhelper.getString(3);
					
					out.println("<tr>");
					out.println("<td width=\"40%\"><a href=\"javascript:onclickuser('" + data1 + "');\"><input type=checkbox name=username value="+data1+" class=\"style1\">"+data2+"("+data1+")</td>");
					out.println("<td width=\"60%\"><input type=text name=useremail value="+data3+" size=55 class=\"style1\" readonly></td>");
					out.println("</tr>");
					count++;
				}
			}

		}catch (Exception e){

			out.println(e.getMessage());

		}finally{
		   if(userhelper != null)
		      try{
		         userhelper.close();
		      }catch (Exception ee){
		      }
         scUser.getDisconnect();
		}

%>
</form>
   <td colspan=3>
   	<form name="opcode" method="get" action="JManageAddress.jsp">
	      <!-- <a href="javascript:checkEnd(1);"><img src="images/enter_up.gif" border=0 hsapce=0 vspace=0></a>
	      <a href="javascript:checkEnd(4);"><img src="images/del.gif" border=0 hsapce=0 vspace=0></a> -->

	   </form>
	   <br>
	</td>
</table>


<%
   if(status == SCADD){ //in the case of schedule registration/modification, just show confirm buttion.
      int btnX = 250;
      int btnY = 50;
      int gap = count * 20;
      JLButton btnadd = new JLButton();
      btnadd.printButton(btnX,btnY+gap,"confirm",105,3,Message.get("scuser_15"),"checkEnd(1)","images/edit.gif",20);
      out.println("</body></html>");
      return;
   }
%>

<!-- ////////////////////////////////////////////////////////////////////////////////////// -->
<br>
<br>
<table>
<td>
<font color=#6699FF><%= Message.get("scuser_05") %></font>
</td>
</table>

<table border="0"  width=100% cellspacing="1" cellpadding="1" bordercolordark="white" bordercolorlight="7c7c7c">
   <!-- <td colspan=2><font color=darkblue><%= Message.get("scuser_06") %></font><br></td> -->
   <tr bgcolor=<%=m_sBtnFace%>>
      <td align=center width=40% height=25><b><font color=white><%= Message.get("scuser_07") %></td>
      <td align=center wdith=60%><b><font color=white><%= Message.get("scuser_08") %></td>
   <tr>
	 <td>

	 	 <form name="adduser" method="post" action="JManageAddress.jsp">
	 	 <input type=hidden name="opcode" value="2">
	 	 	<%= Message.get("scuser_09") %>
	 </td>
	 <td>
	 	<input type="text" name="userid" value="" size="50" class=style1 >
	 </td>
 </tr>
 <tr>
    <td>
		 <%= Message.get("scuser_10") %>
	 </td>
    <td>
	 	<input type="text" name="username" value="" size="50" class=style1 >
	 </td>
 </tr>
 <tr>
 	<td>
 	 	<%= Message.get("scuser_11") %>
 	 </td>
    <td>
 	 	<input type="text" name="useremail" value="" size="50" class=style1 >
 	 </td>
  </tr>
 <tr>
   <td>
      <%= Message.get("envfiledb_09") %> <!-- pwassword -->
    </td>
    <td>
      <input type="password" name="userpass" maxlength=29 size=12 class="style1">
      Confirm Passwrd&nbsp;<input type="password" name="userpass1" maxlength=29 size=12 class="style1">
    </td>
  </tr>
  <tr>
   <td>
      <%= Message.get("scuser_18") %>
    </td>
    <td>

      <select name="usergroup" class=style1>
          <option value="Users" selected>Users</option>
          <option value="Administrator">Administrator</option>
      </select>

    </td>
  </tr>


  <tr>
     <td colspan=3>
	      <!-- <a href="javascript:checkEnd(2);"><img src="images/uadd.gif" border=0 hsapce=0 vspace=0></a> -->
     </td>

 </table>
 	 </form> <!-- for user add form-->
 </form>


<%
      int btnX = 250;
      int btnY = 50;
      int gap = count * 25;
      JLButton btnadd = new JLButton();
      //btnadd.printButton(btnX,btnY+gap,"confirm",105,3,Message.get("scuser_15"),"checkEnd(1)","images/edit.gif",20);
      btnadd.printButton(btnX,btnY+gap,"del",105,3,Message.get("scuser_16"),"checkEnd(4)","images/delete.gif",20);
      btnadd.printButton(btnX,270+gap,"add",105,3,Message.get("scuser_17"),"checkEnd(2)","images/button_icon_add.gif",20);
%>
</body>
</html>
