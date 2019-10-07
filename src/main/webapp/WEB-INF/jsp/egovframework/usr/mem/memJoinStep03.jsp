<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLogOutCheck.jsp" %>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
	<fieldset>
		<legend>회원가입양식 작성</legend>
		<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
		<input type="hidden" name="p_process" value="" />
		<input type="hidden" name="p_chkExistID" />
		<!--<input type="hidden" name="p_resno" value="${p_resno}" />-->
		<input type="hidden" name="p_hrdc" value="" />
		<input type="hidden" name="p_userid_temp" id="p_userid_temp" value="" />
		<input type="hidden" name="p_handphone_no" value="" />
		<input type="hidden" name="p_virtualNo" value="${virtualNo}"/>
		<input type="hidden" name="p_authInfo" value="${authInfo}"/>
		<input type="hidden" name="p_birthDate" value="${birthDate}"/>
		<input type="hidden" name="p_sex" value="${sex}"/>
		<input type="hidden" name="p_zipgubun" id="p_zipgubun" value="1" />
		<input type="hidden" name="p_schoolgubun" id="p_schoolgubun" value="1" />
		<input type="hidden" name="p_hometel" id="p_hometel" value="" />
		<input type="hidden" name="p_handphone" id="p_handphone" value="" />
		<input type="hidden" name="p_handphone_no2" id="p_handphone_no2" value="" />
		<input type="hidden" name="p_overlap" id="p_overlap" value="N" />
		<input type="hidden" name="p_dept_cd" value="" />
		<input type="hidden" name="p_agency_cd" value="" />

		<input type="hidden" name="sCipherTime" value="${sCipherTime }"/>
		<input type="hidden" name="reqSeq" value="${REQ_SEQ }"/>
		<input type="hidden" name="resSeq" value="${RES_SEQ }"/>
		<input type="hidden" name="authType" value="${AUTH_TYPE }"/>
		<input type="hidden" name="di" value="${DI }"/>
		<input type="hidden" name="ci" value="${CI }"/>
		<input type="hidden" name="nationainfo" value="${NATIONAINFO }"/>
		<input type="hidden" name="certgubun" value="${certgubun }"/>

		<input type="hidden" name="p_d_type" id="p_d_type"	value="O"/>

		<div class="wrap_step">
			<ul>
				<li><img src="/images/user/img_step01_off.gif" alt="약관동의" /></li>
				<li><img src="/images/user/img_step02_off.gif" alt="가입인증" /></li>
				<li><img src="/images/user/img_step03_on.gif" alt="현재단계  기본정보입력" /></li>
				<li><img src="/images/user/img_step04_off.gif" alt="가입완료" /></li>
			</ul>
		</div>

		<p class="m30 mb5"><span class="num1">*</span> 표시는 필수 입력사항입니다</p>

		<!-- list table-->
		<div class="studyList">
			<table summary="회원가입 양식이며 2열로 구성되어 1열은 가입항목을 2열은 내용으로 구성됩니다." cellspacing="0" width="100%">
				<caption>회원가입양식작성</caption>
				<colgroup>
					<col width="25%" />
					<col width="75%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="row" class="last left">이름 <span class="num1">*</span></th>
						<td class="last left">
							<c:out value="${realName}"/>
							<input type="hidden" name="p_name" size="20" id="p_name" value="${realName}" />
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left">생년월일 <span class="num1">*</span></th>
						<td class="last left">
							<c:out value="${fn:substring(birthDate, 0,4)}"/>년 <c:out value="${fn:substring(birthDate, 4,6)}"/>월 <c:out value="${fn:substring(birthDate, 6,8)}"/>일
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left">성별 <span class="num1">*</span></th>
						<td class="last left">
							<c:if test="${sex eq 'F'}">여</c:if>
							<c:if test="${sex eq 'M'}">남</c:if>
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left"><label for="p_userid">아이디</label> <span class="num1">*</span></th>
						<td class="last left">
							<input type="text" name="p_userid" size="20" id="p_userid" onkeypress="action_enter(event);" style="ime-mode:disabled;" title="아이디" />
							<a href="#" class="pop_btn01" onclick="existID()" title="ID 중복확인 새창" style="cursor:hand" ><span>ID중복확인</span></a>
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left"><label for="p_pwd">비밀번호</label> <span class="num1">*</span></th>
						<td class="last left">
							<input type="password" name="p_pwd" size="20" id="p_pwd" title="비밀번호" />
							<br/>
							* 비밀번호는 <strong>영문자+숫자+특수문자</strong>를 혼합하여(각 문자 1회 꼭 이용)<br />&nbsp;&nbsp;8자 이상 입력하셔야 합니다.<br />
							* 비밀번호내에 문자는 최소 2자 이상 입력하셔야 합니다.
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left"><label for="p_pwd2">비밀번호 확인</label> <span class="num1">*</span></th>
						<td class="last left">
							<input type="password" name="p_pwd2" size="20" id="p_pwd2" title="비밀번호 확인" />
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left"><label for="p_hometel1">전화번호</label><span class="num1">*</span></th>
						<td class="last left">
							<input type="text" name="p_hometel1" size="4" id="p_hometel1" maxlength="4" value="" title="전화번호 앞자리" /> -
							<input type="text" name="p_hometel2" size="4" id="p_hometel2" maxlength="4" value="" title="전화번호 가운뎃자리"/> -
							<input type="text" name="p_hometel3" size="4" id="p_hometel3" maxlength="4" value="" title="전화번호 뒷자리"/>
							* 번호 02-123-1231
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left"><label for="p_handphone1">휴대폰번호</label> 및 수신여부 <span class="num1">*</span></th>
						<td class="last left">
							<input type="text" name="p_handphone1" size="4" id="p_handphone1"  maxlength="4" value="" title="휴대폰 앞자리" /> -
							<input type="text" name="p_handphone2" size="4" id="p_handphone2"  maxlength="4" value="" title="휴대폰 가운뎃자리" /> -
							<input type="text" name="p_handphone3" size="4" id="p_handphone3"  maxlength="4" value="" title="휴대폰 뒷자리" />
							<br />
							<input type="checkbox" name="p_issms" id="p_issms"  value="Y" checked="checked"class="input_border vrM"/><label for="p_issms">SMS(문자메세지) 수신설정에 동의하겠습니다.</label>
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left"><label for="p_email1">전자우편</label> <span class="num1">*</span></th>
						<td class="last left">
							<input type="text" name="p_email1" size="20" id="p_email1" style="ime-mode:disabled" />
							@<label for="p_email2" class="blind">서비스도메인</label>
							<input type="text" name="p_email2" size="17" id="p_email2" style="ime-mode:disabled" value="" title="서비스도메인"/>
							<label for="p_email3" class="blind">서비스도메인 선택</label>
							<ui:code id="p_email3" selectItem="${p_email3}" gubun="" codetype="0067"  upper="" title="서비스도메인 선택" className="" type="select" selectTitle="직접입력" event="changeMail" />
							<br />
							<input type="checkbox" name="p_ismailling" id="p_ismailling"  value="Y" checked="checked" class="input_border vrM"/>
							<label for="p_ismailling">전자우편 수신설정에 동의하겠습니다.</label>
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left">자택주소 <span class="num1">*</span></th>
						<td class="last left">
							<a href="#" onclick="sample6_execDaumPostcode(1);return false;" title="새창"><img src="/images/user/btn_post.gif" alt="우편번호찾기"/></a>
							<label for="p_post1" class="blind">우편번호</label>
							<input type="text" name="p_post1" size="10" id="p_post1" readonly="readonly" title="우편번호 앞자리"/>
							<!-- -
							<input type="text" name="p_post2" size="10" id="p_post2" readonly="readonly" title="우편번호 뒷자리"/> -->
							<br />
							<input type="text" name="p_address1" size="45" id="p_address1" readonly="readonly" class="mt7" title="주소"/><br />
							<label for="p_address2">상세주소 :</label><input type="text" name="p_address2" size="45" id="p_address2"  class="mt7" title="상세주소" /><br /><br/>
							*택배 발송용 주소이므로 자세하고 정확하게 입력해 주세요.<br />
							*이수증 발급시 주소가 불분명하면 반송되는 경우가 많습니다.<br />
							번지수까지 자세하게 기입하여 주십시오.(교원만 해당)
						</td>
					</tr>
					<tr>
						<th scope="row" class="last left">회원구분 <span class="num1">*</span></th>
						<td class="last left">
							<input type="radio" name="p_emp_gubun" id="p_emp_gubun1" value="T" onclick="changeGubun(1);" class="input_border vrM" checked="checked"><label for="p_emp_gubun1">교원</label>&nbsp;
							<input type="radio" name="p_emp_gubun" id="p_emp_gubun3" value="E" onclick="changeGubun(3);" class="input_border vrM"><label for="p_emp_gubun3">보조인력</label>
							<input type="radio" name="p_emp_gubun" id="p_emp_gubun2" value="R" onclick="changeGubun(2);" class="input_border vrM"><label for="p_emp_gubun2">교육 전문직</label>
							<input type="radio" name="p_emp_gubun" id="p_emp_gubun4" value="P" onclick="changeGubun(4);" class="input_border vrM"><label for="p_emp_gubun4">일반회원(학부모 등)</label>
							<input type="radio" name="p_emp_gubun" id="p_emp_gubun5" value="O" onclick="changeGubun(5);" class="input_border vrM"><label for="p_emp_gubun5">공무원</label>
						</td>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<!-- list table-->

		<!--교원정보-->
		<div id="memberGubun1">
			<div class="sub_txt_1depth m30">
				<h4>회원근무정보</h4>
			</div>

			<div class="studyList">
				<table summary="회원근무정보 입력양식으로 2열로 구성되며 1열은 작성항목 2열은 작성란으로 구성" cellspacing="0" border="0" width="100%">
					<caption>회원근무정보</caption>
					<colgroup>
						<col width="20%" />
						<col width="30%" />
						<col width="20%" />
						<col width="30%" />
					</colgroup>
					<thead>
						<tr>
							<th scope="row" class="last left"><label for="p_job_cd">교사자격등급</label> <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<select name="p_job_cd" id="p_job_cd" title="교사자격등급">
									<option value="">===선택===</option>
									<c:forEach items="${list}" var="result" varStatus="i">
										<option value="${result.code }">${result.codenm }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr id="tr_nicePersonalNum">
							<th scope="row" class="last left"><label for="p_nicePersonalNum"><font color="red">나이스 개인번호</font></label> <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<input type="text" name="p_nicePersonalNum" id="p_nicePersonalNum" maxlength="10" title="나이스 개인번호" value="" onkeyup="fnUpper()"/>
								<b>
									<font color="red">
										* 나이스 개인번호는 총 10자리이며, 첫글자는 반드시 영문, 네번째 자리는 1:교원, 6:지방공무원, 8:원어민보조교사, 9:기타직, A:영어회화 정문강사이며 나머지는 숫자로만 입력하셔야 합니다.
									</font>
								</b>
							</td>
						</tr>
						<tr id="tr_nicePersonalNum2">
							<th scope="row" class="last left"><label for="p_nicePersonalNum2">나이스 개인번호 확인</label> <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<input type="text" name="p_nicePersonalNum2" id="p_nicePersonalNum2" maxlength="10" title="나이스 개인번호 확인" value="" onkeyup="fnUpper()"/>
								<a href="#" class="pop_btn01" onclick="validNicePersonalNum();return false;" title="나이스개인번호중복확인" style="cursor:hand"><span>나이스개인번호 검증</span></a>
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label for="p_user_path">학교명</label> <span class="num1">*</span></th>
							<td class="last left">
								<input type="text" name="p_user_path" size="20" id="p_user_path" title="학교명" readonly />
								<a href="#" class="pop_btn01" onclick="searchSchool(1)" title="학교검색" style="cursor:hand"><span>검색</span></a>
							</td>
							<th scope="row" class="last left"><label for="p_handphone_no1">학교 연락처</label><span class="num1">*</span></th>
							<td class="last left">
								<!--
								<input type="text" name="p_handphone_no11" size="4" id="p_handphone_no11"  maxlength="4" value=""/> -
								<input type="text" name="p_handphone_no12" size="4" id="p_handphone_no12"  maxlength="4" value=""/> -
								<input type="text" name="p_handphone_no13" size="4" id="p_handphone_no13"  maxlength="4" value=""/>
								-->
								<input type="text" name="p_handphone_no1" size="15" id="p_handphone_no1"  maxlength="15" value="" title="학교연락처"/>
								<br/>* 번호 02-123-1231
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label for="p_dept_cd_nm">시도교육청</label> <span class="num1">*</span></th>
							<td class="last left" >
								<!--<ui:code id="p_dept_cd" selectItem="" gubun="eduOrg" codetype="M"  upper="" title="시도교육청" className="" type="select" selectTitle="::선택::" event="whenSelection()" /> -->
								<input type="text" name="p_dept_cd_nm" size="20" id="p_dept_cd_nm"  maxlength="20" value="" title="시도교육청" readonly/>
							</td>
							<th scope="row" class="last left"><label for="p_agency_cd_nm">교육지원청</label> <span class="num1">*</span></th>
							<td class="last left" >
								<!--<ui:code id="p_agency_cd" selectItem="" gubun="eduOrg" codetype=""  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="" />-->
								<input type="text" name="p_agency_cd_nm" size="20" id="p_agency_cd_nm"  maxlength="20" value="" title="교육지원청" readonly/>
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label for="p_career">경력</label> <span class="num1">*</span></th>
							<td class="last left" >
								<ui:code id="p_career" selectItem="" gubun="" codetype="0116"  upper="" title="경력" className="" type="select" selectTitle="선택" event="" />
							</td>
							<th scope="row" class="last left"><label for="p_subject">담당교과</label> <span class="num1">*</span></th>
							<td class="last left" >
								<input type="text" name="p_subject" id="p_subject" maxlength="30" title="담당교과" value=""/>
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left">학교상세주소 <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<a href="#" onclick="sample6_execDaumPostcode(2);return false;" title="새창"><img src="/images/user/btn_post.gif" alt="우편번호찾기"/></a>
								<label for="p_mpost1" class="blind">우편번호</label>
								<input type="text" name="p_mpost1" size="10" id="p_mpost1" readonly="readonly" title="우편번호 앞자리"/>
								<!-- -
								<input type="text" name="p_mpost2" size="10" id="p_mpost2" readonly="readonly" title="우편번호 뒷자리"/> -->
								<br />
								<label for="p_maddress1" class="blind">주소</label>
								<input type="text" name="p_maddress1" size="45" id="p_maddress1" readonly="readonly" class="mt7" title="주소"/>
								<br/>
								<input type="text" name="p_maddress2" size="45" id="p_maddress2" class="mt7" title="상세주소" />
								<label for="p_maddress2">상세주소</label>
								<br />
								번지수까지 자세하게 기입하여 주십시오.(교원만 해당)
							</td>
						</tr>
						<!-- <tr>
							<th scope="row" class="last left">교재 수령지 <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<input type="radio" name="p_hrdc1" id="p_hrdc1_1" value="C" checked="checked"class="input_border vrM"><label for="p_hrdc1_1">학교</label>&nbsp;
								<input type="radio" name="p_hrdc1" id="p_hrdc1_2" value="H" class="input_border vrM"><label for="p_hrdc1_2">자택</label>
							</td>
						</tr> -->
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>

		<!-- 보조인력 -->
		<div id="memberGubun3" style="display:none">
			<div class="sub_txt_1depth m30">
				<h4>회원근무정보</h4>
			</div>
			<div class="studyList">
				<table summary="회원근무정보 입력양식으로 2열로 구성되며 1열은 작성항목 2열은 작성란으로 구성" cellspacing="0" width="100%">
					<caption>회원근무정보</caption>
					<colgroup>
						<col width="20%" />
						<col width="30%" />
						<col width="20%" />
						<col width="30%" />
					</colgroup>
					<thead>
						<!--
						<tr>
							<th scope="row" class="last left"><label for="p_job_cd2">교사자격등급</label> <span class="num1">*</span></th>
							<td class="last left"  colspan="3">
								<ui:code id="p_job_cd2" selectItem="${p_job_cd}" gubun="" codetype="0115"  upper="" title="" className="" type="select" selectTitle="선택" event="" />
							</td>
						</tr>
						-->
						<tr>
							<th scope="row" class="last left"><label for="p_user_path2">학교명</label> <span class="num1">*</span></th>
							<td class="last left">
								<input type="text" name="p_user_path2" size="20" id="p_user_path2" title="학교명" readonly/>
								<input type="hidden" name="p_job_cd2" size="20" id="p_job_cd2" value="0"/>
								<a href="#" class="pop_btn01" onclick="searchSchool(2)" title="학교검색" style="cursor:hand"><span>검색</span></a>
							</td>
							<th scope="row" class="last left"><label for="p_handphone_no3">학교 연락처</label><span class="num1">*</span></th>
							<td class="last left">
								<!--<input type="text" name="p_handphone_no31" size="4" id="p_handphone_no31"  maxlength="4" value=""/> -
								<input type="text" name="p_handphone_no32" size="4" id="p_handphone_no32"  maxlength="4" value=""/> -
								<input type="text" name="p_handphone_no33" size="4" id="p_handphone_no33"  maxlength="4" value=""/>-->
								<input type="text" name="p_handphone_no3" size="15" id="p_handphone_no3"  maxlength="15" value="" title="학교연락처" />
								<br/>* 번호 02-123-1231
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label for="p_dept_cd_nm2">시도교육청</label> <span class="num1">*</span></th>
							<td class="last left" >
								<!--<ui:code id="p_dept_cd" selectItem="" gubun="eduOrg" codetype="M"  upper="" title="시도교육청" className="" type="select" selectTitle="::선택::" event="whenSelection()" /> -->
								<input type="text" name="p_dept_cd_nm2" size="20" id="p_dept_cd_nm2"  maxlength="20" value="" title="시도교육청" readonly/>
							</td>
							<th scope="row" class="last left"><label for="p_agency_cd2_nm">교육지원청</label> <span class="num1">*</span></th>
							<td class="last left" >
								<!--<ui:code id="p_agency_cd" selectItem="" gubun="eduOrg" codetype=""  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="" />-->
								<input type="text" name="p_agency_cd_nm2" size="20" id="p_agency_cd_nm2"  maxlength="20" value="" title="교육지원청" readonly/>
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left">학교상세주소 <span class="num1">*</span></th>
							<td class="last left"  colspan="3">
								<a href="#" onclick="sample6_execDaumPostcode(4);return false;" title="새창"><img src="/images/user/btn_post.gif" alt="우편번호찾기"/></a>
								<label for="p_mpost21" class="blind">우편번호</label>
								<input type="text" name="p_mpost21" size="10" id="p_mpost21" readonly="readonly" title="우편번호 앞자리"/>
								<!-- -
								<input type="text" name="p_mpost22" size="10" id="p_mpost22" readonly="readonly" title="우편번호 뒷자리"/> -->
								<br />
								<label for="p_maddress21" class="blind">주소</label>
								<input type="text" name="p_maddress21" size="45" id="p_maddress21" readonly="readonly" class="mt7" title="주소"/><br/>
								<input type="text" name="p_maddress22" size="45" id="p_maddress22" class="mt7"/><label for="p_maddress22">상세주소</label>
								<br />
								번지수까지 자세하게 기입하여 주십시오.(교원만 해당)
							</td>
						</tr>
						<!-- <tr>
							<th scope="row" class="last left" >교재 수령지 <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<input type="radio" name="p_hrdc3" id="p_hrdc3_1" value="C" checked="checked" class="input_border vrM"><label for="p_hrdc3_1">학교</label>&nbsp;
								<input type="radio" name="p_hrdc3" id="p_hrdc3_2" value="H" class="input_border vrM"><label for="p_hrdc3_2">자택</label>
							</td>
						</tr> -->
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>

		<!--학부모정보 && 교육 전문직-->
		<div id="memberGubun2" style="display:none">
			<div class="sub_txt_1depth m30">
				<h4>일반회원 정보</h4>
			</div>
			<div class="studyList">
				<table summary="일반회원 정보 작성양식으로 2열로 구성되며 1열은 작성항목 2열은 작성란으로 구성" cellspacing="0" width="100%">
					<caption>일반회원 정보</caption>
					<colgroup>
						<col width="20%" />
						<col width="30%" />
						<col width="20%" />
						<col width="30%" />
					</colgroup>
					<thead>
						<tr>
							<th scope="row" class="last left">
								<label for="p_position_nm">
									<span id="plus_info_001" style="font-weight:bold;">소속/직장명</span>
								</label>
							</th>
							<td class="last left" colspan="3">
								<!--<ui:code id="p_dept_cd" selectItem="" gubun="eduOrg" codetype="M"  upper="" title="시도교육청" className="" type="select" selectTitle="::선택::" event="whenSelection()" /> -->
								<input type="text" name="p_position_nm" size="20" id="p_position_nm"   value="" />
							</td>
							<!--
							<th scope="row" class="last left"><label for="p_work_place_nm">근무지</label> </th>
							<td class="last left" colspan="3" >
								<ui:code id="p_agency_cd" selectItem="" gubun="eduOrg" codetype=""  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="" />
								<input type="text" name="p_work_place_nm" size="20" id="p_work_place_nm"   value="" />
							</td>
							-->
						</tr>
						<tr id="plus_info_004">
							<th scope="row" class="last left"><label for="p_user_path2">교육청선택</label> <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<%-- <input type="text" name="education_Office" size="20" id="education_Office" value="${view.deptnm}" readonly/>	 --%>
								<a href="#" class="pop_btn01" onclick="education_Office()" title="교육청검색" style="cursor:hand"><span>교육청 검색</span></a>
							</td>
						</tr>
						<tr id="plus_info_005">
							<th scope="row" class="last left"><label for="p_dept_cd">시도교육청</label> <span class="num1">*</span></th>
							<td class="last left">
								<!--<ui:code id="p_dept_cd" selectItem="${view.deptCd}" gubun="eduOrg" codetype="M"  upper="" title="시도교육청" className="" type="select" selectTitle="::선택::" event="whenSelection()" />-->
								<input type="text" name="p_dept_cd_nm3" size="20" id="p_dept_cd_nm3"  maxlength="20" value="${view.deptnm }" readonly/>
							</td>
							<th scope="row" class="last left"><label for="p_agency_cd">교육지원청</label> <span class="num1">*</span></th>
							<td class="last left">
								<!--<ui:code id="p_agency_cd" selectItem="${view.agencyCd}" gubun="eduOrg" codetype=""  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="" /> -->
								<input type="text" name="p_agency_cd_nm3" size="20" id="p_agency_cd_nm3"  maxlength="20" value="${view.agencynm}" readonly/>
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label for="p_division_nm">부서명</label> </th>
							<td class="last left" >
								<!--<ui:code id="p_dept_cd" selectItem="" gubun="eduOrg" codetype="M"  upper="" title="시도교육청" className="" type="select" selectTitle="::선택::" event="whenSelection()" /> -->
								<input type="text" name="p_division_nm" size="20" id="p_division_nm"  value="" />
							</td>
							<th scope="row" class="last left"><label for="p_post_nm">직급</label> </th>
							<td class="last left" >
								<!--<ui:code id="p_agency_cd" selectItem="" gubun="eduOrg" codetype=""  upper="" title="교육지원청" className="" type="select" selectTitle="::선택::" event="" />-->
								<input type="text" name="p_post_nm" size="20" id="p_post_nm"  value="" />
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left"><label for="p_handphone_no21">직장 연락처</label> <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<input type="text" name="p_handphone_no21" size="4" id="p_handphone_no21"  maxlength="4" value=""/> -
								<input type="text" name="p_handphone_no22" size="4" id="p_handphone_no22"  maxlength="4" value=""/> -
								<input type="text" name="p_handphone_no23" size="4" id="p_handphone_no23"  maxlength="4" value=""/> *번호 02-123-1231
							</td>
						</tr>
						<tr>
							<th scope="row" class="last left">직장상세주소 <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<a href="#" onclick="sample6_execDaumPostcode(3);return false;" title="새창"><img src="/images/user/btn_post.gif" alt="우편번호찾기"/></a>
								<label for="p_spost1" class="blind">우편번호</label>
								<input type="text" name="p_spost1" size="10" id="p_spost1" readonly="readonly" title="우편번호 앞자리"/>
								<!-- -
								<input type="text" name="p_spost2" size="10" id="p_spost2" readonly="readonly" title="우편번호 뒷자리"/> -->
								<br />
								<label for="p_saddress1" class="blind">주소</label>
								<input type="text" name="p_saddress1" size="45" id="p_saddress1" readonly="readonly" class="mt7" title="주소"/><br/>
								<input type="text" name="p_saddress2" size="45" id="p_saddress2" class="mt7"/><label for="p_saddress2">상세주소</label>
								<br />
								번지수까지 자세하게 기입하여 주십시오.<br/>
							</td>
						</tr>
						<!-- <tr>
							<th scope="row" class="last left">교재 수령지 <span class="num1">*</span></th>
							<td class="last left" colspan="3">
								<input type="radio" name="p_hrdc2" id="p_hrdc2_1" value="C" checked="checked"class="input_border vrM"><label id="plus_info_002" for="p_hrdc2_1">학교</label>&nbsp;
								<input type="radio" name="p_hrdc2" id="p_hrdc2_2" value="H"  class="input_border vrM"><label id="plus_info_003" for="p_hrdc2_2">자택</label>
							</td>
						</tr> -->
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
		<p class="m10">* 시도교육청과 학교명이 바르게 기재되지 않으면 연수인증이 되지 않을 수 있으니 반드시 기재 바랍니다. </p>

		<!-- button -->
		<ul class="btnR">
			<li><a href="#" onclick="frmSubmit()"><img src="/images/user/btn_save.gif" alt="저장" /></a></li>
		</ul>
		<!-- // button -->
	</fieldset>
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

<script type="text/javascript">
<!--

var frm = eval('document.<c:out value="${gsMainForm}"/>');


//하위기관..
function whenSelection() {

	$("#p_agency_cd").html('');
	$("#p_agency_cd").append('<option value=\"\">::선택::</option>');

	$.ajax({
		url: "<c:out value="${gsDomainContext}" />/com/aja/sch/selectEduOrgList.do",
		data: {searchAgencyCode : function() {return $("#p_dept_cd").val();}},
		dataType: 'json',
		contentType : "application/json:charset=utf-8",
		success: function(data) {
			data = data.result;
			for (var i = 0; i < data.length; i++) {
				var value = data[i].code;
				var title = data[i].codenm

				if('<c:out value="${view.agencyCd}"/>' == value)
					$("select#p_agency_cd").append("<option value='"+value+"' selected>"+title+"</option>");
				else
					$("select#p_agency_cd").append("<option value='"+value+"'>"+title+"</option>");
			}
		},
		error: function(xhr, status, error) {
			alert(status);
			alert(error);
		}
	});
}

function whenSelection2() {

	$("#p_agency_cd2").html('');
	$("#p_agency_cd2").append('<option value=\"\">::선택::</option>');

	$.ajax({
		url: "<c:out value="${gsDomainContext}" />/com/aja/sch/selectEduOrgList.do",
		data: {searchAgencyCode : function() {return $("#p_dept_cd2").val();}},
		dataType: 'json',
		contentType : "application/json:charset=utf-8",
		success: function(data) {
			data = data.result;
			for (var i = 0; i < data.length; i++) {
				var value = data[i].code;
				var title = data[i].codenm

				if('<c:out value="${view.agencyCd}"/>' == value)
					$("select#p_agency_cd2").append("<option value='"+value+"' selected>"+title+"</option>");
				else
					$("select#p_agency_cd2").append("<option value='"+value+"'>"+title+"</option>");
			}
		},
		error: function(xhr, status, error) {
			alert(status);
			alert(error);
		}
	});
}


function existID( ) {
	var userid = frm.p_userid.value;
	var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	var str_1 = 0;
	if(frm.p_userid.value.search(/\S/) < 0) {
		alert("아이디를 입력하세요.");
		frm.p_userid.focus();
		return;
	}

	if (!(isAlphaNum(frm.p_userid.value)) || frm.p_userid.value.length < 6 || frm.p_userid.value.length > 10 )
    {
        alert("아이디는 6~10자까지 영문/숫자만 가능합니다.");
        frm.p_userid.focus();
        return;
    }

    for(var a=0; a<frm.p_userid.value.length; a++) {
		for(var b=0; b < str.length; b++) {
			if(frm.p_userid.value.charAt(a)==str.charAt(b)) str_1++;
		}
	}

	if( str_1 < 1) {
		alert("문자는 최소 1개 이상 입력하세요.");
		frm.p_userid.focus();
		return;
	}

	window.open('', 'existIdPop', 'width=300,height=200');
	frm.action = "/com/pop/existIdPopup.do";
	frm.target = "existIdPop";
	frm.submit();
}

function action_enter(e)  {
    if (e.keyCode =="13"){
    	//existNickname();
    	existID();
        e.returnValue = false;
        e.cancelBubble = true;
    }
}


//우편번호찾기
function searchZipcode(p_zipgubun){
	frm.p_zipgubun.value = p_zipgubun;
	window.open('', 'zipcodeWindowPop', 'width=500,height=600');
	frm.action = "/com/pop/searchZipcodePopup.do";
	frm.target = "zipcodeWindowPop";
	frm.submit();
}

function searchSchool(p_schoolgubun){
	frm.p_schoolgubun.value = p_schoolgubun;
	window.open('', 'schoolWindowPop', 'width=550,height=600');
	frm.action = "/usr/mem/searchSchoolPop.do";
	frm.target = "schoolWindowPop";
	frm.submit();
}

function receiveSchool(arr){
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

}
//우편번호 넣기
function receiveZipcode(arr){
	var p_zipgubun = frm.p_zipgubun.value;

	if(p_zipgubun == 1)
	{
		frm.p_post1.value = arr[0];
		frm.p_post2.value = arr[1];
		frm.p_address1.value = arr[2];
	}
	if(p_zipgubun == 2)
	{
		frm.p_mpost1.value = arr[0];
		frm.p_mpost2.value = arr[1];
		frm.p_maddress1.value = arr[2];
	}
	if(p_zipgubun == 3)
	{
		frm.p_spost1.value = arr[0];
		frm.p_spost2.value = arr[1];
		frm.p_saddress1.value = arr[2];
	}
	if(p_zipgubun == 4)
	{
		frm.p_mpost21.value = arr[0];
		frm.p_mpost22.value = arr[1];
		frm.p_maddress21.value = arr[2];
	}
}


function changeGubun(i){
	$('#memberGubun1 tr:eq(0)').after($('#tr_nicePersonalNum2'));
	$('#memberGubun1 tr:eq(0)').after($('#tr_nicePersonalNum'));
	if (i == 2) {	//교원 전문직
		document.getElementById("memberGubun2").style.display="";
		document.getElementById("memberGubun1").style.display="none";
		document.getElementById("memberGubun3").style.display="none";
		document.getElementById("plus_info_001").innerHTML = "소속";
		//document.getElementById("plus_info_002").innerHTML = "학교";
		//document.getElementById("plus_info_003").innerHTML = "근무지";
		document.getElementById("plus_info_004").style.display = "";
		document.getElementById("plus_info_005").style.display = "";
	} else if(i == 4) { //일반 학부모등
		document.getElementById("memberGubun2").style.display = "";
		document.getElementById("memberGubun1").style.display = "none";
		document.getElementById("memberGubun3").style.display = "none";
		document.getElementById("plus_info_001").innerHTML = "직장명";
		//document.getElementById("plus_info_002").innerHTML = "직장";
		//document.getElementById("plus_info_003").innerHTML = "자택";
		document.getElementById("plus_info_004").style.display = "none";
		document.getElementById("plus_info_005").style.display = "none";
	}else if(i == 3){
		document.getElementById("memberGubun3").style.display="";
		document.getElementById("memberGubun2").style.display="none";
		document.getElementById("memberGubun1").style.display="none";
	}else if(i == 5){
		document.getElementById("memberGubun2").style.display = "";
		document.getElementById("memberGubun1").style.display = "none";
		document.getElementById("memberGubun3").style.display = "none";
		document.getElementById("plus_info_001").innerHTML = "직장명";
		//document.getElementById("plus_info_002").innerHTML = "직장";
		//document.getElementById("plus_info_003").innerHTML = "자택";
		document.getElementById("plus_info_004").style.display = "none";
		document.getElementById("plus_info_005").style.display = "none";
		document.getElementById("plus_info_004").style.display = "none";
		document.getElementById("plus_info_005").style.display = "none";
		$('#plus_info_005').after($('#tr_nicePersonalNum2'));
		$('#plus_info_005').after($('#tr_nicePersonalNum'));
	}else{
		document.getElementById("memberGubun1").style.display="";
		document.getElementById("memberGubun2").style.display="none";
		document.getElementById("memberGubun3").style.display="none";
	}
}

//직접입력 메일
function changeMail(){
	if(frm.p_email3.value != ""){
		frm.p_email2.value = frm.p_email3.value;
	}else if(frm.p_email3.value.length == 0){
		frm.p_email2.value = "";
	}
}

function radioChange(val){
	if(frm.addressChange.checked){
		if(val == "C"){
			$("#c_mpost1").val(frm.p_mpost1.value);
			$("#c_mpost2").val(frm.p_mpost2.value);
			$("#c_maddress1").val(frm.p_maddress1.value);
			$("#c_maddress2").val(frm.p_maddress2.value);
		}else{
			$("#c_mpost1").val(frm.p_post1.value);
			$("#c_mpost2").val(frm.p_post2.value);
			$("#c_maddress1").val(frm.p_address1.value);
			$("#c_maddress2").val(frm.p_address2.value);
		}
	}else{

	}
}

function addChange(i){
	//alert($("#p_hrdc2_1").val());
	if(i == "1"){
		if(frm.addressChange1.checked){
			//alert(frm.p_hrdc1[0].value);
			if(frm.p_hrdc1[0].checked == true){
				$("#c_mpost1").val(frm.p_mpost1.value);
				$("#c_mpost2").val(frm.p_mpost2.value);
				$("#c_maddress1").val(frm.p_maddress1.value);
				$("#c_maddress2").val(frm.p_maddress2.value);
			}else{
				$("#c_mpost1").val(frm.p_post1.value);
				$("#c_mpost2").val(frm.p_post2.value);
				$("#c_maddress1").val(frm.p_address1.value);
				$("#c_maddress2").val(frm.p_address2.value);
			}
			$("#addressC1").show();
		}else{
			$("#c_mpost1").val("");
			$("#c_mpost2").val("");
			$("#c_maddress1").val("");
			$("#c_maddress2").val("");
			$("#addressC1").hide();
		}
	}else if(i == "2"){
		if(frm.addressChange2.checked){
			if(frm.p_hrdc3[0].checked == true){
				$("#c_mpost21").val(frm.p_mpost21.value);
				$("#c_mpost22").val(frm.p_mpost22.value);
				$("#c_maddress21").val(frm.p_maddress21.value);
				$("#c_maddress22").val(frm.p_maddress22.value);
			}else{
				$("#c_mpost21").val(frm.p_post1.value);
				$("#c_mpost22").val(frm.p_post2.value);
				$("#c_maddress21").val(frm.p_address1.value);
				$("#c_maddress22").val(frm.p_address2.value);
			}
			$("#addressC2").show();
		}else{
			$("#c_mpost21").val("");
			$("#c_mpost22").val("");
			$("#c_maddress21").val("");
			$("#c_maddress22").val("");
			$("#addressC2").hide();
		}
	}else{
		if(frm.addressChange3.checked){
			if(frm.p_hrdc2[0].checked == true){
				$("#c_spost1").val(frm.p_spost1.value);
				$("#c_spost2").val(frm.p_spost2.value);
				$("#c_saddress1").val(frm.p_saddress1.value);
				$("#c_saddress2").val(frm.p_saddress2.value);
			}else{
				$("#c_spost1").val(frm.p_post1.value);
				$("#c_spost2").val(frm.p_post2.value);
				$("#c_saddress1").val(frm.p_address1.value);
				$("#c_saddress2").val(frm.p_address2.value);
			}
			$("#addressC3").show();
		}else{
			$("#c_spost1").val("");
			$("#c_spost2").val("");
			$("#c_saddress1").val("");
			$("#c_saddress2").val("");
			$("#addressC3").hide();
		}
	}

}

function existPersonalNum(){

	var nicepNum = frm.p_nicePersonalNum.value;


	var nicepNum1 = nicepNum.substring(3,4);
	//alert("nicepNum1 : " + nicepNum1);

	 var param = "p_nicePersonalNum=" + frm.p_nicePersonalNum.value;

	 if(frm.p_emp_gubun[0].checked == true){
	 	if(nicepNum1 != '1'){
			alert("회원구분 교원과 일치 하지 않는 나이스 번호입니다.");
			frm.p_nicePersonalNum.focus();
			return;
	 	}
	 }

	 if(frm.p_nicePersonalNum.value == '') {
			alert("나이스 개인번호를 입력하세요.");
			frm.p_nicePersonalNum.focus();
			return;
	}

	 if(frm.p_nicePersonalNum2.value == '') {
			alert("나이스 개인번호를 입력하세요.");
			frm.p_nicePersonalNum2.focus();
			return;
	}

	 if(frm.p_nicePersonalNum.value != frm.p_nicePersonalNum2.value) {
			alert("나이스 개인번호가 일치하지 않습니다.");
			return;
	}

	 $.ajax({
		  type:"post",
		  url : "/com/aja/mem/nicePersonalNumOverlap.do",
		  data: param,
		  success:function(data){
			result = data.result;
			if(result == true)
			{
				alert("이미 중복된 개인번호가 있습니다. 다시 확인 후 입력해주세요.");
				frm.p_overlap.value = 'N';
			}
			else
			{
				alert("중복된 개인번호가 없습니다.");
				frm.p_overlap.value = 'Y';
			}
		  },
		  error:function(e){
		   alert(e);
		  }
		 });
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

	//d_type
	frm.p_d_type.value = "T";

	if ( flag && !frm.p_userid.value ) {
		alert("아이디를 입력하세요.");
		frm.p_userid.focus();
		flag = false;
	}

	if ( flag && !frm.p_userid_temp.value ) {
		alert("아이디 중복확인을 하세요.");
		frm.p_userid.focus();
		flag = false;
	}




	if ( flag && !frm.p_pwd.value ) {
		alert("비밀번호를 입력하세요.");
		frm.p_pwd.focus();
		flag = false;
	}

	if ( flag && !frm.p_pwd2.value ) {
		alert("확인을 위해 비밀번호를 한번 더 입력하세요.");
		frm.p_pwd2.focus();
		flag = false;
	}

	if ( flag && frm.p_pwd.value != frm.p_pwd2.value ) {
		alert("입력하신 비밀번호가 일치하지 않습니다.");
		frm.p_pwd2.focus();
		flag = false;
	}
    if( flag && frm.p_pwd.value.length < 8 ) {
		alert("비밀번호는 최소 8자리 이상 입력해주세요.");
		frm.p_pwd.focus();
		flag = false;
	}

   	var str = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	var chr = '~!@#$%^&*()-_=+';

	var res_1 = 0; var res_2 = 0;

	for(var i=0; i<=frm.p_pwd.value.length-3; i++) {
		if( flag && (frm.p_pwd.value.charAt(i)==frm.p_pwd.value.charAt(i+1) && frm.p_pwd.value.charAt(i)==frm.p_pwd.value.charAt(i+2)) ) {
			alert("같은 문자열을 3개 이상 연속으로 사용할 수 없습니다.");
			frm.p_pwd.focus();
			flag = false;
		}
	}


	for(var x=0; x<frm.p_pwd.value.length; x++) {
		for(var y=0; y<str.length; y++) {
			if(frm.p_pwd.value.charAt(x)==str.charAt(y)) res_1++;
		}

		for(var z=0; z<chr.length; z++) {
			if(frm.p_pwd.value.charAt(x)==chr.charAt(z)) res_2++;
		}
	}
	if( flag && res_1 < 2) {
		alert("문자는 최소 2개 이상 입력하세요.");
		frm.p_pwd.focus();
		flag = false;
	}


	if( flag && res_2 < 1) {
		alert("옆과 같은 특수문자를 1개 이상 입력하셔야 합니다. : "+chr);
		frm.p_pwd.focus();
		flag = false;
	}


    if ( flag && !frm.p_email1.value ) {
		alert("전자우편을 입력하세요.");
		frm.p_email1.focus();
		flag = false;
	}
	if ( flag && !frm.p_email2.value ) {
		alert("전자우편을 입력하세요.");
		frm.p_email2.focus();
		flag = false;
	}



	if ( flag && !frm.p_address1.value ) {
		alert("우편번호 찾기를 이용해 주소를 입력하세요.");
		frm.p_address1.focus();
		flag = false;
	}

	if ( flag && !frm.p_address2.value ) {
		alert("상세주소를 입력하세요.");
		frm.p_address2.focus();
		flag = false;
	}



	/*
	if ( flag && (!frm.p_hometel.value && !frm.p_comptel.value && !frm.p_handphone.value) ) {
		alert("집전화, 휴대전화, 회사전화 중 한가지는 필수 입력사항입니다.");
		frm.p_hometel.focus();
		flag = false;
	}

	if( flag && frm.p_hometel.value && isNaN( frm.p_hometel.value.replace(/-/g, '')) ){
		alert( '집전화번호는  숫자만 입력 가능합니다.' );
		frm.p_hometel.focus();
		flag = false;
	}
	if ( flag && !frm.p_hometel.value ) {
		alert("집전화번호를 입력하세요.")
		frm.p_hometel.focus();
		flag = false;
	}
	*/

	if( flag && frm.p_hometel1.value && isNaN( frm.p_hometel1.value.replace(/-/g, '')) ){
		alert( "집전화번호는  숫자만 입력 가능합니다." );
		frm.p_hometel1.focus();
		flag = false;
	}
	if( flag && frm.p_hometel2.value && isNaN( frm.p_hometel2.value.replace(/-/g, '')) ){
		alert( "집전화번호는  숫자만 입력 가능합니다." );
		frm.p_hometel2.focus();
		flag = false;
	}
	if( flag && frm.p_hometel3.value && isNaN( frm.p_hometel3.value.replace(/-/g, '')) ){
		alert( "집전화번호는  숫자만 입력 가능합니다." );
		frm.p_hometel3.focus();
		flag = false;
	}


	if( flag && frm.p_handphone1.value && isNaN( frm.p_handphone1.value.replace(/-/g, '') ) ){
		alert( "휴대전화번호는 숫자만 입력 가능합니다." );
		frm.p_handphone1.focus();
		flag = false;
	}
	if( flag && frm.p_handphone2.value && isNaN( frm.p_handphone2.value.replace(/-/g, '') ) ){
		alert( "휴대전화번호는 숫자만 입력 가능합니다." );
		frm.p_handphone2.focus();
		flag = false;
	}
	if( flag && frm.p_handphone3.value && isNaN( frm.p_handphone3.value.replace(/-/g, '') ) ){
		alert( "휴대전화번호는 숫자만 입력 가능합니다." );
		frm.p_handphone3.focus();
		flag = false;
	}
	if( flag && (frm.p_hometel1.value == "" || frm.p_hometel2.value == "" || frm.p_hometel3.value == "") ){
		alert( "휴대전화번호를 입력하세요." );
		frm.p_hometel1.focus();
		flag = false;
	}


	if(frm.p_emp_gubun[0].checked==true){
		if( flag && frm.p_job_cd.value==''){
    		alert("교사자격등급을  선택하세요.");
    		frm.p_job_cd.focus();
    		flag = false;
    	}
		
		//사립유치원교원 아니면
		if(frm.p_job_cd.value != '00039'){				
			//나이스개인번호 시작
			var reg = /^[A-Za-z]/i;
			var reg1 = /[A-Za-z0-9]/;
			var reg2 = /^[0-9]$/i;
			var niceNum = frm.p_nicePersonalNum.value;
	
			if (flag && frm.p_nicePersonalNum.value == '') {
				alert("나이스 개인번호를 입력하세요.");
				frm.p_nicePersonalNum.focus();
				return false;
			}
	
			if(flag && !reg.test(niceNum)){
				alert("나이스 개인번호 첫 글자는 영문이고, 나머지는 숫자입니다.");
				frm.p_nicePersonalNum.focus();
				return false;
			}
	
			if(flag && !reg1.test(niceNum)){
				alert("나이스 개인번호는 영문과 숫자만 허용됩니다.");
				frm.p_nicePersonalNum.focus();
				return false;
			}
	
			if(flag && niceNum.length < 10){
				alert("나이스 개인번호는 10자 입니다. 확인 후 다시 입력하세요.");
				frm.p_nicePersonalNum.focus();
				return false;
			}
	
			for(var i = 0; i < niceNum.length; i++){
				if(i > 0){
					if(!reg2.test(niceNum.charAt(i))){
						alert("첫글자만 영문이고, 나머지는 숫자만 입력 가능합니다.");
						return false;
					}
	
				}
			}
	
			if ( flag && frm.p_overlap.value == 'N' ) {
				//alert("나이스 개인번호 중복확인을 해주세요.");
				alert("나이스개인번호 검증을 다시 해주세요.");
				//frm.p_overlap.value = 'Y';
				flag = false;
			}
	
			if(frm.p_nicePersonalNum.value != frm.p_nicePersonalNum2.value) {
				alert("나이스 개인번호가 일치하지 않습니다.");
				return;
			}
			//나이스개인번호 끝
		}
	
		//var selectedVal = document.getElementById('p_job_cd').options[document.getElementById('p_job_cd').selectedIndex].value;
    	if( flag && frm.p_job_cd.value=='')
    	{
    		if ( flag && !frm.p_maddress1.value ) {
	    		alert("우편번호 찾기를 이용해 주소를 입력하세요.");
	    		frm.p_maddress1.focus();
	    		flag = false;
			}

			if ( flag && !frm.p_maddress2.value ) {
	    		alert("상세주소를 입력하세요.");
	    		frm.p_maddress2.focus();
	    		flag = false;
			}
    	}
    	else
    	{
	    	if( flag && frm.p_user_path.value == ''){
	    		alert("학교명을 검색하셔서 입력하세요.");
	    		frm.p_user_path.focus();
	    		flag = false;
	    	}
	    	if( flag && !frm.p_handphone_no1.value){
	    		alert("학교 연락처를 입력하세요.");
	    		frm.p_handphone_no1.focus();
	    		flag = false;
	    	}

	    	if ( flag && frm.p_career.value == '' ) {
				alert("경력을 선택하세요.");
				frm.p_career.focus();
				flag = false;
			}

	    	if ( flag && frm.p_subject.value == '' ) {
				alert("담당교과를 입력하세요.");
				frm.p_subject.focus();
				flag = false;
			}

	    	if ( flag && !frm.p_maddress1.value ) {
	    		alert("우편번호 찾기를 이용해 주소를 입력하세요.");
	    		frm.p_maddress1.focus();
	    		flag = false;
			}

			if ( flag && !frm.p_maddress2.value ) {
	    		alert("상세주소를 입력하세요.");
	    		frm.p_maddress2.focus();
	    		flag = false;
			}

    	}


		//if(frm.p_hrdc1[0].checked==true){
		//	frm.p_hrdc.value = "C";
		//}else{
		//	frm.p_hrdc.value = "H";
		//}

//		frm.p_emp_gubun.value = "T";
	}else if(frm.p_emp_gubun[1].checked==true){
		if( flag && frm.p_job_cd2.value==''){
    		alert("교사자격등급을  선택하세요.");
    		frm.p_job_cd2.focus();
    		flag = false;
    	}


    	if( frm.p_job_cd2.value=='0' )
    	{
    		if ( flag && !frm.p_maddress21.value ) {
	    		alert("우편번호 찾기를 이용해 주소를 입력하세요.");
	    		frm.p_maddress21.focus();
	    		flag = false;
			}

			if ( flag && !frm.p_maddress22.value ) {
	    		alert("상세주소를 입력하세요.");
	    		frm.p_maddress22.focus();
	    		flag = false;
			}
    	}
    	else
    	{
	    	if( flag && !frm.p_user_path2.value){
	    		alert("학교명을 검색하셔서 입력하세요.");
	    		frm.p_user_path2.focus();
	    		flag = false;
	    	}
	    	if( flag && !frm.p_handphone_no3.value){
	    		alert("학교 연락처를 입력하세요.");
	    		frm.p_handphone_no3.focus();
	    		flag = false;
	    	}


	    	if ( flag && !frm.p_maddress21.value ) {
	    		alert("우편번호 찾기를 이용해 주소를 입력하세요.");
	    		frm.p_maddress21.focus();
	    		flag = false;
			}

			if ( flag && !frm.p_maddress22.value ) {
	    		alert("상세주소를 입력하세요.");
	    		frm.p_maddress22.focus();
	    		flag = false;
			}

    	}


		//if(frm.p_hrdc3[0].checked==true){
		//	frm.p_hrdc.value = "C";
		//}else{
		//	frm.p_hrdc.value = "H";
		//}

//		frm.p_emp_gubun.value = "T";
	}else{

		if( flag && !frm.p_handphone_no21.value){
    		alert("연락처를 입력하세요.");
    		frm.p_handphone_no21.focus();
    		flag = false;
    	}
		if( flag && !frm.p_handphone_no22.value){
    		alert("연락처를 입력하세요.");
    		frm.p_handphone_no22.focus();
    		flag = false;
    	}
		if( flag && !frm.p_handphone_no23.value){
    		alert("연락처를 입력하세요.");
    		frm.p_handphone_no23.focus();
    		flag = false;
    	}

    	if ( flag && !frm.p_saddress1.value ) {
    		//alert("우편번호 찾기를 이용해 주소를 입력하세요.");
    		//frm.p_saddress1.focus();
    		//flag = false;
		}

		if ( flag && !frm.p_saddress2.value ) {
    		//alert("상세주소를 입력하세요.");
    		//frm.p_saddress2.focus();
    		//flag = false;
		}
		
		if('O' == $('input[name=p_emp_gubun]:checked').val()) {
			if ( flag && frm.p_overlap.value == 'N' ) {
				alert("나이스개인번호 검증을 다시 해주세요.");
				flag = false;
			}
	
			if(frm.p_nicePersonalNum.value != frm.p_nicePersonalNum2.value) {
				alert("나이스 개인번호가 일치하지 않습니다.");
				return;
			}
		}
		//if(frm.p_hrdc2[0].checked==true){
		//	frm.p_hrdc.value = "C";
		//}else{
		//	frm.p_hrdc.value = "H";
		//}
		//frm.p_emp_gubun.value = "P";
	}

	if ( flag ) {
		frm.p_hometel.value 				= frm.p_hometel1.value 			+ '-' + frm.p_hometel2.value 				+ '-' + frm.p_hometel3.value;
    	frm.p_handphone.value 			= frm.p_handphone1.value 		+ '-' + frm.p_handphone2.value 			+ '-' + frm.p_handphone3.value;
    	//frm.p_handphone_no1.value 	= frm.p_handphone_no11.value 	+ '-' + frm.p_handphone_no12.value 	+ '-' + frm.p_handphone_no13.value;
		frm.p_handphone_no2.value 	= frm.p_handphone_no21.value 	+ '-' + frm.p_handphone_no22.value 	+ '-' + frm.p_handphone_no23.value;
		//frm.p_handphone_no3.value 	= frm.p_handphone_no31.value 	+ '-' + frm.p_handphone_no32.value 	+ '-' + frm.p_handphone_no33.value;


		var c_url   = this.location+"";
        var url = "https://iedu.nise.go.kr/usr/mem/memJoinAction.do";
		if(c_url.match("localhost")){
			url = "/usr/mem/memJoinAction.do";
		}

		//frm.action = "https://iedu.nise.go.kr/usr/mem/memJoinAction.do";
		//frm.action = "/usr/mem/memJoinAction.do";


		frm.action = url;
		frm.p_process.value = "insert";
		frm.target	= "_self";
		frm.submit();
	}
}

//교육청 검색
function education_Office(){
	frm.p_schoolgubun.value = 3;
	window.open('', 'education_OfficePop', 'width=550,height=600');
	frm.action = "/usr/mem/searchEducationOfficePop.do";
	frm.target = "education_OfficePop";
	frm.submit();
}

document.title="회원가입(회원정보입력) :회원가입";

$(function(){ 
	$("#p_job_cd").change(function () {   
		var str = "";   
		$("#p_job_cd option:selected").each(function () {   
			//str += $(this).text() + " ";   
			str += $(this).val();
		});   
		//$("div").text(str);
		
		//사립유치원교원
		if(str == '00039'){			
			$("#tr_nicePersonalNum").hide();			
			$("#tr_nicePersonalNum2").hide();
		}else{
			$("#tr_nicePersonalNum").show();
			$("#tr_nicePersonalNum2").show();
		}
	}); 
}) 
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
                if(1 == p_zipgubun) {
	                document.getElementById('p_post1').value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById('p_address1').value = fullAddr;
	
	                document.getElementById('p_address2').focus();
                } else if(2 == p_zipgubun) {
                	document.getElementById('p_mpost1').value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById('p_maddress1').value = fullAddr;
	
	                document.getElementById('p_maddress2').focus();
                } else if(4 == p_zipgubun) {
                	document.getElementById('p_mpost21').value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById('p_maddress21').value = fullAddr;
	
	                document.getElementById('p_maddress22').focus();
                } else if(3 == p_zipgubun) {
                	document.getElementById('p_spost1').value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById('p_saddress1').value = fullAddr;
	
	                document.getElementById('p_saddress2').focus();
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
    	// 우편번호
    	var zipCode = '';
    	if('T' == empGubun) {
    		zipCode = $('input[name=p_mpost1]').val();
    	} else if('E' == empGubun) {
    		zipCode = $('input[name=p_mpost21]').val();
    	} else {
    		zipCode = $('input[name=p_spost1]').val();
    	}
    	// valid
		if('' == nicePersonalNum || 10 != nicePersonalNum.length) {
			alert('나이스개인번호 10자리를 입력해 주세요.');
    		return;
    	}
    	if('T' == empGubun && '' == deptCd) {
    		alert('회원구분이 교원일 경우 시도교육청을 선택해주세요.');
    		return;
    	}
		if('' == zipCode) {
			alert('우편번호를 등록해주세요.');
    		return;
    	}
    	
    	$.ajax({
    		url: '${pageContext.request.contextPath}/valid/nicePersonalNum.do'
    		, type: 'post'
    		, data: {
    			deptCd: deptCd
    			, empGubun: empGubun
    			, nicePersonalNum: nicePersonalNum
    			, zipCode: zipCode
    		}
    		, dataType: 'json'
    		, success: function(result) {
    			alert(result.resultMsg);
    			if(result.resultCode == 1) {
    				frm.p_overlap.value = 'Y';
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
</script>