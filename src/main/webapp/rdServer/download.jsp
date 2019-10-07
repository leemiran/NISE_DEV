<%@page import="m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message,java.io.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*"%><%!RdPropertyManager rp = null;
	String webpath = null;
	String encoding = null;
	String userdefinecharset = null;
	boolean downencoding = true;
%><%
	try {
		rp = RdPropertyManager.getInstance();
		if (rp == null) {
			RdPropertyManager.init(RDUtil.getRdPropPath());
			rp = RdPropertyManager.getInstance();
		} else if (rp.getProperty("server.webpath") == null) {
			//sjs 04.08
			RdPropertyManager.decodeInstance();
			rp = RdPropertyManager.getInstance();
		}
		webpath = rp.getProperty("server.webpath", "");
		encoding = rp.getProperty("server.encoding");
		userdefinecharset = rp.getProperty("server.userdefinecharset");
	} catch (Exception e) {
		e.printStackTrace();
	}
%><%
	String downName = request.getParameter("filename");
	String schedulefile = request.getParameter("sh");
	InputStreamReader in = null;
	FileInputStream fis = null;
	OutputStream outs = null;
	int bufferSize = 4096;
	try {
		int nRead = 0;
		byte buffer[] = new byte[bufferSize];
		
		if (downName == null || downName.indexOf("..") != -1)
			return;
		
		if (downencoding)
			downName = RDUtil.toHangleDecode(downName, userdefinecharset);
		
		String fname = downName.substring(downName.lastIndexOf("/") + 1);
		String waskind = getServletConfig().getServletContext().getServerInfo().toLowerCase();
		
		if (downName.length() > 0) {
			if ((waskind.indexOf("jeus") == -1) && (fname.endsWith(".htm") || fname.endsWith(".txt") || fname.endsWith(".log"))) {

				String path = "";

				if (fname.endsWith(".log") || (fname.endsWith(".txt") && (schedulefile == null || schedulefile.equals(""))))
					path = downName;
				else
					path = webpath + "/" + downName;

				if (L.isUnicodeVersion() && fname.endsWith(".txt"))
					in = new InputStreamReader(new FileInputStream(path), "UnicodeLittle");
				else
					in = new InputStreamReader(new FileInputStream(path), userdefinecharset);

				boolean encoding = false;

				if (waskind.indexOf("tomcat") != -1)
					response.setContentType(Message.get("RDUtil_CharSettomcat"));
				else if (waskind.indexOf("apachejserv") != -1)
					encoding = true;
				else if (waskind.indexOf("resin") != -1) {
					encoding = true;
					response.setContentType(Message.getcontentType());
				}else if (waskind.indexOf("oracle9i") != -1 || waskind.indexOf("websphere") != -1) {
					if (fname.endsWith(".htm"))
						response.setContentType("text/html; charset=" + Message.get("RDUtil_CharSet"));
					else
						response.setContentType(Message.getcontentType());
					encoding = true;
				}else
					response.setContentType(Message.getcontentType()); //text/html; charset=EUC_KR

				if (L.isUnicodeVersion() && ((fname.endsWith(".txt")) || (fname.endsWith(".log"))))
					response.setContentType(Message.getcontentType());

				String readString;
				int rsize;
				char[] buf = new char[1024];

				if (encoding) {
					while ((rsize = in.read(buf, 0, 1024)) != -1) {
						out.print(RDUtil.toHangleDecode(new String(buffer, 0, rsize))); //in the case of webshpere5.0.2.5 //file.enconding:ms949,writer encoding:null, change the RDUtil.toHangleDecode() with RDUtil.toHangleEncode.
					}
				} else {
					while ((rsize = in.read(buf, 0, 1024)) != -1) {
						out.print(new String(buf, 0, rsize));
					}
				}
			} else {

				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=" + fname + ";");
				pageContext.getOut().clear();
				outs = response.getOutputStream();
				fis = new FileInputStream(webpath + "/" + downName);
				if (fis.available() == 0)
					outs.write(0x00);
				else {
					while ((nRead = fis.read(buffer)) != -1) {
						outs.write(buffer, 0, nRead);
					}
				}
			}
		}

	} catch (Exception e) {
		System.out.println("RDServer/download.jsp : " + e.toString());
	} finally {
		if (in != null)
			in.close();
		if (fis != null)
			fis.close();
		if (outs != null) {
			outs.flush();
			outs.close();
		}
	}
%>