<%@ page import="java.io.*,java.net.*,java.util.*,m2soft.rdsystem.server.servlet.*,m2soft.rdsystem.server.requestservice.*,m2soft.rdsystem.server.*"%><%!
	RDServerExportServletManager server_export = null;
	public void jspInit()
	{
		try{
			String url = "http://27.101.217.158:80/rdServer/rdagent.jsp";

			URL noCompress = new URL(url);
			HttpURLConnection huc = (HttpURLConnection)noCompress.openConnection();
			huc.setRequestMethod("POST");
			huc.setRequestProperty("user-agent", "ReportDesigner");
			huc.setRequestProperty("content-length", "0");
			huc.connect();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = huc.getInputStream();
			int tmp = 0;
			while((tmp = is.read()) != -1) {
				baos.write(tmp);
			}
			String ret = baos.toString();
			is.close();
		}
		catch (Throwable e)
		{
		}
  		server_export  = new RDServerExportServletManager();
	}
	public void jspDestroy()
	{
	}
	public static String getStackTraceAsString(Throwable e)
	{
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		StringBuffer error = stringWriter.getBuffer();
		return error.toString();
	}
%><%
	try
	{
		if(server_export  != null)
			server_export.process(out,new RDServletHttpResponseImpl(response),new RDServletHttpRequestImpl(request));
	}
	catch (Throwable e)
	{
		out.println("0|502|"+getStackTraceAsString(e));
	}%>