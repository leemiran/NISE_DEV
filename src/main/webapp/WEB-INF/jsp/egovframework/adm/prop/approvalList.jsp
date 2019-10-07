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
		<c:param name="selectViewType"      value="B_1"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value="PROP"							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<div class="listTop">
			<p class="floatL point">TOTAL : <span class="num1"><c:out value="${fn:length(list)}"/></span> 명</p>
            	
            	<div class="btnR">
                	<a href="#none" onclick="whenXlsDownLoad('delete')" class="btn_excel"><span>삭제자엑셀출력</span></a>
          		</div>
                <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDownLoad('approval')" class="btn_excel"><span>엑셀출력</span></a>
                </div>
                
<!--                총괄관리지만 보이도록 한다.-->
<c:if test="${fn:indexOf(sessionScope.gadmin, 'A') > -1}">                
                <!--  
                <div class="btnR MR05">
           		  <a href="#none" onclick="whenChangeAll(3)" class="btn_search01" title="리스트의 수강생의  처리상태를 모두 삭제상태로 변경합니다."><span>삭제상태로</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="#none" onclick="whenChangeAll(2)" class="btn_search01" title="리스트의 수강생의  처리상태를 모두 반려상태로 변경합니다."><span>반려상태로</span></a>
                </div>
                <div class="btnR MR05">
           		  <a href="#none" onclick="whenChangeAll(0)" class="btn_search01" title="리스트의 수강생의  처리상태를 모두 취소상태로 변경합니다."><span>취소상태로</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="#none" onclick="whenChangeAll(1)" class="btn_search01" title="리스트의 수강생의  처리상태를 모두 승인상태로 변경합니다."><span>승인상태로</span></a>
                </div>
                -->
                <div class="btnR MR05">
               		<a href="#none" onclick="whenApprovalProcess()" class="btn01" title="선택된 수강생의 처리상태를 서버에 저장합니다."><span>확인(저장)</span></a>
                </div>
<!--                  
                <div class="btnR MR05">
               		<a href="#none" onclick="whenApprovalProcessSelect()" class="btn01" title="선택된 수강생을 무조건 수강승인상태로 저장합니다."><span>선택자강제승인처리</span></a>
                </div>
-->                
                
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
                    <!--<col width="%"/>-->
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="80px"/>
                    <col width="%"/>
                    <col width="2%"/>                    			
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row" colspan="2" ><a href="#none" onclick="doOderList('scsubj')" >과정명</a></th>
						<th scope="row">지역</th>
						<th scope="row">교육구분</th>
						<th scope="row">차수</th>
						<th scope="row"><a href="#none" onclick="doOderList('edustart')" >연수기간</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('userid')" >ID</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('name')" >성명</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('emp_gubun')" >회원구분</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('user_path')" >학교명/소속근무처</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('appdate')" >신청일시</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('ldate')" >승인일시</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('chkfinal')" >처리상태</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('paycd')" >결제수단<br />
						  				(입금예정일)
						  				</a>
                        </th>
						<th scope="row"><a href="#none" onclick="doOderList('enter_dt')" >입금일</a></th>
						<th scope="row">비고</th>
						<th scope="row">수강료</th>
						<th scope="row"><input type="checkbox" name="checkedAlls" onclick="checkAll(this.checked)"></th>
					</tr>
				</thead>
				<tbody>
				
<c:set var="total_biyong">0</c:set>				
<c:forEach items="${list}" var="result" varStatus="status" >
	
	<c:if test="${result.paycd eq 'RE' || result.paycd eq 'FE'}">
		<c:set var="total_biyong">${total_biyong}</c:set>
	</c:if>
	<c:if test="${result.paycd ne 'RE' && result.paycd ne 'FE'}">
		<c:if test="${result.paycd eq 'OB'}">
			<c:set var="total_biyong">${total_biyong + result.biyong2}</c:set>
		</c:if>
		<c:if test="${result.paycd ne 'OB'}">
			<c:set var="total_biyong">${total_biyong + result.biyong}</c:set>
		</c:if>
								 
		
	</c:if>
					<tr>
						<td class="num"><c:out value="${status.count}"/></td>
						<td colspan="2" class="left"><c:out value="${result.subjnm}"/></td>
						<td>${result.areaCodenm}</td>
						<td class="num"><c:out value="${result.isonoff}"/></td>
						<td><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
						<td><c:out value="${fn2:getFormatDate(result.edustart, 'yy.MM.dd')}"/>~<c:out value="${fn2:getFormatDate(result.eduend, 'yy.MM.dd')}"/></td>
						<td>
						<a href="javascript:whenMemberInfo('${result.userid}')"><c:out value="${result.userid}"/></a>
						</td>
						<td><c:out value="${result.name}"/></td>
						<td>
							<c:choose>
								<c:when test="${result.empGubun eq 'T'}">교원</c:when>
								<c:when test="${result.empGubun eq 'E'}">보조인력</c:when>
								<c:when test="${result.empGubun eq 'R'}">교육전문직</c:when>
								<c:when test="${result.empGubun eq 'P'}">일반인(학부모)</c:when>
								<c:when test="${result.empGubun eq 'O'}">공무원</c:when>
								<c:otherwise>기타</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:if test="${result.empGubun eq 'R'}">
								<c:out value="${result.positionNm}"/>
							</c:if>
							<c:if test="${result.empGubun ne 'R'}">
								<c:out value="${result.userPath}"/>
							</c:if>
							
						</td>
						<td><c:out value="${fn2:getFormatDate(result.appdate, 'yyyy.MM.dd HH:mm:ss')}"/></td>
						<td><c:out value="${fn2:getFormatDate(result.approvaldate, 'yyyy.MM.dd HH:mm:ss')}"/></td>
						<td>
							
							<input type="hidden" name="_Array_p_params" value="${result.subj},${result.year},${result.subjseq},${result.userid}">
							<input type="hidden" name="_Array_p_final_chk" value="${result.chkfinal}">
							<select name='_Array_p_chkfinal'>
								<option value='B'>미처리</option>
								<option value='Y' 	<c:if test="${result.chkfinal eq 'Y'}">selected</c:if>>승인</option>
								<option value='N' 	<c:if test="${result.chkfinal eq 'N'}">selected</c:if>>반려</option>
								<option value='D' 	<c:if test="${result.chkfinal eq 'D'}">selected</c:if>>삭제</option>
							</select>
							
						</td>
						<td>
						
							<c:if test="${result.paycd eq 'PB' || result.paycd eq 'OB' || result.paycd eq 'RE' || result.paycd eq 'FE'}">
								<select name="_Array_p_paytype">
									<option value="PB"	<c:if test="${result.paycd eq 'PB'}">selected</c:if>>무통장</option>
									<option value="OB"	<c:if test="${result.paycd eq 'OB'}">selected</c:if>>교육청일괄납부</option>
									<option value="RE"	<c:if test="${result.paycd eq 'RE'}">selected</c:if>>재수강</option>
									<option value="FE"	<c:if test="${result.paycd eq 'FE'}">selected</c:if>>무료</option>
								</select>
							</c:if>
							<c:if test="${result.paycd ne 'PB' && result.paycd ne 'OB' && result.paycd ne 'RE' && result.paycd ne 'FE'}">
								<input type="hidden" name="_Array_p_paytype" value="${result.paycd}">
								<c:out value="${result.pay}"/>
							</c:if>
							<c:if test="${not empty result.enteranceDt}">
								<br/>(<c:out value="${result.enteranceDt}"/>)
							</c:if>
							
						</td>
						<td>
							<c:if test="${result.paycd eq 'OB' || result.paycd eq 'RE'}">
								-
								<input type="hidden" name="_Array_p_enter_dt"> 
							</c:if>
							<c:if test="${result.paycd ne 'OB' && result.paycd ne 'RE'}">
								<input type="text" name="_Array_p_enter_dt" size="10" value="${result.enterDt}"
								OnDblClick="this.value = '';" OnClick="popUpCalendar(this, this, 'yyyy-mm-dd')" readonly /> 
							</c:if>
							
							<input type="hidden" name="_Array_p_paycd" value="${result.paycd}"> 
							<input type="hidden" name="_Array_p_order_id" value="${result.orderId}">
							
						</td>
						<td>
						<c:if test="${not empty result.etc}">
							<a href="#none"  class="btn_search01"  
							onclick="whenEtc('${result.subj}','${result.year}','${result.subjseq}','${result.userid}');"
							onMouseOver="document.all.divtag_<c:out value="${status.count}"/>.style.display='block';" 
							onMouseOut="document.all.divtag_<c:out value="${status.count}"/>.style.display='none';"
							>
							<span>보기</span>
							</a>
							<div style="position:absolute;background:white;width:150px;display:none;" id="divtag_<c:out value="${status.count}"/>">
								<table width="150" border="0" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF">
								<tr><td class="left" height="50"><c:out value="${result.etc}"/></td></tr>
								</table>
							</div>
						</c:if>
						<c:if test="${empty result.etc}">
							<a href="#none" class="btn_search"  
							onclick="whenEtc('${result.subj}','${result.year}','${result.subjseq}','${result.userid}');"
							>
							<span>보기</span>
							</a>
						</c:if>
						
						</td>
						<td class="num">
							<c:if test="${result.paycd eq 'RE' || result.paycd eq 'FE'}">
								0
							</c:if>
							<c:if test="${result.paycd ne 'RE' && result.paycd ne 'FE'}">
								<c:if test="${result.paycd eq 'OB'}">
									<fmt:formatNumber value="${result.biyong2}" type="number"/>
								</c:if>
								<c:if test="${result.paycd ne 'OB'}">
									<fmt:formatNumber value="${result.biyong}" type="number"/>
								</c:if>
								
							</c:if>
						
						</td>
						<td>
							<input type="checkbox" name="_Array_appcheck" id="_Array_appcheck" onclick="chkvalue(thisForm._Array_checkvalue, this.checked, ${status.index})"> 
							<input type="hidden" name="_Array_checkvalue" id="_Array_checkvalue" value="0">
						</td>
					</tr>
</c:forEach>			
					<tr>
						<td colspan="17">합계</td>
						<td class="num" colspan="2"><fmt:formatNumber value="${total_biyong}" type="number"/></td>
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
function whenXlsDownLoad(p_process) {
	thisForm.action = "/adm/prop/approvalExcelDown.do";
	thisForm.p_process.value = p_process;
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/prop/approvalList1.do";
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
	thisForm.action = "/adm/prop/approvalList1.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

function chkvalue(pobj,val,i){
	
	var obj = document.getElementsByName("_Array_appcheck");
	if(obj)
	{
	    if(obj.length > 1){
	    	if(val){
				pobj[i].value="1";
			}else{
				pobj[i].value="0";
			}
		}else{
		    if(val){
				pobj.value="1";
			}else{
				pobj.value="0";
			}
		}
	}
}


function checkAll(val){
	var obj1 = document.getElementsByName("_Array_appcheck");
	var obj2 = document.getElementsByName("_Array_checkvalue");
	
	if(val==true){
		if(obj1.length > 1){	
			for(i=0;i < obj1.length;i++){
				obj1[i].checked = true;
				obj2[i].value="1";
			}
		}else{
			obj1[0].checked = true;
			obj2[0].value="1";
		}
	}else{
		if(obj1.length > 1){
			for(i=0;i < obj1.length;i++){
				obj1[i].checked = false;
				obj2[i].value="0";
			}
		}else{
			obj1[0].checked = false;
			obj2[0].value="0";
		}
	}
}

//비고 팝업
function whenEtc(subj, year, subjseq, userid) {

	var url = '/adm/prop/approvalEtcPopup.do'
	+ '?p_grcode=<c:out value="${sessionScope.grcode}"/>'
	+ '&p_subj=' + subj
	+ '&p_year=' + year
	+ '&p_subjseq=' + subjseq
	+ '&p_userid=' + userid
	;
		
	window.open(url,"ApprovalEtcPopupWindowPop","width=660,height=450,scrollbars=yes");
}



//전체 옵션선택 버튼
function whenChangeAll(idx){
	var varCnt = 0;

	for(var i=0;i<thisForm.length;i++){
		if ( thisForm.elements[i].name == "_Array_p_chkfinal") {
			varCnt++; 
		}
	}

	if ( varCnt == 0 ) { 
		if ( idx == 1 )
			alert("승인할 데이타가 없습니다. 수강생을 선택하세요!");
		else if ( idx == 0 )	
			alert("승인 취소할 데이타가 없습니다. 수강생을 선택하세요!");
		else if ( idx == 2 )	
			alert("반려할 데이타가 없습니다. 수강생을 선택하세요!");
		else if ( idx == 3 )	
			alert("삭제할 데이타가 없습니다. 수강생을 선택하세요!");
		return;	
	}	

	if(!confirm("변경가능한 승인옵션이 모두 변경됩니다.\n\n계속하시겠습니까?")) return;

	for(var i=0;i<thisForm.length;i++){
		if(thisForm.elements[i].name == "_Array_p_chkfinal"){
			thisForm.elements[i].selectedIndex=idx;
		}
	}

	alert("현재 화면의 상태가 변경되었습니다.\n[선택자상태저장] 버튼을 클릭하셔야 현재 상태가 서버에 저장됩니다");

}


//승인(취소,삭제)처리
function whenApprovalProcess(){

	var varCnt = 0;

	for(var i=0;i<thisForm.length;i++){
		if (thisForm.elements[i].name == "_Array_appcheck" && thisForm.elements[i].checked) {
			varCnt++; 
		}
	}

	if ( varCnt == 0 ) { 
		alert("선택된 수강생이 없습니다. 수강생을 선택하세요!");
		return;
	}
	
	if (!confirm("선택된 수강생의 정보를 저장 하시겠습니까?")) {
		return;
	}
	
	thisForm.action = "/adm/prop/approvalAction.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
