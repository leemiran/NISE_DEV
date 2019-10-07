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
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonPopUpHead.jsp" %>

<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
<!--login check-->

</head>

<body style="background:none;">

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
		<input type="hidden"  name="p_subj" value="${p_subj}" />
		<input type="hidden"  name="p_year" value="${p_year}" />
		<input type="hidden"  name="p_subjseq" value="${p_subjseq}" />
		<input type="hidden"  name="p_hrdc2" value="${p_hrdc2}" />
		<input type="hidden"  name="p_post1" value="${p_post1}" />
		<input type="hidden"  name="p_post2" value="${p_post2}" />
		<input type="hidden"  name="p_address1" value="${p_address1}" />
		<input type="hidden"  name="p_is_attend" value="${p_is_attend}" />
		<input type="hidden"  name="p_addrFlag" value="${p_addrFlag}" />
		<input type="hidden"  name="p_usebook_yn" value="${v_usebook_yn}" />
		
		
		<c:forEach var="sel_no" items="${p_lec_sel_no}">
 				<input type="hidden"  name="p_lec_sel_no" value="${sel_no}" />
        </c:forEach>
		
		
		
<div id="money"><!-- 팝업 사이즈 800*650 -->
	<div class="con2">
		<div class="popCon">
			<!-- header -->
			<div class="tit_bg">
			<h2>수강신청</h2>
       		</div>
			<!-- //header -->







<!-- contents -->
			<div class="mycon">				
            	
              <!-- 과정타이틀 -->
              <div class="bbsList3">
				<ul>
					<li>
						<dl>
						
<c:set var="v_biyong" value="0" />
<c:set var="v_upperclass" value="" />



<c:forEach items="${subjinfo}" var="result" varStatus="status"> 

	<c:set var="v_biyong" value="${result.biyong + v_biyong}" />		
	<c:set var="subjnmVal" value="${result.subjnm}" />
	<c:if test="${status.index == 0}">
				<c:set var="v_upperclass" value="${result.upperclass}" />
				<c:set var="v_ischarge" value="${result.ischarge}" />
	</c:if>						
							<dt class="subject">
                            	<span class="head">강의명</span>
								<span class="txt" style="font-weight:bold;color:blue;">${result.subjnm}</span>	                              
                          </dt>
                            <dd class="info">
								<span class="head">연수비</span>
								<span class="txt">
									<c:choose>
										<c:when test="${v_ischarge eq 'S'}">
						           			교육청일괄납부						             
						           		</c:when>
						           		<c:otherwise>
						           			<fmt:formatNumber value="${result.biyong}" type="number"/>원
						           		</c:otherwise>
					           		</c:choose>
								</span>
							</dd> 
</c:forEach>			




				
                            <dd class="info">
								<span class="head">결제방식</span>
								<span class="txt">
								<input type="hidden" name="p_ischarge" value="${v_ischarge}"/>
								<!--<c:if test="${v_upperclass eq 'PAR' || v_upperclass eq 'OTH' || v_upperclass eq 'SPC'}">
									<input type="radio" name="p_pay_sel" value="OB" onclick="div_check(this.value);" checked class="vrM"/>교육청일괄납부 
						            <script>
						               window:onload = function () {document.getElementById("enterancedt").style.display="none"; }
									</script> 
								</c:if>
								<c:if test="${v_upperclass ne 'PAR' && v_upperclass ne 'OTH' && v_upperclass ne 'SPC'}">
									<input type="radio" name="p_pay_sel" value="PB" onclick="div_check(this.value);" class="vrM"/>무통장
					               	<input type="radio" name="p_pay_sel" value="OB" onclick="div_check(this.value);" class="vrM"/>교육청일괄납부  
					               	<input type="radio" name="p_pay_sel" value="SC0030" onclick="div_check(this.value);" class="vrM"/>실시간 계좌이체 
					               	<input type="radio" name="p_pay_sel" value="SC0010" onclick="div_check(this.value);" class="vrM"/>카드결제
					           </c:if>
					           --><c:choose>
					           	<c:when test="${v_ischarge eq 'C'}">
					           		
					           		<input type="radio" name="p_pay_sel" value="PB" onclick="div_check(this.value);" class="vrM"/>무통장
					               	<input type="radio" name="p_pay_sel" value="OB" onclick="div_check(this.value);" class="vrM"/>교육청일괄납부  
					               	<input type="radio" name="p_pay_sel" value="SC0030" onclick="div_check(this.value);" class="vrM"/>실시간 계좌이체 
					               	<input type="radio" name="p_pay_sel" value="SC0010" onclick="div_check(this.value);" class="vrM"/>카드결제
					           	</c:when>
					           	<c:when test="${v_ischarge eq 'S'}">
					           		<input type="radio" name="p_pay_sel" value="OB" onclick="div_check(this.value);" checked class="vrM"/>교육청일괄납부 
						            <script>
						               window:onload = function () {document.getElementById("enterancedt").style.display="none"; }
									</script> 
					           	</c:when>
					           	<c:otherwise>
					           		무료
					           		<input type="radio" name="p_pay_sel" value="FE" checked style="display:none;"/>
					           	</c:otherwise>
					           </c:choose>
                                </span>
							</dd>                              
					  </dl>
                        
				  </li>					
				</ul>
			</div>
            <!-- //과정타이틀 -->  
            
            <ul class="course_1">
                	<li class="last_2">시.도 교육청 특별과정 신청시 교육청일괄납부 결제방식 선택후 결제하기 버튼 클릭하세요(연수비 결제되지 않습니다. - 연수신청 순서)</li>
                	<li class="last_2">“일반연수는 교육청일괄납부 선택 후 다음단계 진행하세요.”</li>
              </ul>                    
                
             <ul class="course_1">
                	<li><strong>시도교육청 위탁연수</strong>의 수강 신청시 교육청일괄납부 선택</li>
                	<li class="last_1">※ 시도교육청 요청 단체연수는 시도교육청에서 선발합니다.<br />(통보된 선발자 명단에 없으면 수강신청 무효)</li>
                	
                	<c:if test="${v_ischarge ne 'S'}">                	
	                    <li><strong>직무연수과정 </strong>등 다른 과정은 아래 계좌로 입금하시기 바라며 입금 전까지는 수강신청중으로 유지됩니다.</li>
	                    <li><strong>현금영수증</strong>은 강의시작 후 나의강의실 > 연수행정실 > 영수증출력 에서 발급 받으실 수 있습니다.</li>
                    </c:if>
              </ul> 
                
              
	          <c:if test="${v_ischarge ne 'S'}">
	          <div  id="enterancedt">      
	              <div class="sub_text" style="border-bottom:1px solid #e2e2e2;">
	                    <h4>무통장 입금 계좌안내</h4>
	              </div> 
	              <ul class="course_1">
	                	<li>은행명 : 우체국</li>                	
	                    <li>계좌번호 : 301622-01-000041</li>
	                    <li>예금주 : 국립특수교육원</li>
	                    <li class="last">
	                    입금예정일 : 
	                    <input id="p_enterance_dt" name="p_enterance_dt" type="text" maxlength="10"  OnClick="popUpCalendar(this, this, 'yyyy-mm-dd')" value=""> ex) 2014-01-01
	                    </li>
	                    <li class="last_1">※ <!-- 무통장 입금하신 후에 고객센터 (입금확인 요청) 게시판에 글을 남겨 주시면 빠른 수강승인이 가능합니다. -->
	                    무통장입금은 <strong>수강신청인과  입금인의 성함이 다를 경우</strong>, <br/>
	                    연수행정코너 입금확인요청 게시판에 글을 남겨주시면 빠른 수강승인 가능합니다.
	                    </li>
	                    <li class="last_1">※ 신용카드 및 계좌와 관련된 모든 정보 입력은 (주)LG테이콤의 eCredit에서 이루어지면 귀하의 신용카드 관련 정보를 저정하거나 입력 받지 않습니다.</li>
	              </ul>    
	          </div>    
			  </c:if>	
            
              <!-- button -->
              <ul class="btnCen mrt20">
                <li><a href="#none" class="pop_btn01" onclick="gen()"><span>다 음 ▶</span></a></li>                
              </ul>
              <!-- // button -->
                
                
		  </div>
			<!-- //contents -->
			
			
			
			
			
			
			
			
			
			
		</div>
	</div>
	
<!-- 페이지 정보 -->
	<%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
	<!-- 페이지 정보 -->
</div>
<!-- // wrapper -->

</form>


<!--아래는 팝업과 뷰를 따로 스크립트를 프로그램한다.-->
<script type="text/javascript">
<!--
var frm = eval('document.<c:out value="${gsMainForm}"/>');



var now = new Date();   //현재시간

/* 년 */
var year = now.getFullYear();   //현재시간 중 4자리 연도

/* 월 */
var month = now.getMonth()+1;   //현재시간 중 달. 달은 0부터 시작하기 때문에 +1 해준다.
if((month+"").length < 2){
	month="0"+month;   //달의 숫자가 1자리면 앞에 0을 붙여서 01, 02, 03 등으로 표현
}

/* 일 */
var date = now.getDate();      //현재 시간 중 날짜.
if((date+"").length < 2){
	date="0"+date;      //날짜도 1자리면 앞에 0을 붙여서 01, 02, 03 등으로 표현한다.
}

var today = year + "-" + month + "-" + date;      //오늘 날짜 완성.


frm.p_enterance_dt.value = today;



function div_check(val){
	if(val=="PB" || val == "SC0030")
		document.getElementById("enterancedt").style.display="block";
	else
		document.getElementById("enterancedt").style.display="none";
}


function checkPay(){
	var obj = document.getElementsByName("p_pay_sel"); 
	var cnt=0;
	if(frm.p_pay_sel.length)
	{
		for(i=0;i<obj.length;i++){
			if(obj[i].checked==true){
				cnt++;
			}
		}
		
		if(cnt==0){
			alert('결제방식은 무통장으로 선택되었습니다.');
			obj[0].checked=true;
		}
	}
}

function gen() {
	var chk = false;
	var cart = "";
	if(frm.p_pay_sel.length)
	{
		for (var i=0;i<frm.p_pay_sel.length;i++) {
			if (frm.p_pay_sel[i].checked) {
				chk = true;
				cart = frm.p_pay_sel[i].value;
			}
		}
	}
	else if (frm.p_pay_sel.checked)
	{
		chk = true;
		cart = frm.p_pay_sel.value;
	}

	if(!chk)
	{
		alert("결제방식을 선택하세요");
		return;
	}
	else
	{
		if(cart == 'PB')
		{
			if(frm.p_enterance_dt.value == "")
			{
				alert("입금예정일을 입력해주세요");
				return;
			}
		}
		
		frm.action = "/usr/subj/subjProposeStep03.do";
		frm.target = "_self";
		frm.submit();
	}
}


document.title="수강신청 2단계("+"<c:out value='${subjnmVal}'/> )"+" : 개설교육과정/신청";
//-->
</script>



</body>
</html>