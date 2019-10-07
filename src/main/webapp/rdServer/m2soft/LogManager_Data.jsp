<%@ page import="java.util.*,
		java.text.*,
		java.io.*,
		m2soft.rdsystem.server.MasterBase,
		m2soft.rdsystem.server.core.rddbagent.*,
		m2soft.rdsystem.server.log.report.*,
		m2soft.rdsystem.server.master.manager.ReportLogWriter,
		m2soft.rdsystem.server.core.rddbagent.util.RDUtil" 
		contentType ="text/html; charset=euc-kr" %><%!
		public static boolean debug = false;
%><%
	BufferedOutputStream output	= null;
	MasterBase masterBase = AgentProcess.getMasterBase();
	ReportLogWriter reportLogWriter = masterBase.getReportLogManager().getReportLogWriter();
	String[] keys = masterBase.getReportLogManager().getKeys();
	Properties[] result = reportLogWriter.getCurrentSearchResult();
	Properties ins = null;
	String key = null;
	String value = null;
	
	try
	{
		if (debug)
		{
			System.out.println("LogManager_View: debug flag is set to TRUE");
			System.out.println("LogManager_View: Size of current result: "+result.length);
			System.out.println("LogManager_View: "+result.toString());
		}
		output = new BufferedOutputStream(response.getOutputStream());
		if (debug)
			System.out.println("------------------------------------------------<br>");
		int counter = 0;
		for (int i=0; i<result.length; i++)
		{
			ins = result[i];
			if (debug)
				System.out.println("LogManager_View: "+ins.toString());
			for (int j = 0; j < keys.length; j++)
			{
				key = keys[j];
				value = ins.getProperty(key,"");
				value = RDUtil.replace(value,"\"","\'");
				
				if (key.equals("Query") && value.length() > 150)
					value = value.substring(0,150);
				
				if (value.length() > 100)
					value = value.substring(0,100);
				
				if (debug)
					System.out.println(key+"="+value+"<br>");
				output.write((value+"^").getBytes());
			}
			output.write("\n".getBytes());
			if (debug)
				System.out.println("------------------------------------------------<br>");
		}
	}
	catch (Exception e)
	{
		System.out.println("LogManager_View: Exception reported: "+e.getMessage());
		
		if (debug)
			e.printStackTrace();
	}
	finally
	{
      try
      {
      	output.flush();
      	output.close();
      }
      catch (Exception e){}
   }
%>