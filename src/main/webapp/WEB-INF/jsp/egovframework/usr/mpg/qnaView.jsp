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
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>

	<input type = "hidden" name="p_process"     value = "" />
	<input type = "hidden" id="pageIndex" name = "pageIndex"     value = "${pageIndex}"/>

<!-- detail wrap -->
        <div class="tbDetail">
            <table summary="제목, 작성자, 등록일, 조회수, 첨부파일, 답변, 답변파일로 구성" cellspacing="0" width="100%">
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
                        <th scope="col">제목</th>
                        <td colspan="5">${view.title}</td>
                    </tr>
                    <tr>
                        <th scope="col">작성자</th>
                        <td class="borderR">${view.name}</td>
                        <th scope="col">등록일</th>
                        <td class="borderR">${fn2:getFormatDate(view.indate, 'yyyy.MM.dd')}</td>
                        <th scope="col">조회수</th>
                        <td>${view.cnt}</td>
                    </tr>
                    <tr>
                        <th scope="col">첨부파일 </th>
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
                           	<c:out value="${fn:replace(view.content, lf, '<br/>')}" escapeXml="false"/>
                        </td>
                    </tr>
                    <tr class="title">
                        <th scope="col">답변</th>
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
                        <th scope="col">답변파일 </th>
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
        
        	<li><a href="#none"  onclick="doPageList()"><img src="/images/user/btn_list.gif" alt="목록" /></a></li>
		</ul> 
		     
		<!-- // button -->
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList() {
	thisForm.action = "/usr/mpg/memMyQnaList.do";
	thisForm.target = "_self";
	thisForm.submit();
}


//-->
</script>