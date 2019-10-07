<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<body>
		<table>
			<tbody>
				<c:forEach var="smsTo" items="${smsToList}" varStatus="status" end="99">
					<tr>
						<td class="no">${status.index + 1}</td>
						<td>${smsTo.phoneNumber}<input type="hidden" name="phoneNumber" value="${smsTo.phoneNumber}"/></td>
						<td>${smsTo.name}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<strong class="point">${fn:length(smsToList)}</strong>
		<div class="errorFile">${errorFileName}</div>
		<div class="errorCnt">${errorCnt}</div>
	</body>
</html>