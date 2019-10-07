<%@ page import="java.util.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.rddbagent.role.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.log.m.*" %><% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %><%!
   Object[] groupnames = null;
   Object[] users = null;
%>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);
   String opcode = m_param.GetStrValue("opcode");
   String userid = m_param.GetStrValue("userid");
   String pass = m_param.GetStrValue("pass");
   String verify = m_param.GetStrValue("verify");
   String grouplist = m_param.GetStrValue("grouplist");
   groupnames = ServiceNGroups.getGroupNames();

   if(opcode != null && opcode.equals("newuser"))
   {
      new Users().addUser(userid,pass,grouplist," ");
   }

   if(opcode != null && opcode.equals("deluser"))
   {
      UserInfo uinfo = new Users().deleteUser(m_param.GetStrValue("userlist"));
   }

   users = Users.getUserIds();
   UserInfo uinfo = null;

%>

<style>
<%@ include file="icis.css" %>
</style>
<script>

   var isIE=document.all?true:false;
   var isNS4=document.layers?true:false;
   var isNS6=!isIE&&document.getElementById?true:false;
   var _w = '';
   var groupinxs = Array();
   var usernames = Array();
   var cnt = 0;

   <%
     if(users != null)
     {
        for (int i=0; i < users.length; i++)
        {

          uinfo = Users.getUserInfo(users[i].toString());
          out.println("groupinxs[cnt] = \"("+ uinfo.getGroup()+")\";");
          out.println("usernames[cnt] = \""+ users[i].toString()+"\";");
          out.println("cnt++;");

        }
     }
   %>


   function addUser()
   {
      var frm = document.UserManForm;
      if(frm.userid.value.length == 0 || frm.pass.value.length == 0)
         return;
      if(frm.pass.value != frm.verify.value)
      {
         alert(' The password is not correct');
         return;
      }

      frm.opcode.value = 'newuser';
      frm.submit();

   }

   function delUser()
   {
     var frm = document.UserManForm;
     if(frm.userlist.length == 0)
        return;
      var _F =  frm.userlist;
      var ret = confirm(_F.value + ' Are you sure you delete the chosen user?');
      if(ret)
      {

         var _inx = _F.selectedIndex;
         if(_inx == -1)
            return;
         var value = _F.options[_inx].value;
         frm.opcode.value = 'deluser';
         frm.submit();

      }


   }

   function showGroup()
   {

     var frm = document.UserManForm;

     if(frm.userlist.length == 0)
        return;
      var _F =  frm.userlist;

      var _inx = _F.selectedIndex;
      if(_inx == -1)
      return;

      var value = groupinxs[_inx];
      var value2 = usernames[_inx];


      if (isNS4)
      {
         document.ServiceList.document.open();
         document.ServiceList.document.write(value);
         document.ServiceList.document.close();

      }
      if (isIE)
      {
         document.all.ServiceList.innerHTML=value;

      }

      if(isNS6)
      {
         document.getElementById("ServiceList").innerHTML=value;
      }

      document.UserManForm.userid.value = value2;


   }


</script>


</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<br>
<form name="UserManForm" action="JSecurityUser.jsp" method="POST">
<input type=hidden name='opcode'>
<table align=left cellspacing="0" cellpadding="1" border="0" bgcolor="#2d3851" >
<tr>
<td>
   <table cellspacing="0" cellpadding="1" border="0" bgcolor="#BFC4CB"><tr><td>
   <table cellspacing="0" cellpadding="2" border="0" bgcolor="#ffffff"><tr><td>
   <table cellspacing="0" cellpadding="1" border="0" bgcolor="#2d3851"><tr><td>
   <table cellspacing="0" cellpadding="1" border="0" bgcolor="#dfdfdf"><tr><td>
   <table width=680 height=200 border=10 cellpadding=0 cellspacing=0 valign=top><tr>
   </td>
   <td>
   <table border=0 width=600><td><img src="images/target.gif" border=0 hspace=0 halign=center>New User</td><td>&nbsp;</td><td><img src="images/t.gif" border=0 hspace=0>Users(Group)</td><td>&nbsp;</td><tr>
   <td valign=top widh=34%>

      <table>
      <td>
      </td>
      <td><input type=text name='userid' size=12 maxlength=30>
      </td><tr>
      <td>
      Password
      </td>
      <td><input type=password name='pass' size=12 maxlength=12>
      </td><tr>
      <td>
      Verify
      </td>
      <td><input type=password name='verify' size=12 maxlength=12>
      </td><tr>
      <td>Group
      </td>
      <td>
         <select name='grouplist'>
         <%

            if(groupnames != null)
            {
               for(int i = 0; i < groupnames.length; i++)
               out.println("<option value='"+ groupnames[i].toString() + "'>"+groupnames[i].toString()+"</option>");
            }
         %>
         </select>
      </td>
      </tr>
      <td colspan=2 align=right>
      <br>

      <%
          JLButton btnadd = new JLButton();
          int x = 110;
          int y = 210;
          btnadd.printButton(x,y,"adduser",80,3,Message.get("menu_04"),"addUser()","images/button_icon_add.gif",20);
     %>

      </td>
      </table>

   </td>
   <td width=1% align=left valign=right>&nbsp;</td>
   </td>

   <td width=30% valign=top>
   <select name=userlist onclick="javascript:showGroup();" style="font-size:8pt; color:blue; border-width:0; border-color:gray; border-style:none; background-color:#e4e4e4; text-align:center" size=14>
   <%

      if(groupnames != null)
      {

         if(users != null)
         {
            for(int j = 0; j < users.length; j++)
               out.println("<option value='"+ users[j].toString()+ "'>"+ users[j].toString()+ "</option>");
         }
      }
   %>

   </select>
   <%btnadd.printButton(x+250,y,"deluser",80,3,Message.get("menu_06"),"delUser()","images/button_icon_del.gif",20);%>
   </td>
   <td width=35% align=left valign=center>
   &nbsp;
   </td>


   </table>

</td></table>
</td></table>
</td></table>
</td></table>
</td></table>
</td></table>

</FORM>


<div id="ServiceList"  style="POSITION: absolute; top:80px; left:350px; width=150; height=200; visibility:visible">
<table cellspacing='0' cellpadding='0' border='0' width='100%'>
<tr valign='middle'>
<td>
(Group)
</td>
</tr>
</table>
</div>


</body>
</html>

