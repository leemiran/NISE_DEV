<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page language="java" import="java.io.*,java.util.jar.*,java.util.*, java.sql.*, java.net.*" %>
<%@ include file="commonlib.h"%>
<%!

	// Resources in KOREAN
	String confirm_dbconnect_deleteBackupFiles = "Delete Backup Files";

	private boolean debug = false;

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

	public void deleteAllBackupFiles(JspWriter out, String rdserverHome, String confDir)
	{
		try
		{
			deleteBackupFiles(rdserverHome);
			deleteBackupFiles(confDir);

			out.println("<script>");
			out.println("document.confirm_dbconnect.action='confirm_productinfo.jsp';");
			out.println("document.confirm_dbconnect.submit();");
			out.println("</script>");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void deleteBackupFiles(String dir)
	{
		File direc = new File(dir);

		if (direc.isDirectory())
		{
			File[] list = direc.listFiles();
			File file = null;
			String fileName = null;
			if (debug)
			{
				System.out.println("===================================");
				System.out.println("deleteBackupFiles; Path; "+dir);
				System.out.println("===================================");
			}
			for (int i=0;i<list.length;i++)
			{
				file = list[i];
				fileName = file.getName();

				if (fileName.endsWith(".bak") && file.isFile())
				{
					if (debug)
						System.out.println("Deleting "+fileName+"..");
					file.delete();
				}
			}
			if (debug)
				System.out.println("===================================");
		}
		else
		{
			System.out.println("The specified path "+dir+" is not a directory..");
		}
	}
%>

<html>
	<style>
	<%@ include file="setup.css" %>
	</style>

	<!-- ////////////////Script Start-->
	<script language="JavaScript">
		function callNextSetup()
		{
			if (document.confirm_dbconnect.backup.checked == true)
			{
				document.confirm_dbconnect.action="confirm_dbconnect.jsp?delete=true";
				document.confirm_dbconnect.submit();
			}
			else
			{
				document.confirm_dbconnect.action="confirm_productinfo.jsp";
				document.confirm_dbconnect.submit();
			}
		}

		function callPrevSetup()
		{
			document.confirm_dbconnect.action ='confirm_library.jsp';
		   document.confirm_dbconnect.submit();
		}
	</script>
	<!-- ////////////////Script End -->

	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">
		<form name="confirm_dbconnect" method="post">

		<!-- ///////////////STEP Header -->
		<table>
		<td bgcolor=#813E00  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#FFDC74  width=550 height=30><b>Report Designer5.0 Setup Confirmation</td><td bgcolor=#FF8A19 width=50 align=center><font color=#ffffff> Step 3</td>
		<tr>
		<td bgcolor=#FFEFBF  colspan=2 align=center>Test Result on RDServer DB Connections</td>
		<tr>
		<td bgcolor=#813E00  height=2 colspan=2></td>
		<tr>
		</table>
		<br>
		<!--///////////////STEP Header End -->

		<!--///////////////Navigation Button Start--->
		<table>
			<td align=left>
				<input type="checkbox" name="backup" checked><font class="style4"><%=confirm_dbconnect_deleteBackupFiles%></font>
			</td>
			<td align=right>
				<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
			<td bgcolor=#813E00  width=605 height=2 colspan=2></td>
			<tr>
		</table>
		<!--///////////////Navigation Button End--->


	DB Connections by JDBC drivers<br>
	<%
		String CONF_DIR = "";
		String rdserverHome = getServerConfigPath(out,application,request);
		String rdserversetupconfig = rdserverHome + "/rdserversetup.config";
		Vector connInfo = new Vector();
		Vector service = new Vector();
		boolean deleteBackupFiles = false;

		String delete = request.getParameter("delete");
		if (delete != null)
		{
			if (delete.equals("true"))
			{
				deleteBackupFiles = true;
			}
		}

		try
		{
// rdserversetup.config Check if file exists
			File conf_file = new File(rdserversetupconfig);
			if(conf_file.exists())
			{
				BufferedReader pin = null;
				String temp = "";

// config directory Find
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
// rdserversetup.config Does not have file
			else
			{
				out.println(writeErrMsg("ERROR : Unable to find rdserversetup.config."));
				return;
			}


			BufferedReader pin = null;
			String temp = "";
			String url = "";
			String user = "";
			String pass = "";

			// db.propertis Get the information od DB Connection from file.
			try
			{
				pin = new BufferedReader(new FileReader(CONF_DIR + "/db.properties"));
				while ((temp = pin.readLine()) != null)
				{
					if(!temp.startsWith("#") && temp.indexOf(".url=") != -1)
					{
						connInfo.addElement(temp);
						service.addElement(temp.substring(0, temp.indexOf(".")));
					}
					if(!temp.startsWith("#") && temp.indexOf(".user=") != -1)
					{
						connInfo.addElement(temp);
					}
					if(!temp.startsWith("#") && temp.indexOf(".password=") != -1)
					{
						connInfo.addElement(temp);
					}
				}

				for(int i=0; i<service.size(); i++)
				{
					for(int j=0; j<connInfo.size(); j++)
					{
						String s = (String)connInfo.elementAt(j);

						if(s.indexOf(service.elementAt(i) + ".url") != -1)
							url = s.substring(s.indexOf("=") + 1, s.length());

						if(s.indexOf(service.elementAt(i) + ".user") != -1)
							user = s.substring(s.indexOf("=") + 1, s.length());

						if(s.indexOf(service.elementAt(i) + ".password") != -1)
							pass = s.substring(s.indexOf("=") + 1, s.length());
					}

					try
					{
						// Class.forName("");
						Connection conn = DriverManager.getConnection(url, user, pass);
						java.sql.Statement stmt = conn.createStatement();
						stmt.close();
						conn.close();

						out.println(writeMsg(service.elementAt(i) + " connection success"));
					}
					catch(Exception se)
					{
						out.println(writeErrMsg(service.elementAt(i) + " connection failure - " + se.getMessage()));
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
		}
		catch(Exception e)
		{
			out.println(writeErrMsg("Error reported : " + e.getMessage()));
			return;
		}
	%>

	<p></p>
	DB Connections by RDServer
	<%
		BufferedReader pin = null;
		String serverIp = "";

		// rd.propertis Get the information of IP, port of RDServer from file.
		try
		{
			String temp = "";
			pin = new BufferedReader(new FileReader(CONF_DIR + "/rd.properties"));
			while ((temp = pin.readLine()) != null)
			{
				if(!temp.startsWith("#") && temp.indexOf("server.serverip=") != -1)
				{
					serverIp = temp.substring(temp.indexOf("=") + 1, temp.length());
				}
			}

		}
		catch (Exception exc)
		{
			out.println(writeErrMsg("Error reported while reading rd.properties : " + exc.getMessage()));
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

		// Get context path of rdserver.
		String context = request.getRequestURI();
		context = context.substring(0, context.indexOf("/setup/"));

		// Connect RDServer and test DB Connection.
		// Just Test RD Pool. Do not test WAS Pool.
		// If there does not have RD Pool, just invoke rdagent.jsp for starting RDServer. 2006.01.16
		try
		{
			int tmp = 0;

			if(service.size() <= 0)
			{
				String url = "http://" + serverIp + context + "/rdagent.jsp?opcode=150&pwkey=M2-S20040515-RDEDITOR";

				URL noCompress = new URL(url);
				HttpURLConnection huc = (HttpURLConnection)noCompress.openConnection();
				huc.setRequestMethod("POST");
				huc.setRequestProperty("user-agent", "ReportDesigner");
				huc.setRequestProperty("content-length", "0");
				huc.connect();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				InputStream is = huc.getInputStream();
				while((tmp = is.read()) != -1) {
					baos.write(tmp);
				}

				String ret = baos.toString();
				is.close();
			}

			for(int i=0; i<service.size(); i++)
			{
				String url = "http://" + serverIp + context + "/rdagent.jsp?opcode=100&pwkey=M2-S20040515-RDEDITOR&service=" + (String)service.elementAt(i);

				URL noCompress = new URL(url);
				HttpURLConnection huc = (HttpURLConnection)noCompress.openConnection();
				huc.setRequestMethod("POST");
				huc.setRequestProperty("user-agent", "ReportDesigner");
				huc.setRequestProperty("content-length", "0");
				huc.connect();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				InputStream is = huc.getInputStream();
				while((tmp = is.read()) != -1) {
					baos.write(tmp);
				}

				String ret = baos.toString();
				is.close();

				if(ret.startsWith("1"))
					out.println(writeMsg(service.elementAt(i) + " connection success"));
				else if(ret.startsWith("0"))
				{
					out.println(writeErrMsg(service.elementAt(i) + " connection failure - " + ret.substring(ret.indexOf("|") + 1, ret.length())));
				}
				else
				{
					out.println(writeErrMsg(service.elementAt(i) + " connection failure - " + ret));
				}
			}
		}
		catch (Exception exc)
		{
			out.println(writeErrMsg("RDServer connection error : " + exc.getMessage()));
			return;
		}

		if (deleteBackupFiles)
			deleteAllBackupFiles(out, rdserverHome, CONF_DIR);
	%>
		<!--///////////////Navigation Button Start--->
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