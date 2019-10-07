<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:480px">
    	<div class="tit_bg">
			<h2>과정검색</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" name="pageIndex" id="pageIndex" value="1"/>
		<div class="in" style="width:100%; text-align: right">
			<strong class="shTit">과정명</strong>
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}"/>
			<a href="#none" class="btn_search" onclick="searchList()"><span>검색</span></a>
		</div>
		</form>
		<!-- contents -->
		<div class="tbList">
			<table summary="선택, 년도, 기수, 과정코드, 과정명로 구성" cellspacing="0" width="100%">
                <caption>과정검색</caption>
                <colgroup>
				<col width="30px" />
				<col width="50px"/>
				<col width="40px"/>
				<col width="80px"/>
				<col />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">선택</th>
					<th scope="row">년도</th>
					<th scope="row">기수</th>
					<th scope="row">과정코드</th>
					<th scope="row">과정명</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<%-- <td><input type="checkbox" name="no" class="no" value="<c:out value="${result.subj}"/>" onclick="javascipt:selectSubj('<c:out value="${result.subj}"/>' ,'<c:out value="${result.subjnm}"/>','<c:out value="${p_key2}"/>','','')" style="cursor:pointer"/></td>--%>
					<td><input type="checkbox" name="no" value="<c:out value="${result.subj}"/>"><input type="hidden" class="subjnm" value="<c:out value="${result.subjnm}"/>" /><input type="hidden" class="p_key2" value="<c:out value="${p_key2}"/>" /></td>
					<td><c:out value="${result.year}"/></td>
					<td><c:out value="${result.subjseq}"/></td>
					<td><c:out value="${result.subj}"/></td>
					<td class="left"><c:out value="${result.subjnm}"/></td>
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
	$(document).ready(function(){
		$("input[name='no']").change(function() {
		    var checked = $(this).is(':checked');
		    var subj = $(this).val();
		    var subjnm = $(this).next().val();
		    var tmp1 = $(this).next().next().val();

		    if(checked) {
		    	opener.receiveSubj(subj , subjnm, tmp1, '', '');
		    	
				var msg = $("#sendMsg", opener.document).val();
				
				if(msg) {
					alert(msg);
					$(this).attr('checked', false);
					$("#sendMsg", opener.document).val('');
				}
		    } else {
		    	opener.delSubjPop(subj , subjnm);
		    }
		});
	});
           
           
	function searchList(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.action = "/com/pop/searchSubjPopup.do";
		frm.submit();
	}
	
	function linkPage(idx){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.pageIndex.value = idx;
		frm.action = "/com/pop/searchSubjPopup.do";
		frm.submit();
	}

	/*function selectSubj(subj , subjnm, tmp1, tmp2, tmp3){
		opener.receiveSubj(subj , subjnm, tmp1, tmp2, tmp3);
        self.close();
	}*/
//]]>
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>