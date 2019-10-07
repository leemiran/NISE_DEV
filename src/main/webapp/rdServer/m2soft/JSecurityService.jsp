<!-- Author : kimsunhee, 2004/04/08
 Security - Access restriction for table-user 
 Users Group Management
-->
<%@ page import="java.util.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.rddbagent.role.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.log.m.*" %><% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %><%!

   private static Hashtable stableAll; //key(groupname)-value(userid)
   private String[] names;

   public void jspInit()
   {
      names = ServiceNTables.getServiceNames();
      stableAll = ServiceNTables.getSelectedTableList();
   }
   public void jspDestroy()
   {
      ServiceNTables.saveToTableListNServiceName();
   }%><%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="../control/lib/JSHttp.jsp"%>

<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<%

	initArg(request,out);

	if(names == null)
      names = ServiceNTables.getServiceNames();
   if(stableAll == null) stableAll = new Hashtable();
   String opcode = m_param.GetStrValue("opcode");
   String stable[] = null;
   String services = m_param.GetStrValue("services");
   int servicesInx = m_param.GetIntValue("servicesInx"); //undefined
   Object[] ttnames = null;

   if(opcode != null && opcode.equals("tblsave"))
   {
      stable = request.getParameterValues("stables");
      boolean encoding = true;
      if(stable != null)
      {
         if(encoding)
            for(int i = 0; i < stable.length ; i++){
               stable[i] = RDUtil.toHangleDecode(stable[i]);
            }
         stableAll.put(services,stable);
         ServiceNTables.setToTableListNServiceName(stableAll);
         ServiceNTables.saveToTableListNServiceName();
      }
      //store it in here
   }
   else if(opcode != null && opcode.equals("tbllist"))
    ;
   else
   {
     opcode ="tbllist";
     servicesInx = 0;
     if(names.length != 0)
        services = names[0];
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

   if (isNS4) _w = '<layer id=one name=one><b><%if(services != null) out.print(services);%></b></layer>';
   if (isIE||isNS6) _w = '<div id=one name=one><b><%if(services != null) out.print(services);%></b></div>';
   document.write(_w);


   var _bool=false;
   var _original="default";
   var _changeTo="move"; /* crosshair, default, move, etc... */
   function changeEm()
   {
     _bool = ! _bool;
     //document.styleSheets[0].rules[0].style.cursor=_bool?_original:_changeTo;
     document.styleSheets[0].rules[0].style.cursor= "crosshair";
   }

   function replaceIt() {

      var _n = document.HitCriteria[0].options[document.HitCriteria[0].selectedIndex].value;

     // if(_v)_n=_v;
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

      document.HitCriteria.opcode.value = 'tbllist';
      document.HitCriteria.servicesInx.value = document.HitCriteria[0].selectedIndex;
      document.HitCriteria.submit();
   }

   function openTarget(url,color)
   {
      document.all.body.src= url;
      replaceIt(color);
   }

   function addTblname(tbl)
   {
       var NI = document.TableList.tables.options.length++;
       document.TableList.tables.options[NI].text=tbl;
   }

   function addSTblname(tbl)
   {

       var isExsist = 0;
       for(var i = 0; i < document.SelectedTableList.stables.options.length; i++)
       {
          if(document.SelectedTableList.stables.options[i].text == tbl)
          {
             isExsist = 1;
             break;

          }
       }

       if(isExsist == 0)
       {
          var NI = document.SelectedTableList.stables.options.length++;
          document.SelectedTableList.stables.options[NI].text=tbl;
          document.SelectedTableList.stables.options[NI].value=tbl;
       }


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

   function tableAdd()
   {

      var _y = 0;
      var _F1 = document.TableList.tables.options;

      var _addInx = new Array();
      var _inx = 0;

      for(var i=0;i<_F1.length;i++)
      {
         _y += (_F1[i].selected)?1:0;
         if(_F1[i].selected)
         {
            addSTblname(_F1[i].text);
            _addInx[_inx++] = i;

         }
      }

   }


   function tableDelete()
   {
      var _x = 0;
      var _F = document.SelectedTableList.stables.options;
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

   function tableSave()
   {

      for(var i=0; i < document.SelectedTableList.stables.options.length ; i++)
         document.SelectedTableList.stables.options[i].selected = true;

      document.SelectedTableList.submit();
   }


</script>
</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<%
   if(!RdMs.getRptServerStatus())
   {
      debugPrint("<img src=images/arrow_right.gif hspace=10>" + Message.get("jrptmoniter_01") + "<img src=images/ok.gif> Not loading");
      return;
   }
%>

<table align=left cellspacing="0" cellpadding="1" border="0" bgcolor="#2d3851">
<tr>
<td>
<table cellspacing="0" cellpadding="1" border="0" bgcolor="#BFC4CB"><tr><td>
<table cellspacing="0" cellpadding="2" border="0" bgcolor="#ffffff"><tr><td>
<table cellspacing="0" cellpadding="1" border="0" bgcolor="#2d3851"><tr><td>
<table cellspacing="0" cellpadding="1" border="0" bgcolor="#dfdfdf"><tr><td>
<table width=680 height=200 border=10 cellpadding=0 cellspacing=0 valign=top><tr>
<td>
   <table border=0 width=100%><td>Services</td><td>Tables</td><td>&nbsp;</td><td>Table(s) in Current Reporting</td><td>&nbsp;</td><tr>
   <td valign=top widh=20%>
   <FORM Name="HitCriteria" METHOD="Post" action="JSecurityService.jsp">
   <SELECT Name="services"  LANGUAGE=javascript ondblclick="return replaceIt()" style="font-size:8pt; color:black; border-width:1px; border-color:gray; border-style:ridge ; background-color:#e4e4e4; text-align:center" size=16>
<%
   if(names != null)
   {
      for(int i=0;i < names.length ; i++)
      {
         if(i == servicesInx)
            out.println("<option selected value =\""+names[i]+"\">"+names[i]+"</option>");
         else
            out.println("<option value =\""+names[i]+"\">"+names[i]+"</option>");
      }
  }
  else
  {
      out.println("<script>alert('Not running --> Monitering - Start Report Engine!');</script>");
      return;
  }

%>
 </SELECT>

 <input type=hidden name="opcode">
 <input type=hidden name="servicesInx">
 </FORM>
 </td>

 <td width=25% valign=top>

    <FORM Name ="TableList" METHOD="POST" action="JSecurityService.jsp">

    <SELECT Name="tables" ondblclick="return tableAdd()" style="font-size:8pt; color:blue; border-width:0; border-color:gray; border-style:none; background-color:#e4e4e4; text-align:center" size=16 multiple>
     <%
      if(opcode != null && (opcode.equals("tbllist") || opcode.equals("tblsave")) )
      {
         ttnames = ServiceNTables.getTableList(services);

         if(ttnames != null && ttnames.length > 0)
         {
            for(int i=0; i < ttnames.length; i++)
            {
               out.println("<option value =\""+ttnames[i]+"\">"+ttnames[i]+"</option>");
            }
         }
         else
         {
            if(ttnames == null)
            {
               String msg = RdMs.getLastErrmsg(services);
               if(msg != null)
                  msg = RDUtil.replace(msg,"'"," ");

               out.println("<script>document.all.one.innerHTML= document.all.one.innerHTML + '<br><font color=red>"+msg+"</font>';</script>");

           }
           else
              out.println("<script>document.all.one.innerHTML= document.all.one.innerHTML + '<br><font color=red>Dose not exist tables</font>';</script>");


           return;

         }
     }
   %>
    </SELECT>
    </FORM>

 </td>
 <td width=10% align=center>
 <%
    JLButton btnadd = new JLButton();
    JLButton btndel = new JLButton();
    JLButton btnsave = new JLButton();
    int x = 300;
    int y = 120;
    btnadd.printButton(x,y,"tadd",80,3,Message.get("menu_04"),"tableAdd()","images/button_icon_next.gif",20);
    btnadd.printButton(x,y+30,"tdel",80,3,Message.get("menu_06"),"tableDelete()","images/button_icon_prev.gif",20);

 %>
</td>

 </td>
 <td width=35% valign=top>

    <FORM Name ="SelectedTableList" METHOD="POST" action="JSecurityService.jsp">
       <input type=hidden name=opcode value ="tblsave">
       <input type=hidden name=services value ="<%=services%>">
       <SELECT Name="stables" ondblclick="return tableDelete()" style="font-size:8pt; color:red; border-width:1px; border-color:gray; border-style:ridge ; background-color:#e4e4e4; text-align:center" size=16 multiple>

       <%

            if(opcode != null && (opcode.equals("tbllist") || opcode.equals("tblsave")) && names != null)
            {

               Object[] objs = (Object[])stableAll.get(services);

               if(objs != null)
               {
                  for(int i=0; i< objs.length ; i++)
                  {
                     out.println("<option value =\""+objs[i]+"\">"+objs[i]+"</option>");
                  }
               }
            }

       %>
       </SELECT>

     <%  btnadd.printButton(x+240,y+158,"tsave",80,3,"Apply","tableSave()","images/button_icon_save.gif",20); %>

       <!-- <input type=image src="images/button_icon_save.gif" nobr onclick="return tableSave()" hspace=3 alt='Save' >Apply -->
    </FORM>

 </td>

 <td width=10% align=center>

 </td>

 </table>

</td></table>
</td></table>
</td></table>
</td></table>
</td></table>

<div id="tableCount" style="POSITION: absolute; top:45px; left:200px;">
   <table>
    <td>
    <%
    if(ttnames != null)
        out.println("("+ttnames.length+")");
    %>
   </td>
   </table>
</div>

</body>
</html>

