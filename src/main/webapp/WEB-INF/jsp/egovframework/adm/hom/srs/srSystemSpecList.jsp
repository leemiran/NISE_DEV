<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_tabseq"     value = "${p_tabseq}" />
	<input type = "hidden" name="p_seq"     value = "" />
	<input type = "hidden" name="p_reseq"     value = "" />	
	<input type = "hidden" name="p_type"     value = "${p_type}" />
	
	
	<!-- 검색박스 시작-->
	<div class="searchWrap txtL">
		<div>		
			<ul class="datewrap">
				<li class="floatL">
					<select name="p_search" id="p_search">
						<option value="adtitle"		<c:if test="${p_search eq 'adtitle'}">selected</c:if>>제목</option>
						<option value="adcontents"	<c:if test="${p_search eq 'adcontents'}">selected</c:if>>내용</option>
						<option value="addate"		<c:if test="${p_search eq 'addate'}">selected</c:if>>작성일자</option>
						<option value="adname"		<c:if test="${p_search eq 'adname'}">selected</c:if>>작성자</option>
						<option value="aduserid"	<c:if test="${p_search eq 'aduserid'}">selected</c:if>>아이디</option>
					</select>
					<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" size="50" onkeypress="fn_keyEvent('doPageList')" style="ime-mode:active;"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					요청일자 : 
					<input name="p_search_from" type="text" size="10" maxlength="10" readonly value="${p_search_from}"> 
					<img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_search_from, 'yyyy.mm.dd')" /> ~
                    <input name="p_search_to" type="text" size="10" maxlength="10" readonly value="${p_search_to}"> 
                    <img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_search_to, 'yyyy.mm.dd')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<!--					긴급여부 : 
					<ui:code id="busy_gubun" selectItem="${busy_gubun}" gubun="" codetype="0131"  upper="" title="" className="" type="select" selectTitle="" event="" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
					완료일자 : 
-->
<!-- 
					<input name="p_finish_date" type="text" size="10" maxlength="10" readonly value="${p_finish_date}"> 
					<img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_finish_date, 'yyyy.mm.dd')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
-->						
 					시스템구분 : 
					<ui:code id="sys_gubun" selectItem="${sys_gubun}" gubun="" codetype="0122"  upper="" title="" className="" type="select" selectTitle="" event="" />&nbsp;&nbsp;&nbsp;					

					긴급여부 : 
					<ui:code id="busy_gubun" selectItem="${busy_gubun}" gubun="" codetype="0131"  upper="" title="" className="" type="select" selectTitle="" event="" />&nbsp;&nbsp;&nbsp;

					답변여부 : 
					<select name="answer_gubun">
						<option value="" <c:if test="${answer_gubun eq ''}">selected</c:if>>선택</option>
						<option value="0" <c:if test="${answer_gubun eq '0'}">selected</c:if>>미완료</option>
						<option value="1" <c:if test="${answer_gubun eq '1'}">selected</c:if>>완료</option>
					</select>
					&nbsp;&nbsp;&nbsp;
					<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
				</li>
			</ul>		
		</div>
	</div>
	<!-- 검색박스 끝 -->
	
	<div class="listTop">	
		<div class="btnL">
			<b>전체건수 : ${totCnt} 건</b>
		</div>
		<div class="btnR">
			<a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a>
			<a href="#none" onclick="whenXlsDownLoad1()" class="btn_excel"><span>엑셀출력</span></a>			
		</div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="10px" />
				<col width="50px" />				
				<col width="180px" />
				<col width="50px" />
				<col width="50px" />
				<col width="40px" />
				<col width="40px" />				
				<col width="50px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>시스템구분 </th>
					<th>제목</th>					
					<th>등록자</th>
					<th>등록일</th>
					<th>완료일</th>
					<th>내용확인일시</th>					
					<th>진행율</th>
				</tr>
			</thead>
			<tbody>
			
<!--이벤트 : /ico/ico_event.gif-->
<!--축하 : /ico/ico_happy.gif-->
<!--안내 : /ico/ico_guide.gif-->
<!--설문 : /ico/ico_poll.gif -->
<!--긴급 : /ico/ico_busy.gif-->
<!--기타 : /ico/ico_others.gif			-->

			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td >${result.rn}</td>
					<td >${result.sysGubun}</td>					
					<td class="left">
						<c:if test="${result.busyGubun eq 'A' and result.reSeq eq 0}"><img src="/images/adm/ico/ico_normal.gif" alt="보통" /></c:if>
						<c:if test="${result.busyGubun eq 'B' and result.reSeq eq 0}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
						<c:if test="${result.srLevel > 0}">
							 <c:forEach var="i" begin="1" end="${result.srLevel}"> <!-- scope 생략으로 페이지 영역에 저장 step생략으로 1씩 증가 -->
							 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</c:forEach>							
							<img src="/images/adm/ico/ico_others2.png" alt="" />&nbsp;
						</c:if>
						<a href="#none" onclick="doView('${result.seq}','${result.reSeq}')">${result.reqTitle}</a>
					</td>   
					<td >${result.regName}</td>
					<td >${result.regDate}</td>
					<td >${result.finishDate}</td>					
					<td >
						${fn2:getFormatDate(result.confirmDate, 'yyyy.MM.dd HH:mm:ss')}
					</td>					
					<td >${result.procRate}
						<c:if test="${result.reSeq ne 0}">%</c:if> 
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="20">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- 페이징 시작 -->
	<div class="paging">
		<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
	</div>
	<!-- 페이징 끝 -->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/srs/srSystemSpecList.do";
		frm.target = "_self";
		frm.submit();
	}

	function doLinkPage(index) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/srs/srSystemSpecList.do";
		frm.pageIndex.value = index;
		frm.target = "_self";
		frm.submit();
	}

	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/srs/srSystemSpecInsert.do";
		frm.target = "_self";
		frm.submit();
	}


	function doView(p_seq,p_reseq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_seq.value = p_seq;
		frm.p_reseq.value = p_reseq;		
		frm.action = "/adm/hom/srs/srSystemSpecView.do";
		frm.target = "_self";
		frm.submit();
	}
	
	//엑셀다운로드 
	function whenXlsDownLoad1(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/srs/srSystemSpecListExcelDown.do";
		frm.target = "_self";
		frm.submit();
	}

</script>