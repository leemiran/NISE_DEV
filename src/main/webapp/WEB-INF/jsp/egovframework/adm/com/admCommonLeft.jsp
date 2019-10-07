<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	<!-- left Lnb -->
	<div id="LnbWrap">		
		<div class="leftCon">			
			<!-- tree -->
			<div class="lnbSub">
				<ul class="dep1">
				<c:forEach items="${list_admLeftMenu}" var="result">
				
					<c:set var="class" value="down"/>
					<c:if test="${result.submenu == s_submenu}"><c:set var="class" value="on"/></c:if>
					
					<c:if test="${result.menunm == 'separator' and empty result.pgm}">
					<li><div class="separator"></div></li>
					</c:if>
					<c:if test="${result.menunm ne 'separator' and not empty result.pgm}">
					
					<!-- 추가 하위메뉴 start-->
					<c:if test="${result.submenu > '31080000' && result.submenu < '31190001'}">
						<c:set var="class" value="sub"/>
					</c:if>
					<!-- 추가 하위메뉴 end -->
					
					<li>
						<a href="#none" class="<c:out value="${class}"/>" <c:if test="${result.submenu ne '31080000'}"> onclick="javascript:fn_admin_pageMove('<c:out value="${result.menu}"/>', '<c:out value="${result.submenu}"/>', '<c:out value="${result.pgm}"/>')" </c:if>>
							<c:out value="${result.menunm  }"/>
						</a>
					</li>
					
					</c:if>
				</c:forEach>
				
				
				</ul>
			</div>
			<!-- // tree -->
		</div>
	</div>
	<!-- // left Lnb -->
	
	
<script>

	//alert($(".dep1").text());


</script>
	
  
