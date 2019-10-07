<%@page import="java.lang.*,java.io.*,java.util.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.*"%>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@include file="properties.h"%>
<%
   String[] list = request.getParameterValues("del");//RDServer/reports delete
   if(list == null){
      list = request.getParameterValues("delocxhtm"); //RDServer/ocxhtm delete

      if(list != null)
      {
	      File tmpFile = null;
	      for(int i=0;i<list.length;i++)
	      {
	        try{
	            list[i] = list[i].substring(0,list[i].length()-4);
	            if(hangleflag)
	               list[i] = RDUtil.toHangleDecode(list[i]);

	            list[i] = webpath+"/"+list[i];
	            tmpFile = new File(list[i]);
	            tmpFile.delete();

	            list[i] = RDUtil.replace(list[i],ocxhtmpath,datapath);
	            list[i] = RDUtil.replace(list[i],".html",".txt");
	            tmpFile = new File(list[i]);
	            tmpFile.delete();

	            list[i] = RDUtil.replace(list[i],".txt",".mrd");
	            tmpFile = new File(list[i]);
	            tmpFile.delete();

	            list[i] = RDUtil.replace(list[i],".mrd",".mrr");
	            tmpFile = new File(list[i]);
	            tmpFile.delete();

	            list[i] = RDUtil.replace(list[i],".mrr",".mrq");
	            tmpFile = new File(list[i]);
	            tmpFile.delete();

	         }catch (SecurityException ee){
	            ee.printStackTrace();
	         }
	      }
	      out.println("<script>window.location='JCompleteReport.jsp';</script>");
	    }else
	    {
         if(request.getParameter("delocx") != null && request.getParameter("delocx").equals("ok"))
         {
            out.println("<script>window.location='JCompleteReport.jsp';</script>");
         }
         else
         {
      	  out.println("<script>window.location='JDocAdd.jsp';</script>");
      	}
      }

   }else{
      RDUtil.deleteFiles(list,hangleflag);
      out.println("<script>window.location='JDocAdd.jsp';</script>");
   }
%>
