<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	<input type="hidden" name="p_subjnm" 	id="p_subjnm"	value="${p_subjnm}">
	<input type="hidden" name="p_tabseq" 	id="p_tabseq"	value="${p_tabseq}">
	<input type="hidden" name="p_seq" 		id="p_seq"		value="${p_seq}">
	<input type="hidden" name="p_upfilecnt" id="p_upfilecnt"value="${p_upfilecnt}">
	<input type="hidden" name="p_userid" 	id="p_userid"	value="${p_userid}">
	<input type="hidden" name="p_action" 	id="p_action"	value="${p_action}">
	<input type="hidden" name="pageIndex" 	id="pageIndex"	value="${pageIndex}">
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>취소</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="updateData()"><span>저장</span></a></div>
	</div>
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="150px" />
                <col width="" />
            </colgroup>
            <tbody>
                <tr>
                    <th>작성자</th>
                    <td>[${data.gadmin}] ${data.name}(${data.userid})</td>
                </tr>
                <tr>
                    <th>상담분류</th>
                    <td>
                    	<ui:code id="p_sangdam_gubun" selectItem="${data.sangdamGubun}" gubun="" codetype="0047"  upper="" title="" className="" type="select" selectTitle="" event="" />
                    </td>
                </tr>
                <tr>
                    <th>공개여부</th>
                    <td>
                    	<input type="radio" id="p_isopen" name="p_isopen" value="Y" <c:if test="${data.isopen eq 'Y'}">checked</c:if>>공개
                    	<input type="radio" id="p_isopen" name="p_isopen" value="N" <c:if test="${data.isopen eq 'N'}">checked</c:if>>비공개
                    </td>
                </tr>
                <tr class="title">
                    <th>제목</th>
                    <td>
                    	<input type="text" id="p_title" name="p_title"  size="80" style="ime-mode:active" value="<c:out value="${data.title}"/>">
                    </td>
                </tr>
                <tr class="title">
                    <th>내용</th>
                    <td>
                    	<c:set var="content" value="${data.content}"/>
                    	<c:set var="width"  value="600"/>
                    	<c:set var="height" value="400" />
                    	<%@ include file="/WEB-INF/jsp/egovframework/com/lib/DhtmlEditor.jsp" %>
						<input type="hidden" name="p_content" id="p_content">
                    </td>
                </tr>
                <tr class="title">
                    <th>첨부파일</th>
                    <td>
                    	<c:forEach items="${fileList}" var="file" varStatus="i">
                    		<a href="#none" onclick="fn_download('${file.realfile}', '${file.savefile}', 'qnabycourse')"><c:out value="${file.realfile}"/></a>
		                    <input type="checkbox"  name="p_filedel" value="<c:out value="${i.count}"/>"> (삭제시 체크)<br/>
							<input type="hidden" name="p_realfile${i.count}"  value="<c:out value="${file.realfile}"/>">
							<input type="hidden" name="p_savefile${i.count}"  value="<c:out value="${file.savefile}"/>">
							<input type="hidden" name="p_fileseq${i.count}"  value="<c:out value="${file.fileseq}"/>">
                    	</c:forEach>
                    	<c:forEach begin="1" end="${5 - fn:length(fileList)}" varStatus="i">
                    	<input type="file" 	 name="p_file${i.count}" 	 size="80" class="input">
                    	</c:forEach>
                    	<input type="hidden" name="p_upload_dir" value="qnabycourse">
                    </td>
                </tr>
            </tbody>
        </table>				
    </div>
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/qna/courseAdminQnaList.do";
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
        frm.p_content.value = ObjEditor.document.all.tags("html")[0].outerHTML;
		frm.action = "/adm/cmg/qna/courseAdminQnaUpdate.do";
		frm.submit();
	}
</script>