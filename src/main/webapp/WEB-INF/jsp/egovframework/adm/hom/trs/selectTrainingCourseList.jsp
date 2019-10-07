<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/jquery/jquery-latest.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/jquery/jquery-ui.js"></script>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type = "hidden" name="p_seq"     value = "${p_seq}" />
	<input type = "hidden" name="p_iseq"     value = "" />
	<input type = "hidden" name="p_process"     value = "" />
	
	
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doPageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="trainingCourseMove()"><span>순서 수정</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>		
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="70px" />				
				<col width="100px" />
				<col />	
				<col width="100px" />
				<col width="100px" />							
				<col width="100px" />
				<col width="100px" />
				<col width="200px" />
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>구분</th>
					<th>과정명</th>
					<th>시간</th>
					<th>대상</th>
					<th>통합교육</th>
					<th>보조인력</th>
					<th>등록일</th>
					<th>등록자</th>
					<th>사용여부</th>
					<th>수정</th>
					<th>삭제</th>
					<th>순서변경</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
				<input type="hidden" name="_Array_p_params" value="${result.iseq}">
					<td >${totCnt - result.rn + 1}</td>
					<td class="left">${result.gubun}</td>
					<td >${result.coursenm}</td>
					<td >${result.eduTime}</td>
					<td >${result.eduTarget}</td>
					<td >${result.totalEdu}</td>					
					<td >${result.assist}</td>
					<td >${result.ldate}</td>
					<td >${result.name}</td>
					<td >${result.useYn}</td>
					<td ><a href="#none" onclick="trainingCourseEdit('${result.iseq}')"><span>수정</span></a></td>
					<td ><a href="#" onclick="whenTrainingCourseSave('${result.iseq}', 'delete')"><span>삭제</span></a></td>
					<td ><button type="button" onclick="moveUp(this)">올리기</button>
						 <button type="button" onclick="moveDown(this)">내리기</button></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="50">조회된 내용이 없습니다.</td>
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

	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	
	function doPageList() {
		frm.action = "/adm/hom/trs/selectTrainingList.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function insertPage(){		
		frm.action = "/adm/hom/trs/trainingCourseInsertPage.do";
		frm.target = "_self";
		frm.submit();
	}	
	
	//수정하기
	function trainingCourseEdit(iseq) {		
		frm.p_iseq.value = iseq;
		frm.action = "/adm/hom/trs/trainingCourseInsertPage.do";
		frm.target = "_self";
		frm.submit();
    }
    
	//삭제하기
	function whenTrainingCourseSave(iseq, mode) {

		if(confirm("현재글을 삭제하시겠습니까?"))
		{   
			frm.p_iseq.value = iseq;
			frm.action = "/adm/hom/trs/trainingCourseActionPage.do";
			frm.p_process.value = mode;
			frm.target = "_self";
			frm.submit();
		}
    }
	
	//순서수정하기
	function trainingCourseMove() {
		if(confirm("순서 수정 하시겠습니까?")){
			frm.p_process.value = "update";
			frm.action = "/adm/hom/trs/trainingCourseMove.do";
			frm.target = "_self";
			frm.submit();
		}	
    }
	
</script>


<script type="text/javascript">

	function moveUp(el){		
		var $tr = $(el).parent().parent(); // 클릭한 버튼이 속한 tr 요소
		$tr.prev().before($tr); // 현재 tr 의 이전 tr 앞에 선택한 tr 넣기
	}

	function moveDown(el){
		var $tr = $(el).parent().parent(); // 클릭한 버튼이 속한 tr 요소
		$tr.next().after($tr); // 현재 tr 의 다음 tr 뒤에 선택한 tr 넣기
	}
	

</script>