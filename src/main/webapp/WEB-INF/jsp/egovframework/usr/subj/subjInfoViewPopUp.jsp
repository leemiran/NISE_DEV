<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" 	 uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" 		 uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" 	 uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" 	 uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" 		 uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" 	 uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonHead.jsp" %>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>



</head>


<body style="background:none;">
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2">
		<div class="popCon">
			<!-- header -->
			<div class="tit_bg">
			<h2>과정상세정보</h2>
       		</div>
			<!-- //header -->











<%//과정정보 인클루드 %>
<%@ include file = "./subjInfoViewInclude.jsp" %>








			 
                
               <!-- button -->
              <ul class="btnCen">
                <li><a href="#none" onclick="window.close();" class="pop_btn01"><span>닫 기</span></a></li>                
              </ul>
              <!-- // button --> 
		</div>
	</div>
	
<!-- 페이지 정보 -->
	<%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
	<!-- 페이지 정보 -->
</div>
<!-- // wrapper -->


<!--아래는 팝업과 뷰를 따로 스크립트를 프로그램한다.-->
<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


function doPageList()
{
}


//교육후기 페이징
function doLinkPage(index) {
	thisForm.action = "/usr/subj/subjInfoViewPopUp.do";
	thisForm.pageIndex.value = index;
	thisForm.p_process.value = "commentsList";
	thisForm.target = "_self";
	thisForm.submit();
}

//교육후기 페이징 때문에 추가함 = p_process == commentsList 이면 교육후기를 보여준다.
window.onload = function () 
{
	if('${p_process}' == 'commentsList')
	{
		displayTabMenu(3);
	}

	//팝업일 경우는 id = PopupSubjPageList 를 숨기도록 한다.
	//팝업에서는 리스트로 가기가 없다 
	document.all("PopupSubjPageList").style.display = "none";
	
	document.title="과정상세정보 보기("+"<c:out value='${view.subjnm}' /> "+")";
	
}
//-->
</script>



</body>
</html>