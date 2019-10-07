<%@page import="java.net.*" %>
<%!
   String logTrue;
   int rMilisecond;
   int mlogLevel = 0;
   String ipnport="localhost";
   String logServiceName;
   LogPropertyManager p;

%>
<%
   try{
		p = LogPropertyManager.getInstance();
		
		if(p.getProperty("log.maxfilenum") == null){
      	  //sjs 04.09 If p is encrypted, decrypt it
      	  LogPropertyManager.decodeInstance();
      	  p = LogPropertyManager.getInstance();
        }
        
		rMilisecond = p.getInt("log.refreshMileTime");
		logTrue  = p.getProperty("log.moniteringlog");
		if(logTrue != null) logTrue = logTrue.toLowerCase();
		logServiceName = p.getProperty("log.moniteringlogServiceName","");
		mlogLevel  = p.getInt("log.moniteringlogLevel");            
		ipnport = InetAddress.getLocalHost().getHostAddress()+":"+request.getServerPort();   
		
	}catch (Exception e){
	   e.printStackTrace();
	}
%>