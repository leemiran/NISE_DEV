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
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" enctype="multipart/form-data" action="">
<fieldset>
<legend>나의질문방</legend>
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>

	<input type = "hidden" name="p_process"     value = "" />
	<input type = "hidden" name="p_tabseq"     value = "${p_tabseq}" />
	<input type = "hidden" name="p_seq"     value = "${p_seq}" />
	<input type = "hidden" name="p_search"     value = "${p_search}" />
	<input type = "hidden" name="p_searchtext"     value = "<c:out value="${p_searchtext}"  escapeXml="true" />" />
	<input type = "hidden" id="pageIndex" name = "pageIndex"    value = "${pageIndex}"/>

<!-- detail wrap -->
        <div class="tbDetail">
            <table summary="제목, 작성자, 등록일, 조회수, 첨부파일, 내용, 답변으로 구성" cellspacing="0" width="100%">
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
                    
                    
                    <c:if test="${p_kind eq '1' }">
	                    <tr>
	                        <th scope="row">첨부파일 ${p_kind}</th>
	                        <td colspan="5">
	                        <c:forEach items="${list}" var="result" varStatus="status">
	                    		<a href="#none" onclick="fn_download('${result.realfile}', '${result.savefile}', 'qnabycourse')">
	                    		<img src="/images/user/icon_file.gif" alt="첨부파일"/>
	                    		<c:out value="${empty result.realfile ? result.savefile : result.realfile}"/>
	                    		</a>
	                    	</c:forEach>
	                        </td>
	                    </tr>
                    </c:if>
                    
                    <c:if test="${p_kind eq '2' }">
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
                    </c:if>
                    
                    <tr>
                        <td colspan="6" class="tbcon2"><!-- 내용 -->
                           	<c:out value="${fn:replace(view.content, lf, '<br/>')}" escapeXml="false"/>                           	
                        </td>
                    </tr>
                    <tr class="title">
                        <th scope="row">답변</th>
                        <td colspan="5">
                        <c:if test="${not empty view.atitle}">[답변] ${view.atitle}</c:if>
                        <c:if test="${empty view.atitle}">처리중..</c:if>
                        </td>
                    </tr>
                    
                    <c:if test="${p_kind eq '1' }">                    
	                    <tr>
	                        <td colspan="6" class="tbcon2"><!-- 내용 -->
	                           	<c:out value="${view.acontents}" escapeXml="false"/>
	                        </td>
	                    </tr>	                    	                  
                    </c:if>
                    
                    <c:if test="${p_kind eq '2' }">                    
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
                    </c:if>
                    
                </tbody>
            </table>				
        </div>
        <!-- // detail wrap -->
        
        
        
        <!-- button -->
		<ul class="btnR">
        
        	<li><a href="#none"  onclick="doPageList()"><img src="/images/user/btn_list.gif" alt="목록" /></a></li>
		</ul> 
		     
		<!-- // button -->
	</fieldset>
</form>
<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList() {
	thisForm.action = "/usr/myh/myCursQnaList.do";
	thisForm.target = "_self";
	thisForm.submit();
}
document.title="<c:out value="${view.title}" escapeXml="true" />";
//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->