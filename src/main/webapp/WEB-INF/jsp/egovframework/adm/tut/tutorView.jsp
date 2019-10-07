<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	<input type="hidden" id="p_process" 			name="p_process">

	<input type="hidden" name="p_search"			value = "name">
	<input type="hidden" name="p_searchtext"			value = "">
	<input type="hidden" name="p_compcd"		value = "1001">
	<input type="hidden" name="p_fmon"			value = "">
	<input type="hidden" name="p_tmon"			value = "">
	<input type="hidden" name="p_pagegubun"		value = "">
	<input type="hidden" name="p_tutorgubun"	value = "${view.tutorgubun}">
	<input type="hidden" name="p_ischk"			value= "0">
	<input type="hidden" name="p_isoverlap"		value= "N">
	<input type="hidden" name="p_isinfo"		value= "N">
	<input type="hidden" name="p_cono"			value= "">
	<input type="hidden" name="p_saoi"    		value= "">
	<input type="hidden" name="p_gadmin"    	value= "P1">
	
	
	<c:if test="${empty p_userid}">
	
   		<div class="m_searchWrap">
            	<span class="shTit"><span class="num1">비회원</span> : 
					<input type="checkbox" name="p_saoiChk" onclick="do_saoi();" class="vrM"/> (강사등록시 자동으로 회원 등록됨)  
				</span>			
    	</div>
	</c:if>
	
	
		<!-- list table-->
		<div class="tbDetail">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="15%" />
					<col width="35%" />
                    <col width="15%" />
					<col width="35%" />					
				</colgroup>
				<thead>
				
				
				<c:if test="${sessionScope.gadmin eq 'A1' || sessionScope.gadmin eq 'A2'}">
                        <tr>
                            <th scope="row"><strong>강사권한여부</strong></th>
                            <td colspan="3" scope="row">
                            
                            <input type="radio" name="p_manager" value="Y" onclick="auth();" <c:if test="${view.ismanager eq 'Y' || empty p_userid}">checked</c:if>>
				              Yes 
				            <input type="radio" name="p_manager" value="N" onclick="auth();" <c:if test="${view.ismanager eq 'N'}">checked</c:if>>
				              No
				            
                            </td>
                        </tr>
                 </c:if>  
	            <c:if test="${sessionScope.gadmin ne 'A1' && sessionScope.gadmin ne 'A2'}">
                	<input type="hidden" name="p_manager" value="N">
                </c:if>
                
                
                
                        <tr id="term" style="display:<c:if test="${view.ismanager eq 'N' || empty p_userid}">none</c:if>">
                            <th scope="row"><strong>권한사용기간</strong></th>
                            <td colspan="3" scope="row">
                            	<input name="p_sdate" type="text" size="10" maxlength="10" readonly value="${fn2:getFormatDate(view.fmon, 'yyyy.MM.dd')}"> 
                            	<img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_sdate, 'yyyy.mm.dd')" /> ~
                                <input name="p_ldate" type="text" size="10" maxlength="10" readonly value="${fn2:getFormatDate(view.tmon, 'yyyy.MM.dd')}"> 
                                <img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_ldate, 'yyyy.mm.dd')" />
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">* 성명</th>
                            <td scope="row">
                            
                            <c:if test="${empty p_userid}">
                            <input onkeypress="searchSubjnmKeyEvent(event)" name="p_name" type="text" size="10" maxlength="10" value="${view.name}">
                            <a href="#none" onclick="tutor_search()"  id="srchnm" class="btn_search01"><span>회원조회</span></a>
                            </c:if>
                            
                            <c:if test="${not empty p_userid}">
                            <input name="p_name" type="text" size="10" maxlength="10" value="${view.name}" readonly >
                            </c:if>
                            
                            </td>
                            <th scope="row"><span id="span_userid">* 아이디</span></th>
                            <td scope="row">
                            
                            
                            <input name="p_userid" 		id="p_userid" type="text" size="20" maxlength="13" readonly value="${p_userid}">
                            <input name="p_userid_temp" id="p_userid_temp" type="hidden" value="${p_userid}">
                            
                            <c:if test="${empty p_userid}">
						  	&nbsp; <a href="javascript:mem_check()" class="btn_search01" id="srchid"><span>아이디체크</span></a>
						  	</c:if>
							
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">* 비밀번호</th>
                            <td scope="row"><input name="p_loginpw" type="password" size="20" maxlength="20" value="${view.pwd}"></td>
                            <th scope="row">* 비밀번호 확인</th>
                            <td scope="row"><input name="p_loginpw2" type="password" size="20" maxlength="20" value="${view.pwd}"></td>
                        </tr>
                        
                        <tr>
                        	<th scope="row">E-mail</th>
                            <td scope="row" colspan="3"><input name="p_email" type="text" size="40" maxlength="40" readonly onkeyup="isValidEmail(this)" value="${view.email}"></td>
                        </tr>
                        <tr>
                            <th scope="row">주소</th>
                            <td colspan="3" scope="row">
                            	 <input type="text" name="p_post1" size="5" maxlength="5" readonly value="${view.post1}">
			                    <%-- - 
			                    <input type="text" name="p_post2" size="3"  maxlength="3" readonly value="${view.post2}"> --%> 
			                    <a href="javascript:sample6_execDaumPostcode()" id="addr"><img src ="/images/adm/btn/btn_post.gif" alt="우편번호" /></a><br />
                                
                                <input name="p_addr" type="text" size="100" maxlength="50"  value="${view.add1}">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">전화</th>
                            <td scope="row"><input name="p_phone" type="text" size="20" maxlength="13" readonly value="${view.phone}">(-구분)</td>
                            <th scope="row">휴대폰</th>
                            <td scope="row"><input name="p_handphone" type="text" size="20" maxlength="13" readonly value="${view.handphone}">(-구분)</td>
                        </tr>
                        <tr>
                            <th scope="row">강사료</th>
                            <td colspan="3" scope="row"><input name="p_price" type="text" size="20" maxlength="7" style="text-align:right" value="${view.price}"></td>
                        </tr>
                        <tr>
                            <th scope="row">은행</th>
                            <td scope="row">
                                <ui:code id="p_bank" selectItem="${view.bank}" gubun="" codetype="0102"  upper="" levels="1" title="은행" className="" type="select" selectTitle="::선택::" event="" />
                                
                            </td>
                            <th scope="row">계좌번호</th>
                            <td scope="row"><input name="p_account" type="text" size="20" maxlength="20" value="${view.account}"> (-없이)</td>
                        </tr>
                        <tr>
                            <th scope="row">현재소속</th>
                            <td scope="row"><input name="p_comp" type="text" size="40" maxlength="40" readonly value="${view.comp}"></td>
                            <th scope="row">직위</th>
                            <td scope="row"><input name="p_jik" type="text" size="20" maxlength="20" readonly value="${view.jik}"></td>
                        </tr>
                        <tr id="subj" style="display: <c:if test="${view.ismanager eq 'N' || empty p_userid}">none</c:if>">
                            <th scope="row">강의과정</th>
                            <td colspan="3" scope="row" valign="top">
                            
                            <a href="javascript:searchSubj()" class="btn_search01"><span>과정검색</span></a> 
                            &nbsp;&nbsp;&nbsp;&nbsp;
		                    <a href="javascript:delSubj()" class="btn_del"><span>과정삭제</span></a> 
		                    <br/>
		                    
		                    
                            <select name="_Array_p_subj" size="10"  multiple style="width:680px">
<c:forEach items="${subjList}" var="result" varStatus="status" >
			<option value="${result.subj}">[${result.subj}] ${result.subjnm}</option>
</c:forEach>
                    		</select> 
                    		
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">강사소개말</th>
                            <td colspan="3" scope="row"><textarea name="p_intro" cols="110" style="height:80" rows="4"><c:out value="${view.intro}"/></textarea></td>
                        </tr>
                        <tr>
                            <th scope="row">학력사항</th>
                            <td colspan="3" scope="row"><textarea name="p_academic" cols="110" style="height:80" rows="4"><c:out value="${view.academic}"/></textarea></td>
                        </tr>
                        <tr>
                            <th scope="row">경력사항</th>
                            <td colspan="3" scope="row"><textarea name="p_career" cols="110" style="height:80" rows="4"><c:out value="${view.career}"/></textarea></td>
                        </tr>
                        <tr>
                            <th scope="row">저서</th>
                            <td colspan="3" scope="row"><textarea name="p_book" cols="110" style="height:80" rows="4"><c:out value="${view.book}"/></textarea></td>
                        </tr>
                        <tr>
                            <th scope="row">전공분야</th>
                            <td colspan="3" scope="row"><textarea name="p_professional" cols="110" style="height:80" rows="4"><c:out value="${view.professional}"/></textarea></td>
                        </tr>
                        <tr>
                            <th scope="row">센터활동실적</th>
                            <td colspan="3" scope="row"><textarea name="p_profile" cols="110" style="height:80" rows="4"><c:out value="${view.profile}"/></textarea></td>
                        </tr>
				</thead>				
			</table>
   		</div>
		<!-- list table-->		
        
 		<!-- button -->		
		<div class="btnR MR05">
			<li><a href="#none" onclick="doPageList()" class="btn03"><span>취 소</span></a></li>
		</div>

<c:if test="${empty p_userid}">		
        <div class="btnR MR05">
			<li><a href="#none" onclick="whenSave('insert')" class="btn02"><span>저 장</span></a></li>
		</div>
</c:if>

<c:if test="${not empty p_userid}">	
		<div class="btnR MR05">
			<li><a href="#none" onclick="whenSave('delete')" class="btn02"><span>삭 제</span></a></li>
		</div>	
		<div class="btnR MR05">
			<li><a href="#none" onclick="whenSave('update')" class="btn02"><span>수 정</span></a></li>
		</div>
</c:if>		

		<!-- // button -->

</form>



<c:if test="${not empty p_userid}">
		<br/><br/><br/>
		
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="%" />
					<col width="%" />
					<col width="%" />
					<col width="%" />
					<col width="%" />
                    <col width="%" />
                    <col width="%" />
                    <col width="%" />
                    <col width="%" />
                    <col width="%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row">분반</th>
						<th scope="row">과정명</th>
						<th scope="row">연수기간</th>
						<th scope="row">입과인원</th>
						<th scope="row">수료인원</th>
						<th scope="row">학습시간</th>
						<th scope="row">강사료</th>
						<th scope="row">만족도</th>
						<th scope="row">교육상태</th>
					</tr>
				</thead>
				<tbody>
<c:forEach items="${subjHistList}" var="result" varStatus="status" >				
					<tr>
						<td class="num"><c:out value="${(pageTotCnt+1)-result.num}"/></td>
						<td class="name"><c:out value="${result.class}"></c:out></td>
						<td>
							<c:out value="${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')}"/>~<c:out value="${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}"/>
						</td>
						<td><c:out value="${result.stucnt}"></c:out></td>
						<td><c:out value="${result.grayncnt}"></c:out></td>
						<td><c:out value="${result.edutimes}"></c:out></td>
						<td><c:out value="${result.jigub1}"></c:out></td>
                        <td><c:out value="${result.satisfaction}"></c:out></td>
                        <td>
	                        <c:if test="${result.isclosed eq 'Y'}">교육종료</c:if>
	                        <c:if test="${result.isclosed ne 'Y'}">교육중</c:if>
                        </td>
					</tr>
</c:forEach>					
				</tbody>
			</table>
		</div>
		<!-- list table-->
</c:if>






		<br/><br/>
		
<input type="hidden" id="sendMsg" value="" />		
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList() {
	thisForm.action = "/adm/tut/tutorList.do";
	thisForm.target = "_self";
	thisForm.submit();
}


//권한부여
function auth() {
	<c:if test="${sessionScope.gadmin eq 'A1' || sessionScope.gadmin eq 'A2'}">
	var ff = thisForm;
	if (ff.p_manager[0].checked) { //권한부여
		term.style.display = '';
		subj.style.display = '';
		
		//사외강사
		//if(ff.p_isgubun[0].checked) {
			//loginid.style.display ='none';
		//} else {
			//loginid.style.display ='';
		//}
	} else {//권한 미부여
		term.style.display ='none';
		subj.style.display ='none';
		//loginid.style.display ='none';
	}
	</c:if>
	
}


//사내강사,그룹사강사 일때 화면 전환
function do_sane() {
	//사내강사
	//성명 검색 버튼 Disable
	//아이디/PW Enabled
	//ReadOnly Disable
	srchnm.style.display ='';
	srchid.style.display ='none';
	//pwd.style.display ='none';
	//addr.style.display ='none';
	chk_readOnly(true);
	document.all.span_userid.innerHTML = "<strong>* 아이디</strong>";
	auth();
	value_ini();

}

//사외강사 권한별 화면 전환
function do_saoi() {
	
	if(thisForm.p_saoiChk.checked == true)
	{
		//사외강사
		 srchnm.style.display ='none';
		 srchid.style.display ='';
		 //pwd.style.display ='';
		 //addr.style.display ='';
		 chk_readOnly(false);
		 document.all.span_userid.innerHTML = "* 아이디";
		 auth();
		 value_ini();
	}
	else
	{
		do_sane();
	}

}

//변수 기본값 셋팅
function value_ini() {

	thisForm.p_userid.value = "";
	thisForm.p_loginpw.value = "";
	//thisForm.p_idchk.value = "";
	thisForm.p_name.value = "";
	thisForm.p_phone.value = "";
	thisForm.p_handphone.value = "";
	thisForm.p_comp.value = "";
	//thisForm.p_compcd.value = "";
	thisForm.p_email.value = "";
	thisForm.p_jik.value = "";
	thisForm.p_post1.value = "";
	//thisForm.p_post2.value = "";
	thisForm.p_addr.value = "";
	

}



//입력 필드별 권한부여
function chk_readOnly(gubun) {
	if (gubun) {
		//사내강사는 입력할 수 없도록 설정
		thisForm.p_userid.readOnly = true;
		thisForm.p_phone.readOnly = true;
		thisForm.p_handphone.readOnly = true;
		thisForm.p_comp.readOnly = true;
		thisForm.p_email.readOnly = true;
		thisForm.p_jik.readOnly = true;
		//thisForm.p_addr.readOnly = true;
		
	} else {
		//사외강사는 입력할 수 있도록 설정
		thisForm.p_userid.readOnly = false;
		thisForm.p_phone.readOnly = false;
		thisForm.p_handphone.readOnly = false;
		thisForm.p_comp.readOnly = false;
		thisForm.p_email.readOnly = false;
		thisForm.p_jik.readOnly = false;
		//thisForm.p_addr.readOnly = false;
		
	}
}

//사내강사 임직원조회
function tutor_search() {        
    var f  = thisForm;
    f.p_searchtext.value = f.p_name.value;
	window.open("","openWinMuser","top=0, left=0, width=580, height=550, scrollbars=yes").focus();
	f.action="/com/pop/searchMemberPopup.do";
	f.target="openWinMuser";
	f.submit();

	
}

function searchSubjnmKeyEvent(e){
    if (e.keyCode =='13'){  tutor_search();  }
} 
	
// 멤버 검색 후 처리
function receiveMember(userid, name, email, compnm, telno, hometel, comptel, position_nm, lvl_nm, tmp1, tmp2, tmp3){
        var f  = thisForm;
		f.p_name.value = name;
		f.p_userid.value   = userid;
		f.p_email.value   = email;
		f.p_handphone.value   = comptel;
		f.p_phone.value   = telno;
		f.p_comp.value   = compnm;
		f.p_jik.value   = lvl_nm;  
	}


function searchZipcode(){
	var frm = thisForm;
	window.open('', 'zipcode', 'width=500,height=600');
	frm.action = "/com/pop/searchZipcodePopup.do";
	frm.target = "zipcode";
	frm.submit();
}


function receiveZipcode(arr){
	var frm = thisForm;
	frm.p_post1.value = arr[0];
	frm.p_post2.value = arr[1];
	frm.p_addr.value = arr[2];
}

//과정조회화면 팝업
function open_window(name, url, left, top, width, height, toolbar, menubar, statusbar, scrollbar, resizable)
{
    toolbar_str = toolbar ? 'yes' : 'no';
    menubar_str = menubar ? 'yes' : 'no';
    statusbar_str = statusbar ? 'yes' : 'no';
    scrollbar_str = scrollbar ? 'yes' : 'no';
    resizable_str = resizable ? 'yes' : 'no';
    window.open(url, name, 'left='+left+',top='+top+',width='+width+',height='+height+',toolbar='+toolbar_str+',menubar='+menubar_str+',status='+statusbar_str+',scrollbars='+scrollbar_str+',resizable='+resizable_str).focus();
}


//과정찾기
function searchSubj() {
	window.open('/com/pop/searchSubjPopup.do', 'searchSubjPopupWindow', 'width=800,height=600');
}

//과정 매칭
function receiveSubj(subj,subjnm){
    var j = thisForm._Array_p_subj.length;
    var f_exist = "";
    for(var i=0;i<j;i++){
        if(thisForm._Array_p_subj.options[i].value==subj){
        	f_exist = "Y";
        	//alert(subjnm+"은 이미 선택되었습니다");
        	document.getElementById('sendMsg').value = subjnm+"은 이미 선택되었습니다";
        }        
    }
    if (f_exist != "Y"){
        thisForm._Array_p_subj.options[j] = new Option(subjnm,subj);
        thisForm._Array_p_subj.options[j].selected = true;
    }
}

//과정 매칭정보 삭제
function delSubj(){
    for(var i = 0 ;i<thisForm._Array_p_subj.length;i++){
        if(thisForm._Array_p_subj.options[i].selected==true)
        	thisForm._Array_p_subj.options[i] = null;
        }
}

//과정 매칭정보 삭제(popup에서 체크 해제 시 삭제)
function delSubjPop(subj, subjnm){
    for(var i = 0 ;i<thisForm._Array_p_subj.length;i++){
        if(thisForm._Array_p_subj.options[i].value==subj)
        	thisForm._Array_p_subj.options[i] = null;
        }
}


//회원아이디 조회
function mem_check(){
	var tf = thisForm;
		
	if(tf.p_userid.value.length == 0){
		alert("아이디를 입력하셔야 합니다.");
		tf.p_userid.focus();
		return;
	}

	if(!useridCheck(tf.p_userid.value)){
		alert("아이디를  영문, 숫자로 입력하셔야 합니다.");
		tf.p_userid.value="";
		tf.p_userid.focus();
		return;
	}
	else{

		window.open('', 'existIdPop', 'width=300,height=200');
		tf.action = "/adm/cfg/mem/existIdPopup.do";
		tf.target = "existIdPop";
		tf.submit();
		
	}   
} 

//강사정보 저장
function whenSave(mode) { 
	ff = thisForm;
	
	
<c:if test="${sessionScope.gadmin eq 'A1'}">
    // 권한부여시에만 과정체크
	if(ff.p_manager[0].checked) {
		if (ff.p_sdate.value=="") {
			alert("권한사용기간을 입력하세요.");
			return;
		}
		
		if (ff.p_ldate.value=="") {
			alert("권한사용기간을 입력하세요.");
			return;
		}			
	
		if ( ff._Array_p_subj.length == 0 ) {
			alert("강의과정을 지정해주십시오. ");
			return;
		}							
	}
</c:if>

	if (blankCheck(ff.p_name.value)) {
        alert("성명을 입력하세요!");
        ff.p_name.focus();
        return;
    }  
    
    if (blankCheck(ff.p_userid.value)) {
        alert("아이디를 입력하세요.");
        ff.p_userid.focus();
        return;
    }
    
<c:if test="${empty p_userid}">
  	if(ff.p_saoiChk.checked) {	// 비회원 등록이라면.        	
		//사외 강사는 아이디 체크,비밀번호를 체크한다.
		if (ff.p_userid.value=="") {
			alert("Login 아이디를 입력하세요.")
			ff.p_userid.focus();
			return;
		}

		if(ff.p_userid_temp.value == ""){
			alert("아이디 중복체크를 해주세요");
			ff.p_userid.focus();
			return;
		}
			
		if (ff.p_loginpw.value=="") {
			alert("비밀번호를 입력하세요.")
			ff.p_loginpw.focus();
			return;
		}
			
		if (ff.p_loginpw2.value=="") {
			alert("비밀번호를 입력하세요.")
			ff.p_loginpw2.focus();
			return;
		}

		if (ff.p_loginpw.value!=ff.p_loginpw2.value) {
			alert("비밀번호를 확인하세요.")
			ff.p_loginpw.focus();
			return;
		}
			
	
	}

	//사외강사는 경력,학력,소속입력 필수
	//사외강사는 전화번호,이동전화중 하나는 입력 필수
	if(ff.p_saoiChk.checked) {
	
		if (ff.p_phone.value=='' && ff.p_handphone.value=='') {
			alert('전화번호 또는 휴대폰번호를 입력하세요.');
			return;
		}
		
		if (ff.p_academic.value=='') {
			alert('학력을 입력하세요.');
			ff.p_academic.focus();
			return;
		}
		
		if (ff.p_career.value=='') {
			alert('경력을 입력하세요.');
			ff.p_career.focus();
			return;
		}
		
		if (ff.p_post1.value=='') {
			alert('우편번호를 입력하세요.');
			return;
		}
	}
	
    if( ff.p_saoiChk.checked ) {          
        ff.p_saoi.value = "2";
    } else {
    	ff.p_saoi.value = "1";
   	}

	ff.p_tutorgubun.value = "T";
 </c:if>


	for(i=0;i<ff._Array_p_subj.length;i++) {
		ff._Array_p_subj[i].selected = true;
	}

    var st_date = make_date(ff.p_sdate.value);
    var ed_date = make_date(ff.p_ldate.value); 
	ff.p_fmon.value = st_date;
    ff.p_tmon.value = ed_date;
	
	var msg = "저장하시겠습니까?";
	if(mode == "delete") msg = "삭제하시겠습니까?";

	if(confirm(msg))
	{
		ff.action = "/adm/tut/tutorAction.do";
		ff.p_process.value = mode;
		ff.target = "_self";
		ff.submit();
	}
	
}        


<c:if test="${empty p_userid}">
if(window.addEventListener) {
    window.addEventListener("load", do_sane, false);
} else if(window.attachEvent) {
    window.attachEvent("onload", do_sane);
}

</c:if>
//-->
</script>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
    function sample6_execDaumPostcode(p_zipgubun) {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullAddr = ''; // 최종 주소 변수
                var extraAddr = ''; // 조합형 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    fullAddr = data.roadAddress;

                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    fullAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                if(data.userSelectedType === 'R'){
                    //법정동명이 있을 경우 추가한다.
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있을 경우 추가한다.
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                // 커서를 상세주소 필드로 이동한다.
                thisForm.p_post1.value = data.zonecode; //5자리 새우편번호 사용
                thisForm.p_addr.value = fullAddr;

                thisForm.p_addr.focus();
            }
        }).open();
    }
</script>




<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
