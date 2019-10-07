<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>

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

 	<input type="hidden" name="p_process" value="">
	<input type="hidden" name="p_subj"  value="">
	<input type="hidden" name="p_lesson"  value="">
	<input type="hidden" name="p_examtype"  value="">
	
	
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="AA"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


        
		
	   <!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="%"/>
		      <col width="20%"/>
		      <col width="10%"/>
		      <col width="10%"/>
	        </colgroup>
		    <thead>
		      <tr>
		        <th scope="row">과정명</th>
		        <th scope="row">평가타입</th>
		        <th scope="row">총문제수</th>
		        <th scope="row">총점수</th>
	          </tr>
	        </thead>
		    <tbody>
<c:forEach items="${list}" var="result" varStatus="status">		    
		      <tr>
		        <c:if test="${status.index == 0}">
		        <td class="left" rowspan="${fn:length(list)}">
              		${result.subjnm}
              		<c:if test="${status.index == 0}">
               		<div class="btnR"><a href="javascript:doMasterPage('','','');" class="btn_plus"><span>추가</span></a></div>
               		</c:if>
                </td>
                </c:if>
                
		        <td><a href="javascript:doMasterPage('${result.lesson}','${result.examtype}', 'update');" >${result.examtypenm}</a></td>
		        <td>${result.examcnt}</td>
		        <td>${result.totalscore}</td>
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

	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}
	

	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/exm/examMasterList.do";
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
	thisForm.action = "/adm/exm/examMasterList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}


//평가 마스터  추가 
function doMasterPage(p_lesson, p_examtype, p_process) {
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

	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}
	
        farwindow = window.open("", "examMasterViewStep01PopUpWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 1020, height = 667, top=0, left=0");
        
		thisForm.p_subj.value = $("#ses_search_subj").val();
		thisForm.p_lesson.value  = p_lesson;
		thisForm.p_examtype.value   = p_examtype;
		thisForm.p_process.value   = p_process;
		
		thisForm.target = "examMasterViewStep01PopUpWindow";
		thisForm.action = "/adm/exm/examMasterViewStep01.do";
		thisForm.submit();

        farwindow.window.focus();

}


//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
