<%@ page language="java" import="java.util.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message"%>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page session="false" %>
<%@ include file="session.h"%>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h" %>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>


<%
   FileUpload fu = new FileUpload();
   fu.readForm(request,response,mrdfile_abs_dir+"/mrd",dbencoding);

   FileDesc fde = new FileDesc();
   String strfiledesc = fde.readFileDesc(request,response,mrdfile_abs_dir+"/mrd",dbencoding);
   //System.out.println(strfiledesc);

   //2003.10.17 kokim
   //String strfiledesc = request.getParameter("filedesc");
   String strfilepath = request.getParameter("filepath");
   String strfilename = null;
   int idx = strfilepath.lastIndexOf("\\");
   strfilename = strfilepath.substring(idx+1);


	// in the case of management from file
	if(docinfo.equals("no"))
	{
	   BeansFileDescription fd = new BeansFileDescription();
	   fd.insert(strfilename, strfiledesc);
	}
	// in the case of management from DB
	else
	{
		FileDescription fd = new FileDescription();
		fd.insert(strfilename, strfiledesc, servicename);
	}
%>
<body onload="move()">

   <script language="JavaScript">
      function move() {
         opener.location.reload();
         close();
     }
   </script>
</body>

