<%@ page import="java.io.*,m2soft.rdsystem.server.master.manager.*,m2soft.rdsystem.server.core.batchjob.*,m2soft.rdsystem.server.master.event.*,m2soft.rdsystem.server.core.install.Message"%>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%!
	public static String getStackTraceAsString(Throwable e)
	{
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		StringBuffer error = stringWriter.getBuffer();
		return error.toString();
	}
%>
<%
	try
	{
		String jobName = request.getParameter("jobname");
		if(jobName != null)
		{
			JobManager manager = JobManagerFactory.getJobManager();
			SyncResult ret = manager.invokeJob(jobName);
			if(ret.isOK())
			{
				out.println("1|success");
			}
			else
			{
				out.println("0|" + ret.getObject());
			}
		}
		else
		{
			out.println("0|Not enough parameters");
		}
	}
	catch(Throwable e)
	{
		out.println("0|"+getStackTraceAsString(e));
	}
%>