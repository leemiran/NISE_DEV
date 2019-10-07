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
	<input type="hidden" id="p_process" 			name="p_process">
	
	<input type="hidden" id="p_year" 			name="p_year">
	<input type="hidden" id="p_subj" 			name="p_subj">
	<input type="hidden" id="p_subjseq" 			name="p_subjseq">
	<input type="hidden" id="p_isgraduated" 			name="p_isgraduated">
	<input type="hidden" id="rd_gubun" 			name="rd_gubun" value="C">
	<input type="hidden" id="search_att_name_value" 			name="search_att_name_value">	

			
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<div class="listTop">
                <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>
               		<a href="#" onclick="yearSubjectPrint()" class="btn01"><span>인쇄</span></a>
                </div>
  </div>
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width=""/>
					<col width=""/>
					<col width=""/>
					<col width=""/>
					<col width="60px"/>
					<col width="60px"/>
					<col width="60px"/>
					<col width="60px"/>
					<col width="60px"/>
				</colgroup>
				<thead>
					<tr>
<!--						<th scope="row" rowspan="2">No</th>-->
<!--						<th scope="row" rowspan="2"><a href="#none" onclick="doOderList('grseq')" >교육기수</a></th>-->
						<th scope="row" rowspan="2"><a href="#none" onclick="doOderList('subj')" >과정명</a></th>
						<th scope="row" rowspan="2">분야</th>
						<th scope="row" rowspan="2"><a href="#none" onclick="doOderList('subjseq')" >기수</a></th>
						<th scope="row" rowspan="2">
						<a href="#none" onclick="doOderList('propstart')" >신청기간</a><br/>
						<a href="#none" onclick="doOderList('propend')" >교육기간</a>
						</th>
						<th scope="row" colspan="5">인원수</th>
					</tr>
					<tr>
						<th scope="row"><a href="#none" onclick="doOderList('studentlimit')" >정원</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('procnt')" >신청</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('proycnt')" >학습예정</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('stucnt')" >학습진행<br/>(미수료)</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('comcnt')" >학습완료<br/>(수료)</a></th>
					</tr>
				</thead>
				<tbody>
<c:set var="v_totalcnt">0</c:set>
<c:set var="v_totalprocnt">0</c:set>
<c:set var="v_totalcancnt">0</c:set>
<c:set var="v_totalproycnt">0</c:set>
<c:set var="v_totalstucnt">0</c:set>
<c:set var="v_totalcomcnt">0</c:set>

<c:forEach items="${list}" var="result" varStatus="status" >

<c:set var="v_totalcnt">${v_totalcnt + result.studentlimit}</c:set>
<c:set var="v_totalprocnt">${v_totalprocnt + result.procnt}</c:set>
<c:set var="v_totalcancnt">${v_totalcancnt + result.cancnt}</c:set>
<c:set var="v_totalproycnt">${v_totalproycnt + result.proycnt}</c:set>
<c:set var="v_totalstucnt">${v_totalstucnt + result.stucnt}</c:set>
<c:set var="v_totalcomcnt">${v_totalcomcnt + result.comcnt}</c:set>

					<tr>
<!--						<td class="num">-->
<!--							<c:out value="${status.count}"/>-->
<!--						</td>-->
<!--						<td class="left"><c:out value="${result.grseqnm}"/></td>-->
						<td class="left"><c:out value="${result.subjnm}"/></td>
						<td><c:out value="${result.classname}"/></td>
						<td><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
						<td>
						<c:out value="${fn2:getFormatDate(result.propstart, 'yy.MM.dd')}"/>~<c:out value="${fn2:getFormatDate(result.propend, 'yy.MM.dd')}"/><br/>
						<c:out value="${fn2:getFormatDate(result.edustart, 'yy.MM.dd')}"/>~<c:out value="${fn2:getFormatDate(result.eduend, 'yy.MM.dd')}"/>
						</td>
						<td><c:out value="${result.studentlimit}"/></td>
						<td><a href="javascript:WhenMemberList('${result.year}','${result.subj}','${result.subjseq}','');"><c:out value="${result.procnt}"/></a></td>
						<td><c:out value="${result.proycnt}"/></td>
						<td><a href="javascript:WhenMemberList('${result.year}','${result.subj}','${result.subjseq}','N');"><c:out value="${result.stucnt}"/></a></td>
						<td><a href="javascript:WhenMemberList('${result.year}','${result.subj}','${result.subjseq}','Y');"><c:out value="${result.comcnt}"/></a></td>
					</tr>
</c:forEach>			

					<tr>
						<td colspan="4">합계</td>
						<td class="num"><fmt:formatNumber value="${v_totalcnt}" type="number"/></td>
						<td class="num"><fmt:formatNumber value="${v_totalprocnt}" type="number"/></td>
						<td class="num"><fmt:formatNumber value="${v_totalproycnt}" type="number"/></td>
						<td class="num"><fmt:formatNumber value="${v_totalstucnt}" type="number"/></td>
						<td class="num"><fmt:formatNumber value="${v_totalcomcnt}" type="number"/></td>
					</tr>
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
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	thisForm.action = "/adm/stu/stuMemberCountExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/sat/subjectSatisList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/sat/subjectSatisList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}


function WhenMemberList(p_year, p_subj, p_subjseq, p_isgraduated) {
	
	thisForm.action = "/adm/sat/subjectSatisDetailList.do";
	thisForm.p_year.value = p_year;
	thisForm.p_subj.value = p_subj;
	thisForm.p_subjseq.value = p_subjseq;
	thisForm.p_isgraduated.value = p_isgraduated;
	thisForm.target = "_self";
	thisForm.submit();
}

//통계 RD 출력
function yearSubjectPrint(){
	//var frm = eval('document.<c:out value="${gsMainForm}"/>');
	window.open('', 'subjectPop', 'left=100,top=100,width=1050,height=550,scrollbars=yes');
	thisForm.search_att_name_value.value=$("#ses_search_att option:selected").text();
	thisForm.action = "/adm/sta/yearSubjectPrint.do";
	thisForm.target = "subjectPop";
	thisForm.submit();
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
