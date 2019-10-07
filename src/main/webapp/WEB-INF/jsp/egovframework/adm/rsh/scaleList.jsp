<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>


<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">
<input type="hidden" value="" name="p_process">
<input type="hidden" value="" name="p_scalecode">


<!---->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>설문척도관리</h2>
         </div>
		 <!-- contents -->
        
		 <div class="listTop MR10" style="padding-bottom:0px;">				
                <div class="btnR MR05">
               		<a href="#none" onclick="doView('');" class="btn01"><span>등록</span></a>
                </div>                 
   		 </div>
      
          <div class="popCon MR20">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="45%" />	
                        <col width="45%" />					
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="col" class="txtC">No</th>
                            <th scope="col" class="txtC">척도</th>
                            <th scope="col" class="txtC">척도분류</th>
                        </tr>
<c:forEach items="${list}" var="result" varStatus="status">	                        
                        <tr>
                            <td scope="col" class="bold txtC">${result.scalecode}</td>
                            <td class="txtC"><a href="#none" onclick="doView('${result.scalecode}');">${result.scalename}</a></td>
                            <td class="txtC">${result.sGubun}점척도</td>
                        </tr>	                        
</c:forEach>                        							
                    </tbody>
                </table>
    	</div>        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="window.close();"><span>닫기</span></a></li>            
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

	function doView(p_scalecode)
	{
		thisForm.p_scalecode.value = p_scalecode;
		thisForm.action = "/adm/rsh/scaleView.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
	
</script>