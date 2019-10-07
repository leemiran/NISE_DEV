<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" 	 uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" 		 uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" 	 uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" 	 uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" 		 uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" 	 uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>

<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonPopUpHead.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/svt/jquery.form.js"></script>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
<!--login check-->
<script  type="text/javascript">
//<![CDATA[
<c:if test="${!empty resultMsg}">
alert("${resultMsg}");
var url = "/usr/mpg/memMyPage.do?menu_main=6&menu_sub=3&menu_tab_title=마이페이지&menu_sub_title=개인정보수정";
window.close();
opener.document.location.replace(url);
</c:if>
//]]>
</script>
<script type="text/javascript">

if('' != '${validResult}') 
{
	alert('${validResult}');
	opener.location.reload();
	window.close();
}


	var msg = "";


<c:choose>
	<c:when test="${view.a1 > 0}">
		//수강신청지남
		msg += "\n${name}님께서는\n이미 수강신청하셨습니다.";
	</c:when>
	<c:when test="${view.a2 > 0}">
		//정원초과
		msg += "\n선택하신 과정은\n정원초과입니다.";
	</c:when>
	<c:when test="${view.a3 > 0}">
		//이미수료한과정
		msg += "\n선택하신 과정은\n이미 수료한 과정이므로 수강신청 하실 수 없습니다.";
	</c:when>
	<c:when test="${view.a4 > 0}">
		//다른기수를 학습하고 있는과정
		msg += "\n선택하신 과정은\n이미 수강중인 과정입니다.";
	</c:when>
	<c:when test="${view.a5 > 0}">
		//다른기수를 신청한 과정이면 안됨
		msg += "\n선택하신 과정은\n이미 수강신청하신 과정입니다.";
	</c:when>
	<c:when test="${view.a6 > 0}">
		//수강신청데이터중에 반려데이터가있을경우
		msg += "\n수강신청과정중에 반려된과정이 있습니다.\n운영자에게 문의하세요.";		
	</c:when>	
	<c:when test="${view.a7 >= 2  && view.a9 >= 201708 }">
		//신청기간동안 연수 신청 가능 갯수 2개로 제한
		msg += "\n2개이상 연수신청을 하실 수 없습니다.";	
	</c:when>
	<c:when test="${view.a8 > 0 && view.a9 >= 201709 }">
		//이전 수강 중 미이수 데이터가 있을 경우
		msg += "\n이전 수강 중 미이수 하신 과정이므로 수강신청을 하실 수 없습니다.";	
	</c:when>
	
</c:choose>


	if(msg != "") 
	{
		alert("" + msg);
		opener.location.reload();
		window.close();
	}

</script>

</head>

<body style="background:none;">

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
		<input type="hidden"  name="p_subj" value="${p_subj}" />
		<input type="hidden"  name="p_year" value="${p_year}" />
		<input type="hidden"  name="p_subjseq" value="${p_subjseq}" />
		<input type="hidden"  name="p_gubun" value="" />
		<input type="hidden"  name="p_addrFlag" value="N" />
		
<div id="money"><!-- 팝업 사이즈 800*650 -->
	<div class="con2">
		<div class="popCon">
			<!-- header -->
			<div class="tit_bg">
			<h2>수강신청</h2>
       		</div>
			<!-- //header -->










<!-- contents -->
			<div class="mycon">				
            	
              <!-- 과정타이틀 -->
              <div class="bbsList3">
				<ul>
					<li>
						<dl>
<c:set var="v_edutimes" value="0" />
<c:set var="v_newroom" value="" />


<c:forEach items="${subjinfo}" var="result" varStatus="status"> 
<c:set var="v_edutimes" value="${result.edutimes}" />		
<c:set var="v_usebook" value="${result.usebook}" />		
<c:set var="subjnmVal" value="${result.subjnm}" />
<c:if test="${result.status == 0}">
<c:set var="v_newroom" value="${result.neweroom}" />
</c:if>
		
							<dt class="subject">
                            	<span class="head">강&nbsp;의&nbsp;명</span>
								<span class="head" style="font-weight:bold;color:blue;">${result.subjnm}</span>	                               
                            </dt>							
                            <dd class="info">
								<span class="head">정&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;원</span>
								<span class="txt"><fmt:formatNumber value="${result.studentlimit}" type="number"/>명</span>	                               
							</dd>                            
                            <dd class="info">								
                                <span class="head">이수시간</span>
								<span class="txt">${result.edutimes}시간</span>							
							</dd>
                            <dd class="info">
								<span class="head">회원구분</span>
								<span class="txt">
								<c:if test="${meminfo.empGubun eq 'P'}">일반회원(학부모 등)</c:if>
								<c:if test="${meminfo.empGubun eq 'T'}">교원</c:if>
								<c:if test="${meminfo.empGubun eq 'E'}">보조인력</c:if>
								<c:if test="${meminfo.empGubun eq 'R'}">교육 전문직</c:if>
								</span>								
							</dd>
                            <dd class="info">								
								<span class="head">수강기간</span>
                                <span class="txt">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</span>
							</dd>
                            <%-- <dd class="info">
								<span class="head">연&nbsp;수&nbsp;비</span>
								<span class="txt"><fmt:formatNumber value="${result.biyong}" type="number"/>원</span>
							</dd>  --%>
</c:forEach>
							
							
                      <!-- <dd class="info">
								<span class="head">연수구분</span>
								<span class="txt_1">- 시도교육청의 요청에 의해 개설되는 특별과정은 시도교육청이 연수비를 부담합니다.
								<br />(다음단계에서 교육청일괄납부을 선택하시면 됩니다.)</span>
							</dd>              -->
					  </dl>
                        
				  </li>					
				</ul>
			</div>
            <!-- //과정타이틀 -->     
            <c:if test="${emp_gubun eq 'T' or emp_gubun eq 'R'}">   
            <div class="sub_text" style="border-bottom:1px solid #e2e2e2;">
                    <h4>교원 직무연수 과정은 연수지명번호가 필요합니다.<br/>(수강신청 이후에도 입력가능)</h4>
           	</div>  
                    
                
                <ul class="course_1">
                
                <c:if test="${fn:length(subjinfo) > 1}">
                	<c:forEach items="${subjinfo}" var="result" varStatus="status">
                		<%-- <li>${result.subjnm}<br/>- <label for="p_lec_sel_no">연수지명번호 : </label><input style="width:140px" name="p_lec_sel_no" id="p_lec_sel_no" maxlength="100"  class="t"/></li> --%>
                		
                		<li>${result.subjnm}<br/>- <label for="p_lec_sel_no">연수지명번호 : </label>
                		
                		<!-- <input style="width:140px" name="p_lec_sel_no" id="p_lec_sel_no" maxlength="100"  class="t"/> -->
                		
                		<input type="hidden" name="p_lec_sel_no" id="id_p_lec_sel_no_${status.count}"/>
                		
                		
                		<input type="text" name="p_lec_sel_no_${status.count}_0" id="p_lec_sel_no_${status.count}_0" value="" size="4" maxlength="4">
                		-<input type="text" name="p_lec_sel_no_${status.count}_1" 	id="p_lec_sel_no_${status.count}_1" value="" size="10" maxlength="20">
                		-<input type="text" name="p_lec_sel_no_${status.count}_2" 	id="p_lec_sel_no_${status.count}_2" value="" size="4" maxlength="4">
                		-<input type="text" name="p_lec_sel_no_${status.count}_3"	 id="p_lec_sel_no_${status.count}_3" value="" size="4" maxlength="4">
                        
                		
                		
                		</li>
                		
                		
                	</c:forEach>
                </c:if>
                	
                <c:if test="${fn:length(subjinfo) <= 1}">
                	<li><label for="p_lec_sel_no">연수지명번호 : </label>
                	
                	<!-- <input style="width:140px" name="p_lec_sel_no" id="p_lec_sel_no" maxlength="100"  class="t"/> -->
                	
                	<input type="hidden" name="p_lec_sel_no" id="id_p_lec_sel_no_1"/>
                	
                	<input type="text" name="p_lec_sel_no_1_0" id="p_lec_sel_no_1_0" value="" size="4" maxlength="4">
                		-<input type="text" name="p_lec_sel_no_1_1" 	id="p_lec_sel_no_1_1" value="" size="10" maxlength="20">
                		-<input type="text" name="p_lec_sel_no_1_2" 	id="p_lec_sel_no_1_2" value="" size="4" maxlength="4">
                		-<input type="text" name="p_lec_sel_no_1_3"	 id="p_lec_sel_no_1_3" value="" size="4" maxlength="4">
                		
                	
                	</li>
                </c:if>
                	
                	
                	<li class="last">※  입력형식 : 16개 광역시도명-학교명-당해연도 두자리-교내 연수번호
                	<br/>(예: 경기-경기초-12-055)
                	<br/>(수강신청 이후에도 입력가능 - 연수지명번호는 1,2과정 각각 다르게 입력하셔야합니다.)
                	<br/>※ 특수교육보조인력, 학부모연수는 해당되지 않습니다.
                	</li>
                </ul>
                
                
<c:if test="${v_edutimes >= 60}">
				<div class="sub_text" style="border-bottom:1px solid #e2e2e2;">
                    <h4>출석평가를 위하여 출석고사희망지역을 선택하여 주십시오.<br/>(61시간이상과정만해당)</h4>
           		</div>
				<ul class="course_1">
					<li> 출석고사 희망지역 : 
					<ui:code id="p_is_attend" selectItem="" gubun="schoolRoom" codetype="${v_newroom}"  
					upper="" title="출석고사장" className="" type="select" selectTitle="::선택::" event="" itemRowCount="1"/>
                	</li>
					<li class="last">※ 61시간 이상 직무연수는 연수기간 마지막날에 출석시험이 있습니다.
					<br/>학교내외 행사 등으로 출석평가 응시가 곤란한 선생님들은 수강신청을 하시지 않도록 부탁드립니다.
					<br/>※ 특수교육보조인력, 학부모연수 해당되지 않습니다.
					</li>
				</ul>
</c:if>
                </c:if>
                <%-- <input type="hidden" name="v_usebook_yn" value="${v_usebook }" /> --%>
                <input type="hidden" name="v_usebook_yn" value="N" />
<%-- <c:if test="${v_usebook eq 'Y'}"> --%>
<c:if test="${'N' eq 'Y'}">
              <div class="sub_text" style="border-bottom:1px solid #e2e2e2;">
                    <h4>교재수령지</h4>
               </div>  
                    
                
                <ul class="course_1">
                	<li>수령지구분 : 
                	<input name="p_hrdc2" type="radio" value="C" class="vrM" id="p_hrdc2_1" onclick="addressChange('1');"/><label for="p_hrdc2_1">학교/직장</label> 
                	<input name="p_hrdc2" type="radio" value="H"  class="vrM" id="p_hrdc2_2" checked onclick="addressChange('2');"/><label for="p_hrdc2_2">자택</label>
                	<input name="p_address_change" id="p_address_change" type="checkbox" onclick="addChange();"/><label for="p_address_change">주소변경(다음버튼 클릭시 저장됨)</label> 
                	</li>
                    <!-- div id="address_change_view" style="display:none;" -->
                    <li>우편번호 : <input type="text" name="p_post1" size="10" id="i_post1" value="${meminfo.zipCd1 }" maxlength="3" readonly  class="t" title="우편번호 앞자리"/>
								-
								<input type="text" name="p_post2" size="10" id="i_post2" value="${meminfo.zipCd2 }" maxlength="3" readonly  class="t" title="우편번호 뒷자리"/>  
								<a href="#none" onclick="searchZipcode()" title="새창"><img src="/images/user/btn_post.gif" alt="우편번호찾기"/></a>
					</li>
                    <li>수령주소 : <input type="text" name="p_address1" size="65" id="i_address1"  value="${meminfo.address}"  class="t" title="수령주소"/></li>
<!--                	<li class="last">※ 시도교육청의 요청에 의해 개설된 특별과정은 출석고사장을 추후에 별도로 공지함</li>-->

<li class="last">※ 주소 입력은 도로명으로 하지마시고 지번으로 상세하게 입력해주세요.<br/>
예) 번지수 및 학교명, 아파트 호수까지 상세입력( 000학교/ 00아파트 00동000호)</li>
					<!-- </div> -->
                </ul>     
				</c:if>
            
              <!-- button -->
              <ul class="btnCen mrt20">
              <input type="radio" name="p_pay_sel" value="FE" checked style="display:none;"/>
                <li><a href="#none" onclick="whenPBOBSubmit()" class="pop_btn01"><span>수강신청</span></a></li>                
              </ul>
              <!-- // button -->
                
                
		  </div>
			<!-- //contents -->




			
			
			
			
			
			
			
			
			
			
		</div>
	</div>
	
<!-- 페이지 정보 -->
	<%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
	<!-- 페이지 정보 -->
</div>
<!-- // wrapper -->

</form>


<!--아래는 팝업과 뷰를 따로 스크립트를 프로그램한다.-->
<script type="text/javascript">
<!--
var frm = eval('document.<c:out value="${gsMainForm}"/>');

//우편번호찾기
function searchZipcode(){
	window.open('', 'zipcodeWindowPop', 'width=500,height=600');
	frm.action = "/com/pop/searchZipcodePopup.do";
	frm.target = "zipcodeWindowPop";
	frm.submit();
}

//우편번호 넣기
function receiveZipcode(arr){
	frm.p_post1.value = arr[0];
	frm.p_post2.value = arr[1];
	frm.p_address1.value = arr[2];
}

function addressChange(ch){
	if(ch == '1'){
		frm.p_post1.value = "<c:out value='${meminfo.szipCd1}'/>";
		frm.p_post2.value = "<c:out value='${meminfo.szipCd2}'/>";
		frm.p_address1.value = "<c:out value='${meminfo.address1}'/>";
	}else{
		frm.p_post1.value = "<c:out value='${meminfo.zipCd1}'/>";
		frm.p_post2.value = "<c:out value='${meminfo.zipCd2}'/>";
		frm.p_address1.value = "<c:out value='${meminfo.address}'/>";
	}

	if($('input:checkbox[name="p_address_change"]').is(":checked")){
		$("#i_post1").val("");
		$("#i_post2").val("");
		$("#i_address1").val("");
	}else{
		if(ch == '1'){
			frm.p_post1.value = "<c:out value='${meminfo.szipCd1}'/>";
			frm.p_post2.value = "<c:out value='${meminfo.szipCd2}'/>";
			frm.p_address1.value = "<c:out value='${meminfo.address1}'/>";
		}else{
			frm.p_post1.value = "<c:out value='${meminfo.zipCd1}'/>";
			frm.p_post2.value = "<c:out value='${meminfo.zipCd2}'/>";
			frm.p_address1.value = "<c:out value='${meminfo.address}'/>";
		}
	}
}

function addChange(){
	var radio_value = $('input:radio="p_hrdc2"]:checked').val();
	//if(frm.p_address_change.checked){
	//	frm.p_addrFlag.value = 'Y';
	//	$("#address_change_view").show();
	//}else{
	//	frm.p_addrFlag.value = 'N';
	//	$("#address_change_view").hide();
	//}
	if($('input:checkbox[name="p_address_change"]').is(":checked")){
		if(radio_value == 'H'){
			$("#i_post1").val("");
			$("#i_post2").val("");
			$("#i_address1").val("");
		}else if(radio_value == 'C'){

			$("#i_post1").val("");
			$("#i_post2").val("");
			$("#i_address1").val("");
		}
	}else{
		if(radio_value == 'H'){
			frm.p_post1.value = "<c:out value='${meminfo.zipCd1}'/>";
			frm.p_post2.value = "<c:out value='${meminfo.zipCd2}'/>";
			frm.p_address1.value = "<c:out value='${meminfo.address}'/>";
		}else if(radio_value == 'C'){
			frm.p_post1.value = "<c:out value='${meminfo.szipCd1}'/>";
			frm.p_post2.value = "<c:out value='${meminfo.szipCd2}'/>";
			frm.p_address1.value = "<c:out value='${meminfo.address1}'/>";
		}
	}
}


function whenConfirm(){
	var chk = 0;

	<c:if test="${v_usebook eq 'Y'}">
	 for(i=0; i<frm.p_hrdc2.length; i++){
	 	if(frm.p_hrdc2[i].checked == true){
	 		chk = 1;
	 		frm.p_gubun.value=frm.p_hrdc2[i].value;
	 	}
	 }	
 	if(chk==0){
 		alert("교재수령지 구분을 선택해 주세요.");
 		frm.p_hrdc2[0].focus();
 		return;
	}
	  
	if(frm.p_post1.value == "" ||frm.p_address1.value == ""){
		alert("교재수령지 주소를 입력해 주십시오. ");
		frm.p_address1.focus();
		return;	
	}
	</c:if>


	<c:forEach items="${subjinfo}" var="result" varStatus="status">
		/* var a_${status.count} = p_lec_sel_no_check('p_lec_sel_no_${status.count}');
		if(a_${status.count} == false){
			return;
		} */		
		
		var p_lec_sel_val = $("#p_lec_sel_no_${status.count}_0").val() +"-"+ $("#p_lec_sel_no_${status.count}_1").val() +"-"+ $("#p_lec_sel_no_${status.count}_2").val() +"-"+ $("#p_lec_sel_no_${status.count}_3").val();
		if(p_lec_sel_val == "---"){
			p_lec_sel_val = "";
		}
		$("#id_p_lec_sel_no_${status.count}").val(p_lec_sel_val);	
		
	</c:forEach>	
	//return;
	frm.action = "/usr/subj/subjProposeStep02.do";
	frm.target = "_self";
	frm.submit();
}

document.title="수강신청 1단계("+"<c:out value='${subjnmVal}'/> )"+" : 개설교육과정/신청";


//상태저장
function p_lec_sel_no_check(p_lec_sel_no){
	
	if($("#" + p_lec_sel_no + "_0").val() == "")	{	
		alert("연수지명번호를 입력해 주세요!");		
		$("#" + p_lec_sel_no + "_0").focus();
		return false;
	}	
	if($("#" + p_lec_sel_no + "_1").val() == "")
	{
			alert("연수지명번호를 입력해 주세요!");
			$("#" + p_lec_sel_no + "_1").focus();
			return false;
	}
	if($("#" + p_lec_sel_no + "_2").val() == "")
	{
			alert("연수지명번호를 입력해 주세요!");
			$("#" + p_lec_sel_no + "_2").focus();
			return false;
	}
	if($("#" + p_lec_sel_no + "_3").val() == "")
	{
			alert("연수지명번호를 입력해 주세요!");
			$("#" + p_lec_sel_no + "_3").focus();
			return false;
	}
	
	
}	
//-->
function whenPBOBSubmit()
{
		
	<c:forEach items="${subjinfo}" var="result" varStatus="status">
	
	var p_lec_sel_val = $("#p_lec_sel_no_${status.count}_0").val() +"-"+ $("#p_lec_sel_no_${status.count}_1").val() +"-"+ $("#p_lec_sel_no_${status.count}_2").val() +"-"+ $("#p_lec_sel_no_${status.count}_3").val();
	if(p_lec_sel_val == "---"){
		p_lec_sel_val = "";
	}
	$("#id_p_lec_sel_no_${status.count}").val(p_lec_sel_val);	
	
	</c:forEach>	
 
	$.ajax({
		url: "${pageContext.request.contextPath}/usr/subj/subjProposeStepAction.do"
		, type: 'post'
		, data: $('#${gsMainForm}').serialize()
		, dataType: 'json'
		, success: function(result) {
			alert(result.resultMsg);
			if(result.resultCode == 'SUCCESS' && result.joinYn == 'N') {
				opener.popLifetimeJoin(result);
			}
			setTimeout("opener.location.reload()", 500);
			setTimeout("self.close()", 600);
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}
</script>

</body>
</html>