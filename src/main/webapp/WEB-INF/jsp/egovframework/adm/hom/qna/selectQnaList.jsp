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
	<c:if test="${p_tabseq eq '2' or p_tabseq eq '3'}">
	<input type = "hidden" name = "p_tabseq"       value = "${p_tabseq}">
	</c:if>
	
	
	
	
	
	
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
                            	<c:if test="${p_tabseq eq '1'}">Q&A</c:if>
                            	<c:if test="${p_tabseq eq '2'}">입금</c:if>
                            	<c:if test="${p_tabseq eq '3'}">환불</c:if>
                            	<c:if test="${p_tabseq eq '0'}">연수후기</c:if>
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
                        <tr>
                            <th scope="row">학습자 첨부파일</th>
                            <td scope="row">
                            <c:if test="${not empty view.realfile}">
                            * <a href="#none" onclick="fn_download('${view.realfile}', '${view.savefile}', 'bulletin')">
                    		<c:out value="${empty view.realfile ? view.savefile : view.realfile}"/>
                    		</a>
                    		</c:if>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="bc">답변작성자</th>
                            <td scope="row">
                            ${view.aname}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="bc">답변작성일</th>
                            <td scope="row">
                            <c:out value="${fn2:getFormatDate(view.adate, 'yyyy.MM.dd')}"/>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="bc">답변제목</th>
                            <td scope="row">
                            ${view.atitle}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="bc">답변내용</th>
                            <td scope="row">
                            <c:out value="${view.acontent}" escapeXml="false"/>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="bc">답변자 첨부파일</th>
                            <td scope="row">
                            <c:if test="${not empty view.realfile2}">
                            * <a href="#none" onclick="fn_download('${view.realfile2}', '${view.savefile2}', 'bulletin')">
                    		<c:out value="${empty view.realfile2 ? view.savefile2 : view.realfile2}"/>
                    		</a>
                    		</c:if>
                            </td>
                        </tr>

				</thead>				
			</table>
   		</div>
	    
</c:if>

		<div class="listTop">
		<c:if test="${not empty p_seq}">
		    <div class="btnR MR05"><a href="#" class="btn03" onclick="doPageList('${p_tabseq}')"><span>목 록</span></a></div>
		    
			<div class="btnR MR05"><a href="#" class="btn02" onclick="whenAction()"><span>삭 제</span></a></div>
			
			<div class="btnR MR05"><a href="#" class="btn02" onclick="doEdit('${p_seq}')"><span>수 정</span></a></div>
			
			<div class="btnR MR05"><a href="#" class="btn02" onclick="doEdit('${p_seq}')"><span>답변등록</span></a></div>
		</c:if>
		
			<div class="btnR MR05"><a href="#" class="btn02" onclick="doEdit('')"><span>등 록</span></a></div>
			
		</div>


	
	<!-- 검색박스 시작-->
	<div class="searchWrap txtL">
		<div>		
			<ul class="datewrap">
				<li class="floatL">
				  <select name="p_answer">
                      	<option value=''  >답변여부</option>
	                    <option value='Y' <c:if test="${p_answer eq 'Y'}">selected</c:if>>답변있음</option>
	                    <option value='N' <c:if test="${p_answer eq 'N'}">selected</c:if>>답변없음</option>
	              </select>
	              <c:if test="${p_tabseq eq '1' or p_tabseq eq '0'}">
	              <select name="p_tabseq">
	                      <option value='1' <c:if test="${p_tabseq eq '1'}">selected</c:if>>Q&A</option>
<!--	                      <option value='2' <c:if test="${p_tabseq eq '2'}">selected</c:if>>입금</option>-->
<!--	                      <option value='3' <c:if test="${p_tabseq eq '3'}">selected</c:if>>환불</option>-->
	                      <option value='0' <c:if test="${p_tabseq eq '0'}">selected</c:if>>연수후기</option>
	              </select>
	              </c:if>
	              <select name="p_search">
	                      <option value='name'	 	<c:if test="${p_search eq 'name'}">selected</c:if>>작성자</option>
	                      <option value='title' 	<c:if test="${p_search eq 'title'}">selected</c:if>>제목</option>
	                      <option value='content' 	<c:if test="${p_search eq 'content'}">selected</c:if>>내용</option>
	                      <option value='userid' 	<c:if test="${p_search eq 'userid'}">selected</c:if>>아이디</option>
	                      <option value='ldate'  	<c:if test="${p_search eq 'ldate'}">selected</c:if>>작성일자</option>
	              </select>
					<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" size="50" onkeypress="fn_keyEvent('doPageList')" style="ime-mode:active;"/>
					<a href="#none" class="btn_search" onclick="doPageList('${p_tabseq}')"><span>검색</span></a>
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
					<col width="50%" />
					<col width="10%" />
					<col width="%" />
					<col width="%" />
                    <col width="%" />
					<col width="%" />					
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row">제목</th>
						<th scope="row">작성자</th>
						<th scope="row">등록일</th>
						<th scope="row">공개여부</th>
						<th scope="row">답변여부</th>
						<th scope="row">조회수</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${list}" var="result" varStatus="status">
					<tr>
						<td class="num"><c:out value="${(pageTotCnt+1)-(status.count+firstIndex)}"/></td>
						<td class="left">
						<a href="#none" onclick="doView('${result.seq}', '${p_tabseq}')">
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
						<td>${result.isopen eq 'Y' ? '공개' : '비공개'}</td>
						<td>${result.hasanswer eq 'Y' ? '답변있음' : '답변없음'}</td>
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

	
	function doPageList(seq) {
		
		if(seq == "" || seq == undefined) seq = ${p_tabseq};		
		
		if(seq =='2'){
			frm.action = "/adm/hom/qna/selectQnaList2.do";
		}else if(seq == '3'){
			frm.action = "/adm/hom/qna/selectQnaList3.do";
		}else{
			frm.action = "/adm/hom/qna/selectQnaList.do";
		}
		
		frm.p_seq.value = '';
		frm.target = "_self";
		frm.submit();
	}
	
	function doLinkPage(index) {
		var url = "";
		
		<c:if test="${p_tabseq eq '2'}">
			url = "/adm/hom/qna/selectQnaList2.do";
		</c:if>
		<c:if test="${p_tabseq eq '3'}">
			url = "/adm/hom/qna/selectQnaList3.do";
		</c:if>
		<c:if test="${p_tabseq ne '2' and p_tabseq ne '3'}">
			url = "/adm/hom/qna/selectQnaList.do";
		</c:if>
		
		frm.action = url;
		frm.p_seq.value = '';
		frm.pageIndex.value = index;
		frm.target = "_self";
		frm.submit();
	}

	function doView(p_seq, tabseq){
		frm.p_seq.value = p_seq;
		if(tabseq == '2'){
			frm.action = "/adm/hom/qna/selectQnaList2.do";
		}else if(tabseq == '3'){
			frm.action = "/adm/hom/qna/selectQnaList3.do";
		}else{
			frm.action = "/adm/hom/qna/selectQnaList.do";
		}
		
		frm.target = "_self";
		frm.submit();
	}


	function doEdit(p_seq){
		frm.p_seq.value = p_seq;
		frm.action = "/adm/hom/qna/selectQnaView.do";
		frm.target = "_self";
		frm.submit();
	}


    function whenAction() {
 		if (confirm("삭제하시겠습니까?")) {
             frm.action = "/adm/hom/qna/selectQnaAction.do";
     		frm.pageIndex.value = '1';
     		frm.p_process.value = "delete";
     		frm.target = "_self";
     		frm.submit();
         }
    }

</script>