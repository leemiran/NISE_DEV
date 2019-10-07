<%@ page contentType="text/html; charset=euc-kr" %><%@ page language="java" import="m2soft.rdsystem.server.rdon.*, java.net.*, java.io.*, java.util.*" %><%!
	private static boolean isServerOn = false;
%><%
	request.setCharacterEncoding("euc-kr");			//Character Encoding
	if(!isServerOn) callRdagent(request);			//Call rdagent.jsp (RDServer initialization)
	
	RemoteInfo info = new RemoteInfo();
	
	Enumeration en = request.getParameterNames();

	while(en.hasMoreElements())
	{
		String key = (String)en.nextElement();
		String value = request.getParameter(key);
		info.put(key, value);
	}
	
	RDRemoteAgent agent = new RDRemoteAgent();
	
	String ret = agent.remoteReport(info);
	
	out.println(ret);

%><%!
	private void callRdagent(HttpServletRequest request) throws Exception
	{
		String url = request.getRequestURL().toString().replaceAll("rdremote.jsp", "rdagent.jsp");
		
		URL noCompress = new URL(url);
		HttpURLConnection huc = (HttpURLConnection)noCompress.openConnection();
		huc.connect();

		InputStream is = huc.getInputStream();
		int tmp = 0;
		while((tmp = is.read()) != -1) {
		}
		
		is.close();
		isServerOn = true;
	}
%>