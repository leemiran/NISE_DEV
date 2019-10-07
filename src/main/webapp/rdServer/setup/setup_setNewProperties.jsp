<%@ page import="java.io.*,
				java.util.*,
				java.util.jar.*,
				javax.servlet.*,
				javax.servlet.http.*,
				javax.servlet.jsp.*"
				
	contentType="text/html; charset=euc-kr" 
%>
<%@ include file = "commonlib.h" %>
<html>
<head><title>Set Properties for New RD Service..</title>
<style>
	<%@ include file="setup.css" %>
</style>

<script language="JavaScript">
<!--
function addRDService()
{
    <% mapToParentForm(out); %>
    
	if(opener != null)
	{
		window.close();
	}
	return;
}
function onClickCharSet()
{
	document.setProperties.charset.value = document.setProperties.charsets.value;
}
-->
</script>

</head>
<body>
<FORM name="setProperties" method="post">
<%
  String newServiceName = request.getParameter("name");
  String dbKind = request.getParameter("dbKind");
  setDBKind(dbKind);
  setMainPanel(out,newServiceName);
%>
<%!
   private String dbKind = "";
   
   public void setDBKind(String dbKind)
   {
      this.dbKind = dbKind;
   }
   public String getDBKind()
   {
      return dbKind;
   }
   
   public void mapToParentForm(JspWriter out)
   {
      try
      {
	      RDService rdservice = new RDService();
	      String[] keyStrings = rdservice.getKeyStrings();
	      
	      out.write("	opener.document.setProperties.serviceName.value = document.setProperties.serviceName.value;\r\n");
	      for (int i=0;i<keyStrings.length;i++)
	      {
	    	  out.write("	opener.document.setProperties."+rdservice.keyStrings[i].substring(rdservice.keyStrings[i].indexOf(".")+1)+".value = document.setProperties."+rdservice.keyStrings[i].substring(rdservice.keyStrings[i].indexOf(".")+1)+".value;\r\n");
    	  }
    	  out.write("	opener.document.setProperties.submit();");
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   public void setMainPanel(JspWriter out, String newServiceName)
   {
      try
      {
	      RDService rdservice = new RDService();
	      String[] keyStrings = rdservice.getKeyStrings();
	      String[] labels = rdservice.getLabels();
	      String name = "";
	      
	      out.write("<input type ='hidden' name='serviceName' value='"+newServiceName+"'/>");
	      out.write("<table CLASS='tabFrame'>\r\n");
	      out.write("	<tr>\r\n");
		  out.write("		<td width=605 align='center'>\r\n");
		  out.write("			<b>"+newServiceName+"</b>\r\n");
		  out.write("		</td>\r\n");
		  out.write("	</tr>\r\n");
  	   	  out.write("</table>\r\n");
  	   	  
	      out.write("<table CLASS='tabFrame'>\r\n");
	      for (int i=0;i<keyStrings.length;i++)
	      {
	      	  name = rdservice.keyStrings[i].substring(rdservice.keyStrings[i].indexOf(".")+1);
	    	  out.write("	<tr>\r\n");
	    	  out.write("		<td bgcolor=#EFDFFF colspan=2>\r\n");
	    	  out.write("		"+labels[i]+"\r\n");
	    	  out.write("		</td>\r\n");
	    	  out.write("	</tr>\r\n");
	    	  out.write("	<tr>\r\n");
	    	  out.write("		<td width=200>"+newServiceName+rdservice.keyStrings[i]+"</td>\r\n");
	    	  if (name.equals("url") || name.equals("driver"))
	    	  {
	    	  	out.write("		<td><input type ='text' name='"+name+"' size=76 value='"+getValue(name)+"' class='style2'/></td>\r\n");
	    	  }
	    	  else if (name.equals("maxconn"))
	    	  {
	    	  	out.write("		<td><input type ='text' name='"+name+"' size=76 value='10' class='style2'/></td>\r\n");
	    	  }
	    	  else if (name.equals("charset"))
	    	  {
	    	  	out.write("		<td>\r\n");
			 	out.write("			<input type ='text' name='"+name+"' size=43 value='EUC_KR' class='style2'/>\r\n");
			  	out.write("			<select name=\"charsets\" onClick=\"onClickCharSet()\">\r\n");
				for (int j = 0; j< charsets.length; j++)
					out.write("				<option value=\""+charsets[j]+"\">"+charsets[j]+"</option>\r\n");
			 	out.write("			</select>\r\n");
			 	out.write("		<td>\r\n");
	    	  }
	    	  else if (name.equals("timeout"))
	    	  {
	    	  	out.write("		<td><input type ='text' name='"+name+"' size=76 value='60' class='style2'/></td>\r\n");
	    	  }
	    	  else
	    	  {
	    	  	out.write("		<td><input type ='text' name='"+name+"' size=76 value='' class='style2'/></td>\r\n");
	    	  }
	    	  out.write("	</tr>\r\n");
    	  }
    	  // Add service control buttons (modify, delete)
    	  out.write("	<tr>\r\n");
	   	  out.write("		<td bgcolor=#EFDFFF colspan=2 align='right'>\r\n");
	   	  out.write("			<a href='javascript:addRDService();'><img src='images/enter_up.gif' border=0></a>\r\n");
	   	  out.write("		</td>\r\n");
	   	  out.write("	</tr>\r\n");
	   	  out.write("</table>");
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   public String getValue(String name)
   {
      for (int i=0; i<templates.length ; i++)
      {
         if (templates[i][0].equals(dbKind))
         {
            if (name.equals("driver"))
            	return templates[i][1];
            else if (name.equals("url"))
            	return templates[i][2];
         }
      }
      return "";
   }
%>
</FORM>
</body>
</html>