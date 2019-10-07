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

	<input type = "hidden" name="p_process"     value = "${p_process}" />
	<input type = "hidden" name="p_seq"     value = "${p_seq}" />
	<input type = "hidden" id="pageIndex" name = "pageIndex"     value = "${pageIndex}"/>
	<input type = "hidden" name="p_tabseq"     value = "${p_tabseq}" />
	<input type = "hidden" name="p_search"     value = "${p_search}" />
	<input type = "hidden" name="p_searchtext"     value = "${p_searchtext}" />
	<input type="hidden" name="p_refseq"   id="p_refseq"   value="${view.refseq}"/>
	<input type="hidden" name="p_levels"   id="p_levels"   value="${view.levels}"/>
	<input type="hidden" name="p_position" id="p_position" value="${view.position}"/>


<!-- detail wrap -->
        <div class="tbDetail">
            <table summary="작성자, 공개여부, 제목, 이메일, 내용 으로 구성" cellspacing="0" width="100%">
                <caption>${v_title}</caption>
                <colgroup>
                    <col width="90px" />
                    <col width="" />
                </colgroup>
                <tbody>
                    <tr class="title">
                        <th scope="col">작성자</th>
                        <td>
<c:if test="${empty p_seq || p_process eq 'reply'}">
                        	<c:out value="${sessionScope.name}"/>(<c:out value="${sessionScope.userid}"/>)
</c:if>
<c:if test="${not empty p_seq && p_process ne 'reply'}">
                        	<c:out value="${view.name}"/>(<c:out value="${view.userid}"/>)
</c:if>
                        </td>
                    </tr>
<c:if test="${p_tabseq eq '1'}">
						<tr>
							<th scope="col">공개여부</th>
							<td>
							<input id="p_isopen" name="p_isopen" type="radio" value="Y" class="input_border vrM" <c:if test="${view.isopen eq 'Y'}">checked</c:if> />공개
							<input id="p_isopen" name="p_isopen" type="radio" value="N" class="input_border vrM" <c:if test="${view.isopen ne 'Y'}">checked</c:if> />비공개
							</td>
						</tr>
</c:if>
<c:if test="${p_tabseq ne '1'}">
							<input id="i_isopeny" name="p_isopen" type="hidden" value="Y">
</c:if>                   
                    <tr>
                        <th scope="col">제목</th>
                        <td>
                        
                        <c:set var="v_title"><c:out value="${view.title}"/></c:set>
                        <input type="text" name="p_title" style="width:500px;" value="<c:out value="${v_title}"/>" maxlength="200"/>
                        
                        </td>
                    </tr>
                    <tr>
                          <th scope="col">이메일</th>
                          <td>
<c:if test="${empty p_seq || p_process eq 'reply'}">
                        	<input type="text" name="p_email" style="width:200px;" value="<c:out value="${sessionScope.email}"/>" maxlength="50"/>
</c:if>
<c:if test="${not empty p_seq && p_process ne 'reply'}">
                        	<input type="text" name="p_email" style="width:200px;" value="<c:out value="${view.email}"/>" maxlength="50"/>
</c:if>
                          
                          </td>
                    </tr>
                    <tr>
                          <th scope="col">내용</th>
                          <td>
                          	<c:set var="v_content"><c:out value="${view.content}" escapeXml="false"/></c:set>
	                        <c:if test="${p_process eq 'reply'}">
	                        	<c:set var="v_content">&nbsp;${crlf}${crlf}${crlf}${crlf}-----------------------------------[원문]--------------------------------${crlf}${crlf}${view.content}</c:set>
	                        </c:if>
                          <textarea name="p_content" rows="15" style="width:600px;"><c:out value="${v_content}" escapeXml="false"/></textarea>
                          
                          </td>
                    </tr>  
                </tbody>
            </table>				
        </div>
        <!-- // detail wrap -->
        
        
        
		<!-- button -->
		<ul class="btnR">
<c:if test="${empty p_seq}">
        	<li><a href="#none" onclick="doPageSave()"><img src="/images/user/btn_ok.gif" alt="확인"/></a></li>
</c:if>
<c:if test="${not empty p_seq}">
        	<li><a href="#none" onclick="doPageSave()"><img src="/images/user/btn_ok.gif" alt="확인"/></a></li>
</c:if>        	
        	<li><a href="#none" onclick="doPageList()"><img src="/images/user/btn_list.gif" alt="목록"/></a></li>        	
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
	thisForm.action = "/usr/bod/opinionUserList.do";
	thisForm.target = "_self";
	thisForm.submit();
}


function doPageSave(mode){

	if(thisForm.p_title.value == "")
	{
		alert("제목을 입력해 주십시오.");
		thisForm.p_title.focus();
		return;
	}
	
	if (!isValidEmail(thisForm.p_email)) {
         alert("올바른 이메일 주소가 아닙니다.");
         thisForm.p_email.focus();
         return;
    }
    
	if(thisForm.p_content.value == "")
	{
		alert("내용을 입력해 주십시오.");
		thisForm.p_content.focus();
		return;
	}

	
	if(confirm("작성하신 글을 저장하시겠습니까?"))
	{
		thisForm.action = "/usr/bod/opinionUserAction.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
}
	document.title="연수소감(쓰기) : 참여마담";

//-->
</script>