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
				<input type="hidden" name="p_tabseq" id="p_tabseq"  value="${p_tabseq}"/>
				<input type="hidden" name="p_seq" id="p_seq"  value="${p_seq}"/>
				<input type="hidden" name="pageIndex" 	id="pageIndex"	value="${pageIndex}">
				
				<h4 class="tit">Q&amp;A</h4>
				<div class="tbDetail">
					<table summary="작성자, 등록일, 제목, 내용, 첨부파일로 구성" cellspacing="0" width="100%">
						<caption>${data.title}</caption>
						<colgroup>
							<col width="18%" /> 
							<col width="" />
							<col width="18%" />
							<col width="25%" />
						</colgroup>
						<tbody>
							<tr>
								<th scope="col">작성자</th>
								<td>${data.gadmin}</td>
								<th scope="col">등록일</th>
								<td>${data.indate}</td>
							</tr>
							<tr class="title">
								<th scope="col">제목</th>
								<td colspan="3">${data.title}</td>
							</tr>
							<tr class="title">
								<th scope="col">내용</th>
								<td colspan="3">
								<div style="height:300px; overflow: auto;">
								<c:out value="${data.content}" escapeXml="false"/>
								</div>
								</td>
							</tr>
							<tr class="title">
								<th scope="col">첨부파일</th>
								<td colspan="3">
									<c:if test="${empty fileList}">첨부파일이 없습니다.</c:if>
			                    	<c:if test="${not empty fileList}">
			                    		<c:forEach items="${fileList}" var="file" varStatus="i">
			                    		<c:if test="${i.count > 1}">,&nbsp;</c:if>
					                    <a href="#none" onclick="fn_download('${file.realfile}', '${file.savefile}', 'qnabycourse')"><c:out value="${file.realfile}"/></a>
					                    <input type="hidden" name="p_savefile"  value="<c:out value="${file.savefile}"/>" />
					                    <br/>
			                    		</c:forEach>
			                    		<input type="hidden" name="p_upload_dir" value="qnabycourse" />
			                    	</c:if>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				</form>
				
				<ul class="btnR">
					<li><a class="btn02" href="#" onclick="replyPage()"><span>답변</span></a></li>
					<c:if test="${data.userid eq sessionScope.userid}">
					<li><a class="btn02" href="#" onclick="updatePage()"><span>수정</span></a></li>
					<li><a class="btn02" href="#" onclick="deleteData()"><span>삭제</span></a></li>
					</c:if>
					<li><a class="btn02" href="#" onclick="doPageList()"><span>목록</span></a></li>
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
		frm.action = "/usr/stu/std/userStudyQnaList.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function replyPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyQnaReplyInsertPage.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function updatePage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyQnaUpdatePage.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function deleteData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyQnaDelete.do";
		frm.target = "_self";
		frm.submit();
	}
	document.title="<c:out value='${data.title}' />";

</script>
