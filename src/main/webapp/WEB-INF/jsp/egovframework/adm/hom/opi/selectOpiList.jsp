<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">



	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type="hidden" id="p_process" name="p_process">
	<input type = "hidden" name = "p_seq"       value = "${p_seq}">
	
	
	
	
	
	
<c:if test="${not empty p_seq}">	
	<div class="tbDetail">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="15%" />
					<col width="85%" />					
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
                            	<a href="#none" onclick="whenPersonGeneralPopup('${view.userid}')">
                            		<c:if test="${not empty view.gadmin}">
										<c:if test="${view.gadmin ne 'ZZ'}">${view.gadmin}</c:if>
									</c:if>
									${view.name}
									(${view.userid})
								</a>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">제목</th>
                            <td scope="row">
                            ${view.title}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">등록일</th>
                            <td scope="row">
                            <c:out value="${fn2:getFormatDate(view.indate, 'yyyy.MM.dd')}"/>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">조회수</th>
                            <td scope="row">
                            ${view.cnt}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">내용</th>
                            <td scope="row">
                            
                            <c:out value="${fn:replace(view.content, lf, '<br/>')}" escapeXml="false"/>
                            
                            </td>
                        </tr>

				</thead>				
			</table>
   		</div>
	    
</c:if>

		<div class="listTop">
		<c:if test="${not empty p_seq}">
			<div class="btnR MR05"><a href="#" class="btn03" onclick="doPageList()"><span>목 록</span></a></div>
		    <div class="btnR MR05"><a href="#" class="btn02" onclick="whenAction()"><span>삭 제</span></a></div>
		</c:if>
		</div>


	
	<!-- 검색박스 시작-->
	<div class="searchWrap txtL">
		<div>		
			<ul class="datewrap">
				<li class="floatL">
					<select name="p_gubuna">
                      	<option value=''  >의견구분</option>
	                    <option value='TRO' <c:if test="${p_gubuna eq 'TRO'}">selected</c:if>>연수개선의견</option>
	                    <option value='ERO' <c:if test="${p_gubuna eq 'ERO'}">selected</c:if>>오류사항의견</option>
	              </select>
	              <select name="p_search">
	                      <option value='name'	 	<c:if test="${p_search eq 'name'}">selected</c:if>>작성자</option>
	                      <option value='title' 	<c:if test="${p_search eq 'title'}">selected</c:if>>제목</option>
	                      <option value='content' 	<c:if test="${p_search eq 'content'}">selected</c:if>>내용</option>
	                      <option value='userid' 	<c:if test="${p_search eq 'userid'}">selected</c:if>>아이디</option>
	                      <option value='ldate'  	<c:if test="${p_search eq 'ldate'}">selected</c:if>>작성일자</option>
	              </select>
					<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" size="50" onkeypress="fn_keyEvent('doPageList')" style="ime-mode:active;"/>
					<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
				</li>
			</ul>		
		</div>
	</div>
	<!-- 검색박스 끝 -->
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="4%" />
					<col width="15%" />
					<col width="50%" />
					<col width="10%" />
					<col width="%" />
					<col width="%" />				
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row">구분</th>
						<th scope="row">제목</th>
						<th scope="row">작성자</th>
						<th scope="row">등록일</th>
						<th scope="row">조회수</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${list}" var="result" varStatus="status">
					<tr>
						<td class="num"><c:out value="${(pageTotCnt+1)-(status.count+firstIndex)}"/></td>
						<td>
						<c:if test="${result.gubuna eq 'TRO'}">
						연수개선의견
						</c:if>
						<c:if test="${result.gubuna eq 'ERO'}">
						오류사항의견
						</c:if>
						</td>
						<td class="left">
						<a href="#none" onclick="doView('${result.seq}')">
						${result.title}
						</a>
						</td>
						<td>
						<a href="#none" onclick="whenPersonGeneralPopup('${result.userid}')">
							<c:if test="${not empty result.gadmin}">
								<c:if test="${result.gadmin ne 'ZZ'}">${result.gadmin}</c:if>
							</c:if>
							${result.name}
							(${result.userid})
						</a>
						</td>
						<td><c:out value="${fn2:getFormatDate(result.indate, 'yyyy.MM.dd')}"/></td>
						<td>${result.cnt}</td>
					</tr>
				</c:forEach>
				<c:if test="${empty list}">
					<tr>
						<td colspan="20">조회된 내용이 없습니다.</td>
					</tr>
				</c:if>
				</tbody>
			</table>
		</div>
		<!-- list table-->
	
	<!-- 페이징 시작 -->
	<div class="paging">
		<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
	</div>
	<!-- 페이징 끝 -->
	
	
		
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	var frm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function doPageList() {
		frm.action = "/adm/hom/opi/selectOpiList.do";
		frm.p_seq.value = '';
		frm.target = "_self";
		frm.submit();
	}
	
	function doLinkPage(index) {
		
		frm.action = "/adm/hom/opi/selectOpiList.do";
		frm.p_seq.value = '';
		frm.pageIndex.value = index;
		frm.target = "_self";
		frm.submit();
	}

	function doView(p_seq){
		frm.p_seq.value = p_seq;
		frm.action = "/adm/hom/opi/selectOpiList.do";
		frm.target = "_self";
		frm.submit();
	}


	function doEdit(p_seq){
		frm.p_seq.value = p_seq;
		frm.action = "/adm/hom/opi/selectOpiView.do";
		frm.target = "_self";
		frm.submit();
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