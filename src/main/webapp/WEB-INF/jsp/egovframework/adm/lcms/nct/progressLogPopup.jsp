<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.doSelectBrowser();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:435px">
    	<div class="tit_bg">
			<h2>학습로그</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" id="subj" name="subj" value="${subj}"/>
		
		<!-- contents -->
		<!-- button -->
		<div class="listTop">			
		    <div class="btnR"><a href="#" class="btn01" onclick="window.close()"><span>닫기</span></a></div>
			<div class="btnR"><a href="#" class="btn01" onclick="doDeleteLog()"><span>로그삭제</span></a></div>
		</div>
		<!-- // button -->
		<div class="tbList" style="overflow-y:scroll; height:370px;">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="5%" />
					<col width="10%" />
					<col />
					<col width="10%"/>
					<col width="15%" />
					<col width="15%" />
				</colgroup>
				<thead>
					<tr>
						<th>번호</th>
						<th>Lesson</th>
						<th>Lesson명</th>
						<th>학습횟수</th>
						<th>학습상태</th>
						<th>최종수정일</th>
					</tr>
				</thead>
				<tbody>
				<c:set var="completedCnt" value="0"/>
				<c:set var="lessonCnt" value="0"/>
				<c:set var="studyCnt" value="0"/>
				<c:set var="tempModule" value=""/>
				<c:forEach items="${progressList}" var="result" varStatus="i">
					<c:if test="${tempModule == ''}">
					<c:set var="tempModule" value="${result.module}"/>
					</c:if>
					<c:if test="${tempModule != result.module}">
					<tr>
						<td colspan="3">
						<input type="hidden" id="chk" name="chk" value="${tempModule}"/>
							<Strong>합 계</Strong>
						</td>
						<td><Strong><c:out value="${lessonCnt}"/></Strong></td>
						<td colspan="2">
							<c:if test="${completedCnt < studyCnt}"><Strong>학습중</Strong></c:if>
							<c:if test="${completedCnt >= studyCnt}"><Strong>학습완료</Strong></c:if>
						</td>
					</tr>
					<c:set var="tempModule" value="${result.module}"/>
					<c:set var="lessonCnt" value="0"/>
					<c:set var="studyCnt" value="0"/>
					<c:set var="completedCnt" value="0"/>
					</c:if>
					<tr>
						<td>
							<c:out value="${i.count}"/>
							<c:set var="lessonCnt" value="${lessonCnt + result.lessonCount}"/>
							<c:set var="completedCnt" value="${completedCnt + result.completed}"/>
							<c:set var="studyCnt" value="${result.studyCnt}"/>
						</td>
						<td><c:out value="${result.lesson}"/></td>
						<td class="subject"><c:out value="${result.lessonName}"/></td>
						<td><c:out value="${result.lessonCount}"/></td>
						<td><c:out value="${result.lessonstatus}"/></td>
						<td><fmt:formatDate value="${result.ldate}" pattern="yyyy-MM-dd"></fmt:formatDate></td>
					</tr>
					<c:if test="${i.last}">
					<tr>
						<td colspan="3">
							<input type="hidden" id="chk" name="chk" value="${tempModule}"/>
							<Strong>합 계</Strong>
						</td>
						<td><Strong><c:out value="${lessonCnt}"/></Strong></td>
						<td colspan="2">
							<c:if test="${completedCnt < studyCnt}"><Strong>학습중</Strong></c:if>
							<c:if test="${completedCnt >= studyCnt}"><Strong>학습완료</Strong></c:if>
						</td>
					</tr>
					</c:if>
				</c:forEach>
				<c:if test="${empty progressList}">
					<tr>
						<td colspan="6">학습전 입니다.</td>
					</tr>
				</c:if>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		<!-- // button -->
		</form>
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">
	
function doDeleteLog(){
	if(<c:out value="${empty progressList}"/>){
		alert("학습전 입니다.");
		return;
	}
	var frm = eval('document.<c:out value="${gsPopForm}"/>');
	frm.target = "_self";
	frm.action = "/adm/lcms/nct/deleteLog.do";
	frm.submit();
}
</script>