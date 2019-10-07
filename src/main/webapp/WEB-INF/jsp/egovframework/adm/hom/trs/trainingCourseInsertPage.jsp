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
                    <th scope="row">구분</th>
                    <td scope="row">
                    	<input type="text" name="p_gubun" size="20" maxlength="10" value="${view.gubun}">
                    </td>
                </tr>                
                <tr>
                    <th scope="row">과정명</th>
                    <td scope="row">
                    	<input type="text" name="p_coursenm" size="50" maxlength="50" value="${view.coursenm}">							
                    </td>
                </tr>                
                <tr>
                    <th scope="row">시간</th>
                    <td scope="row">
                    	<input type="text" name="p_edu_time" size="3" maxlength="3" value="${view.eduTime}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">대상</th>
                    <td scope="row">
                    	<input type="text" name="p_edu_target" size="30" maxlength="15" value="${view.eduTarget}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">통합교육</th>
                    <td scope="row">
                    	<input type="text" name="p_total_edu" size="5" maxlength="5" value="${view.totalEdu}">
                    </td>
                </tr>
                <tr>
                    <th scope="row">보조인력</th>
                    <td scope="row">
                    	<input type="text" name="p_assist" size="5" maxlength="5" value="${view.assist}">
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
             
        if (blankCheck(thisForm.p_gubun.value)) {
            alert("구분을 입력하세요");
            thisForm.p_gubun.focus();
            return;
        }
        
        if (blankCheck(thisForm.p_coursenm.value)) {
            alert("과정명을 입력하세요");
            thisForm.p_coursenm.focus();
            return;
        }
        
        if (blankCheck(thisForm.p_edu_time.value)) {
            alert("시간을 입력하세요");
            thisForm.p_edu_time.focus();
            return;
        }
        
        if (blankCheck(thisForm.p_edu_target.value)) {
            alert("대상을 입력하세요");
            thisForm.p_edu_target.focus();
            return;
        }
        
        thisForm.action = "/adm/hom/trs/trainingCourseActionPage.do";
        thisForm.p_process.value = mode;
        thisForm.target = "_self";
        thisForm.submit();
    }
	
		
</script>