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

	<input type="hidden" name="p_tabseq"     value = "4" />
	<input type="hidden" name="p_refseq"   id="p_refseq"   value=""/>
	<input type="hidden" name="p_levels"   id="p_levels"   value=""/>
	<input type="hidden" name="p_position" id="p_position" value=""/>


<!-- detail wrap -->
        <div class="tbDetail">
            <table summary="작성자, 공개여부, 제목, 이메일, 내용 으로 구성" cellspacing="0" width="100%">
                <caption>${v_title}</caption>
                <colgroup>
                    <col width="90px" />
                    <col width="" />
                </colgroup>
                <tbody>
                	<tr>
                        <th scope="col">구분</th>
                        <td>
                        <input id="p_opgubun_1" name="p_gubuna" type="radio" value="TRO" class="input_border vrM" checked/>연수개선의견
						<input id="p_opgubun_2" name="p_gubuna" type="radio" value="ERO" class="input_border vrM" />오류사항의견
                        </td>
                    </tr>
                    <tr class="title">
                        <th scope="col">작성자</th>
                        <td>
                        	<c:out value="${sessionScope.name}"/>(<c:out value="${sessionScope.userid}"/>)
                        </td>
                    </tr>                
                    <tr>
                        <th scope="col">제목</th>
                        <td>
                            <input type="text" name="p_title" style="width:500px;" value="" maxlength="200"/>
                        </td>
                    </tr>
                    <tr>
                          <th scope="col">이메일</th>
                          <td>
                        	<input type="text" name="p_email" style="width:200px;" value="<c:out value="${sessionScope.email}"/>" maxlength="50"/>
						 </td>
                    </tr>
                    <tr>
                          <th scope="col">내용</th>
                          <td>
                          <textarea name="p_content" rows="15" style="width:600px;"></textarea>
                          
                          </td>
                    </tr>  
                </tbody>
            </table>				
        </div>
        <!-- // detail wrap -->
        
        
        
		<!-- button -->
		<ul class="btnR">
        	<li><a href="#none" onclick="doPageSave()"><img src="/images/user/btn_ok.gif" alt="확인"/></a></li>    	
		</ul>      
		<!-- // button -->
		     
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageSave(){

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
		thisForm.action = "/usr/bod/opinionUpgradeAction.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
}
	document.title="연수개선의견 : 참여마담";

//-->
</script>