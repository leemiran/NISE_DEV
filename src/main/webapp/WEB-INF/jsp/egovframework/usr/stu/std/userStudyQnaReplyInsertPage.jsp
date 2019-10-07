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
				
				<form name="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
				<input type="hidden" name="p_tabseq" id="p_tabseq"  value="${p_tabseq}"/>
				<input type="hidden" name="p_seq" id="p_seq"  value="${p_seq}"/>
				<input type="hidden" name="p_refseq"    value="${data.refseq}"/>
			    <input type="hidden" name="p_levels"    value="${data.levels+1}"/>
			    <input type="hidden" name="p_position"  value="${data.position+1}"/>
			    <input type="hidden" name="p_isopen" 	value="${data.isopen}"/>
			    <input type="hidden" name="pageIndex" 	id="pageIndex"	value="${pageIndex}">
					<h4 class="tit">Q&amp;A</h4>
					<div class="tbDetail">
						<table summary="작성자, 제목, 내용, 첨부파일로 구성" cellspacing="0" width="100%">
							<caption>${data.title}</caption>
							<colgroup>
								<col width="18%" /> 
								<col width="" />
							</colgroup>
							<tbody>
								<tr>
									<th scope="col">작성자</th>
									<td>${sessionScope.name}</td>
								</tr>
								<tr class="title">
									<th scope="col">제목</th>
									<td>
										<input type="text" id="p_title" name="p_title" style="width:85%; ime-mode:active" value="Re:${data.title}" title="제목"/>
									</td>
								</tr>
								<tr class="title">
									<th scope="col">내용</th>
									<td>
										<textarea name="p_content" cols="100" rows="18" class="debateText" title="내용"></textarea>
									</td>
								</tr>
								<tr class="title">
									<th scope="col">첨부파일</th>
									<td>
										<input type="file" 	 name="p_file" 	 size="80" class="input" title="첨부파일"/>
	                    				<input type="hidden" name="p_upload_dir" value="qnabycourse"/>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</form>
				
				<ul class="btnR">
					<li><a class="btn02" href="#" onclick="insertData()"><span>등록</span></a></li>
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
	
	function insertData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if (frm.p_title.value == "") {
            alert("제목을 입력하세요");
            frm.p_title.focus();
            return;
        }
        if (realsize(frm.p_title.value) > 200) {
            alert("제목은 한글기준 100자를 초과하지 못합니다.");
            frm.p_title.focus();
            return;
        }
		frm.action = "/usr/stu/std/userStudyQnaReplyInsert.do";
		frm.target = "_self";
		frm.submit();
	}
	document.title="질문방(글쓰기) : 나의 강의실 : 국립특수교육원부설원격교육연수원";
</script>
