<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp"%>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<script language="VBScript">
Function exec1(url, chk)
	Set objShell = CreateObject("WScript.Shell")
	objShell.Run "IExplore.exe " + chk + url
End Function
</script>


<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
	<input type="hidden" name="p_subj"> <input type="hidden" name="p_year"> <input type="hidden" name="p_subjseq"> <input type="hidden" value="${p_userid}" name="p_userid"> <input type="hidden" value="" name="p_process">
	<!--성적보기시 사용자의 아이디와 충돌을 피하기 위하여 넣어주어야 한다. p_userid = userid의 업데이트를 막기위하여 넣어주어야한다.-->
	<input type="hidden" value="Y" name="p_isAdmin"> <input type="hidden" value="${view.hrdc}" name="p_hrdc"> <input type="hidden" name="p_zipgubun" id="p_zipgubun" value="1" />
	<input type="hidden" name="p_schoolgubun" id="p_schoolgubun" value="1" />


	<div id="popwrapper">
		<div class="popIn">
			<div class="tit_bg">
				<h2>개인별 학습현황</h2>
			</div>
			<!-- contents -->

			<!-- 기본정보 -->
			<div class="listTop" style="margin: 20px 10px 10px 10px;">
				<!-- tab -->
				<div class="conwrap2" style="margin-bottom: 5px;">
					<ul class="mtab2">
						<li><a href="#none" class="on">기본정보</a></li>
					</ul>
				</div>
				<!-- // tab-->

				<div class="btnR MR05">
					<a href="#none" onClick="whenMemberSave()" class="btn01"><span>저장</span></a>
				</div>
				<div class="btnR MR05">
					<a href="#none" onClick="whenCommonSmsSend(<c:out value="${gsPopForm}"/>, 'p_userid')" class="btn01"><span>SMS</span></a>
				</div>
				<div class="btnR MR05">
					<a href="#none" onClick="whenCommonMailSend(<c:out value="${gsPopForm}"/>, 'p_userid')" class="btn01"><span>메일발송</span></a>
				</div>
			</div>

			<div class="popCon" style="margin-right: 20px;">
				<table summary="" width="100%" class="popTb">
					<colgroup>
						<col width="15%" />
						<col width="35%" />
						<col width="15%" />
						<col width="35%" />
					</colgroup>
					<tbody>
						<tr>
							<th scope="col">아이디</th>
							<td class="bold"><a href="javascript:whenConnection('${view.userid}', '${view.pwd}')"><c:out value="${view.userid}" /></a></td>
							<th scope="col">성명</th>
							<td class="bold"><input type="text" value="${view.name}" name="p_name"></td>
						</tr>
						<tr>
							<th scope="col">생년월일</th>
							<td class="bold"><input type="text" value="${view.birthDate}" name="p_birth_date"></td>
							<th scope="col">가입/탈퇴</th>
							<td class="bold"><select name="p_isretire" class="inputpsearch">
									<option value='Y' <c:if test="${view.isretire eq 'Y'}">selected</c:if>>탈퇴</option>
									<option value='N' <c:if test="${view.isretire eq 'N'}">selected</c:if>>가입</option>
							</select></td>
						</tr>
						<tr>
							<th scope="col">집전화번호</th>
							<td class="bold"><input type="text" name="p_hometel" value="${view.hometel}" size="25"></td>
							<th scope="col">휴대전화번호</th>
							<td class="bold"><input type="text" name="p_handphone" value="${view.handphone}" size="25"> &nbsp;<input name="p_issms" type="checkbox" value="Y" <c:if test="${view.issms eq 'Y'}">checked</c:if> class="vrM"> SMS 수신 허용(<c:out value="${view.issms}"></c:out>)</td>
						</tr>
						<tr>
							<th scope="col">주소</th>
							<td class="bold">
								<input type="text" name="p_post1" size="5" value="${view.zipCd1}" maxlength="3" readonly> <a href="#none" onclick="sample6_execDaumPostcode(1)"><img src="/images/adm/btn/btn_post.gif" /></a>
								<input type="text" name="p_addr" value="${view.address}" size="50">
							</td>
							<th scope="col">E-mail</th>
							<td class="bold"><input type="text" name="p_email" value="${view.email}" size="25"> &nbsp;<input type="checkbox" name="p_ismailling" value="Y" <c:if test="${view.ismailling eq 'Y'}">checked</c:if> class="vrM"> Email 수신 허용(<c:out value="${view.ismailling}"></c:out>)</td>
						</tr>
						<tr>
							<th scope="col">비밀번호 오류횟수</th>
							<td class="bold"><c:out value="${view.lgfail}" /> 회</td>
							<th scope="col">비밀번호</th>
							<td class="bold">
								<a href="#none" onclick="whenLgFSetting()" class="btn_search"><span>초기화</span></a>
								<a href="#none" onClick="pwdInit();" class="btn_search"><span>비밀번호초기화</span></a>
							</td>
						</tr>
						<tr>
							<th scope="col">장애 여부</th>
							<td class="bold">
								<select name="p_handicap_yn" class="inputpsearch">
									<option value='N' <c:if test="${view.handicapYn eq 'N'}">selected</c:if>>아니오</option>
									<option value='Y' <c:if test="${view.handicapYn eq 'Y'}">selected</c:if>>예</option>
								</select>
							</td>
							<th scope="col">휴면상태</th>
							<td class="bold">
								<select name="p_dormant_yn" class="inputpsearch">
									<option value='N' <c:if test="${view.dormantYn eq 'N'}">selected</c:if>>아니오</option>
									<option value='Y' <c:if test="${view.dormantYn eq 'Y'}">selected</c:if>>메일발송</option>
									<option value='E' <c:if test="${view.dormantYn eq 'E'}">selected</c:if>>예</option>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
				
				<table summary="" width="100%" class="popTb">
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
									<option value="T" <c:if test="${view.empGubun eq 'T'}">selected</c:if>>교원</option>
									<option value="E" <c:if test="${view.empGubun eq 'E'}">selected</c:if>>보조인력</option>
									<option value="R" <c:if test="${view.empGubun eq 'R'}">selected</c:if>>교육 전문직</option>
									<option value="P" <c:if test="${view.empGubun eq 'P'}">selected</c:if>>일반회원(학부모 등)</option>
									<option value="O" <c:if test="${view.empGubun eq 'O'}">selected</c:if>>공무원</option>
							</select></td>
							<th scope="col">근무지 주소</th>
							<td class="bold">
	                        	<input type="text" name="p_spost1" size="5" value="${view.szipCd1}" maxlength="5" readonly> <a href="#none" onclick="sample6_execDaumPostcode(2)"><img src="/images/adm/btn/btn_post.gif" /></a>
								<input type="text" name="p_saddr" value="${view.address1}" size="50">
	                        </td>
						</tr>
						<tr class="T O"><!-- TO -->
							<th scope="col">나이스개인번호</th>
							<td class="bold" colspan="3"><input type="text" value="${view.nicePersonalNum}" name="p_nicePersonalNum"></td>
						</tr>
						<tr class="T"><!-- T -->
							<th scope="col">교원 나이스개인번호 <br />제외</th>
							<td class="bold">
								<select name="p_niceNumAllowYn" class="inputpsearch">
									<option value='Y' <c:if test="${view.niceNumAllowYn eq 'Y'}">selected</c:if>>허용</option>
									<option value='N' <c:if test="${view.niceNumAllowYn eq 'N'}">selected</c:if>>허용안함</option>
								</select>
								<br />(허용시 사용자가 재로그인을 해야 적용됩니다.)
							</td>
							<th scope="col">교원자격등급</th>
							<td class="bold">
								<select name="p_job_cd" id="p_job_cd" title="교원자격등급">
	                      			<option value="">선택</option>
									<c:forEach items="${list}" var="result" varStatus="i">
										<option value="${result.code }" <c:if test="${view.jobCd eq result.code }">selected</c:if>>${result.codenm }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr class="T E"><!-- TE -->
							<th scope="col">학교명</th>
							<td class="bold">
								<input type="text" name="p_user_path" id="p_user_path" value="${view.userPath}" readonly size="20"/>
								<a href="#" class="btn_search" onclick="searchSchool(1);return false;" title="학교검색" style="cursor:hand"><span>검색</span></a>
							</td>
							<th scope="col">학교 연락처</th>
							<td class="bold"><input type="text" name="p_handphone_no" id="p_handphone_no" value="${view.handphoneNo}" size="14"/></td>
						</tr>
						<tr class="T E R"><!-- TER -->
							<th scope="col">상위교육청</th>
							<td class="bold">
								<input type="text" name="p_dept_cd_nm" id="p_dept_cd_nm" value="${view.deptnm}" readonly size="20"/>
								<input type="hidden" name="p_dept_cd" id="p_dept_cd" value="${view.deptCd}"/>
								<a id="eduSchBtn" href="#" class="btn_search" onclick="education_Office();return false;" title="교육청검색" style="cursor:hand"><span>교육청 검색</span></a>
							</td>
							<th scope="col">하위교육청</th>
							<td class="bold">
								<input type="text" name="p_agency_cd_nm" id="p_agency_cd_nm" value="${view.agencynm}" readonly size="20">
								<input type="hidden" name="p_agency_cd" id="p_agency_cd" value="${view.agencyCd}"/>
							</td>
						</tr>
						<tr class="T"><!-- T -->
							<th scope="col">경력</th>
							<td class="bold">
								<ui:code id="p_career" selectItem="${view.career}" codetype="0116" levels="1" title="경력" type="select" selectTitle="선택"/>
							</td>
							<th scope="col">담당교과</th>
							<td class="bold"><input type="text" name="p_subject" id="p_subject" maxlength="30" title="담당교과" value="${view.deptnm}"></td>
						</tr>
						<tr class="R P O"><!-- RPO -->
							<th scope="col">소속</th>
							<td class="bold"><input type="text" name="p_position_nm" size="20" id="p_position_nm" value="${view.positionNm}"></td>
							<th scope="col">직장 연락처</th>
							<td class="bold"><input type="text" name="p_handphone_no" id="p_handphone_no" value="${view.handphoneNo}" size="14"/></td>
						</tr>
						<tr class="R P O"><!-- RPO -->
							<th scope="col">부서명</th>
							<td class="bold"><input type="text" name="p_division_nm" value="${view.divisionNm}"></td>
							<th scope="col">직급</th>
							<td class="bold"><input type="text" name="p_post_nm" value="${view.postNm}"></td>
						</tr>
						<tr class="T E R P O">
							<th scope="col">등록IP</th>
							<td class="bold"><c:out value="${view.lgip}" /></td>
							<th scope="col">총 방문수</th>
							<td class="bold"><c:out value="${view.lgcnt}" /> 번</td>
						</tr>
						<tr class="T E R P O">
							<th scope="col">가입일</th>
							<td class="bold"><c:out value="${fn2:getFormatDate(view.indate, 'yyyy.MM.dd')}" /></td>
							<th scope="col">최근 방문일</th>
							<td class="bold"><c:out value="${fn2:getFormatDate(view.lglast, 'yyyy.MM.dd')}" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- //기본정보 -->









			<!-- 신청과정 -->
			<div class="listTop" style="margin: 10px;">
				<!-- tab -->
				<div class="conwrap2 MT20" style="margin-bottom: 5px;">
					<ul class="mtab2">
						<li><a href="#none" class="on" id="div_a_userEdu_1" name="div_a_userEdu_1" onClick="view_UserEduList(1)">수강과정</a></li>
						<li><a href="#none" id="div_a_userEdu_2" name="div_a_userEdu_2" onClick="view_UserEduList(2)">신청과정</a></li>
						<li class="end"><a href="#none" id="div_a_userEdu_3" name="div_a_userEdu_3" onClick="view_UserEduList(3)">수료과정</a></li>
					</ul>
				</div>
				<!-- // tab-->
			</div>

			<!-- //수강과정 -->
			<div class="popCon" style="margin-right: 20px;" id="tbl_userEduList_1">
				<!-- list table-->
				<table summary="" cellspacing="0" width="100%" class="popTb">
					<colgroup>
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="15%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
					</colgroup>
					<thead>
						<tr>
							<th scope="row">과정명</th>
							<th scope="row">기수</th>
							<th scope="row">교육구분</th>
							<th scope="row">교육기간</th>
							<th scope="row">진도율<br>(점수)
							</th>
							<th scope="row">중간<br>평가
							</th>
							<th scope="row">최종<br>평가
							</th>
							<th scope="row">리포트</th>
							<th scope="row">참여도</th>
							<th scope="row">총점</th>
							<th scope="row">수료<br>가능
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${eduList}" var="result" varStatus="status">
							<tr>
								<td class="left"><a href="javascript:insertCounselSubj('${result.subj}', '${result.gyear}', '${result.subjseq}')"> <c:out value="${result.subjnm}" />
								</a></td>
								<td><c:out value="${fn2:toNumber(result.subjseq)}" /></td>
								<td><c:out value="${result.isonoff}" /></td>
								<td><c:out value="${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')}" /> <br /> ~<c:out value="${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}" /></td>
								<td><a href="#none" onclick="fn_statusPopup('${result.subj}', '${result.year}', '${result.subjseq}');"> <c:out value="${result.tstep}" />(<c:out value="${result.avtstep}" />)
								</a></td>
								<td><c:out value="${result.mtest}" /></td>
								<td><c:out value="${result.ftest}" /></td>
								<td><c:out value="${result.report}" /></td>
								<td><c:out value="${result.etc1}" /></td>
								<td><c:out value="${result.score}" /></td>
								<td><c:out value="${result.isgraduated}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<!-- //수강과정 -->

			<!-- //신청과정 -->
			<div class="popCon" style="display: none; margin-right: 20px;" id="tbl_userEduList_2">
				<!-- list table-->
				<table summary="" cellspacing="0" width="100%" class="popTb">
					<colgroup>
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="15%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
					</colgroup>
					<thead>
						<tr>
							<th scope="row">교육그룹</th>
							<th scope="row">년도</th>
							<th scope="row">교육기수</th>
							<th scope="row">과정명</th>
							<th scope="row">기수</th>
							<th scope="row">교육구분</th>
							<th scope="row">교육기간</th>
							<th scope="row">신청일</th>
							<th scope="row">최종<br />승인여부
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${propList}" var="result" varStatus="status">
							<tr>
								<td><c:out value="${result.grcodenm}" /></td>
								<td><c:out value="${result.gyear}" /></td>
								<td><c:out value="${result.grseqnm}" /></td>
								<td class="left"><a href="javascript:insertCounselSubj('${result.subj}', '${result.gyear}', '${result.subjseq}')"> <c:out value="${result.subjnm}" />
								</a></td>
								<td><c:out value="${fn2:toNumber(result.subjseq)}" /></td>
								<td><c:out value="${result.isonoff}" /></td>
								<td><c:out value="${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')}" /> <br /> ~<c:out value="${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}" /></td>
								<td><c:out value="${fn2:getFormatDate(result.appdate, 'yyyy.MM.dd')}" /></td>
								<td><c:choose>
										<c:when test="${result.chkfinal eq 'Y'}">
							승인
                    	</c:when>
										<c:when test="${result.chkfinal eq 'N'}">
                    		반려
                    	</c:when>
										<c:otherwise>
                    		미처리
                    	</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<!-- //신청과정 -->

			<!-- //수료과정 -->
			<div class="popCon" style="display: none; margin-right: 20px;" id="tbl_userEduList_3">
				<!-- list table-->
				<table summary="" cellspacing="0" width="100%" class="popTb">
					<colgroup>
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="15%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
					</colgroup>
					<thead>
						<tr>
							<th scope="row">과정명</th>
							<th scope="row">기수</th>
							<th scope="row">교육구분</th>
							<th scope="row">교육기간</th>
							<th scope="row">진도율<br>(점수)
							</th>
							<th scope="row">중간<br>평가
							</th>
							<th scope="row">최종<br>평가
							</th>
							<th scope="row">리포트</th>
							<th scope="row">참여도</th>
							<th scope="row">총점</th>
							<th scope="row">수료<br>여부
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${graduList}" var="result" varStatus="status">
							<tr>
								<td class="left"><a href="javascript:insertCounselSubj('${result.subj}', '${result.gyear}', '${result.subjseq}')"> <c:out value="${result.subjnm}" />
								</a></td>
								<td><c:out value="${fn2:toNumber(result.subjseq)}" /></td>
								<td><c:out value="${result.isonoff}" /></td>
								<td><c:out value="${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')}" /> <br /> ~<c:out value="${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}" /></td>
								<td><c:out value="${result.tstep}" />(<c:out value="${result.avtstep}" />)</td>
								<td><c:out value="${result.mtest}" /></td>
								<td><c:out value="${result.ftest}" /></td>
								<td><c:out value="${result.report}" /></td>
								<td><c:out value="${result.etc1}" /></td>
								<td><c:out value="${result.score}" /></td>
								<td><c:out value="${result.graduatxt}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<!-- //수료과정 -->

			<!-- 상담내용 -->
			<div class="listTop" style="margin: 10px;">
				<!-- tab -->
				<div class="conwrap2 MT20" style="margin-bottom: 5px;">
					<ul class="mtab2">
						<li><a href="#" class="on">상담내용</a></li>
					</ul>
				</div>
				<!-- //tab -->
			</div>
			<div style="clear: both; overflow: hidden;">
				<p class="floatL ML20">
					분류
					<ui:code id="search_mcode" selectItem="${search_mcode}" gubun="" codetype="0047" upper="" levels="1" title="분류" className="" type="select" selectTitle="::전체::" event="selectMcode()" />
				</p>
				<div class="btnR MR05">
					<a href="#none" onClick="insertCounsel()" class="btn01"><span>상담등록</span></a>
				</div>
			</div>

			<div class="popCon" style="margin-right: 20px;">
				<!-- list table-->
				<table summary="" cellspacing="0" width="100%" class="popTb">
					<colgroup>
						<col width="%" />
						<col width="%" />
						<col width="40%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
						<col width="%" />
					</colgroup>
					<thead>
						<tr>
							<th scope="row">No</th>
							<th scope="row">분류</th>
							<th scope="row">내역</th>
							<th scope="row">작성자</th>
							<th scope="row">처리상태</th>
							<th scope="row">상담일자</th>
							<th scope="row">구분</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${counselList}" var="result" varStatus="status">
							<tr>
								<td><c:out value="${status.count}" /></td>
								<td><c:out value="${result.mcodenm}" /></td>
								<td class="left"><a href="#none" onclick="viewCounsel(${result.no})"><c:out value="${result.title}" /></a></td>
								<td><c:out value="${result.name}" /></td>
								<td><c:choose>
										<c:when test="${result.status eq '1'}">
							미처리
                    	</c:when>
										<c:when test="${result.chkfinal eq '2'}">
                    		처리중
                    	</c:when>
										<c:otherwise>
                    		완료
                    	</c:otherwise>
									</c:choose></td>
								<td><c:out value="${fn2:getFormatDate(result.sdate, 'yyyy.MM.dd')}" /></td>
								<td><c:if test="${result.gubun eq 'in'}">
                    	수신
                    </c:if> <c:if test="${result.gubun eq 'out'}">
                    	발신
                    </c:if></td>
							</tr>
						</c:forEach>


					</tbody>
				</table>
			</div>
			<!-- //상담내용 -->

			<!-- button -->
			<ul class="btnCen">
				<li><a href="#none" onclick="window.close()" class="pop_btn01"><span>닫기</span></a></li>
			</ul>
			<!-- // button -->
		</div>
	</div>
</form>
<!-- // popup wrapper -->
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp"%>
<script type="text/javascript">


var thisForm = eval('document.<c:out value="${gsPopForm}"/>');


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


//개인정보 변경시
function whenMemberSave() {
	if (thisForm.p_name.value == "") {
        alert("이름을 입력하세요.");
        thisForm.p_name.focus();
        return;
    }
    
	if (thisForm.p_email.value == "") {
        alert("이메일을 입력하세요.");
        thisForm.p_email.focus();
        return;
    }
    
    if(confirm("개인정보를 수정하시겠습니까?"))
    {
	    thisForm.action = "/adm/pop/searchPersonGeneralAction.do";
	    thisForm.p_process.value = "update";
		thisForm.target = "_self";
		thisForm.submit();
    }
}

//비밀번호 초기화시..
function whenLgFSetting(){
	if(confirm("비밀번호 오류 횟수를 [0]으로 초기화 하시겠습니까?"))
    {
		thisForm.action = "/adm/pop/searchPersonGeneralAction.do";
	  	thisForm.p_process.value = "Lgfsetting";
		thisForm.target = "_self";
		thisForm.submit();
    }
}


//상담 분류 선택
function selectMcode() {
	thisForm.action = "/adm/pop/searchPersonGeneralPopup.do";
  	thisForm.p_process.value = "";
	thisForm.target = "_self";
	thisForm.submit();
}


//상담등록
function insertCounsel() {
	var url = '/adm/pop/searchCounselPopup.do'
		+ '?p_userid=<c:out value="${p_userid}"/>'
		;
			
	window.open(url,"searchCounselPopupWindowPop","width=800,height=650,scrollbars=yes");
}

// // 상담등록 - 과정클릭시
function insertCounselSubj(subj, year, subjseq) {
	var url = '/adm/pop/searchCounselPopup.do'
		+ '?p_userid=<c:out value="${p_userid}"/>'
		+ '&p_subjyearsubjseq=' + subj + '/' + year + '/' + subjseq;
		;
			
	window.open(url,"searchCounselPopupWindowPop","width=800,height=650,scrollbars=yes");
	
}

// 상담내역보기
function viewCounsel(p_no) {
	var url = '/adm/pop/searchCounselPopup.do'
		+ '?p_userid=<c:out value="${p_userid}"/>'
		+ '&p_no=' + p_no
		;
			
	window.open(url,"searchCounselPopupWindowPop","width=800,height=650,scrollbars=yes");
}

function view_UserEduList(num)
{
	if(num == 1)
	{
		document.all("tbl_userEduList_1").style.display = "block";
		document.all("tbl_userEduList_2").style.display = "none";
		document.all("tbl_userEduList_3").style.display = "none";

		document.getElementById("div_a_userEdu_1").className = "on";
		document.getElementById("div_a_userEdu_2").className = "";
		document.getElementById("div_a_userEdu_3").className = "";
	}

	if(num == 2)
	{
		document.all("tbl_userEduList_1").style.display = "none";
		document.all("tbl_userEduList_2").style.display = "block";
		document.all("tbl_userEduList_3").style.display = "none";
		
		document.getElementById("div_a_userEdu_1").className = "";
		document.getElementById("div_a_userEdu_2").className = "on";
		document.getElementById("div_a_userEdu_3").className = "";
	}

	if(num == 3)
	{
		document.getElementById("tbl_userEduList_1").style.display = "none";
		document.getElementById("tbl_userEduList_2").style.display = "none";
		document.getElementById("tbl_userEduList_3").style.display = "block";

		document.getElementById("div_a_userEdu_1").className = "";
		document.getElementById("div_a_userEdu_2").className = "";
		document.getElementById("div_a_userEdu_3").className = "on";
	}
}

function exec(url, chk){
	try
    {
        var objWSH = new ActiveXObject("WScript.Shell");
        if (objWSH == null) return;
        objWSH.Run("IExplore.exe "+chk+url);
    }
    catch (e) {
        alert(e.message + "\n도구 > 인터넷 옵션 > 보안 > 사용자 지정 수준 클릭>스크립팅하기 안전하지 않은 것으로 표시된 ActiveX컨트롤 초기화 및 스크립팅을 확인으로 변경하세요!\n");
        //alert(e.message);
    }
}

function pwdInit() {
	if(!confirm('비밀번호를 초기화를 하시겠습니까?')) return;
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/userPwdInit.do'
		, type: 'post'
		, data: {
			p_userid_1: '${p_userid}'
			, p_handphone: '${view.handphone}'
			, p_birthDate: '${view.birthDate}'
		}
		, dataType: 'json'
		, success: function(result) {
			alert(result.resultMsg);
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}

$(function() {
	empGubunChange('${view.empGubun}');
	
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
	thisForm.p_schoolgubun.value = p_schoolgubun;
	window.open('', 'schoolWindowPop', 'width=750,height=650');
	thisForm.action = "/usr/mem/searchSchoolPop.do";
	thisForm.target = "schoolWindowPop";
	thisForm.submit();
}

function receiveSchool(arr) {
	var p_schoolgubun = thisForm.p_schoolgubun.value;

	if(p_schoolgubun == 1)
	{
		thisForm.p_user_path.value = arr[0];
		thisForm.p_dept_cd_nm.value = arr[1];
		thisForm.p_agency_cd_nm.value = arr[2];
		if(arr[3] != null && arr[3] != '') 
			thisForm.p_handphone_no.value = arr[3];
		thisForm.p_dept_cd.value = arr[4];
		thisForm.p_agency_cd.value = arr[5];
	}

	//교육청
	if(p_schoolgubun == 3){
		thisForm.p_dept_cd.value = arr[0];
		thisForm.p_agency_cd.value = arr[1];
		thisForm.p_dept_cd_nm.value = arr[2];
		thisForm.p_agency_cd_nm.value = arr[3];
	}
}

//교육청 검색
function education_Office() {
	thisForm.p_schoolgubun.value = 3;
	window.open('', 'education_OfficePop', 'width=550,height=500');
	thisForm.action = "/usr/mem/searchEducationOfficePop.do";
	thisForm.target = "education_OfficePop";
	thisForm.submit();
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
                	thisForm.p_post1.value = data.zonecode; //5자리 새우편번호 사용
                	thisForm.p_addr.value = fullAddr;
                } else if(2 == p_zipgubun) {
                	thisForm.p_spost1.value = data.zonecode; //5자리 새우편번호 사용
                	thisForm.p_saddr.value = fullAddr;
                }
            }
        }).open();
    }
</script>
















