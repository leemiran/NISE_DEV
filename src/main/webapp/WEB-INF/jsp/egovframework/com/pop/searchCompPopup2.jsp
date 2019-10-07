<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:480px">
    	<div class="tit_bg">
			<h2>회사검색</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" name="pageIndex" id="pageIndex" value="1"/>
		<div class="in" style="width:100%; text-align: right">
			<select id="p_compgubun" name="p_compgubun">
				<option value="ALL">전체</option>
                <option value="1" <c:if test="${p_compgubun == '1'}">selected</c:if>>AKIS(주)</option>
                <option value="2" <c:if test="${p_compgubun == '2'}">selected</c:if>>계열사</option>
                <option value="3" <c:if test="${p_compgubun == '3'}">selected</c:if>>비계열사</option>
                <option value="4" <c:if test="${p_compgubun == '4'}">selected</c:if>>고객센터</option>
                <option value="5" <c:if test="${p_compgubun == '5'}">selected</c:if>>협력사</option>
                <option value="6" <c:if test="${p_compgubun == '6'}">selected</c:if>>기타</option>
			</select>
			<strong class="shTit">회사명</strong>
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}"/>
			<a href="#none" class="btn_search" onclick="searchList()"><span>검색</span></a>
		</div>
		<!-- contents -->
		<div class="tbList">
			<table summary="선택, 회사명으로 구분" cellspacing="0" width="100%">
                <caption>
                회사검색
                </caption>
                <colgroup>
				<col width="50px" />
				<col />
			</colgroup>
			<thead>
				<tr>
					<th scope="row"><input type="checkbox" name="p_chkeckall" onclick="chkeckall();" title="선택"></th>
					<th scope="row">회사명</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><input type="checkbox" name="no" id="no" value="<c:out value="${result.comp}"/>" title="선택"></td>
					<td class="left"><c:out value="${result.compnm}"/><input type="hidden" name="p_compnm" id="p_compnm" value="<c:out value="${result.compnm}"/>"></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
		</form>
		<!-- // contents -->
		<!-- 페이징 시작 -->
		<div class="paging">
			<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="linkPage"/>
		</div>
		<!-- 페이징 끝 -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="selectComp()"><span>선택</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>

<script type="text/javascript">
//<![CDATA[

	function searchList(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.action = "/com/pop/searchCompPopup.do";
		frm.submit();
	}
	
	function linkPage(idx){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.pageIndex.value = idx;
		frm.action = "/com/pop/searchCompPopup.do";
		frm.submit();
	}

	function chkeckall(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var attr = frm.p_chkeckall.checked;
	    if(frm.all['no']) {
	        if (frm.no.length > 0) {
	            for (i=0; i<frm.no.length; i++) {
	            	frm.no[i].checked = attr;
	            }
	        } else {
	        	frm.no.checked = attr;
	        }
	    }
	}
	function selectComp() {
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
	    opener.receiveComp(frm.no, frm.p_compnm);
	    self.close();
	}
//]]>
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>