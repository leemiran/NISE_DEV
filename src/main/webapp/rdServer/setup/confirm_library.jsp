<%@ page contentType="text/html; charset=euc-kr" %> 
<%@ page language="java" import="java.io.*,java.util.jar.*,java.util.*" %>
<%@ include file="commonlib.h"%>
<%!
	public String writeMsg(String msg)
	{
		String tmp="";
		tmp = "<li>" + msg + "</li>";
		return tmp;
	}

	public String writeErrMsg(String msg)
	{
		String tmp="";
		tmp = "<li><font color=RED>" + msg + "</font></li>";
		return tmp;
	}
%>

<html>
	<style>
	<%@ include file="setup.css" %>
	</style>

	<!-- //////////////// Start Script -->
	<script language="JavaScript">
		function callNextSetup()
		{
			document.confirm_library.submit();
		}

		function callPrevSetup()
		{
			document.confirm_library.action ='confirm_config.jsp';
		   document.confirm_library.submit();
		}
	</script>
	<!-- ////////////////End Script -->

	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">
		<form name="confirm_library" method="post" action="confirm_dbconnect.jsp">

		<!-- ///////////////STEP Header -->
		<table>
		<td bgcolor=#813E00  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#FFDC74  width=550 height=30><b>Report Designer5.0 Setup Confirmation</td><td bgcolor=#FF8A19 width=50 align=center><font color=#ffffff> Step 2</td>
		<tr>
		<td bgcolor=#FFEFBF  colspan=2 align=center>Test Result on RDServer Libraries</td>
		<tr>
		<td bgcolor=#813E00  height=2 colspan=2></td>
		<tr>
		</table>
		<br>
		<!--///////////////STEP Header End -->

		<!--///////////////Navigation Button Beginning--->
		<table>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
			<td bgcolor=#813E00  width=605 height=2 colspan=2></td>
			<tr>
		</table>
		<!--///////////////Navigation Button End --->
	
	<%

	// rdserver.jar check
		try
		{
			Class rdserver = Class.forName("m2soft.rdsystem.server.Server");
			out.println(writeMsg("rdserver library loaded OK"));
		}
		catch(ClassNotFoundException e)
		{
			out.println(writeErrMsg("Unable to find the rdserver library. (failed to load rdserver.jar)"));
		}
		catch(Exception e)
		{
			out.println(writeErrMsg("Failed to load the rdserver library : " + e.getMessage()));
		}

	// rdmaster.jar check
		try
		{
			Class rdmaster = Class.forName("m2soft.rdsystem.server.master.service.MasterServer");
			out.println(writeMsg("rdmaster library loaded OK"));
		}
		catch(ClassNotFoundException e)
		{
			out.println(writeErrMsg("Unable to find the rdmaster library. (failed to load rdmaster.jar)"));
		}
		catch(Exception e)
		{
			out.println(writeErrMsg("Failed to load the rdmaster library : " + e.getMessage()));
		}

	// jdbc driver check
		String CONF_DIR = "";
		String rdserversetupconfig = getServerConfigPath(out,application,request) + "/rdserversetup.config";
		String drivers = "";
		String[] arrDriverList = null;
		
		try
		{
			// rdserversetup.config  check wether there does have file
			File conf_file = new File(rdserversetupconfig);
			if(conf_file.exists())
			{
				BufferedReader pin = null;
				String temp = "";
					
				// config directory find
				try
				{
					pin = new BufferedReader(new FileReader(rdserversetupconfig));
					CONF_DIR = "";
					while ((temp = pin.readLine()) != null) 
					{
						if(!temp.startsWith("#") && temp.indexOf("RDSERVER_PROPERTIES_DIR=") != -1)
						{
							CONF_DIR = temp.substring( temp.indexOf("=")+1, temp.length() );
							break;
						}
					}
				} 
				catch (Exception exc) 
				{
					out.println(writeErrMsg("Error reported while reading rdserversetup.config : " + exc.getMessage()));
					return;
				} 					
				finally 
				{
					try
					{
						if( pin != null )
							pin.close();
					} catch (Exception ce) {}
				}
			}
			// rdserversetup.config does not have file
			else
			{
				out.println(writeErrMsg("ERROR : Unable to find rdserversetup.config."));
				return;
			}
			
			BufferedReader pin = null;
			String temp = "";
				
			// db.propertis read the drivers= items from file.
			try
			{
				pin = new BufferedReader(new FileReader(CONF_DIR + "/db.properties"));
				while ((temp = pin.readLine()) != null) 
				{
					if(!temp.startsWith("#") && temp.indexOf("drivers=") != -1)
					{
						drivers = temp.substring( temp.indexOf("=")+1, temp.length() );
						break;
					}
				}
			} 
			catch (Exception exc) 
			{
				out.println(writeErrMsg("Error reported while reading db.properties : " + exc.getMessage()));
				return;
			} 					
			finally 
			{
				try
				{
					if( pin != null )
						pin.close();
				} catch (Exception ce) {}
			}

			// drivers Save items into arrary
			StringTokenizer tokens = new StringTokenizer(drivers);
			arrDriverList = new String[tokens.countTokens()];
			int i= 0;
			while (tokens.hasMoreElements())
			  arrDriverList[i++] = (String)tokens.nextToken();

			// check if JDBCD driver is loading correctly 
			if(arrDriverList != null)
			{
				for(i=0; i<arrDriverList.length; i++)
				{
					try
					{
						Class rdserver = Class.forName(arrDriverList[i]);
						out.println(writeMsg(arrDriverList[i] + " library loaded OK"));
					}
					catch(ClassNotFoundException e)
					{
						out.println(writeErrMsg("Unable to find "+arrDriverList[i]));
					}
					catch(Exception e)
					{
						out.println(writeErrMsg(arrDriverList[i] + " library loading failure : " + e.getMessage()));
					}
				}
			}
			else
			{
				out.println(writeErrMsg("ERROR : Check the value of drivers in db.properties"));
			}
		}
		catch(Exception e)
		{
			out.println(writeErrMsg("Error reported : " + e.getMessage()));
			return;
		}
	%>

		<!--///////////////Navigation Button Beginning--->
		<table>
			<td bgcolor=#813E00  width=605 height=2 colspan=2></td>
			<tr>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
		</table>
		<!--///////////////Navigation Button End --->

	</body>
</html>