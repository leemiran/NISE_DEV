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
	<input type="hidden" id="p_process" 	name="p_process"   value="update">
	<input type="hidden" id="p_gyear"  		name=p_gyear    value="${p_gyear}">
	<input type="hidden" id="p_grseq"  		name="p_grseq"    value="${p_grseq}">
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
						<c:out value="${p_gyear}"></c:out>
						</td>
					</tr>
					<tr>
						<th scope="col">월</th>
						<td><select id="p_gmonth"  name="p_gmonth" >
									<option value="">선택</option>
									<c:forEach  var="xMonth" begin="1" end="12">										
										<option value="${xMonth }"  <c:if test="${view.gmonth eq xMonth}">selected</c:if>>${xMonth }</option>
									</c:forEach>
								</select>	
						</td>
					</tr>
					<tr>
						<th scope="col">교육기수명</th>
						<td>
						<input id="p_grseqnm" name="p_grseqnm" type="text" class="date" size="40" maxlength="50" value="${view.grseqnm}">
						<br />ex)11월, 2009.11.01~2009.11.30 
						</td>
					</tr>
					
					
									
				</tbody>
			</table>
		</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#none" onClick="whenSubmit()" class="pop_btn01"><span>저 장</span></a></li>
			<li><a href="#none" onClick="whenDelete()" class="pop_btn01"><span>삭 제</span></a></li>
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
    frm.p_process.value = "update";
	frm.target = "_self";
	frm.submit();
	
}


function whenDelete(){
	if(!confirm('모든 과정의 학습자정보가 삭제되며 복구할 수 없습니다.\n\n교육기수를 삭제하시겠습니까?'))	return;

	frm.action = "/adm/cou/grSeqAction.do";
    frm.p_process.value = "delete";
	frm.target = "_self";
	frm.submit();
}


</script>