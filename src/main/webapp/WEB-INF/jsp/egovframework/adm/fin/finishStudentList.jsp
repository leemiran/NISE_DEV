<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>
<style>
table.line td{border:0px;}
</style>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_action" 	id="p_action">
	
	<input type="hidden" name="p_subj" 			id="p_subj"			value="${p_subj}">
	<input type="hidden" name="p_year" 			id="p_year"			value="${p_year}">
	<input type="hidden" name="p_subjseq" 		id="p_subjseq"		value="${p_subjseq}">
	<input type="hidden" name="p_isclosed" 		id="p_isclosed"		value="${p_isclosed}">
	<input type="hidden" name="p_subjnm" 		id="p_subjnm"		value="${p_subjnm}">
	<input type="hidden" name="p_isonoff" 		id="p_isonoff"		value="${p_isonoff}">
	<input type="hidden" name="p_isapproval" 	id="p_isapproval"	value="${p_isapproval}">
	<input type="hidden" name="p_subjseqgr" 	id="p_subjseqgr"	value="${p_subjseqgr}">
	<input type="hidden" name="p_stolddate" 	id="p_stolddate"	value="${p_stolddate}">
	<input type="hidden" name="p_mgubun" 		id="p_mgubun"		value="${p_mgubun}">
	
	
	<input type="hidden" name="p_connum" 		id="p_connum"		value="${p_connum}">
	
	
	<input type="hidden" name="p_userid" 		id="p_userid">
	<input type="hidden" name="p_isgraduated" 	id="p_isgraduated">
	<input type="hidden" name="p_process" 		id="p_process">
	<input type="hidden" name="p_studentlist" 	id="p_studentlist">
	<input type="hidden" name="p_emp_gubun" 	id="p_emp_gubun">
	<input type="hidden" name="p_scoreYn" 	id="p_scoreYn">
	<input type="hidden" name="p_upperclass" 	id="p_upperclass" value="${subjseqInfo.upperclass}">
	
	<input type="hidden" name="p_schoolparent" 	id="p_schoolparent" value="${subjseqInfo.schoolparent}">
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
            </colgroup>
            <tbody>
                <tr class="title">
                    <th>교육그룹</th>
                    <td><c:out value="${subjseqInfo.grcodenm}"/></td>
                    <th>년도</th>
                    <td><c:out value="${subjseqInfo.gyear}"/>년</td>
                </tr>
                <tr class="title">
                    <th>과정명</th>
                    <td><c:out value="${subjseqInfo.subjnm}"/></td>
                    <th>기수</th>
                    <td><c:out value="${fn2:toNumber(p_subjseq)}"/></td>
                </tr>
                <tr class="title">
                    <th>수료처리완료</th>
                    <td><c:out value="${p_isclosed eq 'Y' ? '처리완료' : '미처리'}"/></td>
                    <th>위탁과정여부</th>
                    <td><c:out value="${subjseqInfo.isoutsourcing}"/></td>
                </tr>
            </tbody>
        </table>
    </div>
    <!-- // detail wrap -->
    
    <c:if test="${p_isonoff eq 'ON'}">
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="15%" />
                <col width="20%" />
                <col width="15%" />
                <col width="15%" />
                <col width="20%" />
            </colgroup>
            <tbody>
                <tr class="title">
                    <th>교육기간</th>
                    <td colspan="5">
                    	<c:out value="${fn2:getFormatDate(subjseqInfo.edustart, 'yyyy.MM.dd')}"/>
                    	~
                    	<c:out value="${fn2:getFormatDate(subjseqInfo.eduend, 'yyyy.MM.dd')}"/>
                    </td>
                </tr>
                <c:if test="${subjseqInfo.isoutsourcing eq 'N'}">
                <tr class="title">
					<th class="table_title" rowspan="7">수료기준</th>
					<td>총점</td>
					<td colspan="4">${subjseqInfo.gradscore}점 이상</td>
				</tr>
				<tr class="title">
					<td>참여도(학습진도율)</td>
					<td>${subjseqInfo.wetc1}점 이상</td>
					<th rowspan="6">가중치(%)</th>
					<td>참여도(학습진도율)</td>
					<td>${subjseqInfo.wetc2}%</td>
				</tr>
				<tr class="title">
					<td>출석시험</td>
					<td>${subjseqInfo.gradexam}점 이상</td>
					<td>출석시험</td>
					<td>${subjseqInfo.wmtest}%</td>
				</tr>
				<tr class="title">
					<td>온라인시험</td>
					<td>${subjseqInfo.gradftest}점 이상</td>
					<td>온라인시험</td>
					<td>${subjseqInfo.wftest}%</td>
				</tr>
				<tr class="title">
					<td>온라인과제</td>
					<td>${subjseqInfo.gradreport}점 이상</td>
					<td>온라인과제</td>
					<td>${subjseqInfo.wreport}%</td>
				</tr>
                </c:if>
            </tbody>
        </table>
    </div>
    </c:if>
    
    <font color="red">
    * 이수시간이 60 시간 이상의 경우에만 조정산정점수 가능.<br/>
	* 원점수 총점 60점 미만은 경우 과락, 등수 및 조정점수 산정하지 않음.
	</font>
	<br/>
	<table width="100%">
		<tr>
			<td align="left">
				<b>수료: ${subjseqInfo.stoldycnt}명</b>
			</td>
			<td align="right">
				<c:if test="${empty subjseqInfo.subjGu}">
					<c:set var="isComplete" value="0"/>
					<c:if test="${not empty subjseqInfo.eduend and subjseqInfo.today > fn2:getFormatDate(subjseqInfo.eduend, 'yyyyMMdd')}">
					<c:set var="isComplete" value="1"/>
					</c:if>
					<c:if test="${isComplete eq 1}">
						<!-- 1. 외주업체 과정인 경우 -->
						<c:if test="${subjseqInfo.isoutsourcing eq 'Y'}">
							<!-- 1.1.1 업체가 결과를 입력했고  -->
							<c:if test="${subjseqInfo.iscpresult eq 'Y'}">
								<!-- 1.1.1.1 수료처리를 하지 않은 경우 -->
								<c:if test="${p_isclosed eq 'N'}">
									<a href="#" class="btn01_off" onclick="whenComplete('outSubjIsClosed');"><span>수료처리</span></a>
									<a href="#" class="btn01_off" onclick="whenOutSubjReject();"><span>결과재요청</span></a>
								</c:if>
									<c:if test="${not empty subjseqInfo.stolddate}">
										<c:if test="${fn2:getFormatDate(subjseqInfo.stolddate, 'yyyyMMdd') >= subjseqInfo.today}">
											<a href="#" class="btn01_off" onclick="whenCancel('subjectCompleteCancel');"><span>수료처리취소</span></a>
										</c:if>
									</c:if>
							</c:if>
						</c:if>
						<c:if test="${subjseqInfo.isoutsourcing ne 'Y'}">
							<!-- 2. 외주업체 과정이 아닌경우 -->
							<!-- 2.1.2 수료처리를 하지 않은 경우 -->
							<c:if test="${p_isclosed eq 'N'}">
								<c:if test="${p_isonoff eq 'RC'}">
									<a href="#" class="btn01_off" onclick="whenComplete('bookSubjectComplete');"><span>수료처리</span></a>
								</c:if>
							</c:if>
								<!-- 2.1.3 수료처리를 한 경우 -->
								<c:if test="${not empty subjseqInfo.stolddate}">
									<c:if test="${fn2:getFormatDate(subjseqInfo.stolddate, 'yyyyMMdd') >= subjseqInfo.today}">
										<a href="#" class="btn01_off" onclick="whenCancel('subjectCompleteCancel');"><span>수료처리취소</span></a>
									</c:if>
								</c:if>
						</c:if>
					</c:if>
					<c:if test="${isComplete ne 1}">
						<c:if test="${subjseqInfo.isoutsourcing ne 'Y'}">
							<!-- 점수재계산 -->
							<a href="#" class="btn01_off" onclick="ReRating();"><span>점수재계산</span></a>
						</c:if>
					</c:if>
					
				</c:if>
			</td>
		</tr>
	</table>
	
	
	<div class="listTop">	
	
		<div class="btnR"><a href="#none" class="btn01_off" onclick="listpage();"><span>목록</span></a></div> 
		<div class="btnR"><a href="#none" class="btn01_off" onclick="excelPrint();"><span>Excel출력</span></a></div>
		<div class="btnR"><a href="#none" class="btn01_off" onclick="suRoyJeung('');"><span>수료증일괄출력</span></a></div>
		<!--  div class="btnR">
			<c:set var="lSize" value="${fn:length(list)}"/>
			<c:set var="printSize" value="40"/>
			<c:set var="xx" value="${((lSize-1)/printSize) + 1}"/>
			<select name="parintcnt">
			<c:forEach begin="1" end="${xx}" varStatus="i">
				<option value="${i.count * printSize}">${((i.count-1)*printSize)} ~ ${(i.count*printSize)}</option>
			</c:forEach>
			</select>
		</div-->
		<div class="btnR"><a href="#none" class="btn01_off" onclick="whenComplete('subjectComplete');"><span>수료처리</span></a></div>
		<!-- 학부모 과정인 경우 조정점수 산정 -->
		<c:if test="${fn:substring(p_subj, 0, 3) ne 'PAR'}">
			<!-- 이수시간이 60시간 이상인 경우에만 조정 산점 점수를 할수 있도록 한다. -->
			<c:if test="${subjseqInfo.edutimes >= 60}">
				<div class="btnR"><a href="#none" class="btn01_off" onclick="whenComplete3('subjectComplete3');"><span>조정점수산정</span></a></div>
			</c:if>
		</c:if>
	</div>
	
	
	
	<c:if test="${fn:substring(p_subj, 0, 3) ne 'PAR' and subjseqInfo.edutimes >= 60}">
	<div class="tbList">
	<table summary="" cellspacing="0" width="100%" class="line">
		<tr>
			<c:forEach items="${ScoreCntList}" var="result">
			<td>
				<table border="1" width="100%">
					<tr>
						<th class="table_title">${result.editscore}점</th>
					</tr>
					<tr>
						<td>${result.cnt}명</td>
					</tr>
				</table>
			</td>
			</c:forEach>
		</tr>
	</table>
	</div>
	</c:if>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col width="120px" />
				<col width="100px" />
				<!--<col width="110px" />-->
				<col />
				<col width="70px" />
				<!-- <col width="70px" /> -->
				<col width="70px" />
				<col width="70px" />
				<col width="70px" />
				<col width="70px" />
				<col width="40px" />
				<col width="40px" />
				<c:if test="${subjseqInfo.edutimes >= 60 and fn:substring(p_subj, 0, 3) eq 'PAR'}">
				<col width="60px" />
				</c:if>
				<col width="70px" />
				<col width="70px" />
				<col width="70px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>ID</th>
					<th>성명</th>
					<th>근무지</th>
					<th>진도율(%)</th>
					<!-- <th>참여도<br/>(일)</th> -->
					<th>참여도<br/>평가</th>
					<th>온라인<br/>시험</th>
					<th>온라인<br/>과제</th>
					<th>출석<br/>시험</th>					
					<th>총점</th>
					<th>등수</th>
					<c:if test="${subjseqInfo.edutimes >= 60 and fn:substring(p_subj, 0, 3) ne 'PAR'}">
					<th>조정<br/>점수</th>
					</c:if>
					<th>이수<br/>여부</th>
					<th>이수<br/>번호</th>
					<th>평생학급<br/>계좌전송</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>${i.count}</td>
					<td>${result.userid}</td>
					<td>${result.name}</td>
					<td><c:out value="${result.userPath}" /></td>
					
					<td><c:out value="${result.tstep}" />%</td>
					<%-- <td><c:out value="${result.etc1}" /></td> --%>
					<%-- <td><c:out value="${result.avetc2}" /></td> --%>
					<td><c:out value="${result.etc2}" /></td>
					<td><c:out value="${result.avftest}" /></td>
					<td><c:out value="${result.avreport}" /></td>
					<td><c:out value="${result.avmtest}" /></td>					
					<td><c:out value="${result.score}" /></td>
					<td><c:out value="${result.ranking}" /></td>
					<c:if test="${subjseqInfo.edutimes >= 60 and fn:substring(p_subj, 0, 3) ne 'PAR'}">
					<td><c:out value="${result.editscore}" /></td>
					</c:if>
					<td>
						<c:if test="${subjseqInfo.isoutsourcing eq 'Y' or p_isclosed eq 'Y'}">
							<c:if test="${result.isgraduated eq 'Y'}">
								<c:if test="${subjseqInfo.isoutsourcing eq 'N'}">
									<a href="#none" onclick="whenChangeIsGradu('${result.userid}', 'N');">수료</a>
								</c:if>
								<c:if test="${subjseqInfo.isoutsourcing ne 'N'}">
									수료
								</c:if>
							</c:if>
							<c:if test="${result.isgraduated ne 'Y'}">
								<c:if test="${subjseqInfo.isoutsourcing eq 'N'}">
									<c:if test="${not empty result.notgraducd}">
										<a href="#none" onclick="whenChangeIsGradu('${result.userid}', 'Y');">미수료</a>
									</c:if>
									<c:if test="${empty result.notgraducd}">
										<a href="#none" onclick="whenChangeIsGradu('${result.userid}', 'Y');">미처리</a>
									</c:if>
								</c:if>
								<c:if test="${subjseqInfo.isoutsourcing ne 'N'}">
									<a href="#none" onclick="whenChangeIsGradu('${result.userid}', 'Y');">미수료</a>
								</c:if>
							</c:if>
						</c:if>
						<c:if test="${subjseqInfo.isoutsourcing ne 'Y' and p_isclosed ne 'Y'}">
							미처리
						</c:if>
					</td>
					<td>
						<c:if test="${result.isgraduated eq 'Y'}">
							<a href="#none" onclick="suRoyJeung('${result.userid}', '${result.empGubun }');">${result.serno}</a>
						</c:if>
					</td>
					<td>${result.lifetimeYn}<c:if test="${not empty result.lifetimeResult and 'N' eq result.lifetimeYn}"> (${result.lifetimeResult})</c:if></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="14">조회된 내용이 없습니다.</td>
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
	function listpage() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/fin/finishCourseList.do";
		frm.p_action.vlaue = "go";
		frm.target = "_self";
		frm.submit();
	}

	function whenChangeIsGradu(userid, isgraduated){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/fin/finishGraduatedUpdate.do";
		frm.p_userid.value = userid;
		frm.p_isgraduated.value = isgraduated;
		frm.target = "_self";
		frm.submit();
	}

	//수료처리
	function whenComplete(process) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( parseInt('<c:out value="${subjseqInfo.edustart}"/>') < 2010050100 ){
	        alert("2010년05월 이전 데이터는 수료처리 할 수 없습니다.");
	        return;
		} 
		var connum = frm.p_connum.value;
		//학부모 과정인 경우 조정점수 산정 
		if( <c:out value="${subjseqInfo.edutimes >= 60 and fn:substring(p_subj, 0, 3) eq 'PAR'}"/> ){
			if( connum != 1 ){
				alert("조정점수산청을 먼저 실행하세요!!");
				return;
			}
		}
		if( confirm("수료처리를 하시겠습니까?") ){
			frm.action = "/adm/fin/finishCompleteUpdate.do";
			frm.p_process.value = process;
			frm.target = "_self";
			frm.submit();
		}
	}

	//엑셀출력
	function excelPrint(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/fin/finishStudentExcelList.do";
		frm.target = "_self";
		frm.submit();
	}

	//수료취소
	function whenCancel(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( parseInt('<c:out value="${subjseqInfo.edustart}"/>') < 2010050100 ){
	        alert("2010년05월 이전 데이터는 수료취소를 할 수 없습니다.");
	        return;
		} 

		if( confirm("수료취소를 하시겠습니까?") ){
			frm.action = "/adm/fin/completeCancelUpdate.do";
			frm.target = "_self";
			frm.submit();
		}
	}

	//결과재요청
	function whenOutSubjReject(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if (confirm("결과입력을 재요청 하시겠습니까?")) {
			frm.action = "/adm/fin/completeOutSubjReject.do";
			frm.target = "_self";
			frm.submit();
		}
	}

	function ReRating(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		if( parseInt('<c:out value="${subjseqInfo.edustart}"/>') < 2010050100 ){
	        alert("2010년05월 이전 데이터는 점수재계산을  할 수 없습니다.");
	        return;
		} 
		

		frm.p_studentlist.value = "1";   //subjectSelect

		if (confirm("점수재계산 하시겠습니까?")) {
			frm.action = "/adm/fin/subjectCompleteRerating.do";
			frm.target = "_self";
			frm.submit();
			
		}
	}

	function whenComplete3(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		if( parseInt('<c:out value="${subjseqInfo.edustart}"/>') < 2010050100 ){
	        alert("2010년05월 이전 데이터는 조정점수산정처리 할 수 없습니다.");
	        return;
		} 
	  
		if (confirm("조정점수 산청처리를  하시겠습니까?")) {
			frm.action = "/adm/fin/subjectComplete3.do";
			frm.target = "_self";
			frm.submit();
		}
	}
	
	//수료증출력
	function suRoyJeung(userid, emp){
	
		
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		if(confirm("이수증에 성적보기를 원하시면 확인, 원하지 않으시면 취소를 눌러주세요.")){
			window.open('', 'suRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
			frm.p_userid.value = userid;
			frm.p_scoreYn.value = "Y";
			frm.p_emp_gubun.value = emp;
			frm.action = "/adm/fin/suRoyJeungPrint.do";
			frm.target = "suRoyJeungPop";			
		}else{
			window.open('', 'suRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
			frm.p_userid.value = userid;
			frm.p_scoreYn.value = "N";
			frm.p_emp_gubun.value = emp;
			frm.action = "/adm/fin/suRoyJeungPrint.do";
			frm.target = "suRoyJeungPop";
			
		}
		frm.submit();
	}

	
</script>