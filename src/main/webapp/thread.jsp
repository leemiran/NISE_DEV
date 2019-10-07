<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import = "java.lang.ThreadGroup" %>
<%@page import = "java.lang.Thread" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>thread count</title>
</head>
<body>
<% 

ThreadGroup tg = Thread.currentThread().getThreadGroup();
out.println("<li> Active count = " + tg.activeCount());
Thread[] t = new Thread[tg.activeCount()];
tg.enumerate(t);
for( Thread th : t) {
	out.println("<li> Thread Name = " + th.getName());
}

%>
</body>
</html>