<%@page language="java" contentType="text/html; charset=UTF-8"%>	
<script>
window.onload = function () {
	if('true' == '${isCert}') {
		alert('인증에  성공하였습니다.');
		document.resEpkiFrm.action='${pageContext.request.contextPath}/usr/mem/memJoinStep03.do';
		document.resEpkiFrm.target='_self';
		document.resEpkiFrm.submit();
	} else {
		alert('유효하지 않은 인증서입니다.');
		history.back();
	}
}
</script>

<form name="resEpkiFrm" method="post">
	<input type="hidden" name="certgubun" value="E"/> <!-- EPKI 인증: E -->
</form>