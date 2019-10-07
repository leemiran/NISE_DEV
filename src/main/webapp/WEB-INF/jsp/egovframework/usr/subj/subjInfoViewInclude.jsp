<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<form name="<c:out value="${gsMainForm}" escapeXml="true" />" id="<c:out value="${gsMainForm}" escapeXml="true" />" method="post" onsubmit="return false;" action="">
<fieldset>
<legend>과정상세정보</legend>
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
<input type="hidden"  name="p_subj" value="<c:out value="${p_subj}" escapeXml="true" />"/>
<input type="hidden"  name="p_year" value=""/>
<input type="hidden"  name="p_subjseq" value="<c:out value="${p_subjseq}" escapeXml="true" />"/>
<input type="hidden"  name="p_subjnm" value=""/>
<input type="hidden"  name="p_process" value=""/>
<input type="hidden" id="pageIndex" name="pageIndex"/>

<!--검색-->
<input type="hidden"  name="search_text" value="<c:out value="${search_text}" escapeXml="true" />"/>
<input type="hidden"  name="search_upperclass" value="<c:out value="${search_upperclass}" escapeXml="true" />"/>
<input type="hidden"  name="search_year" value="<c:out value="${search_year}" escapeXml="true" />"/>
<input type="hidden"  name="search_month" value="<c:out value="${search_month}" escapeXml="true" />"/>

<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>

<div class="mycon">
	<div class="sub_text">
		<h4><%-- [${view.subj}] --%> ${view.subjnm} <span class="txtred">[${view.codenm}]</span></h4>
	</div>
	<!-- 과정타이틀 시작 -->
	<div class="bbsList2">
		<ul>
			<li>
				<dl>
					<dt class="subject">
						<span class="head">과정만족도</span>
						<span class="txt">
							<span class="graph_off">
								<c:set var="v_satisfaction">0</c:set>
								<c:if test="${not empty view.satisfaction}">
									<c:set var="v_satisfaction">${view.satisfaction}</c:set>
								</c:if>
								<span class="graph_on">
									<img src="/images/user/graph_bar.gif" alt="" width="${v_satisfaction}" height="1" />
								</span>
							</span>
						</span>
						<span class="">&nbsp;(<fmt:formatNumber value="${v_satisfaction}"  pattern=",###.#" type="number"/>)</span>
						<span class="write">교육후기</span>
						<span class="txt"><span class="txtred">(<fmt:formatNumber value="${view.commentsCnt}" type="number"/>)</span></span>
					</dt>
					<dd class="thumbnail">
					
						<c:if test="${not empty view.introducefilenamenew}">
						
							<c:if test="${ view.youtubeYn eq 'Y'}">
								<iframe width="245" height="173" src="https://www.youtube.com/embed/L14wSQ0EPXo" frameborder="0" allowfullscreen></iframe>
							</c:if>
							<c:if test="${ view.youtubeYn eq 'N'}">
								<img src="/dp/subject/${view.introducefilenamenew}" alt="${view.subjnm}" width="245" height="173" />
							</c:if>
						</c:if>
						<c:if test="${empty view.introducefilenamenew}">
							<c:if test="${ view.youtubeYn eq 'Y'}">
								<embed src="http://www.youtube.com/v/${view.youtubeUrl}&hl=ko_KR" type="application/x-shockwave-flash" width="245" height="173" allowfullscreen="true">
							</c:if>
							<c:if test="${ view.youtubeYn eq 'N'}">
								<img src="/images/user/list2_img.gif" alt="${view.subjnm}" width="245" height="173" />
							</c:if>
							
						</c:if>
					</dd>
					<dd class="info">
						<span class="head">과정분류</span>
						<span class="txt">${view.uppcheck}</span>
					</dd>
					<dd class="info">
						<span class="head">교육기간/복습기간</span>
						<span class="txt">
							${(view.eduperiod eq '') ? '0' : view.eduperiod}주(${view.edutimes}시간)
							<c:if test="${view.isablereview eq 'Y'}">
								/ 수료 후 ${view.reviewdays}개월간 복습가능
							</c:if>
							<c:if test="${view.isablereview ne 'Y'}">
								/ 복습없음
							</c:if>
						</span>
					</dd>
					<dd class="info">
						<span class="head">수강료</span>
						<span class="txt"><fmt:formatNumber value="${view.biyong}"  pattern="#,###"/>원</span>
						<span class="head" Style="padding-left:10px">교재수령여부</span>
						<span class="txt">
							<c:if test="${view.usebook eq 'Y'}">제공</c:if>
							<c:if test="${view.usebook ne 'Y'}">미제공</c:if>
						</span>
					</dd>
					<dd class="info">
						<span class="head">정원</span>
						<!--<span class="txt"><fmt:formatNumber value="${view.biyong}" type="number"/>원</span>-->
						<!--<span style="padding-left:178px;" class="head count">정원</span>-->
						<span class="txt"><fmt:formatNumber value="${view.studentlimit}" type="number"/>명</span>
					</dd>
					<c:if test="${view.usebook eq 'Y' && not empty view.bookname}">
						<dd class="info">
							<span class="head">교재</span>
							<span class="txt">
								<c:out value="${view.bookname}"/>
							</span>
						</dd>
					</c:if>
					<dd class="info_bg">
						<%-- <c:if test="${not empty sessionScope.userid}">
							<span><a href="#none" onclick="saveSubjConcern()"><img src="/images/user/btn_course_in.gif" alt="관심목록담기"/></a></span>
						</c:if> --%>
						<span class="floatR" id="PopupSubjPageList"><a href="#none" onclick="doPageList()"><img src="/images/user/btn_course_list.gif" alt="과정목록"/></a></span>
					</dd>
				</dl>
			</li>
		</ul>
	</div>
	<!-- 과정타이틀 끝 -->

	<!--로그인시에만 보여줌            -->
	<c:if test="${not empty sessionScope.userid}">
		<!-- list -->
		<div class="studyList">
			<table summary="기수, 과정명(기수), 연수기간, 정원[신청수], 수강여부, 신청하기로 구성">
				<caption>수강신청</caption>
				<colgroup>
					<col width="6%"/>
					<col width="*"/>
					<col width="22%"/>
					<col width="12%"/>
					<col width="9%"/>
					<!--<col width="9%"/>-->
					<col width="12%"/>
				</colgroup>
				<thead>
					<tr>
						<th scope="col">기수</th>
						<th scope="col">과정명(기수)</th>
						<th scope="col">연수기간</th>
						<th scope="col">정원[신청수]</th>
						<!--<th scope="col">수강료</th>-->
						<th scope="col">수강여부</th>
						<th scope="col" class="last">신청하기</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty subjSeqList}">
						<tr>
							<td class="last" colspan="10">수강신청기간이 아닙니다.</td>
						</tr>
					</c:if>
					<c:forEach items="${subjSeqList}" var="result" varStatus="status">
						<tr>
							<td>${fn2:toNumber(result.subjseq)}</td>
							<td style="text-align:left;padding-left:2px;">
								<c:if test="${result.course ne '000000'}">
									<span style="color:#3366cc;">[패키지 : ${result.coursenm}]</span><br/>
								</c:if>
								${result.subjnm}
							</td>
							<td>${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
							<td><fmt:formatNumber value="${result.studentlimit}" type="number"/>[<fmt:formatNumber value="${result.propcnt}" type="number"/>]</td>
							<!--<td><fmt:formatNumber value="${result.biyong}" type="number"/>원</td>-->
							<td>
								<c:choose>
									<c:when test="${'A00' ne result.areaCodes and !fn:contains(result.areaCodes, userAreaCode)}">
										<img src="/images/user/btn_request_off.gif" alt="신청불가">
									</c:when>
									<c:otherwise>
										<!-- 수화기초과정 11기 수강신청 구분 제외 시키기  -->
										<c:choose>
											<c:when test="${(('교원직무연수' eq view.uppcheck and 'E' eq result.empGubun) or 
															 ('교원직무연수' eq view.uppcheck and 'P' eq result.empGubun) or 
															 ('교원직무연수' eq view.uppcheck and 'O' eq result.empGubun))
															 or 
															(('보조인력연수' eq view.uppcheck and 'T' eq result.empGubun) or 
															 ('보조인력연수' eq view.uppcheck and 'R' eq result.empGubun))}">
												<img src="/images/user/btn_request_off.gif" alt="신청불가">
											</c:when>
											<c:otherwise>
												<c:if test="${result.propstatus eq 'A'}">
													<c:choose>
														<c:when test="${result.isPropose > 0}">
															<c:choose>
																<c:when test="${result.isProposeChk > 0}">
																	<img src="/images/user/ico_applying2.gif" alt="승인">
																</c:when>
																<c:otherwise>
																	<img src="/images/user/ico_status.gif" alt="대기중">
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${result.propcnt < result.studentlimit}">
																	<img src="/images/user/btn_request_on.gif" alt="신청가능">
																</c:when>
																<c:otherwise>
																	<img src="/images/user/m_course_end.gif" alt="마감">
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												</c:if>
												<c:if test="${result.propstatus eq 'B'}">
													<img src="/images/user/m_course_end.gif" alt="마감">
												</c:if>
												<c:if test="${result.propstatus eq 'C'}">
													<img src="/images/user/m_course_end.gif" alt="마감">
												</c:if>
												<c:if test="${result.propstatus eq 'D'}">
													<img src="/images/user/btn_request_off.gif" alt="신청불가">
												</c:if>
											</c:otherwise>
										</c:choose>
										<!--
										${result.empGubun}
										${view.uppcheck}
										${fn:indexOf(view.uppcheck, '학부모')}
										-->
									</c:otherwise>
								</c:choose>
							</td>
							<td class="last">
								<c:choose>
									<c:when test="${'A00' ne result.areaCodes and !fn:contains(result.areaCodes, userAreaCode)}">
										-
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${(('교원직무연수' eq view.uppcheck and 'E' eq result.empGubun) or 
															 ('교원직무연수' eq view.uppcheck and 'P' eq result.empGubun) or 
															 ('교원직무연수' eq view.uppcheck and 'O' eq result.empGubun))
															 or 
															(('보조인력연수' eq view.uppcheck and 'T' eq result.empGubun) or 
															 ('보조인력연수' eq view.uppcheck and 'R' eq result.empGubun))}">
												-
											</c:when>
											<c:otherwise>
												<c:if test="${result.propstatus eq 'A'}">
													<c:choose>
														<c:when test="${result.isPropose < 1 and result.propcnt < result.studentlimit}">
															<a href="#none" onclick="whenSubjPropose('${result.subj}','${result.year}','${result.subjseq}');">
				                                    			<img src="/images/user/btn_course_request.gif" alt="수강신청" title="새 창으로 열기">
				                                    		</a>
														</c:when>
														<c:when test="${result.isPropose > 0}">
															<a href="#none" onclick="whenSubjCancel('${result.subj}','${result.year}','${result.subjseq}');">
																<img src="/images/user/subj_study_cancel.gif" alt="신청취소"/>
															</a>
														</c:when>
														<c:otherwise>
															-
														</c:otherwise>
													</c:choose>
			                                    </c:if>
			                                    <c:if test="${result.propstatus ne'A'}">
			                                    	-
			                                    </c:if>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- // list -->
	</c:if>

	<div class="sub_text">
		<h4>과정소개</h4>
	</div>

	<!-- tab -->
	<div class="conwrap2">
		<ul class="mtab2" id="subTabMenu1" style="display:block;">
			<li><a href="#none" onclick="displayTabMenu(1)" class="on">과정소개</a></li>
			<li><a href="#none" onclick="displayTabMenu(2)">교육내용</a></li>
			<li><a href="#none" onclick="displayTabMenu(3)" class="end">교육후기</a></li>
		</ul>
		<ul class="mtab2" id="subTabMenu2" style="display:none;">
			<li><a href="#none" onclick="displayTabMenu(1)">과정소개</a></li>
			<li><a href="#none" onclick="displayTabMenu(2)" class="on">교육내용</a></li>
			<li><a href="#none" onclick="displayTabMenu(3)" class="end">교육후기</a></li>
		</ul>
		<ul class="mtab2" id="subTabMenu3" style="display:none;">
			<li><a href="#none" onclick="displayTabMenu(1)">과정소개</a></li>
			<li><a href="#none" onclick="displayTabMenu(2)">교육내용</a></li>
			<li><a href="#none" onclick="displayTabMenu(3)" class="on">교육후기</a></li>
		</ul>
	</div>
	<!-- //tab -->

	<!--과정소개-->
	<div id="subTabContent1" style="display:block;">
		<div class="course"><span class="title">교육목표</span></div>
		<ul class="course">
			<li>
				<c:out value="${fn:replace(view.intro, lf, '<br/>')}" escapeXml="false"/>
			</li>
		</ul>
		<div class="course"><span class="title">교육개요</span></div>
		<ul class="course">
			<li>
				<c:out value="${fn:replace(view.edumans, lf, '<br/>')}" escapeXml="false"/>
			</li>
		</ul>
		<div class="course"><span class="title">평가기준</span></div>
			<ul class="course">
				<li class="last">
					<c:out value="${fn:replace(view.memo, lf, '<br/>')}" escapeXml="false"/>
					<div class="courseList">
						<table summary="구분, 가중치, 수료기준으로 구성">
							<caption>평가기준</caption>
							<colgroup>
								<col width="40%"/>
								<col width="20%"/>
								<col width="40%"/>
							</colgroup>
							<thead>
								<tr>
									<th scope="col">구분</th>
									<th scope="col">가중치</th>
									<th scope="col" class="last">수료기준</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${view.wmtest > 0}">
									<tr>
										<td>출석평가</td>
										<td class="num">${view.wmtest}%</td>
										<%-- <td class="last">${view.wmtest > 0 ? '반드시 제출' : '-'}</td> --%>
										<td class="last">필수</td>
									</tr>
								</c:if>
								<c:if test="${view.wftest > 0}">
									<tr>
										<td>온라인평가</td>
										<td class="num">${view.wftest}%</td>
										<%-- <td class="last">${view.wftest > 0 ? '반드시 제출' : '-'}</td> --%>
										<td class="last">필수</td>
									</tr>
								</c:if>
								<c:if test="${view.wreport > 0}">
									<tr>
										<td>온라인과제</td>
										<td class="num">${view.wreport}%</td>
										<%-- <td class="last">${view.wreport > 0 ? '반드시 제출' : '-'}</td> --%>
										<td class="last">선택</td>
									</tr>
								</c:if>
								<!--<tr>-->
									<!--<td>출석일</td>-->
									<!--<td class="num">${view.wetc1}일이상</td>-->
									<!--<td class="last">-</td>-->
								<!--</tr>-->
								<tr>
									<td>참여도평가</td>
									<td class="num">${view.wetc2}%</td>
									<td class="last">${view.ratewbt}%</td>
								</tr>
								<tr>
									<td>총점</td>
									<td class="num">100%</td>
									<td class="last">${view.gradscore}점 이상</td>
								</tr>
							</tbody>
						</table>
					</div>
				</li>
			</ul>
		</div>
		<!--교육내용-->
		<ul class="course" id="subTabContent2" style="display:none;">
			<div class="course"><span class="title">교육내용</span></div>
			<c:if test="${fn:length(view.explain) >0}">
				<li>
					<c:out value="${fn:replace(view.explain, lf, '<br/>')}" escapeXml="false"/>
				</li>
			</c:if>
			<c:if test="${view.explainfilereal != null && view.explainfilereal != ''}">
				<a href="/dp/subject/<c:out value="${view.explainfile}"/>" target="_blank">
					<img src="/dp/subject/<c:out value="${view.explainfile}"/>" alt="교육내용" border="0"/>
				</a>
			</c:if>
		</ul>
		<br/>
		<br/>
		<ul class="course" id="subTabContent3" style="display:none;">
			<c:forEach items="${cmmList}" var="result" varStatus="status">
				<li style="background:none;">
					<span class="title">
						${result.name} ${result.ldate} <%-- [${fn2:getFormatDate(result.ldate, 'yyyy.MM.dd')}] --%>
					</span>
				</li>
				<li>
					<c:out value="${fn:replace(result.comments, lf, '<br/>')}" escapeXml="false"/>
				</li>
			</c:forEach>
			<c:if test="${empty cmmList}">
			<li>교육후기가 없습니다.</li>
			</c:if>
			<!-- 페이징 시작 -->
			<li style="background:none;">
				<div class="paging">
					<ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="doLinkPage"/>
				</div>
			</li>
		</ul>
	</div>
</fieldset>
</form>

<!--아래는 팝업과 뷰를 공통으로  스크립트를 프로그램한다.-->
<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

//관심과정 저장
function saveSubjConcern() {
	if(confirm("[${view.subjnm}] 과정을 관심 과정으로 저장하시겠습니까?"))
	{
		thisForm.p_process.value = "insert";
		thisForm.action = "/usr/mpg/concernInfoAction.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
}

//tab menu
function displayTabMenu(num)
{
	for(var i=1; i<=3; i++)
	{
		if(num == i)
		{
			document.all("subTabMenu" + i).style.display = "block";
			document.all("subTabContent" + i).style.display = "block";
		}
		else
		{
			document.all("subTabMenu" + i).style.display = "none";
			document.all("subTabContent" + i).style.display = "none";
		}
	}
}

//수강신청하기
function whenSubjPropose(p_subj, p_year, p_subjseq)
{
	window.open("","subjectProposeStepWindowPopup","width=610,height=800,scrollbars=yes");
	thisForm.p_subj.value = p_subj;
	thisForm.p_year.value = p_year;
	thisForm.p_subjseq.value = p_subjseq;
	thisForm.action = "/usr/subj/subjProposeStep01.do";
	thisForm.target = "subjectProposeStepWindowPopup";
	thisForm.submit();
}

//수강취소하기
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

//-->
</script>
