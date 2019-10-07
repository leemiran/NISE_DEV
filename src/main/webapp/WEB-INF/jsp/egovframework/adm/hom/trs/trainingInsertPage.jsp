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
                    <th scope="row">년도</th>
                    <td scope="row">
                    	<select name="ses_search_gyear" id="ses_search_gyear" onchange="search_init(true)">
							<option value="">선택</option>
						<c:forEach items="${year_list}" var="result">							
							<option value="${result.gyear}" <c:if test="${result.gyear eq view.year}">selected</c:if>>${result.gyear}</option>
						</c:forEach>
						</select>
                    </td>
                </tr>                
                <tr>
                    <th scope="row">제목</th>
                    <td scope="row">
                    	<input type="text" name="p_subject" size="100" maxlength="80" value="${view.subject}">
                    </td>
                </tr> 
                <tr>
                    <th scope="row">설명</th>
                    <td scope="row">
                    	<textarea rows="5" cols="100" name="p_contents">${view.contents}</textarea>
                    </td>
                </tr>                
                <tr>
                    <th scope="row">교과목 및 시간 배당 첨부파일</th>
                    <td scope="row">
                    	<c:forEach items="${fileList}" var="result" varStatus="i">
                    	* <a href="#none" onclick="fn_download('${result.realfile}', '${result.savefile}', 'training')">
                    	<c:out value="${empty result.realfile ? result.savefile : result.realfile}"/>
                    	</a> 
                    	[첨부파일 추가 시 이전 파일 자동 삭제]<br/>
                    	</c:forEach>
                    	
                    	<input type="FILE" name="p_file1" size="80"><br/>
                    </td>
                </tr>
            </tbody>
        </table>				
    </div>
    
    
    
    <!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR MR05"><a href="#" class="btn03" onclick="pageList()"><span>취소</span></a></div>
	    
<c:if test="${empty p_seq}">
		<div class="btnR"><a href="#" class="btn02" onclick="whenNoticeSave('insert')"><span>저장</span></a></div>
</c:if>

<c:if test="${not empty p_seq}">
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
             
        if (blankCheck(thisForm.ses_search_gyear.value)) {
            alert("년도를 선택하세요");
            thisForm.ses_search_gyear.focus();
            return;
        }
        
        if (blankCheck(thisForm.p_subject.value)) {
            alert("제목을 입력하세요");
            thisForm.p_subject.focus();
            return;
        }
        
        if (realsize(thisForm.p_subject.value) > 200) {
            alert("제목은 한글기준 100자를 초과하지 못합니다.");
            thisForm.p_subject.focus();
            return;
        }
        
        thisForm.action = "/adm/hom/trs/trainingActionPage.do";
        thisForm.p_process.value = mode;
        thisForm.target = "_self";
        thisForm.submit();
    }
	
		
</script>