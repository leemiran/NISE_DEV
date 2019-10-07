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
		<c:param name="selectParameter"     value="PROPCANCEL"							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
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
               		<a href="#none" onclick="whenSave()" class="btn01"><span>저 장</span></a>
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
                    <col width="2%"/>                    			
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row"><a href="#none" onclick="doOderList('grseq')" >교육기수</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('subj')" >과정명</a></th>
						<th scope="row">지역</th>
						<th scope="row">교육구분</th>
						<th scope="row">기수</th>
						<th scope="row"><a href="#none" onclick="doOderList('userid')" >ID</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('name')" >성명</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('ldate')">취소일</a></th>
						<th scope="row">취소구분</th>
						<th scope="row">취소사유</th>
						<th scope="row">결제수단</th>
						<th scope="row">금액</th>
						<th scope="row">입금여부</th>
						<th scope="row">환불여부</th>
						<th scope="row">환불일시</th>
						<th scope="row">SMS수신여부</th>
						<th scope="row"><input type="checkbox" name="checkedAlls" onclick="checkAll(this.checked)"></th>
					</tr>
				</thead>
				<tbody>
				
<c:set var="total_biyong">0</c:set>				
<c:forEach items="${list}" var="result" varStatus="status" >
<c:set var="total_biyong">${total_biyong + result.biyong}</c:set>	
					<tr>
						<td class="num"><c:out value="${status.count}"/></td>
						<td><c:out value="${result.grseqnm}"/></td>
						<td class="left"><c:out value="${result.subjnm}"/></td>
						<td>${result.areaCodenm}</td>
						<td class="num"><c:out value="${result.isonoff}"/></td>
						<td><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
						<td>
						<a href="javascript:whenMemberInfo('${result.userid}')"><c:out value="${result.userid}"/></a>
						</td>
						<td><c:out value="${result.name}"/></td>
						<td><c:out value="${fn2:getFormatDate(result.canceldate, 'yyyy.MM.dd')}"/></td>
						<td>
								<c:if test="${result.cancelkind eq 'P'}">본인취소</c:if>
								<c:if test="${result.cancelkind eq 'F'}">운영자반려</c:if>
								<c:if test="${result.cancelkind eq 'D'}">운영자삭제</c:if>
						</td>
						
						<td><c:out value="${result.reason}"/></td>
						<td><c:out value="${result.paynm}"/></td>
						<td>
							<c:if test="${result.paycd eq 'OB'}">
								<input type="text" style="text-align: right;" size="7" name="_Array_amount" value="${result.amount}">	
							</c:if>
							<c:if test="${result.paycd ne 'OB'}">
								<fmt:formatNumber value="${result.amount}" type="number"/>		
								<input type="hidden"  name="_Array_amount" value="${result.amount}">							
							</c:if>
								<input type="hidden"  name="_Array_userid" value="${result.userid}">		
							
						</td>
						<td>
							<c:if test="${result.paycd eq 'FE'}">-</c:if>
							<c:if test="${result.paycd ne 'FE'}"><c:out value="${result.enterYn}"/></c:if>
						</td>
						<td>
							<c:if test="${result.paycd eq 'FE'}">
								-
								<input type="hidden" name="_Array_repayYn">
							</c:if>
							<c:if test="${result.paycd ne 'FE'}">
								<select name="_Array_repayYn">
									<option value="Y" <c:if test="${result.repayYn eq 'Y'}">selected</c:if>>Y</option>
									<option value="N" <c:if test="${result.repayYn eq 'N'}">selected</c:if>>N</option>
								</select>
							</c:if>
						</td>
						<td>
								<c:if test="${result.paycd eq 'OB' or result.paycd eq 'FE'}">
									-
									<input type="hidden" name="_Array_repay_dt">
								</c:if>
								
								<c:if test="${result.paycd ne 'OB' and result.paycd ne 'FE'}">
									<c:if test="${empty result.repayDt}">
										<c:if test="${result.paycd ne 'OB' and result.paycd ne 'FE'}">
											<input type="text" name="_Array_repay_dt" size="10" OnClick="popUpCalendar(this, this, 'yyyy-mm-dd')" readonly>
										</c:if>
									</c:if>
									
									<c:if test="${not empty result.repayDt}">
										<c:out value="${result.repayDt}"/>
										<input type="hidden" name="_Array_repay_dt" size="10" value="${result.repayDt}">
									</c:if>
								</c:if>
								
									
								<input type="hidden" name="_Array_order_id" value="${result.orderId}">
								<input type="hidden" name="_Array_checkvalue" value="0">
						</td>
						<td>${result.issms}</td>
						<td>
							<input type="checkbox" name="_Array_p_checks" value="${result.subj},${result.year},${result.subjseq},${result.userid}" onclick="chkvalue(thisForm._Array_checkvalue, this.checked, ${status.index})"> 
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
	thisForm.action = "/adm/prop/propCancelMemberExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/prop/propCancelMemberList.do";
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
	thisForm.action = "/adm/prop/propCancelMemberList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

function checkAll(val){
	var obj1 = document.getElementsByName("_Array_p_checks");
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


function chkvalue(pobj,val,i){
	
	var obj = document.getElementsByName("_Array_p_checks");
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


// 체크박스 체크
function chkSelected() {
	var selectedcnt = 0;
	if(thisForm._Array_p_checks) {
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



//저장
function whenSave(){
	if (chkSelected() < 1) {
		alert('저장할 대상을 선택하세요!');
		return;
	}
	thisForm.action = '/adm/prop/propCancelMemberAction.do'; 
	thisForm.target = "_self";     
	thisForm.submit();

}
//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
