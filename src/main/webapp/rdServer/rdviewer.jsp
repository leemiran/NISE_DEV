<%@page import="java.io.*,java.net.*,m2soft.rdsystem.server.core.rddbagent.util.*"%>
<%@include file="m2soft/properties.h"%>
<%!
	public void jspInit()
	{
		try
		{
			String url = "http://serverip:port/RDServer/rdagent.jsp";

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
	}
%>
<%
  String mrdpath = request.getParameter("mrdpath"); //open ÇÒ mrd ÀÌ¸§
  String param = request.getParameter("param");
  int enc_type = 0;
  try
  {
	  String enctype = request.getParameter("enc_type");
	  enc_type = Integer.parseInt(enctype);
  }
  catch (Exception e)
  {
  }
  mrdpath = new String(A.unprocess(mrdpath,enc_type));
  param = new String(A.unprocess(param,enc_type));
%>
<HTML>
<HEAD><TITLE>Report Desinger5.0  Server Side Export example</TITLE></HEAD>
<BODY leftmargin=0 topmargin=0 onLoad="javascript:rdOpen()" padding=0>
<script src="rdviewer.js"></script>
<SCRIPT>
function rdOpen()
{
   Rdviewer.SetBackgroundColor(255,255,255);
   Rdviewer.SetPageLineColor(255,255,255);
   Rdviewer.SetPageColor(255,255,255);
   Rdviewer.HideToolBar();
   Rdviewer.AutoAdjust=0;
   Rdviewer.ApplyLicense("http://serverip:port/RDServer/rdagent.jsp");
   Rdviewer.FileOpen("<%=mrdpath%>","<%=param%>");
}
</SCRIPT>

</BODY>
</HTML>