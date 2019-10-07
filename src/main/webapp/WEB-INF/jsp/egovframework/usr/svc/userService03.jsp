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
	<input type = "hidden" name="p_scoreYn" id="p_scoreYn" value="N" />
	<input type="hidden" name="p_upperclass"/>
	<input type="hidden" name="p_schoolparent" id="p_schoolparent">
	<input type="hidden" name="title" id="title">


		<!-- <p class="floatR"><a href="#none" onclick="open_permission()"><img src="/images/user/btn_dom_print.gif" alt="원격연수원 인가서 인쇄" /></a></p> -->
		<div class="sub_title">
			<ul>
                <li class="tred">이수증은 강의 종료 후 수료인 경우만 이수증 출력 버튼이 보입니다. </li>
				<li class="tred">연수성적 증명서는 인터넷 발급이 되지 않습니다. 고객센터에 전화로 문의해 주십시요.</li>
				<li class="tred">이수증에 연수성적을 표시하고 싶으시면 성적보기에 체크해주시기 바랍니다 .</li>
            </ul>
		</div>

<!-- list table-->
		<div class="studyList">
			<table summary="연수구분, 운영형태, 과정, 연수기간 등 으로 구성됨" cellspacing="0" width="100%">
				<caption>영수증/이수증</caption>
                <colgroup>
					<col width="6%" />
					<col width="6%" />
					<col width="%" />
					<col width="18%" />	
					<col width="8%" />	
					<col width="7%" />					
					<col width="10%" />					
					<col width="10%" />					
					<col width="10%" />					
				</colgroup>
				<thead>
					<tr>
						<th scope="col">연수<br>구분</th>
                    	<th scope="col">운영<br>형태</th>
						<th scope="col">과정</th>
						<th scope="col" class="">연수기간</th>
						<th scope="col">성적보기</th>
						<th scope="col" >이수증</th>						
						<th scope="col">성적보기</th>						
						<th scope="col">연수확인증<br>출력</th>
						<th scope="col" class="last">연수영수증<br>출력</th>
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
                       	 	<c:if test="${result.year >= 2010}">
                        	<a href="#none" onclick="fn_statusPopup('${result.subj}', '${result.year}', '${result.subjseq}', '${result.userid}' );" title="새창">
                        		<img src="/images/user/btn_study_view.gif" alt="성적보기" />
                        	</a>
                        	</c:if>
                        	<c:if test="${result.year < 2010}">
                        	-
                        	</c:if>
                        </td>
                        <td>
                        
                        <c:choose>
	                        
	                        <c:when test="${result.isgraduated eq 'Y'}">
	                        	<c:choose>	                        
	                        		
	                        		<c:when test="${result.suroyprint eq 'Y'}">
	                        			<a href="javascript:suRoyJeung('${result.curYear}', '${result.subj}', '${result.year}', '${result.subjseq}', '${status.count }', '${result.lecselno}', '${result.upperclass }', '${result.schoolparent}')" title="새창"><img src="/images/user/icon_print.gif" alt="수료증" /></a>                      		
	                        		</c:when>
	                        		
	                        		<c:otherwise>
	                        			수료처리중.    		
	                        		</c:otherwise>
	                        	</c:choose>
	                        
	                        	
	                        </c:when>
	                        
	                        <c:otherwise>
	                        	<c:choose>
		                        	<c:when test="${empty result.isgraduated && (result.edustart > fn2:getFormatDateNow('yyyyMMddHH'))}">
		                        	수강전
		                        	</c:when>
		                        	<c:when test="${empty result.isgraduated && (result.eduend >= fn2:getFormatDateNow('yyyyMMddHH'))}">
		                        	수강중
		                        	</c:when>
		                        	<c:when test="${empty result.isgraduated && (result.eduend < fn2:getFormatDateNow('yyyyMMddHH'))}">
		                        	수료처리중
		                        	</c:when>
		                        	<c:otherwise>
		                        	${result.isgraduatedValue}
		                        	</c:otherwise>
		                        	
		                        </c:choose>
	                        </c:otherwise>
                        </c:choose>
                        </td>
                        <td class="num">
                        	<input type="checkbox" name="p_check" id="p_check" value="${status.count }" title="성적보기">
                        </td>
                        <td class="num">
                        	<a href="#none" onclick="confirmPrint('${result.subj}','${result.year}','${result.subjseq}')"  title="새창">
							<img alt="확인증" src="/images/user/btn_identify.gif"/>
							</a>
                        </td>
                        <td class="last num">
                        <c:if test="${result.biyong > '0'}">
<!--						학부모가 아닌경우 영수증 출력-->
							<c:if test="${result.type !=null && result.type ne 'OB' && result.type ne 'RE' }">
								<a href="#none" onclick="whenCashPrint('${result.subj}','${result.year}','${result.subjseq}')" title="새창"><img src="/images/user/btn_y_print.gif" alt="영수증출력" /></a>
							</c:if>
						</c:if>
                        </td>
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
function suRoyJeung(p_cur_year, p_subj, p_year, p_subjseq, cnt,p_lecselno,p_upperclass, p_schoolparent){
	
	
	thisForm.p_subj.value = p_subj;
	thisForm.p_year.value = p_year;
	thisForm.p_subjseq.value = p_subjseq;
	thisForm.p_upperclass.value = p_upperclass;
	thisForm.p_schoolparent.value = p_schoolparent;
    
	if(p_upperclass == 'PRF' && p_year >= '3015') {	// 기존 2015인것을 3015로 변경하여 동작 무력화함
		
		if(p_lecselno == 'null'){
			alert('연수지명번호 미입력되셨습니다.\n연수지명번호 입력시 이수증 출력 가능합니다.');
			return;
		}
		if(p_lecselno == ''){
			alert('연수지명번호 미입력되셨습니다.\n연수지명번호 입력시 이수증 출력 가능합니다.');
			return;
		}
	
	}
	
	$('input:checkbox[name="p_check"]').each(function(){
		if(this.checked){
			if(this.value == cnt){
				$("#p_scoreYn").val("Y");
				return false;
			}
		}else{
			$("#p_scoreYn").val("N");
		}
	});
	
//	if(thisForm.p_check.length = "undefined"){
//		if(thisForm.p_check.checked){
//			alert("11");
//			 thisForm.p_scoreYn.value = "Y";
//		}else{
//			alert("22");
//			thisForm.p_scoreYn.value = "N";
//		}
//	}else{
//		if(thisForm.p_check[cnt-1].checked){
//			alert("!!");
//			 thisForm.p_scoreYn.value = "Y";
//		}else{
//			alert("@@");
//			thisForm.p_scoreYn.value = "N";
//		}	
//	}

	window.open('', 'suRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
	
	
	var c_url   = this.location+"";		
	if(c_url.match("localhost")){		
		thisForm.action = "/usr/myh/suRoyJeungPrint.do";
	}else{
		thisForm.action = "/usr/myh/suRoyJeungPrint.do";
	}
	
	
	
	thisForm.target = "suRoyJeungPop";
	thisForm.submit();
}

//확인증
function confirmPrint(p_subj, p_year, p_subjseq){

	thisForm.p_subj.value=p_subj;
	thisForm.p_year.value=p_year;
	thisForm.p_subjseq.value=p_subjseq;
	thisForm.title.value="연수확인증 출력";
	
	window.open('', 'confirmPrintWiindowPop', 'left=100,top=100,width=688,height=600,scrollbars=yes');
	
	if(c_url.match("localhost")){		
		thisForm.action = "/usr/myh/selectUserReqPrint.do";
	}else{
		thisForm.action = "/usr/myh/selectUserReqPrint.do";
	}
	
	
	thisForm.target = "confirmPrintWiindowPop";
	thisForm.submit();
}

//영수증
function whenCashPrint(p_subj, p_year, p_subjseq){

	thisForm.p_subj.value=p_subj;
	thisForm.p_year.value=p_year;
	thisForm.p_subjseq.value=p_subjseq;
	thisForm.title.value="연수영수증 출력";
	
	window.open('', 'whenCashPrintWindowPop', 'left=100,top=100,width=467,height=600, menubar=no,directories=no,resizable=yes,status=no,scrollbars=yes');
	
			
	if(c_url.match("localhost")){		
		thisForm.action = "/usr/myh/selectUserCashPrint.do";
	}else{
		thisForm.action = "/usr/myh/selectUserCashPrint.do";
	}
	
	
	thisForm.target = "whenCashPrintWindowPop";
	thisForm.submit();
}
//-->
</script>
        
        
        
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
