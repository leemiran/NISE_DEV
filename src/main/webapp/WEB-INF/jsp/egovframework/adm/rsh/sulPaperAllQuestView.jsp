<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>


<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">
		<input type="hidden" name="p_process"     value="">
        <input type="hidden" name="p_action"      value="">
        <input type="hidden" name="p_grcode"      value="ALL">
        <input type="hidden" name="p_gubun"       value="ALL">
        <input type="hidden" name="p_sulpapernum" value="${p_sulpapernum}">
        <input type="hidden" name="p_totcnt"      value="0">
        <input type="hidden" name="p_sulnums"     value="">
        <input type="hidden" name="p_sulstart"   value="">
        <input type="hidden" name="p_sulend"     value="">
		<input type="hidden" name="p_reloadlist"  value="">
		
		
		
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>
			설문지관리
			<c:if test="${view.sulapplycnt > 0}">
			<font color="red">( 응시자가 있는 설문지입니다. 수정/삭제가 불가능합니다.)</font>
        	</c:if>
			
			</h2>
         </div>
		<!-- contents -->
       
        
        
        
        
		<!-- search wrap -->
		<div class="searchWrap txtL" style="margin:10px;">
			<ul class="datewrap">
				<li><strong class="shTit">설문지명 : </strong>
 				  <input name="p_sulpapernm" type="text" class="input" value="${view.sulpapernm}" size="80" >
				</li>
				<li class="floatL ML20"><strong class="shTit">설문타입 : </strong>
				
	  				<select name="p_sultype">
	               		<option value="1" <c:if test="${view.sultype eq '1'}">selected</c:if>>과정설문</option>
	               		<option value="2" <c:if test="${view.sultype eq '1'}">selected</c:if>>사전설문</option>
	               		<option value="3" <c:if test="${view.sultype eq '1'}">selected</c:if>>사후설문</option>
                 	</select>
                 	
				</li>
                <li class="floatL ML20"><strong class="shTit">필수여부 : </strong>
                
	  				<select id="p_proposetype" name="p_sulgubun" style="width:50px;">
	                    <option value='Y' <c:if test="${view.sulgubun eq 'Y'}">selected</c:if>>Y</option>
	                    <option value='N' <c:if test="${view.sulgubun eq 'N'}">selected</c:if>>N</option>
                	</select>
                	
				</li>                
	    </ul>			
	    </div>
        
		<!-- //search wrap -->
        <div class="listTop MR10" style="padding-bottom:0px;">				
                <div class="btnR MR05">
               		<a href="#none" onclick="window.close()" class="btn01"><span>닫기</span></a>
                </div>  
<c:if test="${not empty p_sulpapernum}">   
                <div class="btnR MR05">
               		<a href="#none" onclick="doAction('delete')" class="btn01"><span>삭제</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="#none" onclick="doAction('update')" class="btn01"><span>수정</span></a>
                </div>
</c:if>
<c:if test="${empty p_sulpapernum}"> 
                <div class="btnR MR05">
               		<a href="#none" onclick="doAction('insert')" class="btn01"><span>등록</span></a>
                </div>
</c:if>                
   		</div>
      
          <div class="popCon MR20">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="48%" />
                        <col width="48%" />
                        <col width="4%" />                        				
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="col" class="txtC">설문문제</th>
                            <th scope="col" class="txtC">선택문제</th>
                            <th scope="col" class="txtC">순서</th>
                        </tr>
                        <tr>
                            <td class="txtC">
                            		<select name="p_questions" size="25" style="width:99%;" onchange="ChooseQuestion();">
<c:forEach items="${list}" var="result" varStatus="status">
            						<option value="${result.sulnum}">(${result.sulnum}) [${result.distcodenm}][${result.sultypenm}] ${result.sultext}</option>                          
</c:forEach>                     
                                    </select>
                            </td>
                            <td>
                            		<select name="p_choosed" size="25" style="width:99%;">
<c:forEach items="${sulList}" var="result" varStatus="status">
            						<option value="${result.sulnum}">${status.count} - (${result.sulnum}) [${result.distcodenm}][${result.sultypenm}] ${result.sultext}</option>                          
</c:forEach>                            		
                            		</select>
                            </td>
                            <td>
                            		<a href="#none" onclick="move(thisForm.p_choosed.selectedIndex,-1)" class="pop_btn01"><span>↑</span></a>
                            		<br/><br/>
                            		<a href="#none" onclick="move(thisForm.p_choosed.selectedIndex,+1)" class="pop_btn01"><span>↓</span></a>
                            </td>
                        </tr>                        					
                    </tbody>
                </table>
    	</div>        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#none" onclick="CancelChoosedQuestion()" class="pop_btn01"><span>문항삭제</span></a></li>
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

	//문항체크
	function SulApplyCheck() {
		var cnt = 0;
		if('<c:out value="${view.sulapplycnt}"/>' != '') cnt = parseInt('<c:out value="${view.sulapplycnt}"/>');
		
		if(cnt > 0)
		{
			alert("응시자가 있는 설문지입니다. 수정/삭제가 불가능합니다.");
			return false;
		}
		else
		{
			return true;
		}

	}
	
	// 문항삭제
	function CancelChoosedQuestion() {
		if(SulApplyCheck())
		{
		    var boxLength = thisForm.p_choosed.length;
		    arrSelected = new Array();
		    var count = 0;
		    for (i = 0; i < boxLength; i++) {
		        if (thisForm.p_choosed.options[i].selected) {
		            arrSelected[count] = thisForm.p_choosed.options[i].value;
		        }
		        count++;
		    }
		    var x;
		    for (i = 0; i < boxLength; i++) {
		        for (x = 0; x < arrSelected.length; x++) {
		            if (thisForm.p_choosed.options[i].value == arrSelected[x]) {
		                thisForm.p_choosed.options[i] = null;
		            }
		        }
		        boxLength = thisForm.p_choosed.length;
		    }
		}
	}
	
	// 문항 이동
	function move(index,to) {
		if(SulApplyCheck())
		{
		    var list = thisForm.p_choosed;
		    var total = list.options.length-1;
		    if (index == -1) return false;
		    if (to == +1 && index == total) return false;
		    if (to == -1 && index == 0) return false;
		    var items = new Array;
		    var values = new Array;
	
		    for (i = total; i >= 0; i--) {
		        items[i] = list.options[i].text;
		        values[i] = list.options[i].value;
		    }
	
		    for (i = total; i >= 0; i--) {
		        if (index == i) {
		            list.options[i + to] = new Option(items[i],values[i], 0, 1);
		            list.options[i] = new Option(items[i + to], values[i + to]);
		            i--;
		        } else {
		            list.options[i] = new Option(items[i], values[i]);
		        }
		    }
		    list.focus();
		}
	}


	// 설문문제 선택
	function ChooseQuestion() {
		if(SulApplyCheck())
		{
		    var boxLength    = thisForm.p_choosed.length;
		    var selectedItem = thisForm.p_questions.selectedIndex;
		    var selectedText = (boxLength+1) + '-' + thisForm.p_questions.options[selectedItem].text;
		    var selectedValue= thisForm.p_questions.options[selectedItem].value;
		    var i;
		    var isNew = true;
		    if (boxLength != 0) {
		        for (i = 0; i < boxLength; i++) {
		            thisitem = thisForm.p_choosed.options[i].value;
		            if (thisitem == selectedValue) {
		                isNew = false;
		                break;
		            }
		        }
		    }
		    if (isNew) {
		        newoption = new Option(selectedText, selectedValue, false, false);
		        thisForm.p_choosed.options[boxLength] = newoption;
		    }
		    thisForm.p_questions.selectedIndex=-1;
		}
	}

	// 선택된 문항여부 체크
	function getChoosedQuestion() {
	    var strValues = "";
	    var boxLength = thisForm.p_choosed.length;
	    var count = 0;
	    if (boxLength != 0) {
	        for (i = 0; i < boxLength; i++) {
	            if (count == 0) {
	                strValues = thisForm.p_choosed.options[i].value;
	            }
	            else {
	                strValues = strValues + "," + thisForm.p_choosed.options[i].value;
	            }
	            count++;
	        }
	    }
	    thisForm.p_sulnums.value = strValues;
	    thisForm.p_totcnt.value  = count;
	    return count;
	}	


	function doAction(p_process) {
		if(SulApplyCheck())
		{
			var choosed = getChoosedQuestion();
		    if (choosed < 1) {
		        alert('선택된 문제가 없습니다.');
		        return;
		    }

			if(thisForm.p_sulpapernm.value == "")
			{
				alert('설문지명을 입력하세요');
				thisForm.p_sulpapernm.focus();
				return;
			}
		      var msg = " ";
		      if(p_process == 'insert') msg += "등록하시겠습니까?";
		      if(p_process == 'update') msg += "수정하시겠습니까?";
		      if(p_process == 'delete') msg += "삭제하시겠습니까?";
	
		      
		      if(confirm(msg))
		      {
				  thisForm.p_process.value = p_process;
				  thisForm.p_reloadlist.value = 'true';
				  thisForm.action = "/adm/rsh/sulPaperAllQuestAction.do";
				  thisForm.target = "_self";
				  thisForm.submit();
		      }
		}
	}
	
</script>