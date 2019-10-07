<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_action" 	id="p_action">
	<input type="hidden" name="p_subj" 		id="p_subj"		value="">
	<input type="hidden" name="p_year" 		id="p_year"		value="">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="">
	<input type="hidden" name="p_ordseq" 	id="p_ordseq">
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col />
				<col width="200px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th rowspan="2">No</th>
					<th rowspan="2">과제명</th>
					<th >제출기한</th>
					<th rowspan="2">대상자수</th>
					<th rowspan="2">제출자수</th>
					<th rowspan="2">미제출자수</th>
					<th rowspan="2">조회</th>
				</tr>
				<tr>
					<th >추가체줄기한</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}"  var="result" varStatus="i">
				<tr>
					<td rowspan="2">${i.count}</td>
					<td rowspan="2">
						<a href="#none" onclick="whenDetail('${result.subj}', '${result.year}', '${result.subjseq}', '${result.ordseq}')">
						${result.title}
						</a>
					</td>
					<td>${fn2:getFormatDate(result.startdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.expiredate, 'yyyy.MM.dd')}</td>
					<td rowspan="2">${result.total}</td>
					<td rowspan="2">${result.submitcnt}</td>
					<td rowspan="2">${result.total - result.submitcnt}</td>
					<td rowspan="2"><a href="#" class="btn01" onclick="whenDetail('${result.subj}', '${result.year}', '${result.subjseq}', '${result.ordseq}')"><span>제출</span></a></td>
				</tr>
				<tr>
					<td>${fn2:getFormatDate(result.restartdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.reexpiredate, 'yyyy.MM.dd')}</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="7">조회된 내용이 없습니다.</td>
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

	//search 함수
	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		//if( frm.ses_search_subj.value == "" ){
		//	alert("과정을 선택하세요.");
		//	return;
	//	}
		frm.p_action.value = "go";
		frm.action = "/adm/rep/reportResultList.do";
		frm.target = "_self";
		frm.submit();
	}

	function whenDetail(v_subj, v_year, v_subjseq, v_ordseq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_subj.value = v_subj;
		frm.p_year.value = v_year;
		frm.p_subjseq.value = v_subjseq;
		frm.p_ordseq.value = v_ordseq;
		frm.action = "/adm/rep/reportResultDetailList.do";
		frm.target = "_self";
		frm.submit();
	}
	
</script>