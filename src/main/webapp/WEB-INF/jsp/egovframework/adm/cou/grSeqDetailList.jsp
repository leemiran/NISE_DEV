<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	
	<input type="hidden" id="p_grcode" 		name="p_grcode"			value="${p_grcode}">
	<input type="hidden" id="p_gyear" 		name="p_gyear"			value="${sessionScope.ses_search_gyear}">
	<input type="hidden" id="p_subj" 			name="p_subj"			value="${p_subj}">
	<input type="hidden" id="p_subjnm" 		name="p_subjnm"			value="${p_subjnm}">
	<input type="hidden" id="p_grseq" 		name="p_grseq"			value="${p_grseq}">
	
	<input type="hidden" id="p_process" 		name="p_process"			value="">
	<input type="hidden" id="p_subjseq" 		name="p_subjseq"			value="">	
		
	
	
		<!-- // titleWrap -->
        <div class="sub_tit">
			<h4>과 정 : <c:out value="${p_subjnm}"></c:out>   <c:out value="${sessionScope.ses_search_gyear}"></c:out>년도 </h4>						
		</div>
		

		<div class="listTop">			
                <div class="btnR">
               		<a href="#none" onclick="goSubjectListPage()" class="btn01"><span>목록</span></a>
                </div> 
                <div class="btnR MR05">
               		<a href="#none" onclick="whenAddSeq()" class="btn01"><span>기수추가</span></a>
                </div>
                
                <div class="btnR MR05" id="addseq" style="display:none;">
               		* 추가할 기수  <input type='text' name='p_seqcnt' class='input' maxlength='3' size='4' value='1'>개
               		<a href="#none" onclick="whenSeqSave()" class="btn01"><span>저장</span></a>
               		<a href="#none" onclick="whenSeqCancel()" class="btn01"><span>취소</span></a>
               		&nbsp;&nbsp;
                </div>                
                
		</div>
		
		
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="4%"/>
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
                    <col width="7%"/>
                    <col width="7%"/>                    				
				</colgroup>
				<thead>
					<tr>
					  <th scope="row"><a href="#none" onclick="whenOrder('subjseqgr')">기수</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('studentlimit')">정원</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('propstart')">신청시작일</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('propend')">신청종료일</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('edustart')">학습시작일</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('eduend')">학습종료일</a></th>
					  <th scope="row">상태</th>
					  <th scope="row"><a href="#none" onclick="whenOrder('proposecnt')">신청</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('cancelcnt')">취소</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('studentcnt')">승인</a></th>
					  <th scope="row"><a href="#none" onclick="whenOrder('stoldcnt')">수료</a></th>
					  <th scope="row">담당자</th>
					  <th scope="row">수정</th>
					  <th scope="row">삭제</th>
				  </tr>
				</thead>
				<tbody>
<c:forEach items="${list}" var="result" varStatus="status" >				
					<tr>
						<td class="num"><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
						<td class="num"><c:out value="${result.studentlimit}"/></td>
						<td class="num"><c:out value="${fn2:getFormatDate(result.propstart,'yyyy.MM.dd')}"/></td>
						<td class="num"><c:out value="${fn2:getFormatDate(result.propend,'yyyy.MM.dd')}"/></td>
						<td class="num"><c:out value="${fn2:getFormatDate(result.edustart,'yyyy.MM.dd')}"/></td>
						<td class="num"><c:out value="${fn2:getFormatDate(result.eduend,'yyyy.MM.dd')}"/></td>
						<td class="num">
						
						<c:choose>
							<c:when test="${result.eduterm eq '0'}">
								-
							</c:when>
							<c:when test="${result.eduterm eq '1' || result.eduterm eq '2' || result.eduterm eq '3'}">
								교육전
							</c:when>
							<c:when test="${result.eduterm eq '4'}">
								교육중
							</c:when>
							<c:when test="${result.eduterm eq '5'}">
								교육완료
							</c:when>
						</c:choose>
						
						</td>
						<td class="num"><c:out value="${result.proposecnt}"/></td>
						<td class="num"><c:out value="${result.cancelcnt}"/></td>
						<td class="num"><c:out value="${result.studentcnt}"/></td>
						<td class="num"><c:out value="${result.stoldcnt}"/></td>
						<td class="num"><c:out value="${result.musernm}"/></td>
						<td>
						<c:if test="${result.isdeleted eq 'Y'}">
							<a href="#none" onclick="cancelDeleted('${p_subj}', '${sessionScope.ses_search_gyear}', '${result.subjseq}')" class="btn_edit"><span>수정</span></a>
						</c:if>
						<c:if test="${result.isdeleted ne 'Y'}">
							<a href="#none" 
							onclick="whenSubjseq('${p_grcode}', '${sessionScope.ses_search_gyear}', '${p_grseq}', '${p_subj}', '${sessionScope.ses_search_gyear}', '${result.subjseq}', '${result.isonoff}')" 
							class="btn_edit"><span>수정</span></a>
						</c:if>
						</td>
						<td><a href="#none" 
						onclick="delSubj('${p_subj}', '${result.year}', '${result.subjseq}', '${result.stoldcnt}', ${result.proposecnt})" 
						class="btn_del"><span>삭제</span></a></td>
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
* 정렬처리 함수
******************************************************** */
function whenOrder(column) {
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/cou/grSeqDetailList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
}


//교육기수 리스트 화면으로
function goSubjectListPage() {
  thisForm.action = "/adm/cou/grSeqList.do";
  thisForm.target = "_self";
  thisForm.submit();
	  
}

//과정기수 추가(화면 디스플레이)
function whenAddSeq() {
    document.all.addseq.style.display='';
}

//과정기수 추가 취소(화면 디스플레이)
function whenSeqCancel() {
    document.all.addseq.style.display='none';
}



//과정기수 추가 (실제)
function whenSeqSave() {
  var deny_pattern = /[^(0-9)]/;

  if(deny_pattern.test(thisForm.p_seqcnt.value)) {
      alert("숫자만 입력할 수 있습니다.");
      thisForm.focus();
      return;
  }

  if (confirm(thisForm.p_seqcnt.value + "개의 과정기수를 추가하시겠습니까?")) {
	  thisForm.p_process.value = "insert";
	  thisForm.action = "/adm/cou/grSeqDetailAction.do";
	  thisForm.target = "_self";
	  thisForm.submit();
  }
}

//과정기수 삭제처리
function delSubj(subj,year,subjseq,stoldcnt,propcnt){
/*
    if (stoldcnt>0) {
        alert("수료처리취소 후 다시 시도하세요.");
        return;
    }
*/
    if (propcnt==0) {
        if(confirm("과정기수 정보를 삭제하시겠습니까?"))  {
        	thisForm.p_process.value = "delete";
            thisForm.p_subjseq.value=subjseq;
	      	thisForm.action = "/adm/cou/grSeqDetailAction.do";
	      	thisForm.target = "_self";
	      	thisForm.submit();      
        }
    } else {
        alert('신청 또는 학습자가 있어 삭제할 수 없습니다.');
        return;
    }
    

}


//기수 상세정보 팝업
function whenSubjseq(grcode,gyear,grseq,subj,year,subjseq,isonoff){

	var url = "/adm/cou/grSeqDetailView.do"
		+ '?p_grcode=' + grcode
		+ '&p_gyear=' + gyear
		+ '&p_grseq=' + grseq
		+ '&p_subj=' + subj
		+ '&p_year=' + year
		+ '&p_subjseq=' + subjseq
		+ '&p_isonoff=' + isonoff
		;

	window.open(url,"grSeqDetailViewWindowPop","resizable=yes,scrollbars=yes,status=yes,width=800,height=600");

}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
