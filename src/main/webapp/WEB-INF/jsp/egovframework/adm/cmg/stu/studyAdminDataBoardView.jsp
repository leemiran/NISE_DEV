<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
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
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="deleteData()"><span>삭제</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="updatePage()"><span>수정</span></a></div>
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
                <tr class="title">
                    <th>제목</th>
                    <td><c:out value="${data.title}"/></td>
                </tr>
                <tr>
                    <th>등록일</th>
                    <td><c:out value="${data.indate}"/></td>
                </tr>
                <tr>
                    <th>조회수</th>
                    <td><c:out value="${data.cnt}"/></td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td><c:out value="${data.content}" escapeXml="false"/></td>
                </tr>
                <tr class="title">
                    <th>첨부파일</th>
                    <td>
                    	<c:if test="${empty fileList}">첨부파일이 없습니다.</c:if>
                    	<c:if test="${not empty fileList}">
                    		<c:forEach items="${fileList}" var="file">
		                    <a href="#none" onclick="fn_download('${file.realfile}', '${file.savefile}', 'studyadmindata')"><c:out value="${file.realfile}"/></a>
		                    <input type="hidden" name="p_savefile"  value="<c:out value="${file.savefile}"/>">
		                    <br/>
                    		</c:forEach>
                    		<input type="hidden" name="p_upload_dir" value="studyadmindata">
                    	</c:if>
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
		frm.action = "/adm/cmg/stu/studyAdminDataBoardList.do";
		frm.submit();
	}
	
	function updatePage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/stu/studyAdminDataUpdatePage.do";
		frm.submit();
	}
	
	function deleteData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( !confirm("정말 삭제하시겠습니까?") ){
			return;
		}
		frm.action = "/adm/cmg/stu/studyAdminDataDelete.do";
		frm.submit();
	}
</script>