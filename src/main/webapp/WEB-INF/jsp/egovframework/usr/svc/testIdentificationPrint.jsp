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
<div id=>


    
	<table width="750" border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td>&nbsp;</td>
    </tr>   
	<tr>
    	<td><img src="/images/user/testIdentificationPrint_bgline.gif"/></td>
    </tr>
    
    <tr>
    	<td><img src="/images/user/testIdentificationPrint_logo.gif"/></td>
    </tr>
  	<tr> 
    	<td style="padding: 0px 6px 0 6px;">
    		<table width="720" border="0" align="center" cellpadding="0" cellspacing="0" class="txtList" summary="수험번호, 성명,생년월일, 소속, 고사장, 응시일시로 구성">
            <caption>영수내역</caption>
            <colgroup>
					<col width="10%" />
					<col width="10%" />
					<col width="10%" />
					<col width="16%" />
					<col />
					<col width="28%" />					                   
			</colgroup>
        		<tr bgcolor="#eaeaea"> 
          			<td height="15" ><strong>수험번호</strong></td>
          			<td height="15" ><strong>성명</strong></td>
          			<td height="15" ><strong>생년월일</strong></td>
          			<td height="15" ><strong>소속</strong></td>
          			<td height="15" ><strong>고사장</strong></td>
          			<td height="15"  class="left last"><strong>응시일시</strong></td>
        		</tr>
        		<tr> 
          			<td height="15" ><c:out value="${view.examnum}"/></td>
          			<td height="15" ><c:out value="${view.name}"/></td>
          			<td height="15" ><c:out value="${view.birthDate}"/></td>
          			<td height="15" ><c:out value="${view.userPath}"/></td>
          			<td height="15" ><c:out value="${view.gosa}"/></td>          			
          			<td height="15"  class="left last"><c:out value="${view.testDay}"/> ${view.testDayTime}</td>
        		</tr>
        		
      		</table>
      	</td>
  	</tr>
  	<tr> 
    	<td>
    		<table width="700" border="0" cellspacing="0" cellpadding="0" align="center">
        		<tr> 
          			<td style="padding:10px 0 0 10px;">
          				<p style="font-weight:bold;font-size:12px;">□ 응시자 유의사항</p>
            		</td>
        		</tr>
        		<tr> 
          			<td style="padding-left:20px;">
          				&bull;입실시간 : 10:40분까지 입실완료(11:30 정각 이후 입실불가)
            		</td>
        		</tr>
        		<tr> 
          			<td style="padding-left:20px;">
          				&bull;준비물 : 수험표, 신분증, 컴퓨터용 싸인펜
          			</td>
        		</tr>
				<tr>
					<td style="padding-left:20px;">
          				※ 답안 작성은 컴퓨터용싸인펜과 수정테이프만 가능(타 필기구 및 수정액 사용시 성적처리 불가)
						<br/>&bull;시험시간 : 50분(시험 중간에 휴식시간 없음)
            		</td>
        		</tr>
				<tr>
					<td style="padding-left:20px;">
          				※ 시험시작 30분후  퇴실가능
						<br/>&bull;시험당일고사장 내 주차가 어려울수 있으니 가급적 대중교통 이용 요망
            		</td>
        		</tr>
				<tr> 
          			<td style="padding:10px 0 0 10px;">
          				<p style="font-weight:bold;font-size:12px;">□ 시험시간 안내</p>
            		</td>
        		</tr>
				<tr> 
          			<td style="padding:10px;">
          				<table width="720" border="0" align="center" cellpadding="0" cellspacing="0" class="txtList" summary="일시, 내용으로 구성">
						<colgroup>
								<col width="30%" />
								<col width="70%" />					                   
						</colgroup>
							<tr bgcolor="#eaeaea"> 
								<td height="15" ><strong>일시</strong></td>
								<td height="15"  class="left last"><strong>내용</strong></td>
							</tr>
							<tr> 
								<td height="15" >당일 10:40</td>       			
								<td height="15"  class="left last">입실완료</td>
							</tr>
							<tr> 
								<td height="15" >당일 10:40 ~ 10:55</td>       			
								<td height="15" class="left last">신분증 확인 및 오리엔테이션</td>
							</tr>
							<tr> 
								<td height="15" >당일 11:00 ~ 11:50</td>       			
								<td height="15"  class="left last">시험(11:30 이후 퇴실 가능)</td>
							</tr>							
						</table>
            		</td>
        		</tr>
				<tr> 
          			<td style="padding:10px 0 0 10px;">          				
          				<p style="font-weight:bold;font-size:12px;">□ 국립특수교육원 부설 원격교육연수원 출석고사 신분증 확인요령</p>
            		</td>
        		</tr>
				<tr> 
          			<td style="padding:10px;">
          				<table width="720" border="0" align="center" cellpadding="0" cellspacing="0" class="txtList" summary="일시, 내용으로 구성">
						<colgroup>
								<col width="30%" />
								<col width="70%" />					                   
						</colgroup>
							<tr bgcolor="#eaeaea"> 
								<td height="15" ><strong>구분</strong></td>
								<td height="15"  class="left last"><strong>규정 신분증</strong></td>
							</tr>
							<tr> 
								<td height="15" >교원</td>       			
								<td height="15" class="left last">주민등록증, 운전면허증, 기간만료 전의 여권, 공무원증, 장애인 복지카드</td>
							</tr>
							<tr> 
								<td height="15" >교육전문직</td>       			
								<td height="15" class="left last">주민등록증, 운전면허증, 기간만료 전의 여권, 공무원증, 장애인 복지카드</td>
							</tr>							
						</table>
            		</td>
        		</tr>
				<tr> 
          			<td style="padding:10px 0 0 10px;">          				
          				<p style="font-weight:bold;font-size:12px;">□ 성적확인</p>
            		</td>
        		</tr>
        		<tr> 
          			<td style="padding-left:20px;">
          				&bull;답안 확인은 시험응시일 다음주 월요일에 확인가능<br/>
					 	&nbsp;&nbsp;[나의강의실-나의학습과정-학습완료과정(복습)-복습하기-공지사항]<br/>
						&bull;성적 확인은 [나의강의실-나의연수이력-성적보기]에서 확인가능(추후 공지)<br/>
						&bull;성적이의 신청기간은 시험응시일 다음주 일주일간입니다.<br/>
					  	&nbsp;&nbsp;성적에 이의가 있으실 경우 <br/>
					  	&nbsp;&nbsp;[나의강의실-나의학습과정-학습완료과정(복습)-복습하기-Q&A]에 글 작성
            		</td>
        		</tr>
      		</table>
      	</td>
  	</tr>
  	<tr>
    	<td>&nbsp;</td>
    </tr>   
	<tr>
    	<td><img src="/images/user/testIdentificationPrint_bgline.gif"/></td>
    </tr>
    <tr>
    	<td style="padding-top:10px;word-spacing: -2px;">[336-857] 충청남도 아산시 배방읍 공원로 40 국립특수교육원 원격교육연수원 | tel: 041-537-1475~8 | http://iedu.nise.go.kr/</td>
    </tr> 
</table>

</div>
<!-- // wrapper -->


<br/>
<div id="a">
	<br/>
	<ul class="btnCen">
	    <li><a href="#none" class="pop_btn01" onclick="onPrint()" id="bottomImg1" ><span>인쇄하기</span></a></li> 
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


function onPrint(){
	  document.getElementById("a").style.display="none";
	  window.print();
	  document.getElementById("a").style.display="";
	}

//-->
</script>



</body>
</html>