<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_action" 	id="p_action">
	
	<input type="hidden" name="p_subj" 			id="p_subj">
	<input type="hidden" name="p_year" 			id="p_year">
	<input type="hidden" name="p_subjseq" 		id="p_subjseq">
	<input type="hidden" name="p_isclosed" 		id="p_isclosed">
	<input type="hidden" name="p_subjnm" 		id="p_subjnm">
	<input type="hidden" name="p_isonoff" 		id="p_isonoff">
	<input type="hidden" name="p_isapproval" 	id="p_isapproval">
	<input type="hidden" name="p_subjseqgr" 	id="p_subjseqgr">
	<input type="hidden" name="p_stolddate" 	id="p_stolddate">
	<input type="hidden" name="p_mgubun" 		id="p_mgubun">
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col width="180px" />
				<col />
				<col width="60px" />
				<col width="60px" />
				<col width="140px" />
				<col width="60px" />
				<col width="60px" />
				<col width="60px" />
				<col width="100px" />
				<col width="110px" />
				<col width="60px" />
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th rowspan="2">No</th>
					<th rowspan="2">패키지</th>
					<th rowspan="2">과정명</th>
					<th rowspan="2">기수</th>
					<th rowspan="2">교육구분</th>
					<th rowspan="2">교육기간</th>
					<th colspan="3">인원</th>
					<th rowspan="2">수료처리</th>
					<th rowspan="2">최근재계산일자</th>
					<th rowspan="2">이수증출력</th>
					<th rowspan="2">평생학습계좌</th>
					<th rowspan="2">평생학습계좌 전송 수</th>
				</tr>
				<tr>
					<th>교육</th>
					<th>수료</th>
					<th>미수료</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>${i.count}</td>
				<c:if test="${result.course eq '000000'}">
					<td class="left" colspan="2">
					<c:if test="${result.studentcnt == 0}">
						<a href="#none" onclick="alert('수강생이 없습니다.');">${result.subjnm}</a>
					</c:if>
					<c:if test="${result.studentcnt != 0}">
						<a href="#none" onclick="selectSubj(
								'${result.subj}',
								'${result.year}',
								'${result.subjseq}',
								'${result.isclosed}',
								'${result.subjnm}',
								'${result.isonoff}',
								'${result.approvalstatus}',
								'${result.subjseqgr}',
								'${result.stolddate}',
								'${result.mgubun}',
								'${result.recalcudate}'
							);">${result.subjnm}</a>
					</c:if>
					</td>
				</c:if>
				<c:if test="${result.isnewcourse eq 'Y'}">
					<td class="left" rowspan="${result.rowspan}">
						${result.coursenm} ${result.courseseq}
					</td>
				</c:if>
				<c:if test="${result.course ne '000000'}">
					<td class="left">
					<c:if test="${result.studentcnt == 0}">
						<a href="#none" onclick="alert('수강생이 없습니다.');">${result.subjnm}</a>
					</c:if>
					<c:if test="${result.studentcnt != 0}">
						<a href="#none" onclick="selectSubj(
								'${result.subj}',
								'${result.year}',
								'${result.subjseq}',
								'${result.isclosed}',
								'${result.subjnm}',
								'${result.isonoff}',
								'${result.approvalstatus}',
								'${result.subjseqgr}',
								'${result.stolddate}',
								'${result.mgubun}',
								'${result.recalcudate}' 
							);">${result.subjnm}</a>
					</c:if>
					</td>
				</c:if>
					<td>${fn2:toNumber(result.subjseq)}</td>
					<td>${result.isonoffval}</td>
					<td>${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
					<td>${result.studentcnt}</td>
					<td>
						<c:set var="cnt2" value="0"/>
						<c:set var="cnt3" value="0"/>
						<c:if test="${result.isclosed eq 'Y'}">
							<c:set var="cnt2" value="${result.stoldycnt}"/>
							<c:set var="cnt3" value="${result.stoldncnt}"/>
						</c:if>
						<c:if test="${result.isclosed ne 'Y'}">
							<c:set var="cnt2" value="${result.stoldycnt1}"/>
							<c:set var="cnt3" value="${result.stoldncnt1}"/>
						</c:if>
						<c:out value="${cnt2}"/>
					</td>
					<td>
						<c:if test="${cnt2 != 0}">${result.studentcnt-cnt2}</c:if>
						<c:if test="${cnt2 == 0}">0</c:if>
					</td>
					<td>
						<c:set var="isComplete" value="0"/>
						<c:set var="checkdate" value="${result.edustart}"/>
						<c:if test="${not empty result.eduend}">
							<c:if test="${fn2:toNumber(result.today) > fn2:toNumber(fn2:getFormatDate(result.eduend, 'yyyyMMdd'))}">
							<c:set var="isComplete" value="1"/>
							</c:if>
						</c:if>
						<c:if test="${isComplete eq 1}"> <!-- 교육종료일이 지난경우 --> 
							<c:if test="${result.isclosed eq 'Y'}"> <!-- 수료처리 완료 -->
								<!-- 수료처리일+1 까지 수료처리취소 버튼 확인 -->
								<c:if test="${not empty result.stolddate && fn2:getFormatDate(result.stolddate, 'yyyyMMdd') >= result.today}">
									<!-- 외주업체가 아닌경우 -->
									<c:if test="${result.isoutsourcing eq 'N'}">
										<c:if test="${result.isonoff eq 'ON' or result.isonoff eq 'RC'}">
											<!-- 2010년도 05월 이전 데이터 라면 점수계산및 수료처리를 못하게 함. -->
											<!-- why : 데이터 마이그레이션 안됨(협의된 사항임) -->
											<a href="#none" onclick="whenCancel('${result.subj}', '${result.year}', '${result.subjseq}', '${result.studentcnt}', '${checkdate}');"><img src='/images/adm/button/b_surucancel.gif'></a>
										</c:if>
									</c:if>
									<c:if test="${result.isoutsourcing ne 'N'}">
										<!-- 2010년도 05월 이전 데이터 라면 점수계산및 수료처리를 못하게 함. -->
										<!-- why : 데이터 마이그레이션 안됨(협의된 사항임) -->
										<a href="#none" onclick="whenCancel('${result.subj}', '${result.year}', '${result.subjseq}', '${result.studentcnt}', '${checkdate}');"><img src='/images/adm/button/b_surucancel.gif'></a>
									</c:if>
								</c:if>
								<c:if test="${empty result.stolddate or fn2:getFormatDate(result.stolddate, 'yyyyMMdd') < result.today}">
									<img src='/images/adm/button/b_surucomplete.gif' alt='수료완료'>
								</c:if>
							</c:if>
							<c:if test="${result.isclosed ne 'Y'}">
								<c:if test="${result.isoutsourcing eq 'N'}">
									<c:if test="${result.isonoff eq 'ON' or result.isonoff eq 'RC'}">
										<!-- 2010년도 05월 이전 데이터 라면 점수계산및 수료처리를 못하게 함. -->
										<!-- why : 데이터 마이그레이션 안됨(협의된 사항임) -->
										<a href="#none" onclick="ReRating('${result.subj}','${result.year}','${result.subjseq}','${result.isonoff}', '${result.studentcnt}','${checkdate}');">
											<img src='/images/adm/button/b_rerating.gif'>
										</a>
									</c:if>
								</c:if>
								<c:if test="${result.isoutsourcing ne 'N'}">-</c:if>
							</c:if>
						</c:if>
						<c:if test="${isComplete ne 1}">
							<!-- 수료처리 미완료 -->
							<c:if test="${result.isclosed eq 'N'}">
								<!-- 외주업체가 아닌경우 -->
								<c:if test="${result.isoutsourcing eq 'N'}">
									<a href="#none" onclick="ReRating('${result.subj}','${result.year}','${result.subjseq}','${result.isonoff}', '${result.studentcnt}','${checkdate}');">
										<img src='/images/adm/button/b_rerating.gif' alt='점수재계산'>
									</a>
								</c:if>
								<c:if test="${result.isoutsourcing ne 'N'}">-</c:if>
							</c:if>
							<c:if test="${result.isclosed ne 'N'}">
								<img src='/images/adm/button/b_surucomplete.gif' alt='수료완료'>
							</c:if>
						</c:if>
					</td>
					<td>
						${fn2:getFormatDate(result.recalcudate, 'yyyy.MM.dd hh:mm:ss')}
					</td>
					<td>
						<a href="#none" onclick="fnSuroyprint('${result.subj}','${result.year}','${result.subjseq}');">${result.suroyprint}</a>
						<input type="hidden" name="p_suroyprint" value="${result.suroyprint}">
					</td>
					<td>
						<c:choose>
							<c:when test="${result.isclosed eq 'Y'}">
									<c:if test="${result.lifetimeYCnt > 0}">								
										<a href="#none" onclick="lifetimeComplete('${result.subj}','${result.year}','${result.subjseq}');" class="btn01"><span>전송완료</span></a>
									</c:if>
									<c:if test="${result.lifetimeYCnt == 0}">								
										<a href="#none" onclick="lifetimeComplete('${result.subj}','${result.year}','${result.subjseq}');" class="btn01"><span>전송</span></a>
									</c:if>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>${result.lifetimeYCnt }</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="13" style="width:100%;">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/fin/finishCourseList.do";
		frm.p_action.vlaue = "go";
		frm.target = "_self";
		frm.submit();
	}

	function selectSubj(subj, year, subjseq, isclosed, subjnm, isonoff, isapproval, subjseqgr, stolddate, mgubun, recalcudate){
		if(recalcudate =="" ){
           alert("점수재계산을 먼저 실행 하세요!");
           return;
		}
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_subj.value    		= subj;
		frm.p_year.value    		= year;
		frm.p_subjseq.value 		= subjseq;
		frm.p_isclosed.value 		= isclosed;
		frm.p_subjnm.value 			= subjnm;
		frm.p_isonoff.value 		= isonoff;
		frm.p_isapproval.value 		= isapproval;
		frm.p_subjseqgr.value 		= subjseqgr;
		frm.p_stolddate.value 		= stolddate;
		frm.p_mgubun.value 			= mgubun;
		frm.action = "/adm/fin/finishStudentList.do";
		frm.target = "_self";
		frm.submit();
	}

	function whenCancel(subj, year, subjseq, student, checkdate){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( parseInt('<c:out value="${subjseqInfo.edustart}"/>') < 2010050100 ){
	        alert("2010년05월 이전 데이터는 수료취소를 할 수 없습니다.");
	        return;
		}
		
		if (student==0) {
			alert("수강생이 없습니다.");
			return;
		}
		if (confirm("수료처리를 취소 하시겠습니까?")) {
			frm.p_subj.value = subj;
			frm.p_year.value = year;
			frm.p_subjseq.value = subjseq;
			frm.action = "/adm/fin/finishStudentRerating.do";
			frm.target = "_self";
			frm.submit();
		}
	}

	function ReRating(subj, year, subjseq, isonoff, student, checkdate){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( parseInt('<c:out value="${subjseqInfo.edustart}"/>') < 2010050100 ){
	        alert("2010년05월 이전 데이터는 점수를 재계산 할 수 없습니다.");
	        return;
		}
		
		if (student==0) {
			alert("수강생이 없습니다.");
			return;
		}
		if (confirm("점수재계산 하시겠습니까?")) {
			frm.p_subj.value = subj;
			frm.p_year.value = year;
			frm.p_subjseq.value = subjseq;
			frm.action = "/adm/fin/subjectCompleteRerating.do";
			frm.target = "_self";
			frm.submit();
		}
	}
	
	function fnSuroyprint(subj, year, subjseq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		
		var p_suroyprint = frm.p_suroyprint.value;		
		
		if(p_suroyprint=='Y'){
			frm.p_suroyprint.value = 'N';
		}else{
			frm.p_suroyprint.value = 'Y';
		}
		
		
		if (confirm("이수증 출력을 수정하시겠습니까?")) {
			frm.p_subj.value = subj;
			frm.p_year.value = year;
			frm.p_subjseq.value = subjseq;
			frm.action = "/adm/fin/suroyprintYnUpdate.do";
			frm.target = "_self";
			frm.submit();
		}
	}
	
	function lifetimeComplete(subj, year, subjseq) {
		$.ajax({
			url: '${pageContext.request.contextPath}/lifetime/putSubj.do'
			, type: 'post'
			, data: {
				subj: subj
				, year: year
				, subjseq: subjseq
			}
			, dataType: 'json'
			, success: function(result) {
				var msg = '';
				if(result.failCnt < 1) {
					var msg = '평생학습계좌로 이수정보가 등록되었습니다.'
				} else {
					var msg = result.successCnt + '건 연동성공, ' + result.failCnt + '건이 연동실패 했습니다. 다시 시도해주세요.';
				}
				
				if(result.noneMember > 0) {
					msg += '\n평생학습계좌 비연동회원: ' + result.noneMember + '명';
				}
				alert(msg);
			}
			, error: function(xhr, status, error) {
				console.log(status);
				console.log(error);   
			}
		});
	}

</script>