<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>


<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">
		


<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>미리보기</h2>
         </div>
		<!-- contents -->
        
		<div>
			<p class="pop_tit">${view.sulpapernm}</p>			
		</div>
        
            
      
          <div class="popCon MR20">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="90%" />	
                    </colgroup>
                    <tbody>
                    
<c:set var="v_sulnum">-1</c:set>
<c:set var="v_idx">0</c:set>
<c:set var="old_sultype"></c:set>
                    
<c:forEach items="${sulList}" var="result" varStatus="status">	
                        
                        
<c:if test="${result.sulnum ne v_sulnum}">
	<c:set var="v_sulnum">${result.sulnum}</c:set>
	<c:set var="v_idx">${v_idx + 1}</c:set>
	
<!--	이전의 루프가 설문타입이 복합형이었다면 기타의견을 넣어준다.-->
	<c:if test="${old_sultype eq '4'}">
						<tr>
                            <th scope="col" class="bold txtC">
                            	기타의견
                            </th>
                            <td>
                            	<textarea name="" cols="90" rows="15" class="input"></textarea>
                            </td>
                        </tr>
    </c:if>
                        
                        <tr>
                            <th colspan="2">
                            	(${v_idx})[${result.distcodenm}] ${result.sultext}
                            </th>                            
                        </tr>
                        
	<c:set var="old_sultype">${result.sultype}</c:set>                        
</c:if>
                        
                    
<!--                        단일선택-->
               		<c:if test="${result.sultype eq '1'}">
               			<tr>
                            <th scope="col" class="bold txtC">
                            <input type="radio" name="" value="">
                            </th>
                            <td>
                            	${result.selnum}. ${result.seltext}
                            </td>
                        </tr>
               		</c:if>
<!--                        복수선택-->
               		<c:if test="${result.sultype eq '2'}">
               			<tr>
                            <th scope="col" class="bold txtC">
                            <input type="checkbox" name="" value="">
                            </th>
                            <td>
                            	${result.selnum}. ${result.seltext}
                            </td>
                        </tr>
               		</c:if>
<!--                        서술형-->
               		<c:if test="${result.sultype eq '3'}">
               			<tr>
               				<th scope="col" class="bold txtC">
                            	서술형
                            </th>
                            <td>
                            	<textarea name="" cols="90" rows="15" class="input"></textarea>
                            </td>
                        </tr>
               		</c:if>
<!--                        복합형-->
               		<c:if test="${result.sultype eq '4'}">
               			<tr>
                            <th scope="col" class="bold txtC">
                            <input type="radio" name="" value="">
                            </th>
                            <td>
                            	${result.selnum}. ${result.seltext}
                            </td>
                        </tr>
                        
               		</c:if>               		               		
<!--                        5점척도-->
               		<c:if test="${result.sultype eq '5'}">
               			<tr>
                            <th scope="col" class="bold txtC">
                            <input type="radio" name="" value="">
                            </th>
                            <td>
                            	${result.selnum}. ${result.seltext}
                            </td>
                        </tr>
               		</c:if>               		       
<!--                        7점척도-->
               		<c:if test="${result.sultype eq '6'}">
               			<tr>
                            <th scope="col" class="bold txtC">
                            <input type="radio" name="" value="">
                            </th>
                            <td>
                            	${result.selnum}. ${result.seltext}
                            </td>
                        </tr>
               		</c:if>             		               		
               		
               		 
               		
               		
</c:forEach>
                    </tbody>
                </table>
    </div>
        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
            <li><a href="#none" onclick="window.close();" class="pop_btn01"><span>닫기</span></a></li>            
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