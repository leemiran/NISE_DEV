<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>


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
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>

	<input type = "hidden" name="p_seq"     value = "<c:out value="${p_seq}"  escapeXml="true" />" />
	<input type = "hidden" id="pageIndex" name = "pageIndex"     value = "<c:out value="${pageIndex}"  escapeXml="true" />" />
	<input type = "hidden" name="p_tabseq"     value = "<c:out value="${p_tabseq}"  escapeXml="true" />" />
	<input type = "hidden" name="p_type"       value = "<c:out value="${p_type}"  escapeXml="true" />" />
	<input type = "hidden" name="p_search"     value = "<c:out value="${p_search}"  escapeXml="true" />" />
	<input type = "hidden" name="p_searchtext" value = "<c:out value="${p_searchtext}"  escapeXml="true" />" />
	


<!-- detail wrap -->
        <div class="tbDetail">
            <table summary="제목, 작성자, 등록일, 조회수, 내용, 첨부파일로 구성" cellspacing="0" width="100%">
                <caption>${view.adtitle}</caption>
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
                        <td colspan="5">
                        <c:if test="${view.noticeGubun eq 'A'}"><img src="/images/adm/ico/ico_notice.gif" alt="공지" /></c:if>
						<c:if test="${view.noticeGubun eq 'B'}"><img src="/images/adm/ico/ico_event.gif" alt="이벤트" /></c:if>
						<c:if test="${view.noticeGubun eq 'C'}"><img src="/images/adm/ico/ico_happy.gif" alt="축하" /></c:if>
						<c:if test="${view.noticeGubun eq 'D'}"><img src="/images/adm/ico/ico_guide.gif" alt="안내" /></c:if>
						<c:if test="${view.noticeGubun eq 'E'}"><img src="/images/adm/ico/ico_poll.gif" alt="설문" /></c:if>
						<c:if test="${view.noticeGubun eq 'F'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
						<c:if test="${view.noticeGubun eq 'G'}"><img src="/images/adm/ico/ico_others.gif" alt="기타" /></c:if>
						
						${view.adtitle}
                        
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">작성자</th>
                        <td class="borderR">${view.adname}</td>
                        <th scope="row">등록일</th>
                        <td class="borderR">${fn2:getFormatDate(view.addate, 'yyyy.MM.dd')}</td>
                        <th scope="row">조회수</th>
                        <td>${view.cnt}</td>
                    </tr>
                    <tr>
                        <th scope="row">첨부파일 </th>
                        <td colspan="5">
                            
                       <c:forEach items="${fileList}" var="result" varStatus="i">
                    		<a href="#none" onclick="fn_download('${result.realfile}', '${result.savefile}', 'bulletin')">
                    		<img src="/images/user/icon_file.gif" alt="첨부파일"/>
                    		<c:out value="${empty result.realfile ? result.savefile : result.realfile}"/>
                    		</a> <br/>        	
                    	</c:forEach>
                            
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" class="tbcon2"><!-- 내용 -->
                           	<c:out value="${view.adcontent}" escapeXml="false"/>
                        </td>
                    </tr>
                </tbody>
            </table>				
        </div>
        <!-- // detail wrap -->
        
        <!-- button -->
		<ul class="btnR">
        	<li><a href="#none"  onclick="pageList()"><img src="/images/user/btn_list.gif" alt="목록" /></a></li>
		</ul>      
		<!-- // button -->
</form>


<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


function pageList(){
	thisForm.action = "/usr/bod/noticeList.do";
	thisForm.target = "_self";
	thisForm.submit();
}

document.title="<c:out value='${view.adtitle}' />";
//-->
</script>

<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->