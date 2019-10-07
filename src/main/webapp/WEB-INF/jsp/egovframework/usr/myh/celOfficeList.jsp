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
		<p class="floatR"><a href="#none" onclick="open_permission()"><img src="/images/user/btn_dom_print.gif" alt="원격연수원 인가서 인쇄" /></a></p>
			<ul>
                <li class="tred">연수수강 내역을 확인 하실수 있습니다.</li>
            </ul>
		</div>

<!-- list table-->
		<div class="studyList">
			<table summary="교육구분, 과정, 연수기간으로 구성" cellspacing="0" width="100%">
				<caption>연수행정실</caption>
                <colgroup>
					<col width="7%" />
					<col width="7%" />
					<col width="%" />
					<col width="20%" />	
					<col width="10%" />	
					<col width="20%" />	
					<col width="20%" />					
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
						
						<th scope="col" class="">연수지명번호</th>
						<th scope="col" class="last">출석고사장</th>						
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
						<td class="left"><a href="#none" onclick="goSubjInfo('${result.subj}','${result.year}','${result.subjseq}')">${result.subjnm}</a></td>
                        <td class="num">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
                        <td class="num">
                        <c:if test="${result.biyong eq '0'}">무료</c:if>
                        <c:if test="${result.biyong ne '0'}"><fmt:formatNumber value="${result.biyong}" type="number"/>원</c:if>
                        </td>
                        <td class=" num">${result.lecselno}</td>
                        <td class="last num">${result.isattendname}</td>
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



        
        
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

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
