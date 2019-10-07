<%@ page import="java.io.*,java.util.*,java.util.Date,java.sql.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,java.text.*,m2soft.rdsystem.server.core.install.Message" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>

<%@ include file="properties.h"%>

<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
   initArg(request,out);
%>
<%!
   int btnY = 0;
   String viewscheduledate_03 = Message.get("viewscheduledate_03");
   String currentjsp ="";
   
   String parseDateString(String sdate) {
      
      Calendar rcalendar = new GregorianCalendar();
      SimpleDateFormat sformat = new SimpleDateFormat(viewscheduledate_03);
               
      rcalendar.set(Calendar.YEAR,Integer.parseInt(sdate.substring(0,4)));
      rcalendar.set(Calendar.MONTH,Integer.parseInt(sdate.substring(4,6))-1);
      rcalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(sdate.substring(6,8)));
      rcalendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(sdate.substring(8,10)));
      rcalendar.set(Calendar.MINUTE,Integer.parseInt(sdate.substring(10,12)));
      rcalendar.set(Calendar.SECOND,Integer.parseInt(sdate.substring(12,14)));
      
      return sformat.format(rcalendar.getTime());     
   }
%>
<%
   try {
      currentjsp = request.getParameter("currentjsp");
      if((currentjsp.toLowerCase().indexOf("http://") == -1) && (currentjsp.toLowerCase().indexOf("ftp://") == -1))
      {
         %><script>alert("<%= Message.get("scheduleadd_111") %>");</script><%
      }
   }catch (Exception e)
   {
      System.out.println("Error 000 JManageScheduling.jsp: "+e);
   }
%>


<!-- list -->
<html>
   <head>
      <style type="text/css">
         <%@ include file="default.css" %>
      </style>
      <script language="JavaScript">
      
         function closeTheWindow() {
            var oPenerFrame = opener;
            if(oPenerFrame != null){
               window.close();
            }
            return;
         }

      </script>
   </head>
   <title><%= Message.get("scheduleadd_108") %></title>

<body onLoad="" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">

<form name="userDefinedList">

<table border="0" width=100% cellspacing="1" cellpadding="0" bordercolordark="white" bordercolorlight="7c7c7c">
   <tr bgcolor=<%=m_sBtnFace%>>
      <td align=center width="40%" height=25><b><font color=white><%= Message.get("scheduleadd_108") %></td>
   </tr>

<%
   try
   {
      URLData ud = new URLData();
      StringTokenizer st = new StringTokenizer(ud.get(currentjsp),":");
      String eachToken = "";
      btnY = 15;
      while (st.hasMoreTokens())
      {
         eachToken = st.nextToken();
         out.println("<tr>");
         out.print("<td width=\"50%\" align=\"center\">"+parseDateString(eachToken)+"</td>");
         out.println("</tr>");
         btnY+=20;
      }
    }
    catch(FileNotFoundException fe)
    {
      %><script>alert("<%= Message.get("scuserdef_02") %>");self.close();</script><%
    }
    catch(StringIndexOutOfBoundsException se)
    {
      %><script>alert("<%= Message.get("scuserdef_01") %>");self.close();</script><%
    }
    catch(Exception e)
    {
      %><script>self.close();</script><%
    }
%>
</form>
</table>


<%
      int btnX = 50;
      int count = 1; 
      int gap = count * 20;
      JLButton btnadd = new JLButton();
      btnadd.printButton(btnX,btnY+gap,"confirm",105,3,Message.get("scuser_15"),"javascript:closeTheWindow();","images/edit.gif",20);
      out.println("</body></html>");
%>
