<%@ page pageEncoding="UTF-8"%>
<c:if test="${empty sessionScope.userid}">
	<script type="text/javascript">
		<!--
		alert("개인정보보호를 위하여 아래의 경우\n자동 로그아웃되니 재로그인 하여 주십시오.\n(60분이상 미사용등)");
		if(opener)
		{
			opener.window.location.replace('/usr/mem/userLogin.do');
			window.close();
		}
		else
		{
			window.location.replace('/usr/mem/userLogin.do');
		}
		-->
	</script>
</c:if>
