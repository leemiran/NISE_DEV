<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>


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
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" name="p_process" />
		
	
	
	 <!-- search wrap-->
		<div class="stduyWrap">
			<ul class="floatL">
            	<li>관심과정입니다.</li>
            </ul>            
		</div>
		<!-- // search wrap -->	
		
		<div class="sub_text_2">
        	<h4>관심과정</h4>
            <span><a href="#" onclick="delSubjConcern();return false;"><img src="/images/user/btn_all_del.gif" alt="삭제" /></a></span>            
            <span><a href="#" onclick="discheck(<c:out value="${gsMainForm}"/>);return false;"><img src="/images/user/btn_all_select01.gif" alt="선택해제" /></a></span>
            <span><a href="#" onclick="allcheck(<c:out value="${gsMainForm}"/>);return false;"><img src="/images/user/btn_all_select.gif" alt="전체선택" /></a></span>
        </div>
        
        <!-- list table-->
		<div class="studyList">
			<table summary="선택, 구분, 과정분류, 과정명, 상태로 구분" cellspacing="0" width="100%">
				<caption>관심과정</caption>
                <colgroup>
					<col width="10%" />
					<col width="10%" />
                    <col width="20%" />
                    <col width="50%" />
					<col width="10%" />					
				</colgroup>
				<thead>
					<tr>
						<th scope="row">선택</th>
						<th scope="row">구분</th>
                        <th scope="row">과정분류</th>
                        <th scope="row">과정명</th>
						<th scope="row" class="last">상태</th>						
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${list}" var="result" varStatus="status">
				
				<c:set var="v_status">-</c:set>
				
				<c:if test="${result.proposeGu eq 'Y'}">
					<c:set var="v_status">수강신청</c:set>
				</c:if>
				<c:if test="${result.proposeGu eq 'N'}">
					<c:set var="v_status">반려</c:set>
				</c:if>
				
				<c:if test="${result.studentGu eq 'Y'}">
					<c:set var="v_status">수강</c:set>
				</c:if>
				<c:if test="${result.stoldGu eq 'Y'}">
					<c:set var="v_status">수료</c:set>
				</c:if>
				<c:if test="${result.stoldGu eq 'N'}">
					<c:set var="v_status">미수료</c:set>
				</c:if>
				<c:if test="${result.planGu eq 'Y'}">
					<c:set var="v_status">학습계획</c:set>
				</c:if>
				
					<tr>
						<td><label for="_Array_p_checks_${result.subj}" class="blind">선택</label><input type="checkbox" name="_Array_p_checks" id="_Array_p_checks_${result.subj}" value="${result.subj}" class="input_border"  title="선택"/></td>						
						<td>${result.isonoffvalue}</td>
                        <td>${result.upperclassnm}</td>
                        <td class="left"><a href="#none" onclick="whenSubjInfoViewPopup('${result.subj}', 'view')" title="새창">${result.subjnm}</a></td>
						<td class="num last">${v_status}</td>						
					</tr>					
				</c:forEach>
				
				<c:if test="${empty list}">
					<tr>
						<td colspan="10">등록된 내용이 없습니다.</td>
					</tr>
				</c:if>
				</tbody>
			</table>
		</div>
		<!-- list table-->
	
	
	
	
	
	
	
	
	
        
</form>



        
        
<script type="text/javascript">
<!--

var frm = eval('document.<c:out value="${gsMainForm}"/>');

//관심과정 삭제
function delSubjConcern() {
	
	if (!hasCheckedBox(frm._Array_p_checks)) {
		alert("관심과정에서 삭제할 항목을 선택해 주세요.");
		return;
	}

	if(confirm("삭제하시겠습니까?"))
	{
		frm.action = "/usr/mpg/concernInfoAction.do";
		frm.target = "_self";
		frm.p_process.value = "delete";
		frm.submit();
	}
}

//-->
</script>
        
        
        
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
