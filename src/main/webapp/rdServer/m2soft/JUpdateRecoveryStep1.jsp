<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File, m2soft.rdsystem.server.log.l.*,m2soft.rdsystem.server.core.install.Message " %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
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
   out.println("<body bgcolor="+m_sBtnFace+">");
%>

<style>
<%@ include file="icis.css" %>
</style>

<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">


<form name="login" action="JUpdateRecoveryStep2.jsp" method="post">

<table border="0" cellspacing=1 cellpadding=0 height=294 width="680">
  <td width="50" height="321"><img src="images/blank.gif" width=50 hspace=0 vspace=0></td>
  <td width="5" height="321">&nbsp;
            <p>&nbsp;</p>
</td>
  <td height="321" width="625">
            <p align="left">&nbsp;</p>
            <table border=0 cellpadding=1 cellspacing=0 align=top width="590">
            <tr>
               <td  align=top colspan=3 width="589">

               </td>
                    <td width="1"></td>
            </tr>
            <tr>

              <td width="187" height="19">&nbsp;</td>

              <td width="37" height="19">
                 <br>


              </td>

              <td width="365" height="19">&nbsp;</td>
                    <td width="1" height="19"></td>
            </tr>
            <tr>
            <td colspan=3 width="589"><font size="4" color="blue">&nbsp;<%= Message.get("JUpdateRecoveryStep1_00") %></font><hr>            <p>&nbsp;&nbsp;<%= Message.get("JUpdateRecoveryStep1_02") %></font></p>
            <p>&nbsp;&nbsp;<%= Message.get("JUpdateRecoveryStep1_03") %></font></p>
<hr></td>
<td width="1">
&nbsp;                    </td>
            </tr>
            <tr>
              <td colspan=3 width="589">
                        <p>&nbsp;</p>
              </td>
                    <td width="1"></td>
            </tr>
            </table>

            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
  </td>

    </tr>
</table>




<%
   {
      JLButton btnadd = new JLButton();
      btnadd.printButton(75,250,"envup",160,6, "<font size=2><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Message.get("jupdatestep1_05")+"</b></font>"  ,"javascript:login.submit();","images/arrow_right.gif",28);
   }

   out.println("<input type=hidden name=checkstep value=step1>");
%>



</form>
