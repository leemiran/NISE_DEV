<%@ page language = "java" contentType = "text/html; charset=UTF-8"%>

<%!
    String getSession(HttpSession session, String attrName)
    {
        return session.getAttribute(attrName) != null ? (String)session.getAttribute(attrName) : "";
    }
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta content="text/html; charset=UTF-8" http-equiv="content-type" />
    <title>GPIN SP - SAMPLE - MYPIN</title>
</head>
<body>
<%
	if (request.getRemoteAddr().equals(session.getAttribute("gpinUserIP")))
	{
%>
    <table> 
        <tr>
            <td>중복확인코드(dupInfo)</td>
            <td><%= getSession(session, "dupInfo") %></td>
        </tr>
        <tr>
            <td>연계정보(coinfo1)</td>
            <td><%= getSession(session, "coInfo1") %></td>
        </tr>
        <tr>
            <td>연계정보 변경시(coinfo2)</td>
            <td><%= getSession(session, "coInfo2") %></td>
        </tr>
        <tr>
            <td>CI갱신횟수(ciupdate)</td>
            <td><%= getSession(session, "ciupdate") %></td>
        </tr>
        <tr>
            <td>개인식별번호(virtualNo)</td>
            <td><%= getSession(session, "virtualNo") %></td>
        </tr>
         <tr>
            <td>상태값(status)</td>
            <td><%= getSession(session, "status") %></td>
        </tr>
    </table>
<%
	}
	else
	{
%>
		<table>
		<tr><td>세션값을 받지 못했습니다.</td></tr>
		</table>
<%
	}
	%>


    <br />
    <a href="Sample-index.jsp">Go Back</a>
    <br />
    <a href="Sample-MyPinSessionClear.jsp">Session Clear</a>
</body>
</html>