<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
<!--login check-->
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>



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
<fieldset>
<legend>연수행정실</legend>
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_userid"     value = "${sessionScope.userid}" />
	<input type = "hidden" name="p_subj" />
	<input type = "hidden" name="p_year" />
	<input type = "hidden" name="p_subjseq" />




		<div class="sub_title">
			<ul>
                <li class="tred">신용카드 결제인 경우 신용카드 영수증을 출력하실 수 있습니다.</li>
                <!-- <li class="tred">무통장입금, 가상계좌로 결재하신 경우 현금영수증이 발급됩니다. </li> -->
                <!-- <li class="tred">은행계좌로 직접 무통장 입급하신 경우 강의시작 14일 경과후에 현금영수증이 출력됩니다.(환불마감 후 발급) </li> -->
                <li class="tred">무통장입금, 계좌이체로 결재하신 경우 연수영수증이 발급됩니다.</li>
                <li class="tred">교육청 위탁과정, 특별과정 등 무료수강 하신경우 수강료 영수증 발급이 되지 않습니다.  </li>
                <li class="tred">수강료를 직접 납부하셨는데 영수증이 출력안될 경우 고객센터-연수관련상담 게시판에 문의하시면 확인해 드립니다.</li>
            </ul>
		</div>

<!-- list table-->
		<div class="studyList">
			<table summary="교육구분, 과정, 연수기간으로 구성" cellspacing="0" width="100%">
				<caption>연수행정실</caption>
                <colgroup>
					<col width="6%" />
					<col width="6%" />
					<col width="%" />
					<col width="18%" />	
					<col width="8%" />	
					<col width="10%" />	
					<col width="10%" />					
					<col width="12%" />					
<!--					<col width="8%" />-->
<!--                    <col width="15%" />-->
				</colgroup>
				<thead>
					<tr>
						<th scope="col">연수<br>구분</th>
                    	<th scope="col">운영<br>형태</th>
						<th scope="col">과정</th>
						<th scope="col" class="">연수기간</th>
						<th scope="col">수강료</th>
						
						<th scope="col" class="">연수확인증<br>출력</th>
						<th scope="col" class="">연수영수증<br>출력</th>
					<!--	<th scope="col" class="last">현금영수증<br>출력</th>	-->					
<!--						<th scope="col">배송상태</th>-->
<!--                        <th scope="col" class="last">택배사/송장번호</th>-->
					</tr>
				</thead>
				<tbody>               	
				
<c:forEach items="${list}" var="result" varStatus="status">				
					<tr>
						<td>
							<c:if test="${result.upperclass eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
							<c:if test="${result.upperclass eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
							<c:if test="${result.upperclass eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="교양연수"/></c:if>
							<c:if test="${result.upperclass eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
							<c:if test="${result.upperclass eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모연수"/></c:if>
	                    </td>
	                    <td>
	                     <c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
	                     <c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
	                     <c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
	                    </td>		
						<td class="left">
<!--						<a href="#none" onclick="goSubjInfo('${result.subj}','${result.year}','${result.subjseq}')">-->
						${result.subjnm}
<!--						</a>-->
						</td>
                        <td class="num">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
                        <td class="num">
                        <c:if test="${result.biyong eq '0'}">무료</c:if>
                        <c:if test="${result.biyong ne '0'}"><fmt:formatNumber value="${result.biyong}" type="number"/>원</c:if>
                        </td>
                        <td class=" num">
                        	<a href="#none" onclick="confirmPrint('${result.subj}','${result.year}','${result.subjseq}')" title="새창">
							<img alt="확인증" src="/images/user/btn_identify.gif"/>
							</a>
                        </td>
                        <td class=" num">
                        <c:if test="${result.biyong > '0'}">
<!--						학부모가 아닌경우 영수증 출력-->
							<c:if test="${result.type !=null && result.type ne 'OB' && result.type ne 'RE' }">
								<a href="#none" onclick="whenCashPrint('${result.subj}','${result.year}','${result.subjseq}')"><img src="/images/user/btn_y_print.gif" alt="영수증출력" /></a>
							</c:if>
						</c:if>
                        </td>
                     <!--    <td class="last num">
                        <c:if test="${result.biyong > '0'}">
	                        <c:choose>
						  	<c:when test="${result.type eq 'SC0010'}">
						  		<!-- 신용카드 -->
								<!-- authdata = md5(아이디+주문번호+martkey)  로 암호화를 위에서 실행함...주의) 암호화 전에 주문번호는 DB에서 불러와서 암호화 해야 함-->
						  	<!--	<a href="javascript:showReceiptByTID('believe2024', '${result.transactionId}', '${result.authdata}')" class="pop_btn01"><span>신용카드</span></a>
						  	</c:when>
						  	<c:when test="${result.type eq 'SC0020' or result.type eq 'PB'}">
								<!-- 무통장 입급  -->
						 <!-- 		<a href="javascript:showCashReceipts('believe2024','${result.orderId}','','CR','service')" class="pop_btn01"><span>무통장</span></a>
						  	</c:when>
						  	<c:when test="${result.type eq 'SC0030'}">
								<!--계좌이체 -->
						 <!-- 		<a href="javascript:showCashReceipts('believe2024','${result.orderId}','','BANK','service')" class="pop_btn01"><span>실시간 계좌이체</span></a>
						  	</c:when>
						  	<c:when test="${result.type eq 'SC0040'}">
								<!--가상계좌 --> 
						 <!-- 		<a href="javascript:showCashReceipts('believe2024','${result.orderId}','','CAS','service')" class="pop_btn01"><span>가상계좌</span></a>
						  	</c:when>
						  	<c:when test="${result.type eq 'OB'}">
						  		<img src="/images/user/btn_s_1.gif">
						  	</c:when>
						  	<c:when test="${result.type eq 'PB'}">
						  		<img src="/images/user/btn_s_2.gif">
						  	</c:when>
						  	<c:otherwise>
						  		<!--구사용자들중  구분,order_id값이 없는 사용자 영수증출력가능 / 무통장입금처리2010-05-19-->
						<!--  		<img src="/images/user/btn_s_3.gif">
						  	</c:otherwise>
						  </c:choose>
					  	</c:if>
                        </td> -->
<!--                        <td>-->
<!--                        <c:if test="${not empty result.deliveryStatus}">-->
<!--                        	${result.deliveryStatus}-->
<!--                        </c:if>-->
<!--                        <c:if test="${empty result.deliveryStatus}">-->
<!--                        	--->
<!--                        </c:if>-->
<!--                        </td>-->
<!--                        <td class="last left">-->
<!--                        <c:if test="${not empty result.deliveryComp && not empty result.deliveryNumber}">-->
<!--                        	* ${result.deliveryComp}<br/>-->
<!--                        	* ${result.deliveryNumber}-->
<!--                        </c:if>-->
<!--                        <c:if test="${empty result.deliveryComp || empty result.deliveryNumber}">-->
<!--                        	--->
<!--                        </c:if>-->
<!--                        </td>                        -->
					</tr>	
</c:forEach>
                </tbody>
			</table>
		</div>
		<!-- list table-->
	</fieldset>
</form>



        
        
        
<!--eCredit 영수증 연결 영역 시작  --> 
<script language="JavaScript" src="http://pgweb.dacom.net/WEB_SERVER/js/receipt_link.js" type="text/javascript"></script>     
<!--eCredit 영수증 연결 영역 종료 -->
        
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
var c_url   = this.location+"";


//연수원인가서 보기
function open_permission()
{
	window.open('/html/usr/hom/permission.html','permission_window','width=670,height=1000,scrollbars=yes');
}


//상세화면
function goSubjInfo(p_subj, p_year, p_subjseq){

	thisForm.p_subj.value=p_subj;
	thisForm.p_year.value=p_year;
	thisForm.p_subjseq.value=p_subjseq;
	   
	thisForm.action = "/usr/myh/celOfficeDetailList.do";
	thisForm.target = "_self";
	thisForm.submit();

}


//확인증
function confirmPrint(p_subj, p_year, p_subjseq){

	thisForm.p_subj.value=p_subj;
	thisForm.p_year.value=p_year;
	thisForm.p_subjseq.value=p_subjseq;
	
	window.open('', 'confirmPrintWiindowPop', 'left=100,top=100,width=688,height=600,scrollbars=yes');
	
	if(c_url.match("localhost")){		
		thisForm.action = "/usr/myh/selectUserReqPrint.do";
	}else{
		thisForm.action = "http://iedu.nise.go.kr/usr/myh/selectUserReqPrint.do";
	}
	
	
	thisForm.target = "confirmPrintWiindowPop";
	thisForm.submit();
}

//영수증
function whenCashPrint(p_subj, p_year, p_subjseq){

	thisForm.p_subj.value=p_subj;
	thisForm.p_year.value=p_year;
	thisForm.p_subjseq.value=p_subjseq;
	
	window.open('', 'whenCashPrintWindowPop', 'left=100,top=100,width=467,height=600, menubar=no,directories=no,resizable=yes,status=no,scrollbars=yes');
	
			
	if(c_url.match("localhost")){		
		thisForm.action = "/usr/myh/selectUserCashPrint.do";
	}else{
		thisForm.action = "http://iedu.nise.go.kr/usr/myh/selectUserCashPrint.do";
	}
	
	
	thisForm.target = "whenCashPrintWindowPop";
	thisForm.submit();
}

//수료증출력
function suRoyJeung(p_subj, p_year, p_subjseq){
	thisForm.p_subj.value = p_subj;
	thisForm.p_year.value = p_year;
	thisForm.p_subjseq.value = p_subjseq;
	
	window.open('', 'suRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
	thisForm.action = "/usr/myh/suRoyJeungPrint.do";
	thisForm.target = "suRoyJeungPop";
	thisForm.submit();
}

//-->
</script>
        
        
        
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
