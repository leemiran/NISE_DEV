<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type="hidden" id="p_process" name="p_process">
	
    	
	
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="85%" />
            </colgroup>
            <tbody>
            
           
                <tr>
                    <th scope="row">FAQ 카테고리코드</th>
                    <td scope="row">
                    	<input type="hidden" name="p_faqcategory" value="${p_faqcategory}"/>
                    	${p_faqcategory}
                    </td>
                </tr>
                
                <tr>
                    <th scope="row">FAQ 카테고리코드</th>
                    <td scope="row">
                    	<input name="p_faqcategorynm" type="text" size="80" value="${view.faqcategorynm}">
                    </td>
                </tr>
                
            </tbody>
        </table>				
    </div>
    
    
    
    <!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>취소</span></a></div>
	    
<c:if test="${empty p_faqcategory}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenAction('insert')"><span>저 장</span></a></div>
</c:if>

<c:if test="${not empty p_faqcategory}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenAction('delete')"><span>삭 제</span></a></div>
		
		<div class="btnR"><a href="#" class="btn02" onclick="whenAction('update')"><span>저 장</span></a></div>
</c:if>	    
		
		
	</div>
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		thisForm.action = "/adm/hom/faq/selectCategoryList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
	

	function whenAction(mode) {
		if(thisForm.p_faqcategorynm.value == ""){
			alert("카테고리명을 입력하세요!");
			thisForm.p_faqcategorynm.focus();
			return;
		}

		var msg = "저장하시겠습니까?";
		if(mode == 'delete') msg = "삭제하시겠습니까?\n삭제하실 경우 카테고리의 등록된 글들은 모두 삭제됩니다.\n그래도 삭제하시겠습니까?";
		if (confirm(msg + "")) {
			thisForm.action = "/adm/hom/faq/selectCategoryAction.do";
			thisForm.p_process.value = mode;
			thisForm.target = "_self";
			thisForm.submit();
		}else{
			return;
		}

	}
</script>