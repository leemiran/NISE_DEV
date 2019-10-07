<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<head>
<title>Innorix File Transfer Solution</title>
</head>

<body>
<font size="5"><b>전송결과</b></font> - <a href="javascript:;" onclick="history.back();">다른파일 전송하기</a>
<br /><br />

test1 : <%=request.getParameter("test1")%><br /><br />

<table width="570" border="0" cellspacing="1" cellpadding="3" bgcolor="#c0c0c0">
<tr bgcolor="#ebe8d6" align="center" height="25">
	<td nowrap>원본 파일명</td>
	<td nowrap>저장 파일명</td>
	<td nowrap>저장 폴더명</td>
	<td nowrap>파일 사이즈</td>
	<td nowrap>개발자 정의값</td>
	<td nowrap>컴포넌트 ID</td>	
</tr>
<%
request.setCharacterEncoding("utf-8");
String[] origfilename = request.getParameterValues("_innorix_origfilename"); 	// 원본 파일명
String[] savefilename = request.getParameterValues("_innorix_savefilename"); 	// 저장 파일명
String[] savepath = request.getParameterValues("_innorix_savepath"); 			// 파일 저장경로
String[] filesize = request.getParameterValues("_innorix_filesize"); 			// 파일 사이즈
String[] foldername = request.getParameterValues("_innorix_folder"); 			// 폴더정보(폴더 업로드시만)
String[] customvalue = request.getParameterValues("_innorix_customvalue"); 		// 개발자 정의값
String[] componentname = request.getParameterValues("_innorix_componentname"); 	// 컴포넌트 이름

if (origfilename != null)
{
	for (int i = 0; i < origfilename.length; i++)
	{
		/* 여기에 업로드 파일 정보를 DB에 입력하는 코드를 작성 합니다. */
%>
<tr bgcolor="#ffffff" align="left" height="22">
	<td nowrap><%=origfilename[i]%></td>
	<td nowrap><%=savefilename[i]%></td>
	<td nowrap><%=savepath[i]%></td>
	<td nowrap align="right"><%=filesize[i]%></td>
	<td nowrap><%=customvalue[i]%></td>
	<td nowrap><%=componentname[i]%></td>
</tr>
<%
	}
}
%>
</table>

<%
String[] strExistID = request.getParameterValues("_innorix_exist_id"); 		// 존재하는 파일ID
String[] strExistName = request.getParameterValues("_innorix_exist_name");	// 존재하는 파일이름
String[] strExistSize = request.getParameterValues("_innorix_exist_size");	// 존재하는 파일용량

if (strExistID != null)
{
%>
<br />
<table width="570" border="0" cellspacing="1" cellpadding="3" bgcolor="#c0c0c0">
<tr bgcolor="#ebe8d6" align="center" height="25">
	<td nowrap>존재하는 파일ID</td>
	<td nowrap>존재하는 파일이름</td>
	<td nowrap>존재하는 파일용량</td>
</tr>
<%
	for (int i = 0; i < strExistID.length; i++)
	{
		/* 여기에 삭제된 파일 정보를 받아 파일을 삭제하는 코드를 작성 */
%>
<tr bgcolor="#ffffff" align="left" height="22">
	<td><strong></strong><%=strExistID[i]%></td>
	<td><%=strExistName[i]%></td>
	<td align="right"><%=strExistSize[i]%></td>
</tr>
<%
	}
}
%>
</table>

<%
String[] strDeletedID = request.getParameterValues("_innorix_deleted_id"); // 삭제된 파일ID
String[] strDeletedName = request.getParameterValues("_innorix_deleted_name"); // 삭제된 파일이름
String[] strDeletedSize = request.getParameterValues("_innorix_deleted_size"); // 삭제된 파일용량

if (strDeletedID != null)
{
%>
<br />
<table width="570" border="0" cellspacing="1" cellpadding="3" bgcolor="#c0c0c0">
<tr bgcolor="#ebe8d6" align="center" height="25">
	<td nowrap>삭제된 파일ID</td>
	<td nowrap>삭제된 파일이름</td>
	<td nowrap>삭제된 파일용량</td>
</tr>
<%
	for (int i = 0; i < strDeletedID.length; i++)
	{
		/* 여기에 삭제된 파일 정보를 받아 파일을 삭제하는 코드를 작성 */
%>
<tr bgcolor="#ffffff" align="left" height="22">
	<td><strong></strong><%=strDeletedID[i]%></td>
	<td><%=strDeletedName[i]%></td>
	<td align="right"><%=strDeletedSize[i]%></td>
</tr>
<%
	}
}
%>
</table>
</body>
</html>