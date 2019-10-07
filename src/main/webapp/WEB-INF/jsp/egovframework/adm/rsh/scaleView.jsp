<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>


<c:set var="p_sgubun"></c:set>
<c:set var="p_scalename"></c:set>

<c:forEach items="${list}" var="result" varStatus="status">
	<c:set var="p_sgubun">${result.sGubun}</c:set>
	<c:set var="p_scalename">${result.scalename}</c:set>
</c:forEach>

<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">
	<input type="hidden" name="p_process" value="">
        <input type="hidden" name="p_scaletype"    value="S">
        <input type="hidden" name="p_grcode"  value="ALL">
        <input type="hidden" name="p_scalecode"  value="${p_scalecode}">
        <input type="hidden" name="p_reloadlist"  value="">
        
        
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>설문척도관리</h2>
         </div>
		<!-- contents -->
        
		<div>
			<p class="pop_tit">5.7점 척도</p>			
		</div>
        
            <div class="popCon MR20">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="40%" />
                        <col width="10%" />
                        <col width="40%" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="col">척도분류</th>
                            <td>
                                  <SELECT name="p_sgubun" onchange="display();"> 
						            <option value="5" <c:if test="${p_sgubun eq '5'}">selected</c:if>>5점척도</option> 
						            <option value="7" <c:if test="${p_sgubun eq '7'}">selected</c:if>>7점척도</option> 
						          </SELECT> 
						          
						          <c:if test="${result.sGubun eq '5'}">selected</c:if>	
						          
                            </td>
                        </tr>
                        <tr>
                            <th scope="col">척도명</th>
                            <td><textarea name="p_scalename" cols="80" rows="3"><c:out value="${p_scalename}" escapeXml="false"/></textarea></td>
                        </tr>					
                    </tbody>
                </table>
     	 </div>
      
          <div class="popCon MR20">
                <table summary="" width="100%" class="popTb" ID="sgubun1" STYLE="display:">
                    <colgroup>
                        <col width="5%" />
                        <col width="80%" />
                        <col width="15%" />					
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="col" class="txtC">No.</th>
                            <th scope="col" class="txtC">보기</th>
                            <th scope="col" class="txtC">점수</th>                            
                        </tr>
                        
                        
<c:if test="${empty p_scalecode}">
	<c:forEach begin="1" end="5" var="i">
						<tr>
                            <th scope="col" class="bold">${i}번</th>
                            <td><input name="_Array_p_seltext1" type="text" size="80" value=""></td>
                            <td class="class="txtC"><input name="_Array_p_selpoint1" type="text" class="input" size="3" value=""> 점</td>
                        </tr>
	</c:forEach>
</c:if>

<c:set var="listCount" value="1"/>
<c:if test="${not empty p_scalecode}">                 
	<c:forEach items="${list}" var="result" varStatus="status">
		<c:if test="${p_sgubun eq '7' && listCount <= 5}">
						<tr>
                            <th scope="col" class="bold">${empty result.selnum ? status.count : result.selnum}번</th>
                            <td><input name="_Array_p_seltext1" type="text" size="80" value="${result.seltext}"></td>
                            <td class="class="txtC"><input name="_Array_p_selpoint1" type="text" class="input" size="3" value="${result.selpoint}"> 점</td>
                        </tr>
        </c:if>
        
        <c:if test="${p_sgubun eq '5'}">
						<tr>
                            <th scope="col" class="bold">${empty result.selnum ? status.count : result.selnum}번</th>
                            <td><input name="_Array_p_seltext1" type="text" size="80" value="${result.seltext}"></td>
                            <td class="class="txtC"><input name="_Array_p_selpoint1" type="text" class="input" size="3" value="${result.selpoint}"> 점</td>
                        </tr>
        </c:if>
        
        
         
         <c:set var="listCount">${listCount+1}</c:set>
         
         
         
	</c:forEach>
	
		<c:forEach begin="${listCount}" end="5" var="i">
						<tr>
                            <th scope="col" class="bold">${i}번</th>
                            <td><input name="_Array_p_seltext1" type="text" size="80" value=""></td>
                            <td class="class="txtC"><input name="_Array_p_selpoint1" type="text" class="input" size="3" value=""> 점</td>
                        </tr>
		</c:forEach>
</c:if>
						
                        
                    </tbody>
                </table>
                
                
                <table summary="" width="100%" class="popTb" ID="sgubun2" STYLE="display:">
                    <colgroup>
                        <col width="5%" />
                        <col width="80%" />
                        <col width="15%" />					
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="col" class="txtC">No.</th>
                            <th scope="col" class="txtC">보기</th>
                            <th scope="col" class="txtC">점수</th>                            
                        </tr>
                        
<c:if test="${empty p_scalecode}">
	<c:forEach begin="1" end="7" var="i">
						<tr>
                            <th scope="col" class="bold">${i}번</th>
                            <td><input name="_Array_p_seltext2" type="text" size="80" value=""></td>
                            <td class="class="txtC"><input name="_Array_p_selpoint2" type="text" class="input" size="3" value=""> 점</td>
                        </tr>
	</c:forEach>
</c:if>
<c:if test="${not empty p_scalecode}">                 
	<c:forEach items="${list}" var="result" varStatus="status">
						<tr>
                            <th scope="col" class="bold">${result.selnum}번</th>
                            <td><input name="_Array_p_seltext2" type="text" size="80" value="${result.seltext}"></td>
                            <td class="class="txtC"><input name="_Array_p_selpoint2" type="text" class="input" size="3" value="${result.selpoint}"> 점</td>
                        </tr>
	</c:forEach>
	
	<c:if test="${p_sgubun eq '5'}">
	
						<tr>
                            <th scope="col" class="bold">6번</th>
                            <td><input name="_Array_p_seltext2" type="text" size="80" value=""></td>
                            <td class="class="txtC"><input name="_Array_p_selpoint2" type="text" class="input" size="3" value=""> 점</td>
                        </tr>
                        <tr>
                            <th scope="col" class="bold">7번</th>
                            <td><input name="_Array_p_seltext2" type="text" size="80" value=""></td>
                            <td class="class="txtC"><input name="_Array_p_selpoint2" type="text" class="input" size="3" value=""> 점</td>
                        </tr>
	
	</c:if>
</c:if>
                    </tbody>
                </table>
                
                
      	</div>
        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
<c:if test="${empty p_scalecode}">		
			<li><a href="#none" onclick="doAction('insert')" class="pop_btn01"><span>등록</span></a></li>
</c:if>			
<c:if test="${not empty p_scalecode}">
			<li><a href="#none" onclick="doAction('update')" class="pop_btn01"><span>수정</span></a></li>  
            <li><a href="#none" onclick="doAction('delete')" class="pop_btn01"><span>삭제</span></a></li>
</c:if>            
            <li><a href="#none" onclick="doPageList()" class="pop_btn01"><span>목록</span></a></li>           
		</ul>
 		<!-- // button -->
	</div>
</div>


</form>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>



<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

	if(window.addEventListener) {
	    window.addEventListener("load", display, false);
	} else if(window.attachEvent) {
	    window.attachEvent("onload", display);
	}
		
	function display() {
	    
		var v_sgubun = thisForm.p_sgubun.options[thisForm.p_sgubun.selectedIndex].value;
		if(v_sgubun=="5"){ 
			sgubun1.style.display="";
	        sgubun2.style.display="none";
	    }else if(v_sgubun=="7"){ 
			sgubun1.style.display="none";
	        sgubun2.style.display="";
	    }
	}


	function chkData() 
	{
		  if (blankCheck(thisForm.p_scalename.value)) {
		    thisForm.p_scalename.focus();
		    alert('척도명을  입력하십시요.');
		    return false;
		  }

		  var v_sgubun = thisForm.p_sgubun.options[thisForm.p_sgubun.selectedIndex].value;

		  var v_seltextcnt1 = 0;
		  var v_seltextcnt2 = 0;

		  if (v_sgubun == "5"){
		    for (k=0; k<5; k++) {
		      if (!blankCheck(thisForm._Array_p_seltext1[k].value)) {
		        v_seltextcnt1++;
		      }
		    }
		    if (v_seltextcnt1 < 5) {
		      alert('척도 보기를  입력하십시요.');
		      return false;
		     }
		    for (k=0; k<5; k++) {
		      if (!blankCheck(thisForm._Array_p_selpoint1[k].value)) {
		        v_seltextcnt2++;
		      }
		    }
		    if (v_seltextcnt2 < 5) {
		      alert('척도 점수를  입력하십시요.');
		      return false;
		     }	 
		  } else if (v_sgubun == "7") {
		    for (k=0; k<7; k++) {
		      if (!blankCheck(thisForm._Array_p_seltext2[k].value)) {
		        v_seltextcnt1++;
		      }
		    }
		    if (v_seltextcnt1 < 7) {
		      alert('척도 보기를  입력하십시요.');
		      return false;
		     }
		    for (k=0; k<7; k++) {
		      if (!blankCheck(thisForm._Array_p_selpoint2[k].value)) {
		        v_seltextcnt2++;
		      }
		    }
		    if (v_seltextcnt2 < 7) {
		      alert('척도 점수를  입력하십시요.');
		      return false;
		     }	 
		  }

		  return true;
	}


	function doAction(p_process) {
		  if(!chkData()) {
		    return;
		 }
		  thisForm.p_process.value = p_process;
		  thisForm.p_reloadlist.value = 'true';
		  thisForm.action = "/adm/rsh/scaleAction.do";
		  thisForm.target = "_self";
		  thisForm.submit();
	}


	function doPageList()
	{
		thisForm.action = "/adm/rsh/scaleList.do";
		thisForm.target = "_self";
		thisForm.submit();
	}
			
</script>