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
    <meta content="text/html; charset=utf-8" http-equiv="content-type" />
    <title>GPIN SP - SAMPLE - 사용자 본인인증 결과</title>
</head>
<body>
<%
    /**
     * Sample-AuthRequest 를 통한 사용자인증 완료후 session에 저장된 사용자정보를 가져오는 페이지입니다.
     * Sample-AuthRequest에서 리턴페이지로 지정을 해주어야 연결되며 보여지는 항목의 상세한 내용은 가이드를참조하시기바랍니다.
     */
    // 인증 수신시 요청처와 동일한 위치인지를 session에 저장한 요청자 IP와 비교합니다.
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
	if (request.getRemoteAddr().equals(session.getAttribute("gpinUserIP"))) {
%>
<form method="post" id="frmJoin" name="frmJoin">
	<input type="hidden" name="virtualNo" value="<%= getSession(session, "virtualNo") %>">
	<input type="hidden" name="realName" value="<%= getSession(session, "realName") %>">
	<input type="hidden" name="birthDate" value="<%= getSession(session, "birthDate") %>">
	<input type="hidden" name="findGubun" value="<%= getSession(session, "findGubun") %>">
</form>

<script language="javascript">
	window.onload = function () {
		var frm = document.getElementById("frmJoin");
		
		if('ID' == '<%=getSession(session, "findGubun")%>') {
			frm.action = "/cert/gpin/findId.do";
		} else {
			frm.action = "/cert/gpin/findPwd.do";
		}
		frm.submit();
	}
</script>
<%	} else {%>
<script language="javascript">

	window.onload = function () {
		alert("아이핀 인증을 실패하였습니다. 다시 시도 하시기 바랍니다.");
		history.back(-2);
	}

</script>
<%	}%>
</body>
</html>
