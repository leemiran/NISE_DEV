<%@ page import="java.io.*,java.util.*,java.util.jar.*" contentType="text/html; charset=euc-kr" %>
<%@ include file="commonlib.h"%>
<html>
	<style>
	<%@ include file="setup.css" %>
	</style>

<%
	String opcode = request.getParameter("opcode");


	if(opcode != null && opcode.equals("setup6"))
	{
		String tmpRDHomeDir = getServerConfigPath(out,application,request);

		String setupfilename = "/rdserversetup.config";

		File f = new File(tmpRDHomeDir + setupfilename);

		String maxfilenum							= "log.maxfilenum=" + request.getParameter("maxfilenum");
		String intervalofsweeper					= "log.intervalofsweeper=" + request.getParameter("intervalofsweeper");
		String errordirection						= "log.errordirection=" + request.getParameter("errordirection");
		String errorlevel							= "log.errorlevel=" + request.getParameter("errorlevel");
		String moniteringlog						= "log.moniteringlog=" + request.getParameter("moniteringlog");
		String moniteringlogLevel					= "log.moniteringlogLevel=" + request.getParameter("moniteringlogLevel");
		String moniteringlogServiceName				= "log.moniteringlogServiceName=" + request.getParameter("moniteringlogServiceName");
		String refreshMileTime						= "log.refreshMileTime=" + request.getParameter("refreshMileTime");
		//sjsung 2005-06-30 LogManager
		String writereportlog						= "log.writereportlog=" + request.getParameter("writereportlog");
		String reportlogmethod						= "log.reportlogmethod=" + request.getParameter("reportlogmethod");
		String reportlogdir							= "log.reportlogdir=" + request.getParameter("reportlogdir");
		//kokim 2006.02.28 Do not set the below items in Setup.
		//String ejb_jndiname							= "log.ejb_jndiname=" + request.getParameter("ejb_jndiname");
		//String ejb_methodname						= "log.ejb_methodname=" + request.getParameter("ejb_methodname");
		String ejb_jndiname							= "log.ejb_jndiname=";
		String ejb_methodname						= "log.ejb_methodname=";

		String maxfilenum_comment					= "#Maximum number of log files";
		String intervalofsweeper_comment			= "#Time interval of Log Sweeper [default 180 minutes]";
		String errordirection_comment				= "#Option of showing errors on console or saving to file";
		String errorlevel_comment					= "#Level of saving error (debug option enables to save the most detailed info.[debug < info < error < none])";
		String moniteringlog_comment				= "#Option of whether saving monitoring log file or not";
		String moniteringlogLevel_comment			= "#Level of saving monitoring log";
		String moniteringlogServiceName_comment		= "#Monitoring service name";
		String refreshMileTime_comment				= "#Time interval of reloading error log";
		//sjsung 2005-06-30 LogManager
		String writereportlog_comment				= "#LogManager, option to specify a use of LogManager (TRUE or FALSE)";
		String reportlogmethod_comment				= "#LogManager, option to specify a logging method (File or DB)";
		String reportlogdir_comment					= "#LogManager, report log directory path";
		String ejb_jndiname_comment					= "#log EJB exportname ex)RDLogEJB";
		String ejb_methodname_comment				= "#ejb EJB method available type : void logmethod(String mainquery,String rpparam); ex)logmethod";


//Symmetry comment array and the corresponding key values 
		String[] comments = {maxfilenum_comment,intervalofsweeper_comment,errordirection_comment,errorlevel_comment,moniteringlog_comment,moniteringlogLevel_comment,moniteringlogServiceName_comment,refreshMileTime_comment,writereportlog_comment,reportlogmethod_comment,reportlogdir_comment,ejb_jndiname_comment,ejb_methodname_comment};

		String[] keyandvalues = {maxfilenum,intervalofsweeper,errordirection,errorlevel,moniteringlog,moniteringlogLevel,moniteringlogServiceName,refreshMileTime,writereportlog,reportlogmethod,reportlogdir,ejb_jndiname,ejb_methodname};

		Properties ps = new Properties();

		FileInputStream inputstream = new FileInputStream(f);

		String prp_path = "";

		boolean ret = true;

		try
		{
			ps.load(inputstream);
			prp_path = ps.getProperty("RDSERVER_PROPERTEIS_DIR",null);
			if(prp_path == null || prp_path.equals(""))
			{
				prp_path = tmpRDHomeDir+"/conf/log.properties";
			}
			else
			{
				prp_path += "/log.properties";
			}

			File rdprop_file = new File(prp_path);


//back up and save 
			if(rdprop_file.exists())
			{
				String bakupf = prp_path+System.currentTimeMillis()+".bak";
				copyFile(prp_path,bakupf);

				//read mode
				if(!rdprop_file.canWrite()){
					rdprop_file.delete();
				}
			}
//end

			PrintWriter outwriter = null;

			try
			{
				outwriter  = new PrintWriter(new BufferedWriter(new FileWriter(prp_path)));

				outwriter.println("#Update Time: Reports Designer Server - log.properties " + new java.text.SimpleDateFormat("'['yyyy-MM-dd'] 'a' 'hh:mm:ss.SS' '").format( new java.util.Date()));
				outwriter.println("");

				for (int i = 0; i< comments.length ; i++)
				{
					outwriter.println(comments[i]);
					outwriter.println(keyandvalues[i]);
					outwriter.println();
				}

			}
			catch (Exception ex)
			{
				out.println ("Error: <font color=red><pre>"+ getStackTraceAsString(ex));
				ret = false;
			}
			finally
			{
				if(outwriter != null)
					outwriter.close();
			}

			if(!ret)
				return;

		}
		catch (FileNotFoundException ex)
		{
			out.println("Error: Unable to read the property files previously defined at RDServer Setup - Step 1. "+ ex.toString());
			ret = false;
		}
		finally
		{
			if(inputstream != null)
				inputstream.close();
		}

		if(ret)
		{
			pageContext.forward("setup7.jsp");
		}
	}
%>
<!-- ////////////////start script -->
	<script language="JavaScript">

		function callNextSetup()
		{
		   document.setup6.submit();
		}

		function callPrevSetup()
		{
			document.setup6.action ='setup5.jsp';
		   document.setup6.submit();
		}

      function OnClickServiceList()
      {
         document.setup6.moniteringlogServiceName.value = document.setup6.servicelist.value;
      }
	</script>
<!-- ////////////////script end -->

	<body class="header" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">
	   <form name="setup6" method="post" action="setup6.jsp">
		<input type="hidden" name="opcode" value="setup6">

 <!-- ///////////////STEP HEADER -->
		<table>
		<td bgcolor=#3399CC  width=600 height=2 colspan=2></td>
		<tr>
		<td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Step 6</td>
		<tr>
		<td bgcolor=#DBDBEA  colspan=2 align=center>RDServer Environment Setup (log)</td>
		<tr>
		<td bgcolor=#3399CC  height=2 colspan=2></td>
		<tr>
		</table>
		<br>
<!--///////////////STEP HEADER END -->

<!--///////////////Navigation Buttion Begining--->
		<table>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
			</td>
			<tr>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
		</table>
<!--///////////////Navigation Buttion End --->

<!-- ////////////Setting log.properties -->

	<%
	String file="",db="";	file = "checked";
//	String serveripport = java.net.InetAddress.getLocalHost().getHostAddress()+":"+request.getServerPort();
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

	   for (Enumeration e = prop.propertyNames() ; e.hasMoreElements() ;) {
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
<!--///////////////Log Setting Table-->
		<table>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#Maximum number of log files</td>
		<tr>
		<td width=200>logMaxNum</td>
		<td width=400><input type ="text" name="maxfilenum" size=76 value="30" class="style2"></td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#Time interval of Log Sweeper [default 180 minutes]</td>
		<tr>
		<td width=200>intervalOfSweeper</td>
		<td width=400><input type ="text" name="intervalofsweeper" size=76 value="180" class="style2"></td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#Option of showing errors on console or saving to file</td>
		<tr>
		<td width=200>errordirection</td>
		<td width=400><input type ="text" name="errordirection" size=76 value="file" class="style2"></td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#Level of saving error (debug option enables to save the most detailed info.[debug < info < error < none])</td>
		<tr>
		<td width=200>errorlevel</td>
		<td width=400><input type ="text" name="errorlevel" size=76 value="debug" class="style2"></td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#Option of whether saving monitoring log file or not</td>
		<tr>
		<td width=200>moniteringlog</td>
		<td width=400><input type ="text" name="moniteringlog" size=76 value="true" class="style2"></td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#Level of saving monitoring log</td>
		<tr>
		<td width=200>moniteringlogLevel</td>
		<td width=400><input type ="text" name="moniteringlogLevel" size=76 value="0" class="style2"></td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#Monitoring service name</td>
		<tr>
		<td width=200>moniteringlogServiceName</td>
      <%

         if(v_serviceList.size() > 0)
         {
		      out.println("<td width=400><input type =\"text\" name=\"moniteringlogServiceName\" size=30 value=\""+(String)v_serviceList.get(0)+"\" class=\"style2\">");

            out.println("<select name=\"servicelist\" onClick=\"OnClickServiceList()\">");
            for (int i = 0; i< v_serviceList.size(); i++)
                  out.println ("<option value=\""+(String)v_serviceList.get(i) +"\">"+(String)v_serviceList.get(i)+"</option>");
            out.println("</select>");
	      }
			else
			{
				out.println("<td width=400><input type =\"text\" name=\"moniteringlogServiceName\" size=30 value=\"\" class=\"style2\">");
			}
      %>
   	</td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#Time interval of reloading error log</td>
		<tr>
		<td width=200>refreshMileTime</td>
		<td width=400><input type ="text" name="refreshMileTime" size=76 value="60" class="style2"></td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#LogManager, option to specify a use of LogManager (TRUE or FALSE)</td>
		<tr>
		<td width=200>writereportlog</td>
		<td width=400><input type ="text" name="writereportlog" size=76 value="FALSE" class="style2"></td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#LogManager, option to specify a logging method (File or DB)</td>
		<tr>
		<td width=200>reportlogmethod</td>
		<td width=400><input type ="text" name="reportlogmethod" size=76 value="File" class="style2"></td>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#LogManager, report log directory path</td>
		<tr>
		<td width=200>reportlogdir</td>
		<td width=400><input type ="text" name="reportlogdir" size=76 value="<%=getServerConfigPath(out, application, request)%>/ReportLog" class="style2"></td>
		<tr>
		<!-- kokim 2006.02.28 Do not set the below items in Setup.
		<td width=600 bgcolor=#EFDFFF colspan=2>#log EJB exportname ex)RDLogEJB</td>
		<tr>
		<td width=200>ejb_jndiname</td>
		<td width=400><input type ="text" name="ejb_jndiname" size=76 value="" class="style2"></td>
		<tr>
		<tr>
		<td width=600 bgcolor=#EFDFFF colspan=2>#ejb EJB method available type : void logmethod(String mainquery,String rpparam); ex)logmethod</td>
		<tr>
		<td width=200>ejb_methodname</td>
		<td width=400><input type ="text" name="ejb_methodname" size=76 value="" class="style2"></td>
		<tr>
		-->
		</table>
<!--///////////////End -->

<!--///////////////Navigation Buttion Begining--->
		<table>
			<td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
			<tr>
			<td align=right>
			<a href="javascript:callPrevSetup();"><img src="images/prev_up.gif" border=0 hspace=10></a><a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
	   </td>

		</table>
<!--///////////////Navigation Buttion End --->

		</form>
	</body>
</html>