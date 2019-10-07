<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.rdscheduler.RdMail"  %>
<% String contentType1 = m2soft.rdsystem.server.core.install.Message.getcontentType(); response.setContentType(contentType1);%>
<%@ page session="false" %>
<%@ include file="session.h"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JLJsp.jsp" %>
<%@ include file="../control/lib/JLObj.java" %>
<%@ include file="../control/lib/JLRuntimeClass.java" %>
<%@ include file="../control/lib/JLHttp.java" %>

<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<title><%=Message.get("JDocSendMail_01")%></title>
<% 
   initArg(request,out); 
   String targetmrd = null;
   String gosendmail = null;   
   
   boolean ret = false;
   
   targetmrd =m_param.GetStrValue("targetpath");
   
   gosendmail = m_param.GetStrValue("sendopcode");
  
   if(gosendmail != null && gosendmail.equals("1")) {
      
      String to  = m_param.GetStrValue("to");
      
      Vector tos = new Vector();
      if(to != null){
         StringTokenizer st = new StringTokenizer(to,";");
         while (st.hasMoreTokens()) 
            tos.addElement((String)st.nextToken());
      }
      
      String from  = m_param.GetStrValue("from");
      String title  = m_param.GetStrValue("title");
      String attachefile ="";
      String contents = "";


      attachefile  = mrdfile_abs_dir+m_param.GetStrValue("targetpath");
      contents  = m_param.GetStrValue("contents");
      
      RdMail m = new RdMail();
      try{
         m.setFrom(from);
         m.setTo(tos);
         m.setSubject(title);
         m.setAttacheFile(attachefile,contents);
         ret = m.send();
         if(ret){
%>
            <body onload="move()">
               <script language="JavaScript">
                  function move() {
                     alert('<%=Message.get("JDocSendMail_13")%>');
                     close();
                  }
               </script>
            </body>   
<%      
         }else{
            debugPrint(attachefile);
            debugPrint(Message.get("JDocSendMail_12"));
            debugPrint("<script>alert('"+Message.get("JDocSendMail_11")+"');close();</script>");
         }
      } catch (Throwable e) {
         debugPrint(e.getMessage());
         e.printStackTrace();
      }
      return;
   }
%>
<html>
<head>
<script language="JavaScript"> 
   function submitUp() {
      var frm = document.mailform;
      var filepath,ext;
   
      if(frm.from.value == ""){
         alert(document.setmessage.JDocSendMailMsg_01.value);
         frm.from.focus();
         return;
      }
      
      if(frm.to.value == ""){
         alert(document.setmessage.JDocSendMailMsg_02.value);
         frm.to.focus();
         return;
      }
    
      frm.action="JDocSendMail.jsp"; 
      frm.submit();
   }
</script>
</head>
<title><%=Message.get("JDocSendMail_09")%></title>
<style>
<!--
<%@ include file="addschedule.css" %>
</style>
 
<body>
  <form name="setmessage">
     <input type="hidden" name="JDocSendMailMsg_01" value="<%=Message.get("JDocSendMail_02")%>"> 
     <input type="hidden" name="JDocSendMailMsg_02" value="<%=Message.get("JDocSendMail_03")%>"> 
  </form>
  <form name="mailform">
   <table boerder=1 cellpadding=0 cellspacing=0 nobr>
      <tr>
         <td><%=Message.get("JDocSendMail_04")%></td><td><%=targetmrd%></td>
      </tr>    
      <tr>
         <td><%=Message.get("JDocSendMail_05")%></td><td><input type="text" name="from" size=30 class='style5'>&nbsp;ex)emailuser@smtp.host.name</td>
      </tr> 
       <tr>
         <td><%=Message.get("JDocSendMail_06")%></td><td><input type="text" name="to" size=50 class='style5'>&nbsp;ex)emailuser@smtp.host.name;..</td>
      </tr>
       <tr>
         <td><%=Message.get("JDocSendMail_07")%></td><td><input type="text" name="title" size=30 class='style5'></td>
      </tr>
       <tr>
         <td><%=Message.get("JDocSendMail_08")%></td><td><textarea name="contents" rows="5" cols="40" class='style5'></textarea></td>
      </tr>             
   </table>
      <input type="hidden" name="targetpath" value="<%=targetmrd%>">
      <input type="hidden" name="sendopcode" value="1">
      
  </form>
  

<%
{

   int btnX = 380;
   int btnY = 145;
   JLButton btn = new JLButton();
   btn.printButton(btnX,btnY,"btnupload",105,3,Message.get("JDocSendMail_10"),"submitUp()","images/button_icon_send.gif",20);
   
}
%>
</body>