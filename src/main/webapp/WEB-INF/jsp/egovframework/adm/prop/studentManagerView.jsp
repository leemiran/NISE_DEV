<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
self.close();
alert("${resultMsg}");
opener.location.reload();
</c:if>
-->
</script>

<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
	<input type="hidden" id="ajp_subj"  		name="ajp_subj"    		value="${p_subj}">
	<input type="hidden" id="ajp_year"  		name="ajp_year"    		value="${p_year}">
	<input type="hidden" id="ajp_oldsubjseq"  	name="ajp_oldsubjseq"   value="${p_subjseq}">
	<input type="hidden" id="ajp_newsubjseq"  	name="ajp_newsubjseq"   value="">
	<input type="hidden" id="ajp_grseq"  		name="ajp_grseq"    	value="${p_grseq}">
	<input type="hidden" id="ajp_userids"  		name="ajp_userids"    	value="${p_userids}">
	<input type="hidden" id="p_reSubjseq_1"     name="p_reSubjseq_1"    value="" >
	<input type="hidden" id="p_reSubjseq_2"     name="p_reSubjseq_2"    value="" >
	<input type="hidden" id="p_reSubjseq_3"     name="p_reSubjseq_3"    value="" >
	<input type="hidden" id="p_reSubjseq_4"     name="p_reSubjseq_4"    value="" >
	
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>재수강자등록</h2>
         </div>
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
                <colgroup>
                	<col width="10%" />
                   	<col width="40%" />
                   	<col width="10%" />
                   	<col width="40%" />
                </colgroup>
                <tbody>
                	<tr>
                    	<th scope="col">수강생</th>
                    	<td>${subjseqInfo.name}(${subjseqInfo.userid})</td>
                   	</tr>
                   	<tr>
                    	<th scope="col">최초 신청 기수</th>
                    	<td><c:out value="${subjseqInfo.grseqnm}"/> <c:out value="${subjseqInfo.subjseq}"/>기
							<c:out value="${subjseqInfo.edustart}"/> ~ <c:out value="${subjseqInfo.eduend}"/>
						</td>
                   	</tr>
                   	<!-- <tr>
                    	<th scope="col">재수강 처리</th>
                    	<td>
                    		&nbsp;<input name="p_reSubjseq_1" id="p_reSubjseq_1" type="checkbox" value="Y" class="vrM"> 재수강등록
                    		&nbsp;<input name="p_reSubjseq_2" id="p_reSubjseq_2" type="checkbox" value="Y" class="vrM"> 진도
                    		&nbsp;<input name="p_reSubjseq_3" id="p_reSubjseq_3" type="checkbox" value="Y" class="vrM"> 온라인시험
                    		&nbsp;<input name="p_reSubjseq_4" id="p_reSubjseq_4" type="checkbox" value="Y" class="vrM"> 과제
                    	</td>
                   	</tr> -->
                   	<tr>
                    	<th scope="col">연기사유</th>
                    	<td><input type="text" name="p_reSubjseq_comment" id="p_reSubjseq_comment" /></td>
                   	</tr>			
				</tbody>
            </table>
                
			<table summary="" width="98%" class="popTb">
				<colgroup>
					<col width="5%" />
					<col width="8%" />
					<col width="34%" />
					<col width="30%" />
					<col width="23%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="row"></th>
						<th scope="row">기수</th>
						<th scope="row">과정명</th>
						<th scope="row">교육기수</th>
						<th scope="row">교육기간</th>
					</tr>
				</thead>		
				<tbody>
				<c:if test="${fn:length(resultList) == 0}">
						<tr>
						<td colspan="5" style="text-align: center">
							<spring:message code="common.nodata.msg" />
						</td>
						</tr>
					</c:if>  
				<c:forEach items="${resultList}" var="result" varStatus="status" >
					<tr>
						<td ><input type="radio" name="p_check" id="p_check" onchange="updateSubjSeq('<c:out value="${result.subjseq}"/>');" value="<c:out value="${result.subjseq}"/>"></td>
						<td ><c:out value="${result.subjseq}"/></td>
						<td ><c:out value="${result.subjnm}"/></td>
						<td ><c:out value="${result.grseqnm}"/></td>
						<td ><c:out value="${result.edustart}"/> ~ <c:out value="${result.eduend}"/></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#none" onClick="whenPaySave()" class="pop_btn01"><span>저 장</span></a></li>
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
function whenPaySave() {
	
	/* var cnt = 0;
	if (frm.p_reSubjseq_1.checked == true) {		
		cnt++;
	}
	if (frm.p_reSubjseq_2.checked == true) {		
		cnt++;
	}
	if (frm.p_reSubjseq_3.checked == true) {		
		cnt++;
	}
	if (frm.p_reSubjseq_4.checked == true) {		
		cnt++;
	}
	
	if(cnt==0){
		alert("선택된 재수강 처리가 없습니다.");
		return
	} */
	
	if(frm.p_reSubjseq_comment.value == ""){
		alert("연기사유를 입력하세요.");
		return
	}
	
	if(frm.ajp_newsubjseq.value == "")
	{
		alert("선택된 기수 없습니다.");
		return
	}
	
	if(confirm("선택된 기수로 재수강 하시겠습니까?"))
	{
		frm.action = "/adm/prop/updateSubjseq.do";
		frm.target = "_self";
		frm.submit();
	}
}

function updateSubjSeq(seq){
	$("#ajp_newsubjseq").val(seq);
}


</script>