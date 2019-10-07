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
	<input type="hidden" id="p_seq" 			name="p_seq">
	<input type="hidden" id="p_process" 			name="p_process">
	<input type="hidden" id="p_status" 				name="p_status">
	<input type="hidden" id="p_year" 				name="p_year"					value="${ses_search_gyear}">
	<input type="hidden" id="p_subjseq" 			name="p_subjseq"				value="${ses_search_subjseq}">
	<input type="hidden" id="p_subj" 				name="p_subj"					value="${ses_search_subj}">
	<input type	= "hidden" name = "p_order" 	value= "${p_order}">
	<input type = "hidden" name = "p_orderType" value= "${p_orderType}">

			
   	<!-- 검색박스 시작-->
	
	<!-- 검색박스 끝 -->


		<div class="listTop">
              	<div class="btnR MR05">
               		<a href="#none" onclick="actionInsertPage()" class="btn01"><span>등록</span></a>
                </div>
  		</div>
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="5%"/>
                    <col width="40%"/>
                    <col width="20%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row">과정명</th>
						<th scope="row">성명</th>
						<th scope="row">금액</th>
						<th scope="row">입금일</th>
						<th scope="row">작성자</th>
						<th scope="row">작성일</th>
						<th scope="row">영수증</th>
					</tr> 
				</thead>
				<tbody>
				<c:forEach items="${list}" var="result" varStatus="status">
				<tr> 
					<td class="num"><c:out value="${status.count}"/></td>
					<td class="left"><a href="#none" onclick="cashPrintView('${result.seq}')"><c:out value="${result.subjNm}"/></a></td>
					<td class="left">${result.uname}</td>
					<td><fmt:formatNumber value="${result.amount}" type="number"/>원</td>
					<td><c:out value="${result.amountDate}"/></td>
					<td><c:out value="${result.luserid}"/><br/>(<c:out value="${result.name}"/>)</td>
					<td><c:out value="${fn2:getFormatDate(result.ldate, 'yyyy-MM-dd')}"/></td>
					<td><a href="#none" onclick="whenCashPrint('${result.seq}')"><img src="/images/user/btn_y_print.gif" alt="영수증출력" /></a></td>
				</tr>
				</c:forEach>
				<c:if test="${fn:length(list) == 0}">
				<tr>
					<td colspan="8">데이터가 없습니다.</td>
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
 * 등록페이지
 ******************************************************** */
function actionInsertPage() {
	thisForm.action = "/adm/cash/cashPrintActionPage.do";
	thisForm.submit();
}

/***********************************************************
 * 뷰/수정페이지
 ************************************************************/
function cashPrintView(seq) {
	thisForm.p_seq.value=seq;
	thisForm.action = "/adm/cash/cashPrintViewPage.do";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/book/selectCpBookList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

//영수증
function whenCashPrint(seq){

	thisForm.p_seq.value=seq;
	
	window.open('', 'whenCashPrintWindowPop', 'left=100,top=100,width=467,height=600, menubar=no,directories=no,resizable=yes,status=no,scrollbars=yes');
	thisForm.action = "/adm/cash/selectAdminCashPrint.do";
	thisForm.target = "whenCashPrintWindowPop";
	thisForm.submit();
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/book/selectCpBookList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

function chkeckall(){
    if(thisForm.checkedAlls.checked){
      whenAllSelect();
    }
    else{
      whenAllSelectCancel();
    }
}

function openDeliveryFoward(number, url){
	window.open(url+number, "delivery", "width=600, height=480, scrollbars=auto");
}


//전체선택
function whenAllSelect() {
	//alert(thisForm.all['_Array_p_checks']);
	if(document.getElementsByTagName('_Array_p_checks')) {
		if (thisForm._Array_p_checks.length > 0) {
			for (i=0; i<thisForm._Array_p_checks.length; i++) {
				thisForm._Array_p_checks[i].checked = true;
			}
		} else {
			thisForm._Array_p_checks.checked = true;
		}
	}
}
 


//전체선택취소
function whenAllSelectCancel() {
	if(document.getElementsByTagName('_Array_p_checks')) {
		if (thisForm._Array_p_checks.length > 0) {
			for (i=0; i<thisForm._Array_p_checks.length; i++) {
				thisForm._Array_p_checks[i].checked = false;
			}
		} else {
			thisForm._Array_p_checks.checked = false;
		}
	}
}

//배송완료 처리
function delivery_action (gubun){
    
	if (chkSelected() <1) {
        alert("처리할 대상을 선택하세요.");
        return;
	}

	var statusNm = "";
    if(gubun == 'Y'){
        statusNm = "배송완료로";
    }else if(gubun == 'A'){
        statusNm = "배송중으로";
    }else{
        statusNm = "수령확인으로";
    }

    if(confirm(statusNm+" 상태변경을 하시겠습니까?")){
		thisForm.action="/adm/book/updateCpBookStatus.do";
		thisForm.p_status.value= gubun;
		thisForm.submit();
    }	    
}

//체크박스 체크
function chkSelected() {
	var selectedcnt = 0;
	if(document.getElementsByTagName('_Array_p_checks')) {
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

function excel_upload() {

	if(thisForm.p_subj.value == ""){
		alert("과정을 검색해주세요");
		return;
	}

	window.open("","excelUploadPage","left=100,top=100,width=1024,height=768,toolbar=no,menubar=no,status=yes,scrollbars=yes,resizable=yes");
	thisForm.target = "excelUploadPage";
	thisForm.action="/adm/book/cpBookExcelUploadPop.do";
	thisForm.submit();
	//thisForm.reset();
}

// 정렬
function whenOrder(column) {
    if (thisForm.p_orderType.value == " asc") {
    	thisForm.p_orderType.value = " desc";
    } else {
    	thisForm.p_orderType.value = " asc";
    }

    thisForm.action = "/adm/book/selectCpBookList.do";
    thisForm.p_order.value = column;
    thisForm.submit();
}   	

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
