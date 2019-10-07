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
    <input type="hidden" name="p_subj"     value="">
    <input type="hidden" name="p_gyear"     value="">
    <input type="hidden" name="p_year"     value="">
    <input type="hidden" name="p_subjseq"  value="">
    <input type="hidden" name="p_lesson"   value="">
    <input type="hidden" name="p_examtype"    value="">
    <input type="hidden" name="p_papernum" value="">
    <input type="hidden" name="p_grseq"     value="">   
	

	      
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<div class="listTop">			
                <div class="btnR MR05">
               		<a href="javascript:InsertPaperGrseq()" class="btn01"><span>교욱기수별 문제지 추가</span></a>
                </div>                                
		</div>
        
		
	  <!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="3%"/>		      
		      <col width="5%"/>
		      <col width="40%"/>
		      <col width="%"/>
		      <col width="%"/>
              <col width="%"/>
		      <col width="15%"/>
		      <col width="%"/>
		      <col width="10%"/>
	        </colgroup>
		    <thead>
		      <tr>
		        <th scope="row">No</th>
		        <th scope="row">연도</th>
		        <th scope="row">과정명</th>
		        <th scope="row">과정기수</th>
		        <th scope="row">평가타입</th>
		        <th scope="row">응시자수</th>
		        <th scope="row">난이도별 문항수<br />
		          (상/중/하 : 전체)</th>
		        <th scope="row">총점수</th>
		        <th scope="row">기능</th>
	          </tr>
	        </thead>
		    <tbody>
		    
<c:forEach items="${list}" var="result" varStatus="status">			    
		      <tr>
		        <td class="num">${status.count}</td>
		        <td class="num">${result.year}</td>
		        <td class="left">${result.subjnm}</td>
		        <td>
		        	${fn2:toNumber(result.subjseq)}기
		        	
		        	
		        <c:if test="${empty result.papernum}">
		        	<a href="javascript:InsertPaperPage('${result.subj}','${result.year}','${result.subjseq}');" class="btn_plus"><span>추가</span></a>
		        </c:if>
		        
		        </td>
		        <td>
		        
		        <c:if test="${not empty result.papernum}">
		        <a href="javascript:doPaperView('${result.subj}','${result.year}','${result.subjseq}','${result.lesson}','${result.examtype}','${result.papernum}');">
		        ${result.examtypenm}
		        </a>
		        </c:if>
		        
		        </td>
		        <td>${result.studentcnt} / ${result.examstudentcnt}</td>
		        <td>${result.cntlevel1}/${result.cntlevel2}/${result.cntlevel3} :${result.examcnt}</td>
		        <td>${result.totalscore}</td>
		        <td>
		        <c:if test="${not empty result.papernum}">
		        <a href="javascript:doPaperPreview('${result.subj}','${result.year}','${result.subjseq}','${result.lesson}','${result.examtype}','${result.papernum}');" 
		        class="btn_search01">
		        <span>미리보기</span>
		        </a>
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
		alert("과정-기수를 선택하세요");
		return;
	}


	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/exm/examPaperList.do";
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
	thisForm.action = "/adm/exm/examPaperList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

//평가 문제지  보기화면
function doPaperView(p_subj, p_gyear, p_subjseq, p_lesson, p_examtype, p_papernum) {
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
	
      farwindow = window.open("", "examPaperViewStep01PopUpWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 1020, height = 667, top=0, left=0");
      
		thisForm.p_subj.value  = p_subj;
		thisForm.p_gyear.value   = p_gyear;
		thisForm.p_subjseq.value   = p_subjseq;
		thisForm.p_lesson.value  = p_lesson;
		thisForm.p_examtype.value   = p_examtype;
		thisForm.p_papernum.value   = p_papernum;
		
		thisForm.target = "examPaperViewStep01PopUpWindow";
		thisForm.action = "/adm/exm/examPaperViewStep01.do";
		thisForm.submit();

      farwindow.window.focus();

}




//평가 문제지  미리보기 
function doPaperPreview(p_subj, p_gyear, p_subjseq, p_lesson, p_examtype, p_papernum) {
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
	
      farwindow = window.open("", "examMasterPreviewPopUpWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 800, height = 667, top=0, left=0");
      
		thisForm.p_subj.value  = p_subj;
		thisForm.p_gyear.value   = p_gyear;
		thisForm.p_year.value   = p_gyear;
		thisForm.p_subjseq.value   = p_subjseq;
		thisForm.p_lesson.value  = p_lesson;
		thisForm.p_examtype.value   = p_examtype;
		thisForm.p_papernum.value   = p_papernum;
		
		thisForm.target = "examMasterPreviewPopUpWindow";
		thisForm.action = "/adm/exm/examPaperPreview.do";
		thisForm.submit();

      farwindow.window.focus();

}

//평가 기수별 문제지 추가
function InsertPaperGrseq() {
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


	  thisForm.p_grseq.value    = $("#ses_search_grseq").val();           
	  thisForm.p_gyear.value    = $("#ses_search_gyear").val();
	  thisForm.p_process.value = 'insertGrSeq';
	  
	  thisForm.target = "_self";
	  thisForm.action = "/adm/exm/examPaperAction.do";
	  thisForm.submit();
}

//문제지 추가 
function InsertPaperPage(p_subj, p_gyear, p_subjseq) {
	
	thisForm.p_process.value = 'insert';
	thisForm.p_subj.value    = p_subj;
	thisForm.p_gyear.value    = p_gyear;
	thisForm.p_subjseq.value = p_subjseq;

	thisForm.target = "_self";
	thisForm.action = "/adm/exm/examPaperAction.do";
	thisForm.submit();
}
  
//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
