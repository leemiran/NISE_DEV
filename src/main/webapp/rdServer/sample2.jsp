<%@ page contentType="text/html; charset=euc-kr" %> 
<%@ page language="java" import="java.io.*, java.util.*, java.net.*" %>
<%!
	String getStackTraceAsString(Throwable e)
	{
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		StringBuffer error = stringWriter.getBuffer();

		return error.toString();
	}
%>

<%
	String mrd = "http://27.101.217.158/mrd/eduCerti.mrd"; // ������(ex> test)
//	String mrdparams = "/rf [http://27.101.217.158/rdServer/MRD/sample.xml]"; // �Ķ���� ��(ex> �Ķ���͸� ������ �ѱ�� ��� : 19990202:::444:::555
    //String mrdparams = "/rf [http://27.101.217.158/rdServer/MRD/sample.xml]"; // �Ķ���� ��(ex> �Ķ���͸� ������ �ѱ�� ��� : 19990202:::444:::555
    String mrdparams = "/rp [RDA][E1Y110002][2][yooktge]";
//        String mrdparams = "/rf RDA:1:1:1";
 	String exportname = "test2";
	String rdserver_url = "http://27.101.217.158/rdServer/rdremote.jsp";
	String rdonip = "27.101.217.158";
	String rdonport = "6585";
	String exporttype = "pdf";
	String opcode = "2";
	String RDAGENT_URL = "/rf [http://27.101.217.158/rdServer/rdagent.jsp]";
	 
   try
   {
      /*
      * HTTP Connection ���
      */
      HttpURLConnection conn = null;
      URL url = new URL(rdserver_url);
      conn = (HttpURLConnection)url.openConnection();
      conn.setUseCaches(false);
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("POST");
      conn.connect();

      /*
      * Request Parameter �����
      */
      StringBuffer requestparambuffer = new StringBuffer();

      requestparambuffer.append("rdonip=");
      requestparambuffer.append(URLEncoder.encode(rdonip, "euc-kr"));
      requestparambuffer.append("&");

      requestparambuffer.append("rdonport=");
      requestparambuffer.append(URLEncoder.encode(rdonport, "euc-kr"));
      requestparambuffer.append("&");

      requestparambuffer.append("mrd=");
      requestparambuffer.append(URLEncoder.encode(mrd, "euc-kr"));
      requestparambuffer.append("&");
      
      requestparambuffer.append("mrdparams=");
      requestparambuffer.append(URLEncoder.encode(mrdparams, "euc-kr"));
      requestparambuffer.append("&");

      requestparambuffer.append("exporttype=");
      requestparambuffer.append(URLEncoder.encode(exporttype, "euc-kr"));
      requestparambuffer.append("&");

      requestparambuffer.append("exportname=");
      requestparambuffer.append(URLEncoder.encode(exportname, "euc-kr"));
      requestparambuffer.append("&");

      requestparambuffer.append("opcode=");
      requestparambuffer.append(URLEncoder.encode(opcode, "euc-kr"));
      requestparambuffer.append("&");

      String requestparam = requestparambuffer.toString();

      /*
      * Request ������
      */
      OutputStream os = conn.getOutputStream();
      os.write(requestparam.getBytes("euc-kr"));
      os.flush();
      os.close();

      /*
      * Response �޾ƿ���
      */
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      InputStream is = conn.getInputStream();
      int tmp = 0 ;
      while ((tmp = is.read()) != -1) 
      {
         baos.write(tmp);
      }
      is.close();
      
      conn.disconnect();
      
      /* 
      * ȭ�鿡 ����ϱ�
      */
      out.println(new String(baos.toByteArray()));
   }
   catch (Throwable e)
   {
      out.println ("<pre>");
      out.println ("Error:");
      out.println (getStackTraceAsString(e));
      out.println ("</pre>");
   }
%>
