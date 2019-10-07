<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>


<jsp:useBean id="now" class="java.util.Date"></jsp:useBean>
<fmt:formatDate value="${now}" pattern="yyyy" var="nowYear"/>
<c:if test="${empty p_gyear}">
	<c:set var="p_gyear">${nowYear}</c:set>
</c:if>

<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
    <input type="hidden" id="p_isblended"   name="p_isblended"     value="N">    				<!--in relation to select-->
    <input type="hidden" id="p_isexpert"    name="p_isexpert"      value="N">    				<!--in relation to select-->
	<input type="hidden" id="p_process" 	name="p_process"   value="insert">
	<input type="hidden" id="p_grcode"  	name="p_grcode"    value="${sessionScope.grcode}">
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>기수개설</h2>
         </div>
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="25%" />
					<col width="75%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">교육그룹</th>
						<td class="bold">국립특수교육원</td>
					</tr>
					<tr>
						<th scope="col">년도</th>
						<td>
						<input id="p_gyear" name="p_gyear" type="text" class="date" size="4" value="${p_gyear}">
						&nbsp;&nbsp;ex)2012
						</td>
					</tr>
					<tr>
						<th scope="col">월</th>
						<td><select id="p_gmonth"  name="p_gmonth" >
									<option value="">선택</option>
									<c:forEach  var="xMonth" begin="1" end="12">										
										<option value="${xMonth }"  <c:if test="${p_gmonth eq xMonth}">selected</c:if>>${xMonth }</option>
									</c:forEach>
								</select>	
						</td>
					</tr>
					<tr>
						<th scope="col">교육기수명</th>
						<td>
						<input id="p_grseqnm" name="p_grseqnm" type="text" class="date" size="40" maxlength="50" value="${p_grseqnm}">
						<br />ex)11월, 2009.11.01~2009.11.30 
						</td>
					</tr>
					<tr>
						<th scope="col">연결과정 승계</th>
						<td>
							 
							<select id="p_makeoption" name="p_makeoption" onchange="javascript:changeMakeoption(this);">
								<option value="MANUAL" <c:if test="${p_makeoption eq 'MANUAL'}">selected</c:if>>직접 선택/패키지과정</option>
								<option value="MAKE_ALL" <c:if test="${p_makeoption eq 'MAKE_ALL'}">selected</c:if>>교육그룹에 등록된 과정 모두 일괄생성</option>
								<option value="SELECT_ALL" <c:if test="${p_makeoption eq 'SELECT_ALL'}">selected</c:if>>선택 교육기수에 등록된 과정 모두 일괄생성</option>
							</select>
							<span id="span_packageyn" style="display:block">
							※ 페키지 과정 여부 : <input type="checkbox" name="p_package" value="Y"/>
							</span>
						</td>
					</tr>
					<tr id="idcopyseq" style="display:none;"> 
						<th scope="col"><b>복사대상교육기수</b></th>
						<td>
						교육년도 : 
						<select name="p_copy_gyear" id="p_copy_gyear" onchange="whenSelection()">
							<option value="">::선택::</option>
						<c:forEach items="${year_list}" var="result">
							<option value="${result.gyear}" <c:if test="${result.gyear eq p_copy_gyear}">selected</c:if>>${result.gyear}</option>
						</c:forEach>
						</select>
						<br/>
						교육기수 : 
						<select name="p_copy_grseq" id="p_copy_grseq" style="width:270px;">
						</select>
						
						
						<input type="hidden" id="p_bfcheck" name="p_bfcheck" value="O"/>
						</td>
					</tr>
									
				</tbody>
			</table>
		</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#none" onClick="whenSubmit()" class="pop_btn01"><span>저 장</span></a></li>
			<li><a href="#none" onClick="window.close()" class="pop_btn01"><span>닫 기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
</form>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var frm = document.<c:out value="${gsPopForm}"/>;
//저장
function whenSubmit() {

	if (document.getElementById("p_gyear").value.length != 4){
        alert("년도를 4자리로 입력하여 주십시오");
        document.getElementById("p_gyear").focus();
        return;
    }
    
    if (isNaN(document.getElementById("p_gyear").value)) {
    	alert("년도는 숫자만 입력가능합니다.");
        document.getElementById("p_gyear").focus();
        return;
    }
    
    if (document.getElementById("p_gmonth").value.length == 0){
    	alert("월을 선택하여 주십시오");
        document.getElementById("p_gmonth").focus();
        return;
    }
    
    if (document.getElementById("p_grseqnm").value.length < 2){
        alert("교육기수명을 2~25자리로 입력하여 주십시오");
        document.getElementById("p_grseqnm").focus();
        return;
    }
    
   
    frm.action = "/adm/cou/grSeqAction.do";
	frm.target = "_self";
	frm.submit();
}

//셀렉트 선택후 검색
function whenSelection() {
	frm.action = "/adm/cou/grSeqInsert.do";
	frm.target = "_self";
	frm.submit();
}


//연결과정승계 콤보박스 선택시수
function changeMakeoption(value) {
	if (value.value == "SELECT_ALL") {
		document.getElementById("idcopyseq").style.display=""; 
	} else { 
		document.getElementById("idcopyseq").style.display="none";
	}

	document.getElementById("p_package").checked = false;

	if(value.value == "MANUAL")
	{
		document.getElementById("span_packageyn").style.display="";
	} else { 
		document.getElementById("span_packageyn").style.display="none";
	}
}

//페이지 로딩시 연결과정승계에 따른 복사대상 교육기수 표시여부
function body_onLoad() {
	<c:if test="${p_makeoption eq 'SELECT_ALL'}">
	document.getElementById("idcopyseq").style.display="";
	getGrSeqJsonData();
	</c:if> 
}


//교육기수가져오기
function getGrSeqJsonData() { 
	$("#p_copy_grseq").html('');
	$("#p_copy_grseq").append('<option value=\"\">::선택::</option>');
	
	$.ajax({  
		url: "<c:out value="${gsDomainContext}" />/com/aja/sch/selectGrSeqList.do",  
		data: {searchGyear : function() {return $("#p_copy_gyear").val();}},
		dataType: 'json',
		contentType : "application/json:charset=utf-8",
		success: function(data) {   
			data = data.result;
			for (var i = 0; i < data.length; i++) {
				var value = data[i].grseq;
				var title = data[i].grseqnm

				if('<c:out value="${p_copy_grseq}"/>' == value)
					$("select#p_copy_grseq").append("<option value='"+value+"' selected>"+title+"</option>");
				else
					$("select#p_copy_grseq").append("<option value='"+value+"'>"+title+"</option>");
			}
		},    
		error: function(xhr, status, error) {   
			alert(status);   
			alert(error);    
		}   
	});   
} 	



if(window.addEventListener) {
    window.addEventListener("load", body_onLoad, false);
} else if(window.attachEvent) {
    window.attachEvent("onload", body_onLoad);
}


</script>