<%@ page import="java.util.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.rddbagent.role.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.log.m.*" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page session="false" %>
<%@ include file="session.h"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="../control/lib/JSHttp.jsp"%>

<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);
	Object[] groupnames = ServiceNGroups.getGroupNames();
   String opcode = m_param.GetStrValue("opcode");
   String groupname = m_param.GetStrValue("groupname"); //The current group name
   int groupInx = m_param.GetIntValue("groupinx"); //undefined

   Object[] serviceids = null;
   if(opcode != null)
   {
      boolean encoding = true;
      ArrayList list = new ArrayList();

      if(opcode.equals("servicesave"))
      {
         groupname =  m_param.GetStrValue("gname");

         Object[] userids = request.getParameterValues("slist");

         if(userids != null)
         {
            if(encoding)
            {
               for(int i = 0; i < userids.length ; i++)
               {
                  userids[i] = RDUtil.toHangleDecode(userids[i].toString());
               }
            }

            // kokim 2004.10.27 for NongHyup
				if(isNongHyup)
				{
					Vector vdb = new Vector();
					Vector vxml = new Vector();
					String[] dbNames = (String[])userids;

					for(int i = 0; i< dbNames.length; i++)
					{
						if(dbNames[i].indexOf("(db)") > 0)
						{
							vdb.addElement(dbNames[i].substring(0, dbNames[i].indexOf("(db)")));
						}
						else if(dbNames[i].indexOf("(xml)") > 0)
						{
							vxml.addElement(dbNames[i].substring(0, dbNames[i].indexOf("(xml)")));
						}
					}

					Object[] db = new Object[vdb.size()];
					for(int i=0; i<vdb.size(); i++)
					{
						db[i] = (Object)vdb.get(i);
					}
					ServiceNGroups.addGroup(groupname, db);

					Object[] xml = new Object[vxml.size()];
					for(int i=0; i<vxml.size(); i++)
					{
						xml[i] = (Object)vxml.get(i);
					}
					NHServiceNGroups.addGroup(groupname, xml);

				}
				else
					ServiceNGroups.addGroup(groupname,userids);
			}
			else
			{
           ServiceNGroups.addGroup(groupname,new Object[0]);
			  if(isNongHyup)
					NHServiceNGroups.addGroup(groupname, new Object[0]);
			}
      }
      else if(opcode.equals("groupsave")) //
      {
         ServiceNGroups.addGroup(groupname,new Object[0]);
         groupnames = ServiceNGroups.getGroupNames();
      }
      else if(opcode.equals("groupdel"))
      {
         ServiceNGroups.deleteGroup(groupname);
         groupnames = ServiceNGroups.getGroupNames();

         groupInx = 0;
         groupname = groupnames[groupInx].toString();

      }

   }
   else
   {
      opcode = "servicelist";
      groupname = groupnames[groupInx].toString();
   }


%>

<style>
<%@ include file="icis.css" %>
</style>
<script>


   var isIE=document.all?true:false;
   var isNS4=document.layers?true:false;
   var isNS6=!isIE&&document.getElementById?true:false;
   var _w = '';

   if (isNS4) _w = '<layer id=one name=one><b><%if(groupname != null) out.print(groupname);%></b></layer>';
   if (isIE||isNS6) _w = '<div id=one name=one><b><%if(groupname != null) out.print(groupname);%></b></div>';
   document.write(_w);

   function replaceIt()
   {
      var _n = document.Groups[0].options[document.Groups[0].selectedIndex].value;
      if (isNS4)
      {
         document.one.document.open();
         document.one.document.write(_n);
         document.one.document.close();

      }
      if (isIE)
      {
         document.all.one.innerHTML=_n;

      }

      if(isNS6)
      {
         document.getElementById("one").innerHTML=_n;
      }

      document.Groups.opcode.value = 'servicelist';
      document.Groups.groupInx.value = document.Groups[0].selectedIndex;
      document.Groups.submit();
   }


   function groupSave(_v,inx,mod)
   {
      document.Groups.groupname.options[inx].selected = true;
      document.Groups.opcode.value = mod;
      document.Groups.groupInx.value = 0;
      document.Groups.submit();
   }


   function groupService(_v,inx,mod)
   {
      var _F1 = document.Services.slist.options;

      document.Services.opcode.value = mod;
      document.Services.groupInx.value = inx;
      document.Services.gname.value = _v;

      if(document.Services.slist.options.length > 0)
      {
         for(var ii=0; ii < document.Services.slist.options.length; ii++)
            _F1[ii].selected = true;;
      }

      document.Services.submit();

   }

   function whichButton()
   {
      if (window.event.keyCode == 13)
      {
         groupfunction(document.getElementById('promptbox').value);

      }

   }


   function prompt2(promptpicture, prompttitle, message, sendto)
   {

      document.getElementById('ulist').style.visibility='hidden';
      promptbox = document.createElement('div');
      promptbox.setAttribute ('id' , 'prompt');
      document.getElementsByTagName('body')[0].appendChild(promptbox);
      promptbox = eval("document.getElementById('prompt').style");
      promptbox.position = 'absolute';
      promptbox.top = 100;
      promptbox.left = 180;
      promptbox.width = 300;
      promptbox.border = 'outset 1 #bbbbbb';
      document.getElementById('prompt').innerHTML = "<table cellspacing='0' cellpadding='0' border='0' width='100%' bgcolor=#ffffff><tr valign='middle'><td width='22' height='22' style='text-indent:2;' class='titlebar'><img src='" + promptpicture + "' height='18' width='18'></td><td class='titlebar'>" + prompttitle + "</td></tr></table>";
      document.getElementById('prompt').innerHTML = document.getElementById('prompt').innerHTML + "<table cellspacing='1' cellpadding='1' border='0' width='100%' bgcolor=#ffffff><tr><td>" + message + "</td></tr><tr><td><input type='text' id='promptbox' onblur='this.focus()' class='promptbox' onkeydown='whichButton()'><br></td></tr><tr><td align='right'><br><input type='button' class='prompt' value='OK' onMouseOut='this.style.border=\"1 outset #dddddd\"' onMouseOver='this.style.border=\"1 solid transparent\"' onClick='" + sendto + "(document.getElementById(\"promptbox\").value); document.getElementsByTagName(\"body\")[0].removeChild(document.getElementById(\"prompt\"));'> <input type='button' class='prompt' value='Cancel' onMouseOut='this.style.border=\"1 outset transparent\"' onMouseOver='this.style.border=\"1 solid transparent\"' onClick='" + sendto + "(\"\"); document.getElementsByTagName(\"body\")[0].removeChild(document.getElementById(\"prompt\"));'></td></tr></table>";
      document.getElementById("promptbox").focus();

   }


   function groupfunction(value)
   {

      if(value.length <= 0)
      {
         document.getElementById('ulist').style.visibility='visible';
         return false;
      }
      else
      {
       var _F1 = document.Groups.groupname.options;

       var isExsist = 0;



          for(var j =0 ; j < _F1.length; j++)
          {
             if(_F1[j].text == value)
             {
                isExsist = 1;
                break;
             }
          }

          if(isExsist == 0)
          {
             var NI = document.Groups.groupname.options.length++;
             document.Groups.groupname.options[NI].text=value;
             document.Groups.groupname.options[NI].value=value;
             groupSave(value,NI,'groupsave');
          }


      }

   }


   function callPrompt(icon,title,msg)
   {
      prompt2(icon, title,msg, 'groupfunction');
   }


   function newGroup()
   {
      callPrompt('images/button_icon_add.gif','New Group','<%=Message.get("JSecurityGroup01")%>');
   }


   function delGroup()
   {

     var _F = document.Groups.groupname;

     if(document.Groups.groupname.length == 0)
        return;

      var ret = confirm(_F.value + '"<%=Message.get("JSecurityGroup02")%>"');
      if(ret)
      {

         var _inx = _F.selectedIndex;
         if(_inx == -1)
            return;
         var value = _F.options[_inx].value;
         groupSave(_F.options[_inx].value,_inx,'groupdel');

      }
   }

   function addService(val, dbkind)
   {
       var _F1 = document.Services.slist.options;


       var isExsist = 0;
       for(var i = 0; i < val.length; i++)
       {
          isExsist = 0;

          for(var j =0 ; j < _F1.length; j++)
          {
             if(_F1[j].text == val[i])
             {
                isExsist = 1;
                break;
             }
          }

          if(isExsist == 0)
          {
             var NI = _F1.length++;
				 <% if(isNongHyup) { %>
					_F1[NI].text=val[i]+"("+dbkind+")";
				 <% }else{ %>
					 _F1[NI].text=val[i];
				 <% } %>
				 <% if(isNongHyup) { %>
					_F1[NI].value=val[i]+"("+dbkind+")";
				 <% }else{ %>
					 _F1[NI].value=val[i];
				 <% } %>
             _F1[NI].selected = true;
          }

      }

  }

   function serviceDelete()
   {
      var _x = 0;
      var _F = document.Services.slist.options;
      var _addInx = new Array();
      var _inx = 0;


      for(var i=0;i<_F.length;i++)
      {
         _x += (_F[i].selected)?1:0;

         if(_F[i].selected)
         {
            _addInx[_inx++] = i;
         }

      }

      deleteList(_addInx,_F);

   }

   function deleteList(_addInx,target)
   {
      var _F1 = target;
      var _addInxs= _addInx;

      if(_addInxs.length > 0)
      {

         for(var j=_addInxs.length-1; j >= 0 ; j--)
         {
            if(document.all)
               _F1.remove(_addInxs[j]);
            if (document.layers)
             _F1[_addInxs[j]] = null;
         }
      }


   }

   function selectedServicename(chkobjs)
   {
      if(chkobjs == "")
      {
         document.getElementById('ulist').style.visibility='visible';
         document.getElementById('ServiceList').style.visibility='hidden';
         return false;
      }

      var n = chkobjs.length;
      var str = "";
      var cnt = 0;
      var inxs = Array();

      for (var i=0; i < n; i++)
      {

         if (chkobjs[i].checked)
         {
            inxs[cnt] = chkobjs[i].value;
            cnt++;
         }
      }


     if (cnt == 0 && chkobjs.checked)
        inxs[0] = chkobjs.value;


      addService(inxs, "db");
      document.getElementById('ulist').style.visibility='visible';
      document.getElementById('ServiceList').style.visibility='hidden';
      return true;
   }

   function selectedXmlname(chkobjs)
   {
      if(chkobjs == "")
      {
         document.getElementById('ulist').style.visibility='visible';
         document.getElementById('XmlList').style.visibility='hidden';
         return false;
      }

      var n = chkobjs.length;
      var str = "";
      var cnt = 0;
      var inxs = Array();

      for (var i=0; i < n; i++)
      {

         if (chkobjs[i].checked)
         {
            inxs[cnt] = chkobjs[i].value;
            cnt++;
         }
      }


     if (cnt == 0 && chkobjs.checked)
        inxs[0] = chkobjs.value;


      addService(inxs, "xml");
      document.getElementById('ulist').style.visibility='visible';
      document.getElementById('XmlList').style.visibility='hidden';
      return true;
   }

	function selectServices()
   {
       document.getElementById('ulist').style.visibility='hidden';
       document.getElementById('ServiceList').style.visibility='visible';
       document.getElementById('XmlList').style.visibility='hidden';
       document.getElementById('ServiceList').firstChild.focus();

   }

	function selectXml()
   {
       document.getElementById('ulist').style.visibility='hidden';
       document.getElementById('ServiceList').style.visibility='hidden';
       document.getElementById('XmlList').style.visibility='visible';
       document.getElementById('XmlList').firstChild.focus();

   }


</script>


</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<table align=left cellspacing="0" cellpadding="1" border="0" bgcolor="#2d3851">
<tr>
<td>
<table cellspacing="0" cellpadding="1" border="0" bgcolor="#BFC4CB"><tr><td>
<table cellspacing="0" cellpadding="2" border="0" bgcolor="#ffffff"><tr><td>
<table cellspacing="0" cellpadding="1" border="0" bgcolor="#2d3851"><tr><td>
<table cellspacing="0" cellpadding="1" border="0" bgcolor="#dfdfdf"><tr><td>
<table width=680 height=200 border=10 cellpadding=0 cellspacing=0 valign=top><tr></td>
<td>
   <table border=0 width=600><td>Group</td><td>&nbsp;</td><td>Service(s) in Current </td><td><img src="images/button_icon_share.gif"></td><tr>
   <td valign=top widh=25%>
   <FORM Name="Groups" METHOD="Post" action="JSecurityGroup.jsp">

   <SELECT Name="groupname"  alt ='abc' LANGUAGE=javascript ondblclick="return replaceIt()" style="font-size:8pt; color:black; border-width:1px; border-color:gray; border-style:ridge ; background-color:#e4e4e4; text-align:center" size=16>
<%
   if(groupnames != null)
   {
      for(int i=0;i < groupnames.length ; i++)
      {
         if(i == groupInx)
            out.println("<option selected value =\""+groupnames[i]+"\">"+groupnames[i]+"</option>");
         else
            out.println("<option value =\""+groupnames[i]+"\">"+groupnames[i]+"</option>");
      }
  }
  else
  {
      out.println("<script>alert('Not running --> Monitering - Start Report Engine!');</script>");
      return;
  }

    JLButton btnadd = new JLButton();
    int x = 150;
    int y = 130;

%>
 </SELECT>

 <input type=hidden name="opcode" value="groupsave">
 <input type=hidden name="groupInx">
 </FORM>
 </td>
 <td width=10% align=left valign=center>
 <%
     btnadd.printButton(x,y,"tadd",80,3,Message.get("menu_04"),"newGroup()","images/button_icon_add.gif",20);
     btnadd.printButton(x,y+30,"tshare",80,3,"Service +","selectServices()","images/button_icon_share.gif",20);
	  if(isNongHyup)
			btnadd.printButton(x,y+60,"txml",80,3,"Xml +","selectXml()","images/button_icon_share.gif",20);
     btnadd.printButton(x,y+90,"tdel",80,3,Message.get("menu_06"),"delGroup()","images/button_icon_del.gif",20);

 %>
 </td>


 <td width=25% valign=top>

    <div id ='ulist' style="Z-INDEX:30; VISIBILITY:visible;">
    <table>
    <td>
    <FORM Name ="Services" METHOD="POST" action="JSecurityGroup.jsp">

    <SELECT Name="slist" style="font-size:8pt; color:blue; border-width:0; border-color:gray; border-style:none; background-color:#e4e4e4; text-align:center" size=16 multiple>
     <%

      if(opcode != null) //&& opcode.equals("servicelist") || opcode.equals("groupdel") || opcode.equals("servicesave")
      {
			// kokim 2004.10.27 for NongHyup
			if(isNongHyup)
			{
				Object[] dbservice = ServiceNGroups.getServiceList(groupname);
				Object[] xmlservice = NHServiceNGroups.getServiceList(groupname);
				int servicecount = 0;

				if(dbservice != null && dbservice.length > 0)
				{
					for(int i=0; i < dbservice.length; i++)
					{
						out.println("<option value =\""+dbservice[i]+"(db)\">"+dbservice[i]+"(db)</option>");
					}
					servicecount = servicecount + dbservice.length;
				}

				if(xmlservice != null && xmlservice.length > 0)
				{
					for(int i=0; i < xmlservice.length; i++)
					{
						out.println("<option value =\""+xmlservice[i]+"(xml)\">"+xmlservice[i]+"(xml)</option>");
					}
					servicecount = servicecount + xmlservice.length;
				}

				serviceids = new Object[servicecount];
				int tmp = 0;

				if(dbservice != null && dbservice.length > 0)
				{
					System.arraycopy(dbservice, 0, serviceids, 0, dbservice.length);
					tmp = dbservice.length;
				}
				if(xmlservice != null && xmlservice.length > 0)
				{
					System.arraycopy(xmlservice, 0, serviceids, tmp, xmlservice.length);
				}
			}
			else
			{
				serviceids = ServiceNGroups.getServiceList(groupname);

				if(serviceids != null && serviceids.length > 0)
				{
					for(int i=0; i < serviceids.length; i++)
					{
						out.println("<option value =\""+serviceids[i]+"\">"+serviceids[i]+"</option>");
					}
				}
			}
      }
   %>
    </SELECT>
     <input type=hidden name="opcode" value="servicesave">
     <input type=hidden name="gname" value="<%=groupname%>">
     <input type=hidden name="groupInx">

    </td>
    </table>
    </div>
 </td>

  <td width=40% align=left valign=center><br>
  <%
     btnadd.printButton(x+250,y+30,"sdel",80,3,Message.get("menu_06"),"serviceDelete()","images/button_icon_del.gif",20);
     btnadd.printButton(x+250,y+60,"ssave",80,3,"Apply","groupService(document.Groups[0].options[document.Groups[0].selectedIndex].text, document.Groups[0].selectedIndex,'servicesave');","images/button_icon_save.gif",20);
  %>
 </td>


 </table>

</td></table>
</td></table>
</td></table>
</td></table>
</td></table>
</td></table>

</FORM>


<div id="ServiceList"  style="POSITION: absolute; top:60px; left:180px; width=200; height=250; visibility:hidden; overflow:auto; ">

<FORM name="ServiceChecker">
<html>
<body>
<table cellspacing='0' cellpadding='0' border='0' width='100%'>
<tr valign='middle'>
<td width='22' height='22' style='text-indent:2;' class='titlebar'>
  <img src='images/button_icon_share.gif' height='18' width='18'>
</td>
<td class='titlebar'>
   Add Service
</td>
</tr>
</table>

<table bgcolor=#ffffff border=0 width=100%>
<td><%=Message.get("Security6")%><td></tr>
<%
   String[] names = ServiceNTables.getServiceNames();
   out.println("<tr><td>");
   for(int i = 0; i < names.length; i++)
   {
      out.println("<input type=\"checkbox\" name=\"servicename\" value=\""+names[i]+"\">"+names[i]+"<br>");
   }
   out.println("</td>");

   if(names.length > 0)
      out.println("<tr><td align=right><input type='button' class='prompt' value='OK' onMouseOut='this.style.border=\"1 outset #dddddd\"' onMouseOver='this.style.border=\"1 solid transparent\"' onClick='selectedServicename(document.ServiceChecker.servicename);'> <input type='button' class='prompt' value='Cancel' onMouseOut ='this.style.border=\"1 outset transparent\"' onMouseOver='this.style.border=\"1 solid transparent\"' onClick='return selectedServicename(\"\");'>");
   else
      out.println("</tr>");
%>

</table>
</FORM>
</body>
</html>
</div>









<div id="XmlList"  style="POSITION: absolute; top:60px; left:180px; width=200; height=300; visibility:hidden; overflow:auto;">

<FORM name="XmlChecker">

<table cellspacing='0' cellpadding='0' border='0' width='100%'>
<tr valign='middle'>
<td width='22' height='22' style='text-indent:2;' class='titlebar'>
  <img src='images/button_icon_share.gif' height='18' width='18'>
</td>
<td class='titlebar'>
   Add Xml Service
</td>
</tr>
</table>

<table bgcolor=#ffffff border=0 width=100%>
<td><%=Message.get("Security6")%><td></tr>
<%
	if(isNongHyup)
	{
		String[] xmlnames = NHServiceNTables.getServiceNames();
		out.println("<tr><td>");
		for(int i = 0; i < xmlnames.length; i++)
		{
			out.println("<input type=\"checkbox\" name=\"servicename\" value=\""+xmlnames[i]+"\">"+xmlnames[i]+"<br>");
		}
		out.println("</td>");

		if(xmlnames.length > 0)
			out.println("<tr><td align=right><input type='button' class='prompt' value='OK' onMouseOut='this.style.border=\"1 outset #dddddd\"' onMouseOver='this.style.border=\"1 solid transparent\"' onClick='selectedXmlname(document.XmlChecker.servicename);'> <input type='button' class='prompt' value='Cancel' onMouseOut ='this.style.border=\"1 outset transparent\"' onMouseOver='this.style.border=\"1 solid transparent\"' onClick='return selectedXmlname(\"\");'>");
		else
			out.println("</tr>");
	}
%>

</table>
</FORM>
</div>










<div id="ServiceCount" style="POSITION: absolute; top:72px; left:350px;">
   <table>
    <td>
    <%
    if(serviceids != null)
        out.println("<font color=blue>("+serviceids.length+")</font>");
    %>
   </td>
   </table>
</div>



</body>
</html>

