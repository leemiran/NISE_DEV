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
				
				<form name="<c:out value="${gsMainForm}"/>" method="post">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
				</form>
				<h4 class="tit">공지사항</h4>
				<div class="tbDetail">
					<table summary="작성자, 제목, 등록일, 내용, 첨부파일로 구성" cellspacing="0" width="100%">
						<caption>${selectGong.title}</caption>
						<colgroup>
							<col width="18%" />
							<col width="" />
							<col width="18%" />
							<col width="25%" />
						</colgroup>
						<tbody>
							<tr>
								<th scope="col">작성자</th>
								<td>${selectGong.gadmin}</td>
								<th scope="col">등록일</th>
								<td>${fn2:getFormatDate(selectGong.addate, 'yyyy.MM.dd')}</td>
							</tr>
							<tr class="title">
								<th scope="col">제목</th>
								<td colspan="3">${selectGong.title}</td>
							</tr>
							<tr class="title">
								<th scope="col">내용</th>
								<td colspan="3">
								<div style="height:300px; overflow: auto;">
								<c:out value="${selectGong.adcontent}" escapeXml="false"/>
								</div>
								</td>
							</tr>
							<tr class="title">
								<th scope="col">첨부파일</th>
								<td colspan="3">
									<c:if test="${empty selectGong.upfile}">첨부파일이 없습니다.</c:if>
									<c:if test="${not empty selectGong.upfile}">
										<a href="#none" onclick="fn_download('${selectGong.realfile}', '${selectGong.upfile}', 'bulletin')"><c:out value="${selectGong.realfile}"/></a>
									</c:if>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<ul class="btnR">
					<li><a class="btn02" href="#" onclick="doPageList()"><span>목록</span></a></li>
					<c:if test="${popupClose eq 'Y'}">
					<li><a class="btn02" href="#" onclick="window.close()"><span>닫기</span></a></li>
					</c:if>
				</ul>
			
		  	</div>
			<!-- //contents -->
		</div>
	</div>
</div>
</body>
</html>
<script type="text/javascript">

	/* ********************************************************
	 * 조회
	 ******************************************************** */
	function doPageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyNoticeList.do";
		frm.target = "_self";
		frm.submit();
	}
	document.title="<c:out value='${selectGong.title}' />";
</script>
