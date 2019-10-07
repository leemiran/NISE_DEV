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
<object id="factory" classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814" codebase="/script/ScriptX.cab#Version=6,1,429,14">
</object>


<div id="idPrint">
	<table border="0" cellpadding="5" cellspacing="0" bordercolordark="white" bordercolorlight="black" width="640" align="center">
		<tr>
			<td>
				<br/><br/>
				<p class="pt2" align="center">교육연수이수증</p>
				<br/><br/><br/><br/>
				<table align="center" border="0" cellpadding="0" cellspacing="0" width="90%">
					<tr>
						<td width="563">
						
						
								<p class="pt">증 &nbsp;서 &nbsp;번 &nbsp;호 : ${view.compnumber}</p>
							    <p class="pt">성 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명 : ${view.name}</p>
								
								<p class="pt">연 &nbsp;수 &nbsp;종 &nbsp;별 : ${view.gubun}</p>
								<p class="pt">연 &nbsp;수 &nbsp;명 &nbsp;칭 : ${view.couname}</p>
								<p class="pt">연 &nbsp;수 &nbsp;기 &nbsp;간 : 
								${view.stDate} ∼ 
								${view.edDate}</p>
								
								<p class="pt">이 &nbsp;수 &nbsp;시 &nbsp;간 : ${view.edutime}시간</p>
								<p class="pt">성 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;적 : ${view.score}점</p>
							
						</td>
					</tr>
					<tr>
						<td height="65px">&nbsp;</td>
					</tr>
					<tr>
						<td width="563">
							<p class="pt1" align="center">위와 같이 이수하였음을 증명합니다.</p> 
							
						</td>
					</tr>
				</table>
				<p class="pt" align="center">
					<strong>${fn2:getFormatDate(view.ldate, 'yyyy')}년 &nbsp; ${fn2:getFormatDate(view.ldate, 'MM')}월 &nbsp; ${fn2:getFormatDate(view.ldate, 'dd')}일&nbsp; </strong>
				  <br/><br/><br/><br/><br/>
					<img src="/images/user/award1-2.gif"/>
				</p>
				<br/>
			</td>
		</tr>
	</table>   
</div>
<p style='page-break-after:always;'/>
<br/>



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
			return;
		}
		while(factory){
			factory.printing.header		   = "";
			factory.printing.footer		   = "";
			factory.printing.portrait	   = true;
			factory.printing.leftMargin = 0;
			factory.printing.topMargin = 30;
			factory.printing.rightMargin = 0;
			factory.printing.bottomMargin = 0;
			factory.printing.Preview(window); 
			//factory.printing.Print(false, window)
		}

	}
</script> 