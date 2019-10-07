<%@ page language="java" %><%@ page contentType="application/octet-stream" %><%@ page import="java.io.*" %><%
  	BufferedReader in = null;
 	String inputLine = null;
 	int icount = 0;
  	try {
 	    in = new BufferedReader(new FileReader(request.getRealPath("/initech/plugin/cert.js")));
 	    icount = 0;
 	    StringBuffer sb = new StringBuffer();
 	    while ((inputLine = in.readLine()) != null) {
 	        inputLine = inputLine.trim();
 	        if (inputLine.indexOf("SCert") != -1) {
				int sinx = inputLine.indexOf("\"");
				int einx = inputLine.lastIndexOf("\"");
				if(sinx != -1 && einx != -1)
				{
					String pCertStr = inputLine.substring(sinx+1,einx);
					out.print(pCertStr);
				}
				break;
 	        }
  	        icount++;
  	    }
 	} catch (Exception e) {
 	    e.printStackTrace();
 	} finally {
 	    if (in != null) {
 	        try {
 	            in.close();
 	        } catch (IOException e1) {
 	            
 	            e1.printStackTrace();
 	        }
 	    }
	}
%><%!
 	public String replace(String body, String from, String to) {

        String strBody = body;

        if (strBody == null || strBody.trim().equals("")) return body;
        if (from == null || to == null) return body;

        String a = "", b = "";
        int idxStart, idxEnd, col;
        int col_from = from.length();
        int col_to = to.length();

        idxStart = strBody.indexOf(from);
        idxEnd = idxStart + col_from;

        while (idxStart != -1) {
            col = idxEnd - idxStart - 1;
            a = strBody.substring(0, idxStart);
            b = strBody.substring(idxEnd);
            strBody = a + to + b;
            idxStart = strBody.indexOf(from);
            idxEnd = idxStart + col_from;
        }

        return strBody;
    }
%>