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
<div id=idPrint_1>
	<table width="350" border="0" cellspacing="0" cellpadding="0">   
	<tr>
		<td align="left">인가번호 제 04-01호</td>
	</tr> 
	<tr> 
    	<td align="center"><img src="/images/user/receipt_top.jpg"></td>
  	</tr>
  	<tr> 
    	<td background="/images/cert/bg_line.gif" style="padding: 0px 6px 0 6px;">
    		<table width="320" border="0" align="center" cellpadding="0" cellspacing="0" class="txtList" summary="성명, 소속학교, 주민등록번호, 과정명, 연수종별, 금액, 수강신청일로 구성">
            <caption>영수내역</caption>
            <colgroup>
					<col width="30%" />
					<col width="70%" />					                   
			</colgroup>
        		<tr> 
          			<td height="20" colspan="2" class="left last" bgcolor="#eaeaea"><strong>영수내역</strong></td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70" style="text-align:left;">&nbsp;소&nbsp;&nbsp;&nbsp;속&nbsp;&nbsp;&nbsp;기&nbsp;&nbsp;관</th>
          			<td class="left last">${view.orgName}</td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70"  style="text-align:left;">&nbsp;연&nbsp;수&nbsp;&nbsp;대&nbsp;상&nbsp;자</th>
          			<td class="left last">${view.uname}</td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70"  style="text-align:left;">&nbsp;과&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;정&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명</th>
          			<td class="left last">${view.subjNm}</td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70"  style="text-align:left;">&nbsp;연&nbsp;&nbsp;&nbsp;수&nbsp;&nbsp;&nbsp;종&nbsp;&nbsp;별</th>
          			<td class="left last">
          				${view.yonsuName}
          			</td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70"  style="text-align:left;">&nbsp;금&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;액</th>
          			<td class="left last"><fmt:formatNumber value="${view.amount}" type="number"/>원(면세)</td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70"  style="text-align:left;">&nbsp;입&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;금&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;일</th>
          			<td class="left last">${view.amountDate }</td>
        		</tr>
      		</table> 
      	</td>
  	</tr>
  	<tr> 
    	<td>
    		<table width="300" border="0" cellspacing="0" cellpadding="0" align="center">
        		<tr> 
          			<td style="padding:10px;">
          				<strong>이  영수증은 소득공제용 현금영수증의 효력이 없습니다.<br/>
            			우리원은 국가기관으로 법인세법 72조의 2에 의거 현금영수증 가맹점 가입 범위에 제외됩니다.</strong>
            		</td>
        		</tr>
      		</table>
      	</td>
  	</tr>
  	<tr> 
    	<td align="center"><img src="/images/user/receipt_bottom.jpg"/></td>
  	</tr>
    <tr>
        <td height="4"></td>
    </tr>    
</table>

</div>
<!-- // wrapper -->


<br/>
<div>
	<br/>
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