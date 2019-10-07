<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<!-- popup wrapper 팝업사이즈width=650,height=500-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>강사검색</h2>
		</div>	
		
		<!-- search wrap-->
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" name="pageIndex" id="pageIndex" value="1"/>
		<input type="hidden" name="tmp1" id="tmp1" value="${tmp1}"/>
		<input type="hidden" name="tmp2" id="tmp2" value="${tmp2}"/>
		<input type="hidden" name="tmp3" id="tmp3" value="${tmp3}"/>
		<div class="searchWrap" style="margin:10px;">
			<div class="in">
				<select name="p_search" id="p_search">
				<option value="name"   <c:if test="${p_search == 'name'}">selected</c:if>>이름</option>
				<option value="userid" <c:if test="${p_search == 'userid'}">selected</c:if>>ID</option>
			</select>
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" style="ime-mode:active"/>
			<a href="#none" class="btn_search01" onclick="searchList()"><span>검색</span></a>
			</div>
		</div>
		</form>
		<!-- // search wrap -->
		
		<!-- contents -->
		<div class="popList">
			<table summary="선택, ID, 성명, 연락처, 강사권한(기간)으로 구성" cellspacing="0" width="100%">
                <caption>
                강사검색
                </caption>
                <colgroup>
				<col width="40px" />
				<col width="100px"/>
				<col width="100px"/>
				<col width="110px"/>
				<col width=""/>
			</colgroup>
			<thead>
				<tr>
					<th scope="row">선택</th>
					<th scope="row">ID</th>
					<th scope="row">성명</th>
					<th scope="row">연락처</th>
					<th scope="row">강사권한(기간)</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td>
						<input type="radio" name="no" value="<c:out value="${result.userid}"/>"
						       onclick="javascipt:selectTutor(
						               '<c:out value="${result.userid}"/>' ,'<c:out value="${result.name}"/>'
						       )" style="cursor:pointer"/>
					</td>
					<td><c:out value="${result.userid}"/></td>
					<td><c:out value="${result.name}"/></td>
					<td><c:out value="${result.phone}"/></td>
					<td><c:out value="${result.authname}"/></td>
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
		if (frm.p_searchtext.value == "") {
            alert("검색어를 입력해주세요");
            return;
        }
		frm.action = "/com/pop/searchTutorPopup.do";
		frm.submit();
	}
	
	function linkPage(idx){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.pageIndex.value = idx;
		frm.action = "/com/pop/searchTutorPopup.do";
		frm.submit();
	}

    function selectTutor(userid, name) {
        opener.receiveTutor(userid, name);
        self.close();
    }
//]]>
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>