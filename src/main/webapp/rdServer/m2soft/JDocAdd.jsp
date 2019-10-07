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
%>
    <title><%= Message.get("mrdfilelist_01") %></title>
      <style><%@ include file="addschedule.css" %></style>
      <script language="JavaScript">
         function set_mrdfilename(X){
            parent.document.scform.schedulefile.value = X;
         }

         function submitUp() {
            var frm = document.upform;
            var filepath,ext;

            if(frm.filename.value == ""){
               alert(document.setmessage.mrdfilelist_02.value);
               frm.filename.focus();
               return;
            }else {
               filepath = frm.filename.value;
               ext = filepath.substr(filepath.length-3, 3);
               if(ext != "mrd" && ext != "txt"){
                  alert(document.setmessage.mrdfilelist_03.value);
                  frm.filename.focus();
                  return;
               }
            }
            var param = "?filedesc="+frm.filedesc.value + "&filepath="+frm.filename.value;
            frm.action="JDocUpload.jsp"+param;
            frm.submit();
       }

      </script>
  </head>
<%!
  boolean add = false;
%>
<%
    //Check whether it is added into iFrame by Scheduling->add 
   try{
   if(request.getParameter("add").equals("yes"))
      add = true;
   }catch (Exception n){
      add = false;
   }
   MrdFileListMgr mrdlistmgr = MrdFileListMgr.getInstance();
   BeansMrdFileList[] filelist = null;

   filelist =  mrdlistmgr.getPermittedMrdList(mrdfile_abs_dir,sessionUser, mrdpath, servicename);


//read the files of the directory, and get the modified day, size
%>

<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">

<form name="setmessage">
   <input type="hidden" name="mrdfilelist_02" value=<%= Message.get("mrdfilelist_02") %> >
   <input type="hidden" name="mrdfilelist_03" value=<%= Message.get("mrdfilelist_03") %> >
</form>

<form name="delfileform" method="post" action="JDocDelete.jsp">
<table border=0 width=800 cellpadding=0 cellspacing=0>
<tr>
   <td>

      <table border="0" cellPadding="0" cellSpacing="0">
         <tr height=1>
            <td id=td_lefttop_4 width=1 bgcolor="#ffffff"></td>
            <td id=td_top_4 bgcolor="#ffffff"></td>
            <td id=td_righttop_4 width=1 bgcolor="#666666"></td>
         </tr>
         <tr height='15'>
         <td id=td_left_4 width=1 bgcolor="#ffffff"></td>
         <td id=td_text_4 align=left width=80 bgcolor="#B0C4DE">
          <table border="0" cellPadding="0" cellSpacing="0"><tr>
          <td width=2 bgcolor="#B0C4DE"></td>
          <td width=2 bgcolor="#B0C4DE"></td>
          <td><img alt border=0 height=2 src=images/trans.gif width="10"><br><%= Message.get("mrdfilelist_04") %></td></tr>

         </table>
         </td>
         <td id=td_right_4 width=1 bgcolor="#666666"></td>
         </tr>
         <tr height=1>
            <td id=td_leftbottom_4 width=1 bgcolor="#666666">
            </td>
            <td id=td_bottom_4 bgcolor="#666666">
            </td>
            <td id=td_rightbottom_4 width=1 bgcolor="#666666">
            </td>
         </tr>
      </table>

   </td>
   <td>
         <table border="0" cellPadding="0" cellSpacing="0">
         <tr height=1>
            <td id=td_lefttop_4 width=1 bgcolor="#ffffff"></td>
            <td id=td_top_4 bgcolor="#ffffff"></td>
            <td id=td_righttop_4 width=1 bgcolor="#666666"></td>
         </tr>
         <tr height='15'>
         <td id=td_left_4 width=1 bgcolor="#ffffff"></td>
         <td id=td_text_4 align=left width=350 bgcolor="#B0C4DE">
          <table border="0" cellPadding="0" cellSpacing="0"><tr>
          <td width=2 bgcolor="#B0C4DE"></td>
          <td width=2 bgcolor="#B0C4DE"></td>
          <td><img alt border=0 height=2 src=images/trans.gif width="10"><br><%= Message.get("mrdfilelist_05") %></td></tr>

         </table>
         </td>
         <td id=td_right_4 width=1 bgcolor="#666666"></td>
         </tr>
         <tr height=1>
            <td id=td_leftbottom_4 width=1 bgcolor="#666666">
            </td>
            <td id=td_bottom_4 bgcolor="#666666">
            </td>
            <td id=td_rightbottom_4 width=1 bgcolor="#666666">
            </td>
         </tr>
      </table>

   </td>
   <td>
         <table border="0" cellPadding="0" cellSpacing="0">
         <tr height=1>
            <td id=td_lefttop_4 width=1 bgcolor="#ffffff"></td>
            <td id=td_top_4 bgcolor="#ffffff"></td>
            <td id=td_righttop_4 width=1 bgcolor="#666666"></td>
         </tr>
         <tr height='15'>
         <td id=td_left_4 width=1 bgcolor="#ffffff"></td>
         <td id=td_text_4 align=left width=160 bgcolor="#B0C4DE">
          <table border="0" cellPadding="0" cellSpacing="0"><tr>
          <td width=2 bgcolor="#B0C4DE"></td>
          <td width=2 bgcolor="#B0C4DE"></td>
          <td><img alt border=0 height=2 src=images/trans.gif width="10"><br><%= Message.get("mrdfilelist_06") %></td></tr>

         </table>
         </td>
         <td id=td_right_4 width=1 bgcolor="#666666"></td>
         </tr>
         <tr height=1>
            <td id=td_leftbottom_4 width=1 bgcolor="#666666">
            </td>
            <td id=td_bottom_4 bgcolor="#666666">
            </td>
            <td id=td_rightbottom_4 width=1 bgcolor="#666666">
            </td>
         </tr>
      </table>
   </td>
   <td width=260>
        <table border="0" cellPadding="0" cellSpacing="0">
         <tr height=1>
            <td id=td_lefttop_4 width=1 bgcolor="#ffffff"></td>
            <td id=td_top_4 bgcolor="#ffffff"></td>
            <td id=td_righttop_4 width=1 bgcolor="#666666"></td>
         </tr>
         <tr height='18'>
         <td id=td_left_4 width=1 bgcolor="#ffffff"></td>
         <td id=td_text_4 align=left width=160 bgcolor="#B0C4DE">
          <table border="0" cellPadding="0" cellSpacing="0"><tr>
          <td width=2 bgcolor="#B0C4DE"></td>
          <td width=2 bgcolor="#B0C4DE"></td>
          <td><img alt border=0 height=2 src=images/trans.gif width="10"><br><%= Message.get("mrdfilelist_07") %></td></tr>

         </table>
         </td>
         <td id=td_right_4 width=1 bgcolor="#666666"></td>
         </tr>
         <tr height=1>
            <td id=td_leftbottom_4 width=1 bgcolor="#666666">
            </td>
            <td id=td_bottom_4 bgcolor="#666666">
            </td>
            <td id=td_rightbottom_4 width=1 bgcolor="#666666">
            </td>
         </tr>
      </table>
   </td>



<tr>
<%
      for(int i=0;i< filelist.length;i++)
      {
         /// bgso 2002/10/07 show only mrd file
         String FileExtension = filelist[i].getFilename().substring(filelist[i].getFilename().length()-4,filelist[i].getFilename().length());
         if(FileExtension.equals(".mrd"))
         {
 %>

   <tr onMouseOver="this.style.backgroundColor='lightyellow';status='';return true;" onMouseOut="this.style.backgroundColor='';status='';return true;">
   <td align=center height=20><%=filelist[i].getFilenum()%></td>
   <td><img src="images/rd.gif" border=0>&nbsp;
<%
   if(add) { //it is added into IFRAME by scheduling-> add
      out.println("<a href='javascript:set_mrdfilename(\""+ mrdfile_abs_dir + DIRSEPARATOR + "mrd" + filelist[i].getFilename() + "\")'>"+filelist[i].getFilename()+"</a>");
   } else {
      String waskind = getServletConfig().getServletContext().getServerInfo().toLowerCase();
      if(waskind.indexOf("jeus") != -1 || waskind.indexOf("weblogic") != -1)
        out.println("<a href="+ "\"http://"+serverip+"/"+mrdpath+DIRSEPARATOR+"mrd"+DIRSEPARATOR+filelist[i].getFilename()+"\">"+filelist[i].getFilename().substring(1)+"</a>");
      else
      out.println("<a href=\"download.jsp?filename="+mrdpath+DIRSEPARATOR+ "mrd"+DIRSEPARATOR+filelist[i].getFilename()+"\">"+filelist[i].getFilename().substring(1)+"</a>");

   }
%>
   </td>
   <td align=center>
      <%=filelist[i].getFiletime() %>
   </td>
   <td align=center>
      <%=filelist[i].getFilelength()%>
   </td>



   <tr>
<%
     }
   }
%>

   </table>
</form>


</body>
</html>