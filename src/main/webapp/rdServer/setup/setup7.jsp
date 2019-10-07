<%@ page import="java.io.*,java.util.*,java.util.jar.*" contentType="text/html; charset=euc-kr" %>
<%@ include file="commonlib.h"%>

<%!
	public String getServiceList(Vector list)
	{
		StringBuffer buffer = new StringBuffer();
		for (int i=0;i<list.size();i++)
		{
			if (i != (list.size()-1))
				buffer.append(""+list.elementAt(i)+", ");
			else
				buffer.append(""+list.elementAt(i));
		}
		
		return buffer.toString();
	}
%>
<%
	String server_config_filepath = getServerConfigPath(out,application,request) + "/rdserversetup.config";
	String server_home = "";
	String server_lib = "";
	String server_conf = "";
	String server_license = getServerConfigPath(out,application,request) + "/license";

	File config_file = new File(server_config_filepath);

	if(config_file.exists())
	{
		BufferedReader br = new BufferedReader(new FileReader(server_config_filepath));

		String temp = "";
		while((temp = br.readLine()) != null)
		{
			if(!temp.startsWith("#") && (temp.indexOf("RDSERVER_DIR=") != -1))
			{
				server_home = temp.substring(temp.indexOf("=")+1, temp.length());
			}
			else if(!temp.startsWith("#") && (temp.indexOf("RDSERVER_LIB_DIR=") != -1))
			{
				server_lib = temp.substring(temp.indexOf("=")+1, temp.length());
			}
			else if(!temp.startsWith("#") && (temp.indexOf("RDSERVER_PROPERTIES_DIR=") != -1))
			{
				server_conf = temp.substring(temp.indexOf("=")+1, temp.length());
			}
		}
	}
	else
	{
	}
	
	//*************************
	//sjs 0520 Get the service list
	//*************************
	String serverhome = getServerConfigPath(out,application,request);
	String default_dbproperties = serverhome +"/conf/db.properties";
	Properties prop = new Properties();
	Vector v_serviceList = new Vector();

	FileInputStream in = null;

	try
	{
		in = new FileInputStream(default_dbproperties);
		prop.load(in);

		String urlname;
		int inx;

	   for (Enumeration e = prop.propertyNames() ; e.hasMoreElements() ;) 
	   {
	      urlname = (String)e.nextElement();
	      inx = urlname.indexOf(".url");
	      if(inx != -1)
	         v_serviceList.add(urlname.substring(0,inx));
     	}

   }
   catch (Exception ex) {
   }
   finally
   {
   	if(in != null)
   		try {
   			in.close();
		   }
		   catch (Exception ex) {
		   }
   }
%>

<html>
	<style>
	<%@ include file="setup.css" %>
	</style>

<!-- ////////////////start script -->
	<script language="JavaScript">

		function callNextSetup()
		{
		   document.setup7.submit();
		}

		function callPrevSetup()
		{
			document.setup7.action ='setup6.jsp';
		   document.setup7.submit();
		}

	</script>
<!-- ////////////////end script-->

	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">
	   <form name="setup7" method="post" action="confirm_config.jsp">

 <!-- ///////////////STEP HEADER -->
		<table>
		<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Step 7</td>
		<tr>
		<td bgcolor=#DBDBEA  colspan=2 align=center>RDServer Setup Completed</td>
		<tr>
		<td bgcolor=#3399CC  height=2 colspan=2></td>
		<tr>
		</table>
		<br>
<!--///////////////STEP HEADER END -->
	
<!--///////////////Navigation Buttion Begining--->
		<table>
			<td align=left>
			<font size=-2>Ref) Click on next after restarting the WAS</font>
			</td>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
		</table>
<!--///////////////Navigation Buttion End --->
		
		<table width=605>
			<td>
			<b>The Setup is all done.. <br>After restarting the WAS, proceed with the next button to see that<br>RDServer is properly configured..</b>
			</td>
		</table>

		<dl>
		<dt><li>Location of RDServer Setup Configuration File:</li></dt>
			<dl><font color="BLUE" face="Tahoma"><%=server_config_filepath%></font></dl>
		<dt><li>RDServer Home Directory:</li></dt>
			<dl><font color="BLUE" face="Tahoma"><%=server_home%></font></dl>
		<dt><li>RDServer CLASSPATH:</li></dt>
			<dl><font color="BLUE" face="Tahoma"><%=server_lib%></font></dl>
		<dt><li>RDServer Environment Setup (conf) Directroy:</li></dt>
			<dl><font color="BLUE" face="Tahoma"><%=server_conf%></font></dl>
		<dt><li>Service Name List</li></dt>
			<dl><font color="BLUE" face="Tahoma"><%=getServiceList(v_serviceList)%></font></dl>
		<dt><li>RDServer License Directory</li></dt>
			<dl><font color="BLUE" face="Tahoma"><%=server_license%></font></dl>


	<%
		File license = new File(server_license + "/license");
		if(!license.exists())
		{
			out.println("<dl><font color=\"RED\">License file not found.</font></dl>");
		}
	%>
		</dl>
<!--///////////////Navigation Buttion Begining--->
		<table>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
			<td align=left>
			<font size=-2>Ref) Click on next after restarting the WAS</font>
			</td>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
	   </td>

		</table>
<!--///////////////Navigation Buttion End --->

		</form>

	</body>
</html>