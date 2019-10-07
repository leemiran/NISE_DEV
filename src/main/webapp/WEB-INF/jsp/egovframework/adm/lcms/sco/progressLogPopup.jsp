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
					<col/>
					<col width="10%" />
					<col width="10%" />
					<col width="10%" />
					<col width="10%" />
					<col width="10%" />
					<col width="15%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="row">번호</th>
						<th scope="row">학습객체명</th>
						<th scope="row">학습시간</th>
						<th scope="row">학습횟수</th>
						<th scope="row">진도측정값</th>
						<th scope="row">완료기준진도</th>
						<th scope="row">완료상태</th>
						<th scope="row">최종수정일</th>
					</tr>
				</thead>
				<tbody>
				<c:if test="${not empty progressList}">
	            <c:set var="tempOrg" value=""/>
				<c:set var="second" value="0"/>
				<c:set var="studyCnt" value="0"/>
				<c:set var="avg" value="0"/>
				<c:set var="completionCnt" value="0"/>
				<c:set var="itemCnt" value="0"/>
				<c:forEach items="${progressList}" var="result" varStatus="i">
					<c:if test="${tempOrg == ''}">
					<c:set var="tempOrg" value="${result.orgSeq}"/>
					</c:if>
					<c:if test="${tempOrg != result.orgSeq}">
					<input type="hidden" id="chk" name="chk" value="${result.orgSeq}"/>
					<tr>
						<td colspan="2" align="center"><Strong>합 계</Strong></td>
						<td><Strong><c:out value="${customFn:toStudyTimeConvert(second)}" /></Strong></td>
						<td><Strong><c:out value="${studyCnt}"/></Strong></td>
						<td><Strong><c:out value="${avg}"/>%</Strong></td>
						<td colspan="3">
						<c:if test="${completionCnt < itemCnt}"><Strong>학습중</Strong></c:if>
						<c:if test="${completionCnt >= itemCnt}"><Strong>학습완료</Strong></c:if>
						</td>
					</tr>
					<c:set var="tempOrg" value="${result.orgSeq}"/>
					<c:set var="studyCnt" value="0"/>
					<c:set var="second" value="0"/>
					</c:if>
					<tr>
						<td><c:out value="${i.count}"/></td>
						<td class="left"><c:out value="${result.itemTitle}"/></td>
						<td><c:out value="${customFn:toScormTimeConvert(result.totalTime)}" /> </td>
						<td><c:out value="${result.attempt}"/></td>
						<td><c:out value="${result.progressMeasure}"/>%</td>
						<td><c:out value="${result.completionThreshold}"/></td>
						<td><c:out value="${result.completionStatus}"/></td>
						<td><c:out value="${result.updateDt}"/></td>
						<c:set var="avg" value="${result.totalAvg}"/>
						<c:set var="second" value="${second + result.totalSecond}"/>
						<c:set var="studyCnt" value="${studyCnt + result.attempt}"/>
						<c:set var="completionCnt" value="${result.completionCnt}"/>
						<c:set var="itemCnt" value="${result.itemCnt}"/>
					</tr>
					<c:if test="${i.last}">
					<input type="hidden" id="chk" name="chk" value="${result.orgSeq}"/>
					<tr>
						<td colspan="2" class="center"><Strong>합 계</Strong></td>
						<td><Strong><c:out value="${customFn:toStudyTimeConvert(second)}" /></Strong></td>
						<td><Strong><c:out value="${studyCnt}"/></Strong></td>
						<td><Strong><c:out value="${avg}"/>%</Strong></td>
						<td colspan="3">
						<c:if test="${completionCnt < itemCnt}"><Strong>학습중</Strong></c:if>
						<c:if test="${completionCnt >= itemCnt}"><Strong>학습완료</Strong></c:if>
						</td>
					</tr>
					</c:if>
				</c:forEach>
				</c:if>
				<c:if test="${empty progressList}">
					<tr>
						<td colspan="8" align="center">학습전 입니다.</td>
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
		frm.action = "/adm/lcms/sco/deleteLog.do";
		frm.submit();
	}
</script>