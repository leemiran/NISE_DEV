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
   FileUpload.setUserDefindpath(mrdfile_abs_dir+m_param.GetStrValue("targetpath"));

%>
    <title><%= Message.get("JUploadSelect_01") %></title> 
    <style><%@ include file="addschedule.css" %></style>
       
<script language="JavaScript"> 
         function set_mrdfilename(X){
            parent.document.scform.schedulefile.value = X;
         }
         
         function submitUp() {
            var frm = document.upform;
            var filepath,ext;
            
            if(frm.filename.value == ""){
               alert(document.setmessage.mrdfileupload_02.value);
               frm.filename.focus();
               return;
            }else {
               filepath = frm.filename.value;
               ext = filepath.substr(filepath.length-3, 3);
					ext = ext.toLowerCase();
               if(ext != "mrd" && ext != "txt"){
                  alert(document.setmessage.mrdfileupload_03.value);
                  frm.filename.focus();
                  return;
               }
            }
            var param = "?filedesc="+frm.filedesc.value + "&filepath="+frm.filename.value;
            frm.action="JUploadMrd.jsp"+param; 
            frm.submit();
       }

      </script>
      
  </head>
  
<%
     debugPrint(Message.get("JUploadSelect_02"));
%>

<form name="setmessage">
	<input type="hidden" name="mrdfileupload_02" value=<%= Message.get("mrdfileupload_02") %> >
	<input type="hidden" name="mrdfileupload_03" value=<%= Message.get("mrdfileupload_03") %> >
</form>
  
   <form name="upform" method="post" action="JUploadMrd.jsp" encType="multipart/form-data">
   <table border=0 cellpadding=0 cellspacing=3 align=center>
      <tr>
         <td>
            <input type="file" name="filename" size=80 class="style1">
         </td>    
      </tr>
      <tr>
      	<td>
      	<%= Message.get("mrdfilelist_11") %>
      	</td>
      </tr>
      <tr>
      	<td>
      	<input type="text" name="filedesc" size=80 class="style1">
      	</td>
      </tr>
    </table>
   </form>
  
<%
{

   int btnX = 400;
   int btnY = 125;
   JLButton btn = new JLButton();
   btn.printButton(btnX,btnY,"btnupload",105,3,Message.get("mrdfilelist_09"),"submitUp()","images/button_icon_arrowup.gif",20);
   
}
%>
</body>
</html>