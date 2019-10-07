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

			
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value="STUDENT"							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
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
           		  <a href="#none" onclick="whenCommonSmsSend(kniseForm, '_Array_p_checks')" class="btn01"><span>SMS</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="#none" onclick="whenCommonMailSend(kniseForm, '_Array_p_checks')" class="btn01"><span>메일발송</span></a>
                </div>
              	<div class="btnR MR05">
               		<a href="#none" onclick="whenExamNum()" class="btn01"><span>수험번호</span></a>
                </div>
                
</c:if>         

                       
  </div>
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="80"/>
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
						<th scope="row">수험번호</th>
						<th scope="row"><a href="#none" onclick="doOderList('subj')" >과정명</a></th>
						<th scope="row">지역</th>
						<th scope="row">교육구분</th>
						<th scope="row">기수</th>
						<th scope="row"><a href="#none" onclick="doOderList('userid')" >ID</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('name')" >성명</a></th>
						<th scope="row">핸드폰번호</th>
						<th scope="row">학교명/소속근무처</th>
						<th scope="row">출석고사장</th>
						<th scope="row">연수지명번호</th>
						<th scope="row">SMS수신여부</th>
						<th scope="row"><input type="checkbox" name="checkedAlls" onclick="checkAll(this.checked)"></th>
					</tr>
				</thead>
				<tbody>
				
<c:forEach items="${list}" var="result" varStatus="status" >
					<tr>
						<td class="num">
							<c:out value="${result.examnum}"/>
							<c:if test="${empty result.examnum}">
								<input type="hidden" name="_Array_p_ExamNum_chk" value="N">
							</c:if>
							<c:if test="${not empty result.examnum}">
								<input type="hidden" name="_Array_p_ExamNum_chk" value="Y">
							</c:if>
						</td>
						<td class="left"><c:out value="${result.subjnm}"/></td>
						<td>${result.areaCodenm}</td>
						<td class="num"><c:out value="${result.isonoff}"/></td>
						<td><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
						<td>
						<a href="javascript:whenMemberInfo('${result.userid}')"><c:out value="${result.userid}"/></a>
						</td>
						<td><c:out value="${result.name}"/></td>
						<td><c:out value="${result.handphone}"/></td>
						<td>
							<c:if test="${result.empGubun eq 'R'}">
					        	<c:out value="${result.positionNm}"/>
					       	</c:if>
					       	<c:if test="${result.empGubun ne 'R'}">
					        	<c:out value="${result.userPath}"/>
       						</c:if>
						</td>
						<td><c:out value="${result.gosa}"/></td>
						<td><c:out value="${result.lecSelNo}"/></td>
						<td><c:out value="${result.issms}"/></td>
						<td>
							<input type="checkbox" name="_Array_p_checks" value="${result.subj},${result.year},${result.subjseq},${result.userid}">
                           	<input type="hidden" name="_Array_p_subj" value="${result.subj}">
                        	<input type="hidden" name="_Array_p_year" value="${result.year}">
                        	<input type="hidden" name="_Array_p_subjseq" value="${result.subjseq}"> 
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
	thisForm.action = "/adm/stu/stuMemberExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/stu/stuMemberList.do";
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
	thisForm.action = "/adm/stu/stuMemberList.do";
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



// 수험번호 생성
function whenExamNum(){

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
	
	var exam = false;
	if (thisForm._Array_p_ExamNum_chk.length > 0) {
    	for (i=0; i<thisForm._Array_p_ExamNum_chk.length; i++) {
        	if(thisForm._Array_p_ExamNum_chk[i].value == 'Y'){
        		exam = true;
        		break;
        	}else{
   				exam = false;
        	}	
		}
    }
    if(exam == true){
		alert("수험번호가 이미 생성되었습니다.");
    	return;
	}
    
	if(confirm("수험번호를 생성하시게 되면 입과처리를 더이상 할수 없습니다.\n수험번호를 생성하시겠습니까?")){
		thisForm.action = "/adm/stu/stuMemberAction.do";
		thisForm.target = "_self";
		thisForm.submit();
		
	}

	
}
//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
