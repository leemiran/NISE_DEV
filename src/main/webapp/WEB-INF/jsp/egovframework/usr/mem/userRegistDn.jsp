<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>인증서 등록 결과</title>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript">
	<c:if test="${result}">
		alert("인증서를 등록하였습니다.");
		if("${closeYn}" == "Y"){
			top.window.close();
		}else{
			parent.document.kniseForm.epkiDn.value = "${userDn}";
			$('#loading_login', parent.document).hide();
		}
	</c:if>
	
	<c:if test="${!result}">
		alert("인증서 등록에 실패하였습니다.\n 아이디를 잘못입력 하셨거나 혹은 본인의 인증서가 맞는지 확인해주세요.");
		if("${closeYn}" == "Y"){
			top.window.close();
		}
	</c:if>
</script>
</head>
<body>

</body>
</html>