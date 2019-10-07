<%@ page pageEncoding="UTF-8"%>
<div class="contop">
	<c:choose>
		<c:when test="${menu_main eq 3 and menu_sub eq 4}">
			<h3><img src="/images/user/h4_05_1.gif" alt="<c:out value="${menu_sub_title}"  escapeXml="true" />"/></h3>
		</c:when>
		<c:otherwise>
			<h3><img src="/images/user/h4_0${menu_main}_${menu_sub}.gif" alt="<c:out value="${menu_sub_title}"  escapeXml="true" />"/></h3>
		</c:otherwise>
	</c:choose>
	<!-- 글자크기 -->            
	<ul class="font_size">
		<li class="fri"><img src="/images/user/txt_img.gif" alt="화면크기"/></li>
		<li><a href="#" onclick="zoomIn()" ><img src="/images/user/plus.gif" alt="키우기"/></a></li>
		<li><a href="#" onclick="zoomOut()" ><img src="/images/user/minus.gif" alt="줄이기"/></a></li>
		<li><a href="#" onclick="zoomFrist()"><img src="/images/user/100.gif" alt="기본"/></a></li>
	</ul>
	<!-- //글자크기 --> 
	<%-- <div class="loc">
		<a href="#" onclick="changeMenu('', '')">홈</a> &gt; <a href="#" onclick="changeMenu(<c:out value="${menu_main}"  escapeXml="true" />, 1)"><c:out value="${menu_tab_title}"  escapeXml="true" /></a> &gt; <span><c:out value="${menu_sub_title}"  escapeXml="true" /></span>
	</div> --%>
</div>