<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.net.URL" %>
<%@ page import="java.io.*" %>
<%
	String xmlPath = request.getParameter("path");
	File xmlFile = new File(xmlPath);
	
	FileInputStream fis = new FileInputStream(xmlFile);
	BufferedReader bis = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
	String text = bis.readLine();
	while(text != null){
		out.println(text);
		text=bis.readLine();
	}
%>