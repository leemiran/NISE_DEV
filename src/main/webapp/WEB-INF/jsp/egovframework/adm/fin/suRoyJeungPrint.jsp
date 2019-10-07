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
</head> 


<body style="background:white;" onload="body_onload()">
<object id="factory" width="0" height="0" classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814" codebase="/script/smsx.cab#Version=7,0,0,8">
</object>


<c:forEach items="${list}" var="result">
<div id="idPrint" style="padding-left:2px;">
	<table border="0" cellpadding="5" cellspacing="0" bordercolordark="white" bordercolorlight="black" width="645">
		<tr>
			<td align="center">
				<br/><br/>
				<p class="pt2" align="center">교육연수이수증</p>
				<br/><br/><br/><br/>
				<table  border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td width="100%" align="left" style="padding-left:40px;">
						
						
							<c:if test="${result.upperclassnm ne '일반연수'}">
								<p class="pt">증 &nbsp;서 &nbsp;번 &nbsp;호 : 특수-${fn:replace(fn:replace(result.upperclassnm, '특별과정','교원'), '기타','')} 직무-${fn2:getFormatDate(result.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}</p>
							</c:if>
							<c:if test="${result.upperclassnm eq '일반연수'}">
								<%-- <p class="pt">증 &nbsp;서 &nbsp;번 &nbsp;호 : 특수-사이버-${fn2:getFormatDate(result.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}</p> --%>
								<p class="pt">증 &nbsp;서 &nbsp;번 &nbsp;호 : 특수-${fn:replace(fn:replace(result.upperclassnm, '특별과정','교원'), '기타','')} 직무-${fn2:getFormatDate(result.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}</p>
							</c:if>
							 <p class="pt">성 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명 : ${result.name}</p>
								
							<c:if test="${result.empGubun eq 'T'}"> 
								<p class="pt">연 &nbsp;수 &nbsp;종 &nbsp;별 : 직무연수</p>
							</c:if>
								<p class="pt">연 &nbsp;수 &nbsp;명 &nbsp;칭 : ${result.subjnm}</p>
								<p class="pt">연 &nbsp;수 &nbsp;기 &nbsp;간 : 
								${fn2:getFormatDate(result.edustart, 'yyyy')}년${fn2:getFormatDate(result.edustart, 'MM')}월${fn2:getFormatDate(result.edustart, 'dd')}일 ∼ 
								${fn2:getFormatDate(result.eduend, 'yyyy')}년${fn2:getFormatDate(result.eduend, 'MM')}월${fn2:getFormatDate(result.eduend, 'dd')}일</p>
								
								
							<c:if test="${result.empGubun eq 'T'}">
								<p class="pt">이 &nbsp;수 &nbsp;시 &nbsp;간 : ${result.edutimes}시간  ( ${result.point}학점  ) </p>
								<%-- <c:if test="${result.edutimes >= 60}">
								<p class="pt">성 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;적 : ${result.score}점</p>
								</c:if> --%>
								<c:if test="${p_scoreYn eq 'Y'}">
								<p class="pt">성 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;적 : ${result.score}점</p>
								</c:if>
								
								<c:if test="${not empty result.lecselno && result.lecselno ne 'null'}">
								<p class="pt">연수지명번호 : ${result.lecselno}</p>	
								</c:if>
							</c:if>
							<c:if test="${result.empGubun ne 'T'}">
								<p class="pt">이 &nbsp;수 &nbsp;시 &nbsp;간 : ${result.edutimes}시간  </p>
							</c:if>
							
						</td>
					</tr>
					<tr>
						<td height="65px">&nbsp;</td>
					</tr>
					<tr>
						<td  align="center">
							<p class="pt1" align="center">위와 같이 이수하였음을 증명합니다.</p> 
							
						</td>
					</tr>
				</table>
				<p class="pt" align="center">
					<strong>${result.gradYear}년 &nbsp; ${result.gradMonth}월 &nbsp; ${result.gradDay}일&nbsp;</strong>
				  
				  	<c:if test="${result.empGubun eq 'T'}">
					<br/><br/><br/>
					</c:if>
					<c:if test="${result.empGubun ne 'T'}">
					<br/><br/><br/><br/><br/>
					</c:if>
					
					<c:if test="${sessionScope.gadmin eq 'ZZ'}">
					<br/>
					<c:choose>
						<c:when test="${result.gradYear > '2012' and result.gradMonth > '02' }">
							<img src="/images/user/award1-2-1.gif" style="padding-top:20px;"/>
						</c:when>
						<c:otherwise>
							<img src="/images/user/award1-2.gif" style="padding-top:20px;"/>
						</c:otherwise>
					</c:choose>
					
					</c:if>
					
					<c:if test="${sessionScope.gadmin ne 'ZZ'}">
					<br/><br/><br/><br/>
					<c:if test="${empty result.lecselno}">
						<br/><br/>
					</c:if>
					</c:if>
				</p>
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