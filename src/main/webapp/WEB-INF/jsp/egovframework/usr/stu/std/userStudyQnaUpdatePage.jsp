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
				<input type="hidden" name="p_seq"    id="p_seq"     value="${p_seq}"/>
				<input type="hidden" name="pageIndex" 	id="pageIndex"	value="${pageIndex}">
				<h4 class="tit">Q&amp;A</h4>
				<div class="tbDetail">
					<table summary="작성자, 등록일, 제목, 공개여부, 내용, 첨부파일로 구성" cellspacing="0" width="100%">
						<caption>Q&amp;A 작성하기</caption>
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
								<td colspan="3">
								<input type="text" id="p_title" name="p_title" title="제목" style="width:85%; ime-mode:active" value="<c:out value="${data.title}"/>"/>
								</td>
							</tr>
							<c:if test="${data.levels == 1}">
							<tr class="title">
								<th scope="col">공개여부</th>
								<td>
									<input type="radio" name="p_isopen" id="openYes"  class="btnBorder" value="Y" <c:if test="${data.isopen eq 'Y'}">checked</c:if> /> <label for="openYes">공개</label> 
									<input type="radio" name="p_isopen" id="openNo"  class="btnBorder" value="N" <c:if test="${data.isopen eq 'N'}">checked</c:if> /> <label for="openNo">비공개(비공개 선택시 운영자와 강사 그리고 작성자만 볼 수 있습니다.)</label>
								</td>
							</tr>
							</c:if>
							<tr class="title">
								<th scope="col">내용</th>
								<td colspan="3">
								<textarea name="p_content" cols="100" rows="18" class="debateText"  title="내용"><c:out value="${data.content}"/></textarea>
								</td>
							</tr>
							<tr class="title">
								<th scope="col">첨부파일</th>
								<td colspan="3">
									<c:forEach items="${fileList}" var="file" varStatus="i">
										<c:if test="${not empty file.realfile}">
			                    		<a href="#none" onclick="fn_download('${file.realfile}', '${file.savefile}', 'qnabycourse')"><c:out value="${file.realfile}"/></a>
					                    <input type="checkbox"  name="p_filedel" id="p_filedel"  value="<c:out value="${i.count}"/>" /><label for="p_filedel" > (삭제시 체크)</label><br/>
										<input type="hidden" name="p_realfile${i.count}"  value="<c:out value="${file.realfile}"/>"/> 
										<input type="hidden" name="p_savefile${i.count}"  value="<c:out value="${file.savefile}"/>"/>
										<input type="hidden" name="p_fileseq${i.count}"  value="<c:out value="${file.fileseq}"/>"/>
										</c:if>
			                    	</c:forEach>
			                    	<input type="file" 	 name="p_file" 	 size="80" class="input" title="첨부파일"/>
			                    	<input type="hidden" name="p_upload_dir" value="qnabycourse"/>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				</form>
				
				<ul class="btnR">
					<li><a class="btn02" href="#" onclick="updateData()"><span>수정</span></a></li>
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
	
	function updateData(){
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
		frm.action = "/usr/stu/std/userStudyQnaUpdate.do";
		frm.target = "_self";
		frm.submit();
	}

</script>
