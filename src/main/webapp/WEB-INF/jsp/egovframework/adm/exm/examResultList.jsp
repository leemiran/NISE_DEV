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


	<input type="hidden" name="p_process"  value="">
	<input type="hidden" name="p_action"   value="">
	 
	<input type="hidden" name="p_subj"     value="">
	<input type="hidden" name="p_gyear"     value="">
	<input type="hidden" name="p_subjseq"  value="">
	<input type="hidden" name="p_lesson"   value="">
	<input type="hidden" name="p_examtype"    value="">
	<input type="hidden" name="p_papernum" value="">
	<input type="hidden" name="p_userid"   value="">
	<input type="hidden" name="p_subjnm"   value="">
	<input type="hidden" name="p_average"   value="">
	<input type="hidden" name="p_personcnt"   value="">
	<input type="hidden" name="p_examtypenm"  value="">
	<input type="hidden" name="p_userretry"  value="">
	<input type="hidden" name="p_retry"  value="">
    <input type="hidden" name="examtabletemp"  value="">
    <input type="hidden" name="p_examsubj"  value="">
            
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value="EXAM"							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<!-- list table-->
		<div class="tbList mrb20">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="%"/>		      
		      <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
		      <col width="%"/>
              <col width="%"/>		     
	        </colgroup>
		    <thead>
		      <tr>
		        <th scope="row">문제수</th>
		        <th scope="row">총점</th>
		        <th scope="row">대상자수</th>
		        <th scope="row">응시자수</th>
		        <th scope="row">미응시자수</th>
		        <th scope="row">전체평균</th>
	          </tr>
	        </thead>
		    <tbody>
		      <tr>
		        <td class="num">${view.examcnt}</td>
		        <td class="num">100점</td>
		        <td class="num">${fn:length(list)}</td>
		        <td class="num">${view.usercnt}</td>
		        <td class="num">${fn:length(list) - view.usercnt}</td>
		        <td class="num">${view.avgscore}</td>
	          </tr>
	        </tbody>            
	      </table>
		</div>
<!-- list table-->

<c:if test="${not empty list}">
        <div class="listTop">		
        		
        		<div class="btnR MR05">
               		<a href="#none" onclick="examSubmit()" class="btn01"><span>시험 제출/재채점</span></a>
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn01"><span>엑셀출력</span></a>
                </div>  	
                <div class="btnR MR05">
<!--                	(※조회된 학습자들이 재채점됩니다.)-->
<!--               		<a href="#none" class="btn01"><span>재채점</span></a>-->
                </div> 
                              
		</div>
</c:if>        
		
	  <!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="3%"/>		      
		      <col width="7%"/>
		      <col width="5%"/>
		      <col width="%"/>
		      <col width="%"/>
              <col width="10%"/>
		      <col width="5%"/>
		      <col width="5%"/>
		      <col width="8%"/>
              <col width="8%"/>
              <col width="8%"/>
              <col width="8%"/>
              <col width="5%"/>
              <col width="5%"/>
              <col width="%"/>
	        </colgroup>
		    <thead>
		      <tr>
		        <th scope="row" rowspan="2">No</th>
		        <th scope="row" rowspan="2"><a href="javascript:doOderList('b.userid')">ID(재채점)</a></th>
		        <th scope="row" rowspan="2"><a href="javascript:doOderList('b.name')">성명</a></th>
		        <th scope="row" rowspan="2"><a href="javascript:doOderList('b.hometel')">전화번호</a></th>
		        <th scope="row" rowspan="2"><a href="javascript:doOderList('b.handphone')">휴대전화</a></th>
		        <th scope="row" rowspan="2"><a href="javascript:doOderList('b.email')">이메일주소</a></th>
		        <th scope="row" colspan="2"><a href="javascript:doOderList('c.answer')">상태(평가보기)</a></th>
		        <th scope="row" colspan="2"><a href="javascript:doOderList('c.indate')">최초응시일시</a></th>
          		<th scope="row" colspan="2"><a href="javascript:doOderList('b.eduend')">완료일시</a></th>
          		<th scope="row" colspan="2"><a href="javascript:doOderList('c.score')">평가점수</a></th>
          		<th scope="row" rowspan="2">성적반영점수</th>
          		<th scope="row" rowspan="2"><a href="javascript:doOderList('userretry')">사용자<br/>응시횟수<br/>(재응시셋)</a></th>
          		<th scope="row" rowspan="2"><input type="checkbox" id="allChkUser"/></th>
	          </tr>
	          
	          <tr>
		        <th scope="row">1차</th>
		        <th scope="row">2차</th>
		       <th scope="row">1차</th>
		        <th scope="row">2차</th>
		        <th scope="row">1차</th>
		        <th scope="row">2차</th>
		        <th scope="row">1차</th>
		        <th scope="row">2차</th>
	          </tr>
	          
	        </thead>
		    <tbody>
		    
<c:forEach items="${list}" var="result" varStatus="status">
		      <tr>
		        <td class="num">${status.count}</td>
		        <td>${result.userid}</td>
		        <td>${result.name}</td>
		        <td>${result.hometel}</td>
		        <td>${result.handphone}</td>
		        <td>${result.email}</td>
		        <td>
		        	<c:choose>
		        		<c:when test="${result.submitYn eq 'Y'}">
		        			<a href="javascript:IndividualResult('${result.subj}','${result.year}','${result.subjseq}','${result.lesson}','${result.examtype}','','${result.papernum}','${result.userid}','${result.subjnm}','${view.avgscore}','${fn:length(list)}', '','${result.examsubj}')">
		        				<font color="blue">완료</font>
		        			</a>
		        		</c:when>
		        		<c:when test="${not empty result.started and result.submitYn eq 'N'}">
		        			<a href="javascript:IndividualResult('${result.subj}','${result.year}','${result.subjseq}','${result.lesson}','${result.examtype}','','${result.papernum}','${result.userid}','${result.subjnm}','${view.avgscore}','${fn:length(list)}', '','${result.examsubj}')">
			        			<font color="green">응시(미제출)</font>
		        			</a>
		        		</c:when>
		        		<c:otherwise>
	        				<font color="red">미응시</font>
		        		</c:otherwise>
		        	</c:choose>
		        </td>
		     	<td>
		        	<c:choose>
		        		<c:when test="${not empty result.startedTemp && empty result.endedTemp}">
			        		<a href="javascript:IndividualResult('${result.subj}','${result.year}','${result.subjseq}','${result.lesson}','${result.examtype}','','${result.papernum}','${result.userid}','${result.subjnm}','${view.avgscore}','${fn:length(list)}', 'temp','${result.examsubj}')">
			        			<font color="green">응시(미제출)</font>
			        		</a>	
		        		</c:when>
		        		<c:when test="${empty result.answercntTemp}">
		        			<font color="red">미응시</font>
		        		</c:when>
		        		<c:otherwise>
		        			<a href="javascript:IndividualResult('${result.subj}','${result.year}','${result.subjseq}','${result.lesson}','${result.examtype}','','${result.papernum}','${result.userid}','${result.subjnm}','${view.avgscore}','${fn:length(list)}', 'temp','${result.examsubj}')">
		        				<font color="blue">완료</font>
		        			</a>
		        		</c:otherwise>
		        	</c:choose>
		        </td>
		        <td><c:out value="${fn2:getFormatDate(result.indate, 'yyyy.MM.dd HH:mm')}"/></td>
		        <td><c:out value="${fn2:getFormatDate(result.indateTemp, 'yyyy.MM.dd HH:mm')}"/></td>
		        
		        <td><c:out value="${fn2:getFormatDate(result.ended, 'yyyy.MM.dd HH:mm')}"/></td>
		        <td><c:out value="${fn2:getFormatDate(result.endedTemp, 'yyyy.MM.dd HH:mm')}"/></td>
		        <td>${result.score}</td>
		        <td>${result.scoreTemp}</td>
		        <td>${result.ftest}(${result.avftest})</td>
		        <td>
		        <c:if test="${empty result.answercnt}">
		        	${result.userretry}
			        (${result.retrycnt})
		        </c:if>
		        <c:if test="${not empty result.answercnt}">
		        <a href="javascript:UpdateRetry('${result.subj}','${result.year}','${result.subjseq}','${result.lesson}','${result.examtype}','','${result.papernum}','${result.userid}','${result.subjnm}','${result.userretry}','${result.retrycnt}');">
			        ${result.userretry}
			        (${result.retrycnt})
		        </a>
		        </c:if>
		        </td>
		        <td>
		        	<c:if test="${not empty result.started}">
		        		<input type="checkbox" name="chkUser" value="${result.userid}"/>
		        	</c:if>
		        </td>
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
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	thisForm.action = "/adm/exm/examResultList.do?p_target=xlsdown";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {

	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return;
	}
	
	if($("#ses_search_subj").val() == '' || $("#ses_search_subjseq").val() == '')
	{	
		alert($("#ses_search_subj").val());
		alert($("#ses_search_subjseq").val());
		//alert("과정-기수를 선택하세요");
		return;
	}


	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/exm/examResultList.do";
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
	thisForm.action = "/adm/exm/examResultList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}


//재응시 횟수 수정
function UpdateRetry(p_subj, p_year, p_subjseq, p_lesson, p_examtype, p_branch, p_papernum, p_userid, p_subjnm, p_userretry, p_retry) 
{
		window.open("","examUserUpdateRetryPopupWindowPop","width=700,height=370,scrollbars=no");

		thisForm.p_subj.value    = p_subj;
		thisForm.p_gyear.value    = p_year;
		thisForm.p_subjseq.value = p_subjseq;
		thisForm.p_lesson.value  = p_lesson;
		thisForm.p_examtype.value   = p_examtype;
		thisForm.p_papernum.value= p_papernum;
		thisForm.p_userid.value  = p_userid;
		thisForm.p_subjnm.value   = p_subjnm;
		thisForm.p_userretry.value= p_userretry;
		thisForm.p_retry.value    = p_retry;

		thisForm.action = "/adm/exm/examUserUpdateRetry.do";
		thisForm.target = "examUserUpdateRetryPopupWindowPop";
		thisForm.submit();
}




//재채점 링크
function IndividualResult(p_subj, p_year, p_subjseq, p_lesson, p_examtype, p_branch, p_papernum, p_userid, p_subjnm, p_average, p_personcnt, examtabletemp, examsubj) 
{

	window.open("","examUserReRatingViewPopupWindowPop","width=1000,height=800,scrollbars=yes");
	
	thisForm.p_subj.value    = p_subj;
	thisForm.p_gyear.value    = p_year;
	thisForm.p_subjseq.value = p_subjseq;
	thisForm.p_lesson.value  = p_lesson;
	thisForm.p_examtype.value   = p_examtype;
	thisForm.p_papernum.value= p_papernum;
	thisForm.p_userid.value  = p_userid;
	thisForm.p_subjnm.value  = p_subjnm;
	thisForm.p_average.value  = p_average;
	thisForm.p_personcnt.value  = p_personcnt;
	thisForm.examtabletemp.value  = examtabletemp;
	thisForm.p_examsubj.value  = examsubj;
	
	
	thisForm.action = "/adm/exm/examUserReRatingView.do";
	thisForm.target = "examUserReRatingViewPopupWindowPop";
	thisForm.submit();
}

//-->

function examSubmit() {
	if($('input[name=chkUser]:checked').length < 1){
		alert("재채점할 연수생을 선택하여 주시기 바랍니다.");
		return;
	} 
		
	
	if(!confirm('체크한 회원의 시험을 제출 또는 재채점 하시겠습니까?')) return;
	
	thisForm.p_subj.value = '${list[0].subj}';
	thisForm.p_gyear.value = '${list[0].year}';
	thisForm.p_subjseq.value = '${list[0].subjseq}';
	thisForm.p_lesson.value = '${list[0].lesson}';
	thisForm.p_examtype.value = '${list[0].examtype}';
	thisForm.p_papernum.value = '${list[0].papernum}';
	
	thisForm.action = "/adm/exm/examSubmit.do";
	thisForm.target = "_self";
	thisForm.submit();
}
$(function() {
	$('#allChkUser').click(function() {
		if($(this).is(':checked')) {
			$('input[name=chkUser]').attr('checked', 'checked');
		} else {
			$('input[name=chkUser]').attr('checked', '');
		}
	});
});
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
