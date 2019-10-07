<%@ page contentType="text/html; charset=euc-kr" %> 
<%@ page language="java" import="java.io.*,java.util.jar.*" %>
<%@ include file="commonlib.h"%>
<%!
	public String writeMsg(String msg)
	{
		String tmp="";
		tmp = "<dt><li>" + msg + "</li></dt>";
		return tmp;
	}

	public String writeSubMsg(String msg)
	{
		String tmp="";
		tmp = "<dl><font color=BLUE face=µ¸¿òÃ¼>" + msg + "</font></dl>";
		return tmp;
	}

	public String writeErrMsg(String msg)
	{
		String tmp="";
		tmp = "<dt><li><font color=RED>" + msg + "</font></li></dt>";
		return tmp;
	}

	public String writeSubErrMsg(String msg)
	{
		String tmp="";
		tmp = "<dl><font color=RED face=µ¸¿òÃ¼>" + msg + "</font></dl>";
		return tmp;
	}
%>

<html>
	<style>
	<%@ include file="setup.css" %>
	</style>

	<!-- ////////////////Script Start -->
	<script language="JavaScript">
		function callNextSetup()
		{
			document.confirm_config.submit();
		}
	</script>
	<!-- ////////////////Script End -->

	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">
		<form name="confirm_config" method="post" action="confirm_library.jsp">

		<!-- ///////////////STEP Header -->
		<table>
		<td bgcolor=#813E00  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#FFDC74  width=550 height=30><b>Report Designer5.0 Setup Confirmation</td><td bgcolor=#FF8A19 width=50 align=center><font color=#ffffff> Step 1</td>
		<tr>
		<td bgcolor=#FFEFBF  colspan=2 align=center>Test Result on RDServer Environment Setup</td>
		<tr>
		<td bgcolor=#813E00  height=2 colspan=2></td>
		<tr>
		</table>
		<br>
		<!--///////////////STEP Header End -->

		<!--///////////////Navigation Button Start--->
		<table>
			<td align=right>
			<a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
			<td bgcolor=#813E00  width=605 height=2 colspan=2></td>
			<tr>
		</table>
		<!--///////////////Navigation Button End --->
	
	<dl>
	<%		
		String CONF_DIR = "";
		String LOG_DIR = "";
      String confPathAtRdagent = "";
		String rdserversetupconfig = getServerConfigPath(out,application,request) + "/rdserversetup.config";
      String rdagent = getServerConfigPath(out,application,request) + "/rdagent.jsp";
		
		try
		{
			out.println(writeMsg("Location of the RDServer setup configuration file:"));
			out.println(writeSubMsg(rdserversetupconfig));

   // rdagent.jsp  rdserversetup.config Check if the file location is setting
         File agent_file = new File(rdagent);
         if(agent_file.exists())
         {
				BufferedReader pin = null;
            String temp = "";
            String findString = "setRDServerSetupConfigDir(\"";
							
				try
				{
					pin = new BufferedReader(new FileReader(rdagent));
					while ((temp = pin.readLine()) != null) 
					{
                  int index = temp.indexOf(findString);
						if(index != -1)
						{
							confPathAtRdagent = temp.substring(index + findString.length(), temp.indexOf("\");"));
							break;
						}
					}

               if(!rdserversetupconfig.equals(confPathAtRdagent + "/rdserversetup.config"))
               {
                  out.println(writeSubErrMsg("The path of RDServer setup configuration file defined in rdagent.jsp is incorrect."));
                  out.println(writeSubErrMsg("---> " + confPathAtRdagent));
               }
				} 
				catch (Exception exc) 
				{
					out.println(writeSubErrMsg("Error reported while reading rdagent.jsp : " + exc.getMessage()));
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
         else
         {
				out.println(writeSubErrMsg("ERROR : Unable to find rdagent.jsp."));
         }
      
	// rdserversetup.config Check if the file exists
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
						if(!temp.startsWith("#") && (temp.indexOf("RDSERVER_PROPERTIES_DIR=") != -1))
						{
							CONF_DIR = temp.substring( temp.indexOf("=")+1, temp.length() );
						}

						// Log Directory 
						if(!temp.startsWith("#") && (temp.indexOf("RDSERVER_LOG_DIR=") != -1))
						{
							LOG_DIR = temp.substring( temp.indexOf("=")+1, temp.length() );
						}
					}

					out.println(writeMsg("RDServer Configuration Directory:"));
					out.println(writeSubMsg(CONF_DIR));
				} 
				catch (Exception exc) 
				{
					out.println(writeSubErrMsg("Error reported while reading rdserversetup.config : " + exc.getMessage()));
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
	// rdserversetup.config Does not have file
			else
			{
				out.println(writeSubErrMsg("ERROR : Unable to find rdserversetup.config."));
				return;
			}


	// db.properties Confirm File
			boolean check_conf = true;
			File db_file = new File(CONF_DIR + "/db.properties");
			if(db_file.exists())
			{
				out.println(writeSubMsg("db.properties confirmed OK"));
			}
			else
			{
				check_conf = false;
				out.println(writeSubErrMsg("ERROR : Unable to find db.properties."));
			}

	// rd.properties Confirm File
			File rd_file = new File(CONF_DIR + "/rd.properties");
			if(rd_file.exists())
			{
				out.println(writeSubMsg("rd.properties confirmed OK"));
			}
			else
			{
				check_conf = false;
				out.println(writeSubErrMsg("ERROR : Unable to find rd.properties."));
			}

	// log.properties Confirm File
			File log_file = new File(CONF_DIR + "/log.properties");
			if(log_file.exists())
			{
				out.println(writeSubMsg("log.properties confirmed OK"));
			}
			else
			{
				check_conf = false;
				out.println(writeSubErrMsg("ERROR : Unable to find log.properties."));
			}

			if(!check_conf)
				return;

	// Log Directory Confirm 
			out.println(writeMsg("Test on Log Directory"));
			if(LOG_DIR.equals(""))
			{
				out.println(writeSubErrMsg("ERROR : Unable to find RDSERVER_LOG_DIR key in rdserversetup.config file."));
			}
			else
			{
				File log_dir = new File(LOG_DIR);
				if(!log_dir.exists())
				{
					out.println(writeSubErrMsg("ERROR : Unable to find " + LOG_DIR));
					return;
				}

				out.println(writeSubMsg(LOG_DIR));
			}
		}
		catch(Exception e)
		{
			out.println(writeErrMsg("Error reported : " + e.getMessage()));
		}
	%>
	</dl>

		<!--///////////////Navigation Button Start--->
		<table>
			<td bgcolor=#813E00  width=605 height=2 colspan=2></td>
			<tr>
			<td align=right>
			<a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
		</table>
		<!--///////////////Navigation Button End--->

	</body>
</html>