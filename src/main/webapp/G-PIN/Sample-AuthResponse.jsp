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
	if (request.getRemoteAddr().equals(session.getAttribute("gpinUserIP")))
	{
%>
    <!--table>
        <tr>
            <td>중복확인코드(dupInfo)</td>
            <td><%= getSession(session, "dupInfo") %></td>
        </tr>
        <tr>
            <td>개인식별번호(virtualNo)</td>
            <td><%= getSession(session, "virtualNo") %></td>
        </tr>
        <tr>
            <td>이름(realName)</td>
            <td><%= getSession(session, "realName") %></td>
        </tr>
        <tr>
            <td>성별(sex)</td>
            <td><%= getSession(session, "sex") %></td>
        </tr>
        <tr>
            <td>나이(age)</td>
            <td><%= getSession(session, "age") %></td>
        </tr>
        <tr>
            <td>생년월일(birthDate)</td>
            <td><%= getSession(session, "birthDate") %></td>
        </tr>
        <tr>
            <td>국적(nationalInfo)</td>
            <td><%= getSession(session, "nationalInfo") %></td>
        </tr>
        <tr>
            <td>본인인증방법(authInfo)</td>
            <td><%= getSession(session, "authInfo") %></td>
        </tr>
    </table //-->
<form method="post" id="frmJoin" name="frmJoin">
	<input type="hidden" name="p_process">
	
	<input type="hidden" name="p_agreement1"	value="Y"/>
	<input type="hidden" name="p_agreement2"	value="Y"/>
	<input type="hidden" name="p_agreement3"	value="Y"/>

 

	<input type="hidden" name="virtualNo" value="<%= getSession(session, "virtualNo") %>">
	<input type="hidden" name="realName" value="<%= getSession(session, "realName") %>">
	<input type="hidden" name="birthDate" value="<%= getSession(session, "birthDate") %>">
	<input type="hidden" name="authInfo" value="<%= getSession(session, "authInfo") %>">
	<input type="hidden" name="sex" value="<%= getSession(session, "sex") %>">
	<input type="hidden" name="certgubun" value="G">

</form>

	<script language="javascript">
		window.onload = function () {
			var frm = document.getElementById("frmJoin");


			//frm.p_type.value      = value;
			frm.p_process.value   = "";
			

			
			<% if(request.getLocalName().equals("127.0.0.1") || request.getLocalName().equalsIgnoreCase("localhost")) { %>
			frm.action            =  "http://localhost:8083/usr/mem/memJoinStep03.do";
			<% } else { %>
			frm.action            =  "http://iedu.nise.go.kr/usr/mem/memJoinStep03.do";
			<% }%>


			frm.submit();
		}
	</script>

<%
		
	}
	else
	{
%>
	<script language="javascript">

			window.onload = function () {
				alert("아이핀 인증을 실패하였습니다. 다시 시도 하시기 바랍니다.");
				history.back(-2);
			}

	</script>
		<!-- table>
		<tr><td>세션값을 받지 못했습니다.</td></tr>
		</table //-->
<%
	}
	%>

<!--
    <br />
    <a href="javascript:history.back(-2)">Go Back</a>
    <br />
    <a href="Sample-SessionClear.jsp">Session Clear</a>

//-->
</body>
</html>
