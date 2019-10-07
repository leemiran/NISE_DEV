<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data" onsubmit="return false;">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type="hidden" id="p_process" name="p_process">
	<input type="hidden" id="p_tabseq" name="p_tabseq" value="${p_tabseq}">
	<input type="hidden" id="p_seq" name="p_seq" value="${p_seq}">
    	
	
	
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
                            <th scope="row">구분</th>
                            <td scope="row">
                            	<c:if test="${view.gubunA eq 'TRO'}">연수개선의견</c:if>
                            	<c:if test="${view.gubunA eq 'ERO'}">오류사항의견</c:if>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">작성자</th>
                            <td scope="row">
<c:if test="${empty p_seq}">
                             ${sessionScope.name}(${sessionScope.userid})
</c:if>
<c:if test="${not empty p_seq}">
								<a href="#none" onclick="whenPersonGeneralPopup('${view.userid}')">
									<c:if test="${not empty view.gadmin}">
										<c:if test="${view.gadmin ne 'ZZ'}">${view.gadmin}</c:if>
									</c:if>
									${view.name}
									(${view.userid})
								</a>
</c:if>  
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">제목</th>
                            <td scope="row"><input size="80" maxlength="100" name="p_title" value = "${view.title}"></td>
                        </tr>
                        <tr>
                            <th scope="row">내용</th>
                            <td scope="row">
                            <textarea rows="10" cols="80" name="p_content">${view.content}</textarea>
                            </td>
                        </tr>
				</thead>				
			</table>
   		</div>
		<!-- list table-->	
    
    
    <div class="listTop">
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>목 록</span></a></div>

		<div class="btnR MR05"><a href="#" class="btn02" onclick="whenAction()"><span>삭 제</span></a></div>
		   
	</div>	
		
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		thisForm.action = "/adm/hom/opi/selectOpiList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
	

	function whenAction() {
		
		if (confirm("삭제하시겠습니까?")) {
            frm.action = "/adm/hom/opi/selectOpiAction.do";
    		frm.pageIndex.value = '1';
    		frm.target = "_self";
    		frm.submit();
        }

	}

</script>