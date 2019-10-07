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


	<input type="hidden" name="p_subjseqgr" 	value="${view.subjseqgr}">
	<input type="hidden" name="p_isclosed" 		value="${view.isclosed}">
	<input type="hidden" name="p_studentlimit" 	value="${view.studentlimit}">
	<input type="hidden" name="p_isaddpossible" value="${view.isaddpossible}">
	<input type="hidden" name="p_propcnt" 		value="${view.propcnt}">
	<input type="hidden" name="p_eduterm" 		value="${view.eduterm}">
	<input type="hidden" name="p_isonoff" 		value="${view.isonoff}">
	
	<input type="hidden" name="p_chkfinal">
	<input type="hidden" name="p_userlist">
	<input type="hidden" name="p_rejectidlist">      <!-- 교육반려자   Userid List --->
	<input type="hidden" name="p_chkfinalNlist">     <!-- 최종승인이 아닌자 Userid List --->
	<input type="hidden" name="p_reapprovallist">    <!-- 승인불가능   Userid List-->
	<input type="hidden" name="p_nogoyonglist">      <!-- 고용보험미적용자변경불가능   Userid List-->
	<input type="hidden" name="p_disrejlist">        <!-- 반려불가능   Userid List-->
	<input type="hidden" name="p_grduserllist">      <!-- 수료처리자 Userid List-->
	<input type="hidden" name="p_closeidlist">       <!-- 수료결재완료 Userid List-->
	<input type="hidden" name="p_onoffchkpointlist"> <!-- 온라인교육이면서 교육일이후 -->


	<input type="hidden" name="p_subj" id="p_subj" value="">
	<input type="hidden" name="p_year" id="p_year" value="">
	<input type="hidden" name="p_subjseq" id="p_subjseq" value="">
	<input type="hidden" name="p_grseq" id="p_grseq" value="">
	<input type="hidden" name="p_userids" id="p_userids" value="">



			
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value="PROP"							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
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
           		  <a href="#none" onclick="whenCommonSmsSend(<c:out value="${gsMainForm}"/>, '_Array_p_checks')" class="btn01"><span>SMS</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="#none" onclick="whenCommonMailSend(<c:out value="${gsMainForm}"/>, '_Array_p_checks')" class="btn01"><span>메일발송</span></a>
                </div>
                <div class="btnR MR05">
           		  <a href="#none" onclick="whenStudentApp('D')" class="btn01"><span>삭제</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="#none" onclick="whenStudentApp('N')" class="btn01"><span>반려</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="#none" onclick="whenStudentApp('Y')" class="btn01"><span>승인</span></a>
                </div>
              	<div class="btnR MR05">
               		<a href="#none" onclick="whenStudentAdd()" class="btn01"><span>대상자추가</span></a>
                </div>
               	<div class="btnR MR05">
               		<a href="#none" onclick="updateSubySeqMove()" class="btn01"><span>재수강자등록</span></a>
                </div>
                
</c:if>         

                       
  </div>
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="2%"/>
<!--                    <col width="%"/>-->
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
                    <col width="%"/>                    			
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
<!--						<th scope="row"><a href="#none" onclick="doOderList('companynm')" >회사명</a></th>-->
						<th scope="row"><a href="#none" onclick="doOderList('subjnm')" >과정명</a></th>
						<th scope="row">지역</th>
						<th scope="row">교육구분</th>
						<th scope="row">기수</th>
						<th scope="row">교육기간</th>
						<th scope="row"><a href="#none" onclick="doOderList('userid')" >ID</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('name')" >성명</a></th>
						<th scope="row">학교명/소속근무처</th>
						<th scope="row">신청일</th>
						<th scope="row">승인여부</th>
						<th scope="row">결재방법변경</th>
						<th scope="row">수료여부</th>
						<th scope="row">SMS수신여부</th>
						<th scope="row"><input type="checkbox" name="checkedAlls" onclick="chkeckall()"></th>
					</tr>
				</thead>
				<tbody>
				
<c:set var="total_biyong">0</c:set>				
<c:forEach items="${list}" var="result" varStatus="status" >
<c:set var="total_biyong">${total_biyong + result.biyong}</c:set>	
					<tr>
						<td class="num"><c:out value="${status.count}"/></td>
<!--						<td><c:out value="${result.companynm}"/></td>-->
						<td class="left"><c:out value="${result.subjnm}"/></td>
						<td>${result.areaCodenm}</td>
						<td class="num"><c:out value="${result.isonoff}"/></td>
						
						<td>
								<c:out value="${fn2:toNumber(result.subjseq)}"/>
						</td>
						
						<td><c:out value="${fn2:getFormatDate(result.edustart, 'yy.MM.dd')}"/>~<c:out value="${fn2:getFormatDate(result.eduend, 'yy.MM.dd')}"/></td>
						<td>
						<a href="javascript:whenMemberInfo('${result.userid}')"><c:out value="${result.userid}"/></a>
						</td>
						<td><c:out value="${result.name}"/></td>
						<td>
							<c:if test="${result.empGubun eq 'R'}">
					        	<c:out value="${result.positionNm}"/>
					       	</c:if>
					       	<c:if test="${result.empGubun ne 'R'}">
					        	<c:out value="${result.userPath}"/>
       						</c:if>
						</td>
						<td><c:out value="${fn2:getFormatDate(result.appdate, 'yyyy.MM.dd HH:mm:ss')}"/></td>
<!--						<td><c:out value="${fn2:getFormatDate(result.approvaldate, 'yyyy.MM.dd HH:mm:ss')}"/></td>-->
						<td>
								<c:if test="${result.chkfinal eq 'B'}">미처리</c:if>
								<c:if test="${result.chkfinal eq 'Y'}">승인</c:if>
								<c:if test="${result.chkfinal eq 'N'}">반려</c:if>
								<c:if test="${result.chkfinal eq 'D'}">삭제</c:if>
							
						</td>
						<td>
						
							<c:if test="${result.paycd eq 'PB' || result.paycd eq 'OB'}">
								<a href="#none" class="btn_search"  
								onclick="whenProposeType('${result.subj}','${result.year}','${result.subjseq}','${result.userid}','${result.orderId}');"
								>
								<span>${result.pay}</span>
								</a>
							</c:if>
							<c:if test="${result.paycd ne 'PB' && result.paycd ne 'OB'}">
								${result.pay}
							</c:if>
							
							
						</td>
						<td>
								<c:if test="${result.grdvalue eq 'Y'}">수료</c:if>
								<c:if test="${result.grdvalue eq 'M'}">미처리</c:if>
								<c:if test="${result.grdvalue eq 'N'}"><font color="red">미수료</font></c:if>
						</td>
						<td><c:out value="${result.issms}"/></td>
						<td>
							<input type="checkbox" name="_Array_p_checks" id="_Array_p_checks" value="${result.subj},${result.year},${result.subjseq},${result.userid},${result.orderId}">
							<%-- <input type="checkbox" name="_Array_p_checks" id="_Array_p_checks" value="${result.userid}"> --%>
							<input type="hidden" name="_Array_p_seluserid" value="${result.userid}">
							<input type="hidden" name="_Array_p_isonoff" value="${result.isonoff}">
							<input type="hidden" name="_Array_p_eduterm" value="${result.eduterm}">
							<input type="hidden" name="_Array_p_isclosed" value="${result.isclosed}">
							<input type="hidden" name="_Array_p_rejectpossible" value="${result.rejectpossible}">
							<input type="hidden" name="_Array_p_chkfinal" value="${result.chkfinal}">
							<c:if test="${not empty result.examnum}"><input type="hidden" name="_Array_p_ExamNum_chk" value="Y"></c:if>
							<c:if test="${empty result.examnum}"><input type="hidden" name="_Array_p_ExamNum_chk" value="N"></c:if>
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

$(function (){
	$(".selSeq").change(function (){
		//과정코드(0)_기존기수(1)_userid(2)_새로운기수(3)
		var param = new String($(this).val()).split("_");
		if(param[1] == param[3])return;
		$.ajax({  
   			url: "/adm/prop/updateMemberSubjSeqInfo.do",  
   			data: {ajp_subj:param[0],ajp_oldsubjseq:param[1],ajp_userid:param[2],ajp_newsubjseq:param[3],ajp_year:$("#ses_search_year").val()},
   			dataType: 'json',
   			contentType : "application/json:charset=utf-8",
   			success: function(data) {   
   				data = data.result;   	
   				if(data == "Y"){
   					alert("기수변경이 정상적으로 변경되었습니다.");
   				}else if(data == "A"){
   					alert("선택한 기수의 등록된 수강생입니다.");
   					var selectSeq = param[0]+"_"+param[1]+"_"+param[2]+"_"+param[1]
   					$(this).val(selectSeq).attr("selected","selected");
   					
   				}else{
   					
   				}
   				doPageList($("#pageIndex").val());
   			},    
   			error: function(xhr, status, error) {   
   				alert(status);   
   				alert(error);    
   			}   
   		});  
		
	});
	
});
/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	thisForm.action = "/adm/prop/studentManagerExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/prop/studentManagerList.do";
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
	thisForm.action = "/adm/prop/studentManagerList.do";
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

//조건체크
function chkSelected() {
	var selectedcnt = 0;
	if(document.getElementsByTagName('_Array_p_checks')) {
		if (thisForm._Array_p_checks.length > 0) {
			for (i=0; i<thisForm._Array_p_checks.length; i++) {
				if (thisForm._Array_p_checks[i].checked == true) {
					selectedcnt++;

					//교육반려자 userid list
					if(thisForm._Array_p_chkfinal[i].value == "N"){
						thisForm.p_rejectidlist.value += thisForm._Array_p_seluserid[i].value+", ";
					}

					//최종승인이 아닌자 userid List - 반려시 제약
					if(thisForm._Array_p_chkfinal[i].value != "Y"){
						thisForm.p_chkfinalNlist.value +=  thisForm._Array_p_seluserid[i].value+", ";
					}

					//승인불가능 userid List
					//if(thisForm._Array_p_isclosed[i].value == "Y" || thisForm._Array_p_chkfinal[i].value != "N"){
					if(thisForm._Array_p_isclosed[i].value == "Y"){
						thisForm.p_reapprovallist.value +=  thisForm._Array_p_seluserid[i].value+", ";
					}

					//고용보험미적용자변경불가능 userid List
					if(thisForm._Array_p_isclosed[i].value == "Y" || thisForm._Array_p_chkfinal[i].value != "Y"){
						thisForm.p_nogoyonglist.value +=  thisForm._Array_p_seluserid[i].value+", ";
					}

					//수료결재완료 userid List
					if(thisForm._Array_p_isclosed[i].value == "Y"){
						thisForm.p_closeidlist.value += thisForm._Array_p_seluserid[i].value+", ";
					}

					//온라인교육이면서 교육일이후인것 userid List
					if(!(thisForm._Array_p_isonoff[i].value == "ON" || parseInt(thisForm._Array_p_eduterm[i].value)>3)){
						thisForm.p_onoffchkpointlist.value += thisForm._Array_p_seluserid[i].value+", ";
					}
					
				}
			}
		} else {
			if (thisForm._Array_p_checks.checked == true) {
				selectedcnt++;

				//교육반려자 userid
				if(thisForm._Array_p_chkfinal.value == "N"){
					thisForm.p_rejectidlist.value += thisForm._Array_p_seluserid.value+", ";
				}

				//최종승인이 아닌자 userid List - 반려시 제약
				if(thisForm._Array_p_chkfinal.value != "Y"){
					thisForm.p_chkfinalNlist.value +=  thisForm._Array_p_seluserid.value+", ";
				}

				//승인불가능 userid
				if(thisForm._Array_p_isclosed.value == "Y" ||thisForm._Array_p_chkfinal.value != "N"){
					thisForm.p_reapprovallist.value +=  thisForm._Array_p_seluserid.value+", ";
				}

				//고용보험미적용자변경불가능 userid List
				if(thisForm._Array_p_isclosed.value == "Y" || thisForm._Array_p_chkfinal.value != "Y"){
					thisForm.p_nogoyonglist.value +=  thisForm._Array_p_seluserid.value+", ";
				}

				//반려 불가능 userid
				if(thisForm._Array_p_rejectpossible.value == "N"){
					thisForm.p_disrejlist.value += thisForm._Array_p_seluserid.value+", ";
				}

				//수료결재완료 userid
				if(thisForm._Array_p_isclosed.value == "Y"){
					thisForm.p_closeidlist.value += thisForm._Array_p_seluserid.value+", ";
				}

				//온라인교육이면서 교육일이후인것 userid List
				if(!(thisForm._Array_p_isonoff.value == "ON" || parseInt(thisForm._Array_p_eduterm.value)>3)){
					thisForm.p_onoffchkpointlist.value += thisForm._Array_p_seluserid.value+", ";
				}
			}
		}
	}

	return selectedcnt;
}

//대상자 추가
function whenStudentAdd() {
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
	if(thisForm._Array_p_ExamNum_chk)
	{
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
	}

	
    if(exam == true){
		alert("수험번호가 이미 생성되어 해당과정에 더이상 대상자를 추가할수 없습니다.");
    	return;
	}

	
	if(thisForm.p_isclosed.value == "Y"){
		alert("수료처리 되어 대상자를 추가할수 없는 상태입니다.");
		return;
	}


	var url = '/adm/prop/acceptTargetMemberList.do'
		+ '?p_grseq=<c:out value="${sessionScope.ses_search_grseq}"/>'
		+ '&p_subj=<c:out value="${sessionScope.ses_search_subj}"/>'
		+ '&p_subjseq=<c:out value="${sessionScope.ses_search_subjseq}"/>'
		+ '&p_year=<c:out value="${sessionScope.ses_search_year}"/>'
		;
		
	window.open(url,"acceptTargetMemberListWindowPop","width=1024,height=600,scrollbars=yes");

}


//교육청일괄등록 및 무통장입금 결제방법변경(신용카드, 가상계좌 결제 정보가 넘어오지 않은경우 사용함)
function whenProposeType(subj, year, subjseq, userid, orderid)
{
	var url = '/adm/prop/studentPayTypeView.do'
		+ '?p_subj=' + subj
		+ '&p_year=' + year
		+ '&p_subjseq=' + subjseq
		+ '&p_userid=' + userid
		+ '&p_orderid=' + orderid
		;
		
	window.open(url,"propStudentPayTypeViewWindowPop","width=500,height=400,scrollbars=yes");
	
}




//초기화
function clearlistcode(){
	thisForm.p_rejectidlist.value        = "";
	thisForm.p_chkfinalNlist.value        = "";
	thisForm.p_reapprovallist.value      = "";
	thisForm.p_nogoyonglist.value        = "";
	thisForm.p_disrejlist.value          = "";
	thisForm.p_closeidlist.value         = "";
	thisForm.p_userlist.value            = "";
	thisForm.p_grduserllist.value        = "";
	thisForm.p_onoffchkpointlist.value   = "";
}

//반려자 재승인처리
function whenStudentApp(p_chkfinal){
	
	if(thisForm.p_isclosed.value == "Y"){
		alert("수료처리 되어 대상자를 승인할수 없습니다.");
		//return;
	}

	if (chkSelected() <1) {
		alert("수강대상자를 선택하세요.");
		return;
	}
	
	
	if(p_chkfinal == "Y")
	{
		
		var value2 = thisForm.p_reapprovallist.value;
		value2 = value2.replace(/(^\s*)|(\s*$)/g, "");    
		if(value2.length > 1) {
			alert("승인처리할수 없는 대상이 있습니다.!!!!\n\n<승인할수 없는대상>\n"+value2.substring(0,value2.length-1));
			alert("<승인대상조건>\n\n 현재 반려처리되있는 수강생중 수료처리 이전상태  ");
			clearlistcode();
			return;
		}
	}
	else if(p_chkfinal == "N")
	{
		var value = thisForm.p_chkfinalNlist.value;
		value = value.replace(/(^\s*)|(\s*$)/g, "");    
		if (value != "") {
			if(value.length > 1) {
				alert("승인되지 않은대상은 반려처리할수 없습니다.!!!!\n\n<반려 할수 없는대상>\n"+value.substring(0,value.length-1));
			}
			clearlistcode();
			return;
		}

		if (thisForm.p_closeidlist.value != "") {
			alert("수료처리 완료된 대상은 반려할수 없습니다!!!!\n\n<반려 할수 없는대상>\n"+thisForm.p_closeidlist.value);
			clearlistcode();
			return;
		}

		if (thisForm.p_rejectidlist.value != "") {
			alert("이미 반려처리되어 있는 대상이 있습니다!!!!\n\n<반려 할수 없는대상>\n"+thisForm.p_rejectidlist.value);
			clearlistcode();
			return;
		}
			
	}
	else if(p_chkfinal == "D")
	{
		if (thisForm.p_closeidlist.value != "") {
			alert("수료처리 완료된 대상은 삭제할수 없습니다!!!!\n\n<삭제 할수 없는대상>\n"+thisForm.p_closeidlist.value);
			clearlistcode();
			return;
		}
		
	}
	
	clearlistcode();

	thisForm.p_chkfinal.value = p_chkfinal;
	thisForm.action = "/adm/prop/studentManagerAction.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//재수강등록
function updateSubySeqMove()
{
	/* if(thisForm.p_isclosed.value == "Y"){
		alert("수료 처리되어 대상자를 재수강 등록할 수 없습니다.");
		return;
	} */
	
	/* 
	var cnt = 0;
	for (i=0; i<thisForm._Array_p_checks.length; i++) {
		if (thisForm._Array_p_checks[i].checked == true) {
			if(thisForm._Array_p_chkfinal[i].value == "B"){
			thisForm.p_chkfinalNlist.value +=  thisForm._Array_p_seluserid[i].value+", ";
			cnt++;
			}
		}
	} */
	
	
	var cnt = 0;

	for(var i=0;i<thisForm.length;i++){
		if (thisForm.elements[i].name == "_Array_p_checks" && thisForm.elements[i].checked) {
			//thisForm.p_chkfinalNlist.value +=  thisForm.elements[i].value+", ";

			var p_chkfinalNlist = thisForm.elements[i].value;
			var a_chkfinalNlist = p_chkfinalNlist.split(",");
		
			//thisForm.p_chkfinalNlist.value =  thisForm.elements[i].value;
			thisForm.p_chkfinalNlist.value =  a_chkfinalNlist[3];
			cnt++; 
		}
	}

	if (cnt <1) {
		//alert("선택된 수강대상자가 없거나 승인여부가 미처리인 수강생만 재수강등록을 할 수 있습니다.");
		alert("선택된 수강대상자가 없습니다.");
		return;
	}
	
	if (cnt >=2) {
		//alert("선택된 수강대상자가 없거나 승인여부가 미처리인 수강생만 재수강등록을 할 수 있습니다.");
		alert("한명만 석택하세요");	
		clearlistcode();
		return;
	}
	
	var value = thisForm.p_chkfinalNlist.value;
	
	//alert(value);
	
	
	
	thisForm.p_subj.value = $("#ses_search_subj").val();
	thisForm.p_year.value = $("#ses_search_gyear").val();
	thisForm.p_subjseq.value = $("#ses_search_subjseq").val();
	thisForm.p_grseq.value = $("#ses_search_grseq").val();
	thisForm.p_userids.value = value;
	
	/* var url = '/adm/prop/studentManagerView.do'
		+ '?p_subj=' + $("#ses_search_subj").val()
		+ '&p_year=' + $("#ses_search_gyear").val()
		+ '&p_subjseq=' + $("#ses_search_subjseq").val()
		+ '&p_grseq=' + $("#ses_search_grseq").val()	
		+ '&p_userids=' + value		
		; */
	clearlistcode();
	
	window.open("","propStudentPayTypeViewWindowPop","width=700,height=350,scrollbars=yes");
	
	thisForm.target = "propStudentPayTypeViewWindowPop"
    thisForm.action = "/adm/prop/studentManagerView.do";
	thisForm.submit();
	
}
//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
