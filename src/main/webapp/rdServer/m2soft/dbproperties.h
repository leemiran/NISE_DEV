<%@page import="java.net.*" %>
<%!
   DbPropertyManager dp;
%>
<%
   try{
		dp = DbPropertyManager.getInstance();
		
		if(dp == null){
			DbPropertyManager.init(RDUtil.getDbPropPath());
			dp = DbPropertyManager.getInstance();
		}
        else if(dp.getProperty("drivers") == null){
      	  //sjs 04.09 If dp is encrypted, decrypt it
      	  DbPropertyManager.decodeInstance();
      	  dp = DbPropertyManager.getInstance();
        }
	}catch (Exception e){
	   e.printStackTrace();
	}
%>