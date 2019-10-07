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
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>

	<input type = "hidden" name="p_process"     value = "" />
	<input type = "hidden" name="p_seq"     value = "${p_seq}" />
	<input type = "hidden" id="pageIndex" name = "pageIndex"     value = "${pageIndex}"/>
	<input type = "hidden" name="p_tabseq"     value = "${p_tabseq}" />
	<input type = "hidden" name="p_search"     value = "${p_search}" />
	<input type = "hidden" name="p_searchtext"     value = "${p_searchtext}" />
	


<!-- detail wrap -->
        <div class="tbDetail">
            <table summary="제목, 작성자, 등록일, 조회수 내용으로 구성" cellspacing="0" width="100%">
                <caption>${view.title}</caption>
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
                        <th scope="row">제목</th>
                        <td colspan="5">${view.title}</td>
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
                        <td colspan="6" class="tbcon2"><!-- 내용 -->
                           	<c:out value="${fn:replace(view.content, lf, '<br/>')}" escapeXml="false"/>
                        </td>
                    </tr>
                </tbody>
            </table>				
        </div>
        <!-- // detail wrap -->
        
        
        
        <!-- button -->
		<ul class="btnR">
		
			<li><a href="#none"  onclick="doReply()"><img src="/images/user/btn_re_write.gif" alt="답변" /></a></li>
		
		<c:if test="${(view.hasanswer ne 'Y' && view.userid eq sessionScope.userid) || (fn:indexOf(sessionScope.gadmin, 'A') > -1)}">
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
	thisForm.action = "/usr/bod/opinionUserList.do";
	thisForm.p_process.value = '';
	thisForm.target = "_self";
	thisForm.submit();
}

function doEdit(){
	thisForm.action = "/usr/bod/opinionUserEdit.do";
	thisForm.target = "_self";
	thisForm.p_process.value = 'update';
	thisForm.submit();
}

function doReply(){
	thisForm.action = "/usr/bod/opinionUserEdit.do";
	thisForm.target = "_self";
	thisForm.p_process.value = 'reply';
	thisForm.submit();
}



function doDelete(){
	if(confirm("작성하신 글을 삭제하시겠습니까?"))
	{
		thisForm.action = "/usr/bod/opinionUserAction.do";
		thisForm.pageIndex.value = "1";
		thisForm.p_process.value = "delete";
		thisForm.target = "_self";
		thisForm.submit();
	}
}
document.title="<c:out value='${view.title}' />";
//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->