<%@ page import="java.net.*,java.util.*,java.sql.*,java.io.BufferedInputStream,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rdscheduler.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.logger.*" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="properties.h"%>
<%@ include file="dbproperties.h"%>
<html>
<title><%= Message.get("envfiledb_01") %></title> 
      <style><%@ include file="addschedule.css" %></style>
</title>      
<body bgcolor="#ffffff" text="#000000" leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<%!
   //DB that can provide the service of scheduling currently 
   String oracle ="", sqlserver="", mysql="", db2="", odbc="", as400="", drivers="", url="", driverName=null;
   String delete ="";
%>
<%
   delete = request.getParameter("delete");
   
   url = dp.getProperty(servicename+".url",null);
   drivers = dp.getProperty("drivers",null);
   if(RdmrdDBAgent.getUseWasPool()){
      
      try{
         RdmrdDBAgent agent = new RdmrdDBAgent(servicename,RdLogManager.getInstance().getScheduleLog());
         url = agent.getDriverName();
         url = RDUtil.replace(url," ","-");
         drivers = url;
         agent.disconnect();
         
      }catch (Exception e){
         e.printStackTrace();
      }
   }
   
   if(url != null){
      url = url.toLowerCase();
      if(url.indexOf("oracle") != -1){
         oracle = "selected";
         sqlserver ="";
         mysql="";
         db2="";
         as400="";
      }
      else if(url.indexOf("sqlserver") != -1){
         sqlserver ="selected";
         oracle ="";
         mysql="";
         db2="";
         as400="";
      }
      else if(url.indexOf("mysql") != -1){
         mysql ="selected";
         oracle="";
         sqlserver="";
         db2="";
         as400="";
      } else if((url.indexOf("db2") != -1)){
         mysql ="";
         oracle="";
         sqlserver="";
         as400="";
         db2="selected";
      }else if(url.indexOf("as400") != -1){
         mysql ="";
         oracle="";
         sqlserver="";
         db2="";
         as400="selected";
      }else if(url.indexOf("odbc") != -1){
         mysql ="";
         oracle="";
         sqlserver="";
         db2="";
         odbc="selected";
         as400="";
      }
      
   }
 
   if(drivers != null){
%>

<form name="envdb" method="get" action="JSchTable.jsp">
     
<table border=0 width=100% cellpadding=0 cellspacing=3>
<tr>
   <td width=8% height=25 align=center bgcolor=#d4d0c8>
      <p><font color=white><b><%= Message.get("envfiledb_02") %></b></font>
   </td>
   <td width=50% bgcolor=#d4d0c8 align=center>
      <p><font color=white><b><%= Message.get("envfiledb_03") %></b></font>
   </td>
     <tr>
     <td><%= Message.get("envfiledb_04") %></td><td>
     <select name="scdrivers">
<%
	  String envfiledb_05 = Message.get("envfiledb_05");
      StringTokenizer driverlist = new StringTokenizer(drivers);
      while (driverlist.hasMoreTokens()) {
         driverName = driverlist.nextToken();
         driverName = driverName.toLowerCase();

         if(driverName.indexOf("oracle") != -1){
            out.println("<option value=\"oracle\" "+oracle+">Oracle---"+driverName+"</option>");
         }
         else if(driverName.indexOf("sqlserver") != -1){
            out.println("<option value=\"msql\" "+sqlserver+">MSSQL---"+driverName+"</option>");
         }
         else if(driverName.indexOf("mysql") != -1){
            out.println("<option value=\"mysql\" "+mysql+">MySql---"+driverName+"</option>");
         }else if(driverName.indexOf("db2") != -1){
            out.println("<option value=\"db2\" "+db2+">DB2---"+driverName+"</option>");
         }else if(driverName.indexOf("as400") != -1){
            out.println("<option value=\"as400\" "+as400+">AS400---"+driverName+"</option>");
         }else if(driverName.indexOf("odbc") != -1){
            out.println("<option value=\"odbc\" "+odbc+">ODBC---"+driverName+"</option>");
         }else
           out.println("<option value=notsurport>"+driverName+envfiledb_05+"</option>");
       
      } //while end 
      out.println("</select></from>");
   } //if(drivers != null) end
   
%>   
   </td>
   <tr>
   <td><%= Message.get("envfiledb_06") %></td><td><font color=red><%= servicename%></td>
   <tr>
   <td><%= Message.get("envfiledb_07") %></td><td><font color=red><%= dp.getProperty(servicename+".url","")%></td>
   <tr>
   <td><%= Message.get("envfiledb_08") %></td><td><font color=red><%= dp.getProperty(servicename+".user","")%></td>
   <tr>
   <td><%= Message.get("envfiledb_09") %></td><td><font color=red><%= dp.getProperty(servicename+".password","")%></td>
   
   <tr>
   <td colspan=2>
<%  
    String envfiledb_10 = Message.get("envfiledb_10");
    if(this.oracle.equals("selected")) {
      
      if(delete == null || delete.equals("") || delete.equals("no"))
          CreateTableOracle.execute(servicename,out,false); 
      else{
          CreateTableOracle.execute(servicename,out,true); 
      }
    }
       
    if(this.sqlserver.equals("selected")){
      
      if(delete == null || delete.equals("") || delete.equals("no"))
          CreateTableMsql.execute(servicename,out,false); 
      else{
          CreateTableMsql.execute(servicename,out,true); 
      }
    }
       
    if(this.mysql.equals("selected")) {
      
       if(delete == null || delete.equals("") || delete.equals("no"))
          CreateTableMysql.execute(servicename,out,false); 
       else{
          CreateTableMysql.execute(servicename,out,true); 
       }
    }
    
    if(this.db2.equals("selected") || this.as400.equals("selected")) {
       if(delete == null || delete.equals("") || delete.equals("no"))
          CreateTableDb2.execute(servicename,out,false); 
       else{
          CreateTableDb2.execute(servicename,out,true); 
       }
    }

    if(this.odbc.equals("selected")) {
       if(delete == null || delete.equals("") || delete.equals("no"))
          CreateTableOdbc.execute(servicename,out,false); 
       else{
          CreateTableOdbc.execute(servicename,out,true); 
       }
    }    
    
    Socket connSock = null;
    DataInputStream sin = null;
    DataOutputStream sout = null;
       
    try {
           //Generate Socket
         connSock = new Socket ("localhost", 4989); 
         
         //Generate DataInputStream and DataOutputStream of socket 
         sin = new DataInputStream(connSock.getInputStream()); 
         sout = new DataOutputStream(connSock.getOutputStream()); 
         sout.writeUTF("anonymous");
         sout.writeUTF("scstop");
         sout.flush();
         
      }catch (Exception ex){
        // if(ex instanceof ConnectException)
        //    out.println(envfiledb_10);
        // else
           System.out.println(ex);
      }
%>
 </td>
 </table>
 </form>
 </body>
 </html>