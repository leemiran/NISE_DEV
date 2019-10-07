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
	<input type = "hidden" name="p_is_attend" />

	<input type = "hidden" name="p_redirect_url" value="${requestScope["javax.servlet.forward.request_uri"]}" />




		<div class="sub_title">
			<ul>
                <li class="tred">출서고사 3주전까지 출석고사장을 입력/변경하셔야 합니다.(3주이후에는 변경불가합니다.)</li>
                <li class="tred">61시간 이상 연수만 선택합니다.</li>
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
					<col width="10%" />	
					<col width="15%" />			
					<col width='28%' />	
					<col width="7%" />	
					<col width="7%" />	
					<col width="7%" />			
<!--					<col width="8%" />-->
<!--                    <col width="15%" />-->
				</colgroup>
				<thead>
					<tr>
						<th scope="col">연수<br>구분</th>
                    	<th scope="col">운영<br>형태</th>
						<th scope="col">과정</th>
						<th scope="col" class="">연수기간</th>
						<th scope="col" class="">출석고사일</th>
						<th scope="col" >출석고사장</th>
						<th scope="col" class="">확인</th>
						<th scope="col" >위치</th>
						<th scope="col" class="last">수험표<br/> 출력</th>						
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
                        <td>${result.testDay}<br/>${result.testDayTime}</td>
                        <td class="" style="text-align:left;padding-left:4px;">
                        <ui:code id="p_is_attend_${status.count}" selectItem="${result.isAttend}" gubun="schoolRoom" codetype="${result.newroom}"  upper="" title="출석고사장" className="'" type="select" selectTitle="::선택::" event="" itemRowCount="1"/>
                        </td>
                        <td class=" num">
                        <c:if test="${result.insYn eq 'Y'}">
                        <a href="javascript:;" onclick="whenSave('${result.subj}','${result.year}','${result.subjseq}', 'p_is_attend_${status.count}' )">
                        <img src="/images/user/user_save_btn.gif" alt="저장">
                        </a>
                        </c:if>
                        <c:if test="${result.insYn eq 'N'}">
                        -
                        </c:if>
                        </td>
                        <td>
                        <a href="javascript:;" onclick="schoolAreaMap('p_is_attend_${status.count}')">
                        <img src="/images/user/user_confirm_btn.gif" alt="확인">
                        </a>
                        </td>
                        <td class="last num">
                        	<a href="#none" onclick="confirmTestIdentificationPrint('${result.subj}','${result.year}','${result.subjseq}')">
							<img src="/images/user/btn_print.gif" alt="출력">
							</a>
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
function suRoyJeung(p_subj, p_year, p_subjseq){
	thisForm.p_subj.value = p_subj;
	thisForm.p_year.value = p_year;
	thisForm.p_subjseq.value = p_subjseq;
	
	window.open('', 'suRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
	thisForm.action = "/usr/myh/suRoyJeungPrint.do";
	thisForm.target = "suRoyJeungPop";
	thisForm.submit();
}



//상태저장
function whenSave(p_subj, p_year, p_subjseq, v){

	if($("#" + v + " option:selected").val() == "")
	{
			alert("출석고사장을 선택해 주세요!");
			$("#" + v).focus();
			return;
	}
	

	thisForm.p_subj.value = p_subj;
	thisForm.p_year.value = p_year;
	thisForm.p_subjseq.value = p_subjseq;

	thisForm.p_is_attend.value = $("#" + v + " option:selected").val();
	
	if(confirm('저장하시겠습니까?')){
	 	thisForm.action='/usr/myh/celOfficeDetailAction.do';
	  	thisForm.target = "_self";
	  	thisForm.submit();
	}
}


//출석고사장 위치정보 
function schoolAreaMap(v)
{
	if($("#" + v + " option:selected").val() == "")
	{
			alert("출석고사장을 선택해 주세요!");
			$("#" + v).focus();
			return;
	}

	$.ajax({  
			url: "/com/aja/svc/schoolAreaMapUrl.do",  
			data: {p_seq : $("#" + v + " option:selected").val()},
			dataType: 'json',
			contentType : "application/json:charset=utf-8",
			success: function(data) {   
				//console.log(data.result.areaMap);
				if(data.result)
				{
					if(data.result.areaMap)
					{
						//window.open(data.result.areaMap);

						 $('.all_knise_modal_popup').bPopup({
							 	content:'iframe', //'ajax', 'iframe' or 'image'
					            contentContainer:'.all_knise_modal_popup_content',
					            iframeAttr : 'scrolling="auto" width="100%" height="600" frameborder="0"',
					            loadUrl:data.result.areaMap //Uses jQuery.load()
						   });
					}
					else
					{
						alert("위치 정보가 존재하지 않습니다.")
					}
				}
			},    
			error: function(xhr, status, error) {   
				alert(status);   
				alert(error);    
			}   
		});   

	
}

//출석고사수험표
function confirmTestIdentificationPrint(p_subj, p_year, p_subjseq){

	thisForm.p_subj.value=p_subj;
	thisForm.p_year.value=p_year;
	thisForm.p_subjseq.value=p_subjseq;
	
	window.open('', 'confirmTestIdentificationPrintPop', 'left=100,top=100,width=688,height=760,scrollbars=yes');
	thisForm.action = "/usr/svc/testIdentificationPrint.do";
	thisForm.target = "confirmTestIdentificationPrintPop";
	thisForm.submit();
}


//-->
</script>
        
        
        
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
