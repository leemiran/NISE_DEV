<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "com.ziaan.exam.*" %>
<%@ page import = "egovframework.rte.psl.dataaccess.util.EgovMap" %>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<c:set var="v_urldir">/dp/exam/</c:set>

<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>
<style>
table.ex1 {width:97%; margin:0 auto; text-align:left; border-collapse:collapse}
 .ex1 th, .ex1 td {padding:5px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
</style>


<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">


<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>:+: 평가 :+:</h2>
         </div>
		<!-- contents -->
        
        
        
        <!-- list table-->
		<div class="tbList ML10 MR10 MT10">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="7%"/>		      
		      <col width="%"/>
	        </colgroup>
	        
	        
<c:set var="titletxt" value=""/>   
<c:forEach items="${list}" var="result" varStatus="status">
		<c:forEach items="${result}" var="subResult" varStatus="subStatus">  
			<c:if test="${subStatus.index == 0}">
				<thead>
                  <tr>
                        <th scope="row">문제${subResult.examnum}</th>
                        <th scope="row" class="left">
                        
                        	<c:set var="titletxt" value="${fn:replace(subResult.examtext, lf, '<br/>')}"></c:set>
							<c:if test="${not empty titletxt}">
								<c:if test="${fn:indexOf(titletxt, '보기>') > -1}">
									<c:set var='titletxt'>${fn:replace(titletxt, "보기>", "<table class='ex1'><tr><th scope='col'>보기</th><td>")}</c:set>
									<c:set var="titletxt">${titletxt}</td></tr></table></c:set>
								</c:if>
								
								${titletxt}
							</c:if>
							
                        	
                        </th>
                  </tr>
                </thead>
           </c:if>
           
<!--           주관식일때-->
           <c:if test="${subResult.examtype eq '2'}">
           			<tr>
           				<td></td>
                        <td class="left" colspan="2"><input type="text" value="${subResult.seltext}"/></td>
                    </tr>
           </c:if>     
           <!--           주관식이 아닐때-->
           <c:if test="${subResult.examtype ne '2'}">
                  <tr>
                        <c:if test="${subStatus.index == 0}"><td rowspan="${fn:length(result)}"></td></c:if>
                        <td class="left"><input type="radio" <c:if test="${subResult.isanswer eq 'Y'}">checked</c:if>> ${subResult.selnum}. ${subResult.seltext}</td>
                  </tr>
          </c:if>        
			</c:forEach>                
</c:forEach>    
            
	      </table>
		</div>
		<!-- list table-->
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
            <li><a href="javascript:self.close()" class="pop_btn01"><span>닫기</span></a></li>            
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->



    
	

</form>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>



<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

	
</script>