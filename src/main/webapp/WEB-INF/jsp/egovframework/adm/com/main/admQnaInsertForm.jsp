<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${not empty resultMsg1}">
	alert("${resultMsg1}");
	opener.location.href="/adm/com/main/admActionMainPage.do";
	self.close();
	
</c:if>
-->
</script>
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" enctype="multipart/form-data" onsubmit="return false;">
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type="hidden" id="p_process" name="p_process">
	<input type="hidden" id="p_tabseq" name="p_tabseq" value="${p_tabseq}">
	<input type="hidden" id="p_seq" name="p_seq" value="${p_seq}">
    	
	<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>답변등11111111록</h2>
         </div>
		<!-- contents -->
		<div class="popCon">
	
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
                            	<c:if test="${p_tabseq eq '1'}">Q&A</c:if>
                            	<c:if test="${p_tabseq eq '2'}">입금</c:if>
                            	<c:if test="${p_tabseq eq '3'}">환불</c:if>
                            	<c:if test="${p_tabseq eq '0'}">연수후기</c:if>
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
                        
<c:if test="${not empty p_seq}">                        
                        <tr>
                            <th scope="row" class="bc">답변제목</th>
                            <td scope="row">
                            <input size="80" maxlength="100" name="p_atitle" value ="${view.atitle}">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="bc">답변내용</th>
                            <td scope="row">
                            <c:set var="content">${view.acontent}</c:set>
	                    	<c:set var="width"  value="600"/>
	                    	<c:set var="height" value="400" />
	                    	<%@ include file="/WEB-INF/jsp/egovframework/com/lib/DhtmlEditor.jsp" %>
	                    	<textarea rows="10" cols="80" name="p_acontent" style="display:none">${view.acontent}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="bc">답변자 첨부파일</th>
                            <td scope="row">
                            <c:if test="${not empty view.realfile2}">
                            * <a href="#none" onclick="fn_download('${view.realfile2}', '${view.savefile2}', 'bulletin')">
                    		<c:out value="${empty view.realfile2 ? view.savefile2 : view.realfile2}"/>
                    		</a>
                    		<input type = "checkbox"  name = "p_check" value = "Y"> (삭제시 체크)<br/>
                    		</c:if>
	                    	
	                    	<input type="FILE" name="p_file1" size="80"><br/>
                            </td>
                        </tr>
</c:if>                        
				</thead>				
			</table>
   		</div>
		<!-- list table-->	
    
    
    <div class="listTop">
<!--	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>목 록</span></a></div>-->
	    
<c:if test="${empty p_seq}">
		<div class="btnR MR05"><a href="#" class="btn02" onclick="whenAction('insert')"><span>저 장</span></a></div>
</c:if>

<c:if test="${not empty p_seq}">
<!--		<div class="btnR MR05"><a href="#" class="btn02" onclick="whenAction('delete')"><span>삭 제</span></a></div>-->
		
		<div class="btnR MR05"><a href="#" class="btn02" onclick="whenAction('update')"><span>저 장</span></a></div>
</c:if>	    
	</div>	
		
    	</div>
	</div>
</div>
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

	
	function pageList(){
		thisForm.action = "/adm/hom/qna/selectQnaList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
	

	function whenAction(mode) {
		if (blankCheck(thisForm.p_title.value)) {
            alert("제목을 입력하세요!");
            thisForm.p_title.focus();
            return;
        }
        if (realsize(thisForm.p_title.value) > 200) {
            alert("제목은 한글기준 100자를 초과하지 못합니다.");
            thisForm.p_title.focus();
            return;
        }
        if (blankCheck(thisForm.p_content.value)) {
            alert("내용을 입력하세요!");
            thisForm.p_content.focus();
            return;
        }

        
		<c:if test="${not empty p_seq}">
		thisForm.txtDetail.value = ObjEditor.document.all.tags("html")[0].outerHTML;
		thisForm.p_acontent.value = thisForm.txtDetail.value;
		thisForm.p_acontent.value.replace("&","&amp;");
		</c:if>
		
		var msg = "저장하시겠습니까?";
		if(mode == 'delete') msg = "삭제하시겠습니까?";
		if (confirm(msg + "")) {
			thisForm.action = "/adm/com/main/qnaInsert.do";
			thisForm.p_process.value = mode;
			thisForm.pageIndex.value = "1";
			thisForm.target = "_self";
			thisForm.submit();
		}else{
			return;
		}

	}

</script>