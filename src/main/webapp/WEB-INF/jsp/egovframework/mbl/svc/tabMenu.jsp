<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
	String url = request.getAttribute("javax.servlet.forward.request_uri").toString();
	if ( request.getQueryString() != null )
	//url = url + "?" + request.getQueryString();
	//url = url.replaceAll("&", "||");
	
	//out.println(url);
%>
		<ul class="tab mrt10">
		
		<%
			if(url.indexOf("/mbl/svc/notice") == -1)
			{
		%>
			<li><a href="noticeList.do">공지사항</a></li>
		<% 
			}
			else
			{
		%>
			<li><a href="noticeList.do" class="on">공지사항</a></li>
		<% 
			}
		%>
		<%
			if(url.indexOf("/mbl/svc/faq") == -1)
			{
		%>
			<li><a href="faqList.do">자주묻는질문</a></li>
		<% 
			}
			else
			{
		%>
			<li><a href="faqList.do" class="on">자주묻는질문</a></li>
		<% 
			}
		%>
		<%
			if(url.indexOf("/mbl/svc/data") == -1)
			{
		%>
			<!-- <li><a href="dataList.do">자료실</a></li> -->
		<% 
			}
			else
			{
		%>
			<!-- <li><a href="dataList.do" class="on">자료실</a></li> -->
		<% 
			}
		%>		
			
		</ul>
