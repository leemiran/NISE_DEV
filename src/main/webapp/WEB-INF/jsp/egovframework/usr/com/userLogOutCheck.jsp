<%@ page pageEncoding="UTF-8"%>
<c:if test="${not empty sessionScope.userid}">
	<script language="javascript" type="text/javascript">
		//<![CDATA[
		alert("이미 로그인 되어 있습니다.");
		window.location.replace('/');
		//]]>
	</script>
</c:if>
