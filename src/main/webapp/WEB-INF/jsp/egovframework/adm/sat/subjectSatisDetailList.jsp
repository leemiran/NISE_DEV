<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>
<style>
table.line td{border:0px;}
</style>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	<input type="hidden" id="p_process" 			name="p_process">
	<input type="hidden" id="rd_gubun" 			name="rd_gubun" value="D">
	
	<input type="hidden" id="p_year" 			name="p_year"		value="${p_year}">
	<input type="hidden" id="p_subj" 			name="p_subj"		value="${p_subj}">
	<input type="hidden" id="p_subjseq" 		name="p_subjseq"	value="${p_subjseq}">	
	<input type="hidden" id="p_isgraduated" 	name="p_isgraduated"	value="${p_isgraduated}">	
	
	
	<div class="listTop">
                <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>
               		<a href="#" onclick="subjectDetailPrint()" class="btn01"><span>인쇄</span></a>
                </div>
                <div class="btnR"><a href="#none" class="btn01" onclick="doPageList();"><span>목록</span></a></div>
  </div>
	<font color="red">
   총 ${fn:length(list)}명
	</font>
	<br/>
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>학교명</th>
					<th>구분</th>
					<th>ID</th>
					<th>성명</th>
					<th>기수</th>
					<th>연수과정명</th>
					<th>연수<br/>시간</th>
					<th>참여도<br/>평가</th>
					<th>온라인<br/>시험</th>
					<th>출석<br/>시험</th>
					<th>온라인<br/>과제</th>
					<th>총점</th>
					<th>조정<br/>점수</th>
					<th>이수<br/>여부</th>
					<th>이수번호</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>${i.count}</td>
					<td><c:out value="${result.userPath}" /></td>
					<td>
						<c:if test="${result.empGubun eq 'T' }">교원</c:if>
						<c:if test="${result.empGubun eq 'E' }">보조인력</c:if>
						<c:if test="${result.empGubun eq 'P' }">일반회원(학부모 등)</c:if>
						<c:if test="${result.empGubun eq 'W' }">기타</c:if>
					</td>
					<td>${result.userid}</td>
					<td><a href="javascript:whenPersonGeneralPopup('${result.userid}')">${result.name}</a></td>
					<td><c:out value="${fn2:toNumber(result.subjseq)}" /></td>
					<td><c:out value="${result.subjnm}" /></td>
					<td><c:out value="${result.edutimes}" /></td>
					<td><c:out value="${result.avetc2}" /></td>
					<td><c:out value="${result.avftest}" /></td>
					<td><c:out value="${result.avmtest}" /></td>
					<td><c:out value="${result.avreport}" /></td>
					<td><c:out value="${result.score}" /></td>
					<td>
					<c:if test="${result.edutimes >= 60 and fn:substring(p_subj, 0, 3) ne 'PAR'}">
						<c:out value="${result.editscore}" />
					</c:if>
					</td>
					<td><c:out value="${result.isgraduatedname}" /></td>
					
					
					
					<td>
						<c:if test="${not empty result.serno}">
							<c:if test="${result.upperclassnm ne '일반연수'}">
							특수-${fn:replace(fn:replace(result.upperclassnm, '특별과정','교원'), '기타','')} 직무-${fn2:getFormatDate(result.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}
							</c:if>
							<c:if test="${result.upperclassnm eq '일반연수'}">
							특수-사이버-${fn2:getFormatDate(result.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}
							</c:if>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="16">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


function doPageList() {
	thisForm.action = "/adm/sat/subjectSatisList.do";
	thisForm.target = "_self";
	thisForm.submit();
}



/* ********************************************************
* 엑셀다운로드 함수
******************************************************** */
function whenXlsDownLoad() {
	thisForm.p_process.value = "xlsdown";
	thisForm.action = "/adm/sat/subjectSatisDetailList.do";
	thisForm.target = "_self";
	thisForm.submit();
}

function subjectDetailPrint(){
	//var frm = eval('document.<c:out value="${gsMainForm}"/>');
	window.open('', 'subjectDetailPop', 'left=100,top=100,width=1050,height=550,scrollbars=yes');
	thisForm.action = "/adm/sta/yearSubjectPrint.do";
	thisForm.target = "subjectDetailPop";
	thisForm.submit();
}
</script>