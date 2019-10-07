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
		<input type="hidden" name="p_key2" id="p_key2" value="${p_key2}"/>
		
		<div class="in" style="width:100%; text-align: right">
			<strong class="shTit">회사명</strong>
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}"/>
			<a href="#none" class="btn_search" onclick="searchList()"><span>검색</span></a>
		</div>
		</form>
		<!-- contents -->
		<div class="tbList">
			<table summary="선택, 회사명으로 구성" cellspacing="0" width="100%">
                <caption>
                회사검색
                </caption>
                <colgroup>
				<col width="30px" />
				<col />
			</colgroup>
			<thead>
				<tr>
					<th scope="col">선택</th>
					<th scope="col">회사명</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><input type="radio" name="no" value="<c:out value="${result.comp}"/>" onclick="javascipt:selectGrpComp('<c:out value="${result.comp}"/>' ,'<c:out value="${result.compnm}"/>','<c:out value="${p_key2}"/>','','')" style="cursor:pointer" title="선택"/></td>
					<td class="left"><c:out value="${result.compnm}"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
		<!-- // contents -->
		<!-- 페이징 시작 -->
		<div class="paging">
			<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="linkPage"/>
		</div>
		<!-- 페이징 끝 -->
		<!-- button -->
		<ul class="btnCen">
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

	function selectGrpComp(comp , compnm, tmp1, tmp2, tmp3){
		opener.receiveGrpComp(comp , compnm, tmp1, tmp2, tmp3);
        self.close();
	}
//]]>
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>