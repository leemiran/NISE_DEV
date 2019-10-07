<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/userCommonHead.jsp" %>

<STYLE TYPE="TEXT/CSS"> 

.title {
	font-family:'견고딕';
	font-size:50px;
	font-weight:bold;
	line-height:200%;
	color:#000;
	letter-spacing:-1px
}
</STYLE> 

</head> 


<body style="background:white;" onload="body_onload()">
<object id="factory" width="0" height="0" classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814" codebase="/script/smsx.cab#Version=7,0,0,8">
</object>


<c:forEach items="${list}" var="result">
<div id="idPrint" style="padding-left:2px;">
	<table border="0" cellpadding="5" cellspacing="0" bordercolordark="white" bordercolorlight="black" width="645">
		<tr>
			<td align="center">
				<table  border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td width="100%" align="left" style="padding-left:40px;">
							<c:if test="${result.upperclassnm ne '일반연수'}">
								특수-${fn:replace(fn:replace(result.upperclassnm, '특별과정','교원'), '기타','')} 직무-${fn2:getFormatDate(result.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}
							</c:if>
							<c:if test="${result.upperclassnm eq '일반연수'}">
								특수-사이버-${fn2:getFormatDate(result.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}
							</c:if>
							호
						</td>
					</tr>
				</table>
				<br/><br/>
				<p class="title" align="center">교육연수 이수증</p>
				<br/><br/><br/><br/>
				<table  border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td width="100%" align="left" style="padding-left:40px;">
							
							<table>
								<tr>
									<td class="pt">성 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명</td>
									<td class="pt">${result.name}</td>
									<td class="pt">생년월일</td>
									<td class="pt">${result.birthDate}</td>
								</tr>
								<tr><td colspan="4" height="10px">&nbsp;</td></tr>
								<tr>
									<td class="pt">연 &nbsp;수 &nbsp;종 &nbsp;류</td>
									<td class="pt">
										<c:if test="${result.upperclassnm ne '일반연수'}">직무연수</c:if>
										<c:if test="${result.upperclassnm eq '일반연수'}">-</c:if>
									</td>
									<td class="pt">이 &nbsp;수 &nbsp;시 &nbsp;간</td>
									<td class="pt">${result.edutimes}<c:if test="${result.edutimes >= 60}">(${result.score}점)</c:if></td>
								</tr>
								<tr><td colspan="4" height="10px">&nbsp;</td></tr>
								<tr>
									<td class="pt">연 &nbsp;수 &nbsp;기 &nbsp;간</td>
									<td class="pt" colspan="3">
										${fn2:getFormatDate(result.edustart, 'yyyy')}년${fn2:getFormatDate(result.edustart, 'MM')}월${fn2:getFormatDate(result.edustart, 'dd')}일 ∼ 
										${fn2:getFormatDate(result.eduend, 'yyyy')}년${fn2:getFormatDate(result.eduend, 'MM')}월${fn2:getFormatDate(result.eduend, 'dd')}일
										(${result.eduday} 일간)
									</td>
								</tr>
								<tr><td colspan="4" height="10px">&nbsp;</td></tr>
								<tr>
									<td class="pt">연 &nbsp;수 &nbsp;목 &nbsp;적</td>
									<td class="pt" colspan="3">${result.subjnm} 을(를) 위한 연수</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td height="65px">&nbsp;</td>
					</tr>
					<tr>
						<td  align="center" style="padding-left:40px;">
							<p class="pt" align="left">&nbsp;&nbsp;「교원 등의 연수에 관한 규정」 제6조제1항에 따른 연수를 위와 같이<br/>이수하였음을 증명합니다.</p> 
						</td>
					</tr>
				</table>
				<br/><br/>
				<p class="pt" align="right">
					${result.gradYear}년 &nbsp; ${result.gradMonth}월 &nbsp; ${result.gradDay}일&nbsp;
				</p>
			  
				<br/><br/><br/><br/><br/><br/>
				<c:if test="${sessionScope.gadmin eq 'ZZ'}">
				<img src="/images/user/award1-2-1.gif" style="padding-top:20px;"/>
				</c:if>
				<c:if test="${sessionScope.gadmin ne 'ZZ'}">
					<br/><br/><br/><br/>
				</c:if>
				<br/><br/>
			</td>
		</tr>
	</table>   
</div>
<p style='page-break-after:always;'/>
<br/>
</c:forEach>
</body>
</html>
<script type="text/javascript"> 

	// 화면 로딩시 위치 및 크기 조절
	function body_onload(){
		parent.moveTo(0, 0);
		parent.resizeTo(750, 1060);

		ScriptX_print();
		
	}
	
	function ScriptX_print(){

		if ( !factory.object ) {
			alert("MeadCo's ScriptX Control is not properly installed!");
			window.print();
			return true;
		}
		while(factory){
			factory.printing.header		   = "";
			factory.printing.footer		   = "";
			factory.printing.portrait	   = true;
			factory.printing.leftMargin = 6.65;
			factory.printing.topMargin = 25;
			factory.printing.rightMargin = 6.65;
			factory.printing.bottomMargin = 5;
			//factory.printing.Preview(); 
			factory.printing.Print(true);
		}
	}
</script> 