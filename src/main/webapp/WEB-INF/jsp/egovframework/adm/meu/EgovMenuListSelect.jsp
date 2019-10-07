<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	
		<c:set var="count">0</c:set>
		
	    <c:forEach items="${list_menulist}" var="result" varStatus="status" >
			<c:set var="v_levels">${result.levels}</c:set>
			<c:set var="v_menunm">${result.menunm}</c:set>
			<c:set var="v_pgm">${result.pgm}</c:set>
			<c:set var="v_menu">${result.menu}</c:set>
			<c:set var="v_upper">${result.upper}</c:set>
			
			
			<c:if test="${v_levels == 1 && status.index == 0}">
				<div style=" overflow:hidden;">
				<div class="sitemap">
	                	<h3>${v_menunm}</h3>
	                	<ul>
	                	
	            <c:set var="count">${count + 1}</c:set>
			</c:if>
			
			<c:if test="${v_levels == 1 && status.index > 0}">
				<c:if test="${count % 4 == 0}">
					</div>
					<div style=" overflow:hidden; clear:both; padding-top:30px;">
				</c:if>
				
				<c:set var="count">${count + 1}</c:set>
				
						</ul>
				</div>
				<div class="sitemap">
	                	<h3>${v_menunm}</h3>
	                	<ul>
			</c:if>
			
			
	        <c:if test="${v_levels > 1}">
	                    	<li><a href="#" onclick="fn_admin_pageMove('${v_upper}', '${v_menu}', '${v_pgm}')">${v_menunm}</a></li>
	         </c:if>
	        
	        
		</c:forEach>
				</ul>
			</div>
	</div>	
	
	
	
	
	
	
	
	
</form>




<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
