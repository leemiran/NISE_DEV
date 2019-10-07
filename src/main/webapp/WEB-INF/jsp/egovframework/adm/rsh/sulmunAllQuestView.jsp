<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>


<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">
		<input type="hidden" name="p_process" value="">
        <input type="hidden" name="p_gubun"   value="ALL">
        <input type="hidden" name="p_subj"    value="">
        <input type="hidden" name="p_grcode"  value="ALL">
		<input type="hidden" name="p_sulnum"  value="${p_sulnum}">
        <input type="hidden" name="p_scalecode"  value="">
		<input type="hidden" name="p_reloadlist"  value="">
        
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>설문문제관리</h2>
         </div>
		<!-- contents -->
        
		<div>
			<p class="pop_tit">설문문제</p>			
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
                            <th scope="col">설문분류</th>
                            <td>
                                <ui:code id="p_distcode" selectItem="${view.distcode}" gubun="" codetype="0054" levels="1" 
                                upper="" title="설문분류" className="" type="select" selectTitle="::선택::" event="selecttarget()" />
                            </td>
                            <th>문제분류</th>
                            <td>
                            	
                                <SELECT name="p_sultype" onchange="display();"> 
						            <option value="1" <c:if test="${view.sultype eq '1'}">selected</c:if>>단일선택</option> 
						            <option value="2" <c:if test="${view.sultype eq '2'}">selected</c:if>>복수선택</option> 
						            <option value="3" <c:if test="${view.sultype eq '3'}">selected</c:if>>서술형</option> 
						            <option value="4" <c:if test="${view.sultype eq '4'}">selected</c:if>>복합형</option> 
						            <option value="5" <c:if test="${view.sultype eq '5'}">selected</c:if>>5점척도</option> 
						            <option value="6" <c:if test="${view.sultype eq '6'}">selected</c:if>>7점척도</option> 
						          </SELECT>
                            </td>
                        </tr>
                        <tr>
                            <th scope="col">답변유형</th>
                            <td colspan="3">
                              <SELECT name="p_sulreturn"> 
					            <option value="Y" <c:if test="${view.sulreturn eq 'Y'}">selected</c:if>>필수</option> 
					            <option value="N" <c:if test="${view.sulreturn eq 'N'}">selected</c:if>>선택</option> 
					          </SELECT>
                            </td>
                        </tr>
                        <tr>
                            <th scope="col">문제</th>
                            <td colspan="3">
                            <textarea name="p_sultext" cols="80" rows="2" class="input">${view.sultext}</textarea>
                            </td>
                        </tr>					
                    </tbody>
                </table>
          </div>
          
          
          
          
          
          
      
          <div class="popCon MR20" id="sultype1">
         	<input type="hidden" name="p_selmax1"  value="">
	    	<input type="hidden" name="p_scalecode1"  value="">	
	    	<input type="hidden" name="p_selcount1"  value="">
	    	
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="%" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th colspan="2" scope="col">보기</th>
                        </tr>
<c:forEach var="i" begin="1" end="10">                        
                        <tr>
                            <th scope="col" class="bold">${i}번</th>
                            <td>
<c:set var="v_seltext"></c:set>
<c:set var="v_selpoint">0</c:set>      

<c:if test="${view.sultype eq '1'}">                      
	<c:forEach items="${list}" var="result" varStatus="status">
		<c:if test="${result.selnum eq i}">
			<c:set var="v_seltext">${result.seltext}</c:set>
			<c:set var="v_selpoint">${result.selpoint}</c:set>	
		</c:if>
	</c:forEach>
</c:if>
                            <input name="_Array_p_seltext1" type="text" class="input" size="80" value='${v_seltext}'>
			 				<input type="hidden" name="_Array_p_selpoint1"  value="${v_selpoint}">
                            </td>
                        </tr>	
</c:forEach>
                    </tbody>
                </table>
          </div>
        
        
        
        
        
        
          <div class="popCon MR20" id="sultype2">
         	<input type="hidden" name="p_scalecode2"  value="">	
	    	<input type="hidden" name="p_selcount2"  value="">
	    	
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="5%" />
                        <col width="%" />
                        <col width="10%" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th colspan="2" scope="col">복수 선택수</th>
                            <td colspan="2">
                            <SELECT name="p_selmax2"> 
                            <c:forEach var="i" begin="1" end="10"> 
					            <option value="${i}" <c:if test="${view.selmax eq i}">selected</c:if>>${i}</option> 
					        </c:forEach>
					          </SELECT> 
                            </td>
                        </tr>
                        <tr>
                            <th colspan="4" scope="col">보기</th>
                        </tr>
<c:forEach var="i" begin="1" end="10">                        
                        <tr>
                            <th scope="col" class="bold">${i}번</th>
                            <td colspan="3">
<c:set var="v_seltext"></c:set>
<c:set var="v_selpoint">0</c:set>      

<c:if test="${view.sultype eq '2'}">                      
	<c:forEach items="${list}" var="result" varStatus="status">
		<c:if test="${result.selnum eq i}">
			<c:set var="v_seltext">${result.seltext}</c:set>
			<c:set var="v_selpoint">${result.selpoint}</c:set>	
		</c:if>
	</c:forEach>
</c:if>                            
                            <input name="_Array_p_seltext2" type="text" class="input" size="80" value='${v_seltext}'>
			 				<input type="hidden" name="_Array_p_selpoint2"  value="${v_selpoint}">
                            </td>
                        </tr>	
</c:forEach>
                    </tbody>
                </table>
          </div>
        
        
        
        
        <div class="popCon MR20" id="sultype3">
	        <input type="hidden" name="p_selmax3"  value="">
		    <input type="hidden" name="p_selcount3"  value="">
		    <input type="hidden" name="p_scalecode3"  value="">	
		    <input type="hidden" name="_Array_p_seltext3"  value="">	
	    	<input type="hidden" name="_Array_p_selpoint3"  value="0">
        </div>
        
        
        
        <div class="popCon MR20" id="sultype4">
         	<input type="hidden" name="p_selmax4"  value="">
		    <input type="hidden" name="p_scalecode4"  value="">	
		    <input type="hidden" name="p_selcount4"  value="">
	    	
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="%" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th colspan="2" scope="col">보기</th>
                        </tr>
<c:forEach var="i" begin="1" end="10">                        
                        <tr>
                            <th scope="col" class="bold">${i}번</th>
                            <td>
<c:set var="v_seltext"></c:set>
<c:set var="v_selpoint">0</c:set>      

<c:if test="${view.sultype eq '4'}">                      
	<c:forEach items="${list}" var="result" varStatus="status">
		<c:if test="${result.selnum eq i}">
			<c:set var="v_seltext">${result.seltext}</c:set>
			<c:set var="v_selpoint">${result.selpoint}</c:set>	
		</c:if>
	</c:forEach>
</c:if>                            
                            <input name="_Array_p_seltext4" type="text" class="input" size="80" value='${v_seltext}'>
			 				<input type="hidden" name="_Array_p_selpoint4"  value="${v_selpoint}">
                            </td>
                        </tr>	
</c:forEach>
                    </tbody>
                </table>
          </div>
        
        
        
        
        
          <div class="popCon MR20" id="sultype5">
         	<input type="hidden" name="p_selmax5"  value="">
	    	<input type="hidden" name="p_selcount5"  value="">
	    	
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="5%" />
                        <col width="%" />
                        <col width="10%" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="col">척도명</th>
                            <td colspan="3">
                            
                            <ui:code id="p_scalecode5" selectItem="${view.scalecode}" gubun="sulScale" codetype="5" levels="" 
                                upper="" title="척도명" className="" type="select" selectTitle="::척도를 선택하세요::" event="changeScale(1)" />
                            
                            </td>
                        </tr>
                        <tr>
                            <th colspan="4" scope="col">보기</th>
                        </tr>
<c:forEach var="i" begin="1" end="5">  
<c:set var="v_seltext"></c:set>
<c:set var="v_selpoint">0</c:set>      

<c:if test="${view.sultype eq '5'}">                      
	<c:forEach items="${list}" var="result" varStatus="status">
		<c:if test="${result.selnum eq i}">
			<c:set var="v_seltext">${result.seltext}</c:set>
			<c:set var="v_selpoint">${result.selpoint}</c:set>	
		</c:if>
	</c:forEach>
</c:if>
                        <tr>
                            <th scope="col" class="bold">${i}번</th>
                            <td colspan="2">
                            	<input name="_Array_p_seltext5" type="text" class="input" size="80" value='${v_seltext}'>
                            </td>
                            <td>
                            	<input name="_Array_p_selpoint5" type="text" class="input" size="3" value='${v_selpoint}' maxlength="3" readonly> 점
                            </td>
                        </tr>	
</c:forEach>
                    </tbody>
                </table>
          </div>
                
                
                
          <div class="popCon MR20" id="sultype6">
         	<input type="hidden" name="p_selmax6"  value="">
	    	<input type="hidden" name="p_selcount6"  value="">
	    	
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="5%" />
                        <col width="%" />
                        <col width="10%" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="col">척도명</th>
                            <td colspan="3">
                            <ui:code id="p_scalecode6" selectItem="${view.scalecode}" gubun="sulScale" codetype="7" levels="" 
                                upper="" title="척도명" className="" type="select" selectTitle="::척도를 선택하세요::" event="changeScale(2)" />
                            
                            </td>
                        </tr>
                        <tr>
                            <th colspan="4" scope="col">보기</th>
                        </tr>
<c:forEach var="i" begin="1" end="7"> 
<c:set var="v_seltext"></c:set>
<c:set var="v_selpoint">0</c:set>      

<c:if test="${view.sultype eq '6'}">                      
	<c:forEach items="${list}" var="result" varStatus="status">
		<c:if test="${result.selnum eq i}">
			<c:set var="v_seltext">${result.seltext}</c:set>
			<c:set var="v_selpoint">${result.selpoint}</c:set>	
		</c:if>
	</c:forEach>
</c:if>                       
                        <tr>
                            <th scope="col" class="bold">${i}번</th>
                            <td colspan="2">
                            	<input name="_Array_p_seltext6" type="text" class="input" size="80" value='${v_seltext}'>
                            </td>
                            <td>
                            	<input name="_Array_p_selpoint6" type="text" class="input" size="3" value='${v_selpoint}' maxlength="3" readonly> 점
                            </td>
                        </tr>	
</c:forEach>
                    </tbody>
                </table>
          </div>
        
        
        
        
        
        
        
        
        
        
        
        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
		<c:if test="${empty p_sulnum}">
			<li><a href="#none" onclick="doAction('insert')" class="pop_btn01"><span>등록</span></a></li>
		</c:if>	
		<c:if test="${not empty p_sulnum}">
			<li><a href="#none" onclick="doAction('update')" class="pop_btn01"><span>수정</span></a></li>
			<li><a href="#none" onclick="doAction('delete')" class="pop_btn01"><span>삭제</span></a></li>
		</c:if>			
			<li><a href="#none" onclick="window.close()" class="pop_btn01"><span>닫기</span></a></li>            
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

	
	function display() {
	    
		var v_sultype = thisForm.p_sultype.options[thisForm.p_sultype.selectedIndex].value;
		
		if(v_sultype=="1"){ 
			sultype1.style.display="";
	        sultype2.style.display="none";
	        sultype3.style.display="none";
	        sultype4.style.display="none";
	        sultype5.style.display="none";
	        sultype6.style.display="none";
	    }else if(v_sultype=="2"){ 
			sultype1.style.display="none";
	        sultype2.style.display="";
	        sultype3.style.display="none";
	        sultype4.style.display="none";
	        sultype5.style.display="none";
	        sultype6.style.display="none";
	    }else if(v_sultype=="3"){ 
			sultype1.style.display="none";
	        sultype2.style.display="none";
	        sultype3.style.display="";
	        sultype4.style.display="none";
	        sultype5.style.display="none";
	        sultype6.style.display="none";
	    }else if(v_sultype=="4"){ 
			sultype1.style.display="none";
	        sultype2.style.display="none";
	        sultype3.style.display="none";
	        sultype4.style.display="";
	        sultype5.style.display="none";
	        sultype6.style.display="none";
	    }else if(v_sultype=="5"){ 
			sultype1.style.display="none";
	        sultype2.style.display="none";
	        sultype3.style.display="none";
	        sultype4.style.display="none";
	        sultype5.style.display="";
	        sultype6.style.display="none";
	    }else if(v_sultype=="6"){ 
			sultype1.style.display="none";
	        sultype2.style.display="none";
	        sultype3.style.display="none";
	        sultype4.style.display="none";
	        sultype5.style.display="none";
	        sultype6.style.display="";
	    }
	}	

	window.onlaod = display();

	function selecttarget(){
			if(thisForm.p_distcode.options[thisForm.p_distcode.selectedIndex].value == "1") {
				thisForm.p_sultype.value= '5';
				display();
			}else {}
	}

	function changeScale(flag) {

		  if(flag == 1){
		  var v_scalecode = thisForm.p_scalecode5.options[thisForm.p_scalecode5.selectedIndex].value;
		  }else{
		  var v_scalecode = thisForm.p_scalecode6.options[thisForm.p_scalecode6.selectedIndex].value;
		  }

		  if(v_scalecode > 0){
				  $.ajax({  
			   			url: "/adm/rsh/scaleViewAjax.do",  
			   			data: {p_scalecode : v_scalecode},
			   			dataType: 'json',
			   			contentType : "application/json:charset=utf-8",
			   			success: function(data) {   
			   				data = data.result;
			   				for (var i = 0; i < data.length; i++) {
			   					var seltext = data[i].seltext;
			   					var selpoint = data[i].selpoint;
								//5점
								if(flag == 1)
								{
				   					if(thisForm._Array_p_seltext5)
				   					{
				   						if(thisForm._Array_p_seltext5.length)
				   						{
				   							thisForm._Array_p_seltext5[i].value = seltext;
				   							thisForm._Array_p_selpoint5[i].value = selpoint;
				   						}
				   						else
				   						{
				   							thisForm._Array_p_seltext5.value = seltext;
				   							thisForm._Array_p_selpoint5.value = selpoint;
				   						}
				   					}
								}
								//7점
								else
								{
									if(thisForm._Array_p_seltext6)
				   					{
				   						if(thisForm._Array_p_seltext6.length)
				   						{
				   							thisForm._Array_p_seltext6[i].value = seltext;
				   							thisForm._Array_p_selpoint6[i].value = selpoint;
				   						}
				   						else
				   						{
				   							thisForm._Array_p_seltext6.value = seltext;
				   							thisForm._Array_p_selpoint6.value = selpoint;
				   						}
				   					}
								}
			   				}
			   				
			   			},    
			   			error: function(xhr, status, error) {   
			   				alert(status);   
			   				alert(error);    
			   			}   
			   		});   
		   }
	}


	function chkData() {

		 	if(thisForm.p_distcode.value=="") {
			  	alert("설문분류를 선택하십시요.");
			  	return false;
			  }
			  
		
		  if (blankCheck(thisForm.p_sultext.value)) {
		    thisForm.p_sultext.focus();
		    alert('설문문제명을  입력하십시요.');
		    return false;
		  }
		  
			//alert(thisForm.p_distcode.value);
		  if(thisForm.p_distcode.value == "1") {	//과정만족도 인 경우 문제분류가 5점척도만 선택해야한다. 
		  //alert(thisForm.p_sultype.options[thisForm.p_sultype.selectedIndex].value);
		  	  if(!(thisForm.p_sultype.options[thisForm.p_sultype.selectedIndex].value == "5") ) {
		  		  alert("과정만족도는 5점 척도만 가능합니다.");
		  		  return false;
		  	  }
		  }
		  
		  var v_sultype = thisForm.p_sultype.options[thisForm.p_sultype.selectedIndex].value;
		  var v_selcount = 0;
		  var v_seltextcnt = 0;

		  if (v_sultype == "1"){
		    for (k=0; k<thisForm._Array_p_seltext1.length; k++) {
		      if (!blankCheck(thisForm._Array_p_seltext1[k].value)) {
		        v_seltextcnt++;
		      }
		    }
		    if (v_seltextcnt == 0) {
		      alert('설문문제 보기를  입력하십시요.');
		      return false;
		     }     
			v_selcount = v_seltextcnt;
			thisForm.p_selcount1.value = v_selcount;

		  } else if (v_sultype == "2") {
		    for (k=0; k<thisForm._Array_p_seltext2.length; k++) {
		      if (!blankCheck(thisForm._Array_p_seltext2[k].value)) {
		        v_seltextcnt++;
		      }
		    }
		    if (v_seltextcnt == 0) {
		      alert('설문문제 보기를  입력하십시요.');
		      return false;
		     }     

			if (v_seltextcnt < thisForm.p_selmax2.value) {
		      alert('복수 선택수가 보기보다 많습니다.');
		      return false;	 
			 }    
			v_selcount = v_seltextcnt;
			thisForm.p_selcount2.value = v_selcount;

		  } else if (v_sultype == "4") {
		    for (k=0; k<thisForm._Array_p_seltext4.length; k++) {
		      if (!blankCheck(thisForm._Array_p_seltext4[k].value)) {
		        v_seltextcnt++;
		      }
		    }
		    if (v_seltextcnt == 0) {
		      alert('설문문제 보기를  입력하십시요.');
		      return false;
		     }     
			v_selcount = v_seltextcnt;
			thisForm.p_selcount4.value = v_selcount;
		  }  else if (v_sultype == "5") {
			if (1 > thisForm.p_scalecode5.value || thisForm.p_scalecode5.value=="") {
		      alert('척도을 선택해 주세요,');
		      return false;	 
			 }    
			thisForm.p_selcount5.value = "5";
		  }  else if (v_sultype == "6") {
			if (1 > thisForm.p_scalecode6.value || thisForm.p_scalecode6.value=="") {
		      alert('척도을 선택해 주세요,');
		      return false;	 
			 }    
			thisForm.p_selcount6.value = "7";
		  }

		  return true;
		}



	
	function doAction(p_process) {
	  if(!chkData()) {
         return;
      }
      var msg = "설문을 ";
      if(p_process == 'insert') msg += "등록하시겠습니까?";
      if(p_process == 'update') msg += "수정하시겠습니까?";
      if(p_process == 'delete') msg += "삭제하시겠습니까?";

      
      if(confirm(msg))
      {
		  thisForm.p_process.value = p_process;
		  thisForm.p_reloadlist.value = 'true';
		  thisForm.action = "/adm/rsh/sulmunAllQuestAction.do";
		  thisForm.target = "_self";
		  thisForm.submit();
      }
	}
</script>