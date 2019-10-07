<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>


<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>회원정보</h2>
         </div>
		<!-- contents -->
		<div class="popCon">
			<table summary="성명(ID/PWD),구분(자격),학교,회사주소,전화번호(회사),자택주소,전화번호(H.P),E-mail로 구성" width="100%" class="popTb">
				<caption>회원정보</caption>
                <colgroup>
					<col width="25%" />
					<col width="75%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">성명(ID)</th>
						<td class="bold">
						<c:out value="${view.name}"/>
						(<c:out value="${p_userid}"/><c:if test="${sessionScope.userid eq 'admin'}"></c:if>)
						</td>
					</tr>
					<tr>
						<th scope="col">생년월일</th>
						<td>
						<c:out value="${view.birthDate}"/>
						</td>
					</tr>
					<tr>
						<th scope="col">구분(자격)</th>
						<td>
						<c:out value="${view.gb}"/>
						(<c:out value="${view.lic}"/>)
						</td>
					</tr>
					<tr>
						<th scope="col">학교</th>
						<td>
						<c:out value="${view.snm}"/>
						</td>
					</tr>
					<tr>
						<th scope="col">회사주소</th>
						<td>
						[<c:out value="${view.zipCd1}"/>] 
						<c:out value="${view.address1}"/>
						
						<c:if test="${view.rv ne '자택'}"><font color="red">(교재수령)</font></c:if>
						
						</td>
					</tr>
					<tr>
						<th scope="col">전화번호(회사)</th>
						<td>
						<c:out value="${view.hometel}"/>
						</td>
					</tr>
					<tr>
						<th scope="col">자택주소</th>
						<td>
						[<c:out value="${view.zipCd}"/>] 
						<c:out value="${view.address}"/>
						
						<c:if test="${view.rv eq '자택'}"><font color="red">(교재수령)</font></c:if>
						
						</td>
					</tr>
					<tr>
						<th scope="col">전화번호(H.P)</th>
						<td>
						<c:out value="${view.handphone}"/>
						</td>
					</tr>
					
					<tr>
						<th scope="col">E-mail</th>
						<td>
						<c:out value="${view.email}"/>
						</td>
					</tr>
					
									
				</tbody>
			</table>
		</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#none" onClick="window.close()" class="pop_btn01"><span>닫 기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>

