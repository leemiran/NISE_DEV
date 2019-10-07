<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>

<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>



<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype" value="<c:out value="${p_contenttype}"  escapeXml="true" />" />
<input type="hidden" name="p_subj" value="<c:out value="${p_subj}"  escapeXml="true" />" />
<input type="hidden" name="p_year" value="<c:out value="${p_year}"  escapeXml="true" />" />
<input type="hidden" name="p_subjseq" value="<c:out value="${p_subjseq}"  escapeXml="true" />" />
<input type="hidden" name="p_studytype" value="<c:out value="${p_studytype}"  escapeXml="true" />" />
<input type="hidden" name="p_process" value="<c:out value="${p_process}"  escapeXml="true" />" />
<input type="hidden" name="p_next_process" value="<c:out value="${p_next_process}"  escapeXml="true" />" />
<input type="hidden" name="p_height" value="<c:out value="${p_height}"  escapeXml="true" />" />
<input type="hidden" name="p_width" value="<c:out value="${p_width}"  escapeXml="true" />" />
<input type="hidden" name="p_lcmstype" value="<c:out value="${p_lcmstype}"  escapeXml="true" />" />
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" enctype="multipart/form-data" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>

	<input type = "hidden" name="p_process"     value = "<c:out value="${p_process}"  escapeXml="true" />" />
	<input type = "hidden" name="p_seq"     value = "<c:out value="${p_seq}"  escapeXml="true" />" />
	<input type = "hidden" id="pageIndex" name = "pageIndex"     value = "<c:out value="${pageIndex}"  escapeXml="true" />"/>
	<input type = "hidden" name="p_tabseq"     value = "<c:out value="${view.tabseq}"  escapeXml="true" />" />
	<input type = "hidden" name="p_search"     value = "<c:out value="${p_search}"  escapeXml="true" />" />
	<input type = "hidden" name="p_searchtext"     value = "<c:out value="${p_searchtext}"  escapeXml="true" />" />
	


<!-- detail wrap -->
        <div class="tbDetail">
            <table summary="제목, 조회수, 작성자, 등록일, 첨부파일, 내용, 답변, 답변파일로 구성" cellspacing="0" width="100%">
                <caption><c:out value="${view.title}" escapeXml="true" /></caption>
                <colgroup>
                    <col width="90px" />
                    <col width="" />
                    <col width="90px" />
                    <col width="" />
                    <col width="90px" />
                    <col width="" />
                </colgroup>
                <tbody>
                    <tr class="title">
                        <th scope="row">구분</th>
                        <td colspan="5">
                        <c:if test="${view.tabseq ne '2'}">환불요청</c:if>
                        <c:if test="${view.tabseq eq '2'}">입금확인</c:if>
                        </td>
                    </tr>
                    <tr class="title">
                        <th scope="row">제목</th>
                        <td colspan="5"><c:out value="${view.title}" escapeXml="true" /></td>
                    </tr>
                    <tr>
                        <th scope="row">작성자</th>
                        <td class="borderR">${view.name}</td>
                        <th scope="row">등록일</th>
                        <td class="borderR">${fn2:getFormatDate(view.indate, 'yyyy.MM.dd')}</td>
                        <th scope="row">조회수</th>
                        <td>${view.cnt}</td>
                    </tr>
                    
                    <tr>
                        <th scope="row">첨부파일 </th>
                        <td colspan="5">
                        <c:if test="${not empty view.realfile}">
                    		<a href="#none" onclick="fn_download('${view.realfile}', '${view.savefile}', 'bulletin')">
                    		<img src="/images/user/icon_file.gif" alt="첨부파일"/>
                    		<c:out value="${empty view.realfile ? view.savefile : view.realfile}"/>
                    		</a>
                    	</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" class="tbcon2"><!-- 내용 -->
                           	<c:out value="${fn:replace(view.content, lf, '<br/>')}" escapeXml="true"/>
                        </td>
                    </tr>
                    <tr class="title">
                        <th scope="row">답변</th>
                        <td colspan="5">
                        <c:if test="${not empty view.atitle}">[답변] ${view.atitle}</c:if>
                        <c:if test="${empty view.atitle}">처리중..</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" class="tbcon2"><!-- 내용 -->
                           	<c:out value="${view.acontent}" escapeXml="false"/>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">답변파일 </th>
                        <td colspan="5">
                        <c:if test="${not empty view.realfile2}">
                    		<a href="#none" onclick="fn_download('${view.realfile2}', '${view.savefile2}', 'bulletin')">
                    		<img src="/images/user/icon_file.gif" alt="첨부파일"/>
                    		<c:out value="${empty view.realfile2 ? view.savefile2 : view.realfile2}"/>
                    		</a>
                    	</c:if>
                        </td>
                    </tr>
                </tbody>
            </table>				
        </div>
        <!-- // detail wrap -->
        
        
        
        <!-- button -->
		<ul class="btnR">
		
		<c:if test="${view.hasanswer ne 'Y' && view.userid eq sessionScope.userid}">
        	<li><a href="#none"  onclick="doEdit()"><img src="/images/user/btn_edit.gif" alt="수정" /></a></li>
        	<li><a href="#none"  onclick="doDelete()"><img src="/images/user/btn_delete.gif" alt="삭제" /></a></li>
        </c:if>
        
        
        	<li><a href="#none"  onclick="doPageList()"><img src="/images/user/btn_list.gif" alt="목록" /></a></li>
		</ul> 
		     
		<!-- // button -->
</form>


<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList() {
	<c:if test="${p_tabseq eq '1'}">		
	thisForm.action = "/usr/svc/qnaList01.do";
	</c:if>
	<c:if test="${p_tabseq ne '1'}">		
	thisForm.action = "/usr/svc/qnaList02.do";
	</c:if>
	thisForm.target = "_self";
	thisForm.submit();
}

function doEdit(){
	<c:if test="${p_tabseq eq '1'}">		
	thisForm.action = "/usr/svc/qnaEdit01.do";
	</c:if>
	<c:if test="${p_tabseq ne '1'}">		
	thisForm.action = "/usr/svc/qnaEdit02.do";
	</c:if>
	thisForm.target = "_self";
	thisForm.submit();
}



function doDelete(){
	if(confirm("작성하신 글을 삭제하시겠습니까?"))
	{
		thisForm.action = "/usr/svc/qnaAction.do";
		thisForm.pageIndex.value = "1";
		thisForm.p_process.value = "delete";
		thisForm.target = "_self";
		thisForm.submit();
	}
}

document.title="<c:out value='${view.title}' escapeXml="true" />";
//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->