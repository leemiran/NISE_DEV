<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
	<c:when test="${resultMap.msgChk eq 'N' }">
		<script>
		window.onload = function () {
			alert("비정상적인 접근입니다.!!");
			self.close();
		}
	    </script>
	</c:when>
	<c:when test="${resultMap.msgChk eq 'Y' }">
		<script>
		window.onload = function () {
		    window.opener.name = "parentPage"; // 부모창의 이름 설정
		    document.resChkPlusFrm.target = "parentPage"; // 타켓을 부모창으로 설정 "_top"; // "_self"; //
		    document.resChkPlusFrm.action = "${pageContext.request.contextPath}${retUrl}";
		    document.resChkPlusFrm.submit();
		    self.close();
			// '${resultMap.result}' // 인증성공여부
			// '${resultMap.cellNo}' // 핸드폰번호
			// '${resultMap.cellCorp}' // 이동통신사
		}
		</script>
		
		<form name="resChkPlusFrm" method="post">
			<c:choose>
				<c:when test="${retType eq 'join' }">
					<input type="hidden" name="realName" value="${resultMap.name}"/>
					<input type="hidden" name="sex" value="${resultMap.sex}"/>
					<input type="hidden" name="birthDate" value="${resultMap.birYMD}"/>
					<input type="hidden" name="NATIONAINFO" value="${resultMap.fgnGbn}"/>
					<input type="hidden" name="DI" value="${resultMap.di}"/>
					<input type="hidden" name="REQ_SEQ" value="${resultMap.reqNum}"/>
					<input type="hidden" name="AUTH_TYPE" value="${resultMap.certGb}"/>
					<input type="hidden" name="sCipherTime" value="${fn:substring(resultMap.certDate, 2, 14)}"/>
					<input type="hidden" name="certgubun" value="C"/> <!-- CHECKPLUSSAFE 본인인증: C -->
					<input type="hidden" name="virtualNo" value=""/> <!-- 빈값 --><!-- ipin -->
					<!-- <input type="hidden" name="RES_SEQ" value=""/> -->
					<!-- <input type="hidden" name="CI" value=""/> -->
				</c:when>
				<c:when test="${retType eq 'dormant' }">
					<input type="hidden" name="p_userid_ok" value="${resultMap.addVar}"/> <!-- 빈값 --><!-- ipin -->
					<input type="hidden" name="birthDate" value="${resultMap.birYMD}"/>
				</c:when>
			</c:choose>
		</form>
	</c:when>
	<c:otherwise>
		<script>
		window.onload = function () {
			self.close();
		}
	    </script>
	</c:otherwise>
</c:choose>