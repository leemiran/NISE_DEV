<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>


<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>
			<c:if test="${p_gubun eq 'propose'}">신청</c:if>
			<c:if test="${p_gubun eq 'cancel'}">취소</c:if>
			<c:if test="${p_gubun eq 'student'}">승인</c:if>
			<c:if test="${p_gubun eq 'stold'}">수료</c:if>
			
			(Total : <c:out value="${fn:length(list)}"/> 명)
			</h2>
		</div>
		
    <!-- list table-->
		<div class="popList">
		
<c:if test="${p_gubun eq 'cancel'}">		
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="4%"/>
                    <col width="%"/>
                    <col width="10%"/>
                    <col width="30%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>                                                                            			
		    </colgroup>
				<thead>
					<tr>
					  <th rowspan="2" scope="row">NO</th>
					  <th rowspan="2" scope="row">교육구분</th>
					  <th rowspan="2" scope="row">회사</th>
					  <th rowspan="2" scope="row">과정명</th>
					  <th rowspan="2" scope="row">기수</th>
					  <th rowspan="2" scope="row">ID</th>
					  <th rowspan="2" scope="row">성명</th>
						<th rowspan="2" scope="row">수강료</th>
						<th colspan="2" scope="row">반려/취소</th>
					</tr>
					<tr>
						<th scope="row">구분</th>
						<th scope="row">사유</th>
				  </tr>
				</thead>
				<tbody>
<c:forEach items="${list}" var="result" varStatus="status" >				
					<tr>
						<td class="num"><c:out value="${status.count}"/></td>
						<td><c:out value="${result.isonoffnm}"/></td>
						<td><c:out value="${result.company}"/></td>
						<td class="left"><c:out value="${result.subjnm}"/></td>
						<td class="num"><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
						<td><a href="#none" onclick="whenMemberInfo('<c:out value="${result.userid}"/>')"><c:out value="${result.userid}"/></a></td>
						<td><c:out value="${result.name}"/></td>
						<td class="num"><c:out value="${result.biyong}"/></td>
                        <td><c:out value="${result.cancelkind}"/></td>
                        
                        <td><c:out value="${result.reason}"/></td>
                        				
			      </tr>
</c:forEach>
				</tbody>
			</table>
</c:if>
			
			
			
			
<c:if test="${p_gubun ne 'cancel'}">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="4%"/>
                    <col width="%"/>
                    <col width="10%"/>
                    <col width="30%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>                                                                            			
		    </colgroup>
				<thead>
					<tr>
					  <th rowspan="2" scope="row">NO</th>
					  <th rowspan="2" scope="row">교육구분</th>
					  <th rowspan="2" scope="row">회사</th>
					  <th rowspan="2" scope="row">과정명</th>
					  <th rowspan="2" scope="row">기수</th>
					  <th rowspan="2" scope="row">수강료</th>
					  <th colspan="2" scope="row">환급금액</th>
					  <th rowspan="2" scope="row">ID</th>
					  <th rowspan="2" scope="row">성명</th>
				  </tr>
					<tr>
						<th scope="row">대기업</th>
						<th scope="row">우선지원</th>
				  </tr>
				</thead>
				<tbody>
<c:forEach items="${list}" var="result" varStatus="status" >				
					<tr>
						<td class="num"><c:out value="${status.count}"/></td>
						<td rowspan="<c:out value="${result.cnt}"/>"><c:out value="${result.isonoffnm}"/></td>
						<td><c:out value="${result.company}"/></td>
						<td class="left"><c:out value="${result.subjnm}"/></td>
						<td class="num"><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
						<td class="num"><c:out value="${result.biyong}"/></td>
						<td><c:out value="${result.goyongpricemajor}"/></td>
						<td><c:out value="${result.goyongpriceminor}"/></td>
						<td><a href="#none" onclick="whenMemberInfo('<c:out value="${result.userid}"/>')"><c:out value="${result.userid}"/></a></td>						
                        <td><c:out value="${result.name}"/></td>						
			      </tr>
</c:forEach>			      
				</tbody>
			</table>
</c:if>			
			
			</div>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="window.close();"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

</script>