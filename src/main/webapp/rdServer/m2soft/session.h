<%
response.setHeader("Pragma","No-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0);
response.setHeader ("Cache-Control", "no-cache");
	HttpSession rdsession = request.getSession(false);
	
   if(rdsession != null && ((String)rdsession.getValue("sc.user") != null)) {
	   ;
	}else{
%>
     <script language="JavaScript"> 
	     var url="index.jsp";
		  var frameobj=eval("top");
		  frameobj.location=url;
     </script>
<%		
		return;
	}
%>