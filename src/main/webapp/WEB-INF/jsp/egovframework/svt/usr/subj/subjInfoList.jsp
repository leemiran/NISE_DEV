<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
<!--login check-->

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}\n개인정보 수정 페이지로 이동합니다.");
	changeMenu(3, 5);
</c:if>
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
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
<input type="hidden"  name="p_subj" value=""/>
<input type="hidden"  name="p_year" value=""/>
<input type="hidden"  name="p_subjseq" value=""/>
<input type="hidden"  name="p_subjnm" value=""/>
<input type="hidden"  name="p_process" value=""/>
<input type="hidden" name="p_course" value=""/>
<input type="hidden" name="areaCode" value="<c:out value="${areaCode}"  escapeXml="true" />"/>

<%-- <div id="content05">
	<div class="kyoicon2">
		<div id="tabB">
			<a href="javascript:"><img src="/images/user/ysgo_01<c:if test="${'B10' eq areaCode}">_1</c:if>.png" alt="서울" /><img src="/images/user/ysgo_01_1.png" alt="서울" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_02<c:if test="${'C10' eq areaCode}">_1</c:if>.png" alt="부산" /><img src="/images/user/ysgo_02_1.png" alt="부산" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_03<c:if test="${'D10' eq areaCode}">_1</c:if>.png" alt="대구" /><img src="/images/user/ysgo_03_1.png" alt="대구" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_04<c:if test="${'E10' eq areaCode}">_1</c:if>.png" alt="인천" /><img src="/images/user/ysgo_04_1.png" alt="인천" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_05<c:if test="${'F10' eq areaCode}">_1</c:if>.png" alt="광주" /><img src="/images/user/ysgo_05_1.png" alt="광주" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_06<c:if test="${'G10' eq areaCode}">_1</c:if>.png" alt="대전" /><img src="/images/user/ysgo_06_1.png" alt="대전" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_07<c:if test="${'H10' eq areaCode}">_1</c:if>.png" alt="울산" /><img src="/images/user/ysgo_07_1.png" alt="울산" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_08<c:if test="${'I10' eq areaCode}">_1</c:if>.png" alt="세종" /><img src="/images/user/ysgo_08_1.png" alt="세종" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_09<c:if test="${'J10' eq areaCode}">_1</c:if>.png" alt="경기" /><img src="/images/user/ysgo_09_1.png" alt="경기" class="over"></a>
		</div>
	</div>
	<div class="kyoicon2">
		<div id="tabB">
			<a href="javascript:"><img src="/images/user/ysgo_10<c:if test="${'K10' eq areaCode}">_1</c:if>.png" alt="강원" /><img src="/images/user/ysgo_10_1.png" alt="강원" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_11<c:if test="${'M10' eq areaCode}">_1</c:if>.png" alt="충북" /><img src="/images/user/ysgo_11_1.png" alt="충북" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_12<c:if test="${'N10' eq areaCode}">_1</c:if>.png" alt="충남" /><img src="/images/user/ysgo_12_1.png" alt="충남" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_13<c:if test="${'P10' eq areaCode}">_1</c:if>.png" alt="전북" /><img src="/images/user/ysgo_13_1.png" alt="전북" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_14<c:if test="${'Q10' eq areaCode}">_1</c:if>.png" alt="전남" /><img src="/images/user/ysgo_14_1.png" alt="전남" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_15<c:if test="${'R10' eq areaCode}">_1</c:if>.png" alt="경북" /><img src="/images/user/ysgo_15_1.png" alt="경북" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_16<c:if test="${'S10' eq areaCode}">_1</c:if>.png" alt="경남" /><img src="/images/user/ysgo_16_1.png" alt="경남" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_17<c:if test="${'T10' eq areaCode}">_1</c:if>.png" alt="제주" /><img src="/images/user/ysgo_17_1.png" alt="제주" class="over"></a>
			<a href="javascript:"><img src="/images/user/ysgo_18<c:if test="${'A00' eq areaCode}">_1</c:if>.png" alt="교양" /><img src="/images/user/ysgo_18_1.png" alt="교양" class="over"></a>
		</div>
	</div>
</div> --%>

<!-- search wrap-->
<div class="courseWrap">
	<ul>
		<li>
			교육분류
			<ui:code id="search_upperclass" selectItem="${search_upperclass}" gubun="cursBunryu" codetype="" upper="" title="교육분류" className="" type="select" selectTitle="전체과정" event="" />
		</li>
		<li>
			교육시작일
			<c:set var="nowYear">${fn2:getFormatDateNow('yyyy')}</c:set>
			<c:set var="nowMonth">${fn2:getFormatDateNow('MM')}</c:set>
			<select name="search_year" title="교육시작일 연도선택">
				<option value="">전체</option>
				<c:forEach var="i" begin="${nowYear-1}" end="${nowYear+1}" step="1">
				<c:set var="str">${i}</c:set>
				<option value="${str}" <c:if test="${str == search_year}">selected</c:if>>${str}년</option>
				</c:forEach>
			</select>
			<select name="search_month" title="월선택">
				<option value="">전체</option>
				<c:forEach var="j" begin="1" end="12" step="1">
				<c:if test="${j < 10}">
				<c:set var="str">0${j}</c:set>
				<option value="${str}" <c:if test="${(str) == search_month}">selected</c:if>>${str}월</option>
				</c:if>
				<c:if test="${j >= 10}">
				<c:set var="str">${j}</c:set>
				<option value="${str}" <c:if test="${(str) == search_month}">selected</c:if>>${str}월</option>
				</c:if>
				</c:forEach>
			</select>
			<input name="search_text" type="text" size="20" value="<c:out value="${search_text}"  escapeXml="true" />" onkeydown="fn_keyEvent('doSearchList')" title="검색어"/>
			<a href="#none" onclick="doSearchList()"><img src="/images/user/btn_search.gif" alt="검색" /></a>
		</li>
	</ul>
</div>
<!-- // search wrap -->
<div class="mb5">
	<span><img src="/images/user/btn_new.gif" alt="신규" /></span>
	<span class="pl5"><img src="/images/user/btn_choose.gif" alt="추천" /></span>
</div>
<div class="mb5">
	<span><img src="/images/user/upperclass_info.gif" alt="연수종별" /></span>
</div>

<!-- list table-->
<div class="studyList">
	<table summary="패키지, 과정명, 연수기간, 수강여부로 구성" cellspacing="0" width="100%">
		<caption>교육분류</caption>
		<colgroup>
			<col width="6%" />
			<col width="6%" />
			<col width="6%" />
			<col width="%" />
			<col width="15%" />
			<col width="10%" />
			<col width="10%" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col">기별</th>
				<th scope="col">연수<br>구분</th>
				<th scope="col">운영<br>형태</th>
				<th scope="col">과정명</th>
				<th scope="col">연수기간</th>
				<th scope="col">수강여부</th>
				<th scope="col" class="last">수강신청</th>
			</tr>
		</thead>
		<tbody>
			<c:set var="rownumCnt" value="0"/>
			<!-- 목록 시작 -->
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
					<c:if test="${rownumCnt == 0}">
						<!-- 기별 -->
						<td<c:if test="${result.coursecount > 1 && rownumCnt == 0}"> rowspan="${result.coursecount}"</c:if>>${fn2:toNumber(result.subjseq)}</td>
						<!-- 과정 구분 -->
						<td<c:if test="${result.coursecount > 1 && rownumCnt == 0}"> rowspan="${result.coursecount}"</c:if>>
							<c:if test="${result.upperclass eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
							<c:if test="${result.upperclass eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
							<c:if test="${result.upperclass eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="교양연수"/></c:if>
							<c:if test="${result.upperclass eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
							<c:if test="${result.upperclass eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모연수"/></c:if>
						
						<!-- 운영 형태 -->
						<td<c:if test="${result.coursecount > 1 && rownumCnt == 0}"> rowspan="${result.coursecount}"</c:if>>
							<c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
							<c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
							<c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
						</td>
					</c:if>
					<!-- 과정명 -->
					<td class="left">
						<a href="#none" onclick="goSubjInfo('${result.subj}', '${result.course}')">
							<c:if test="${result.course ne '000000'}">
								[패키지]  ${result.subjnm} ${fn2:toNumber(result.subjseq)}기
							</c:if>
							<c:if test="${result.course eq '000000'}">
								${result.subjnm}
							</c:if>
						</a>
					</td>
					<!-- 연수 기간 -->
					<td class="num">
						${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')}&nbsp;~&nbsp;
						${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}&nbsp;/&nbsp;
						${result.eduperiod}주
					</td>
					<c:if test="${rownumCnt == 0}">
						<!-- 수강 여부 -->
						<td class="num"<c:if test="${result.coursecount > 1 && rownumCnt == 0}"> rowspan="${result.coursecount}"</c:if>>
							<c:choose>
								<c:when test="${'A00' ne result.areaCodes and !fn:contains(result.areaCodes, userAreaCode)}">
									<img src="/images/user/subj_status05.gif" alt="신청불가"/>
								</c:when>
								<c:otherwise>
									<!--회원구분이 학부모인데 학부모 연수가 아닌 과정인 경우 신청불가로 한다.-->
									<c:choose>
										<c:when test="${(('PRF' eq result.upperclass and 'E' eq result.empGubun) or 
														 ('PRF' eq result.upperclass and 'P' eq result.empGubun) or 
														 ('PRF' eq result.upperclass and 'O' eq result.empGubun))
														 or 
														(('EXT' eq result.upperclass and 'T' eq result.empGubun) or 
														 ('EXT' eq result.upperclass and 'R' eq result.empGubun))}">
											<img src="/images/user/subj_status05.gif" alt="신청불가"/>
										</c:when>
										<c:otherwise>
											<c:if test="${result.propstatus eq 'A'}">
												<c:choose>
													<c:when test="${result.isPropose > 0}">
														<c:choose>
															<c:when test="${result.chkfinal eq 'Y'}">
																<img src="/images/user/subj_status06.gif" alt="승인"/>
															</c:when>
															<c:otherwise>
																<img src="/images/user/subj_status04.gif" alt="신청중"/>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${result.isPropose2 < result.studentlimit}">
																<img src="/images/user/subj_status02.gif" alt="신청가능"/>
															</c:when>
															<c:otherwise>
																<img src="/images/user/subj_status03.gif" alt="신청마감"/>
															</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</c:if>
											<c:if test="${result.propstatus eq 'B'}">
												<img src="/images/user/subj_status01.gif" alt="준비중"/>
											</c:if>
											<c:if test="${result.propstatus eq 'C'}">
												<img src="/images/user/subj_status03.gif" alt="신청마감"/>
											</c:if>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</td>
						<!-- 수강 신청 -->
						<td class="num last"<c:if test="${result.coursecount > 1 && rownumCnt == 0}"> rowspan="${result.coursecount}"</c:if>>
							<c:choose>
								<c:when test="${'A00' ne result.areaCodes and !fn:contains(result.areaCodes, userAreaCode)}">
									<img src="/images/user/subj_status05.gif" alt="신청불가"/>
								</c:when>
								<c:otherwise>
									<!--회원구분이 학부모인데 학부모 연수가 아닌 과정인 경운 신청불가로 한다.-->
									<c:choose>
										<c:when test="${(('PRF' eq result.upperclass and 'E' eq result.empGubun) or 
														 ('PRF' eq result.upperclass and 'P' eq result.empGubun) or 
														 ('PRF' eq result.upperclass and 'O' eq result.empGubun))
														 or 
														(('EXT' eq result.upperclass and 'T' eq result.empGubun) or 
														 ('EXT' eq result.upperclass and 'R' eq result.empGubun))}">
											<img src="/images/user/subj_status05.gif" alt="신청불가"/>
										</c:when>
										<c:otherwise>
											<c:if test="${result.propstatus eq 'A'}">
												<c:choose>
													<c:when test="${result.isPropose < 1 and result.isPropose2 < result.studentlimit}">
														<a href="#none" onclick="whenSubjPropose('${result.subj}','${result.year}','${result.subjseq}');">
			                                    			<img src="/images/user/subj_study_add.gif" alt="신청하기"/>
			                                    		</a>
													</c:when>
													<c:when test="${result.isPropose > 0}">
														<a href="#none" onclick="whenSubjCancel('${result.subj}','${result.year}','${result.subjseq}');">
															<img src="/images/user/subj_study_cancel.gif" alt="신청취소"/>
														</a>
													</c:when>
													<c:otherwise>
														<img src="/images/user/subj_status05.gif" alt="신청불가"/>
													</c:otherwise>
												</c:choose>
		                                    </c:if>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</td>
					</c:if>
				</tr>
				<c:if test="${result.coursecount > 1}">
					<c:choose>
						<c:when test="${result.coursecount == rownumCnt + 1}">
							<c:set var="rownumCnt" value="0"/>
						</c:when>
						<c:otherwise>
							<c:set var="rownumCnt" value="${rownumCnt + 1}"/>
						</c:otherwise>
					</c:choose>
				</c:if>
			</c:forEach>
			<!-- 목록 끝 -->
			<c:if test="${empty list}">
				<tr>
					<td colspan="7" class="last">검색된 정보가 없습니다.</td>
				</tr>
			</c:if>
		</tbody>
	</table>
</div>
<!-- list table-->

<!-- button
<ul class="btnR m20">
	<li><a href="#none" onclick="saveSubjConcernForCheckbox();"><img src="/images/user/btn_course_in.gif" alt="관심목록담기"/></a></li>
</ul>
button -->
</form>

<script type="text/javascript">
<!--
window.onload = function () {
	/*
	if (confirm("수강신청 하시기 전에 회원정보의 나이스 번호가 올바르게 입력되었는지 확인하여 주십시요.\n회원정보를 수정하시려면 확인을 하여 주십시요.") == true){    //확인
		location.href = "/usr/mpg/memMyPage.do?menu_main=6&menu_sub=3";
	}else{   //취소

	}
	*/
}

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList()
{
	thisForm.action = "/usr/subj/subjInfoList.do";
	thisForm.target = "_self";
	thisForm.submit();
}

function doSearchList() {
	if(thisForm.search_text.value == '') {
		if(!confirm("검색어 없이 검색하시면 전체가 검색됩니다. 검색하시겠습니까?")) {
			thisForm.search_text.focus();
			return;
		} else {
			thisForm.action = "/usr/subj/subjInfoList.do";
			thisForm.target = "_self";
			thisForm.submit();
		}
	} else {
		thisForm.action = "/usr/subj/subjInfoList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
}

//선택박스에서 선택해서 하는 유형의  관심과정 저장
function saveSubjConcernForCheckbox() {
	if(!hasCheckedBox(thisForm._Array_p_checks)) {
	     alert("하나이상 선택해야 합니다.");
	     return;
	}

	if(confirm("선택하신 과정을 관심과정으로 저장하시겠습니까?"))
	{
		thisForm.p_process.value = "insert";
		thisForm.action = "/usr/mpg/concernInfoAction.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
}

//과정 상세 페이지로 전환
function goSubjInfo(subj, course) {
	thisForm.p_course.value = course;
	thisForm.p_subj.value = subj;
	thisForm.action = "/usr/subj/subjInfoView.do";
	thisForm.target = "_self";
	thisForm.submit();
}

function whenSubjCancel(p_subj, p_year, p_subjseq) {
	//console.log(p_subj);
	//console.log(p_year);
	//console.log(p_subjseq);

	if (confirm("정말로 수강을 취소하시겠습니까?")) {
		thisForm.p_subj.value = p_subj;
		thisForm.p_year.value = p_year;
		thisForm.p_subjseq.value = p_subjseq;
		thisForm.method = 'post';
		thisForm.action = "/usr/subj/subjProposeCancelAction.do";
		thisForm.submit();
	}
}

function whenSubjPropose(p_subj, p_year, p_subjseq) {
	window.open("","subjectProposeStepWindowPopup","width=614,height=850,scrollbars=yes");
	thisForm.p_subj.value = p_subj;
	thisForm.p_year.value = p_year;
	thisForm.p_subjseq.value = p_subjseq;
	thisForm.action = "/usr/subj/subjProposeStep01.do";
	thisForm.target = "subjectProposeStepWindowPopup";
	thisForm.submit();
}
//-->

$(document).ready(function() {
	var areaCode = ['B10','C10','D10','E10','F10','G10','H10','I10','J10','K10','M10','N10','P10','Q10','R10','S10','T10','A00'];
	$('.kyoicon2 a').on('click', function() {
		$('input[name=areaCode]').val(areaCode[$('.kyoicon2 a').index($(this))]);
		thisForm.action = "/usr/subj/subjInfoList.do";
		thisForm.target = "_self";
		thisForm.submit();
	});
});
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
