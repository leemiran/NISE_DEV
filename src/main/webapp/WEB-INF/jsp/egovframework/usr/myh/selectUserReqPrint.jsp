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





<body style=" background:white;">
<div id=idPrint>
	<table border="0" cellpadding="5" cellspacing="0" bordercolordark="white" bordercolorlight="black" width="640" height="700" align="center">
		<tr>
			<td><br/>				
				<p class="pt2" align="center">확&nbsp;&nbsp;&nbsp;인&nbsp;&nbsp;&nbsp;증</p>
				<br/><br/>
				<table align="center" border="0" cellpadding="0" cellspacing="0" width="90%">
					<tr>
						<td width="563">
							<p class="pt">성 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명 : ${view.name}</p>
                            <p class="pt">소 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;속 : ${view.userPath}</p>
							<p class="pt">연 &nbsp;수 &nbsp;과 &nbsp;정 : ${view.subjnm}</p>	
							<p class="pt">연 &nbsp;수 &nbsp;기 &nbsp;간 : 
								${fn2:getFormatDate(view.edustart, 'yyyy년 MM월 dd일')} ∼ 
								${fn2:getFormatDate(view.eduend, 'yyyy년 MM월 dd일')}
							</p>
						</td>
					</tr>
					<tr>
						<td height="65px">&nbsp;</td>
					</tr>
					<tr>
						<td width="563">
							<p class="pt3" align="center">위사람은 국립특수교육원부설원격교육연수원에서<br/> 운영하는 연수과정을 신청하셨음을 증명합니다.</p> 
							
						</td>
					</tr>
				</table>
				<p class="pt" align="center"><br/><br/>
					<strong>${fn:replace(fn2:getFormatDate(view.appdate, 'yyyy년  MM월   dd일'), ' ', '&nbsp;')}</strong>
				     <br/><br/>
					<img src="/images/user/com_down.gif"/>
				</p>
				<br/>
			</td>
		</tr>
	</table>   

</div>

<br/>
<div>
	<ul class="btnCen">
	    <li><a href="#none" class="pop_btn01" onclick="cleanPrinting()" id="bottomImg1" ><span>인쇄하기</span></a></li> 
	    <li><a href="#none" class="pop_btn01" onclick="window.close();" id="bottomImg2" ><span>닫기</span></a></li>               
	</ul>
</div> 



<!-- 페이지 정보 -->
<%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
<!-- 페이지 정보 -->




<!--아래는 팝업과 뷰를 따로 스크립트를 프로그램한다.-->
<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


function cleanPrinting()
{
    for( i = 1; i <= 2; i++ ) document.getElementById( "bottomImg" + i ).style.display = "none";
    alert( "출력시 배경이미지가 나오지 않을 경우,\n\n[도구]메뉴 - [인터넷 옵션] - [고급]탭 - 인쇄 항목을 확인해주시기 바랍니다." );
    window.print();
    //document.getElementById( "bottomImg2" ).style.display = "inline";
}
//-->
</script>



</body>
</html>