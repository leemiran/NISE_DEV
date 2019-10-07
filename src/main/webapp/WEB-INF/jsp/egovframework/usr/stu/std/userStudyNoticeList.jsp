<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrStudyHead.jsp" %>
</head> 

<body>
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2">
		<div class="popCon">
			<!-- header -->
			<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonTop.jsp" %>
			<!-- //header -->
			<!-- contents -->
			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				
				
				<h4 class="tit">공지사항</h4>
				<!-- search wrap-->
				<form name="<c:out value="${gsMainForm}"/>" method="post">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
				<input type="hidden" name="p_seq" id="p_seq" />
				
				<div class="searchWrap">
                    <div class="in">
						<select name="p_search" title="구분">
							<option value="title"    <c:if test="${p_search eq 'title'}">selected</c:if>>제목</option>
							<option value="content"  <c:if test="${p_search eq 'content'}">selected</c:if>>내용</option>
						</select>
						<input type="text" name="p_searchtext" id="p_searchtext" class="t" style="ime-mode:active" onfocus="this.select()" value="${p_searchtext}" title="검색어"/>
						<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
                    </div>
                </div>
				</form>
				<!-- // search wrap -->
				<!-- list -->
				<div class="tbList1">
					<table summary="번호, 제목, 등록자, 등록일자, 조회수로 구분" style="width:100%">
						<caption>시험목록</caption>
						<colgroup>
							<col width="50px" />
							<col width="" />
							<col width="100px" />
							<col width="100px" />
							<col width="90px" />
						</colgroup>
						<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">제목</th>
								<th scope="col">등록자</th>
								<th scope="col">등록일자</th>
								<th scope="col">조회수</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${selectListAll}" var="result">
							<tr>
								<td>전체</td>
								<td class="left"><a href="#" onclick="noticeView('${result.seq}')">${fn2:getFixTitle(result.title, 50)}</a></td>
								<td>${result.gadmin}</td>
								<td>${fn2:getFormatDate(result.addate, 'yyyy.MM.dd')}</td>
								<td>${result.cnt}</td>
							</tr>
						</c:forEach>
						<c:forEach items="${selectList}" var="result" varStatus="i">
							<tr>
								<td>${(fn:length(selectList) + 1) - i.count}</td>
								<td class="left"><a href="#" onclick="noticeView('${result.seq}')">${result.title}</a></td>
								<td>
									<c:if test="${result.gadmin eq 'ZZ'}">${result.name}</c:if>
									<c:if test="${result.gadmin ne 'ZZ'}">${result.gadmin}</c:if>
								</td>
								<td>${fn2:getFormatDate(result.addate, 'yyyy.MM.dd')}</td>
								<td>${result.cnt}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<br/><br/>
				<ul class="btnR">
					<li><a class="btn02" href="#" onclick="window.close()"><span>닫기</span></a></li>
				</ul>
            
		  	</div>
			
			<!-- //contents -->
		</div>
	</div>
</div>

<script type="text/javascript">
<!--
	/* ********************************************************
	 * 조회
	 ******************************************************** */
	function doPageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyNoticeList.do";
		frm.target = "_self";
		frm.submit();
	}

	
	function noticeView(seq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyNoticeView.do";
		frm.p_seq.value = seq;
		frm.target = "_self";
		frm.submit();
	}
	
	
	document.title="공지사항(목록) : 나의 강의실 : 국립특수교육원부설원격교육연수원";	
		
	
-->
</script>
</body>
</html>
