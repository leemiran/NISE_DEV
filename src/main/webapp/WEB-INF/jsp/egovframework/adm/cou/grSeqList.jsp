<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	
	<input type="hidden" id="p_grcode"      name="p_grcode"      value="${sessionScope.grcode}">
	<input type="hidden" id="p_grseq"      	name="p_grseq"      value="">
	<input type="hidden" id="p_subj"        name="p_subj"        value="">
	<input type="hidden" id="p_subjnm"      name="p_subjnm"      value="">


		
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="AA"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


<c:if test="${sessionScope.grcode != ''}">
		<div class="listTop">
            <div class="btnR MR05">
			<a href="#" onclick="addGrseq()" class="btn01"><span>교육기수추가</span></a>
			</div>            
		</div>
</c:if>		
		
		
		<div class="listTop">
			<p class="right">전체목록 : <span class="num1"><c:out value="${fn:length(list)}"/></span> 건</p>
		</div>
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="10%"/>
                    <col width="10%"/>
                    <col width="30%"/>
                    <col width="5%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="5%"/>
                    <col width="5%"/>
                    <col width="5%"/>
                    <col width="5%"/>
                    <col width="5%"/>					
				</colgroup>
				<thead>
					<tr>
						<th rowspan="2" scope="row">교육기수</th>
						<th rowspan="2" scope="row">패키지</th>
						<th rowspan="2" scope="row"><a href="#none" onclick="whenOrder('subjnm')">과정명</a></th>
						<th rowspan="2" scope="row"><a href="#none" onclick="whenOrder('subjseq')">과정<br />기수</a></th>
						<th rowspan="2" scope="row"><a href="#none" onclick="whenOrder('isonoffnm')">교육구분</a></th>
						<th rowspan="2" scope="row"><a href="#none" onclick="whenOrder('musername')">담당자</a></th>
						<th rowspan="2" scope="row"><a href="#none" onclick="whenOrder('isuse')">사용</a></th>
						<th colspan="4" scope="row">인원</th>
					</tr>
					<tr>
					  <th scope="row"><a href="#none" onclick="whenOrder('proposecnt')">신청</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('cancelcnt')">취소</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('studentcnt')">승인</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('stoldcnt')">수료</a></th>
				  </tr>
				</thead>
				<tbody>
				
<c:set var="v_grseq" value=""></c:set>
<c:set var="v_gyear" value=""></c:set>

			
<c:forEach items="${list}" var="result" varStatus="status" >
					
					<c:set var="v_coursesubjnmlink" >
						<a href="#none" onclick="whenSubjseq('${result.gyear}','${result.grseq}','${result.subj}','${result.year}','${result.subjseq}','${result.isonoff}')">
						[${result.subj}] ${result.subjnm}
						</a>
					</c:set>
					<c:set var="v_subjnmlink">
						<input type='hidden' name='tnm<c:out value="${status.index}"/>' id='tnm<c:out value="${status.index}"/>' value='${result.subjnm}'>
						<a href="#none" onclick="whenDetail('${result.subj}',document.getElementById('tnm<c:out value="${status.index}"/>').value,'${result.grseq}')" nm='${result.subjnm}'>
					 	[${result.subj}] ${result.subjnm}
					 	</a>
					</c:set>											







					<tr>


<c:if test="${v_grseq != result.grseq || v_gyear != result.gyear}">	
	
		<c:set var="v_grseq" value="${result.grseq}"></c:set>
		<c:set var="v_gyear" value="${result.gyear}"></c:set>
	
					
				
				
						<td  rowspan="<c:out value="${result.rowspanGrseq}"/>"  valign="top">
						
								<b><c:out value="${result.gyear}"/>-<c:out value="${result.grseq}"/></b><br/>
								<a href="#none" onclick="whenGrseq('${result.gyear}','${result.grseq}')" alt="과정기수명수정"><c:out value="${result.grseqnm}"/></a><br/>
								<a href="#none" onclick="assignSubjCourse('${result.gyear}','${result.grseq}')" class="btn01"><span>일괄과정지정</span></a><br/>
								<a href="#none" onclick="updateSubjCourse('${result.gyear}','${result.grseq}')" class="btn01"><span>과정수정</span></a><br/>
								<b>총과정 : <c:out value="${result.rowspanGrseq}"/>개</b>
						
						</td>
</c:if>





	
<c:if test="${empty result.subj}">
							<td colspan="12">등록된 과정이 없습니다.</td>
</c:if>
<c:if test="${not empty result.subj}">
		<c:set var="v_courseStr" value="${result.course}||${result.cyear}||${result.courseseq}"></c:set>
		<c:set var="v_temp_courseStr" value=""></c:set>
		
		<c:choose>
			<c:when test="${result.course ne '000000' && v_courseStr ne v_temp_courseStr}">
				<c:set var="v_temp_courseStr" value="${result.course}||${result.cyear}||${result.courseseq}"></c:set>
				<c:set var="v_course" value="${result.course}"/>
				<c:set var="v_cyear" value="${result.cyear}"/>
				<c:set var="v_courseseq" value="${result.courseseq}"/>
				
				
									<td rowspan="">
<!--						              <a href="#none" onclick="whenCourseseq('${v_course}','${v_cyear}','${v_courseseq}')">-->
						              	<c:out value="${result.coursenm}"></c:out> <c:out value="${v_courseseq}"></c:out>
<!--						              </a>-->
						            </td>
<!--						            <td class="left"><c:out value="${v_coursesubjnmlink}" escapeXml="false"/></td>-->
						            <td class="left"><c:out value="${v_subjnmlink}" escapeXml="false"/></td>
			</c:when>
			<c:when test="${result.course eq '000000'}">
				<c:set var="v_course" value="${result.course}"/>
				<c:set var="v_cyear" value="${result.cyear}"/>
				<c:set var="v_courseseq" value="${result.courseseq}"/>
				
				<td colspan="2" class="left"><c:out value="${v_subjnmlink}" escapeXml="false"/></td>
			</c:when>
			<c:otherwise>
				<td class="left"><c:out value="${v_coursesubjnmlink}"/><c:out value="${result.course}"/></td>
			</c:otherwise>
		</c:choose>
						<td class="num"><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
						<td><c:out value="${result.isonoffnm}"/></td>
						<td><c:out value="${result.musername}"/></td>
						<td><c:out value="${result.isuse}"/></td>
						<td>
						<a href="#none" onclick="whenStudentList('${result.gyear}','${result.subj}','${result.subjseq}','propose')">
						<c:out value="${result.proposecnt}"/>명
						</a>
						</td>
						<td>
						<a href="#none" onclick="whenStudentList('${result.gyear}','${result.subj}','${result.subjseq}','cancel')">
						<c:out value="${result.cancelcnt}"/>명
						</a>
						</td>
						<td>
						<a href="#none" onclick="whenStudentList('${result.gyear}','${result.subj}','${result.subjseq}','student')">
						<c:out value="${result.studentcnt}"/>명
						</a>
						</td>
						<td>
						<a href="#none" onclick="whenStudentList('${result.gyear}','${result.subj}','${result.subjseq}','stold')">
						<c:out value="${result.stoldcnt}"/>명
						</a>
						</td>

</c:if>				
				</tr>

</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- list table-->




</form>




<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


/* ********************************************************
 * 페이징처리 함수
 ******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/cou/grSeqList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}


// 교육기수 추가
function addGrseq(){        
	var url = "/adm/cou/grSeqInsert.do";
	window.open(url,"grSeqInsertWindowPop","width=650,height=370");
}


// 교육기수 상세화면
function whenGrseq(gyear,grseq){
	var url = "/adm/cou/grSeqUpdate.do"
	+ '?p_gyear=' + gyear
	+ '&p_grseq=' + grseq
	;
    window.open(url,"grSeqUpdateWindowPop","width=650,height=370");
}


/* ********************************************************
* 정렬처리 함수
******************************************************** */
function whenOrder(column) {
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/cou/grSeqList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
}


//교육기수별 인원 상세 조회(신청, 취소, 승인, 수료)
function whenStudentList(year, subj, subjseq, gubun) {
	var url = '/adm/cou/grSeqStudentList.do'
	+ '?p_grcode=<c:out value="${sessionScope.grcode}"/>'
	+ '&p_gyear=' + year
	+ '&p_subj=' + subj
	+ '&p_subjseq=' + subjseq
	+ '&p_gubun=' + gubun
		;
	
	window.open(url,"grSeqStudentListWindowPop","width=1024,height=600,scrollbars=yes");
}


//기수 상세페이지 리스트로 이동
function whenDetail(subj, th, grseq) {
	document.getElementById("search_orderType").value = "";
	document.getElementById("search_orderColumn").value = "";
	document.getElementById("p_grseq").value = grseq;
	document.getElementById("p_subj").value = subj;
	document.getElementById("p_subjnm").value = th;
	thisForm.action = "/adm/cou/grSeqDetailList.do";
	thisForm.target = "_self";
	thisForm.submit();
}



//과정지정화면으로 이동
function assignSubjCourse(gyear, grseq){
	var url = '/adm/cou/grSeqAssignSubjCourseList.do'
		+ '?p_grcode=<c:out value="${sessionScope.grcode}"/>'
		+ '&p_gyear=' + gyear
		+ '&p_grseq=' + grseq
			;
		
	window.open(url,"grSeqAssignSubjCourseWindowPop","width=1024,height=600,scrollbars=yes");
}

// 과정일괄 수정화면으로 이동
function updateSubjCourse(gyear, grseq){
	var url = '/adm/cou/grSeqUpdateSubjCourseList.do'
		+ '?p_grcode=<c:out value="${sessionScope.grcode}"/>'
		+ '&p_gyear=' + gyear
		+ '&p_grseq=' + grseq
			;
		
	window.open(url,"grSeqUpdateSubjCourseWindowPop","width=1024,height=600,scrollbars=yes");
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
