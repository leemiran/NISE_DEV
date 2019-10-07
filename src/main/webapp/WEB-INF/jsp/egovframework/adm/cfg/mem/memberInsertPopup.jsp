<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>회원등록</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" name="p_schoolgubun" id="p_schoolgubun" value="1" />
		<!-- contents -->
		<div class="popCon">
			<font color="red">(*)로 표시된 내용들을 회원정보 등록을 위해 꼭 필요한 내용들입니다. 빠짐없이 채워주시기 바랍니다.</font>
			
			<table summary="" class="popTb">
				<colgroup>
					<col width="15%" />
					<col width="35%" />
					<col width="15%" />
					<col width="35%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">아이디<font color="red">(*)</font></th>
						<td class="bold" colspan="3">
							<input type="text" id="p_userid" name="p_userid" class="ipt" style="width:20%;IME-MODE:disabled" onfocus="this.select()" onkeypress="fn_keyEvent('existID')"/>
							<input type="hidden" id="p_userid_temp" name="p_userid_temp" />
							<a href="#none" class="pop_btn01" onclick="javascript:existID()"><span>아이디 중복확인</span></a>
						</td>
					</tr>
					<tr>
						<th scope="col">비밀번호<font color="red">(*)</font></th>
						<td class="bold">
							<input type="password" id="p_pwd" name="p_pwd" class="ipt" style="width:50%;IME-MODE:disabled" maxlength="18" onfocus="this.select()"/>
						</td>
						<th scope="col">비밀번호확인<font color="red">(*)</font></th>
						<td class="bold">
							<input type="password" id="p_pwd2" name="p_pwd2" class="ipt" style="width:50%;IME-MODE:disabled" maxlength="18" onfocus="this.select()"/>
						</td>
					</tr>
					<tr>
						<th scope="col">성명<font color="red">(*)</font></th>
						<td class="bold"><input type="text" name="p_name"></td>
						<th scope="col">생년월일<font color="red">(*)</font></th>
						<td class="bold"><input type="text" name="p_birthDate"></td>
					</tr>
					<tr>
						<th scope="col">휴대전화번호<font color="red">(*)</font></th>
						<td class="bold" colspan="3">
							<input type="text" id="p_handphone" name="p_handphone" class="ipt" style="width:20%;IME-MODE:disabled" onfocus="this.select()"/>
							<br/>
							<input type="checkbox" name="p_issms" id="p_issms" class="chkbox" value="Y" checked /><label for="p_issms"> SMS(문자메세지) 수신동의</label>
						</td>
					</tr>
					<tr>
						<th scope="col">E-mail<font color="red">(*)</font></th>
						<td class="bold" colspan="3">
							<input type="text" id="p_email1" name="p_email1" class="ipt" style="width:20%;IME-MODE:disabled" onfocus="this.select()"/>
							@
							<input type="text" id="p_email2" name="p_email2" class="ipt" style="width:20%;IME-MODE:disabled" onfocus="this.select()"/>
							<ui:code id="p_email3" selectItem="${p_email3}" gubun="" codetype="0067"  upper="" title="" className="" type="select" selectTitle="직접입력" event="changeMail" />
							<br/>
							<input type="checkbox" name="p_email_get" id="p_email_get" value="Y" style="border:0;"><label for="p_email_get"> E-mail 수신동의</label>
						</td>
					</tr>
					<tr>
						<th scope="col">주소<font color="red">(*)</font></th>
						<td class="bold">
							<input type="text" name="p_post1" size="5" maxlength="3" readonly> <a href="#none" onclick="sample6_execDaumPostcode(1)"><img src="/images/adm/btn/btn_post.gif" /></a>
							<input type="text" name="p_addr" size="50">
						</td>
						<th scope="col">집전화번호</th>
						<td class="bold"><input type="text" name="p_hometel" size="25"></td>
					</tr>
					<tr>
						<th scope="col">장애 여부</th>
						<td class="bold">
							<select name="p_handicap_yn" class="inputpsearch">
								<option value="N">아니오</option>
								<option value="Y">예</option>
							</select>
						</td>
						<th scope="col"></th>
						<td class="bold"></td>
					</tr>
				</tbody>
			</table>
			
			<table summary="" class="popTb">
				<colgroup>
					<col width="15%" />
					<col width="35%" />
					<col width="15%" />
					<col width="35%" />
				</colgroup>
				<tbody>
					<tr class="T E R P O">
						<th scope="col">회원구분</th>
						<td class="bold"><select name="p_emp_gubun">
								<option value="T">교원</option>
								<option value="E">보조인력</option>
								<option value="R">교육 전문직</option>
								<option value="P" selected>일반회원(학부모 등)</option>
								<option value="O">공무원</option>
						</select></td>
						<th scope="col">근무지 주소</th>
						<td class="bold">
                        	<input type="text" name="p_spost1" size="5" maxlength="5" readonly> <a href="#none" onclick="sample6_execDaumPostcode(2)"><img src="/images/adm/btn/btn_post.gif" /></a>
							<input type="text" name="p_saddr" size="50">
                        </td>
					</tr>
					<tr class="T O"><!-- TO -->
						<th scope="col">나이스개인번호</th>
						<td class="bold" colspan="3"><input type="text" name="p_nicePersonalNum"></td>
					</tr>
					<tr class="T"><!-- T -->
						<th scope="col">교원 나이스개인번호 <br />제외</th>
						<td class="bold">
							<select name="p_niceNumAllowYn" class="inputpsearch">
								<option value="N">허용안함</option>
								<option value="Y">허용</option>
							</select>
							<br />(허용시 사용자가 재로그인을 해야 적용됩니다.)
						</td>
						<th scope="col">교원자격등급</th>
						<td class="bold">
							<select name="p_job_cd" id="p_job_cd" title="교원자격등급">
                      			<option value="">선택</option>
								<c:forEach items="${list}" var="result" varStatus="i">
									<option value="${result.code }">${result.codenm }</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr class="T E"><!-- TE -->
						<th scope="col">학교명</th>
						<td class="bold">
							<input type="text" name="p_user_path" id="p_user_path" readonly size="20"/>
							<a href="#" class="btn_search" onclick="searchSchool(1);return false;" title="학교검색" style="cursor:hand"><span>검색</span></a>
						</td>
						<th scope="col">학교 연락처</th>
						<td class="bold"><input type="text" name="p_handphone_no" id="p_handphone_no" size="14"/></td>
					</tr>
					<tr class="T E R"><!-- TER -->
						<th scope="col">상위교육청</th>
						<td class="bold">
							<input type="text" name="p_dept_cd_nm" id="p_dept_cd_nm" readonly size="20"/>
							<input type="hidden" name="p_dept_cd" id="p_dept_cd"/>
							<a id="eduSchBtn" href="#" class="btn_search" onclick="education_Office();return false;" title="교육청검색" style="cursor:hand"><span>교육청 검색</span></a>
						</td>
						<th scope="col">하위교육청</th>
						<td class="bold">
							<input type="text" name="p_agency_cd_nm" id="p_agency_cd_nm" readonly size="20">
							<input type="hidden" name="p_agency_cd" id="p_agency_cd"/>
						</td>
					</tr>
					<tr class="T"><!-- T -->
						<th scope="col">경력</th>
						<td class="bold">
							<ui:code id="p_career" codetype="0116" levels="1" title="경력" type="select" selectTitle="선택"/>
						</td>
						<th scope="col">담당교과</th>
						<td class="bold"><input type="text" name="p_subject" id="p_subject" maxlength="30" title="담당교과"></td>
					</tr>
					<tr class="R P O"><!-- RPO -->
						<th scope="col">소속</th>
						<td class="bold"><input type="text" name="p_position_nm" size="20" id="p_position_nm"></td>
						<th scope="col">직장 연락처</th>
						<td class="bold"><input type="text" name="p_handphone_no" id="p_handphone_no" size="14"/></td>
					</tr>
					<tr class="R P O"><!-- RPO -->
						<th scope="col">부서명</th>
						<td class="bold"><input type="text" name="p_division_nm"></td>
						<th scope="col">직급</th>
						<td class="bold"><input type="text" name="p_post_nm"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:insertMemberData()"><span>등록</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">
	var frm = eval('document.<c:out value="${gsPopForm}"/>');

	function insertMemberData(){
		if (frm.p_userid.value == "") {
	        alert("아이디를 입력하세요.");
	        frm.p_userid.focus();
	        return;
	    }
	    //2008.10.06 오충현 추가
	    if (frm.p_userid.value.indexOf(" ") > -1){
				alert("아이디에 공백이 존재합니다.");
				frm.p_userid.focus();
				return;
		}

	    if ( frm.p_userid_temp.value != frm.p_userid.value ) {
			alert("아이디 중복확인을 하세요.");
			frm.p_userid.focus();
			return;
		}
		
		<%--패스워드 정책  : S --%>
		if (  !frm.p_pwd.value ) {
			alert("패스워드를 입력하세요.");
			frm.p_pwd.focus();
			return;
		}
		
		<%--패스워드 정책 : E --%>
		if (frm.p_pwd.value.indexOf(" ") > -1){
			alert("비밀번호에 공백이 존재합니다.");
			frm.p_pwd.focus();
			return false;
		}
		
		if (  !frm.p_pwd2.value ) {
			alert("확인을 위해 패스워드를 한번 더 입력하세요.");
			frm.p_pwd2.focus();
			return;
		}
		
		if (  frm.p_pwd.value != frm.p_pwd2.value ) {
			alert("입력하신 패스워드가 일치하지 않습니다.");
			frm.p_pwd2.focus();
			return;
		}


		if (frm.p_name.value == "") {
		    alert("이름을 입력하세요.");
		    frm.p_name.focus();
		    return;
		}
		
		if (frm.p_birthDate.value == "") {
		    alert("생년월일을 입력하세요.");
		    frm.p_birthDate.focus();
		    return;
		}
		
		if (frm.p_email1.value == "") {
		    alert("이메일을 입력하세요.");
		    frm.p_email1.focus();
		    return;
		}
		if (frm.p_email2.value == "") {
		    alert("이메일을 입력하세요.");
		    frm.p_email2.focus();
		    return;
		}
		
		if (frm.p_post1.value == "") {
		    alert("우편번호를 입력하세요.");
		    frm.p_post1.focus();
		    return;
		}
		
		if (frm.p_addr.value == "") {
		    alert("주소를 입력하세요.");
		    frm.p_addr.focus();
		    return;
		}
		if (frm.p_saddr.value == "") {
		    alert("주소를 입력하세요.");
		    frm.p_saddr.focus();
		    return;
		}
	    
	    

		if( frm.p_hometel.value && parseInt(frm.p_hometel.value.replace(/-/g, '')) * 0 != 0  ) {
	   		alert( "집전화번호는  숫자와 '-'만 입력 가능합니다." );
	   		document.frmJoin.p_hometel.focus();
	   		return;
	   	}
		
		if (frm.p_handphone.value == "") {
		    alert("휴대전화를 입력하세요.");
		    frm.p_handphone.focus();
		    return;
		}
		
	   	if( frm.p_handphone.value && parseInt(frm.p_handphone.value.replace(/-/g, '')) * 0 != 0 ) {
	   		alert( "휴대전화번호는  숫자와 '-'만 입력 가능합니다." );
	   		frm.p_handphone.focus();
	   		return;
	   	}	
	   	
	   	
	   	
		
		frm.target = "_self";
		frm.action = "/adm/cfg/mem/memberInsertData.do";
		frm.submit();
	}

	function changeMail(){
		if(frm.p_email3.selectedIndex > 0 ){
			frm.p_email2.value = frm.p_email3.value;
			frm.p_email2.readOnly = true;
		}else{
			frm.p_email2.value = "";
			frm.p_email2.readOnly = false;
		}
	}

	function existID(){
		if( frm.p_userid.value == "" ){
			alert("아이디를 입력하세요.");
			frm.p_userid.focus();
			return;
		}
		window.open('', 'existIdPop', 'width=300,height=200');
		frm.action = "/adm/cfg/mem/existIdPopup.do";
		frm.target = "existIdPop";
		frm.submit();
	}

	function searchZipcode(){
		window.open('', 'zipcode', 'width=500,height=600');
		frm.action = "/com/pop/searchZipcodePopup.do";
		frm.target = "zipcode";
		frm.submit();
	}

	function receiveZipcode(arr){
		frm.p_post1.value = arr[0];
		frm.p_post2.value = arr[1];
		frm.p_addr.value = arr[2];
	}

	$(function() {
		empGubunChange('P');
		
		$('select[name=p_emp_gubun]').change(function() {
			empGubunChange($(this).val());
		});
	});

	function empGubunChange(empGugun) {
		var empGubunClass = '.' + empGugun;
		$(empGubunClass).siblings().not(empGubunClass).hide();
	    $(empGubunClass).show();
	    
	    $(empGubunClass).siblings().not(empGubunClass).find('input,select').attr('disabled', 'disabled');
	    $(empGubunClass).find('input,select').attr('disabled', '');
	    
		// 교육청 검색버튼은 교육전문직일 경우에만
	    if('R' == empGugun) {
	    	$('#eduSchBtn').show();
	    } else {
	    	$('#eduSchBtn').hide();
	    }
	}

	function searchSchool(p_schoolgubun) {
		frm.p_schoolgubun.value = p_schoolgubun;
		window.open('', 'schoolWindowPop', 'width=750,height=650');
		frm.action = "/usr/mem/searchSchoolPop.do";
		frm.target = "schoolWindowPop";
		frm.submit();
	}

	function receiveSchool(arr) {
		var p_schoolgubun = frm.p_schoolgubun.value;

		if(p_schoolgubun == 1)
		{
			frm.p_user_path.value = arr[0];
			frm.p_dept_cd_nm.value = arr[1];
			frm.p_agency_cd_nm.value = arr[2];
			if(arr[3] != null && arr[3] != '') 
				frm.p_handphone_no.value = arr[3];
			frm.p_dept_cd.value = arr[4];
			frm.p_agency_cd.value = arr[5];
		}

		//교육청
		if(p_schoolgubun == 3){
			frm.p_dept_cd.value = arr[0];
			frm.p_agency_cd.value = arr[1];
			frm.p_dept_cd_nm.value = arr[2];
			frm.p_agency_cd_nm.value = arr[3];
		}
	}

	//교육청 검색
	function education_Office() {
		frm.p_schoolgubun.value = 3;
		window.open('', 'education_OfficePop', 'width=550,height=500');
		frm.action = "/usr/mem/searchEducationOfficePop.do";
		frm.target = "education_OfficePop";
		frm.submit();
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
                	frm.p_post1.value = data.zonecode; //5자리 새우편번호 사용
                	frm.p_addr.value = fullAddr;
                } else if(2 == p_zipgubun) {
                	frm.p_spost1.value = data.zonecode; //5자리 새우편번호 사용
                	frm.p_saddr.value = fullAddr;
                }
            }
        }).open();
    }
</script>