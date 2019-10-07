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
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
		<input type="hidden"  name="p_subj" value="">
		<input type="hidden"  name="p_year" value="">
		<input type="hidden"  name="p_subjseq" value="">
		<input type="hidden"  name="p_subjnm" value="">
		<input type="hidden"  name="p_order_id" value="">


<!-- tab -->     
                <div class="conwrap2" id="tmenu1">
                    <ul class="mtab2">
                        <li><a href="#none" class="on" >수강신청중</a></li> 
                        <li class="end"><a href="#none" onclick="DisplayMenu(2)">취소/반려</a></li>                                     
                    </ul>
                </div>
                
                <div class="conwrap2" id="tmenu2" style="display:none;">
                    <ul class="mtab2">
                        <li><a href="#none" onclick="DisplayMenu(1)">수강신청중</a></li> 
                        <li class="end"><a href="#none" class="on" >취소/반려</a></li>                                     
                    </ul>
                </div>
                <!-- //tab --> 
               
                
                <!-- list table-->
                
                
<!--                수강신청-->
                    <div class="studyList" id="dmenu1" style="display:block;">
                        <table summary="교육구분, 과정명, 신청기간, 연수기간, 상태로 구분" cellspacing="0" width="100%">
                <caption>수강신청 목록</caption>
                <colgroup>
                                <col width="10%" />
                                <col width="40%" />
                                <col width="20%" />
                                <col width="20%" />
                                <col width="10%" />                                
                            </colgroup>
                            <thead>
                                <tr>
                                    <th scope="row">교육구분</th>
                                    <th scope="row">과정명</th>
                                    <th scope="row">신청기간</th>
                                    <th scope="row">연수기간</th>
                                    <th scope="row" class="last">상태</th>
                                </tr>
                            </thead>
                            <tbody>		
<c:forEach items="${cancPosList}" var="result" varStatus="status">                            			
                                <tr>
                                    <td>${result.codenm}</td>
                                    <td class="left">
                                    <a href="#none" onclick="whenSubjInfoViewPopup('${result.subj}')" title="새창">
                                    ${result.subjnm}
                                    </a>
                                    </td>
                                    <td class="num">${fn2:getFormatDate(result.propstart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.propend, 'yyyy.MM.dd')}</td>
                                    <td class="num">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
                                    <td class="last">
                                    
                                    <c:if test="${result.chkfinal eq 'Y'}"><img src="/images/user/btn_approval.gif"></c:if>
                                    <c:if test="${result.chkfinal eq 'N'}"><img src="/images/user/btn_return.gif"></c:if>
                                    <c:if test="${result.chkfinal eq 'B'}">
                                    	<img src="/images/user/b_submiting.gif">
                                    	<c:if test="${fn2:getFormatDate(result.edustart, 'yyyyMMdd') <= fn2:getFormatDateNow('yyyyMMdd')}">
<!--                                    		<a href="#none" onclick="alert('학습이 시작되어  수강취소가 불가능합니다.')"><img src="/images/user/btn_cancellation.gif"></a>-->
                                    	</c:if>
                                    	
                                    	<c:if test="${fn2:getFormatDate(result.edustart, 'yyyyMMdd') > fn2:getFormatDateNow('yyyyMMdd')}">
<!--                                    		<a href="#none" onclick="whenCancel('${result.subj}','${result.year}','${result.subjseq}','${result.orderId}','${result.subjnm}')"><img src="/images/user/btn_cancellation.gif"></a>-->
                                    	</c:if>
                                    </c:if>
                                    
                                    </td>
                                   
                                </tr> 
</c:forEach>
<c:forEach items="${couCancList}" var="result" varStatus="status">                            			
                                <tr>
                                    <td>${result.codenm}</td>
                                    <td class="left">
                                    <a href="#none" onclick="whenSubjInfoViewPopup('${result.subj}')" title="새창">
                                    ${result.subjnm}
                                    </a>
                                    </td>
                                    <td class="num">${fn2:getFormatDate(result.propstart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.propend, 'yyyy.MM.dd')}</td>
                                    <td class="num">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
                                    <td class="last">
                                    <c:if test="${result.chkfinal eq 'Y'}"><img src="/images/user/btn_approval.gif"></c:if>
                                    <c:if test="${result.chkfinal eq 'N'}"><img src="/images/user/btn_return.gif"></c:if>
                                    <c:if test="${result.chkfinal eq 'B'}">
                                    	<img src="/images/user/b_submiting.gif">
                                    	<c:if test="${fn2:getFormatDate(result.edustart, 'yyyyMMdd') <= fn2:getFormatDateNow('yyyyMMdd')}">
<!--                                    		<a href="#none" onclick="alert('학습이 시작되어  수강취소가 불가능합니다.')"><img src="/images/user/btn_cancellation.gif"></a>-->
                                    	</c:if>
                                    	
                                    	<c:if test="${fn2:getFormatDate(result.edustart, 'yyyyMMdd') > fn2:getFormatDateNow('yyyyMMdd')}">
<!--                                    		<a href="#none" onclick="whenCancel('${result.subj}','${result.year}','${result.subjseq}','${result.orderId}','${result.subjnm}')"><img src="/images/user/btn_cancellation.gif"></a>-->
                                    	</c:if>
                                    </c:if>
                                    
                                    </td>
                                   
                                </tr> 
</c:forEach>

<c:if test="${empty couCancList && empty cancPosList}">
								<tr>
                                    <td colspan="5" class="last">검색된 정보가 없습니다.</td>
                                </tr> 
</c:if>
                            </tbody>
                        </table>
                    </div>
                    
                    
<!--                    취소/반려-->
                   <div class="studyList" id="dmenu2" style="display:none;">
                        <table summary="교육구분, 과정명, 신청기간, 연수기간, 상태로 구분" cellspacing="0" width="100%">
                <caption>취소/반려 목록</caption>
                <colgroup>
                                <col width="46%" />
                                <col width="12%" />
                                <col width="12%" />
                                <col width="30%" />           
                            </colgroup>
                            <thead>
                                <tr>
                                    <th scope="row">과정명</th>
                                    <th scope="row">취소/반려</th>
                                    <th scope="row">취소/반려일</th>
                                    <th scope="row" class="last">취소/반려사유</th>
                                </tr>
                            </thead>
                            <tbody>		
<c:forEach items="${cancelList}" var="result" varStatus="status">                            			
                                <tr>
                                    <td class="left">
                                    <a href="#none" onclick="whenSubjInfoViewPopup('${result.subj}')" title="새창">
                                    ${result.subjnm}
                                    </a>
                                    </td>
                                    <td class="num">${result.cancelkind}</td>
                                    <td class="num">${fn2:getFormatDate(result.canceldate, 'yyyy.MM.dd')}</td>
                                    <td class="last">
                                    ${result.reason}
                                    </td>
                                   
                                </tr> 
</c:forEach>
                            </tbody>
                        </table>
                    </div>                    
                    <!-- list table-->

		
           
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function DisplayMenu(index) {
	for (var i=1; i<=2; i++)
		if (index == i) {
		thisMenu = eval("document.getElementById('dmenu" + index + "').style");
		thisMenu.display = "";
		thisMenu1 = eval("document.getElementById('tmenu" + index + "').style");
		thisMenu1.display = "";		
		} 
        else {
		otherMenu = eval("document.getElementById('dmenu" + i + "').style"); 
		otherMenu.display = "none"; 
		otherMenu1 = eval("document.getElementById('tmenu" + i + "').style"); 
		otherMenu1.display = "none"; 		
		}
}
		
//수강신청 취소
function whenCancel(p_subj, p_year, p_subjseq, p_order_id, p_subjnm){  
	thisForm.p_subj.value = p_subj;
	thisForm.p_year.value = p_year;
	thisForm.p_subjseq.value = p_subjseq;
	thisForm.p_order_id.value = p_order_id;
	thisForm.p_subjnm.value = p_subjnm;

	if(confirm("[ " + thisForm.p_subjnm.value + " ] 과정을 취소하시겠습니까?"))
	{
		window.open("", "propCancelPopupWindow", "width=500,height=400");
		
		thisForm.action = "/usr/subj/propCancelPopup.do";
		thisForm.target = "propCancelPopupWindow";
		thisForm.submit();
	}
	else
	{
		thisForm.p_subj.value = "";
		thisForm.p_year.value = "";
		thisForm.p_subjseq.value = "";
		thisForm.p_order_id.value = "";
		thisForm.p_subjnm.value = "";
			
	}
	
}
//-->
</script>