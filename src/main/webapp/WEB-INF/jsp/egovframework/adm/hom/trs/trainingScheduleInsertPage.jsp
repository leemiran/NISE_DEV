<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<script type="text/javascript" src="<%=request.getContextPath()%>/smartEditor/js/HuskyEZCreator.js" charset="utf-8"></script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type = "hidden" name="p_seq"     value = "${p_seq}" />
	<input type = "hidden" name="p_iseq"     value = "${p_iseq}" />
    <input type = "hidden" name="p_process"  value = "" />
    
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="30%" />
                <col width="70%" />
            </colgroup>
            
            <tbody>           
           		<tr>
                    <th scope="row">사용유무</th>
                    <td scope="row">
                    	<c:set var="p_useyn">Y</c:set>
                    	<c:if test="${not empty view.useYn}">
                    		<c:set var="p_useyn">${view.useYn}</c:set>
                    	</c:if>
                    	
                    	
                    	<ui:code id="p_use" selectItem="${p_useyn}" gubun="defaultYn" codetype=""  upper="" title="사용유무" className="" type="radio" selectTitle="" event="" />
                    </td>
                </tr>	
                <tr>
                    <th scope="row">월</th>
                    <td scope="row">
                    	<select name="p_mon" id="p_mon">
							<option value="">월 선택</option>													
							<option value="1" <c:if test="${'1' eq view.mon}">selected</c:if>>1</option>
							<option value="2" <c:if test="${'2' eq view.mon}">selected</c:if>>2</option>
							<option value="3" <c:if test="${'3' eq view.mon}">selected</c:if>>3</option>
							<option value="4" <c:if test="${'4' eq view.mon}">selected</c:if>>4</option>
							<option value="5" <c:if test="${'5' eq view.mon}">selected</c:if>>5</option>
							<option value="6" <c:if test="${'6' eq view.mon}">selected</c:if>>6</option>
							<option value="7" <c:if test="${'7' eq view.mon}">selected</c:if>>7</option>
							<option value="8" <c:if test="${'8' eq view.mon}">selected</c:if>>8</option>
							<option value="9" <c:if test="${'9' eq view.mon}">selected</c:if>>9</option>
							<option value="10" <c:if test="${'10' eq view.mon}">selected</c:if>>10</option>
							<option value="11" <c:if test="${'11' eq view.mon}">selected</c:if>>11</option>
							<option value="12" <c:if test="${'12' eq view.mon}">selected</c:if>>12</option>
						</select>
                    </td>
                </tr>                
                <tr>
                    <th scope="row">과정</th>
                    <td scope="row">                    	
                    	<input type="text" name="p_course" size="30" maxlength="30" value="${view.course}">							
                    </td>
                </tr>                
                <tr>
                    <th scope="row">등록기간</th>
                    <td scope="row">
                    	<input type="text" name="p_reg_term" size="30" maxlength="30" value="${view.regTerm}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">연수기간(주)</th>
                    <td scope="row">
                    	<input type="text" name="p_training_term" size="30" maxlength="30" value="${view.trainingTerm}">
                    </td>
                </tr>
            </tbody>
        </table>				
    </div>
    
    
    
    <!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>취소</span></a></div>
	    
<c:if test="${empty p_iseq}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('insert')"><span>저장</span></a></div>
</c:if>

<c:if test="${not empty p_iseq}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('update')"><span>저장</span></a></div>
</c:if>	    
		
		
	</div>
	
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/trs/selectTrainingList.do"
		frm.target = "_self";
		frm.submit();
	}
	
	function whenNoticeSave(mode) {
             
        if (blankCheck(thisForm.p_mon.value)) {
            alert("월을 선택하세요");
            thisForm.p_mon.focus();
            return;
        }
        
        if (blankCheck(thisForm.p_reg_term.value)) {
            alert("등록기간을 입력하세요");
            thisForm.p_reg_term.focus();
            return;
        }
        
        if (blankCheck(thisForm.p_training_term.value)) {
            alert("연수기간(주)를 입력하세요");
            thisForm.p_training_term.focus();
            return;
        }
        
        thisForm.action = "/adm/hom/trs/trainingScheduleActionPage.do";
        thisForm.p_process.value = mode;
        thisForm.target = "_self";
        thisForm.submit();
    }
	
		
</script>