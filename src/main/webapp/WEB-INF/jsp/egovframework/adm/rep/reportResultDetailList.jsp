<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	<input type="hidden" name="p_ordseq" 	id="p_ordseq"	value="${p_ordseq}">
	<input type="hidden" name="p_indate" 	id="p_indate"	value="${view.indate}">
	<input type="hidden" name="p_class" 	id="p_class"		value="${view.class}">
	<input type="hidden" name="p_userid" 	id="p_userid"		value="">
	
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col />
				<col width="200px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="80px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th>과제명</th>
					<th>제출기한</th>
					<th>대상자수</th>
					<th>제출자수</th>
					<th>미제출자수</th>
					<th>만점</th>
					<th>제출기본점</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${view.title}</td>
					<td>${fn2:getFormatDate(view.startdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(view.expiredate, 'yyyy.MM.dd')}</td>
					<td>${view.total}</td>
					<td>${view.submitcnt}</td>
					<td>${view.total - view.submitcnt}</td>
					<td>${view.perfectscore}점</td>
					<td>${view.submitscore}점</td>
				</tr>
			</tbody>
		</table>
	</div>
	<br/>
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doListPage()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="whenExcelExec()"><span>엑셀업로드</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="downloadExcel()"><span>엑셀다운로드</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="whenReportMark(${fn:length(list)})"><span>저장</span></a></div>
	</div>
	<!-- list table-->
	<c:set var="filecnt" value="0"></c:set>
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col width="80px" />
				<col width="150px" />
				<col width="120px" />
				<col width="100px" />
				<col width="100px" />
				<col />
				<col width="80px" />
				<col width="80px" />
				<col width="80px" />
			</colgroup>
			<thead>	
				<tr> 
				 	<th>NO</th>
				  	<th>제출상태</th>
				  	<th>아이디</th>
				  	<th>이름</th>
				  	<th>제출일</th>
				  	<th>추가제출일</th>
				  	<th>제출과제물</th>		  
				  	<th>평가점수</th>
				  	<th>감점</th>
				  	<th>취득점수</th>
		        </tr> 
			</thead>
			<tbody>
			<c:forEach items="${list}"  var="result" varStatus="i">
				<tr>
					<td>${i.count}</td>
					<td>
						<c:choose>
							<c:when test="${result.indate eq 'IN'}">기한내</c:when>
							<c:when test="${result.indate eq 'RE'}">추가기한</c:when>
							<c:when test="${result.indate eq 'OUT'}">기간 외</c:when>
							<c:otherwise>미제출</c:otherwise>
						</c:choose>
					</td> 
					<td>
						<a href="javascript:doViewReport('${result.projid}')">${result.projid}</a>
						<input type="hidden" name="p_projid" value="${result.projid}"/>
					</td>
					<td>${result.name}</td>
					<td>
						<c:if test="${empty result.ldate}">-</c:if>
						<c:if test="${not empty result.ldate}">${fn2:getFormatDate(result.ldate, 'yyyy.MM.dd')}</c:if>
					</td>
					<td>
						<c:if test="${empty result.adddate}">-</c:if>
						<c:if test="${not empty result.adddate}">${result.adddate}</c:if>
					</td>
					<td style="text-align:left;padding-left:5px;">
						<c:if test="${empty result.realfile}">-</c:if>
						<c:if test="${not empty result.realfile}">
		                    <a href="#none" onclick="fn_download('${result.realfile}', '${result.newfile}', 'reportstu/${p_year}/${p_subjseq}/${sessionScope.grcode}/${p_subj}/${result.class}/${p_ordseq}')"><c:out value="${result.realfile}"/></a>
							<c:set var="filecnt" value="${filecnt+1}"/>
						</c:if>
					</td>
					<td>
						<c:set var="minus" value="0.0"></c:set>
						<c:if test="${result.minusday < 0}">
							<c:if test="${(result.minusday * -1) < 6}">
								<%-- <c:set var="minus" value="${result.minusday * 2.5}" /> --%>
								<!-- 20160427 -->
								<%-- <c:set var="minus" value="${result.minusday * 7.5}" /> --%>
								<!-- 20180611 -->
								<c:set var="minus" value="${result.minusday * 5}" />
							</c:if>
							<c:if test="${(result.minusday * -1) >= 6}">
								<c:set var="minus" value="-30" />
							</c:if>
						</c:if>
						<c:if test="${not empty result.getscore and result.getscore ne ''}">
							<input type="text" size="5" name="p_getscore" id="p_getscore${i.count}" value="${result.getscore}" onBlur="getScore(this.value,'${minus}','${i.count}')" onfocus="this.select()" size="4" maxlength="4">
						</c:if>
						<c:if test="${empty result.getscore or result.getscore eq ''}">
							<input type="text" name="p_getscore" id="p_getscore${i.count}" value="0.0" onBlur="getScore(this.value,'${minus}','${i.count}')" onfocus="this.select()" size="4" maxlength="4">
						</c:if>
					</td>
					<td>
						<c:if test="${not empty result.minusscore}">
							<%-- <input type="hidden" name="p_minusscore" id="p_minusscore${i.count}" value="${result.minusscore}"> --%>
							<input type="hidden" name="p_minusscore" id="p_minusscore${i.count}" value="${minus}">
						</c:if>
						<c:if test="${empty result.minusscore}">
							<input type="hidden" name="p_minusscore" id="p_minusscore${i.count}" value="0">
						</c:if>
						
						<c:if test="${result.minusday < 0 }"><c:out value="${minus}" /></c:if>
						<c:if test="${result.minusday >= 0 }">0</c:if>
						<br/>
						(<c:out value="${result.minusday < 0 ? result.minusday * -1 : 0}"/>일)
					</td>
					<td>
						
						<input name="p_finalscore" id="p_finalscore${i.count}" type="text" readonly	class="text" size="4" maxlength="4" value="<c:out value="${result.scoreexists eq 'N' ? result.notsubmitscore : result.finalscore}"/>">
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="8">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
		<input type="hidden" name="p_file_cnt" value="<c:out value="${filecnt}"/>" />
	</div>
	<!-- list table-->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	//learch 함수
	function doListPage() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/rep/reportResultList.do";
		frm.target = "_self";
		frm.submit();
	}
    
	function getScore(objval, minus, idx){
		if( parseFloat(objval) > 100 ){
			alert('점수는 100점 이상 입력할수 없습니다.');
    		return;
		}
		var  pobj = document.getElementById("p_finalscore"+idx);
		pobj.value = parseFloat(objval) + parseFloat(minus);
	}

	function whenReportMark(size){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( frm.p_indate.value == "Y" ){
			if( !confirm("과제 제출기간입니다. 계속진행하시겠습니까?") ){
				return;
			}
		}
		if( size == 0 ){
			alert("과제 제출한 수강생이 없습니다.");
			return;
		}

		frm.action = "/adm/rep/reportResultUpdateData.do";
		frm.target = "_self";
		frm.submit();
	}

	function downloadExcel(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/rep/reportResultExcelList.do";
		frm.target = "_self";
		frm.submit();
	}

	function whenExcelExec(){
	    var winWidth = 580;
	    var winHeight = 430;
	    var popHorizontal = screen.width/2 - winWidth/2;
	    var popLongitudinal = screen.height/2 - winHeight/2;
	    var frm = eval('document.<c:out value="${gsMainForm}"/>');
	    window.open("","excelPop","width="+ winWidth +",height="+ winHeight +",top="+ popLongitudinal +",left="+ popHorizontal +",toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=auto,resizable=no,copyhistory=no");
	  
		frm.action = "/adm/rep/reportResultExcelInsertPage.do";
		frm.target = "excelPop";
		frm.submit();
	    document.form1.target = "excelPop";
	    document.form1.action = "/servlet/controller.learn.ReportResultServlet";
			document.form1.p_process.value="excelPage";
	  	document.form1.submit();
	}

	function doViewReport(p_userid)
	{
		   var winWidth = 800;
		   var winHeight = 600;
		   var popHorizontal = screen.width/2 - winWidth/2;
		   var popLongitudinal = screen.height/2 - winHeight/2;
		   var frm = eval('document.<c:out value="${gsMainForm}"/>');
		   window.open("","ReportWindowPop","width="+ winWidth +",height="+ winHeight +",top="+ popLongitudinal +",left="+ popHorizontal +",toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,copyhistory=no");

		 	frm.p_userid.value = p_userid;
			frm.action = "/adm/rep/reportViewPage.do";
			frm.target = "ReportWindowPop";
			frm.submit();
	}
</script>





















