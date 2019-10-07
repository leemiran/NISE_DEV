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
	
	
	<input type="hidden" id="p_subj"  		name="p_subj"    	value="">
	<input type="hidden" id="p_year"  		name="p_year"    	value="">
	<input type="hidden" id="p_subjseq"  	name="p_subjseq"    value="">
	
<!-- 점수재계산을 하고 돌아오려 할때 쓰이는 변수 1일때만 종합학습현황으로 돌아온다. -->	
	<input type="hidden" id="p_stgubun"  	name="p_stgubun"    value="1">
	
	<input type="hidden" name="p_d_type" id="p_d_type"	value="O"/>
	
	
			
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value="TOTSCORE"							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<div class="listTop">
			<p class="floatL point">TOTAL : <span class="num1"><c:out value="${fn:length(list)}"/></span> 명</p>
            	
                <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>
                </div>
                
<!--                총괄관리지만 보이도록 한다.-->
<c:if test="${fn:indexOf(sessionScope.gadmin, 'A') > -1}">                
                <div class="btnR MR05">
               		<a href="#none" onclick="ReRating()" class="btn01"><span>점수재계산</span></a>
                </div> 
                <div class="btnR MR05">
           		  <a href="#none" onclick="whenCommonSmsSend(<c:out value="${gsMainForm}"/>, '_Array_p_checks')" class="btn01"><span>SMS</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="#none" onclick="whenCommonMailSend(<c:out value="${gsMainForm}"/>, '_Array_p_checks')" class="btn01"><span>메일발송</span></a>
                </div>
                
</c:if>         

                       
  </div>
		
		
		<!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="2%"/>
		      <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
              <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
              <col width="%"/>
              <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
              <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
		      <col width="2%"/>
	        </colgroup>
		    <thead>
		      <tr>
		        <th rowspan="2" scope="row">No</th>
		        <th rowspan="2" scope="row">수험번호</th>
		        <th rowspan="2" scope="row"><a href="javascript:whenOrder('a.subjnm')" >과정명</a></th>
		        <th rowspan="2" scope="row">지역</th>
		        <th rowspan="2" scope="row"><a href="javascript:whenOrder('a.isonoff')" >교육구분</a></th>
		        <th rowspan="2" scope="row"><a href="javascript:whenOrder('a.subjseq')" >기수</a></th>
		        <th rowspan="2" scope="row">ID</th>
		        <th rowspan="2" scope="row">성명</th>
		        <th rowspan="2" scope="row">학교명/소속근무처</th>
		        <th rowspan="2" scope="row">진도율(%)</th>
		        <th rowspan="2" scope="row">참여도(일)</th>
		        <th rowspan="2" scope="row">참여도평가</th>
		        <th rowspan="2" scope="row"><a href="javascript:whenOrder('a.ftest')" >온라인시험</a></th>
		        <th rowspan="2" scope="row"><a href="javascript:whenOrder('a.mtest')" >출석시험</a></th>
		        <th rowspan="2" scope="row"><a href="javascript:whenOrder('a.report')" >온라인과제</a></th>
		        <th rowspan="2" scope="row"><a href="javascript:whenOrder('a.score')" >총점</a></th>
		        <th colspan="3" scope="row">미응시/미제출</th>
		        <th rowspan="2" scope="row">SMS수신여부</th>
		        <th rowspan="2" scope="row"><input type="checkbox" name="checkedAlls" onclick="checkAll(this.checked)"></th>
	          </tr>
		      <tr>
		        <th scope="row"><a href="javascript:whenOrder('repexamcnt')" >평가</a></th>
		        <th scope="row"><a href="javascript:whenOrder('repprojcnt')" >리포트</a></th>
		        <th scope="row"><a href="javascript:whenOrder('repsulcnt')" >설문</a></th>
	          </tr>
	        </thead>
		    <tbody>
		    
<c:set var="v_total">${fn:length(list)}</c:set>

<c:set var="v_avg_tstep">0</c:set>
<c:set var="v_avg_avtstep">0</c:set>
<c:set var="v_avg_report">0</c:set>
<c:set var="v_avg_mtest">0</c:set>
<c:set var="v_avg_ftest">0</c:set>
<c:set var="v_avg_etc1">0</c:set>
<c:set var="v_avg_etc2">0</c:set>
<c:set var="v_avg_act">0</c:set>
<c:set var="v_avg_score">0</c:set>




<c:forEach items="${list}" var="result" varStatus="status" >

<c:set var="v_reportstatus">-</c:set>
<c:set var="v_resend">${(result.totprojcnt - result.repprojcnt)}</c:set>

<c:set var="v_avg_tstep">${v_avg_tstep + result.tstep}</c:set>
<c:set var="v_avg_avtstep">${v_avg_avtstep + result.avtstep}</c:set>
<c:set var="v_avg_report">${v_avg_report + result.report}</c:set>
<c:set var="v_avg_mtest">${v_avg_mtest + result.mtest}</c:set>
<c:set var="v_avg_ftest">${v_avg_ftest + result.ftest}</c:set>
<c:set var="v_avg_etc1">${v_avg_etc1 + result.rect1}</c:set>
<c:set var="v_avg_etc2">${v_avg_etc2 + result.avetc2}</c:set>
<c:set var="v_avg_act">${v_avg_act + result.act}</c:set>
<c:set var="v_avg_score">${v_avg_score + result.score}</c:set>



<c:if test="${result.totprojcnt > 0}">
	<c:if test="${result.totprojcnt > 0}">
		<c:if test="${(result.totprojcnt - result.repprojcnt + result.isretcnt) == 0}">
			<c:set var="v_reportstatus">완료</c:set>
		</c:if>
		<c:if test="${(result.totprojcnt - result.repprojcnt + result.isretcnt) != 0}">
			<c:if test="${result.isretcnt > 0}">
				<c:set var="v_reportstatus">${result.isretcnt}<br/>(${result.totprojcnt})<font color='red'>모사</font></c:set>
			</c:if>
			<c:if test="${result.isretcnt <= 0}">
				<c:set var="v_reportstatus">${v_resend}<br/>${result.totprojcnt}</c:set>
			</c:if>
		</c:if>
	</c:if>
</c:if>
	    
		      <tr>
		        <td class="num"><c:out value="${status.count}"/></td>
		        <td><c:out value="${result.examnum}"/></td>
		        <td class="left"><c:out value="${result.subjnm}"/></td>
		        <td>${result.areaCodenm}</td>
		        <td><c:out value="${result.isonoffval}"/></td>
		        <td><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
		        <td><c:out value="${result.userid}"/></td>
		        <td>
		        <a href="javascript:whenPersonGeneralPopup('${result.userid}')">
		        <c:out value="${result.name}"/>
		        </a>
		        </td>
		        <td>
		        	<c:if test="${result.empGubun eq 'R'}">
			        	<c:out value="${result.positionNm}"/>
			       	</c:if>
			       	<c:if test="${result.empGubun ne 'R'}">
			        	<c:out value="${result.userPath}"/>
     				</c:if>
		        </td>
		        <td>
		       <a href="#none" onclick="whenPersonalTimeList('${result.subj}','${result.year}','${result.subjseq}','${result.userid}')">
		        <fmt:formatNumber value="${result.tstep}" 	 type="number" pattern=",###.##"/><!-- 진도율 -->
		        </a>
		        </td>
		        <td>
		        <a href="javascript:whenAttend('${result.subj}','${result.year}','${result.subjseq}','${result.userid}','${result.name}')">
		        <fmt:formatNumber value="${result.rect1}" 	 type="number" pattern=",###.##"/>
		        </a>
		        </td>
		        <td><fmt:formatNumber value="${result.avetc2}" 	 type="number" pattern=",###.##"/><!-- 참여도평가--></td>
		        <td><fmt:formatNumber value="${result.ftest}" 	 type="number" pattern=",###.##"/><!-- 시험 --></td>
		        <td><fmt:formatNumber value="${result.mtest}" 	 type="number" pattern=",###.##"/><!--mtest --></td>
		        <td><fmt:formatNumber value="${result.report}" 	 type="number" pattern=",###.##"/></td>
		        <td><fmt:formatNumber value="${result.score}" 	 type="number" pattern=",###.##"/></td>
		        <td>
		        	<c:if test="${result.totexamcnt > 0}">
		        		<c:if test="${(result.totexamcnt - result.repexamcnt) == 0}">
		        			완료
		        		</c:if>
		        		<c:if test="${(result.totexamcnt - result.repexamcnt) != 0}">
		        			<c:out value="${result.totexamcnt - result.repexamcnt}"></c:out>
		        			<br/>(<c:out value="${result.totexamcnt}"></c:out>)
		        		</c:if>
		        		
		        	</c:if>
		        	<c:if test="${result.totexamcnt <= 0}">
		        		-
		        	</c:if>
		        </td>
		        <td><c:out value="${v_reportstatus}"/></td>
		        <td>
		        	<c:if test="${result.totsulcnt > 0}">
		        		<c:if test="${(result.totsulcnt - result.repsulcnt) == 0}">
		        			완료
		        		</c:if>
		        		<c:if test="${(result.totsulcnt - result.repsulcnt) != 0}">
		        			<c:out value="${result.totsulcnt - result.repsulcnt}"></c:out>
		        			<br/>(<c:out value="${result.totsulcnt}"></c:out>)
		        		</c:if>
		        		
		        	</c:if>
		        	<c:if test="${result.totsulcnt <= 0}">
		        		-
		        	</c:if>
		        </td>
		        <td><c:out value="${result.issms}"/></td>
		        <td>
		        	<input type="checkbox" name="_Array_p_checks" value="${result.subj},${result.year},${result.subjseq},${result.userid}">
		        </td>
	          </tr>
</c:forEach>	       

<c:if test="${v_total > 0}">

<c:set var="v_avg_tstep">${v_avg_tstep / v_total}</c:set>
<c:set var="v_avg_avtstep">${v_avg_avtstep / v_total}</c:set>
<c:set var="v_avg_report">${v_avg_report / v_total}</c:set>
<c:set var="v_avg_mtest">${v_avg_mtest / v_total}</c:set>
<c:set var="v_avg_ftest">${v_avg_ftest / v_total}</c:set>
<c:set var="v_avg_etc1">${v_avg_etc1 / v_total}</c:set>
<c:set var="v_avg_etc2">${v_avg_etc2 / v_total}</c:set>
<c:set var="v_avg_act">${v_avg_act / v_total}</c:set>
<c:set var="v_avg_score">${v_avg_score / v_total}</c:set>

				<tr>
					<td colspan="9"><b>평균</b></td>
					<td><fmt:formatNumber value="${v_avg_tstep}" 	 type="number" pattern=",###.##"/>%</td>
					<td><fmt:formatNumber value="${v_avg_etc1}" 	 type="number" pattern=",###.##"/></td>
					<td><fmt:formatNumber value="${v_avg_etc2}" 	 type="number" pattern=",###.##"/></td>
					<td><fmt:formatNumber value="${v_avg_ftest}" 	 type="number" pattern=",###.##"/></td>
					<td><fmt:formatNumber value="${v_avg_mtest}" 	 type="number" pattern=",###.##"/></td>
					<td><fmt:formatNumber value="${v_avg_report}" 	 type="number" pattern=",###.##"/></td>
					<td><fmt:formatNumber value="${v_avg_score}" 	 type="number" pattern=",###.##"/></td>
					<td colspan="9">&nbsp;</td>
				</tr>



</c:if>
   
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
	thisForm.action = "/adm/stu/totalScoreMemberExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	
	//d_type
	thisForm.p_d_type.value = "T";
	
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/stu/totalScoreMemberList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function whenOrder(column) {
	
	//d_type
	thisForm.p_d_type.value = "T";
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/stu/totalScoreMemberList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}


function checkAll(val){
	var obj1 = document.getElementsByName("_Array_p_checks");
	
	if(val==true){
		if(obj1.length > 1){	
			for(i=0;i < obj1.length;i++){
				obj1[i].checked = true;
			}
		}else{
			obj1[0].checked = true;
		}
	}else{
		if(obj1.length > 1){
			for(i=0;i < obj1.length;i++){
				obj1[i].checked = false;
			}
		}else{
			obj1[0].checked = false;
		}
	}
}


function ReRating(){
	if('<c:out value="${sessionScope.ses_search_grseq}"/>' == ''){
		alert('교육기수를 선택하세요');
		return;
	}

	if('<c:out value="${sessionScope.ses_search_subj}"/>' == ''){
		alert("먼저 과정명-기수를 검색하셔야 합니다.");
		return;
	}

	if('<c:out value="${sessionScope.ses_search_subjseq}"/>' == ''){
		alert("먼저 과정명-기수를 검색하셔야 합니다.");
		return;
	}

	if('<c:out value="${sessionScope.ses_search_year}"/>' == ''){
		alert("먼저 과정명-기수를 검색하셔야 합니다.");
		return;
	}

	
	if (<c:out value="${v_total}"/> == 0) {
		alert("수강생이 없습니다.");
		return;
	}
	if (confirm("점수재계산 하시겠습니까?")) {
		
		thisForm.p_subj.value = '<c:out value="${sessionScope.ses_search_subj}"/>';
		thisForm.p_year.value = '<c:out value="${sessionScope.ses_search_year}"/>';
		thisForm.p_subjseq.value = '<c:out value="${sessionScope.ses_search_subjseq}"/>';
		thisForm.p_stgubun.value = "1";
		
		thisForm.action = "/adm/fin/subjectCompleteRerating.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
}



function checkAll(val){
	var obj1 = document.getElementsByName("_Array_p_checks");
	
	if(val==true){
		if(obj1.length > 1){	
			for(i=0;i < obj1.length;i++){
				obj1[i].checked = true;
			}
		}else{
			obj1[0].checked = true;
		}
	}else{
		if(obj1.length > 1){
			for(i=0;i < obj1.length;i++){
				obj1[i].checked = false;
			}
		}else{
			obj1[0].checked = false;
		}
	}
}


// 체크박스 체크
function chkSelected() {
	var selectedcnt = 0;
	if(thisForm.all['_Array_p_checks']) {
		if (thisForm._Array_p_checks.length > 0) {
			for (i=0; i<thisForm._Array_p_checks.length; i++) {
				if (thisForm._Array_p_checks[i].checked == true) {
					selectedcnt++;
				}
			}
		} else {
			if (thisForm._Array_p_checks.checked == true) {
				selectedcnt++;
			}
		}
	}
	return selectedcnt;
}

//개인별 학습창 접근 로그 팝업
function whenPersonalTimeList(subj, year, subjseq, userid) {

	var url = '/adm/stu/personalTimeListPopUp.do'
	+ '?p_grcode=<c:out value="${sessionScope.grcode}"/>'
	+ '&p_subj=' + subj
	+ '&p_year=' + year
	+ '&p_subjseq=' + subjseq
	+ '&p_userid=' + userid
	;
		
	window.open(url,"personalTimeListPopupWindowPop","width=1000,height=600,scrollbars=yes");
}


//출석부
function whenAttend(subj, year, subjseq, userid, name) {

	var url = '/adm/stu/personalAttendListPopUp.do'
	+ '?p_grcode=<c:out value="${sessionScope.grcode}"/>'
	+ '&p_subj=' + subj
	+ '&p_year=' + year
	+ '&p_subjseq=' + subjseq
	+ '&p_userid=' + userid
	+ '&p_name=' + name
	;
		
	window.open(url,"personalAttendListPopupWindowPop","width=660,height=600,scrollbars=yes");
}



//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
