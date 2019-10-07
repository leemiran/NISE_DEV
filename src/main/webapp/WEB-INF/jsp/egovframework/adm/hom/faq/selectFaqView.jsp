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
	<input type="hidden" id="p_fnum" name="p_fnum" value="${p_fnum}">
	<input type="hidden" id="p_o_faqcategory" name="p_o_faqcategory" value="${p_faqcategory}">
    	
	
	
	<!-- list table-->
		<div class="tbDetail">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="30%" />
					<col width="70%" />					
				</colgroup>
				<thead>
					<tr>
						<th scope="row">FAQ 카테고리</th>
						<td scope="row">
                        	<select name="p_faqcategory" onchange="doPageList()"> 
								<c:forEach items="${categoryList}" var="result" varStatus="i">
									<option value="${result.faqcategory}" <c:if test="${result.faqcategory eq p_faqcategory}">selected</c:if> >${result.faqcategorynm}</option> 
								</c:forEach>
							</select>
                        </td>
					</tr>
                    <tr>
						<th scope="row">FAQ 번호</th>
						<td scope="row">${view.fnum}</td>
					</tr>
                    <tr>
						<th scope="row">FAQ 제목</th>
						<td scope="row"><input name="p_title" type="text" size="80" value="${view.title}"></td>
					</tr>
                    <tr>
						<th scope="row"><span class="point">주의사항</span></th>
						<td scope="row">이미지를 함께 올릴경우 이미지 가로사이즈510이 넘지 않게 해주세요. <span class="point">(510이 넘으면 사이트깨짐)</span></td>
					</tr>
                    <tr>
						<th scope="row">FAQ 답변</th>
						<td scope="row">
						<c:set var="content">${view.contents}</c:set>
                    	<c:set var="width"  value="600"/>
                    	<c:set var="height" value="400" />
                    	<%@ include file="/WEB-INF/jsp/egovframework/com/lib/DhtmlEditor.jsp" %>
						<textarea rows="10" cols="80" name="p_content" style="display:none">${view.contents}</textarea>
						</td>
					</tr>
				</thead>				
			</table>
    </div>
    
    
    <!-- // search wrap -->
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>목 록</span></a></div>
	    
<c:if test="${empty p_fnum}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenAction('insert')"><span>저 장</span></a></div>
</c:if>

<c:if test="${not empty p_fnum}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenAction('delete')"><span>삭 제</span></a></div>
		
		<div class="btnR"><a href="#" class="btn02" onclick="whenAction('update')"><span>저 장</span></a></div>
</c:if>	    
		
		
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		thisForm.action = "/adm/hom/faq/selectFaqList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
	

	function whenAction(mode) {
		 if(thisForm.p_title.value == ""){
	        alert("FAQ 제목을 입력하세요!");
	        thisForm.p_title.focus();
	        return;
	    }

		if (document.all.ObjEditor.style.display == "none") {
			thisForm.p_content.value = thisForm.txtDetail.value;
		} else {
			thisForm.p_content.value = ObjEditor.document.all.tags("html")[0].outerHTML;
			thisForm.p_content.value.replace("&","&amp;");
		}


		
		var msg = "저장하시겠습니까?";
		if(mode == 'delete') msg = "삭제하시겠습니까?\n삭제하실 경우 카테고리의 등록된 글들은 모두 삭제됩니다.\n그래도 삭제하시겠습니까?";
		if (confirm(msg + "")) {
			thisForm.action = "/adm/hom/faq/selectFaqAction.do";
			thisForm.p_process.value = mode;
			thisForm.target = "_self";
			thisForm.submit();
		}else{
			return;
		}

	}
</script>