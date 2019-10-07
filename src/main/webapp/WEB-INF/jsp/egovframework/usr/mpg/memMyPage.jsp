<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include
	file="/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp"%>
<!--logout check-->
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp"%>

<script type="text/javascript">
//<![CDATA[
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
//]]>
</script>

<style>
#loading_login {
	display: none;
	position: absolute;
	left: 50%;
	margin-left: -500px;
	z-index: 1000;
	width: 100%;
	height: 100%;
}
</style>

<center>
	<div id="loading_login">
		<img alt="로딩중..." src="/images/loading_login.gif" />
	</div>
</center>

<!-- 부트스트랩  CSS -->
<%-- <link rel="stylesheet" href="<c:out value="${gsDomainContext}"/>/kcase/lib/css/bootstrap.min.css"/> --%>
<!-- KSign Style Sheet -->
<link rel="stylesheet" href="<c:out value="${gsDomainContext}"/>/kcase/lib/css/kcase.css" />
<!-- Sample CSS -->
<link rel="stylesheet" href="<c:out value="${gsDomainContext}"/>/kcase/lib/css/sample.css" />
<script	src="<c:out value="${gsDomainContext}"/>/kcase/lib/js/jquery-1.11.3.js"></script>
<script 	src="<c:out value="${gsDomainContext}"/>/kcase/lib/js/jquery-ui.js"></script>
<script	src="<c:out value="${gsDomainContext}"/>/kcase/lib/js/kcase_os_check.js"></script>
<script src="<c:out value="${gsDomainContext}"/>/kcase/EPKICommon.js"></script>


<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
	<input type="hidden" name="p_contenttype" /> 
	<input type="hidden"name="p_subj" /> <input type="hidden" name="p_year" /> 
	<input	type="hidden" name="p_subjseq" /> 
	<input type="hidden"name="p_studytype" /> 
	<input type="hidden" name="p_process" /> 
	<input	type="hidden" name="p_next_process" /> 
	<input type="hidden" name="p_height" /> 
	<input type="hidden" name="p_width" /> 
	<input type="hidden" name="p_lcmstype" />
</form>

<form name="hiddenForm"
	action="<c:url value='/uat/uia/EgovUserRegistDn.do'/>" method="post">
	<input type="hidden" name="requestData" /> <input type="hidden"
		name="closeYn" value="N" /> <input type="hidden" name="userId"
		value="<c:out value="${userid}"  escapeXml="true" />" /> <input type="hidden"
		name="tIdNo" value="" />
</form>

<form name="<c:out value="${gsMainForm}"/>"
	id="<c:out value="${gsMainForm}"/>" method="post"
	onsubmit="return false;" action="">
	<fieldset>
		<legend>비밀번호변경</legend>
		<%@ include
			file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
		<input type="hidden" name="p_process" value="" /> 
		<input	type="hidden" name="p_chkExistID" /> 
		<input type="hidden" name="p_resno" /> 
		<input type="hidden" name="p_hrdc" value="" /> 
		<input	type="hidden" name="p_handphone_no" value="" /> 
		<input type="hidden" name="p_dept_cd" value="<c:out value="${view.deptCd}"  escapeXml="true" />" /> 
		<input type="hidden" name="p_agency_cd" value="<c:out value="${view.agencyCd }"  escapeXml="true" />" /> 
		<input type="hidden" name="p_virtualNo" value="<c:out value="${p_virtualNo}"  escapeXml="true" />" /> 
		<input type="hidden" name="p_authInfo" value="<c:out value="${p_authInfo}"  escapeXml="true" />" /> 
		<input type="hidden" name="p_zipgubun" id="p_zipgubun" value="1" /> 
		<input type="hidden" name="p_schoolgubun" id="p_schoolgubun" value="1" /> 
		<input type="hidden" name="chkvalue" id="chkvalue" value="<c:out value="${view.chkvalue}"  escapeXml="true" />" />

		<input type="hidden" name="o_nicePersonalNum"	value="<c:out value="${view.nicePersonalNum}"  escapeXml="true" />" />


		<c:if test="${empty view.nicePersonalNum}">
			<input type="hidden" name="p_overlap" id="p_overlap" value="N" />
		</c:if>
		<c:if test="${not empty view.nicePersonalNum}">
			<input type="hidden" name="p_overlap" id="p_overlap" value="Y" />
		</c:if>
		<input type="hidden" name="personalState" id="personalState" value="Y" />

		<p class="mb5">
			<span class="num1">*</span> 표시는 필수 입력사항입니다
		</p>

		<!-- list table-->
		<div class="studyList">
			<table summary="회원기본정보 테이블 입니다. 2열로 구성되어 1열은 항목을 2열은 내용으로 구성되었습니다."
				cellspacing="0" width="100%">
				<caption>회원구분</caption>
				<colgroup>
					<col width="25%" />
					<col width="75%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="row" class="last left">회원구분 <span class="num1">*</span></th>
						<td class="last left"><input type="radio" name="p_emp_gubun"
							id="p_emp_gubun_1" value="T" onclick="changeGubun(1);"
							class="input_border vrM"
							<c:if test="${empty view.empGubun || view.empGubun eq 'T'}">checked="checked"</c:if> />
							<label for="p_emp_gubun_1">교원</label>&nbsp; <input type="radio"
							name="p_emp_gubun" id="p_emp_gubun_3" value="E"
							onclick="changeGubun(3);" class="input_border vrM"
							<c:if test="${view.empGubun eq 'E'}">checked="checked"</c:if> />
							<label for="p_emp_gubun_3">보조인력</label> <input type="radio"
							name="p_emp_gubun" id="p_emp_gubun_2" value="R"
							onclick="changeGubun(2);" class="input_border vrM"
							<c:if test="${view.empGubun eq 'R'}">checked="checked"</c:if> />
							<label for="p_emp_gubun_2">교육전문직(교육연구사, 장학사 등)</label> <input
							type="radio" name="p_emp_gubun" id="p_emp_gubun_4" value="P"
							onclick="changeGubun(4);" class="input_border vrM"
							<c:if test="${view.empGubun eq 'P'}">checked="checked"</c:if> />
							<label for="p_emp_gubun_4">일반회원(학부모 등)</label> <input
							type="radio" name="p_emp_gubun" id="p_emp_gubun_5" value="O"
							onclick="changeGubun(5);" class="input_border vrM"
							<c:if test="${view.empGubun eq 'O'}">checked="checked"</c:if> />
							<label for="p_emp_gubun_5">공무원</label>
							<input
							type="radio" name="p_emp_gubun" id="p_emp_gubun_6" value="A"
							onclick="changeGubun(6);" class="input_border vrM"
							<c:if test="${view.empGubun eq 'A'}">checked="checked"</c:if> />
							<label for="p_emp_gubun_6">방과후교사 등(돌봄교사, 문예체 전문강사)</label></td>
					</tr>
				</tbody>
			</table>


			<table summary="회원기본정보 테이블 입니다. 2열로 구성되어 1열은 항목을 2열은 내용으로 구성되었습니다."
				cellspacing="0" width="100%">
				<caption>회원기본정보입력</caption>
				<colgroup>
					<col width="25%" />
					<col width="75%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="row" class="last left">아이디 <span class="num1">*</span></th>
						<td class="last left"><c:out value="${view.userid}"  escapeXml="true" /> <input type="hidden"
							name="p_userid" size="20" id="p_userid" value="<c:out value="${view.userid}"  escapeXml="true" />" />
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left">이름 <span class="num1">*</span></th>
						<td class="last left"><c:out value="${view.name}"  escapeXml="true" /> <input
							type="hidden" name="p_name" size="20" id="p_name"
							value="<c:out value="${view.name}"  escapeXml="true" />" /></td>
					</tr>
					<!--
                      <c:if test="${view.empGubun eq 'T'}">
                      <tr>
                      	<th scope="row" class="last left">나이스 개인번호 <span class="num1">*</span></th>
                        <td class="last left"><input type="text" name="p_nicePersonalNum" id="p_nicePersonalNum" maxlength="11" title="나이스 개인번호" <c:if test="${not empty view.nicePersonalNum }">readOnly</c:if> value="<c:out value="${view.nicePersonalNum}"/>"/></td>
                      </tr>
                      <c:if test="${empty view.nicePersonalNum }">
                      <tr>
                      	<th scope="row" class="last left">나이스 개인번호 확인<span class="num1">*</span></th>
                        <td class="last left">
                        	<input type="text" name="p_nicePersonalNum2" id="p_nicePersonalNum2" maxlength="11" title="나이스 개인번호 확인" <c:if test="${not empty view.nicePersonalNum }">readOnly</c:if> value="<c:out value="${view.nicePersonalNum}"/>"/>
                        	<a href="#" class="pop_btn01" onclick="existPersonalNum()" title="나이스개인번호중복확인"><span>중복확인</span></a>
                        </td>
                      </tr>
                      </c:if>
                      </c:if>
                      -->
					<tr>
						<th scope="row" class="last left">인증서정보</th>
						<td class="last left">
							<%-- <label for="p_resno1" class="blind">주민번호</label>
                          <input type="password" id="p_resno1" name="p_resno1" value="${jumin1}"  size="14" maxlength="6" style="ime-mode:disabled;" title="주민번호앞 6자리"/>
			            -
			            <label for="p_resno2" class="blind">주민번호 뒤7자리</label><input type="password" name="p_resno2" value="${jumin2}" id="p_resno2"  size="14"  maxlength="7"  style="ime-mode:disabled;" title="주민번호 뒤7자리"/>
 --%> <!--   <input type="password" name="inputIdNo" id="inputIdNo" maxlength="13" title="주민번호"/> -->
							<!-- (*주민번호 입력후 인증서 검색버튼을 눌러주세요.<br/> --> &nbsp;<a href="#none"
							name="epkiBtn" id="epkiBtn" title="새창"><img
								src="/images/user/btn_epki_search.png" alt="인증서 검색"></a><label
							for="epkiDn" class="blind">인증서</label> <input type="text"
							name="epkiDn" id="epkiDn" maxlength="200" size="40"
							value="<c:out value="${view.epkiDn}"  escapeXml="true" />" readonly="readonly" 	title="인증서" /> <br />
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left"><label for="p_email1">전자우편</label><span
							class="num1">*</span></th>
						<td class="last left"><c:set var="p_email1">
							</c:set> <c:set var="p_email2">
							</c:set> <c:if test="${fn:indexOf(view.email, '@') != -1}">
								<c:set var="p_email1">
                            ${fn:substring(view.email, 0, fn:indexOf(view.email, '@'))}
                            </c:set>
								<c:set var="p_email2">
                            ${fn:substring(view.email, fn:indexOf(view.email, '@')+1, fn:length(view.email))}
                            </c:set>
							</c:if> <input type="text" name="p_email1" size="20" id="p_email1"
							style="ime-mode: disabled" value="<c:out value="${p_email1}"  escapeXml="true" />" /> @ <label
							for="p_email2" class="blind">도메인</label> <input type="text"
							name="p_email2" size="17" id="p_email2"
							style="ime-mode: disabled" value="<c:out value="${p_email2}"  escapeXml="true" />" title="도메인" /> <label
							for="p_email3" class="blind">도메인선택</label> <ui:code id="p_email3"
								selectItem="${p_email2}" gubun="" codetype="0067" upper=""
								title="도메인선택" className="" type="select" selectTitle="직접입력"
								event="changeMail" /> <br /> <input type="checkbox"
							name="p_ismailling" id="p_ismailling" value="Y"
							class="input_border vrM"
							<c:if test="${empty view.ismailling || view.ismailling eq 'Y'}">checked="checked"</c:if> />
							<label for="p_ismailling">전자우편 수신설정에 동의하겠습니다.</label></td>
					</tr>
					<tr>
						<th scope="row" class="last left">자택주소 <span
							id="p_address1_view" class="num1">*</span></th>
						<td class="last left"><a href="#"
							onclick="sample6_execDaumPostcode(1);return false;" title="새창">
								<img src="/images/user/btn_post.gif" alt="우편번호찾기" />
						</a> <label for="p_post1" class="blind">우편번호</label> <input
							type="text" name="p_post1" size="10" id="p_post1"
							readonly="readonly" value="<c:out value="${view.zipCd1}"  escapeXml="true" />" title="우편번호" /> <%-- -
	                          <input type="text" name="p_post2" size="10" id="p_post2" readonly="readonly" value="${view.zipCd2}" title="우편번호 뒷자리"/><br /> --%>
							<label for="p_address1" class="blind">주소</label><input
							type="text" name="p_address1" size="80" id="p_address1"
							class="mt7" value="<c:out value="${view.address}"  escapeXml="true" />" title="주소" /> <!-- <br />
	                          *택배 발송용 주소이므로 자세하고 정확하게 입력해 주세요.<br />
	                          *이수증 발급시 주소가 불분명하면 반송되는 경우가 많습니다.<br />
	                          번지수까지 자세하게 기입하여 주십시오.(교원만 해당) --></td>
					</tr>
					<tr>
						<th scope="row" class="last left"><label for="p_hometel">전화번호</label></th>
						<%-- <td class="last left"><input type="text" name="p_hometel" size="30" id="p_hometel" value="${view.hometel}"/>번호는 '-'와 숫자만 쓸수있습니다. </td> --%>
						<td class="last left"><input type="hidden" name="p_hometel"
							id="p_hometel" value=""> <input type="text"
							name="p_hometel_1" size="4" id="p_hometel_1"
							value="<c:out value="${view.hometel1}"  escapeXml="true" />" title="전화번호 첫번째 입력" /> - <input
							type="text" name="p_hometel_2" size="4" id="p_hometel_2"
							value="<c:out value="${view.hometel2}"  escapeXml="true" />" title="전화번호 두번째 입력" /> - <input
							type="text" name="p_hometel_3" size="4" id="p_hometel_3"
							value="<c:out value="${view.hometel3}"  escapeXml="true" />" title="전화번호 세번째 입력" /></td>
					</tr>
					<tr>
						<th scope="row" class="last left"><label for="p_handphone">휴대폰번호</label>
							및 수신여부 <span class="num1">*</span></th>
						<%-- <td class="last left"><input type="text" name="p_handphone" size="30" id="p_handphone"  value="${view.handphone}"/> --%>
						<td class="last left"><input type="hidden" name="p_handphone"
							id="p_handphone" value="" /> <input type="text" size="4"
							name="p_handphone_1" id="p_handphone_1"
							value="<c:out value="${view.handphone1}"  escapeXml="true" />" title="휴대폰번호 첫번째 입력" /> - <input
							type="text" size="4" name="p_handphone_2" id="p_handphone_2"
							value="<c:out value="${view.handphone2}"  escapeXml="true" />" title="휴대폰번호 두번째 입력" /> - <input
							type="text" size="4" name="p_handphone_3" id="p_handphone_3"
							value="<c:out value="${view.handphone3}"  escapeXml="true" />" title="휴대폰번호 세번째 입력" /> <br /> <input
							type="checkbox" name="p_issms" id="p_issms" value="Y"
							class="input_border vrM"
							<c:if test="${empty view.issms || view.issms eq 'Y'}">checked="checked"</c:if> />
							<label for="p_issms">SMS(문자메세지) 수신설정에 동의하겠습니다. </label></td>
					</tr>

				</tbody>
			</table>
		</div>
		<!--//list table-->
		<!--교원정보-->
		<div id="memberGubun1">
			<div class="sub_txt_1depth m30">
				<h4>근무지 정보</h4>
			</div>
			<div class="studyList">
				<table summary="근무지 정보 테이블 입니다. 2열로 구성되어 1열은 항목을 2열은 내용으로 구성되었습니다."
					cellspacing="0" width="100%">
					<caption>근무지 정보입력</caption>
					<colgroup>
						<col width="22%" />
						<col width="28%" />
						<col width="22%" />
						<col width="28%" />
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="last left"><label for="p_job_cd">교육대상자
									구분</label> <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<!--
	                      <select name="p_job_cd" id="p_job_cd" title="교육대상자 구분" onchange="selectBoxOnChange()" disabled>
	                       --> <select name="p_job_cd" id="p_job_cd"
								title="교육대상자 구분" onChange="showWorkPlaceInfo();">
									<option value="">===선택===</option>
									<c:forEach items="${list}" var="result" varStatus="i">
										<c:if test="${i.index lt 9 }">
										<option value="${result.code }"
											<c:if test="${view.jobCd eq result.code }">selected</c:if>>${result.codenm }</option>
										</c:if>
									</c:forEach>
							</select>
							<select name="p_job_cd3" id="p_job_cd3" style="display:none"
								title="교육대상자 구분">
									<option value="">===선택===</option>
									<c:forEach items="${list}" var="result" varStatus="idx">
										<c:if test="${idx.index gt 9 }">
										<option value="${result.code }"
											<c:if test="${view.jobCd eq result.code }">selected</c:if>>${result.codenm }</option>
										</c:if>
									</c:forEach>
							</select>
							</td>
						</tr>
						<tr id="trWorkPlaceInfo" style="display:none;">
							<th scope="row" class="last left">근무지정보</th>
							<td class="last left" colspan="3">
								<input type="checkbox" name="p_wp_gubun" id="p_wp_gubun1" value="S" class="input_border vrM"><label for="p_wp_gubun1">특수학교</label>&nbsp;
								<input type="checkbox" name="p_wp_gubun" id="p_wp_gubun2" value="C" class="input_border vrM"><label for="p_wp_gubun2">특수학급</label>&nbsp;
							</td>
						</tr>
						<c:if
							test="${(view.niceNumAllowYn eq 'N') || (view.niceNumAllowYn eq null)}">

							<tr id="trNicePersonalNum1">
								<th scope="row" class="last left">나이스 개인번호<span
									class="num1">*</span>
								</th>
								<td class="last left" colspan="3">
									<!--
                        	<input type="text" name="p_nicePersonalNum" id="p_nicePersonalNum" maxlength="11" title="나이스 개인번호" <c:if test="${not empty view.nicePersonalNum }">readOnly</c:if> value="<c:out value="${view.nicePersonalNum}"/>"/>
                        --> <input type="text" name="p_nicePersonalNum"
									id="p_nicePersonalNum" maxlength="10" title="나이스 개인번호"
									value="<c:out value="${view.nicePersonalNum}"  escapeXml="true" />"
									onkeyup="fnUpper()" /> <a href="#" id="niceNumConfirm"
									class="pop_btn01" onclick="fnPopup();return false;"
									title="나이스개인번호중복확인 새창"><span>나이스개인번호 구성안내</span></a> <a
									href="#" id="niceNumCheck" class="pop_btn01"
									onclick="fnPopup1();return false;" title="나이스번호 확인방법 새창"><span>나이스번호
											확인방법</span></a>

								</td>
							</tr>
							<tr id="trNicePersonalNum2">
								<th scope="row" class="last left">나이스 개인번호 확인<span
									class="num1">*</span></th>
								<td class="last left" colspan="3"><input type="text"
									name="p_nicePersonalNum2" id="p_nicePersonalNum2"
									maxlength="10" title="나이스 개인번호 확인"
									value="<c:out value="${view.nicePersonalNum}"  escapeXml="true" />"
									onkeyup="fnUpper()" /> <a href="#" id="niceNumCheck1"
									class="pop_btn01"
									onclick="validNicePersonalNum();return false;"
									title="나이스개인번호중복확인"><span>나이스개인번호중복확인</span></a> <!-- <a href="#" id="niceNumCheck2" class="pop_btn01" onclick="existPersonalNum1()" title="검증"><span>검증</span></a> -->

								</td>
							</tr>
						</c:if>
						<c:if test="${view.niceNumAllowYn eq 'Y'}">
							<input type="hidden" name="p_nicePersonalNum"
								id="p_nicePersonalNum" maxlength="10" title="나이스 개인번호"
								value="<c:out value="${view.nicePersonalNum}"  escapeXml="true" />" />
							<input type="hidden" name="p_nicePersonalNum2"
								id="p_nicePersonalNum2" maxlength="10" title="나이스 개인번호"
								value="<c:out value="${view.nicePersonalNum}"  escapeXml="true" />" />
						</c:if>
						<tr>
							<th scope="row" class="last left"><label for="p_user_path" id="p_path">근무기관명</label>
								<span class="num1">*</span></th>
							<td class="last left"><input type="text" name="p_user_path"
								size="20" id="p_user_path" value="<c:out value="${view.userPath}"  escapeXml="true" />" readonly />
								<a href="#" class="pop_btn01"
								onclick="searchSchool(1);return false;" title="근무기관명 검색"
								style="cursor: hand"><span>검색</span></a></td>
							<th scope="row" class="last left"><label
								for="p_handphone_no1" id="p_no">근무지 연락처</label><span class="num1">*</span></th>
							<td class="last left"><input type="hidden"
								name="p_handphone_no1" size="30" id="p_handphone_no1" value="" />
								<input type="text" name="p_handphone_no1_1" size="4"
								id="p_handphone_no1_1" value="<c:out value="${view.handphoneNo1}"  escapeXml="true" />"
								title="근무지 연락처 첫번째 입력" /> - <input type="text"
								name="p_handphone_no1_2" size="4" id="p_handphone_no1_2"
								value="<c:out value="${view.handphoneNo2}"  escapeXml="true" />" title="근무지 연락처 두번째 입력" /> - <input
								type="text" name="p_handphone_no1_3" size="4"
								id="p_handphone_no1_3" value="<c:out value="${view.handphoneNo3}"  escapeXml="true" />"
								title="근무지 연락처 세번째 입력" /> <!-- * 번호 02-123-1231 --></td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label for="p_dept_cd">시도교육청</label>
								<span class="num1">*</span></th>
							<td class="last left">
								<!--<ui:code id="p_dept_cd" selectItem="${view.deptCd}" gubun="eduOrg" codetype="M"  upper="" title="시도교육청" className="" type="select" selectTitle="::선택::" event="whenSelection()" />-->
								<input type="text" name="p_dept_cd_nm" size="20"
								id="p_dept_cd_nm" maxlength="20" value="<c:out value="${view.deptnm }"  escapeXml="true" />"
								title="시도교육청" readonly />
							</td>
							<th scope="row" class="last left"><label for="p_agency_cd">교육지원청</label>
								<span class="num1">*</span></th>
							<td class="last left">
								<!--<ui:code id="p_agency_cd" selectItem="${view.agencyCd}" gubun="eduOrg" codetype=""  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="" /> -->
								<input type="text" name="p_agency_cd_nm" size="20"
								id="p_agency_cd_nm" maxlength="20" value="<c:out value="${view.agencynm}"  escapeXml="true" />"
								title="교육지원청" readonly />
							</td>
						</tr>
						<tr id="trCareerSubject">
							<th scope="row" class="last left"><label for="p_career">경력</label>
								<span class="num1">*</span></th>
							<td class="last left"><ui:code id="p_career"
									selectItem="${view.career}" gubun="" codetype="0116" upper=""
									title="경력" className="" type="select" selectTitle="선택" event="" />
							</td>
							<th scope="row" class="last left"><label for="p_subject">담당교과</label>
								<span class="num1">*</span></th>
							<td class="last left" colspan="3"><input type="text"
								name="p_subject" id="p_subject" maxlength="30" title="담당교과"
								value="<c:out value="${view.subject }"  escapeXml="true" />" /></td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label id="p_addr">근무지 주소 </label> <span class="num1">*</span></th>
							<td class="last left" colspan="3"><a href="#"
								onclick="sample6_execDaumPostcode(2);return false;" title="새창">
									<img src="/images/user/btn_post.gif" alt="우편번호찾기" />
							</a> <label for="p_mpost1" class="blind">우편번호</label> <input
								type="text" name="p_mpost1" size="10" id="p_mpost1"
								readonly="readonly" value="<c:out value="${view.zipCd3}"  escapeXml="true" />" title="우편번호" /> <%-- -
                        <input type="text" name="p_mpost2" size="10" id="p_mpost2" readonly="readonly" value="${view.zipCd4}" title="우편번호 뒷자리"/> --%>
								<br /> <label for="p_maddress1" class="blind">주소</label> <input
								type="text" name="p_maddress1" size="45" id="p_maddress1"
								class="mt7" value="<c:out value="${view.address1}"  escapeXml="true" />" title="주소" /> <!-- <br />

                        번지수까지 자세하게 기입하여 주십시오.(교원만 해당) --></td>
						</tr>
						<%-- <tr>
                      <th scope="row" class="last left">교재 수령지 <span class="num1">*</span></th>
                      <td class="last left" colspan="3"><input type="radio" name="p_hrdc1" id="p_hrdc1_1" value="C" class="input_border vrM"
                                <c:if test="${empty view.hrdc || view.hrdc eq 'C'}">checked="checked"</c:if> />
                        <label for="p_hrdc1_1">학교</label>&nbsp;
                        <input type="radio" name="p_hrdc1" id="p_hrdc1_2" value="H" class="input_border vrM"
                                <c:if test="${view.hrdc eq 'H'}">checked="checked"</c:if> />
                        <label for="p_hrdc1_2">자택</label> </td>
                    </tr> --%>
					</tbody>
				</table>
			</div>
		</div>
		<!--//교원정보-->
		<!-- 보조인력 -->
		<div id="memberGubun3" style="display: none">
			<div class="sub_txt_1depth m30">
				<h4>근무지 정보</h4>
			</div>
			<div class="studyList">
				<table summary="근무지 정보 테이블 입니다. 2열로 구성되어 1열은 항목을 2열은 내용으로 구성되었습니다."
					cellspacing="0" width="100%">
					<caption>근무지 정보입력</caption>
					<colgroup>
						<col width="20%" />
						<col width="30%" />
						<col width="20%" />
						<col width="30%" />
					</colgroup>
					<tbody>
						<!--<tr>
                      <th scope="row" class="last left"><label for="p_job_cd2">교사자격등급</label> <span class="num1">*</span></th>
                      <td class="last left" colspan="3"><ui:code id="p_job_cd2" selectItem="${view.jobCd}" gubun="" codetype="0115"  upper="" title="" className="" type="select" selectTitle="선택" event="" /></td>
                    </tr>
                    -->
						<tr>
							<th scope="row" class="last left"><label for="p_user_path2">학교명</label>
								<span class="num1">*</span></th>
							<td class="last left"><input type="text" name="p_user_path2"
								size="20" id="p_user_path2" value="<c:out value="${view.userPath}"  escapeXml="true" />" readonly />
								<input type="hidden" name="p_job_cd2" size="20" id="p_job_cd2"
								value="0" /> <a href="#" class="pop_btn01"
								onclick="searchSchool(2);return false;" title="학교검색"
								style="cursor: hand"><span>검색</span></a></td>
							<th scope="row" class="last left"><label
								for="p_handphone_no3">학교 연락처</label><span class="num1">*</span></th>
							<td class="last left"><input type="hidden"
								name="p_handphone_no3" size="30" id="p_handphone_no3" value="" />
								<input type="text" name="p_handphone_no3_1" size="4"
								id="p_handphone_no3_1" value="<c:out value="${view.handphoneNo1}"  escapeXml="true" />"
								title="학교 연락처 첫번째 입력" /> - <input type="text"
								name="p_handphone_no3_2" size="4" id="p_handphone_no3_2"
								value="<c:out value="${view.handphoneNo2}"  escapeXml="true" />" title="학교 연락처 두번째 입력" /> - <input
								type="text" name="p_handphone_no3_3" size="4"
								id="p_handphone_no3_3" value="<c:out value="${view.handphoneNo3}"  escapeXml="true" />"
								title="학교 연락처 세번째 입력" /> <!-- * 번호 02-123-1231 --></td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label for="p_dept_cd2">시도교육청</label>
								<span class="num1">*</span></th>
							<td class="last left">
								<!--<ui:code id="p_dept_cd" selectItem="${view.deptCd}" gubun="eduOrg" codetype="M"  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="whenSelection()" />-->
								<input type="text" name="p_dept_cd_nm2" size="20"
								id="p_dept_cd_nm2" maxlength="20" value="<c:out value="${view.deptnm }"  escapeXml="true" />"
								title="시도교육청" readonly />
							</td>
							<th scope="row" class="last left"><label for="p_agency_cd">교육지원청</label>
								<span class="num1">*</span></th>
							<td class="last left">
								<!--<ui:code id="p_agency_cd" selectItem="${view.agencyCd}" gubun="eduOrg" codetype=""  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="" /> -->
								<input type="text" name="p_agency_cd_nm2" size="20"
								id="p_agency_cd_nm2" maxlength="20" value="<c:out value="${view.agencynm}"  escapeXml="true" />"
								title="교육지원청" readonly />
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left">학교상세주소 <span class="num1">*</span></th>
							<td class="last left" colspan="3"><a href="#"
								onclick="sample6_execDaumPostcode(4);return false;" title="새창">
									<img src="/images/user/btn_post.gif" alt="우편번호찾기" />
							</a> <label for="p_mpost21" class="blind">우편번호</label> <input
								type="text" name="p_mpost21" size="10" id="p_mpost21"
								readonly="readonly" value="<c:out value="${view.zipCd3}"  escapeXml="true" />" title="우편번호" /> <%-- -
                        <input type="text" name="p_mpost22" size="10" id="p_mpost22" readonly="readonly" value="${view.zipCd4}" title="우편번호 뒷자리"/> --%>
								<br /> <label for="p_maddress21" class="blind">주소</label> <input
								type="text" name="p_maddress21" size="45" id="p_maddress21"
								class="mt7" value="<c:out value="${view.address1}"  escapeXml="true" />" title="주소" /> <!-- <br />

                        번지수까지 자세하게 기입하여 주십시오.(교원만 해당) --></td>
						</tr>
						<%-- <tr>
                      <th scope="row" class="last left">교재 수령지 <span class="num1">*</span></th>
                      <td class="last left" colspan="3"><input type="radio" name="p_hrdc3" id="p_hrdc3_1" value="C" class="input_border vrM"
                                <c:if test="${empty view.hrdc || view.hrdc eq 'C'}">checked="checked"</c:if> />
                        <label for="p_hrdc1_1">학교</label>&nbsp;
                        <input type="radio" name="p_hrdc3" id="p_hrdc3_2" value="H" class="input_border vrM"
                                <c:if test="${view.hrdc eq 'H'}">checked="checked"</c:if> />
                        <label for="p_hrdc1_2">자택</label> </td>
                    </tr> --%>
					</tbody>
				</table>
			</div>
		</div>
		<!--교육전문직 / 학부모정보 / 공무원 정보 -->
		<div id="memberGubun2" style="display: none">
			<div class="sub_txt_1depth m30">
				<h4>근무지 정보</h4>
			</div>
			<div class="studyList">
				<table summary="일반회원 정보 테이블 입니다. 2열로 구성되어 1열은 항목을 2열은 내용으로 구성되었습니다."
					cellspacing="0" width="100%">
					<caption>근무지 정보</caption>
					<colgroup>
						<col width="20%" />
						<col width="30%" />
						<col width="20%" />
						<col width="30%" />
					</colgroup>
					<tbody>
						<tr id="trNicePersonalNumYn">
							<th scope="row" class="last left"><label for="personalNumYn">
									<span id="trNicePersonalNumYnSpan" style="font-weight: bold;">나이스개인번호
										여부</span>
							</label></th>
							<td class="last left" colspan="3"><input type="radio"
								name="personalNumYn" " id="personalNumY" value="Y"
								checked="checked" class="input_border vrM"
								onclick="changePersonalNumYn('Y');"><label
								for="personalNumY">Yes</label> &nbsp; <input type="radio"
								name="personalNumYn" " id="personalNumN" value="N"
								class="input_border vrM" onclick="changePersonalNumYn('N');"><label
								for="personalNumN">No</label> &nbsp;</td>
						</tr>
						<tr id="plus_info_006">
							<th scope="row" class="last left"><label for="p_position_nm">
									<span id="plus_info_001" style="font-weight: bold;">소속/직장명</span>

							</label></th>
							<td class="last left" colspan="3">
								<!--<ui:code id="p_dept_cd" selectItem="" gubun="eduOrg" codetype="M"  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="whenSelection()" />-->
								<input type="text" name="p_position_nm" size="20"
								id="p_position_nm" value="<c:out value="${view.positionNm}"  escapeXml="true" />" />
							</td>
							<!--<th scope="row" class="last left"><label for="p_work_place_nm">근무지</label> </th>
                            <td class="last left" colspan="3">
                            	<ui:code id="p_agency_cd" selectItem="" gubun="eduOrg" codetype=""  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="" />
                            	<input type="text" name="p_work_place_nm" size="20" id="p_work_place_nm"   value="${view.workPlaceNm}" />
                            </td>
                        -->
						</tr>

						<!-- <tr  id="plus_info_004" >
	                      <th scope="row" class="last left"><label for="p_user_path2">교육청선택</label> <span class="num1">*</span></th>
	                      <td class="last left" colspan="3">	                      	
		                      	<a href="#" class="pop_btn01" onclick="education_Office();return false;" title="교육청검색" style="cursor:hand"><span>교육청 검색</span></a>
	                      </td>
	                    </tr> -->
						<tr id="plus_info_004">
							<th scope="row" class="last left"><label for="p_user_path2">근무기관명</label>
								<span class="num1">*</span></th>
							<td class="last left" colspan="3"><input type="text"
								name="p_user_path4" size="20" id="p_user_path4"
								value="<c:out value="${view.userPath}"  escapeXml="true" />" readonly /> <a href="#"
								class="pop_btn01" onclick="searchSchool(4);return false;"
								title="근무기관명 검색" style="cursor: hand"><span>검색</span></a></td>
						</tr>
						<tr id="plus_info_005">
							<th scope="row" class="last left"><label for="p_dept_cd">시도교육청</label>
								<span class="num1">*</span></th>
							<td class="last left"><input type="text"
								name="p_dept_cd_nm3" size="20" id="p_dept_cd_nm3" maxlength="20"
								value="<c:out value="${view.deptnm }"  escapeXml="true" />" title="시도교육청" readonly /></td>
							<th scope="row" class="last left"><label for="p_agency_cd">교육지원청</label>
								<span class="num1">*</span></th>
							<td class="last left"><input type="text"
								name="p_agency_cd_nm3" size="20" id="p_agency_cd_nm3"
								maxlength="20" value="<c:out value="${view.agencynm}"  escapeXml="true" />" title="교육지원청" readonly />
							</td>
						</tr>


						<tr>
							<th scope="row" class="last left"><label for="p_division_nm">부서명</label>
							</th>
							<td class="last left">
								<!--<ui:code id="p_dept_cd" selectItem="" gubun="eduOrg" codetype="M"  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="whenSelection()" /> -->
								<input type="text" name="p_division_nm" size="20"
								id="p_division_nm" value="<c:out value="${view.divisionNm}"  escapeXml="true" />" />
							</td>
							<th scope="row" class="last left"><label for="p_post_nm">직급</label>
							</th>
							<td class="last left">
								<!--<ui:code id="p_agency_cd" selectItem="" gubun="eduOrg" codetype=""  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="" />-->
								<input type="text" name="p_post_nm" size="20" id="p_post_nm"
								value="<c:out value="${view.postNm}"  escapeXml="true" />" />
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label
								for="p_handphone_no2">직장 연락처</label> <span
								id="p_handphone_no2_view" class="num1">*</span></th>
							<td class="last left" colspan="3"><input type="hidden"
								name="p_handphone_no2" size="30" id="p_handphone_no2" value="" />
								<input type="text" name="p_handphone_no2_1" size="4"
								id="p_handphone_no2_1" value="<c:out value="${view.handphoneNo1}"  escapeXml="true" />"
								title="직장 연락처 첫번째 입력" /> - <input type="text"
								name="p_handphone_no2_2" size="4" id="p_handphone_no2_2"
								value="<c:out value="${view.handphoneNo2}"  escapeXml="true" />" title="직장 연락처 두번째 입력" /> - <input
								type="text" name="p_handphone_no2_3" size="4"
								id="p_handphone_no2_3" value="<c:out value="${view.handphoneNo3}"  escapeXml="true" />"
								title="직장 연락처 세번째 입력" /> <!-- *번호 02-123-1231 --></td>
						</tr>
						<tr>
							<th scope="row" class="last left">직장상세주소 <span
								id="p_saddress1_view" class="num1">*</span></th>
							<td class="last left" colspan="3"><a href="#"
								onclick="sample6_execDaumPostcode(3);return false;" title="새창"><img
									src="/images/user/btn_post.gif" alt="우편번호찾기" /></a> <label
								for="p_spost1" class="blind">우편번호</label> <input type="text"
								name="p_spost1" size="10" id="p_spost1" readonly="readonly"
								value="<c:out value="${view.zipCd3}"  escapeXml="true" />" title="우편번호" /> <%-- -
                                    <input type="text" name="p_spost2" size="10" id="p_spost2" readonly="readonly"  value="${view.zipCd4}" title="우편번호 뒷자리"/> --%>
								<br /> <label for="p_saddress1" class="blind">주소</label> <input
								type="text" name="p_saddress1" size="45" id="p_saddress1"
								class="mt7" value="<c:out value="${view.address1}"  escapeXml="true" />" title="주소" />
							<!-- <br/>
                                    번지수까지 자세하게 기입하여 주십시오.<br/>
 --></td>
						</tr>
						<%-- <tr>
                              <th scope="row" class="last left">교재 수령지 <span class="num1">*</span></th>
                                <td class="last left" colspan="3">
                                <input type="radio" name="p_hrdc2" id="p_hrdc2_1" value="C" class="input_border vrM"
                                <c:if test="${empty view.hrdc || view.hrdc eq 'C'}">checked="checked"</c:if> /><label for="p_hrdc2_1">
                                <span id="plus_info_002">학교/직장</span>
                                </label>&nbsp;
                                <input type="radio" name="p_hrdc2" id="p_hrdc2_2" value="H"  class="input_border vrM"
                                <c:if test="${view.hrdc eq 'H'}">checked="checked"</c:if> /><label for="p_hrdc2_2">
                                <span id="plus_info_003">자택</span>
                                </label>
                                </td>
                            </tr> --%>
					</tbody>
				</table>
			</div>
		</div>
		<!--//학부모정보-->

		<!-- list table-->
		<p class="m10">* 시도교육청과 학교명이 바르게 기재되지 않으면 연수인증이 되지 않을 수 있으니 반드시 기재
			바랍니다.</p>
		<!-- button -->
		<ul class="btnR">
			<li><a href="#" onclick="frmSubmit();return false;"><img
					src="/images/user/btn_save.gif" alt="저장" /></a></li>
		</ul>
		<!-- // button -->
	</fieldset>
</form>


<div id="epkidiv">
	<form id="epki_form" name="epki_form"
		action="https://iedu.nise.go.kr/usr/lgn/userRegistDnHtml5.do" method="post">
		<input type="hidden" name="p_resno_userid" id="p_resno_userid" />
		<!-- EPKI 인증: E -->
		<input type="hidden" name="closeYn" value="N" />
		<div style="display: none">
			<textarea name="reqSecLoginData"></textarea>
		</div>
	</form>
</div>


<!-- <script src="/EPKI/EPKICommon.js" type="text/javascript"></script> -->

<script type="text/javascript">
//SetupObjECT(true);
//InitObjECT();

var frm = eval('document.<c:out value="${gsMainForm}"/>');

function RequestVerifyVID_NO() {
	<c:if test="${not empty serverCert}">
	var frm = document.hiddenForm;
	var frm1 = eval('document.<c:out value="${gsMainForm}"/>');
	var strRequestData = RequestVerifyVID("${serverCert}", $('#inputIdNo').val());

	if(strRequestData != "") {
		frm.requestData.value = strRequestData;
		frm.target = "frmDn";
		frm.action = "<c:url value='/usr/lgn/userRegistDn.do'/>";
		frm.submit();
	}
	</c:if>

	<c:if test="${empty serverCert}">
	alert("시스템에 문제가있어 진행할 수 없습니다.");
	</c:if>
}




//하위기관..
function whenSelection() {
	$("#p_agency_cd").html('');
	$("#p_agency_cd").append('<option value=\"\">::선택::</option>');

	$.ajax({
		url: "<c:out value="${gsDomainContext}" />/com/aja/sch/selectEduOrgList.do",
		data: {
			searchAgencyCode : function() {
				return $("#p_dept_cd").val();
			}
		},
		dataType: 'json',
		contentType : "application/json:charset=utf-8",
		success: function(data) {
			data = data.result;
			for (var i = 0; i < data.length; i++) {
				var value = data[i].code;
				var title = data[i].codenm

				if('<c:out value="${view.agencyCd}"/>' == value) {
					$("select#p_agency_cd").append("<option value='" + value + "' selected>" + title + "</option>");
				} else {
					$("select#p_agency_cd").append("<option value='" + value + "'>" + title + "</option>");
				}
			}
		},
		error: function(xhr, status, error) {
			alert(status);
			alert(error);
		}
	});
}

function searchSchool(p_schoolgubun) {
	frm.p_schoolgubun.value = p_schoolgubun;
	window.open('', 'schoolWindowPop', 'width=750,height=650');
	frm.action = "/usr/mem/searchSchoolPop.do";
	frm.target = "schoolWindowPop";
	frm.submit();
}

//교육청 검색
function education_Office() {
	frm.p_schoolgubun.value = 3;
	window.open('', 'education_OfficePop', 'width=550,height=500');
	frm.action = "/usr/mem/searchEducationOfficePop.do";
	frm.target = "education_OfficePop";
	frm.submit();
}

function receiveSchool(arr) {
	var p_schoolgubun = frm.p_schoolgubun.value;

	if(p_schoolgubun == 1)
	{
		frm.p_user_path.value = arr[0];
		frm.p_dept_cd_nm.value = arr[1];
		frm.p_agency_cd_nm.value = arr[2];
		frm.p_handphone_no1.value = arr[3];
		frm.p_dept_cd.value = arr[4];
		frm.p_agency_cd.value = arr[5];
	}

	if(p_schoolgubun == 2)
	{
		frm.p_user_path2.value = arr[0];
		frm.p_dept_cd_nm2.value = arr[1];
		frm.p_agency_cd_nm2.value = arr[2];
		frm.p_handphone_no3.value = arr[3];
		frm.p_dept_cd.value = arr[4];
		frm.p_agency_cd.value = arr[5];
	}

	//교육청
	if(p_schoolgubun == 3){
		frm.p_dept_cd.value = arr[0];
		frm.p_agency_cd.value = arr[1];
		frm.p_dept_cd_nm3.value = arr[2];
		frm.p_agency_cd_nm3.value = arr[3];
	}
	//교육전문직
	if(p_schoolgubun == 4){		
		frm.p_user_path4.value = arr[0];
		frm.p_dept_cd_nm3.value = arr[1];
		frm.p_agency_cd_nm3.value = arr[2];
		//frm.p_handphone_no1.value = arr[3];
		frm.p_dept_cd.value = arr[4];
		frm.p_agency_cd.value = arr[5];		
	}
	
}

function changeGubun(i,  check) {
	$('#jobCd1 option:eq(0)').prop('selected', true);
	$('#jobCd2 option:eq(0)').prop('selected', true);
	$('#memberGubun1 tr:eq(0)').after($('#trNicePersonalNum2'));
	$('#memberGubun1 tr:eq(0)').after($('#trNicePersonalNum1'));
	
	document.getElementById("trWorkPlaceInfo").style.display = "none";
	if(i == 2) { //교육 전문직
		if(check == undefined) {
			//alert("회원구분을 교육 전문직으로 변경합니다. \n교육전문직으로 변경시 나이스개인번호와 학교정보는 삭제됩니다.");
			alert("회원구분을 교육 전문직으로 변경합니다.");
		}
		document.getElementById("p_handphone_no2_view").style.display="";	//직장연락처
		document.getElementById("p_saddress1_view").style.display="";				//직장 상세주소
		document.getElementById("p_address1_view").style.display="";				//자택주소
		document.getElementById("memberGubun2").style.display = "";
		document.getElementById("memberGubun1").style.display = "none";
		document.getElementById("memberGubun3").style.display = "none";
		document.getElementById("plus_info_004").style.display = "";
		document.getElementById("plus_info_005").style.display = "";

		document.getElementById("plus_info_001").innerHTML = "소속기관명";
		//document.getElementById("plus_info_002").innerHTML = "근무지";
		//document.getElementById("plus_info_003").innerHTML = "자택";

		frm.personalState.vlaue = "Y";
		
		
		$('#plus_info_006').before($('#trNicePersonalNum1'));
		$('#plus_info_006').before($('#trNicePersonalNum2'));
		
		document.getElementById("trNicePersonalNumYn").style.display="";
		
		<c:if test="${empty view.nicePersonalNum}">
			changePersonalNumYn("N");				
			$("#personalNumN").prop("checked", true)
		</c:if>
		<c:if test="${not empty view.nicePersonalNum}">
			changePersonalNumYn("Y");				
			$("#personalNumY").prop("checked", true)
		</c:if>	
			
		
		
	} else if(i == 4) { //일반 학부모등
		if(check == undefined) {
			alert("회원구분을 일반회원(학부모 등)으로 변경합니다.");
		}
		
		document.getElementById("p_handphone_no2_view").style.display="none";	//직장연락처
		document.getElementById("p_saddress1_view").style.display="none";				//직장 상세주소
		document.getElementById("p_address1_view").style.display="";				//자택주소
		document.getElementById("memberGubun2").style.display = "";
		document.getElementById("memberGubun1").style.display = "none";
		document.getElementById("memberGubun3").style.display = "none";
		document.getElementById("plus_info_004").style.display = "none";
		document.getElementById("plus_info_005").style.display = "none";

		document.getElementById("plus_info_001").innerHTML = "직장명";
		//document.getElementById("plus_info_002").innerHTML = "학교/직장";
		//document.getElementById("plus_info_003").innerHTML = "자택";

		frm.personalState.vlaue = "Y";
		
		document.getElementById("trNicePersonalNumYn").style.display="none";
		
	} else if(i == 3) {
		if(check == undefined) {
			alert("회원구분을 보조인력으로 변경합니다.");
		}
		document.getElementById("p_handphone_no2_view").style.display="";	//직장연락처
		document.getElementById("p_saddress1_view").style.display="";				//직장 상세주소
		document.getElementById("p_address1_view").style.display="";				//자택주소
		document.getElementById("memberGubun3").style.display="";
		document.getElementById("memberGubun2").style.display="none";
		document.getElementById("memberGubun1").style.display="none";

		frm.personalState.vlaue = "Y";
	} else if(i == 5) {
		if(check == undefined) {
			alert("회원구분을 공무원으로 변경합니다.");
		}
		document.getElementById("p_handphone_no2_view").style.display="";	//직장연락처
		document.getElementById("p_saddress1_view").style.display="";				//직장 상세주소
		document.getElementById("p_address1_view").style.display="";				//자택주소
		document.getElementById("memberGubun2").style.display = "";
		document.getElementById("memberGubun1").style.display = "none";
		document.getElementById("memberGubun3").style.display = "none";
		document.getElementById("plus_info_004").style.display = "none";
		document.getElementById("plus_info_005").style.display = "none";

		document.getElementById("plus_info_001").innerHTML = "직장명";
		//document.getElementById("plus_info_002").innerHTML = "학교/직장";
		//document.getElementById("plus_info_003").innerHTML = "자택";
		$('#plus_info_005').after($('#trNicePersonalNum2'));
		$('#plus_info_005').after($('#trNicePersonalNum1'));
		//document.getElementById("p_nicePersonalNum").disabled="true";
		//document.getElementById("p_nicePersonalNum2").disabled="true";

		frm.personalState.vlaue = "Y";
		document.getElementById("trNicePersonalNumYn").style.display="";
		
		<c:if test="${empty view.nicePersonalNum}">
			changePersonalNumYn("N");				
			$("#personalNumN").prop("checked", true)
		</c:if>
		<c:if test="${not empty view.nicePersonalNum}">
			changePersonalNumYn("Y");				
			$("#personalNumY").prop("checked", true)
		</c:if>
		
	} else if(i == 6) {
		if(check == undefined) {
			alert("회원구분을 방과후교사 등으로 변경합니다.");
		}
		document.getElementById("memberGubun1").style.display="";
		document.getElementById("memberGubun2").style.display="none";
		document.getElementById("memberGubun3").style.display="none";
		
		document.getElementById("trNicePersonalNum1").style.display="none";
		document.getElementById("trNicePersonalNum2").style.display="none";	
		document.getElementById("trCareerSubject").style.display="none";
		
		document.getElementById("p_path").innerHTML = "학교명";
		document.getElementById("p_no").innerHTML = "학교 연락처";
		document.getElementById("p_addr").innerHTML = "학교 상세주소";
		
		document.getElementById("p_job_cd").style.display="none";
		document.getElementById("p_job_cd3").style.display="";
	} else {
		if(check == undefined) {
			alert("회원구분을 교원으로 변경합니다. \n교원으로 회원구분 변경시 관리자에게 연락주시기 바랍니다. \n☏ 041-537-1475~8");
		}
		document.getElementById("p_handphone_no2_view").style.display="";	//직장연락처
		document.getElementById("p_saddress1_view").style.display="";				//직장 상세주소
		document.getElementById("p_address1_view").style.display="none";				//자택주소
		document.getElementById("memberGubun1").style.display="";
		document.getElementById("memberGubun2").style.display="none";
		document.getElementById("memberGubun3").style.display="none";
		document.getElementById("p_job_cd").disabled="true";
		document.getElementById("p_job_cd").style.display="";
		document.getElementById("p_job_cd3").style.display="none";
		document.getElementById("p_nicePersonalNum").disabled="true";
		document.getElementById("p_nicePersonalNum2").disabled="true";
		document.getElementById("p_user_path").disabled="true";
		//document.getElementById("p_handphone_no1").disabled="true";
		document.getElementById("p_handphone_no1_1").disabled="true";
		document.getElementById("p_handphone_no1_2").disabled="true";
		document.getElementById("p_handphone_no1_3").disabled="true";

		document.getElementById("p_dept_cd_nm").disabled="true";
		document.getElementById("p_agency_cd_nm").disabled="true";
		document.getElementById("p_career").disabled="true";
		document.getElementById("p_subject").disabled="true";
		document.getElementById("p_mpost1").disabled="true";
		//document.getElementById("p_mpost2").disabled="true";
		document.getElementById("p_maddress1").disabled="true";
		frm.personalState.vlaue = "N";
		
		document.getElementById("trNicePersonalNumYn").style.display="none";
		
		document.getElementById("trNicePersonalNum1").style.display="";
		document.getElementById("trNicePersonalNum2").style.display="";	
		document.getElementById("trCareerSubject").style.display="";
	
		document.getElementById("p_path").innerHTML = "근무기관명";
		document.getElementById("p_no").innerHTML = "근무지 연락처";
		document.getElementById("p_addr").innerHTML = "근무지 주소";
	}
}

//직접입력 메일
function changeMail() {
	if (frm.p_email3.value != "") {
		frm.p_email2.value = frm.p_email3.value;
	} else if (frm.p_email3.value.length == 0) {
		frm.p_email2.value = "";
	}
}

//대문자 입력
function fnUpper(){
	var obj = document.getElementById("p_nicePersonalNum");
	var obj1 = document.getElementById("p_nicePersonalNum2");
	obj.value = obj.value.toUpperCase();
	obj1.value = obj1.value.toUpperCase();
}

function frmSubmit() {
	var flag = true;
	var reg = /^[A-Za-z]/i;
	var reg1 = /[A-Za-z0-9]/;
	var reg2 = /^[0-9]$/i;
	
	var frm = eval('document.<c:out value="${gsMainForm}"/>');

    if ( flag && !frm.p_email1.value ) {
		alert("전자우편을 입력하세요.");
		frm.p_email1.focus();
		flag = false;
		return false;
	}
	if ( flag && !frm.p_email2.value ) {
		alert("전자우편을 입력하세요.");
		frm.p_email2.focus();
		flag = false;
		return false;
	}

	if (frm.p_emp_gubun[0].checked == false) {
		if ( flag && !frm.p_address1.value ) {
			alert("우편번호 찾기를 이용해 주소를 입력하세요...");
			flag = false
			return false;;
		}
	}
	if ( flag && frm.p_hometel_1.value && isNaN( frm.p_hometel_1.value) ) {
		alert( "집전화번호는 숫자만 입력 가능합니다." );
		frm.p_hometel_1.focus();
		flag = false;
		return false;
	}

	if ( flag && frm.p_hometel_2.value && isNaN( frm.p_hometel_2.value) ) {
		alert( "집전화번호는 숫자와 -만 입력 가능합니다." );
		frm.p_hometel_2.focus();
		flag = false;
		return false;
	}

	if ( flag && frm.p_hometel_3.value && isNaN( frm.p_hometel_3.value) ) {
		alert( "집전화번호는 숫자와 -만 입력 가능합니다." );
		frm.p_hometel_3.focus();
		flag = false;
		return false;
	}

	if ((flag&&frm.p_hometel_1.value) && (flag&&frm.p_hometel_2.value) && (flag&&frm.p_hometel_3.value)) {
		frm.p_hometel.value=frm.p_hometel_1.value+"-"+frm.p_hometel_2.value+"-"+frm.p_hometel_3.value;
	}

	if ( flag && frm.p_handphone_1.value && isNaN( frm.p_handphone_1.value) ) {
		alert( "휴대전화번호는 숫자만 입력 가능합니다." );
		frm.p_handphone_1.focus();
		flag = false;
		return false;
	}

	if ( flag && frm.p_handphone_2.value && isNaN( frm.p_handphone_2.value) ) {
		alert( "휴대전화번호는 숫자만 입력 가능합니다." );
		frm.p_handphone_2.focus();
		flag = false;
		return false;
	}

	if ( flag && frm.p_handphone_3.value && isNaN( frm.p_handphone_3.value) ) {
		alert( "휴대전화번호는 숫자만 입력 가능합니다." );
		frm.p_handphone_3.focus();
		flag = false;
		return false;
	}

	if ((flag&&frm.p_handphone_1.value) && (flag&&frm.p_handphone_2.value) && (flag&&frm.p_handphone_3.value)) {
		frm.p_handphone.value = frm.p_handphone_1.value + "-" + frm.p_handphone_2.value + "-" + frm.p_handphone_3.value;
	}

	if (!frm.p_handphone.value) {
		alert("휴대전화번호를 입력하세요.");
		frm.p_handphone_1.focus();
		flag = false;
		return false;
	}

	if (frm.p_emp_gubun[0].checked == true) {
		

		<c:if test="${sessionScope.s_niceNumAllowYn eq 'N'}">

		var niceNum = frm.p_nicePersonalNum.value;
		
		if (frm.p_job_cd.value != '00039') {
			if (flag && frm.p_nicePersonalNum.value == '') {
				alert("나이스 개인번호를 입력하세요.");
				frm.p_nicePersonalNum.focus();
				return false;
			}

			if (!reg.test(niceNum)) {
				alert("나이스 개인번호 첫 글자는 영문이고, 나머지는 숫자입니다.");
				frm.p_nicePersonalNum.focus();
				return false;
			}

			if (!reg1.test(niceNum)) {
				alert("나이스 개인번호는 영문과 숫자만 허용됩니다.");
				frm.p_nicePersonalNum.focus();
				return false;
			}

			if (niceNum.length < 10) {
				alert("나이스 개인번호는 10자 입니다. 확인 후 다시 입력하세요.");
				frm.p_nicePersonalNum.focus();
				return false;
			}

			for (var i = 0; i < niceNum.length; i++) {
				if (i > 0) {
					if (!reg2.test(niceNum.charAt(i))) {
						alert("첫글자만 영문이고, 나머지는 숫자만 입력 가능합니다.");
						return false;
					}
				}
			}
			
			if(frm.o_nicePersonalNum.value !=  frm.p_nicePersonalNum.value){
				
				frm.p_overlap.value = 'N'
				
				if(frm.p_nicePersonalNum.value != frm.p_nicePersonalNum2.value) {
					alert("나이스 개인번호가 일치하지 않습니다.");
					return;
				}
				
				if ( flag && frm.p_overlap.value == 'N' ) {
					//alert("나이스 개인번호 중복확인을 해주세요.");
					alert("나이스개인번호중복확인을 다시 해주세요.");
					flag = false;
					return false;
				}
	
				if(frm.chkvalue.value != 'Y'){
					alert("나이스개인번호중복확인을 다시 해주세요.");
					return false;
				}
				
				
			
			}
		}
		</c:if>

		if( flag && frm.p_job_cd.value==''){
			alert("교사자격등급을  선택하세요.");
			frm.p_job_cd.focus();
			flag = false;
			return false;
		}

		if( flag && frm.p_user_path.value == ''){
			alert("근무기관명을 입력하세요.");
			frm.p_user_path.focus();
			flag = false;
			return false;
		}

		if( flag && frm.p_handphone_no1_1.value && isNaN( frm.p_handphone_no1_1.value) ){
			alert( "근무지 연락처는 숫자만 입력 가능합니다." );
			frm.p_handphone_no1_1.focus();
			flag = false;
			return false;
		}

		if( flag && frm.p_handphone_no1_2.value && isNaN( frm.p_handphone_no1_2.value) ){
			alert( "근무지 연락처는 숫자만 입력 가능합니다." );
			frm.p_handphone_no1_2.focus();
			flag = false;
			return false;
		}

		if( flag && frm.p_handphone_no1_3.value && isNaN( frm.p_handphone_no1_3.value) ){
			alert( "근무지 연락처는 숫자만 입력 가능합니다." );
			frm.p_handphone_no1_3.focus();
			flag = false;
			return false;
		}

		if((flag&&frm.p_handphone_no1_1.value) && (flag&&frm.p_handphone_no1_2.value) && (flag&&frm.p_handphone_no1_3.value)){
			frm.p_handphone_no.value=frm.p_handphone_no1_1.value+"-"+frm.p_handphone_no1_2.value+"-"+frm.p_handphone_no1_3.value;
			frm.p_handphone_no1.value=frm.p_handphone_no1_1.value+"-"+frm.p_handphone_no1_2.value+"-"+frm.p_handphone_no1_3.value;
		}

		if ( flag && !frm.p_maddress1.value ) {
			alert("우편번호 찾기를 이용해 근무지 주소를 입력하세요.");
			flag = false;
			return false;
		}

		if ( flag && frm.p_dept_cd_nm.value=='') {
			alert("근무기관명을 검색하여 입력해 주시기 바랍니다.");
			flag = false;
			return false;
		}

		//if (frm.p_hrdc1[0].checked==true) {
		//	frm.p_hrdc.value = "C";
		//} else {
		//	frm.p_hrdc.value = "H";
		//}
	} else if (frm.p_emp_gubun[1].checked==true) {
		
		/* if (flag && frm.p_job_cd2.value=='') {
			alert("교사자격등급을  선택하세요.");
			frm.p_job_cd.focus();
			flag = false;
			return false;
		} */

		if (flag && !frm.p_user_path2.value) {
			alert("학교명을 입력하세요.");
			frm.p_user_path2.focus();
			flag = false;
			return false;
		}

		if (flag && frm.p_handphone_no3_1.value && isNaN( frm.p_handphone_no3_1.value)) {
			alert( "학교 연락처는 숫자만 입력 가능합니다." );
			frm.p_handphone_no3_1.focus();
			flag = false;
			return false;
		}

		if (flag && frm.p_handphone_no3_2.value && isNaN( frm.p_handphone_no3_2.value)) {
			alert( "학교 연락처는 숫자만 입력 가능합니다." );
			frm.p_handphone_no3_2.focus();
			flag = false;
			return false;
		}

		if( flag && frm.p_handphone_no3_3.value && isNaN( frm.p_handphone_no3_3.value) ){
			alert( "학교 연락처는 숫자만 입력 가능합니다." );
			frm.p_handphone_no3_3.focus();
			flag = false;
			return false;
		}
		if((flag&&frm.p_handphone_no3_1.value) && (flag&&frm.p_handphone_no3_2.value) && (flag&&frm.p_handphone_no3_3.value)){
			frm.p_handphone_no.value=frm.p_handphone_no3_1.value+"-"+frm.p_handphone_no3_2.value+"-"+frm.p_handphone_no3_3.value;
			frm.p_handphone_no3.value=frm.p_handphone_no3_1.value+"-"+frm.p_handphone_no3_2.value+"-"+frm.p_handphone_no3_3.value;
		}
		if ( flag && !frm.p_maddress21.value ) {
			alert("우편번호 찾기를 이용해 주소를 입력하세요.");
			flag = false;
			return false;
		}

		if ( flag && frm.p_dept_cd_nm2.value=='-') {
			alert("학교를 다시 한번 검색하여 입력해 주시기 바랍니다.");
			flag = false;
			return false;
		}

		//if (frm.p_hrdc3[0].checked == true) {
		//	frm.p_hrdc.value = "C";
		//} else {
		//	frm.p_hrdc.value = "H";
		//}
	} else {
		
		if (frm.p_emp_gubun[2].checked == true  || frm.p_emp_gubun[4].checked == true) {	//R 교육전문직(교육연구사, 장학사 등) , O 공무원
			<c:if test="${sessionScope.s_niceNumAllowYn eq 'N'}">
			var niceNum = frm.p_nicePersonalNum.value;
			
			if(frm.personalNumYn[0].checked == true ){			
				if (flag && frm.p_nicePersonalNum.value == '') {
					alert("나이스 개인번호를 입력하세요.");
					frm.p_nicePersonalNum.focus();
					return false;
				}

				if (!reg.test(niceNum)) {
					alert("나이스 개인번호 첫 글자는 영문이고, 나머지는 숫자입니다.");
					frm.p_nicePersonalNum.focus();
					return false;
				}

				if (!reg1.test(niceNum)) {
					alert("나이스 개인번호는 영문과 숫자만 허용됩니다.");
					frm.p_nicePersonalNum.focus();
					return false;
				}

				if (niceNum.length < 10) {
					alert("나이스 개인번호는 10자 입니다. 확인 후 다시 입력하세요.");
					frm.p_nicePersonalNum.focus();
					return false;
				}

				for (var i = 0; i < niceNum.length; i++) {
					if (i > 0) {
						if (!reg2.test(niceNum.charAt(i))) {
							alert("첫글자만 영문이고, 나머지는 숫자만 입력 가능합니다.");
							return false;
						}
					}
				}
				if(frm.o_nicePersonalNum.value !=  frm.p_nicePersonalNum.value){
					
					frm.p_overlap.value = 'N'
					
					if(frm.p_nicePersonalNum.value != frm.p_nicePersonalNum2.value) {
						alert("나이스 개인번호가 일치하지 않습니다.");
						return;
					}
					
					if ( flag && frm.p_overlap.value == 'N' ) {
						//alert("나이스 개인번호 중복확인을 해주세요.");
						alert("나이스개인번호중복확인을 다시 해주세요.");
						flag = false;
						return false;
					}
	
					if(frm.chkvalue.value != 'Y'){
						alert("나이스개인번호중복확인을 다시 해주세요.");
						return false;
					}
				}
			}
			</c:if>
		}
		
		
		if( flag && frm.p_handphone_no2_1.value && isNaN( frm.p_handphone_no2_1.value) ){
			alert( "직장 연락처는 숫자만 입력 가능합니다." );
			frm.p_handphone_no2_1.focus();
			flag = false;
			return false;
		}

		if( flag && frm.p_handphone_no2_2.value && isNaN( frm.p_handphone_no2_2.value) ){
			alert( "직장 연락처는 숫자만 입력 가능합니다." );
			frm.p_handphone_no2_2.focus();
			flag = false;
			return false;
		}

		if( flag && frm.p_handphone_no2_3.value && isNaN( frm.p_handphone_no2_3.value) ){
			alert( "직장 연락처는 숫자만 입력 가능합니다." );
			frm.p_handphone_no2_3.focus();
			flag = false;
			return false;
		}

		if ((flag&&frm.p_handphone_no2_1.value) && (flag&&frm.p_handphone_no2_2.value) && (flag&&frm.p_handphone_no2_3.value)) {
			frm.p_handphone_no.value=frm.p_handphone_no2_1.value+"-"+frm.p_handphone_no2_2.value+"-"+frm.p_handphone_no2_3.value;
			frm.p_handphone_no2.value=frm.p_handphone_no2_1.value+"-"+frm.p_handphone_no2_2.value+"-"+frm.p_handphone_no2_3.value;
		}

		if (frm.p_dept_cd.value=='' && frm.p_emp_gubun[2].checked==true) {
			alert("근무기관명을 검색하여 입력해 주시기 바랍니다.");
			flag = false;
			return;
		}

		if ( flag && !frm.p_saddress1.value ) {
			alert("우편번호 찾기를 이용해 주소를 입력하세요.");
			flag = false;
			return;
		}

		//if (frm.p_hrdc2[0].checked==true) {
		//	frm.p_hrdc.value = "C";
		//} else {
		//	frm.p_hrdc.value = "H";
		//}
	}

	if (frm.personalState.vlaue == 'N') {
		alert("교원으로 회원구분 변경시 관리자에게 연락주시기 바랍니다. \n☏ 041-537-1475~8");
		flag = false;
	}
	

	var c_url = this.location+"";
    var url = "https://iedu.nise.go.kr/usr/mem/memJoinAction.do";
    //var url = "https://test.nise.go.kr/usr/mem/memJoinAction.do";
    if(c_url.match("localhost")){
    	url = "/usr/mem/memJoinAction.do";
    }
    
    
    
	if (flag) {
		if (confirm("저장하시겠습니까?")) {
			// 교원, 공무원이 아니면 초기화			
			if(frm.p_emp_gubun[0].checked != true && frm.p_emp_gubun[2].checked != true && frm.p_emp_gubun[4].checked != true) {			
				frm.p_nicePersonalNum.value = '';
				frm.p_nicePersonalNum2.value = '';
			}		
					
			if(frm.personalNumYn[1].checked == true){
				frm.p_nicePersonalNum.value = '';
				frm.p_nicePersonalNum2.value = '';
			}
			
			frm.action = url;
			frm.p_process.value = "update";
			frm.target	= "_self";
			frm.submit();
		}
	}
}

function fnPopup() {
	window.open('/usr/mpg/personalnumber.do', 'popupChk', 'width=395, height=530, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbars=no');
}
function fnPopup1() {
	window.open('/usr/mpg/personalnumber1.do', 'popupChk', 'width=646, height=427, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbars=yes');
}

window.onload = function () {
	if (frm.p_emp_gubun[0].checked == true ) {
		//changeGubun(1, true);
		document.getElementById("p_address1_view").style.display="none";
	}
	else if (frm.p_emp_gubun[1].checked == true) {
		changeGubun(3, true);
	}
	else if (frm.p_emp_gubun[2].checked == true) {
		changeGubun(2, true);
	}
	else if (frm.p_emp_gubun[4].checked == true) {
		changeGubun(5, true);
	} 
	else if (frm.p_emp_gubun[5].checked == true) {
		changeGubun(6, true);
	} 
	else {
		changeGubun(4, true);
	}

	$("#p_job_cd").change(function () {
		//사립유치원교원
		if($(this).val() == '00039'){			
			$("#trNicePersonalNum1").hide();			
			$("#trNicePersonalNum2").hide();
		}else{
			$("#trNicePersonalNum1").show();
			$("#trNicePersonalNum2").show();
		}
	}); 
	<c:if test="${view.jobCd eq '00039' }">
		$("#trNicePersonalNum1").hide();			
		$("#trNicePersonalNum2").hide();
	</c:if>
}
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
                if(1 == p_zipgubun) {
	                document.getElementById('p_post1').value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById('p_address1').value = fullAddr;
	
	                //document.getElementById('p_address2').focus();
                } else if(2 == p_zipgubun) {
                	document.getElementById('p_mpost1').value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById('p_maddress1').value = fullAddr;
	
	                //document.getElementById('p_maddress2').focus();
                } else if(4 == p_zipgubun) {
                	document.getElementById('p_mpost21').value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById('p_maddress21').value = fullAddr;
	
	                //document.getElementById('p_maddress22').focus();
                } else if(3 == p_zipgubun) {
                	document.getElementById('p_spost1').value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById('p_saddress1').value = fullAddr;
	
	                //document.getElementById('p_saddress2').focus();
                }
            }
        }).open();
    }
    
    function validNicePersonalNum() {
    	// 시도교육청
    	var deptCd = $('input[name=p_dept_cd]').val();
    	// 신분구분
    	var empGubun = $('input[name=p_emp_gubun]:checked').val();
    	// 나이스개인번호
    	var nicePersonalNum = $('input[name=p_nicePersonalNum2]').val();
    	
    	// valid
		if('' == nicePersonalNum || 10 != nicePersonalNum.length) {
			alert('나이스개인번호 10자리를 입력해 주세요.');
    		return;
    	}
    	//if('T' == empGubun && '' == deptCd) {
    		//alert('회원구분이 교원일 경우 시도교육청을 선택해주세요.');
    		//return;
    	//}
    	
    	$.ajax({
    		url: '${pageContext.request.contextPath}/valid/nicePersonalNum.do'
    		, type: 'post'
    		, data: {
    			deptCd: deptCd
    			, empGubun: empGubun
    			, nicePersonalNum: nicePersonalNum
    			, p_userid: frm.p_userid.value
    		}
    		, dataType: 'json'
    		, success: function(result) {
    			alert(result.resultMsg);
    			if(result.resultCode == 1) {
    				// 중복 값이 Y면 가입가능 
    				frm.p_overlap.value = 'Y';
    				frm.chkvalue.value = 'Y';
    				frm.o_nicePersonalNum.value = frm.p_nicePersonalNum.value;
    			} else {
    				frm.p_overlap.value = 'N';
    			}
    		}
    		, error: function(xhr, status, error) {
    			console.log(status);
    			console.log(error);   
    		}
    	});
    }
    
    function changePersonalNumYn(ynval){
    	if(ynval == "Y"){
    		document.getElementById("trNicePersonalNum1").style.display = "";
    		document.getElementById("trNicePersonalNum2").style.display = "";
    	}else {    		
    		document.getElementById("trNicePersonalNum1").style.display = "none";
    		document.getElementById("trNicePersonalNum2").style.display = "none";
    	}
    }
    
  	//교육대상자 구분에 특수로 선택 할 경우 근무지 정보 선택 할 수 있도록 추가 - 2019.08.29
    function showWorkPlaceInfo() {
    	var jobCdSelect = document.getElementById("p_job_cd");
		
    	var jobCdVal = jobCdSelect.options[jobCdSelect.selectedIndex].value;
    	
    	if(jobCdVal=="00033" || jobCdVal=="00034" || jobCdVal=="00035") {
    		document.getElementById("trWorkPlaceInfo").style.display = "";
    		$('#memberGubun1 tr:eq(0)').after($('#trWorkPlaceInfo'));
    	} else {
    		document.getElementById("trWorkPlaceInfo").style.display = "none";
    	}
    	
    }
</script>


<script type="text/javascript">
<%//주민등록번호(뒷번호 첫자리가 1-4), 법인번호(뒷번호 첫자리가 0), 외국인 번호(뒷번호 첫자리가 5-9) 유효성 검증%>
function validSsn( ssn ) {
    if ( -1>=ssn.search( /^\d\d[01]\d[0-3]\d\d\d{6}$/ ) )
        return false;

    var parity = 0;
    for ( i=0; i<12; i++ )
        parity += ssn.charAt( i ) * ( i%8 + 2 )
    parity  = ( 11 - parity % 11 ) % 10;

    return parity == ssn.charAt(12);
}


$(document).ready(function() {

	<c:if test="${!isDevelopmentMode}">
	
	    var sessionId = '${sessionId}';
	    var serverCert = '${serverCert}';
		var algorithm = "SEED";
		
	    epki.init(sessionId);
	    
	    
	    var thisForm = eval('document.<c:out value="${gsMainForm}"/>');	    
	    
	    html5Epki = function() {	
        	
	    	/* if(thisForm.p_resno1.value.search(/\S/) < 0 || thisForm.p_resno2.value.search(/\S/) < 0 ){
	            alert( "주민등록번호를 입력하세요.");
	            thisForm.p_resno1.focus();
	            return;
	        }

	    	$("#p_resno_userid").val(thisForm.p_resno1.value + thisForm.p_resno2.value);
	        if(!validSsn($("#p_resno_userid").val())){
	        	alert("주민등록번호 양식에 어긋납니다.");
	            return;
	        }
	        
	    	var userId = $("#p_resno_userid").val();	    	
            if(userId == ""){
            	alert( "주민등록번호를 입력하세요.");
            	$("#p_usrjumin").focus();
            } */
            
             var vidTexts = $("#epki_form").find("textarea"); 
        	
            /* Call Back Function Definition */
            var success = function(output) {            	
            	vidTexts.eq(0).val(output);
            	$("#epki_form").attr("target", "frmDn");
            	$("#epkidiv").find("form").submit();	
              	$("#loading_login").show();
            };
            var error = function(errCode, errMsg) {
                alert(errCode + ": " + errMsg);
            };

            epki.reqSecChannelAndLogin(serverCert, algorithm, sessionId, success, error);//주민번호 없이
            //epki.requestVerifyVID(serverCert, userId, success, error);	//주민번호

	    }
	   
        $("#epkiBtn").eq(0).click(function() {		
        	html5Epki();
        });        
        
    </c:if>
		
});
</script>


<iframe src="" frameborder="0" id="frmDn" name="frmDn" width="0"
	height="0" title="빈프레임"></iframe>

<!-- button -->
<%@ include
	file="/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp"%>
<!-- // button -->
